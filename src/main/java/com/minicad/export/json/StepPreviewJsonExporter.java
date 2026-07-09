package com.minicad.export.json;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.MiniCadIssue;
import com.minicad.common.StepParseException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.TopologyException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.builder.StepAssemblyGraphBuilder;
import com.minicad.builder.StepAssemblyGraphBuilder.AssemblyGraph;
import com.minicad.builder.StepAssemblyGraphBuilder.AssemblyNode;
import com.minicad.builder.StepAssemblyGraphBuilder.AssemblyRepresentation;
import com.minicad.export.glb.PreviewMeshExporter;
import com.minicad.export.glb.TessellatedFaceExporter;
import com.minicad.helper.MathUtilityHelper;
import com.minicad.helper.ShellHelper;
import com.minicad.helper.SurfaceGeometryHelper;
import com.minicad.helper.ProductMetadataExtractor;
import com.minicad.helper.ValidationReportHelper;
import com.minicad.preview.builder.PmiTargetHelper;
import com.minicad.preview.sampling.Curve2SamplingHelper;
import com.minicad.preview.sampling.Curve3SamplingHelper;
import com.minicad.preview.sampling.ConicSamplingHelper;
import com.minicad.preview.mapper.SurfaceMapperHelper;
import com.minicad.preview.statistics.GeometryMeasurementHelper;
import com.minicad.preview.statistics.PreviewStatisticsHelper;
import com.minicad.export.glb.PreviewMaterialExporter;
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
import com.minicad.step.model.StepFillAreaWithOutline;
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
import com.minicad.step.model.StepCylinderVolume;
import com.minicad.step.model.StepExtrudedFaceSolid;
import com.minicad.step.model.StepPrismVolume;
import com.minicad.step.model.StepRevolvedFaceSolid;
import com.minicad.step.model.StepRightCircularConeVolume;
import com.minicad.step.model.StepSphereVolume;
import com.minicad.step.model.StepSweptFaceSolid;
import com.minicad.step.model.StepTorusVolume;
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
import com.minicad.step.model.StepParaboloidSurface;
import com.minicad.step.model.StepHyperboloidSurface;
import com.minicad.step.model.StepSurfaceOfTranslation;
import com.minicad.step.model.StepSurfaceOfProjection;
import com.minicad.step.model.StepTessellatedFace;
import com.minicad.step.model.StepTessellatedFaceSet;
import com.minicad.step.model.StepTessellatedTriangle;
import com.minicad.step.model.StepForwardChainingRulePremise;
import com.minicad.step.model.StepGeometricCurveSet;
import com.minicad.step.model.StepGeometricReplica;
import com.minicad.step.model.StepDatum;
import com.minicad.step.model.StepDatumFeature;
import com.minicad.step.model.StepDatumTarget;
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
import com.minicad.step.model.StepDimensionalLocation;
import com.minicad.step.model.StepDimensionCurve;
import com.minicad.step.model.StepGeometricTolerance;
import com.minicad.step.model.StepGeometricToleranceWithDatumReference;
import com.minicad.step.model.StepGeometricToleranceWithDefinedAreaUnit;
import com.minicad.step.model.StepGeometricToleranceWithMaximumTolerance;
import com.minicad.step.model.StepToleranceZone;
import com.minicad.step.model.StepToleranceZoneForm;
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
import com.minicad.step.model.StepComplexTriangulatedFace;
import com.minicad.step.model.StepCubicBezierTriangulatedFace;
import com.minicad.step.model.StepTriangulatedFace;
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
import com.minicad.step.model.StepGeometricMeasurement;
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
import com.minicad.step.model.StepToroidalSurfaceWithSpecifiedBends;
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
import com.minicad.helper.StepMetadataExtractor;
import com.minicad.helper.UnitExtractor;
import com.minicad.helper.ValidationReportHelper;
import com.minicad.builder.CompiledStepDocument;
import com.minicad.preview.payload.AssemblyData;
import com.minicad.preview.payload.AssemblyMetrics;
import com.minicad.preview.payload.BoundsPayload;
import com.minicad.preview.payload.ColorPayload;
import com.minicad.preview.payload.EdgeCurvePayload;
import com.minicad.preview.payload.EdgePayload;
import com.minicad.preview.payload.FacePayload;
import com.minicad.preview.payload.FaceSurfacePayload;
import com.minicad.preview.payload.GeometryCollection;
import com.minicad.preview.payload.GeometrySummary;
import com.minicad.preview.payload.InstancePayload;
import com.minicad.preview.payload.LoopPayload;
import com.minicad.preview.payload.ParametricLoopPayload;
import com.minicad.preview.payload.PbrPayload;
import com.minicad.preview.payload.PayloadConversionHelper;
import com.minicad.preview.payload.PayloadReductionHelper;
import com.minicad.preview.sampling.PcurveSamplingHelper;
import com.minicad.preview.sampling.TriangulationHelper;
import com.minicad.helper.MathUtilityHelper;
import com.minicad.preview.builder.PmiPayload;
import com.minicad.preview.builder.PmiTargetPayload;
import com.minicad.preview.payload.PointPayload;
import com.minicad.preview.payload.PreviewPayload;
import com.minicad.preview.payload.PreviewPayloadCopies;
import com.minicad.preview.payload.PreviewStats;
import com.minicad.preview.payload.PreviewFaceResult;
import com.minicad.preview.payload.RepresentationBuildResult;
import com.minicad.preview.payload.RepresentationPayload;
import com.minicad.preview.payload.UnsupportedBooleanPayload;
import com.minicad.preview.payload.UnsupportedFacePayload;
import com.minicad.preview.payload.SurfacePatch;
import com.minicad.preview.payload.UvBounds;
import com.minicad.preview.payload.UvPoint;
import com.minicad.preview.payload.ValidationContext;
import com.minicad.preview.payload.ValidationPayload;
import com.minicad.preview.payload.ValidationReportPayload;
import com.minicad.preview.payload.VectorPayload;
import com.minicad.preview.sampling.CurveEvaluator;
import com.minicad.preview.mapper.ParametricSurfaceMapper;
import com.minicad.export.json.PreviewSerializers.BoundsAccumulator;
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
    private static final int TOPOLOGY_SURFACE_GRID_SEGMENTS = 16;

    private StepPreviewJsonExporter() {
    }

    public static String export(String stepText) {
        long startedAt = System.nanoTime();
        log.info("stage={} textLength={}", "export_start", stepText.length());
        return export(compileForExport(stepText, "parse_done", "resolve_done"), startedAt, "export_done");
    }

    public static String export(CompiledStepDocument compiled) {
        return export(compiled, System.nanoTime(), "export_done");
    }

    private static String export(CompiledStepDocument compiled, long startedAt, String doneStageName) {
        long payloadStartedAt = System.nanoTime();
        PreviewPayload payload = buildPayload(compiled.stepFile(), compiled.resolved(), compiled.builder());
        log.info("stage={} trianglePoints={}, loopPoints={}, edgePoints={}, pmiPoints={}, representationFaceCount={}, representationEdgeCount={}",
                "payload_geometry_summary",
                PayloadReductionHelper.countTrianglePoints(payload),
                PayloadReductionHelper.countLoopPoints(payload),
                PayloadReductionHelper.countEdgePoints(payload),
                PayloadReductionHelper.countPmiPoints(payload),
                payload.representations().stream().mapToInt(representation -> representation.faces().size()).sum(),
                payload.representations().stream().mapToInt(representation -> representation.edges().size()).sum());
        log.info("stage={} elapsedMs={}, faces={}, edges={}, unsupportedFaces={}, representations={}, instances={}", "payload_done",
                SerializationHelper.elapsedMillis(payloadStartedAt),
                        payload.faces().size(),
                        payload.edges().size(),
                        payload.unsupportedFaces().size(),
                        payload.representations().size(),
                        payload.instances().size());
        long jsonStartedAt = System.nanoTime();
        String json = SerializationHelper.toJson(payload);
        log.info("stage={} elapsedMs={}, jsonLength={}", "json_done", SerializationHelper.elapsedMillis(jsonStartedAt), json.length());
        log.info("stage={} totalElapsedMs={}", doneStageName, SerializationHelper.elapsedMillis(startedAt));
        return json;
    }

    public static byte[] exportBinary(String stepText) {
        long startedAt = System.nanoTime();
        log.info("stage={} textLength={}", "binary_export_start", stepText.length());
        return exportBinary(compileForExport(stepText, "binary_parse_done", "binary_resolve_done"), startedAt, "binary_export_done");
    }

    static byte[] exportBinary(CompiledStepDocument compiled) {
        return exportBinary(compiled, System.nanoTime(), "binary_export_done");
    }

    private static byte[] exportBinary(CompiledStepDocument compiled, long startedAt, String doneStageName) {
        long payloadStartedAt = System.nanoTime();
        PreviewPayload payload = PayloadReductionHelper.reducePayloadGeometry(buildPayload(compiled.stepFile(), compiled.resolved(), compiled.builder()));
        log.info("stage={} trianglePoints={}, loopPoints={}, edgePoints={}, pmiPoints={}, representationFaceCount={}, representationEdgeCount={}",
                "binary_payload_geometry_summary",
                PayloadReductionHelper.countTrianglePoints(payload),
                PayloadReductionHelper.countLoopPoints(payload),
                PayloadReductionHelper.countEdgePoints(payload),
                PayloadReductionHelper.countPmiPoints(payload),
                payload.representations().stream().mapToInt(representation -> representation.faces().size()).sum(),
                payload.representations().stream().mapToInt(representation -> representation.edges().size()).sum());
        log.info("stage={} elapsedMs={}, faces={}, edges={}, unsupportedFaces={}, representations={}, instances={}", "binary_payload_done",
                SerializationHelper.elapsedMillis(payloadStartedAt),
                payload.faces().size(),
                payload.edges().size(),
                payload.unsupportedFaces().size(),
                payload.representations().size(),
                payload.instances().size());
        long binaryStartedAt = System.nanoTime();
        byte[] binary = SerializationHelper.toBinary(payload);
        log.info("stage={} elapsedMs={}, binaryLength={}", "binary_encode_done", SerializationHelper.elapsedMillis(binaryStartedAt), binary.length);
        log.info("stage={} totalElapsedMs={}", doneStageName, SerializationHelper.elapsedMillis(startedAt));
        return binary;
    }

    public static byte[] exportGlb(String stepText) {
        long startedAt = System.nanoTime();
        log.info("stage={} textLength={}", "glb_export_start", stepText.length());
        return exportGlb(compileForExport(stepText, "glb_parse_done", "glb_resolve_done"), startedAt, "glb_export_done");
    }

    public static byte[] exportGlb(CompiledStepDocument compiled) {
        return exportGlb(compiled, System.nanoTime(), "glb_export_done");
    }

    private static byte[] exportGlb(CompiledStepDocument compiled, long startedAt, String doneStageName) {
        long payloadStartedAt = System.nanoTime();
        PreviewPayload payload = PayloadReductionHelper.reducePayloadGeometry(
                buildPayload(compiled.stepFile(), compiled.resolved(), compiled.builder()),
                GLB_MAX_TOTAL_TRIANGLE_POINTS,
                MAX_TOTAL_LOOP_POINTS,
                "glb_payload_geometry_reduced"
        );
        log.info("stage={} trianglePoints={}, loopPoints={}, edgePoints={}, pmiPoints={}, representationFaceCount={}, representationEdgeCount={}",
                "glb_payload_geometry_summary",
                PayloadReductionHelper.countTrianglePoints(payload),
                PayloadReductionHelper.countLoopPoints(payload),
                PayloadReductionHelper.countEdgePoints(payload),
                PayloadReductionHelper.countPmiPoints(payload),
                payload.representations().stream().mapToInt(representation -> representation.faces().size()).sum(),
                payload.representations().stream().mapToInt(representation -> representation.edges().size()).sum());
        log.info("stage={} elapsedMs={}, faces={}, edges={}, unsupportedFaces={}, representations={}, instances={}", "glb_payload_done",
                SerializationHelper.elapsedMillis(payloadStartedAt),
                payload.faces().size(),
                payload.edges().size(),
                payload.unsupportedFaces().size(),
                payload.representations().size(),
                payload.instances().size());
        long glbStartedAt = System.nanoTime();
        byte[] glb = SerializationHelper.toGlb(payload);
        PreviewSerializers.validateGlb(glb);
        log.info("stage={} elapsedMs={}, glbLength={}", "glb_encode_done", SerializationHelper.elapsedMillis(glbStartedAt), glb.length);
        log.info("stage={} totalElapsedMs={}", doneStageName, SerializationHelper.elapsedMillis(startedAt));
        return glb;
    }

    private static CompiledStepDocument compileForExport(String stepText, String parseStageName, String resolveStageName) {
        long parseStartedAt = System.nanoTime();
        StepFile stepFile = StepParser.parse(stepText);
        log.info("stage={} elapsedMs={}, entityCount={}", parseStageName, SerializationHelper.elapsedMillis(parseStartedAt), stepFile.entities().size());
        long resolveStartedAt = System.nanoTime();
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(stepFile);
        log.info("stage={} elapsedMs={}, resolvedCount={}", resolveStageName, SerializationHelper.elapsedMillis(resolveStartedAt), resolved.size());
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);
        return new CompiledStepDocument(stepText, stepFile, resolved, builder);
    }

    private static PreviewPayload buildPayload(
            StepFile stepFile,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder
    ) {
        long metadataStartedAt = System.nanoTime();
        StepMetadataExtractor metadata = StepMetadataExtractor.fromResolved(resolved);
        log.debug("stage={} elapsedMs={}", "metadata_done", SerializationHelper.elapsedMillis(metadataStartedAt));
        ProductMetadataExtractor.ProductMetadata productInfo = ProductMetadataExtractor.extract(stepFile, resolved);
        UnitExtractor.UnitInfo units = UnitExtractor.extract(resolved);
        long assemblyStartedAt = System.nanoTime();
        AssemblyData assembly = buildAssemblyData(resolved, builder, metadata, units);
        log.info("stage={} elapsedMs={}, representations={}, instances={}, unsupportedFaces={}", "assembly_done",
                SerializationHelper.elapsedMillis(assemblyStartedAt),
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
                    SerializationHelper.elapsedMillis(legacyStartedAt),
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
        ValidationReportHelper.includePmi(bounds, pmi);
        ValidationPayload validation = buildValidationPayload(legacyGeometry, assembly, geometryBounds, resolved);
        List<UnsupportedFacePayload> unsupportedFaces = assemblyMode
                ? assembly.unsupportedFaces()
                : legacyGeometry.unsupportedFaces();
        List<UnsupportedBooleanPayload> unsupportedBooleans = collectUnsupportedBooleans(resolved);
        int faceCount = assemblyMode ? assembly.summary().faceCount() : legacyGeometry.faces().size();
        int edgeCount = assemblyMode ? assembly.summary().edgeCount() : legacyGeometry.edges().size();

        PreviewStats stats = new PreviewStats(
                stepFile.entities().size(),
                PreviewStatisticsHelper.countSolidEntities(resolved),
                PreviewStatisticsHelper.countShells(resolved),
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
                    PreviewStatisticsHelper.summarizeUnsupportedFacesBySurfaceType(unsupportedFaces),
                            PreviewStatisticsHelper.summarizeUnsupportedFacesByReason(unsupportedFaces));
        }
        if (!unsupportedBooleans.isEmpty()) {
            log.debug("stage={} byType={}, byReason={}", "unsupported_booleans_summary",
                    PreviewStatisticsHelper.summarizeUnsupportedBooleansByType(unsupportedBooleans),
                    PreviewStatisticsHelper.summarizeUnsupportedBooleansByReason(unsupportedBooleans));
        }

        return new PreviewPayload(
                stats,
                bounds.toPayload(),
                validation,
                productInfo,
                units,
                pmi,
                previewIssues(units, unsupportedBooleans, unsupportedFaces),
                unsupportedBooleans,
                unsupportedFaces,
                legacyGeometry.edges(),
                legacyGeometry.faces(),
                assembly.representations(),
                assembly.instances()
        );
    }

    private static List<MiniCadIssue> previewIssues(
            UnitExtractor.UnitInfo units,
            List<UnsupportedBooleanPayload> unsupportedBooleans,
            List<UnsupportedFacePayload> unsupportedFaces
    ) {
        List<MiniCadIssue> issues = new ArrayList<>(unsupportedBooleans.size() + unsupportedFaces.size() + 1);
        if (units != null && units.scaleToMeters() != null
                && Math.abs(units.scaleToMeters() - 1.0) > 1.0e-12) {
            issues.add(MiniCadIssue.warning(
                    "units.coordinates_not_normalized",
                    null,
                    null,
                    "geometry coordinates are emitted in source STEP units; assembly transforms are scaled to meters"));
        }
        for (UnsupportedBooleanPayload item : unsupportedBooleans) {
            issues.add(MiniCadIssue.unsupported(item.stepId(), item.name(), item.reason()));
        }
        for (UnsupportedFacePayload face : unsupportedFaces) {
            issues.add(MiniCadIssue.unsupported(face.stepId(), face.name(), face.reason()));
        }
        return List.copyOf(issues);
    }

    private static List<UnsupportedBooleanPayload> collectUnsupportedBooleans(Map<Integer, StepEntity> resolved) {
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);
        List<UnsupportedBooleanPayload> list = new ArrayList<>();
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepBooleanClippingResult) {
            StepBooleanClippingResult clippingResult = (StepBooleanClippingResult) entity;
                String reason = unsupportedBooleanReason(builder, clippingResult.id());
                if (reason != null) {
                    list.add(new UnsupportedBooleanPayload(
                            clippingResult.id(),
                            clippingResult.name(),
                            "BOOLEAN_CLIPPING_RESULT",
                            reason
                    ));
                }
            } else if (entity instanceof StepBooleanResult) {
            StepBooleanResult booleanResult = (StepBooleanResult) entity;
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
                    || entity instanceof StepSurfacePatch
                    || entity instanceof StepExtrudedFaceSolid
                    || entity instanceof StepRevolvedFaceSolid
                    || entity instanceof StepSweptFaceSolid
                    || entity instanceof StepCylinderVolume
                    || entity instanceof StepSphereVolume
                    || entity instanceof StepTorusVolume
                    || entity instanceof StepPrismVolume
                    || entity instanceof StepRightCircularConeVolume
                    || entity instanceof StepTessellatedFace
                    || entity instanceof StepTessellatedFaceSet
                    || entity instanceof StepTriangulatedFace
                    || entity instanceof StepComplexTriangulatedFace
                    || entity instanceof StepCubicBezierTriangulatedFace) {
                solidIds.add(entity.id());
            }
            if (isStandaloneEdgeSource(entity)) {
                collectStandaloneEdges(entity, standaloneEdges, resolved, builder, metadata);
            }
        }
        // Remove shells that are referenced by B-rep solids to avoid duplicate processing
        for (Integer solidId : solidIds) {
            StepEntity solidEntity = resolved.get(solidId);
            if (solidEntity instanceof StepManifoldSolidBrep) {
            StepManifoldSolidBrep brep = (StepManifoldSolidBrep) solidEntity;
                shellIds.remove(brep.outer().id());
            } else if (solidEntity instanceof StepFacettedBrep) {
            StepFacettedBrep brep = (StepFacettedBrep) solidEntity;
                shellIds.remove(brep.outer().id());
            } else if (solidEntity instanceof StepNonManifoldSolidBrep) {
            StepNonManifoldSolidBrep brep = (StepNonManifoldSolidBrep) solidEntity;
                shellIds.remove(brep.outer().id());
            } else if (solidEntity instanceof StepAdvancedBrep) {
            StepAdvancedBrep brep = (StepAdvancedBrep) solidEntity;
                shellIds.remove(brep.outer().id());
                for (StepEntity voidShell : brep.voids()) {
                    shellIds.remove(voidShell.id());
                }
            } else if (solidEntity instanceof StepBrepWithVoids) {
            StepBrepWithVoids brep = (StepBrepWithVoids) solidEntity;
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
            if (shellEntity instanceof StepTessellatedFaceSet) {
            StepTessellatedFaceSet tessellated = (StepTessellatedFaceSet) shellEntity;
                List<FacePayload> tessFaces = TessellatedFaceExporter.buildTessellatedFacePayloads(tessellated, metadata.forItem(shellId));
                faces.addAll(tessFaces);
                log.debug("stage={} shellId={}, tessellatedFaceCount={}", "geometry_tessellated_shell", shellId, tessFaces.size());
                continue;
            }
            if (shellEntity instanceof StepTessellatedFace) {
            StepTessellatedFace tessellatedFace = (StepTessellatedFace) shellEntity;
                FacePayload payload = TessellatedFaceExporter.buildTessellatedFacePayload(tessellatedFace, metadata.forItem(shellId));
                if (payload != null) {
                    faces.add(payload);
                }
                log.debug("stage={} shellId={}, tessellatedFaceBuilt={}", "geometry_tessellated_face", shellId, payload != null);
                continue;
            }
            List<StepFaceEntity> shellFaces = ShellHelper.shellFaces(shellEntity);
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
                    if (bound.loop() instanceof com.minicad.step.model.StepEdgeLoop) {
                        com.minicad.step.model.StepEdgeLoop edgeLoop = (com.minicad.step.model.StepEdgeLoop) bound.loop();
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
            edges.add(buildEdgePayload(edgeId, resolved, builder, metadata));
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
                    faces.add(PreviewMeshExporter.facePayloadFromTopologyFace(
                            solidId * 1000 + faceIndex++,
                            face,
                            baseName,
                            itemMetadata
                    ));
                    collectTopologyEdges(face, uniqueEdges);
                }
                for (var voidShell : solid.voidShells()) {
                    for (Face face : voidShell.faces()) {
                        faces.add(PreviewMeshExporter.facePayloadFromTopologyFace(
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
                        geometryTypeName(entity),
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
        if (item instanceof StepStyledItem) {
            StepStyledItem styledItem = (StepStyledItem) item;
            collectShellLikeIds(styledItem.item(), shellIds);
            return;
        }
        if (item instanceof StepOverRidingStyledItem) {
            StepOverRidingStyledItem styledItem = (StepOverRidingStyledItem) item;
            collectShellLikeIds(styledItem.item(), shellIds);
            return;
        }
        if (ShellHelper.isShellLikeEntity(item)) {
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
        if (item instanceof StepShellBasedSurfaceModel) {
            StepShellBasedSurfaceModel surfaceModel = (StepShellBasedSurfaceModel) item;
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
        if (item instanceof StepManifoldSurfaceModel) {
            StepManifoldSurfaceModel manifoldModel = (StepManifoldSurfaceModel) item;
            for (StepEntity shell : manifoldModel.shells()) {
                collectShellLikeIds(shell, shellIds);
            }
            return;
        }
        if (item instanceof StepFaceBasedSurfaceModel) {
            StepFaceBasedSurfaceModel faceModel = (StepFaceBasedSurfaceModel) item;
            for (StepEntity faceSet : faceModel.faceSets()) {
                collectShellLikeIds(faceSet, shellIds);
            }
        }
    }

    private static void collectStandaloneEdges(
            StepEntity item,
            Map<Integer, EdgePayload> edges,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata
    ) {
        if (item instanceof StepStyledItem) {
            StepStyledItem styledItem = (StepStyledItem) item;
            collectStandaloneEdges(styledItem.item(), edges, resolved, builder, metadata);
            return;
        }
        if (item instanceof StepOverRidingStyledItem) {
            StepOverRidingStyledItem styledItem = (StepOverRidingStyledItem) item;
            collectStandaloneEdges(styledItem.item(), edges, resolved, builder, metadata);
            return;
        }
        if (item instanceof StepPolyline) {
            StepPolyline polyline = (StepPolyline) item;
            edges.putIfAbsent(polyline.id(), toPolylineEdgePayload(polyline));
            return;
        }
        if (item instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) item;
            for (StepEntity element : curveSet.elements()) {
                collectStandaloneEdges(element, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) item;
            for (StepEntity element : geometricSet.elements()) {
                collectStandaloneEdges(element, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepShellBasedWireframeModel) {
            StepShellBasedWireframeModel wireframeModel = (StepShellBasedWireframeModel) item;
            for (StepEntity boundary : wireframeModel.boundaries()) {
                collectStandaloneEdges(boundary, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepEdgeBasedWireframeModel) {
            StepEdgeBasedWireframeModel wireframeModel = (StepEdgeBasedWireframeModel) item;
            for (StepConnectedEdgeSet boundary : wireframeModel.boundaries()) {
                collectStandaloneEdges(boundary, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepConnectedEdgeSet) {
            StepConnectedEdgeSet connectedEdgeSet = (StepConnectedEdgeSet) item;
            for (StepEntity edge : connectedEdgeSet.edges()) {
                collectStandaloneEdges(edge, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepEdgeCurve) {
            StepEdgeCurve edgeCurve = (StepEdgeCurve) item;
            edges.putIfAbsent(edgeCurve.id(), buildEdgePayload(edgeCurve.id(), resolved, builder, metadata));
            return;
        }
        if (item instanceof StepFilletEdge) {
            StepFilletEdge filletEdge = (StepFilletEdge) item;
            edges.putIfAbsent(filletEdge.id(), buildEdgePayload(filletEdge.id(), resolved, builder, metadata));
            return;
        }
        if (item instanceof StepChamferEdge) {
            StepChamferEdge chamferEdge = (StepChamferEdge) item;
            edges.putIfAbsent(chamferEdge.id(), buildEdgePayload(chamferEdge.id(), resolved, builder, metadata));
            return;
        }
        if (item instanceof StepPath) {
            StepPath path = (StepPath) item;
            for (StepOrientedEdge orientedEdge : path.edges()) {
                edges.putIfAbsent(orientedEdge.edgeElement().id(), buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }
        if (item instanceof StepOpenPath) {
            StepOpenPath path = (StepOpenPath) item;
            for (StepOrientedEdge orientedEdge : path.edges()) {
                edges.putIfAbsent(orientedEdge.edgeElement().id(), buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }
        if (item instanceof StepSubpath) {
            StepSubpath subpath = (StepSubpath) item;
            for (StepOrientedEdge orientedEdge : subpath.edges()) {
                edges.putIfAbsent(orientedEdge.edgeElement().id(), buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }
        if (item instanceof StepOrientedPath) {
            StepOrientedPath orientedPath = (StepOrientedPath) item;
            for (StepOrientedEdge orientedEdge : orientedPath.edges()) {
                edges.putIfAbsent(orientedEdge.edgeElement().id(), buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }
        if (item instanceof StepWireShell) {
            StepWireShell wireShell = (StepWireShell) item;
            for (StepEntity loop : wireShell.loops()) {
                collectStandaloneEdges(loop, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepEdgeWire) {
            StepEdgeWire edgeWire = (StepEdgeWire) item;
            for (StepEntity edge : edgeWire.edges()) {
                collectStandaloneEdges(edge, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepGeometricSurfaceSet) {
            StepGeometricSurfaceSet surfaceSet = (StepGeometricSurfaceSet) item;
            for (StepEntity element : surfaceSet.elements()) {
                collectStandaloneEdges(element, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepEdgeLoop) {
            StepEdgeLoop edgeLoop = (StepEdgeLoop) item;
            for (StepOrientedEdge orientedEdge : edgeLoop.edges()) {
                edges.putIfAbsent(orientedEdge.edgeElement().id(), buildEdgePayload(orientedEdge.edgeElement().id(), resolved, builder, metadata));
            }
            return;
        }
        if (item instanceof StepPolyLoop) {
            StepPolyLoop polyLoop = (StepPolyLoop) item;
            edges.putIfAbsent(polyLoop.id(), toPolyLoopEdgePayload(polyLoop));
            return;
        }
        if (item instanceof StepVertexShell || item instanceof com.minicad.step.model.StepVertexLoop) {
            return;
        }
        if (item instanceof StepAnnotationCurveOccurrence) {
            StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) item;
            collectStandaloneEdges(occurrence.item(), edges, resolved, builder, metadata);
            return;
        }
        if (item instanceof StepAnnotationFillArea) {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) item;
            for (StepEntity boundary : fillArea.boundaries()) {
                collectStandaloneEdges(boundary, edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepAnnotationFillAreaOccurrence) {
            StepAnnotationFillAreaOccurrence fillAreaOccurrence = (StepAnnotationFillAreaOccurrence) item;
            collectStandaloneEdges(fillAreaOccurrence.item(), edges, resolved, builder, metadata);
            return;
        }
        if (item instanceof StepAnnotationSymbol) {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) item;
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
        if (item instanceof StepAnnotationSymbolOccurrence) {
            StepAnnotationSymbolOccurrence symbolOccurrence = (StepAnnotationSymbolOccurrence) item;
            if (!collectMappedAnnotationCarrierEdges(
                    symbolOccurrence.id(),
                    "ANNOTATION_SYMBOL_OCCURRENCE",
                    symbolOccurrence.id(),
                    symbolOccurrence.item(),
                    edges,
                    resolved,
                    builder
            )) {
                collectStandaloneEdges(symbolOccurrence.item(), edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepAnnotationSubfigureOccurrence) {
            StepAnnotationSubfigureOccurrence subfigureOccurrence = (StepAnnotationSubfigureOccurrence) item;
            if (!collectMappedAnnotationCarrierEdges(
                    subfigureOccurrence.id(),
                    "ANNOTATION_SUBFIGURE_OCCURRENCE",
                    subfigureOccurrence.id(),
                    subfigureOccurrence.item(),
                    edges,
                    resolved,
                    builder
            )) {
                collectStandaloneEdges(subfigureOccurrence.item(), edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepAnnotationText) {
            StepAnnotationText annotationText = (StepAnnotationText) item;
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
        if (item instanceof StepAnnotationTextCharacter) {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) item;
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
        if (item instanceof StepDimensionCurve) {
            StepDimensionCurve dimensionCurve = (StepDimensionCurve) item;
            EdgePayload sampled = sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
                collectStandaloneEdges(dimensionCurve.item(), edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepLeaderCurve) {
            StepLeaderCurve leaderCurve = (StepLeaderCurve) item;
            EdgePayload sampled = sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
                collectStandaloneEdges(leaderCurve.item(), edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepProjectionCurve) {
            StepProjectionCurve projectionCurve = (StepProjectionCurve) item;
            EdgePayload sampled = sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
                collectStandaloneEdges(projectionCurve.item(), edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepDraughtingAnnotationOccurrence) {
            StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) item;
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
                collectStandaloneEdges(annotationOccurrence.item(), edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepTerminatorSymbol) {
            StepTerminatorSymbol terminatorSymbol = (StepTerminatorSymbol) item;
            EdgePayload sampled = sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
                collectStandaloneEdges(terminatorSymbol.annotatedCurve(), edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepAnnotationCurveOccurrence) {
            StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) item;
            EdgePayload sampled = sampledCurveEdgePayload(item, builder);
            if (sampled != null) {
                edges.putIfAbsent(sampled.stepId(), sampled);
            } else {
                collectStandaloneEdges(occurrence.item(), edges, resolved, builder, metadata);
            }
            return;
        }
        if (item instanceof StepFilletEdge) {
            StepFilletEdge filletEdge = (StepFilletEdge) item;
            collectStandaloneEdges(filletEdge.originalEdge(), edges, resolved, builder, metadata);
            return;
        }
        if (item instanceof StepChamferEdge) {
            StepChamferEdge chamferEdge = (StepChamferEdge) item;
            collectStandaloneEdges(chamferEdge.originalEdge(), edges, resolved, builder, metadata);
            return;
        }
        if (item instanceof StepSubedge) {
            StepSubedge subedge = (StepSubedge) item;
            collectStandaloneEdges(subedge.parentEdge(), edges, resolved, builder, metadata);
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
                || (item instanceof StepGeometricReplica && "CURVE_REPLICA".equals(((StepGeometricReplica) item).entityName()));
    }

    private static StepEntity unwrapStyledItem(StepEntity item) {
        StepEntity current = item;
        while (true) {
            if (current instanceof StepStyledItem) {
            StepStyledItem styledItem = (StepStyledItem) current;
                current = styledItem.item();
                continue;
            }
            if (current instanceof StepOverRidingStyledItem) {
            StepOverRidingStyledItem styledItem = (StepOverRidingStyledItem) current;
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
        if (item instanceof StepAnnotationSymbol) {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) item;
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
        if (item instanceof StepAnnotationText) {
            StepAnnotationText annotationText = (StepAnnotationText) item;
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
        if (item instanceof StepAnnotationTextCharacter) {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) item;
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
        if (item instanceof StepAnnotationSymbolOccurrence) {
            StepAnnotationSymbolOccurrence symbolOccurrence = (StepAnnotationSymbolOccurrence) item;
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
        if (item instanceof StepAnnotationSubfigureOccurrence) {
            StepAnnotationSubfigureOccurrence subfigureOccurrence = (StepAnnotationSubfigureOccurrence) item;
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

    public static double[] matrixForMappedPlacement(
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

    public static double[] matrixForPlacementEntity(StepEntity placement, StepCadBuilder builder) {
        if (placement instanceof StepAxis2Placement3D) {
            StepAxis2Placement3D placement3D = (StepAxis2Placement3D) placement;
            return StepAssemblyGraphBuilder.matrixForPlacement(placement3D);
        }
        if (placement instanceof StepAxis2Placement2D) {
            StepAxis2Placement2D placement2D = (StepAxis2Placement2D) placement;
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

    public static PreviewFaceResult buildPreviewFaceResult(
            StepFaceEntity stepFace,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        if (stepFace instanceof StepOrientedFace) {
            StepOrientedFace orientedFace = (StepOrientedFace) stepFace;
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
                    FacePayload payload = PreviewMeshExporter.facePayloadFromTopologyFace(stepFace.id(), builder.buildFace(stepFace.id()), faceDisplayName(stepFace), metadata);
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
        if (previewGeometry instanceof StepCylindricalSurface) {
            StepCylindricalSurface cylindricalSurface = (StepCylindricalSurface) previewGeometry;
            try {
                if (geometry instanceof StepCylindricalSurface) {
                    FacePayload payload = toCylindricalFacePayload(stepFace, cylindricalSurface, builder, metadata);
                    if (payload != null) {
                        logPreviewFacePayload("face_payload_built", payload);
                        return new PreviewFaceResult(payload, null);
                    }
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                // C03: No silent geometry loss - log and return unsupported face payload
                return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "cylindrical face build failed: " + ex.getMessage()));
            }
        }
        if (previewGeometry instanceof StepConicalSurface) {
            StepConicalSurface conicalSurface = (StepConicalSurface) previewGeometry;
            try {
                if (geometry instanceof StepConicalSurface) {
                    FacePayload payload = toConicalFacePayload(stepFace, conicalSurface, builder, metadata);
                    if (payload != null) {
                        logPreviewFacePayload("face_payload_built", payload);
                        return new PreviewFaceResult(payload, null);
                    }
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                // C03: No silent geometry loss - log and return unsupported face payload
                return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "conical face build failed: " + ex.getMessage()));
            }
        }
        if (previewGeometry instanceof StepSphericalSurface) {
            PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
            if (trimmed.face() != null) {
                logPreviewFacePayload("face_payload_built", trimmed.face());
            }
            return trimmed;
        }
        if (previewGeometry instanceof StepRationalBSplineSurface) {
            StepRationalBSplineSurface splineSurface = (StepRationalBSplineSurface) previewGeometry;
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
        if (previewGeometry instanceof StepToroidalSurfaceWithSpecifiedBends) {
            StepToroidalSurfaceWithSpecifiedBends toroidalSurfaceWithBends = (StepToroidalSurfaceWithSpecifiedBends) previewGeometry;
            try {
                if (geometry instanceof StepToroidalSurfaceWithSpecifiedBends) {
                    FacePayload payload = toToroidalWithSpecifiedBendsFacePayload(stepFace, toroidalSurfaceWithBends, builder, metadata);
                    if (payload != null) {
                        logPreviewFacePayload("face_payload_built", payload);
                        return new PreviewFaceResult(payload, null);
                    }
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                // C03: No silent geometry loss - log and return unsupported face payload
                return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "toroidal surface with specified bends face build failed: " + ex.getMessage()));
            }
        }
        if (previewGeometry instanceof StepToroidalSurface) {
            StepToroidalSurface toroidalSurface = (StepToroidalSurface) previewGeometry;
            try {
                if (geometry instanceof StepToroidalSurface) {
                    FacePayload payload = toToroidalFacePayload(stepFace, toroidalSurface, builder, metadata);
                    if (payload != null) {
                        logPreviewFacePayload("face_payload_built", payload);
                        return new PreviewFaceResult(payload, null);
                    }
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                // C03: No silent geometry loss - log and return unsupported face payload
                return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "toroidal face build failed: " + ex.getMessage()));
            }
        }
        if (previewGeometry instanceof StepCylindricalSurface
                || previewGeometry instanceof StepConicalSurface
                || previewGeometry instanceof StepDegenerateToroidalSurface
                || previewGeometry instanceof StepToroidalSurface
                || previewGeometry instanceof StepToroidalSurfaceWithSpecifiedBends) {
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
        if (previewGeometry instanceof StepRuledSurface) {
            StepRuledSurface ruledSurface = (StepRuledSurface) previewGeometry;
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
        if (previewGeometry instanceof StepSurfaceOfConstantRadius) {
            StepSurfaceOfConstantRadius surfaceOfConstantRadius = (StepSurfaceOfConstantRadius) previewGeometry;
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
        if (previewGeometry instanceof StepParaboloidSurface) {
            StepParaboloidSurface paraboloidSurface = (StepParaboloidSurface) previewGeometry;
            try {
                FacePayload payload = toParametricSurfaceFacePayload(stepFace, paraboloidSurface, "PARABOLOID_SURFACE", builder, metadata);
                if (payload != null) {
                    logPreviewFacePayload("face_payload_built", payload);
                    return new PreviewFaceResult(payload, null);
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
            }
            return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "paraboloid surface preview failed"));
        }
        if (previewGeometry instanceof StepHyperboloidSurface) {
            StepHyperboloidSurface hyperboloidSurface = (StepHyperboloidSurface) previewGeometry;
            try {
                FacePayload payload = toParametricSurfaceFacePayload(stepFace, hyperboloidSurface, "HYPERBOLOID_SURFACE", builder, metadata);
                if (payload != null) {
                    logPreviewFacePayload("face_payload_built", payload);
                    return new PreviewFaceResult(payload, null);
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
            }
            return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "hyperboloid surface preview failed"));
        }
        if (previewGeometry instanceof StepSurfaceOfTranslation) {
            StepSurfaceOfTranslation translationSurface = (StepSurfaceOfTranslation) previewGeometry;
            try {
                FacePayload payload = toParametricSurfaceFacePayload(stepFace, translationSurface, "SURFACE_OF_TRANSLATION", builder, metadata);
                if (payload != null) {
                    logPreviewFacePayload("face_payload_built", payload);
                    return new PreviewFaceResult(payload, null);
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
            }
            return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "surface of translation preview failed"));
        }
        if (previewGeometry instanceof StepSurfaceOfProjection) {
            StepSurfaceOfProjection projectionSurface = (StepSurfaceOfProjection) previewGeometry;
            try {
                FacePayload payload = toParametricSurfaceFacePayload(stepFace, projectionSurface, "SURFACE_OF_PROJECTION", builder, metadata);
                if (payload != null) {
                    logPreviewFacePayload("face_payload_built", payload);
                    return new PreviewFaceResult(payload, null);
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
            }
            return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "surface of projection preview failed"));
        }
        if (previewGeometry instanceof StepBlendedSurface) {
            StepBlendedSurface blended = (StepBlendedSurface) previewGeometry;
            // Blended surface: approximate by rendering the primary surface with blend radius as metadata
            try {
                PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, blended.primarySurface(), metadata, builder);
                if (trimmed.face() != null) {
                    logPreviewFacePayload("face_payload_built", trimmed.face());
                    return trimmed;
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                // C03: No silent geometry loss - continue to fallback
            }
            return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "blended surface preview failed"));
        }
        // Free-form surfaces: try parametric mapping, fall back to sampled tessellation
        if (previewGeometry instanceof StepFreeFormSurface) {
            StepFreeFormSurface freeForm = (StepFreeFormSurface) previewGeometry;
            try {
                PreviewFaceResult trimmed = toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
                if (trimmed.face() != null) {
                    logPreviewFacePayload("face_payload_built", trimmed.face());
                    return trimmed;
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                // C03: No silent geometry loss - continue to fallback tessellation
            }
            // Fallback: tessellate via sampled grid if parametric mapping fails
            try {
                List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
                if (!bounds.isEmpty()) {
                    BSplineSurface3 surface = PreviewMeshExporter.buildFreeFormSurface(freeForm, builder);
                    FacePayload payload = toSampledSurfaceFacePayload(stepFace, surface, "FREE_FORM_SURFACE", bounds, metadata);
                    if (payload != null) {
                        logPreviewFacePayload("face_payload_built", payload);
                        return new PreviewFaceResult(payload, null);
                    }
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException | GeometryException ex) {
                // C03: No silent geometry loss - continue to unsupported face payload
            } catch (Exception ex) {
                // C03: Catch unexpected errors - log and return unsupported face payload
                return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "free-form surface preview failed: unexpected error - " + ex.getMessage()));
            }
            return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "free-form surface preview failed"));
        }
        // Machined surface: delegate to the underlying face geometry
        if (previewGeometry instanceof StepMachinedSurface) {
            StepMachinedSurface machinedSurface = (StepMachinedSurface) previewGeometry;
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
            if (current instanceof StepRectangularTrimmedSurface) {
            StepRectangularTrimmedSurface trimmedSurface = (StepRectangularTrimmedSurface) current;
                current = trimmedSurface.basisSurface();
                continue;
            }
            if (current instanceof StepCurveBoundedSurface) {
            StepCurveBoundedSurface boundedSurface = (StepCurveBoundedSurface) current;
                current = boundedSurface.basisSurface();
                continue;
            }
            if (current instanceof StepOrientedSurface) {
            StepOrientedSurface orientedSurface = (StepOrientedSurface) current;
                current = orientedSurface.surfaceElement();
                continue;
            }
            if (current instanceof StepOffsetSurface) {
            StepOffsetSurface offsetSurface = (StepOffsetSurface) current;
                current = offsetSurface.basisSurface();
                continue;
            }
            if (current instanceof StepOffsetSurface2) {
            StepOffsetSurface2 offsetSurface2 = (StepOffsetSurface2) current;
                current = offsetSurface2.basisSurface();
                continue;
            }
            if (current instanceof StepSurfacePatch) {
            StepSurfacePatch surfacePatch = (StepSurfacePatch) current;
                current = surfacePatch.basisSurface();
                continue;
            }
            if (current instanceof StepRectangularCompositeSurface) {
            StepRectangularCompositeSurface compositeSurface = (StepRectangularCompositeSurface) current;
                current = compositeSurface.parentSurface();
                continue;
            }
            if (current instanceof StepMachinedSurface) {
            StepMachinedSurface machinedSurface = (StepMachinedSurface) current;
                current = machinedSurface.face();
                continue;
            }
            if (current instanceof StepBlendedSurface) {
            StepBlendedSurface blended = (StepBlendedSurface) current;
                current = blended.primarySurface();
                continue;
            }
            if (current instanceof StepMappedItem) {
            StepMappedItem mappedItem = (StepMappedItem) current;
                current = mappedItem.mappingTarget();
                continue;
            }
            if (current instanceof StepGeometricReplica && "SURFACE_REPLICA".equals(((StepGeometricReplica) current).entityName())) {
                StepGeometricReplica replica = (StepGeometricReplica) current;
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
        if (surface instanceof StepRectangularTrimmedSurface) {
            StepRectangularTrimmedSurface trimmedSurface = (StepRectangularTrimmedSurface) surface;
            return describeUnsupportedPreviewSurface(trimmedSurface.basisSurface(), builder);
        }
        if (surface instanceof StepCurveBoundedSurface) {
            StepCurveBoundedSurface curveBoundedSurface = (StepCurveBoundedSurface) surface;
            return describeUnsupportedPreviewSurface(curveBoundedSurface.basisSurface(), builder);
        }
        if (surface instanceof StepOrientedSurface) {
            StepOrientedSurface orientedSurface = (StepOrientedSurface) surface;
            return describeUnsupportedPreviewSurface(orientedSurface.surfaceElement(), builder);
        }
        if (surface instanceof StepOffsetSurface) {
            StepOffsetSurface offsetSurface = (StepOffsetSurface) surface;
            return describeUnsupportedPreviewSurface(offsetSurface.basisSurface(), builder);
        }
        if (surface instanceof StepOffsetSurface2) {
            StepOffsetSurface2 offsetSurface2 = (StepOffsetSurface2) surface;
            return describeUnsupportedPreviewSurface(offsetSurface2.basisSurface(), builder);
        }
        if (surface instanceof StepSurfacePatch) {
            StepSurfacePatch surfacePatch = (StepSurfacePatch) surface;
            return describeUnsupportedPreviewSurface(surfacePatch.basisSurface(), builder);
        }
        if (surface instanceof StepRectangularCompositeSurface) {
            StepRectangularCompositeSurface compositeSurface = (StepRectangularCompositeSurface) surface;
            return describeUnsupportedPreviewSurface(compositeSurface.parentSurface(), builder);
        }
        if (surface instanceof StepMachinedSurface) {
            StepMachinedSurface machinedSurface = (StepMachinedSurface) surface;
            return describeUnsupportedPreviewSurface(machinedSurface.face(), builder);
        }
        if (surface instanceof StepBlendedSurface) {
            StepBlendedSurface blended = (StepBlendedSurface) surface;
            return describeUnsupportedPreviewSurface(blended.primarySurface(), builder);
        }
        if (surface instanceof StepGeometricReplica && "SURFACE_REPLICA".equals(((StepGeometricReplica) surface).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) surface;
            if (replica.transformation() instanceof com.minicad.step.model.StepCartesianTransformationOperator) {
                com.minicad.step.model.StepCartesianTransformationOperator transformation = (com.minicad.step.model.StepCartesianTransformationOperator) replica.transformation();
                double scale = transformation.scale() == null ? 1.0 : transformation.scale();
                if (Math.abs(scale) <= 1.0e-9) {
                    return "SURFACE_REPLICA zero scale preview is unsupported";
                }
                if (builder != null) {
                    double[] matrix = matrixForTransformationOperator(transformation, builder);
                    if (MathUtilityHelper.inverseUniformScaleTransform(matrix) == null) {
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
            StepMetadataExtractor metadata,
            UnitExtractor.UnitInfo units
    ) {
        // F04: Assembly transforms should stay in source units when units are not normalized
        // When scaleToMeters != 1.0 (units not normalized), pass 1.0 to keep transforms in source units
        // When scaleToMeters == 1.0 (units normalized or SI), pass 1.0 (no scaling needed)
        AssemblyGraph graph = StepAssemblyGraphBuilder.build(resolved, 1.0);
        Map<Integer, RepresentationPayload> representations = new LinkedHashMap<>();
        List<UnsupportedFacePayload> unsupportedFaces = new ArrayList<>();
        for (AssemblyRepresentation assemblyRepresentation : graph.representations()) {
            StepEntity entity = resolved.get(assemblyRepresentation.representationId());
            if (entity instanceof StepRepresentation) { StepRepresentation representation = (StepRepresentation) entity;
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
                if (entity instanceof StepRepresentation) { StepRepresentation representation = (StepRepresentation) entity;
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
                    node.representationIds().isEmpty() ? null : node.representationIds().get(0),
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

    public static RepresentationBuildResult buildRepresentationPayload(
            StepRepresentation representation,
            String displayName,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata
    ) {
        return buildRepresentationPayload(representation, displayName, resolved, builder, metadata, new LinkedHashSet<>());
    }

    public static RepresentationBuildResult buildRepresentationPayload(
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
        representationEdges.addAll(collectRepresentationLooseEdges(representation, resolved, builder, metadata));
        RepresentationBuildResult result = new RepresentationBuildResult(
                new RepresentationPayload(
                        representation.id(),
                        displayName,
                        representationMetadata.layers(),
                        PayloadConversionHelper.toColorPayload(representationMetadata.rgb()),
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
                if (item instanceof StepMappedItem) {
            StepMappedItem mappedItem = (StepMappedItem) item;
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
            if (!(entity instanceof StepRepresentationRelationshipWithTransformation)) {
                continue;
            }
            StepRepresentationRelationshipWithTransformation relationship = (StepRepresentationRelationshipWithTransformation) entity;
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
                    .collect(Collectors.toList());
            List<FacePayload> faces = source.payload().faces().stream()
                    .map(face -> transformMappedFace(face, relationship.id(), matrix, relationshipMetadata))
                    .collect(Collectors.toList());
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
                .collect(Collectors.toList());
        List<FacePayload> faces = source.payload().faces().stream()
                .map(face -> transformMappedFace(face, mappedItem.id(), matrix, itemMetadata))
                .collect(Collectors.toList());
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
            StepCadBuilder builder,
            StepMetadataExtractor metadata
    ) {
        Map<Integer, EdgePayload> edges = new LinkedHashMap<>();
        for (StepRepresentation candidate : linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                collectStandaloneEdges(item, edges, resolved, builder, metadata);
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

    public static List<StepRepresentation> linkedShapeRepresentations(
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
            if (entity instanceof StepShapeRepresentationRelationship) {
            StepShapeRepresentationRelationship relationship = (StepShapeRepresentationRelationship) entity;
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
            if (entity instanceof StepRepresentationRelationship) {
            StepRepresentationRelationship relationship = (StepRepresentationRelationship) entity;
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

    public static StepMetadataExtractor.DisplayMetadata mergeMetadata(
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

    public static String faceDisplayName(StepFaceEntity stepFace) {
        if (stepFace instanceof StepOrientedFace) {
            StepOrientedFace orientedFace = (StepOrientedFace) stepFace;
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



    private static int countUnsupportedFaces(Map<Integer, StepEntity> resolved, StepCadBuilder builder) {
        long startedAt = System.nanoTime();
        int unsupported = 0;
        int processed = 0;
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepFaceEntity
                    && buildPreviewFaceResult((StepFaceEntity) entity, builder, StepMetadataExtractor.DisplayMetadata.EMPTY).face() == null) {
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
                SerializationHelper.elapsedMillis(startedAt), processed, unsupported);
        return unsupported;
    }



    // facePayloadFromTopologyFace, newFacePayloadFromGrid, and sampleTopologySurfaceGrid
    // have been extracted to PreviewMeshExporter.java

    private static FacePayload toCylindricalFacePayload(
            StepFaceEntity stepFace,
            StepCylindricalSurface stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.get(0).outer()) {
            return null;
        }

        if (!(bounds.get(0).loop() instanceof EdgeLoop)) {
            return null;
        }
        EdgeLoop outerLoop = (EdgeLoop) bounds.get(0).loop();
        if (outerLoop.edges().size() != 4) {
            return null;
        }

        List<OrientedEdge> circleEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Circle)
                .collect(Collectors.toList());
        List<OrientedEdge> lineEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Line3)
                .collect(Collectors.toList());
        if (circleEdges.size() != 2 || lineEdges.size() != 2) {
            return null;
        }

        CylindricalSurface surface = builder.buildCylindricalSurface(stepSurface.id());
        OrientedEdge lowerArc = circleEdges.get(0);
        OrientedEdge upperArc = circleEdges.get(circleEdges.size() - 1);
        if (SurfaceGeometryHelper.averageAxialHeight(surface, sampleOrientedEdge(lowerArc)) > SurfaceGeometryHelper.averageAxialHeight(surface, sampleOrientedEdge(upperArc))) {
            lowerArc = circleEdges.get(circleEdges.size() - 1);
            upperArc = circleEdges.get(0);
        }

        List<CartesianPoint> lowerArcPoints = sampleOrientedEdge(lowerArc);
        List<CartesianPoint> upperArcPoints = sampleOrientedEdge(upperArc);
        double lowerHeight = SurfaceGeometryHelper.averageAxialHeight(surface, lowerArcPoints);
        double upperHeight = SurfaceGeometryHelper.averageAxialHeight(surface, upperArcPoints);
        if (Math.abs(upperHeight - lowerHeight) <= Epsilon.EPS) {
            return null;
        }

        List<Double> angles = SurfaceGeometryHelper.unwrapAngles(surface, lowerArcPoints);
        if (angles.size() < 2) {
            return null;
        }

        boolean sameSense = faceSameSense(stepFace);
        List<PointPayload> triangles = TriangulationHelper.triangulateCylindricalStrip(surface, lowerHeight, upperHeight, angles, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = SurfaceGeometryHelper.cylindricalNormal(surface, angles.get(0), sameSense);
        return new FacePayload(
                stepFace.id(),
                faceDisplayName(stepFace),
                "CYLINDRICAL_SURFACE",
                PayloadConversionHelper.toPointPayload(SurfaceGeometryHelper.surfacePoint(surface, angles.get(0), lowerHeight)),
                new VectorPayload(startNormal.x(), startNormal.y(), startNormal.z()),
                sameSense,
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, PayloadConversionHelper.toPointPayloads(sampleLoop(bounds.get(0))))),
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
                        angles.get(0),
                        angles.get(angles.size() - 1) - angles.get(0),
                        null,
                        null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null, null
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
        if (bounds.size() != 1 || !bounds.get(0).outer()) {
            return null;
        }
        if (!(bounds.get(0).loop() instanceof EdgeLoop) || ((EdgeLoop) bounds.get(0).loop()).edges().size() != 4) {
            return null;
        }
        EdgeLoop outerLoop = (EdgeLoop) bounds.get(0).loop();

        List<OrientedEdge> circleEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Circle)
                .collect(Collectors.toList());
        List<OrientedEdge> lineEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Line3)
                .collect(Collectors.toList());
        if (circleEdges.size() != 2 || lineEdges.size() != 2) {
            return null;
        }

        ConicalSurface surface = builder.buildConicalSurface(stepSurface.id());
        OrientedEdge lowerArc = circleEdges.get(0);
        OrientedEdge upperArc = circleEdges.get(circleEdges.size() - 1);
        if (SurfaceGeometryHelper.averageAxialHeight(surface.position(), sampleOrientedEdge(lowerArc)) > SurfaceGeometryHelper.averageAxialHeight(surface.position(), sampleOrientedEdge(upperArc))) {
            lowerArc = circleEdges.get(circleEdges.size() - 1);
            upperArc = circleEdges.get(0);
        }

        List<CartesianPoint> lowerArcPoints = sampleOrientedEdge(lowerArc);
        List<CartesianPoint> upperArcPoints = sampleOrientedEdge(upperArc);
        double lowerHeight = SurfaceGeometryHelper.averageAxialHeight(surface.position(), lowerArcPoints);
        double upperHeight = SurfaceGeometryHelper.averageAxialHeight(surface.position(), upperArcPoints);
        if (Math.abs(upperHeight - lowerHeight) <= Epsilon.EPS) {
            return null;
        }

        List<Double> angles = SurfaceGeometryHelper.unwrapAngles(surface.position(), lowerArcPoints);
        if (angles.size() < 2) {
            return null;
        }

        boolean sameSense = faceSameSense(stepFace);
        List<PointPayload> triangles = TriangulationHelper.triangulateConicalStrip(surface, lowerHeight, upperHeight, angles, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = SurfaceGeometryHelper.conicalNormal(surface, angles.get(0), sameSense);
        return new FacePayload(
                stepFace.id(),
                faceDisplayName(stepFace),
                "CONICAL_SURFACE",
                PayloadConversionHelper.toPointPayload(SurfaceGeometryHelper.conicalSurfacePoint(surface, angles.get(0), lowerHeight)),
                new VectorPayload(startNormal.x(), startNormal.y(), startNormal.z()),
                sameSense,
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, PayloadConversionHelper.toPointPayloads(sampleLoop(bounds.get(0))))),
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
                        angles.get(0),
                        angles.get(angles.size() - 1) - angles.get(0),
                        null,
                        null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null, null
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
        if (bounds.size() != 1 || !bounds.get(0).outer()) {
            return null;
        }
        if (!(bounds.get(0).loop() instanceof EdgeLoop) || ((EdgeLoop) bounds.get(0).loop()).edges().size() != 4) {
            return null;
        }
        EdgeLoop outerLoop = (EdgeLoop) bounds.get(0).loop();

        List<OrientedEdge> circleEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Circle)
                .collect(Collectors.toList());
        if (circleEdges.size() != 4) {
            return null;
        }

        ToroidalSurface surface = builder.buildToroidalSurface(stepSurface.id());
        List<OrientedEdge> varyingUEdges = new ArrayList<>();
        List<OrientedEdge> varyingVEdges = new ArrayList<>();
        for (OrientedEdge edge : circleEdges) {
            List<CartesianPoint> points = sampleOrientedEdge(edge);
            List<Double> uValues = SurfaceGeometryHelper.unwrapToroidalU(surface, points);
            List<Double> vValues = SurfaceGeometryHelper.unwrapToroidalV(surface, points);
            double uRange = Math.abs(uValues.get(uValues.size() - 1) - uValues.get(0));
            double vRange = Math.abs(vValues.get(vValues.size() - 1) - vValues.get(0));
            if (uRange >= vRange) {
                varyingUEdges.add(edge);
            } else {
                varyingVEdges.add(edge);
            }
        }
        if (varyingUEdges.size() != 2 || varyingVEdges.size() != 2) {
            return null;
        }

        OrientedEdge lowerVEdge = varyingUEdges.get(0);
        OrientedEdge upperVEdge = varyingUEdges.get(varyingUEdges.size() - 1);
        if (SurfaceGeometryHelper.averageToroidalV(surface, sampleOrientedEdge(lowerVEdge)) > SurfaceGeometryHelper.averageToroidalV(surface, sampleOrientedEdge(upperVEdge))) {
            lowerVEdge = varyingUEdges.get(varyingUEdges.size() - 1);
            upperVEdge = varyingUEdges.get(0);
        }

        List<CartesianPoint> lowerPoints = sampleOrientedEdge(lowerVEdge);
        List<Double> uValues = SurfaceGeometryHelper.unwrapToroidalU(surface, lowerPoints);
        double lowerV = SurfaceGeometryHelper.averageToroidalV(surface, lowerPoints);
        double upperV = SurfaceGeometryHelper.averageToroidalV(surface, sampleOrientedEdge(upperVEdge));
        if (Math.abs(upperV - lowerV) <= Epsilon.EPS || uValues.size() < 2) {
            return null;
        }

        boolean sameSense = faceSameSense(stepFace);
        List<PointPayload> triangles = TriangulationHelper.triangulateToroidalStrip(surface, lowerV, upperV, uValues, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = SurfaceGeometryHelper.toroidalNormal(surface, uValues.get(0), lowerV, sameSense);
        return new FacePayload(
                stepFace.id(),
                faceDisplayName(stepFace),
                "TOROIDAL_SURFACE",
                PayloadConversionHelper.toPointPayload(SurfaceGeometryHelper.toroidalSurfacePoint(surface, uValues.get(0), lowerV)),
                new VectorPayload(startNormal.x(), startNormal.y(), startNormal.z()),
                sameSense,
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, PayloadConversionHelper.toPointPayloads(sampleLoop(bounds.get(0))))),
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
                        uValues.get(0),
                        uValues.get(uValues.size() - 1) - uValues.get(0),
                        null,
                        null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null, null
                ),
                null
        );
    }

    private static FacePayload toToroidalWithSpecifiedBendsFacePayload(
            StepFaceEntity stepFace,
            StepToroidalSurfaceWithSpecifiedBends stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.get(0).outer()) {
            return null;
        }
        if (!(bounds.get(0).loop() instanceof EdgeLoop) || ((EdgeLoop) bounds.get(0).loop()).edges().size() != 4) {
            return null;
        }
        EdgeLoop outerLoop = (EdgeLoop) bounds.get(0).loop();

        List<OrientedEdge> circleEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Circle)
                .collect(Collectors.toList());
        if (circleEdges.size() != 4) {
            return null;
        }

        ToroidalSurface surface = builder.buildToroidalSurface(stepSurface.id());
        List<OrientedEdge> varyingUEdges = new ArrayList<>();
        List<OrientedEdge> varyingVEdges = new ArrayList<>();
        for (OrientedEdge edge : circleEdges) {
            List<CartesianPoint> points = sampleOrientedEdge(edge);
            List<Double> uValues = SurfaceGeometryHelper.unwrapToroidalU(surface, points);
            List<Double> vValues = SurfaceGeometryHelper.unwrapToroidalV(surface, points);
            double uRange = Math.abs(uValues.get(uValues.size() - 1) - uValues.get(0));
            double vRange = Math.abs(vValues.get(vValues.size() - 1) - vValues.get(0));
            if (uRange >= vRange) {
                varyingUEdges.add(edge);
            } else {
                varyingVEdges.add(edge);
            }
        }
        if (varyingUEdges.size() != 2 || varyingVEdges.size() != 2) {
            return null;
        }

        OrientedEdge lowerVEdge = varyingUEdges.get(0);
        OrientedEdge upperVEdge = varyingUEdges.get(varyingUEdges.size() - 1);
        if (SurfaceGeometryHelper.averageToroidalV(surface, sampleOrientedEdge(lowerVEdge)) > SurfaceGeometryHelper.averageToroidalV(surface, sampleOrientedEdge(upperVEdge))) {
            lowerVEdge = varyingUEdges.get(varyingUEdges.size() - 1);
            upperVEdge = varyingUEdges.get(0);
        }

        List<CartesianPoint> lowerPoints = sampleOrientedEdge(lowerVEdge);
        List<Double> uValues = SurfaceGeometryHelper.unwrapToroidalU(surface, lowerPoints);
        double lowerV = SurfaceGeometryHelper.averageToroidalV(surface, lowerPoints);
        double upperV = SurfaceGeometryHelper.averageToroidalV(surface, sampleOrientedEdge(upperVEdge));
        if (Math.abs(upperV - lowerV) <= Epsilon.EPS || uValues.size() < 2) {
            return null;
        }

        boolean sameSense = faceSameSense(stepFace);
        List<PointPayload> triangles = TriangulationHelper.triangulateToroidalStrip(surface, lowerV, upperV, uValues, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = SurfaceGeometryHelper.toroidalNormal(surface, uValues.get(0), lowerV, sameSense);
        return new FacePayload(
                stepFace.id(),
                faceDisplayName(stepFace),
                "TOROIDAL_SURFACE_WITH_SPECIFIED_BENDS",
                PayloadConversionHelper.toPointPayload(SurfaceGeometryHelper.toroidalSurfacePoint(surface, uValues.get(0), lowerV)),
                new VectorPayload(startNormal.x(), startNormal.y(), startNormal.z()),
                sameSense,
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, PayloadConversionHelper.toPointPayloads(sampleLoop(bounds.get(0))))),
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
                        uValues.get(0),
                        uValues.get(uValues.size() - 1) - uValues.get(0),
                        null,
                        null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null, null
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
        if (bounds.size() != 1 || !bounds.get(0).outer()) {
            return null;
        }
        if (!(bounds.get(0).loop() instanceof EdgeLoop) || ((EdgeLoop) bounds.get(0).loop()).edges().size() != 4) {
            return null;
        }
        EdgeLoop outerLoop = (EdgeLoop) bounds.get(0).loop();

        SurfacePatch patch = buildFourSidedPatch(outerLoop);
        if (patch == null) {
            return null;
        }
        BSplineSurface3 surface = PreviewMeshExporter.buildBsplineSurface(stepSurface, builder);
        int uSegments = Math.max(patch.uSegments(), 10);
        int vSegments = Math.max(patch.vSegments(), 10);
        List<PointPayload> triangles = TriangulationHelper.triangulateSurfaceGrid(
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
                PayloadConversionHelper.toPointPayload(surface.pointAt(surface.uStart(), surface.vStart())),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                faceSameSense(stepFace),
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, PayloadConversionHelper.toPointPayloads(sampleLoop(bounds.get(0))))),
                triangles,
                null,
                null
        );
    }

    public static BSplineSurface3 buildBsplineSurface(StepEntity geometry, StepCadBuilder builder) {
        if (geometry instanceof StepBSplineSurfaceWithKnots) {
            StepBSplineSurfaceWithKnots splineSurface = (StepBSplineSurfaceWithKnots) geometry;
            return builder.buildBSplineSurface(splineSurface.id());
        }
        if (geometry instanceof StepBSplineSurface) {
            StepBSplineSurface splineSurface = (StepBSplineSurface) geometry;
            return builder.buildGenericBSplineSurface(splineSurface.id());
        }
        if (geometry instanceof StepBSplineSurfaceWithKnotsAndBreakpoints) {
            StepBSplineSurfaceWithKnotsAndBreakpoints splineSurface = (StepBSplineSurfaceWithKnotsAndBreakpoints) geometry;
            return builder.buildBSplineSurfaceWithBreakpoints(splineSurface.id());
        }
        if (geometry instanceof StepBezierSurface) {
            StepBezierSurface splineSurface = (StepBezierSurface) geometry;
            return builder.buildBezierSurface(splineSurface.id());
        }
        if (geometry instanceof StepUniformSurface) {
            StepUniformSurface splineSurface = (StepUniformSurface) geometry;
            return builder.buildUniformSurface(splineSurface.id());
        }
        if (geometry instanceof StepQuasiUniformSurface) {
            StepQuasiUniformSurface splineSurface = (StepQuasiUniformSurface) geometry;
            return builder.buildQuasiUniformSurface(splineSurface.id());
        }
        if (geometry instanceof StepPiecewiseBezierSurface) {
            StepPiecewiseBezierSurface splineSurface = (StepPiecewiseBezierSurface) geometry;
            return builder.buildPiecewiseBezierSurface(splineSurface.id());
        }
        throw new UnsupportedGeometryException(surfaceTypeName(geometry) + " is not a supported B-spline-like surface");
    }

    public static BSplineSurface3 buildFreeFormSurface(StepFreeFormSurface surface, StepCadBuilder builder) {
        int uCount = surface.controlPoints().size();
        int vCount = surface.controlPoints().isEmpty() ? 0 : surface.controlPoints().get(0).size();
        if (uCount < 2 || vCount < 2) {
            throw new UnsupportedGeometryException("FREE_FORM_SURFACE requires at least 2x2 control points");
        }
        List<List<CartesianPoint>> controlPoints = new ArrayList<>(uCount);
        for (List<StepEntity> row : surface.controlPoints()) {
            List<CartesianPoint> pointRow = new ArrayList<>(row.size());
            for (StepEntity pt : row) {
                if (pt instanceof com.minicad.step.model.StepCartesianPoint) {
                    com.minicad.step.model.StepCartesianPoint cartesianPoint = (com.minicad.step.model.StepCartesianPoint) pt;
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
        if (bounds.size() != 1 || !bounds.get(0).outer()) {
            return null;
        }
        RationalBSplineSurface3 surface = builder.buildRationalBSplineSurface(stepSurface.id());
        List<PointPayload> triangles = TriangulationHelper.triangulateSurfaceGrid(
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
                PayloadConversionHelper.toPointPayload(surface.pointAt(surface.uStart(), surface.vStart())),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                faceSameSense(stepFace),
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, PayloadConversionHelper.toPointPayloads(sampleLoop(bounds.get(0))))),
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
        if (bounds.size() != 1 || !bounds.get(0).outer()) {
            return null;
        }
        if (!(bounds.get(0).loop() instanceof EdgeLoop) || ((EdgeLoop) bounds.get(0).loop()).edges().size() != 4) {
            return null;
        }
        EdgeLoop outerLoop = (EdgeLoop) bounds.get(0).loop();
        SurfacePatch patch = buildFourSidedPatch(outerLoop);
        if (patch == null) {
            return null;
        }
        List<PointPayload> triangles = TriangulationHelper.triangulatePatch(patch, faceSameSense(stepFace));
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
                PayloadConversionHelper.toPointPayload(patch.pointAt(0.0, 0.0)),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                faceSameSense(stepFace),
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, PayloadConversionHelper.toPointPayloads(sampleLoop(bounds.get(0))))),
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
        List<PointPayload> triangles = TriangulationHelper.triangulateSurfaceGrid(grid, faceSameSense(stepFace));
        if (triangles.isEmpty()) {
            return null;
        }
        boolean sameSense = faceSameSense(stepFace);
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) normal = normal.scale(-1.0);
        List<LoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : bounds) {
            loops.add(new LoopPayload(bound.outer(), PayloadConversionHelper.toPointPayloads(sampleLoop(bound))));
        }
        return new FacePayload(
                stepFace.id(),
                faceDisplayName(stepFace),
                "RULED_SURFACE",
                triangles.get(0),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops,
                triangles,
                new FaceSurfacePayload(
                        "ruled_surface", null, null, null, 0.0, null, null,
                        0.0, 0.0, 0.0, 0.0,
                        null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null, null, null
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
        List<PointPayload> triangles = TriangulationHelper.triangulateSurfaceGrid(grid, faceSameSense(stepFace));
        if (triangles.isEmpty()) {
            return null;
        }
        boolean sameSense = faceSameSense(stepFace);
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) normal = normal.scale(-1.0);
        List<LoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : bounds) {
            loops.add(new LoopPayload(bound.outer(), PayloadConversionHelper.toPointPayloads(sampleLoop(bound))));
        }
        return new FacePayload(
                stepFace.id(),
                faceDisplayName(stepFace),
                "SURFACE_OF_CONSTANT_RADIUS",
                triangles.get(0),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops,
                triangles,
                new FaceSurfacePayload(
                        "constant_radius_surface", null, null, null, surface.radius(), null, null,
                        0.0, 0.0, 0.0, 0.0,
                        null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null, null, null
                ),
                null
        );
    }

    /**
     * Generic parametric surface preview for surfaces with sampleGrid:
     * paraboloid, hyperboloid, surface of translation, surface of projection.
     */
    private static FacePayload toParametricSurfaceFacePayload(
            StepFaceEntity stepFace,
            StepEntity stepSurface,
            String surfaceTypeName,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) throws TopologyException, StepResolutionException, UnsupportedGeometryException, GeometryException {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.isEmpty()) {
            return null;
        }
        SurfaceGeometry surface = builder.buildSurfaceGeometry(stepSurface.id());
        java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(32, 32);
        List<PointPayload> triangles = TriangulationHelper.triangulateSurfaceGrid(grid, faceSameSense(stepFace));
        if (triangles.isEmpty()) {
            return null;
        }
        boolean sameSense = faceSameSense(stepFace);
        Vector3 normal = surface.normalAt(0.5, 0.5);
        if (!sameSense) normal = normal.scale(-1.0);
        List<LoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : bounds) {
            loops.add(new LoopPayload(bound.outer(), PayloadConversionHelper.toPointPayloads(sampleLoop(bound))));
        }
        return new FacePayload(
                stepFace.id(),
                faceDisplayName(stepFace),
                surfaceTypeName,
                triangles.get(0),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                sameSense,
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                loops,
                triangles,
                null,
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
        List<PointPayload> triangles = TriangulationHelper.triangulateSurfaceGrid(grid, sameSense);
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
                PayloadConversionHelper.toColorPayload(metadata.rgb()),
                metadata.transparency(),
                PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                metadata.layers(),
                List.of(new LoopPayload(true, PayloadConversionHelper.toPointPayloads(sampleLoop(bounds.get(0))))),
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
        ParametricSurfaceMapper mapper = SurfaceMapperHelper.mapperForSurface(geometry, builder);
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
                            PreviewStatisticsHelper.formatUvBounds(uvBounds),
                            sampleCount,
                            baseUSegments,
                            baseVSegments,
                            PreviewStatisticsHelper.summarizeLoopPointCounts(loops));
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
                        PayloadConversionHelper.toPointPayload(mapper.pointAt(centerU, centerV)),
                        new VectorPayload(normal.x(), normal.y(), normal.z()),
                        faceSameSense(stepFace),
                        PayloadConversionHelper.toColorPayload(metadata.rgb()),
                        metadata.transparency(),
                        PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                        metadata.layers(),
                        toParametricLoopPayloads(loops, mapper),
                        triangles,
                        faceSurfacePayload(geometry, uvBounds, builder),
                        loops
                ),
                null
        );
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
            uvPoints.set(0, uvPoints.get(0));
            uvPoints.set(uvPoints.size() - 1, uvPoints.get(0));
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
            if (!(bound.loop() instanceof com.minicad.step.model.StepEdgeLoop)) {
                log.debug("stage={} faceId={}, surfaceType={}, boundId={}, reason={}", "parametric_loop_build_failed",
                        stepFace.id(), surfaceTypeName(geometry), bound.id(), "bound loop is not EDGE_LOOP");
                return List.of();
            }
            com.minicad.step.model.StepEdgeLoop edgeLoop = (com.minicad.step.model.StepEdgeLoop) bound.loop();
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
            if (!PcurveSamplingHelper.sameUv(loopPoints.get(0), loopPoints.get(loopPoints.size() - 1))) {
                loopPoints.add(loopPoints.get(0));
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
                    u = MathUtilityHelper.unwrapPeriodic(u, previous.u(), uPeriod);
                }
                if (vPeriod != null) {
                    v = MathUtilityHelper.unwrapPeriodic(v, previous.v(), vPeriod);
                }
            }
            UvPoint normalizedPoint = new UvPoint(u, v);
            normalized.add(normalizedPoint);
            previous = normalizedPoint;
        }
        if (normalized.size() >= 2) {
            UvPoint first = normalized.get(0);
            UvPoint last = normalized.get(normalized.size() - 1);
            double u = last.u();
            double v = last.v();
            if (uPeriod != null) {
                u = MathUtilityHelper.unwrapPeriodic(u, first.u(), uPeriod);
            }
            if (vPeriod != null) {
                v = MathUtilityHelper.unwrapPeriodic(v, first.v(), vPeriod);
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
        if (surfaceGeometry instanceof StepPlane) {
            StepPlane stepPlane = (StepPlane) surfaceGeometry;
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
                    null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepCylindricalSurface) {
            StepCylindricalSurface cylindricalSurface = (StepCylindricalSurface) surfaceGeometry;
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
                    null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepConicalSurface) {
            StepConicalSurface conicalSurface = (StepConicalSurface) surfaceGeometry;
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
                    null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepSphericalSurface) {
            StepSphericalSurface sphericalSurface = (StepSphericalSurface) surfaceGeometry;
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
                    null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepToroidalSurface) {
            StepToroidalSurface toroidalSurface = (StepToroidalSurface) surfaceGeometry;
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
                    null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepDegenerateToroidalSurface) {
            StepDegenerateToroidalSurface toroidalSurface = (StepDegenerateToroidalSurface) surfaceGeometry;
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
                    null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepSurfaceOfLinearExtrusion) {
            StepSurfaceOfLinearExtrusion extrusionSurface = (StepSurfaceOfLinearExtrusion) surfaceGeometry;
            SurfaceOfLinearExtrusion3 surface = builder.buildSurfaceOfLinearExtrusion(extrusionSurface.id());
            Direction3 axis = surface.extrusionVector().normalize().asDirection();
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
                    null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepSurfaceOfRevolution) {
            StepSurfaceOfRevolution revolutionSurface = (StepSurfaceOfRevolution) surfaceGeometry;
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
                    null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepRationalBSplineSurface) {
            StepRationalBSplineSurface splineSurface = (StepRationalBSplineSurface) surfaceGeometry;
            RationalBSplineSurface3 surface = builder.buildRationalBSplineSurface(splineSurface.id());
            List<List<List<Double>>> controlPoints = surface.controlPoints().stream()
                    .map(row -> row.stream()
                            .map(point -> List.of(point.x(), point.y(), point.z()))
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());
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
                    surface.vKnots(),
                    null, null, null, null, null, null, null, null, null, null, null, null
            ), geometry);
        }
        if (surfaceGeometry instanceof StepBSplineSurfaceWithKnots
                || surfaceGeometry instanceof StepBezierSurface
                || surfaceGeometry instanceof StepUniformSurface
                || surfaceGeometry instanceof StepQuasiUniformSurface
                || surfaceGeometry instanceof StepPiecewiseBezierSurface) {
            BSplineSurface3 surface = PreviewMeshExporter.buildBsplineSurface(surfaceGeometry, builder);
            List<List<List<Double>>> controlPoints = surface.controlPoints().stream()
                    .map(row -> row.stream()
                            .map(point -> List.of(point.x(), point.y(), point.z()))
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());
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
                    surface.vKnots(),
                    null, null, null, null, null, null, null, null, null, null, null, null
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

        if (geometry instanceof StepRectangularTrimmedSurface) {
            StepRectangularTrimmedSurface trimmedSurface = (StepRectangularTrimmedSurface) geometry;
            basisType = surfaceTypeName(trimmedSurface.basisSurface());
            basisStepId = trimmedSurface.basisSurface().id();
            trimU1 = trimmedSurface.u1();
            trimU2 = trimmedSurface.u2();
            trimV1 = trimmedSurface.v1();
            trimV2 = trimmedSurface.v2();
        } else if (geometry instanceof StepCurveBoundedSurface) {
            StepCurveBoundedSurface boundedSurface = (StepCurveBoundedSurface) geometry;
            basisType = surfaceTypeName(boundedSurface.basisSurface());
            basisStepId = boundedSurface.basisSurface().id();
            implicitOuter = boundedSurface.implicitOuter();
        } else if (geometry instanceof StepOrientedSurface) {
            StepOrientedSurface orientedSurface = (StepOrientedSurface) geometry;
            basisType = surfaceTypeName(orientedSurface.surfaceElement());
            basisStepId = orientedSurface.surfaceElement().id();
            orientation = orientedSurface.orientation();
        } else if (geometry instanceof StepOffsetSurface) {
            StepOffsetSurface offsetSurface = (StepOffsetSurface) geometry;
            basisType = surfaceTypeName(offsetSurface.basisSurface());
            basisStepId = offsetSurface.basisSurface().id();
            offsetDistance = offsetSurface.distance();
        } else if (geometry instanceof StepGeometricReplica && "SURFACE_REPLICA".equals(((StepGeometricReplica) geometry).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) geometry;
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
        Direction3 xDirection = reference.subtract(axis.scale(reference.dot(axis))).normalize().asDirection();
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
        List<StepEntity> pcurves = null;
        // Default to empty list for unsupported source types
        pcurves = List.of();
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
            if (built instanceof Line2) {
            Line2 line = (Line2) built;
                UvPoint start = PcurveSamplingHelper.snapToLine(projectedStart, line);
                UvPoint end = PcurveSamplingHelper.snapToLine(projectedEnd, line);
                double score = PcurveSamplingHelper.distanceSquared(projectedStart, start) + PcurveSamplingHelper.distanceSquared(projectedEnd, end);
                List<UvPoint> samples = PcurveSamplingHelper.sampleLinePcurve(line, start, end);
                if (best == null || score < bestScore) {
                    best = samples;
                    bestScore = score;
                }
                continue;
            }
            if (built instanceof BSplineCurve2) {
            BSplineCurve2 spline = (BSplineCurve2) built;
                List<UvPoint> samples = PcurveSamplingHelper.sampleSplinePcurve(spline, projectedStart, projectedEnd);
                if (!samples.isEmpty()) {
                    double score = PcurveSamplingHelper.distanceSquared(projectedStart, samples.get(0)) + PcurveSamplingHelper.distanceSquared(projectedEnd, samples.get(samples.size() - 1));
                    if (best == null || score < bestScore) {
                        best = samples;
                        bestScore = score;
                    }
                }
                continue;
            }
            if (built instanceof Circle2) {
            Circle2 circle = (Circle2) built;
                UvPoint start = PcurveSamplingHelper.snapToCircle(projectedStart, circle);
                UvPoint end = PcurveSamplingHelper.snapToCircle(projectedEnd, circle);
                double score = PcurveSamplingHelper.distanceSquared(projectedStart, start) + PcurveSamplingHelper.distanceSquared(projectedEnd, end);
                List<UvPoint> samples = PcurveSamplingHelper.sampleCirclePcurve(circle, start, end);
                if (!samples.isEmpty() && (best == null || score < bestScore)) {
                    best = samples;
                    bestScore = score;
                }
                continue;
            }
            if (built instanceof Ellipse2) {
            Ellipse2 ellipse = (Ellipse2) built;
                UvPoint start = PcurveSamplingHelper.snapToEllipse(projectedStart, ellipse);
                UvPoint end = PcurveSamplingHelper.snapToEllipse(projectedEnd, ellipse);
                double score = PcurveSamplingHelper.distanceSquared(projectedStart, start) + PcurveSamplingHelper.distanceSquared(projectedEnd, end);
                List<UvPoint> samples = PcurveSamplingHelper.sampleEllipsePcurve(ellipse, start, end);
                if (!samples.isEmpty() && (best == null || score < bestScore)) {
                    best = samples;
                    bestScore = score;
                }
                continue;
            }
            if (built instanceof TrimmedCurve2) {
            TrimmedCurve2 trimmed = (TrimmedCurve2) built;
                List<UvPoint> samples = PcurveSamplingHelper.sampleTrimmedPcurve(trimmed, projectedStart, projectedEnd);
                if (!samples.isEmpty()) {
                    double score = PcurveSamplingHelper.distanceSquared(projectedStart, samples.get(0)) + PcurveSamplingHelper.distanceSquared(projectedEnd, samples.get(samples.size() - 1));
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
        StepEntity unwrapped = unwrapAssociatedCurveGeometry(edgeGeometry);
        if (unwrapped instanceof StepSurfaceCurve) {
            StepSurfaceCurve surfaceCurve = (StepSurfaceCurve) unwrapped;
            return surfaceCurve.associatedGeometry().isEmpty();
        } else if (unwrapped instanceof StepSeamCurve) {
            StepSeamCurve seamCurve = (StepSeamCurve) unwrapped;
            return seamCurve.associatedGeometry().isEmpty();
        } else {
            return true;
        }
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
        StepEntity unwrapped = unwrapAssociatedCurveGeometry(edgeGeometry);
        List<StepEntity> associated;
        if (unwrapped instanceof StepSurfaceCurve) {
            StepSurfaceCurve surfaceCurve = (StepSurfaceCurve) unwrapped;
            associated = surfaceCurve.associatedGeometry();
        } else if (unwrapped instanceof StepSeamCurve) {
            StepSeamCurve seamCurve = (StepSeamCurve) unwrapped;
            associated = seamCurve.associatedGeometry();
        } else {
            associated = List.of();
        }
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
            if (current instanceof StepOrientedCurve) {
            StepOrientedCurve orientedCurve = (StepOrientedCurve) current;
                current = orientedCurve.curveElement();
                continue;
            }
            if (current instanceof StepGeometricReplica && "CURVE_REPLICA".equals(((StepGeometricReplica) current).entityName())) {
                StepGeometricReplica replica = (StepGeometricReplica) current;
                current = replica.parent();
                continue;
            }
            if (current instanceof StepAnnotationCurveOccurrence) {
            StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) current;
                current = occurrence.item();
                continue;
            }
            if (current instanceof StepDimensionCurve) {
            StepDimensionCurve dimensionCurve = (StepDimensionCurve) current;
                current = dimensionCurve.item();
                continue;
            }
            if (current instanceof StepLeaderCurve) {
            StepLeaderCurve leaderCurve = (StepLeaderCurve) current;
                current = leaderCurve.item();
                continue;
            }
            if (current instanceof StepProjectionCurve) {
            StepProjectionCurve projectionCurve = (StepProjectionCurve) current;
                current = projectionCurve.item();
                continue;
            }
            if (current instanceof StepDraughtingAnnotationOccurrence) {
            StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) current;
                current = annotationOccurrence.item();
                continue;
            }
            if (current instanceof StepTerminatorSymbol) {
            StepTerminatorSymbol terminatorSymbol = (StepTerminatorSymbol) current;
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
                    if (pcurve instanceof StepPcurve) {
            StepPcurve exact = (StepPcurve) pcurve;
                        return "#" + exact.id() + "->#" + exact.basisSurface().id();
                    }
                    if (pcurve instanceof StepDegeneratePcurve) {
            StepDegeneratePcurve degenerate = (StepDegeneratePcurve) pcurve;
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
            if (associated instanceof StepPcurve && acceptableSurfaceIds.contains(((StepPcurve) associated).basisSurface().id())) {
                StepPcurve pcurve = (StepPcurve) associated;
                matches.add(pcurve);
            } else if (associated instanceof StepDegeneratePcurve && acceptableSurfaceIds.contains(((StepDegeneratePcurve) associated).basisSurface().id())) {
                StepDegeneratePcurve pcurve = (StepDegeneratePcurve) associated;
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
            if (current instanceof StepRectangularTrimmedSurface) {
            StepRectangularTrimmedSurface trimmedSurface = (StepRectangularTrimmedSurface) current;
                current = trimmedSurface.basisSurface();
                continue;
            }
            if (current instanceof StepCurveBoundedSurface) {
            StepCurveBoundedSurface boundedSurface = (StepCurveBoundedSurface) current;
                current = boundedSurface.basisSurface();
                continue;
            }
            if (current instanceof StepOrientedSurface) {
            StepOrientedSurface orientedSurface = (StepOrientedSurface) current;
                current = orientedSurface.surfaceElement();
                continue;
            }
            if (current instanceof StepOffsetSurface) {
            StepOffsetSurface offsetSurface = (StepOffsetSurface) current;
                current = offsetSurface.basisSurface();
                continue;
            }
            if (current instanceof StepGeometricReplica && "SURFACE_REPLICA".equals(((StepGeometricReplica) current).entityName())) {
                StepGeometricReplica replica = (StepGeometricReplica) current;
                current = replica.parent();
                continue;
            }
            break;
        }
        return Set.copyOf(ids);
    }

    
    

    

    
    




    
    private static List<LoopPayload> toParametricLoopPayloads(List<ParametricLoopPayload> loops, ParametricSurfaceMapper mapper) {
        List<LoopPayload> payloads = new ArrayList<>(loops.size());
        for (ParametricLoopPayload loop : loops) {
            List<PointPayload> points = new ArrayList<>(loop.points().size());
            for (UvPoint point : loop.points()) {
                points.add(PayloadConversionHelper.toPointPayload(mapper.pointAt(point.u(), point.v())));
            }
            payloads.add(new LoopPayload(loop.outer(), List.copyOf(points)));
        }
        return List.copyOf(payloads);
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
        List<ParametricLoopPayload> holes = loops.stream().filter(loop -> !loop.outer()).collect(Collectors.toList());
        List<PointPayload> triangles = new ArrayList<>();
        for (int ui = 0; ui < uSegments; ui++) {
            double u0 = bounds.minU() + bounds.uSpan() * ui / uSegments;
            double u1 = bounds.minU() + bounds.uSpan() * (ui + 1) / uSegments;
            for (int vi = 0; vi < vSegments; vi++) {
                double v0 = bounds.minV() + bounds.vSpan() * vi / vSegments;
                double v1 = bounds.minV() + bounds.vSpan() * (vi + 1) / vSegments;
                UvPoint center = new UvPoint((u0 + u1) * 0.5, (v0 + v1) * 0.5);
                if (!TriangulationHelper.contains(outer.points(), center)) {
                    continue;
                }
                boolean insideHole = false;
                for (ParametricLoopPayload hole : holes) {
                    if (TriangulationHelper.contains(hole.points(), center)) {
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
                TriangulationHelper.appendOrientedTriangle(triangles, p00, p10, p11, normal);
                TriangulationHelper.appendOrientedTriangle(triangles, p00, p11, p01, normal);
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
            double area = Math.abs(TriangulationHelper.signedArea(loops.get(index).points()));
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

    
    
    

    

    


    public static CurveEvaluator curveEvaluator(StepEntity curve, StepCadBuilder builder) {
        // Converted from switch expression to if-else for Java 11 compatibility
        if (curve instanceof StepLine) {
            StepLine line = (StepLine) curve;
            Line3 geometry = builder.buildLine(line.id());
            return new CurveEvaluator() {
                @Override
                public double start() { return -1.0; }
                @Override
                public double end() { return 1.0; }
                @Override
                public CartesianPoint pointAt(double parameter) {
                    return geometry.pointAt(parameter);
                }
            };
        } else if (curve instanceof StepCircle) {
            StepCircle circle = (StepCircle) curve;
            Circle geometry = builder.buildCircle(circle.id());
            return new CurveEvaluator() {
                @Override
                public double start() { return 0.0; }
                @Override
                public double end() { return Math.PI * 2.0; }
                @Override
                public CartesianPoint pointAt(double parameter) {
                    return geometry.pointAt(parameter);
                }
            };
        } else if (curve instanceof StepEllipse) {
            StepEllipse ellipse = (StepEllipse) curve;
            Ellipse3 geometry = builder.buildEllipse(ellipse.id());
            return new CurveEvaluator() {
                @Override
                public double start() { return 0.0; }
                @Override
                public double end() { return Math.PI * 2.0; }
                @Override
                public CartesianPoint pointAt(double parameter) {
                    return geometry.pointAt(parameter);
                }
            };
        } else if (curve instanceof StepBSplineCurveWithKnots) {
            StepBSplineCurveWithKnots spline = (StepBSplineCurveWithKnots) curve;
            BSplineCurve3 geometry = builder.buildBSplineCurve(spline.id());
            return new CurveEvaluator() {
                @Override
                public double start() { return geometry.startParameter(); }
                @Override
                public double end() { return geometry.endParameter(); }
                @Override
                public CartesianPoint pointAt(double parameter) {
                    return geometry.pointAt(parameter);
                }
            };
        } else if (curve instanceof StepTrimmedCurve) {
            StepTrimmedCurve trimmedCurve = (StepTrimmedCurve) curve;
            return curveEvaluator(trimmedCurve.basisCurve(), builder);
        } else if (curve instanceof StepSurfaceCurve) {
            StepSurfaceCurve surfaceCurve = (StepSurfaceCurve) curve;
            return curveEvaluator(surfaceCurve.curve3d(), builder);
        } else if (curve instanceof StepRationalBSplineCurve) {
            StepRationalBSplineCurve spline = (StepRationalBSplineCurve) curve;
            com.minicad.geometry.RationalBSplineCurve3 geometry = builder.buildRationalBSplineCurve(spline.id());
            return new CurveEvaluator() {
                @Override public double start() { return geometry.startParameter(); }
                @Override public double end() { return geometry.endParameter(); }
                @Override public CartesianPoint pointAt(double parameter) { return geometry.pointAt(parameter); }
            };
        } else if (curve instanceof StepPolyline) {
            StepPolyline polyline = (StepPolyline) curve;
            Polyline3 geometry = builder.buildPolyline(polyline.id());
            return new CurveEvaluator() {
                @Override public double start() { return 0.0; }
                @Override public double end() { return 1.0; }
                @Override public CartesianPoint pointAt(double parameter) { return geometry.pointAt(parameter); }
            };
        } else if (curve instanceof StepCompositeCurve) {
            StepCompositeCurve compositeCurve = (StepCompositeCurve) curve;
            CompositeCurve3 geometry = builder.buildCompositeCurve(compositeCurve.id());
            return sampledCurveEvaluator(geometry);
        } else if (curve instanceof StepBezierCurve) {
            StepBezierCurve bezier = (StepBezierCurve) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(bezier.id()));
        } else if (curve instanceof StepUniformCurve) {
            StepUniformCurve uniform = (StepUniformCurve) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(uniform.id()));
        } else if (curve instanceof StepQuasiUniformCurve) {
            StepQuasiUniformCurve quasiUniform = (StepQuasiUniformCurve) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(quasiUniform.id()));
        } else if (curve instanceof StepPiecewiseBezierCurve) {
            StepPiecewiseBezierCurve piecewiseBezier = (StepPiecewiseBezierCurve) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(piecewiseBezier.id()));
        } else if (curve instanceof StepOffsetCurve3D) {
            StepOffsetCurve3D offsetCurve3D = (StepOffsetCurve3D) curve;
            return sampledCurveEvaluator(builder.buildOffsetCurve3(offsetCurve3D.id()));
        } else if (curve instanceof StepConicCurve) {
            StepConicCurve conic = (StepConicCurve) curve;
            List<CartesianPoint> points = ConicSamplingHelper.sampleConicCurvePoints(conic, builder);
            if (points == null || points.size() < 2) return null;
            return sampledCurveEvaluator(new Polyline3(points));
        } else if (curve instanceof StepOrientedCurve) {
            StepOrientedCurve orientedCurve = (StepOrientedCurve) curve;
            return curveEvaluator(orientedCurve.curveElement(), builder);
        } else if (curve instanceof StepGeometricReplica) {
            StepGeometricReplica replica = (StepGeometricReplica) curve;
            return curveEvaluator(replica.parent(), builder);
        } else if (curve instanceof StepBSplineCurve) {
            StepBSplineCurve bspline = (StepBSplineCurve) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(bspline.id()));
        } else if (curve instanceof StepSeamCurve) {
            StepSeamCurve seamCurve = (StepSeamCurve) curve;
            return sampledCurveEvaluator(builder.buildSeamCurve(seamCurve.id()).curve3d());
        } else if (curve instanceof StepCircle2D) {
            StepCircle2D circle2D = (StepCircle2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(circle2D.id()));
        } else if (curve instanceof StepEllipse2D) {
            StepEllipse2D ellipse2D = (StepEllipse2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(ellipse2D.id()));
        } else if (curve instanceof StepPolyline2D) {
            StepPolyline2D polyline2D = (StepPolyline2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(polyline2D.id()));
        } else if (curve instanceof StepTrimmedCurve2D) {
            StepTrimmedCurve2D trimmedCurve2D = (StepTrimmedCurve2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(trimmedCurve2D.id()));
        } else if (curve instanceof StepCompositeCurve2D) {
            StepCompositeCurve2D compositeCurve2D = (StepCompositeCurve2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(compositeCurve2D.id()));
        } else if (curve instanceof StepBezierCurve2D) {
            StepBezierCurve2D bezier2D = (StepBezierCurve2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(bezier2D.id()));
        } else if (curve instanceof StepQuasiUniformCurve2D) {
            StepQuasiUniformCurve2D quasiUniform2D = (StepQuasiUniformCurve2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(quasiUniform2D.id()));
        } else if (curve instanceof StepUniformCurve2D) {
            StepUniformCurve2D uniform2D = (StepUniformCurve2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(uniform2D.id()));
        } else if (curve instanceof StepPiecewiseBezierCurve2D) {
            StepPiecewiseBezierCurve2D piecewiseBezier2D = (StepPiecewiseBezierCurve2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(piecewiseBezier2D.id()));
        } else if (curve instanceof StepIndexedPolyCurve2D) {
            StepIndexedPolyCurve2D polyCurve2D = (StepIndexedPolyCurve2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(polyCurve2D.id()));
        } else if (curve instanceof StepDegenerateCurve2D) {
            StepDegenerateCurve2D degenerateCurve2D = (StepDegenerateCurve2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(degenerateCurve2D.id()));
        } else if (curve instanceof StepBSplineCurve2D) {
            StepBSplineCurve2D bspline2D = (StepBSplineCurve2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(bspline2D.id()));
        } else if (curve instanceof StepRationalBSplineCurve2D) {
            StepRationalBSplineCurve2D rationalBspline2D = (StepRationalBSplineCurve2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(rationalBspline2D.id()));
        } else if (curve instanceof StepLine2D) {
            StepLine2D line2D = (StepLine2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(line2D.id()));
        } else if (curve instanceof StepCurve2D) {
            StepCurve2D curve2D = (StepCurve2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(curve2D.id()));
        } else if (curve instanceof StepHyperbola2D) {
            StepHyperbola2D hyperbola2D = (StepHyperbola2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(hyperbola2D.id()));
        } else if (curve instanceof StepParabola2D) {
            StepParabola2D parabola2D = (StepParabola2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(parabola2D.id()));
        } else if (curve instanceof StepOffsetCurve2D) {
            StepOffsetCurve2D offsetCurve2D = (StepOffsetCurve2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(offsetCurve2D.id()));
        } else if (curve instanceof StepClothoid) {
            StepClothoid clothoid = (StepClothoid) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(clothoid.id()));
        } else if (curve instanceof StepIndexedPolyCurve) {
            StepIndexedPolyCurve polyCurve = (StepIndexedPolyCurve) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(polyCurve.id()));
        } else if (curve instanceof StepDegenerateCurve) {
            StepDegenerateCurve degenerate = (StepDegenerateCurve) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(degenerate.id()));
        } else if (curve instanceof StepBSplineCurveWithKnotsAndBreakpoints) {
            StepBSplineCurveWithKnotsAndBreakpoints splineBreak = (StepBSplineCurveWithKnotsAndBreakpoints) curve;
            return sampledCurveEvaluator(builder.buildBSplineCurveWithBreakpoints(splineBreak.id()));
        } else if (curve instanceof StepCompositeCurveOnSurface) {
            StepCompositeCurveOnSurface compositeOnSurface = (StepCompositeCurveOnSurface) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(compositeOnSurface.id()));
        } else if (curve instanceof StepCompositeCurveOnSurface3D) {
            StepCompositeCurveOnSurface3D compositeOnSurface3D = (StepCompositeCurveOnSurface3D) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(compositeOnSurface3D.id()));
        } else if (curve instanceof StepLineSegment) {
            StepLineSegment lineSeg = (StepLineSegment) curve;
            List<CartesianPoint> pts = List.of(
                    builder.buildPoint(lineSeg.startPoint().id()),
                    builder.buildPoint(lineSeg.endPoint().id())
            );
            return sampledCurveEvaluator(new Polyline3(pts));
        } else if (curve instanceof StepPath) {
            StepPath path = (StepPath) curve;
            return sampledCurveEvaluator(builder.buildPath(path.id()));
        } else if (curve instanceof StepOpenPath) {
            StepOpenPath openPath = (StepOpenPath) curve;
            return sampledCurveEvaluator(builder.buildPath(openPath.id()));
        } else if (curve instanceof StepSubpath) {
            StepSubpath subpath = (StepSubpath) curve;
            return sampledCurveEvaluator(builder.buildPath(subpath.id()));
        } else if (curve instanceof StepOrientedPath) {
            StepOrientedPath orientedPath = (StepOrientedPath) curve;
            return sampledCurveEvaluator(builder.buildPath(orientedPath.id()));
        } else if (curve instanceof StepEdgeCurve) {
            StepEdgeCurve edgeCurve = (StepEdgeCurve) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(edgeCurve.id()));
        } else if (curve instanceof StepSurfacedEdgeCurve) {
            StepSurfacedEdgeCurve surfacedEdge = (StepSurfacedEdgeCurve) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(surfacedEdge.id()));
        } else if (curve instanceof StepAnnotationCurveOccurrence) {
            StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) curve;
            return curveEvaluator(occurrence.item(), builder);
        } else if (curve instanceof StepDimensionCurve) {
            StepDimensionCurve dimensionCurve = (StepDimensionCurve) curve;
            return curveEvaluator(dimensionCurve.item(), builder);
        } else if (curve instanceof StepLeaderCurve) {
            StepLeaderCurve leaderCurve = (StepLeaderCurve) curve;
            return curveEvaluator(leaderCurve.item(), builder);
        } else if (curve instanceof StepProjectionCurve) {
            StepProjectionCurve projectionCurve = (StepProjectionCurve) curve;
            return curveEvaluator(projectionCurve.item(), builder);
        } else if (curve instanceof StepDraughtingAnnotationOccurrence) {
            StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) curve;
            return curveEvaluator(annotationOccurrence.item(), builder);
        } else if (curve instanceof StepTerminatorSymbol) {
            StepTerminatorSymbol terminatorSymbol = (StepTerminatorSymbol) curve;
            return curveEvaluator(terminatorSymbol.annotatedCurve(), builder);
        } else if (curve instanceof StepCurve) {
            StepCurve abstractCurve = (StepCurve) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(abstractCurve.id()));
        } else if (curve instanceof StepBoundedCurve) {
            StepBoundedCurve boundedCurve = (StepBoundedCurve) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(boundedCurve.id()));
        } else if (curve instanceof StepMappedItem) {
            StepMappedItem mappedItem = (StepMappedItem) curve;
            return curveEvaluator(mappedItem.mappingTarget(), builder);
        } else {
            return null;
        }
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
        return close(bottom.get(0), left.get(0))
                && close(bottom.get(bottom.size() - 1), right.get(0))
                && close(top.get(0), left.get(left.size() - 1))
                && close(top.get(top.size() - 1), right.get(right.size() - 1));
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
            return List.of(points.get(0));
        }
        List<Double> lengths = new ArrayList<>(points.size());
        lengths.add(0.0);
        for (int i = 1; i < points.size(); i++) {
            lengths.add(lengths.get(i - 1) + points.get(i - 1).distanceTo(points.get(i)));
        }
        double total = lengths.get(lengths.size() - 1);
        if (total <= Epsilon.EPS) {
            return java.util.Collections.nCopies(segments + 1, points.get(0));
        }
        List<CartesianPoint> result = new ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double target = total * i / segments;
            result.add(pointAtDistance(points, lengths, target));
        }
        result.set(0, points.get(0));
        result.set(result.size() - 1, points.get(points.size() - 1));
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
        return points.get(points.size() - 1);
    }

    private static CartesianPoint interpolate(CartesianPoint a, CartesianPoint b, double alpha) {
        return new CartesianPoint(
                a.x() * (1.0 - alpha) + b.x() * alpha,
                a.y() * (1.0 - alpha) + b.y() * alpha,
                a.z() * (1.0 - alpha) + b.z() * alpha
        );
    }

    

    private static List<FaceBound> buildFaceBounds(StepFaceEntity stepFace, StepCadBuilder builder) {
        List<FaceBound> bounds = stepFace.bounds().stream().map(bound -> builder.buildFaceBound(bound.id())).collect(Collectors.toList());
        if (bounds.stream().noneMatch(FaceBound::outer) && bounds.size() == 1) {
            FaceBound bound = bounds.get(0);
            return List.of(FaceBound.outer(bound.loop(), bound.orientation()));
        }
        return bounds;
    }

    private static StepEntity faceGeometry(StepFaceEntity stepFace) {
        if (stepFace instanceof StepAdvancedFace) {
            StepAdvancedFace advancedFace = (StepAdvancedFace) stepFace;
            return advancedFace.faceGeometry();
        }
        if (stepFace instanceof StepFaceSurface) {
            StepFaceSurface faceSurface = (StepFaceSurface) stepFace;
            return faceSurface.faceGeometry();
        }
        if (stepFace instanceof StepOrientedFace) {
            StepOrientedFace orientedFace = (StepOrientedFace) stepFace;
            return faceGeometry(orientedFace.faceElement());
        }
        return null;
    }

    public static String surfaceTypeName(StepEntity geometry) {
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
        if (geometry instanceof StepConicCurve) {
            StepConicCurve conic = (StepConicCurve) geometry;
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
        if (geometry instanceof StepFaceBound) {
            StepFaceBound faceBound = (StepFaceBound) geometry;
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
        if (geometry instanceof StepSweptAreaSolid) {
            StepSweptAreaSolid sweptAreaSolid = (StepSweptAreaSolid) geometry;
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
        if (geometry instanceof StepCsgPrimitive) {
            StepCsgPrimitive primitive = (StepCsgPrimitive) geometry;
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
        if (geometry instanceof StepGeometricReplica) {
            StepGeometricReplica replica = (StepGeometricReplica) geometry;
            return replica.entityName();
        }
        return geometry.getClass().getSimpleName();
    }

    private static String surfaceTypeNameForGeometry(SurfaceGeometry surface) {
        if (surface instanceof Plane) return "PLANE";
        else if (surface instanceof CylindricalSurface) return "CYLINDRICAL_SURFACE";
        else if (surface instanceof ConicalSurface) return "CONICAL_SURFACE";
        else if (surface instanceof SphericalSurface) return "SPHERICAL_SURFACE";
        else if (surface instanceof ToroidalSurface) return "TOROIDAL_SURFACE";
        else if (surface instanceof BSplineSurface3) return "BSPLINE_SURFACE";
        else if (surface instanceof RationalBSplineSurface3) return "RATIONAL_BSPLINE_SURFACE";
        else if (surface instanceof RuledSurface3) return "RULED_SURFACE";
        else if (surface instanceof SurfaceOfRevolution3) return "SURFACE_OF_REVOLUTION";
        else if (surface instanceof OffsetSurface3) return "OFFSET_SURFACE";
        else if (surface instanceof SurfaceOfLinearExtrusion3) return "SURFACE_OF_LINEAR_EXTRUSION";
        else if (surface instanceof SurfaceOfConstantRadius3) return "SURFACE_OF_CONSTANT_RADIUS";
        else if (surface instanceof ParaboloidSurface) return "PARABOLOID_SURFACE";
        else if (surface instanceof HyperboloidSurface) return "HYPERBOLOID_SURFACE";
        else if (surface instanceof SurfaceOfTranslation3) return "SURFACE_OF_TRANSLATION";
        else if (surface instanceof SurfaceOfProjection3) return "SURFACE_OF_PROJECTION";
        else throw new IllegalArgumentException("Unknown surface type: " + surface.getClass().getSimpleName());
    }

    /**
     * Determines the geometry type name for error reporting, distinguishing
     * between open shells (SHELL) and closed shells/solids (SOLID).
     *
     * @param entity the STEP entity, may be null
     * @return "SHELL" for open shells, "SOLID" for closed shells/solids,
     *         or surfaceTypeName for other entities
     */
    public static String geometryTypeName(StepEntity entity) {
        if (entity == null) {
            return "SOLID"; // Default for unknown entities in solid context
        }
        // Open shell types -> SHELL (surface, not a closed volume)
        if (entity instanceof StepOpenShell
                || entity instanceof StepSurfacedOpenShell
                || entity instanceof StepOrientedOpenShell) {
            return "SHELL";
        }
        // Closed shell types and solid-like entities -> SOLID
        if (entity instanceof StepClosedShell
                || entity instanceof StepOrientedClosedShell
                || entity instanceof StepManifoldSolidBrep
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
                || entity instanceof StepSurfacePatch
                || entity instanceof StepExtrudedFaceSolid
                || entity instanceof StepRevolvedFaceSolid
                || entity instanceof StepSweptFaceSolid
                || entity instanceof StepCylinderVolume
                || entity instanceof StepSphereVolume
                || entity instanceof StepTorusVolume
                || entity instanceof StepPrismVolume
                || entity instanceof StepRightCircularConeVolume) {
            return "SOLID";
        }
        // For other entities, use the detailed type name from surfaceTypeName
        return surfaceTypeName(entity);
    }


    private static boolean faceSameSense(StepFaceEntity stepFace) {
        if (stepFace instanceof StepAdvancedFace) {
            StepAdvancedFace advancedFace = (StepAdvancedFace) stepFace;
            return advancedFace.sameSense();
        }
        if (stepFace instanceof StepFaceSurface) {
            StepFaceSurface faceSurface = (StepFaceSurface) stepFace;
            return faceSurface.sameSense();
        }
        if (stepFace instanceof StepOrientedFace) {
            StepOrientedFace orientedFace = (StepOrientedFace) stepFace;
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
        if (bound.loop() instanceof VertexLoop) {
            VertexLoop vertexLoop = (VertexLoop) bound.loop();
            return List.of(vertexLoop.vertex().point());
        }
        if (bound.loop() instanceof PolyLoop) {
            PolyLoop polyLoop = (PolyLoop) bound.loop();
            List<CartesianPoint> sampled = new ArrayList<>(polyLoop.points());
            if (!sampled.isEmpty() && sampled.get(0).distanceTo(sampled.get(sampled.size() - 1)) > 1.0e-9) {
                sampled.add(sampled.get(0));
            }
            return bound.orientation() ? sampled : reverseClosedLoop(sampled);
        }
        if (!(bound.loop() instanceof EdgeLoop)) {
            throw new UnsupportedGeometryException("preview export requires EDGE_LOOP, POLY_LOOP or VERTEX_LOOP");
        }
        EdgeLoop edgeLoop = (EdgeLoop) bound.loop();
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
        if (!sampled.isEmpty() && sampled.get(0).distanceTo(sampled.get(sampled.size() - 1)) > 1.0e-9) {
            sampled.add(sampled.get(0));
        }
        return bound.orientation() ? sampled : reverseClosedLoop(sampled);
    }

    private static void collectTopologyEdges(Face face, Set<Edge> edges) {
        for (FaceBound bound : face.bounds()) {
            if (bound.loop() instanceof EdgeLoop) {
                EdgeLoop edgeLoop = (EdgeLoop) bound.loop();
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
        if (reversed.get(0).equals(reversed.get(reversed.size() - 1))) {
            T start = reversed.remove(reversed.size() - 1);
            java.util.Collections.reverse(reversed);
            reversed.add(reversed.get(0));
            reversed.set(0, start);
            reversed.set(reversed.size() - 1, start);
            return reversed;
        }
        java.util.Collections.reverse(reversed);
        return reversed;
    }

    public static List<CartesianPoint> sampleOrientedEdge(OrientedEdge orientedEdge) {
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
            if (!(entity instanceof StepEdgeCurve)) {
                throw ex;
            }
            StepEdgeCurve edge = (StepEdgeCurve) entity;
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

    public static EdgePayload buildEdgePayload(
            int edgeId,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata
    ) {
        List<CartesianPoint> polyline = sampleEdgePreview(edgeId, resolved, builder);
        StepEntity entity = resolved.get(edgeId);
        ColorPayload color = resolveEdgeColor(edgeId, metadata);
        if (entity instanceof StepEdgeCurve) {
            StepEdgeCurve edge = (StepEdgeCurve) entity;
            CartesianPoint start = pointFromStep(edge.start().point());
            CartesianPoint end = pointFromStep(edge.end().point());
            return new EdgePayload(
                    edgeId,
                    PayloadConversionHelper.toPointPayloads(polyline),
                    edgeCurvePayload(edge.edgeGeometry(), start, end, edge.sameSense(), builder),
                    color
            );
        }
        if (entity instanceof StepSeamEdge) {
            StepSeamEdge seamEdge = (StepSeamEdge) entity;
            // Seam edge: curve geometry is resolved at the same ID in entitiesById.
            StepEntity actual = resolved.get(seamEdge.id());
            if (actual != null && actual != seamEdge) {
                Edge edge = builder.buildEdge(edgeId);
                CartesianPoint start = edge.start().point();
                CartesianPoint end = edge.end().point();
                EdgeCurvePayload curvePayload = edgeCurvePayload(actual, start, end, true, builder);
                if (curvePayload != null) {
                    return new EdgePayload(edgeId, PayloadConversionHelper.toPointPayloads(polyline), curvePayload, color);
                }
            }
        }
        if (entity instanceof StepFilletEdge) {
            StepFilletEdge filletEdge = (StepFilletEdge) entity;
            // Fillet edge: sample the original edge geometry for preview.
            StepEntity original = filletEdge.originalEdge();
            if (original != null) {
                EdgeCurvePayload curvePayload = edgeCurvePayload(original, polyline.get(0), polyline.get(polyline.size() - 1), true, builder);
                if (curvePayload != null) {
                    return new EdgePayload(edgeId, PayloadConversionHelper.toPointPayloads(polyline), curvePayload, color);
                }
            }
        }
        if (entity instanceof StepChamferEdge) {
            StepChamferEdge chamferEdge = (StepChamferEdge) entity;
            // Chamfer edge: sample the original edge geometry for preview.
            StepEntity original = chamferEdge.originalEdge();
            if (original != null) {
                EdgeCurvePayload curvePayload = edgeCurvePayload(original, polyline.get(0), polyline.get(polyline.size() - 1), true, builder);
                if (curvePayload != null) {
                    return new EdgePayload(edgeId, PayloadConversionHelper.toPointPayloads(polyline), curvePayload, color);
                }
            }
        }
        return new EdgePayload(edgeId, PayloadConversionHelper.toPointPayloads(polyline), null, color);
    }

    private static ColorPayload resolveEdgeColor(int edgeId, StepMetadataExtractor metadata) {
        StepMetadataExtractor.DisplayMetadata meta = metadata.forItem(edgeId);
        return meta.rgb() != null ? PayloadConversionHelper.toColorPayload(meta.rgb()) : null;
    }

    private static EdgePayload buildTopologyEdgePayload(int edgeId, Edge edge) {
        return new EdgePayload(
                edgeId,
                PayloadConversionHelper.toPointPayloads(sampleEdge(edge.start().point(), edge.end().point(), edge.curve(), edge.sameSense())),
                null,
                null
        );
    }

    private static EdgePayload toPolylineEdgePayload(StepPolyline polyline) {
        List<CartesianPoint> points = polyline.points().stream()
                .map(StepPreviewJsonExporter::pointFromStep)
                .collect(Collectors.toList());
        return new EdgePayload(polyline.id(), PayloadConversionHelper.toPointPayloads(points), null, null);
    }

    private static EdgePayload toPolyLoopEdgePayload(StepPolyLoop polyLoop) {
        List<CartesianPoint> points = polyLoop.polygon().stream()
                .map(StepPreviewJsonExporter::pointFromStep)
                .collect(Collectors.toList());
        List<CartesianPoint> closed = new ArrayList<>(points);
        if (!closed.isEmpty() && closed.get(0).distanceTo(closed.get(closed.size() - 1)) > 1.0e-9) {
            closed.add(closed.get(0));
        }
        return new EdgePayload(polyLoop.id(), PayloadConversionHelper.toPointPayloads(List.copyOf(closed)), null, null);
    }

    public static EdgePayload sampledCurveEdgePayload(StepEntity item, StepCadBuilder builder) {
        List<CartesianPoint> points = sampleLooseEdgePoints(item, builder);
        if (points == null || points.size() < 2) {
            return null;
        }
        return new EdgePayload(item.id(), PayloadConversionHelper.toPointPayloads(points), sampledCurvePayload(item, builder), null);
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
            if (item instanceof StepCircle) {
            StepCircle circle = (StepCircle) item;
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
            if (item instanceof StepEllipse) {
            StepEllipse ellipse = (StepEllipse) item;
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
        if (item instanceof StepConicCurve) {
            StepConicCurve conic = (StepConicCurve) item;
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
        if (item instanceof StepSurfaceCurve) {
            StepSurfaceCurve surfaceCurve = (StepSurfaceCurve) item;
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
        if (item instanceof StepGeometricReplica && "CURVE_REPLICA".equals(((StepGeometricReplica) item).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) item;
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
        if (item instanceof StepOrientedPath) {
            StepOrientedPath orientedPath = (StepOrientedPath) item;
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
        if (item instanceof StepSurfaceCurve) {
            StepSurfaceCurve surfaceCurve = (StepSurfaceCurve) item;
            return previewCurveTypeName(surfaceCurve.curve3d());
        }
        if (item instanceof StepSeamCurve) {
            StepSeamCurve seamCurve = (StepSeamCurve) item;
            return previewCurveTypeName(seamCurve.curve3d());
        }
        if (item instanceof StepTrimmedCurve) {
            StepTrimmedCurve trimmedCurve = (StepTrimmedCurve) item;
            return previewCurveTypeName(trimmedCurve.basisCurve());
        }
        if (item instanceof StepOffsetCurve2D) {
            StepOffsetCurve2D offsetCurve2D = (StepOffsetCurve2D) item;
            return previewCurveTypeName(offsetCurve2D.basisCurve());
        }
        if (item instanceof StepOffsetCurve3D) {
            StepOffsetCurve3D offsetCurve3D = (StepOffsetCurve3D) item;
            return previewCurveTypeName(offsetCurve3D.basisCurve());
        }
        if (item instanceof StepOrientedCurve) {
            StepOrientedCurve orientedCurve = (StepOrientedCurve) item;
            return previewCurveTypeName(orientedCurve.curveElement());
        }
        if (item instanceof StepAnnotationCurveOccurrence) {
            StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) item;
            return previewCurveTypeName(occurrence.item());
        }
        if (item instanceof StepDimensionCurve) {
            StepDimensionCurve dimensionCurve = (StepDimensionCurve) item;
            return previewCurveTypeName(dimensionCurve.item());
        }
        if (item instanceof StepLeaderCurve) {
            StepLeaderCurve leaderCurve = (StepLeaderCurve) item;
            return previewCurveTypeName(leaderCurve.item());
        }
        if (item instanceof StepProjectionCurve) {
            StepProjectionCurve projectionCurve = (StepProjectionCurve) item;
            return previewCurveTypeName(projectionCurve.item());
        }
        if (item instanceof StepDraughtingAnnotationOccurrence) {
            StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) item;
            return previewCurveTypeName(annotationOccurrence.item());
        }
        if (item instanceof StepTerminatorSymbol) {
            StepTerminatorSymbol terminatorSymbol = (StepTerminatorSymbol) item;
            return previewCurveTypeName(terminatorSymbol.annotatedCurve());
        }
        if (item instanceof StepGeometricReplica && "CURVE_REPLICA".equals(((StepGeometricReplica) item).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) item;
            return previewCurveTypeName(replica.parent());
        }
        if (item instanceof StepTrimmedCurve2D) {
            StepTrimmedCurve2D trimmedCurve2D = (StepTrimmedCurve2D) item;
            return previewCurveTypeName(trimmedCurve2D.basisCurve());
        }
        return null;
    }

    private static Integer previewCurveBasisStepId(StepEntity item) {
        if (item instanceof StepSurfaceCurve) {
            StepSurfaceCurve surfaceCurve = (StepSurfaceCurve) item;
            return surfaceCurve.curve3d().id();
        }
        if (item instanceof StepSeamCurve) {
            StepSeamCurve seamCurve = (StepSeamCurve) item;
            return seamCurve.curve3d().id();
        }
        if (item instanceof StepTrimmedCurve) {
            StepTrimmedCurve trimmedCurve = (StepTrimmedCurve) item;
            return trimmedCurve.basisCurve().id();
        }
        if (item instanceof StepOffsetCurve2D) {
            StepOffsetCurve2D offsetCurve2D = (StepOffsetCurve2D) item;
            return offsetCurve2D.basisCurve().id();
        }
        if (item instanceof StepOffsetCurve3D) {
            StepOffsetCurve3D offsetCurve3D = (StepOffsetCurve3D) item;
            return offsetCurve3D.basisCurve().id();
        }
        if (item instanceof StepOrientedCurve) {
            StepOrientedCurve orientedCurve = (StepOrientedCurve) item;
            return orientedCurve.curveElement().id();
        }
        if (item instanceof StepAnnotationCurveOccurrence) {
            StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) item;
            return occurrence.item().id();
        }
        if (item instanceof StepDimensionCurve) {
            StepDimensionCurve dimensionCurve = (StepDimensionCurve) item;
            return dimensionCurve.item().id();
        }
        if (item instanceof StepLeaderCurve) {
            StepLeaderCurve leaderCurve = (StepLeaderCurve) item;
            return leaderCurve.item().id();
        }
        if (item instanceof StepProjectionCurve) {
            StepProjectionCurve projectionCurve = (StepProjectionCurve) item;
            return projectionCurve.item().id();
        }
        if (item instanceof StepDraughtingAnnotationOccurrence) {
            StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) item;
            return annotationOccurrence.item().id();
        }
        if (item instanceof StepTerminatorSymbol) {
            StepTerminatorSymbol terminatorSymbol = (StepTerminatorSymbol) item;
            return terminatorSymbol.annotatedCurve().id();
        }
        if (item instanceof StepGeometricReplica && "CURVE_REPLICA".equals(((StepGeometricReplica) item).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) item;
            return replica.parent().id();
        }
        if (item instanceof StepTrimmedCurve2D) {
            StepTrimmedCurve2D trimmedCurve2D = (StepTrimmedCurve2D) item;
            return trimmedCurve2D.basisCurve().id();
        }
        return null;
    }

    private static Boolean previewCurveOrientation(StepEntity item) {
        if (item instanceof StepOrientedCurve) {
            StepOrientedCurve orientedCurve = (StepOrientedCurve) item;
            return orientedCurve.orientation();
        }
        return null;
    }

    private static Boolean previewCurveSenseAgreement(StepEntity item) {
        if (item instanceof StepTrimmedCurve) {
            StepTrimmedCurve trimmedCurve = (StepTrimmedCurve) item;
            return trimmedCurve.senseAgreement();
        }
        if (item instanceof StepTrimmedCurve2D) {
            StepTrimmedCurve2D trimmedCurve2D = (StepTrimmedCurve2D) item;
            return trimmedCurve2D.senseAgreement();
        }
        return null;
    }

    private static Double previewCurveOffsetDistance(StepEntity item) {
        if (item instanceof StepOffsetCurve2D) {
            StepOffsetCurve2D offsetCurve2D = (StepOffsetCurve2D) item;
            return offsetCurve2D.distance();
        }
        if (item instanceof StepOffsetCurve3D) {
            StepOffsetCurve3D offsetCurve3D = (StepOffsetCurve3D) item;
            return offsetCurve3D.distance();
        }
        return null;
    }

    private static Boolean previewCurveSelfIntersect(StepEntity item) {
        if (item instanceof StepOffsetCurve2D) {
            StepOffsetCurve2D offsetCurve2D = (StepOffsetCurve2D) item;
            return offsetCurve2D.selfIntersect();
        }
        if (item instanceof StepOffsetCurve3D) {
            StepOffsetCurve3D offsetCurve3D = (StepOffsetCurve3D) item;
            return offsetCurve3D.selfIntersect();
        }
        if (item instanceof StepCompositeCurveOnSurface) {
            StepCompositeCurveOnSurface compositeOnSurface = (StepCompositeCurveOnSurface) item;
            return compositeOnSurface.selfIntersect();
        }
        return null;
    }

    private static List<Double> previewCurveRefDirection(StepEntity item) {
        if (item instanceof StepOffsetCurve3D) {
            StepOffsetCurve3D offsetCurve3D = (StepOffsetCurve3D) item;
            return List.copyOf(offsetCurve3D.refDirection().directionRatios());
        }
        return null;
    }

    private static Double previewCurveTransformScale(StepEntity item) {
        if (item instanceof StepGeometricReplica && "CURVE_REPLICA".equals(((StepGeometricReplica) item).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) item;
            return replica.transformation().scale();
        }
        return null;
    }

    private static String previewCurveMasterRepresentation(StepEntity item) {
        StepEntity semanticCurve = previewCurveSemanticItem(item);
        if (semanticCurve instanceof StepSurfaceCurve) {
            StepSurfaceCurve surfaceCurve = (StepSurfaceCurve) semanticCurve;
            return surfaceCurve.masterRepresentation();
        }
        if (semanticCurve instanceof StepSeamCurve) {
            StepSeamCurve seamCurve = (StepSeamCurve) semanticCurve;
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
            if (associated instanceof StepPcurve) {
            StepPcurve pcurve = (StepPcurve) associated;
                surfaceTypes.add(surfaceTypeName(pcurve.basisSurface()));
            } else if (associated instanceof StepDegeneratePcurve) {
            StepDegeneratePcurve pcurve = (StepDegeneratePcurve) associated;
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
            if (associated instanceof StepPcurve) {
            StepPcurve pcurve = (StepPcurve) associated;
                surfaceIds.add(pcurve.basisSurface().id());
            } else if (associated instanceof StepDegeneratePcurve) {
            StepDegeneratePcurve pcurve = (StepDegeneratePcurve) associated;
                surfaceIds.add(pcurve.basisSurface().id());
            }
        }
        return surfaceIds.isEmpty() ? null : List.copyOf(surfaceIds);
    }

    private static List<StepEntity> previewCurveAssociatedGeometry(StepEntity item) {
        StepEntity semanticCurve = previewCurveSemanticItem(item);
        if (semanticCurve instanceof StepSurfaceCurve) {
            StepSurfaceCurve surfaceCurve = (StepSurfaceCurve) semanticCurve;
            return surfaceCurve.associatedGeometry();
        }
        if (semanticCurve instanceof StepSeamCurve) {
            StepSeamCurve seamCurve = (StepSeamCurve) semanticCurve;
            return seamCurve.associatedGeometry();
        }
        return null;
    }

    private static StepEntity previewCurveSemanticItem(StepEntity item) {
        StepEntity current = item;
        while (true) {
            if (current instanceof StepOrientedCurve) {
            StepOrientedCurve orientedCurve = (StepOrientedCurve) current;
                current = orientedCurve.curveElement();
                continue;
            }
            if (current instanceof StepAnnotationCurveOccurrence) {
            StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) current;
                current = occurrence.item();
                continue;
            }
            if (current instanceof StepDimensionCurve) {
            StepDimensionCurve dimensionCurve = (StepDimensionCurve) current;
                current = dimensionCurve.item();
                continue;
            }
            if (current instanceof StepLeaderCurve) {
            StepLeaderCurve leaderCurve = (StepLeaderCurve) current;
                current = leaderCurve.item();
                continue;
            }
            if (current instanceof StepProjectionCurve) {
            StepProjectionCurve projectionCurve = (StepProjectionCurve) current;
                current = projectionCurve.item();
                continue;
            }
            if (current instanceof StepDraughtingAnnotationOccurrence) {
            StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) current;
                current = annotationOccurrence.item();
                continue;
            }
            if (current instanceof StepTerminatorSymbol) {
            StepTerminatorSymbol terminatorSymbol = (StepTerminatorSymbol) current;
                current = terminatorSymbol.annotatedCurve();
                continue;
            }
            if (current instanceof StepGeometricReplica && "CURVE_REPLICA".equals(((StepGeometricReplica) current).entityName())) {
                StepGeometricReplica replica = (StepGeometricReplica) current;
                current = replica.parent();
                continue;
            }
            return current;
        }
    }

    public static List<CartesianPoint> sampleLooseEdgePoints(StepEntity item, StepCadBuilder builder) {
        if (item instanceof StepAnnotationFillArea) {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) item;
            return sampleAnnotationFillAreaPoints(fillArea, builder);
        }
        if (item instanceof StepAnnotationFillAreaOccurrence) {
            StepAnnotationFillAreaOccurrence fillAreaOccurrence = (StepAnnotationFillAreaOccurrence) item;
            return sampleAnnotationFillAreaPoints(fillAreaOccurrence.item(), builder);
        }
        if (item instanceof StepEdgeBasedWireframeModel) {
            StepEdgeBasedWireframeModel wireframeModel = (StepEdgeBasedWireframeModel) item;
            return sampleWireframeBoundaryPoints(wireframeModel.boundaries(), builder);
        }
        if (item instanceof StepShellBasedWireframeModel) {
            StepShellBasedWireframeModel wireframeModel = (StepShellBasedWireframeModel) item;
            return sampleWireframeBoundaryPoints(wireframeModel.boundaries(), builder);
        }
        if (item instanceof StepAnnotationSymbol) {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) item;
            return sampleMappedAnnotationPoints(
                    annotationSymbol.mappingSource().mappedRepresentation(),
                    annotationSymbol.mappingSource().mappedOrigin(),
                    annotationSymbol.mappingTarget(),
                    builder
            );
        }
        if (item instanceof StepAnnotationText) {
            StepAnnotationText annotationText = (StepAnnotationText) item;
            return sampleMappedAnnotationPoints(
                    annotationText.mappingSource().mappedRepresentation(),
                    annotationText.mappingSource().mappedOrigin(),
                    annotationText.mappingTarget(),
                    builder
            );
        }
        if (item instanceof StepAnnotationTextCharacter) {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) item;
            return sampleMappedAnnotationPoints(
                    annotationTextCharacter.mappingSource().mappedRepresentation(),
                    annotationTextCharacter.mappingSource().mappedOrigin(),
                    annotationTextCharacter.mappingTarget(),
                    builder
            );
        }
        if (item instanceof StepGeometricReplica && "CURVE_REPLICA".equals(((StepGeometricReplica) item).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) item;
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
        if (item instanceof StepOrientedCurve) {
            StepOrientedCurve orientedCurve = (StepOrientedCurve) item;
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
        if (item instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) item;
            return sampleGeometricCollectionPoints(geometricSet.elements(), builder);
        }
        if (item instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) item;
            return sampleGeometricCollectionPoints(curveSet.elements(), builder);
        }
        if (item instanceof StepConnectedEdgeSet) {
            StepConnectedEdgeSet connectedEdgeSet = (StepConnectedEdgeSet) item;
            return sampleGeometricCollectionPoints(connectedEdgeSet.edges(), builder);
        }
        if (item instanceof StepWireShell) {
            StepWireShell wireShell = (StepWireShell) item;
            return sampleWireShellPoints(wireShell, builder);
        }
        if (item instanceof StepEdgeWire) {
            StepEdgeWire edgeWire = (StepEdgeWire) item;
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
                points.add(MathUtilityHelper.transformCartesian(point, matrix));
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

    public static List<CartesianPoint> sampleAnnotationFillAreaPoints(
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
            if (item instanceof StepLine) {
            StepLine line = (StepLine) item;
                return builder.buildLine(line.id());
            }
            if (item instanceof StepCircle) {
            StepCircle circle = (StepCircle) item;
                return builder.buildCircle(circle.id());
            }
            if (item instanceof StepEllipse) {
            StepEllipse ellipse = (StepEllipse) item;
                return builder.buildEllipse(ellipse.id());
            }
            if (item instanceof StepConicCurve) {
            StepConicCurve conic = (StepConicCurve) item;
                List<CartesianPoint> points = ConicSamplingHelper.sampleConicCurvePoints(conic, builder);
                return points == null ? null : new Polyline3(points);
            }
            if (item instanceof StepBezierCurve) {
            StepBezierCurve curve = (StepBezierCurve) item;
                return builder.buildCurveReference3(curve.id());
            }
            if (item instanceof StepUniformCurve) {
            StepUniformCurve curve = (StepUniformCurve) item;
                return builder.buildCurveReference3(curve.id());
            }
            if (item instanceof StepQuasiUniformCurve) {
            StepQuasiUniformCurve curve = (StepQuasiUniformCurve) item;
                return builder.buildCurveReference3(curve.id());
            }
            if (item instanceof StepPiecewiseBezierCurve) {
            StepPiecewiseBezierCurve curve = (StepPiecewiseBezierCurve) item;
                return builder.buildCurveReference3(curve.id());
            }
            if (item instanceof StepBSplineCurveWithKnots) {
            StepBSplineCurveWithKnots spline = (StepBSplineCurveWithKnots) item;
                return builder.buildBSplineCurve(spline.id());
            }
            if (item instanceof StepSurfaceCurve) {
            StepSurfaceCurve surfaceCurve = (StepSurfaceCurve) item;
                return builder.buildSurfaceCurve(surfaceCurve.id());
            }
            if (item instanceof StepSeamCurve) {
            StepSeamCurve seamCurve = (StepSeamCurve) item;
                return builder.buildSeamCurve(seamCurve.id());
            }
            if (item instanceof StepTrimmedCurve) {
            StepTrimmedCurve trimmedCurve = (StepTrimmedCurve) item;
                return builder.buildTrimmedCurve(trimmedCurve.id());
            }
            if (item instanceof StepPolyline) {
            StepPolyline polyline = (StepPolyline) item;
                return builder.buildPolyline(polyline.id());
            }
            if (item instanceof com.minicad.step.model.StepCompositeCurve) {
                com.minicad.step.model.StepCompositeCurve compositeCurve = (com.minicad.step.model.StepCompositeCurve) item;
                return builder.buildCompositeCurve(compositeCurve.id());
            }
            if (item instanceof com.minicad.step.model.StepCompositeCurveOnSurface) {
                com.minicad.step.model.StepCompositeCurveOnSurface compositeCurveOnSurface = (com.minicad.step.model.StepCompositeCurveOnSurface) item;
                return builder.buildCompositeCurve(compositeCurveOnSurface.id());
            }
            if (item instanceof com.minicad.step.model.StepRationalBSplineCurve) {
                com.minicad.step.model.StepRationalBSplineCurve spline = (com.minicad.step.model.StepRationalBSplineCurve) item;
                return builder.buildRationalBSplineCurve(spline.id());
            }
            if (item instanceof StepOffsetCurve2D) {
            StepOffsetCurve2D offsetCurve2D = (StepOffsetCurve2D) item;
                return liftCurve2(builder.buildOffsetCurve2(offsetCurve2D.id()));
            }
            if (item instanceof StepOffsetCurve3D) {
            StepOffsetCurve3D offsetCurve3D = (StepOffsetCurve3D) item;
                return builder.buildOffsetCurve3(offsetCurve3D.id());
            }
            if (item instanceof StepPcurve) {
            StepPcurve pcurve = (StepPcurve) item;
                Object built = builder.buildPcurve2(pcurve.id());
                if (built instanceof Curve2) {
                    Curve2 curve2 = (Curve2) built;
                    return liftCurve2(curve2);
                }
                return null;
            }
            if (item instanceof StepDegeneratePcurve) {
            StepDegeneratePcurve pcurve = (StepDegeneratePcurve) item;
                Object built = builder.buildPcurve2(pcurve.id());
                if (built instanceof Curve2) {
                    Curve2 curve2 = (Curve2) built;
                    return liftCurve2(curve2);
                }
                return null;
            }
            if (item instanceof StepOrientedCurve) {
            StepOrientedCurve orientedCurve = (StepOrientedCurve) item;
                return curveForLooseEdge(orientedCurve.curveElement(), builder);
            }
            if (item instanceof StepAnnotationCurveOccurrence) {
            StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) item;
                return curveForLooseEdge(occurrence.item(), builder);
            }
            if (item instanceof StepDimensionCurve) {
            StepDimensionCurve dimensionCurve = (StepDimensionCurve) item;
                return curveForLooseEdge(dimensionCurve.item(), builder);
            }
            if (item instanceof StepLeaderCurve) {
            StepLeaderCurve leaderCurve = (StepLeaderCurve) item;
                return curveForLooseEdge(leaderCurve.item(), builder);
            }
            if (item instanceof StepProjectionCurve) {
            StepProjectionCurve projectionCurve = (StepProjectionCurve) item;
                return curveForLooseEdge(projectionCurve.item(), builder);
            }
            if (item instanceof StepDraughtingAnnotationOccurrence) {
            StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) item;
                return curveForLooseEdge(annotationOccurrence.item(), builder);
            }
            if (item instanceof StepTerminatorSymbol) {
            StepTerminatorSymbol terminatorSymbol = (StepTerminatorSymbol) item;
                return curveForLooseEdge(terminatorSymbol.annotatedCurve(), builder);
            }
            if (item instanceof StepGeometricReplica && "CURVE_REPLICA".equals(((StepGeometricReplica) item).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) item;
                List<CartesianPoint> points = sampleLooseEdgePoints(replica, builder);
                return points == null ? null : new Polyline3(points);
            }
            if (item instanceof StepIndexedPolyCurve) {
            StepIndexedPolyCurve polyCurve = (StepIndexedPolyCurve) item;
                return builder.buildCurveReference3(polyCurve.id());
            }
            if (item instanceof StepClothoid) {
            StepClothoid clothoid = (StepClothoid) item;
                return builder.buildCurveReference3(clothoid.id());
            }
            if (item instanceof StepDegenerateCurve) {
            StepDegenerateCurve degenerate = (StepDegenerateCurve) item;
                return builder.buildCurveReference3(degenerate.id());
            }
            if (item instanceof StepBSplineCurve) {
            StepBSplineCurve bspline = (StepBSplineCurve) item;
                return builder.buildCurveReference3(bspline.id());
            }
            if (item instanceof StepCompositeCurveOnSurface) {
            StepCompositeCurveOnSurface compositeOnSurface = (StepCompositeCurveOnSurface) item;
                return builder.buildCurveReference3(compositeOnSurface.id());
            }
            if (item instanceof StepBSplineCurveWithKnotsAndBreakpoints) {
            StepBSplineCurveWithKnotsAndBreakpoints splineBreak = (StepBSplineCurveWithKnotsAndBreakpoints) item;
                return builder.buildBSplineCurveWithBreakpoints(splineBreak.id());
            }
            if (item instanceof StepLineSegment) {
            StepLineSegment lineSeg = (StepLineSegment) item;
                return new Polyline3(List.of(
                        builder.buildPoint(lineSeg.startPoint().id()),
                        builder.buildPoint(lineSeg.endPoint().id())
                ));
            }
            if (item instanceof StepEdgeCurve) {
            StepEdgeCurve edgeCurve = (StepEdgeCurve) item;
                return builder.buildCurveReference3(edgeCurve.id());
            }
            if (item instanceof StepSurfacedEdgeCurve) {
            StepSurfacedEdgeCurve surfacedEdge = (StepSurfacedEdgeCurve) item;
                return builder.buildCurveReference3(surfacedEdge.id());
            }
            if (item instanceof StepCompositeCurveOnSurface3D) {
            StepCompositeCurveOnSurface3D compositeOnSurface3D = (StepCompositeCurveOnSurface3D) item;
                return builder.buildCurveReference3(compositeOnSurface3D.id());
            }
            if (item instanceof StepPath) {
            StepPath path = (StepPath) item;
                return builder.buildPath(path.id());
            }
            if (item instanceof StepOpenPath) {
            StepOpenPath openPath = (StepOpenPath) item;
                return builder.buildPath(openPath.id());
            }
            if (item instanceof StepSubpath) {
            StepSubpath subpath = (StepSubpath) item;
                return builder.buildPath(subpath.id());
            }
            if (item instanceof StepSeamCurve) {
            StepSeamCurve seamCurve = (StepSeamCurve) item;
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
        if (item instanceof StepBoundedCurve) {
            StepBoundedCurve bounded = (StepBoundedCurve) item;
            return builder.buildCurveReference3(bounded.id());
        }
        if (item instanceof StepMappedItem) {
            StepMappedItem mappedItem = (StepMappedItem) item;
            return curveForLooseEdge(mappedItem.mappingTarget(), builder);
        }
        return null;
    }


    public static List<CartesianPoint> sampleLooseCurve(Curve3 curve) {
        if (curve instanceof TrimmedCurve3) {
            TrimmedCurve3 trimmedCurve = (TrimmedCurve3) curve;
            return Curve3SamplingHelper.sampleTrimmedCurve3(trimmedCurve, 72);
        }
        if (curve instanceof SurfaceCurve3) {
            SurfaceCurve3 surfaceCurve = (SurfaceCurve3) curve;
            return sampleLooseCurve(surfaceCurve.curve3d());
        }
        if (curve instanceof Polyline3) {
            Polyline3 polyline = (Polyline3) curve;
            return polyline.points();
        }
        if (curve instanceof CompositeCurve3) {
            CompositeCurve3 compositeCurve = (CompositeCurve3) curve;
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
        List<Point2> points2 = Curve2SamplingHelper.sampleLooseCurve2(curve);
        List<CartesianPoint> points3 = new ArrayList<>(points2.size());
        for (Point2 point : points2) {
            points3.add(new CartesianPoint(point.x(), point.y(), 0.0));
        }
        return new Polyline3(List.copyOf(points3));
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
            if (edgeGeometry instanceof StepCircle) {
            StepCircle circle = (StepCircle) edgeGeometry;
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
                        Curve3SamplingHelper.arcSweep(startAngle, endAngle, start.distanceTo(end) <= Epsilon.EPS, naturalForward)
                );
            }
            if (edgeGeometry instanceof StepEllipse) {
            StepEllipse ellipse = (StepEllipse) edgeGeometry;
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
                        Curve3SamplingHelper.arcSweep(startAngle, endAngle, start.distanceTo(end) <= Epsilon.EPS, naturalForward)
                );
            }
            if (edgeGeometry instanceof StepBSplineCurveWithKnots) {
            StepBSplineCurveWithKnots bspline = (StepBSplineCurveWithKnots) edgeGeometry;
                BSplineCurve3 geometry = builder.buildBSplineCurve(bspline.id());
                return newBSplineCurvePayload(edgeGeometry.id(), geometry);
            }
            if (edgeGeometry instanceof StepBSplineCurve) {
            StepBSplineCurve bspline = (StepBSplineCurve) edgeGeometry;
                BSplineCurve3 geometry = builder.buildBSplineCurve(bspline.id());
                return newBSplineCurvePayload(edgeGeometry.id(), geometry);
            }
            if (edgeGeometry instanceof StepRationalBSplineCurve) {
            StepRationalBSplineCurve rational = (StepRationalBSplineCurve) edgeGeometry;
                RationalBSplineCurve3 geometry = builder.buildRationalBSplineCurve3(rational.id());
                return newRationalBSplineCurvePayload(edgeGeometry.id(), geometry);
            }
            if (edgeGeometry instanceof StepPolyline) {
            StepPolyline polyline = (StepPolyline) edgeGeometry;
                Polyline3 geometry = builder.buildPolyline(polyline.id());
                return newPolylineCurvePayload(edgeGeometry.id(), geometry, start, end);
            }
            if (edgeGeometry instanceof StepLine) {
            StepLine line = (StepLine) edgeGeometry;
                return newLineCurvePayload(edgeGeometry.id(), builder, line, start, end);
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


    private static EdgeCurvePayload newBSplineCurvePayload(int stepId, BSplineCurve3 geometry) {
        return new EdgeCurvePayload(
                stepId, "bspline_curve", null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, 0.0, 0.0);
    }

    private static EdgeCurvePayload newRationalBSplineCurvePayload(int stepId, RationalBSplineCurve3 geometry) {
        return new EdgeCurvePayload(
                stepId, "rational_bspline_curve", null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, 0.0, 0.0);
    }

    private static EdgeCurvePayload newPolylineCurvePayload(int stepId, Polyline3 geometry, CartesianPoint start, CartesianPoint end) {
        return new EdgeCurvePayload(
                stepId, "polyline", null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, 0.0, 0.0);
    }

    private static EdgeCurvePayload newLineCurvePayload(int stepId, StepCadBuilder builder, StepLine line, CartesianPoint start, CartesianPoint end) {
        return new EdgeCurvePayload(
                stepId, "line", null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, 0.0, 0.0);
    }


    private static List<CartesianPoint> sampleEdge(CartesianPoint start, CartesianPoint end, Curve3 curve, boolean naturalForward) {
        if (curve instanceof TrimmedCurve3) {
            TrimmedCurve3 trimmedCurve = (TrimmedCurve3) curve;
            List<CartesianPoint> points = new ArrayList<>(Curve3SamplingHelper.sampleTrimmedCurve3(trimmedCurve, 72));
            if (!naturalForward) {
                java.util.Collections.reverse(points);
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
        }
        if (curve instanceof SurfaceCurve3) {
            SurfaceCurve3 surfaceCurve = (SurfaceCurve3) curve;
            return sampleEdge(start, end, surfaceCurve.curve3d(), naturalForward);
        }
        if (curve instanceof BSplineCurve3) {
            BSplineCurve3 splineCurve = (BSplineCurve3) curve;
            List<CartesianPoint> points = new ArrayList<>(splineCurve.sample(72));
            if (!naturalForward) {
                java.util.Collections.reverse(points);
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
        }
        if (curve instanceof RationalBSplineCurve3) {
            RationalBSplineCurve3 splineCurve = (RationalBSplineCurve3) curve;
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
        if (curve instanceof Circle) {
            Circle circle = (Circle) curve;
            return Curve3SamplingHelper.sampleCircleArc(circle, start, end, naturalForward);
        }
        if (curve instanceof Ellipse3) {
            Ellipse3 ellipse = (Ellipse3) curve;
            return Curve3SamplingHelper.sampleEllipseArc(ellipse, start, end, naturalForward);
        }
        if (curve instanceof Polyline3) {
            Polyline3 polyline = (Polyline3) curve;
            List<CartesianPoint> points = new ArrayList<>(polyline.points());
            if (!naturalForward) {
                java.util.Collections.reverse(points);
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
        }
        if (curve instanceof CompositeCurve3) {
            CompositeCurve3 compositeCurve = (CompositeCurve3) curve;
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
        if (curve instanceof Parabola3) {
            Parabola3 parabola = (Parabola3) curve;
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
        if (curve instanceof Hyperbola3) {
            Hyperbola3 hyperbola = (Hyperbola3) curve;
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
        if (curve instanceof Clothoid3) {
            Clothoid3 clothoid = (Clothoid3) curve;
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
        if (curve instanceof DegenerateCurve3) {
            DegenerateCurve3 degenerate = (DegenerateCurve3) curve;
            // Degenerate curve: a single collapsed point; return start-end as a degenerate edge
            return List.of(start, end);
        }
        throw new UnsupportedGeometryException("preview export requires LINE, CIRCLE, ELLIPSE, PARABOLA, HYPERBOLA, CLOTHOID, POLYLINE, COMPOSITE_CURVE, B_SPLINE, RATIONAL_B_SPLINE_CURVE, OFFSET_CURVE_2D/3D, SURFACE_CURVE, SEAM_CURVE, DEGENERATE_CURVE or TRIMMED_CURVE topology");
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
                            bounds.include(MathUtilityHelper.transform(point, instance.worldMatrix()));
                        }
                    }
                }
                for (EdgePayload edge : representation.edges()) {
                    for (PointPayload point : edge.points()) {
                        bounds.include(MathUtilityHelper.transform(point, instance.worldMatrix()));
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
                ValidationReportHelper.buildValidationReport(
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
                GeometryMeasurementHelper.approximateSurfaceArea(geometry.faces()),
                GeometryMeasurementHelper.approximateEdgeLength(geometry.edges())
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
                area += GeometryMeasurementHelper.approximateSurfaceArea(representation.faces(), instance.worldMatrix());
                edgeLength += GeometryMeasurementHelper.approximateEdgeLength(representation.edges(), instance.worldMatrix());
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
                    bounds.include(MathUtilityHelper.transform(point, matrix));
                }
            }
        }
        for (EdgePayload edge : representation.edges()) {
            for (PointPayload point : edge.points()) {
                bounds.include(MathUtilityHelper.transform(point, matrix));
            }
        }
    }


    public static double[] mappedItemMatrix(StepMappedItem mappedItem, StepCadBuilder builder) {
        StepRepresentationMap mappingSource = mappedItem.mappingSource();
        if (!(mappingSource.mappedOrigin() instanceof com.minicad.step.model.StepAxis2Placement3D)) {
            return null;
        }
        com.minicad.step.model.StepAxis2Placement3D originPlacement = (com.minicad.step.model.StepAxis2Placement3D) mappingSource.mappedOrigin();
        double[] sourceMatrix = StepAssemblyGraphBuilder.matrixForPlacement(originPlacement);
        double[] targetMatrix;
        if (mappedItem.mappingTarget() instanceof com.minicad.step.model.StepCartesianTransformationOperator) {
            com.minicad.step.model.StepCartesianTransformationOperator transformation = (com.minicad.step.model.StepCartesianTransformationOperator) mappedItem.mappingTarget();
            targetMatrix = matrixForTransformationOperator(transformation, builder);
        } else if (mappedItem.mappingTarget() instanceof com.minicad.step.model.StepAxis2Placement3D) {
            com.minicad.step.model.StepAxis2Placement3D targetPlacement = (com.minicad.step.model.StepAxis2Placement3D) mappedItem.mappingTarget();
            targetMatrix = StepAssemblyGraphBuilder.matrixForPlacement(targetPlacement);
        } else {
            return null;
        }
        return StepAssemblyGraphBuilder.multiplyMatrices(
                targetMatrix,
                StepAssemblyGraphBuilder.inverseRigidTransform(sourceMatrix)
        );
    }

    public static double[] matrixForTransformationOperator(
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

    public static EdgePayload transformMappedEdge(EdgePayload edge, int mappedItemId, double[] matrix) {
        return transformMappedEdge(edge, mappedItemId, matrix, null, null);
    }

    public static EdgePayload transformMappedEdge(
            EdgePayload edge,
            int mappedItemId,
            double[] matrix,
            String sourceType,
            Integer sourceStepId
    ) {
        List<PointPayload> points = edge.points().stream()
                .map(point -> MathUtilityHelper.transform(point, matrix))
                .collect(Collectors.toList());
        return new EdgePayload(
                mappedPayloadId(mappedItemId, edge.stepId(), 1),
                points,
                transformMappedCurve(edge.curve(), matrix, sourceType, sourceStepId),
                edge.color()
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
                : PreviewSerializers.pointList(MathUtilityHelper.transform(new PointPayload(curve.center().get(0), curve.center().get(1), curve.center().get(2)), matrix));
        List<Double> axis = curve.axis() == null
                ? null
                : PreviewSerializers.vectorList(MathUtilityHelper.transform(new VectorPayload(curve.axis().get(0), curve.axis().get(1), curve.axis().get(2)), matrix));
        List<Double> xDirection = curve.xDirection() == null
                ? null
                : PreviewSerializers.vectorList(MathUtilityHelper.transform(new VectorPayload(curve.xDirection().get(0), curve.xDirection().get(1), curve.xDirection().get(2)), matrix));
        List<Double> refDirection = curve.refDirection() == null
                ? null
                : PreviewSerializers.vectorList(MathUtilityHelper.transform(new VectorPayload(curve.refDirection().get(0), curve.refDirection().get(1), curve.refDirection().get(2)), matrix));
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

    public static FacePayload transformMappedFace(
            FacePayload face,
            int mappedItemId,
            double[] matrix,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<LoopPayload> loops = face.loops().stream()
                .map(loop -> new LoopPayload(
                        loop.outer(),
                        loop.points().stream().map(point -> MathUtilityHelper.transform(point, matrix)).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
        List<PointPayload> triangles = face.triangles().stream()
                .map(point -> MathUtilityHelper.transform(point, matrix))
                .collect(Collectors.toList());
        int[] rgb = metadata.rgb() != null ? metadata.rgb() : null;
        ColorPayload color = rgb == null ? face.color() : PayloadConversionHelper.toColorPayload(rgb);
        double transparency = metadata.transparency() > 0 ? metadata.transparency() : face.transparency();
        PbrPayload pbr = metadata.pbr() != null ? PayloadConversionHelper.toPbrPayload(metadata.pbr()) : face.pbr();
        List<String> layers = metadata.layers().isEmpty() ? face.layers() : metadata.layers();
        return new FacePayload(
                mappedPayloadId(mappedItemId, face.stepId(), 2),
                face.name(),
                face.surfaceType(),
                MathUtilityHelper.transform(face.origin(), matrix),
                MathUtilityHelper.transform(face.normal(), matrix),
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
            if (entity instanceof StepGeometricItemSpecificUsage) {
            StepGeometricItemSpecificUsage usage = (StepGeometricItemSpecificUsage) entity;
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
            } else if (entity instanceof StepChainBasedGeometricItemSpecificUsage) {
            StepChainBasedGeometricItemSpecificUsage usage = (StepChainBasedGeometricItemSpecificUsage) entity;
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
            } else if (entity instanceof StepItemIdentifiedRepresentationUsage) {
            StepItemIdentifiedRepresentationUsage usage = (StepItemIdentifiedRepresentationUsage) entity;
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
            } else if (entity instanceof StepChainBasedItemIdentifiedRepresentationUsage) {
            StepChainBasedItemIdentifiedRepresentationUsage usage = (StepChainBasedItemIdentifiedRepresentationUsage) entity;
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
            } else if (entity instanceof StepPlacedTarget) {
            StepPlacedTarget usage = (StepPlacedTarget) entity;
                appendRepresentationBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), instanceIdsByTargetId, "PLACED_TARGET", usage.id());
                appendAttachedRepresentationRelationshipTargets(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), resolved, instanceIdsByTargetId);
                appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.definition(), resolved, instanceIdsByTargetId);
            } else if (entity instanceof StepDraughtingModelItemAssociation) {
            StepDraughtingModelItemAssociation usage = (StepDraughtingModelItemAssociation) entity;
                appendRepresentationBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), instanceIdsByTargetId, "DRAUGHTING_MODEL_ITEM_ASSOCIATION", usage.id());
                appendAttachedRepresentationRelationshipTargets(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), resolved, instanceIdsByTargetId);
                appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.definition(), resolved, instanceIdsByTargetId);
            } else if (entity instanceof StepDraughtingModelItemAssociationWithPlaceholder) {
            StepDraughtingModelItemAssociationWithPlaceholder usage = (StepDraughtingModelItemAssociationWithPlaceholder) entity;
                appendRepresentationBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), instanceIdsByTargetId, "DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER", usage.id());
                appendAttachedRepresentationRelationshipTargets(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), resolved, instanceIdsByTargetId);
                appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.definition(), resolved, instanceIdsByTargetId);
            } else if (entity instanceof StepPmiRequirementItemAssociation) {
            StepPmiRequirementItemAssociation usage = (StepPmiRequirementItemAssociation) entity;
                appendRepresentationBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), instanceIdsByTargetId, "PMI_REQUIREMENT_ITEM_ASSOCIATION", usage.id());
                appendAttachedRepresentationRelationshipTargets(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), resolved, instanceIdsByTargetId);
                appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.requirement(), instanceIdsByTargetId);
                appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.requirement(), instanceIdsByTargetId);
                appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.definition(), resolved, instanceIdsByTargetId);
                appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.requirement(), resolved, instanceIdsByTargetId);
            } else if (entity instanceof StepMechanicalDesignRequirementItemAssociation) {
            StepMechanicalDesignRequirementItemAssociation usage = (StepMechanicalDesignRequirementItemAssociation) entity;
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
            if (entity instanceof StepDraughtingCalloutRelationship) {
            StepDraughtingCalloutRelationship relationship = (StepDraughtingCalloutRelationship) entity;
                propagateCalloutTargets(targetsByUsageId, relationship);
            }
        }
        List<PmiPayload> pmi = new ArrayList<>();
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepDraughtingCallout) {
            StepDraughtingCallout callout = (StepDraughtingCallout) entity;
                PmiPayload payload = toPmiPayload(callout, targetsByUsageId.getOrDefault(callout.id(), List.of()), builder);
                if (payload != null) {
                    pmi.add(payload);
                }
            } else if (entity instanceof StepAnnotationTextOccurrence) {
            StepAnnotationTextOccurrence textOccurrence = (StepAnnotationTextOccurrence) entity;
                CartesianPoint position = pointFromAnnotationPoint(textOccurrence.position(), builder);
                if (position == null) {
                    continue;
                }
                List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(textOccurrence.id(), List.of());
                pmi.add(new PmiPayload(
                        textOccurrence.name(),
                        textOccurrence.text(),
                        PayloadConversionHelper.toPointPayload(position),
                        List.of(),
                        targets.stream().map(PmiTargetPayload::id).collect(Collectors.toList()),
                        targets
                ));
            } else if (entity instanceof StepAnnotationPointOccurrence) {
            StepAnnotationPointOccurrence pointOccurrence = (StepAnnotationPointOccurrence) entity;
                CartesianPoint position = pointFromAnnotationPoint(pointOccurrence.item(), builder);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(pointOccurrence.id(), List.of());
                    pmi.add(toStandalonePointPmi(pointOccurrence.id(), pointOccurrence.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationFillAreaOccurrence) {
            StepAnnotationFillAreaOccurrence fillAreaOccurrence = (StepAnnotationFillAreaOccurrence) entity;
                CartesianPoint position = pointFromAnnotationPoint(fillAreaOccurrence.fillStyleTarget(), builder);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(fillAreaOccurrence.id(), List.of());
                    pmi.add(toStandalonePointPmi(fillAreaOccurrence.id(), fillAreaOccurrence.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationPlaceholderOccurrence) {
            StepAnnotationPlaceholderOccurrence placeholderOccurrence = (StepAnnotationPlaceholderOccurrence) entity;
                appendPlaceholderPmi(
                        placeholderOccurrence,
                        pmi,
                        builder,
                        targetsByUsageId.getOrDefault(placeholderOccurrence.id(), List.of()));
            } else if (entity instanceof StepAnnotationSymbolOccurrence) {
            StepAnnotationSymbolOccurrence symbolOccurrence = (StepAnnotationSymbolOccurrence) entity;
                CartesianPoint position = pointFromAnnotationOccurrence(symbolOccurrence.item(), builder);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(symbolOccurrence.id(), List.of());
                    pmi.add(toStandalonePointPmi(symbolOccurrence.id(), symbolOccurrence.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationSymbol) {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) entity;
                CartesianPoint position = pointFromAnnotationSymbol(annotationSymbol);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(annotationSymbol.id(), List.of());
                    pmi.add(toStandalonePointPmi(annotationSymbol.id(), annotationSymbol.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationText) {
            StepAnnotationText annotationText = (StepAnnotationText) entity;
                CartesianPoint position = pointFromPlacement(annotationText.mappingTarget());
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(annotationText.id(), List.of());
                    pmi.add(toStandalonePointPmi(annotationText.id(), annotationText.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationTextCharacter) {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) entity;
                CartesianPoint position = pointFromPlacement(annotationTextCharacter.mappingTarget());
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(annotationTextCharacter.id(), List.of());
                    pmi.add(toStandalonePointPmi(annotationTextCharacter.id(), annotationTextCharacter.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationFillArea) {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) entity;
                CartesianPoint position = pointFromAnnotationFillArea(fillArea, builder);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(fillArea.id(), List.of());
                    pmi.add(toStandalonePointPmi(fillArea.id(), fillArea.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationSubfigureOccurrence) {
            StepAnnotationSubfigureOccurrence subfigureOccurrence = (StepAnnotationSubfigureOccurrence) entity;
                CartesianPoint position = pointFromAnnotationOccurrence(subfigureOccurrence.item(), builder);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(subfigureOccurrence.id(), List.of());
                    pmi.add(toStandalonePointPmi(subfigureOccurrence.id(), subfigureOccurrence.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationPlane) {
            StepAnnotationPlane annotationPlane = (StepAnnotationPlane) entity;
                appendAnnotationPlanePmi(
                        annotationPlane,
                        pmi,
                        builder,
                        targetsByUsageId.getOrDefault(annotationPlane.id(), List.of()));
            } else if (entity instanceof StepDraughtingAnnotationOccurrence) {
            StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) entity;
                appendDraughtingAnnotationPmi(
                        annotationOccurrence,
                        pmi,
                        builder,
                        targetsByUsageId.getOrDefault(annotationOccurrence.id(), List.of()));
            } else if (entity instanceof StepAnnotationOccurrenceRelationship) {
            StepAnnotationOccurrenceRelationship relationship = (StepAnnotationOccurrenceRelationship) entity;
                appendAnnotationOccurrenceRelationshipPmi(relationship, pmi, builder);
            } else if (entity instanceof StepPointSet) {
            StepPointSet pointSet = (StepPointSet) entity;
                appendPointSetPmi(pointSet, pmi, builder);
            } else if (entity instanceof StepGeometricMeasurement) {
            StepGeometricMeasurement measurement = (StepGeometricMeasurement) entity;
                appendGeometricMeasurementPmi(measurement, pmi, builder);
            } else if (entity instanceof StepVertexShell) {
            StepVertexShell vertexShell = (StepVertexShell) entity;
                pmi.add(toStandalonePointPmi(
                        vertexShell.id(),
                        vertexShell.name(),
                        pointFromStep(vertexShell.extent().loopVertex().point())
                ));
            } else if (entity instanceof StepGeometricReplica && "POINT_REPLICA".equals(((StepGeometricReplica) entity).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) entity;
                CartesianPoint position = pointFromReplica(replica, builder);
                if (position != null) {
                    pmi.add(toStandalonePointPmi(replica.id(), replica.name(), position));
                }
            } else if (entity instanceof StepFillAreaWithOutline) {
            StepFillAreaWithOutline fillArea = (StepFillAreaWithOutline) entity;
                appendFillAreaWithOutlinePmi(fillArea, pmi, builder);
            } else if (entity instanceof StepGeometricTolerance) {
            StepGeometricTolerance tolerance = (StepGeometricTolerance) entity;
                appendGeometricTolerancePmi(tolerance, pmi, builder);
            } else if (entity instanceof StepGeometricToleranceWithDatumReference) {
            StepGeometricToleranceWithDatumReference tolerance = (StepGeometricToleranceWithDatumReference) entity;
                appendGeometricToleranceWithDatumPmi(tolerance, pmi, builder);
            } else if (entity instanceof StepGeometricToleranceWithDefinedAreaUnit) {
            StepGeometricToleranceWithDefinedAreaUnit tolerance = (StepGeometricToleranceWithDefinedAreaUnit) entity;
                appendGeometricToleranceWithAreaUnitPmi(tolerance, pmi, builder);
            } else if (entity instanceof StepGeometricToleranceWithMaximumTolerance) {
            StepGeometricToleranceWithMaximumTolerance tolerance = (StepGeometricToleranceWithMaximumTolerance) entity;
                appendGeometricToleranceWithMaxPmi(tolerance, pmi, builder);
            } else if (entity instanceof StepDimensionalLocation) {
            StepDimensionalLocation location = (StepDimensionalLocation) entity;
                appendDimensionalLocationPmi(location, pmi, builder);
            } else if (entity instanceof StepToleranceZone) {
            StepToleranceZone zone = (StepToleranceZone) entity;
                appendToleranceZonePmi(zone, pmi, builder);
            } else if (entity instanceof StepDatum) {
            StepDatum datum = (StepDatum) entity;
                appendDatumPmi(datum, pmi, builder);
            } else if (entity instanceof StepDatumTarget) {
            StepDatumTarget datumTarget = (StepDatumTarget) entity;
                appendDatumTargetPmi(datumTarget, pmi, builder);
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
            if (element instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) element;
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

    private static void appendGeometricMeasurementPmi(
            StepGeometricMeasurement measurement,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = pointFromAnnotationPoint(measurement.measurementGeometry(), builder);
        if (position != null) {
            String label = measurement.name() + " (" + measurement.geometricType() + ")";
            if (measurement.measuredValue() != 0.0) {
                label += ": " + String.format("%.3f", measurement.measuredValue());
            }
            pmi.add(toStandalonePointPmi(measurement.id(), label, position));
        }
        for (StepEntity pt : measurement.measurementPoints()) {
            CartesianPoint mp = pointFromAnnotationPoint(pt, builder);
            if (mp != null) {
                pmi.add(toStandalonePointPmi(measurement.id() * 1000 + measurement.measurementPoints().indexOf(pt),
                        measurement.name() + " point", mp));
            }
        }
    }

    private static void appendFillAreaWithOutlinePmi(
            StepFillAreaWithOutline fillArea,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        List<CartesianPoint> points = new ArrayList<>();
        for (StepEntity outline : fillArea.outlines()) {
            List<CartesianPoint> sampled = sampleLooseEdgePoints(outline, builder);
            if (sampled != null && !sampled.isEmpty()) {
                points.addAll(sampled);
            }
        }
        if (!points.isEmpty()) {
            CartesianPoint center = points.get(points.size() / 2);
            pmi.add(toStandalonePointPmi(fillArea.id(), fillArea.name(), center));
        }
    }

    private static void appendGeometricTolerancePmi(
            StepGeometricTolerance tolerance,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = pointFromAnnotationPoint(tolerance.toleratedShape(), builder);
        if (position != null) {
            String label = tolerance.name() != null ? tolerance.name() : "GEOMETRIC_TOLERANCE";
            if (tolerance.magnitude() != 0.0) {
                label += ": " + String.format("%.3f", tolerance.magnitude());
            }
            pmi.add(toStandalonePointPmi(tolerance.id(), label, position));
        }
    }

    private static void appendGeometricToleranceWithDatumPmi(
            StepGeometricToleranceWithDatumReference tolerance,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = pointFromAnnotationPoint(tolerance.tolerancedFeature(), builder);
        if (position != null) {
            String label = tolerance.name() != null ? tolerance.name() : tolerance.toleranceType();
            if (tolerance.magnitude() != null && tolerance.magnitude() != 0.0) {
                label += ": " + String.format("%.3f", tolerance.magnitude());
            }
            pmi.add(toStandalonePointPmi(tolerance.id(), label, position));
        }
    }

    private static void appendGeometricToleranceWithAreaUnitPmi(
            StepGeometricToleranceWithDefinedAreaUnit tolerance,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = pointFromAnnotationPoint(tolerance.tolerancedFeature(), builder);
        if (position != null) {
            String label = tolerance.name() != null ? tolerance.name() : tolerance.toleranceType();
            if (tolerance.magnitude() != null && tolerance.magnitude() != 0.0) {
                label += ": " + String.format("%.3f", tolerance.magnitude());
            }
            pmi.add(toStandalonePointPmi(tolerance.id(), label, position));
        }
    }

    private static void appendGeometricToleranceWithMaxPmi(
            StepGeometricToleranceWithMaximumTolerance tolerance,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = pointFromAnnotationPoint(tolerance.tolerancedFeature(), builder);
        if (position != null) {
            String label = tolerance.name() != null ? tolerance.name() : tolerance.toleranceType();
            if (tolerance.magnitude() != null && tolerance.magnitude() != 0.0) {
                label += ": " + String.format("%.3f", tolerance.magnitude());
            }
            if (tolerance.maximumTolerance() != null) {
                label += " / " + String.format("%.3f", tolerance.maximumTolerance());
            }
            pmi.add(toStandalonePointPmi(tolerance.id(), label, position));
        }
    }

    private static void appendDimensionalLocationPmi(
            StepDimensionalLocation location,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = pointFromAnnotationPoint(location.relatedShape(), builder);
        if (position != null) {
            String label = location.name() != null ? location.name() : "DIMENSIONAL_LOCATION";
            pmi.add(toStandalonePointPmi(location.id(), label, position));
        }
    }

    private static void appendToleranceZonePmi(
            StepToleranceZone zone,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        String zoneShape = null;
        if (zone.form() instanceof StepToleranceZoneForm) {
            StepToleranceZoneForm form = (StepToleranceZoneForm) zone.form();
            zoneShape = form.zoneShape();
        }
        CartesianPoint position = pointFromAnnotationPoint(zone.form(), builder);
        if (position != null) {
            String label = zone.name() != null ? zone.name() : "TOLERANCE_ZONE";
            if (zoneShape != null) {
                label += " (" + zoneShape + ")";
            }
            pmi.add(toStandalonePointPmi(zone.id(), label, position));
        }
    }

    private static void appendDatumPmi(
            StepDatum datum,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = pointFromAnnotationPoint(datum.target(), builder);
        if (position != null) {
            String label = datum.name() != null ? datum.name() : "DATUM";
            pmi.add(toStandalonePointPmi(datum.id(), label, position));
        }
    }

    private static void appendDatumTargetPmi(
            StepDatumTarget datumTarget,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = pointFromAnnotationPoint(datumTarget.targetShape(), builder);
        if (position != null) {
            String label = datumTarget.name() != null ? datumTarget.name() : "DATUM_TARGET";
            pmi.add(toStandalonePointPmi(datumTarget.id(), label, position));
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
                PayloadConversionHelper.toPointPayload(position),
                List.of(),
                targets.stream().map(PmiTargetPayload::id).collect(Collectors.toList()),
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

    public static CartesianPoint pointFromAnnotationPoint(StepEntity item, StepCadBuilder builder) {
        if (item instanceof StepCartesianPoint) {
            StepCartesianPoint point = (StepCartesianPoint) item;
            return pointFromStep(point);
        }
        if (item instanceof StepVertexPoint) {
            StepVertexPoint vertexPoint = (StepVertexPoint) item;
            return pointFromStep(vertexPoint.point());
        }
        if (item instanceof StepVertexShell) {
            StepVertexShell vertexShell = (StepVertexShell) item;
            return pointFromStep(vertexShell.extent().loopVertex().point());
        }
        if (item instanceof StepPointSet) {
            StepPointSet pointSet = (StepPointSet) item;
            return pointFromPointSet(pointSet, builder);
        }
        if (item instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) item;
            return pointFromGeometricSet(geometricSet, builder);
        }
        if (item instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) item;
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
        if (builder != null && item instanceof StepGeometricReplica) {
            StepGeometricReplica replica = (StepGeometricReplica) item;
            if ("POINT_REPLICA".equals(replica.entityName())) {
                return pointFromReplica(replica, builder);
            }
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
        return sampled.get(0);
    }

    private static CartesianPoint pointFromAnnotationOccurrence(StepEntity occurrence, StepCadBuilder builder) {
        if (occurrence instanceof StepAnnotationPointOccurrence) {
            StepAnnotationPointOccurrence pointOccurrence = (StepAnnotationPointOccurrence) occurrence;
            return pointFromAnnotationPoint(pointOccurrence.item(), builder);
        } else if (occurrence instanceof StepAnnotationCurveOccurrence) {
            StepAnnotationCurveOccurrence curveOccurrence = (StepAnnotationCurveOccurrence) occurrence;
            return pointFromCurveCarrier(curveOccurrence.item(), builder);
        } else if (occurrence instanceof StepLeaderCurve) {
            StepLeaderCurve leaderCurve = (StepLeaderCurve) occurrence;
            return pointFromCurveCarrier(leaderCurve.item(), builder);
        } else if (occurrence instanceof StepDimensionCurve) {
            StepDimensionCurve dimensionCurve = (StepDimensionCurve) occurrence;
            return pointFromCurveCarrier(dimensionCurve.item(), builder);
        } else if (occurrence instanceof StepProjectionCurve) {
            StepProjectionCurve projectionCurve = (StepProjectionCurve) occurrence;
            return pointFromCurveCarrier(projectionCurve.item(), builder);
        } else if (occurrence instanceof StepAnnotationFillAreaOccurrence) {
            StepAnnotationFillAreaOccurrence fillAreaOccurrence = (StepAnnotationFillAreaOccurrence) occurrence;
            return pointFromAnnotationPoint(fillAreaOccurrence.fillStyleTarget(), builder);
        } else if (occurrence instanceof StepAnnotationFillArea) {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) occurrence;
            return pointFromAnnotationFillArea(fillArea, builder);
        } else if (occurrence instanceof StepAnnotationSymbol) {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) occurrence;
            return pointFromAnnotationSymbol(annotationSymbol);
        } else if (occurrence instanceof StepAnnotationSymbolOccurrence) {
            StepAnnotationSymbolOccurrence symbolOccurrence = (StepAnnotationSymbolOccurrence) occurrence;
            return pointFromAnnotationOccurrence(symbolOccurrence.item(), builder);
        } else if (occurrence instanceof StepAnnotationSubfigureOccurrence) {
            StepAnnotationSubfigureOccurrence subfigureOccurrence = (StepAnnotationSubfigureOccurrence) occurrence;
            return pointFromAnnotationOccurrence(subfigureOccurrence.item(), builder);
        } else if (occurrence instanceof StepAnnotationPlaceholderOccurrence) {
            StepAnnotationPlaceholderOccurrence placeholderOccurrence = (StepAnnotationPlaceholderOccurrence) occurrence;
            return pointFromPlaceholderItem(placeholderOccurrence.item(), builder);
        } else if (occurrence instanceof StepAnnotationPlane) {
            StepAnnotationPlane annotationPlane = (StepAnnotationPlane) occurrence;
            return pointFromAnnotationPlane(annotationPlane, builder);
        } else if (occurrence instanceof StepAnnotationText) {
            StepAnnotationText annotationText = (StepAnnotationText) occurrence;
            return pointFromPlacement(annotationText.mappingTarget());
        } else if (occurrence instanceof StepAnnotationTextCharacter) {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) occurrence;
            return pointFromPlacement(annotationTextCharacter.mappingTarget());
        } else if (occurrence instanceof StepAnnotationTextOccurrence) {
            StepAnnotationTextOccurrence textOccurrence = (StepAnnotationTextOccurrence) occurrence;
            return pointFromAnnotationPoint(textOccurrence.position(), builder);
        } else if (occurrence instanceof StepDraughtingAnnotationOccurrence) {
            StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) occurrence;
            return pointFromAnnotationOccurrence(annotationOccurrence.item(), builder);
        } else if (occurrence instanceof StepTerminatorSymbol) {
            StepTerminatorSymbol terminatorSymbol = (StepTerminatorSymbol) occurrence;
            CartesianPoint position = pointFromAnnotationOccurrence(terminatorSymbol.item(), builder);
            if (position == null) {
                position = pointFromAnnotationOccurrence(terminatorSymbol.annotatedCurve(), builder);
            }
            return position;
        } else if (occurrence instanceof StepPointSet) {
            StepPointSet pointSet = (StepPointSet) occurrence;
            return pointFromPointSet(pointSet, builder);
        } else if (occurrence instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) occurrence;
            return pointFromGeometricSet(geometricSet, builder);
        } else if (occurrence instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) occurrence;
            return pointFromGeometricCurveSet(curveSet, builder);
        } else if (occurrence instanceof StepVertexShell) {
            StepVertexShell vertexShell = (StepVertexShell) occurrence;
            return pointFromStep(vertexShell.extent().loopVertex().point());
        } else if (occurrence instanceof StepGeometricReplica
                && "POINT_REPLICA".equals(((StepGeometricReplica) occurrence).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) occurrence;
            return builder == null ? null : pointFromReplica(replica, builder);
        } else {
            return null;
        }
    }

    private static CartesianPoint pointFromCurveCarrier(StepEntity item, StepCadBuilder builder) {
        List<CartesianPoint> sampled = sampleLooseEdgePoints(item, builder);
        if (sampled == null || sampled.isEmpty()) {
            return null;
        }
        return sampled.get(0);
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
            if (element instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) element;
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
        if (item instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) item;
            return pointFromGeometricSet(geometricSet, builder);
        }
        if (item instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) item;
            return pointFromGeometricCurveSet(curveSet, builder);
        }
        if (item instanceof StepPointSet) {
            StepPointSet pointSet = (StepPointSet) item;
            return pointFromPointSet(pointSet, builder);
        }
        if (item instanceof StepAnnotationPlane) {
            StepAnnotationPlane annotationPlane = (StepAnnotationPlane) item;
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
        if (item instanceof StepPointSet) {
            StepPointSet pointSet = (StepPointSet) item;
            for (StepEntity point : pointSet.points()) {
                collectPlaceholderPositions(point, positions, builder);
            }
            return;
        }
        if (item instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) item;
            for (StepEntity element : geometricSet.elements()) {
                collectPlaceholderPositions(element, positions, builder);
            }
            return;
        }
        if (item instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) item;
            for (StepEntity element : curveSet.elements()) {
                collectPlaceholderPositions(element, positions, builder);
            }
            return;
        }
        if (item instanceof StepAnnotationPlane) {
            StepAnnotationPlane annotationPlane = (StepAnnotationPlane) item;
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
        if (placement instanceof StepAxis2Placement3D) {
            StepAxis2Placement3D placement3D = (StepAxis2Placement3D) placement;
            return pointFromStep(placement3D.location());
        }
        if (placement instanceof StepAxis2Placement2D) {
            StepAxis2Placement2D placement2D = (StepAxis2Placement2D) placement;
            StepCartesianPoint point = placement2D.location();
            return new CartesianPoint(point.coordinates().get(0), point.coordinates().get(1), 0.0);
        }
        return null;
    }

    public static CartesianPoint pointFromReplica(StepGeometricReplica replica, StepCadBuilder builder) {
        if (replica.parent() instanceof StepCartesianPoint) {
            StepCartesianPoint point = (StepCartesianPoint) replica.parent();
            return transformPoint(pointFromStep(point), replica.transformation(), builder);
        }
        if (replica.parent() instanceof StepVertexPoint) {
            StepVertexPoint vertexPoint = (StepVertexPoint) replica.parent();
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
            if (content instanceof StepAnnotationTextOccurrence) {
            StepAnnotationTextOccurrence annotationText = (StepAnnotationTextOccurrence) content;
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
                PayloadConversionHelper.toPointPayload(position),
                List.copyOf(leader),
                targets.stream().map(PmiTargetPayload::id).collect(Collectors.toList()),
                List.copyOf(targets)
        );
    }

    private static void appendPmiLeader(
            StepEntity content,
            List<PointPayload> leader,
            StepCadBuilder builder
    ) {
        if (content instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) content;
            for (StepEntity element : geometricSet.elements()) {
                appendPmiLeader(element, leader, builder);
            }
            return;
        }
        if (content instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) content;
            for (StepEntity element : curveSet.elements()) {
                appendPmiLeader(element, leader, builder);
            }
            return;
        }
        if (content instanceof StepPointSet) {
            StepPointSet pointSet = (StepPointSet) content;
            for (StepEntity point : pointSet.points()) {
                appendPmiLeader(point, leader, builder);
            }
            return;
        }
        if (content instanceof StepAnnotationPlaceholderOccurrence) {
            StepAnnotationPlaceholderOccurrence placeholderOccurrence = (StepAnnotationPlaceholderOccurrence) content;
            appendPmiLeader(placeholderOccurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepAnnotationPlane) {
            StepAnnotationPlane annotationPlane = (StepAnnotationPlane) content;
            for (StepEntity element : annotationPlane.elements()) {
                appendPmiLeader(element, leader, builder);
            }
            return;
        }
        if (content instanceof StepFaceBasedSurfaceModel) {
            StepFaceBasedSurfaceModel surfaceModel = (StepFaceBasedSurfaceModel) content;
            for (StepEntity faceSet : surfaceModel.faceSets()) {
                appendPmiLeader(faceSet, leader, builder);
            }
            return;
        }
        if (content instanceof StepShellBasedSurfaceModel) {
            StepShellBasedSurfaceModel surfaceModel = (StepShellBasedSurfaceModel) content;
            for (StepEntity shell : surfaceModel.shells()) {
                appendPmiLeader(shell, leader, builder);
            }
            return;
        }
        if (content instanceof StepManifoldSolidBrep) {
            StepManifoldSolidBrep solid = (StepManifoldSolidBrep) content;
            appendPmiLeader(solid.outer(), leader, builder);
            return;
        }
        if (content instanceof StepBrepWithVoids) {
            StepBrepWithVoids solid = (StepBrepWithVoids) content;
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
        if (content instanceof StepAdvancedFace) {
            StepAdvancedFace face = (StepAdvancedFace) content;
            for (StepFaceBound bound : face.bounds()) {
                appendPmiLeader(bound, leader, builder);
            }
            return;
        }
        if (content instanceof StepFaceSurface) {
            StepFaceSurface face = (StepFaceSurface) content;
            for (StepFaceBound bound : face.bounds()) {
                appendPmiLeader(bound, leader, builder);
            }
            return;
        }
        if (content instanceof StepOrientedFace) {
            StepOrientedFace face = (StepOrientedFace) content;
            appendPmiLeader(face.faceElement(), leader, builder);
            return;
        }
        if (content instanceof StepFaceBound) {
            StepFaceBound faceBound = (StepFaceBound) content;
            appendPmiLeader(faceBound.loop(), leader, builder);
            return;
        }
        if (content instanceof StepOpenShell) {
            StepOpenShell shell = (StepOpenShell) content;
            for (StepFaceEntity face : shell.faces()) {
                appendPmiLeader(face, leader, builder);
            }
            return;
        }
        if (content instanceof StepSurfacedOpenShell) {
            StepSurfacedOpenShell shell = (StepSurfacedOpenShell) content;
            for (StepFaceEntity face : shell.faces()) {
                appendPmiLeader(face, leader, builder);
            }
            return;
        }
        if (content instanceof StepOrientedOpenShell) {
            StepOrientedOpenShell shell = (StepOrientedOpenShell) content;
            appendPmiLeader(shell.openShellElement(), leader, builder);
            return;
        }
        if (content instanceof StepClosedShell) {
            StepClosedShell shell = (StepClosedShell) content;
            for (StepFaceEntity face : shell.faces()) {
                appendPmiLeader(face, leader, builder);
            }
            return;
        }
        if (content instanceof StepOrientedClosedShell) {
            StepOrientedClosedShell shell = (StepOrientedClosedShell) content;
            appendPmiLeader(shell.closedShellElement(), leader, builder);
            return;
        }
        if (content instanceof StepConnectedFaceSet) {
            StepConnectedFaceSet faceSet = (StepConnectedFaceSet) content;
            for (StepFaceEntity face : faceSet.faces()) {
                appendPmiLeader(face, leader, builder);
            }
            return;
        }
        if (content instanceof StepConnectedFaceSubSet) {
            StepConnectedFaceSubSet faceSet = (StepConnectedFaceSubSet) content;
            for (StepFaceEntity face : faceSet.faces()) {
                appendPmiLeader(face, leader, builder);
            }
            return;
        }
        if (content instanceof StepAnnotationPointOccurrence) {
            StepAnnotationPointOccurrence pointOccurrence = (StepAnnotationPointOccurrence) content;
            appendPmiLeader(pointOccurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepAnnotationCurveOccurrence) {
            StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) content;
            appendPmiLeader(occurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepAnnotationFillArea) {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) content;
            List<CartesianPoint> sampled = sampleAnnotationFillAreaPoints(fillArea, builder);
            if (sampled != null) {
                for (CartesianPoint point : sampled) {
                    leader.add(PayloadConversionHelper.toPointPayload(point));
                }
            }
            return;
        }
        if (content instanceof StepAnnotationFillAreaOccurrence) {
            StepAnnotationFillAreaOccurrence fillAreaOccurrence = (StepAnnotationFillAreaOccurrence) content;
            appendPmiLeader(fillAreaOccurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepAnnotationSymbol) {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) content;
            List<CartesianPoint> sampled = sampleLooseEdgePoints(annotationSymbol, builder);
            if (sampled != null) {
                for (CartesianPoint point : sampled) {
                    leader.add(PayloadConversionHelper.toPointPayload(point));
                }
            }
            return;
        }
        if (content instanceof StepAnnotationSymbolOccurrence) {
            StepAnnotationSymbolOccurrence symbolOccurrence = (StepAnnotationSymbolOccurrence) content;
            appendPmiLeader(symbolOccurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepAnnotationSubfigureOccurrence) {
            StepAnnotationSubfigureOccurrence subfigureOccurrence = (StepAnnotationSubfigureOccurrence) content;
            appendPmiLeader(subfigureOccurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepAnnotationText) {
            StepAnnotationText annotationText = (StepAnnotationText) content;
            List<CartesianPoint> sampled = sampleLooseEdgePoints(annotationText, builder);
            if (sampled != null) {
                for (CartesianPoint point : sampled) {
                    leader.add(PayloadConversionHelper.toPointPayload(point));
                }
            }
            return;
        }
        if (content instanceof StepAnnotationTextCharacter) {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) content;
            List<CartesianPoint> sampled = sampleLooseEdgePoints(annotationTextCharacter, builder);
            if (sampled != null) {
                for (CartesianPoint point : sampled) {
                    leader.add(PayloadConversionHelper.toPointPayload(point));
                }
            }
            return;
        }
        if (content instanceof StepDimensionCurve) {
            StepDimensionCurve dimensionCurve = (StepDimensionCurve) content;
            appendPmiLeader(dimensionCurve.item(), leader, builder);
            return;
        }
        if (content instanceof StepLeaderCurve) {
            StepLeaderCurve leaderCurve = (StepLeaderCurve) content;
            appendPmiLeader(leaderCurve.item(), leader, builder);
            return;
        }
        if (content instanceof StepProjectionCurve) {
            StepProjectionCurve projectionCurve = (StepProjectionCurve) content;
            appendPmiLeader(projectionCurve.item(), leader, builder);
            return;
        }
        if (content instanceof StepDraughtingAnnotationOccurrence) {
            StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) content;
            appendPmiLeader(annotationOccurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepTerminatorSymbol) {
            StepTerminatorSymbol terminatorSymbol = (StepTerminatorSymbol) content;
            appendPmiLeader(terminatorSymbol.annotatedCurve(), leader, builder);
            return;
        }
        if (content instanceof StepPath) {
            StepPath path = (StepPath) content;
            appendPmiPathLeader(path.edges(), leader, builder);
            return;
        }
        if (content instanceof StepOpenPath) {
            StepOpenPath path = (StepOpenPath) content;
            appendPmiPathLeader(path.edges(), leader, builder);
            return;
        }
        if (content instanceof StepSubpath) {
            StepSubpath subpath = (StepSubpath) content;
            appendPmiPathLeader(subpath.edges(), leader, builder);
            return;
        }
        if (content instanceof StepOrientedPath) {
            StepOrientedPath orientedPath = (StepOrientedPath) content;
            appendPmiPathLeader(orientedPath.edges(), leader, builder);
            return;
        }
        if (content instanceof StepConnectedEdgeSet) {
            StepConnectedEdgeSet connectedEdgeSet = (StepConnectedEdgeSet) content;
            for (StepEntity edge : connectedEdgeSet.edges()) {
                appendPmiLeader(edge, leader, builder);
            }
            return;
        }
        if (content instanceof StepEdgeBasedWireframeModel) {
            StepEdgeBasedWireframeModel wireframeModel = (StepEdgeBasedWireframeModel) content;
            for (StepConnectedEdgeSet boundary : wireframeModel.boundaries()) {
                appendPmiLeader(boundary, leader, builder);
            }
            return;
        }
        if (content instanceof StepShellBasedWireframeModel) {
            StepShellBasedWireframeModel wireframeModel = (StepShellBasedWireframeModel) content;
            for (StepEntity boundary : wireframeModel.boundaries()) {
                appendPmiLeader(boundary, leader, builder);
            }
            return;
        }
        if (content instanceof StepWireShell) {
            StepWireShell wireShell = (StepWireShell) content;
            for (StepEntity loop : wireShell.loops()) {
                appendPmiLeader(loop, leader, builder);
            }
            return;
        }
        if (content instanceof StepEdgeLoop) {
            StepEdgeLoop edgeLoop = (StepEdgeLoop) content;
            appendPmiPathLeader(edgeLoop.edges(), leader, builder);
            return;
        }
        if (content instanceof StepVertexLoop) {
            StepVertexLoop vertexLoop = (StepVertexLoop) content;
            leader.add(PayloadConversionHelper.toPointPayload(pointFromStep(vertexLoop.loopVertex().point())));
            return;
        }
        if (content instanceof StepPolyLoop) {
            StepPolyLoop polyLoop = (StepPolyLoop) content;
            for (StepCartesianPoint point : polyLoop.polygon()) {
                leader.add(PayloadConversionHelper.toPointPayload(pointFromStep(point)));
            }
            return;
        }
        if (content instanceof StepVertexShell) {
            StepVertexShell vertexShell = (StepVertexShell) content;
            leader.add(PayloadConversionHelper.toPointPayload(pointFromStep(vertexShell.extent().loopVertex().point())));
            return;
        }
        if (content instanceof StepGeometricReplica && "POINT_REPLICA".equals(((StepGeometricReplica) content).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) content;
            CartesianPoint point = pointFromReplica(replica, builder);
            if (point != null) {
                leader.add(PayloadConversionHelper.toPointPayload(point));
            }
            return;
        }
        if (content instanceof StepCartesianPoint) {
            StepCartesianPoint point = (StepCartesianPoint) content;
            leader.add(PayloadConversionHelper.toPointPayload(pointFromStep(point)));
            return;
        }
        if (content instanceof StepVertexPoint) {
            StepVertexPoint vertexPoint = (StepVertexPoint) content;
            leader.add(PayloadConversionHelper.toPointPayload(pointFromStep(vertexPoint.point())));
            return;
        }
        List<CartesianPoint> sampled = sampleLooseEdgePoints(content, builder);
        if (sampled == null) {
            return;
        }
        for (CartesianPoint point : sampled) {
            leader.add(PayloadConversionHelper.toPointPayload(point));
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
        if (loop instanceof EdgeLoop) {
            EdgeLoop edgeLoop = (EdgeLoop) loop;
            for (OrientedEdge edge : edgeLoop.edges()) {
                appendTopologyEdgeLeader(edge, leader);
            }
            return;
        }
        if (loop instanceof VertexLoop) {
            VertexLoop vertexLoop = (VertexLoop) loop;
            leader.add(PayloadConversionHelper.toPointPayload(vertexLoop.vertex().point()));
            return;
        }
        if (loop instanceof PolyLoop) {
            PolyLoop polyLoop = (PolyLoop) loop;
            for (CartesianPoint point : polyLoop.points()) {
                leader.add(PayloadConversionHelper.toPointPayload(point));
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
            leader.add(PayloadConversionHelper.toPointPayload(point));
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
                leader.add(PayloadConversionHelper.toPointPayload(point));
            }
        }
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
                PmiTargetHelper.pmiTargetType(target),
                PmiTargetHelper.pmiTargetName(target),
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
        if (definition instanceof StepAnnotationOccurrenceRelationship) {
            StepAnnotationOccurrenceRelationship relationship = (StepAnnotationOccurrenceRelationship) definition;
            appendPmiTarget(
                    targetsByUsageId,
                    identifiedItem.id(),
                    representation,
                    instanceIdsByTargetId,
                    relationship.entityName(),
                    relationship.id()
            );
        } else if (definition instanceof StepDraughtingCalloutRelationship) {
            StepDraughtingCalloutRelationship relationship = (StepDraughtingCalloutRelationship) definition;
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

    public static void appendSemanticDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!isSupportedPmiUsageCarrier(identifiedItem)) {
            return;
        }
        if (definition instanceof StepAnnotationOccurrenceRelationship) {
            StepAnnotationOccurrenceRelationship relationship = (StepAnnotationOccurrenceRelationship) definition;
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
        if (definition instanceof StepDraughtingCalloutRelationship) {
            StepDraughtingCalloutRelationship relationship = (StepDraughtingCalloutRelationship) definition;
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
        if (definition instanceof StepPropertyDefinitionRelationship) {
            StepPropertyDefinitionRelationship relationship = (StepPropertyDefinitionRelationship) definition;
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
        if (definition instanceof StepPropertyDefinition) {
            StepPropertyDefinition propertyDefinition = (StepPropertyDefinition) definition;
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
        } else if (definition instanceof StepGeneralPropertyRelationship) {
            StepGeneralPropertyRelationship relationship = (StepGeneralPropertyRelationship) definition;
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
        } else if (definition instanceof StepShapeAspectRelationship) {
            StepShapeAspectRelationship relationship = (StepShapeAspectRelationship) definition;
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
        } else if (definition instanceof StepGeneralProperty) {
            StepGeneralProperty generalProperty = (StepGeneralProperty) definition;
            appendGeneralPropertyRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    generalProperty,
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepShapeAspect) {
            StepShapeAspect shapeAspect = (StepShapeAspect) definition;
            appendShapeAspectRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    shapeAspect,
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepProduct) {
            StepProduct product = (StepProduct) definition;
            appendProductRelationshipTargets(targetsByUsageId, identifiedItem.id(), product, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductDefinitionFormation) {
            StepProductDefinitionFormation formation = (StepProductDefinitionFormation) definition;
            appendProductDefinitionFormationRelationshipTargets(targetsByUsageId, identifiedItem.id(), formation, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductDefinition) {
            StepProductDefinition productDefinition = (StepProductDefinition) definition;
            appendProductDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), productDefinition, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductRelationship) {
            StepProductRelationship relationship = (StepProductRelationship) definition;
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
        } else if (definition instanceof StepProductDefinitionFormationRelationship) {
            StepProductDefinitionFormationRelationship relationship = (StepProductDefinitionFormationRelationship) definition;
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
        } else if (definition instanceof StepGroupRelationship) {
            StepGroupRelationship relationship = (StepGroupRelationship) definition;
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
        } else if (definition instanceof StepDocumentRelationship) {
            StepDocumentRelationship relationship = (StepDocumentRelationship) definition;
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
        } else if (definition instanceof StepOrganizationRelationship) {
            StepOrganizationRelationship relationship = (StepOrganizationRelationship) definition;
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
        } else if (definition instanceof StepEffectivityRelationship) {
            StepEffectivityRelationship relationship = (StepEffectivityRelationship) definition;
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
        } else if (definition instanceof StepProductCategoryRelationship) {
            StepProductCategoryRelationship relationship = (StepProductCategoryRelationship) definition;
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
        } else if (definition instanceof StepGroup) {
            StepGroup group = (StepGroup) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, group, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, group, resolved, instanceIdsByTargetId);
            appendGroupRelationshipTargets(targetsByUsageId, identifiedItem.id(), group, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDocument) {
            StepDocument document = (StepDocument) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, document, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, document, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    document.kind(),
                    instanceIdsByTargetId
            );
            appendDocumentRelationshipTargets(targetsByUsageId, identifiedItem.id(), document, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDocumentReference) {
            StepDocumentReference reference = (StepDocumentReference) definition;
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
        } else if (definition instanceof StepAppliedDocumentReference) {
            StepAppliedDocumentReference reference = (StepAppliedDocumentReference) definition;
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
        } else if (definition instanceof StepApprovalAssignment) {
            StepApprovalAssignment assignment = (StepApprovalAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedApproval(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedApproval(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedApproval().status(), instanceIdsByTargetId);
            appendApprovalDecorationTargets(targetsByUsageId, identifiedItem, assignment.assignedApproval(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSecurityClassificationAssignment) {
            StepSecurityClassificationAssignment assignment = (StepSecurityClassificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedSecurityClassification(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedSecurityClassification(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedSecurityClassification().securityLevel(), instanceIdsByTargetId);
        } else if (definition instanceof StepContractAssignment) {
            StepContractAssignment assignment = (StepContractAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedContract(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedContract(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedContract().kind(), instanceIdsByTargetId);
        } else if (definition instanceof StepCertificationAssignment) {
            StepCertificationAssignment assignment = (StepCertificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedCertification(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedCertification(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedCertification().kind(), instanceIdsByTargetId);
        } else if (definition instanceof StepPersonAndOrganizationAssignment) {
            StepPersonAndOrganizationAssignment assignment = (StepPersonAndOrganizationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedPersonAndOrganization(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedPersonAndOrganization(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedPersonAndOrganization().person(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedPersonAndOrganization().organization(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), instanceIdsByTargetId);
        } else if (definition instanceof StepOrganizationAssignment) {
            StepOrganizationAssignment assignment = (StepOrganizationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedOrganization(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedOrganization(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), instanceIdsByTargetId);
        } else if (definition instanceof StepLanguageAssignment) {
            StepLanguageAssignment assignment = (StepLanguageAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedLanguage(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedLanguage(), instanceIdsByTargetId);
        } else if (definition instanceof StepGroupAssignment) {
            StepGroupAssignment assignment = (StepGroupAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedGroup(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedGroup(), instanceIdsByTargetId);
        } else if (definition instanceof StepClassificationAssignment) {
            StepClassificationAssignment assignment = (StepClassificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedClass(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedClass(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), instanceIdsByTargetId);
        } else if (definition instanceof StepDateAssignment) {
            StepDateAssignment assignment = (StepDateAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedDate(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedDate(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), instanceIdsByTargetId);
        } else if (definition instanceof StepDateTimeAssignment) {
            StepDateTimeAssignment assignment = (StepDateTimeAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedDateAndTime(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedDateAndTime(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedDateAndTime().dateComponent(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedDateAndTime().timeComponent(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedDateAndTime().timeComponent().zone(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), instanceIdsByTargetId);
        } else if (definition instanceof StepIdentificationAssignment) {
            StepIdentificationAssignment assignment = (StepIdentificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepExternalIdentificationAssignment) {
            StepExternalIdentificationAssignment assignment = (StepExternalIdentificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assignment.source(), resolved, instanceIdsByTargetId);
            appendExternalSourceRelationshipTargets(targetsByUsageId, identifiedItem, assignment.source(), resolved, instanceIdsByTargetId);
            appendExternallyDefinedItemTargets(targetsByUsageId, identifiedItem, assignment.source(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOrganization) {
            StepOrganization organization = (StepOrganization) definition;
            appendOrganizationRelationshipTargets(targetsByUsageId, identifiedItem.id(), organization, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepEffectivity) {
            StepEffectivity effectivity = (StepEffectivity) definition;
            appendProductDefinitionEffectivityTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    effectivity,
                    resolved,
                    instanceIdsByTargetId
            );
            appendEffectivityRelationshipTargets(targetsByUsageId, identifiedItem.id(), effectivity, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductCategory) {
            StepProductCategory category = (StepProductCategory) definition;
            appendProductCategoryRelationshipTargets(targetsByUsageId, identifiedItem.id(), category, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepExternalSource) {
            StepExternalSource source = (StepExternalSource) definition;
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
        } else if (definition instanceof StepExternallyDefinedItem) {
            StepExternallyDefinedItem item = (StepExternallyDefinedItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, item.source(), instanceIdsByTargetId);
            appendExternalSourceRelationshipTargets(targetsByUsageId, identifiedItem, item.source(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDocumentUsageConstraint) {
            StepDocumentUsageConstraint documentUsageConstraint = (StepDocumentUsageConstraint) definition;
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
        } else if (definition instanceof StepRepresentation) {
            StepRepresentation representation = (StepRepresentation) definition;
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
        } else if (definition instanceof StepProductDefinitionShape) {
            StepProductDefinitionShape productDefinitionShape = (StepProductDefinitionShape) definition;
            appendProductDefinitionShapeRepresentationTargets(
                    targetsByUsageId,
                    identifiedItem,
                    productDefinitionShape,
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepProductDefinition) {
            StepProductDefinition productDefinition = (StepProductDefinition) definition;
            appendProductDefinitionRepresentationTargets(
                    targetsByUsageId,
                    identifiedItem,
                    productDefinition,
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepNextAssemblyUsageOccurrence) {
            StepNextAssemblyUsageOccurrence occurrence = (StepNextAssemblyUsageOccurrence) definition;
            appendOccurrenceRepresentationTargets(
                    targetsByUsageId,
                    identifiedItem,
                    occurrence,
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepShapeAspectOccurrence) {
            StepShapeAspectOccurrence occurrence = (StepShapeAspectOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    occurrence.definition(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepProductDefinitionRelationshipRelationship) {
            StepProductDefinitionRelationshipRelationship relationship = (StepProductDefinitionRelationshipRelationship) definition;
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
        } else if (definition instanceof StepApprovalPersonOrganization) {
            StepApprovalPersonOrganization assignment = (StepApprovalPersonOrganization) definition;
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
        } else if (definition instanceof StepApprovalDateTime) {
            StepApprovalDateTime assignment = (StepApprovalDateTime) definition;
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
        } else if (definition instanceof StepCalendarDate) {
            StepCalendarDate calendarDate = (StepCalendarDate) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, calendarDate, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, calendarDate, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepNameAttribute) {
            StepNameAttribute attribute = (StepNameAttribute) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, attribute, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, attribute, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, attribute.namedItem(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDescriptionAttribute) {
            StepDescriptionAttribute attribute = (StepDescriptionAttribute) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, attribute, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, attribute, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, attribute.describedItem(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepIdAttribute) {
            StepIdAttribute attribute = (StepIdAttribute) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, attribute, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, attribute, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, attribute.identifiedItem(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepNameAssignment) {
            StepNameAssignment assignment = (StepNameAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAppliedNameAssignment) {
            StepAppliedNameAssignment assignment = (StepAppliedNameAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            for (StepEntity item : assignment.items()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepDateAndTime) {
            StepDateAndTime dateAndTime = (StepDateAndTime) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, dateAndTime, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, dateAndTime, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, dateAndTime.dateComponent(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, dateAndTime.timeComponent(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepLocalTime) {
            StepLocalTime localTime = (StepLocalTime) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, localTime, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, localTime, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, localTime.zone(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCoordinatedUniversalTimeOffset) {
            StepCoordinatedUniversalTimeOffset zone = (StepCoordinatedUniversalTimeOffset) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, zone, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, zone, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepApprovalStatus) {
            StepApprovalStatus status = (StepApprovalStatus) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, status, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, status, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSecurityClassificationLevel) {
            StepSecurityClassificationLevel level = (StepSecurityClassificationLevel) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, level, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, level, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepContractType) {
            StepContractType kind = (StepContractType) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, kind, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, kind, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCertificationType) {
            StepCertificationType kind = (StepCertificationType) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, kind, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, kind, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepApprovalRole) {
            StepApprovalRole role = (StepApprovalRole) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOrganizationRole) {
            StepOrganizationRole role = (StepOrganizationRole) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPersonAndOrganizationRole) {
            StepPersonAndOrganizationRole role = (StepPersonAndOrganizationRole) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepClassificationRole) {
            StepClassificationRole role = (StepClassificationRole) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDateRole) {
            StepDateRole role = (StepDateRole) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDateTimeRole) {
            StepDateTimeRole role = (StepDateTimeRole) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepIdentificationRole) {
            StepIdentificationRole role = (StepIdentificationRole) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDocumentType) {
            StepDocumentType kind = (StepDocumentType) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, kind, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, kind, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepApproval) {
            StepApproval approval = (StepApproval) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, approval, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, approval, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, approval.status(), resolved, instanceIdsByTargetId);
            appendApprovalDecorationTargets(targetsByUsageId, identifiedItem, approval, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSecurityClassification) {
            StepSecurityClassification classification = (StepSecurityClassification) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, classification, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, classification, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, classification.securityLevel(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepContract) {
            StepContract contract = (StepContract) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, contract, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, contract, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, contract.kind(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCertification) {
            StepCertification certification = (StepCertification) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, certification, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, certification, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, certification.kind(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPerson) {
            StepPerson person = (StepPerson) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, person, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, person, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPersonAndOrganization) {
            StepPersonAndOrganization personAndOrganization = (StepPersonAndOrganization) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, personAndOrganization, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, personAndOrganization, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, personAndOrganization.person(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, personAndOrganization.organization(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepLanguage) {
            StepLanguage language = (StepLanguage) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, language, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, language, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAppliedClassificationAssignment) {
            StepAppliedClassificationAssignment assignment = (StepAppliedClassificationAssignment) definition;
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
        } else if (definition instanceof StepAppliedDateAssignment) {
            StepAppliedDateAssignment assignment = (StepAppliedDateAssignment) definition;
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
        } else if (definition instanceof StepAppliedDateTimeAssignment) {
            StepAppliedDateTimeAssignment assignment = (StepAppliedDateTimeAssignment) definition;
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
        } else if (definition instanceof StepAppliedApprovalAssignment) {
            StepAppliedApprovalAssignment assignment = (StepAppliedApprovalAssignment) definition;
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
        } else if (definition instanceof StepAppliedSecurityClassificationAssignment) {
            StepAppliedSecurityClassificationAssignment assignment = (StepAppliedSecurityClassificationAssignment) definition;
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
        } else if (definition instanceof StepAppliedContractAssignment) {
            StepAppliedContractAssignment assignment = (StepAppliedContractAssignment) definition;
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
        } else if (definition instanceof StepAppliedCertificationAssignment) {
            StepAppliedCertificationAssignment assignment = (StepAppliedCertificationAssignment) definition;
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
        } else if (definition instanceof StepAppliedPersonAndOrganizationAssignment) {
            StepAppliedPersonAndOrganizationAssignment assignment = (StepAppliedPersonAndOrganizationAssignment) definition;
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
        } else if (definition instanceof StepAppliedOrganizationAssignment) {
            StepAppliedOrganizationAssignment assignment = (StepAppliedOrganizationAssignment) definition;
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
        } else if (definition instanceof StepAppliedLanguageAssignment) {
            StepAppliedLanguageAssignment assignment = (StepAppliedLanguageAssignment) definition;
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
        } else if (definition instanceof StepAppliedGroupAssignment) {
            StepAppliedGroupAssignment assignment = (StepAppliedGroupAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedGroup(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAppliedIdentificationAssignment) {
            StepAppliedIdentificationAssignment assignment = (StepAppliedIdentificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), resolved, instanceIdsByTargetId);
            for (StepEntity item : assignment.items()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepAppliedExternalIdentificationAssignment) {
            StepAppliedExternalIdentificationAssignment assignment = (StepAppliedExternalIdentificationAssignment) definition;
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
        } else if (definition instanceof StepAnnotationCurveOccurrence) {
            StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAnnotationFillArea) {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, fillArea, instanceIdsByTargetId);
            for (StepEntity boundary : fillArea.boundaries()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, boundary, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepAnnotationFillAreaOccurrence) {
            StepAnnotationFillAreaOccurrence occurrence = (StepAnnotationFillAreaOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.fillStyleTarget(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAnnotationPlaceholderOccurrence) {
            StepAnnotationPlaceholderOccurrence occurrence = (StepAnnotationPlaceholderOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAnnotationPointOccurrence) {
            StepAnnotationPointOccurrence occurrence = (StepAnnotationPointOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAnnotationSymbolOccurrence) {
            StepAnnotationSymbolOccurrence occurrence = (StepAnnotationSymbolOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAnnotationSubfigureOccurrence) {
            StepAnnotationSubfigureOccurrence occurrence = (StepAnnotationSubfigureOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAnnotationTextOccurrence) {
            StepAnnotationTextOccurrence occurrence = (StepAnnotationTextOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.position(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDraughtingAnnotationOccurrence) {
            StepDraughtingAnnotationOccurrence occurrence = (StepDraughtingAnnotationOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTerminatorSymbol) {
            StepTerminatorSymbol symbol = (StepTerminatorSymbol) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, symbol, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, symbol.item(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, symbol.annotatedCurve(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPresentationStyleAssignment) {
            StepPresentationStyleAssignment assignment = (StepPresentationStyleAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            for (StepEntity style : assignment.styles()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepSurfaceStyleUsage) {
            StepSurfaceStyleUsage usage = (StepSurfaceStyleUsage) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, usage, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, usage.style(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceSideStyle) {
            StepSurfaceSideStyle style = (StepSurfaceSideStyle) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            for (StepEntity component : style.styles()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, component, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepSurfaceStyleFillArea) {
            StepSurfaceStyleFillArea style = (StepSurfaceStyleFillArea) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.fillStyle(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepFillAreaStyle) {
            StepFillAreaStyle style = (StepFillAreaStyle) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            for (StepFillAreaStyleColour component : style.styles()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, component, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepFillAreaStyleColour) {
            StepFillAreaStyleColour style = (StepFillAreaStyleColour) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.colour(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCurveStyle) {
            StepCurveStyle style = (StepCurveStyle) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.curveFont(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.colour(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleBoundary) {
            StepSurfaceStyleBoundary style = (StepSurfaceStyleBoundary) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.style(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleParameterLine) {
            StepSurfaceStyleParameterLine style = (StepSurfaceStyleParameterLine) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.style(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleControlGrid) {
            StepSurfaceStyleControlGrid style = (StepSurfaceStyleControlGrid) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.style(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleSegmentationCurve) {
            StepSurfaceStyleSegmentationCurve style = (StepSurfaceStyleSegmentationCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.style(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleSilhouette) {
            StepSurfaceStyleSilhouette style = (StepSurfaceStyleSilhouette) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.style(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCharacterGlyphStyleStroke) {
            StepCharacterGlyphStyleStroke style = (StepCharacterGlyphStyleStroke) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.strokeStyle(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCharacterGlyphStyleOutline) {
            StepCharacterGlyphStyleOutline style = (StepCharacterGlyphStyleOutline) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.outlineStyle(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCharacterGlyphStyleOutlineWithCharacteristics) {
            StepCharacterGlyphStyleOutlineWithCharacteristics style = (StepCharacterGlyphStyleOutlineWithCharacteristics) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.outlineStyle(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.characteristics(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTextStyle) {
            StepTextStyle style = (StepTextStyle) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.characterAppearance(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTextStyleWithSpacing) {
            StepTextStyleWithSpacing style = (StepTextStyleWithSpacing) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.characterAppearance(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTextStyleWithBoxCharacteristics) {
            StepTextStyleWithBoxCharacteristics style = (StepTextStyleWithBoxCharacteristics) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.characterAppearance(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTextStyleWithJustification) {
            StepTextStyleWithJustification style = (StepTextStyleWithJustification) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.characterAppearance(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTextStyleWithMirror) {
            StepTextStyleWithMirror style = (StepTextStyleWithMirror) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.characterAppearance(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.mirrorPlacement(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTextStyleForDefinedFont) {
            StepTextStyleForDefinedFont style = (StepTextStyleForDefinedFont) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.textColour(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPointStyle) {
            StepPointStyle style = (StepPointStyle) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.marker(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.colour(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSymbolColour) {
            StepSymbolColour style = (StepSymbolColour) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.colour(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSymbolStyle) {
            StepSymbolStyle style = (StepSymbolStyle) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.styleOfSymbol(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleTransparent) {
            StepSurfaceStyleTransparent style = (StepSurfaceStyleTransparent) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleReflectanceAmbient) {
            StepSurfaceStyleReflectanceAmbient style = (StepSurfaceStyleReflectanceAmbient) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleReflectanceAmbientDiffuse) {
            StepSurfaceStyleReflectanceAmbientDiffuse style = (StepSurfaceStyleReflectanceAmbientDiffuse) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleReflectanceAmbientDiffuseSpecular) {
            StepSurfaceStyleReflectanceAmbientDiffuseSpecular style = (StepSurfaceStyleReflectanceAmbientDiffuseSpecular) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.specularColour(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedSurfaceSideStyle) {
            StepPreDefinedSurfaceSideStyle style = (StepPreDefinedSurfaceSideStyle) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, style, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedColour) {
            StepPreDefinedColour colour = (StepPreDefinedColour) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, colour, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, colour, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDraughtingPreDefinedColour) {
            StepDraughtingPreDefinedColour colour = (StepDraughtingPreDefinedColour) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, colour, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, colour, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepColourRgb) {
            StepColourRgb colour = (StepColourRgb) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, colour, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, colour, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepColourSpecification) {
            StepColourSpecification colour = (StepColourSpecification) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, colour, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, colour, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepColour) {
            StepColour colour = (StepColour) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, colour, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, colour, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedCurveFont) {
            StepPreDefinedCurveFont font = (StepPreDefinedCurveFont) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, font, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, font, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDraughtingPreDefinedCurveFont) {
            StepDraughtingPreDefinedCurveFont font = (StepDraughtingPreDefinedCurveFont) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, font, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, font, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedTextFont) {
            StepPreDefinedTextFont font = (StepPreDefinedTextFont) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, font, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, font, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDraughtingPreDefinedTextFont) {
            StepDraughtingPreDefinedTextFont font = (StepDraughtingPreDefinedTextFont) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, font, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, font, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedTerminatorSymbol) {
            StepPreDefinedTerminatorSymbol symbol = (StepPreDefinedTerminatorSymbol) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, symbol, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, symbol, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedSymbol) {
            StepPreDefinedSymbol symbol = (StepPreDefinedSymbol) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, symbol, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, symbol, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedDimensionSymbol) {
            StepPreDefinedDimensionSymbol symbol = (StepPreDefinedDimensionSymbol) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, symbol, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, symbol, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedGeometricalToleranceSymbol) {
            StepPreDefinedGeometricalToleranceSymbol symbol = (StepPreDefinedGeometricalToleranceSymbol) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, symbol, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, symbol, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedItem) {
            StepPreDefinedItem item = (StepPreDefinedItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAnnotationPlane) {
            StepAnnotationPlane plane = (StepAnnotationPlane) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, plane, instanceIdsByTargetId);
            for (StepPresentationStyleAssignment style : plane.styles()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style, resolved, instanceIdsByTargetId);
            }
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, plane.item(), resolved, instanceIdsByTargetId);
            for (StepEntity element : plane.elements()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, element, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepDraughtingCallout) {
            StepDraughtingCallout callout = (StepDraughtingCallout) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, callout, instanceIdsByTargetId);
            for (StepEntity content : callout.contents()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, content, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepPresentationLayerAssignment) {
            StepPresentationLayerAssignment assignment = (StepPresentationLayerAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            for (StepEntity item : assignment.assignedItems()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepStyledItem) {
            StepStyledItem styledItem = (StepStyledItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, styledItem, instanceIdsByTargetId);
            for (StepPresentationStyleAssignment style : styledItem.styles()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style, resolved, instanceIdsByTargetId);
            }
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, styledItem.item(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOverRidingStyledItem) {
            StepOverRidingStyledItem styledItem = (StepOverRidingStyledItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, styledItem, instanceIdsByTargetId);
            for (StepPresentationStyleAssignment style : styledItem.styles()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style, resolved, instanceIdsByTargetId);
            }
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, styledItem.item(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, styledItem.overRiddenStyle(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepRepresentationMap) {
            StepRepresentationMap representationMap = (StepRepresentationMap) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, representationMap, instanceIdsByTargetId);
            appendRepresentationMapDefinitionTargets(targetsByUsageId, identifiedItem, representationMap, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, representationMap, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSymbolRepresentationMap) {
            StepSymbolRepresentationMap representationMap = (StepSymbolRepresentationMap) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, representationMap, instanceIdsByTargetId);
            appendRepresentationMapDefinitionTargets(targetsByUsageId, identifiedItem, representationMap, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, representationMap, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepMappedItem) {
            StepMappedItem mappedItem = (StepMappedItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, mappedItem, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, mappedItem.mappingSource(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, mappedItem.mappingTarget(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, mappedItem, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepGeometricReplica) {
            StepGeometricReplica replica = (StepGeometricReplica) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, replica, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, replica.parent(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, replica.transformation(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, replica, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepItemDefinedTransformation) {
            StepItemDefinedTransformation transformation = (StepItemDefinedTransformation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, transformation, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, transformation.transformItem1(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, transformation.transformItem2(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, transformation, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCartesianTransformationOperator) {
            StepCartesianTransformationOperator transformation = (StepCartesianTransformationOperator) definition;
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
        } else if (definition instanceof StepAxis1Placement) {
            StepAxis1Placement placement = (StepAxis1Placement) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, placement, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.location(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.axis(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAxis2Placement2D) {
            StepAxis2Placement2D placement = (StepAxis2Placement2D) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, placement, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.location(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.refDirection(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCartesianPoint) {
            StepCartesianPoint point = (StepCartesianPoint) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, point, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, point, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPoint) {
            StepPoint point = (StepPoint) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, point, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, point, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDirection) {
            StepDirection direction = (StepDirection) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, direction, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, direction, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepVector) {
            StepVector vector = (StepVector) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, vector, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, vector.orientation(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, vector, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAxis2Placement3D) {
            StepAxis2Placement3D placement = (StepAxis2Placement3D) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, placement, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.location(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.axis(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.refDirection(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, placement, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPlane) {
            StepPlane plane = (StepPlane) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, plane, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, plane.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, plane, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepGeometricSet) {
            StepGeometricSet set = (StepGeometricSet) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, set, instanceIdsByTargetId);
            for (StepEntity element : set.elements()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, element, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, set, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet set = (StepGeometricCurveSet) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, set, instanceIdsByTargetId);
            for (StepEntity element : set.elements()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, element, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, set, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPointSet) {
            StepPointSet set = (StepPointSet) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, set, instanceIdsByTargetId);
            for (StepEntity point : set.points()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, point, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, set, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPath) {
            StepPath path = (StepPath) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, path, instanceIdsByTargetId);
            for (StepOrientedEdge edge : path.edges()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, path, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOpenPath) {
            StepOpenPath path = (StepOpenPath) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, path, instanceIdsByTargetId);
            for (StepOrientedEdge edge : path.edges()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, path, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOrientedPath) {
            StepOrientedPath path = (StepOrientedPath) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, path, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, path.pathElement(), resolved, instanceIdsByTargetId);
            for (StepOrientedEdge edge : path.edges()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, path, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSubpath) {
            StepSubpath path = (StepSubpath) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, path, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, path.parentPath(), resolved, instanceIdsByTargetId);
            for (StepOrientedEdge edge : path.edges()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, path, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepEdgeLoop) {
            StepEdgeLoop loop = (StepEdgeLoop) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, loop, instanceIdsByTargetId);
            for (StepOrientedEdge edge : loop.edges()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, loop, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPolyLoop) {
            StepPolyLoop loop = (StepPolyLoop) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, loop, instanceIdsByTargetId);
            for (StepCartesianPoint point : loop.polygon()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, point, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, loop, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepConnectedEdgeSet) {
            StepConnectedEdgeSet set = (StepConnectedEdgeSet) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, set, instanceIdsByTargetId);
            for (StepEntity edge : set.edges()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, set, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepEdgeBasedWireframeModel) {
            StepEdgeBasedWireframeModel model = (StepEdgeBasedWireframeModel) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, model, instanceIdsByTargetId);
            for (StepConnectedEdgeSet boundary : model.boundaries()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, boundary, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, model, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepShellBasedWireframeModel) {
            StepShellBasedWireframeModel model = (StepShellBasedWireframeModel) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, model, instanceIdsByTargetId);
            for (StepEntity boundary : model.boundaries()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, boundary, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, model, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepWireShell) {
            StepWireShell shell = (StepWireShell) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            for (StepLoop loop : shell.loops()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, loop, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepVertexShell) {
            StepVertexShell shell = (StepVertexShell) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, shell.extent(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepVertexLoop) {
            StepVertexLoop loop = (StepVertexLoop) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, loop, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, loop.loopVertex(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, loop, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOrientedEdge) {
            StepOrientedEdge edge = (StepOrientedEdge) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, edge, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge.edgeElement(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepEdgeCurve) {
            StepEdgeCurve edge = (StepEdgeCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, edge, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge.start(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge.end(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge.edgeGeometry(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepVertexPoint) {
            StepVertexPoint vertex = (StepVertexPoint) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, vertex, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, vertex.point(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, vertex, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAdvancedFace) {
            StepAdvancedFace face = (StepAdvancedFace) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, face, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face.faceGeometry(), resolved, instanceIdsByTargetId);
            for (StepFaceBound bound : face.bounds()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, bound, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepFaceSurface) {
            StepFaceSurface face = (StepFaceSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, face, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face.faceGeometry(), resolved, instanceIdsByTargetId);
            for (StepFaceBound bound : face.bounds()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, bound, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOrientedFace) {
            StepOrientedFace face = (StepOrientedFace) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, face, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face.faceElement(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepConnectedFaceSet) {
            StepConnectedFaceSet faceSet = (StepConnectedFaceSet) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, faceSet, instanceIdsByTargetId);
            for (StepFaceEntity face : faceSet.faces()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, faceSet, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepConnectedFaceSubSet) {
            StepConnectedFaceSubSet faceSet = (StepConnectedFaceSubSet) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, faceSet, instanceIdsByTargetId);
            for (StepFaceEntity face : faceSet.faces()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
            }
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, faceSet.parentFaceSet(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, faceSet, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOpenShell) {
            StepOpenShell shell = (StepOpenShell) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            for (StepFaceEntity face : shell.faces()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfacedOpenShell) {
            StepSurfacedOpenShell shell = (StepSurfacedOpenShell) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            for (StepFaceEntity face : shell.faces()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepClosedShell) {
            StepClosedShell shell = (StepClosedShell) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            for (StepFaceEntity face : shell.faces()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOrientedOpenShell) {
            StepOrientedOpenShell shell = (StepOrientedOpenShell) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, shell.openShellElement(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOrientedClosedShell) {
            StepOrientedClosedShell shell = (StepOrientedClosedShell) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, shell.closedShellElement(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepFaceBasedSurfaceModel) {
            StepFaceBasedSurfaceModel model = (StepFaceBasedSurfaceModel) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, model, instanceIdsByTargetId);
            for (StepEntity faceSet : model.faceSets()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, faceSet, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, model, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepShellBasedSurfaceModel) {
            StepShellBasedSurfaceModel model = (StepShellBasedSurfaceModel) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, model, instanceIdsByTargetId);
            for (StepEntity shell : model.shells()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, model, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepManifoldSolidBrep) {
            StepManifoldSolidBrep solid = (StepManifoldSolidBrep) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.outer(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBrepWithVoids) {
            StepBrepWithVoids solid = (StepBrepWithVoids) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.outer(), resolved, instanceIdsByTargetId);
            for (StepEntity voidShell : solid.voids()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, voidShell, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSweptAreaSolid) {
            StepSweptAreaSolid solid = (StepSweptAreaSolid) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.sweptArea(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.position(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.sweepReference(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSweptDiskSolid) {
            StepSweptDiskSolid solid = (StepSweptDiskSolid) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.sweptCurve(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepComplexClippingResult) {
            StepComplexClippingResult solid = (StepComplexClippingResult) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.firstOperand(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.secondOperand(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSolidReplica) {
            StepSolidReplica solid = (StepSolidReplica) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.parentSolid(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.transformation(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepHalfSpaceSolid) {
            StepHalfSpaceSolid solid = (StepHalfSpaceSolid) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.baseSurface(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.enclosure(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCsgSolid) {
            StepCsgSolid solid = (StepCsgSolid) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.treeRootExpression(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCsgPrimitive) {
            StepCsgPrimitive primitive = (StepCsgPrimitive) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, primitive, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, primitive.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, primitive, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProfileDef) {
            StepProfileDef profile = (StepProfileDef) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, profile, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, profile.position(), resolved, instanceIdsByTargetId);
            for (StepEntity curve : profile.curves()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, profile, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepConicCurve) {
            StepConicCurve curve = (StepConicCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBSplineCurve) {
            StepBSplineCurve curve = (StepBSplineCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBSplineCurveWithKnots) {
            StepBSplineCurveWithKnots curve = (StepBSplineCurveWithKnots) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepRationalBSplineCurve) {
            StepRationalBSplineCurve curve = (StepRationalBSplineCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBezierCurve) {
            StepBezierCurve curve = (StepBezierCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepUniformCurve) {
            StepUniformCurve curve = (StepUniformCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepQuasiUniformCurve) {
            StepQuasiUniformCurve curve = (StepQuasiUniformCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPiecewiseBezierCurve) {
            StepPiecewiseBezierCurve curve = (StepPiecewiseBezierCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepLine) {
            StepLine line = (StepLine) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, line, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, line.point(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, line.vector(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, line, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCircle) {
            StepCircle circle = (StepCircle) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, circle, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, circle.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, circle, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepEllipse) {
            StepEllipse ellipse = (StepEllipse) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, ellipse, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, ellipse.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, ellipse, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCurve) {
            StepCurve curve = (StepCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPolyline) {
            StepPolyline polyline = (StepPolyline) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, polyline, instanceIdsByTargetId);
            for (StepCartesianPoint point : polyline.points()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, point, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, polyline, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTrimmedCurve) {
            StepTrimmedCurve curve = (StepTrimmedCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.basisCurve(), resolved, instanceIdsByTargetId);
            for (StepValue trim : curve.trim1()) {
                if (trim instanceof StepValue.ReferenceValue) {
                    StepValue.ReferenceValue ref = (StepValue.ReferenceValue) trim;
                    appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, resolved.get(ref.id()), resolved, instanceIdsByTargetId);
                }
            }
            for (StepValue trim : curve.trim2()) {
                if (trim instanceof StepValue.ReferenceValue) {
                    StepValue.ReferenceValue ref = (StepValue.ReferenceValue) trim;
                    appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, resolved.get(ref.id()), resolved, instanceIdsByTargetId);
                }
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOffsetCurve2D) {
            StepOffsetCurve2D curve = (StepOffsetCurve2D) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.basisCurve(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOffsetCurve3D) {
            StepOffsetCurve3D curve = (StepOffsetCurve3D) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.basisCurve(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.refDirection(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPcurve) {
            StepPcurve curve = (StepPcurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.basisSurface(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.referenceToCurve(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDegeneratePcurve) {
            StepDegeneratePcurve curve = (StepDegeneratePcurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.basisSurface(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.referenceToCurve(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceCurve) {
            StepSurfaceCurve curve = (StepSurfaceCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.curve3d(), resolved, instanceIdsByTargetId);
            for (StepEntity associated : curve.associatedGeometry()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, associated, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSeamCurve) {
            StepSeamCurve curve = (StepSeamCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.curve3d(), resolved, instanceIdsByTargetId);
            for (StepEntity associated : curve.associatedGeometry()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, associated, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCompositeCurve) {
            StepCompositeCurve curve = (StepCompositeCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            for (StepCompositeCurveSegment segment : curve.segments()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, segment, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCompositeCurveOnSurface) {
            StepCompositeCurveOnSurface curve = (StepCompositeCurveOnSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            for (StepCompositeCurveSegment segment : curve.segments()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, segment, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCompositeCurveSegment) {
            StepCompositeCurveSegment segment = (StepCompositeCurveSegment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, segment, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, segment.parentCurve(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, segment, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCylindricalSurface) {
            StepCylindricalSurface surface = (StepCylindricalSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepConicalSurface) {
            StepConicalSurface surface = (StepConicalSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSphericalSurface) {
            StepSphericalSurface surface = (StepSphericalSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepToroidalSurface) {
            StepToroidalSurface surface = (StepToroidalSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceOfLinearExtrusion) {
            StepSurfaceOfLinearExtrusion surface = (StepSurfaceOfLinearExtrusion) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.sweptCurve(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.extrusionAxis(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceOfRevolution) {
            StepSurfaceOfRevolution surface = (StepSurfaceOfRevolution) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.sweptCurve(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.axisPosition(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepRectangularTrimmedSurface) {
            StepRectangularTrimmedSurface surface = (StepRectangularTrimmedSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.basisSurface(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCurveBoundedSurface) {
            StepCurveBoundedSurface surface = (StepCurveBoundedSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.basisSurface(), resolved, instanceIdsByTargetId);
            for (StepEntity boundary : surface.boundaries()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, boundary, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOrientedSurface) {
            StepOrientedSurface surface = (StepOrientedSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.surfaceElement(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOffsetSurface) {
            StepOffsetSurface surface = (StepOffsetSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.basisSurface(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBSplineSurface) {
            StepBSplineSurface surface = (StepBSplineSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBSplineSurfaceWithKnots) {
            StepBSplineSurfaceWithKnots surface = (StepBSplineSurfaceWithKnots) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepRationalBSplineSurface) {
            StepRationalBSplineSurface surface = (StepRationalBSplineSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBezierSurface) {
            StepBezierSurface surface = (StepBezierSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepUniformSurface) {
            StepUniformSurface surface = (StepUniformSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepQuasiUniformSurface) {
            StepQuasiUniformSurface surface = (StepQuasiUniformSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPiecewiseBezierSurface) {
            StepPiecewiseBezierSurface surface = (StepPiecewiseBezierSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepFace) {
            StepFace face = (StepFace) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, face, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBoundedCurve) {
            StepBoundedCurve curve = (StepBoundedCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBoundedSurface) {
            StepBoundedSurface surface = (StepBoundedSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurface) {
            StepSurface surface = (StepSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepMeasureRepresentationItem) {
            StepMeasureRepresentationItem item = (StepMeasureRepresentationItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item.unit(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDescriptiveRepresentationItem) {
            StepDescriptiveRepresentationItem item = (StepDescriptiveRepresentationItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepValueRepresentationItem) {
            StepValueRepresentationItem item = (StepValueRepresentationItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceModel) {
            StepSurfaceModel model = (StepSurfaceModel) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, model, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, model, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSolidModel) {
            StepSolidModel model = (StepSolidModel) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, model, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, model, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepRepresentationItem) {
            StepRepresentationItem item = (StepRepresentationItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepGeometricRepresentationItem) {
            StepGeometricRepresentationItem item = (StepGeometricRepresentationItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTopologicalRepresentationItem) {
            StepTopologicalRepresentationItem item = (StepTopologicalRepresentationItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepMeasureWithUnit) {
            StepMeasureWithUnit measure = (StepMeasureWithUnit) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, measure, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, measure.unitComponent(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, measure, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTypedMeasureWithUnit) {
            StepTypedMeasureWithUnit measure = (StepTypedMeasureWithUnit) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, measure, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, measure.unitComponent(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, measure, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepUncertaintyMeasureWithUnit) {
            StepUncertaintyMeasureWithUnit measure = (StepUncertaintyMeasureWithUnit) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, measure, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, measure.unitComponent(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, measure, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepConversionBasedUnit) {
            StepConversionBasedUnit unit = (StepConversionBasedUnit) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, unit, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, unit.conversionFactor(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepConversionBasedUnitWithOffset) {
            StepConversionBasedUnitWithOffset unit = (StepConversionBasedUnitWithOffset) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, unit, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, unit.conversionFactor(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDerivedUnit) {
            StepDerivedUnit unit = (StepDerivedUnit) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, unit, instanceIdsByTargetId);
            for (StepDerivedUnitElement element : unit.elements()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, element, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepDerivedUnitElement) {
            StepDerivedUnitElement element = (StepDerivedUnitElement) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, element, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, element.unit(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepNamedUnit) {
            StepNamedUnit unit = (StepNamedUnit) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, unit, instanceIdsByTargetId);
        } else if (definition instanceof StepSiUnit) {
            StepSiUnit unit = (StepSiUnit) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, unit, instanceIdsByTargetId);
        } else if (definition instanceof StepContextDependentUnit) {
            StepContextDependentUnit unit = (StepContextDependentUnit) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, unit, instanceIdsByTargetId);
        } else if (definition instanceof StepRepresentationContext) {
            StepRepresentationContext context = (StepRepresentationContext) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, context, instanceIdsByTargetId);
        } else if (definition instanceof StepGeometricRepresentationContext) {
            StepGeometricRepresentationContext context = (StepGeometricRepresentationContext) definition;
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
        } else if (definition instanceof StepGlobalUnitAssignedContext) {
            StepGlobalUnitAssignedContext context = (StepGlobalUnitAssignedContext) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, context, instanceIdsByTargetId);
            for (StepEntity unit : context.units()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, unit, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepGlobalUncertaintyAssignedContext) {
            StepGlobalUncertaintyAssignedContext context = (StepGlobalUncertaintyAssignedContext) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, context, instanceIdsByTargetId);
            for (StepUncertaintyMeasureWithUnit uncertainty : context.uncertainties()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, uncertainty, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepAddress) {
            StepAddress address = (StepAddress) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, address, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, address, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCharacterizedObject) {
            StepCharacterizedObject characterizedObject = (StepCharacterizedObject) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, characterizedObject, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, characterizedObject, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDimensionalExponents) {
            StepDimensionalExponents exponents = (StepDimensionalExponents) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, exponents, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, exponents, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepVertex) {
            StepVertex vertex = (StepVertex) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, vertex, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, vertex, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepEdge) {
            StepEdge edge = (StepEdge) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, edge, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAbstractVariable) {
            StepAbstractVariable variable = (StepAbstractVariable) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, variable, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    variable.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, variable.definition(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepRowVariable) {
            StepRowVariable variable = (StepRowVariable) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, variable, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    variable.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, variable.definition(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepScalarVariable) {
            StepScalarVariable variable = (StepScalarVariable) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, variable, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    variable.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, variable.definition(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepForwardChainingRulePremise) {
            StepForwardChainingRulePremise variable = (StepForwardChainingRulePremise) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, variable, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    variable.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, variable.definition(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBackChainingRuleBody) {
            StepBackChainingRuleBody variable = (StepBackChainingRuleBody) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, variable, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    variable.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, variable.definition(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPropertyDefinitionRepresentation) {
            StepPropertyDefinitionRepresentation link = (StepPropertyDefinitionRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepActionPropertyRepresentation) {
            StepActionPropertyRepresentation link = (StepActionPropertyRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepContactRatioRepresentation) {
            StepContactRatioRepresentation link = (StepContactRatioRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepKinematicPropertyDefinitionRepresentation) {
            StepKinematicPropertyDefinitionRepresentation link = (StepKinematicPropertyDefinitionRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepKinematicPropertyMechanismRepresentation) {
            StepKinematicPropertyMechanismRepresentation link = (StepKinematicPropertyMechanismRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepKinematicPropertyRepresentationRelation) {
            StepKinematicPropertyRepresentationRelation link = (StepKinematicPropertyRepresentationRelation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepKinematicPropertyTopologyRepresentation) {
            StepKinematicPropertyTopologyRepresentation link = (StepKinematicPropertyTopologyRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepResourcePropertyRepresentation) {
            StepResourcePropertyRepresentation link = (StepResourcePropertyRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAttributeAssertion) {
            StepAttributeAssertion assertion = (StepAttributeAssertion) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assertion, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assertion.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assertion.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepShapeDefinitionRepresentation) {
            StepShapeDefinitionRepresentation link = (StepShapeDefinitionRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepApplicationProtocolDefinition) {
            StepApplicationProtocolDefinition protocolDefinition = (StepApplicationProtocolDefinition) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, protocolDefinition, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, protocolDefinition.application(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProduct) {
            StepProduct product = (StepProduct) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, product, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, product, resolved, instanceIdsByTargetId);
            for (StepProductContext context : product.frameOfReference()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, context, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepProductDefinitionFormation) {
            StepProductDefinitionFormation formation = (StepProductDefinitionFormation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, formation, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, formation, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, formation.ofProduct(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductDefinition) {
            StepProductDefinition productDefinition = (StepProductDefinition) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, productDefinition, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, productDefinition, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, productDefinition.formation(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, productDefinition.frameOfReference(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductDefinitionShape) {
            StepProductDefinitionShape productDefinitionShape = (StepProductDefinitionShape) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, productDefinitionShape, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, productDefinitionShape, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, productDefinitionShape.definition(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductContext) {
            StepProductContext productContext = (StepProductContext) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, productContext, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, productContext, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, productContext.frameOfReference(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductDefinitionContext) {
            StepProductDefinitionContext productDefinitionContext = (StepProductDefinitionContext) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, productDefinitionContext, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, productDefinitionContext, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, productDefinitionContext.frameOfReference(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepApplicationContext) {
            StepApplicationContext applicationContext = (StepApplicationContext) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, applicationContext, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, applicationContext, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepGroup) {
            StepGroup group = (StepGroup) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, group, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, group, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDocument) {
            StepDocument document = (StepDocument) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, document, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, document, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, document.kind(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDocumentReference) {
            StepDocumentReference reference = (StepDocumentReference) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, reference, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, reference, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, reference.assignedDocument(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, reference.assignedDocument().kind(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAppliedDocumentReference) {
            StepAppliedDocumentReference reference = (StepAppliedDocumentReference) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, reference, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, reference, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, reference.assignedDocument(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, reference.assignedDocument().kind(), resolved, instanceIdsByTargetId);
            for (StepEntity item : reference.items()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepExternalSource) {
            StepExternalSource source = (StepExternalSource) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, source, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, source, resolved, instanceIdsByTargetId);
            appendExternalSourceRelationshipTargets(targetsByUsageId, identifiedItem, source, resolved, instanceIdsByTargetId);
            appendExternallyDefinedItemTargets(targetsByUsageId, identifiedItem, source, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepExternallyDefinedItem) {
            StepExternallyDefinedItem item = (StepExternallyDefinedItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item.source(), resolved, instanceIdsByTargetId);
            appendExternalSourceRelationshipTargets(targetsByUsageId, identifiedItem, item.source(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDocumentUsageConstraint) {
            StepDocumentUsageConstraint documentUsageConstraint = (StepDocumentUsageConstraint) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, documentUsageConstraint, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, documentUsageConstraint, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, documentUsageConstraint.source(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, documentUsageConstraint.source().kind(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductCategory) {
            StepProductCategory category = (StepProductCategory) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, category, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, category, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductRelatedProductCategory) {
            StepProductRelatedProductCategory relatedCategory = (StepProductRelatedProductCategory) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relatedCategory, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relatedCategory, resolved, instanceIdsByTargetId);
            for (StepProduct product : relatedCategory.products()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, product, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepGeneralProperty) {
            StepGeneralProperty generalProperty = (StepGeneralProperty) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, generalProperty, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, generalProperty, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductDefinitionEffectivity) {
            StepProductDefinitionEffectivity effectivity = (StepProductDefinitionEffectivity) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, effectivity, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, effectivity, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, effectivity.productDefinition(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepEffectivity) {
            StepEffectivity effectivity = (StepEffectivity) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, effectivity, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, effectivity, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepRepresentationRelationship) {
            StepRepresentationRelationship relationship = (StepRepresentationRelationship) definition;
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
        } else if (definition instanceof StepShapeRepresentationRelationship) {
            StepShapeRepresentationRelationship relationship = (StepShapeRepresentationRelationship) definition;
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
        } else if (definition instanceof StepContextDependentShapeRepresentation) {
            StepContextDependentShapeRepresentation shapeRepresentation = (StepContextDependentShapeRepresentation) definition;
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
        } else if (definition instanceof StepRepresentationRelationshipWithTransformation) {
            StepRepresentationRelationshipWithTransformation relationship = (StepRepresentationRelationshipWithTransformation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendNestedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    relationship.transformationOperator(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepBoxDomain) {
            StepBoxDomain boxDomain = (StepBoxDomain) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, boxDomain, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, boxDomain.corner(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBooleanClippingResult) {
            StepBooleanClippingResult result = (StepBooleanClippingResult) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, result, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, result.firstOperand(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, result.secondOperand(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBooleanResult) {
            StepBooleanResult result = (StepBooleanResult) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, result, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, result.firstOperand(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, result.secondOperand(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedMarker) {
            StepPreDefinedMarker marker = (StepPreDefinedMarker) definition;
            appendPointMarkerStyleTargets(targetsByUsageId, identifiedItem, marker.id(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, marker, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedPointMarkerSymbol) {
            StepPreDefinedPointMarkerSymbol marker = (StepPreDefinedPointMarkerSymbol) definition;
            appendPointMarkerStyleTargets(targetsByUsageId, identifiedItem, marker.id(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, marker, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAnnotationSymbol) {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) definition;
            appendMappedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    annotationSymbol.mappingSource(),
                    annotationSymbol.mappingTarget(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAnnotationText) {
            StepAnnotationText annotationText = (StepAnnotationText) definition;
            appendMappedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    annotationText.mappingSource(),
                    annotationText.mappingTarget(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAnnotationTextCharacter) {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) definition;
            appendMappedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    annotationTextCharacter.mappingSource(),
                    annotationTextCharacter.mappingTarget(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepUserDefinedCurveFont) {
            StepUserDefinedCurveFont curveFont = (StepUserDefinedCurveFont) definition;
            appendMappedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    curveFont.mappingSource(),
                    curveFont.mappingTarget(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepUserDefinedMarker) {
            StepUserDefinedMarker marker = (StepUserDefinedMarker) definition;
            appendMappedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    marker.mappingSource(),
                    marker.mappingTarget(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepUserDefinedTerminatorSymbol) {
            StepUserDefinedTerminatorSymbol symbol = (StepUserDefinedTerminatorSymbol) definition;
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
            if (candidate instanceof StepPropertyDefinition
                    && ((StepPropertyDefinition) candidate).definition().id() == definition.id()) {
                StepPropertyDefinition propertyDefinition = (StepPropertyDefinition) candidate;
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
            if (candidate instanceof StepProductRelationship) {
            StepProductRelationship relationship = (StepProductRelationship) candidate;
                if (relationship.relatingProduct().id() == product.id()) {
                    appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatedProduct(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
                }
                if (relationship.relatedProduct().id() == product.id()) {
                    appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatingProduct(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
                }
            } else if (candidate instanceof StepProductRelatedProductCategory
                    && ((StepProductRelatedProductCategory) candidate).products().stream().anyMatch(related -> related.id() == product.id())) {
                StepProductRelatedProductCategory relatedCategory = (StepProductRelatedProductCategory) candidate;
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
            if (!(candidate instanceof StepProductDefinitionFormationRelationship)) {
                continue;
            }
            StepProductDefinitionFormationRelationship relationship = (StepProductDefinitionFormationRelationship) candidate;
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
            if (!(candidate instanceof StepProductDefinitionRelationship
                    || candidate instanceof StepNextAssemblyUsageOccurrence)) {
                continue;
            }
            if (candidate instanceof StepProductDefinitionRelationship) {
                StepProductDefinitionRelationship relationship = (StepProductDefinitionRelationship) candidate;
                if (relationship.relatingProductDefinition().id() == productDefinition.id()) {
                    appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatedProductDefinition(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
                }
                if (relationship.relatedProductDefinition().id() == productDefinition.id()) {
                    appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatingProductDefinition(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
                }
            } else {
                StepNextAssemblyUsageOccurrence occurrence = (StepNextAssemblyUsageOccurrence) candidate;
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
            if (candidate instanceof StepShapeDefinitionRepresentation
                    && ((StepShapeDefinitionRepresentation) candidate).definition().id() == productDefinitionShape.id()) {
                StepShapeDefinitionRepresentation link = (StepShapeDefinitionRepresentation) candidate;
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
            } else if (candidate instanceof StepContextDependentShapeRepresentation
                    && ((StepContextDependentShapeRepresentation) candidate).representedProductRelation().id() == productDefinitionShape.id()) {
                StepContextDependentShapeRepresentation contextDependent = (StepContextDependentShapeRepresentation) candidate;
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
                    if (contextDependent.representationRelationship() instanceof StepRepresentationRelationshipWithTransformation) {
                        StepRepresentationRelationshipWithTransformation transformed = (StepRepresentationRelationshipWithTransformation) contextDependent.representationRelationship();
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
            if (candidate instanceof StepRepresentationRelationship
                    && referencesRepresentation(((StepRepresentationRelationship) candidate).rep1(),
                            ((StepRepresentationRelationship) candidate).rep2(), representation.id())) {
                StepRepresentationRelationship relationship = (StepRepresentationRelationship) candidate;
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem.id(),
                        relationship,
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            } else if (candidate instanceof StepRepresentationRelationshipWithTransformation
                    && referencesRepresentation(((StepRepresentationRelationshipWithTransformation) candidate).rep1(),
                            ((StepRepresentationRelationshipWithTransformation) candidate).rep2(), representation.id())) {
                StepRepresentationRelationshipWithTransformation transformed = (StepRepresentationRelationshipWithTransformation) candidate;
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
            } else if (candidate instanceof StepShapeRepresentationRelationship
                    && referencesRepresentation(((StepShapeRepresentationRelationship) candidate).rep1(),
                            ((StepShapeRepresentationRelationship) candidate).rep2(), representation.id())) {
                StepShapeRepresentationRelationship relationship = (StepShapeRepresentationRelationship) candidate;
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
            if (candidate instanceof StepProductDefinitionShape
                    && ((StepProductDefinitionShape) candidate).definition().id() == productDefinition.id()) {
                StepProductDefinitionShape shape = (StepProductDefinitionShape) candidate;
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
            if (candidate instanceof StepProductDefinitionShape
                    && ((StepProductDefinitionShape) candidate).definition().id() == occurrence.id()) {
                StepProductDefinitionShape shape = (StepProductDefinitionShape) candidate;
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
            if (!(candidate instanceof StepPropertyDefinitionRelationship)) {
                continue;
            }
            StepPropertyDefinitionRelationship relationship = (StepPropertyDefinitionRelationship) candidate;
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
            if (candidate instanceof StepPropertyDefinitionRepresentation
                    && ((StepPropertyDefinitionRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepPropertyDefinitionRepresentation representationLink = (StepPropertyDefinitionRepresentation) candidate;
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
            } else if (candidate instanceof StepAttributeAssertion
                    && ((StepAttributeAssertion) candidate).definition().id() == propertyDefinition.id()) {
                StepAttributeAssertion representationLink = (StepAttributeAssertion) candidate;
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
            } else if (candidate instanceof StepActionPropertyRepresentation
                    && ((StepActionPropertyRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepActionPropertyRepresentation representationLink = (StepActionPropertyRepresentation) candidate;
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
            } else if (candidate instanceof StepContactRatioRepresentation
                    && ((StepContactRatioRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepContactRatioRepresentation representationLink = (StepContactRatioRepresentation) candidate;
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
            } else if (candidate instanceof StepKinematicPropertyDefinitionRepresentation
                    && ((StepKinematicPropertyDefinitionRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepKinematicPropertyDefinitionRepresentation representationLink = (StepKinematicPropertyDefinitionRepresentation) candidate;
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
            } else if (candidate instanceof StepKinematicPropertyMechanismRepresentation
                    && ((StepKinematicPropertyMechanismRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepKinematicPropertyMechanismRepresentation representationLink = (StepKinematicPropertyMechanismRepresentation) candidate;
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
            } else if (candidate instanceof StepKinematicPropertyRepresentationRelation
                    && ((StepKinematicPropertyRepresentationRelation) candidate).definition().id() == propertyDefinition.id()) {
                StepKinematicPropertyRepresentationRelation representationLink = (StepKinematicPropertyRepresentationRelation) candidate;
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
            } else if (candidate instanceof StepKinematicPropertyTopologyRepresentation
                    && ((StepKinematicPropertyTopologyRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepKinematicPropertyTopologyRepresentation representationLink = (StepKinematicPropertyTopologyRepresentation) candidate;
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
            } else if (candidate instanceof StepResourcePropertyRepresentation
                    && ((StepResourcePropertyRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepResourcePropertyRepresentation representationLink = (StepResourcePropertyRepresentation) candidate;
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
            } else if (candidate instanceof StepForwardChainingRulePremise
                    && ((StepForwardChainingRulePremise) candidate).definition().id() == propertyDefinition.id()) {
                StepForwardChainingRulePremise representationLink = (StepForwardChainingRulePremise) candidate;
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
            } else if (candidate instanceof StepBackChainingRuleBody
                    && ((StepBackChainingRuleBody) candidate).definition().id() == propertyDefinition.id()) {
                StepBackChainingRuleBody representationLink = (StepBackChainingRuleBody) candidate;
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
            } else if (candidate instanceof StepPlacedDatumTargetFeature
                    && ((StepPlacedDatumTargetFeature) candidate).definition().id() == propertyDefinition.id()) {
                StepPlacedDatumTargetFeature representationLink = (StepPlacedDatumTargetFeature) candidate;
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
        if (candidate instanceof StepPropertyDefinitionRepresentation
                && ((StepPropertyDefinitionRepresentation) candidate).definition().id() == propertyDefinitionId) {
            StepPropertyDefinitionRepresentation representationLink = (StepPropertyDefinitionRepresentation) candidate;
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepAttributeAssertion
                && ((StepAttributeAssertion) candidate).definition().id() == propertyDefinitionId) {
            StepAttributeAssertion representationLink = (StepAttributeAssertion) candidate;
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepActionPropertyRepresentation
                && ((StepActionPropertyRepresentation) candidate).definition().id() == propertyDefinitionId) {
            StepActionPropertyRepresentation representationLink = (StepActionPropertyRepresentation) candidate;
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepContactRatioRepresentation
                && ((StepContactRatioRepresentation) candidate).definition().id() == propertyDefinitionId) {
            StepContactRatioRepresentation representationLink = (StepContactRatioRepresentation) candidate;
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepKinematicPropertyDefinitionRepresentation
                && ((StepKinematicPropertyDefinitionRepresentation) candidate).definition().id() == propertyDefinitionId) {
            StepKinematicPropertyDefinitionRepresentation representationLink = (StepKinematicPropertyDefinitionRepresentation) candidate;
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepKinematicPropertyMechanismRepresentation
                && ((StepKinematicPropertyMechanismRepresentation) candidate).definition().id() == propertyDefinitionId) {
            StepKinematicPropertyMechanismRepresentation representationLink = (StepKinematicPropertyMechanismRepresentation) candidate;
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepKinematicPropertyRepresentationRelation
                && ((StepKinematicPropertyRepresentationRelation) candidate).definition().id() == propertyDefinitionId) {
            StepKinematicPropertyRepresentationRelation representationLink = (StepKinematicPropertyRepresentationRelation) candidate;
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepKinematicPropertyTopologyRepresentation
                && ((StepKinematicPropertyTopologyRepresentation) candidate).definition().id() == propertyDefinitionId) {
            StepKinematicPropertyTopologyRepresentation representationLink = (StepKinematicPropertyTopologyRepresentation) candidate;
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepResourcePropertyRepresentation
                && ((StepResourcePropertyRepresentation) candidate).definition().id() == propertyDefinitionId) {
            StepResourcePropertyRepresentation representationLink = (StepResourcePropertyRepresentation) candidate;
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepForwardChainingRulePremise
                && ((StepForwardChainingRulePremise) candidate).definition().id() == propertyDefinitionId) {
            StepForwardChainingRulePremise representationLink = (StepForwardChainingRulePremise) candidate;
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepBackChainingRuleBody
                && ((StepBackChainingRuleBody) candidate).definition().id() == propertyDefinitionId) {
            StepBackChainingRuleBody representationLink = (StepBackChainingRuleBody) candidate;
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepPlacedDatumTargetFeature
                && ((StepPlacedDatumTargetFeature) candidate).definition().id() == propertyDefinitionId) {
            StepPlacedDatumTargetFeature representationLink = (StepPlacedDatumTargetFeature) candidate;
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
            if (!(candidate instanceof StepGroupRelationship)) {
                continue;
            }
            StepGroupRelationship relationship = (StepGroupRelationship) candidate;
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
            if (!(candidate instanceof StepGeneralPropertyRelationship)) {
                continue;
            }
            StepGeneralPropertyRelationship relationship = (StepGeneralPropertyRelationship) candidate;
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
            if (!(candidate instanceof StepDocumentRelationship)) {
                continue;
            }
            StepDocumentRelationship relationship = (StepDocumentRelationship) candidate;
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
            if (candidate instanceof StepApprovalPersonOrganization
                    && ((StepApprovalPersonOrganization) candidate).authorizedApproval().id() == approval.id()) {
                StepApprovalPersonOrganization personOrganization = (StepApprovalPersonOrganization) candidate;
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
            } else if (candidate instanceof StepApprovalDateTime
                    && ((StepApprovalDateTime) candidate).datedApproval().id() == approval.id()) {
                StepApprovalDateTime approvalDateTime = (StepApprovalDateTime) candidate;
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
            if (candidate instanceof StepPointStyle && ((StepPointStyle) candidate).marker().id() == markerId) {
            StepPointStyle pointStyle = (StepPointStyle) candidate;
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
            if (!(candidate instanceof StepOrganizationRelationship)) {
                continue;
            }
            StepOrganizationRelationship relationship = (StepOrganizationRelationship) candidate;
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
            if (!(candidate instanceof StepEffectivityRelationship)) {
                continue;
            }
            StepEffectivityRelationship relationship = (StepEffectivityRelationship) candidate;
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
            if (!(candidate instanceof StepProductCategoryRelationship)) {
                continue;
            }
            StepProductCategoryRelationship relationship = (StepProductCategoryRelationship) candidate;
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
                if (!(candidate instanceof StepEffectivityRelationship)) {
                    continue;
                }
                StepEffectivityRelationship relationship = (StepEffectivityRelationship) candidate;
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
            if (candidate instanceof StepProductDefinitionEffectivity
                    && linkedEffectivityNames.contains(((StepProductDefinitionEffectivity) candidate).effectivityId())) {
                StepProductDefinitionEffectivity productDefinitionEffectivity = (StepProductDefinitionEffectivity) candidate;
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
            if (!(candidate instanceof StepExternalSourceRelationship)) {
                continue;
            }
            StepExternalSourceRelationship relationship = (StepExternalSourceRelationship) candidate;
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
                if (!(candidate instanceof StepExternalSourceRelationship)) {
                    continue;
                }
                StepExternalSourceRelationship relationship = (StepExternalSourceRelationship) candidate;
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
            if (candidate instanceof StepExternallyDefinedItem
                    && linkedSourceIds.contains(((StepExternallyDefinedItem) candidate).source().id())) {
                StepExternallyDefinedItem item = (StepExternallyDefinedItem) candidate;
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
        if (mappingSource instanceof StepRepresentationMap) {
            StepRepresentationMap representationMap = (StepRepresentationMap) mappingSource;
            appendRepresentationMapDefinitionTargets(targetsByUsageId, identifiedItem, representationMap, instanceIdsByTargetId);
        } else if (mappingSource instanceof StepSymbolRepresentationMap) {
            StepSymbolRepresentationMap representationMap = (StepSymbolRepresentationMap) mappingSource;
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
        if (placement instanceof StepAxis1Placement) {
            StepAxis1Placement axis1Placement = (StepAxis1Placement) placement;
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
        } else if (placement instanceof StepAxis2Placement2D) {
            StepAxis2Placement2D axis2Placement2D = (StepAxis2Placement2D) placement;
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
        } else if (placement instanceof StepAxis2Placement3D) {
            StepAxis2Placement3D axis2Placement3D = (StepAxis2Placement3D) placement;
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
            if (!(candidate instanceof StepShapeAspectRelationship)) {
                continue;
            }
            StepShapeAspectRelationship relationship = (StepShapeAspectRelationship) candidate;
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
        if (relatedDefinition instanceof StepPropertyDefinition) {
            StepPropertyDefinition propertyDefinition = (StepPropertyDefinition) relatedDefinition;
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

    public static String definitionTypeName(StepEntity definition) {
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
            if (value instanceof String) { String name = (String) value;
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
        if (entity instanceof StepPropertyDefinition) {
            StepPropertyDefinition propertyDefinition = (StepPropertyDefinition) entity;
            targets.addAll(collectSemanticTargets(propertyDefinition.definition(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepPropertyDefinitionRepresentation
                    && ((StepPropertyDefinitionRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepPropertyDefinitionRepresentation representationLink = (StepPropertyDefinitionRepresentation) candidate;
                targets.add(representationLink.usedRepresentation());
            } else if (candidate instanceof StepActionPropertyRepresentation
                    && ((StepActionPropertyRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepActionPropertyRepresentation representationLink = (StepActionPropertyRepresentation) candidate;
                targets.add(representationLink.usedRepresentation());
            } else if (candidate instanceof StepContactRatioRepresentation
                    && ((StepContactRatioRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepContactRatioRepresentation representationLink = (StepContactRatioRepresentation) candidate;
                targets.add(representationLink.usedRepresentation());
            } else if (candidate instanceof StepKinematicPropertyDefinitionRepresentation
                    && ((StepKinematicPropertyDefinitionRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepKinematicPropertyDefinitionRepresentation representationLink = (StepKinematicPropertyDefinitionRepresentation) candidate;
                targets.add(representationLink.usedRepresentation());
            } else if (candidate instanceof StepKinematicPropertyMechanismRepresentation
                    && ((StepKinematicPropertyMechanismRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepKinematicPropertyMechanismRepresentation representationLink = (StepKinematicPropertyMechanismRepresentation) candidate;
                targets.add(representationLink.usedRepresentation());
            } else if (candidate instanceof StepKinematicPropertyRepresentationRelation
                    && ((StepKinematicPropertyRepresentationRelation) candidate).definition().id() == propertyDefinition.id()) {
                StepKinematicPropertyRepresentationRelation representationLink = (StepKinematicPropertyRepresentationRelation) candidate;
                targets.add(representationLink.usedRepresentation());
            } else if (candidate instanceof StepKinematicPropertyTopologyRepresentation
                    && ((StepKinematicPropertyTopologyRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepKinematicPropertyTopologyRepresentation representationLink = (StepKinematicPropertyTopologyRepresentation) candidate;
                targets.add(representationLink.usedRepresentation());
            } else if (candidate instanceof StepResourcePropertyRepresentation
                    && ((StepResourcePropertyRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepResourcePropertyRepresentation representationLink = (StepResourcePropertyRepresentation) candidate;
                targets.add(representationLink.usedRepresentation());
            } else if (candidate instanceof StepForwardChainingRulePremise
                    && ((StepForwardChainingRulePremise) candidate).definition().id() == propertyDefinition.id()) {
                StepForwardChainingRulePremise representationLink = (StepForwardChainingRulePremise) candidate;
                targets.add(representationLink.usedRepresentation());
            } else if (candidate instanceof StepBackChainingRuleBody
                    && ((StepBackChainingRuleBody) candidate).definition().id() == propertyDefinition.id()) {
                StepBackChainingRuleBody representationLink = (StepBackChainingRuleBody) candidate;
                targets.add(representationLink.usedRepresentation());
            } else if (candidate instanceof StepPlacedDatumTargetFeature
                    && ((StepPlacedDatumTargetFeature) candidate).definition().id() == propertyDefinition.id()) {
                StepPlacedDatumTargetFeature datumTargetFeature = (StepPlacedDatumTargetFeature) candidate;
                targets.add(datumTargetFeature.usedRepresentation());
            } else if (candidate instanceof StepPropertyDefinitionRelationship) {
                StepPropertyDefinitionRelationship relationship = (StepPropertyDefinitionRelationship) candidate;
                if (relationship.relatingPropertyDefinition().id() == propertyDefinition.id()) {
                    targets.addAll(collectSemanticTargets(relationship.relatedPropertyDefinition(), resolved, visiting));
                }
                if (relationship.relatedPropertyDefinition().id() == propertyDefinition.id()) {
                    targets.addAll(collectSemanticTargets(relationship.relatingPropertyDefinition(), resolved, visiting));
                }
            }
        }
    } else if (entity instanceof StepDescriptiveRepresentationItem) {
            StepDescriptiveRepresentationItem item = (StepDescriptiveRepresentationItem) entity;
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting));
        } else if (entity instanceof StepValueRepresentationItem) {
            StepValueRepresentationItem item = (StepValueRepresentationItem) entity;
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting));
        } else if (entity instanceof StepMeasureRepresentationItem) {
            StepMeasureRepresentationItem item = (StepMeasureRepresentationItem) entity;
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting));
            targets.addAll(collectSemanticTargets(item.unit(), resolved, visiting));
        } else if (entity instanceof StepMeasureWithUnit) {
            StepMeasureWithUnit measure = (StepMeasureWithUnit) entity;
            targets.addAll(collectSemanticTargets(measure.unitComponent(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(measure.id(), resolved, visiting));
        } else if (entity instanceof StepTypedMeasureWithUnit) {
            StepTypedMeasureWithUnit measure = (StepTypedMeasureWithUnit) entity;
            targets.addAll(collectSemanticTargets(measure.unitComponent(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(measure.id(), resolved, visiting));
        } else if (entity instanceof StepUncertaintyMeasureWithUnit) {
            StepUncertaintyMeasureWithUnit measure = (StepUncertaintyMeasureWithUnit) entity;
            targets.addAll(collectSemanticTargets(measure.unitComponent(), resolved, visiting));
            targets.addAll(collectTargetsForAssignedUncertainty(measure.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(measure.id(), resolved, visiting));
        } else if (entity instanceof StepCartesianPoint) {
            StepCartesianPoint point = (StepCartesianPoint) entity;
            targets.addAll(collectTargetsReferencingEntity(point.id(), resolved, visiting));
        } else if (entity instanceof StepDirection) {
            StepDirection direction = (StepDirection) entity;
            targets.addAll(collectTargetsReferencingEntity(direction.id(), resolved, visiting));
        } else if (entity instanceof StepVector) {
            StepVector vector = (StepVector) entity;
            targets.addAll(collectSemanticTargets(vector.orientation(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(vector.id(), resolved, visiting));
        } else if (entity instanceof StepAxis1Placement) {
            StepAxis1Placement placement = (StepAxis1Placement) entity;
            targets.addAll(collectSemanticTargets(placement.location(), resolved, visiting));
            targets.addAll(collectSemanticTargets(placement.axis(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(placement.id(), resolved, visiting));
        } else if (entity instanceof StepAxis2Placement2D) {
            StepAxis2Placement2D placement = (StepAxis2Placement2D) entity;
            targets.addAll(collectSemanticTargets(placement.location(), resolved, visiting));
            if (placement.refDirection() != null) {
                targets.addAll(collectSemanticTargets(placement.refDirection(), resolved, visiting));
            }
            targets.addAll(collectTargetsReferencingEntity(placement.id(), resolved, visiting));
        } else if (entity instanceof StepAxis2Placement3D) {
            StepAxis2Placement3D placement = (StepAxis2Placement3D) entity;
            targets.addAll(collectSemanticTargets(placement.location(), resolved, visiting));
            if (placement.axis() != null) {
                targets.addAll(collectSemanticTargets(placement.axis(), resolved, visiting));
            }
            if (placement.refDirection() != null) {
                targets.addAll(collectSemanticTargets(placement.refDirection(), resolved, visiting));
            }
            targets.addAll(collectTargetsReferencingEntity(placement.id(), resolved, visiting));
        } else if (entity instanceof StepAddress) {
            StepAddress address = (StepAddress) entity;
            targets.addAll(collectTargetsReferencingEntity(address.id(), resolved, visiting));
        } else if (entity instanceof StepCharacterizedObject) {
            StepCharacterizedObject characterizedObject = (StepCharacterizedObject) entity;
            targets.addAll(collectTargetsReferencingEntity(characterizedObject.id(), resolved, visiting));
        } else if (entity instanceof StepPoint) {
            StepPoint point = (StepPoint) entity;
            targets.addAll(collectTargetsReferencingEntity(point.id(), resolved, visiting));
        } else if (entity instanceof StepPointSet) {
            StepPointSet pointSet = (StepPointSet) entity;
            targets.addAll(collectSemanticTargets(pointSet.points(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(pointSet.id(), resolved, visiting));
        } else if (entity instanceof StepPolyline) {
            StepPolyline polyline = (StepPolyline) entity;
            targets.addAll(collectSemanticTargets(polyline.points(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(polyline.id(), resolved, visiting));
        } else if (entity instanceof StepProfileDef) {
            StepProfileDef profile = (StepProfileDef) entity;
            if (profile.position() != null) {
                targets.addAll(collectSemanticTargets(profile.position(), resolved, visiting));
            }
            targets.addAll(collectSemanticTargets(profile.curves(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(profile.id(), resolved, visiting));
        } else if (entity instanceof StepLine) {
            StepLine line = (StepLine) entity;
            targets.addAll(collectSemanticTargets(line.point(), resolved, visiting));
            targets.addAll(collectSemanticTargets(line.vector(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(line.id(), resolved, visiting));
        } else if (entity instanceof StepCircle) {
            StepCircle circle = (StepCircle) entity;
            targets.addAll(collectSemanticTargets(circle.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(circle.id(), resolved, visiting));
        } else if (entity instanceof StepEllipse) {
            StepEllipse ellipse = (StepEllipse) entity;
            targets.addAll(collectSemanticTargets(ellipse.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(ellipse.id(), resolved, visiting));
        } else if (entity instanceof StepCurve) {
            StepCurve curve = (StepCurve) entity;
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepBoundedCurve) {
            StepBoundedCurve curve = (StepBoundedCurve) entity;
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepConicCurve) {
            StepConicCurve curve = (StepConicCurve) entity;
            targets.addAll(collectSemanticTargets(curve.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepBSplineCurve) {
            StepBSplineCurve curve = (StepBSplineCurve) entity;
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepBezierCurve) {
            StepBezierCurve curve = (StepBezierCurve) entity;
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepBSplineCurveWithKnots) {
            StepBSplineCurveWithKnots curve = (StepBSplineCurveWithKnots) entity;
            targets.addAll(collectSemanticTargets(curve.controlPoints(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepRationalBSplineCurve) {
            StepRationalBSplineCurve curve = (StepRationalBSplineCurve) entity;
            targets.addAll(collectSemanticTargets(curve.controlPoints(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepPiecewiseBezierCurve) {
            StepPiecewiseBezierCurve curve = (StepPiecewiseBezierCurve) entity;
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepUniformCurve) {
            StepUniformCurve curve = (StepUniformCurve) entity;
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepQuasiUniformCurve) {
            StepQuasiUniformCurve curve = (StepQuasiUniformCurve) entity;
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepOffsetCurve2D) {
            StepOffsetCurve2D curve = (StepOffsetCurve2D) entity;
            targets.addAll(collectSemanticTargets(curve.basisCurve(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepOffsetCurve3D) {
            StepOffsetCurve3D curve = (StepOffsetCurve3D) entity;
            targets.addAll(collectSemanticTargets(curve.basisCurve(), resolved, visiting));
            targets.addAll(collectSemanticTargets(curve.refDirection(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepOrientedCurve) {
            StepOrientedCurve curve = (StepOrientedCurve) entity;
            targets.addAll(collectSemanticTargets(curve.curveElement(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepTrimmedCurve) {
            StepTrimmedCurve curve = (StepTrimmedCurve) entity;
            targets.addAll(collectSemanticTargets(curve.basisCurve(), resolved, visiting));
            for (StepValue trim : curve.trim1()) {
                if (trim instanceof StepValue.ReferenceValue && resolved.containsKey(((StepValue.ReferenceValue) trim).id())) {
                    StepValue.ReferenceValue ref = (StepValue.ReferenceValue) trim;
                    targets.addAll(collectSemanticTargets(resolved.get(ref.id()), resolved, visiting));
                }
            }
            for (StepValue trim : curve.trim2()) {
                if (trim instanceof StepValue.ReferenceValue && resolved.containsKey(((StepValue.ReferenceValue) trim).id())) {
                    StepValue.ReferenceValue ref = (StepValue.ReferenceValue) trim;
                    targets.addAll(collectSemanticTargets(resolved.get(ref.id()), resolved, visiting));
                }
            }
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceCurve) {
            StepSurfaceCurve curve = (StepSurfaceCurve) entity;
            targets.addAll(collectSemanticTargets(curve.curve3d(), resolved, visiting));
            targets.addAll(collectSemanticTargets(curve.associatedGeometry(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepSeamCurve) {
            StepSeamCurve curve = (StepSeamCurve) entity;
            targets.addAll(collectSemanticTargets(curve.curve3d(), resolved, visiting));
            targets.addAll(collectSemanticTargets(curve.associatedGeometry(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepPcurve) {
            StepPcurve curve = (StepPcurve) entity;
            targets.addAll(collectSemanticTargets(curve.basisSurface(), resolved, visiting));
            targets.addAll(collectSemanticTargets(curve.referenceToCurve(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepCompositeCurve) {
            StepCompositeCurve curve = (StepCompositeCurve) entity;
            targets.addAll(collectSemanticTargets(curve.segments(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepCompositeCurveOnSurface) {
            StepCompositeCurveOnSurface curve = (StepCompositeCurveOnSurface) entity;
            targets.addAll(collectSemanticTargets(curve.segments(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepCompositeCurveSegment) {
            StepCompositeCurveSegment segment = (StepCompositeCurveSegment) entity;
            targets.addAll(collectSemanticTargets(segment.parentCurve(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(segment.id(), resolved, visiting));
        } else if (entity instanceof StepSurface) {
            StepSurface surface = (StepSurface) entity;
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepBoundedSurface) {
            StepBoundedSurface surface = (StepBoundedSurface) entity;
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepBSplineSurface) {
            StepBSplineSurface surface = (StepBSplineSurface) entity;
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepBezierSurface) {
            StepBezierSurface surface = (StepBezierSurface) entity;
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepBSplineSurfaceWithKnots) {
            StepBSplineSurfaceWithKnots surface = (StepBSplineSurfaceWithKnots) entity;
            targets.addAll(collectSemanticTargets(surface.controlPoints().stream().flatMap(List::stream).collect(Collectors.toList()),
                    resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepRationalBSplineSurface) {
            StepRationalBSplineSurface surface = (StepRationalBSplineSurface) entity;
            targets.addAll(collectSemanticTargets(surface.controlPoints().stream().flatMap(List::stream).collect(Collectors.toList()),
                    resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepPiecewiseBezierSurface) {
            StepPiecewiseBezierSurface surface = (StepPiecewiseBezierSurface) entity;
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepUniformSurface) {
            StepUniformSurface surface = (StepUniformSurface) entity;
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepQuasiUniformSurface) {
            StepQuasiUniformSurface surface = (StepQuasiUniformSurface) entity;
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepPlane) {
            StepPlane plane = (StepPlane) entity;
            targets.addAll(collectSemanticTargets(plane.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(plane.id(), resolved, visiting));
        } else if (entity instanceof StepCylindricalSurface) {
            StepCylindricalSurface surface = (StepCylindricalSurface) entity;
            targets.addAll(collectSemanticTargets(surface.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepConicalSurface) {
            StepConicalSurface surface = (StepConicalSurface) entity;
            targets.addAll(collectSemanticTargets(surface.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepToroidalSurface) {
            StepToroidalSurface surface = (StepToroidalSurface) entity;
            targets.addAll(collectSemanticTargets(surface.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceOfLinearExtrusion) {
            StepSurfaceOfLinearExtrusion surface = (StepSurfaceOfLinearExtrusion) entity;
            targets.addAll(collectSemanticTargets(surface.sweptCurve(), resolved, visiting));
            targets.addAll(collectSemanticTargets(surface.extrusionAxis(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceOfRevolution) {
            StepSurfaceOfRevolution surface = (StepSurfaceOfRevolution) entity;
            targets.addAll(collectSemanticTargets(surface.sweptCurve(), resolved, visiting));
            targets.addAll(collectSemanticTargets(surface.axisPosition(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepRectangularTrimmedSurface) {
            StepRectangularTrimmedSurface surface = (StepRectangularTrimmedSurface) entity;
            targets.addAll(collectSemanticTargets(surface.basisSurface(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepCurveBoundedSurface) {
            StepCurveBoundedSurface surface = (StepCurveBoundedSurface) entity;
            targets.addAll(collectSemanticTargets(surface.basisSurface(), resolved, visiting));
            targets.addAll(collectSemanticTargets(surface.boundaries(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepOrientedSurface) {
            StepOrientedSurface surface = (StepOrientedSurface) entity;
            targets.addAll(collectSemanticTargets(surface.surfaceElement(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepOffsetSurface) {
            StepOffsetSurface surface = (StepOffsetSurface) entity;
            targets.addAll(collectSemanticTargets(surface.basisSurface(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepSphericalSurface) {
            StepSphericalSurface surface = (StepSphericalSurface) entity;
            targets.addAll(collectSemanticTargets(surface.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepDegenerateToroidalSurface) {
            StepDegenerateToroidalSurface surface = (StepDegenerateToroidalSurface) entity;
            targets.addAll(collectSemanticTargets(surface.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepShellBasedSurfaceModel) {
            StepShellBasedSurfaceModel model = (StepShellBasedSurfaceModel) entity;
            targets.addAll(collectSemanticTargets(model.shells(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(model.id(), resolved, visiting));
        } else if (entity instanceof StepFaceBasedSurfaceModel) {
            StepFaceBasedSurfaceModel model = (StepFaceBasedSurfaceModel) entity;
            targets.addAll(collectSemanticTargets(model.faceSets(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(model.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceModel) {
            StepSurfaceModel model = (StepSurfaceModel) entity;
            targets.addAll(collectTargetsReferencingEntity(model.id(), resolved, visiting));
        } else if (entity instanceof StepSolidModel) {
            StepSolidModel model = (StepSolidModel) entity;
            targets.addAll(collectTargetsReferencingEntity(model.id(), resolved, visiting));
        } else if (entity instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet set = (StepGeometricCurveSet) entity;
            targets.addAll(collectSemanticTargets(set.elements(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(set.id(), resolved, visiting));
        } else if (entity instanceof StepGeometricSet) {
            StepGeometricSet set = (StepGeometricSet) entity;
            targets.addAll(collectSemanticTargets(set.elements(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(set.id(), resolved, visiting));
        } else if (entity instanceof StepBoxDomain) {
            StepBoxDomain boxDomain = (StepBoxDomain) entity;
            targets.addAll(collectSemanticTargets(boxDomain.corner(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(boxDomain.id(), resolved, visiting));
        } else if (entity instanceof StepDimensionalExponents) {
            StepDimensionalExponents exponents = (StepDimensionalExponents) entity;
            targets.addAll(collectTargetsReferencingEntity(exponents.id(), resolved, visiting));
        } else if (entity instanceof StepDegeneratePcurve) {
            StepDegeneratePcurve curve = (StepDegeneratePcurve) entity;
            targets.addAll(collectSemanticTargets(curve.basisSurface(), resolved, visiting));
            targets.addAll(collectSemanticTargets(curve.referenceToCurve(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepHalfSpaceSolid) {
            StepHalfSpaceSolid halfSpaceSolid = (StepHalfSpaceSolid) entity;
            targets.addAll(collectSemanticTargets(halfSpaceSolid.baseSurface(), resolved, visiting));
            if (halfSpaceSolid.enclosure() != null) {
                targets.addAll(collectSemanticTargets(halfSpaceSolid.enclosure(), resolved, visiting));
            }
            targets.addAll(collectTargetsReferencingEntity(halfSpaceSolid.id(), resolved, visiting));
        } else if (entity instanceof StepVertex) {
            StepVertex vertex = (StepVertex) entity;
            targets.addAll(collectTargetsReferencingEntity(vertex.id(), resolved, visiting));
        } else if (entity instanceof StepVertexPoint) {
            StepVertexPoint vertexPoint = (StepVertexPoint) entity;
            targets.addAll(collectSemanticTargets(vertexPoint.point(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(vertexPoint.id(), resolved, visiting));
        } else if (entity instanceof StepEdge) {
            StepEdge edge = (StepEdge) entity;
            targets.addAll(collectTargetsReferencingEntity(edge.id(), resolved, visiting));
        } else if (entity instanceof StepConnectedEdgeSet) {
            StepConnectedEdgeSet edgeSet = (StepConnectedEdgeSet) entity;
            targets.addAll(collectSemanticTargets(edgeSet.edges(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(edgeSet.id(), resolved, visiting));
        } else if (entity instanceof StepEdgeBasedWireframeModel) {
            StepEdgeBasedWireframeModel model = (StepEdgeBasedWireframeModel) entity;
            targets.addAll(collectSemanticTargets(model.boundaries(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(model.id(), resolved, visiting));
        } else if (entity instanceof StepPolyLoop) {
            StepPolyLoop loop = (StepPolyLoop) entity;
            targets.addAll(collectSemanticTargets(loop.polygon(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(loop.id(), resolved, visiting));
        } else if (entity instanceof StepLoop) {
            StepLoop loop = (StepLoop) entity;
            targets.addAll(collectTargetsReferencingEntity(loop.id(), resolved, visiting));
        } else if (entity instanceof StepEdgeLoop) {
            StepEdgeLoop loop = (StepEdgeLoop) entity;
            targets.addAll(collectSemanticTargets(loop.edges(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(loop.id(), resolved, visiting));
        } else if (entity instanceof StepVertexLoop) {
            StepVertexLoop loop = (StepVertexLoop) entity;
            targets.addAll(collectSemanticTargets(loop.loopVertex(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(loop.id(), resolved, visiting));
        } else if (entity instanceof com.minicad.step.model.StepFaceBound) {
            com.minicad.step.model.StepFaceBound faceBound = (com.minicad.step.model.StepFaceBound) entity;
            targets.addAll(collectSemanticTargets(faceBound.loop(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(faceBound.id(), resolved, visiting));
        } else if (entity instanceof StepFace) {
            StepFace face = (StepFace) entity;
            targets.addAll(collectTargetsReferencingEntity(face.id(), resolved, visiting));
        } else if (entity instanceof StepAdvancedFace) {
            StepAdvancedFace face = (StepAdvancedFace) entity;
            targets.addAll(collectSemanticTargets(face.bounds(), resolved, visiting));
            targets.addAll(collectSemanticTargets(face.faceGeometry(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(face.id(), resolved, visiting));
        } else if (entity instanceof StepFaceSurface) {
            StepFaceSurface face = (StepFaceSurface) entity;
            targets.addAll(collectSemanticTargets(face.bounds(), resolved, visiting));
            targets.addAll(collectSemanticTargets(face.faceGeometry(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(face.id(), resolved, visiting));
        } else if (entity instanceof StepOpenShell) {
            StepOpenShell shell = (StepOpenShell) entity;
            targets.addAll(collectSemanticTargets(shell.faces(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(shell.id(), resolved, visiting));
        } else if (entity instanceof StepSurfacedOpenShell) {
            StepSurfacedOpenShell shell = (StepSurfacedOpenShell) entity;
            targets.addAll(collectSemanticTargets(shell.faces(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(shell.id(), resolved, visiting));
        } else if (entity instanceof StepOrientedOpenShell) {
            StepOrientedOpenShell shell = (StepOrientedOpenShell) entity;
            targets.addAll(collectSemanticTargets(shell.openShellElement(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(shell.id(), resolved, visiting));
        } else if (entity instanceof StepClosedShell) {
            StepClosedShell shell = (StepClosedShell) entity;
            targets.addAll(collectSemanticTargets(shell.faces(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(shell.id(), resolved, visiting));
        } else if (entity instanceof StepOrientedClosedShell) {
            StepOrientedClosedShell shell = (StepOrientedClosedShell) entity;
            targets.addAll(collectSemanticTargets(shell.closedShellElement(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(shell.id(), resolved, visiting));
        } else if (entity instanceof StepConnectedFaceSet) {
            StepConnectedFaceSet faceSet = (StepConnectedFaceSet) entity;
            targets.addAll(collectSemanticTargets(faceSet.faces(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(faceSet.id(), resolved, visiting));
        } else if (entity instanceof StepConnectedFaceSubSet) {
            StepConnectedFaceSubSet faceSet = (StepConnectedFaceSubSet) entity;
            targets.addAll(collectSemanticTargets(faceSet.faces(), resolved, visiting));
            targets.addAll(collectSemanticTargets(faceSet.parentFaceSet(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(faceSet.id(), resolved, visiting));
        } else if (entity instanceof StepOrientedEdge) {
            StepOrientedEdge edge = (StepOrientedEdge) entity;
            targets.addAll(collectSemanticTargets(edge.edgeElement(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(edge.id(), resolved, visiting));
        } else if (entity instanceof StepOrientedFace) {
            StepOrientedFace face = (StepOrientedFace) entity;
            targets.addAll(collectSemanticTargets(face.faceElement(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(face.id(), resolved, visiting));
        } else if (entity instanceof StepPath) {
            StepPath path = (StepPath) entity;
            targets.addAll(collectSemanticTargets(path.edges(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(path.id(), resolved, visiting));
        } else if (entity instanceof StepOpenPath) {
            StepOpenPath path = (StepOpenPath) entity;
            targets.addAll(collectSemanticTargets(path.edges(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(path.id(), resolved, visiting));
        } else if (entity instanceof StepSubpath) {
            StepSubpath subpath = (StepSubpath) entity;
            targets.addAll(collectSemanticTargets(subpath.edges(), resolved, visiting));
            targets.addAll(collectSemanticTargets(subpath.parentPath(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(subpath.id(), resolved, visiting));
        } else if (entity instanceof StepOrientedPath) {
            StepOrientedPath path = (StepOrientedPath) entity;
            targets.addAll(collectSemanticTargets(path.pathElement(), resolved, visiting));
            targets.addAll(collectSemanticTargets(path.edges(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(path.id(), resolved, visiting));
        } else if (entity instanceof StepWireShell) {
            StepWireShell wireShell = (StepWireShell) entity;
            targets.addAll(collectSemanticTargets(wireShell.loops(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(wireShell.id(), resolved, visiting));
        } else if (entity instanceof StepVertexShell) {
            StepVertexShell vertexShell = (StepVertexShell) entity;
            targets.addAll(collectSemanticTargets(vertexShell.extent(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(vertexShell.id(), resolved, visiting));
        } else if (entity instanceof StepShellBasedWireframeModel) {
            StepShellBasedWireframeModel model = (StepShellBasedWireframeModel) entity;
            targets.addAll(collectSemanticTargets(model.boundaries(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(model.id(), resolved, visiting));
        } else if (entity instanceof StepSubedge) {
            StepSubedge subedge = (StepSubedge) entity;
            targets.addAll(collectSemanticTargets(subedge.start(), resolved, visiting));
            targets.addAll(collectSemanticTargets(subedge.end(), resolved, visiting));
            targets.addAll(collectSemanticTargets(subedge.parentEdge(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(subedge.id(), resolved, visiting));
        } else if (entity instanceof StepCartesianTransformationOperator) {
            StepCartesianTransformationOperator transformation = (StepCartesianTransformationOperator) entity;
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
        } else if (entity instanceof StepGeometricReplica) {
            StepGeometricReplica replica = (StepGeometricReplica) entity;
            targets.addAll(collectSemanticTargets(replica.parent(), resolved, visiting));
            targets.addAll(collectSemanticTargets(replica.transformation(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(replica.id(), resolved, visiting));
        } else if (entity instanceof StepSweptAreaSolid) {
            StepSweptAreaSolid solid = (StepSweptAreaSolid) entity;
            targets.addAll(collectSemanticTargets(solid.sweptArea(), resolved, visiting));
            targets.addAll(collectSemanticTargets(solid.position(), resolved, visiting));
            targets.addAll(collectSemanticTargets(solid.sweepReference(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting));
        } else if (entity instanceof StepSweptDiskSolid) {
            StepSweptDiskSolid solid = (StepSweptDiskSolid) entity;
            targets.addAll(collectSemanticTargets(solid.sweptCurve(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting));
        } else if (entity instanceof StepComplexClippingResult) {
            StepComplexClippingResult solid = (StepComplexClippingResult) entity;
            targets.addAll(collectSemanticTargets(solid.firstOperand(), resolved, visiting));
            targets.addAll(collectSemanticTargets(solid.secondOperand(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting));
        } else if (entity instanceof StepSolidReplica) {
            StepSolidReplica solid = (StepSolidReplica) entity;
            targets.addAll(collectSemanticTargets(solid.parentSolid(), resolved, visiting));
            targets.addAll(collectSemanticTargets(solid.transformation(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting));
        } else if (entity instanceof StepManifoldSolidBrep) {
            StepManifoldSolidBrep solid = (StepManifoldSolidBrep) entity;
            targets.addAll(collectSemanticTargets(solid.outer(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting));
        } else if (entity instanceof StepBrepWithVoids) {
            StepBrepWithVoids solid = (StepBrepWithVoids) entity;
            targets.addAll(collectSemanticTargets(solid.outer(), resolved, visiting));
            targets.addAll(collectSemanticTargets(solid.voids(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting));
        } else if (entity instanceof StepBooleanClippingResult) {
            StepBooleanClippingResult result = (StepBooleanClippingResult) entity;
            targets.addAll(collectSemanticTargets(result.firstOperand(), resolved, visiting));
            targets.addAll(collectSemanticTargets(result.secondOperand(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(result.id(), resolved, visiting));
        } else if (entity instanceof StepBooleanResult) {
            StepBooleanResult result = (StepBooleanResult) entity;
            targets.addAll(collectSemanticTargets(result.firstOperand(), resolved, visiting));
            targets.addAll(collectSemanticTargets(result.secondOperand(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(result.id(), resolved, visiting));
        } else if (entity instanceof StepCsgSolid) {
            StepCsgSolid solid = (StepCsgSolid) entity;
            targets.addAll(collectSemanticTargets(solid.treeRootExpression(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting));
        } else if (entity instanceof StepCsgPrimitive) {
            StepCsgPrimitive primitive = (StepCsgPrimitive) entity;
            targets.addAll(collectSemanticTargets(primitive.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(primitive.id(), resolved, visiting));
        } else if (entity instanceof StepRepresentationContext) {
            StepRepresentationContext context = (StepRepresentationContext) entity;
            targets.addAll(collectTargetsForRepresentationContext(context.id(), resolved, visiting));
        } else if (entity instanceof StepGeometricRepresentationContext) {
            StepGeometricRepresentationContext context = (StepGeometricRepresentationContext) entity;
            if (context.globalUnitAssignedContext() != null) {
                targets.addAll(collectSemanticTargets(context.globalUnitAssignedContext(), resolved, visiting));
            }
            if (context.globalUncertaintyAssignedContext() != null) {
                targets.addAll(collectSemanticTargets(context.globalUncertaintyAssignedContext(), resolved, visiting));
            }
            targets.addAll(collectTargetsForRepresentationContext(context.id(), resolved, visiting));
        } else if (entity instanceof StepAbstractVariable) {
            StepAbstractVariable variable = (StepAbstractVariable) entity;
            targets.add(variable.usedRepresentation());
            targets.addAll(collectSemanticTargets(variable.definition(), resolved, visiting));
        } else if (entity instanceof StepScalarVariable) {
            StepScalarVariable variable = (StepScalarVariable) entity;
            targets.add(variable.usedRepresentation());
            targets.addAll(collectSemanticTargets(variable.definition(), resolved, visiting));
        } else if (entity instanceof StepRowVariable) {
            StepRowVariable variable = (StepRowVariable) entity;
            targets.add(variable.usedRepresentation());
            targets.addAll(collectSemanticTargets(variable.definition(), resolved, visiting));
        } else if (entity instanceof StepForwardChainingRulePremise) {
            StepForwardChainingRulePremise variable = (StepForwardChainingRulePremise) entity;
            targets.add(variable.usedRepresentation());
            targets.addAll(collectSemanticTargets(variable.definition(), resolved, visiting));
        } else if (entity instanceof StepBackChainingRuleBody) {
            StepBackChainingRuleBody variable = (StepBackChainingRuleBody) entity;
            targets.add(variable.usedRepresentation());
            targets.addAll(collectSemanticTargets(variable.definition(), resolved, visiting));
        } else if (entity instanceof StepApplicationContext) {
            StepApplicationContext applicationContext = (StepApplicationContext) entity;
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepApplicationProtocolDefinition
                        && ((StepApplicationProtocolDefinition) candidate).application().id() == applicationContext.id()) {
                    StepApplicationProtocolDefinition protocolDefinition = (StepApplicationProtocolDefinition) candidate;
                    targets.addAll(collectSemanticTargets(protocolDefinition, resolved, visiting));
                } else if (candidate instanceof StepProductContext
                        && ((StepProductContext) candidate).frameOfReference().id() == applicationContext.id()) {
                    StepProductContext productContext = (StepProductContext) candidate;
                    targets.addAll(collectSemanticTargets(productContext, resolved, visiting));
                } else if (candidate instanceof StepProductDefinitionContext
                        && ((StepProductDefinitionContext) candidate).frameOfReference().id() == applicationContext.id()) {
                    StepProductDefinitionContext productDefinitionContext = (StepProductDefinitionContext) candidate;
                    targets.addAll(collectSemanticTargets(productDefinitionContext, resolved, visiting));
                }
            }
        } else if (entity instanceof StepApplicationProtocolDefinition) {
            StepApplicationProtocolDefinition protocolDefinition = (StepApplicationProtocolDefinition) entity;
            targets.addAll(collectSemanticTargets(protocolDefinition.application(), resolved, visiting));
        } else if (entity instanceof StepProductContext) {
            StepProductContext productContext = (StepProductContext) entity;
            targets.addAll(collectSemanticTargets(productContext.frameOfReference(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepProduct
                        && ((StepProduct) candidate).frameOfReference().stream().anyMatch(context -> context.id() == productContext.id())) {
                    StepProduct product = (StepProduct) candidate;
                    targets.addAll(collectSemanticTargets(product, resolved, visiting));
                }
            }
        } else if (entity instanceof StepProductDefinitionContext) {
            StepProductDefinitionContext productDefinitionContext = (StepProductDefinitionContext) entity;
            targets.addAll(collectSemanticTargets(productDefinitionContext.frameOfReference(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepProductDefinition
                        && ((StepProductDefinition) candidate).frameOfReference().id() == productDefinitionContext.id()) {
                    StepProductDefinition productDefinition = (StepProductDefinition) candidate;
                    targets.addAll(collectSemanticTargets(productDefinition, resolved, visiting));
                }
            }
        } else if (entity instanceof StepGeneralProperty) {
            StepGeneralProperty generalProperty = (StepGeneralProperty) entity;
            targets.addAll(collectTargetsReferencingEntity(generalProperty.id(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepGeneralPropertyRelationship) {
                    StepGeneralPropertyRelationship relationship = (StepGeneralPropertyRelationship) candidate;
                    if (relationship.relatingGeneralProperty().id() == generalProperty.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedGeneralProperty(), resolved, visiting));
                    }
                    if (relationship.relatedGeneralProperty().id() == generalProperty.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingGeneralProperty(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepDocument) {
            StepDocument document = (StepDocument) entity;
            targets.addAll(collectTargetsReferencingEntity(document.id(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepDocumentReference
                        && ((StepDocumentReference) candidate).assignedDocument().id() == document.id()) {
                    StepDocumentReference reference = (StepDocumentReference) candidate;
                    targets.addAll(collectSemanticTargets(reference, resolved, visiting));
                } else if (candidate instanceof StepAppliedDocumentReference
                        && ((StepAppliedDocumentReference) candidate).assignedDocument().id() == document.id()) {
                    StepAppliedDocumentReference reference = (StepAppliedDocumentReference) candidate;
                    targets.addAll(collectSemanticTargets(reference, resolved, visiting));
                } else if (candidate instanceof StepDocumentRelationship) {
                    StepDocumentRelationship relationship = (StepDocumentRelationship) candidate;
                    if (relationship.relatingDocument().id() == document.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedDocument(), resolved, visiting));
                    }
                    if (relationship.relatedDocument().id() == document.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingDocument(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepDocumentUsageConstraint) {
            StepDocumentUsageConstraint documentUsageConstraint = (StepDocumentUsageConstraint) entity;
            targets.addAll(collectSemanticTargets(documentUsageConstraint.source(), resolved, visiting));
        } else if (entity instanceof StepGroup) {
            StepGroup group = (StepGroup) entity;
            targets.addAll(collectTargetsReferencingEntity(group.id(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepGroupAssignment
                    && ((StepGroupAssignment) candidate).assignedGroup().id() == group.id()) {
                StepGroupAssignment assignment = (StepGroupAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment.assignedGroup(), resolved, visiting));
            } else if (candidate instanceof StepAppliedGroupAssignment
                    && ((StepAppliedGroupAssignment) candidate).assignedGroup().id() == group.id()) {
                StepAppliedGroupAssignment assignment = (StepAppliedGroupAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            } else if (candidate instanceof StepClassificationAssignment
                    && ((StepClassificationAssignment) candidate).assignedClass().id() == group.id()) {
                StepClassificationAssignment assignment = (StepClassificationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment.assignedClass(), resolved, visiting));
            } else if (candidate instanceof StepAppliedClassificationAssignment
                    && ((StepAppliedClassificationAssignment) candidate).assignedClass().id() == group.id()) {
                StepAppliedClassificationAssignment assignment = (StepAppliedClassificationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepGroupRelationship) {
            StepGroupRelationship relationship = (StepGroupRelationship) candidate;
                    if (relationship.relatingGroup().id() == group.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedGroup(), resolved, visiting));
                    }
                    if (relationship.relatedGroup().id() == group.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingGroup(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepOrganization) {
            StepOrganization organization = (StepOrganization) entity;
            targets.addAll(collectTargetsReferencingEntity(organization.id(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepAppliedOrganizationAssignment
                        && ((StepAppliedOrganizationAssignment) candidate).assignedOrganization().id() == organization.id()) {
                    StepAppliedOrganizationAssignment assignment = (StepAppliedOrganizationAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepOrganizationAssignment
                        && ((StepOrganizationAssignment) candidate).assignedOrganization().id() == organization.id()) {
                    StepOrganizationAssignment assignment = (StepOrganizationAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.assignedOrganization(), resolved, visiting));
                } else if (candidate instanceof StepOrganizationRelationship) {
                    StepOrganizationRelationship relationship = (StepOrganizationRelationship) candidate;
                    if (relationship.relatingOrganization().id() == organization.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedOrganization(), resolved, visiting));
                    }
                    if (relationship.relatedOrganization().id() == organization.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingOrganization(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepProductCategory) {
            StepProductCategory productCategory = (StepProductCategory) entity;
            targets.addAll(collectTargetsReferencingEntity(productCategory.id(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepProductCategoryRelationship) {
                    StepProductCategoryRelationship relationship = (StepProductCategoryRelationship) candidate;
                    if (relationship.category().id() == productCategory.id()) {
                        targets.addAll(collectSemanticTargets(relationship.subCategory(), resolved, visiting));
                    }
                    if (relationship.subCategory().id() == productCategory.id()) {
                        targets.addAll(collectSemanticTargets(relationship.category(), resolved, visiting));
                    }
                } else if (candidate instanceof StepProductRelatedProductCategory
                        && ((StepProductRelatedProductCategory) candidate).id() == productCategory.id()) {
                    StepProductRelatedProductCategory relatedCategory = (StepProductRelatedProductCategory) candidate;
                    targets.addAll(collectSemanticTargets(relatedCategory.products(), resolved, visiting));
                }
            }
        } else if (entity instanceof StepProductRelatedProductCategory) {
            StepProductRelatedProductCategory relatedCategory = (StepProductRelatedProductCategory) entity;
            targets.addAll(collectSemanticTargets(relatedCategory.products(), resolved, visiting));
        } else if (entity instanceof StepProduct) {
            StepProduct product = (StepProduct) entity;
            targets.addAll(collectTargetsReferencingEntity(product.id(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepProductDefinitionFormation
                        && ((StepProductDefinitionFormation) candidate).ofProduct().id() == product.id()) {
                    StepProductDefinitionFormation formation = (StepProductDefinitionFormation) candidate;
                    targets.addAll(collectSemanticTargets(formation, resolved, visiting));
                } else if (candidate instanceof StepProductRelatedProductCategory
                        && ((StepProductRelatedProductCategory) candidate).products().stream().anyMatch(related -> related.id() == product.id())) {
                    StepProductRelatedProductCategory relatedCategory = (StepProductRelatedProductCategory) candidate;
                    targets.addAll(collectSemanticTargets(relatedCategory, resolved, visiting));
                } else if (candidate instanceof StepProductRelationship) {
                    StepProductRelationship relationship = (StepProductRelationship) candidate;
                    if (relationship.relatingProduct().id() == product.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedProduct(), resolved, visiting));
                    }
                    if (relationship.relatedProduct().id() == product.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingProduct(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepProductDefinitionFormation) {
            StepProductDefinitionFormation formation = (StepProductDefinitionFormation) entity;
            targets.addAll(collectTargetsReferencingEntity(formation.id(), resolved, visiting));
            targets.addAll(collectSemanticTargets(formation.ofProduct(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepProductDefinition
                        && ((StepProductDefinition) candidate).formation().id() == formation.id()) {
                    StepProductDefinition productDefinition = (StepProductDefinition) candidate;
                    targets.addAll(collectSemanticTargets(productDefinition, resolved, visiting));
                } else if (candidate instanceof StepProductDefinitionFormationRelationship) {
                    StepProductDefinitionFormationRelationship relationship = (StepProductDefinitionFormationRelationship) candidate;
                    if (relationship.relatingFormation().id() == formation.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedFormation(), resolved, visiting));
                    }
                    if (relationship.relatedFormation().id() == formation.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingFormation(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepProductDefinitionEffectivity) {
            StepProductDefinitionEffectivity effectivity = (StepProductDefinitionEffectivity) entity;
            targets.addAll(collectSemanticTargets(effectivity.productDefinition(), resolved, visiting));
        } else if (entity instanceof StepEffectivity) {
            StepEffectivity effectivity = (StepEffectivity) entity;
            targets.addAll(collectTargetsReferencingEntity(effectivity.id(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepEffectivityRelationship) {
            StepEffectivityRelationship relationship = (StepEffectivityRelationship) candidate;
                    if (relationship.relatingEffectivity().id() == effectivity.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedEffectivity(), resolved, visiting));
                    }
                    if (relationship.relatedEffectivity().id() == effectivity.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingEffectivity(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepCalendarDate) {
            StepCalendarDate calendarDate = (StepCalendarDate) entity;
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepDateAssignment
                        && ((StepDateAssignment) candidate).assignedDate().id() == calendarDate.id()) {
                    StepDateAssignment assignment = (StepDateAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
                } else if (candidate instanceof StepAppliedDateAssignment
                        && ((StepAppliedDateAssignment) candidate).assignedDate().id() == calendarDate.id()) {
                    StepAppliedDateAssignment assignment = (StepAppliedDateAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepDateAndTime
                        && ((StepDateAndTime) candidate).dateComponent().id() == calendarDate.id()) {
                    StepDateAndTime dateAndTime = (StepDateAndTime) candidate;
                    targets.addAll(collectSemanticTargets(dateAndTime, resolved, visiting));
                }
            }
        } else if (entity instanceof StepDateAndTime) {
            StepDateAndTime dateAndTime = (StepDateAndTime) entity;
            targets.addAll(collectSemanticTargets(dateAndTime.dateComponent(), resolved, visiting));
            targets.addAll(collectSemanticTargets(dateAndTime.timeComponent(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepDateTimeAssignment
                        && ((StepDateTimeAssignment) candidate).assignedDateAndTime().id() == dateAndTime.id()) {
                    StepDateTimeAssignment assignment = (StepDateTimeAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
                } else if (candidate instanceof StepAppliedDateTimeAssignment
                        && ((StepAppliedDateTimeAssignment) candidate).assignedDateAndTime().id() == dateAndTime.id()) {
                    StepAppliedDateTimeAssignment assignment = (StepAppliedDateTimeAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepApprovalDateTime
                        && ((StepApprovalDateTime) candidate).dateTime().id() == dateAndTime.id()) {
                    StepApprovalDateTime approvalDateTime = (StepApprovalDateTime) candidate;
                    targets.addAll(collectSemanticTargets(approvalDateTime, resolved, visiting));
                }
            }
        } else if (entity instanceof StepLocalTime) {
            StepLocalTime localTime = (StepLocalTime) entity;
            targets.addAll(collectSemanticTargets(localTime.zone(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepDateAndTime
                        && ((StepDateAndTime) candidate).timeComponent().id() == localTime.id()) {
                    StepDateAndTime dateAndTime = (StepDateAndTime) candidate;
                    targets.addAll(collectSemanticTargets(dateAndTime, resolved, visiting));
                }
            }
        } else if (entity instanceof StepCoordinatedUniversalTimeOffset) {
            StepCoordinatedUniversalTimeOffset zone = (StepCoordinatedUniversalTimeOffset) entity;
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepLocalTime
                        && ((StepLocalTime) candidate).zone().id() == zone.id()) {
                    StepLocalTime localTime = (StepLocalTime) candidate;
                    targets.addAll(collectSemanticTargets(localTime, resolved, visiting));
                }
            }
        } else if (entity instanceof StepDateAssignment) {
            StepDateAssignment assignment = (StepDateAssignment) entity;
            targets.addAll(collectTargetsForDateRole(assignment.role().id(), resolved, visiting));
        } else if (entity instanceof StepDateTimeAssignment) {
            StepDateTimeAssignment assignment = (StepDateTimeAssignment) entity;
            targets.addAll(collectTargetsForDateTimeRole(assignment.role().id(), resolved, visiting));
        } else if (entity instanceof StepPerson) {
            StepPerson person = (StepPerson) entity;
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepPersonAndOrganization
                        && ((StepPersonAndOrganization) candidate).person().id() == person.id()) {
                    StepPersonAndOrganization personAndOrganization = (StepPersonAndOrganization) candidate;
                    targets.addAll(collectSemanticTargets(personAndOrganization, resolved, visiting));
                }
            }
        } else if (entity instanceof StepApprovalStatus) {
            StepApprovalStatus status = (StepApprovalStatus) entity;
            targets.addAll(collectTargetsForApprovalStatus(status.id(), resolved, visiting));
        } else if (entity instanceof StepSecurityClassificationLevel) {
            StepSecurityClassificationLevel level = (StepSecurityClassificationLevel) entity;
            targets.addAll(collectTargetsForSecurityLevel(level.id(), resolved, visiting));
        } else if (entity instanceof StepContractType) {
            StepContractType kind = (StepContractType) entity;
            targets.addAll(collectTargetsForContractType(kind.id(), resolved, visiting));
        } else if (entity instanceof StepCertificationType) {
            StepCertificationType kind = (StepCertificationType) entity;
            targets.addAll(collectTargetsForCertificationType(kind.id(), resolved, visiting));
        } else if (entity instanceof StepApprovalRole) {
            StepApprovalRole role = (StepApprovalRole) entity;
            targets.addAll(collectTargetsForApprovalRole(role.id(), resolved, visiting));
        } else if (entity instanceof StepOrganizationRole) {
            StepOrganizationRole role = (StepOrganizationRole) entity;
            targets.addAll(collectTargetsForOrganizationRole(role.id(), resolved, visiting));
        } else if (entity instanceof StepPersonAndOrganizationRole) {
            StepPersonAndOrganizationRole role = (StepPersonAndOrganizationRole) entity;
            targets.addAll(collectTargetsForPersonAndOrganizationRole(role.id(), resolved, visiting));
        } else if (entity instanceof StepClassificationRole) {
            StepClassificationRole role = (StepClassificationRole) entity;
            targets.addAll(collectTargetsForClassificationRole(role.id(), resolved, visiting));
        } else if (entity instanceof StepDateRole) {
            StepDateRole role = (StepDateRole) entity;
            targets.addAll(collectTargetsForDateRole(role.id(), resolved, visiting));
        } else if (entity instanceof StepDateTimeRole) {
            StepDateTimeRole role = (StepDateTimeRole) entity;
            targets.addAll(collectTargetsForDateTimeRole(role.id(), resolved, visiting));
        } else if (entity instanceof StepIdentificationRole) {
            StepIdentificationRole role = (StepIdentificationRole) entity;
            targets.addAll(collectTargetsForIdentificationRole(role.id(), resolved, visiting));
        } else if (entity instanceof StepDocumentType) {
            StepDocumentType kind = (StepDocumentType) entity;
            targets.addAll(collectTargetsForDocumentType(kind.id(), resolved, visiting));
        } else if (entity instanceof StepApproval) {
            StepApproval approval = (StepApproval) entity;
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepAppliedApprovalAssignment
                        && ((StepAppliedApprovalAssignment) candidate).assignedApproval().id() == approval.id()) {
                    StepAppliedApprovalAssignment assignment = (StepAppliedApprovalAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepApprovalAssignment
                        && ((StepApprovalAssignment) candidate).assignedApproval().id() == approval.id()) {
                    StepApprovalAssignment assignment = (StepApprovalAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.assignedApproval(), resolved, visiting));
                } else if (candidate instanceof StepApprovalPersonOrganization
                        && ((StepApprovalPersonOrganization) candidate).authorizedApproval().id() == approval.id()) {
                    StepApprovalPersonOrganization personOrganization = (StepApprovalPersonOrganization) candidate;
                    targets.addAll(collectSemanticTargets(personOrganization.personOrganization(), resolved, visiting));
                } else if (candidate instanceof StepApprovalDateTime
                        && ((StepApprovalDateTime) candidate).datedApproval().id() == approval.id()) {
                    StepApprovalDateTime approvalDateTime = (StepApprovalDateTime) candidate;
                    targets.addAll(collectSemanticTargets(approvalDateTime.dateTime(), resolved, visiting));
                }
            }
        } else if (entity instanceof StepSecurityClassification) {
            StepSecurityClassification classification = (StepSecurityClassification) entity;
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepAppliedSecurityClassificationAssignment
                        && ((StepAppliedSecurityClassificationAssignment) candidate).assignedSecurityClassification().id() == classification.id()) {
                    StepAppliedSecurityClassificationAssignment assignment = (StepAppliedSecurityClassificationAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepSecurityClassificationAssignment
                        && ((StepSecurityClassificationAssignment) candidate).assignedSecurityClassification().id() == classification.id()) {
                    StepSecurityClassificationAssignment assignment = (StepSecurityClassificationAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.assignedSecurityClassification(), resolved, visiting));
                }
            }
        } else if (entity instanceof StepContract) {
            StepContract contract = (StepContract) entity;
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepAppliedContractAssignment
                        && ((StepAppliedContractAssignment) candidate).assignedContract().id() == contract.id()) {
                    StepAppliedContractAssignment assignment = (StepAppliedContractAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepContractAssignment
                        && ((StepContractAssignment) candidate).assignedContract().id() == contract.id()) {
                    StepContractAssignment assignment = (StepContractAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.assignedContract(), resolved, visiting));
                }
            }
        } else if (entity instanceof StepCertification) {
            StepCertification certification = (StepCertification) entity;
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepAppliedCertificationAssignment
                        && ((StepAppliedCertificationAssignment) candidate).assignedCertification().id() == certification.id()) {
                    StepAppliedCertificationAssignment assignment = (StepAppliedCertificationAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepCertificationAssignment
                        && ((StepCertificationAssignment) candidate).assignedCertification().id() == certification.id()) {
                    StepCertificationAssignment assignment = (StepCertificationAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.assignedCertification(), resolved, visiting));
                }
            }
        } else if (entity instanceof StepPersonAndOrganization) {
            StepPersonAndOrganization personAndOrganization = (StepPersonAndOrganization) entity;
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepAppliedPersonAndOrganizationAssignment
                        && ((StepAppliedPersonAndOrganizationAssignment) candidate).assignedPersonAndOrganization().id() == personAndOrganization.id()) {
                    StepAppliedPersonAndOrganizationAssignment assignment = (StepAppliedPersonAndOrganizationAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepPersonAndOrganizationAssignment
                        && ((StepPersonAndOrganizationAssignment) candidate).assignedPersonAndOrganization().id() == personAndOrganization.id()) {
                    StepPersonAndOrganizationAssignment assignment = (StepPersonAndOrganizationAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.assignedPersonAndOrganization(), resolved, visiting));
                }
            }
        } else if (entity instanceof StepLanguage) {
            StepLanguage language = (StepLanguage) entity;
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepAppliedLanguageAssignment
                        && ((StepAppliedLanguageAssignment) candidate).assignedLanguage().id() == language.id()) {
                    StepAppliedLanguageAssignment assignment = (StepAppliedLanguageAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepLanguageAssignment
                        && ((StepLanguageAssignment) candidate).assignedLanguage().id() == language.id()) {
                    StepLanguageAssignment assignment = (StepLanguageAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.assignedLanguage(), resolved, visiting));
                }
            }
        } else if (entity instanceof StepExternalIdentificationAssignment) {
            StepExternalIdentificationAssignment assignment = (StepExternalIdentificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.source(), resolved, visiting));
        } else if (entity instanceof StepExternalSource) {
            StepExternalSource source = (StepExternalSource) entity;
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepExternallyDefinedItem
                        && ((StepExternallyDefinedItem) candidate).source().id() == source.id()) {
                    StepExternallyDefinedItem item = (StepExternallyDefinedItem) candidate;
                    targets.addAll(collectSemanticTargets(item, resolved, visiting));
                } else if (candidate instanceof StepAppliedExternalIdentificationAssignment
                        && ((StepAppliedExternalIdentificationAssignment) candidate).source().id() == source.id()) {
                    StepAppliedExternalIdentificationAssignment assignment = (StepAppliedExternalIdentificationAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepExternalSourceRelationship) {
                    StepExternalSourceRelationship relationship = (StepExternalSourceRelationship) candidate;
                    if (relationship.relatingSource().id() == source.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedSource(), resolved, visiting));
                    }
                    if (relationship.relatedSource().id() == source.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingSource(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepExternallyDefinedItem) {
            StepExternallyDefinedItem item = (StepExternallyDefinedItem) entity;
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting));
            targets.addAll(collectSemanticTargets(item.source(), resolved, visiting));
        } else if (entity instanceof StepGeneralPropertyRelationship) {
            StepGeneralPropertyRelationship relationship = (StepGeneralPropertyRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingGeneralProperty(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedGeneralProperty(), resolved, visiting));
        } else if (entity instanceof StepApprovalAssignment) {
            StepApprovalAssignment assignment = (StepApprovalAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedApproval(), resolved, visiting));
        } else if (entity instanceof StepClassificationAssignment) {
            StepClassificationAssignment assignment = (StepClassificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedClass(), resolved, visiting));
        } else if (entity instanceof StepGroupAssignment) {
            StepGroupAssignment assignment = (StepGroupAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedGroup(), resolved, visiting));
        } else if (entity instanceof StepSecurityClassificationAssignment) {
            StepSecurityClassificationAssignment assignment = (StepSecurityClassificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedSecurityClassification(), resolved, visiting));
        } else if (entity instanceof StepContractAssignment) {
            StepContractAssignment assignment = (StepContractAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedContract(), resolved, visiting));
        } else if (entity instanceof StepCertificationAssignment) {
            StepCertificationAssignment assignment = (StepCertificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedCertification(), resolved, visiting));
        } else if (entity instanceof StepPersonAndOrganizationAssignment) {
            StepPersonAndOrganizationAssignment assignment = (StepPersonAndOrganizationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedPersonAndOrganization(), resolved, visiting));
        } else if (entity instanceof StepOrganizationAssignment) {
            StepOrganizationAssignment assignment = (StepOrganizationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedOrganization(), resolved, visiting));
        } else if (entity instanceof StepLanguageAssignment) {
            StepLanguageAssignment assignment = (StepLanguageAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedLanguage(), resolved, visiting));
        } else if (entity instanceof StepDocumentReference) {
            StepDocumentReference reference = (StepDocumentReference) entity;
            targets.addAll(collectSemanticTargets(reference.assignedDocument(), resolved, visiting));
        } else if (entity instanceof StepPresentationLayerAssignment) {
            StepPresentationLayerAssignment assignment = (StepPresentationLayerAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedItems(), resolved, visiting));
        } else if (entity instanceof StepApprovalPersonOrganization) {
            StepApprovalPersonOrganization approvalPersonOrganization = (StepApprovalPersonOrganization) entity;
            targets.addAll(collectSemanticTargets(approvalPersonOrganization.authorizedApproval(), resolved, visiting));
        } else if (entity instanceof StepApprovalDateTime) {
            StepApprovalDateTime approvalDateTime = (StepApprovalDateTime) entity;
            targets.addAll(collectSemanticTargets(approvalDateTime.datedApproval(), resolved, visiting));
        } else if (entity instanceof StepItemDefinedTransformation) {
            StepItemDefinedTransformation transformation = (StepItemDefinedTransformation) entity;
            targets.addAll(collectTargetsForItemDefinedTransformation(transformation.id(), resolved));
        } else if (entity instanceof StepExternalSourceRelationship) {
            StepExternalSourceRelationship relationship = (StepExternalSourceRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingSource(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedSource(), resolved, visiting));
        } else if (entity instanceof StepDocumentRelationship) {
            StepDocumentRelationship relationship = (StepDocumentRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingDocument(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedDocument(), resolved, visiting));
        } else if (entity instanceof StepGroupRelationship) {
            StepGroupRelationship relationship = (StepGroupRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingGroup(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedGroup(), resolved, visiting));
        } else if (entity instanceof StepOrganizationRelationship) {
            StepOrganizationRelationship relationship = (StepOrganizationRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingOrganization(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedOrganization(), resolved, visiting));
        } else if (entity instanceof StepProductCategoryRelationship) {
            StepProductCategoryRelationship relationship = (StepProductCategoryRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.category(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.subCategory(), resolved, visiting));
        } else if (entity instanceof StepProductRelationship) {
            StepProductRelationship relationship = (StepProductRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingProduct(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedProduct(), resolved, visiting));
        } else if (entity instanceof StepProductDefinitionFormationRelationship) {
            StepProductDefinitionFormationRelationship relationship = (StepProductDefinitionFormationRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingFormation(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedFormation(), resolved, visiting));
        } else if (entity instanceof StepEffectivityRelationship) {
            StepEffectivityRelationship relationship = (StepEffectivityRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingEffectivity(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedEffectivity(), resolved, visiting));
        } else if (entity instanceof StepRepresentationRelationship) {
            StepRepresentationRelationship relationship = (StepRepresentationRelationship) entity;
            targets.addAll(collectRepresentationTargetsFromRelationship(relationship));
        } else if (entity instanceof StepRepresentationRelationshipWithTransformation) {
            StepRepresentationRelationshipWithTransformation relationship = (StepRepresentationRelationshipWithTransformation) entity;
            targets.addAll(collectRepresentationTargetsFromRelationship(relationship));
        } else if (entity instanceof StepShapeRepresentationRelationship) {
            StepShapeRepresentationRelationship relationship = (StepShapeRepresentationRelationship) entity;
            targets.addAll(collectRepresentationTargetsFromRelationship(relationship));
        } else if (entity instanceof StepGeometricItemSpecificUsage) {
            StepGeometricItemSpecificUsage usage = (StepGeometricItemSpecificUsage) entity;
            targets.addAll(collectSemanticTargets(usage.usage(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
        } else if (entity instanceof StepChainBasedGeometricItemSpecificUsage) {
            StepChainBasedGeometricItemSpecificUsage usage = (StepChainBasedGeometricItemSpecificUsage) entity;
            targets.addAll(collectSemanticTargets(usage.usage(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.nodes(), resolved, visiting));
            for (StepRepresentationRelationship relationship : usage.undirectedLinks()) {
                targets.addAll(collectRepresentationTargetsFromRelationship(relationship));
            }
        } else if (entity instanceof StepItemIdentifiedRepresentationUsage) {
            StepItemIdentifiedRepresentationUsage usage = (StepItemIdentifiedRepresentationUsage) entity;
            targets.add(usage.usedRepresentation());
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
        } else if (entity instanceof StepChainBasedItemIdentifiedRepresentationUsage) {
            StepChainBasedItemIdentifiedRepresentationUsage usage = (StepChainBasedItemIdentifiedRepresentationUsage) entity;
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.nodes(), resolved, visiting));
            for (StepRepresentationRelationship relationship : usage.undirectedLinks()) {
                targets.addAll(collectRepresentationTargetsFromRelationship(relationship));
            }
        } else if (entity instanceof StepPlacedTarget) {
            StepPlacedTarget usage = (StepPlacedTarget) entity;
            targets.add(usage.usedRepresentation());
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
        } else if (entity instanceof StepDraughtingModelItemAssociation) {
            StepDraughtingModelItemAssociation usage = (StepDraughtingModelItemAssociation) entity;
            targets.add(usage.usedRepresentation());
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
        } else if (entity instanceof StepDraughtingModelItemAssociationWithPlaceholder) {
            StepDraughtingModelItemAssociationWithPlaceholder usage = (StepDraughtingModelItemAssociationWithPlaceholder) entity;
            targets.add(usage.usedRepresentation());
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.annotationPlaceholder(), resolved, visiting));
        } else if (entity instanceof StepPmiRequirementItemAssociation) {
            StepPmiRequirementItemAssociation usage = (StepPmiRequirementItemAssociation) entity;
            targets.add(usage.usedRepresentation());
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.requirement(), resolved, visiting));
        } else if (entity instanceof StepMechanicalDesignRequirementItemAssociation) {
            StepMechanicalDesignRequirementItemAssociation usage = (StepMechanicalDesignRequirementItemAssociation) entity;
            targets.add(usage.usedRepresentation());
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.requirement(), resolved, visiting));
        } else if (entity instanceof StepStyledItem) {
            StepStyledItem styledItem = (StepStyledItem) entity;
            targets.addAll(collectSemanticTargets(styledItem.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(styledItem.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(styledItem.id(), resolved, visiting));
        } else if (entity instanceof StepOverRidingStyledItem) {
            StepOverRidingStyledItem styledItem = (StepOverRidingStyledItem) entity;
            targets.addAll(collectSemanticTargets(styledItem.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(styledItem.item(), resolved, visiting));
            targets.addAll(collectSemanticTargets(styledItem.overRiddenStyle(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(styledItem.id(), resolved, visiting));
        } else if (entity instanceof StepMappedItem) {
            StepMappedItem mappedItem = (StepMappedItem) entity;
            targets.addAll(collectSemanticTargets(mappedItem.mappingSource(), resolved, visiting));
            targets.addAll(collectSemanticTargets(mappedItem.mappingTarget(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(mappedItem.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationCurveOccurrence) {
            StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationFillArea) {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) entity;
            targets.addAll(collectSemanticTargets(fillArea.boundaries(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(fillArea.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationFillAreaOccurrence) {
            StepAnnotationFillAreaOccurrence occurrence = (StepAnnotationFillAreaOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.fillStyleTarget(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationPlaceholderOccurrence) {
            StepAnnotationPlaceholderOccurrence occurrence = (StepAnnotationPlaceholderOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationPlane) {
            StepAnnotationPlane plane = (StepAnnotationPlane) entity;
            targets.addAll(collectSemanticTargets(plane.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(plane.item(), resolved, visiting));
            targets.addAll(collectSemanticTargets(plane.elements(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(plane.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationPointOccurrence) {
            StepAnnotationPointOccurrence occurrence = (StepAnnotationPointOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationSymbolOccurrence) {
            StepAnnotationSymbolOccurrence occurrence = (StepAnnotationSymbolOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationSubfigureOccurrence) {
            StepAnnotationSubfigureOccurrence occurrence = (StepAnnotationSubfigureOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationTextOccurrence) {
            StepAnnotationTextOccurrence occurrence = (StepAnnotationTextOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepDraughtingAnnotationOccurrence) {
            StepDraughtingAnnotationOccurrence occurrence = (StepDraughtingAnnotationOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepDimensionCurve) {
            StepDimensionCurve occurrence = (StepDimensionCurve) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepLeaderCurve) {
            StepLeaderCurve occurrence = (StepLeaderCurve) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepProjectionCurve) {
            StepProjectionCurve occurrence = (StepProjectionCurve) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepTerminatorSymbol) {
            StepTerminatorSymbol symbol = (StepTerminatorSymbol) entity;
            targets.addAll(collectSemanticTargets(symbol.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(symbol.item(), resolved, visiting));
            targets.addAll(collectSemanticTargets(symbol.annotatedCurve(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(symbol.id(), resolved, visiting));
        } else if (entity instanceof StepDraughtingCallout) {
            StepDraughtingCallout callout = (StepDraughtingCallout) entity;
            targets.addAll(collectSemanticTargets(callout.contents(), resolved, visiting));
        } else if (entity instanceof StepDraughtingCalloutRelationship) {
            StepDraughtingCalloutRelationship relationship = (StepDraughtingCalloutRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingCallout(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedCallout(), resolved, visiting));
        } else if (entity instanceof StepAnnotationOccurrenceRelationship) {
            StepAnnotationOccurrenceRelationship relationship = (StepAnnotationOccurrenceRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingAnnotationOccurrence(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedAnnotationOccurrence(), resolved, visiting));
        } else if (entity instanceof StepRepresentationMap) {
            StepRepresentationMap mapping = (StepRepresentationMap) entity;
            targets.add(mapping.mappedRepresentation());
            targets.addAll(collectSemanticTargets(mapping.mappedOrigin(), resolved, visiting));
        } else if (entity instanceof StepSymbolRepresentationMap) {
            StepSymbolRepresentationMap mapping = (StepSymbolRepresentationMap) entity;
            targets.add(mapping.mappedRepresentation());
            targets.addAll(collectSemanticTargets(mapping.mappedOrigin(), resolved, visiting));
        } else if (entity instanceof StepAnnotationSymbol) {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) entity;
            targets.addAll(collectSemanticTargets(annotationSymbol.mappingSource(), resolved, visiting));
            targets.addAll(collectSemanticTargets(annotationSymbol.mappingTarget(), resolved, visiting));
        } else if (entity instanceof StepAnnotationText) {
            StepAnnotationText annotationText = (StepAnnotationText) entity;
            targets.addAll(collectSemanticTargets(annotationText.mappingSource(), resolved, visiting));
            targets.addAll(collectSemanticTargets(annotationText.mappingTarget(), resolved, visiting));
        } else if (entity instanceof StepAnnotationTextCharacter) {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) entity;
            targets.addAll(collectSemanticTargets(annotationTextCharacter.mappingSource(), resolved, visiting));
            targets.addAll(collectSemanticTargets(annotationTextCharacter.mappingTarget(), resolved, visiting));
        } else if (entity instanceof StepUserDefinedCurveFont) {
            StepUserDefinedCurveFont curveFont = (StepUserDefinedCurveFont) entity;
            targets.addAll(collectSemanticTargets(curveFont.mappingSource(), resolved, visiting));
            targets.addAll(collectSemanticTargets(curveFont.mappingTarget(), resolved, visiting));
        } else if (entity instanceof StepUserDefinedMarker) {
            StepUserDefinedMarker marker = (StepUserDefinedMarker) entity;
            targets.addAll(collectSemanticTargets(marker.mappingSource(), resolved, visiting));
            targets.addAll(collectSemanticTargets(marker.mappingTarget(), resolved, visiting));
        } else if (entity instanceof StepUserDefinedTerminatorSymbol) {
            StepUserDefinedTerminatorSymbol symbol = (StepUserDefinedTerminatorSymbol) entity;
            targets.addAll(collectSemanticTargets(symbol.mappingSource(), resolved, visiting));
            targets.addAll(collectSemanticTargets(symbol.mappingTarget(), resolved, visiting));
        } else if (entity instanceof StepPresentationStyleAssignment) {
            StepPresentationStyleAssignment assignment = (StepPresentationStyleAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.styles(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(assignment.id(), resolved, visiting));
        } else if (entity instanceof StepFillAreaStyle) {
            StepFillAreaStyle fillAreaStyle = (StepFillAreaStyle) entity;
            targets.addAll(collectSemanticTargets(fillAreaStyle.styles(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(fillAreaStyle.id(), resolved, visiting));
        } else if (entity instanceof StepFillAreaStyleColour) {
            StepFillAreaStyleColour fillAreaStyleColour = (StepFillAreaStyleColour) entity;
            targets.addAll(collectSemanticTargets(fillAreaStyleColour.colour(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(fillAreaStyleColour.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleFillArea) {
            StepSurfaceStyleFillArea style = (StepSurfaceStyleFillArea) entity;
            targets.addAll(collectSemanticTargets(style.fillStyle(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepCharacterGlyphStyleStroke) {
            StepCharacterGlyphStyleStroke style = (StepCharacterGlyphStyleStroke) entity;
            targets.addAll(collectSemanticTargets(style.strokeStyle(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepCharacterGlyphStyleOutline) {
            StepCharacterGlyphStyleOutline style = (StepCharacterGlyphStyleOutline) entity;
            targets.addAll(collectSemanticTargets(style.outlineStyle(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepCharacterGlyphStyleOutlineWithCharacteristics) {
            StepCharacterGlyphStyleOutlineWithCharacteristics style = (StepCharacterGlyphStyleOutlineWithCharacteristics) entity;
            targets.addAll(collectSemanticTargets(style.outlineStyle(), resolved, visiting));
            targets.addAll(collectSemanticTargets(style.characteristics(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedCurveFont) {
            StepPreDefinedCurveFont curveFont = (StepPreDefinedCurveFont) entity;
            targets.addAll(collectTargetsForCurveFont(curveFont.id(), resolved, visiting));
        } else if (entity instanceof StepDraughtingPreDefinedCurveFont) {
            StepDraughtingPreDefinedCurveFont curveFont = (StepDraughtingPreDefinedCurveFont) entity;
            targets.addAll(collectTargetsForCurveFont(curveFont.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedMarker) {
            StepPreDefinedMarker marker = (StepPreDefinedMarker) entity;
            targets.addAll(collectTargetsForPointMarker(marker.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedPointMarkerSymbol) {
            StepPreDefinedPointMarkerSymbol marker = (StepPreDefinedPointMarkerSymbol) entity;
            targets.addAll(collectTargetsForPointMarker(marker.id(), resolved, visiting));
        } else if (entity instanceof StepColour) {
            StepColour colour = (StepColour) entity;
            targets.addAll(collectTargetsForStyleColour(colour.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(colour.id(), resolved, visiting));
        } else if (entity instanceof StepColourSpecification) {
            StepColourSpecification colour = (StepColourSpecification) entity;
            targets.addAll(collectTargetsForStyleColour(colour.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(colour.id(), resolved, visiting));
        } else if (entity instanceof StepColourRgb) {
            StepColourRgb colour = (StepColourRgb) entity;
            targets.addAll(collectTargetsForStyleColour(colour.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(colour.id(), resolved, visiting));
        } else if (entity instanceof StepConversionBasedUnit) {
            StepConversionBasedUnit unit = (StepConversionBasedUnit) entity;
            targets.addAll(collectSemanticTargets(unit.conversionFactor(), resolved, visiting));
            targets.addAll(collectTargetsForAssignedUnit(unit.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(unit.id(), resolved, visiting));
        } else if (entity instanceof StepConversionBasedUnitWithOffset) {
            StepConversionBasedUnitWithOffset unit = (StepConversionBasedUnitWithOffset) entity;
            targets.addAll(collectSemanticTargets(unit.conversionFactor(), resolved, visiting));
            targets.addAll(collectTargetsForAssignedUnit(unit.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(unit.id(), resolved, visiting));
        } else if (entity instanceof StepDerivedUnit) {
            StepDerivedUnit unit = (StepDerivedUnit) entity;
            targets.addAll(collectSemanticTargets(unit.elements(), resolved, visiting));
            targets.addAll(collectTargetsForAssignedUnit(unit.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(unit.id(), resolved, visiting));
        } else if (entity instanceof StepDerivedUnitElement) {
            StepDerivedUnitElement element = (StepDerivedUnitElement) entity;
            targets.addAll(collectSemanticTargets(element.unit(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(element.id(), resolved, visiting));
        } else if (entity instanceof StepNamedUnit) {
            StepNamedUnit unit = (StepNamedUnit) entity;
            targets.addAll(collectTargetsForAssignedUnit(unit.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(unit.id(), resolved, visiting));
        } else if (entity instanceof StepSiUnit) {
            StepSiUnit unit = (StepSiUnit) entity;
            targets.addAll(collectTargetsForAssignedUnit(unit.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(unit.id(), resolved, visiting));
        } else if (entity instanceof StepContextDependentUnit) {
            StepContextDependentUnit unit = (StepContextDependentUnit) entity;
            targets.addAll(collectTargetsForAssignedUnit(unit.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(unit.id(), resolved, visiting));
        } else if (entity instanceof StepGlobalUncertaintyAssignedContext) {
            StepGlobalUncertaintyAssignedContext context = (StepGlobalUncertaintyAssignedContext) entity;
            targets.addAll(collectSemanticTargets(context.uncertainties(), resolved, visiting));
            targets.addAll(collectTargetsForGlobalUncertaintyContext(context.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(context.id(), resolved, visiting));
        } else if (entity instanceof StepGlobalUnitAssignedContext) {
            StepGlobalUnitAssignedContext context = (StepGlobalUnitAssignedContext) entity;
            targets.addAll(collectSemanticTargets(context.units(), resolved, visiting));
            targets.addAll(collectTargetsForGlobalUnitContext(context.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(context.id(), resolved, visiting));
        } else if (entity instanceof StepRepresentationItem) {
            StepRepresentationItem item = (StepRepresentationItem) entity;
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting));
        } else if (entity instanceof StepGeometricRepresentationItem) {
            StepGeometricRepresentationItem item = (StepGeometricRepresentationItem) entity;
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting));
        } else if (entity instanceof StepTopologicalRepresentationItem) {
            StepTopologicalRepresentationItem item = (StepTopologicalRepresentationItem) entity;
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedColour) {
            StepPreDefinedColour colour = (StepPreDefinedColour) entity;
            targets.addAll(collectTargetsForStyleColour(colour.id(), resolved, visiting));
        } else if (entity instanceof StepDraughtingPreDefinedColour) {
            StepDraughtingPreDefinedColour colour = (StepDraughtingPreDefinedColour) entity;
            targets.addAll(collectTargetsForStyleColour(colour.id(), resolved, visiting));
        } else if (entity instanceof StepCurveStyle) {
            StepCurveStyle curveStyle = (StepCurveStyle) entity;
            targets.addAll(collectSemanticTargets(curveStyle.curveFont(), resolved, visiting));
            targets.addAll(collectSemanticTargets(curveStyle.colour(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curveStyle.id(), resolved, visiting));
        } else if (entity instanceof StepPointStyle) {
            StepPointStyle pointStyle = (StepPointStyle) entity;
            targets.addAll(collectSemanticTargets(pointStyle.marker(), resolved, visiting));
            targets.addAll(collectSemanticTargets(pointStyle.colour(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(pointStyle.id(), resolved, visiting));
        } else if (entity instanceof StepSymbolColour) {
            StepSymbolColour symbolColour = (StepSymbolColour) entity;
            targets.addAll(collectSemanticTargets(symbolColour.colour(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(symbolColour.id(), resolved, visiting));
        } else if (entity instanceof StepSymbolStyle) {
            StepSymbolStyle symbolStyle = (StepSymbolStyle) entity;
            targets.addAll(collectSemanticTargets(symbolStyle.styleOfSymbol(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(symbolStyle.id(), resolved, visiting));
        } else if (entity instanceof StepTextStyleForDefinedFont) {
            StepTextStyleForDefinedFont textStyle = (StepTextStyleForDefinedFont) entity;
            targets.addAll(collectSemanticTargets(textStyle.textColour(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(textStyle.id(), resolved, visiting));
        } else if (entity instanceof StepTextStyle) {
            StepTextStyle textStyle = (StepTextStyle) entity;
            targets.addAll(collectSemanticTargets(textStyle.characterAppearance(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(textStyle.id(), resolved, visiting));
        } else if (entity instanceof StepTextStyleWithSpacing) {
            StepTextStyleWithSpacing textStyle = (StepTextStyleWithSpacing) entity;
            targets.addAll(collectSemanticTargets(textStyle.characterAppearance(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(textStyle.id(), resolved, visiting));
        } else if (entity instanceof StepTextStyleWithJustification) {
            StepTextStyleWithJustification textStyle = (StepTextStyleWithJustification) entity;
            targets.addAll(collectSemanticTargets(textStyle.characterAppearance(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(textStyle.id(), resolved, visiting));
        } else if (entity instanceof StepTextStyleWithBoxCharacteristics) {
            StepTextStyleWithBoxCharacteristics textStyle = (StepTextStyleWithBoxCharacteristics) entity;
            targets.addAll(collectSemanticTargets(textStyle.characterAppearance(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(textStyle.id(), resolved, visiting));
        } else if (entity instanceof StepTextStyleWithMirror) {
            StepTextStyleWithMirror textStyle = (StepTextStyleWithMirror) entity;
            targets.addAll(collectSemanticTargets(textStyle.characterAppearance(), resolved, visiting));
            targets.addAll(collectSemanticTargets(textStyle.mirrorPlacement(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(textStyle.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleBoundary) {
            StepSurfaceStyleBoundary style = (StepSurfaceStyleBoundary) entity;
            targets.addAll(collectSemanticTargets(style.style(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleParameterLine) {
            StepSurfaceStyleParameterLine style = (StepSurfaceStyleParameterLine) entity;
            targets.addAll(collectSemanticTargets(style.style(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleSegmentationCurve) {
            StepSurfaceStyleSegmentationCurve style = (StepSurfaceStyleSegmentationCurve) entity;
            targets.addAll(collectSemanticTargets(style.style(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleSilhouette) {
            StepSurfaceStyleSilhouette style = (StepSurfaceStyleSilhouette) entity;
            targets.addAll(collectSemanticTargets(style.style(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleControlGrid) {
            StepSurfaceStyleControlGrid style = (StepSurfaceStyleControlGrid) entity;
            targets.addAll(collectSemanticTargets(style.style(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceSideStyle) {
            StepSurfaceSideStyle sideStyle = (StepSurfaceSideStyle) entity;
            targets.addAll(collectSemanticTargets(sideStyle.styles(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(sideStyle.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleUsage) {
            StepSurfaceStyleUsage usage = (StepSurfaceStyleUsage) entity;
            targets.addAll(collectSemanticTargets(usage.style(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(usage.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleTransparent) {
            StepSurfaceStyleTransparent style = (StepSurfaceStyleTransparent) entity;
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleReflectanceAmbient) {
            StepSurfaceStyleReflectanceAmbient style = (StepSurfaceStyleReflectanceAmbient) entity;
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleReflectanceAmbientDiffuse) {
            StepSurfaceStyleReflectanceAmbientDiffuse style = (StepSurfaceStyleReflectanceAmbientDiffuse) entity;
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleReflectanceAmbientDiffuseSpecular) {
            StepSurfaceStyleReflectanceAmbientDiffuseSpecular style = (StepSurfaceStyleReflectanceAmbientDiffuseSpecular) entity;
            targets.addAll(collectSemanticTargets(style.specularColour(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedSurfaceSideStyle) {
            StepPreDefinedSurfaceSideStyle style = (StepPreDefinedSurfaceSideStyle) entity;
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedTextFont) {
            StepPreDefinedTextFont textFont = (StepPreDefinedTextFont) entity;
            targets.addAll(collectTargetsReferencingEntity(textFont.id(), resolved, visiting));
        } else if (entity instanceof StepDraughtingPreDefinedTextFont) {
            StepDraughtingPreDefinedTextFont textFont = (StepDraughtingPreDefinedTextFont) entity;
            targets.addAll(collectTargetsReferencingEntity(textFont.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedTerminatorSymbol) {
            StepPreDefinedTerminatorSymbol symbol = (StepPreDefinedTerminatorSymbol) entity;
            targets.addAll(collectTargetsReferencingEntity(symbol.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedSymbol) {
            StepPreDefinedSymbol symbol = (StepPreDefinedSymbol) entity;
            targets.addAll(collectTargetsReferencingEntity(symbol.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedDimensionSymbol) {
            StepPreDefinedDimensionSymbol symbol = (StepPreDefinedDimensionSymbol) entity;
            targets.addAll(collectTargetsReferencingEntity(symbol.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedGeometricalToleranceSymbol) {
            StepPreDefinedGeometricalToleranceSymbol symbol = (StepPreDefinedGeometricalToleranceSymbol) entity;
            targets.addAll(collectTargetsReferencingEntity(symbol.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedItem) {
            StepPreDefinedItem item = (StepPreDefinedItem) entity;
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting));
        } else if (entity instanceof StepDescriptionAttribute) {
            StepDescriptionAttribute descriptionAttribute = (StepDescriptionAttribute) entity;
            targets.addAll(collectSemanticTargets(descriptionAttribute.describedItem(), resolved, visiting));
        } else if (entity instanceof StepNameAttribute) {
            StepNameAttribute nameAttribute = (StepNameAttribute) entity;
            targets.addAll(collectSemanticTargets(nameAttribute.namedItem(), resolved, visiting));
        } else if (entity instanceof StepIdAttribute) {
            StepIdAttribute idAttribute = (StepIdAttribute) entity;
            targets.addAll(collectSemanticTargets(idAttribute.identifiedItem(), resolved, visiting));
        } else if (entity instanceof StepAppliedNameAssignment) {
            StepAppliedNameAssignment assignment = (StepAppliedNameAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedIdentificationAssignment) {
            StepAppliedIdentificationAssignment assignment = (StepAppliedIdentificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedExternalIdentificationAssignment) {
            StepAppliedExternalIdentificationAssignment assignment = (StepAppliedExternalIdentificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedGroupAssignment) {
            StepAppliedGroupAssignment assignment = (StepAppliedGroupAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedClassificationAssignment) {
            StepAppliedClassificationAssignment assignment = (StepAppliedClassificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedDateAssignment) {
            StepAppliedDateAssignment assignment = (StepAppliedDateAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedDateTimeAssignment) {
            StepAppliedDateTimeAssignment assignment = (StepAppliedDateTimeAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedApprovalAssignment) {
            StepAppliedApprovalAssignment assignment = (StepAppliedApprovalAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedSecurityClassificationAssignment) {
            StepAppliedSecurityClassificationAssignment assignment = (StepAppliedSecurityClassificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedDocumentReference) {
            StepAppliedDocumentReference assignment = (StepAppliedDocumentReference) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedContractAssignment) {
            StepAppliedContractAssignment assignment = (StepAppliedContractAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedCertificationAssignment) {
            StepAppliedCertificationAssignment assignment = (StepAppliedCertificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedPersonAndOrganizationAssignment) {
            StepAppliedPersonAndOrganizationAssignment assignment = (StepAppliedPersonAndOrganizationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedOrganizationAssignment) {
            StepAppliedOrganizationAssignment assignment = (StepAppliedOrganizationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedLanguageAssignment) {
            StepAppliedLanguageAssignment assignment = (StepAppliedLanguageAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAttributeAssertion) {
            StepAttributeAssertion attributeAssertion = (StepAttributeAssertion) entity;
            targets.add(attributeAssertion.usedRepresentation());
            targets.addAll(collectSemanticTargets(attributeAssertion.definition(), resolved, visiting));
        } else if (entity instanceof StepIdentificationAssignment
                || entity instanceof StepNameAssignment) {
            // Pure assignment metadata without item references contributes no target by itself.
        } else if (entity instanceof StepShapeDefinitionRepresentation) {
            StepShapeDefinitionRepresentation shapeDefinitionRepresentation = (StepShapeDefinitionRepresentation) entity;
            targets.add(shapeDefinitionRepresentation.usedRepresentation());
            targets.addAll(collectSemanticTargets(shapeDefinitionRepresentation.definition(), resolved, visiting));
        } else if (entity instanceof StepContextDependentShapeRepresentation) {
            StepContextDependentShapeRepresentation contextDependent = (StepContextDependentShapeRepresentation) entity;
            targets.addAll(collectRepresentationTargetsFromRelationship(contextDependent.representationRelationship()));
            targets.addAll(collectSemanticTargets(contextDependent.representedProductRelation(), resolved, visiting));
        } else if (entity instanceof StepProductDefinitionShape) {
            StepProductDefinitionShape productDefinitionShape = (StepProductDefinitionShape) entity;
            targets.addAll(collectSemanticTargets(productDefinitionShape.definition(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepShapeDefinitionRepresentation
                        && ((StepShapeDefinitionRepresentation) candidate).definition().id() == productDefinitionShape.id()) {
                    StepShapeDefinitionRepresentation link = (StepShapeDefinitionRepresentation) candidate;
                    targets.add(link.usedRepresentation());
                } else if (candidate instanceof StepContextDependentShapeRepresentation
                        && ((StepContextDependentShapeRepresentation) candidate).representedProductRelation().id() == productDefinitionShape.id()) {
                    StepContextDependentShapeRepresentation contextDependent = (StepContextDependentShapeRepresentation) candidate;
                    targets.addAll(collectSemanticTargets(contextDependent, resolved, visiting));
                } else if (candidate instanceof StepShapeAspect
                        && ((StepShapeAspect) candidate).ofShape().id() == productDefinitionShape.id()) {
                    StepShapeAspect shapeAspect = (StepShapeAspect) candidate;
                    targets.addAll(collectSemanticTargets(shapeAspect, resolved, visiting));
                } else if (candidate instanceof StepShapeAspectOccurrence
                        && ((StepShapeAspectOccurrence) candidate).ofShape().id() == productDefinitionShape.id()) {
                    StepShapeAspectOccurrence occurrence = (StepShapeAspectOccurrence) candidate;
                    targets.addAll(collectSemanticTargets(occurrence, resolved, visiting));
                }
            }
        } else if (entity instanceof StepProductDefinition) {
            StepProductDefinition productDefinition = (StepProductDefinition) entity;
            targets.addAll(collectTargetsForProductDefinition(productDefinition.id(), resolved, visiting));
        } else if (entity instanceof StepNextAssemblyUsageOccurrence) {
            StepNextAssemblyUsageOccurrence occurrence = (StepNextAssemblyUsageOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.relatedProductDefinition(), resolved, visiting));
            targets.addAll(collectTargetsForOccurrence(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepProductDefinitionRelationship) {
            StepProductDefinitionRelationship relationship = (StepProductDefinitionRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingProductDefinition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedProductDefinition(), resolved, visiting));
        } else if (entity instanceof StepProductDefinitionRelationshipRelationship) {
            StepProductDefinitionRelationshipRelationship relationship = (StepProductDefinitionRelationshipRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relating(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.related(), resolved, visiting));
        } else if (entity instanceof StepPropertyDefinitionRelationship) {
            StepPropertyDefinitionRelationship relationship = (StepPropertyDefinitionRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingPropertyDefinition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedPropertyDefinition(), resolved, visiting));
        } else if (entity instanceof StepShapeAspectOccurrence) {
            StepShapeAspectOccurrence occurrence = (StepShapeAspectOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.definition(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepShapeAspect) {
            StepShapeAspect shapeAspect = (StepShapeAspect) entity;
            targets.addAll(collectTargetsReferencingEntity(shapeAspect.id(), resolved, visiting));
        } else if (entity instanceof StepShapeAspectRelationship) {
            StepShapeAspectRelationship relationship = (StepShapeAspectRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingShapeAspect(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedShapeAspect(), resolved, visiting));
        }
        visiting.remove(entity.id());
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectRepresentationTargetsFromRelationship(StepEntity relationship) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        if (relationship instanceof StepRepresentationRelationship) {
            StepRepresentationRelationship representationRelationship = (StepRepresentationRelationship) relationship;
            targets.add(representationRelationship.rep1());
            targets.add(representationRelationship.rep2());
        } else if (relationship instanceof StepRepresentationRelationshipWithTransformation) {
            StepRepresentationRelationshipWithTransformation representationRelationship = (StepRepresentationRelationshipWithTransformation) relationship;
            targets.add(representationRelationship.rep1());
            targets.add(representationRelationship.rep2());
        } else if (relationship instanceof StepShapeRepresentationRelationship) {
            StepShapeRepresentationRelationship representationRelationship = (StepShapeRepresentationRelationship) relationship;
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
            if (candidate instanceof StepPropertyDefinition
                    && ((StepPropertyDefinition) candidate).definition().id() == referencedId) {
                StepPropertyDefinition propertyDefinition = (StepPropertyDefinition) candidate;
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
        return PreviewMaterialExporter.collectTargetsForCurveFont(curveFontId, resolved, visiting, StepPreviewJsonExporter::collectSemanticTargets);
    }

    private static Set<StepEntity> collectTargetsForRepresentationContext(
            int contextId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepRepresentation
                    && ((StepRepresentation) candidate).context() != null
                    && ((StepRepresentation) candidate).context().id() == contextId) {
                StepRepresentation representation = (StepRepresentation) candidate;
                targets.add(representation);
            } else if (candidate instanceof StepPropertyDefinition
                    && ((StepPropertyDefinition) candidate).definition().id() == contextId) {
                StepPropertyDefinition propertyDefinition = (StepPropertyDefinition) candidate;
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
            if (candidate instanceof StepGlobalUnitAssignedContext
                    && ((StepGlobalUnitAssignedContext) candidate).units().stream().anyMatch(unit -> unit.id() == unitId)) {
                StepGlobalUnitAssignedContext context = (StepGlobalUnitAssignedContext) candidate;
                targets.addAll(collectSemanticTargets(context, resolved, visiting));
            } else if (candidate instanceof StepGeometricRepresentationContext
                    && ((StepGeometricRepresentationContext) candidate).globalUnitAssignedContext() != null
                    && ((StepGeometricRepresentationContext) candidate).globalUnitAssignedContext().units().stream().anyMatch(unit -> unit.id() == unitId)) {
                StepGeometricRepresentationContext context = (StepGeometricRepresentationContext) candidate;
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
            if (candidate instanceof StepGlobalUncertaintyAssignedContext
                    && ((StepGlobalUncertaintyAssignedContext) candidate).uncertainties().stream().anyMatch(uncertainty -> uncertainty.id() == uncertaintyId)) {
                StepGlobalUncertaintyAssignedContext context = (StepGlobalUncertaintyAssignedContext) candidate;
                targets.addAll(collectSemanticTargets(context, resolved, visiting));
            } else if (candidate instanceof StepGeometricRepresentationContext
                    && ((StepGeometricRepresentationContext) candidate).globalUncertaintyAssignedContext() != null
                    && ((StepGeometricRepresentationContext) candidate).globalUncertaintyAssignedContext().uncertainties().stream()
                            .anyMatch(uncertainty -> uncertainty.id() == uncertaintyId)) {
                StepGeometricRepresentationContext context = (StepGeometricRepresentationContext) candidate;
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
            if (candidate instanceof StepGeometricRepresentationContext
                    && ((StepGeometricRepresentationContext) candidate).globalUnitAssignedContext() != null
                    && ((StepGeometricRepresentationContext) candidate).globalUnitAssignedContext().id() == contextId) {
                StepGeometricRepresentationContext context = (StepGeometricRepresentationContext) candidate;
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
            if (candidate instanceof StepGeometricRepresentationContext
                    && ((StepGeometricRepresentationContext) candidate).globalUncertaintyAssignedContext() != null
                    && ((StepGeometricRepresentationContext) candidate).globalUncertaintyAssignedContext().id() == contextId) {
                StepGeometricRepresentationContext context = (StepGeometricRepresentationContext) candidate;
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
            if (candidate instanceof StepRepresentationRelationshipWithTransformation
                    && ((StepRepresentationRelationshipWithTransformation) candidate).transformationOperator().id() == transformationId) {
                StepRepresentationRelationshipWithTransformation relationship = (StepRepresentationRelationshipWithTransformation) candidate;
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
        return PreviewMaterialExporter.collectTargetsForPointMarker(markerId, resolved, visiting, StepPreviewJsonExporter::collectSemanticTargets);
    }

    private static Set<StepEntity> collectTargetsForStyleColour(
            int colourId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        return PreviewMaterialExporter.collectTargetsForStyleColour(colourId, resolved, visiting, StepPreviewJsonExporter::collectSemanticTargets);
    }

    private static Set<StepEntity> collectTargetsForProductDefinition(
            int productDefinitionId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProductDefinitionShape
                    && ((StepProductDefinitionShape) candidate).definition().id() == productDefinitionId) {
                StepProductDefinitionShape shape = (StepProductDefinitionShape) candidate;
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
            if (candidate instanceof StepProductDefinitionShape
                    && ((StepProductDefinitionShape) candidate).definition().id() == occurrenceId) {
                StepProductDefinitionShape shape = (StepProductDefinitionShape) candidate;
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
            if (candidate instanceof StepAppliedDateAssignment
                    && ((StepAppliedDateAssignment) candidate).role().id() == roleId) {
                StepAppliedDateAssignment assignment = (StepAppliedDateAssignment) candidate;
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
            if (candidate instanceof StepApproval
                    && ((StepApproval) candidate).status().id() == statusId) {
                StepApproval approval = (StepApproval) candidate;
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
            if (candidate instanceof StepSecurityClassification
                    && ((StepSecurityClassification) candidate).securityLevel().id() == levelId) {
                StepSecurityClassification classification = (StepSecurityClassification) candidate;
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
            if (candidate instanceof StepContract
                    && ((StepContract) candidate).kind().id() == kindId) {
                StepContract contract = (StepContract) candidate;
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
            if (candidate instanceof StepCertification
                    && ((StepCertification) candidate).kind().id() == kindId) {
                StepCertification certification = (StepCertification) candidate;
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
            if (candidate instanceof StepApprovalPersonOrganization
                    && ((StepApprovalPersonOrganization) candidate).role().id() == roleId) {
                StepApprovalPersonOrganization assignment = (StepApprovalPersonOrganization) candidate;
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
            if (candidate instanceof StepOrganizationAssignment
                    && ((StepOrganizationAssignment) candidate).role().id() == roleId) {
                StepOrganizationAssignment assignment = (StepOrganizationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
            } else if (candidate instanceof StepAppliedOrganizationAssignment
                    && ((StepAppliedOrganizationAssignment) candidate).role().id() == roleId) {
                StepAppliedOrganizationAssignment assignment = (StepAppliedOrganizationAssignment) candidate;
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
            if (candidate instanceof StepPersonAndOrganizationAssignment
                    && ((StepPersonAndOrganizationAssignment) candidate).role().id() == roleId) {
                StepPersonAndOrganizationAssignment assignment = (StepPersonAndOrganizationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
            } else if (candidate instanceof StepAppliedPersonAndOrganizationAssignment
                    && ((StepAppliedPersonAndOrganizationAssignment) candidate).role().id() == roleId) {
                StepAppliedPersonAndOrganizationAssignment assignment = (StepAppliedPersonAndOrganizationAssignment) candidate;
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
            if (candidate instanceof StepClassificationAssignment
                    && ((StepClassificationAssignment) candidate).role().id() == roleId) {
                StepClassificationAssignment assignment = (StepClassificationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
            } else if (candidate instanceof StepAppliedClassificationAssignment
                    && ((StepAppliedClassificationAssignment) candidate).role().id() == roleId) {
                StepAppliedClassificationAssignment assignment = (StepAppliedClassificationAssignment) candidate;
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
            if (candidate instanceof StepIdentificationAssignment
                    && ((StepIdentificationAssignment) candidate).role().id() == roleId) {
                StepIdentificationAssignment assignment = (StepIdentificationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
            } else if (candidate instanceof StepAppliedIdentificationAssignment
                    && ((StepAppliedIdentificationAssignment) candidate).role().id() == roleId) {
                StepAppliedIdentificationAssignment assignment = (StepAppliedIdentificationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            } else if (candidate instanceof StepExternalIdentificationAssignment
                    && ((StepExternalIdentificationAssignment) candidate).role().id() == roleId) {
                StepExternalIdentificationAssignment assignment = (StepExternalIdentificationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
            } else if (candidate instanceof StepAppliedExternalIdentificationAssignment
                    && ((StepAppliedExternalIdentificationAssignment) candidate).role().id() == roleId) {
                StepAppliedExternalIdentificationAssignment assignment = (StepAppliedExternalIdentificationAssignment) candidate;
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
            if (candidate instanceof StepDocument
                    && ((StepDocument) candidate).kind().id() == kindId) {
                StepDocument document = (StepDocument) candidate;
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
            if (candidate instanceof StepAppliedDateTimeAssignment
                    && ((StepAppliedDateTimeAssignment) candidate).role().id() == roleId) {
                StepAppliedDateTimeAssignment assignment = (StepAppliedDateTimeAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    public static CartesianPoint pointFromStep(StepCartesianPoint point) {
        double x = point.coordinates().get(0);
        double y = point.coordinates().size() > 1 ? point.coordinates().get(1) : 0.0;
        double z = point.coordinates().size() > 2 ? point.coordinates().get(2) : 0.0;
        return new CartesianPoint(x, y, z);
    }


    
    
}
