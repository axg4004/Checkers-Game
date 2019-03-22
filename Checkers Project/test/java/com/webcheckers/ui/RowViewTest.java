package com.webcheckers.ui;

import static org.junit.jupiter.api.Assertions.*;

import com.webcheckers.model.Space;
import com.webcheckers.model.Space.SpColor;

import java.lang.reflect.Array;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("UI-Tier")
public class RowViewTest {

    //
    // Constants
    //
    private static final int ROW_INDEX = 1;

    //
    // Attributes
    //
    private RowView rowView;
    Space[] spaces = new Space[2];
    SpaceView blackSpace = new SpaceView(new Space(SpColor.black), 0);
    SpaceView whiteSpace = new SpaceView(new Space(SpColor.white), 1);

    //
    // Setup
    //
    @BeforeEach
    public void setUp() {
        spaces[0] = new Space(SpColor.black);
        spaces[1] = new Space(SpColor.white);
        // Set default value for the rowView
        rowView = new RowView(spaces, ROW_INDEX, false);
    }

    //
    // Tests
    //
    @Test
    public void testConstructorStandard() {
        assertEquals(blackSpace, rowView.listSpaceViews.get(0), "RowView did not construct correctly");
    }

    @Test
    public void testConstructorReverse() {
        // Override default with a reversed RowView
        rowView = new RowView(spaces, ROW_INDEX, true);
        assertEquals(whiteSpace, rowView.listSpaceViews.get(0), "RowView did not reverse correctly");
    }

    @Test
    public void testGetIndex() {
        assertEquals(rowView.getIndex(), ROW_INDEX, "Index was null");
    }
}
