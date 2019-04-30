package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateAccountController {
    @FXML
    Button registerBtn;

    @FXML
     Button backToLoginbtn;

    @FXML
     TextField emailtxtField;

    @FXML
     TextField firstNametxtField;

    @FXML
    TextField lastNametxtField;

    @FXML
     Label successLabel;
    @FXML
    TextField passwordtxtField;






    public void backtoLogin(ActionEvent event) throws IOException {
        Parent createAccountParent = FXMLLoader.load(getClass().getResource("/view/loginScreen.fxml"));
        Scene createAccountScene = new Scene(createAccountParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(createAccountScene);
        window.show();
    }

}
