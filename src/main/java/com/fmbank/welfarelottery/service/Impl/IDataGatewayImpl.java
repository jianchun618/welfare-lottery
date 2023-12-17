package com.fmbank.welfarelottery.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fmbank.welfarelottery.entity.LotteryRecord;
import com.fmbank.welfarelottery.entity.TThreeDRecord;
import com.fmbank.welfarelottery.service.IDataGateway;
import com.fmbank.welfarelottery.util.BallRandomUtil;
import com.fmbank.welfarelottery.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Component
@Service
@Slf4j
public class IDataGatewayImpl implements IDataGateway {

    @Resource
    HttpClientUtil httpClientUtil;

    /**
     * 从外围接口获取开奖记录
     *
     * @param integer 获取的条数
     * @return 开奖记录
     */
    @Override
    public List<LotteryRecord> getLotteryRecord(Integer integer) {
        String url = "http://www.cwl.gov.cn/cwl_admin/front/cwlkj/search/kjxx/findDrawNotice";
        HashMap<String, String> params = getDataRequestParamMap(integer);
        HashMap<String, Object> header = getDataRequestHeadMap();
        //执行调用外围接口
        String response = httpClientUtil.doGet(url, header, params);
        JSONObject jsonObject = JSONObject.parseObject(response);
        //数据临时存储
        ArrayList<LotteryRecord> lotteryRecords = new ArrayList<>();
        if (jsonObject.containsKey("result")) {
            String result = jsonObject.get("result").toString();
            JSONArray objects = JSONObject.parseArray(result);
            for (int i = 0; i < objects.size(); i++) {
                String s = objects.get(i).toString();
                JSONObject oneData = JSONObject.parseObject(s);
                LotteryRecord lotteryRecord = new LotteryRecord();
                lotteryRecord.setDate(oneData.get("date").toString().substring(0, 10));//日期
                lotteryRecord.setWeek(oneData.get("week").toString());//星期几
                lotteryRecord.setCode(oneData.get("code").toString());//期号
                lotteryRecord.setRed(oneData.get("red").toString());//红
                lotteryRecord.setBlue(oneData.get("blue").toString());//蓝
                lotteryRecord.setContent(oneData.get("content").toString());
                lotteryRecord.setCreateTime(new Date());
                lotteryRecord.setModifyTime(new Date());
                lotteryRecords.add(lotteryRecord);
            }
        }
        lotteryRecords.sort(Comparator.comparing(LotteryRecord::getDate));

        if (lotteryRecords.size() > 0) {
            log.info("本次获取到-[{}]条数据,开始日期-[{}],结束日期-[{}]", lotteryRecords.size(), lotteryRecords.get(0).getDate(), lotteryRecords.get(lotteryRecords.size() - 1).getDate());
        } else {
            log.info("本次获取到-[{}]条数据", 0);
        }
        return lotteryRecords;
    }

    @Override
    public List<LotteryRecord> getDataByRandom(Integer integer) {
        List<LotteryRecord> lotteryRecords = new ArrayList<>();
        List<String> balls = BallRandomUtil.getBalls(integer);
        for (String ball : balls) {
            LotteryRecord lotteryRecord = new LotteryRecord();
            lotteryRecord.setModifyTime(new Date());
            lotteryRecord.setCreateTime(new Date());
        }
        return null;
    }

