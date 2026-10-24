package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved SEQUENCE_DEFINITION.
 * A sequence definition entity.
 *
 * @param id STEP instance id
 * @param name sequence name
 * @param sequenceType sequence variance type
 * @param sequenceItems sequence variance item definitions
 * @param sequenceOrder sequence variance ordering policy
 * @param sequenceStatus sequence variance status
 */
public final class StepSequenceDefinition extends AbstractStepEntity {
    private final String sequenceType;
    private final List<StepEntity> sequenceItems;
    private final String sequenceOrder;
    private final String sequenceStatus;

    public StepSequenceDefinition(int id, String name, String sequenceType, List<StepEntity> sequenceItems, String sequenceOrder, String sequenceStatus) {
        super(id, name);
        this.sequenceType = sequenceType;
        this.sequenceItems = sequenceItems == null ? null : java.util.List.copyOf(sequenceItems);
        this.sequenceOrder = sequenceOrder;
        this.sequenceStatus = sequenceStatus;
    }

    public String getSequenceType() {
        return sequenceType;
    }

    public List<StepEntity> getSequenceItems() {
        return sequenceItems;
    }

    public String getSequenceOrder() {
        return sequenceOrder;
    }

    public String getSequenceStatus() {
        return sequenceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "sequenceType", sequenceType,
                "sequenceItems", sequenceItems, "sequenceOrder", sequenceOrder,
                "sequenceStatus", sequenceStatus);
    }
}
