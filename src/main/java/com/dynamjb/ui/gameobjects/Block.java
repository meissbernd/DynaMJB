package com.dynamjb.ui.gameobjects;

import com.dynamjb.controller.LabyrinthControllerImpl;

public class Block extends TileObject{
    public Block(int[] animationPattern, LabyrinthControllerImpl controller) {
        super(animationPattern, false, controller);
        setAnimationFinishEvent(AnimationFinishEvent.DESTROY);
        setSolid(true);
        setDestructible(true);
    }
}
