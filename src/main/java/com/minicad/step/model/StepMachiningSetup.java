package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MACHINING_SETUP.
 * A machining setup entity.
 *
 * @param id STEP instance id
 * @param name setup name
 * @param workpiece workpiece definition
 * @param fixture fixture definition
 * @param toolList machining tools used
 * @param machineSetup machine setup configuration
 */
public final class StepMachiningSetup extends AbstractStepEntity {
    private final StepEntity workpiece;
    private final StepEntity fixture;
    private final List<StepEntity> toolList;
    private final StepEntity machineSetup;

    public StepMachiningSetup(int id, String name, StepEntity workpiece, StepEntity fixture, List<StepEntity> toolList, StepEntity machineSetup) {
        super(id, name);
        this.workpiece = workpiece;
        this.fixture = fixture;
        this.toolList = toolList == null ? null : java.util.List.copyOf(toolList);
        this.machineSetup = machineSetup;
    }

    public StepEntity getWorkpiece() {
        return workpiece;
    }

    public StepEntity getFixture() {
        return fixture;
    }

    public List<StepEntity> getToolList() {
        return toolList;
    }

    public StepEntity getMachineSetup() {
        return machineSetup;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("workpiece", workpiece);
        state.put("fixture", fixture);
        state.put("toolList", toolList);
        state.put("machineSetup", machineSetup);
        return state;
    }
}
