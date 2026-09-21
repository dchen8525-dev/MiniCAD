package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepDisposalRecord extends AbstractStepEntity {
    private final StepEntity varianceItem;
    private final String varianceMethod;
    private final StepEntity varianceDate;
    private final StepEntity varianceAuthorization;
    private final String varianceEnvironmental;
    private final String varianceStatus;

    public StepDisposalRecord(int id, String name, StepEntity varianceItem, String varianceMethod, StepEntity varianceDate, StepEntity varianceAuthorization, String varianceEnvironmental, String varianceStatus) {
        super(id, name);
        this.varianceItem = varianceItem;
        this.varianceMethod = varianceMethod;
        this.varianceDate = varianceDate;
        this.varianceAuthorization = varianceAuthorization;
        this.varianceEnvironmental = varianceEnvironmental;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceItem() {
        return varianceItem;
    }

    public String getVarianceMethod() {
        return varianceMethod;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public StepEntity getVarianceAuthorization() {
        return varianceAuthorization;
    }

    public String getVarianceEnvironmental() {
        return varianceEnvironmental;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceItem", varianceItem);
        state.put("varianceMethod", varianceMethod);
        state.put("varianceDate", varianceDate);
        state.put("varianceAuthorization", varianceAuthorization);
        state.put("varianceEnvironmental", varianceEnvironmental);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
