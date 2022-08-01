package com.koleff.chess.CoordinatesAndMoves;

import com.koleff.chess.BoardAndFEN.Board;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import com.koleff.chess.Pieces.*;
import com.koleff.chess.Player.Player;
import com.koleff.chess.Threads.CalculatingAttackingMovesRunnable;
import com.koleff.chess.Threads.CalculatingAttackingMovesThread;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.koleff.chess.BoardAndFEN.ChessBoardController.*;
import static com.koleff.chess.CoordinatesAndMoves.Coordinates.calculateX;
import static com.koleff.chess.CoordinatesAndMoves.Coordinates.getCoordinatesToString;

public class Moves implements Serializable {
    /**
     * Fields
     */
    private HashMap<String, Piece> chessPiecesMap = new HashMap(){
        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

             this.entrySet().forEach(e -> stringBuilder.append(((Map.Entry<String, Piece>)e).getKey())
                    .append(" ")
                    .append(((Map.Entry<String, Piece>)e).getValue()) //Compiler doesn't recognize the map value types... (implicit casting)
                    .append("\n")
            );
            return stringBuilder.toString();
        }

        @Override
        public boolean equals(Object o){
            Map oldPosition = (HashMap<String, Piece>)o;
            List<Piece> currentPositionPiecesList = new ArrayList<>(this.values());
            List<String> oldPositionCoordinatesList = new ArrayList<>(oldPosition.keySet());
            int counter = 0;

            //Checks if all pieces from current position exist in the old position
            for (Piece piece : currentPositionPiecesList){
                for (String oldPositionCoordinates : oldPositionCoordinatesList) {
                    if (piece.equals(oldPosition.get(oldPositionCoordinates))){
                        counter++;
                        break;
                    }
                }
                if(counter == 0){
                    return false;
                }
                counter = 0;
            }
            return true;
        }
    };//String - coordinate | Piece - the piece with that coordinates

    private List<String> legalMovesList = new ArrayList<>(); //String - coordinates
    private List<String> attackingMovesList = new ArrayList<>(); //String - coordinates
    private Piece selectedPiece = null;

    //Make non-static...
    public static boolean calculatingIfPieceProtectsKing = false;
    public static boolean isCalculatingKingDiscoveryFromAllyPiece = false;
    public static boolean isCalculatingAttackingMoves = false;
    public static boolean isCalculatingProtection = false;
    public static boolean isCalculatingCheckmate = false;
    public static boolean isCalculatingStalemate = false;
    public static boolean hasToBreak = false;

    public List<String> castlingMovesList = new ArrayList<>(); //String - coordinates
    private List<CalculatingAttackingMovesThread> calculateAttackingMovesThreadsList = new ArrayList<>();

    /**
     * Getters & Setters
     */
    public List<String> getAttackingMovesList() {
        return attackingMovesList;
    }

    public HashMap<String, Piece> getChessPiecesMap() {
        return chessPiecesMap;
    }

    public void setChessPiecesMap(HashMap<String, Piece> chessPiecesMap) {
        this.chessPiecesMap = chessPiecesMap;
    }

    public List<String> getLegalMovesList() {
        return legalMovesList;
    }

    public void setAttackingMovesList(List<String> attackingMovesList) {
        this.attackingMovesList = attackingMovesList;
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(Piece selectedPiece) {
        this.selectedPiece = selectedPiece;
    }


    /**
     * Calculates all attacking moves of the player with the color given
     *
     * @param pieceColor player pieces color
     * @return list with all attacking moves of the player with the color given
     */
    public List<String> calculateAttackingMoves(Colour pieceColor) {
        attackingMovesList.clear();
        isCalculatingAttackingMoves = true;

        List<Piece> playerPieces;
        Piece selectedPieceTemp = selectedPiece;

        playerPieces = chessPiecesMap.values().stream()
                .filter(e -> e.getColor().equals(pieceColor))
                .collect(Collectors.toList());

        calculateMoves(playerPieces);

        if (!calculatingIfPieceProtectsKing && !isCalculatingKingDiscoveryFromAllyPiece) {
            String playerColor = pieceColor.toString().charAt(0) + pieceColor.toString().substring(1).toLowerCase();

            System.out.println();
            System.out.printf("Attacking moves from the %s team: \n", playerColor);

            attackingMovesList = attackingMovesList.stream()
                    .sorted()
                    .distinct()
                    .collect(Collectors.toList());

            attackingMovesList.stream()
                    .forEach(e -> System.out.print(e + " "));
            System.out.println();
        }

        selectedPiece = selectedPieceTemp;
        isCalculatingAttackingMoves = false;

        return attackingMovesList;
    }


    /**
     * Calculates the protection of the player pieces with the color given
     * - for all player pieces is calculated what pieces each one protects
     *
     * @param pieceColor player pieces color
     */
    public void calculateProtection(Colour pieceColor) {
        List<Piece> playerPieces;
        Piece selectedPieceTemp = selectedPiece;

        playerPieces = chessPiecesMap.values().stream()
                .filter(e -> e.getColor().equals(pieceColor))
                .collect(Collectors.toList());

        isCalculatingProtection = true;
        calculateMoves(playerPieces);

        isCalculatingProtection = false;

        selectedPiece = selectedPieceTemp;
    }

    /**
     * Helping function
     *
     * @param playerPieces all pieces of a player
     * @see #calculateAttackingMoves(Colour)
     * @see #calculateProtection(Colour)
     */
    private void calculateMoves(List<Piece> playerPieces) {
        Piece selectedPieceTemp = selectedPiece;
        for (Piece piece : playerPieces) {
            setSelectedPiece(piece);

            CalculatingAttackingMovesRunnable runnable = new CalculatingAttackingMovesRunnable(piece);
            CalculatingAttackingMovesThread thread = new CalculatingAttackingMovesThread(runnable);

            thread.start();
            calculateAttackingMovesThreadsList.add(thread);
        }
        calculateAttackingMovesThreadsList.forEach(e -> e.requestStop());
        calculateAttackingMovesThreadsList.clear();

        setSelectedPiece(selectedPieceTemp);
    }

    /**
     * Clears the isProtected field for all pieces of the player with the given color
     *
     * @param pieceColor player pieces color
     */
    public void clearProtection(Colour pieceColor) {
        System.out.println();
        List<Piece> playerPieces;

        playerPieces = chessPiecesMap.values().stream()
                .filter(e -> e.getColor().equals(pieceColor))
                .collect(Collectors.toList());

        for (Piece piece : playerPieces) {
            if (piece.getIsProtected()) {
                System.out.printf("%s %s with player color %s was cleared of its protection!\n", piece.getClass().getSimpleName(), piece.getCoordinates(), piece.getColor());
                piece.setIsProtected(false);
            }
        }
    }

    /**
     * The brain of the chess engine
     * - checks if a move is legal
     * - checks if the selected piece can move to the coordinates given
     *
     * @param coordinates the coordinates where the selected piece or a chosen piece wants to go
     * @return false if the coordinates are legal (because it is not illegal move)
     * @see #selectedPiece
     */
    public boolean checkIfMoveIsIllegal(String coordinates) {
        //If coordinates are invalid.
        if (coordinates.equals("") || hasToBreak) {
            if (selectedPiece instanceof King || selectedPiece instanceof Pawn) {
                hasToBreak = false;
            }
            return true;
        }

        int coordinatesX = calculateX(coordinates.charAt(0));
        int coordinatesY = Integer.parseInt(String.valueOf(coordinates.charAt(1)));

        //Checks if the piece gets moved the ally king gets discovered
        if (!isCalculatingAttackingMoves && !isCalculatingProtection) {
            if (isCalculatingCheckmate || isCalculatingStalemate) {
                if (checkIfKingCanBeDiscovered(coordinates, nextTurnPlayer)) {
                    isCalculatingKingDiscoveryFromAllyPiece = false;
                } else {
                    return true;
                }
            } else if (checkIfKingCanBeDiscovered(coordinates, currentPlayer)) {
                isCalculatingKingDiscoveryFromAllyPiece = false;
            } else {
                isCalculatingKingDiscoveryFromAllyPiece = false;
                if (!nextTurnPlayer.isInCheck) { // (continue for the whole diagonal if king is checked)
                    return true;
                }
            }
        }

        try {
            if (!chessPiecesMap.get(coordinates).getColor().equals(selectedPiece.getColor())) { //Check if the found piece is the same color as the selected piece otherwise it's the last legal move
                if (!isCalculatingAttackingMoves && !isCalculatingProtection) {
                    if (!nextTurnPlayer.isInCheck) { //If king is not in check logic...
                        if (chessPiecesMap.get(coordinates).getIsProtected() && selectedPiece instanceof King) { //If king is not in check but is trying to get a piece that is protected by other piece from the same color
                            return true;
                        } else if (selectedPiece instanceof Pawn && selectedPiece.getCoordinatesXInt() != coordinatesX) { //Pawn logic...
                            showLegalMove(coordinates);
                            return false;
                        } else if (!(selectedPiece instanceof Pawn)) {
                            showLegalMove(coordinates);
                            hasToBreak = true; //Needed for the default pieces when protecting
                            return false;
                        }
                        hasToBreak = true;
                        return true;
                    } else {
                        try {
                            if (attackingMovesList.contains(coordinates) && selectedPiece instanceof King
                                    && (chessPiecesMap.get(coordinates)).getIsProtected()) { //If the king is moving and the square is illegal
                                return true;
                            } else {
                                return checkIfPieceDefendsCheck(coordinates);
                            }
                        } catch (RuntimeException e) {
                            return true; //There is no piece on the coordinates but its attacked
                        }
                    }
                } else {
                    if (isCalculatingProtection) {
                        if (chessPiecesMap.get(coordinates).getColor().equals(selectedPiece.getColor())) {
                            System.out.printf("%s %s is protecting %s %s with player color -> %s!!\n",
                                    selectedPiece.getClass().getSimpleName(),
                                    selectedPiece.getCoordinates(),
                                    chessPiecesMap.get(coordinates).getClass().getSimpleName(),
                                    coordinates, selectedPiece.getColor());
                            chessPiecesMap.get(coordinates).setIsProtected(true);
                            hasToBreak = true;
                            return true;
                        }
                        return false;
                    }
                    attackingMovesList.add(coordinates);
                    if (chessPiecesMap.get(coordinates) instanceof King) { //Adds the whole diagonal... King can't be on the same line as Rook/Queen
                        return false;
                    }
                }
                hasToBreak = true; //Used for blocking check
                return true;
            }
            if (isCalculatingProtection) {//selectedPiece protects the piece with current coordinates
                System.out.printf("%s %s is protecting %s %s with player color -> %s!\n",
                        selectedPiece.getClass().getSimpleName(),
                        selectedPiece.getCoordinates(),
                        chessPiecesMap.get(coordinates).getClass().getSimpleName(),
                        coordinates, selectedPiece.getColor());
                (chessPiecesMap.get(coordinates)).setIsProtected(true);
                hasToBreak = true;
                return true;
            } else if (nextTurnPlayer.isInCheck) {
                return checkIfPieceDefendsCheck(coordinates);
            }
        } catch (RuntimeException e) {
            if (isCalculatingProtection) {
                return false;
            }
            if (!isCalculatingAttackingMoves) {
                if (!nextTurnPlayer.isInCheck) {
                    if (!attackingMovesList.contains(coordinates) || !(selectedPiece instanceof King)) {
                        showLegalMove(coordinates);
                    }
                } else {
                    return checkIfPieceDefendsCheck(coordinates);
                }
//                showLegalMove(coordinates);
                return false;
            } else {
                attackingMovesList.add(coordinates);
            }
            return false;
        }
        hasToBreak = true;
        return true;
    }


    /**
     * Checks if after the move the chosen players king will be discovered
     * - if after calculateAttackingMoves() the returned list contains the king coordinates then it's illegal move
     *
     * @param coordinates the coordinates where the selected piece will move to
     * @param player      makes calculations for the given player
     * @return true if king can be discovered
     * @see #checkIfMoveIsIllegal(String) where this function is used
     * @see #calculateAttackingMoves(Colour)
     */
    private boolean checkIfKingCanBeDiscovered(String coordinates, Player player) { //checkKingDiscoverable
        Piece currentPiece = selectedPiece.copy();
        Piece tempPiece = selectedPiece.copy();

        Piece enemyPieceTemp = null;

        try {
            if (chessPiecesMap.get(coordinates).getColor().equals(player.getPlayerPiecesColor())) {
                hasToBreak = true;
                return true;
            } else { //The piece is from the enemy team (it gets removed through the calculations)
                enemyPieceTemp = chessPiecesMap.get(coordinates).copy();
            }
        } catch (RuntimeException e) {

        }
        chessPiecesMap.remove(selectedPiece.getCoordinates());
        currentPiece.setCoordinates(coordinates);
        chessPiecesMap.put(coordinates, currentPiece);

        isCalculatingKingDiscoveryFromAllyPiece = true;
        if (player.checkForKingChecks()) {
            chessPiecesMap.put(tempPiece.getCoordinates(), tempPiece);
            chessPiecesMap.remove(currentPiece.getCoordinates());
            selectedPiece = tempPiece.copy();

            if (enemyPieceTemp != null) { //Enemy piece has overlapping coordinates with the ally piece (gets removed)
                chessPiecesMap.put(enemyPieceTemp.getCoordinates(), enemyPieceTemp);
                hasToBreak = true;
            }
            return false; //can't be discovered (is in check) | return false - continue for the whole diagonal
        } else {
            chessPiecesMap.put(tempPiece.getCoordinates(), tempPiece); //selectedPiece
            chessPiecesMap.remove(currentPiece.getCoordinates());
            selectedPiece = tempPiece.copy();

            if (enemyPieceTemp != null) { //Enemy piece has overlapping coordinates with ally piece (gets removed)
                chessPiecesMap.put(enemyPieceTemp.getCoordinates(), enemyPieceTemp);
            }

            if (nextTurnPlayer.isInCheck) {
                legalMovesList.add(coordinates);
            }
            return true; //can protect (is not in check)
        }
    }


    /**
     * Checks if the selected piece defends check with its future move to the coordinates
     *
     * @param coordinates the coordinates of the future move of the selected piece
     * @return false if current player has deflected check (because it is not illegal move)
     * @see #selectedPiece
     * @see #checkIfMoveIsIllegal(String)
     */
    private boolean checkIfPieceDefendsCheck(String coordinates) {
        if (!isCalculatingAttackingMoves && selectedPiece instanceof King) { //King logic...
            try {
                if (chessPiecesMap.get(coordinates).getIsProtected()
                        || (chessPiecesMap.get(coordinates).getColor().equals(selectedPiece.getColor()))) { //If the piece that the King tries to take is protected then the move is illegal
                    hasToBreak = true;
                    return true;
                } else if (!chessPiecesMap.get(coordinates).getIsProtected()) { //If the piece that attacks the King is not protected
                    showLegalMove(coordinates);
                    return false;
                }
                hasToBreak = true;
                return true;
            } catch (RuntimeException e) {
                if (!attackingMovesList.contains(coordinates)) {
                    showLegalMove(coordinates);
                    return false;
                }
            }
            return true;
        } else {
            if (isCalculatingCheckmate || isCalculatingStalemate) {
                try {
                    if (chessPiecesMap.get(coordinates).getColor().equals(selectedPiece.getColor())) {
                        hasToBreak = true;
                        return true;
                    } else {
                        return checkIfPieceDeflectsCheck(coordinates, nextTurnPlayer);
                    }
                } catch (RuntimeException e) {
                    return checkIfPieceDeflectsCheck(coordinates, nextTurnPlayer);
                }
            } else {
                try {
                    if (selectedPiece instanceof Pawn
                            && selectedPiece.getCoordinatesXChar() == coordinates.charAt(0)
                            && !chessPiecesMap.get(coordinates).getColor().equals(selectedPiece.getColor())) {
                        legalMovesList.clear();
                        hasToBreak = true;
                        return true;
                    } else {
                        return checkIfPieceDeflectsCheck(coordinates, currentPlayer);
                    }
                } catch (NullPointerException e) {
                    return checkIfPieceDeflectsCheck(coordinates, currentPlayer);
                }
            }
        }
    }

    /**
     * Checks if the selected piece deflects check (if moving to the coordinates and blocks check)
     *
     * @param coordinates the coordinates of the future move of the selected piece
     * @param player  makes calculations for the given player
     * @return false if current player can deflect check (because it is not illegal move)
     * @see #checkIfPieceDefendsCheck(String)
     * @see #checkIfMoveIsIllegal(String)
     */
    private boolean checkIfPieceDeflectsCheck(String coordinates, Player player) {
        if (!isCalculatingAttackingMoves && attackingMovesList.contains(coordinates)
                || (nextTurnPlayer.isInCheck && !isCalculatingAttackingMoves)) {
            Piece removedPiece = null;

            if (chessPiecesMap.containsKey(coordinates)) { //There is a piece on the place of the coordinates
                removedPiece = chessPiecesMap.get(coordinates).copy();
            }

            chessPiecesMap.put(coordinates, selectedPiece);
            chessPiecesMap.remove(selectedPiece.getCoordinates());
            calculatingIfPieceProtectsKing = true;
            try {
                if (!player.checkForKingChecks() && legalMovesList.contains(coordinates)) {
                    chessPiecesMap.remove(coordinates);
                    chessPiecesMap.put(selectedPiece.getCoordinates(), selectedPiece);

                    if (removedPiece != null) { //If there was a piece that was removed (the new coordinates of the selectedPiece overlap with other piece)
                        chessPiecesMap.put(removedPiece.getCoordinates(), removedPiece);
                    }

                    if (!isCalculatingCheckmate && !isCalculatingStalemate) {
                        showLegalMove(coordinates);
                    } else {
                        legalMovesList.add(coordinates);
                    }
                    hasToBreak = true;
                    calculatingIfPieceProtectsKing = false;
                    return false; //(needed)
                }
            } catch (RuntimeException e) {
                System.out.printf("Exception with the %s king coordinates...\n", player.getPlayerPiecesColor());
                System.out.printf("King coordinates: %s\n", player.getKingCoordinates());
            }
            chessPiecesMap.remove(coordinates);
            chessPiecesMap.put(selectedPiece.getCoordinates(), selectedPiece);

            if (removedPiece != null) {  //If there was a piece that was removed (the new coordinates of the selectedPiece overlap with other piece)
                chessPiecesMap.put(removedPiece.getCoordinates(), removedPiece);
            }

            if (!chessPiecesMap.containsKey(coordinates)) { //If the coordinates are a Piece - last calculation...
                return true; //false (adds the whole diagonal / line)
            }
        }
        hasToBreak = true;
        return true;
    }

    /**
     * Shows the legal moves as green circles on the board
     * @param coordinates legal coordinates of the selected piece
     */
    public void showLegalMove(String coordinates) {
        int coordinatesX = calculateX(coordinates.charAt(0));
        int coordinatesY = Integer.parseInt(String.valueOf(coordinates.charAt(1)));

        Circle circle = new Circle(CELL_WIDTH / 4);
        circle.setFill(Color.GREEN);
//        circle.opacityProperty().setValue(5);
        Board.getGridPane().add(circle, coordinatesX - 1, 8 - coordinatesY); //Starts counting from 1 to 8 | bottom left corner - (1,1) | upper right corner - (8,8)
        Board.getGridPane().setHalignment(circle, HPos.CENTER);
        Board.getGridPane().setValignment(circle, VPos.CENTER);

        if (!legalMovesList.contains(coordinates)) {
            legalMovesList.add(coordinates);
        }
    }


    /**
     * Shows the legal moves as green circles on the board
     * @param coordinatesXCopy X legal coordinates of the selected piece
     * @param coordinatesYCopy Y legal coordinates of the selected piece
     */
    private void showLegalMove(int coordinatesXCopy, int coordinatesYCopy) {
        String coordinates = getCoordinatesToString(coordinatesXCopy, coordinatesYCopy);
        showLegalMove(coordinates);
    }
}
