package com.fmbank.welfarelottery.controller;


import com.fmbank.welfarelottery.response.Result;
import com.fmbank.welfarelottery.service.IThreeDRecordService;
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
@RequestMapping("/lotteryThreed")
@Api(tags = "数据记录服务类")
public class LotteryDController {
    @Resource
    IThreeDRecordService iThreeDRecordService;

    @GetMapping("/getDataByYear")
    @ApiOperation("拉取N年的数据")
    public Result dataToDb(String year) {
        return Result.success(iThreeDRecordService.dataToDbByYear(year));
    }

    @GetMapping("/buyDataToDb")
    @ApiOperation("第n年的获取购买数据入库")
    public Result buyDataToDb(String year) {
        return Result.success(iThreeDRecordService.buyDataToDb(year));
    }

}
