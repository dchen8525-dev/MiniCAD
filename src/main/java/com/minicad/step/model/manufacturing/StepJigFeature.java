package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved JIG_FEATURE.
 * A jig feature entity.
 *
 * @param id STEP instance id
 * @param name jig name
 * @param jigType jig type classification
 * @param jigGeometry jig geometry representation
 * @param guideElements guide elements for tool positioning
 * @param referenceSurfaces reference surfaces for alignment
 * @param jigCapacity jig capacity/workpiece size
 * @param jigMaterial jig material reference
 */
/**
 * Resolved JIG_FEATURE.
 * A jig feature entity.
 *
 * @param id STEP instance id
 * @param name jig name
 * @param jigType jig type classification
 * @param jigGeometry jig geometry representation
 * @param guideElements guide elements for tool positioning
 * @param referenceSurfaces reference surfaces for alignment
 * @param jigCapacity jig capacity/workpiece size
 * @param jigMaterial jig material reference
 */
public final class StepJigFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String jigType;
    private final StepEntity jigGeometry;
    private final List<StepEntity> guideElements;
    private final List<StepEntity> referenceSurfaces;
    private final double jigCapacity;
    private final StepEntity jigMaterial;

    public StepJigFeature(int id, String name, String jigType, StepEntity jigGeometry, List<StepEntity> guideElements, List<StepEntity> referenceSurfaces, double jigCapacity, StepEntity jigMaterial) {
        this.id = id;
        this.name = name;
        this.jigType = jigType;
        this.jigGeometry = jigGeometry;
        this.guideElements = guideElements == null ? null : java.util.List.copyOf(guideElements);
        this.referenceSurfaces = referenceSurfaces == null ? null : java.util.List.copyOf(referenceSurfaces);
        this.jigCapacity = jigCapacity;
        this.jigMaterial = jigMaterial;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getJigType() {
        return jigType;
    }

    public StepEntity getJigGeometry() {
        return jigGeometry;
    }

    public List<StepEntity> getGuideElements() {
        return guideElements;
    }

    public List<StepEntity> getReferenceSurfaces() {
        return referenceSurfaces;
    }

    public double getJigCapacity() {
        return jigCapacity;
    }

    public StepEntity getJigMaterial() {
        return jigMaterial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepJigFeature that = (StepJigFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(jigType, that.jigType) && Objects.equals(jigGeometry, that.jigGeometry) && Objects.equals(guideElements, that.guideElements) && Objects.equals(referenceSurfaces, that.referenceSurfaces) && jigCapacity == that.jigCapacity && Objects.equals(jigMaterial, that.jigMaterial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, jigType, jigGeometry, guideElements, referenceSurfaces, jigCapacity, jigMaterial);
    }

    @Override
    public String toString() {
        return "StepJigFeature{" + "id=" + id + "name=" + name + "jigType=" + jigType + "jigGeometry=" + jigGeometry + "guideElements=" + guideElements + "referenceSurfaces=" + referenceSurfaces + "jigCapacity=" + jigCapacity + "jigMaterial=" + jigMaterial + "}";
    }
}