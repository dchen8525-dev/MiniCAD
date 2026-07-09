package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PLATING_FEATURE.
 * A plating feature entity.
 *
 * @param id STEP instance id
 * @param name plating name
 * @param platingType plating type (electroplating, electroless, anodizing)
 * @param platingMaterial plating material specification
 * @param platingThickness plating thickness
 * @param appliedSurfaces surfaces to be plated
 * @param platingParameters plating process parameters
 * @param platingQuality plating quality grade
 */
/**
 * Resolved PLATING_FEATURE.
 * A plating feature entity.
 *
 * @param id STEP instance id
 * @param name plating name
 * @param platingType plating type (electroplating, electroless, anodizing)
 * @param platingMaterial plating material specification
 * @param platingThickness plating thickness
 * @param appliedSurfaces surfaces to be plated
 * @param platingParameters plating process parameters
 * @param platingQuality plating quality grade
 */
public final class StepPlatingFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String platingType;
    private final StepEntity platingMaterial;
    private final double platingThickness;
    private final List<StepEntity> appliedSurfaces;
    private final List<Double> platingParameters;
    private final String platingQuality;

    public StepPlatingFeature(int id, String name, String platingType, StepEntity platingMaterial, double platingThickness, List<StepEntity> appliedSurfaces, List<Double> platingParameters, String platingQuality) {
        this.id = id;
        this.name = name;
        this.platingType = platingType;
        this.platingMaterial = platingMaterial;
        this.platingThickness = platingThickness;
        this.appliedSurfaces = appliedSurfaces == null ? null : java.util.List.copyOf(appliedSurfaces);
        this.platingParameters = platingParameters == null ? null : java.util.List.copyOf(platingParameters);
        this.platingQuality = platingQuality;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlatingType() {
        return platingType;
    }

    public StepEntity getPlatingMaterial() {
        return platingMaterial;
    }

    public double getPlatingThickness() {
        return platingThickness;
    }

    public List<StepEntity> getAppliedSurfaces() {
        return appliedSurfaces;
    }

    public List<Double> getPlatingParameters() {
        return platingParameters;
    }

    public String getPlatingQuality() {
        return platingQuality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPlatingFeature that = (StepPlatingFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(platingType, that.platingType) && Objects.equals(platingMaterial, that.platingMaterial) && platingThickness == that.platingThickness && Objects.equals(appliedSurfaces, that.appliedSurfaces) && Objects.equals(platingParameters, that.platingParameters) && Objects.equals(platingQuality, that.platingQuality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, platingType, platingMaterial, platingThickness, appliedSurfaces, platingParameters, platingQuality);
    }

    @Override
    public String toString() {
        return "StepPlatingFeature{" + "id=" + id + "name=" + name + "platingType=" + platingType + "platingMaterial=" + platingMaterial + "platingThickness=" + platingThickness + "appliedSurfaces=" + appliedSurfaces + "platingParameters=" + platingParameters + "platingQuality=" + platingQuality + "}";
    }
}