package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepClampFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String clampType;
    private final StepEntity clampGeometry;
    private final double clampForce;
    private final double clampOpening;
    private final StepEntity clampMaterial;
    private final int clampingSequence;

    public StepClampFeature(int id, String name, String clampType, StepEntity clampGeometry, double clampForce, double clampOpening, StepEntity clampMaterial, int clampingSequence) {
        this.id = id;
        this.name = name;
        this.clampType = clampType;
        this.clampGeometry = clampGeometry;
        this.clampForce = clampForce;
        this.clampOpening = clampOpening;
        this.clampMaterial = clampMaterial;
        this.clampingSequence = clampingSequence;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepClampFeature that = (StepClampFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(clampType, that.clampType) && Objects.equals(clampGeometry, that.clampGeometry) && clampForce == that.clampForce && clampOpening == that.clampOpening && Objects.equals(clampMaterial, that.clampMaterial) && clampingSequence == that.clampingSequence;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, clampType, clampGeometry, clampForce, clampOpening, clampMaterial, clampingSequence);
    }

    @Override
    public String toString() {
        return "StepClampFeature{" + "id=" + id + "name=" + name + "clampType=" + clampType + "clampGeometry=" + clampGeometry + "clampForce=" + clampForce + "clampOpening=" + clampOpening + "clampMaterial=" + clampMaterial + "clampingSequence=" + clampingSequence + "}";
    }
}