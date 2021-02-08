package sample;

import javafx.scene.image.ImageView;

public class Arrow {

    private final static String FIRE_IMAGE_PATH = "images/fire15.png";
    private ImageView arrow;

    public Arrow(){
        arrow = new ImageView(FIRE_IMAGE_PATH);
    }

    public ImageView getArrow() {
        return arrow;
    }
}
