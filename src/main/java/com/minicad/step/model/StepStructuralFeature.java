package com.minicad.step.model.product;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved STRUCTURAL_FEATURE.
 * A structural feature entity.
 *
 * @param id STEP instance id
 * @param name feature name
 * @param structuralType structural type (beam, column, plate, connection)
 * @param crossSection cross-section geometry
 * @param structuralLength length dimension
 * @param structuralMaterial material specification
 * @param endConditions end condition features
 * @param loadPoints load application points
 */
/**
 * Resolved STRUCTURAL_FEATURE.
 * A structural feature entity.
 *
 * @param id STEP instance id
 * @param name feature name
 * @param structuralType structural type (beam, column, plate, connection)
 * @param crossSection cross-section geometry
 * @param structuralLength length dimension
 * @param structuralMaterial material specification
 * @param endConditions end condition features
 * @param loadPoints load application points
 */
public final class StepStructuralFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String structuralType;
    private final StepEntity crossSection;
    private final double structuralLength;
    private final StepEntity structuralMaterial;
    private final List<StepEntity> endConditions;
    private final List<StepEntity> loadPoints;

    public StepStructuralFeature(int id, String name, String structuralType, StepEntity crossSection, double structuralLength, StepEntity structuralMaterial, List<StepEntity> endConditions, List<StepEntity> loadPoints) {
        this.id = id;
        this.name = name;
        this.structuralType = structuralType;
        this.crossSection = crossSection;
        this.structuralLength = structuralLength;
        this.structuralMaterial = structuralMaterial;
        this.endConditions = endConditions == null ? null : java.util.List.copyOf(endConditions);
        this.loadPoints = loadPoints == null ? null : java.util.List.copyOf(loadPoints);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStructuralType() {
        return structuralType;
    }

    public StepEntity getCrossSection() {
        return crossSection;
    }

    public double getStructuralLength() {
        return structuralLength;
    }

    public StepEntity getStructuralMaterial() {
        return structuralMaterial;
    }

    public List<StepEntity> getEndConditions() {
        return endConditions;
    }

    public List<StepEntity> getLoadPoints() {
        return loadPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStructuralFeature that = (StepStructuralFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(structuralType, that.structuralType) && Objects.equals(crossSection, that.crossSection) && structuralLength == that.structuralLength && Objects.equals(structuralMaterial, that.structuralMaterial) && Objects.equals(endConditions, that.endConditions) && Objects.equals(loadPoints, that.loadPoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, structuralType, crossSection, structuralLength, structuralMaterial, endConditions, loadPoints);
    }

    @Override
    public String toString() {
        return "StepStructuralFeature{" + "id=" + id + "name=" + name + "structuralType=" + structuralType + "crossSection=" + crossSection + "structuralLength=" + structuralLength + "structuralMaterial=" + structuralMaterial + "endConditions=" + endConditions + "loadPoints=" + loadPoints + "}";
    }
}