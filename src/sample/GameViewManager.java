package sample;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameViewManager {

    private AnchorPane gamePane;
    private Scene gameScene;
    private Stage gameStage;

    private static final int GAME_WIDTH = 600;
    private static final int GAME_HEIGHT = 800;

    private Stage menuStage;
    private ImageView ship;

    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;
    private int angle;
    AnimationTimer gameTimer;

    private GridPane gridPane1;
    private GridPane gridPane2;
    private final static String BACKGROUND_PATH = "images/galaxy3.jpg";
    private final static String METEOR_GREY_PATH = "images/meteorGrey_med2.png";
    private final static String METEOR_BROWN_PATH = "images/meteorBrown_med1.png";
    private final static String GAME_OVER_BACKGROUND_PATH = "images/red_button13.png";
    private static final String RETURN_TO_MAIN_MENU_BUTTON_BACKGROUND = "images/green_button13.png";
    private static final String INCREASE_ATTACK_SPEED_POWERUP_IMAGE_PATH = "images/bolt_gold.png";

    private ImageView[] greyMeteors;
    private ImageView[] brownMeteors;
    Random randomPositionGenerator;

    private ImageView star;
    private ImageView[] playerLifes;
    int playerLife;
    int points;
    private final static String GOLD_STAR_PATH = "images/star_gold.png";
    SmallInfoLabel pointsLabel;

    private final static int STAR_RADIUS = 12;
    private final static int SHIP_RADIUS = 27;
    private final static int METEOR_RADIUS = 20;
    private final static int ARROW_RADIUS = 10;

    private final static String FIRE_IMAGE_PATH = "images/fire15.png";
    private ArrayList<Arrow> arrows;
    private boolean isSpacePressed;

    private long timeOfFire=0, currentTime, startTime;
    private long allowedTimeOfFire=700; //attack speed

    protected GameSubScene gameOverSubScene;
    public static String gameDifficulty = "초급";
    private int numOfMeteors;
    private int meteorSpeed;

    private ImageView attackSpeedPowerUp;
    private final static int POWERUP_RADIUS = 11;

    private final static long enemyShipSpawnDelay = 4000;
    private boolean isEnemyShipOnMap=false;
    private EnemyShip enemyShip;
    private final static int ENEMY_SHIP_RADIUS = 25;


    public GameViewManager(){
        initializeStage();
        createKeyListeners();
        randomPositionGenerator = new Random();
        //enemyShip = new EnemyShip();
    }

    public void createNewGame(Stage menuStage, SHIP chosenShip){
        this.menuStage = menuStage;
        this.menuStage.hide();
        createBackground();
        createShip(chosenShip);
        createGameElements(chosenShip);
        startTime = System.currentTimeMillis();
        createGameLoop();
        gameStage.show();

        System.out.println("Difficulty: "+gameDifficulty);

    }

    private void createGameLoop(){
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                moveBackground();
                moveShip();
                moveGameElements();
                checkIfElementsAreBehindTheShipAndRelocate();
                checkIfElementsCollide();
                fireArrow();
                updateCurrentTime();
              //  createEnemyShip();

            }
        };
        gameTimer.start();
    }

    private void createGameElements(SHIP chosenSHip){
        playerLife=3;
        star = new ImageView(GOLD_STAR_PATH);
        setNewElementPosition(star);
        gamePane.getChildren().add(star);
        pointsLabel = new SmallInfoLabel("점 : 00");
        pointsLabel.setLayoutX(460);
        pointsLabel.setLayoutY(20);
        gamePane.getChildren().add(pointsLabel);
        playerLifes = new ImageView[3];
        arrows=new ArrayList<Arrow>();
        attackSpeedPowerUp = new ImageView(INCREASE_ATTACK_SPEED_POWERUP_IMAGE_PATH);
        gamePane.getChildren().add(attackSpeedPowerUp);
        setNewElementPosition(attackSpeedPowerUp);

        enemyShip = new EnemyShip();
        createEnemyShip();

        for(int i=0;i<playerLifes.length;i++){
            playerLifes[i] = new ImageView(chosenSHip.getUrlLife());
            playerLifes[i].setLayoutX(455+(i*50));
            playerLifes[i].setLayoutY(50);
            gamePane.getChildren().add(playerLifes[i]);
        }

        if(gameDifficulty=="초급"){
            numOfMeteors=3;
        } else if(gameDifficulty=="중급"){
            numOfMeteors=7;
        } else if(gameDifficulty=="고급"){
            numOfMeteors=11;
        }

       // numOfMeteors=0; //disabled for testing only

        brownMeteors = new ImageView[numOfMeteors];
        for(int i=0;i<brownMeteors.length;i++){
            brownMeteors[i] = new ImageView(METEOR_BROWN_PATH);
            setNewElementPosition(brownMeteors[i]);
            gamePane.getChildren().add(brownMeteors[i]);
        }

        greyMeteors = new ImageView[numOfMeteors];
        for(int i=0;i<greyMeteors.length;i++){
            greyMeteors[i] = new ImageView(METEOR_GREY_PATH);
            setNewElementPosition(greyMeteors[i]);
            gamePane.getChildren().add(greyMeteors[i]);
        }




    }

    private void setNewElementPosition(ImageView image){
        image.setLayoutX(randomPositionGenerator.nextInt(550));   //prev 370
        image.setLayoutY(-(randomPositionGenerator.nextInt(3200)+600));
    }


    private boolean enemyShipReachedRightSide=false;
    private boolean enemyShipReachedLeftSide=false;


    private void moveGameElements(){

        star.setLayoutY(star.getLayoutY()+5);
        attackSpeedPowerUp.setLayoutY(attackSpeedPowerUp.getLayoutY()+9);

        for(Arrow arrow: arrows){
            arrow.getArrow().setLayoutY(arrow.getArrow().getLayoutY()-5);
        }

        if(gameDifficulty=="초급"){
            meteorSpeed=7;
        } else if(gameDifficulty=="중급"){
            meteorSpeed=8;
        } else if(gameDifficulty=="고급"){
            meteorSpeed=10;
        }

        for(int i=0;i<brownMeteors.length;i++){
            brownMeteors[i].setLayoutY(brownMeteors[i].getLayoutY()+meteorSpeed);
            if(gameDifficulty=="고급"){
                brownMeteors[i].setLayoutX(brownMeteors[i].getLayoutX()+0.5);
            }
            brownMeteors[i].setRotate(brownMeteors[i].getRotate()+4);
        }
        for(int i=0;i<greyMeteors.length;i++){
            greyMeteors[i].setLayoutY(greyMeteors[i].getLayoutY()+meteorSpeed);
            greyMeteors[i].setRotate(greyMeteors[i].getRotate()+4);
        }

        moveEnemyShip();


    }

    private void moveEnemyShip(){


/*
        if(enemyShip.getEnemyShipImageView().getLayoutX()<80){
            enemyShipReachedLeftSide=true;
            enemyShipReachedRightSide=false;
        }
        if(enemyShip.getEnemyShipImageView().getLayoutX()>450){
            enemyShipReachedRightSide=true;
            enemyShipReachedLeftSide=false;
        }

       // System.out.println("\nreached left: "+enemyShipReachedLeftSide+"\nreached right: "+enemyShipReachedRightSide
      //  +"\nlayout X: "+enemyShip.getEnemyShipImageView().getLayoutX());

        if(enemyShipReachedLeftSide) {
            enemyShip.getEnemyShipImageView().setLayoutX(enemyShip.getEnemyShipImageView().getLayoutX()+2);

        } else if(enemyShipReachedRightSide) {
            enemyShip.getEnemyShipImageView().setLayoutX(enemyShip.getEnemyShipImageView().getLayoutX()-2);

        } else {

                enemyShip.getEnemyShipImageView().setLayoutX(enemyShip.getEnemyShipImageView().getLayoutX()-2);
        }

*/

    }

    private void checkIfElementsAreBehindTheShipAndRelocate(){

        if(star.getLayoutY()>1200){
            setNewElementPosition(star);
        }
        if(attackSpeedPowerUp.getLayoutY()>3500){
            setNewElementPosition(attackSpeedPowerUp);
        }

        List<Arrow> arrowsToRemove = new ArrayList<Arrow>();
        for(Arrow arrow: arrows){
            if(arrow.getArrow().getLayoutY()<=10){

                gamePane.getChildren().remove(arrow.getArrow());
                arrowsToRemove.add(arrow);
            }
        }
        arrows.removeAll(arrowsToRemove);


        for(int i=0;i<brownMeteors.length;i++){
            if(brownMeteors[i].getLayoutY()>900){
                setNewElementPosition(brownMeteors[i]);
            }
        }
        for(int i=0;i<greyMeteors.length;i++){
            if(greyMeteors[i].getLayoutY()>900){
                setNewElementPosition(greyMeteors[i]);
            }
        }
    }

    private void createKeyListeners(){
        gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.LEFT){
                  //  System.out.println("You pressed left key");
                    isLeftKeyPressed = true;
                } else if(keyEvent.getCode() == KeyCode.RIGHT){
                    isRightKeyPressed = true;
                } else if(keyEvent.getCode() == KeyCode.Q){
                    isSpacePressed = true;

                }
            }
        });

        gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.LEFT){
                    isLeftKeyPressed=false;
                } else if(keyEvent.getCode() == KeyCode.RIGHT){
                    isRightKeyPressed=false;
                } else if(keyEvent.getCode() == KeyCode.Q){
                    isSpacePressed = false;
                }
            }
        });






    }




    private void updateCurrentTime(){
        currentTime = System.currentTimeMillis();
    }

    private void fireArrow(){
        if(isSpacePressed){


            if(currentTime-timeOfFire>=allowedTimeOfFire){
                timeOfFire = System.currentTimeMillis();
                Arrow firedArrow = new Arrow();
                firedArrow.getArrow().setLayoutX(ship.getLayoutX()+43);
                firedArrow.getArrow().setLayoutY(ship.getLayoutY()-14);
                arrows.add(firedArrow);
                gamePane.getChildren().add(firedArrow.getArrow());
                isSpacePressed = false;
            }


        }
    }

    private void createBackground(){
        gridPane1 = new GridPane();
        gridPane2 = new GridPane();

        for(int i=0;i<12;i++){
            ImageView backgroundImage1 = new ImageView(new Image(BACKGROUND_PATH, 600, 800, false, true));
            ImageView backgroundImage2 = new ImageView(new Image(BACKGROUND_PATH, 600, 800, false, true));
            GridPane.setConstraints(backgroundImage1, i%3, i/3);
            GridPane.setConstraints(backgroundImage2, i%3, i/3);
            gridPane1.getChildren().add(backgroundImage1);
            gridPane2.getChildren().add(backgroundImage2);
        }
        gridPane2.setLayoutY(-1024);
        gamePane.getChildren().addAll(gridPane1,gridPane2);
    }

    private void moveBackground(){
        gridPane1.setLayoutY(gridPane1.getLayoutY()+3);
        gridPane2.setLayoutY(gridPane2.getLayoutY()+3);

        if(gridPane1.getLayoutY() >=1024){
            gridPane1.setLayoutY(-1024);
        }
        if(gridPane2.getLayoutY() >=1024){
            gridPane2.setLayoutY(-1024);
        }

    }



    private void moveShip(){
        if(isLeftKeyPressed && isRightKeyPressed==false){
            if(angle> -30){
                angle = angle - 5;
            }
            ship.setRotate(angle);
            if(ship.getLayoutX()> -20){
                ship.setLayoutX(ship.getLayoutX()-4);
            }
        }

        if(isRightKeyPressed && isLeftKeyPressed==false){
            if(angle< 30){
                angle = angle + 5;
            }
            ship.setRotate(angle);
            if(ship.getLayoutX() < 522){
                ship.setLayoutX(ship.getLayoutX()+4);
            }

        }
        if(isRightKeyPressed && isLeftKeyPressed){
            if(angle<0){
                angle+=5;
            } else if(angle>0){
                angle-=5;
            }
            ship.setRotate(angle);
        }
        if(isRightKeyPressed==false && isLeftKeyPressed==false){
            if(angle<0){
                angle+=5;
            } else if(angle>0){
                angle-=5;
            }
            ship.setRotate(angle);
        }

    }







    private void createShip (SHIP chosenShip){
        ship = new ImageView(chosenShip.getUrlShip());
        ship.setLayoutX(GAME_WIDTH/2);
        ship.setLayoutY(GAME_HEIGHT-90);
        gamePane.getChildren().add(ship);
    }


    private void initializeStage(){
        gamePane = new AnchorPane();
        gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
        gameStage = new Stage();
        gameStage.setScene(gameScene);
    }

    private void checkIfElementsCollide(){
        if(SHIP_RADIUS + STAR_RADIUS > calculateDistance(ship.getLayoutX()+49, star.getLayoutX()+15,ship.getLayoutY()+37, star.getLayoutY()+15)){
            setNewElementPosition(star);
            points+=3;
            String textToSet = "점 : ";
            if(points<10){
                textToSet = textToSet + "0";
            }
            pointsLabel.setText(textToSet + points);
        }

        if(SHIP_RADIUS + POWERUP_RADIUS > calculateDistance(ship.getLayoutX()+49, attackSpeedPowerUp.getLayoutX()+15,
                ship.getLayoutY()+37, attackSpeedPowerUp.getLayoutY()+15)){
            setNewElementPosition(attackSpeedPowerUp);
            if(allowedTimeOfFire>=100){
                allowedTimeOfFire = allowedTimeOfFire - 50;
            }


        }



        for (int i = 0; i < brownMeteors.length; i++) {
            if (METEOR_RADIUS + SHIP_RADIUS > calculateDistance(ship.getLayoutX() + 49, brownMeteors[i].getLayoutX() + 20,
                    ship.getLayoutY() + 37, brownMeteors[i].getLayoutY() + 20)) {
                removeLife();
                setNewElementPosition(brownMeteors[i]);
            }
        }

        for (int i = 0; i < greyMeteors.length; i++) {
            if (METEOR_RADIUS + SHIP_RADIUS > calculateDistance(ship.getLayoutX() + 49, greyMeteors[i].getLayoutX() + 20,
                    ship.getLayoutY() + 37, greyMeteors[i].getLayoutY() + 20)) {
                removeLife();
                setNewElementPosition(greyMeteors[i]);
            }
        }

        List<Arrow> arrowsToRemove = new ArrayList<>();
        for(Arrow arrow : arrows){
            for(int i=0;i<greyMeteors.length;i++){
                if(METEOR_RADIUS + ARROW_RADIUS > calculateDistance(arrow.getArrow().getLayoutX()+5, greyMeteors[i].getLayoutX()+20,
                        arrow.getArrow().getLayoutY()+10, greyMeteors[i].getLayoutY()+20)){
                    setNewElementPosition(greyMeteors[i]);
                    gamePane.getChildren().remove(arrow.getArrow());
                    arrowsToRemove.add(arrow);
                    points++;
                    String textToSet = "점 : ";
                    if(points<10){
                        textToSet = textToSet + "0";
                    }
                    pointsLabel.setText(textToSet + points);
                }


                    boolean colisionDetected=false;

                if(ENEMY_SHIP_RADIUS + ARROW_RADIUS > calculateDistance(arrow.getArrow().getLayoutX()+5, enemyShip.getEnemyShipImageView().getLayoutX()+20,
                        arrow.getArrow().getLayoutY()+10, enemyShip.getEnemyShipImageView().getLayoutY()+20)){
                  /*  if( enemyShip.getEnemyShipImageView().getBoundsInParent().intersects(arrow.getArrow().getBoundsInParent())){
                      colisionDetected=true;
                }*/


                        gamePane.getChildren().remove(arrow.getArrow());
                        colisionDetected=true;
                        System.out.println("HERE!");
                        arrowsToRemove.add(arrow);
                        enemyShip.setHitpoints(enemyShip.getHitpoints()-1);
                        if(enemyShip.getHitpoints()<=0){
                            System.out.println("Enemy ship dead!");
                            gamePane.getChildren().remove(enemyShip.getEnemyShipImageView());
                            //  isEnemyShipOnMap=false;
                            points+=5;
                            String textToSet = "점 : ";
                            if(points<10){
                                textToSet = textToSet + "0";
                            }
                            pointsLabel.setText(textToSet + points);
                            //createEnemyShip();
                        }

                    }

            }

            for(int i=0;i<brownMeteors.length;i++){
                if(METEOR_RADIUS + ARROW_RADIUS > calculateDistance(arrow.getArrow().getLayoutX()+5, brownMeteors[i].getLayoutX()+20,
                        arrow.getArrow().getLayoutY()+10, brownMeteors[i].getLayoutY()+20)){
                    setNewElementPosition(brownMeteors[i]);
                    gamePane.getChildren().remove(arrow.getArrow());
                    arrowsToRemove.add(arrow);
                    points++;
                    String textToSet = "점 : ";
                    if(points<10){
                        textToSet = textToSet + "0";
                    }
                    pointsLabel.setText(textToSet + points);
                }
            }
        }
        arrows.removeAll(arrowsToRemove);
    }

    private void removeLife(){
        gamePane.getChildren().remove(playerLifes[playerLife-1]);
        playerLife--;
        if(playerLife<=0){
            createGameOverSubScene();
          //  gameStage.close();
            gameTimer.stop();
          //  menuStage.show();
        }
    }

    private double calculateDistance(double x1, double x2, double y1, double y2){
        return Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
    }

    protected void createGameOverSubScene(){
        gameOverSubScene = new GameSubScene();
        gameOverSubScene.setWidth(500);

        gameOverSubScene.setLayoutX(44);
        gameOverSubScene.setLayoutY(200);
        gameOverSubScene.prefHeight(900);

        gamePane.getChildren().add(gameOverSubScene);
        InfoLabel gameOverLabel = new InfoLabel("게임 끝나기");
        gameOverLabel.setLayoutX(55);
        gameOverLabel.setLayoutY(100);
        gameOverLabel.setTheBackgroundImage(GAME_OVER_BACKGROUND_PATH);
        gameOverSubScene.getAnchorPane().getChildren().add(gameOverLabel);

        SmallInfoLabel pointsLabel = new SmallInfoLabel("최종 점 : " + points);
        pointsLabel.setLayoutX(350);
        pointsLabel.setLayoutY(175);
        gameOverSubScene.getAnchorPane().getChildren().add(pointsLabel);

        Button1 returnToMainMenuButton = new Button1("나가기");
        returnToMainMenuButton.setLayoutY(250);
        returnToMainMenuButton.setLayoutX(55);

        returnToMainMenuButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gameStage.close();
                menuStage.show();
            }
        });
        gameOverSubScene.getAnchorPane().getChildren().add(returnToMainMenuButton);
    }

    //currentTime-timeOfFire>=allowedTimeOfFire
    private void createEnemyShip (){
          //this must be created OUTSIDE this method because every tick, the game creates a new object. TO BE MODIFIED!!!

        //System.out.println("current time: "+ currentTime+"\nenemy ship spwn time: "+enemyShip.getEnemyShipSpawnTime());
        //if(isEnemyShipOnMap==false && currentTime-enemyShip.getEnemyShipSpawnTime()>=enemyShipSpawnDelay){
            System.out.println("Enemy ship created\n"+"current time: "+ currentTime+"\nenemy ship spwn time: "+enemyShip.getEnemyShipSpawnTime());
            enemyShip = new EnemyShip();
            enemyShip.getEnemyShipImageView().setLayoutX(randomPositionGenerator.nextInt(650));
            enemyShip.getEnemyShipImageView().setLayoutY(200);
            gamePane.getChildren().add(enemyShip.getEnemyShipImageView());
            isEnemyShipOnMap = true;
            enemyShip.updateEnemyShipSpawnTime();
      //  }
    }


    private void createEnemyShipFirstTime (){
        //this must be created OUTSIDE this method because every tick, the game creates a new object. TO BE MODIFIED!!!

       // System.out.println("current time: "+ currentTime+"\nenemy ship spwn time: "+enemyShip.getEnemyShipSpawnTime());
        if(isEnemyShipOnMap==false && currentTime-enemyShip.getEnemyShipSpawnTime()>=enemyShipSpawnDelay){
            System.out.println("Enemy ship created\n"+"current time: "+ currentTime+"\nenemy ship spwn time: "+enemyShip.getEnemyShipSpawnTime());
            enemyShip = new EnemyShip();
            enemyShip.getEnemyShipImageView().setLayoutX(randomPositionGenerator.nextInt(650));
            enemyShip.getEnemyShipImageView().setLayoutY(200);
            gamePane.getChildren().add(enemyShip.getEnemyShipImageView());
            isEnemyShipOnMap = true;
            enemyShip.updateEnemyShipSpawnTime();
        }
    }




}
