package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SHEET_METAL_BEND.
 * A sheet metal bend entity.
 *
 * @param id STEP instance id
 * @param name bend name
 * @param bendLine bend line geometry
 * @param bendAngle bend angle in degrees
 * @param bendRadius bend radius
 * @param bendDirection bend direction (up, down)
 * @param bendAllowance bend allowance factor
 * @param kFactor k-factor for bend calculation
 */
public final class StepSheetMetalBend extends AbstractStepEntity {
    private final StepEntity bendLine;
    private final double bendAngle;
    private final double bendRadius;
    private final String bendDirection;
    private final double bendAllowance;
    private final double kFactor;

    public StepSheetMetalBend(int id, String name, StepEntity bendLine, double bendAngle, double bendRadius, String bendDirection, double bendAllowance, double kFactor) {
        super(id, name);
        this.bendLine = bendLine;
        this.bendAngle = bendAngle;
        this.bendRadius = bendRadius;
        this.bendDirection = bendDirection;
        this.bendAllowance = bendAllowance;
        this.kFactor = kFactor;
    }

    public StepEntity getBendLine() {
        return bendLine;
    }

    public double getBendAngle() {
        return bendAngle;
    }

    public double getBendRadius() {
        return bendRadius;
    }

    public String getBendDirection() {
        return bendDirection;
    }

    public double getBendAllowance() {
        return bendAllowance;
    }

    public double getKFactor() {
        return kFactor;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("bendLine", bendLine);
        state.put("bendAngle", bendAngle);
        state.put("bendRadius", bendRadius);
        state.put("bendDirection", bendDirection);
        state.put("bendAllowance", bendAllowance);
        state.put("kFactor", kFactor);
        return state;
    }
}
