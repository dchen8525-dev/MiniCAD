package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepActuatorFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String actuatorType;
    private final StepEntity actuatorGeometry;
    private final StepEntity actuatorPosition;
    private final double actuatorForce;
    private final double strokeLength;
    private final double varianceSpeed;

    public StepActuatorFeature(int id, String name, String actuatorType, StepEntity actuatorGeometry, StepEntity actuatorPosition, double actuatorForce, double strokeLength, double varianceSpeed) {
        this.id = id;
        this.name = name;
        this.actuatorType = actuatorType;
        this.actuatorGeometry = actuatorGeometry;
        this.actuatorPosition = actuatorPosition;
        this.actuatorForce = actuatorForce;
        this.strokeLength = strokeLength;
        this.varianceSpeed = varianceSpeed;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepActuatorFeature that = (StepActuatorFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(actuatorType, that.actuatorType) && Objects.equals(actuatorGeometry, that.actuatorGeometry) && Objects.equals(actuatorPosition, that.actuatorPosition) && actuatorForce == that.actuatorForce && strokeLength == that.strokeLength && varianceSpeed == that.varianceSpeed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, actuatorType, actuatorGeometry, actuatorPosition, actuatorForce, strokeLength, varianceSpeed);
    }

    @Override
    public String toString() {
        return "StepActuatorFeature{" + "id=" + id + "name=" + name + "actuatorType=" + actuatorType + "actuatorGeometry=" + actuatorGeometry + "actuatorPosition=" + actuatorPosition + "actuatorForce=" + actuatorForce + "strokeLength=" + strokeLength + "varianceSpeed=" + varianceSpeed + "}";
    }
}