package com.minicad.common;

/**
 * Exception raised when topology invariants are violated.
 */
public class TopologyException extends IllegalArgumentException {

    /**
     * Creates a topology exception.
     *
     * @param message error message
     */
    public TopologyException(String message) {
        super(message);
    }
}
