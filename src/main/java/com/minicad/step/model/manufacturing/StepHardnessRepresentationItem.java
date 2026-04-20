package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved HARDNESS_REPRESENTATION_ITEM.
 * A hardness representation item entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param hardnessValue hardness variance value
 * @param hardnessUnit hardness variance unit reference
 * @param hardnessMethod hardness variance measurement method
 * @param hardnessStatus hardness variance status
 */
public record StepHardnessRepresentationItem(
    int id,
    String name,
    double hardnessValue,
    StepEntity hardnessUnit,
    String hardnessMethod,
    String hardnessStatus) implements StepEntity {
}