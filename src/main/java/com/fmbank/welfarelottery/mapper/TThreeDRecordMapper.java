package com.fmbank.welfarelottery.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fmbank.welfarelottery.entity.TThreeDRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 3d开奖记录表 Mapper 接口
 * </p>
 *
 * @author jianchun
 * @since 2023-12-09
 */
@Mapper
public interface TThreeDRecordMapper extends BaseMapper<TThreeDRecord> {
    Integer insertBatchs(@Param(value = "records") List<TThreeDRecord> records);

    List<TThreeDRecord> selectByYear(@Param(value = "year") String year);
    TThreeDRecord selectByDate(@Param(value = "date") String date);
    /*之前的期数数据*/
    TThreeDRecord selectDateByBeforeCode(@Param(value = "date") String date,@Param(value = "lotteryNumber") String lotteryNumber);

}
