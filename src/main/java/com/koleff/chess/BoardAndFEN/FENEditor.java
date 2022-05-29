package com.koleff.chess.BoardAndFEN;

import com.koleff.chess.Pieces.Colour;
import com.koleff.chess.Pieces.Pawn;
import com.koleff.chess.Pieces.Piece;
import javafx.scene.image.Image;

import static com.koleff.chess.BoardAndFEN.ChessBoardController.*;

/**
 * Not finished... (Coming soon)
 */
public class FENEditor {
    private StringBuilder boardToFEN = new StringBuilder();
    private String pieceTypeFEN = null;

    public void transformBoardToFEN() {
        String coordinates;
        int emptySpacesCounter = 0;

        for (int y = 8; y >= 1; y--) {
            for (char x = 'a'; x <= 'h'; x++) {
                coordinates = x + "" + y;

                if (moves.getChessPiecesMap().containsKey(coordinates)) {
                    transformPieceToFEN(moves.getChessPiecesMap().get(coordinates));

                    if (emptySpacesCounter != 0) {
                        boardToFEN.append(emptySpacesCounter)
                                .append(pieceTypeFEN);
                    } else {
                        boardToFEN.append(pieceTypeFEN);
                    }
                    emptySpacesCounter = 0;
                } else {
                    emptySpacesCounter++;
                }
            }
            if (emptySpacesCounter == 8) {
                boardToFEN.append("8/");
            } else if (boardToFEN.toString().split("/").length < 7) {
                boardToFEN.append('/');
            }
            emptySpacesCounter = 0;
        }

        //Adds to the FEN the current players turn color
        switch (currentPlayer.getPlayerPiecesColor()) {
            case BLACK -> boardToFEN.append(" b");
            case WHITE -> boardToFEN.append(" w");
        }

        //Adds to the FEN castling right
        if (whitePlayer.canCastle) {  //TO FIX...
            //King side (Short castle)
            if (whitePlayer.canCastleKingSide) {
                boardToFEN.append("K");
            }
            //Queen side (Long castle)
            if (whitePlayer.canCastleQueenSide) {
                boardToFEN.append("Q");
            }
        } else {
            boardToFEN.append('-');
        }

        if (blackPlayer.canCastle) {
            //King side (Short castle)
            if (blackPlayer.canCastleKingSide) {
                boardToFEN.append("k");
            }
            //Queen side (Long castle)
            if (blackPlayer.canCastleQueenSide) {
                boardToFEN.append("q");
            }
        } else {
            boardToFEN.append('-');
        }

        //Adds to the FEN  En Passant (enemy pawn square)
        if (moves.getSelectedPiece() instanceof Pawn && moves.enPassantSquare != null) {
            boardToFEN.append(moves.enPassantEnemyPawnSquare);
        } else {
            boardToFEN.append(" -");
        }

        //Halfmove clock (50 move rule)

        //Fullmove number (the number of turns - starts from 1 and increments after the black players turn)
    }

    /**
     * Transforms the type of the piece and it's color to FEN
     * - white pieces are with capital letters
     * - knight is with the letter 'n' because the king is with the letter 'k'
     *
     * @param piece piece to transform
     */
    private void transformPieceToFEN(Piece piece) {
        switch (piece.getClass().getSimpleName()) {
            case "Rook" -> {
                if (piece.getColor().equals(Colour.BLACK)) {
                    pieceTypeFEN = "r";
                } else {
                    pieceTypeFEN = "R";
                }
                break;
            }
            case "Knight" -> {
                if (piece.getColor().equals(Colour.BLACK)) {
                    pieceTypeFEN = "n";
                } else {
                    pieceTypeFEN = "N";
                }
                break;
            }
            case "Bishop" -> {
                if (piece.getColor().equals(Colour.BLACK)) {
                    pieceTypeFEN = "b";
                } else {
                    pieceTypeFEN = "B";
                }
                break;
            }
            case "Queen" -> {
                if (piece.getColor().equals(Colour.BLACK)) {
                    pieceTypeFEN = "q";
                } else {
                    pieceTypeFEN = "Q";
                }
                break;
            }
            case "King" -> {
                if (piece.getColor().equals(Colour.BLACK)) {
                    pieceTypeFEN = "k";
                } else {
                    pieceTypeFEN = "K";
                }
                break;
            }
            case "Pawn" -> {
                if (piece.getColor().equals(Colour.BLACK)) {
                    pieceTypeFEN = "p";
                } else {
                    pieceTypeFEN = "P";
                }
                break;
            }
        }
    }
}
