package com.wzz.mapper;

import java.util.List;
import java.util.Map;

public interface SysAccessLogMapper {

    /**
     * 查询系统日志列表
     * @Author AZhen
     * @Param [map]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public List<Map<String, Object>> querySysLogList(Map<String, Object> map);

    /**
     * 保存日志
     * @Author AZhen
     * @Param [map]
     * @return int
     **/
    public int saveSysLog(Map<String, Object> map);
}
