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
    boolean animationFinishEvent = true;

    public void setSolid(boolean solid) {
        this.solid = solid;
    }

    public void setKills(boolean kills) {
        this.kills = kills;
    }

    private int[] animationPattern;

    public boolean isSolid() {
        return solid;
    }

    public boolean isKills() {
        return kills;
    }

    private boolean solid = false;
    private boolean kills = false;

    public int[] getAnimationPattern() {
        return animationPattern;
    }

    public int getCurrentImage() {
        return animationPattern[this.currentFrame];
    }

    private void createAnimationTimeline() {
        // Create the animation timeline
        this.animationTimeline = new Timeline(new KeyFrame(Duration.millis(ANIMATION_DURATION), event -> {
            this.currentFrame++;

            // Reset the current frame to 0 if it exceeds the total number of frames
            if (this.currentFrame >= animationPattern.length) {
                this.currentFrame = 0;
                // call animationFinished in controller
                if (animationFinishEvent) {
                    controller.animationFinished(this.id);
                }
            }
        }));
        this.animationTimeline.setCycleCount(Timeline.INDEFINITE); // Repeat the animation indefinitely
        this.animationTimeline.play();
    }

    public TileObject(int[] animationPattern, LabyrinthControllerImpl controller) {
        this.controller = controller;
        this.id = counter++;
        this.animationPattern = animationPattern;
        createAnimationTimeline();

    }

    public TileObject(int imageOffset, LabyrinthControllerImpl controller) {
        this.controller = controller;
        this.id = counter++;
        this.animationPattern = new int[]{imageOffset};
    }

    public TileObject(int imageOffset, boolean solid, LabyrinthControllerImpl controller) {
        this(imageOffset, controller);
        this.solid = solid;
    }

    public void startAnimate() {
        boolean animate = true;
    }

    public void setAnimationPattern(int[] animationPattern) {
        this.animationPattern = animationPattern;
    }
}