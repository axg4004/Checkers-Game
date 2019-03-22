package com.webcheckers.ui;

import com.webcheckers.model.Piece;

public class PieceView {
    private Color color;
    private Type type;

    /**
     * Piece is either red or white
     */
    public enum Color {
        RED, WHITE;
    }

    /**
     * Piece is either a single piece or a king
     */
    public enum Type {
        SINGLE, KING;
    }

    /**
     * Creates a new PieceView
     * @param color color of the piece, red or white
     * @param type type of the piece, single or king
     */
    public PieceView(Color color, Type type) {
        this.color = color;
        this.type = type;
    }

    /**
     * Create a new PieceView object using a model-tier representation of the
     * piece
     *
     * @param piece the model-tier representation of the piece
     */
    public PieceView(Piece piece){
        if (piece.pieceColor == Piece.PColor.red) {
            this.color = Color.RED;
        } else {
            this.color = Color.WHITE;
        }

        if (piece.pieceType == Piece.PType.king) {
            this.type = Type.KING;
        } else {
            this.type = Type.SINGLE;
        }
    }

    /**
     * Gets this piece's type
     * @return type of piece
     */
    public Type getType() {
        return type;
    }

    /**
     * get the color of this piece
     * @return
     */
    public Color getColor() {
        return this.color;
    }

    @Override
    public boolean equals(Object o){
        if (o == this) return true;
        if (!(o instanceof PieceView)) return false;
        final PieceView that = (PieceView) o;
        return (this.color == that.color && this.type == that.type);


    }
}
