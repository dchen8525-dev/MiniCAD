package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepSequenceDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String sequenceType;
    private final List<StepEntity> sequenceItems;
    private final String sequenceOrder;
    private final String sequenceStatus;

    public StepSequenceDefinition(int id, String name, String sequenceType, List<StepEntity> sequenceItems, String sequenceOrder, String sequenceStatus) {
        this.id = id;
        this.name = name;
        this.sequenceType = sequenceType;
        this.sequenceItems = sequenceItems == null ? null : java.util.List.copyOf(sequenceItems);
        this.sequenceOrder = sequenceOrder;
        this.sequenceStatus = sequenceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSequenceDefinition that = (StepSequenceDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(sequenceType, that.sequenceType) && Objects.equals(sequenceItems, that.sequenceItems) && Objects.equals(sequenceOrder, that.sequenceOrder) && Objects.equals(sequenceStatus, that.sequenceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sequenceType, sequenceItems, sequenceOrder, sequenceStatus);
    }

    @Override
    public String toString() {
        return "StepSequenceDefinition{" + "id=" + id + "name=" + name + "sequenceType=" + sequenceType + "sequenceItems=" + sequenceItems + "sequenceOrder=" + sequenceOrder + "sequenceStatus=" + sequenceStatus + "}";
    }
}