package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal POINT_STYLE.
 *
 * @param id STEP instance id
 * @param name style name
 * @param marker referenced point marker
 * @param markerSize marker size
 * @param colour referenced colour
 */
/**
 * Minimal POINT_STYLE.
 *
 * @param id STEP instance id
 * @param name style name
 * @param marker referenced point marker
 * @param markerSize marker size
 * @param colour referenced colour
 */
public final class StepPointStyle implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity marker;
    private final double markerSize;
    private final StepEntity colour;

    public StepPointStyle(int id, String name, StepEntity marker, double markerSize, StepEntity colour) {
        this.id = id;
        this.name = name;
        this.marker = marker;
        this.markerSize = markerSize;
        this.colour = colour;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getMarker() {
        return marker;
    }

    public double getMarkerSize() {
        return markerSize;
    }

    public StepEntity getColour() {
        return colour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPointStyle that = (StepPointStyle) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(marker, that.marker) && markerSize == that.markerSize && Objects.equals(colour, that.colour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, marker, markerSize, colour);
    }

    @Override
    public String toString() {
        return "StepPointStyle{" + "id=" + id + "name=" + name + "marker=" + marker + "markerSize=" + markerSize + "colour=" + colour + "}";
    }
}
