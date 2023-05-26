package com.dynamjb.controller;

import com.dynamjb.DynaMJBApplication;
import com.dynamjb.ui.gameobjects.*;
import com.dynamjb.ui.pane.MainPane;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
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

    // StackedLabyrinth Borders for Clipping
    int minX;
    int maxX;
    int minY;
    int maxY;

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
        // Set Borders for FameClipping
        setBorders(1, 1, 1, 1);
        createPlayers();

        createStackedLabyrinth();
    }

    public Player getPlayer(int player){
        return players.get(player);
    }

    public void doPlayers(){
        for (Player player:players){
            double x = player.getXPosition();
            double y =player.getYPosition();
            double velocitX = player.getVelocityX();
            double velocitY = player.getVelocityY();

            // Check Position in Map, Collision with Flames or Booster ...
            x += velocitX;
            y += velocitY;

            player.setXPosition(x);
            player.setYPosition(y);

        }

    }
    private void createPlayers() {
        players = new ArrayList<>();
        Player player1 = new Player(24, 32, 1.5, 11, this);
        Player player2 = new Player(24, 32, 3, 4, this);
        players.add(player1);
        players.add(player2);
        setPlayerMoveState(players, Player.GO_DOWN, player2.getPlayerId());

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setPlayerPos(players, 7, 7, player1.getPlayerId());
                setPlayerMoveState(players, Player.GO_LEFT, player1.getPlayerId());
                setPlayerMoveState(players, Player.GO_UP, player2.getPlayerId());
                setBomb(11, 3, 0, 2);
            }
        }, 6000); //  (4 seconds)        Timer timer = new Timer();


        Timer timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                setPlayerPos(players, 7, 7, player1.getPlayerId());
                setPlayerMoveState(players, Player.GO_LEFT, player1.getPlayerId());
                setPlayerMoveState(players, Player.GO_UP, player2.getPlayerId());
