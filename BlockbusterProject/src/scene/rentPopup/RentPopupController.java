package scene.rentPopup;

import database.DbConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Movie;
import model.User;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class RentPopupController implements Initializable {

    @FXML
    Label infoLbl, infoDates;
    @FXML
    ImageView rentImageView;
    @FXML
    TextField enterDaysOfRental;

    private LocalDate localDate = LocalDate.now();
    private static User balance;

    public static User getBalance() {
        return balance;
    }

    public static Movie movieToRent;

    private static Movie getMovieToRent() {
        return movieToRent;
    }

    private Date convertToDateFormat(LocalDate localDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dateTimeFormatter.format(localDate);
        return java.sql.Date.valueOf(localDate);
    }

    public static void setMovieToRent(Movie movieToRent) {
        RentPopupController.movieToRent = movieToRent;
    }

    public void close(ActionEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void rentalHandler(){
        DbConnector dbConnector = new DbConnector();
        dbConnector.addRental(getMovieToRent(), convertToDateFormat(localDate), convertToDateFormat(localDate.plusDays(Integer.parseInt(enterDaysOfRental.getText()))));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String inStock;
        if (movieToRent.getQuantity() <= 0){
            inStock = "Out of stock";
        } else {
            //inStock = Integer.toString(movieToRent.getQuantity());
            inStock = "In stock!";
        }
        infoLbl.setText(
                        "Title: " + movieToRent.getTitle() +
                        "\nDirector: " + movieToRent.getDirector() +
                        "\nGenre: " + movieToRent.getGenreAsString() +
                        "\nRelease Year: " + movieToRent.getReleaseYear() +
                        "\n\n" + "Status: " +  inStock +
                        "\n\nPrice: " + movieToRent.getPrice() +"$"
        );
        Image image = new Image(movieToRent.getImagePath());
        rentImageView.setImage(image);
    }

}