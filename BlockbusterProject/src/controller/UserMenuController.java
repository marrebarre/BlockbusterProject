package controller;

import data.DbConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.stage.Screen;
import model.Logic;
import model.Movie;
import model.User;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class UserMenuController implements Initializable {
    @FXML
    private TextField firstNameText, lastNameText, emailText, balanceText, addressText, phoneNumberText;

    @FXML
    Button btnLogOut, updateBtn, btnSearch;

    @FXML
    private ComboBox<String> sortBox;

    @FXML
    private TextField searchField;

    @FXML
    TilePane tilePaneBrowse;

    @FXML
    ScrollPane scrollPane;

    @FXML
    Label lblWelcomeMessage;

    private DbConnector dbConnector = new DbConnector();
    private Logic logic = new Logic();
    public static User loggedInUser;

    public void btnPressedLogOut(MouseEvent event) {
        String logOutFXML = "/view/loginScreenRedux.fxml";
        logic.changeSceneHandler(event, logOutFXML, false);
        if (!dbConnector.users.isEmpty()) {
            dbConnector.users.clear();
        } else {
            dbConnector.admins.clear();
        }
    }

    @FXML
    public void handleSearchBtn(ActionEvent event) throws FileNotFoundException {
        List<Movie> movieTitle = dbConnector.getMovieTitle(searchField.getText());
    }

    @FXML
    public void loadBrowse() {
        dbConnector.connect();
        try {
            PreparedStatement ps = dbConnector.connection.prepareStatement("SELECT * FROM movie");
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
                    RentMovieController.setMovieToRent(movie);
                    logic.openSceneInNewWindow("/view/rentMovie.fxml", "Rent Movie");
                });
                tempTilePane.getChildren().addAll(tempImageView);
                tilePaneBrowse.getChildren().add(tempTilePane);
                tilePaneBrowse.setPrefColumns(10);
            }
        } catch (Exception e) {
            System.out.println("ohShit");
            e.printStackTrace();
        } finally {
            dbConnector.disconnect();
        }
    }

    @FXML
    void handleSortBox(ActionEvent event) {
//?
    }

    @FXML
    void settingsHandleUpdateBtn() throws SQLException {
        if (!firstNameText.getText().equals("") && !firstNameText.getText().equals(loggedInUser.getFirstName())){
            loggedInUser.setFirstName(firstNameText.getText());
            dbConnector.updateFirstName(loggedInUser.getIdUser(), loggedInUser);
        }
        if (!lastNameText.getText().equals("")&& !lastNameText.getText().equals(loggedInUser.getLastName())){
            loggedInUser.setLastName(lastNameText.getText());
            dbConnector.updateLastName(loggedInUser.getIdUser(), loggedInUser);
        }
        if (!emailText.getText().equals("")&& !emailText.getText().equals(loggedInUser.getEmail())){
            loggedInUser.setEmail(emailText.getText());
            dbConnector.updateEmail(loggedInUser.getIdUser(), loggedInUser);
        }
        if (!addressText.getText().equals("")&& !addressText.getText().equals(loggedInUser.getAddress())){
            loggedInUser.setAddress(addressText.getText());
            dbConnector.updateAddress(loggedInUser.getIdUser(), loggedInUser);
        }
        if (!phoneNumberText.getText().equals("")&& !phoneNumberText.getText().equals(loggedInUser.getPhoneNr())){
            loggedInUser.setPhoneNr(phoneNumberText.getText());
            dbConnector.updatePhoneNumber(loggedInUser.getIdUser(), loggedInUser);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        scrollPane.setLayoutX(primaryScreenBounds.getMinX());
        scrollPane.setLayoutY(primaryScreenBounds.getMinY() + 50);
        scrollPane.setPrefWidth(primaryScreenBounds.getWidth());
        scrollPane.setPrefHeight(primaryScreenBounds.getHeight() - 115);
        loadBrowse();
        firstNameText.setText(loggedInUser.getFirstName());
        lastNameText.setText(loggedInUser.getLastName());
        emailText.setText(loggedInUser.getEmail());
        addressText.setText(loggedInUser.getAddress());
        phoneNumberText.setText(loggedInUser.getPhoneNr());
        lblWelcomeMessage.setText("Welcome to our store, " + loggedInUser.getFirstName() + " " + loggedInUser.getLastName() + "!");
        sortBox.getItems().add("Action");
        sortBox.getItems().add("Adventure");
        sortBox.getItems().add("Drama");
        sortBox.getItems().add("Family");
        sortBox.setOnAction(event -> {
            String choice = sortBox.getSelectionModel().getSelectedItem();
            switch (choice) {
                case "Action":
                    dbConnector.searchMovieByGenre("Action");
                    //Display images of movies.
                    break;
                case "Adventure":
                    dbConnector.searchMovieByGenre("Adventure");
                    break;
                case "Drama":
                    dbConnector.searchMovieByGenre("Drama");
                    break;
                case "Family":
                    dbConnector.searchMovieByGenre("Family");
                    break;
            }
        });
    }
}