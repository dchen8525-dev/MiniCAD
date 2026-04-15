package com.minicad.step.model;

import java.util.List;

/**
 * Resolved DRAWING_REFERENCE.
 * A drawing reference entity.
 *
 * @param id STEP instance id
 * @param name reference name
 * @param drawingId drawing identifier/number
 * @param drawingType drawing type (assembly, detail, schematic)
 * @param drawingRevision drawing revision
 * @param drawingScale drawing scale factor
 * @param drawingStatus drawing status
 * @param drawingAuthor drawing author reference
 */
public record StepDrawingReference(
    int id,
    String name,
    String drawingId,
    String drawingType,
    String drawingRevision,
    double drawingScale,
    String drawingStatus,
    StepEntity drawingAuthor) implements StepEntity {
}