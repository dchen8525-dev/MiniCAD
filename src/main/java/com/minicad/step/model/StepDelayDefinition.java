package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved DELAY_DEFINITION.
 * A delay definition entity.
 *
 * @param id STEP instance id
 * @param name delay name
 * @param delayType delay variance type
 * @param delayDuration delay variance duration
 * @param delayCondition delay variance condition
 * @param delayAction delay variance action after delay
 * @param delayStatus delay variance status
 */
public final class StepDelayDefinition extends AbstractStepEntity {
    private final String delayType;
    private final int delayDuration;
    private final String delayCondition;
    private final StepEntity delayAction;
    private final String delayStatus;

    public StepDelayDefinition(int id, String name, String delayType, int delayDuration, String delayCondition, StepEntity delayAction, String delayStatus) {
        super(id, name);
        this.delayType = delayType;
        this.delayDuration = delayDuration;
        this.delayCondition = delayCondition;
        this.delayAction = delayAction;
        this.delayStatus = delayStatus;
    }

    public String getDelayType() {
        return delayType;
    }

    public int getDelayDuration() {
        return delayDuration;
    }

    public String getDelayCondition() {
        return delayCondition;
    }

    public StepEntity getDelayAction() {
        return delayAction;
    }

    public String getDelayStatus() {
        return delayStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "delayType", delayType, "delayDuration", delayDuration,
                "delayCondition", delayCondition, "delayAction", delayAction, "delayStatus", delayStatus);
    }
}
