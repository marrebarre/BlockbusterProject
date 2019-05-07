package model;

public class Admin extends Account{
    private boolean isAdmin;

    public Admin(String email, String password, boolean isAdmin) {
        super(email, password);
        this.isAdmin = isAdmin;
    }
}
