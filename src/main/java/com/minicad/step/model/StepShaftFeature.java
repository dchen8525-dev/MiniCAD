package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SHAFT_FEATURE.
 * A shaft feature entity.
 *
 * @param id STEP instance id
 * @param name shaft name
 * @param shaftDiameter shaft diameter
 * @param shaftLength shaft length
 * @param shaftType shaft type classification (solid, hollow, stepped)
 * @param features features on the shaft (keyways, threads, grooves)
 * @param shaftMaterial shaft material specification
 * @param surfaceTreatment surface treatment specification
 */
/**
 * Resolved SHAFT_FEATURE.
 * A shaft feature entity.
 *
 * @param id STEP instance id
 * @param name shaft name
 * @param shaftDiameter shaft diameter
 * @param shaftLength shaft length
 * @param shaftType shaft type classification (solid, hollow, stepped)
 * @param features features on the shaft (keyways, threads, grooves)
 * @param shaftMaterial shaft material specification
 * @param surfaceTreatment surface treatment specification
 */
public final class StepShaftFeature implements StepEntity {
    private final int id;
    private final String name;
    private final double shaftDiameter;
    private final double shaftLength;
    private final String shaftType;
    private final List<StepEntity> features;
    private final StepEntity shaftMaterial;
    private final StepEntity surfaceTreatment;

    public StepShaftFeature(int id, String name, double shaftDiameter, double shaftLength, String shaftType, List<StepEntity> features, StepEntity shaftMaterial, StepEntity surfaceTreatment) {
        this.id = id;
        this.name = name;
        this.shaftDiameter = shaftDiameter;
        this.shaftLength = shaftLength;
        this.shaftType = shaftType;
        this.features = features == null ? null : java.util.List.copyOf(features);
        this.shaftMaterial = shaftMaterial;
        this.surfaceTreatment = surfaceTreatment;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getShaftDiameter() {
        return shaftDiameter;
    }

    public double getShaftLength() {
        return shaftLength;
    }

    public String getShaftType() {
        return shaftType;
    }

    public List<StepEntity> getFeatures() {
        return features;
    }

    public StepEntity getShaftMaterial() {
        return shaftMaterial;
    }

    public StepEntity getSurfaceTreatment() {
        return surfaceTreatment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepShaftFeature that = (StepShaftFeature) o;
        return id == that.id && Objects.equals(name, that.name) && shaftDiameter == that.shaftDiameter && shaftLength == that.shaftLength && Objects.equals(shaftType, that.shaftType) && Objects.equals(features, that.features) && Objects.equals(shaftMaterial, that.shaftMaterial) && Objects.equals(surfaceTreatment, that.surfaceTreatment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, shaftDiameter, shaftLength, shaftType, features, shaftMaterial, surfaceTreatment);
    }

    @Override
    public String toString() {
        return "StepShaftFeature{" + "id=" + id + "name=" + name + "shaftDiameter=" + shaftDiameter + "shaftLength=" + shaftLength + "shaftType=" + shaftType + "features=" + features + "shaftMaterial=" + shaftMaterial + "surfaceTreatment=" + surfaceTreatment + "}";
    }
}