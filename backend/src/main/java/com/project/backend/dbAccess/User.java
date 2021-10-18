package com.project.backend.dbAccess;

import org.springframework.data.annotation.Id;

public class User {

    @Id
    private String id;

    private String userName;
    private String password;
    private int accBalance;
    private String role;

    public User(String userName, String password, int accBalance, String role) {
        this.userName = userName;
        this.password = password;
        this.accBalance = accBalance;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getAccBalance() {
        return accBalance;
    }

    public void setAccBalance(int accBalance) {
        this.accBalance = accBalance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
