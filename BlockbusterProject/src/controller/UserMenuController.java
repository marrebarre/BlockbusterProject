package controller;

/*import data.DbConnector;*/
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class UserMenuController {

    @FXML
    ImageView aStarIsBorn, inception;


    /*private DbConnector dbConnector = new DbConnector();*/


    public void hoverOverPictureEntered(){
        aStarIsBorn.setFitHeight(250);
        aStarIsBorn.setFitWidth(250);

    }
    public void hoverOverPictureExit(){
        aStarIsBorn.setFitWidth(200);
        aStarIsBorn.setFitHeight(200);

    }
    public void mouseOverPicture(){
        inception.setFitHeight(250);
        inception.setFitWidth(250);
    }
    public void mouseOverPictureExit(){
        inception.setFitWidth(200);
        inception.setFitHeight(200);
    }


}
