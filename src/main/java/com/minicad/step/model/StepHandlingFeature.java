package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved HANDLING_FEATURE.
 * A handling feature entity.
 *
 * @param id STEP instance id
 * @param name handling name
 * @param handlingType handling type (lift, grab, support, transport)
 * @param handlingGeometry handling geometry representation
 * @param handlingPoints handling point locations
 * @param handlingWeight handling weight capacity
 * @param handlingEquipment handling equipment reference
 */
public final class StepHandlingFeature extends AbstractStepEntity {
    private final String handlingType;
    private final StepEntity handlingGeometry;
    private final List<StepEntity> handlingPoints;
    private final double handlingWeight;
    private final StepEntity handlingEquipment;

    public StepHandlingFeature(int id, String name, String handlingType, StepEntity handlingGeometry, List<StepEntity> handlingPoints, double handlingWeight, StepEntity handlingEquipment) {
        super(id, name);
        this.handlingType = handlingType;
        this.handlingGeometry = handlingGeometry;
        this.handlingPoints = handlingPoints == null ? null : java.util.List.copyOf(handlingPoints);
        this.handlingWeight = handlingWeight;
        this.handlingEquipment = handlingEquipment;
    }

    public String getHandlingType() {
        return handlingType;
    }

    public StepEntity getHandlingGeometry() {
        return handlingGeometry;
    }

    public List<StepEntity> getHandlingPoints() {
        return handlingPoints;
    }

    public double getHandlingWeight() {
        return handlingWeight;
    }

    public StepEntity getHandlingEquipment() {
        return handlingEquipment;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("handlingType", handlingType);
        state.put("handlingGeometry", handlingGeometry);
        state.put("handlingPoints", handlingPoints);
        state.put("handlingWeight", handlingWeight);
        state.put("handlingEquipment", handlingEquipment);
        return state;
    }
}
