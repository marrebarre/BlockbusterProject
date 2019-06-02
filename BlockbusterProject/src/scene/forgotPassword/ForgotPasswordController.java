package scene.forgotPassword;

import database.DbConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.Logic;
import model.User;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class ForgotPasswordController {
    @FXML
    Button btnResetPW, btnReturnToLogin;

    @FXML
    TextField enterEmailForReset;

    @FXML
    Label lblForgotPW;

    private DbConnector dbConnector = new DbConnector();
    private Logic logic = new Logic();
    private static String ourEmail = "thebustblocker1@gmail.com";  // Mail-name
    private static String ourEmailsPassword = "Buster!321"; // Mail password (Maybe make one just for this project team
    private static String emailTitle = "Bust Blocker"; // Add the title of the e-mail here.
    private User user = new User("", "", false, "", "", 2, "", "", 1);

    public void btnPressedReturnToLogin(MouseEvent event) {
        String logInFXML = "/scene/loginScreen/loginScreenRedux.fxml";
        logic.changeSceneHandler(event, logInFXML, false);
    }

    public void getEmailForPW() {
        final String emailForPw = enterEmailForReset.getText();
        String mess = Logic.generatePassword(); // message to be sent
        user.setPassword(mess);
        dbConnector.updatePassword(emailForPw, user);

        String recipientEmailString = emailForPw;
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

            message.setSubject(emailTitle);
            message.setText(messageToBeSent);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, ourEmail, ourEmailsPassword);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (AddressException ae) {
            System.out.println("address Exception");
            ae.getMessage();
            //ae.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.NONE, "Invalid email entered", ButtonType.OK);
            alert.setTitle("Error");
            alert.showAndWait();
        } catch (MessagingException me) {
            System.out.println("Message Exception");
            me.getMessage();
            me.printStackTrace();
        }
    }

    public void handleReset(ActionEvent actionEvent) {
        getEmailForPW();
    }
}