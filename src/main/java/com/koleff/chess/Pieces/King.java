package com.koleff.chess.Pieces;

import java.util.LinkedHashMap;

import static com.koleff.chess.Board.ChessBoardController.moves;
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
    public  <T extends Piece> void checkForCastling(Colour piecesColor) {
        LinkedHashMap<String, T> chessPiecesMap = ((LinkedHashMap<String, T>) chessPiecesMapMediator.getData()); /**To test...*/

        moves.calculateAttackingMoves(nextTurnPlayer.getPlayerPiecesColor());

        if (castlingMovesList.isEmpty()) {
            castlingMovesList.add("b1");
            castlingMovesList.add("b8");
            castlingMovesList.add("f1");
            castlingMovesList.add("f8");
        }
        try {
            switch (piecesColor) {
                case WHITE -> {
                    //Short castle (king side)
                    if (!kingIsChecked && selectedPieceMediator.getData() instanceof King
                            && chessPiecesMap.containsKey("a8")
                            && !chessPiecesMap.get("a8").hasMoved
                            && !chessPiecesMap.get("d8").hasMoved
                            && !chessPiecesMap.containsKey("b8")
                            && !chessPiecesMap.containsKey("c8")
                            && !attackingMovesList.contains("b8")
                            && !attackingMovesList.contains("c8")) {

                        System.out.println("Short Castle -> b8");
                        showLegalMove("b8");
                        whitePlayer.canCastle = true;
                    }
                    //Long castle (queen side)
                    if (!kingIsChecked && selectedPieceMediator.getData() instanceof King
                            && chessPiecesMap.containsKey("h8")
                            && !chessPiecesMap.get("h8").hasMoved
                            && !chessPiecesMap.get("d8").hasMoved
                            && !chessPiecesMap.containsKey("e8")
                            && !chessPiecesMap.containsKey("f8")
                            && !chessPiecesMap.containsKey("g8")
                            && !attackingMovesList.contains("e8")
                            && !attackingMovesList.contains("f8")
                            && !attackingMovesList.contains("g8")) {

                        System.out.println("Long Castle -> f8");
                        showLegalMove("f8");
                        whitePlayer.canCastle = true;
                    }
                    break;
                }
                case BLACK -> {
                    //Short castle (king side)
                    if (!kingIsChecked && selectedPieceMediator.getData() instanceof King
                            && chessPiecesMap.containsKey("a1")
                            && !chessPiecesMap.get("a1").hasMoved
                            && !chessPiecesMap.get("d1").hasMoved
                            && !chessPiecesMap.containsKey("b1")
                            && !chessPiecesMap.containsKey("c1")
                            && !attackingMovesList.contains("b1")
                            && !attackingMovesList.contains("c1")) {

                        System.out.println("Short Castle -> b1");
                        showLegalMove("b1");
                        blackPlayer.canCastle = true;
                    }
                    //Long castle (queen side)
                    if (!kingIsChecked && selectedPieceMediator.getData() instanceof King
                            && chessPiecesMap.containsKey("h1")
                            && !chessPiecesMap.get("h1").hasMoved
                            && !chessPiecesMap.get("d1").hasMoved
                            && !chessPiecesMap.containsKey("e1")
                            && !chessPiecesMap.containsKey("f1")
                            && !chessPiecesMap.containsKey("g1")
                            && !attackingMovesList.contains("e1")
                            && !attackingMovesList.contains("f1")
                            && !attackingMovesList.contains("g1")) {

                        System.out.println("Long Castle -> f1");
                        showLegalMove("f1");
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