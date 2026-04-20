package com.minicad.step.model.topology;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved WIRE_SHELL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param loops defining loops
 */
public record StepWireShell(int id, String name, List<StepLoop> loops) implements StepEntity {

    public StepWireShell {
        loops = List.copyOf(loops);
    }
}
