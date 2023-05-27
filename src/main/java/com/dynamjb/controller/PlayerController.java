package com.dynamjb.controller;

import com.dynamjb.ui.gameobjects.Player;

// This class contains all methods to change a player object
public class PlayerController {

    private final Player contolledPlayer;

    public PlayerController(Player playerToControl){
        this.contolledPlayer = playerToControl;
    }

    public void increaseXPos(){
        double dx = contolledPlayer.getSpeed();
        contolledPlayer.setXPosition(contolledPlayer.getXPosition() + dx);
    }
    public void decreaseXPos(){
        double dx = contolledPlayer.getSpeed();
        contolledPlayer.setXPosition(contolledPlayer.getXPosition() - dx);
    }

//    public void increaseYPos(){
//        double dy = contolledPlayer.getSpeed();
//        contolledPlayer.setYPosition(contolledPlayer.getYPosition() + dy);
//    }
//    public void decreaseYPos() {
//        double dy = contolledPlayer.getSpeed();
//        contolledPlayer.setYPosition(contolledPlayer.getYPosition() - dy);
//    }
    public void clearVelocity(){
        contolledPlayer.setVelocityX(0);
        contolledPlayer.setVelocityY(0);
    }
    public void setVelocity(double vertical, double horizontal){
        contolledPlayer.setVelocityY(vertical);
        contolledPlayer.setVelocityX(horizontal);
    }
}
