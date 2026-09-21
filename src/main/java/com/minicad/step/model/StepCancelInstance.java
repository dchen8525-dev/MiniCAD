package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CANCEL_INSTANCE.
 * A cancel instance entity.
 *
 * @param id STEP instance id
 * @param name cancel instance name
 * @param cancelDefinition cancel variance definition reference
 * @param cancelState cancel variance state
 * @param cancelTime cancel variance cancellation time
 * @param cancelReason cancel variance reason
 * @param cancelStatus cancel variance status
 */
public final class StepCancelInstance extends AbstractStepEntity {
    private final StepEntity cancelDefinition;
    private final String cancelState;
    private final StepEntity cancelTime;
    private final String cancelReason;
    private final String cancelStatus;

    public StepCancelInstance(int id, String name, StepEntity cancelDefinition, String cancelState, StepEntity cancelTime, String cancelReason, String cancelStatus) {
        super(id, name);
        this.cancelDefinition = cancelDefinition;
        this.cancelState = cancelState;
        this.cancelTime = cancelTime;
        this.cancelReason = cancelReason;
        this.cancelStatus = cancelStatus;
    }

    public StepEntity getCancelDefinition() {
        return cancelDefinition;
    }

    public String getCancelState() {
        return cancelState;
    }

    public StepEntity getCancelTime() {
        return cancelTime;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public String getCancelStatus() {
        return cancelStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("cancelDefinition", cancelDefinition);
        state.put("cancelState", cancelState);
        state.put("cancelTime", cancelTime);
        state.put("cancelReason", cancelReason);
        state.put("cancelStatus", cancelStatus);
        return state;
    }
}
