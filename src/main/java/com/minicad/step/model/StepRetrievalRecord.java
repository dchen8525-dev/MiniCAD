package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepRetrievalRecord extends AbstractStepEntity {
    private final StepEntity varianceData;
    private final StepEntity varianceArchive;
    private final StepEntity varianceDate;
    private final StepEntity varianceRequester;
    private final String variancePurpose;
    private final String varianceStatus;

    public StepRetrievalRecord(int id, String name, StepEntity varianceData, StepEntity varianceArchive, StepEntity varianceDate, StepEntity varianceRequester, String variancePurpose, String varianceStatus) {
        super(id, name);
        this.varianceData = varianceData;
        this.varianceArchive = varianceArchive;
        this.varianceDate = varianceDate;
        this.varianceRequester = varianceRequester;
        this.variancePurpose = variancePurpose;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceData() {
        return varianceData;
    }

    public StepEntity getVarianceArchive() {
        return varianceArchive;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public StepEntity getVarianceRequester() {
        return varianceRequester;
    }

    public String getVariancePurpose() {
        return variancePurpose;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceData", varianceData);
        state.put("varianceArchive", varianceArchive);
        state.put("varianceDate", varianceDate);
        state.put("varianceRequester", varianceRequester);
        state.put("variancePurpose", variancePurpose);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
