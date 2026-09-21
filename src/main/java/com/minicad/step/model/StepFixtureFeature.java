package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FIXTURE_FEATURE.
 * A fixture feature entity.
 *
 * @param id STEP instance id
 * @param name fixture name
 * @param fixtureType fixture type (clamping, supporting, locating)
 * @param fixtureGeometry fixture geometry representation
 * @param clampingPoints clamping point locations
 * @param supportingPoints supporting point locations
 * @param fixtureForce fixture force specification
 * @param fixtureMaterial fixture material reference
 */
public final class StepFixtureFeature extends AbstractStepEntity {
    private final String fixtureType;
    private final StepEntity fixtureGeometry;
    private final List<StepEntity> clampingPoints;
    private final List<StepEntity> supportingPoints;
    private final double fixtureForce;
    private final StepEntity fixtureMaterial;

    public StepFixtureFeature(int id, String name, String fixtureType, StepEntity fixtureGeometry, List<StepEntity> clampingPoints, List<StepEntity> supportingPoints, double fixtureForce, StepEntity fixtureMaterial) {
        super(id, name);
        this.fixtureType = fixtureType;
        this.fixtureGeometry = fixtureGeometry;
        this.clampingPoints = clampingPoints == null ? null : java.util.List.copyOf(clampingPoints);
        this.supportingPoints = supportingPoints == null ? null : java.util.List.copyOf(supportingPoints);
        this.fixtureForce = fixtureForce;
        this.fixtureMaterial = fixtureMaterial;
    }

    public String getFixtureType() {
        return fixtureType;
    }

    public StepEntity getFixtureGeometry() {
        return fixtureGeometry;
    }

    public List<StepEntity> getClampingPoints() {
        return clampingPoints;
    }

    public List<StepEntity> getSupportingPoints() {
        return supportingPoints;
    }

    public double getFixtureForce() {
        return fixtureForce;
    }

    public StepEntity getFixtureMaterial() {
        return fixtureMaterial;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("fixtureType", fixtureType);
        state.put("fixtureGeometry", fixtureGeometry);
        state.put("clampingPoints", clampingPoints);
        state.put("supportingPoints", supportingPoints);
        state.put("fixtureForce", fixtureForce);
        state.put("fixtureMaterial", fixtureMaterial);
        return state;
    }
}
