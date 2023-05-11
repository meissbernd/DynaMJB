package com.dyna.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DynaController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to DynaMJB the JavaFX Application!");
    }
}