package com.koleff.chess.unused;

import com.koleff.chess.Pieces.Piece;

/**
 * This class is used to store variables (non-static) and access them in different classes.
 * Singleton doesn't work because you need the data, not just the instance.
 */
public class Mediator<T> {
    private T data;

    public Mediator(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
