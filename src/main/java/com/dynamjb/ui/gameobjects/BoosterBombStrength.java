package com.dynamjb.ui.gameobjects;

import com.dynamjb.controller.LabyrinthControllerImpl;

public class BoosterBombStrength extends Booster{
    public BoosterBombStrength(int[] animationPattern, LabyrinthControllerImpl controller) {
        super(0, 1, 0, 0, false, false, animationPattern, controller);
    }

}
