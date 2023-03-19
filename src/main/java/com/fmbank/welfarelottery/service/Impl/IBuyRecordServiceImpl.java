package com.fmbank.welfarelottery.service.Impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fmbank.welfarelottery.entity.BuyRecord;
import com.fmbank.welfarelottery.mapper.BuyRecordMapper;
import com.fmbank.welfarelottery.service.IBuyRecordService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 购买记录表 服务实现类
 * </p>
 *
 * @author jianchun
 * @since 2023-03-19
 */
@Service
public class IBuyRecordServiceImpl extends ServiceImpl<BuyRecordMapper, BuyRecord> implements IBuyRecordService {

}
