package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved METRIC_ENTRY.
 * A metric entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryName entry variance metric name
 * @param entryValue entry variance metric value
 * @param entryTimestamp entry variance timestamp
 * @param entryTags entry variance tags
 * @param entryStatus entry variance status
 */
public final class StepMetricEntry extends AbstractStepEntity {
    private final String entryType;
    private final String entryName;
    private final double entryValue;
    private final StepEntity entryTimestamp;
    private final List<String> entryTags;
    private final String entryStatus;

    public StepMetricEntry(int id, String name, String entryType, String entryName, double entryValue, StepEntity entryTimestamp, List<String> entryTags, String entryStatus) {
        super(id, name);
        this.entryType = entryType;
        this.entryName = entryName;
        this.entryValue = entryValue;
        this.entryTimestamp = entryTimestamp;
        this.entryTags = entryTags == null ? null : java.util.List.copyOf(entryTags);
        this.entryStatus = entryStatus;
    }

    public String getEntryType() {
        return entryType;
    }

    public String getEntryName() {
        return entryName;
    }

    public double getEntryValue() {
        return entryValue;
    }

    public StepEntity getEntryTimestamp() {
        return entryTimestamp;
    }

    public List<String> getEntryTags() {
        return entryTags;
    }

    public String getEntryStatus() {
        return entryStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("entryType", entryType);
        state.put("entryName", entryName);
        state.put("entryValue", entryValue);
        state.put("entryTimestamp", entryTimestamp);
        state.put("entryTags", entryTags);
        state.put("entryStatus", entryStatus);
        return state;
    }
}
