package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CONFIGURATION_RECORD.
 * A configuration record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceItem configured variance item
 * @varianceSettings configuration variance settings
 * @varianceDate configuration variance date
 * @varianceReason configuration variance reason
 * @variancePrevious previous variance configuration
 * @varianceStatus record variance status
 */
public final class StepConfigurationRecord extends AbstractStepEntity {
    private final StepEntity varianceItem;
    private final List<String> varianceSettings;
    private final StepEntity varianceDate;
    private final String varianceReason;
    private final StepEntity variancePrevious;
    private final String varianceStatus;

    public StepConfigurationRecord(int id, String name, StepEntity varianceItem, List<String> varianceSettings, StepEntity varianceDate, String varianceReason, StepEntity variancePrevious, String varianceStatus) {
        super(id, name);
        this.varianceItem = varianceItem;
        this.varianceSettings = varianceSettings == null ? null : java.util.List.copyOf(varianceSettings);
        this.varianceDate = varianceDate;
        this.varianceReason = varianceReason;
        this.variancePrevious = variancePrevious;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceItem() {
        return varianceItem;
    }

    public List<String> getVarianceSettings() {
        return varianceSettings;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public String getVarianceReason() {
        return varianceReason;
    }

    public StepEntity getVariancePrevious() {
        return variancePrevious;
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
        state.put("varianceSettings", varianceSettings);
        state.put("varianceDate", varianceDate);
        state.put("varianceReason", varianceReason);
        state.put("variancePrevious", variancePrevious);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
