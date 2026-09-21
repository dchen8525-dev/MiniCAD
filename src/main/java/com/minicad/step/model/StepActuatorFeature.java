package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ACTUATOR_FEATURE.
 * An actuator feature entity.
 *
 * @param id STEP instance id
 * @param name actuator name
 * @param actuatorType actuator type (linear, rotary, pneumatic, hydraulic)
 * @param actuatorGeometry actuator geometry representation
 * @param actuatorPosition actuator position placement
 * @param actuatorForce actuator force output
 * @param strokeLength actuator stroke length
 * @varianceSpeed actuator variance speed
 */
public final class StepActuatorFeature extends AbstractStepEntity {
    private final String actuatorType;
    private final StepEntity actuatorGeometry;
    private final StepEntity actuatorPosition;
    private final double actuatorForce;
    private final double strokeLength;
    private final double varianceSpeed;

    public StepActuatorFeature(int id, String name, String actuatorType, StepEntity actuatorGeometry, StepEntity actuatorPosition, double actuatorForce, double strokeLength, double varianceSpeed) {
        super(id, name);
        this.actuatorType = actuatorType;
        this.actuatorGeometry = actuatorGeometry;
        this.actuatorPosition = actuatorPosition;
        this.actuatorForce = actuatorForce;
        this.strokeLength = strokeLength;
        this.varianceSpeed = varianceSpeed;
    }

    public String getActuatorType() {
        return actuatorType;
    }

    public StepEntity getActuatorGeometry() {
        return actuatorGeometry;
    }

    public StepEntity getActuatorPosition() {
        return actuatorPosition;
    }

    public double getActuatorForce() {
        return actuatorForce;
    }

    public double getStrokeLength() {
        return strokeLength;
    }

    public double getVarianceSpeed() {
        return varianceSpeed;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("actuatorType", actuatorType);
        state.put("actuatorGeometry", actuatorGeometry);
        state.put("actuatorPosition", actuatorPosition);
        state.put("actuatorForce", actuatorForce);
        state.put("strokeLength", strokeLength);
        state.put("varianceSpeed", varianceSpeed);
        return state;
    }
}
