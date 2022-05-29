package com.koleff.chess.Pieces;

import static com.koleff.chess.BoardAndFEN.ChessBoardController.moves;
import static com.koleff.chess.CoordinatesAndMoves.Coordinates.getCoordinatesToString;
import static com.koleff.chess.CoordinatesAndMoves.Moves.*;

public class Bishop extends Piece {
    /**Fields*/

    /**
     * Constructors
     */
    public Bishop(char coordinatesX, int coordinatesY, Colour pieceColor) {
        super(coordinatesX, coordinatesY, pieceColor);
        hasMoved = false;
    }

    public Bishop(char coordinatesX, int coordinatesY, Colour pieceColor, boolean isProtected, boolean hasMoved) {
        super(coordinatesX, coordinatesY, pieceColor);
        this.isProtected = isProtected;
        this.hasMoved = hasMoved;
    }

    public Bishop(Piece piece) {
        super(piece.getCoordinatesXChar(), piece.getCoordinatesY(), piece.getColor());
    }

    @Override
    public Bishop copy() {
        return new Bishop(coordinatesX, coordinatesY, pieceColor, isProtected, hasMoved);
    }

    @Override
    public void move() {
        int coordinatesX = this.getCoordinatesXInt();
        int coordinatesY = this.getCoordinatesY();
        String coordinates;
        boolean isIllegal;

        //DIAGONALLY UP LEFT
        while (coordinatesY < 8 && coordinatesX > 1 && !hasToBreak) {
            coordinatesY++;
            coordinatesX--;

            coordinates = getCoordinatesToString(coordinatesX, coordinatesY);

            isIllegal = moves.checkIfMoveIsIllegal(coordinates);
            if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                System.out.printf("Diagonally up left -> %s\n", coordinates);
            }
        }
        coordinatesX = this.getCoordinatesXInt();
        coordinatesY = this.getCoordinatesY();
        hasToBreak = false;

        //DIAGONALLY UP RIGHT
        while (coordinatesY < 8 && coordinatesX < 8 && !hasToBreak) {
            coordinatesY++;
            coordinatesX++;

            coordinates = getCoordinatesToString(coordinatesX, coordinatesY);

            isIllegal = moves.checkIfMoveIsIllegal(coordinates);
            if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                System.out.printf("Diagonally up right -> %s\n", coordinates);
            }
        }
        coordinatesX = this.getCoordinatesXInt();
        coordinatesY = this.getCoordinatesY();
        hasToBreak = false;

        //DIAGONALLY DOWN LEFT
        while (coordinatesY > 1 && coordinatesX > 1 && !hasToBreak) {
            coordinatesY--;
            coordinatesX--;

            coordinates = getCoordinatesToString(coordinatesX, coordinatesY);

            isIllegal = moves.checkIfMoveIsIllegal(coordinates);
            if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                System.out.printf("Diagonally down left -> %s\n", coordinates);
            }
        }
        coordinatesX = this.getCoordinatesXInt();
        coordinatesY = this.getCoordinatesY();
        hasToBreak = false;

        //DIAGONALLY DOWN RIGHT
        while (coordinatesY > 1 && coordinatesX < 8 && !hasToBreak) {
            coordinatesY--;
            coordinatesX++;

            coordinates = getCoordinatesToString(coordinatesX, coordinatesY);

            isIllegal = moves.checkIfMoveIsIllegal(coordinates);
            if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                System.out.printf("Diagonally down right -> %s\n", coordinates);
            }
        }
        hasToBreak = false;
    }
}

