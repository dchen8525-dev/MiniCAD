package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ACCESS_FEATURE.
 * An access feature entity.
 *
 * @param id STEP instance id
 * @param name access name
 * @param accessType access type (door, panel, hatch, inspection)
 * @param accessGeometry access geometry representation
 * @param accessOpening access opening dimensions
 * @param accessLocation access location placement
 * @varianceFrequency access variance frequency (regular, emergency)
 */
public final class StepAccessFeature extends AbstractStepEntity {
    private final String accessType;
    private final StepEntity accessGeometry;
    private final List<Double> accessOpening;
    private final StepEntity accessLocation;
    private final String varianceFrequency;

    public StepAccessFeature(int id, String name, String accessType, StepEntity accessGeometry, List<Double> accessOpening, StepEntity accessLocation, String varianceFrequency) {
        super(id, name);
        this.accessType = accessType;
        this.accessGeometry = accessGeometry;
        this.accessOpening = accessOpening == null ? null : java.util.List.copyOf(accessOpening);
        this.accessLocation = accessLocation;
        this.varianceFrequency = varianceFrequency;
    }

    public String getAccessType() {
        return accessType;
    }

    public StepEntity getAccessGeometry() {
        return accessGeometry;
    }

    public List<Double> getAccessOpening() {
        return accessOpening;
    }

    public StepEntity getAccessLocation() {
        return accessLocation;
    }

    public String getVarianceFrequency() {
        return varianceFrequency;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("accessType", accessType);
        state.put("accessGeometry", accessGeometry);
        state.put("accessOpening", accessOpening);
        state.put("accessLocation", accessLocation);
        state.put("varianceFrequency", varianceFrequency);
        return state;
    }
}
