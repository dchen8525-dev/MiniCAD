package com.minicad.step.model.approval;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal CERTIFICATION metadata.
 *
 * @param id STEP instance id
 * @param name certification name
 * @param purpose certification purpose
 * @param kind certification type
 */
/**
 * Minimal CERTIFICATION metadata.
 *
 * @param id STEP instance id
 * @param name certification name
 * @param purpose certification purpose
 * @param kind certification type
 */
public final class StepCertification implements StepEntity {
    private final int id;
    private final String name;
    private final String purpose;
    private final StepCertificationType kind;

    public StepCertification(int id, String name, String purpose, StepCertificationType kind) {
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

    public StepCertificationType getKind() {
        return kind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCertification that = (StepCertification) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(purpose, that.purpose) && Objects.equals(kind, that.kind);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, purpose, kind);
    }

    @Override
    public String toString() {
        return "StepCertification{" + "id=" + id + "name=" + name + "purpose=" + purpose + "kind=" + kind + "}";
    }
}
