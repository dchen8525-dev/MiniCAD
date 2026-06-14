package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved SWEPT_DISK_SOLID.
 * A solid formed by sweeping a circular disk along a curve.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param sweptCurve the curve along which to sweep
 * @param radius disk radius
 * @param innerRadius inner disk radius (0 for solid disk)
 */
/**
 * Resolved SWEPT_DISK_SOLID.
 * A solid formed by sweeping a circular disk along a curve.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param sweptCurve the curve along which to sweep
 * @param radius disk radius
 * @param innerRadius inner disk radius (0 for solid disk)
 */
public final class StepSweptDiskSolid implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity sweptCurve;
    private final double radius;
    private final Double innerRadius;

    public StepSweptDiskSolid(int id, String name, StepEntity sweptCurve, double radius, Double innerRadius) {
        this.id = id;
        this.name = name;
        this.sweptCurve = sweptCurve;
        this.radius = radius;
        this.innerRadius = innerRadius;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getSweptCurve() {
        return sweptCurve;
    }

    public double getRadius() {
        return radius;
    }

    public Double getInnerRadius() {
        return innerRadius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSweptDiskSolid that = (StepSweptDiskSolid) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(sweptCurve, that.sweptCurve) && radius == that.radius && Objects.equals(innerRadius, that.innerRadius);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sweptCurve, radius, innerRadius);
    }

    @Override
    public String toString() {
        return "StepSweptDiskSolid{" + "id=" + id + "name=" + name + "sweptCurve=" + sweptCurve + "radius=" + radius + "innerRadius=" + innerRadius + "}";
    }
}
