package com.webcheckers.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.mockito.Mockito;


@Tag("Model-Tier")
public class PositionTest {

    private static final int ROW = 1;
    private static final int COL = 1;
    private static final String CORRECT_STRING = "(1, 1)";

    // Component Under Test
    Position CuT;

    @Test
    public void testConstructor() {
        CuT = new Position(ROW, COL);
        Assertions.assertEquals(ROW, CuT.row);
        Assertions.assertEquals(COL, CuT.cell);
    }

    @Test
    public void testGetRow() {
        CuT = new Position(ROW, COL);
        Assertions.assertEquals(ROW, CuT.getRow());
    }

    @Test
    public void testGetCell() {
        CuT = new Position(ROW, COL);
        Assertions.assertEquals(COL, CuT.getCell());
    }

    @Test
    public void equalsFailureBadObject() {
        CuT = new Position(ROW, COL);
        Player badObj = new Player("Memes");
        Assertions.assertFalse(CuT.equals(badObj));
    }

    @Test
    public void equalsSuccess() {
        CuT = new Position(ROW, COL);
        Position other = new Position(1, 1);
        Assertions.assertTrue(CuT.equals(other));
    }

    @Test public void testToString() {
        CuT = new Position(ROW, COL);
        Assertions.assertEquals(CORRECT_STRING, CuT.toString());
    }
}
