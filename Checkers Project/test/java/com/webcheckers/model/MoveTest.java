package com.webcheckers.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

@Tag("Model-Tier")
public class MoveTest {

    private static final int START_ROW = 0;
    private static final int START_COL = 0;
    private static final int END_ROW = 1;
    private static final int END_COL = 1;
    private static final String TEST_NAME_RED = "red";
    private static final String TEST_NAME_WHITE= "white";

    // Component Under Test
    private Move CuT;

    // Friendly Objects
    private Position testStart;
    private Position testEnd;
    private Game testGame;

    @BeforeEach
    public void setup() {
        testStart = new Position(START_ROW, START_COL);
        testEnd = new Position(END_ROW, END_COL);
        CuT = new Move(testStart, testEnd);
        testGame = new Game(new Player(TEST_NAME_RED), new Player(TEST_NAME_WHITE), 0);
    }

    @AfterEach
    public void tearDown() {
        CuT = null;
        testStart = null;
        testEnd = null;
        testGame = null;
    }

    @Test
    public void testConstructor() {
        Assertions.assertNotNull(this.testStart);
        Assertions.assertNotNull(this.testEnd);
    }

    @Test
    public void testGetEnd() {
        Position actualEnd = CuT.getEnd();
        Assertions.assertEquals(this.testEnd, actualEnd);
    }

    @Test
    public void testGetStart() {
        Position actualStart = CuT.getStart();
        Assertions.assertEquals(this.testStart, actualStart);
    }

    @Test
    public void testValidateMoveShouldThrowException(){
        UnsupportedOperationException ex = Assertions.assertThrows(UnsupportedOperationException.class,
            () -> {
               CuT.validateMove(testGame);
            });
        Assertions.assertEquals(Move.GENERIC_VALIDATION_ERROR_MESSAGE, ex.getMessage());
    }

    @Test
    public void testExecuteMoveShouldThrowException(){
        UnsupportedOperationException ex = Assertions.assertThrows(UnsupportedOperationException.class,
            () -> {
                CuT.executeMove(testGame);
            });
        Assertions.assertEquals(Move.GENERIC_EXECUTION_ERROR_MESSAGE, ex.getMessage());
    }

    @Test
    public void testEqualsWithWrongClassType() {
        Assertions.assertFalse(CuT.equals(testGame));
    }

    @Test
    public void testEqualsWithSubclass() {
        SimpleMove subClass = new SimpleMove(testStart, testEnd);
        Assertions.assertEquals(CuT, subClass);
    }


}
