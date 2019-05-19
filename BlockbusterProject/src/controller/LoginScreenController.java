package controller;

import data.DbConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.Logic;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable {
    @FXML
    TextField username = new TextField(), password = new TextField();
    @FXML
    Label forgotPW = new Label(), isConnected = new Label();
    @FXML
    Button signIn, btnCreateAccount;
    @FXML
    ImageView imageSwap, logo;

    private DbConnector dbConnector = new DbConnector();
    private Logic logic = new Logic();

    public void handleLogin(ActionEvent event) {
        dbConnector.connect();
        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            isConnected.setText("Email and/or password not entered.");
            username.setStyle("-fx-background-color: #c12403; -fx-border-color: black; -fx-text-fill: black; -fx-prompt-text-fill: black");
            password.setStyle("-fx-background-color: #c12403; -fx-border-color: black; -fx-text-fill: black; -fx-prompt-text-fill: black");
        } else {
            if (dbConnector.verifyAccount(username.getText(), password.getText())) {
                if (!dbConnector.admins.isEmpty()) {
                    String adminMenuFXML = "/view/adminMenu.fxml";
                    logic.changeSceneHandler(event, adminMenuFXML, true);
                } else if (dbConnector.verifyAccount(username.getText(), password.getText()) && !UserMenuController.loggedInUser.isAdmin()) {
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

    public void btnPressedCreateAccount(ActionEvent event) {
        String createAccountFXML = "/view/createAccountScreen.fxml";
        logic.changeSceneHandler(event, createAccountFXML, true);
    }

    public void btnPressedForgotPW(MouseEvent event) {
        String forgotPasswordFXML = "/view/forgotPW.fxml";
        logic.changeSceneHandler(event, forgotPasswordFXML, false);
    }

    public void exitProgram() {
        System.exit(0);
        System.out.println("Program closed");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image("Images/BlockbusterLogo.png");
        logo.setImage(image);
        /*Path path = new Path();
        path.getElements().add(new MoveTo(0,0));
        path.getElements().add(new LineTo(500,280));
        path.getElements().add(new LineTo(630,50));
        path.getElements().add(new LineTo(550,-10));
        path.getElements().add((new LineTo(50,280)));
        path.getElements().add(new LineTo(0,0));
        PathTransition transition = new PathTransition();
        transition.setNode(aStarIsBorn);
        transition.setDuration(Duration.seconds(10));
        transition.setPath(path);
        transition.setAutoReverse(true);
        transition.setCycleCount(PathTransition.INDEFINITE);
        transition.play();*/
    }
}