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
    public TilePane tilePaneBrowse, tilePaneMyRentals;

    @FXML
    ScrollPane scrollPane, scrollPaneMyRentals;

    @FXML
    Label lblWelcomeMessage, currentBalance;

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

    public void loadMyRentals() {
        tilePaneMyRentals.getChildren().clear();
        dbConnector.loadRentals(tilePaneMyRentals);
    }

    public void handleSearchBtn(/*ActionEvent event*/) /*throws FileNotFoundException */ {
        tilePaneBrowse.getChildren().clear();
        searchByTitle(searchField.getText());
    }

    private void loadBrowse() {
        tilePaneBrowse.getChildren().clear();
        String SQLQuery = "SELECT * FROM movie";
        logic.loadBrowsePageData(SQLQuery, tilePaneBrowse);
    }

    public void settingsHandleUpdateBtn(){
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

    private void sortByGenre(String genre) {
        String query = "SELECT * FROM movie WHERE genre = '" + genre + "'";
        logic.loadBrowsePageData(query, tilePaneBrowse);
    }

    private void searchByTitle(String title) {
        String query = "SELECT * FROM movie WHERE title LIKE '%" + title + "%' ";
        logic.loadBrowsePageData(query, tilePaneBrowse);
    }

    private void treeview() {
        TreeItem rootItem = new TreeItem("FAQ's");
        TreeItem q1 = new TreeItem("ABOUT US");
        TreeItem q2 = new TreeItem("IS IT FREE?");
        TreeItem q3 = new TreeItem("WHAT DOES THIS SERVICE OFFER?");
        TreeItem q4 = new TreeItem("PAYMENTS?");
        TreeItem q5 = new TreeItem("HOW DO I GET THE MOVIE/S?");
        TreeItem q6 = new TreeItem("RETURN POLICIES?");
        TreeItem q7 = new TreeItem("FORGOT MY PASSWORD?");

        faq.setRoot(rootItem);
        rootItem.getChildren().addAll(q1, q2, q3, q4, q5, q6, q7);
        TreeItem a1 = new TreeItem("This is a movie rental program that allows the user to login, browse and rent movies from a store. " +
                "\nThe application is easy to use and displays which movies are currently in stock. When logged in, " +
                "\nthe user is able to rent movies, check stock and choose to have the movie delivered for a small " +
                "\nfee or to pick it up from a local store.");
        q1.getChildren().addAll(a1);
        TreeItem a2 = new TreeItem("Renting a movie comes with a fee, but being a member to this service is free of cost.");
        q2.getChildren().addAll(a2);
        TreeItem a3 = new TreeItem("This service offers users to rent physical copies of movies.");
        q3.getChildren().addAll(a3);
        TreeItem a4 = new TreeItem("Every user has a economical balance which payments will be drawn from.");
        q4.getChildren().addAll(a4);
        TreeItem a5 = new TreeItem("The movies can be aqquired by a user simply by picking it up at the designated store, " +
                "\nor a user can have the movie sent home for a small fee.");
        q5.getChildren().addAll(a5);
        TreeItem a6 = new TreeItem("The movies are being lent out during a specific period of time. If a user overdraws " +
                "\ntheir lending-period, additional fees will be drawn from this user.");
        q6.getChildren().addAll(a6);
        TreeItem a7 = new TreeItem("There is a link underneath the login-prompt. There you will be directed further");
        q6.getChildren().addAll(a7);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        scrollPane.setLayoutX(primaryScreenBounds.getMinX());
        scrollPane.setLayoutY(primaryScreenBounds.getMinY() + 50);
        scrollPane.setPrefWidth(primaryScreenBounds.getWidth());
        scrollPane.setPrefHeight(primaryScreenBounds.getHeight() - 115);
        loadBrowse();
        loadMyRentals();
        treeview();
        firstNameText.setText(loggedInUser.getFirstName());
        lastNameText.setText(loggedInUser.getLastName());
        emailText.setText(loggedInUser.getEmail());
        addressText.setText(loggedInUser.getAddress());
        phoneNumberText.setText(loggedInUser.getPhoneNr());
        lblWelcomeMessage.setText("Welcome, " + loggedInUser.getFirstName() + " " + loggedInUser.getLastName() + "!");
        currentBalance.setText("Balance: " + loggedInUser.getBalance() + "$");
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
}


