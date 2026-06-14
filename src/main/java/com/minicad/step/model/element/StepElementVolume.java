package com.minicad.step.model.element;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved ELEMENT_VOLUME.
 * Volume of a finite element.
 */
/**
 * Resolved ELEMENT_VOLUME.
 * Volume of a finite element.
 */
public final class StepElementVolume implements StepEntity {
    private final int id;
    private final String name;
    private final double volume;

    public StepElementVolume(int id, String name, double volume) {
        this.id = id;
        this.name = name;
        this.volume = volume;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getVolume() {
        return volume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepElementVolume that = (StepElementVolume) o;
        return id == that.id && Objects.equals(name, that.name) && volume == that.volume;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, volume);
    }

    @Override
    public String toString() {
        return "StepElementVolume{" + "id=" + id + "name=" + name + "volume=" + volume + "}";
    }
}
