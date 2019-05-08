package controller;

import data.DbConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Logic;
import java.io.IOException;

public class LoginScreenController {
    @FXML
    TextField username = new TextField();

    @FXML
    TextField password = new TextField();

    @FXML
    Label forgotPW = new Label();

    @FXML
    Label isConnected = new Label();

    @FXML
    Button signIn;

    @FXML
    Button btnCreateAccount;

    private DbConnector dbConnector = new DbConnector();
    private Logic logic = new Logic();

    public void adminLogin(ActionEvent event){
        try {
            Parent mainMenuAdmin = FXMLLoader.load(getClass().getResource("/view/adminMenu.fxml"));
            Stage adminMainMenu = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene loginScreen = new Scene(mainMenuAdmin);
            logic.setToFullscreen(adminMainMenu);
            adminMainMenu.setMaximized(true);
            adminMainMenu.setScene(loginScreen);
            adminMainMenu.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleLogin(ActionEvent event) {
        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            //System.out.println("Inget användarnamn eller lösen angivet.");
            isConnected.setText("Email and/or password not entered.");
            username.setStyle("-fx-background-color: #c12403; -fx-border-color: black; -fx-text-fill: black; -fx-prompt-text-fill: black");
            password.setStyle("-fx-background-color: #c12403; -fx-border-color: black; -fx-text-fill: black; -fx-prompt-text-fill: black");

        } else if (username.getText().equals(dbConnector.getEmail(username.getText())) && password.getText().equals(dbConnector.getPassword(password.getText()))) {
            username.setStyle("-fx-background-color: green; -fx-border-color: black; -fx-text-fill: black; -fx-prompt-text-fill: black");
            password.setStyle("-fx-background-color: green; -fx-border-color: black; -fx-text-fill: black; -fx-prompt-text-fill: black");

            if (dbConnector.isAdmin(true)) {
                try {
                    Parent mainMenuAdmin = FXMLLoader.load(getClass().getResource("/view/adminMenu.fxml"));
                    Stage adminMainMenu = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene loginScreen = new Scene(mainMenuAdmin);
                    logic.setToFullscreen(adminMainMenu);
                    adminMainMenu.setMaximized(true);
                    adminMainMenu.setScene(loginScreen);
                    adminMainMenu.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (dbConnector.isAdmin(false)) {
                try {
                    Parent mainMenuUser = FXMLLoader.load(getClass().getResource("/view/userMenu.fxml"));
                    Stage userMainMenu = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene loginScreen = new Scene(mainMenuUser);
                    logic.setToFullscreen(userMainMenu);
                    userMainMenu.setMaximized(true);
                    userMainMenu.setScene(loginScreen);
                    userMainMenu.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.NONE, "Invalid Email or password", ButtonType.OK);
            alert.setTitle("Login failed");
            alert.showAndWait();
        }

        /*}else if (username.getText().equals(dbConnector.userEmail(username.getText())) && password.getText().equals(dbConnector.userPassword(password.getText()))) {
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
            }*/
        //isConnected.setText("Login failed. Try again!");
    }

    public void btnPressedCreateAccount(ActionEvent event) throws IOException {
        Parent createAccountParent = FXMLLoader.load(getClass().getResource("/view/createAccountScreen.fxml"));
        Stage createAccountMenu = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene createAccountScene = new Scene(createAccountParent);
        logic.setToFullscreen(createAccountMenu);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(createAccountScene);
        window.show();
    }

    public void exitProgram() {
        System.exit(0);
        System.out.println("Program closed");
    }
}