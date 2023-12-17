package com.fmbank.welfarelottery.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fmbank.welfarelottery.entity.TThreeDBuyRecord;
import com.fmbank.welfarelottery.entity.TThreeDRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 3购买记录表 Mapper 接口
 * </p>
 *
 * @author jianchun
 * @since 2023-12-09
 */
@Mapper
public interface TThreeDBuyRecordMapper extends BaseMapper<TThreeDBuyRecord> {
    Integer insertBatchs(@Param(value = "records") List<TThreeDBuyRecord> records);

}
