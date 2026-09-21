package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MEASUREMENT_ENTRY.
 * A measurement entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryMeasurement entry variance measurement type
 * @param entryValue entry variance measurement value
 * @param entryUnit entry variance unit reference
 * @param entryTimestamp entry variance timestamp
 * @param entryAccuracy entry variance accuracy
 * @param entryStatus entry variance status
 */
public final class StepMeasurementEntry extends AbstractStepEntity {
    private final String entryType;
    private final String entryMeasurement;
    private final double entryValue;
    private final StepEntity entryUnit;
    private final StepEntity entryTimestamp;
    private final double entryAccuracy;
    private final String entryStatus;

    public StepMeasurementEntry(int id, String name, String entryType, String entryMeasurement, double entryValue, StepEntity entryUnit, StepEntity entryTimestamp, double entryAccuracy, String entryStatus) {
        super(id, name);
        this.entryType = entryType;
        this.entryMeasurement = entryMeasurement;
        this.entryValue = entryValue;
        this.entryUnit = entryUnit;
        this.entryTimestamp = entryTimestamp;
        this.entryAccuracy = entryAccuracy;
        this.entryStatus = entryStatus;
    }

    public String getEntryType() {
        return entryType;
    }

    public String getEntryMeasurement() {
        return entryMeasurement;
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

    public double getEntryAccuracy() {
        return entryAccuracy;
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
        state.put("entryMeasurement", entryMeasurement);
        state.put("entryValue", entryValue);
        state.put("entryUnit", entryUnit);
        state.put("entryTimestamp", entryTimestamp);
        state.put("entryAccuracy", entryAccuracy);
        state.put("entryStatus", entryStatus);
        return state;
    }
}
