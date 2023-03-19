package com.fmbank.welfarelottery.response;

import com.fmbank.welfarelottery.enums.CodeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author jc
 * @CreateTime 2022/6/20
 * @Description: 接口基础返回对象
 */
@Data
@NoArgsConstructor
public class BaseResult implements Serializable {
    /**
     * 成功标识
     */
    private boolean success;
    /**
     * 响应码
     */
    protected Integer code;
    /**
     * 错诶信息
     */
    protected String errorMessage;

    public BaseResult(boolean success, Integer code, String errorMessage) {
        this.success = success;
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public static BaseResult successBaseResult() {
        return successBaseResult(CodeEnum.SUCCESS.getMsg());
    }

    public static BaseResult successBaseResult(String msg) {
        return new BaseResult(true, CodeEnum.SUCCESS.getCode(), msg);
    }

    public static BaseResult failBaseResult() {
        return failBaseResult(CodeEnum.ERROR.getCode(), CodeEnum.ERROR.getMsg());
    }

    public static BaseResult failBaseResult(String errorMessage) {
        return failBaseResult(CodeEnum.ERROR.getCode(), errorMessage);
    }

    public static BaseResult failBaseResult(CodeEnum codeEnum) {
        return failBaseResult(codeEnum.getCode(), codeEnum.getMsg());
    }

    public static BaseResult failBaseResult(Integer code, String errorMessage) {
        return new BaseResult(false, code, errorMessage);
    }

    public boolean isSuccess() {
        return code != null && code.equals(CodeEnum.SUCCESS.getCode()) && success;
    }

}
