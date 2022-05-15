package com.koleff.chess.Pieces;


import static com.koleff.chess.CoordinatesAndMoves.Coordinates.getCoordinatesToString;
import static com.koleff.chess.CoordinatesAndMoves.Moves.*;

public class Rook extends Piece {
    /**
     * Fields
     */

    /**
     * Constructors
     */

    /**
     * @param coordinatesX
     * @param coordinatesY
     * @param pieceColor
     */
    public Rook(char coordinatesX, int coordinatesY, Colour pieceColor) {
        super(coordinatesX, coordinatesY, pieceColor);
        hasMoved = false;
    }

    public Rook(char coordinatesX, int coordinatesY, Colour pieceColor, boolean isProtected, boolean hasMoved) {
        super(coordinatesX, coordinatesY, pieceColor);
        this.isProtected = isProtected;
        this.hasMoved = hasMoved;
    }

    public Rook(Piece piece) {
        super(piece.getCoordinatesXChar(), piece.getCoordinatesY(), piece.getColor());
    }

    @Override
    public Rook copy() {
        return new Rook(coordinatesX, coordinatesY, pieceColor, isProtected, hasMoved);
    }

    @Override
    public void move() {
        int coordinatesX = this.getCoordinatesXInt();
        int coordinatesY = this.getCoordinatesY();
        String coordinates;
        boolean isIllegal;

        //UP
        while (coordinatesY < 8 && !hasToBreak) {
            coordinatesY++;

            coordinates = getCoordinatesToString(coordinatesX, coordinatesY);

            isIllegal = moves.checkIfMoveIsIllegal(coordinates);
            if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                System.out.printf("Up -> %s\n", coordinates);
            }
        }

        coordinatesY = this.getCoordinatesY();
        hasToBreak = false;

        //DOWN
        while (coordinatesY > 1 && !hasToBreak) {
            coordinatesY--;

            coordinates = getCoordinatesToString(coordinatesX, coordinatesY);

            isIllegal = moves.checkIfMoveIsIllegal(coordinates);
            if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                System.out.printf("Down -> %s\n", coordinates);
            }
        }

        coordinatesY = this.getCoordinatesY();
        hasToBreak = false;

        //LEFT
        while (coordinatesX > 1 && !hasToBreak) {
            coordinatesX--;

            coordinates = getCoordinatesToString(coordinatesX, coordinatesY);

            isIllegal = moves.checkIfMoveIsIllegal(coordinates);
            if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                System.out.printf("Left -> %s\n", coordinates);
            }
        }

        coordinatesX = this.getCoordinatesXInt();
        hasToBreak = false;

        //RIGHT
        while (coordinatesX < 8 && !hasToBreak) {
            coordinatesX++;

            coordinates = getCoordinatesToString(coordinatesX, coordinatesY);

            isIllegal = moves.checkIfMoveIsIllegal(coordinates);
            if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                System.out.printf("Right -> %s\n", coordinates);
            }
        }
        hasToBreak = false;
    }
}
