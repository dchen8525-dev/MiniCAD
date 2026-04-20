package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepMachiningSetup(
    int id,
    String name,
    StepEntity workpiece,
    StepEntity fixture,
    List<StepEntity> toolList,
    StepEntity machineSetup) implements StepEntity {
}