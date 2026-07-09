package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SURFACE_TEXTURE_REPRESENTATION_ITEM.
 * A surface texture representation item entity.
 *
 * @param id STEP instance id
 * @param name item name
 * @param roughnessValue roughness value
 * @param roughnessUnit roughness unit
 * @param measurementMethod measurement method
 */
/**
 * Resolved SURFACE_TEXTURE_REPRESENTATION_ITEM.
 * A surface texture representation item entity.
 *
 * @param id STEP instance id
 * @param name item name
 * @param roughnessValue roughness value
 * @param roughnessUnit roughness unit
 * @param measurementMethod measurement method
 */
public final class StepSurfaceTextureRepresentationItem implements StepEntity {
    private final int id;
    private final String name;
    private final Double roughnessValue;
    private final StepEntity roughnessUnit;
    private final String measurementMethod;

    public StepSurfaceTextureRepresentationItem(int id, String name, Double roughnessValue, StepEntity roughnessUnit, String measurementMethod) {
        this.id = id;
        this.name = name;
        this.roughnessValue = roughnessValue;
        this.roughnessUnit = roughnessUnit;
        this.measurementMethod = measurementMethod;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getRoughnessValue() {
        return roughnessValue;
    }

    public StepEntity getRoughnessUnit() {
        return roughnessUnit;
    }

    public String getMeasurementMethod() {
        return measurementMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSurfaceTextureRepresentationItem that = (StepSurfaceTextureRepresentationItem) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(roughnessValue, that.roughnessValue) && Objects.equals(roughnessUnit, that.roughnessUnit) && Objects.equals(measurementMethod, that.measurementMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, roughnessValue, roughnessUnit, measurementMethod);
    }

    @Override
    public String toString() {
        return "StepSurfaceTextureRepresentationItem{" + "id=" + id + "name=" + name + "roughnessValue=" + roughnessValue + "roughnessUnit=" + roughnessUnit + "measurementMethod=" + measurementMethod + "}";
    }
}