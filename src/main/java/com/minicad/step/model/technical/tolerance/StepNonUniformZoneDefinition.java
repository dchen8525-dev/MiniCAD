package com.minicad.step.model.technical.tolerance;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved NON_UNIFORM_ZONE_DEFINITION.
 * A tolerance zone definition that varies non-uniformly across the feature.
 */
/**
 * Resolved NON_UNIFORM_ZONE_DEFINITION.
 * A tolerance zone definition that varies non-uniformly across the feature.
 */
public final class StepNonUniformZoneDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String zoneType;
    private final StepEntity definingCurve;
    private final Double variationMagnitude;

    public StepNonUniformZoneDefinition(int id, String name, String zoneType, StepEntity definingCurve, Double variationMagnitude) {
        this.id = id;
        this.name = name;
        this.zoneType = zoneType;
        this.definingCurve = definingCurve;
        this.variationMagnitude = variationMagnitude;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getZoneType() {
        return zoneType;
    }

    public StepEntity getDefiningCurve() {
        return definingCurve;
    }

    public Double getVariationMagnitude() {
        return variationMagnitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepNonUniformZoneDefinition that = (StepNonUniformZoneDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(zoneType, that.zoneType) && Objects.equals(definingCurve, that.definingCurve) && Objects.equals(variationMagnitude, that.variationMagnitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, zoneType, definingCurve, variationMagnitude);
    }

    @Override
    public String toString() {
        return "StepNonUniformZoneDefinition{" + "id=" + id + "name=" + name + "zoneType=" + zoneType + "definingCurve=" + definingCurve + "variationMagnitude=" + variationMagnitude + "}";
    }
}
