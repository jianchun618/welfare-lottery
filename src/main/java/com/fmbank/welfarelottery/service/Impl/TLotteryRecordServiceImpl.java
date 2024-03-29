package com.fmbank.welfarelottery.service.Impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fmbank.welfarelottery.entity.BuyRecord;
import com.fmbank.welfarelottery.entity.LotteryRecord;
import com.fmbank.welfarelottery.mapper.BuyRecordMapper;
import com.fmbank.welfarelottery.mapper.LotteryRecordMapper;
import com.fmbank.welfarelottery.response.model.CountStatisticsResult;
import com.fmbank.welfarelottery.service.IDataGateway;
import com.fmbank.welfarelottery.service.ILotteryRecordService;
import com.fmbank.welfarelottery.util.BallRandomUtil;
import com.fmbank.welfarelottery.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TLotteryRecordServiceImpl extends ServiceImpl<LotteryRecordMapper, LotteryRecord> implements ILotteryRecordService {
    @Resource
    IDataGateway iDataGateway;
    @Resource
    LotteryRecordMapper lotteryRecordMapper;
    @Resource
    BuyRecordMapper buyRecordMapper;

    /*外围系统数据入库*/
    @Override
    public Integer dataToDb(Integer integer) {
        List<LotteryRecord> records = iDataGateway.getLotteryRecord(integer);
        return lotteryRecordMapper.insertBatchs(records);
    }

    @Override
    public LotteryRecord showLatestRecordInfo() {
        LotteryRecord lotteryRecord = lotteryRecordMapper.latestRecord();
        return lotteryRecord;
    }

    @Override
    public CountStatisticsResult showRecordSizeAndMapSize() {
        EntityWrapper<LotteryRecord> queryWrapper = new EntityWrapper<>();
        //获取所有数据
        List<LotteryRecord> lotteryRecords = this.baseMapper.selectList(queryWrapper);
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (LotteryRecord lotteryRecord : lotteryRecords) {
            if (hashMap.containsKey(lotteryRecord.getRed())) {
                hashMap.put(lotteryRecord.getRed(), hashMap.get(lotteryRecord.getRed() + 1));
            } else {
                hashMap.put(lotteryRecord.getRed(), 1);
            }
        }
        log.info("lotteryRecords -size-[{}]", lotteryRecords.size());
        log.info("hashMap -size-[{}]", hashMap.size());
        return CountStatisticsResult.builder().recordSum(lotteryRecords.size()).mapSize(hashMap.size())
                .isEquals(lotteryRecords.size() == hashMap.size()).build();
    }

    @Override
    @Transactional
    public Integer dataRandom(Integer integer, String dataDate) {
        if (!DateUtil.isWeekday(dataDate)) {
            log.error("录入的日期，非开奖日期");
            return 0;
        }
        ArrayList<BuyRecord> buyRecords = getBuyRecords(integer, dataDate);
        return buyRecordMapper.insertBatchs(buyRecords);
    }

    private ArrayList<BuyRecord> getBuyRecords(Integer integer, String dataDate) {
        List<String> balls = BallRandomUtil.getDoubleColorBallNumber(integer);
        ArrayList<BuyRecord> buyRecords = new ArrayList<>();
        for (int i = 0; i < balls.size(); i++) {
            String ball = balls.get(i);
            EntityWrapper<LotteryRecord> queryWrapper = new EntityWrapper<>();
            queryWrapper.eq("red", ball);
            List<LotteryRecord> lotteryRecords = this.baseMapper.selectList(queryWrapper);
            if (lotteryRecords.size() > 0) {
                i--;
                continue;
                //LotteryRecord lotteryRecord = lotteryRecords.get(0);
                //throw new RuntimeException("生成的红球号码已被-[" + lotteryRecord.getDate() + "]日开奖过，" + "号码-[" + ball + "]-期数-[" + lotteryRecords.get(0).getCode() + "]");
            }
            BuyRecord buyRecord = new BuyRecord();
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            buyRecord.setDate(dataDate);
            buyRecord.setRed(ball);
            //i小于0，进行拼接0
            int blue = (i + 1) % 16;
            if (0 == blue) {
                blue = 16;
            }
            buyRecord.setBlue(blue < 10 ? "0" + blue : String.valueOf(blue));
            buyRecord.setCreateTime(new Date());
            buyRecord.setModifyTime(new Date());
            buyRecords.add(buyRecord);
        }
        return buyRecords;
    }

    @Override
    public Integer dataInit(String dataString) {
        //组装查询包装类，是否初始化过数据检查。
        EntityWrapper<BuyRecord> queryWrapper = new EntityWrapper<>();
        //String dataString = getDateString();
        queryWrapper.eq("date", dataString);
        List<BuyRecord> initedData = buyRecordMapper.selectList(queryWrapper);
        if (initedData.size() > 0) {
            throw new RuntimeException("当日已完成数据的初始化，请检查！");
        }
        //获取近一期的16条数据
        List<BuyRecord> buyRecords = buyRecordMapper.latestBuyRecord();
        List<BuyRecord> addLists = new ArrayList<>();
        //构建当日购买记录并设置相应的值
        for (BuyRecord his : buyRecords) {
            BuyRecord newRecord = new BuyRecord();
            newRecord.setDate(dataString);
            newRecord.setRed(his.getRed());
            newRecord.setBlue(his.getBlue());
            addLists.add(newRecord);
        }
        //执行入库操作
        if (addLists.size() > 0) {
            return buyRecordMapper.insertBatchs(addLists);
        }
        return 0;
    }

    private String getDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    @Override
    public double cashAPrize(String date) {
        double winningAmount = 0;
        EntityWrapper<LotteryRecord> queryWrapperLotteryRecord = new EntityWrapper<>();
        queryWrapperLotteryRecord.eq("date", date);

//        LotteryRecord latestRecord = lotteryRecordMapper.latestRecord();
        List<LotteryRecord> lotteryRecords = lotteryRecordMapper.selectList(queryWrapperLotteryRecord);
        LotteryRecord latestRecord = null;
        if (ObjectUtils.isEmpty(lotteryRecords)) {
            log.info("未获取到兑奖记录");
            return winningAmount;
        }
        latestRecord = lotteryRecords.get(0);
        log.info("兑奖开奖记录-[{}]", latestRecord);
        String red = latestRecord.getRed();
        String[] split = red.split(",");
        //中奖数据
        ArrayList<String> reds = new ArrayList<>();
        for (String s : split) {
            reds.add(s);
        }
        EntityWrapper<BuyRecord> queryWrapper = new EntityWrapper<>();
//        String dataString = getDateString();
        queryWrapper.eq("date", date);
        List<BuyRecord> buyRecords = buyRecordMapper.selectList(queryWrapper);
        for (BuyRecord buyRecord : buyRecords) {
            Integer redCount = 0;
            Integer blueCount = 0;
            String[] buyReds = buyRecord.getRed().split(",");
            ArrayList<String> hitNumbers = new ArrayList<>();
            for (String buyRed : buyReds) {
                if (reds.contains(buyRed)) {
                    hitNumbers.add(buyRed);
                    redCount++;
                }
            }
            if (latestRecord.getBlue().equals(buyRecord.getBlue())) blueCount++;
            buyRecord.setRedHitTotal(redCount);
            buyRecord.setBlueHitTotal(blueCount);
            if (hitNumbers.size() > 0) {
                buyRecord.setHitNumber(StringUtils.join(hitNumbers, ","));
            }
            double amount = getAmount(redCount, blueCount);
            winningAmount = winningAmount + amount;
            buyRecord.setWinningAmount(amount);
            buyRecord.setResult(latestRecord.getRed());
        }
        if (buyRecords.size() > 0) {
            buyRecordMapper.insertBatchs(buyRecords);
        } else {
            throw new RuntimeException("未获取到当日购买的数据。。。");
        }
        log.info("开奖核对成功！");
        return winningAmount;
    }

    @Override
    public List<BuyRecord> dateData(String dataDate) {
        EntityWrapper<BuyRecord> queryWrapper = new EntityWrapper<>();
//        String dataString = getDateString();
        queryWrapper.eq("date", dataDate);
        return buyRecordMapper.selectList(queryWrapper);
    }

    /**
     * 按年每天生成 n主购买数据
     *
     * @param year
     * @param integer
     * @return
     */
    @Override
    @Transactional
    public Object dataRandomYear(String year, Integer integer) {
        EntityWrapper<LotteryRecord> queryWrapper = new EntityWrapper<>();
        queryWrapper.like("date", year);
        List<LotteryRecord> lotteryRecords = this.baseMapper.selectList(queryWrapper);
        ArrayList<BuyRecord> buyR = new ArrayList<>();
        for (LotteryRecord lotteryRecord : lotteryRecords) {
            ArrayList<BuyRecord> buyRecords = getBuyRecords(integer, lotteryRecord.getDate());
            buyR.addAll(buyRecords);
            log.info("日期-[{}],生成的注数-[],注成功", lotteryRecord.getDate(), integer);
        }
        return buyRecordMapper.insertBatchs(buyR);
    }

    @Override
    public void yearCashAPrize(String year) {
        EntityWrapper<BuyRecord> queryWrapper = new EntityWrapper<>();
        queryWrapper.like("date", year);
        List<BuyRecord> lotteryRecords = buyRecordMapper.selectList(queryWrapper);
        List<String> dates = lotteryRecords.stream().map(item -> item.getDate()).distinct().collect(Collectors.toList());
        log.info("date size is - [{}]", dates.size());
        for (String date : dates) {
            this.cashAPrize(date);
        }
        log.info("deal success - [{}]", dates.size());
    }

    private double getAmount(Integer redCount, Integer blueCount) {
        /*一等奖*/
        if ((redCount == 6 && blueCount == 1)) return 1000000;
        //二等奖
        if ((redCount == 6 && blueCount == 0)) return 50000;
        //三等奖
        if ((redCount == 5 && blueCount == 1)) return 3000;
        //四等奖
        if ((redCount == 5 && blueCount == 0)) return 200;
        if ((redCount == 4 && blueCount == 1)) return 200;
        //五等奖
        if ((redCount == 4 && blueCount == 0)) return 10;
        if ((redCount == 3 && blueCount == 1)) return 10;
        //六等奖
        if ((redCount <= 2 && blueCount == 1)) return 5;
        return 0;
    }

    public static void main(String[] args) {


    }
}
