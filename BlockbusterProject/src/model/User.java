package model;

public class User extends Account {
    private String firstName;
    private String lastName;
    private double balance;
    private String address;
    private String phone;

    public User(String email, String password, boolean admin, String firstName, String lastName, double balance, String address, String phone) {
        super(email, password, admin);
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
        this.address = address;
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public double getBalance() {
        return balance;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}