package model;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.scene.control.Alert;
import scene.rentPopup.RentPopupController;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;
import scene.rentPopup.RentPopupController;

import database.DbConnector;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;

import static java.lang.Math.random;
import static scene.userMenu.UserMenuController.loggedInUser;

public class Logic {
    DbConnector dbConnector = new DbConnector();

    public static void alert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.setHeaderText("We got a message for you");
        alert.setTitle("ErrorBuster 9000");
        alert.showAndWait();
    }

    private void setToFullscreen(Stage stage) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
    }

    public void changeSceneHandler(Event actionEvent, String addressForFXML, boolean maximizeScene) {
        try {
            Parent parent1 = FXMLLoader.load(getClass().getResource(addressForFXML));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(parent1);
            stage.setScene(scene);
            stage.setMaximized(maximizeScene);
            /*Group root = new Group();
            Group circles = new Group();
            for (int i = 0; i < 30; i++) {
                Circle circle = new Circle(150, Color.web("white", 0.05));
                circle.setStrokeType(StrokeType.OUTSIDE);
                circle.setStroke(Color.web("white", 0.16));
                circle.setStrokeWidth(4);
                circles.getChildren().add(circle);
            }
            Rectangle colors = new Rectangle(scene.getWidth(), scene.getHeight(),
                    new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new Stop(0, Color.web("#f8bd55")),
                            new Stop(0.14, Color.web("#c0fe56")),
                            new Stop(0.28, Color.web("#5dfbc1")),
                            new Stop(0.43, Color.web("#64c2f8")),
                            new Stop(0.57, Color.web("#be4af7")),
                            new Stop(0.71, Color.web("#ed5fc2")),
                            new Stop(0.85, Color.web("#ef504c")),
                            new Stop(1, Color.web("#f2660f"))));
            colors.widthProperty().bind(scene.widthProperty());
            colors.heightProperty().bind(scene.heightProperty());
            Group blendModeGroup =
                    new Group(new Group(new Rectangle(scene.getWidth(), scene.getHeight(),
                            Color.BLACK), circles), colors);
            colors.setBlendMode(BlendMode.OVERLAY);
            root.getChildren().add(blendModeGroup);
            circles.setEffect(new BoxBlur(10, 10, 3));
            Timeline timeline = new Timeline();
            for (Node circle : circles.getChildren()) {
                timeline.getKeyFrames().addAll(
                        new KeyFrame(Duration.ZERO, // set start position at 0
                                new KeyValue(circle.translateXProperty(), random() * 800),
                                new KeyValue(circle.translateYProperty(), random() * 600)),
                        new KeyFrame(new Duration(40000), // set end position at 40s
                                new KeyValue(circle.translateXProperty(), random() * 800),
                                new KeyValue(circle.translateYProperty(), random() * 600)));
            }
            // play 40s of animation
            timeline.play();*/
            if (maximizeScene) {
                setToFullscreen(stage);
            }
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void openSceneInNewWindow(String fxmlPath, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(fxmlPath));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadBrowsePageData(String query, TilePane tilePane) {
        dbConnector.connect();
        try {
            PreparedStatement ps = dbConnector.connection.prepareStatement(query);
            dbConnector.resultSet = ps.executeQuery();
            while (dbConnector.resultSet.next()) {
                String imagePath;
                Movie movie = new Movie(
                        dbConnector.resultSet.getInt("idMovie"),
                        dbConnector.resultSet.getString("title"),
                        dbConnector.resultSet.getString("director"),
                        dbConnector.resultSet.getDouble("price"),
                        Movie.getStringAsGenre(dbConnector.resultSet.getString("genre")),
                        dbConnector.resultSet.getString("releaseYear"),
                        dbConnector.resultSet.getInt("quantity"),
                        imagePath = dbConnector.resultSet.getString("imagePath")
                );
                TilePane tempTilePane = new TilePane();
                tempTilePane.setPrefColumns(1);
                tempTilePane.setPrefRows(5);
                tempTilePane.setPadding(new Insets(30));
                ImageView tempImageView = new ImageView();
                tempImageView.getStyleClass().add("image-view-user-menu");
                tempImageView.setFitHeight(200);
                tempImageView.setFitWidth(133);
                Image image = new Image(imagePath);
                tempImageView.setImage(image);
                tempImageView.setOnMouseClicked(e -> {
                    RentPopupController.setMovieToRent(movie);
                    openSceneInNewWindow("/scene/rentPopup/rentPopup.fxml", "Rent Movie");
                });
                tempTilePane.getChildren().addAll(tempImageView);
                tilePane.getChildren().add(tempTilePane);
                tilePane.setPrefColumns(10);
            }
        } catch (Exception e) {
            System.out.println("ohShit");
            e.printStackTrace();
        } finally {
            dbConnector.disconnect();
        }
    }

    public static final String ACCOUNT_SID = "AC6f314e8681deaa0ae4d82eaf59876daa";
    public static final String AUTH_TOKEN = "79be81f419784528cdb25a38226909df";

    public static void textMessageHandler() {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message
                .creator(new PhoneNumber("+46734453860"), // to
                        new PhoneNumber("+46769448476"), // from
                        "Test text")
                .create();

        System.out.println(message.getSid());
    }

    public void pdf() {
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Receipt.pdf"));
            document.open();
            document.add(new Paragraph("Receipt of Rentals"));
            String rentals = String.valueOf(dbConnector.showRentals(loggedInUser.getIdUser()));
            document.add(new Paragraph(String.valueOf(dbConnector.showRentals(loggedInUser.getIdUser()))));
            document.close();
            writer.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

/*
    // krille - work in progress


    /* krille - work in progress

    public void generatePassword(){
        int length = 10;
        String symbol = "-/.^&*_!@%=+>)";
        String cap_letter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String small_letter = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        String finalString = cap_letter + small_letter +
                numbers + symbol;

        Random random = new Random();

        char[] password = new char[length];

        for (int i = 0; i < length; i++)
        {
            password[i] =
                    finalString.charAt(random.nextInt(finalString.length()));
        }
        System.out.println(password);
    }
    */
}