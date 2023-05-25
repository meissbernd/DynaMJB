package com.dynamjb.ui.gameobjects;

import com.dynamjb.controller.LabyrinthControllerImpl;

public class BoosterSpeed extends Booster {
    public BoosterSpeed(int[] animationPattern, LabyrinthControllerImpl controller) {
        super(0, 0, 1.0, 0,false, false, animationPattern, controller);
    }

    public BoosterSpeed(double speed, int[] animationPattern, LabyrinthControllerImpl controller) {
        super(0, 0, speed, 0,false,false, animationPattern, controller);
    }
}
