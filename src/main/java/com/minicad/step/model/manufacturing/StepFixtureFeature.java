package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FIXTURE_FEATURE.
 * A fixture feature entity.
 *
 * @param id STEP instance id
 * @param name fixture name
 * @param fixtureType fixture type (clamping, supporting, locating)
 * @param fixtureGeometry fixture geometry representation
 * @param clampingPoints clamping point locations
 * @param supportingPoints supporting point locations
 * @param fixtureForce fixture force specification
 * @param fixtureMaterial fixture material reference
 */
/**
 * Resolved FIXTURE_FEATURE.
 * A fixture feature entity.
 *
 * @param id STEP instance id
 * @param name fixture name
 * @param fixtureType fixture type (clamping, supporting, locating)
 * @param fixtureGeometry fixture geometry representation
 * @param clampingPoints clamping point locations
 * @param supportingPoints supporting point locations
 * @param fixtureForce fixture force specification
 * @param fixtureMaterial fixture material reference
 */
public final class StepFixtureFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String fixtureType;
    private final StepEntity fixtureGeometry;
    private final List<StepEntity> clampingPoints;
    private final List<StepEntity> supportingPoints;
    private final double fixtureForce;
    private final StepEntity fixtureMaterial;

    public StepFixtureFeature(int id, String name, String fixtureType, StepEntity fixtureGeometry, List<StepEntity> clampingPoints, List<StepEntity> supportingPoints, double fixtureForce, StepEntity fixtureMaterial) {
        this.id = id;
        this.name = name;
        this.fixtureType = fixtureType;
        this.fixtureGeometry = fixtureGeometry;
        this.clampingPoints = clampingPoints == null ? null : java.util.List.copyOf(clampingPoints);
        this.supportingPoints = supportingPoints == null ? null : java.util.List.copyOf(supportingPoints);
        this.fixtureForce = fixtureForce;
        this.fixtureMaterial = fixtureMaterial;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFixtureType() {
        return fixtureType;
    }

    public StepEntity getFixtureGeometry() {
        return fixtureGeometry;
    }

    public List<StepEntity> getClampingPoints() {
        return clampingPoints;
    }

    public List<StepEntity> getSupportingPoints() {
        return supportingPoints;
    }

    public double getFixtureForce() {
        return fixtureForce;
    }

    public StepEntity getFixtureMaterial() {
        return fixtureMaterial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFixtureFeature that = (StepFixtureFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(fixtureType, that.fixtureType) && Objects.equals(fixtureGeometry, that.fixtureGeometry) && Objects.equals(clampingPoints, that.clampingPoints) && Objects.equals(supportingPoints, that.supportingPoints) && fixtureForce == that.fixtureForce && Objects.equals(fixtureMaterial, that.fixtureMaterial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, fixtureType, fixtureGeometry, clampingPoints, supportingPoints, fixtureForce, fixtureMaterial);
    }

    @Override
    public String toString() {
        return "StepFixtureFeature{" + "id=" + id + "name=" + name + "fixtureType=" + fixtureType + "fixtureGeometry=" + fixtureGeometry + "clampingPoints=" + clampingPoints + "supportingPoints=" + supportingPoints + "fixtureForce=" + fixtureForce + "fixtureMaterial=" + fixtureMaterial + "}";
    }
}