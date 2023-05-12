package com.example.dynamjb.ui.viewModel;

import com.example.dynamjb.DynaMJBApplication;
import com.example.dynamjb.ui.pane.MainPane;
import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.ImagePattern;
import java.util.Objects;
import java.util.logging.Logger;
import static com.example.dynamjb.constants.GameConstants.FRAMES_PER_SECOND;
import static com.example.dynamjb.constants.GameConstants.TILE_SIZE;
import static com.example.dynamjb.ui.pane.MainPane.loadTileset;

public class MainViewModel {
    private static final Logger logger = Logger.getLogger(DynaMJBApplication.class.getName());

    public static final int[][] LABYRINTH = { // Sample labyrinth data
            {16, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 18},
            {32, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 2, 34},
            {48, 0, 1, 2, 1, 2, 1, 2, 1, 2, 1, 0, 1, 0, 50},
            {64, 2, 2, 0, 2, 2, 2, 2, 2, 0, 2, 2, 2, 2, 66},
            {32, 2, 1, 0, 1, 2, 1, 2, 1, 0, 1, 2, 1, 2, 34},
            {32, 2, 0, 0, 2, 2, 2, 2, 0, 2, 2, 2, 2, 2, 34},
            {32, 2, 1, 2, 1, 0, 1, 2, 1, 2, 1, 2, 1, 0, 34},
            {32, 2, 0, 2, 2, 0, 0, 2, 2, 2, 2, 2, 2, 0, 34},
            {48, 2, 2, 2, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 50},
            {64, 2, 1, 2, 1, 2, 1, 0, 1, 0, 1, 2, 1, 2, 66},
            {32, 2, 2, 2, 2, 0, 0, 0, 2, 2, 2, 0, 2, 0, 34},
            {32, 0, 1, 0, 1, 2, 1, 0, 1, 2, 1, 2, 1, 2, 34},
            {32, 2, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 34},
            {33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33},
    };
    static String tilePath = Objects.requireNonNull(MainPane.class.getResource("/gfx/tiles_16x16_21x22.png")).toString();
    public static ImagePattern[] tileset = loadTileset(tilePath, TILE_SIZE, TILE_SIZE);

    private static final long FRAME_TIME_NS = 1000000000L / FRAMES_PER_SECOND;

    private AnimationTimer gameLoop;
    private boolean isRunning = false;

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
