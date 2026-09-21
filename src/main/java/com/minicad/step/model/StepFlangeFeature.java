package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FLANGE_FEATURE.
 * A flange feature entity.
 *
 * @param id STEP instance id
 * @param name flange name
 * @param flangeType flange type classification (flat, raised face, weld neck)
 * @param flangeDiameter flange outer diameter
 * @param flangeThickness flange thickness
 * @param boltHoles bolt hole features
 * @param boltCircle bolt circle diameter
 * @param numberOfBoltHoles number of bolt holes
 * @param flangeStandard flange standard specification
 */
public final class StepFlangeFeature extends AbstractStepEntity {
    private final String flangeType;
    private final double flangeDiameter;
    private final double flangeThickness;
    private final List<StepEntity> boltHoles;
    private final double boltCircle;
    private final int numberOfBoltHoles;
    private final String flangeStandard;

    public StepFlangeFeature(int id, String name, String flangeType, double flangeDiameter, double flangeThickness, List<StepEntity> boltHoles, double boltCircle, int numberOfBoltHoles, String flangeStandard) {
        super(id, name);
        this.flangeType = flangeType;
        this.flangeDiameter = flangeDiameter;
        this.flangeThickness = flangeThickness;
        this.boltHoles = boltHoles == null ? null : java.util.List.copyOf(boltHoles);
        this.boltCircle = boltCircle;
        this.numberOfBoltHoles = numberOfBoltHoles;
        this.flangeStandard = flangeStandard;
    }

    public String getFlangeType() {
        return flangeType;
    }

    public double getFlangeDiameter() {
        return flangeDiameter;
    }

    public double getFlangeThickness() {
        return flangeThickness;
    }

    public List<StepEntity> getBoltHoles() {
        return boltHoles;
    }

    public double getBoltCircle() {
        return boltCircle;
    }

    public int getNumberOfBoltHoles() {
        return numberOfBoltHoles;
    }

    public String getFlangeStandard() {
        return flangeStandard;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("flangeType", flangeType);
        state.put("flangeDiameter", flangeDiameter);
        state.put("flangeThickness", flangeThickness);
        state.put("boltHoles", boltHoles);
        state.put("boltCircle", boltCircle);
        state.put("numberOfBoltHoles", numberOfBoltHoles);
        state.put("flangeStandard", flangeStandard);
        return state;
    }
}
