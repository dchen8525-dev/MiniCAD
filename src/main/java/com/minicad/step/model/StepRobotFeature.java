package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ROBOT_FEATURE.
 * A robot feature entity.
 *
 * @param id STEP instance id
 * @param name robot name
 * @param robotType robot type (articulated, SCARA, cartesian)
 * @param robotGeometry robot geometry representation
 * @param numberOfAxes number of robot axes
 * @param reachRange robot reach range specification
 * @param payloadCapacity robot payload capacity
 * @varianceSpeed robot variance speed specification
 */
public final class StepRobotFeature extends AbstractStepEntity {
    private final String robotType;
    private final StepEntity robotGeometry;
    private final int numberOfAxes;
    private final List<Double> reachRange;
    private final double payloadCapacity;
    private final double varianceSpeed;

    public StepRobotFeature(int id, String name, String robotType, StepEntity robotGeometry, int numberOfAxes, List<Double> reachRange, double payloadCapacity, double varianceSpeed) {
        super(id, name);
        this.robotType = robotType;
        this.robotGeometry = robotGeometry;
        this.numberOfAxes = numberOfAxes;
        this.reachRange = reachRange == null ? null : java.util.List.copyOf(reachRange);
        this.payloadCapacity = payloadCapacity;
        this.varianceSpeed = varianceSpeed;
    }

    public String getRobotType() {
        return robotType;
    }

    public StepEntity getRobotGeometry() {
        return robotGeometry;
    }

    public int getNumberOfAxes() {
        return numberOfAxes;
    }

    public List<Double> getReachRange() {
        return reachRange;
    }

    public double getPayloadCapacity() {
        return payloadCapacity;
    }

    public double getVarianceSpeed() {
        return varianceSpeed;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("robotType", robotType);
        state.put("robotGeometry", robotGeometry);
        state.put("numberOfAxes", numberOfAxes);
        state.put("reachRange", reachRange);
        state.put("payloadCapacity", payloadCapacity);
        state.put("varianceSpeed", varianceSpeed);
        return state;
    }
}
