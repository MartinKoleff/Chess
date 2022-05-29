package com.koleff.chess.CoordinatesAndMoves;

import com.koleff.chess.BoardAndFEN.Board;
import javafx.scene.input.MouseEvent;

import static com.koleff.chess.BoardAndFEN.ChessBoardController.CELL_WIDTH;

public class Coordinates {

    /**
     * Turns mouse click coordinates into X chess board coordinates
     *
     * @param mouseEvent the coordinates of the mouse click
     * @return X coordinates as char
     */
    public static char calculateX(MouseEvent mouseEvent) {
        double coordinatesX = mouseEvent.getX();

        return calculateX(coordinatesX);
    }

    /**
     * Turning X number coordinates into char coordinates
     *
     * @param coordinatesX X coordinates as number
     * @return X coordinates as char
     */
    public static char calculateX(double coordinatesX) {
        double coordinates;

        //If called with Mouse Event
        if (coordinatesX > 8) {
            coordinates = coordinatesX;
        } else {
            coordinates = (coordinatesX) * 100;
        }


        if (coordinates <= CELL_WIDTH && coordinates >= 0) {
            return 'a';
        } else if (coordinates > CELL_WIDTH && coordinates <= CELL_WIDTH * 2) {
            return 'b';
        } else if (coordinates > CELL_WIDTH * 2 && coordinates <= CELL_WIDTH * 3) {
            return 'c';
        } else if (coordinates > CELL_WIDTH * 3 && coordinates <= CELL_WIDTH * 4) {
            return 'd';
        } else if (coordinates > CELL_WIDTH * 4 && coordinates <= CELL_WIDTH * 5) {
            return 'e';
        } else if (coordinates > CELL_WIDTH * 5 && coordinates <= CELL_WIDTH * 6) {
            return 'f';
        } else if (coordinates > CELL_WIDTH * 6 && coordinates <= CELL_WIDTH * 7) {
            return 'g';
        } else if (coordinates > CELL_WIDTH * 7 && coordinates <= CELL_WIDTH * 8) {
            return 'h';
        } else {
            try {
                throw new Exception("Error on the board. You have selected square out of bounds. Try again.");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            } finally {
                return 0;
            }
        }
    }

    /**
     * Turning char coordinates into int coordinates
     *
     * @param coordinatesX coordinates as char
     */
    public static int calculateX(char coordinatesX) {
        switch (coordinatesX) {
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
                try {
                    throw new Exception("Invalid coordinates. Try again.");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                } finally {
                    return 0;
                }
        }
    }

    /**
     * Turns mouse click coordinates into y chess board coordinates
     *
     * @param mouseEvent the coordinates of the mouse click
     * @return Y chess board coordinates
     */
    public static int calculateY(MouseEvent mouseEvent) {
        int coordinates = (int) mouseEvent.getY();

        int gridPaneHeight = (int) Board.getGridPane().getHeight();
        int widthPerSquare = gridPaneHeight / 8;

        if (coordinates < widthPerSquare) {
            return 8;
        } else if (coordinates > widthPerSquare && coordinates <= widthPerSquare * 2) {
            return 7;
        } else if (coordinates > widthPerSquare * 2 && coordinates <= widthPerSquare * 3) {
            return 6;
        } else if (coordinates > widthPerSquare * 3 && coordinates <= widthPerSquare * 4) {
            return 5;
        } else if (coordinates > widthPerSquare * 4 && coordinates <= widthPerSquare * 5) {
            return 4;
        } else if (coordinates > widthPerSquare * 5 && coordinates <= widthPerSquare * 6) {
            return 3;
        } else if (coordinates > widthPerSquare * 6 && coordinates <= widthPerSquare * 7) {
            return 2;
        } else if (coordinates > widthPerSquare * 7 && coordinates <= widthPerSquare * 8) {
            return 1;
        } else {
            try {
                throw new Exception("Error on the board. You have selected square out of bounds. Try again.");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            } finally {
                return 0;
            }
        }
    }

    /**
     * Combines X and Y coordinates
     *
     * @return String chess board coordinates
     */
    public static String getCoordinatesToString(char coordinatesX, int coordinatesY) {
        String coordinates = coordinatesX + Integer.toString(coordinatesY);
        return coordinates;
    }

    public static String getCoordinatesToString(int coordinatesX, int coordinatesY) {
        String coordinates = calculateX(coordinatesX) + Integer.toString(coordinatesY);
        if (coordinatesAreValid(coordinatesX, coordinatesY)) {
            return coordinates;
        } else {
            return "";
        }
    }

    /**
     * Validates coordinates
     *
     * @param coordinatesX X coordinates as number
     * @param coordinatesY Y coordinates as number
     * @return if coordinates between 1 and 8
     */
    private static boolean coordinatesAreValid(int coordinatesX, int coordinatesY) {
        if (coordinatesX >= 1 && coordinatesX <= 8 && coordinatesY >= 1 && coordinatesY <= 8) {
            return true;
        }
        return false;
    }
}


