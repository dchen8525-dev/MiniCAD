package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved PROJECTED_ZONE_DEFINITION.
 * A projected tolerance zone definition entity.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param description zone description
 * @param projectedZone the projected zone entity reference
 * @param applied whether the projected zone is applied
 */
public final class StepProjectedZoneDefinition extends AbstractStepEntity {
    private final String description;
    private final StepEntity projectedZone;
    private final boolean applied;

    public StepProjectedZoneDefinition(int id, String name, String description, StepEntity projectedZone, boolean applied) {
        super(id, name);
        this.description = description;
        this.projectedZone = projectedZone;
        this.applied = applied;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getProjectedZone() {
        return projectedZone;
    }

    public boolean isApplied() {
        return applied;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("projectedZone", projectedZone);
        state.put("applied", applied);
        return state;
    }
}
