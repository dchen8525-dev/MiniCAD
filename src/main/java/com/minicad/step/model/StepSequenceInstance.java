package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepSequenceInstance extends AbstractStepEntity {
    private final StepEntity sequenceDefinition;
    private final String sequenceState;
    private final int sequencePosition;
    private final int sequenceCompleted;
    private final String sequenceStatus;

    public StepSequenceInstance(int id, String name, StepEntity sequenceDefinition, String sequenceState, int sequencePosition, int sequenceCompleted, String sequenceStatus) {
        super(id, name);
        this.sequenceDefinition = sequenceDefinition;
        this.sequenceState = sequenceState;
        this.sequencePosition = sequencePosition;
        this.sequenceCompleted = sequenceCompleted;
        this.sequenceStatus = sequenceStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "sequenceDefinition", sequenceDefinition,
                "sequenceState", sequenceState, "sequencePosition", sequencePosition,
                "sequenceCompleted", sequenceCompleted, "sequenceStatus", sequenceStatus);
    }
}
