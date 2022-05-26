package com.koleff.chess.Pieces;

public abstract class Piece {
    /**
     * Fields
     */
    protected char coordinatesX; //a-h (1-8 converted in int values)
    protected int coordinatesY; //1-8
    protected Colour pieceColor;
    protected boolean isProtected = false; //if true -> can't be taken by the enemy king
    public boolean hasMoved = false;

    /**
     * Constructors
     */
    public Piece() {

    }

    public Piece(char coordinatesX, int coordinatesY, Colour pieceColor) {
        this.coordinatesX = coordinatesX;
        this.coordinatesY = coordinatesY;
        this.pieceColor = pieceColor;
        hasMoved = false; //this.
    }


    public Piece(Piece piece) {
//        super(piece.coordinatesX, piece.coordinatesY, piece.pieceColor);
        this.coordinatesX = piece.coordinatesX;
        this.coordinatesY = piece.coordinatesY;
        this.pieceColor = piece.pieceColor;
        this.isProtected = piece.isProtected;
        this.hasMoved = piece.hasMoved;
    }

    public abstract Piece copy();
    public abstract void move();


    /**
     * Getters & Setters
     */
    public Colour getColor() {
        return pieceColor;
    }

    public String getCoordinates() {
        return coordinatesX + Integer.toString(coordinatesY);
    }

    public void setIsProtected(boolean isProtected) {
        this.isProtected = isProtected;
    }

    public boolean getIsProtected() {
        return isProtected;
    }

    public void setCoordinates(String coordinates) {
        this.coordinatesX = coordinates.charAt(0);
        this.coordinatesY = Integer.parseInt(String.valueOf(coordinates.charAt(1)));
    }

    /**
     * When needed to set coordinates for gridPane (each cell)
     */
    public int getCoordinatesXInt() {
        switch (getCoordinatesXChar()) {
            case 'a':
                return 1;
            case 'b':
                return 2;
            case 'c':
                return 3;
            case 'd':
                return 4;
            case 'e':
                return 5;
            case 'f':
                return 6;
            case 'g':
                return 7;
            case 'h':
                return 8;
            default:
                return -1;
        }
    }

    public char getCoordinatesXChar() {
        return coordinatesX;
    }

    public void setCoordinatesXChar(char coordinatesY) {
        this.coordinatesX = coordinatesY;
    }

    public int getCoordinatesY() {
        return coordinatesY;
    }

    public void setCoordinatesY(int coordinatesY) {
        this.coordinatesY = coordinatesY;
    }

}
