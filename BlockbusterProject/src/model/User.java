package model;

public class User extends Account {
    private String firstName;
    private String lastName;
    private double balance;
    private String address;
    private String phoneNr;
    private int idUser;
    private boolean admin;

    public User(String email, String password,int idUser, String firstName, String lastName, double balance, String address, String phoneNr, boolean admin) {
        super(email, password);
        this.idUser = idUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
        this.address = address;
        this.phoneNr = phoneNr;
        this.admin = admin;
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

    public String getPhoneNr() {
        return phoneNr;
    }

    public int getIdUser() {
        return idUser;
    }

    public boolean isAdmin() {
        return admin;
    }
}