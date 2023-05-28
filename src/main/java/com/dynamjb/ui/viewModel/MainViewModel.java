package com.dynamjb.ui.viewModel;

import com.dynamjb.DynaMJBApplication;
import com.dynamjb.controller.KeyEventManager;
import com.dynamjb.controller.LabyrinthControllerImpl;
import com.dynamjb.controller.LabyrinthObserver;
import com.dynamjb.ui.gameobjects.Player;
import com.dynamjb.ui.gameobjects.TileObject;
import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.ImagePattern;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.dynamjb.constants.GameConstants.*;

public class MainViewModel implements LabyrinthObserver {
    private static final Logger logger = Logger.getLogger(DynaMJBApplication.class.getName());

    // Represents the distance from the canvas where the play map starts,
    private double mapstartX = 1 * TILE_SIZE;
    private double mapstartY = 1 * TILE_SIZE;
    public List<TileObject>[][] stackedLabyrinth;
    public List<Player> players;
    private static final long FRAME_TIME_NS = 1000000000L / FRAMES_PER_SECOND;
    private LabyrinthControllerImpl controller;
    public KeyEventManager keyEventManager;

    private KeyEvent keyEvent = new KeyEvent(
            KeyEvent.KEY_RELEASED,
            "", "",
            KeyCode.UNDEFINED,
            false, false, false, false);
    private AnimationTimer gameLoop;
    private boolean isRunning = false;
    ImagePattern[] playerSet;
    ImagePattern[] labyrinthSet;

    /**
     * Constructs a MainViewModel object with the given LabyrinthControllerImpl.
     *
     * @param labyrinthController The LabyrinthControllerImpl associated with the MainViewModel.
     */
    public MainViewModel(LabyrinthControllerImpl labyrinthController) {
        this.controller = labyrinthController;

        this.labyrinthSet = controller.getLabyrinthSet();
        this.stackedLabyrinth = this.controller.getStackedLabyrinthTiles();

        this.playerSet = controller.getPlayerSet();
        this.players = controller.getPlayers();

        this.keyEventManager = new KeyEventManager(controller.getControllerOfPlayer1(), controller.getControllerOfPlayer2());
    }

    public void doPlayers() {
        controller.updatePlayers();
    }

    private void updateStackedLabyrinth() {
        stackedLabyrinth = controller.getStackedLabyrinthTiles();
    }

    public ImagePattern[] getPlayerSet() {
        return this.playerSet;
    }

    public ImagePattern[] getLabyrinthSet() {
        return this.controller.getLabyrinthSet();
    }

    private void createTestPlayers() {
        players = new ArrayList<>();

    }

    public void startGameLoop() {
        gameLoop = new AnimationTimer() {
            private long lastFrameTime = 0;

            @Override
            public void handle(long now) {
                if (now - lastFrameTime >= FRAME_TIME_NS) {
                    setRedrawNeeded(true);
                    lastFrameTime = now;
                }
            }
        };
        gameLoop.start();
        isRunning = true;
    }

    public void stopGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
        isRunning = false;
    }

    public List<Player> getPlayers() {
        this.players = controller.getPlayers();
        return this.players;
    }

    // Implement the LabyrinthObserver interface methods
    @Override
    public void onLabyrinthChanged(int[][] labyrinth) {
        updateStackedLabyrinth();

    }

    //    @Override
//    public void onPlayerChanged(List<Player> players) {
//        updatePlayer();
//    }
    //------------------------- redrawNeeded --------------------------
    public boolean isRunning() {
        return isRunning;
    }

    private BooleanProperty redrawNeeded = new SimpleBooleanProperty(false);

    public BooleanProperty redrawNeededProperty() {
        return redrawNeeded;
    }

    public void setRedrawNeeded(boolean value) {
        redrawNeeded.set(value);
    }

    public boolean isRedrawNeeded() {
        return redrawNeeded.get();
    }

    //------------------------------ rootHeight --------------------------
    private final DoubleProperty rootHeight = new SimpleDoubleProperty();

    public DoubleProperty rootHeightProperty() {
        return rootHeight;
    }

    public double getRootHeight() {
        return rootHeight.get();
    }

    public void setRootHeight(double height) {
        rootHeight.set(height);
    }

    public KeyEvent getKeyEvent() {
        return keyEvent;
    }

    public void setKeyEvent(KeyEvent keyEvent) {
        this.keyEvent = keyEvent;
    }
}

//enum TileObjectEvent {
//    ANIMATION_FINISH
//}

