package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SPOTFACE_HOLE_DEFINITION.
 * A spotface hole definition entity.
 *
 * @param id STEP instance id
 * @param name hole name
 * @param throughHoleReference reference to the through hole
 * @param spotfaceDiameter diameter of the spotface
 * @param spotfaceDepth depth of the spotface
 */
public final class StepSpotfaceHoleDefinition extends AbstractStepEntity {
    private final StepEntity throughHoleReference;
    private final Double spotfaceDiameter;
    private final Double spotfaceDepth;

    public StepSpotfaceHoleDefinition(int id, String name, StepEntity throughHoleReference, Double spotfaceDiameter, Double spotfaceDepth) {
        super(id, name);
        this.throughHoleReference = throughHoleReference;
        this.spotfaceDiameter = spotfaceDiameter;
        this.spotfaceDepth = spotfaceDepth;
    }

    public StepEntity getThroughHoleReference() {
        return throughHoleReference;
    }

    public Double getSpotfaceDiameter() {
        return spotfaceDiameter;
    }

    public Double getSpotfaceDepth() {
        return spotfaceDepth;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("throughHoleReference", throughHoleReference);
        state.put("spotfaceDiameter", spotfaceDiameter);
        state.put("spotfaceDepth", spotfaceDepth);
        return state;
    }
}
