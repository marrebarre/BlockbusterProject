package controller;

import data.DbConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Account;
import model.User;

import java.io.IOException;
import java.util.IllegalFormatException;

public class CreateAccountController {
    @FXML
    Button registerBtn;

    @FXML
    Button backToLoginbtn;

    @FXML
    TextField emailtxtField, firstNametxtField, lastNametxtField, passwordtxtField, addresstxtField, phonetxtField;


    @FXML
    Label successLabel;

    DbConnector dbConnector = new DbConnector();


    public void backtoLogin(ActionEvent event) throws IOException {
        Parent createAccountParent = FXMLLoader.load(getClass().getResource("/view/loginScreenRedux.fxml"));
        Scene createAccountScene = new Scene(createAccountParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(createAccountScene);
        window.show();
    }

    public void registerPressed() {

        try {
            User user = new User(emailtxtField.getText(), passwordtxtField.getText(),false, firstNametxtField.getText(),
                    lastNametxtField.getText(), 0, addresstxtField.getText(), phonetxtField.getText());
            dbConnector.connect();
            dbConnector.addUserToDb(user);
            dbConnector.disconnect();


        } catch (IllegalFormatException e) {
            System.out.println("Error");
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
        layout.getChildren().addAll(label,closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

    }
}


