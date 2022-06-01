package com.koleff.chess.Player;

import com.koleff.chess.Pieces.*;
import com.koleff.chess.Timer.Clock;

import java.util.*;
import java.util.stream.Collectors;

import static com.koleff.chess.BoardAndFEN.ChessBoardController.*;
import static com.koleff.chess.CoordinatesAndMoves.Moves.*;


public class Player {
    /**
     * Fields
     */
    private String playerName;

    private boolean isPlayerTurn = false;
    private Colour playerPiecesColor;
    private LinkedHashMap<String, Piece> playerPiecesMap = new LinkedHashMap<>();

    //Encapsulate...
    public boolean canCastle = false;
    public boolean hasCastled = false;

    //Used in FENEditor
    public boolean hasCastlingRights = true;
    public boolean canCastleQueenSide = true;
    public boolean canCastleKingSide = true;

    public boolean isCheckmated = false;
    public boolean isStalemated = false;
    public boolean isInCheck = false;

    private Clock clock;
    private Pawn enPassantPawn;

    /**
     * Constructor
     */
    public Player(Colour playerPiecesColor) {
        this.playerPiecesColor = playerPiecesColor;
    }

    /**
     * Getters
     */
    public boolean getPlayerTurn() {
        return isPlayerTurn;
    }

    /**
     * Setters
     */
    public void setPlayerTurn(boolean isPlayerTurn) {
        this.isPlayerTurn = isPlayerTurn;
    }

    public Colour getPlayerPiecesColor() {
        return playerPiecesColor;
    }

    public void setPlayerPiecesColor(Colour playerPiecesColor) {
        this.playerPiecesColor = playerPiecesColor;
    }

    /**
     * Adds a piece of the players color into the player pieces map
     *
     * @see #playerPiecesMap
     */
    public void addPiece(Piece piece) {
        playerPiecesMap.put(piece.getCoordinates(), piece);
    }

    /**
     * Removes a piece of the players color from the player pieces map
     *
     * @see #playerPiecesMap
     */
    public void removePiece(Piece piece) {
        playerPiecesMap.remove(piece.getCoordinates(), piece);
    }

    /**
     * Get all pieces of the player
     *
     * @return list of the pieces of the player
     */
    public List<Piece> getPieces() {
        return moves.getChessPiecesMap().values().stream()
                .filter(e -> e.getColor().equals(playerPiecesColor))
                .toList();
    }

    /**
     * Get the players king coordinates
     * - If kingCoordinates are empty then there is no king on the board
     *
     * @return the players king coordinates
     */
    public String getKingCoordinates() {
        String kingCoordinates = moves.getChessPiecesMap().entrySet().stream()
                .filter(e -> e.getValue().getColor().equals(this.getPlayerPiecesColor()) //currentPlayer
                        && e.getValue() instanceof King)
                .map(Map.Entry::getKey)
                .map(String::valueOf)
                .collect(Collectors.joining(""));

        return kingCoordinates;
    }

    /**
     * Get the players king
     *
     * @return the players king
     */
    public King getPlayerKing() {
        return (King) moves.getChessPiecesMap().get(getKingCoordinates());
    }

    /**
     * Checks if the king from the players color is in check
     *
     * @return true if the players king is in check
     */
    public boolean checkForKingChecks() {
        String kingCoordinates = this.getKingCoordinates();
        List<String> attackingMovesListTemp = new ArrayList(moves.getAttackingMovesList());

        String playerColor = this.getPlayerPiecesColor().toString().charAt(0) + this.getPlayerPiecesColor().toString().substring(1).toLowerCase();
        if (moves.calculateAttackingMoves(this.getNextTurnPlayer().getPlayerPiecesColor()).contains(kingCoordinates)) {
            if (!calculatingIfPieceProtectsKing && !isCalculatingKingDiscoveryFromAllyPiece) {
                System.out.printf("%s's king is in check!\n", playerColor);
                System.out.println();
            }
            moves.setAttackingMovesList(attackingMovesListTemp);
            calculatingIfPieceProtectsKing = false;
            return true;
        }
        moves.setAttackingMovesList(attackingMovesListTemp);
        return false;
    }

    /**
     * Returns the next turn player
     *
     * @return the opposite color player
     */
    private Player getNextTurnPlayer() {
        if (this.getPlayerPiecesColor().equals(Colour.WHITE)) {
            return blackPlayer;
        }
        return whitePlayer;
    }

