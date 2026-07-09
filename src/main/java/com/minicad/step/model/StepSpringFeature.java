package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SPRING_FEATURE.
 * A spring feature entity.
 *
 * @param id STEP instance id
 * @param name spring name
 * @param springType spring type classification (compression, extension, torsion)
 * @param wireDiameter wire diameter
 * @param coilDiameter coil (outer) diameter
 * @param numberOfCoils number of active coils
 * @param freeLength free length
 * @param springRate spring rate/constant
 * @param springMaterial spring material specification
 */
/**
 * Resolved SPRING_FEATURE.
 * A spring feature entity.
 *
 * @param id STEP instance id
 * @param name spring name
 * @param springType spring type classification (compression, extension, torsion)
 * @param wireDiameter wire diameter
 * @param coilDiameter coil (outer) diameter
 * @param numberOfCoils number of active coils
 * @param freeLength free length
 * @param springRate spring rate/constant
 * @param springMaterial spring material specification
 */
public final class StepSpringFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String springType;
    private final double wireDiameter;
    private final double coilDiameter;
    private final int numberOfCoils;
    private final double freeLength;
    private final double springRate;
    private final StepEntity springMaterial;

    public StepSpringFeature(int id, String name, String springType, double wireDiameter, double coilDiameter, int numberOfCoils, double freeLength, double springRate, StepEntity springMaterial) {
        this.id = id;
        this.name = name;
        this.springType = springType;
        this.wireDiameter = wireDiameter;
        this.coilDiameter = coilDiameter;
        this.numberOfCoils = numberOfCoils;
        this.freeLength = freeLength;
        this.springRate = springRate;
        this.springMaterial = springMaterial;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpringType() {
        return springType;
    }

    public double getWireDiameter() {
        return wireDiameter;
    }

    public double getCoilDiameter() {
        return coilDiameter;
    }

    public int getNumberOfCoils() {
        return numberOfCoils;
    }

    public double getFreeLength() {
        return freeLength;
    }

    public double getSpringRate() {
        return springRate;
    }

    public StepEntity getSpringMaterial() {
        return springMaterial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSpringFeature that = (StepSpringFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(springType, that.springType) && wireDiameter == that.wireDiameter && coilDiameter == that.coilDiameter && numberOfCoils == that.numberOfCoils && freeLength == that.freeLength && springRate == that.springRate && Objects.equals(springMaterial, that.springMaterial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, springType, wireDiameter, coilDiameter, numberOfCoils, freeLength, springRate, springMaterial);
    }

    @Override
    public String toString() {
        return "StepSpringFeature{" + "id=" + id + "name=" + name + "springType=" + springType + "wireDiameter=" + wireDiameter + "coilDiameter=" + coilDiameter + "numberOfCoils=" + numberOfCoils + "freeLength=" + freeLength + "springRate=" + springRate + "springMaterial=" + springMaterial + "}";
    }
}