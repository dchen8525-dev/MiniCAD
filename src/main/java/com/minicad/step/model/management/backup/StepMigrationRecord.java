package com.minicad.step.model.management.backup;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved MIGRATION_RECORD.
 * A migration record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceData migrated variance data
 * @varianceFrom migration variance source
 * @varianceTo migration variance destination
 * @varianceDate migration variance date
 * @varianceFormat migration variance format conversion
 * @varianceStatus record variance status
 */
/**
 * Resolved MIGRATION_RECORD.
 * A migration record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceData migrated variance data
 * @varianceFrom migration variance source
 * @varianceTo migration variance destination
 * @varianceDate migration variance date
 * @varianceFormat migration variance format conversion
 * @varianceStatus record variance status
 */
public final class StepMigrationRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceData;
    private final String varianceFrom;
    private final String varianceTo;
    private final StepEntity varianceDate;
    private final String varianceFormat;
    private final String varianceStatus;

    public StepMigrationRecord(int id, String name, StepEntity varianceData, String varianceFrom, String varianceTo, StepEntity varianceDate, String varianceFormat, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceData = varianceData;
        this.varianceFrom = varianceFrom;
        this.varianceTo = varianceTo;
        this.varianceDate = varianceDate;
        this.varianceFormat = varianceFormat;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceData() {
        return varianceData;
    }

    public String getVarianceFrom() {
        return varianceFrom;
    }

    public String getVarianceTo() {
        return varianceTo;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public String getVarianceFormat() {
        return varianceFormat;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMigrationRecord that = (StepMigrationRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceData, that.varianceData) && Objects.equals(varianceFrom, that.varianceFrom) && Objects.equals(varianceTo, that.varianceTo) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceFormat, that.varianceFormat) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceData, varianceFrom, varianceTo, varianceDate, varianceFormat, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepMigrationRecord{" + "id=" + id + "name=" + name + "varianceData=" + varianceData + "varianceFrom=" + varianceFrom + "varianceTo=" + varianceTo + "varianceDate=" + varianceDate + "varianceFormat=" + varianceFormat + "varianceStatus=" + varianceStatus + "}";
    }
}