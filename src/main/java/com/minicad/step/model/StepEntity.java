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
        StepProductDefinitionFormation, StepProductDefinitionContext, StepProductDefinition, StepRepresentationMap, StepSymbolRepresentationMap,
        StepProductDefinitionShape, StepShapeDefinitionRepresentation, StepMeasureWithUnit, StepTypedMeasureWithUnit,
        StepShapeRepresentationRelationship, StepNextAssemblyUsageOccurrence,
        StepContextDependentShapeRepresentation, StepItemDefinedTransformation,
        StepRepresentationRelationship, StepRepresentationRelationshipWithTransformation, StepUncertaintyMeasureWithUnit,
        StepGlobalUnitAssignedContext, StepGlobalUncertaintyAssignedContext, StepColour, StepColourSpecification, StepColourRgb,
        StepShellBasedSurfaceModel, StepFaceBasedSurfaceModel, StepShellBasedWireframeModel,
        StepCurveStyle, StepPointStyle, StepCharacterGlyphStyleStroke, StepCharacterGlyphStyleOutline, StepCharacterGlyphStyleOutlineWithCharacteristics, StepTextStyleForDefinedFont, StepTextStyle, StepTextStyleWithSpacing, StepTextStyleWithJustification, StepTextStyleWithMirror, StepTextStyleWithBoxCharacteristics, StepSymbolColour, StepSymbolStyle, StepPreDefinedItem, StepPreDefinedMarker, StepPreDefinedSymbol, StepPreDefinedPointMarkerSymbol, StepPreDefinedDimensionSymbol, StepPreDefinedGeometricalToleranceSymbol, StepPreDefinedTerminatorSymbol, StepPreDefinedSurfaceSideStyle, StepPreDefinedTextFont, StepDraughtingPreDefinedTextFont, StepPreDefinedCurveFont, StepDraughtingPreDefinedCurveFont, StepUserDefinedCurveFont, StepPreDefinedColour, StepDraughtingPreDefinedColour, StepUserDefinedMarker, StepUserDefinedTerminatorSymbol,
        StepFillAreaStyleColour, StepFillAreaStyle, StepSurfaceStyleFillArea, StepSurfaceStyleBoundary, StepSurfaceStyleControlGrid, StepSurfaceStyleSegmentationCurve, StepSurfaceStyleSilhouette, StepSurfaceStyleTransparent, StepSurfaceStyleReflectanceAmbient, StepSurfaceStyleReflectanceAmbientDiffuse, StepSurfaceStyleReflectanceAmbientDiffuseSpecular, StepSurfaceStyleParameterLine, StepSurfaceSideStyle,
        StepSurfaceStyleUsage, StepPresentationStyleAssignment, StepStyledItem, StepOverRidingStyledItem,
        StepPresentationLayerAssignment,
        StepAnnotationFillArea, StepAnnotationFillAreaOccurrence, StepAnnotationPlaceholderOccurrence, StepAnnotationPlane, StepAnnotationPointOccurrence, StepAnnotationCurveOccurrence, StepAnnotationSymbol, StepAnnotationSymbolOccurrence, StepAnnotationSubfigureOccurrence, StepDraughtingAnnotationOccurrence, StepTerminatorSymbol, StepAnnotationOccurrenceRelationship, StepLeaderCurve, StepProjectionCurve, StepDimensionCurve, StepAnnotationText, StepAnnotationTextCharacter, StepAnnotationTextOccurrence, StepGeometricCurveSet, StepDraughtingCallout, StepDraughtingCalloutRelationship, StepMeasureRepresentationItem, StepValueRepresentationItem,
        StepItemIdentifiedRepresentationUsage, StepChainBasedItemIdentifiedRepresentationUsage, StepChainBasedGeometricItemSpecificUsage, StepPmiRequirementItemAssociation, StepMechanicalDesignRequirementItemAssociation, StepPlacedTarget, StepDraughtingModelItemAssociation, StepDraughtingModelItemAssociationWithPlaceholder, StepGeometricItemSpecificUsage, StepPcurve, StepDegeneratePcurve, StepRepresentationItem, StepGeometricRepresentationItem,
        StepDescriptiveRepresentationItem, StepPropertyDefinition,
        StepPropertyDefinitionRepresentation, StepAbstractVariable, StepRowVariable, StepScalarVariable,
        StepAttributeAssertion, StepForwardChainingRulePremise, StepBackChainingRuleBody, StepActionPropertyRepresentation,
        StepContactRatioRepresentation, StepKinematicPropertyDefinitionRepresentation,
        StepKinematicPropertyMechanismRepresentation, StepKinematicPropertyRepresentationRelation,
        StepKinematicPropertyTopologyRepresentation, StepPlacedDatumTargetFeature,
        StepResourcePropertyRepresentation, StepDerivedUnitElement, StepDerivedUnit,
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
