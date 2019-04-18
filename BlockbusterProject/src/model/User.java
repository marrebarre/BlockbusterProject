package model;

public class User extends Account {
    private String firstName;
    private String lastName;
    private double balance;

    public User(String email, String password, boolean admin, String firstName, String lastName, double balance) {
        super(email, password, admin);
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
    }
}