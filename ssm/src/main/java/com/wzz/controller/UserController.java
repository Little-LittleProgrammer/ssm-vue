package com.wzz.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
import com.wzz.common.AssembleResponseMsg;
import com.wzz.common.DESUtils;
import com.wzz.model.ResponseBody;
import com.wzz.service.IUserService;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
    public ResponseBody queryUserList(@RequestBody String str) throws Exception {
        String aws = DESUtils.decryption(str,"mmhll1314520lhxq");
        Map<String,Object> map = JSON.parseObject(aws, HashMap.class);
        Map<String, Object> resultMap= userService.queryUserList(map);
        return new AssembleResponseMsg().success(resultMap);
    }

//    @RequestMapping(value = "/loginUri",produces = "application/json;charset=utf-8")
//    public ResponseBody loginUri(@RequestBody String str) throws Exception {
////        String aws = DESUtils.decryption(str,"mmhll1314520lhxq");
////        Map<String,Object> map = JSON.parseObject(aws, HashMap.class);
//        Map<String,Object> resultMap = new HashMap<>();
//        resultMap.put('data','')
////        Map<String, Object> resultMap= userService.queryUserList(map);
//        return new AssembleResponseMsg().success(resultMap);
//    }

    @RequestMapping(value = "/queryUser",produces = "application/json;charset=utf-8")
    // public ResponseBody queryUser(@RequestBody Map<String,Object> map){
    public ResponseBody queryUser(@RequestBody String str) throws Exception {
        String aws = DESUtils.decryption(str,"mmhll1314520lhxq");
        Map<String,Object> map = JSON.parseObject(aws, HashMap.class);
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
            all.put("flag","OK");
            return new AssembleResponseMsg().success(all);
        }{
            return new AssembleResponseMsg().failure(200,"error","用户名或密码错误");
        }
    }
    @RequestMapping(value = "/upUserPass",produces = "application/json;charset=utf-8")
    public ResponseBody upUserPass(@RequestBody String str) throws Exception {
        String aws = DESUtils.decryption(str,"mmhll1314520lhxq");
        Map<String,Object> map = JSON.parseObject(aws, HashMap.class);
        userService.upUserPass(map);
        return new AssembleResponseMsg().success("OK");
    }
//    @RequestMapping(value = "/upUserPass",produces = "application/json;charset=utf-8")
//    public ResponseBody upUserPass(@RequestBody Map str) throws Exception {
////        String aws = DESUtils.decryption(str,"mmhll1314520lhxq");
////        Map<String,Object> map = JSON.parseObject(aws, HashMap.class);
//        userService.upUserPass(str);
//        return new AssembleResponseMsg().success("OK");
//    }
    @RequestMapping(value = "/addUser",produces = "application/json;charset=utf-8")
    public ResponseBody addUser(@RequestBody String str) throws Exception {
        String aws = DESUtils.decryption(str,"mmhll1314520lhxq");
        Map<String,Object> map = JSON.parseObject(aws, HashMap.class);
        userService.addUser(map);
        return new AssembleResponseMsg().success("OK");
    }

    // POST模式下,@RequestParam接收不到body里面的数据，但可以接收到url里的参数
    @RequestMapping(value = "/delUser",produces = "application/json;charset=utf-8")
    public ResponseBody delUser(@RequestBody String str) throws Exception {
        String aws = DESUtils.decryption(str,"mmhll1314520lhxq");
        Map<String,Object> map = JSON.parseObject(aws, HashMap.class);
        userService.delUser(map);
        return new AssembleResponseMsg().success("OK");
    }

    @RequestMapping(value = "/upUser",produces = "application/json;charset=utf-8")
    public ResponseBody upUser(@RequestBody String str) throws Exception {
        String aws = DESUtils.decryption(str,"mmhll1314520lhxq");
        Map<String,Object> map = JSON.parseObject(aws, HashMap.class);
        userService.upUser(map);
        return new AssembleResponseMsg().success("OK");
    }

    // 通过二维码登录，网页端
    @RequestMapping(value = "/loginByQR",produces = "application/json;charset=utf-8")
    public ResponseBody loginByQR() throws Exception {
        String val = "";
        Random random = new Random();
        for (int i=0;i<20;i++){
            val += String.valueOf(random.nextInt(10));
        }
        Map<String,String> all = new HashMap<String,String>();
        all.put("QR","/page/confirmLogin/index");
        all.put("token",val);
        all.put("status","OK");
        return new AssembleResponseMsg().success(all);
    }

    // 通过二维码登录，小程序端
    @RequestMapping(value = "/loginByQRWithApp",produces = "application/json;charset=utf-8")
    public ResponseBody loginByQRWithApp(@RequestBody Map map) throws Exception {
        userService.addLoginStatus(map);
        return new AssembleResponseMsg().success("ok");
    }

    // 二维码登录查询token，网页端
    @RequestMapping(value = "/queryLoginStatus",produces = "application/json;charset=utf-8")
    public ResponseBody queryLoginStatus(@RequestBody String str) throws Exception {
        try{
            String aws = DESUtils.decryption(str,"mmhll1314520lhxq");
            Map<String,Object> map = JSON.parseObject(aws, HashMap.class);
            System.out.println(map);
            int num = userService.queryLogin(map);
            if(num == 1){
                Map<String, Object> resultMap= userService.queryLoginStatus(map);
                return new AssembleResponseMsg().success(resultMap);
            }else {
                return new AssembleResponseMsg().failure(300,"error","查询失败");
            }
        }catch (Exception e){
            return new AssembleResponseMsg().failure(300,"error","查询失败");
        }

    }
}