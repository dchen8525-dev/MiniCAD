package com.minicad.step.model.document;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepReportInstance(
    int id,
    String name,
    StepEntity reportDefinition,
    StepEntity reportGeneratedTime,
    String reportContent,
    List<String> reportAttachments,
    String reportStatus) implements StepEntity {
}