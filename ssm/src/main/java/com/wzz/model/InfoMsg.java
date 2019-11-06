package com.wzz.model;

import java.io.Serializable;

/**
 * @ClassName InfoMsg
 * @Description TODO   错误信息消息体
 * @Author AZhen
 * Version 1.0
 **/
public class InfoMsg implements Serializable {
    //自定义错误码    默认0表示正常执行
    private String code="0";
    //错误信息
    private String message="操作成功";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}