package org.example.game;

import lombok.Getter;
import lombok.Setter;
import org.example.Color;
import org.example.board.BoardState;
import org.example.board.Tile;
import org.example.exceptions.IllegalCoordinateException;
import org.example.exceptions.ImpossibleMoveException;

public class Player {
    @Getter
    private final Color color;
    @Setter
    private boolean moved;

    public Player(Color color) {
        this.color = color;
    }

    public void choosePromotion(String promoteTo) {

    }

    public boolean hasMoved() {
        return moved;
    }

    public boolean moves(
            Turn currentTurn, BoardState boardState, int[] coordinates
    ) throws ImpossibleMoveException, IllegalCoordinateException {

        if (coordinates.length != 4) {
            throw new ImpossibleMoveException(coordinates.length);
        }
        Tile from;
        Tile to;
        try {
            from = boardState.getTile(coordinates[0], coordinates[1]);
            to = boardState.getTile(coordinates[2], coordinates[3]);
        } catch (IllegalCoordinateException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        if (from.isOccupied()) {
            if (from.getOccupyingPiece().getColor() == this.color) {
                return from.getOccupyingPiece().moveFromTo(currentTurn, from, to);
            } else {
                return false;
            }

        } else {
            return false;
        }
    }
}
