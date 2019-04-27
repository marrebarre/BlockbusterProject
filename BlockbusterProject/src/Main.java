import data.DbConnector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/loginScreenRedux.fxml"));
        root.getStylesheets().add(getClass().getResource("css/mainTheme.css").toExternalForm());
        primaryStage.setTitle("Bustblocker");
        primaryStage.setMaximized(true);
        primaryStage.setScene(new Scene(root));
        //primaryStage.initStyle(StageStyle.TRANSPARENT); FULLSKÄRM UTAN KNAPPARNA!
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