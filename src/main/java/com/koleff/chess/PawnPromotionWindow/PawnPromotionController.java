package com.koleff.chess.PawnPromotionWindow;

import com.koleff.chess.Pieces.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.koleff.chess.BoardAndFEN.ChessBoardController.board;
import static com.koleff.chess.BoardAndFEN.ChessBoardController.currentPlayer;

public class PawnPromotionController implements Initializable {
    /**
     * Fields
     */
    private Piece promotedPawn;
    private static Pawn oldPawn;
    private static Stage pawnPromotionStage;
    @FXML
    private ImageView imageViewQueen;
    @FXML
    private ImageView imageViewRook;
    @FXML
    private ImageView imageViewBishop;
    @FXML
    private ImageView imageViewKnight;

    /**
     * Sets some fields
     *
     * @param coordinates the future coordinates
     * @param pawn        the pawn that is promoting
     */
    public static void setPiece(String coordinates, Pawn pawn) {
        pawn.setCoordinates(coordinates);
        oldPawn = new Pawn(pawn);
    }

    /**
     * Sets the stage, so it can be closed from the Controller in the future
     *
     * @param pawnPromotionStage the stage of the promotion window
     */
    public static void setStage(Stage pawnPromotionStage) {
        PawnPromotionController.pawnPromotionStage = pawnPromotionStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imageViewQueen.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            promotedPawn = new Queen(oldPawn);
            board.addToBoard(promotedPawn.getCoordinates(), promotedPawn);

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

        switch (currentPlayer.getPlayerPiecesColor()) {
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