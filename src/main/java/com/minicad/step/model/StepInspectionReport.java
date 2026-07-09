package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved INSPECTION_REPORT.
 * An inspection report entity.
 *
 * @param id STEP instance id
 * @param name report name
 * @param reportId report identifier
 * @param inspectionType inspection type (dimensional, visual, functional)
 * @varianceItems inspected variance items
 * @varianceResults inspection variance results
 * @varianceDefects found variance defects
 * @param inspectionDate inspection date
 * @param inspector inspector reference
 * @param reportStatus report status
 */
/**
 * Resolved INSPECTION_REPORT.
 * An inspection report entity.
 *
 * @param id STEP instance id
 * @param name report name
 * @param reportId report identifier
 * @param inspectionType inspection type (dimensional, visual, functional)
 * @varianceItems inspected variance items
 * @varianceResults inspection variance results
 * @varianceDefects found variance defects
 * @param inspectionDate inspection date
 * @param inspector inspector reference
 * @param reportStatus report status
 */
public final class StepInspectionReport implements StepEntity {
    private final int id;
    private final String name;
    private final String reportId;
    private final String inspectionType;
    private final List<StepEntity> varianceItems;
    private final List<StepEntity> varianceResults;
    private final List<StepEntity> varianceDefects;
    private final StepEntity inspectionDate;
    private final StepEntity inspector;
    private final String reportStatus;

    public StepInspectionReport(int id, String name, String reportId, String inspectionType, List<StepEntity> varianceItems, List<StepEntity> varianceResults, List<StepEntity> varianceDefects, StepEntity inspectionDate, StepEntity inspector, String reportStatus) {
        this.id = id;
        this.name = name;
        this.reportId = reportId;
        this.inspectionType = inspectionType;
        this.varianceItems = varianceItems == null ? null : java.util.List.copyOf(varianceItems);
        this.varianceResults = varianceResults == null ? null : java.util.List.copyOf(varianceResults);
        this.varianceDefects = varianceDefects == null ? null : java.util.List.copyOf(varianceDefects);
        this.inspectionDate = inspectionDate;
        this.inspector = inspector;
        this.reportStatus = reportStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getReportId() {
        return reportId;
    }

    public String getInspectionType() {
        return inspectionType;
    }

    public List<StepEntity> getVarianceItems() {
        return varianceItems;
    }

    public List<StepEntity> getVarianceResults() {
        return varianceResults;
    }

    public List<StepEntity> getVarianceDefects() {
        return varianceDefects;
    }

    public StepEntity getInspectionDate() {
        return inspectionDate;
    }

    public StepEntity getInspector() {
        return inspector;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepInspectionReport that = (StepInspectionReport) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(reportId, that.reportId) && Objects.equals(inspectionType, that.inspectionType) && Objects.equals(varianceItems, that.varianceItems) && Objects.equals(varianceResults, that.varianceResults) && Objects.equals(varianceDefects, that.varianceDefects) && Objects.equals(inspectionDate, that.inspectionDate) && Objects.equals(inspector, that.inspector) && Objects.equals(reportStatus, that.reportStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, reportId, inspectionType, varianceItems, varianceResults, varianceDefects, inspectionDate, inspector, reportStatus);
    }

    @Override
    public String toString() {
        return "StepInspectionReport{" + "id=" + id + "name=" + name + "reportId=" + reportId + "inspectionType=" + inspectionType + "varianceItems=" + varianceItems + "varianceResults=" + varianceResults + "varianceDefects=" + varianceDefects + "inspectionDate=" + inspectionDate + "inspector=" + inspector + "reportStatus=" + reportStatus + "}";
    }
}