package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PAINTING_FEATURE.
 * A painting feature entity.
 *
 * @param id STEP instance id
 * @param name painting name
 * @param paintType paint type classification
 * @param paintColor paint color specification
 * @param paintThickness paint thickness
 * @param appliedSurfaces surfaces to be painted
 * @param primerCoat primer coat specification
 * @varnishCoat varnish/clear coat specification
 * @param paintStandard paint standard reference
 */
/**
 * Resolved PAINTING_FEATURE.
 * A painting feature entity.
 *
 * @param id STEP instance id
 * @param name painting name
 * @param paintType paint type classification
 * @param paintColor paint color specification
 * @param paintThickness paint thickness
 * @param appliedSurfaces surfaces to be painted
 * @param primerCoat primer coat specification
 * @varnishCoat varnish/clear coat specification
 * @param paintStandard paint standard reference
 */
public final class StepPaintingFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String paintType;
    private final StepEntity paintColor;
    private final double paintThickness;
    private final List<StepEntity> appliedSurfaces;
    private final StepEntity primerCoat;
    private final StepEntity varnishCoat;
    private final String paintStandard;

    public StepPaintingFeature(int id, String name, String paintType, StepEntity paintColor, double paintThickness, List<StepEntity> appliedSurfaces, StepEntity primerCoat, StepEntity varnishCoat, String paintStandard) {
        this.id = id;
        this.name = name;
        this.paintType = paintType;
        this.paintColor = paintColor;
        this.paintThickness = paintThickness;
        this.appliedSurfaces = appliedSurfaces == null ? null : java.util.List.copyOf(appliedSurfaces);
        this.primerCoat = primerCoat;
        this.varnishCoat = varnishCoat;
        this.paintStandard = paintStandard;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPaintType() {
        return paintType;
    }

    public StepEntity getPaintColor() {
        return paintColor;
    }

    public double getPaintThickness() {
        return paintThickness;
    }

    public List<StepEntity> getAppliedSurfaces() {
        return appliedSurfaces;
    }

    public StepEntity getPrimerCoat() {
        return primerCoat;
    }

    public StepEntity getVarnishCoat() {
        return varnishCoat;
    }

    public String getPaintStandard() {
        return paintStandard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPaintingFeature that = (StepPaintingFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(paintType, that.paintType) && Objects.equals(paintColor, that.paintColor) && paintThickness == that.paintThickness && Objects.equals(appliedSurfaces, that.appliedSurfaces) && Objects.equals(primerCoat, that.primerCoat) && Objects.equals(varnishCoat, that.varnishCoat) && Objects.equals(paintStandard, that.paintStandard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, paintType, paintColor, paintThickness, appliedSurfaces, primerCoat, varnishCoat, paintStandard);
    }

    @Override
    public String toString() {
        return "StepPaintingFeature{" + "id=" + id + "name=" + name + "paintType=" + paintType + "paintColor=" + paintColor + "paintThickness=" + paintThickness + "appliedSurfaces=" + appliedSurfaces + "primerCoat=" + primerCoat + "varnishCoat=" + varnishCoat + "paintStandard=" + paintStandard + "}";
    }
}