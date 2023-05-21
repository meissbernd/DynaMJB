package com.dynamjb.controller;

import com.dynamjb.ui.gameobjects.Player;
import javafx.scene.input.KeyEvent;

public class KeyEventManager {

    PlayerController controlledPlayer1;
    PlayerController controlledPlayer2;


    public KeyEventManager(Player player1, Player player2){
        this.controlledPlayer1 = new PlayerController(player1);
        this.controlledPlayer2 = new PlayerController(player2);
    }

    public void handle(KeyEvent keyEvent) {
        switch (keyEvent.getCode()){
            case UP:
                System.out.println("P1 up");
                controlledPlayer1.decreaseYPos();
                break;
            case DOWN:
                System.out.println("P1 down");
                controlledPlayer1.increaseYPos();
                break;
            case LEFT:
                System.out.println("P1 left");
                controlledPlayer1.decreaseXPos();
                break;
            case RIGHT:
                System.out.println("P1 right");
                controlledPlayer1.increaseXPos();
                break;
            case W:
                System.out.println("P2 up");
                controlledPlayer2.decreaseYPos();
                break;
            case S:
                System.out.println("P2 down");
                controlledPlayer2.increaseYPos();
                break;
            case A:
                System.out.println("P2 left");
                controlledPlayer2.decreaseXPos();
                break;
            case D:
                System.out.println("P2 right");
                controlledPlayer2.increaseXPos();
                break;
        }
    }

}
