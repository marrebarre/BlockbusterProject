package model;

public abstract class Account {
    String email;
    String password;

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }

}