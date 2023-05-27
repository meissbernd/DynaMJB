package com.dynamjb.ui.gameobjects;

import com.dynamjb.controller.LabyrinthControllerImpl;

public class BoosterLife extends Booster{
    public BoosterLife(int[] animationPattern, LabyrinthControllerImpl controller) {
        super(0, 0, 0, 1,false, false, animationPattern, controller);
    }
}
