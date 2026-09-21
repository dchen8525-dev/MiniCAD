package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepControllerFeature extends AbstractStepEntity {
    private final String controllerType;
    private final StepEntity controllerGeometry;
    private final int varianceInputs;
    private final int varianceOutputs;
    private final String communicationProtocol;
    private final StepEntity controllerProgram;

    public StepControllerFeature(int id, String name, String controllerType, StepEntity controllerGeometry, int varianceInputs, int varianceOutputs, String communicationProtocol, StepEntity controllerProgram) {
        super(id, name);
        this.controllerType = controllerType;
        this.controllerGeometry = controllerGeometry;
        this.varianceInputs = varianceInputs;
        this.varianceOutputs = varianceOutputs;
        this.communicationProtocol = communicationProtocol;
        this.controllerProgram = controllerProgram;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("controllerType", controllerType);
        state.put("controllerGeometry", controllerGeometry);
        state.put("varianceInputs", varianceInputs);
        state.put("varianceOutputs", varianceOutputs);
        state.put("communicationProtocol", communicationProtocol);
        state.put("controllerProgram", controllerProgram);
        return state;
    }
}
