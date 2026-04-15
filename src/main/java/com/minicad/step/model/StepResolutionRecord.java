package com.minicad.step.model;

import java.util.List;

/**
 * Resolved RESOLUTION_RECORD.
 * A resolution record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceIssue resolved variance issue
 * @varianceSolution solution variance applied
 * @varianceDate resolution variance date
 * @varianceResolver resolver variance reference
 * @varianceVerification verification variance method
 * @variancePrevention prevention variance measures
 * @varianceStatus record variance status
 */
public record StepResolutionRecord(
    int id,
    String name,
    StepEntity varianceIssue,
    String varianceSolution,
    StepEntity varianceDate,
    StepEntity varianceResolver,
    String varianceVerification,
    List<String> variancePrevention,
    String varianceStatus) implements StepEntity {
}