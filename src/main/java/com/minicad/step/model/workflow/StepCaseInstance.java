package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepCaseInstance(
    int id,
    String name,
    StepEntity caseDefinition,
    String caseState,
    List<String> caseActualOutputs,
    boolean caseResult,
    String caseStatus) implements StepEntity {
}