package scene.userMenu;

import database.DbConnector;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.stage.Screen;
import model.Logic;
import model.User;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserMenuController implements Initializable {
    @FXML
    private TextField firstNameText, lastNameText, emailText, addressText, phoneNumberText;

    @FXML
    Button btnLogOut, updateBtn, btnSearch;

    @FXML
    private ComboBox<String> sortBox;

    @FXML
    private TextField searchField;

    @FXML
    TilePane tilePaneBrowse;

    @FXML
    ScrollPane scrollPane;

    @FXML
    Label lblWelcomeMessage;

   @FXML
   private TreeView faq;


    private DbConnector dbConnector = new DbConnector();
    private Logic logic = new Logic();
    public static User loggedInUser;

    public void btnPressedLogOut(MouseEvent event) {
        String logOutFXML = "/scene/loginScreen/loginScreenRedux.fxml";
        logic.changeSceneHandler(event, logOutFXML, false);
        if (!dbConnector.users.isEmpty()) {
            dbConnector.users.clear();
        } else {
            dbConnector.admins.clear();
        }
    }

    @FXML
    void handleSearchBtn(/*ActionEvent event*/) /*throws FileNotFoundException */{
        tilePaneBrowse.getChildren().clear();
        searchByTitle(searchField.getText());
    }

    public void loadBrowse() {
        tilePaneBrowse.getChildren().clear();
        String SQLQuery = "SELECT * FROM movie";
        logic.loadBrowsePageData(SQLQuery, tilePaneBrowse);
    }

    @FXML
    void handleSortBox(/*ActionEvent event*/) {
//?
    }

    @FXML
    void settingsHandleUpdateBtn() throws SQLException {
        if (!firstNameText.getText().equals("") && !firstNameText.getText().equals(loggedInUser.getFirstName())) {
            loggedInUser.setFirstName(firstNameText.getText());
            dbConnector.updateFirstName(loggedInUser.getIdUser(), loggedInUser);
        }
        if (!lastNameText.getText().equals("") && !lastNameText.getText().equals(loggedInUser.getLastName())) {
            loggedInUser.setLastName(lastNameText.getText());
            dbConnector.updateLastName(loggedInUser.getIdUser(), loggedInUser);
        }
        if (!emailText.getText().equals("") && !emailText.getText().equals(loggedInUser.getEmail())) {
            loggedInUser.setEmail(emailText.getText());
            dbConnector.updateEmail(loggedInUser.getIdUser(), loggedInUser);
        }
        if (!addressText.getText().equals("") && !addressText.getText().equals(loggedInUser.getAddress())) {
            loggedInUser.setAddress(addressText.getText());
            dbConnector.updateAddress(loggedInUser.getIdUser(), loggedInUser);
        }
        if (!phoneNumberText.getText().equals("") && !phoneNumberText.getText().equals(loggedInUser.getPhoneNr())) {
            loggedInUser.setPhoneNr(phoneNumberText.getText());
            dbConnector.updatePhoneNumber(loggedInUser.getIdUser(), loggedInUser);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        scrollPane.setLayoutX(primaryScreenBounds.getMinX());
        scrollPane.setLayoutY(primaryScreenBounds.getMinY() + 50);
        scrollPane.setPrefWidth(primaryScreenBounds.getWidth());
        scrollPane.setPrefHeight(primaryScreenBounds.getHeight() - 115);
        loadBrowse();
        firstNameText.setText(loggedInUser.getFirstName());
        lastNameText.setText(loggedInUser.getLastName());
        emailText.setText(loggedInUser.getEmail());
        addressText.setText(loggedInUser.getAddress());
        phoneNumberText.setText(loggedInUser.getPhoneNr());
        lblWelcomeMessage.setText("Welcome, " + loggedInUser.getFirstName() + " " + loggedInUser.getLastName() + "!");
        sortBox.getItems().add("Action");
        sortBox.getItems().add("Adventure");
        sortBox.getItems().add("Drama");
        sortBox.getItems().add("Family");
        sortBox.getItems().add("Show All Movies");
        sortBox.setOnAction(event -> {
            String choice = sortBox.getSelectionModel().getSelectedItem();
            switch (choice) {
                case "Action":
                    tilePaneBrowse.getChildren().clear();
                    sortByGenre("Action");
                    break;
                case "Adventure":
                    tilePaneBrowse.getChildren().clear();
                    sortByGenre("Adventure");
                    break;
                case "Drama":
                    tilePaneBrowse.getChildren().clear();
                    sortByGenre("Drama");
                    break;
                case "Family":
                    tilePaneBrowse.getChildren().clear();
                    sortByGenre("Family");
                    break;
                case "Show All Movies":
                    loadBrowse();
                    break;
            }
        });
    }

    private void sortByGenre(String genre) {
        String query = "SELECT * FROM movie WHERE genre = '" + genre + "'";
        logic.loadBrowsePageData(query, tilePaneBrowse);
    }

    private void searchByTitle(String title) {
        String query = "SELECT * FROM movie WHERE title LIKE '%" + title + "%' ";
        logic.loadBrowsePageData(query, tilePaneBrowse);
    }
    private void Treview(){
        TreeItem rootItem = new TreeItem("FAQ");
        TreeItem q1 = new TreeItem("FAQ");
        TreeItem q2 = new TreeItem("FAQ");
        TreeItem q3 = new TreeItem("FAQ");
        TreeItem q4 = new TreeItem("FAQ");

        faq.setRoot(rootItem);
        rootItem.getChildren().addAll(q1,q2,q3,q4);

        TreeItem a1 = new TreeItem("A1");
        q1.getChildren().addAll(a1);
        TreeItem a2 = new TreeItem("A2");
        q2.getChildren().addAll(a2);
        TreeItem a3 = new TreeItem("A3");
        q3.getChildren().addAll(a3);
        TreeItem a4 = new TreeItem("A4");
        q4.getChildren().addAll(a4);


    }
}