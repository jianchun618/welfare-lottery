package com.fmbank.welfarelottery.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fmbank.welfarelottery.entity.LotteryRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 开奖记录表 Mapper 接口
 * </p>
 *
 * @author jianchun
 * @since 2023-03-18
 */
@Mapper
public interface LotteryRecordMapper extends BaseMapper<LotteryRecord> {
    Integer insertBatchs(@Param(value = "records") List<LotteryRecord> records);

    LotteryRecord latestRecord();
}
