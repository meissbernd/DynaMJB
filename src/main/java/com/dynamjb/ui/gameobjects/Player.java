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

public class Player {
    private int tileHeight;
    private int tileWidth;
    private int yOffset = -28;
    private int xOffset = -11;
    //    private int yOffset = 0;
//    private int xOffset = 0;
    private double xPosition = 0;
    private double yPosition = 0;
    private double xMapOffset;
    private double yMapOffset;
    private static final int ANIMATION_DURATION = 200; // milliseconds
    public static double scale = 1;
    private static double FACTOR = 0.8;
    private ImagePattern imagePattern;
    private ImageView imageView;
    private Timeline animationTimeline;
    private int currentFrame = 0;

    static String playerPath;
    public static ImagePattern[] playerset;
    private static int[] goRight = {0, 1, 0, 2};
    private int[] playerMoveState = goRight;

    public Player(
            String tilePath,
            int tileWidth,
            int tileHeight,
            double mapStartX,
            double mapStartY,
            double startX,
            double startY) {
        playerPath = Objects.requireNonNull(MainPane.class.getResource(tilePath)).toString();
        playerset = MainPane.loadTileset(playerPath, tileWidth, tileHeight);
        this.tileWidth = (int) (tileWidth * this.scale);
        this.tileHeight = (int) (tileHeight * this.scale);
        this.xMapOffset = mapStartX;
        this.yMapOffset = mapStartY;
        this.xPosition = startX;
        this.yPosition = startY;

        // Initialize the image pattern with the first frame
//        this.imagePattern = new ImagePattern(getImageTile(0));

        // Create the image view with the initial image pattern
        this.imageView = new ImageView();
        this.imageView.setImage(playerset[0].getImage());
        setPlayerPosition(this.scale);
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
            this.setPlayerPosition(scale);
//            this.imageView.setImage(getImageTile(this.currentFrame));
        }));
        this.animationTimeline.setCycleCount(Timeline.INDEFINITE); // Repeat the animation indefinitely
        startAnimation();
    }


    // Helper method to get a specific tile from the image tiles
    private Image getImageTile(int index) {
        Image imageTiles = new Image("path/to/image/tiles.png");
        int x = index % 4 * tileWidth;
        int y = index / 4 * tileHeight;
        return new WritableImage(imageTiles.getPixelReader(), x, y, tileWidth, tileHeight);
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
        this.scale = scale * FACTOR;

    }

    public void setPlayerSize() {
        this.imageView.setFitWidth(this.tileWidth * this.scale);
        this.imageView.setFitHeight(this.tileHeight * this.scale);
    }

    // Start the player's animation
    public void setPlayerOffset(double mapStartX, double mapStartY, double scale) {
        this.xMapOffset = mapStartX;
        this.yMapOffset = mapStartY;
    }

    public void setPlayerPosition(double mapScale) {
//        imageView.setTranslateX(xPosition + (xOffset + xMapOffset + tileWidth * 0.5)  * scale); // Set the x position to 50
//        imageView.setTranslateY(yPosition + (yOffset + yMapOffset + tileHeight * 0.5) * scale); // Set the y position to 50

        double mapPositionX = xMapOffset * mapScale
                + tileWidth / 2 * mapScale
                + this.xOffset * FACTOR * mapScale;

        double mapPositionY = yMapOffset * mapScale
                + tileHeight / 2 * mapScale
                + this.yOffset * FACTOR * mapScale;
        imageView.setTranslateX(mapPositionX);
        imageView.setTranslateY(mapPositionY);
    }

    public void updateDimensions(double mapScale) {
        calcScale(mapScale);
        this.setPlayerSize();
        this.setPlayerPosition(mapScale);
    }

}

enum PlayerMoveState {
    RIGHT, LEFT, UP, DOWN
}