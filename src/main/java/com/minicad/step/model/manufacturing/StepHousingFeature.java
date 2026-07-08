package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved HOUSING_FEATURE.
 * A housing feature entity.
 *
 * @param id STEP instance id
 * @param name housing name
 * @param housingType housing type classification
 * @param bearingSeats bearing seat features
 * @param mountingFeatures mounting features (bolt holes, dowels)
 * @param sealGrooves seal groove features
 * @param housingMaterial housing material specification
 * @param housingGeometry housing geometry representation
 */
/**
 * Resolved HOUSING_FEATURE.
 * A housing feature entity.
 *
 * @param id STEP instance id
 * @param name housing name
 * @param housingType housing type classification
 * @param bearingSeats bearing seat features
 * @param mountingFeatures mounting features (bolt holes, dowels)
 * @param sealGrooves seal groove features
 * @param housingMaterial housing material specification
 * @param housingGeometry housing geometry representation
 */
public final class StepHousingFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String housingType;
    private final List<StepEntity> bearingSeats;
    private final List<StepEntity> mountingFeatures;
    private final List<StepEntity> sealGrooves;
    private final StepEntity housingMaterial;
    private final StepEntity housingGeometry;

    public StepHousingFeature(int id, String name, String housingType, List<StepEntity> bearingSeats, List<StepEntity> mountingFeatures, List<StepEntity> sealGrooves, StepEntity housingMaterial, StepEntity housingGeometry) {
        this.id = id;
        this.name = name;
        this.housingType = housingType;
        this.bearingSeats = bearingSeats == null ? null : java.util.List.copyOf(bearingSeats);
        this.mountingFeatures = mountingFeatures == null ? null : java.util.List.copyOf(mountingFeatures);
        this.sealGrooves = sealGrooves == null ? null : java.util.List.copyOf(sealGrooves);
        this.housingMaterial = housingMaterial;
        this.housingGeometry = housingGeometry;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHousingType() {
        return housingType;
    }

    public List<StepEntity> getBearingSeats() {
        return bearingSeats;
    }

    public List<StepEntity> getMountingFeatures() {
        return mountingFeatures;
    }

    public List<StepEntity> getSealGrooves() {
        return sealGrooves;
    }

    public StepEntity getHousingMaterial() {
        return housingMaterial;
    }

    public StepEntity getHousingGeometry() {
        return housingGeometry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepHousingFeature that = (StepHousingFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(housingType, that.housingType) && Objects.equals(bearingSeats, that.bearingSeats) && Objects.equals(mountingFeatures, that.mountingFeatures) && Objects.equals(sealGrooves, that.sealGrooves) && Objects.equals(housingMaterial, that.housingMaterial) && Objects.equals(housingGeometry, that.housingGeometry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, housingType, bearingSeats, mountingFeatures, sealGrooves, housingMaterial, housingGeometry);
    }

    @Override
    public String toString() {
        return "StepHousingFeature{" + "id=" + id + "name=" + name + "housingType=" + housingType + "bearingSeats=" + bearingSeats + "mountingFeatures=" + mountingFeatures + "sealGrooves=" + sealGrooves + "housingMaterial=" + housingMaterial + "housingGeometry=" + housingGeometry + "}";
    }
}