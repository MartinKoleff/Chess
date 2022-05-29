package com.koleff.chess.BoardAndFEN;

import com.koleff.chess.Threads.PawnPromotionRunnable;
import com.koleff.chess.Threads.PawnPromotionThread;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import com.koleff.chess.CoordinatesAndMoves.Moves;
import com.koleff.chess.Pieces.*;
import com.koleff.chess.Player.Player;

import java.net.URL;
import java.util.*;

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

    @FXML
    private Label clockLabel;

    public static final int CELL_WIDTH = chessBoardWidth / 8;
    public static final int CELL_HEIGHT = chessBoardHeight / 8;

    public static Player whitePlayer = new Player(Colour.WHITE);
    public static Player blackPlayer = new Player(Colour.BLACK);
    public static Player currentPlayer = whitePlayer;
    public static Player nextTurnPlayer = blackPlayer;

    public static Board board;
    public static Moves moves;
    public FENEditor fenEditor;

    /**
     * This method is called upon fxml load (before the program starts)
     */
    public void initialize(URL location, ResourceBundle resources) {
        gridPane.setAlignment(Pos.CENTER_LEFT);

        board = new Board(gridPane);
        moves = new Moves();
        fenEditor = new FENEditor();

//        board.arrangeTestingBoard(); //Used for testing...
        board.arrangeBoard();
        fenEditor.transformBoardToFEN();

        System.out.println(currentPlayer.getPlayerPiecesColor() + "'s Player Turn.");
    }

    /**
     * Determines which piece was selected by the user
     *
     * @param mouseEvent the coordinates of the mouse click
     */
    public void selectPiece(MouseEvent mouseEvent) {
        String coordinates = locatePiece(mouseEvent);

        //If piece is already selected - move
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
            if (!moves.getChessPiecesMap().get(coordinates).getColor().equals(currentPlayer.getPlayerPiecesColor())) {
                System.out.println("You have selected pieces from wrong color! Please try to select piece from your color!");
                return;
            }

            //Piece is selected logic...
            Piece piece = moves.getChessPiecesMap().get(coordinates);
            moves.setSelectedPiece(piece);

            //Protection of enemy pieces
            moves.calculateProtection(nextTurnPlayer.getPlayerPiecesColor()); //CHECK IF NEEDED...

            moves.calculateAttackingMoves(nextTurnPlayer.getPlayerPiecesColor()); //CHECK IF NEEDED...
            moves.getSelectedPiece().move();

            //If piece doesn't have moves - don't have to double-click / wait for clicking again
            if (moves.getLegalMovesList().isEmpty()) {
                moves.setSelectedPiece(null);
            }
        }
    }

    /**
     * Move the pieces by clicking on legal square.
     *
     * @param mouseEvent    the coordinates of the mouse click
     * @param selectedPiece the selected piece
     */
    public void movePieceByClicking(MouseEvent mouseEvent, Piece selectedPiece) {
        String newCoordinates = locatePiece(mouseEvent);
        char newCoordinatesX = newCoordinates.charAt(0);
        int newCoordinatesY = Integer.parseInt(String.valueOf(newCoordinates.charAt(1)));

        //Move is legal
        if (moves.getLegalMovesList().contains(newCoordinates)) {

            //Castle
            if (moves.getSelectedPiece() instanceof King && !currentPlayer.hasCastled && currentPlayer.canCastle
                    && moves.castlingMovesList.contains(newCoordinates) && !moves.getSelectedPiece().hasMoved) {
                castle(newCoordinates);
            }
            selectedPiece.hasMoved = true;

            //En Passant setter
            if (selectedPiece instanceof Pawn) {
                if (newCoordinatesY - 2 == selectedPiece.getCoordinatesY()
                        || newCoordinatesY + 2 == selectedPiece.getCoordinatesY()) {
                    ((Pawn) selectedPiece).hasDoubleMoved = true;
                    currentPlayer.setEnPassantPawn((Pawn) selectedPiece);
                }

                //Promotion
                if (newCoordinatesY == 8 || newCoordinatesY == 1) {
                    PawnPromotionRunnable pawnPromotionRunnable = new PawnPromotionRunnable((Pawn) selectedPiece, newCoordinates);
                    PawnPromotionThread pawnPromotionThread = new PawnPromotionThread(pawnPromotionRunnable);
                    pawnPromotionThread.start();
                }

                //Has done En Passant
                if (nextTurnPlayer.containsEnPassantPawn()
                        && newCoordinates.equals(nextTurnPlayer.getEnPassantPawn().getEnPassantSquare())) {
                    ((Pawn) selectedPiece).hasDoubleMoved = false;
                    moves.getChessPiecesMap().remove(nextTurnPlayer.getEnPassantPawn().getCoordinates());
                }
            }

            try {
                if (!((Pawn) selectedPiece).hasPromoted && selectedPiece instanceof Pawn) { //Shouldn't go in if promoting...
                    throw new ClassCastException();
                }
            } catch (ClassCastException e) {
                moves.getChessPiecesMap().put(newCoordinates, selectedPiece);
                moves.getChessPiecesMap().remove(selectedPiece.getCoordinates());

                selectedPiece.setCoordinates(newCoordinates);
            }

            if (selectedPiece instanceof King) {
                selectedPiece.setCoordinates(newCoordinates);
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

            nextTurnPlayer.resetEnPassant();

            //Switches player, resets turns and resets En passant
            if (currentPlayer.getPlayerPiecesColor().equals(Colour.WHITE)) { /**FIND DECLARATIVE WAY TO WRITE...*/
                currentPlayer = blackPlayer;
                nextTurnPlayer = whitePlayer;
            } else {
                currentPlayer = whitePlayer;
                nextTurnPlayer = blackPlayer;
            }
            currentPlayer.setPlayerTurn(true);
            nextTurnPlayer.setPlayerTurn(false);
            System.out.println("\n" + currentPlayer.getPlayerPiecesColor() + "'s Player Turn.");
        } else {
            System.out.println("The move you are trying to make is illegal!");
        }

        //Saves last turn with FEN notation into .txt file
        whitePlayer.checkForCastlingRights();
        blackPlayer.checkForCastlingRights();
        fenEditor.transformBoardToFEN();

        //Resetting variables
        moves.setSelectedPiece(null);
        moves.getAttackingMovesList().clear();
        moves.getLegalMovesList().clear();
        board.updateBoard();
    }

    /**
     * Checks if game has ended in a draw (Stalemate)
     * - No moves for the enemy king
     * - The enemy King is NOT in check
     * - All the enemy pieces don't have legal moves or can't move
     */
    private void checkForStalemate() {
        isCalculatingStalemate = true;
        moves.getLegalMovesList().clear();

        List<Piece> enemyPlayerPieces = nextTurnPlayer.getPieces();
        Piece selectedPieceTemp = moves.getSelectedPiece();
        for (Piece piece : enemyPlayerPieces) {
            moves.setSelectedPiece(piece);
            piece.move();

            if (moves.getLegalMovesList().size() > 0) {
                isCalculatingStalemate = false;
                moves.setSelectedPiece(selectedPieceTemp);
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
     * Checks if game has ended in a win for the current player (Checkmate)
     * - The enemy king is in check
     * - The enemy king has no legal moves
     * - No enemy piece can protect or deflect the check
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
     * - Only replaces the rook (the King moves to its correct place in movePieceByClicking())
     *
     * @param castlingCoordinates coordinates of where the ally King goes
     */
    private void castle(String castlingCoordinates) {
        Rook rook;

        switch (moves.getSelectedPiece().getColor()) {
            case BLACK -> {
                if (castlingCoordinates.charAt(0) == 'b') { //Short castle
                    rook = (Rook) moves.getChessPiecesMap().get("a1");
                    rook.setCoordinates("c1");
                    moves.getChessPiecesMap().remove("a1");
                    moves.getChessPiecesMap().put("c1", rook);
                } else if (castlingCoordinates.charAt(0) == 'f') { //Long castle
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
                if (castlingCoordinates.charAt(0) == 'b') { //Short castle
                    rook = (Rook) moves.getChessPiecesMap().get("a8");
                    rook.setCoordinates("c8");
                    moves.getChessPiecesMap().remove("a8");
                    moves.getChessPiecesMap().put("c8", rook);
                } else if (castlingCoordinates.charAt(0) == 'f') { //Long castle
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
     * Gets the square of the mouse press
     *
     * @param mouseEvent the coordinates of the mouse click
     * @return chess board coordinates of the mouse click
     * @see com.koleff.chess.CoordinatesAndMoves.Coordinates for coordinates calculation
     */
    public String locatePiece(MouseEvent mouseEvent) {
        char coordinatesX = calculateX(mouseEvent);
        int coordinatesY = calculateY(mouseEvent);
        String coordinates = getCoordinatesToString(coordinatesX, coordinatesY);

        return coordinates;
    }
}
