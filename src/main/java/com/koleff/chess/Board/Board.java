package com.koleff.chess.Board;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import com.koleff.chess.Pieces.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Board<T extends Piece> extends ChessBoardController {
    /**
     * Fields
     */
    private static GridPane gridPane;

    /**
     * Functions
     */
    public static GridPane getGridPane() {
        return gridPane;
    }

    /**
     * Arrange the board in the starting position
     */
    @SuppressWarnings("unchecked")
    public void arrangeBoard() {
        //White Pieces
        addToBoard("a8", (T) new Rook('a', 8, Colour.WHITE));
        addToBoard("b8", (T) new Knight('b', 8, Colour.WHITE));
        addToBoard("c8", (T) new Bishop('c', 8, Colour.WHITE));
        addToBoard("e8", (T) new Queen('e', 8, Colour.WHITE));
        addToBoard("d8", (T) new King('d', 8, Colour.WHITE));
        addToBoard("f8", (T) new Bishop('f', 8, Colour.WHITE));
        addToBoard("g8", (T) new Knight('g', 8, Colour.WHITE));
        addToBoard("h8", (T) new Rook('h', 8, Colour.WHITE));

        addToBoard("a7", (T) new Pawn('a', 7, Colour.WHITE));
        addToBoard("b7", (T) new Pawn('b', 7, Colour.WHITE));
        addToBoard("c7", (T) new Pawn('c', 7, Colour.WHITE));
        addToBoard("d7", (T) new Pawn('d', 7, Colour.WHITE));
        addToBoard("e7", (T) new Pawn('e', 7, Colour.WHITE));
        addToBoard("f7", (T) new Pawn('f', 7, Colour.WHITE));
        addToBoard("g7", (T) new Pawn('g', 7, Colour.WHITE));
        addToBoard("h7", (T) new Pawn('h', 7, Colour.WHITE));

        //Black Pieces
        addToBoard("a1", (T) new Rook('a', 1, Colour.BLACK));
        addToBoard("b1", (T) new Knight('b', 1, Colour.BLACK));
        addToBoard("c1", (T) new Bishop('c', 1, Colour.BLACK));
        addToBoard("e1", (T) new Queen('e', 1, Colour.BLACK));
        addToBoard("d1", (T) new King('d', 1, Colour.BLACK));
        addToBoard("f1", (T) new Bishop('f', 1, Colour.BLACK));
        addToBoard("g1", (T) new Knight('g', 1, Colour.BLACK));
        addToBoard("h1", (T) new Rook('h', 1, Colour.BLACK));

        addToBoard("a2", (T) new Pawn('a', 2, Colour.BLACK));
        addToBoard("b2", (T) new Pawn('b', 2, Colour.BLACK));
        addToBoard("c2", (T) new Pawn('c', 2, Colour.BLACK));
        addToBoard("d2", (T) new Pawn('d', 2, Colour.BLACK));
        addToBoard("e2", (T) new Pawn('e', 2, Colour.BLACK));
        addToBoard("f2", (T) new Pawn('f', 2, Colour.BLACK));
        addToBoard("g2", (T) new Pawn('g', 2, Colour.BLACK));
        addToBoard("h2", (T) new Pawn('h', 2, Colour.BLACK));
    }


    /**
     * Used to test different scenarios
     */
    protected void arrangeTestingBoard() {
        //Rook Testing
//        addToBoard("a8", new Piece('a', 8, Colour.BLACK, Type.ROOK));
//        addToBoard("e4", new Piece('e', 4, Colour.BLACK, Type.ROOK));
//        addToBoard("h7", new Piece('h', 7, Colour.BLACK, Type.ROOK));
//        addToBoard("d1", new Piece('d', 1, Colour.BLACK, Type.ROOK));

        //Bishop Testing
//        addToBoard("d1", new Piece('d', 1, Colour.BLACK, Type.BISHOP));
//        addToBoard("e4", new Piece('e', 4, Colour.BLACK, Type.BISHOP));
//        addToBoard("a8", new Piece('a', 8, Colour.BLACK, Type.BISHOP));
//        addToBoard("h7", new Piece('h', 7, Colour.BLACK, Type.BISHOP));

        //Rook & Bishop Testing
//        addToBoard("d1", new Piece('d', 1, Colour.WHITE, Type.BISHOP));
//        addToBoard("a8", new Piece('a', 8, Colour.BLACK, Type.ROOK));
//        addToBoard("a2", new Piece('a', 2, Colour.WHITE, Type.BISHOP));
//        addToBoard("h7", new Piece('h', 7, Colour.BLACK, Type.BISHOP));

        //Bishop Testing 2
//        addToBoard("a2", new Piece('a', 2, Piece.Colour.BLACK, Piece.Type.BISHOP));
//        addToBoard("b1", new Piece('b', 1, Piece.Colour.WHITE, Piece.Type.BISHOP));
//        addToBoard("c2", new Piece('c', 2, Piece.Colour.BLACK, Piece.Type.BISHOP));

        //Queen Testing
//        addToBoard("d1", new Piece('d', 1, Colour.BLACK, Type.QUEEN));
//        addToBoard("e4", new Piece('e', 4, Colour.WHITE, Type.QUEEN));
//        addToBoard("a8", new Piece('a', 8, Colour.BLACK, Type.QUEEN));
//        addToBoard("h7", new Piece('h', 7, Colour.WHITE, Type.QUEEN));

        //Pawn Testing
//        addToBoard("a2", new Piece('a', 2, Colour.BLACK, Type.PAWN));
//        addToBoard("f2", new Piece('f', 2, Colour.BLACK, Type.PAWN));
//        addToBoard("c2", new Piece('c', 2, Colour.BLACK, Type.PAWN));
//
//        addToBoard("g7", new Piece('g', 7, Colour.WHITE, Type.PAWN));
//        addToBoard("h7", new Piece('h', 7, Colour.WHITE, Type.PAWN));
//        addToBoard("c7", new Piece('c', 7, Colour.WHITE, Type.PAWN));

//        Pawn Testing 2
//        addToBoard("a7", new Piece('a', 7, Colour.BLACK, Type.PAWN));
//        addToBoard("f6", new Piece('f', 6, Colour.BLACK, Type.PAWN));
//        addToBoard("c2", new Piece('c', 2, Colour.BLACK, Type.PAWN));
//
//        addToBoard("g2", new Piece('g', 2, Colour.WHITE, Type.PAWN));
//        addToBoard("h3", new Piece('h', 3, Colour.WHITE, Type.PAWN));

//        Pawn Testing 3
//        addToBoard("a3", new Piece('a', 3, Piece.Colour.BLACK, Piece.Type.PAWN));
//        addToBoard("b4", new Piece('b', 4, Piece.Colour.WHITE, Piece.Type.PAWN));
//        addToBoard("c6", new Piece('c', 6, Piece.Colour.WHITE, Piece.Type.PAWN));
//        addToBoard("d5", new Piece('d', 5, Piece.Colour.BLACK, Piece.Type.PAWN));
//        addToBoard("b3", new Piece('b', 3, Piece.Colour.BLACK, Piece.Type.PAWN));


        //Pawn Testing 4
//        addToBoard("d4", new Piece('d', 4, Piece.Colour.BLACK, Piece.Type.PAWN));
//        addToBoard("e5", new Piece('e', 5, Piece.Colour.WHITE, Piece.Type.PAWN));
//        addToBoard("f4", new Piece('f', 4, Piece.Colour.BLACK, Piece.Type.PAWN));
//        addToBoard("c3", new Piece('c', 3, Piece.Colour.BLACK, Piece.Type.PAWN));
//        addToBoard("b2", new Piece('b', 2, Piece.Colour.BLACK, Piece.Type.PAWN));
//        addToBoard("e3", new Piece('e', 3, Piece.Colour.BLACK, Piece.Type.PAWN));


        //Pawn Testing 5
//        addToBoard("a8", new Pawn('a', 8, Colour.BLACK, Type.PAWN));
//        addToBoard("b8", new Pawn('b', 8, Colour.WHITE, Type.PAWN));
//        addToBoard("a7", new Pawn('a', 7, Colour.BLACK, Type.PAWN));
//        addToBoard("h2", new Pawn('h', 2, Colour.BLACK, Type.PAWN));
//        addToBoard("h1", new Pawn('h', 1, Colour.BLACK, Type.PAWN));
//        addToBoard("g1", new Pawn('g', 1, Colour.WHITE, Type.PAWN));


        //Pawn Testing 6
//        addToBoard("a7", new Pawn('a', 7, Piece.Colour.WHITE, Piece.Type.PAWN));
//        addToBoard("b7", new Pawn('b', 7, Piece.Colour.WHITE, Piece.Type.PAWN));
//        addToBoard("c7", new Pawn('c', 7, Piece.Colour.WHITE, Piece.Type.PAWN));
//        addToBoard("d7", new Pawn('d', 7, Piece.Colour.WHITE, Piece.Type.PAWN));
//        addToBoard("e7", new Pawn('e', 7, Piece.Colour.WHITE, Piece.Type.PAWN));
//        addToBoard("f7", new Pawn('f', 7, Piece.Colour.WHITE, Piece.Type.PAWN));
//        addToBoard("g7", new Pawn('g', 7, Piece.Colour.WHITE, Piece.Type.PAWN));
//        addToBoard("h7", new Pawn('h', 7, Piece.Colour.WHITE, Piece.Type.PAWN));
//
//        addToBoard("a2", new Pawn('a', 2, Piece.Colour.BLACK, Piece.Type.PAWN));
//        addToBoard("b2", new Pawn('b', 2, Piece.Colour.BLACK, Piece.Type.PAWN));
//        addToBoard("c2", new Pawn('c', 2, Piece.Colour.BLACK, Piece.Type.PAWN));
//        addToBoard("d2", new Pawn('d', 2, Piece.Colour.BLACK, Piece.Type.PAWN));
//        addToBoard("e2", new Pawn('e', 2, Piece.Colour.BLACK, Piece.Type.PAWN));
//        addToBoard("f2", new Pawn('f', 2, Piece.Colour.BLACK, Piece.Type.PAWN));
//        addToBoard("g2", new Pawn('g', 2, Piece.Colour.BLACK, Piece.Type.PAWN));
//        addToBoard("h2", new Pawn('h', 2, Piece.Colour.BLACK, Piece.Type.PAWN));


        //El Passant Testing 1
//        addToBoard("d4", new Pawn('d', 4, Piece.Colour.BLACK, Piece.Type.PAWN));
//        addToBoard("e6", new Pawn('e', 6, Piece.Colour.WHITE, Piece.Type.PAWN));

//        El Passant Testing 2
//        addToBoard("a4", new Pawn('a', 4, Piece.Colour.BLACK, Piece.Type.PAWN));
//        addToBoard("c4", new Pawn('c', 4, Piece.Colour.BLACK, Piece.Type.PAWN));
//        addToBoard("b6", new Pawn('b', 6, Piece.Colour.WHITE, Piece.Type.PAWN));

//        El Passant Testing 3
//        addToBoard("e6",(T)new Pawn('e', 6, Colour.WHITE, Type.PAWN));
//        addToBoard("f4",(T)new Pawn('f', 4, Colour.BLACK, Type.PAWN));
//        addToBoard("d4",(T)new Pawn('d', 4, Colour.BLACK, Type.PAWN));

        //El Passant Testing 4
//        addToBoard("a8", (T)new Pawn('a', 8, Colour.WHITE, Type.PAWN));
//        addToBoard("b6", (T)new Pawn('b', 6, Colour.BLACK, Type.PAWN));
//        addToBoard("h1", (T)new Pawn('h', 1, Colour.BLACK, Type.PAWN));
//        addToBoard("g3", (T)new Pawn('g', 3, Colour.WHITE, Type.PAWN));

//        El Passant Testing 5
//        addToBoard("a6", (T) new Pawn('a', 6, Colour.BLACK, Type.PAWN));
//        addToBoard("b8", (T) new Pawn('b', 8, Colour.WHITE, Type.PAWN));
//        addToBoard("c6", (T) new Pawn('c', 6, Colour.BLACK, Type.PAWN));
//        addToBoard("h1", (T) new Pawn('h', 1, Colour.BLACK, Type.PAWN));
//        addToBoard("g3", (T) new Pawn('g', 3, Colour.WHITE, Type.PAWN));
//        addToBoard("h8", (T) new Bishop('h', 8, Colour.BLACK, Type.BISHOP));

        //El Passant Testing 6
//        addToBoard("a1", (T)new Pawn('a', 1, Colour.BLACK, Type.PAWN));
//        addToBoard("b3", (T)new Pawn('b', 3, Colour.WHITE, Type.PAWN));
//        addToBoard("c3", (T)new Pawn('c', 3, Colour.BLACK, Type.PAWN));
//        addToBoard("f5", (T)new Bishop('f', 5, Colour.BLACK, Type.BISHOP));
//        addToBoard("h3", (T)new Pawn('h', 3, Colour.WHITE, Type.PAWN));

        //Knight Testing 1
//        addToBoard("d1", (T) new Knight('d', 1, Colour.WHITE, Type.KNIGHT));
//        addToBoard("e4", (T) new Knight('e', 4, Colour.BLACK, Type.KNIGHT));
//        addToBoard("a8", (T) new Bishop('a', 8, Colour.WHITE, Type.BISHOP));
//        addToBoard("h7", (T) new Knight('h', 7, Colour.BLACK, Type.KNIGHT));
//        addToBoard("a1", (T) new King('a', 1, Colour.WHITE, Type.KING));
//        addToBoard("h8", (T) new King('h', 8, Colour.BLACK, Type.KING));

        //King Testing 1 //TOFIX
//        addToBoard("d1", new King('d', 1, Colour.BLACK, Type.KING));
//        addToBoard("b2", new King('b', 2, Colour.WHITE, Type.KING));
//        addToBoard("h8", new Piece('h', 8, Colour.BLACK, Type.BISHOP));
//        addToBoard("g6", new Piece('g', 6, Colour.WHITE, Type.ROOK));
//        addToBoard("f2", new Piece('f', 2, Colour.WHITE, Type.BISHOP));
//        addToBoard("e5", new Piece('e', 5, Colour.BLACK, Type.ROOK));

        //King Testing 2
//        addToBoard("d1", new King('d', 1, Colour.BLACK, Type.KING));
//        addToBoard("b2", new King('b', 2, Colour.WHITE, Type.KING));
//        addToBoard("h8", new Piece('h', 8, Colour.BLACK, Type.BISHOP));
//        addToBoard("g6", new Piece('g', 6, Colour.WHITE, Type.ROOK));
//        addToBoard("f2", new Piece('f', 2, Colour.WHITE, Type.BISHOP));
//        addToBoard("e7", new Piece('e', 7, Colour.BLACK, Type.ROOK));


        //King Testing 3
//        addToBoard("a1", new King('a', 1, Colour.WHITE, Type.KING));
//        addToBoard("h5", new King('h', 5, Colour.BLACK, Type.KING));
//        addToBoard("h7", new Piece('h', 7, Colour.BLACK, Type.BISHOP));
//        addToBoard("a7", new Piece('a', 7, Colour.WHITE, Type.BISHOP));
//        addToBoard("f4", new Piece('f', 4, Colour.BLACK, Type.ROOK));
//        addToBoard("g2", new Piece('g', 2, Colour.WHITE, Type.ROOK));

        //King Testing 4
//        addToBoard("d1", (T) new King('d', 1, Colour.WHITE, Type.KING));
//        addToBoard("b2", (T) new King('b', 2, Colour.BLACK, Type.KING));
//        addToBoard("h8", (T) new Bishop('h', 8, Colour.WHITE, Type.BISHOP));
//        addToBoard("g6", (T) new Rook('g', 6, Colour.BLACK, Type.ROOK));
//        addToBoard("f2", (T) new Bishop('f', 2, Colour.BLACK, Type.BISHOP));
//        addToBoard("e5", (T) new Rook('e', 5, Colour.WHITE, Type.ROOK));

        //Protecting Pieces Checks Testing 1
//        addToBoard("d1", new King('d', 1, Colour.WHITE, Type.KING));
//        addToBoard("b2", new King('b', 2, Colour.BLACK, Type.KING));
//        addToBoard("d8", new Piece('d', 8, Colour.WHITE, Type.ROOK));
//        addToBoard("d6", new Pawn('d', 6, Colour.WHITE, Type.PAWN));
//        addToBoard("h2", new Piece('h', 2, Colour.BLACK, Type.ROOK));
//        addToBoard("h4", new Pawn('h', 4, Colour.BLACK, Type.PAWN));
//        addToBoard("e6", new Piece('e', 6, Colour.WHITE, Type.BISHOP));
//        addToBoard("c6", new Piece('c', 6, Colour.WHITE, Type.ROOK));

        //Protecting Pieces Checks Testing 2
//        addToBoard("e1", new King('e', 1, Colour.WHITE, Type.KING));
//        addToBoard("b2", new King('b', 2, Colour.BLACK, Type.KING));
//        addToBoard("d8", new Piece('d', 8, Colour.BLACK, Type.ROOK));
////        addToBoard("d5", new Pawn('d', 5, Colour.BLACK, Type.PAWN));
////        addToBoard("c2", new Pawn('c', 2, Colour.WHITE, Type.PAWN));
//        addToBoard("d6", new Piece('d', 6, Colour.BLACK, Type.ROOK));
//        addToBoard("c4", new Piece('c', 4, Colour.WHITE, Type.ROOK));
//        addToBoard("c6", new Piece('c', 6, Colour.WHITE, Type.ROOK));

        //Protecting and King Testing 1
//        addToBoard("a1", new King('a', 1, Colour.BLACK, Type.KING));
//        addToBoard("b2", new Piece('b', 2, Colour.WHITE, Type.ROOK));
//        addToBoard("c2", new King('c', 2, Colour.WHITE, Type.KING));

        //Protecting and King Testing 2
//        addToBoard("e5", new King('e', 5, Colour.BLACK, Type.KING));
//        addToBoard("d3", new Piece('d', 3, Colour.WHITE, Type.ROOK));
//        addToBoard("f2", new Piece('f', 2, Colour.BLACK, Type.BISHOP));
////        addToBoard("d4", new Piece('d', 4, Colour.WHITE, Type.KNIGHT));
//        addToBoard("h6", new King('h', 6, Colour.WHITE, Type.KING));
//        addToBoard("e2", new Piece('e', 2, Colour.BLACK, Type.ROOK));

        //Protecting and King Testing 3
//        addToBoard("e5", new King('e', 5, Colour.BLACK, Type.KING));
//        addToBoard("d3", new Piece('d', 3, Colour.WHITE, Type.ROOK));
//        addToBoard("d4", new Piece('d', 4, Colour.BLACK, Type.BISHOP));
//        addToBoard("h6", new King('h', 6, Colour.WHITE, Type.KING));
//        addToBoard("e2", new Piece('e', 2, Colour.BLACK, Type.ROOK));

//        Protecting and King Testing 4
//        addToBoard("e5", new King('e', 5, Colour.BLACK, Type.KING));
//        addToBoard("d3", new Piece('d', 3, Colour.WHITE, Type.ROOK));
//        addToBoard("c5", new Piece('c', 5, Colour.BLACK, Type.BISHOP));
//        addToBoard("h6", new King('h', 6, Colour.WHITE, Type.KING));
//        addToBoard("e2", new Piece('e', 2, Colour.BLACK, Type.ROOK));

        //Protecting and King Testing 4 //over d4 for the bishop -> glitchy FIXED
//    addToBoard("e5", new King('e', 5, Colour.BLACK, Type.KING));
//    addToBoard("d3", new Piece('d', 3, Colour.WHITE, Type.ROOK));
//    addToBoard("c5", new Piece('c', 5, Colour.BLACK, Type.BISHOP));
//    addToBoard("h6", new King('h', 6, Colour.WHITE, Type.KING));
//    addToBoard("e2", new Piece('e', 2, Colour.BLACK, Type.ROOK));

        //Pawn Protection System Testing 1 / Ally Piece Discovers King 1
//        addToBoard("e5", (T) new King('e', 5, Colour.BLACK, Type.KING));
//        addToBoard("d3", (T) new Rook('d', 3, Colour.WHITE, Type.ROOK));
//        addToBoard("c5", (T) new Bishop ('c', 5, Colour.BLACK, Type.BISHOP));
//        addToBoard("c7", (T) new Pawn('c', 7, Colour.WHITE, Type.PAWN));
//        addToBoard("h7", (T) new King('h', 7, Colour.WHITE, Type.KING));
//        addToBoard("g5", (T) new Bishop('g', 5, Colour.WHITE, Type.BISHOP));
//        addToBoard("e2", (T) new Rook('e', 2, Colour.BLACK, Type.ROOK));
//        addToBoard("b2", (T) new Pawn('b', 2, Colour.BLACK, Type.PAWN));
//        addToBoard("b1", (T) new Bishop('b', 1, Colour.BLACK, Type.BISHOP));
//        addToBoard("g7", (T) new Pawn('g', 7, Colour.WHITE, Type.PAWN));

        //En Passant King Protect Ally Piece from Check 1
//        addToBoard("e5", (T) new King('e', 5, Colour.WHITE, Type.KING));
//        addToBoard("d5", (T) new Pawn('d', 5, Colour.WHITE, Type.PAWN));
//        addToBoard("h7", (T) new King('h', 7, Colour.BLACK, Type.KING));
//        addToBoard("b3", (T) new Rook('b', 3, Colour.BLACK, Type.ROOK));
//        addToBoard("c3", (T) new Pawn('c', 3, Colour.BLACK, Type.PAWN));
//        addToBoard("g7", (T) new Pawn('g', 7, Colour.WHITE, Type.PAWN));

        //Ally Piece Discovers King 2
//        addToBoard("b1", (T) new Bishop('b', 1, Colour.BLACK, Type.BISHOP));
//        addToBoard("h7", (T) new King('h', 7, Colour.WHITE, Type.KING));
//        addToBoard("g5", (T) new King('g', 5, Colour.BLACK, Type.KING));
//        addToBoard("d3", (T) new Bishop('d', 3, Colour.WHITE, Type.BISHOP));
//        addToBoard("c3", (T) new Bishop('c', 3, Colour.WHITE, Type.ROOK));
//        addToBoard("e5", (T) new Bishop('e', 5, Colour.BLACK, Type.BISHOP));
//        addToBoard("b4", (T) new Rook('b', 4, Colour.WHITE, Type.BISHOP));
//        addToBoard("e3", (T) new Knight('e', 3, Colour.WHITE, Type.KNIGHT));
//        addToBoard("g6", (T) new Knight('g', 6, Colour.BLACK, Type.KNIGHT));
////        addToBoard("g6", (T) new Knight('g', 6, Colour.BLACK, Type.KNIGHT));

        //Pawn Ally Piece Discovery 1
//        addToBoard("h7", (T) new King('h', 7, Colour.WHITE, Type.KING));
//        addToBoard("a3", (T) new King('a', 3, Colour.BLACK, Type.KING));
//        addToBoard("g7", (T) new Pawn('g', 7, Colour.WHITE, Type.PAWN));
//        addToBoard("b2", (T) new Pawn('b', 2, Colour.BLACK, Type.PAWN));
//        addToBoard("c3", (T) new Rook('c', 3, Colour.BLACK, Type.ROOK));
//        addToBoard("e4", (T) new Rook('e', 4, Colour.WHITE, Type.BISHOP));

        //Deflecting check with pieces 1
//        addToBoard("d1", (T) new King('d', 1, Colour.WHITE, Type.KING));
//        addToBoard("b1", (T) new King('b', 1, Colour.BLACK, Type.KING));
//        addToBoard("c3", (T) new Bishop('c', 3, Colour.WHITE, Type.BISHOP));
//        addToBoard("e5", (T) new Rook('e', 5, Colour.BLACK, Type.ROOK));
//        addToBoard("e3", (T) new Rook('e', 3, Colour.WHITE, Type.ROOK));

        //LegalMovesList rework test 1
//        addToBoard("e1", (T) new King('e', 1, Colour.WHITE, Type.KING));
//        addToBoard("a1", (T) new King('a', 1, Colour.BLACK, Type.KING));
//        addToBoard("c4", (T) new Bishop('c', 4, Colour.WHITE, Type.BISHOP));
//        addToBoard("e2", (T) new Bishop('e', 2, Colour.BLACK, Type.BISHOP));
//        addToBoard("c7", (T) new Rook('c', 7, Colour.BLACK, Type.ROOK));
//        addToBoard("f7", (T) new Rook('f', 7, Colour.WHITE, Type.ROOK));
//        addToBoard("e3", (T) new Knight('e', 3, Colour.WHITE, Type.KNIGHT));
//        addToBoard("g3", (T) new Knight('g', 3, Colour.BLACK, Type.KNIGHT));

        //King castling test 1
//        addToBoard("e1", (T) new King('e', 1, Colour.WHITE, Type.KING));
//        addToBoard("e8", (T) new King('e', 8, Colour.BLACK, Type.KING));
//        addToBoard("a1", (T) new Rook('a', 1, Colour.WHITE, Type.ROOK));
//        addToBoard("h8", (T) new Rook('h', 8, Colour.BLACK, Type.ROOK));
//        addToBoard("h1", (T) new Rook('h', 1, Colour.WHITE, Type.ROOK));
//        addToBoard("a8", (T) new Rook('a', 8, Colour.BLACK, Type.ROOK));
//        addToBoard("d5", (T) new Queen('d', 5, Colour.WHITE, Type.QUEEN));

        //Knight King printing test 1
//        addToBoard("h8", (T) new King('h', 8, Colour.WHITE, Type.KING));
//        addToBoard("a1", (T) new King('a', 1, Colour.BLACK, Type.KING));
//        addToBoard("e4", (T) new Knight('e', 4, Colour.BLACK, Type.KNIGHT));
//        addToBoard("d6", (T) new Knight('d', 6, Colour.WHITE, Type.KNIGHT));

        //First turn calculating protection test 1
//        addToBoard("e1", (T) new King('e', 1, Colour.WHITE, Type.KING));
//        addToBoard("a1", (T) new King('a', 1, Colour.BLACK, Type.KING));
//        addToBoard("e2", (T) new Knight('e', 2, Colour.BLACK, Type.KNIGHT));
//        addToBoard("a2", (T) new Knight('a', 2, Colour.WHITE, Type.KNIGHT));
//        addToBoard("g4", (T) new Bishop('g', 4, Colour.BLACK, Type.BISHOP));
//        addToBoard("c4", (T) new Bishop('c', 4, Colour.WHITE, Type.BISHOP));

        //Protection test 1
//        addToBoard("e1", (T) new King('e', 1, Colour.WHITE, Type.KING));
//        addToBoard("a1", (T) new King('a', 1, Colour.BLACK, Type.KING));

        //Checkmate test 1
//        addToBoard("e1", (T) new King('e', 1, Colour.WHITE, Type.KING));
//        addToBoard("a1", (T) new King('a', 1, Colour.BLACK, Type.KING));
//        addToBoard("d4", (T) new Bishop('d', 4, Colour.BLACK, Type.BISHOP));
//        addToBoard("a7", (T) new Bishop('a', 7, Colour.WHITE, Type.BISHOP));
//        addToBoard("h5", (T) new Rook('h', 5, Colour.BLACK, Type.ROOK));
//        addToBoard("g6", (T) new Rook('g', 6, Colour.WHITE, Type.ROOK));
//        addToBoard("f5", (T) new Rook('f', 5, Colour.WHITE, Type.ROOK));

        //Checkmate test 2
//        addToBoard("a1", (T) new King('a', 1, Colour.BLACK, Type.KING));
//        addToBoard("b2", (T) new Bishop('b', 2, Colour.BLACK, Type.BISHOP));
//        addToBoard("b5", (T) new Pawn('b', 5, Colour.BLACK, Type.PAWN));
//        addToBoard("c7", (T) new Pawn('c', 7, Colour.WHITE, Type.PAWN));
//        addToBoard("d5", (T) new Queen('d', 5, Colour.BLACK, Type.QUEEN));
//        addToBoard("e5", (T) new Rook('e', 5, Colour.BLACK, Type.ROOK));
//        addToBoard("f8", (T) new King('f', 8, Colour.WHITE, Type.KING));
//        addToBoard("g1", (T) new Rook('g', 1, Colour.BLACK, Type.ROOK));
//        addToBoard("h5", (T) new Pawn('h', 5, Colour.WHITE, Type.PAWN));

        //Checkmate test 3
//        addToBoard("h5", (T) new King('h', 5, Colour.BLACK));
//        addToBoard("e5", (T) new Knight('e', 5, Colour.WHITE));
//        addToBoard("a4", (T) new Bishop('a', 4, Colour.WHITE));
//        addToBoard("g4", (T) new Rook('g', 4, Colour.WHITE));
//        addToBoard("g2", (T) new Rook('g', 2, Colour.WHITE));
//        addToBoard("a7", (T) new Rook('a', 7, Colour.BLACK));
//        addToBoard("e7", (T) new Bishop('e', 7, Colour.BLACK));
//        addToBoard("b4", (T) new King('b', 4, Colour.WHITE));

        //Pawn calculateAttackingMoves test 1
//                addToBoard("h8", (T) new King('h', 8, Colour.WHITE, Type.KING));
//                addToBoard("a1", (T) new King('a', 1, Colour.BLACK, Type.KING));
//                addToBoard("e2", (T) new Pawn('e', 2, Colour.BLACK, Type.PAWN));
//                addToBoard("d7", (T) new Pawn('d', 7, Colour.WHITE, Type.PAWN));

        //Stalemate test 1
//        addToBoard("a8", (T) new King('a', 8, Colour.BLACK));
//        addToBoard("h1", (T) new King('h', 1, Colour.WHITE));
//        addToBoard("c5", (T) new Queen('c', 5, Colour.WHITE));

        //Stalemate test 2
//        addToBoard("h5", (T) new King('h', 5, Colour.BLACK));
//        addToBoard("h3", (T) new Pawn('h', 3, Colour.BLACK));
//        addToBoard("f5", (T) new King('f', 5, Colour.WHITE));
//        addToBoard("f6", (T) new Pawn('f', 6, Colour.WHITE));
//        addToBoard("g7", (T) new Pawn('g', 7, Colour.WHITE));
//        addToBoard("a1", (T) new Knight('a', 1, Colour.WHITE));


        //Pawn en passant pin test 1
//        addToBoard("a4", (T) new King('a', 4, Colour.BLACK));
//        addToBoard("h7", (T) new King('h', 7, Colour.WHITE));
//        addToBoard("b2", (T) new Pawn('b', 2, Colour.BLACK));
//        addToBoard("c6", (T) new Pawn('c', 6, Colour.WHITE));
//        addToBoard("g5", (T) new Rook('g', 5, Colour.WHITE));
//        addToBoard("f4", (T) new Bishop('f', 4, Colour.BLACK));

        //Checkmate test 4 (by En Passant 1)
//        addToBoard("a1", (T) new King('a', 1, Colour.BLACK));
//        addToBoard("f8", (T) new King('f', 8, Colour.WHITE));
//        addToBoard("g1", (T) new Rook('g', 1, Colour.BLACK));
//        addToBoard("e5", (T) new Rook('e', 5, Colour.BLACK));
//        addToBoard("d5", (T) new Queen('d', 5, Colour.BLACK));
//        addToBoard("b2", (T) new Bishop('b', 2, Colour.BLACK));
//        addToBoard("b5", (T) new Pawn('b', 5, Colour.BLACK));
//        addToBoard("c7", (T) new Pawn('c', 7, Colour.WHITE));
//        addToBoard("h8", (T) new Bishop('h', 8, Colour.WHITE));

        //Pawn promotion test 1
//        addToBoard("a1", (T) new King('a', 1, Colour.WHITE));
//        addToBoard("a8", (T) new King('a', 8, Colour.BLACK));
//        addToBoard("c7", (T) new Pawn('c', 7, Colour.BLACK));
//        addToBoard("h3", (T) new Pawn('h', 3, Colour.WHITE));

    }


    /**
     * Adds the pieces in the Map
     */
    public void addToBoard(String coordinates, T piece) {
        chessPiecesMap.put(coordinates, piece);
        chessPiecesMapMediator.setData(chessPiecesMap);

        if (piece.getColor().equals(Colour.WHITE)) {
            whitePlayer.addPiece(piece);
        } else {
            blackPlayer.addPiece(piece);
        }
        showPieceToBoard(piece);
    }


    /**
     * Shows the pieces in the GUI
     */
    protected void showPieceToBoard(T piece) {
        Image image = null;
        ImageView imageView;

        switch (piece.getClass().getSimpleName()) {
            case "Rook" -> {
                if (piece.getColor().equals(Colour.BLACK)) {
                    image = new Image(getClass().getResourceAsStream("/com.koleff.chess/pictures/black_rook.png"), cellWidth, cellHeight, false, false);
                } else {
                    image = new Image(getClass().getResourceAsStream("/com.koleff.chess/pictures/white_rook.png"), cellWidth, cellHeight, false, false);
                }
                break;
            }
            case "Knight" -> {
                if (piece.getColor().equals(Colour.BLACK)) {
                    image = new Image(getClass().getResourceAsStream("/com.koleff.chess/pictures/black_knight.png"), cellWidth, cellHeight, false, false);
                } else {
                    image = new Image(getClass().getResourceAsStream("/com.koleff.chess/pictures/white_knight.png"), cellWidth, cellHeight, false, false);
                }
                break;
            }
            case "Bishop" -> {
                if (piece.getColor().equals(Colour.BLACK)) {
                    image = new Image(getClass().getResourceAsStream("/com.koleff.chess/pictures/black_bishop.png"), cellWidth, cellHeight, false, false);
                } else {
                    image = new Image(getClass().getResourceAsStream("/com.koleff.chess/pictures/white_bishop.png"), cellWidth, cellHeight, false, false);
                }
                break;
            }
            case "Queen" -> {
                if (piece.getColor().equals(Colour.BLACK)) {
                    image = new Image(getClass().getResourceAsStream("/com.koleff.chess/pictures/black_queen.png"), cellWidth, cellHeight, false, false);
                } else {
                    image = new Image(getClass().getResourceAsStream("/com.koleff.chess/pictures/white_queen.png"), cellWidth, cellHeight, false, false);
                }
                break;
            }
            case "King" -> {
                if (piece.getColor().equals(Colour.BLACK)) {
                    image = new Image(getClass().getResourceAsStream("/com.koleff.chess/pictures/black_king.png"), cellWidth, cellHeight, false, false);
                } else {
                    image = new Image(getClass().getResourceAsStream("/com.koleff.chess/pictures/white_king.png"), cellWidth, cellHeight, false, false);
                }
                break;
            }
            case "Pawn" -> {
                if (piece.getColor().equals(Colour.BLACK)) {
                    image = new Image(getClass().getResourceAsStream("/com.koleff.chess/pictures/black_pawn.png"), cellWidth, cellHeight, false, false);
                } else {
                    image = new Image(getClass().getResourceAsStream("/com.koleff.chess/pictures/white_pawn.png"), cellWidth, cellHeight, false, false);
                }
                break;
            }
        }
        imageView = new ImageView();
        imageView.setImage(image);
        gridPane.add(imageView, piece.getCoordinatesXInt() - 1, 8 - piece.getCoordinatesY());
    }

    /**
     * Called when first painting the board
     */
    public static void paintBoard(GridPane startingGridPane) {
        gridPane = startingGridPane;

        paintBoard();
    }

    /**
     * Paints the board in 2 colours
     */
    public static void paintBoard() {
        Rectangle rectangle;
        boolean lastSquareWasWhite = true;

        for (int j = 0; j < gridPane.getRowCount(); j++) {
            if (lastSquareWasWhite) { // Last square of each row is the same as the first in the new row -> no reset needed
                lastSquareWasWhite = false;
            } else {
                lastSquareWasWhite = true;
            }
            for (int i = 0; i < gridPane.getColumnCount(); i++) {
                if (lastSquareWasWhite) {
                    rectangle = new Rectangle(cellWidth, cellHeight + 1, Color.valueOf("#996633"));//Coffee Brown //Color.BLACK
                    lastSquareWasWhite = false;
                } else {
                    rectangle = new Rectangle(cellWidth, cellHeight + 1, Color.valueOf("#F8F2DA")); //Creme White //Color.White
                    lastSquareWasWhite = true;
                }
                gridPane.add(rectangle, i, j);
            }
        }
    }


    /**
     * Arrange the board based on the pieces in the Map
     * (arrangeBoardByMap)
     */
    public void updateBoard() {
        paintBoard();
        if (hasSelectedPiece) {
            clearGridPane();
        }
        for (T piece : ((LinkedHashMap<String, T>) chessPiecesMapMediator.getData()).values()) {
            showPieceToBoard(piece);
        }
    }

    /**
     * Reset the board back to its original arrangement
     */
    public void resetBoard() {
        paintBoard();
        clearGridPane();

        chessPiecesMap.clear();
//        arrangeBoard();
        arrangeTestingBoard();
    }

    private static void clearGridPane() {
        List<Node> children = gridPane.getChildren().stream()
                .filter(e -> e instanceof ImageView)
                .collect(Collectors.toList());

        for (Node node : children) {
            gridPane.getChildren().remove(node);
        }
    }
}