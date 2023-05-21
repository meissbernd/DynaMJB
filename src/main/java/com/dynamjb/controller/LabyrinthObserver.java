package com.dynamjb.controller;

import com.dynamjb.ui.gameobjects.Player;

import java.util.List;

public interface LabyrinthObserver {
    void onLabyrinthChanged(int[][] labyrinth);
    void onPlayerChanged(List<Player> players);
}