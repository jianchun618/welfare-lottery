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
 * 开奖记录表 前端控制器
 * </p>
 *
 * @author jianchun
 * @since 2023-03-18
 */
@RestController
@RequestMapping("/lotteryRecord")
@Api(tags = "开奖记录服务类")
public class LotteryRecordController {
    @Resource
    ILotteryRecordService iLotteryRecordService;

    @GetMapping("/dataToDb")
    @ApiOperation("拉取最新的N条数据")
    public Result dataToDb(Integer integer) {
        return Result.success(iLotteryRecordService.dataToDb(integer));
    }

    @GetMapping("/getNewLeastRecord")
    @ApiOperation("获取最新开奖记录")
    public Result getNewLeastRecord() {
        return Result.success(iLotteryRecordService.showLatestRecordInfo());
    }

    @GetMapping("/showRecordSizeAndMapSize")
    @ApiOperation("展示总记录数和去重map总条数")
    public Result showRecordSizeAndMapSize() {
        return Result.success(iLotteryRecordService.showRecordSizeAndMapSize());
    }

/*    @GetMapping("/dataRandom")
    @ApiOperation("系统生成16条购买数据")
    public Result dataRandom(Integer integer) {
        return Result.success(iLotteryRecordService.dataRandom(integer));
    }*/

    @GetMapping("/cashAPrize")
    @ApiOperation("统计当期中奖情况")
    public Result cashAPrize() {
        iLotteryRecordService.cashAPrize();
        return Result.success();
    }
}
