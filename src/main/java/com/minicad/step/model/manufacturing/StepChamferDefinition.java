package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CHAMFER_DEFINITION.
 * A chamfer definition entity.
 *
 * @param id STEP instance id
 * @param name chamfer name
 * @param edges edges being chamfered
 * @param angle chamfer angle
 * @param width chamfer width
 */
/**
 * Resolved CHAMFER_DEFINITION.
 * A chamfer definition entity.
 *
 * @param id STEP instance id
 * @param name chamfer name
 * @param edges edges being chamfered
 * @param angle chamfer angle
 * @param width chamfer width
 */
public final class StepChamferDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> edges;
    private final Double angle;
    private final Double width;

    public StepChamferDefinition(int id, String name, List<StepEntity> edges, Double angle, Double width) {
        this.id = id;
        this.name = name;
        this.edges = edges == null ? null : java.util.List.copyOf(edges);
        this.angle = angle;
        this.width = width;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getEdges() {
        return edges;
    }

    public Double getAngle() {
        return angle;
    }

    public Double getWidth() {
        return width;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepChamferDefinition that = (StepChamferDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(edges, that.edges) && Objects.equals(angle, that.angle) && Objects.equals(width, that.width);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, edges, angle, width);
    }

    @Override
    public String toString() {
        return "StepChamferDefinition{" + "id=" + id + "name=" + name + "edges=" + edges + "angle=" + angle + "width=" + width + "}";
    }
}