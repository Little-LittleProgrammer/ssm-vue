package com.wzz.service;

import java.util.Map;

public interface IBookService {
    Map<String,Object> queryBookList(Map<String, Object> map);

    void addSubBook(Map<String, Object> map);

    void upInventories(Map<String, Object> map);

    Map<String,Object> querySub(Map<String, Object> map);

    void addBook(Map<String,Object> map);

    void batchAddBook(Map<String,Object> map);

    void editBook(Map<String,Object> map);

    void returnSubBook(Map<String,Object> map);
}
