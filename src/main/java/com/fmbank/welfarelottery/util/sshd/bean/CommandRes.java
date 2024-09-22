package com.fmbank.welfarelottery.util.sshd.bean;

import java.io.Serializable;

/**
 * @ClassName: CommandRes
 * @Description: 命令返回对象
 * @author: jc
 * @date: 2022/8/22 11:21
 */
public class CommandRes implements Serializable {
    private Integer exitStatus;
    private String out;
    private String err;
    private boolean timeOut;
    private boolean success;

    public boolean isSuccess() {
        return success&&exitStatus==0;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getExitStatus() {
        return exitStatus;
    }

    public void setExitStatus(Integer exitStatus) {
        this.exitStatus = exitStatus;
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public boolean isTimeOut() {
        return timeOut;
    }

    public void setTimeOut(boolean timeOut) {
        this.timeOut = timeOut;
    }

    @Override
    public String toString() {
        return "CommandRes{" +
                "exitStatus=" + exitStatus +
                ", out='" + out + '\'' +
                ", err='" + err + '\'' +
                ", timeOut=" + timeOut +
                ", success=" + success +
                '}';
    }
}
