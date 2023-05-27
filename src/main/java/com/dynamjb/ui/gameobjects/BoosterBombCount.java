package com.dynamjb.ui.gameobjects;

import com.dynamjb.controller.LabyrinthControllerImpl;

public class BoosterBombCount extends Booster{
    public BoosterBombCount(int[] animationPattern, LabyrinthControllerImpl controller) {
        super(1, 0, 0, 0,false, false, animationPattern, controller);
    }
}
