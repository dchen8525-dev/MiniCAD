package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CONTROLLER_FEATURE.
 * A controller feature entity.
 *
 * @param id STEP instance id
 * @param name controller name
 * @param controllerType controller type (PLC, CNC, robot controller)
 * @param controllerGeometry controller geometry representation
 * @varianceInputs controller variance inputs
 * @varianceOutputs controller variance outputs
 * @param communicationProtocol communication protocol specification
 * @param controllerProgram controller program reference
 */
/**
 * Resolved CONTROLLER_FEATURE.
 * A controller feature entity.
 *
 * @param id STEP instance id
 * @param name controller name
 * @param controllerType controller type (PLC, CNC, robot controller)
 * @param controllerGeometry controller geometry representation
 * @varianceInputs controller variance inputs
 * @varianceOutputs controller variance outputs
 * @param communicationProtocol communication protocol specification
 * @param controllerProgram controller program reference
 */
public final class StepControllerFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String controllerType;
    private final StepEntity controllerGeometry;
    private final int varianceInputs;
    private final int varianceOutputs;
    private final String communicationProtocol;
    private final StepEntity controllerProgram;

    public StepControllerFeature(int id, String name, String controllerType, StepEntity controllerGeometry, int varianceInputs, int varianceOutputs, String communicationProtocol, StepEntity controllerProgram) {
        this.id = id;
        this.name = name;
        this.controllerType = controllerType;
        this.controllerGeometry = controllerGeometry;
        this.varianceInputs = varianceInputs;
        this.varianceOutputs = varianceOutputs;
        this.communicationProtocol = communicationProtocol;
        this.controllerProgram = controllerProgram;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getControllerType() {
        return controllerType;
    }

    public StepEntity getControllerGeometry() {
        return controllerGeometry;
    }

    public int getVarianceInputs() {
        return varianceInputs;
    }

    public int getVarianceOutputs() {
        return varianceOutputs;
    }

    public String getCommunicationProtocol() {
        return communicationProtocol;
    }

    public StepEntity getControllerProgram() {
        return controllerProgram;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepControllerFeature that = (StepControllerFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(controllerType, that.controllerType) && Objects.equals(controllerGeometry, that.controllerGeometry) && varianceInputs == that.varianceInputs && varianceOutputs == that.varianceOutputs && Objects.equals(communicationProtocol, that.communicationProtocol) && Objects.equals(controllerProgram, that.controllerProgram);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, controllerType, controllerGeometry, varianceInputs, varianceOutputs, communicationProtocol, controllerProgram);
    }

    @Override
    public String toString() {
        return "StepControllerFeature{" + "id=" + id + "name=" + name + "controllerType=" + controllerType + "controllerGeometry=" + controllerGeometry + "varianceInputs=" + varianceInputs + "varianceOutputs=" + varianceOutputs + "communicationProtocol=" + communicationProtocol + "controllerProgram=" + controllerProgram + "}";
    }
}