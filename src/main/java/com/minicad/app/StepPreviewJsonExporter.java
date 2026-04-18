package com.minicad.app;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.StepParseException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.TopologyException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.app.StepAssemblyGraphBuilder.AssemblyGraph;
import com.minicad.app.StepAssemblyGraphBuilder.AssemblyNode;
import com.minicad.app.StepAssemblyGraphBuilder.AssemblyRepresentation;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.BSplineCurve3;
import com.minicad.geometry.BSplineSurface3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Circle;
import com.minicad.geometry.CompositeCurve3;
import com.minicad.geometry.ConicalSurface;
import com.minicad.geometry.CylindricalSurface;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Ellipse3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.Parabola3;
import com.minicad.geometry.Hyperbola3;
import com.minicad.geometry.Clothoid3;
import com.minicad.geometry.DegenerateCurve3;
import com.minicad.geometry.Plane;
import com.minicad.geometry.Polyline3;
import com.minicad.geometry.RationalBSplineCurve3;
import com.minicad.geometry.RationalBSplineSurface3;
import com.minicad.geometry.OffsetSurface3;
import com.minicad.geometry.RuledSurface3;
import com.minicad.geometry.SurfaceOfConstantRadius3;
import com.minicad.geometry.ParaboloidSurface;
import com.minicad.geometry.HyperboloidSurface;
import com.minicad.geometry.SurfaceOfTranslation3;
import com.minicad.geometry.SurfaceOfProjection3;
import com.minicad.geometry.SurfaceCurve3;
import com.minicad.geometry.SurfaceGeometry;
import com.minicad.geometry.SurfaceOfLinearExtrusion3;
import com.minicad.geometry.SurfaceOfRevolution3;
import com.minicad.geometry.SphericalSurface;
import com.minicad.geometry.ToroidalSurface;
import com.minicad.geometry.TrimmedCurve3;
import com.minicad.geometry.Vector3;
import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.CompositeCurve2;
import com.minicad.geometry2d.Curve2;
import com.minicad.geometry2d.DegenerateCurve2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.Hyperbola2;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Parabola2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry2d.Polyline2;
import com.minicad.geometry2d.RationalBSplineCurve2;
import com.minicad.geometry2d.TrimmedCurve2;
import com.minicad.step.model.StepAbstractVariable;
import com.minicad.step.model.StepAddress;
import com.minicad.step.model.StepAdvancedFace;
import com.minicad.step.syntax.StepValue;
import com.minicad.step.model.StepAnnotationCurveOccurrence;
import com.minicad.step.model.StepAnnotationFillArea;
import com.minicad.step.model.StepAnnotationFillAreaOccurrence;
import com.minicad.step.model.StepAnnotationOccurrenceRelationship;
import com.minicad.step.model.StepAnnotationPlane;
import com.minicad.step.model.StepAnnotationPlaceholderOccurrence;
import com.minicad.step.model.StepAnnotationPointOccurrence;
import com.minicad.step.model.StepAnnotationSubfigureOccurrence;
import com.minicad.step.model.StepAnnotationSymbol;
import com.minicad.step.model.StepAnnotationSymbolOccurrence;
import com.minicad.step.model.StepAnnotationText;
import com.minicad.step.model.StepAnnotationTextCharacter;
import com.minicad.step.model.StepAxis1Placement;
import com.minicad.step.model.StepAxis2Placement2D;
import com.minicad.step.model.StepAxis2Placement3D;
import com.minicad.step.model.StepActionPropertyRepresentation;
import com.minicad.step.model.StepApplicationContext;
import com.minicad.step.model.StepApplicationProtocolDefinition;
import com.minicad.step.model.StepAppliedApprovalAssignment;
import com.minicad.step.model.StepAppliedCertificationAssignment;
import com.minicad.step.model.StepAppliedClassificationAssignment;
import com.minicad.step.model.StepAppliedContractAssignment;
import com.minicad.step.model.StepAppliedDateAssignment;
import com.minicad.step.model.StepAppliedDateTimeAssignment;
import com.minicad.step.model.StepAppliedDocumentReference;
import com.minicad.step.model.StepAppliedExternalIdentificationAssignment;
import com.minicad.step.model.StepAppliedGroupAssignment;
import com.minicad.step.model.StepAppliedIdentificationAssignment;
import com.minicad.step.model.StepAppliedLanguageAssignment;
import com.minicad.step.model.StepAppliedNameAssignment;
import com.minicad.step.model.StepAppliedOrganizationAssignment;
import com.minicad.step.model.StepAppliedPersonAndOrganizationAssignment;
import com.minicad.step.model.StepAppliedSecurityClassificationAssignment;
import com.minicad.step.model.StepApproval;
import com.minicad.step.model.StepApprovalRole;
import com.minicad.step.model.StepApprovalAssignment;
import com.minicad.step.model.StepApprovalDateTime;
import com.minicad.step.model.StepApprovalPersonOrganization;
import com.minicad.step.model.StepApprovalStatus;
import com.minicad.step.model.StepAttributeAssertion;
import com.minicad.step.model.StepBooleanClippingResult;
import com.minicad.step.model.StepBooleanResult;
import com.minicad.step.model.StepBrepWithVoids;
import com.minicad.step.model.StepChainBasedItemIdentifiedRepresentationUsage;
import com.minicad.step.model.StepChainBasedGeometricItemSpecificUsage;
import com.minicad.step.model.StepClosedShell;
import com.minicad.step.model.StepClothoid;
import com.minicad.step.model.StepBezierCurve2D;
import com.minicad.step.model.StepBSplineCurve2D;
import com.minicad.step.model.StepCircle2D;
import com.minicad.step.model.StepCompositeCurve2D;
import com.minicad.step.model.StepCurve2D;
import com.minicad.step.model.StepEllipse2D;
import com.minicad.step.model.StepIndexedPolyCurve2D;
import com.minicad.step.model.StepLine2D;
import com.minicad.step.model.StepPolyline2D;
import com.minicad.step.model.StepQuasiUniformCurve2D;
import com.minicad.step.model.StepRationalBSplineCurve2D;
import com.minicad.step.model.StepSeamCurve;
import com.minicad.step.model.StepTrimmedCurve2D;
import com.minicad.step.model.StepUniformCurve2D;
import com.minicad.step.model.StepPiecewiseBezierCurve2D;
import com.minicad.step.model.StepCertification;
import com.minicad.step.model.StepCertificationAssignment;
import com.minicad.step.model.StepCertificationType;
import com.minicad.step.model.StepClassificationAssignment;
import com.minicad.step.model.StepClassificationRole;
import com.minicad.step.model.StepConnectedEdgeSet;
import com.minicad.step.model.StepConnectedFaceSet;
import com.minicad.step.model.StepConnectedFaceSubSet;
import com.minicad.step.model.StepCartesianTransformationOperator;
import com.minicad.step.model.StepCsgPrimitive;
import com.minicad.step.model.StepCsgSolid;
import com.minicad.step.model.StepCsgVolume;
import com.minicad.step.model.StepBlockVolume;
import com.minicad.step.model.StepConicalSurface;
import com.minicad.step.model.StepContextDependentShapeRepresentation;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepCurve;
import com.minicad.step.model.StepCurveBoundedSurface;
import com.minicad.step.model.StepCurveStyle;
import com.minicad.step.model.StepCylindricalSurface;
import com.minicad.step.model.StepEdgeBasedWireframeModel;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepFaceBound;
import com.minicad.step.model.StepFaceEntity;
import com.minicad.step.model.StepFaceBasedSurfaceModel;
import com.minicad.step.model.StepManifoldSurfaceModel;
import com.minicad.step.model.StepFaceSurface;
import com.minicad.step.model.StepFillAreaStyle;
import com.minicad.step.model.StepFillAreaStyleColour;
import com.minicad.step.model.StepDirection;
import com.minicad.step.model.StepAdvancedBrep;
import com.minicad.step.model.StepBlendedSurface;
import com.minicad.step.model.StepBSplineSurfaceWithKnotsAndBreakpoints;
import com.minicad.step.model.StepConicalSurfaceWithEllipticalAxis;
import com.minicad.step.model.StepCylindricalSurfaceWithEllipticalAxis;
import com.minicad.step.model.StepFlatPattern;
import com.minicad.step.model.StepFiniteElementMesh;
import com.minicad.step.model.StepFacettedBrep;
import com.minicad.step.model.StepFreeFormSurface;
import com.minicad.step.model.StepMachinedSurface;
import com.minicad.step.model.StepNonManifoldSolidBrep;
import com.minicad.step.model.StepOffsetSurface2;
import com.minicad.step.model.StepRectangularCompositeSurface;
import com.minicad.step.model.StepSphericalSurfaceWithEllipticalAxis;
import com.minicad.step.model.StepSurfacePatch;
import com.minicad.step.model.StepToroidalSurfaceWithCylindricalAxis;
import com.minicad.step.model.StepToroidalSurfaceWithEllipticalAxis;
import com.minicad.step.model.StepRuledSurface;
import com.minicad.step.model.StepSurfaceOfConstantRadius;
import com.minicad.step.model.StepTessellatedFace;
import com.minicad.step.model.StepTessellatedFaceSet;
import com.minicad.step.model.StepTessellatedTriangle;
import com.minicad.step.model.StepForwardChainingRulePremise;
import com.minicad.step.model.StepGeometricCurveSet;
import com.minicad.step.model.StepGeometricReplica;
import com.minicad.step.model.StepGeometricRepresentationContext;
import com.minicad.step.model.StepGeometricRepresentationItem;
import com.minicad.step.model.StepGeometricSurfaceSet;
import com.minicad.step.model.StepOrientedSubface;
import com.minicad.step.model.StepSubface;
import com.minicad.step.model.StepGeometricSet;
import com.minicad.step.model.StepGeometricItemSpecificUsage;
import com.minicad.step.model.StepGeneralProperty;
import com.minicad.step.model.StepGeneralPropertyRelationship;
import com.minicad.step.model.StepGlobalUncertaintyAssignedContext;
import com.minicad.step.model.StepIdAttribute;
import com.minicad.step.model.StepIdentificationAssignment;
import com.minicad.step.model.StepIdentificationRole;
import com.minicad.step.model.StepItemDefinedTransformation;
import com.minicad.step.model.StepHalfSpaceSolid;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepAnnotationTextOccurrence;
import com.minicad.step.model.StepBackChainingRuleBody;
import com.minicad.step.model.StepBSplineCurve;
import com.minicad.step.model.StepBSplineCurveWithKnots;
import com.minicad.step.model.StepBSplineCurveWithKnotsAndBreakpoints;
import com.minicad.step.model.StepBSplineSurface;
import com.minicad.step.model.StepBSplineSurfaceWithKnots;
import com.minicad.step.model.StepBezierCurve;
import com.minicad.step.model.StepBezierSurface;
import com.minicad.step.model.StepBoundedCurve;
import com.minicad.step.model.StepBoundedCurve2D;
import com.minicad.step.model.StepBoundedSurface;
import com.minicad.step.model.StepCalendarDate;
import com.minicad.step.model.StepCharacterGlyphStyleOutline;
import com.minicad.step.model.StepCharacterGlyphStyleOutlineWithCharacteristics;
import com.minicad.step.model.StepCharacterGlyphStyleStroke;
import com.minicad.step.model.StepCharacterizedObject;
import com.minicad.step.model.StepCircle;
import com.minicad.step.model.StepColour;
import com.minicad.step.model.StepColourRgb;
import com.minicad.step.model.StepColourSpecification;
import com.minicad.step.model.StepCompositeCurve;
import com.minicad.step.model.StepCompositeCurveOnSurface;
import com.minicad.step.model.StepCompositeCurveOnSurface3D;
import com.minicad.step.model.StepCompositeCurveSegment;
import com.minicad.step.model.StepConicCurve;
import com.minicad.step.model.StepContract;
import com.minicad.step.model.StepContractAssignment;
import com.minicad.step.model.StepContractType;
import com.minicad.step.model.StepContactRatioRepresentation;
import com.minicad.step.model.StepCoordinatedUniversalTimeOffset;
import com.minicad.step.model.StepDraughtingAnnotationOccurrence;
import com.minicad.step.model.StepDraughtingCallout;
import com.minicad.step.model.StepDraughtingCalloutRelationship;
import com.minicad.step.model.StepDraughtingPreDefinedColour;
import com.minicad.step.model.StepDraughtingPreDefinedCurveFont;
import com.minicad.step.model.StepDraughtingPreDefinedTextFont;
import com.minicad.step.model.StepDescriptionAttribute;
import com.minicad.step.model.StepDateAssignment;
import com.minicad.step.model.StepDateAndTime;
import com.minicad.step.model.StepDateRole;
import com.minicad.step.model.StepDateTimeAssignment;
import com.minicad.step.model.StepDateTimeRole;
import com.minicad.step.model.StepDegenerateCurve;
import com.minicad.step.model.StepDegenerateCurve2D;
import com.minicad.step.model.StepDegeneratePcurve;
import com.minicad.step.model.StepDegenerateToroidalSurface;
import com.minicad.step.model.StepDescriptiveRepresentationItem;
import com.minicad.step.model.StepDimensionCurve;
import com.minicad.step.model.StepDimensionalExponents;
import com.minicad.step.model.StepDocument;
import com.minicad.step.model.StepDocumentReference;
import com.minicad.step.model.StepDocumentRelationship;
import com.minicad.step.model.StepDocumentType;
import com.minicad.step.model.StepDocumentUsageConstraint;
import com.minicad.step.model.StepEdge;
import com.minicad.step.model.StepEdgeCurve;
import com.minicad.step.model.StepEdgeLoop;
import com.minicad.step.model.StepEdgeWire;
import com.minicad.step.model.StepChamferEdge;
import com.minicad.step.model.StepFilletEdge;
import com.minicad.step.model.StepSeamEdge;
import com.minicad.step.model.StepEllipse;
import com.minicad.step.model.StepExternalIdentificationAssignment;
import com.minicad.step.model.StepExternallyDefinedItem;
import com.minicad.step.model.StepExternalSource;
import com.minicad.step.model.StepExternalSourceRelationship;
import com.minicad.step.model.StepEffectivity;
import com.minicad.step.model.StepEffectivityRelationship;
import com.minicad.step.model.StepFace;
import com.minicad.step.model.StepDraughtingModelItemAssociation;
import com.minicad.step.model.StepDraughtingModelItemAssociationWithPlaceholder;
import com.minicad.step.model.StepGroup;
import com.minicad.step.model.StepGroupAssignment;
import com.minicad.step.model.StepGroupRelationship;
import com.minicad.step.model.StepItemIdentifiedRepresentationUsage;
import com.minicad.step.model.StepIndexedPolyCurve;
import com.minicad.step.model.StepKinematicPropertyDefinitionRepresentation;
import com.minicad.step.model.StepKinematicPropertyMechanismRepresentation;
import com.minicad.step.model.StepKinematicPropertyRepresentationRelation;
import com.minicad.step.model.StepKinematicPropertyTopologyRepresentation;
import com.minicad.step.model.StepLanguage;
import com.minicad.step.model.StepLanguageAssignment;
import com.minicad.step.model.StepLeaderCurve;
import com.minicad.step.model.StepLocalTime;
import com.minicad.step.model.StepMechanicalDesignRequirementItemAssociation;
import com.minicad.step.model.StepMeasureRepresentationItem;
import com.minicad.step.model.StepMeasureWithUnit;
import com.minicad.step.model.StepMappedItem;
import com.minicad.step.model.StepNameAssignment;
import com.minicad.step.model.StepNameAttribute;
import com.minicad.step.model.StepNextAssemblyUsageOccurrence;
import com.minicad.step.model.StepOpenShell;
import com.minicad.step.model.StepOpenPath;
import com.minicad.step.model.StepOrganization;
import com.minicad.step.model.StepOrganizationAssignment;
import com.minicad.step.model.StepOrganizationRole;
import com.minicad.step.model.StepOrganizationRelationship;
import com.minicad.step.model.StepOrientedClosedShell;
import com.minicad.step.model.StepOrientedOpenShell;
import com.minicad.step.model.StepOrientedCurve;
import com.minicad.step.model.StepOrientedEdge;
import com.minicad.step.model.StepOrientedFace;
import com.minicad.step.model.StepOrientedPath;
import com.minicad.step.model.StepOrientedSurface;
import com.minicad.step.model.StepPath;
import com.minicad.step.model.StepOffsetCurve2D;
import com.minicad.step.model.StepHyperbola2D;
import com.minicad.step.model.StepParabola2D;
import com.minicad.step.model.StepOffsetCurve3D;
import com.minicad.step.model.StepOffsetSurface;
import com.minicad.step.model.StepLoop;
import com.minicad.step.model.StepPlacedDatumTargetFeature;
import com.minicad.step.model.StepPlacedTarget;
import com.minicad.step.model.StepPoint;
import com.minicad.step.model.StepPointSet;
import com.minicad.step.model.StepPointStyle;
import com.minicad.step.model.StepPmiRequirementItemAssociation;
import com.minicad.step.model.StepPresentationLayerAssignment;
import com.minicad.step.model.StepPresentationStyleAssignment;
import com.minicad.step.model.StepPerson;
import com.minicad.step.model.StepPersonAndOrganization;
import com.minicad.step.model.StepPersonAndOrganizationAssignment;
import com.minicad.step.model.StepPersonAndOrganizationRole;
import com.minicad.step.model.StepPreDefinedColour;
import com.minicad.step.model.StepPreDefinedCurveFont;
import com.minicad.step.model.StepPreDefinedDimensionSymbol;
import com.minicad.step.model.StepPreDefinedGeometricalToleranceSymbol;
import com.minicad.step.model.StepPreDefinedItem;
import com.minicad.step.model.StepPreDefinedMarker;
import com.minicad.step.model.StepPreDefinedPointMarkerSymbol;
import com.minicad.step.model.StepPreDefinedSurfaceSideStyle;
import com.minicad.step.model.StepPreDefinedSymbol;
import com.minicad.step.model.StepPreDefinedTerminatorSymbol;
import com.minicad.step.model.StepPreDefinedTextFont;
import com.minicad.step.model.StepPropertyDefinition;
import com.minicad.step.model.StepPropertyDefinitionRelationship;
import com.minicad.step.model.StepPropertyDefinitionRepresentation;
import com.minicad.step.model.StepProduct;
import com.minicad.step.model.StepProductCategory;
import com.minicad.step.model.StepProductCategoryRelationship;
import com.minicad.step.model.StepProductContext;
import com.minicad.step.model.StepProductDefinition;
import com.minicad.step.model.StepProductDefinitionContext;
import com.minicad.step.model.StepProductDefinitionEffectivity;
import com.minicad.step.model.StepProductDefinitionFormation;
import com.minicad.step.model.StepProductDefinitionFormationRelationship;
import com.minicad.step.model.StepProductDefinitionRelationship;
import com.minicad.step.model.StepProductDefinitionRelationshipRelationship;
import com.minicad.step.model.StepProductDefinitionShape;
import com.minicad.step.model.StepProductRelatedProductCategory;
import com.minicad.step.model.StepProductRelationship;
import com.minicad.step.model.StepProfileDef;
import com.minicad.step.model.StepProjectionCurve;
import com.minicad.step.model.StepRepresentationMap;
import com.minicad.step.model.StepRepresentation;
import com.minicad.step.model.StepRepresentationContext;
import com.minicad.step.model.StepRepresentationItem;
import com.minicad.step.model.StepRepresentationRelationship;
import com.minicad.step.model.StepRepresentationRelationshipWithTransformation;
import com.minicad.step.model.StepResourcePropertyRepresentation;
import com.minicad.step.model.StepRowVariable;
import com.minicad.step.model.StepScalarVariable;
import com.minicad.step.model.StepSecurityClassification;
import com.minicad.step.model.StepSecurityClassificationAssignment;
import com.minicad.step.model.StepSecurityClassificationLevel;
import com.minicad.step.model.StepPlane;
import com.minicad.step.model.StepLine;
import com.minicad.step.model.StepLineSegment;
import com.minicad.step.model.StepPolyline;
import com.minicad.step.model.StepPolyLoop;
import com.minicad.step.model.StepPcurve;
import com.minicad.step.model.StepPlanarBox;
import com.minicad.step.model.StepPlanarExtent;
import com.minicad.step.model.StepPiecewiseBezierCurve;
import com.minicad.step.model.StepPiecewiseBezierSurface;
import com.minicad.step.model.StepQuasiUniformCurve;
import com.minicad.step.model.StepQuasiUniformSurface;
import com.minicad.step.model.StepRationalBSplineCurve;
import com.minicad.step.model.StepRationalBSplineSurface;
import com.minicad.step.model.StepRectangularTrimmedSurface;
import com.minicad.step.model.StepSeamCurve;
import com.minicad.step.model.StepShellBasedSurfaceModel;
import com.minicad.step.model.StepShellBasedWireframeModel;
import com.minicad.step.model.StepShapeAspect;
import com.minicad.step.model.StepShapeAspectOccurrence;
import com.minicad.step.model.StepShapeDefinitionRepresentation;
import com.minicad.step.model.StepShapeAspectRelationship;
import com.minicad.step.model.StepShapeRepresentationRelationship;
import com.minicad.step.model.StepSolidModel;
import com.minicad.step.model.StepSolidReplica;
import com.minicad.step.model.StepSubedge;
import com.minicad.step.model.StepStyledItem;
import com.minicad.step.model.StepSubpath;
import com.minicad.step.model.StepSurface;
import com.minicad.step.model.StepSurfaceCurve;
import com.minicad.step.model.StepSurfaceModel;
import com.minicad.step.model.StepSurfaceSideStyle;
import com.minicad.step.model.StepSurfacedEdgeCurve;
import com.minicad.step.model.StepSurfaceStyleBoundary;
import com.minicad.step.model.StepSurfaceStyleControlGrid;
import com.minicad.step.model.StepSurfaceStyleFillArea;
import com.minicad.step.model.StepSurfaceStyleParameterLine;
import com.minicad.step.model.StepSurfaceStyleReflectanceAmbient;
import com.minicad.step.model.StepSurfaceStyleReflectanceAmbientDiffuse;
import com.minicad.step.model.StepSurfaceStyleReflectanceAmbientDiffuseSpecular;
import com.minicad.step.model.StepSurfaceStyleSegmentationCurve;
import com.minicad.step.model.StepSurfaceStyleSilhouette;
import com.minicad.step.model.StepSurfaceStyleTransparent;
import com.minicad.step.model.StepSurfaceStyleUsage;
import com.minicad.step.model.StepSurfaceOfLinearExtrusion;
import com.minicad.step.model.StepSurfaceOfRevolution;
import com.minicad.step.model.StepSurfacedOpenShell;
import com.minicad.step.model.StepSweptAreaSolid;
import com.minicad.step.model.StepComplexClippingResult;
import com.minicad.step.model.StepExtrudedAreaSolidTapered;
import com.minicad.step.model.StepPolygonalBoundedHalfSpace;
import com.minicad.step.model.StepRevolvedAreaSolidTapered;
import com.minicad.step.model.StepSurfaceCurveSweptAreaSolid;
import com.minicad.step.model.StepSweptDiskSolid;
import com.minicad.step.model.StepSphericalSurface;
import com.minicad.step.model.StepSymbolRepresentationMap;
import com.minicad.step.model.StepSymbolColour;
import com.minicad.step.model.StepSymbolStyle;
import com.minicad.step.model.StepTypedMeasureWithUnit;
import com.minicad.step.model.StepUncertaintyMeasureWithUnit;
import com.minicad.step.model.StepTerminatorSymbol;
import com.minicad.step.model.StepTextStyle;
import com.minicad.step.model.StepTextStyleForDefinedFont;
import com.minicad.step.model.StepTextStyleWithBoxCharacteristics;
import com.minicad.step.model.StepTextStyleWithJustification;
import com.minicad.step.model.StepTextStyleWithMirror;
import com.minicad.step.model.StepTextStyleWithSpacing;
import com.minicad.step.model.StepTopologicalRepresentationItem;
import com.minicad.step.model.StepToroidalSurface;
import com.minicad.step.model.StepTrimmedCurve;
import com.minicad.step.model.StepUniformCurve;
import com.minicad.step.model.StepUniformSurface;
import com.minicad.step.model.StepUserDefinedCurveFont;
import com.minicad.step.model.StepUserDefinedMarker;
import com.minicad.step.model.StepUserDefinedTerminatorSymbol;
import com.minicad.step.model.StepValueRepresentationItem;
import com.minicad.step.model.StepVector;
import com.minicad.step.model.StepVertex;
import com.minicad.step.model.StepVertexLoop;
import com.minicad.step.model.StepVertexPoint;
import com.minicad.step.model.StepVertexShell;
import com.minicad.step.model.StepWireShell;
import com.minicad.step.model.StepBoxDomain;
import com.minicad.step.model.StepContextDependentUnit;
import com.minicad.step.model.StepConversionBasedUnit;
import com.minicad.step.model.StepConversionBasedUnitWithOffset;
import com.minicad.step.model.StepDerivedUnit;
import com.minicad.step.model.StepDerivedUnitElement;
import com.minicad.step.model.StepGlobalUnitAssignedContext;
import com.minicad.step.model.StepNamedUnit;
import com.minicad.step.model.StepSiUnit;
import com.minicad.step.model.StepOverRidingStyledItem;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepParser;
import com.minicad.topology.Edge;
import com.minicad.topology.EdgeLoop;
import com.minicad.topology.Face;
import com.minicad.topology.FaceBound;
import com.minicad.topology.Loop;
import com.minicad.topology.OrientedEdge;
import com.minicad.topology.PolyLoop;
import com.minicad.topology.Shell;
import com.minicad.topology.Solid;
import com.minicad.topology.VertexLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Converts supported STEP topology into a JSON payload for the browser viewer.
 */
public final class StepPreviewJsonExporter {
    private static final Logger log = LoggerFactory.getLogger(StepPreviewJsonExporter.class);

    private static final int FACE_PROGRESS_INTERVAL = 25;
    private static final int EDGE_PROGRESS_INTERVAL = 100;
    private static final int MAX_TOTAL_TRIANGLE_POINTS = 6_000_000;
    private static final int GLB_MAX_TOTAL_TRIANGLE_POINTS = 12_000_000;
    private static final int MAX_TOTAL_LOOP_POINTS = 250_000;

    private StepPreviewJsonExporter() {
    }

    public static String export(String stepText) {
        long startedAt = System.nanoTime();
        log.info("stage={} textLength={}", "export_start", stepText.length());
        long parseStartedAt = System.nanoTime();
        StepFile stepFile = StepParser.parse(stepText);
        log.info("stage={} elapsedMs={}, entityCount={}", "parse_done", elapsedMillis(parseStartedAt), stepFile.entities().size());
        long resolveStartedAt = System.nanoTime();
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(stepFile);
        log.info("stage={} elapsedMs={}, resolvedCount={}", "resolve_done", elapsedMillis(resolveStartedAt), resolved.size());
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        long payloadStartedAt = System.nanoTime();
        PreviewPayload payload = buildPayload(stepFile, resolved, builder);
        log.info("stage={} trianglePoints={}, loopPoints={}, edgePoints={}, pmiPoints={}, representationFaceCount={}, representationEdgeCount={}",
                "payload_geometry_summary",
                countTrianglePoints(payload),
                countLoopPoints(payload),
                countEdgePoints(payload),
                countPmiPoints(payload),
                payload.representations().stream().mapToInt(representation -> representation.faces().size()).sum(),
                payload.representations().stream().mapToInt(representation -> representation.edges().size()).sum());
        log.info("stage={} elapsedMs={}, faces={}, edges={}, unsupportedFaces={}, representations={}, instances={}", "payload_done",
                elapsedMillis(payloadStartedAt),
                        payload.faces().size(),
                        payload.edges().size(),
                        payload.unsupportedFaces().size(),
                        payload.representations().size(),
                        payload.instances().size());
        long jsonStartedAt = System.nanoTime();
        String json = toJson(payload);
        log.info("stage={} elapsedMs={}, jsonLength={}", "json_done", elapsedMillis(jsonStartedAt), json.length());
        log.info("stage={} totalElapsedMs={}", "export_done", elapsedMillis(startedAt));
        return json;
    }

    public static byte[] exportBinary(String stepText) {
        long startedAt = System.nanoTime();
        log.info("stage={} textLength={}", "binary_export_start", stepText.length());
        long parseStartedAt = System.nanoTime();
        StepFile stepFile = StepParser.parse(stepText);
        log.info("stage={} elapsedMs={}, entityCount={}", "binary_parse_done", elapsedMillis(parseStartedAt), stepFile.entities().size());
        long resolveStartedAt = System.nanoTime();
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(stepFile);
        log.info("stage={} elapsedMs={}, resolvedCount={}", "binary_resolve_done", elapsedMillis(resolveStartedAt), resolved.size());
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        long payloadStartedAt = System.nanoTime();
        PreviewPayload payload = reducePayloadGeometry(buildPayload(stepFile, resolved, builder));
        log.info("stage={} trianglePoints={}, loopPoints={}, edgePoints={}, pmiPoints={}, representationFaceCount={}, representationEdgeCount={}",
                "binary_payload_geometry_summary",
                countTrianglePoints(payload),
                countLoopPoints(payload),
                countEdgePoints(payload),
                countPmiPoints(payload),
                payload.representations().stream().mapToInt(representation -> representation.faces().size()).sum(),
                payload.representations().stream().mapToInt(representation -> representation.edges().size()).sum());
        log.info("stage={} elapsedMs={}, faces={}, edges={}, unsupportedFaces={}, representations={}, instances={}", "binary_payload_done",
                elapsedMillis(payloadStartedAt),
                payload.faces().size(),
                payload.edges().size(),
                payload.unsupportedFaces().size(),
                payload.representations().size(),
                payload.instances().size());
        long binaryStartedAt = System.nanoTime();
        byte[] binary = toBinary(payload);
        log.info("stage={} elapsedMs={}, binaryLength={}", "binary_encode_done", elapsedMillis(binaryStartedAt), binary.length);
        log.info("stage={} totalElapsedMs={}", "binary_export_done", elapsedMillis(startedAt));
        return binary;
    }

    public static byte[] exportGlb(String stepText) {
        long startedAt = System.nanoTime();
        log.info("stage={} textLength={}", "glb_export_start", stepText.length());
        long parseStartedAt = System.nanoTime();
        StepFile stepFile = StepParser.parse(stepText);
        log.info("stage={} elapsedMs={}, entityCount={}", "glb_parse_done", elapsedMillis(parseStartedAt), stepFile.entities().size());
        long resolveStartedAt = System.nanoTime();
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(stepFile);
        log.info("stage={} elapsedMs={}, resolvedCount={}", "glb_resolve_done", elapsedMillis(resolveStartedAt), resolved.size());
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        long payloadStartedAt = System.nanoTime();
        PreviewPayload payload = reducePayloadGeometry(
                buildPayload(stepFile, resolved, builder),
                GLB_MAX_TOTAL_TRIANGLE_POINTS,
                MAX_TOTAL_LOOP_POINTS,
                "glb_payload_geometry_reduced"
        );
        log.info("stage={} trianglePoints={}, loopPoints={}, edgePoints={}, pmiPoints={}, representationFaceCount={}, representationEdgeCount={}",
                "glb_payload_geometry_summary",
                countTrianglePoints(payload),
                countLoopPoints(payload),
                countEdgePoints(payload),
                countPmiPoints(payload),
                payload.representations().stream().mapToInt(representation -> representation.faces().size()).sum(),
                payload.representations().stream().mapToInt(representation -> representation.edges().size()).sum());
        log.info("stage={} elapsedMs={}, faces={}, edges={}, unsupportedFaces={}, representations={}, instances={}", "glb_payload_done",
                elapsedMillis(payloadStartedAt),
                payload.faces().size(),
                payload.edges().size(),
                payload.unsupportedFaces().size(),
                payload.representations().size(),
                payload.instances().size());
        long glbStartedAt = System.nanoTime();
        byte[] glb = toGlb(payload);
        log.info("stage={} elapsedMs={}, glbLength={}", "glb_encode_done", elapsedMillis(glbStartedAt), glb.length);
        log.info("stage={} totalElapsedMs={}", "glb_export_done", elapsedMillis(startedAt));
        return glb;
    }

    private static PreviewPayload buildPayload(
            StepFile stepFile,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder
    ) {
        long metadataStartedAt = System.nanoTime();
        StepMetadataExtractor metadata = StepMetadataExtractor.fromResolved(resolved);
        log.debug("stage={} elapsedMs={}", "metadata_done", elapsedMillis(metadataStartedAt));
        ProductMetadataExtractor.ProductMetadata productInfo = ProductMetadataExtractor.extract(stepFile, resolved);
        UnitExtractor.UnitInfo units = UnitExtractor.extract(resolved);
        long assemblyStartedAt = System.nanoTime();
        AssemblyData assembly = buildAssemblyData(resolved, builder, metadata);
        log.info("stage={} elapsedMs={}, representations={}, instances={}, unsupportedFaces={}", "assembly_done",
                elapsedMillis(assemblyStartedAt),
                        assembly.representations().size(),
                        assembly.instances().size(),
                        assembly.unsupportedFaces().size());
        boolean assemblyMode = !assembly.instances().isEmpty() && !assembly.representations().isEmpty();
        GeometryCollection legacyGeometry;
        if (assemblyMode) {
            legacyGeometry = new GeometryCollection(List.of(), List.of(), List.of());
        } else {
            long legacyStartedAt = System.nanoTime();
            legacyGeometry = buildLegacyGeometry(resolved, builder, metadata);
            log.debug("stage={} elapsedMs={}, faces={}, edges={}, unsupportedFaces={}", "legacy_geometry_done",
                    elapsedMillis(legacyStartedAt),
                            legacyGeometry.faces().size(),
                            legacyGeometry.edges().size(),
                            legacyGeometry.unsupportedFaces().size());
        }

        BoundsAccumulator geometryBounds = new BoundsAccumulator();
        if (assemblyMode) {
            includeBounds(geometryBounds, assembly.bounds());
        } else {
            includeGeometry(geometryBounds, legacyGeometry);
        }
        List<PmiPayload> pmi = buildPmiPayloads(resolved, assembly, builder);
        BoundsAccumulator bounds = copyBounds(geometryBounds);
        includePmi(bounds, pmi);
        ValidationPayload validation = buildValidationPayload(legacyGeometry, assembly, geometryBounds, resolved);
        List<UnsupportedFacePayload> unsupportedFaces = assemblyMode
                ? assembly.unsupportedFaces()
                : legacyGeometry.unsupportedFaces();
        List<UnsupportedBooleanPayload> unsupportedBooleans = collectUnsupportedBooleans(resolved);
        int faceCount = assemblyMode ? assembly.summary().faceCount() : legacyGeometry.faces().size();
        int edgeCount = assemblyMode ? assembly.summary().edgeCount() : legacyGeometry.edges().size();

        PreviewStats stats = new PreviewStats(
                stepFile.entities().size(),
                countSolidEntities(resolved),
                countShells(resolved),
                faceCount,
                edgeCount,
                unsupportedFaces.size(),
                unsupportedBooleans.size()
        );
        log.info("stage={} entityCount={}, solidCount={}, shellCount={}, faceCount={}, edgeCount={}, unsupportedFaceCount={}, unsupportedBooleanCount={}", "stats_done",
                stats.entityCount(),
                        stats.solidCount(),
                        stats.shellCount(),
                        stats.faceCount(),
                        stats.edgeCount(),
                        stats.unsupportedFaceCount(),
                        stats.unsupportedBooleanCount());
        if (!unsupportedFaces.isEmpty()) {
            log.debug("stage={} bySurfaceType={}, byReason={}", "unsupported_faces_summary",
                    summarizeUnsupportedFacesBySurfaceType(unsupportedFaces),
                            summarizeUnsupportedFacesByReason(unsupportedFaces));
        }
        if (!unsupportedBooleans.isEmpty()) {
            log.debug("stage={} byType={}, byReason={}", "unsupported_booleans_summary",
                    summarizeUnsupportedBooleansByType(unsupportedBooleans),
                    summarizeUnsupportedBooleansByReason(unsupportedBooleans));
        }

        return new PreviewPayload(
                stats,
                bounds.toPayload(),
                validation,
                productInfo,
                units,
                pmi,
                unsupportedBooleans,
                unsupportedFaces,
                legacyGeometry.edges(),
                legacyGeometry.faces(),
                assembly.representations(),
                assembly.instances()
        );
    }

    private static List<UnsupportedBooleanPayload> collectUnsupportedBooleans(Map<Integer, StepEntity> resolved) {
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);
        List<UnsupportedBooleanPayload> list = new ArrayList<>();
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepBooleanClippingResult clippingResult) {
                String reason = unsupportedBooleanReason(builder, clippingResult.id());
                if (reason != null) {
                    list.add(new UnsupportedBooleanPayload(
                            clippingResult.id(),
                            clippingResult.name(),
                            "BOOLEAN_CLIPPING_RESULT",
                            reason
                    ));
                }
            } else if (entity instanceof StepBooleanResult booleanResult) {
                String reason = unsupportedBooleanReason(builder, booleanResult.id());
                if (reason != null) {
                    list.add(new UnsupportedBooleanPayload(
                            booleanResult.id(),
                            booleanResult.name(),
                            "BOOLEAN_RESULT",
                            reason
                    ));
                }
            }
        }
        return List.copyOf(list);
    }

    private static String unsupportedBooleanReason(StepCadBuilder builder, int id) {
        try {
            builder.buildSolid(id);
            return null;
        } catch (RuntimeException ex) {
            String reason = ex.getMessage();
            return reason == null || reason.isBlank()
                    ? "preview export does not support this boolean result"
                    : reason;
        }
    }

    private static GeometryCollection buildLegacyGeometry(
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata
    ) {
        Set<Integer> shellIds = new TreeSet<>();
        Set<Integer> solidIds = new TreeSet<>();
        Map<Integer, EdgePayload> standaloneEdges = new LinkedHashMap<>();
        for (StepEntity entity : resolved.values()) {
            collectShellLikeIds(entity, shellIds);
            if (entity instanceof StepSweptAreaSolid
                    || entity instanceof StepSolidReplica
                    || entity instanceof StepCsgSolid
                    || entity instanceof StepCsgPrimitive
                    || entity instanceof StepBooleanClippingResult
                    || entity instanceof StepBooleanResult
                    || entity instanceof StepSweptDiskSolid
                    || entity instanceof StepExtrudedAreaSolidTapered
                    || entity instanceof StepRevolvedAreaSolidTapered
                    || entity instanceof StepSurfaceCurveSweptAreaSolid
                    || entity instanceof StepPolygonalBoundedHalfSpace
                    || entity instanceof StepComplexClippingResult
                    || entity instanceof StepHalfSpaceSolid
                    || entity instanceof StepCsgVolume
                    || entity instanceof StepBlockVolume
                    || entity instanceof StepFiniteElementMesh
                    || entity instanceof StepFlatPattern
                    || entity instanceof StepBrepWithVoids
                    || entity instanceof StepManifoldSolidBrep
                    || entity instanceof StepFacettedBrep
                    || entity instanceof StepNonManifoldSolidBrep
                    || entity instanceof StepAdvancedBrep
                    || entity instanceof StepMappedItem
                    || entity instanceof StepSolidModel
                    || entity instanceof StepSurfacePatch) {
                solidIds.add(entity.id());
            }
            if (isStandaloneEdgeSource(entity)) {
                collectStandaloneEdges(entity, standaloneEdges, resolved, builder);
            }
        }
        // Remove shells that are referenced by B-rep solids to avoid duplicate processing
        for (Integer solidId : solidIds) {
            StepEntity solidEntity = resolved.get(solidId);
            if (solidEntity instanceof StepManifoldSolidBrep brep) {
                shellIds.remove(brep.outer().id());
            } else if (solidEntity instanceof StepFacettedBrep brep) {
                shellIds.remove(brep.outer().id());
            } else if (solidEntity instanceof StepNonManifoldSolidBrep brep) {
                shellIds.remove(brep.outer().id());
            } else if (solidEntity instanceof StepAdvancedBrep brep) {
                shellIds.remove(brep.outer().id());
                for (StepEntity voidShell : brep.voids()) {
                    shellIds.remove(voidShell.id());
                }
            } else if (solidEntity instanceof StepBrepWithVoids brep) {
                shellIds.remove(brep.outer().id());
                for (StepEntity voidShell : brep.voids()) {
                    shellIds.remove(voidShell.id());
                }
            }
        }
        GeometryCollection shellGeometry = buildGeometryForShells(shellIds, resolved, builder, metadata, Map.of());
        GeometryCollection solidGeometry = buildGeometryForSolids(solidIds, resolved, builder, metadata, Map.of());
        GeometryCollection edgeGeometry = new GeometryCollection(List.copyOf(standaloneEdges.values()), List.of(), List.of());
        return mergeGeometry(mergeGeometry(shellGeometry, solidGeometry), edgeGeometry);
    }

    private static GeometryCollection buildGeometryForShells(
            Set<Integer> shellIds,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Map<Integer, StepMetadataExtractor.DisplayMetadata> inheritedShellMetadata
    ) {
        log.debug("stage={} shellCount={}", "geometry_shells_start", shellIds.size());
        List<FacePayload> faces = new ArrayList<>();
        List<UnsupportedFacePayload> unsupportedFaces = new ArrayList<>();
        Set<Integer> uniqueEdgeIds = new LinkedHashSet<>();
        int processedFaces = 0;

        for (Integer shellId : shellIds) {
            StepEntity shellEntity = resolved.get(shellId);
            if (shellEntity instanceof StepTessellatedFaceSet tessellated) {
                List<FacePayload> tessFaces = buildTessellatedFacePayloads(tessellated, metadata.forItem(shellId));
                faces.addAll(tessFaces);
                log.debug("stage={} shellId={}, tessellatedFaceCount={}", "geometry_tessellated_shell", shellId, tessFaces.size());
                continue;
            }
            if (shellEntity instanceof StepTessellatedFace tessellatedFace) {
                FacePayload payload = buildTessellatedFacePayload(tessellatedFace, metadata.forItem(shellId));
                if (payload != null) {
                    faces.add(payload);
                }
                log.debug("stage={} shellId={}, tessellatedFaceBuilt={}", "geometry_tessellated_face", shellId, payload != null);
                continue;
            }
            List<StepFaceEntity> shellFaces = shellFaces(shellEntity);
            log.debug("stage={} shellId={}, shellFaceCount={}", "geometry_shell_start", shellId, shellFaces.size());
            for (StepFaceEntity stepFace : shellFaces) {
                PreviewFaceResult previewFace = buildPreviewFaceResult(
                        stepFace,
                        builder,
                        mergeMetadata(inheritedShellMetadata.get(shellId), metadata.forItem(stepFace.id()))
                );
                processedFaces++;
                if (previewFace.face() == null) {
                    unsupportedFaces.add(previewFace.unsupportedFace());
                    if (unsupportedFaces.size() <= 10 || unsupportedFaces.size() % FACE_PROGRESS_INTERVAL == 0) {
                        log.debug("stage={} faceId={}, processedFaces={}, unsupportedFaces={}, reason={}", "geometry_face_unsupported",
                                stepFace.id(), processedFaces, unsupportedFaces.size(), (previewFace.unsupportedFace() == null ? "null" : previewFace.unsupportedFace().reason()));
                    }
                    continue;
                }
                faces.add(previewFace.face());
                if (processedFaces % FACE_PROGRESS_INTERVAL == 0) {
                    log.debug("stage={} processedFaces={}, supportedFaces={}, unsupportedFaces={}, uniqueEdges={}", "geometry_face_progress",
                            processedFaces, faces.size(), unsupportedFaces.size(), uniqueEdgeIds.size());
                }
                for (com.minicad.step.model.StepFaceBound bound : stepFace.bounds()) {
                    if (bound.loop() instanceof com.minicad.step.model.StepEdgeLoop edgeLoop) {
                        for (StepOrientedEdge edge : edgeLoop.edges()) {
                            uniqueEdgeIds.add(edge.edgeElement().id());
                        }
                    }
                }
            }
        }

        List<EdgePayload> edges = new ArrayList<>();
        int processedEdges = 0;
        for (Integer edgeId : uniqueEdgeIds) {
            edges.add(buildEdgePayload(edgeId, resolved, builder));
            processedEdges++;
            if (processedEdges % EDGE_PROGRESS_INTERVAL == 0) {
                log.debug("stage={} processedEdges={}, totalUniqueEdges={}", "geometry_edge_progress",
                        processedEdges, uniqueEdgeIds.size());
            }
        }
        log.debug("stage={} shellCount={}, processedFaces={}, supportedFaces={}, unsupportedFaces={}, edges={}", "geometry_shells_done",
                shellIds.size(), processedFaces, faces.size(), unsupportedFaces.size(), edges.size());
        return new GeometryCollection(List.copyOf(edges), List.copyOf(faces), List.copyOf(unsupportedFaces));
    }

    private static GeometryCollection buildGeometryForSolids(
            Set<Integer> solidIds,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Map<Integer, StepMetadataExtractor.DisplayMetadata> inheritedSolidMetadata
    ) {
        List<EdgePayload> edges = new ArrayList<>();
        List<FacePayload> faces = new ArrayList<>();
        List<UnsupportedFacePayload> unsupportedFaces = new ArrayList<>();
        Set<Edge> uniqueEdges = new LinkedHashSet<>();

        for (Integer solidId : solidIds) {
            StepEntity entity = resolved.get(solidId);
            StepMetadataExtractor.DisplayMetadata itemMetadata = mergeMetadata(
                    inheritedSolidMetadata.get(solidId),
                    metadata.forItem(solidId)
            );
            try {
                Solid solid = builder.buildSolid(solidId);
                String baseName = entity == null ? null : entity.name();
                int faceIndex = 0;
                for (Face face : solid.outerShell().faces()) {
                    faces.add(facePayloadFromTopologyFace(
                            solidId * 1000 + faceIndex++,
                            face,
                            baseName,
                            itemMetadata
                    ));
                    collectTopologyEdges(face, uniqueEdges);
                }
                for (var voidShell : solid.voidShells()) {
                    for (Face face : voidShell.faces()) {
                        faces.add(facePayloadFromTopologyFace(
                                solidId * 1000 + faceIndex++,
                                face,
                                baseName,
                                itemMetadata
                        ));
                        collectTopologyEdges(face, uniqueEdges);
                    }
                }
            } catch (UnsupportedGeometryException | StepResolutionException | TopologyException ex) {
                unsupportedFaces.add(new UnsupportedFacePayload(
                        solidId,
                        entity == null ? null : entity.name(),
                        entity == null ? "SOLID" : surfaceTypeName(entity),
                        ex.getMessage()
                ));
            }
        }

        int edgeIndex = 0;
        for (Edge edge : uniqueEdges) {
            edges.add(buildTopologyEdgePayload(-(edgeIndex + 1), edge));
            edgeIndex++;
        }
        return new GeometryCollection(List.copyOf(edges), List.copyOf(faces), List.copyOf(unsupportedFaces));
    }

    private static GeometryCollection mergeGeometry(GeometryCollection left, GeometryCollection right) {
        List<EdgePayload> edges = new ArrayList<>(left.edges());
        edges.addAll(right.edges());
        List<FacePayload> faces = new ArrayList<>(left.faces());
        faces.addAll(right.faces());
        List<UnsupportedFacePayload> unsupportedFaces = new ArrayList<>(left.unsupportedFaces());
        unsupportedFaces.addAll(right.unsupportedFaces());
        return new GeometryCollection(List.copyOf(edges), List.copyOf(faces), List.copyOf(unsupportedFaces));
    }

    private static void collectShellLikeIds(StepEntity item, Set<Integer> shellIds) {
        if (item instanceof StepStyledItem styledItem) {
            collectShellLikeIds(styledItem.item(), shellIds);
            return;
        }
        if (item instanceof StepOverRidingStyledItem styledItem) {
            collectShellLikeIds(styledItem.item(), shellIds);
            return;
        }
        if (isShellLikeEntity(item)) {
            shellIds.add(item.id());
            return;
        }
        // B-rep solid types (MANIFOLD_SOLID_BREP, FACETTED_BREP, etc.) are now handled
        // through the solid path — skip shell collection to avoid duplicate output.
        if (item instanceof StepManifoldSolidBrep
                || item instanceof StepFacettedBrep
                || item instanceof StepNonManifoldSolidBrep
                || item instanceof StepAdvancedBrep
                || item instanceof StepBrepWithVoids
                || item instanceof StepMappedItem
                || item instanceof StepSolidModel
                || item instanceof StepSurfacePatch) {
            return;
        }
        if (item instanceof StepShellBasedSurfaceModel surfaceModel) {
            for (StepEntity shell : surfaceModel.shells()) {
                collectShellLikeIds(shell, shellIds);
            }
            return;
        }
        if (item instanceof StepTessellatedFaceSet) {
            shellIds.add(item.id());
            return;
        }
        if (item instanceof StepTessellatedFace) {
            shellIds.add(item.id());
            return;
        }
        if (item instanceof StepManifoldSurfaceModel manifoldModel) {
            for (StepEntity shell : manifoldModel.shells()) {
                collectShellLikeIds(shell, shellIds);
            }
            return;
        }
        if (item instanceof StepFaceBasedSurfaceModel faceModel) {
            for (StepEntity faceSet : faceModel.faceSets()) {
                collectShellLikeIds(faceSet, shellIds);
            }
        }
    }

    private static void collectStandaloneEdges(
            StepEntity item,
            Map<Integer, EdgePayload> edges,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder
    ) {
        if (item instanceof StepStyledItem styledItem) {
            collectStandaloneEdges(styledItem.item(), edges, resolved, builder);
            return;
        }
        if (item instanceof StepOverRidingStyledItem styledItem) {
            collectStandaloneEdges(styledItem.item(), edges, resolved, builder);
            return;
        }
        if (item instanceof StepPolyline polyline) {
            edges.putIfAbsent(polyline.id(), toPolylineEdgePayload(polyline));
            return;
        }
        if (item instanceof StepGeometricCurveSet curveSet) {
            for (StepEntity element : curveSet.elements()) {
                collectStandaloneEdges(element, edges, resolved, builder);
            }
            return;
        }
        if (item instanceof StepGeometricSet geometricSet) {
            for (StepEntity element : geometricSet.elements()) {
                collectStandaloneEdges(element, edges, resolved, builder);
            }
            return;
        }
        if (item instanceof StepShellBasedWireframeModel wireframeModel) {
            for (StepEntity boundary : wireframeModel.boundaries()) {
                collectStandaloneEdges(boundary, edges, resolved, builder);
            }
            return;
        }
        if (item instanceof StepEdgeBasedWireframeModel wireframeModel) {
            for (StepConnectedEdgeSet boundary : wireframeModel.boundaries()) {
                collectStandaloneEdges(boundary, edges, resolved, builder);
            }
            return;
        }
        if (item instanceof StepConnectedEdgeSet connectedEdgeSet) {
            for (StepEntity edge : connectedEdgeSet.edges()) {
                collectStandaloneEdges(edge, edges, resolved, builder);
            }
            return;
        }
        if (item instanceof StepEdgeCurve edgeCurve) {
            edges.putIfAbsent(edgeCurve.id(), buildEdgePayload(edgeCurve.id(), resolved, builder));
            return;
        }
        if (item instanceof StepFilletEdge filletEdge) {
            edges.putIfAbsent(filletEdge.id(), buildEdgePayload(filletEdge.id(), resolved, builder));
            return;
        }
        if (item instanceof StepChamferEdge chamferEdge) {
            edges.putIfAbsent(chamferEdge.id(), buildEdgePayload(chamferEdge.id(), resolved, builder));
            return;
        }
        if (item instanceof StepPath path) {
            for (StepOrientedEdge orientedEdge : path.edges()) {
                edges.putIfAbsent(orientedEdge.edgeElement().id(), buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder));
            }
            return;
        }
        if (item instanceof StepOpenPath path) {
            for (StepOrientedEdge orientedEdge : path.edges()) {
                edges.putIfAbsent(orientedEdge.edgeElement().id(), buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder));
            }
            return;
        }
        if (item instanceof StepSubpath subpath) {
            for (StepOrientedEdge orientedEdge : subpath.edges()) {
                edges.putIfAbsent(orientedEdge.edgeElement().id(), buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder));
            }
            return;
        }
        if (item instanceof StepOrientedPath orientedPath) {
            for (StepOrientedEdge orientedEdge : orientedPath.edges()) {
                edges.putIfAbsent(orientedEdge.edgeElement().id(), buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder));
            }
            return;
        }
        if (item instanceof StepWireShell wireShell) {
            for (StepEntity loop : wireShell.loops()) {
                collectStandaloneEdges(loop, edges, resolved, builder);
            }
            return;
        }
        if (item instanceof StepEdgeWire edgeWire) {
            for (StepEntity edge : edgeWire.edges()) {
                collectStandaloneEdges(edge, edges, resolved, builder);
            }
            return;
        }
        if (item instanceof StepGeometricSurfaceSet surfaceSet) {
            for (StepEntity element : surfaceSet.elements()) {
                collectStandaloneEdges(element, edges, resolved, builder);
            }
            return;
        }
        if (item instanceof StepEdgeLoop edgeLoop) {
            for (StepOrientedEdge orientedEdge : edgeLoop.edges()) {
                edges.putIfAbsent(orientedEdge.edgeElement().id(), buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder));
            }
            return;
        }
        if (item instanceof StepPolyLoop polyLoop) {
            edges.putIfAbsent(polyLoop.id(), toPolyLoopEdgePayload(polyLoop));
            return;
        }
        if (item instanceof StepVertexShell || item instanceof com.minicad.step.model.StepVertexLoop) {
            return;
        }
        if (item instanceof StepAnnotationCurveOccurrence occurrence) {
            collectStandaloneEdges(occurrence.item(), edges, resolved, builder);
            return;
        }
        if (item instanceof StepAnnotationFillArea fillArea) {
            for (StepEntity boundary : fillArea.boundaries()) {
                collectStandaloneEdges(boundary, edges, resolved, builder);
            }
            return;
        }
        if (item instanceof StepAnnotationFillAreaOccurrence fillAreaOccurrence) {
            collectStandaloneEdges(fillAreaOccurrence.item(), edges, resolved, builder);
            return;
        }
        if (item instanceof StepAnnotationSymbol annotationSymbol) {
            collectMappedAnnotationEdges(
                    annotationSymbol.id(),
                    annotationSymbol.mappingSource().mappedRepresentation(),
                    annotationSymbol.mappingSource().mappedOrigin(),
                    annotationSymbol.mappingTarget(),
                    null,
                    null,
                    edges,
                    resolved,
                    builder
            );
            return;
        }
        if (item instanceof StepAnnotationSymbolOccurrence symbolOccurrence) {
            if (!collectMappedAnnotationCarrierEdges(
                    symbolOccurrence.id(),
                    "ANNOTATION_SYMBOL_OCCURRENCE",
                    symbolOccurrence.id(),
                    symbolOccurrence.item(),
                    edges,
                    resolved,
                    builder
            )) {
                collectStandaloneEdges(symbolOccurrence.item(), edges, resolved, builder);
            }
            return;
        }
        if (item instanceof StepAnnotationSubfigureOccurrence subfigureOccurrence) {
            if (!collectMappedAnnotationCarrierEdges(
                    subfigureOccurrence.id(),
                    "ANNOTATION_SUBFIGURE_OCCURRENCE",
                    subfigureOccurrence.id(),
                    subfigureOccurrence.item(),
                    edges,
                    resolved,
                    builder
            )) {
                collectStandaloneEdges(subfigureOccurrence.item(), edges, resolved, builder);
            }
            return;
        }
        if (item instanceof StepAnnotationText annotationText) {
            collectMappedAnnotationEdges(
                    annotationText.id(),
                    annotationText.mappingSource().mappedRepresentation(),
                    annotationText.mappingSource().mappedOrigin(),
                    annotationText.mappingTarget(),
                    null,
                    null,
                    edges,
                    resolved,
                    builder
            );
            return;
        }
        if (item instanceof StepAnnotationTextCharacter annotationTextCharacter) {
            collectMappedAnnotationEdges(
                    annotationTextCharacter.id(),
                    annotationTextCharacter.mappingSource().mappedRepresentation(),
                    annotationTextCharacter.mappingSource().mappedOrigin(),
                    annotationTextCharacter.mappingTarget(),
                    null,
                    null,
                    edges,
                    resolved,
                    builder
            );
            return;
        }
        if (item instanceof StepDimensionCurve dimensionCurve) {
            EdgePayload sampled = sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
                collectStandaloneEdges(dimensionCurve.item(), edges, resolved, builder);
            }
            return;
        }
        if (item instanceof StepLeaderCurve leaderCurve) {
            EdgePayload sampled = sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
                collectStandaloneEdges(leaderCurve.item(), edges, resolved, builder);
            }
            return;
        }
        if (item instanceof StepProjectionCurve projectionCurve) {
            EdgePayload sampled = sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
                collectStandaloneEdges(projectionCurve.item(), edges, resolved, builder);
            }
            return;
        }
        if (item instanceof StepDraughtingAnnotationOccurrence annotationOccurrence) {
            EdgePayload sampled = sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            } else if (collectMappedAnnotationCarrierEdges(
                    annotationOccurrence.id(),
                    "DRAUGHTING_ANNOTATION_OCCURRENCE",
                    annotationOccurrence.id(),
                    annotationOccurrence.item(),
                    edges,
                    resolved,
                    builder
            )) {
                return;
            } else {
                collectStandaloneEdges(annotationOccurrence.item(), edges, resolved, builder);
            }
            return;
        }
        if (item instanceof StepTerminatorSymbol terminatorSymbol) {
            EdgePayload sampled = sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
                collectStandaloneEdges(terminatorSymbol.annotatedCurve(), edges, resolved, builder);
            }
            return;
        }
        if (item instanceof StepAnnotationCurveOccurrence occurrence) {
            EdgePayload sampled = sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
                collectStandaloneEdges(occurrence.item(), edges, resolved, builder);
            }
            return;
        }
        if (item instanceof StepFilletEdge filletEdge) {
            collectStandaloneEdges(filletEdge.originalEdge(), edges, resolved, builder);
            return;
        }
        if (item instanceof StepChamferEdge chamferEdge) {
            collectStandaloneEdges(chamferEdge.originalEdge(), edges, resolved, builder);
            return;
        }
        if (item instanceof StepSubedge subedge) {
            collectStandaloneEdges(subedge.parentEdge(), edges, resolved, builder);
            return;
        }
        if (isSampledCurveSource(item)) {
            EdgePayload sampled = sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            }
        }
    }

    private static boolean isStandaloneEdgeSource(StepEntity item) {
        return item instanceof StepPolyline
                || item instanceof StepGeometricCurveSet
                || item instanceof StepGeometricSet
                || item instanceof StepShellBasedWireframeModel
                || item instanceof StepEdgeBasedWireframeModel
                || item instanceof StepConnectedEdgeSet
                || item instanceof StepEdgeWire
                || item instanceof StepPath
                || item instanceof StepOpenPath
                || item instanceof StepSubpath
                || item instanceof StepOrientedPath
                || item instanceof StepWireShell
                || item instanceof StepAnnotationCurveOccurrence
                || item instanceof StepAnnotationFillArea
                || item instanceof StepAnnotationFillAreaOccurrence
                || item instanceof StepAnnotationSymbol
                || item instanceof StepAnnotationSymbolOccurrence
                || item instanceof StepAnnotationSubfigureOccurrence
                || item instanceof StepFilletEdge
                || item instanceof StepChamferEdge
                || item instanceof StepSubedge
                || item instanceof StepAnnotationText
                || item instanceof StepAnnotationTextCharacter
                || item instanceof StepDimensionCurve
                || item instanceof StepLeaderCurve
                || item instanceof StepProjectionCurve
                || item instanceof StepDraughtingAnnotationOccurrence
                || item instanceof StepTerminatorSymbol
                || item instanceof StepGeometricSurfaceSet;
    }

    private static boolean isSampledCurveSource(StepEntity item) {
        return item instanceof StepLine
                || item instanceof StepCircle
                || item instanceof StepEllipse
                || item instanceof StepConicCurve
                || item instanceof StepBezierCurve
                || item instanceof StepUniformCurve
                || item instanceof StepQuasiUniformCurve
                || item instanceof StepPiecewiseBezierCurve
                || item instanceof StepBSplineCurveWithKnots
                || item instanceof StepBSplineCurve
                || item instanceof com.minicad.step.model.StepRationalBSplineCurve
                || item instanceof StepSurfaceCurve
                || item instanceof StepSeamCurve
                || item instanceof StepTrimmedCurve
                || item instanceof StepPolyline
                || item instanceof com.minicad.step.model.StepCompositeCurve
                || item instanceof com.minicad.step.model.StepCompositeCurveOnSurface
                || item instanceof StepCompositeCurveOnSurface3D
                || item instanceof StepOffsetCurve2D
                || item instanceof StepOffsetCurve3D
                || item instanceof StepPcurve
                || item instanceof StepDegeneratePcurve
                || item instanceof StepOrientedCurve
                || item instanceof StepAnnotationCurveOccurrence
                || item instanceof StepDimensionCurve
                || item instanceof StepLeaderCurve
                || item instanceof StepProjectionCurve
                || item instanceof StepDraughtingAnnotationOccurrence
                || item instanceof StepTerminatorSymbol
                || item instanceof StepClothoid
                || item instanceof StepIndexedPolyCurve
                || item instanceof StepDegenerateCurve
                || item instanceof StepBSplineCurveWithKnotsAndBreakpoints
                || item instanceof StepLineSegment
                || item instanceof StepEdgeCurve
                || item instanceof StepSurfacedEdgeCurve
                || item instanceof StepPath
                || item instanceof StepOpenPath
                || item instanceof StepSubpath
                || item instanceof StepOrientedPath
                || item instanceof StepCurve
                || item instanceof StepBoundedCurve
                || item instanceof StepCircle2D
                || item instanceof StepEllipse2D
                || item instanceof StepPolyline2D
                || item instanceof StepTrimmedCurve2D
                || item instanceof StepCompositeCurve2D
                || item instanceof StepBezierCurve2D
                || item instanceof StepQuasiUniformCurve2D
                || item instanceof StepUniformCurve2D
                || item instanceof StepPiecewiseBezierCurve2D
                || item instanceof StepIndexedPolyCurve2D
                || item instanceof StepDegenerateCurve2D
                || item instanceof StepBSplineCurve2D
                || item instanceof StepRationalBSplineCurve2D
                || item instanceof StepLine2D
                || item instanceof StepCurve2D
                || item instanceof StepHyperbola2D
                || item instanceof StepParabola2D
                || (item instanceof StepGeometricReplica replica && "CURVE_REPLICA".equals(replica.entityName()));
    }

    private static StepEntity unwrapStyledItem(StepEntity item) {
        StepEntity current = item;
        while (true) {
            if (current instanceof StepStyledItem styledItem) {
                current = styledItem.item();
                continue;
            }
            if (current instanceof StepOverRidingStyledItem styledItem) {
                current = styledItem.item();
                continue;
            }
            return current;
        }
    }

    private static void collectMappedAnnotationEdges(
            int mappedOwnerId,
            StepRepresentation representation,
            StepEntity mappedOrigin,
            StepEntity mappingTarget,
            String sourceType,
            Integer sourceStepId,
            Map<Integer, EdgePayload> edges,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder
    ) {
        double[] matrix = matrixForMappedPlacement(mappedOrigin, mappingTarget, builder);
        if (matrix == null) {
            return;
        }
        RepresentationBuildResult source = buildRepresentationPayload(
                representation,
                representation.name(),
                resolved,
                builder,
                StepMetadataExtractor.fromResolved(resolved),
                new LinkedHashSet<>()
        );
        for (EdgePayload edge : source.payload().edges()) {
            EdgePayload transformed = transformMappedEdge(edge, mappedOwnerId, matrix, sourceType, sourceStepId);
            edges.putIfAbsent(transformed.stepId(), transformed);
        }
    }

    private static boolean collectMappedAnnotationCarrierEdges(
            int mappedOwnerId,
            String sourceType,
            Integer sourceStepId,
            StepEntity item,
            Map<Integer, EdgePayload> edges,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder
    ) {
        if (item instanceof StepAnnotationSymbol annotationSymbol) {
            collectMappedAnnotationEdges(
                    mappedOwnerId,
                    annotationSymbol.mappingSource().mappedRepresentation(),
                    annotationSymbol.mappingSource().mappedOrigin(),
                    annotationSymbol.mappingTarget(),
                    sourceType,
                    sourceStepId,
                    edges,
                    resolved,
                    builder
            );
            return true;
        }
        if (item instanceof StepAnnotationText annotationText) {
            collectMappedAnnotationEdges(
                    mappedOwnerId,
                    annotationText.mappingSource().mappedRepresentation(),
                    annotationText.mappingSource().mappedOrigin(),
                    annotationText.mappingTarget(),
                    sourceType,
                    sourceStepId,
                    edges,
                    resolved,
                    builder
            );
            return true;
        }
        if (item instanceof StepAnnotationTextCharacter annotationTextCharacter) {
            collectMappedAnnotationEdges(
                    mappedOwnerId,
                    annotationTextCharacter.mappingSource().mappedRepresentation(),
                    annotationTextCharacter.mappingSource().mappedOrigin(),
                    annotationTextCharacter.mappingTarget(),
                    sourceType,
                    sourceStepId,
                    edges,
                    resolved,
                    builder
            );
            return true;
        }
        if (item instanceof StepAnnotationSymbolOccurrence symbolOccurrence) {
            return collectMappedAnnotationCarrierEdges(
                    mappedOwnerId,
                    sourceType,
                    sourceStepId,
                    symbolOccurrence.item(),
                    edges,
                    resolved,
                    builder
            );
        }
        if (item instanceof StepAnnotationSubfigureOccurrence subfigureOccurrence) {
            return collectMappedAnnotationCarrierEdges(
                    mappedOwnerId,
                    sourceType,
                    sourceStepId,
                    subfigureOccurrence.item(),
                    edges,
                    resolved,
                    builder
            );
        }
        return false;
    }

    private static double[] matrixForMappedPlacement(
            StepEntity mappedOrigin,
            StepEntity mappingTarget,
            StepCadBuilder builder
    ) {
        double[] sourceMatrix = matrixForPlacementEntity(mappedOrigin, builder);
        double[] targetMatrix = matrixForPlacementEntity(mappingTarget, builder);
        if (sourceMatrix == null || targetMatrix == null) {
            return null;
        }
        return StepAssemblyGraphBuilder.multiplyMatrices(
                targetMatrix,
                StepAssemblyGraphBuilder.inverseRigidTransform(sourceMatrix)
        );
    }

    private static double[] matrixForPlacementEntity(StepEntity placement, StepCadBuilder builder) {
        if (placement instanceof StepAxis2Placement3D placement3D) {
            return StepAssemblyGraphBuilder.matrixForPlacement(placement3D);
        }
        if (placement instanceof StepAxis2Placement2D placement2D) {
            CartesianPoint origin = pointFromPlacement(placement2D);
            if (origin == null) {
                return null;
            }
            Vector3 x;
            if (placement2D.refDirection() == null) {
                x = new Vector3(1.0, 0.0, 0.0);
            } else {
                List<Double> ratios = placement2D.refDirection().directionRatios();
                x = new Vector3(ratios.get(0), ratios.get(1), 0.0).normalize().asVector();
            }
            Vector3 z = new Vector3(0.0, 0.0, 1.0);
            Vector3 y = z.cross(x).normalize().asVector();
            return new double[]{
                    x.x(), y.x(), z.x(), origin.x(),
                    x.y(), y.y(), z.y(), origin.y(),
                    x.z(), y.z(), z.z(), origin.z(),
                    0.0, 0.0, 0.0, 1.0
            };
        }
        return null;
    }

    private static PreviewFaceResult buildPreviewFaceResult(
            StepFaceEntity stepFace,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        if (stepFace instanceof StepOrientedFace orientedFace) {
            PreviewFaceResult base = buildPreviewFaceResult(orientedFace.faceElement(), builder, metadata);
            if (base.face() == null) {
                return new PreviewFaceResult(
                        null,
                        toUnsupportedFacePayload(stepFace, base.unsupportedFace() == null ? null : base.unsupportedFace().reason())
                );
            }
            if (orientedFace.orientation()) {
                return new PreviewFaceResult(base.face(), null);
            }
            FacePayload reversed = reverseFacePayload(base.face());
            logPreviewFacePayload("face_payload_built", reversed);
            return new PreviewFaceResult(reversed, null);
        }

        StepEntity geometry = faceGeometry(stepFace);
        StepEntity previewGeometry = unwrapParametricPreviewSurface(geometry);
        if (previewGeometry instanceof StepPlane) {
            try {
                PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
                if (trimmed.face() != null) {
                    logPreviewFacePayload("face_payload_built", trimmed.face());
                    return trimmed;
                }
                if (geometry instanceof StepPlane) {
                    FacePayload payload = facePayloadFromTopologyFace(stepFace.id(), builder.buildFace(stepFace.id()), faceDisplayName(stepFace), metadata);
                    logPreviewFacePayload("face_payload_built", payload);
                    return new PreviewFaceResult(payload, null);
                }
                return trimmed;
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                String reason = ex.getMessage();
                if (reason != null && !reason.isBlank() && reason.contains("POLY_LOOP")) {
                    return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, reason));
                }
                return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "planar face build failed"));
            }
        }
        if (previewGeometry instanceof StepCylindricalSurface cylindricalSurface) {
            try {
                if (geometry instanceof StepCylindricalSurface) {
                    FacePayload payload = toCylindricalFacePayload(stepFace, cylindricalSurface, builder, metadata);
                    if (payload != null) {
                        logPreviewFacePayload("face_payload_built", payload);
                        return new PreviewFaceResult(payload, null);
                    }
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
            }
        }
        if (previewGeometry instanceof StepConicalSurface conicalSurface) {
            try {
                if (geometry instanceof StepConicalSurface) {
                    FacePayload payload = toConicalFacePayload(stepFace, conicalSurface, builder, metadata);
                    if (payload != null) {
                        logPreviewFacePayload("face_payload_built", payload);
                        return new PreviewFaceResult(payload, null);
                    }
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
            }
        }
        if (previewGeometry instanceof StepSphericalSurface) {
            PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
            if (trimmed.face() != null) {
                logPreviewFacePayload("face_payload_built", trimmed.face());
            }
            return trimmed;
        }
        if (previewGeometry instanceof StepRationalBSplineSurface splineSurface) {
            try {
                PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
                if (trimmed.face() != null || trimmed.unsupportedFace() != null) {
                    if (trimmed.face() != null) {
                        logPreviewFacePayload("face_payload_built", trimmed.face());
                    }
                    return trimmed;
                }
                FacePayload payload = toRationalBSplineSurfaceFacePayload(stepFace, splineSurface, builder, metadata);
                if (payload != null) {
                    logPreviewFacePayload("face_payload_built", payload);
                    return new PreviewFaceResult(payload, null);
                }
                return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "rational b-spline surface patch preview failed"));
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                log.debug("stage={} faceId={}, surfaceId={}, reason={}", "rational_bspline_surface_preview_exception",
                        stepFace.id(), splineSurface.id(), ex.getMessage());
                return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "rational b-spline surface preview failed"));
            }
        }
        if (previewGeometry instanceof StepBSplineSurfaceWithKnots
                || previewGeometry instanceof StepBSplineSurface
                || previewGeometry instanceof StepBezierSurface
                || previewGeometry instanceof StepUniformSurface
                || previewGeometry instanceof StepQuasiUniformSurface
                || previewGeometry instanceof StepPiecewiseBezierSurface) {
            try {
                PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
                if (trimmed.face() != null || trimmed.unsupportedFace() != null) {
                    if (trimmed.face() != null) {
                        logPreviewFacePayload("face_payload_built", trimmed.face());
                    }
                    return trimmed;
                }
                FacePayload payload = toBSplineSurfaceFacePayload(stepFace, previewGeometry, builder, metadata);
                if (payload != null) {
                    logPreviewFacePayload("face_payload_built", payload);
                    return new PreviewFaceResult(payload, null);
                }
                return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "b-spline surface patch preview failed"));
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                log.debug("stage={} faceId={}, surfaceId={}, reason={}", "bspline_surface_preview_exception",
                        stepFace.id(), previewGeometry.id(), ex.getMessage());
                return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "b-spline surface preview failed"));
            }
        }
        if (previewGeometry instanceof StepSurfaceOfLinearExtrusion || previewGeometry instanceof StepSurfaceOfRevolution) {
            PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
            if (trimmed.face() != null) {
                logPreviewFacePayload("face_payload_built", trimmed.face());
            }
            return trimmed;
        }
        if (previewGeometry instanceof StepDegenerateToroidalSurface) {
            PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
            if (trimmed.face() != null) {
                logPreviewFacePayload("face_payload_built", trimmed.face());
            }
            return trimmed;
        }
        if (previewGeometry instanceof StepToroidalSurface toroidalSurface) {
            try {
                if (geometry instanceof StepToroidalSurface) {
                    FacePayload payload = toToroidalFacePayload(stepFace, toroidalSurface, builder, metadata);
                    if (payload != null) {
                        logPreviewFacePayload("face_payload_built", payload);
                        return new PreviewFaceResult(payload, null);
                    }
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
            }
        }
        if (previewGeometry instanceof StepCylindricalSurface
                || previewGeometry instanceof StepConicalSurface
                || previewGeometry instanceof StepDegenerateToroidalSurface
                || previewGeometry instanceof StepToroidalSurface) {
            PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
            if (trimmed.face() != null) {
                logPreviewFacePayload("face_payload_built", trimmed.face());
            }
            return trimmed;
        }
        if (previewGeometry instanceof StepCylindricalSurfaceWithEllipticalAxis
                || previewGeometry instanceof StepConicalSurfaceWithEllipticalAxis
                || previewGeometry instanceof StepSphericalSurfaceWithEllipticalAxis
                || previewGeometry instanceof StepToroidalSurfaceWithCylindricalAxis
                || previewGeometry instanceof StepToroidalSurfaceWithEllipticalAxis) {
            PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
            if (trimmed.face() != null) {
                logPreviewFacePayload("face_payload_built", trimmed.face());
            }
            return trimmed;
        }
        if (previewGeometry instanceof StepBSplineSurfaceWithKnotsAndBreakpoints) {
            PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
            if (trimmed.face() != null) {
                logPreviewFacePayload("face_payload_built", trimmed.face());
            }
            return trimmed;
        }
        if (previewGeometry instanceof StepFreeFormSurface) {
            PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
            if (trimmed.face() != null) {
                logPreviewFacePayload("face_payload_built", trimmed.face());
            }
            return trimmed;
        }
        if (previewGeometry instanceof StepRuledSurface ruledSurface) {
            try {
                FacePayload payload = toRuledSurfaceFacePayload(stepFace, ruledSurface, builder, metadata);
                if (payload != null) {
                    logPreviewFacePayload("face_payload_built", payload);
                    return new PreviewFaceResult(payload, null);
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
            }
            return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "ruled surface preview failed"));
        }
        if (previewGeometry instanceof StepSurfaceOfConstantRadius surfaceOfConstantRadius) {
            try {
                FacePayload payload = toSurfaceOfConstantRadiusFacePayload(stepFace, surfaceOfConstantRadius, builder, metadata);
                if (payload != null) {
                    logPreviewFacePayload("face_payload_built", payload);
                    return new PreviewFaceResult(payload, null);
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
            }
            return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "surface of constant radius preview failed"));
        }
        if (previewGeometry instanceof StepBlendedSurface blended) {
            // Blended surface: approximate by rendering the primary surface with blend radius as metadata
            try {
                PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, blended.primarySurface(), metadata, builder);
                if (trimmed.face() != null) {
                    logPreviewFacePayload("face_payload_built", trimmed.face());
                    return trimmed;
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
            }
            return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "blended surface preview failed"));
        }
        // Free-form surfaces: try parametric mapping, fall back to sampled tessellation
        if (previewGeometry instanceof StepFreeFormSurface freeForm) {
            try {
                PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
                if (trimmed.face() != null) {
                    logPreviewFacePayload("face_payload_built", trimmed.face());
                    return trimmed;
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
            }
            // Fallback: tessellate via sampled grid if parametric mapping fails
            try {
                List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
                if (!bounds.isEmpty()) {
                    BSplineSurface3 surface = buildFreeFormSurface(freeForm, builder);
                    FacePayload payload = toSampledSurfaceFacePayload(stepFace, surface, "FREE_FORM_SURFACE", bounds, metadata);
                    if (payload != null) {
                        logPreviewFacePayload("face_payload_built", payload);
                        return new PreviewFaceResult(payload, null);
                    }
                }
            } catch (Exception ex) {
            }
            return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "free-form surface preview failed"));
        }
        // Machined surface: delegate to the underlying face geometry
        if (previewGeometry instanceof StepMachinedSurface machinedSurface) {
            return buildPreviewFaceResult((StepFaceEntity) machinedSurface.face(), builder, metadata);
        }
        String unsupportedSurface = describeUnsupportedPreviewSurface(geometry, builder);
        String reason = unsupportedSurface == null
                ? "surface type not previewable"
                : unsupportedSurface + " preview is unsupported";
        return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, reason));
    }

    private static StepEntity unwrapParametricPreviewSurface(StepEntity geometry) {
        StepEntity current = geometry;
        for (int depth = 0; depth < 16 && current != null; depth++) {
            if (current instanceof StepRectangularTrimmedSurface trimmedSurface) {
                current = trimmedSurface.basisSurface();
                continue;
            }
            if (current instanceof StepCurveBoundedSurface boundedSurface) {
                current = boundedSurface.basisSurface();
                continue;
            }
            if (current instanceof StepOrientedSurface orientedSurface) {
                current = orientedSurface.surfaceElement();
                continue;
            }
            if (current instanceof StepOffsetSurface offsetSurface) {
                current = offsetSurface.basisSurface();
                continue;
            }
            if (current instanceof StepOffsetSurface2 offsetSurface2) {
                current = offsetSurface2.basisSurface();
                continue;
            }
            if (current instanceof StepSurfacePatch surfacePatch) {
                current = surfacePatch.basisSurface();
                continue;
            }
            if (current instanceof StepRectangularCompositeSurface compositeSurface) {
                current = compositeSurface.parentSurface();
                continue;
            }
            if (current instanceof StepMachinedSurface machinedSurface) {
                current = machinedSurface.face();
                continue;
            }
            if (current instanceof StepBlendedSurface blended) {
                current = blended.primarySurface();
                continue;
            }
            if (current instanceof StepMappedItem mappedItem) {
                current = mappedItem.mappingTarget();
                continue;
            }
            if (current instanceof StepGeometricReplica replica && "SURFACE_REPLICA".equals(replica.entityName())) {
                current = replica.parent();
                continue;
            }
            return current;
        }
        return current;
    }

    private static String describeUnsupportedPreviewSurface(StepEntity surface) {
        return describeUnsupportedPreviewSurface(surface, null);
    }

    private static String describeUnsupportedPreviewSurface(StepEntity surface, StepCadBuilder builder) {
        if (surface == null) {
            return null;
        }
        if (surface instanceof StepRectangularTrimmedSurface trimmedSurface) {
            return describeUnsupportedPreviewSurface(trimmedSurface.basisSurface(), builder);
        }
        if (surface instanceof StepCurveBoundedSurface curveBoundedSurface) {
            return describeUnsupportedPreviewSurface(curveBoundedSurface.basisSurface(), builder);
        }
        if (surface instanceof StepOrientedSurface orientedSurface) {
            return describeUnsupportedPreviewSurface(orientedSurface.surfaceElement(), builder);
        }
        if (surface instanceof StepOffsetSurface offsetSurface) {
            return describeUnsupportedPreviewSurface(offsetSurface.basisSurface(), builder);
        }
        if (surface instanceof StepOffsetSurface2 offsetSurface2) {
            return describeUnsupportedPreviewSurface(offsetSurface2.basisSurface(), builder);
        }
        if (surface instanceof StepSurfacePatch surfacePatch) {
            return describeUnsupportedPreviewSurface(surfacePatch.basisSurface(), builder);
        }
        if (surface instanceof StepRectangularCompositeSurface compositeSurface) {
            return describeUnsupportedPreviewSurface(compositeSurface.parentSurface(), builder);
        }
        if (surface instanceof StepMachinedSurface machinedSurface) {
            return describeUnsupportedPreviewSurface(machinedSurface.face(), builder);
        }
        if (surface instanceof StepBlendedSurface blended) {
            return describeUnsupportedPreviewSurface(blended.primarySurface(), builder);
        }
        if (surface instanceof StepGeometricReplica replica && "SURFACE_REPLICA".equals(replica.entityName())) {
            if (replica.transformation() instanceof com.minicad.step.model.StepCartesianTransformationOperator transformation) {
                double scale = transformation.scale() == null ? 1.0 : transformation.scale();
                if (Math.abs(scale) <= 1.0e-9) {
                    return "SURFACE_REPLICA zero scale preview is unsupported";
                }
                if (builder != null) {
                    double[] matrix = matrixForTransformationOperator(transformation, builder);
                    if (inverseUniformScaleTransform(matrix) == null) {
                        return "SURFACE_REPLICA non-uniform scale preview is unsupported";
                    }
                }
            }
            return describeUnsupportedPreviewSurface(replica.parent(), builder);
        }
        return surfaceTypeName(surface);
    }

    private static void logPreviewFacePayload(String stage, FacePayload face) {
        int loopCount = face.loops() == null ? 0 : face.loops().size();
        int innerLoopCount = face.loops() == null ? 0 : (int) face.loops().stream().filter(loop -> !loop.outer()).count();
        int triangleCount = face.triangles() == null ? 0 : face.triangles().size() / 3;
        int uvLoopCount = face.uvLoops() == null ? 0 : face.uvLoops().size();
        String parametricType = face.surface() == null ? "none" : face.surface().type();
        log.info("stage={} faceId={}, surfaceType={}, parametricType={}, loopCount={}, innerLoopCount={}, triangleCount={}, uvLoopCount={}, sameSense={}",
                stage,
                face.stepId(),
                face.surfaceType(),
                parametricType,
                loopCount,
                innerLoopCount,
                triangleCount,
                uvLoopCount,
                face.sameSense());
    }

    private static AssemblyData buildAssemblyData(
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata
    ) {
        AssemblyGraph graph = StepAssemblyGraphBuilder.build(resolved);
        Map<Integer, RepresentationPayload> representations = new LinkedHashMap<>();
        List<UnsupportedFacePayload> unsupportedFaces = new ArrayList<>();
        for (AssemblyRepresentation assemblyRepresentation : graph.representations()) {
            StepEntity entity = resolved.get(assemblyRepresentation.representationId());
            if (entity instanceof StepRepresentation representation && representation.shapeRepresentation()) {
                RepresentationBuildResult result = buildRepresentationPayload(
                        representation,
                        assemblyRepresentation.name(),
                        resolved,
                        builder,
                        metadata
                );
                unsupportedFaces.addAll(result.unsupportedFaces());
                representations.put(representation.id(), result.payload());
            }
        }

        if (representations.isEmpty()) {
            for (StepEntity entity : resolved.values()) {
                if (entity instanceof StepRepresentation representation && representation.shapeRepresentation()) {
                    RepresentationBuildResult result = buildRepresentationPayload(
                            representation,
                            representation.name(),
                            resolved,
                            builder,
                            metadata
                    );
                    unsupportedFaces.addAll(result.unsupportedFaces());
                    representations.putIfAbsent(
                            representation.id(),
                            result.payload()
                    );
                }
            }
        }

        List<InstancePayload> instances = new ArrayList<>();
        for (AssemblyNode node : graph.nodes()) {
            instances.add(new InstancePayload(
                    node.id(),
                    node.parentId(),
                    node.productDefinitionId(),
                    node.occurrenceId(),
                    node.representationIds().isEmpty() ? null : node.representationIds().getFirst(),
                    node.representationIds(),
                    node.label(),
                    node.description(),
                    node.localMatrix(),
                    node.worldMatrix(),
                    node.depth()
                ));
        }

        List<RepresentationPayload> representationList = List.copyOf(representations.values());
        List<InstancePayload> instanceList = List.copyOf(instances);
        AssemblyMetrics metrics = measureAssembly(representationList, instanceList);
        return new AssemblyData(
                representationList,
                instanceList,
                List.copyOf(unsupportedFaces),
                metrics.summary(),
                metrics.bounds()
        );
    }

    private static RepresentationBuildResult buildRepresentationPayload(
            StepRepresentation representation,
            String displayName,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata
    ) {
        return buildRepresentationPayload(representation, displayName, resolved, builder, metadata, new LinkedHashSet<>());
    }

    private static RepresentationBuildResult buildRepresentationPayload(
            StepRepresentation representation,
            String displayName,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Set<Integer> visitingRepresentations
    ) {
        if (!visitingRepresentations.add(representation.id())) {
            return new RepresentationBuildResult(
                    new RepresentationPayload(
                            representation.id(),
                            displayName,
                            List.of(),
                            null,
                            List.of(),
                            List.of()
                    ),
                    List.of()
            );
        }
        Set<Integer> shellIds = collectRepresentationShells(representation, resolved);
        Set<Integer> solidIds = collectRepresentationSolids(representation, resolved);
        StepMetadataExtractor.DisplayMetadata representationMetadata = metadata.forItem(representation.id());
        GeometryCollection shellGeometry = buildGeometryForShells(
                shellIds,
                resolved,
                builder,
                metadata,
                collectInheritedShellMetadata(representation, metadata, resolved)
        );
        GeometryCollection solidGeometry = buildGeometryForSolids(
                solidIds,
                resolved,
                builder,
                metadata,
                collectInheritedSolidMetadata(representation, metadata, resolved)
        );
        GeometryCollection mappedGeometry = buildMappedRepresentationGeometry(
                representation,
                resolved,
                builder,
                metadata,
                visitingRepresentations
        );
        GeometryCollection relatedGeometry = buildRelatedRepresentationGeometry(
                representation,
                resolved,
                builder,
                metadata,
                visitingRepresentations
        );
        GeometryCollection geometry = mergeGeometry(
                mergeGeometry(mergeGeometry(shellGeometry, solidGeometry), mappedGeometry),
                relatedGeometry
        );
        List<EdgePayload> representationEdges = new ArrayList<>(geometry.edges());
        representationEdges.addAll(collectRepresentationLooseEdges(representation, resolved, builder));
        RepresentationBuildResult result = new RepresentationBuildResult(
                new RepresentationPayload(
                        representation.id(),
                        displayName,
                        representationMetadata.layers(),
                        toColorPayload(representationMetadata.rgb()),
                        List.copyOf(representationEdges),
                        geometry.faces()
                ),
                geometry.unsupportedFaces()
        );
        visitingRepresentations.remove(representation.id());
        return result;
    }

    private static GeometryCollection buildMappedRepresentationGeometry(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Set<Integer> visitingRepresentations
    ) {
        GeometryCollection geometry = new GeometryCollection(List.of(), List.of(), List.of());
        for (StepRepresentation candidate : linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                if (item instanceof StepMappedItem mappedItem) {
                    geometry = mergeGeometry(
                            geometry,
                            expandMappedItemGeometry(mappedItem, resolved, builder, metadata, visitingRepresentations)
                    );
                }
            }
        }
        return geometry;
    }

    private static GeometryCollection buildRelatedRepresentationGeometry(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Set<Integer> visitingRepresentations
    ) {
        GeometryCollection geometry = new GeometryCollection(List.of(), List.of(), List.of());
        for (StepEntity entity : resolved.values()) {
            if (!(entity instanceof StepRepresentationRelationshipWithTransformation relationship)) {
                continue;
            }
            if (!relationship.rep1().shapeRepresentation()
                    || !relationship.rep2().shapeRepresentation()
                    || relationship.rep2().id() != representation.id()) {
                continue;
            }
            double[] matrix = StepAssemblyGraphBuilder.matrixFor(relationship.transformationOperator());
            RepresentationBuildResult source = buildRepresentationPayload(
                    relationship.rep1(),
                    relationship.rep1().name(),
                    resolved,
                    builder,
                    metadata,
                    visitingRepresentations
            );
            StepMetadataExtractor.DisplayMetadata relationshipMetadata = metadata.forItem(relationship.id());
            List<EdgePayload> edges = source.payload().edges().stream()
                    .map(edge -> transformMappedEdge(edge, relationship.id(), matrix))
                    .toList();
            List<FacePayload> faces = source.payload().faces().stream()
                    .map(face -> transformMappedFace(face, relationship.id(), matrix, relationshipMetadata))
                    .toList();
            geometry = mergeGeometry(geometry, new GeometryCollection(edges, faces, source.unsupportedFaces()));
        }
        return geometry;
    }

    private static GeometryCollection expandMappedItemGeometry(
            StepMappedItem mappedItem,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Set<Integer> visitingRepresentations
    ) {
        double[] matrix = mappedItemMatrix(mappedItem, builder);
        if (matrix == null) {
            return new GeometryCollection(List.of(), List.of(), List.of());
        }
        StepRepresentationMap mappingSource = mappedItem.mappingSource();
        RepresentationBuildResult source = buildRepresentationPayload(
                mappingSource.mappedRepresentation(),
                mappingSource.mappedRepresentation().name(),
                resolved,
                builder,
                metadata,
                visitingRepresentations
        );
        StepMetadataExtractor.DisplayMetadata itemMetadata = metadata.forItem(mappedItem.id());
        List<EdgePayload> edges = source.payload().edges().stream()
                .map(edge -> transformMappedEdge(edge, mappedItem.id(), matrix))
                .toList();
        List<FacePayload> faces = source.payload().faces().stream()
                .map(face -> transformMappedFace(face, mappedItem.id(), matrix, itemMetadata))
                .toList();
        return new GeometryCollection(edges, faces, source.unsupportedFaces());
    }

    private static Set<Integer> collectRepresentationShells(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved
    ) {
        Set<Integer> shellIds = new TreeSet<>();
        for (StepRepresentation candidate : linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                StepEntity unwrapped = unwrapStyledItem(item);
                if (!isRepresentationSolidItem(unwrapped)) {
                    collectShellLikeIds(item, shellIds);
                }
            }
        }
        return shellIds;
    }

    private static Set<Integer> collectRepresentationSolids(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved
    ) {
        Set<Integer> solidIds = new TreeSet<>();
        for (StepRepresentation candidate : linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                StepEntity unwrapped = unwrapStyledItem(item);
                if (isRepresentationSolidItem(unwrapped)) {
                    solidIds.add(unwrapped.id());
                }
            }
        }
        return solidIds;
    }

    private static List<EdgePayload> collectRepresentationLooseEdges(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder
    ) {
        Map<Integer, EdgePayload> edges = new LinkedHashMap<>();
        for (StepRepresentation candidate : linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                collectStandaloneEdges(item, edges, resolved, builder);
            }
        }
        return List.copyOf(edges.values());
    }

    private static Map<Integer, StepMetadataExtractor.DisplayMetadata> collectInheritedShellMetadata(
            StepRepresentation representation,
            StepMetadataExtractor metadata,
            Map<Integer, StepEntity> resolved
    ) {
        Map<Integer, StepMetadataExtractor.DisplayMetadata> metadataByShellId = new LinkedHashMap<>();
        for (StepRepresentation candidate : linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                StepEntity unwrapped = unwrapStyledItem(item);
                if (isRepresentationSolidItem(unwrapped)) {
                    continue;
                }
                StepMetadataExtractor.DisplayMetadata itemMetadata = metadata.forItem(item.id());
                Set<Integer> itemShellIds = new LinkedHashSet<>();
                collectShellLikeIds(item, itemShellIds);
                for (Integer shellId : itemShellIds) {
                    metadataByShellId.put(shellId, mergeMetadata(metadataByShellId.get(shellId), itemMetadata));
                }
            }
        }
        return Map.copyOf(metadataByShellId);
    }

    private static Map<Integer, StepMetadataExtractor.DisplayMetadata> collectInheritedSolidMetadata(
            StepRepresentation representation,
            StepMetadataExtractor metadata,
            Map<Integer, StepEntity> resolved
    ) {
        Map<Integer, StepMetadataExtractor.DisplayMetadata> metadataBySolidId = new LinkedHashMap<>();
        for (StepRepresentation candidate : linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                StepEntity unwrapped = unwrapStyledItem(item);
                if (isRepresentationSolidItem(unwrapped)) {
                    StepMetadataExtractor.DisplayMetadata itemMetadata = metadata.forItem(item.id());
                    metadataBySolidId.put(unwrapped.id(), mergeMetadata(metadataBySolidId.get(unwrapped.id()), itemMetadata));
                }
            }
        }
        return Map.copyOf(metadataBySolidId);
    }

    private static boolean isRepresentationSolidItem(StepEntity entity) {
        return entity instanceof StepManifoldSolidBrep
                || entity instanceof StepFacettedBrep
                || entity instanceof StepNonManifoldSolidBrep
                || entity instanceof StepAdvancedBrep
                || entity instanceof StepBrepWithVoids
                || entity instanceof StepSweptAreaSolid
                || entity instanceof StepSolidReplica
                || entity instanceof StepCsgSolid
                || entity instanceof StepCsgPrimitive
                || entity instanceof StepBooleanClippingResult
                || entity instanceof StepBooleanResult
                || entity instanceof StepTessellatedFaceSet
                || entity instanceof StepTessellatedFace
                || entity instanceof StepSweptDiskSolid
                || entity instanceof StepExtrudedAreaSolidTapered
                || entity instanceof StepRevolvedAreaSolidTapered
                || entity instanceof StepSurfaceCurveSweptAreaSolid
                || entity instanceof StepPolygonalBoundedHalfSpace
                || entity instanceof StepComplexClippingResult
                || entity instanceof StepHalfSpaceSolid
                || entity instanceof StepCsgVolume
                || entity instanceof StepBlockVolume
                || entity instanceof StepFiniteElementMesh
                || entity instanceof StepFlatPattern
                || entity instanceof StepMappedItem
                || entity instanceof StepSolidModel
                || entity instanceof StepSurfacePatch;
    }

    private static List<StepRepresentation> linkedShapeRepresentations(
            StepRepresentation seed,
            Map<Integer, StepEntity> resolved
    ) {
        List<StepRepresentation> ordered = new ArrayList<>();
        Set<Integer> visited = new LinkedHashSet<>();
        collectLinkedShapeRepresentations(seed, resolved, visited, ordered);
        return List.copyOf(ordered);
    }

    private static void collectLinkedShapeRepresentations(
            StepRepresentation current,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visited,
            List<StepRepresentation> ordered
    ) {
        if (!visited.add(current.id())) {
            return;
        }
        ordered.add(current);
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepShapeRepresentationRelationship relationship) {
                if (!relationship.rep1().shapeRepresentation() || !relationship.rep2().shapeRepresentation()) {
                    continue;
                }
                if (relationship.rep1().id() == current.id()) {
                    collectLinkedShapeRepresentations(relationship.rep2(), resolved, visited, ordered);
                } else if (relationship.rep2().id() == current.id()) {
                    collectLinkedShapeRepresentations(relationship.rep1(), resolved, visited, ordered);
                }
                continue;
            }
            if (entity instanceof StepRepresentationRelationship relationship) {
                if (!relationship.rep1().shapeRepresentation() || !relationship.rep2().shapeRepresentation()) {
                    continue;
                }
                if (relationship.rep1().id() == current.id()) {
                    collectLinkedShapeRepresentations(relationship.rep2(), resolved, visited, ordered);
                } else if (relationship.rep2().id() == current.id()) {
                    collectLinkedShapeRepresentations(relationship.rep1(), resolved, visited, ordered);
                }
            }
        }
    }

    private static StepMetadataExtractor.DisplayMetadata mergeMetadata(
            StepMetadataExtractor.DisplayMetadata inherited,
            StepMetadataExtractor.DisplayMetadata direct
    ) {
        StepMetadataExtractor.DisplayMetadata left = inherited == null ? StepMetadataExtractor.DisplayMetadata.EMPTY : inherited;
        StepMetadataExtractor.DisplayMetadata right = direct == null ? StepMetadataExtractor.DisplayMetadata.EMPTY : direct;
        int[] rgb = right.rgb() != null ? right.rgb() : left.rgb();
        Set<String> layers = new LinkedHashSet<>(left.layers());
        layers.addAll(right.layers());
        double transparency = right.transparency() > 0 ? right.transparency() : left.transparency();
        StepMetadataExtractor.PbrMetadata pbr = right.pbr() != null ? right.pbr() : left.pbr();
        return new StepMetadataExtractor.DisplayMetadata(rgb, List.copyOf(layers), transparency, pbr);
    }

    private static String faceDisplayName(StepFaceEntity stepFace) {
        if (stepFace instanceof StepOrientedFace orientedFace) {
            return faceDisplayName(orientedFace.faceElement());
        }
        return stepFace.name();
    }

    private static UnsupportedFacePayload toUnsupportedFacePayload(StepFaceEntity stepFace, String reason) {
        StepEntity geometry = faceGeometry(stepFace);
        return new UnsupportedFacePayload(
                stepFace.id(),
                faceDisplayName(stepFace),
                surfaceTypeName(geometry),
                reason == null ? "preview export returned no mesh" : reason
        );
    }

    private static ColorPayload toColorPayload(int[] rgb) {
        if (rgb == null) {
            return null;
        }
        return new ColorPayload(rgb[0], rgb[1], rgb[2]);
    }

    private static PbrPayload toPbrPayload(StepMetadataExtractor.PbrMetadata metadata) {
        if (metadata == null) {
            return null;
        }
        return new PbrPayload(metadata.diffuse(), metadata.specular(), metadata.specularExponent(), metadata.specularColor());
    }

    private static int countEntities(Map<Integer, StepEntity> resolved, Class<? extends StepEntity> type) {
        int count = 0;
        for (StepEntity entity : resolved.values()) {
            if (type.isInstance(entity)) {
                count++;
            }
        }
        return count;
    }

    private static int countSolidEntities(Map<Integer, StepEntity> resolved) {
        int count = 0;
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepManifoldSolidBrep
                    || entity instanceof StepBrepWithVoids
                    || entity instanceof StepSweptAreaSolid
                    || entity instanceof StepSolidReplica
                    || entity instanceof StepCsgSolid
                    || entity instanceof StepCsgPrimitive
                    || entity instanceof StepBooleanClippingResult
                    || entity instanceof StepBooleanResult
                    || entity instanceof StepSweptDiskSolid
                    || entity instanceof StepExtrudedAreaSolidTapered
                    || entity instanceof StepRevolvedAreaSolidTapered
                    || entity instanceof StepSurfaceCurveSweptAreaSolid
                    || entity instanceof StepPolygonalBoundedHalfSpace
                    || entity instanceof StepComplexClippingResult
                    || entity instanceof StepHalfSpaceSolid
                    || entity instanceof StepCsgVolume
                    || entity instanceof StepBlockVolume) {
                count++;
            }
        }
        return count;
    }

    private static String summarizeUnsupportedFacesBySurfaceType(List<UnsupportedFacePayload> unsupportedFaces) {
        Map<String, Long> counts = unsupportedFaces.stream()
                .collect(Collectors.groupingBy(
                        face -> face.surfaceType() == null ? "UNKNOWN" : face.surfaceType(),
                        LinkedHashMap::new,
                        Collectors.counting()
                ));
        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining("|"));
    }

    private static String summarizeUnsupportedFacesByReason(List<UnsupportedFacePayload> unsupportedFaces) {
        Map<String, Long> counts = unsupportedFaces.stream()
                .collect(Collectors.groupingBy(
                        face -> face.reason() == null ? "unknown" : face.reason(),
                        LinkedHashMap::new,
                        Collectors.counting()
                ));
        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining("|"));
    }

    private static String summarizeUnsupportedBooleansByType(List<UnsupportedBooleanPayload> unsupportedBooleans) {
        Map<String, Long> counts = unsupportedBooleans.stream()
                .collect(Collectors.groupingBy(
                        UnsupportedBooleanPayload::type,
                        LinkedHashMap::new,
                        Collectors.counting()
                ));
        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining("|"));
    }

    private static String summarizeUnsupportedBooleansByReason(List<UnsupportedBooleanPayload> unsupportedBooleans) {
        Map<String, Long> counts = unsupportedBooleans.stream()
                .collect(Collectors.groupingBy(
                        item -> item.reason() == null ? "unknown" : item.reason(),
                        LinkedHashMap::new,
                        Collectors.counting()
                ));
        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining("|"));
    }

    private static String summarizeLoopPointCounts(List<ParametricLoopPayload> loops) {
        return loops.stream()
                .map(loop -> (loop.outer() ? "outer" : "inner") + ":" + loop.points().size())
                .collect(Collectors.joining("|"));
    }

    private static String formatUvBounds(UvBounds bounds) {
        return String.format(
                "(minU=%.6f,minV=%.6f,maxU=%.6f,maxV=%.6f,uSpan=%.6f,vSpan=%.6f)",
                bounds.minU(),
                bounds.minV(),
                bounds.maxU(),
                bounds.maxV(),
                bounds.uSpan(),
                bounds.vSpan()
        );
    }

    private static int countShells(Map<Integer, StepEntity> resolved) {
        int count = 0;
        for (StepEntity entity : resolved.values()) {
            if (isShellLikeEntity(entity)) {
                count++;
            }
        }
        return count;
    }

    private static int countUnsupportedFaces(Map<Integer, StepEntity> resolved, StepCadBuilder builder) {
        long startedAt = System.nanoTime();
        int unsupported = 0;
        int processed = 0;
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepFaceEntity stepFace
                    && buildPreviewFaceResult(stepFace, builder, StepMetadataExtractor.DisplayMetadata.EMPTY).face() == null) {
                unsupported++;
            }
            if (entity instanceof StepFaceEntity) {
                processed++;
                if (processed % FACE_PROGRESS_INTERVAL == 0) {
                    log.debug("stage={} processedFaces={}, unsupportedFaces={}", "count_unsupported_faces_progress",
                            processed, unsupported);
                }
            }
        }
        log.debug("stage={} elapsedMs={}, processedFaces={}, unsupportedFaces={}", "count_unsupported_faces_done",
                elapsedMillis(startedAt), processed, unsupported);
        return unsupported;
    }

    private static List<FacePayload> buildTessellatedFacePayloads(
            StepTessellatedFaceSet tessellated,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<StepCartesianPoint> coords = tessellated.coordinates();
        List<PointPayload> points = new ArrayList<>(coords.size());
        for (StepCartesianPoint cp : coords) {
            double cx = cp.coordinates().get(0);
            double cy = cp.coordinates().size() > 1 ? cp.coordinates().get(1) : 0.0;
            double cz = cp.coordinates().size() > 2 ? cp.coordinates().get(2) : 0.0;
            points.add(new PointPayload(cx, cy, cz));
        }
        List<FacePayload> faces = new ArrayList<>(tessellated.faceIndices().size());
        for (List<Integer> faceIndex : tessellated.faceIndices()) {
            if (faceIndex.size() < 3) continue;
            PointPayload p1 = points.get(faceIndex.get(0) - 1);
            PointPayload p2 = points.get(faceIndex.get(1) - 1);
            PointPayload p3 = points.get(faceIndex.get(2) - 1);
            VectorPayload normal = computeNormal(p1, p2, p3);
            if (normal == null) continue;
            List<PointPayload> triangle = List.of(p1, p2, p3);
            FacePayload face = new FacePayload(
                    tessellated.id(),
                    tessellated.name(),
                    "TESSELLATED_FACE_SET",
                    p1,
                    normal,
                    true,
                    toColorPayload(metadata.rgb()),
                    metadata.transparency(),
                    toPbrPayload(metadata.pbr()),
                    metadata.layers(),
                    List.of(new LoopPayload(true, triangle)),
                    triangle,
                    null,
                    null
            );
            faces.add(face);
        }
        return faces;
    }

    private static FacePayload buildTessellatedFacePayload(
            StepTessellatedFace tessellatedFace,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<PointPayload> allPoints = new ArrayList<>();
        for (StepEntity triangleRef : tessellatedFace.triangles()) {
            if (triangleRef instanceof StepTessellatedTriangle triangle) {
                PointPayload p1 = pointPayloadFromVertex(triangle.vertex1());
                PointPayload p2 = pointPayloadFromVertex(triangle.vertex2());
                PointPayload p3 = pointPayloadFromVertex(triangle.vertex3());
                if (p1 == null || p2 == null || p3 == null) continue;
                VectorPayload normal = computeNormal(p1, p2, p3);
                if (normal == null) continue;
                List<PointPayload> tri = List.of(p1, p2, p3);
                return new FacePayload(
                        tessellatedFace.id(),
                        tessellatedFace.name(),
                        "TESSELLATED_FACE",
                        p1,
                        normal,
                        true,
                        toColorPayload(metadata.rgb()),
                        metadata.transparency(),
                        toPbrPayload(metadata.pbr()),
                        metadata.layers(),
                        List.of(new LoopPayload(true, tri)),
                        tri,
                        null,
                        null
                );
            }
        }
        return null;
    }

    /**
     * Computes the face normal from three triangle vertices using the cross product.
     * Returns null if the normal is degenerate (near-zero length).
     */
    private static VectorPayload computeNormal(PointPayload p1, PointPayload p2, PointPayload p3) {
        double nx = (p2.y() - p1.y()) * (p3.z() - p1.z()) - (p2.z() - p1.z()) * (p3.y() - p1.y());
        double ny = (p2.z() - p1.z()) * (p3.x() - p1.x()) - (p2.x() - p1.x()) * (p3.z() - p1.z());
        double nz = (p2.x() - p1.x()) * (p3.y() - p1.y()) - (p2.y() - p1.y()) * (p3.x() - p1.x());
        double len = Math.sqrt(nx * nx + ny * ny + nz * nz);
        if (len < 1.0e-9) return null;
        return new VectorPayload(nx / len, ny / len, nz / len);
    }

    private static PointPayload pointPayloadFromVertex(StepEntity vertex) {
        if (vertex instanceof StepCartesianPoint cp) {
            double cx = cp.coordinates().get(0);
            double cy = cp.coordinates().size() > 1 ? cp.coordinates().get(1) : 0.0;
            double cz = cp.coordinates().size() > 2 ? cp.coordinates().get(2) : 0.0;
            return new PointPayload(cx, cy, cz);
        }
        return null;
    }

    private static List<StepFaceEntity> shellFaces(StepEntity entity) {
        if (entity instanceof StepOpenShell openShell) {
            return openShell.faces();
        }
        if (entity instanceof StepSurfacedOpenShell surfacedOpenShell) {
            return surfacedOpenShell.faces();
        }
        if (entity instanceof StepOrientedOpenShell orientedOpenShell) {
            return orientedOpenShell.faces();
        }
        if (entity instanceof StepClosedShell closedShell) {
            return closedShell.faces();
        }
        if (entity instanceof StepOrientedClosedShell orientedClosedShell) {
            return orientedClosedShell.faces();
        }
        if (entity instanceof StepConnectedFaceSet connectedFaceSet) {
            return connectedFaceSet.faces();
        }
        if (entity instanceof StepConnectedFaceSubSet connectedFaceSubSet) {
            return connectedFaceSubSet.faces();
        }
        throw new UnsupportedGeometryException(
                "preview export requires shell or connected face set geometry");
    }

    private static boolean isShellEntity(StepEntity entity) {
        return entity instanceof StepOpenShell
                || entity instanceof StepSurfacedOpenShell
                || entity instanceof StepOrientedOpenShell
                || entity instanceof StepClosedShell
                || entity instanceof StepOrientedClosedShell;
    }

    private static boolean isShellLikeEntity(StepEntity entity) {
        return isShellEntity(entity)
                || entity instanceof StepConnectedFaceSet
                || entity instanceof StepConnectedFaceSubSet
                || entity instanceof StepTessellatedFaceSet
                || entity instanceof StepTessellatedFace
                || entity instanceof StepGeometricSurfaceSet
                || entity instanceof StepPlanarBox
                || entity instanceof StepPlanarExtent
                || entity instanceof StepFiniteElementMesh
                || entity instanceof StepFlatPattern
                || entity instanceof StepSurfacePatch;
    }

    private static FacePayload facePayloadFromTopologyFace(
            int stepId,
            Face face,
            String name,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        SurfaceGeometry surface = face.surface();
        boolean sameSense = face.sameSense();
        if (surface instanceof Plane plane) {
            List<LoopPayload> loops = new ArrayList<>();
            for (FaceBound bound : face.bounds()) {
                loops.add(new LoopPayload(bound.outer(), toPointPayloads(sampleLoop(bound))));
            }
            Direction3 normal = plane.normal();
            if (!sameSense) {
                normal = normal.reverse();
            }
            return new FacePayload(
                    stepId,
                    name,
                    "PLANE",
                    toPointPayload(plane.origin()),
                    new VectorPayload(normal.x(), normal.y(), normal.z()),
                    sameSense,
                    toColorPayload(metadata.rgb()),
                    metadata.transparency(),
                    toPbrPayload(metadata.pbr()),
                    metadata.layers(),
                    loops,
                    List.of(),
                    new FaceSurfacePayload(
                            "plane_face",
                            List.of(plane.origin().x(), plane.origin().y(), plane.origin().z()),
                            List.of(plane.normal().x(), plane.normal().y(), plane.normal().z()),
                            basisDirectionForNormal(normal),
                            0.0,
                            null, null, 0.0, 0.0, 0.0, 0.0,
                            null, null, null, null, null, null, null
                    ),
                    null
            );
        }
        // Paraboloid surface: parametric payload for viewer rebuild
        if (surface instanceof ParaboloidSurface paraboloid) {
            Axis2Placement3D pos = paraboloid.position();
            Vector3 normal = surface.normalAt(0.5, 0.5);
            if (!sameSense) {
                normal = normal.scale(-1.0);
            }
            List<LoopPayload> loops = new ArrayList<>();
            for (FaceBound bound : face.bounds()) {
                loops.add(new LoopPayload(bound.outer(), toPointPayloads(sampleLoop(bound))));
            }
            java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(32, 32);
            List<PointPayload> triangles = triangulateSurfaceGrid(grid, sameSense);
            return new FacePayload(
                    stepId,
                    name,
                    "PARABOLOID_SURFACE",
                    new PointPayload(pos.location().x(), pos.location().y(), pos.location().z()),
                    new VectorPayload(normal.x(), normal.y(), normal.z()),
                    sameSense,
                    toColorPayload(metadata.rgb()),
                    metadata.transparency(),
                    toPbrPayload(metadata.pbr()),
                    metadata.layers(),
                    loops,
                    triangles,
                    new FaceSurfacePayload(
                            "paraboloid_surface",
                            List.of(pos.location().x(), pos.location().y(), pos.location().z()),
                            List.of(pos.axis().x(), pos.axis().y(), pos.axis().z()),
                            List.of(pos.xDirection().x(), pos.xDirection().y(), pos.xDirection().z()),
                            paraboloid.focalLength(),
                            null, null, 0.0, 0.0, 0.0, 0.0,
                            null, null, null, null, null, null, null
                    ),
                    null
            );
        }
        // Hyperboloid surface: parametric payload for viewer rebuild
        if (surface instanceof HyperboloidSurface hyperboloid) {
            Axis2Placement3D pos = hyperboloid.position();
            Vector3 normal = surface.normalAt(0.5, 0.5);
            if (!sameSense) {
                normal = normal.scale(-1.0);
            }
            List<LoopPayload> loops = new ArrayList<>();
            for (FaceBound bound : face.bounds()) {
                loops.add(new LoopPayload(bound.outer(), toPointPayloads(sampleLoop(bound))));
            }
            java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(32, 32);
            List<PointPayload> triangles = triangulateSurfaceGrid(grid, sameSense);
            return new FacePayload(
                    stepId,
                    name,
                    "HYPERBOLOID_SURFACE",
                    new PointPayload(pos.location().x(), pos.location().y(), pos.location().z()),
                    new VectorPayload(normal.x(), normal.y(), normal.z()),
                    sameSense,
                    toColorPayload(metadata.rgb()),
                    metadata.transparency(),
                    toPbrPayload(metadata.pbr()),
                    metadata.layers(),
                    loops,
                    triangles,
                    new FaceSurfacePayload(
                            "hyperboloid_surface",
                            List.of(pos.location().x(), pos.location().y(), pos.location().z()),
                            List.of(pos.axis().x(), pos.axis().y(), pos.axis().z()),
                            List.of(pos.xDirection().x(), pos.xDirection().y(), pos.xDirection().z()),
                            hyperboloid.radius(),
                            null, hyperboloid.semiAxis(), 0.0, 0.0, 0.0, 0.0,
                            null, null, null, null, null, null, null
                    ),
                    null
            );
        }
        // Surface of translation: parametric payload for viewer rebuild
        if (surface instanceof SurfaceOfTranslation3 translation) {
            Vector3 dir = translation.direction();
            Vector3 normal = surface.normalAt(0.5, 0.5);
            if (!sameSense) {
                normal = normal.scale(-1.0);
            }
            List<LoopPayload> loops = new ArrayList<>();
            for (FaceBound bound : face.bounds()) {
                loops.add(new LoopPayload(bound.outer(), toPointPayloads(sampleLoop(bound))));
            }
            java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(32, 32);
            List<PointPayload> triangles = triangulateSurfaceGrid(grid, sameSense);
            return new FacePayload(
                    stepId,
                    name,
                    "SURFACE_OF_TRANSLATION",
                    new PointPayload(triangles.get(0).x(), triangles.get(0).y(), triangles.get(0).z()),
                    new VectorPayload(normal.x(), normal.y(), normal.z()),
                    sameSense,
                    toColorPayload(metadata.rgb()),
                    metadata.transparency(),
                    toPbrPayload(metadata.pbr()),
                    metadata.layers(),
                    loops,
                    triangles,
                    new FaceSurfacePayload(
                            "surface_of_translation",
                            null,
                            List.of(dir.x(), dir.y(), dir.z()),
                            null,
                            0.0,
                            null, null, 0.0, 0.0, 0.0, 0.0,
                            null, null, null, null, null, null, null
                    ),
                    null
            );
        }
        // Surface of projection: parametric payload for viewer rebuild
        if (surface instanceof SurfaceOfProjection3 projection) {
            Vector3 dir = projection.projectionDirection();
            Vector3 normal = surface.normalAt(0.5, 0.5);
            if (!sameSense) {
                normal = normal.scale(-1.0);
            }
            List<LoopPayload> loops = new ArrayList<>();
            for (FaceBound bound : face.bounds()) {
                loops.add(new LoopPayload(bound.outer(), toPointPayloads(sampleLoop(bound))));
            }
            java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(32, 32);
            List<PointPayload> triangles = triangulateSurfaceGrid(grid, sameSense);
            return new FacePayload(
                    stepId,
                    name,
                    "SURFACE_OF_PROJECTION",
                    new PointPayload(triangles.get(0).x(), triangles.get(0).y(), triangles.get(0).z()),
                    new VectorPayload(normal.x(), normal.y(), normal.z()),
                    sameSense,
                    toColorPayload(metadata.rgb()),
                    metadata.transparency(),
                    toPbrPayload(metadata.pbr()),
                    metadata.layers(),
                    loops,
                    triangles,
                    new FaceSurfacePayload(
                            "surface_of_projection",
                            null,
                            List.of(dir.x(), dir.y(), dir.z()),
                            null,
                            0.0,
                            null, null, 0.0, 0.0, 0.0, 0.0,
                            null, null, null, null, null, null, null
                    ),
                    null
            );
        }
        // Non-planar: grid-based triangulation
        int segments = 32;
        java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(segments, segments);
        if (grid.isEmpty()) {
            throw new UnsupportedGeometryException(surfaceTypeNameForGeometry(surface) + " produced no sample grid");
        }
        List<PointPayload> triangles = triangulateSurfaceGrid(grid, sameSense);
        if (triangles.isEmpty()) {
            throw new UnsupportedGeometryException(surfaceTypeNameForGeometry(surface) + " triangulation produced no cells");
        }
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) {
            normal = normal.scale(-1.0);
        }
        List<LoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : face.bounds()) {
            loops.add(new LoopPayload(bound.outer(), toPointPayloads(sampleLoop(bound))));
        }
        return new FacePayload(
                stepId,
                name,
                surfaceTypeNameForGeometry(surface),
                new PointPayload(triangles.get(0).x(), triangles.get(0).y(), triangles.get(0).z()),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops,
                triangles,
                null,
                null
        );
    }

    private static FacePayload toCylindricalFacePayload(
            StepFaceEntity stepFace,
            StepCylindricalSurface stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.getFirst().outer()) {
            return null;
        }

        if (!(bounds.getFirst().loop() instanceof EdgeLoop outerLoop)) {
            return null;
        }
        if (outerLoop.edges().size() != 4) {
            return null;
        }

        List<OrientedEdge> circleEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Circle)
                .toList();
        List<OrientedEdge> lineEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Line3)
                .toList();
        if (circleEdges.size() != 2 || lineEdges.size() != 2) {
            return null;
        }

        CylindricalSurface surface = builder.buildCylindricalSurface(stepSurface.id());
        OrientedEdge lowerArc = circleEdges.getFirst();
        OrientedEdge upperArc = circleEdges.getLast();
        if (averageAxialHeight(surface, sampleOrientedEdge(lowerArc)) > averageAxialHeight(surface, sampleOrientedEdge(upperArc))) {
            lowerArc = circleEdges.getLast();
            upperArc = circleEdges.getFirst();
        }

        List<CartesianPoint> lowerArcPoints = sampleOrientedEdge(lowerArc);
        List<CartesianPoint> upperArcPoints = sampleOrientedEdge(upperArc);
        double lowerHeight = averageAxialHeight(surface, lowerArcPoints);
        double upperHeight = averageAxialHeight(surface, upperArcPoints);
        if (Math.abs(upperHeight - lowerHeight) <= Epsilon.EPS) {
            return null;
        }

        List<Double> angles = unwrapAngles(surface, lowerArcPoints);
        if (angles.size() < 2) {
            return null;
        }

        boolean sameSense = faceSameSense(stepFace);
        List<PointPayload> triangles = triangulateCylindricalStrip(surface, lowerHeight, upperHeight, angles, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = cylindricalNormal(surface, angles.getFirst(), sameSense);
        return new FacePayload(
                stepFace.id(),
                faceDisplayName(stepFace),
                "CYLINDRICAL_SURFACE",
                toPointPayload(surfacePoint(surface, angles.getFirst(), lowerHeight)),
                new VectorPayload(startNormal.x(), startNormal.y(), startNormal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.getFirst())))),
                triangles,
                new FaceSurfacePayload(
                        "cylindrical_strip",
                        List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                        List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                        List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                        surface.radius(),
                        null,
                        null,
                        lowerHeight,
                        upperHeight,
                        angles.getFirst(),
                        angles.getLast() - angles.getFirst(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                ),
                null
        );
    }

    private static FacePayload toConicalFacePayload(
            StepFaceEntity stepFace,
            StepConicalSurface stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.getFirst().outer()) {
            return null;
        }
        if (!(bounds.getFirst().loop() instanceof EdgeLoop outerLoop) || outerLoop.edges().size() != 4) {
            return null;
        }

        List<OrientedEdge> circleEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Circle)
                .toList();
        List<OrientedEdge> lineEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Line3)
                .toList();
        if (circleEdges.size() != 2 || lineEdges.size() != 2) {
            return null;
        }

        ConicalSurface surface = builder.buildConicalSurface(stepSurface.id());
        OrientedEdge lowerArc = circleEdges.getFirst();
        OrientedEdge upperArc = circleEdges.getLast();
        if (averageAxialHeight(surface.position(), sampleOrientedEdge(lowerArc)) > averageAxialHeight(surface.position(), sampleOrientedEdge(upperArc))) {
            lowerArc = circleEdges.getLast();
            upperArc = circleEdges.getFirst();
        }

        List<CartesianPoint> lowerArcPoints = sampleOrientedEdge(lowerArc);
        List<CartesianPoint> upperArcPoints = sampleOrientedEdge(upperArc);
        double lowerHeight = averageAxialHeight(surface.position(), lowerArcPoints);
        double upperHeight = averageAxialHeight(surface.position(), upperArcPoints);
        if (Math.abs(upperHeight - lowerHeight) <= Epsilon.EPS) {
            return null;
        }

        List<Double> angles = unwrapAngles(surface.position(), lowerArcPoints);
        if (angles.size() < 2) {
            return null;
        }

        boolean sameSense = faceSameSense(stepFace);
        List<PointPayload> triangles = triangulateConicalStrip(surface, lowerHeight, upperHeight, angles, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = conicalNormal(surface, angles.getFirst(), sameSense);
        return new FacePayload(
                stepFace.id(),
                faceDisplayName(stepFace),
                "CONICAL_SURFACE",
                toPointPayload(conicalSurfacePoint(surface, angles.getFirst(), lowerHeight)),
                new VectorPayload(startNormal.x(), startNormal.y(), startNormal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.getFirst())))),
                triangles,
                new FaceSurfacePayload(
                        "conical_strip",
                        List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                        List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                        List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                        surface.radius(),
                        null,
                        surface.semiAngle(),
                        lowerHeight,
                        upperHeight,
                        angles.getFirst(),
                        angles.getLast() - angles.getFirst(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                ),
                null
        );
    }

    private static FacePayload toToroidalFacePayload(
            StepFaceEntity stepFace,
            StepToroidalSurface stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.getFirst().outer()) {
            return null;
        }
        if (!(bounds.getFirst().loop() instanceof EdgeLoop outerLoop) || outerLoop.edges().size() != 4) {
            return null;
        }

        List<OrientedEdge> circleEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Circle)
                .toList();
        if (circleEdges.size() != 4) {
            return null;
        }

        ToroidalSurface surface = builder.buildToroidalSurface(stepSurface.id());
        List<OrientedEdge> varyingUEdges = new ArrayList<>();
        List<OrientedEdge> varyingVEdges = new ArrayList<>();
        for (OrientedEdge edge : circleEdges) {
            List<CartesianPoint> points = sampleOrientedEdge(edge);
            List<Double> uValues = unwrapToroidalU(surface, points);
            List<Double> vValues = unwrapToroidalV(surface, points);
            double uRange = Math.abs(uValues.getLast() - uValues.getFirst());
            double vRange = Math.abs(vValues.getLast() - vValues.getFirst());
            if (uRange >= vRange) {
                varyingUEdges.add(edge);
            } else {
                varyingVEdges.add(edge);
            }
        }
        if (varyingUEdges.size() != 2 || varyingVEdges.size() != 2) {
            return null;
        }

        OrientedEdge lowerVEdge = varyingUEdges.getFirst();
        OrientedEdge upperVEdge = varyingUEdges.getLast();
        if (averageToroidalV(surface, sampleOrientedEdge(lowerVEdge)) > averageToroidalV(surface, sampleOrientedEdge(upperVEdge))) {
            lowerVEdge = varyingUEdges.getLast();
            upperVEdge = varyingUEdges.getFirst();
        }

        List<CartesianPoint> lowerPoints = sampleOrientedEdge(lowerVEdge);
        List<Double> uValues = unwrapToroidalU(surface, lowerPoints);
        double lowerV = averageToroidalV(surface, lowerPoints);
        double upperV = averageToroidalV(surface, sampleOrientedEdge(upperVEdge));
        if (Math.abs(upperV - lowerV) <= Epsilon.EPS || uValues.size() < 2) {
            return null;
        }

        boolean sameSense = faceSameSense(stepFace);
        List<PointPayload> triangles = triangulateToroidalStrip(surface, lowerV, upperV, uValues, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = toroidalNormal(surface, uValues.getFirst(), lowerV, sameSense);
        return new FacePayload(
                stepFace.id(),
                faceDisplayName(stepFace),
                "TOROIDAL_SURFACE",
                toPointPayload(toroidalSurfacePoint(surface, uValues.getFirst(), lowerV)),
                new VectorPayload(startNormal.x(), startNormal.y(), startNormal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.getFirst())))),
                triangles,
                new FaceSurfacePayload(
                        "toroidal_strip",
                        List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                        List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                        List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                        surface.majorRadius(),
                        surface.minorRadius(),
                        null,
                        lowerV,
                        upperV,
                        uValues.getFirst(),
                        uValues.getLast() - uValues.getFirst(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                ),
                null
        );
    }

    private static FacePayload toBSplineSurfaceFacePayload(
            StepFaceEntity stepFace,
            StepEntity stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.getFirst().outer()) {
            return null;
        }
        if (!(bounds.getFirst().loop() instanceof EdgeLoop outerLoop) || outerLoop.edges().size() != 4) {
            return null;
        }

        SurfacePatch patch = buildFourSidedPatch(outerLoop);
        if (patch == null) {
            return null;
        }
        BSplineSurface3 surface = buildBsplineSurface(stepSurface, builder);
        int uSegments = Math.max(patch.uSegments(), 10);
        int vSegments = Math.max(patch.vSegments(), 10);
        List<PointPayload> triangles = triangulateSurfaceGrid(
                sampleSurfaceGrid(surface, uSegments, vSegments),
                faceSameSense(stepFace)
        );
        if (triangles.isEmpty()) {
            return null;
        }
        Vector3 normal = surface.normalAt((surface.uStart() + surface.uEnd()) * 0.5, (surface.vStart() + surface.vEnd()) * 0.5);
        if (!faceSameSense(stepFace)) {
            normal = normal.scale(-1.0);
        }
        return new FacePayload(
                stepFace.id(),
                faceDisplayName(stepFace),
                surfaceTypeName(stepSurface),
                toPointPayload(surface.pointAt(surface.uStart(), surface.vStart())),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                faceSameSense(stepFace),
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.getFirst())))),
                triangles,
                null,
                null
        );
    }

    private static BSplineSurface3 buildBsplineSurface(StepEntity geometry, StepCadBuilder builder) {
        if (geometry instanceof StepBSplineSurfaceWithKnots splineSurface) {
            return builder.buildBSplineSurface(splineSurface.id());
        }
        if (geometry instanceof StepBSplineSurface splineSurface) {
            return builder.buildGenericBSplineSurface(splineSurface.id());
        }
        if (geometry instanceof StepBSplineSurfaceWithKnotsAndBreakpoints splineSurface) {
            return builder.buildBSplineSurfaceWithBreakpoints(splineSurface.id());
        }
        if (geometry instanceof StepBezierSurface splineSurface) {
            return builder.buildBezierSurface(splineSurface.id());
        }
        if (geometry instanceof StepUniformSurface splineSurface) {
            return builder.buildUniformSurface(splineSurface.id());
        }
        if (geometry instanceof StepQuasiUniformSurface splineSurface) {
            return builder.buildQuasiUniformSurface(splineSurface.id());
        }
        if (geometry instanceof StepPiecewiseBezierSurface splineSurface) {
            return builder.buildPiecewiseBezierSurface(splineSurface.id());
        }
        throw new UnsupportedGeometryException(surfaceTypeName(geometry) + " is not a supported B-spline-like surface");
    }

    private static BSplineSurface3 buildFreeFormSurface(StepFreeFormSurface surface, StepCadBuilder builder) {
        int uCount = surface.controlPoints().size();
        int vCount = surface.controlPoints().isEmpty() ? 0 : surface.controlPoints().getFirst().size();
        if (uCount < 2 || vCount < 2) {
            throw new UnsupportedGeometryException("FREE_FORM_SURFACE requires at least 2x2 control points");
        }
        List<List<CartesianPoint>> controlPoints = new ArrayList<>(uCount);
        for (List<StepEntity> row : surface.controlPoints()) {
            List<CartesianPoint> pointRow = new ArrayList<>(row.size());
            for (StepEntity pt : row) {
                if (pt instanceof com.minicad.step.model.StepCartesianPoint cartesianPoint) {
                    pointRow.add(builder.buildPoint(cartesianPoint.id()));
                } else {
                    throw new UnsupportedGeometryException("FREE_FORM_SURFACE control points must be Cartesian points");
                }
            }
            controlPoints.add(List.copyOf(pointRow));
        }
        int uDegree = surface.degreeU();
        int vDegree = surface.degreeV();
        // Generate uniform knot vectors
        int uKnotCount = uCount + uDegree + 1;
        int vKnotCount = vCount + vDegree + 1;
        List<Double> uKnots = new ArrayList<>();
        for (int i = 0; i < uKnotCount; i++) {
            uKnots.add((double) i / (uKnotCount - 1));
        }
        List<Double> vKnots = new ArrayList<>();
        for (int i = 0; i < vKnotCount; i++) {
            vKnots.add((double) i / (vKnotCount - 1));
        }
        List<Integer> uMults = List.of(1);
        List<Integer> vMults = List.of(1);
        return new BSplineSurface3(uDegree, vDegree, controlPoints, uMults, vMults, uKnots, vKnots);
    }

    private static FacePayload toRationalBSplineSurfaceFacePayload(
            StepFaceEntity stepFace,
            StepRationalBSplineSurface stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.getFirst().outer()) {
            return null;
        }
        RationalBSplineSurface3 surface = builder.buildRationalBSplineSurface(stepSurface.id());
        List<PointPayload> triangles = triangulateSurfaceGrid(
                sampleSurfaceGrid(surface, 16, 16),
                faceSameSense(stepFace)
        );
        if (triangles.isEmpty()) {
            return null;
        }
        Vector3 normal = surface.normalAt((surface.uStart() + surface.uEnd()) * 0.5, (surface.vStart() + surface.vEnd()) * 0.5);
        if (!faceSameSense(stepFace)) {
            normal = normal.scale(-1.0);
        }
        return new FacePayload(
                stepFace.id(),
                faceDisplayName(stepFace),
                "RATIONAL_B_SPLINE_SURFACE",
                toPointPayload(surface.pointAt(surface.uStart(), surface.vStart())),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                faceSameSense(stepFace),
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.getFirst())))),
                triangles,
                null,
                null
        );
    }

    private static FacePayload toFourSidedPatchFacePayload(
            StepFaceEntity stepFace,
            StepEntity geometry,
            StepMetadataExtractor.DisplayMetadata metadata,
            StepCadBuilder builder
    ) {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.getFirst().outer()) {
            return null;
        }
        if (!(bounds.getFirst().loop() instanceof EdgeLoop outerLoop) || outerLoop.edges().size() != 4) {
            return null;
        }
        SurfacePatch patch = buildFourSidedPatch(outerLoop);
        if (patch == null) {
            return null;
        }
        List<PointPayload> triangles = triangulatePatch(patch, faceSameSense(stepFace));
        if (triangles.isEmpty()) {
            return null;
        }
        Vector3 normal = patch.normalAt(0.5, 0.5);
        if (!faceSameSense(stepFace)) {
            normal = normal.scale(-1.0);
        }
        return new FacePayload(
                stepFace.id(),
                faceDisplayName(stepFace),
                surfaceTypeName(geometry),
                toPointPayload(patch.pointAt(0.0, 0.0)),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                faceSameSense(stepFace),
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.getFirst())))),
                triangles,
                null,
                null
        );
    }

    private static FacePayload toRuledSurfaceFacePayload(
            StepFaceEntity stepFace,
            StepRuledSurface stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) throws TopologyException, StepResolutionException, UnsupportedGeometryException, GeometryException {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.isEmpty()) {
            return null;
        }
        RuledSurface3 surface = builder.buildRuledSurface(stepSurface.id());
        java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(32, 32);
        List<PointPayload> triangles = triangulateSurfaceGrid(grid, faceSameSense(stepFace));
        if (triangles.isEmpty()) {
            return null;
        }
        boolean sameSense = faceSameSense(stepFace);
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) normal = normal.scale(-1.0);
        List<LoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : bounds) {
            loops.add(new LoopPayload(bound.outer(), toPointPayloads(sampleLoop(bound))));
        }
        return new FacePayload(
                stepFace.id(),
                faceDisplayName(stepFace),
                "RULED_SURFACE",
                triangles.get(0),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops,
                triangles,
                new FaceSurfacePayload(
                        "ruled_surface", null, null, null, 0.0, null, null,
                        0.0, 0.0, 0.0, 0.0,
                        null, null, null, null, null, null, null
                ),
                null
        );
    }

    private static FacePayload toSurfaceOfConstantRadiusFacePayload(
            StepFaceEntity stepFace,
            StepSurfaceOfConstantRadius stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) throws TopologyException, StepResolutionException, UnsupportedGeometryException, GeometryException {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.isEmpty()) {
            return null;
        }
        SurfaceOfConstantRadius3 surface = builder.buildSurfaceOfConstantRadius(stepSurface.id());
        java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(32, 32);
        List<PointPayload> triangles = triangulateSurfaceGrid(grid, faceSameSense(stepFace));
        if (triangles.isEmpty()) {
            return null;
        }
        boolean sameSense = faceSameSense(stepFace);
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) normal = normal.scale(-1.0);
        List<LoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : bounds) {
            loops.add(new LoopPayload(bound.outer(), toPointPayloads(sampleLoop(bound))));
        }
        return new FacePayload(
                stepFace.id(),
                faceDisplayName(stepFace),
                "SURFACE_OF_CONSTANT_RADIUS",
                triangles.get(0),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops,
                triangles,
                new FaceSurfacePayload(
                        "constant_radius_surface", null, null, null, surface.radius(), null, null,
                        0.0, 0.0, 0.0, 0.0,
                        null, null, null, null, null, null, null
                ),
                null
        );
    }

    private static FacePayload toSampledSurfaceFacePayload(
            StepFaceEntity stepFace,
            SurfaceGeometry surface,
            String surfaceType,
            List<FaceBound> bounds,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        int segments = 32;
        java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(segments, segments);
        if (grid.isEmpty()) {
            return null;
        }
        boolean sameSense = faceSameSense(stepFace);
        List<PointPayload> triangles = triangulateSurfaceGrid(grid, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) {
            normal = normal.scale(-1.0);
        }
        return new FacePayload(
                stepFace.id(),
                faceDisplayName(stepFace),
                surfaceType,
                triangles.get(0),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.transparency(),
                toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.getFirst())))),
                triangles,
                null,
                null
        );
    }

    private static PreviewFaceResult toParametricTrimmedFaceResult(
            StepFaceEntity stepFace,
            StepEntity geometry,
            StepMetadataExtractor.DisplayMetadata metadata,
            StepCadBuilder builder
    ) {
        List<FaceBound> normalizedBounds = List.of();
        try {
            normalizedBounds = buildFaceBounds(stepFace, builder);
        } catch (TopologyException | StepResolutionException | UnsupportedGeometryException ex) {
            String unsupportedSurface = describeUnsupportedPreviewSurface(geometry, builder);
            if (unsupportedSurface != null && unsupportedSurface.contains("unsupported")) {
                return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, unsupportedSurface));
            }
            log.debug("stage={} faceId={}, surfaceType={}, reason={}", "parametric_bounds_fallback",
                    stepFace.id(), surfaceTypeName(geometry), ex.getMessage());
        }
        ParametricSurfaceMapper mapper = mapperForSurface(geometry, builder);
        if (mapper == null) {
            String unsupportedSurface = describeUnsupportedPreviewSurface(geometry, builder);
            String reason = unsupportedSurface == null
                    ? "no parametric mapper for surface"
                    : unsupportedSurface.contains("unsupported")
                    ? unsupportedSurface
                    : unsupportedSurface + " preview is unsupported";
            return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, reason));
        }
        List<ParametricLoopPayload> loops = buildParametricLoops(stepFace, geometry, mapper, builder);
        if (loops.isEmpty()) {
            try {
                loops = buildParametricLoops(normalizedBounds, mapper);
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException ex) {
                return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "failed to derive face bounds"));
            }
        }
        if (loops.isEmpty()) {
            return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "failed to build parametric loops"));
        }
        loops = normalizeLoopRoles(stepFace, geometry, loops);
        if (loops.stream().noneMatch(ParametricLoopPayload::outer)) {
            log.debug("stage={} faceId={}, surfaceType={}, semanticBoundCount={}, semanticOuterCount={}, normalizedBoundCount={}, loopCount={}", "parametric_outer_bound_missing",
                    stepFace.id(), surfaceTypeName(geometry),
                            stepFace.bounds().size(),
                            stepFace.bounds().stream().filter(com.minicad.step.model.StepFaceBound::outer).count(),
                            normalizedBounds.size(),
                            loops.size());
            return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "missing outer bound"));
        }
        UvBounds uvBounds = boundsOf(loops);
        if (uvBounds == null || uvBounds.uSpan() <= Epsilon.EPS || uvBounds.vSpan() <= Epsilon.EPS) {
            return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "degenerate parametric bounds"));
        }

        int sampleCount = loops.stream().mapToInt(loop -> loop.points().size()).max().orElse(0);
        // Preview meshes should stay light enough for API transport and browser upload.
        int baseUSegments = Math.max(12, Math.min(32, sampleCount * 2));
        int baseVSegments = Math.max(8, Math.min(24, sampleCount * 2));
        if (geometry instanceof StepRationalBSplineSurface) {
            baseUSegments = Math.max(12, Math.min(24, sampleCount * 2));
            baseVSegments = Math.max(8, Math.min(18, sampleCount * 2));
        } else if (geometry instanceof StepBSplineSurfaceWithKnots) {
            baseUSegments = Math.max(12, Math.min(24, sampleCount * 2));
            baseVSegments = Math.max(8, Math.min(18, sampleCount * 2));
        } else if (geometry instanceof StepPlane) {
            int planeSegments = Math.max(16, Math.min(32, sampleCount * 2));
            if (loops.size() > 1) {
                planeSegments = Math.max(planeSegments, 40);
            }
            double dominantSpan = Math.max(uvBounds.uSpan(), uvBounds.vSpan());
            double uRatio = dominantSpan <= Epsilon.EPS ? 1.0 : uvBounds.uSpan() / dominantSpan;
            double vRatio = dominantSpan <= Epsilon.EPS ? 1.0 : uvBounds.vSpan() / dominantSpan;
            baseUSegments = Math.max(baseUSegments, Math.max(16, (int) Math.ceil(planeSegments * uRatio)));
            baseVSegments = Math.max(baseVSegments, Math.max(16, (int) Math.ceil(planeSegments * vRatio)));
        } else if (geometry instanceof StepCylindricalSurface) {
            baseUSegments = Math.max(baseUSegments, 28);
            baseVSegments = Math.max(baseVSegments, 16);
        } else if (geometry instanceof StepConicalSurface || geometry instanceof StepToroidalSurface) {
            baseUSegments = Math.max(baseUSegments, 28);
            baseVSegments = Math.max(baseVSegments, 16);
        }
        List<PointPayload> triangles = triangulateParametricFaceAdaptive(
                mapper,
                loops,
                uvBounds,
                baseUSegments,
                baseVSegments,
                faceSameSense(stepFace)
        );
        if (triangles.isEmpty()) {
            log.debug("stage={} faceId={}, surfaceType={}, loopCount={}, outerLoopCount={}, innerLoopCount={}, uvBounds={}, sampleCount={}, baseUSegments={}, baseVSegments={}, loopPoints={}", "parametric_triangulation_empty",
                    stepFace.id(), surfaceTypeName(geometry), loops.size(),
                            loops.stream().filter(ParametricLoopPayload::outer).count(),
                            loops.stream().filter(loop -> !loop.outer()).count(),
                            formatUvBounds(uvBounds),
                            sampleCount,
                            baseUSegments,
                            baseVSegments,
                            summarizeLoopPointCounts(loops));
            return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "parametric triangulation produced no cells"));
        }

        double centerU = (uvBounds.minU() + uvBounds.maxU()) * 0.5;
        double centerV = (uvBounds.minV() + uvBounds.maxV()) * 0.5;
        Vector3 normal = mapper.normalAt(centerU, centerV);
        if (!faceSameSense(stepFace)) {
            normal = normal.scale(-1.0);
        }
        return new PreviewFaceResult(
                new FacePayload(
                        stepFace.id(),
                        faceDisplayName(stepFace),
                        surfaceTypeName(geometry),
                        toPointPayload(mapper.pointAt(centerU, centerV)),
                        new VectorPayload(normal.x(), normal.y(), normal.z()),
                        faceSameSense(stepFace),
                        toColorPayload(metadata.rgb()),
                        metadata.transparency(),
                        toPbrPayload(metadata.pbr()),
                        metadata.layers(),
                        toParametricLoopPayloads(loops, mapper),
                        triangles,
                        faceSurfacePayload(geometry, uvBounds, builder),
                        loops
                ),
                null
        );
    }

    private static List<PointPayload> triangulateCylindricalStrip(
            CylindricalSurface surface,
            double lowerHeight,
            double upperHeight,
            List<Double> angles,
            boolean sameSense
    ) {
        List<PointPayload> triangles = new ArrayList<>();
        for (int index = 0; index < angles.size() - 1; index++) {
            double angle0 = angles.get(index);
            double angle1 = angles.get(index + 1);
            if (Math.abs(angle1 - angle0) <= Epsilon.EPS) {
                continue;
            }

            CartesianPoint lower0 = surfacePoint(surface, angle0, lowerHeight);
            CartesianPoint lower1 = surfacePoint(surface, angle1, lowerHeight);
            CartesianPoint upper0 = surfacePoint(surface, angle0, upperHeight);
            CartesianPoint upper1 = surfacePoint(surface, angle1, upperHeight);

            Vector3 targetNormal = cylindricalNormal(surface, (angle0 + angle1) * 0.5, sameSense);
            appendOrientedTriangle(triangles, lower0, lower1, upper1, targetNormal);
            appendOrientedTriangle(triangles, lower0, upper1, upper0, targetNormal);
        }
        return List.copyOf(triangles);
    }

    private static List<PointPayload> triangulateConicalStrip(
            ConicalSurface surface,
            double lowerHeight,
            double upperHeight,
            List<Double> angles,
            boolean sameSense
    ) {
        List<PointPayload> triangles = new ArrayList<>();
        for (int index = 0; index < angles.size() - 1; index++) {
            double angle0 = angles.get(index);
            double angle1 = angles.get(index + 1);
            if (Math.abs(angle1 - angle0) <= Epsilon.EPS) {
                continue;
            }

            CartesianPoint lower0 = conicalSurfacePoint(surface, angle0, lowerHeight);
            CartesianPoint lower1 = conicalSurfacePoint(surface, angle1, lowerHeight);
            CartesianPoint upper0 = conicalSurfacePoint(surface, angle0, upperHeight);
            CartesianPoint upper1 = conicalSurfacePoint(surface, angle1, upperHeight);

            Vector3 targetNormal = conicalNormal(surface, (angle0 + angle1) * 0.5, sameSense);
            appendOrientedTriangle(triangles, lower0, lower1, upper1, targetNormal);
            appendOrientedTriangle(triangles, lower0, upper1, upper0, targetNormal);
        }
        return List.copyOf(triangles);
    }

    private static List<PointPayload> triangulateToroidalStrip(
            ToroidalSurface surface,
            double lowerV,
            double upperV,
            List<Double> uValues,
            boolean sameSense
    ) {
        List<PointPayload> triangles = new ArrayList<>();
        for (int index = 0; index < uValues.size() - 1; index++) {
            double u0 = uValues.get(index);
            double u1 = uValues.get(index + 1);
            if (Math.abs(u1 - u0) <= Epsilon.EPS) {
                continue;
            }
            CartesianPoint p00 = toroidalSurfacePoint(surface, u0, lowerV);
            CartesianPoint p10 = toroidalSurfacePoint(surface, u1, lowerV);
            CartesianPoint p01 = toroidalSurfacePoint(surface, u0, upperV);
            CartesianPoint p11 = toroidalSurfacePoint(surface, u1, upperV);
            Vector3 targetNormal = toroidalNormal(surface, (u0 + u1) * 0.5, (lowerV + upperV) * 0.5, sameSense);
            appendOrientedTriangle(triangles, p00, p10, p11, targetNormal);
            appendOrientedTriangle(triangles, p00, p11, p01, targetNormal);
        }
        return List.copyOf(triangles);
    }

    private static List<PointPayload> triangulatePatch(SurfacePatch patch, boolean sameSense) {
        List<PointPayload> triangles = new ArrayList<>();
        for (int u = 0; u < patch.uSegments(); u++) {
            for (int v = 0; v < patch.vSegments(); v++) {
                CartesianPoint p00 = patch.pointAt((double) u / patch.uSegments(), (double) v / patch.vSegments());
                CartesianPoint p10 = patch.pointAt((double) (u + 1) / patch.uSegments(), (double) v / patch.vSegments());
                CartesianPoint p01 = patch.pointAt((double) u / patch.uSegments(), (double) (v + 1) / patch.vSegments());
                CartesianPoint p11 = patch.pointAt((double) (u + 1) / patch.uSegments(), (double) (v + 1) / patch.vSegments());
                Vector3 targetNormal = patch.normalAt((u + 0.5) / patch.uSegments(), (v + 0.5) / patch.vSegments());
                if (!sameSense) {
                    targetNormal = targetNormal.scale(-1.0);
                }
                appendOrientedTriangle(triangles, p00, p10, p11, targetNormal);
                appendOrientedTriangle(triangles, p00, p11, p01, targetNormal);
            }
        }
        return List.copyOf(triangles);
    }

    private static List<PointPayload> triangulateSurfaceGrid(List<List<CartesianPoint>> grid, boolean sameSense) {
        List<PointPayload> triangles = new ArrayList<>();
        if (grid.size() < 2 || grid.getFirst().size() < 2) {
            return List.of();
        }
        for (int u = 0; u + 1 < grid.size(); u++) {
            for (int v = 0; v + 1 < grid.get(u).size(); v++) {
                CartesianPoint p00 = grid.get(u).get(v);
                CartesianPoint p10 = grid.get(u + 1).get(v);
                CartesianPoint p01 = grid.get(u).get(v + 1);
                CartesianPoint p11 = grid.get(u + 1).get(v + 1);
                Vector3 targetNormal = p10.subtract(p00).cross(p01.subtract(p00));
                if (targetNormal.norm() <= Epsilon.EPS) {
                    continue;
                }
                if (!sameSense) {
                    targetNormal = targetNormal.scale(-1.0);
                }
                appendOrientedTriangle(triangles, p00, p10, p11, targetNormal);
                appendOrientedTriangle(triangles, p00, p11, p01, targetNormal);
            }
        }
        return List.copyOf(triangles);
    }

    private static List<List<CartesianPoint>> sampleSurfaceGrid(BSplineSurface3 surface, int uSegments, int vSegments) {
        return surface.sampleGrid(Math.max(uSegments, 2), Math.max(vSegments, 2));
    }

    private static List<List<CartesianPoint>> sampleSurfaceGrid(RationalBSplineSurface3 surface, int uSegments, int vSegments) {
        return surface.sampleGrid(Math.max(uSegments, 2), Math.max(vSegments, 2));
    }

    private static List<ParametricLoopPayload> buildParametricLoops(List<FaceBound> bounds, ParametricSurfaceMapper mapper) {
        List<ParametricLoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : bounds) {
            if (bound.loop() instanceof VertexLoop) {
                return List.of();
            }
            List<CartesianPoint> points3d = sampleLoop(bound);
            if (points3d.size() < 4) {
                return List.of();
            }
            List<UvPoint> uvPoints = new ArrayList<>(points3d.size());
            UvPoint previous = null;
            for (CartesianPoint point : points3d) {
                UvPoint uv = mapper.project(point, previous);
                if (uv == null) {
                    return List.of();
                }
                uvPoints.add(uv);
                previous = uv;
            }
            uvPoints = normalizePeriodicLoop(uvPoints, mapper);
            uvPoints.set(0, uvPoints.getFirst());
            uvPoints.set(uvPoints.size() - 1, uvPoints.getFirst());
            loops.add(new ParametricLoopPayload(bound.outer(), List.copyOf(uvPoints)));
        }
        return List.copyOf(loops);
    }

    private static List<ParametricLoopPayload> buildParametricLoops(
            StepFaceEntity stepFace,
            StepEntity geometry,
            ParametricSurfaceMapper mapper,
            StepCadBuilder builder
    ) {
        List<ParametricLoopPayload> loops = new ArrayList<>();
        boolean promoteSingleOuter = stepFace.bounds().size() == 1
                && stepFace.bounds().stream().noneMatch(com.minicad.step.model.StepFaceBound::outer);
        for (com.minicad.step.model.StepFaceBound bound : stepFace.bounds()) {
            if (!(bound.loop() instanceof com.minicad.step.model.StepEdgeLoop edgeLoop)) {
                log.debug("stage={} faceId={}, surfaceType={}, boundId={}, reason={}", "parametric_loop_build_failed",
                        stepFace.id(), surfaceTypeName(geometry), bound.id(), "bound loop is not EDGE_LOOP");
                return List.of();
            }
            List<UvPoint> loopPoints = new ArrayList<>();
            boolean firstEdge = true;
            for (com.minicad.step.model.StepOrientedEdge orientedEdge : edgeLoop.edges()) {
                List<UvPoint> edgePoints = sampleParametricOrientedEdge(orientedEdge, geometry, mapper, builder);
                if (edgePoints == null || edgePoints.size() < 2) {
                    log.debug("stage={} faceId={}, surfaceType={}, boundId={}, edgeId={}, orientedEdgeId={}, reason={}", "parametric_loop_build_failed",
                            stepFace.id(), surfaceTypeName(geometry), bound.id(),
                                    orientedEdge.edgeElement().id(), orientedEdge.id(),
                                    "edge sampling returned " + (edgePoints == null ? "null" : edgePoints.size() + " points"));
                    return List.of();
                }
                int startIndex = firstEdge ? 0 : 1;
                for (int index = startIndex; index < edgePoints.size(); index++) {
                    loopPoints.add(edgePoints.get(index));
                }
                firstEdge = false;
            }
            if (loopPoints.size() < 4) {
                log.debug("stage={} faceId={}, surfaceType={}, boundId={}, reason={}, loopPointCount={}", "parametric_loop_build_failed",
                        stepFace.id(), surfaceTypeName(geometry), bound.id(),
                                "loop contains fewer than 4 UV points", loopPoints.size());
                return List.of();
            }
            if (!bound.orientation()) {
                loopPoints = reverseClosedLoop(loopPoints);
            }
            loopPoints = normalizePeriodicLoop(loopPoints, mapper);
            if (!sameUv(loopPoints.getFirst(), loopPoints.getLast())) {
                loopPoints.add(loopPoints.getFirst());
            }
            loops.add(new ParametricLoopPayload(bound.outer() || promoteSingleOuter, List.copyOf(loopPoints)));
        }
        return List.copyOf(loops);
    }

    private static List<UvPoint> normalizePeriodicLoop(List<UvPoint> points, ParametricSurfaceMapper mapper) {
        if (points.size() < 2) {
            return points;
        }
        Double uPeriod = mapper.uPeriod();
        Double vPeriod = mapper.vPeriod();
        List<UvPoint> normalized = new ArrayList<>(points.size());
        UvPoint previous = null;
        for (UvPoint point : points) {
            double u = point.u();
            double v = point.v();
            if (previous != null) {
                if (uPeriod != null) {
                    u = unwrapPeriodic(u, previous.u(), uPeriod);
                }
                if (vPeriod != null) {
                    v = unwrapPeriodic(v, previous.v(), vPeriod);
                }
            }
            UvPoint normalizedPoint = new UvPoint(u, v);
            normalized.add(normalizedPoint);
            previous = normalizedPoint;
        }
        if (normalized.size() >= 2) {
            UvPoint first = normalized.getFirst();
            UvPoint last = normalized.getLast();
            double u = last.u();
            double v = last.v();
            if (uPeriod != null) {
                u = unwrapPeriodic(u, first.u(), uPeriod);
            }
            if (vPeriod != null) {
                v = unwrapPeriodic(v, first.v(), vPeriod);
            }
            normalized.set(normalized.size() - 1, new UvPoint(u, v));
        }
        return normalized;
    }

    private static UvBounds boundsOf(List<ParametricLoopPayload> loops) {
        double minU = Double.POSITIVE_INFINITY;
        double minV = Double.POSITIVE_INFINITY;
        double maxU = Double.NEGATIVE_INFINITY;
        double maxV = Double.NEGATIVE_INFINITY;
        for (ParametricLoopPayload loop : loops) {
            for (UvPoint point : loop.points()) {
                minU = Math.min(minU, point.u());
                minV = Math.min(minV, point.v());
                maxU = Math.max(maxU, point.u());
                maxV = Math.max(maxV, point.v());
            }
        }
        if (!Double.isFinite(minU) || !Double.isFinite(minV) || !Double.isFinite(maxU) || !Double.isFinite(maxV)) {
            return null;
        }
        return new UvBounds(minU, minV, maxU, maxV);
    }

    private static FaceSurfacePayload faceSurfacePayload(
            StepEntity geometry,
            UvBounds uvBounds,
            StepCadBuilder builder
    ) {
        StepEntity surfaceGeometry = unwrapParametricPreviewSurface(geometry);
        if (surfaceGeometry instanceof StepPlane stepPlane) {
            Plane plane = builder.buildPlane(stepPlane.id());
            Direction3 normal = plane.normal();
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "plane_face",
                    List.of(plane.origin().x(), plane.origin().y(), plane.origin().z()),
                    List.of(normal.x(), normal.y(), normal.z()),
                    basisDirectionForNormal(normal),
                    0.0,
                    null,
                    null,
                    uvBounds.minU(),
                    uvBounds.maxU(),
                    uvBounds.minV(),
                    uvBounds.maxV(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepCylindricalSurface cylindricalSurface) {
            CylindricalSurface surface = builder.buildCylindricalSurface(cylindricalSurface.id());
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "cylindrical_strip",
                    List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                    List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                    List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                    surface.radius(),
                    null,
                    null,
                    uvBounds.minV(),
                    uvBounds.maxV(),
                    uvBounds.minU(),
                    uvBounds.uSpan(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepConicalSurface conicalSurface) {
            ConicalSurface surface = builder.buildConicalSurface(conicalSurface.id());
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "conical_strip",
                    List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                    List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                    List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                    surface.radius(),
                    null,
                    surface.semiAngle(),
                    uvBounds.minV(),
                    uvBounds.maxV(),
                    uvBounds.minU(),
                    uvBounds.uSpan(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepSphericalSurface sphericalSurface) {
            SphericalSurface surface = builder.buildSphericalSurface(sphericalSurface.id());
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "spherical_surface",
                    List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                    List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                    List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                    surface.radius(),
                    null,
                    null,
                    uvBounds.minV(),
                    uvBounds.maxV(),
                    uvBounds.minU(),
                    uvBounds.uSpan(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepToroidalSurface toroidalSurface) {
            ToroidalSurface surface = builder.buildToroidalSurface(toroidalSurface.id());
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "toroidal_strip",
                    List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                    List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                    List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                    surface.majorRadius(),
                    surface.minorRadius(),
                    null,
                    uvBounds.minV(),
                    uvBounds.maxV(),
                    uvBounds.minU(),
                    uvBounds.uSpan(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepDegenerateToroidalSurface toroidalSurface) {
            ToroidalSurface surface = builder.buildDegenerateToroidalSurface(toroidalSurface.id());
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "toroidal_strip",
                    List.of(surface.position().location().x(), surface.position().location().y(), surface.position().location().z()),
                    List.of(surface.position().axis().x(), surface.position().axis().y(), surface.position().axis().z()),
                    List.of(surface.position().xDirection().x(), surface.position().xDirection().y(), surface.position().xDirection().z()),
                    surface.majorRadius(),
                    surface.minorRadius(),
                    null,
                    uvBounds.minV(),
                    uvBounds.maxV(),
                    uvBounds.minU(),
                    uvBounds.uSpan(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepSurfaceOfLinearExtrusion extrusionSurface) {
            SurfaceOfLinearExtrusion3 surface = builder.buildSurfaceOfLinearExtrusion(extrusionSurface.id());
            Direction3 axis = surface.extrusionVector().normalize();
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "surface_of_linear_extrusion",
                    null,
                    List.of(axis.x(), axis.y(), axis.z()),
                    null,
                    0.0,
                    null,
                    null,
                    uvBounds.minV(),
                    uvBounds.maxV(),
                    uvBounds.minU(),
                    uvBounds.uSpan(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepSurfaceOfRevolution revolutionSurface) {
            SurfaceOfRevolution3 surface = builder.buildSurfaceOfRevolution(revolutionSurface.id());
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "surface_of_revolution",
                    List.of(surface.axisOrigin().x(), surface.axisOrigin().y(), surface.axisOrigin().z()),
                    List.of(surface.axisDirection().x(), surface.axisDirection().y(), surface.axisDirection().z()),
                    null,
                    0.0,
                    null,
                    null,
                    uvBounds.minV(),
                    uvBounds.maxV(),
                    uvBounds.minU(),
                    uvBounds.uSpan(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepRationalBSplineSurface splineSurface) {
            RationalBSplineSurface3 surface = builder.buildRationalBSplineSurface(splineSurface.id());
            List<List<List<Double>>> controlPoints = surface.controlPoints().stream()
                    .map(row -> row.stream()
                            .map(point -> List.of(point.x(), point.y(), point.z()))
                            .toList())
                    .toList();
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "rational_bspline_surface",
                    null,
                    null,
                    null,
                    0.0,
                    null,
                    null,
                    surface.uStart(),
                    surface.uEnd(),
                    surface.vStart(),
                    surface.vEnd(),
                    surface.uDegree(),
                    surface.vDegree(),
                    controlPoints,
                    surface.uMultiplicities(),
                    surface.vMultiplicities(),
                    surface.uKnots(),
                    surface.vKnots()
            ), geometry);
        }
        if (surfaceGeometry instanceof StepBSplineSurfaceWithKnots
                || surfaceGeometry instanceof StepBezierSurface
                || surfaceGeometry instanceof StepUniformSurface
                || surfaceGeometry instanceof StepQuasiUniformSurface
                || surfaceGeometry instanceof StepPiecewiseBezierSurface) {
            BSplineSurface3 surface = buildBsplineSurface(surfaceGeometry, builder);
            List<List<List<Double>>> controlPoints = surface.controlPoints().stream()
                    .map(row -> row.stream()
                            .map(point -> List.of(point.x(), point.y(), point.z()))
                            .toList())
                    .toList();
            return withSurfaceSourceMetadata(new FaceSurfacePayload(
                    "bspline_surface",
                    null,
                    null,
                    null,
                    0.0,
                    null,
                    null,
                    surface.uStart(),
                    surface.uEnd(),
                    surface.vStart(),
                    surface.vEnd(),
                    surface.uDegree(),
                    surface.vDegree(),
                    controlPoints,
                    surface.uMultiplicities(),
                    surface.vMultiplicities(),
                    surface.uKnots(),
                    surface.vKnots()
            ), geometry);
        }
        return null;
    }

    private static FaceSurfacePayload withSurfaceSourceMetadata(FaceSurfacePayload base, StepEntity geometry) {
        if (base == null || geometry == null) {
            return base;
        }
        String basisType = null;
        Integer basisStepId = null;
        Boolean orientation = null;
        Double offsetDistance = null;
        Double trimU1 = null;
        Double trimU2 = null;
        Double trimV1 = null;
        Double trimV2 = null;
        Boolean implicitOuter = null;
        Double transformScale = null;

        if (geometry instanceof StepRectangularTrimmedSurface trimmedSurface) {
            basisType = surfaceTypeName(trimmedSurface.basisSurface());
            basisStepId = trimmedSurface.basisSurface().id();
            trimU1 = trimmedSurface.u1();
            trimU2 = trimmedSurface.u2();
            trimV1 = trimmedSurface.v1();
            trimV2 = trimmedSurface.v2();
        } else if (geometry instanceof StepCurveBoundedSurface boundedSurface) {
            basisType = surfaceTypeName(boundedSurface.basisSurface());
            basisStepId = boundedSurface.basisSurface().id();
            implicitOuter = boundedSurface.implicitOuter();
        } else if (geometry instanceof StepOrientedSurface orientedSurface) {
            basisType = surfaceTypeName(orientedSurface.surfaceElement());
            basisStepId = orientedSurface.surfaceElement().id();
            orientation = orientedSurface.orientation();
        } else if (geometry instanceof StepOffsetSurface offsetSurface) {
            basisType = surfaceTypeName(offsetSurface.basisSurface());
            basisStepId = offsetSurface.basisSurface().id();
            offsetDistance = offsetSurface.distance();
        } else if (geometry instanceof StepGeometricReplica replica && "SURFACE_REPLICA".equals(replica.entityName())) {
            basisType = surfaceTypeName(replica.parent());
            basisStepId = replica.parent().id();
            transformScale = replica.transformation().scale();
        }

        return new FaceSurfacePayload(
                base.type(),
                base.center(),
                base.axis(),
                base.xDirection(),
                base.radius(),
                base.minorRadius(),
                base.semiAngle(),
                base.lowerHeight(),
                base.upperHeight(),
                base.startAngle(),
                base.sweepAngle(),
                base.uDegree(),
                base.vDegree(),
                base.controlPoints(),
                base.uMultiplicities(),
                base.vMultiplicities(),
                base.uKnots(),
                base.vKnots(),
                surfaceTypeName(geometry),
                geometry.id(),
                basisType,
                basisStepId,
                orientation,
                offsetDistance,
                trimU1,
                trimU2,
                trimV1,
                trimV2,
                implicitOuter,
                transformScale
        );
    }

    private static List<Double> basisDirectionForNormal(Direction3 normal) {
        Vector3 axis = normal.asVector();
        Vector3 reference = Math.abs(axis.x()) < 0.9
                ? new Vector3(1.0, 0.0, 0.0)
                : new Vector3(0.0, 1.0, 0.0);
        Direction3 xDirection = reference.subtract(axis.scale(reference.dot(axis))).normalize();
        return List.of(xDirection.x(), xDirection.y(), xDirection.z());
    }

    private static List<UvPoint> sampleParametricOrientedEdge(
            com.minicad.step.model.StepOrientedEdge orientedEdge,
            StepEntity faceGeometry,
            ParametricSurfaceMapper mapper,
            StepCadBuilder builder
    ) {
        StepVertexPoint startVertex = orientedEdge.orientation()
                ? orientedEdge.edgeElement().start()
                : orientedEdge.edgeElement().end();
        StepVertexPoint endVertex = orientedEdge.orientation()
                ? orientedEdge.edgeElement().end()
                : orientedEdge.edgeElement().start();
        StepEntity edgeGeometry = orientedEdge.edgeElement().edgeGeometry();
        StepEntity associatedSource = unwrapAssociatedCurveGeometry(edgeGeometry);
        List<StepEntity> pcurves = switch (associatedSource) {
            case StepSurfaceCurve surfaceCurve -> matchingPcurves(surfaceCurve.associatedGeometry(), faceGeometry);
            case StepSeamCurve seamCurve -> matchingPcurves(seamCurve.associatedGeometry(), faceGeometry);
            default -> List.of();
        };
        if (pcurves.isEmpty()) {
            if (shouldFallbackToProjectedEdge(edgeGeometry)) {
                List<UvPoint> fallback = projectSampledEdge(orientedEdge, mapper, builder);
                if (fallback != null) {
                    log.debug("stage={} edgeId={}, orientedEdgeId={}, surfaceType={}, edgeGeometryType={}, reason={}", "parametric_edge_sampling_fallback",
                            orientedEdge.edgeElement().id(), orientedEdge.id(),
                                    surfaceTypeName(faceGeometry), surfaceTypeName(edgeGeometry),
                                    "projected sampled 3d edge because no pcurves");
                    return fallback;
                }
            }
            log.debug("stage={} edgeId={}, orientedEdgeId={}, surfaceType={}, edgeGeometryType={}, associatedGeometry={}, reason={}", "parametric_edge_sampling_failed",
                    orientedEdge.edgeElement().id(), orientedEdge.id(),
                            surfaceTypeName(faceGeometry), surfaceTypeName(edgeGeometry),
                            associatedGeometrySummary(edgeGeometry), "no matching pcurves");
            return null;
        }
        UvPoint projectedStart = mapper.project(pointFromStep(startVertex.point()), null);
        UvPoint projectedEnd = mapper.project(pointFromStep(endVertex.point()), projectedStart);
        List<UvPoint> best = null;
        double bestScore = Double.POSITIVE_INFINITY;
        int unsupportedPcurveCount = 0;
        for (StepEntity pcurve : pcurves) {
            Object built;
            try {
                built = builder.buildPcurve2(pcurve.id());
            } catch (UnsupportedGeometryException ex) {
                unsupportedPcurveCount++;
                continue;
            }
            if (built instanceof Line2 line) {
                UvPoint start = snapToLine(projectedStart, line);
                UvPoint end = snapToLine(projectedEnd, line);
                double score = distanceSquared(projectedStart, start) + distanceSquared(projectedEnd, end);
                List<UvPoint> samples = sampleLinePcurve(line, start, end);
                if (best == null || score < bestScore) {
                    best = samples;
                    bestScore = score;
                }
                continue;
            }
            if (built instanceof BSplineCurve2 spline) {
                List<UvPoint> samples = sampleSplinePcurve(spline, projectedStart, projectedEnd);
                if (!samples.isEmpty()) {
                    double score = distanceSquared(projectedStart, samples.getFirst()) + distanceSquared(projectedEnd, samples.getLast());
                    if (best == null || score < bestScore) {
                        best = samples;
                        bestScore = score;
                    }
                }
                continue;
            }
            if (built instanceof Circle2 circle) {
                UvPoint start = snapToCircle(projectedStart, circle);
                UvPoint end = snapToCircle(projectedEnd, circle);
                double score = distanceSquared(projectedStart, start) + distanceSquared(projectedEnd, end);
                List<UvPoint> samples = sampleCirclePcurve(circle, start, end);
                if (!samples.isEmpty() && (best == null || score < bestScore)) {
                    best = samples;
                    bestScore = score;
                }
                continue;
            }
            if (built instanceof Ellipse2 ellipse) {
                UvPoint start = snapToEllipse(projectedStart, ellipse);
                UvPoint end = snapToEllipse(projectedEnd, ellipse);
                double score = distanceSquared(projectedStart, start) + distanceSquared(projectedEnd, end);
                List<UvPoint> samples = sampleEllipsePcurve(ellipse, start, end);
                if (!samples.isEmpty() && (best == null || score < bestScore)) {
                    best = samples;
                    bestScore = score;
                }
                continue;
            }
            if (built instanceof TrimmedCurve2 trimmed) {
                List<UvPoint> samples = sampleTrimmedPcurve(trimmed, projectedStart, projectedEnd);
                if (!samples.isEmpty()) {
                    double score = distanceSquared(projectedStart, samples.getFirst()) + distanceSquared(projectedEnd, samples.getLast());
                    if (best == null || score < bestScore) {
                        best = samples;
                        bestScore = score;
                    }
                }
            }
        }
        if (best == null) {
            List<UvPoint> fallback = projectSampledEdge(orientedEdge, mapper, builder);
            if (fallback != null) {
                log.debug("stage={} edgeId={}, orientedEdgeId={}, surfaceType={}, edgeGeometryType={}, pcurveCount={}, unsupportedPcurveCount={}, reason={}", "parametric_edge_sampling_fallback",
                        orientedEdge.edgeElement().id(), orientedEdge.id(),
                                surfaceTypeName(faceGeometry), surfaceTypeName(edgeGeometry),
                                pcurves.size(), unsupportedPcurveCount,
                                "projected sampled 3d edge after unusable pcurves");
                return fallback;
            }
            log.debug("stage={} edgeId={}, orientedEdgeId={}, surfaceType={}, pcurveCount={}, unsupportedPcurveCount={}, pcurveBasisSurfaces={}, reason={}", "parametric_edge_sampling_failed",
                    orientedEdge.edgeElement().id(), orientedEdge.id(),
                            surfaceTypeName(faceGeometry), pcurves.size(),
                            unsupportedPcurveCount, pcurveBasisSurfaceSummary(pcurves),
                            "no usable pcurve samples");
        }
        return best;
    }

    private static List<UvPoint> projectSampledEdge(
            com.minicad.step.model.StepOrientedEdge orientedEdge,
            ParametricSurfaceMapper mapper,
            StepCadBuilder builder
    ) {
        List<CartesianPoint> sampled = sampleStepOrientedEdge(orientedEdge, builder);
        if (sampled.size() < 2) {
            return null;
        }
        List<UvPoint> points = new ArrayList<>(sampled.size());
        UvPoint previous = null;
        for (CartesianPoint point : sampled) {
            UvPoint uv = mapper.project(point, previous);
            if (uv == null) {
                return null;
            }
            points.add(uv);
            previous = uv;
        }
        return List.copyOf(points);
    }

    private static boolean shouldFallbackToProjectedEdge(StepEntity edgeGeometry) {
        return switch (unwrapAssociatedCurveGeometry(edgeGeometry)) {
            case StepSurfaceCurve surfaceCurve -> surfaceCurve.associatedGeometry().isEmpty();
            case StepSeamCurve seamCurve -> seamCurve.associatedGeometry().isEmpty();
            default -> true;
        };
    }

    private static List<CartesianPoint> sampleStepOrientedEdge(
            com.minicad.step.model.StepOrientedEdge orientedEdge,
            StepCadBuilder builder
    ) {
        StepEdgeCurve edge = orientedEdge.edgeElement();
        CartesianPoint start = pointFromStep(orientedEdge.orientation() ? edge.start().point() : edge.end().point());
        CartesianPoint end = pointFromStep(orientedEdge.orientation() ? edge.end().point() : edge.start().point());
        boolean naturalForward = orientedEdge.orientation() ? edge.sameSense() : !edge.sameSense();
        Curve3 curve = curveForLooseEdge(edge.edgeGeometry(), builder);
        if (curve == null) {
            return List.of();
        }
        try {
            return sampleEdge(start, end, curve, naturalForward);
        } catch (GeometryException ex) {
            return List.of(start, end);
        }
    }

    private static String associatedGeometrySummary(StepEntity edgeGeometry) {
        List<StepEntity> associated = switch (unwrapAssociatedCurveGeometry(edgeGeometry)) {
            case StepSurfaceCurve surfaceCurve -> surfaceCurve.associatedGeometry();
            case StepSeamCurve seamCurve -> seamCurve.associatedGeometry();
            default -> List.of();
        };
        if (associated.isEmpty()) {
            return "[]";
        }
        return associated.stream()
                .map(entity -> surfaceTypeName(entity) + "#" + entity.id())
                .collect(Collectors.joining("|"));
    }

    private static StepEntity unwrapAssociatedCurveGeometry(StepEntity edgeGeometry) {
        StepEntity current = edgeGeometry;
        for (int depth = 0; depth < 16; depth++) {
            if (current instanceof StepOrientedCurve orientedCurve) {
                current = orientedCurve.curveElement();
                continue;
            }
            if (current instanceof StepGeometricReplica replica && "CURVE_REPLICA".equals(replica.entityName())) {
                current = replica.parent();
                continue;
            }
            if (current instanceof StepAnnotationCurveOccurrence occurrence) {
                current = occurrence.item();
                continue;
            }
            if (current instanceof StepDimensionCurve dimensionCurve) {
                current = dimensionCurve.item();
                continue;
            }
            if (current instanceof StepLeaderCurve leaderCurve) {
                current = leaderCurve.item();
                continue;
            }
            if (current instanceof StepProjectionCurve projectionCurve) {
                current = projectionCurve.item();
                continue;
            }
            if (current instanceof StepDraughtingAnnotationOccurrence annotationOccurrence) {
                current = annotationOccurrence.item();
                continue;
            }
            if (current instanceof StepTerminatorSymbol terminatorSymbol) {
                current = terminatorSymbol.annotatedCurve();
                continue;
            }
            return current;
        }
        return current;
    }

    private static String pcurveBasisSurfaceSummary(List<StepEntity> pcurves) {
        return pcurves.stream()
                .map(pcurve -> {
                    if (pcurve instanceof StepPcurve exact) {
                        return "#" + exact.id() + "->#" + exact.basisSurface().id();
                    }
                    if (pcurve instanceof StepDegeneratePcurve degenerate) {
                        return "#" + degenerate.id() + "->#" + degenerate.basisSurface().id();
                    }
                    return "#" + pcurve.id();
                })
                .collect(Collectors.joining("|"));
    }

    private static List<StepEntity> matchingPcurves(List<StepEntity> associatedGeometry, StepEntity faceGeometry) {
        Set<Integer> acceptableSurfaceIds = acceptablePcurveBasisSurfaceIds(faceGeometry);
        List<StepEntity> matches = new ArrayList<>();
        for (StepEntity associated : associatedGeometry) {
            if (associated instanceof StepPcurve pcurve && acceptableSurfaceIds.contains(pcurve.basisSurface().id())) {
                matches.add(pcurve);
            } else if (associated instanceof StepDegeneratePcurve pcurve && acceptableSurfaceIds.contains(pcurve.basisSurface().id())) {
                matches.add(pcurve);
            }
        }
        return List.copyOf(matches);
    }

    private static Set<Integer> acceptablePcurveBasisSurfaceIds(StepEntity faceGeometry) {
        LinkedHashSet<Integer> ids = new LinkedHashSet<>();
        StepEntity current = faceGeometry;
        for (int depth = 0; depth < 16 && current != null; depth++) {
            ids.add(current.id());
            if (current instanceof StepRectangularTrimmedSurface trimmedSurface) {
                current = trimmedSurface.basisSurface();
                continue;
            }
            if (current instanceof StepCurveBoundedSurface boundedSurface) {
                current = boundedSurface.basisSurface();
                continue;
            }
            if (current instanceof StepOrientedSurface orientedSurface) {
                current = orientedSurface.surfaceElement();
                continue;
            }
            if (current instanceof StepOffsetSurface offsetSurface) {
                current = offsetSurface.basisSurface();
                continue;
            }
            if (current instanceof StepGeometricReplica replica && "SURFACE_REPLICA".equals(replica.entityName())) {
                current = replica.parent();
                continue;
            }
            break;
        }
        return Set.copyOf(ids);
    }

    private static UvPoint snapToLine(UvPoint point, Line2 line) {
        com.minicad.geometry2d.Point2 snapped = line.closestPoint(new com.minicad.geometry2d.Point2(point.u(), point.v()));
        return new UvPoint(snapped.x(), snapped.y());
    }

    private static UvPoint snapToCircle(UvPoint point, Circle2 circle) {
        com.minicad.geometry2d.Vector2 offset = new com.minicad.geometry2d.Point2(point.u(), point.v()).subtract(circle.center());
        double norm = offset.norm();
        if (norm <= Epsilon.EPS) {
            com.minicad.geometry2d.Point2 fallback = circle.pointAt(0.0);
            return new UvPoint(fallback.x(), fallback.y());
        }
        com.minicad.geometry2d.Point2 snapped = circle.center().add(offset.scale(circle.radius() / norm));
        return new UvPoint(snapped.x(), snapped.y());
    }

    private static UvPoint snapToEllipse(UvPoint point, Ellipse2 ellipse) {
        double angle = ellipse.angleOf(ellipse.pointAt(ellipse.angleOf(snapEllipseSeed(point, ellipse))));
        com.minicad.geometry2d.Point2 snapped = ellipse.pointAt(angle);
        return new UvPoint(snapped.x(), snapped.y());
    }

    private static List<UvPoint> sampleLinePcurve(Line2 line, UvPoint start, UvPoint end) {
        com.minicad.geometry2d.Point2 startPoint = new com.minicad.geometry2d.Point2(start.u(), start.v());
        com.minicad.geometry2d.Point2 endPoint = new com.minicad.geometry2d.Point2(end.u(), end.v());
        double startParameter = line.parameterOf(startPoint);
        double endParameter = line.parameterOf(endPoint);
        int segments = Math.max(12, (int) Math.ceil(Math.abs(endParameter - startParameter) * 6.0));
        List<UvPoint> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            double parameter = startParameter + (endParameter - startParameter) * index / segments;
            com.minicad.geometry2d.Point2 point = line.pointAt(parameter);
            points.add(new UvPoint(point.x(), point.y()));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return List.copyOf(points);
    }

    private static List<UvPoint> sampleSplinePcurve(BSplineCurve2 spline, UvPoint projectedStart, UvPoint projectedEnd) {
        List<com.minicad.geometry2d.Point2> sampled = spline.sample(48);
        if (sampled.size() < 2) {
            return List.of();
        }
        int startIndex = closestPointIndex(sampled, projectedStart);
        int endIndex = closestPointIndex(sampled, projectedEnd);
        if (startIndex == endIndex) {
            return List.of(projectedStart, projectedEnd);
        }
        List<UvPoint> points = new ArrayList<>();
        int step = startIndex <= endIndex ? 1 : -1;
        for (int index = startIndex; index != endIndex + step; index += step) {
            com.minicad.geometry2d.Point2 point = sampled.get(index);
            points.add(new UvPoint(point.x(), point.y()));
        }
        points.set(0, projectedStart);
        points.set(points.size() - 1, projectedEnd);
        return List.copyOf(points);
    }

    private static List<UvPoint> sampleCirclePcurve(Circle2 circle, UvPoint start, UvPoint end) {
        com.minicad.geometry2d.Point2 startPoint = new com.minicad.geometry2d.Point2(start.u(), start.v());
        com.minicad.geometry2d.Point2 endPoint = new com.minicad.geometry2d.Point2(end.u(), end.v());
        double startAngle = circle.angleOf(startPoint);
        double endAngle = circle.angleOf(endPoint);
        double delta = endAngle - startAngle;
        if (delta > Math.PI) {
            delta -= Math.PI * 2.0;
        } else if (delta < -Math.PI) {
            delta += Math.PI * 2.0;
        }
        int segments = Math.max(18, (int) Math.ceil(Math.abs(delta) * 18.0));
        List<UvPoint> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            double angle = startAngle + delta * index / segments;
            com.minicad.geometry2d.Point2 point = circle.pointAt(angle);
            points.add(new UvPoint(point.x(), point.y()));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return List.copyOf(points);
    }

    private static List<UvPoint> sampleEllipsePcurve(Ellipse2 ellipse, UvPoint start, UvPoint end) {
        com.minicad.geometry2d.Point2 startPoint = new com.minicad.geometry2d.Point2(start.u(), start.v());
        com.minicad.geometry2d.Point2 endPoint = new com.minicad.geometry2d.Point2(end.u(), end.v());
        double startAngle = ellipse.angleOf(startPoint);
        double endAngle = ellipse.angleOf(endPoint);
        double delta = endAngle - startAngle;
        if (delta > Math.PI) {
            delta -= Math.PI * 2.0;
        } else if (delta < -Math.PI) {
            delta += Math.PI * 2.0;
        }
        int segments = Math.max(18, (int) Math.ceil(Math.abs(delta) * 18.0));
        List<UvPoint> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            double angle = startAngle + delta * index / segments;
            com.minicad.geometry2d.Point2 point = ellipse.pointAt(angle);
            points.add(new UvPoint(point.x(), point.y()));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return List.copyOf(points);
    }

    private static List<UvPoint> sampleTrimmedPcurve(TrimmedCurve2 trimmed, UvPoint projectedStart, UvPoint projectedEnd) {
        UvPoint trimStart = new UvPoint(trimmed.trimStart().x(), trimmed.trimStart().y());
        UvPoint trimEnd = new UvPoint(trimmed.trimEnd().x(), trimmed.trimEnd().y());
        List<UvPoint> forward = sampleCurve2(trimmed.basisCurve(), trimStart, trimEnd);
        List<UvPoint> reverse = sampleCurve2(trimmed.basisCurve(), trimEnd, trimStart);
        if (forward.isEmpty() && reverse.isEmpty()) {
            return List.of();
        }
        List<UvPoint> preferred;
        if (!trimmed.senseAgreement()) {
            preferred = reverse.isEmpty() ? forward : reverse;
        } else {
            preferred = score(projectedStart, projectedEnd, forward) <= score(projectedStart, projectedEnd, reverse)
                    ? forward
                    : reverse;
        }
        return alignTrimmedSamples(preferred, projectedStart, projectedEnd);
    }

    private static List<UvPoint> sampleCurve2(com.minicad.geometry2d.Curve2 curve, UvPoint start, UvPoint end) {
        if (curve instanceof Line2 line) {
            return sampleLinePcurve(line, start, end);
        }
        if (curve instanceof Circle2 circle) {
            return sampleCirclePcurve(circle, start, end);
        }
        if (curve instanceof Ellipse2 ellipse) {
            return sampleEllipsePcurve(ellipse, start, end);
        }
        if (curve instanceof BSplineCurve2 spline) {
            return sampleSplinePcurve(spline, start, end);
        }
        if (curve instanceof TrimmedCurve2 trimmed) {
            return sampleTrimmedPcurve(trimmed, start, end);
        }
        return List.of();
    }

    private static double score(UvPoint start, UvPoint end, List<UvPoint> samples) {
        if (samples.isEmpty()) {
            return Double.POSITIVE_INFINITY;
        }
        return distanceSquared(start, samples.getFirst()) + distanceSquared(end, samples.getLast());
    }

    private static List<UvPoint> alignTrimmedSamples(List<UvPoint> samples, UvPoint projectedStart, UvPoint projectedEnd) {
        if (samples.isEmpty()) {
            return samples;
        }
        List<UvPoint> aligned = new ArrayList<>(samples);
        double forwardScore = distanceSquared(projectedStart, aligned.getFirst()) + distanceSquared(projectedEnd, aligned.getLast());
        double reverseScore = distanceSquared(projectedStart, aligned.getLast()) + distanceSquared(projectedEnd, aligned.getFirst());
        if (reverseScore < forwardScore) {
            java.util.Collections.reverse(aligned);
        }
        aligned.set(0, projectedStart);
        aligned.set(aligned.size() - 1, projectedEnd);
        return List.copyOf(aligned);
    }

    private static com.minicad.geometry2d.Point2 snapEllipseSeed(UvPoint point, Ellipse2 ellipse) {
        com.minicad.geometry2d.Vector2 offset = new com.minicad.geometry2d.Point2(point.u(), point.v()).subtract(ellipse.center());
        if (offset.norm() <= Epsilon.EPS) {
            return ellipse.pointAt(0.0);
        }
        com.minicad.geometry2d.Vector2 x = ellipse.xDirection().asVector();
        com.minicad.geometry2d.Vector2 y = new com.minicad.geometry2d.Vector2(-x.y(), x.x());
        double nx = offset.dot(x) / ellipse.semiAxis1();
        double ny = offset.dot(y) / ellipse.semiAxis2();
        double norm = Math.hypot(nx, ny);
        if (norm <= Epsilon.EPS) {
            return ellipse.pointAt(0.0);
        }
        double angle = Math.atan2(ny / norm, nx / norm);
        return ellipse.pointAt(angle);
    }

    private static List<LoopPayload> toParametricLoopPayloads(List<ParametricLoopPayload> loops, ParametricSurfaceMapper mapper) {
        List<LoopPayload> payloads = new ArrayList<>(loops.size());
        for (ParametricLoopPayload loop : loops) {
            List<PointPayload> points = new ArrayList<>(loop.points().size());
            for (UvPoint point : loop.points()) {
                points.add(toPointPayload(mapper.pointAt(point.u(), point.v())));
            }
            payloads.add(new LoopPayload(loop.outer(), List.copyOf(points)));
        }
        return List.copyOf(payloads);
    }

    private static int closestPointIndex(List<com.minicad.geometry2d.Point2> points, UvPoint target) {
        int bestIndex = 0;
        double bestDistance = Double.POSITIVE_INFINITY;
        for (int index = 0; index < points.size(); index++) {
            com.minicad.geometry2d.Point2 point = points.get(index);
            double du = point.x() - target.u();
            double dv = point.y() - target.v();
            double distance = du * du + dv * dv;
            if (distance < bestDistance) {
                bestDistance = distance;
                bestIndex = index;
            }
        }
        return bestIndex;
    }

    private static boolean sameUv(UvPoint left, UvPoint right) {
        return distanceSquared(left, right) <= 1.0e-12;
    }

    private static double distanceSquared(UvPoint left, UvPoint right) {
        double du = left.u() - right.u();
        double dv = left.v() - right.v();
        return du * du + dv * dv;
    }

    private static List<PointPayload> triangulateParametricFace(
            ParametricSurfaceMapper mapper,
            List<ParametricLoopPayload> loops,
            UvBounds bounds,
            int uSegments,
            int vSegments,
            boolean sameSense
    ) {
        ParametricLoopPayload outer = loops.stream().filter(ParametricLoopPayload::outer).findFirst().orElse(null);
        if (outer == null) {
            return List.of();
        }
        List<ParametricLoopPayload> holes = loops.stream().filter(loop -> !loop.outer()).toList();
        List<PointPayload> triangles = new ArrayList<>();
        for (int ui = 0; ui < uSegments; ui++) {
            double u0 = bounds.minU() + bounds.uSpan() * ui / uSegments;
            double u1 = bounds.minU() + bounds.uSpan() * (ui + 1) / uSegments;
            for (int vi = 0; vi < vSegments; vi++) {
                double v0 = bounds.minV() + bounds.vSpan() * vi / vSegments;
                double v1 = bounds.minV() + bounds.vSpan() * (vi + 1) / vSegments;
                UvPoint center = new UvPoint((u0 + u1) * 0.5, (v0 + v1) * 0.5);
                if (!contains(outer.points(), center)) {
                    continue;
                }
                boolean insideHole = false;
                for (ParametricLoopPayload hole : holes) {
                    if (contains(hole.points(), center)) {
                        insideHole = true;
                        break;
                    }
                }
                if (insideHole) {
                    continue;
                }
                CartesianPoint p00 = mapper.pointAt(u0, v0);
                CartesianPoint p10 = mapper.pointAt(u1, v0);
                CartesianPoint p01 = mapper.pointAt(u0, v1);
                CartesianPoint p11 = mapper.pointAt(u1, v1);
                Vector3 normal = mapper.normalAt(center.u(), center.v());
                if (!sameSense) {
                    normal = normal.scale(-1.0);
                }
                appendOrientedTriangle(triangles, p00, p10, p11, normal);
                appendOrientedTriangle(triangles, p00, p11, p01, normal);
            }
        }
        return List.copyOf(triangles);
    }

    private static List<ParametricLoopPayload> normalizeLoopRoles(
            StepFaceEntity stepFace,
            StepEntity geometry,
            List<ParametricLoopPayload> loops
    ) {
        if (loops.isEmpty() || loops.stream().anyMatch(ParametricLoopPayload::outer)) {
            return loops;
        }
        int outerIndex = -1;
        double outerArea = Double.NEGATIVE_INFINITY;
        for (int index = 0; index < loops.size(); index++) {
            double area = Math.abs(signedArea(loops.get(index).points()));
            if (area > outerArea + Epsilon.EPS) {
                outerArea = area;
                outerIndex = index;
            }
        }
        if (outerIndex < 0) {
            return loops;
        }
        log.debug("stage={} faceId={}, surfaceType={}, loopCount={}, inferredOuterIndex={}, inferredOuterArea={}", "parametric_outer_bound_inferred",
                stepFace.id(), surfaceTypeName(geometry), loops.size(), outerIndex, outerArea);
        List<ParametricLoopPayload> normalized = new ArrayList<>(loops.size());
        for (int index = 0; index < loops.size(); index++) {
            normalized.add(new ParametricLoopPayload(index == outerIndex, loops.get(index).points()));
        }
        return List.copyOf(normalized);
    }

    private static double signedArea(List<UvPoint> points) {
        if (points.size() < 3) {
            return 0.0;
        }
        double area = 0.0;
        for (int index = 0; index + 1 < points.size(); index++) {
            UvPoint current = points.get(index);
            UvPoint next = points.get(index + 1);
            area += current.u() * next.v() - next.u() * current.v();
        }
        return area * 0.5;
    }

    private static List<PointPayload> triangulateParametricFaceAdaptive(
            ParametricSurfaceMapper mapper,
            List<ParametricLoopPayload> loops,
            UvBounds bounds,
            int baseUSegments,
            int baseVSegments,
            boolean sameSense
    ) {
        int uSegments = baseUSegments;
        int vSegments = baseVSegments;
        for (int attempt = 0; attempt < 4; attempt++) {
            List<PointPayload> triangles = triangulateParametricFace(mapper, loops, bounds, uSegments, vSegments, sameSense);
            if (!triangles.isEmpty()) {
                return triangles;
            }
            if (uSegments >= 512 && vSegments >= 256) {
                break;
            }
            uSegments = Math.min(uSegments * 2, 512);
            vSegments = Math.min(vSegments * 2, 256);
        }
        return List.of();
    }

    private static boolean contains(List<UvPoint> polygon, UvPoint point) {
        if (polygon.size() < 3) {
            return false;
        }
        if (isOnPolygonBoundary(polygon, point)) {
            return true;
        }
        boolean inside = false;
        for (int i = 0, j = polygon.size() - 1; i < polygon.size(); j = i++) {
            UvPoint a = polygon.get(i);
            UvPoint b = polygon.get(j);
            boolean intersects = ((a.v() > point.v()) != (b.v() > point.v()))
                    && (point.u() < (b.u() - a.u()) * (point.v() - a.v()) / ((b.v() - a.v()) + 1.0e-12) + a.u());
            if (intersects) {
                inside = !inside;
            }
        }
        return inside;
    }

    private static boolean isOnPolygonBoundary(List<UvPoint> polygon, UvPoint point) {
        for (int index = 0; index + 1 < polygon.size(); index++) {
            if (isOnSegment(polygon.get(index), polygon.get(index + 1), point)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isOnSegment(UvPoint a, UvPoint b, UvPoint point) {
        double abU = b.u() - a.u();
        double abV = b.v() - a.v();
        double lengthSquared = abU * abU + abV * abV;
        if (lengthSquared <= 1.0e-18) {
            return distanceSquared(a, point) <= 1.0e-18;
        }
        double apU = point.u() - a.u();
        double apV = point.v() - a.v();
        double cross = abU * apV - abV * apU;
        if (Math.abs(cross) > 1.0e-9) {
            return false;
        }
        double dot = apU * abU + apV * abV;
        if (dot < -1.0e-9) {
            return false;
        }
        return dot <= lengthSquared + 1.0e-9;
    }

    private static ParametricSurfaceMapper mapperForSurface(StepEntity geometry, StepCadBuilder builder) {
        if (geometry instanceof StepRectangularTrimmedSurface trimmedSurface) {
            return mapperForSurface(trimmedSurface.basisSurface(), builder);
        }
        if (geometry instanceof StepCurveBoundedSurface boundedSurface) {
            return mapperForSurface(boundedSurface.basisSurface(), builder);
        }
        if (geometry instanceof StepOrientedSurface orientedSurface) {
            ParametricSurfaceMapper base = mapperForSurface(orientedSurface.surfaceElement(), builder);
            if (base == null) {
                return null;
            }
            if (orientedSurface.orientation()) {
                return base;
            }
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    return base.project(point, previous);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return base.pointAt(u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return base.normalAt(u, v).scale(-1.0);
                }

                @Override
                public Double uPeriod() {
                    return base.uPeriod();
                }

                @Override
                public Double vPeriod() {
                    return base.vPeriod();
                }
            };
        }
        if (geometry instanceof StepOffsetSurface offsetSurface) {
            ParametricSurfaceMapper base = mapperForSurface(offsetSurface.basisSurface(), builder);
            if (base == null) {
                return null;
            }
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    return base.project(point, previous);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    CartesianPoint basePoint = base.pointAt(u, v);
                    Vector3 normal = base.normalAt(u, v);
                    return basePoint.add(normal.scale(offsetSurface.distance()));
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return base.normalAt(u, v);
                }

                @Override
                public Double uPeriod() {
                    return base.uPeriod();
                }

                @Override
                public Double vPeriod() {
                    return base.vPeriod();
                }
            };
        }
        // Elliptical-axis surfaces — CadBuilder approximates these as standard surfaces
        if (geometry instanceof StepCylindricalSurfaceWithEllipticalAxis ellipticalAxis) {
            CylindricalSurface surface = builder.buildCylindricalSurfaceWithEllipticalAxis(ellipticalAxis.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    double u = unwrapPeriodic(cylindricalAngle(surface.position(), point), previous == null ? null : previous.u(), Math.PI * 2.0);
                    return new UvPoint(u, axialHeight(surface.position(), point));
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return surfacePoint(surface, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return cylindricalNormal(surface, u, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepConicalSurfaceWithEllipticalAxis ellipticalAxis) {
            ConicalSurface surface = builder.buildConicalSurfaceWithEllipticalAxis(ellipticalAxis.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    double u = unwrapPeriodic(cylindricalAngle(surface.position(), point), previous == null ? null : previous.u(), Math.PI * 2.0);
                    return new UvPoint(u, axialHeight(surface.position(), point));
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return conicalSurfacePoint(surface, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return conicalNormal(surface, u, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepSphericalSurfaceWithEllipticalAxis ellipticalAxis) {
            SphericalSurface surface = builder.buildSphericalSurfaceWithEllipticalAxis(ellipticalAxis.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    double u = unwrapPeriodic(sphericalU(surface.position(), point), previous == null ? null : previous.u(), Math.PI * 2.0);
                    return new UvPoint(u, sphericalV(surface.position(), point, surface.radius()));
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return sphericalSurfacePoint(surface.position(), surface.radius(), u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return sphericalNormal(surface.position(), u, v, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepToroidalSurfaceWithCylindricalAxis ellipticalAxis) {
            ToroidalSurface surface = builder.buildToroidalSurfaceWithCylindricalAxis(ellipticalAxis.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    Double previousU = previous == null ? null : previous.u();
                    Double previousV = previous == null ? null : previous.v();
                    double u = unwrapPeriodic(toroidalU(surface, point), previousU, Math.PI * 2.0);
                    double v = unwrapPeriodic(toroidalV(surface, point), previousV, Math.PI * 2.0);
                    return new UvPoint(u, v);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return toroidalSurfacePoint(surface, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return toroidalNormal(surface, u, v, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }

                @Override
                public Double vPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepToroidalSurfaceWithEllipticalAxis ellipticalAxis) {
            ToroidalSurface surface = builder.buildToroidalSurfaceWithEllipticalAxis(ellipticalAxis.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    Double previousU = previous == null ? null : previous.u();
                    Double previousV = previous == null ? null : previous.v();
                    double u = unwrapPeriodic(toroidalU(surface, point), previousU, Math.PI * 2.0);
                    double v = unwrapPeriodic(toroidalV(surface, point), previousV, Math.PI * 2.0);
                    return new UvPoint(u, v);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return toroidalSurfacePoint(surface, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return toroidalNormal(surface, u, v, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }

                @Override
                public Double vPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepOffsetSurface2 offsetSurface2) {
            ParametricSurfaceMapper base = mapperForSurface(offsetSurface2.basisSurface(), builder);
            if (base == null) {
                return null;
            }
            double dist = offsetSurface2.sameSense() ? offsetSurface2.distance() : -offsetSurface2.distance();
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    return base.project(point, previous);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    CartesianPoint basePoint = base.pointAt(u, v);
                    Vector3 normal = base.normalAt(u, v);
                    return basePoint.add(normal.scale(dist));
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return base.normalAt(u, v);
                }

                @Override
                public Double uPeriod() {
                    return base.uPeriod();
                }

                @Override
                public Double vPeriod() {
                    return base.vPeriod();
                }
            };
        }
        if (geometry instanceof StepGeometricReplica replica && "SURFACE_REPLICA".equals(replica.entityName())) {
            if (!(replica.transformation() instanceof com.minicad.step.model.StepCartesianTransformationOperator transformation)) {
                return null;
            }
            ParametricSurfaceMapper base = mapperForSurface(replica.parent(), builder);
            if (base == null) {
                return null;
            }
            double[] matrix = matrixForTransformationOperator(transformation, builder);
            double[] inverse = inverseUniformScaleTransform(matrix);
            if (inverse == null) {
                return null;
            }
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    return base.project(transformCartesian(point, inverse), previous);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return transformCartesian(base.pointAt(u, v), matrix);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    VectorPayload transformed = transform(
                            new VectorPayload(base.normalAt(u, v).x(), base.normalAt(u, v).y(), base.normalAt(u, v).z()),
                            matrix
                    );
                    return new Vector3(transformed.x(), transformed.y(), transformed.z());
                }

                @Override
                public Double uPeriod() {
                    return base.uPeriod();
                }

                @Override
                public Double vPeriod() {
                    return base.vPeriod();
                }
            };
        }
        if (geometry instanceof StepPlane stepPlane) {
            Axis2Placement3D placement = builder.buildPlacement(stepPlane.position().id());
            Plane plane = builder.buildPlane(stepPlane.id());
            Direction3 uDirection = placement.xDirection();
            Direction3 vDirection = placement.yDirection();
            CartesianPoint origin = plane.origin();
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    Vector3 offset = point.subtract(origin);
                    return new UvPoint(offset.dot(uDirection.asVector()), offset.dot(vDirection.asVector()));
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return origin
                            .add(uDirection.asVector().scale(u))
                            .add(vDirection.asVector().scale(v));
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return plane.normal().asVector();
                }
            };
        }
        if (geometry instanceof StepCylindricalSurface cylindricalSurface) {
            CylindricalSurface surface = builder.buildCylindricalSurface(cylindricalSurface.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    double u = unwrapPeriodic(cylindricalAngle(surface, point), previous == null ? null : previous.u(), Math.PI * 2.0);
                    return new UvPoint(u, axialHeight(surface, point));
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return surfacePoint(surface, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return cylindricalNormal(surface, u, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepConicalSurface conicalSurface) {
            ConicalSurface surface = builder.buildConicalSurface(conicalSurface.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    double u = unwrapPeriodic(cylindricalAngle(surface.position(), point), previous == null ? null : previous.u(), Math.PI * 2.0);
                    return new UvPoint(u, axialHeight(surface.position(), point));
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return conicalSurfacePoint(surface, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return conicalNormal(surface, u, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepSphericalSurface sphericalSurface) {
            Axis2Placement3D placement = builder.buildPlacement(sphericalSurface.position().id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    double u = unwrapPeriodic(sphericalU(placement, point), previous == null ? null : previous.u(), Math.PI * 2.0);
                    return new UvPoint(u, sphericalV(placement, point, sphericalSurface.radius()));
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return sphericalSurfacePoint(placement, sphericalSurface.radius(), u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return sphericalNormal(placement, u, v, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepDegenerateToroidalSurface degenerateToroidalSurface) {
            Axis2Placement3D placement = builder.buildPlacement(degenerateToroidalSurface.position().id());
            double majorRadius = degenerateToroidalSurface.majorRadius();
            double minorRadius = degenerateToroidalSurface.minorRadius();
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    Double previousU = previous == null ? null : previous.u();
                    Double previousV = previous == null ? null : previous.v();
                    double u = unwrapPeriodic(toroidalU(placement, point), previousU, Math.PI * 2.0);
                    double v = unwrapPeriodic(toroidalV(placement, majorRadius, point), previousV, Math.PI * 2.0);
                    return new UvPoint(u, v);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return toroidalSurfacePoint(placement, majorRadius, minorRadius, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return toroidalNormal(placement, u, v, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }

                @Override
                public Double vPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepToroidalSurface toroidalSurface) {
            ToroidalSurface surface = builder.buildToroidalSurface(toroidalSurface.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    Double previousU = previous == null ? null : previous.u();
                    Double previousV = previous == null ? null : previous.v();
                    double u = unwrapPeriodic(toroidalU(surface, point), previousU, Math.PI * 2.0);
                    double v = unwrapPeriodic(toroidalV(surface, point), previousV, Math.PI * 2.0);
                    return new UvPoint(u, v);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return toroidalSurfacePoint(surface, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return toroidalNormal(surface, u, v, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }

                @Override
                public Double vPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepRationalBSplineSurface splineSurface) {
            RationalBSplineSurface3 surface = builder.buildRationalBSplineSurface(splineSurface.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    return nearestUvOnRationalBSplineSurface(surface, point, previous);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return surface.pointAt(u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return surface.normalAt(u, v);
                }
            };
        }
        if (geometry instanceof StepBSplineSurfaceWithKnots
                || geometry instanceof StepBSplineSurface
                || geometry instanceof StepBSplineSurfaceWithKnotsAndBreakpoints
                || geometry instanceof StepBezierSurface
                || geometry instanceof StepUniformSurface
                || geometry instanceof StepQuasiUniformSurface
                || geometry instanceof StepPiecewiseBezierSurface) {
            BSplineSurface3 surface = buildBsplineSurface(geometry, builder);
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    return nearestUvOnBSplineSurface(surface, point, previous);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return surface.pointAt(u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return surface.normalAt(u, v);
                }
            };
        }
        if (geometry instanceof StepSurfaceOfLinearExtrusion extrusionSurface) {
            return extrusionMapper(extrusionSurface, builder);
        }
        if (geometry instanceof StepSurfaceOfRevolution revolutionSurface) {
            return revolutionMapper(revolutionSurface, builder);
        }
        // Rectangular composite surface: delegate to parent surface mapper
        if (geometry instanceof StepRectangularCompositeSurface compositeSurface) {
            return mapperForSurface(compositeSurface.parentSurface(), builder);
        }
        // Surface patch: delegate to basis surface mapper
        if (geometry instanceof StepSurfacePatch surfacePatch) {
            return mapperForSurface(surfacePatch.basisSurface(), builder);
        }
        // Blended surface: delegate to primary surface mapper
        if (geometry instanceof StepBlendedSurface blended) {
            return mapperForSurface(blended.primarySurface(), builder);
        }
        // Free-form surface: build as BSplineSurface3 and use grid-based parametric mapping
        if (geometry instanceof StepFreeFormSurface freeForm) {
            BSplineSurface3 surface = buildFreeFormSurface(freeForm, builder);
            double uSpan = surface.uEnd() - surface.uStart();
            double vSpan = surface.vEnd() - surface.vStart();
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    double u = previous != null ? previous.u() : surface.uStart() + uSpan * 0.5;
                    double v = previous != null ? previous.v() : surface.vStart() + vSpan * 0.5;
                    return new UvPoint(u, v);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return surface.pointAt(u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return surface.normalAt(u, v);
                }
            };
        }
        return null;
    }

    private static double[] inverseUniformScaleTransform(double[] matrix) {
        double sx = Math.sqrt(matrix[0] * matrix[0] + matrix[4] * matrix[4] + matrix[8] * matrix[8]);
        double sy = Math.sqrt(matrix[1] * matrix[1] + matrix[5] * matrix[5] + matrix[9] * matrix[9]);
        double sz = Math.sqrt(matrix[2] * matrix[2] + matrix[6] * matrix[6] + matrix[10] * matrix[10]);
        if (sx <= 1.0e-12 || sy <= 1.0e-12 || sz <= 1.0e-12) {
            return null;
        }
        double maxScale = Math.max(sx, Math.max(sy, sz));
        double tolerance = maxScale * 1.0e-6;
        if (Math.abs(sx - sy) > tolerance || Math.abs(sx - sz) > tolerance || Math.abs(sy - sz) > tolerance) {
            return null;
        }
        double n01 = ((matrix[0] / sx) * (matrix[1] / sy)) + ((matrix[4] / sx) * (matrix[5] / sy)) + ((matrix[8] / sx) * (matrix[9] / sy));
        double n02 = ((matrix[0] / sx) * (matrix[2] / sz)) + ((matrix[4] / sx) * (matrix[6] / sz)) + ((matrix[8] / sx) * (matrix[10] / sz));
        double n12 = ((matrix[1] / sy) * (matrix[2] / sz)) + ((matrix[5] / sy) * (matrix[6] / sz)) + ((matrix[9] / sy) * (matrix[10] / sz));
        if (Math.abs(n01) > 1.0e-6 || Math.abs(n02) > 1.0e-6 || Math.abs(n12) > 1.0e-6) {
            return null;
        }
        double scale = (sx + sy + sz) / 3.0;
        double scaleSquared = scale * scale;
        if (scaleSquared <= 1.0e-18) {
            return null;
        }
        double tx = matrix[3];
        double ty = matrix[7];
        double tz = matrix[11];
        return new double[]{
                matrix[0] / scaleSquared, matrix[4] / scaleSquared, matrix[8] / scaleSquared,
                -((matrix[0] * tx) + (matrix[4] * ty) + (matrix[8] * tz)) / scaleSquared,
                matrix[1] / scaleSquared, matrix[5] / scaleSquared, matrix[9] / scaleSquared,
                -((matrix[1] * tx) + (matrix[5] * ty) + (matrix[9] * tz)) / scaleSquared,
                matrix[2] / scaleSquared, matrix[6] / scaleSquared, matrix[10] / scaleSquared,
                -((matrix[2] * tx) + (matrix[6] * ty) + (matrix[10] * tz)) / scaleSquared,
                0.0, 0.0, 0.0, 1.0
        };
    }

    private static UvPoint nearestUvOnBSplineSurface(BSplineSurface3 surface, CartesianPoint point, UvPoint previous) {
        double uStart = surface.uStart();
        double uEnd = surface.uEnd();
        double vStart = surface.vStart();
        double vEnd = surface.vEnd();
        boolean hasPrevious = previous != null;

        double bestU = hasPrevious ? clamp(previous.u(), uStart, uEnd) : uStart;
        double bestV = hasPrevious ? clamp(previous.v(), vStart, vEnd) : vStart;
        double bestDistance = surface.pointAt(bestU, bestV).distanceTo(point);

        int uSamples = hasPrevious ? 4 : 12;
        int vSamples = hasPrevious ? 4 : 12;
        double coarseWindowU = (uEnd - uStart) * (hasPrevious ? 0.08 : 0.25);
        double coarseWindowV = (vEnd - vStart) * (hasPrevious ? 0.08 : 0.25);
        double coarseMinU = hasPrevious ? Math.max(uStart, bestU - coarseWindowU) : uStart;
        double coarseMaxU = hasPrevious ? Math.min(uEnd, bestU + coarseWindowU) : uEnd;
        double coarseMinV = hasPrevious ? Math.max(vStart, bestV - coarseWindowV) : vStart;
        double coarseMaxV = hasPrevious ? Math.min(vEnd, bestV + coarseWindowV) : vEnd;

        for (int ui = 0; ui <= uSamples; ui++) {
            double u = coarseMinU + (coarseMaxU - coarseMinU) * ui / (double) uSamples;
            for (int vi = 0; vi <= vSamples; vi++) {
                double v = coarseMinV + (coarseMaxV - coarseMinV) * vi / (double) vSamples;
                double distance = surface.pointAt(u, v).distanceTo(point);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestU = u;
                    bestV = v;
                }
            }
        }

        double windowU = Math.max((uEnd - uStart) * (hasPrevious ? 0.03 : 0.08), 1.0e-5);
        double windowV = Math.max((vEnd - vStart) * (hasPrevious ? 0.03 : 0.08), 1.0e-5);
        int refinements = hasPrevious ? 3 : 4;
        int refinementSamples = hasPrevious ? 4 : 6;
        for (int refinement = 0; refinement < refinements; refinement++) {
            double minU = Math.max(uStart, bestU - windowU);
            double maxU = Math.min(uEnd, bestU + windowU);
            double minV = Math.max(vStart, bestV - windowV);
            double maxV = Math.min(vEnd, bestV + windowV);
            for (int ui = 0; ui <= refinementSamples; ui++) {
                double u = minU + (maxU - minU) * ui / (double) refinementSamples;
                for (int vi = 0; vi <= refinementSamples; vi++) {
                    double v = minV + (maxV - minV) * vi / (double) refinementSamples;
                    double distance = surface.pointAt(u, v).distanceTo(point);
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        bestU = u;
                        bestV = v;
                    }
                }
            }
            if (bestDistance <= 1.0e-6) {
                break;
            }
            windowU *= 0.5;
            windowV *= 0.5;
        }
        return new UvPoint(bestU, bestV);
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private static ParametricSurfaceMapper extrusionMapper(
            StepSurfaceOfLinearExtrusion extrusionSurface,
            StepCadBuilder builder
    ) {
        CurveEvaluator directrix = curveEvaluator(extrusionSurface.sweptCurve(), builder);
        if (directrix == null) {
            return null;
        }
        Vector3 extrusionDirection = builder.buildVector(extrusionSurface.extrusionAxis().id()).normalize().asVector();
        return new ParametricSurfaceMapper() {
            @Override
            public UvPoint project(CartesianPoint point, UvPoint previous) {
                Vector3 offset = point.subtract(directrix.pointAt(directrix.start()));
                double v = offset.dot(extrusionDirection);
                CartesianPoint basePoint = point.add(extrusionDirection.scale(-v));
                double u = closestParameter(directrix, basePoint, previous == null ? null : previous.u());
                return new UvPoint(u, v);
            }

            @Override
            public CartesianPoint pointAt(double u, double v) {
                return directrix.pointAt(u).add(extrusionDirection.scale(v));
            }

            @Override
            public Vector3 normalAt(double u, double v) {
                Vector3 tangent = directrix.tangentAt(u);
                Vector3 normal = tangent.cross(extrusionDirection);
                if (normal.norm() <= Epsilon.EPS) {
                    normal = fallbackNormal(extrusionDirection);
                }
                return normal.normalize().asVector();
            }
        };
    }

    private static ParametricSurfaceMapper revolutionMapper(
            StepSurfaceOfRevolution revolutionSurface,
            StepCadBuilder builder
    ) {
        CurveEvaluator directrix = curveEvaluator(revolutionSurface.sweptCurve(), builder);
        if (directrix == null) {
            return null;
        }
        StepCadBuilder.Axis1Placement axisPlacement = builder.buildAxis1Placement(revolutionSurface.axisPosition().id());
        Direction3 axisDirection = axisPlacement.axis();
        CartesianPoint axisOrigin = axisPlacement.location();
        Direction3 radialReference = revolutionReferenceDirection(directrix, axisOrigin, axisDirection);
        Direction3 tangentialReference = Direction3.from(axisDirection.asVector().cross(radialReference.asVector()));
        return new ParametricSurfaceMapper() {
            @Override
            public UvPoint project(CartesianPoint point, UvPoint previous) {
                Vector3 offset = point.subtract(axisOrigin);
                double v = unwrapPeriodic(
                        Math.atan2(offset.dot(tangentialReference.asVector()), offset.dot(radialReference.asVector())),
                        previous == null ? null : previous.v(),
                        Math.PI * 2.0
                );
                CartesianPoint meridianPoint = toRevolutionMeridianPoint(point, axisOrigin, axisDirection, radialReference);
                double u = closestParameter(directrix, meridianPoint, previous == null ? null : previous.u());
                return new UvPoint(u, v);
            }

            @Override
            public CartesianPoint pointAt(double u, double v) {
                return revolveAroundAxis(directrix.pointAt(u), axisOrigin, axisDirection, radialReference, tangentialReference, v);
            }

            @Override
            public Vector3 normalAt(double u, double v) {
                Vector3 tangentU = tangentAlongRevolutionDirectrix(
                        directrix,
                        axisOrigin,
                        axisDirection,
                        radialReference,
                        tangentialReference,
                        u,
                        v
                );
                Vector3 tangentV = tangentAroundRevolution(
                        axisOrigin,
                        axisDirection,
                        radialReference,
                        tangentialReference,
                        directrix.pointAt(u),
                        v
                );
                Vector3 normal = tangentU.cross(tangentV);
                if (normal.norm() <= Epsilon.EPS) {
                    normal = fallbackNormal(axisDirection.asVector());
                }
                return normal.normalize().asVector();
            }

            @Override
            public Double vPeriod() {
                return Math.PI * 2.0;
            }
        };
    }

    private static CurveEvaluator curveEvaluator(StepEntity curve, StepCadBuilder builder) {
        return switch (curve) {
            case StepLine line -> {
                Line3 geometry = builder.buildLine(line.id());
                yield new CurveEvaluator() {
                    @Override
                    public double start() {
                        return -1.0;
                    }

                    @Override
                    public double end() {
                        return 1.0;
                    }

                    @Override
                    public CartesianPoint pointAt(double parameter) {
                        return geometry.pointAt(parameter);
                    }
                };
            }
            case StepCircle circle -> {
                Circle geometry = builder.buildCircle(circle.id());
                yield new CurveEvaluator() {
                    @Override
                    public double start() {
                        return 0.0;
                    }

                    @Override
                    public double end() {
                        return Math.PI * 2.0;
                    }

                    @Override
                    public CartesianPoint pointAt(double parameter) {
                        return geometry.pointAt(parameter);
                    }
                };
            }
            case StepEllipse ellipse -> {
                Ellipse3 geometry = builder.buildEllipse(ellipse.id());
                yield new CurveEvaluator() {
                    @Override
                    public double start() {
                        return 0.0;
                    }

                    @Override
                    public double end() {
                        return Math.PI * 2.0;
                    }

                    @Override
                    public CartesianPoint pointAt(double parameter) {
                        return geometry.pointAt(parameter);
                    }
                };
            }
            case StepBSplineCurveWithKnots spline -> {
                BSplineCurve3 geometry = builder.buildBSplineCurve(spline.id());
                yield new CurveEvaluator() {
                    @Override
                    public double start() {
                        return geometry.startParameter();
                    }

                    @Override
                    public double end() {
                        return geometry.endParameter();
                    }

                    @Override
                    public CartesianPoint pointAt(double parameter) {
                        return geometry.pointAt(parameter);
                    }
                };
            }
            case StepTrimmedCurve trimmedCurve -> curveEvaluator(trimmedCurve.basisCurve(), builder);
            case StepSurfaceCurve surfaceCurve -> curveEvaluator(surfaceCurve.curve3d(), builder);
            case StepRationalBSplineCurve spline -> {
                com.minicad.geometry.RationalBSplineCurve3 geometry = builder.buildRationalBSplineCurve(spline.id());
                yield new CurveEvaluator() {
                    @Override public double start() { return geometry.startParameter(); }
                    @Override public double end() { return geometry.endParameter(); }
                    @Override public CartesianPoint pointAt(double parameter) { return geometry.pointAt(parameter); }
                };
            }
            case StepPolyline polyline -> {
                Polyline3 geometry = builder.buildPolyline(polyline.id());
                yield new CurveEvaluator() {
                    @Override public double start() { return 0.0; }
                    @Override public double end() { return 1.0; }
                    @Override public CartesianPoint pointAt(double parameter) { return geometry.pointAt(parameter); }
                };
            }
            case com.minicad.step.model.StepCompositeCurve compositeCurve -> {
                CompositeCurve3 geometry = builder.buildCompositeCurve(compositeCurve.id());
                yield sampledCurveEvaluator(geometry);
            }
            case StepBezierCurve bezier -> sampledCurveEvaluator(builder.buildCurveReference3(bezier.id()));
            case StepUniformCurve uniform -> sampledCurveEvaluator(builder.buildCurveReference3(uniform.id()));
            case StepQuasiUniformCurve quasiUniform -> sampledCurveEvaluator(builder.buildCurveReference3(quasiUniform.id()));
            case StepPiecewiseBezierCurve piecewiseBezier -> sampledCurveEvaluator(builder.buildCurveReference3(piecewiseBezier.id()));
            case StepOffsetCurve3D offsetCurve3D -> sampledCurveEvaluator(builder.buildOffsetCurve3(offsetCurve3D.id()));
            case StepConicCurve conic -> {
                List<CartesianPoint> points = sampleConicCurvePoints(conic, builder);
                if (points == null || points.size() < 2) yield null;
                yield sampledCurveEvaluator(new Polyline3(points));
            }
            case StepOrientedCurve orientedCurve -> curveEvaluator(orientedCurve.curveElement(), builder);
            case StepGeometricReplica replica -> curveEvaluator(replica.parent(), builder);
            case StepBSplineCurve bspline -> sampledCurveEvaluator(builder.buildCurveReference3(bspline.id()));
            case StepSeamCurve seamCurve -> sampledCurveEvaluator(builder.buildSeamCurve(seamCurve.id()).curve3d());
            case StepCircle2D circle2D -> sampledCurveEvaluator(builder.buildCurve3From2D(circle2D.id()));
            case StepEllipse2D ellipse2D -> sampledCurveEvaluator(builder.buildCurve3From2D(ellipse2D.id()));
            case StepPolyline2D polyline2D -> sampledCurveEvaluator(builder.buildCurve3From2D(polyline2D.id()));
            case StepTrimmedCurve2D trimmedCurve2D -> sampledCurveEvaluator(builder.buildCurve3From2D(trimmedCurve2D.id()));
            case StepCompositeCurve2D compositeCurve2D -> sampledCurveEvaluator(builder.buildCurve3From2D(compositeCurve2D.id()));
            case StepBezierCurve2D bezier2D -> sampledCurveEvaluator(builder.buildCurve3From2D(bezier2D.id()));
            case StepQuasiUniformCurve2D quasiUniform2D -> sampledCurveEvaluator(builder.buildCurve3From2D(quasiUniform2D.id()));
            case StepUniformCurve2D uniform2D -> sampledCurveEvaluator(builder.buildCurve3From2D(uniform2D.id()));
            case StepPiecewiseBezierCurve2D piecewiseBezier2D -> sampledCurveEvaluator(builder.buildCurve3From2D(piecewiseBezier2D.id()));
            case StepIndexedPolyCurve2D polyCurve2D -> sampledCurveEvaluator(builder.buildCurve3From2D(polyCurve2D.id()));
            case StepDegenerateCurve2D degenerateCurve2D -> sampledCurveEvaluator(builder.buildCurve3From2D(degenerateCurve2D.id()));
            case StepBSplineCurve2D bspline2D -> sampledCurveEvaluator(builder.buildCurve3From2D(bspline2D.id()));
            case StepRationalBSplineCurve2D rationalBspline2D -> sampledCurveEvaluator(builder.buildCurve3From2D(rationalBspline2D.id()));
            case StepLine2D line2D -> sampledCurveEvaluator(builder.buildCurve3From2D(line2D.id()));
            case StepCurve2D curve2D -> sampledCurveEvaluator(builder.buildCurve3From2D(curve2D.id()));
            case StepHyperbola2D hyperbola2D -> sampledCurveEvaluator(builder.buildCurve3From2D(hyperbola2D.id()));
            case StepParabola2D parabola2D -> sampledCurveEvaluator(builder.buildCurve3From2D(parabola2D.id()));
            case StepOffsetCurve2D offsetCurve2D -> sampledCurveEvaluator(builder.buildCurve3From2D(offsetCurve2D.id()));
            case StepClothoid clothoid -> sampledCurveEvaluator(builder.buildCurveReference3(clothoid.id()));
            case StepIndexedPolyCurve polyCurve -> sampledCurveEvaluator(builder.buildCurveReference3(polyCurve.id()));
            case StepDegenerateCurve degenerate -> sampledCurveEvaluator(builder.buildCurveReference3(degenerate.id()));
            case StepBSplineCurveWithKnotsAndBreakpoints splineBreak -> sampledCurveEvaluator(builder.buildBSplineCurveWithBreakpoints(splineBreak.id()));
            case StepCompositeCurveOnSurface compositeOnSurface -> sampledCurveEvaluator(builder.buildCurveReference3(compositeOnSurface.id()));
            case StepCompositeCurveOnSurface3D compositeOnSurface3D -> sampledCurveEvaluator(builder.buildCurveReference3(compositeOnSurface3D.id()));
            case StepLineSegment lineSeg -> {
                List<CartesianPoint> pts = List.of(
                        builder.buildPoint(lineSeg.startPoint().id()),
                        builder.buildPoint(lineSeg.endPoint().id())
                );
                yield sampledCurveEvaluator(new Polyline3(pts));
            }
            case StepPath path -> sampledCurveEvaluator(builder.buildPath(path.id()));
            case StepOpenPath openPath -> sampledCurveEvaluator(builder.buildPath(openPath.id()));
            case StepSubpath subpath -> sampledCurveEvaluator(builder.buildPath(subpath.id()));
            case StepOrientedPath orientedPath -> sampledCurveEvaluator(builder.buildPath(orientedPath.id()));
            case StepEdgeCurve edgeCurve -> sampledCurveEvaluator(builder.buildCurveReference3(edgeCurve.id()));
            case StepSurfacedEdgeCurve surfacedEdge -> sampledCurveEvaluator(builder.buildCurveReference3(surfacedEdge.id()));
            case StepAnnotationCurveOccurrence occurrence -> curveEvaluator(occurrence.item(), builder);
            case StepDimensionCurve dimensionCurve -> curveEvaluator(dimensionCurve.item(), builder);
            case StepLeaderCurve leaderCurve -> curveEvaluator(leaderCurve.item(), builder);
            case StepProjectionCurve projectionCurve -> curveEvaluator(projectionCurve.item(), builder);
            case StepDraughtingAnnotationOccurrence annotationOccurrence -> curveEvaluator(annotationOccurrence.item(), builder);
            case StepTerminatorSymbol terminatorSymbol -> curveEvaluator(terminatorSymbol.annotatedCurve(), builder);
            case StepCurve abstractCurve -> sampledCurveEvaluator(builder.buildCurveReference3(abstractCurve.id()));
            case StepBoundedCurve boundedCurve -> sampledCurveEvaluator(builder.buildCurveReference3(boundedCurve.id()));
            case StepMappedItem mappedItem -> curveEvaluator(mappedItem.mappingTarget(), builder);
            default -> null;
        };
    }

    private static CurveEvaluator sampledCurveEvaluator(Curve3 curve) {
        List<CartesianPoint> points = curve.sample(128);
        if (points.size() < 2) return null;
        return new CurveEvaluator() {
            @Override public double start() { return 0.0; }
            @Override public double end() { return 1.0; }
            @Override
            public CartesianPoint pointAt(double parameter) {
                double t = Math.max(0, Math.min(1, parameter));
                double idx = t * (points.size() - 1);
                int i0 = (int) idx;
                int i1 = Math.min(i0 + 1, points.size() - 1);
                double f = idx - i0;
                CartesianPoint p0 = points.get(i0);
                CartesianPoint p1 = points.get(i1);
                return new CartesianPoint(
                        p0.x() + (p1.x() - p0.x()) * f,
                        p0.y() + (p1.y() - p0.y()) * f,
                        p0.z() + (p1.z() - p0.z()) * f
                );
            }
        };
    }

    private static double closestParameter(CurveEvaluator curve, CartesianPoint point, Double preferred) {
        int coarseSegments = 160;
        double start = curve.start();
        double end = curve.end();
        double bestParameter = start;
        double bestDistance = Double.POSITIVE_INFINITY;
        for (int index = 0; index <= coarseSegments; index++) {
            double parameter = start + (end - start) * index / coarseSegments;
            double distance = curve.pointAt(parameter).distanceTo(point);
            if (distance < bestDistance) {
                bestDistance = distance;
                bestParameter = parameter;
            }
        }
        if (preferred != null && preferred >= start && preferred <= end) {
            double preferredDistance = curve.pointAt(preferred).distanceTo(point);
            if (preferredDistance <= bestDistance * 1.25) {
                bestDistance = preferredDistance;
                bestParameter = preferred;
            }
        }
        double window = Math.max((end - start) / coarseSegments, 1.0e-6);
        for (int refinement = 0; refinement < 5; refinement++) {
            double min = Math.max(start, bestParameter - window);
            double max = Math.min(end, bestParameter + window);
            for (int index = 0; index <= 12; index++) {
                double parameter = min + (max - min) * index / 12.0;
                double distance = curve.pointAt(parameter).distanceTo(point);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestParameter = parameter;
                }
            }
            window *= 0.35;
        }
        return bestParameter;
    }

    private static Direction3 revolutionReferenceDirection(
            CurveEvaluator directrix,
            CartesianPoint axisOrigin,
            Direction3 axisDirection
    ) {
        for (CartesianPoint sample : directrix.sample(96)) {
            Vector3 radial = radialComponent(sample, axisOrigin, axisDirection);
            if (radial.norm() > Epsilon.EPS) {
                return Direction3.from(radial);
            }
        }
        Vector3 axis = axisDirection.asVector();
        Vector3 seed = Math.abs(axis.x()) < 0.9 ? new Vector3(1.0, 0.0, 0.0) : new Vector3(0.0, 0.0, 1.0);
        Vector3 radial = seed.subtract(axis.scale(seed.dot(axis)));
        return Direction3.from(radial);
    }

    private static CartesianPoint toRevolutionMeridianPoint(
            CartesianPoint point,
            CartesianPoint axisOrigin,
            Direction3 axisDirection,
            Direction3 radialReference
    ) {
        Vector3 offset = point.subtract(axisOrigin);
        double axisCoordinate = offset.dot(axisDirection.asVector());
        Vector3 radial = radialComponent(point, axisOrigin, axisDirection);
        double radius = radial.norm();
        return axisOrigin
                .add(axisDirection.asVector().scale(axisCoordinate))
                .add(radialReference.asVector().scale(radius));
    }

    private static CartesianPoint revolveAroundAxis(
            CartesianPoint point,
            CartesianPoint axisOrigin,
            Direction3 axisDirection,
            Direction3 radialReference,
            Direction3 tangentialReference,
            double angle
    ) {
        Vector3 offset = point.subtract(axisOrigin);
        double axisCoordinate = offset.dot(axisDirection.asVector());
        double radius = radialComponent(point, axisOrigin, axisDirection).norm();
        Vector3 rotated = radialReference.asVector().scale(Math.cos(angle) * radius)
                .add(tangentialReference.asVector().scale(Math.sin(angle) * radius))
                .add(axisDirection.asVector().scale(axisCoordinate));
        return axisOrigin.add(rotated);
    }

    private static Vector3 tangentAlongRevolutionDirectrix(
            CurveEvaluator directrix,
            CartesianPoint axisOrigin,
            Direction3 axisDirection,
            Direction3 radialReference,
            Direction3 tangentialReference,
            double u,
            double v
    ) {
        double span = Math.max(directrix.end() - directrix.start(), 1.0);
        double step = Math.max(span * 1.0e-4, 1.0e-5);
        double u0 = Math.max(directrix.start(), u - step);
        double u1 = Math.min(directrix.end(), u + step);
        if (u1 - u0 <= Epsilon.EPS) {
            u0 = Math.max(directrix.start(), u - step * 2.0);
            u1 = Math.min(directrix.end(), u + step * 2.0);
        }
        CartesianPoint p0 = revolveAroundAxis(directrix.pointAt(u0), axisOrigin, axisDirection, radialReference, tangentialReference, v);
        CartesianPoint p1 = revolveAroundAxis(directrix.pointAt(u1), axisOrigin, axisDirection, radialReference, tangentialReference, v);
        return p1.subtract(p0);
    }

    private static Vector3 tangentAroundRevolution(
            CartesianPoint axisOrigin,
            Direction3 axisDirection,
            Direction3 radialReference,
            Direction3 tangentialReference,
            CartesianPoint point,
            double angle
    ) {
        CartesianPoint rotated = revolveAroundAxis(point, axisOrigin, axisDirection, radialReference, tangentialReference, angle);
        Vector3 radial = radialComponent(rotated, axisOrigin, axisDirection);
        return axisDirection.asVector().cross(radial);
    }

    private static Vector3 radialComponent(CartesianPoint point, CartesianPoint axisOrigin, Direction3 axisDirection) {
        Vector3 offset = point.subtract(axisOrigin);
        return offset.subtract(axisDirection.asVector().scale(offset.dot(axisDirection.asVector())));
    }

    private static Vector3 fallbackNormal(Vector3 preferredAxis) {
        Vector3 seed = Math.abs(preferredAxis.x()) < 0.9 ? new Vector3(1.0, 0.0, 0.0) : new Vector3(0.0, 1.0, 0.0);
        Vector3 normal = preferredAxis.cross(seed);
        if (normal.norm() <= Epsilon.EPS) {
            normal = preferredAxis.cross(new Vector3(0.0, 0.0, 1.0));
        }
        return normal.norm() <= Epsilon.EPS ? new Vector3(0.0, 0.0, 1.0) : normal;
    }

    private static double unwrapPeriodic(double value, Double previous, double period) {
        if (previous == null) {
            return value;
        }
        while (value - previous > period * 0.5) {
            value -= period;
        }
        while (value - previous < -period * 0.5) {
            value += period;
        }
        return value;
    }

    private static SurfacePatch buildFourSidedPatch(EdgeLoop outerLoop) {
        if (outerLoop.edges().size() != 4) {
            return null;
        }
        List<CartesianPoint> bottom = sampleOrientedEdge(outerLoop.edges().get(0));
        List<CartesianPoint> right = sampleOrientedEdge(outerLoop.edges().get(1));
        List<CartesianPoint> top = reversed(sampleOrientedEdge(outerLoop.edges().get(2)));
        List<CartesianPoint> left = reversed(sampleOrientedEdge(outerLoop.edges().get(3)));
        if (!cornersMatch(bottom, right, top, left)) {
            return null;
        }
        int uSegments = Math.max(Math.max(bottom.size(), top.size()) - 1, 8);
        int vSegments = Math.max(Math.max(left.size(), right.size()) - 1, 8);
        return new SurfacePatch(
                resamplePolyline(bottom, uSegments),
                resamplePolyline(top, uSegments),
                resamplePolyline(left, vSegments),
                resamplePolyline(right, vSegments)
        );
    }

    private static boolean cornersMatch(
            List<CartesianPoint> bottom,
            List<CartesianPoint> right,
            List<CartesianPoint> top,
            List<CartesianPoint> left
    ) {
        return close(bottom.getFirst(), left.getFirst())
                && close(bottom.getLast(), right.getFirst())
                && close(top.getFirst(), left.getLast())
                && close(top.getLast(), right.getLast());
    }

    private static boolean close(CartesianPoint left, CartesianPoint right) {
        return left.distanceTo(right) <= 1.0e-6;
    }

    private static List<CartesianPoint> reversed(List<CartesianPoint> points) {
        List<CartesianPoint> copy = new ArrayList<>(points);
        java.util.Collections.reverse(copy);
        return List.copyOf(copy);
    }

    private static List<CartesianPoint> resamplePolyline(List<CartesianPoint> points, int segments) {
        if (points.size() < 2) {
            return List.of(points.getFirst());
        }
        List<Double> lengths = new ArrayList<>(points.size());
        lengths.add(0.0);
        for (int i = 1; i < points.size(); i++) {
            lengths.add(lengths.get(i - 1) + points.get(i - 1).distanceTo(points.get(i)));
        }
        double total = lengths.getLast();
        if (total <= Epsilon.EPS) {
            return java.util.Collections.nCopies(segments + 1, points.getFirst());
        }
        List<CartesianPoint> result = new ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double target = total * i / segments;
            result.add(pointAtDistance(points, lengths, target));
        }
        result.set(0, points.getFirst());
        result.set(result.size() - 1, points.getLast());
        return List.copyOf(result);
    }

    private static CartesianPoint pointAtDistance(List<CartesianPoint> points, List<Double> lengths, double target) {
        for (int i = 1; i < lengths.size(); i++) {
            if (target <= lengths.get(i)) {
                double start = lengths.get(i - 1);
                double segment = lengths.get(i) - start;
                double alpha = segment <= Epsilon.EPS ? 0.0 : (target - start) / segment;
                return interpolate(points.get(i - 1), points.get(i), alpha);
            }
        }
        return points.getLast();
    }

    private static CartesianPoint interpolate(CartesianPoint a, CartesianPoint b, double alpha) {
        return new CartesianPoint(
                a.x() * (1.0 - alpha) + b.x() * alpha,
                a.y() * (1.0 - alpha) + b.y() * alpha,
                a.z() * (1.0 - alpha) + b.z() * alpha
        );
    }

    private static void appendOrientedTriangle(
            List<PointPayload> triangles,
            CartesianPoint a,
            CartesianPoint b,
            CartesianPoint c,
            Vector3 targetNormal
    ) {
        Vector3 normal = b.subtract(a).cross(c.subtract(a));
        if (normal.dot(targetNormal) < 0.0) {
            triangles.add(toPointPayload(a));
            triangles.add(toPointPayload(c));
            triangles.add(toPointPayload(b));
            return;
        }
        triangles.add(toPointPayload(a));
        triangles.add(toPointPayload(b));
        triangles.add(toPointPayload(c));
    }

    private static List<Double> unwrapAngles(CylindricalSurface surface, List<CartesianPoint> points) {
        return unwrapAngles(surface.position(), points);
    }

    private static List<Double> unwrapAngles(Axis2Placement3D placement, List<CartesianPoint> points) {
        List<Double> angles = new ArrayList<>(points.size());
        for (CartesianPoint point : points) {
            double angle = cylindricalAngle(placement, point);
            if (!angles.isEmpty()) {
                double previous = angles.getLast();
                while (angle - previous > Math.PI) {
                    angle -= Math.PI * 2.0;
                }
                while (angle - previous < -Math.PI) {
                    angle += Math.PI * 2.0;
                }
            }
            angles.add(angle);
        }
        return List.copyOf(angles);
    }

    private static double averageAxialHeight(CylindricalSurface surface, List<CartesianPoint> points) {
        return averageAxialHeight(surface.position(), points);
    }

    private static double averageAxialHeight(Axis2Placement3D placement, List<CartesianPoint> points) {
        double total = 0.0;
        for (CartesianPoint point : points) {
            total += axialHeight(placement, point);
        }
        return total / points.size();
    }

    private static double axialHeight(CylindricalSurface surface, CartesianPoint point) {
        return axialHeight(surface.position(), point);
    }

    private static double axialHeight(Axis2Placement3D placement, CartesianPoint point) {
        return point.subtract(placement.location()).dot(placement.axis().asVector());
    }

    private static double cylindricalAngle(CylindricalSurface surface, CartesianPoint point) {
        return cylindricalAngle(surface.position(), point);
    }

    private static double cylindricalAngle(Axis2Placement3D placement, CartesianPoint point) {
        Vector3 offset = point.subtract(placement.location());
        double x = offset.dot(placement.xDirection().asVector());
        double y = offset.dot(placement.yDirection().asVector());
        return Math.atan2(y, x);
    }

    private static CartesianPoint surfacePoint(CylindricalSurface surface, double angle, double height) {
        Axis2Placement3D placement = surface.position();
        Vector3 radial = placement.xDirection().asVector().scale(Math.cos(angle) * surface.radius())
                .add(placement.yDirection().asVector().scale(Math.sin(angle) * surface.radius()));
        Vector3 axial = placement.axis().asVector().scale(height);
        return placement.location().add(radial.add(axial));
    }

    private static Vector3 cylindricalNormal(CylindricalSurface surface, double angle, boolean sameSense) {
        Axis2Placement3D placement = surface.position();
        Vector3 radial = placement.xDirection().asVector().scale(Math.cos(angle))
                .add(placement.yDirection().asVector().scale(Math.sin(angle)));
        return sameSense ? radial : radial.scale(-1.0);
    }

    private static CartesianPoint conicalSurfacePoint(ConicalSurface surface, double angle, double height) {
        Axis2Placement3D placement = surface.position();
        double radius = surface.radius() + height * Math.tan(surface.semiAngle());
        Vector3 radial = placement.xDirection().asVector().scale(Math.cos(angle) * radius)
                .add(placement.yDirection().asVector().scale(Math.sin(angle) * radius));
        Vector3 axial = placement.axis().asVector().scale(height);
        return placement.location().add(radial.add(axial));
    }

    private static Vector3 conicalNormal(ConicalSurface surface, double angle, boolean sameSense) {
        Axis2Placement3D placement = surface.position();
        double slope = Math.tan(surface.semiAngle());
        Vector3 radial = placement.xDirection().asVector().scale(Math.cos(angle))
                .add(placement.yDirection().asVector().scale(Math.sin(angle)));
        Vector3 normal = radial.subtract(placement.axis().asVector().scale(slope));
        return sameSense ? normal.normalize().asVector() : normal.normalize().reverse().asVector();
    }

    private static double sphericalU(Axis2Placement3D placement, CartesianPoint point) {
        Vector3 offset = point.subtract(placement.location());
        double x = offset.dot(placement.xDirection().asVector());
        double y = offset.dot(placement.yDirection().asVector());
        return Math.atan2(y, x);
    }

    private static double sphericalV(Axis2Placement3D placement, CartesianPoint point, double radius) {
        Vector3 offset = point.subtract(placement.location());
        double z = offset.dot(placement.axis().asVector());
        double normalized = radius <= 1.0e-12 ? 0.0 : z / radius;
        normalized = Math.max(-1.0, Math.min(1.0, normalized));
        return Math.asin(normalized);
    }

    private static CartesianPoint sphericalSurfacePoint(Axis2Placement3D placement, double radius, double u, double v) {
        double cosV = Math.cos(v);
        Vector3 normal = placement.xDirection().asVector().scale(Math.cos(u) * cosV)
                .add(placement.yDirection().asVector().scale(Math.sin(u) * cosV))
                .add(placement.axis().asVector().scale(Math.sin(v)));
        return placement.location().add(normal.scale(radius));
    }

    private static Vector3 sphericalNormal(Axis2Placement3D placement, double u, double v, boolean sameSense) {
        double cosV = Math.cos(v);
        Vector3 normal = placement.xDirection().asVector().scale(Math.cos(u) * cosV)
                .add(placement.yDirection().asVector().scale(Math.sin(u) * cosV))
                .add(placement.axis().asVector().scale(Math.sin(v)));
        return sameSense ? normal.normalize().asVector() : normal.normalize().reverse().asVector();
    }

    private static CartesianPoint toroidalSurfacePoint(ToroidalSurface surface, double u, double v) {
        return toroidalSurfacePoint(surface.position(), surface.majorRadius(), surface.minorRadius(), u, v);
    }

    private static CartesianPoint toroidalSurfacePoint(
            Axis2Placement3D placement,
            double majorRadius,
            double minorRadius,
            double u,
            double v
    ) {
        double radial = majorRadius + minorRadius * Math.cos(v);
        Vector3 xy = placement.xDirection().asVector().scale(Math.cos(u) * radial)
                .add(placement.yDirection().asVector().scale(Math.sin(u) * radial));
        Vector3 z = placement.axis().asVector().scale(minorRadius * Math.sin(v));
        return placement.location().add(xy.add(z));
    }

    private static Vector3 toroidalNormal(ToroidalSurface surface, double u, double v, boolean sameSense) {
        return toroidalNormal(surface.position(), u, v, sameSense);
    }

    private static Vector3 toroidalNormal(Axis2Placement3D placement, double u, double v, boolean sameSense) {
        Vector3 normal = placement.xDirection().asVector().scale(Math.cos(u) * Math.cos(v))
                .add(placement.yDirection().asVector().scale(Math.sin(u) * Math.cos(v)))
                .add(placement.axis().asVector().scale(Math.sin(v)));
        return sameSense ? normal.normalize().asVector() : normal.normalize().reverse().asVector();
    }

    private static List<Double> unwrapToroidalU(ToroidalSurface surface, List<CartesianPoint> points) {
        List<Double> values = new ArrayList<>(points.size());
        for (CartesianPoint point : points) {
            double value = toroidalU(surface, point);
            if (!values.isEmpty()) {
                double previous = values.getLast();
                while (value - previous > Math.PI) {
                    value -= Math.PI * 2.0;
                }
                while (value - previous < -Math.PI) {
                    value += Math.PI * 2.0;
                }
            }
            values.add(value);
        }
        return List.copyOf(values);
    }

    private static List<Double> unwrapToroidalV(ToroidalSurface surface, List<CartesianPoint> points) {
        List<Double> values = new ArrayList<>(points.size());
        for (CartesianPoint point : points) {
            double value = toroidalV(surface, point);
            if (!values.isEmpty()) {
                double previous = values.getLast();
                while (value - previous > Math.PI) {
                    value -= Math.PI * 2.0;
                }
                while (value - previous < -Math.PI) {
                    value += Math.PI * 2.0;
                }
            }
            values.add(value);
        }
        return List.copyOf(values);
    }

    private static double averageToroidalV(ToroidalSurface surface, List<CartesianPoint> points) {
        double total = 0.0;
        for (CartesianPoint point : points) {
            total += toroidalV(surface, point);
        }
        return total / points.size();
    }

    private static double toroidalU(ToroidalSurface surface, CartesianPoint point) {
        return toroidalU(surface.position(), point);
    }

    private static double toroidalU(Axis2Placement3D placement, CartesianPoint point) {
        Vector3 offset = point.subtract(placement.location());
        double x = offset.dot(placement.xDirection().asVector());
        double y = offset.dot(placement.yDirection().asVector());
        return Math.atan2(y, x);
    }

    private static double toroidalV(ToroidalSurface surface, CartesianPoint point) {
        return toroidalV(surface.position(), surface.majorRadius(), point);
    }

    private static double toroidalV(Axis2Placement3D placement, double majorRadius, CartesianPoint point) {
        Vector3 offset = point.subtract(placement.location());
        double x = offset.dot(placement.xDirection().asVector());
        double y = offset.dot(placement.yDirection().asVector());
        double z = offset.dot(placement.axis().asVector());
        double rho = Math.sqrt(x * x + y * y);
        return Math.atan2(z, rho - majorRadius);
    }

    private static List<FaceBound> buildFaceBounds(StepFaceEntity stepFace, StepCadBuilder builder) {
        List<FaceBound> bounds = stepFace.bounds().stream().map(bound -> builder.buildFaceBound(bound.id())).toList();
        if (bounds.stream().noneMatch(FaceBound::outer) && bounds.size() == 1) {
            FaceBound bound = bounds.getFirst();
            return List.of(FaceBound.outer(bound.loop(), bound.orientation()));
        }
        return bounds;
    }

    private static StepEntity faceGeometry(StepFaceEntity stepFace) {
        if (stepFace instanceof StepAdvancedFace advancedFace) {
            return advancedFace.faceGeometry();
        }
        if (stepFace instanceof StepFaceSurface faceSurface) {
            return faceSurface.faceGeometry();
        }
        if (stepFace instanceof StepOrientedFace orientedFace) {
            return faceGeometry(orientedFace.faceElement());
        }
        return null;
    }

    private static String surfaceTypeName(StepEntity geometry) {
        if (geometry instanceof StepLine) {
            return "LINE";
        }
        if (geometry instanceof StepCircle) {
            return "CIRCLE";
        }
        if (geometry instanceof StepEllipse) {
            return "ELLIPSE";
        }
        if (geometry instanceof StepPolyline) {
            return "POLYLINE";
        }
        if (geometry instanceof StepBSplineCurve) {
            return "B_SPLINE_CURVE";
        }
        if (geometry instanceof StepBSplineCurveWithKnots) {
            return "B_SPLINE_CURVE_WITH_KNOTS";
        }
        if (geometry instanceof StepBezierCurve) {
            return "BEZIER_CURVE";
        }
        if (geometry instanceof StepUniformCurve) {
            return "UNIFORM_CURVE";
        }
        if (geometry instanceof StepQuasiUniformCurve) {
            return "QUASI_UNIFORM_CURVE";
        }
        if (geometry instanceof StepPiecewiseBezierCurve) {
            return "PIECEWISE_BEZIER_CURVE";
        }
        if (geometry instanceof StepRationalBSplineCurve) {
            return "RATIONAL_B_SPLINE_CURVE";
        }
        if (geometry instanceof StepOffsetCurve2D) {
            return "OFFSET_CURVE_2D";
        }
        if (geometry instanceof StepOffsetCurve3D) {
            return "OFFSET_CURVE_3D";
        }
        if (geometry instanceof StepTrimmedCurve) {
            return "TRIMMED_CURVE";
        }
        if (geometry instanceof StepSurfaceCurve) {
            return "SURFACE_CURVE";
        }
        if (geometry instanceof StepSeamCurve) {
            return "SEAM_CURVE";
        }
        if (geometry instanceof StepPcurve) {
            return "PCURVE";
        }
        if (geometry instanceof StepCompositeCurve) {
            return "COMPOSITE_CURVE";
        }
        if (geometry instanceof StepCompositeCurveOnSurface) {
            return "COMPOSITE_CURVE_ON_SURFACE";
        }
        if (geometry instanceof StepConicCurve conic) {
            return conic.entityName();
        }
        if (geometry instanceof StepOrientedCurve) {
            return "ORIENTED_CURVE";
        }
        if (geometry instanceof StepPath) {
            return "PATH";
        }
        if (geometry instanceof StepOpenPath) {
            return "OPEN_PATH";
        }
        if (geometry instanceof StepSubpath) {
            return "SUBPATH";
        }
        if (geometry instanceof StepOrientedPath) {
            return "ORIENTED_PATH";
        }
        if (geometry instanceof StepVertex) {
            return "VERTEX";
        }
        if (geometry instanceof StepVertexPoint) {
            return "VERTEX_POINT";
        }
        if (geometry instanceof StepEdgeCurve) {
            return "EDGE_CURVE";
        }
        if (geometry instanceof StepSubedge) {
            return "SUBEDGE";
        }
        if (geometry instanceof StepEdge) {
            return "EDGE";
        }
        if (geometry instanceof StepLoop) {
            return "LOOP";
        }
        if (geometry instanceof StepPolyLoop) {
            return "POLY_LOOP";
        }
        if (geometry instanceof StepEdgeLoop) {
            return "EDGE_LOOP";
        }
        if (geometry instanceof StepVertexLoop) {
            return "VERTEX_LOOP";
        }
        if (geometry instanceof StepFaceBound faceBound) {
            return faceBound.outer() ? "FACE_OUTER_BOUND" : "FACE_BOUND";
        }
        if (geometry instanceof StepOrientedEdge) {
            return "ORIENTED_EDGE";
        }
        if (geometry instanceof StepOrientedFace) {
            return "ORIENTED_FACE";
        }
        if (geometry instanceof StepConnectedEdgeSet) {
            return "CONNECTED_EDGE_SET";
        }
        if (geometry instanceof StepConnectedFaceSubSet) {
            return "CONNECTED_FACE_SUB_SET";
        }
        if (geometry instanceof StepConnectedFaceSet) {
            return "CONNECTED_FACE_SET";
        }
        if (geometry instanceof StepOpenShell) {
            return "OPEN_SHELL";
        }
        if (geometry instanceof StepSurfacedOpenShell) {
            return "SURFACED_OPEN_SHELL";
        }
        if (geometry instanceof StepOrientedOpenShell) {
            return "ORIENTED_OPEN_SHELL";
        }
        if (geometry instanceof StepClosedShell) {
            return "CLOSED_SHELL";
        }
        if (geometry instanceof StepOrientedClosedShell) {
            return "ORIENTED_CLOSED_SHELL";
        }
        if (geometry instanceof StepWireShell) {
            return "WIRE_SHELL";
        }
        if (geometry instanceof StepVertexShell) {
            return "VERTEX_SHELL";
        }
        if (geometry instanceof StepShellBasedSurfaceModel) {
            return "SHELL_BASED_SURFACE_MODEL";
        }
        if (geometry instanceof StepFaceBasedSurfaceModel) {
            return "FACE_BASED_SURFACE_MODEL";
        }
        if (geometry instanceof StepEdgeBasedWireframeModel) {
            return "EDGE_BASED_WIREFRAME_MODEL";
        }
        if (geometry instanceof StepShellBasedWireframeModel) {
            return "SHELL_BASED_WIREFRAME_MODEL";
        }
        if (geometry instanceof StepGeometricCurveSet) {
            return "GEOMETRIC_CURVE_SET";
        }
        if (geometry instanceof StepGeometricSet) {
            return "GEOMETRIC_SET";
        }
        if (geometry instanceof StepRepresentation) {
            return "REPRESENTATION";
        }
        if (geometry instanceof StepRepresentationMap) {
            return "REPRESENTATION_MAP";
        }
        if (geometry instanceof StepRepresentationRelationshipWithTransformation) {
            return "REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION";
        }
        if (geometry instanceof StepRepresentationRelationship) {
            return "REPRESENTATION_RELATIONSHIP";
        }
        if (geometry instanceof StepMappedItem) {
            return "MAPPED_ITEM";
        }
        if (geometry instanceof StepStyledItem) {
            return "STYLED_ITEM";
        }
        if (geometry instanceof StepOverRidingStyledItem) {
            return "OVER_RIDING_STYLED_ITEM";
        }
        if (geometry instanceof StepSurface) {
            return "SURFACE";
        }
        if (geometry instanceof StepBoundedSurface) {
            return "BOUNDED_SURFACE";
        }
        if (geometry instanceof StepBSplineSurface) {
            return "B_SPLINE_SURFACE";
        }
        if (geometry instanceof StepBezierSurface) {
            return "BEZIER_SURFACE";
        }
        if (geometry instanceof StepUniformSurface) {
            return "UNIFORM_SURFACE";
        }
        if (geometry instanceof StepQuasiUniformSurface) {
            return "QUASI_UNIFORM_SURFACE";
        }
        if (geometry instanceof StepPiecewiseBezierSurface) {
            return "PIECEWISE_BEZIER_SURFACE";
        }
        if (geometry instanceof StepPlane) {
            return "PLANE";
        }
        if (geometry instanceof StepCylindricalSurface) {
            return "CYLINDRICAL_SURFACE";
        }
        if (geometry instanceof StepConicalSurface) {
            return "CONICAL_SURFACE";
        }
        if (geometry instanceof StepToroidalSurface) {
            return "TOROIDAL_SURFACE";
        }
        if (geometry instanceof StepSphericalSurface) {
            return "SPHERICAL_SURFACE";
        }
        if (geometry instanceof StepDegenerateToroidalSurface) {
            return "DEGENERATE_TOROIDAL_SURFACE";
        }
        if (geometry instanceof StepSurfaceOfLinearExtrusion) {
            return "SURFACE_OF_LINEAR_EXTRUSION";
        }
        if (geometry instanceof StepSurfaceOfRevolution) {
            return "SURFACE_OF_REVOLUTION";
        }
        if (geometry instanceof StepRationalBSplineSurface) {
            return "RATIONAL_B_SPLINE_SURFACE";
        }
        if (geometry instanceof StepBSplineSurfaceWithKnots) {
            return "B_SPLINE_SURFACE_WITH_KNOTS";
        }
        if (geometry instanceof StepRectangularTrimmedSurface) {
            return "RECTANGULAR_TRIMMED_SURFACE";
        }
        if (geometry instanceof StepCurveBoundedSurface) {
            return "CURVE_BOUNDED_SURFACE";
        }
        if (geometry instanceof StepOrientedSurface) {
            return "ORIENTED_SURFACE";
        }
        if (geometry instanceof StepOffsetSurface) {
            return "OFFSET_SURFACE";
        }
        if (geometry instanceof StepSweptAreaSolid sweptAreaSolid) {
            return sweptAreaSolid.entityName();
        }
        if (geometry instanceof StepSolidReplica) {
            return "SOLID_REPLICA";
        }
        if (geometry instanceof StepManifoldSolidBrep) {
            return "MANIFOLD_SOLID_BREP";
        }
        if (geometry instanceof StepBrepWithVoids) {
            return "BREP_WITH_VOIDS";
        }
        if (geometry instanceof StepCsgSolid) {
            return "CSG_SOLID";
        }
        if (geometry instanceof StepCsgPrimitive primitive) {
            return primitive.entityName();
        }
        if (geometry instanceof StepBooleanClippingResult) {
            return "BOOLEAN_CLIPPING_RESULT";
        }
        if (geometry instanceof StepBooleanResult) {
            return "BOOLEAN_RESULT";
        }
        if (geometry instanceof StepSweptDiskSolid) {
            return "SWEPT_DISK_SOLID";
        }
        if (geometry instanceof StepComplexClippingResult) {
            return "COMPLEX_CLIPPING_RESULT";
        }
        if (geometry instanceof StepGeometricReplica replica) {
            return replica.entityName();
        }
        return geometry.getClass().getSimpleName();
    }

    private static String surfaceTypeNameForGeometry(SurfaceGeometry surface) {
        return switch (surface) {
            case Plane ignored -> "PLANE";
            case CylindricalSurface ignored -> "CYLINDRICAL_SURFACE";
            case ConicalSurface ignored -> "CONICAL_SURFACE";
            case SphericalSurface ignored -> "SPHERICAL_SURFACE";
            case ToroidalSurface ignored -> "TOROIDAL_SURFACE";
            case BSplineSurface3 ignored -> "BSPLINE_SURFACE";
            case RationalBSplineSurface3 ignored -> "RATIONAL_BSPLINE_SURFACE";
            case RuledSurface3 ignored -> "RULED_SURFACE";
            case SurfaceOfRevolution3 ignored -> "SURFACE_OF_REVOLUTION";
            case OffsetSurface3 ignored -> "OFFSET_SURFACE";
            case SurfaceOfLinearExtrusion3 ignored -> "SURFACE_OF_LINEAR_EXTRUSION";
            case SurfaceOfConstantRadius3 ignored -> "SURFACE_OF_CONSTANT_RADIUS";
            case ParaboloidSurface ignored -> "PARABOLOID_SURFACE";
            case HyperboloidSurface ignored -> "HYPERBOLOID_SURFACE";
            case SurfaceOfTranslation3 ignored -> "SURFACE_OF_TRANSLATION";
            case SurfaceOfProjection3 ignored -> "SURFACE_OF_PROJECTION";
        };
    }

    private static UvPoint nearestUvOnRationalBSplineSurface(
            RationalBSplineSurface3 surface,
            CartesianPoint point,
            UvPoint previous
    ) {
        double uStart = surface.uStart();
        double uEnd = surface.uEnd();
        double vStart = surface.vStart();
        double vEnd = surface.vEnd();
        boolean hasPrevious = previous != null;

        double bestU = hasPrevious ? clamp(previous.u(), uStart, uEnd) : uStart;
        double bestV = hasPrevious ? clamp(previous.v(), vStart, vEnd) : vStart;
        double bestDistance = surface.pointAt(bestU, bestV).distanceTo(point);

        int uSamples = hasPrevious ? 4 : 12;
        int vSamples = hasPrevious ? 4 : 12;
        double coarseWindowU = (uEnd - uStart) * (hasPrevious ? 0.08 : 0.25);
        double coarseWindowV = (vEnd - vStart) * (hasPrevious ? 0.08 : 0.25);
        double coarseMinU = hasPrevious ? Math.max(uStart, bestU - coarseWindowU) : uStart;
        double coarseMaxU = hasPrevious ? Math.min(uEnd, bestU + coarseWindowU) : uEnd;
        double coarseMinV = hasPrevious ? Math.max(vStart, bestV - coarseWindowV) : vStart;
        double coarseMaxV = hasPrevious ? Math.min(vEnd, bestV + coarseWindowV) : vEnd;

        for (int i = 0; i <= uSamples; i++) {
            double u = coarseMinU + (coarseMaxU - coarseMinU) * i / Math.max(uSamples, 1);
            for (int j = 0; j <= vSamples; j++) {
                double v = coarseMinV + (coarseMaxV - coarseMinV) * j / Math.max(vSamples, 1);
                double distance = surface.pointAt(u, v).distanceTo(point);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestU = u;
                    bestV = v;
                }
            }
        }

        for (int iteration = 0; iteration < 4; iteration++) {
            double stepU = (uEnd - uStart) / Math.pow(4.0, iteration + 2);
            double stepV = (vEnd - vStart) / Math.pow(4.0, iteration + 2);
            for (int du = -1; du <= 1; du++) {
                for (int dv = -1; dv <= 1; dv++) {
                    double u = clamp(bestU + du * stepU, uStart, uEnd);
                    double v = clamp(bestV + dv * stepV, vStart, vEnd);
                    double distance = surface.pointAt(u, v).distanceTo(point);
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        bestU = u;
                        bestV = v;
                    }
                }
            }
        }
        return new UvPoint(bestU, bestV);
    }

    private static boolean faceSameSense(StepFaceEntity stepFace) {
        if (stepFace instanceof StepAdvancedFace advancedFace) {
            return advancedFace.sameSense();
        }
        if (stepFace instanceof StepFaceSurface faceSurface) {
            return faceSurface.sameSense();
        }
        if (stepFace instanceof StepOrientedFace orientedFace) {
            boolean base = faceSameSense(orientedFace.faceElement());
            return orientedFace.orientation() ? base : !base;
        }
        return true;
    }

    private static FacePayload reverseFacePayload(FacePayload base) {
        List<PointPayload> reversedTriangles = new ArrayList<>(base.triangles().size());
        for (int index = 0; index + 2 < base.triangles().size(); index += 3) {
            reversedTriangles.add(base.triangles().get(index));
            reversedTriangles.add(base.triangles().get(index + 2));
            reversedTriangles.add(base.triangles().get(index + 1));
        }
        return new FacePayload(
                base.stepId(),
                base.name(),
                base.surfaceType(),
                base.origin(),
                new VectorPayload(-base.normal().x(), -base.normal().y(), -base.normal().z()),
                !base.sameSense(),
                base.color(),
                base.transparency(),
                base.pbr(),
                base.layers(),
                base.loops(),
                List.copyOf(reversedTriangles),
                base.surface(),
                base.uvLoops()
        );
    }

    private static List<CartesianPoint> sampleLoop(FaceBound bound) {
        if (bound.loop() instanceof VertexLoop vertexLoop) {
            return List.of(vertexLoop.vertex().point());
        }
        if (bound.loop() instanceof PolyLoop polyLoop) {
            List<CartesianPoint> sampled = new ArrayList<>(polyLoop.points());
            if (!sampled.isEmpty() && sampled.getFirst().distanceTo(sampled.getLast()) > 1.0e-9) {
                sampled.add(sampled.getFirst());
            }
            return bound.orientation() ? sampled : reverseClosedLoop(sampled);
        }
        if (!(bound.loop() instanceof EdgeLoop edgeLoop)) {
            throw new UnsupportedGeometryException("preview export requires EDGE_LOOP, POLY_LOOP or VERTEX_LOOP");
        }
        List<CartesianPoint> sampled = new ArrayList<>();
        boolean firstEdge = true;
        for (OrientedEdge orientedEdge : edgeLoop.edges()) {
            List<CartesianPoint> edgePoints = sampleOrientedEdge(orientedEdge);
            int startIndex = firstEdge ? 0 : 1;
            for (int i = startIndex; i < edgePoints.size(); i++) {
                sampled.add(edgePoints.get(i));
            }
            firstEdge = false;
        }
        if (!sampled.isEmpty() && sampled.getFirst().distanceTo(sampled.getLast()) > 1.0e-9) {
            sampled.add(sampled.getFirst());
        }
        return bound.orientation() ? sampled : reverseClosedLoop(sampled);
    }

    private static void collectTopologyEdges(Face face, Set<Edge> edges) {
        for (FaceBound bound : face.bounds()) {
            if (bound.loop() instanceof EdgeLoop edgeLoop) {
                for (OrientedEdge orientedEdge : edgeLoop.edges()) {
                    edges.add(orientedEdge.edge());
                }
            }
        }
    }

    private static <T> List<T> reverseClosedLoop(List<T> points) {
        if (points.size() < 2) {
            return points;
        }
        List<T> reversed = new ArrayList<>(points);
        if (reversed.getFirst().equals(reversed.getLast())) {
            T start = reversed.removeLast();
            java.util.Collections.reverse(reversed);
            reversed.add(reversed.getFirst());
            reversed.set(0, start);
            reversed.set(reversed.size() - 1, start);
            return reversed;
        }
        java.util.Collections.reverse(reversed);
        return reversed;
    }

    private static List<CartesianPoint> sampleOrientedEdge(OrientedEdge orientedEdge) {
        Edge edge = orientedEdge.edge();
        boolean naturalForward = orientedEdge.orientation() ? edge.sameSense() : !edge.sameSense();
        return sampleEdge(orientedEdge.startVertex().point(), orientedEdge.endVertex().point(), edge.curve(), naturalForward);
    }

    private static List<CartesianPoint> sampleEdgePreview(
            int edgeId,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder
    ) {
        try {
            Edge edge = builder.buildEdge(edgeId);
            return sampleEdge(edge.start().point(), edge.end().point(), edge.curve(), edge.sameSense());
        } catch (TopologyException ex) {
            StepEntity entity = resolved.get(edgeId);
            if (!(entity instanceof StepEdgeCurve edge)) {
                throw ex;
            }
            CartesianPoint start = pointFromStep(edge.start().point());
            CartesianPoint end = pointFromStep(edge.end().point());
            StepEntity edgeGeometry = edge.edgeGeometry();
            Curve3 curve = curveForLooseEdge(edgeGeometry, builder);
            if (curve == null) {
                throw ex;
            }
            try {
                return sampleEdge(start, end, curve, edge.sameSense());
            } catch (GeometryException geometryException) {
                return List.of(start, end);
            }
        }
    }

    private static EdgePayload buildEdgePayload(
            int edgeId,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder
    ) {
        List<CartesianPoint> polyline = sampleEdgePreview(edgeId, resolved, builder);
        StepEntity entity = resolved.get(edgeId);
        if (entity instanceof StepEdgeCurve edge) {
            CartesianPoint start = pointFromStep(edge.start().point());
            CartesianPoint end = pointFromStep(edge.end().point());
            return new EdgePayload(
                    edgeId,
                    toPointPayloads(polyline),
                    edgeCurvePayload(edge.edgeGeometry(), start, end, edge.sameSense(), builder)
            );
        }
        if (entity instanceof StepSeamEdge seamEdge) {
            // Seam edge: curve geometry is resolved at the same ID in entitiesById.
            StepEntity actual = resolved.get(seamEdge.id());
            if (actual != null && actual != seamEdge) {
                Edge edge = builder.buildEdge(edgeId);
                CartesianPoint start = edge.start().point();
                CartesianPoint end = edge.end().point();
                EdgeCurvePayload curvePayload = edgeCurvePayload(actual, start, end, true, builder);
                if (curvePayload != null) {
                    return new EdgePayload(edgeId, toPointPayloads(polyline), curvePayload);
                }
            }
        }
        if (entity instanceof StepFilletEdge filletEdge) {
            // Fillet edge: sample the original edge geometry for preview.
            StepEntity original = filletEdge.originalEdge();
            if (original != null) {
                EdgeCurvePayload curvePayload = edgeCurvePayload(original, polyline.getFirst(), polyline.getLast(), true, builder);
                if (curvePayload != null) {
                    return new EdgePayload(edgeId, toPointPayloads(polyline), curvePayload);
                }
            }
        }
        if (entity instanceof StepChamferEdge chamferEdge) {
            // Chamfer edge: sample the original edge geometry for preview.
            StepEntity original = chamferEdge.originalEdge();
            if (original != null) {
                EdgeCurvePayload curvePayload = edgeCurvePayload(original, polyline.getFirst(), polyline.getLast(), true, builder);
                if (curvePayload != null) {
                    return new EdgePayload(edgeId, toPointPayloads(polyline), curvePayload);
                }
            }
        }
        return new EdgePayload(edgeId, toPointPayloads(polyline), null);
    }

    private static EdgePayload buildTopologyEdgePayload(int edgeId, Edge edge) {
        return new EdgePayload(
                edgeId,
                toPointPayloads(sampleEdge(edge.start().point(), edge.end().point(), edge.curve(), edge.sameSense())),
                null
        );
    }

    private static EdgePayload toPolylineEdgePayload(StepPolyline polyline) {
        List<CartesianPoint> points = polyline.points().stream()
                .map(StepPreviewJsonExporter::pointFromStep)
                .toList();
        return new EdgePayload(polyline.id(), toPointPayloads(points), null);
    }

    private static EdgePayload toPolyLoopEdgePayload(StepPolyLoop polyLoop) {
        List<CartesianPoint> points = polyLoop.polygon().stream()
                .map(StepPreviewJsonExporter::pointFromStep)
                .toList();
        List<CartesianPoint> closed = new ArrayList<>(points);
        if (!closed.isEmpty() && closed.getFirst().distanceTo(closed.getLast()) > 1.0e-9) {
            closed.add(closed.getFirst());
        }
        return new EdgePayload(polyLoop.id(), toPointPayloads(List.copyOf(closed)), null);
    }

    private static EdgePayload sampledCurveEdgePayload(StepEntity item, StepCadBuilder builder) {
        List<CartesianPoint> points = sampleLooseEdgePoints(item, builder);
        if (points == null || points.size() < 2) {
            return null;
        }
        return new EdgePayload(item.id(), toPointPayloads(points), sampledCurvePayload(item, builder));
    }

    private static EdgeCurvePayload sampledCurvePayload(StepEntity item, StepCadBuilder builder) {
        String type = previewCurveTypeName(item);
        if (type == null) {
            return null;
        }
        String basisType = previewCurveBasisTypeName(item);
        Integer basisStepId = previewCurveBasisStepId(item);
        Boolean orientation = previewCurveOrientation(item);
        Boolean senseAgreement = previewCurveSenseAgreement(item);
        Double offsetDistance = previewCurveOffsetDistance(item);
        Boolean selfIntersect = previewCurveSelfIntersect(item);
        List<Double> refDirection = previewCurveRefDirection(item);
        Double transformScale = previewCurveTransformScale(item);
        String masterRepresentation = previewCurveMasterRepresentation(item);
        List<String> associatedSurfaceTypes = previewCurveAssociatedSurfaceTypes(item);
        List<Integer> associatedSurfaceStepIds = previewCurveAssociatedSurfaceStepIds(item);
        try {
            if (item instanceof StepCircle circle) {
                Circle geometry = builder.buildCircle(circle.id());
                Axis2Placement3D placement = geometry.position();
                return new EdgeCurvePayload(
                        item.id(),
                        "CIRCLE",
                        basisType,
                        basisStepId,
                        List.of(placement.location().x(), placement.location().y(), placement.location().z()),
                        List.of(placement.axis().x(), placement.axis().y(), placement.axis().z()),
                        List.of(placement.xDirection().x(), placement.xDirection().y(), placement.xDirection().z()),
                        geometry.radius(),
                        null,
                        null,
                        orientation,
                        senseAgreement,
                        offsetDistance,
                        selfIntersect,
                        refDirection,
                        transformScale,
                        masterRepresentation,
                        associatedSurfaceTypes,
                        associatedSurfaceStepIds,
                        null,
                        null,
                        0.0,
                        Math.PI * 2.0
                );
            }
            if (item instanceof StepEllipse ellipse) {
                Ellipse3 geometry = builder.buildEllipse(ellipse.id());
                Axis2Placement3D placement = geometry.position();
                return new EdgeCurvePayload(
                        item.id(),
                        "ELLIPSE",
                        basisType,
                        basisStepId,
                        List.of(placement.location().x(), placement.location().y(), placement.location().z()),
                        List.of(placement.axis().x(), placement.axis().y(), placement.axis().z()),
                        List.of(placement.xDirection().x(), placement.xDirection().y(), placement.xDirection().z()),
                        null,
                        geometry.semiAxis1(),
                        geometry.semiAxis2(),
                        orientation,
                        senseAgreement,
                        offsetDistance,
                        selfIntersect,
                        refDirection,
                        transformScale,
                        masterRepresentation,
                        associatedSurfaceTypes,
                        associatedSurfaceStepIds,
                        null,
                        null,
                        0.0,
                        Math.PI * 2.0
                );
            }
        } catch (GeometryException | StepResolutionException ex) {
            log.debug("stage={} curveId={}, reason={}", "sampled_curve_payload_fallback", item.id(), ex.getMessage());
        }
        return new EdgeCurvePayload(
                item.id(),
                type,
                basisType,
                basisStepId,
                null,
                null,
                null,
                null,
                null,
                null,
                orientation,
                senseAgreement,
                offsetDistance,
                selfIntersect,
                refDirection,
                transformScale,
                masterRepresentation,
                associatedSurfaceTypes,
                associatedSurfaceStepIds,
                null,
                null,
                0.0,
                0.0
        );
    }

    private static String previewCurveTypeName(StepEntity item) {
        if (item instanceof StepLine) {
            return "LINE";
        }
        if (item instanceof StepCircle) {
            return "CIRCLE";
        }
        if (item instanceof StepEllipse) {
            return "ELLIPSE";
        }
        if (item instanceof StepConicCurve conic) {
            return conic.entityName();
        }
        if (item instanceof StepBezierCurve) {
            return "BEZIER_CURVE";
        }
        if (item instanceof StepUniformCurve) {
            return "UNIFORM_CURVE";
        }
        if (item instanceof StepQuasiUniformCurve) {
            return "QUASI_UNIFORM_CURVE";
        }
        if (item instanceof StepPiecewiseBezierCurve) {
            return "PIECEWISE_BEZIER_CURVE";
        }
        if (item instanceof StepBSplineCurveWithKnots) {
            return "B_SPLINE_CURVE_WITH_KNOTS";
        }
        if (item instanceof com.minicad.step.model.StepRationalBSplineCurve) {
            return "RATIONAL_B_SPLINE_CURVE";
        }
        if (item instanceof StepSurfaceCurve surfaceCurve) {
            return surfaceCurve.entityName();
        }
        if (item instanceof StepSeamCurve) {
            return "SEAM_CURVE";
        }
        if (item instanceof StepTrimmedCurve) {
            return "TRIMMED_CURVE";
        }
        if (item instanceof StepPolyline) {
            return "POLYLINE";
        }
        if (item instanceof StepCompositeCurve) {
            return "COMPOSITE_CURVE";
        }
        if (item instanceof StepCompositeCurveOnSurface) {
            return "COMPOSITE_CURVE_ON_SURFACE";
        }
        if (item instanceof StepOffsetCurve2D) {
            return "OFFSET_CURVE_2D";
        }
        if (item instanceof StepOffsetCurve3D) {
            return "OFFSET_CURVE_3D";
        }
        if (item instanceof StepPcurve) {
            return "PCURVE";
        }
        if (item instanceof StepDegeneratePcurve) {
            return "DEGENERATE_PCURVE";
        }
        if (item instanceof StepOrientedCurve) {
            return "ORIENTED_CURVE";
        }
        if (item instanceof StepAnnotationCurveOccurrence) {
            return "ANNOTATION_CURVE_OCCURRENCE";
        }
        if (item instanceof StepDimensionCurve) {
            return "DIMENSION_CURVE";
        }
        if (item instanceof StepLeaderCurve) {
            return "LEADER_CURVE";
        }
        if (item instanceof StepProjectionCurve) {
            return "PROJECTION_CURVE";
        }
        if (item instanceof StepDraughtingAnnotationOccurrence) {
            return "DRAUGHTING_ANNOTATION_OCCURRENCE";
        }
        if (item instanceof StepTerminatorSymbol) {
            return "TERMINATOR_SYMBOL";
        }
        if (item instanceof StepGeometricReplica replica && "CURVE_REPLICA".equals(replica.entityName())) {
            return "CURVE_REPLICA";
        }
        if (item instanceof StepBSplineCurve) {
            return "B_SPLINE_CURVE";
        }
        if (item instanceof com.minicad.step.model.StepRationalBSplineCurve) {
            return "RATIONAL_B_SPLINE_CURVE";
        }
        if (item instanceof StepCompositeCurveOnSurface3D) {
            return "COMPOSITE_CURVE_ON_SURFACE_3D";
        }
        if (item instanceof StepClothoid) {
            return "CLOTHOID";
        }
        if (item instanceof StepIndexedPolyCurve) {
            return "INDEXED_POLY_CURVE";
        }
        if (item instanceof StepDegenerateCurve) {
            return "DEGENERATE_CURVE";
        }
        if (item instanceof StepBSplineCurveWithKnotsAndBreakpoints) {
            return "B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS";
        }
        if (item instanceof StepLineSegment) {
            return "LINE_SEGMENT";
        }
        if (item instanceof StepEdgeCurve) {
            return "EDGE_CURVE";
        }
        if (item instanceof StepSurfacedEdgeCurve) {
            return "SURFACED_EDGE_CURVE";
        }
        if (item instanceof StepCompositeCurveOnSurface) {
            return "COMPOSITE_CURVE_ON_SURFACE";
        }
        if (item instanceof StepPath) {
            return "PATH";
        }
        if (item instanceof StepOpenPath) {
            return "OPEN_PATH";
        }
        if (item instanceof StepSubpath) {
            return "SUBPATH";
        }
        if (item instanceof StepOrientedPath orientedPath) {
            return "ORIENTED_PATH";
        }
        if (item instanceof StepCurve) {
            return "CURVE";
        }
        if (item instanceof StepBoundedCurve) {
            return "BOUNDED_CURVE";
        }
        if (item instanceof StepCircle2D) {
            return "CIRCLE_2D";
        }
        if (item instanceof StepEllipse2D) {
            return "ELLIPSE_2D";
        }
        if (item instanceof StepPolyline2D) {
            return "POLYLINE_2D";
        }
        if (item instanceof StepTrimmedCurve2D) {
            return "TRIMMED_CURVE_2D";
        }
        if (item instanceof StepCompositeCurve2D) {
            return "COMPOSITE_CURVE_2D";
        }
        if (item instanceof StepBezierCurve2D) {
            return "BEZIER_CURVE_2D";
        }
        if (item instanceof StepQuasiUniformCurve2D) {
            return "QUASI_UNIFORM_CURVE_2D";
        }
        if (item instanceof StepUniformCurve2D) {
            return "UNIFORM_CURVE_2D";
        }
        if (item instanceof StepPiecewiseBezierCurve2D) {
            return "PIECEWISE_BEZIER_CURVE_2D";
        }
        if (item instanceof StepIndexedPolyCurve2D) {
            return "INDEXED_POLY_CURVE_2D";
        }
        if (item instanceof StepDegenerateCurve2D) {
            return "DEGENERATE_CURVE_2D";
        }
        if (item instanceof StepBSplineCurve2D) {
            return "B_SPLINE_CURVE_2D";
        }
        if (item instanceof StepRationalBSplineCurve2D) {
            return "RATIONAL_B_SPLINE_CURVE_2D";
        }
        if (item instanceof StepLine2D) {
            return "LINE_2D";
        }
        if (item instanceof StepCurve2D) {
            return "CURVE_2D";
        }
        if (item instanceof StepHyperbola2D) {
            return "HYPERBOLA_2D";
        }
        if (item instanceof StepParabola2D) {
            return "PARABOLA_2D";
        }
        return null;
    }

    private static String previewCurveBasisTypeName(StepEntity item) {
        if (item instanceof StepSurfaceCurve surfaceCurve) {
            return previewCurveTypeName(surfaceCurve.curve3d());
        }
        if (item instanceof StepSeamCurve seamCurve) {
            return previewCurveTypeName(seamCurve.curve3d());
        }
        if (item instanceof StepTrimmedCurve trimmedCurve) {
            return previewCurveTypeName(trimmedCurve.basisCurve());
        }
        if (item instanceof StepOffsetCurve2D offsetCurve2D) {
            return previewCurveTypeName(offsetCurve2D.basisCurve());
        }
        if (item instanceof StepOffsetCurve3D offsetCurve3D) {
            return previewCurveTypeName(offsetCurve3D.basisCurve());
        }
        if (item instanceof StepOrientedCurve orientedCurve) {
            return previewCurveTypeName(orientedCurve.curveElement());
        }
        if (item instanceof StepAnnotationCurveOccurrence occurrence) {
            return previewCurveTypeName(occurrence.item());
        }
        if (item instanceof StepDimensionCurve dimensionCurve) {
            return previewCurveTypeName(dimensionCurve.item());
        }
        if (item instanceof StepLeaderCurve leaderCurve) {
            return previewCurveTypeName(leaderCurve.item());
        }
        if (item instanceof StepProjectionCurve projectionCurve) {
            return previewCurveTypeName(projectionCurve.item());
        }
        if (item instanceof StepDraughtingAnnotationOccurrence annotationOccurrence) {
            return previewCurveTypeName(annotationOccurrence.item());
        }
        if (item instanceof StepTerminatorSymbol terminatorSymbol) {
            return previewCurveTypeName(terminatorSymbol.annotatedCurve());
        }
        if (item instanceof StepGeometricReplica replica && "CURVE_REPLICA".equals(replica.entityName())) {
            return previewCurveTypeName(replica.parent());
        }
        if (item instanceof StepTrimmedCurve2D trimmedCurve2D) {
            return previewCurveTypeName(trimmedCurve2D.basisCurve());
        }
        return null;
    }

    private static Integer previewCurveBasisStepId(StepEntity item) {
        if (item instanceof StepSurfaceCurve surfaceCurve) {
            return surfaceCurve.curve3d().id();
        }
        if (item instanceof StepSeamCurve seamCurve) {
            return seamCurve.curve3d().id();
        }
        if (item instanceof StepTrimmedCurve trimmedCurve) {
            return trimmedCurve.basisCurve().id();
        }
        if (item instanceof StepOffsetCurve2D offsetCurve2D) {
            return offsetCurve2D.basisCurve().id();
        }
        if (item instanceof StepOffsetCurve3D offsetCurve3D) {
            return offsetCurve3D.basisCurve().id();
        }
        if (item instanceof StepOrientedCurve orientedCurve) {
            return orientedCurve.curveElement().id();
        }
        if (item instanceof StepAnnotationCurveOccurrence occurrence) {
            return occurrence.item().id();
        }
        if (item instanceof StepDimensionCurve dimensionCurve) {
            return dimensionCurve.item().id();
        }
        if (item instanceof StepLeaderCurve leaderCurve) {
            return leaderCurve.item().id();
        }
        if (item instanceof StepProjectionCurve projectionCurve) {
            return projectionCurve.item().id();
        }
        if (item instanceof StepDraughtingAnnotationOccurrence annotationOccurrence) {
            return annotationOccurrence.item().id();
        }
        if (item instanceof StepTerminatorSymbol terminatorSymbol) {
            return terminatorSymbol.annotatedCurve().id();
        }
        if (item instanceof StepGeometricReplica replica && "CURVE_REPLICA".equals(replica.entityName())) {
            return replica.parent().id();
        }
        if (item instanceof StepTrimmedCurve2D trimmedCurve2D) {
            return trimmedCurve2D.basisCurve().id();
        }
        return null;
    }

    private static Boolean previewCurveOrientation(StepEntity item) {
        if (item instanceof StepOrientedCurve orientedCurve) {
            return orientedCurve.orientation();
        }
        return null;
    }

    private static Boolean previewCurveSenseAgreement(StepEntity item) {
        if (item instanceof StepTrimmedCurve trimmedCurve) {
            return trimmedCurve.senseAgreement();
        }
        if (item instanceof StepTrimmedCurve2D trimmedCurve2D) {
            return trimmedCurve2D.senseAgreement();
        }
        return null;
    }

    private static Double previewCurveOffsetDistance(StepEntity item) {
        if (item instanceof StepOffsetCurve2D offsetCurve2D) {
            return offsetCurve2D.distance();
        }
        if (item instanceof StepOffsetCurve3D offsetCurve3D) {
            return offsetCurve3D.distance();
        }
        return null;
    }

    private static Boolean previewCurveSelfIntersect(StepEntity item) {
        if (item instanceof StepOffsetCurve2D offsetCurve2D) {
            return offsetCurve2D.selfIntersect();
        }
        if (item instanceof StepOffsetCurve3D offsetCurve3D) {
            return offsetCurve3D.selfIntersect();
        }
        if (item instanceof StepCompositeCurveOnSurface compositeOnSurface) {
            return compositeOnSurface.selfIntersect();
        }
        return null;
    }

    private static List<Double> previewCurveRefDirection(StepEntity item) {
        if (item instanceof StepOffsetCurve3D offsetCurve3D) {
            return List.copyOf(offsetCurve3D.refDirection().directionRatios());
        }
        return null;
    }

    private static Double previewCurveTransformScale(StepEntity item) {
        if (item instanceof StepGeometricReplica replica && "CURVE_REPLICA".equals(replica.entityName())) {
            return replica.transformation().scale();
        }
        return null;
    }

    private static String previewCurveMasterRepresentation(StepEntity item) {
        StepEntity semanticCurve = previewCurveSemanticItem(item);
        if (semanticCurve instanceof StepSurfaceCurve surfaceCurve) {
            return surfaceCurve.masterRepresentation();
        }
        if (semanticCurve instanceof StepSeamCurve seamCurve) {
            return seamCurve.masterRepresentation();
        }
        return null;
    }

    private static List<String> previewCurveAssociatedSurfaceTypes(StepEntity item) {
        List<StepEntity> associatedGeometry = previewCurveAssociatedGeometry(item);
        if (associatedGeometry == null || associatedGeometry.isEmpty()) {
            return null;
        }
        List<String> surfaceTypes = new ArrayList<>();
        for (StepEntity associated : associatedGeometry) {
            if (associated instanceof StepPcurve pcurve) {
                surfaceTypes.add(surfaceTypeName(pcurve.basisSurface()));
            } else if (associated instanceof StepDegeneratePcurve pcurve) {
                surfaceTypes.add(surfaceTypeName(pcurve.basisSurface()));
            }
        }
        return surfaceTypes.isEmpty() ? null : List.copyOf(surfaceTypes);
    }

    private static List<Integer> previewCurveAssociatedSurfaceStepIds(StepEntity item) {
        List<StepEntity> associatedGeometry = previewCurveAssociatedGeometry(item);
        if (associatedGeometry == null || associatedGeometry.isEmpty()) {
            return null;
        }
        List<Integer> surfaceIds = new ArrayList<>();
        for (StepEntity associated : associatedGeometry) {
            if (associated instanceof StepPcurve pcurve) {
                surfaceIds.add(pcurve.basisSurface().id());
            } else if (associated instanceof StepDegeneratePcurve pcurve) {
                surfaceIds.add(pcurve.basisSurface().id());
            }
        }
        return surfaceIds.isEmpty() ? null : List.copyOf(surfaceIds);
    }

    private static List<StepEntity> previewCurveAssociatedGeometry(StepEntity item) {
        StepEntity semanticCurve = previewCurveSemanticItem(item);
        if (semanticCurve instanceof StepSurfaceCurve surfaceCurve) {
            return surfaceCurve.associatedGeometry();
        }
        if (semanticCurve instanceof StepSeamCurve seamCurve) {
            return seamCurve.associatedGeometry();
        }
        return null;
    }

    private static StepEntity previewCurveSemanticItem(StepEntity item) {
        StepEntity current = item;
        while (true) {
            if (current instanceof StepOrientedCurve orientedCurve) {
                current = orientedCurve.curveElement();
                continue;
            }
            if (current instanceof StepAnnotationCurveOccurrence occurrence) {
                current = occurrence.item();
                continue;
            }
            if (current instanceof StepDimensionCurve dimensionCurve) {
                current = dimensionCurve.item();
                continue;
            }
            if (current instanceof StepLeaderCurve leaderCurve) {
                current = leaderCurve.item();
                continue;
            }
            if (current instanceof StepProjectionCurve projectionCurve) {
                current = projectionCurve.item();
                continue;
            }
            if (current instanceof StepDraughtingAnnotationOccurrence annotationOccurrence) {
                current = annotationOccurrence.item();
                continue;
            }
            if (current instanceof StepTerminatorSymbol terminatorSymbol) {
                current = terminatorSymbol.annotatedCurve();
                continue;
            }
            if (current instanceof StepGeometricReplica replica && "CURVE_REPLICA".equals(replica.entityName())) {
                current = replica.parent();
                continue;
            }
            return current;
        }
    }

    private static List<CartesianPoint> sampleLooseEdgePoints(StepEntity item, StepCadBuilder builder) {
        if (item instanceof StepAnnotationFillArea fillArea) {
            return sampleAnnotationFillAreaPoints(fillArea, builder);
        }
        if (item instanceof StepAnnotationFillAreaOccurrence fillAreaOccurrence) {
            return sampleAnnotationFillAreaPoints(fillAreaOccurrence.item(), builder);
        }
        if (item instanceof StepEdgeBasedWireframeModel wireframeModel) {
            return sampleWireframeBoundaryPoints(wireframeModel.boundaries(), builder);
        }
        if (item instanceof StepShellBasedWireframeModel wireframeModel) {
            return sampleWireframeBoundaryPoints(wireframeModel.boundaries(), builder);
        }
        if (item instanceof StepAnnotationSymbol annotationSymbol) {
            return sampleMappedAnnotationPoints(
                    annotationSymbol.mappingSource().mappedRepresentation(),
                    annotationSymbol.mappingSource().mappedOrigin(),
                    annotationSymbol.mappingTarget(),
                    builder
            );
        }
        if (item instanceof StepAnnotationText annotationText) {
            return sampleMappedAnnotationPoints(
                    annotationText.mappingSource().mappedRepresentation(),
                    annotationText.mappingSource().mappedOrigin(),
                    annotationText.mappingTarget(),
                    builder
            );
        }
        if (item instanceof StepAnnotationTextCharacter annotationTextCharacter) {
            return sampleMappedAnnotationPoints(
                    annotationTextCharacter.mappingSource().mappedRepresentation(),
                    annotationTextCharacter.mappingSource().mappedOrigin(),
                    annotationTextCharacter.mappingTarget(),
                    builder
            );
        }
        if (item instanceof StepGeometricReplica replica && "CURVE_REPLICA".equals(replica.entityName())) {
            List<CartesianPoint> parentPoints = sampleLooseEdgePoints(replica.parent(), builder);
            if (parentPoints == null) {
                return null;
            }
            List<CartesianPoint> transformed = new ArrayList<>(parentPoints.size());
            for (CartesianPoint point : parentPoints) {
                transformed.add(transformPoint(point, replica.transformation(), builder));
            }
            return List.copyOf(transformed);
        }
        if (item instanceof StepOrientedCurve orientedCurve) {
            List<CartesianPoint> points = sampleLooseEdgePoints(orientedCurve.curveElement(), builder);
            if (points == null) {
                return null;
            }
            if (orientedCurve.orientation()) {
                return points;
            }
            List<CartesianPoint> reversed = new ArrayList<>(points);
            Collections.reverse(reversed);
            return List.copyOf(reversed);
        }
        if (item instanceof StepGeometricSet geometricSet) {
            return sampleGeometricCollectionPoints(geometricSet.elements(), builder);
        }
        if (item instanceof StepGeometricCurveSet curveSet) {
            return sampleGeometricCollectionPoints(curveSet.elements(), builder);
        }
        if (item instanceof StepConnectedEdgeSet connectedEdgeSet) {
            return sampleGeometricCollectionPoints(connectedEdgeSet.edges(), builder);
        }
        if (item instanceof StepWireShell wireShell) {
            return sampleWireShellPoints(wireShell, builder);
        }
        if (item instanceof StepEdgeWire edgeWire) {
            return sampleGeometricCollectionPoints(edgeWire.edges(), builder);
        }
        Curve3 curve = curveForLooseEdge(item, builder);
        if (curve == null) {
            return null;
        }
        return sampleLooseCurve(curve);
    }

    private static List<CartesianPoint> sampleGeometricCollectionPoints(
            List<StepEntity> elements,
            StepCadBuilder builder
    ) {
        List<CartesianPoint> points = new ArrayList<>();
        for (StepEntity element : elements) {
            List<CartesianPoint> sampled = sampleLooseEdgePoints(element, builder);
            if (sampled != null && !sampled.isEmpty()) {
                points.addAll(sampled);
            }
        }
        return points.isEmpty() ? null : List.copyOf(points);
    }

    private static List<CartesianPoint> sampleWireShellPoints(
            StepWireShell wireShell,
            StepCadBuilder builder
    ) {
        List<CartesianPoint> points = new ArrayList<>();
        for (StepLoop loop : wireShell.loops()) {
            List<CartesianPoint> sampled = sampleLooseEdgePoints(loop, builder);
            if (sampled != null && !sampled.isEmpty()) {
                points.addAll(sampled);
            }
        }
        return points.isEmpty() ? null : List.copyOf(points);
    }

    private static List<CartesianPoint> sampleMappedAnnotationPoints(
            StepRepresentation representation,
            StepEntity mappedOrigin,
            StepEntity mappingTarget,
            StepCadBuilder builder
    ) {
        double[] matrix = matrixForMappedPlacement(mappedOrigin, mappingTarget, builder);
        if (matrix == null) {
            return null;
        }
        List<CartesianPoint> points = new ArrayList<>();
        for (StepEntity content : representation.items()) {
            List<CartesianPoint> sampled = sampleLooseEdgePoints(content, builder);
            if (sampled == null) {
                continue;
            }
            for (CartesianPoint point : sampled) {
                points.add(transformCartesian(point, matrix));
            }
        }
        return points.isEmpty() ? null : List.copyOf(points);
    }

    private static List<CartesianPoint> sampleWireframeBoundaryPoints(
            List<? extends StepEntity> boundaries,
            StepCadBuilder builder
    ) {
        List<CartesianPoint> points = new ArrayList<>();
        boolean first = true;
        for (StepEntity boundary : boundaries) {
            List<CartesianPoint> sampled = sampleLooseEdgePoints(boundary, builder);
            if (sampled == null || sampled.isEmpty()) {
                continue;
            }
            int start = first ? 0 : 1;
            for (int i = start; i < sampled.size(); i++) {
                points.add(sampled.get(i));
            }
            first = false;
        }
        return points.isEmpty() ? null : List.copyOf(points);
    }

    private static List<CartesianPoint> sampleAnnotationFillAreaPoints(
            StepAnnotationFillArea fillArea,
            StepCadBuilder builder
    ) {
        List<CartesianPoint> points = new ArrayList<>();
        boolean first = true;
        for (StepEntity boundary : fillArea.boundaries()) {
            List<CartesianPoint> sampled = sampleLooseEdgePoints(boundary, builder);
            if (sampled == null || sampled.isEmpty()) {
                continue;
            }
            int start = first ? 0 : 1;
            for (int i = start; i < sampled.size(); i++) {
                points.add(sampled.get(i));
            }
            first = false;
        }
        return points.isEmpty() ? null : List.copyOf(points);
    }

    private static Curve3 curveForLooseEdge(StepEntity item, StepCadBuilder builder) {
        try {
            if (item instanceof StepLine line) {
                return builder.buildLine(line.id());
            }
            if (item instanceof StepCircle circle) {
                return builder.buildCircle(circle.id());
            }
            if (item instanceof StepEllipse ellipse) {
                return builder.buildEllipse(ellipse.id());
            }
            if (item instanceof StepConicCurve conic) {
                List<CartesianPoint> points = sampleConicCurvePoints(conic, builder);
                return points == null ? null : new Polyline3(points);
            }
            if (item instanceof StepBezierCurve curve) {
                return builder.buildCurveReference3(curve.id());
            }
            if (item instanceof StepUniformCurve curve) {
                return builder.buildCurveReference3(curve.id());
            }
            if (item instanceof StepQuasiUniformCurve curve) {
                return builder.buildCurveReference3(curve.id());
            }
            if (item instanceof StepPiecewiseBezierCurve curve) {
                return builder.buildCurveReference3(curve.id());
            }
            if (item instanceof StepBSplineCurveWithKnots spline) {
                return builder.buildBSplineCurve(spline.id());
            }
            if (item instanceof StepSurfaceCurve surfaceCurve) {
                return builder.buildSurfaceCurve(surfaceCurve.id());
            }
            if (item instanceof StepSeamCurve seamCurve) {
                return builder.buildSeamCurve(seamCurve.id());
            }
            if (item instanceof StepTrimmedCurve trimmedCurve) {
                return builder.buildTrimmedCurve(trimmedCurve.id());
            }
            if (item instanceof StepPolyline polyline) {
                return builder.buildPolyline(polyline.id());
            }
            if (item instanceof com.minicad.step.model.StepCompositeCurve compositeCurve) {
                return builder.buildCompositeCurve(compositeCurve.id());
            }
            if (item instanceof com.minicad.step.model.StepCompositeCurveOnSurface compositeCurveOnSurface) {
                return builder.buildCompositeCurve(compositeCurveOnSurface.id());
            }
            if (item instanceof com.minicad.step.model.StepRationalBSplineCurve spline) {
                return builder.buildRationalBSplineCurve(spline.id());
            }
            if (item instanceof StepOffsetCurve2D offsetCurve2D) {
                return liftCurve2(builder.buildOffsetCurve2(offsetCurve2D.id()));
            }
            if (item instanceof StepOffsetCurve3D offsetCurve3D) {
                return builder.buildOffsetCurve3(offsetCurve3D.id());
            }
            if (item instanceof StepPcurve pcurve) {
                Object built = builder.buildPcurve2(pcurve.id());
                return built instanceof Curve2 curve2 ? liftCurve2(curve2) : null;
            }
            if (item instanceof StepDegeneratePcurve pcurve) {
                Object built = builder.buildPcurve2(pcurve.id());
                return built instanceof Curve2 curve2 ? liftCurve2(curve2) : null;
            }
            if (item instanceof StepOrientedCurve orientedCurve) {
                return curveForLooseEdge(orientedCurve.curveElement(), builder);
            }
            if (item instanceof StepAnnotationCurveOccurrence occurrence) {
                return curveForLooseEdge(occurrence.item(), builder);
            }
            if (item instanceof StepDimensionCurve dimensionCurve) {
                return curveForLooseEdge(dimensionCurve.item(), builder);
            }
            if (item instanceof StepLeaderCurve leaderCurve) {
                return curveForLooseEdge(leaderCurve.item(), builder);
            }
            if (item instanceof StepProjectionCurve projectionCurve) {
                return curveForLooseEdge(projectionCurve.item(), builder);
            }
            if (item instanceof StepDraughtingAnnotationOccurrence annotationOccurrence) {
                return curveForLooseEdge(annotationOccurrence.item(), builder);
            }
            if (item instanceof StepTerminatorSymbol terminatorSymbol) {
                return curveForLooseEdge(terminatorSymbol.annotatedCurve(), builder);
            }
            if (item instanceof StepGeometricReplica replica && "CURVE_REPLICA".equals(replica.entityName())) {
                List<CartesianPoint> points = sampleLooseEdgePoints(replica, builder);
                return points == null ? null : new Polyline3(points);
            }
            if (item instanceof StepIndexedPolyCurve polyCurve) {
                return builder.buildCurveReference3(polyCurve.id());
            }
            if (item instanceof StepClothoid clothoid) {
                return builder.buildCurveReference3(clothoid.id());
            }
            if (item instanceof StepDegenerateCurve degenerate) {
                return builder.buildCurveReference3(degenerate.id());
            }
            if (item instanceof StepBSplineCurve bspline) {
                return builder.buildCurveReference3(bspline.id());
            }
            if (item instanceof StepCompositeCurveOnSurface compositeOnSurface) {
                return builder.buildCurveReference3(compositeOnSurface.id());
            }
            if (item instanceof StepBSplineCurveWithKnotsAndBreakpoints splineBreak) {
                return builder.buildBSplineCurveWithBreakpoints(splineBreak.id());
            }
            if (item instanceof StepLineSegment lineSeg) {
                return new Polyline3(List.of(
                        builder.buildPoint(lineSeg.startPoint().id()),
                        builder.buildPoint(lineSeg.endPoint().id())
                ));
            }
            if (item instanceof StepEdgeCurve edgeCurve) {
                return builder.buildCurveReference3(edgeCurve.id());
            }
            if (item instanceof StepSurfacedEdgeCurve surfacedEdge) {
                return builder.buildCurveReference3(surfacedEdge.id());
            }
            if (item instanceof StepCompositeCurveOnSurface3D compositeOnSurface3D) {
                return builder.buildCurveReference3(compositeOnSurface3D.id());
            }
            if (item instanceof StepPath path) {
                return builder.buildPath(path.id());
            }
            if (item instanceof StepOpenPath openPath) {
                return builder.buildPath(openPath.id());
            }
            if (item instanceof StepSubpath subpath) {
                return builder.buildPath(subpath.id());
            }
            if (item instanceof StepSeamCurve seamCurve) {
                return builder.buildSeamCurve(seamCurve.id()).curve3d();
            }
            if (item instanceof StepCircle2D
                    || item instanceof StepEllipse2D
                    || item instanceof StepHyperbola2D
                    || item instanceof StepParabola2D
                    || item instanceof StepPolyline2D
                    || item instanceof StepTrimmedCurve2D
                    || item instanceof StepCompositeCurve2D
                    || item instanceof StepBezierCurve2D
                    || item instanceof StepQuasiUniformCurve2D
                    || item instanceof StepUniformCurve2D
                    || item instanceof StepPiecewiseBezierCurve2D
                    || item instanceof StepIndexedPolyCurve2D
                    || item instanceof StepDegenerateCurve2D
                    || item instanceof StepBSplineCurve2D
                    || item instanceof StepRationalBSplineCurve2D
                    || item instanceof StepLine2D
                    || item instanceof StepCurve2D
                    || item instanceof StepBoundedCurve2D) {
                return builder.buildCurve3From2D(item.id());
            }
        } catch (UnsupportedGeometryException | StepResolutionException ex) {
            return null;
        }
        if (item instanceof StepBoundedCurve bounded) {
            return builder.buildCurveReference3(bounded.id());
        }
        if (item instanceof StepMappedItem mappedItem) {
            return curveForLooseEdge(mappedItem.mappingTarget(), builder);
        }
        return null;
    }

    private static List<CartesianPoint> sampleConicCurvePoints(StepConicCurve curve, StepCadBuilder builder) {
        double[] matrix = matrixForPlacementEntity(curve.position(), builder);
        if (matrix == null) {
            return null;
        }
        return switch (curve.entityName()) {
            case "CIRCLE" -> sampleConicCirclePoints(curve, matrix);
            case "ELLIPSE" -> sampleConicEllipsePoints(curve, matrix);
            case "PARABOLA" -> sampleParabolaPoints(curve, matrix);
            case "HYPERBOLA" -> sampleHyperbolaPoints(curve, matrix);
            case "DEGENERATE_CONIC" -> {
                CartesianPoint point = transformCartesian(new CartesianPoint(0.0, 0.0, 0.0), matrix);
                yield List.of(point, point);
            }
            default -> null;
        };
    }

    private static List<CartesianPoint> sampleConicCirclePoints(StepConicCurve curve, double[] matrix) {
        if (curve.parameters().isEmpty()) return null;
        double radius = curve.parameters().get(0);
        if (!Double.isFinite(radius) || radius <= Epsilon.EPS) return null;
        return sampleConicPointsInMatrix(matrix, radius, radius, 72);
    }

    private static List<CartesianPoint> sampleConicEllipsePoints(StepConicCurve curve, double[] matrix) {
        if (curve.parameters().size() < 2) return null;
        double semiMajor = curve.parameters().get(0);
        double semiMinor = curve.parameters().get(1);
        if (!Double.isFinite(semiMajor) || !Double.isFinite(semiMinor)) return null;
        if (semiMajor <= Epsilon.EPS || semiMinor <= Epsilon.EPS) return null;
        return sampleConicPointsInMatrix(matrix, semiMajor, semiMinor, 72);
    }

    private static List<CartesianPoint> sampleConicPointsInMatrix(double[] matrix, double rx, double ry, int segments) {
        List<CartesianPoint> points = new ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double angle = 2.0 * Math.PI * i / segments;
            CartesianPoint local = new CartesianPoint(rx * Math.cos(angle), ry * Math.sin(angle), 0.0);
            points.add(transformCartesian(local, matrix));
        }
        return List.copyOf(points);
    }

    private static List<CartesianPoint> sampleParabolaPoints(StepConicCurve curve, double[] matrix) {
        if (curve.parameters().isEmpty()) {
            return null;
        }
        double focalDistance = curve.parameters().get(0);
        if (!Double.isFinite(focalDistance) || focalDistance <= Epsilon.EPS) {
            return null;
        }
        double yExtent = Math.max(1.0, focalDistance * 4.0);
        int segments = 96;
        List<CartesianPoint> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            double t = -yExtent + (2.0 * yExtent * index) / segments;
            double x = (t * t) / (4.0 * focalDistance);
            points.add(transformCartesian(new CartesianPoint(x, t, 0.0), matrix));
        }
        return List.copyOf(points);
    }

    private static List<CartesianPoint> sampleHyperbolaPoints(StepConicCurve curve, double[] matrix) {
        if (curve.parameters().size() < 2) {
            return null;
        }
        double semiAxis = curve.parameters().get(0);
        double semiImaginaryAxis = curve.parameters().get(1);
        if (!Double.isFinite(semiAxis)
                || !Double.isFinite(semiImaginaryAxis)
                || semiAxis <= Epsilon.EPS
                || semiImaginaryAxis <= Epsilon.EPS) {
            return null;
        }
        double extent = 1.75;
        int segments = 96;
        List<CartesianPoint> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            double t = -extent + (2.0 * extent * index) / segments;
            double x = semiAxis * Math.cosh(t);
            double y = semiImaginaryAxis * Math.sinh(t);
            points.add(transformCartesian(new CartesianPoint(x, y, 0.0), matrix));
        }
        return List.copyOf(points);
    }

    private static List<CartesianPoint> sampleLooseCurve(Curve3 curve) {
        if (curve instanceof TrimmedCurve3 trimmedCurve) {
            return sampleTrimmedCurve3(trimmedCurve, 72);
        }
        if (curve instanceof SurfaceCurve3 surfaceCurve) {
            return sampleLooseCurve(surfaceCurve.curve3d());
        }
        if (curve instanceof Polyline3 polyline) {
            return polyline.points();
        }
        if (curve instanceof CompositeCurve3 compositeCurve) {
            List<CartesianPoint> points = new ArrayList<>();
            boolean first = true;
            for (Curve3 segment : compositeCurve.segments()) {
                List<CartesianPoint> segmentPoints = sampleLooseCurve(segment);
                int start = first ? 0 : 1;
                for (int i = start; i < segmentPoints.size(); i++) {
                    points.add(segmentPoints.get(i));
                }
                first = false;
            }
            return List.copyOf(points);
        }
        List<CartesianPoint> points = curve.sample(72);
        if (points.isEmpty()) {
            throw new UnsupportedGeometryException("curve sampling for " + curve.getClass().getSimpleName() + " is unsupported");
        }
        return points;
    }

    private static Curve3 liftCurve2(Curve2 curve) {
        List<Point2> points2 = sampleLooseCurve2(curve);
        List<CartesianPoint> points3 = new ArrayList<>(points2.size());
        for (Point2 point : points2) {
            points3.add(new CartesianPoint(point.x(), point.y(), 0.0));
        }
        return new Polyline3(List.copyOf(points3));
    }

    private static List<Point2> sampleLooseCurve2(Curve2 curve) {
        if (curve instanceof Line2 line) {
            return List.of(line.pointAt(0.0), line.pointAt(1.0));
        }
        if (curve instanceof Circle2 circle) {
            return sampleCircle2Points(circle, 72);
        }
        if (curve instanceof Ellipse2 ellipse) {
            return sampleEllipse2Points(ellipse, 72);
        }
        if (curve instanceof Parabola2 parabola) {
            return parabola.sample(72);
        }
        if (curve instanceof Hyperbola2 hyperbola) {
            return hyperbola.sample(72);
        }
        if (curve instanceof DegenerateCurve2 degenerate) {
            return List.of(degenerate.point());
        }
        if (curve instanceof BSplineCurve2 spline) {
            return spline.sample(72);
        }
        if (curve instanceof RationalBSplineCurve2 spline) {
            return spline.sample(72);
        }
        if (curve instanceof TrimmedCurve2 trimmedCurve) {
            return sampleTrimmedCurve2(trimmedCurve, 72);
        }
        if (curve instanceof Polyline2 polyline) {
            return polyline.points();
        }
        if (curve instanceof CompositeCurve2 compositeCurve) {
            List<Point2> points = new ArrayList<>();
            boolean first = true;
            for (Curve2 segment : compositeCurve.segments()) {
                List<Point2> segmentPoints = sampleLooseCurve2(segment);
                int start = first ? 0 : 1;
                for (int i = start; i < segmentPoints.size(); i++) {
                    points.add(segmentPoints.get(i));
                }
                first = false;
            }
            return List.copyOf(points);
        }
        throw new UnsupportedGeometryException("2D curve sampling for " + curveTypeName(curve) + " is unsupported");
    }

    private static List<Point2> sampleTrimmedCurve2(TrimmedCurve2 trimmedCurve, int segments) {
        List<Point2> sampled = sampleLooseCurve2(trimmedCurve.basisCurve());
        if (sampled.size() < 2) {
            return List.of(trimmedCurve.trimStart(), trimmedCurve.trimEnd());
        }
        boolean closed = sampled.getFirst().subtract(sampled.getLast()).norm() <= 1.0e-9;
        List<Point2> basisPoints = closed ? List.copyOf(sampled.subList(0, sampled.size() - 1)) : sampled;
        int startIndex = nearestPointIndex2(basisPoints, trimmedCurve.trimStart());
        int endIndex = nearestPointIndex2(basisPoints, trimmedCurve.trimEnd());

        List<Point2> trimmed = new ArrayList<>(Math.max(segments + 1, 2));
        trimmed.add(trimmedCurve.trimStart());
        if (closed) {
            appendClosedTrimmedPoints2(trimmed, basisPoints, startIndex, endIndex, trimmedCurve.senseAgreement());
        } else {
            appendOpenTrimmedPoints2(trimmed, basisPoints, startIndex, endIndex);
        }
        addDistinctPoint2(trimmed, trimmedCurve.trimEnd());
        return List.copyOf(trimmed);
    }

    private static int nearestPointIndex2(List<Point2> points, Point2 target) {
        int nearestIndex = 0;
        double nearestDistance = Double.POSITIVE_INFINITY;
        for (int index = 0; index < points.size(); index++) {
            double distance = points.get(index).subtract(target).norm();
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestIndex = index;
            }
        }
        return nearestIndex;
    }

    private static void appendClosedTrimmedPoints2(
            List<Point2> target,
            List<Point2> basisPoints,
            int startIndex,
            int endIndex,
            boolean senseAgreement
    ) {
        int size = basisPoints.size();
        int index = startIndex;
        while (index != endIndex) {
            index = senseAgreement ? (index + 1) % size : (index - 1 + size) % size;
            addDistinctPoint2(target, basisPoints.get(index));
        }
    }

    private static void appendOpenTrimmedPoints2(
            List<Point2> target,
            List<Point2> basisPoints,
            int startIndex,
            int endIndex
    ) {
        if (startIndex <= endIndex) {
            for (int index = startIndex + 1; index <= endIndex; index++) {
                addDistinctPoint2(target, basisPoints.get(index));
            }
            return;
        }
        for (int index = startIndex - 1; index >= endIndex; index--) {
            addDistinctPoint2(target, basisPoints.get(index));
        }
    }

    private static void addDistinctPoint2(List<Point2> points, Point2 candidate) {
        if (points.isEmpty() || points.getLast().subtract(candidate).norm() > 1.0e-9) {
            points.add(candidate);
        }
    }

    private static String curveTypeName(Curve3 curve) {
        if (curve instanceof Line3) {
            return "LINE";
        }
        if (curve instanceof Circle) {
            return "CIRCLE";
        }
        if (curve instanceof Ellipse3) {
            return "ELLIPSE";
        }
        if (curve instanceof Parabola3) {
            return "PARABOLA";
        }
        if (curve instanceof Hyperbola3) {
            return "HYPERBOLA";
        }
        if (curve instanceof Clothoid3) {
            return "CLOTHOID";
        }
        if (curve instanceof DegenerateCurve3) {
            return "DEGENERATE_CURVE";
        }
        if (curve instanceof BSplineCurve3) {
            return "B_SPLINE_CURVE";
        }
        if (curve instanceof RationalBSplineCurve3) {
            return "RATIONAL_B_SPLINE_CURVE";
        }
        if (curve instanceof TrimmedCurve3) {
            return "TRIMMED_CURVE";
        }
        if (curve instanceof SurfaceCurve3) {
            return "SURFACE_CURVE";
        }
        if (curve instanceof Polyline3) {
            return "POLYLINE";
        }
        if (curve instanceof CompositeCurve3) {
            return "COMPOSITE_CURVE";
        }
        return curve.getClass().getSimpleName();
    }

    private static String curveTypeName(Curve2 curve) {
        if (curve instanceof Line2) {
            return "LINE";
        }
        if (curve instanceof Circle2) {
            return "CIRCLE";
        }
        if (curve instanceof Ellipse2) {
            return "ELLIPSE";
        }
        if (curve instanceof Parabola2) {
            return "PARABOLA";
        }
        if (curve instanceof Hyperbola2) {
            return "HYPERBOLA";
        }
        if (curve instanceof DegenerateCurve2) {
            return "DEGENERATE_CURVE";
        }
        if (curve instanceof BSplineCurve2) {
            return "B_SPLINE_CURVE";
        }
        if (curve instanceof RationalBSplineCurve2) {
            return "RATIONAL_B_SPLINE_CURVE";
        }
        if (curve instanceof TrimmedCurve2) {
            return "TRIMMED_CURVE";
        }
        if (curve instanceof Polyline2) {
            return "POLYLINE";
        }
        if (curve instanceof CompositeCurve2) {
            return "COMPOSITE_CURVE";
        }
        return curve.getClass().getSimpleName();
    }

    private static List<Point2> sampleCircle2Points(Circle2 circle, int segments) {
        List<Point2> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            points.add(circle.pointAt(Math.PI * 2.0 * index / segments));
        }
        return List.copyOf(points);
    }

    private static List<Point2> sampleEllipse2Points(Ellipse2 ellipse, int segments) {
        List<Point2> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            points.add(ellipse.pointAt(Math.PI * 2.0 * index / segments));
        }
        return List.copyOf(points);
    }

    private static EdgeCurvePayload edgeCurvePayload(
            StepEntity edgeGeometry,
            CartesianPoint start,
            CartesianPoint end,
            boolean naturalForward,
            StepCadBuilder builder
    ) {
        try {
            if (edgeGeometry instanceof StepLine) {
                return sampledCurvePayload(edgeGeometry, builder);
            }
            if (edgeGeometry instanceof StepCircle circle) {
                Circle geometry = builder.buildCircle(circle.id());
                Axis2Placement3D placement = geometry.position();
                double startAngle = geometry.angleOf(start);
                double endAngle = geometry.angleOf(end);
                return new EdgeCurvePayload(
                        edgeGeometry.id(),
                        "circle_arc",
                        null,
                        null,
                        List.of(placement.location().x(), placement.location().y(), placement.location().z()),
                        List.of(placement.axis().x(), placement.axis().y(), placement.axis().z()),
                        List.of(placement.xDirection().x(), placement.xDirection().y(), placement.xDirection().z()),
                        geometry.radius(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        startAngle,
                        arcSweep(startAngle, endAngle, start.distanceTo(end) <= Epsilon.EPS, naturalForward)
                );
            }
            if (edgeGeometry instanceof StepEllipse ellipse) {
                Ellipse3 geometry = builder.buildEllipse(ellipse.id());
                Axis2Placement3D placement = geometry.position();
                double startAngle = geometry.angleOf(start);
                double endAngle = geometry.angleOf(end);
                return new EdgeCurvePayload(
                        edgeGeometry.id(),
                        "ellipse_arc",
                        null,
                        null,
                        List.of(placement.location().x(), placement.location().y(), placement.location().z()),
                        List.of(placement.axis().x(), placement.axis().y(), placement.axis().z()),
                        List.of(placement.xDirection().x(), placement.xDirection().y(), placement.xDirection().z()),
                        null,
                        geometry.semiAxis1(),
                        geometry.semiAxis2(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        startAngle,
                        arcSweep(startAngle, endAngle, start.distanceTo(end) <= Epsilon.EPS, naturalForward)
                );
            }
            EdgeCurvePayload generic = sampledCurvePayload(edgeGeometry, builder);
            if (generic != null) {
                return generic;
            }
        } catch (GeometryException | TopologyException ex) {
            log.debug("stage={} edgeGeometryId={}, reason={}", "edge_curve_payload_skipped", edgeGeometry.id(), ex.getMessage());
        }
        return null;
    }

    private static double arcSweep(double startAngle, double endAngle, boolean closed, boolean naturalForward) {
        double delta = endAngle - startAngle;
        if (closed) {
            return naturalForward ? Math.PI * 2.0 : -Math.PI * 2.0;
        }
        if (naturalForward) {
            return delta < 0.0 ? delta + Math.PI * 2.0 : delta;
        }
        return delta > 0.0 ? delta - Math.PI * 2.0 : delta;
    }

    private static List<CartesianPoint> sampleEdge(CartesianPoint start, CartesianPoint end, Curve3 curve, boolean naturalForward) {
        if (curve instanceof TrimmedCurve3 trimmedCurve) {
            List<CartesianPoint> points = new ArrayList<>(sampleTrimmedCurve3(trimmedCurve, 72));
            if (!naturalForward) {
                java.util.Collections.reverse(points);
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
        }
        if (curve instanceof SurfaceCurve3 surfaceCurve) {
            return sampleEdge(start, end, surfaceCurve.curve3d(), naturalForward);
        }
        if (curve instanceof BSplineCurve3 splineCurve) {
            List<CartesianPoint> points = new ArrayList<>(splineCurve.sample(72));
            if (!naturalForward) {
                java.util.Collections.reverse(points);
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
        }
        if (curve instanceof RationalBSplineCurve3 splineCurve) {
            List<CartesianPoint> points = new ArrayList<>(splineCurve.sample(72));
            if (!naturalForward) {
                java.util.Collections.reverse(points);
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
        }
        if (curve instanceof Line3) {
            return List.of(start, end);
        }
        if (curve instanceof Circle circle) {
            return sampleCircleArc(circle, start, end, naturalForward);
        }
        if (curve instanceof Ellipse3 ellipse) {
            return sampleEllipseArc(ellipse, start, end, naturalForward);
        }
        if (curve instanceof Polyline3 polyline) {
            List<CartesianPoint> points = new ArrayList<>(polyline.points());
            if (!naturalForward) {
                java.util.Collections.reverse(points);
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
        }
        if (curve instanceof CompositeCurve3 compositeCurve) {
            List<CartesianPoint> points = new ArrayList<>();
            boolean firstSegment = true;
            for (Curve3 segment : compositeCurve.segments()) {
                List<CartesianPoint> segmentPoints = sampleEdge(start, end, segment, naturalForward);
                int startIndex = firstSegment ? 0 : 1;
                for (int i = startIndex; i < segmentPoints.size(); i++) {
                    points.add(segmentPoints.get(i));
                }
                firstSegment = false;
            }
            if (!points.isEmpty()) {
                points.set(0, start);
                points.set(points.size() - 1, end);
            }
            return List.copyOf(points);
        }
        if (curve instanceof Parabola3 parabola) {
            List<CartesianPoint> points = new ArrayList<>(parabola.sample(72));
            if (!naturalForward) {
                java.util.Collections.reverse(points);
            }
            if (points.size() >= 2) {
                points.set(0, start);
                points.set(points.size() - 1, end);
            }
            return List.copyOf(points);
        }
        if (curve instanceof Hyperbola3 hyperbola) {
            List<CartesianPoint> points = new ArrayList<>(hyperbola.sample(72));
            if (!naturalForward) {
                java.util.Collections.reverse(points);
            }
            if (points.size() >= 2) {
                points.set(0, start);
                points.set(points.size() - 1, end);
            }
            return List.copyOf(points);
        }
        if (curve instanceof Clothoid3 clothoid) {
            List<CartesianPoint> points = new ArrayList<>(clothoid.sample(72));
            if (!naturalForward) {
                java.util.Collections.reverse(points);
            }
            if (points.size() >= 2) {
                points.set(0, start);
                points.set(points.size() - 1, end);
            }
            return List.copyOf(points);
        }
        if (curve instanceof DegenerateCurve3 degenerate) {
            // Degenerate curve: a single collapsed point; return start-end as a degenerate edge
            return List.of(start, end);
        }
        throw new UnsupportedGeometryException("preview export requires LINE, CIRCLE, ELLIPSE, PARABOLA, HYPERBOLA, CLOTHOID, POLYLINE, COMPOSITE_CURVE, B_SPLINE, RATIONAL_B_SPLINE_CURVE, OFFSET_CURVE_2D/3D, SURFACE_CURVE, SEAM_CURVE, DEGENERATE_CURVE or TRIMMED_CURVE topology");
    }

    private static List<CartesianPoint> sampleTrimmedCurve3(TrimmedCurve3 trimmedCurve, int segments) {
        List<CartesianPoint> sampled = sampleLooseCurve(trimmedCurve.basisCurve());
        if (sampled.size() < 2) {
            return List.of(trimmedCurve.trimStart(), trimmedCurve.trimEnd());
        }
        boolean closed = sampled.getFirst().distanceTo(sampled.getLast()) <= 1.0e-9;
        List<CartesianPoint> basisPoints = closed ? List.copyOf(sampled.subList(0, sampled.size() - 1)) : sampled;
        int startIndex = nearestPointIndex(basisPoints, trimmedCurve.trimStart());
        int endIndex = nearestPointIndex(basisPoints, trimmedCurve.trimEnd());

        List<CartesianPoint> trimmed = new ArrayList<>(Math.max(segments + 1, 2));
        trimmed.add(trimmedCurve.trimStart());
        if (closed) {
            appendClosedTrimmedPoints(trimmed, basisPoints, startIndex, endIndex, trimmedCurve.senseAgreement());
        } else {
            appendOpenTrimmedPoints(trimmed, basisPoints, startIndex, endIndex);
        }
        addDistinctPoint(trimmed, trimmedCurve.trimEnd());
        return List.copyOf(trimmed);
    }

    private static int nearestPointIndex(List<CartesianPoint> points, CartesianPoint target) {
        int nearestIndex = 0;
        double nearestDistance = Double.POSITIVE_INFINITY;
        for (int index = 0; index < points.size(); index++) {
            double distance = points.get(index).distanceTo(target);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestIndex = index;
            }
        }
        return nearestIndex;
    }

    private static void appendClosedTrimmedPoints(
            List<CartesianPoint> target,
            List<CartesianPoint> basisPoints,
            int startIndex,
            int endIndex,
            boolean senseAgreement
    ) {
        int size = basisPoints.size();
        int index = startIndex;
        while (index != endIndex) {
            index = senseAgreement ? (index + 1) % size : (index - 1 + size) % size;
            addDistinctPoint(target, basisPoints.get(index));
        }
    }

    private static void appendOpenTrimmedPoints(
            List<CartesianPoint> target,
            List<CartesianPoint> basisPoints,
            int startIndex,
            int endIndex
    ) {
        if (startIndex <= endIndex) {
            for (int index = startIndex + 1; index <= endIndex; index++) {
                addDistinctPoint(target, basisPoints.get(index));
            }
            return;
        }
        for (int index = startIndex - 1; index >= endIndex; index--) {
            addDistinctPoint(target, basisPoints.get(index));
        }
    }

    private static void addDistinctPoint(List<CartesianPoint> points, CartesianPoint candidate) {
        if (points.isEmpty() || points.getLast().distanceTo(candidate) > 1.0e-9) {
            points.add(candidate);
        }
    }

    private static List<CartesianPoint> sampleCircleArc(Circle circle, CartesianPoint start, CartesianPoint end, boolean naturalForward) {
        // Project points onto circle if they're close (numerical tolerance)
        CartesianPoint projectedStart = circle.contains(start) ? start : circle.closestPointTo(start);
        CartesianPoint projectedEnd = circle.contains(end) ? end : circle.closestPointTo(end);
        double startAngle = circle.angleOf(projectedStart);
        double endAngle = circle.angleOf(projectedEnd);
        double delta = endAngle - startAngle;
        if (projectedStart.distanceTo(projectedEnd) <= Epsilon.EPS) {
            delta = naturalForward ? Math.PI * 2.0 : -Math.PI * 2.0;
        } else if (naturalForward) {
            if (delta < 0.0) {
                delta += Math.PI * 2.0;
            }
        } else if (delta > 0.0) {
            delta -= Math.PI * 2.0;
        }

        int segments = Math.max(64, (int) Math.ceil(Math.abs(delta) / (Math.PI / 72.0)));
        List<CartesianPoint> points = new ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double angle = startAngle + delta * i / segments;
            points.add(circle.pointAt(angle));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return points;
    }

    private static List<CartesianPoint> sampleEllipseArc(Ellipse3 ellipse, CartesianPoint start, CartesianPoint end, boolean naturalForward) {
        double startAngle = ellipse.angleOf(start);
        double endAngle = ellipse.angleOf(end);
        double delta = endAngle - startAngle;
        if (start.distanceTo(end) <= Epsilon.EPS) {
            delta = naturalForward ? Math.PI * 2.0 : -Math.PI * 2.0;
        } else if (naturalForward) {
            if (delta < 0.0) {
                delta += Math.PI * 2.0;
            }
        } else if (delta > 0.0) {
            delta -= Math.PI * 2.0;
        }

        int segments = Math.max(72, (int) Math.ceil(Math.abs(delta) / (Math.PI / 96.0)));
        List<CartesianPoint> points = new ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double angle = startAngle + delta * i / segments;
            points.add(ellipse.pointAt(angle));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return points;
    }

    private static PointPayload toPointPayload(CartesianPoint point) {
        return new PointPayload(point.x(), point.y(), point.z());
    }

    private static List<PointPayload> toPointPayloads(List<CartesianPoint> points) {
        return points.stream().map(StepPreviewJsonExporter::toPointPayload).toList();
    }

    private static PreviewPayload reducePayloadGeometry(PreviewPayload payload) {
        return reducePayloadGeometry(payload, MAX_TOTAL_TRIANGLE_POINTS, MAX_TOTAL_LOOP_POINTS, "payload_geometry_reduced");
    }

    private static PreviewPayload reducePayloadGeometry(
            PreviewPayload payload,
            int maxTrianglePoints,
            int maxLoopPoints,
            String reductionStage
    ) {
        int trianglePoints = countTrianglePoints(payload);
        int loopPoints = countLoopPoints(payload);
        int triangleFactor = Math.max(1, (int) Math.ceil(trianglePoints / (double) maxTrianglePoints));
        int loopFactor = Math.max(1, (int) Math.ceil(loopPoints / (double) maxLoopPoints));
        if (triangleFactor == 1 && loopFactor == 1) {
            return payload;
        }
        List<FacePayload> faces = payload.faces().stream()
                .map(face -> reduceFacePayload(face, triangleFactor, loopFactor))
                .toList();
        List<RepresentationPayload> representations = payload.representations().stream()
                .map(representation -> new RepresentationPayload(
                        representation.id(),
                        representation.name(),
                        representation.layers(),
                        representation.color(),
                        representation.edges(),
                        representation.faces().stream()
                                .map(face -> reduceFacePayload(face, triangleFactor, loopFactor))
                                .toList()
                ))
                .toList();
        PreviewPayload reduced = new PreviewPayload(
                payload.stats(),
                payload.bounds(),
                payload.validation(),
                payload.product(),
                payload.units(),
                payload.pmi(),
                payload.unsupportedBooleans(),
                payload.unsupportedFaces(),
                payload.edges(),
                faces,
                representations,
                payload.instances()
        );
        log.info("stage={} originalTrianglePoints={}, reducedTrianglePoints={}, originalLoopPoints={}, reducedLoopPoints={}, triangleFactor={}, loopFactor={}, maxTrianglePoints={}, maxLoopPoints={}",
                reductionStage,
                trianglePoints,
                countTrianglePoints(reduced),
                loopPoints,
                countLoopPoints(reduced),
                triangleFactor,
                loopFactor,
                maxTrianglePoints,
                maxLoopPoints);
        return reduced;
    }

    private static FacePayload reduceFacePayload(FacePayload face, int triangleFactor, int loopFactor) {
        return new FacePayload(
                face.stepId(),
                face.name(),
                face.surfaceType(),
                face.origin(),
                face.normal(),
                face.sameSense(),
                face.color(),
                face.transparency(),
                face.pbr(),
                face.layers(),
                reduceLoopPoints(face.loops(), loopFactor),
                reduceTrianglePoints(face.triangles(), triangleFactor),
                face.surface(),
                face.uvLoops()
        );
    }

    private static List<PointPayload> reduceTrianglePoints(List<PointPayload> triangles, int factor) {
        if (factor <= 1 || triangles.size() <= 3) {
            return triangles;
        }
        int triangleCount = triangles.size() / 3;
        List<PointPayload> reduced = new ArrayList<>(Math.max(3, triangles.size() / factor));
        for (int triangleIndex = 0; triangleIndex < triangleCount; triangleIndex += factor) {
            int base = triangleIndex * 3;
            reduced.add(triangles.get(base));
            reduced.add(triangles.get(base + 1));
            reduced.add(triangles.get(base + 2));
        }
        return List.copyOf(reduced);
    }

    private static List<LoopPayload> reduceLoopPoints(List<LoopPayload> loops, int factor) {
        if (factor <= 1) {
            return loops;
        }
        List<LoopPayload> reduced = new ArrayList<>(loops.size());
        for (LoopPayload loop : loops) {
            if (loop.points().size() <= 2) {
                reduced.add(loop);
                continue;
            }
            List<PointPayload> points = new ArrayList<>(Math.max(2, loop.points().size() / factor));
            for (int index = 0; index < loop.points().size(); index += factor) {
                points.add(loop.points().get(index));
            }
            PointPayload last = loop.points().getLast();
            if (!points.getLast().equals(last)) {
                points.add(last);
            }
            reduced.add(new LoopPayload(loop.outer(), List.copyOf(points)));
        }
        return List.copyOf(reduced);
    }

    private static int countTrianglePoints(PreviewPayload payload) {
        int count = payload.faces().stream().mapToInt(face -> face.triangles().size()).sum();
        count += payload.representations().stream()
                .flatMap(representation -> representation.faces().stream())
                .mapToInt(face -> face.triangles().size())
                .sum();
        return count;
    }

    private static int countLoopPoints(PreviewPayload payload) {
        int count = payload.faces().stream()
                .flatMap(face -> face.loops().stream())
                .mapToInt(loop -> loop.points().size())
                .sum();
        count += payload.representations().stream()
                .flatMap(representation -> representation.faces().stream())
                .flatMap(face -> face.loops().stream())
                .mapToInt(loop -> loop.points().size())
                .sum();
        return count;
    }

    private static int countEdgePoints(PreviewPayload payload) {
        int count = payload.edges().stream().mapToInt(edge -> edge.points().size()).sum();
        count += payload.representations().stream()
                .flatMap(representation -> representation.edges().stream())
                .mapToInt(edge -> edge.points().size())
                .sum();
        return count;
    }

    private static int countPmiPoints(PreviewPayload payload) {
        return payload.pmi().stream().mapToInt(item -> item.leader().size() + 1).sum();
    }

    private static void includeGeometry(BoundsAccumulator bounds, GeometryCollection geometry) {
        for (FacePayload face : geometry.faces()) {
            for (LoopPayload loop : face.loops()) {
                for (PointPayload point : loop.points()) {
                    bounds.include(point);
                }
            }
        }
        for (EdgePayload edge : geometry.edges()) {
            for (PointPayload point : edge.points()) {
                bounds.include(point);
            }
        }
    }

    private static void includeAssembly(BoundsAccumulator bounds, AssemblyData assembly) {
        Map<Integer, RepresentationPayload> byId = assembly.representations().stream()
                .collect(Collectors.toMap(RepresentationPayload::id, representation -> representation, (left, right) -> left, LinkedHashMap::new));
        for (InstancePayload instance : assembly.instances()) {
            for (Integer representationId : instance.representationIds()) {
                RepresentationPayload representation = byId.get(representationId);
                if (representation == null) {
                    continue;
                }
                for (FacePayload face : representation.faces()) {
                    for (LoopPayload loop : face.loops()) {
                        for (PointPayload point : loop.points()) {
                            bounds.include(transform(point, instance.worldMatrix()));
                        }
                    }
                }
                for (EdgePayload edge : representation.edges()) {
                    for (PointPayload point : edge.points()) {
                        bounds.include(transform(point, instance.worldMatrix()));
                    }
                }
            }
        }
    }

    private static void includeBounds(BoundsAccumulator target, BoundsPayload bounds) {
        target.include(bounds.min());
        target.include(bounds.max());
    }

    private static BoundsAccumulator copyBounds(BoundsAccumulator source) {
        BoundsAccumulator copy = new BoundsAccumulator();
        if (!source.isEmpty()) {
            copy.minX = source.minX;
            copy.minY = source.minY;
            copy.minZ = source.minZ;
            copy.maxX = source.maxX;
            copy.maxY = source.maxY;
            copy.maxZ = source.maxZ;
        }
        return copy;
    }

    private static ValidationPayload buildValidationPayload(
            GeometryCollection legacyGeometry,
            AssemblyData assembly,
            BoundsAccumulator bounds,
            Map<Integer, StepEntity> resolved
    ) {
        GeometrySummary summary = assembly.instances().isEmpty()
                ? summarizeGeometry(legacyGeometry)
                : assembly.summary();
        PointPayload center = bounds.isEmpty()
                ? new PointPayload(0.0, 0.0, 0.0)
                : new PointPayload(
                        (bounds.minX + bounds.maxX) * 0.5,
                        (bounds.minY + bounds.maxY) * 0.5,
                        (bounds.minZ + bounds.maxZ) * 0.5
                );
        double sizeX = bounds.isEmpty() ? 0.0 : bounds.maxX - bounds.minX;
        double sizeY = bounds.isEmpty() ? 0.0 : bounds.maxY - bounds.minY;
        double sizeZ = bounds.isEmpty() ? 0.0 : bounds.maxZ - bounds.minZ;
        return new ValidationPayload(
                assembly.representations().size(),
                assembly.instances().size(),
                summary.faceCount(),
                summary.edgeCount(),
                summary.approxSurfaceArea(),
                summary.approxEdgeLength(),
                center,
                buildValidationReport(
                        resolved,
                        summary,
                        new ValidationContext(
                                assembly.representations().size(),
                                assembly.instances().size(),
                                center,
                                sizeX,
                                sizeY,
                                sizeZ
                        )
                )
        );
    }

    private static GeometrySummary summarizeGeometry(GeometryCollection geometry) {
        return new GeometrySummary(
                geometry.faces().size(),
                geometry.edges().size(),
                approximateSurfaceArea(geometry.faces()),
                approximateEdgeLength(geometry.edges())
        );
    }

    private static AssemblyMetrics measureAssembly(
            List<RepresentationPayload> representations,
            List<InstancePayload> instances
    ) {
        Map<Integer, RepresentationPayload> byId = representations.stream()
                .collect(Collectors.toMap(RepresentationPayload::id, representation -> representation, (left, right) -> left, LinkedHashMap::new));
        int faceCount = 0;
        int edgeCount = 0;
        double area = 0.0;
        double edgeLength = 0.0;
        BoundsAccumulator bounds = new BoundsAccumulator();
        for (InstancePayload instance : instances) {
            for (Integer representationId : instance.representationIds()) {
                RepresentationPayload representation = byId.get(representationId);
                if (representation == null) {
                    continue;
                }
                faceCount += representation.faces().size();
                edgeCount += representation.edges().size();
                area += approximateSurfaceArea(representation.faces(), instance.worldMatrix());
                edgeLength += approximateEdgeLength(representation.edges(), instance.worldMatrix());
                includeRepresentationBounds(bounds, representation, instance.worldMatrix());
            }
        }
        return new AssemblyMetrics(
                new GeometrySummary(faceCount, edgeCount, area, edgeLength),
                bounds.toPayload()
        );
    }

    private static void includeRepresentationBounds(
            BoundsAccumulator bounds,
            RepresentationPayload representation,
            double[] matrix
    ) {
        for (FacePayload face : representation.faces()) {
            for (LoopPayload loop : face.loops()) {
                for (PointPayload point : loop.points()) {
                    bounds.include(transform(point, matrix));
                }
            }
        }
        for (EdgePayload edge : representation.edges()) {
            for (PointPayload point : edge.points()) {
                bounds.include(transform(point, matrix));
            }
        }
    }

    private static double approximateSurfaceArea(List<FacePayload> faces) {
        double total = 0.0;
        for (FacePayload face : faces) {
            if (!face.triangles().isEmpty()) {
                total += triangleArea(face.triangles());
            } else {
                total += loopArea(face);
            }
        }
        return total;
    }

    private static double approximateSurfaceArea(List<FacePayload> faces, double[] matrix) {
        double total = 0.0;
        for (FacePayload face : faces) {
            if (!face.triangles().isEmpty()) {
                total += triangleArea(face.triangles(), matrix);
            } else {
                total += loopArea(face, matrix);
            }
        }
        return total;
    }

    private static double approximateEdgeLength(List<EdgePayload> edges) {
        double total = 0.0;
        for (EdgePayload edge : edges) {
            for (int i = 0; i + 1 < edge.points().size(); i++) {
                total += distance(edge.points().get(i), edge.points().get(i + 1));
            }
        }
        return total;
    }

    private static double approximateEdgeLength(List<EdgePayload> edges, double[] matrix) {
        double total = 0.0;
        for (EdgePayload edge : edges) {
            for (int i = 0; i + 1 < edge.points().size(); i++) {
                total += distance(transform(edge.points().get(i), matrix), transform(edge.points().get(i + 1), matrix));
            }
        }
        return total;
    }

    private static double triangleArea(List<PointPayload> triangles) {
        double total = 0.0;
        for (int i = 0; i + 2 < triangles.size(); i += 3) {
            PointPayload a = triangles.get(i);
            PointPayload b = triangles.get(i + 1);
            PointPayload c = triangles.get(i + 2);
            double abx = b.x() - a.x();
            double aby = b.y() - a.y();
            double abz = b.z() - a.z();
            double acx = c.x() - a.x();
            double acy = c.y() - a.y();
            double acz = c.z() - a.z();
            double cx = aby * acz - abz * acy;
            double cy = abz * acx - abx * acz;
            double cz = abx * acy - aby * acx;
            total += 0.5 * Math.sqrt(cx * cx + cy * cy + cz * cz);
        }
        return total;
    }

    private static double triangleArea(List<PointPayload> triangles, double[] matrix) {
        double total = 0.0;
        for (int i = 0; i + 2 < triangles.size(); i += 3) {
            PointPayload a = transform(triangles.get(i), matrix);
            PointPayload b = transform(triangles.get(i + 1), matrix);
            PointPayload c = transform(triangles.get(i + 2), matrix);
            double abx = b.x() - a.x();
            double aby = b.y() - a.y();
            double abz = b.z() - a.z();
            double acx = c.x() - a.x();
            double acy = c.y() - a.y();
            double acz = c.z() - a.z();
            double cx = aby * acz - abz * acy;
            double cy = abz * acx - abx * acz;
            double cz = abx * acy - aby * acx;
            total += 0.5 * Math.sqrt(cx * cx + cy * cy + cz * cz);
        }
        return total;
    }

    private static double loopArea(FacePayload face) {
        double total = 0.0;
        for (LoopPayload loop : face.loops()) {
            double area = polygonArea(loop.points(), face.normal());
            total += loop.outer() ? area : -area;
        }
        return Math.abs(total);
    }

    private static double loopArea(FacePayload face, double[] matrix) {
        double total = 0.0;
        for (LoopPayload loop : face.loops()) {
            double area = polygonArea(loop.points(), face.normal(), matrix);
            total += loop.outer() ? area : -area;
        }
        return Math.abs(total);
    }

    private static double polygonArea(List<PointPayload> points, VectorPayload normal) {
        if (points.size() < 3) {
            return 0.0;
        }
        double nx = normal.x();
        double ny = normal.y();
        double nz = normal.z();
        double length = Math.sqrt(nx * nx + ny * ny + nz * nz);
        if (length <= Epsilon.EPS) {
            return 0.0;
        }
        nx /= length;
        ny /= length;
        nz /= length;
        double areaVectorX = 0.0;
        double areaVectorY = 0.0;
        double areaVectorZ = 0.0;
        for (int i = 0; i < points.size(); i++) {
            PointPayload current = points.get(i);
            PointPayload next = points.get((i + 1) % points.size());
            areaVectorX += current.y() * next.z() - current.z() * next.y();
            areaVectorY += current.z() * next.x() - current.x() * next.z();
            areaVectorZ += current.x() * next.y() - current.y() * next.x();
        }
        return Math.abs((areaVectorX * nx + areaVectorY * ny + areaVectorZ * nz) * 0.5);
    }

    private static double polygonArea(List<PointPayload> points, VectorPayload normal, double[] matrix) {
        if (points.size() < 3) {
            return 0.0;
        }
        double nx = normal.x();
        double ny = normal.y();
        double nz = normal.z();
        double length = Math.sqrt(nx * nx + ny * ny + nz * nz);
        if (length <= Epsilon.EPS) {
            return 0.0;
        }
        nx /= length;
        ny /= length;
        nz /= length;
        double areaVectorX = 0.0;
        double areaVectorY = 0.0;
        double areaVectorZ = 0.0;
        for (int i = 0; i < points.size(); i++) {
            PointPayload current = transform(points.get(i), matrix);
            PointPayload next = transform(points.get((i + 1) % points.size()), matrix);
            areaVectorX += current.y() * next.z() - current.z() * next.y();
            areaVectorY += current.z() * next.x() - current.x() * next.z();
            areaVectorZ += current.x() * next.y() - current.y() * next.x();
        }
        return Math.abs((areaVectorX * nx + areaVectorY * ny + areaVectorZ * nz) * 0.5);
    }

    private static double distance(PointPayload a, PointPayload b) {
        double dx = b.x() - a.x();
        double dy = b.y() - a.y();
        double dz = b.z() - a.z();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    private static double[] mappedItemMatrix(StepMappedItem mappedItem, StepCadBuilder builder) {
        StepRepresentationMap mappingSource = mappedItem.mappingSource();
        if (!(mappingSource.mappedOrigin() instanceof com.minicad.step.model.StepAxis2Placement3D originPlacement)) {
            return null;
        }
        double[] sourceMatrix = StepAssemblyGraphBuilder.matrixForPlacement(originPlacement);
        double[] targetMatrix;
        if (mappedItem.mappingTarget() instanceof com.minicad.step.model.StepCartesianTransformationOperator transformation) {
            targetMatrix = matrixForTransformationOperator(transformation, builder);
        } else if (mappedItem.mappingTarget() instanceof com.minicad.step.model.StepAxis2Placement3D targetPlacement) {
            targetMatrix = StepAssemblyGraphBuilder.matrixForPlacement(targetPlacement);
        } else {
            return null;
        }
        return StepAssemblyGraphBuilder.multiplyMatrices(
                targetMatrix,
                StepAssemblyGraphBuilder.inverseRigidTransform(sourceMatrix)
        );
    }

    private static double[] matrixForTransformationOperator(
            com.minicad.step.model.StepCartesianTransformationOperator transformation,
            StepCadBuilder builder
    ) {
        Vector3 axis1 = transformation.axis1() == null
                ? new Vector3(1.0, 0.0, 0.0)
                : builder.buildDirection(transformation.axis1().id()).asVector();
        Vector3 axis2;
        if (transformation.axis2() != null) {
            axis2 = builder.buildDirection(transformation.axis2().id()).asVector();
        } else {
            Vector3 fallback = new Vector3(0.0, 1.0, 0.0);
            axis2 = axis1.cross(fallback).isZero() ? new Vector3(0.0, 0.0, 1.0) : fallback;
        }
        Vector3 axis3;
        if (transformation.axis3() != null) {
            axis3 = builder.buildDirection(transformation.axis3().id()).asVector();
        } else {
            Vector3 cross = axis1.cross(axis2);
            axis3 = cross.isZero() ? new Vector3(0.0, 0.0, 1.0) : cross.normalize().asVector();
        }
        double scale = transformation.scale() == null ? 1.0 : transformation.scale();
        CartesianPoint origin = builder.buildPoint(transformation.localOrigin().id());
        return new double[]{
                axis1.x() * scale, axis2.x() * scale, axis3.x() * scale, origin.x(),
                axis1.y() * scale, axis2.y() * scale, axis3.y() * scale, origin.y(),
                axis1.z() * scale, axis2.z() * scale, axis3.z() * scale, origin.z(),
                0.0, 0.0, 0.0, 1.0
        };
    }

    private static EdgePayload transformMappedEdge(EdgePayload edge, int mappedItemId, double[] matrix) {
        return transformMappedEdge(edge, mappedItemId, matrix, null, null);
    }

    private static EdgePayload transformMappedEdge(
            EdgePayload edge,
            int mappedItemId,
            double[] matrix,
            String sourceType,
            Integer sourceStepId
    ) {
        List<PointPayload> points = edge.points().stream()
                .map(point -> transform(point, matrix))
                .toList();
        return new EdgePayload(
                mappedPayloadId(mappedItemId, edge.stepId(), 1),
                points,
                transformMappedCurve(edge.curve(), matrix, sourceType, sourceStepId)
        );
    }

    private static EdgeCurvePayload transformMappedCurve(
            EdgeCurvePayload curve,
            double[] matrix,
            String sourceType,
            Integer sourceStepId
    ) {
        if (curve == null) {
            return null;
        }
        List<Double> center = curve.center() == null
                ? null
                : pointList(transform(new PointPayload(curve.center().get(0), curve.center().get(1), curve.center().get(2)), matrix));
        List<Double> axis = curve.axis() == null
                ? null
                : vectorList(transform(new VectorPayload(curve.axis().get(0), curve.axis().get(1), curve.axis().get(2)), matrix));
        List<Double> xDirection = curve.xDirection() == null
                ? null
                : vectorList(transform(new VectorPayload(curve.xDirection().get(0), curve.xDirection().get(1), curve.xDirection().get(2)), matrix));
        List<Double> refDirection = curve.refDirection() == null
                ? null
                : vectorList(transform(new VectorPayload(curve.refDirection().get(0), curve.refDirection().get(1), curve.refDirection().get(2)), matrix));
        return new EdgeCurvePayload(
                curve.stepId(),
                curve.type(),
                curve.basisType(),
                curve.basisStepId(),
                center,
                axis,
                xDirection,
                curve.radius(),
                curve.semiAxis1(),
                curve.semiAxis2(),
                curve.orientation(),
                curve.senseAgreement(),
                curve.offsetDistance(),
                curve.selfIntersect(),
                refDirection,
                curve.transformScale(),
                curve.masterRepresentation(),
                curve.associatedSurfaceTypes(),
                curve.associatedSurfaceStepIds(),
                sourceType != null ? sourceType : curve.sourceType(),
                sourceStepId != null ? sourceStepId : curve.sourceStepId(),
                curve.startAngle(),
                curve.sweepAngle()
        );
    }

    private static FacePayload transformMappedFace(
            FacePayload face,
            int mappedItemId,
            double[] matrix,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<LoopPayload> loops = face.loops().stream()
                .map(loop -> new LoopPayload(
                        loop.outer(),
                        loop.points().stream().map(point -> transform(point, matrix)).toList()
                ))
                .toList();
        List<PointPayload> triangles = face.triangles().stream()
                .map(point -> transform(point, matrix))
                .toList();
        int[] rgb = metadata.rgb() != null ? metadata.rgb() : null;
        ColorPayload color = rgb == null ? face.color() : toColorPayload(rgb);
        double transparency = metadata.transparency() > 0 ? metadata.transparency() : face.transparency();
        PbrPayload pbr = metadata.pbr() != null ? toPbrPayload(metadata.pbr()) : face.pbr();
        List<String> layers = metadata.layers().isEmpty() ? face.layers() : metadata.layers();
        return new FacePayload(
                mappedPayloadId(mappedItemId, face.stepId(), 2),
                face.name(),
                face.surfaceType(),
                transform(face.origin(), matrix),
                transform(face.normal(), matrix),
                face.sameSense(),
                color,
                transparency,
                pbr,
                layers,
                loops,
                triangles,
                null,
                List.of()
        );
    }

    private static int mappedPayloadId(int mappedItemId, int sourceId, int salt) {
        return -Math.abs(mappedItemId * 10_000 + sourceId * 10 + salt);
    }

    private static List<PmiPayload> buildPmiPayloads(
            Map<Integer, StepEntity> resolved,
            AssemblyData assembly,
            StepCadBuilder builder
    ) {
        Map<Integer, List<PmiTargetPayload>> targetsByUsageId = new LinkedHashMap<>();
        Map<Integer, List<String>> instanceIdsByTargetId = buildInstanceIdsByTargetId(assembly);
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepGeometricItemSpecificUsage usage) {
                appendPmiTarget(
                        targetsByUsageId,
                        usage.usage().id(),
                        usage.identifiedItem(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        "GEOMETRIC_ITEM_SPECIFIC_USAGE",
                        usage.id()
                );
            } else if (entity instanceof StepChainBasedGeometricItemSpecificUsage usage) {
                appendPmiTarget(
                        targetsByUsageId,
                        usage.usage().id(),
                        usage.identifiedItem(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE",
                        usage.id()
                );
            } else if (entity instanceof StepItemIdentifiedRepresentationUsage usage) {
                appendRepresentationBacklinkTarget(
                        targetsByUsageId,
                        usage.identifiedItem(),
                        usage.usedRepresentation(),
                        instanceIdsByTargetId,
                        "ITEM_IDENTIFIED_REPRESENTATION_USAGE",
                        usage.id()
                );
                appendAttachedRepresentationRelationshipTargets(
                        targetsByUsageId,
                        usage.identifiedItem(),
                        usage.usedRepresentation(),
                        resolved,
                        instanceIdsByTargetId
                );
            } else if (entity instanceof StepChainBasedItemIdentifiedRepresentationUsage usage) {
                appendRepresentationBacklinkTarget(
                        targetsByUsageId,
                        usage.identifiedItem(),
                        usage.leaf(),
                        instanceIdsByTargetId,
                        "CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE",
                        usage.id()
                );
                appendAttachedRepresentationRelationshipTargets(
                        targetsByUsageId,
                        usage.identifiedItem(),
                        usage.leaf(),
                        resolved,
                        instanceIdsByTargetId
                );
            } else if (entity instanceof StepPlacedTarget usage) {
                appendRepresentationBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), instanceIdsByTargetId, "PLACED_TARGET", usage.id());
                appendAttachedRepresentationRelationshipTargets(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), resolved, instanceIdsByTargetId);
                appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.definition(), resolved, instanceIdsByTargetId);
            } else if (entity instanceof StepDraughtingModelItemAssociation usage) {
                appendRepresentationBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), instanceIdsByTargetId, "DRAUGHTING_MODEL_ITEM_ASSOCIATION", usage.id());
                appendAttachedRepresentationRelationshipTargets(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), resolved, instanceIdsByTargetId);
                appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.definition(), resolved, instanceIdsByTargetId);
            } else if (entity instanceof StepDraughtingModelItemAssociationWithPlaceholder usage) {
                appendRepresentationBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), instanceIdsByTargetId, "DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER", usage.id());
                appendAttachedRepresentationRelationshipTargets(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), resolved, instanceIdsByTargetId);
                appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.definition(), resolved, instanceIdsByTargetId);
            } else if (entity instanceof StepPmiRequirementItemAssociation usage) {
                appendRepresentationBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), instanceIdsByTargetId, "PMI_REQUIREMENT_ITEM_ASSOCIATION", usage.id());
                appendAttachedRepresentationRelationshipTargets(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), resolved, instanceIdsByTargetId);
                appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.requirement(), instanceIdsByTargetId);
                appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.requirement(), instanceIdsByTargetId);
                appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.definition(), resolved, instanceIdsByTargetId);
                appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.requirement(), resolved, instanceIdsByTargetId);
            } else if (entity instanceof StepMechanicalDesignRequirementItemAssociation usage) {
                appendRepresentationBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), instanceIdsByTargetId, "MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION", usage.id());
                appendAttachedRepresentationRelationshipTargets(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), resolved, instanceIdsByTargetId);
                appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.requirement(), instanceIdsByTargetId);
                appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.requirement(), instanceIdsByTargetId);
                appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.definition(), resolved, instanceIdsByTargetId);
                appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.requirement(), resolved, instanceIdsByTargetId);
            }
        }
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepDraughtingCalloutRelationship relationship) {
                propagateCalloutTargets(targetsByUsageId, relationship);
            }
        }
        List<PmiPayload> pmi = new ArrayList<>();
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepDraughtingCallout callout) {
                PmiPayload payload = toPmiPayload(callout, targetsByUsageId.getOrDefault(callout.id(), List.of()), builder);
                if (payload != null) {
                    pmi.add(payload);
                }
            } else if (entity instanceof StepAnnotationTextOccurrence textOccurrence) {
                CartesianPoint position = pointFromAnnotationPoint(textOccurrence.position(), builder);
                if (position == null) {
                    continue;
                }
                List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(textOccurrence.id(), List.of());
                pmi.add(new PmiPayload(
                        textOccurrence.name(),
                        textOccurrence.text(),
                        toPointPayload(position),
                        List.of(),
                        targets.stream().map(PmiTargetPayload::id).toList(),
                        targets
                ));
            } else if (entity instanceof StepAnnotationPointOccurrence pointOccurrence) {
                CartesianPoint position = pointFromAnnotationPoint(pointOccurrence.item(), builder);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(pointOccurrence.id(), List.of());
                    pmi.add(toStandalonePointPmi(pointOccurrence.id(), pointOccurrence.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationFillAreaOccurrence fillAreaOccurrence) {
                CartesianPoint position = pointFromAnnotationPoint(fillAreaOccurrence.fillStyleTarget(), builder);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(fillAreaOccurrence.id(), List.of());
                    pmi.add(toStandalonePointPmi(fillAreaOccurrence.id(), fillAreaOccurrence.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationPlaceholderOccurrence placeholderOccurrence) {
                appendPlaceholderPmi(
                        placeholderOccurrence,
                        pmi,
                        builder,
                        targetsByUsageId.getOrDefault(placeholderOccurrence.id(), List.of()));
            } else if (entity instanceof StepAnnotationSymbolOccurrence symbolOccurrence) {
                CartesianPoint position = pointFromAnnotationOccurrence(symbolOccurrence.item(), builder);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(symbolOccurrence.id(), List.of());
                    pmi.add(toStandalonePointPmi(symbolOccurrence.id(), symbolOccurrence.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationSymbol annotationSymbol) {
                CartesianPoint position = pointFromAnnotationSymbol(annotationSymbol);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(annotationSymbol.id(), List.of());
                    pmi.add(toStandalonePointPmi(annotationSymbol.id(), annotationSymbol.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationText annotationText) {
                CartesianPoint position = pointFromPlacement(annotationText.mappingTarget());
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(annotationText.id(), List.of());
                    pmi.add(toStandalonePointPmi(annotationText.id(), annotationText.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationTextCharacter annotationTextCharacter) {
                CartesianPoint position = pointFromPlacement(annotationTextCharacter.mappingTarget());
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(annotationTextCharacter.id(), List.of());
                    pmi.add(toStandalonePointPmi(annotationTextCharacter.id(), annotationTextCharacter.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationFillArea fillArea) {
                CartesianPoint position = pointFromAnnotationFillArea(fillArea, builder);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(fillArea.id(), List.of());
                    pmi.add(toStandalonePointPmi(fillArea.id(), fillArea.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationSubfigureOccurrence subfigureOccurrence) {
                CartesianPoint position = pointFromAnnotationOccurrence(subfigureOccurrence.item(), builder);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(subfigureOccurrence.id(), List.of());
                    pmi.add(toStandalonePointPmi(subfigureOccurrence.id(), subfigureOccurrence.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationPlane annotationPlane) {
                appendAnnotationPlanePmi(
                        annotationPlane,
                        pmi,
                        builder,
                        targetsByUsageId.getOrDefault(annotationPlane.id(), List.of()));
            } else if (entity instanceof StepDraughtingAnnotationOccurrence annotationOccurrence) {
                appendDraughtingAnnotationPmi(
                        annotationOccurrence,
                        pmi,
                        builder,
                        targetsByUsageId.getOrDefault(annotationOccurrence.id(), List.of()));
            } else if (entity instanceof StepAnnotationOccurrenceRelationship relationship) {
                appendAnnotationOccurrenceRelationshipPmi(relationship, pmi, builder);
            } else if (entity instanceof StepPointSet pointSet) {
                appendPointSetPmi(pointSet, pmi, builder);
            } else if (entity instanceof StepVertexShell vertexShell) {
                pmi.add(toStandalonePointPmi(
                        vertexShell.id(),
                        vertexShell.name(),
                        pointFromStep(vertexShell.extent().loopVertex().point())
                ));
            } else if (entity instanceof StepGeometricReplica replica && "POINT_REPLICA".equals(replica.entityName())) {
                CartesianPoint position = pointFromReplica(replica, builder);
                if (position != null) {
                    pmi.add(toStandalonePointPmi(replica.id(), replica.name(), position));
                }
            }
        }
        return List.copyOf(pmi);
    }

    private static void appendPlaceholderPmi(
            StepAnnotationPlaceholderOccurrence placeholderOccurrence,
            List<PmiPayload> pmi,
            StepCadBuilder builder,
            List<PmiTargetPayload> targets
    ) {
        List<CartesianPoint> positions = new ArrayList<>();
        collectPlaceholderPositions(placeholderOccurrence.item(), positions, builder);
        int pointIndex = 0;
        for (CartesianPoint position : positions) {
            String name = placeholderOccurrence.name();
            if (positions.size() > 1) {
                name = name + "[" + pointIndex + "]";
            }
            pmi.add(toStandalonePointPmi(
                    placeholderOccurrence.id() * 1000 + pointIndex,
                    name,
                    position,
                    targets));
            pointIndex++;
        }
    }

    private static void appendAnnotationPlanePmi(
            StepAnnotationPlane annotationPlane,
            List<PmiPayload> pmi,
            StepCadBuilder builder,
            List<PmiTargetPayload> targets
    ) {
        int pointIndex = 0;
        for (StepEntity element : annotationPlane.elements()) {
            CartesianPoint position = null;
            if (element instanceof StepGeometricSet geometricSet) {
                position = pointFromGeometricSet(geometricSet, builder);
            }
            if (position == null) {
                position = pointFromAnnotationOccurrence(element, builder);
            }
            if (position == null) {
                position = pointFromAnnotationPoint(element, builder);
            }
            if (position == null) {
                continue;
            }
            String name = annotationPlane.elements().size() == 1
                    ? annotationPlane.name()
                    : annotationPlane.name() + "[" + pointIndex + "]";
            pmi.add(toStandalonePointPmi(
                    annotationPlane.id() * 1000 + pointIndex,
                    name,
                    position,
                    targets));
            pointIndex++;
        }
    }

    private static void appendAnnotationOccurrenceRelationshipPmi(
            StepAnnotationOccurrenceRelationship relationship,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = pointFromAnnotationOccurrence(relationship.relatedAnnotationOccurrence(), builder);
        if (position == null) {
            position = pointFromAnnotationOccurrence(relationship.relatingAnnotationOccurrence(), builder);
        }
        if (position != null) {
            pmi.add(toStandalonePointPmi(relationship.id(), relationship.name(), position));
        }
    }

    private static void appendDraughtingAnnotationPmi(
            StepDraughtingAnnotationOccurrence annotationOccurrence,
            List<PmiPayload> pmi,
            StepCadBuilder builder,
            List<PmiTargetPayload> targets
    ) {
        CartesianPoint position = pointFromAnnotationOccurrence(annotationOccurrence.item(), builder);
        if (position != null) {
            pmi.add(toStandalonePointPmi(annotationOccurrence.id(), annotationOccurrence.name(), position, targets));
        }
    }

    private static void appendPointSetPmi(StepPointSet pointSet, List<PmiPayload> pmi, StepCadBuilder builder) {
        int pointIndex = 0;
        for (StepEntity item : pointSet.points()) {
            CartesianPoint position = pointFromAnnotationPoint(item, builder);
            if (position == null) {
                continue;
            }
            String pointName = pointSet.points().size() == 1
                    ? pointSet.name()
                    : pointSet.name() + "[" + pointIndex + "]";
            pmi.add(toStandalonePointPmi(pointSet.id() * 1000 + pointIndex, pointName, position));
            pointIndex++;
        }
    }

    private static PmiPayload toStandalonePointPmi(int id, String name, CartesianPoint position) {
        return toStandalonePointPmi(id, name, position, List.of());
    }

    private static PmiPayload toStandalonePointPmi(
            int id,
            String name,
            CartesianPoint position,
            List<PmiTargetPayload> targets
    ) {
        return new PmiPayload(
                name == null || name.isBlank() ? "POINT_" + id : name,
                "",
                toPointPayload(position),
                List.of(),
                targets.stream().map(PmiTargetPayload::id).toList(),
                targets
        );
    }

    private static boolean isSupportedPmiUsageCarrier(StepEntity entity) {
        return entity instanceof StepDraughtingCallout
                || entity instanceof StepAnnotationSymbol
                || entity instanceof StepAnnotationText
                || entity instanceof StepAnnotationTextCharacter
                || entity instanceof StepAnnotationFillArea
                || entity instanceof StepAnnotationTextOccurrence
                || entity instanceof StepAnnotationPointOccurrence
                || entity instanceof StepAnnotationFillAreaOccurrence
                || entity instanceof StepAnnotationPlaceholderOccurrence
                || entity instanceof StepAnnotationPlane
                || entity instanceof StepAnnotationSymbolOccurrence
                || entity instanceof StepAnnotationSubfigureOccurrence
                || entity instanceof StepDraughtingAnnotationOccurrence
                || entity instanceof StepTerminatorSymbol;
    }

    private static CartesianPoint pointFromAnnotationPoint(StepEntity item, StepCadBuilder builder) {
        if (item instanceof StepCartesianPoint point) {
            return pointFromStep(point);
        }
        if (item instanceof StepVertexPoint vertexPoint) {
            return pointFromStep(vertexPoint.point());
        }
        if (item instanceof StepVertexShell vertexShell) {
            return pointFromStep(vertexShell.extent().loopVertex().point());
        }
        if (item instanceof StepPointSet pointSet) {
            return pointFromPointSet(pointSet, builder);
        }
        if (item instanceof StepGeometricSet geometricSet) {
            return pointFromGeometricSet(geometricSet, builder);
        }
        if (item instanceof StepGeometricCurveSet curveSet) {
            return pointFromGeometricCurveSet(curveSet, builder);
        }
        if (item instanceof StepAnnotationSymbol
                || item instanceof StepAnnotationText
                || item instanceof StepAnnotationTextCharacter
                || item instanceof StepAnnotationFillArea) {
            return pointFromAnnotationOccurrence(item, builder);
        }
        if (item instanceof StepAnnotationPointOccurrence
                || item instanceof StepAnnotationFillAreaOccurrence
                || item instanceof StepAnnotationTextOccurrence
                || item instanceof StepAnnotationPlaceholderOccurrence
                || item instanceof StepAnnotationSymbolOccurrence
                || item instanceof StepAnnotationSubfigureOccurrence
                || item instanceof StepDraughtingAnnotationOccurrence
                || item instanceof StepAnnotationPlane) {
            return pointFromAnnotationOccurrence(item, builder);
        }
        if (builder != null && item instanceof StepGeometricReplica replica && "POINT_REPLICA".equals(replica.entityName())) {
            return pointFromReplica(replica, builder);
        }
        return null;
    }

    private static CartesianPoint pointFromAnnotationSymbol(StepAnnotationSymbol annotationSymbol) {
        return pointFromPlacement(annotationSymbol.mappingTarget());
    }

    private static CartesianPoint pointFromAnnotationFillArea(
            StepAnnotationFillArea fillArea,
            StepCadBuilder builder
    ) {
        List<CartesianPoint> sampled = sampleAnnotationFillAreaPoints(fillArea, builder);
        if (sampled == null || sampled.isEmpty()) {
            return null;
        }
        return sampled.getFirst();
    }

    private static CartesianPoint pointFromAnnotationOccurrence(StepEntity occurrence, StepCadBuilder builder) {
        return switch (occurrence) {
            case StepAnnotationPointOccurrence pointOccurrence -> pointFromAnnotationPoint(pointOccurrence.item(), builder);
            case StepAnnotationCurveOccurrence curveOccurrence -> pointFromCurveCarrier(curveOccurrence.item(), builder);
            case StepLeaderCurve leaderCurve -> pointFromCurveCarrier(leaderCurve.item(), builder);
            case StepDimensionCurve dimensionCurve -> pointFromCurveCarrier(dimensionCurve.item(), builder);
            case StepProjectionCurve projectionCurve -> pointFromCurveCarrier(projectionCurve.item(), builder);
            case StepAnnotationFillAreaOccurrence fillAreaOccurrence -> pointFromAnnotationPoint(fillAreaOccurrence.fillStyleTarget(), builder);
            case StepAnnotationFillArea fillArea -> pointFromAnnotationFillArea(fillArea, builder);
            case StepAnnotationSymbol annotationSymbol -> pointFromAnnotationSymbol(annotationSymbol);
            case StepAnnotationSymbolOccurrence symbolOccurrence -> pointFromAnnotationOccurrence(symbolOccurrence.item(), builder);
            case StepAnnotationSubfigureOccurrence subfigureOccurrence -> pointFromAnnotationOccurrence(subfigureOccurrence.item(), builder);
            case StepAnnotationPlaceholderOccurrence placeholderOccurrence -> pointFromPlaceholderItem(placeholderOccurrence.item(), builder);
            case StepAnnotationPlane annotationPlane -> pointFromAnnotationPlane(annotationPlane, builder);
            case StepAnnotationText annotationText -> pointFromPlacement(annotationText.mappingTarget());
            case StepAnnotationTextCharacter annotationTextCharacter -> pointFromPlacement(annotationTextCharacter.mappingTarget());
            case StepAnnotationTextOccurrence textOccurrence -> pointFromAnnotationPoint(textOccurrence.position(), builder);
            case StepDraughtingAnnotationOccurrence annotationOccurrence -> pointFromAnnotationOccurrence(annotationOccurrence.item(), builder);
            case StepTerminatorSymbol terminatorSymbol -> {
                CartesianPoint position = pointFromAnnotationOccurrence(terminatorSymbol.item(), builder);
                if (position == null) {
                    position = pointFromAnnotationOccurrence(terminatorSymbol.annotatedCurve(), builder);
                }
                yield position;
            }
            case StepPointSet pointSet -> pointFromPointSet(pointSet, builder);
            case StepGeometricSet geometricSet -> pointFromGeometricSet(geometricSet, builder);
            case StepGeometricCurveSet curveSet -> pointFromGeometricCurveSet(curveSet, builder);
            case StepVertexShell vertexShell -> pointFromStep(vertexShell.extent().loopVertex().point());
            case StepGeometricReplica replica when "POINT_REPLICA".equals(replica.entityName()) -> builder == null ? null : pointFromReplica(replica, builder);
            default -> null;
        };
    }

    private static CartesianPoint pointFromCurveCarrier(StepEntity item, StepCadBuilder builder) {
        List<CartesianPoint> sampled = sampleLooseEdgePoints(item, builder);
        if (sampled == null || sampled.isEmpty()) {
            return null;
        }
        return sampled.getFirst();
    }

    private static CartesianPoint pointFromGeometricSet(StepGeometricSet geometricSet, StepCadBuilder builder) {
        for (StepEntity element : geometricSet.elements()) {
            CartesianPoint point = pointFromAnnotationOccurrence(element, builder);
            if (point != null) {
                return point;
            }
            point = pointFromAnnotationPoint(element, builder);
            if (point != null) {
                return point;
            }
            point = pointFromCurveCarrier(element, builder);
            if (point != null) {
                return point;
            }
        }
        return null;
    }

    private static CartesianPoint pointFromGeometricCurveSet(StepGeometricCurveSet curveSet, StepCadBuilder builder) {
        for (StepEntity element : curveSet.elements()) {
            CartesianPoint point = pointFromAnnotationOccurrence(element, builder);
            if (point != null) {
                return point;
            }
            point = pointFromAnnotationPoint(element, builder);
            if (point != null) {
                return point;
            }
            point = pointFromCurveCarrier(element, builder);
            if (point != null) {
                return point;
            }
        }
        return null;
    }

    private static CartesianPoint pointFromAnnotationPlane(StepAnnotationPlane annotationPlane, StepCadBuilder builder) {
        for (StepEntity element : annotationPlane.elements()) {
            if (element instanceof StepGeometricSet geometricSet) {
                CartesianPoint point = pointFromGeometricSet(geometricSet, builder);
                if (point != null) {
                    return point;
                }
            }
            CartesianPoint point = pointFromAnnotationOccurrence(element, builder);
            if (point != null) {
                return point;
            }
            point = pointFromAnnotationPoint(element, builder);
            if (point != null) {
                return point;
            }
        }
        return null;
    }

    private static CartesianPoint pointFromPointSet(StepPointSet pointSet, StepCadBuilder builder) {
        for (StepEntity item : pointSet.points()) {
            CartesianPoint point = pointFromAnnotationPoint(item, builder);
            if (point != null) {
                return point;
            }
        }
        return null;
    }

    private static CartesianPoint pointFromPlaceholderItem(StepEntity item, StepCadBuilder builder) {
        if (item instanceof StepGeometricSet geometricSet) {
            return pointFromGeometricSet(geometricSet, builder);
        }
        if (item instanceof StepGeometricCurveSet curveSet) {
            return pointFromGeometricCurveSet(curveSet, builder);
        }
        if (item instanceof StepPointSet pointSet) {
            return pointFromPointSet(pointSet, builder);
        }
        if (item instanceof StepAnnotationPlane annotationPlane) {
            return pointFromAnnotationPlane(annotationPlane, builder);
        }
        CartesianPoint point = pointFromAnnotationOccurrence(item, builder);
        if (point != null) {
            return point;
        }
        return pointFromAnnotationPoint(item, builder);
    }

    private static void collectPlaceholderPositions(
            StepEntity item,
            List<CartesianPoint> positions,
            StepCadBuilder builder
    ) {
        if (item instanceof StepPointSet pointSet) {
            for (StepEntity point : pointSet.points()) {
                collectPlaceholderPositions(point, positions, builder);
            }
            return;
        }
        if (item instanceof StepGeometricSet geometricSet) {
            for (StepEntity element : geometricSet.elements()) {
                collectPlaceholderPositions(element, positions, builder);
            }
            return;
        }
        if (item instanceof StepGeometricCurveSet curveSet) {
            for (StepEntity element : curveSet.elements()) {
                collectPlaceholderPositions(element, positions, builder);
            }
            return;
        }
        if (item instanceof StepAnnotationPlane annotationPlane) {
            for (StepEntity element : annotationPlane.elements()) {
                collectPlaceholderPositions(element, positions, builder);
            }
            return;
        }
        CartesianPoint point = pointFromAnnotationOccurrence(item, builder);
        if (point == null) {
            point = pointFromAnnotationPoint(item, builder);
        }
        if (point != null) {
            positions.add(point);
        }
    }

    private static CartesianPoint pointFromPlacement(StepEntity placement) {
        if (placement instanceof StepAxis2Placement3D placement3D) {
            return pointFromStep(placement3D.location());
        }
        if (placement instanceof StepAxis2Placement2D placement2D) {
            StepCartesianPoint point = placement2D.location();
            return new CartesianPoint(point.coordinates().get(0), point.coordinates().get(1), 0.0);
        }
        return null;
    }

    private static CartesianPoint pointFromReplica(StepGeometricReplica replica, StepCadBuilder builder) {
        if (replica.parent() instanceof StepCartesianPoint point) {
            return transformPoint(pointFromStep(point), replica.transformation(), builder);
        }
        if (replica.parent() instanceof StepVertexPoint vertexPoint) {
            return transformPoint(pointFromStep(vertexPoint.point()), replica.transformation(), builder);
        }
        return null;
    }

    private static CartesianPoint transformPoint(
            CartesianPoint point,
            com.minicad.step.model.StepCartesianTransformationOperator transformation,
            StepCadBuilder builder
    ) {
        Vector3 axis1 = transformation.axis1() == null
                ? new Vector3(1.0, 0.0, 0.0)
                : builder.buildDirection(transformation.axis1().id()).asVector();
        Vector3 axis2;
        if (transformation.axis2() != null) {
            axis2 = builder.buildDirection(transformation.axis2().id()).asVector();
        } else {
            Vector3 fallback = new Vector3(0.0, 1.0, 0.0);
            axis2 = axis1.cross(fallback).isZero() ? new Vector3(0.0, 0.0, 1.0) : fallback;
        }
        Vector3 axis3;
        if (transformation.axis3() != null) {
            axis3 = builder.buildDirection(transformation.axis3().id()).asVector();
        } else {
            Vector3 cross = axis1.cross(axis2);
            axis3 = cross.isZero() ? new Vector3(0.0, 0.0, 1.0) : cross.normalize().asVector();
        }
        double scale = transformation.scale() == null ? 1.0 : transformation.scale();
        CartesianPoint origin = builder.buildPoint(transformation.localOrigin().id());
        Vector3 offset = axis1.scale(point.x() * scale)
                .add(axis2.scale(point.y() * scale))
                .add(axis3.scale(point.z() * scale));
        return origin.add(offset);
    }

    private static Map<Integer, List<String>> buildInstanceIdsByTargetId(AssemblyData assembly) {
        Map<Integer, RepresentationPayload> representationsById = assembly.representations().stream()
                .collect(Collectors.toMap(RepresentationPayload::id, representation -> representation, (left, right) -> left, LinkedHashMap::new));
        Map<Integer, Set<String>> targetInstances = new LinkedHashMap<>();
        for (InstancePayload instance : assembly.instances()) {
            for (Integer representationId : instance.representationIds()) {
                targetInstances.computeIfAbsent(representationId, ignored -> new LinkedHashSet<>()).add(instance.id());
                RepresentationPayload representation = representationsById.get(representationId);
                if (representation == null) {
                    continue;
                }
                for (FacePayload face : representation.faces()) {
                    targetInstances.computeIfAbsent(face.stepId(), ignored -> new LinkedHashSet<>()).add(instance.id());
                }
                for (EdgePayload edge : representation.edges()) {
                    targetInstances.computeIfAbsent(edge.stepId(), ignored -> new LinkedHashSet<>()).add(instance.id());
                }
            }
        }
        Map<Integer, List<String>> byTargetId = new LinkedHashMap<>();
        for (Map.Entry<Integer, Set<String>> entry : targetInstances.entrySet()) {
            byTargetId.put(entry.getKey(), List.copyOf(entry.getValue()));
        }
        return Map.copyOf(byTargetId);
    }

    private static PmiPayload toPmiPayload(
            StepDraughtingCallout callout,
            List<PmiTargetPayload> targets,
            StepCadBuilder builder
    ) {
        StepAnnotationTextOccurrence text = null;
        List<PointPayload> leader = new ArrayList<>();
        for (StepEntity content : callout.contents()) {
            if (content instanceof StepAnnotationTextOccurrence annotationText) {
                text = annotationText;
            } else {
                appendPmiLeader(content, leader, builder);
            }
        }
        if (text == null) {
            return null;
        }
        CartesianPoint position = pointFromAnnotationPoint(text.position(), builder);
        if (position == null) {
            return null;
        }
        return new PmiPayload(
                callout.name(),
                text.text(),
                toPointPayload(position),
                List.copyOf(leader),
                targets.stream().map(PmiTargetPayload::id).toList(),
                List.copyOf(targets)
        );
    }

    private static void appendPmiLeader(
            StepEntity content,
            List<PointPayload> leader,
            StepCadBuilder builder
    ) {
        if (content instanceof StepGeometricSet geometricSet) {
            for (StepEntity element : geometricSet.elements()) {
                appendPmiLeader(element, leader, builder);
            }
            return;
        }
        if (content instanceof StepGeometricCurveSet curveSet) {
            for (StepEntity element : curveSet.elements()) {
                appendPmiLeader(element, leader, builder);
            }
            return;
        }
        if (content instanceof StepPointSet pointSet) {
            for (StepEntity point : pointSet.points()) {
                appendPmiLeader(point, leader, builder);
            }
            return;
        }
        if (content instanceof StepAnnotationPlaceholderOccurrence placeholderOccurrence) {
            appendPmiLeader(placeholderOccurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepAnnotationPlane annotationPlane) {
            for (StepEntity element : annotationPlane.elements()) {
                appendPmiLeader(element, leader, builder);
            }
            return;
        }
        if (content instanceof StepFaceBasedSurfaceModel surfaceModel) {
            for (StepEntity faceSet : surfaceModel.faceSets()) {
                appendPmiLeader(faceSet, leader, builder);
            }
            return;
        }
        if (content instanceof StepShellBasedSurfaceModel surfaceModel) {
            for (StepEntity shell : surfaceModel.shells()) {
                appendPmiLeader(shell, leader, builder);
            }
            return;
        }
        if (content instanceof StepManifoldSolidBrep solid) {
            appendPmiLeader(solid.outer(), leader, builder);
            return;
        }
        if (content instanceof StepBrepWithVoids solid) {
            appendPmiLeader(solid.outer(), leader, builder);
            for (StepEntity voidShell : solid.voids()) {
                appendPmiLeader(voidShell, leader, builder);
            }
            return;
        }
        if (content instanceof StepSweptAreaSolid
                || content instanceof StepSolidReplica
                || content instanceof StepCsgSolid
                || content instanceof StepCsgPrimitive
                || content instanceof StepBooleanResult
                || content instanceof StepBooleanClippingResult
                || content instanceof StepSweptDiskSolid
                || content instanceof StepExtrudedAreaSolidTapered
                || content instanceof StepRevolvedAreaSolidTapered
                || content instanceof StepSurfaceCurveSweptAreaSolid
                || content instanceof StepPolygonalBoundedHalfSpace
                || content instanceof StepComplexClippingResult) {
            appendPmiLeaderForSolid(content, leader, builder);
            return;
        }
        if (content instanceof StepAdvancedFace face) {
            for (StepFaceBound bound : face.bounds()) {
                appendPmiLeader(bound, leader, builder);
            }
            return;
        }
        if (content instanceof StepFaceSurface face) {
            for (StepFaceBound bound : face.bounds()) {
                appendPmiLeader(bound, leader, builder);
            }
            return;
        }
        if (content instanceof StepOrientedFace face) {
            appendPmiLeader(face.faceElement(), leader, builder);
            return;
        }
        if (content instanceof StepFaceBound faceBound) {
            appendPmiLeader(faceBound.loop(), leader, builder);
            return;
        }
        if (content instanceof StepOpenShell shell) {
            for (StepFaceEntity face : shell.faces()) {
                appendPmiLeader(face, leader, builder);
            }
            return;
        }
        if (content instanceof StepSurfacedOpenShell shell) {
            for (StepFaceEntity face : shell.faces()) {
                appendPmiLeader(face, leader, builder);
            }
            return;
        }
        if (content instanceof StepOrientedOpenShell shell) {
            appendPmiLeader(shell.openShellElement(), leader, builder);
            return;
        }
        if (content instanceof StepClosedShell shell) {
            for (StepFaceEntity face : shell.faces()) {
                appendPmiLeader(face, leader, builder);
            }
            return;
        }
        if (content instanceof StepOrientedClosedShell shell) {
            appendPmiLeader(shell.closedShellElement(), leader, builder);
            return;
        }
        if (content instanceof StepConnectedFaceSet faceSet) {
            for (StepFaceEntity face : faceSet.faces()) {
                appendPmiLeader(face, leader, builder);
            }
            return;
        }
        if (content instanceof StepConnectedFaceSubSet faceSet) {
            for (StepFaceEntity face : faceSet.faces()) {
                appendPmiLeader(face, leader, builder);
            }
            return;
        }
        if (content instanceof StepAnnotationPointOccurrence pointOccurrence) {
            appendPmiLeader(pointOccurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepAnnotationCurveOccurrence occurrence) {
            appendPmiLeader(occurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepAnnotationFillArea fillArea) {
            List<CartesianPoint> sampled = sampleAnnotationFillAreaPoints(fillArea, builder);
            if (sampled != null) {
                for (CartesianPoint point : sampled) {
                    leader.add(toPointPayload(point));
                }
            }
            return;
        }
        if (content instanceof StepAnnotationFillAreaOccurrence fillAreaOccurrence) {
            appendPmiLeader(fillAreaOccurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepAnnotationSymbol annotationSymbol) {
            List<CartesianPoint> sampled = sampleLooseEdgePoints(annotationSymbol, builder);
            if (sampled != null) {
                for (CartesianPoint point : sampled) {
                    leader.add(toPointPayload(point));
                }
            }
            return;
        }
        if (content instanceof StepAnnotationSymbolOccurrence symbolOccurrence) {
            appendPmiLeader(symbolOccurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepAnnotationSubfigureOccurrence subfigureOccurrence) {
            appendPmiLeader(subfigureOccurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepAnnotationText annotationText) {
            List<CartesianPoint> sampled = sampleLooseEdgePoints(annotationText, builder);
            if (sampled != null) {
                for (CartesianPoint point : sampled) {
                    leader.add(toPointPayload(point));
                }
            }
            return;
        }
        if (content instanceof StepAnnotationTextCharacter annotationTextCharacter) {
            List<CartesianPoint> sampled = sampleLooseEdgePoints(annotationTextCharacter, builder);
            if (sampled != null) {
                for (CartesianPoint point : sampled) {
                    leader.add(toPointPayload(point));
                }
            }
            return;
        }
        if (content instanceof StepDimensionCurve dimensionCurve) {
            appendPmiLeader(dimensionCurve.item(), leader, builder);
            return;
        }
        if (content instanceof StepLeaderCurve leaderCurve) {
            appendPmiLeader(leaderCurve.item(), leader, builder);
            return;
        }
        if (content instanceof StepProjectionCurve projectionCurve) {
            appendPmiLeader(projectionCurve.item(), leader, builder);
            return;
        }
        if (content instanceof StepDraughtingAnnotationOccurrence annotationOccurrence) {
            appendPmiLeader(annotationOccurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepTerminatorSymbol terminatorSymbol) {
            appendPmiLeader(terminatorSymbol.annotatedCurve(), leader, builder);
            return;
        }
        if (content instanceof StepPath path) {
            appendPmiPathLeader(path.edges(), leader, builder);
            return;
        }
        if (content instanceof StepOpenPath path) {
            appendPmiPathLeader(path.edges(), leader, builder);
            return;
        }
        if (content instanceof StepSubpath subpath) {
            appendPmiPathLeader(subpath.edges(), leader, builder);
            return;
        }
        if (content instanceof StepOrientedPath orientedPath) {
            appendPmiPathLeader(orientedPath.edges(), leader, builder);
            return;
        }
        if (content instanceof StepConnectedEdgeSet connectedEdgeSet) {
            for (StepEntity edge : connectedEdgeSet.edges()) {
                appendPmiLeader(edge, leader, builder);
            }
            return;
        }
        if (content instanceof StepEdgeBasedWireframeModel wireframeModel) {
            for (StepConnectedEdgeSet boundary : wireframeModel.boundaries()) {
                appendPmiLeader(boundary, leader, builder);
            }
            return;
        }
        if (content instanceof StepShellBasedWireframeModel wireframeModel) {
            for (StepEntity boundary : wireframeModel.boundaries()) {
                appendPmiLeader(boundary, leader, builder);
            }
            return;
        }
        if (content instanceof StepWireShell wireShell) {
            for (StepEntity loop : wireShell.loops()) {
                appendPmiLeader(loop, leader, builder);
            }
            return;
        }
        if (content instanceof StepEdgeLoop edgeLoop) {
            appendPmiPathLeader(edgeLoop.edges(), leader, builder);
            return;
        }
        if (content instanceof StepVertexLoop vertexLoop) {
            leader.add(toPointPayload(pointFromStep(vertexLoop.loopVertex().point())));
            return;
        }
        if (content instanceof StepPolyLoop polyLoop) {
            for (StepCartesianPoint point : polyLoop.polygon()) {
                leader.add(toPointPayload(pointFromStep(point)));
            }
            return;
        }
        if (content instanceof StepVertexShell vertexShell) {
            leader.add(toPointPayload(pointFromStep(vertexShell.extent().loopVertex().point())));
            return;
        }
        if (content instanceof StepGeometricReplica replica && "POINT_REPLICA".equals(replica.entityName())) {
            CartesianPoint point = pointFromReplica(replica, builder);
            if (point != null) {
                leader.add(toPointPayload(point));
            }
            return;
        }
        if (content instanceof StepCartesianPoint point) {
            leader.add(toPointPayload(pointFromStep(point)));
            return;
        }
        if (content instanceof StepVertexPoint vertexPoint) {
            leader.add(toPointPayload(pointFromStep(vertexPoint.point())));
            return;
        }
        List<CartesianPoint> sampled = sampleLooseEdgePoints(content, builder);
        if (sampled == null) {
            return;
        }
        for (CartesianPoint point : sampled) {
            leader.add(toPointPayload(point));
        }
    }

    private static void appendPmiLeaderForSolid(
            StepEntity solidEntity,
            List<PointPayload> leader,
            StepCadBuilder builder
    ) {
        try {
            Solid solid = builder.buildSolid(solidEntity.id());
            for (Face face : solid.outerShell().faces()) {
                appendPmiLeader(face, leader, builder);
            }
            for (Shell voidShell : solid.voidShells()) {
                for (Face face : voidShell.faces()) {
                    appendPmiLeader(face, leader, builder);
                }
            }
        } catch (UnsupportedGeometryException | StepResolutionException | TopologyException ex) {
            // Callout leader extraction is best-effort; unsupported solid content should not fail export.
        }
    }

    private static void appendPmiLeader(
            Face face,
            List<PointPayload> leader,
            StepCadBuilder builder
    ) {
        for (FaceBound bound : face.bounds()) {
            appendPmiLeader(bound.loop(), leader, builder);
        }
    }

    private static void appendPmiLeader(
            Loop loop,
            List<PointPayload> leader,
            StepCadBuilder builder
    ) {
        if (loop instanceof EdgeLoop edgeLoop) {
            for (OrientedEdge edge : edgeLoop.edges()) {
                appendTopologyEdgeLeader(edge, leader);
            }
            return;
        }
        if (loop instanceof VertexLoop vertexLoop) {
            leader.add(toPointPayload(vertexLoop.vertex().point()));
            return;
        }
        if (loop instanceof PolyLoop polyLoop) {
            for (CartesianPoint point : polyLoop.points()) {
                leader.add(toPointPayload(point));
            }
        }
    }

    private static void appendTopologyEdgeLeader(
            OrientedEdge orientedEdge,
            List<PointPayload> leader
    ) {
        List<CartesianPoint> points = sampleLooseCurve(orientedEdge.edge().curve());
        if (!orientedEdge.orientation()) {
            List<CartesianPoint> reversed = new ArrayList<>(points);
            Collections.reverse(reversed);
            points = reversed;
        }
        for (CartesianPoint point : points) {
            leader.add(toPointPayload(point));
        }
    }

    private static void appendPmiPathLeader(
            List<StepOrientedEdge> edges,
            List<PointPayload> leader,
            StepCadBuilder builder
    ) {
        for (StepOrientedEdge edge : edges) {
            List<CartesianPoint> points = sampleLooseEdgePoints(edge.edgeElement(), builder);
            if (points == null) {
                continue;
            }
            for (CartesianPoint point : points) {
                leader.add(toPointPayload(point));
            }
        }
    }

    private static String pmiTargetType(StepEntity target) {
        if (target instanceof StepFaceEntity) {
            return "face";
        }
        if (target instanceof com.minicad.step.model.StepEdgeCurve
                || target instanceof StepSubedge
                || target instanceof StepOrientedEdge) {
            return "edge";
        }
        if (target instanceof StepPath
                || target instanceof StepOpenPath
                || target instanceof StepSubpath
                || target instanceof StepOrientedPath) {
            return "path";
        }
        if (target instanceof StepConnectedEdgeSet) {
            return "edge_set";
        }
        if (target instanceof StepPointSet) {
            return "point_set";
        }
        if (target instanceof StepAnnotationSymbol) {
            return "annotation_symbol";
        }
        if (target instanceof StepAnnotationText) {
            return "annotation_text";
        }
        if (target instanceof StepAnnotationTextCharacter) {
            return "annotation_text_character";
        }
        if (target instanceof StepAnnotationFillArea) {
            return "annotation_fill_area";
        }
        if (target instanceof StepGeometricSet) {
            return "geometric_set";
        }
        if (target instanceof StepGeometricCurveSet) {
            return "curve_set";
        }
        if (target instanceof StepOpenShell
                || target instanceof StepSurfacedOpenShell
                || target instanceof StepOrientedOpenShell
                || target instanceof StepClosedShell
                || target instanceof StepOrientedClosedShell) {
            return "shell";
        }
        if (target instanceof StepWireShell) {
            return "wire_shell";
        }
        if (target instanceof StepVertexShell) {
            return "vertex_shell";
        }
        if (target instanceof StepEdgeLoop
                || target instanceof StepVertexLoop
                || target instanceof StepPolyLoop) {
            return "loop";
        }
        if (target instanceof StepConnectedFaceSet || target instanceof StepConnectedFaceSubSet) {
            return "face_set";
        }
        if (target instanceof StepFaceBasedSurfaceModel || target instanceof StepShellBasedSurfaceModel) {
            return "surface_model";
        }
        if (target instanceof StepEdgeBasedWireframeModel || target instanceof StepShellBasedWireframeModel) {
            return "wireframe_model";
        }
        if (target instanceof StepManifoldSolidBrep
                || target instanceof StepBrepWithVoids
                || target instanceof StepSweptAreaSolid
                || target instanceof StepSolidReplica
                || target instanceof StepCsgSolid
                || target instanceof StepCsgPrimitive
                || target instanceof StepBooleanResult
                || target instanceof StepBooleanClippingResult
                || target instanceof StepSweptDiskSolid
                || target instanceof StepExtrudedAreaSolidTapered
                || target instanceof StepRevolvedAreaSolidTapered
                || target instanceof StepSurfaceCurveSweptAreaSolid
                || target instanceof StepPolygonalBoundedHalfSpace
                || target instanceof StepComplexClippingResult) {
            return "solid";
        }
        if (target instanceof StepRepresentation) {
            return "representation";
        }
        return "entity";
    }

    private static String pmiTargetName(StepEntity target) {
        if (target instanceof StepFaceEntity face) {
            return faceDisplayName(face);
        }
        if (target instanceof com.minicad.step.model.StepEdgeCurve edge) {
            return edge.name();
        }
        if (target instanceof StepSubedge subedge) {
            return subedge.name();
        }
        if (target instanceof StepOrientedEdge orientedEdge) {
            return orientedEdge.name();
        }
        if (target instanceof StepPath path) {
            return path.name();
        }
        if (target instanceof StepOpenPath path) {
            return path.name();
        }
        if (target instanceof StepSubpath subpath) {
            return subpath.name();
        }
        if (target instanceof StepOrientedPath orientedPath) {
            return orientedPath.name();
        }
        if (target instanceof StepConnectedEdgeSet edgeSet) {
            return edgeSet.name();
        }
        if (target instanceof StepPointSet pointSet) {
            return pointSet.name();
        }
        if (target instanceof StepAnnotationSymbol annotationSymbol) {
            return annotationSymbol.name();
        }
        if (target instanceof StepAnnotationText annotationText) {
            return annotationText.name();
        }
        if (target instanceof StepAnnotationTextCharacter annotationTextCharacter) {
            return annotationTextCharacter.name();
        }
        if (target instanceof StepAnnotationFillArea fillArea) {
            return fillArea.name();
        }
        if (target instanceof StepGeometricSet geometricSet) {
            return geometricSet.name();
        }
        if (target instanceof StepGeometricCurveSet curveSet) {
            return curveSet.name();
        }
        if (target instanceof StepOpenShell openShell) {
            return openShell.name();
        }
        if (target instanceof StepSurfacedOpenShell openShell) {
            return openShell.name();
        }
        if (target instanceof StepOrientedOpenShell openShell) {
            return openShell.name();
        }
        if (target instanceof StepClosedShell closedShell) {
            return closedShell.name();
        }
        if (target instanceof StepOrientedClosedShell closedShell) {
            return closedShell.name();
        }
        if (target instanceof StepWireShell wireShell) {
            return wireShell.name();
        }
        if (target instanceof StepVertexShell vertexShell) {
            return vertexShell.name();
        }
        if (target instanceof StepEdgeLoop edgeLoop) {
            return edgeLoop.name();
        }
        if (target instanceof StepVertexLoop vertexLoop) {
            return vertexLoop.name();
        }
        if (target instanceof StepPolyLoop polyLoop) {
            return polyLoop.name();
        }
        if (target instanceof StepConnectedFaceSet faceSet) {
            return faceSet.name();
        }
        if (target instanceof StepConnectedFaceSubSet faceSet) {
            return faceSet.name();
        }
        if (target instanceof StepFaceBasedSurfaceModel surfaceModel) {
            return surfaceModel.name();
        }
        if (target instanceof StepShellBasedSurfaceModel surfaceModel) {
            return surfaceModel.name();
        }
        if (target instanceof StepEdgeBasedWireframeModel wireframeModel) {
            return wireframeModel.name();
        }
        if (target instanceof StepShellBasedWireframeModel wireframeModel) {
            return wireframeModel.name();
        }
        if (target instanceof StepManifoldSolidBrep solid) {
            return solid.name();
        }
        if (target instanceof StepBrepWithVoids solid) {
            return solid.name();
        }
        if (target instanceof StepSweptAreaSolid solid) {
            return solid.name();
        }
        if (target instanceof StepSolidReplica solid) {
            return solid.name();
        }
        if (target instanceof StepCsgSolid solid) {
            return solid.name();
        }
        if (target instanceof StepCsgPrimitive solid) {
            return solid.name();
        }
        if (target instanceof StepBooleanResult solid) {
            return solid.name();
        }
        if (target instanceof StepBooleanClippingResult solid) {
            return solid.name();
        }
        if (target instanceof StepSweptDiskSolid solid) {
            return solid.name();
        }
        if (target instanceof StepComplexClippingResult solid) {
            return solid.name();
        }
        if (target instanceof StepRepresentation representation) {
            return representation.name();
        }
        return "";
    }

    private static void appendPmiTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepEntity target,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendPmiTarget(targetsByUsageId, usageId, target, instanceIdsByTargetId, null, null, null, null);
    }

    private static void appendPmiTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepEntity target,
            Map<Integer, List<String>> instanceIdsByTargetId,
            String viaRelationshipType,
            Integer viaRelationshipId
    ) {
        appendPmiTarget(targetsByUsageId, usageId, target, instanceIdsByTargetId, viaRelationshipType, viaRelationshipId, null, null);
    }

    private static void appendPmiTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepEntity target,
            Map<Integer, List<String>> instanceIdsByTargetId,
            String viaRelationshipType,
            Integer viaRelationshipId,
            String viaUsageType,
            Integer viaUsageId
    ) {
        appendPmiTarget(
                targetsByUsageId,
                usageId,
                target,
                instanceIdsByTargetId,
                viaRelationshipType,
                viaRelationshipId,
                viaUsageType,
                viaUsageId,
                null,
                null
        );
    }

    private static void appendPmiTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepEntity target,
            Map<Integer, List<String>> instanceIdsByTargetId,
            String viaRelationshipType,
            Integer viaRelationshipId,
            String viaUsageType,
            Integer viaUsageId,
            String viaDefinitionType,
            Integer viaDefinitionId
    ) {
        PmiTargetPayload payload = new PmiTargetPayload(
                target.id(),
                pmiTargetType(target),
                pmiTargetName(target),
                List.copyOf(instanceIdsByTargetId.getOrDefault(target.id(), List.of())),
                viaRelationshipType,
                viaRelationshipId,
                viaUsageType,
                viaUsageId,
                viaDefinitionType,
                viaDefinitionId
        );
        List<PmiTargetPayload> targets = targetsByUsageId.computeIfAbsent(usageId, ignored -> new ArrayList<>());
        if (!targets.contains(payload)) {
            targets.add(payload);
        }
    }

    private static void appendRepresentationBacklinkTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepRepresentation representation,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendRepresentationBacklinkTarget(targetsByUsageId, identifiedItem, representation, instanceIdsByTargetId, null, null);
    }

    private static void appendRepresentationBacklinkTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepRepresentation representation,
            Map<Integer, List<String>> instanceIdsByTargetId,
            String viaUsageType,
            Integer viaUsageId
    ) {
        if (!isSupportedPmiUsageCarrier(identifiedItem)) {
            return;
        }
        appendPmiTarget(targetsByUsageId, identifiedItem.id(), representation, instanceIdsByTargetId, null, null, viaUsageType, viaUsageId);
    }

    private static void appendDefinitionBacklinkTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepRepresentation representation,
            StepEntity definition,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!isSupportedPmiUsageCarrier(identifiedItem) || definition == null) {
            return;
        }
        appendPmiTarget(
                targetsByUsageId,
                identifiedItem.id(),
                representation,
                instanceIdsByTargetId,
                null,
                null,
                null,
                null,
                definitionTypeName(definition),
                definition.id()
        );
    }

    private static void appendExistingRepresentationDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!isSupportedPmiUsageCarrier(identifiedItem) || definition == null) {
            return;
        }
        List<PmiTargetPayload> existingTargets = List.copyOf(targetsByUsageId.getOrDefault(identifiedItem.id(), List.of()));
        for (PmiTargetPayload target : existingTargets) {
            if (!"representation".equals(target.type())) {
                continue;
            }
            PmiTargetPayload payload = new PmiTargetPayload(
                    target.id(),
                    target.type(),
                    target.name(),
                    List.copyOf(instanceIdsByTargetId.getOrDefault(target.id(), target.instanceIds())),
                    null,
                    null,
                    null,
                    null,
                    definitionTypeName(definition),
                    definition.id()
            );
            List<PmiTargetPayload> targets = targetsByUsageId.computeIfAbsent(identifiedItem.id(), ignored -> new ArrayList<>());
            if (!targets.contains(payload)) {
                targets.add(payload);
            }
        }
    }

    private static void appendRelationshipBacklinkTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepRepresentation representation,
            StepEntity definition,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!isSupportedPmiUsageCarrier(identifiedItem)) {
            return;
        }
        if (definition instanceof StepAnnotationOccurrenceRelationship relationship) {
            appendPmiTarget(
                    targetsByUsageId,
                    identifiedItem.id(),
                    representation,
                    instanceIdsByTargetId,
                    relationship.entityName(),
                    relationship.id()
            );
        } else if (definition instanceof StepDraughtingCalloutRelationship relationship) {
            appendPmiTarget(
                    targetsByUsageId,
                    identifiedItem.id(),
                    representation,
                    instanceIdsByTargetId,
                    "DRAUGHTING_CALLOUT_RELATIONSHIP",
                    relationship.id()
            );
        }
    }

    private static void appendSemanticDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!isSupportedPmiUsageCarrier(identifiedItem)) {
            return;
        }
        if (definition instanceof StepAnnotationOccurrenceRelationship relationship) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendRelationshipSemanticTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.entityName(),
                    relationship.id(),
                    relationship.relatingAnnotationOccurrence(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendRelationshipSemanticTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.entityName(),
                    relationship.id(),
                    relationship.relatedAnnotationOccurrence(),
                    resolved,
                    instanceIdsByTargetId
            );
            return;
        }
        if (definition instanceof StepDraughtingCalloutRelationship relationship) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendRelationshipSemanticTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    "DRAUGHTING_CALLOUT_RELATIONSHIP",
                    relationship.id(),
                    relationship.relatingCallout(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendRelationshipSemanticTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    "DRAUGHTING_CALLOUT_RELATIONSHIP",
                    relationship.id(),
                    relationship.relatedCallout(),
                    resolved,
                    instanceIdsByTargetId
            );
            return;
        }
        if (definition instanceof StepPropertyDefinitionRelationship relationship) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendPropertyRepresentationLinkTargets(
                    targetsByUsageId,
                    identifiedItem,
                    relationship.relatingPropertyDefinition(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendPropertyRepresentationLinkTargets(
                    targetsByUsageId,
                    identifiedItem,
                    relationship.relatedPropertyDefinition(),
                    resolved,
                    instanceIdsByTargetId
            );
        }
        if (definition instanceof StepPropertyDefinition propertyDefinition) {
            appendPropertyDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    propertyDefinition,
                    resolved,
                    instanceIdsByTargetId
            );
            appendPropertyRepresentationLinkTargets(
                    targetsByUsageId,
                    identifiedItem,
                    propertyDefinition,
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    propertyDefinition.definition(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepGeneralPropertyRelationship relationship) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatingGeneralProperty(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatedGeneralProperty(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepShapeAspectRelationship relationship) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatingShapeAspect(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatedShapeAspect(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepGeneralProperty generalProperty) {
            appendGeneralPropertyRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    generalProperty,
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepShapeAspect shapeAspect) {
            appendShapeAspectRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    shapeAspect,
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepProduct product) {
            appendProductRelationshipTargets(targetsByUsageId, identifiedItem.id(), product, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductDefinitionFormation formation) {
            appendProductDefinitionFormationRelationshipTargets(targetsByUsageId, identifiedItem.id(), formation, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductDefinition productDefinition) {
            appendProductDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), productDefinition, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductRelationship relationship) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relationship, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatingProduct(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatedProduct(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepProductDefinitionFormationRelationship relationship) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relationship, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatingFormation(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatedFormation(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepGroupRelationship relationship) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relationship, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatingGroup(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatedGroup(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepDocumentRelationship relationship) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relationship, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatingDocument(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatedDocument(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepOrganizationRelationship relationship) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relationship, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatingOrganization(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatedOrganization(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepEffectivityRelationship relationship) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relationship, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatingEffectivity(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatedEffectivity(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepProductCategoryRelationship relationship) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relationship, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.category(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.subCategory(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepGroup group) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, group, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, group, resolved, instanceIdsByTargetId);
            appendGroupRelationshipTargets(targetsByUsageId, identifiedItem.id(), group, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDocument document) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, document, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, document, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    document.kind(),
                    instanceIdsByTargetId
            );
            appendDocumentRelationshipTargets(targetsByUsageId, identifiedItem.id(), document, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDocumentReference reference) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, reference, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, reference, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    reference.assignedDocument(),
                    instanceIdsByTargetId
            );
            appendDocumentRelationshipTargets(targetsByUsageId, identifiedItem.id(), reference.assignedDocument(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    reference.assignedDocument().kind(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAppliedDocumentReference reference) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, reference, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, reference, resolved, instanceIdsByTargetId);
            appendDocumentRelationshipTargets(targetsByUsageId, identifiedItem.id(), reference.assignedDocument(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    reference.assignedDocument(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    reference.assignedDocument().kind(),
                    definitionTypeName(reference.assignedDocument().kind()),
                    reference.assignedDocument().kind().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    reference.assignedDocument().kind(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepApprovalAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedApproval(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedApproval(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedApproval().status(), instanceIdsByTargetId);
            appendApprovalDecorationTargets(targetsByUsageId, identifiedItem, assignment.assignedApproval(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSecurityClassificationAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedSecurityClassification(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedSecurityClassification(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedSecurityClassification().securityLevel(), instanceIdsByTargetId);
        } else if (definition instanceof StepContractAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedContract(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedContract(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedContract().kind(), instanceIdsByTargetId);
        } else if (definition instanceof StepCertificationAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedCertification(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedCertification(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedCertification().kind(), instanceIdsByTargetId);
        } else if (definition instanceof StepPersonAndOrganizationAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedPersonAndOrganization(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedPersonAndOrganization(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedPersonAndOrganization().person(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedPersonAndOrganization().organization(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), instanceIdsByTargetId);
        } else if (definition instanceof StepOrganizationAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedOrganization(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedOrganization(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), instanceIdsByTargetId);
        } else if (definition instanceof StepLanguageAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedLanguage(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedLanguage(), instanceIdsByTargetId);
        } else if (definition instanceof StepGroupAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedGroup(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedGroup(), instanceIdsByTargetId);
        } else if (definition instanceof StepClassificationAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedClass(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedClass(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), instanceIdsByTargetId);
        } else if (definition instanceof StepDateAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedDate(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedDate(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), instanceIdsByTargetId);
        } else if (definition instanceof StepDateTimeAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedDateAndTime(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedDateAndTime(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedDateAndTime().dateComponent(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedDateAndTime().timeComponent(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedDateAndTime().timeComponent().zone(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), instanceIdsByTargetId);
        } else if (definition instanceof StepIdentificationAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepExternalIdentificationAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assignment.source(), resolved, instanceIdsByTargetId);
            appendExternalSourceRelationshipTargets(targetsByUsageId, identifiedItem, assignment.source(), resolved, instanceIdsByTargetId);
            appendExternallyDefinedItemTargets(targetsByUsageId, identifiedItem, assignment.source(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOrganization organization) {
            appendOrganizationRelationshipTargets(targetsByUsageId, identifiedItem.id(), organization, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepEffectivity effectivity) {
            appendProductDefinitionEffectivityTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    effectivity,
                    resolved,
                    instanceIdsByTargetId
            );
            appendEffectivityRelationshipTargets(targetsByUsageId, identifiedItem.id(), effectivity, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductCategory category) {
            appendProductCategoryRelationshipTargets(targetsByUsageId, identifiedItem.id(), category, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepExternalSource source) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, source, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, source, resolved, instanceIdsByTargetId);
            appendExternallyDefinedItemTargets(
                    targetsByUsageId,
                    identifiedItem,
                    source,
                    resolved,
                    instanceIdsByTargetId
            );
            appendExternalSourceRelationshipTargets(targetsByUsageId, identifiedItem, source, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepExternallyDefinedItem item) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, item.source(), instanceIdsByTargetId);
            appendExternalSourceRelationshipTargets(targetsByUsageId, identifiedItem, item.source(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDocumentUsageConstraint documentUsageConstraint) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, documentUsageConstraint, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, documentUsageConstraint, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    documentUsageConstraint.source(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    documentUsageConstraint.source().kind(),
                    instanceIdsByTargetId
            );
            appendDocumentRelationshipTargets(targetsByUsageId, identifiedItem.id(), documentUsageConstraint.source(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepRepresentation representation) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, representation, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    representation,
                    resolved,
                    instanceIdsByTargetId
            );
            if (representation.context() != null) {
                appendNestedDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        representation.context(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
            for (StepEntity item : representation.items()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepProductDefinitionShape productDefinitionShape) {
            appendProductDefinitionShapeRepresentationTargets(
                    targetsByUsageId,
                    identifiedItem,
                    productDefinitionShape,
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepProductDefinition productDefinition) {
            appendProductDefinitionRepresentationTargets(
                    targetsByUsageId,
                    identifiedItem,
                    productDefinition,
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepNextAssemblyUsageOccurrence occurrence) {
            appendOccurrenceRepresentationTargets(
                    targetsByUsageId,
                    identifiedItem,
                    occurrence,
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepShapeAspectOccurrence occurrence) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    occurrence.definition(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepProductDefinitionRelationshipRelationship relationship) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relating(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.related(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepApprovalPersonOrganization assignment) {
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.personOrganization(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.personOrganization().person(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.personOrganization().organization(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.authorizedApproval(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.authorizedApproval().status(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.role(),
                    definitionTypeName(assignment.role()),
                    assignment.role().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.role(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepApprovalDateTime assignment) {
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.dateTime(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.dateTime().dateComponent(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.dateTime().timeComponent(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.dateTime().timeComponent().zone(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.datedApproval(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.datedApproval().status(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepCalendarDate calendarDate) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, calendarDate, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, calendarDate, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepNameAttribute attribute) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, attribute, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, attribute, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, attribute.namedItem(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDescriptionAttribute attribute) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, attribute, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, attribute, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, attribute.describedItem(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepIdAttribute attribute) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, attribute, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, attribute, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, attribute.identifiedItem(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepNameAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAppliedNameAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            for (StepEntity item : assignment.items()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepDateAndTime dateAndTime) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, dateAndTime, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, dateAndTime, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, dateAndTime.dateComponent(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, dateAndTime.timeComponent(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepLocalTime localTime) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, localTime, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, localTime, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, localTime.zone(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCoordinatedUniversalTimeOffset zone) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, zone, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, zone, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepApprovalStatus status) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, status, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, status, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSecurityClassificationLevel level) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, level, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, level, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepContractType kind) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, kind, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, kind, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCertificationType kind) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, kind, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, kind, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepApprovalRole role) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOrganizationRole role) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPersonAndOrganizationRole role) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepClassificationRole role) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDateRole role) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDateTimeRole role) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepIdentificationRole role) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDocumentType kind) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, kind, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, kind, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepApproval approval) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, approval, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, approval, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, approval.status(), resolved, instanceIdsByTargetId);
            appendApprovalDecorationTargets(targetsByUsageId, identifiedItem, approval, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSecurityClassification classification) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, classification, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, classification, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, classification.securityLevel(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepContract contract) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, contract, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, contract, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, contract.kind(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCertification certification) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, certification, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, certification, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, certification.kind(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPerson person) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, person, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, person, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPersonAndOrganization personAndOrganization) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, personAndOrganization, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, personAndOrganization, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, personAndOrganization.person(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, personAndOrganization.organization(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepLanguage language) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, language, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, language, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAppliedClassificationAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.role(),
                    definitionTypeName(assignment.role()),
                    assignment.role().id(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAppliedDateAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedDate(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.role(),
                    definitionTypeName(assignment.role()),
                    assignment.role().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.role(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAppliedDateTimeAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedDateAndTime(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedDateAndTime().timeComponent(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedDateAndTime().timeComponent().zone(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.role(),
                    definitionTypeName(assignment.role()),
                    assignment.role().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.role(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAppliedApprovalAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedApproval(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.assignedApproval().status(),
                    definitionTypeName(assignment.assignedApproval().status()),
                    assignment.assignedApproval().status().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedApproval().status(),
                    instanceIdsByTargetId
            );
            appendApprovalDecorationTargets(targetsByUsageId, identifiedItem, assignment.assignedApproval(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAppliedSecurityClassificationAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedSecurityClassification(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.assignedSecurityClassification().securityLevel(),
                    definitionTypeName(assignment.assignedSecurityClassification().securityLevel()),
                    assignment.assignedSecurityClassification().securityLevel().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedSecurityClassification().securityLevel(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAppliedContractAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedContract(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.assignedContract().kind(),
                    definitionTypeName(assignment.assignedContract().kind()),
                    assignment.assignedContract().kind().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedContract().kind(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAppliedCertificationAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedCertification(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.assignedCertification().kind(),
                    definitionTypeName(assignment.assignedCertification().kind()),
                    assignment.assignedCertification().kind().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedCertification().kind(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAppliedPersonAndOrganizationAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedPersonAndOrganization(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedPersonAndOrganization().person(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedPersonAndOrganization().organization(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.role(),
                    definitionTypeName(assignment.role()),
                    assignment.role().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.role(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAppliedOrganizationAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedOrganization(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.role(),
                    definitionTypeName(assignment.role()),
                    assignment.role().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.role(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAppliedLanguageAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.assignedLanguage(),
                    definitionTypeName(assignment.assignedLanguage()),
                    assignment.assignedLanguage().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedLanguage(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAppliedGroupAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedGroup(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAppliedIdentificationAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), resolved, instanceIdsByTargetId);
            for (StepEntity item : assignment.items()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepAppliedExternalIdentificationAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assignment.source(), resolved, instanceIdsByTargetId);
            appendExternalSourceRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.source(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExternallyDefinedItemTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.source(),
                    resolved,
                    instanceIdsByTargetId
            );
            for (StepEntity item : assignment.items()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepAnnotationCurveOccurrence occurrence) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAnnotationFillArea fillArea) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, fillArea, instanceIdsByTargetId);
            for (StepEntity boundary : fillArea.boundaries()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, boundary, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepAnnotationFillAreaOccurrence occurrence) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.fillStyleTarget(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAnnotationPlaceholderOccurrence occurrence) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAnnotationPointOccurrence occurrence) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAnnotationSymbolOccurrence occurrence) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAnnotationSubfigureOccurrence occurrence) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAnnotationTextOccurrence occurrence) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.position(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDraughtingAnnotationOccurrence occurrence) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTerminatorSymbol symbol) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, symbol, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, symbol.item(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, symbol.annotatedCurve(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPresentationStyleAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            for (StepEntity style : assignment.styles()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepSurfaceStyleUsage usage) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, usage, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, usage.style(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceSideStyle style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            for (StepEntity component : style.styles()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, component, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepSurfaceStyleFillArea style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.fillStyle(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepFillAreaStyle style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            for (StepFillAreaStyleColour component : style.styles()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, component, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepFillAreaStyleColour style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.colour(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCurveStyle style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.curveFont(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.colour(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleBoundary style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.style(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleParameterLine style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.style(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleControlGrid style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.style(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleSegmentationCurve style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.style(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleSilhouette style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.style(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCharacterGlyphStyleStroke style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.strokeStyle(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCharacterGlyphStyleOutline style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.outlineStyle(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCharacterGlyphStyleOutlineWithCharacteristics style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.outlineStyle(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.characteristics(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTextStyle style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.characterAppearance(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTextStyleWithSpacing style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.characterAppearance(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTextStyleWithBoxCharacteristics style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.characterAppearance(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTextStyleWithJustification style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.characterAppearance(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTextStyleWithMirror style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.characterAppearance(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.mirrorPlacement(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTextStyleForDefinedFont style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.textColour(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPointStyle style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.marker(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.colour(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSymbolColour style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.colour(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSymbolStyle style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.styleOfSymbol(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleTransparent style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleReflectanceAmbient style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleReflectanceAmbientDiffuse style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleReflectanceAmbientDiffuseSpecular style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.specularColour(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedSurfaceSideStyle style) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, style, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedColour colour) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, colour, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, colour, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDraughtingPreDefinedColour colour) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, colour, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, colour, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepColourRgb colour) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, colour, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, colour, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepColourSpecification colour) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, colour, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, colour, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepColour colour) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, colour, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, colour, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedCurveFont font) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, font, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, font, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDraughtingPreDefinedCurveFont font) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, font, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, font, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedTextFont font) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, font, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, font, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDraughtingPreDefinedTextFont font) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, font, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, font, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedTerminatorSymbol symbol) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, symbol, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, symbol, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedSymbol symbol) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, symbol, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, symbol, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedDimensionSymbol symbol) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, symbol, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, symbol, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedGeometricalToleranceSymbol symbol) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, symbol, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, symbol, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedItem item) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAnnotationPlane plane) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, plane, instanceIdsByTargetId);
            for (StepPresentationStyleAssignment style : plane.styles()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style, resolved, instanceIdsByTargetId);
            }
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, plane.item(), resolved, instanceIdsByTargetId);
            for (StepEntity element : plane.elements()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, element, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepDraughtingCallout callout) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, callout, instanceIdsByTargetId);
            for (StepEntity content : callout.contents()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, content, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepPresentationLayerAssignment assignment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            for (StepEntity item : assignment.assignedItems()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepStyledItem styledItem) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, styledItem, instanceIdsByTargetId);
            for (StepPresentationStyleAssignment style : styledItem.styles()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style, resolved, instanceIdsByTargetId);
            }
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, styledItem.item(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOverRidingStyledItem styledItem) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, styledItem, instanceIdsByTargetId);
            for (StepPresentationStyleAssignment style : styledItem.styles()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style, resolved, instanceIdsByTargetId);
            }
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, styledItem.item(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, styledItem.overRiddenStyle(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepRepresentationMap representationMap) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, representationMap, instanceIdsByTargetId);
            appendRepresentationMapDefinitionTargets(targetsByUsageId, identifiedItem, representationMap, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, representationMap, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSymbolRepresentationMap representationMap) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, representationMap, instanceIdsByTargetId);
            appendRepresentationMapDefinitionTargets(targetsByUsageId, identifiedItem, representationMap, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, representationMap, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepMappedItem mappedItem) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, mappedItem, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, mappedItem.mappingSource(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, mappedItem.mappingTarget(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, mappedItem, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepGeometricReplica replica) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, replica, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, replica.parent(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, replica.transformation(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, replica, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepItemDefinedTransformation transformation) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, transformation, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, transformation.transformItem1(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, transformation.transformItem2(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, transformation, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCartesianTransformationOperator transformation) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, transformation, instanceIdsByTargetId);
            if (transformation.axis1() != null) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, transformation.axis1(), resolved, instanceIdsByTargetId);
            }
            if (transformation.axis2() != null) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, transformation.axis2(), resolved, instanceIdsByTargetId);
            }
            if (transformation.axis3() != null) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, transformation.axis3(), resolved, instanceIdsByTargetId);
            }
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, transformation.localOrigin(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, transformation, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAxis1Placement placement) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, placement, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.location(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.axis(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAxis2Placement2D placement) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, placement, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.location(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.refDirection(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCartesianPoint point) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, point, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, point, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPoint point) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, point, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, point, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDirection direction) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, direction, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, direction, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepVector vector) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, vector, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, vector.orientation(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, vector, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAxis2Placement3D placement) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, placement, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.location(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.axis(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.refDirection(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, placement, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPlane plane) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, plane, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, plane.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, plane, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepGeometricSet set) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, set, instanceIdsByTargetId);
            for (StepEntity element : set.elements()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, element, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, set, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepGeometricCurveSet set) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, set, instanceIdsByTargetId);
            for (StepEntity element : set.elements()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, element, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, set, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPointSet set) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, set, instanceIdsByTargetId);
            for (StepEntity point : set.points()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, point, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, set, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPath path) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, path, instanceIdsByTargetId);
            for (StepOrientedEdge edge : path.edges()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, path, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOpenPath path) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, path, instanceIdsByTargetId);
            for (StepOrientedEdge edge : path.edges()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, path, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOrientedPath path) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, path, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, path.pathElement(), resolved, instanceIdsByTargetId);
            for (StepOrientedEdge edge : path.edges()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, path, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSubpath path) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, path, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, path.parentPath(), resolved, instanceIdsByTargetId);
            for (StepOrientedEdge edge : path.edges()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, path, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepEdgeLoop loop) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, loop, instanceIdsByTargetId);
            for (StepOrientedEdge edge : loop.edges()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, loop, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPolyLoop loop) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, loop, instanceIdsByTargetId);
            for (StepCartesianPoint point : loop.polygon()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, point, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, loop, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepConnectedEdgeSet set) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, set, instanceIdsByTargetId);
            for (StepEntity edge : set.edges()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, set, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepEdgeBasedWireframeModel model) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, model, instanceIdsByTargetId);
            for (StepConnectedEdgeSet boundary : model.boundaries()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, boundary, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, model, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepShellBasedWireframeModel model) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, model, instanceIdsByTargetId);
            for (StepEntity boundary : model.boundaries()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, boundary, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, model, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepWireShell shell) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            for (StepLoop loop : shell.loops()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, loop, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepVertexShell shell) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, shell.extent(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepVertexLoop loop) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, loop, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, loop.loopVertex(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, loop, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOrientedEdge edge) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, edge, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge.edgeElement(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepEdgeCurve edge) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, edge, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge.start(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge.end(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge.edgeGeometry(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepVertexPoint vertex) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, vertex, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, vertex.point(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, vertex, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAdvancedFace face) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, face, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face.faceGeometry(), resolved, instanceIdsByTargetId);
            for (StepFaceBound bound : face.bounds()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, bound, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepFaceSurface face) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, face, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face.faceGeometry(), resolved, instanceIdsByTargetId);
            for (StepFaceBound bound : face.bounds()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, bound, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOrientedFace face) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, face, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face.faceElement(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepConnectedFaceSet faceSet) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, faceSet, instanceIdsByTargetId);
            for (StepFaceEntity face : faceSet.faces()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, faceSet, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepConnectedFaceSubSet faceSet) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, faceSet, instanceIdsByTargetId);
            for (StepFaceEntity face : faceSet.faces()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
            }
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, faceSet.parentFaceSet(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, faceSet, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOpenShell shell) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            for (StepFaceEntity face : shell.faces()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfacedOpenShell shell) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            for (StepFaceEntity face : shell.faces()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepClosedShell shell) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            for (StepFaceEntity face : shell.faces()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOrientedOpenShell shell) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, shell.openShellElement(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOrientedClosedShell shell) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, shell.closedShellElement(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepFaceBasedSurfaceModel model) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, model, instanceIdsByTargetId);
            for (StepEntity faceSet : model.faceSets()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, faceSet, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, model, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepShellBasedSurfaceModel model) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, model, instanceIdsByTargetId);
            for (StepEntity shell : model.shells()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, model, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepManifoldSolidBrep solid) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.outer(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBrepWithVoids solid) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.outer(), resolved, instanceIdsByTargetId);
            for (StepEntity voidShell : solid.voids()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, voidShell, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSweptAreaSolid solid) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.sweptArea(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.position(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.sweepReference(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSweptDiskSolid solid) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.sweptCurve(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepComplexClippingResult solid) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.firstOperand(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.secondOperand(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSolidReplica solid) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.parentSolid(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.transformation(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepHalfSpaceSolid solid) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.baseSurface(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.enclosure(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCsgSolid solid) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.treeRootExpression(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCsgPrimitive primitive) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, primitive, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, primitive.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, primitive, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProfileDef profile) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, profile, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, profile.position(), resolved, instanceIdsByTargetId);
            for (StepEntity curve : profile.curves()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, profile, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepConicCurve curve) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBSplineCurve curve) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBSplineCurveWithKnots curve) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepRationalBSplineCurve curve) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBezierCurve curve) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepUniformCurve curve) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepQuasiUniformCurve curve) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPiecewiseBezierCurve curve) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepLine line) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, line, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, line.point(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, line.vector(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, line, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCircle circle) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, circle, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, circle.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, circle, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepEllipse ellipse) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, ellipse, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, ellipse.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, ellipse, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCurve curve) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPolyline polyline) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, polyline, instanceIdsByTargetId);
            for (StepCartesianPoint point : polyline.points()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, point, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, polyline, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTrimmedCurve curve) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.basisCurve(), resolved, instanceIdsByTargetId);
            for (StepValue trim : curve.trim1()) {
                if (trim instanceof StepValue.ReferenceValue ref) {
                    appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, resolved.get(ref.id()), resolved, instanceIdsByTargetId);
                }
            }
            for (StepValue trim : curve.trim2()) {
                if (trim instanceof StepValue.ReferenceValue ref) {
                    appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, resolved.get(ref.id()), resolved, instanceIdsByTargetId);
                }
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOffsetCurve2D curve) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.basisCurve(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOffsetCurve3D curve) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.basisCurve(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.refDirection(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPcurve curve) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.basisSurface(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.referenceToCurve(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDegeneratePcurve curve) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.basisSurface(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.referenceToCurve(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceCurve curve) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.curve3d(), resolved, instanceIdsByTargetId);
            for (StepEntity associated : curve.associatedGeometry()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, associated, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSeamCurve curve) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.curve3d(), resolved, instanceIdsByTargetId);
            for (StepEntity associated : curve.associatedGeometry()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, associated, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCompositeCurve curve) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            for (StepCompositeCurveSegment segment : curve.segments()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, segment, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCompositeCurveOnSurface curve) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            for (StepCompositeCurveSegment segment : curve.segments()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, segment, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCompositeCurveSegment segment) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, segment, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, segment.parentCurve(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, segment, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCylindricalSurface surface) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepConicalSurface surface) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSphericalSurface surface) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepToroidalSurface surface) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceOfLinearExtrusion surface) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.sweptCurve(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.extrusionAxis(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceOfRevolution surface) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.sweptCurve(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.axisPosition(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepRectangularTrimmedSurface surface) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.basisSurface(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCurveBoundedSurface surface) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.basisSurface(), resolved, instanceIdsByTargetId);
            for (StepEntity boundary : surface.boundaries()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, boundary, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOrientedSurface surface) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.surfaceElement(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOffsetSurface surface) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.basisSurface(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBSplineSurface surface) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBSplineSurfaceWithKnots surface) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepRationalBSplineSurface surface) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBezierSurface surface) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepUniformSurface surface) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepQuasiUniformSurface surface) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPiecewiseBezierSurface surface) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepFace face) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, face, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBoundedCurve curve) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBoundedSurface surface) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurface surface) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepMeasureRepresentationItem item) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item.unit(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDescriptiveRepresentationItem item) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepValueRepresentationItem item) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceModel model) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, model, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, model, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSolidModel model) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, model, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, model, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepRepresentationItem item) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepGeometricRepresentationItem item) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTopologicalRepresentationItem item) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepMeasureWithUnit measure) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, measure, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, measure.unitComponent(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, measure, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTypedMeasureWithUnit measure) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, measure, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, measure.unitComponent(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, measure, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepUncertaintyMeasureWithUnit measure) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, measure, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, measure.unitComponent(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, measure, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepConversionBasedUnit unit) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, unit, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, unit.conversionFactor(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepConversionBasedUnitWithOffset unit) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, unit, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, unit.conversionFactor(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDerivedUnit unit) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, unit, instanceIdsByTargetId);
            for (StepDerivedUnitElement element : unit.elements()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, element, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepDerivedUnitElement element) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, element, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, element.unit(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepNamedUnit unit) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, unit, instanceIdsByTargetId);
        } else if (definition instanceof StepSiUnit unit) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, unit, instanceIdsByTargetId);
        } else if (definition instanceof StepContextDependentUnit unit) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, unit, instanceIdsByTargetId);
        } else if (definition instanceof StepRepresentationContext context) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, context, instanceIdsByTargetId);
        } else if (definition instanceof StepGeometricRepresentationContext context) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, context, instanceIdsByTargetId);
            if (context.globalUnitAssignedContext() != null) {
                appendNestedDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        context.globalUnitAssignedContext(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
            if (context.globalUncertaintyAssignedContext() != null) {
                appendNestedDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        context.globalUncertaintyAssignedContext(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
        } else if (definition instanceof StepGlobalUnitAssignedContext context) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, context, instanceIdsByTargetId);
            for (StepEntity unit : context.units()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, unit, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepGlobalUncertaintyAssignedContext context) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, context, instanceIdsByTargetId);
            for (StepUncertaintyMeasureWithUnit uncertainty : context.uncertainties()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, uncertainty, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepAddress address) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, address, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, address, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCharacterizedObject characterizedObject) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, characterizedObject, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, characterizedObject, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDimensionalExponents exponents) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, exponents, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, exponents, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepVertex vertex) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, vertex, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, vertex, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepEdge edge) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, edge, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAbstractVariable variable) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, variable, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    variable.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, variable.definition(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepRowVariable variable) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, variable, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    variable.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, variable.definition(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepScalarVariable variable) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, variable, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    variable.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, variable.definition(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepForwardChainingRulePremise variable) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, variable, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    variable.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, variable.definition(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBackChainingRuleBody variable) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, variable, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    variable.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, variable.definition(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPropertyDefinitionRepresentation link) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepActionPropertyRepresentation link) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepContactRatioRepresentation link) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepKinematicPropertyDefinitionRepresentation link) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepKinematicPropertyMechanismRepresentation link) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepKinematicPropertyRepresentationRelation link) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepKinematicPropertyTopologyRepresentation link) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepResourcePropertyRepresentation link) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAttributeAssertion assertion) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assertion, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assertion.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assertion.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepShapeDefinitionRepresentation link) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepApplicationProtocolDefinition protocolDefinition) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, protocolDefinition, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, protocolDefinition.application(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProduct product) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, product, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, product, resolved, instanceIdsByTargetId);
            for (StepProductContext context : product.frameOfReference()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, context, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepProductDefinitionFormation formation) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, formation, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, formation, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, formation.ofProduct(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductDefinition productDefinition) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, productDefinition, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, productDefinition, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, productDefinition.formation(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, productDefinition.frameOfReference(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductDefinitionShape productDefinitionShape) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, productDefinitionShape, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, productDefinitionShape, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, productDefinitionShape.definition(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductContext productContext) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, productContext, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, productContext, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, productContext.frameOfReference(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductDefinitionContext productDefinitionContext) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, productDefinitionContext, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, productDefinitionContext, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, productDefinitionContext.frameOfReference(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepApplicationContext applicationContext) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, applicationContext, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, applicationContext, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepGroup group) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, group, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, group, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDocument document) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, document, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, document, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, document.kind(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDocumentReference reference) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, reference, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, reference, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, reference.assignedDocument(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, reference.assignedDocument().kind(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAppliedDocumentReference reference) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, reference, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, reference, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, reference.assignedDocument(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, reference.assignedDocument().kind(), resolved, instanceIdsByTargetId);
            for (StepEntity item : reference.items()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepExternalSource source) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, source, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, source, resolved, instanceIdsByTargetId);
            appendExternalSourceRelationshipTargets(targetsByUsageId, identifiedItem, source, resolved, instanceIdsByTargetId);
            appendExternallyDefinedItemTargets(targetsByUsageId, identifiedItem, source, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepExternallyDefinedItem item) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item.source(), resolved, instanceIdsByTargetId);
            appendExternalSourceRelationshipTargets(targetsByUsageId, identifiedItem, item.source(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDocumentUsageConstraint documentUsageConstraint) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, documentUsageConstraint, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, documentUsageConstraint, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, documentUsageConstraint.source(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, documentUsageConstraint.source().kind(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductCategory category) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, category, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, category, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductRelatedProductCategory relatedCategory) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relatedCategory, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relatedCategory, resolved, instanceIdsByTargetId);
            for (StepProduct product : relatedCategory.products()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, product, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepGeneralProperty generalProperty) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, generalProperty, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, generalProperty, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductDefinitionEffectivity effectivity) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, effectivity, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, effectivity, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, effectivity.productDefinition(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepEffectivity effectivity) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, effectivity, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, effectivity, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepRepresentationRelationship relationship) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            for (StepEntity target : collectRepresentationTargetsFromRelationship(relationship)) {
                appendPmiTarget(
                        targetsByUsageId,
                        identifiedItem.id(),
                        target,
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(relationship),
                        relationship.id()
                );
            }
        } else if (definition instanceof StepShapeRepresentationRelationship relationship) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            for (StepEntity target : collectRepresentationTargetsFromRelationship(relationship)) {
                appendPmiTarget(
                        targetsByUsageId,
                        identifiedItem.id(),
                        target,
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(relationship),
                        relationship.id()
                );
            }
        } else if (definition instanceof StepContextDependentShapeRepresentation shapeRepresentation) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shapeRepresentation, instanceIdsByTargetId);
            appendNestedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    shapeRepresentation.representationRelationship(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    shapeRepresentation.representedProductRelation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepRepresentationRelationshipWithTransformation relationship) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendNestedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    relationship.transformationOperator(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepBoxDomain boxDomain) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, boxDomain, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, boxDomain.corner(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBooleanClippingResult result) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, result, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, result.firstOperand(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, result.secondOperand(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBooleanResult result) {
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, result, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, result.firstOperand(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, result.secondOperand(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedMarker marker) {
            appendPointMarkerStyleTargets(targetsByUsageId, identifiedItem, marker.id(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, marker, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedPointMarkerSymbol marker) {
            appendPointMarkerStyleTargets(targetsByUsageId, identifiedItem, marker.id(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, marker, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAnnotationSymbol annotationSymbol) {
            appendMappedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    annotationSymbol.mappingSource(),
                    annotationSymbol.mappingTarget(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAnnotationText annotationText) {
            appendMappedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    annotationText.mappingSource(),
                    annotationText.mappingTarget(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAnnotationTextCharacter annotationTextCharacter) {
            appendMappedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    annotationTextCharacter.mappingSource(),
                    annotationTextCharacter.mappingTarget(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepUserDefinedCurveFont curveFont) {
            appendMappedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    curveFont.mappingSource(),
                    curveFont.mappingTarget(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepUserDefinedMarker marker) {
            appendMappedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    marker.mappingSource(),
                    marker.mappingTarget(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepUserDefinedTerminatorSymbol symbol) {
            appendMappedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    symbol.mappingSource(),
                    symbol.mappingTarget(),
                    instanceIdsByTargetId
            );
        }
        for (StepEntity target : collectSemanticTargets(definition, resolved, new LinkedHashSet<>())) {
            appendPmiTarget(
                    targetsByUsageId,
                    identifiedItem.id(),
                    target,
                    instanceIdsByTargetId,
                    null,
                    null,
                    null,
                    null,
                    definitionTypeName(definition),
                    definition.id()
            );
        }
    }

    private static void appendIndirectPropertyRepresentationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepPropertyDefinition propertyDefinition
                    && propertyDefinition.definition().id() == definition.id()) {
                appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, propertyDefinition, instanceIdsByTargetId);
                appendPropertyRepresentationLinkTargets(
                        targetsByUsageId,
                        identifiedItem,
                        propertyDefinition,
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }

    private static void appendProductRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepProduct product,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProductRelationship relationship) {
                if (relationship.relatingProduct().id() == product.id()) {
                    appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatedProduct(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
                }
                if (relationship.relatedProduct().id() == product.id()) {
                    appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatingProduct(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
                }
            } else if (candidate instanceof StepProductRelatedProductCategory relatedCategory
                    && relatedCategory.products().stream().anyMatch(related -> related.id() == product.id())) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relatedCategory, relationshipTypeName(relatedCategory), relatedCategory.id(), resolved, instanceIdsByTargetId);
            }
        }
    }

    private static void appendProductDefinitionFormationRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepProductDefinitionFormation formation,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepProductDefinitionFormationRelationship relationship)) {
                continue;
            }
            if (relationship.relatingFormation().id() == formation.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatedFormation(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
            if (relationship.relatedFormation().id() == formation.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatingFormation(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
        }
    }

    private static void appendProductDefinitionRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepProductDefinition productDefinition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProductDefinitionRelationship relationship) {
                if (relationship.relatingProductDefinition().id() == productDefinition.id()) {
                    appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatedProductDefinition(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
                }
                if (relationship.relatedProductDefinition().id() == productDefinition.id()) {
                    appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatingProductDefinition(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
                }
            } else if (candidate instanceof StepNextAssemblyUsageOccurrence occurrence) {
                if (occurrence.relatingProductDefinition().id() == productDefinition.id()
                        || occurrence.relatedProductDefinition().id() == productDefinition.id()) {
                    appendDefinitionRelationshipTargets(targetsByUsageId, usageId, occurrence, relationshipTypeName(occurrence), occurrence.id(), resolved, instanceIdsByTargetId);
                }
            }
        }
    }

    private static void appendProductDefinitionShapeRepresentationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepProductDefinitionShape productDefinitionShape,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepShapeDefinitionRepresentation link
                    && link.definition().id() == productDefinitionShape.id()) {
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem.id(),
                        link,
                        relationshipTypeName(link),
                        link.id(),
                        resolved,
                        instanceIdsByTargetId
                );
                appendAttachedRepresentationRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem,
                        link.usedRepresentation(),
                        resolved,
                        instanceIdsByTargetId
                );
            } else if (candidate instanceof StepContextDependentShapeRepresentation contextDependent
                    && contextDependent.representedProductRelation().id() == productDefinitionShape.id()) {
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem.id(),
                        contextDependent,
                        relationshipTypeName(contextDependent),
                        contextDependent.id(),
                        resolved,
                        instanceIdsByTargetId
                );
                if (contextDependent.representationRelationship() != null) {
                    appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                            identifiedItem.id(),
                            contextDependent.representationRelationship(),
                            relationshipTypeName(contextDependent.representationRelationship()),
                            contextDependent.representationRelationship().id(),
                            resolved,
                            instanceIdsByTargetId
                    );
                    if (contextDependent.representationRelationship() instanceof StepRepresentationRelationshipWithTransformation transformed) {
                        appendDefinitionRelationshipTargets(
                                targetsByUsageId,
                                identifiedItem.id(),
                                transformed.transformationOperator(),
                                definitionTypeName(transformed.transformationOperator()),
                                transformed.transformationOperator().id(),
                                resolved,
                                instanceIdsByTargetId
                        );
                        appendNestedDefinitionTargets(
                                targetsByUsageId,
                                identifiedItem,
                                transformed.transformationOperator().transformItem1(),
                                resolved,
                                instanceIdsByTargetId
                        );
                        appendNestedDefinitionTargets(
                                targetsByUsageId,
                                identifiedItem,
                                transformed.transformationOperator().transformItem2(),
                                resolved,
                                instanceIdsByTargetId
                        );
                    }
                }
            }
        }
    }

    private static void appendAttachedRepresentationRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepRepresentationRelationship relationship
                    && referencesRepresentation(relationship.rep1(), relationship.rep2(), representation.id())) {
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem.id(),
                        relationship,
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            } else if (candidate instanceof StepRepresentationRelationshipWithTransformation transformed
                    && referencesRepresentation(transformed.rep1(), transformed.rep2(), representation.id())) {
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem.id(),
                        transformed,
                        relationshipTypeName(transformed),
                        transformed.id(),
                        resolved,
                        instanceIdsByTargetId
                );
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem.id(),
                        transformed.transformationOperator(),
                        definitionTypeName(transformed.transformationOperator()),
                        transformed.transformationOperator().id(),
                        resolved,
                        instanceIdsByTargetId
                );
                appendNestedDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        transformed.transformationOperator().transformItem1(),
                        resolved,
                        instanceIdsByTargetId
                );
                appendNestedDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        transformed.transformationOperator().transformItem2(),
                        resolved,
                        instanceIdsByTargetId
                );
            } else if (candidate instanceof StepShapeRepresentationRelationship relationship
                    && referencesRepresentation(relationship.rep1(), relationship.rep2(), representation.id())) {
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem.id(),
                        relationship,
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }

    private static boolean referencesRepresentation(
            StepRepresentation rep1,
            StepRepresentation rep2,
            int representationId
    ) {
        return rep1.id() == representationId || rep2.id() == representationId;
    }

    private static void appendProductDefinitionRepresentationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepProductDefinition productDefinition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProductDefinitionShape shape
                    && shape.definition().id() == productDefinition.id()) {
                appendProductDefinitionShapeRepresentationTargets(
                        targetsByUsageId,
                        identifiedItem,
                        shape,
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }

    private static void appendOccurrenceRepresentationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepNextAssemblyUsageOccurrence occurrence,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProductDefinitionShape shape
                    && shape.definition().id() == occurrence.id()) {
                appendProductDefinitionShapeRepresentationTargets(
                        targetsByUsageId,
                        identifiedItem,
                        shape,
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }

    private static void appendPropertyDefinitionRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepPropertyDefinition propertyDefinition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepPropertyDefinitionRelationship relationship)) {
                continue;
            }
            if (relationship.relatingPropertyDefinition().id() == propertyDefinition.id()) {
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        usageId,
                        relationship.relatedPropertyDefinition(),
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
            if (relationship.relatedPropertyDefinition().id() == propertyDefinition.id()) {
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        usageId,
                        relationship.relatingPropertyDefinition(),
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }

    private static void appendPropertyRepresentationLinkTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepPropertyDefinition propertyDefinition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendPropertyRepresentationLinkTargets(
                targetsByUsageId,
                identifiedItem.id(),
                propertyDefinition,
                resolved,
                instanceIdsByTargetId
        );
        for (StepEntity candidate : resolved.values()) {
            StepRepresentation usedRepresentation = propertyRepresentationLinkRepresentation(candidate, propertyDefinition.id());
            if (usedRepresentation != null) {
                appendAttachedRepresentationRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem,
                        usedRepresentation,
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }

    private static void appendPropertyRepresentationLinkTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepPropertyDefinition propertyDefinition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepPropertyDefinitionRepresentation representationLink
                    && representationLink.definition().id() == propertyDefinition.id()) {
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        representationLink.usedRepresentation(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(representationLink),
                        representationLink.id()
                );
            } else if (candidate instanceof StepAttributeAssertion representationLink
                    && representationLink.definition().id() == propertyDefinition.id()) {
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        representationLink.usedRepresentation(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(representationLink),
                        representationLink.id()
                );
            } else if (candidate instanceof StepActionPropertyRepresentation representationLink
                    && representationLink.definition().id() == propertyDefinition.id()) {
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        representationLink.usedRepresentation(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(representationLink),
                        representationLink.id()
                );
            } else if (candidate instanceof StepContactRatioRepresentation representationLink
                    && representationLink.definition().id() == propertyDefinition.id()) {
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        representationLink.usedRepresentation(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(representationLink),
                        representationLink.id()
                );
            } else if (candidate instanceof StepKinematicPropertyDefinitionRepresentation representationLink
                    && representationLink.definition().id() == propertyDefinition.id()) {
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        representationLink.usedRepresentation(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(representationLink),
                        representationLink.id()
                );
            } else if (candidate instanceof StepKinematicPropertyMechanismRepresentation representationLink
                    && representationLink.definition().id() == propertyDefinition.id()) {
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        representationLink.usedRepresentation(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(representationLink),
                        representationLink.id()
                );
            } else if (candidate instanceof StepKinematicPropertyRepresentationRelation representationLink
                    && representationLink.definition().id() == propertyDefinition.id()) {
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        representationLink.usedRepresentation(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(representationLink),
                        representationLink.id()
                );
            } else if (candidate instanceof StepKinematicPropertyTopologyRepresentation representationLink
                    && representationLink.definition().id() == propertyDefinition.id()) {
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        representationLink.usedRepresentation(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(representationLink),
                        representationLink.id()
                );
            } else if (candidate instanceof StepResourcePropertyRepresentation representationLink
                    && representationLink.definition().id() == propertyDefinition.id()) {
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        representationLink.usedRepresentation(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(representationLink),
                        representationLink.id()
                );
            } else if (candidate instanceof StepForwardChainingRulePremise representationLink
                    && representationLink.definition().id() == propertyDefinition.id()) {
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        representationLink.usedRepresentation(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(representationLink),
                        representationLink.id()
                );
            } else if (candidate instanceof StepBackChainingRuleBody representationLink
                    && representationLink.definition().id() == propertyDefinition.id()) {
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        representationLink.usedRepresentation(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(representationLink),
                        representationLink.id()
                );
            } else if (candidate instanceof StepPlacedDatumTargetFeature representationLink
                    && representationLink.definition().id() == propertyDefinition.id()) {
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        representationLink.usedRepresentation(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(representationLink),
                        representationLink.id()
                );
            }
        }
    }

    private static StepRepresentation propertyRepresentationLinkRepresentation(StepEntity candidate, int propertyDefinitionId) {
        if (candidate instanceof StepPropertyDefinitionRepresentation representationLink
                && representationLink.definition().id() == propertyDefinitionId) {
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepAttributeAssertion representationLink
                && representationLink.definition().id() == propertyDefinitionId) {
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepActionPropertyRepresentation representationLink
                && representationLink.definition().id() == propertyDefinitionId) {
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepContactRatioRepresentation representationLink
                && representationLink.definition().id() == propertyDefinitionId) {
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepKinematicPropertyDefinitionRepresentation representationLink
                && representationLink.definition().id() == propertyDefinitionId) {
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepKinematicPropertyMechanismRepresentation representationLink
                && representationLink.definition().id() == propertyDefinitionId) {
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepKinematicPropertyRepresentationRelation representationLink
                && representationLink.definition().id() == propertyDefinitionId) {
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepKinematicPropertyTopologyRepresentation representationLink
                && representationLink.definition().id() == propertyDefinitionId) {
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepResourcePropertyRepresentation representationLink
                && representationLink.definition().id() == propertyDefinitionId) {
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepForwardChainingRulePremise representationLink
                && representationLink.definition().id() == propertyDefinitionId) {
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepBackChainingRuleBody representationLink
                && representationLink.definition().id() == propertyDefinitionId) {
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepPlacedDatumTargetFeature representationLink
                && representationLink.definition().id() == propertyDefinitionId) {
            return representationLink.usedRepresentation();
        }
        return null;
    }

    private static void appendGroupRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepGroup group,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepGroupRelationship relationship)) {
                continue;
            }
            if (relationship.relatingGroup().id() == group.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatedGroup(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
            if (relationship.relatedGroup().id() == group.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatingGroup(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
        }
    }

    private static void appendGeneralPropertyRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepGeneralProperty generalProperty,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepGeneralPropertyRelationship relationship)) {
                continue;
            }
            if (relationship.relatingGeneralProperty().id() == generalProperty.id()) {
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        usageId,
                        relationship.relatedGeneralProperty(),
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
            if (relationship.relatedGeneralProperty().id() == generalProperty.id()) {
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        usageId,
                        relationship.relatingGeneralProperty(),
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }

    private static void appendDocumentRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepDocument document,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepDocumentRelationship relationship)) {
                continue;
            }
            if (relationship.relatingDocument().id() == document.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatedDocument(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
            if (relationship.relatedDocument().id() == document.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatingDocument(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
        }
    }

    private static void appendApprovalDecorationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepApproval approval,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepApprovalPersonOrganization personOrganization
                    && personOrganization.authorizedApproval().id() == approval.id()) {
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        personOrganization,
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        personOrganization.personOrganization(),
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        personOrganization.personOrganization().person(),
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        personOrganization.personOrganization().organization(),
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        personOrganization.role(),
                        instanceIdsByTargetId
                );
            } else if (candidate instanceof StepApprovalDateTime approvalDateTime
                    && approvalDateTime.datedApproval().id() == approval.id()) {
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        approvalDateTime,
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        approvalDateTime.dateTime(),
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        approvalDateTime.dateTime().dateComponent(),
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        approvalDateTime.dateTime().timeComponent(),
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        approvalDateTime.dateTime().timeComponent().zone(),
                        instanceIdsByTargetId
                );
            }
        }
    }

    private static void appendPointMarkerStyleTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            int markerId,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepPointStyle pointStyle && pointStyle.marker().id() == markerId) {
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        pointStyle,
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        pointStyle.colour(),
                        instanceIdsByTargetId
                );
            }
        }
    }

    private static void appendOrganizationRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepOrganization organization,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepOrganizationRelationship relationship)) {
                continue;
            }
            if (relationship.relatingOrganization().id() == organization.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatedOrganization(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
            if (relationship.relatedOrganization().id() == organization.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatingOrganization(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
        }
    }

    private static void appendEffectivityRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepEffectivity effectivity,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepEffectivityRelationship relationship)) {
                continue;
            }
            if (relationship.relatingEffectivity().id() == effectivity.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatedEffectivity(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
            if (relationship.relatedEffectivity().id() == effectivity.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatingEffectivity(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
        }
    }

    private static void appendProductCategoryRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepProductCategory category,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepProductCategoryRelationship relationship)) {
                continue;
            }
            if (relationship.category().id() == category.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.subCategory(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
            if (relationship.subCategory().id() == category.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.category(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
        }
    }

    private static void appendProductDefinitionEffectivityTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepEffectivity effectivity,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        Set<String> linkedEffectivityNames = new LinkedHashSet<>();
        linkedEffectivityNames.add(effectivity.name());
        boolean changed;
        do {
            changed = false;
            for (StepEntity candidate : resolved.values()) {
                if (!(candidate instanceof StepEffectivityRelationship relationship)) {
                    continue;
                }
                String relatingName = relationship.relatingEffectivity().name();
                String relatedName = relationship.relatedEffectivity().name();
                if (linkedEffectivityNames.contains(relatingName) && linkedEffectivityNames.add(relatedName)) {
                    changed = true;
                }
                if (linkedEffectivityNames.contains(relatedName) && linkedEffectivityNames.add(relatingName)) {
                    changed = true;
                }
            }
        } while (changed);
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProductDefinitionEffectivity productDefinitionEffectivity
                    && linkedEffectivityNames.contains(productDefinitionEffectivity.effectivityId())) {
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        usageId,
                        productDefinitionEffectivity,
                        definitionTypeName(productDefinitionEffectivity),
                        productDefinitionEffectivity.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }

    private static void appendExternalSourceRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepExternalSource source,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepExternalSourceRelationship relationship)) {
                continue;
            }
            if (relationship.relatingSource().id() == source.id()) {
                appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            }
            if (relationship.relatedSource().id() == source.id()) {
                appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            }
        }
    }

    private static void appendExternallyDefinedItemTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepExternalSource source,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        Set<Integer> linkedSourceIds = new LinkedHashSet<>();
        linkedSourceIds.add(source.id());
        boolean changed;
        do {
            changed = false;
            for (StepEntity candidate : resolved.values()) {
                if (!(candidate instanceof StepExternalSourceRelationship relationship)) {
                    continue;
                }
                int relatingId = relationship.relatingSource().id();
                int relatedId = relationship.relatedSource().id();
                if (linkedSourceIds.contains(relatingId) && linkedSourceIds.add(relatedId)) {
                    changed = true;
                }
                if (linkedSourceIds.contains(relatedId) && linkedSourceIds.add(relatingId)) {
                    changed = true;
                }
            }
        } while (changed);
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepExternallyDefinedItem item
                    && linkedSourceIds.contains(item.source().id())) {
                appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            }
        }
    }

    private static void appendMappedDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity mappingSource,
            StepEntity mappingTarget,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendExistingRepresentationDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                mappingSource,
                instanceIdsByTargetId
        );
        if (mappingSource instanceof StepRepresentationMap representationMap) {
            appendRepresentationMapDefinitionTargets(targetsByUsageId, identifiedItem, representationMap, instanceIdsByTargetId);
        } else if (mappingSource instanceof StepSymbolRepresentationMap representationMap) {
            appendRepresentationMapDefinitionTargets(targetsByUsageId, identifiedItem, representationMap, instanceIdsByTargetId);
        }
        appendPlacementDefinitionTargets(targetsByUsageId, identifiedItem, mappingTarget, instanceIdsByTargetId);
    }

    private static void appendCarrierDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity carrier,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendExistingRepresentationDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                carrier,
                instanceIdsByTargetId
        );
    }

    private static void appendNestedDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity nestedDefinition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (nestedDefinition == null) {
            return;
        }
        appendExistingRepresentationDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                nestedDefinition,
                instanceIdsByTargetId
        );
        appendSemanticDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                nestedDefinition,
                resolved,
                instanceIdsByTargetId
        );
    }

    private static void appendSplineCurveControlPointTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            List<StepCartesianPoint> controlPoints,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepCartesianPoint controlPoint : controlPoints) {
            appendNestedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    controlPoint,
                    resolved,
                    instanceIdsByTargetId
            );
        }
    }

    private static void appendSplineSurfaceControlPointTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            List<List<StepCartesianPoint>> controlPoints,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (List<StepCartesianPoint> row : controlPoints) {
            appendSplineCurveControlPointTargets(
                    targetsByUsageId,
                    identifiedItem,
                    row,
                    resolved,
                    instanceIdsByTargetId
            );
        }
    }

    private static void appendRepresentationMapDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepRepresentationMap representationMap,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendPlacementDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                representationMap.mappedOrigin(),
                instanceIdsByTargetId
        );
        appendExistingRepresentationDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                representationMap.mappedRepresentation(),
                instanceIdsByTargetId
        );
    }

    private static void appendRepresentationMapDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepSymbolRepresentationMap representationMap,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendPlacementDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                representationMap.mappedOrigin(),
                instanceIdsByTargetId
        );
        appendExistingRepresentationDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                representationMap.mappedRepresentation(),
                instanceIdsByTargetId
        );
    }

    private static void appendPlacementDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity placement,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendExistingRepresentationDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                placement,
                instanceIdsByTargetId
        );
        if (placement instanceof StepAxis1Placement axis1Placement) {
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    axis1Placement.location(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    axis1Placement.axis(),
                    instanceIdsByTargetId
            );
        } else if (placement instanceof StepAxis2Placement2D axis2Placement2D) {
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    axis2Placement2D.location(),
                    instanceIdsByTargetId
            );
            if (axis2Placement2D.refDirection() != null) {
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        axis2Placement2D.refDirection(),
                        instanceIdsByTargetId
                );
            }
        } else if (placement instanceof StepAxis2Placement3D axis2Placement3D) {
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    axis2Placement3D.location(),
                    instanceIdsByTargetId
            );
            if (axis2Placement3D.axis() != null) {
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        axis2Placement3D.axis(),
                        instanceIdsByTargetId
                );
            }
            if (axis2Placement3D.refDirection() != null) {
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        axis2Placement3D.refDirection(),
                        instanceIdsByTargetId
                );
            }
        }
    }

    private static void appendShapeAspectRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepShapeAspect shapeAspect,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepShapeAspectRelationship relationship)) {
                continue;
            }
            if (relationship.relatingShapeAspect().id() == shapeAspect.id()) {
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        usageId,
                        relationship.relatedShapeAspect(),
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
            if (relationship.relatedShapeAspect().id() == shapeAspect.id()) {
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        usageId,
                        relationship.relatingShapeAspect(),
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }

    private static void appendDefinitionRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepEntity relatedDefinition,
            String viaDefinitionType,
            int viaDefinitionId,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (relatedDefinition instanceof StepPropertyDefinition propertyDefinition) {
            appendPropertyRepresentationLinkTargets(
                    targetsByUsageId,
                    usageId,
                    propertyDefinition,
                    resolved,
                    instanceIdsByTargetId
            );
        }
        for (StepEntity target : collectSemanticTargets(relatedDefinition, resolved, new LinkedHashSet<>())) {
            appendPmiTarget(
                    targetsByUsageId,
                    usageId,
                    target,
                    instanceIdsByTargetId,
                    null,
                    null,
                    null,
                    null,
                    viaDefinitionType,
                    viaDefinitionId
            );
        }
    }

    private static String camelToStepLike(String value) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char current = value.charAt(i);
            if (i > 0 && Character.isUpperCase(current)
                    && (Character.isLowerCase(value.charAt(i - 1))
                    || (i + 1 < value.length() && Character.isLowerCase(value.charAt(i + 1))))) {
                builder.append('_');
            } else if (i > 0 && Character.isDigit(current) && Character.isLetter(value.charAt(i - 1))) {
                builder.append('_');
            }
            builder.append(Character.toUpperCase(current));
        }
        return builder.toString();
    }

    private static String definitionTypeName(StepEntity definition) {
        String entityName = tryEntityName(definition);
        if (entityName != null) {
            return entityName;
        }
        if (definition instanceof StepAxis1Placement) {
            return "AXIS1_PLACEMENT";
        }
        if (definition instanceof StepAxis2Placement2D) {
            return "AXIS2_PLACEMENT_2D";
        }
        if (definition instanceof StepAxis2Placement3D) {
            return "AXIS2_PLACEMENT_3D";
        }
        return definition.getClass().getSimpleName().startsWith("Step")
                ? camelToStepLike(definition.getClass().getSimpleName().substring(4))
                : definition.getClass().getSimpleName();
    }

    private static String tryEntityName(StepEntity definition) {
        try {
            Object value = definition.getClass().getMethod("entityName").invoke(definition);
            if (value instanceof String name && !name.isBlank()) {
                return name;
            }
        } catch (ReflectiveOperationException ignored) {
            // Not every semantic record exposes entityName; fall back to explicit naming below.
        }
        return null;
    }

    private static String relationshipTypeName(StepEntity relationship) {
        return definitionTypeName(relationship);
    }

    private static void appendRelationshipSemanticTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            String relationshipType,
            int relationshipId,
            StepEntity source,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity target : collectSemanticTargets(source, resolved, new LinkedHashSet<>())) {
            appendPmiTarget(
                    targetsByUsageId,
                    usageId,
                    target,
                    instanceIdsByTargetId,
                    relationshipType,
                    relationshipId
            );
        }
    }

    private static void propagateCalloutTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepDraughtingCalloutRelationship relationship
    ) {
        List<PmiTargetPayload> relatingTargets = targetsByUsageId.get(relationship.relatingCallout().id());
        if (relatingTargets == null || relatingTargets.isEmpty()) {
            return;
        }
        List<PmiTargetPayload> relatedTargets = targetsByUsageId.computeIfAbsent(
                relationship.relatedCallout().id(),
                ignored -> new ArrayList<>()
        );
        for (PmiTargetPayload target : relatingTargets) {
            PmiTargetPayload propagated = new PmiTargetPayload(
                    target.id(),
                    target.type(),
                    target.name(),
                    target.instanceIds(),
                    "DRAUGHTING_CALLOUT_RELATIONSHIP",
                    relationship.id(),
                    target.viaUsageType(),
                    target.viaUsageId(),
                    target.viaDefinitionType(),
                    target.viaDefinitionId()
            );
            if (!relatedTargets.contains(propagated)) {
                relatedTargets.add(propagated);
            }
        }
    }

    private static Set<StepEntity> collectSemanticTargets(
            StepEntity entity,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        if (entity == null || !visiting.add(entity.id())) {
            return Set.of();
        }
        Set<StepEntity> targets = new LinkedHashSet<>();
        if (entity instanceof StepFaceEntity
                || entity instanceof StepEdgeCurve
                || entity instanceof StepPath
                || entity instanceof StepOpenPath
                || entity instanceof StepSubpath
                || entity instanceof StepOrientedPath
                || entity instanceof StepConnectedEdgeSet
                || entity instanceof StepPointSet
                || entity instanceof StepGeometricSet
                || entity instanceof StepGeometricCurveSet
                || entity instanceof StepOpenShell
                || entity instanceof StepSurfacedOpenShell
                || entity instanceof StepOrientedOpenShell
                || entity instanceof StepClosedShell
                || entity instanceof StepOrientedClosedShell
                || entity instanceof StepWireShell
                || entity instanceof StepVertexShell
                || entity instanceof StepEdgeLoop
                || entity instanceof StepPolyLoop
                || entity instanceof StepConnectedFaceSet
                || entity instanceof StepConnectedFaceSubSet
                || entity instanceof StepFaceBasedSurfaceModel
                || entity instanceof StepShellBasedSurfaceModel
                || entity instanceof StepEdgeBasedWireframeModel
                || entity instanceof StepShellBasedWireframeModel
                || entity instanceof StepManifoldSolidBrep
                || entity instanceof StepBrepWithVoids
                || entity instanceof StepSweptAreaSolid
                || entity instanceof StepSolidReplica
                || entity instanceof StepCsgSolid
                || entity instanceof StepCsgPrimitive
                || entity instanceof StepBooleanResult
                || entity instanceof StepBooleanClippingResult
                || entity instanceof StepRepresentation) {
            targets.add(entity);
        }
        if (entity instanceof StepPropertyDefinition propertyDefinition) {
            targets.addAll(collectSemanticTargets(propertyDefinition.definition(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepPropertyDefinitionRepresentation representationLink
                        && representationLink.definition().id() == propertyDefinition.id()) {
                    targets.add(representationLink.usedRepresentation());
                } else if (candidate instanceof StepActionPropertyRepresentation representationLink
                        && representationLink.definition().id() == propertyDefinition.id()) {
                    targets.add(representationLink.usedRepresentation());
                } else if (candidate instanceof StepContactRatioRepresentation representationLink
                        && representationLink.definition().id() == propertyDefinition.id()) {
                    targets.add(representationLink.usedRepresentation());
                } else if (candidate instanceof StepKinematicPropertyDefinitionRepresentation representationLink
                        && representationLink.definition().id() == propertyDefinition.id()) {
                    targets.add(representationLink.usedRepresentation());
                } else if (candidate instanceof StepKinematicPropertyMechanismRepresentation representationLink
                        && representationLink.definition().id() == propertyDefinition.id()) {
                    targets.add(representationLink.usedRepresentation());
                } else if (candidate instanceof StepKinematicPropertyRepresentationRelation representationLink
                        && representationLink.definition().id() == propertyDefinition.id()) {
                    targets.add(representationLink.usedRepresentation());
                } else if (candidate instanceof StepKinematicPropertyTopologyRepresentation representationLink
                        && representationLink.definition().id() == propertyDefinition.id()) {
                    targets.add(representationLink.usedRepresentation());
                } else if (candidate instanceof StepResourcePropertyRepresentation representationLink
                        && representationLink.definition().id() == propertyDefinition.id()) {
                    targets.add(representationLink.usedRepresentation());
                } else if (candidate instanceof StepForwardChainingRulePremise representationLink
                        && representationLink.definition().id() == propertyDefinition.id()) {
                    targets.add(representationLink.usedRepresentation());
                } else if (candidate instanceof StepBackChainingRuleBody representationLink
                        && representationLink.definition().id() == propertyDefinition.id()) {
                    targets.add(representationLink.usedRepresentation());
                } else if (candidate instanceof StepPlacedDatumTargetFeature datumTargetFeature
                        && datumTargetFeature.definition().id() == propertyDefinition.id()) {
                    targets.add(datumTargetFeature.usedRepresentation());
                } else if (candidate instanceof StepPropertyDefinitionRelationship relationship) {
                    if (relationship.relatingPropertyDefinition().id() == propertyDefinition.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedPropertyDefinition(), resolved, visiting));
                    }
                    if (relationship.relatedPropertyDefinition().id() == propertyDefinition.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingPropertyDefinition(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepDescriptiveRepresentationItem item) {
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting));
        } else if (entity instanceof StepValueRepresentationItem item) {
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting));
        } else if (entity instanceof StepMeasureRepresentationItem item) {
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting));
            targets.addAll(collectSemanticTargets(item.unit(), resolved, visiting));
        } else if (entity instanceof StepMeasureWithUnit measure) {
            targets.addAll(collectSemanticTargets(measure.unitComponent(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(measure.id(), resolved, visiting));
        } else if (entity instanceof StepTypedMeasureWithUnit measure) {
            targets.addAll(collectSemanticTargets(measure.unitComponent(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(measure.id(), resolved, visiting));
        } else if (entity instanceof StepUncertaintyMeasureWithUnit measure) {
            targets.addAll(collectSemanticTargets(measure.unitComponent(), resolved, visiting));
            targets.addAll(collectTargetsForAssignedUncertainty(measure.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(measure.id(), resolved, visiting));
        } else if (entity instanceof StepCartesianPoint point) {
            targets.addAll(collectTargetsReferencingEntity(point.id(), resolved, visiting));
        } else if (entity instanceof StepDirection direction) {
            targets.addAll(collectTargetsReferencingEntity(direction.id(), resolved, visiting));
        } else if (entity instanceof StepVector vector) {
            targets.addAll(collectSemanticTargets(vector.orientation(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(vector.id(), resolved, visiting));
        } else if (entity instanceof StepAxis1Placement placement) {
            targets.addAll(collectSemanticTargets(placement.location(), resolved, visiting));
            targets.addAll(collectSemanticTargets(placement.axis(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(placement.id(), resolved, visiting));
        } else if (entity instanceof StepAxis2Placement2D placement) {
            targets.addAll(collectSemanticTargets(placement.location(), resolved, visiting));
            if (placement.refDirection() != null) {
                targets.addAll(collectSemanticTargets(placement.refDirection(), resolved, visiting));
            }
            targets.addAll(collectTargetsReferencingEntity(placement.id(), resolved, visiting));
        } else if (entity instanceof StepAxis2Placement3D placement) {
            targets.addAll(collectSemanticTargets(placement.location(), resolved, visiting));
            if (placement.axis() != null) {
                targets.addAll(collectSemanticTargets(placement.axis(), resolved, visiting));
            }
            if (placement.refDirection() != null) {
                targets.addAll(collectSemanticTargets(placement.refDirection(), resolved, visiting));
            }
            targets.addAll(collectTargetsReferencingEntity(placement.id(), resolved, visiting));
        } else if (entity instanceof StepAddress address) {
            targets.addAll(collectTargetsReferencingEntity(address.id(), resolved, visiting));
        } else if (entity instanceof StepCharacterizedObject characterizedObject) {
            targets.addAll(collectTargetsReferencingEntity(characterizedObject.id(), resolved, visiting));
        } else if (entity instanceof StepPoint point) {
            targets.addAll(collectTargetsReferencingEntity(point.id(), resolved, visiting));
        } else if (entity instanceof StepPointSet pointSet) {
            targets.addAll(collectSemanticTargets(pointSet.points(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(pointSet.id(), resolved, visiting));
        } else if (entity instanceof StepPolyline polyline) {
            targets.addAll(collectSemanticTargets(polyline.points(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(polyline.id(), resolved, visiting));
        } else if (entity instanceof StepProfileDef profile) {
            if (profile.position() != null) {
                targets.addAll(collectSemanticTargets(profile.position(), resolved, visiting));
            }
            targets.addAll(collectSemanticTargets(profile.curves(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(profile.id(), resolved, visiting));
        } else if (entity instanceof StepLine line) {
            targets.addAll(collectSemanticTargets(line.point(), resolved, visiting));
            targets.addAll(collectSemanticTargets(line.vector(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(line.id(), resolved, visiting));
        } else if (entity instanceof StepCircle circle) {
            targets.addAll(collectSemanticTargets(circle.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(circle.id(), resolved, visiting));
        } else if (entity instanceof StepEllipse ellipse) {
            targets.addAll(collectSemanticTargets(ellipse.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(ellipse.id(), resolved, visiting));
        } else if (entity instanceof StepCurve curve) {
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepBoundedCurve curve) {
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepConicCurve curve) {
            targets.addAll(collectSemanticTargets(curve.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepBSplineCurve curve) {
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepBezierCurve curve) {
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepBSplineCurveWithKnots curve) {
            targets.addAll(collectSemanticTargets(curve.controlPoints(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepRationalBSplineCurve curve) {
            targets.addAll(collectSemanticTargets(curve.controlPoints(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepPiecewiseBezierCurve curve) {
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepUniformCurve curve) {
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepQuasiUniformCurve curve) {
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepOffsetCurve2D curve) {
            targets.addAll(collectSemanticTargets(curve.basisCurve(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepOffsetCurve3D curve) {
            targets.addAll(collectSemanticTargets(curve.basisCurve(), resolved, visiting));
            targets.addAll(collectSemanticTargets(curve.refDirection(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepOrientedCurve curve) {
            targets.addAll(collectSemanticTargets(curve.curveElement(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepTrimmedCurve curve) {
            targets.addAll(collectSemanticTargets(curve.basisCurve(), resolved, visiting));
            for (StepValue trim : curve.trim1()) {
                if (trim instanceof StepValue.ReferenceValue ref && resolved.containsKey(ref.id())) {
                    targets.addAll(collectSemanticTargets(resolved.get(ref.id()), resolved, visiting));
                }
            }
            for (StepValue trim : curve.trim2()) {
                if (trim instanceof StepValue.ReferenceValue ref && resolved.containsKey(ref.id())) {
                    targets.addAll(collectSemanticTargets(resolved.get(ref.id()), resolved, visiting));
                }
            }
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceCurve curve) {
            targets.addAll(collectSemanticTargets(curve.curve3d(), resolved, visiting));
            targets.addAll(collectSemanticTargets(curve.associatedGeometry(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepSeamCurve curve) {
            targets.addAll(collectSemanticTargets(curve.curve3d(), resolved, visiting));
            targets.addAll(collectSemanticTargets(curve.associatedGeometry(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepPcurve curve) {
            targets.addAll(collectSemanticTargets(curve.basisSurface(), resolved, visiting));
            targets.addAll(collectSemanticTargets(curve.referenceToCurve(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepCompositeCurve curve) {
            targets.addAll(collectSemanticTargets(curve.segments(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepCompositeCurveOnSurface curve) {
            targets.addAll(collectSemanticTargets(curve.segments(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepCompositeCurveSegment segment) {
            targets.addAll(collectSemanticTargets(segment.parentCurve(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(segment.id(), resolved, visiting));
        } else if (entity instanceof StepSurface surface) {
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepBoundedSurface surface) {
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepBSplineSurface surface) {
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepBezierSurface surface) {
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepBSplineSurfaceWithKnots surface) {
            targets.addAll(collectSemanticTargets(surface.controlPoints().stream().flatMap(List::stream).toList(),
                    resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepRationalBSplineSurface surface) {
            targets.addAll(collectSemanticTargets(surface.controlPoints().stream().flatMap(List::stream).toList(),
                    resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepPiecewiseBezierSurface surface) {
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepUniformSurface surface) {
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepQuasiUniformSurface surface) {
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepPlane plane) {
            targets.addAll(collectSemanticTargets(plane.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(plane.id(), resolved, visiting));
        } else if (entity instanceof StepCylindricalSurface surface) {
            targets.addAll(collectSemanticTargets(surface.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepConicalSurface surface) {
            targets.addAll(collectSemanticTargets(surface.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepToroidalSurface surface) {
            targets.addAll(collectSemanticTargets(surface.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceOfLinearExtrusion surface) {
            targets.addAll(collectSemanticTargets(surface.sweptCurve(), resolved, visiting));
            targets.addAll(collectSemanticTargets(surface.extrusionAxis(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceOfRevolution surface) {
            targets.addAll(collectSemanticTargets(surface.sweptCurve(), resolved, visiting));
            targets.addAll(collectSemanticTargets(surface.axisPosition(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepRectangularTrimmedSurface surface) {
            targets.addAll(collectSemanticTargets(surface.basisSurface(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepCurveBoundedSurface surface) {
            targets.addAll(collectSemanticTargets(surface.basisSurface(), resolved, visiting));
            targets.addAll(collectSemanticTargets(surface.boundaries(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepOrientedSurface surface) {
            targets.addAll(collectSemanticTargets(surface.surfaceElement(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepOffsetSurface surface) {
            targets.addAll(collectSemanticTargets(surface.basisSurface(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepSphericalSurface surface) {
            targets.addAll(collectSemanticTargets(surface.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepDegenerateToroidalSurface surface) {
            targets.addAll(collectSemanticTargets(surface.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepShellBasedSurfaceModel model) {
            targets.addAll(collectSemanticTargets(model.shells(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(model.id(), resolved, visiting));
        } else if (entity instanceof StepFaceBasedSurfaceModel model) {
            targets.addAll(collectSemanticTargets(model.faceSets(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(model.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceModel model) {
            targets.addAll(collectTargetsReferencingEntity(model.id(), resolved, visiting));
        } else if (entity instanceof StepSolidModel model) {
            targets.addAll(collectTargetsReferencingEntity(model.id(), resolved, visiting));
        } else if (entity instanceof StepGeometricCurveSet set) {
            targets.addAll(collectSemanticTargets(set.elements(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(set.id(), resolved, visiting));
        } else if (entity instanceof StepGeometricSet set) {
            targets.addAll(collectSemanticTargets(set.elements(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(set.id(), resolved, visiting));
        } else if (entity instanceof StepBoxDomain boxDomain) {
            targets.addAll(collectSemanticTargets(boxDomain.corner(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(boxDomain.id(), resolved, visiting));
        } else if (entity instanceof StepDimensionalExponents exponents) {
            targets.addAll(collectTargetsReferencingEntity(exponents.id(), resolved, visiting));
        } else if (entity instanceof StepDegeneratePcurve curve) {
            targets.addAll(collectSemanticTargets(curve.basisSurface(), resolved, visiting));
            targets.addAll(collectSemanticTargets(curve.referenceToCurve(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepHalfSpaceSolid halfSpaceSolid) {
            targets.addAll(collectSemanticTargets(halfSpaceSolid.baseSurface(), resolved, visiting));
            if (halfSpaceSolid.enclosure() != null) {
                targets.addAll(collectSemanticTargets(halfSpaceSolid.enclosure(), resolved, visiting));
            }
            targets.addAll(collectTargetsReferencingEntity(halfSpaceSolid.id(), resolved, visiting));
        } else if (entity instanceof StepVertex vertex) {
            targets.addAll(collectTargetsReferencingEntity(vertex.id(), resolved, visiting));
        } else if (entity instanceof StepVertexPoint vertexPoint) {
            targets.addAll(collectSemanticTargets(vertexPoint.point(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(vertexPoint.id(), resolved, visiting));
        } else if (entity instanceof StepEdge edge) {
            targets.addAll(collectTargetsReferencingEntity(edge.id(), resolved, visiting));
        } else if (entity instanceof StepConnectedEdgeSet edgeSet) {
            targets.addAll(collectSemanticTargets(edgeSet.edges(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(edgeSet.id(), resolved, visiting));
        } else if (entity instanceof StepEdgeBasedWireframeModel model) {
            targets.addAll(collectSemanticTargets(model.boundaries(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(model.id(), resolved, visiting));
        } else if (entity instanceof StepPolyLoop loop) {
            targets.addAll(collectSemanticTargets(loop.polygon(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(loop.id(), resolved, visiting));
        } else if (entity instanceof StepLoop loop) {
            targets.addAll(collectTargetsReferencingEntity(loop.id(), resolved, visiting));
        } else if (entity instanceof StepEdgeLoop loop) {
            targets.addAll(collectSemanticTargets(loop.edges(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(loop.id(), resolved, visiting));
        } else if (entity instanceof StepVertexLoop loop) {
            targets.addAll(collectSemanticTargets(loop.loopVertex(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(loop.id(), resolved, visiting));
        } else if (entity instanceof com.minicad.step.model.StepFaceBound faceBound) {
            targets.addAll(collectSemanticTargets(faceBound.loop(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(faceBound.id(), resolved, visiting));
        } else if (entity instanceof StepFace face) {
            targets.addAll(collectTargetsReferencingEntity(face.id(), resolved, visiting));
        } else if (entity instanceof StepAdvancedFace face) {
            targets.addAll(collectSemanticTargets(face.bounds(), resolved, visiting));
            targets.addAll(collectSemanticTargets(face.faceGeometry(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(face.id(), resolved, visiting));
        } else if (entity instanceof StepFaceSurface face) {
            targets.addAll(collectSemanticTargets(face.bounds(), resolved, visiting));
            targets.addAll(collectSemanticTargets(face.faceGeometry(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(face.id(), resolved, visiting));
        } else if (entity instanceof StepOpenShell shell) {
            targets.addAll(collectSemanticTargets(shell.faces(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(shell.id(), resolved, visiting));
        } else if (entity instanceof StepSurfacedOpenShell shell) {
            targets.addAll(collectSemanticTargets(shell.faces(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(shell.id(), resolved, visiting));
        } else if (entity instanceof StepOrientedOpenShell shell) {
            targets.addAll(collectSemanticTargets(shell.openShellElement(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(shell.id(), resolved, visiting));
        } else if (entity instanceof StepClosedShell shell) {
            targets.addAll(collectSemanticTargets(shell.faces(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(shell.id(), resolved, visiting));
        } else if (entity instanceof StepOrientedClosedShell shell) {
            targets.addAll(collectSemanticTargets(shell.closedShellElement(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(shell.id(), resolved, visiting));
        } else if (entity instanceof StepConnectedFaceSet faceSet) {
            targets.addAll(collectSemanticTargets(faceSet.faces(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(faceSet.id(), resolved, visiting));
        } else if (entity instanceof StepConnectedFaceSubSet faceSet) {
            targets.addAll(collectSemanticTargets(faceSet.faces(), resolved, visiting));
            targets.addAll(collectSemanticTargets(faceSet.parentFaceSet(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(faceSet.id(), resolved, visiting));
        } else if (entity instanceof StepOrientedEdge edge) {
            targets.addAll(collectSemanticTargets(edge.edgeElement(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(edge.id(), resolved, visiting));
        } else if (entity instanceof StepOrientedFace face) {
            targets.addAll(collectSemanticTargets(face.faceElement(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(face.id(), resolved, visiting));
        } else if (entity instanceof StepPath path) {
            targets.addAll(collectSemanticTargets(path.edges(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(path.id(), resolved, visiting));
        } else if (entity instanceof StepOpenPath path) {
            targets.addAll(collectSemanticTargets(path.edges(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(path.id(), resolved, visiting));
        } else if (entity instanceof StepSubpath subpath) {
            targets.addAll(collectSemanticTargets(subpath.edges(), resolved, visiting));
            targets.addAll(collectSemanticTargets(subpath.parentPath(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(subpath.id(), resolved, visiting));
        } else if (entity instanceof StepOrientedPath path) {
            targets.addAll(collectSemanticTargets(path.pathElement(), resolved, visiting));
            targets.addAll(collectSemanticTargets(path.edges(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(path.id(), resolved, visiting));
        } else if (entity instanceof StepWireShell wireShell) {
            targets.addAll(collectSemanticTargets(wireShell.loops(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(wireShell.id(), resolved, visiting));
        } else if (entity instanceof StepVertexShell vertexShell) {
            targets.addAll(collectSemanticTargets(vertexShell.extent(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(vertexShell.id(), resolved, visiting));
        } else if (entity instanceof StepShellBasedWireframeModel model) {
            targets.addAll(collectSemanticTargets(model.boundaries(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(model.id(), resolved, visiting));
        } else if (entity instanceof StepSubedge subedge) {
            targets.addAll(collectSemanticTargets(subedge.start(), resolved, visiting));
            targets.addAll(collectSemanticTargets(subedge.end(), resolved, visiting));
            targets.addAll(collectSemanticTargets(subedge.parentEdge(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(subedge.id(), resolved, visiting));
        } else if (entity instanceof StepCartesianTransformationOperator transformation) {
            if (transformation.axis1() != null) {
                targets.addAll(collectSemanticTargets(transformation.axis1(), resolved, visiting));
            }
            if (transformation.axis2() != null) {
                targets.addAll(collectSemanticTargets(transformation.axis2(), resolved, visiting));
            }
            if (transformation.axis3() != null) {
                targets.addAll(collectSemanticTargets(transformation.axis3(), resolved, visiting));
            }
            targets.addAll(collectSemanticTargets(transformation.localOrigin(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(transformation.id(), resolved, visiting));
        } else if (entity instanceof StepGeometricReplica replica) {
            targets.addAll(collectSemanticTargets(replica.parent(), resolved, visiting));
            targets.addAll(collectSemanticTargets(replica.transformation(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(replica.id(), resolved, visiting));
        } else if (entity instanceof StepSweptAreaSolid solid) {
            targets.addAll(collectSemanticTargets(solid.sweptArea(), resolved, visiting));
            targets.addAll(collectSemanticTargets(solid.position(), resolved, visiting));
            targets.addAll(collectSemanticTargets(solid.sweepReference(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting));
        } else if (entity instanceof StepSweptDiskSolid solid) {
            targets.addAll(collectSemanticTargets(solid.sweptCurve(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting));
        } else if (entity instanceof StepComplexClippingResult solid) {
            targets.addAll(collectSemanticTargets(solid.firstOperand(), resolved, visiting));
            targets.addAll(collectSemanticTargets(solid.secondOperand(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting));
        } else if (entity instanceof StepSolidReplica solid) {
            targets.addAll(collectSemanticTargets(solid.parentSolid(), resolved, visiting));
            targets.addAll(collectSemanticTargets(solid.transformation(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting));
        } else if (entity instanceof StepManifoldSolidBrep solid) {
            targets.addAll(collectSemanticTargets(solid.outer(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting));
        } else if (entity instanceof StepBrepWithVoids solid) {
            targets.addAll(collectSemanticTargets(solid.outer(), resolved, visiting));
            targets.addAll(collectSemanticTargets(solid.voids(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting));
        } else if (entity instanceof StepBooleanClippingResult result) {
            targets.addAll(collectSemanticTargets(result.firstOperand(), resolved, visiting));
            targets.addAll(collectSemanticTargets(result.secondOperand(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(result.id(), resolved, visiting));
        } else if (entity instanceof StepBooleanResult result) {
            targets.addAll(collectSemanticTargets(result.firstOperand(), resolved, visiting));
            targets.addAll(collectSemanticTargets(result.secondOperand(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(result.id(), resolved, visiting));
        } else if (entity instanceof StepCsgSolid solid) {
            targets.addAll(collectSemanticTargets(solid.treeRootExpression(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting));
        } else if (entity instanceof StepCsgPrimitive primitive) {
            targets.addAll(collectSemanticTargets(primitive.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(primitive.id(), resolved, visiting));
        } else if (entity instanceof StepRepresentationContext context) {
            targets.addAll(collectTargetsForRepresentationContext(context.id(), resolved, visiting));
        } else if (entity instanceof StepGeometricRepresentationContext context) {
            if (context.globalUnitAssignedContext() != null) {
                targets.addAll(collectSemanticTargets(context.globalUnitAssignedContext(), resolved, visiting));
            }
            if (context.globalUncertaintyAssignedContext() != null) {
                targets.addAll(collectSemanticTargets(context.globalUncertaintyAssignedContext(), resolved, visiting));
            }
            targets.addAll(collectTargetsForRepresentationContext(context.id(), resolved, visiting));
        } else if (entity instanceof StepAbstractVariable variable) {
            targets.add(variable.usedRepresentation());
            targets.addAll(collectSemanticTargets(variable.definition(), resolved, visiting));
        } else if (entity instanceof StepScalarVariable variable) {
            targets.add(variable.usedRepresentation());
            targets.addAll(collectSemanticTargets(variable.definition(), resolved, visiting));
        } else if (entity instanceof StepRowVariable variable) {
            targets.add(variable.usedRepresentation());
            targets.addAll(collectSemanticTargets(variable.definition(), resolved, visiting));
        } else if (entity instanceof StepForwardChainingRulePremise variable) {
            targets.add(variable.usedRepresentation());
            targets.addAll(collectSemanticTargets(variable.definition(), resolved, visiting));
        } else if (entity instanceof StepBackChainingRuleBody variable) {
            targets.add(variable.usedRepresentation());
            targets.addAll(collectSemanticTargets(variable.definition(), resolved, visiting));
        } else if (entity instanceof StepApplicationContext applicationContext) {
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepApplicationProtocolDefinition protocolDefinition
                        && protocolDefinition.application().id() == applicationContext.id()) {
                    targets.addAll(collectSemanticTargets(protocolDefinition, resolved, visiting));
                } else if (candidate instanceof StepProductContext productContext
                        && productContext.frameOfReference().id() == applicationContext.id()) {
                    targets.addAll(collectSemanticTargets(productContext, resolved, visiting));
                } else if (candidate instanceof StepProductDefinitionContext productDefinitionContext
                        && productDefinitionContext.frameOfReference().id() == applicationContext.id()) {
                    targets.addAll(collectSemanticTargets(productDefinitionContext, resolved, visiting));
                }
            }
        } else if (entity instanceof StepApplicationProtocolDefinition protocolDefinition) {
            targets.addAll(collectSemanticTargets(protocolDefinition.application(), resolved, visiting));
        } else if (entity instanceof StepProductContext productContext) {
            targets.addAll(collectSemanticTargets(productContext.frameOfReference(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepProduct product
                        && product.frameOfReference().stream().anyMatch(context -> context.id() == productContext.id())) {
                    targets.addAll(collectSemanticTargets(product, resolved, visiting));
                }
            }
        } else if (entity instanceof StepProductDefinitionContext productDefinitionContext) {
            targets.addAll(collectSemanticTargets(productDefinitionContext.frameOfReference(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepProductDefinition productDefinition
                        && productDefinition.frameOfReference().id() == productDefinitionContext.id()) {
                    targets.addAll(collectSemanticTargets(productDefinition, resolved, visiting));
                }
            }
        } else if (entity instanceof StepGeneralProperty generalProperty) {
            targets.addAll(collectTargetsReferencingEntity(generalProperty.id(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepGeneralPropertyRelationship relationship) {
                    if (relationship.relatingGeneralProperty().id() == generalProperty.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedGeneralProperty(), resolved, visiting));
                    }
                    if (relationship.relatedGeneralProperty().id() == generalProperty.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingGeneralProperty(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepDocument document) {
            targets.addAll(collectTargetsReferencingEntity(document.id(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepDocumentReference reference
                        && reference.assignedDocument().id() == document.id()) {
                    targets.addAll(collectSemanticTargets(reference, resolved, visiting));
                } else if (candidate instanceof StepAppliedDocumentReference reference
                        && reference.assignedDocument().id() == document.id()) {
                    targets.addAll(collectSemanticTargets(reference, resolved, visiting));
                } else if (candidate instanceof StepDocumentRelationship relationship) {
                    if (relationship.relatingDocument().id() == document.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedDocument(), resolved, visiting));
                    }
                    if (relationship.relatedDocument().id() == document.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingDocument(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepDocumentUsageConstraint documentUsageConstraint) {
            targets.addAll(collectSemanticTargets(documentUsageConstraint.source(), resolved, visiting));
        } else if (entity instanceof StepGroup group) {
            targets.addAll(collectTargetsReferencingEntity(group.id(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepGroupAssignment assignment
                        && assignment.assignedGroup().id() == group.id()) {
                    targets.addAll(collectSemanticTargets(assignment.assignedGroup(), resolved, visiting));
                } else if (candidate instanceof StepAppliedGroupAssignment assignment
                        && assignment.assignedGroup().id() == group.id()) {
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepClassificationAssignment assignment
                        && assignment.assignedClass().id() == group.id()) {
                    targets.addAll(collectSemanticTargets(assignment.assignedClass(), resolved, visiting));
                } else if (candidate instanceof StepAppliedClassificationAssignment assignment
                        && assignment.assignedClass().id() == group.id()) {
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepGroupRelationship relationship) {
                    if (relationship.relatingGroup().id() == group.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedGroup(), resolved, visiting));
                    }
                    if (relationship.relatedGroup().id() == group.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingGroup(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepOrganization organization) {
            targets.addAll(collectTargetsReferencingEntity(organization.id(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepAppliedOrganizationAssignment assignment
                        && assignment.assignedOrganization().id() == organization.id()) {
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepOrganizationAssignment assignment
                        && assignment.assignedOrganization().id() == organization.id()) {
                    targets.addAll(collectSemanticTargets(assignment.assignedOrganization(), resolved, visiting));
                } else if (candidate instanceof StepOrganizationRelationship relationship) {
                    if (relationship.relatingOrganization().id() == organization.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedOrganization(), resolved, visiting));
                    }
                    if (relationship.relatedOrganization().id() == organization.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingOrganization(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepProductCategory productCategory) {
            targets.addAll(collectTargetsReferencingEntity(productCategory.id(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepProductCategoryRelationship relationship) {
                    if (relationship.category().id() == productCategory.id()) {
                        targets.addAll(collectSemanticTargets(relationship.subCategory(), resolved, visiting));
                    }
                    if (relationship.subCategory().id() == productCategory.id()) {
                        targets.addAll(collectSemanticTargets(relationship.category(), resolved, visiting));
                    }
                } else if (candidate instanceof StepProductRelatedProductCategory relatedCategory
                        && relatedCategory.id() == productCategory.id()) {
                    targets.addAll(collectSemanticTargets(relatedCategory.products(), resolved, visiting));
                }
            }
        } else if (entity instanceof StepProductRelatedProductCategory relatedCategory) {
            targets.addAll(collectSemanticTargets(relatedCategory.products(), resolved, visiting));
        } else if (entity instanceof StepProduct product) {
            targets.addAll(collectTargetsReferencingEntity(product.id(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepProductDefinitionFormation formation
                        && formation.ofProduct().id() == product.id()) {
                    targets.addAll(collectSemanticTargets(formation, resolved, visiting));
                } else if (candidate instanceof StepProductRelatedProductCategory relatedCategory
                        && relatedCategory.products().stream().anyMatch(related -> related.id() == product.id())) {
                    targets.addAll(collectSemanticTargets(relatedCategory, resolved, visiting));
                } else if (candidate instanceof StepProductRelationship relationship) {
                    if (relationship.relatingProduct().id() == product.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedProduct(), resolved, visiting));
                    }
                    if (relationship.relatedProduct().id() == product.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingProduct(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepProductDefinitionFormation formation) {
            targets.addAll(collectTargetsReferencingEntity(formation.id(), resolved, visiting));
            targets.addAll(collectSemanticTargets(formation.ofProduct(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepProductDefinition productDefinition
                        && productDefinition.formation().id() == formation.id()) {
                    targets.addAll(collectSemanticTargets(productDefinition, resolved, visiting));
                } else if (candidate instanceof StepProductDefinitionFormationRelationship relationship) {
                    if (relationship.relatingFormation().id() == formation.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedFormation(), resolved, visiting));
                    }
                    if (relationship.relatedFormation().id() == formation.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingFormation(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepProductDefinitionEffectivity effectivity) {
            targets.addAll(collectSemanticTargets(effectivity.productDefinition(), resolved, visiting));
        } else if (entity instanceof StepEffectivity effectivity) {
            targets.addAll(collectTargetsReferencingEntity(effectivity.id(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepEffectivityRelationship relationship) {
                    if (relationship.relatingEffectivity().id() == effectivity.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedEffectivity(), resolved, visiting));
                    }
                    if (relationship.relatedEffectivity().id() == effectivity.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingEffectivity(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepCalendarDate calendarDate) {
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepDateAssignment assignment
                        && assignment.assignedDate().id() == calendarDate.id()) {
                    targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
                } else if (candidate instanceof StepAppliedDateAssignment assignment
                        && assignment.assignedDate().id() == calendarDate.id()) {
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepDateAndTime dateAndTime
                        && dateAndTime.dateComponent().id() == calendarDate.id()) {
                    targets.addAll(collectSemanticTargets(dateAndTime, resolved, visiting));
                }
            }
        } else if (entity instanceof StepDateAndTime dateAndTime) {
            targets.addAll(collectSemanticTargets(dateAndTime.dateComponent(), resolved, visiting));
            targets.addAll(collectSemanticTargets(dateAndTime.timeComponent(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepDateTimeAssignment assignment
                        && assignment.assignedDateAndTime().id() == dateAndTime.id()) {
                    targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
                } else if (candidate instanceof StepAppliedDateTimeAssignment assignment
                        && assignment.assignedDateAndTime().id() == dateAndTime.id()) {
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepApprovalDateTime approvalDateTime
                        && approvalDateTime.dateTime().id() == dateAndTime.id()) {
                    targets.addAll(collectSemanticTargets(approvalDateTime, resolved, visiting));
                }
            }
        } else if (entity instanceof StepLocalTime localTime) {
            targets.addAll(collectSemanticTargets(localTime.zone(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepDateAndTime dateAndTime
                        && dateAndTime.timeComponent().id() == localTime.id()) {
                    targets.addAll(collectSemanticTargets(dateAndTime, resolved, visiting));
                }
            }
        } else if (entity instanceof StepCoordinatedUniversalTimeOffset zone) {
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepLocalTime localTime
                        && localTime.zone().id() == zone.id()) {
                    targets.addAll(collectSemanticTargets(localTime, resolved, visiting));
                }
            }
        } else if (entity instanceof StepDateAssignment assignment) {
            targets.addAll(collectTargetsForDateRole(assignment.role().id(), resolved, visiting));
        } else if (entity instanceof StepDateTimeAssignment assignment) {
            targets.addAll(collectTargetsForDateTimeRole(assignment.role().id(), resolved, visiting));
        } else if (entity instanceof StepPerson person) {
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepPersonAndOrganization personAndOrganization
                        && personAndOrganization.person().id() == person.id()) {
                    targets.addAll(collectSemanticTargets(personAndOrganization, resolved, visiting));
                }
            }
        } else if (entity instanceof StepApprovalStatus status) {
            targets.addAll(collectTargetsForApprovalStatus(status.id(), resolved, visiting));
        } else if (entity instanceof StepSecurityClassificationLevel level) {
            targets.addAll(collectTargetsForSecurityLevel(level.id(), resolved, visiting));
        } else if (entity instanceof StepContractType kind) {
            targets.addAll(collectTargetsForContractType(kind.id(), resolved, visiting));
        } else if (entity instanceof StepCertificationType kind) {
            targets.addAll(collectTargetsForCertificationType(kind.id(), resolved, visiting));
        } else if (entity instanceof StepApprovalRole role) {
            targets.addAll(collectTargetsForApprovalRole(role.id(), resolved, visiting));
        } else if (entity instanceof StepOrganizationRole role) {
            targets.addAll(collectTargetsForOrganizationRole(role.id(), resolved, visiting));
        } else if (entity instanceof StepPersonAndOrganizationRole role) {
            targets.addAll(collectTargetsForPersonAndOrganizationRole(role.id(), resolved, visiting));
        } else if (entity instanceof StepClassificationRole role) {
            targets.addAll(collectTargetsForClassificationRole(role.id(), resolved, visiting));
        } else if (entity instanceof StepDateRole role) {
            targets.addAll(collectTargetsForDateRole(role.id(), resolved, visiting));
        } else if (entity instanceof StepDateTimeRole role) {
            targets.addAll(collectTargetsForDateTimeRole(role.id(), resolved, visiting));
        } else if (entity instanceof StepIdentificationRole role) {
            targets.addAll(collectTargetsForIdentificationRole(role.id(), resolved, visiting));
        } else if (entity instanceof StepDocumentType kind) {
            targets.addAll(collectTargetsForDocumentType(kind.id(), resolved, visiting));
        } else if (entity instanceof StepApproval approval) {
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepAppliedApprovalAssignment assignment
                        && assignment.assignedApproval().id() == approval.id()) {
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepApprovalAssignment assignment
                        && assignment.assignedApproval().id() == approval.id()) {
                    targets.addAll(collectSemanticTargets(assignment.assignedApproval(), resolved, visiting));
                } else if (candidate instanceof StepApprovalPersonOrganization personOrganization
                        && personOrganization.authorizedApproval().id() == approval.id()) {
                    targets.addAll(collectSemanticTargets(personOrganization.personOrganization(), resolved, visiting));
                } else if (candidate instanceof StepApprovalDateTime approvalDateTime
                        && approvalDateTime.datedApproval().id() == approval.id()) {
                    targets.addAll(collectSemanticTargets(approvalDateTime.dateTime(), resolved, visiting));
                }
            }
        } else if (entity instanceof StepSecurityClassification classification) {
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepAppliedSecurityClassificationAssignment assignment
                        && assignment.assignedSecurityClassification().id() == classification.id()) {
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepSecurityClassificationAssignment assignment
                        && assignment.assignedSecurityClassification().id() == classification.id()) {
                    targets.addAll(collectSemanticTargets(assignment.assignedSecurityClassification(), resolved, visiting));
                }
            }
        } else if (entity instanceof StepContract contract) {
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepAppliedContractAssignment assignment
                        && assignment.assignedContract().id() == contract.id()) {
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepContractAssignment assignment
                        && assignment.assignedContract().id() == contract.id()) {
                    targets.addAll(collectSemanticTargets(assignment.assignedContract(), resolved, visiting));
                }
            }
        } else if (entity instanceof StepCertification certification) {
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepAppliedCertificationAssignment assignment
                        && assignment.assignedCertification().id() == certification.id()) {
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepCertificationAssignment assignment
                        && assignment.assignedCertification().id() == certification.id()) {
                    targets.addAll(collectSemanticTargets(assignment.assignedCertification(), resolved, visiting));
                }
            }
        } else if (entity instanceof StepPersonAndOrganization personAndOrganization) {
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepAppliedPersonAndOrganizationAssignment assignment
                        && assignment.assignedPersonAndOrganization().id() == personAndOrganization.id()) {
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepPersonAndOrganizationAssignment assignment
                        && assignment.assignedPersonAndOrganization().id() == personAndOrganization.id()) {
                    targets.addAll(collectSemanticTargets(assignment.assignedPersonAndOrganization(), resolved, visiting));
                }
            }
        } else if (entity instanceof StepLanguage language) {
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepAppliedLanguageAssignment assignment
                        && assignment.assignedLanguage().id() == language.id()) {
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepLanguageAssignment assignment
                        && assignment.assignedLanguage().id() == language.id()) {
                    targets.addAll(collectSemanticTargets(assignment.assignedLanguage(), resolved, visiting));
                }
            }
        } else if (entity instanceof StepExternalIdentificationAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.source(), resolved, visiting));
        } else if (entity instanceof StepExternalSource source) {
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepExternallyDefinedItem item
                        && item.source().id() == source.id()) {
                    targets.addAll(collectSemanticTargets(item, resolved, visiting));
                } else if (candidate instanceof StepAppliedExternalIdentificationAssignment assignment
                        && assignment.source().id() == source.id()) {
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepExternalSourceRelationship relationship) {
                    if (relationship.relatingSource().id() == source.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedSource(), resolved, visiting));
                    }
                    if (relationship.relatedSource().id() == source.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingSource(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepExternallyDefinedItem item) {
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting));
            targets.addAll(collectSemanticTargets(item.source(), resolved, visiting));
        } else if (entity instanceof StepGeneralPropertyRelationship relationship) {
            targets.addAll(collectSemanticTargets(relationship.relatingGeneralProperty(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedGeneralProperty(), resolved, visiting));
        } else if (entity instanceof StepApprovalAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.assignedApproval(), resolved, visiting));
        } else if (entity instanceof StepClassificationAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.assignedClass(), resolved, visiting));
        } else if (entity instanceof StepGroupAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.assignedGroup(), resolved, visiting));
        } else if (entity instanceof StepSecurityClassificationAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.assignedSecurityClassification(), resolved, visiting));
        } else if (entity instanceof StepContractAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.assignedContract(), resolved, visiting));
        } else if (entity instanceof StepCertificationAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.assignedCertification(), resolved, visiting));
        } else if (entity instanceof StepPersonAndOrganizationAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.assignedPersonAndOrganization(), resolved, visiting));
        } else if (entity instanceof StepOrganizationAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.assignedOrganization(), resolved, visiting));
        } else if (entity instanceof StepLanguageAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.assignedLanguage(), resolved, visiting));
        } else if (entity instanceof StepDocumentReference reference) {
            targets.addAll(collectSemanticTargets(reference.assignedDocument(), resolved, visiting));
        } else if (entity instanceof StepPresentationLayerAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.assignedItems(), resolved, visiting));
        } else if (entity instanceof StepApprovalPersonOrganization approvalPersonOrganization) {
            targets.addAll(collectSemanticTargets(approvalPersonOrganization.authorizedApproval(), resolved, visiting));
        } else if (entity instanceof StepApprovalDateTime approvalDateTime) {
            targets.addAll(collectSemanticTargets(approvalDateTime.datedApproval(), resolved, visiting));
        } else if (entity instanceof StepItemDefinedTransformation transformation) {
            targets.addAll(collectTargetsForItemDefinedTransformation(transformation.id(), resolved));
        } else if (entity instanceof StepExternalSourceRelationship relationship) {
            targets.addAll(collectSemanticTargets(relationship.relatingSource(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedSource(), resolved, visiting));
        } else if (entity instanceof StepDocumentRelationship relationship) {
            targets.addAll(collectSemanticTargets(relationship.relatingDocument(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedDocument(), resolved, visiting));
        } else if (entity instanceof StepGroupRelationship relationship) {
            targets.addAll(collectSemanticTargets(relationship.relatingGroup(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedGroup(), resolved, visiting));
        } else if (entity instanceof StepOrganizationRelationship relationship) {
            targets.addAll(collectSemanticTargets(relationship.relatingOrganization(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedOrganization(), resolved, visiting));
        } else if (entity instanceof StepProductCategoryRelationship relationship) {
            targets.addAll(collectSemanticTargets(relationship.category(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.subCategory(), resolved, visiting));
        } else if (entity instanceof StepProductRelationship relationship) {
            targets.addAll(collectSemanticTargets(relationship.relatingProduct(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedProduct(), resolved, visiting));
        } else if (entity instanceof StepProductDefinitionFormationRelationship relationship) {
            targets.addAll(collectSemanticTargets(relationship.relatingFormation(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedFormation(), resolved, visiting));
        } else if (entity instanceof StepEffectivityRelationship relationship) {
            targets.addAll(collectSemanticTargets(relationship.relatingEffectivity(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedEffectivity(), resolved, visiting));
        } else if (entity instanceof StepRepresentationRelationship relationship) {
            targets.addAll(collectRepresentationTargetsFromRelationship(relationship));
        } else if (entity instanceof StepRepresentationRelationshipWithTransformation relationship) {
            targets.addAll(collectRepresentationTargetsFromRelationship(relationship));
        } else if (entity instanceof StepShapeRepresentationRelationship relationship) {
            targets.addAll(collectRepresentationTargetsFromRelationship(relationship));
        } else if (entity instanceof StepGeometricItemSpecificUsage usage) {
            targets.addAll(collectSemanticTargets(usage.usage(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
        } else if (entity instanceof StepChainBasedGeometricItemSpecificUsage usage) {
            targets.addAll(collectSemanticTargets(usage.usage(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.nodes(), resolved, visiting));
            for (StepRepresentationRelationship relationship : usage.undirectedLinks()) {
                targets.addAll(collectRepresentationTargetsFromRelationship(relationship));
            }
        } else if (entity instanceof StepItemIdentifiedRepresentationUsage usage) {
            targets.add(usage.usedRepresentation());
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
        } else if (entity instanceof StepChainBasedItemIdentifiedRepresentationUsage usage) {
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.nodes(), resolved, visiting));
            for (StepRepresentationRelationship relationship : usage.undirectedLinks()) {
                targets.addAll(collectRepresentationTargetsFromRelationship(relationship));
            }
        } else if (entity instanceof StepPlacedTarget usage) {
            targets.add(usage.usedRepresentation());
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
        } else if (entity instanceof StepDraughtingModelItemAssociation usage) {
            targets.add(usage.usedRepresentation());
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
        } else if (entity instanceof StepDraughtingModelItemAssociationWithPlaceholder usage) {
            targets.add(usage.usedRepresentation());
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.annotationPlaceholder(), resolved, visiting));
        } else if (entity instanceof StepPmiRequirementItemAssociation usage) {
            targets.add(usage.usedRepresentation());
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.requirement(), resolved, visiting));
        } else if (entity instanceof StepMechanicalDesignRequirementItemAssociation usage) {
            targets.add(usage.usedRepresentation());
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.requirement(), resolved, visiting));
        } else if (entity instanceof StepStyledItem styledItem) {
            targets.addAll(collectSemanticTargets(styledItem.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(styledItem.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(styledItem.id(), resolved, visiting));
        } else if (entity instanceof StepOverRidingStyledItem styledItem) {
            targets.addAll(collectSemanticTargets(styledItem.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(styledItem.item(), resolved, visiting));
            targets.addAll(collectSemanticTargets(styledItem.overRiddenStyle(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(styledItem.id(), resolved, visiting));
        } else if (entity instanceof StepMappedItem mappedItem) {
            targets.addAll(collectSemanticTargets(mappedItem.mappingSource(), resolved, visiting));
            targets.addAll(collectSemanticTargets(mappedItem.mappingTarget(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(mappedItem.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationCurveOccurrence occurrence) {
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationFillArea fillArea) {
            targets.addAll(collectSemanticTargets(fillArea.boundaries(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(fillArea.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationFillAreaOccurrence occurrence) {
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.fillStyleTarget(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationPlaceholderOccurrence occurrence) {
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationPlane plane) {
            targets.addAll(collectSemanticTargets(plane.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(plane.item(), resolved, visiting));
            targets.addAll(collectSemanticTargets(plane.elements(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(plane.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationPointOccurrence occurrence) {
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationSymbolOccurrence occurrence) {
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationSubfigureOccurrence occurrence) {
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationTextOccurrence occurrence) {
            targets.addAll(collectSemanticTargets(occurrence.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepDraughtingAnnotationOccurrence occurrence) {
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepDimensionCurve occurrence) {
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepLeaderCurve occurrence) {
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepProjectionCurve occurrence) {
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepTerminatorSymbol symbol) {
            targets.addAll(collectSemanticTargets(symbol.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(symbol.item(), resolved, visiting));
            targets.addAll(collectSemanticTargets(symbol.annotatedCurve(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(symbol.id(), resolved, visiting));
        } else if (entity instanceof StepDraughtingCallout callout) {
            targets.addAll(collectSemanticTargets(callout.contents(), resolved, visiting));
        } else if (entity instanceof StepDraughtingCalloutRelationship relationship) {
            targets.addAll(collectSemanticTargets(relationship.relatingCallout(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedCallout(), resolved, visiting));
        } else if (entity instanceof StepAnnotationOccurrenceRelationship relationship) {
            targets.addAll(collectSemanticTargets(relationship.relatingAnnotationOccurrence(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedAnnotationOccurrence(), resolved, visiting));
        } else if (entity instanceof StepRepresentationMap mapping) {
            targets.add(mapping.mappedRepresentation());
            targets.addAll(collectSemanticTargets(mapping.mappedOrigin(), resolved, visiting));
        } else if (entity instanceof StepSymbolRepresentationMap mapping) {
            targets.add(mapping.mappedRepresentation());
            targets.addAll(collectSemanticTargets(mapping.mappedOrigin(), resolved, visiting));
        } else if (entity instanceof StepAnnotationSymbol annotationSymbol) {
            targets.addAll(collectSemanticTargets(annotationSymbol.mappingSource(), resolved, visiting));
            targets.addAll(collectSemanticTargets(annotationSymbol.mappingTarget(), resolved, visiting));
        } else if (entity instanceof StepAnnotationText annotationText) {
            targets.addAll(collectSemanticTargets(annotationText.mappingSource(), resolved, visiting));
            targets.addAll(collectSemanticTargets(annotationText.mappingTarget(), resolved, visiting));
        } else if (entity instanceof StepAnnotationTextCharacter annotationTextCharacter) {
            targets.addAll(collectSemanticTargets(annotationTextCharacter.mappingSource(), resolved, visiting));
            targets.addAll(collectSemanticTargets(annotationTextCharacter.mappingTarget(), resolved, visiting));
        } else if (entity instanceof StepUserDefinedCurveFont curveFont) {
            targets.addAll(collectSemanticTargets(curveFont.mappingSource(), resolved, visiting));
            targets.addAll(collectSemanticTargets(curveFont.mappingTarget(), resolved, visiting));
        } else if (entity instanceof StepUserDefinedMarker marker) {
            targets.addAll(collectSemanticTargets(marker.mappingSource(), resolved, visiting));
            targets.addAll(collectSemanticTargets(marker.mappingTarget(), resolved, visiting));
        } else if (entity instanceof StepUserDefinedTerminatorSymbol symbol) {
            targets.addAll(collectSemanticTargets(symbol.mappingSource(), resolved, visiting));
            targets.addAll(collectSemanticTargets(symbol.mappingTarget(), resolved, visiting));
        } else if (entity instanceof StepPresentationStyleAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.styles(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(assignment.id(), resolved, visiting));
        } else if (entity instanceof StepFillAreaStyle fillAreaStyle) {
            targets.addAll(collectSemanticTargets(fillAreaStyle.styles(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(fillAreaStyle.id(), resolved, visiting));
        } else if (entity instanceof StepFillAreaStyleColour fillAreaStyleColour) {
            targets.addAll(collectSemanticTargets(fillAreaStyleColour.colour(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(fillAreaStyleColour.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleFillArea style) {
            targets.addAll(collectSemanticTargets(style.fillStyle(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepCharacterGlyphStyleStroke style) {
            targets.addAll(collectSemanticTargets(style.strokeStyle(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepCharacterGlyphStyleOutline style) {
            targets.addAll(collectSemanticTargets(style.outlineStyle(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepCharacterGlyphStyleOutlineWithCharacteristics style) {
            targets.addAll(collectSemanticTargets(style.outlineStyle(), resolved, visiting));
            targets.addAll(collectSemanticTargets(style.characteristics(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedCurveFont curveFont) {
            targets.addAll(collectTargetsForCurveFont(curveFont.id(), resolved, visiting));
        } else if (entity instanceof StepDraughtingPreDefinedCurveFont curveFont) {
            targets.addAll(collectTargetsForCurveFont(curveFont.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedMarker marker) {
            targets.addAll(collectTargetsForPointMarker(marker.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedPointMarkerSymbol marker) {
            targets.addAll(collectTargetsForPointMarker(marker.id(), resolved, visiting));
        } else if (entity instanceof StepColour colour) {
            targets.addAll(collectTargetsForStyleColour(colour.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(colour.id(), resolved, visiting));
        } else if (entity instanceof StepColourSpecification colour) {
            targets.addAll(collectTargetsForStyleColour(colour.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(colour.id(), resolved, visiting));
        } else if (entity instanceof StepColourRgb colour) {
            targets.addAll(collectTargetsForStyleColour(colour.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(colour.id(), resolved, visiting));
        } else if (entity instanceof StepConversionBasedUnit unit) {
            targets.addAll(collectSemanticTargets(unit.conversionFactor(), resolved, visiting));
            targets.addAll(collectTargetsForAssignedUnit(unit.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(unit.id(), resolved, visiting));
        } else if (entity instanceof StepConversionBasedUnitWithOffset unit) {
            targets.addAll(collectSemanticTargets(unit.conversionFactor(), resolved, visiting));
            targets.addAll(collectTargetsForAssignedUnit(unit.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(unit.id(), resolved, visiting));
        } else if (entity instanceof StepDerivedUnit unit) {
            targets.addAll(collectSemanticTargets(unit.elements(), resolved, visiting));
            targets.addAll(collectTargetsForAssignedUnit(unit.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(unit.id(), resolved, visiting));
        } else if (entity instanceof StepDerivedUnitElement element) {
            targets.addAll(collectSemanticTargets(element.unit(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(element.id(), resolved, visiting));
        } else if (entity instanceof StepNamedUnit unit) {
            targets.addAll(collectTargetsForAssignedUnit(unit.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(unit.id(), resolved, visiting));
        } else if (entity instanceof StepSiUnit unit) {
            targets.addAll(collectTargetsForAssignedUnit(unit.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(unit.id(), resolved, visiting));
        } else if (entity instanceof StepContextDependentUnit unit) {
            targets.addAll(collectTargetsForAssignedUnit(unit.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(unit.id(), resolved, visiting));
        } else if (entity instanceof StepGlobalUncertaintyAssignedContext context) {
            targets.addAll(collectSemanticTargets(context.uncertainties(), resolved, visiting));
            targets.addAll(collectTargetsForGlobalUncertaintyContext(context.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(context.id(), resolved, visiting));
        } else if (entity instanceof StepGlobalUnitAssignedContext context) {
            targets.addAll(collectSemanticTargets(context.units(), resolved, visiting));
            targets.addAll(collectTargetsForGlobalUnitContext(context.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(context.id(), resolved, visiting));
        } else if (entity instanceof StepRepresentationItem item) {
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting));
        } else if (entity instanceof StepGeometricRepresentationItem item) {
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting));
        } else if (entity instanceof StepTopologicalRepresentationItem item) {
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedColour colour) {
            targets.addAll(collectTargetsForStyleColour(colour.id(), resolved, visiting));
        } else if (entity instanceof StepDraughtingPreDefinedColour colour) {
            targets.addAll(collectTargetsForStyleColour(colour.id(), resolved, visiting));
        } else if (entity instanceof StepCurveStyle curveStyle) {
            targets.addAll(collectSemanticTargets(curveStyle.curveFont(), resolved, visiting));
            targets.addAll(collectSemanticTargets(curveStyle.colour(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curveStyle.id(), resolved, visiting));
        } else if (entity instanceof StepPointStyle pointStyle) {
            targets.addAll(collectSemanticTargets(pointStyle.marker(), resolved, visiting));
            targets.addAll(collectSemanticTargets(pointStyle.colour(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(pointStyle.id(), resolved, visiting));
        } else if (entity instanceof StepSymbolColour symbolColour) {
            targets.addAll(collectSemanticTargets(symbolColour.colour(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(symbolColour.id(), resolved, visiting));
        } else if (entity instanceof StepSymbolStyle symbolStyle) {
            targets.addAll(collectSemanticTargets(symbolStyle.styleOfSymbol(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(symbolStyle.id(), resolved, visiting));
        } else if (entity instanceof StepTextStyleForDefinedFont textStyle) {
            targets.addAll(collectSemanticTargets(textStyle.textColour(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(textStyle.id(), resolved, visiting));
        } else if (entity instanceof StepTextStyle textStyle) {
            targets.addAll(collectSemanticTargets(textStyle.characterAppearance(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(textStyle.id(), resolved, visiting));
        } else if (entity instanceof StepTextStyleWithSpacing textStyle) {
            targets.addAll(collectSemanticTargets(textStyle.characterAppearance(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(textStyle.id(), resolved, visiting));
        } else if (entity instanceof StepTextStyleWithJustification textStyle) {
            targets.addAll(collectSemanticTargets(textStyle.characterAppearance(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(textStyle.id(), resolved, visiting));
        } else if (entity instanceof StepTextStyleWithBoxCharacteristics textStyle) {
            targets.addAll(collectSemanticTargets(textStyle.characterAppearance(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(textStyle.id(), resolved, visiting));
        } else if (entity instanceof StepTextStyleWithMirror textStyle) {
            targets.addAll(collectSemanticTargets(textStyle.characterAppearance(), resolved, visiting));
            targets.addAll(collectSemanticTargets(textStyle.mirrorPlacement(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(textStyle.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleBoundary style) {
            targets.addAll(collectSemanticTargets(style.style(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleParameterLine style) {
            targets.addAll(collectSemanticTargets(style.style(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleSegmentationCurve style) {
            targets.addAll(collectSemanticTargets(style.style(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleSilhouette style) {
            targets.addAll(collectSemanticTargets(style.style(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleControlGrid style) {
            targets.addAll(collectSemanticTargets(style.style(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceSideStyle sideStyle) {
            targets.addAll(collectSemanticTargets(sideStyle.styles(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(sideStyle.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleUsage usage) {
            targets.addAll(collectSemanticTargets(usage.style(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(usage.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleTransparent style) {
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleReflectanceAmbient style) {
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleReflectanceAmbientDiffuse style) {
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleReflectanceAmbientDiffuseSpecular style) {
            targets.addAll(collectSemanticTargets(style.specularColour(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedSurfaceSideStyle style) {
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedTextFont textFont) {
            targets.addAll(collectTargetsReferencingEntity(textFont.id(), resolved, visiting));
        } else if (entity instanceof StepDraughtingPreDefinedTextFont textFont) {
            targets.addAll(collectTargetsReferencingEntity(textFont.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedTerminatorSymbol symbol) {
            targets.addAll(collectTargetsReferencingEntity(symbol.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedSymbol symbol) {
            targets.addAll(collectTargetsReferencingEntity(symbol.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedDimensionSymbol symbol) {
            targets.addAll(collectTargetsReferencingEntity(symbol.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedGeometricalToleranceSymbol symbol) {
            targets.addAll(collectTargetsReferencingEntity(symbol.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedItem item) {
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting));
        } else if (entity instanceof StepDescriptionAttribute descriptionAttribute) {
            targets.addAll(collectSemanticTargets(descriptionAttribute.describedItem(), resolved, visiting));
        } else if (entity instanceof StepNameAttribute nameAttribute) {
            targets.addAll(collectSemanticTargets(nameAttribute.namedItem(), resolved, visiting));
        } else if (entity instanceof StepIdAttribute idAttribute) {
            targets.addAll(collectSemanticTargets(idAttribute.identifiedItem(), resolved, visiting));
        } else if (entity instanceof StepAppliedNameAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedIdentificationAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedExternalIdentificationAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedGroupAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedClassificationAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedDateAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedDateTimeAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedApprovalAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedSecurityClassificationAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedDocumentReference assignment) {
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedContractAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedCertificationAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedPersonAndOrganizationAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedOrganizationAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedLanguageAssignment assignment) {
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAttributeAssertion attributeAssertion) {
            targets.add(attributeAssertion.usedRepresentation());
            targets.addAll(collectSemanticTargets(attributeAssertion.definition(), resolved, visiting));
        } else if (entity instanceof StepIdentificationAssignment
                || entity instanceof StepNameAssignment) {
            // Pure assignment metadata without item references contributes no target by itself.
        } else if (entity instanceof StepShapeDefinitionRepresentation shapeDefinitionRepresentation) {
            targets.add(shapeDefinitionRepresentation.usedRepresentation());
            targets.addAll(collectSemanticTargets(shapeDefinitionRepresentation.definition(), resolved, visiting));
        } else if (entity instanceof StepContextDependentShapeRepresentation contextDependent) {
            targets.addAll(collectRepresentationTargetsFromRelationship(contextDependent.representationRelationship()));
            targets.addAll(collectSemanticTargets(contextDependent.representedProductRelation(), resolved, visiting));
        } else if (entity instanceof StepProductDefinitionShape productDefinitionShape) {
            targets.addAll(collectSemanticTargets(productDefinitionShape.definition(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepShapeDefinitionRepresentation link
                        && link.definition().id() == productDefinitionShape.id()) {
                    targets.add(link.usedRepresentation());
                } else if (candidate instanceof StepContextDependentShapeRepresentation contextDependent
                        && contextDependent.representedProductRelation().id() == productDefinitionShape.id()) {
                    targets.addAll(collectSemanticTargets(contextDependent, resolved, visiting));
                } else if (candidate instanceof StepShapeAspect shapeAspect
                        && shapeAspect.ofShape().id() == productDefinitionShape.id()) {
                    targets.addAll(collectSemanticTargets(shapeAspect, resolved, visiting));
                } else if (candidate instanceof StepShapeAspectOccurrence occurrence
                        && occurrence.ofShape().id() == productDefinitionShape.id()) {
                    targets.addAll(collectSemanticTargets(occurrence, resolved, visiting));
                }
            }
        } else if (entity instanceof StepProductDefinition productDefinition) {
            targets.addAll(collectTargetsForProductDefinition(productDefinition.id(), resolved, visiting));
        } else if (entity instanceof StepNextAssemblyUsageOccurrence occurrence) {
            targets.addAll(collectSemanticTargets(occurrence.relatedProductDefinition(), resolved, visiting));
            targets.addAll(collectTargetsForOccurrence(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepProductDefinitionRelationship relationship) {
            targets.addAll(collectSemanticTargets(relationship.relatingProductDefinition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedProductDefinition(), resolved, visiting));
        } else if (entity instanceof StepProductDefinitionRelationshipRelationship relationship) {
            targets.addAll(collectSemanticTargets(relationship.relating(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.related(), resolved, visiting));
        } else if (entity instanceof StepPropertyDefinitionRelationship relationship) {
            targets.addAll(collectSemanticTargets(relationship.relatingPropertyDefinition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedPropertyDefinition(), resolved, visiting));
        } else if (entity instanceof StepShapeAspectOccurrence occurrence) {
            targets.addAll(collectSemanticTargets(occurrence.definition(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepShapeAspect shapeAspect) {
            targets.addAll(collectTargetsReferencingEntity(shapeAspect.id(), resolved, visiting));
        } else if (entity instanceof StepShapeAspectRelationship relationship) {
            targets.addAll(collectSemanticTargets(relationship.relatingShapeAspect(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedShapeAspect(), resolved, visiting));
        }
        visiting.remove(entity.id());
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectRepresentationTargetsFromRelationship(StepEntity relationship) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        if (relationship instanceof StepRepresentationRelationship representationRelationship) {
            targets.add(representationRelationship.rep1());
            targets.add(representationRelationship.rep2());
        } else if (relationship instanceof StepRepresentationRelationshipWithTransformation representationRelationship) {
            targets.add(representationRelationship.rep1());
            targets.add(representationRelationship.rep2());
        } else if (relationship instanceof StepShapeRepresentationRelationship representationRelationship) {
            targets.add(representationRelationship.rep1());
            targets.add(representationRelationship.rep2());
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectSemanticTargets(
            List<? extends StepEntity> entities,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity entity : entities) {
            targets.addAll(collectSemanticTargets(entity, resolved, visiting));
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsReferencingEntity(
            int referencedId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepPropertyDefinition propertyDefinition
                    && propertyDefinition.definition().id() == referencedId) {
                targets.addAll(collectSemanticTargets(propertyDefinition, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForCurveFont(
            int curveFontId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepCurveStyle curveStyle && curveStyle.curveFont().id() == curveFontId) {
                targets.addAll(collectSemanticTargets(curveStyle, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForRepresentationContext(
            int contextId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepRepresentation representation
                    && representation.context() != null
                    && representation.context().id() == contextId) {
                targets.add(representation);
            } else if (candidate instanceof StepPropertyDefinition propertyDefinition
                    && propertyDefinition.definition().id() == contextId) {
                targets.addAll(collectSemanticTargets(propertyDefinition, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForAssignedUnit(
            int unitId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepGlobalUnitAssignedContext context
                    && context.units().stream().anyMatch(unit -> unit.id() == unitId)) {
                targets.addAll(collectSemanticTargets(context, resolved, visiting));
            } else if (candidate instanceof StepGeometricRepresentationContext context
                    && context.globalUnitAssignedContext() != null
                    && context.globalUnitAssignedContext().units().stream().anyMatch(unit -> unit.id() == unitId)) {
                targets.addAll(collectSemanticTargets(context, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForAssignedUncertainty(
            int uncertaintyId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepGlobalUncertaintyAssignedContext context
                    && context.uncertainties().stream().anyMatch(uncertainty -> uncertainty.id() == uncertaintyId)) {
                targets.addAll(collectSemanticTargets(context, resolved, visiting));
            } else if (candidate instanceof StepGeometricRepresentationContext context
                    && context.globalUncertaintyAssignedContext() != null
                    && context.globalUncertaintyAssignedContext().uncertainties().stream()
                    .anyMatch(uncertainty -> uncertainty.id() == uncertaintyId)) {
                targets.addAll(collectSemanticTargets(context, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForGlobalUnitContext(
            int contextId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepGeometricRepresentationContext context
                    && context.globalUnitAssignedContext() != null
                    && context.globalUnitAssignedContext().id() == contextId) {
                targets.addAll(collectSemanticTargets(context, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForGlobalUncertaintyContext(
            int contextId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepGeometricRepresentationContext context
                    && context.globalUncertaintyAssignedContext() != null
                    && context.globalUncertaintyAssignedContext().id() == contextId) {
                targets.addAll(collectSemanticTargets(context, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForItemDefinedTransformation(
            int transformationId,
            Map<Integer, StepEntity> resolved
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepRepresentationRelationshipWithTransformation relationship
                    && relationship.transformationOperator().id() == transformationId) {
                targets.addAll(collectRepresentationTargetsFromRelationship(relationship));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForPointMarker(
            int markerId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepPointStyle pointStyle && pointStyle.marker().id() == markerId) {
                targets.addAll(collectSemanticTargets(pointStyle, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForStyleColour(
            int colourId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepFillAreaStyleColour fillAreaStyleColour
                    && fillAreaStyleColour.colour().id() == colourId) {
                targets.addAll(collectSemanticTargets(fillAreaStyleColour, resolved, visiting));
            } else if (candidate instanceof StepCurveStyle curveStyle
                    && curveStyle.colour().id() == colourId) {
                targets.addAll(collectSemanticTargets(curveStyle, resolved, visiting));
            } else if (candidate instanceof StepPointStyle pointStyle
                    && pointStyle.colour().id() == colourId) {
                targets.addAll(collectSemanticTargets(pointStyle, resolved, visiting));
            } else if (candidate instanceof StepTextStyleForDefinedFont textStyle
                    && textStyle.textColour().id() == colourId) {
                targets.addAll(collectSemanticTargets(textStyle, resolved, visiting));
            } else if (candidate instanceof StepSymbolColour symbolColour
                    && symbolColour.colour().id() == colourId) {
                targets.addAll(collectSemanticTargets(symbolColour, resolved, visiting));
            } else if (candidate instanceof StepSurfaceStyleReflectanceAmbientDiffuseSpecular style
                    && style.specularColour().id() == colourId) {
                targets.addAll(collectSemanticTargets(style, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForProductDefinition(
            int productDefinitionId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProductDefinitionShape shape
                    && shape.definition().id() == productDefinitionId) {
                targets.addAll(collectSemanticTargets(shape, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForOccurrence(
            int occurrenceId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProductDefinitionShape shape
                    && shape.definition().id() == occurrenceId) {
                targets.addAll(collectSemanticTargets(shape, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForDateRole(
            int roleId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepAppliedDateAssignment assignment
                    && assignment.role().id() == roleId) {
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForApprovalStatus(
            int statusId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepApproval approval
                    && approval.status().id() == statusId) {
                targets.addAll(collectSemanticTargets(approval, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForSecurityLevel(
            int levelId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepSecurityClassification classification
                    && classification.securityLevel().id() == levelId) {
                targets.addAll(collectSemanticTargets(classification, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForContractType(
            int kindId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepContract contract
                    && contract.kind().id() == kindId) {
                targets.addAll(collectSemanticTargets(contract, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForCertificationType(
            int kindId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepCertification certification
                    && certification.kind().id() == kindId) {
                targets.addAll(collectSemanticTargets(certification, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForApprovalRole(
            int roleId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepApprovalPersonOrganization assignment
                    && assignment.role().id() == roleId) {
                targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForOrganizationRole(
            int roleId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepOrganizationAssignment assignment
                    && assignment.role().id() == roleId) {
                targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
            } else if (candidate instanceof StepAppliedOrganizationAssignment assignment
                    && assignment.role().id() == roleId) {
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForPersonAndOrganizationRole(
            int roleId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepPersonAndOrganizationAssignment assignment
                    && assignment.role().id() == roleId) {
                targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
            } else if (candidate instanceof StepAppliedPersonAndOrganizationAssignment assignment
                    && assignment.role().id() == roleId) {
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForClassificationRole(
            int roleId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepClassificationAssignment assignment
                    && assignment.role().id() == roleId) {
                targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
            } else if (candidate instanceof StepAppliedClassificationAssignment assignment
                    && assignment.role().id() == roleId) {
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForIdentificationRole(
            int roleId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepIdentificationAssignment assignment
                    && assignment.role().id() == roleId) {
                targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
            } else if (candidate instanceof StepAppliedIdentificationAssignment assignment
                    && assignment.role().id() == roleId) {
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            } else if (candidate instanceof StepExternalIdentificationAssignment assignment
                    && assignment.role().id() == roleId) {
                targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
            } else if (candidate instanceof StepAppliedExternalIdentificationAssignment assignment
                    && assignment.role().id() == roleId) {
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForDocumentType(
            int kindId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepDocument document
                    && document.kind().id() == kindId) {
                targets.addAll(collectSemanticTargets(document, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForDateTimeRole(
            int roleId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepAppliedDateTimeAssignment assignment
                    && assignment.role().id() == roleId) {
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static CartesianPoint pointFromStep(StepCartesianPoint point) {
        double x = point.coordinates().get(0);
        double y = point.coordinates().size() > 1 ? point.coordinates().get(1) : 0.0;
        double z = point.coordinates().size() > 2 ? point.coordinates().get(2) : 0.0;
        return new CartesianPoint(x, y, z);
    }

    private static void includePmi(BoundsAccumulator bounds, List<PmiPayload> pmi) {
        for (PmiPayload item : pmi) {
            bounds.include(item.position());
            for (PointPayload point : item.leader()) {
                bounds.include(point);
            }
        }
    }

    private static ValidationReportPayload buildValidationReport(
            Map<Integer, StepEntity> resolved,
            GeometrySummary summary,
            ValidationContext context
    ) {
        List<ValidationCheckPayload> checks = new ArrayList<>();
        int okCount = 0;
        int warnCount = 0;
        for (StepEntity entity : resolved.values()) {
            if (!(entity instanceof StepMeasureRepresentationItem item)) {
                continue;
            }
            String propertyId = StepValidationMatcher.matchPropertyId(item.name(), item.measureType());
            Double actual = actualValidationValue(propertyId, summary, context);
            if (actual == null) {
                continue;
            }
            double delta = actual - item.value();
            boolean matches = Math.abs(delta) <= 1.0e-6;
            if (matches) {
                okCount++;
            } else {
                warnCount++;
            }
            checks.add(new ValidationCheckPayload(
                    propertyId,
                    item.name(),
                    item.measureType(),
                    item.value(),
                    actual,
                    delta,
                    matches ? "ok" : "warn",
                    matches
            ));
        }
        return new ValidationReportPayload(
                checks.isEmpty() ? "empty" : warnCount == 0 ? "ok" : "warn",
                okCount,
                warnCount,
                List.copyOf(checks)
        );
    }

    private static Double actualValidationValue(String propertyId, GeometrySummary summary, ValidationContext context) {
        if ("surface_area".equals(propertyId)) {
            return summary.approxSurfaceArea();
        }
        if ("edge_length".equals(propertyId)) {
            return summary.approxEdgeLength();
        }
        if ("center_x".equals(propertyId)) {
            return context.center().x();
        }
        if ("center_y".equals(propertyId)) {
            return context.center().y();
        }
        if ("center_z".equals(propertyId)) {
            return context.center().z();
        }
        if ("bbox_x".equals(propertyId)) {
            return context.sizeX();
        }
        if ("bbox_y".equals(propertyId)) {
            return context.sizeY();
        }
        if ("bbox_z".equals(propertyId)) {
            return context.sizeZ();
        }
        if ("face_count".equals(propertyId)) {
            return (double) summary.faceCount();
        }
        if ("edge_count".equals(propertyId)) {
            return (double) summary.edgeCount();
        }
        if ("representation_count".equals(propertyId)) {
            return (double) context.representationCount();
        }
        if ("instance_count".equals(propertyId)) {
            return (double) context.instanceCount();
        }
        return null;
    }

    private static PointPayload transform(PointPayload point, double[] matrix) {
        double x = point.x();
        double y = point.y();
        double z = point.z();
        return new PointPayload(
                matrix[0] * x + matrix[1] * y + matrix[2] * z + matrix[3],
                matrix[4] * x + matrix[5] * y + matrix[6] * z + matrix[7],
                matrix[8] * x + matrix[9] * y + matrix[10] * z + matrix[11]
        );
    }

    private static CartesianPoint transformCartesian(CartesianPoint point, double[] matrix) {
        double x = point.x();
        double y = point.y();
        double z = point.z();
        return new CartesianPoint(
                matrix[0] * x + matrix[1] * y + matrix[2] * z + matrix[3],
                matrix[4] * x + matrix[5] * y + matrix[6] * z + matrix[7],
                matrix[8] * x + matrix[9] * y + matrix[10] * z + matrix[11]
        );
    }

    private static VectorPayload transform(VectorPayload vector, double[] matrix) {
        double x = matrix[0] * vector.x() + matrix[1] * vector.y() + matrix[2] * vector.z();
        double y = matrix[4] * vector.x() + matrix[5] * vector.y() + matrix[6] * vector.z();
        double z = matrix[8] * vector.x() + matrix[9] * vector.y() + matrix[10] * vector.z();
        double length = Math.sqrt(x * x + y * y + z * z);
        if (length <= Epsilon.EPS) {
            return vector;
        }
        return new VectorPayload(x / length, y / length, z / length);
    }

    private static String toJson(PreviewPayload payload) {
        StringBuilder json = new StringBuilder(4096);
        json.append('{');
        json.append("\"stats\":");
        appendStats(json, payload.stats());
        json.append(",\"bounds\":");
        appendBounds(json, payload.bounds());
        json.append(",\"validation\":");
        appendValidation(json, payload.validation());
        json.append(",\"product\":");
        appendProductMetadata(json, payload.product());
        json.append(",\"units\":");
        appendUnitInfo(json, payload.units());
        json.append(",\"pmi\":");
        appendPmi(json, payload.pmi());
        json.append(",\"unsupportedBooleans\":");
        appendUnsupportedBooleans(json, payload.unsupportedBooleans());
        json.append(",\"unsupportedFaces\":");
        appendUnsupportedFaces(json, payload.unsupportedFaces());
        json.append(",\"edges\":");
        appendEdges(json, payload.edges());
        json.append(",\"faces\":");
        appendFaces(json, payload.faces());
        json.append(",\"representations\":");
        appendRepresentations(json, payload.representations());
        json.append(",\"instances\":");
        appendInstances(json, payload.instances());
        json.append('}');
        return json.toString();
    }

    private static byte[] toBinary(PreviewPayload payload) {
        BinaryGeometryBuffer geometry = new BinaryGeometryBuffer();
        BinaryPreviewPayload binaryPayload = toBinaryPayload(payload, geometry);
        byte[] metadata = toBinaryMetadataJson(binaryPayload).getBytes(StandardCharsets.UTF_8);
        int geometryOffset = alignTo4(16 + metadata.length);
        ByteArrayOutputStream output = new ByteArrayOutputStream(geometryOffset + geometry.size());
        output.writeBytes(new byte[]{'M', 'C', 'P', 'B'});
        writeIntLE(output, 1);
        writeIntLE(output, metadata.length);
        writeIntLE(output, geometryOffset);
        output.writeBytes(metadata);
        while (output.size() < geometryOffset) {
            output.write(0);
        }
        output.writeBytes(geometry.toByteArray());
        return output.toByteArray();
    }

    private static byte[] toGlb(PreviewPayload payload) {
        GlbSceneBuilder builder = new GlbSceneBuilder();
        byte[] jsonBytes = builder.buildJson(payload).getBytes(StandardCharsets.UTF_8);
        byte[] paddedJson = padChunk(jsonBytes);
        byte[] binaryChunk = builder.binaryChunk();
        byte[] paddedBinary = padChunk(binaryChunk);
        log.info("stage={} faceMeshCount={}, edgeMeshCount={}, nodeCount={}, materialCount={}, accessorCount={}, bufferViewCount={}, faceVertexCount={}, faceIndexCount={}, lineVertexCount={}, maxFaceVertexCount={}, maxFaceIndexCount={}, parametricFaceCount={}, uvLoopFaceCount={}, jsonChunkLength={}, binaryChunkLength={}",
                "glb_builder_summary",
                builder.faceMeshCount(),
                builder.edgeMeshCount(),
                builder.nodeCount(),
                builder.materialCount(),
                builder.accessorCount(),
                builder.bufferViewCount(),
                builder.faceVertexCount(),
                builder.faceIndexCount(),
                builder.lineVertexCount(),
                builder.maxFaceVertexCount(),
                builder.maxFaceIndexCount(),
                builder.parametricFaceCount(),
                builder.uvLoopFaceCount(),
                jsonBytes.length,
                binaryChunk.length);

        ByteArrayOutputStream output = new ByteArrayOutputStream(12 + 8 + paddedJson.length + 8 + paddedBinary.length);
        writeIntLE(output, 0x46546C67);
        writeIntLE(output, 2);
        writeIntLE(output, 12 + 8 + paddedJson.length + 8 + paddedBinary.length);
        writeIntLE(output, paddedJson.length);
        writeIntLE(output, 0x4E4F534A);
        output.writeBytes(paddedJson);
        writeIntLE(output, paddedBinary.length);
        writeIntLE(output, 0x004E4942);
        output.writeBytes(paddedBinary);
        return output.toByteArray();
    }

    private static byte[] padChunk(byte[] bytes) {
        int paddedLength = alignTo4(bytes.length);
        if (paddedLength == bytes.length) {
            return bytes;
        }
        byte[] padded = new byte[paddedLength];
        System.arraycopy(bytes, 0, padded, 0, bytes.length);
        for (int i = bytes.length; i < padded.length; i++) {
            padded[i] = 0x20;
        }
        return padded;
    }

    private static BinaryPreviewPayload toBinaryPayload(PreviewPayload payload, BinaryGeometryBuffer geometry) {
        return new BinaryPreviewPayload(
                payload.stats(),
                payload.bounds(),
                payload.validation(),
                payload.product(),
                payload.units(),
                payload.pmi(),
                payload.unsupportedBooleans(),
                payload.unsupportedFaces(),
                payload.edges().stream().map(edge -> toBinaryEdge(edge, geometry)).toList(),
                payload.faces().stream().map(face -> toBinaryFace(face, geometry)).toList(),
                payload.representations().stream().map(representation -> new BinaryRepresentationPayload(
                        representation.id(),
                        representation.name(),
                        representation.layers(),
                        representation.color(),
                        representation.edges().stream().map(edge -> toBinaryEdge(edge, geometry)).toList(),
                        representation.faces().stream().map(face -> toBinaryFace(face, geometry)).toList()
                )).toList(),
                payload.instances()
        );
    }

    private static BinaryEdgePayload toBinaryEdge(EdgePayload edge, BinaryGeometryBuffer geometry) {
        PointRange range = geometry.append(edge.points());
        return new BinaryEdgePayload(edge.stepId(), range.offset(), range.count(), edge.curve());
    }

    private static BinaryFacePayload toBinaryFace(FacePayload face, BinaryGeometryBuffer geometry) {
        PointRange triangles = geometry.append(face.triangles());
        List<BinaryLoopPayload> loops = face.loops().stream()
                .map(loop -> {
                    PointRange range = geometry.append(loop.points());
                    return new BinaryLoopPayload(loop.outer(), range.offset(), range.count());
                })
                .toList();
        return new BinaryFacePayload(
                face.stepId(),
                face.name(),
                face.surfaceType(),
                face.origin(),
                face.normal(),
                face.sameSense(),
                face.color(),
                face.layers(),
                face.surface(),
                face.uvLoops(),
                loops,
                triangles.offset(),
                triangles.count()
        );
    }

    private static String toBinaryMetadataJson(BinaryPreviewPayload payload) {
        StringBuilder json = new StringBuilder(4096);
        json.append('{');
        json.append("\"format\":\"binary-preview-v1\"");
        json.append(",\"pointEncoding\":\"float32-le\"");
        json.append(",\"pointStride\":3");
        json.append(",\"stats\":");
        appendStats(json, payload.stats());
        json.append(",\"bounds\":");
        appendBounds(json, payload.bounds());
        json.append(",\"validation\":");
        appendValidation(json, payload.validation());
        json.append(",\"product\":");
        appendProductMetadata(json, payload.product());
        json.append(",\"units\":");
        appendUnitInfo(json, payload.units());
        json.append(",\"pmi\":");
        appendPmi(json, payload.pmi());
        json.append(",\"unsupportedBooleans\":");
        appendUnsupportedBooleans(json, payload.unsupportedBooleans());
        json.append(",\"unsupportedFaces\":");
        appendUnsupportedFaces(json, payload.unsupportedFaces());
        json.append(",\"edges\":");
        appendBinaryEdges(json, payload.edges());
        json.append(",\"faces\":");
        appendBinaryFaces(json, payload.faces());
        json.append(",\"representations\":");
        appendBinaryRepresentations(json, payload.representations());
        json.append(",\"instances\":");
        appendInstances(json, payload.instances());
        json.append('}');
        return json.toString();
    }

    private static void appendBinaryEdges(StringBuilder json, List<BinaryEdgePayload> edges) {
        json.append('[');
        for (int i = 0; i < edges.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            BinaryEdgePayload edge = edges.get(i);
            json.append('{');
            json.append("\"id\":").append(edge.stepId());
            json.append(",\"pointOffset\":").append(edge.pointOffset());
            json.append(",\"pointCount\":").append(edge.pointCount());
            if (edge.curve() != null) {
                json.append(",\"curve\":");
                appendJsonValue(json, previewEdgeCurveMap(edge.curve()));
            }
            json.append('}');
        }
        json.append(']');
    }

    private static void appendBinaryFaces(StringBuilder json, List<BinaryFacePayload> faces) {
        json.append('[');
        for (int i = 0; i < faces.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            BinaryFacePayload face = faces.get(i);
            json.append('{');
            json.append("\"id\":").append(face.stepId());
            json.append(",\"name\":").append(quote(face.name()));
            json.append(",\"surfaceType\":").append(quote(face.surfaceType()));
            json.append(",\"origin\":");
            appendPoint(json, face.origin());
            json.append(",\"normal\":");
            appendVector(json, face.normal());
            json.append(",\"sameSense\":").append(face.sameSense());
            json.append(",\"color\":");
            appendColor(json, face.color());
            json.append(",\"layers\":");
            appendStringList(json, face.layers());
            if (face.surface() != null) {
                json.append(",\"surface\":");
                appendJsonValue(json, previewFaceSurfaceMap(face.surface()));
            }
            if (face.uvLoops() != null && !face.uvLoops().isEmpty()) {
                json.append(",\"surfaceUvLoops\":");
                appendJsonValue(json, previewUvLoopMaps(face.uvLoops()));
            }
            json.append(",\"loops\":");
            appendBinaryLoops(json, face.loops());
            json.append(",\"triangleOffset\":").append(face.triangleOffset());
            json.append(",\"triangleCount\":").append(face.triangleCount());
            json.append('}');
        }
        json.append(']');
    }

    private static Map<String, Object> previewFaceSurfaceMap(FaceSurfacePayload surface) {
        Map<String, Object> value = new LinkedHashMap<>();
        value.put("type", surface.type());
        if (surface.sourceType() != null) {
            value.put("sourceType", surface.sourceType());
        }
        if (surface.sourceStepId() != null) {
            value.put("sourceStepId", surface.sourceStepId());
        }
        if (surface.basisType() != null) {
            value.put("basisType", surface.basisType());
        }
        if (surface.basisStepId() != null) {
            value.put("basisStepId", surface.basisStepId());
        }
        if (surface.orientation() != null) {
            value.put("orientation", surface.orientation());
        }
        if (surface.offsetDistance() != null) {
            value.put("offsetDistance", surface.offsetDistance());
        }
        if (surface.trimU1() != null) {
            value.put("trimU1", surface.trimU1());
        }
        if (surface.trimU2() != null) {
            value.put("trimU2", surface.trimU2());
        }
        if (surface.trimV1() != null) {
            value.put("trimV1", surface.trimV1());
        }
        if (surface.trimV2() != null) {
            value.put("trimV2", surface.trimV2());
        }
        if (surface.implicitOuter() != null) {
            value.put("implicitOuter", surface.implicitOuter());
        }
        if (surface.transformScale() != null) {
            value.put("transformScale", surface.transformScale());
        }
        if (surface.center() != null) {
            value.put("center", surface.center());
        }
        if (surface.axis() != null) {
            value.put("axis", surface.axis());
        }
        if (surface.xDirection() != null) {
            value.put("xDirection", surface.xDirection());
        }
        value.put("radius", surface.radius());
        if (surface.minorRadius() != null) {
            value.put("minorRadius", surface.minorRadius());
        }
        if (surface.semiAngle() != null) {
            value.put("semiAngle", surface.semiAngle());
        }
        value.put("lowerHeight", surface.lowerHeight());
        value.put("upperHeight", surface.upperHeight());
        value.put("startAngle", surface.startAngle());
        value.put("sweepAngle", surface.sweepAngle());
        if (surface.uDegree() != null) {
            value.put("uDegree", surface.uDegree());
        }
        if (surface.vDegree() != null) {
            value.put("vDegree", surface.vDegree());
        }
        if (surface.controlPoints() != null) {
            value.put("controlPoints", surface.controlPoints());
        }
        if (surface.uMultiplicities() != null) {
            value.put("uMultiplicities", surface.uMultiplicities());
        }
        if (surface.vMultiplicities() != null) {
            value.put("vMultiplicities", surface.vMultiplicities());
        }
        if (surface.uKnots() != null) {
            value.put("uKnots", surface.uKnots());
        }
        if (surface.vKnots() != null) {
            value.put("vKnots", surface.vKnots());
        }
        return value;
    }

    private static List<Map<String, Object>> previewUvLoopMaps(List<ParametricLoopPayload> loops) {
        List<Map<String, Object>> values = new ArrayList<>(loops.size());
        for (ParametricLoopPayload loop : loops) {
            values.add(Map.of(
                    "outer", loop.outer(),
                    "points", loop.points().stream()
                            .map(point -> List.of(point.u(), point.v()))
                            .toList()
            ));
        }
        return List.copyOf(values);
    }

    private static void appendBinaryLoops(StringBuilder json, List<BinaryLoopPayload> loops) {
        json.append('[');
        for (int i = 0; i < loops.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            BinaryLoopPayload loop = loops.get(i);
            json.append('{');
            json.append("\"outer\":").append(loop.outer());
            json.append(",\"pointOffset\":").append(loop.pointOffset());
            json.append(",\"pointCount\":").append(loop.pointCount());
            json.append('}');
        }
        json.append(']');
    }

    private static void appendBinaryRepresentations(StringBuilder json, List<BinaryRepresentationPayload> representations) {
        json.append('[');
        for (int i = 0; i < representations.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            BinaryRepresentationPayload representation = representations.get(i);
            json.append('{');
            json.append("\"id\":").append(representation.id());
            json.append(",\"name\":").append(quote(representation.name()));
            json.append(",\"layers\":");
            appendStringList(json, representation.layers());
            json.append(",\"color\":");
            appendColor(json, representation.color());
            json.append(",\"edges\":");
            appendBinaryEdges(json, representation.edges());
            json.append(",\"faces\":");
            appendBinaryFaces(json, representation.faces());
            json.append('}');
        }
        json.append(']');
    }

    private static int alignTo4(int value) {
        int remainder = value % 4;
        return remainder == 0 ? value : value + (4 - remainder);
    }

    private static void writeIntLE(ByteArrayOutputStream output, int value) {
        output.write(value & 0xFF);
        output.write((value >>> 8) & 0xFF);
        output.write((value >>> 16) & 0xFF);
        output.write((value >>> 24) & 0xFF);
    }

    private static void writeFloatLE(ByteArrayOutputStream output, float value) {
        writeIntLE(output, Float.floatToRawIntBits(value));
    }

    private static void appendJsonValue(StringBuilder json, Object value) {
        if (value == null) {
            json.append("null");
            return;
        }
        if (value instanceof String text) {
            json.append(quote(text));
            return;
        }
        if (value instanceof Boolean || value instanceof Integer || value instanceof Long) {
            json.append(value);
            return;
        }
        if (value instanceof Float || value instanceof Double) {
            json.append(format(((Number) value).doubleValue()));
            return;
        }
        if (value instanceof Map<?, ?> map) {
            json.append('{');
            boolean first = true;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (!first) {
                    json.append(',');
                }
                first = false;
                json.append(quote(String.valueOf(entry.getKey()))).append(':');
                appendJsonValue(json, entry.getValue());
            }
            json.append('}');
            return;
        }
        if (value instanceof List<?> list) {
            json.append('[');
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) {
                    json.append(',');
                }
                appendJsonValue(json, list.get(i));
            }
            json.append(']');
            return;
        }
        throw new IllegalArgumentException("unsupported json value: " + value.getClass().getName());
    }

    private static Map<String, Object> previewMetadata(PreviewPayload payload) {
        Map<String, Object> preview = new LinkedHashMap<>();
        preview.put("stats", previewStatsMap(payload.stats()));
        preview.put("bounds", boundsMap(payload.bounds()));
        preview.put("validation", validationMap(payload.validation()));
        preview.put("pmi", pmiMaps(payload.pmi()));
        preview.put("unsupportedBooleans", unsupportedBooleanMaps(payload.unsupportedBooleans()));
        preview.put("unsupportedFaces", unsupportedFaceMaps(payload.unsupportedFaces()));
        preview.put("instances", instanceMaps(payload.instances()));
        return preview;
    }

    private static Map<String, Object> previewStatsMap(PreviewStats stats) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("entityCount", stats.entityCount());
        map.put("solidCount", stats.solidCount());
        map.put("shellCount", stats.shellCount());
        map.put("faceCount", stats.faceCount());
        map.put("edgeCount", stats.edgeCount());
        map.put("unsupportedFaceCount", stats.unsupportedFaceCount());
        map.put("unsupportedBooleanCount", stats.unsupportedBooleanCount());
        return map;
    }

    private static Map<String, Object> boundsMap(BoundsPayload bounds) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("min", pointList(bounds.min()));
        map.put("max", pointList(bounds.max()));
        return map;
    }

    private static Map<String, Object> validationMap(ValidationPayload validation) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("representationCount", validation.representationCount());
        map.put("instanceCount", validation.instanceCount());
        map.put("renderedFaceCount", validation.renderedFaceCount());
        map.put("renderedEdgeCount", validation.renderedEdgeCount());
        map.put("approxSurfaceArea", validation.approxSurfaceArea());
        map.put("approxEdgeLength", validation.approxEdgeLength());
        map.put("center", pointList(validation.center()));
        map.put("report", validationReportMap(validation.report()));
        map.put("nativeChecks", validationChecks(validation.report().checks()));
        return map;
    }

    private static Map<String, Object> validationReportMap(ValidationReportPayload report) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("status", report.status());
        map.put("okCount", report.okCount());
        map.put("warnCount", report.warnCount());
        map.put("checks", validationChecks(report.checks()));
        return map;
    }

    private static List<Map<String, Object>> validationChecks(List<ValidationCheckPayload> checks) {
        List<Map<String, Object>> list = new ArrayList<>(checks.size());
        for (ValidationCheckPayload check : checks) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("propertyId", check.propertyId());
            map.put("name", check.name());
            map.put("measureType", check.measureType());
            map.put("expected", check.expected());
            map.put("actual", check.actual());
            map.put("delta", check.delta());
            map.put("status", check.status());
            map.put("matches", check.matches());
            list.add(map);
        }
        return List.copyOf(list);
    }

    private static List<Map<String, Object>> pmiMaps(List<PmiPayload> pmi) {
        List<Map<String, Object>> list = new ArrayList<>(pmi.size());
        for (PmiPayload item : pmi) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("name", item.name());
            map.put("text", item.text());
            map.put("position", pointList(item.position()));
            map.put("leader", item.leader().stream().map(StepPreviewJsonExporter::pointList).toList());
            map.put("targetIds", item.targetIds());
            map.put("targets", item.targets().stream().map(target -> {
                Map<String, Object> targetMap = new LinkedHashMap<>();
                targetMap.put("id", target.id());
                targetMap.put("type", target.type());
                targetMap.put("name", target.name());
                targetMap.put("instanceIds", target.instanceIds());
                if (target.viaRelationshipType() != null) {
                    targetMap.put("viaRelationshipType", target.viaRelationshipType());
                }
                if (target.viaRelationshipId() != null) {
                    targetMap.put("viaRelationshipId", target.viaRelationshipId());
                }
                if (target.viaUsageType() != null) {
                    targetMap.put("viaUsageType", target.viaUsageType());
                }
                if (target.viaUsageId() != null) {
                    targetMap.put("viaUsageId", target.viaUsageId());
                }
                if (target.viaDefinitionType() != null) {
                    targetMap.put("viaDefinitionType", target.viaDefinitionType());
                }
                if (target.viaDefinitionId() != null) {
                    targetMap.put("viaDefinitionId", target.viaDefinitionId());
                }
                return targetMap;
            }).toList());
            list.add(map);
        }
        return List.copyOf(list);
    }

    private static List<Map<String, Object>> unsupportedFaceMaps(List<UnsupportedFacePayload> unsupportedFaces) {
        List<Map<String, Object>> list = new ArrayList<>(unsupportedFaces.size());
        for (UnsupportedFacePayload face : unsupportedFaces) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", face.stepId());
            map.put("name", face.name());
            map.put("surfaceType", face.surfaceType());
            map.put("reason", face.reason());
            list.add(map);
        }
        return List.copyOf(list);
    }

    private static List<Map<String, Object>> unsupportedBooleanMaps(List<UnsupportedBooleanPayload> unsupportedBooleans) {
        List<Map<String, Object>> list = new ArrayList<>(unsupportedBooleans.size());
        for (UnsupportedBooleanPayload item : unsupportedBooleans) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", item.stepId());
            map.put("name", item.name());
            map.put("type", item.type());
            map.put("reason", item.reason());
            list.add(map);
        }
        return List.copyOf(list);
    }

    private static List<Map<String, Object>> instanceMaps(List<InstancePayload> instances) {
        List<Map<String, Object>> list = new ArrayList<>(instances.size());
        for (InstancePayload instance : instances) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", instance.id());
            map.put("parentId", instance.parentId());
            map.put("productDefinitionId", instance.productDefinitionId());
            map.put("occurrenceId", instance.occurrenceId());
            map.put("representationId", instance.representationId());
            map.put("representationIds", instance.representationIds());
            map.put("label", instance.label());
            map.put("description", instance.description());
            map.put("depth", instance.depth());
            list.add(map);
        }
        return List.copyOf(list);
    }

    private static List<Double> pointList(PointPayload point) {
        return List.of(point.x(), point.y(), point.z());
    }

    private static List<Double> vectorList(VectorPayload vector) {
        return List.of(vector.x(), vector.y(), vector.z());
    }

    private static List<Double> gltfMatrix(double[] rowMajorMatrix) {
        return List.of(
                rowMajorMatrix[0], rowMajorMatrix[4], rowMajorMatrix[8], rowMajorMatrix[12],
                rowMajorMatrix[1], rowMajorMatrix[5], rowMajorMatrix[9], rowMajorMatrix[13],
                rowMajorMatrix[2], rowMajorMatrix[6], rowMajorMatrix[10], rowMajorMatrix[14],
                rowMajorMatrix[3], rowMajorMatrix[7], rowMajorMatrix[11], rowMajorMatrix[15]
        );
    }

    private static void appendStats(StringBuilder json, PreviewStats stats) {
        json.append('{');
        json.append("\"entityCount\":").append(stats.entityCount());
        json.append(",\"solidCount\":").append(stats.solidCount());
        json.append(",\"shellCount\":").append(stats.shellCount());
        json.append(",\"faceCount\":").append(stats.faceCount());
        json.append(",\"edgeCount\":").append(stats.edgeCount());
        json.append(",\"unsupportedFaceCount\":").append(stats.unsupportedFaceCount());
        json.append(",\"unsupportedBooleanCount\":").append(stats.unsupportedBooleanCount());
        json.append('}');
    }

    private static void appendBounds(StringBuilder json, BoundsPayload bounds) {
        json.append('{');
        json.append("\"min\":");
        appendPoint(json, bounds.min());
        json.append(",\"max\":");
        appendPoint(json, bounds.max());
        json.append('}');
    }

    private static void appendValidation(StringBuilder json, ValidationPayload validation) {
        json.append('{');
        json.append("\"representationCount\":").append(validation.representationCount());
        json.append(",\"instanceCount\":").append(validation.instanceCount());
        json.append(",\"renderedFaceCount\":").append(validation.renderedFaceCount());
        json.append(",\"renderedEdgeCount\":").append(validation.renderedEdgeCount());
        json.append(",\"approxSurfaceArea\":").append(format(validation.approxSurfaceArea()));
        json.append(",\"approxEdgeLength\":").append(format(validation.approxEdgeLength()));
        json.append(",\"center\":");
        appendPoint(json, validation.center());
        json.append(",\"report\":");
        appendValidationReport(json, validation.report());
        json.append(",\"nativeChecks\":");
        appendValidationChecks(json, validation.report().checks());
        json.append('}');
    }

    private static void appendValidationReport(StringBuilder json, ValidationReportPayload report) {
        json.append('{');
        json.append("\"status\":").append(quote(report.status()));
        json.append(",\"okCount\":").append(report.okCount());
        json.append(",\"warnCount\":").append(report.warnCount());
        json.append(",\"checks\":");
        appendValidationChecks(json, report.checks());
        json.append('}');
    }

    private static void appendValidationChecks(StringBuilder json, List<ValidationCheckPayload> checks) {
        json.append('[');
        for (int i = 0; i < checks.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            ValidationCheckPayload check = checks.get(i);
            json.append('{');
            json.append("\"propertyId\":").append(quote(check.propertyId()));
            json.append(',');
            json.append("\"name\":").append(quote(check.name()));
            json.append(",\"measureType\":").append(quote(check.measureType()));
            json.append(",\"expected\":").append(format(check.expected()));
            json.append(",\"actual\":").append(format(check.actual()));
            json.append(",\"delta\":").append(format(check.delta()));
            json.append(",\"status\":").append(quote(check.status()));
            json.append(",\"matches\":").append(check.matches());
            json.append('}');
        }
        json.append(']');
    }

    private static void appendProductMetadata(StringBuilder json, ProductMetadataExtractor.ProductMetadata product) {
        json.append('{');
        json.append("\"fileName\":").append(quoteNullable(product.fileName()));
        json.append(",\"fileDescription\":").append(quoteNullable(product.fileDescription()));
        json.append(",\"productName\":").append(quoteNullable(product.productName()));
        json.append(",\"productDescription\":").append(quoteNullable(product.productDescription()));
        json.append(",\"productIdentifier\":").append(quoteNullable(product.productIdentifier()));
        json.append(",\"schemas\":");
        appendStringList(json, product.schemaNames());
        json.append(",\"components\":");
        appendComponentList(json, product.components());
        json.append('}');
    }

    private static void appendUnitInfo(StringBuilder json, UnitExtractor.UnitInfo units) {
        json.append('{');
        json.append("\"lengthUnit\":").append(quoteNullable(units.lengthUnit()));
        json.append(",\"scaleToMeters\":");
        if (units.scaleToMeters() != null) {
            json.append(format(units.scaleToMeters()));
        } else {
            json.append("null");
        }
        json.append(",\"angleUnit\":").append(quoteNullable(units.angleUnit()));
        json.append('}');
    }

    private static void appendComponentList(StringBuilder json, List<ProductMetadataExtractor.ProductMetadata.ComponentInfo> list) {
        json.append('[');
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) json.append(',');
            ProductMetadataExtractor.ProductMetadata.ComponentInfo c = list.get(i);
            json.append('{');
            json.append("\"name\":").append(quoteNullable(c.name()));
            json.append(",\"identifier\":").append(quoteNullable(c.identifier()));
            json.append(",\"description\":").append(quoteNullable(c.description()));
            json.append('}');
        }
        json.append(']');
    }

    private static String quoteNullable(String s) {
        return s == null ? "null" : quote(s);
    }

    private static void appendPmi(StringBuilder json, List<PmiPayload> pmi) {
        json.append('[');
        for (int i = 0; i < pmi.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            PmiPayload item = pmi.get(i);
            json.append('{');
            json.append("\"name\":").append(quote(item.name()));
            json.append(",\"text\":").append(quote(item.text()));
            json.append(",\"position\":");
            appendPoint(json, item.position());
            json.append(",\"leader\":");
            appendPoints(json, item.leader());
            json.append(",\"targetIds\":");
            appendIntegerList(json, item.targetIds());
            json.append(",\"targets\":");
            appendPmiTargets(json, item.targets());
            json.append('}');
        }
        json.append(']');
    }

    private static void appendUnsupportedFaces(StringBuilder json, List<UnsupportedFacePayload> unsupportedFaces) {
        json.append('[');
        for (int i = 0; i < unsupportedFaces.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            UnsupportedFacePayload face = unsupportedFaces.get(i);
            json.append('{');
            json.append("\"id\":").append(face.stepId());
            json.append(",\"name\":").append(quote(face.name()));
            json.append(",\"surfaceType\":").append(quote(face.surfaceType()));
            json.append(",\"reason\":").append(quote(face.reason()));
            json.append('}');
        }
        json.append(']');
    }

    private static void appendUnsupportedBooleans(StringBuilder json, List<UnsupportedBooleanPayload> unsupportedBooleans) {
        json.append('[');
        for (int i = 0; i < unsupportedBooleans.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            UnsupportedBooleanPayload item = unsupportedBooleans.get(i);
            json.append('{');
            json.append("\"id\":").append(item.stepId());
            json.append(",\"name\":").append(quote(item.name()));
            json.append(",\"type\":").append(quote(item.type()));
            json.append(",\"reason\":").append(quote(item.reason()));
            json.append('}');
        }
        json.append(']');
    }

    private static void appendPmiTargets(StringBuilder json, List<PmiTargetPayload> targets) {
        json.append('[');
        for (int i = 0; i < targets.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            PmiTargetPayload target = targets.get(i);
            json.append('{');
            json.append("\"id\":").append(target.id());
            json.append(",\"type\":").append(quote(target.type()));
            json.append(",\"name\":").append(quote(target.name()));
            json.append(",\"instanceIds\":");
            appendQuotedList(json, target.instanceIds());
            if (target.viaRelationshipType() != null) {
                json.append(",\"viaRelationshipType\":").append(quote(target.viaRelationshipType()));
            }
            if (target.viaRelationshipId() != null) {
                json.append(",\"viaRelationshipId\":").append(target.viaRelationshipId());
            }
            if (target.viaUsageType() != null) {
                json.append(",\"viaUsageType\":").append(quote(target.viaUsageType()));
            }
            if (target.viaUsageId() != null) {
                json.append(",\"viaUsageId\":").append(target.viaUsageId());
            }
            if (target.viaDefinitionType() != null) {
                json.append(",\"viaDefinitionType\":").append(quote(target.viaDefinitionType()));
            }
            if (target.viaDefinitionId() != null) {
                json.append(",\"viaDefinitionId\":").append(target.viaDefinitionId());
            }
            json.append('}');
        }
        json.append(']');
    }

    private static void appendEdges(StringBuilder json, List<EdgePayload> edges) {
        json.append('[');
        for (int i = 0; i < edges.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            EdgePayload edge = edges.get(i);
            json.append('{');
            json.append("\"id\":").append(edge.stepId());
            json.append(",\"points\":");
            appendPoints(json, edge.points());
            if (edge.curve() != null) {
                json.append(",\"curve\":");
                appendJsonValue(json, previewEdgeCurveMap(edge.curve()));
            }
            json.append('}');
        }
        json.append(']');
    }

    private static Map<String, Object> previewEdgeCurveMap(EdgeCurvePayload curve) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("stepId", curve.stepId());
        map.put("type", curve.type());
        if (curve.basisType() != null) {
            map.put("basisType", curve.basisType());
        }
        if (curve.basisStepId() != null) {
            map.put("basisStepId", curve.basisStepId());
        }
        if (curve.center() != null) {
            map.put("center", curve.center());
        }
        if (curve.axis() != null) {
            map.put("axis", curve.axis());
        }
        if (curve.xDirection() != null) {
            map.put("xDirection", curve.xDirection());
        }
        if (curve.radius() != null) {
            map.put("radius", curve.radius());
        }
        if (curve.semiAxis1() != null) {
            map.put("semiAxis1", curve.semiAxis1());
        }
        if (curve.semiAxis2() != null) {
            map.put("semiAxis2", curve.semiAxis2());
        }
        if (curve.orientation() != null) {
            map.put("orientation", curve.orientation());
        }
        if (curve.senseAgreement() != null) {
            map.put("senseAgreement", curve.senseAgreement());
        }
        if (curve.offsetDistance() != null) {
            map.put("offsetDistance", curve.offsetDistance());
        }
        if (curve.selfIntersect() != null) {
            map.put("selfIntersect", curve.selfIntersect());
        }
        if (curve.refDirection() != null) {
            map.put("refDirection", curve.refDirection());
        }
        if (curve.transformScale() != null) {
            map.put("transformScale", curve.transformScale());
        }
        if (curve.masterRepresentation() != null) {
            map.put("masterRepresentation", curve.masterRepresentation());
        }
        if (curve.associatedSurfaceTypes() != null) {
            map.put("associatedSurfaceTypes", curve.associatedSurfaceTypes());
        }
        if (curve.associatedSurfaceStepIds() != null) {
            map.put("associatedSurfaceStepIds", curve.associatedSurfaceStepIds());
        }
        if (curve.sourceType() != null) {
            map.put("sourceType", curve.sourceType());
        }
        if (curve.sourceStepId() != null) {
            map.put("sourceStepId", curve.sourceStepId());
        }
        map.put("startAngle", curve.startAngle());
        map.put("sweepAngle", curve.sweepAngle());
        return map;
    }

    private static void appendFaces(StringBuilder json, List<FacePayload> faces) {
        json.append('[');
        for (int i = 0; i < faces.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            appendFace(json, faces.get(i));
        }
        json.append(']');
    }

    private static void appendRepresentations(StringBuilder json, List<RepresentationPayload> representations) {
        json.append('[');
        for (int i = 0; i < representations.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            RepresentationPayload representation = representations.get(i);
            json.append('{');
            json.append("\"id\":").append(representation.id());
            json.append(",\"name\":").append(quote(representation.name()));
            json.append(",\"layers\":");
            appendStringList(json, representation.layers());
            json.append(",\"color\":");
            appendColor(json, representation.color());
            json.append(",\"edges\":");
            appendEdges(json, representation.edges());
            json.append(",\"faces\":");
            appendFaces(json, representation.faces());
            json.append('}');
        }
        json.append(']');
    }

    private static void appendInstances(StringBuilder json, List<InstancePayload> instances) {
        json.append('[');
        for (int i = 0; i < instances.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            InstancePayload instance = instances.get(i);
            json.append('{');
            json.append("\"id\":").append(quote(instance.id()));
            json.append(",\"parentId\":").append(instance.parentId() == null ? "null" : quote(instance.parentId()));
            json.append(",\"productDefinitionId\":").append(instance.productDefinitionId());
            json.append(",\"occurrenceId\":").append(instance.occurrenceId() == null ? "null" : instance.occurrenceId());
            json.append(",\"representationId\":").append(instance.representationId() == null ? "null" : instance.representationId());
            json.append(",\"representationIds\":");
            appendIntegerList(json, instance.representationIds());
            json.append(",\"label\":").append(quote(instance.label()));
            json.append(",\"description\":").append(quote(instance.description()));
            json.append(",\"localMatrix\":");
            appendMatrix(json, instance.localMatrix());
            json.append(",\"matrix\":");
            appendMatrix(json, instance.worldMatrix());
            json.append(",\"depth\":").append(instance.depth());
            json.append('}');
        }
        json.append(']');
    }

    private static void appendIntegerList(StringBuilder json, List<Integer> values) {
        json.append('[');
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            json.append(values.get(i));
        }
        json.append(']');
    }

    private static void appendStringList(StringBuilder json, List<String> values) {
        appendQuotedList(json, values);
    }

    private static void appendQuotedList(StringBuilder json, List<String> values) {
        json.append('[');
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            json.append(quote(values.get(i)));
        }
        json.append(']');
    }

    private static void appendFace(StringBuilder json, FacePayload face) {
        json.append('{');
        json.append("\"id\":").append(face.stepId());
        json.append(",\"name\":").append(quote(face.name()));
        json.append(',');
        json.append("\"surfaceType\":").append(quote(face.surfaceType()));
        json.append(',');
        json.append("\"origin\":");
        appendPoint(json, face.origin());
        json.append(",\"normal\":");
        appendVector(json, face.normal());
        json.append(",\"sameSense\":").append(face.sameSense());
        json.append(",\"color\":");
        appendColor(json, face.color());
        json.append(",\"transparency\":").append(format(face.transparency()));
        if (face.pbr() != null) {
            json.append(",\"pbr\":");
            appendPbr(json, face.pbr());
        }
        json.append(",\"layers\":");
        appendStringList(json, face.layers());
        if (face.surface() != null) {
            json.append(",\"surface\":");
            appendJsonValue(json, previewFaceSurfaceMap(face.surface()));
        }
        if (face.uvLoops() != null && !face.uvLoops().isEmpty()) {
            json.append(",\"surfaceUvLoops\":");
            appendJsonValue(json, previewUvLoopMaps(face.uvLoops()));
        }
        json.append(",\"loops\":");
        appendLoops(json, face.loops());
        json.append(",\"triangles\":");
        appendPoints(json, face.triangles());
        json.append('}');
    }

    private static void appendColor(StringBuilder json, ColorPayload color) {
        if (color == null) {
            json.append("null");
            return;
        }
        json.append('[')
                .append(color.red())
                .append(',')
                .append(color.green())
                .append(',')
                .append(color.blue())
                .append(']');
    }

    private static void appendPbr(StringBuilder json, PbrPayload pbr) {
        json.append('{');
        json.append("\"diffuse\":").append(format(pbr.diffuse()));
        json.append(",\"specular\":").append(format(pbr.specular()));
        if (pbr.specularExponent() != null) {
            json.append(",\"specularExponent\":").append(format(pbr.specularExponent()));
        }
        if (pbr.specularColor() != null) {
            json.append(",\"specularColor\":[")
                    .append(pbr.specularColor()[0]).append(',')
                    .append(pbr.specularColor()[1]).append(',')
                    .append(pbr.specularColor()[2]).append(']');
        }
        json.append('}');
    }

    private static void appendLoops(StringBuilder json, List<LoopPayload> loops) {
        json.append('[');
        for (int i = 0; i < loops.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            LoopPayload loop = loops.get(i);
            json.append('{');
            json.append("\"outer\":").append(loop.outer());
            json.append(",\"points\":");
            appendPoints(json, loop.points());
            json.append('}');
        }
        json.append(']');
    }

    private static void appendPoints(StringBuilder json, List<PointPayload> points) {
        json.append('[');
        for (int i = 0; i < points.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            appendPoint(json, points.get(i));
        }
        json.append(']');
    }

    private static void appendPoint(StringBuilder json, PointPayload point) {
        json.append('[')
                .append(format(point.x()))
                .append(',')
                .append(format(point.y()))
                .append(',')
                .append(format(point.z()))
                .append(']');
    }

    private static void appendVector(StringBuilder json, VectorPayload vector) {
        json.append('[')
                .append(format(vector.x()))
                .append(',')
                .append(format(vector.y()))
                .append(',')
                .append(format(vector.z()))
                .append(']');
    }

    private static void appendMatrix(StringBuilder json, double[] matrix) {
        json.append('[');
        for (int i = 0; i < matrix.length; i++) {
            if (i > 0) {
                json.append(',');
            }
            json.append(format(matrix[i]));
        }
        json.append(']');
    }

    private static String quote(String text) {
        StringBuilder escaped = new StringBuilder(text.length() + 16);
        escaped.append('"');
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            switch (ch) {
                case '\\' -> escaped.append("\\\\");
                case '"' -> escaped.append("\\\"");
                case '\n' -> escaped.append("\\n");
                case '\r' -> escaped.append("\\r");
                case '\t' -> escaped.append("\\t");
                case '\b' -> escaped.append("\\b");
                case '\f' -> escaped.append("\\f");
                default -> {
                    if (ch < 0x20) {
                        escaped.append(String.format("\\u%04x", (int) ch));
                    } else {
                        escaped.append(ch);
                    }
                }
            }
        }
        escaped.append('"');
        return escaped.toString();
    }

    private static String format(double value) {
        return Double.toString(value);
    }

    private record PreviewPayload(
            PreviewStats stats,
            BoundsPayload bounds,
            ValidationPayload validation,
            ProductMetadataExtractor.ProductMetadata product,
            UnitExtractor.UnitInfo units,
            List<PmiPayload> pmi,
            List<UnsupportedBooleanPayload> unsupportedBooleans,
            List<UnsupportedFacePayload> unsupportedFaces,
            List<EdgePayload> edges,
            List<FacePayload> faces,
            List<RepresentationPayload> representations,
            List<InstancePayload> instances
    ) {
    }

    private record AssemblyData(
            List<RepresentationPayload> representations,
            List<InstancePayload> instances,
            List<UnsupportedFacePayload> unsupportedFaces,
            GeometrySummary summary,
            BoundsPayload bounds
    ) {
    }

    private record AssemblyMetrics(
            GeometrySummary summary,
            BoundsPayload bounds
    ) {
    }

    private record GeometryCollection(
            List<EdgePayload> edges,
            List<FacePayload> faces,
            List<UnsupportedFacePayload> unsupportedFaces
    ) {
    }

    private record RepresentationBuildResult(
            RepresentationPayload payload,
            List<UnsupportedFacePayload> unsupportedFaces
    ) {
    }

    private record PreviewStats(
            int entityCount,
            int solidCount,
            int shellCount,
            int faceCount,
            int edgeCount,
            int unsupportedFaceCount,
            int unsupportedBooleanCount
    ) {
    }

    private record BoundsPayload(PointPayload min, PointPayload max) {
    }

    private record ValidationPayload(
            int representationCount,
            int instanceCount,
            int renderedFaceCount,
            int renderedEdgeCount,
            double approxSurfaceArea,
            double approxEdgeLength,
            PointPayload center,
            ValidationReportPayload report
    ) {
    }

    private record ValidationReportPayload(
            String status,
            int okCount,
            int warnCount,
            List<ValidationCheckPayload> checks
    ) {
    }

    private record ValidationCheckPayload(
            String propertyId,
            String name,
            String measureType,
            double expected,
            double actual,
            double delta,
            String status,
            boolean matches
    ) {
    }

    private record ValidationContext(
            int representationCount,
            int instanceCount,
            PointPayload center,
            double sizeX,
            double sizeY,
            double sizeZ
    ) {
    }

    private record PmiPayload(
            String name,
            String text,
            PointPayload position,
            List<PointPayload> leader,
            List<Integer> targetIds,
            List<PmiTargetPayload> targets
    ) {
    }

    private record PmiTargetPayload(
            int id,
            String type,
            String name,
            List<String> instanceIds,
            String viaRelationshipType,
            Integer viaRelationshipId,
            String viaUsageType,
            Integer viaUsageId,
            String viaDefinitionType,
            Integer viaDefinitionId
    ) {
    }

    private record RepresentationPayload(
            int id,
            String name,
            List<String> layers,
            ColorPayload color,
            List<EdgePayload> edges,
            List<FacePayload> faces
    ) {
    }

    private record InstancePayload(
            String id,
            String parentId,
            int productDefinitionId,
            Integer occurrenceId,
            Integer representationId,
            List<Integer> representationIds,
            String label,
            String description,
            double[] localMatrix,
            double[] worldMatrix,
            int depth
    ) {
    }

    private record BinaryPreviewPayload(
            PreviewStats stats,
            BoundsPayload bounds,
            ValidationPayload validation,
            ProductMetadataExtractor.ProductMetadata product,
            UnitExtractor.UnitInfo units,
            List<PmiPayload> pmi,
            List<UnsupportedBooleanPayload> unsupportedBooleans,
            List<UnsupportedFacePayload> unsupportedFaces,
            List<BinaryEdgePayload> edges,
            List<BinaryFacePayload> faces,
            List<BinaryRepresentationPayload> representations,
            List<InstancePayload> instances
    ) {
    }

    private record BinaryRepresentationPayload(
            int id,
            String name,
            List<String> layers,
            ColorPayload color,
            List<BinaryEdgePayload> edges,
            List<BinaryFacePayload> faces
    ) {
    }

    private record BinaryEdgePayload(int stepId, int pointOffset, int pointCount, EdgeCurvePayload curve) {
    }

    private record BinaryFacePayload(
            int stepId,
            String name,
            String surfaceType,
            PointPayload origin,
            VectorPayload normal,
            boolean sameSense,
            ColorPayload color,
            List<String> layers,
            FaceSurfacePayload surface,
            List<ParametricLoopPayload> uvLoops,
            List<BinaryLoopPayload> loops,
            int triangleOffset,
            int triangleCount
    ) {
    }

    private record BinaryLoopPayload(boolean outer, int pointOffset, int pointCount) {
    }

    private record PointRange(int offset, int count) {
    }

    private interface ParametricSurfaceMapper {
        UvPoint project(CartesianPoint point, UvPoint previous);

        CartesianPoint pointAt(double u, double v);

        Vector3 normalAt(double u, double v);

        default Double uPeriod() {
            return null;
        }

        default Double vPeriod() {
            return null;
        }
    }

    private interface CurveEvaluator {
        double start();

        double end();

        CartesianPoint pointAt(double parameter);

        default Vector3 tangentAt(double parameter) {
            double span = Math.max(end() - start(), 1.0);
            double step = Math.max(span * 1.0e-4, 1.0e-5);
            double t0 = Math.max(start(), parameter - step);
            double t1 = Math.min(end(), parameter + step);
            if (t1 - t0 <= Epsilon.EPS) {
                t0 = Math.max(start(), parameter - step * 2.0);
                t1 = Math.min(end(), parameter + step * 2.0);
            }
            return pointAt(t1).subtract(pointAt(t0));
        }

        default List<CartesianPoint> sample(int segments) {
            List<CartesianPoint> points = new ArrayList<>(segments + 1);
            for (int index = 0; index <= segments; index++) {
                double parameter = start() + (end() - start()) * index / (double) segments;
                points.add(pointAt(parameter));
            }
            return List.copyOf(points);
        }
    }

    private record UvPoint(double u, double v) {
    }

    private record ParametricLoopPayload(boolean outer, List<UvPoint> points) {
    }

    private record UvBounds(double minU, double minV, double maxU, double maxV) {
        double uSpan() {
            return maxU - minU;
        }

        double vSpan() {
            return maxV - minV;
        }
    }

    private record EdgePayload(int stepId, List<PointPayload> points, EdgeCurvePayload curve) {
    }

    private record EdgeCurvePayload(
            int stepId,
            String type,
            String basisType,
            Integer basisStepId,
            List<Double> center,
            List<Double> axis,
            List<Double> xDirection,
            Double radius,
            Double semiAxis1,
            Double semiAxis2,
            Boolean orientation,
            Boolean senseAgreement,
            Double offsetDistance,
            Boolean selfIntersect,
            List<Double> refDirection,
            Double transformScale,
            String masterRepresentation,
            List<String> associatedSurfaceTypes,
            List<Integer> associatedSurfaceStepIds,
            String sourceType,
            Integer sourceStepId,
            double startAngle,
            double sweepAngle
    ) {
        private EdgeCurvePayload(
                int stepId,
                String type,
                String basisType,
                Integer basisStepId,
                List<Double> center,
                List<Double> axis,
                List<Double> xDirection,
                Double radius,
                Double semiAxis1,
                Double semiAxis2,
                double startAngle,
                double sweepAngle
        ) {
            this(stepId, type, basisType, basisStepId, center, axis, xDirection, radius, semiAxis1, semiAxis2,
                    null, null, null, null, null, null, null, null, null, null, null, startAngle, sweepAngle);
        }
    }

    private record FaceSurfacePayload(
            String type,
            List<Double> center,
            List<Double> axis,
            List<Double> xDirection,
            double radius,
            Double minorRadius,
            Double semiAngle,
            double lowerHeight,
            double upperHeight,
            double startAngle,
            double sweepAngle,
            Integer uDegree,
            Integer vDegree,
            List<List<List<Double>>> controlPoints,
            List<Integer> uMultiplicities,
            List<Integer> vMultiplicities,
            List<Double> uKnots,
            List<Double> vKnots,
            String sourceType,
            Integer sourceStepId,
            String basisType,
            Integer basisStepId,
            Boolean orientation,
            Double offsetDistance,
            Double trimU1,
            Double trimU2,
            Double trimV1,
            Double trimV2,
            Boolean implicitOuter,
            Double transformScale
    ) {
        private FaceSurfacePayload(
                String type,
                List<Double> center,
                List<Double> axis,
                List<Double> xDirection,
                double radius,
                Double minorRadius,
                Double semiAngle,
                double lowerHeight,
                double upperHeight,
                double startAngle,
                double sweepAngle,
                Integer uDegree,
                Integer vDegree,
                List<List<List<Double>>> controlPoints,
                List<Integer> uMultiplicities,
                List<Integer> vMultiplicities,
                List<Double> uKnots,
                List<Double> vKnots
        ) {
            this(type, center, axis, xDirection, radius, minorRadius, semiAngle, lowerHeight, upperHeight, startAngle, sweepAngle,
                    uDegree, vDegree, controlPoints, uMultiplicities, vMultiplicities, uKnots, vKnots,
                    null, null, null, null, null, null, null, null, null, null, null, null);
        }
    }

    private record FacePayload(
            int stepId,
            String name,
            String surfaceType,
            PointPayload origin,
            VectorPayload normal,
            boolean sameSense,
            ColorPayload color,
            double transparency,
            PbrPayload pbr,
            List<String> layers,
            List<LoopPayload> loops,
            List<PointPayload> triangles,
            FaceSurfacePayload surface,
            List<ParametricLoopPayload> uvLoops
    ) {
    }

    private record UnsupportedFacePayload(
            int stepId,
            String name,
            String surfaceType,
            String reason
    ) {
    }

    private record UnsupportedBooleanPayload(
            int stepId,
            String name,
            String type,
            String reason
    ) {
    }

    private record PreviewFaceResult(FacePayload face, UnsupportedFacePayload unsupportedFace) {
    }

    private record SurfacePatch(
            List<CartesianPoint> bottom,
            List<CartesianPoint> top,
            List<CartesianPoint> left,
            List<CartesianPoint> right
    ) {

        int uSegments() {
            return bottom.size() - 1;
        }

        int vSegments() {
            return left.size() - 1;
        }

        CartesianPoint pointAt(double u, double v) {
            CartesianPoint c0 = sample(bottom, u);
            CartesianPoint c1 = sample(top, u);
            CartesianPoint d0 = sample(left, v);
            CartesianPoint d1 = sample(right, v);
            CartesianPoint p00 = bottom.getFirst();
            CartesianPoint p10 = bottom.getLast();
            CartesianPoint p01 = top.getFirst();
            CartesianPoint p11 = top.getLast();
            return bilinearBlend(c0, c1, d0, d1, p00, p10, p01, p11, u, v);
        }

        Vector3 normalAt(double u, double v) {
            double du = Math.max(1.0 / Math.max(uSegments(), 1), 1.0e-3);
            double dv = Math.max(1.0 / Math.max(vSegments(), 1), 1.0e-3);
            CartesianPoint p = pointAt(u, v);
            CartesianPoint pu = pointAt(Math.min(1.0, u + du), v);
            CartesianPoint pv = pointAt(u, Math.min(1.0, v + dv));
            Vector3 normal = pu.subtract(p).cross(pv.subtract(p));
            if (normal.norm() <= Epsilon.EPS) {
                return new Vector3(0.0, 0.0, 1.0);
            }
            return normal.normalize().asVector();
        }

        private static CartesianPoint sample(List<CartesianPoint> polyline, double t) {
            double clamped = Math.max(0.0, Math.min(1.0, t));
            double scaled = clamped * (polyline.size() - 1);
            int low = Math.min((int) Math.floor(scaled), polyline.size() - 1);
            int high = Math.min(low + 1, polyline.size() - 1);
            double alpha = scaled - low;
            return interpolate(polyline.get(low), polyline.get(high), alpha);
        }

        private static CartesianPoint bilinearBlend(
                CartesianPoint c0,
                CartesianPoint c1,
                CartesianPoint d0,
                CartesianPoint d1,
                CartesianPoint p00,
                CartesianPoint p10,
                CartesianPoint p01,
                CartesianPoint p11,
                double u,
                double v
        ) {
            double x = (1.0 - v) * c0.x() + v * c1.x() + (1.0 - u) * d0.x() + u * d1.x()
                    - ((1.0 - u) * (1.0 - v) * p00.x() + u * (1.0 - v) * p10.x()
                    + (1.0 - u) * v * p01.x() + u * v * p11.x());
            double y = (1.0 - v) * c0.y() + v * c1.y() + (1.0 - u) * d0.y() + u * d1.y()
                    - ((1.0 - u) * (1.0 - v) * p00.y() + u * (1.0 - v) * p10.y()
                    + (1.0 - u) * v * p01.y() + u * v * p11.y());
            double z = (1.0 - v) * c0.z() + v * c1.z() + (1.0 - u) * d0.z() + u * d1.z()
                    - ((1.0 - u) * (1.0 - v) * p00.z() + u * (1.0 - v) * p10.z()
                    + (1.0 - u) * v * p01.z() + u * v * p11.z());
            return new CartesianPoint(x, y, z);
        }
    }

    private record LoopPayload(boolean outer, List<PointPayload> points) {
    }

    private record PointPayload(double x, double y, double z) {
    }

    private record VectorPayload(double x, double y, double z) {
    }

    private record ColorPayload(int red, int green, int blue) {
    }

    private record PbrPayload(double diffuse, double specular, Double specularExponent, int[] specularColor) {
    }

    private record GeometrySummary(int faceCount, int edgeCount, double approxSurfaceArea, double approxEdgeLength) {
    }

    private static final class BoundsAccumulator {
        private double minX = Double.POSITIVE_INFINITY;
        private double minY = Double.POSITIVE_INFINITY;
        private double minZ = Double.POSITIVE_INFINITY;
        private double maxX = Double.NEGATIVE_INFINITY;
        private double maxY = Double.NEGATIVE_INFINITY;
        private double maxZ = Double.NEGATIVE_INFINITY;

        void include(CartesianPoint point) {
            include(new PointPayload(point.x(), point.y(), point.z()));
        }

        void include(PointPayload point) {
            minX = Math.min(minX, point.x());
            minY = Math.min(minY, point.y());
            minZ = Math.min(minZ, point.z());
            maxX = Math.max(maxX, point.x());
            maxY = Math.max(maxY, point.y());
            maxZ = Math.max(maxZ, point.z());
        }

        boolean isEmpty() {
            return !Double.isFinite(minX);
        }

        BoundsPayload toPayload() {
            if (!Double.isFinite(minX)) {
                PointPayload zero = new PointPayload(0.0, 0.0, 0.0);
                return new BoundsPayload(zero, zero);
            }
            return new BoundsPayload(new PointPayload(minX, minY, minZ), new PointPayload(maxX, maxY, maxZ));
        }
    }

    private static long elapsedMillis(long startedAt) {
        return (System.nanoTime() - startedAt) / 1_000_000L;
    }

    private static final class BinaryGeometryBuffer {
        private final ByteArrayOutputStream output = new ByteArrayOutputStream();
        private int pointCount;

        PointRange append(List<PointPayload> points) {
            int offset = pointCount;
            for (PointPayload point : points) {
                writeFloatLE(output, (float) point.x());
                writeFloatLE(output, (float) point.y());
                writeFloatLE(output, (float) point.z());
                pointCount++;
            }
            return new PointRange(offset, points.size());
        }

        int size() {
            return output.size();
        }

        byte[] toByteArray() {
            return output.toByteArray();
        }
    }

    private static final class GlbSceneBuilder {
        private static final ColorPayload DEFAULT_FACE_COLOR = new ColorPayload(200, 122, 82);
        private static final ColorPayload DEFAULT_EDGE_COLOR = new ColorPayload(155, 133, 120);

        private final ByteArrayOutputStream binary = new ByteArrayOutputStream();
        private final List<Map<String, Object>> bufferViews = new ArrayList<>();
        private final List<Map<String, Object>> accessors = new ArrayList<>();
        private final List<Map<String, Object>> materials = new ArrayList<>();
        private final List<Map<String, Object>> meshes = new ArrayList<>();
        private final List<Map<String, Object>> nodes = new ArrayList<>();
        private final Map<String, Integer> materialCache = new LinkedHashMap<>();
        private int faceMeshCount;
        private int edgeMeshCount;
        private long faceVertexCount;
        private long faceIndexCount;
        private long lineVertexCount;
        private int maxFaceVertexCount;
        private int maxFaceIndexCount;
        private int parametricFaceCount;
        private int uvLoopFaceCount;

        String buildJson(PreviewPayload payload) {
            boolean assemblyMode = !payload.instances().isEmpty() && !payload.representations().isEmpty();
            int rootNode = addNode("MiniCADPreview", null, List.of(), Map.of("kind", "root"), null);

            if (assemblyMode) {
                Map<Integer, RepresentationMeshes> representationMeshes = new LinkedHashMap<>();
                for (RepresentationPayload representation : payload.representations()) {
                    representationMeshes.put(representation.id(), buildRepresentationMeshes(representation));
                }
                Map<String, Integer> instanceNodes = new LinkedHashMap<>();
                for (InstancePayload instance : payload.instances()) {
                    Map<String, Object> extras = new LinkedHashMap<>();
                    extras.put("kind", "instance");
                    extras.put("instanceId", instance.id());
                    extras.put("label", instance.label());
                    extras.put("description", instance.description());
                    extras.put("depth", instance.depth());
                    extras.put("representationCount", instance.representationIds().size());
                    int instanceNode = addNode(
                            instance.label() == null || instance.label().isBlank() ? instance.id() : instance.label(),
                            null,
                            new ArrayList<>(),
                            extras,
                            gltfMatrix(instance.localMatrix())
                    );
                    instanceNodes.put(instance.id(), instanceNode);
                }
                for (InstancePayload instance : payload.instances()) {
                    int parent = instance.parentId() != null && instanceNodes.containsKey(instance.parentId())
                            ? instanceNodes.get(instance.parentId())
                            : rootNode;
                    childList(nodes.get(parent)).add(instanceNodes.get(instance.id()));
                    for (Integer representationId : instance.representationIds()) {
                        RepresentationMeshes representation = representationMeshes.get(representationId);
                        if (representation == null) {
                            continue;
                        }
                        for (FaceNode faceNode : representation.faces()) {
                            childList(nodes.get(instanceNodes.get(instance.id()))).add(addNode(
                                    faceNode.name(),
                                    faceNode.meshIndex(),
                                    List.of(),
                                    instanceFaceExtras(faceNode.face(), instance, representation.name()),
                                    null
                            ));
                        }
                        for (EdgeNode edgeNode : representation.edges()) {
                            childList(nodes.get(instanceNodes.get(instance.id()))).add(addNode(
                                    edgeNode.name(),
                                    edgeNode.meshIndex(),
                                    List.of(),
                                    instanceEdgeExtras(edgeNode.edge(), instance, representation.name()),
                                    null
                            ));
                        }
                    }
                }
            } else {
                for (FacePayload face : payload.faces()) {
                    int meshIndex = addFaceMesh(face);
                    childList(nodes.get(rootNode)).add(addNode(
                            face.name(),
                            meshIndex,
                            List.of(),
                            legacyFaceExtras(face),
                            null
                    ));
                }
                for (EdgePayload edge : payload.edges()) {
                    int meshIndex = addEdgeMesh(edge, null);
                    childList(nodes.get(rootNode)).add(addNode(
                            "Edge #" + edge.stepId(),
                            meshIndex,
                            List.of(),
                            legacyEdgeExtras(edge),
                            null
                    ));
                }
            }

            Map<String, Object> scene = new LinkedHashMap<>();
            scene.put("nodes", List.of(rootNode));
            scene.put("extras", Map.of("preview", previewMetadata(payload)));

            Map<String, Object> document = new LinkedHashMap<>();
            document.put("asset", Map.of("version", "2.0", "generator", "MiniCAD"));
            document.put("scene", 0);
            document.put("scenes", List.of(scene));
            document.put("nodes", nodes);
            document.put("meshes", meshes);
            document.put("materials", materials);
            document.put("bufferViews", bufferViews);
            document.put("accessors", accessors);
            document.put("buffers", List.of(Map.of("byteLength", binary.size())));

            StringBuilder json = new StringBuilder(4096);
            appendJsonValue(json, document);
            return json.toString();
        }

        byte[] binaryChunk() {
            return binary.toByteArray();
        }

        int faceMeshCount() {
            return faceMeshCount;
        }

        int edgeMeshCount() {
            return edgeMeshCount;
        }

        int parametricFaceCount() {
            return parametricFaceCount;
        }

        int uvLoopFaceCount() {
            return uvLoopFaceCount;
        }

        int nodeCount() {
            return nodes.size();
        }

        int materialCount() {
            return materials.size();
        }

        int accessorCount() {
            return accessors.size();
        }

        int bufferViewCount() {
            return bufferViews.size();
        }

        long faceVertexCount() {
            return faceVertexCount;
        }

        long faceIndexCount() {
            return faceIndexCount;
        }

        long lineVertexCount() {
            return lineVertexCount;
        }

        int maxFaceVertexCount() {
            return maxFaceVertexCount;
        }

        int maxFaceIndexCount() {
            return maxFaceIndexCount;
        }

        private RepresentationMeshes buildRepresentationMeshes(RepresentationPayload representation) {
            List<FaceNode> faces = new ArrayList<>();
            for (FacePayload face : representation.faces()) {
                faces.add(new FaceNode(
                        face,
                        addFaceMesh(face),
                        face.name() == null || face.name().isBlank() ? "Face #" + face.stepId() : face.name()
                ));
            }
            List<EdgeNode> edges = new ArrayList<>();
            for (EdgePayload edge : representation.edges()) {
                edges.add(new EdgeNode(
                        edge,
                        addEdgeMesh(edge, representation.color()),
                        "Edge #" + edge.stepId()
                ));
            }
            return new RepresentationMeshes(representation.name(), List.copyOf(faces), List.copyOf(edges));
        }

        private int addFaceMesh(FacePayload face) {
            IndexedTriangleMesh meshData = indexedTriangleMesh(face.triangles());
            int positionAccessor = addAccessor(meshData.positions(), true);
            int normalAccessor = addAccessor(meshData.normals(), false);
            int indexAccessor = addIndexAccessor(meshData.indices());
            int materialIndex = materialIndex(face);
            Map<String, Object> primitive = new LinkedHashMap<>();
            primitive.put("attributes", Map.of(
                    "POSITION", positionAccessor,
                    "NORMAL", normalAccessor
            ));
            primitive.put("indices", indexAccessor);
            primitive.put("material", materialIndex);
            Map<String, Object> mesh = new LinkedHashMap<>();
            mesh.put("primitives", List.of(primitive));
            meshes.add(mesh);
            faceMeshCount += 1;
            faceVertexCount += meshData.positions().count();
            faceIndexCount += meshData.indices().count();
            maxFaceVertexCount = Math.max(maxFaceVertexCount, meshData.positions().count());
            maxFaceIndexCount = Math.max(maxFaceIndexCount, meshData.indices().count());
            return meshes.size() - 1;
        }

        private int addEdgeMesh(EdgePayload edge, ColorPayload color) {
            FloatArrayData positions = floatArray(edge.points());
            int positionAccessor = addAccessor(positions, true);
            int materialIndex = materialIndex(color == null ? DEFAULT_EDGE_COLOR : color, true);
            Map<String, Object> primitive = new LinkedHashMap<>();
            primitive.put("attributes", Map.of("POSITION", positionAccessor));
            primitive.put("material", materialIndex);
            primitive.put("mode", 3);
            Map<String, Object> mesh = new LinkedHashMap<>();
            mesh.put("primitives", List.of(primitive));
            meshes.add(mesh);
            edgeMeshCount += 1;
            lineVertexCount += positions.count();
            return meshes.size() - 1;
        }

        private int addAccessor(FloatArrayData data, boolean includeBounds) {
            int byteOffset = binary.size();
            for (float value : data.values()) {
                writeFloatLE(binary, value);
            }
            Map<String, Object> bufferView = new LinkedHashMap<>();
            bufferView.put("buffer", 0);
            bufferView.put("byteOffset", byteOffset);
            bufferView.put("byteLength", data.values().length * Float.BYTES);
            bufferView.put("target", 34962);
            bufferViews.add(bufferView);

            Map<String, Object> accessor = new LinkedHashMap<>();
            accessor.put("bufferView", bufferViews.size() - 1);
            accessor.put("componentType", 5126);
            accessor.put("count", data.count());
            accessor.put("type", "VEC3");
            if (includeBounds && data.min() != null && data.max() != null) {
                accessor.put("min", List.of((double) data.min()[0], (double) data.min()[1], (double) data.min()[2]));
                accessor.put("max", List.of((double) data.max()[0], (double) data.max()[1], (double) data.max()[2]));
            }
            accessors.add(accessor);
            return accessors.size() - 1;
        }

        private int addIndexAccessor(IntArrayData data) {
            int byteOffset = binary.size();
            for (int value : data.values()) {
                writeIntLE(binary, value);
            }
            Map<String, Object> bufferView = new LinkedHashMap<>();
            bufferView.put("buffer", 0);
            bufferView.put("byteOffset", byteOffset);
            bufferView.put("byteLength", data.values().length * Integer.BYTES);
            bufferView.put("target", 34963);
            bufferViews.add(bufferView);

            Map<String, Object> accessor = new LinkedHashMap<>();
            accessor.put("bufferView", bufferViews.size() - 1);
            accessor.put("componentType", 5125);
            accessor.put("count", data.count());
            accessor.put("type", "SCALAR");
            accessors.add(accessor);
            return accessors.size() - 1;
        }

        private int materialIndex(FacePayload face) {
            ColorPayload color = face.color() != null ? face.color() : DEFAULT_FACE_COLOR;
            double alpha = 1.0 - face.transparency();
            double metallic, roughness;
            if (face.pbr() != null) {
                PbrPayload pbr = face.pbr();
                metallic = Math.sqrt(pbr.specular());
                roughness = 1.0 - pbr.diffuse();
            } else {
                metallic = 0.08;
                roughness = 0.48;
            }
            long alphaRounded = Math.round(alpha * 100);
            long metallicRounded = Math.round(metallic * 100);
            long roughnessRounded = Math.round(roughness * 100);
            String key = "f:" + color.red() + "," + color.green() + "," + color.blue()
                    + ",a" + alphaRounded + ",m" + metallicRounded + ",r" + roughnessRounded;
            Integer existing = materialCache.get(key);
            if (existing != null) {
                return existing;
            }
            Map<String, Object> gltfPbr = new LinkedHashMap<>();
            gltfPbr.put("baseColorFactor", List.of(
                    color.red() / 255.0,
                    color.green() / 255.0,
                    color.blue() / 255.0,
                    alpha
            ));
            gltfPbr.put("metallicFactor", metallic);
            gltfPbr.put("roughnessFactor", roughness);
            Map<String, Object> material = new LinkedHashMap<>();
            material.put("pbrMetallicRoughness", gltfPbr);
            material.put("doubleSided", true);
            material.put("alphaMode", "BLEND");
            materials.add(material);
            int index = materials.size() - 1;
            materialCache.put(key, index);
            return index;
        }

        private int materialIndex(ColorPayload color, boolean line) {
            String key = (line ? "line:" : "face:") + color.red() + "," + color.green() + "," + color.blue();
            Integer existing = materialCache.get(key);
            if (existing != null) {
                return existing;
            }
            Map<String, Object> pbr = new LinkedHashMap<>();
            pbr.put("baseColorFactor", List.of(
                    color.red() / 255.0,
                    color.green() / 255.0,
                    color.blue() / 255.0,
                    line ? 0.72 : 0.62
            ));
            pbr.put("metallicFactor", 0.08);
            pbr.put("roughnessFactor", 0.48);
            Map<String, Object> material = new LinkedHashMap<>();
            material.put("pbrMetallicRoughness", pbr);
            material.put("doubleSided", !line);
            material.put("alphaMode", "BLEND");
            materials.add(material);
            int index = materials.size() - 1;
            materialCache.put(key, index);
            return index;
        }

        private int addNode(String name, Integer mesh, List<Integer> children, Map<String, Object> extras, List<Double> matrix) {
            Map<String, Object> node = new LinkedHashMap<>();
            if (name != null && !name.isBlank()) {
                node.put("name", name);
            }
            if (mesh != null) {
                node.put("mesh", mesh);
            }
            if (!children.isEmpty()) {
                node.put("children", new ArrayList<>(children));
            }
            if (!extras.isEmpty()) {
                node.put("extras", extras);
            }
            if (matrix != null) {
                node.put("matrix", matrix);
            }
            nodes.add(node);
            return nodes.size() - 1;
        }

        @SuppressWarnings("unchecked")
        private List<Integer> childList(Map<String, Object> node) {
            return (List<Integer>) node.computeIfAbsent("children", ignored -> new ArrayList<Integer>());
        }

        private Map<String, Object> legacyFaceExtras(FacePayload face) {
            Map<String, Object> extras = new LinkedHashMap<>();
            extras.put("kind", "face");
            extras.put("stepId", face.stepId());
            extras.put("sameSense", face.sameSense());
            if (face.surface() != null) {
                parametricFaceCount += 1;
                extras.put("surface", faceSurfaceValue(face.surface()));
                if ("plane_face".equals(face.surface().type())) {
                    extras.put("surfaceLoops", loopValues(face.loops()));
                }
            }
            if (face.uvLoops() != null && !face.uvLoops().isEmpty()) {
                uvLoopFaceCount += 1;
                extras.put("surfaceUvLoops", uvLoopValues(face.uvLoops()));
            }
            extras.put("selection", List.of(
                    List.of("类型", "面"),
                    List.of("STEP", "#" + face.stepId()),
                    List.of("名称", face.name() == null ? "" : face.name()),
                    List.of("曲面", face.surfaceType() == null ? "PLANE" : face.surfaceType()),
                    List.of("颜色", formatColorValue(face.color())),
                    List.of("透明度", face.transparency() > 0 ? String.format("%.2f", face.transparency()) : "无"),
                    List.of("图层", formatLayersValue(face.layers())),
                    List.of("边界环", String.valueOf(face.loops().size())),
                    List.of("内环", String.valueOf(face.loops().stream().filter(loop -> !loop.outer()).count())),
                    List.of("法向", formatPointValue(vectorList(face.normal())))
            ));
            return extras;
        }

        private Map<String, Object> instanceFaceExtras(FacePayload face, InstancePayload instance, String representationName) {
            Map<String, Object> extras = legacyFaceExtras(face);
            extras.put("instanceId", instance.id());
            extras.put("selection", List.of(
                    List.of("类型", (instance.label() == null || instance.label().isBlank() ? instance.id() : instance.label()) + " / 面"),
                    List.of("STEP", "#" + face.stepId()),
                    List.of("名称", face.name() == null ? "" : face.name()),
                    List.of("曲面", face.surfaceType() == null ? "PLANE" : face.surfaceType()),
                    List.of("表示", representationName == null ? "" : representationName),
                    List.of("实例", instance.id()),
                    List.of("颜色", formatColorValue(face.color())),
                    List.of("透明度", face.transparency() > 0 ? String.format("%.2f", face.transparency()) : "无"),
                    List.of("图层", formatLayersValue(face.layers())),
                    List.of("边界环", String.valueOf(face.loops().size())),
                    List.of("内环", String.valueOf(face.loops().stream().filter(loop -> !loop.outer()).count())),
                    List.of("法向", formatPointValue(vectorList(face.normal())))
            ));
            return extras;
        }

        private Map<String, Object> legacyEdgeExtras(EdgePayload edge) {
            Map<String, Object> extras = new LinkedHashMap<>();
            extras.put("kind", "edge");
            extras.put("stepId", edge.stepId());
            if (edge.curve() != null) {
                extras.put("curve", edgeCurveValue(edge.curve()));
            }
            extras.put("selection", List.of(
                    List.of("类型", "边"),
                    List.of("STEP", "#" + edge.stepId()),
                    List.of("采样点", String.valueOf(edge.points().size())),
                    List.of("线段数", String.valueOf(Math.max(0, edge.points().size() - 1))),
                    List.of("起点", formatPointValue(pointList(edge.points().getFirst()))),
                    List.of("终点", formatPointValue(pointList(edge.points().getLast())))
            ));
            return extras;
        }

        private Map<String, Object> instanceEdgeExtras(EdgePayload edge, InstancePayload instance, String representationName) {
            Map<String, Object> extras = legacyEdgeExtras(edge);
            extras.put("instanceId", instance.id());
            extras.put("selection", List.of(
                    List.of("类型", (instance.label() == null || instance.label().isBlank() ? instance.id() : instance.label()) + " / 边"),
                    List.of("STEP", "#" + edge.stepId()),
                    List.of("表示", representationName == null ? "" : representationName),
                    List.of("实例", instance.id()),
                    List.of("采样点", String.valueOf(edge.points().size())),
                    List.of("线段数", String.valueOf(Math.max(0, edge.points().size() - 1))),
                    List.of("起点", formatPointValue(pointList(edge.points().getFirst()))),
                    List.of("终点", formatPointValue(pointList(edge.points().getLast())))
            ));
            return extras;
        }

        private String formatPointValue(List<Double> point) {
            return point.stream().map(value -> String.format("%.3f", value)).collect(Collectors.joining(", "));
        }

        private String formatColorValue(ColorPayload color) {
            if (color == null) {
                return "未指定";
            }
            return "rgb(" + color.red() + ", " + color.green() + ", " + color.blue() + ")";
        }

        private String formatLayersValue(List<String> layers) {
            return layers == null || layers.isEmpty() ? "未指定" : String.join(", ", layers);
        }

        private List<Map<String, Object>> loopValues(List<LoopPayload> loops) {
            List<Map<String, Object>> values = new ArrayList<>(loops.size());
            for (LoopPayload loop : loops) {
                values.add(Map.of(
                        "outer", loop.outer(),
                        "points", loop.points().stream().map(StepPreviewJsonExporter::pointList).toList()
                ));
            }
            return values;
        }

        private Map<String, Object> faceSurfaceValue(FaceSurfacePayload surface) {
            Map<String, Object> value = new LinkedHashMap<>();
            value.put("type", surface.type());
            if (surface.sourceType() != null) {
                value.put("sourceType", surface.sourceType());
            }
            if (surface.sourceStepId() != null) {
                value.put("sourceStepId", surface.sourceStepId());
            }
            if (surface.basisType() != null) {
                value.put("basisType", surface.basisType());
            }
            if (surface.basisStepId() != null) {
                value.put("basisStepId", surface.basisStepId());
            }
            if (surface.orientation() != null) {
                value.put("orientation", surface.orientation());
            }
            if (surface.offsetDistance() != null) {
                value.put("offsetDistance", surface.offsetDistance());
            }
            if (surface.trimU1() != null) {
                value.put("trimU1", surface.trimU1());
            }
            if (surface.trimU2() != null) {
                value.put("trimU2", surface.trimU2());
            }
            if (surface.trimV1() != null) {
                value.put("trimV1", surface.trimV1());
            }
            if (surface.trimV2() != null) {
                value.put("trimV2", surface.trimV2());
            }
            if (surface.implicitOuter() != null) {
                value.put("implicitOuter", surface.implicitOuter());
            }
            if (surface.transformScale() != null) {
                value.put("transformScale", surface.transformScale());
            }
            if (surface.center() != null) {
                value.put("center", surface.center());
            }
            if (surface.axis() != null) {
                value.put("axis", surface.axis());
            }
            if (surface.xDirection() != null) {
                value.put("xDirection", surface.xDirection());
            }
            value.put("radius", surface.radius());
            if (surface.minorRadius() != null) {
                value.put("minorRadius", surface.minorRadius());
            }
            if (surface.semiAngle() != null) {
                value.put("semiAngle", surface.semiAngle());
            }
            value.put("lowerHeight", surface.lowerHeight());
            value.put("upperHeight", surface.upperHeight());
            value.put("startAngle", surface.startAngle());
            value.put("sweepAngle", surface.sweepAngle());
            if (surface.uDegree() != null) {
                value.put("uDegree", surface.uDegree());
            }
            if (surface.vDegree() != null) {
                value.put("vDegree", surface.vDegree());
            }
            if (surface.controlPoints() != null) {
                value.put("controlPoints", surface.controlPoints());
            }
            if (surface.uMultiplicities() != null) {
                value.put("uMultiplicities", surface.uMultiplicities());
            }
            if (surface.vMultiplicities() != null) {
                value.put("vMultiplicities", surface.vMultiplicities());
            }
            if (surface.uKnots() != null) {
                value.put("uKnots", surface.uKnots());
            }
            if (surface.vKnots() != null) {
                value.put("vKnots", surface.vKnots());
            }
            return value;
        }

        private List<Map<String, Object>> uvLoopValues(List<ParametricLoopPayload> loops) {
            List<Map<String, Object>> values = new ArrayList<>(loops.size());
            for (ParametricLoopPayload loop : loops) {
                values.add(Map.of(
                        "outer", loop.outer(),
                        "points", loop.points().stream()
                                .map(point -> List.of(point.u(), point.v()))
                                .toList()
                ));
            }
            return values;
        }

        private Map<String, Object> edgeCurveValue(EdgeCurvePayload curve) {
            Map<String, Object> value = new LinkedHashMap<>();
            value.put("stepId", curve.stepId());
            value.put("type", curve.type());
            if (curve.basisType() != null) {
                value.put("basisType", curve.basisType());
            }
            if (curve.basisStepId() != null) {
                value.put("basisStepId", curve.basisStepId());
            }
            value.put("center", curve.center());
            value.put("axis", curve.axis());
            value.put("xDirection", curve.xDirection());
            value.put("startAngle", curve.startAngle());
            value.put("sweepAngle", curve.sweepAngle());
            if (curve.radius() != null) {
                value.put("radius", curve.radius());
            }
            if (curve.semiAxis1() != null) {
                value.put("semiAxis1", curve.semiAxis1());
            }
            if (curve.semiAxis2() != null) {
                value.put("semiAxis2", curve.semiAxis2());
            }
            if (curve.orientation() != null) {
                value.put("orientation", curve.orientation());
            }
            if (curve.senseAgreement() != null) {
                value.put("senseAgreement", curve.senseAgreement());
            }
            if (curve.offsetDistance() != null) {
                value.put("offsetDistance", curve.offsetDistance());
            }
            if (curve.selfIntersect() != null) {
                value.put("selfIntersect", curve.selfIntersect());
            }
            if (curve.refDirection() != null) {
                value.put("refDirection", curve.refDirection());
            }
            if (curve.transformScale() != null) {
                value.put("transformScale", curve.transformScale());
            }
            if (curve.masterRepresentation() != null) {
                value.put("masterRepresentation", curve.masterRepresentation());
            }
            if (curve.associatedSurfaceTypes() != null) {
                value.put("associatedSurfaceTypes", curve.associatedSurfaceTypes());
            }
            if (curve.associatedSurfaceStepIds() != null) {
                value.put("associatedSurfaceStepIds", curve.associatedSurfaceStepIds());
            }
            if (curve.sourceType() != null) {
                value.put("sourceType", curve.sourceType());
            }
            if (curve.sourceStepId() != null) {
                value.put("sourceStepId", curve.sourceStepId());
            }
            return value;
        }

        private FloatArrayData floatArray(List<PointPayload> points) {
            float[] values = new float[points.size() * 3];
            float[] min = new float[]{Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY};
            float[] max = new float[]{Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};
            int index = 0;
            for (PointPayload point : points) {
                values[index++] = (float) point.x();
                values[index++] = (float) point.y();
                values[index++] = (float) point.z();
                min[0] = Math.min(min[0], (float) point.x());
                min[1] = Math.min(min[1], (float) point.y());
                min[2] = Math.min(min[2], (float) point.z());
                max[0] = Math.max(max[0], (float) point.x());
                max[1] = Math.max(max[1], (float) point.y());
                max[2] = Math.max(max[2], (float) point.z());
            }
            return new FloatArrayData(values, points.size(), min, max);
        }

        private FloatArrayData triangleNormals(List<PointPayload> triangles) {
            float[] values = new float[triangles.size() * 3];
            for (int i = 0; i + 2 < triangles.size(); i += 3) {
                PointPayload a = triangles.get(i);
                PointPayload b = triangles.get(i + 1);
                PointPayload c = triangles.get(i + 2);
                double abx = b.x() - a.x();
                double aby = b.y() - a.y();
                double abz = b.z() - a.z();
                double acx = c.x() - a.x();
                double acy = c.y() - a.y();
                double acz = c.z() - a.z();
                double nx = aby * acz - abz * acy;
                double ny = abz * acx - abx * acz;
                double nz = abx * acy - aby * acx;
                double norm = Math.sqrt(nx * nx + ny * ny + nz * nz);
                if (norm <= Epsilon.EPS) {
                    nx = 0.0;
                    ny = 0.0;
                    nz = 1.0;
                } else {
                    nx /= norm;
                    ny /= norm;
                    nz /= norm;
                }
                for (int vertex = 0; vertex < 3; vertex++) {
                    int base = (i + vertex) * 3;
                    values[base] = (float) nx;
                    values[base + 1] = (float) ny;
                    values[base + 2] = (float) nz;
                }
            }
            return new FloatArrayData(values, triangles.size(), null, null);
        }

        private IndexedTriangleMesh indexedTriangleMesh(List<PointPayload> triangles) {
            Map<PointPayload, Integer> indexByPoint = new LinkedHashMap<>();
            List<PointPayload> uniquePoints = new ArrayList<>();
            List<Integer> indices = new ArrayList<>(triangles.size());
            List<double[]> normalSums = new ArrayList<>();

            for (int i = 0; i + 2 < triangles.size(); i += 3) {
                PointPayload a = triangles.get(i);
                PointPayload b = triangles.get(i + 1);
                PointPayload c = triangles.get(i + 2);
                double abx = b.x() - a.x();
                double aby = b.y() - a.y();
                double abz = b.z() - a.z();
                double acx = c.x() - a.x();
                double acy = c.y() - a.y();
                double acz = c.z() - a.z();
                double nx = aby * acz - abz * acy;
                double ny = abz * acx - abx * acz;
                double nz = abx * acy - aby * acx;
                double norm = Math.sqrt(nx * nx + ny * ny + nz * nz);
                if (norm <= Epsilon.EPS) {
                    nx = 0.0;
                    ny = 0.0;
                    nz = 1.0;
                } else {
                    nx /= norm;
                    ny /= norm;
                    nz /= norm;
                }

                for (PointPayload point : List.of(a, b, c)) {
                    Integer existing = indexByPoint.get(point);
                    int index;
                    if (existing == null) {
                        index = uniquePoints.size();
                        indexByPoint.put(point, index);
                        uniquePoints.add(point);
                        normalSums.add(new double[]{0.0, 0.0, 0.0});
                    } else {
                        index = existing;
                    }
                    double[] normal = normalSums.get(index);
                    normal[0] += nx;
                    normal[1] += ny;
                    normal[2] += nz;
                    indices.add(index);
                }
            }

            float[] normalValues = new float[uniquePoints.size() * 3];
            for (int index = 0; index < uniquePoints.size(); index++) {
                double[] normal = normalSums.get(index);
                double norm = Math.sqrt(normal[0] * normal[0] + normal[1] * normal[1] + normal[2] * normal[2]);
                int base = index * 3;
                if (norm <= Epsilon.EPS) {
                    normalValues[base] = 0.0f;
                    normalValues[base + 1] = 0.0f;
                    normalValues[base + 2] = 1.0f;
                } else {
                    normalValues[base] = (float) (normal[0] / norm);
                    normalValues[base + 1] = (float) (normal[1] / norm);
                    normalValues[base + 2] = (float) (normal[2] / norm);
                }
            }

            int[] indexValues = new int[indices.size()];
            for (int index = 0; index < indices.size(); index++) {
                indexValues[index] = indices.get(index);
            }
            return new IndexedTriangleMesh(
                    floatArray(uniquePoints),
                    new FloatArrayData(normalValues, uniquePoints.size(), null, null),
                    new IntArrayData(indexValues, indexValues.length)
            );
        }
    }

    private record RepresentationMeshes(String name, List<FaceNode> faces, List<EdgeNode> edges) {
    }

    private record FaceNode(FacePayload face, int meshIndex, String name) {
    }

    private record EdgeNode(EdgePayload edge, int meshIndex, String name) {
    }

    private record FloatArrayData(float[] values, int count, float[] min, float[] max) {
    }

    private record IntArrayData(int[] values, int count) {
    }

    private record IndexedTriangleMesh(FloatArrayData positions, FloatArrayData normals, IntArrayData indices) {
    }
}
