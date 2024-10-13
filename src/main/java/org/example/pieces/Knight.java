package org.example.pieces;

import org.example.Color;
import org.example.board.Tile;
import org.example.exceptions.ImpossibleMoveException;
import org.example.game.Turn;

public class Knight extends Piece {
    public Knight(Color color) {
        super(color);
    }

    @Override
    public boolean moveFromTo(
            Turn currentTurn, Tile initial, Tile newTile
    ) throws ImpossibleMoveException {
        return false;
    }

    @Override
    public String toString() {
        if (color == Color.WHITE) {
            return String.valueOf('♞');
        }
        return String.valueOf('♘');
    }
}
