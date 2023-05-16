package com.dynamjb.ui.pane;

import com.dynamjb.DynaMJBApplication;
import com.dynamjb.ui.gameobjects.Player;
import com.dynamjb.ui.gameobjects.TileObject;
import com.dynamjb.ui.viewModel.MainViewModel;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Scale;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.dynamjb.constants.GameConstants.*;

public class MainPane extends Pane {
    private static final Logger logger = Logger.getLogger(DynaMJBApplication.class.getName());
    private StackPane stackPane;
    private final Canvas canvas;
    private final GraphicsContext gc;
    private double mapScale;
    private MainViewModel viewModel;

    public StackPane getStackPane() {
        return stackPane;
    }

    public MainPane(MainViewModel viewModel) {
        super();
        stackPane = new StackPane();
        this.viewModel = viewModel;
        // Add a listener to the redrawNeeded property
        viewModel.redrawNeededProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                redrawStackedLabyrinth(viewModel.getLabyrinthSet(), viewModel.getStackedLabyrinth());
                viewModel.setRedrawNeeded(false);
            }
        });
        // Create a Canvas with initial size
        this.canvas = new Canvas(
                viewModel.labyrinth[0].length * TILE_SIZE * mapScale,
                viewModel.labyrinth.length * TILE_SIZE * mapScale);
        this.canvas.setCache(true);                 // enable caching to improve performance
        this.canvas.setCacheHint(CacheHint.SPEED);  // set the caching hint for best performance
        gc = this.canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false);

        // Draw the labyrinth on the canvas
        this.stackPane.setAlignment(Pos.TOP_LEFT);

        // Add the canvas to the StackPane
        this.stackPane.getChildren().add(this.canvas);

        // Set the size of the StackPane to the size of the canvas
        stackPane.setPrefSize(
                viewModel.labyrinth[0].length * TILE_SIZE * mapScale,
                viewModel.labyrinth.length * TILE_SIZE * mapScale);

        viewModel.rootHeightProperty().addListener((obs, oldVal, newVal) -> {
            calcScale(newVal.doubleValue());
            for (Player player : viewModel.players) {
                player.updateDimensions(this.mapScale);
            }
        });
        for (Player player : viewModel.players) {
            stackPane.getChildren().add(player.getImageView());
        }

        // Add the StackPane to the MainPane
        getChildren().add(stackPane);
        viewModel.startGameLoop();
//        viewModel.setRedrawNeeded(true);
    }

    private void calcScale(double height) {
        this.mapScale = (
                height - TOP_PANE_HEIGHT - BOTTOM_PANE_HEIGHT)
                / viewModel.labyrinth.length
                / TILE_SIZE;
    }

    /**
     * Redraws the labyrinth based on the current scaling factors and tileset.
     * Clears the canvas and draws each tile on the canvas using the corresponding
     * tile pattern from the tileset.
     *
     * @param tileset   an array of ImagePatterns representing each tile in the labyrinth
     * @param labyrinth an int[][]
     */
    public void redraw(ImagePattern[] tileset, int[][] labyrinth) {
//        long startTime = System.nanoTime(); // get the start time

        // Calculate the new size of the canvas based on the scale factors
        double canvasWidth = labyrinth[0].length * TILE_SIZE * this.mapScale;
        double canvasHeight = labyrinth.length * TILE_SIZE * this.mapScale;

        // Resize the canvas to the new size
        canvas.setWidth(canvasWidth);
        canvas.setHeight(canvasHeight);

        // Clear the canvas
        gc.setFill(Color.RED);
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Apply the scaling transform
        Scale scaleTransform = new Scale(this.mapScale, this.mapScale);
        Affine affineTransform = new Affine(scaleTransform);
        gc.setTransform(affineTransform);

        for (int y = 0; y < labyrinth.length; y++) {
            for (int x = 0; x < labyrinth[y].length; x++) {
                gc.setFill(tileset[labyrinth[y][x]]);
                gc.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE + 1, TILE_SIZE + 1);
            }
        }
//        long endTime = System.nanoTime(); // get the end time
//        double duration = (endTime - startTime) / 1000000.0;
//        logger.log(Level.INFO, "redrawtime: " + duration);
    }

    public void redrawStackedLabyrinth(ImagePattern[] tileset, List<TileObject>[][] labyrinth) {
//        long startTime = System.nanoTime(); // get the start time

        // Calculate the new size of the canvas based on the scale factors
        double canvasWidth = labyrinth[0].length * TILE_SIZE * this.mapScale;
        double canvasHeight = labyrinth.length * TILE_SIZE * this.mapScale;

        // Resize the canvas to the new size
        canvas.setWidth(canvasWidth);
        canvas.setHeight(canvasHeight);

        // Clear the canvas
        gc.setFill(Color.RED);
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Apply the scaling transform
        Scale scaleTransform = new Scale(this.mapScale, this.mapScale);
        Affine affineTransform = new Affine(scaleTransform);
        gc.setTransform(affineTransform);

        for (int y = 0; y < labyrinth.length; y++) {
            for (int x = 0; x < labyrinth[y].length; x++) {

                List<TileObject> tileObjects = labyrinth[y][x];

                for (TileObject tileObject : tileObjects) {

                    gc.setFill(tileset[tileObject.getCurrentImage()]);
                    gc.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE + 1, TILE_SIZE + 1);
                }
//                gc.setFill(tileset[labyrinth[y][x].get(0).getCurrentImage()]);
//                gc.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE + 1, TILE_SIZE + 1);
            }
        }
//        long endTime = System.nanoTime(); // get the end time
//        double duration = (endTime - startTime) / 1000000.0;
//        logger.log(Level.INFO, "redrawtime: " + duration);
    }

    /**
     * Loads a tileset from the specified image file path, with the given tile width and height.
     *
     * @param imagePath  the file path of the tileset image
     * @param tileWidth  the width of each tile in pixels
     * @param tileHeight the height of each tile in pixels
     * @return an array of ImagePattern objects, each representing a tile in the tileset
     */

}