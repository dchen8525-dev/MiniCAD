package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CLAMP_FEATURE.
 * A clamp feature entity.
 *
 * @param id STEP instance id
 * @param name clamp name
 * @param clampType clamp type (manual, hydraulic, pneumatic)
 * @param clampGeometry clamp geometry representation
 * @param clampForce clamp force specification
 * @param clampOpening clamp opening distance
 * @param clampMaterial clamp material reference
 * @param clampingSequence clamping sequence order
 */
public record StepClampFeature(
    int id,
    String name,
    String clampType,
    StepEntity clampGeometry,
    double clampForce,
    double clampOpening,
    StepEntity clampMaterial,
    int clampingSequence) implements StepEntity {
}