package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved RESTORE_DEFINITION.
 * A restore definition entity.
 *
 * @param id STEP instance id
 * @param name restore name
 * @param restoreType restore variance type
 * @param restoreSource restore variance source backup reference
 * @param restoreTarget restore variance target reference
 * @param restoreOptions restore variance options
 * @param restoreStatus restore variance status
 */
public final class StepRestoreDefinition extends AbstractStepEntity {
    private final String restoreType;
    private final StepEntity restoreSource;
    private final StepEntity restoreTarget;
    private final List<String> restoreOptions;
    private final String restoreStatus;

    public StepRestoreDefinition(int id, String name, String restoreType, StepEntity restoreSource, StepEntity restoreTarget, List<String> restoreOptions, String restoreStatus) {
        super(id, name);
        this.restoreType = restoreType;
        this.restoreSource = restoreSource;
        this.restoreTarget = restoreTarget;
        this.restoreOptions = restoreOptions == null ? null : java.util.List.copyOf(restoreOptions);
        this.restoreStatus = restoreStatus;
    }

    public String getRestoreType() {
        return restoreType;
    }

    public StepEntity getRestoreSource() {
        return restoreSource;
    }

    public StepEntity getRestoreTarget() {
        return restoreTarget;
    }

    public List<String> getRestoreOptions() {
        return restoreOptions;
    }

    public String getRestoreStatus() {
        return restoreStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("restoreType", restoreType);
        state.put("restoreSource", restoreSource);
        state.put("restoreTarget", restoreTarget);
        state.put("restoreOptions", restoreOptions);
        state.put("restoreStatus", restoreStatus);
        return state;
    }
}
