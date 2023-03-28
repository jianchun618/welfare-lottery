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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    public Integer dataRandom(Integer integer) {
        List<String> balls = BallRandomUtil.getDoubleColorBallNumber(integer);
        ArrayList<BuyRecord> buyRecords = new ArrayList<>();
        for (int i = 0; i < balls.size(); i++) {
            String ball = balls.get(i);
            EntityWrapper<LotteryRecord> queryWrapper = new EntityWrapper<>();
            queryWrapper.eq("red", ball);
            List<LotteryRecord> lotteryRecords = this.baseMapper.selectList(queryWrapper);
            if (lotteryRecords.size() > 0) {
                LotteryRecord lotteryRecord = lotteryRecords.get(0);
                throw new RuntimeException("生成的红球号码已被-[" + lotteryRecord.getDate() + "]日开奖过，" + "号码-[" + ball + "]-期数-[" + lotteryRecords.get(0).getCode() + "]");
            }
            BuyRecord buyRecord = new BuyRecord();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            buyRecord.setDate(sdf.format(new Date()));
            buyRecord.setRed(ball);
            //i小于0，进行拼接0
            buyRecord.setBlue((i + 1) < 10 ? "0" + (i + 1) : String.valueOf(i + 1));
            buyRecord.setCreateTime(new Date());
            buyRecord.setModifyTime(new Date());
            buyRecords.add(buyRecord);
        }
        return buyRecordMapper.insertBatchs(buyRecords);
    }

    @Override
    public Integer dataInit() {
        //组装查询包装类，是否初始化过数据检查。
        EntityWrapper<BuyRecord> queryWrapper = new EntityWrapper<>();
        String dataString = getDateString();
        queryWrapper.eq("date", dataString);
        List<BuyRecord> initedData = buyRecordMapper.selectList(queryWrapper);
        if(initedData.size()>0){
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
        return buyRecordMapper.insertBatchs(addLists);
    }

    private String getDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    @Override
    public double cashAPrize() {
        double winningAmount = 0;
        LotteryRecord latestRecord = lotteryRecordMapper.latestRecord();
        log.info("最近的开奖记录-[{}]", latestRecord);
        String red = latestRecord.getRed();
        String[] split = red.split(",");
        //中奖数据
        ArrayList<String> reds = new ArrayList<>();
        for (String s : split) {
            reds.add(s);
        }
        EntityWrapper<BuyRecord> queryWrapper = new EntityWrapper<>();
        String dataString = getDateString();
        queryWrapper.eq("date", dataString);
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
        if(buyRecords.size()>0){
            buyRecordMapper.insertBatchs(buyRecords);
        }else {
            throw new RuntimeException("未获取到当日购买的数据。。。");
        }
        log.info("开奖核对成功！");
        return winningAmount;
    }

    @Override
    public List<BuyRecord> dateData() {
        EntityWrapper<BuyRecord> queryWrapper = new EntityWrapper<>();
        String dataString = getDateString();
        queryWrapper.eq("date", dataString);
        return buyRecordMapper.selectList(queryWrapper);
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
}
