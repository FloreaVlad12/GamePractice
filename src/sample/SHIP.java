package sample;

public enum SHIP {
    BLACK ("images/enemyBlack3.png", "images/playerLife3_orange.png"),
    BLUE ("images/enemyBlue3.png", "images/playerLife1_blue.png"),
    RED ("images/enemyRed1.png", "images/playerLife1_red.png"),
    GREEN ("images/enemyGreen2.png", "images/playerLife1_green.png");

    String urlShip;
    String urlLife;

    private SHIP(String urlShip, String urlLife){
        this.urlShip=urlShip;
        this.urlLife=urlLife;
    }

    public String getUrlShip() {
        return urlShip;
    }

    public String getUrlLife(){
        return urlLife;
    }
}
