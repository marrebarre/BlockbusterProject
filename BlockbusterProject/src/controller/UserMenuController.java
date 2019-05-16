package controller;

import data.DbConnector;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import model.Logic;
import model.Movie;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.ResourceBundle;

public class UserMenuController implements Initializable {
    @FXML
    ImageView aStarIsBorn, inception;

    @FXML
    Button btnLogOut;

    @FXML
    private ComboBox<String> sortBox;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchBtn;

    @FXML
    TilePane tilePaneBrowse;


    private DbConnector dbConnector = new DbConnector();
    private Logic logic = new Logic();

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
    void handleSearchBtn(ActionEvent event) throws FileNotFoundException {
        List<Movie> movieTitle = dbConnector.getMovieTitle(searchField.getText());
        // title should be displayed with matching photo :)
    }

    public void loadBrowse() {
        dbConnector.connect();
        try {
            PreparedStatement ps = dbConnector.connection.prepareStatement("SELECT * FROM movie");
            dbConnector.resultSet = ps.executeQuery();

            while (dbConnector.resultSet.next()) {
                Movie movie = new Movie(
                        dbConnector.resultSet.getInt("idMovie"),
                        dbConnector.resultSet.getString("title"),
                        dbConnector.resultSet.getString("director"),
                        dbConnector.resultSet.getDouble("price"),
                        Movie.getStringAsGenre(dbConnector.resultSet.getString("genre")),
                        dbConnector.resultSet.getString("releaseYear"),
                        dbConnector.resultSet.getInt("quantity")
                );
                TilePane tempTilePane = new TilePane();
                tempTilePane.setPrefColumns(1);
                tempTilePane.setPrefRows(3);
                tempTilePane.setPadding(new Insets(30));

                Label tempLabel = new Label();
                tempLabel.setText(movie.getTitle());
                tempLabel.setStyle("-fx-font-size: 24;");

                ImageView tempImageView = new ImageView();
                tempImageView.setFitHeight(160);
                tempImageView.setFitWidth(110);
                Image image = new Image("/Images/dvbackground.jpg");
                tempImageView.setImage(image);
                tempImageView.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override public void handle(MouseEvent e) {
                        RentMovieController.setMovieToRent(movie);
                        logic.openSceneInNewWindow("/view/rentMovie.fxml","Rent Movie");
                    }

                });

                Button tempButton = new Button();
                tempButton.setText("Rent");
                tempButton.setOnAction(new EventHandler<ActionEvent>(){
                    @Override public void handle(ActionEvent e) {
                        RentMovieController.setMovieToRent(movie);
                        logic.openSceneInNewWindow("/view/rentMovie.fxml","Rent Movie");
                    }

                });

                tempTilePane.getChildren().addAll( tempLabel, tempImageView, tempButton);
                tilePaneBrowse.getChildren().add(tempTilePane);
                tilePaneBrowse.setPrefColumns(5);

            }
        }catch (Exception e){
            System.out.println("ohShit");
        }finally {
            dbConnector.disconnect();
        }
    }

    @FXML
    void handleSortBox(ActionEvent event) {
//?
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sortBox.getItems().add("Action");
        sortBox.getItems().add("Adventure");
        sortBox.getItems().add("Drama");
        sortBox.getItems().add("Family");
        sortBox.setOnAction(event -> {
            String choice = sortBox.getSelectionModel().getSelectedItem();
            if (choice == "Action") {
                dbConnector.searchMovieByGenre("Action");
                //Display images of movies.
            } else if (choice == "Adventure") {
                dbConnector.searchMovieByGenre("Adventure");
            } else if (choice == "Drama") {
                dbConnector.searchMovieByGenre("Drama");
            } else if (choice == "Family") {
                dbConnector.searchMovieByGenre("Family");
            }
        });
    }
}