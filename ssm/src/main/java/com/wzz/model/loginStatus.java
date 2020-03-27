package com.wzz.model;

public class loginStatus {
    private int id;
    private String token;
    private String userName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "loginStatus{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
