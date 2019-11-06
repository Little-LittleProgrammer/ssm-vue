package com.wzz.service;

import java.util.Map;

/**
 * 系统访问日志接口
 * @ClassName ISysAccessLogService
 * @Description TODO
 * @Author AZhen
 * Version 1.0
 **/
public interface ISysAccessLogService {

    /**
     * 查询系统日志列表
     * @Author AZhen
     * @Param [map]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public Map<String,Object> querySysLogList(Map<String, Object> map);

    /**
     * 保存日志
     * @Author AZhen
     * @Param [map]
     * @return int
     **/
    public int saveSysLog(Map<String, Object> map);
}