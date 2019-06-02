package scene.createAccount;

import database.DbConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Logic;
import model.User;
import java.util.IllegalFormatException;

public class CreateAccountController {
    @FXML
    Button registerBtn;

    @FXML
    Button backToLoginbtn;

    @FXML
    TextField emailtxtField, firstNametxtField, lastNametxtField, passwordtxtField, addresstxtField, phonetxtField;

    @FXML
    Label successLabel, firstNameError, lastNameError, emailError, passwordError, addressError, phoneError, unfilledError;

    private DbConnector dbConnector = new DbConnector();
    private Logic logic = new Logic();

    public void backToLogin(ActionEvent event) {
        String loginFXML = "/scene/loginScreen/loginScreenRedux.fxml";
        logic.changeSceneHandler(event, loginFXML, false);
    }

    public void registerPressed(ActionEvent event){
        if (!firstNametxtField.getText().isEmpty() || !lastNametxtField.getText().isEmpty() || !emailtxtField.getText().isEmpty() ||
                !passwordtxtField.getText().isEmpty() || !addresstxtField.getText().isEmpty() || !phonetxtField.getText().isEmpty()) {
            firstNameError.setVisible(false);
            lastNameError.setVisible(false);
            emailError.setVisible(false);
            passwordError.setVisible(false);
            addressError.setVisible(false);
            phoneError.setVisible(false);
        }
        if (firstNametxtField.getText().isEmpty()) {
            unfilledError.setText("All fields with * must be filled");
            firstNameError.setVisible(true);
        }
        if (lastNametxtField.getText().isEmpty()) {
            unfilledError.setText("All fields with * must be filled");
            lastNameError.setVisible(true);
        }
        if (emailtxtField.getText().isEmpty()) {
            unfilledError.setText("All fields with * must be filled");
            emailError.setVisible(true);
        }
        if (passwordtxtField.getText().isEmpty()) {
            unfilledError.setText("All fields with * must be filled");
            passwordError.setVisible(true);
        }
        if (addresstxtField.getText().isEmpty()) {
            unfilledError.setText("All fields with * must be filled");
            addressError.setVisible(true);
        }
        if (phonetxtField.getText().isEmpty()) {
            unfilledError.setText("All fields with * must be filled");
            phoneError.setVisible(true);
        }
        if(!emailtxtField.getText().matches("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Email");
            alert.setContentText("Please enter vaild Email");
            alert.showAndWait();
            emailError.setVisible(true);
        } else if (!firstNametxtField.getText().isEmpty() && !lastNametxtField.getText().isEmpty() && !emailtxtField.getText().isEmpty() &&
                !passwordtxtField.getText().isEmpty() && !addresstxtField.getText().isEmpty() && !phonetxtField.getText().isEmpty() && emailtxtField.getText().matches("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+")) {
            dbConnector.connect();
            try {
                User user = new User(emailtxtField.getText(), passwordtxtField.getText(), false, firstNametxtField.getText(),
                        lastNametxtField.getText(), 0, addresstxtField.getText(), phonetxtField.getText(), dbConnector.tableSizeAccount() + 1);
                dbConnector.addUserToDb(user);


            } catch (IllegalFormatException e) {
                System.out.println("Error");
            } finally {
                dbConnector.disconnect();
            }
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Registration");
            window.setMinWidth(250);
            Label label = new Label();
            label.setText("Registration completed successfully");
            Button closeButton = new Button("Ok");
            closeButton.setOnAction(e -> window.close());

            VBox layout = new VBox(10);
            layout.getChildren().addAll(label, closeButton);
            layout.setAlignment(Pos.CENTER);

            Scene scene = new Scene(layout);
            window.setScene(scene);
            window.showAndWait();
            String logInFXML = "/scene/loginScreen/loginScreenRedux.fxml";
            logic.changeSceneHandler(event, logInFXML, false);
        }
    }
}