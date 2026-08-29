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
        AssemblyData assembly = StepRepresentationPayloadBuilder.buildAssemblyData(resolved, builder, metadata, units);
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
            legacyGeometry = StepLegacyGeometryBuilder.buildLegacyGeometry(resolved, builder, metadata);
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
        List<PmiPayload> pmi = StepPmiPayloadBuilder.buildPmiPayloads(resolved, assembly, builder);
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


    // Delegate to StepValidationHelper - extracted utility class




    /**
     * Generic parametric surface preview for surfaces with sampleGrid:
     * paraboloid, hyperboloid, surface of translation, surface of projection.
     */


    // Delegate to StepSummaryBuilder - extracted utility class


















    // Delegate to StepGeometryHelper - extracted utility class

    // Delegate to StepPayloadBuilder - extracted utility class







    // Delegate to StepBoundsAccumulator - extracted utility class
    private static void includeGeometry(BoundsAccumulator bounds, GeometryCollection geometry) {
        StepBoundsAccumulator.includeGeometry(bounds, geometry);
    }

    // Delegate to StepBoundsAccumulator - extracted utility class

    // Delegate to StepBoundsAccumulator - extracted utility class
    private static void includeBounds(BoundsAccumulator target, BoundsPayload bounds) {
        StepBoundsAccumulator.includeBounds(target, bounds);
    }

    // Delegate to StepBoundsAccumulator - extracted utility class
    private static BoundsAccumulator copyBounds(BoundsAccumulator source) {
        return StepBoundsAccumulator.copyBounds(source);
    }

    private static ValidationPayload buildValidationPayload(
            GeometryCollection legacyGeometry,
            AssemblyData assembly,
            BoundsAccumulator bounds,
            Map<Integer, StepEntity> resolved
    ) {
        GeometrySummary summary = assembly.instances().isEmpty()
                ? StepRepresentationPayloadBuilder.summarizeGeometry(legacyGeometry)
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













}
