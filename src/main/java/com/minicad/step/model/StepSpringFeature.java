package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepSpringFeature extends AbstractStepEntity {
    private final String springType;
    private final double wireDiameter;
    private final double coilDiameter;
    private final int numberOfCoils;
    private final double freeLength;
    private final double springRate;
    private final StepEntity springMaterial;

    public StepSpringFeature(int id, String name, String springType, double wireDiameter, double coilDiameter, int numberOfCoils, double freeLength, double springRate, StepEntity springMaterial) {
        super(id, name);
        this.springType = springType;
        this.wireDiameter = wireDiameter;
        this.coilDiameter = coilDiameter;
        this.numberOfCoils = numberOfCoils;
        this.freeLength = freeLength;
        this.springRate = springRate;
        this.springMaterial = springMaterial;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "springType", springType, "wireDiameter", wireDiameter,
                "coilDiameter", coilDiameter, "numberOfCoils", numberOfCoils, "freeLength", freeLength,
                "springRate", springRate, "springMaterial", springMaterial);
    }
}
