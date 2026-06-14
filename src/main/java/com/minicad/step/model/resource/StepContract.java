package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal CONTRACT metadata.
 *
 * @param id STEP instance id
 * @param name contract name
 * @param purpose contract purpose
 * @param kind contract type
 */
/**
 * Minimal CONTRACT metadata.
 *
 * @param id STEP instance id
 * @param name contract name
 * @param purpose contract purpose
 * @param kind contract type
 */
public final class StepContract implements StepEntity {
    private final int id;
    private final String name;
    private final String purpose;
    private final StepContractType kind;

    public StepContract(int id, String name, String purpose, StepContractType kind) {
        this.id = id;
        this.name = name;
        this.purpose = purpose;
        this.kind = kind;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPurpose() {
        return purpose;
    }

    public StepContractType getKind() {
        return kind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepContract that = (StepContract) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(purpose, that.purpose) && Objects.equals(kind, that.kind);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, purpose, kind);
    }

    @Override
    public String toString() {
        return "StepContract{" + "id=" + id + "name=" + name + "purpose=" + purpose + "kind=" + kind + "}";
    }
}
