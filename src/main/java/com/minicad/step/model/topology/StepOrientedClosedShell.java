package com.minicad.step.model.topology;

import com.minicad.step.model.base.StepEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.minicad.step.model.base.StepFaceEntity;
import java.util.Objects;

/**
 * Resolved ORIENTED_CLOSED_SHELL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param closedShellElement referenced base closed shell
 * @param orientation orientation flag
 */
/**
 * Resolved ORIENTED_CLOSED_SHELL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param closedShellElement referenced base closed shell
 * @param orientation orientation flag
 */
public final class StepOrientedClosedShell implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity closedShellElement;
    private final boolean orientation;

    public StepOrientedClosedShell(int id, String name, StepEntity closedShellElement, boolean orientation) {
        this.id = id;
        this.name = name;
        this.closedShellElement = closedShellElement;
        this.orientation = orientation;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
            return ((StepOrientedClosedShell) closedShellElement).faces();
        } else if (closedShellElement instanceof StepClosedShell) {
            return ((StepClosedShell) closedShellElement).faces();
        }
        return Collections.emptyList();
    }

    public StepEntity closedShellElement() {
        return closedShellElement;
    }

    public boolean orientation() {
        return isOrientation();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepOrientedClosedShell that = (StepOrientedClosedShell) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(closedShellElement, that.closedShellElement) && orientation == that.orientation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, closedShellElement, orientation);
    }

    @Override
    public String toString() {
        return "StepOrientedClosedShell{" + "id=" + id + "name=" + name + "closedShellElement=" + closedShellElement + "orientation=" + orientation + "}";
    }
}
