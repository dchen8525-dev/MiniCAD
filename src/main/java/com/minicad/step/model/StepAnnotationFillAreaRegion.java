package com.minicad.step.model.annotation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ANNOTATION_FILL_AREA_REGION.
 */
/**
 * Resolved ANNOTATION_FILL_AREA_REGION.
 */
public final class StepAnnotationFillAreaRegion implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> regions;

    public StepAnnotationFillAreaRegion(int id, String name, List<StepEntity> regions) {
        this.id = id;
        this.name = name;
        this.regions = regions == null ? null : java.util.List.copyOf(regions);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getRegions() {
        return regions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAnnotationFillAreaRegion that = (StepAnnotationFillAreaRegion) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(regions, that.regions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, regions);
    }

    @Override
    public String toString() {
        return "StepAnnotationFillAreaRegion{" + "id=" + id + "name=" + name + "regions=" + regions + "}";
    }
}