    /**
     * Checks if the players king is eligible to castle
     * Requirements to castle:
     * - if selected piece is king
     * - if the squares between the king and the rooks are empty
     * - if the squares between the king and the rooks are NOT in check (not inside attackingMovesList)
     * - if the king is NOT in check (not inside attackingMovesList)
     * - if the king has NOT already castled
     */
    public void checkForCastling() {
        if (this.hasCastled) {
            return;
        }
        moves.calculateAttackingMoves(this.getNextTurnPlayer().getPlayerPiecesColor());

        if (moves.castlingMovesList.isEmpty()) {
            moves.castlingMovesList.add("b1");
            moves.castlingMovesList.add("b8");
            moves.castlingMovesList.add("f1");
            moves.castlingMovesList.add("f8");
        }
        try {
            switch (this.getPlayerPiecesColor()) {
                case WHITE -> {
                    //Short castle (king side)
                    if (!this.isInCheck && moves.getSelectedPiece() instanceof King
                            && moves.getChessPiecesMap().containsKey("a8")
                            && !moves.getChessPiecesMap().get("a8").hasMoved
                            && !moves.getChessPiecesMap().get("d8").hasMoved
                            && !moves.getChessPiecesMap().containsKey("b8")
                            && !moves.getChessPiecesMap().containsKey("c8")
                            && !moves.getAttackingMovesList().contains("b8")
                            && !moves.getAttackingMovesList().contains("c8")) {

                        System.out.println("Short Castle -> b8");
                        moves.showLegalMove("b8");
                        this.canCastle = true;
                        this.canCastleKingSide = true;
                    }
                    //Long castle (queen side)
                    if (!this.isInCheck && moves.getSelectedPiece() instanceof King
                            && moves.getChessPiecesMap().containsKey("h8")
                            && !moves.getChessPiecesMap().get("h8").hasMoved
                            && !moves.getChessPiecesMap().get("d8").hasMoved
                            && !moves.getChessPiecesMap().containsKey("e8")
                            && !moves.getChessPiecesMap().containsKey("f8")
                            && !moves.getChessPiecesMap().containsKey("g8")
                            && !moves.getAttackingMovesList().contains("e8") //BUG GOES HERE...
                            && !moves.getAttackingMovesList().contains("f8")
                            && !moves.getAttackingMovesList().contains("g8")) {

                        System.out.println("Long Castle -> f8");
                        moves.showLegalMove("f8");
                        this.canCastle = true;
                        this.canCastleQueenSide = true;
                    }
                    break;
                }
                case BLACK -> {
                    //Short castle (king side)
                    if (!this.isInCheck && moves.getSelectedPiece() instanceof King
                            && moves.getChessPiecesMap().containsKey("a1")
                            && !moves.getChessPiecesMap().get("a1").hasMoved
                            && !moves.getChessPiecesMap().get("d1").hasMoved
                            && !moves.getChessPiecesMap().containsKey("b1")
                            && !moves.getChessPiecesMap().containsKey("c1")
                            && !moves.getAttackingMovesList().contains("b1")
                            && !moves.getAttackingMovesList().contains("c1")) {

                        System.out.println("Short Castle -> b1");
                        moves.showLegalMove("b1");
                        this.canCastle = true;
                        this.canCastleKingSide = true;
                    }
                    //Long castle (queen side)
                    if (!this.isInCheck && moves.getSelectedPiece() instanceof King
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
                        this.canCastle = true;
                        this.canCastleQueenSide = true;
                    }
                    break;
                }
            }
        } catch (NullPointerException e) {
            if (currentPlayer.canCastle) {
                return;
            }
        }
    }

    /**
     * Used for FEN notation
     * - if rook has not been moved
     * - if king has not been moved
     */
    public void checkForCastlingRights() { //TEST...
        this.hasCastlingRights = this.getPieces().stream()
                .filter(e -> e instanceof Rook)
                .filter(e -> !e.hasMoved)
                .count() > 0
                && !this.getPlayerKing().hasMoved;

        try {
            this.canCastleQueenSide = !this.getPieces().stream()
                    .filter(e -> e instanceof Rook)
                    .filter(e -> !e.hasMoved)
                    .filter(e -> e.getCoordinatesXChar() == 'a')
                    .findFirst()
                    .get()
                    .hasMoved;
        } catch (NoSuchElementException e) {
            this.canCastleQueenSide = false;
        }

        try {
            this.canCastleKingSide = !this.getPieces().stream()
                    .filter(e -> e instanceof Rook)
                    .filter(e -> !e.hasMoved)
                    .filter(e -> e.getCoordinatesXChar() == 'h')
                    .findFirst() //.orElseThrow()
                    .get()//BUG GOES HERE...
                    .hasMoved;
        } catch (NoSuchElementException e) {
            this.canCastleKingSide = false;
        }
    }

    /**
     * Used to set the en passant pawn
     *
     * @param enPassantPawn pawn that has double moved
     */
    public void setEnPassantPawn(Pawn enPassantPawn) {
        this.enPassantPawn = enPassantPawn;
    }

    /**
     * Check if the player contains pawn that has double moved
     *
     * @return true if enPassantPawn contains value
     */
    public boolean containsEnPassantPawn() {
        return enPassantPawn != null;
    }

    /**
     * Get the players last pawn that has double moved
     *
     * @return the last pawn that has double moved
     */
    public Pawn getEnPassantPawn() {
        return enPassantPawn;
    }


    /**
     * Resets En Passant
     */
    public void resetEnPassant() {
        for (Piece piece : moves.getChessPiecesMap().values()) {
            if (piece instanceof Pawn && piece.getColor().equals(this.getPlayerPiecesColor())) {
                ((Pawn) piece).hasDoubleMoved = false;
                ((Pawn) piece).setEnPassantSquare(null);
            }
        }
        this.setEnPassantPawn(null);
    }
}
