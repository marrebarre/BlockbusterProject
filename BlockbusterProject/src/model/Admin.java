package model;

public class Admin extends User {
    public Admin(String email, String password, boolean admin, String firstName, String lastName, double balance, String address, String phoneNr, int idUser) {
        super(email, password, admin, firstName, lastName, balance, address, phoneNr, idUser);
    }
}
