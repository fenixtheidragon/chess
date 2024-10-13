package org.example.game;

import lombok.Getter;
import lombok.Setter;
import org.example.board.BoardState;
@Getter
public class Turn {
    private int number;
    private Player player;
    private BoardState initialState;
    @Setter
    private BoardState endState;

    public Turn(int number, Player player, BoardState initialState) {
        this.number = number;
        this.player = player;
        this.initialState = initialState;
    }


}
