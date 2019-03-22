package com.webcheckers.ui;

import com.webcheckers.model.Board;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.mockito.Mockito;
import spark.utils.Assert;

@Tag("UI-Tier")
public class BoardViewTest {

    // Component Under Test
    private BoardView CuT;

    // Friendly Components
    private Board board;

    // Mocked Components

    @Test
    public void testConstructorReversed() {
        board = new Board();
        BoardView notReversed = new BoardView(board, false);
        CuT = new BoardView(board, true);

        // starting and either end of each board, check if they're the same
        // if all of them match then the row has been flipped
        Collections.reverse(notReversed.listRowViews);
        for(int i = 0; i < CuT.listRowViews.size(); i++) {
            if(CuT.listRowViews.get(i).equals(notReversed.listRowViews.get(i))) {
                continue;
            } else {
                Assertions.fail("Rows not reversed properly");
            }
        }

    }

    @Test
    public void testIterator() {
        board = new Board();
        CuT = new BoardView(board, true);
        Assertions.assertNotNull(CuT.iterator());
    }


}
