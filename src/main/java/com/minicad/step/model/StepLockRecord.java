package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepLockRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String lockType;
    private final StepEntity lockTarget;
    private final StepEntity lockHolder;
    private final StepEntity lockAcquiredTime;
    private final StepEntity lockExpiresTime;
    private final String lockStatus;

    public StepLockRecord(int id, String name, String lockType, StepEntity lockTarget, StepEntity lockHolder, StepEntity lockAcquiredTime, StepEntity lockExpiresTime, String lockStatus) {
        this.id = id;
        this.name = name;
        this.lockType = lockType;
        this.lockTarget = lockTarget;
        this.lockHolder = lockHolder;
        this.lockAcquiredTime = lockAcquiredTime;
        this.lockExpiresTime = lockExpiresTime;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLockRecord that = (StepLockRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(lockType, that.lockType) && Objects.equals(lockTarget, that.lockTarget) && Objects.equals(lockHolder, that.lockHolder) && Objects.equals(lockAcquiredTime, that.lockAcquiredTime) && Objects.equals(lockExpiresTime, that.lockExpiresTime) && Objects.equals(lockStatus, that.lockStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lockType, lockTarget, lockHolder, lockAcquiredTime, lockExpiresTime, lockStatus);
    }

    @Override
    public String toString() {
        return "StepLockRecord{" + "id=" + id + "name=" + name + "lockType=" + lockType + "lockTarget=" + lockTarget + "lockHolder=" + lockHolder + "lockAcquiredTime=" + lockAcquiredTime + "lockExpiresTime=" + lockExpiresTime + "lockStatus=" + lockStatus + "}";
    }
}