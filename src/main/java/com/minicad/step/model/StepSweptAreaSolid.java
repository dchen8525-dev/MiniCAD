package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal parse-only swept area solid.
 *
 * @param id step id
 * @param name step label
 * @param sweptArea profile definition to sweep
 * @param position solid placement
 * @param sweepReference extrusion direction or revolution axis
 * @param parameter depth or angle in STEP order
 * @param entityName concrete STEP entity name
 */
public final class StepSweptAreaSolid extends AbstractStepEntity {
    private final StepProfileDef sweptArea;
    private final StepAxis2Placement3D position;
    private final StepEntity sweepReference;
    private final double parameter;
    private final String entityName;

    public StepSweptAreaSolid(int id, String name, StepProfileDef sweptArea, StepAxis2Placement3D position, StepEntity sweepReference, double parameter, String entityName) {
        super(id, name);
        this.sweptArea = sweptArea;
        this.position = position;
        this.sweepReference = sweepReference;
        this.parameter = parameter;
        this.entityName = entityName;
    }

    public StepProfileDef getSweptArea() {
        return sweptArea;
    }

    public StepAxis2Placement3D getPosition() {
        return position;
    }

    public StepEntity getSweepReference() {
        return sweepReference;
    }

    public double getParameter() {
        return parameter;
    }

    public String getEntityName() {
        return entityName;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepProfileDef sweptArea() { return getSweptArea(); }
    public StepAxis2Placement3D position() { return getPosition(); }
    public StepEntity sweepReference() { return getSweepReference(); }
    public double parameter() { return getParameter(); }
    public String entityName() { return getEntityName(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("sweptArea", sweptArea);
        state.put("position", position);
        state.put("sweepReference", sweepReference);
        state.put("parameter", parameter);
        state.put("entityName", entityName);
        return state;
    }
}
