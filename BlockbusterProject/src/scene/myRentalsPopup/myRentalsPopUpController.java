package scene.myRentalsPopup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Account_Has_Movie;
import model.Movie;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import static scene.rentPopup.RentPopupController.movieToRent;

public class myRentalsPopUpController implements Initializable {
    @FXML
    private ImageView myRentedMovie = new ImageView();

    @FXML
    private Label movieData, estReturn;

    public static Account_Has_Movie movieRented;


    public static void setRentedMovie(Movie movieSelected) {
        movieToRent = movieSelected;
    }

    public static void setMovieRented (Account_Has_Movie accountHasMovie){
        movieRented = accountHasMovie;
    }

    public void displayRentedMovie(){

    }

    public void close(ActionEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LocalDate todayDate = LocalDate.now();
        LocalDate returnDate = LocalDate.parse(movieRented.getEstimatedReturnDate());
        long daysBetween = ChronoUnit.DAYS.between(todayDate,returnDate);
        movieRented.setFee(0);
        if (returnDate.isAfter(todayDate)){
            movieRented.setFee(daysBetween);
        }
        movieData.setText(
                "Title: " + movieRented.getTitle() +
                        "\nDirector: " + movieRented.getDirector() +
                        "\nGenre: " + movieRented.getGenreAsString() +
                        "\nRelease Year: " + movieRented.getReleaseYear() +
                        "\n\nCurrent fee: "  +movieRented.getFee()+"$" +
                        "\nRental date: " + movieRented.getRented()
        );
        estReturn.setText("Return before: " + movieRented.getEstimatedReturnDate());
        Image image = new Image(movieRented.getImagePath());
        myRentedMovie.setImage(image);
    }
}