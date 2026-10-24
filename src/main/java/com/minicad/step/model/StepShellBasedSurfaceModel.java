package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Minimal SHELL_BASED_SURFACE_MODEL.
 *
 * @param id step id
 * @param name step label
 * @param shells referenced open or closed shells
 */
public final class StepShellBasedSurfaceModel extends AbstractStepEntity {
    private final List<StepEntity> shells;

    public StepShellBasedSurfaceModel(int id, String name, List<StepEntity> shells) {
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
        return componentsOf(
                "id", getId(), "name", getName(), "shells", shells);
    }
}
