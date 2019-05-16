package controller;

import data.DbConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.Logic;

public class LoginScreenController {
    @FXML
    TextField username = new TextField(), password = new TextField();

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
                if (dbConnector.verifyAccount(username.getText(), password.getText()) && !dbConnector.admins.isEmpty()){
                    String adminMenuFXML = "/view/adminMenu.fxml";
                    logic.changeSceneHandler(event, adminMenuFXML, true);
                } else if (dbConnector.verifyAccount(username.getText(), password.getText()) && !dbConnector.users.isEmpty()) {
                    String userMenuFXML = "/view/userMenu.fxml";
                    logic.changeSceneHandler(event, userMenuFXML, true);
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

    public void btnPressedCreateAccount(ActionEvent event){
        String createAccountFXML = "/view/createAccountScreen.fxml";
        logic.changeSceneHandler(event, createAccountFXML, true);
        /*createAccountParent.translateYProperty().set(createAccountScene.getHeight());
        borderPane.getChildren().add(createAccountParent);
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(createAccountParent.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();*/
    }

    public void btnPressedForgotPW (MouseEvent event){
        String forgotPasswordFXML = "/view/forgotPW.fxml";
        logic.changeSceneHandler(event, forgotPasswordFXML, false);
    }

    public void exitProgram() {
        System.exit(0);
        System.out.println("Program closed");
    }
}