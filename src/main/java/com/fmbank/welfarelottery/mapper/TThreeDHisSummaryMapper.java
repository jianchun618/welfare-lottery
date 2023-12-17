package com.fmbank.welfarelottery.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fmbank.welfarelottery.entity.TThreeDHisSummary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 奖号历史未开奖的期数统计 Mapper 接口
 * </p>
 *
 * @author jianchun
 * @since 2023-12-09
 */
@Mapper
public interface TThreeDHisSummaryMapper extends BaseMapper<TThreeDHisSummary> {
    Integer insertBatchs(@Param(value = "records") List<TThreeDHisSummary> records);

    List<TThreeDHisSummary> selectLastTenData();

}
