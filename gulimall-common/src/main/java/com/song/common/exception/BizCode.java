package com.song.common.exception;

/* *
 * @program: gulimall
 * @description 业务状态码
 * @author: swq
 * @create: 2021-01-25 21:28
 **/
public enum BizCode {

    UN_KNOW_SYSTEM_EXCEPTION(10000, "未知的系统异常"),
    VALID_ERROR_EXCEPTION(10001, "数据校验异常");

    private int code;
    private String msg;


    BizCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
