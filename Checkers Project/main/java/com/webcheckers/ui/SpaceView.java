package com.webcheckers.ui;

import com.webcheckers.model.Space;

/**
 * The class that represents the UI tier of a space
 */

public class SpaceView {
    //
    // Attributes
    //
    private PieceView pieceView;
    private int cellIdx;
    private ViewColor viewColor;

    /**
     * A SpaceView either represents a white or black space
     */
    public enum ViewColor {
        WHITE, BLACK
    }

    /**
     * Create a new SpaceView object using a model-tier representation of the
     * space.
     *
     * @param space the model-tier representation of a space
     * @param cellIndex the index of the space in the row
     */
    public SpaceView(Space space, int cellIndex) {
        this.cellIdx = cellIndex;
        this.pieceView = null;

        if (space.isBlack()) {
            this.viewColor = ViewColor.BLACK;
            if (space.doesHasPiece()) {
                this.pieceView = space.pieceInfo().getPieceView();
            }
        } else {
            this.viewColor = ViewColor.WHITE;
        }
    }

    //
    // Methods
    //
    /**
     * Gets the index of the space in its row
     * @return index of cell
     */
    public int getCellIdx() {
        return this.cellIdx;
    }

    /**
     * Determines if the space this object represents can have a piece placed on it
     * @return true if the space is black and there is no piece already on it
     */
    public boolean isValid() {
        return this.pieceView == null && this.viewColor == ViewColor.BLACK;
    }

    /**
     * Gets the UI object that represents the piece on the space this object represents
     * @return PieceView of this space's piece
     */
    public PieceView getPieceView() {
        return pieceView;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SpaceView)) return false;
        final SpaceView that = (SpaceView) o;
        return (this.pieceView == that.pieceView && this.cellIdx == that.cellIdx && this.viewColor == that.viewColor);
    }
}
