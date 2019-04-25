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

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();

        primaryStage.setScene(new Scene(root, width, height));
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