package com.wzz.model;

import com.wzz.common.DateUtil;

import java.io.Serializable;

/**
 * @ClassName ResponseBody
 * @Description TODO  封装响应的数据结构
 * @Author AZhen
 * Version 1.0
 **/
public class ResponseBody<T> implements Serializable {
    //时间
    private String date= DateUtil.getCurrentDateStr("yyyy-MM-dd HH:mm:ss");
    //状态码 默认200响应成功
    private int status=200;
    //接口返回的数据
    private T data;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public InfoMsg getInfo() {
        return info;
    }

    public void setInfo(InfoMsg info) {
        this.info = info;
    }

    //消息内容
    private InfoMsg info;

}