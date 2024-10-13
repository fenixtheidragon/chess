package org.example.board;

import lombok.Getter;
import lombok.Setter;
import org.example.Color;
import org.example.exceptions.IllegalCoordinateException;
import org.example.pieces.King;
import org.example.pieces.Piece;

@Getter
public class BoardState {
    private final TwoDMatrix tiles;
    @Setter
    private boolean checkmate;

    public BoardState(TwoDMatrix tiles) {
        this.tiles = tiles;
        this.checkmate = false;
    }

    public Tile getTile(Tile tileToFind) throws IllegalCoordinateException {
        int letterCoordinate = tileToFind.getLetterCoordinate();
        int digitCoordinate = tileToFind.getDigitCoordinate();
        return tiles.getTile(letterCoordinate, digitCoordinate);
    }

    public Tile getTile(int letter, int digit) throws IllegalCoordinateException{
        return tiles.getTile(letter, digit);
    }

    public Tile getTileWithKing(Color color) throws Exception{
        for (TileRow tileRow: tiles.getTiles()) {
            for (Tile tile: tileRow.getTileRow()) {
                if (tile.isOccupied()
                        && tile.getOccupyingPiece().getClass().equals(King.class)
                        && tile.getOccupyingPiece().getColor() == color
                ) {
                    return tile;
                }
            }
        }
        throw new Exception("NO KING ON BOARD! WTF!");
    }

    public void promotePawnOnTileTo(Tile tile, Piece piece) throws IllegalCoordinateException{
        getTile(tile).setOccupiedBy(piece);
    }
}
