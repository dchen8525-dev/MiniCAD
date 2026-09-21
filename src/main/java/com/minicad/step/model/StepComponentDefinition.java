package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved COMPONENT_DEFINITION.
 * A component definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceComponent defined variance component
 * @varianceFunction component variance function
 * @varianceInterface component variance interface specification
 * @varianceDependencies component variance dependencies
 * @varianceProperties component variance properties
 * @varianceStatus definition variance status
 */
public final class StepComponentDefinition extends AbstractStepEntity {
    private final StepEntity varianceComponent;
    private final String varianceFunction;
    private final StepEntity varianceInterface;
    private final List<StepEntity> varianceDependencies;
    private final List<StepEntity> varianceProperties;
    private final String varianceStatus;

    public StepComponentDefinition(int id, String name, StepEntity varianceComponent, String varianceFunction, StepEntity varianceInterface, List<StepEntity> varianceDependencies, List<StepEntity> varianceProperties, String varianceStatus) {
        super(id, name);
        this.varianceComponent = varianceComponent;
        this.varianceFunction = varianceFunction;
        this.varianceInterface = varianceInterface;
        this.varianceDependencies = varianceDependencies == null ? null : java.util.List.copyOf(varianceDependencies);
        this.varianceProperties = varianceProperties == null ? null : java.util.List.copyOf(varianceProperties);
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceComponent() {
        return varianceComponent;
    }

    public String getVarianceFunction() {
        return varianceFunction;
    }

    public StepEntity getVarianceInterface() {
        return varianceInterface;
    }

    public List<StepEntity> getVarianceDependencies() {
        return varianceDependencies;
    }

    public List<StepEntity> getVarianceProperties() {
        return varianceProperties;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceComponent", varianceComponent);
        state.put("varianceFunction", varianceFunction);
        state.put("varianceInterface", varianceInterface);
        state.put("varianceDependencies", varianceDependencies);
        state.put("varianceProperties", varianceProperties);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
