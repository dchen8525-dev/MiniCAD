package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepReportInstance extends AbstractStepEntity {
    private final StepEntity reportDefinition;
    private final StepEntity reportGeneratedTime;
    private final String reportContent;
    private final List<String> reportAttachments;
    private final String reportStatus;

    public StepReportInstance(int id, String name, StepEntity reportDefinition, StepEntity reportGeneratedTime, String reportContent, List<String> reportAttachments, String reportStatus) {
        super(id, name);
        this.reportDefinition = reportDefinition;
        this.reportGeneratedTime = reportGeneratedTime;
        this.reportContent = reportContent;
        this.reportAttachments = reportAttachments == null ? null : java.util.List.copyOf(reportAttachments);
        this.reportStatus = reportStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("reportDefinition", reportDefinition);
        state.put("reportGeneratedTime", reportGeneratedTime);
        state.put("reportContent", reportContent);
        state.put("reportAttachments", reportAttachments);
        state.put("reportStatus", reportStatus);
        return state;
    }
}
