package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepGearFeature extends AbstractStepEntity {
    private final String gearType;
    private final int numberOfTeeth;
    private final double module;
    private final double pressureAngle;
    private final double helixAngle;
    private final double pitchDiameter;
    private final double rootDiameter;
    private final double tipDiameter;

    public StepGearFeature(int id, String name, String gearType, int numberOfTeeth, double module, double pressureAngle, double helixAngle, double pitchDiameter, double rootDiameter, double tipDiameter) {
        super(id, name);
        this.gearType = gearType;
        this.numberOfTeeth = numberOfTeeth;
        this.module = module;
        this.pressureAngle = pressureAngle;
        this.helixAngle = helixAngle;
        this.pitchDiameter = pitchDiameter;
        this.rootDiameter = rootDiameter;
        this.tipDiameter = tipDiameter;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("gearType", gearType);
        state.put("numberOfTeeth", numberOfTeeth);
        state.put("module", module);
        state.put("pressureAngle", pressureAngle);
        state.put("helixAngle", helixAngle);
        state.put("pitchDiameter", pitchDiameter);
        state.put("rootDiameter", rootDiameter);
        state.put("tipDiameter", tipDiameter);
        return state;
    }
}
