package com.minicad.step.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ORIENTED_CLOSED_SHELL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param closedShellElement referenced base closed shell
 * @param orientation orientation flag
 */
public final class StepOrientedClosedShell extends AbstractStepEntity {
    private final StepEntity closedShellElement;
    private final boolean orientation;

    public StepOrientedClosedShell(int id, String name, StepEntity closedShellElement, boolean orientation) {
        super(id, name);
        this.closedShellElement = closedShellElement;
        this.orientation = orientation;
    }

    public StepEntity getClosedShellElement() {
        return closedShellElement;
    }

    public boolean isOrientation() {
        return orientation;
    }

    // Record-style accessor - derives faces from the underlying closed shell
    public List<StepFaceEntity> faces() {
        if (closedShellElement instanceof StepOrientedClosedShell) {
            // Handle nested oriented shells
            return ((StepOrientedClosedShell) closedShellElement).getFaces();
        } else if (closedShellElement instanceof StepClosedShell) {
            return ((StepClosedShell) closedShellElement).getFaces();
        }
        return Collections.emptyList();
    }

    // Java Bean style accessor
    public List<StepFaceEntity> getFaces() {
        return faces();
    }

    public StepEntity closedShellElement() {
        return closedShellElement;
    }

    public boolean orientation() {
        return isOrientation();
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("closedShellElement", closedShellElement);
        state.put("orientation", orientation);
        return state;
    }
}
