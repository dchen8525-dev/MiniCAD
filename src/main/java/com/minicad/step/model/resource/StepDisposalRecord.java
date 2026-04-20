package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved DISPOSAL_RECORD.
 * A disposal record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceItem disposed variance item
 * @varianceMethod disposal variance method
 * @varianceDate disposal variance date
 * @varianceAuthorization authorization variance reference
 * @varianceEnvironmental environmental variance compliance
 * @varianceStatus record variance status
 */
public record StepDisposalRecord(
    int id,
    String name,
    StepEntity varianceItem,
    String varianceMethod,
    StepEntity varianceDate,
    StepEntity varianceAuthorization,
    String varianceEnvironmental,
    String varianceStatus) implements StepEntity {
}