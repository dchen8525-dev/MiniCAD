package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MANIFOLD_SURFACE_MODEL.
 * A surface model composed of a manifold set of connected faces.
 *
 * @param id STEP instance id
 * @param name model name
 * @param shells the shells forming the model
 */
public final class StepManifoldSurfaceModel extends AbstractStepEntity {
    private final List<StepEntity> shells;

    public StepManifoldSurfaceModel(int id, String name, List<StepEntity> shells) {
        super(id, name);
        this.shells = shells == null ? null : java.util.List.copyOf(shells);
    }

    public List<StepEntity> getShells() {
        return shells;
    }

    // Record-style accessor
    public List<StepEntity> shells() {
        return shells;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("shells", shells);
        return state;
    }
}
