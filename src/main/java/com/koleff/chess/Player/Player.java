package com.koleff.chess.Player;

import com.koleff.chess.Pieces.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.koleff.chess.Board.ChessBoardController.*;
import static com.koleff.chess.CoordinatesAndMoves.Moves.*;


public class Player {
    /**
     * Fields
     */
    private static boolean isPlayerTurn = false;
    private Colour playerPiecesColor;

    private String playerName;
    private LinkedHashMap<String, Piece> playerPieces = new LinkedHashMap<>();
    public boolean canCastle = false;
    public boolean hasCastled = false;
    public boolean isCheckmated = false;
    public boolean isStalemated = false;
    public boolean isInCheck = false;

    /**
     * Functions
     */

    /**
     * Constructor
     */
    public Player(Colour playerPiecesColor) {
        this.playerPiecesColor = playerPiecesColor;
    }

    /**
     * Adds the Pieces from the players color into List
     */
    public void addPiece(Piece piece) {
        playerPieces.put(piece.getCoordinates(), piece);
    }

    public void removePiece(Piece piece) {
        playerPieces.remove(piece.getCoordinates(), piece);
    }

    public List<Piece> getPieces() {
        return moves.getChessPiecesMap().values().stream()
                .filter(e -> e.getColor().equals(playerPiecesColor)) //.filter(e -> ((T) e).getColor().equals(playerPiecesColor))
                .toList();
    }

    /**
     * Gets the players king
     * If kingCoordinates == "" -> no king on the board (illegal move)
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
     * Returns the King of the player
     */
    public King getPlayerKing() {
        return (King) moves.getChessPiecesMap().get(getKingCoordinates());
    }

    /**
     * Checks if the King from the players color is in check
     */
    public boolean checkForKingChecks() {
        String kingCoordinates = this.getKingCoordinates();
        List<String> attackingMovesListTemp = new ArrayList(moves.getAttackingMovesList());

        String playerColor = this.getPlayerPiecesColor().toString().charAt(0) + this.getPlayerPiecesColor().toString().substring(1).toLowerCase();
        if (moves.calculateAttackingMoves(getNextTurnPlayer(this).getPlayerPiecesColor()).contains(kingCoordinates)) { //BUGGING HERE...
            if (!calculatingIfPieceProtectsKing && !isCalculatingKingDiscoveryFromAllyPiece) {
                System.out.printf("%s's king is in check!\n", playerColor);
                System.out.println();
            }
            moves.setAttackingMovesList(attackingMovesListTemp);
            calculatingIfPieceProtectsKing = false;
            return true;
        }
//        else if (kingCoordinates.isEmpty()) {
//            System.out.printf("%s's king is missing...\n", playerColor);
//        }
        moves.setAttackingMovesList(attackingMovesListTemp);
        return false;
    }

    /**
     * Returns the next turn player
     */
    private Player getNextTurnPlayer(Player currentPlayer) {
        if (currentPlayer.getPlayerPiecesColor().equals(Colour.WHITE)) {
            return blackPlayer;
        } else {
            return whitePlayer;
        }
    }

    /**
     * Getters
     */
    public static boolean getPlayerTurn() {
        return isPlayerTurn;
    }

    /**
     * Setters
     */
    public static void setPlayerTurn(boolean isPlayerTurn) {
        Player.isPlayerTurn = isPlayerTurn;
    }

    public Colour getPlayerPiecesColor() {
        return playerPiecesColor;
    }

    public void setPlayerPiecesColor(Colour playerPiecesColor) {
        this.playerPiecesColor = playerPiecesColor;
    }

}
