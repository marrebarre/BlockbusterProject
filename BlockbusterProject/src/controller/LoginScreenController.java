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

public class LoginScreenController {
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
        if (true){//Om sant, ändra scenen till huvudmeny (Admin eller User, beroende på inlogg)
        }
        username.clear();
        password.clear();
    }

    public void changeSceneToMainMenu (ActionEvent event){  //Byta sida!!!
        try {
            Parent mainMenuAdmin = FXMLLoader.load(getClass().getResource("/view/adminMenu.fxml"));
            Stage adminMainMenu = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene loginScreen = new Scene(mainMenuAdmin);
            adminMainMenu.setScene(loginScreen);
            adminMainMenu.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exitProgram(){
        System.exit(0);
        System.out.println("Program closed");
    }
}