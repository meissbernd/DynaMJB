package com.dynamjb.controller;

import com.dynamjb.DynaMJBApplication;
import com.dynamjb.ui.gameobjects.*;
import com.dynamjb.ui.pane.MainPane;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.ImagePattern;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.dynamjb.constants.GameConstants.*;
import static java.lang.Math.round;

public class LabyrinthControllerImpl implements LabyrinthController {
    public static final Logger logger = Logger.getLogger(DynaMJBApplication.class.getName());

    private int[][] labyrinth = new LabyrinthGrid().grid;

//    // StackedLabyrinth Borders for Clipping
//    private int minX;
//    private int maxX;
//    private int minY;
//    private int maxY;

    // Offsets of solid Tiles
    private int[] solidTiles = {44};
    static String tilePath = Objects.requireNonNull(MainPane.class.getResource(LABYRINTH_IMAGE)).toString();
    private ImagePattern[] labyrinthSet;
    List<TileObject>[][] stackedLabyrinthTiles;
    LabyrinthStacked stackedLabyrinth;
    // Player
    static String playerPath = Objects.requireNonNull(MainPane.class.getResource(PLAYER_IMAGE)).toString();
    private ImagePattern[] playerSet;
    private final List<Player> players;
    private List<PlayerController> controlledPlayers;

    public LabyrinthControllerImpl() {
        this.labyrinthSet = loadTileset(tilePath, TILE_SIZE, TILE_SIZE);
        this.playerSet = loadTileset(playerPath, 24, 32);

        this.players = createPlayers();
        this.controlledPlayers = createPlayerControllers(this.players);

        this.stackedLabyrinth = new LabyrinthStacked(this.labyrinth, solidTiles, this);
        this.stackedLabyrinthTiles = this.stackedLabyrinth.tilesOfLabyrinth;
    }

    public PlayerController getControllerOfPlayer1() {
        return this.controlledPlayers.get(0);
    }
    public PlayerController getControllerOfPlayer2() {
        return this.controlledPlayers.get(1);
    }


    /**
     * Do all updates of players (move in labyrinth, collision with flames, boost velocity
     */
    public void updatePlayers() {
        for (PlayerController controlledPlayer : this.controlledPlayers) {

            Player player = controlledPlayer.getPlayer();
            // ToDo: player.update()

            double x = player.getXPosition();
            double y = player.getYPosition();
            double velocityX = player.getVelocityX();
            double velocityY = player.getVelocityY();


            // Check Position in Map, Collision with Flames or Booster ...
            x += velocityX;
            y += velocityY;

            Coordinates left = new Coordinates((int)x-1, (int)y);
            Coordinates leftDown = new Coordinates((int)x-1, (int)y+1);
            Coordinates right = new Coordinates((int)x+1, (int)y);
            Coordinates rightDown = new Coordinates((int)x-1, (int)y+1);

            if((y-(int)y) <0.5) {
                if (isTileObject(right, WallTile.class)) {
                    x = (int) x;
                    y = y + velocityX;
                }
            } else {
                if (isTileObject(rightDown, WallTile.class)) {
                    x = (int) x;
                    y = y - velocityX;
                }
            }

            player.setXPosition(x);
            player.setYPosition(y);

        }

    }
//    double adjustVertical(double vertical, double velocity){
//        if(vertical-(int)vertical < 0.5){
//            if((vertical+velocity)-(int)(vertical+velocity) < 0.5){
//                return vertical+velocity;
//            } else {
//                return (int)(vertical+velocity);
//            }
//        }else {
//            if((vertical+velocity)-(int)(vertical+velocity) >= 0.5){
//                return vertical+velocity;
//            } else {
//                return (int)(vertical+velocity);
//            }
//        }
//    }

