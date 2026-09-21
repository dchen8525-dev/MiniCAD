package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepMarkingFeature extends AbstractStepEntity {
    private final String markingType;
    private final StepEntity markingGeometry;
    private final String markingContent;
    private final double markingDepth;
    private final StepEntity markingPosition;

    public StepMarkingFeature(int id, String name, String markingType, StepEntity markingGeometry, String markingContent, double markingDepth, StepEntity markingPosition) {
        super(id, name);
        this.markingType = markingType;
        this.markingGeometry = markingGeometry;
        this.markingContent = markingContent;
        this.markingDepth = markingDepth;
        this.markingPosition = markingPosition;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("markingType", markingType);
        state.put("markingGeometry", markingGeometry);
        state.put("markingContent", markingContent);
        state.put("markingDepth", markingDepth);
        state.put("markingPosition", markingPosition);
        return state;
    }
}
