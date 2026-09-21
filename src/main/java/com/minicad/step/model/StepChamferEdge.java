package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CHAMFER_EDGE.
 * A chamfer edge entity.
 *
 * @param id STEP instance id
 * @param name edge name
 * @param originalEdge original edge being chamfered
 * @param chamferAngle chamfer angle in degrees
 * @param chamferWidth chamfer width/distance
 * @param adjacentFaces adjacent faces for chamfer
 * @param chamferType chamfer type classification (symmetric, asymmetric)
 */
public final class StepChamferEdge extends AbstractStepEntity {
    private final StepEntity originalEdge;
    private final double chamferAngle;
    private final double chamferWidth;
    private final List<StepEntity> adjacentFaces;
    private final String chamferType;

    public StepChamferEdge(int id, String name, StepEntity originalEdge, double chamferAngle, double chamferWidth, List<StepEntity> adjacentFaces, String chamferType) {
        super(id, name);
        this.originalEdge = originalEdge;
        this.chamferAngle = chamferAngle;
        this.chamferWidth = chamferWidth;
        this.adjacentFaces = adjacentFaces == null ? null : java.util.List.copyOf(adjacentFaces);
        this.chamferType = chamferType;
    }

    public StepEntity getOriginalEdge() {
        return originalEdge;
    }

    public double getChamferAngle() {
        return chamferAngle;
    }

    public double getChamferWidth() {
        return chamferWidth;
    }

    public List<StepEntity> getAdjacentFaces() {
        return adjacentFaces;
    }

    public String getChamferType() {
        return chamferType;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity originalEdge() { return getOriginalEdge(); }
    public double chamferAngle() { return getChamferAngle(); }
    public double chamferWidth() { return getChamferWidth(); }
    public List<StepEntity> adjacentFaces() { return getAdjacentFaces(); }
    public String chamferType() { return getChamferType(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("originalEdge", originalEdge);
        state.put("chamferAngle", chamferAngle);
        state.put("chamferWidth", chamferWidth);
        state.put("adjacentFaces", adjacentFaces);
        state.put("chamferType", chamferType);
        return state;
    }
}
