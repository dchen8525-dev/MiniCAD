package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ENCRYPTION_SPECIFICATION.
 * An encryption specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceAlgorithm encryption variance algorithm
 * @varianceKeySize key variance size
 * @varianceMode encryption variance mode
 * @varianceKeyManagement key variance management specification
 * @varianceStatus specification variance status
 */
/**
 * Resolved ENCRYPTION_SPECIFICATION.
 * An encryption specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceAlgorithm encryption variance algorithm
 * @varianceKeySize key variance size
 * @varianceMode encryption variance mode
 * @varianceKeyManagement key variance management specification
 * @varianceStatus specification variance status
 */
public final class StepEncryptionSpecification implements StepEntity {
    private final int id;
    private final String name;
    private final String varianceAlgorithm;
    private final int varianceKeySize;
    private final String varianceMode;
    private final StepEntity varianceKeyManagement;
    private final String varianceStatus;

    public StepEncryptionSpecification(int id, String name, String varianceAlgorithm, int varianceKeySize, String varianceMode, StepEntity varianceKeyManagement, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceAlgorithm = varianceAlgorithm;
        this.varianceKeySize = varianceKeySize;
        this.varianceMode = varianceMode;
        this.varianceKeyManagement = varianceKeyManagement;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVarianceAlgorithm() {
        return varianceAlgorithm;
    }

    public int getVarianceKeySize() {
        return varianceKeySize;
    }

    public String getVarianceMode() {
        return varianceMode;
    }

    public StepEntity getVarianceKeyManagement() {
        return varianceKeyManagement;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepEncryptionSpecification that = (StepEncryptionSpecification) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceAlgorithm, that.varianceAlgorithm) && varianceKeySize == that.varianceKeySize && Objects.equals(varianceMode, that.varianceMode) && Objects.equals(varianceKeyManagement, that.varianceKeyManagement) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceAlgorithm, varianceKeySize, varianceMode, varianceKeyManagement, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepEncryptionSpecification{" + "id=" + id + "name=" + name + "varianceAlgorithm=" + varianceAlgorithm + "varianceKeySize=" + varianceKeySize + "varianceMode=" + varianceMode + "varianceKeyManagement=" + varianceKeyManagement + "varianceStatus=" + varianceStatus + "}";
    }
}