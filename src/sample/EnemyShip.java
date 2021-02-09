package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EnemyShip {


    private ImageView enemyShipImageView;
    protected final static String ENEMY_SHIP_IMAGE_PATH = "images/enemy_ship_no_background.png";
    private long enemyShipSpawnTime;
    private int hitpoints;
    private static boolean isEnemyShipOnMap;
    protected boolean isMoving;




    public  EnemyShip(){
         enemyShipImageView = new ImageView(new Image(ENEMY_SHIP_IMAGE_PATH, 170,170,false,true));
       // enemyShipImageView = new ImageView(ENEMY_SHIP_IMAGE_PATH);
        enemyShipSpawnTime = System.currentTimeMillis();
        hitpoints = 5;
        isEnemyShipOnMap=false;
        isMoving = true;

    }


    public ImageView getEnemyShipImageView() {
        return enemyShipImageView;
    }

    public long getEnemyShipSpawnTime() {
        return enemyShipSpawnTime;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public void setHitpoints(int hitpoints) {
        this.hitpoints = hitpoints;
    }

    public void updateEnemyShipSpawnTime(){
        enemyShipSpawnTime = System.currentTimeMillis();
    }

    public void damageEnemyShip(){
        hitpoints--;
    }

    public boolean isEnemyShipOnMap() {
        return isEnemyShipOnMap;
    }

    public void setEnemyShipOnMap(boolean enemyShipOnMap) {
        isEnemyShipOnMap = enemyShipOnMap;
    }
}
