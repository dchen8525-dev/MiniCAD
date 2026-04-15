package com.minicad.step.model;

import java.util.List;

/**
 * Resolved COMPOSITE_DATUM_REFERENCE.
 * A composite datum reference entity.
 *
 * @param id STEP instance id
 * @param name datum name
 * @param datums list of datum entities
 * @param compositeType composite type (common, simultaneous, etc.)
 */
public record StepCompositeDatumReference(
    int id,
    String name,
    List<StepEntity> datums,
    String compositeType) implements StepEntity {
}