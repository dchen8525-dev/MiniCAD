package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepControllerFeature(
    int id,
    String name,
    String controllerType,
    StepEntity controllerGeometry,
    int varianceInputs,
    int varianceOutputs,
    String communicationProtocol,
    StepEntity controllerProgram) implements StepEntity {
}