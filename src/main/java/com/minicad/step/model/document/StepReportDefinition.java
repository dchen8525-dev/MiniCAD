package com.minicad.step.model.document;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepReportDefinition(
    int id,
    String name,
    String reportType,
    String reportTemplate,
    List<String> reportSections,
    List<String> reportParameters,
    String reportStatus) implements StepEntity {
}