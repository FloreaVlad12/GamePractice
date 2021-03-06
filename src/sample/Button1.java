package sample;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Button1 extends Button {

    private  final String FONT_PATH = "src/images/kenvector_future.tff";
    private final String BUTTON_PRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/images/blue_button00.png');";
    private final String BUTTON_FREE_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/images/blue_button00.png');";

     public Button1(String shownString){
         setText(shownString);
         setButtonFont();
         setPrefHeight(49);
         setPrefWidth(190);
         setStyle(BUTTON_FREE_STYLE);
         initialiseButtonListeners();
     }

     private void setButtonFont (){
         try {
             setFont(Font.loadFont(new FileInputStream(FONT_PATH), 23));
         } catch (FileNotFoundException e) {
             setFont(Font.font("Veranda", 23));
         }
     }

     private void setButtonPressedStyle (){
         setStyle(BUTTON_PRESSED_STYLE);
         setPrefHeight(45);
         setLayoutY(getLayoutY()+4);
     }

     private void setButtonReleasedStyle (){
         setStyle(BUTTON_FREE_STYLE);
         setPrefHeight(49);
         setLayoutY(getLayoutY()-4);
     }

     private void initialiseButtonListeners(){
         setOnMousePressed(new EventHandler<MouseEvent>() {
             @Override
             public void handle(MouseEvent mouseEvent) {
                 if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                     setButtonPressedStyle();
                 }
             }
         });

         setOnMouseReleased(new EventHandler<MouseEvent>() {
             @Override
             public void handle(MouseEvent mouseEvent) {
                 if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                     setButtonReleasedStyle();
                 }
             }
         });

         setOnMouseEntered(new EventHandler<MouseEvent>() {
             @Override
             public void handle(MouseEvent mouseEvent) {
                 setEffect(new DropShadow());
             }
         });

         setOnMouseExited(new EventHandler<MouseEvent>() {
             @Override
             public void handle(MouseEvent mouseEvent) {
                 setEffect(null);

             }
         });

     }
}
