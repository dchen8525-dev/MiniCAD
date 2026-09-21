package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepHeatAffectedZone extends AbstractStepEntity {
    private final StepEntity zoneGeometry;
    private final StepEntity affectedMaterial;
    private final double zoneWidth;
    private final double hardnessChange;
    private final String microstructureChange;

    public StepHeatAffectedZone(int id, String name, StepEntity zoneGeometry, StepEntity affectedMaterial, double zoneWidth, double hardnessChange, String microstructureChange) {
        super(id, name);
        this.zoneGeometry = zoneGeometry;
        this.affectedMaterial = affectedMaterial;
        this.zoneWidth = zoneWidth;
        this.hardnessChange = hardnessChange;
        this.microstructureChange = microstructureChange;
    }

    public StepEntity getZoneGeometry() {
        return zoneGeometry;
    }

    public StepEntity getAffectedMaterial() {
        return affectedMaterial;
    }

    public double getZoneWidth() {
        return zoneWidth;
    }

    public double getHardnessChange() {
        return hardnessChange;
    }

    public String getMicrostructureChange() {
        return microstructureChange;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("zoneGeometry", zoneGeometry);
        state.put("affectedMaterial", affectedMaterial);
        state.put("zoneWidth", zoneWidth);
        state.put("hardnessChange", hardnessChange);
        state.put("microstructureChange", microstructureChange);
        return state;
    }
}
