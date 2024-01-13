package com.fmbank.welfarelottery.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fmbank.welfarelottery.entity.TThreeDRecord;
import com.fmbank.welfarelottery.entity.TThreeSixBuyRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 3组六购买记录表 Mapper 接口
 * </p>
 *
 * @author jianchun
 * @since 2024-01-13
 */
@Mapper
public interface TThreeSixBuyRecordMapper extends BaseMapper<TThreeSixBuyRecord> {
    Integer insertBatchs(@Param(value = "records") List<TThreeSixBuyRecord> records);

}
