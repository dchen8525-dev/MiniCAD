package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * DATA_EQUIVALENCE_REPORT_REQUEST entity model.
 * Request for data equivalence report.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param reportRequestType type of report request (for A3M_EQUIVALENCE_SUMMARY_REPORT_REQUEST_WITH_REPRESENTATIVE_VALUE)
 * @param entityName actual entity type name (for subtype handling)
 */
public final class StepDataEquivalenceReportRequest implements StepEntity {
    private final int id;
    private final String name;
    private final Object reportRequestType; // representative_value_type reference
    private final String entityName;

    public StepDataEquivalenceReportRequest(
        int id,
        String name,
        Object reportRequestType,
        String entityName) {
        this.id = id;
        this.name = name;
        this.reportRequestType = reportRequestType;
        this.entityName = entityName;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public Object getReportRequestType() {
        return reportRequestType;
    }

    public String getEntityName() {
        return entityName;
    }

    public String entityName() {
        return entityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDataEquivalenceReportRequest that = (StepDataEquivalenceReportRequest) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "StepDataEquivalenceReportRequest{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", reportRequestType=" + reportRequestType +
            ", entityName='" + entityName + '\'' +
            '}';
    }
}