package controller;

import data.DbConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.Logic;
import model.Movie;
import java.io.FileNotFoundException;
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
}