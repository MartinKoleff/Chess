package com.koleff.chess.Pieces;

import com.koleff.chess.PawnPromotionWindow.PawnPromotionController;
import com.koleff.chess.CoordinatesAndMoves.Coordinates;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import static com.koleff.chess.BoardAndFEN.ChessBoardController.*;
import static com.koleff.chess.CoordinatesAndMoves.Coordinates.getCoordinatesToString;
import static com.koleff.chess.CoordinatesAndMoves.Moves.*;
import static com.koleff.chess.MainMenu.Controller.chessBoardStage;

public class Pawn extends Piece {
    /**
     * Fields
     */
    public boolean hasDoubleMoved = false;
    public boolean hasPromoted = false;
    private String enPassantSquare; //The square behind the pawn (where the opponents pawn will go after capturing by en passant)

    private static Stage pawnPromotionStage;

    /**
     * Constructors
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
     * Getters & Setters
     */
    public String getEnPassantSquare() {
        return enPassantSquare;
    }

    public void setEnPassantSquare(String enPassantSquare) {
        this.enPassantSquare = enPassantSquare;
    }

    /**
     * Promotion
     * - when the pawn reaches the back rank and becomes better piece
     * - the pawn can turn into rook, queen, bishop, knight
     * @param coordinates the coordinates of the back rank (where the pawn will be after it moves and promotes)
     */
    public void promote(String coordinates) {
        moves.getChessPiecesMap().remove(this.getCoordinates());

        this.hasPromoted = true;

        //Open pawnPromotionMenu.fxml ...
        openPawnPromotion(coordinates);
    }

    /**
     * Opens menu with all promotion figures
     * @param coordinates the coordinates of the back rank (where the pawn will be after it moves and promotes)
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

        pawnPromotionStage.setScene(new Scene(pawnPromotionRoot, CELL_WIDTH, 400));
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

        pawnPromotionStage.setX(chessBoardStage.getX() + (Coordinates.calculateX(coordinates.charAt(0)) * CELL_WIDTH - 100 + 7));

        pawnPromotionStage.toFront();
        pawnPromotionStage.showAndWait();
    }

    @Override
    public Pawn copy() {
        return new Pawn(coordinatesX, coordinatesY, pieceColor, isProtected, hasMoved, hasDoubleMoved);
    }

    @Override
    public void move() {
        int coordinatesX;
        int coordinatesY;

        String coordinatesUpperSquare;
        String coordinatesUpperLeftSquare = null;
        String coordinatesUpperRightSquare = null;
        String coordinatesEnPassantRightSquare = null;
        String coordinatesEnPassantLeftSquare = null;
        boolean isIllegal;

        //BLACK UP
        if (this.getColor().equals(Colour.BLACK)) {
            coordinatesX = this.getCoordinatesXInt();
            coordinatesY = this.getCoordinatesY();

            setEnPassantSquare();
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

            setEnPassantSquare();
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

        //En Passant
        if (!isCalculatingAttackingMoves && !isCalculatingProtection) {
            checkForEnPassant(coordinatesEnPassantLeftSquare);
            checkForEnPassant(coordinatesEnPassantRightSquare);
        }
        hasToBreak = false;
    }

    /**
     * Used to set the en passant square
     * @see #enPassantSquare
     */
    private void setEnPassantSquare() {
        if (this.getColor().equals(Colour.WHITE) && coordinatesY + 1 < 8
                && !moves.getChessPiecesMap().containsKey(this.coordinatesX + "" + (this.coordinatesY + 1))) {
            this.enPassantSquare = this.coordinatesX + "" + (this.coordinatesY + 1);
        } else if (this.getColor().equals(Colour.BLACK) && coordinatesY - 1 > 0
                && !moves.getChessPiecesMap().containsKey(this.coordinatesX + "" + (this.coordinatesY - 1))) {
            this.enPassantSquare = this.coordinatesX + "" + (this.coordinatesY - 1);
        }
    }

    /**
     * Checks for En Passant
     *
     * @param enemyPawnSquare the square of the enemy pawn (that can be taken with en passant)
     */
    public void checkForEnPassant(String enemyPawnSquare) {
        try {
            if (((Pawn) moves.getChessPiecesMap().get(enemyPawnSquare)).hasDoubleMoved
                    && nextTurnPlayer.containsEnPassantPawn()) {

                //Check for discovery...
                Pawn enemyPawn = nextTurnPlayer.getEnPassantPawn();
                moves.getChessPiecesMap().remove(enemyPawn.getCoordinates());
                moves.getChessPiecesMap().remove(moves.getSelectedPiece().getCoordinates());
                moves.getChessPiecesMap().put(enemyPawn.enPassantSquare, moves.getSelectedPiece());

                if (!currentPlayer.checkForKingChecks()) {
                    moves.showLegalMove(enemyPawn.enPassantSquare);
                    System.out.printf("En Passant -> %s\n", enemyPawn.enPassantSquare);
                }

                moves.getChessPiecesMap().put(enemyPawn.getCoordinates(), enemyPawn);
                moves.getChessPiecesMap().remove(enemyPawn.enPassantSquare);
                moves.getChessPiecesMap().put(moves.getSelectedPiece().getCoordinates(), moves.getSelectedPiece());
            }
        } catch (NullPointerException e) {
            return;
        } catch (RuntimeException e) {
            return;
        }
    }
}
