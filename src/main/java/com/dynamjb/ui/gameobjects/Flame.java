package com.dynamjb.ui.gameobjects;

import com.dynamjb.controller.LabyrinthControllerImpl;

public class Flame extends TileObject{
    public Flame(long playerId, int[] animationPattern, LabyrinthControllerImpl controller) {
        super(animationPattern, true, controller);
        setAnimationFinishEvent(AnimationFinishEvent.DESTROY);
        setOwnerId(playerId);
    }
}
