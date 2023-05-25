package com.dynamjb.ui.gameobjects;

import com.dynamjb.controller.LabyrinthControllerImpl;

public class Booster extends TileObject{
    private int bombCount;
    private int bombStrength;
    private double speed;
    private int life;
    private boolean invincible;
    private boolean phasing;

    public Booster(int bombCount, int bombStrength, double speed, int life, boolean invincible, boolean phasing, int[] animationPattern, LabyrinthControllerImpl controller) {
        super(animationPattern, controller);
        this.bombCount = bombCount;
        this.bombStrength = bombStrength;
        this.speed = speed;
        this.life = life;
        this.invincible = invincible;
        this.phasing = phasing;
    }

    public int getBombCount() {
        return bombCount;
    }

    public int getBombStrengthAdd() {
        return bombStrength;
    }

    public double getSpeed() {
        return speed;
    }


    public int getBombStrength() {
        return bombStrength;
    }

    public int getLifes() {
        return life;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public boolean isPhasing() {
        return phasing;
    }
}
