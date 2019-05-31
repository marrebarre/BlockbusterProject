package model;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.scene.control.Alert;
import scene.rentPopup.RentPopupController;
/*import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;*/
import database.DbConnector;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import scene.rentPopup.RentPopupController;
import scene.userMenu.UserMenuController;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;

import static scene.userMenu.UserMenuController.df;
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
            Scene scene = new Scene(parent1);
            stage.setMaximized(maximizeScene);
            if (maximizeScene){
                setToFullscreen(stage);
            }
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openSceneInNewWindow(String fxmlPath ,String title){
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

    public void loadBrowsePageData(String query, TilePane tilePane){
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

    public  void pdf(){
        com.itextpdf.text.Document document = new Document();
        try
        {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Receipt.pdf"));
            document.open();
            document.add(new Paragraph("Receipt of Rentals"));
            String rentals = String.valueOf(dbConnector.showRentals(loggedInUser.getIdUser()));
            document.add(new Paragraph(String.valueOf(dbConnector.showRentals(loggedInUser.getIdUser()))));
            document.close();
            writer.close();
        } catch (DocumentException e)
        {
            e.printStackTrace();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /* krille - work in progress
    public void generatePassword(){
        int length = 10;
        String symbol = "-/.^&*_!@%=+>)";
        String cap_letter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String small_letter = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        String finalString = cap_letter + small_letter +
                numbers + symbol;

        Random random = new Random();

        char[] password = new char[length];

        for (int i = 0; i < length; i++)
        {
            password[i] =
                    finalString.charAt(random.nextInt(finalString.length()));
        }
        System.out.println(password);
    }
    */
}