package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepMachiningSetup implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity workpiece;
    private final StepEntity fixture;
    private final List<StepEntity> toolList;
    private final StepEntity machineSetup;

    public StepMachiningSetup(int id, String name, StepEntity workpiece, StepEntity fixture, List<StepEntity> toolList, StepEntity machineSetup) {
        this.id = id;
        this.name = name;
        this.workpiece = workpiece;
        this.fixture = fixture;
        this.toolList = toolList == null ? null : java.util.List.copyOf(toolList);
        this.machineSetup = machineSetup;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMachiningSetup that = (StepMachiningSetup) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(workpiece, that.workpiece) && Objects.equals(fixture, that.fixture) && Objects.equals(toolList, that.toolList) && Objects.equals(machineSetup, that.machineSetup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, workpiece, fixture, toolList, machineSetup);
    }

    @Override
    public String toString() {
        return "StepMachiningSetup{" + "id=" + id + "name=" + name + "workpiece=" + workpiece + "fixture=" + fixture + "toolList=" + toolList + "machineSetup=" + machineSetup + "}";
    }
}