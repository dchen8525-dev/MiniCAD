package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved REGULATION_INSTANCE.
 * A regulation instance entity.
 *
 * @param id STEP instance id
 * @param name regulation instance name
 * @param regulationDefinition regulation variance definition reference
 * @param regulationCompliance regulation variance compliance status
 * @param regulationViolations regulation variance violations
 * @param regulationStatus regulation variance status
 */
/**
 * Resolved REGULATION_INSTANCE.
 * A regulation instance entity.
 *
 * @param id STEP instance id
 * @param name regulation instance name
 * @param regulationDefinition regulation variance definition reference
 * @param regulationCompliance regulation variance compliance status
 * @param regulationViolations regulation variance violations
 * @param regulationStatus regulation variance status
 */
public final class StepRegulationInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity regulationDefinition;
    private final String regulationCompliance;
    private final int regulationViolations;
    private final String regulationStatus;

    public StepRegulationInstance(int id, String name, StepEntity regulationDefinition, String regulationCompliance, int regulationViolations, String regulationStatus) {
        this.id = id;
        this.name = name;
        this.regulationDefinition = regulationDefinition;
        this.regulationCompliance = regulationCompliance;
        this.regulationViolations = regulationViolations;
        this.regulationStatus = regulationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getRegulationDefinition() {
        return regulationDefinition;
    }

    public String getRegulationCompliance() {
        return regulationCompliance;
    }

    public int getRegulationViolations() {
        return regulationViolations;
    }

    public String getRegulationStatus() {
        return regulationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRegulationInstance that = (StepRegulationInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(regulationDefinition, that.regulationDefinition) && Objects.equals(regulationCompliance, that.regulationCompliance) && regulationViolations == that.regulationViolations && Objects.equals(regulationStatus, that.regulationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, regulationDefinition, regulationCompliance, regulationViolations, regulationStatus);
    }

    @Override
    public String toString() {
        return "StepRegulationInstance{" + "id=" + id + "name=" + name + "regulationDefinition=" + regulationDefinition + "regulationCompliance=" + regulationCompliance + "regulationViolations=" + regulationViolations + "regulationStatus=" + regulationStatus + "}";
    }
}