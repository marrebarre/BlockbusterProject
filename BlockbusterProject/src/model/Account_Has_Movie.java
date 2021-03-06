package model;


public class Account_Has_Movie extends Movie{
    private int rentalID;
    private int account_idUser;
    private int movie_idMovie;
    private String rented;
    private String estimatedReturnDate;
    private double fee;
    private boolean returned;

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Account_Has_Movie(int idMovie, String title, String director, double price, Genre genre, String releaseYear, int quantity, String imagePath, int rentalID, int account_idUser, int movie_idMovie, String rented, String estimatedReturnDate, double fee, boolean returned) {
        super(idMovie, title, director, price, genre, releaseYear, quantity, imagePath);

        this.rentalID = rentalID;
        this.account_idUser = account_idUser;
        this.movie_idMovie = movie_idMovie;
        this.rented = rented;
        this.estimatedReturnDate = estimatedReturnDate;
        this.fee = fee;
        this.returned = returned;
    }

    public void setAccount_idUser(int account_idUser) {
        this.account_idUser = account_idUser;
    }

    public void setRentalID(int rentalID) {
        this.rentalID = rentalID;
    }

    public void setMovie_idMovie(int movie_idMovie) {
        this.movie_idMovie = movie_idMovie;
    }

    public void setRented(String rented) {
        this.rented = rented;
    }

    public void setEstimatedReturnDate(String estimatedReturnDate) {
        this.estimatedReturnDate = estimatedReturnDate;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public int getAccount_idUser() {
        return account_idUser;
    }

    public int getRentalID() {
        return rentalID;
    }

    public int getMovie_idMovie() {
        return movie_idMovie;
    }

    public String getRented() {
        return rented;
    }

    public String getEstimatedReturnDate() {
        return estimatedReturnDate;
    }

    public double getFee() {
        return fee;
    }

    public boolean getReturned() {
        return returned;
    }

    public String toString(){
        return "\n---------------------------------------------------\n"+"\nRental ID: "+rentalID + "\nUser ID: " +account_idUser+ " \nMovie ID: "+ movie_idMovie +"\nDate of Rent: "+rented+
                "\nEstimated Return Date: "+estimatedReturnDate+"\nFee: "+fee+"\n"+"\n---------------------------------------------------";
    }
}
