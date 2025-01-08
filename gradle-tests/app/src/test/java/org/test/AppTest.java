package org.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import com.lab.*;

class ClassTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCapture = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCapture));
    }

    @Test
    public void testOutput() {
        Class object = new Class();
        object.printSomething();
        assertEquals(outputStreamCapture.toString().trim(), "expected output");
    }

    @Test
    public void testEquality() {
        Class object = new Class();
        assertEquals(object.getValue(), "something");
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}
