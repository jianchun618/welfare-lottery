package com.fmbank.welfarelottery.controller;


import com.fmbank.welfarelottery.response.Result;
import com.fmbank.welfarelottery.service.ILotteryRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 数据记录服务 前端控制器
 * </p>
 *
 * @author jianchun
 * @since 2023-03-18
 */
@RestController
@RequestMapping("/lotteryRecord")
@Api(tags = "数据记录服务类")
public class LotteryRecordController {
    @Resource
    ILotteryRecordService iLotteryRecordService;

    @GetMapping("/pullDataToDb")
    @ApiOperation("拉取最新的N条数据")
    public Result dataToDb(Integer integer) {
        return Result.success(iLotteryRecordService.dataToDb(integer));
    }

    @GetMapping("/getLeastRecord")
    @ApiOperation("获取最近的开奖记录")
    public Result getNewLeastRecord() {
        return Result.success(iLotteryRecordService.showLatestRecordInfo());
    }

    @GetMapping("/dataInit")
    @ApiOperation("初始化日期购买数据,格式:yyyy-MM-dd)")
    public Result dataInit(String dataString) {
        return Result.success(iLotteryRecordService.dataInit(dataString));
    }

    @GetMapping("/cashAPrize")
    @ApiOperation("日期统计盈利,日期格式:yyyy-MM-dd")
    public Result cashAPrize(String date) {
        return Result.success("本期盈利金额：" + iLotteryRecordService.cashAPrize(date) + "元");
    }

    @GetMapping("/currentDateData")
    @ApiOperation("日期获取购买数据,日期格式:yyyy-MM-dd")
    public Result dateData(String dataDate) {
        return Result.success(iLotteryRecordService.dateData(dataDate));
    }

    @GetMapping("/dataRandom")
    @ApiOperation("日期，随机生成16条购买数据")
    public Result dataRandom(Integer integer, String dataDate) {
        return Result.success(iLotteryRecordService.dataRandom(integer, dataDate));
    }

    @GetMapping("/dataRandomYear")
    @ApiOperation("按年，随机每天生成N条购买数据")
    public Result dataRandom(String year, Integer integer) {
        iLotteryRecordService.dataRandomYear(year, integer);
        iLotteryRecordService.yearCashAPrize(year);
        return Result.success();
    }

    @GetMapping("/dataRandomNYear")
    @ApiOperation("生成N年，每天N注进行兑奖")
    public Result dataRandomNYear(Integer integer) {
        List<String> years = Arrays.asList("2014", "2015", "2016", "2017", "2018",
                "2019", "2020", "2021", "2022", "2023");
        for (String year : years) {
            iLotteryRecordService.dataRandomYear(year, integer);
            iLotteryRecordService.yearCashAPrize(year);
        }
        return Result.success();
    }

    @GetMapping("/showRecordSizeAndMapSize")
    @ApiOperation("展示总记录数和去重map总条数")
    public Result showRecordSizeAndMapSize() {
        return Result.success(iLotteryRecordService.showRecordSizeAndMapSize());
    }

}
