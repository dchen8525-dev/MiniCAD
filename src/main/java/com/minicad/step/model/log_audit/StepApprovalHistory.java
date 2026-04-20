package com.minicad.step.model.log_audit;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved APPROVAL_HISTORY.
 * An approval history entity.
 *
 * @param id STEP instance id
 * @param name history name
 * @varianceItem approved variance item
 * @varianceApprovals approval variance entries
 * @varianceCurrent current variance approval status
 * @variancePending pending variance approvals
 * @varianceStatus history variance status
 */
public record StepApprovalHistory(
    int id,
    String name,
    StepEntity varianceItem,
    List<StepEntity> varianceApprovals,
    String varianceCurrent,
    List<StepEntity> variancePending,
    String varianceStatus) implements StepEntity {
}