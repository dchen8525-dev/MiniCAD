package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved DIRECTION.
 *
 * @param id step id
 * @param name step label
 * @param directionRatios 3D direction ratios
 */
public final class StepDirection extends AbstractStepEntity {
    private final List<Double> directionRatios;

    public StepDirection(int id, String name, List<Double> directionRatios) {
        super(id, name);
        this.directionRatios = directionRatios == null ? null : java.util.List.copyOf(directionRatios);
    }

    public List<Double> getDirectionRatios() {
        return directionRatios;
    }

    // Record-style accessor
    public List<Double> directionRatios() { return getDirectionRatios(); }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "directionRatios", directionRatios);
    }
}
