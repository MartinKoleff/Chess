package com.koleff.chess.Pieces;

import static com.koleff.chess.Board.ChessBoardController.*;
import static com.koleff.chess.CoordinatesAndMoves.Coordinates.getCoordinatesToString;
import static com.koleff.chess.CoordinatesAndMoves.Moves.*;

public class King extends Piece {
    /**
     * Fields
     */
    private boolean isInCheck = false;


    /**
     * Constructors
     */

    /**
     * @param coordinatesX
     * @param coordinatesY
     * @param pieceColor
     */
    public King(char coordinatesX, int coordinatesY, Colour pieceColor) {
        super(coordinatesX, coordinatesY, pieceColor);
        hasMoved = false;
    }

    public King(King piece) { //change Piece to King...
        super(piece.getCoordinatesXChar(), piece.getCoordinatesY(), piece.getColor());
        this.isInCheck = piece.isInCheck;
        this.hasMoved = piece.hasMoved;
    }

    public King(char coordinatesX, int coordinatesY, Colour pieceColor, boolean isProtected, boolean hasMoved, boolean isInCheck) {
        super(coordinatesX, coordinatesY, pieceColor);
        this.isProtected = isProtected;
        this.hasMoved = hasMoved;
        this.isInCheck = isInCheck;
    }

    /**
     * Functions
     */

    public void setIsInCheck(boolean isInCheck) {
        this.isInCheck = isInCheck;
    }

    public boolean isInCheck() {
        return isInCheck;
    }

    @Override
    public King copy() {
        return new King(coordinatesX, coordinatesY, pieceColor, isProtected, hasMoved, isInCheck);
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

        if (!isCalculatingAttackingMoves && !isCalculatingProtection && !currentPlayer.hasCastled && !currentPlayer.getPlayerKing().isInCheck()) {
            checkForCastling(currentPlayer.getPlayerPiecesColor());
        }
    }

    /**
     * Legal requirements for castling to work:
     * - Checks if the selected piece is instanceof King
     * - Check for if the squares to the left / right are empty (no pieces between king and rook)
     * - Checks if king is in check
     * - Check if the squares are also not in attackingMovesList (between a1-kingCoordinates for Short castle
     * and kingCoordinates-h1  for Long castle) (a1 and h1 not included)
     */
    public void checkForCastling(Colour piecesColor) {
        moves.calculateAttackingMoves(nextTurnPlayer.getPlayerPiecesColor());

        if (moves.castlingMovesList.isEmpty()) {
            moves.castlingMovesList.add("b1");
            moves.castlingMovesList.add("b8");
            moves.castlingMovesList.add("f1");
            moves.castlingMovesList.add("f8");
        }
        try {
            switch (piecesColor) {
                case WHITE -> {
                    //Short castle (king side)
                    if (!nextTurnPlayer.isInCheck && moves.getSelectedPiece()  instanceof King
                            && moves.getChessPiecesMap().containsKey("a8")
                            && !moves.getChessPiecesMap().get("a8").hasMoved
                            && !moves.getChessPiecesMap().get("d8").hasMoved
                            && !moves.getChessPiecesMap().containsKey("b8")
                            && !moves.getChessPiecesMap().containsKey("c8")
                            && !moves.getAttackingMovesList().contains("b8")
                            && !moves.getAttackingMovesList().contains("c8")) {

                        System.out.println("Short Castle -> b8");
                        moves.showLegalMove("b8");
                        whitePlayer.canCastle = true;
                    }
                    //Long castle (queen side)
                    if (!nextTurnPlayer.isInCheck && moves.getSelectedPiece()  instanceof King
                            && moves.getChessPiecesMap().containsKey("h8")
                            && !moves.getChessPiecesMap().get("h8").hasMoved
                            && !moves.getChessPiecesMap().get("d8").hasMoved
                            && !moves.getChessPiecesMap().containsKey("e8")
                            && !moves.getChessPiecesMap().containsKey("f8")
                            && !moves.getChessPiecesMap().containsKey("g8")
                            && !moves.getAttackingMovesList().contains("e8")
                            && !moves.getAttackingMovesList().contains("f8")
                            && !moves.getAttackingMovesList().contains("g8")) {

                        System.out.println("Long Castle -> f8");
                        moves.showLegalMove("f8");
                        whitePlayer.canCastle = true;
                    }
                    break;
                }
                case BLACK -> {
                    //Short castle (king side)
                    if (!nextTurnPlayer.isInCheck && moves.getSelectedPiece() instanceof King
                            && moves.getChessPiecesMap().containsKey("a1")
                            && !moves.getChessPiecesMap().get("a1").hasMoved
                            && !moves.getChessPiecesMap().get("d1").hasMoved
                            && !moves.getChessPiecesMap().containsKey("b1")
                            && !moves.getChessPiecesMap().containsKey("c1")
                            && !moves.getAttackingMovesList().contains("b1")
                            && !moves.getAttackingMovesList().contains("c1")) {

                        System.out.println("Short Castle -> b1");
                        moves.showLegalMove("b1");
                        blackPlayer.canCastle = true;
                    }
                    //Long castle (queen side)
                    if (!nextTurnPlayer.isInCheck && moves.getSelectedPiece()  instanceof King
                            && moves.getChessPiecesMap().containsKey("h1")
                            && !moves.getChessPiecesMap().get("h1").hasMoved
                            && !moves.getChessPiecesMap().get("d1").hasMoved
                            && !moves.getChessPiecesMap().containsKey("e1")
                            && !moves.getChessPiecesMap().containsKey("f1")
                            && !moves.getChessPiecesMap().containsKey("g1")
                            && !moves.getAttackingMovesList().contains("e1")
                            && !moves.getAttackingMovesList().contains("f1")
                            && !moves.getAttackingMovesList().contains("g1")) {

                        System.out.println("Long Castle -> f1");
                        moves.showLegalMove("f1");
                        blackPlayer.canCastle = true;
                    }
                    break;
                }
            }
        } catch (NullPointerException e) {
            if (currentPlayer.canCastle) {
                return;
            }
            currentPlayer.canCastle = false;
            return;
        }
    }
}