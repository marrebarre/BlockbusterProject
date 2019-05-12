package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;

public class ForgotPasswordController {
    @FXML
    Button btnResetPW, btnReturnToLogin;

    @FXML
    TextField enterEmailForReset;

    @FXML
    Label lblForgotPW;

    public void btnPressedReturnToLogin(MouseEvent event) throws IOException {
        Parent createAccountParent = FXMLLoader.load(getClass().getResource("/view/loginScreenRedux.fxml"));
        Scene createAccountScene = new Scene(createAccountParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setMaximized(false);
        window.setScene(createAccountScene);
        window.show();
    }
}