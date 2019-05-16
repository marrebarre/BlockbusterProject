package controller;

import data.DbConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Logic;
import model.Movie;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
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

    private DbConnector dbConnector = new DbConnector();
    private Logic logic = new Logic();

    /*public void hoverOverPictureEntered(){
        aStarIsBorn.setFitHeight(250);
        aStarIsBorn.setFitWidth(250);

    }*/
    /*public void hoverOverPictureExit(){
        aStarIsBorn.setFitWidth(200);
        aStarIsBorn.setFitHeight(200);

    }*/
    /*public void mouseOverPicture(){
        inception.setFitHeight(250);
        inception.setFitWidth(250);
    }*/
    /*public void mouseOverPictureExit(){
        inception.setFitWidth(200);
        inception.setFitHeight(200);
    }*/

    public void btnPressedLogOut(MouseEvent event) throws IOException {
        Parent createAccountParent = FXMLLoader.load(getClass().getResource("/view/loginScreenRedux.fxml"));
        Scene createAccountScene = new Scene(createAccountParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setMaximized(false);
        window.setScene(createAccountScene);
        window.show();
        if (!dbConnector.users.isEmpty()){
            dbConnector.users.clear();
        } else {
            dbConnector.admins.clear();
        }
    }
    @FXML
    void handleSearchBtn(ActionEvent event) throws FileNotFoundException {
      List<Movie> movieTitle =  dbConnector.getMovieTitle(searchField.getText());
      // title should be displayed with matching photo :)
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
            if (choice == "Action"){
                dbConnector.searchMovieByGenre("Action");
                //Display images of movies.
            }
            else if (choice == "Adventure"){
                dbConnector.searchMovieByGenre("Adventure");
            }
            else if (choice == "Drama"){
                dbConnector.searchMovieByGenre("Drama");
            }
            else if (choice == "Family"){
                dbConnector.searchMovieByGenre("Family");
            }

        });

    }
}