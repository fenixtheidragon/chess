package org.example.board;

import lombok.Getter;

@Getter
public class TileRow {
    private final Tile[] tileRow;

    public TileRow(int width) {
        this.tileRow = new Tile[width];
    }
}
