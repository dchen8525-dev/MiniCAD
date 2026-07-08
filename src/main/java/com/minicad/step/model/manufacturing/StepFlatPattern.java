package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepFlatPattern implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity flatGeometry;
    private final List<StepEntity> bendLines;
    private final List<StepEntity> formingFeatures;
    private final StepEntity grainDirection;
    private final List<StepEntity> unfoldingSequence;

    public StepFlatPattern(int id, String name, StepEntity flatGeometry, List<StepEntity> bendLines, List<StepEntity> formingFeatures, StepEntity grainDirection, List<StepEntity> unfoldingSequence) {
        this.id = id;
        this.name = name;
        this.flatGeometry = flatGeometry;
        this.bendLines = bendLines == null ? null : java.util.List.copyOf(bendLines);
        this.formingFeatures = formingFeatures == null ? null : java.util.List.copyOf(formingFeatures);
        this.grainDirection = grainDirection;
        this.unfoldingSequence = unfoldingSequence == null ? null : java.util.List.copyOf(unfoldingSequence);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFlatPattern that = (StepFlatPattern) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(flatGeometry, that.flatGeometry) && Objects.equals(bendLines, that.bendLines) && Objects.equals(formingFeatures, that.formingFeatures) && Objects.equals(grainDirection, that.grainDirection) && Objects.equals(unfoldingSequence, that.unfoldingSequence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, flatGeometry, bendLines, formingFeatures, grainDirection, unfoldingSequence);
    }

    @Override
    public String toString() {
        return "StepFlatPattern{" + "id=" + id + "name=" + name + "flatGeometry=" + flatGeometry + "bendLines=" + bendLines + "formingFeatures=" + formingFeatures + "grainDirection=" + grainDirection + "unfoldingSequence=" + unfoldingSequence + "}";
    }
}