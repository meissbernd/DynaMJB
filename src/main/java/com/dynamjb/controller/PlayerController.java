package com.dynamjb.controller;

import com.dynamjb.ui.gameobjects.Player;

// This class contains all methods to change a player object
public class PlayerController {

    private final Player contolledPlayer;

    public PlayerController(Player playerToControl){
        this.contolledPlayer = playerToControl;
    }

    public void increaseXPos(){
        contolledPlayer.setXPosition(contolledPlayer.getXPosition() + 0.5);
    }
    public void decreaseXPos(){
        contolledPlayer.setXPosition(contolledPlayer.getXPosition() - 0.5);
    }

    public void increaseYPos(){
        contolledPlayer.setYPosition(contolledPlayer.getYPosition() + 0.5);
    }
    public void decreaseYPos() {
        contolledPlayer.setYPosition(contolledPlayer.getYPosition() - 0.5);
    }
}
