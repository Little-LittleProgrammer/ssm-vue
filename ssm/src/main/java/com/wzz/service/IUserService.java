package com.wzz.service;

import java.util.Map;

/**
 * @ClassName IUserService
 * @Description TODO 用户业务层接口
 * @Author AZhen
 * Version 1.0
 **/
public interface IUserService {
    Map<String,Object> queryUserList(Map<String, Object> map);

    int queryUser(Map<String, Object> map);

    //修改密码
    void upUserPass(Map<String, Object> map);

    //新增用户
    void addUser(Map<String, Object> map);

    //删除用户
    void delUser(Map<String, Object> map);

    //修改用户
    void upUser(Map<String, Object> map);
}