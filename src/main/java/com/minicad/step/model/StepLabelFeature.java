package com.minicad.step.model;

import java.util.List;

/**
 * Resolved LABEL_FEATURE.
 * A label feature entity.
 *
 * @param id STEP instance id
 * @param name label name
 * @param labelType label type (barcode, QR, RFID, text)
 * @param labelGeometry label geometry representation
 * @param labelPosition label position placement
 * @param labelContent label content text/data
 * @param labelStandard label standard reference
 */
public record StepLabelFeature(
    int id,
    String name,
    String labelType,
    StepEntity labelGeometry,
    StepEntity labelPosition,
    String labelContent,
    String labelStandard) implements StepEntity {
}