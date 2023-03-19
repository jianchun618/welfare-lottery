package com.fmbank.welfarelottery.service;

import com.fmbank.welfarelottery.entity.LotteryRecord;

import java.util.List;

public interface IDataGateway {

    List<LotteryRecord> getLotteryRecord(Integer integer);

    /**
     * 系统生成N组 号码：
     *      规则：6红球，1个蓝球   6个红球范围：1-33 1个蓝球：1-16
     * @param integer
     * @return
     */
    List<LotteryRecord> getDataByRandom(Integer integer);

}
