package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved MACHINING_OPERATION.
 * Represents a machining operation in manufacturing.
 *
 * @param id STEP instance id
 * @param name operation name
 * @param status operation status
 * @param features features being machined
 */
/**
 * Resolved MACHINING_OPERATION.
 * Represents a machining operation in manufacturing.
 *
 * @param id STEP instance id
 * @param name operation name
 * @param status operation status
 * @param features features being machined
 */
public final class StepMachiningOperation implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity status;
    private final List<StepEntity> features;

    public StepMachiningOperation(int id, String name, StepEntity status, List<StepEntity> features) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.features = features == null ? null : java.util.List.copyOf(features);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getStatus() {
        return status;
    }

    public List<StepEntity> getFeatures() {
        return features;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMachiningOperation that = (StepMachiningOperation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(status, that.status) && Objects.equals(features, that.features);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, features);
    }

    @Override
    public String toString() {
        return "StepMachiningOperation{" + "id=" + id + "name=" + name + "status=" + status + "features=" + features + "}";
    }
}