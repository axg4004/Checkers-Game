package com.webcheckers.model;

public class Position {

    private static final int BOARD_DIM = 7;

    int row;
    int cell;

    public Position(int row, int cell) {
        this.row = row;
        this.cell = cell;
    }

    public int getCell() {
        return cell;
    }

    public int getRow() {
        return row;
    }

    public boolean outOfBounds() {
        return row < 0 || row > BOARD_DIM || cell < 0 || cell > BOARD_DIM;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Position)) {
            return false;
        } else {
            Position pos = (Position)obj;
            return this.getRow() == pos.getRow() && this.getCell() == pos.getCell();
        }
    }
    @Override
    public String toString() {
        return String.format("(%d, %d)", row, cell);
    }
}
