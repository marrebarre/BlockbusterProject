package controller;

import data.DbConnector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Logic;

import java.io.IOException;

public class UserMenuController {

    @FXML
    ImageView aStarIsBorn, inception;

    @FXML
    Button btnLogOut;

    private DbConnector dbConnector = new DbConnector();
    private Logic logic = new Logic();

    public void hoverOverPictureEntered(){
        aStarIsBorn.setFitHeight(250);
        aStarIsBorn.setFitWidth(250);

    }
    public void hoverOverPictureExit(){
        aStarIsBorn.setFitWidth(200);
        aStarIsBorn.setFitHeight(200);

    }
    public void mouseOverPicture(){
        inception.setFitHeight(250);
        inception.setFitWidth(250);
    }
    public void mouseOverPictureExit(){
        inception.setFitWidth(200);
        inception.setFitHeight(200);
    }

    public void btnPressedLogOut(MouseEvent event) throws IOException {
        Parent createAccountParent = FXMLLoader.load(getClass().getResource("/view/loginScreenRedux.fxml"));
        Scene createAccountScene = new Scene(createAccountParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setMaximized(false);
        window.setScene(createAccountScene);
        window.show();
        if (!dbConnector.users.isEmpty()){
            dbConnector.users.clear();
        } else {
            dbConnector.admins.clear();
        }
    }
}