package com.webcheckers.model;
import static org.junit.jupiter.api.Assertions.*;

import com.webcheckers.model.Piece;
import com.webcheckers.model.Space;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class SpaceTest {

    Piece piece = new Piece(Piece.PColor.white, Piece.PType.single);
    Space spacewh = new Space(Space.SpColor.white);
    Space spacebl = new Space(Space.SpColor.black);

    @Test
    public void testConstructorWhite() {
        assertFalse(spacewh.isValid(), "Space shouldn't be valid");
        assertFalse(spacewh.isBlack(), "space should be white");
    }

    @Test
    public void testConstructorBlack() {
        assertTrue(spacebl.isValid(), "Space should be valid");
        assertTrue(spacebl.isBlack(), "space should be black");
    }

    @Test
    public void testisValid1(){
        assertTrue(spacebl.isValid(),"Space should be valid");
    }

    @Test
    public void testisValid2(){
        spacebl.addPiece(piece);
        assertFalse(spacebl.isValid(),"Space should be invalid");
    }

    @Test
    public void testaddpiece(){
        spacebl.addPiece(piece);
        assertTrue(spacebl.doesHasPiece(), "Space should have piece");
    }

    @Test
    public void testremovepiece(){
        spacebl.addPiece(piece);
        spacebl.removePiece();
        assertFalse(spacebl.doesHasPiece(), "Space should not have piece");
    }

}
