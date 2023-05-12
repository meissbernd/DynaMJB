package com.dynamjb.ui.pane;

import com.dynamjb.DynaMJBApplication;
import com.dynamjb.ui.gameobjects.Player;
import com.dynamjb.ui.viewModel.MainViewModel;
import javafx.geometry.Insets;
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
import static com.dynamjb.constants.GameConstants.TILE_SIZE;

public class MainPane extends Pane {
    private static final Logger logger = Logger.getLogger(DynaMJBApplication.class.getName());
    private final Canvas canvas;
    private final GraphicsContext gc;
    private double scale = 2.0;
    private double mapSize;
    private double mapstartX = 16;
    private double  mapstartY = 16;

    // Create a StackPane to hold the canvas
    StackPane root = new StackPane();

    public StackPane getRoot() {
        return root;
    }

    public MainPane(MainViewModel viewModel) {
        super(new StackPane());
        // Add a listener to the redrawNeeded property
        viewModel.redrawNeededProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
//                logger.log(Level.INFO, "redrawNeeded: " + newValue);
                // Call the redraw method
                redraw(MainViewModel.tileset, MainViewModel.LABYRINTH);
                viewModel.setRedrawNeeded(false);
            }
        });

        // Create a Canvas with initial size
        this.canvas = new Canvas(MainViewModel.LABYRINTH[0].length * TILE_SIZE * scale, MainViewModel.LABYRINTH.length * TILE_SIZE * scale);
//        canvas.setDisable(true); // disable the canvas temporarily
        this.canvas.setCache(true); // enable caching to improve performance
        this.canvas.setCacheHint(CacheHint.SPEED); // set the caching hint for best performance
//        canvas.setDisable(false); // re-enable the canvas
        gc = this.canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false);

        // Draw the labyrinth on the canvas
        this.root.setAlignment(Pos.TOP_LEFT);
        StackPane.setMargin(canvas, new Insets(0));
        this.root.setPadding(new Insets(0));

        // Add the canvas to the StackPane
        this.root.getChildren().add(this.canvas);

        // Set the size of the StackPane to the size of the canvas
        root.setPrefSize(MainViewModel.LABYRINTH[0].length * TILE_SIZE * scale, MainViewModel.LABYRINTH.length * TILE_SIZE * scale);


        Player player = new Player(
                "/gfx/bomberman1.png",
                24,
                32,
                mapstartX,
                mapstartY,
                0,
                0);



        viewModel.rootHeightProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Root height changed to " + newVal.doubleValue());
            calcScale(newVal.doubleValue());
            System.out.println("Tile Scale " + this.scale);
            player.updateDimensions(this.scale);

            System.out.println("Player Scale " + player.scale);

        });

//        root.heightProperty().addListener((obs, oldVal, newVal) -> {
//            double height = newVal.doubleValue();
//            // do something with the new height value
//            System.out.println("Root height changed to " + height);
//        });

        // Add the StackPane to the MainPane
        getChildren().add(root);

        viewModel.setRedrawNeeded(true);

//        logger.log(Level.INFO, "redrawNeeded: " + viewModel.isRedrawNeeded());

        viewModel.startGameLoop();

        //        logger.log(Level.INFO, "width: " + this.widthProperty().get());

//        calcScale(main);
//        calcScale(MainViewModel.LABYRINTH[0].length * TILE_SIZE * scale, MainViewModel.LABYRINTH.length * TILE_SIZE * scale);
//        redraw(tileset, LABYRINTH); // Call the redraw method on the MainPane instance


        root.getChildren().add(player.getImageView());

    }

    // Other methods...


    public void calcScale(double width, double height) {
        if (width / height <= (double) MainViewModel.LABYRINTH[0].length / MainViewModel.LABYRINTH.length) {
            this.scale = width / (MainViewModel.LABYRINTH[0].length * TILE_SIZE);

        } else {
            this.scale = (MainViewModel.LABYRINTH.length * TILE_SIZE) / height;
        }
    }
    public void  calcScale(double height) {
        scale = (height - 64 * 2) / MainViewModel.LABYRINTH.length / TILE_SIZE;
                logger.log(Level.INFO, "scale: " + this.scale);

    }


//    public void drawPlayer(ImagePattern[] playerset){
//        ImageView playerView = new ImageView(playerset);
//        playerView.setX(100); // set the initial x position
//        playerView.setY(100); // set the initial y position
//        playerView.setFitWidth(32); // set the width of the player image
//        playerView.setFitHeight(32); // set the height of the player image
//        root.getChildren().add(playerView);
//    }
    /**
     * Redraws the labyrinth based on the current scaling factors and tileset.
     * Clears the canvas and draws each tile on the canvas using the corresponding
     * tile pattern from the tileset.
     *
     * @param tileset   an array of ImagePatterns representing each tile in the labyrinth
     * @param labyrinth an int[][]
     */
    public  void redraw(ImagePattern[] tileset, int[][] labyrinth) {
        long startTime = System.nanoTime(); // get the start time


        // Calculate the new size of the canvas based on the scale factors
        double canvasWidth = labyrinth[0].length * TILE_SIZE * this.scale;
        double canvasHeight = labyrinth.length * TILE_SIZE * this.scale;

        // Resize the canvas to the new size
        canvas.setWidth(canvasWidth);
        canvas.setHeight(canvasHeight);

        // Clear the canvas
        gc.setFill(Color.RED);
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Apply the scaling transform
        Scale scaleTransform = new Scale(this.scale, this.scale);
        Affine affineTransform = new Affine(scaleTransform);
        gc.setTransform(affineTransform);

        for (int y = 0; y < labyrinth.length; y++) {
            for (int x = 0; x < labyrinth[y].length; x++) {
                gc.setFill(tileset[labyrinth[y][x]]);
                gc.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE + 1, TILE_SIZE + 1);
            }
        }
        long endTime = System.nanoTime(); // get the end time
        double duration = (endTime - startTime) / 1000000.0;

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
