package org.example.game;

import org.example.Color;
import org.example.board.Board;
import org.example.board.BoardState;
import org.example.exceptions.IllegalCoordinateException;
import org.example.exceptions.ImpossibleMoveException;
import org.example.exceptions.ParsingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game implements Runnable {
    private final List<Turn> turns = new ArrayList<>();
    private final Board board;
    private final Scanner scanner;
    private final Player white;
    private final Player black;
    private Player currentPlayer;
    private Turn lastTurn;
    private Turn currentTurn;
    private int turnCounter;

    public Game() {
        this.board = new Board(8,8);
        board.initialize();
        this.scanner = new Scanner(System.in);
        white = new Player(Color.WHITE);
        black = new Player(Color.BLACK);
    }

    @Override
    public void run() {
        initializeGame();
        do {
            printBoard();
            setTurnUp();
            while (!currentPlayer.hasMoved()) {
                String move = scanner.nextLine();
                if (move.equals("skip")) {
                    break;
                }
                int [] coordinates;
                try {
                    coordinates = Parser.parse(move);
                    if (currentPlayer.moves(currentTurn, currentTurn.getInitialState(), coordinates)) {
                        currentPlayer.setMoved(true);
                        break;
                    } else {
                        System.out.println("something wrong");
                    }
                } catch (ParsingException | IllegalCoordinateException | ImpossibleMoveException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
            //todo нормально сделать
            currentTurn.setEndState(new BoardState(board.getTiles()));
            turns.add(currentTurn);
            lastTurn = currentTurn;
        } while (!lastTurn.getEndState().isCheckmate());
    }

    private void initializeGame() {
        turnCounter = 0;
        lastTurn = new Turn(turnCounter, white, new BoardState(board.getTiles()));
        lastTurn.setEndState(new BoardState(board.getTiles()));
    }

    private void setTurnUp() {
        turnCounter++;
        currentPlayer = turnCounter % 2 == 1 ? white : black;
        currentPlayer.setMoved(false);
        currentTurn = new Turn(
                turnCounter,
                currentPlayer,
                new BoardState(lastTurn.getEndState().getTiles())
        );
    }

    private void printBoard() {
        System.out.print("\033[H\033[2J"); // Clear the screen
        System.out.flush(); // Flush the output
        System.out.println(board);
    }
}
