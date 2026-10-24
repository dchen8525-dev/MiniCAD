package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved LOCK_RECORD.
 * A lock record entity.
 *
 * @param id STEP instance id
 * @param name lock name
 * @param lockType lock variance type
 * @param lockTarget lock variance target reference
 * @param lockHolder lock variance holder reference
 * @param lockAcquiredTime lock variance acquired time
 * @param lockExpiresTime lock variance expires time
 * @param lockStatus lock variance status
 */
public final class StepLockRecord extends AbstractStepEntity {
    private final String lockType;
    private final StepEntity lockTarget;
    private final StepEntity lockHolder;
    private final StepEntity lockAcquiredTime;
    private final StepEntity lockExpiresTime;
    private final String lockStatus;

    public StepLockRecord(int id, String name, String lockType, StepEntity lockTarget, StepEntity lockHolder, StepEntity lockAcquiredTime, StepEntity lockExpiresTime, String lockStatus) {
        super(id, name);
        this.lockType = lockType;
        this.lockTarget = lockTarget;
        this.lockHolder = lockHolder;
        this.lockAcquiredTime = lockAcquiredTime;
        this.lockExpiresTime = lockExpiresTime;
        this.lockStatus = lockStatus;
    }

    public String getLockType() {
        return lockType;
    }

    public StepEntity getLockTarget() {
        return lockTarget;
    }

    public StepEntity getLockHolder() {
        return lockHolder;
    }

    public StepEntity getLockAcquiredTime() {
        return lockAcquiredTime;
    }

    public StepEntity getLockExpiresTime() {
        return lockExpiresTime;
    }

    public String getLockStatus() {
        return lockStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "lockType", lockType, "lockTarget", lockTarget,
                "lockHolder", lockHolder, "lockAcquiredTime", lockAcquiredTime,
                "lockExpiresTime", lockExpiresTime, "lockStatus", lockStatus);
    }
}
