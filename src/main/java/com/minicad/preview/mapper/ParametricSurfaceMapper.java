package com.minicad.preview.mapper;

import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Vector3;

/**
 * Interface for mapping parametric surfaces to UV coordinates.
 */
interface ParametricSurfaceMapper {
    UvPoint project(CartesianPoint point, UvPoint previous);

    CartesianPoint pointAt(double u, double v);

    Vector3 normalAt(double u, double v);

    default Double uPeriod() {
        return null;
    }

    default Double vPeriod() {
        return null;
    }
}