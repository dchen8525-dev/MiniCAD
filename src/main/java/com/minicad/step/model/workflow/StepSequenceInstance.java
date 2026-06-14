package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SEQUENCE_INSTANCE.
 * A sequence instance entity.
 *
 * @param id STEP instance id
 * @param name sequence instance name
 * @param sequenceDefinition sequence variance definition reference
 * @param sequenceState sequence variance state
 * @param sequencePosition sequence variance current position
 * @param sequenceCompleted sequence variance completed items
 * @param sequenceStatus sequence variance status
 */
/**
 * Resolved SEQUENCE_INSTANCE.
 * A sequence instance entity.
 *
 * @param id STEP instance id
 * @param name sequence instance name
 * @param sequenceDefinition sequence variance definition reference
 * @param sequenceState sequence variance state
 * @param sequencePosition sequence variance current position
 * @param sequenceCompleted sequence variance completed items
 * @param sequenceStatus sequence variance status
 */
public final class StepSequenceInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity sequenceDefinition;
    private final String sequenceState;
    private final int sequencePosition;
    private final int sequenceCompleted;
    private final String sequenceStatus;

    public StepSequenceInstance(int id, String name, StepEntity sequenceDefinition, String sequenceState, int sequencePosition, int sequenceCompleted, String sequenceStatus) {
        this.id = id;
        this.name = name;
        this.sequenceDefinition = sequenceDefinition;
        this.sequenceState = sequenceState;
        this.sequencePosition = sequencePosition;
        this.sequenceCompleted = sequenceCompleted;
        this.sequenceStatus = sequenceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getSequenceDefinition() {
        return sequenceDefinition;
    }

    public String getSequenceState() {
        return sequenceState;
    }

    public int getSequencePosition() {
        return sequencePosition;
    }

    public int getSequenceCompleted() {
        return sequenceCompleted;
    }

    public String getSequenceStatus() {
        return sequenceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSequenceInstance that = (StepSequenceInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(sequenceDefinition, that.sequenceDefinition) && Objects.equals(sequenceState, that.sequenceState) && sequencePosition == that.sequencePosition && sequenceCompleted == that.sequenceCompleted && Objects.equals(sequenceStatus, that.sequenceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sequenceDefinition, sequenceState, sequencePosition, sequenceCompleted, sequenceStatus);
    }

    @Override
    public String toString() {
        return "StepSequenceInstance{" + "id=" + id + "name=" + name + "sequenceDefinition=" + sequenceDefinition + "sequenceState=" + sequenceState + "sequencePosition=" + sequencePosition + "sequenceCompleted=" + sequenceCompleted + "sequenceStatus=" + sequenceStatus + "}";
    }
}