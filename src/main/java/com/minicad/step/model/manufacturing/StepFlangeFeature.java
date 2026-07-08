package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FLANGE_FEATURE.
 * A flange feature entity.
 *
 * @param id STEP instance id
 * @param name flange name
 * @param flangeType flange type classification (flat, raised face, weld neck)
 * @param flangeDiameter flange outer diameter
 * @param flangeThickness flange thickness
 * @param boltHoles bolt hole features
 * @param boltCircle bolt circle diameter
 * @param numberOfBoltHoles number of bolt holes
 * @param flangeStandard flange standard specification
 */
/**
 * Resolved FLANGE_FEATURE.
 * A flange feature entity.
 *
 * @param id STEP instance id
 * @param name flange name
 * @param flangeType flange type classification (flat, raised face, weld neck)
 * @param flangeDiameter flange outer diameter
 * @param flangeThickness flange thickness
 * @param boltHoles bolt hole features
 * @param boltCircle bolt circle diameter
 * @param numberOfBoltHoles number of bolt holes
 * @param flangeStandard flange standard specification
 */
public final class StepFlangeFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String flangeType;
    private final double flangeDiameter;
    private final double flangeThickness;
    private final List<StepEntity> boltHoles;
    private final double boltCircle;
    private final int numberOfBoltHoles;
    private final String flangeStandard;

    public StepFlangeFeature(int id, String name, String flangeType, double flangeDiameter, double flangeThickness, List<StepEntity> boltHoles, double boltCircle, int numberOfBoltHoles, String flangeStandard) {
        this.id = id;
        this.name = name;
        this.flangeType = flangeType;
        this.flangeDiameter = flangeDiameter;
        this.flangeThickness = flangeThickness;
        this.boltHoles = boltHoles == null ? null : java.util.List.copyOf(boltHoles);
        this.boltCircle = boltCircle;
        this.numberOfBoltHoles = numberOfBoltHoles;
        this.flangeStandard = flangeStandard;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFlangeType() {
        return flangeType;
    }

    public double getFlangeDiameter() {
        return flangeDiameter;
    }

    public double getFlangeThickness() {
        return flangeThickness;
    }

    public List<StepEntity> getBoltHoles() {
        return boltHoles;
    }

    public double getBoltCircle() {
        return boltCircle;
    }

    public int getNumberOfBoltHoles() {
        return numberOfBoltHoles;
    }

    public String getFlangeStandard() {
        return flangeStandard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFlangeFeature that = (StepFlangeFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(flangeType, that.flangeType) && flangeDiameter == that.flangeDiameter && flangeThickness == that.flangeThickness && Objects.equals(boltHoles, that.boltHoles) && boltCircle == that.boltCircle && numberOfBoltHoles == that.numberOfBoltHoles && Objects.equals(flangeStandard, that.flangeStandard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, flangeType, flangeDiameter, flangeThickness, boltHoles, boltCircle, numberOfBoltHoles, flangeStandard);
    }

    @Override
    public String toString() {
        return "StepFlangeFeature{" + "id=" + id + "name=" + name + "flangeType=" + flangeType + "flangeDiameter=" + flangeDiameter + "flangeThickness=" + flangeThickness + "boltHoles=" + boltHoles + "boltCircle=" + boltCircle + "numberOfBoltHoles=" + numberOfBoltHoles + "flangeStandard=" + flangeStandard + "}";
    }
}