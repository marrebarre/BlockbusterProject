package model;

public abstract class Account {
    String email;
    String password;
    boolean admin;

    public Account(String email, String password, boolean admin) {
        this.email = email;
        this.password = password;
        this.admin = admin;
    }
}