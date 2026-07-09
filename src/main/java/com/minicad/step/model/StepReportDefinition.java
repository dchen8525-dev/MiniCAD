package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved REPORT_DEFINITION.
 * A report definition entity.
 *
 * @param id STEP instance id
 * @param name report name
 * @param reportType report variance type
 * @param reportTemplate report variance template reference
 * @param reportSections report variance sections
 * @param reportParameters report variance parameters
 * @param reportStatus report variance status
 */
/**
 * Resolved REPORT_DEFINITION.
 * A report definition entity.
 *
 * @param id STEP instance id
 * @param name report name
 * @param reportType report variance type
 * @param reportTemplate report variance template reference
 * @param reportSections report variance sections
 * @param reportParameters report variance parameters
 * @param reportStatus report variance status
 */
public final class StepReportDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String reportType;
    private final String reportTemplate;
    private final List<String> reportSections;
    private final List<String> reportParameters;
    private final String reportStatus;

    public StepReportDefinition(int id, String name, String reportType, String reportTemplate, List<String> reportSections, List<String> reportParameters, String reportStatus) {
        this.id = id;
        this.name = name;
        this.reportType = reportType;
        this.reportTemplate = reportTemplate;
        this.reportSections = reportSections == null ? null : java.util.List.copyOf(reportSections);
        this.reportParameters = reportParameters == null ? null : java.util.List.copyOf(reportParameters);
        this.reportStatus = reportStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getReportType() {
        return reportType;
    }

    public String getReportTemplate() {
        return reportTemplate;
    }

    public List<String> getReportSections() {
        return reportSections;
    }

    public List<String> getReportParameters() {
        return reportParameters;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepReportDefinition that = (StepReportDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(reportType, that.reportType) && Objects.equals(reportTemplate, that.reportTemplate) && Objects.equals(reportSections, that.reportSections) && Objects.equals(reportParameters, that.reportParameters) && Objects.equals(reportStatus, that.reportStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, reportType, reportTemplate, reportSections, reportParameters, reportStatus);
    }

    @Override
    public String toString() {
        return "StepReportDefinition{" + "id=" + id + "name=" + name + "reportType=" + reportType + "reportTemplate=" + reportTemplate + "reportSections=" + reportSections + "reportParameters=" + reportParameters + "reportStatus=" + reportStatus + "}";
    }
}