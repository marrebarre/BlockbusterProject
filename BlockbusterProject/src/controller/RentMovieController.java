package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import model.Movie;
import model.User;

import java.net.URL;
import java.util.ResourceBundle;

import static controller.AdminMenuController.alert;

public class RentMovieController implements Initializable {

    @FXML
    Label infoLbl;
    @FXML
    ImageView rentImageView;

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

    public void close(ActionEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
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
                        "\n\nIn Stock: " + inStock +
                        "\n\nPrice: " + String.valueOf(movieToRent.getPrice())+"$"
        );

        Image image = new Image(movieToRent.getImagePath());
        rentImageView.setImage(image);
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