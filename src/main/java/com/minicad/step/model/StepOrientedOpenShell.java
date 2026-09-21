package com.minicad.step.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ORIENTED_OPEN_SHELL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param openShellElement referenced base open shell
 * @param orientation orientation flag
 */
public final class StepOrientedOpenShell extends AbstractStepEntity {
    private final StepEntity openShellElement;
    private final boolean orientation;

    public StepOrientedOpenShell(int id, String name, StepEntity openShellElement, boolean orientation) {
        super(id, name);
        this.openShellElement = openShellElement;
        this.orientation = orientation;
    }

    public StepEntity getOpenShellElement() {
        return openShellElement;
    }

    public boolean isOrientation() {
        return orientation;
    }

    // Record-style accessor - derives faces from the underlying open shell
    public List<StepFaceEntity> faces() {
        if (openShellElement instanceof StepOrientedOpenShell) {
            // Handle nested oriented shells
            return ((StepOrientedOpenShell) openShellElement).getFaces();
        } else if (openShellElement instanceof StepOpenShell) {
            return ((StepOpenShell) openShellElement).getFaces();
        } else if (openShellElement instanceof StepSurfacedOpenShell) {
            return ((StepSurfacedOpenShell) openShellElement).getFaces();
        }
        return Collections.emptyList();
    }

    // Java Bean style accessor
    public List<StepFaceEntity> getFaces() {
        return faces();
    }

    public StepEntity openShellElement() {
        return openShellElement;
    }

    public boolean orientation() {
        return isOrientation();
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("openShellElement", openShellElement);
        state.put("orientation", orientation);
        return state;
    }
}