    private List<Player> createPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        Player player1 = new Player(24, 32, 1.5, 11, this);
        Player player2 = new Player(24, 32, 3, 4, this);
        players.add(player1);
        players.add(player2);
//        setPlayerMoveState(players, Player.GO_DOWN, player2.getPlayerId());
        return players;
    }

    private List<PlayerController> createPlayerControllers(List<Player> players) {
        controlledPlayers = new ArrayList<>();
        for (Player player : players) {
            controlledPlayers.add(new PlayerController(player));
        }
        return controlledPlayers;
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
            if (stackedLabyrinth.inLabyrinth(x + dx, y)) {
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
            if (stackedLabyrinth.inLabyrinth(x - dx, y)) {
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
            if (stackedLabyrinth.inLabyrinth(x, y - dy)) {
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
            if (stackedLabyrinth.inLabyrinth(x, y + dy)) {
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

    @Override
    public List<TileObject>[][] getStackedLabyrinthTiles() {
        return stackedLabyrinthTiles;
    }

    @Override
    public ImagePattern[] getPlayerSet() {
        return playerSet;
    }

    public ImagePattern[] getLabyrinthSet() {
        return labyrinthSet;
    }

    /**
     * Adds a TileObject to the stacked labyrinth at coordinates (x, y) if there is no solid tile.
     *
     * @param x          The x-coordinate of the tile.
     * @param y          The y-coordinate of the tile.
     * @param tileObject The TileObject to add.
     * @return True if the TileObject was added successfully, false if the tile is solid.
     */
    private boolean addTileObjectToStackedLabyrinth(int x, int y, TileObject tileObject) {
        if (!isSolid(x, y)) {
            this.stackedLabyrinthTiles[y][x].add(tileObject);
            return true;
        }
        return false;
    }

//    private TileObject getTileObjectById(long tileObjectId) {
//        int rows = labyrinth.length;
//        int columns = labyrinth[0].length;
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < columns; j++) {
//                List<TileObject> tileObjects = stackedLabyrinthTiles[i][j];
//                for (TileObject tileObject : tileObjects) {
//                    if (tileObject.getId() == tileObjectId) {
//                        return tileObject;
//                    }
//                }
//            }
//        }
//        return null;
//    }

    private boolean deleteTileObject(long tileObjectId) {
        int rows = labyrinth.length;
        int columns = labyrinth[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                List<TileObject> tileObjects = stackedLabyrinthTiles[i][j];
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
                List<TileObject> tileObjects = stackedLabyrinthTiles[i][j];
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

//    public void setPlayerPos(List<Player> players, double x, double y, long id) {
//        for (Player player : players) {
//            if (player.getPlayerId() == id) {
//                player.setXPosition(x);
//                player.setYPosition(y);
//                break;
//            }
//        }
//    }

//    public void setPlayerMoveState(List<Player> players, int[] moveState, long id) {
//        for (Player player : players) {
//            if (player.getPlayerId() == id) {
//                player.setPlayerMoveState(moveState);
//                break;
//            }
//        }
//    }

    /**
     * Checks if a tile at the coordinates (x, y) is destrutible and start the animation .
     *
     * @param x The x-coordinate of the tile.
     * @param y The y-coordinate of the tile.
     */
    public void startDestruction(int x, int y) {
        List<TileObject> tileObjects = stackedLabyrinthTiles[y][x];

        for (TileObject tileObject : tileObjects) {
            if (tileObject.isDestructible()) {
                tileObject.startAnimate();
            }
        }
    }

    /**
     * Checks if a tile at the coordinates (x, y) is solid .
     *
     * @param x The x-coordinate of the tile.
     * @param y The y-coordinate of the tile.
     * @return True if the tile is solid, false otherwise.
     */
    public boolean isSolid(int x, int y) {
        List<TileObject> tileObjects = stackedLabyrinthTiles[y][x];

        for (TileObject tileObject : tileObjects) {
            if (tileObject.isSolid()) {
                return true;
            }
        }
        return false;
    }
    /**
     * Checks if there is a tile object of the specified type at the given coordinates in the labyrinth.
     * @param x The x-coordinate of the tile object.
     * @param y The y-coordinate of the tile object.
     * @param tileType The type of tile object to check for.
     * @return {@code false} if a tile object of the specified type is found, {@code false} otherwise.
     */
    public boolean isTileObject(int x, int y, Class<? extends TileObject> tileType) {
        List<TileObject> tileObjects = stackedLabyrinthTiles[y][x];

        for (TileObject tileObject : tileObjects) {
            if (tileType.isInstance(tileObject)) {
                return true;
            }
        }
        return false;
    }
    public boolean isTileObject(Coordinates coord, Class<? extends TileObject> tileType) {
        return isTileObject(coord.getX(), coord.y, tileType);
    }

    /**
     * Checks if a tile at the coordinates (x, y) is explosive.
     * Returns the ID of the explosive object if found, or -1 if no explosive object is present.
     *
     * @param x The x-coordinate of the tile.
     * @param y The y-coordinate of the tile.
     * @return The ID of the explosive object, or -1 if no explosive object is present.
     */
    public long isExplosive(int x, int y) {
        List<TileObject> tileObjects = stackedLabyrinthTiles[y][x];
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
     *
     * @param id                   The ID of the tile object.
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
    public class Coordinates {
        private int x;
        private int y;

        public Coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

}
