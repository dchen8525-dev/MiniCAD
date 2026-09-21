package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FILLET_EDGE.
 * A fillet edge entity.
 *
 * @param id STEP instance id
 * @param name edge name
 * @param originalEdge original edge being filleted
 * @param filletRadius fillet radius
 * @param adjacentFaces adjacent faces for fillet
 * @param filletType fillet type classification (constant, variable)
 */
public final class StepFilletEdge extends AbstractStepEntity {
    private final StepEntity originalEdge;
    private final double filletRadius;
    private final List<StepEntity> adjacentFaces;
    private final String filletType;

    public StepFilletEdge(int id, String name, StepEntity originalEdge, double filletRadius, List<StepEntity> adjacentFaces, String filletType) {
        super(id, name);
        this.originalEdge = originalEdge;
        this.filletRadius = filletRadius;
        this.adjacentFaces = adjacentFaces == null ? null : java.util.List.copyOf(adjacentFaces);
        this.filletType = filletType;
    }

    public StepEntity getOriginalEdge() {
        return originalEdge;
    }

    public double getFilletRadius() {
        return filletRadius;
    }

    public List<StepEntity> getAdjacentFaces() {
        return adjacentFaces;
    }

    public String getFilletType() {
        return filletType;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity originalEdge() { return getOriginalEdge(); }
    public double filletRadius() { return getFilletRadius(); }
    public List<StepEntity> adjacentFaces() { return getAdjacentFaces(); }
    public String filletType() { return getFilletType(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("originalEdge", originalEdge);
        state.put("filletRadius", filletRadius);
        state.put("adjacentFaces", adjacentFaces);
        state.put("filletType", filletType);
        return state;
    }
}
