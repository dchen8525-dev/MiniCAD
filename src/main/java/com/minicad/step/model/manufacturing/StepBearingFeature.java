package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved BEARING_FEATURE.
 * A bearing feature entity.
 *
 * @param id STEP instance id
 * @param name bearing name
 * @param bearingType bearing type classification (ball, roller, needle, plain)
 * @param boreDiameter bore (inner) diameter
 * @param outerDiameter outer diameter
 * @param bearingWidth bearing width
 * @param numberOfElements number of bearing elements (balls, rollers)
 * @param bearingStandard bearing standard specification
 * @param bearingPlacement bearing position placement
 */
/**
 * Resolved BEARING_FEATURE.
 * A bearing feature entity.
 *
 * @param id STEP instance id
 * @param name bearing name
 * @param bearingType bearing type classification (ball, roller, needle, plain)
 * @param boreDiameter bore (inner) diameter
 * @param outerDiameter outer diameter
 * @param bearingWidth bearing width
 * @param numberOfElements number of bearing elements (balls, rollers)
 * @param bearingStandard bearing standard specification
 * @param bearingPlacement bearing position placement
 */
public final class StepBearingFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String bearingType;
    private final double boreDiameter;
    private final double outerDiameter;
    private final double bearingWidth;
    private final int numberOfElements;
    private final String bearingStandard;
    private final StepEntity bearingPlacement;

    public StepBearingFeature(int id, String name, String bearingType, double boreDiameter, double outerDiameter, double bearingWidth, int numberOfElements, String bearingStandard, StepEntity bearingPlacement) {
        this.id = id;
        this.name = name;
        this.bearingType = bearingType;
        this.boreDiameter = boreDiameter;
        this.outerDiameter = outerDiameter;
        this.bearingWidth = bearingWidth;
        this.numberOfElements = numberOfElements;
        this.bearingStandard = bearingStandard;
        this.bearingPlacement = bearingPlacement;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBearingType() {
        return bearingType;
    }

    public double getBoreDiameter() {
        return boreDiameter;
    }

    public double getOuterDiameter() {
        return outerDiameter;
    }

    public double getBearingWidth() {
        return bearingWidth;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public String getBearingStandard() {
        return bearingStandard;
    }

    public StepEntity getBearingPlacement() {
        return bearingPlacement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBearingFeature that = (StepBearingFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(bearingType, that.bearingType) && boreDiameter == that.boreDiameter && outerDiameter == that.outerDiameter && bearingWidth == that.bearingWidth && numberOfElements == that.numberOfElements && Objects.equals(bearingStandard, that.bearingStandard) && Objects.equals(bearingPlacement, that.bearingPlacement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, bearingType, boreDiameter, outerDiameter, bearingWidth, numberOfElements, bearingStandard, bearingPlacement);
    }

    @Override
    public String toString() {
        return "StepBearingFeature{" + "id=" + id + "name=" + name + "bearingType=" + bearingType + "boreDiameter=" + boreDiameter + "outerDiameter=" + outerDiameter + "bearingWidth=" + bearingWidth + "numberOfElements=" + numberOfElements + "bearingStandard=" + bearingStandard + "bearingPlacement=" + bearingPlacement + "}";
    }
}