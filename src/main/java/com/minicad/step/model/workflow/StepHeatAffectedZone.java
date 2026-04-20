package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved HEAT_AFFECTED_ZONE.
 * A heat affected zone entity.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param zoneGeometry zone geometry representation
 * @param affectedMaterial affected material properties
 * @param zoneWidth zone width specification
 * @param hardnessChange hardness change in HAZ
 * @param microstructureChange microstructure change description
 */
public record StepHeatAffectedZone(
    int id,
    String name,
    StepEntity zoneGeometry,
    StepEntity affectedMaterial,
    double zoneWidth,
    double hardnessChange,
    String microstructureChange) implements StepEntity {
}