    @Override
    public List<TThreeDRecord> getThreeDByYear(String yearStart, String yearEnd) {
        log.info("yearStart-[{}]- and yearEnd-[{}]",yearStart,yearEnd);
        String url = "https://www.cwl.gov.cn/cwl_admin/front/cwlkj/search/kjxx/findDrawNotice";
        HashMap<String, String> params = get3dDataRequestParamMap(yearStart,yearEnd);
        HashMap<String, Object> header = getStringObjectHashMap();
        //执行调用外围接口
        String response = httpClientUtil.doGet(url, header, params);
        JSONObject jsonObject = JSONObject.parseObject(response);
        //数据临时存储
        ArrayList<TThreeDRecord> tThreeDRecords = new ArrayList<>();
        if (jsonObject.containsKey("result")) {
            String result = jsonObject.get("result").toString();
            JSONArray objects = JSONObject.parseArray(result);
            for (int i = 0; i < objects.size(); i++) {
                String s = objects.get(i).toString();
                JSONObject oneData = JSONObject.parseObject(s);
                TThreeDRecord lotteryRecord = new TThreeDRecord();
                lotteryRecord.setCreateTime(new Date());
                lotteryRecord.setModifyTime(new Date());
                lotteryRecord.setCode(oneData.get("code").toString());//期号
                lotteryRecord.setDate(oneData.get("date").toString().substring(0, 10));//日期
                lotteryRecord.setWeek(oneData.get("week").toString());//星期几
                lotteryRecord.setLotteryNumber(oneData.get("red").toString().replace(",",""));//开奖号码
                tThreeDRecords.add(lotteryRecord);
            }
        }
        tThreeDRecords.sort(Comparator.comparing(TThreeDRecord::getDate));

        if (tThreeDRecords.size() > 0) {
            log.info("本次获取到-[{}]条数据,开始日期-[{}],结束日期-[{}]", tThreeDRecords.size(), yearStart,yearEnd);
        } else {
            log.info("本次获取到-[{}]条数据", 0);
        }
        return tThreeDRecords;
    }

    private HashMap<String, Object> getStringObjectHashMap() {
        HashMap<String, Object> header = new HashMap<>();
        header.put("Accept", "application/json, text/javascript, */*; q=0.01");
        header.put("Accept-Encoding", "gzip, deflate, br");
        header.put("Accept-Language", "zh-CN,zh;q=0.9");
        header.put("Connection", "keep-alive");
        header.put("Cookie", "HMF_CI=2adcbc4a606bb355d6693146d6c8608ce6d2818e221e4091aef8c9e8fe8637617cf74ce2816ad471236c39f4bf657adfeac17625d80dab8f45cce342757a82d4e2; 21_vq=8");
        header.put("Host", "www.cwl.gov.cn");
        header.put("Referer", "http://www.cwl.gov.cn/ygkj/wqkjgg/");
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36");
        header.put("X-Requested-With", "XMLHttpRequest");
        return header;
    }

    /**
     * 组装数据请求参数信息
     *
     * @return java.util.HashMap
     */
    private HashMap<String, String> getDataRequestParamMap(Integer integer) {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", "ssq");
        params.put("pageNo", "1");
        params.put("pageSize", String.valueOf(integer));
        params.put("systemType", "PC");
        return params;
    }
    /**
     * 组装数据请求参数信息
     *
     * @return java.util.HashMap
     */
    private HashMap<String, String> get3dDataRequestParamMap(String yearStart,String yearEnd) {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", "3d");
        params.put("dayStart", yearStart);
        params.put("dayEnd", yearEnd);
        params.put("pageNo", "1");
        params.put("pageSize", "1000");
        params.put("systemType", "PC");
        return params;
    }

    /**
     * 组装数据请求头信息
     *
     * @return java.util.HashMap
     */
    private HashMap<String, Object> getDataRequestHeadMap() {
        HashMap<String, Object> header = new HashMap<>();
        header.put("Accept", "application/json, text/javascript, */*; q=0.01");
        header.put("Accept-Encoding", "gzip, deflate");
        header.put("Accept-Language", "zh-CN,zh;q=0.9");
        header.put("Connection", "keep-alive");
        header.put("Cookie", "HMF_CI=411524744fa2418ad41524f259fa1179f0c09314d25a467c92bd8d95fa48c1305432526a848f64e8b7062c3865228d24ca314541bb26e462dbd2b6a2ce67a7a2c4; 21_vq=2");
        header.put("Host", "www.cwl.gov.cn");
        header.put("Referer", "http://www.cwl.gov.cn/ygkj/wqkjgg/");
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36");
        header.put("X-Requested-With", "XMLHttpRequest");
        return header;
    }
}
