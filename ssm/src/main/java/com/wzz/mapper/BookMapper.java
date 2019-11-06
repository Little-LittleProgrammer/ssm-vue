package com.wzz.mapper;

import com.wzz.model.Book;
import com.wzz.model.BookSub;

import java.util.List;
import java.util.Map;

public interface BookMapper {
    List<Book> queryBookList(Map<String,Object> map);

    void addSubBook(Map<String, Object> map);

    void upInventories(Map<String, Object> map);

    List<BookSub> querySub(Map<String,Object> map);

    void addBook(Map<String,Object> map);

    void editBook(Map<String,Object> map);

    void returnSubBook(Map<String,Object> map);
}
