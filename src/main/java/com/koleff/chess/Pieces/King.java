package com.koleff.chess.Pieces;

import java.io.Serializable;

import static com.koleff.chess.BoardAndFEN.ChessBoardController.*;
import static com.koleff.chess.CoordinatesAndMoves.Coordinates.getCoordinatesToString;
import static com.koleff.chess.CoordinatesAndMoves.Moves.*;

public class King extends Piece implements Serializable {
    /**
     * Fields
     */

    /**
     * Constructors
     */
    public King(char coordinatesX, int coordinatesY, Colour pieceColor) {
        super(coordinatesX, coordinatesY, pieceColor);
        hasMoved = false;
    }

    public King(char coordinatesX, int coordinatesY, Colour pieceColor, boolean isProtected, boolean hasMoved) {
        super(coordinatesX, coordinatesY, pieceColor);
        this.isProtected = isProtected;
        this.hasMoved = hasMoved;
    }

    /**
     * Functions
     */

    @Override
    public King copy() {
        return new King(coordinatesX, coordinatesY, pieceColor, isProtected, hasMoved);
    }

    @Override
    public void move() {
        int coordinatesX = this.getCoordinatesXInt();
        int coordinatesY = this.getCoordinatesY();
        boolean isIllegal;

        if (coordinatesY < 8) {
            if (coordinatesX > 1) {
                String topLeftCoordinates = getCoordinatesToString(coordinatesX - 1, coordinatesY + 1);
                isIllegal = moves.checkIfMoveIsIllegal(topLeftCoordinates);
                if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                    System.out.printf("Diagonally up left -> %s\n", topLeftCoordinates);
                }
                hasToBreak = false;
            }

            String topCoordinates = getCoordinatesToString(coordinatesX, coordinatesY + 1);
            isIllegal = moves.checkIfMoveIsIllegal(topCoordinates);
            if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                System.out.printf("Up -> %s\n", topCoordinates);
            }
            hasToBreak = false;

            if (coordinatesX < 8) {
                String topRightCoordinates = getCoordinatesToString(coordinatesX + 1, coordinatesY + 1);
                isIllegal = moves.checkIfMoveIsIllegal(topRightCoordinates);
                if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                    System.out.printf("Diagonally up right -> %s\n", topRightCoordinates);
                }
                hasToBreak = false;
            }
        }

        if (coordinatesX > 1) {
            String leftCoordinates = getCoordinatesToString(coordinatesX - 1, coordinatesY);
            isIllegal = moves.checkIfMoveIsIllegal(leftCoordinates);
            if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                System.out.printf("Left -> %s\n", leftCoordinates);
            }
            hasToBreak = false;
        }

        if (coordinatesX < 8) {
            String rightCoordinates = getCoordinatesToString(coordinatesX + 1, coordinatesY);
            isIllegal = moves.checkIfMoveIsIllegal(rightCoordinates);
            if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                System.out.printf("Right -> %s\n", rightCoordinates);
            }
            hasToBreak = false;
        }

        if (coordinatesY > 1) {
            if (coordinatesX > 1) {
                String downLeftCoordinates = getCoordinatesToString(coordinatesX - 1, coordinatesY - 1);
                isIllegal = moves.checkIfMoveIsIllegal(downLeftCoordinates);
                if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                    System.out.printf("Diagonally down left -> %s\n", downLeftCoordinates);
                }
                hasToBreak = false;
            }

            String downCoordinates = getCoordinatesToString(coordinatesX, coordinatesY - 1);
            isIllegal = moves.checkIfMoveIsIllegal(downCoordinates);
            if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                System.out.printf("Down -> %s\n", downCoordinates);
            }
            hasToBreak = false;

            if (coordinatesX < 8) {
                String downRightCoordinates = getCoordinatesToString(coordinatesX + 1, coordinatesY - 1);
                isIllegal = moves.checkIfMoveIsIllegal(downRightCoordinates);
                if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                    System.out.printf("Diagonally down right -> %s\n", downRightCoordinates);
                }
                hasToBreak = false;
            }
        }

        if (!isCalculatingAttackingMoves && !isCalculatingProtection) {
            currentPlayer.checkForCastling();
        }
    }
}