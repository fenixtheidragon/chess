package org.example.exceptions;

import org.example.board.Tile;
import org.example.pieces.Piece;

public class ImpossibleMoveException extends Exception{

    public ImpossibleMoveException(int amountOfCoordinates) {
        super("Move is impossible because amount of coordinates must be 4, but was " + amountOfCoordinates);
    }
    public ImpossibleMoveException(Piece piece, Tile tile) {
        super("Move to tile " + tile.getLetterCoordinate() + "," + tile.getDigitCoordinate()
                + " with piece " + piece.toString() + " is impossible.");
    }
}
