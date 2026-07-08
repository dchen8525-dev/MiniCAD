package com.minicad.step.model.organization.org.document;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved REPORT_INSTANCE.
 * A report instance entity.
 *
 * @param id STEP instance id
 * @param name report instance name
 * @param reportDefinition report variance definition reference
 * @param reportGeneratedTime report variance generated time
 * @param reportContent report variance content
 * @param reportAttachments report variance attachments
 * @param reportStatus report variance status
 */
/**
 * Resolved REPORT_INSTANCE.
 * A report instance entity.
 *
 * @param id STEP instance id
 * @param name report instance name
 * @param reportDefinition report variance definition reference
 * @param reportGeneratedTime report variance generated time
 * @param reportContent report variance content
 * @param reportAttachments report variance attachments
 * @param reportStatus report variance status
 */
public final class StepReportInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity reportDefinition;
    private final StepEntity reportGeneratedTime;
    private final String reportContent;
    private final List<String> reportAttachments;
    private final String reportStatus;

    public StepReportInstance(int id, String name, StepEntity reportDefinition, StepEntity reportGeneratedTime, String reportContent, List<String> reportAttachments, String reportStatus) {
        this.id = id;
        this.name = name;
        this.reportDefinition = reportDefinition;
        this.reportGeneratedTime = reportGeneratedTime;
        this.reportContent = reportContent;
        this.reportAttachments = reportAttachments == null ? null : java.util.List.copyOf(reportAttachments);
        this.reportStatus = reportStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getReportDefinition() {
        return reportDefinition;
    }

    public StepEntity getReportGeneratedTime() {
        return reportGeneratedTime;
    }

    public String getReportContent() {
        return reportContent;
    }

    public List<String> getReportAttachments() {
        return reportAttachments;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepReportInstance that = (StepReportInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(reportDefinition, that.reportDefinition) && Objects.equals(reportGeneratedTime, that.reportGeneratedTime) && Objects.equals(reportContent, that.reportContent) && Objects.equals(reportAttachments, that.reportAttachments) && Objects.equals(reportStatus, that.reportStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, reportDefinition, reportGeneratedTime, reportContent, reportAttachments, reportStatus);
    }

    @Override
    public String toString() {
        return "StepReportInstance{" + "id=" + id + "name=" + name + "reportDefinition=" + reportDefinition + "reportGeneratedTime=" + reportGeneratedTime + "reportContent=" + reportContent + "reportAttachments=" + reportAttachments + "reportStatus=" + reportStatus + "}";
    }
}