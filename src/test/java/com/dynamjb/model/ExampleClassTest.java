package com.dynamjb.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class ExampleClassTest {
    ExampleClass testObject;

    @Test
    void doSomething() {
        testObject = new ExampleClass();
        String result = testObject.doSomething();
        Assertions.assertEquals("Halloo", result);

    }
}