package com.dynamjb.controller;

import com.dynamjb.ui.pane.MainPane;
import com.dynamjb.ui.viewModel.MainViewModel;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.Objects;

import static com.dynamjb.constants.GameConstants.*;
public class SceneManager {
    private final Stage stage;
    private final MainViewModel mainViewModel;
    private final LabyrinthControllerImpl labyrinthController;

    Scene scene;

    public SceneManager(Stage stage) {
        this.stage = stage;
        this.labyrinthController = new LabyrinthControllerImpl();
        this.mainViewModel = new MainViewModel(this.labyrinthController); // Create the ViewModel instance

        // Start Scene
        setScene(SceneType.MAIN);
    }


    public void setScene(SceneType sceneType) {

        switch (sceneType) {
            case WELCOME:
                scene = null;
                break;
            case MAIN:
                scene = getMainScene();
        }
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Returns the main Scene for the application.
     * contains a BorderPane layout with AnchorPane regions and a MainPane.
     * sets up the background image, labels, ...
     * styled with CSS using the "styles.css" file.
     * @return The main Scene of the application.
     */
    protected Scene getMainScene() {

        // Create a new Image object with the path to image file
        String backgroundPath = Objects.requireNonNull(getClass().getResource(BACKGROUND_IMAGE)).toString();
        Image backgroundImage = new Image(backgroundPath);

        MainPane mainPane = new MainPane(this.mainViewModel);
        mainPane.setId("mainPane");

        BorderPane root = new BorderPane();
        root.setPrefWidth(SCENE_WIDTH);
        root.setPrefHeight(SCENE_HEIGHT);

        root.setBackground(new Background(new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, false, false))));

        AnchorPane anchorPaneTop = new AnchorPane();
        anchorPaneTop.setId("anchorPaneTop");
        AnchorPane anchorPaneRight = new AnchorPane();
        anchorPaneRight.setId("anchorPaneRight");
        AnchorPane anchorPaneBottom = new AnchorPane();
        anchorPaneBottom.setId("anchorPaneBottom");
        AnchorPane anchorPaneLeft = new AnchorPane();
        anchorPaneLeft.setId("anchorPaneLeft");

        Label timeLabel = new Label("02:33");
        timeLabel.setId("time-label");

        anchorPaneRight.getChildren().add(timeLabel);

        Label player1Label = new Label("Player 1");
        player1Label.setId("player1-label");
        anchorPaneTop.getChildren().add(player1Label);
        AnchorPane.setTopAnchor(player1Label, 20.0);
        AnchorPane.setRightAnchor(player1Label, 10.0);

        Label player2Label = new Label("Player 2");
        player2Label.setId("player2-label");
        anchorPaneBottom.getChildren().add(player2Label);
        AnchorPane.setBottomAnchor(player2Label, 20.0);
        AnchorPane.setRightAnchor(player2Label, 10.0);
        player2Label.setPrefWidth(100.0);
        player2Label.setPrefHeight(20);

        anchorPaneTop.getChildren().add(addFramesCounter());

        root.setTop(anchorPaneTop);
        root.setRight(anchorPaneRight);
        root.setBottom(anchorPaneBottom);
        root.setLeft(anchorPaneLeft);
        root.setCenter(mainPane);

        // Bind root height to MainViewModel rootHeight property
        mainViewModel.rootHeightProperty().bind(root.heightProperty());

        Scene scene = new Scene(root, Color.BLACK);
        String cssPath = Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm();
        scene.getStylesheets().add(cssPath);
        scene.setUserData(mainPane); // Store a reference to the MainPane instance in the Scene's user data

        return scene;
    }

    private Label  addFramesCounter() {
        Label fpsLabel = new Label("FPS: 0");
        fpsLabel.setId("fps-label");
        // Create an AnimationTimer to update the FPS label
        AnimationTimer fpsTimer = new AnimationTimer() {
            private long lastTime = 0;
            private int frameCount = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }
                frameCount++;

                long elapsedNanos = now - lastTime;
                if (elapsedNanos >= 1e9) {
                    double fps = frameCount / (elapsedNanos / 1e9);
                    fpsLabel.setText(String.format("FPS: %.2f", fps));
                    lastTime = now;
                    frameCount = 0;
                }
            }
        };
        fpsTimer.start();
        return fpsLabel;
    }
}

enum SceneType {
    WELCOME, MAIN, HIGHSCORE
}

