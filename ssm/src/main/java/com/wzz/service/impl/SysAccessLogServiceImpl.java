package com.wzz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wzz.mapper.SysAccessLogMapper;
import com.wzz.service.ISysAccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统访问日志实现类
 * @ClassName SysAccessLogServiceImpl
 * @Description TODO
 * @Author AZhen
 * Version 1.0
 **/
@Service
public class SysAccessLogServiceImpl implements ISysAccessLogService {
    @Autowired
    private SysAccessLogMapper sysAccessLogMapper;
    @Override
    public Map<String, Object> querySysLogList(Map<String, Object> map) {
        int pageNum=Integer.parseInt(map.get("pageNum").toString()); //当前页
        int pageSize=Integer.parseInt(map.get("pageSize").toString());  //每页几条
        PageHelper.startPage(pageNum,pageSize);
        List<Map<String, Object>> resultList = sysAccessLogMapper.querySysLogList(map);
        PageInfo pageInfo=new PageInfo(resultList);
        long total = pageInfo.getTotal();  //总条数
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("total",total);
        resultMap.put("rows",resultList);
        return resultMap;
    }

    @Override
    public int saveSysLog(Map<String, Object> map) {
        return sysAccessLogMapper.saveSysLog(map);
    }
}