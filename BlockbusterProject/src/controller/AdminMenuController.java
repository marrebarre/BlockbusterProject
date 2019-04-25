package controller;

import data.DbConnector;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Logic;
import model.Movie;

import java.net.URL;
import java.util.InputMismatchException;
import java.util.ResourceBundle;


public class AdminMenuController implements Initializable {

@FXML
    ComboBox<Movie.Genre> genreComboBox = new ComboBox<>();

@FXML
    TextField titleTxt, directorTxt, priceTxt;

    DbConnector dbConnector = new DbConnector();


    public void addMoviePressed(){

        try {
            Movie movie = new Movie(titleTxt.getText(), directorTxt.getText(), Double.parseDouble(priceTxt.getText()), genreComboBox.getValue());

            dbConnector.connect();
            dbConnector.addMovieToDB(movie);
            dbConnector.disconnect();
        }catch (IllegalArgumentException e){
            System.out.println("Wrong datatype bruh");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("DataTypeMismatch");
            alert.setContentText("Please double-check the format");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        genreComboBox.getItems().addAll(Movie.Genre.Action, Movie.Genre.Family, Movie.Genre.Adventure, Movie.Genre.Drama, Movie.Genre.Horror, Movie.Genre.Scifi);
    }
}
