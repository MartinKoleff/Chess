package com.koleff.chess.Board;

import com.koleff.chess.Threads.PawnPromotionRunnable;
import com.koleff.chess.Threads.PawnPromotionThread;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import com.koleff.chess.CoordinatesAndMoves.Moves;
import com.koleff.chess.Pieces.*;
import com.koleff.chess.Player.Player;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.koleff.chess.Board.Board.paintBoard;
import static com.koleff.chess.CoordinatesAndMoves.Coordinates.*;
import static com.koleff.chess.CoordinatesAndMoves.Moves.*;
import static com.koleff.chess.MainMenu.Controller.chessBoardHeight;
import static com.koleff.chess.MainMenu.Controller.chessBoardWidth;


public class ChessBoardController implements Initializable {
    /**
     * Fields
     */
    @FXML
    protected GridPane gridPane;
    @FXML
    protected BorderPane gameWindow;
    public static final int cellWidth = chessBoardWidth / 8; //make non-static (give to constructors...)
    public static final int cellHeight = chessBoardHeight / 8;

    public static Player whitePlayer = new Player(Colour.WHITE);
    public static Player blackPlayer = new Player(Colour.BLACK);
    public static Player currentPlayer = whitePlayer;
    public static Player nextTurnPlayer = blackPlayer;

    public static Board board;
    public static Moves moves;

    /**
     * Functions
     */

    /**
     * This method is called upon fxml load (before the program loads)
     */
    public void initialize(URL location, ResourceBundle resources) {
        gridPane.setAlignment(Pos.CENTER_LEFT);

        paintBoard(gridPane);

        board = new Board();
        moves = new Moves();

//        board.arrangeTestingBoard();
        board.arrangeBoard();

        System.out.println(currentPlayer.getPlayerPiecesColor() + "'s Player Turn.");
    }

    /**
     * Select logic (Triggered on every mouse press)
     */
    public void selectPiece(MouseEvent mouseEvent) {
        String coordinates = locatePiece(mouseEvent);

        //If piece is already selected -> move
        if (moves.getSelectedPiece() != null) {
            movePieceByClicking(mouseEvent, moves.getSelectedPiece());
            return;
        } else {
            moves.getLegalMovesList().clear();
        }

        if (!moves.getChessPiecesMap().containsKey(coordinates)) {
            moves.setSelectedPiece(null);

            board.updateBoard();
            System.out.printf("No piece on square %s! Try again.\n", coordinates);
        } else {
            if (!moves.getChessPiecesMap().get(coordinates).getColor().equals(currentPlayer.getPlayerPiecesColor())) { //If piece is the color of the player continue. Otherwise, try again
                System.out.println("You have selected pieces from wrong color! Please try to select piece from your color!\n");
                return;
            }

            //Piece is selected logic...
            Piece piece = moves.getChessPiecesMap().get(coordinates);
            moves.setSelectedPiece(piece);

            //Protection of enemy pieces
            moves.calculateProtection(nextTurnPlayer.getPlayerPiecesColor());

            moves.calculateAttackingMoves(nextTurnPlayer.getPlayerPiecesColor());
            moves.getSelectedPiece().move();

            //If piece doesn't have moves -> don't have to double-click / wait for clicking again
            if (moves.getLegalMovesList().isEmpty()) {
                moves.setSelectedPiece(null);
            }
        }
    }

