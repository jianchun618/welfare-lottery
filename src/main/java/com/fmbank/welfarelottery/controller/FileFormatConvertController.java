package com.fmbank.welfarelottery.controller;


import com.fmbank.welfarelottery.response.Result;
import com.fmbank.welfarelottery.service.IFileFormatConvertService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 文件转换控制器
 * </p>
 *
 * @author jianchun
 * @since 2024-03-29
 */
@RestController
@RequestMapping("/fileConvert")
@Api(tags = "图片文件格式转换服务")
public class FileFormatConvertController {
    @Resource
    IFileFormatConvertService iFileFormatConvertService;

    @GetMapping("/jpgToWebp")
    @ApiOperation("JPG/PNG转webp")
    public Result jpgToWebp(String oldFile, String newFile) {
        return Result.success(iFileFormatConvertService.jpgToWebp(oldFile, newFile));
    }

    @GetMapping("/webpToJpg")
    @ApiOperation("webp转JPG/PNG")
    public Result webpToJpg(String oldFile, String newFile) {
        return Result.success(iFileFormatConvertService.webpToJpg(oldFile, newFile));
    }

}
