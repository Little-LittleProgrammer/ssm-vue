package com.wzz.controller;

import com.wzz.common.AssembleResponseMsg;
import com.wzz.model.ResponseBody;
import com.wzz.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName UserController
 * @Description TODO 用户控制层
 * @Author AZhen
 * Version 1.0
 **/
@RestController
public class UserController {
    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/queryUserList",produces = "application/json;charset=utf-8")
    public ResponseBody queryUserList(@RequestBody Map<String,Object> map){
        Map<String, Object> resultMap= userService.queryUserList(map);
        return new AssembleResponseMsg().success(resultMap);
    }
    @RequestMapping(value = "/queryUser",produces = "application/json;charset=utf-8")
    public ResponseBody queryUser(@RequestBody Map<String,Object> map){
        int flag = userService.queryUser(map);
        String name = "wal";
        Map<String,String> all = new HashMap<String,String>();
        if (flag==1){
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                System.out.println("key"+entry.getKey()+" value"+entry.getValue());
                if(entry.getValue().equals(name)){
                    all.put("token","admin");
                    break;
                }else {
                    all.put("token","editor");
                }
            }
            all.put("status","OK");
            return new AssembleResponseMsg().success(all);
        }{
            return new AssembleResponseMsg().failure(200,"error","用户名或密码错误");
        }
    }
    @RequestMapping(value = "/upUserPass",produces = "application/json;charset=utf-8")
    public ResponseBody upUserPass(@RequestBody Map<String,Object> map){
        userService.upUserPass(map);
        return new AssembleResponseMsg().success("OK");
    }
    @RequestMapping(value = "/addUser",produces = "application/json;charset=utf-8")
    public ResponseBody addUser(@RequestBody Map<String,Object> map){
        userService.addUser(map);
        return new AssembleResponseMsg().success("OK");
    }

    // POST模式下,@RequestParam接收不到body里面的数据，但可以接收到url里的参数
    @RequestMapping(value = "/delUser",produces = "application/json;charset=utf-8")
    public ResponseBody delUser(@RequestBody Map<String,Object> map){
        userService.delUser(map);
        return new AssembleResponseMsg().success("OK");
    }

    @RequestMapping(value = "/upUser",produces = "application/json;charset=utf-8")
    public ResponseBody upUser(@RequestBody Map<String,Object> map){
        userService.upUser(map);
        return new AssembleResponseMsg().success("OK");
    }
}