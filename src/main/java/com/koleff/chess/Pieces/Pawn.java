package com.koleff.chess.Pieces;

import com.koleff.chess.Board.PawnPromotionController;
import com.koleff.chess.CoordinatesAndMoves.Coordinates;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import static com.koleff.chess.Board.ChessBoardController.*;
import static com.koleff.chess.CoordinatesAndMoves.Coordinates.getCoordinatesToString;
import static com.koleff.chess.CoordinatesAndMoves.Moves.*;
import static com.koleff.chess.MainMenu.Controller.chessBoardStage;

public class Pawn extends Piece {
    /**
     * Fields
     */
    public boolean hasDoubleMoved = false;
    public boolean hasPromoted = false;
    private static Stage pawnPromotionStage;
    /**
     * Constructors
     */

    /**
     * @param coordinatesX
     * @param coordinatesY
     * @param pieceColor
     */
    public Pawn(char coordinatesX, int coordinatesY, Colour pieceColor) {
        super(coordinatesX, coordinatesY, pieceColor);
        hasMoved = false;
    }


    public Pawn(Piece piece) {
        super(piece.getCoordinatesXChar(), piece.getCoordinatesY(), piece.getColor());
        this.hasDoubleMoved = ((Pawn) piece).hasDoubleMoved;
    }

    public Pawn(char coordinatesX, int coordinatesY, Colour pieceColor, boolean isProtected, boolean hasMoved, boolean hasDoubleMoved) {
        super(coordinatesX, coordinatesY, pieceColor);
        this.isProtected = isProtected;
        this.hasDoubleMoved = hasDoubleMoved;
        this.hasMoved = hasMoved;
    }

    /**
     * Functions
     */
    public void promote(String coordinates) {
        moves.getChessPiecesMap().remove(this.getCoordinates());

        this.hasPromoted = true;

        //Open pawnPromotionMenu.fxml ...
        openPawnPromotion(coordinates);
    }

    /**
     * Menu with all figures (except King and Pawn) from which the Pawn turns into when it reaches the back rank (y = 1 or y = 8)
     */
    private synchronized void openPawnPromotion(String coordinates) {
        Parent pawnPromotionRoot = null;
        try {
            pawnPromotionRoot = FXMLLoader.load(PawnPromotionController.class.getResource("/com.koleff.chess/pawnPromotionMenu.fxml")); ///com.koleff.chess/
        } catch (IOException e) {
            e.printStackTrace();
        }
        pawnPromotionStage = new Stage();
        pawnPromotionStage.setTitle("");

        //Cant make object because fxml controllers cant have constructors...
        PawnPromotionController.setPiece(coordinates, this);
        PawnPromotionController.setStage(pawnPromotionStage);

        pawnPromotionStage.setScene(new Scene(pawnPromotionRoot, cellWidth, 400));
        pawnPromotionStage.setResizable(false);
        pawnPromotionStage.initStyle(StageStyle.UNDECORATED);

        switch (currentPlayer.getPlayerPiecesColor()) {
            case WHITE -> {
                pawnPromotionStage.getIcons().add(new Image(getClass().getResourceAsStream("/com.koleff.chess/pictures/white_pawn.png")));
                pawnPromotionStage.setY(425); //Bottom of the screen + 400 for the pieces
            }
            case BLACK -> {
                pawnPromotionStage.getIcons().add(new Image(getClass().getResourceAsStream("/com.koleff.chess/pictures/black_pawn.png")));
                pawnPromotionStage.setY(25); //0
            }
        }

        pawnPromotionStage.setX(chessBoardStage.getX() + (Coordinates.calculateX(coordinates.charAt(0)) * cellWidth - 100 + 7));

        pawnPromotionStage.toFront();
        pawnPromotionStage.showAndWait();
    }

    @Override
    public Pawn copy() {
        return new Pawn(coordinatesX, coordinatesY, pieceColor, isProtected, hasMoved, hasDoubleMoved);
    }

