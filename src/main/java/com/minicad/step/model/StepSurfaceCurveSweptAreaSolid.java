package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved SURFACE_CURVE_SWEPT_AREA_SOLID.
 * A swept solid where the trajectory follows a surface curve.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param sweptArea profile to sweep
 * @param referenceSurface surface the trajectory follows
 * @param trajectory path curve
 * @param startPoint start parameter
 * @param endPoint end parameter
 */
/**
 * Resolved SURFACE_CURVE_SWEPT_AREA_SOLID.
 * A swept solid where the trajectory follows a surface curve.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param sweptArea profile to sweep
 * @param referenceSurface surface the trajectory follows
 * @param trajectory path curve
 * @param startPoint start parameter
 * @param endPoint end parameter
 */
public final class StepSurfaceCurveSweptAreaSolid implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity sweptArea;
    private final StepEntity referenceSurface;
    private final StepEntity trajectory;
    private final double startPoint;
    private final double endPoint;

    public StepSurfaceCurveSweptAreaSolid(int id, String name, StepEntity sweptArea, StepEntity referenceSurface, StepEntity trajectory, double startPoint, double endPoint) {
        this.id = id;
        this.name = name;
        this.sweptArea = sweptArea;
        this.referenceSurface = referenceSurface;
        this.trajectory = trajectory;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getSweptArea() {
        return sweptArea;
    }

    public StepEntity getReferenceSurface() {
        return referenceSurface;
    }

    public StepEntity getTrajectory() {
        return trajectory;
    }

    public double getStartPoint() {
        return startPoint;
    }

    public double getEndPoint() {
        return endPoint;
    }

    // Record-style accessors
    public StepEntity sweptArea() { return sweptArea; }
    public StepEntity trajectory() { return trajectory; }
    public double startPoint() { return startPoint; }
    public double endPoint() { return endPoint; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSurfaceCurveSweptAreaSolid that = (StepSurfaceCurveSweptAreaSolid) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(sweptArea, that.sweptArea) && Objects.equals(referenceSurface, that.referenceSurface) && Objects.equals(trajectory, that.trajectory) && startPoint == that.startPoint && endPoint == that.endPoint;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sweptArea, referenceSurface, trajectory, startPoint, endPoint);
    }

    @Override
    public String toString() {
        return "StepSurfaceCurveSweptAreaSolid{" + "id=" + id + "name=" + name + "sweptArea=" + sweptArea + "referenceSurface=" + referenceSurface + "trajectory=" + trajectory + "startPoint=" + startPoint + "endPoint=" + endPoint + "}";
    }
}
