package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved COMPOSITE_DATUM_REFERENCE.
 * A composite datum reference entity.
 *
 * @param id STEP instance id
 * @param name datum name
 * @param datums list of datum entities
 * @param compositeType composite type (common, simultaneous, etc.)
 */
/**
 * Resolved COMPOSITE_DATUM_REFERENCE.
 * A composite datum reference entity.
 *
 * @param id STEP instance id
 * @param name datum name
 * @param datums list of datum entities
 * @param compositeType composite type (common, simultaneous, etc.)
 */
public final class StepCompositeDatumReference implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> datums;
    private final String compositeType;

    public StepCompositeDatumReference(int id, String name, List<StepEntity> datums, String compositeType) {
        this.id = id;
        this.name = name;
        this.datums = datums == null ? null : java.util.List.copyOf(datums);
        this.compositeType = compositeType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getDatums() {
        return datums;
    }

    public String getCompositeType() {
        return compositeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCompositeDatumReference that = (StepCompositeDatumReference) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(datums, that.datums) && Objects.equals(compositeType, that.compositeType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, datums, compositeType);
    }

    @Override
    public String toString() {
        return "StepCompositeDatumReference{" + "id=" + id + "name=" + name + "datums=" + datums + "compositeType=" + compositeType + "}";
    }
}