package com.fmbank.welfarelottery.service;

import com.baomidou.mybatisplus.service.IService;
import com.fmbank.welfarelottery.entity.BuyRecord;
import com.fmbank.welfarelottery.entity.LotteryRecord;
import com.fmbank.welfarelottery.response.model.CountStatisticsResult;

import java.util.List;

/**
 *
 */
public interface IThreeDRecordService extends IService<LotteryRecord> {

    Integer dataToDbByYear(String year);

    Integer buyDataToDb(String year);

    Object calculateByDate(String date);
}