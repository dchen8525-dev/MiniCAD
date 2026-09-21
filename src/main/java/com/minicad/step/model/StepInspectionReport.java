package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepInspectionReport extends AbstractStepEntity {
    private final String reportId;
    private final String inspectionType;
    private final List<StepEntity> varianceItems;
    private final List<StepEntity> varianceResults;
    private final List<StepEntity> varianceDefects;
    private final StepEntity inspectionDate;
    private final StepEntity inspector;
    private final String reportStatus;

    public StepInspectionReport(int id, String name, String reportId, String inspectionType, List<StepEntity> varianceItems, List<StepEntity> varianceResults, List<StepEntity> varianceDefects, StepEntity inspectionDate, StepEntity inspector, String reportStatus) {
        super(id, name);
        this.reportId = reportId;
        this.inspectionType = inspectionType;
        this.varianceItems = varianceItems == null ? null : java.util.List.copyOf(varianceItems);
        this.varianceResults = varianceResults == null ? null : java.util.List.copyOf(varianceResults);
        this.varianceDefects = varianceDefects == null ? null : java.util.List.copyOf(varianceDefects);
        this.inspectionDate = inspectionDate;
        this.inspector = inspector;
        this.reportStatus = reportStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("reportId", reportId);
        state.put("inspectionType", inspectionType);
        state.put("varianceItems", varianceItems);
        state.put("varianceResults", varianceResults);
        state.put("varianceDefects", varianceDefects);
        state.put("inspectionDate", inspectionDate);
        state.put("inspector", inspector);
        state.put("reportStatus", reportStatus);
        return state;
    }
}
