package com.fmbank.welfarelottery.service;

import com.baomidou.mybatisplus.service.IService;
import com.fmbank.welfarelottery.entity.BuyRecord;
import com.fmbank.welfarelottery.entity.LotteryRecord;
import com.fmbank.welfarelottery.response.model.CountStatisticsResult;

import java.util.List;

/**
 *
 */
public interface ILotteryRecordService extends IService<LotteryRecord> {

    Integer dataToDb(Integer integer);

    LotteryRecord showLatestRecordInfo();

    CountStatisticsResult showRecordSizeAndMapSize();

    Integer dataRandom(Integer integer,String dataDate);

    Integer dataInit(String dataString);

    double cashAPrize(String date);

    List<BuyRecord> dateData(String dataDate);
}