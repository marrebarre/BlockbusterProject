package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import model.Movie;
import model.User;

import java.net.URL;
import java.util.ResourceBundle;

import static controller.AdminMenuController.alert;

public class RentMovieController implements Initializable {

    @FXML
    Label infoLbl;

    public static User balance;

    public static User getBalance() {
        return balance;
    }

    public static Movie movieToRent;

    public static Movie getMovieToRent() {
        return movieToRent;
    }

    public static void setMovieToRent(Movie movieToRent) {
        RentMovieController.movieToRent = movieToRent;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Boolean inStock;
        if (movieToRent.getQuantity() > 0) {
            inStock = true;
        } else {
            inStock = false;
        }
        infoLbl.setText(
                        "Title: " + movieToRent.getTitle() +
                        "\nDirector: " + movieToRent.getDirector() +
                        "\nGenre: " + movieToRent.getGenreAsString() +
                        "\nRelease Year: " + String.valueOf(movieToRent.getReleaseYear()) +
                        "\nIn Stock: " + inStock +
                        "\nPrice: " + String.valueOf(movieToRent.getPrice())
        );
    }
   /* public void rentPressed(){

        if (movieToRent.getQuantity() > 0 && balance.getBalance() >= movieToRent.getPrice() ){
            alert("You purchase was succesful!", Alert.AlertType.CONFIRMATION);

          balance.getBalance() = balance.getBalance() - movieToRent.getPrice();

        }else{
            alert("You don't have enough credits to preform this purchase", Alert.AlertType.INFORMATION);
        }
    }*/
}