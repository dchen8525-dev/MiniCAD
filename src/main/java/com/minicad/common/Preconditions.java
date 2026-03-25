package com.minicad.common;

/**
 * Small validation helper for constructor invariants.
 */
public final class Preconditions {

    private Preconditions() {
    }

    /**
     * Ensures the value is finite.
     *
     * @param value numeric value
     * @param name field name
     */
    public static void requireFinite(double value, String name) {
        if (!Double.isFinite(value)) {
            throw new GeometryException(name + " must be finite");
        }
    }

    /**
     * Ensures the object is non-null.
     *
     * @param value object value
     * @param name field name
     * @param <T> object type
     * @return the validated object
     */
    public static <T> T requireNonNull(T value, String name) {
        if (value == null) {
            throw new GeometryException(name + " must not be null");
        }
        return value;
    }
}
