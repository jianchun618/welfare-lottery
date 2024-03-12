package com.fmbank.welfarelottery.controller;


import com.fmbank.welfarelottery.response.Result;
import com.fmbank.welfarelottery.service.ILotteryRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    @ApiOperation("根据日期购初始化买数据(日期格式:yyyy-MM-dd)")
    public Result dataInit(String dataString) {
        return Result.success(iLotteryRecordService.dataInit(dataString));
    }

    @GetMapping("/cashAPrize")
    @ApiOperation("统计当期盈利金额")
    public Result cashAPrize() {
        return Result.success("本期盈利金额：" + iLotteryRecordService.cashAPrize() + "元");
    }

    @GetMapping("/currentDateData")
    @ApiOperation("当日数据详情")
    public Result dateData() {
        return Result.success(iLotteryRecordService.dateData());
    }

    @GetMapping("/dataRandom")
    @ApiOperation("根据日期，随机生成16条购买数据")
    public Result dataRandom(Integer integer, String dataDate) {
        return Result.success(iLotteryRecordService.dataRandom(integer, dataDate));
    }

    @GetMapping("/showRecordSizeAndMapSize")
    @ApiOperation("展示总记录数和去重map总条数")
    public Result showRecordSizeAndMapSize() {
        return Result.success(iLotteryRecordService.showRecordSizeAndMapSize());
    }

}
