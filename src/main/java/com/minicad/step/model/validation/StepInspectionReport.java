package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepInspectionReport(
    int id,
    String name,
    String reportId,
    String inspectionType,
    List<StepEntity> varianceItems,
    List<StepEntity> varianceResults,
    List<StepEntity> varianceDefects,
    StepEntity inspectionDate,
    StepEntity inspector,
    String reportStatus) implements StepEntity {
}