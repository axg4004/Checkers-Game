package com.webcheckers.model;


import com.webcheckers.model.Piece.PColor;
import com.webcheckers.model.Piece.PType;
import java.util.ArrayList;

/**
 * Object that holds all of the game data for the state of the board
 */

public class Board {
    //
    // Attributes
    //
    /** Number of ROWS and COLUMNS*/
    public static final int ROWS = 8;
    public static final int COLUMNS = 8;

    /** used for alternating colors(space) on the board*/
    private boolean darkSpace = true;

    /** 2D Array of spaces*/
    Space[][] boardArray;

    //
    // Constructor
    //
    public Board() {
        boardArray = initBoard();

        //go back through all of the spaces and put pieces where they belong
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (i <= 2) {
                    if (boardArray[i][j].isBlack()) {
                        boardArray[i][j].addPiece(new Piece(Piece.PColor.white, Piece.PType.single));
                    }
                } else if(i >= 5) {
                    if (boardArray[i][j].isBlack()) {
                        boardArray[i][j].addPiece(new Piece(Piece.PColor.red, Piece.PType.single));
                    }
                }
            }
        }
    }

    /** Constructor used for testing*/
    public Board(ArrayList<Position> redSpaces, ArrayList<Position> whiteSpaces) {
        initBoard();
        //start by creating all of the spaces on the board as empty spaces
        for(Position space : redSpaces) {
            boardArray[space.getRow()][space.getCell()].addPiece(new Piece(PColor.red, PType.single));
        }
        for(Position space : whiteSpaces) {
            boardArray[space.getRow()][space.getCell()].addPiece(new Piece(PColor.white, PType.single));
        }
    }

    private Space[][] initBoard() {
        boardArray = new Space[ROWS][COLUMNS];
        //start by creating all of the spaces on the board as empty spaces
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                //Even ROWS start with black space
                if (i % 2 == 0) {
                    if (j % 2 != 0) {
                        boardArray[i][j] = new Space(Space.SpColor.black);
                    } else {
                        boardArray[i][j] = new Space(Space.SpColor.white);
                    }
                } else {
                    if (j % 2 != 0) {
                        boardArray[i][j] = new Space(Space.SpColor.white);
                    } else {
                        boardArray[i][j] = new Space(Space.SpColor.black);
                    }
                }
            }
        }
        return boardArray;
    }

    //
    // Methods
    //
    /**
     * Move piece from one location to another, not worrying
     * about validation. We assume the move calling this
     * has already validated
     * @param location1 location the piece starts in
     * @param location2 the desired location of the piece
     */
    public void move(Position location1, Position location2) {
        Space startSpace = boardArray[location1.getRow()][location1.getCell()];
        Space endSpace = boardArray[location2.getRow()][location2.getCell()];
        Piece beingMoved = startSpace.pieceInfo();
            startSpace.removePiece();
            endSpace.addPiece(beingMoved);
    }

    public boolean spaceIsValid(Position position) {
        Space space;
        try {
            space = boardArray[position.getRow()][position.getCell()];
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return space.isValid();
    }

    /**
     * Return a row of spaces at the specified index
     * @param rowIndex the index of the row to return
     * @return a row of spaces
     */
    public Space[] getRow(int rowIndex) {
        return this.boardArray[rowIndex];
    }

    /**
     * Helper method to return a space at a specific location
     * @param position
     * @return
     */
    public Space getSpace(Position position) {
        return this.boardArray[position.getRow()][position.getCell()];
    }

    /**
     * Helper method that locates all the pieces on the board of a certain
     * color, then returns their locations.
     *
     * @param color the piece color to search for
     * @return a list of the positions that pieces of the specified color can
     *      be found
     */
    public ArrayList<Position> getPieceLocations(Piece.PColor color) {
        ArrayList<Position> pieces = new ArrayList<>();
        for(int row = 0; row < ROWS; row++) {
            for(int col = 0; col < COLUMNS; col++) {
                Space currentSpace = this.boardArray[row][col];
                if(currentSpace.doesHasPiece() && currentSpace.pieceInfo().pieceColor == color) {
                    pieces.add(new Position(row, col));
                }
            }
        }
        return pieces;
    }

    @Override
    public String toString() {
        String out = "\n";
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if(boardArray[i][j].doesHasPiece()) {
                    if(boardArray[i][j].pieceInfo().pieceColor == PColor.red) {
                        out = out + "X";
                    } else {
                        out = out + "O";
                    }
                } else {
                    out = out + "_";
                }
            }
            out = out + "\n";
        }
        return out;
    }
}
