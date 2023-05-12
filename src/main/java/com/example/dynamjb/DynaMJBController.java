package com.example.dynamjb;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DynaMJBController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

}