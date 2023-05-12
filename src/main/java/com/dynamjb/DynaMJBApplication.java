package com.dynamjb;

import com.dynamjb.controller.SceneManager;
import com.dynamjb.ui.viewModel.MainViewModel;
import javafx.application.Application;
import javafx.stage.Stage;

public class DynaMJBApplication extends Application {

    private MainViewModel viewModel;

    public DynaMJBApplication() {
        // code for default constructor
    }

    public DynaMJBApplication(MainViewModel viewModel){
        this.viewModel = viewModel;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        SceneManager sceneManager = new SceneManager(primaryStage);

//        MainScene scene = new MainScene();0ho w
//        // Set the Scene on the Stage
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Labyrinth");
//r
//        // Show the Stage
//        primaryStage.show();
    }


    public static void main(String[] args) {
        MainViewModel viewModel = new MainViewModel();
        DynaMJBApplication app = new DynaMJBApplication(viewModel);
        app.launch(args);
    }

}