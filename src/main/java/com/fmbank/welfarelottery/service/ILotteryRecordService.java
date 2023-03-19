package com.fmbank.welfarelottery.service;

import com.baomidou.mybatisplus.service.IService;
import com.fmbank.welfarelottery.entity.LotteryRecord;
import com.fmbank.welfarelottery.response.model.CountStatisticsResult;

/**
 *
 */
public interface ILotteryRecordService extends IService<LotteryRecord> {

    Integer dataToDb(Integer integer);

    LotteryRecord showLatestRecordInfo();

    CountStatisticsResult showRecordSizeAndMapSize();

    Integer dataRandom(Integer integer);

    void cashAPrize();

}