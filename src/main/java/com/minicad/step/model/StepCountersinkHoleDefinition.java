package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved COUNTERSINK_HOLE_DEFINITION.
 * A countersink hole definition entity.
 *
 * @param id STEP instance id
 * @param name hole name
 * @param throughHoleReference reference to the through hole
 * @param countersinkDiameter diameter of the countersink
 * @param countersinkAngle angle of the countersink
 */
public final class StepCountersinkHoleDefinition extends AbstractStepEntity {
    private final StepEntity throughHoleReference;
    private final Double countersinkDiameter;
    private final Double countersinkAngle;

    public StepCountersinkHoleDefinition(int id, String name, StepEntity throughHoleReference, Double countersinkDiameter, Double countersinkAngle) {
        super(id, name);
        this.throughHoleReference = throughHoleReference;
        this.countersinkDiameter = countersinkDiameter;
        this.countersinkAngle = countersinkAngle;
    }

    public StepEntity getThroughHoleReference() {
        return throughHoleReference;
    }

    public Double getCountersinkDiameter() {
        return countersinkDiameter;
    }

    public Double getCountersinkAngle() {
        return countersinkAngle;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("throughHoleReference", throughHoleReference);
        state.put("countersinkDiameter", countersinkDiameter);
        state.put("countersinkAngle", countersinkAngle);
        return state;
    }
}
