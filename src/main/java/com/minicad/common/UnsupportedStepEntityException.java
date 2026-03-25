package com.minicad.common;

/**
 * Exception raised when a STEP entity or parameter form is intentionally unsupported.
 */
public class UnsupportedStepEntityException extends StepResolutionException {

    /**
     * Creates an unsupported-entity exception.
     *
     * @param message error message
     */
    public UnsupportedStepEntityException(String message) {
        super(message);
    }
}
