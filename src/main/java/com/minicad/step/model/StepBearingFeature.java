package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepBearingFeature extends AbstractStepEntity {
    private final String bearingType;
    private final double boreDiameter;
    private final double outerDiameter;
    private final double bearingWidth;
    private final int numberOfElements;
    private final String bearingStandard;
    private final StepEntity bearingPlacement;

    public StepBearingFeature(int id, String name, String bearingType, double boreDiameter, double outerDiameter, double bearingWidth, int numberOfElements, String bearingStandard, StepEntity bearingPlacement) {
        super(id, name);
        this.bearingType = bearingType;
        this.boreDiameter = boreDiameter;
        this.outerDiameter = outerDiameter;
        this.bearingWidth = bearingWidth;
        this.numberOfElements = numberOfElements;
        this.bearingStandard = bearingStandard;
        this.bearingPlacement = bearingPlacement;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("bearingType", bearingType);
        state.put("boreDiameter", boreDiameter);
        state.put("outerDiameter", outerDiameter);
        state.put("bearingWidth", bearingWidth);
        state.put("numberOfElements", numberOfElements);
        state.put("bearingStandard", bearingStandard);
        state.put("bearingPlacement", bearingPlacement);
        return state;
    }
}
