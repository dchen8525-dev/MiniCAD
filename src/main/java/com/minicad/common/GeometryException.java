package com.minicad.common;

/**
 * Exception raised when geometry invariants are violated.
 */
public class GeometryException extends IllegalArgumentException {

    /**
     * Creates a geometry exception.
     *
     * @param message error message
     */
    public GeometryException(String message) {
        super(message);
    }
}
