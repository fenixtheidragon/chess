package org.example.board;


import lombok.Getter;
import lombok.Setter;
import org.example.Color;
import org.example.pieces.Piece;

@Getter
public class Tile {

    private final int letterCoordinate;
    private final int digitCoordinate;
    private final Color color;
    private boolean occupied;
    private Piece occupyingPiece;

    public Tile(int letterCoordinate, int digitCoordinate, Color color) {
        this.letterCoordinate = letterCoordinate;
        this.digitCoordinate = digitCoordinate;
        this.color = color;
        this.occupied = false;
        this.occupyingPiece = null;
    }

    public void setOccupiedBy(Piece piece) {
        this.occupyingPiece = piece;
        this.occupied = true;
    }

    public void setUnoccupied() {
        this.occupyingPiece = null;
        this.occupied = false;
    }

    @Override
    public String toString() {
        if (occupied) {
            return occupyingPiece.toString();
        }
        if (color == Color.WHITE) {
            return String.valueOf('◼');
        }
        return String.valueOf('◻');
    }
}