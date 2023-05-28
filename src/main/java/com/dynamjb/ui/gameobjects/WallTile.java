package com.dynamjb.ui.gameobjects;

import com.dynamjb.controller.LabyrinthControllerImpl;

public class WallTile extends TileObject{
    public WallTile(int imageOffset, LabyrinthControllerImpl controller) {
        super(imageOffset, controller);
        setSolid(true);
    }
    public WallTile(int imageOffset, LabyrinthControllerImpl controller, int x, int y) {
        super(imageOffset, controller, x, y);
        setSolid(true);
    }
}
