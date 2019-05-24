package scene.loginScreen;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import scene.userMenu.UserMenuController;
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
import java.security.Key;
import java.util.ResourceBundle;

import static scene.userMenu.UserMenuController.loggedInUser;

public class LoginScreenController implements Initializable {
    @FXML
    TextField username = new TextField();
    @FXML
    PasswordField password = new PasswordField();
    @FXML
    Label forgotPW = new Label(), isConnected = new Label();
    @FXML
    Button signIn, btnCreateAccount;
    @FXML
    ImageView /*imageSwap,*/ logo,avengersSwap,kingarthurSwap,hpSwap,warcraftSwap,lotrSwap,wpSwap,inceptionSwap,venturaSwap;

    private DbConnector dbConnector = new DbConnector();
    private Logic logic = new Logic();

    public void handleLogin(ActionEvent event) {
        dbConnector.connect();
        try {
            if (username.getText().isEmpty() || password.getText().isEmpty()) {
                isConnected.setText("Email and/or password not entered.");
                //System.out.println("Email and/or password not entered.");
                username.setStyle("-fx-prompt-text-fill: red");
                password.setStyle("-fx-prompt-text-fill: red");
            } else if (!username.getText().isEmpty() && !password.getText().isEmpty()) {
                if (dbConnector.verifyAccount(username.getText(), password.getText()) && !dbConnector.admins.isEmpty()) {
                    String adminMenuFXML = "/scene/adminMenu/adminMenu.fxml";
                    logic.changeSceneHandler(event, adminMenuFXML, true);
                } else if (dbConnector.verifyAccount(username.getText(), password.getText()) && !loggedInUser.isAdmin()) {
                    String userMenuFXML = "/scene/userMenu/userMenu.fxml";
                    logic.changeSceneHandler(event, userMenuFXML, true);
                }
            }
            dbConnector.disconnect();
        } catch (NullPointerException e) {
            System.out.println("Login failed");
            Alert alert = new Alert(Alert.AlertType.NONE, "Invalid email or password", ButtonType.OK);
            alert.setTitle("Login failed");
            alert.showAndWait();
            //e.printStackTrace();
            System.out.println("Match to entered data not found within DB. Error thrown: " + e.toString());
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
        Image image = new Image("image/BlockbusterLogo.png");
        logo.setImage(image);



        KeyFrame kf16 = new KeyFrame(Duration.seconds(2), new KeyValue(venturaSwap.opacityProperty(),1));
        KeyFrame kf12 = new KeyFrame(Duration.seconds(3),new KeyValue(venturaSwap.opacityProperty(),0));
        KeyFrame kf13 = new KeyFrame(Duration.seconds(5),new KeyValue(inceptionSwap.opacityProperty(),1));
        KeyFrame kf10 = new KeyFrame(Duration.seconds(7),new KeyValue(inceptionSwap.opacityProperty(),0));

        KeyFrame kf11 = new KeyFrame(Duration.seconds(9),new KeyValue(wpSwap.opacityProperty(),1));
        KeyFrame kf8 = new KeyFrame(Duration.seconds(11),new KeyValue(wpSwap.opacityProperty(),0));

        KeyFrame kf9 = new KeyFrame(Duration.seconds(13),new KeyValue(lotrSwap.opacityProperty(),1));
        KeyFrame kf6 = new KeyFrame(Duration.seconds(15), new KeyValue(lotrSwap.opacityProperty(),0));

        KeyFrame kf5 = new KeyFrame(Duration.seconds(17), new KeyValue(warcraftSwap.opacityProperty(),1));
        KeyFrame kf7 = new KeyFrame(Duration.seconds(19),new KeyValue(warcraftSwap.opacityProperty(),0));

        KeyFrame kf2 = new KeyFrame(Duration.seconds(21),new KeyValue(hpSwap.opacityProperty(),1));
        KeyFrame kf1 = new KeyFrame(Duration.seconds(23),new KeyValue(hpSwap.opacityProperty(),0));

        KeyFrame kf3 = new KeyFrame(Duration.seconds(25),new KeyValue(kingarthurSwap.opacityProperty(),1));
        KeyFrame kf4 = new KeyFrame(Duration.seconds(27),new KeyValue(kingarthurSwap.opacityProperty(),0));
        KeyFrame kf15 = new KeyFrame(Duration.seconds(29),new KeyValue(avengersSwap.opacityProperty(),1));

        Timeline timelineOn = new Timeline(kf12,kf13,kf10,kf11,kf8,kf9, kf6, kf5, kf7, kf2,kf1, kf3 ,kf4,kf16,kf15);
        timelineOn.setCycleCount(Timeline.INDEFINITE);
        timelineOn.setAutoReverse(true);
        timelineOn.play();
    }
}