package com.wzz.controller;

import com.wzz.common.AssembleResponseMsg;
import com.wzz.model.ResponseBody;
import com.wzz.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class BookController {
    @Autowired
    private IBookService bookService;
    @RequestMapping(value = "/queryBookList",produces = "application/json;charset=utf-8")
    public ResponseBody queryBookList(@RequestBody Map<String,Object> map){
        Map<String, Object> resultMap= bookService.queryBookList(map);
        return new AssembleResponseMsg().success(resultMap);
    }
    @RequestMapping(value = "/addSubBook",produces = "application/json;charset=utf-8")
    public ResponseBody addSubBook(@RequestBody Map<String,Object> map){
        bookService.addSubBook(map);
        return new AssembleResponseMsg().success("OK");
    }
    @RequestMapping(value = "/upInventories",produces = "application/json;charset=utf-8")
    public ResponseBody upInventories(@RequestBody Map<String,Object> map){
        bookService.upInventories(map);
        return new AssembleResponseMsg().success("OK");
    }
    @RequestMapping(value = "/querySub",produces = "application/json;charset=utf-8")
    public ResponseBody querySub(@RequestBody Map<String,Object> map){
        Map<String, Object> resultMap=bookService.querySub(map);
        return new AssembleResponseMsg().success(resultMap);
    }
    @RequestMapping(value = "/addBook",produces = "application/json;charset=utf-8")
    public ResponseBody addBook(@RequestBody Map<String,Object> map){
        try {
            bookService.addBook(map);
            return new AssembleResponseMsg().success("OK");
        } catch (Exception e) {
            return new AssembleResponseMsg().failure(200,"error","添加失败");
        }
    }
    @RequestMapping(value = "/editBook",produces = "application/json;charset=utf-8")
    public ResponseBody editBook(@RequestBody Map<String,Object> map){
        try {
            bookService.editBook(map);
            return new AssembleResponseMsg().success("OK");
        } catch (Exception e) {
            return new AssembleResponseMsg().failure(200,"error","添加失败");
        }
    }
    @RequestMapping(value = "/returnSubBook",produces = "application/json;charset=utf-8")
    public ResponseBody returnSubBook(@RequestBody Map<String,Object> map){
        try {
            bookService.returnSubBook(map);
            return new AssembleResponseMsg().success("OK");
        } catch (Exception e) {
            return new AssembleResponseMsg().failure(200,"error","添加失败");
        }
    }
}
