package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepMeasurementEntry implements StepEntity {
    private final int id;
    private final String name;
    private final String entryType;
    private final String entryMeasurement;
    private final double entryValue;
    private final StepEntity entryUnit;
    private final StepEntity entryTimestamp;
    private final double entryAccuracy;
    private final String entryStatus;

    public StepMeasurementEntry(int id, String name, String entryType, String entryMeasurement, double entryValue, StepEntity entryUnit, StepEntity entryTimestamp, double entryAccuracy, String entryStatus) {
        this.id = id;
        this.name = name;
        this.entryType = entryType;
        this.entryMeasurement = entryMeasurement;
        this.entryValue = entryValue;
        this.entryUnit = entryUnit;
        this.entryTimestamp = entryTimestamp;
        this.entryAccuracy = entryAccuracy;
        this.entryStatus = entryStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMeasurementEntry that = (StepMeasurementEntry) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(entryType, that.entryType) && Objects.equals(entryMeasurement, that.entryMeasurement) && entryValue == that.entryValue && Objects.equals(entryUnit, that.entryUnit) && Objects.equals(entryTimestamp, that.entryTimestamp) && entryAccuracy == that.entryAccuracy && Objects.equals(entryStatus, that.entryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, entryType, entryMeasurement, entryValue, entryUnit, entryTimestamp, entryAccuracy, entryStatus);
    }

    @Override
    public String toString() {
        return "StepMeasurementEntry{" + "id=" + id + "name=" + name + "entryType=" + entryType + "entryMeasurement=" + entryMeasurement + "entryValue=" + entryValue + "entryUnit=" + entryUnit + "entryTimestamp=" + entryTimestamp + "entryAccuracy=" + entryAccuracy + "entryStatus=" + entryStatus + "}";
    }
}