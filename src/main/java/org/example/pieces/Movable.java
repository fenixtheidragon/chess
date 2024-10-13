package org.example.pieces;

import org.example.board.Tile;
import org.example.exceptions.IllegalCoordinateException;
import org.example.exceptions.ImpossibleMoveException;
import org.example.game.Turn;

public interface Movable {
    public boolean moveFromTo(
            Turn currentTurn, Tile initial, Tile newTile
    ) throws ImpossibleMoveException, IllegalCoordinateException;
}
