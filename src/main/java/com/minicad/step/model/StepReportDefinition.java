package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepReportDefinition extends AbstractStepEntity {
    private final String reportType;
    private final String reportTemplate;
    private final List<String> reportSections;
    private final List<String> reportParameters;
    private final String reportStatus;

    public StepReportDefinition(int id, String name, String reportType, String reportTemplate, List<String> reportSections, List<String> reportParameters, String reportStatus) {
        super(id, name);
        this.reportType = reportType;
        this.reportTemplate = reportTemplate;
        this.reportSections = reportSections == null ? null : java.util.List.copyOf(reportSections);
        this.reportParameters = reportParameters == null ? null : java.util.List.copyOf(reportParameters);
        this.reportStatus = reportStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("reportType", reportType);
        state.put("reportTemplate", reportTemplate);
        state.put("reportSections", reportSections);
        state.put("reportParameters", reportParameters);
        state.put("reportStatus", reportStatus);
        return state;
    }
}
