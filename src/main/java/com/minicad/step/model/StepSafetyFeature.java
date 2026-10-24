package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepSafetyFeature extends AbstractStepEntity {
    private final String safetyType;
    private final StepEntity safetyGeometry;
    private final StepEntity safetyZone;
    private final String safetyClass;
    private final String safetyStandard;

    public StepSafetyFeature(int id, String name, String safetyType, StepEntity safetyGeometry, StepEntity safetyZone, String safetyClass, String safetyStandard) {
        super(id, name);
        this.safetyType = safetyType;
        this.safetyGeometry = safetyGeometry;
        this.safetyZone = safetyZone;
        this.safetyClass = safetyClass;
        this.safetyStandard = safetyStandard;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "safetyType", safetyType, "safetyGeometry", safetyGeometry,
                "safetyZone", safetyZone, "safetyClass", safetyClass, "safetyStandard", safetyStandard);
    }
}
