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
import java.util.List;

@Getter
@Setter
public class Pawn extends Piece {
    private boolean firstMove;
    private boolean enPassantPossibleForThisPawn;
    private boolean enPassantPossibleAgainstThisPawn;
    private boolean enPassantRealised;

    public Pawn(Color color) {
        super(color);
        this.firstMove = true;
        this.enPassantPossibleForThisPawn = false;
        this.enPassantPossibleAgainstThisPawn = false;
        this.enPassantRealised = false;
    }

    @Override
    public boolean moveFromTo(
            Turn currentTurn, Tile initial, Tile newTile
    ) throws ImpossibleMoveException, IllegalCoordinateException {

        initializePotentialMoves(currentTurn, initial, newTile);
        return completeMove(currentTurn, initial, newTile);
    }

    public List<Tile> calculatePotentialMoves(
            Turn currentTurn, Tile initial
    ) throws IllegalCoordinateException {

        BoardState boardState = currentTurn.getInitialState();
        potentialMoves = new ArrayList<>();
        letterCoordinate = initial.getLetterCoordinate();
        digitCoordinate = initial.getDigitCoordinate();
        addOneTileForwardMove(boardState);
        addTwoTileForwardMove(boardState);
        addEatingMove(boardState);
        addEnPassantMove(boardState);
        return potentialMoves;
    }

