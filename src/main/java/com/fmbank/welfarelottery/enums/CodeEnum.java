package com.fmbank.welfarelottery.enums;

/**
 * @Author jc
 * @CreateTime 2022/6/20
 * @Description: http枚举
 */
public enum CodeEnum {

    SUCCESS(200, "操作成功"),
    CONFIRMED(201, "存在重复标签，请确认是否覆盖"),
    ERROR_PARAM(202, "参数异常"),
    ERROR_CODE_PARAM(202, "无效编码"),
    ERROR(203, "操作失败"),
    REPETITION_REQ(500, "操作太过频繁"),
    ;
    /**
     * 响应码
     */
    private int code;

    /**
     * 响应信息
     */
    private String msg;

    CodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
