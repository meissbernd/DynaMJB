package com.dynamjb.ui.gameobjects;

import com.dynamjb.controller.LabyrinthControllerImpl;

import java.util.ArrayList;
import java.util.List;

public class LabyrinthStacked {

    public List<TileObject>[][] stackedLabyrinthWithTiles;

    private LabyrinthControllerImpl controller;

    private int[] solidTiles = {44};

    // StackedLabyrinth Borders for Clipping
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

public LabyrinthStacked(int[][] labyrinth, int[] solidTiles, LabyrinthControllerImpl controller){
    this.controller = controller;

    int rows = labyrinth.length;
    int columns = labyrinth[0].length;

    this.stackedLabyrinthWithTiles = new List[rows][columns];

    // Initialize each element of the array with an empty list
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < columns; j++) {
            this.stackedLabyrinthWithTiles[i][j] = new ArrayList<>();

            // TODO get more than one Image for Animation
            int imageOffset = labyrinth[i][j];

            if (isTileSolid(imageOffset)) {
                this.stackedLabyrinthWithTiles[i][j].add(new WallTile(imageOffset, this.controller));
            } else {
                this.stackedLabyrinthWithTiles[i][j].add(new GroundTile(imageOffset, this.controller));
            }
        }
    }

    // Add more special tiles
    addBlocksAndBoostersToLabyrinth();

    // Set Borders for FameClipping
    setBorders(1, 1, 1, 1);
}

    public TileObject getTileAtGridPosition(int x_grid, int y_grid) {
        return this.stackedLabyrinthWithTiles[x_grid][y_grid].get(0);
    }

    public boolean inLabyrinth(int x_grid, int y_grid) {
        return ((x_grid >= minX) && (x_grid <= maxX) && (y_grid >= minY) && (y_grid <= maxY));
    }

    private void setBorders(int offsetTop, int offsetRight, int offsetBottom, int offsetLeft) {
        this.minY = offsetTop;
//        this.maxX = labyrinth[0].length - offsetRight - offsetLeft;
        this.maxX = stackedLabyrinthWithTiles[0].length - offsetRight - offsetLeft;
//        this.maxY = labyrinth.length - offsetBottom - offsetTop;
        this.maxY = stackedLabyrinthWithTiles.length - offsetBottom - offsetTop;
        this.minX = offsetLeft;
    }

    private void addBlocksAndBoostersToLabyrinth() {
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
        setBoosterInvincible(3, 1);
        setBoosterLife(4, 1);
        setBoosterBombStrength(5, 1);
        setBoosterPhasing(6, 7);
    }

    private void setBoosterBombCount(int x, int y) {
        addTileObjectToStackedLabyrinth(x, y, new BoosterBombCount(new int[]{128, 129}, this.controller));
    }

    private void setBoosterBombStrength(int x, int y) {
        addTileObjectToStackedLabyrinth(x, y, new BoosterBombCount(new int[]{136, 137}, this.controller));
    }

    private void setBoosterPhasing(int x, int y) {
        addTileObjectToStackedLabyrinth(x, y, new BoosterBombCount(new int[]{138, 139}, this.controller));
    }

    private void setBoosterSpeed(int x, int y) {
        addTileObjectToStackedLabyrinth(x, y, new BoosterBombCount(new int[]{130, 131}, this.controller));
    }

    private void setBoosterLife(int x, int y) {
        addTileObjectToStackedLabyrinth(x, y, new BoosterBombCount(new int[]{134, 135}, this.controller));
    }

    private void setBoosterInvincible(int x, int y) {
        addTileObjectToStackedLabyrinth(x, y, new BoosterBombCount(new int[]{132, 133}, this.controller));
    }

    private void setBlock(int x, int y) {
        Block block = new Block(new int[]{16, 17, 18, 19, 20, 21, 22}, this.controller);
        addTileObjectToStackedLabyrinth(x, y, block);
    }

    private boolean addTileObjectToStackedLabyrinth(int x, int y, TileObject tileObject) {
        if (!isSolid(x, y)) {
            this.stackedLabyrinthWithTiles[y][x].add(tileObject);
            return true;
        }
        return false;
    }

    /**
     * Checks if a tile at the coordinates (x, y) is solid .
     *
     * @param x The x-coordinate of the tile.
     * @param y The y-coordinate of the tile.
     * @return True if the tile is solid, false otherwise.
     */
    public boolean isSolid(int x_grid, int y_grid) {
        List<TileObject> tileObjects = this.stackedLabyrinthWithTiles[y_grid][x_grid];
        for (TileObject tileObject : tileObjects) {
            if (tileObject.isSolid()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks tile if solid for Labyrinth creation
     *
     * @param imageOffset the image offset of the tile
     * @return true if tile must be solid
     */
    private boolean isTileSolid(int imageOffset) {
        for (int solidTile : solidTiles) {
            if (imageOffset == solidTile) {
                return true;
            }
        }
        return false;
    }

}