    private void initializePotentialMoves(
            Turn currentTurn, Tile initial, Tile newTile
    ) throws ImpossibleMoveException {
        try {
            potentialMoves = calculatePotentialMoves(currentTurn, initial);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (potentialMoves.isEmpty()) {
            throw new ImpossibleMoveException(this, newTile);
        }
    }

    private boolean completeMove(
            Turn currentTurn, Tile initial, Tile newTile
    ) throws IllegalCoordinateException {

        BoardState boardState = currentTurn.getInitialState();
        for (Tile potential : potentialMoves) {
            if (potential != null && potential.equals(newTile)) {
                boardState.getTile(initial).setUnoccupied();
                boardState.getTile(newTile).setOccupiedBy(this);
                if (firstMove) {
                    firstMove = false;
                    isEnPassantNowPossibleAgainstThisPawn(boardState, newTile);
                }
                checkIfEnPassantWasMade(boardState, newTile);
                return true;
            }
        }
        return false;
    }

    private void addOneTileForwardMove(BoardState boardState) throws IllegalCoordinateException {
        if (!boardState.getTile(letterCoordinate, digitCoordinate + getDigitModifier()).isOccupied()) {
            if (isPawnPinnedToKingHorizontally(boardState)) {
                return;
            }
            potentialMoves.add(boardState.getTile(letterCoordinate, digitCoordinate + getDigitModifier()));
        }
    }

    private void addTwoTileForwardMove(BoardState boardState) throws IllegalCoordinateException {
        Tile tile = boardState.getTile(letterCoordinate, digitCoordinate + getDigitModifier() * 2);
        if (firstMove && !tile.isOccupied()) {
            if (isPawnPinnedToKingHorizontally(boardState)) {
                return;
            }
            potentialMoves.add(tile);
        }
    }

    private void addEatingMove(BoardState boardState) throws IllegalCoordinateException {
        if (isPawnPinnedToKingHorizontally(boardState) || isPawnPinnedToKingVertically(boardState)) {
            return;
        }
        if (boardState.getTile(letterCoordinate + 1, digitCoordinate + getDigitModifier()).isOccupied()) {
            if (isPawnPinnedToKingDiagonally(boardState)) {
                Tile kingTile = getKingTile(boardState);
                int[] differenceBetweenPawnAndKing = boardState.getTiles().getDifferenceBetweenTiles(
                        letterCoordinate, digitCoordinate,
                        kingTile.getLetterCoordinate(), kingTile.getDigitCoordinate());
                int[] differenceBetweenNewTileAndPawn = boardState.getTiles().getDifferenceBetweenTiles(
                        letterCoordinate + 1, digitCoordinate + getDigitModifier(),
                        letterCoordinate, digitCoordinate);
                //диагональ слева снизу вправо вверх (для белых фигур)
                if ((differenceBetweenPawnAndKing[0] < 0 && differenceBetweenNewTileAndPawn[0] < 0
                        && differenceBetweenPawnAndKing[1] < 0 && differenceBetweenNewTileAndPawn[1] < 0
                        && color == Color.WHITE)
                        //диагональ слева сверху вправо вниз (для чёрных фигур)
                        || (differenceBetweenPawnAndKing[0] < 0 && differenceBetweenNewTileAndPawn[0] < 0
                        && differenceBetweenPawnAndKing[1] > 0 && differenceBetweenNewTileAndPawn[1] > 0
                        && color == Color.BLACK)
                ) {
                    potentialMoves.add(boardState.getTile(
                            letterCoordinate + 1, digitCoordinate + getDigitModifier()));
                }
            } else {
                potentialMoves.add(boardState.getTile(
                        letterCoordinate + 1, digitCoordinate + getDigitModifier()));
            }
        }
        if (boardState.getTile(letterCoordinate - 1, digitCoordinate + getDigitModifier()).isOccupied()) {
            if (isPawnPinnedToKingDiagonally(boardState)) {
                Tile kingTile = getKingTile(boardState);
                int[] differenceBetweenPawnAndKing = boardState.getTiles().getDifferenceBetweenTiles(
                        letterCoordinate, digitCoordinate,
                        kingTile.getLetterCoordinate(), kingTile.getDigitCoordinate());
                int[] differenceBetweenNewTileAndPawn = boardState.getTiles().getDifferenceBetweenTiles(
                        letterCoordinate - 1, digitCoordinate + getDigitModifier(),
                        letterCoordinate, digitCoordinate);
                //диагональ справа снизу влево вверх (для белых фигур)
                if ((differenceBetweenPawnAndKing[0] > 0 && differenceBetweenNewTileAndPawn[0] > 0
                        && differenceBetweenPawnAndKing[1] < 0 && differenceBetweenNewTileAndPawn[1] < 0
                        && color == Color.WHITE)
                        //диагональ слева сверху вправо вниз (для чёрных фигур)
                        || (differenceBetweenPawnAndKing[0] > 0 && differenceBetweenNewTileAndPawn[0] > 0
                        && differenceBetweenPawnAndKing[1] > 0 && differenceBetweenNewTileAndPawn[1] > 0
                        && color == Color.BLACK)
                ) {
                    potentialMoves.add(boardState.getTile(
                            letterCoordinate - 1, digitCoordinate + getDigitModifier()));
                }
            } else {
                potentialMoves.add(boardState.getTile(
                        letterCoordinate - 1, digitCoordinate + getDigitModifier()));
            }
        }
    }

    private Tile getKingTile(BoardState boardState) {
        try {
            return boardState.getTileWithKing(color);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void addEnPassantMove(BoardState boardState) throws IllegalCoordinateException {
        if (!enPassantPossibleForThisPawn) {
            return;
        }
        if (letterCoordinate >= 0 && letterCoordinate <= 7) {
            if (letterCoordinate == 7) {
                tryToAddEnPassantToPotentialMoves(boardState, -1);
            } else if (letterCoordinate == 0) {
                tryToAddEnPassantToPotentialMoves(boardState, 1);
            } else {
                tryToAddEnPassantToPotentialMoves(boardState, 1);
                tryToAddEnPassantToPotentialMoves(boardState, -1);
            }

        }
    }

    private void tryToAddEnPassantToPotentialMoves(
            BoardState boardState, int letterCoordinateModifier
    ) throws IllegalCoordinateException {
        Tile neighboringTile = boardState.getTile(letterCoordinate + letterCoordinateModifier, digitCoordinate);
        if (isEnPassantPossibleAgainst(neighboringTile)) {
            addEnPassantToPotentialMoves(boardState, neighboringTile);
        }
    }

    private boolean isEnPassantPossibleAgainst(Tile neighboringTile) {
        return neighboringTile.isOccupied()
                && neighboringTile.getOccupyingPiece().getClass().equals(Pawn.class)
                && (((Pawn) neighboringTile.getOccupyingPiece()).isEnPassantPossibleAgainstThisPawn());
    }

    private void addEnPassantToPotentialMoves(
            BoardState boardState, Tile neighboringTile
    ) throws IllegalCoordinateException {

        potentialMoves.add(boardState.getTile(
                neighboringTile.getLetterCoordinate(),
                color == Color.WHITE ? digitCoordinate + 1 : digitCoordinate - 1)
        );
    }

    private boolean isPawnPinnedToKingHorizontally(BoardState boardState) throws IllegalCoordinateException {
        if (pinnedToKing) {
            Tile kingTile = getKingTile(boardState);
            int[] difference = boardState.getTiles()
                    .getDifferenceBetweenTiles(letterCoordinate, digitCoordinate,
                            kingTile.getLetterCoordinate(), kingTile.getDigitCoordinate());
            //если пешка на одной линии с королём по горизонтали
            return difference[0] != 0 && difference[1] == 0;
        }
        return false;
    }

    private boolean isPawnPinnedToKingDiagonally(BoardState boardState) throws IllegalCoordinateException {
        if (pinnedToKing) {
            Tile kingTile = getKingTile(boardState);
            int[] difference = boardState.getTiles()
                    .getDifferenceBetweenTiles(letterCoordinate, digitCoordinate,
                            kingTile.getLetterCoordinate(), kingTile.getDigitCoordinate());
            //если пешка на одной линии с королём по диагонали
            return Math.abs(difference[0]) == Math.abs(difference[1]);
        }
        return false;
    }


    private boolean isPawnPinnedToKingVertically(BoardState boardState) throws IllegalCoordinateException {
        if (pinnedToKing) {
            Tile kingTile = getKingTile(boardState);
            int[] difference = boardState.getTiles()
                    .getDifferenceBetweenTiles(letterCoordinate, digitCoordinate,
                            kingTile.getLetterCoordinate(), kingTile.getDigitCoordinate());
            //если пешка на одной линии с королём по вертикали
            return difference[0] == 0 && difference[1] != 0;
        }
        return false;
    }

    private int getDigitModifier() {
        return color == Color.WHITE ? 1 : -1;
    }

    private void isEnPassantNowPossibleAgainstThisPawn(
            BoardState boardState, Tile newTile
    ) throws IllegalCoordinateException {
        int letter = newTile.getLetterCoordinate();
        if (letter == 0) {
            tryToSetEnPassant(boardState, newTile, 1);
        } else if (letter == 7) {
            tryToSetEnPassant(boardState, newTile, -1);
        } else {
            tryToSetEnPassant(boardState, newTile, 1);
            tryToSetEnPassant(boardState, newTile, -1);
        }
    }

    private void tryToSetEnPassant(
            BoardState boardState, Tile newTile, int letterCoordinateModifier
    ) throws IllegalCoordinateException {

        Tile neighboringTile = boardState.getTile(
                newTile.getLetterCoordinate() + letterCoordinateModifier,
                newTile.getDigitCoordinate());
        if (neighboringTile.isOccupied()
                && neighboringTile.getOccupyingPiece().getClass().equals(Pawn.class)) {
            ((Pawn) neighboringTile.getOccupyingPiece()).setEnPassantPossibleForThisPawn(true);
            this.enPassantPossibleAgainstThisPawn = true;
        }
    }

    private void checkIfEnPassantWasMade(BoardState boardState, Tile newTile) throws IllegalCoordinateException {
        Tile tile = boardState.getTile(
                newTile.getLetterCoordinate(),
                newTile.getDigitCoordinate() - getDigitModifier());
        if (tile.isOccupied() && tile.getOccupyingPiece().getClass().equals(Pawn.class)) {
            enPassantPossibleForThisPawn = false;
            boardState.getTile(
                    newTile.getLetterCoordinate(),
                    newTile.getDigitCoordinate() - getDigitModifier()
            ).setUnoccupied();
        }
    }

    //private Tile getPotentialOneTileForwardMove(BoardState boardState, Tile initial) {}

    private boolean isPromoted(Tile current) {
        return (current.getDigitCoordinate() == 7
                || current.getDigitCoordinate() == 0
        );
    }

    public Piece promoteTo(String piece) {
        return switch (piece) {
            case "Rook" -> new Rook(color);
            case "Knight" -> new Knight(color);
            case "Bishop" -> new Bishop(color);
            default -> new Queen(color);
        };
    }

    @Override
    public String toString() {
        if (color == Color.WHITE) {
            return String.valueOf('♟');
        }
        return String.valueOf('♙');
    }
}
