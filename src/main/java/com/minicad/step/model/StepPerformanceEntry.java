package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PERFORMANCE_ENTRY.
 * A performance entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryMetric entry variance metric name
 * @param entryValue entry variance metric value
 * @param entryUnit entry variance unit
 * @param entryTimestamp entry variance timestamp
 * @param entryContext entry variance context reference
 * @param entryStatus entry variance status
 */
public final class StepPerformanceEntry extends AbstractStepEntity {
    private final String entryType;
    private final String entryMetric;
    private final double entryValue;
    private final StepEntity entryUnit;
    private final StepEntity entryTimestamp;
    private final StepEntity entryContext;
    private final String entryStatus;

    public StepPerformanceEntry(int id, String name, String entryType, String entryMetric, double entryValue, StepEntity entryUnit, StepEntity entryTimestamp, StepEntity entryContext, String entryStatus) {
        super(id, name);
        this.entryType = entryType;
        this.entryMetric = entryMetric;
        this.entryValue = entryValue;
        this.entryUnit = entryUnit;
        this.entryTimestamp = entryTimestamp;
        this.entryContext = entryContext;
        this.entryStatus = entryStatus;
    }

    public String getEntryType() {
        return entryType;
    }

    public String getEntryMetric() {
        return entryMetric;
    }

    public double getEntryValue() {
        return entryValue;
    }

    public StepEntity getEntryUnit() {
        return entryUnit;
    }

    public StepEntity getEntryTimestamp() {
        return entryTimestamp;
    }

    public StepEntity getEntryContext() {
        return entryContext;
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
        state.put("entryMetric", entryMetric);
        state.put("entryValue", entryValue);
        state.put("entryUnit", entryUnit);
        state.put("entryTimestamp", entryTimestamp);
        state.put("entryContext", entryContext);
        state.put("entryStatus", entryStatus);
        return state;
    }
}
