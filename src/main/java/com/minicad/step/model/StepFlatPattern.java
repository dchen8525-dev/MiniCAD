package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FLAT_PATTERN.
 * A flat pattern entity for sheet metal.
 *
 * @param id STEP instance id
 * @param name pattern name
 * @param flatGeometry flat pattern geometry
 * @param bendLines bend line locations
 * @param formingFeatures forming features in flat state
 * @param grainDirection grain direction reference
 * @param unfoldingSequence unfolding sequence operations
 */
public final class StepFlatPattern extends AbstractStepEntity {
    private final StepEntity flatGeometry;
    private final List<StepEntity> bendLines;
    private final List<StepEntity> formingFeatures;
    private final StepEntity grainDirection;
    private final List<StepEntity> unfoldingSequence;

    public StepFlatPattern(int id, String name, StepEntity flatGeometry, List<StepEntity> bendLines, List<StepEntity> formingFeatures, StepEntity grainDirection, List<StepEntity> unfoldingSequence) {
        super(id, name);
        this.flatGeometry = flatGeometry;
        this.bendLines = bendLines == null ? null : java.util.List.copyOf(bendLines);
        this.formingFeatures = formingFeatures == null ? null : java.util.List.copyOf(formingFeatures);
        this.grainDirection = grainDirection;
        this.unfoldingSequence = unfoldingSequence == null ? null : java.util.List.copyOf(unfoldingSequence);
    }

    public StepEntity getFlatGeometry() {
        return flatGeometry;
    }

    public List<StepEntity> getBendLines() {
        return bendLines;
    }

    public List<StepEntity> getFormingFeatures() {
        return formingFeatures;
    }

    public StepEntity getGrainDirection() {
        return grainDirection;
    }

    public List<StepEntity> getUnfoldingSequence() {
        return unfoldingSequence;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity flatGeometry() { return getFlatGeometry(); }
    public List<StepEntity> bendLines() { return getBendLines(); }
    public List<StepEntity> formingFeatures() { return getFormingFeatures(); }
    public StepEntity grainDirection() { return getGrainDirection(); }
    public List<StepEntity> unfoldingSequence() { return getUnfoldingSequence(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("flatGeometry", flatGeometry);
        state.put("bendLines", bendLines);
        state.put("formingFeatures", formingFeatures);
        state.put("grainDirection", grainDirection);
        state.put("unfoldingSequence", unfoldingSequence);
        return state;
    }
}
