package controller;

import data.DbConnector;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.Logic;

public class UserMenuController {

    @FXML
    ImageView aStarIsBorn, inception;

    @FXML
    Button btnLogOut;

    private DbConnector dbConnector = new DbConnector();
    private Logic logic = new Logic();

    public void btnPressedLogOut(MouseEvent event) {
        String logOutFXML = "/view/loginScreenRedux.fxml";
        logic.changeSceneHandler(event, logOutFXML, false);
        if (!dbConnector.users.isEmpty()){
            dbConnector.users.clear();
        } else {
            dbConnector.admins.clear();
        }
    }
}