package com.dynamjb.ui.gameobjects;

import com.dynamjb.controller.LabyrinthControllerImpl;

import java.util.ArrayList;
import java.util.List;

public class LabyrinthStacked {

    public List<TileObject>[][] tilesOfLabyrinth;

    private int[] solidTiles = {44};

public LabyrinthStacked(int[][] labyrinth, int[] solidTiles, LabyrinthControllerImpl controller){
        int rows = labyrinth.length;
        int columns = labyrinth[0].length;

        List<TileObject>[][] stackedLabyrinth = new List[rows][columns];
        this.tilesOfLabyrinth = stackedLabyrinth;

        // Initialize each element of the array with an empty list
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                stackedLabyrinth[i][j] = new ArrayList<>();

                // TODO get more than one Image for Animation
                int imageOffset = labyrinth[i][j];

                if (isTileSolid(imageOffset)) {
                    stackedLabyrinth[i][j].add(new WallTile(imageOffset, controller));
                } else {
                    stackedLabyrinth[i][j].add(new GroundTile(imageOffset, controller));
                }
            }
        }
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




