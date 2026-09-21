package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved MACHINED_SURFACE.
 * Represents a surface that has been machined.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param face the face that was machined
 */
public final class StepMachinedSurface extends AbstractStepEntity {
    private final StepEntity face;

    public StepMachinedSurface(int id, String name, StepEntity face) {
        super(id, name);
        this.face = face;
    }

    public StepEntity getFace() {
        return face;
    }

    // Record-style accessor
    public StepEntity face() { return getFace(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("face", face);
        return state;
    }
}
