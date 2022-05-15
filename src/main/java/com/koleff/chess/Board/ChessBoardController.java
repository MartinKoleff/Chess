package com.koleff.chess.Board;

import com.koleff.chess.MainMenu.Main;
import com.koleff.chess.MediatorAndThreads.PawnPromotionRunnable;
import com.koleff.chess.MediatorAndThreads.PawnPromotionThread;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import com.koleff.chess.CoordinatesAndMoves.Moves;
import com.koleff.chess.MediatorAndThreads.Mediator;
import com.koleff.chess.Pieces.*;
import com.koleff.chess.Player.Player;

import java.net.URL;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static com.koleff.chess.Board.Board.paintBoard;
import static com.koleff.chess.CoordinatesAndMoves.Coordinates.*;
import static com.koleff.chess.CoordinatesAndMoves.Moves.*;
import static com.koleff.chess.MainMenu.Controller.chessBoardHeight;
import static com.koleff.chess.MainMenu.Controller.chessBoardWidth;


public class ChessBoardController<T extends Piece> implements Initializable {
    /**
     * Fields
     */
    @FXML
    protected GridPane gridPane;
    @FXML
    protected BorderPane gameWindow;

    public Map<String, T> chessPiecesMap = new LinkedHashMap(); //<K> String -> Coordinates (example: e4) | <V> Piece -> The Piece that is on the <K> coordinates
    protected static List<String> legalMovesList = new ArrayList<>(); //String -> coordinates
    public static List<String> attackingMovesList = new ArrayList<>(); //String -> coordinates

    public T selectedPiece = null;
    protected static boolean hasSelectedPiece = false;

    public static int cellWidth;
    protected static int cellHeight;
    public static Mediator chessPiecesMapMediator;
    public static Mediator selectedPieceMediator;

    public static Player whitePlayer = new Player(Colour.WHITE);
    public static Player blackPlayer = new Player(Colour.BLACK);
    public static Player currentPlayer = whitePlayer;
    public static Player nextTurnPlayer = blackPlayer;

    public static boolean kingIsChecked = false;
    public static boolean isCalculatingCheckmate = false;
    public static boolean isCalculatingStalemate = false;

    public static Board board;
    public static Moves moves;

    /**
     * Functions
     */

    /**
     * This method is called upon fxml load (before the program loads)
     */
    public void initialize(URL location, ResourceBundle resources) {
        setCellWidth();
        setCellHeight();
        gridPane.setAlignment(Pos.CENTER_LEFT);

        chessPiecesMapMediator = new Mediator(chessPiecesMap);
        selectedPieceMediator = new Mediator(selectedPiece);

        paintBoard(gridPane);

        board = new Board();
        moves = new Moves();

//        board.arrangeTestingBoard();
        board.arrangeBoard();

        System.out.println(currentPlayer.getPlayerPiecesColor() + "'s Player Turn.");
    }

    /**
     * Setters
     */

    private void setCellHeight() {
        cellHeight = chessBoardHeight / gridPane.getColumnCount();
    }

    private void setCellWidth() {
        cellWidth = chessBoardWidth / gridPane.getRowCount();
    }

    public void updateChessPiecesMap() {
        chessPiecesMap = ((LinkedHashMap<String, T>) chessPiecesMapMediator.getData());
    }

    /**
     * Select logic (Triggered on every mouse press)
     */
    public void selectPiece(MouseEvent mouseEvent) {
        String coordinates = locatePiece(mouseEvent);
        updateChessPiecesMap();

        //If piece is already selected -> don't reset the legal moves
        if (hasSelectedPiece) {
            movePieceByClicking(mouseEvent, selectedPiece);
            return;
        } else {
            legalMovesList.clear();
        }

        if (!chessPiecesMap.containsKey(coordinates)) {
            hasSelectedPiece = false;

            board.updateBoard();
            System.out.printf("No piece on square %s! Try again.\n", coordinates);
        } else {
            if (!chessPiecesMap.get(coordinates).getColor().equals(currentPlayer.getPlayerPiecesColor())) { //If piece is the color of the player continue. Otherwise try again
                System.out.println("You have selected pieces from wrong color! Please try to select piece from your color!\n");
                return;
            }

            //Piece is selected logic...
            hasSelectedPiece = true;

            T piece = chessPiecesMap.get(coordinates);
            selectedPiece = piece;
            selectedPieceMediator.setData(selectedPiece);

            //Protection of enemy pieces
            moves.calculateProtection(nextTurnPlayer.getPlayerPiecesColor());

            moves.calculateAttackingMoves(nextTurnPlayer.getPlayerPiecesColor());
            selectedPiece.move();

            /**Check if selectedPiece is the same after calculations...*/

            //If piece doesn't have moves -> dont have to double click (resets the wait)
            if (legalMovesList.isEmpty()) {
                hasSelectedPiece = false;
                selectedPiece = null;
            }
        }
    }

