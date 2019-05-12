package controller;

import data.DbConnector;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Logic;
import java.io.IOException;

public class LoginScreenController {
    @FXML
    TextField username = new TextField(), password = new TextField();

    @FXML
    BorderPane borderPane = new BorderPane();

    @FXML
    Label forgotPW = new Label(), isConnected = new Label();

    @FXML
    Button signIn, btnCreateAccount;

    private DbConnector dbConnector = new DbConnector();
    private Logic logic = new Logic();

    public void handleLogin(ActionEvent event) {
        dbConnector.connect();
        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            isConnected.setText("Email and/or password not entered.");
            username.setStyle("-fx-background-color: #c12403; -fx-border-color: black; -fx-text-fill: black; -fx-prompt-text-fill: black");
            password.setStyle("-fx-background-color: #c12403; -fx-border-color: black; -fx-text-fill: black; -fx-prompt-text-fill: black");
        } else {
            System.out.println("Enter email/username: " + username.getText() + "\nEnter password: " + password.getText() + "\n<><><><><><><><><>");
            if (dbConnector.verifyAccount(username.getText(), password.getText())) {
                //System.out.println(dbConnector.verifyUser(username.getText(), password.getText()));
                if (dbConnector.verifyAccount(username.getText(), password.getText()) && !dbConnector.admins.isEmpty()){
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
                } else if (dbConnector.verifyAccount(username.getText(), password.getText()) && !dbConnector.users.isEmpty()) {
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
                } else {
                    System.out.println("Login failed");
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.NONE, "Invalid Email or password", ButtonType.OK);
                alert.setTitle("Login failed");
                alert.showAndWait();
            }
            dbConnector.disconnect();
        }
    }

    public void btnPressedCreateAccount(ActionEvent event) throws IOException {
        Parent createAccountParent = FXMLLoader.load(getClass().getResource("/view/createAccountScreen.fxml"));
        Stage createAccountMenu = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene createAccountScene = new Scene(createAccountParent);
        /*createAccountParent.translateYProperty().set(createAccountScene.getHeight());
        borderPane.getChildren().add(createAccountParent);
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(createAccountParent.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();*/
        logic.setToFullscreen(createAccountMenu);
        createAccountMenu.setMaximized(true);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(createAccountScene);
        window.show();
    }

    public void btnPressedForgotPW(MouseEvent mouseEvent) throws IOException {
        Parent forgotPWParent = FXMLLoader.load(getClass().getResource("/view/forgotPW.fxml"));
        Stage forgotPWStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene forgotPWScene = new Scene(forgotPWParent);
        logic.setToFullscreen(forgotPWStage);
        forgotPWStage.setMaximized(true);
        Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        window.setScene(forgotPWScene);
        window.show();
    }

    public void exitProgram() {
        System.exit(0);
        System.out.println("Program closed");
    }
}