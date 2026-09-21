package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved LOCK_INSTANCE.
 * A lock instance entity.
 *
 * @param id STEP instance id
 * @param name lock instance name
 * @param lockDefinition lock variance definition reference
 * @param lockState lock variance state
 * @param lockHolder lock variance holder reference
 * @param lockAcquiredTime lock variance acquired time
 * @param lockStatus lock variance status
 */
public final class StepLockInstance extends AbstractStepEntity {
    private final StepEntity lockDefinition;
    private final String lockState;
    private final StepEntity lockHolder;
    private final StepEntity lockAcquiredTime;
    private final String lockStatus;

    public StepLockInstance(int id, String name, StepEntity lockDefinition, String lockState, StepEntity lockHolder, StepEntity lockAcquiredTime, String lockStatus) {
        super(id, name);
        this.lockDefinition = lockDefinition;
        this.lockState = lockState;
        this.lockHolder = lockHolder;
        this.lockAcquiredTime = lockAcquiredTime;
        this.lockStatus = lockStatus;
    }

    public StepEntity getLockDefinition() {
        return lockDefinition;
    }

    public String getLockState() {
        return lockState;
    }

    public StepEntity getLockHolder() {
        return lockHolder;
    }

    public StepEntity getLockAcquiredTime() {
        return lockAcquiredTime;
    }

    public String getLockStatus() {
        return lockStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("lockDefinition", lockDefinition);
        state.put("lockState", lockState);
        state.put("lockHolder", lockHolder);
        state.put("lockAcquiredTime", lockAcquiredTime);
        state.put("lockStatus", lockStatus);
        return state;
    }
}
