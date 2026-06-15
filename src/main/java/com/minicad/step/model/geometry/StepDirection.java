package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved DIRECTION.
 *
 * @param id step id
 * @param name step label
 * @param directionRatios 3D direction ratios
 */
/**
 * Resolved DIRECTION.
 *
 * @param id step id
 * @param name step label
 * @param directionRatios 3D direction ratios
 */
public final class StepDirection implements StepEntity {
    private final int id;
    private final String name;
    private final List<Double> directionRatios;

    public StepDirection(int id, String name, List<Double> directionRatios) {
        this.id = id;
        this.name = name;
        this.directionRatios = directionRatios == null ? null : java.util.List.copyOf(directionRatios);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Double> getDirectionRatios() {
        return directionRatios;
    }

    // Record-style accessor
    public List<Double> directionRatios() { return getDirectionRatios(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDirection that = (StepDirection) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(directionRatios, that.directionRatios);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, directionRatios);
    }

    @Override
    public String toString() {
        return "StepDirection{" + "id=" + id + "name=" + name + "directionRatios=" + directionRatios + "}";
    }
}
