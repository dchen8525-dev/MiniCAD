package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SAFETY_FEATURE.
 * A safety feature entity.
 *
 * @param id STEP instance id
 * @param name safety name
 * @param safetyType safety type (guard, interlock, emergency stop, warning)
 * @param safetyGeometry safety geometry representation
 * @param safetyZone safety zone specification
 * @param safetyClass safety classification level
 * @param safetyStandard safety standard reference
 */
/**
 * Resolved SAFETY_FEATURE.
 * A safety feature entity.
 *
 * @param id STEP instance id
 * @param name safety name
 * @param safetyType safety type (guard, interlock, emergency stop, warning)
 * @param safetyGeometry safety geometry representation
 * @param safetyZone safety zone specification
 * @param safetyClass safety classification level
 * @param safetyStandard safety standard reference
 */
public final class StepSafetyFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String safetyType;
    private final StepEntity safetyGeometry;
    private final StepEntity safetyZone;
    private final String safetyClass;
    private final String safetyStandard;

    public StepSafetyFeature(int id, String name, String safetyType, StepEntity safetyGeometry, StepEntity safetyZone, String safetyClass, String safetyStandard) {
        this.id = id;
        this.name = name;
        this.safetyType = safetyType;
        this.safetyGeometry = safetyGeometry;
        this.safetyZone = safetyZone;
        this.safetyClass = safetyClass;
        this.safetyStandard = safetyStandard;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSafetyType() {
        return safetyType;
    }

    public StepEntity getSafetyGeometry() {
        return safetyGeometry;
    }

    public StepEntity getSafetyZone() {
        return safetyZone;
    }

    public String getSafetyClass() {
        return safetyClass;
    }

    public String getSafetyStandard() {
        return safetyStandard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSafetyFeature that = (StepSafetyFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(safetyType, that.safetyType) && Objects.equals(safetyGeometry, that.safetyGeometry) && Objects.equals(safetyZone, that.safetyZone) && Objects.equals(safetyClass, that.safetyClass) && Objects.equals(safetyStandard, that.safetyStandard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, safetyType, safetyGeometry, safetyZone, safetyClass, safetyStandard);
    }

    @Override
    public String toString() {
        return "StepSafetyFeature{" + "id=" + id + "name=" + name + "safetyType=" + safetyType + "safetyGeometry=" + safetyGeometry + "safetyZone=" + safetyZone + "safetyClass=" + safetyClass + "safetyStandard=" + safetyStandard + "}";
    }
}