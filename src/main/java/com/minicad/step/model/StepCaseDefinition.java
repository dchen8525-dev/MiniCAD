package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CASE_DEFINITION.
 * A case definition entity.
 *
 * @param id STEP instance id
 * @param name case name
 * @param caseType case variance type
 * @param caseScenario case variance scenario
 * @param caseInputs case variance inputs
 * @param caseExpectedOutputs case variance expected outputs
 * @param caseStatus case variance status
 */
/**
 * Resolved CASE_DEFINITION.
 * A case definition entity.
 *
 * @param id STEP instance id
 * @param name case name
 * @param caseType case variance type
 * @param caseScenario case variance scenario
 * @param caseInputs case variance inputs
 * @param caseExpectedOutputs case variance expected outputs
 * @param caseStatus case variance status
 */
public final class StepCaseDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String caseType;
    private final StepEntity caseScenario;
    private final List<String> caseInputs;
    private final List<String> caseExpectedOutputs;
    private final String caseStatus;

    public StepCaseDefinition(int id, String name, String caseType, StepEntity caseScenario, List<String> caseInputs, List<String> caseExpectedOutputs, String caseStatus) {
        this.id = id;
        this.name = name;
        this.caseType = caseType;
        this.caseScenario = caseScenario;
        this.caseInputs = caseInputs == null ? null : java.util.List.copyOf(caseInputs);
        this.caseExpectedOutputs = caseExpectedOutputs == null ? null : java.util.List.copyOf(caseExpectedOutputs);
        this.caseStatus = caseStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCaseType() {
        return caseType;
    }

    public StepEntity getCaseScenario() {
        return caseScenario;
    }

    public List<String> getCaseInputs() {
        return caseInputs;
    }

    public List<String> getCaseExpectedOutputs() {
        return caseExpectedOutputs;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCaseDefinition that = (StepCaseDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(caseType, that.caseType) && Objects.equals(caseScenario, that.caseScenario) && Objects.equals(caseInputs, that.caseInputs) && Objects.equals(caseExpectedOutputs, that.caseExpectedOutputs) && Objects.equals(caseStatus, that.caseStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, caseType, caseScenario, caseInputs, caseExpectedOutputs, caseStatus);
    }

    @Override
    public String toString() {
        return "StepCaseDefinition{" + "id=" + id + "name=" + name + "caseType=" + caseType + "caseScenario=" + caseScenario + "caseInputs=" + caseInputs + "caseExpectedOutputs=" + caseExpectedOutputs + "caseStatus=" + caseStatus + "}";
    }
}