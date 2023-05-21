package com.dynamjb.ui.gameobjects;
import com.dynamjb.ui.viewModel.MainViewModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;

import static com.dynamjb.constants.GameConstants.TILE_SIZE;

public class Player extends Node {
    private final double playerHeight;
    private final double playerWidth;

    private double yOffset;
    private double xOffset;

    private double xPosition;
    private double yPosition;
    private double mapStartX;
    private double mapStartY;
    private static final int ANIMATION_DURATION = 200; // milliseconds
    public double mapScale = 1;
    private final double factor = 0.8;
    private ImageView imageView;
    private Timeline animationTimeline;
    private int currentFrame = 0;
    private MainViewModel viewModel;

    public static ImagePattern[] playerset;
    private static final int[] goRight = {0,1,0,2};
    private static final int[] testImage = {31};
    private int[] playerMoveState = goRight;

    public Player(
            String tilePath,
            int width,
            int height,
            double xPos,
            double yPos,
            MainViewModel viewModel
    ) {
        this(
                tilePath,
                width,
                height,
                xPos,
                yPos,
                viewModel,
                1 * TILE_SIZE,
                1 * TILE_SIZE);
    }

    public Player(
            String tilePath,
            int width,
            int height,
            double xPos,
            double yPos,
            MainViewModel viewModel,
            double mapStartX,
            double mapStartY
    ) {

        this.playerWidth = width;
        this.playerHeight = height;
        this.mapStartX = mapStartX;
        this.mapStartY = mapStartY;
        this.xPosition = xPos;
        this.yPosition = yPos;
        this.xOffset = (TILE_SIZE - width * this.factor) / 2;
        this.yOffset = (TILE_SIZE - height * this.factor);
        this.viewModel = viewModel;
        playerset = this.viewModel.getPlayerSet();
        // Create the image view with the initial image pattern
        this.imageView = new ImageView();
        this.imageView.setImage(playerset[0].getImage());
        setPlayerPosition();
        setPlayerSize();


        // Create the animation timeline to change the player's image over time
        this.animationTimeline = new Timeline(new KeyFrame(Duration.millis(ANIMATION_DURATION), event -> {
            // Increment the current frame
            this.currentFrame++;

            // Reset the current frame to 0 if it exceeds the total number of frames
            if (this.currentFrame >= playerMoveState.length) {
                this.currentFrame = 0;
            }

            // Update the image pattern with the current frame
//            this.imagePattern = new ImagePattern(getImageTile(this.currentFrame));
            this.imageView.setImage(playerset[playerMoveState[this.currentFrame]].getImage());
            this.setPlayerPosition();
//            this.imageView.setImage(getImageTile(this.currentFrame));
        }));
        this.animationTimeline.setCycleCount(Timeline.INDEFINITE); // Repeat the animation indefinitely
        startAnimation();
    }

    public double getXPosition(){
        return this.xPosition;
    }

    public double getYPosition(){
        return this.yPosition;
    }

    public void setXPosition(double xPosition){
        this.xPosition = xPosition;
    }

    public void setYPosition(double yPosition){
        this.yPosition = yPosition;
    }


    // Helper method to get a specific tile from the image tiles
    private Image getImageTile(int index) {
        Image imageTiles = new Image("path/to/image/tiles.png");
        int x = (int) (index / 4 * playerWidth);
        int y = (int) (index / 4 * playerHeight);
        return new WritableImage(imageTiles.getPixelReader(), x, y, (int) playerWidth, (int) playerHeight);
    }

    // Start the player's animation
    public void startAnimation() {
        this.animationTimeline.play();
    }

    // Stop the player's animation
    public void stopAnimation() {
        this.animationTimeline.stop();
    }

    // Get the image view for the player
    public ImageView getImageView() {
        return this.imageView;
    }

    public void calcScale(double scale) {
        this.mapScale = scale * factor;

    }

    public void setPlayerSize() {
        this.imageView.setFitWidth(this.playerWidth * this.mapScale * factor);
        this.imageView.setFitHeight(this.playerHeight * this.mapScale * factor);
    }

    // Start the player's animation
    public void setPlayerOffset(double mapStartX, double mapStartY, double scale) {
        this.mapStartX = mapStartX;
        this.mapStartY = mapStartY;
    }

    public void setPlayerPosition() {
        double mapPositionX =
                mapStartX * mapScale
                        + this.xPosition * TILE_SIZE * mapScale
                        + this.xOffset * mapScale;
        double mapPositionY =
                mapStartY * this.mapScale
                        + this.yPosition * TILE_SIZE * this.mapScale
                        + (this.yOffset * mapScale);
        imageView.setTranslateX(mapPositionX);
        imageView.setTranslateY(mapPositionY);
    }

    public void updateDimensions(double mapScale) {
        this.mapScale = mapScale;
        this.setPlayerSize();
        this.setPlayerPosition();
    }
}

enum PlayerMoveState {
    RIGHT, LEFT, UP, DOWN
}