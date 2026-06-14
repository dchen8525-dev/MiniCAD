package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved REGULATION_DEFINITION.
 * A regulation definition entity.
 *
 * @param id STEP instance id
 * @param name regulation name
 * @param regulationType regulation variance type
 * @param regulationAuthority regulation variance authority
 * @param regulationRequirements regulation variance requirements
 * @param regulationPenalties regulation variance penalties
 * @param regulationStatus regulation variance status
 */
/**
 * Resolved REGULATION_DEFINITION.
 * A regulation definition entity.
 *
 * @param id STEP instance id
 * @param name regulation name
 * @param regulationType regulation variance type
 * @param regulationAuthority regulation variance authority
 * @param regulationRequirements regulation variance requirements
 * @param regulationPenalties regulation variance penalties
 * @param regulationStatus regulation variance status
 */
public final class StepRegulationDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String regulationType;
    private final String regulationAuthority;
    private final List<String> regulationRequirements;
    private final String regulationPenalties;
    private final String regulationStatus;

    public StepRegulationDefinition(int id, String name, String regulationType, String regulationAuthority, List<String> regulationRequirements, String regulationPenalties, String regulationStatus) {
        this.id = id;
        this.name = name;
        this.regulationType = regulationType;
        this.regulationAuthority = regulationAuthority;
        this.regulationRequirements = regulationRequirements == null ? null : java.util.List.copyOf(regulationRequirements);
        this.regulationPenalties = regulationPenalties;
        this.regulationStatus = regulationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegulationType() {
        return regulationType;
    }

    public String getRegulationAuthority() {
        return regulationAuthority;
    }

    public List<String> getRegulationRequirements() {
        return regulationRequirements;
    }

    public String getRegulationPenalties() {
        return regulationPenalties;
    }

    public String getRegulationStatus() {
        return regulationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRegulationDefinition that = (StepRegulationDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(regulationType, that.regulationType) && Objects.equals(regulationAuthority, that.regulationAuthority) && Objects.equals(regulationRequirements, that.regulationRequirements) && Objects.equals(regulationPenalties, that.regulationPenalties) && Objects.equals(regulationStatus, that.regulationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, regulationType, regulationAuthority, regulationRequirements, regulationPenalties, regulationStatus);
    }

    @Override
    public String toString() {
        return "StepRegulationDefinition{" + "id=" + id + "name=" + name + "regulationType=" + regulationType + "regulationAuthority=" + regulationAuthority + "regulationRequirements=" + regulationRequirements + "regulationPenalties=" + regulationPenalties + "regulationStatus=" + regulationStatus + "}";
    }
}