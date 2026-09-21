package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CANCEL_DEFINITION.
 * A cancel definition entity.
 *
 * @param id STEP instance id
 * @param name cancel name
 * @param cancelType cancel variance type
 * @param cancelCondition cancel variance condition
 * @param cancelGracePeriod cancel variance grace period
 * @param cancelCleanup cancel variance cleanup action
 * @param cancelStatus cancel variance status
 */
public final class StepCancelDefinition extends AbstractStepEntity {
    private final String cancelType;
    private final String cancelCondition;
    private final int cancelGracePeriod;
    private final StepEntity cancelCleanup;
    private final String cancelStatus;

    public StepCancelDefinition(int id, String name, String cancelType, String cancelCondition, int cancelGracePeriod, StepEntity cancelCleanup, String cancelStatus) {
        super(id, name);
        this.cancelType = cancelType;
        this.cancelCondition = cancelCondition;
        this.cancelGracePeriod = cancelGracePeriod;
        this.cancelCleanup = cancelCleanup;
        this.cancelStatus = cancelStatus;
    }

    public String getCancelType() {
        return cancelType;
    }

    public String getCancelCondition() {
        return cancelCondition;
    }

    public int getCancelGracePeriod() {
        return cancelGracePeriod;
    }

    public StepEntity getCancelCleanup() {
        return cancelCleanup;
    }

    public String getCancelStatus() {
        return cancelStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("cancelType", cancelType);
        state.put("cancelCondition", cancelCondition);
        state.put("cancelGracePeriod", cancelGracePeriod);
        state.put("cancelCleanup", cancelCleanup);
        state.put("cancelStatus", cancelStatus);
        return state;
    }
}
