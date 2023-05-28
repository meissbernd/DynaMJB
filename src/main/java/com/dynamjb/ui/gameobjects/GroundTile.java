package com.dynamjb.ui.gameobjects;

import com.dynamjb.controller.LabyrinthControllerImpl;

public class GroundTile extends TileObject{
    public GroundTile(int imageOffset, LabyrinthControllerImpl controller) {
        super(imageOffset, controller);
        setSolid(false);
    }

    public GroundTile(int imageOffset, LabyrinthControllerImpl controller, int x, int y) {
        super(imageOffset, controller, x, y);
        setSolid(false);
    }
}
