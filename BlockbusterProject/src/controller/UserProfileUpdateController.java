package controller;

import data.DbConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Logic;
import model.User;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserProfileUpdateController implements Initializable {

    @FXML
    private Label phonenumberLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label lastnameLabel;

    @FXML
    private Label balanceLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label firstnameLabel;

    @FXML
    private TextField firstnameText;

    @FXML
    private TextField lastnameText;

    @FXML
    private TextField emailText;

    @FXML
    private TextField balanceText;

    @FXML
    private TextField addressText;

    @FXML
    private TextField phonenumberText;

    @FXML
    private Button updateBtn;

    @FXML
    private Button backBtn;

    DbConnector db = new DbConnector();
  //  String username = LoginScreenController.getCurrentUser(); So later we can get the current users email and exequte sql queries.
    //                                                          It will display current users information. It can be used when displaying a users rental history

    private User user = new User("","",false,"","",0,"","",1);
    private Logic logic = new Logic();
    @FXML
    void handleBackBtn(ActionEvent event) {
        try {
            Parent mainMenuUser = FXMLLoader.load(getClass().getResource("/view/userMenu.fxml"));
            Stage userMainMenu = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene loginScreen = new Scene(mainMenuUser);
            logic.setToFullscreen(userMainMenu);
            userMainMenu.setMaximized(true);
            userMainMenu.setScene(loginScreen);
            userMainMenu.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleUpdateBtn(ActionEvent event) throws SQLException{
       if (!firstnameText.getText().equals("") && !firstnameText.getText().equals(db.getFirstName(1))){
           user.setFirstName(firstnameText.getText());
           db.updateFirstName(1,user);
       }
        if (!lastnameText.getText().equals("")&& !lastnameText.getText().equals(db.getLastname(1))){
            user.setLastName(lastnameText.getText());
            db.updateLastName(1,user);
        }
        if (!emailText.getText().equals("")&& !emailText.getText().equals(db.getEmail(1))){
            user.setEmail(emailText.getText());
            db.updateEmail(1,user);
        }
        if (!addressText.getText().equals("")&& !addressText.getText().equals(db.getAddress(1))){
            user.setAddress(addressText.getText());
            db.updateAddress(1,user);
        }
        if (!phonenumberText.getText().equals("")&& !phonenumberText.getText().equals(db.getPhoneNumber(1))){
            user.setPhoneNr(phonenumberText.getText());
            db.updatePhoneNumber(1,user);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //here when you open up user-profile-update, the users initial data will be displayed
        firstnameText.setText(db.getFirstName(1));
        lastnameText.setText(db.getLastname(1));
        emailText.setText(db.getEmail(1));
        addressText.setText(db.getAddress(1));
        phonenumberText.setText(db.getPhoneNumber(1));
    }
}