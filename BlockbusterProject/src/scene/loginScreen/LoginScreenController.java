package scene.loginScreen;

import database.DbConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.Logic;
import java.net.URL;
import java.util.*;

import static scene.userMenu.UserMenuController.loggedInUser;

public class LoginScreenController implements Initializable {
    @FXML
    TextField username = new TextField();
    @FXML
    PasswordField password = new PasswordField();
    @FXML
    Label forgotPW = new Label();
    @FXML
    Button signIn, btnCreateAccount;
    @FXML
    ImageView logo;

    private DbConnector dbConnector = new DbConnector();
    private Logic logic = new Logic();

    public void handleLogin(ActionEvent event) {
        dbConnector.connect();
        try {
            if (username.getText().isEmpty() || password.getText().isEmpty()) {
                //System.out.println("Email and/or password not entered.");
                username.setStyle("-fx-prompt-text-fill: red");
                password.setStyle("-fx-prompt-text-fill: red");
            } else {
                if (dbConnector.verifyAccount(username.getText(), password.getText()) && !dbConnector.admins.isEmpty()) {
                    String adminMenuFXML = "/scene/adminMenu/adminMenu.fxml";
                    logic.changeSceneHandler(event, adminMenuFXML, true);
                } else if (!username.getText().isEmpty() && !password.getText().isEmpty()) {
                    if (dbConnector.verifyAccount(username.getText(), password.getText()) && !loggedInUser.isAdmin()) {
                        String userMenuFXML = "/scene/userMenu/userMenu.fxml";
                        logic.changeSceneHandler(event, userMenuFXML, true);
                    }
                }
            }
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Invalid email or password", ButtonType.OK);
            alert.setTitle("Login failed");
            alert.showAndWait();
            System.out.println("Match to entered data not found within DB. Error thrown: " + e.toString());
        } finally {
            dbConnector.disconnect();
        }
    }

    public void btnPressedCreateAccount(ActionEvent event) {
        String createAccountFXML = "/scene/createAccount/createAccountScreen.fxml";
        logic.changeSceneHandler(event, createAccountFXML, true);
    }

    public void btnPressedForgotPW(MouseEvent event) {
        String forgotPasswordFXML = "/scene/forgotPassword/forgotPW.fxml";
        logic.changeSceneHandler(event, forgotPasswordFXML, false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image("image/BustblockerLogo.png");
        logo.setImage(image);
    }
}