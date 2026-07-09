package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved BLENDED_SURFACE.
 * A blended surface entity.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param blendingType blending type classification
 * @param primarySurface primary surface for blend
 * @param secondarySurface secondary surface for blend
 * @param blendRadius blend radius
 * @param blendCurve blend curve defining the blend path
 */
/**
 * Resolved BLENDED_SURFACE.
 * A blended surface entity.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param blendingType blending type classification
 * @param primarySurface primary surface for blend
 * @param secondarySurface secondary surface for blend
 * @param blendRadius blend radius
 * @param blendCurve blend curve defining the blend path
 */
public final class StepBlendedSurface implements StepEntity {
    private final int id;
    private final String name;
    private final String blendingType;
    private final StepEntity primarySurface;
    private final StepEntity secondarySurface;
    private final double blendRadius;
    private final StepEntity blendCurve;

    public StepBlendedSurface(int id, String name, String blendingType, StepEntity primarySurface, StepEntity secondarySurface, double blendRadius, StepEntity blendCurve) {
        this.id = id;
        this.name = name;
        this.blendingType = blendingType;
        this.primarySurface = primarySurface;
        this.secondarySurface = secondarySurface;
        this.blendRadius = blendRadius;
        this.blendCurve = blendCurve;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBlendingType() {
        return blendingType;
    }

    public StepEntity getPrimarySurface() {
        return primarySurface;
    }

    public StepEntity getSecondarySurface() {
        return secondarySurface;
    }

    public double getBlendRadius() {
        return blendRadius;
    }

    public StepEntity getBlendCurve() {
        return blendCurve;
    }

    // Record-style accessors
    public StepEntity primarySurface() { return getPrimarySurface(); }
    public StepEntity secondarySurface() { return getSecondarySurface(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBlendedSurface that = (StepBlendedSurface) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(blendingType, that.blendingType) && Objects.equals(primarySurface, that.primarySurface) && Objects.equals(secondarySurface, that.secondarySurface) && blendRadius == that.blendRadius && Objects.equals(blendCurve, that.blendCurve);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, blendingType, primarySurface, secondarySurface, blendRadius, blendCurve);
    }

    @Override
    public String toString() {
        return "StepBlendedSurface{" + "id=" + id + "name=" + name + "blendingType=" + blendingType + "primarySurface=" + primarySurface + "secondarySurface=" + secondarySurface + "blendRadius=" + blendRadius + "blendCurve=" + blendCurve + "}";
    }
}