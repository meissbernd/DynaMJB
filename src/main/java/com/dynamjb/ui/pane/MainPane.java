package com.dynamjb.ui.pane;

import com.dynamjb.DynaMJBApplication;
import com.dynamjb.ui.gameobjects.Player;
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

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.dynamjb.constants.GameConstants.*;

public class MainPane extends Pane {
    private static final Logger logger = Logger.getLogger(DynaMJBApplication.class.getName());
    private StackPane stackPane;
    private final Canvas canvas;
    private final GraphicsContext gc;
    private double mapScale = 4;
    private MainViewModel viewModel;

    // Represents the distance from the canvas where the play map starts,
    private double mapstartX = 1 * TILE_SIZE;
    private double mapstartY = 1 * TILE_SIZE;

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
                redraw(MainViewModel.tileset, viewModel.LABYRINTH);
                viewModel.setRedrawNeeded(false);
                logger.log(Level.INFO, "mapScale :", mapScale);
            }
        });

        // Create a Canvas with initial size
        this.canvas = new Canvas(
                viewModel.LABYRINTH[0].length * TILE_SIZE * mapScale,
                viewModel.LABYRINTH.length * TILE_SIZE * mapScale);
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
                viewModel.LABYRINTH[0].length * TILE_SIZE * mapScale,
                viewModel.LABYRINTH.length * TILE_SIZE * mapScale);

        Player player = new Player(
                "/gfx/bomberman1.png",
                24,
                32,
                mapstartX,
                mapstartY,
                1.5,
                10);

        viewModel.rootHeightProperty().addListener((obs, oldVal, newVal) -> {
            calcScale(newVal.doubleValue());
            player.updateDimensions(this.mapScale);
        });

        stackPane.getChildren().add(player.getImageView());

        // Add the StackPane to the MainPane
        getChildren().add(stackPane);
        viewModel.startGameLoop();
//        viewModel.setRedrawNeeded(true);
    }

    private void calcScale(double height) {
        this.mapScale = (
                height - TOP_PANE_HEIGHT - BOTTOM_PANE_HEIGHT)
                / viewModel.LABYRINTH.length
                / TILE_SIZE;
        logger.log(Level.INFO, "calcScale: " + this.mapScale);
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

    /**
     * Loads a tileset from the specified image file path, with the given tile width and height.
     *
     * @param imagePath  the file path of the tileset image
     * @param tileWidth  the width of each tile in pixels
     * @param tileHeight the height of each tile in pixels
     * @return an array of ImagePattern objects, each representing a tile in the tileset
     */
    public static ImagePattern[] loadTileset(String imagePath, int tileWidth, int tileHeight) {
        Image image = new Image(imagePath);
        int numTilesX = (int) image.getWidth() / tileWidth;
        int numTilesY = (int) image.getHeight() / tileHeight;
        ImagePattern[] tileset = new ImagePattern[numTilesX * numTilesY];

        for (int y = 0; y < numTilesY; y++) {
            for (int x = 0; x < numTilesX; x++) {
                int startX = x * tileWidth;
                int startY = y * tileHeight;
                Image tileImage = new WritableImage(image.getPixelReader(), startX, startY, tileWidth, tileHeight);
                tileset[y * numTilesX + x] = new ImagePattern(tileImage);
            }
        }
        return tileset;
    }
}