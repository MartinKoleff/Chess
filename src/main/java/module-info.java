module com.koleff.chess {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.datatransfer;
    requires java.desktop;
    requires com.google.common;

    opens com.koleff.chess.MainMenu to javafx.fxml;
//    exports com.koleff.chess.MainMenu;

    opens com.koleff.chess.Board to javafx.fxml, com.google.common;
//    exports com.koleff.chess.Board;

    opens com.koleff.chess.Timer to com.google.common;
//    exports com.koleff.chess.Timer;

    exports com.koleff.chess.MainMenu;
    opens com.koleff.chess.PawnPromotionWindow to com.google.common, javafx.fxml;
}