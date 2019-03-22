package com.webcheckers.ui;
import static org.junit.jupiter.api.Assertions.*;

import com.webcheckers.model.Piece;
import com.webcheckers.ui.PieceView;
import org.junit.jupiter.api.Test;

public class PieceViewTest {
    private PieceView pieceView;

    Piece pieceWhite = new Piece(Piece.PColor.white, Piece.PType.single);
    Piece pieceRed = new Piece(Piece.PColor.red, Piece.PType.single);
    Piece pieceKing = new Piece(Piece.PColor.white, Piece.PType.king);

    PieceView pieceViewNew = new PieceView(new Piece(Piece.PColor.white, Piece.PType.single));
    PieceView pieceViewNew1 = new PieceView(new Piece(Piece.PColor.red, Piece.PType.single));
    PieceView pieceViewNewK = new PieceView(new Piece(Piece.PColor.white, Piece.PType.king));

    @Test
    public void testWhiteConstructor(){

        pieceView = new PieceView(pieceWhite);

        assertEquals(pieceViewNew, pieceView, "White piece not created correctly");
    }

    @Test
    public void testRedConstructor(){

        pieceView = new PieceView(pieceRed);

        assertEquals(pieceViewNew1, pieceView, "Red piece not created correctly");
    }

    @Test
    public void testKingConstructor(){

        pieceView = new PieceView(pieceKing);

        assertEquals(pieceViewNewK, pieceView, "King piece not created correctly");
    }

    @Test public void testGetType() {
        pieceView = new PieceView(pieceWhite);

        assertNotNull(pieceView.getType(), "Type is null");
    }

    @Test public void testGetColor() {
        pieceView = new PieceView(pieceWhite);

        assertNotNull(pieceView.getColor(), "Color is null");
    }
}
