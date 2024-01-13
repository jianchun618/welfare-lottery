package com.fmbank.welfarelottery.service;

import com.baomidou.mybatisplus.service.IService;
import com.fmbank.welfarelottery.entity.LotteryRecord;
import com.fmbank.welfarelottery.entity.TThreeSixBuyRecord;

/**
 *
 */
public interface IThreeDSixRecordService extends IService<TThreeSixBuyRecord> {

    String buyDataToDb(String year);

}