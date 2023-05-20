package com.dynamjb;

import com.dynamjb.controller.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class DynaMJBApplication extends Application {
    public DynaMJBApplication(){
    }


    @Override
    public void start(Stage primaryStage) {
        SceneManager sceneManager = new SceneManager(primaryStage);

    }


    public static void main(String[] args) {
//        DynaMJBApplication app = new DynaMJBApplication();
        launch(args);
    }

}