package com.dynamjb.ui.gameobjects;

import com.dynamjb.DynaMJBApplication;
import com.dynamjb.controller.LabyrinthControllerImpl;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.logging.Logger;


public class TileObject {
    public static final Logger logger = Logger.getLogger(DynaMJBApplication.class.getName());
    LabyrinthControllerImpl controller;
    static long counter = 0;
    private final long id;
    private Timeline animationTimeline;
    private static final int ANIMATION_DURATION = 200; // milliseconds
    private int currentFrame = 0;

    private int[] animationPattern;
    private boolean solid = false;
    private AnimationFinishEvent animationFinishEvent = AnimationFinishEvent.DEFAULT;
    public long getId() {
        return id;
    }
    public void setSolid(boolean solid) {
        this.solid = solid;
    }
    public boolean isSolid() {
        return solid;
    }
    public enum AnimationFinishEvent {
        NONE, DESTROY, EXPLODE;
        public static final AnimationFinishEvent DEFAULT = NONE;
    }
    public int getCurrentImage() {
        return animationPattern[this.currentFrame];
    }
    protected void createAnimationTimeline() {
        this.animationTimeline = new Timeline(new KeyFrame(Duration.millis(ANIMATION_DURATION), event -> {
            this.currentFrame++;

            // Reset the current frame to 0 if it exceeds the total number of frames
            if (this.currentFrame >= animationPattern.length) {
                this.currentFrame = 0;
                // call animationFinished in controller
                if (animationFinishEvent != AnimationFinishEvent.NONE) {
                    controller.animationFinished(this.id, animationFinishEvent);
                }
            }
        }));
        this.animationTimeline.setCycleCount(Timeline.INDEFINITE); // Repeat the animation indefinitely
    }





    private long ownerId = -1;
    private boolean kills = false;
    private boolean destructible = false;
    private boolean explosive = false;
    private int explosiveStrength = 1;

    public AnimationFinishEvent getAnimationFinishEvent() {
        return animationFinishEvent;
    }

    public void setAnimationFinishEvent(AnimationFinishEvent animationFinishEvent) {
        this.animationFinishEvent = animationFinishEvent;
    }


    public void setKills(boolean kills) {
        this.kills = kills;
    }



    public boolean isKills() {
        return kills;
    }

    public int[] getAnimationPattern() {
        return animationPattern;
    }





    public TileObject(
            int[] animationPattern,
            LabyrinthControllerImpl controller
    ) {
        this(animationPattern, AnimationFinishEvent.NONE, true, controller);
    }

    public TileObject(
            int[] animationPattern,
            boolean startAnimation,
            LabyrinthControllerImpl controller
    ) {
        this(animationPattern, AnimationFinishEvent.NONE, startAnimation, controller);
    }

    public TileObject(
            int[] animationPattern,
            AnimationFinishEvent animationFinishEvent,
            boolean startAnimation,
            LabyrinthControllerImpl controller
    ) {
        this.controller = controller;
        this.id = counter++;
        this.animationPattern = animationPattern;
        this.animationFinishEvent = animationFinishEvent;
        createAnimationTimeline();
        if (startAnimation) {
            this.animationTimeline.play();
        }
    }


    public TileObject(
            int imageOffset,
            LabyrinthControllerImpl controller
    ) {
        this.controller = controller;
        this.id = counter++;
        this.animationFinishEvent = AnimationFinishEvent.NONE;
        this.animationPattern = new int[]{imageOffset};
    }

    public TileObject(
            int imageOffset,
            boolean solid,
            LabyrinthControllerImpl controller
    ) {
        this(imageOffset, controller);
        this.solid = solid;
    }

    public void startAnimate() {
        this.animationTimeline.play();

    }

    public void setAnimationPattern(int[] animationPattern) {
        this.animationPattern = animationPattern;
    }

    public boolean isDestructible() {
        return destructible;
    }

    public void setDestructible(boolean destructible) {
        this.destructible = destructible;
    }

    public boolean isExplosive() {
        return explosive;
    }

    public void setExplosive(boolean explosive) {
        this.explosive = explosive;
    }

    public int getExplosiveStrength() {
        return explosiveStrength;
    }

    public void setExplosiveStrength(int explosiveStrength) {
        this.explosiveStrength = explosiveStrength;
    }


    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }
}