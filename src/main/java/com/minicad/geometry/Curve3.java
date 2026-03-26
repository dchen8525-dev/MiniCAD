package com.minicad.geometry;

/**
 * Minimal curve abstraction for topology-backed geometry.
 */
public interface Curve3 {

    /**
     * Returns whether a point lies on the curve within epsilon.
     *
     * @param point queried point
     * @return whether the point lies on the curve
     */
    boolean contains(CartesianPoint point);
}
