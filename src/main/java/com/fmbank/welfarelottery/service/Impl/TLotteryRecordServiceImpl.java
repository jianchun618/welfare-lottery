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
            buyRecord.setBlue((i+1)< 10 ? "0" + (i+1) : String.valueOf(i+1));
            buyRecord.setCreateTime(new Date());
            buyRecord.setModifyTime(new Date());
            buyRecords.add(buyRecord);
        }
        return buyRecordMapper.insertBatchs(buyRecords);
    }


}
