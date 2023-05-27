package com.dynamjb.ui.gameobjects;

import com.dynamjb.controller.LabyrinthControllerImpl;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;

import static com.dynamjb.constants.GameConstants.PLAYER_SPEED;
import static com.dynamjb.constants.GameConstants.TILE_SIZE;

public class Player extends Node {
    static long counter = 0;
    private final long id;
    private final double playerHeight;
    private final double playerWidth;
    private double yOffset;
    private double xOffset;
    private double xPosition;
    private double yPosition;
    private static final int ANIMATION_DURATION = 200; // milliseconds
    private static final int POSITION_UPDATE_DURATION = 16; // milliseconds
    public double mapScale = 1;
    private final double magnificationFactor = 0.8;
    private ImageView imageView;
    private Timeline animationTimeline;
    private Timeline positionTimeline;
    private double velocityX;
    private double velocityY;
    private int currentFrame = 0;
    private LabyrinthControllerImpl controller;
    public static ImagePattern[] playerset;
    public static final int[] GO_RIGHT = {0, 1, 0, 2};
    public static final int[] GO_UP = {16, 17, 16, 18};
    public static final int[] GO_DOWN = {8, 9, 8, 9};
    public static final int[] STOP_RIGHT = {0};
    public static final int[] STOP_UP = {16};
    public static final int[] STOP_DOWN = {8};
    public static final int[] STOP_LEFT = {63};
    public static final int[] GO_LEFT = {63, 62, 63, 61};
    private int[] playerMoveState = STOP_DOWN;
    private int bombCount = 1;
    private int bombStrength = 1;
    private double speed = PLAYER_SPEED;
    private int life = 3;
    private boolean invincible = false;
    private boolean phasing = false;

    public Player(int width, int height, double xPos, double yPos, LabyrinthControllerImpl controller
    ) {
        this.id = counter++;
        this.playerWidth = width;
        this.playerHeight = height;
        this.xPosition = xPos;
        this.yPosition = yPos;
        this.controller = controller;
        this.xOffset = (TILE_SIZE - width * this.magnificationFactor) / 2;
        this.yOffset = (TILE_SIZE - height * this.magnificationFactor);
        playerset = this.controller.getPlayerSet();
        // Create the image view with the initial image pattern
        this.imageView = new ImageView();
        this.imageView.setImage(playerset[0].getImage());
        setPlayerPosition();
        setPlayerSize();

        // Create the animation timeline to change the player's image over time
        this.animationTimeline = new Timeline(new KeyFrame(Duration.millis(ANIMATION_DURATION), event -> {
            this.currentFrame++;

            // Reset the current frame to 0 if it exceeds the total number of frames
            if (this.currentFrame >= playerMoveState.length) {
                this.currentFrame = 0;
            }

            // Update the image pattern with the current frame
            this.imageView.setImage(playerset[playerMoveState[this.currentFrame]].getImage());
//            this.setPlayerPosition();
        }));
        this.animationTimeline.setCycleCount(Timeline.INDEFINITE); // Repeat the animation indefinitely

        // Create the animation timeline to change the player's image over time
        this.positionTimeline = new Timeline(new KeyFrame(Duration.millis(POSITION_UPDATE_DURATION), event -> {
            this.setPlayerPosition();
        }));
        this.positionTimeline.setCycleCount(Timeline.INDEFINITE); // Repeat the animation indefinitely

        startAnimationTimeLine();
        startPositionUpdateTimeLine();
    }

    // Helper method to get a specific tile from the image tiles
    private Image getImageTile(int index) {
        Image imageTiles = new Image("path/to/image/tiles.png");
        int x = (int) (index / 4 * playerWidth);
        int y = (int) (index / 4 * playerHeight);
        return new WritableImage(imageTiles.getPixelReader(), x, y, (int) playerWidth, (int) playerHeight);
    }

    // Start the player's animation
    public void startAnimationTimeLine() {
        this.animationTimeline.play();
    }
    public void startPositionUpdateTimeLine() {
        this.positionTimeline.play();
    }


    // Stop the player's animation
    public void stopAnimation() {
        this.animationTimeline.stop();
    }

    // Get the image view for the player
    public ImageView getImageView() {
        return this.imageView;
    }

    public void setPlayerSize() {
        this.imageView.setFitWidth(this.playerWidth * this.mapScale * magnificationFactor);
        this.imageView.setFitHeight(this.playerHeight * this.mapScale * magnificationFactor);
    }

    public void setPlayerPosition() {
        double mapPositionX =
                this.controller.getMapStartX() * mapScale
                        + this.xPosition * TILE_SIZE * mapScale
                        + this.xOffset * mapScale;
        double mapPositionY =
                this.controller.getMapStartY() * this.mapScale
                        + this.yPosition * TILE_SIZE * this.mapScale
                        + (this.yOffset * mapScale);
        imageView.setTranslateX(mapPositionX);
        imageView.setTranslateY(mapPositionY);
    }

    public void updateDimensions(double mapScale) {
        this.mapScale = mapScale;
        setPlayerSize();
        setPlayerPosition();
    }

    public double getXPosition() {
        return xPosition;
    }

    public void setXPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    public double getYPosition() {
        return yPosition;
    }

    public void setYPosition(double yPosition) {
        this.yPosition = yPosition;
    }

    public long getPlayerId() {
        return id;
    }

    public int[] getPlayerMoveState() {
        return playerMoveState;
    }

    public void setPlayerMoveState(int[] playerMoveState) {
        this.playerMoveState = playerMoveState;
    }
    public int getBombCount() {
        return bombCount;
    }

    public void setBombCount(int bombCount) {
        this.bombCount = bombCount;
    }

    public int getBombStrength() {
        return bombStrength;
    }

    public void setBombStrength(int bombStrength) {
        this.bombStrength = bombStrength;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public boolean isPhasing() {
        return phasing;
    }

    public void setPhasing(boolean phasing) {
        this.phasing = phasing;
    }
    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX * this.speed;
    }
    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY * this.speed;
    }
}

