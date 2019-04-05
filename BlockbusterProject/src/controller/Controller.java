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
import model.Admin;

import java.io.IOException;

public class Controller {
    @FXML
    Button btnLogin = new Button();

    @FXML
    TextField username = new TextField();

    @FXML
    TextField password = new TextField();

    @FXML
    Label forgotPW = new Label();

    public void signIn() {
        Admin admin = new Admin(username.getText(), password.getText(), true);
        //if (){
        //changeScene();  //Om sant, ändra scenen till huvudmeny (Admin eller User, beroende på inlogg)
        //}
        username.clear();
        password.clear();
    }

    /*public void changeScene(ActionEvent event) {            //Byta sida!!!
        try {
            Parent parentPage = FXMLLoader.load(getClass().getResource("Page2.fxml"));
            Scene loginScene = new Scene(parentPage);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(loginScene);
            app_stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}