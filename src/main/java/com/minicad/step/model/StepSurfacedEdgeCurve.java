package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved SURFACED_EDGE_CURVE.
 * An edge curve with associated surface geometry.
 *
 * @param id STEP instance id
 * @param name edge name
 * @param edgeGeometry the underlying curve
 * @param surface1 first associated surface
 * @param surface2 second associated surface
 * @param sameSurface flag indicating surfaces are identical
 */
/**
 * Resolved SURFACED_EDGE_CURVE.
 * An edge curve with associated surface geometry.
 *
 * @param id STEP instance id
 * @param name edge name
 * @param edgeGeometry the underlying curve
 * @param surface1 first associated surface
 * @param surface2 second associated surface
 * @param sameSurface flag indicating surfaces are identical
 */
public final class StepSurfacedEdgeCurve implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity edgeGeometry;
    private final StepEntity surface1;
    private final StepEntity surface2;
    private final boolean sameSurface;

    public StepSurfacedEdgeCurve(int id, String name, StepEntity edgeGeometry, StepEntity surface1, StepEntity surface2, boolean sameSurface) {
        this.id = id;
        this.name = name;
        this.edgeGeometry = edgeGeometry;
        this.surface1 = surface1;
        this.surface2 = surface2;
        this.sameSurface = sameSurface;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getEdgeGeometry() {
        return edgeGeometry;
    }

    public StepEntity getSurface1() {
        return surface1;
    }

    public StepEntity getSurface2() {
        return surface2;
    }

    public boolean isSameSurface() {
        return sameSurface;
    }

    // Record-style accessors
    public StepEntity edgeGeometry() { return getEdgeGeometry(); }
    public StepEntity surface1() { return getSurface1(); }
    public StepEntity surface2() { return getSurface2(); }
    public boolean sameSurface() { return isSameSurface(); }
    public boolean reversed() { return false; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSurfacedEdgeCurve that = (StepSurfacedEdgeCurve) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(edgeGeometry, that.edgeGeometry) && Objects.equals(surface1, that.surface1) && Objects.equals(surface2, that.surface2) && sameSurface == that.sameSurface;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, edgeGeometry, surface1, surface2, sameSurface);
    }

    @Override
    public String toString() {
        return "StepSurfacedEdgeCurve{" + "id=" + id + "name=" + name + "edgeGeometry=" + edgeGeometry + "surface1=" + surface1 + "surface2=" + surface2 + "sameSurface=" + sameSurface + "}";
    }
}
