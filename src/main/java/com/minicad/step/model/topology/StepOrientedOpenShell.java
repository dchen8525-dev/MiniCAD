package com.minicad.step.model.topology;

import com.minicad.step.model.base.StepEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.minicad.step.model.geometry.StepSurfacedOpenShell;
import com.minicad.step.model.base.StepFaceEntity;
import java.util.Objects;

/**
 * Resolved ORIENTED_OPEN_SHELL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param openShellElement referenced base open shell
 * @param orientation orientation flag
 */
/**
 * Resolved ORIENTED_OPEN_SHELL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param openShellElement referenced base open shell
 * @param orientation orientation flag
 */
public final class StepOrientedOpenShell implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity openShellElement;
    private final boolean orientation;

    public StepOrientedOpenShell(int id, String name, StepEntity openShellElement, boolean orientation) {
        this.id = id;
        this.name = name;
        this.openShellElement = openShellElement;
        this.orientation = orientation;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getOpenShellElement() {
        return openShellElement;
    }

    public boolean isOrientation() {
        return orientation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepOrientedOpenShell that = (StepOrientedOpenShell) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(openShellElement, that.openShellElement) && orientation == that.orientation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, openShellElement, orientation);
    }

    @Override
    public String toString() {
        return "StepOrientedOpenShell{" + "id=" + id + "name=" + name + "openShellElement=" + openShellElement + "orientation=" + orientation + "}";
    }
}
