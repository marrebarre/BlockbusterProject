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
import model.Logic;

import java.io.IOException;

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
    Logic logic;

    public void handleLogin(ActionEvent event) {
        if (username.getText().equals(dbConnector.getEmail(username.getText())) && password.getText().equals(dbConnector.getPassword(password.getText()))) {
            if (dbConnector.isAdmin(true)){
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
            } else if (dbConnector.isAdmin(false)){
                try {
                    Parent mainMenuUser = FXMLLoader.load(getClass().getResource("/view/userMenu.fxml"));
                    Stage userMainMenu = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene loginScreen = new Scene(mainMenuUser);

                    Screen screen = Screen.getPrimary();
                    Rectangle2D bounds = screen.getVisualBounds();
                    userMainMenu.setWidth(bounds.getWidth());
                    userMainMenu.setHeight(bounds.getHeight());

                    userMainMenu.setMaximized(true);
                    userMainMenu.setScene(loginScreen);
                    userMainMenu.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            //isConnected.setText("Login failed. Try again!");
            Alert alert = new Alert(Alert.AlertType.NONE, "Invalid Email or Password entered", ButtonType.OK);
            alert.setTitle("Login failed");
            alert.showAndWait();
        }
    }

        public void btnPressedCreateAccount (ActionEvent event) throws IOException {
            Parent createAccountParent = FXMLLoader.load(getClass().getResource("/view/createAccountScreen.fxml"));
            Stage createAccountMenu = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene createAccountScene = new Scene(createAccountParent);
            logic.setToFullscreen(createAccountMenu);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(createAccountScene);
            window.show();
        }

        public void exitProgram () {
            System.exit(0);
            System.out.println("Program closed");
        }
    }