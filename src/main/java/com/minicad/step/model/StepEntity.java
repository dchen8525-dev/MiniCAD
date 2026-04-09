package com.minicad.step.model;

/**
 * Marker interface for resolved STEP semantic entities.
 */
public sealed interface StepEntity permits StepCartesianPoint, StepDirection, StepVector,
        StepAxis1Placement, StepAxis2Placement2D, StepAxis2Placement3D, StepPoint, StepCurve, StepSurface,
        StepLine, StepPlane, StepCircle, StepEllipse, StepPolyline, StepOffsetCurve3D, StepOffsetSurface, StepCompositeCurveSegment, StepCompositeCurve, StepCompositeCurveOnSurface,
        StepSurfaceCurve, StepSeamCurve, StepGeometricSet, StepPointSet, StepBoundedCurve, StepBSplineCurve, StepBSplineCurveWithKnots, StepRationalBSplineCurve, StepPiecewiseBezierCurve, StepBezierCurve, StepUniformCurve, StepQuasiUniformCurve, StepSurfaceModel, StepBoundedSurface, StepBSplineSurface, StepBSplineSurfaceWithKnots, StepRationalBSplineSurface, StepPiecewiseBezierSurface, StepBezierSurface, StepUniformSurface, StepQuasiUniformSurface, StepCylindricalSurface,
        StepConicalSurface, StepToroidalSurface, StepSphericalSurface, StepSurfaceOfLinearExtrusion, StepSurfaceOfRevolution,
        StepTrimmedCurve, StepTopologicalRepresentationItem, StepVertex, StepEdge, StepFace, StepPath, StepOpenPath, StepOrientedPath, StepSubpath, StepVertexPoint, StepEdgeCurve, StepSubedge, StepConnectedEdgeSet, StepEdgeBasedWireframeModel,
        StepOrientedEdge, StepLoop, StepFaceBound, StepFaceEntity, StepConnectedFaceSet, StepConnectedFaceSubSet, StepOpenShell, StepSurfacedOpenShell, StepOrientedOpenShell, StepVertexShell, StepWireShell,
        StepClosedShell, StepOrientedClosedShell, StepSolidModel, StepManifoldSolidBrep, StepBrepWithVoids, StepBooleanResult, StepBooleanClippingResult, StepRepresentationContext, StepGeometricRepresentationContext,
        StepNamedUnit, StepSiUnit, StepContextDependentUnit, StepConversionBasedUnit, StepConversionBasedUnitWithOffset, StepRepresentation, StepApplicationContext, StepProductContext, StepProduct,
        StepProductDefinitionFormation, StepProductDefinitionContext, StepProductDefinition,
        StepProductDefinitionShape, StepShapeDefinitionRepresentation, StepMeasureWithUnit, StepTypedMeasureWithUnit,
        StepShapeRepresentationRelationship, StepNextAssemblyUsageOccurrence,
        StepContextDependentShapeRepresentation, StepItemDefinedTransformation,
        StepRepresentationRelationship, StepRepresentationRelationshipWithTransformation, StepUncertaintyMeasureWithUnit,
        StepGlobalUnitAssignedContext, StepGlobalUncertaintyAssignedContext, StepColourRgb,
        StepShellBasedSurfaceModel, StepFaceBasedSurfaceModel, StepShellBasedWireframeModel,
        StepCurveStyle, StepDraughtingPreDefinedCurveFont, StepDraughtingPreDefinedColour,
        StepFillAreaStyleColour, StepFillAreaStyle, StepSurfaceStyleFillArea, StepSurfaceSideStyle,
        StepSurfaceStyleUsage, StepPresentationStyleAssignment, StepStyledItem, StepOverRidingStyledItem,
        StepPresentationLayerAssignment,
        StepAnnotationTextOccurrence, StepGeometricCurveSet, StepDraughtingCallout, StepMeasureRepresentationItem, StepValueRepresentationItem,
        StepGeometricItemSpecificUsage, StepPcurve, StepDegeneratePcurve, StepRepresentationItem, StepGeometricRepresentationItem,
        StepDescriptiveRepresentationItem, StepPropertyDefinition,
        StepPropertyDefinitionRepresentation, StepDerivedUnitElement, StepDerivedUnit,
        StepApplicationProtocolDefinition, StepProductRelatedProductCategory {

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
