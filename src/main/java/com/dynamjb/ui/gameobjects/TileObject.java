package com.dynamjb.ui.gameobjects;


public class TileObject {
    static long counter = 0;
    private final long id;
    private int[] animationPattern;
    boolean solid = false;
    boolean kills = false;

    public int[] getAnimationPattern() {
        return animationPattern;
    }

    public int getCurrentImage() {
        int animationPosition = 0;
        return animationPattern[animationPosition];
    }

    //    Offsets of ImagePatterns


    public TileObject(int[] animationPattern) {
        this.id = counter++;
        this.animationPattern = animationPattern;
    }

    public TileObject(int imageOffset) {
        this.id = counter++;
        this.animationPattern = new int[]{imageOffset};
    }

    public void startAnimate() {
        boolean animate = true;
    }

    public void setAnimationPattern(int[] animationPattern) {
        this.animationPattern = animationPattern;
    }
}

