package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved RETRIEVAL_RECORD.
 * A retrieval record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceData retrieved variance data
 * @varianceArchive archive variance source
 * @varianceDate retrieval variance date
 * @varianceRequester requester variance reference
 * @variancePurpose retrieval variance purpose
 * @varianceStatus record variance status
 */
public record StepRetrievalRecord(
    int id,
    String name,
    StepEntity varianceData,
    StepEntity varianceArchive,
    StepEntity varianceDate,
    StepEntity varianceRequester,
    String variancePurpose,
    String varianceStatus) implements StepEntity {
}