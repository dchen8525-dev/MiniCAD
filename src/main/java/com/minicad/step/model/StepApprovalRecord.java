package com.minicad.step.model;

import java.util.List;

/**
 * Resolved APPROVAL_RECORD.
 * An approval record entity.
 *
 * @param id STEP instance id
 * @param name approval name
 * @param approvalId approval identifier
 * @varianceItem approved variance item
 * @varianceApprover approving variance person
 * @varianceRole approver variance role
 * @varianceDate approval variance date
 * @varianceDecision approval variance decision
 * @varianceComments approval variance comments
 * @varianceStatus approval variance status
 */
public record StepApprovalRecord(
    int id,
    String name,
    String approvalId,
    StepEntity varianceItem,
    StepEntity varianceApprover,
    StepEntity varianceRole,
    StepEntity varianceDate,
    String varianceDecision,
    String varianceComments,
    String varianceStatus) implements StepEntity {
}