package com.wzz.mapper;

import com.wzz.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName UserMapper
 * @Description TODO 用户持久层接口
 * @Author AZhen
 * Version 1.0
 **/
public interface UserMapper {
    List<User> queryUserList(Map<String, Object> map);

    //查询用户
    int queryUser(Map<String, Object> map);

    //修改密码
    int upUserPass(Map<String, Object> map);

    //新增用户
    void addUser(Map<String, Object> map);

    //删除用户
    void delUser(Map<String, Object> map);

    //修改用户
    void upUser(Map<String, Object> map);
}