    /**
     * Move the pieces by clicking on the legal moves.
     */
    public void movePieceByClicking(MouseEvent mouseEvent, Piece piece) {
        String newCoordinates = locatePiece(mouseEvent);
        char newCoordinatesX = newCoordinates.charAt(0);
        int newCoordinatesY = Integer.parseInt(String.valueOf(newCoordinates.charAt(1)));

        //Move is legal
        if (moves.getLegalMovesList().contains(newCoordinates)) {

            //Castle
            if (moves.getSelectedPiece() instanceof King && !currentPlayer.hasCastled && currentPlayer.canCastle && moves.castlingMovesList.contains(newCoordinates)
                    && !moves.getSelectedPiece().hasMoved) {
                castle(newCoordinates);
            }
            piece.hasMoved = true;

            //En passant setter
            if (piece instanceof Pawn) {
                if (newCoordinatesY - 2 == piece.getCoordinatesY() || newCoordinatesY + 2 == piece.getCoordinatesY()) {
                    ((Pawn) piece).hasDoubleMoved = true;
                }

                //Promotion
                if (newCoordinatesY == 8 || newCoordinatesY == 1) {
                    PawnPromotionRunnable pawnPromotionRunnable = new PawnPromotionRunnable((Pawn) piece, newCoordinates);
                    PawnPromotionThread pawnPromotionThread = new PawnPromotionThread(pawnPromotionRunnable);
                    pawnPromotionThread.start();
                }
            }

            try {
               if(!((Pawn)piece).hasPromoted && piece instanceof Pawn){ //SHOULD NOT GO IN IF PROMOTING...
                    throw new ClassCastException();
                }
            }catch (ClassCastException e){
                moves.getChessPiecesMap().put(newCoordinates, piece);
                moves.getChessPiecesMap().remove(piece.getCoordinates());

                piece.setCoordinates(newCoordinates);
            }

            if (piece instanceof King) {
                piece.setCoordinates(newCoordinates);
            }

            //Has done En Passant
            if (enPassantCoordinatesList.contains(newCoordinates) && moves.getSelectedPiece() instanceof Pawn) { //newCoordinates.equals(enPassantSquare)
                enPassantEnemyPawnSquare = enPassantEnemyPawnCoordinatesList.stream()
                        .filter(e -> e.charAt(0) == newCoordinatesX).collect(Collectors.joining());
                ((Pawn) piece).hasDoubleMoved = false;
                moves.getChessPiecesMap().remove(enPassantEnemyPawnSquare);
                enPassantSquare = null;
            }

            //If the enemy king is in check
            if (nextTurnPlayer.checkForKingChecks()) {
                    nextTurnPlayer.isInCheck = true;
//                moves.calculateProtection(currentPlayer.getPlayerPiecesColor());

                //Checkmate
                checkForCheckmate();

                //End the game... (currentPlayer wins)
                if (nextTurnPlayer.isCheckmated) {
                    nextTurnPlayer.getPlayerKing();
                    System.out.printf("%s's king is checkmated.\n", nextTurnPlayer.getPlayerPiecesColor());
                }
            } else {
                nextTurnPlayer.isInCheck = false;

                //Stalemate
                checkForStalemate();

                //End of game... (draw)
                if (nextTurnPlayer.isStalemated) {
                    System.out.printf("%s's king is stalemated.\n", nextTurnPlayer.getPlayerPiecesColor());
                }
            }

            //Resetting protection of enemy pieces...
            moves.clearProtection(nextTurnPlayer.getPlayerPiecesColor());

            //Switches player, resets turns and resets En passant
            if (currentPlayer.getPlayerPiecesColor().equals(Colour.WHITE)) {
                currentPlayer = blackPlayer;
                nextTurnPlayer = whitePlayer;
                blackPlayer.setPlayerTurn(true);
                whitePlayer.setPlayerTurn(false);
                resetEnPassantAfterTurn(Colour.BLACK);
            } else {
                currentPlayer = whitePlayer;
                nextTurnPlayer = blackPlayer;
                whitePlayer.setPlayerTurn(true);
                blackPlayer.setPlayerTurn(false);
                resetEnPassantAfterTurn(Colour.WHITE);
            }
            System.out.println("\n" + currentPlayer.getPlayerPiecesColor() + "'s Player Turn.");
        } else {
            System.out.println("The move you are trying to make is illegal!");
        }

        //Resetting variables
        moves.setSelectedPiece(null);
        enPassantCoordinatesList.clear();
        enPassantEnemyPawnCoordinatesList.clear();
        moves.getAttackingMovesList().clear();
        moves.getLegalMovesList().clear();
        board.updateBoard();
    }

