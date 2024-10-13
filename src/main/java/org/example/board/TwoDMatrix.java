package org.example.board;

import lombok.Getter;
import org.example.exceptions.IllegalCoordinateException;

@Getter
public class TwoDMatrix {
    private final TileRow[] tiles;


    public TwoDMatrix(int width, int height) {
        this.tiles = new TileRow[height];
        for (int i = 0; i < height; i++) {
            tiles[i] = new TileRow(width);
        }
    }

    public Tile getTile(int x, int y) throws IllegalCoordinateException {
        if (x >= 0 && x < tiles[x].getTileRow().length) {
            if (y >= 0 && y < tiles.length) {
                return tiles[y].getTileRow()[x];
            }
            throw new IllegalCoordinateException("y", y);
        }
        throw new IllegalCoordinateException("x", x);
    }

    public void setTile(int x, int y, Tile tile) throws IllegalCoordinateException {
        if (x >= 0 && x < tiles[x].getTileRow().length) {
            if (y >= 0 && y < tiles.length) {
                tiles[y].getTileRow()[x] = tile;
            } else {
                throw new IllegalCoordinateException("y", y);
            }
        } else {
            throw new IllegalCoordinateException("x", x);
        }
    }

    public int[] getDifferenceBetweenTiles(int... coordinates) throws IllegalCoordinateException {
        if (coordinates.length > 4) {
            throw new IllegalCoordinateException("fifth coordinate");
        }
        return new int[] {coordinates[2] - coordinates[0], coordinates[3] - coordinates[1]};
    }
}