    @Override
    public void move() {
        int coordinatesX = this.getCoordinatesXInt();
        int coordinatesY = this.getCoordinatesY();

        String coordinatesUpperSquare;
        String coordinatesUpperLeftSquare = null;
        String coordinatesUpperRightSquare = null;
        String coordinatesEnPassantRightSquare = null;
        String coordinatesEnPassantLeftSquare = null;
        boolean isIllegal;

        //BLACK UP
        if (this.getColor().equals(Colour.BLACK)) {
            coordinatesUpperSquare = getCoordinatesToString(coordinatesX, coordinatesY + 1);

            if (coordinatesX - 1 > 0) {
                coordinatesUpperLeftSquare = getCoordinatesToString(coordinatesX - 1, coordinatesY + 1);
                coordinatesEnPassantLeftSquare = getCoordinatesToString(coordinatesX - 1, coordinatesY);
            }
            if (coordinatesX + 1 <= 8) {
                coordinatesUpperRightSquare = getCoordinatesToString(coordinatesX + 1, coordinatesY + 1);
                coordinatesEnPassantRightSquare = getCoordinatesToString(coordinatesX + 1, coordinatesY);
            }

            if (!isCalculatingAttackingMoves && !isCalculatingProtection) {
                isIllegal = moves.checkIfMoveIsIllegal(coordinatesUpperSquare);
                if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                    System.out.printf("Up -> %s\n", coordinatesUpperSquare);
                }
                if (!this.hasMoved) {
                    String coordinatesDoubleUpperSquare = getCoordinatesToString(coordinatesX, coordinatesY + 2);

                    isIllegal = moves.checkIfMoveIsIllegal(coordinatesDoubleUpperSquare);
                    if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                        System.out.printf("Up -> %s\n", coordinatesDoubleUpperSquare);
                    }
                }
            }
            hasToBreak = false;
        } else { //WHITE DOWN
            coordinatesX = this.getCoordinatesXInt();
            coordinatesY = this.getCoordinatesY();

            coordinatesUpperSquare = getCoordinatesToString(coordinatesX, coordinatesY - 1);

            if (coordinatesX - 1 > 0) {
                coordinatesUpperLeftSquare = getCoordinatesToString(coordinatesX - 1, coordinatesY - 1);
                coordinatesEnPassantLeftSquare = getCoordinatesToString(coordinatesX - 1, coordinatesY);
            }
            if (coordinatesX + 1 <= 8) {
                coordinatesUpperRightSquare = getCoordinatesToString(coordinatesX + 1, coordinatesY - 1);
                coordinatesEnPassantRightSquare = getCoordinatesToString(coordinatesX + 1, coordinatesY);
            }

            if (!isCalculatingAttackingMoves && !isCalculatingProtection) {
                isIllegal = moves.checkIfMoveIsIllegal(coordinatesUpperSquare);
                if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                    System.out.printf("Up -> %s\n", coordinatesUpperSquare);
                }
                if (!this.hasMoved) {
                    String coordinatesDoubleUpperSquare = getCoordinatesToString(coordinatesX, coordinatesY - 2);

                    isIllegal = moves.checkIfMoveIsIllegal(coordinatesDoubleUpperSquare);
                    if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                        System.out.printf("Up -> %s\n", coordinatesDoubleUpperSquare);
                    }
                }
            }
        }
        hasToBreak = false;
        if (moves.getChessPiecesMap().containsKey(coordinatesUpperLeftSquare) || (isCalculatingAttackingMoves && coordinatesUpperLeftSquare != null)) {
            isIllegal = moves.checkIfMoveIsIllegal(coordinatesUpperLeftSquare);
            if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                System.out.printf("Left -> %s\n", coordinatesUpperLeftSquare);
            }
        }
        hasToBreak = false;
        if (moves.getChessPiecesMap().containsKey(coordinatesUpperRightSquare) || (isCalculatingAttackingMoves && coordinatesUpperRightSquare != null)) {
            isIllegal = moves.checkIfMoveIsIllegal(coordinatesUpperRightSquare);
            if (!isIllegal && !isCalculatingAttackingMoves && !isCalculatingKingDiscoveryFromAllyPiece && !calculatingIfPieceProtectsKing && !isCalculatingProtection) {
                System.out.printf("Right -> %s\n", coordinatesUpperRightSquare);
            }
        }
        hasToBreak = false;

        /**En Passant */
        if (!isCalculatingAttackingMoves && !isCalculatingProtection) {
            checkForEnPassantOnTheLeft(coordinatesEnPassantLeftSquare, coordinatesY);
            checkForEnPassantOnTheRight(coordinatesEnPassantRightSquare, coordinatesY);
        }
        hasToBreak = false;
    }

    /**
     * Helping function for pawnMovesLocator()
     * Checks for En Passant on the left
     */
    public void checkForEnPassantOnTheLeft(String coordinatesEnPassantLeftSquare, int coordinatesY) {
        try {
            if (moves.getChessPiecesMap().get(coordinatesEnPassantLeftSquare) instanceof Pawn) { //En passant on the left
                if (moves.getChessPiecesMap().get(coordinatesEnPassantLeftSquare).getColor().equals(Colour.BLACK)
                        && (moves.getSelectedPiece().getColor().equals(Colour.WHITE))) {
                    enPassantSquare = getCoordinatesToString(this.getCoordinatesXInt() - 1, coordinatesY - 1);
                    enPassantEnemyPawnSquare = getCoordinatesToString(this.getCoordinatesXInt() - 1, coordinatesY);
                } else if (moves.getChessPiecesMap().get(coordinatesEnPassantLeftSquare).getColor().equals(Colour.WHITE)
                        && (moves.getSelectedPiece().getColor().equals(Colour.BLACK))) {
                    enPassantSquare = getCoordinatesToString(this.getCoordinatesXInt() - 1, coordinatesY + 1);
                    enPassantEnemyPawnSquare = getCoordinatesToString(this.getCoordinatesXInt() - 1, coordinatesY);
                }
                checkForEnPassant(coordinatesEnPassantLeftSquare);
            }
        } catch (RuntimeException e) {
            return;
        }
    }

    /**
     * Helping function for pawnMovesLocator()
     * Checks for En Passant on the right
     */
    public void checkForEnPassantOnTheRight(String coordinatesEnPassantRightSquare, int coordinatesY) {
        try {
            if (moves.getChessPiecesMap().get(coordinatesEnPassantRightSquare) instanceof Pawn) { //En passant on the right
                if (moves.getChessPiecesMap().get(coordinatesEnPassantRightSquare).getColor().equals(Colour.BLACK)
                        && (moves.getSelectedPiece().getColor().equals(Colour.WHITE))) {
                    enPassantSquare = getCoordinatesToString(this.getCoordinatesXInt() + 1, coordinatesY - 1);
                    enPassantEnemyPawnSquare = getCoordinatesToString(this.getCoordinatesXInt() + 1, coordinatesY);
                } else if (moves.getChessPiecesMap().get(coordinatesEnPassantRightSquare).getColor().equals(Colour.WHITE)
                        && (moves.getSelectedPiece().getColor().equals(Colour.BLACK))) {
                    enPassantSquare = getCoordinatesToString(this.getCoordinatesXInt() + 1, coordinatesY + 1);
                    enPassantEnemyPawnSquare = getCoordinatesToString(this.getCoordinatesXInt() + 1, coordinatesY);
                }
                checkForEnPassant(coordinatesEnPassantRightSquare);
            }
        } catch (RuntimeException e) {
            return;
        }
    }


    /**
     * Helping function for pawnMovesLocator()
     * Shows En Passant on the board if the move is legal
     */
    private void checkForEnPassant(String coordinatesEnPassantLeftOrRightSquare) {
        if (!moves.getChessPiecesMap().containsKey(enPassantSquare) && enPassantSquare != null) {
            if (((Pawn) moves.getChessPiecesMap().get(coordinatesEnPassantLeftOrRightSquare)).hasDoubleMoved) {
                enPassantCoordinatesList.add(enPassantSquare);
                enPassantEnemyPawnCoordinatesList.add(enPassantEnemyPawnSquare);


                //Check for discovery...
                Pawn enemyPawn = (Pawn) moves.getChessPiecesMap().get(enPassantEnemyPawnSquare);
                moves.getChessPiecesMap().remove(enPassantEnemyPawnSquare);
                moves.getChessPiecesMap().remove(moves.getSelectedPiece().getCoordinates());
                moves.getChessPiecesMap().put(enPassantSquare, moves.getSelectedPiece());

                if (!currentPlayer.checkForKingChecks()) {
                    moves.showLegalMove(enPassantSquare);
                    System.out.printf("En Passant -> %s\n", enPassantSquare);
                }

                moves.getChessPiecesMap().put(enemyPawn.getCoordinates(), enemyPawn);
                moves.getChessPiecesMap().remove(enPassantSquare);
                moves.getChessPiecesMap().put(moves.getSelectedPiece().getCoordinates(), moves.getSelectedPiece());
            }
        }
    }
}
