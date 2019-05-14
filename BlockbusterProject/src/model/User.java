package model;

public class User extends Account {
    private String firstName;
    private String lastName;
    private double balance;
    private String address;
    private String phoneNr;
    private int idUser;


    public User(String email, String password, boolean admin, String firstName, String lastName, double balance, String address, String phoneNr, int idUser) {
        super(email, password, admin);
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
        this.address = address;
        this.phoneNr = phoneNr;
        this.idUser = idUser;
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

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }
}