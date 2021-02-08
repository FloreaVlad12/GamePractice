package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ViewManager {

    private static final int HEIGHT = 600;
    private static final int WIDTH = 1024;
    private Scene mainScene;
    private Stage mainStage;
    private AnchorPane mainPane;

    private GameSubScene arinScene;
    private GameSubScene everglowScene;
    private GameSubScene presentSubScene;
    private GameSubScene shipPickerSubScene;
    private List<ShipPicker> shipsList;
    private SHIP chosenShip;







    public ViewManager (){
        mainPane = new AnchorPane();
        mainStage = new Stage();
        mainStage.setTitle("Everglow forever let's go!");
        mainScene = new Scene(mainPane, WIDTH, HEIGHT);

        mainStage.setScene(mainScene);
        createSubScenes();
        addButtons();
        createBackground();


    }

    private void showSubScene (GameSubScene gameSubScene) {
        if(presentSubScene !=null){
            presentSubScene.moveSubScene();
        }
        gameSubScene.moveSubScene();
        presentSubScene = gameSubScene;
    }

    private void createSubScenes(){
        createDifficultyChooserScene();

        everglowScene = new GameSubScene();
        mainPane.getChildren().add(everglowScene);

        createShipChoserSubScene();


    }

    private void createDifficultyChooserScene(){
        arinScene = new GameSubScene();
        String shownText="textnotyetset";
        VBox box = new VBox();
        box.setSpacing(20);
        Button1[] difficultyButtons = new Button1[3];
        Button1 easyButton = new Button1("초급");
        Button1 mediumButton = new Button1("중급");
        Button1 hardButton = new Button1("고급");

        boolean buttonPressed=false;
        for(int i=0;i<3;i++) {
            if (i == 0) {
                shownText = "초급";
            }
            if (i == 1) {
                shownText = "중급";
            }
            if (i == 2) {
                shownText = "고급";
            }
            Button1 difficultyButton = new Button1(shownText);
            difficultyButtons[i] = difficultyButton;
            difficultyButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    for(int j =0;j<difficultyButtons.length;j++){
                        difficultyButtons[j].setEffect(null);
                    }
                    DropShadow shadow = new DropShadow();
                    shadow.setRadius(4);
                    shadow.setSpread(2);
                    difficultyButton.setEffect(shadow);
                    GameViewManager.gameDifficulty = difficultyButton.getText();
                }
            });
            difficultyButton.setOnMouseExited(null);
            difficultyButton.setOnMouseEntered(null);

            box.getChildren().add(difficultyButton);
        }
        box.setLayoutX(100);
        box.setLayoutY(170);
        InfoLabel label = new InfoLabel("어려움 선택");
        label.setLayoutY(30);
        label.setLayoutX(100);

        arinScene.getAnchorPane().getChildren().add(label);
        arinScene.getAnchorPane().getChildren().add(box);

        mainPane.getChildren().add(arinScene);

    }



    private void createShipChoserSubScene(){
        shipPickerSubScene = new GameSubScene();
        mainPane.getChildren().add(shipPickerSubScene);
        InfoLabel chosenShipLabel = new InfoLabel("시프 선택하세요");
        chosenShipLabel.setLayoutX(110);
        chosenShipLabel.setLayoutY(25);
        shipPickerSubScene.getAnchorPane().getChildren().add(chosenShipLabel);
        shipPickerSubScene.getAnchorPane().getChildren().add(createShipsToChoose());
        shipPickerSubScene.getAnchorPane().getChildren().add(addButtonToStart());

    }

    private Button1  addButtonToStart(){
        Button1 startButton = new Button1("시작");
        startButton.setLayoutX(350);
        startButton.setLayoutY(300);
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(chosenShip!=null){
                    GameViewManager gameViewManager = new GameViewManager();
                    gameViewManager.createNewGame(mainStage, chosenShip);
                } else{
                    Text choseShipText = new Text(30,280,"개임을 시작하기 전에 시프 선택해햐 한다");
                    Effect glowEffect = new Glow(5.0);
                    if(choseShipText.getEffect()!=null){
                        choseShipText.setEffect(glowEffect);
                    }
                    choseShipText.setStyle("-fx-font: 24 arial;");
                    shipPickerSubScene.getAnchorPane().getChildren().add(choseShipText);
                }
            }
        });

        return startButton;
    }

    private HBox createShipsToChoose (){
        HBox box = new HBox();
        box.setSpacing(20);
        shipsList = new ArrayList<>();
        for(SHIP ship : SHIP.values()){
            ShipPicker shipToPick = new ShipPicker(ship);
            box.getChildren().add(shipToPick);
            shipsList.add(shipToPick);
            shipToPick.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    for(ShipPicker ship : shipsList){
                        ship.setIsCircleChosen(false);
                    }
                    shipToPick.setIsCircleChosen(true);
                    chosenShip = shipToPick.getShip();
                }
            });
        }

        box.setLayoutX(300-(118*2));
        box.setLayoutY(100);
        return box;


    }


    public Stage getMainStage(){
        return mainStage;
    }

    private void addButtons () {
      /*  Button button = new Button();
        button.setLayoutX(100);
        button.setLayoutY(100);
        mainPane.getChildren().add(button);
        button.setOnMouseEntered(new EventHandler<Event>() {
            @Override
            public void handle(Event mouseEvent) {
                System.out.println("Everglow");

            }
        });*/

      Button1 button1 = new Button1("에버글러우");
      button1.setLayoutX(100);
      button1.setLayoutY(150);
      button1.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent actionEvent) {
              showSubScene(shipPickerSubScene);
          }
      });

      mainPane.getChildren().add(button1);

      Button1 button12 = new Button1("아린");
      button12.setLayoutX(100);
      button12.setLayoutY(250);
      button12.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent actionEvent) {
              showSubScene(arinScene);

          }
      });
      mainPane.getChildren().add(button12);

      Button1 exitButton = new Button1("끄기");
      exitButton.setLayoutX(100);
      exitButton.setLayoutY(350);
      exitButton.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent actionEvent) {
              mainStage.close();
          }
      });
      mainPane.getChildren().add(exitButton);

    }

    private void createBackground(){
        Image image = new Image("images/space.png", 1024, 700, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, null);

        mainPane.setBackground(new Background(backgroundImage));
    }

}
