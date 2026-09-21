package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved SURFACE_CURVE_SWEPT_AREA_SOLID.
 * A swept solid where the trajectory follows a surface curve.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param sweptArea profile to sweep
 * @param referenceSurface surface the trajectory follows
 * @param trajectory path curve
 * @param startPoint start parameter
 * @param endPoint end parameter
 */
public final class StepSurfaceCurveSweptAreaSolid extends AbstractStepEntity {
    private final StepEntity sweptArea;
    private final StepEntity referenceSurface;
    private final StepEntity trajectory;
    private final double startPoint;
    private final double endPoint;

    public StepSurfaceCurveSweptAreaSolid(int id, String name, StepEntity sweptArea, StepEntity referenceSurface, StepEntity trajectory, double startPoint, double endPoint) {
        super(id, name);
        this.sweptArea = sweptArea;
        this.referenceSurface = referenceSurface;
        this.trajectory = trajectory;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public StepEntity getSweptArea() {
        return sweptArea;
    }

    public StepEntity getReferenceSurface() {
        return referenceSurface;
    }

    public StepEntity getTrajectory() {
        return trajectory;
    }

    public double getStartPoint() {
        return startPoint;
    }

    public double getEndPoint() {
        return endPoint;
    }

    // Record-style accessors
    public StepEntity sweptArea() { return sweptArea; }
    public StepEntity trajectory() { return trajectory; }
    public double startPoint() { return startPoint; }
    public double endPoint() { return endPoint; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("sweptArea", sweptArea);
        state.put("referenceSurface", referenceSurface);
        state.put("trajectory", trajectory);
        state.put("startPoint", startPoint);
        state.put("endPoint", endPoint);
        return state;
    }
}
