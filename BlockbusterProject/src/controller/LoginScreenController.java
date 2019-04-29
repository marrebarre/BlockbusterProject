package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.Admin;
import model.Logic;

import java.io.IOException;
import java.sql.*;

public class LoginScreenController {
    @FXML
    public TextField username = new TextField();

    @FXML
    public TextField password = new TextField();

    @FXML
    Label forgotPW = new Label();

    @FXML
    Label isConnected = new Label();

    @FXML
    Button signIn;

    @FXML
    Button createAccountbtn;

    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private Connection connection;
    private data.DbConnector dbConnector = new data.DbConnector();

    public void handleLogin(ActionEvent event) {
        dbConnector.connect();


        if (username.getText().equals(getEmail()) && password.getText().equals(getPassword())) {
            try {
                Parent mainMenuAdmin = FXMLLoader.load(getClass().getResource("/view/adminMenu.fxml"));
                Stage adminMainMenu = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene loginScreen = new Scene(mainMenuAdmin);

                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getVisualBounds();
                adminMainMenu.setWidth(bounds.getWidth());
                adminMainMenu.setHeight(bounds.getHeight());

                adminMainMenu.setMaximized(true);
                adminMainMenu.setScene(loginScreen);
                adminMainMenu.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.NONE, "Invalid Email or Password entered", ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
        }
        dbConnector.disconnect();
    }

    public String getEmail() {
        String email = "";

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE email = ?");
            preparedStatement.setString(1, username.getText());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                email = resultSet.getString(1);
                isConnected.setText("Connected!");
                resultSet.close();
            }
        } catch (SQLException | NullPointerException ex) {
            System.out.println(ex);
        }
        return email;
    }

    public String getPassword() {
        String pw = "";

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE password = ?");
            preparedStatement.setString(1, password.getText());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                pw = resultSet.getString(2);
                resultSet.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return pw;
    }


/*    public void signIn(ActionEvent event) {
        dbConnector.connect();
        LoginScreenController myApp = new LoginScreenController();
        data.DbConnector dbConnector = new data.DbConnector();

        PreparedStatement loginPreparedStatement;
        ResultSet loginResultSet;
        String loginQuery = "SELECT * FROM user WHERE user.email =? AND user.password =?";

        String email = myApp.username.getText();
        String password = myApp.password.getText();

        try {
            loginPreparedStatement = dbConnector.getConnection().prepareStatement(loginQuery);
            loginPreparedStatement.setString(1, email);
            loginPreparedStatement.setString(2, password);
            loginResultSet = loginPreparedStatement.executeQuery();

            if (loginResultSet.next()) {
                System.out.println("YES");
                Parent createAccountParent = FXMLLoader.load(getClass().getResource("/view/createAccountScreen.fxml"));
                Scene createAccountScene = new Scene(createAccountParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(createAccountScene);
                window.show();
            } else {
                System.out.println("NO");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } catch (NullPointerException npe) {
            System.out.println(npe + " Not referring properly");
        }
        dbConnector.disconnect();
    }*/


        public void changeSceneToMainMenu (ActionEvent event){  //Byta sida!!!
        }

        public void createAccountbuttonPressed (ActionEvent event) throws IOException {
            Parent createAccountParent = FXMLLoader.load(getClass().getResource("/view/createAccountScreen.fxml"));
            Scene createAccountScene = new Scene(createAccountParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(createAccountScene);
            window.show();
        }

        public void exitProgram () {
            System.exit(0);
            System.out.println("Program closed");
        }
    }
