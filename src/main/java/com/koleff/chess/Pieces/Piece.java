package com.koleff.chess.Pieces;

public abstract class Piece{
    /**
     * Fields
     */
    protected char coordinatesX; //a-h
    protected int coordinatesY; //1-8
    protected Colour pieceColor;
    protected boolean isProtected = false; //if true then can't be taken by the enemy king

    //Encapsulate... (to protected)
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

    public void setCoordinates(String coordinates) {
        this.coordinatesX = coordinates.charAt(0);
        this.coordinatesY = Integer.parseInt(String.valueOf(coordinates.charAt(1)));
    }

    public boolean getIsProtected() {
        return isProtected;
    }
    public void setIsProtected(boolean isProtected) {
        this.isProtected = isProtected;
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

    /**
     * Transforms char X coordinates to number value
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

    @Override
    public String toString(){
        return this.getCoordinates() + " "
                + this.getClass().getSimpleName() + " "
                + this.getColor().toString() + " "
                + this.hasMoved;
    }
}
