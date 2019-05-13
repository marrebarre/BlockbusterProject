package controller;

import data.DbConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.User;

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

    User user;

    @FXML
    void handleBackBtn(ActionEvent event) {

    }

    @FXML
    void handleUpdateBtn(ActionEvent event) throws SQLException{
       if (!firstnameText.getText().equals("")){

           //user.setFirstName(firstnameText.getText()); to be able to update, first setter is needed.
           db.updateFirstName(1,user);
       }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //here when you open up user-profile-update, the users initial data will be displayed
        firstnameText.setText(db.getFirstName(1));
        lastnameText.setText(db.getFirstName(1));
    }
}
