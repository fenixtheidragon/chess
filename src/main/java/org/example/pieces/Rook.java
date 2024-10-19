package org.example.pieces;

import lombok.Getter;
import lombok.Setter;
import org.example.Color;
import org.example.board.Tile;
import org.example.exceptions.ImpossibleMoveException;
import org.example.game.Turn;
@Getter
@Setter
public class Rook extends Piece{
    private boolean firstMove;

    public Rook(Color color) {
        super(color);
        this.firstMove = true;
    }

    @Override
    public boolean moveFromTo(Turn currentTurn, Tile initial, Tile newTile) throws ImpossibleMoveException {
        return false;
    }

    @Override
    public String toString() {
        if (color == Color.WHITE) {
            return String.valueOf('♜');
        }
        return String.valueOf('♖');
    }
}
