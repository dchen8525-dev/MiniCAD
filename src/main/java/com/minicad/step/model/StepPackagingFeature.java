package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PACKAGING_FEATURE.
 * A packaging feature entity.
 *
 * @param id STEP instance id
 * @param name packaging name
 * @param packagingType packaging type (box, pallet, crate)
 * @param packagingGeometry packaging geometry representation
 * @param packagingMaterial packaging material specification
 * @param packagingWeight packaging weight
 * @param packagingDimensions packaging dimensions (L, W, H)
 * @param packagingStandard packaging standard reference
 */
/**
 * Resolved PACKAGING_FEATURE.
 * A packaging feature entity.
 *
 * @param id STEP instance id
 * @param name packaging name
 * @param packagingType packaging type (box, pallet, crate)
 * @param packagingGeometry packaging geometry representation
 * @param packagingMaterial packaging material specification
 * @param packagingWeight packaging weight
 * @param packagingDimensions packaging dimensions (L, W, H)
 * @param packagingStandard packaging standard reference
 */
public final class StepPackagingFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String packagingType;
    private final StepEntity packagingGeometry;
    private final StepEntity packagingMaterial;
    private final double packagingWeight;
    private final List<Double> packagingDimensions;
    private final String packagingStandard;

    public StepPackagingFeature(int id, String name, String packagingType, StepEntity packagingGeometry, StepEntity packagingMaterial, double packagingWeight, List<Double> packagingDimensions, String packagingStandard) {
        this.id = id;
        this.name = name;
        this.packagingType = packagingType;
        this.packagingGeometry = packagingGeometry;
        this.packagingMaterial = packagingMaterial;
        this.packagingWeight = packagingWeight;
        this.packagingDimensions = packagingDimensions == null ? null : java.util.List.copyOf(packagingDimensions);
        this.packagingStandard = packagingStandard;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPackagingType() {
        return packagingType;
    }

    public StepEntity getPackagingGeometry() {
        return packagingGeometry;
    }

    public StepEntity getPackagingMaterial() {
        return packagingMaterial;
    }

    public double getPackagingWeight() {
        return packagingWeight;
    }

    public List<Double> getPackagingDimensions() {
        return packagingDimensions;
    }

    public String getPackagingStandard() {
        return packagingStandard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPackagingFeature that = (StepPackagingFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(packagingType, that.packagingType) && Objects.equals(packagingGeometry, that.packagingGeometry) && Objects.equals(packagingMaterial, that.packagingMaterial) && packagingWeight == that.packagingWeight && Objects.equals(packagingDimensions, that.packagingDimensions) && Objects.equals(packagingStandard, that.packagingStandard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, packagingType, packagingGeometry, packagingMaterial, packagingWeight, packagingDimensions, packagingStandard);
    }

    @Override
    public String toString() {
        return "StepPackagingFeature{" + "id=" + id + "name=" + name + "packagingType=" + packagingType + "packagingGeometry=" + packagingGeometry + "packagingMaterial=" + packagingMaterial + "packagingWeight=" + packagingWeight + "packagingDimensions=" + packagingDimensions + "packagingStandard=" + packagingStandard + "}";
    }
}