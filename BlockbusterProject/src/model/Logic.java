package model;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;

public class Logic {
    public void setToFullscreen(Stage stage) {
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
            stage.setMaximized(maximizeScene);
            //System.out.println("Fullscreen status tracker: " + maximizeScene);
            if (maximizeScene){
                setToFullscreen(stage);
            }
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}