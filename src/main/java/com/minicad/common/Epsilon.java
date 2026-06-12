package com.minicad.common;

/**
 * Global numeric tolerance used by the teaching implementation.
 */
public final class Epsilon {

    /**
     * A single global tolerance for floating-point comparisons.
     */
    public static final double EPS = 1.0e-9;

    /**
     * Tolerance for STEP import topology connectivity after geometric projection.
     */
    public static final double IMPORT_TOPOLOGY_TOLERANCE = 1.0e-2;

    /**
     * Tolerance for STEP import curve membership after endpoint projection.
     */
    public static final double IMPORT_CURVE_TOLERANCE = 1.0e-2;

    /**
     * Tolerance for STEP import planar face membership after coordinate rounding.
     */
    public static final double IMPORT_PLANE_TOLERANCE = 1.0e-2;

    private Epsilon() {
    }

    /**
     * Returns {@code true} if two values are equal within {@link #EPS}.
     *
     * @param a first value
     * @param b second value
     * @return whether the values are approximately equal
     */
    public static boolean equals(double a, double b) {
        return Math.abs(a - b) <= EPS;
    }

    /**
     * Returns {@code true} if the value is approximately zero.
     *
     * @param value tested value
     * @return whether the value is near zero
     */
    public static boolean isZero(double value) {
        return Math.abs(value) <= EPS;
    }
}
