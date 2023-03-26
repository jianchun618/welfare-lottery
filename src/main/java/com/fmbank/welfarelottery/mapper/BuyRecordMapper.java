package com.fmbank.welfarelottery.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fmbank.welfarelottery.entity.BuyRecord;
import com.fmbank.welfarelottery.entity.LotteryRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 购买记录表 Mapper 接口
 * </p>
 *
 * @author jianchun
 * @since 2023-03-19
 */
@Mapper
public interface BuyRecordMapper extends BaseMapper<BuyRecord> {
    Integer insertBatchs(@Param(value = "records") List<BuyRecord> records);

    List<BuyRecord> latestBuyRecord();
}
