package sample;

import javafx.animation.TranslateTransition;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class GameSubScene extends SubScene {

     private final static String FONT_PATH = "images/kenvector_future.ttf";
     private final static String BACKGROUND_IMAGE_PATH = "images/blue.jpg";
     private boolean isHidden;




    public GameSubScene() {
        super(new AnchorPane(), 600, 400);
        prefWidth(600);
        prefWidth(400);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(BACKGROUND_IMAGE_PATH, 600, 400, false, true)
                , BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,null);
        AnchorPane anchorPane = (AnchorPane) this.getRoot();
        anchorPane.setBackground(new Background(backgroundImage));
        isHidden = true;
        setLayoutX(1024);
        setLayoutY(180);
    }


    public void moveSubScene(){
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.3));
        transition.setNode(this);

        if(isHidden==true){
            transition.setToX(-676);
            isHidden=false;
        } else {

            transition.setToX(0);
            isHidden=true;

        }

        transition.play();
    }

    public AnchorPane getAnchorPane(){
        return (AnchorPane) this.getRoot();
    }

}
