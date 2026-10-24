package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepClampFeature extends AbstractStepEntity {
    private final String clampType;
    private final StepEntity clampGeometry;
    private final double clampForce;
    private final double clampOpening;
    private final StepEntity clampMaterial;
    private final int clampingSequence;

    public StepClampFeature(int id, String name, String clampType, StepEntity clampGeometry, double clampForce, double clampOpening, StepEntity clampMaterial, int clampingSequence) {
        super(id, name);
        this.clampType = clampType;
        this.clampGeometry = clampGeometry;
        this.clampForce = clampForce;
        this.clampOpening = clampOpening;
        this.clampMaterial = clampMaterial;
        this.clampingSequence = clampingSequence;
    }

    public String getClampType() {
        return clampType;
    }

    public StepEntity getClampGeometry() {
        return clampGeometry;
    }

    public double getClampForce() {
        return clampForce;
    }

    public double getClampOpening() {
        return clampOpening;
    }

    public StepEntity getClampMaterial() {
        return clampMaterial;
    }

    public int getClampingSequence() {
        return clampingSequence;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "clampType", clampType, "clampGeometry", clampGeometry,
                "clampForce", clampForce, "clampOpening", clampOpening, "clampMaterial", clampMaterial,
                "clampingSequence", clampingSequence);
    }
}
