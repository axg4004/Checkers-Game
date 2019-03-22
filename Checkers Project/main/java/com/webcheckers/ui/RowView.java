package com.webcheckers.ui;

import com.webcheckers.model.Space;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class RowView implements Iterable<SpaceView>{
    //
    // Attributes
    //
    ArrayList<SpaceView> listSpaceViews;
    private int index;

    /**
     * Create a new RowView object using a model-tier representation of the row
     *
     * @param row
     *      the model-tier representation of a row
     * @param index
     *      the index of the row in the board
     * @param reverse
     *      if the row should be reversed, as in when the board is being
     *      rendered for the White player
     */
    public RowView(Space[] row, int index, boolean reverse) {
        // Initialize attributes
        this.index = index;
        this.listSpaceViews = new ArrayList<SpaceView>();

        // Create the spaces
        for (int spaceIndex = 0; spaceIndex < row.length; spaceIndex++) {
            this.listSpaceViews.add(new SpaceView(row[spaceIndex], spaceIndex));
        }

        // Reverse the spaces when rendering for White so the board appears
        // correctly.
        if (reverse) {
            Collections.reverse(this.listSpaceViews);
        }
    }

    //
    // Methods
    //
    /**
     * Get the index of the row
     * @return row index
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Gets an iterator of SpaceViews
     * @return SpaceView iterator
     */
    @Override
    public Iterator<SpaceView> iterator() {
        return listSpaceViews.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof RowView))
            return false;
        else
            return ((RowView) other).index == this.index;
    }
}
