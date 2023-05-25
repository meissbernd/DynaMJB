package com.dynamjb.ui.gameobjects;

import com.dynamjb.controller.LabyrinthControllerImpl;

public class BoosterPhasing extends Booster{

    public BoosterPhasing( int[] animationPattern, LabyrinthControllerImpl controller) {
        super(0, 0, 0,0, false, true, animationPattern, controller);
    }
}
