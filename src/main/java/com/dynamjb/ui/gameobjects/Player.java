package com.dynamjb.ui.gameobjects;

import com.dynamjb.ui.pane.MainPane;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;

import java.util.Objects;

import static com.dynamjb.constants.GameConstants.TILE_SIZE;

public class Player {
    private double playerHeight;
    private double playerWidth;

    private double yOffset = 0;
    private double xOffset = 0;

    private double xPosition = 0;
    private double yPosition = 0;
    private double mapStartX;
    private double mapStartY;
    private static final int ANIMATION_DURATION = 200; // milliseconds
    public double mapScale = 1;
    private double factor = 0.8;
    private ImagePattern imagePattern;
    private ImageView imageView;
    private Timeline animationTimeline;
    private int currentFrame = 0;

    static String playerPath;
    public static ImagePattern[] playerset;
    private static int[] goRight = {0, 1, 0, 2};
    private static int[] testImage = {31};
    private int[] playerMoveState = goRight;

    public Player(
            String tilePath,
            int playerWidth,
            int playerHeight,
            double mapStartX,
            double mapStartY,
            double playerStartX,
            double playerStartY) {
        playerPath = Objects.requireNonNull(MainPane.class.getResource(tilePath)).toString();
        playerset = MainPane.loadTileset(playerPath, playerWidth, playerHeight);
        this.playerWidth = (int) (playerWidth);
        this.playerHeight = (int) (playerHeight);
        this.mapStartX = mapStartX;
        this.mapStartY = mapStartY;
        this.xPosition = playerStartX;
        this.yPosition = playerStartY;
        this.xOffset = (TILE_SIZE - playerWidth * this.factor) / 2;
        this.yOffset = (TILE_SIZE - playerHeight * this.factor);

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


    // Helper method to get a specific tile from the image tiles
    private Image getImageTile(int index) {
        Image imageTiles = new Image("path/to/image/tiles.png");
        int x = (int) (index / 4 * playerWidth);
        int y = (int) (index / 4 * playerHeight);
        return new WritableImage(imageTiles.getPixelReader(), x, y, (int)playerWidth, (int) playerHeight);
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
//        imageView.setTranslateX(xPosition + (xOffset + xMapOffset + tileWidth * 0.5)  * scale); // Set the x position to 50
//        imageView.setTranslateY(yPosition + (yOffset + yMapOffset + tileHeight * 0.5) * scale); // Set the y position to 50

        double mapPositionX =
                mapStartX * mapScale
                        + this.xPosition * TILE_SIZE * mapScale
                        + this.xOffset * mapScale
//                + TILE_SIZE * mapScale
//                 + playerWidth / 2 * mapScale
//                + this.xOffset * FACTOR * mapScale
                ;


//
//        double ydiff = (mapStartY + this.yPosition * TILE_SIZE);
//        ydiff =
//                this.yOffset * factor;

        double mapPositionY =
                mapStartY * this.mapScale
                        + this.yPosition * TILE_SIZE * this.mapScale
                        + (this.yOffset * mapScale)

//                + TILE_SIZE * mapScale

//                + playerHeight / 2 * mapScale
//                + this.yOffset * FACTOR * mapScale
                ;
        imageView.setTranslateX(mapPositionX);
        imageView.setTranslateY(mapPositionY);
    }

    public void updateDimensions(double mapScale) {
//        calcScale(mapScale);
        this.mapScale = mapScale;
        this.setPlayerSize();
        this.setPlayerPosition();
    }
}

enum PlayerMoveState {
    RIGHT, LEFT, UP, DOWN
}