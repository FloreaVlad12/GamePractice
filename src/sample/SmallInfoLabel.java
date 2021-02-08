package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SmallInfoLabel extends Label {

    private static final String FONT_PATH = "images/kenvector_future.ttf";
    private static final String BACKGROUND_PATH = "images/green_button13.png";


    public SmallInfoLabel(String text){
        setPrefWidth(130);
        setPrefHeight(50);
        setTheBackground(BACKGROUND_PATH);
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(10,10,10,10));
        setText(text);
        setLabelFont();


    }

    protected void setTheBackground (String backgroundPath){
        BackgroundImage backgroundImage = new BackgroundImage(new Image(backgroundPath,130,50,false,true), BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,null);
        setBackground(new Background(backgroundImage));
    }

    private void setLabelFont(){
        try {
            setFont(Font.loadFont(new FileInputStream(new File(FONT_PATH)),15));
        } catch (FileNotFoundException e) {
            setFont(Font.font("Veranda",15));
        }
    }

}
