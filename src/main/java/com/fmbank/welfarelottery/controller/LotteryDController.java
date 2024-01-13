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
    @ApiOperation("3D-拉取N年的数据")
    public Result dataToDb(String year) {
        return Result.success(iThreeDRecordService.dataToDbByYear(year));
    }

    /*  @GetMapping("/buyDataToDb5Year")
      @ApiOperation("3D-拉取5年的数据")
      public Result buyDataToDb5Year(String year) {
          return Result.success(iThreeDRecordService.buyDataToDb5Year(year));

      }*/
    @GetMapping("/buyDataToDb")
    @ApiOperation("3D-第n年的获取购买数据入库")
    public Result buyDataToDb(String year) {
        return Result.success(iThreeDRecordService.buyDataToDb(year));
    }

    @GetMapping("/calculateByDate")
    @ApiOperation("3D-某日购买号码计算")
    public Result calculateByDate(String date) {
        return Result.success(iThreeDRecordService.calculateByDate(date));
    }

    public static void main(String[] args) {
        String a = "012,013,014,015,016,017,018,019," +//8
                "023,024,025,026,027,028,029" +//7
                "034,035,036,037,038,039" +//6
                "045,046,047,048,049" +//5
                "056,057,058,059" +//4
                "067,068,069" +//3
                "078,179" +//2
                "089" +//1  --------36
                "123,124,125,126,127,128,129" +//7
                "134,135,136,137,138,139" +//6
                "145,146,147,148,149" +//5
                "156,157,158,159" +//4
                "167,168,169" +//3
                "178,179" +//2
                "189" +//1------------------------------->28
                "234,235,236,237,238,239" +//6
                "245,246,247,248,249" +//5
                "256,257,258,259" +//4
                "267,268,269" +//3
                "278,279" +//2
                "289" +//1------------------------------->21
                "345,346,347,348,349" +//5
                "356,357,358,359" +//4
                "367,368,369" +//3
                "378,379" +//2
                "389" +//1------------------------------->15
                "456,457,458,459" +//4
                "467,468,469" +//3
                "478,479" +//2
                "489" +//1------------------------------>10
                "567,568,569" +//3
                "578,579" +//2
                "589" +//1------------------------------->6
                "678,679" +//2
                "689" +//1------------------------------->3
                "789";//1-------------------------------->1
        System.out.println(36+28+21+15+10+6+3+1);
    }

}
