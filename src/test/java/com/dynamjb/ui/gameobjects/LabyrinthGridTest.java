package com.dynamjb.ui.gameobjects;

import com.dynamjb.model.ExampleClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.ItemEvent;

import static org.junit.jupiter.api.Assertions.*;

class LabyrinthGridTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    LabyrinthGrid testGrid;

    @Test
    void testGridDimensions() {
        testGrid = new LabyrinthGrid();
        int[][] testArray = testGrid.grid;
        int x_expected = 13;
        int y_expected = 15;
        Assertions.assertEquals(x_expected, testArray.length);
        Assertions.assertEquals(y_expected, testArray[0].length);
    }
}
