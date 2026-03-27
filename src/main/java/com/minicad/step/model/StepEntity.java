package com.minicad.step.model;

/**
 * Marker interface for resolved STEP semantic entities.
 */
public sealed interface StepEntity permits StepCartesianPoint, StepDirection, StepVector,
        StepAxis2Placement2D, StepAxis2Placement3D, StepLine, StepPlane, StepCircle, StepEllipse, StepSurfaceCurve,
        StepSeamCurve, StepBSplineCurveWithKnots, StepBSplineSurfaceWithKnots, StepCylindricalSurface, StepConicalSurface,
        StepToroidalSurface,
        StepTrimmedCurve, StepVertexPoint, StepEdgeCurve,
        StepOrientedEdge, StepLoop, StepFaceBound, StepFaceEntity, StepOpenShell,
        StepClosedShell, StepManifoldSolidBrep, StepRepresentationContext, StepGeometricRepresentationContext,
        StepNamedUnit, StepSiUnit, StepRepresentation, StepApplicationContext, StepProductContext, StepProduct,
        StepProductDefinitionFormation, StepProductDefinitionContext, StepProductDefinition,
        StepProductDefinitionShape, StepShapeDefinitionRepresentation, StepMeasureWithUnit,
        StepShapeRepresentationRelationship, StepNextAssemblyUsageOccurrence,
        StepContextDependentShapeRepresentation, StepItemDefinedTransformation,
        StepRepresentationRelationshipWithTransformation, StepUncertaintyMeasureWithUnit,
        StepGlobalUnitAssignedContext, StepGlobalUncertaintyAssignedContext, StepColourRgb,
        StepFillAreaStyleColour, StepFillAreaStyle, StepSurfaceStyleFillArea, StepSurfaceSideStyle,
        StepSurfaceStyleUsage, StepPresentationStyleAssignment, StepStyledItem, StepPresentationLayerAssignment,
        StepAnnotationTextOccurrence, StepGeometricCurveSet, StepDraughtingCallout, StepMeasureRepresentationItem,
        StepGeometricItemSpecificUsage, StepPcurve {

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
