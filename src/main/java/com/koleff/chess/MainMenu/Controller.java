package com.koleff.chess.MainMenu;

import com.koleff.chess.Board.ChessBoardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import static com.koleff.chess.MainMenu.Main.mainMenuStage;

public class Controller {
    /**
     * Fields
     */
    @FXML
    private Button openChessBoardButton;

    public static Stage chessBoardStage;
    public static final int chessBoardWidth = 800;
    public static final int chessBoardHeight = 800;

    @FXML
    public void openChessBoard(ActionEvent actionEvent) {
        Parent chessBoardRoot = null;
        try {
            chessBoardRoot = FXMLLoader.load(ChessBoardController.class.getResource("/com.koleff.chess/chessBoard.fxml")); ///com.koleff.chess/
        } catch (IOException e) {
            e.printStackTrace();
        }

        chessBoardStage = new Stage();
        chessBoardStage.setTitle("Chess Board");
        chessBoardStage.getIcons().add(new Image(getClass().getResourceAsStream("/com.koleff.chess/pictures/black_king.png")));

        chessBoardStage.setScene(new Scene(chessBoardRoot, chessBoardWidth, chessBoardHeight));
        chessBoardStage.setResizable(false);

        chessBoardStage.show();
        mainMenuStage.close();
    }
}
