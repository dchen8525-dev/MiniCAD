package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CASE_INSTANCE.
 * A case instance entity.
 *
 * @param id STEP instance id
 * @param name case instance name
 * @param caseDefinition case variance definition reference
 * @param caseState case variance state
 * @param caseActualOutputs case variance actual outputs
 * @param caseResult case variance result (pass/fail)
 * @param caseStatus case variance status
 */
/**
 * Resolved CASE_INSTANCE.
 * A case instance entity.
 *
 * @param id STEP instance id
 * @param name case instance name
 * @param caseDefinition case variance definition reference
 * @param caseState case variance state
 * @param caseActualOutputs case variance actual outputs
 * @param caseResult case variance result (pass/fail)
 * @param caseStatus case variance status
 */
public final class StepCaseInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity caseDefinition;
    private final String caseState;
    private final List<String> caseActualOutputs;
    private final boolean caseResult;
    private final String caseStatus;

    public StepCaseInstance(int id, String name, StepEntity caseDefinition, String caseState, List<String> caseActualOutputs, boolean caseResult, String caseStatus) {
        this.id = id;
        this.name = name;
        this.caseDefinition = caseDefinition;
        this.caseState = caseState;
        this.caseActualOutputs = caseActualOutputs == null ? null : java.util.List.copyOf(caseActualOutputs);
        this.caseResult = caseResult;
        this.caseStatus = caseStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getCaseDefinition() {
        return caseDefinition;
    }

    public String getCaseState() {
        return caseState;
    }

    public List<String> getCaseActualOutputs() {
        return caseActualOutputs;
    }

    public boolean isCaseResult() {
        return caseResult;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCaseInstance that = (StepCaseInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(caseDefinition, that.caseDefinition) && Objects.equals(caseState, that.caseState) && Objects.equals(caseActualOutputs, that.caseActualOutputs) && caseResult == that.caseResult && Objects.equals(caseStatus, that.caseStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, caseDefinition, caseState, caseActualOutputs, caseResult, caseStatus);
    }

    @Override
    public String toString() {
        return "StepCaseInstance{" + "id=" + id + "name=" + name + "caseDefinition=" + caseDefinition + "caseState=" + caseState + "caseActualOutputs=" + caseActualOutputs + "caseResult=" + caseResult + "caseStatus=" + caseStatus + "}";
    }
}