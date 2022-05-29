package com.koleff.chess.Pieces;


import static com.koleff.chess.BoardAndFEN.ChessBoardController.moves;
import static com.koleff.chess.CoordinatesAndMoves.Coordinates.getCoordinatesToString;
import static com.koleff.chess.CoordinatesAndMoves.Moves.*;

public class Knight extends Piece {
    /**
     * Constructors
     */
    public Knight(char coordinatesX, int coordinatesY, Colour pieceColor) {
        super(coordinatesX, coordinatesY, pieceColor);
        hasMoved = false;
    }

    public Knight(char coordinatesX, int coordinatesY, Colour pieceColor, boolean isProtected, boolean hasMoved) {
        super(coordinatesX, coordinatesY, pieceColor);
        this.isProtected = isProtected;
        this.hasMoved = hasMoved;
    }

    public Knight(Piece piece) {
        super(piece.getCoordinatesXChar(), piece.getCoordinatesY(), piece.getColor());
    }

    @Override
    public Knight copy() {
        return new Knight(coordinatesX, coordinatesY, pieceColor, isProtected, hasMoved);
    }

    @Override
    public void move() {
        int coordinatesX = this.getCoordinatesXInt();
        int coordinatesY = this.getCoordinatesY();
        boolean isIllegal;

        if (coordinatesY < 7) {
            if (coordinatesX > 1) {
                String topLeftCoordinates = getCoordinatesToString(coordinatesX - 1, coordinatesY + 2);
                isIllegal = moves.checkIfMoveIsIllegal(topLeftCoordinates);
                if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                    System.out.printf("Up left -> %s\n", topLeftCoordinates);
                }
                hasToBreak = false;
            }
            if (coordinatesX < 8) {
                String topRightCoordinates = getCoordinatesToString(coordinatesX + 1, coordinatesY + 2);
                isIllegal = moves.checkIfMoveIsIllegal(topRightCoordinates);
                if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                    System.out.printf("Up right -> %s\n", topRightCoordinates);
                }
                hasToBreak = false;
            }
        }

        if (coordinatesX > 2) {
            if (coordinatesY > 1) {
                String leftDownCoordinates = getCoordinatesToString(coordinatesX - 2, coordinatesY - 1);
                isIllegal = moves.checkIfMoveIsIllegal(leftDownCoordinates);
                if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                    System.out.printf("Left down -> %s\n", leftDownCoordinates);
                }
                hasToBreak = false;
            }
            if (coordinatesY < 8) {
                String leftTopCoordinates = getCoordinatesToString(coordinatesX - 2, coordinatesY + 1);
                isIllegal = moves.checkIfMoveIsIllegal(leftTopCoordinates);
                if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                    System.out.printf("Left up -> %s\n", leftTopCoordinates);
                }
                hasToBreak = false;
            }
        }

        if (coordinatesX < 7) {
            if (coordinatesY < 8) {
                String rightTopCoordinates = getCoordinatesToString(coordinatesX + 2, coordinatesY + 1);
                isIllegal = moves.checkIfMoveIsIllegal(rightTopCoordinates);
                if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                    System.out.printf("Right up -> %s\n", rightTopCoordinates);
                }
                hasToBreak = false;
            }
            if (coordinatesY > 1) {
                String rightDownCoordinates = getCoordinatesToString(coordinatesX + 2, coordinatesY - 1);
                isIllegal = moves.checkIfMoveIsIllegal(rightDownCoordinates);
                if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                    System.out.printf("Right down -> %s\n", rightDownCoordinates);
                }
                hasToBreak = false;
            }
        }

        if (coordinatesY > 2) {
            if (coordinatesX > 1) {
                String downLeftCoordinates = getCoordinatesToString(coordinatesX - 1, coordinatesY - 2);
                isIllegal = moves.checkIfMoveIsIllegal(downLeftCoordinates);
                if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                    System.out.printf("Down left -> %s\n", downLeftCoordinates);
                }
                hasToBreak = false;
            }
            if (coordinatesX < 8) {
                String downRightCoordinates = getCoordinatesToString(coordinatesX + 1, coordinatesY - 2);
                isIllegal = moves.checkIfMoveIsIllegal(downRightCoordinates);
                if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                    System.out.printf("Down right -> %s\n", downRightCoordinates);
                }
                hasToBreak = false;
            }
        }
    }
}

