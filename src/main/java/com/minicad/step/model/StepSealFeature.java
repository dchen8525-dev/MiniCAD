package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SEAL_FEATURE.
 * A seal feature entity.
 *
 * @param id STEP instance id
 * @param name seal name
 * @param sealType seal type classification (O-ring, gasket, lip seal)
 * @param innerDiameter inner diameter
 * @param outerDiameter outer diameter
 * @param sealWidth seal width/cross-section
 * @param sealMaterial seal material specification
 * @param sealPlacement seal position placement
 */
/**
 * Resolved SEAL_FEATURE.
 * A seal feature entity.
 *
 * @param id STEP instance id
 * @param name seal name
 * @param sealType seal type classification (O-ring, gasket, lip seal)
 * @param innerDiameter inner diameter
 * @param outerDiameter outer diameter
 * @param sealWidth seal width/cross-section
 * @param sealMaterial seal material specification
 * @param sealPlacement seal position placement
 */
public final class StepSealFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String sealType;
    private final double innerDiameter;
    private final double outerDiameter;
    private final double sealWidth;
    private final StepEntity sealMaterial;
    private final StepEntity sealPlacement;

    public StepSealFeature(int id, String name, String sealType, double innerDiameter, double outerDiameter, double sealWidth, StepEntity sealMaterial, StepEntity sealPlacement) {
        this.id = id;
        this.name = name;
        this.sealType = sealType;
        this.innerDiameter = innerDiameter;
        this.outerDiameter = outerDiameter;
        this.sealWidth = sealWidth;
        this.sealMaterial = sealMaterial;
        this.sealPlacement = sealPlacement;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSealType() {
        return sealType;
    }

    public double getInnerDiameter() {
        return innerDiameter;
    }

    public double getOuterDiameter() {
        return outerDiameter;
    }

    public double getSealWidth() {
        return sealWidth;
    }

    public StepEntity getSealMaterial() {
        return sealMaterial;
    }

    public StepEntity getSealPlacement() {
        return sealPlacement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSealFeature that = (StepSealFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(sealType, that.sealType) && innerDiameter == that.innerDiameter && outerDiameter == that.outerDiameter && sealWidth == that.sealWidth && Objects.equals(sealMaterial, that.sealMaterial) && Objects.equals(sealPlacement, that.sealPlacement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sealType, innerDiameter, outerDiameter, sealWidth, sealMaterial, sealPlacement);
    }

    @Override
    public String toString() {
        return "StepSealFeature{" + "id=" + id + "name=" + name + "sealType=" + sealType + "innerDiameter=" + innerDiameter + "outerDiameter=" + outerDiameter + "sealWidth=" + sealWidth + "sealMaterial=" + sealMaterial + "sealPlacement=" + sealPlacement + "}";
    }
}