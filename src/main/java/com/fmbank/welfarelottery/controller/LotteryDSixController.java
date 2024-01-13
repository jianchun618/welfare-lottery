package com.fmbank.welfarelottery.controller;


import com.fmbank.welfarelottery.response.Result;
import com.fmbank.welfarelottery.service.IThreeDRecordService;
import com.fmbank.welfarelottery.service.IThreeDSixRecordService;
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
@RequestMapping("/lottery/six")
@Api(tags = "组六数据服务类")
public class LotteryDSixController {
    @Resource
    IThreeDSixRecordService iThreeDSixRecordService;

    @GetMapping("/buyDataToDbWithYear")
    @ApiOperation("组六-第n年的获取购买数据入库")
    public Result buyDataToDbWithYear(String year) {
        return Result.success(iThreeDSixRecordService.buyDataToDb(year));
    }

}
