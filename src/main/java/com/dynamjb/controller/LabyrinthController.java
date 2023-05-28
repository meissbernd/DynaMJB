package com.dynamjb.controller;

import com.dynamjb.ui.gameobjects.TileObject;
import javafx.scene.paint.ImagePattern;

import java.util.List;

public interface LabyrinthController {

//    int[][] getLabyrinth();
    List<TileObject>[][] getStackedLabyrinthTiles();

    ImagePattern[] getPlayerSet();
    ImagePattern[] getLabyrinthSet();
}
