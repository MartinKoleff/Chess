package com.koleff.chess.Evaluate;

import com.koleff.chess.Pieces.Colour;
import com.koleff.chess.Pieces.Piece;

import java.util.List;

import static com.koleff.chess.BoardAndFEN.ChessBoardController.moves;

/**
 * Still not implemented! (Coming soon...)
 */
public class Evaluate {
    private int pawnPoints = 1;
    private int knightPoints = 3;
    private int bishopPoints = 3;
    private int rookPoints = 5;
    private int queenPoints = 9;
    private int totalPoints = 0;

    /**
     * Sums the points of each player's color
     * @param color makes calculations for the player color given
     * @return points
     */
    public int calculatePoints(Colour color) {
        List<Piece> playerPieces = moves.getChessPiecesMap().values().stream()
                .filter(e -> e.getColor().equals(color))
                .toList();

        playerPieces.forEach(e -> {
            switch (e.getClass().getSimpleName()) {
                case "Rook":
                    totalPoints += rookPoints;
                    break;
                case "Knight":
                    totalPoints += knightPoints;
                    break;
                case "Bishop":
                    totalPoints += bishopPoints;
                    break;
                case "Queen":
                    totalPoints += queenPoints;
                    break;
                case "Pawn":
                    totalPoints += pawnPoints;
                    break;
            }
        });

        return totalPoints;
    }


    /**
     * Shows evaluation in the main screen
     */
    //Updates every turn...
    public void showEvaluation() {
        int blackPlayerPoints = calculatePoints(Colour.BLACK);
        int whitePlayerPoints = calculatePoints(Colour.WHITE);

        //Write a code where a line is filled based on the ratio between white and black points...
    }
}
