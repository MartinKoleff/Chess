package com.koleff.chess.Pieces;


public class Queen extends Piece{
    /**
     * Fields
     */

    /**
     * Constructors
     */

    /**
     * @param coordinatesX
     * @param coordinatesY
     * @param pieceColor
     */
    public Queen(char coordinatesX, int coordinatesY, Colour pieceColor) {
        super(coordinatesX, coordinatesY, pieceColor);
        hasMoved = false;
    }

    public Queen(char coordinatesX, int coordinatesY, Colour pieceColor, boolean isProtected, boolean hasMoved) {
        super(coordinatesX, coordinatesY, pieceColor);
        this.isProtected = isProtected;
        this.hasMoved = hasMoved;
    }

    public Queen(Piece piece) {
        super(piece.getCoordinatesXChar(), piece.getCoordinatesY(), piece.getColor());
    }

    @Override
    public Queen copy() {
        return new Queen(coordinatesX, coordinatesY, pieceColor, isProtected, hasMoved);
    }

    @Override
    public void move() {
        Rook rook = new Rook(this.coordinatesX, this.coordinatesY, this.pieceColor, this.isProtected, this.hasMoved);
        Bishop bishop = new Bishop(this.coordinatesX, this.coordinatesY, this.pieceColor, this.isProtected, this.hasMoved);

        rook.move();
        bishop.move();
    }
}