    /**
     * Checks if game has ended in a draw (Stalemate)
     */
    //3 WAY REPETITION
    //50 TURN RULE
    private void checkForStalemate() {
        /** NO MOVES OF ANY PIECE (STALEMATE)
         * - no moves for the enemy king
         * - enemy King is not in check
         * - all of enemy pieces can't move*/
        isCalculatingStalemate = true;
        moves.getLegalMovesList().clear();

        List<Piece> enemyPlayerPieces = nextTurnPlayer.getPieces();
        Piece selectedPieceTemp = moves.getSelectedPiece();
        for (Piece piece : enemyPlayerPieces) {
            moves.setSelectedPiece(piece);
            piece.move();

            if (moves.getLegalMovesList().size() > 0) {
                isCalculatingStalemate = false;
                return;
            }
        }
        moves.setSelectedPiece(selectedPieceTemp);

        if (moves.getLegalMovesList().size() == 0 && !nextTurnPlayer.isInCheck) {
            nextTurnPlayer.isStalemated = true;
        }
        isCalculatingStalemate = false;
    }

    /**
     *  Checks if game has ended in a win for the current player (Checkmate)
     */
    private void checkForCheckmate() {
        isCalculatingCheckmate = true;
        moves.getLegalMovesList().clear();

        moves.calculateProtection(nextTurnPlayer.getPlayerPiecesColor());

        List<Piece> enemyPlayerPieces = nextTurnPlayer.getPieces();
        Piece selectedPieceTemp = moves.getSelectedPiece();
        for (Piece piece : enemyPlayerPieces) {
            moves.setSelectedPiece(piece);
            piece.move();
        }
        moves.setSelectedPiece(selectedPieceTemp);

        if (moves.getLegalMovesList().size() == 0) {
            nextTurnPlayer.isCheckmated = true;
        }
        isCalculatingCheckmate = false;
    }

    /**
     * Castling
     * - Only replaces the rook (the King moves to its correct place in movePiece())
     */
    private void castle(String castlingCoordinates) {
        Rook rook;

        switch (moves.getSelectedPiece().getColor()) {
            case BLACK -> {
                if (castlingCoordinates.toCharArray()[0] == 'b') { //Short castle
                    rook = (Rook) moves.getChessPiecesMap().get("a1");
                    rook.setCoordinates("c1");
                    moves.getChessPiecesMap().remove("a1");
                    moves.getChessPiecesMap().put("c1", rook);
                } else if (castlingCoordinates.toCharArray()[0] == 'f') { //Long castle
                    rook = (Rook) moves.getChessPiecesMap().get("h1");
                    rook.setCoordinates("e1");
                    moves.getChessPiecesMap().remove("h1");
                    moves.getChessPiecesMap().put("e1", rook);
                }
                blackPlayer.hasCastled = true;
                blackPlayer.canCastle = false;
                break;
            }
            case WHITE -> {
                if (castlingCoordinates.toCharArray()[0] == 'b') { //Short castle
                    rook = (Rook) moves.getChessPiecesMap().get("a8");
                    rook.setCoordinates("c8");
                    moves.getChessPiecesMap().remove("a8");
                    moves.getChessPiecesMap().put("c8", rook);
                } else if (castlingCoordinates.toCharArray()[0] == 'f') { //Long castle
                    rook = (Rook) moves.getChessPiecesMap().get("h8");
                    rook.setCoordinates("e8");
                    moves.getChessPiecesMap().remove("h8");
                    moves.getChessPiecesMap().put("e8", rook);
                }
                whitePlayer.hasCastled = true;
                whitePlayer.canCastle = false;
                break;
            }
        }
    }


    /**
     * Resets En Passant every turn
     * If function throws exception -> there is a pawn that is not set as new Pawn in Board class!
     */
    public void resetEnPassantAfterTurn(Colour colour) {
    /**CONVERT TO STREAM API....*/
        for (Piece piece : moves.getChessPiecesMap().values()) {
            if (piece instanceof Pawn && piece.getColor().equals(colour)) {
                ((Pawn) piece).hasDoubleMoved = false;
            }
        }
    }

    /**
     * Gets the square of the mouse press
     */
    public String locatePiece(MouseEvent mouseEvent) {
        char coordinatesX = calculateX(mouseEvent);
        int coordinatesY = calculateY(mouseEvent);
        String coordinates = getCoordinatesToString(coordinatesX, coordinatesY);

        return coordinates;
    }
}
