package com.wzz.aspect;

import com.wzz.common.IDUtil;
import com.wzz.common.StringUtil;
import com.wzz.controller.SysAccessLogController;
import com.wzz.service.ISysAccessLogService;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SysAccessLogAspect
 * @Description TODO 系统访问日志切面类
 * @Author AZhen
 * Version 1.0
 **/
@Component
public class SysAccessLogAspect {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ISysAccessLogService sysAccessLogService;
    //访问时间
    private Date accessDate;
    //日志信息Map
    private Map<String,Object> logMap=new HashMap<>();

    private Class clazz;  //访问类

    public void doBefore(JoinPoint jp){
        clazz = jp.getTarget().getClass();
        if (clazz!= SysAccessLogController.class) {
            //日志ID
            logMap.put("logId", IDUtil.getUUID());

            String url = request.getRequestURL().toString();
            //请求url
            logMap.put("accessInterface", url);

            String ip = request.getRemoteAddr();
            //请求Ip
            logMap.put("accessIp", ip);

            String requestType = request.getMethod();
            //请求方式
            logMap.put("requestType", requestType);

            accessDate = new Date();
            //访问时间
            logMap.put("accessDate", accessDate);

            String args = Arrays.toString(jp.getArgs());
            //方法参数
            logMap.put("interfaceParams", args);

            String browserSystemInfo = StringUtil.getBrowserSystemInfo(request);
            //访问浏览器系统信息
            logMap.put("accessSource", browserSystemInfo);
        }
    }

    public void doAfter(JoinPoint jp){
        if (clazz!=SysAccessLogController.class){
            long executeTime = new Date().getTime() - accessDate.getTime();
            //执行时长
            logMap.put("executeTime",executeTime);
            //插入日志信息
            sysAccessLogService.saveSysLog(logMap);
        }
    }
}