package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal parse-only conic curve for PARABOLA and HYPERBOLA.
 *
 * @param id STEP instance id
 * @param name curve name
 * @param position curve placement
 * @param parameters numeric conic parameters
 * @param entityName concrete STEP entity name
 */
public final class StepConicCurve extends AbstractStepEntity {
    private final StepEntity position;
    private final List<Double> parameters;
    private final String entityName;

    public StepConicCurve(int id, String name, StepEntity position, List<Double> parameters, String entityName) {
        super(id, name);
        this.position = position;
        this.parameters = parameters == null ? null : java.util.List.copyOf(parameters);
        this.entityName = entityName;
    }

    public StepEntity getPosition() {
        return position;
    }

    public List<Double> getParameters() {
        return parameters;
    }

    public String getEntityName() {
        return entityName;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity position() { return getPosition(); }
    public List<Double> parameters() { return getParameters(); }
    public String entityName() { return getEntityName(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("position", position);
        state.put("parameters", parameters);
        state.put("entityName", entityName);
        return state;
    }
}
