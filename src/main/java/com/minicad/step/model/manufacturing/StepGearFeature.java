package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved GEAR_FEATURE.
 * A gear feature entity.
 *
 * @param id STEP instance id
 * @param name gear name
 * @param gearType gear type classification (spur, helical, bevel, worm)
 * @param numberOfTeeth number of gear teeth
 * @param module gear module
 * @param pressureAngle pressure angle in degrees
 * @param helixAngle helix angle for helical gears
 * @param pitchDiameter pitch diameter
 * @param rootDiameter root diameter
 * @param tipDiameter tip diameter
 */
/**
 * Resolved GEAR_FEATURE.
 * A gear feature entity.
 *
 * @param id STEP instance id
 * @param name gear name
 * @param gearType gear type classification (spur, helical, bevel, worm)
 * @param numberOfTeeth number of gear teeth
 * @param module gear module
 * @param pressureAngle pressure angle in degrees
 * @param helixAngle helix angle for helical gears
 * @param pitchDiameter pitch diameter
 * @param rootDiameter root diameter
 * @param tipDiameter tip diameter
 */
public final class StepGearFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String gearType;
    private final int numberOfTeeth;
    private final double module;
    private final double pressureAngle;
    private final double helixAngle;
    private final double pitchDiameter;
    private final double rootDiameter;
    private final double tipDiameter;

    public StepGearFeature(int id, String name, String gearType, int numberOfTeeth, double module, double pressureAngle, double helixAngle, double pitchDiameter, double rootDiameter, double tipDiameter) {
        this.id = id;
        this.name = name;
        this.gearType = gearType;
        this.numberOfTeeth = numberOfTeeth;
        this.module = module;
        this.pressureAngle = pressureAngle;
        this.helixAngle = helixAngle;
        this.pitchDiameter = pitchDiameter;
        this.rootDiameter = rootDiameter;
        this.tipDiameter = tipDiameter;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGearType() {
        return gearType;
    }

    public int getNumberOfTeeth() {
        return numberOfTeeth;
    }

    public double getModule() {
        return module;
    }

    public double getPressureAngle() {
        return pressureAngle;
    }

    public double getHelixAngle() {
        return helixAngle;
    }

    public double getPitchDiameter() {
        return pitchDiameter;
    }

    public double getRootDiameter() {
        return rootDiameter;
    }

    public double getTipDiameter() {
        return tipDiameter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepGearFeature that = (StepGearFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(gearType, that.gearType) && numberOfTeeth == that.numberOfTeeth && module == that.module && pressureAngle == that.pressureAngle && helixAngle == that.helixAngle && pitchDiameter == that.pitchDiameter && rootDiameter == that.rootDiameter && tipDiameter == that.tipDiameter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, gearType, numberOfTeeth, module, pressureAngle, helixAngle, pitchDiameter, rootDiameter, tipDiameter);
    }

    @Override
    public String toString() {
        return "StepGearFeature{" + "id=" + id + "name=" + name + "gearType=" + gearType + "numberOfTeeth=" + numberOfTeeth + "module=" + module + "pressureAngle=" + pressureAngle + "helixAngle=" + helixAngle + "pitchDiameter=" + pitchDiameter + "rootDiameter=" + rootDiameter + "tipDiameter=" + tipDiameter + "}";
    }
}