package scene.rentPopup;

import database.DbConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Movie;
import scene.userMenu.UserMenuController;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
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
    @FXML
    public CheckBox smsCheck;
    @FXML
    public Label myBalance;
    @FXML
    public Label priceTag;

    public static double tmpPrice;

    private LocalDate localDate = LocalDate.now();

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

    public void updatePrice(){
        try {
            tmpPrice = movieToRent.getPrice() + 0.2 * Double.parseDouble(enterDaysOfRental.getText());
            myBalance.setText("Current balance: "+String.valueOf(UserMenuController.df.format(UserMenuController.loggedInUser.getBalance()))+"$\nAfter purchase:  "+UserMenuController.df.format(UserMenuController.loggedInUser.getBalance()-tmpPrice)+"$");
            priceTag.setText("Price: " + String.valueOf(UserMenuController.df.format(movieToRent.getPrice() + 0.2 * Double.parseDouble(enterDaysOfRental.getText()))));
        }catch (Exception e){

        }
    }

    public void rentalHandler(ActionEvent event){
        DbConnector dbConnector = new DbConnector();
        dbConnector.connect();
        dbConnector.addRental(getMovieToRent(), convertToDateFormat(localDate), convertToDateFormat(localDate.plusDays(Integer.parseInt(enterDaysOfRental.getText()))), event);
        if (DbConnector.verify && smsCheck.isSelected()){
            try {
                dbConnector.textMessageHandler();
            } catch (SQLException e) {
                System.out.println("rentalHandler error SMS-check");
                e.printStackTrace();
            }
        }
        DbConnector.setVerify(true);
        dbConnector.disconnect();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String inStock;
        if (movieToRent.getQuantity() <= 0){
            inStock = "Out of stock";
        } else {
            inStock = Integer.toString(movieToRent.getQuantity());
        }
        infoLbl.setText(
                        "Title: " + movieToRent.getTitle() +
                        "\nDirector: " + movieToRent.getDirector() +
                        "\nGenre: " + movieToRent.getGenreAsString() +
                        "\nRelease Year: " + movieToRent.getReleaseYear() +
                        "\n\n" + "Amount in stock: " +  inStock
        );
        priceTag.setText("Price: "+String.valueOf(UserMenuController.df.format(movieToRent.getPrice())));
        Image image = new Image(movieToRent.getImagePath());
        rentImageView.setImage(image);
        myBalance.setText("Current balance: "+String.valueOf(UserMenuController.df.format(UserMenuController.loggedInUser.getBalance()))+"$\nAfter purchase:  "+UserMenuController.df.format(UserMenuController.loggedInUser.getBalance()-tmpPrice)+"$");
    }
}