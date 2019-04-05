package model;

public abstract class Account {
    String username;
    String password;
    boolean admin;

    public Account(String username, String password, boolean admin) {
        this.username = username;
        this.password = password;
        this.admin = admin;
    }
}