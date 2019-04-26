import data.DbConnector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/loginScreen.fxml"));
        root.getStylesheets().add(getClass().getResource("css/mainTheme.css").toExternalForm());
        primaryStage.setTitle("Bustblocker");
        primaryStage.setMaximized(true);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
        DbConnector connection = new DbConnector();
        connection.connect();
        connection.findMovieInDB("Inglorious Basterds");
        connection.disconnect();
    }
}