package com.koleff.chess.Board;

import com.koleff.chess.Pieces.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

import static com.koleff.chess.Board.ChessBoardController.board;
import static com.koleff.chess.Board.ChessBoardController.currentPlayer;
import static com.koleff.chess.Pieces.Pawn.pawnPromotionStage;

public class PawnPromotionController implements Initializable {
    /**
     * Fields
     */
    private static Piece promotedPawn;
    private static Pawn oldPawn;
    @FXML
    private ImageView imageViewQueen;
    @FXML
    private ImageView imageViewRook;
    @FXML
    private ImageView imageViewBishop;
    @FXML
    private ImageView imageViewKnight;

    public static void setPiece(String coordinates, Pawn pawn) {
        pawn.setCoordinates(coordinates);
        oldPawn = new Pawn(pawn);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imageViewQueen.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
             promotedPawn = new Queen(oldPawn);
             board.addToBoard(promotedPawn.getCoordinates(), promotedPawn);

//             countDownLatch.countDown();
             pawnPromotionStage.close();
        });
        imageViewRook.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            promotedPawn = new Rook(oldPawn);
            board.addToBoard(promotedPawn.getCoordinates(), promotedPawn);

            pawnPromotionStage.close();
        });
        imageViewBishop.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            promotedPawn = new Bishop(oldPawn);
            board.addToBoard(promotedPawn.getCoordinates(), promotedPawn);

            pawnPromotionStage.close();
        });
        imageViewKnight.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            promotedPawn = new Knight(oldPawn);
            board.addToBoard(promotedPawn.getCoordinates(), promotedPawn);

            pawnPromotionStage.close();
        });

        switch (currentPlayer.getPlayerPiecesColor()){
            case WHITE -> {
                imageViewQueen.setImage(new Image(getClass().getResourceAsStream("/com.koleff.chess/pictures/white_queen.png")));
                imageViewRook.setImage(new Image(getClass().getResourceAsStream("/com.koleff.chess/pictures/white_rook.png")));
                imageViewBishop.setImage(new Image(getClass().getResourceAsStream("/com.koleff.chess/pictures/white_bishop.png")));
                imageViewKnight.setImage(new Image(getClass().getResourceAsStream("/com.koleff.chess/pictures/white_knight.png")));
            }
            case BLACK -> {
                imageViewQueen.setImage(new Image(getClass().getResourceAsStream("/com.koleff.chess/pictures/black_queen.png")));
                imageViewRook.setImage(new Image(getClass().getResourceAsStream("/com.koleff.chess/pictures/black_rook.png")));
                imageViewBishop.setImage(new Image(getClass().getResourceAsStream("/com.koleff.chess/pictures/black_bishop.png")));
                imageViewKnight.setImage(new Image(getClass().getResourceAsStream("/com.koleff.chess/pictures/black_knight.png")));
            }
        }
    }
}
