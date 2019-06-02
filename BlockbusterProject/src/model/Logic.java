package model;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.scene.control.Alert;
import scene.myRentalsPopup.myRentalsPopUpController;
import scene.rentPopup.RentPopupController;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;
import scene.rentPopup.RentPopupController;

import database.DbConnector;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static java.lang.Math.random;
import static scene.userMenu.UserMenuController.loggedInUser;

public class Logic {
    DbConnector dbConnector = new DbConnector();

    public static void alert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.setHeaderText("We got a message for you");
        alert.setTitle("ErrorBuster 9000");
        alert.showAndWait();
    }

    private void setToFullscreen(Stage stage) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
    }

    public void changeSceneHandler(Event actionEvent, String addressForFXML, boolean maximizeScene) {
        try {
            Parent parent1 = FXMLLoader.load(getClass().getResource(addressForFXML));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(parent1,600,400);
            stage.setScene(scene);
            if (maximizeScene) {
                setToFullscreen(stage);
                stage.setMaximized(maximizeScene);
            }else{
                stage.setMaximized(maximizeScene);
            }
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void openSceneInNewWindow(String fxmlPath, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(fxmlPath));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadBrowsePageData(String query, TilePane tilePane) {
        dbConnector.connect();
        try {
            PreparedStatement ps = dbConnector.connection.prepareStatement(query);
            dbConnector.resultSet = ps.executeQuery();
            while (dbConnector.resultSet.next()) {
                String imagePath;
                Movie movie = new Movie(
                        dbConnector.resultSet.getInt("idMovie"),
                        dbConnector.resultSet.getString("title"),
                        dbConnector.resultSet.getString("director"),
                        dbConnector.resultSet.getDouble("price"),
                        Movie.getStringAsGenre(dbConnector.resultSet.getString("genre")),
                        dbConnector.resultSet.getString("releaseYear"),
                        dbConnector.resultSet.getInt("quantity"),
                        imagePath = dbConnector.resultSet.getString("imagePath")
                );
                TilePane tempTilePane = new TilePane();
                tempTilePane.setPrefColumns(1);
                tempTilePane.setPrefRows(5);
                tempTilePane.setPadding(new Insets(30));
                ImageView tempImageView = new ImageView();
                tempImageView.getStyleClass().add("image-view-user-menu");
                tempImageView.setFitHeight(200);
                tempImageView.setFitWidth(133);
                Image image = new Image(imagePath);
                tempImageView.setImage(image);
                tempImageView.setOnMouseClicked(e -> {
                    RentPopupController.setMovieToRent(movie);
                    openSceneInNewWindow("/scene/rentPopup/rentPopup.fxml", "Rent Movie");
                });
                tempTilePane.getChildren().addAll(tempImageView);
                tilePane.getChildren().add(tempTilePane);
                tilePane.setPrefColumns(10);
            }
        } catch (Exception e) {
            System.out.println("ohShit");
            e.printStackTrace();
        } finally {
            dbConnector.disconnect();
        }
    }


    public void loadRentals(TilePane tilePane) {
        dbConnector.connect();
        String SQLQuery = "SELECT * from movie INNER JOIN account_has_movie ON movie.idMovie = account_has_movie.movie_idMovie WHERE account_has_movie.account_idUser = ?";
        ResultSet resultSetRental;
        try {
            PreparedStatement ps = dbConnector.connection.prepareStatement(SQLQuery);
            ps.setInt(1, loggedInUser.getIdUser());
            resultSetRental = ps.executeQuery();
            while (resultSetRental.next()) {
                String imagePath;
                Account_Has_Movie account_has_movie = new Account_Has_Movie(
                        resultSetRental.getInt("movie_idMovie"),
                        resultSetRental.getString("title"),
                        resultSetRental.getString("director"),
                        resultSetRental.getDouble("price"),
                        Movie.getStringAsGenre(resultSetRental.getString("genre")),
                        resultSetRental.getString("releaseYear"),
                        resultSetRental.getInt("quantity"),
                        imagePath = resultSetRental.getString("imagePath"),
                        resultSetRental.getInt("rentalID"),
                        resultSetRental.getInt("account_idUser"),
                        resultSetRental.getInt("movie_idMovie"),
                        resultSetRental.getString("dateRented"),
                        resultSetRental.getString("estimatedDateOfReturned"),
                        resultSetRental.getDouble("fee"),
                        resultSetRental.getBoolean("returned")
                );
                TilePane tempTilePane = new TilePane();
                tempTilePane.setPrefColumns(1);
                tempTilePane.setPrefRows(5);
                tempTilePane.setPadding(new Insets(30));
                ImageView tempImageView = new ImageView();
                tempImageView.getStyleClass().add("image-view-user-menu");
                tempImageView.setFitHeight(200);
                tempImageView.setFitWidth(133);
                Image image = new Image(imagePath);
                tempImageView.setImage(image);
                tempImageView.setOnMouseClicked(e -> {
                    myRentalsPopUpController.setMovieRented(account_has_movie);
                    openSceneInNewWindow("/scene/myRentalsPopup/myRentalsPopUp.fxml", "Rented movie");
                });
                tempTilePane.getChildren().addAll(tempImageView);
                tilePane.getChildren().add(tempTilePane);
                tilePane.setPrefColumns(10);
            }
        } catch (Exception e) {
            System.out.println("ohShit");
            e.printStackTrace();
        } finally {
            dbConnector.disconnect();
        }
    }

    public static final String ACCOUNT_SID = "AC6f314e8681deaa0ae4d82eaf59876daa";
    public static final String AUTH_TOKEN = "79be81f419784528cdb25a38226909df";

    public static void textMessageHandler() {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message
                .creator(new PhoneNumber("+46734453860"), // to
                        new PhoneNumber("+46769448476"), // from
                        "Test text")
                .create();

        System.out.println(message.getSid());
    }

    public  void pdf(){
        com.itextpdf.text.Document document = new Document();
        try
        {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Receipt.pdf"));
            document.open();
            document.add(new Paragraph("Receipt of Rentals"));
            String rentals = String.valueOf(dbConnector.showRentals(loggedInUser.getIdUser()));
            document.add(new Paragraph(String.valueOf(rentals)));
            document.close();
            writer.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // krille - work in progress
    public static String generatePassword(){
        int n = 8 ;
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";


        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

}