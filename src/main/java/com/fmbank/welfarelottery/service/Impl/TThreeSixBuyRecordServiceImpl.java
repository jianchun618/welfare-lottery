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

    String a = "" +
            "012,013,014,015,016,017,018,019,023,024," +
            "025,026,027,028,029,034,035,036,037,038," +//015,019,026,034,038
            "039,045,046,047,048,049,056,057,058,059," +
            "067,068,069,078,179,089,123,124,125,126," +//047,057,068,089,126

            "127,128,129,134,135,136,137,138,139,145," +
            "146,147,148,149,156,157,158,159,167,168," +//134,138,147,157,168
            "169,178,179,189,234,235,236,237,238,239," +
            "245,246,247,248,249,256,257,258,259,267," +//189,237,246,256,267

            "268,269,278,279,289,345,346,347,348,349," +
            "356,357,358,359,367,368,369,378,379,389" +//279,347,357,368,389
            "456,457,458,459,467,468,469,478,479,489" +
            "567,568,569,578,579,589,678,679,689,789";//459,478,568,589,789
    private static final String buyNumber = "015,019,026,034,038,047,057,068,089,126,134,138,147,157,168,189,237,246,256,267,279,347,357,368,389,459,478,568,589,789";

    private static final String buyNumber1 = "012,016,023,027,035,039,048,058,069,123,127,135,139,148,158,169,234,238,247,257,268,289,348,358,369,456,467,479,569,678,";
    private static final String buyNumber2 = "013,017,024,028,036,045,049,059,078,124,128,136,145,149,159,178,235,239,248,258,269,345,349,359,378,457,468,489,578,679,";
    private static final String buyNumber3 = "014,018,025,029,037,046,056,067,179,125,129,137,146,156,167,179,236,245,249,259,278,346,356,367,379,458,469,567,579,689,";
    private static final String buyNumber4 = "015,019,026,034,038,047,057,068,089,126,134,138,147,157,168,189,237,246,256,267,279,347,357,368,389,459,478,568,589,789,";

    @Resource
    TThreeDRecordMapper tThreeDRecordMapper;
    @Resource
    TThreeSixBuyRecordMapper tThreeSixBuyRecordMapper;

    @Override
    public String buyDataToDb(String year) {
        HashMap<Integer, Integer> map = getBaseMap();
        List<TThreeDRecord> tThreeDRecords = tThreeDRecordMapper.selectByYear(year);
        Map<String, List<TThreeDRecord>> collect = tThreeDRecords.stream().collect(Collectors.groupingBy(TThreeDRecord::getDate));
        ArrayList<TThreeSixBuyRecord> tThreeSixBuyRecords = new ArrayList<>();
        int buyDouble = 0;
        int periods = 0;
        for (TThreeDRecord lotteryRecord : tThreeDRecords) {
            periods++;
           /* if (6 == buyDouble) {
                buyDouble = 0;
            }*/
            buyDouble++;
            TThreeSixBuyRecord tThreeSixBuyRecord = AbstractConvert.convert(lotteryRecord, TThreeSixBuyRecord.class);
            tThreeSixBuyRecord.setCreateTime(new Date());
            tThreeSixBuyRecord.setModifyTime(new Date());
            if (buyDouble >= 3) {
                //设置购买号码
                tThreeSixBuyRecord.setBuyNumber(buyNumber1);
                if (periods <= 7) {
                    tThreeSixBuyRecord.setBuyDouble(map.get(buyDouble));
                } else {
                    tThreeSixBuyRecord.setBuyDouble(0);
                }
                tThreeSixBuyRecord.setBuyAmount(tThreeSixBuyRecord.getBuyDouble() * 60);
                //获取下一期的数据
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
                            tThreeSixBuyRecord.setPeriods(periods);
                            periods = 0;
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

    private HashMap<Integer, Integer> getBaseMap() {
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        hashMap.put(3, 1);
        hashMap.put(4, 1);
        hashMap.put(5, 1);
        hashMap.put(6, 1);
        hashMap.put(7, 1);
        return hashMap;
    }
}
