package com.wzz.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wzz.common.AssembleResponseMsg;
import com.wzz.common.DESUtils;
import com.wzz.model.ResponseBody;
import com.wzz.service.IBookService;
import com.wzz.common.SM4Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

@RestController
public class BookController {
    @Autowired
    private IBookService bookService;
    @RequestMapping(value = "/queryBookList",produces = "application/json;charset=utf-8")
    public ResponseBody queryBookList(@RequestBody String str) throws Exception {
        String aws = DESUtils.decryption(str,"mmhll1314520lhxq");
        Map<String,Object> map = JSON.parseObject(aws, HashMap.class);
        Map<String, Object> resultMap= bookService.queryBookList(map);
        return new AssembleResponseMsg().success(resultMap);
    }
    @RequestMapping(value = "/addSubBook",produces = "application/json;charset=utf-8")
    public ResponseBody addSubBook(@RequestBody String str) throws Exception {
        String aws = DESUtils.decryption(str,"mmhll1314520lhxq");
        Map<String,Object> map = JSON.parseObject(aws, HashMap.class);
        bookService.addSubBook(map);
        return new AssembleResponseMsg().success("OK");
    }
    @RequestMapping(value = "/upInventories",produces = "application/json;charset=utf-8")
    public ResponseBody upInventories(@RequestBody String str) throws Exception {
        String aws = DESUtils.decryption(str,"mmhll1314520lhxq");
        Map<String,Object> map = JSON.parseObject(aws, HashMap.class);
        bookService.upInventories(map);
        return new AssembleResponseMsg().success("OK");
    }
    @RequestMapping(value = "/querySub",produces = "application/json;charset=utf-8")
    public ResponseBody querySub(@RequestBody String str) throws Exception {
        String aws = DESUtils.decryption(str,"mmhll1314520lhxq");
        Map<String,Object> map = JSON.parseObject(aws, HashMap.class);
        Map<String, Object> resultMap=bookService.querySub(map);
        return new AssembleResponseMsg().success(resultMap);
    }
    @RequestMapping(value = "/addBook",produces = "application/json;charset=utf-8")
    public ResponseBody addBook(@RequestBody String str){
        try {
            String aws = DESUtils.decryption(str,"mmhll1314520lhxq");
            Map<String,Object> map = JSON.parseObject(aws, HashMap.class);
            bookService.addBook(map);
            return new AssembleResponseMsg().success("OK");
        } catch (Exception e) {
            return new AssembleResponseMsg().failure(200,"error","添加失败");
        }
    }
    @RequestMapping(value = "/batchAddBook",produces = "application/json;charset=utf-8")
    public ResponseBody batchAddBook(@RequestBody String str){
        try {
            String aws = DESUtils.decryption(str,"mmhll1314520lhxq");
            JSONObject  map = JSON.parseObject(aws);
            for(String key:map.keySet()) {
                JSONArray job = map.getJSONArray(key);
                System.out.println(job);
                for (int i = 0; i < job.size(); i++) {
                    JSONObject j = job.getJSONObject(i);
                    System.out.println(j);
                    bookService.batchAddBook(j);
                }
            }
            return new AssembleResponseMsg().success("OK");
        } catch (Exception e) {
            return new AssembleResponseMsg().failure(200,"error","添加失败");
        }
    }
    @RequestMapping(value = "/editBook",produces = "application/json;charset=utf-8")
    public ResponseBody editBook(@RequestBody String str){
        try {
            String aws = DESUtils.decryption(str,"mmhll1314520lhxq");
            Map<String,Object> map = JSON.parseObject(aws, HashMap.class);
            bookService.editBook(map);
            return new AssembleResponseMsg().success("OK");
        } catch (Exception e) {
            return new AssembleResponseMsg().failure(200,"error","添加失败");
        }
    }
    @RequestMapping(value = "/returnSubBook",produces = "application/json;charset=utf-8")
    public ResponseBody returnSubBook(@RequestBody String str){
        try {
            String aws = DESUtils.decryption(str,"mmhll1314520lhxq");
            Map<String,Object> map = JSON.parseObject(aws, HashMap.class);
            bookService.returnSubBook(map);
            return new AssembleResponseMsg().success("OK");
        } catch (Exception e) {
            return new AssembleResponseMsg().failure(200,"error","添加失败");
        }
    }
}
