package com.minicad.step.model.organization.org.document;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved STANDARD_INSTANCE.
 * A standard instance entity.
 *
 * @param id STEP instance id
 * @param name standard instance name
 * @param standardDefinition standard variance definition reference
 * @param standardCompliance standard variance compliance level
 * @param standardCertifications standard variance certifications
 * @param standardStatus standard variance status
 */
/**
 * Resolved STANDARD_INSTANCE.
 * A standard instance entity.
 *
 * @param id STEP instance id
 * @param name standard instance name
 * @param standardDefinition standard variance definition reference
 * @param standardCompliance standard variance compliance level
 * @param standardCertifications standard variance certifications
 * @param standardStatus standard variance status
 */
public final class StepStandardInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity standardDefinition;
    private final String standardCompliance;
    private final List<StepEntity> standardCertifications;
    private final String standardStatus;

    public StepStandardInstance(int id, String name, StepEntity standardDefinition, String standardCompliance, List<StepEntity> standardCertifications, String standardStatus) {
        this.id = id;
        this.name = name;
        this.standardDefinition = standardDefinition;
        this.standardCompliance = standardCompliance;
        this.standardCertifications = standardCertifications == null ? null : java.util.List.copyOf(standardCertifications);
        this.standardStatus = standardStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getStandardDefinition() {
        return standardDefinition;
    }

    public String getStandardCompliance() {
        return standardCompliance;
    }

    public List<StepEntity> getStandardCertifications() {
        return standardCertifications;
    }

    public String getStandardStatus() {
        return standardStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStandardInstance that = (StepStandardInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(standardDefinition, that.standardDefinition) && Objects.equals(standardCompliance, that.standardCompliance) && Objects.equals(standardCertifications, that.standardCertifications) && Objects.equals(standardStatus, that.standardStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, standardDefinition, standardCompliance, standardCertifications, standardStatus);
    }

    @Override
    public String toString() {
        return "StepStandardInstance{" + "id=" + id + "name=" + name + "standardDefinition=" + standardDefinition + "standardCompliance=" + standardCompliance + "standardCertifications=" + standardCertifications + "standardStatus=" + standardStatus + "}";
    }
}