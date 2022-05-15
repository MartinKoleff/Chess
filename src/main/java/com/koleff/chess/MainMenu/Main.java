package com.koleff.chess.MainMenu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    protected static Stage mainMenuStage;

    @Override
    public void start(Stage mainStage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Main.class.getResource("/com.koleff.chess/mainMenu.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainMenuStage = mainStage;
        mainMenuStage.setTitle("Main Menu");
        mainMenuStage.getIcons().add(new Image(getClass().getResourceAsStream("/com.koleff.chess/pictures/white_king.png")));

        mainMenuStage.setScene(new Scene(root, 600, 400));
        mainMenuStage.setResizable(false);

        mainMenuStage.show();
    }

    public static void main(String[] args) {
        Main.launch(args);
    }
}

