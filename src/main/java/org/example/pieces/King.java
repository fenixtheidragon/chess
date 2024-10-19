package org.example.pieces;

import lombok.Getter;
import lombok.Setter;
import org.example.Color;
import org.example.board.BoardState;
import org.example.board.Tile;
import org.example.exceptions.IllegalCoordinateException;
import org.example.exceptions.ImpossibleMoveException;
import org.example.game.Turn;

import java.util.ArrayList;

@Getter
@Setter
public class King extends Piece {
    private boolean inCheck;
    private boolean firstMove;

    public King(Color color) {
        super(color);
        this.inCheck = false;
        this.firstMove = true;
    }

    @Override
    public boolean moveFromTo(Turn currentTurn, Tile initial, Tile newTile) throws ImpossibleMoveException {
        BoardState boardState = currentTurn.getInitialState();
        potentialMoves = new ArrayList<>();
        addOneTileMove(boardState, initial);
        addCastlingToTheLeft(boardState);
        addCastlingToTheRight(boardState);
        if (makeMove(boardState, newTile)) {
            return true;
        }
        return false;
    }

    private void addOneTileMove(BoardState boardState, Tile initial) {
        letterCoordinate = initial.getLetterCoordinate();
        digitCoordinate = initial.getDigitCoordinate();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                try {
                    if (letterCoordinate + x >= 0 && letterCoordinate + x <= 7
                            && digitCoordinate + y >= 0 && digitCoordinate + y <= 7) {
                        Tile potential = boardState.getTile(letterCoordinate + x, digitCoordinate + y);
                        if (!potential.isAttacked()) {
                            if (potential.isOccupied() && potential.getOccupyingPiece().getColor() == color) {
                                continue;
                            }
                            potentialMoves.add(potential);
                        }
                    }
                } catch (IllegalCoordinateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addCastlingToTheLeft(BoardState boardState) {
        if (!firstMove || inCheck) {
            return;
        }
        Tile rookTile = getLeftRookTile(boardState);
        Rook rook = getRookFromTile(rookTile);
        if (rook != null && rook.isFirstMove()
                && tilesBetweenRookAndKingAreUnoccupiedAndNotAttacked(boardState, rookTile)) {
            try {
                if (color == Color.WHITE) {
                    potentialMoves.add(boardState.getTile(2, 0));
                } else {
                    potentialMoves.add(boardState.getTile(2, 7));
                }
            } catch (IllegalCoordinateException e) {
                e.printStackTrace();
            }
        }
    }

    private Rook getRookFromTile(Tile rookTile) {
        if (rookTile != null && rookTile.isOccupied()
                && rookTile.getOccupyingPiece().getClass().equals(Rook.class)) {
            return (Rook) rookTile.getOccupyingPiece();
        }
        return null;
    }

    private Tile getLeftRookTile(BoardState boardState) {
        try {
            if (color == Color.WHITE) {
                return boardState.getTile(0, 0);
            } else {
                return boardState.getTile(0, 7);
            }
        } catch (IllegalCoordinateException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addCastlingToTheRight(BoardState boardState) {
        if (!firstMove || inCheck) {
            return;
        }
        Tile rookTile = getRightRookTile(boardState);
        Rook rook = getRookFromTile(rookTile);
        if (rook != null && rook.isFirstMove()
                && tilesBetweenRookAndKingAreUnoccupiedAndNotAttacked(boardState, rookTile)) {
            try {
                if (color == Color.WHITE) {
                    potentialMoves.add(boardState.getTile(6, 0));
                } else {
                    potentialMoves.add(boardState.getTile(6, 7));
                }
            } catch (IllegalCoordinateException e) {
                e.printStackTrace();
            }
        }
    }

    private Tile getRightRookTile(BoardState boardState) {
        try {
            if (color == Color.WHITE) {
                return boardState.getTile(7, 0);
            } else {
                return boardState.getTile(7, 7);
            }
        } catch (IllegalCoordinateException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean makeMove(BoardState boardState, Tile newTile) throws ImpossibleMoveException {
        for (Tile potential : potentialMoves) {
            if (potential.equals(newTile)) {
                try {
                    boardState.getTileWithKing(color).setUnoccupied();
                    boardState.getTile(newTile).setOccupiedBy(this);
                    if (firstMove) {
                        checkIfCastlingMoveWasMade(boardState, newTile);
                        firstMove = false;
                    }
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        throw new ImpossibleMoveException(this, newTile);
    }

    private void checkIfCastlingMoveWasMade(BoardState boardState, Tile newTile) {
        if (wasCastlingMoveMade(newTile)) {
            Tile rookTile;
            int digit = (color == Color.WHITE) ? 0 : 7;
            try {
                int letter = newTile.getLetterCoordinate();
                if (letter == 2) {
                    rookTile = boardState.getTile(letter + 1, digit);
                    Rook rook = (Rook) boardState.getTile(0, digit).getOccupyingPiece();
                    boardState.getTile(0, digit).setUnoccupied();
                    boardState.getTile(3, digit).setOccupiedBy(rook);
                } else {
                    rookTile = boardState.getTile(letter - 1, digit);
                    Rook rook = (Rook) boardState.getTile(7, digit).getOccupyingPiece();
                    boardState.getTile(7, digit).setUnoccupied();
                    boardState.getTile(5, digit).setOccupiedBy(rook);
                }
            } catch (IllegalCoordinateException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            ((Rook) rookTile.getOccupyingPiece()).setFirstMove(false);
        }
    }

    private boolean wasCastlingMoveMade(Tile newTile) {
        if (newTile.getLetterCoordinate() == 2 || newTile.getLetterCoordinate() == 6) {
            return true;
        }
        return false;
    }

    private boolean tilesBetweenRookAndKingAreUnoccupiedAndNotAttacked(
            BoardState boardState, Tile rookTile
    ) {
        try {
            int digit;
            if (color == Color.WHITE) {
                digit = 0;
            } else {
                digit = 7;
            }
            int rookLetter = rookTile.getLetterCoordinate();
            if (rookLetter == 0) {
                for (int x = 2; x <= 3; x++) {
                    Tile tile = boardState.getTile(x, digit);
                    if (tile.isOccupied() || tile.isAttacked()) {
                        return false;
                    }
                }
            } else {
                for (int x = 5; x <= 6; x++) {
                    Tile tile = boardState.getTile(x, digit);
                    if (tile.isOccupied() || tile.isAttacked()) {
                        return false;
                    }
                }
            }

        } catch (IllegalCoordinateException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public String toString() {
        if (color == Color.WHITE) {
            return String.valueOf('♚');
        }
        return String.valueOf('♔');
    }
}
