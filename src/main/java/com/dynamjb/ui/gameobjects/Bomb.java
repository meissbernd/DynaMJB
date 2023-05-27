package com.dynamjb.ui.gameobjects;

import com.dynamjb.controller.LabyrinthControllerImpl;

public class Bomb extends TileObject{
    public Bomb(long ownerId, int strength, int[] animationPattern, LabyrinthControllerImpl controller) {
        super(animationPattern, AnimationFinishEvent.EXPLODE, true, controller);
        this.setExplosive(true);
        this.setSolid(true);
        this.setOwnerId(ownerId);
        this.setExplosiveStrength(strength);
    }
}
