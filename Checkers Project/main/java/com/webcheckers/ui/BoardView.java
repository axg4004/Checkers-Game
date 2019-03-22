package com.webcheckers.ui;

import com.webcheckers.model.Board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class BoardView implements Iterable<RowView> {
    //
    // Attributes
    //
    ArrayList<RowView> listRowViews;

    /**
     * Create a new BoardView object using a Board model-tier class so the
     * checkers board can be rendered for the user.
     *
     * @param board
     *      the checkers board that is to be rendered
     * @param isWhite
     *      true when the board is being rendered for the White player, false
     *      when the board is being rendered for the Red player.
     */
    public BoardView(Board board, boolean isWhite) {
        // Initialize the attributes
        this.listRowViews = new ArrayList<RowView>();

        // Create the rows
        for (int row = 0; row < Board.ROWS; row++) {
            this.listRowViews.add(new RowView(board.getRow(row), row, isWhite));
        }

        // Reverse the rows when rendering for White so the board appears
        // correctly.
        if (isWhite) {
            Collections.reverse(this.listRowViews);
        }
    }

    //
    // Methods
    //
    /**
     * Gets iterator of RowViews
     * @return RowView iterator
     */
    public Iterator<RowView> iterator() {
        return listRowViews.iterator();
    }

}
