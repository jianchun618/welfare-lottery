package com.fmbank.welfarelottery.exception;

import com.fmbank.welfarelottery.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by jwk on 2019/07/05.
 * 全局异常处理
 *
 * @author Jing WenKai
 * @date 2019/07/05 11:57
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("系统异常{0}", e);
        return Result.fail(e.getMessage());
    }
}
