package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepLockDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String lockType;
    private final String lockScope;
    private final int lockTimeout;
    private final String lockPolicy;
    private final String lockStatus;

    public StepLockDefinition(int id, String name, String lockType, String lockScope, int lockTimeout, String lockPolicy, String lockStatus) {
        this.id = id;
        this.name = name;
        this.lockType = lockType;
        this.lockScope = lockScope;
        this.lockTimeout = lockTimeout;
        this.lockPolicy = lockPolicy;
        this.lockStatus = lockStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLockDefinition that = (StepLockDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(lockType, that.lockType) && Objects.equals(lockScope, that.lockScope) && lockTimeout == that.lockTimeout && Objects.equals(lockPolicy, that.lockPolicy) && Objects.equals(lockStatus, that.lockStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lockType, lockScope, lockTimeout, lockPolicy, lockStatus);
    }

    @Override
    public String toString() {
        return "StepLockDefinition{" + "id=" + id + "name=" + name + "lockType=" + lockType + "lockScope=" + lockScope + "lockTimeout=" + lockTimeout + "lockPolicy=" + lockPolicy + "lockStatus=" + lockStatus + "}";
    }
}