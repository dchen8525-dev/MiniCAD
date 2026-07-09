package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved RECOVERY_DEFINITION.
 * A recovery definition entity.
 *
 * @param id STEP instance id
 * @param name recovery name
 * @param recoveryType recovery variance type
 * @param recoveryStrategy recovery variance strategy
 * @param recoverySteps recovery variance recovery steps
 * @param recoveryTimeout recovery variance timeout
 * @param recoveryStatus recovery variance status
 */
/**
 * Resolved RECOVERY_DEFINITION.
 * A recovery definition entity.
 *
 * @param id STEP instance id
 * @param name recovery name
 * @param recoveryType recovery variance type
 * @param recoveryStrategy recovery variance strategy
 * @param recoverySteps recovery variance recovery steps
 * @param recoveryTimeout recovery variance timeout
 * @param recoveryStatus recovery variance status
 */
public final class StepRecoveryDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String recoveryType;
    private final String recoveryStrategy;
    private final List<String> recoverySteps;
    private final int recoveryTimeout;
    private final String recoveryStatus;

    public StepRecoveryDefinition(int id, String name, String recoveryType, String recoveryStrategy, List<String> recoverySteps, int recoveryTimeout, String recoveryStatus) {
        this.id = id;
        this.name = name;
        this.recoveryType = recoveryType;
        this.recoveryStrategy = recoveryStrategy;
        this.recoverySteps = recoverySteps == null ? null : java.util.List.copyOf(recoverySteps);
        this.recoveryTimeout = recoveryTimeout;
        this.recoveryStatus = recoveryStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRecoveryType() {
        return recoveryType;
    }

    public String getRecoveryStrategy() {
        return recoveryStrategy;
    }

    public List<String> getRecoverySteps() {
        return recoverySteps;
    }

    public int getRecoveryTimeout() {
        return recoveryTimeout;
    }

    public String getRecoveryStatus() {
        return recoveryStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRecoveryDefinition that = (StepRecoveryDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(recoveryType, that.recoveryType) && Objects.equals(recoveryStrategy, that.recoveryStrategy) && Objects.equals(recoverySteps, that.recoverySteps) && recoveryTimeout == that.recoveryTimeout && Objects.equals(recoveryStatus, that.recoveryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, recoveryType, recoveryStrategy, recoverySteps, recoveryTimeout, recoveryStatus);
    }

    @Override
    public String toString() {
        return "StepRecoveryDefinition{" + "id=" + id + "name=" + name + "recoveryType=" + recoveryType + "recoveryStrategy=" + recoveryStrategy + "recoverySteps=" + recoverySteps + "recoveryTimeout=" + recoveryTimeout + "recoveryStatus=" + recoveryStatus + "}";
    }
}