package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.Movie;

import java.net.URL;
import java.util.ResourceBundle;

public class RentMovieController implements Initializable {

    @FXML
    Label infoLbl;

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
}
