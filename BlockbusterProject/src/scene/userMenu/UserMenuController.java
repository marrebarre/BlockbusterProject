package scene.userMenu;

import database.DbConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.stage.Screen;
import model.Logic;
import model.User;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.ResourceBundle;

public class UserMenuController implements Initializable {
    @FXML
    private TextField firstNameText, lastNameText, emailText, addressText, phoneNumberText, searchField, passwordText;

    @FXML
    Button btnLogOut, updateBtn, btnSearch, sendReceiptBtn;

    @FXML
    private ComboBox<String> sortBox;

    @FXML
    public TilePane tilePaneBrowse, tilePaneMyRentals;

    @FXML
    ScrollPane scrollPane, scrollPaneMyRentals;

    @FXML
    public Label lblWelcomeMessage, currentBalance;

    @FXML
    private TreeView<String> faq;

    private DbConnector dbConnector = new DbConnector();
    private Logic logic = new Logic();
    public static User loggedInUser;
    public static DecimalFormat df = new DecimalFormat("0.00");

    private static String ourEmail = "thebustblocker1@gmail.com";  // Mail-name
    private static String ourEmailsPassword = "Buster!321"; // Mail password (Maybe make one just for this project team
    private static String emailTitle = "Bust Blocker"; // Add the title of the e-mail here.

    public void btnPressedLogOut(ActionEvent event) {
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
        logic.loadRentals(tilePaneMyRentals);
    }

    public void handleSearchBtn() {
        tilePaneBrowse.getChildren().clear();
        searchByTitle(searchField.getText());
    }

    public void loadBrowse() {
        tilePaneBrowse.getChildren().clear();
        String SQLQuery = "SELECT * FROM movie";
        logic.loadBrowsePageData(SQLQuery, tilePaneBrowse);
    }

    @FXML //krille
    private void handleSendReceipt() {
        logic.pdf();
        String recipent = loggedInUser.getEmail(); // instead loggedInUser.getEmail();
        String mess = "Here is your receipt";

        String recipientEmailString = recipent;
        String messageToBeSent = mess;

        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", ourEmail);
        props.put("mail.smtp.password", ourEmailsPassword);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        System.out.println("after session");

        Message message = new MimeMessage(session);
        System.out.println("after mime");
        String[] recipientEmail = new String[]{
                recipientEmailString}; //Change the String into a String[]
        try {
            message.setFrom(new InternetAddress(ourEmail));

            InternetAddress[] toAddress = new InternetAddress[recipientEmail.length];
            // To get the array of addresses
            for (int i = 0; i < recipientEmail.length; i++) {
                toAddress[i] = new InternetAddress(recipientEmail[i]);
            }
            for (int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }
            BodyPart messageBodyPart = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();
            message.setSubject(emailTitle);
            messageBodyPart.setText(messageToBeSent);
            messageBodyPart = new MimeBodyPart();
            String filename = "pdf/Receipt.pdf";
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, ourEmail, ourEmailsPassword);
            transport.sendMessage(message, message.getAllRecipients());
            System.out.println("email sent");
            transport.close();
        } catch (AddressException ae) {
            System.out.println("address Exception");
            ae.getMessage();
            ae.printStackTrace();
        } catch (MessagingException me) {
            System.out.println("Message Exception");
            me.getMessage();
            me.printStackTrace();
        }
    }

    public void settingsHandleUpdateBtn() {
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
        if (!passwordText.getText().equals("") && !passwordText.getText().equals(loggedInUser.getPassword())) {
            loggedInUser.setPassword(passwordText.getText());
            dbConnector.updatePassword(loggedInUser.getEmail(), loggedInUser);
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
        TreeItem<String> rootItem = new TreeItem<>("FAQ");
        TreeItem<String> q1 = new TreeItem<>("ABOUT US");
        TreeItem<String> q2 = new TreeItem<>("IS IT FREE?");
        TreeItem<String> q3 = new TreeItem<>("WHAT DOES THIS SERVICE OFFER?");
        TreeItem<String> q4 = new TreeItem<>("PAYMENTS?");
        TreeItem<String> q5 = new TreeItem<>("HOW DO I GET THE MOVIE/S?");
        TreeItem<String> q6 = new TreeItem<>("RETURN POLICIES?");
        TreeItem<String> q7 = new TreeItem<>("FORGOT MY PASSWORD?");

        faq.setRoot(rootItem);
        rootItem.getChildren().addAll(q1, q2, q3, q4, q5, q6, q7);
        TreeItem<String> a1 = new TreeItem<>("This is a movie rental program that allows the user to login, browse and rent movies from a store. " +
                "\nThe application is easy to use and displays which movies are currently in stock. When logged in, " +
                "\nthe user is able to rent movies, check stock and choose to have the movie delivered for a small " +
                "\nfee or to pick it up from a local store.");
        q1.getChildren().addAll(a1);
        TreeItem<String> a2 = new TreeItem<>("Renting a movie comes with a fee, but being a member to this service is free of cost.");
        q2.getChildren().addAll(a2);
        TreeItem<String> a3 = new TreeItem<>("This service offers users to rent physical copies of movies.");
        q3.getChildren().addAll(a3);
        TreeItem<String> a4 = new TreeItem<>("Every user has a economical balance which payments will be drawn from.");
        q4.getChildren().addAll(a4);
        TreeItem<String> a5 = new TreeItem<>("The movies can be aqquired by a user simply by picking it up at the designated store, " +
                "\nor a user can have the movie sent home for a small fee.");
        q5.getChildren().addAll(a5);
        TreeItem<String> a6 = new TreeItem<>("The movies are being lent out during a specific period of time. If a user overdraws " +
                "\ntheir lending-period, additional fees will be drawn from this user.");
        q6.getChildren().addAll(a6);
        TreeItem<String> a7 = new TreeItem<>("There is a link underneath the login-prompt. There you will be directed further");
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
        passwordText.setText(loggedInUser.getPassword());
        lblWelcomeMessage.setText("Welcome, " + loggedInUser.getFirstName() + " " + loggedInUser.getLastName() + "!");
        currentBalance.setText("Balance: " + df.format(loggedInUser.getBalance()) + "$");
        sortBox.getItems().add("Horror");
        sortBox.getItems().add("Scifi");
        sortBox.getItems().add("Action");
        sortBox.getItems().add("Adventure");
        sortBox.getItems().add("Drama");
        sortBox.getItems().add("Family");
        sortBox.getItems().add("Show All Movies");
        sortBox.setOnAction(event -> {
            String choice = sortBox.getSelectionModel().getSelectedItem();
            switch (choice) {
                case "Horror":
                    tilePaneBrowse.getChildren().clear();
                    sortByGenre("Horror");
                    break;
                case "Scifi":
                    tilePaneBrowse.getChildren().clear();
                    sortByGenre("Scifi");
                    break;
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