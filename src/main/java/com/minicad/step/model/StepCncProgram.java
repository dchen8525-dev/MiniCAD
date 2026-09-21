package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CNC_PROGRAM.
 * A CNC program entity.
 *
 * @param id STEP instance id
 * @param name program name
 * @param programType program type (turning, milling, drilling)
 * @param toolpaths toolpath sequence
 * @param machiningOperations machining operations
 * @param programCode program code/G-code reference
 * @param postProcessor post-processor specification
 * @param machineTarget target CNC machine
 */
public final class StepCncProgram extends AbstractStepEntity {
    private final String programType;
    private final List<StepEntity> toolpaths;
    private final List<StepEntity> machiningOperations;
    private final StepEntity programCode;
    private final String postProcessor;
    private final StepEntity machineTarget;

    public StepCncProgram(int id, String name, String programType, List<StepEntity> toolpaths, List<StepEntity> machiningOperations, StepEntity programCode, String postProcessor, StepEntity machineTarget) {
        super(id, name);
        this.programType = programType;
        this.toolpaths = toolpaths == null ? null : java.util.List.copyOf(toolpaths);
        this.machiningOperations = machiningOperations == null ? null : java.util.List.copyOf(machiningOperations);
        this.programCode = programCode;
        this.postProcessor = postProcessor;
        this.machineTarget = machineTarget;
    }

    public String getProgramType() {
        return programType;
    }

    public List<StepEntity> getToolpaths() {
        return toolpaths;
    }

    public List<StepEntity> getMachiningOperations() {
        return machiningOperations;
    }

    public StepEntity getProgramCode() {
        return programCode;
    }

    public String getPostProcessor() {
        return postProcessor;
    }

    public StepEntity getMachineTarget() {
        return machineTarget;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("programType", programType);
        state.put("toolpaths", toolpaths);
        state.put("machiningOperations", machiningOperations);
        state.put("programCode", programCode);
        state.put("postProcessor", postProcessor);
        state.put("machineTarget", machineTarget);
        return state;
    }
}
