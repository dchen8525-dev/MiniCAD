package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.profile.StepProfileDef;
import com.minicad.step.model.geometry.StepAxis2Placement3D;
import java.util.Objects;
/**
 * Minimal parse-only swept area solid.
 *
 * @param id step id
 * @param name step label
 * @param sweptArea profile definition to sweep
 * @param position solid placement
 * @param sweepReference extrusion direction or revolution axis
 * @param parameter depth or angle in STEP order
 * @param entityName concrete STEP entity name
 */
/**
 * Minimal parse-only swept area solid.
 *
 * @param id step id
 * @param name step label
 * @param sweptArea profile definition to sweep
 * @param position solid placement
 * @param sweepReference extrusion direction or revolution axis
 * @param parameter depth or angle in STEP order
 * @param entityName concrete STEP entity name
 */
public final class StepSweptAreaSolid implements StepEntity {
    private final int id;
    private final String name;
    private final StepProfileDef sweptArea;
    private final StepAxis2Placement3D position;
    private final StepEntity sweepReference;
    private final double parameter;
    private final String entityName;

    public StepSweptAreaSolid(int id, String name, StepProfileDef sweptArea, StepAxis2Placement3D position, StepEntity sweepReference, double parameter, String entityName) {
        this.id = id;
        this.name = name;
        this.sweptArea = sweptArea;
        this.position = position;
        this.sweepReference = sweepReference;
        this.parameter = parameter;
        this.entityName = entityName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepProfileDef getSweptArea() {
        return sweptArea;
    }

    public StepAxis2Placement3D getPosition() {
        return position;
    }

    public StepEntity getSweepReference() {
        return sweepReference;
    }

    public double getParameter() {
        return parameter;
    }

    public String getEntityName() {
        return entityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSweptAreaSolid that = (StepSweptAreaSolid) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(sweptArea, that.sweptArea) && Objects.equals(position, that.position) && Objects.equals(sweepReference, that.sweepReference) && parameter == that.parameter && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sweptArea, position, sweepReference, parameter, entityName);
    }

    @Override
    public String toString() {
        return "StepSweptAreaSolid{" + "id=" + id + "name=" + name + "sweptArea=" + sweptArea + "position=" + position + "sweepReference=" + sweepReference + "parameter=" + parameter + "entityName=" + entityName + "}";
    }
}
