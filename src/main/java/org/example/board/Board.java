package org.example.board;

import lombok.Getter;
import org.example.Color;
import org.example.exceptions.IllegalCoordinateException;
import org.example.pieces.*;

import java.util.StringJoiner;

public class Board {
    @Getter
    private final static char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    @Getter
    private final static char[] digits = {'1', '2', '3', '4', '5', '6', '7', '8'};
    @Getter
    private final TwoDMatrix tiles;

    public Board(int width, int height) {
        this.tiles = new TwoDMatrix(width, height);
    }

    public void initialize() {
        try {
            initialize8x8();
            placePiecesOnBoard();
        } catch (IllegalCoordinateException e) {
            e.printStackTrace();
        }
    }

    private void initialize8x8() throws IllegalCoordinateException {
        for (int x = 0; x < letters.length; x++) {
            for (int y = 0; y < digits.length; y++) {
                if ((x % 2 == y % 2)) {
                    tiles.setTile(x, y, new Tile(x, y, Color.BLACK));
                } else {
                    tiles.setTile(x, y, new Tile(x, y, Color.WHITE));
                }
            }
        }
    }

    private void placePiecesOnBoard() throws IllegalCoordinateException {
        placeWhitePieces();
        placeBlackPieces();
    }

    private void placeWhitePieces() throws IllegalCoordinateException {
        tiles.getTile(0, 0).setOccupiedBy(new Rook(Color.WHITE));
        tiles.getTile(1, 0).setOccupiedBy(new Knight(Color.WHITE));
        tiles.getTile(2, 0).setOccupiedBy(new Bishop(Color.WHITE));
        tiles.getTile(3, 0).setOccupiedBy(new Queen(Color.WHITE));
        tiles.getTile(4, 0).setOccupiedBy(new King(Color.WHITE));
        tiles.getTile(5, 0).setOccupiedBy(new Bishop(Color.WHITE));
        tiles.getTile(6, 0).setOccupiedBy(new Knight(Color.WHITE));
        tiles.getTile(7, 0).setOccupiedBy(new Rook(Color.WHITE));
        for (int i = 0; i < letters.length; i++) {
            tiles.getTile(i, 1).setOccupiedBy(new Pawn(Color.WHITE));
        }
    }

    private void placeBlackPieces() throws IllegalCoordinateException {
        tiles.getTile(0, 7).setOccupiedBy(new Rook(Color.BLACK));
        tiles.getTile(1, 7).setOccupiedBy(new Knight(Color.BLACK));
        tiles.getTile(2, 7).setOccupiedBy(new Bishop(Color.BLACK));
        tiles.getTile(3, 7).setOccupiedBy(new Queen(Color.BLACK));
        tiles.getTile(4, 7).setOccupiedBy(new King(Color.BLACK));
        tiles.getTile(5, 7).setOccupiedBy(new Bishop(Color.BLACK));
        tiles.getTile(6, 7).setOccupiedBy(new Knight(Color.BLACK));
        tiles.getTile(7, 7).setOccupiedBy(new Rook(Color.BLACK));
        for (int i = 0; i < letters.length; i++) {
            tiles.getTile(i, 6).setOccupiedBy(new Pawn(Color.BLACK));
        }
    }

    @Override
    public String toString() {

        StringJoiner lineJoiner = new StringJoiner(System.lineSeparator());
        for (int y = tiles.getTiles().length - 1; y >= 0; y--) {
            StringJoiner spaceJoiner = new StringJoiner(" ");
            for (int x = 0; x < tiles.getTiles()[y].getTileRow().length; x++) {
                try {
                    spaceJoiner.add(tiles.getTile(x, y).toString());
                } catch (IllegalCoordinateException e) {
                    e.printStackTrace();
                }
            }
            spaceJoiner.add(digits[y] + "");
            lineJoiner.add(spaceJoiner.toString());
        }
        lineJoiner.add("a b c d e f g h");
        return lineJoiner.toString();
    }
}
