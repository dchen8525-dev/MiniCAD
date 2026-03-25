package com.minicad.common;

/**
 * Exception raised when geometry or topology construction is intentionally unsupported.
 */
public class UnsupportedGeometryException extends IllegalArgumentException {

    /**
     * Creates an unsupported-geometry exception.
     *
     * @param message error message
     */
    public UnsupportedGeometryException(String message) {
        super(message);
    }
}
