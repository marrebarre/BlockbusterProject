package scene.loginScreen;

import database.DbConnector;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
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
    Label forgotPW = new Label(), isConnected = new Label();
    @FXML
    Button signIn, btnCreateAccount, changeImage;
    @FXML
    ImageView imageSwap, logo/*,avengersSwap,kingarthurSwap,hpSwap,warcraftSwap,lotrSwap,wpSwap,inceptionSwap,venturaSwap*/;

    private DbConnector dbConnector = new DbConnector();
    private Logic logic = new Logic();
    private List<Image> imageList = new ArrayList<>();

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
/*
    public void loadImages() {
        for (int i = 0; i < 10; i++) {
            imageList.add(new Image(getClass().getResource(i + ".png").toExternalForm()));
        }
    }*/

/*
    private void init(Image[] images) {
        this.imageSwap = new ImageView(images[3]);
        Timeline timeLine = new Timeline();
        Collection<KeyFrame> frames = timeLine.getKeyFrames();
        Duration frameGap = Duration.millis(256);
        Duration frameTime = Duration.ZERO;
        for (Image img : images) {
            frameTime = frameTime.add(frameGap);
            frames.add(new KeyFrame(frameTime, e -> imageSwap.setImage(img)));
        }
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }
*/


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image("image/BlockbusterLogo.png");
        logo.setImage(image);
        /*  loadImages();*/
        /*ImageView view1 = new ImageView(imageList.get(1));
        ImageView view2 = new ImageView(imageList.get(2));
        ImageView view3 = new ImageView(imageList.get(3));*/
        Image img1 = new Image("image/AvengersEndgame.jpg");
        imageList.add(img1);
        Image img2 = new Image("image/FightClub.jpg");
        imageList.add(img2);
        Image img3 = new Image("image/It.jpg");
        imageList.add(img3);

        /*changeImage.setOnAction((event) -> */{
            Collections.shuffle(imageList);
            imageSwap.setImage(imageList.get(0));
            imageSwap.setImage(imageList.get(1));
            imageSwap.setImage(imageList.get(2));

        }

        Collections.shuffle(imageList);
        imageSwap.setImage(imageList.get(0));
        for (Image value : imageList) {
            KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(7), new KeyValue(imageSwap.opacityProperty(), 1));
            KeyFrame keyframe2 = new KeyFrame(Duration.seconds(3), new KeyValue(imageSwap.opacityProperty(), 0));
            imageSwap.setImage(value);
            Timeline timeline = new Timeline(keyFrame1, keyframe2);
            //timeline.setAutoReverse(true);
            timeline.play();
        }

        /*imageSwap.setImage(imageList.get(2));*/
/*

        KeyFrame kf9 = new KeyFrame(Duration.seconds(13),new KeyValue(imageSwap.opacityProperty(),1));
        KeyFrame kf6 = new KeyFrame(Duration.seconds(15), new KeyValue(imageSwap.opacityProperty(),0));

        KeyFrame kf5 = new KeyFrame(Duration.seconds(17), new KeyValue(imageSwap.opacityProperty(),1));
        KeyFrame kf7 = new KeyFrame(Duration.seconds(19),new KeyValue(imageSwap.opacityProperty(),0));

        KeyFrame kf2 = new KeyFrame(Duration.seconds(21),new KeyValue(imageSwap.opacityProperty(),1));
        KeyFrame kf1 = new KeyFrame(Duration.seconds(23),new KeyValue(imageSwap.opacityProperty(),0));

        KeyFrame kf3 = new KeyFrame(Duration.seconds(25),new KeyValue(imageSwap.opacityProperty(),1));
        KeyFrame kf4 = new KeyFrame(Duration.seconds(27),new KeyValue(imageSwap.opacityProperty(),0));
        KeyFrame kf15 = new KeyFrame(Duration.seconds(29),new KeyValue(imageSwap.opacityProperty(),1));*/
/*
        Timeline timelineOn = new Timeline(*//*kf1,kf9, kf6, kf5, kf7, kf2,kf1, kf3 ,kf4,kf2,kf15*//*);
        timelineOn.play();*/
    }
}