package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Minimal surface of constant radius representation.
 * A surface swept by maintaining a constant radius along a path.
 * This can represent pipe-like surfaces with constant cross-section radius.
 *
 * @param sweptSurface the base surface being swept
 * @param radius the constant radius
 */
/**
 * Minimal surface of constant radius representation.
 * A surface swept by maintaining a constant radius along a path.
 * This can represent pipe-like surfaces with constant cross-section radius.
 *
 * @param sweptSurface the base surface being swept
 * @param radius the constant radius
 */
public final class SurfaceOfConstantRadius3 implements SurfaceGeometry {
    private final SurfaceGeometry sweptSurface;
    private final double radius;

    public SurfaceOfConstantRadius3(SurfaceGeometry sweptSurface, double radius) {
        this.sweptSurface = sweptSurface;
        this.radius = radius;
    }

    public SurfaceGeometry getSweptSurface() {
        return sweptSurface;
    }

    public double getRadius() {
        return radius;
    }

    // Record-style accessors
    public SurfaceGeometry sweptSurface() { return getSweptSurface(); }
    public double radius() { return getRadius(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurfaceOfConstantRadius3 that = (SurfaceOfConstantRadius3) o;
        return Objects.equals(sweptSurface, that.sweptSurface) && radius == that.radius;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sweptSurface, radius);
    }

    @Override
    public String toString() {
        return "SurfaceOfConstantRadius3{" + "sweptSurface=" + sweptSurface + "radius=" + radius + "}";
    }
}