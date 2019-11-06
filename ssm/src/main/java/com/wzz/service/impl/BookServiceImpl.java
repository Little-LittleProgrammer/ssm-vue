package com.wzz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wzz.mapper.BookMapper;
import com.wzz.model.Book;
import com.wzz.model.BookSub;
import com.wzz.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookServiceImpl implements IBookService {
    @Autowired
    private BookMapper bookMapper;
    @Override
    public Map<String, Object> queryBookList(Map<String, Object> map) {
        int pageNum=Integer.parseInt(map.get("pageNum").toString()); //当前页
        int pageSize=Integer.parseInt(map.get("pageSize").toString());  //每页几条
        PageHelper.startPage(pageNum,pageSize);
        List<Book> bookList = bookMapper.queryBookList(map);
        PageInfo pageInfo=new PageInfo(bookList);
        long total = pageInfo.getTotal();
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("total",total);
        resultMap.put("rows",bookList);
        return resultMap;
    }

    @Override
    public void addSubBook(Map<String, Object> map) {
        bookMapper.addSubBook(map);
    }

    @Override
    public void upInventories(Map<String, Object> map) {
        bookMapper.upInventories(map);
    }

    @Override
    public Map<String, Object> querySub(Map<String, Object> map) {
        List<BookSub> bookSubList = bookMapper.querySub(map);
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("rows",bookSubList);
        return resultMap;
    }

    @Override
    public void addBook(Map<String, Object> map) {
        bookMapper.addBook(map);
    }

    @Override
    public void editBook(Map<String, Object> map) {
        bookMapper.editBook(map);
    }

    @Override
    public void returnSubBook(Map<String, Object> map) {
        bookMapper.returnSubBook(map);
    }


}
