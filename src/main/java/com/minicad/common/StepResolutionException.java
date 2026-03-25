package com.minicad.common;

/**
 * Exception raised when STEP semantic resolution fails.
 */
public class StepResolutionException extends IllegalArgumentException {

    /**
     * Creates a resolution exception.
     *
     * @param message error message
     */
    public StepResolutionException(String message) {
        super(message);
    }
}
