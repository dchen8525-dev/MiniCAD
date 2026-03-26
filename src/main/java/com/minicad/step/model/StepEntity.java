package com.minicad.step.model;

/**
 * Marker interface for resolved STEP semantic entities.
 */
public sealed interface StepEntity permits StepCartesianPoint, StepDirection, StepVector,
        StepAxis2Placement3D, StepLine, StepPlane, StepCircle, StepCylindricalSurface, StepVertexPoint, StepEdgeCurve,
        StepOrientedEdge, StepEdgeLoop, StepFaceBound, StepAdvancedFace, StepOpenShell,
        StepClosedShell, StepManifoldSolidBrep {

    /**
     * Returns the original STEP instance id.
     *
     * @return instance id
     */
    int id();

    /**
     * Returns the optional STEP label/name field.
     *
     * @return label or empty string
     */
    String name();
}
