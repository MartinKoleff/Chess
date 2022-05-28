package com.koleff.chess.Player;

import com.koleff.chess.Pieces.*;
import com.koleff.chess.Timer.Clock;

import java.util.*;
import java.util.stream.Collectors;

import static com.koleff.chess.Board.ChessBoardController.*;
import static com.koleff.chess.CoordinatesAndMoves.Moves.*;


public class Player {
    /**
     * Fields
     */
    private String playerName;

    private boolean isPlayerTurn = false;
    private Colour playerPiecesColor;
    private LinkedHashMap<String, Piece> playerPieces = new LinkedHashMap<>();

    //Encapsulate...
    public boolean canCastle = false;
    public boolean hasCastled = false;
    public boolean isCheckmated = false;
    public boolean isStalemated = false;
    public boolean isInCheck = false;

    private Clock clock;
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
                .filter(e -> e.getColor().equals(playerPiecesColor))
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
     */
    private Player getNextTurnPlayer() {
        if (this.getPlayerPiecesColor().equals(Colour.WHITE)) {
            return blackPlayer;
        } else {
            return whitePlayer;
        }
    }
}
