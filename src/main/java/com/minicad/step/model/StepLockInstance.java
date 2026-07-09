package com.minicad.step.model.management.security;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepLockInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity lockDefinition;
    private final String lockState;
    private final StepEntity lockHolder;
    private final StepEntity lockAcquiredTime;
    private final String lockStatus;

    public StepLockInstance(int id, String name, StepEntity lockDefinition, String lockState, StepEntity lockHolder, StepEntity lockAcquiredTime, String lockStatus) {
        this.id = id;
        this.name = name;
        this.lockDefinition = lockDefinition;
        this.lockState = lockState;
        this.lockHolder = lockHolder;
        this.lockAcquiredTime = lockAcquiredTime;
        this.lockStatus = lockStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLockInstance that = (StepLockInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(lockDefinition, that.lockDefinition) && Objects.equals(lockState, that.lockState) && Objects.equals(lockHolder, that.lockHolder) && Objects.equals(lockAcquiredTime, that.lockAcquiredTime) && Objects.equals(lockStatus, that.lockStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lockDefinition, lockState, lockHolder, lockAcquiredTime, lockStatus);
    }

    @Override
    public String toString() {
        return "StepLockInstance{" + "id=" + id + "name=" + name + "lockDefinition=" + lockDefinition + "lockState=" + lockState + "lockHolder=" + lockHolder + "lockAcquiredTime=" + lockAcquiredTime + "lockStatus=" + lockStatus + "}";
    }
}