package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SURFACE_QUALITY.
 * A surface quality entity.
 *
 * @param id STEP instance id
 * @param name quality name
 * @param surface surface reference
 * @param roughnessValues surface roughness values (Ra, Rz, etc.)
 * @param qualityGrade quality grade classification
 * @param measurementMethod measurement method
 * @param direction measurement direction
 */
/**
 * Resolved SURFACE_QUALITY.
 * A surface quality entity.
 *
 * @param id STEP instance id
 * @param name quality name
 * @param surface surface reference
 * @param roughnessValues surface roughness values (Ra, Rz, etc.)
 * @param qualityGrade quality grade classification
 * @param measurementMethod measurement method
 * @param direction measurement direction
 */
public final class StepSurfaceQuality implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity surface;
    private final List<Double> roughnessValues;
    private final String qualityGrade;
    private final String measurementMethod;
    private final StepEntity direction;

    public StepSurfaceQuality(int id, String name, StepEntity surface, List<Double> roughnessValues, String qualityGrade, String measurementMethod, StepEntity direction) {
        this.id = id;
        this.name = name;
        this.surface = surface;
        this.roughnessValues = roughnessValues == null ? null : java.util.List.copyOf(roughnessValues);
        this.qualityGrade = qualityGrade;
        this.measurementMethod = measurementMethod;
        this.direction = direction;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getSurface() {
        return surface;
    }

    public List<Double> getRoughnessValues() {
        return roughnessValues;
    }

    public String getQualityGrade() {
        return qualityGrade;
    }

    public String getMeasurementMethod() {
        return measurementMethod;
    }

    public StepEntity getDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSurfaceQuality that = (StepSurfaceQuality) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(surface, that.surface) && Objects.equals(roughnessValues, that.roughnessValues) && Objects.equals(qualityGrade, that.qualityGrade) && Objects.equals(measurementMethod, that.measurementMethod) && Objects.equals(direction, that.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surface, roughnessValues, qualityGrade, measurementMethod, direction);
    }

    @Override
    public String toString() {
        return "StepSurfaceQuality{" + "id=" + id + "name=" + name + "surface=" + surface + "roughnessValues=" + roughnessValues + "qualityGrade=" + qualityGrade + "measurementMethod=" + measurementMethod + "direction=" + direction + "}";
    }
}