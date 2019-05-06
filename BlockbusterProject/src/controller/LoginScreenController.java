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
        }else if (username.getText().equals(dbConnector.userEmail(username.getText())) && password.getText().equals(dbConnector.userPassword(password.getText()))) {
            try {
                Parent mainMenuUser = FXMLLoader.load(getClass().getResource("/view/userMenu.fxml"));
                Stage userMainMenu = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene userMainMenuScene = new Scene(mainMenuUser);

                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getVisualBounds();
                userMainMenu.setWidth(bounds.getWidth());
                userMainMenu.setHeight(bounds.getHeight());

                userMainMenu.setMaximized(true);
                userMainMenu.setScene(userMainMenuScene);
                userMainMenu.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //isConnected.setText("Login failed. Try again!");

        } else {
            Alert alert = new Alert(Alert.AlertType.NONE, "Invalid Email or password", ButtonType.OK);
            alert.setTitle("Login failed");
            alert.showAndWait();

        }
    }



        public void btnPressedCreateAccount (ActionEvent event) throws IOException {
            Parent createAccountParent = FXMLLoader.load(getClass().getResource("/view/createAccountScreen.fxml"));
            Stage createAccountMenu = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene createAccountScene = new Scene(createAccountParent);

            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            createAccountMenu.setWidth(bounds.getWidth());
            createAccountMenu.setHeight(bounds.getHeight());

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(createAccountScene);
            window.show();
        }

        public void exitProgram () {
            System.exit(0);
            System.out.println("Program closed");
        }
    }