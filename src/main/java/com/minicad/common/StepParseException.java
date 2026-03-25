package com.minicad.common;

/**
 * Exception raised when STEP text cannot be parsed as supported syntax.
 */
public class StepParseException extends IllegalArgumentException {

    /**
     * Creates a parse exception.
     *
     * @param message error message
     */
    public StepParseException(String message) {
        super(message);
    }
}
