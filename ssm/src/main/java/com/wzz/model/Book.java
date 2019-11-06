package com.wzz.model;

import java.io.Serializable;

public class Book implements Serializable {
    private int bId;
    private String bookName;
    private String author;
    private int inventory;
    private double price;

    public int getbId() {
        return bId;
    }

    public void setbId(int bId) {
        this.bId = bId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bId=" + bId +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                ", inventory=" + inventory +
                ", price=" + price +
                '}';
    }
}
