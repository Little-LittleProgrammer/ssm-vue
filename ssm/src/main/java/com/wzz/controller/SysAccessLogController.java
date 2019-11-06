package com.wzz.controller;

import com.wzz.common.AssembleResponseMsg;
import com.wzz.model.ResponseBody;
import com.wzz.service.ISysAccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 系统访问日志Controller
 * @ClassName SysAccessLogController
 * @Description TODO
 * @Author AZhen
 * Version 1.0
 **/
@RestController
public class SysAccessLogController {
    @Autowired
    private ISysAccessLogService sysAccessLogService;

    /**
     * 查询系统访问日志列表
     * @Author AZhen
     * @Param [map]
     * @return com.wzz.model.ResponseInfo
     **/
    @RequestMapping(value = "/sysLogList",produces = "application/json;charset=utf-8")
    public ResponseBody querySysLogList(@RequestBody Map<String,Object> map){
        Map<String, Object> resultMap = sysAccessLogService.querySysLogList(map);
        return new AssembleResponseMsg().success(resultMap);
    }
}