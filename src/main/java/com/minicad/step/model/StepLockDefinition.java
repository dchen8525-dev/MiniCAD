package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved LOCK_DEFINITION.
 * A lock definition entity.
 *
 * @param id STEP instance id
 * @param name lock name
 * @param lockType lock variance type
 * @param lockScope lock variance scope
 * @param lockTimeout lock variance timeout in seconds
 * @param lockPolicy lock variance policy
 * @param lockStatus lock variance status
 */
public final class StepLockDefinition extends AbstractStepEntity {
    private final String lockType;
    private final String lockScope;
    private final int lockTimeout;
    private final String lockPolicy;
    private final String lockStatus;

    public StepLockDefinition(int id, String name, String lockType, String lockScope, int lockTimeout, String lockPolicy, String lockStatus) {
        super(id, name);
        this.lockType = lockType;
        this.lockScope = lockScope;
        this.lockTimeout = lockTimeout;
        this.lockPolicy = lockPolicy;
        this.lockStatus = lockStatus;
    }

    public String getLockType() {
        return lockType;
    }

    public String getLockScope() {
        return lockScope;
    }

    public int getLockTimeout() {
        return lockTimeout;
    }

    public String getLockPolicy() {
        return lockPolicy;
    }

    public String getLockStatus() {
        return lockStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("lockType", lockType);
        state.put("lockScope", lockScope);
        state.put("lockTimeout", lockTimeout);
        state.put("lockPolicy", lockPolicy);
        state.put("lockStatus", lockStatus);
        return state;
    }
}