    /**
     * Move the pieces by clicking on the legal moves.
     */
    public void movePieceByClicking(MouseEvent mouseEvent, T piece) {
        String newCoordinates = locatePiece(mouseEvent);
        char newCoordinatesX = newCoordinates.charAt(0);
        int newCoordinatesY = Integer.parseInt(String.valueOf(newCoordinates.charAt(1)));

        //Move is legal
        if (legalMovesList.contains(newCoordinates)) {

            //Castle
            if (selectedPiece instanceof King && !currentPlayer.hasCastled && currentPlayer.canCastle && castlingMovesList.contains(newCoordinates)
                    && !selectedPiece.hasMoved) {
                castle(newCoordinates);
            }
            piece.hasMoved = true;

            //En passant setter
            if (piece instanceof Pawn) {
                if (newCoordinatesY - 2 == piece.getCoordinatesY() || newCoordinatesY + 2 == piece.getCoordinatesY()) {
                    ((Pawn) piece).setHasDoubleMoved(true);
                }

                //Promotion
                if (newCoordinatesY == 8 || newCoordinatesY == 1) {
                    PawnPromotionRunnable pawnPromotionRunnable = new PawnPromotionRunnable((Pawn) piece, newCoordinates);
                    PawnPromotionThread pawnPromotionThread = new PawnPromotionThread(pawnPromotionRunnable);
                    pawnPromotionThread.start();
                }
            }

            try {
                //SHOULD NOT GO IN IF PROMOTING...
               if(!((Pawn)piece).hasPromoted && piece instanceof Pawn){
                    throw new ClassCastException();
                }
            }catch (ClassCastException e){
                chessPiecesMap.put(newCoordinates, piece);
                chessPiecesMap.remove(piece.getCoordinates());

                piece.setCoordinates(newCoordinates);
            }

            if (piece instanceof King) {
                piece.setCoordinates(newCoordinates);
            }

            //Has done En Passant
            if (enPassantCoordinatesList.contains(newCoordinates) && selectedPiece instanceof Pawn) { //newCoordinates.equals(enPassantSquare)
                enPassantEnemyPawnSquare = enPassantEnemyPawnCoordinatesList.stream()
                        .filter(e -> e.charAt(0) == newCoordinatesX).collect(Collectors.joining());
                ((Pawn) piece).setHasDoubleMoved(false);
                chessPiecesMap.remove(enPassantEnemyPawnSquare);
                enPassantSquare = null;
            }

            //If the enemy king is in check
            if (nextTurnPlayer.checkForKingChecks()) {
                kingIsChecked = true;

//                moves.calculateProtection(currentPlayer.getPlayerPiecesColor());

                //Checkmate
                checkForCheckmate();

                //End the game... (currentPlayer wins)
                if (nextTurnPlayer.isCheckmated) {
                    nextTurnPlayer.getPlayerKing();
                    System.out.printf("%s's king is checkmated.\n", nextTurnPlayer.getPlayerPiecesColor());
                }
            } else {
                kingIsChecked = false;

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
        hasSelectedPiece = false;
        selectedPiece = null;
        enPassantCoordinatesList.clear();
        enPassantEnemyPawnCoordinatesList.clear();
        attackingMovesList.clear();
        legalMovesList.clear();
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
        legalMovesList.clear();

        List<T> enemyPlayerPieces = nextTurnPlayer.getPieces();
        T selectedPieceTemp = selectedPiece;
        for (T piece : enemyPlayerPieces) {
            selectedPieceMediator.setData(piece);
            piece.move();

            if (legalMovesList.size() > 0) {
                isCalculatingStalemate = false;
                return;
            }
        }
        selectedPiece = selectedPieceTemp;
        selectedPieceMediator.setData(selectedPieceTemp);

        if (legalMovesList.size() == 0 && !kingIsChecked) {
            nextTurnPlayer.isStalemated = true;
        }
        isCalculatingStalemate = false;
    }

    /**
     *  Checks if game has ended in a win for the current player (Checkmate)
     */
    private void checkForCheckmate() {
        isCalculatingCheckmate = true;
        legalMovesList.clear();

        moves.calculateProtection(nextTurnPlayer.getPlayerPiecesColor());

        List<T> enemyPlayerPieces = nextTurnPlayer.getPieces();
        T selectedPieceTemp = selectedPiece;
        for (T piece : enemyPlayerPieces) {
            selectedPieceMediator.setData(piece);
            piece.move();
        }
        selectedPiece = selectedPieceTemp;
        if (legalMovesList.size() == 0) {
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

        switch (selectedPiece.getColor()) {
            case BLACK -> {
                if (castlingCoordinates.toCharArray()[0] == 'b') { //Short castle
                    rook = (Rook) chessPiecesMap.get("a1");
                    rook.setCoordinates("c1");
                    chessPiecesMap.remove("a1");
                    chessPiecesMap.put("c1", (T) rook);
                } else if (castlingCoordinates.toCharArray()[0] == 'f') { //Long castle
                    rook = (Rook) chessPiecesMap.get("h1");
                    rook.setCoordinates("e1");
                    chessPiecesMap.remove("h1");
                    chessPiecesMap.put("e1", (T) rook);
                }
                blackPlayer.hasCastled = true;
                blackPlayer.canCastle = false;
                break;
            }
            case WHITE -> {
                if (castlingCoordinates.toCharArray()[0] == 'b') { //Short castle
                    rook = (Rook) chessPiecesMap.get("a8");
                    rook.setCoordinates("c8");
                    chessPiecesMap.remove("a8");
                    chessPiecesMap.put("c8", (T) rook);
                } else if (castlingCoordinates.toCharArray()[0] == 'f') { //Long castle
                    rook = (Rook) chessPiecesMap.get("h8");
                    rook.setCoordinates("e8");
                    chessPiecesMap.remove("h8");
                    chessPiecesMap.put("e8", (T) rook);
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

        for (T piece : chessPiecesMap.values()) {
            if (piece instanceof Pawn && piece.getColor().equals(colour)) {
                ((Pawn) piece).setHasDoubleMoved(false);
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
