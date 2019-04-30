package controller;

import data.DbConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.Admin;
import model.Logic;

import java.io.IOException;
import java.sql.*;

public class LoginScreenController {
    @FXML
    public TextField username = new TextField();

    @FXML
    public TextField password = new TextField();

    @FXML
    Label forgotPW = new Label();

    @FXML
    public Label isConnected = new Label();

    @FXML
    Button signIn;

    @FXML
    Button btnCreateAccount;

    DbConnector dbConnector = new DbConnector();

    public void handleLogin(ActionEvent event) {
        if (username.getText().equals(dbConnector.getEmail(username.getText())) && password.getText().equals(dbConnector.getPassword(password.getText()))) {

            try {
                Parent mainMenuAdmin = FXMLLoader.load(getClass().getResource("/view/adminMenu.fxml"));
                Stage adminMainMenu = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene loginScreen = new Scene(mainMenuAdmin);

                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getVisualBounds();
                adminMainMenu.setWidth(bounds.getWidth());
                adminMainMenu.setHeight(bounds.getHeight());

                adminMainMenu.setMaximized(true);
                adminMainMenu.setScene(loginScreen);
                adminMainMenu.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.NONE, "Invalid Email or Password entered", ButtonType.OK);
            alert.setTitle("Login failed");
            alert.showAndWait();
        }
    }

        public void btnPressedCreateAccount (ActionEvent event) throws IOException {
            Parent createAccountParent = FXMLLoader.load(getClass().getResource("/view/createAccountScreen.fxml"));
            Scene createAccountScene = new Scene(createAccountParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(createAccountScene);
            window.show();
        }

        public void exitProgram () {
            System.exit(0);
            System.out.println("Program closed");
        }
    }
