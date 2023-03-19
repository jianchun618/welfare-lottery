package com.fmbank.welfarelottery.response.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CountStatisticsResult {
    /**
     * 开奖总数
     */
    private int recordSum;
    /**
     * 去重Map条数
     */
    private int mapSize;
    /**
     * 记录数是否等于map去重条数
     */
    private boolean isEquals;
}
