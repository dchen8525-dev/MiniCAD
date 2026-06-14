package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved MATERIAL_DESIGNATION.
 * A named material specification.
 *
 * @param id STEP instance id
 * @param name material name
 * @param definitions references defining the material properties
 */
/**
 * Resolved MATERIAL_DESIGNATION.
 * A named material specification.
 *
 * @param id STEP instance id
 * @param name material name
 * @param definitions references defining the material properties
 */
public final class StepMaterialDesignation implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> definitions;

    public StepMaterialDesignation(int id, String name, List<StepEntity> definitions) {
        this.id = id;
        this.name = name;
        this.definitions = definitions == null ? null : java.util.List.copyOf(definitions);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getDefinitions() {
        return definitions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMaterialDesignation that = (StepMaterialDesignation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(definitions, that.definitions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, definitions);
    }

    @Override
    public String toString() {
        return "StepMaterialDesignation{" + "id=" + id + "name=" + name + "definitions=" + definitions + "}";
    }
}
