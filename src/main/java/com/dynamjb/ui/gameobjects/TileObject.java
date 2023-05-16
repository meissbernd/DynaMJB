package com.dynamjb.ui.gameobjects;

import javafx.scene.paint.ImagePattern;

public class TileObject {
    static long counter = 0;
    private long id;
    private int[] animationPattern;
    private boolean animate;
    private int animationPosition = 0;

    public int[] getAnimationPattern() {
        return animationPattern;
    }
    public int getCurrentImage(){
        return animationPattern[animationPosition];
    }

    //    Offsets of ImagePatterns


    public TileObject(int[] animationPattern) {
        this.id = counter++;
        this.animationPattern = animationPattern;
        this.animate = false;
    }
    public TileObject(int imageOffset) {
        this.id = counter++;
        this.animationPattern = new int[]{imageOffset};
        this.animate = false;

    }

    public void startAnimate(){
        this.animate = true;
    }
    public void setAnimationPattern(int[] animationPattern){
        this.animationPattern = animationPattern;
    }
}

