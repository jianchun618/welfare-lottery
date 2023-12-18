package com.fmbank.welfarelottery.service.Impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fmbank.welfarelottery.entity.LotteryRecord;
import com.fmbank.welfarelottery.entity.TThreeDBuyRecord;
import com.fmbank.welfarelottery.entity.TThreeDHisSummary;
import com.fmbank.welfarelottery.entity.TThreeDRecord;
import com.fmbank.welfarelottery.mapper.LotteryRecordMapper;
import com.fmbank.welfarelottery.mapper.TThreeDBuyRecordMapper;
import com.fmbank.welfarelottery.mapper.TThreeDHisSummaryMapper;
import com.fmbank.welfarelottery.mapper.TThreeDRecordMapper;
import com.fmbank.welfarelottery.service.IDataGateway;
import com.fmbank.welfarelottery.service.IThreeDRecordService;
import com.fmbank.welfarelottery.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class IThreeDServiceImpl extends ServiceImpl<LotteryRecordMapper, LotteryRecord> implements IThreeDRecordService {

    @Resource
    private IDataGateway iDataGateway;
    @Resource
    private TThreeDRecordMapper tThreeDRecordMapper;
    @Resource
    private TThreeDHisSummaryMapper tThreeDHisSummaryMapper;
    @Resource
    private TThreeDBuyRecordMapper tThreeDBuyRecordMapper;


    @Override
    @Transactional
    public Integer dataToDbByYear(String year) {
        Date before = DateUtil.parseDateTimeWithPattern(year, DateUtil.DATE_PATTERN_YEAR);
        //获取指定年的第一天
        Date beforeYearStart = DateUtil.getFirstOfYear(before);
        String yearStart = DateUtil.formatDateTime(beforeYearStart);
        //获取指定年的第一天和最后一天
        Date beforeEnd = DateUtil.getLastOfYear(before);
        String yearEnd = DateUtil.formatDateTime(beforeEnd);
        List<TThreeDRecord> threeDByYear = iDataGateway.getThreeDByYear(yearStart, yearEnd);
        return tThreeDRecordMapper.insertBatchs(threeDByYear);
    }

    @Override
    @Transactional
    public Integer buyDataToDb(String year) {
        List<TThreeDRecord> yearList = tThreeDRecordMapper.selectByYear(year);
        ArrayList<TThreeDBuyRecord> tThreeDBuyRecords = new ArrayList<>();
        for (TThreeDRecord tThreeDRecord : yearList) {
            TThreeDBuyRecord tThreeDBuyRecord = new TThreeDBuyRecord();
            tThreeDBuyRecord.setCode(tThreeDRecord.getCode());
            tThreeDBuyRecord.setDate(tThreeDRecord.getDate());
            tThreeDBuyRecord.setLotteryNumber(tThreeDRecord.getLotteryNumber());
            tThreeDBuyRecord.setCreateTime(new Date());
            tThreeDBuyRecord.setModifyTime(new Date());
            //计算下期最大未开奖期数前十个号
            String lastTenNum = getLastTenNum(tThreeDRecord.getDate());
            tThreeDBuyRecord.setBuyNumber(lastTenNum);
            //是否中奖
            //t-1 date
            String lastOfDay = DateUtil.getLastOfDay(tThreeDRecord.getDate());
            //获取t+1的数据，
            TThreeDRecord nextPeriod = tThreeDRecordMapper.selectByDate(lastOfDay);
            if (!ObjectUtils.isEmpty(nextPeriod) && nextPeriod.getLotteryNumber().equals(tThreeDBuyRecord.getBuyNumber())) {
                tThreeDBuyRecord.setWinStatus("1");
            }
            tThreeDBuyRecords.add(tThreeDBuyRecord);
        }
        return tThreeDBuyRecordMapper.insertBatchs(tThreeDBuyRecords);
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.getLastOfDay("2022-03-25"));
    }

    @Override
    public Object calculateByDate(String date) {
        return JSON.toJSONString(getLastTenNum(date));
    }

    /**
     * 获取最大未开奖期数前十个号
     *
     * @param date 期号
     * @return 最大未开奖期数前十个号
     */
    private String getLastTenNum(String date) {
        //计算下期最大未开奖期数前十个号
        ArrayList<TThreeDHisSummary> tThreeDHisSummaries = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            String lotteryNumber = "-1";
            if (i < 10) {
                lotteryNumber = String.format("00%s", i);
            } else if (i < 99) {
                lotteryNumber = String.format("0%s", i);
            } else {
                lotteryNumber = String.format("%s", i);
            }
            TThreeDRecord tThreeDRecords = tThreeDRecordMapper.selectDateByBeforeCode(date, lotteryNumber);
            if (!ObjectUtils.isEmpty(tThreeDRecords)) {
                TThreeDHisSummary tThreeDHisSummary = new TThreeDHisSummary();
                tThreeDHisSummary.setCalculateDate(date);
                tThreeDHisSummary.setLotteryNumber(lotteryNumber);
                tThreeDHisSummary.setCode(tThreeDRecords.getCode());
                tThreeDHisSummary.setDate(tThreeDRecords.getDate());
                //日期相差的天数
                tThreeDHisSummary.setPeriod(DateUtil.getCountOfTwoDay(tThreeDRecords.getDate(),date));
                tThreeDHisSummary.setCreateTime(new Date());
                tThreeDHisSummary.setModifyTime(new Date());
                tThreeDHisSummaries.add(tThreeDHisSummary);
            }
        }
        if (tThreeDHisSummaries.size() > 0) {
            tThreeDHisSummaryMapper.insertBatchs(tThreeDHisSummaries);
        }
        List<TThreeDHisSummary> tThreeHisSumList = tThreeDHisSummaryMapper.selectLastTenData();
        StringBuilder builder = new StringBuilder("");
        for (TThreeDHisSummary tThreeDHisSummary : tThreeHisSumList) {
            builder.append(tThreeDHisSummary.getLotteryNumber());
        }
        return builder.toString();
    }

}
