package com.dynamjb.controller;

import com.dynamjb.ui.gameobjects.Player;
import javafx.scene.input.KeyEvent;

import static javafx.scene.input.KeyCode.*;

public class KeyEventManager {

    PlayerController controlledPlayer1;
    PlayerController controlledPlayer2;

    public KeyEventManager(Player player1, Player player2) {
        this.controlledPlayer1 = new PlayerController(player1);
        this.controlledPlayer2 = new PlayerController(player2);
    }

    public void handleReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == UP
                || keyEvent.getCode() == DOWN
                || keyEvent.getCode() == LEFT
                || keyEvent.getCode() == RIGHT) {
            controlledPlayer1.clearVelocity();
        }
        if (keyEvent.getCode() == W
                || keyEvent.getCode() == S
                || keyEvent.getCode() == A
                || keyEvent.getCode() == D) {
            controlledPlayer2.clearVelocity();
        }
    }

    public void handlePressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                System.out.println("P1 up");
//                controlledPlayer1.decreaseYPos();
                controlledPlayer1.setVelocity(-1,0);
                break;
            case DOWN:
                System.out.println("P1 down");
//                controlledPlayer1.increaseYPos();
                controlledPlayer1.setVelocity(1,0);
                break;
            case LEFT:
                System.out.println("P1 left");
//                controlledPlayer1.decreaseXPos();
                controlledPlayer1.setVelocity(0,-1);
                break;
            case RIGHT:
                System.out.println("P1 right");
//                controlledPlayer1.increaseXPos();
                controlledPlayer1.setVelocity(0,1);
                break;
            case W:
                System.out.println("P2 up");
//                controlledPlayer2.decreaseYPos();
                controlledPlayer2.setVelocity(-1,0);
                break;
            case S:
                System.out.println("P2 down");
//                controlledPlayer2.increaseYPos();
                controlledPlayer2.setVelocity(1,0);
                break;
            case A:
                System.out.println("P2 left");
//                controlledPlayer2.decreaseXPos();
                controlledPlayer2.setVelocity(0,-1);
                break;
            case D:
                System.out.println("P2 right");
//                controlledPlayer2.increaseXPos();
                controlledPlayer2.setVelocity(0,1);
                break;
        }
    }
}
