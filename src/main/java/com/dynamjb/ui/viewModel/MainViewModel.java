package com.dynamjb.ui.viewModel;

import com.dynamjb.DynaMJBApplication;
import com.dynamjb.controller.LabyrinthControllerImpl;
import com.dynamjb.ui.gameobjects.Player;
import com.dynamjb.ui.gameobjects.TileObject;
import com.dynamjb.ui.pane.MainPane;
import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.ImagePattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.dynamjb.constants.GameConstants.*;

public class MainViewModel {
    private static final Logger logger = Logger.getLogger(DynaMJBApplication.class.getName());

    // Represents the distance from the canvas where the play map starts,
    private double mapstartX = 1 * TILE_SIZE;
    private double mapstartY = 1 * TILE_SIZE;
    public int[][] labyrinth;

    public List<Player> players;
    private static final long FRAME_TIME_NS = 1000000000L / FRAMES_PER_SECOND;
    private LabyrinthControllerImpl controller;
    private AnimationTimer gameLoop;
    private boolean isRunning = false;
    ImagePattern[] playerSet;
    ImagePattern[] labyrinthSet;

    public MainViewModel(LabyrinthControllerImpl labyrinthController) {
        this.controller = labyrinthController;

        this.labyrinthSet = controller.getLabyrinthSet();
        this.labyrinth = this.controller.getLabyrinth();

        this.playerSet = controller.getPlayerSet();
        this.players = controller.getPlayers();
    }

    public void onEvent(TileObjectEvent event, int id) {

        switch (event) {
            case ANIMATION_FINISH:
                logger.log(Level.INFO, "tileObject with id: " + id + " finished");
                break;
        }
    }

    public List<TileObject>[][] getStackedLabyrinth() {
        return controller.getStackedLabyrinth();
    }

    public ImagePattern[] getPlayerSet() {
        return this.playerSet;
    }

    public ImagePattern[] getLabyrinthSet() {
        return controller.getLabyrinthSet();
    }

    //    public TileObject[] createTileObjectMap(){
//        List<?>[][] tileObjectMap = new Array[LABYRINTH.length][LABYRINTH[0].length];
//    }
    private void createTestPlayers() {
        players = new ArrayList<>();

    }

//    private int[][] getLabyrinth() {
//        this.labyrinth = labyrinthController.getLabyrinth();
//    }

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

    public boolean isRunning() {
        return isRunning;
    }

    //------------------------- redrawNeeded --------------------------
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
}

enum TileObjectEvent {
    ANIMATION_FINISH
}

