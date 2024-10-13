package org.example.pieces;

import lombok.Getter;
import org.example.Color;
import org.example.board.Tile;
import org.example.exceptions.IllegalCoordinateException;
import org.example.exceptions.ImpossibleMoveException;
import org.example.game.Turn;

import java.util.List;

@Getter
public abstract class Piece implements Movable {
    protected final Color color;
    protected List<Tile> potentialMoves;
    protected int letterCoordinate;
    protected int digitCoordinate;
    protected boolean pinnedToKing;

    public Piece(Color color) {
        this.color = color;
    }

    @Override
    public abstract boolean moveFromTo(
            Turn currentTurn, Tile initial, Tile newTile
    ) throws ImpossibleMoveException, IllegalCoordinateException;

    @Override
    public abstract String toString();
}
