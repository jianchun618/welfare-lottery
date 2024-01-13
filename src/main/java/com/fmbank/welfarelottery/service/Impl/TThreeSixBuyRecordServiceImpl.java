package com.fmbank.welfarelottery.service.Impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fmbank.welfarelottery.entity.TThreeDRecord;
import com.fmbank.welfarelottery.entity.TThreeSixBuyRecord;
import com.fmbank.welfarelottery.mapper.TThreeDRecordMapper;
import com.fmbank.welfarelottery.mapper.TThreeSixBuyRecordMapper;
import com.fmbank.welfarelottery.service.IThreeDSixRecordService;
import com.fmbank.welfarelottery.util.AbstractConvert;
import com.fmbank.welfarelottery.util.DateUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 3组六购买记录表 服务实现类
 * </p>
 *
 * @author jianchun
 * @since 2024-01-13
 */
@Service
public class TThreeSixBuyRecordServiceImpl extends ServiceImpl<TThreeSixBuyRecordMapper, TThreeSixBuyRecord> implements IThreeDSixRecordService {

    private static final String buyNumber = "012,013,014,015,016,017,018,019,023,024,025,026,027,028,029,034,035,036,037,038,039,045,046,047,048,049,056,057,058,059";
    @Resource
    TThreeDRecordMapper tThreeDRecordMapper;
    @Resource
    TThreeSixBuyRecordMapper tThreeSixBuyRecordMapper;


    @Override
    public String buyDataToDb(String year) {
        HashMap<Integer, Integer> map = getMap();
        List<TThreeDRecord> tThreeDRecords = tThreeDRecordMapper.selectByYear(year);
        Map<String, List<TThreeDRecord>> collect = tThreeDRecords.stream().collect(Collectors.groupingBy(TThreeDRecord::getDate));
        ArrayList<TThreeSixBuyRecord> tThreeSixBuyRecords = new ArrayList<>();
        int buyDouble = 0;
        for (TThreeDRecord lotteryRecord : tThreeDRecords) {
            if (12 == buyDouble) {
                buyDouble = 0;
            }
            buyDouble++;
            TThreeSixBuyRecord tThreeSixBuyRecord = AbstractConvert.convert(lotteryRecord, TThreeSixBuyRecord.class);
            tThreeSixBuyRecord.setCreateTime(new Date());
            tThreeSixBuyRecord.setModifyTime(new Date());
            if (buyDouble > 5) {
                //设置购买号码
                tThreeSixBuyRecord.setBuyNumber(buyNumber);
                tThreeSixBuyRecord.setBuyDouble(map.get(buyDouble));
                tThreeSixBuyRecord.setBuyAmount(tThreeSixBuyRecord.getBuyDouble() * 60);
                //获取下下一期的数据
                String lastOfDay = DateUtil.getNextOfDay(tThreeSixBuyRecord.getDate(), 1);
                //获取t+1的数据，
                if (collect.containsKey(lastOfDay)) {
                    TThreeDRecord tThreeDRecord = collect.get(lastOfDay).get(0);
                    String lotteryNumber = tThreeDRecord.getLotteryNumber();
                    String[] split = buyNumber.split(",");
                    boolean added = false;
                    for (int i = 0; i < split.length; i++) {
                        if (lotteryNumber.contains(split[i].substring(0, 1)) && lotteryNumber.contains(split[i].substring(1, 2)) && lotteryNumber.contains(split[i].substring(2, 3))) {
                            //中奖
                            tThreeSixBuyRecord.setWinStatus("1");
                            tThreeSixBuyRecord.setWinNumber(split[i]);
                            tThreeSixBuyRecord.setWinAmount(tThreeSixBuyRecord.getBuyDouble() * 173);
                            buyDouble = 0;
                            tThreeSixBuyRecords.add(tThreeSixBuyRecord);
                            added = true;
                        }
                    }
                    if (!added) {
                        tThreeSixBuyRecords.add(tThreeSixBuyRecord);
                    }
                } else {
                    tThreeSixBuyRecords.add(tThreeSixBuyRecord);
                }
            } else {
                tThreeSixBuyRecords.add(tThreeSixBuyRecord);
            }
        }
        if (!ObjectUtils.isEmpty(tThreeSixBuyRecords)) {
            List<List<TThreeSixBuyRecord>> partition = Lists.partition(tThreeSixBuyRecords, 500);
            for (List<TThreeSixBuyRecord> threeSixBuyRecords : partition) {
                tThreeSixBuyRecordMapper.insertBatchs(threeSixBuyRecords);
            }
        }
        return "SUCCESS";
    }

    private HashMap<Integer, Integer> getMap() {
        HashMap<Integer, Integer> hashMap = new HashMap<>();
      /*  hashMap.put(1, 1);
        hashMap.put(2, 2);
        hashMap.put(3, 3);
        hashMap.put(4, 1);
        hashMap.put(5, 1);*/
        hashMap.put(6, 1);
        hashMap.put(7, 2);
        hashMap.put(8, 3);
        hashMap.put(9, 5);
        hashMap.put(10, 8);
        hashMap.put(11, 13);
        hashMap.put(12, 21);
        return hashMap;
    }

    public static void main(String[] args) {
        String a = "012";
        System.out.println(a.substring(0, 1));
        System.out.println(a.substring(1, 2));
        System.out.println(a.substring(2, 3));

    }
}
