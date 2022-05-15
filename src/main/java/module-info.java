module com.koleff.chess {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.koleff.chess.MainMenu to javafx.fxml;
//    exports com.koleff.chess.MainMenu;

    opens com.koleff.chess.Board to javafx.fxml;
//    exports com.koleff.chess.Board;

    exports com.koleff.chess.MainMenu;
}