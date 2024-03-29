package com.koleff.chess.BoardAndFEN;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.koleff.chess.CoordinatesAndMoves.Moves;
import com.koleff.chess.MainMenu.Controller;
import com.koleff.chess.Pieces.*;
import com.koleff.chess.Player.Player;
import com.koleff.chess.SerializationManager.MapSerializationManager;
import com.koleff.chess.SerializationManager.SerializationManager;
import com.koleff.chess.Threads.PawnPromotionRunnable;
import com.koleff.chess.Threads.PawnPromotionThread;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.koleff.chess.CoordinatesAndMoves.Coordinates.*;
import static com.koleff.chess.CoordinatesAndMoves.Moves.isCalculatingCheckmate;
import static com.koleff.chess.CoordinatesAndMoves.Moves.isCalculatingStalemate;
import static com.koleff.chess.MainMenu.Controller.chessBoardHeight;
import static com.koleff.chess.MainMenu.Controller.chessBoardWidth;


public class ChessBoardController implements Initializable {
    /**
     * Fields
     */
    @FXML
    private GridPane gridPane;

    @FXML
    private BorderPane gameWindow;

    @FXML
    private Label whitePlayerClockLabel;

    @FXML
    private Label blackPlayerClockLabel;

    public static final int CELL_WIDTH = chessBoardWidth / 8;
    public static final int CELL_HEIGHT = chessBoardHeight / 8;
    private int currentTurn = 1;

    public static Player whitePlayer = new Player(Colour.WHITE);
    public static Player blackPlayer = new Player(Colour.BLACK);
    public static Player currentPlayer = whitePlayer;
    public static Player nextTurnPlayer = blackPlayer;

    public static Board board;
    public static Moves moves;
    public FENEditor fenEditor;

    private List<Serializable> serializationList;
    private List<HashMap<String, Piece>> allPositionsList;

    /**
     * This method is called upon fxml load (before the program starts)
     */
    @SuppressWarnings("Implicit casting")
    public void initialize(URL location, ResourceBundle resources) {
        gridPane.setAlignment(Pos.CENTER_LEFT);

        //Initialize fields
        board = new Board(gridPane);
        moves = new Moves();
        fenEditor = new FENEditor();
        serializationList = new ArrayList<>();
        allPositionsList = new ArrayList<>();

//        board.arrangeTestingBoard(); //Used for testing...

        if (Controller.toLoadGame) {
            try {
                serializationList = (List<Serializable>) SerializationManager.load("src/main/resources/com.koleff.chess/data.txt");
                moves.setChessPiecesMap((HashMap<String, Piece>) serializationList.get(0));
                currentPlayer = (Player) serializationList.get(1);

                clearSerializationFile();

                //Add to allPositionsList all old positions...
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("There was a problem with the deserialization of the file.");
                e.printStackTrace();

                clearSerializationFile();
            }
            board.updateBoard();
        } else {
            clearSerializationFile();
            board.arrangeBoard();

            whitePlayer.setClock(whitePlayerClockLabel, 30);
            blackPlayer.setClock(blackPlayerClockLabel, 30);

            allPositionsList.add(new HashMap<>(moves.getChessPiecesMap())); //Default board position
        }
        fenEditor.transformBoardToFEN();
        System.out.println(currentPlayer.getPlayerPiecesColor() + "'s Player Turn.");
    }

    /**
     * Used to clear the data.txt file (the file for the serialization)
     * - Using Apache common io library
     */
    private void clearSerializationFile() {
        try {
            FileUtils.write(new File("src/main/resources/com.koleff.chess/data.txt"), "");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                if (!((Pawn) selectedPiece).hasPromoted) { //Shouldn't go in if promoting...
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

            //Updates turn
            if(currentPlayer.getPlayerPiecesColor().equals(Colour.BLACK)){
                currentTurn++;
            }

            //Updates clock
            if(currentTurn > 1){
                currentPlayer.getClock().pause();
                nextTurnPlayer.getClock().start();
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

        //Serialize the board and the current player...
        try {
//            SerializationManager.save((Serializable) moves.getChessPiecesMap(), "resources/com.koleff.chess/data.txt");
//            SerializationManager.save((Serializable) moves.getChessPiecesMap(), "/com.koleff.chess/data.txt");
//            SerializationManager.save((Serializable) moves.getChessPiecesMap(), "com.koleff.chess/data.txt");
//            SerializationManager.save((Serializable) moves.getChessPiecesMap(), "data.txt");
//            SerializationManager.save((Serializable) moves.getChessPiecesMap(), "../data.txt");

            serializationList.clear();
            serializationList.add((Serializable) moves.getChessPiecesMap());
            serializationList.add(currentPlayer);

            SerializationManager.save((Serializable) serializationList, "src/main/resources/com.koleff.chess/data.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }


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
                checkForThreeMoveRepetitionStalemate();
                return;
            }
        }
        moves.setSelectedPiece(selectedPieceTemp);

        if (moves.getLegalMovesList().size() == 0 && !nextTurnPlayer.isInCheck) {
            nextTurnPlayer.isStalemated = true;
            isCalculatingStalemate = false;
            return;
        }
        isCalculatingStalemate = false;

        checkForThreeMoveRepetitionStalemate();
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


    /**
     * Checks for three move repetition stalemate
     * - if the same position is repeated 3 times then it is stalemate
     *
     * @return true if a position is repeated 3 times
     */
    private void checkForThreeMoveRepetitionStalemate() {
        int repetitionCounter = 1;

        //Check all previous positions...
        for (HashMap oldPosition : allPositionsList) {
           if(moves.getChessPiecesMap().equals(oldPosition)){ //Don't compare piece.hasMoved
                repetitionCounter++;
            }

            if (repetitionCounter == 3) {
                System.out.printf("%s player has been stalemated via 3 move repetition\n", nextTurnPlayer.getPlayerPiecesColor());
                nextTurnPlayer.isStalemated = true;
                return;
            }
        }
        allPositionsList.add(new HashMap<>(moves.getChessPiecesMap()));
    }
}
//USED FOR OPTIMIZING...
//        List<Pawn> oldPositionPawnsList;
//   oldPositionPawnsList = oldPosition.values().stream()
//                    .filter(e -> e instanceof Pawn)
//                    .toList();
//
//            //Check all pawns (if at least 1 is different - reset)
//            if (!oldPositionPawnsList.equals(moves.getChessPiecesMap().values().stream()
//                    .filter(e -> e instanceof Pawn)
//                    .toList())) { //If all pawns are the same as the current position (used for optimizing)
//                allPositionsList.clear();
//                allPositionsList.add(moves.getChessPiecesMap());
//                return;
//            } else