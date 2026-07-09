package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved MARKING_FEATURE.
 * A marking feature entity.
 *
 * @param id STEP instance id
 * @param name marking name
 * @param markingType marking type (laser, stamp, ink, engrave)
 * @param markingGeometry marking geometry representation
 * @param markingContent marking content text/symbol
 * @param markingDepth marking depth for engraving
 * @param markingPosition marking position placement
 */
/**
 * Resolved MARKING_FEATURE.
 * A marking feature entity.
 *
 * @param id STEP instance id
 * @param name marking name
 * @param markingType marking type (laser, stamp, ink, engrave)
 * @param markingGeometry marking geometry representation
 * @param markingContent marking content text/symbol
 * @param markingDepth marking depth for engraving
 * @param markingPosition marking position placement
 */
public final class StepMarkingFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String markingType;
    private final StepEntity markingGeometry;
    private final String markingContent;
    private final double markingDepth;
    private final StepEntity markingPosition;

    public StepMarkingFeature(int id, String name, String markingType, StepEntity markingGeometry, String markingContent, double markingDepth, StepEntity markingPosition) {
        this.id = id;
        this.name = name;
        this.markingType = markingType;
        this.markingGeometry = markingGeometry;
        this.markingContent = markingContent;
        this.markingDepth = markingDepth;
        this.markingPosition = markingPosition;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMarkingType() {
        return markingType;
    }

    public StepEntity getMarkingGeometry() {
        return markingGeometry;
    }

    public String getMarkingContent() {
        return markingContent;
    }

    public double getMarkingDepth() {
        return markingDepth;
    }

    public StepEntity getMarkingPosition() {
        return markingPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMarkingFeature that = (StepMarkingFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(markingType, that.markingType) && Objects.equals(markingGeometry, that.markingGeometry) && Objects.equals(markingContent, that.markingContent) && markingDepth == that.markingDepth && Objects.equals(markingPosition, that.markingPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, markingType, markingGeometry, markingContent, markingDepth, markingPosition);
    }

    @Override
    public String toString() {
        return "StepMarkingFeature{" + "id=" + id + "name=" + name + "markingType=" + markingType + "markingGeometry=" + markingGeometry + "markingContent=" + markingContent + "markingDepth=" + markingDepth + "markingPosition=" + markingPosition + "}";
    }
}