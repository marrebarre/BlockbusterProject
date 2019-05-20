package scene.forgotPassword;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import model.Logic;

public class ForgotPasswordController {
    @FXML
    Button btnResetPW, btnReturnToLogin;

    @FXML
    TextField enterEmailForReset;

    @FXML
    Label lblForgotPW;

    private Logic logic = new Logic();

    public void btnPressedReturnToLogin(MouseEvent event){
        String logInFXML = "/scene/loginScreen/loginScreenRedux.fxml";
        logic.changeSceneHandler(event, logInFXML, false);
    }
}