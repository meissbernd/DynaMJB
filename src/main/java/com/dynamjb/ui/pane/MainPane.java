package com.dynamjb.ui.pane;

import com.dynamjb.DynaMJBApplication;
import com.dynamjb.ui.gameobjects.Player;
import com.dynamjb.ui.gameobjects.TileObject;
import com.dynamjb.ui.viewModel.MainViewModel;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Scale;
import java.util.List;
import java.util.logging.Logger;

import static com.dynamjb.constants.GameConstants.*;

public class MainPane extends Pane {
    public static final Logger logger = Logger.getLogger(DynaMJBApplication.class.getName());

    private StackPane stackPane;
    private final Canvas canvas;
    private final GraphicsContext gc;
    private double mapScale;
    private MainViewModel viewModel;

    /**
     * Constructs a MainPane object with the given MainViewModel.
     *
     * @param viewModel The MainViewModel associated with the MainPane.
     */
    public MainPane(MainViewModel viewModel) {
        super();
        stackPane = new StackPane();
        this.viewModel = viewModel;
        // Add a listener to the redrawNeeded property
        viewModel.redrawNeededProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                redrawStackedLabyrinth(viewModel.getLabyrinthSet(), viewModel.stackedLabyrinth);
                viewModel.setRedrawNeeded(false);
            }
        });
        // Create a Canvas with initial size
        this.canvas = new Canvas(

                viewModel.stackedLabyrinth[0].length * TILE_SIZE * mapScale,
                viewModel.stackedLabyrinth.length * TILE_SIZE * mapScale);
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
                viewModel.stackedLabyrinth[0].length * TILE_SIZE * mapScale,
                viewModel.stackedLabyrinth.length * TILE_SIZE * mapScale);

        viewModel.rootHeightProperty().addListener((obs, oldVal, newVal) -> {
            calcScale(newVal.doubleValue());

            for (Player player : viewModel.players) {
                player.updateDimensions(this.mapScale);
            }
        });
        List<Player> players = viewModel.getPlayers();
        for (Player player : players) {
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
                / viewModel.stackedLabyrinth.length
                / TILE_SIZE;
    }

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

    public StackPane getStackPane() {
        return stackPane;
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
                    gc.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE + 0.3, TILE_SIZE + 0.3);
                }
            }
        }
//        long endTime = System.nanoTime(); // get the end time
//        double duration = (endTime - startTime) / 1000000.0;
//        logger.log(Level.INFO, "redrawtime: " + duration);
    }
}