//                setBomb(11, 3, 0, 2);
                setBomb(3, 9, 0, 3);
                setBomb(9, 8, 1, 5);
            }
        }, 9000); //  (4 seconds)

    }

    private void createStackedLabyrinth() {
        createLabyrinth(labyrinth, solidTiles);

        setBlock(4, 3);
        setBlock(2, 5);
        setBlock(4, 7);
        setBlock(8, 7);
        setBlock(11, 5);
        setBlock(9, 3);

//        setBomb(3, 9, 0, 3);
//        setBomb(9, 8, 1, 5);

        setBoosterBombCount(1, 1);
        setBoosterSpeed(2, 1);
        setBoosterInvincible(3,1);
        setBoosterLife(4, 1);
        setBoosterBombStrength(5,1);
        setBoosterPhasing(6,7);
    }

    private boolean inLabyrinth(int x, int y) {
        return ((x >= minX) && (x <= maxX) && (y >= minY) && (y <= maxY));
    }


    protected boolean setFlame(long playerId, int x, int y, int[] animationPattern) {
        Flame flame = new Flame(playerId, animationPattern, this);
        boolean success = addTileObjectToStackedLabyrinth(x, y, flame);
        if (!success) {
            long tileObjectId = isExplosive(x, y);
            if (tileObjectId > -1) {
                logger.log(Level.INFO, "bomb is exploded with id : " + tileObjectId);
                explodeTileObject(tileObjectId);
            } else {
                startDestruction(x, y);
            }
        }
        return success;
    }

    protected boolean setFlames(long playerId, int flameLength, int x, int y) {
//        if (!inLabyrinth(x, y) && isSolid(x, y)) {
//            return false;
//        }
        if (!setFlame(playerId, x, y, new int[]{48, 49, 50, 51, 50, 49, 52})) {
            return false;
        }

        for (int dx = 1; dx <= flameLength; dx++) {
            if (inLabyrinth(x + dx, y)) {
                if ((dx == flameLength)) {
                    setFlame(playerId, x + dx, y, new int[]{69, 70, 71, 72, 71, 70, 73});
                } else {

                    if (!setFlame(playerId, x + dx, y, new int[]{85, 86, 87, 88, 87, 86, 89})) {
                        break;
                    } else {
                        startDestruction(x + dx, y);
                    }
                }
            }
        }
        for (int dx = 1; dx <= flameLength; dx++) {
            if (inLabyrinth(x - dx, y)) {
                if (dx == flameLength) {
                    setFlame(playerId, x - dx, y, new int[]{53, 54, 55, 56, 55, 54, 57});
                } else {

                    if (!setFlame(playerId, x - dx, y, new int[]{85, 86, 87, 88, 87, 86, 89})) {
                        break;
                    } else {
                        startDestruction(x - dx, y);
                    }
                }
            }

        }
        for (int dy = 1; dy <= flameLength; dy++) {
            if (inLabyrinth(x, y - dy)) {
                if (dy == flameLength) {
                    setFlame(playerId, x, y - dy, new int[]{64, 65, 66, 67, 66, 64, 68});

                } else {
                    if (!setFlame(playerId, x, y - dy, new int[]{80, 81, 82, 83, 82, 81, 84})) {
                        break;
                    } else {
                        startDestruction(x, y - dy);
                    }
                }
            }
        }
        for (int dy = 1; dy <= flameLength; dy++) {
            if (inLabyrinth(x, y + dy)) {
                if (dy == flameLength) {
                    setFlame(playerId, x, y + dy, new int[]{96, 97, 98, 99, 98, 97, 100});

                } else {
                    if (!setFlame(playerId, x, y + dy, new int[]{80, 81, 82, 83, 82, 81, 84})) {
                        break;
                    } else {
                        startDestruction(x, y + dy);
                    }
                }
            }
        }
        return true;
    }

    private void setBlock(int x, int y) {
        Block block = new Block(new int[]{16, 17, 18, 19, 20, 21, 22}, this);
        addTileObjectToStackedLabyrinth(x, y,
                block);
    }

    private void setBomb(int x, int y, long ownerId, int strenght) {
        Bomb bomb = new Bomb(
                ownerId,
                strenght,
                new int[]{112, 112, 113, 114, 112, 112, 113, 114, 112, 112, 113, 114},
                this
        );
           addTileObjectToStackedLabyrinth(x, y, bomb);
    }

    private void setBoosterBombCount(int x, int y) {
        addTileObjectToStackedLabyrinth(x, y, new BoosterBombCount(new int[]{128, 129}, this));
    }
    private void setBoosterBombStrength(int x, int y) {
        addTileObjectToStackedLabyrinth(x, y, new BoosterBombCount(new int[]{136, 137}, this));
    }
    private void setBoosterPhasing(int x, int y) {
        addTileObjectToStackedLabyrinth(x, y, new BoosterBombCount(new int[]{138, 139}, this));
    }
    private void setBoosterSpeed(int x, int y) {
        addTileObjectToStackedLabyrinth(x, y, new BoosterBombCount(new int[]{130, 131}, this));
    }
    private void setBoosterLife(int x, int y) {
        addTileObjectToStackedLabyrinth(x, y, new BoosterBombCount(new int[]{134, 135}, this));
    }
    private void setBoosterInvincible(int x, int y) {
        addTileObjectToStackedLabyrinth(x, y, new BoosterBombCount(new int[]{132, 133}, this));
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

    /**
     * Adds a TileObject to the stacked labyrinth at coordinates (x, y) if there is no solid tile.
     * @param x          The x-coordinate of the tile.
     * @param y          The y-coordinate of the tile.
     * @param tileObject The TileObject to add.
     * @return True if the TileObject was added successfully, false if the tile is solid.
     */
    private boolean addTileObjectToStackedLabyrinth(int x, int y, TileObject tileObject) {
        if (!isSolid(x, y)) {
            this.stackedLabyrinth[y][x].add(tileObject);
            return true;
        }
        return false;
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

                if (isTileSolid(imageOffset)) {
                    stackedLabyrinth[i][j].add(new WallTile(imageOffset, this));

                } else {
                    stackedLabyrinth[i][j].add(new GroundTile(imageOffset, this));
                }
            }
        }
        this.stackedLabyrinth = stackedLabyrinth;
    }

    private boolean isTileSolid(int imageOffset) {
        for (int solidTile : solidTiles) {
            if (imageOffset == solidTile) {
                return true;
            }
        }
        return false;
    }

    private void setBorders(int offsetTop, int offsetRight, int offsetBottom, int offsetLeft) {
        this.minY = offsetTop;
        this.maxX = labyrinth[0].length - offsetRight - offsetLeft;
        this.maxY = labyrinth.length - offsetBottom - offsetTop;
        this.minX = offsetLeft;
    }

    private TileObject getTileObjectById(long tileObjectId) {
        int rows = labyrinth.length;
        int columns = labyrinth[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                List<TileObject> tileObjects = stackedLabyrinth[i][j];
                for (TileObject tileObject : tileObjects) {
                    if (tileObject.getId() == tileObjectId) {
                        return tileObject;
                    }
                }
            }
        }
        return null;
    }

    private boolean deleteTileObject(long tileObjectId) {
        int rows = labyrinth.length;
        int columns = labyrinth[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                List<TileObject> tileObjects = stackedLabyrinth[i][j];
                for (TileObject tileObject : tileObjects) {
                    if (tileObject.getId() == tileObjectId) {
                        tileObjects.remove(tileObject);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean explodeTileObject(long tileObjectId) {
        int rows = labyrinth.length;
        int columns = labyrinth[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                List<TileObject> tileObjects = stackedLabyrinth[i][j];
                for (TileObject tileObject : tileObjects) {
                    if (tileObject.getId() == tileObjectId) {
                        tileObjects.remove(tileObject);
                        setFlames(
                                tileObjectId,
                                tileObject.getExplosiveStrength(),
                                j, i);
                        return true;
                    }
                }
            }
        }
        return false;
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
        mapStartX = 0;
        return mapStartX;
    }

    public double getMapStartY() {
        double mapStartY = 1 * TILE_SIZE;
        mapStartY = 0;
        return mapStartY;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public void setPlayerPos(List<Player> players, double x, double y, long id) {
        for (Player player : players) {
            if (player.getPlayerId() == id) {
                player.setXPosition(x);
                player.setYPosition(y);
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

    /**
     * Checks if a tile at the coordinates (x, y) is destrutible and start the animation .
     * @param x The x-coordinate of the tile.
     * @param y The y-coordinate of the tile.
     */
    public void startDestruction(int x, int y) {
        List<TileObject> tileObjects = stackedLabyrinth[y][x];

        for (TileObject tileObject : tileObjects) {
            if (tileObject.isDestructible()) {
                tileObject.startAnimate();
            }
        }
    }

    /**
     * Checks if a tile at the coordinates (x, y) is solid .
     * @param x The x-coordinate of the tile.
     * @param y The y-coordinate of the tile.
     * @return True if the tile is solid, false otherwise.
     */
    public boolean isSolid(int x, int y) {
        List<TileObject> tileObjects = stackedLabyrinth[y][x];

        for (TileObject tileObject : tileObjects) {
            if (tileObject.isSolid()) {
                return true;
            }
        }
        return false;
    }

    /**
     *  Checks if a tile at the coordinates (x, y) is explosive.
     *  Returns the ID of the explosive object if found, or -1 if no explosive object is present.
     *  @param x The x-coordinate of the tile.
     *  @param y The y-coordinate of the tile.
     *  @return The ID of the explosive object, or -1 if no explosive object is present.
     */
    public long isExplosive(int x, int y) {
        List<TileObject> tileObjects = stackedLabyrinth[y][x];
        for (TileObject tileObject : tileObjects) {
            if (tileObject.isExplosive()) {
                return tileObject.getId();
            }
        }
        return -1;
    }

    /**
     * Handles the event when an animation finishes for a specific tile object.
     * Performs actions based on the provided AnimationFinishEvent.
     * @param id The ID of the tile object.
     * @param animationFinishEvent The type of animation finish event.
     */
    public void animationFinished(long id, TileObject.AnimationFinishEvent animationFinishEvent) {
        switch (animationFinishEvent) {
            case DESTROY:
                deleteTileObject(id);
                break;
            case EXPLODE:
                explodeTileObject(id);
                break;
            default:
                logger.log(Level.INFO, "Tile with id : " + id + " " + animationFinishEvent.toString());
        }
    }
}
