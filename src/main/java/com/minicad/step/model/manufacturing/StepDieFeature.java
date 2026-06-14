package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved DIE_FEATURE.
 * A die feature entity.
 *
 * @param id STEP instance id
 * @param name die name
 * @param dieType die type classification (stamping, forging, extrusion)
 * @param dieGeometry die geometry representation
 * @param dieSurface die working surface
 * @param dieClearance die clearance specification
 * @param dieMaterial die material specification
 */
/**
 * Resolved DIE_FEATURE.
 * A die feature entity.
 *
 * @param id STEP instance id
 * @param name die name
 * @param dieType die type classification (stamping, forging, extrusion)
 * @param dieGeometry die geometry representation
 * @param dieSurface die working surface
 * @param dieClearance die clearance specification
 * @param dieMaterial die material specification
 */
public final class StepDieFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String dieType;
    private final StepEntity dieGeometry;
    private final StepEntity dieSurface;
    private final double dieClearance;
    private final StepEntity dieMaterial;

    public StepDieFeature(int id, String name, String dieType, StepEntity dieGeometry, StepEntity dieSurface, double dieClearance, StepEntity dieMaterial) {
        this.id = id;
        this.name = name;
        this.dieType = dieType;
        this.dieGeometry = dieGeometry;
        this.dieSurface = dieSurface;
        this.dieClearance = dieClearance;
        this.dieMaterial = dieMaterial;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDieType() {
        return dieType;
    }

    public StepEntity getDieGeometry() {
        return dieGeometry;
    }

    public StepEntity getDieSurface() {
        return dieSurface;
    }

    public double getDieClearance() {
        return dieClearance;
    }

    public StepEntity getDieMaterial() {
        return dieMaterial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDieFeature that = (StepDieFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(dieType, that.dieType) && Objects.equals(dieGeometry, that.dieGeometry) && Objects.equals(dieSurface, that.dieSurface) && dieClearance == that.dieClearance && Objects.equals(dieMaterial, that.dieMaterial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dieType, dieGeometry, dieSurface, dieClearance, dieMaterial);
    }

    @Override
    public String toString() {
        return "StepDieFeature{" + "id=" + id + "name=" + name + "dieType=" + dieType + "dieGeometry=" + dieGeometry + "dieSurface=" + dieSurface + "dieClearance=" + dieClearance + "dieMaterial=" + dieMaterial + "}";
    }
}