package com.dynamjb.controller;

import com.dynamjb.DynaMJBApplication;
import com.dynamjb.ui.gameobjects.Player;
import com.dynamjb.ui.gameobjects.TileObject;
import com.dynamjb.ui.pane.MainPane;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.ImagePattern;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.dynamjb.constants.GameConstants.*;

public class LabyrinthControllerImpl implements LabyrinthController {
    public static final Logger logger = Logger.getLogger(DynaMJBApplication.class.getName());

    private int[][] labyrinth = { // Sample labyrinth data
            {32, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 34},
            {35, 2, 1, 0, 2, 1, 3, 2, 2, 2, 2, 0, 0, 2, 36},
            {35, 0, 44, 2, 44, 5, 44, 2, 44, 3, 44, 4, 44, 5, 36},
            {37, 2, 2, 2, 2, 2, 2, 2, 2, 0, 2, 2, 2, 2, 38},
            {39, 2, 44, 2, 44, 2, 44, 2, 44, 0, 44, 2, 44, 2, 40},
            {35, 2, 0, 0, 2, 2, 2, 2, 5, 2, 5, 3, 2, 2, 36},
            {35, 2, 44, 2, 44, 2, 44, 2, 44, 2, 44, 2, 44, 0, 36},
            {35, 2, 0, 2, 3, 2, 2, 4, 4, 2, 2, 2, 2, 1, 36},
            {35, 2, 44, 2, 44, 2, 44, 5, 44, 2, 44, 3, 44, 2, 36},
            {37, 2, 4, 2, 2, 5, 1, 5, 1, 3, 2, 2, 1, 2, 38},
            {39, 2, 44, 2, 44, 0, 44, 0, 44, 2, 44, 0, 44, 0, 40},
            {35, 0, 1, 5, 5, 2, 1, 3, 3, 5, 1, 3, 3, 2, 36},
            {43, 43, 43, 43, 43, 43, 43, 43, 43, 43, 43, 43, 43, 43, 43},
    };
    // Offsets of solid Tiles
    private int[] solidTiles = {44};
    static String tilePath = Objects.requireNonNull(MainPane.class.getResource(LABYRINTH_IMAGE)).toString();
    public static ImagePattern[] labyrinthset;
    List<TileObject>[][] stackedLabyrinth;
    // Player
    static String playerPath = Objects.requireNonNull(MainPane.class.getResource(PLAYER_IMAGE)).toString();
    public static ImagePattern[] playerset;
    public List<Player> players;


    public LabyrinthControllerImpl() {
        labyrinthset = loadTileset(tilePath, TILE_SIZE, TILE_SIZE);
        playerset = loadTileset(playerPath, 24, 32);

        createStackedLabyrinth();
        createPlayers();
    }

    private void createPlayers() {
        players = new ArrayList<>();
        Player player1 = new Player(24, 32, 1.5, 10, this);
        Player player2 = new Player(24, 32, 4, 4, this);
        players.add(player1);
        players.add(player2);
        setPlayerMoveState(players, Player.GO_DOWN, player2.getPlayerId());

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setPlayerPos(players, 6, 6, player1.getPlayerId());
                setPlayerMoveState(players, Player.GO_LEFT, player1.getPlayerId());
                setPlayerMoveState(players, Player.GO_UP, player2.getPlayerId());

            }
        }, 4000); //  (4 seconds)
    }

    private void createStackedLabyrinth() {
        createLabyrinth(labyrinth, solidTiles);
        // Create block
        addTileObjectToStackedLabyrinth(1, 1, new TileObject(16, true,this));
        addTileObjectToStackedLabyrinth(4, 3, new TileObject(16, true,this));
        addTileObjectToStackedLabyrinth(2, 5, new TileObject(16, true,this));
        addTileObjectToStackedLabyrinth(4, 7, new TileObject(16, true,this));

        // Create a Booster and animated block
        addTileObjectToStackedLabyrinth(11, 10, new TileObject(80,this));
        addTileObjectToStackedLabyrinth(11, 10,
                new TileObject(new int[]{16, 17, 18, 19, 20, 21, 22},this));

        // Create Booster and a Block above
        addTileObjectToStackedLabyrinth(10, 1, new TileObject(80,this));
        addTileObjectToStackedLabyrinth(10, 1, new TileObject(16, true,this));

        // Create a Booster
        addTileObjectToStackedLabyrinth(3, 3, new TileObject(80,this));

        // Create Booster
        addTileObjectToStackedLabyrinth(5, 5, new TileObject(96,this));
    }

    @Override
    public List<TileObject>[][] getStackedLabyrinth() {
        return stackedLabyrinth;
    }

    @Override
    public ImagePattern[] getPlayerSet() {
        return playerset;
    }

    public ImagePattern[] getLabyrinthSet() {
        return labyrinthset;
    }

    private void addTileObjectToStackedLabyrinth(int x, int y, TileObject tileObject) {
        this.stackedLabyrinth[y][x].add(tileObject);
    }

    private void createLabyrinth(int[][] labyrinth, int[] solidTiles) {
        int rows = labyrinth.length;
        int columns = labyrinth[0].length;

        List<TileObject>[][] stackedLabyrinth = new List[rows][columns];

        // Initialize each element of the array with an empty list
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                stackedLabyrinth[i][j] = new ArrayList<>();

                // TODO get more than one Image for Animation
//                int[] animationPattern = {labyrinth[i][j]};
                int imageOffset = labyrinth[i][j];

//                TileObject newTileObject = new TileObject(animationPattern,this);
                TileObject newTileObject = new TileObject(imageOffset,this);

                // make Tiles solid
                for (int solidTile : solidTiles) {
                    if (imageOffset == solidTile) {
                        newTileObject.setSolid(true);
                    }
                }
                stackedLabyrinth[i][j].add(newTileObject);
            }
        }
        this.stackedLabyrinth = stackedLabyrinth;
    }

    public static ImagePattern[] loadTileset(
            String imagePath,
            int tileWidth,
            int tileHeight
    ) {
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

    public double getMapStartX() {
        double mapStartX = 1 * TILE_SIZE;
        return mapStartX;
    }

    public double getMapStartY() {
        double mapStartY = 1 * TILE_SIZE;
        return mapStartY;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public void setPlayerPos(List<Player> players, double x, double y, long id) {
        for (Player player : players) {
            if (player.getPlayerId() == id) {
                player.setxPosition(x);
                player.setyPosition(y);
                break;
            }
        }
    }

    public void setPlayerMoveState(List<Player> players, int[] moveState, long id) {
        for (Player player : players) {
            if (player.getPlayerId() == id) {
                player.setPlayerMoveState(moveState);
                break;
            }
        }
    }

    public void animationFinished(long id){
        logger.log(Level.INFO, "Tile with id : " + id + " finish");

    }
}
