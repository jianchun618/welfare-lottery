package com.fmbank.welfarelottery.response;

import com.fmbank.welfarelottery.enums.CodeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author jc
 * @CreateTime 2022/6/20
 * @Description: 接口返回对象
 */
@Data
@NoArgsConstructor
public class Result<T> extends BaseResult implements Serializable {

    protected T data;

    public Result(boolean success, Integer code, String errorMessage) {
        super(success, code, errorMessage);
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T t) {
        return of(true, CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getMsg(), t);
    }

    public static <T> Result<T> success(Integer code, String errorMessage, T t) {
        return of(true, code, errorMessage, t);
    }

    public static <T> Result<T> fail() {
        return fail(CodeEnum.ERROR);
    }

    public static <T> Result<T> fail(String errorMessage) {
        return fail(CodeEnum.ERROR.getCode(), errorMessage);
    }

    public static <T> Result<T> fail(CodeEnum codeEnum) {
        return fail(codeEnum.getCode(), codeEnum.getMsg());
    }

    public static <T> Result<T> fail(Integer code, String errorMessage) {
        return of(false, code, errorMessage, null);
    }

    public static <T> Result<T> fail(Integer code, String errorMessage, T t) {
        return of(false, code, errorMessage, t);
    }

    public static <T> Result<T> of(boolean success, Integer code, String errorMessage, T t) {
        Result pageResponse = new Result(success, code, errorMessage);
        pageResponse.setData(t);
        return pageResponse;
    }

}
