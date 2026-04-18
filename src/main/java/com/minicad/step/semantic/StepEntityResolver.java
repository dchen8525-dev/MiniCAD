package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedStepEntityException;
import com.minicad.step.model.StepAdvancedFace;
import com.minicad.step.model.StepAnnotationCurveOccurrence;
import com.minicad.step.model.StepAnnotationFillArea;
import com.minicad.step.model.StepAnnotationFillAreaOccurrence;
import com.minicad.step.model.StepAnnotationPlane;
import com.minicad.step.model.StepAnnotationPlaceholderOccurrence;
import com.minicad.step.model.StepAnnotationPointOccurrence;
import com.minicad.step.model.StepAnnotationOccurrenceRelationship;
import com.minicad.step.model.StepAnnotationSubfigureOccurrence;
import com.minicad.step.model.StepAnnotationSymbol;
import com.minicad.step.model.StepAnnotationSymbolOccurrence;
import com.minicad.step.model.StepAnnotationText;
import com.minicad.step.model.StepAnnotationTextCharacter;
import com.minicad.step.model.StepAnnotationTextOccurrence;
import com.minicad.step.model.StepAbstractVariable;
import com.minicad.step.model.StepActionPropertyRepresentation;
import com.minicad.step.model.StepAddress;
import com.minicad.step.model.StepApplicationContext;
import com.minicad.step.model.StepApplicationProtocolDefinition;
import com.minicad.step.model.StepAppliedApprovalAssignment;
import com.minicad.step.model.StepAppliedCertificationAssignment;
import com.minicad.step.model.StepAppliedContractAssignment;
import com.minicad.step.model.StepAppliedDateTimeAssignment;
import com.minicad.step.model.StepAppliedDocumentReference;
import com.minicad.step.model.StepAppliedClassificationAssignment;
import com.minicad.step.model.StepAppliedExternalIdentificationAssignment;
import com.minicad.step.model.StepAppliedIdentificationAssignment;
import com.minicad.step.model.StepAppliedLanguageAssignment;
import com.minicad.step.model.StepAppliedNameAssignment;
import com.minicad.step.model.StepAppliedOrganizationAssignment;
import com.minicad.step.model.StepAppliedGroupAssignment;
import com.minicad.step.model.StepAppliedPersonAndOrganizationAssignment;
import com.minicad.step.model.StepAppliedSecurityClassificationAssignment;
import com.minicad.step.model.StepApproval;
import com.minicad.step.model.StepApprovalAssignment;
import com.minicad.step.model.StepApprovalDateTime;
import com.minicad.step.model.StepApprovalPersonOrganization;
import com.minicad.step.model.StepApprovalRole;
import com.minicad.step.model.StepApprovalStatus;
import com.minicad.step.model.StepAttributeAssertion;
import com.minicad.step.model.StepAxis1Placement;
import com.minicad.step.model.StepAxis2Placement2D;
import com.minicad.step.model.StepAxis2Placement3D;
import com.minicad.step.model.StepBackChainingRuleBody;
import com.minicad.step.model.StepBoundedCurve;
import com.minicad.step.model.StepBoundedSurface;
import com.minicad.step.model.StepBezierCurve;
import com.minicad.step.model.StepBezierSurface;
import com.minicad.step.model.StepBlendedSurface;
import com.minicad.step.model.StepBooleanClippingResult;
import com.minicad.step.model.StepBooleanResult;
import com.minicad.step.model.StepBrepWithVoids;
import com.minicad.step.model.StepBSplineCurve;
import com.minicad.step.model.StepBSplineCurveWithKnotsAndBreakpoints;
import com.minicad.step.model.StepBSplineCurveWithKnots;
import com.minicad.step.model.StepBoxDomain;
import com.minicad.step.model.StepCartesianTransformationOperator;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepCertification;
import com.minicad.step.model.StepCertificationAssignment;
import com.minicad.step.model.StepCertificationType;
import com.minicad.step.model.StepChainBasedGeometricItemSpecificUsage;
import com.minicad.step.model.StepChainBasedItemIdentifiedRepresentationUsage;
import com.minicad.step.model.StepCharacterizedObject;
import com.minicad.step.model.StepCharacterGlyphStyleOutline;
import com.minicad.step.model.StepCharacterGlyphStyleOutlineWithCharacteristics;
import com.minicad.step.model.StepCharacterGlyphStyleStroke;
import com.minicad.step.model.StepClassificationAssignment;
import com.minicad.step.model.StepClassificationRole;
import com.minicad.step.model.StepBSplineCurveWithKnots;
import com.minicad.step.model.StepBSplineSurface;
import com.minicad.step.model.StepBSplineSurfaceWithKnotsAndBreakpoints;
import com.minicad.step.model.StepBSplineSurfaceWithKnots;
import com.minicad.step.model.StepRationalBSplineCurve;
import com.minicad.step.model.StepRationalBSplineSurface;
import com.minicad.step.model.StepRectangularCompositeSurface;
import com.minicad.step.model.StepCalendarDate;
import com.minicad.step.model.StepCircle;
import com.minicad.step.model.StepCentreLineArcProfileDef;
import com.minicad.step.model.StepCenteredCircleProfileDef;
import com.minicad.step.model.StepChamferEdge;
import com.minicad.step.model.StepClosedShell;
import com.minicad.step.model.StepChamferDefinition;
import com.minicad.step.model.StepFilletDefinition;
import com.minicad.step.model.StepFilletEdge;
import com.minicad.step.model.StepMachiningOperation;
import com.minicad.step.model.StepMachiningOperationSequence;
import com.minicad.step.model.StepRound;
import com.minicad.step.model.StepGroove;
import com.minicad.step.model.StepHole;
import com.minicad.step.model.StepSlot;
import com.minicad.step.model.StepStud;
import com.minicad.step.model.StepProtrusion;
import com.minicad.step.model.StepCutout;
import com.minicad.step.model.StepDepression;
import com.minicad.step.model.StepMarking;
import com.minicad.step.model.StepCircularPattern;
import com.minicad.step.model.StepLinearPattern;
import com.minicad.step.model.StepPattern;
import com.minicad.step.model.StepFeaturePattern;
import com.minicad.step.model.StepCompositeDatumReference;
import com.minicad.step.model.StepAssemblyProcessPlan;
import com.minicad.step.model.StepMachiningProcessPlan;
import com.minicad.step.model.StepMachiningWorkPlan;
import com.minicad.step.model.StepRectangularToleranceZone;
import com.minicad.step.model.StepToleranceModifier;
import com.minicad.step.model.StepComposedText;
import com.minicad.step.model.StepComplexClippingResult;
import com.minicad.step.model.StepCompositeText;
import com.minicad.step.model.StepClothoid;
import com.minicad.step.model.StepColour;
import com.minicad.step.model.StepColourSpecification;
import com.minicad.step.model.StepColourRgb;
import com.minicad.step.model.StepColorSpecification;
import com.minicad.step.model.StepConfigurationEffectivity;
import com.minicad.step.model.StepConfigurationItem;
import com.minicad.step.model.StepDatum;
import com.minicad.step.model.StepDatumFeature;
import com.minicad.step.model.StepDatumReference;
import com.minicad.step.model.StepDatumReferenceCompartment;
import com.minicad.step.model.StepDatumReferenceElement;
import com.minicad.step.model.StepDatumReferenceModifier;
import com.minicad.step.model.StepDatumReferenceModifierWithValue;
import com.minicad.step.model.StepGeometricToleranceWithDefinedAreaUnit;
import com.minicad.step.model.StepGeometricToleranceWithMaximumTolerance;
import com.minicad.step.model.StepNonUniformZoneDefinition;
import com.minicad.step.model.StepRunoutZoneDefinitionOrientation;
import com.minicad.step.model.StepDatumSystemReference;
import com.minicad.step.model.StepTolerancePair;
import com.minicad.step.model.StepToleranceSet;
import com.minicad.step.model.StepDatumSystem;
import com.minicad.step.model.StepDatumTarget;
import com.minicad.step.model.StepDimensionalLocation;
import com.minicad.step.model.StepDimensionalSize;
import com.minicad.step.model.StepDirectedDimensionalSize;
import com.minicad.step.model.StepFeatureControlFrame;
import com.minicad.step.model.StepGeometricTolerance;
import com.minicad.step.model.StepGeometricMeasurement;
import com.minicad.step.model.StepDimensionalMeasurement;
import com.minicad.step.model.StepLayeredItem;
import com.minicad.step.model.StepMaterialDesignation;
import com.minicad.step.model.StepMeasureQualification;
import com.minicad.step.model.StepMeasureRepresentationItemWithUnit;
import com.minicad.step.model.StepPlusMinusTolerance;
import com.minicad.step.model.StepMakeFromBuildAssembly;
import com.minicad.step.model.StepMakeFromFeature;
import com.minicad.step.model.StepMakeFromUsageOption;
import com.minicad.step.model.StepQuantifiedAssemblyComponentUsage;
import com.minicad.step.model.StepSpecifiedHigherUsageOccurrence;
import com.minicad.step.model.StepAlternateProductRelationship;
import com.minicad.step.model.StepProductDefinitionWithAssociatedDocuments;
import com.minicad.step.model.StepShapeAspectShapeRepresentation;
import com.minicad.step.model.StepAssemblyComponentRelationship;
import com.minicad.step.model.StepDesignMakeFrom;
import com.minicad.step.model.StepDesignedPartDesignVersion;
import com.minicad.step.model.StepInterpolatedConfigurationSegment;
import com.minicad.step.model.StepRangeDimensionalSize;
import com.minicad.step.model.StepSurfaceStyleRendering;
import com.minicad.step.model.StepSurfaceStyleRenderingWithProperties;
import com.minicad.step.model.StepRenderingProperties;
import com.minicad.step.model.StepLightSource;
import com.minicad.step.model.StepLightSourceAmbient;
import com.minicad.step.model.StepLightSourceDirectional;
import com.minicad.step.model.StepLightSourcePositional;
import com.minicad.step.model.StepLightSourceSpot;
import com.minicad.step.model.StepPresentationLayerUsage;
import com.minicad.step.model.StepCameraModelD2;
import com.minicad.step.model.StepCameraModelD3;
import com.minicad.step.model.StepCameraUsage;
import com.minicad.step.model.StepCameraImage;
import com.minicad.step.model.StepPlanarBox;
import com.minicad.step.model.StepPlanarExtent;
import com.minicad.step.model.StepViewVolume;
import com.minicad.step.model.StepMechanicalDesignShapeRepresentation;
import com.minicad.step.model.StepKinematicPair;
import com.minicad.step.model.StepKinematicJoint;
import com.minicad.step.model.StepKinematicLink;
import com.minicad.step.model.StepKinematicStructure;
import com.minicad.step.model.StepPrismaticPair;
import com.minicad.step.model.StepRevolutePair;
import com.minicad.step.model.StepCylindricalPair;
import com.minicad.step.model.StepSphericalPair;
import com.minicad.step.model.StepPlanarPair;
import com.minicad.step.model.StepUniversalPair;
import com.minicad.step.model.StepScrewPair;
import com.minicad.step.model.StepGearPair;
import com.minicad.step.model.StepGearPairWithRange;
import com.minicad.step.model.StepRackAndPinionPair;
import com.minicad.step.model.StepLowOrderKinematicPairWithRange;
import com.minicad.step.model.StepActuatedKinematicPair;
import com.minicad.step.model.StepMechanismStateRepresentation;
import com.minicad.step.model.StepKinematicPath;
import com.minicad.step.model.StepKinematicFrameBasedTransformation;
import com.minicad.step.model.StepPersonAndOrganizationAddress;
import com.minicad.step.model.StepOrganizationAddress;
import com.minicad.step.model.StepPersonAddress;
import com.minicad.step.model.StepAngularSize;
import com.minicad.step.model.StepGeneralizedDatum;
import com.minicad.step.model.StepActionDirective;
import com.minicad.step.model.StepActionMethod;
import com.minicad.step.model.StepAction;
import com.minicad.step.model.StepActionRelationship;
import com.minicad.step.model.StepActionStatus;
import com.minicad.step.model.StepAnalysisInstance;
import com.minicad.step.model.StepAnalysisResult;
import com.minicad.step.model.StepConfigurationInstance;
import com.minicad.step.model.StepModelDefinition;
import com.minicad.step.model.StepModelInstance;
import com.minicad.step.model.StepSimulationDefinition;
import com.minicad.step.model.StepSimulationInstance;
import com.minicad.step.model.StepRunoutToleranceZone;
import com.minicad.step.model.StepShapeDimensionRepresentation;
import com.minicad.step.model.StepToleranceValue;
import com.minicad.step.model.StepToleranceZone;
import com.minicad.step.model.StepToleranceZoneForm;
import com.minicad.step.model.StepWithDescriptiveRepresentationItem;
import com.minicad.step.model.StepConnectedEdgeSet;
import com.minicad.step.model.StepConnectedFaceSet;
import com.minicad.step.model.StepConnectedFaceSubSet;
import com.minicad.step.model.StepCompositeCurve;
import com.minicad.step.model.StepCompositeCurveOnSurface;
import com.minicad.step.model.StepCompositeCurveOnSurface3D;
import com.minicad.step.model.StepCompositeCurveSegment;
import com.minicad.step.model.StepConicCurve;
import com.minicad.step.model.StepCurvedToleranceZone;
import com.minicad.step.model.StepFreeFormSurface;
import com.minicad.step.model.StepMeasurementPoint;
import com.minicad.step.model.StepSurfaceMeasurement;
import com.minicad.step.model.StepSurfaceQuality;
import com.minicad.step.model.StepSurfaceTextureRepresentationItem;
import com.minicad.step.model.StepBSplineCurveWithKnotsAndBreakpoints;
import com.minicad.step.model.StepBSplineSurfaceWithKnotsAndBreakpoints;
import com.minicad.step.model.StepConicalSurface;
import com.minicad.step.model.StepConicalSurfaceWithEllipticalAxis;
import com.minicad.step.model.StepConicCurve;
import com.minicad.step.model.StepContract;
import com.minicad.step.model.StepContractAssignment;
import com.minicad.step.model.StepContractType;
import com.minicad.step.model.StepConversionBasedUnit;
import com.minicad.step.model.StepConversionBasedUnitWithOffset;
import com.minicad.step.model.StepContextDependentUnit;
import com.minicad.step.model.StepContactRatioRepresentation;
import com.minicad.step.model.StepCoordinatedUniversalTimeOffset;
import com.minicad.step.model.StepCylindricalSurface;
import com.minicad.step.model.StepCylindricalSurfaceWithEllipticalAxis;
import com.minicad.step.model.StepCsgPrimitive;
import com.minicad.step.model.StepCsgSolid;
import com.minicad.step.model.StepCsgVolume;
import com.minicad.step.model.StepCylinderVolume;
import com.minicad.step.model.StepSphereVolume;
import com.minicad.step.model.StepTorusVolume;
import com.minicad.step.model.StepPrismVolume;
import com.minicad.step.model.StepBlockVolume;
import com.minicad.step.model.StepTypedMeasureWithUnit;
import com.minicad.step.model.StepCartesianTransformationOperator;
import com.minicad.step.model.StepCurveStyle;
import com.minicad.step.model.StepDateAssignment;
import com.minicad.step.model.StepDegeneratePcurve;
import com.minicad.step.model.StepDegenerateCurve;
import com.minicad.step.model.StepDirection;
import com.minicad.step.model.StepDimensionCurve;
import com.minicad.step.model.StepDimensionalExponents;
import com.minicad.step.model.StepDateAndTime;
import com.minicad.step.model.StepDateTimeAssignment;
import com.minicad.step.model.StepDateTimeRole;
import com.minicad.step.model.StepDateRole;
import com.minicad.step.model.StepAppliedDateAssignment;
import com.minicad.step.model.StepDegenerateToroidalSurface;
import com.minicad.step.model.StepEdgeWire;
import com.minicad.step.model.StepDerivedUnit;
import com.minicad.step.model.StepDerivedUnitElement;
import com.minicad.step.model.StepDescriptiveRepresentationItem;
import com.minicad.step.model.StepDescriptionAttribute;
import com.minicad.step.model.StepDocument;
import com.minicad.step.model.StepDocumentReference;
import com.minicad.step.model.StepDocumentRelationship;
import com.minicad.step.model.StepDocumentType;
import com.minicad.step.model.StepDocumentUsageConstraint;
import com.minicad.step.model.StepDraughtingAnnotationOccurrence;
import com.minicad.step.model.StepDraughtingModelItemAssociation;
import com.minicad.step.model.StepDraughtingModelItemAssociationWithPlaceholder;
import com.minicad.step.model.StepDraughtingPreDefinedColour;
import com.minicad.step.model.StepDraughtingPreDefinedCurveFont;
import com.minicad.step.model.StepDraughtingPreDefinedTextFont;
import com.minicad.step.model.StepDraughtingCallout;
import com.minicad.step.model.StepDraughtingCalloutRelationship;
import com.minicad.step.model.StepEdgeCurve;
import com.minicad.step.model.StepEdgeBasedWireframeModel;
import com.minicad.step.model.StepEdgeLoop;
import com.minicad.step.model.StepEffectivity;
import com.minicad.step.model.StepEffectivityRelationship;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepExternalSource;
import com.minicad.step.model.StepExternalIdentificationAssignment;
import com.minicad.step.model.StepExternalSourceRelationship;
import com.minicad.step.model.StepExternallyDefinedItem;
import com.minicad.step.model.StepFaceEntity;
import com.minicad.step.model.StepFacettedBrep;
import com.minicad.step.model.StepFaceBound;
import com.minicad.step.model.StepFaceBasedSurfaceModel;
import com.minicad.step.model.StepFaceSurface;
import com.minicad.step.model.StepFillAreaStyle;
import com.minicad.step.model.StepFillAreaStyleColour;
import com.minicad.step.model.StepForwardChainingRulePremise;
import com.minicad.step.model.StepGeometricCurveSet;
import com.minicad.step.model.StepGeometricSet;
import com.minicad.step.model.StepGeometricTolerance;
import com.minicad.step.model.StepToleranceZoneForm;
import com.minicad.step.model.StepToleranceZone;
import com.minicad.step.model.StepConfigurationItem;
import com.minicad.step.model.StepDirectedDimensionalSize;
import com.minicad.step.model.StepGeometricItemSpecificUsage;
import com.minicad.step.model.StepGeometricRepresentationContext;
import com.minicad.step.model.StepGeneralProperty;
import com.minicad.step.model.StepGeneralPropertyRelationship;
import com.minicad.step.model.StepGeometricReplica;
import com.minicad.step.model.StepGeometricSurfaceSet;
import com.minicad.step.model.StepGlobalUncertaintyAssignedContext;
import com.minicad.step.model.StepGlobalUnitAssignedContext;
import com.minicad.step.model.StepGroup;
import com.minicad.step.model.StepGroupAssignment;
import com.minicad.step.model.StepGroupRelationship;
import com.minicad.step.model.StepHalfSpaceSolid;
import com.minicad.step.model.StepIdAttribute;
import com.minicad.step.model.StepIdentificationAssignment;
import com.minicad.step.model.StepIdentificationRole;
import com.minicad.step.model.StepItemIdentifiedRepresentationUsage;
import com.minicad.step.model.StepIndexedPolyCurve;
import com.minicad.step.model.StepItemDefinedTransformation;
import com.minicad.step.model.StepKinematicPropertyDefinitionRepresentation;
import com.minicad.step.model.StepKinematicPropertyMechanismRepresentation;
import com.minicad.step.model.StepKinematicPropertyRepresentationRelation;
import com.minicad.step.model.StepKinematicPropertyTopologyRepresentation;
import com.minicad.step.model.StepLanguage;
import com.minicad.step.model.StepLanguageAssignment;
import com.minicad.step.model.StepLeaderCurve;
import com.minicad.step.model.StepLine;
import com.minicad.step.model.StepLineSegment;
import com.minicad.step.model.StepLocalTime;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepManifoldSurfaceModel;
import com.minicad.step.model.StepMappedItem;
import com.minicad.step.model.StepMeasureWithUnit;
import com.minicad.step.model.StepMeasureRepresentationItem;
import com.minicad.step.model.StepMechanicalDesignRequirementItemAssociation;
import com.minicad.step.model.StepNamedUnit;
import com.minicad.step.model.StepNameAssignment;
import com.minicad.step.model.StepNameAttribute;
import com.minicad.step.model.StepOpenShell;
import com.minicad.step.model.StepOrganization;
import com.minicad.step.model.StepOrganizationAssignment;
import com.minicad.step.model.StepOrganizationRelationship;
import com.minicad.step.model.StepOrganizationRole;
import com.minicad.step.model.StepOpenPath;
import com.minicad.step.model.StepOverRidingStyledItem;
import com.minicad.step.model.StepOrientedEdge;
import com.minicad.step.model.StepOrientedFace;
import com.minicad.step.model.StepOrientedClosedShell;
import com.minicad.step.model.StepOrientedOpenShell;
import com.minicad.step.model.StepOrientedPath;
import com.minicad.step.model.StepOrientedCurve;
import com.minicad.step.model.StepOrientedSurface;
import com.minicad.step.model.StepPath;
import com.minicad.step.model.StepPerson;
import com.minicad.step.model.StepPersonAndOrganization;
import com.minicad.step.model.StepPersonAndOrganizationAssignment;
import com.minicad.step.model.StepPersonAndOrganizationRole;
import com.minicad.step.model.StepPlane;
import com.minicad.step.model.StepPlacedDatumTargetFeature;
import com.minicad.step.model.StepPlacedTarget;
import com.minicad.step.model.StepPolyLoop;
import com.minicad.step.model.StepPolyline;
import com.minicad.step.model.StepPcurve;
import com.minicad.step.model.StepEllipse;
import com.minicad.step.model.StepProduct;
import com.minicad.step.model.StepProductCategory;
import com.minicad.step.model.StepProductCategoryRelationship;
import com.minicad.step.model.StepProjectionCurve;
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
import com.minicad.step.model.StepPropertyDefinition;
import com.minicad.step.model.StepPropertyDefinitionRelationship;
import com.minicad.step.model.StepPropertyDefinitionRepresentation;
import com.minicad.step.model.StepPresentationLayerAssignment;
import com.minicad.step.model.StepPresentationStyleAssignment;
import com.minicad.step.model.StepPoint;
import com.minicad.step.model.StepPointSet;
import com.minicad.step.model.StepPiecewiseBezierCurve;
import com.minicad.step.model.StepPiecewiseBezierSurface;
import com.minicad.step.model.StepPointStyle;
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
import com.minicad.step.model.StepPmiRequirementItemAssociation;
import com.minicad.step.model.StepRepresentation;
import com.minicad.step.model.StepAdvancedBrep;
import com.minicad.step.model.StepRuledSurface;
import com.minicad.step.model.StepRevolvedAreaSolidTapered;
import com.minicad.step.model.StepExtrudedAreaSolidTapered;
import com.minicad.step.model.StepSurfaceCurveSweptAreaSolid;
import com.minicad.step.model.StepTessellatedFace;
import com.minicad.step.model.StepTessellatedFaceSet;
import com.minicad.step.model.StepTriangulatedFace;
import com.minicad.step.model.StepComplexTriangulatedFace;
import com.minicad.step.model.StepCubicBezierTriangulatedFace;
import com.minicad.step.model.StepTessellatedTriangle;
import com.minicad.step.model.StepFiniteElementMesh;
import com.minicad.step.model.StepVolume3dElementRepresentation;
import com.minicad.step.model.StepVolume3dElementProperty;
import com.minicad.step.model.StepCurve3dElementProperty;
import com.minicad.step.model.StepSurface3dElementProperty;
import com.minicad.step.model.StepFeaMaterialPropertyRepresentation;
import com.minicad.step.model.StepElementVolume2d;
import com.minicad.step.model.StepElementVolume3d;
import com.minicad.step.model.StepNodeSet;
import com.minicad.step.model.StepElementSet;
import com.minicad.step.model.StepFeaSecuredVariable;
import com.minicad.step.model.StepFeaConstantFunction3d;
import com.minicad.step.model.StepFeaLinearAlgebraicMatrix;
import com.minicad.step.model.StepFeaLinearAlgebraicVector;
import com.minicad.step.model.StepFeaAxis2Placement3d;
import com.minicad.step.model.StepFeaGroupRepresentation;
import com.minicad.step.model.StepTextLiteral;
import com.minicad.step.model.StepRepresentationMap;
import com.minicad.step.model.StepRepresentationItem;
import com.minicad.step.model.StepRepresentationRelationship;
import com.minicad.step.model.StepRepresentationContext;
import com.minicad.step.model.StepRepresentationRelationshipWithTransformation;
import com.minicad.step.model.StepRectangularTrimmedSurface;
import com.minicad.step.model.StepResourcePropertyRepresentation;
import com.minicad.step.model.StepRowVariable;
import com.minicad.step.model.StepScalarVariable;
import com.minicad.step.model.StepCurve;
import com.minicad.step.model.StepCurveBoundedSurface;
import com.minicad.step.model.StepGeometricRepresentationItem;
import com.minicad.step.model.StepSecurityClassification;
import com.minicad.step.model.StepSecurityClassificationAssignment;
import com.minicad.step.model.StepSecurityClassificationLevel;
import com.minicad.step.model.StepShapeAspect;
import com.minicad.step.model.StepShapeAspectOccurrence;
import com.minicad.step.model.StepShapeAspectRelationship;
import com.minicad.step.model.StepShapeRepresentationRelationship;
import com.minicad.step.model.StepShapeDefinitionRepresentation;
import com.minicad.step.model.StepShellBasedSurfaceModel;
import com.minicad.step.model.StepSeamCurve;
import com.minicad.step.model.StepSeamEdge;
import com.minicad.step.model.StepSiUnit;
import com.minicad.step.model.StepSolidModel;
import com.minicad.step.model.StepSolidReplica;
import com.minicad.step.model.StepSurface;
import com.minicad.step.model.StepSurfaceCurve;
import com.minicad.step.model.StepSurfacedEdgeCurve;
import com.minicad.step.model.StepSurfacedOpenShell;
import com.minicad.step.model.StepSurfaceModel;
import com.minicad.step.model.StepSurfaceOfLinearExtrusion;
import com.minicad.step.model.StepSurfaceOfTranslation;
import com.minicad.step.model.StepSurfaceOfProjection;
import com.minicad.step.model.StepParaboloidSurface;
import com.minicad.step.model.StepHyperboloidSurface;
import com.minicad.step.model.StepSurfaceOfRevolution;
import com.minicad.step.model.StepSurfaceOfConstantRadius;
import com.minicad.step.model.StepSphericalSurface;
import com.minicad.step.model.StepSphericalSurfaceWithEllipticalAxis;
import com.minicad.step.model.StepPolygonalBoundedHalfSpace;
import com.minicad.step.model.StepSurfacePatch;
import com.minicad.step.model.StepSurfaceSideStyle;
import com.minicad.step.model.StepSurfaceStyleFillArea;
import com.minicad.step.model.StepSurfaceStyleBoundary;
import com.minicad.step.model.StepSurfaceStyleControlGrid;
import com.minicad.step.model.StepSurfaceStyleParameterLine;
import com.minicad.step.model.StepSurfaceStyleReflectanceAmbient;
import com.minicad.step.model.StepSurfaceStyleReflectanceAmbientDiffuse;
import com.minicad.step.model.StepSurfaceStyleReflectanceAmbientDiffuseSpecular;
import com.minicad.step.model.StepSurfaceStyleSegmentationCurve;
import com.minicad.step.model.StepSurfaceStyleSilhouette;
import com.minicad.step.model.StepSurfaceStyleTransparent;
import com.minicad.step.model.StepSurfaceStyleUsage;
import com.minicad.step.model.StepSweptAreaSolid;
import com.minicad.step.model.StepExtrudedFaceSolid;
import com.minicad.step.model.StepRevolvedFaceSolid;
import com.minicad.step.model.StepSweptFaceSolid;
import com.minicad.step.model.StepSweptDiskSolid;
import com.minicad.step.model.StepSymbolColour;
import com.minicad.step.model.StepSymbolRepresentationMap;
import com.minicad.step.model.StepSymbolStyle;
import com.minicad.step.model.StepStyledItem;
import com.minicad.step.model.StepSubedge;
import com.minicad.step.model.StepSubpath;
import com.minicad.step.model.StepSubface;
import com.minicad.step.model.StepOrientedSubface;
import com.minicad.step.model.StepRectangleHollowProfileDef;
import com.minicad.step.model.StepTextStyle;
import com.minicad.step.model.StepTextStyleWithBoxCharacteristics;
import com.minicad.step.model.StepTextStyleForDefinedFont;
import com.minicad.step.model.StepTextStyleWithJustification;
import com.minicad.step.model.StepTextStyleWithMirror;
import com.minicad.step.model.StepTextStyleWithSpacing;
import com.minicad.step.model.StepTerminatorSymbol;
import com.minicad.step.model.StepTypedMeasureWithUnit;
import com.minicad.step.model.StepTopologicalRepresentationItem;
import com.minicad.step.model.StepTrimmedCurve;
import com.minicad.step.model.StepToroidalSurface;
import com.minicad.step.model.StepToroidalSurfaceWithCylindricalAxis;
import com.minicad.step.model.StepToroidalSurfaceWithEllipticalAxis;
import com.minicad.step.model.StepUniformCurve;
import com.minicad.step.model.StepQuasiUniformCurve;
import com.minicad.step.model.StepUniformSurface;
import com.minicad.step.model.StepQuasiUniformSurface;
import com.minicad.step.model.StepUncertaintyMeasureWithUnit;
import com.minicad.step.model.StepNonManifoldSolidBrep;
import com.minicad.step.model.StepNextAssemblyUsageOccurrence;
import com.minicad.step.model.StepOffsetCurve2D;
import com.minicad.step.model.StepOffsetCurve3D;
import com.minicad.step.model.StepOffsetSurface;
import com.minicad.step.model.StepOffsetSurface2;
import com.minicad.step.model.StepContextDependentShapeRepresentation;
import com.minicad.step.model.StepFace;
import com.minicad.step.model.StepEdge;
import com.minicad.step.model.StepLoop;
import com.minicad.step.model.StepMachinedSurface;
import com.minicad.step.model.StepVector;
import com.minicad.step.model.StepVertex;
import com.minicad.step.model.StepVertexLoop;
import com.minicad.step.model.StepVertexPoint;
import com.minicad.step.model.StepVertexShell;
import com.minicad.step.model.StepValueRepresentationItem;
import com.minicad.step.model.StepUserDefinedCurveFont;
import com.minicad.step.model.StepUserDefinedMarker;
import com.minicad.step.model.StepUserDefinedTerminatorSymbol;
import com.minicad.step.model.StepWireShell;
import com.minicad.step.model.StepShellBasedWireframeModel;
import com.minicad.step.model.StepCircle2D;
import com.minicad.step.model.StepEllipse2D;
import com.minicad.step.model.StepHyperbola2D;
import com.minicad.step.model.StepParabola2D;
import com.minicad.step.model.StepLine2D;
import com.minicad.step.model.StepPolyline2D;
import com.minicad.step.model.StepTrimmedCurve2D;
import com.minicad.step.model.StepBoundedCurve2D;
import com.minicad.step.model.StepCompositeCurve2D;
import com.minicad.step.model.StepCurve2D;
import com.minicad.step.model.StepBSplineCurve2D;
import com.minicad.step.model.StepRationalBSplineCurve2D;
import com.minicad.step.model.StepBezierCurve2D;
import com.minicad.step.model.StepQuasiUniformCurve2D;
import com.minicad.step.model.StepUniformCurve2D;
import com.minicad.step.model.StepPiecewiseBezierCurve2D;
import com.minicad.step.model.StepIndexedPolyCurve2D;
import com.minicad.step.model.StepDegenerateCurve2D;
import com.minicad.step.model.StepValidationPropertyRepresentation;
import com.minicad.step.model.StepCalculatedGeometricRepresentationItem;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepValue;

import java.util.*;
import java.util.stream.Collectors;

/** Resolves raw STEP AST entities into a minimal semantic model. */
public final class StepEntityResolver {

  private static final Map<String, EntityFactory> REGISTRY = createRegistry();

  private final Map<Integer, StepEntityInstance> instancesById;
  private final Map<Integer, StepEntity> resolved = new LinkedHashMap<>();

  private StepEntityResolver(StepFile file) {
    this.instancesById = file.entitiesById();
  }

  /**
   * Resolves all supported entities in the file.
   *
   * @param file parsed STEP file
   * @return resolved entities indexed by id
   */
  public static Map<Integer, StepEntity> resolveAll(StepFile file) {
    return new StepEntityResolver(file).resolveAll();
  }

  private Map<Integer, StepEntity> resolveAll() {
    for (Integer id : instancesById.keySet()) {
      resolve(id);
    }
    return Map.copyOf(resolved);
  }

  private StepEntity resolve(int id) {
    StepEntity existing = resolved.get(id);
    if (existing != null) {
      return existing;
    }

    StepEntityInstance instance = instancesById.get(id);
    if (instance == null) {
      throw new StepResolutionException("missing referenced entity #" + id);
    }

    for (Map.Entry<String, EntityFactory> entry : REGISTRY.entrySet()) {
      if (!instance.hasDefinition(entry.getKey())) {
        continue;
      }
      StepEntity entity = entry.getValue().create(this, instance);
      resolved.put(id, entity);
      return entity;
    }

    throw new UnsupportedStepEntityException("unsupported STEP entity " + instance.name());
  }

  private StepCartesianPoint resolveCartesianPoint(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CARTESIAN_POINT");
    requireParameterCount(instance, definition, 2);
    return new StepCartesianPoint(
        instance.id(),
        stringValue(instance, definition, 0),
        coordinateList(instance, definition, 1, 2, 3));
  }

  private StepDirection resolveDirection(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DIRECTION");
    requireParameterCount(instance, definition, 2);
    return new StepDirection(
        instance.id(),
        stringValue(instance, definition, 0),
        coordinateList(instance, definition, 1, 2, 3));
  }

  private StepVector resolveVector(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "VECTOR");
    requireParameterCount(instance, definition, 3);
    return new StepVector(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepDirection.class,
            "VECTOR orientation must reference DIRECTION"),
        numberValue(instance, definition, 2));
  }

  private StepAxis2Placement3D resolveAxis2Placement3D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "AXIS2_PLACEMENT_3D");
    requireParameterCount(instance, definition, 4);
    if (isUnset(definition.parameters().get(2)) || isUnset(definition.parameters().get(3))) {
      throw new UnsupportedStepEntityException(
          "AXIS2_PLACEMENT_3D requires explicit axis and ref direction");
    }
    return new StepAxis2Placement3D(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepCartesianPoint.class,
            "AXIS2_PLACEMENT_3D location must reference CARTESIAN_POINT"),
        requireEntity(
            referenceId(instance, definition, 2),
            StepDirection.class,
            "AXIS2_PLACEMENT_3D axis must reference DIRECTION"),
        requireEntity(
            referenceId(instance, definition, 3),
            StepDirection.class,
            "AXIS2_PLACEMENT_3D ref direction must reference DIRECTION"));
  }

  private StepAxis1Placement resolveAxis1Placement(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "AXIS1_PLACEMENT");
    requireParameterCount(instance, definition, 3);
    return new StepAxis1Placement(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepCartesianPoint.class,
            "AXIS1_PLACEMENT location must reference CARTESIAN_POINT"),
        requireEntity(
            referenceId(instance, definition, 2),
            StepDirection.class,
            "AXIS1_PLACEMENT axis must reference DIRECTION"));
  }

  private StepAxis2Placement2D resolveAxis2Placement2D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "AXIS2_PLACEMENT_2D");
    requireParameterCount(instance, definition, 3);
    return new StepAxis2Placement2D(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepCartesianPoint.class,
            "AXIS2_PLACEMENT_2D location must reference CARTESIAN_POINT"),
        requireEntity(
            referenceId(instance, definition, 2),
            StepDirection.class,
            "AXIS2_PLACEMENT_2D ref direction must reference DIRECTION"));
  }

  private StepLine resolveLine(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LINE");
    requireParameterCount(instance, definition, 3);
    return new StepLine(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepCartesianPoint.class,
            "LINE point must reference CARTESIAN_POINT"),
        requireEntity(
            referenceId(instance, definition, 2),
            StepVector.class,
            "LINE vector must reference VECTOR"));
  }

  private StepPolyline resolvePolyline(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "POLYLINE");
    requireParameterCount(instance, definition, 2);
    return new StepPolyline(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepCartesianPoint.class,
            "POLYLINE points must reference CARTESIAN_POINT"));
  }

  private StepPlane resolvePlane(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PLANE");
    requireParameterCount(instance, definition, 2);
    return new StepPlane(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "PLANE position must reference AXIS2_PLACEMENT_3D"));
  }

  private StepCircle resolveCircle(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CIRCLE");
    requireParameterCount(instance, definition, 3);
    StepEntity position = resolve(referenceId(instance, definition, 1));
    if (!(position instanceof StepAxis2Placement3D)
        && !(position instanceof StepAxis2Placement2D)) {
      throw new StepResolutionException(
          "CIRCLE position must reference AXIS2_PLACEMENT_3D or AXIS2_PLACEMENT_2D");
    }
    return new StepCircle(
        instance.id(),
        stringValue(instance, definition, 0),
        position,
        numberValue(instance, definition, 2));
  }

  private StepEllipse resolveEllipse(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ELLIPSE");
    requireParameterCount(instance, definition, 4);
    StepEntity position = resolve(referenceId(instance, definition, 1));
    if (!(position instanceof StepAxis2Placement3D)
        && !(position instanceof StepAxis2Placement2D)) {
      throw new StepResolutionException(
          "ELLIPSE position must reference AXIS2_PLACEMENT_3D or AXIS2_PLACEMENT_2D");
    }
    return new StepEllipse(
        instance.id(),
        stringValue(instance, definition, 0),
        position,
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  private StepConicCurve resolveConicCurve(
      StepEntityInstance instance, String entityName, int parameterCount) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, parameterCount + 2);
    StepEntity position = resolve(referenceId(instance, definition, 1));
    if (!(position instanceof StepAxis2Placement3D)
        && !(position instanceof StepAxis2Placement2D)) {
      throw new StepResolutionException(
          entityName + " position must reference AXIS2_PLACEMENT_3D or AXIS2_PLACEMENT_2D");
    }
    List<Double> parameters = new ArrayList<>(parameterCount);
    for (int index = 0; index < parameterCount; index++) {
      parameters.add(numberValue(instance, definition, index + 2));
    }
    return new StepConicCurve(
        instance.id(), stringValue(instance, definition, 0), position, parameters, entityName);
  }

  private StepCylindricalSurface resolveCylindricalSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CYLINDRICAL_SURFACE");
    requireParameterCount(instance, definition, 3);
    return new StepCylindricalSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "CYLINDRICAL_SURFACE position must reference AXIS2_PLACEMENT_3D"),
        numberValue(instance, definition, 2));
  }

  private StepConicalSurface resolveConicalSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONICAL_SURFACE");
    requireParameterCount(instance, definition, 4);
    return new StepConicalSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "CONICAL_SURFACE position must reference AXIS2_PLACEMENT_3D"),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  private StepToroidalSurface resolveToroidalSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TOROIDAL_SURFACE");
    requireParameterCount(instance, definition, 4);
    return new StepToroidalSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "TOROIDAL_SURFACE position must reference AXIS2_PLACEMENT_3D"),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  private StepDegenerateToroidalSurface resolveDegenerateToroidalSurface(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DEGENERATE_TOROIDAL_SURFACE");
    requireParameterCount(instance, definition, 5);
    return new StepDegenerateToroidalSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "DEGENERATE_TOROIDAL_SURFACE position must reference AXIS2_PLACEMENT_3D"),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3),
        booleanValue(instance, definition, 4));
  }

  private StepSphericalSurface resolveSphericalSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SPHERICAL_SURFACE");
    requireParameterCount(instance, definition, 3);
    return new StepSphericalSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "SPHERICAL_SURFACE position must reference AXIS2_PLACEMENT_3D"),
        numberValue(instance, definition, 2));
  }

  private StepCylindricalSurfaceWithEllipticalAxis resolveCylindricalSurfaceWithEllipticalAxis(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CYLINDRICAL_SURFACE_WITH_ELLIPTICAL_AXIS");
    requireParameterCount(instance, definition, 4);
    return new StepCylindricalSurfaceWithEllipticalAxis(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "CYLINDRICAL_SURFACE_WITH_ELLIPTICAL_AXIS position must reference AXIS2_PLACEMENT_3D"),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  private StepConicalSurfaceWithEllipticalAxis resolveConicalSurfaceWithEllipticalAxis(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONICAL_SURFACE_WITH_ELLIPTICAL_AXIS");
    requireParameterCount(instance, definition, 5);
    return new StepConicalSurfaceWithEllipticalAxis(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "CONICAL_SURFACE_WITH_ELLIPTICAL_AXIS position must reference AXIS2_PLACEMENT_3D"),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3),
        numberValue(instance, definition, 4));
  }

  private StepSphericalSurfaceWithEllipticalAxis resolveSphericalSurfaceWithEllipticalAxis(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SPHERICAL_SURFACE_WITH_ELLIPTICAL_AXIS");
    requireParameterCount(instance, definition, 4);
    return new StepSphericalSurfaceWithEllipticalAxis(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "SPHERICAL_SURFACE_WITH_ELLIPTICAL_AXIS position must reference AXIS2_PLACEMENT_3D"),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  private StepToroidalSurfaceWithEllipticalAxis resolveToroidalSurfaceWithEllipticalAxis(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TOROIDAL_SURFACE_WITH_ELLIPTICAL_AXIS");
    requireParameterCount(instance, definition, 5);
    return new StepToroidalSurfaceWithEllipticalAxis(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "TOROIDAL_SURFACE_WITH_ELLIPTICAL_AXIS position must reference AXIS2_PLACEMENT_3D"),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3),
        numberValue(instance, definition, 4));
  }

  private StepToroidalSurfaceWithCylindricalAxis resolveToroidalSurfaceWithCylindricalAxis(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TOROIDAL_SURFACE_WITH_CYLINDRICAL_AXIS");
    requireParameterCount(instance, definition, 4);
    return new StepToroidalSurfaceWithCylindricalAxis(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis1Placement.class,
            "TOROIDAL_SURFACE_WITH_CYLINDRICAL_AXIS position must reference AXIS1_PLACEMENT"),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  private StepBlendedSurface resolveBlendedSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BLENDED_SURFACE");
    requireParameterCount(instance, definition, 7);
    return new StepBlendedSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        numberValue(instance, definition, 4),
        resolve(referenceId(instance, definition, 5)));
  }

  private StepChamferEdge resolveChamferEdge(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CHAMFER_EDGE");
    requireParameterCount(instance, definition, 6);
    List<StepEntity> adjacentFaces =
        entityReferenceList(
            instance,
            definition,
            4,
            "CHAMFER_EDGE adjacent_faces must contain entity references");
    return new StepChamferEdge(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3),
        adjacentFaces,
        stringValue(instance, definition, 5));
  }

  private StepFilletEdge resolveFilletEdge(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FILLET_EDGE");
    requireParameterCount(instance, definition, 5);
    List<StepEntity> adjacentFaces =
        entityReferenceList(
            instance,
            definition,
            3,
            "FILLET_EDGE adjacent_faces must contain entity references");
    return new StepFilletEdge(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2),
        adjacentFaces,
        stringValue(instance, definition, 4));
  }

  private StepFreeFormSurface resolveFreeFormSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FREE_FORM_SURFACE");
    // Free form surface is complex - use a simpler resolution approach
    List<List<StepEntity>> controlPoints = resolveFreeFormControlPoints(instance, definition, 2);
    List<Double> knotVectors = numberList(instance, definition, 6);
    List<Double> weights = numberList(instance, definition, 7);
    return new StepFreeFormSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        controlPoints,
        (int) numberValue(instance, definition, 3),
        (int) numberValue(instance, definition, 4),
        knotVectors,
        weights);
  }

  private List<List<StepEntity>> resolveFreeFormControlPoints(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue outerList)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a nested list");
    }
    List<List<StepEntity>> result = new ArrayList<>();
    for (StepValue outerElement : outerList.elements()) {
      StepValue unwrappedOuter = unwrapTyped(outerElement);
      if (!(unwrappedOuter instanceof StepValue.ListValue innerList)) {
        throw new StepResolutionException(
            definition.name() + " control_points must contain nested lists");
      }
      List<StepEntity> row = new ArrayList<>();
      for (StepValue innerElement : innerList.elements()) {
        StepValue unwrappedInner = unwrapTyped(innerElement);
        if (!(unwrappedInner instanceof StepValue.ReferenceValue referenceValue)) {
          throw new StepResolutionException(
              definition.name() + " control_points inner elements must be references");
        }
        row.add(resolve(referenceValue.id()));
      }
      result.add(List.copyOf(row));
    }
    return List.copyOf(result);
  }

  private StepCurvedToleranceZone resolveCurvedToleranceZone(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CURVED_TOLERANCE_ZONE");
    requireParameterCount(instance, definition, 5);
    return new StepCurvedToleranceZone(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  private StepSurfaceQuality resolveSurfaceQuality(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_QUALITY");
    requireParameterCount(instance, definition, 7);
    List<Double> roughnessValues = numberList(instance, definition, 3);
    return new StepSurfaceQuality(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        roughnessValues,
        stringValue(instance, definition, 4),
        stringValue(instance, definition, 5),
        resolve(referenceId(instance, definition, 6)));
  }

  private StepMeasurementPoint resolveMeasurementPoint(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MEASUREMENT_POINT");
    requireParameterCount(instance, definition, 8);
    return new StepMeasurementPoint(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        stringValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)),
        resolve(referenceId(instance, definition, 4)),
        numberValue(instance, definition, 5),
        (int) numberValue(instance, definition, 6));
  }

  private StepSurfaceMeasurement resolveSurfaceMeasurement(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_MEASUREMENT");
    requireParameterCount(instance, definition, 8);
    List<String> roughnessParameters = literalList(instance, definition, 3);
    List<Double> measuredValues = numberList(instance, definition, 4);
    return new StepSurfaceMeasurement(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        roughnessParameters,
        measuredValues,
        stringValue(instance, definition, 5),
        resolve(referenceId(instance, definition, 6)),
        stringValue(instance, definition, 7));
  }

  private StepSurfaceTextureRepresentationItem resolveSurfaceTextureRepresentationItem(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_TEXTURE_REPRESENTATION_ITEM");
    requireParameterCount(instance, definition, 5);
    return new StepSurfaceTextureRepresentationItem(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalNumberValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        stringValue(instance, definition, 3));
  }

  private StepGeometricReplica resolveGeometricReplica(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 3);
    StepEntity parent = resolve(referenceId(instance, definition, 1));
    boolean validParent =
        switch (entityName) {
          case "POINT_REPLICA" ->
              parent instanceof StepCartesianPoint
                  || parent instanceof StepVertexPoint;
          case "CURVE_REPLICA" -> isSupportedCurveReference(parent);
          case "SURFACE_REPLICA" -> isSupportedSurfaceReference(parent);
          default -> false;
        };
    if (!validParent) {
      throw new UnsupportedStepEntityException(
          entityName + " parent must reference a supported "
              + entityName.substring(0, entityName.indexOf("_")).toLowerCase(Locale.ROOT));
    }
    return new StepGeometricReplica(
        instance.id(),
        stringValue(instance, definition, 0),
        parent,
        requireEntity(
            referenceId(instance, definition, 2),
            StepCartesianTransformationOperator.class,
            entityName + " transformation must reference CARTESIAN_TRANSFORMATION_OPERATOR"),
        entityName);
  }

  private StepRectangularTrimmedSurface resolveRectangularTrimmedSurface(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "RECTANGULAR_TRIMMED_SURFACE");
    requireParameterCount(instance, definition, 8);
    StepEntity basisSurface = resolve(referenceId(instance, definition, 1));
    if (!isSupportedSurfaceReference(basisSurface)) {
      throw new UnsupportedStepEntityException(
          "RECTANGULAR_TRIMMED_SURFACE basis_surface must reference a supported surface");
    }
    return new StepRectangularTrimmedSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        basisSurface,
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3),
        numberValue(instance, definition, 4),
        numberValue(instance, definition, 5),
        booleanValue(instance, definition, 6),
        booleanValue(instance, definition, 7));
  }

  private StepCurveBoundedSurface resolveCurveBoundedSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CURVE_BOUNDED_SURFACE");
    requireParameterCount(instance, definition, 4);
    StepEntity basisSurface = resolve(referenceId(instance, definition, 1));
    if (!isSupportedSurfaceReference(basisSurface)) {
      throw new UnsupportedStepEntityException(
          "CURVE_BOUNDED_SURFACE basis_surface must reference a supported surface");
    }
    List<StepEntity> boundaries =
        entityReferenceList(
            instance,
            definition,
            2,
            "CURVE_BOUNDED_SURFACE boundaries must contain entity references");
    if (boundaries.isEmpty()) {
      throw new StepResolutionException("CURVE_BOUNDED_SURFACE boundaries must not be empty");
    }
    for (StepEntity boundary : boundaries) {
      if (!(boundary instanceof StepPcurve)
          && !(boundary instanceof StepSurfaceCurve)
          && !(boundary instanceof StepSeamCurve)
          && !(boundary instanceof StepCompositeCurveOnSurface)
          && !(boundary instanceof StepCompositeCurve)
          && !isSupportedCurveReference(boundary)) {
        throw new UnsupportedStepEntityException(
            "CURVE_BOUNDED_SURFACE boundaries must reference supported curve entities");
      }
    }
    return new StepCurveBoundedSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        basisSurface,
        boundaries,
        booleanValue(instance, definition, 3));
  }

  private StepAnalysisResult resolveAnalysisResult(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ANALYSIS_RESULT");
    requireParameterCount(instance, definition, 9);
    List<Double> resultValues = numberList(instance, definition, 5);
    List<StepEntity> resultLocations = entityReferenceList(
        instance, definition, 6, "ANALYSIS_RESULT result_locations must contain entity references");
    return new StepAnalysisResult(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        resultValues,
        resultLocations,
        numberValue(instance, definition, 7),
        numberValue(instance, definition, 8));
  }

  private StepAnalysisInstance resolveAnalysisInstance(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ANALYSIS_INSTANCE");
    requireParameterCount(instance, definition, 7);
    List<String> analysisResults = literalList(instance, definition, 4);
    return new StepAnalysisInstance(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        stringValue(instance, definition, 2),
        analysisResults,
        stringValue(instance, definition, 5),
        stringValue(instance, definition, 6));
  }

  private StepConfigurationInstance resolveConfigurationInstance(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONFIGURATION_INSTANCE");
    requireParameterCount(instance, definition, 7);
    List<String> configurationValues = literalList(instance, definition, 4);
    return new StepConfigurationInstance(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        stringValue(instance, definition, 2),
        configurationValues,
        booleanValue(instance, definition, 5),
        stringValue(instance, definition, 6));
  }

  private StepModelDefinition resolveModelDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MODEL_DEFINITION");
    requireParameterCount(instance, definition, 7);
    List<String> modelParameters = literalList(instance, definition, 4);
    List<String> modelConstraints = literalList(instance, definition, 5);
    return new StepModelDefinition(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        modelParameters,
        modelConstraints,
        stringValue(instance, definition, 6));
  }

  private StepModelInstance resolveModelInstance(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MODEL_INSTANCE");
    requireParameterCount(instance, definition, 7);
    List<String> modelProperties = literalList(instance, definition, 5);
    return new StepModelInstance(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        stringValue(instance, definition, 2),
        stringValue(instance, definition, 3),
        modelProperties,
        stringValue(instance, definition, 6));
  }

  private StepSimulationDefinition resolveSimulationDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SIMULATION_DEFINITION");
    requireParameterCount(instance, definition, 7);
    List<String> simulationParameters = literalList(instance, definition, 4);
    return new StepSimulationDefinition(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        simulationParameters,
        numberValue(instance, definition, 5),
        stringValue(instance, definition, 6));
  }

  private StepSimulationInstance resolveSimulationInstance(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SIMULATION_INSTANCE");
    requireParameterCount(instance, definition, 8);
    List<StepEntity> simulationResults = entityReferenceList(
        instance, definition, 6, "SIMULATION_INSTANCE simulation_results must contain entity references");
    return new StepSimulationInstance(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        stringValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)),
        resolve(referenceId(instance, definition, 4)),
        simulationResults,
        stringValue(instance, definition, 7));
  }

  private StepOrientedSurface resolveOrientedSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ORIENTED_SURFACE");
    requireParameterCount(instance, definition, 3);
    StepEntity surfaceElement = resolve(referenceId(instance, definition, 1));
    if (!isSupportedSurfaceReference(surfaceElement)) {
      throw new UnsupportedStepEntityException(
          "ORIENTED_SURFACE surface_element must reference a supported surface");
    }
    return new StepOrientedSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        surfaceElement,
        booleanValue(instance, definition, 2));
  }

  private StepSurfaceOfLinearExtrusion resolveSurfaceOfLinearExtrusion(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_OF_LINEAR_EXTRUSION");
    requireParameterCount(instance, definition, 3);
    StepEntity sweptCurve = resolve(referenceId(instance, definition, 1));
    if (!isSupportedCurveReference(sweptCurve)) {
      throw new UnsupportedStepEntityException(
          "SURFACE_OF_LINEAR_EXTRUSION swept_curve must reference a supported curve");
    }
    return new StepSurfaceOfLinearExtrusion(
        instance.id(),
        stringValue(instance, definition, 0),
        sweptCurve,
        requireEntity(
            referenceId(instance, definition, 2),
            StepVector.class,
            "SURFACE_OF_LINEAR_EXTRUSION extrusion_axis must reference VECTOR"));
  }

  private StepSurfaceOfRevolution resolveSurfaceOfRevolution(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_OF_REVOLUTION");
    requireParameterCount(instance, definition, 3);
    StepEntity sweptCurve = resolve(referenceId(instance, definition, 1));
    if (!isSupportedCurveReference(sweptCurve)) {
      throw new UnsupportedStepEntityException(
          "SURFACE_OF_REVOLUTION swept_curve must reference a supported curve");
    }
    return new StepSurfaceOfRevolution(
        instance.id(),
        stringValue(instance, definition, 0),
        sweptCurve,
        requireEntity(
            referenceId(instance, definition, 2),
            StepAxis1Placement.class,
            "SURFACE_OF_REVOLUTION axis_position must reference AXIS1_PLACEMENT"));
  }

  private StepSurfaceOfTranslation resolveSurfaceOfTranslation(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_OF_TRANSLATION");
    requireParameterCount(instance, definition, 4);
    StepEntity profile = resolve(referenceId(instance, definition, 1));
    if (!isSupportedCurveReference(profile)) {
      throw new UnsupportedStepEntityException(
          "SURFACE_OF_TRANSLATION profile must reference a supported curve");
    }
    return new StepSurfaceOfTranslation(
        instance.id(),
        stringValue(instance, definition, 0),
        profile,
        resolve(referenceId(instance, definition, 2)));
  }

  private StepSurfaceOfProjection resolveSurfaceOfProjection(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_OF_PROJECTION");
    requireParameterCount(instance, definition, 5);
    StepEntity profile = resolve(referenceId(instance, definition, 1));
    if (!isSupportedCurveReference(profile)) {
      throw new UnsupportedStepEntityException(
          "SURFACE_OF_PROJECTION profile must reference a supported curve");
    }
    return new StepSurfaceOfProjection(
        instance.id(),
        stringValue(instance, definition, 0),
        profile,
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  private StepParaboloidSurface resolveParaboloidSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PARABOLOID_SURFACE");
    requireParameterCount(instance, definition, 3);
    return new StepParaboloidSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "PARABOLOID_SURFACE position must reference AXIS2_PLACEMENT_3D"),
        numberValue(instance, definition, 2));
  }

  private StepHyperboloidSurface resolveHyperboloidSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "HYPERBOLOID_SURFACE");
    requireParameterCount(instance, definition, 4);
    return new StepHyperboloidSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "HYPERBOLOID_SURFACE position must reference AXIS2_PLACEMENT_3D"),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  private StepOffsetCurve3D resolveOffsetCurve3D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "OFFSET_CURVE_3D");
    requireParameterCount(instance, definition, 5);
    StepEntity basisCurve = resolve(referenceId(instance, definition, 1));
    if (!isSupportedCurveReference(basisCurve)) {
      throw new UnsupportedStepEntityException(
          "OFFSET_CURVE_3D basis_curve must reference a supported curve");
    }
    return new StepOffsetCurve3D(
        instance.id(),
        stringValue(instance, definition, 0),
        basisCurve,
        numberValue(instance, definition, 2),
        booleanValue(instance, definition, 3),
        requireEntity(
            referenceId(instance, definition, 4),
            StepDirection.class,
            "OFFSET_CURVE_3D ref_direction must reference DIRECTION"));
  }

  private StepOffsetCurve2D resolveOffsetCurve2D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "OFFSET_CURVE_2D");
    requireParameterCount(instance, definition, 4);
    StepEntity basisCurve = resolve(referenceId(instance, definition, 1));
    if (!isSupportedCurveReference(basisCurve)) {
      throw new UnsupportedStepEntityException(
          "OFFSET_CURVE_2D basis_curve must reference a supported curve");
    }
    return new StepOffsetCurve2D(
        instance.id(),
        stringValue(instance, definition, 0),
        basisCurve,
        numberValue(instance, definition, 2),
        booleanValue(instance, definition, 3));
  }

  private StepOrientedCurve resolveOrientedCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ORIENTED_CURVE");
    requireParameterCount(instance, definition, 3);
    int curveElementId = referenceId(instance, definition, 1);
    if (curveElementId == instance.id()) {
      throw new UnsupportedStepEntityException(
          "ORIENTED_CURVE curve_element must not self-reference");
    }
    StepEntity curveElement = resolve(curveElementId);
    if (!isSupportedCurveReference(curveElement)) {
      throw new UnsupportedStepEntityException(
          "ORIENTED_CURVE curve_element must reference a supported curve");
    }
    return new StepOrientedCurve(
        instance.id(),
        stringValue(instance, definition, 0),
        curveElement,
        booleanValue(instance, definition, 2));
  }

  private StepOffsetSurface resolveOffsetSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "OFFSET_SURFACE");
    requireParameterCount(instance, definition, 4);
    StepEntity basisSurface = resolve(referenceId(instance, definition, 1));
    if (!isSupportedSurfaceReference(basisSurface)) {
      throw new UnsupportedStepEntityException(
          "OFFSET_SURFACE basis_surface must reference a supported surface");
    }
    return new StepOffsetSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        basisSurface,
        numberValue(instance, definition, 2),
        booleanValue(instance, definition, 3));
  }

  private StepCompositeCurveSegment resolveCompositeCurveSegment(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COMPOSITE_CURVE_SEGMENT");
    requireParameterCount(instance, definition, 3);
    StepEntity parentCurve = resolve(referenceId(instance, definition, 2));
    if (!isSupportedCurveReference(parentCurve)) {
      throw new UnsupportedStepEntityException(
          "COMPOSITE_CURVE_SEGMENT parent_curve must reference a supported curve");
    }
    return new StepCompositeCurveSegment(
        instance.id(),
        enumValue(instance, definition, 0),
        booleanValue(instance, definition, 1),
        parentCurve);
  }

  private StepCompositeCurve resolveCompositeCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COMPOSITE_CURVE");
    requireParameterCount(instance, definition, 3);
    return new StepCompositeCurve(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepCompositeCurveSegment.class,
            "COMPOSITE_CURVE segments must contain COMPOSITE_CURVE_SEGMENT references"),
        booleanValue(instance, definition, 2));
  }

  private StepCompositeCurveOnSurface resolveCompositeCurveOnSurface(
      StepEntityInstance instance) {
    return resolveCompositeCurveOnSurface(instance, "COMPOSITE_CURVE_ON_SURFACE");
  }

  private StepCompositeCurveOnSurface resolveCompositeCurveOnSurface(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 3);
    return new StepCompositeCurveOnSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepCompositeCurveSegment.class,
            entityName + " segments must contain COMPOSITE_CURVE_SEGMENT references"),
        booleanValue(instance, definition, 2));
  }

  private StepTrimmedCurve resolveTrimmedCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TRIMMED_CURVE");
    requireParameterCount(instance, definition, 6);
    StepEntity basisCurve = resolve(referenceId(instance, definition, 1));
    if (!isSupportedCurveReference(basisCurve)) {
      throw new UnsupportedStepEntityException(
          "TRIMMED_CURVE basis curve must reference a supported curve");
    }
    // Trims can be entity references (e.g., #5) or parameter values (e.g., 0.0).
    // Pass through raw StepValues so the builder can handle both.
    List<StepValue> trim1 = trimValues(definition, 2, "TRIMMED_CURVE trim_1");
    List<StepValue> trim2 = trimValues(definition, 3, "TRIMMED_CURVE trim_2");
    return new StepTrimmedCurve(
        instance.id(),
        stringValue(instance, definition, 0),
        basisCurve,
        trim1,
        trim2,
        booleanValue(instance, definition, 4),
        enumValue(instance, definition, 5));
  }

  private List<StepValue> trimValues(StepEntityDefinition definition, int index, String message) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue listValue)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a list");
    }
    return List.copyOf(listValue.elements());
  }

  private StepSurfaceCurve resolveSurfaceCurve(StepEntityInstance instance) {
    return resolveSurfaceCurve(instance, "SURFACE_CURVE");
  }

  private StepSurfaceCurve resolveSurfaceCurve(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 4);
    StepEntity curve3d = resolve(referenceId(instance, definition, 1));
    if (!isSupportedCurveReference(curve3d)) {
      throw new UnsupportedStepEntityException(
          entityName + " curve_3d must reference a supported curve");
    }
    List<StepEntity> associatedGeometry =
        entityReferenceList(
            instance,
            definition,
            2,
            entityName + " associated_geometry must contain entity references");
    for (StepEntity associated : associatedGeometry) {
      if (!(associated instanceof StepPcurve) && !(associated instanceof StepDegeneratePcurve)) {
        throw new UnsupportedStepEntityException(
            entityName + " associated_geometry currently supports PCURVE or DEGENERATE_PCURVE references");
      }
    }
    return new StepSurfaceCurve(
        instance.id(),
        entityName,
        stringValue(instance, definition, 0),
        curve3d,
        associatedGeometry,
        enumValue(instance, definition, 3));
  }

  private StepSeamCurve resolveSeamCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SEAM_CURVE");
    requireParameterCount(instance, definition, 4);
    StepEntity curve3d = resolve(referenceId(instance, definition, 1));
    if (!isSupportedCurveReference(curve3d)) {
      throw new UnsupportedStepEntityException(
          "SEAM_CURVE curve_3d must reference a supported curve");
    }
    List<StepEntity> associatedGeometry =
        entityReferenceList(
            instance,
            definition,
            2,
            "SEAM_CURVE associated_geometry must contain entity references");
    if (associatedGeometry.size() != 2) {
      throw new UnsupportedStepEntityException(
          "SEAM_CURVE associated_geometry must contain exactly two PCURVE references");
    }
    for (StepEntity associated : associatedGeometry) {
      if (!(associated instanceof StepPcurve) && !(associated instanceof StepDegeneratePcurve)) {
        throw new UnsupportedStepEntityException(
            "SEAM_CURVE associated_geometry currently supports PCURVE or DEGENERATE_PCURVE references");
      }
    }
    return new StepSeamCurve(
        instance.id(),
        stringValue(instance, definition, 0),
        curve3d,
        associatedGeometry,
        enumValue(instance, definition, 3));
  }

  private StepPcurve resolvePcurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PCURVE");
    requireParameterCount(instance, definition, 3);
    StepEntity basisSurface = resolve(referenceId(instance, definition, 1));
    if (!isSupportedSurfaceReference(basisSurface)) {
      throw new UnsupportedStepEntityException(
          "PCURVE basis surface must reference a supported surface");
    }
    StepRepresentation representation =
        requireEntity(
            referenceId(instance, definition, 2),
            StepRepresentation.class,
            "PCURVE reference_to_curve must reference REPRESENTATION");
    if (representation.items().size() != 1) {
      throw new UnsupportedStepEntityException(
          "PCURVE reference_to_curve must contain exactly one 2D curve item");
    }
    StepEntity item = representation.items().getFirst();
    if (!isSupportedCurveReference(item)) {
      throw new UnsupportedStepEntityException(
          "PCURVE reference_to_curve must contain a supported curve item");
    }
    return new StepPcurve(
        instance.id(), stringValue(instance, definition, 0), basisSurface, representation);
  }

  private StepDegeneratePcurve resolveDegeneratePcurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DEGENERATE_PCURVE");
    requireParameterCount(instance, definition, 3);
    StepEntity basisSurface = resolve(referenceId(instance, definition, 1));
    if (!isSupportedSurfaceReference(basisSurface)) {
      throw new UnsupportedStepEntityException(
          "DEGENERATE_PCURVE basis surface must reference a supported surface");
    }
    StepRepresentation representation =
        requireEntity(
            referenceId(instance, definition, 2),
            StepRepresentation.class,
            "DEGENERATE_PCURVE reference_to_curve must reference REPRESENTATION");
    if (representation.items().size() != 1) {
      throw new UnsupportedStepEntityException(
          "DEGENERATE_PCURVE reference_to_curve must contain exactly one 2D curve item");
    }
    StepEntity item = representation.items().getFirst();
    if (!isSupportedCurveReference(item)) {
      throw new UnsupportedStepEntityException(
          "DEGENERATE_PCURVE reference_to_curve must contain a supported curve item");
    }
    return new StepDegeneratePcurve(
        instance.id(), stringValue(instance, definition, 0), basisSurface, representation);
  }

  private StepBSplineCurveWithKnots resolveBSplineCurveWithKnots(StepEntityInstance instance) {
    StepEntityDefinition spline = definition(instance, "B_SPLINE_CURVE_WITH_KNOTS");
    if (instance.hasDefinition("B_SPLINE_CURVE")) {
      requireParameterCount(instance, spline, 3);
      StepEntityDefinition base = definition(instance, "B_SPLINE_CURVE");
      requireParameterCountIn(instance, base, 5, 6);
      boolean hasName = base.parameters().size() == 6;
      return new StepBSplineCurveWithKnots(
          instance.id(),
          hasName ? stringValue(instance, base, 0) : "",
          integerValue(instance, base, hasName ? 1 : 0),
          referenceList(
              instance,
              base,
              hasName ? 2 : 1,
              StepCartesianPoint.class,
              "B_SPLINE_CURVE control points must reference CARTESIAN_POINT"),
          enumValue(instance, base, hasName ? 3 : 2),
          booleanValue(instance, base, hasName ? 4 : 3),
          booleanValue(instance, base, hasName ? 5 : 4),
          integerList(instance, spline, 0),
          numberList(instance, spline, 1),
          enumValue(instance, spline, 2));
    }
    requireParameterCount(instance, spline, 9);
    return new StepBSplineCurveWithKnots(
        instance.id(),
        stringValue(instance, spline, 0),
        integerValue(instance, spline, 1),
        referenceList(
            instance,
            spline,
            2,
            StepCartesianPoint.class,
            "B_SPLINE_CURVE control points must reference CARTESIAN_POINT"),
        enumValue(instance, spline, 3),
        booleanValue(instance, spline, 4),
        booleanValue(instance, spline, 5),
        integerList(instance, spline, 6),
        numberList(instance, spline, 7),
        enumValue(instance, spline, 8));
  }

  private StepBSplineCurve resolveBSplineCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "B_SPLINE_CURVE");
    requireParameterCountIn(instance, definition, 5, 6);
    boolean hasName = definition.parameters().size() == 6;
    return new StepBSplineCurve(
        instance.id(),
        hasName ? stringValue(instance, definition, 0) : "",
        integerValue(instance, definition, hasName ? 1 : 0),
        referenceList(
            instance,
            definition,
            hasName ? 2 : 1,
            StepCartesianPoint.class,
            "B_SPLINE_CURVE control points must reference CARTESIAN_POINT"),
        enumValue(instance, definition, hasName ? 3 : 2),
        booleanValue(instance, definition, hasName ? 4 : 3),
        booleanValue(instance, definition, hasName ? 5 : 4));
  }

  private StepRationalBSplineCurve resolveRationalBSplineCurve(StepEntityInstance instance) {
    StepEntityDefinition rational = definition(instance, "RATIONAL_B_SPLINE_CURVE");
    requireParameterCount(instance, rational, 1);
    StepEntityDefinition base = definition(instance, "B_SPLINE_CURVE");
    requireParameterCountIn(instance, base, 5, 6);
    boolean hasName = base.parameters().size() == 6;
    List<Integer> knotMultiplicities = List.of();
    List<Double> knots = List.of();
    String knotSpec = "";
    if (instance.hasDefinition("B_SPLINE_CURVE_WITH_KNOTS")) {
      StepEntityDefinition knotDefinition = definition(instance, "B_SPLINE_CURVE_WITH_KNOTS");
      requireParameterCount(instance, knotDefinition, 3);
      knotMultiplicities = integerList(instance, knotDefinition, 0);
      knots = numberList(instance, knotDefinition, 1);
      knotSpec = enumValue(instance, knotDefinition, 2);
    }
    return new StepRationalBSplineCurve(
        instance.id(),
        hasName ? stringValue(instance, base, 0) : "",
        integerValue(instance, base, hasName ? 1 : 0),
        referenceList(
            instance,
            base,
            hasName ? 2 : 1,
            StepCartesianPoint.class,
            "B_SPLINE_CURVE control points must reference CARTESIAN_POINT"),
        enumValue(instance, base, hasName ? 3 : 2),
        booleanValue(instance, base, hasName ? 4 : 3),
        booleanValue(instance, base, hasName ? 5 : 4),
        numberList(instance, rational, 0),
        knotMultiplicities,
        knots,
        knotSpec);
  }

  private StepBSplineSurfaceWithKnots resolveBSplineSurfaceWithKnots(StepEntityInstance instance) {
    StepEntityDefinition knots = definition(instance, "B_SPLINE_SURFACE_WITH_KNOTS");
    if (instance.hasDefinition("B_SPLINE_SURFACE")) {
      requireParameterCount(instance, knots, 5);
      StepEntityDefinition base = definition(instance, "B_SPLINE_SURFACE");
      requireParameterCount(instance, base, 7);
      return new StepBSplineSurfaceWithKnots(
          instance.id(),
          "",
          integerValue(instance, base, 0),
          integerValue(instance, base, 1),
          referenceGrid(
              instance,
              base,
              2,
              StepCartesianPoint.class,
              "B_SPLINE_SURFACE control points must reference CARTESIAN_POINT"),
          enumValue(instance, base, 3),
          booleanValue(instance, base, 4),
          booleanValue(instance, base, 5),
          booleanValue(instance, base, 6),
          integerList(instance, knots, 0),
          integerList(instance, knots, 1),
          numberList(instance, knots, 2),
          numberList(instance, knots, 3),
          enumValue(instance, knots, 4));
    }
    requireParameterCount(instance, knots, 13);
    return new StepBSplineSurfaceWithKnots(
        instance.id(),
        stringValue(instance, knots, 0),
        integerValue(instance, knots, 1),
        integerValue(instance, knots, 2),
        referenceGrid(
            instance,
            knots,
            3,
            StepCartesianPoint.class,
            "B_SPLINE_SURFACE control points must reference CARTESIAN_POINT"),
        enumValue(instance, knots, 4),
        booleanValue(instance, knots, 5),
        booleanValue(instance, knots, 6),
        booleanValue(instance, knots, 7),
        integerList(instance, knots, 8),
        integerList(instance, knots, 9),
        numberList(instance, knots, 10),
        numberList(instance, knots, 11),
        enumValue(instance, knots, 12));
  }

  private StepBSplineSurface resolveBSplineSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "B_SPLINE_SURFACE");
    requireParameterCount(instance, definition, 7);
    return new StepBSplineSurface(
        instance.id(),
        "",
        integerValue(instance, definition, 0),
        integerValue(instance, definition, 1),
        referenceGrid(
            instance,
            definition,
            2,
            StepCartesianPoint.class,
            "B_SPLINE_SURFACE control points must reference CARTESIAN_POINT"),
        enumValue(instance, definition, 3),
        booleanValue(instance, definition, 4),
        booleanValue(instance, definition, 5),
        booleanValue(instance, definition, 6));
  }

  private StepBSplineCurveWithKnotsAndBreakpoints resolveBSplineCurveWithKnotsAndBreakpoints(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS");
    if (instance.hasDefinition("B_SPLINE_CURVE_WITH_KNOTS")) {
      requireParameterCount(instance, definition, 1);
      StepEntityDefinition knots = definition(instance, "B_SPLINE_CURVE_WITH_KNOTS");
      requireParameterCount(instance, knots, 3);
      StepEntityDefinition base = definition(instance, "B_SPLINE_CURVE");
      requireParameterCountIn(instance, base, 5, 6);
      boolean hasName = base.parameters().size() == 6;
      return new StepBSplineCurveWithKnotsAndBreakpoints(
          instance.id(),
          hasName ? stringValue(instance, base, 0) : "",
          integerValue(instance, base, hasName ? 1 : 0),
          referenceList(
              instance,
              base,
              hasName ? 2 : 1,
              StepCartesianPoint.class,
              "B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS control points must reference CARTESIAN_POINT"),
          integerList(instance, knots, 0),
          numberList(instance, knots, 1),
          numberList(instance, definition, 0),
          enumValue(instance, base, hasName ? 3 : 2),
          booleanValue(instance, base, hasName ? 4 : 3),
          booleanValue(instance, base, hasName ? 5 : 4));
    }
    // Handle case without B_SPLINE_CURVE_WITH_KNOTS supertype
    requireParameterCount(instance, definition, 4);
    StepEntityDefinition base = definition(instance, "B_SPLINE_CURVE");
    requireParameterCountIn(instance, base, 5, 6);
    boolean hasName = base.parameters().size() == 6;
    return new StepBSplineCurveWithKnotsAndBreakpoints(
        instance.id(),
        hasName ? stringValue(instance, base, 0) : "",
        integerValue(instance, base, hasName ? 1 : 0),
        referenceList(
            instance,
            base,
            hasName ? 2 : 1,
            StepCartesianPoint.class,
            "B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS control points must reference CARTESIAN_POINT"),
        integerList(instance, definition, 0),
        numberList(instance, definition, 1),
        numberList(instance, definition, 2),
        enumValue(instance, base, hasName ? 3 : 2),
        booleanValue(instance, base, hasName ? 4 : 3),
        booleanValue(instance, base, hasName ? 5 : 4));
  }

  private StepBSplineSurfaceWithKnotsAndBreakpoints resolveBSplineSurfaceWithKnotsAndBreakpoints(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "B_SPLINE_SURFACE_WITH_KNOTS_AND_BREAKPOINTS");
    if (instance.hasDefinition("B_SPLINE_SURFACE_WITH_KNOTS")) {
      requireParameterCount(instance, definition, 2);
      StepEntityDefinition knots = definition(instance, "B_SPLINE_SURFACE_WITH_KNOTS");
      requireParameterCount(instance, knots, 5);
      StepEntityDefinition base = definition(instance, "B_SPLINE_SURFACE");
      requireParameterCount(instance, base, 7);
      return new StepBSplineSurfaceWithKnotsAndBreakpoints(
          instance.id(),
          "",
          integerValue(instance, base, 0),
          integerValue(instance, base, 1),
          referenceGrid(
              instance,
              base,
              2,
              StepCartesianPoint.class,
              "B_SPLINE_SURFACE_WITH_KNOTS_AND_BREAKPOINTS control points must reference CARTESIAN_POINT"),
          integerList(instance, knots, 0),
          integerList(instance, knots, 1),
          numberList(instance, knots, 2),
          numberList(instance, knots, 3),
          numberList(instance, definition, 0),
          numberList(instance, definition, 1),
          enumValue(instance, base, 3),
          booleanValue(instance, base, 4),
          booleanValue(instance, base, 5),
          booleanValue(instance, base, 6));
    }
    // Handle case without B_SPLINE_SURFACE_WITH_KNOTS supertype
    requireParameterCount(instance, definition, 7);
    StepEntityDefinition base = definition(instance, "B_SPLINE_SURFACE");
    requireParameterCount(instance, base, 7);
    return new StepBSplineSurfaceWithKnotsAndBreakpoints(
        instance.id(),
        "",
        integerValue(instance, base, 0),
        integerValue(instance, base, 1),
        referenceGrid(
            instance,
            base,
            2,
            StepCartesianPoint.class,
            "B_SPLINE_SURFACE_WITH_KNOTS_AND_BREAKPOINTS control points must reference CARTESIAN_POINT"),
        integerList(instance, definition, 0),
        integerList(instance, definition, 1),
        numberList(instance, definition, 2),
        numberList(instance, definition, 3),
        numberList(instance, definition, 4),
        numberList(instance, definition, 5),
        enumValue(instance, base, 3),
        booleanValue(instance, base, 4),
        booleanValue(instance, base, 5),
        booleanValue(instance, base, 6));
  }

  private StepRationalBSplineSurface resolveRationalBSplineSurface(StepEntityInstance instance) {
    StepEntityDefinition rational = definition(instance, "RATIONAL_B_SPLINE_SURFACE");
    requireParameterCount(instance, rational, 1);
    StepEntityDefinition base = definition(instance, "B_SPLINE_SURFACE");
    requireParameterCount(instance, base, 7);
    List<Integer> uMultiplicities = List.of();
    List<Integer> vMultiplicities = List.of();
    List<Double> uKnots = List.of();
    List<Double> vKnots = List.of();
    String knotSpec = "";
    if (instance.hasDefinition("B_SPLINE_SURFACE_WITH_KNOTS")) {
      StepEntityDefinition knotDefinition = definition(instance, "B_SPLINE_SURFACE_WITH_KNOTS");
      requireParameterCount(instance, knotDefinition, 5);
      uMultiplicities = integerList(instance, knotDefinition, 0);
      vMultiplicities = integerList(instance, knotDefinition, 1);
      uKnots = numberList(instance, knotDefinition, 2);
      vKnots = numberList(instance, knotDefinition, 3);
      knotSpec = enumValue(instance, knotDefinition, 4);
    }
    return new StepRationalBSplineSurface(
        instance.id(),
        "",
        integerValue(instance, base, 0),
        integerValue(instance, base, 1),
        referenceGrid(
            instance,
            base,
            2,
            StepCartesianPoint.class,
            "B_SPLINE_SURFACE control points must reference CARTESIAN_POINT"),
        enumValue(instance, base, 3),
        booleanValue(instance, base, 4),
        booleanValue(instance, base, 5),
        booleanValue(instance, base, 6),
        numberGrid(instance, rational, 0),
        uMultiplicities,
        vMultiplicities,
        uKnots,
        vKnots,
        knotSpec);
  }

  private StepVertexPoint resolveVertexPoint(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "VERTEX_POINT");
    requireParameterCount(instance, definition, 2);
    return new StepVertexPoint(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepCartesianPoint.class,
            "VERTEX_POINT geometry must reference CARTESIAN_POINT"));
  }

  private StepEdgeCurve resolveEdgeCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "EDGE_CURVE");
    requireParameterCount(instance, definition, 5);
    StepEntity edgeGeometry = resolve(referenceId(instance, definition, 3));
    if (!isSupportedCurveReference(edgeGeometry)) {
      throw new UnsupportedStepEntityException(
          "EDGE_CURVE geometry must reference a supported curve");
    }
    return new StepEdgeCurve(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepVertexPoint.class,
            "EDGE_CURVE start must reference VERTEX_POINT"),
        requireEntity(
            referenceId(instance, definition, 2),
            StepVertexPoint.class,
            "EDGE_CURVE end must reference VERTEX_POINT"),
        edgeGeometry,
        booleanValue(instance, definition, 4));
  }

  private StepOrientedEdge resolveOrientedEdge(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ORIENTED_EDGE");
    requireParameterCount(instance, definition, 5);
    StepEdgeCurve edgeElement =
        requireEntity(
            referenceId(instance, definition, 3),
            StepEdgeCurve.class,
            "ORIENTED_EDGE edge_element must reference EDGE_CURVE");
    boolean orientation = booleanValue(instance, definition, 4);
    if (!isUnset(definition.parameters().get(1)) || !isUnset(definition.parameters().get(2))) {
      StepVertexPoint explicitStart =
          requireEntity(
              referenceId(instance, definition, 1),
              StepVertexPoint.class,
              "ORIENTED_EDGE edge_start must reference VERTEX_POINT");
      StepVertexPoint explicitEnd =
          requireEntity(
              referenceId(instance, definition, 2),
              StepVertexPoint.class,
              "ORIENTED_EDGE edge_end must reference VERTEX_POINT");
      StepVertexPoint expectedStart = orientation ? edgeElement.start() : edgeElement.end();
      StepVertexPoint expectedEnd = orientation ? edgeElement.end() : edgeElement.start();
      if (explicitStart.id() != expectedStart.id() || explicitEnd.id() != expectedEnd.id()) {
        throw new StepResolutionException(
            "ORIENTED_EDGE explicit edge_start/edge_end must match edge_element orientation");
      }
    }
    return new StepOrientedEdge(
        instance.id(),
        stringValue(instance, definition, 0),
        edgeElement,
        orientation);
  }

  private StepSubedge resolveSubedge(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SUBEDGE");
    requireParameterCount(instance, definition, 4);
    StepEntity parentEdge = resolve(referenceId(instance, definition, 3));
    if (!(parentEdge instanceof StepEdgeCurve) && !(parentEdge instanceof StepSubedge)) {
      throw new UnsupportedStepEntityException(
          "SUBEDGE parent_edge must reference EDGE_CURVE or SUBEDGE");
    }
    if (parentEdge.id() == instance.id()) {
      throw new UnsupportedStepEntityException("SUBEDGE parent_edge must not self-reference");
    }
    return new StepSubedge(
        instance.id(),
        stringValue(instance, definition, 0),
        requireVertexLike(
            referenceId(instance, definition, 1),
            "SUBEDGE edge_start must reference VERTEX or VERTEX_POINT"),
        requireVertexLike(
            referenceId(instance, definition, 2),
            "SUBEDGE edge_end must reference VERTEX or VERTEX_POINT"),
        parentEdge);
  }

  private StepConnectedEdgeSet resolveConnectedEdgeSet(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONNECTED_EDGE_SET");
    requireParameterCount(instance, definition, 2);
    List<StepEntity> edges = entityReferenceList(
        instance,
        definition,
        1,
        "CONNECTED_EDGE_SET edges must contain edge references");
    for (StepEntity edge : edges) {
      if (!(edge instanceof StepEdgeCurve)
          && !(edge instanceof StepOrientedEdge)
          && !(edge instanceof StepSubedge)
          && !(edge instanceof StepEdge)) {
        throw new UnsupportedStepEntityException(
            "CONNECTED_EDGE_SET edges must reference EDGE subtypes");
      }
    }
    return new StepConnectedEdgeSet(instance.id(), stringValue(instance, definition, 0), edges);
  }

  private StepEdgeBasedWireframeModel resolveEdgeBasedWireframeModel(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "EDGE_BASED_WIREFRAME_MODEL");
    requireParameterCount(instance, definition, 2);
    return new StepEdgeBasedWireframeModel(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepConnectedEdgeSet.class,
            "EDGE_BASED_WIREFRAME_MODEL ebwm_boundary must contain CONNECTED_EDGE_SET references"));
  }

  private StepEdgeLoop resolveEdgeLoop(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "EDGE_LOOP");
    requireParameterCount(instance, definition, 2);
    List<StepOrientedEdge> edges =
        referenceList(
            instance,
            definition,
            1,
            StepOrientedEdge.class,
            "EDGE_LOOP edge list must contain ORIENTED_EDGE references");
    return new StepEdgeLoop(instance.id(), stringValue(instance, definition, 0), edges);
  }

  private StepPath resolvePath(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PATH");
    requireParameterCount(instance, definition, 2);
    List<StepOrientedEdge> edges =
        referenceList(
            instance,
            definition,
            1,
            StepOrientedEdge.class,
            "PATH edge list must contain ORIENTED_EDGE references");
    return new StepPath(instance.id(), stringValue(instance, definition, 0), edges);
  }

  private StepOpenPath resolveOpenPath(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "OPEN_PATH");
    requireParameterCount(instance, definition, 2);
    List<StepOrientedEdge> edges =
        referenceList(
            instance,
            definition,
            1,
            StepOrientedEdge.class,
            "OPEN_PATH edge list must contain ORIENTED_EDGE references");
    if (!edges.isEmpty()) {
      StepOrientedEdge first = edges.getFirst();
      StepOrientedEdge last = edges.getLast();
      if (orientedEdgeStartId(first) == orientedEdgeEndId(last)) {
        throw new UnsupportedStepEntityException(
            "OPEN_PATH start vertex must differ from end vertex");
      }
    }
    return new StepOpenPath(instance.id(), stringValue(instance, definition, 0), edges);
  }

  private StepSubpath resolveSubpath(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SUBPATH");
    requireParameterCount(instance, definition, 3);
    List<StepOrientedEdge> edges =
        referenceList(
            instance,
            definition,
            1,
            StepOrientedEdge.class,
            "SUBPATH edge list must contain ORIENTED_EDGE references");
    StepEntity parentPath = resolve(referenceId(instance, definition, 2));
    if (!isPathEntity(parentPath)) {
      throw new UnsupportedStepEntityException(
          "SUBPATH parent_path must reference PATH, OPEN_PATH, SUBPATH, ORIENTED_PATH or EDGE_LOOP");
    }
    if (parentPath.id() == instance.id()) {
      throw new UnsupportedStepEntityException("SUBPATH parent_path must not reference itself");
    }
    return new StepSubpath(instance.id(), stringValue(instance, definition, 0), edges, parentPath);
  }

  private StepOrientedPath resolveOrientedPath(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ORIENTED_PATH");
    requireParameterCountIn(instance, definition, 2, 3);
    boolean hasStandaloneName = definition.parameters().size() == 3;
    String name =
        hasStandaloneName
            ? stringValue(instance, definition, 0)
            : instance.hasDefinition("PATH") ? stringValue(instance, definition(instance, "PATH"), 0) : "";
    int pathIndex = hasStandaloneName ? 1 : 0;
    StepEntity pathElement = resolve(referenceId(instance, definition, pathIndex));
    if (!isPathEntity(pathElement)) {
      throw new UnsupportedStepEntityException(
          "ORIENTED_PATH path_element must reference PATH, OPEN_PATH, SUBPATH, ORIENTED_PATH or EDGE_LOOP");
    }
    boolean orientation = booleanValue(instance, definition, pathIndex + 1);
    List<StepOrientedEdge> sourceEdges = pathEdges(pathElement);
    List<StepOrientedEdge> edges =
        orientation ? sourceEdges : sourceEdges.reversed();
    return new StepOrientedPath(instance.id(), name, pathElement, orientation, edges);
  }

  private static int orientedEdgeStartId(StepOrientedEdge edge) {
    return edge.orientation() ? edge.edgeElement().start().id() : edge.edgeElement().end().id();
  }

  private static int orientedEdgeEndId(StepOrientedEdge edge) {
    return edge.orientation() ? edge.edgeElement().end().id() : edge.edgeElement().start().id();
  }

  private static boolean isPathEntity(StepEntity entity) {
    return entity instanceof StepPath
        || entity instanceof StepOpenPath
        || entity instanceof StepSubpath
        || entity instanceof StepOrientedPath
        || entity instanceof StepEdgeLoop;
  }

  private static List<StepOrientedEdge> pathEdges(StepEntity entity) {
    return switch (entity) {
      case StepPath path -> path.edges();
      case StepOpenPath openPath -> openPath.edges();
      case StepSubpath subpath -> subpath.edges();
      case StepOrientedPath orientedPath -> orientedPath.edges();
      case StepEdgeLoop edgeLoop -> edgeLoop.edges();
      default -> throw new UnsupportedStepEntityException("entity is not a supported path");
    };
  }

  private StepVertexLoop resolveVertexLoop(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "VERTEX_LOOP");
    requireParameterCount(instance, definition, 2);
    return new StepVertexLoop(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepVertexPoint.class,
            "VERTEX_LOOP loop_vertex must reference VERTEX_POINT"));
  }

  private StepPolyLoop resolvePolyLoop(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "POLY_LOOP");
    requireParameterCount(instance, definition, 2);
    List<StepCartesianPoint> polygon =
        referenceList(
            instance,
            definition,
            1,
            StepCartesianPoint.class,
            "POLY_LOOP polygon must contain CARTESIAN_POINT references");
    if (polygon.size() < 3) {
      throw new UnsupportedStepEntityException("POLY_LOOP requires at least 3 points");
    }
    return new StepPolyLoop(instance.id(), stringValue(instance, definition, 0), polygon);
  }

  private StepFaceBound resolveFaceBound(StepEntityInstance instance, boolean outer) {
    StepEntityDefinition definition =
        definition(instance, outer ? "FACE_OUTER_BOUND" : "FACE_BOUND");
    requireParameterCount(instance, definition, 3);
    return new StepFaceBound(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepLoop.class,
            "FACE_BOUND loop must reference LOOP subtype"),
        booleanValue(instance, definition, 2),
        outer);
  }

  private StepAdvancedFace resolveAdvancedFace(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ADVANCED_FACE");
    requireParameterCount(instance, definition, 4);
    StepEntity faceGeometry = resolve(referenceId(instance, definition, 2));
    if (!isSupportedSurfaceReference(faceGeometry)) {
      throw new UnsupportedStepEntityException(
          "ADVANCED_FACE geometry must reference a supported surface but got "
              + faceGeometry.getClass().getSimpleName());
    }
    return new StepAdvancedFace(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepFaceBound.class,
            "ADVANCED_FACE bounds must contain FACE_BOUND references"),
        faceGeometry,
        booleanValue(instance, definition, 3));
  }

  private StepFaceSurface resolveFaceSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FACE_SURFACE");
    requireParameterCount(instance, definition, 4);
    StepEntity faceGeometry = resolve(referenceId(instance, definition, 2));
    if (!isSupportedSurfaceReference(faceGeometry)) {
      throw new UnsupportedStepEntityException(
          "FACE_SURFACE geometry must reference a supported surface but got "
              + faceGeometry.getClass().getSimpleName());
    }
    return new StepFaceSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepFaceBound.class,
            "FACE_SURFACE bounds must contain FACE_BOUND references"),
        faceGeometry,
        booleanValue(instance, definition, 3));
  }

  private StepOrientedFace resolveOrientedFace(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ORIENTED_FACE");
    requireParameterCount(instance, definition, 3);
    StepFaceEntity faceElement =
        requireEntity(
            referenceId(instance, definition, 1),
            StepFaceEntity.class,
            "ORIENTED_FACE face_element must reference FACE subtype");
    return new StepOrientedFace(
        instance.id(),
        stringValue(instance, definition, 0),
        faceElement,
        booleanValue(instance, definition, 2));
  }

  private StepOpenShell resolveOpenShell(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "OPEN_SHELL");
    requireParameterCount(instance, definition, 2);
    return new StepOpenShell(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepFaceEntity.class,
            "OPEN_SHELL faces must contain FACE subtype references"));
  }

  private StepClosedShell resolveClosedShell(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CLOSED_SHELL");
    requireParameterCount(instance, definition, 2);
    return new StepClosedShell(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepFaceEntity.class,
            "CLOSED_SHELL faces must contain FACE subtype references"));
  }

  private StepSurfacedOpenShell resolveSurfacedOpenShell(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACED_OPEN_SHELL");
    requireParameterCount(instance, definition, 2);
    List<StepFaceEntity> faces =
        referenceList(
            instance,
            definition,
            1,
            StepFaceEntity.class,
            "SURFACED_OPEN_SHELL faces must contain FACE subtype references");
    for (StepFaceEntity face : faces) {
      if (!(face instanceof StepFaceSurface)) {
        throw new StepResolutionException(
            "SURFACED_OPEN_SHELL faces must reference FACE_SURFACE or subtype");
      }
    }
    return new StepSurfacedOpenShell(instance.id(), stringValue(instance, definition, 0), faces);
  }

  private StepOrientedOpenShell resolveOrientedOpenShell(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ORIENTED_OPEN_SHELL");
    requireParameterCount(instance, definition, 3);
    StepEntity openShellElement = resolve(referenceId(instance, definition, 1));
    if (!(openShellElement instanceof StepOpenShell)
        && !(openShellElement instanceof StepSurfacedOpenShell)
        && !(openShellElement instanceof StepOrientedOpenShell)) {
      throw new StepResolutionException(
          "ORIENTED_OPEN_SHELL open_shell_element must reference OPEN_SHELL");
    }
    return new StepOrientedOpenShell(
        instance.id(),
        stringValue(instance, definition, 0),
        openShellElement,
        booleanValue(instance, definition, 2));
  }

  private StepOrientedClosedShell resolveOrientedClosedShell(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ORIENTED_CLOSED_SHELL");
    requireParameterCount(instance, definition, 3);
    StepEntity closedShellElement = resolve(referenceId(instance, definition, 1));
    if (!(closedShellElement instanceof StepClosedShell)
        && !(closedShellElement instanceof StepOrientedClosedShell)) {
      throw new StepResolutionException(
          "ORIENTED_CLOSED_SHELL closed_shell_element must reference CLOSED_SHELL");
    }
    return new StepOrientedClosedShell(
        instance.id(),
        stringValue(instance, definition, 0),
        closedShellElement,
        booleanValue(instance, definition, 2));
  }

  private StepConnectedFaceSet resolveConnectedFaceSet(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONNECTED_FACE_SET");
    requireParameterCount(instance, definition, 2);
    return new StepConnectedFaceSet(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepFaceEntity.class,
            "CONNECTED_FACE_SET cfs_faces must contain FACE subtype references"));
  }

  private StepConnectedFaceSubSet resolveConnectedFaceSubSet(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONNECTED_FACE_SUB_SET");
    requireParameterCount(instance, definition, 3);
    StepEntity parentFaceSet = resolve(referenceId(instance, definition, 2));
    if (!isConnectedFaceSetEntity(parentFaceSet)) {
      throw new StepResolutionException(
          "CONNECTED_FACE_SUB_SET parent_face_set must reference CONNECTED_FACE_SET or CONNECTED_FACE_SUB_SET");
    }
    if (parentFaceSet.id() == instance.id()) {
      throw new StepResolutionException(
          "CONNECTED_FACE_SUB_SET parent_face_set cannot reference itself");
    }
    return new StepConnectedFaceSubSet(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepFaceEntity.class,
            "CONNECTED_FACE_SUB_SET cfs_faces must contain FACE subtype references"),
        parentFaceSet);
  }

  private StepVertexShell resolveVertexShell(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "VERTEX_SHELL");
    requireParameterCount(instance, definition, 2);
    return new StepVertexShell(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepVertexLoop.class,
            "VERTEX_SHELL vertex_shell_extent must reference VERTEX_LOOP"));
  }

  private StepWireShell resolveWireShell(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "WIRE_SHELL");
    requireParameterCount(instance, definition, 2);
    List<StepLoop> loops =
        referenceList(
            instance,
            definition,
            1,
            StepLoop.class,
            "WIRE_SHELL wire_shell_extent must contain LOOP references");
    return new StepWireShell(instance.id(), stringValue(instance, definition, 0), loops);
  }

  private StepManifoldSolidBrep resolveManifoldSolidBrep(StepEntityInstance instance) {
    return resolveManifoldSolidBrep(instance, "MANIFOLD_SOLID_BREP");
  }

  private StepManifoldSolidBrep resolveManifoldSolidBrep(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 2);
    StepEntity outer = resolve(referenceId(instance, definition, 1));
    if (!isClosedShellEntity(outer)) {
      throw new StepResolutionException(entityName + " outer must reference CLOSED_SHELL");
    }
    return new StepManifoldSolidBrep(
        instance.id(),
        stringValue(instance, definition, 0),
        outer);
  }

  private StepNonManifoldSolidBrep resolveNonManifoldSolidBrep(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "NON_MANIFOLD_SOLID_BREP");
    requireParameterCount(instance, definition, 2);
    StepEntity outer = resolve(referenceId(instance, definition, 1));
    if (!(outer instanceof StepClosedShell) && !(outer instanceof StepOpenShell)) {
      throw new StepResolutionException(
          "NON_MANIFOLD_SOLID_BREP outer must reference CLOSED_SHELL or OPEN_SHELL");
    }
    return new StepNonManifoldSolidBrep(
        instance.id(),
        stringValue(instance, definition, 0),
        outer);
  }

  private StepFacettedBrep resolveFacettedBrep(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FACETTED_BREP");
    requireParameterCount(instance, definition, 2);
    StepEntity outer = resolve(referenceId(instance, definition, 1));
    if (!isClosedShellEntity(outer) && !isOpenShellEntity(outer)) {
      throw new StepResolutionException(
          "FACETTED_BREP outer must reference CLOSED_SHELL or OPEN_SHELL");
    }
    return new StepFacettedBrep(
        instance.id(),
        stringValue(instance, definition, 0),
        outer);
  }

  private StepShellBasedSurfaceModel resolveShellBasedSurfaceModel(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SHELL_BASED_SURFACE_MODEL");
    requireParameterCount(instance, definition, 2);
    List<StepEntity> shells =
        entityReferenceList(
            instance,
            definition,
            1,
            "SHELL_BASED_SURFACE_MODEL shells must contain shell references");
    for (StepEntity shell : shells) {
      if (!isShellEntity(shell)) {
        throw new StepResolutionException(
            "SHELL_BASED_SURFACE_MODEL shells must reference OPEN_SHELL, ORIENTED_OPEN_SHELL, CLOSED_SHELL or ORIENTED_CLOSED_SHELL");
      }
    }
    return new StepShellBasedSurfaceModel(
        instance.id(), stringValue(instance, definition, 0), shells);
  }

  private StepFaceBasedSurfaceModel resolveFaceBasedSurfaceModel(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FACE_BASED_SURFACE_MODEL");
    requireParameterCount(instance, definition, 2);
    List<StepEntity> faceSets =
        entityReferenceList(
            instance,
            definition,
            1,
            "FACE_BASED_SURFACE_MODEL fbsm_faces must contain connected face sets");
    for (StepEntity faceSet : faceSets) {
      if (!isConnectedFaceSetEntity(faceSet)
          && !isShellEntity(faceSet)) {
        throw new StepResolutionException(
            "FACE_BASED_SURFACE_MODEL fbsm_faces must reference CONNECTED_FACE_SET, CONNECTED_FACE_SUB_SET, OPEN_SHELL, ORIENTED_OPEN_SHELL, CLOSED_SHELL or ORIENTED_CLOSED_SHELL");
      }
    }
    return new StepFaceBasedSurfaceModel(
        instance.id(), stringValue(instance, definition, 0), faceSets);
  }

  private StepManifoldSurfaceModel resolveManifoldSurfaceModel(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MANIFOLD_SURFACE_MODEL");
    requireParameterCount(instance, definition, 2);
    List<StepEntity> shells =
        entityReferenceList(
            instance,
            definition,
            1,
            "MANIFOLD_SURFACE_MODEL shells must contain shell references");
    return new StepManifoldSurfaceModel(
        instance.id(), stringValue(instance, definition, 0), shells);
  }

  private StepSurfacedEdgeCurve resolveSurfacedEdgeCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACED_EDGE_CURVE");
    requireParameterCount(instance, definition, 5);
    return new StepSurfacedEdgeCurve(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        booleanValue(instance, definition, 4));
  }

  private boolean isConnectedFaceSetEntity(StepEntity entity) {
    return entity instanceof StepConnectedFaceSet || entity instanceof StepConnectedFaceSubSet;
  }

  private StepGeometricTolerance resolveGeometricTolerance(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GEOMETRIC_TOLERANCE");
    requireParameterCount(instance, definition, 3);
    return new StepGeometricTolerance(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepToleranceZoneForm resolveToleranceZoneForm(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TOLERANCE_ZONE_FORM");
    requireParameterCount(instance, definition, 2);
    return new StepToleranceZoneForm(
        instance.id(),
        stringValue(instance, definition, 0),
        enumValue(instance, definition, 1));
  }

  private StepToleranceZone resolveToleranceZone(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TOLERANCE_ZONE");
    requireParameterCount(instance, definition, 2);
    return new StepToleranceZone(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  private StepConfigurationItem resolveConfigurationItem(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONFIGURATION_ITEM");
    requireParameterCount(instance, definition, 4);
    return new StepConfigurationItem(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        enumValue(instance, definition, 3));
  }

  private StepDirectedDimensionalSize resolveDirectedDimensionalSize(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DIRECTED_DIMENSIONAL_SIZE");
    requireParameterCount(instance, definition, 3);
    return new StepDirectedDimensionalSize(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepGeometricTolerance resolveGeometricTolerance(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 3);
    return new StepGeometricTolerance(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepConfigurationEffectivity resolveConfigurationEffectivity(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONFIGURATION_EFFECTIVITY");
    requireParameterCount(instance, definition, 3);
    return new StepConfigurationEffectivity(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepFeatureControlFrame resolveFeatureControlFrame(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEATURE_CONTROL_FRAME");
    requireParameterCount(instance, definition, 3);
    List<StepEntity> datumSystem =
        entityReferenceList(
            instance, definition, 1,
            "FEATURE_CONTROL_FRAME datum_system must contain entity references");
    return new StepFeatureControlFrame(
        instance.id(),
        stringValue(instance, definition, 0),
        datumSystem,
        resolve(referenceId(instance, definition, 2)));
  }

  private StepRunoutToleranceZone resolveRunoutToleranceZone(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "RUNOUT_TOLERANCE_ZONE");
    requireParameterCount(instance, definition, 2);
    return new StepRunoutToleranceZone(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  // Phase 3: GD&T extended tolerance resolve methods

  private StepGeometricToleranceWithDefinedAreaUnit resolveGeometricToleranceWithDefinedAreaUnit(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GEOMETRIC_TOLERANCE_WITH_DEFINED_AREA_UNIT");
    requireParameterCount(instance, definition, 7);
    return new StepGeometricToleranceWithDefinedAreaUnit(
        instance.id(),
        stringValue(instance, definition, 0),
        "GEOMETRIC_TOLERANCE_WITH_DEFINED_AREA_UNIT",
        numberValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        resolve(referenceId(instance, definition, 4)));
  }

  private StepGeometricToleranceWithMaximumTolerance resolveGeometricToleranceWithMaximumTolerance(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GEOMETRIC_TOLERANCE_WITH_MAXIMUM_TOLERANCE");
    requireParameterCount(instance, definition, 7);
    return new StepGeometricToleranceWithMaximumTolerance(
        instance.id(),
        stringValue(instance, definition, 0),
        "GEOMETRIC_TOLERANCE_WITH_MAXIMUM_TOLERANCE",
        numberValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        numberValue(instance, definition, 4));
  }

  private StepNonUniformZoneDefinition resolveNonUniformZoneDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "NON_UNIFORM_ZONE_DEFINITION");
    requireParameterCount(instance, definition, 5);
    return new StepNonUniformZoneDefinition(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        numberValue(instance, definition, 3));
  }

  private StepDatumReferenceModifierWithValue resolveDatumReferenceModifierWithValue(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DATUM_REFERENCE_MODIFIER_WITH_VALUE");
    requireParameterCount(instance, definition, 6);
    return new StepDatumReferenceModifierWithValue(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        numberValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)),
        resolve(referenceId(instance, definition, 4)));
  }

  private StepRunoutZoneDefinitionOrientation resolveRunoutZoneDefinitionOrientation(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "RUNOUT_ZONE_DEFINITION_ORIENTATION");
    requireParameterCount(instance, definition, 5);
    return new StepRunoutZoneDefinitionOrientation(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        numberValue(instance, definition, 3));
  }

  private StepDatumReferenceModifier resolveDatumReferenceModifier(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DATUM_REFERENCE_MODIFIER");
    requireParameterCount(instance, definition, 4);
    return new StepDatumReferenceModifier(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  // Phase 4: Tessellated triangulated resolve methods

  private StepTriangulatedFace resolveTriangulatedFace(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TRIANGULATED_FACE");
    requireParameterCount(instance, definition, 4);
    return new StepTriangulatedFace(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "TRIANGULATED_FACE vertices must contain entity references"),
        integerList(instance, definition, 2));
  }

  private StepComplexTriangulatedFace resolveComplexTriangulatedFace(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COMPLEX_TRIANGULATED_FACE");
    requireParameterCount(instance, definition, 4);
    return new StepComplexTriangulatedFace(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "COMPLEX_TRIANGULATED_FACE boundaries must contain entity references"),
        entityReferenceList(instance, definition, 2,
            "COMPLEX_TRIANGULATED_FACE vertices must contain entity references"));
  }

  private StepCubicBezierTriangulatedFace resolveCubicBezierTriangulatedFace(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CUBIC_BEZIER_TRIANGULATED_FACE");
    requireParameterCount(instance, definition, 4);
    return new StepCubicBezierTriangulatedFace(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "CUBIC_BEZIER_TRIANGULATED_FACE control_points must contain entity references"),
        integerList(instance, definition, 2));
  }

  private StepMaterialDesignation resolveMaterialDesignation(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MATERIAL_DESIGNATION");
    requireParameterCount(instance, definition, 2);
    List<StepEntity> defs =
        entityReferenceList(
            instance, definition, 1,
            "MATERIAL_DESIGNATION definitions must contain entity references");
    return new StepMaterialDesignation(
        instance.id(),
        stringValue(instance, definition, 0),
        defs);
  }

  private StepLayeredItem resolveLayeredItem(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LAYERED_ITEM");
    requireParameterCount(instance, definition, 2);
    return new StepLayeredItem(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  private StepDatum resolveDatum(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DATUM");
    requireParameterCount(instance, definition, 4);
    return new StepDatum(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        booleanValue(instance, definition, 3));
  }

  private StepDatumFeature resolveDatumFeature(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DATUM_FEATURE");
    requireParameterCount(instance, definition, 4);
    return new StepDatumFeature(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepDatumReference resolveDatumReference(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DATUM_REFERENCE");
    requireParameterCount(instance, definition, 4);
    return new StepDatumReference(
        instance.id(),
        stringValue(instance, definition, 0),
        integerValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepDatumReferenceCompartment resolveDatumReferenceCompartment(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DATUM_REFERENCE_COMPARTMENT");
    requireParameterCount(instance, definition, 6);
    return new StepDatumReferenceCompartment(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        integerValue(instance, definition, 3),
        resolve(referenceId(instance, definition, 4)));
  }

  private StepDatumTarget resolveDatumTarget(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DATUM_TARGET");
    requireParameterCount(instance, definition, 4);
    return new StepDatumTarget(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepDatumSystem resolveDatumSystem(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DATUM_SYSTEM");
    requireParameterCount(instance, definition, 4);
    StepValue datumValue = unwrapTyped(definition.parameters().get(2));
    List<StepEntity> datums;
    if (datumValue instanceof StepValue.ListValue listValue) {
      datums = new ArrayList<>(listValue.elements().size());
      for (StepValue element : listValue.elements()) {
        StepValue unwrapped = unwrapTyped(element);
        if (unwrapped instanceof StepValue.ReferenceValue ref) {
          datums.add(resolve(ref.id()));
        }
      }
    } else if (datumValue instanceof StepValue.ReferenceValue ref) {
      datums = List.of(resolve(ref.id()));
    } else {
      throw new StepResolutionException(
          "DATUM_SYSTEM datums must contain entity references");
    }
    StepValue lastValue = unwrapTyped(definition.parameters().get(3));
    StepEntity tolerance = lastValue instanceof StepValue.ReferenceValue ref
        ? resolve(ref.id()) : null;
    return new StepDatumSystem(
        instance.id(),
        stringValue(instance, definition, 0),
        datums,
        stringValue(instance, definition, 1),
        tolerance);
  }

  private StepDatumSystemReference resolveDatumSystemReference(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DATUM_SYSTEM_REFERENCE");
    requireParameterCount(instance, definition, 4);
    return new StepDatumSystemReference(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        integerValue(instance, definition, 2));
  }

  private StepTolerancePair resolveTolerancePair(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TOLERANCE_PAIR");
    requireParameterCount(instance, definition, 6);
    return new StepTolerancePair(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalNumberValue(instance, definition, 1),
        optionalNumberValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)),
        stringValue(instance, definition, 4));
  }

  private StepToleranceSet resolveToleranceSet(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TOLERANCE_SET");
    requireParameterCount(instance, definition, 5);
    List<StepEntity> tolerances =
        entityReferenceList(
            instance, definition, 1,
            "TOLERANCE_SET tolerances must contain entity references");
    return new StepToleranceSet(
        instance.id(),
        stringValue(instance, definition, 0),
        tolerances,
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  private StepGeometricMeasurement resolveGeometricMeasurement(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GEOMETRIC_MEASUREMENT");
    requireParameterCount(instance, definition, 8);
    List<StepEntity> measurementPoints =
        entityReferenceList(
            instance, definition, 6,
            "GEOMETRIC_MEASUREMENT measurement_points must contain entity references");
    return new StepGeometricMeasurement(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        stringValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)),
        numberValue(instance, definition, 4),
        measurementPoints,
        stringValue(instance, definition, 5));
  }

  private StepDimensionalMeasurement resolveDimensionalMeasurement(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DIMENSIONAL_MEASUREMENT");
    requireParameterCount(instance, definition, 9);
    return new StepDimensionalMeasurement(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        stringValue(instance, definition, 2),
        numberValue(instance, definition, 3),
        numberValue(instance, definition, 4),
        numberValue(instance, definition, 5),
        numberValue(instance, definition, 6),
        resolve(referenceId(instance, definition, 7)));
  }

  // Manufacturing operation resolvers
  private StepMachiningOperation resolveMachiningOperation(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MACHINING_OPERATION");
    requireParameterCount(instance, definition, 4);
    List<StepEntity> features =
        entityReferenceList(
            instance, definition, 3,
            "MACHINING_OPERATION features must contain entity references");
    return new StepMachiningOperation(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        features);
  }

  private StepMachiningOperationSequence resolveMachiningOperationSequence(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MACHINING_OPERATION_SEQUENCE");
    requireParameterCount(instance, definition, 4);
    List<StepEntity> operations =
        entityReferenceList(
            instance, definition, 2,
            "MACHINING_OPERATION_SEQUENCE operations must contain entity references");
    return new StepMachiningOperationSequence(
        instance.id(),
        stringValue(instance, definition, 0),
        operations,
        stringValue(instance, definition, 3));
  }

  // Feature definition resolvers
  private StepFilletDefinition resolveFilletDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FILLET_DEFINITION");
    requireParameterCount(instance, definition, 4);
    List<StepEntity> edges =
        entityReferenceList(
            instance, definition, 2,
            "FILLET_DEFINITION edges must contain entity references");
    return new StepFilletDefinition(
        instance.id(),
        stringValue(instance, definition, 0),
        edges,
        optionalNumberValue(instance, definition, 3));
  }

  private StepChamferDefinition resolveChamferDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CHAMFER_DEFINITION");
    requireParameterCount(instance, definition, 5);
    List<StepEntity> edges =
        entityReferenceList(
            instance, definition, 2,
            "CHAMFER_DEFINITION edges must contain entity references");
    return new StepChamferDefinition(
        instance.id(),
        stringValue(instance, definition, 0),
        edges,
        optionalNumberValue(instance, definition, 3),
        optionalNumberValue(instance, definition, 4));
  }

  private StepRound resolveRound(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ROUND");
    requireParameterCount(instance, definition, 4);
    List<StepEntity> edges =
        entityReferenceList(
            instance, definition, 2,
            "ROUND edges must contain entity references");
    return new StepRound(
        instance.id(),
        stringValue(instance, definition, 0),
        edges,
        optionalNumberValue(instance, definition, 3));
  }

  private StepGroove resolveGroove(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GROOVE");
    requireParameterCount(instance, definition, 5);
    return new StepGroove(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        optionalNumberValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)));
  }

  private StepHole resolveHole(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "HOLE");
    requireParameterCount(instance, definition, 6);
    return new StepHole(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        optionalNumberValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)),
        stringValue(instance, definition, 4));
  }

  private StepSlot resolveSlot(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SLOT");
    requireParameterCount(instance, definition, 6);
    return new StepSlot(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        optionalNumberValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)),
        optionalNumberValue(instance, definition, 4));
  }

  private StepStud resolveStud(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "STUD");
    requireParameterCount(instance, definition, 5);
    return new StepStud(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        optionalNumberValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)));
  }

  private StepProtrusion resolveProtrusion(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PROTRUSION");
    requireParameterCount(instance, definition, 6);
    return new StepProtrusion(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        optionalNumberValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)),
        optionalNumberValue(instance, definition, 4));
  }

  private StepCutout resolveCutout(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CUTOUT");
    requireParameterCount(instance, definition, 5);
    return new StepCutout(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        optionalNumberValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)));
  }

  private StepDepression resolveDepression(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DEPRESSION");
    requireParameterCount(instance, definition, 6);
    return new StepDepression(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        optionalNumberValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)),
        optionalNumberValue(instance, definition, 4));
  }

  private StepMarking resolveMarking(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MARKING");
    requireParameterCount(instance, definition, 5);
    return new StepMarking(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        optionalNumberValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)));
  }

  // Pattern resolvers
  private StepCircularPattern resolveCircularPattern(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CIRCULAR_PATTERN");
    requireParameterCount(instance, definition, 6);
    return new StepCircularPattern(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        optionalNumberValue(instance, definition, 3),
        optionalIntegerValue(instance, definition, 4));
  }

  private StepLinearPattern resolveLinearPattern(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LINEAR_PATTERN");
    requireParameterCount(instance, definition, 6);
    return new StepLinearPattern(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        optionalNumberValue(instance, definition, 3),
        optionalIntegerValue(instance, definition, 4));
  }

  private StepCompositeDatumReference resolveCompositeDatumReference(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COMPOSITE_DATUM_REFERENCE");
    requireParameterCount(instance, definition, 4);
    List<StepEntity> datums =
        entityReferenceList(
            instance, definition, 2,
            "COMPOSITE_DATUM_REFERENCE datums must contain entity references");
    return new StepCompositeDatumReference(
        instance.id(),
        stringValue(instance, definition, 0),
        datums,
        stringValue(instance, definition, 3));
  }

  private StepCsgVolume resolveCsgVolume(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CSG_VOLUME");
    requireParameterCount(instance, definition, 3);
    return new StepCsgVolume(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  private StepBlockVolume resolveBlockVolume(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BLOCK_VOLUME");
    requireParameterCount(instance, definition, 6);
    return new StepBlockVolume(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3),
        numberValue(instance, definition, 4));
  }

  private StepAssemblyProcessPlan resolveAssemblyProcessPlan(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ASSEMBLY_PROCESS_PLAN");
    requireParameterCount(instance, definition, 5);
    List<StepEntity> items =
        entityReferenceList(
            instance, definition, 2,
            "ASSEMBLY_PROCESS_PLAN items must contain entity references");
    List<StepEntity> assemblySequence =
        entityReferenceList(
            instance, definition, 4,
            "ASSEMBLY_PROCESS_PLAN assembly_sequence must contain entity references");
    return new StepAssemblyProcessPlan(
        instance.id(),
        stringValue(instance, definition, 0),
        items,
        resolve(referenceId(instance, definition, 3)),
        assemblySequence);
  }

  private StepMachiningProcessPlan resolveMachiningProcessPlan(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MACHINING_PROCESS_PLAN");
    requireParameterCount(instance, definition, 5);
    List<StepEntity> items =
        entityReferenceList(
            instance, definition, 2,
            "MACHINING_PROCESS_PLAN items must contain entity references");
    List<StepEntity> operations =
        entityReferenceList(
            instance, definition, 4,
            "MACHINING_PROCESS_PLAN operations must contain entity references");
    return new StepMachiningProcessPlan(
        instance.id(),
        stringValue(instance, definition, 0),
        items,
        resolve(referenceId(instance, definition, 3)),
        operations);
  }

  private StepMachiningWorkPlan resolveMachiningWorkPlan(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MACHINING_WORK_PLAN");
    requireParameterCount(instance, definition, 5);
    List<StepEntity> items =
        entityReferenceList(
            instance, definition, 2,
            "MACHINING_WORK_PLAN items must contain entity references");
    List<StepEntity> machiningSetup =
        entityReferenceList(
            instance, definition, 4,
            "MACHINING_WORK_PLAN machining_setup must contain entity references");
    return new StepMachiningWorkPlan(
        instance.id(),
        stringValue(instance, definition, 0),
        items,
        resolve(referenceId(instance, definition, 3)),
        machiningSetup);
  }

  private StepRectangularToleranceZone resolveRectangularToleranceZone(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "RECTANGULAR_TOLERANCE_ZONE");
    requireParameterCount(instance, definition, 6);
    return new StepRectangularToleranceZone(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        optionalNumberValue(instance, definition, 3),
        optionalNumberValue(instance, definition, 4));
  }

  private StepToleranceModifier resolveToleranceModifier(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TOLERANCE_MODIFIER");
    requireParameterCount(instance, definition, 5);
    return new StepToleranceModifier(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        numberValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)));
  }

  private StepTypedMeasureWithUnit resolveTypedMeasureWithUnit(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 3);
    return new StepTypedMeasureWithUnit(
        instance.id(),
        entityName,
        numberValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  private StepCartesianTransformationOperator resolveCartesianTransformationOperator(
      StepEntityInstance instance) {
    // Base type: check for concrete 2D/3D subtype at the same ID
    StepEntityDefinition concrete = instance.definitions().stream()
        .filter(d -> !d.name().equals("CARTESIAN_TRANSFORMATION_OPERATOR"))
        .filter(d -> d.name().startsWith("CARTESIAN_TRANSFORMATION_OPERATOR"))
        .findFirst()
        .orElse(null);
    if (concrete != null) {
      return resolveCartesianTransformationOperator(instance, concrete.name());
    }
    throw new UnsupportedStepEntityException("CARTESIAN_TRANSFORMATION_OPERATOR is an abstract base type with no concrete subtype");
  }

  private StepCartesianTransformationOperator resolveCartesianTransformationOperator(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    // 2D has 4-5 params, 3D has 6-7 params
    int paramCount = definition.parameters().size();
    return new StepCartesianTransformationOperator(
        instance.id(),
        stringValue(instance, definition, 0),
        paramCount >= 4 ? optionalResolveDirection(referenceId(instance, definition, 1)) : null,
        paramCount >= 5 ? optionalResolveDirection(referenceId(instance, definition, 2)) : null,
        optionalResolveCartesianPoint(referenceId(instance, definition, paramCount >= 7 ? 3 : paramCount == 6 ? 2 : 2)),
        paramCount >= 6 ? optionalNumberValue(instance, definition, paramCount >= 7 ? 4 : 3) : null,
        paramCount >= 7 ? optionalResolveDirection(referenceId(instance, definition, 5)) : null,
        entityName);
  }

  private StepDirection optionalResolveDirection(int id) {
    StepEntity entity = resolve(id);
    if (entity instanceof StepDirection direction) {
      return direction;
    }
    return null;
  }

  private StepCartesianPoint optionalResolveCartesianPoint(int id) {
    StepEntity entity = resolve(id);
    if (entity instanceof StepCartesianPoint point) {
      return point;
    }
    return null;
  }

  private StepDimensionalSize resolveDimensionalSize(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DIMENSIONAL_SIZE");
    requireParameterCount(instance, definition, 4);
    return new StepDimensionalSize(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepDimensionalLocation resolveDimensionalLocation(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DIMENSIONAL_LOCATION");
    requireParameterCount(instance, definition, 4);
    return new StepDimensionalLocation(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepShapeDimensionRepresentation resolveShapeDimensionRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SHAPE_DIMENSION_REPRESENTATION");
    requireParameterCount(instance, definition, 3);
    List<StepEntity> items =
        entityReferenceList(
            instance, definition, 1,
            "SHAPE_DIMENSION_REPRESENTATION items must contain entity references");
    return new StepShapeDimensionRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        items,
        resolve(referenceId(instance, definition, 2)));
  }

  private StepPlusMinusTolerance resolvePlusMinusTolerance(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PLUS_MINUS_TOLERANCE");
    requireParameterCount(instance, definition, 4);
    return new StepPlusMinusTolerance(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepToleranceValue resolveToleranceValue(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TOLERANCE_VALUE");
    requireParameterCount(instance, definition, 4);
    return new StepToleranceValue(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        numberValue(instance, definition, 2));
  }

  private StepMeasureRepresentationItemWithUnit resolveMeasureRepresentationItemWithUnit(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MEASURE_REPRESENTATION_ITEM_WITH_UNIT");
    requireParameterCount(instance, definition, 4);
    return new StepMeasureRepresentationItemWithUnit(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepMeasureQualification resolveMeasureQualification(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MEASURE_QUALIFICATION");
    requireParameterCount(instance, definition, 4);
    List<StepEntity> qualifiers =
        entityReferenceList(
            instance, definition, 2,
            "MEASURE_QUALIFICATION qualifiers must contain entity references");
    return new StepMeasureQualification(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        qualifiers);
  }

  private StepMakeFromFeature resolveMakeFromFeature(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MAKE_FROM_FEATURE");
    requireParameterCount(instance, definition, 4);
    return new StepMakeFromFeature(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepMakeFromUsageOption resolveMakeFromUsageOption(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MAKE_FROM_USAGE_OPTION");
    requireParameterCount(instance, definition, 4);
    return new StepMakeFromUsageOption(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepQuantifiedAssemblyComponentUsage resolveQuantifiedAssemblyComponentUsage(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "QUANTIFIED_ASSEMBLY_COMPONENT_USAGE");
    requireParameterCount(instance, definition, 5);
    return new StepQuantifiedAssemblyComponentUsage(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        integerValue(instance, definition, 3));
  }

  private StepSpecifiedHigherUsageOccurrence resolveSpecifiedHigherUsageOccurrence(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SPECIFIED_HIGHER_USAGE_OCCURRENCE");
    requireParameterCount(instance, definition, 4);
    return new StepSpecifiedHigherUsageOccurrence(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepAlternateProductRelationship resolveAlternateProductRelationship(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ALTERNATE_PRODUCT_RELATIONSHIP");
    requireParameterCount(instance, definition, 5);
    return new StepAlternateProductRelationship(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  private StepProductDefinitionWithAssociatedDocuments resolveProductDefinitionWithAssociatedDocuments(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRODUCT_DEFINITION_WITH_ASSOCIATED_DOCUMENTS");
    requireParameterCount(instance, definition, 4);
    List<StepEntity> docs =
        entityReferenceList(
            instance, definition, 2,
            "PRODUCT_DEFINITION_WITH_ASSOCIATED_DOCUMENTS documents must contain entity references");
    return new StepProductDefinitionWithAssociatedDocuments(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        docs);
  }

  private StepShapeAspectShapeRepresentation resolveShapeAspectShapeRepresentation(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SHAPE_ASPECT_SHAPE_REPRESENTATION");
    requireParameterCount(instance, definition, 4);
    return new StepShapeAspectShapeRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  private StepMakeFromBuildAssembly resolveMakeFromBuildAssembly(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MAKE_FROM_BUILD_ASSEMBLY");
    requireParameterCount(instance, definition, 4);
    return new StepMakeFromBuildAssembly(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepAssemblyComponentRelationship resolveAssemblyComponentRelationship(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ASSEMBLY_COMPONENT_RELATIONSHIP");
    requireParameterCount(instance, definition, 5);
    return new StepAssemblyComponentRelationship(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  private StepDesignMakeFrom resolveDesignMakeFrom(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DESIGN_MAKE_FROM");
    requireParameterCount(instance, definition, 5);
    return new StepDesignMakeFrom(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  private StepInterpolatedConfigurationSegment resolveInterpolatedConfigurationSegment(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "INTERPOLATED_CONFIGURATION_SEGMENT");
    requireParameterCount(instance, definition, 4);
    return new StepInterpolatedConfigurationSegment(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepRangeDimensionalSize resolveRangeDimensionalSize(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "RANGE_DIMENSIONAL_SIZE");
    requireParameterCount(instance, definition, 5);
    return new StepRangeDimensionalSize(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  private StepDesignedPartDesignVersion resolveDesignedPartDesignVersion(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DESIGNED_PART_DESIGN_VERSION");
    requireParameterCount(instance, definition, 4);
    return new StepDesignedPartDesignVersion(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepSurfaceStyleRendering resolveSurfaceStyleRendering(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_STYLE_RENDERING");
    requireParameterCount(instance, definition, 6);
    return new StepSurfaceStyleRendering(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3),
        numberValue(instance, definition, 4));
  }

  private StepSurfaceStyleRenderingWithProperties resolveSurfaceStyleRenderingWithProperties(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_STYLE_RENDERING_WITH_PROPERTIES");
    requireParameterCount(instance, definition, 3);
    List<StepEntity> props =
        entityReferenceList(
            instance, definition, 1,
            "SURFACE_STYLE_RENDERING_WITH_PROPERTIES properties must contain entity references");
    return new StepSurfaceStyleRenderingWithProperties(
        instance.id(),
        stringValue(instance, definition, 0),
        props);
  }

  private StepRenderingProperties resolveRenderingProperties(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "RENDERING_PROPERTIES");
    requireParameterCount(instance, definition, 4);
    return new StepRenderingProperties(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        numberValue(instance, definition, 2));
  }

  private StepLightSource resolveLightSource(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LIGHT_SOURCE");
    requireParameterCount(instance, definition, 4);
    return new StepLightSource(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2));
  }

  private StepLightSourceAmbient resolveLightSourceAmbient(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LIGHT_SOURCE_AMBIENT");
    requireParameterCount(instance, definition, 4);
    return new StepLightSourceAmbient(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2));
  }

  private StepLightSourceDirectional resolveLightSourceDirectional(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LIGHT_SOURCE_DIRECTIONAL");
    requireParameterCount(instance, definition, 5);
    return new StepLightSourceDirectional(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)));
  }

  private StepLightSourcePositional resolveLightSourcePositional(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LIGHT_SOURCE_POSITIONAL");
    requireParameterCount(instance, definition, 5);
    return new StepLightSourcePositional(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)));
  }

  private StepLightSourceSpot resolveLightSourceSpot(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LIGHT_SOURCE_SPOT");
    requireParameterCount(instance, definition, 8);
    return new StepLightSourceSpot(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)),
        resolve(referenceId(instance, definition, 4)),
        numberValue(instance, definition, 5),
        numberValue(instance, definition, 6));
  }

  private StepPresentationLayerUsage resolvePresentationLayerUsage(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRESENTATION_LAYER_USAGE");
    requireParameterCount(instance, definition, 4);
    return new StepPresentationLayerUsage(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepCameraModelD2 resolveCameraModelD2(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CAMERA_MODEL_D2");
    requireParameterCount(instance, definition, 4);
    return new StepCameraModelD2(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepCameraModelD3 resolveCameraModelD3(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CAMERA_MODEL_D3");
    requireParameterCount(instance, definition, 5);
    return new StepCameraModelD3(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        numberValue(instance, definition, 3));
  }

  private StepCameraUsage resolveCameraUsage(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CAMERA_USAGE");
    requireParameterCount(instance, definition, 4);
    return new StepCameraUsage(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepCameraImage resolveCameraImage(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CAMERA_IMAGE");
    requireParameterCount(instance, definition, 5);
    return new StepCameraImage(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        integerValue(instance, definition, 2),
        integerValue(instance, definition, 3));
  }

  private StepPlanarBox resolvePlanarBox(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PLANAR_BOX");
    requireParameterCount(instance, definition, 5);
    return new StepPlanarBox(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  private StepPlanarExtent resolvePlanarExtent(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PLANAR_EXTENT");
    requireParameterCount(instance, definition, 4);
    return new StepPlanarExtent(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        numberValue(instance, definition, 2));
  }

  private StepViewVolume resolveViewVolume(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "VIEW_VOLUME");
    requireParameterCount(instance, definition, 7);
    return new StepViewVolume(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3),
        numberValue(instance, definition, 4),
        numberValue(instance, definition, 5));
  }

  private StepMechanicalDesignShapeRepresentation resolveMechanicalDesignShapeRepresentation(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MECHANICAL_DESIGN_SHAPE_REPRESENTATION");
    requireParameterCount(instance, definition, 3);
    return new StepMechanicalDesignShapeRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  private StepKinematicPair resolveKinematicPair(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "KINEMATIC_PAIR");
    requireParameterCount(instance, definition, 5);
    return new StepKinematicPair(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  private StepKinematicJoint resolveKinematicJoint(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "KINEMATIC_JOINT");
    requireParameterCount(instance, definition, 4);
    return new StepKinematicJoint(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepKinematicLink resolveKinematicLink(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "KINEMATIC_LINK");
    requireParameterCount(instance, definition, 4);
    return new StepKinematicLink(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepKinematicStructure resolveKinematicStructure(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "KINEMATIC_STRUCTURE");
    requireParameterCount(instance, definition, 4);
    return new StepKinematicStructure(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepKinematicPair resolveKinematicPair(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 7);
    return new StepKinematicPair(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  private StepPrismaticPair resolvePrismaticPair(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRISMATIC_PAIR");
    requireParameterCount(instance, definition, 7);
    return new StepPrismaticPair(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        resolve(referenceId(instance, definition, 4)),
        resolve(referenceId(instance, definition, 5)));
  }

  private StepRevolutePair resolveRevolutePair(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "REVOLUTE_PAIR");
    requireParameterCount(instance, definition, 7);
    return new StepRevolutePair(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        resolve(referenceId(instance, definition, 4)),
        resolve(referenceId(instance, definition, 5)));
  }

  private StepCylindricalPair resolveCylindricalPair(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CYLINDRICAL_PAIR");
    requireParameterCount(instance, definition, 7);
    return new StepCylindricalPair(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        resolve(referenceId(instance, definition, 4)),
        resolve(referenceId(instance, definition, 5)));
  }

  private StepSphericalPair resolveSphericalPair(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SPHERICAL_PAIR");
    requireParameterCount(instance, definition, 6);
    return new StepSphericalPair(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        resolve(referenceId(instance, definition, 4)));
  }

  private StepPlanarPair resolvePlanarPair(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PLANAR_PAIR");
    requireParameterCount(instance, definition, 7);
    return new StepPlanarPair(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        resolve(referenceId(instance, definition, 4)),
        resolve(referenceId(instance, definition, 5)));
  }

  private StepUniversalPair resolveUniversalPair(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "UNIVERSAL_PAIR");
    requireParameterCount(instance, definition, 8);
    return new StepUniversalPair(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        resolve(referenceId(instance, definition, 4)),
        resolve(referenceId(instance, definition, 5)),
        resolve(referenceId(instance, definition, 6)));
  }

  private StepScrewPair resolveScrewPair(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SCREW_PAIR");
    requireParameterCount(instance, definition, 8);
    return new StepScrewPair(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        numberValue(instance, definition, 4),
        resolve(referenceId(instance, definition, 5)),
        resolve(referenceId(instance, definition, 6)));
  }

  private StepGearPair resolveGearPair(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GEAR_PAIR");
    requireParameterCount(instance, definition, 8);
    return new StepGearPair(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        numberValue(instance, definition, 4),
        resolve(referenceId(instance, definition, 5)),
        resolve(referenceId(instance, definition, 6)));
  }

  private StepGearPairWithRange resolveGearPairWithRange(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GEAR_PAIR_WITH_RANGE");
    requireParameterCount(instance, definition, 11);
    return new StepGearPairWithRange(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        numberValue(instance, definition, 4),
        numberValue(instance, definition, 5),
        numberValue(instance, definition, 6),
        resolve(referenceId(instance, definition, 7)),
        resolve(referenceId(instance, definition, 8)));
  }

  private StepRackAndPinionPair resolveRackAndPinionPair(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "RACK_AND_PINION_PAIR");
    requireParameterCount(instance, definition, 8);
    return new StepRackAndPinionPair(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        numberValue(instance, definition, 4),
        resolve(referenceId(instance, definition, 5)),
        resolve(referenceId(instance, definition, 6)));
  }

  private StepLowOrderKinematicPairWithRange resolveLowOrderKinematicPairWithRange(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LOW_ORDER_KINEMATIC_PAIR_WITH_RANGE");
    requireParameterCount(instance, definition, 10);
    return new StepLowOrderKinematicPairWithRange(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        numberValue(instance, definition, 4),
        numberValue(instance, definition, 5),
        resolve(referenceId(instance, definition, 6)),
        resolve(referenceId(instance, definition, 7)));
  }

  private StepActuatedKinematicPair resolveActuatedKinematicPair(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ACTUATED_KINEMATIC_PAIR");
    requireParameterCount(instance, definition, 6);
    return new StepActuatedKinematicPair(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        numberValue(instance, definition, 4));
  }

  private StepMechanismStateRepresentation resolveMechanismStateRepresentation(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MECHANISM_STATE_REPRESENTATION");
    requireParameterCount(instance, definition, 3);
    return new StepMechanismStateRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "MECHANISM_STATE_REPRESENTATION items must contain entity references"),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepKinematicPath resolveKinematicPath(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "KINEMATIC_PATH");
    requireParameterCount(instance, definition, 5);
    return new StepKinematicPath(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        entityReferenceList(instance, definition, 4,
            "KINEMATIC_PATH pairs must contain entity references"));
  }

  private StepKinematicFrameBasedTransformation resolveKinematicFrameBasedTransformation(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "KINEMATIC_FRAME_BASED_TRANSFORMATION");
    requireParameterCount(instance, definition, 5);
    return new StepKinematicFrameBasedTransformation(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  private StepValidationPropertyRepresentation resolveValidationPropertyRepresentation(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "VALIDATION_PROPERTY_REPRESENTATION");
    requireParameterCount(instance, definition, 5);
    return new StepValidationPropertyRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        entityReferenceList(instance, definition, 2,
            "VALIDATION_PROPERTY_REPRESENTATION items must contain entity references"),
        resolve(referenceId(instance, definition, 3)));
  }

  private StepCalculatedGeometricRepresentationItem resolveCalculatedGeometricRepresentationItem(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CALCULATED_GEOMETRIC_REPRESENTATION_ITEM");
    requireParameterCount(instance, definition, 3);
    return new StepCalculatedGeometricRepresentationItem(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  private static void registerKinematicPairAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(entityName,
          (resolver, instance) -> resolver.resolveKinematicPair(instance, entityName));
    }
  }

  // Phase 5: FEA resolve methods

  private StepVolume3dElementRepresentation resolveVolume3dElementRepresentation(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "VOLUME_3D_ELEMENT_REPRESENTATION");
    requireParameterCount(instance, definition, 4);
    return new StepVolume3dElementRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "VOLUME_3D_ELEMENT_REPRESENTATION elements must contain entity references"),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepVolume3dElementProperty resolveVolume3dElementProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "VOLUME_3D_ELEMENT_PROPERTY");
    requireParameterCount(instance, definition, 5);
    return new StepVolume3dElementProperty(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  private StepCurve3dElementProperty resolveCurve3dElementProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CURVE_3D_ELEMENT_PROPERTY");
    requireParameterCount(instance, definition, 5);
    return new StepCurve3dElementProperty(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  private StepSurface3dElementProperty resolveSurface3dElementProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_3D_ELEMENT_PROPERTY");
    requireParameterCount(instance, definition, 5);
    return new StepSurface3dElementProperty(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  private StepFeaMaterialPropertyRepresentation resolveFeaMaterialPropertyRepresentation(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_MATERIAL_PROPERTY_REPRESENTATION");
    requireParameterCount(instance, definition, 4);
    return new StepFeaMaterialPropertyRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        entityReferenceList(instance, definition, 2,
            "FEA_MATERIAL_PROPERTY_REPRESENTATION properties must contain entity references"));
  }

  private StepElementVolume2d resolveElementVolume2d(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ELEMENT_VOLUME_2D");
    requireParameterCount(instance, definition, 4);
    return new StepElementVolume2d(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "ELEMENT_VOLUME_2D nodes must contain entity references"),
        stringValue(instance, definition, 2));
  }

  private StepElementVolume3d resolveElementVolume3d(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ELEMENT_VOLUME_3D");
    requireParameterCount(instance, definition, 4);
    return new StepElementVolume3d(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "ELEMENT_VOLUME_3D nodes must contain entity references"),
        stringValue(instance, definition, 2));
  }

  private StepNodeSet resolveNodeSet(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "NODE_SET");
    requireParameterCount(instance, definition, 3);
    return new StepNodeSet(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "NODE_SET nodes must contain entity references"));
  }

  private StepElementSet resolveElementSet(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ELEMENT_SET");
    requireParameterCount(instance, definition, 3);
    return new StepElementSet(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "ELEMENT_SET elements must contain entity references"));
  }

  private StepFeaSecuredVariable resolveFeaSecuredVariable(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_SECURED_VARIABLE");
    requireParameterCount(instance, definition, 4);
    return new StepFeaSecuredVariable(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepFeaConstantFunction3d resolveFeaConstantFunction3d(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_CONSTANT_FUNCTION_3D");
    requireParameterCount(instance, definition, 4);
    return new StepFeaConstantFunction3d(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepFeaLinearAlgebraicMatrix resolveFeaLinearAlgebraicMatrix(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_LINEAR_ALGEBRAIC_MATRIX");
    requireParameterCount(instance, definition, 5);
    return new StepFeaLinearAlgebraicMatrix(
        instance.id(),
        stringValue(instance, definition, 0),
        (int) numberValue(instance, definition, 1),
        (int) numberValue(instance, definition, 2),
        numberList(instance, definition, 3));
  }

  private StepFeaLinearAlgebraicVector resolveFeaLinearAlgebraicVector(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_LINEAR_ALGEBRAIC_VECTOR");
    requireParameterCount(instance, definition, 4);
    return new StepFeaLinearAlgebraicVector(
        instance.id(),
        stringValue(instance, definition, 0),
        (int) numberValue(instance, definition, 1),
        numberList(instance, definition, 2));
  }

  private StepFeaAxis2Placement3d resolveFeaAxis2Placement3d(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_AXIS_2_PLACEMENT_3D");
    requireParameterCount(instance, definition, 5);
    return new StepFeaAxis2Placement3d(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  private StepFeaGroupRepresentation resolveFeaGroupRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_GROUP_REPRESENTATION");
    requireParameterCount(instance, definition, 4);
    return new StepFeaGroupRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "FEA_GROUP_REPRESENTATION representations must contain entity references"),
        stringValue(instance, definition, 2));
  }

  private static void registerFeaAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(entityName,
          (resolver, instance) -> resolver.resolveRepresentation(instance, entityName, false));
    }
  }

  private StepPersonAndOrganizationAddress resolvePersonAndOrganizationAddress(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PERSON_AND_ORGANIZATION_ADDRESS");
    requireParameterCount(instance, definition, 4);
    return new StepPersonAndOrganizationAddress(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepOrganizationAddress resolveOrganizationAddress(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ORGANIZATION_ADDRESS");
    requireParameterCount(instance, definition, 4);
    return new StepOrganizationAddress(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepPersonAddress resolvePersonAddress(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PERSON_ADDRESS");
    requireParameterCount(instance, definition, 4);
    return new StepPersonAddress(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepAngularSize resolveAngularSize(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ANGULAR_SIZE");
    requireParameterCount(instance, definition, 4);
    return new StepAngularSize(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        numberValue(instance, definition, 2));
  }

  private StepGeneralizedDatum resolveGeneralizedDatum(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GENERALIZED_DATUM");
    requireParameterCount(instance, definition, 4);
    return new StepGeneralizedDatum(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepActionDirective resolveActionDirective(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ACTION_DIRECTIVE");
    requireParameterCount(instance, definition, 4);
    return new StepActionDirective(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        stringValue(instance, definition, 2));
  }

  private StepActionMethod resolveActionMethod(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ACTION_METHOD");
    requireParameterCount(instance, definition, 4);
    return new StepActionMethod(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        stringValue(instance, definition, 2));
  }

  private StepAction resolveAction(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ACTION");
    requireParameterCount(instance, definition, 4);
    return new StepAction(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        stringValue(instance, definition, 2));
  }

  private StepActionRelationship resolveActionRelationship(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ACTION_RELATIONSHIP");
    requireParameterCount(instance, definition, 5);
    return new StepActionRelationship(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  private StepActionStatus resolveActionStatus(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ACTION_STATUS");
    requireParameterCount(instance, definition, 3);
    return new StepActionStatus(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1));
  }

  private StepColorSpecification resolveColorSpecification(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COLOR_SPECIFICATION");
    requireParameterCount(instance, definition, 4);
    return new StepColorSpecification(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  private StepWithDescriptiveRepresentationItem resolveWithDescriptiveRepresentationItem(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "WITH_DESCRIPTIVE_REPRESENTATION_ITEM");
    requireParameterCount(instance, definition, 4);
    List<StepEntity> items =
        entityReferenceList(
            instance, definition, 2,
            "WITH_DESCRIPTIVE_REPRESENTATION_ITEM items must contain entity references");
    return new StepWithDescriptiveRepresentationItem(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        items,
        resolve(referenceId(instance, definition, 3)));
  }

  private static void registerGeometricToleranceAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName, (resolver, instance) -> resolver.resolveGeometricTolerance(instance, entityName));
    }
  }

  private boolean isOpenShellEntity(StepEntity entity) {
    return entity instanceof StepOpenShell
        || entity instanceof StepSurfacedOpenShell
        || entity instanceof StepOrientedOpenShell;
  }

  private boolean isClosedShellEntity(StepEntity entity) {
    return entity instanceof StepClosedShell || entity instanceof StepOrientedClosedShell;
  }

  private boolean isShellEntity(StepEntity entity) {
    return isOpenShellEntity(entity) || isClosedShellEntity(entity);
  }

  private boolean isBooleanOperandEntity(StepEntity entity) {
    return entity instanceof StepManifoldSolidBrep
        || entity instanceof StepBrepWithVoids
        || entity instanceof StepCsgPrimitive
        || entity instanceof StepCsgSolid
        || entity instanceof StepHalfSpaceSolid
        || entity instanceof StepSolidReplica
        || entity instanceof StepSweptAreaSolid
        || entity instanceof StepBooleanResult
        || entity instanceof StepBooleanClippingResult;
  }

  private StepShellBasedWireframeModel resolveShellBasedWireframeModel(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SHELL_BASED_WIREFRAME_MODEL");
    requireParameterCount(instance, definition, 2);
    List<StepEntity> shells =
        entityReferenceList(
            instance,
            definition,
            1,
            "SHELL_BASED_WIREFRAME_MODEL sbwm_boundary must contain shell references");
    for (StepEntity shell : shells) {
      if (!(shell instanceof StepVertexShell) && !(shell instanceof StepWireShell)) {
        throw new StepResolutionException(
            "SHELL_BASED_WIREFRAME_MODEL sbwm_boundary must reference VERTEX_SHELL or WIRE_SHELL");
      }
    }
    return new StepShellBasedWireframeModel(
        instance.id(), stringValue(instance, definition, 0), shells);
  }

  private StepBrepWithVoids resolveBrepWithVoids(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BREP_WITH_VOIDS");
    requireParameterCount(instance, definition, 3);
    return new StepBrepWithVoids(
        instance.id(),
        stringValue(instance, definition, 0),
        requireClosedShellEntity(instance, definition, 1, "BREP_WITH_VOIDS outer must reference CLOSED_SHELL"),
        requireClosedShellEntities(instance, definition, 2, "BREP_WITH_VOIDS voids must contain CLOSED_SHELL references"));
  }

  private StepBooleanResult resolveBooleanResult(StepEntityInstance instance) {
    return resolveBooleanResult(instance, "BOOLEAN_RESULT");
  }

  private StepBooleanResult resolveBooleanResult(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 3);
    StepEntity firstOperand = resolve(referenceId(instance, definition, 1));
    StepEntity secondOperand = resolve(referenceId(instance, definition, 2));
    if (!isBooleanOperandEntity(firstOperand) || !isBooleanOperandEntity(secondOperand)) {
      throw new StepResolutionException(
          entityName
              + " operands must reference a supported solid or CSG operand");
    }
    return new StepBooleanResult(
        instance.id(),
        inheritedRepresentationItemName(instance),
        enumValue(instance, definition, 0),
        firstOperand,
        secondOperand);
  }

  private StepBooleanClippingResult resolveBooleanClippingResult(StepEntityInstance instance) {
    StepBooleanResult base = resolveBooleanResult(instance, "BOOLEAN_CLIPPING_RESULT");
    return new StepBooleanClippingResult(
        base.id(), base.name(), base.operator(), base.firstOperand(), base.secondOperand());
  }

  private StepEntity requireClosedShellEntity(
      StepEntityInstance instance, StepEntityDefinition definition, int parameterIndex, String message) {
    StepEntity shell = resolve(referenceId(instance, definition, parameterIndex));
    if (!isClosedShellEntity(shell)) {
      throw new StepResolutionException(message);
    }
    return shell;
  }

  private List<StepEntity> requireClosedShellEntities(
      StepEntityInstance instance, StepEntityDefinition definition, int parameterIndex, String message) {
    List<StepEntity> shells = entityReferenceList(instance, definition, parameterIndex, message);
    for (StepEntity shell : shells) {
      if (!isClosedShellEntity(shell)) {
        throw new StepResolutionException(message);
      }
    }
    return shells;
  }

  private StepRepresentationContext resolveRepresentationContext(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "REPRESENTATION_CONTEXT");
    requireParameterCount(instance, definition, 2);
    return new StepRepresentationContext(
        instance.id(), stringValue(instance, definition, 0), stringValue(instance, definition, 1));
  }

  private StepApplicationContext resolveApplicationContext(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "APPLICATION_CONTEXT");
    requireParameterCount(instance, definition, 1);
    return new StepApplicationContext(instance.id(), stringValue(instance, definition, 0));
  }

  private StepApplicationProtocolDefinition resolveApplicationProtocolDefinition(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "APPLICATION_PROTOCOL_DEFINITION");
    requireParameterCount(instance, definition, 4);
    return new StepApplicationProtocolDefinition(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        integerValue(instance, definition, 2),
        requireEntity(
            referenceId(instance, definition, 3),
            StepApplicationContext.class,
            "APPLICATION_PROTOCOL_DEFINITION application must reference APPLICATION_CONTEXT"));
  }

  private StepProductContext resolveProductContext(StepEntityInstance instance) {
    return resolveProductContext(instance, "PRODUCT_CONTEXT");
  }

  private StepProductContext resolveProductContext(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 3);
    boolean stepOrder =
        unwrapTyped(definition.parameters().get(1)) instanceof StepValue.ReferenceValue;
    return new StepProductContext(
        instance.id(),
        stringValue(instance, definition, 0),
        stepOrder ? stringValue(instance, definition, 2) : stringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, stepOrder ? 1 : 2),
            StepApplicationContext.class,
            entityName + " frame_of_reference must reference APPLICATION_CONTEXT"),
        entityName);
  }

  private StepProduct resolveProduct(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRODUCT");
    requireParameterCount(instance, definition, 4);
    return new StepProduct(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        optionalStringValue(instance, definition, 2),
        referenceList(
            instance,
            definition,
            3,
            StepProductContext.class,
            "PRODUCT frame_of_reference must contain PRODUCT_CONTEXT references"));
  }

  private StepProductRelatedProductCategory resolveProductRelatedProductCategory(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRODUCT_RELATED_PRODUCT_CATEGORY");
    requireParameterCount(instance, definition, 3);
    return new StepProductRelatedProductCategory(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        referenceList(
            instance,
            definition,
            2,
            StepProduct.class,
            "PRODUCT_RELATED_PRODUCT_CATEGORY products must contain PRODUCT references"));
  }

  private StepProductCategory resolveProductCategory(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRODUCT_CATEGORY");
    requireParameterCount(instance, definition, 2);
    return new StepProductCategory(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1));
  }

  private StepProductCategoryRelationship resolveProductCategoryRelationship(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRODUCT_CATEGORY_RELATIONSHIP");
    requireParameterCount(instance, definition, 4);
    return new StepProductCategoryRelationship(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepProductCategory.class,
            "PRODUCT_CATEGORY_RELATIONSHIP category must reference PRODUCT_CATEGORY"),
        requireEntity(
            referenceId(instance, definition, 3),
            StepProductCategory.class,
            "PRODUCT_CATEGORY_RELATIONSHIP sub_category must reference PRODUCT_CATEGORY"));
  }

  private StepProductRelationship resolveProductRelationship(StepEntityInstance instance) {
    return resolveProductRelationship(instance, "PRODUCT_RELATIONSHIP");
  }

  private StepProductRelationship resolveProductRelationship(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 5);
    return new StepProductRelationship(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        optionalStringValue(instance, definition, 2),
        requireEntity(
            referenceId(instance, definition, 3),
            StepProduct.class,
            entityName + " relating_product must reference PRODUCT"),
        requireEntity(
            referenceId(instance, definition, 4),
            StepProduct.class,
            entityName + " related_product must reference PRODUCT"),
        entityName);
  }

  private StepProductDefinitionFormation resolveProductDefinitionFormation(
      StepEntityInstance instance) {
    StepEntityDefinition definition;
    if (instance.hasDefinition("PRODUCT_DEFINITION_FORMATION_WITH_SPECIFIED_SOURCE")) {
      definition = definition(instance, "PRODUCT_DEFINITION_FORMATION_WITH_SPECIFIED_SOURCE");
      requireParameterCount(instance, definition, 4);
    } else {
      definition = definition(instance, "PRODUCT_DEFINITION_FORMATION");
      requireParameterCount(instance, definition, 3);
    }
    return new StepProductDefinitionFormation(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepProduct.class,
            "PRODUCT_DEFINITION_FORMATION of_product must reference PRODUCT"));
  }

  private StepProductDefinitionFormationRelationship
      resolveProductDefinitionFormationRelationship(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRODUCT_DEFINITION_FORMATION_RELATIONSHIP");
    requireParameterCount(instance, definition, 5);
    return new StepProductDefinitionFormationRelationship(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        optionalStringValue(instance, definition, 2),
        requireEntity(
            referenceId(instance, definition, 3),
            StepProductDefinitionFormation.class,
            "PRODUCT_DEFINITION_FORMATION_RELATIONSHIP relating_product_definition_formation must reference PRODUCT_DEFINITION_FORMATION"),
        requireEntity(
            referenceId(instance, definition, 4),
            StepProductDefinitionFormation.class,
            "PRODUCT_DEFINITION_FORMATION_RELATIONSHIP related_product_definition_formation must reference PRODUCT_DEFINITION_FORMATION"));
  }

  private StepProductDefinitionContext resolveProductDefinitionContext(
      StepEntityInstance instance) {
    return resolveProductDefinitionContext(instance, "PRODUCT_DEFINITION_CONTEXT");
  }

  private StepProductDefinitionContext resolveProductDefinitionContext(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 3);
    boolean stepOrder =
        unwrapTyped(definition.parameters().get(1)) instanceof StepValue.ReferenceValue;
    return new StepProductDefinitionContext(
        instance.id(),
        stringValue(instance, definition, 0),
        stepOrder ? stringValue(instance, definition, 2) : stringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, stepOrder ? 1 : 2),
            StepApplicationContext.class,
            entityName + " frame_of_reference must reference APPLICATION_CONTEXT"),
        entityName);
  }

  private StepProductDefinition resolveProductDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRODUCT_DEFINITION");
    requireParameterCount(instance, definition, 4);
    return new StepProductDefinition(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepProductDefinitionFormation.class,
            "PRODUCT_DEFINITION formation must reference PRODUCT_DEFINITION_FORMATION"),
        requireEntity(
            referenceId(instance, definition, 3),
            StepProductDefinitionContext.class,
            "PRODUCT_DEFINITION frame_of_reference must reference PRODUCT_DEFINITION_CONTEXT"));
  }

  private StepProductDefinitionRelationship resolveProductDefinitionRelationship(
      StepEntityInstance instance) {
    return resolveProductDefinitionRelationship(instance, "PRODUCT_DEFINITION_RELATIONSHIP");
  }

  private StepProductDefinitionRelationship resolveProductDefinitionRelationship(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 5);
    return new StepProductDefinitionRelationship(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        optionalStringValue(instance, definition, 2),
        requireEntity(
            referenceId(instance, definition, 3),
            StepProductDefinition.class,
            entityName + " relating_product_definition must reference PRODUCT_DEFINITION"),
        requireEntity(
            referenceId(instance, definition, 4),
            StepProductDefinition.class,
            entityName + " related_product_definition must reference PRODUCT_DEFINITION"),
        entityName);
  }

  private StepProductDefinitionRelationshipRelationship
      resolveProductDefinitionRelationshipRelationship(StepEntityInstance instance) {
    return resolveProductDefinitionRelationshipRelationship(
        instance, "PRODUCT_DEFINITION_RELATIONSHIP_RELATIONSHIP");
  }

  private StepProductDefinitionRelationshipRelationship resolveProductDefinitionRelationshipRelationship(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 5);
    return new StepProductDefinitionRelationshipRelationship(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        optionalStringValue(instance, definition, 2),
        requireEntity(
            referenceId(instance, definition, 3),
            StepProductDefinitionRelationship.class,
            entityName + " relating_product_definition_relationship must reference PRODUCT_DEFINITION_RELATIONSHIP"),
        requireEntity(
            referenceId(instance, definition, 4),
            StepProductDefinitionRelationship.class,
            entityName + " related_product_definition_relationship must reference PRODUCT_DEFINITION_RELATIONSHIP"),
        entityName);
  }

  private StepProductDefinitionShape resolveProductDefinitionShape(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRODUCT_DEFINITION_SHAPE");
    requireParameterCount(instance, definition, 3);
    StepEntity resolvedDefinition = resolve(referenceId(instance, definition, 2));
    if (!(resolvedDefinition instanceof StepProductDefinition)
        && !(resolvedDefinition instanceof StepNextAssemblyUsageOccurrence)) {
      throw new StepResolutionException(
          "PRODUCT_DEFINITION_SHAPE definition must reference PRODUCT_DEFINITION or NEXT_ASSEMBLY_USAGE_OCCURRENCE"
              + " but got "
              + resolvedDefinition.getClass().getSimpleName());
    }
    return new StepProductDefinitionShape(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        resolvedDefinition);
  }

  private StepPropertyDefinition resolvePropertyDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PROPERTY_DEFINITION");
    requireParameterCount(instance, definition, 3);
    return new StepPropertyDefinition(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepPropertyDefinitionRelationship resolvePropertyDefinitionRelationship(
      StepEntityInstance instance) {
    return resolvePropertyDefinitionRelationship(instance, "PROPERTY_DEFINITION_RELATIONSHIP");
  }

  private StepPropertyDefinitionRelationship resolvePropertyDefinitionRelationship(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 4);
    return new StepPropertyDefinitionRelationship(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepPropertyDefinition.class,
            entityName + " relating_property_definition must reference PROPERTY_DEFINITION"),
        requireEntity(
            referenceId(instance, definition, 3),
            StepPropertyDefinition.class,
            entityName + " related_property_definition must reference PROPERTY_DEFINITION"),
        entityName);
  }

  private StepGeneralProperty resolveGeneralProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GENERAL_PROPERTY");
    requireParameterCount(instance, definition, 3);
    return new StepGeneralProperty(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        optionalStringValue(instance, definition, 2));
  }

  private StepGeneralPropertyRelationship resolveGeneralPropertyRelationship(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GENERAL_PROPERTY_RELATIONSHIP");
    requireParameterCount(instance, definition, 4);
    return new StepGeneralPropertyRelationship(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepGeneralProperty.class,
            "GENERAL_PROPERTY_RELATIONSHIP relating_general_property must reference GENERAL_PROPERTY"),
        requireEntity(
            referenceId(instance, definition, 3),
            StepGeneralProperty.class,
            "GENERAL_PROPERTY_RELATIONSHIP related_general_property must reference GENERAL_PROPERTY"));
  }

  private StepGroup resolveGroup(StepEntityInstance instance) {
    return resolveGroup(instance, "GROUP");
  }

  private StepGroup resolveGroup(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 2);
    return new StepGroup(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        entityName);
  }

  private StepGroupRelationship resolveGroupRelationship(StepEntityInstance instance) {
    return resolveGroupRelationship(instance, "GROUP_RELATIONSHIP");
  }

  private StepGroupRelationship resolveGroupRelationship(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 4);
    return new StepGroupRelationship(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepGroup.class,
            entityName + " relating_group must reference GROUP"),
        requireEntity(
            referenceId(instance, definition, 3),
            StepGroup.class,
            entityName + " related_group must reference GROUP"),
        entityName);
  }

  private StepGroupAssignment resolveGroupAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GROUP_ASSIGNMENT");
    requireParameterCount(instance, definition, 1);
    return new StepGroupAssignment(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepGroup.class,
            "GROUP_ASSIGNMENT assigned_group must reference GROUP"));
  }

  private StepAppliedGroupAssignment resolveAppliedGroupAssignment(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "APPLIED_GROUP_ASSIGNMENT");
    requireParameterCount(instance, definition, 2);
    return new StepAppliedGroupAssignment(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepGroup.class,
            "APPLIED_GROUP_ASSIGNMENT assigned_group must reference GROUP"),
        entityReferenceList(
            instance,
            definition,
            1,
            "APPLIED_GROUP_ASSIGNMENT items must contain entity references"));
  }

  private StepAddress resolveAddress(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ADDRESS");
    requireParameterCount(instance, definition, 12);
    return new StepAddress(
        instance.id(),
        optionalStringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        optionalStringValue(instance, definition, 2),
        optionalStringValue(instance, definition, 3),
        optionalStringValue(instance, definition, 4),
        optionalStringValue(instance, definition, 5),
        optionalStringValue(instance, definition, 6),
        optionalStringValue(instance, definition, 7),
        optionalStringValue(instance, definition, 8),
        optionalStringValue(instance, definition, 9),
        optionalStringValue(instance, definition, 10),
        optionalStringValue(instance, definition, 11));
  }

  private StepDocumentType resolveDocumentType(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DOCUMENT_TYPE");
    requireParameterCount(instance, definition, 1);
    return new StepDocumentType(instance.id(), stringValue(instance, definition, 0));
  }

  private StepDocument resolveDocument(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DOCUMENT");
    requireParameterCount(instance, definition, 4);
    return new StepDocument(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        optionalStringValue(instance, definition, 2),
        requireEntity(
            referenceId(instance, definition, 3),
            StepDocumentType.class,
            "DOCUMENT kind must reference DOCUMENT_TYPE"));
  }

  private StepDocumentRelationship resolveDocumentRelationship(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DOCUMENT_RELATIONSHIP");
    requireParameterCount(instance, definition, 4);
    return new StepDocumentRelationship(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepDocument.class,
            "DOCUMENT_RELATIONSHIP relating_document must reference DOCUMENT"),
        requireEntity(
            referenceId(instance, definition, 3),
            StepDocument.class,
            "DOCUMENT_RELATIONSHIP related_document must reference DOCUMENT"));
  }

  private StepDocumentUsageConstraint resolveDocumentUsageConstraint(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DOCUMENT_USAGE_CONSTRAINT");
    requireParameterCount(instance, definition, 3);
    return new StepDocumentUsageConstraint(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepDocument.class,
            "DOCUMENT_USAGE_CONSTRAINT source must reference DOCUMENT"),
        stringValue(instance, definition, 1),
        stringValue(instance, definition, 2));
  }

  private StepDocumentReference resolveDocumentReference(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DOCUMENT_REFERENCE");
    requireParameterCount(instance, definition, 2);
    return new StepDocumentReference(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepDocument.class,
            "DOCUMENT_REFERENCE assigned_document must reference DOCUMENT"),
        optionalStringValue(instance, definition, 1));
  }

  private StepAppliedDocumentReference resolveAppliedDocumentReference(
      StepEntityInstance instance) {
    return resolveAppliedDocumentReference(instance, "APPLIED_DOCUMENT_REFERENCE");
  }

  private StepAppliedDocumentReference resolveAppliedDocumentReference(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 3);
    return new StepAppliedDocumentReference(
        instance.id(),
        entityName,
        requireEntity(
            referenceId(instance, definition, 0),
            StepDocument.class,
            entityName + " assigned_document must reference DOCUMENT"),
        optionalStringValue(instance, definition, 1),
        entityReferenceList(
            instance,
            definition,
            2,
            entityName + " items must contain entity references"));
  }

  private StepPerson resolvePerson(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PERSON");
    requireParameterCount(instance, definition, 6);
    return new StepPerson(
        instance.id(),
        optionalStringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        optionalStringValue(instance, definition, 2),
        optionalStringListValue(instance, definition, 3),
        optionalStringListValue(instance, definition, 4),
        optionalStringListValue(instance, definition, 5));
  }

  private StepOrganization resolveOrganization(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ORGANIZATION");
    requireParameterCount(instance, definition, 3);
    return new StepOrganization(
        instance.id(),
        optionalStringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        optionalStringValue(instance, definition, 2));
  }

  private StepPersonAndOrganization resolvePersonAndOrganization(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PERSON_AND_ORGANIZATION");
    requireParameterCount(instance, definition, 2);
    return new StepPersonAndOrganization(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepPerson.class,
            "PERSON_AND_ORGANIZATION person must reference PERSON"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepOrganization.class,
            "PERSON_AND_ORGANIZATION organization must reference ORGANIZATION"));
  }

  private StepOrganizationRelationship resolveOrganizationRelationship(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ORGANIZATION_RELATIONSHIP");
    requireParameterCount(instance, definition, 4);
    return new StepOrganizationRelationship(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepOrganization.class,
            "ORGANIZATION_RELATIONSHIP relating_organization must reference ORGANIZATION"),
        requireEntity(
            referenceId(instance, definition, 3),
            StepOrganization.class,
            "ORGANIZATION_RELATIONSHIP related_organization must reference ORGANIZATION"));
  }

  private StepOrganizationRole resolveOrganizationRole(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ORGANIZATION_ROLE");
    requireParameterCount(instance, definition, 1);
    return new StepOrganizationRole(instance.id(), stringValue(instance, definition, 0));
  }

  private StepOrganizationAssignment resolveOrganizationAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ORGANIZATION_ASSIGNMENT");
    requireParameterCount(instance, definition, 2);
    return new StepOrganizationAssignment(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepOrganization.class,
            "ORGANIZATION_ASSIGNMENT assigned_organization must reference ORGANIZATION"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepOrganizationRole.class,
            "ORGANIZATION_ASSIGNMENT role must reference ORGANIZATION_ROLE"));
  }

  private StepAppliedOrganizationAssignment resolveAppliedOrganizationAssignment(
      StepEntityInstance instance) {
    return resolveAppliedOrganizationAssignment(instance, "APPLIED_ORGANIZATION_ASSIGNMENT");
  }

  private StepAppliedOrganizationAssignment resolveAppliedOrganizationAssignment(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 3);
    return new StepAppliedOrganizationAssignment(
        instance.id(),
        entityName,
        requireEntity(
            referenceId(instance, definition, 0),
            StepOrganization.class,
            entityName + " assigned_organization must reference ORGANIZATION"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepOrganizationRole.class,
            entityName + " role must reference ORGANIZATION_ROLE"),
        entityReferenceList(
            instance,
            definition,
            2,
            entityName + " items must contain entity references"));
  }

  private StepLanguage resolveLanguage(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LANGUAGE");
    requireParameterCount(instance, definition, 1);
    return new StepLanguage(instance.id(), stringValue(instance, definition, 0));
  }

  private StepEntity resolveLanguageAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LANGUAGE_ASSIGNMENT");
    if (definition.parameters().size() == 3) {
      return resolveRepresentation(instance, "LANGUAGE_ASSIGNMENT", false);
    }
    requireParameterCount(instance, definition, 1);
    return new StepLanguageAssignment(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepLanguage.class,
            "LANGUAGE_ASSIGNMENT assigned_language must reference LANGUAGE"));
  }

  private StepAppliedLanguageAssignment resolveAppliedLanguageAssignment(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "APPLIED_LANGUAGE_ASSIGNMENT");
    requireParameterCount(instance, definition, 2);
    return new StepAppliedLanguageAssignment(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepLanguage.class,
            "APPLIED_LANGUAGE_ASSIGNMENT assigned_language must reference LANGUAGE"),
        entityReferenceList(
            instance,
            definition,
            1,
            "APPLIED_LANGUAGE_ASSIGNMENT items must contain entity references"));
  }

  private StepPersonAndOrganizationRole resolvePersonAndOrganizationRole(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PERSON_AND_ORGANIZATION_ROLE");
    requireParameterCount(instance, definition, 1);
    return new StepPersonAndOrganizationRole(instance.id(), stringValue(instance, definition, 0));
  }

  private StepPersonAndOrganizationAssignment resolvePersonAndOrganizationAssignment(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PERSON_AND_ORGANIZATION_ASSIGNMENT");
    requireParameterCount(instance, definition, 2);
    return new StepPersonAndOrganizationAssignment(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepPersonAndOrganization.class,
            "PERSON_AND_ORGANIZATION_ASSIGNMENT assigned_person_and_organization must reference PERSON_AND_ORGANIZATION"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepPersonAndOrganizationRole.class,
            "PERSON_AND_ORGANIZATION_ASSIGNMENT role must reference PERSON_AND_ORGANIZATION_ROLE"));
  }

  private StepAppliedPersonAndOrganizationAssignment
      resolveAppliedPersonAndOrganizationAssignment(StepEntityInstance instance) {
    return resolveAppliedPersonAndOrganizationAssignment(
        instance, "APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT");
  }

  private StepAppliedPersonAndOrganizationAssignment
      resolveAppliedPersonAndOrganizationAssignment(
          StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 3);
    return new StepAppliedPersonAndOrganizationAssignment(
        instance.id(),
        entityName,
        requireEntity(
            referenceId(instance, definition, 0),
            StepPersonAndOrganization.class,
            entityName + " assigned_person_and_organization must reference PERSON_AND_ORGANIZATION"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepPersonAndOrganizationRole.class,
            entityName + " role must reference PERSON_AND_ORGANIZATION_ROLE"),
        entityReferenceList(
            instance,
            definition,
            2,
            entityName + " items must contain entity references"));
  }

  private StepCalendarDate resolveCalendarDate(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CALENDAR_DATE");
    requireParameterCount(instance, definition, 3);
    return new StepCalendarDate(
        instance.id(),
        integerValue(instance, definition, 0),
        integerValue(instance, definition, 1),
        integerValue(instance, definition, 2));
  }

  private StepCoordinatedUniversalTimeOffset resolveCoordinatedUniversalTimeOffset(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COORDINATED_UNIVERSAL_TIME_OFFSET");
    requireParameterCount(instance, definition, 3);
    return new StepCoordinatedUniversalTimeOffset(
        instance.id(),
        integerValue(instance, definition, 0),
        optionalIntegerValue(instance, definition, 1),
        enumValue(instance, definition, 2));
  }

  private StepLocalTime resolveLocalTime(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LOCAL_TIME");
    requireParameterCount(instance, definition, 4);
    return new StepLocalTime(
        instance.id(),
        integerValue(instance, definition, 0),
        integerValue(instance, definition, 1),
        optionalNumberValue(instance, definition, 2),
        requireEntity(
            referenceId(instance, definition, 3),
            StepCoordinatedUniversalTimeOffset.class,
            "LOCAL_TIME zone must reference COORDINATED_UNIVERSAL_TIME_OFFSET"));
  }

  private StepDateAndTime resolveDateAndTime(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DATE_AND_TIME");
    requireParameterCount(instance, definition, 2);
    return new StepDateAndTime(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepCalendarDate.class,
            "DATE_AND_TIME date_component must reference CALENDAR_DATE"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepLocalTime.class,
            "DATE_AND_TIME time_component must reference LOCAL_TIME"));
  }

  private StepDateRole resolveDateRole(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DATE_ROLE");
    requireParameterCount(instance, definition, 1);
    return new StepDateRole(instance.id(), stringValue(instance, definition, 0));
  }

  private StepDateAssignment resolveDateAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DATE_ASSIGNMENT");
    requireParameterCount(instance, definition, 2);
    return new StepDateAssignment(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepCalendarDate.class,
            "DATE_ASSIGNMENT assigned_date must reference CALENDAR_DATE"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepDateRole.class,
            "DATE_ASSIGNMENT role must reference DATE_ROLE"));
  }

  private StepAppliedDateAssignment resolveAppliedDateAssignment(StepEntityInstance instance) {
    return resolveAppliedDateAssignment(instance, "APPLIED_DATE_ASSIGNMENT");
  }

  private StepAppliedDateAssignment resolveAppliedDateAssignment(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 3);
    return new StepAppliedDateAssignment(
        instance.id(),
        entityName,
        requireEntity(
            referenceId(instance, definition, 0),
            StepCalendarDate.class,
            entityName + " assigned_date must reference CALENDAR_DATE"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepDateRole.class,
            entityName + " role must reference DATE_ROLE"),
        entityReferenceList(
            instance,
            definition,
            2,
            entityName + " items must contain entity references"));
  }

  private StepDateTimeRole resolveDateTimeRole(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DATE_TIME_ROLE");
    requireParameterCount(instance, definition, 1);
    return new StepDateTimeRole(instance.id(), stringValue(instance, definition, 0));
  }

  private StepDateTimeAssignment resolveDateTimeAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DATE_TIME_ASSIGNMENT");
    requireParameterCount(instance, definition, 2);
    return new StepDateTimeAssignment(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepDateAndTime.class,
            "DATE_TIME_ASSIGNMENT assigned_date_and_time must reference DATE_AND_TIME"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepDateTimeRole.class,
            "DATE_TIME_ASSIGNMENT role must reference DATE_TIME_ROLE"));
  }

  private StepAppliedDateTimeAssignment resolveAppliedDateTimeAssignment(
      StepEntityInstance instance) {
    return resolveAppliedDateTimeAssignment(instance, "APPLIED_DATE_AND_TIME_ASSIGNMENT");
  }

  private StepAppliedDateTimeAssignment resolveAppliedDateTimeAssignment(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 3);
    return new StepAppliedDateTimeAssignment(
        instance.id(),
        entityName,
        requireEntity(
            referenceId(instance, definition, 0),
            StepDateAndTime.class,
            entityName + " assigned_date_and_time must reference DATE_AND_TIME"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepDateTimeRole.class,
            entityName + " role must reference DATE_TIME_ROLE"),
        entityReferenceList(
            instance,
            definition,
            2,
            entityName + " items must contain entity references"));
  }

  private StepApprovalStatus resolveApprovalStatus(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "APPROVAL_STATUS");
    requireParameterCount(instance, definition, 1);
    return new StepApprovalStatus(instance.id(), stringValue(instance, definition, 0));
  }

  private StepApproval resolveApproval(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "APPROVAL");
    requireParameterCount(instance, definition, 2);
    return new StepApproval(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepApprovalStatus.class,
            "APPROVAL status must reference APPROVAL_STATUS"),
        optionalStringValue(instance, definition, 1));
  }

  private StepApprovalRole resolveApprovalRole(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "APPROVAL_ROLE");
    requireParameterCount(instance, definition, 1);
    return new StepApprovalRole(instance.id(), stringValue(instance, definition, 0));
  }

  private StepApprovalAssignment resolveApprovalAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "APPROVAL_ASSIGNMENT");
    requireParameterCount(instance, definition, 1);
    return new StepApprovalAssignment(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepApproval.class,
            "APPROVAL_ASSIGNMENT assigned_approval must reference APPROVAL"));
  }

  private StepAppliedApprovalAssignment resolveAppliedApprovalAssignment(
      StepEntityInstance instance) {
    return resolveAppliedApprovalAssignment(instance, "APPLIED_APPROVAL_ASSIGNMENT");
  }

  private StepAppliedApprovalAssignment resolveAppliedApprovalAssignment(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 2);
    return new StepAppliedApprovalAssignment(
        instance.id(),
        entityName,
        requireEntity(
            referenceId(instance, definition, 0),
            StepApproval.class,
            entityName + " assigned_approval must reference APPROVAL"),
        entityReferenceList(
            instance,
            definition,
            1,
            entityName + " items must contain entity references"));
  }

  private StepApprovalPersonOrganization resolveApprovalPersonOrganization(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "APPROVAL_PERSON_ORGANIZATION");
    requireParameterCount(instance, definition, 3);
    return new StepApprovalPersonOrganization(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepPersonAndOrganization.class,
            "APPROVAL_PERSON_ORGANIZATION person_organization must reference PERSON_AND_ORGANIZATION"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepApproval.class,
            "APPROVAL_PERSON_ORGANIZATION authorized_approval must reference APPROVAL"),
        requireEntity(
            referenceId(instance, definition, 2),
            StepApprovalRole.class,
            "APPROVAL_PERSON_ORGANIZATION role must reference APPROVAL_ROLE"));
  }

  private StepApprovalDateTime resolveApprovalDateTime(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "APPROVAL_DATE_TIME");
    requireParameterCount(instance, definition, 2);
    return new StepApprovalDateTime(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepDateAndTime.class,
            "APPROVAL_DATE_TIME date_time must reference DATE_AND_TIME"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepApproval.class,
            "APPROVAL_DATE_TIME dated_approval must reference APPROVAL"));
  }

  private StepSecurityClassificationLevel resolveSecurityClassificationLevel(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SECURITY_CLASSIFICATION_LEVEL");
    requireParameterCount(instance, definition, 1);
    return new StepSecurityClassificationLevel(instance.id(), stringValue(instance, definition, 0));
  }

  private StepSecurityClassification resolveSecurityClassification(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SECURITY_CLASSIFICATION");
    requireParameterCount(instance, definition, 3);
    return new StepSecurityClassification(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepSecurityClassificationLevel.class,
            "SECURITY_CLASSIFICATION security_level must reference SECURITY_CLASSIFICATION_LEVEL"));
  }

  private StepSecurityClassificationAssignment resolveSecurityClassificationAssignment(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SECURITY_CLASSIFICATION_ASSIGNMENT");
    requireParameterCount(instance, definition, 1);
    return new StepSecurityClassificationAssignment(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepSecurityClassification.class,
            "SECURITY_CLASSIFICATION_ASSIGNMENT assigned_security_classification must reference SECURITY_CLASSIFICATION"));
  }

  private StepAppliedSecurityClassificationAssignment
      resolveAppliedSecurityClassificationAssignment(StepEntityInstance instance) {
    return resolveAppliedSecurityClassificationAssignment(
        instance, "APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT");
  }

  private StepAppliedSecurityClassificationAssignment
      resolveAppliedSecurityClassificationAssignment(
          StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 2);
    return new StepAppliedSecurityClassificationAssignment(
        instance.id(),
        entityName,
        requireEntity(
            referenceId(instance, definition, 0),
            StepSecurityClassification.class,
            entityName + " assigned_security_classification must reference SECURITY_CLASSIFICATION"),
        entityReferenceList(
            instance,
            definition,
            1,
            entityName + " items must contain entity references"));
  }

  private StepContractType resolveContractType(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONTRACT_TYPE");
    requireParameterCount(instance, definition, 1);
    return new StepContractType(instance.id(), stringValue(instance, definition, 0));
  }

  private StepContract resolveContract(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONTRACT");
    requireParameterCount(instance, definition, 3);
    return new StepContract(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepContractType.class,
            "CONTRACT kind must reference CONTRACT_TYPE"));
  }

  private StepContractAssignment resolveContractAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONTRACT_ASSIGNMENT");
    requireParameterCount(instance, definition, 1);
    return new StepContractAssignment(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepContract.class,
            "CONTRACT_ASSIGNMENT assigned_contract must reference CONTRACT"));
  }

  private StepAppliedContractAssignment resolveAppliedContractAssignment(
      StepEntityInstance instance) {
    return resolveAppliedContractAssignment(instance, "APPLIED_CONTRACT_ASSIGNMENT");
  }

  private StepAppliedContractAssignment resolveAppliedContractAssignment(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 2);
    return new StepAppliedContractAssignment(
        instance.id(),
        entityName,
        requireEntity(
            referenceId(instance, definition, 0),
            StepContract.class,
            entityName + " assigned_contract must reference CONTRACT"),
        entityReferenceList(
            instance,
            definition,
            1,
            entityName + " items must contain entity references"));
  }

  private StepCertificationType resolveCertificationType(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CERTIFICATION_TYPE");
    requireParameterCount(instance, definition, 1);
    return new StepCertificationType(instance.id(), stringValue(instance, definition, 0));
  }

  private StepCertification resolveCertification(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CERTIFICATION");
    requireParameterCount(instance, definition, 3);
    return new StepCertification(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepCertificationType.class,
            "CERTIFICATION kind must reference CERTIFICATION_TYPE"));
  }

  private StepCertificationAssignment resolveCertificationAssignment(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CERTIFICATION_ASSIGNMENT");
    requireParameterCount(instance, definition, 1);
    return new StepCertificationAssignment(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepCertification.class,
            "CERTIFICATION_ASSIGNMENT assigned_certification must reference CERTIFICATION"));
  }

  private StepAppliedCertificationAssignment resolveAppliedCertificationAssignment(
      StepEntityInstance instance) {
    return resolveAppliedCertificationAssignment(instance, "APPLIED_CERTIFICATION_ASSIGNMENT");
  }

  private StepAppliedCertificationAssignment resolveAppliedCertificationAssignment(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 2);
    return new StepAppliedCertificationAssignment(
        instance.id(),
        entityName,
        requireEntity(
            referenceId(instance, definition, 0),
            StepCertification.class,
            entityName + " assigned_certification must reference CERTIFICATION"),
        entityReferenceList(
            instance,
            definition,
            1,
            entityName + " items must contain entity references"));
  }

  private StepEffectivity resolveEffectivity(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "EFFECTIVITY");
    requireParameterCount(instance, definition, 1);
    return new StepEffectivity(instance.id(), stringValue(instance, definition, 0));
  }

  private StepProductDefinitionEffectivity resolveProductDefinitionEffectivity(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRODUCT_DEFINITION_EFFECTIVITY");
    requireParameterCount(instance, definition, 3);
    return new StepProductDefinitionEffectivity(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepProductDefinition.class,
            "PRODUCT_DEFINITION_EFFECTIVITY product_definition must reference PRODUCT_DEFINITION"));
  }

  private StepEffectivityRelationship resolveEffectivityRelationship(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "EFFECTIVITY_RELATIONSHIP");
    requireParameterCount(instance, definition, 4);
    return new StepEffectivityRelationship(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepEffectivity.class,
            "EFFECTIVITY_RELATIONSHIP relating_effectivity must reference EFFECTIVITY"),
        requireEntity(
            referenceId(instance, definition, 3),
            StepEffectivity.class,
            "EFFECTIVITY_RELATIONSHIP related_effectivity must reference EFFECTIVITY"));
  }

  private StepClassificationRole resolveClassificationRole(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CLASSIFICATION_ROLE");
    requireParameterCount(instance, definition, 1);
    return new StepClassificationRole(instance.id(), stringValue(instance, definition, 0));
  }

  private StepClassificationAssignment resolveClassificationAssignment(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CLASSIFICATION_ASSIGNMENT");
    requireParameterCount(instance, definition, 2);
    return new StepClassificationAssignment(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepGroup.class,
            "CLASSIFICATION_ASSIGNMENT assigned_class must reference GROUP"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepClassificationRole.class,
            "CLASSIFICATION_ASSIGNMENT role must reference CLASSIFICATION_ROLE"));
  }

  private StepAppliedClassificationAssignment resolveAppliedClassificationAssignment(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "APPLIED_CLASSIFICATION_ASSIGNMENT");
    requireParameterCount(instance, definition, 3);
    return new StepAppliedClassificationAssignment(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepGroup.class,
            "APPLIED_CLASSIFICATION_ASSIGNMENT assigned_class must reference GROUP"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepClassificationRole.class,
            "APPLIED_CLASSIFICATION_ASSIGNMENT role must reference CLASSIFICATION_ROLE"),
        entityReferenceList(
            instance,
            definition,
            2,
            "APPLIED_CLASSIFICATION_ASSIGNMENT items must contain entity references"));
  }

  private StepIdentificationRole resolveIdentificationRole(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "IDENTIFICATION_ROLE");
    requireParameterCount(instance, definition, 1);
    return new StepIdentificationRole(instance.id(), stringValue(instance, definition, 0));
  }

  private StepIdentificationAssignment resolveIdentificationAssignment(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "IDENTIFICATION_ASSIGNMENT");
    requireParameterCount(instance, definition, 2);
    return new StepIdentificationAssignment(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepIdentificationRole.class,
            "IDENTIFICATION_ASSIGNMENT role must reference IDENTIFICATION_ROLE"));
  }

  private StepAppliedIdentificationAssignment resolveAppliedIdentificationAssignment(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "APPLIED_IDENTIFICATION_ASSIGNMENT");
    requireParameterCount(instance, definition, 3);
    return new StepAppliedIdentificationAssignment(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepIdentificationRole.class,
            "APPLIED_IDENTIFICATION_ASSIGNMENT role must reference IDENTIFICATION_ROLE"),
        entityReferenceList(
            instance,
            definition,
            2,
            "APPLIED_IDENTIFICATION_ASSIGNMENT items must contain entity references"));
  }

  private StepExternalIdentificationAssignment resolveExternalIdentificationAssignment(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "EXTERNAL_IDENTIFICATION_ASSIGNMENT");
    requireParameterCount(instance, definition, 3);
    return new StepExternalIdentificationAssignment(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepIdentificationRole.class,
            "EXTERNAL_IDENTIFICATION_ASSIGNMENT role must reference IDENTIFICATION_ROLE"),
        requireEntity(
            referenceId(instance, definition, 2),
            StepExternalSource.class,
            "EXTERNAL_IDENTIFICATION_ASSIGNMENT source must reference EXTERNAL_SOURCE"));
  }

  private StepAppliedExternalIdentificationAssignment
      resolveAppliedExternalIdentificationAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition =
        definition(instance, "APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT");
    requireParameterCount(instance, definition, 4);
    return new StepAppliedExternalIdentificationAssignment(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepIdentificationRole.class,
            "APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT role must reference IDENTIFICATION_ROLE"),
        requireEntity(
            referenceId(instance, definition, 2),
            StepExternalSource.class,
            "APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT source must reference EXTERNAL_SOURCE"),
        entityReferenceList(
            instance,
            definition,
            3,
            "APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT items must contain entity references"));
  }

  private StepNameAssignment resolveNameAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "NAME_ASSIGNMENT");
    requireParameterCount(instance, definition, 1);
    return new StepNameAssignment(instance.id(), stringValue(instance, definition, 0));
  }

  private StepAppliedNameAssignment resolveAppliedNameAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "APPLIED_NAME_ASSIGNMENT");
    requireParameterCount(instance, definition, 2);
    return new StepAppliedNameAssignment(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(
            instance,
            definition,
            1,
            "APPLIED_NAME_ASSIGNMENT items must contain entity references"));
  }

  private StepDescriptionAttribute resolveDescriptionAttribute(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DESCRIPTION_ATTRIBUTE");
    requireParameterCount(instance, definition, 2);
    return new StepDescriptionAttribute(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  private StepNameAttribute resolveNameAttribute(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "NAME_ATTRIBUTE");
    requireParameterCount(instance, definition, 2);
    return new StepNameAttribute(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  private StepIdAttribute resolveIdAttribute(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ID_ATTRIBUTE");
    requireParameterCount(instance, definition, 2);
    return new StepIdAttribute(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  private StepExternalSource resolveExternalSource(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "EXTERNAL_SOURCE");
    requireParameterCount(instance, definition, 1);
    return new StepExternalSource(instance.id(), stringValue(instance, definition, 0));
  }

  private StepExternalSourceRelationship resolveExternalSourceRelationship(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "EXTERNAL_SOURCE_RELATIONSHIP");
    requireParameterCount(instance, definition, 4);
    return new StepExternalSourceRelationship(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepExternalSource.class,
            "EXTERNAL_SOURCE_RELATIONSHIP relating_source must reference EXTERNAL_SOURCE"),
        requireEntity(
            referenceId(instance, definition, 3),
            StepExternalSource.class,
            "EXTERNAL_SOURCE_RELATIONSHIP related_source must reference EXTERNAL_SOURCE"));
  }

  private StepExternallyDefinedItem resolveExternallyDefinedItem(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 2);
    return new StepExternallyDefinedItem(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepExternalSource.class,
            entityName + " source must reference EXTERNAL_SOURCE"),
        entityName);
  }

  private StepCharacterizedObject resolveCharacterizedObject(StepEntityInstance instance) {
    return resolveCharacterizedObject(instance, "CHARACTERIZED_OBJECT");
  }

  private StepCharacterizedObject resolveCharacterizedObject(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 2);
    return new StepCharacterizedObject(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        entityName);
  }

  private StepShapeAspect resolveShapeAspect(StepEntityInstance instance) {
    return resolveShapeAspect(instance, "SHAPE_ASPECT");
  }

  private StepShapeAspect resolveShapeAspect(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 4);
    String productDefinitional = enumValue(instance, definition, 3);
    if (!List.of("T", "F", "U").contains(productDefinitional)) {
      throw new StepResolutionException(
          entityName + " product_definitional must be .T., .F. or .U.");
    }
    return new StepShapeAspect(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepProductDefinitionShape.class,
            entityName + " of_shape must reference PRODUCT_DEFINITION_SHAPE"),
        productDefinitional,
        entityName);
  }

  private StepShapeAspectOccurrence resolveShapeAspectOccurrence(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 5);
    String productDefinitional = enumValue(instance, definition, 3);
    if (!List.of("T", "F", "U").contains(productDefinitional)) {
      throw new StepResolutionException(
          entityName + " product_definitional must be .T., .F. or .U.");
    }
    return new StepShapeAspectOccurrence(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepProductDefinitionShape.class,
            entityName + " of_shape must reference PRODUCT_DEFINITION_SHAPE"),
        productDefinitional,
        resolve(referenceId(instance, definition, 4)),
        entityName);
  }

  private StepShapeAspectRelationship resolveShapeAspectRelationship(StepEntityInstance instance) {
    return resolveShapeAspectRelationship(instance, "SHAPE_ASPECT_RELATIONSHIP");
  }

  private StepShapeAspectRelationship resolveShapeAspectRelationship(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 4);
    return new StepShapeAspectRelationship(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        entityName);
  }

  private StepShapeDefinitionRepresentation resolveShapeDefinitionRepresentation(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SHAPE_DEFINITION_REPRESENTATION");
    requireParameterCount(instance, definition, 2);
    return new StepShapeDefinitionRepresentation(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepProductDefinitionShape.class,
            "SHAPE_DEFINITION_REPRESENTATION definition must reference PRODUCT_DEFINITION_SHAPE"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentation.class,
            "SHAPE_DEFINITION_REPRESENTATION used_representation must reference SHAPE_REPRESENTATION"));
  }

  private StepPropertyDefinitionRepresentation resolvePropertyDefinitionRepresentation(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PROPERTY_DEFINITION_REPRESENTATION");
    requireParameterCount(instance, definition, 2);
    return new StepPropertyDefinitionRepresentation(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepPropertyDefinition.class,
            "PROPERTY_DEFINITION_REPRESENTATION definition must reference PROPERTY_DEFINITION"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentation.class,
            "PROPERTY_DEFINITION_REPRESENTATION used_representation must reference REPRESENTATION"));
  }

  private StepAbstractVariable resolveAbstractVariable(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ABSTRACT_VARIABLE");
    requireParameterCount(instance, definition, 2);
    return new StepAbstractVariable(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepPropertyDefinition.class,
            "ABSTRACT_VARIABLE definition must reference PROPERTY_DEFINITION"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentation.class,
            "ABSTRACT_VARIABLE used_representation must reference REPRESENTATION"));
  }

  private StepRowVariable resolveRowVariable(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ROW_VARIABLE");
    requireParameterCount(instance, definition, 2);
    return new StepRowVariable(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepPropertyDefinition.class,
            "ROW_VARIABLE definition must reference PROPERTY_DEFINITION"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentation.class,
            "ROW_VARIABLE used_representation must reference REPRESENTATION"));
  }

  private StepScalarVariable resolveScalarVariable(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SCALAR_VARIABLE");
    requireParameterCount(instance, definition, 2);
    return new StepScalarVariable(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepPropertyDefinition.class,
            "SCALAR_VARIABLE definition must reference PROPERTY_DEFINITION"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentation.class,
            "SCALAR_VARIABLE used_representation must reference REPRESENTATION"));
  }

  private StepAttributeAssertion resolveAttributeAssertion(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ATTRIBUTE_ASSERTION");
    requireParameterCount(instance, definition, 2);
    return new StepAttributeAssertion(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepPropertyDefinition.class,
            "ATTRIBUTE_ASSERTION definition must reference PROPERTY_DEFINITION"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentation.class,
            "ATTRIBUTE_ASSERTION used_representation must reference REPRESENTATION"));
  }

  private StepForwardChainingRulePremise resolveForwardChainingRulePremise(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FORWARD_CHAINING_RULE_PREMISE");
    requireParameterCount(instance, definition, 2);
    return new StepForwardChainingRulePremise(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepPropertyDefinition.class,
            "FORWARD_CHAINING_RULE_PREMISE definition must reference PROPERTY_DEFINITION"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentation.class,
            "FORWARD_CHAINING_RULE_PREMISE used_representation must reference REPRESENTATION"));
  }

  private StepBackChainingRuleBody resolveBackChainingRuleBody(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BACK_CHAINING_RULE_BODY");
    requireParameterCount(instance, definition, 2);
    return new StepBackChainingRuleBody(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepPropertyDefinition.class,
            "BACK_CHAINING_RULE_BODY definition must reference PROPERTY_DEFINITION"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentation.class,
            "BACK_CHAINING_RULE_BODY used_representation must reference REPRESENTATION"));
  }

  private StepActionPropertyRepresentation resolveActionPropertyRepresentation(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ACTION_PROPERTY_REPRESENTATION");
    requireParameterCount(instance, definition, 2);
    return new StepActionPropertyRepresentation(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepPropertyDefinition.class,
            "ACTION_PROPERTY_REPRESENTATION definition must reference PROPERTY_DEFINITION"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentation.class,
            "ACTION_PROPERTY_REPRESENTATION used_representation must reference REPRESENTATION"));
  }

  private StepContactRatioRepresentation resolveContactRatioRepresentation(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONTACT_RATIO_REPRESENTATION");
    requireParameterCount(instance, definition, 2);
    return new StepContactRatioRepresentation(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepPropertyDefinition.class,
            "CONTACT_RATIO_REPRESENTATION definition must reference PROPERTY_DEFINITION"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentation.class,
            "CONTACT_RATIO_REPRESENTATION used_representation must reference REPRESENTATION"));
  }

  private StepKinematicPropertyDefinitionRepresentation
      resolveKinematicPropertyDefinitionRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition =
        definition(instance, "KINEMATIC_PROPERTY_DEFINITION_REPRESENTATION");
    requireParameterCount(instance, definition, 2);
    return new StepKinematicPropertyDefinitionRepresentation(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepPropertyDefinition.class,
            "KINEMATIC_PROPERTY_DEFINITION_REPRESENTATION definition must reference PROPERTY_DEFINITION"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentation.class,
            "KINEMATIC_PROPERTY_DEFINITION_REPRESENTATION used_representation must reference REPRESENTATION"));
  }

  private StepKinematicPropertyMechanismRepresentation
      resolveKinematicPropertyMechanismRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition =
        definition(instance, "KINEMATIC_PROPERTY_MECHANISM_REPRESENTATION");
    requireParameterCount(instance, definition, 2);
    return new StepKinematicPropertyMechanismRepresentation(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepPropertyDefinition.class,
            "KINEMATIC_PROPERTY_MECHANISM_REPRESENTATION definition must reference PROPERTY_DEFINITION"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentation.class,
            "KINEMATIC_PROPERTY_MECHANISM_REPRESENTATION used_representation must reference REPRESENTATION"));
  }

  private StepKinematicPropertyRepresentationRelation
      resolveKinematicPropertyRepresentationRelation(StepEntityInstance instance) {
    StepEntityDefinition definition =
        definition(instance, "KINEMATIC_PROPERTY_REPRESENTATION_RELATION");
    requireParameterCount(instance, definition, 2);
    return new StepKinematicPropertyRepresentationRelation(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepPropertyDefinition.class,
            "KINEMATIC_PROPERTY_REPRESENTATION_RELATION definition must reference PROPERTY_DEFINITION"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentation.class,
            "KINEMATIC_PROPERTY_REPRESENTATION_RELATION used_representation must reference REPRESENTATION"));
  }

  private StepKinematicPropertyTopologyRepresentation
      resolveKinematicPropertyTopologyRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition =
        definition(instance, "KINEMATIC_PROPERTY_TOPOLOGY_REPRESENTATION");
    requireParameterCount(instance, definition, 2);
    return new StepKinematicPropertyTopologyRepresentation(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepPropertyDefinition.class,
            "KINEMATIC_PROPERTY_TOPOLOGY_REPRESENTATION definition must reference PROPERTY_DEFINITION"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentation.class,
            "KINEMATIC_PROPERTY_TOPOLOGY_REPRESENTATION used_representation must reference REPRESENTATION"));
  }

  private StepPlacedDatumTargetFeature resolvePlacedDatumTargetFeature(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PLACED_DATUM_TARGET_FEATURE");
    requireParameterCount(instance, definition, 2);
    return new StepPlacedDatumTargetFeature(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepPropertyDefinition.class,
            "PLACED_DATUM_TARGET_FEATURE definition must reference PROPERTY_DEFINITION"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentation.class,
            "PLACED_DATUM_TARGET_FEATURE used_representation must reference REPRESENTATION"));
  }

  private StepResourcePropertyRepresentation resolveResourcePropertyRepresentation(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "RESOURCE_PROPERTY_REPRESENTATION");
    requireParameterCount(instance, definition, 2);
    return new StepResourcePropertyRepresentation(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepPropertyDefinition.class,
            "RESOURCE_PROPERTY_REPRESENTATION definition must reference PROPERTY_DEFINITION"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentation.class,
            "RESOURCE_PROPERTY_REPRESENTATION used_representation must reference REPRESENTATION"));
  }

  private StepRepresentationMap resolveRepresentationMap(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "REPRESENTATION_MAP");
    requireParameterCount(instance, definition, 2);
    StepEntity mappedOrigin = resolve(referenceId(instance, definition, 0));
    if (!(mappedOrigin instanceof StepAxis2Placement2D)
        && !(mappedOrigin instanceof StepAxis2Placement3D)) {
      throw new UnsupportedStepEntityException(
          "REPRESENTATION_MAP mapped_origin must reference AXIS2_PLACEMENT_2D or AXIS2_PLACEMENT_3D");
    }
    return new StepRepresentationMap(
        instance.id(),
        mappedOrigin,
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentation.class,
            "REPRESENTATION_MAP mapped_representation must reference REPRESENTATION"));
  }

  private StepSymbolRepresentationMap resolveSymbolRepresentationMap(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SYMBOL_REPRESENTATION_MAP");
    requireParameterCount(instance, definition, 2);
    StepEntity mappedOrigin = resolve(referenceId(instance, definition, 0));
    if (!(mappedOrigin instanceof StepAxis2Placement2D)
        && !(mappedOrigin instanceof StepAxis2Placement3D)) {
      throw new UnsupportedStepEntityException(
          "SYMBOL_REPRESENTATION_MAP mapped_origin must reference AXIS2_PLACEMENT_2D or AXIS2_PLACEMENT_3D");
    }
    return new StepSymbolRepresentationMap(
        instance.id(),
        mappedOrigin,
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentation.class,
            "SYMBOL_REPRESENTATION_MAP mapped_representation must reference REPRESENTATION"));
  }

  private StepMappedItem resolveMappedItem(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MAPPED_ITEM");
    requireParameterCount(instance, definition, 2);
    return new StepMappedItem(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepRepresentationMap.class,
            "MAPPED_ITEM mapping_source must reference REPRESENTATION_MAP"),
        resolve(referenceId(instance, definition, 1)));
  }

  private StepCartesianTransformationOperator resolveCartesianTransformationOperator2D(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CARTESIAN_TRANSFORMATION_OPERATOR_2D");
    requireParameterCount(instance, definition, 5);
    return new StepCartesianTransformationOperator(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalDirectionReference(
            instance,
            definition,
            1,
            "CARTESIAN_TRANSFORMATION_OPERATOR_2D axis1 must reference DIRECTION"),
        optionalDirectionReference(
            instance,
            definition,
            2,
            "CARTESIAN_TRANSFORMATION_OPERATOR_2D axis2 must reference DIRECTION"),
        requireEntity(
            referenceId(instance, definition, 3),
            StepCartesianPoint.class,
            "CARTESIAN_TRANSFORMATION_OPERATOR_2D local_origin must reference CARTESIAN_POINT"),
        optionalNumberValue(instance, definition, 4),
        null,
        "CARTESIAN_TRANSFORMATION_OPERATOR_2D");
  }

  private StepCartesianTransformationOperator resolveCartesianTransformationOperator3D(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CARTESIAN_TRANSFORMATION_OPERATOR_3D");
    requireParameterCount(instance, definition, 6);
    return new StepCartesianTransformationOperator(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalDirectionReference(
            instance,
            definition,
            1,
            "CARTESIAN_TRANSFORMATION_OPERATOR_3D axis1 must reference DIRECTION"),
        optionalDirectionReference(
            instance,
            definition,
            2,
            "CARTESIAN_TRANSFORMATION_OPERATOR_3D axis2 must reference DIRECTION"),
        requireEntity(
            referenceId(instance, definition, 3),
            StepCartesianPoint.class,
            "CARTESIAN_TRANSFORMATION_OPERATOR_3D local_origin must reference CARTESIAN_POINT"),
        optionalNumberValue(instance, definition, 4),
        optionalDirectionReference(
            instance,
            definition,
            5,
            "CARTESIAN_TRANSFORMATION_OPERATOR_3D axis3 must reference DIRECTION"),
        "CARTESIAN_TRANSFORMATION_OPERATOR_3D");
  }

  private StepUserDefinedMarker resolveUserDefinedMarker(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "USER_DEFINED_MARKER");
    requireParameterCount(instance, definition, 3);
    StepEntity mappingTarget = resolve(referenceId(instance, definition, 2));
    if (!(mappingTarget instanceof StepAxis2Placement2D)
        && !(mappingTarget instanceof StepAxis2Placement3D)) {
      throw new UnsupportedStepEntityException(
          "USER_DEFINED_MARKER mapping_target must reference AXIS2_PLACEMENT_2D or AXIS2_PLACEMENT_3D");
    }
    return new StepUserDefinedMarker(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentationMap.class,
            "USER_DEFINED_MARKER mapping_source must reference REPRESENTATION_MAP"),
        mappingTarget);
  }

  private StepUserDefinedCurveFont resolveUserDefinedCurveFont(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "USER_DEFINED_CURVE_FONT");
    requireParameterCount(instance, definition, 3);
    StepEntity mappingTarget = resolve(referenceId(instance, definition, 2));
    if (!(mappingTarget instanceof StepAxis2Placement2D)
        && !(mappingTarget instanceof StepAxis2Placement3D)) {
      throw new UnsupportedStepEntityException(
          "USER_DEFINED_CURVE_FONT mapping_target must reference AXIS2_PLACEMENT_2D or AXIS2_PLACEMENT_3D");
    }
    return new StepUserDefinedCurveFont(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentationMap.class,
            "USER_DEFINED_CURVE_FONT mapping_source must reference REPRESENTATION_MAP"),
        mappingTarget);
  }

  private StepUserDefinedTerminatorSymbol resolveUserDefinedTerminatorSymbol(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "USER_DEFINED_TERMINATOR_SYMBOL");
    requireParameterCount(instance, definition, 3);
    StepEntity mappingTarget = resolve(referenceId(instance, definition, 2));
    if (!(mappingTarget instanceof StepAxis2Placement2D)
        && !(mappingTarget instanceof StepAxis2Placement3D)) {
      throw new UnsupportedStepEntityException(
          "USER_DEFINED_TERMINATOR_SYMBOL mapping_target must reference AXIS2_PLACEMENT_2D or AXIS2_PLACEMENT_3D");
    }
    return new StepUserDefinedTerminatorSymbol(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentationMap.class,
            "USER_DEFINED_TERMINATOR_SYMBOL mapping_source must reference REPRESENTATION_MAP"),
        mappingTarget);
  }

  private StepItemDefinedTransformation resolveItemDefinedTransformation(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ITEM_DEFINED_TRANSFORMATION");
    requireParameterCount(instance, definition, 4);
    return new StepItemDefinedTransformation(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepAxis2Placement3D.class,
            "ITEM_DEFINED_TRANSFORMATION transform_item_1 must reference AXIS2_PLACEMENT_3D"),
        requireEntity(
            referenceId(instance, definition, 3),
            StepAxis2Placement3D.class,
            "ITEM_DEFINED_TRANSFORMATION transform_item_2 must reference AXIS2_PLACEMENT_3D"));
  }

  private StepRepresentationRelationshipWithTransformation
      resolveRepresentationRelationshipWithTransformation(StepEntityInstance instance) {
    StepEntityDefinition relationship = definition(instance, "REPRESENTATION_RELATIONSHIP");
    StepEntityDefinition transformation =
        definition(instance, "REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION");
    requireParameterCount(instance, relationship, 4);
    requireParameterCount(instance, transformation, 1);
    return new StepRepresentationRelationshipWithTransformation(
        instance.id(),
        stringValue(instance, relationship, 0),
        stringValue(instance, relationship, 1),
        requireEntity(
            referenceId(instance, relationship, 2),
            StepRepresentation.class,
            "REPRESENTATION_RELATIONSHIP rep_1 must reference REPRESENTATION"),
        requireEntity(
            referenceId(instance, relationship, 3),
            StepRepresentation.class,
            "REPRESENTATION_RELATIONSHIP rep_2 must reference REPRESENTATION"),
        requireEntity(
            referenceId(instance, transformation, 0),
            StepItemDefinedTransformation.class,
            "REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION transformation_operator must reference ITEM_DEFINED_TRANSFORMATION"));
  }

  private StepRepresentationRelationship resolveRepresentationRelationship(
      StepEntityInstance instance) {
    return resolveRepresentationRelationship(instance, "REPRESENTATION_RELATIONSHIP");
  }

  private StepRepresentationRelationship resolveRepresentationRelationship(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 4);
    return new StepRepresentationRelationship(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepRepresentation.class,
            entityName + " rep_1 must reference REPRESENTATION"),
        requireEntity(
            referenceId(instance, definition, 3),
            StepRepresentation.class,
            entityName + " rep_2 must reference REPRESENTATION"),
        entityName);
  }

  private StepShapeRepresentationRelationship resolveShapeRepresentationRelationship(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SHAPE_REPRESENTATION_RELATIONSHIP");
    requireParameterCount(instance, definition, 4);
    return new StepShapeRepresentationRelationship(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepRepresentation.class,
            "SHAPE_REPRESENTATION_RELATIONSHIP rep_1 must reference REPRESENTATION"),
        requireEntity(
            referenceId(instance, definition, 3),
            StepRepresentation.class,
            "SHAPE_REPRESENTATION_RELATIONSHIP rep_2 must reference REPRESENTATION"));
  }

  private StepUncertaintyMeasureWithUnit resolveUncertaintyMeasureWithUnit(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "UNCERTAINTY_MEASURE_WITH_UNIT");
    requireParameterCount(instance, definition, 4);
    return new StepUncertaintyMeasureWithUnit(
        instance.id(),
        numberValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        stringValue(instance, definition, 2),
        stringValue(instance, definition, 3));
  }

  private StepGlobalUnitAssignedContext resolveGlobalUnitAssignedContext(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GLOBAL_UNIT_ASSIGNED_CONTEXT");
    requireParameterCount(instance, definition, 1);
    return new StepGlobalUnitAssignedContext(
        instance.id(),
        entityReferenceList(
            instance,
            definition,
            0,
            "GLOBAL_UNIT_ASSIGNED_CONTEXT units must contain entity references"));
  }

  private StepGlobalUncertaintyAssignedContext resolveGlobalUncertaintyAssignedContext(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT");
    requireParameterCount(instance, definition, 1);
    return new StepGlobalUncertaintyAssignedContext(
        instance.id(),
        referenceList(
            instance,
            definition,
            0,
            StepUncertaintyMeasureWithUnit.class,
            "GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT uncertainties must contain UNCERTAINTY_MEASURE_WITH_UNIT references"));
  }

  private StepNextAssemblyUsageOccurrence resolveNextAssemblyUsageOccurrence(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "NEXT_ASSEMBLY_USAGE_OCCURRENCE");
    requireParameterCountIn(instance, definition, 5, 6);
    return new StepNextAssemblyUsageOccurrence(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        stringValue(instance, definition, 2),
        requireEntity(
            referenceId(instance, definition, 3),
            StepProductDefinition.class,
            "NEXT_ASSEMBLY_USAGE_OCCURRENCE relating_product_definition must reference PRODUCT_DEFINITION"),
        requireEntity(
            referenceId(instance, definition, 4),
            StepProductDefinition.class,
            "NEXT_ASSEMBLY_USAGE_OCCURRENCE related_product_definition must reference PRODUCT_DEFINITION"),
        definition.parameters().size() > 5 ? optionalStringValue(instance, definition, 5) : null);
  }

  private StepContextDependentShapeRepresentation resolveContextDependentShapeRepresentation(
      StepEntityInstance instance) {
    StepEntityDefinition definition =
        definition(instance, "CONTEXT_DEPENDENT_SHAPE_REPRESENTATION");
    requireParameterCount(instance, definition, 2);
    StepEntity relationship = resolve(referenceId(instance, definition, 0));
    if (!(relationship instanceof StepShapeRepresentationRelationship)
        && !(relationship instanceof StepRepresentationRelationship)
        && !(relationship instanceof StepRepresentationRelationshipWithTransformation)) {
      throw new StepResolutionException(
          "CONTEXT_DEPENDENT_SHAPE_REPRESENTATION representation_relation must reference a representation relationship"
              + " but got "
              + relationship.getClass().getSimpleName());
    }
    StepEntity representedProductRelation = resolve(referenceId(instance, definition, 1));
    if (!(representedProductRelation instanceof StepNextAssemblyUsageOccurrence)
        && !(representedProductRelation instanceof StepProductDefinitionShape)) {
      throw new StepResolutionException(
          "CONTEXT_DEPENDENT_SHAPE_REPRESENTATION represented_product_relation must reference"
              + " NEXT_ASSEMBLY_USAGE_OCCURRENCE or PRODUCT_DEFINITION_SHAPE but got "
              + representedProductRelation.getClass().getSimpleName());
    }
    return new StepContextDependentShapeRepresentation(
        instance.id(), relationship, representedProductRelation);
  }

  private StepMeasureWithUnit resolveMeasureWithUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MEASURE_WITH_UNIT");
    requireParameterCount(instance, definition, 2);
    return new StepMeasureWithUnit(
        instance.id(),
        numberValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  private StepTypedMeasureWithUnit resolveTypedMeasureWithUnit(
      StepEntityInstance instance, String entityName, String expectedUnitKind) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 2);
    StepEntity unitComponent = resolve(referenceId(instance, definition, 1));
    if (!matchesUnitKind(unitComponent, expectedUnitKind)) {
      throw new StepResolutionException(
          entityName + " unit_component must reference " + expectedUnitKind);
    }
    return new StepTypedMeasureWithUnit(
        instance.id(),
        entityName,
        numberValue(instance, definition, 0),
        unitComponent);
  }

  private StepDerivedUnitElement resolveDerivedUnitElement(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DERIVED_UNIT_ELEMENT");
    requireParameterCount(instance, definition, 2);
    return new StepDerivedUnitElement(
        instance.id(),
        resolve(referenceId(instance, definition, 0)),
        numberValue(instance, definition, 1));
  }

  private StepDerivedUnit resolveDerivedUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DERIVED_UNIT");
    requireParameterCount(instance, definition, 1);
    return new StepDerivedUnit(
        instance.id(),
        referenceList(
            instance,
            definition,
            0,
            StepDerivedUnitElement.class,
            "DERIVED_UNIT elements must contain DERIVED_UNIT_ELEMENT references"),
        "DERIVED_UNIT");
  }

  private StepGeometricRepresentationContext resolveGeometricRepresentationContext(
      StepEntityInstance instance) {
    StepEntityDefinition geometric = definition(instance, "GEOMETRIC_REPRESENTATION_CONTEXT");
    StepEntityDefinition representation = definition(instance, "REPRESENTATION_CONTEXT");
    requireParameterCount(instance, geometric, 1);
    requireParameterCount(instance, representation, 2);
    StepGlobalUnitAssignedContext globalUnits =
        instance.hasDefinition("GLOBAL_UNIT_ASSIGNED_CONTEXT")
            ? resolveGlobalUnitAssignedContext(instance)
            : null;
    StepGlobalUncertaintyAssignedContext globalUncertainty =
        instance.hasDefinition("GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT")
            ? resolveGlobalUncertaintyAssignedContext(instance)
            : null;
    return new StepGeometricRepresentationContext(
        instance.id(),
        integerValue(instance, geometric, 0),
        stringValue(instance, representation, 0),
        stringValue(instance, representation, 1),
        globalUnits,
        globalUncertainty);
  }

  private StepNamedUnit resolveNamedUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "NAMED_UNIT");
    requireParameterCount(instance, definition, 1);
    StepValue dimensions = unwrapTyped(definition.parameters().getFirst());
    if (!isUnset(dimensions)) {
      if (!(dimensions instanceof StepValue.ReferenceValue referenceValue)) {
        throw new UnsupportedStepEntityException(
            "NAMED_UNIT dimensions must be omitted, not provided or reference DIMENSIONAL_EXPONENTS");
      }
      requireEntity(
          referenceValue.id(),
          StepDimensionalExponents.class,
          "NAMED_UNIT dimensions must reference DIMENSIONAL_EXPONENTS");
    }
    return new StepNamedUnit(instance.id(), deriveUnitKind(instance));
  }

  private StepDimensionalExponents resolveDimensionalExponents(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DIMENSIONAL_EXPONENTS");
    requireParameterCount(instance, definition, 7);
    return new StepDimensionalExponents(
        instance.id(),
        numberValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3),
        numberValue(instance, definition, 4),
        numberValue(instance, definition, 5),
        numberValue(instance, definition, 6));
  }

  private StepNamedUnit resolveStandaloneUnitKind(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 0);
    return new StepNamedUnit(instance.id(), entityName);
  }

  private StepContextDependentUnit resolveContextDependentUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONTEXT_DEPENDENT_UNIT");
    requireParameterCount(instance, definition, 1);
    return new StepContextDependentUnit(
        instance.id(),
        stringValue(instance, definition, 0),
        deriveUnitKind(instance));
  }

  private StepConversionBasedUnit resolveConversionBasedUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONVERSION_BASED_UNIT");
    requireParameterCount(instance, definition, 2);
    StepEntity conversionFactor = resolve(referenceId(instance, definition, 1));
    if (!(conversionFactor instanceof StepMeasureWithUnit measureWithUnit)) {
      throw new StepResolutionException(
          "CONVERSION_BASED_UNIT conversion_factor must reference MEASURE_WITH_UNIT");
    }
    return new StepConversionBasedUnit(
        instance.id(),
        stringValue(instance, definition, 0),
        deriveUnitKind(instance),
        measureWithUnit);
  }

  private StepConversionBasedUnitWithOffset resolveConversionBasedUnitWithOffset(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONVERSION_BASED_UNIT_WITH_OFFSET");
    requireParameterCount(instance, definition, 1);
    StepConversionBasedUnit base = resolveConversionBasedUnit(instance);
    return new StepConversionBasedUnitWithOffset(
        instance.id(),
        base.name(),
        base.unitKind(),
        base.conversionFactor(),
        numberValue(instance, definition, 0));
  }

  private StepDerivedUnit resolveStandaloneDerivedUnitKind(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 0);
    return new StepDerivedUnit(instance.id(), List.of(), entityName);
  }

  private StepSiUnit resolveSiUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SI_UNIT");
    requireParameterCount(instance, definition, 2);
    String prefix = null;
    if (!isUnset(definition.parameters().get(0))) {
      prefix = enumValue(instance, definition, 0);
    }
    return new StepSiUnit(
        instance.id(), deriveUnitKind(instance), prefix, enumValue(instance, definition, 1));
  }

  private StepRepresentation resolveRepresentation(
      StepEntityInstance instance, boolean shapeRepresentation) {
    String entityName = shapeRepresentation ? "SHAPE_REPRESENTATION" : "REPRESENTATION";
    return resolveRepresentation(instance, entityName, shapeRepresentation);
  }

  private StepRepresentation resolveRepresentation(
      StepEntityInstance instance, String entityName, boolean shapeRepresentation) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 3);
    List<StepEntity> items =
        entityReferenceList(
            instance, definition, 1, entityName + " items must contain entity references");
    StepEntity context = resolve(referenceId(instance, definition, 2));
    if (!(context instanceof StepRepresentationContext)
        && !(context instanceof StepGeometricRepresentationContext)) {
      throw new StepResolutionException(
          entityName + " context must reference a representation context");
    }
    return new StepRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        items,
        context,
        shapeRepresentation,
        entityName);
  }

  private StepRepresentationItem resolveRepresentationItem(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "REPRESENTATION_ITEM");
    requireParameterCount(instance, definition, 1);
    return new StepRepresentationItem(instance.id(), stringValue(instance, definition, 0));
  }

  private StepGeometricRepresentationItem resolveGeometricRepresentationItem(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GEOMETRIC_REPRESENTATION_ITEM");
    requireParameterCount(instance, definition, 0);
    return new StepGeometricRepresentationItem(instance.id(), inheritedRepresentationItemName(instance));
  }

  private StepPoint resolvePoint(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "POINT");
    requireParameterCount(instance, definition, 0);
    return new StepPoint(instance.id(), inheritedRepresentationItemName(instance));
  }

  private StepCurve resolveCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CURVE");
    requireParameterCount(instance, definition, 0);
    return new StepCurve(instance.id(), inheritedRepresentationItemName(instance));
  }

  private StepSurface resolveSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE");
    requireParameterCount(instance, definition, 0);
    return new StepSurface(instance.id(), inheritedRepresentationItemName(instance));
  }

  private StepBoundedCurve resolveBoundedCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BOUNDED_CURVE");
    requireParameterCount(instance, definition, 0);
    return new StepBoundedCurve(instance.id(), inheritedRepresentationItemName(instance));
  }

  private StepUniformCurve resolveUniformCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "UNIFORM_CURVE");
    requireParameterCount(instance, definition, 0);
    if (!instance.hasDefinition("B_SPLINE_CURVE")) {
      return new StepUniformCurve(instance.id(), inheritedRepresentationItemName(instance));
    }
    ResolvedBSplineCurveData spline = resolveInheritedBSplineCurveData(instance);
    return new StepUniformCurve(
        instance.id(),
        spline.name(),
        spline.degree(),
        spline.controlPoints(),
        spline.curveForm(),
        spline.closedCurve(),
        spline.selfIntersect());
  }

  private StepBezierCurve resolveBezierCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BEZIER_CURVE");
    requireParameterCount(instance, definition, 0);
    if (!instance.hasDefinition("B_SPLINE_CURVE")) {
      return new StepBezierCurve(instance.id(), inheritedRepresentationItemName(instance));
    }
    ResolvedBSplineCurveData spline = resolveInheritedBSplineCurveData(instance);
    return new StepBezierCurve(
        instance.id(),
        spline.name(),
        spline.degree(),
        spline.controlPoints(),
        spline.curveForm(),
        spline.closedCurve(),
        spline.selfIntersect());
  }

  private StepPiecewiseBezierCurve resolvePiecewiseBezierCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PIECEWISE_BEZIER_CURVE");
    requireParameterCount(instance, definition, 0);
    if (!instance.hasDefinition("B_SPLINE_CURVE")) {
      return new StepPiecewiseBezierCurve(instance.id(), inheritedRepresentationItemName(instance));
    }
    ResolvedBSplineCurveData spline = resolveInheritedBSplineCurveData(instance);
    return new StepPiecewiseBezierCurve(
        instance.id(),
        spline.name(),
        spline.degree(),
        spline.controlPoints(),
        spline.curveForm(),
        spline.closedCurve(),
        spline.selfIntersect());
  }

  private StepQuasiUniformCurve resolveQuasiUniformCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "QUASI_UNIFORM_CURVE");
    requireParameterCount(instance, definition, 0);
    if (!instance.hasDefinition("B_SPLINE_CURVE")) {
      return new StepQuasiUniformCurve(instance.id(), inheritedRepresentationItemName(instance));
    }
    ResolvedBSplineCurveData spline = resolveInheritedBSplineCurveData(instance);
    return new StepQuasiUniformCurve(
        instance.id(),
        spline.name(),
        spline.degree(),
        spline.controlPoints(),
        spline.curveForm(),
        spline.closedCurve(),
        spline.selfIntersect());
  }

  private StepBoundedSurface resolveBoundedSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BOUNDED_SURFACE");
    requireParameterCount(instance, definition, 0);
    return new StepBoundedSurface(instance.id(), inheritedRepresentationItemName(instance));
  }

  private StepUniformSurface resolveUniformSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "UNIFORM_SURFACE");
    requireParameterCount(instance, definition, 0);
    if (!instance.hasDefinition("B_SPLINE_SURFACE")) {
      return new StepUniformSurface(instance.id(), inheritedRepresentationItemName(instance));
    }
    ResolvedBSplineSurfaceData surface = resolveInheritedBSplineSurfaceData(instance);
    return new StepUniformSurface(
        instance.id(),
        surface.name(),
        surface.uDegree(),
        surface.vDegree(),
        surface.controlPoints(),
        surface.surfaceForm(),
        surface.uClosed(),
        surface.vClosed(),
        surface.selfIntersect());
  }

  private StepBezierSurface resolveBezierSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BEZIER_SURFACE");
    requireParameterCount(instance, definition, 0);
    if (!instance.hasDefinition("B_SPLINE_SURFACE")) {
      return new StepBezierSurface(instance.id(), inheritedRepresentationItemName(instance));
    }
    ResolvedBSplineSurfaceData surface = resolveInheritedBSplineSurfaceData(instance);
    return new StepBezierSurface(
        instance.id(),
        surface.name(),
        surface.uDegree(),
        surface.vDegree(),
        surface.controlPoints(),
        surface.surfaceForm(),
        surface.uClosed(),
        surface.vClosed(),
        surface.selfIntersect());
  }

  private StepPiecewiseBezierSurface resolvePiecewiseBezierSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PIECEWISE_BEZIER_SURFACE");
    requireParameterCount(instance, definition, 0);
    if (!instance.hasDefinition("B_SPLINE_SURFACE")) {
      return new StepPiecewiseBezierSurface(instance.id(), inheritedRepresentationItemName(instance));
    }
    ResolvedBSplineSurfaceData surface = resolveInheritedBSplineSurfaceData(instance);
    return new StepPiecewiseBezierSurface(
        instance.id(),
        surface.name(),
        surface.uDegree(),
        surface.vDegree(),
        surface.controlPoints(),
        surface.surfaceForm(),
        surface.uClosed(),
        surface.vClosed(),
        surface.selfIntersect());
  }

  private StepQuasiUniformSurface resolveQuasiUniformSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "QUASI_UNIFORM_SURFACE");
    requireParameterCount(instance, definition, 0);
    if (!instance.hasDefinition("B_SPLINE_SURFACE")) {
      return new StepQuasiUniformSurface(instance.id(), inheritedRepresentationItemName(instance));
    }
    ResolvedBSplineSurfaceData surface = resolveInheritedBSplineSurfaceData(instance);
    return new StepQuasiUniformSurface(
        instance.id(),
        surface.name(),
        surface.uDegree(),
        surface.vDegree(),
        surface.controlPoints(),
        surface.surfaceForm(),
        surface.uClosed(),
        surface.vClosed(),
        surface.selfIntersect());
  }

  private ResolvedBSplineCurveData resolveInheritedBSplineCurveData(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "B_SPLINE_CURVE");
    requireParameterCountIn(instance, definition, 5, 6);
    boolean hasName = definition.parameters().size() == 6;
    return new ResolvedBSplineCurveData(
        hasName ? stringValue(instance, definition, 0) : inheritedRepresentationItemName(instance),
        integerValue(instance, definition, hasName ? 1 : 0),
        referenceList(
            instance,
            definition,
            hasName ? 2 : 1,
            StepCartesianPoint.class,
            "B_SPLINE_CURVE control points must reference CARTESIAN_POINT"),
        enumValue(instance, definition, hasName ? 3 : 2),
        booleanValue(instance, definition, hasName ? 4 : 3),
        booleanValue(instance, definition, hasName ? 5 : 4));
  }

  private ResolvedBSplineSurfaceData resolveInheritedBSplineSurfaceData(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "B_SPLINE_SURFACE");
    requireParameterCount(instance, definition, 7);
    return new ResolvedBSplineSurfaceData(
        inheritedRepresentationItemName(instance),
        integerValue(instance, definition, 0),
        integerValue(instance, definition, 1),
        referenceGrid(
            instance,
            definition,
            2,
            StepCartesianPoint.class,
            "B_SPLINE_SURFACE control points must reference CARTESIAN_POINT"),
        enumValue(instance, definition, 3),
        booleanValue(instance, definition, 4),
        booleanValue(instance, definition, 5),
        booleanValue(instance, definition, 6));
  }

  private StepSurfaceModel resolveSurfaceModel(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_MODEL");
    requireParameterCount(instance, definition, 0);
    return new StepSurfaceModel(instance.id(), inheritedRepresentationItemName(instance));
  }

  private StepSolidModel resolveSolidModel(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SOLID_MODEL");
    requireParameterCount(instance, definition, 0);
    return new StepSolidModel(instance.id(), inheritedRepresentationItemName(instance));
  }

  private StepCsgSolid resolveCsgSolid(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CSG_SOLID");
    requireParameterCount(instance, definition, 2);
    StepEntity treeRootExpression = resolve(referenceId(instance, definition, 1));
    if (!isBooleanOperandEntity(treeRootExpression)) {
      throw new StepResolutionException(
          "CSG_SOLID tree_root_expression must reference a supported CSG operand");
    }
    return new StepCsgSolid(instance.id(), stringValue(instance, definition, 0), treeRootExpression);
  }

  private StepSolidReplica resolveSolidReplica(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SOLID_REPLICA");
    requireParameterCount(instance, definition, 3);
    StepEntity parentSolid = resolve(referenceId(instance, definition, 1));
    if (!isBooleanOperandEntity(parentSolid) && !(parentSolid instanceof StepSolidModel)) {
      throw new StepResolutionException(
          "SOLID_REPLICA parent_solid must reference a supported solid model");
    }
    return new StepSolidReplica(
        instance.id(),
        stringValue(instance, definition, 0),
        parentSolid,
        requireEntity(
            referenceId(instance, definition, 2),
            StepCartesianTransformationOperator.class,
            "SOLID_REPLICA transformation must reference CARTESIAN_TRANSFORMATION_OPERATOR"));
  }

  private StepCsgPrimitive resolveCsgPrimitive(
      StepEntityInstance instance,
      String entityName,
      Class<? extends StepEntity> positionType,
      String positionTypeName,
      int dimensionCount) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, dimensionCount + 2);
    StepEntity position = resolve(referenceId(instance, definition, 1));
    if (!positionType.isInstance(position)) {
      throw new StepResolutionException(
          entityName + " position must reference " + positionTypeName);
    }
    List<Double> dimensions = new ArrayList<>(dimensionCount);
    for (int index = 0; index < dimensionCount; index++) {
      dimensions.add(numberValue(instance, definition, index + 2));
    }
    return new StepCsgPrimitive(
        instance.id(), stringValue(instance, definition, 0), position, dimensions, entityName);
  }

  private StepProfileDef resolveCircleProfileDef(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CIRCLE_PROFILE_DEF");
    requireParameterCount(instance, definition, 4);
    return new StepProfileDef(
        instance.id(),
        enumValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepAxis2Placement2D.class,
            "CIRCLE_PROFILE_DEF position must reference AXIS2_PLACEMENT_2D"),
        List.of(),
        List.of(numberValue(instance, definition, 3)),
        "CIRCLE_PROFILE_DEF");
  }

  private StepProfileDef resolveRectangleProfileDef(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "RECTANGLE_PROFILE_DEF");
    requireParameterCount(instance, definition, 5);
    return new StepProfileDef(
        instance.id(),
        enumValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepAxis2Placement2D.class,
            "RECTANGLE_PROFILE_DEF position must reference AXIS2_PLACEMENT_2D"),
        List.of(),
        List.of(numberValue(instance, definition, 3), numberValue(instance, definition, 4)),
        "RECTANGLE_PROFILE_DEF");
  }

  private StepProfileDef resolveParameterizedProfileDef(
      StepEntityInstance instance, String entityName, int parameterCount) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, parameterCount + 3);
    List<Double> parameters = new ArrayList<>(parameterCount);
    for (int index = 0; index < parameterCount; index++) {
      parameters.add(numberValue(instance, definition, index + 3));
    }
    return new StepProfileDef(
        instance.id(),
        enumValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepAxis2Placement2D.class,
            entityName + " position must reference AXIS2_PLACEMENT_2D"),
        List.of(),
        parameters,
        entityName);
  }

  private StepProfileDef resolveArbitraryClosedProfileDef(StepEntityInstance instance) {
    return resolveArbitraryProfileDef(instance, "ARBITRARY_CLOSED_PROFILE_DEF");
  }

  private StepProfileDef resolveArbitraryProfileDefWithVoids(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ARBITRARY_PROFILE_DEF_WITH_VOIDS");
    requireParameterCount(instance, definition, 4);
    StepEntity outerCurve = resolve(referenceId(instance, definition, 2));
    if (!isSupportedArbitraryProfileCurve(outerCurve)) {
      throw new StepResolutionException(
          "ARBITRARY_PROFILE_DEF_WITH_VOIDS outer_curve must reference a curve entity");
    }
    List<StepEntity> innerCurves =
        entityReferenceList(
            instance,
            definition,
            3,
            "ARBITRARY_PROFILE_DEF_WITH_VOIDS inner_curves must contain curve references");
    for (StepEntity innerCurve : innerCurves) {
      if (!isSupportedArbitraryProfileCurve(innerCurve)) {
        throw new StepResolutionException(
            "ARBITRARY_PROFILE_DEF_WITH_VOIDS inner_curves must reference curve entities");
      }
    }
    List<StepEntity> curves = new ArrayList<>(1 + innerCurves.size());
    curves.add(outerCurve);
    curves.addAll(innerCurves);
    return new StepProfileDef(
        instance.id(),
        enumValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        null,
        curves,
        List.of(),
        "ARBITRARY_PROFILE_DEF_WITH_VOIDS");
  }

  private StepProfileDef resolveArbitraryProfileDef(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 3);
    StepEntity curve = resolve(referenceId(instance, definition, 2));
    if (!isSupportedArbitraryProfileCurve(curve)) {
      throw new StepResolutionException(
          entityName + " outer_curve must reference a curve entity");
    }
    return new StepProfileDef(
        instance.id(),
        enumValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        null,
        List.of(curve),
        List.of(),
        entityName);
  }

  private StepProfileDef resolveProfileDef(StepEntityInstance instance) {
    // PROFILE_DEF is an abstract base type. Check for concrete subtypes at the same ID.
    StepEntityDefinition concrete = instance.definitions().stream()
        .filter(d -> !d.name().equals("PROFILE_DEF"))
        .filter(d -> d.name().endsWith("_PROFILE_DEF"))
        .findFirst()
        .orElse(null);
    if (concrete != null) {
      return resolveProfileDefSubtype(instance, concrete);
    }
    throw new UnsupportedStepEntityException("PROFILE_DEF is an abstract base type with no concrete subtype");
  }

  private StepProfileDef resolveProfileDefSubtype(StepEntityInstance instance, StepEntityDefinition concrete) {
    return switch (concrete.name()) {
      case "CIRCLE_PROFILE_DEF" -> resolveCircleProfileDef(instance);
      case "RECTANGLE_PROFILE_DEF" -> resolveRectangleProfileDef(instance);
      case "ARBITRARY_CLOSED_PROFILE_DEF" -> resolveArbitraryClosedProfileDef(instance);
      case "ARBITRARY_PROFILE_DEF" -> resolveArbitraryProfileDef(instance, concrete.name());
      case "ARBITRARY_PROFILE_DEF_WITH_VOIDS" -> resolveArbitraryProfileDefWithVoids(instance);
      case "PARAMETERIZED_PROFILE_DEF" -> resolveParameterizedProfileDef(instance, concrete.name(), 3);
      case "CENTERED_CIRCLE_PROFILE_DEF" -> resolveParameterizedProfileDef(instance, "CENTERED_CIRCLE_PROFILE_DEF", 2);
      case "CENTRE_LINE_ARC_PROFILE_DEF" -> resolveParameterizedProfileDef(instance, "CENTRE_LINE_ARC_PROFILE_DEF", 2);
      case "ELLIPSE_PROFILE_DEF" -> resolveParameterizedProfileDef(instance, "ELLIPSE_PROFILE_DEF", 2);
      case "L_SHAPE_PROFILE_DEF" -> resolveParameterizedProfileDef(instance, "L_SHAPE_PROFILE_DEF", 4);
      case "U_SHAPE_PROFILE_DEF" -> resolveParameterizedProfileDef(instance, "U_SHAPE_PROFILE_DEF", 5);
      case "Z_SHAPE_PROFILE_DEF" -> resolveParameterizedProfileDef(instance, "Z_SHAPE_PROFILE_DEF", 5);
      case "CHANNEL_PROFILE_DEF" -> resolveParameterizedProfileDef(instance, "CHANNEL_PROFILE_DEF", 5);
      case "T_SHAPE_PROFILE_DEF" -> resolveParameterizedProfileDef(instance, "T_SHAPE_PROFILE_DEF", 5);
      default -> throw new UnsupportedStepEntityException("PROFILE_DEF subtype " + concrete.name() + " is not a StepProfileDef");
    };
  }

  private boolean isSupportedArbitraryProfileCurve(StepEntity curve) {
    return curve instanceof StepCurve
        || curve instanceof StepPolyline
        || curve instanceof StepCompositeCurve;
  }

  private StepSweptAreaSolid resolveExtrudedAreaSolid(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "EXTRUDED_AREA_SOLID");
    requireParameterCount(instance, definition, 5);
    return new StepSweptAreaSolid(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepProfileDef.class,
            "EXTRUDED_AREA_SOLID swept_area must reference a profile definition"),
        requireEntity(
            referenceId(instance, definition, 2),
            StepAxis2Placement3D.class,
            "EXTRUDED_AREA_SOLID position must reference AXIS2_PLACEMENT_3D"),
        requireEntity(
            referenceId(instance, definition, 3),
            StepDirection.class,
            "EXTRUDED_AREA_SOLID extruded_direction must reference DIRECTION"),
        numberValue(instance, definition, 4),
        "EXTRUDED_AREA_SOLID");
  }

  private StepSweptAreaSolid resolveRevolvedAreaSolid(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "REVOLVED_AREA_SOLID");
    requireParameterCount(instance, definition, 5);
    return new StepSweptAreaSolid(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepProfileDef.class,
            "REVOLVED_AREA_SOLID swept_area must reference a profile definition"),
        requireEntity(
            referenceId(instance, definition, 2),
            StepAxis2Placement3D.class,
            "REVOLVED_AREA_SOLID position must reference AXIS2_PLACEMENT_3D"),
        requireEntity(
            referenceId(instance, definition, 3),
            StepAxis1Placement.class,
            "REVOLVED_AREA_SOLID axis must reference AXIS1_PLACEMENT"),
        numberValue(instance, definition, 4),
        "REVOLVED_AREA_SOLID");
  }

  private StepBoxDomain resolveBoxDomain(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BOX_DOMAIN");
    requireParameterCount(instance, definition, 4);
    return new StepBoxDomain(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepCartesianPoint.class,
            "BOX_DOMAIN corner must reference CARTESIAN_POINT"),
        List.of(
            numberValue(instance, definition, 1),
            numberValue(instance, definition, 2),
            numberValue(instance, definition, 3)));
  }

  private StepHalfSpaceSolid resolveHalfSpaceSolid(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "HALF_SPACE_SOLID");
    requireParameterCount(instance, definition, 3);
    return new StepHalfSpaceSolid(
        instance.id(),
        stringValue(instance, definition, 0),
        requireSurfaceReference(instance, definition, 1, "HALF_SPACE_SOLID base_surface"),
        booleanValue(instance, definition, 2),
        null,
        "HALF_SPACE_SOLID");
  }

  private StepHalfSpaceSolid resolveBoxedHalfSpace(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BOXED_HALF_SPACE");
    requireParameterCount(instance, definition, 4);
    return new StepHalfSpaceSolid(
        instance.id(),
        stringValue(instance, definition, 0),
        requireSurfaceReference(instance, definition, 1, "BOXED_HALF_SPACE base_surface"),
        booleanValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)),
        "BOXED_HALF_SPACE");
  }

  private StepEntity requireSurfaceReference(
      StepEntityInstance instance, StepEntityDefinition definition, int index, String fieldName) {
    StepEntity surface = resolve(referenceId(instance, definition, index));
    if (!(surface instanceof StepSurface)
        && !(surface instanceof StepPlane)
        && !(surface instanceof StepCylindricalSurface)
        && !(surface instanceof StepConicalSurface)
        && !(surface instanceof StepToroidalSurface)
        && !(surface instanceof StepSphericalSurface)
        && !(surface instanceof StepSurfaceOfLinearExtrusion)
        && !(surface instanceof StepSurfaceOfRevolution)
        && !(surface instanceof StepBSplineSurfaceWithKnots)
        && !(surface instanceof StepRationalBSplineSurface)) {
      throw new StepResolutionException(fieldName + " must reference a surface entity");
    }
    return surface;
  }

  private StepTopologicalRepresentationItem resolveTopologicalRepresentationItem(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TOPOLOGICAL_REPRESENTATION_ITEM");
    requireParameterCount(instance, definition, 1);
    return new StepTopologicalRepresentationItem(
        instance.id(), stringValue(instance, definition, 0));
  }

  private StepVertex resolveVertex(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "VERTEX");
    requireParameterCount(instance, definition, 0);
    return new StepVertex(instance.id(), inheritedTopologicalRepresentationItemName(instance));
  }

  private StepEdge resolveEdge(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "EDGE");
    requireParameterCount(instance, definition, 0);
    return new StepEdge(instance.id(), inheritedTopologicalRepresentationItemName(instance));
  }

  private StepFace resolveFace(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FACE");
    requireParameterCount(instance, definition, 0);
    return new StepFace(instance.id(), inheritedTopologicalRepresentationItemName(instance));
  }

  private StepColourRgb resolveColourRgb(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COLOUR_RGB");
    requireParameterCount(instance, definition, 4);
    return new StepColourRgb(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  private StepColour resolveColour(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COLOUR");
    requireParameterCount(instance, definition, 0);
    return new StepColour(instance.id());
  }

  private StepColourSpecification resolveColourSpecification(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COLOUR_SPECIFICATION");
    requireParameterCount(instance, definition, 1);
    return new StepColourSpecification(instance.id(), stringValue(instance, definition, 0));
  }

  private StepDraughtingPreDefinedCurveFont resolveDraughtingPreDefinedCurveFont(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DRAUGHTING_PRE_DEFINED_CURVE_FONT");
    requireParameterCount(instance, definition, 1);
    return new StepDraughtingPreDefinedCurveFont(
        instance.id(), stringValue(instance, definition, 0));
  }

  private StepPreDefinedCurveFont resolvePreDefinedCurveFont(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRE_DEFINED_CURVE_FONT");
    requireParameterCount(instance, definition, 1);
    return new StepPreDefinedCurveFont(instance.id(), stringValue(instance, definition, 0));
  }

  private StepPreDefinedItem resolvePreDefinedItem(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRE_DEFINED_ITEM");
    requireParameterCount(instance, definition, 1);
    return new StepPreDefinedItem(instance.id(), stringValue(instance, definition, 0));
  }

  private StepPreDefinedMarker resolvePreDefinedMarker(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRE_DEFINED_MARKER");
    requireParameterCount(instance, definition, 1);
    return new StepPreDefinedMarker(instance.id(), stringValue(instance, definition, 0));
  }

  private StepPreDefinedSymbol resolvePreDefinedSymbol(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRE_DEFINED_SYMBOL");
    requireParameterCount(instance, definition, 1);
    return new StepPreDefinedSymbol(instance.id(), stringValue(instance, definition, 0));
  }

  private StepPreDefinedPointMarkerSymbol resolvePreDefinedPointMarkerSymbol(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRE_DEFINED_POINT_MARKER_SYMBOL");
    requireParameterCount(instance, definition, 1);
    return new StepPreDefinedPointMarkerSymbol(instance.id(), stringValue(instance, definition, 0));
  }

  private StepPreDefinedDimensionSymbol resolvePreDefinedDimensionSymbol(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRE_DEFINED_DIMENSION_SYMBOL");
    requireParameterCount(instance, definition, 1);
    return new StepPreDefinedDimensionSymbol(instance.id(), stringValue(instance, definition, 0));
  }

  private StepPreDefinedGeometricalToleranceSymbol resolvePreDefinedGeometricalToleranceSymbol(
      StepEntityInstance instance) {
    StepEntityDefinition definition =
        definition(instance, "PRE_DEFINED_GEOMETRICAL_TOLERANCE_SYMBOL");
    requireParameterCount(instance, definition, 1);
    return new StepPreDefinedGeometricalToleranceSymbol(
        instance.id(), stringValue(instance, definition, 0));
  }

  private StepPreDefinedTerminatorSymbol resolvePreDefinedTerminatorSymbol(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRE_DEFINED_TERMINATOR_SYMBOL");
    requireParameterCount(instance, definition, 1);
    return new StepPreDefinedTerminatorSymbol(instance.id(), stringValue(instance, definition, 0));
  }

  private StepPreDefinedSurfaceSideStyle resolvePreDefinedSurfaceSideStyle(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRE_DEFINED_SURFACE_SIDE_STYLE");
    requireParameterCount(instance, definition, 1);
    return new StepPreDefinedSurfaceSideStyle(instance.id(), stringValue(instance, definition, 0));
  }

  private StepDraughtingPreDefinedTextFont resolveDraughtingPreDefinedTextFont(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DRAUGHTING_PRE_DEFINED_TEXT_FONT");
    requireParameterCount(instance, definition, 1);
    return new StepDraughtingPreDefinedTextFont(instance.id(), stringValue(instance, definition, 0));
  }

  private StepPreDefinedTextFont resolvePreDefinedTextFont(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRE_DEFINED_TEXT_FONT");
    requireParameterCount(instance, definition, 1);
    return new StepPreDefinedTextFont(instance.id(), stringValue(instance, definition, 0));
  }

  private StepDraughtingPreDefinedColour resolveDraughtingPreDefinedColour(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DRAUGHTING_PRE_DEFINED_COLOUR");
    requireParameterCount(instance, definition, 1);
    return new StepDraughtingPreDefinedColour(instance.id(), stringValue(instance, definition, 0));
  }

  private StepPreDefinedColour resolvePreDefinedColour(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRE_DEFINED_COLOUR");
    requireParameterCount(instance, definition, 1);
    return new StepPreDefinedColour(instance.id(), stringValue(instance, definition, 0));
  }

  private StepCurveStyle resolveCurveStyle(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CURVE_STYLE");
    requireParameterCount(instance, definition, 4);
    StepEntity font = resolve(referenceId(instance, definition, 1));
    if (!(font instanceof StepDraughtingPreDefinedCurveFont)
        && !(font instanceof StepPreDefinedCurveFont)
        && !(font instanceof StepUserDefinedCurveFont)) {
      throw new UnsupportedStepEntityException(
          "CURVE_STYLE font must reference PRE_DEFINED_CURVE_FONT, DRAUGHTING_PRE_DEFINED_CURVE_FONT or USER_DEFINED_CURVE_FONT");
    }
    StepEntity colour = resolve(referenceId(instance, definition, 3));
    if (!(colour instanceof StepColourRgb)
        && !(colour instanceof StepColourSpecification)
        && !(colour instanceof StepColour)
        && !(colour instanceof StepDraughtingPreDefinedColour)
        && !(colour instanceof StepPreDefinedColour)) {
      throw new UnsupportedStepEntityException(
          "CURVE_STYLE colour must reference COLOUR, COLOUR_SPECIFICATION, COLOUR_RGB, PRE_DEFINED_COLOUR or DRAUGHTING_PRE_DEFINED_COLOUR");
    }
    return new StepCurveStyle(
        instance.id(),
        optionalStringValue(instance, definition, 0),
        font,
        numberValue(instance, definition, 2),
        colour);
  }

  private StepPointStyle resolvePointStyle(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "POINT_STYLE");
    requireParameterCount(instance, definition, 4);
    StepEntity marker = resolve(referenceId(instance, definition, 1));
    if (!(marker instanceof StepPreDefinedPointMarkerSymbol)
        && !(marker instanceof StepPreDefinedMarker)
        && !(marker instanceof StepUserDefinedMarker)) {
      throw new UnsupportedStepEntityException(
          "POINT_STYLE marker must reference PRE_DEFINED_POINT_MARKER_SYMBOL, PRE_DEFINED_MARKER or USER_DEFINED_MARKER");
    }
    StepEntity colour = resolve(referenceId(instance, definition, 3));
    if (!(colour instanceof StepColourRgb)
        && !(colour instanceof StepColourSpecification)
        && !(colour instanceof StepColour)
        && !(colour instanceof StepDraughtingPreDefinedColour)
        && !(colour instanceof StepPreDefinedColour)) {
      throw new UnsupportedStepEntityException(
          "POINT_STYLE colour must reference COLOUR, COLOUR_SPECIFICATION, COLOUR_RGB, PRE_DEFINED_COLOUR or DRAUGHTING_PRE_DEFINED_COLOUR");
    }
    return new StepPointStyle(
        instance.id(),
        optionalStringValue(instance, definition, 0),
        marker,
        numberValue(instance, definition, 2),
        colour);
  }

  private StepCharacterGlyphStyleStroke resolveCharacterGlyphStyleStroke(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CHARACTER_GLYPH_STYLE_STROKE");
    requireParameterCount(instance, definition, 1);
    return new StepCharacterGlyphStyleStroke(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepCurveStyle.class,
            "CHARACTER_GLYPH_STYLE_STROKE stroke_style must reference CURVE_STYLE"));
  }

  private StepCharacterGlyphStyleOutline resolveCharacterGlyphStyleOutline(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CHARACTER_GLYPH_STYLE_OUTLINE");
    requireParameterCount(instance, definition, 1);
    return new StepCharacterGlyphStyleOutline(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepCurveStyle.class,
            "CHARACTER_GLYPH_STYLE_OUTLINE outline_style must reference CURVE_STYLE"));
  }

  private StepCharacterGlyphStyleOutlineWithCharacteristics
      resolveCharacterGlyphStyleOutlineWithCharacteristics(StepEntityInstance instance) {
    StepEntityDefinition definition =
        definition(instance, "CHARACTER_GLYPH_STYLE_OUTLINE_WITH_CHARACTERISTICS");
    requireParameterCount(instance, definition, 2);
    return new StepCharacterGlyphStyleOutlineWithCharacteristics(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepCurveStyle.class,
            "CHARACTER_GLYPH_STYLE_OUTLINE_WITH_CHARACTERISTICS outline_style must reference CURVE_STYLE"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepFillAreaStyle.class,
            "CHARACTER_GLYPH_STYLE_OUTLINE_WITH_CHARACTERISTICS characteristics must reference FILL_AREA_STYLE"));
  }

  private StepTextStyleForDefinedFont resolveTextStyleForDefinedFont(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TEXT_STYLE_FOR_DEFINED_FONT");
    requireParameterCount(instance, definition, 1);
    StepEntity colour = resolve(referenceId(instance, definition, 0));
    if (!(colour instanceof StepColourRgb)
        && !(colour instanceof StepColourSpecification)
        && !(colour instanceof StepColour)
        && !(colour instanceof StepDraughtingPreDefinedColour)
        && !(colour instanceof StepPreDefinedColour)) {
      throw new UnsupportedStepEntityException(
          "TEXT_STYLE_FOR_DEFINED_FONT colour must reference COLOUR, COLOUR_SPECIFICATION, COLOUR_RGB, PRE_DEFINED_COLOUR or DRAUGHTING_PRE_DEFINED_COLOUR");
    }
    return new StepTextStyleForDefinedFont(instance.id(), colour);
  }

  private StepTextStyle resolveTextStyle(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TEXT_STYLE");
    requireParameterCount(instance, definition, 2);
    StepEntity characterAppearance = resolve(referenceId(instance, definition, 1));
    if (!(characterAppearance instanceof StepTextStyleForDefinedFont)) {
      throw new UnsupportedStepEntityException(
          "TEXT_STYLE character_appearance must reference TEXT_STYLE_FOR_DEFINED_FONT");
    }
    return new StepTextStyle(
        instance.id(), stringValue(instance, definition, 0), characterAppearance);
  }

  private StepTextStyleWithSpacing resolveTextStyleWithSpacing(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TEXT_STYLE_WITH_SPACING");
    requireParameterCount(instance, definition, 3);
    StepEntity characterAppearance = resolve(referenceId(instance, definition, 1));
    if (!(characterAppearance instanceof StepTextStyleForDefinedFont)) {
      throw new UnsupportedStepEntityException(
          "TEXT_STYLE_WITH_SPACING character_appearance must reference TEXT_STYLE_FOR_DEFINED_FONT");
    }
    return new StepTextStyleWithSpacing(
        instance.id(),
        stringValue(instance, definition, 0),
        characterAppearance,
        numberValue(instance, definition, 2));
  }

  private StepTextStyleWithJustification resolveTextStyleWithJustification(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TEXT_STYLE_WITH_JUSTIFICATION");
    requireParameterCount(instance, definition, 3);
    StepEntity characterAppearance = resolve(referenceId(instance, definition, 1));
    if (!(characterAppearance instanceof StepTextStyleForDefinedFont)) {
      throw new UnsupportedStepEntityException(
          "TEXT_STYLE_WITH_JUSTIFICATION character_appearance must reference TEXT_STYLE_FOR_DEFINED_FONT");
    }
    return new StepTextStyleWithJustification(
        instance.id(),
        stringValue(instance, definition, 0),
        characterAppearance,
        enumValue(instance, definition, 2));
  }

  private StepTextStyleWithMirror resolveTextStyleWithMirror(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TEXT_STYLE_WITH_MIRROR");
    requireParameterCount(instance, definition, 3);
    StepEntity characterAppearance = resolve(referenceId(instance, definition, 1));
    if (!(characterAppearance instanceof StepTextStyleForDefinedFont)) {
      throw new UnsupportedStepEntityException(
          "TEXT_STYLE_WITH_MIRROR character_appearance must reference TEXT_STYLE_FOR_DEFINED_FONT");
    }
    StepEntity mirrorPlacement = resolve(referenceId(instance, definition, 2));
    if (!(mirrorPlacement instanceof StepAxis2Placement2D)
        && !(mirrorPlacement instanceof StepAxis2Placement3D)) {
      throw new UnsupportedStepEntityException(
          "TEXT_STYLE_WITH_MIRROR mirror_placement must reference AXIS2_PLACEMENT_2D or AXIS2_PLACEMENT_3D");
    }
    return new StepTextStyleWithMirror(
        instance.id(),
        stringValue(instance, definition, 0),
        characterAppearance,
        mirrorPlacement);
  }

  private StepTextStyleWithBoxCharacteristics resolveTextStyleWithBoxCharacteristics(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TEXT_STYLE_WITH_BOX_CHARACTERISTICS");
    requireParameterCount(instance, definition, 3);
    StepEntity characterAppearance = resolve(referenceId(instance, definition, 1));
    if (!(characterAppearance instanceof StepTextStyleForDefinedFont)) {
      throw new UnsupportedStepEntityException(
          "TEXT_STYLE_WITH_BOX_CHARACTERISTICS character_appearance must reference TEXT_STYLE_FOR_DEFINED_FONT");
    }
    List<String> boxCharacteristics = literalList(instance, definition, 2);
    if (boxCharacteristics.isEmpty()) {
      throw new StepResolutionException(
          "TEXT_STYLE_WITH_BOX_CHARACTERISTICS box_characteristics must not be empty");
    }
    if (new LinkedHashSet<>(boxCharacteristics).size() != boxCharacteristics.size()) {
      throw new StepResolutionException(
          "TEXT_STYLE_WITH_BOX_CHARACTERISTICS box_characteristics must not contain duplicate entries");
    }
    return new StepTextStyleWithBoxCharacteristics(
        instance.id(),
        stringValue(instance, definition, 0),
        characterAppearance,
        boxCharacteristics);
  }

  private StepSymbolColour resolveSymbolColour(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SYMBOL_COLOUR");
    requireParameterCount(instance, definition, 1);
    StepEntity colour = resolve(referenceId(instance, definition, 0));
    if (!(colour instanceof StepColourRgb)
        && !(colour instanceof StepColourSpecification)
        && !(colour instanceof StepColour)
        && !(colour instanceof StepDraughtingPreDefinedColour)
        && !(colour instanceof StepPreDefinedColour)) {
      throw new UnsupportedStepEntityException(
          "SYMBOL_COLOUR colour must reference COLOUR, COLOUR_SPECIFICATION, COLOUR_RGB, PRE_DEFINED_COLOUR or DRAUGHTING_PRE_DEFINED_COLOUR");
    }
    return new StepSymbolColour(instance.id(), colour);
  }

  private StepSymbolStyle resolveSymbolStyle(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SYMBOL_STYLE");
    requireParameterCount(instance, definition, 2);
    StepEntity styleOfSymbol = resolve(referenceId(instance, definition, 1));
    if (!(styleOfSymbol instanceof StepSymbolColour)) {
      throw new UnsupportedStepEntityException(
          "SYMBOL_STYLE style_of_symbol must reference SYMBOL_COLOUR");
    }
    return new StepSymbolStyle(instance.id(), stringValue(instance, definition, 0), styleOfSymbol);
  }

  private StepFillAreaStyleColour resolveFillAreaStyleColour(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FILL_AREA_STYLE_COLOUR");
    requireParameterCount(instance, definition, 2);
    StepEntity colour = resolve(referenceId(instance, definition, 1));
    if (!(colour instanceof StepColourRgb)
        && !(colour instanceof StepColourSpecification)
        && !(colour instanceof StepColour)
        && !(colour instanceof StepDraughtingPreDefinedColour)
        && !(colour instanceof StepPreDefinedColour)) {
      throw new UnsupportedStepEntityException(
          "FILL_AREA_STYLE_COLOUR colour must reference COLOUR, COLOUR_SPECIFICATION, COLOUR_RGB, PRE_DEFINED_COLOUR or DRAUGHTING_PRE_DEFINED_COLOUR");
    }
    return new StepFillAreaStyleColour(
        instance.id(), optionalStringValue(instance, definition, 0), colour);
  }

  private StepFillAreaStyle resolveFillAreaStyle(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FILL_AREA_STYLE");
    requireParameterCount(instance, definition, 2);
    return new StepFillAreaStyle(
        instance.id(),
        optionalStringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepFillAreaStyleColour.class,
            "FILL_AREA_STYLE styles must contain FILL_AREA_STYLE_COLOUR references"));
  }

  private StepSurfaceStyleFillArea resolveSurfaceStyleFillArea(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_STYLE_FILL_AREA");
    requireParameterCount(instance, definition, 1);
    return new StepSurfaceStyleFillArea(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepFillAreaStyle.class,
            "SURFACE_STYLE_FILL_AREA fill style must reference FILL_AREA_STYLE"));
  }

  private StepCurveStyle requireCurveStyleReference(
      StepEntityInstance instance, StepEntityDefinition definition, String entityName) {
    return requireEntity(
        referenceId(instance, definition, 0),
        StepCurveStyle.class,
        entityName + " style must reference CURVE_STYLE");
  }

  private StepSurfaceStyleBoundary resolveSurfaceStyleBoundary(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_STYLE_BOUNDARY");
    requireParameterCount(instance, definition, 1);
    return new StepSurfaceStyleBoundary(
        instance.id(), requireCurveStyleReference(instance, definition, "SURFACE_STYLE_BOUNDARY"));
  }

  private StepSurfaceStyleControlGrid resolveSurfaceStyleControlGrid(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_STYLE_CONTROL_GRID");
    requireParameterCount(instance, definition, 1);
    return new StepSurfaceStyleControlGrid(
        instance.id(),
        requireCurveStyleReference(instance, definition, "SURFACE_STYLE_CONTROL_GRID"));
  }

  private StepSurfaceStyleSegmentationCurve resolveSurfaceStyleSegmentationCurve(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_STYLE_SEGMENTATION_CURVE");
    requireParameterCount(instance, definition, 1);
    return new StepSurfaceStyleSegmentationCurve(
        instance.id(),
        requireCurveStyleReference(instance, definition, "SURFACE_STYLE_SEGMENTATION_CURVE"));
  }

  private StepSurfaceStyleSilhouette resolveSurfaceStyleSilhouette(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_STYLE_SILHOUETTE");
    requireParameterCount(instance, definition, 1);
    return new StepSurfaceStyleSilhouette(
        instance.id(), requireCurveStyleReference(instance, definition, "SURFACE_STYLE_SILHOUETTE"));
  }

  private StepSurfaceStyleTransparent resolveSurfaceStyleTransparent(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_STYLE_TRANSPARENT");
    requireParameterCount(instance, definition, 1);
    return new StepSurfaceStyleTransparent(instance.id(), numberValue(instance, definition, 0));
  }

  private StepSurfaceStyleReflectanceAmbient resolveSurfaceStyleReflectanceAmbient(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_STYLE_REFLECTANCE_AMBIENT");
    requireParameterCount(instance, definition, 1);
    return new StepSurfaceStyleReflectanceAmbient(instance.id(), numberValue(instance, definition, 0));
  }

  private StepSurfaceStyleReflectanceAmbientDiffuse resolveSurfaceStyleReflectanceAmbientDiffuse(
      StepEntityInstance instance) {
    StepEntityDefinition definition =
        definition(instance, "SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE");
    requireParameterCount(instance, definition, 2);
    return new StepSurfaceStyleReflectanceAmbientDiffuse(
        instance.id(),
        numberValue(instance, definition, 0),
        numberValue(instance, definition, 1));
  }

  private StepSurfaceStyleReflectanceAmbientDiffuseSpecular
      resolveSurfaceStyleReflectanceAmbientDiffuseSpecular(StepEntityInstance instance) {
    StepEntityDefinition definition =
        definition(instance, "SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE_SPECULAR");
    requireParameterCount(instance, definition, 5);
    StepEntity specularColour = resolve(referenceId(instance, definition, 4));
    if (!(specularColour instanceof StepColour)
        && !(specularColour instanceof StepColourSpecification)
        && !(specularColour instanceof StepColourRgb)
        && !(specularColour instanceof StepDraughtingPreDefinedColour)
        && !(specularColour instanceof StepPreDefinedColour)) {
      throw new UnsupportedStepEntityException(
          "SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE_SPECULAR specular_colour must reference COLOUR, COLOUR_SPECIFICATION, COLOUR_RGB, PRE_DEFINED_COLOUR or DRAUGHTING_PRE_DEFINED_COLOUR");
    }
    return new StepSurfaceStyleReflectanceAmbientDiffuseSpecular(
        instance.id(),
        numberValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3),
        specularColour);
  }

  private StepSurfaceStyleParameterLine resolveSurfaceStyleParameterLine(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_STYLE_PARAMETER_LINE");
    requireParameterCount(instance, definition, 1);
    return new StepSurfaceStyleParameterLine(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepCurveStyle.class,
            "SURFACE_STYLE_PARAMETER_LINE style must reference CURVE_STYLE"));
  }

  private StepSurfaceSideStyle resolveSurfaceSideStyle(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_SIDE_STYLE");
    requireParameterCount(instance, definition, 2);
    return new StepSurfaceSideStyle(
        instance.id(),
        optionalStringValue(instance, definition, 0),
        entityReferenceList(
                instance,
                definition,
                1,
                "SURFACE_SIDE_STYLE styles must contain SURFACE_STYLE_FILL_AREA, SURFACE_STYLE_BOUNDARY, SURFACE_STYLE_CONTROL_GRID, SURFACE_STYLE_SEGMENTATION_CURVE, SURFACE_STYLE_SILHOUETTE, SURFACE_STYLE_TRANSPARENT, SURFACE_STYLE_REFLECTANCE_AMBIENT, SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE, SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE_SPECULAR or SURFACE_STYLE_PARAMETER_LINE references")
                .stream()
                .map(
                        style -> {
                            if (!(style instanceof StepSurfaceStyleFillArea)
                                    && !(style instanceof StepSurfaceStyleBoundary)
                                    && !(style instanceof StepSurfaceStyleControlGrid)
                                    && !(style instanceof StepSurfaceStyleSegmentationCurve)
                                    && !(style instanceof StepSurfaceStyleSilhouette)
                                    && !(style instanceof StepSurfaceStyleTransparent)
                                    && !(style instanceof StepSurfaceStyleReflectanceAmbient)
                                    && !(style instanceof StepSurfaceStyleReflectanceAmbientDiffuse)
                                    && !(style instanceof StepSurfaceStyleReflectanceAmbientDiffuseSpecular)
                                    && !(style instanceof StepSurfaceStyleRendering)
                                    && !(style instanceof StepSurfaceStyleParameterLine)) {
                                throw new StepResolutionException(
                                        "SURFACE_SIDE_STYLE styles must reference SURFACE_STYLE_FILL_AREA, SURFACE_STYLE_BOUNDARY, SURFACE_STYLE_CONTROL_GRID, SURFACE_STYLE_SEGMENTATION_CURVE, SURFACE_STYLE_SILHOUETTE, SURFACE_STYLE_TRANSPARENT, SURFACE_STYLE_REFLECTANCE_AMBIENT, SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE, SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE_SPECULAR, SURFACE_STYLE_RENDERING or SURFACE_STYLE_PARAMETER_LINE");
                            }
                            return style;
                        })
                .toList());
  }

  private StepSurfaceStyleUsage resolveSurfaceStyleUsage(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_STYLE_USAGE");
    requireParameterCount(instance, definition, 2);
    return new StepSurfaceStyleUsage(
        instance.id(),
        enumValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepSurfaceSideStyle.class,
            "SURFACE_STYLE_USAGE style must reference SURFACE_SIDE_STYLE"));
  }

  private StepPresentationStyleAssignment resolvePresentationStyleAssignment(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRESENTATION_STYLE_ASSIGNMENT");
    requireParameterCount(instance, definition, 1);
    return new StepPresentationStyleAssignment(
        instance.id(),
        entityReferenceList(
            instance,
            definition,
            0,
            "PRESENTATION_STYLE_ASSIGNMENT styles must contain entity references"));
  }

  private StepStyledItem resolveStyledItem(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "STYLED_ITEM");
    requireParameterCount(instance, definition, 3);
    return new StepStyledItem(
        instance.id(),
        optionalStringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "STYLED_ITEM styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepOverRidingStyledItem resolveOverRidingStyledItem(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "OVER_RIDING_STYLED_ITEM");
    requireParameterCount(instance, definition, 4);
    return new StepOverRidingStyledItem(
        instance.id(),
        optionalStringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "OVER_RIDING_STYLED_ITEM styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        resolve(referenceId(instance, definition, 2)),
        requireEntity(
            referenceId(instance, definition, 3),
            StepStyledItem.class,
            "OVER_RIDING_STYLED_ITEM over_ridden_style must reference STYLED_ITEM"));
  }

  private StepPresentationLayerAssignment resolvePresentationLayerAssignment(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRESENTATION_LAYER_ASSIGNMENT");
    requireParameterCount(instance, definition, 3);
    return new StepPresentationLayerAssignment(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        entityReferenceList(
            instance,
            definition,
            2,
            "PRESENTATION_LAYER_ASSIGNMENT assigned items must contain entity references"));
  }

  private StepAnnotationTextOccurrence resolveAnnotationTextOccurrence(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ANNOTATION_TEXT_OCCURRENCE");
    requireParameterCount(instance, definition, 3);
    StepEntity position = resolve(referenceId(instance, definition, 2));
    if (!isSupportedAnnotationPointCarrier(position)) {
      throw new StepResolutionException(
          "ANNOTATION_TEXT_OCCURRENCE position must reference supported point carriers or point-like annotation content/occurrences");
    }
    return new StepAnnotationTextOccurrence(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        position);
  }

  private StepAnnotationText resolveAnnotationText(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ANNOTATION_TEXT");
    requireParameterCount(instance, definition, 3);
    StepEntity mappingTarget = resolve(referenceId(instance, definition, 2));
    if (!(mappingTarget instanceof StepAxis2Placement2D)
        && !(mappingTarget instanceof StepAxis2Placement3D)) {
      throw new UnsupportedStepEntityException(
          "ANNOTATION_TEXT mapping_target must reference AXIS2_PLACEMENT_2D or AXIS2_PLACEMENT_3D");
    }
    return new StepAnnotationText(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentationMap.class,
            "ANNOTATION_TEXT mapping_source must reference REPRESENTATION_MAP"),
        mappingTarget);
  }

  private StepAnnotationTextCharacter resolveAnnotationTextCharacter(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ANNOTATION_TEXT_CHARACTER");
    requireParameterCount(instance, definition, 3);
    StepEntity mappingTarget = resolve(referenceId(instance, definition, 2));
    if (!(mappingTarget instanceof StepAxis2Placement2D)
        && !(mappingTarget instanceof StepAxis2Placement3D)) {
      throw new UnsupportedStepEntityException(
          "ANNOTATION_TEXT_CHARACTER mapping_target must reference AXIS2_PLACEMENT_2D or AXIS2_PLACEMENT_3D");
    }
    return new StepAnnotationTextCharacter(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentationMap.class,
            "ANNOTATION_TEXT_CHARACTER mapping_source must reference REPRESENTATION_MAP"),
        mappingTarget);
  }

  private StepAnnotationSymbol resolveAnnotationSymbol(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ANNOTATION_SYMBOL");
    requireParameterCount(instance, definition, 3);
    StepEntity mappingTarget = resolve(referenceId(instance, definition, 2));
    if (!(mappingTarget instanceof StepAxis2Placement2D)
        && !(mappingTarget instanceof StepAxis2Placement3D)) {
      throw new UnsupportedStepEntityException(
          "ANNOTATION_SYMBOL mapping_target must reference AXIS2_PLACEMENT_2D or AXIS2_PLACEMENT_3D");
    }
    return new StepAnnotationSymbol(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepSymbolRepresentationMap.class,
            "ANNOTATION_SYMBOL mapping_source must reference SYMBOL_REPRESENTATION_MAP"),
        mappingTarget);
  }

  private StepAnnotationSymbolOccurrence resolveAnnotationSymbolOccurrence(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ANNOTATION_SYMBOL_OCCURRENCE");
    requireParameterCount(instance, definition, 3);
    StepEntity item = resolve(referenceId(instance, definition, 2));
    if (!isSupportedAnnotationWrapperItem(item)) {
      throw new StepResolutionException(
          "ANNOTATION_SYMBOL_OCCURRENCE item must reference supported annotation content or occurrence");
    }
    return new StepAnnotationSymbolOccurrence(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "ANNOTATION_SYMBOL_OCCURRENCE styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        item);
  }

  private StepAnnotationSubfigureOccurrence resolveAnnotationSubfigureOccurrence(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ANNOTATION_SUBFIGURE_OCCURRENCE");
    requireParameterCount(instance, definition, 3);
    StepEntity item = resolve(referenceId(instance, definition, 2));
    if (!isSupportedAnnotationWrapperItem(item)) {
      throw new StepResolutionException(
          "ANNOTATION_SUBFIGURE_OCCURRENCE item must reference supported annotation content or occurrence");
    }
    return new StepAnnotationSubfigureOccurrence(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "ANNOTATION_SUBFIGURE_OCCURRENCE styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        item);
  }

  private StepDraughtingAnnotationOccurrence resolveDraughtingAnnotationOccurrence(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DRAUGHTING_ANNOTATION_OCCURRENCE");
    requireParameterCount(instance, definition, 3);
    return new StepDraughtingAnnotationOccurrence(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "DRAUGHTING_ANNOTATION_OCCURRENCE styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepTerminatorSymbol resolveTerminatorSymbol(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TERMINATOR_SYMBOL");
    requireParameterCount(instance, definition, 4);
    StepEntity annotatedCurve = resolve(referenceId(instance, definition, 3));
    if (!(annotatedCurve instanceof StepAnnotationCurveOccurrence)
        && !(annotatedCurve instanceof StepLeaderCurve)
        && !(annotatedCurve instanceof StepProjectionCurve)
        && !(annotatedCurve instanceof StepDimensionCurve)) {
      throw new StepResolutionException(
          "TERMINATOR_SYMBOL annotated_curve must reference supported annotation curve occurrence");
    }
    StepEntity item = resolve(referenceId(instance, definition, 2));
    if (!isSupportedAnnotationWrapperItem(item)) {
      throw new StepResolutionException(
          "TERMINATOR_SYMBOL item must reference supported annotation content or occurrence");
    }
    return new StepTerminatorSymbol(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "TERMINATOR_SYMBOL styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        item,
        annotatedCurve);
  }

  private StepAnnotationOccurrenceRelationship resolveAnnotationOccurrenceRelationship(
      StepEntityInstance instance) {
    return resolveAnnotationOccurrenceRelationship(instance, "ANNOTATION_OCCURRENCE_RELATIONSHIP");
  }

  private StepAnnotationOccurrenceRelationship resolveAnnotationOccurrenceRelationship(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 4);
    StepEntity relating = resolve(referenceId(instance, definition, 2));
    StepEntity related = resolve(referenceId(instance, definition, 3));
    if (!isAnnotationOccurrence(relating) || !isAnnotationOccurrence(related)) {
      throw new UnsupportedStepEntityException(
          entityName + " occurrences must reference supported annotation occurrence entities");
    }
    return new StepAnnotationOccurrenceRelationship(
        instance.id(),
        entityName,
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        relating,
        related);
  }

  private StepAnnotationPointOccurrence resolveAnnotationPointOccurrence(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ANNOTATION_POINT_OCCURRENCE");
    requireParameterCount(instance, definition, 3);
    StepEntity item = resolve(referenceId(instance, definition, 2));
    if (!isSupportedAnnotationPointCarrier(item)) {
      throw new StepResolutionException(
          "ANNOTATION_POINT_OCCURRENCE item must reference supported point carriers or point-like annotation content/occurrences");
    }
    return new StepAnnotationPointOccurrence(
        instance.id(),
        optionalStringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "ANNOTATION_POINT_OCCURRENCE styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        item);
  }

  private StepAnnotationCurveOccurrence resolveAnnotationCurveOccurrence(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ANNOTATION_CURVE_OCCURRENCE");
    requireParameterCount(instance, definition, 3);
    StepEntity item = resolve(referenceId(instance, definition, 2));
    if (!isSupportedAnnotationCurveCarrier(item)) {
      throw new StepResolutionException(
          "ANNOTATION_CURVE_OCCURRENCE item must reference a supported curve, EDGE_CURVE, SUBEDGE, ORIENTED_EDGE, EDGE_LOOP, POLY_LOOP, PATH, OPEN_PATH, SUBPATH, ORIENTED_PATH, CONNECTED_EDGE_SET, WIRE_SHELL, wireframe model or GEOMETRIC_CURVE_SET");
    }
    return new StepAnnotationCurveOccurrence(
        instance.id(),
        optionalStringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "ANNOTATION_CURVE_OCCURRENCE styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        item);
  }

  private StepLeaderCurve resolveLeaderCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LEADER_CURVE");
    requireParameterCount(instance, definition, 3);
    StepEntity item = resolve(referenceId(instance, definition, 2));
    if (!isSupportedAnnotationCurveCarrier(item)) {
      throw new StepResolutionException(
          "LEADER_CURVE item must reference a supported curve, EDGE_CURVE, SUBEDGE, ORIENTED_EDGE, EDGE_LOOP, POLY_LOOP, PATH, OPEN_PATH, SUBPATH, ORIENTED_PATH, CONNECTED_EDGE_SET, WIRE_SHELL, wireframe model or GEOMETRIC_CURVE_SET");
    }
    return new StepLeaderCurve(
        instance.id(),
        optionalStringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "LEADER_CURVE styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        item);
  }

  private StepProjectionCurve resolveProjectionCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PROJECTION_CURVE");
    requireParameterCount(instance, definition, 3);
    StepEntity item = resolve(referenceId(instance, definition, 2));
    if (!isSupportedAnnotationCurveCarrier(item)) {
      throw new StepResolutionException(
          "PROJECTION_CURVE item must reference a supported curve, EDGE_CURVE, SUBEDGE, ORIENTED_EDGE, EDGE_LOOP, POLY_LOOP, PATH, OPEN_PATH, SUBPATH, ORIENTED_PATH, CONNECTED_EDGE_SET, WIRE_SHELL, wireframe model or GEOMETRIC_CURVE_SET");
    }
    return new StepProjectionCurve(
        instance.id(),
        optionalStringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "PROJECTION_CURVE styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        item);
  }

  private StepDimensionCurve resolveDimensionCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DIMENSION_CURVE");
    requireParameterCount(instance, definition, 3);
    StepEntity item = resolve(referenceId(instance, definition, 2));
    if (!isSupportedAnnotationCurveCarrier(item)) {
      throw new StepResolutionException(
          "DIMENSION_CURVE item must reference a supported curve, EDGE_CURVE, SUBEDGE, ORIENTED_EDGE, EDGE_LOOP, POLY_LOOP, PATH, OPEN_PATH, SUBPATH, ORIENTED_PATH, CONNECTED_EDGE_SET, WIRE_SHELL, wireframe model or GEOMETRIC_CURVE_SET");
    }
    return new StepDimensionCurve(
        instance.id(),
        optionalStringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "DIMENSION_CURVE styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        item);
  }

  private StepAnnotationFillArea resolveAnnotationFillArea(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ANNOTATION_FILL_AREA");
    requireParameterCount(instance, definition, 2);
    List<StepEntity> boundaries =
        entityReferenceList(
            instance,
            definition,
            1,
            "ANNOTATION_FILL_AREA boundaries must contain curve references");
    for (StepEntity boundary : boundaries) {
      if (!isSupportedAnnotationCurveCarrier(boundary)) {
        throw new StepResolutionException(
            "ANNOTATION_FILL_AREA boundaries must reference supported curves, EDGE_CURVE, SUBEDGE, ORIENTED_EDGE, EDGE_LOOP, POLY_LOOP, PATH, OPEN_PATH, SUBPATH, ORIENTED_PATH, CONNECTED_EDGE_SET, WIRE_SHELL, wireframe model or GEOMETRIC_CURVE_SET");
      }
    }
    return new StepAnnotationFillArea(
        instance.id(), stringValue(instance, definition, 0), boundaries);
  }

  private StepAnnotationFillAreaOccurrence resolveAnnotationFillAreaOccurrence(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ANNOTATION_FILL_AREA_OCCURRENCE");
    requireParameterCount(instance, definition, 4);
    StepEntity fillStyleTarget = resolve(referenceId(instance, definition, 3));
    if (!isSupportedAnnotationPointCarrier(fillStyleTarget)) {
      throw new StepResolutionException(
          "ANNOTATION_FILL_AREA_OCCURRENCE fill_style_target must reference supported point carriers or point-like annotation content/occurrences");
    }
    return new StepAnnotationFillAreaOccurrence(
        instance.id(),
        optionalStringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "ANNOTATION_FILL_AREA_OCCURRENCE styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        requireEntity(
            referenceId(instance, definition, 2),
            StepAnnotationFillArea.class,
            "ANNOTATION_FILL_AREA_OCCURRENCE item must reference ANNOTATION_FILL_AREA"),
        fillStyleTarget);
  }

  private StepAnnotationPlaceholderOccurrence resolveAnnotationPlaceholderOccurrence(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ANNOTATION_PLACEHOLDER_OCCURRENCE");
    requireParameterCount(instance, definition, 5);
    double lineSpacing = numberValue(instance, definition, 4);
    if (!(lineSpacing > 0.0)) {
      throw new StepResolutionException(
          "ANNOTATION_PLACEHOLDER_OCCURRENCE line_spacing must be positive");
    }
    return new StepAnnotationPlaceholderOccurrence(
        instance.id(),
        optionalStringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "ANNOTATION_PLACEHOLDER_OCCURRENCE styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        requireSupportedPlaceholderItem(resolve(referenceId(instance, definition, 2))),
        enumValue(instance, definition, 3),
        lineSpacing);
  }

  private StepEntity requireSupportedPlaceholderItem(StepEntity item) {
    if (!isSupportedAnnotationPlaneElement(item)) {
      throw new UnsupportedStepEntityException(
          "ANNOTATION_PLACEHOLDER_OCCURRENCE item must reference supported point carriers or point-like annotation content/occurrences");
    }
    return item;
  }

  private StepAnnotationPlane resolveAnnotationPlane(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ANNOTATION_PLANE");
    requireParameterCount(instance, definition, 1);
    List<StepEntity> elements = List.of();
    if (!isUnset(definition.parameters().get(0))) {
      elements =
          entityReferenceList(
              instance,
              definition,
              0,
              "ANNOTATION_PLANE elements must contain entity references");
      for (StepEntity element : elements) {
        if (!isSupportedAnnotationPlaneElement(element)) {
          throw new UnsupportedStepEntityException(
              "ANNOTATION_PLANE elements must reference supported point carriers or point-like annotation content/occurrences");
        }
      }
    }
    return new StepAnnotationPlane(
        instance.id(),
        inheritedRepresentationItemName(instance),
        referenceList(
            instance,
            definition(instance, "STYLED_ITEM"),
            1,
            StepPresentationStyleAssignment.class,
            "ANNOTATION_PLANE styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        requireEntity(
            inheritedStyledItemTargetId(instance),
            StepPlane.class,
            "ANNOTATION_PLANE item must reference PLANE"),
        elements);
  }

  private StepGeometricCurveSet resolveGeometricCurveSet(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GEOMETRIC_CURVE_SET");
    requireParameterCount(instance, definition, 2);
    List<StepEntity> elements =
        entityReferenceList(
            instance, definition, 1, "GEOMETRIC_CURVE_SET elements must contain entity references");
    for (StepEntity element : elements) {
      if (!isSupportedGeometricCurveSetElement(element)) {
        throw new UnsupportedStepEntityException(
            "GEOMETRIC_CURVE_SET elements must be supported curves, points, paths, curve topology or nested point/geometry sets");
      }
    }
    return new StepGeometricCurveSet(instance.id(), stringValue(instance, definition, 0), elements);
  }

  private StepGeometricSurfaceSet resolveGeometricSurfaceSet(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GEOMETRIC_SURFACE_SET");
    requireParameterCount(instance, definition, 2);
    List<StepEntity> elements =
        entityReferenceList(
            instance, definition, 1, "GEOMETRIC_SURFACE_SET elements must contain entity references");
    for (StepEntity element : elements) {
      if (!isSupportedSurfaceReference(element)) {
        throw new UnsupportedStepEntityException(
            "GEOMETRIC_SURFACE_SET elements must be supported surfaces");
      }
    }
    return new StepGeometricSurfaceSet(instance.id(), stringValue(instance, definition, 0), elements);
  }

  private StepClothoid resolveClothoid(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CLOTHOID");
    requireParameterCount(instance, definition, 4);
    StepEntity position = resolve(referenceId(instance, definition, 1));
    if (!(position instanceof StepAxis2Placement2D)) {
      throw new StepResolutionException(
          "CLOTHOID position must reference AXIS2_PLACEMENT_2D");
    }
    return new StepClothoid(
        instance.id(),
        stringValue(instance, definition, 0),
        position,
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  private StepIndexedPolyCurve resolveIndexedPolyCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "INDEXED_POLY_CURVE");
    requireParameterCount(instance, definition, 4);
    List<StepCartesianPoint> points =
        referenceList(
            instance,
            definition,
            1,
            StepCartesianPoint.class,
            "INDEXED_POLY_CURVE points must reference CARTESIAN_POINT");
    List<Integer> indices =
        integerList(instance, definition, 2);
    return new StepIndexedPolyCurve(
        instance.id(),
        stringValue(instance, definition, 0),
        points,
        indices,
        booleanValue(instance, definition, 3));
  }

  private StepSurfaceOfConstantRadius resolveSurfaceOfConstantRadius(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_OF_CONSTANT_RADIUS");
    requireParameterCount(instance, definition, 4);
    StepEntity sweptSurface = resolve(referenceId(instance, definition, 1));
    if (!isSupportedSurfaceReference(sweptSurface)) {
      throw new UnsupportedStepEntityException(
          "SURFACE_OF_CONSTANT_RADIUS swept_surface must reference a supported surface");
    }
    return new StepSurfaceOfConstantRadius(
        instance.id(),
        stringValue(instance, definition, 0),
        sweptSurface,
        numberValue(instance, definition, 3));
  }

  private StepDegenerateCurve resolveDegenerateCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DEGENERATE_CURVE");
    requireParameterCount(instance, definition, 3);
    StepEntity basisCurve = resolve(referenceId(instance, definition, 1));
    if (!isSupportedCurveReference(basisCurve)) {
      throw new UnsupportedStepEntityException(
          "DEGENERATE_CURVE basis_curve must reference a supported curve");
    }
    return new StepDegenerateCurve(
        instance.id(),
        stringValue(instance, definition, 0),
        basisCurve);
  }

  private StepCircle2D resolveCircle2D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CIRCLE_2D");
    requireParameterCount(instance, definition, 3);
    return new StepCircle2D(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement2D.class,
            "CIRCLE_2D position must reference AXIS2_PLACEMENT_2D"),
        numberValue(instance, definition, 2));
  }

  private StepEllipse2D resolveEllipse2D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ELLIPSE_2D");
    requireParameterCount(instance, definition, 4);
    return new StepEllipse2D(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement2D.class,
            "ELLIPSE_2D position must reference AXIS2_PLACEMENT_2D"),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  private StepHyperbola2D resolveHyperbola2D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "HYPERBOLA_2D");
    requireParameterCount(instance, definition, 4);
    return new StepHyperbola2D(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement2D.class,
            "HYPERBOLA_2D position must reference AXIS2_PLACEMENT_2D"),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  private StepParabola2D resolveParabola2D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PARABOLA_2D");
    requireParameterCount(instance, definition, 3);
    return new StepParabola2D(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement2D.class,
            "PARABOLA_2D position must reference AXIS2_PLACEMENT_2D"),
        numberValue(instance, definition, 2));
  }

  private StepLine2D resolveLine2D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LINE_2D");
    requireParameterCount(instance, definition, 3);
    return new StepLine2D(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepCartesianPoint.class,
            "LINE_2D point must reference CARTESIAN_POINT"),
        requireEntity(
            referenceId(instance, definition, 2),
            StepDirection.class,
            "LINE_2D direction must reference DIRECTION"));
  }

  private StepPolyline2D resolvePolyline2D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "POLYLINE_2D");
    requireParameterCount(instance, definition, 2);
    return new StepPolyline2D(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepCartesianPoint.class,
            "POLYLINE_2D points must reference CARTESIAN_POINT"));
  }

  private StepTrimmedCurve2D resolveTrimmedCurve2D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TRIMMED_CURVE_2D");
    requireParameterCount(instance, definition, 5);
    return new StepTrimmedCurve2D(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepCurve.class,
            "TRIMMED_CURVE_2D basis_curve must reference a curve"),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3),
        booleanValue(instance, definition, 4));
  }

  private StepCompositeCurve2D resolveCompositeCurve2D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COMPOSITE_CURVE_2D");
    requireParameterCount(instance, definition, 3);
    return new StepCompositeCurve2D(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepCompositeCurveSegment.class,
            "COMPOSITE_CURVE_2D segments must reference COMPOSITE_CURVE_SEGMENT"),
        booleanValue(instance, definition, 2));
  }

  private StepBSplineCurve2D resolveBSplineCurve2D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "B_SPLINE_CURVE_2D");
    requireParameterCountIn(instance, definition, 4, 5);
    boolean hasName = definition.parameters().size() == 5;
    return new StepBSplineCurve2D(
        instance.id(),
        hasName ? stringValue(instance, definition, 0) : "",
        integerValue(instance, definition, hasName ? 1 : 0),
        referenceList(
            instance,
            definition,
            hasName ? 2 : 1,
            StepCartesianPoint.class,
            "B_SPLINE_CURVE_2D control points must reference CARTESIAN_POINT"),
        enumValue(instance, definition, hasName ? 3 : 2));
  }

  private StepRationalBSplineCurve2D resolveRationalBSplineCurve2D(StepEntityInstance instance) {
    StepEntityDefinition rational = definition(instance, "RATIONAL_B_SPLINE_CURVE_2D");
    requireParameterCount(instance, rational, 1);
    StepEntityDefinition base = definition(instance, "B_SPLINE_CURVE_2D");
    requireParameterCountIn(instance, base, 4, 5);
    boolean hasName = base.parameters().size() == 5;
    return new StepRationalBSplineCurve2D(
        instance.id(),
        hasName ? stringValue(instance, base, 0) : "",
        integerValue(instance, base, hasName ? 1 : 0),
        referenceList(
            instance,
            base,
            hasName ? 2 : 1,
            StepCartesianPoint.class,
            "RATIONAL_B_SPLINE_CURVE_2D control points must reference CARTESIAN_POINT"),
        numberList(instance, rational, 0),
        enumValue(instance, base, hasName ? 3 : 2));
  }

  private StepBezierCurve2D resolveBezierCurve2D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BEZIER_CURVE_2D");
    requireParameterCountIn(instance, definition, 3, 4);
    boolean hasName = definition.parameters().size() == 4;
    return new StepBezierCurve2D(
        instance.id(),
        hasName ? stringValue(instance, definition, 0) : "",
        integerValue(instance, definition, hasName ? 1 : 0),
        referenceList(
            instance,
            definition,
            hasName ? 2 : 1,
            StepCartesianPoint.class,
            "BEZIER_CURVE_2D control points must reference CARTESIAN_POINT"));
  }

  private StepQuasiUniformCurve2D resolveQuasiUniformCurve2D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "QUASI_UNIFORM_CURVE_2D");
    requireParameterCountIn(instance, definition, 4, 5);
    boolean hasName = definition.parameters().size() == 5;
    return new StepQuasiUniformCurve2D(
        instance.id(),
        hasName ? stringValue(instance, definition, 0) : "",
        integerValue(instance, definition, hasName ? 1 : 0),
        referenceList(
            instance,
            definition,
            hasName ? 2 : 1,
            StepCartesianPoint.class,
            "QUASI_UNIFORM_CURVE_2D control points must reference CARTESIAN_POINT"),
        enumValue(instance, definition, hasName ? 3 : 2));
  }

  private StepUniformCurve2D resolveUniformCurve2D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "UNIFORM_CURVE_2D");
    requireParameterCountIn(instance, definition, 4, 5);
    boolean hasName = definition.parameters().size() == 5;
    return new StepUniformCurve2D(
        instance.id(),
        hasName ? stringValue(instance, definition, 0) : "",
        integerValue(instance, definition, hasName ? 1 : 0),
        referenceList(
            instance,
            definition,
            hasName ? 2 : 1,
            StepCartesianPoint.class,
            "UNIFORM_CURVE_2D control points must reference CARTESIAN_POINT"),
        enumValue(instance, definition, hasName ? 3 : 2));
  }

  private StepPiecewiseBezierCurve2D resolvePiecewiseBezierCurve2D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PIECEWISE_BEZIER_CURVE_2D");
    requireParameterCountIn(instance, definition, 3, 4);
    boolean hasName = definition.parameters().size() == 4;
    return new StepPiecewiseBezierCurve2D(
        instance.id(),
        hasName ? stringValue(instance, definition, 0) : "",
        integerValue(instance, definition, hasName ? 1 : 0),
        referenceList(
            instance,
            definition,
            hasName ? 2 : 1,
            StepCartesianPoint.class,
            "PIECEWISE_BEZIER_CURVE_2D control points must reference CARTESIAN_POINT"));
  }

  private StepIndexedPolyCurve2D resolveIndexedPolyCurve2D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "INDEXED_POLY_CURVE_2D");
    requireParameterCount(instance, definition, 3);
    return new StepIndexedPolyCurve2D(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepCartesianPoint.class,
            "INDEXED_POLY_CURVE_2D points must reference CARTESIAN_POINT"),
        integerList(instance, definition, 2));
  }

  private StepDegenerateCurve2D resolveDegenerateCurve2D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DEGENERATE_CURVE_2D");
    requireParameterCount(instance, definition, 2);
    return new StepDegenerateCurve2D(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepCartesianPoint.class,
            "DEGENERATE_CURVE_2D point must reference CARTESIAN_POINT"));
  }

  private StepBoundedCurve2D resolveBoundedCurve2D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BOUNDED_CURVE_2D");
    requireParameterCount(instance, definition, 2);
    return new StepBoundedCurve2D(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepCurve.class,
            "BOUNDED_CURVE_2D curve must reference a 2D curve entity"));
  }

  private StepCurve2D resolveCurve2D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CURVE_2D");
    requireParameterCount(instance, definition, 3);
    // Equation parameter may be wrapped in nested list: ((a,b,c,d)) -> ListValue containing ListValue
    StepValue eqParam = unwrapTyped(definition.parameters().get(2));
    if (eqParam instanceof StepValue.ListValue outerList
        && outerList.elements().size() == 1
        && outerList.elements().getFirst() instanceof StepValue.ListValue innerList) {
      eqParam = innerList;
    }
    List<Double> eqList = extractNumberList(definition, eqParam, "CURVE_2D");
    double[] equation = new double[eqList.size()];
    for (int i = 0; i < eqList.size(); i++) {
      equation[i] = eqList.get(i);
    }
    return new StepCurve2D(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement2D.class,
            "CURVE_2D position must reference AXIS2_PLACEMENT_2D"),
        equation);
  }

  private StepSweptAreaSolid resolveSweptAreaSolid(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 5);
    int sweepRefId = referenceId(instance, definition, 3);
    return new StepSweptAreaSolid(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepProfileDef.class,
            entityName + " swept area must reference a profile definition"),
        requireEntity(
            referenceId(instance, definition, 2),
            StepAxis2Placement3D.class,
            entityName + " position must reference AXIS2_PLACEMENT_3D"),
        resolve(sweepRefId),
        numberValue(instance, definition, 4),
        entityName);
  }

  private StepMachinedSurface resolveMachinedSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MACHINED_SURFACE");
    requireParameterCount(instance, definition, 2);
    int faceId = referenceId(instance, definition, 1);
    return new StepMachinedSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(faceId));
  }

  private StepEdgeWire resolveEdgeWire(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "EDGE_WIRE");
    requireParameterCount(instance, definition, 2);
    List<StepEntity> edges =
        entityReferenceList(
            instance, definition, 1, "EDGE_WIRE edges must contain entity references");
    for (StepEntity edge : edges) {
      if (!(edge instanceof StepEdgeCurve) && !(edge instanceof StepOrientedEdge) && !(edge instanceof StepSubedge)) {
        throw new UnsupportedStepEntityException(
            "EDGE_WIRE edges must reference EDGE_CURVE, ORIENTED_EDGE, or SUBEDGE");
      }
    }
    return new StepEdgeWire(instance.id(), stringValue(instance, definition, 0), edges);
  }

  private StepRectangularCompositeSurface resolveRectangularCompositeSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "RECTANGULAR_COMPOSITE_SURFACE");
    requireParameterCount(instance, definition, 7);
    StepEntity parentSurface = resolve(referenceId(instance, definition, 1));
    if (!isSupportedSurfaceReference(parentSurface)) {
      throw new UnsupportedStepEntityException(
          "RECTANGULAR_COMPOSITE_SURFACE parent_surface must reference a supported surface");
    }
    return new StepRectangularCompositeSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        parentSurface,
        numberValue(instance, definition, 3),
        numberValue(instance, definition, 4),
        numberValue(instance, definition, 5),
        numberValue(instance, definition, 6));
  }

  private StepSurfacePatch resolveSurfacePatch(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_PATCH");
    requireParameterCount(instance, definition, 4);
    StepEntity basisSurface = resolve(referenceId(instance, definition, 1));
    if (!isSupportedSurfaceReference(basisSurface)) {
      throw new UnsupportedStepEntityException(
          "SURFACE_PATCH basis_surface must reference a supported surface");
    }
    return new StepSurfacePatch(
        instance.id(),
        stringValue(instance, definition, 0),
        basisSurface,
        booleanValue(instance, definition, 3));
  }

  private StepCompositeCurveOnSurface3D resolveCompositeCurveOnSurface3D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COMPOSITE_CURVE_ON_SURFACE_3D");
    requireParameterCount(instance, definition, 4);
    List<StepCompositeCurveSegment> segments =
        referenceList(
            instance,
            definition,
            1,
            StepCompositeCurveSegment.class,
            "COMPOSITE_CURVE_ON_SURFACE_3D segments must reference COMPOSITE_CURVE_SEGMENT");
    StepEntity surface = resolve(referenceId(instance, definition, 2));
    if (!isSupportedSurfaceReference(surface)) {
      throw new UnsupportedStepEntityException(
          "COMPOSITE_CURVE_ON_SURFACE_3D surface must reference a supported surface");
    }
    return new StepCompositeCurveOnSurface3D(
        instance.id(),
        stringValue(instance, definition, 0),
        segments,
        surface,
        booleanValue(instance, definition, 3));
  }

  private StepLineSegment resolveLineSegment(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LINE_SEGMENT");
    requireParameterCount(instance, definition, 3);
    return new StepLineSegment(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepCartesianPoint.class,
            "LINE_SEGMENT start_point must reference CARTESIAN_POINT"),
        requireEntity(
            referenceId(instance, definition, 2),
            StepCartesianPoint.class,
            "LINE_SEGMENT end_point must reference CARTESIAN_POINT"));
  }

  private StepOffsetSurface2 resolveOffsetSurface2(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "OFFSET_SURFACE_2");
    requireParameterCount(instance, definition, 5);
    StepEntity basisSurface = resolve(referenceId(instance, definition, 1));
    if (!isSupportedSurfaceReference(basisSurface)) {
      throw new UnsupportedStepEntityException(
          "OFFSET_SURFACE_2 basis_surface must reference a supported surface");
    }
    return new StepOffsetSurface2(
        instance.id(),
        stringValue(instance, definition, 0),
        basisSurface,
        numberValue(instance, definition, 3),
        booleanValue(instance, definition, 4));
  }

  private StepPolygonalBoundedHalfSpace resolvePolygonalBoundedHalfSpace(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "POLYGONAL_BOUNDED_HALF_SPACE");
    requireParameterCount(instance, definition, 5);
    StepEntity basisSurface = resolve(referenceId(instance, definition, 1));
    if (!isSupportedSurfaceReference(basisSurface)) {
      throw new UnsupportedStepEntityException(
          "POLYGONAL_BOUNDED_HALF_SPACE basis_surface must reference a supported surface");
    }
    List<StepCartesianPoint> polygonPoints =
        referenceList(
            instance,
            definition,
            3,
            StepCartesianPoint.class,
            "POLYGONAL_BOUNDED_HALF_SPACE points must reference CARTESIAN_POINT");
    return new StepPolygonalBoundedHalfSpace(
        instance.id(),
        stringValue(instance, definition, 0),
        basisSurface,
        requireEntity(
            referenceId(instance, definition, 2),
            StepAxis2Placement3D.class,
            "POLYGONAL_BOUNDED_HALF_SPACE position must reference AXIS2_PLACEMENT_3D"),
        polygonPoints,
        booleanValue(instance, definition, 4));
  }

  private StepSubface resolveSubface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SUBFACE");
    requireParameterCount(instance, definition, 3);
    StepEntity faceElement = resolve(referenceId(instance, definition, 1));
    if (!isSupportedSurfaceReference(faceElement)) {
      throw new UnsupportedStepEntityException(
          "SUBFACE face_element must reference a supported surface");
    }
    return new StepSubface(
        instance.id(),
        stringValue(instance, definition, 0),
        faceElement);
  }

  private StepOrientedSubface resolveOrientedSubface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ORIENTED_SUBFACE");
    requireParameterCount(instance, definition, 4);
    StepEntity faceElement = resolve(referenceId(instance, definition, 1));
    if (!(faceElement instanceof StepSubface)) {
      throw new UnsupportedStepEntityException(
          "ORIENTED_SUBFACE face_element must reference SUBFACE");
    }
    return new StepOrientedSubface(
        instance.id(),
        stringValue(instance, definition, 0),
        faceElement,
        booleanValue(instance, definition, 3));
  }

  private StepRectangleHollowProfileDef resolveRectangleHollowProfileDef(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "RECTANGLE_HOLLOW_PROFILE_DEF");
    requireParameterCountIn(instance, definition, 6, 7);
    boolean hasName = definition.parameters().size() == 7;
    return new StepRectangleHollowProfileDef(
        instance.id(),
        hasName ? stringValue(instance, definition, 1) : "",
        requireEntity(
            referenceId(instance, definition, hasName ? 2 : 1),
            StepAxis2Placement2D.class,
            "RECTANGLE_HOLLOW_PROFILE_DEF position must reference AXIS2_PLACEMENT_2D"),
        numberValue(instance, definition, hasName ? 3 : 2),
        numberValue(instance, definition, hasName ? 4 : 3),
        numberValue(instance, definition, hasName ? 5 : 4),
        numberValue(instance, definition, hasName ? 6 : 5));
  }

  private StepCentreLineArcProfileDef resolveCentreLineArcProfileDef(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CENTRE_LINE_ARC_PROFILE_DEF");
    requireParameterCount(instance, definition, 4);
    return new StepCentreLineArcProfileDef(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement2D.class,
            "CENTRE_LINE_ARC_PROFILE_DEF position must reference AXIS2_PLACEMENT_2D"),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  private StepSweptDiskSolid resolveSweptDiskSolid(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SWEPT_DISK_SOLID");
    requireParameterCount(instance, definition, 4);
    StepEntity sweptCurve = resolve(referenceId(instance, definition, 1));
    if (!isSupportedCurveReference(sweptCurve)) {
      throw new UnsupportedStepEntityException(
          "SWEPT_DISK_SOLID swept_curve must reference a supported curve");
    }
    Double innerRadius = optionalNumberValue(instance, definition, 3);
    return new StepSweptDiskSolid(
        instance.id(),
        stringValue(instance, definition, 0),
        sweptCurve,
        numberValue(instance, definition, 2),
        innerRadius);
  }

  private StepRuledSurface resolveRuledSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "RULED_SURFACE");
    requireParameterCount(instance, definition, 5);
    return new StepRuledSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "RULED_SURFACE position must reference AXIS2_PLACEMENT_3D"),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  private StepCenteredCircleProfileDef resolveCenteredCircleProfileDef(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CENTERED_CIRCLE_PROFILE_DEF");
    requireParameterCountIn(instance, definition, 4, 5);
    boolean hasName = definition.parameters().size() == 5;
    return new StepCenteredCircleProfileDef(
        instance.id(),
        hasName ? stringValue(instance, definition, 1) : "",
        requireEntity(
            referenceId(instance, definition, hasName ? 2 : 1),
            StepAxis2Placement2D.class,
            "CENTERED_CIRCLE_PROFILE_DEF position must reference AXIS2_PLACEMENT_2D"),
        numberValue(instance, definition, hasName ? 3 : 2),
        numberValue(instance, definition, hasName ? 4 : 3));
  }

  private StepRevolvedAreaSolidTapered resolveRevolvedAreaSolidTapered(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "REVOLVED_AREA_SOLID_TAPERED");
    requireParameterCount(instance, definition, 5);
    return new StepRevolvedAreaSolidTapered(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        requireEntity(
            referenceId(instance, definition, 2),
            StepAxis1Placement.class,
            "REVOLVED_AREA_SOLID_TAPERED axis must reference AXIS1_PLACEMENT"),
        numberValue(instance, definition, 3),
        numberValue(instance, definition, 4));
  }

  private StepExtrudedAreaSolidTapered resolveExtrudedAreaSolidTapered(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "EXTRUDED_AREA_SOLID_TAPERED");
    requireParameterCount(instance, definition, 5);
    return new StepExtrudedAreaSolidTapered(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        requireEntity(
            referenceId(instance, definition, 2),
            StepDirection.class,
            "EXTRUDED_AREA_SOLID_TAPERED direction must reference DIRECTION"),
        numberValue(instance, definition, 3),
        numberValue(instance, definition, 4));
  }

  private StepSurfaceCurveSweptAreaSolid resolveSurfaceCurveSweptAreaSolid(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_CURVE_SWEPT_AREA_SOLID");
    requireParameterCount(instance, definition, 6);
    return new StepSurfaceCurveSweptAreaSolid(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        numberValue(instance, definition, 4),
        numberValue(instance, definition, 5));
  }

  // Advanced CSG volume resolve methods

  private StepCylinderVolume resolveCylinderVolume(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CYLINDER_VOLUME");
    requireParameterCount(instance, definition, 5);
    return new StepCylinderVolume(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "CYLINDER_VOLUME position must reference AXIS2_PLACEMENT_3D"),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  private StepSphereVolume resolveSphereVolume(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SPHERE_VOLUME");
    requireParameterCount(instance, definition, 4);
    return new StepSphereVolume(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepCartesianPoint.class,
            "SPHERE_VOLUME center must reference CARTESIAN_POINT"),
        numberValue(instance, definition, 2));
  }

  private StepTorusVolume resolveTorusVolume(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TORUS_VOLUME");
    requireParameterCount(instance, definition, 5);
    return new StepTorusVolume(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "TORUS_VOLUME position must reference AXIS2_PLACEMENT_3D"),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  private StepPrismVolume resolvePrismVolume(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRISM_VOLUME");
    requireParameterCount(instance, definition, 6);
    return new StepPrismVolume(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "PRISM_VOLUME position must reference AXIS2_PLACEMENT_3D"),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3),
        numberValue(instance, definition, 4));
  }

  // Swept face solid resolve methods

  private StepExtrudedFaceSolid resolveExtrudedFaceSolid(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "EXTRUDED_FACE_SOLID");
    requireParameterCount(instance, definition, 7);
    return new StepExtrudedFaceSolid(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        numberValue(instance, definition, 4));
  }

  private StepRevolvedFaceSolid resolveRevolvedFaceSolid(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "REVOLVED_FACE_SOLID");
    requireParameterCount(instance, definition, 7);
    return new StepRevolvedFaceSolid(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        numberValue(instance, definition, 4));
  }

  private StepSweptFaceSolid resolveSweptFaceSolid(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 4);
    return new StepSweptFaceSolid(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        entityName);
  }

  private StepAdvancedBrep resolveAdvancedBrep(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ADVANCED_BREP");
    requireParameterCount(instance, definition, 3);
    List<StepEntity> voids =
        entityReferenceList(
            instance, definition, 2, "ADVANCED_BREP voids must contain entity references");
    return new StepAdvancedBrep(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        voids);
  }

  private StepComplexClippingResult resolveComplexClippingResult(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COMPLEX_CLIPPING_RESULT");
    requireParameterCount(instance, definition, 4);
    return new StepComplexClippingResult(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        enumValue(instance, definition, 3));
  }

  private StepCompositeText resolveCompositeText(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COMPOSITE_TEXT");
    requireParameterCount(instance, definition, 2);
    List<StepEntity> collection =
        entityReferenceList(
            instance, definition, 1, "COMPOSITE_TEXT collection must contain entity references");
    return new StepCompositeText(
        instance.id(), stringValue(instance, definition, 0), collection);
  }

  private StepTextLiteral resolveTextLiteral(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TEXT_LITERAL");
    requireParameterCount(instance, definition, 3);
    return new StepTextLiteral(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepComposedText resolveComposedText(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COMPOSED_TEXT");
    requireParameterCount(instance, definition, 3);
    List<StepEntity> collection =
        entityReferenceList(
            instance, definition, 1, "COMPOSED_TEXT collection must contain entity references");
    return new StepComposedText(
        instance.id(),
        stringValue(instance, definition, 0),
        collection,
        resolve(referenceId(instance, definition, 2)));
  }

  private StepTessellatedFaceSet resolveTessellatedFaceSet(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TESSELLATED_FACE_SET");
    requireParameterCount(instance, definition, 4);
    List<StepEntity> coordinateEntities =
        entityReferenceList(
            instance, definition, 2, "TESSELLATED_FACE_SET coordinates must contain entity references");
    List<StepCartesianPoint> coordinates = new ArrayList<>();
    for (StepEntity entity : coordinateEntities) {
      if (!(entity instanceof StepCartesianPoint point)) {
        throw new StepResolutionException(
            "TESSELLATED_FACE_SET coordinates must contain CARTESIAN_POINT entities");
      }
      coordinates.add(point);
    }
    StepValue value = unwrapTyped(definition.parameters().get(3));
    if (!(value instanceof StepValue.ListValue listValue)) {
      throw new StepResolutionException(
          "TESSELLATED_FACE_SET parameter 3 must be a list");
    }
    List<List<Integer>> faceIndices = new ArrayList<>();
    for (StepValue element : listValue.elements()) {
      if (!(element instanceof StepValue.ListValue innerList)) {
        throw new StepResolutionException(
            "TESSELLATED_FACE_SET face indices must be lists of integers");
      }
      List<Integer> indices = new ArrayList<>();
      for (StepValue innerElement : innerList.elements()) {
        if (!(innerElement instanceof StepValue.NumberValue numValue)) {
          throw new StepResolutionException(
              "TESSELLATED_FACE_SET face indices must be integers");
        }
        indices.add((int) numValue.value());
      }
      faceIndices.add(List.copyOf(indices));
    }
    return new StepTessellatedFaceSet(
        instance.id(),
        stringValue(instance, definition, 0),
        coordinates,
        List.copyOf(faceIndices));
  }

  private StepSeamEdge resolveSeamEdge(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SEAM_EDGE");
    requireParameterCount(instance, definition, 3);
    StepEntity edgeStart = resolve(referenceId(instance, definition, 1));
    if (!(edgeStart instanceof StepVertexPoint) && !(edgeStart instanceof StepVertex)) {
      throw new StepResolutionException(
          "SEAM_EDGE edge_start must reference VERTEX but got " + edgeStart.getClass().getSimpleName());
    }
    StepEntity edgeEnd = resolve(referenceId(instance, definition, 2));
    if (!(edgeEnd instanceof StepVertexPoint) && !(edgeEnd instanceof StepVertex)) {
      throw new StepResolutionException(
          "SEAM_EDGE edge_end must reference VERTEX but got " + edgeEnd.getClass().getSimpleName());
    }
    return new StepSeamEdge(
        instance.id(),
        stringValue(instance, definition, 0),
        edgeStart,
        edgeEnd);
  }

  private StepTessellatedFace resolveTessellatedFace(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TESSELLATED_FACE");
    requireParameterCount(instance, definition, 2);
    List<StepEntity> triangles = entityReferenceList(
        instance, definition, 1, "TESSELLATED_FACE triangles must contain entity references");
    return new StepTessellatedFace(
        instance.id(),
        stringValue(instance, definition, 0),
        triangles);
  }

  private StepTessellatedTriangle resolveTessellatedTriangle(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TESSELLATED_TRIANGLE");
    requireParameterCount(instance, definition, 5);
    StepEntity v1 = resolve(referenceId(instance, definition, 2));
    StepEntity v2 = resolve(referenceId(instance, definition, 3));
    StepEntity v3 = resolve(referenceId(instance, definition, 4));
    if (!(v1 instanceof StepVertexPoint) && !(v1 instanceof StepVertex)) {
      throw new StepResolutionException(
          "TESSELLATED_TRIANGLE vertex1 must reference VERTEX but got " + v1.getClass().getSimpleName());
    }
    if (!(v2 instanceof StepVertexPoint) && !(v2 instanceof StepVertex)) {
      throw new StepResolutionException(
          "TESSELLATED_TRIANGLE vertex2 must reference VERTEX but got " + v2.getClass().getSimpleName());
    }
    if (!(v3 instanceof StepVertexPoint) && !(v3 instanceof StepVertex)) {
      throw new StepResolutionException(
          "TESSELLATED_TRIANGLE vertex3 must reference VERTEX but got " + v3.getClass().getSimpleName());
    }
    return new StepTessellatedTriangle(
        instance.id(),
        stringValue(instance, definition, 0),
        v1, v2, v3);
  }

  private StepFiniteElementMesh resolveFiniteElementMesh(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FINITE_ELEMENT_MESH");
    requireParameterCount(instance, definition, 6);
    String meshType = stringValue(instance, definition, 1);
    List<StepEntity> nodes = entityReferenceList(
        instance, definition, 2, "FINITE_ELEMENT_MESH nodes must contain entity references");
    List<StepEntity> elements = entityReferenceList(
        instance, definition, 3, "FINITE_ELEMENT_MESH elements must contain entity references");
    StepValue elementTypesValue = unwrapTyped(definition.parameters().get(4));
    List<String> elementTypes = new ArrayList<>();
    if (elementTypesValue instanceof StepValue.ListValue typeList) {
      for (StepValue typeElement : typeList.elements()) {
        if (typeElement instanceof StepValue.StringValue sv) {
          elementTypes.add(sv.value());
        } else if (typeElement instanceof StepValue.TypedValue tv && tv.value() instanceof StepValue.StringValue sv) {
          elementTypes.add(sv.value());
        }
      }
    }
    double meshDensity = numberValue(instance, definition, 5);
    return new StepFiniteElementMesh(
        instance.id(),
        stringValue(instance, definition, 0),
        meshType,
        List.copyOf(nodes),
        List.copyOf(elements),
        List.copyOf(elementTypes),
        meshDensity);
  }

  private boolean isSupportedAnnotationCurveCarrier(StepEntity item) {
    return isSupportedCurveReference(item)
        || item instanceof StepEdgeCurve
        || item instanceof StepSubedge
        || item instanceof StepOrientedEdge
        || item instanceof StepEdgeLoop
        || item instanceof StepPolyLoop
        || item instanceof StepPath
        || item instanceof StepOpenPath
        || item instanceof StepSubpath
        || item instanceof StepOrientedPath
        || item instanceof StepConnectedEdgeSet
        || item instanceof StepWireShell
        || item instanceof StepEdgeBasedWireframeModel
        || item instanceof StepShellBasedWireframeModel
        || item instanceof StepGeometricCurveSet;
  }

  private boolean isSupportedAnnotationPointCarrier(StepEntity item) {
    return item instanceof StepCartesianPoint
        || item instanceof StepVertexPoint
        || item instanceof StepVertexShell
        || item instanceof StepPointSet
        || item instanceof StepGeometricSet
        || item instanceof StepGeometricCurveSet
        || isSupportedAnnotationWrapperItem(item)
        || item instanceof StepAnnotationPointOccurrence
        || item instanceof StepAnnotationFillAreaOccurrence
        || item instanceof StepAnnotationTextOccurrence
        || item instanceof StepAnnotationPlaceholderOccurrence
        || item instanceof StepAnnotationSymbolOccurrence
        || item instanceof StepAnnotationSubfigureOccurrence
        || item instanceof StepDraughtingAnnotationOccurrence
        || item instanceof StepAnnotationPlane
        || item instanceof StepGeometricReplica replica
            && "POINT_REPLICA".equals(replica.entityName());
  }

  private boolean isSupportedAnnotationPlaneElement(StepEntity item) {
    return isSupportedAnnotationPointCarrier(item)
        || item instanceof StepAnnotationPointOccurrence
        || item instanceof StepAnnotationFillAreaOccurrence
        || item instanceof StepAnnotationTextOccurrence
        || item instanceof StepAnnotationPlaceholderOccurrence
        || item instanceof StepAnnotationSymbolOccurrence
        || item instanceof StepAnnotationSubfigureOccurrence
        || item instanceof StepDraughtingAnnotationOccurrence
        || item instanceof StepAnnotationPlane;
  }

  private boolean isSupportedAnnotationWrapperItem(StepEntity item) {
    return item instanceof StepAnnotationSymbol
        || item instanceof StepAnnotationText
        || item instanceof StepAnnotationTextCharacter
        || item instanceof StepAnnotationFillArea
        || isAnnotationOccurrence(item);
  }

  private StepGeometricSet resolveGeometricSet(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GEOMETRIC_SET");
    requireParameterCount(instance, definition, 2);
    List<StepEntity> elements =
        entityReferenceList(
            instance, definition, 1, "GEOMETRIC_SET elements must contain entity references");
    for (StepEntity element : elements) {
      if (!isSupportedGeometricSetElement(element)) {
        throw new UnsupportedStepEntityException(
            "GEOMETRIC_SET elements must be supported curves, surfaces, points, paths, topology, shell/model/solid containers or nested sets");
      }
    }
    return new StepGeometricSet(instance.id(), stringValue(instance, definition, 0), elements);
  }

  private boolean isSupportedGeometricCurveSetElement(StepEntity element) {
    return isSupportedCurveReference(element)
        || isPointLikeSetElement(element)
        || element instanceof StepEdgeCurve
        || element instanceof StepSubedge
        || element instanceof StepOrientedEdge
        || element instanceof StepConnectedEdgeSet
        || element instanceof StepEdgeLoop
        || element instanceof StepVertexLoop
        || element instanceof StepPath
        || element instanceof StepOpenPath
        || element instanceof StepSubpath
        || element instanceof StepOrientedPath
        || element instanceof StepPolyLoop
        || element instanceof StepWireShell
        || element instanceof StepVertexShell
        || element instanceof StepEdgeBasedWireframeModel
        || element instanceof StepShellBasedWireframeModel
        || element instanceof StepPointSet
        || element instanceof StepGeometricSet
        || element instanceof StepGeometricCurveSet;
  }

  private boolean isSupportedGeometricSetElement(StepEntity element) {
    return isSupportedGeometricCurveSetElement(element)
        || isSupportedSurfaceReference(element)
        || element instanceof StepVertexLoop
        || element instanceof StepWireShell
        || element instanceof StepOpenShell
        || element instanceof StepSurfacedOpenShell
        || element instanceof StepOrientedOpenShell
        || element instanceof StepClosedShell
        || element instanceof StepOrientedClosedShell
        || element instanceof StepConnectedFaceSet
        || element instanceof StepConnectedFaceSubSet
        || element instanceof StepFaceBasedSurfaceModel
        || element instanceof StepShellBasedSurfaceModel
        || element instanceof StepEdgeBasedWireframeModel
        || element instanceof StepShellBasedWireframeModel
        || element instanceof StepManifoldSolidBrep
        || element instanceof StepBrepWithVoids
        || element instanceof StepSweptAreaSolid
        || element instanceof StepSolidReplica
        || element instanceof StepCsgSolid
        || element instanceof StepCsgPrimitive
        || element instanceof StepBooleanResult
        || element instanceof StepBooleanClippingResult
        || element instanceof StepPointSet
        || element instanceof StepGeometricSet
        || element instanceof StepGeometricCurveSet;
  }

  private static boolean isPointLikeSetElement(StepEntity element) {
    return element instanceof StepCartesianPoint
        || element instanceof StepVertexPoint
        || element instanceof StepGeometricReplica replica
            && "POINT_REPLICA".equals(replica.entityName());
  }

  private StepPointSet resolvePointSet(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "POINT_SET");
    requireParameterCount(instance, definition, 2);
    List<StepEntity> points =
        entityReferenceList(
            instance, definition, 1, "POINT_SET points must contain entity references");
    for (StepEntity point : points) {
      if (!isSupportedAnnotationPointCarrier(point)) {
        throw new UnsupportedStepEntityException(
            "POINT_SET points must reference supported point carriers or point-like annotation content/occurrences");
      }
    }
    return new StepPointSet(instance.id(), stringValue(instance, definition, 0), points);
  }

  private StepDraughtingCallout resolveDraughtingCallout(StepEntityInstance instance) {
    return resolveDraughtingCallout(instance, "DRAUGHTING_CALLOUT");
  }

  private StepDraughtingCallout resolveDraughtingCallout(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 2);
    List<StepEntity> contents =
        entityReferenceList(
            instance, definition, 1, entityName + " contents must contain entity references");
    for (StepEntity content : contents) {
      if (!(content instanceof StepAnnotationTextOccurrence)
          && !(content instanceof StepCartesianPoint)
          && !(content instanceof StepVertexPoint)
          && !isSupportedCurveReference(content)
          && !(content instanceof StepGeometricReplica replica
              && "POINT_REPLICA".equals(replica.entityName()))
          && !(content instanceof StepEdgeCurve)
          && !(content instanceof StepSubedge)
          && !(content instanceof StepOrientedEdge)
          && !(content instanceof StepManifoldSolidBrep)
          && !(content instanceof StepBrepWithVoids)
          && !(content instanceof StepSweptAreaSolid)
          && !(content instanceof StepSolidReplica)
          && !(content instanceof StepCsgSolid)
          && !(content instanceof StepCsgPrimitive)
          && !(content instanceof StepBooleanResult)
          && !(content instanceof StepBooleanClippingResult)
          && !(content instanceof StepAdvancedFace)
          && !(content instanceof StepFaceSurface)
          && !(content instanceof StepOrientedFace)
          && !(content instanceof StepOpenShell)
          && !(content instanceof StepSurfacedOpenShell)
          && !(content instanceof StepOrientedOpenShell)
          && !(content instanceof StepClosedShell)
          && !(content instanceof StepOrientedClosedShell)
          && !(content instanceof StepConnectedFaceSet)
          && !(content instanceof StepConnectedFaceSubSet)
          && !(content instanceof StepFaceBasedSurfaceModel)
          && !(content instanceof StepShellBasedSurfaceModel)
          && !(content instanceof StepGeometricSet)
          && !(content instanceof StepGeometricCurveSet)
          && !(content instanceof StepPointSet)
          && !(content instanceof StepPath)
          && !(content instanceof StepOpenPath)
          && !(content instanceof StepSubpath)
          && !(content instanceof StepOrientedPath)
          && !(content instanceof StepConnectedEdgeSet)
          && !(content instanceof StepPolyLoop)
          && !(content instanceof StepVertexLoop)
          && !(content instanceof StepVertexShell)
          && !(content instanceof StepWireShell)
          && !(content instanceof StepEdgeLoop)
          && !(content instanceof StepEdgeBasedWireframeModel)
          && !(content instanceof StepShellBasedWireframeModel)
          && !(content instanceof StepFaceBasedSurfaceModel)
          && !(content instanceof StepShellBasedSurfaceModel)
          && !(content instanceof StepAnnotationSymbol)
          && !(content instanceof StepAnnotationText)
          && !(content instanceof StepAnnotationTextCharacter)
          && !(content instanceof StepAnnotationFillArea)
          && !(content instanceof StepAnnotationSymbolOccurrence)
          && !(content instanceof StepAnnotationSubfigureOccurrence)
          && !(content instanceof StepAnnotationFillAreaOccurrence)
          && !(content instanceof StepAnnotationPlaceholderOccurrence)
          && !(content instanceof StepAnnotationPlane)
          && !(content instanceof StepAnnotationCurveOccurrence)
          && !(content instanceof StepAnnotationPointOccurrence)
          && !(content instanceof StepDraughtingAnnotationOccurrence)
          && !(content instanceof StepTerminatorSymbol)) {
        throw new UnsupportedStepEntityException(
            entityName
                + " contents must reference supported annotation content, direct point carriers, supported curves/edge carriers, TERMINATOR_SYMBOL, supported face/shell/path/wire/model containers, GEOMETRIC_SET, POINT_SET or GEOMETRIC_CURVE_SET");
      }
    }
    return new StepDraughtingCallout(
        instance.id(),
        stringValue(instance, definition, 0),
        contents,
        entityName);
  }

  private StepDraughtingCalloutRelationship resolveDraughtingCalloutRelationship(
      StepEntityInstance instance) {
    return resolveDraughtingCalloutRelationship(instance, "DRAUGHTING_CALLOUT_RELATIONSHIP");
  }

  private StepDraughtingCalloutRelationship resolveDraughtingCalloutRelationship(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 4);
    return new StepDraughtingCalloutRelationship(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepDraughtingCallout.class,
            entityName + " relating_callout must reference DRAUGHTING_CALLOUT"),
        requireEntity(
            referenceId(instance, definition, 3),
            StepDraughtingCallout.class,
            entityName + " related_callout must reference DRAUGHTING_CALLOUT"));
  }

  private StepMeasureRepresentationItem resolveMeasureRepresentationItem(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MEASURE_REPRESENTATION_ITEM");
    requireParameterCount(instance, definition, 3);
    StepValue value = definition.parameters().get(1);
    if (!(value instanceof StepValue.TypedValue typedValue)) {
      throw new StepResolutionException(
          "MEASURE_REPRESENTATION_ITEM parameter 1 must be a typed measure value");
    }
    StepValue unwrapped = unwrapTyped(typedValue.value());
    if (!(unwrapped instanceof StepValue.NumberValue numberValue)) {
      throw new StepResolutionException(
          "MEASURE_REPRESENTATION_ITEM typed measure must wrap a number");
    }
    return new StepMeasureRepresentationItem(
        instance.id(),
        stringValue(instance, definition, 0),
        typedValue.typeName(),
        numberValue.value(),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepDescriptiveRepresentationItem resolveDescriptiveRepresentationItem(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DESCRIPTIVE_REPRESENTATION_ITEM");
    requireParameterCount(instance, definition, 2);
    return new StepDescriptiveRepresentationItem(
        instance.id(), stringValue(instance, definition, 0), stringValue(instance, definition, 1));
  }

  private StepValueRepresentationItem resolveValueRepresentationItem(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "VALUE_REPRESENTATION_ITEM");
    requireParameterCount(instance, definition, 2);
    StepValue value = definition.parameters().get(1);
    if (!(value instanceof StepValue.TypedValue typedValue)) {
      throw new StepResolutionException(
          "VALUE_REPRESENTATION_ITEM parameter 1 must be a typed value");
    }
    return new StepValueRepresentationItem(
        instance.id(),
        stringValue(instance, definition, 0),
        typedValue.typeName(),
        literalText(typedValue.value()));
  }

  private StepItemIdentifiedRepresentationUsage resolveItemIdentifiedRepresentationUsage(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ITEM_IDENTIFIED_REPRESENTATION_USAGE");
    requireParameterCount(instance, definition, 5);
    StepEntity identifiedItem = resolve(referenceId(instance, definition, 4));
    if (!isSupportedAssociationUsageIdentifiedItem(identifiedItem)) {
      throw new UnsupportedStepEntityException(
          "ITEM_IDENTIFIED_REPRESENTATION_USAGE identified item must reference supported point/geometric set, face, edge, path, loop, shell, model, solid, wire container or REPRESENTATION");
    }
    return new StepItemIdentifiedRepresentationUsage(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        requireEntity(
            referenceId(instance, definition, 3),
            StepRepresentation.class,
            "ITEM_IDENTIFIED_REPRESENTATION_USAGE used_representation must reference REPRESENTATION"),
        identifiedItem);
  }

  private StepChainBasedItemIdentifiedRepresentationUsage
      resolveChainBasedItemIdentifiedRepresentationUsage(StepEntityInstance instance) {
    StepEntityDefinition definition =
        definition(instance, "CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE");
    requireParameterCount(instance, definition, 6);
    List<StepRepresentation> nodes =
        referenceList(
            instance,
            definition,
            3,
            StepRepresentation.class,
            "CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE nodes must contain REPRESENTATION references");
    if (nodes.size() < 2) {
      throw new StepResolutionException(
          "CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE nodes must contain at least 2 representations");
    }
    List<StepRepresentationRelationship> undirectedLinks =
        referenceList(
            instance,
            definition,
            4,
            StepRepresentationRelationship.class,
            "CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE undirected_link must contain REPRESENTATION_RELATIONSHIP references");
    if (undirectedLinks.isEmpty()) {
      throw new StepResolutionException(
          "CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE undirected_link must not be empty");
    }
    StepEntity identifiedItem = resolve(referenceId(instance, definition, 5));
    if (!isSupportedAssociationUsageIdentifiedItem(identifiedItem)) {
      throw new UnsupportedStepEntityException(
          "CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE identified item must reference supported point/geometric set, face, edge, path, loop, shell, model, solid, wire container or REPRESENTATION");
    }
    return new StepChainBasedItemIdentifiedRepresentationUsage(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        nodes,
        undirectedLinks,
        identifiedItem);
  }

  private StepChainBasedGeometricItemSpecificUsage resolveChainBasedGeometricItemSpecificUsage(
      StepEntityInstance instance) {
    StepEntityDefinition definition =
        definition(instance, "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE");
    requireParameterCount(instance, definition, 6);
    StepEntity usage = resolve(referenceId(instance, definition, 2));
    if (!isSupportedAnnotationUsageItem(usage)) {
      throw new UnsupportedStepEntityException(
          "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE definition must reference DRAUGHTING_CALLOUT or supported annotation occurrence");
    }
    List<StepRepresentation> nodes =
        referenceList(
            instance,
            definition,
            3,
            StepRepresentation.class,
            "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE nodes must contain REPRESENTATION references");
    if (nodes.size() < 2) {
      throw new StepResolutionException(
          "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE nodes must contain at least 2 representations");
    }
    List<StepRepresentationRelationship> undirectedLinks =
        referenceList(
            instance,
            definition,
            4,
            StepRepresentationRelationship.class,
            "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE undirected_link must contain REPRESENTATION_RELATIONSHIP references");
    if (undirectedLinks.isEmpty()) {
      throw new StepResolutionException(
          "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE undirected_link must not be empty");
    }
    StepEntity identifiedItem = resolve(referenceId(instance, definition, 5));
    if (!isSupportedGeometricUsageIdentifiedItem(identifiedItem)) {
      throw new UnsupportedStepEntityException(
          "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE identified item must reference supported point/geometric set, face, edge, path, loop, shell, model, solid, wire container, annotation content/occurrence or REPRESENTATION");
    }
    return new StepChainBasedGeometricItemSpecificUsage(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        usage,
        nodes,
        undirectedLinks,
        identifiedItem);
  }

  private StepPmiRequirementItemAssociation resolvePmiRequirementItemAssociation(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PMI_REQUIREMENT_ITEM_ASSOCIATION");
    requireParameterCount(instance, definition, 6);
    StepEntity identifiedItem = resolve(referenceId(instance, definition, 4));
    if (!isSupportedAssociationUsageIdentifiedItem(identifiedItem)) {
      throw new UnsupportedStepEntityException(
          "PMI_REQUIREMENT_ITEM_ASSOCIATION identified item must reference supported point/geometric set, face, edge, path, loop, shell, model, solid, wire container or REPRESENTATION");
    }
    return new StepPmiRequirementItemAssociation(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        requireEntity(
            referenceId(instance, definition, 3),
            StepRepresentation.class,
            "PMI_REQUIREMENT_ITEM_ASSOCIATION used_representation must reference REPRESENTATION"),
        identifiedItem,
        resolve(referenceId(instance, definition, 5)));
  }

  private StepMechanicalDesignRequirementItemAssociation
      resolveMechanicalDesignRequirementItemAssociation(StepEntityInstance instance) {
    StepEntityDefinition definition =
        definition(instance, "MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION");
    requireParameterCount(instance, definition, 6);
    StepEntity identifiedItem = resolve(referenceId(instance, definition, 4));
    if (!isSupportedAssociationUsageIdentifiedItem(identifiedItem)) {
      throw new UnsupportedStepEntityException(
          "MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION identified item must reference supported point/geometric set, face, edge, path, loop, shell, model, solid, wire container or REPRESENTATION");
    }
    return new StepMechanicalDesignRequirementItemAssociation(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        requireEntity(
            referenceId(instance, definition, 3),
            StepRepresentation.class,
            "MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION used_representation must reference REPRESENTATION"),
        identifiedItem,
        resolve(referenceId(instance, definition, 5)));
  }

  private StepPlacedTarget resolvePlacedTarget(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PLACED_TARGET");
    requireParameterCount(instance, definition, 5);
    StepEntity identifiedItem = resolve(referenceId(instance, definition, 4));
    if (!isSupportedAssociationUsageIdentifiedItem(identifiedItem)) {
      throw new UnsupportedStepEntityException(
          "PLACED_TARGET identified item must reference supported point/geometric set, face, edge, path, loop, shell, model, solid, wire container or REPRESENTATION");
    }
    return new StepPlacedTarget(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        requireEntity(
            referenceId(instance, definition, 3),
            StepRepresentation.class,
            "PLACED_TARGET used_representation must reference REPRESENTATION"),
        identifiedItem);
  }

  private StepDraughtingModelItemAssociation resolveDraughtingModelItemAssociation(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DRAUGHTING_MODEL_ITEM_ASSOCIATION");
    requireParameterCount(instance, definition, 5);
    StepEntity identifiedItem = resolve(referenceId(instance, definition, 4));
    if (!isSupportedAssociationUsageIdentifiedItem(identifiedItem)) {
      throw new UnsupportedStepEntityException(
          "DRAUGHTING_MODEL_ITEM_ASSOCIATION identified item must reference supported point/geometric set, face, edge, path, loop, shell, model, solid, wire container or REPRESENTATION");
    }
    return new StepDraughtingModelItemAssociation(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        requireEntity(
            referenceId(instance, definition, 3),
            StepRepresentation.class,
            "DRAUGHTING_MODEL_ITEM_ASSOCIATION used_representation must reference REPRESENTATION"),
        identifiedItem);
  }

  private StepDraughtingModelItemAssociationWithPlaceholder
      resolveDraughtingModelItemAssociationWithPlaceholder(StepEntityInstance instance) {
    StepEntityDefinition definition =
        definition(instance, "DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER");
    requireParameterCount(instance, definition, 6);
    StepEntity identifiedItem = resolve(referenceId(instance, definition, 4));
    if (!isSupportedAnnotationUsageItem(identifiedItem)) {
      throw new StepResolutionException(
          "DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER identified_item must reference DRAUGHTING_CALLOUT or supported annotation content/occurrence");
    }
    return new StepDraughtingModelItemAssociationWithPlaceholder(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        requireEntity(
            referenceId(instance, definition, 3),
            StepRepresentation.class,
            "DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER used_representation must reference REPRESENTATION"),
        identifiedItem,
        requireEntity(
            referenceId(instance, definition, 5),
            StepAnnotationPlaceholderOccurrence.class,
            "DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER annotation_placeholder must reference ANNOTATION_PLACEHOLDER_OCCURRENCE"));
  }

  private StepGeometricItemSpecificUsage resolveGeometricItemSpecificUsage(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GEOMETRIC_ITEM_SPECIFIC_USAGE");
    requireParameterCount(instance, definition, 4);
    StepEntity usage = resolve(referenceId(instance, definition, 2));
    if (!isSupportedAnnotationUsageItem(usage)) {
      throw new UnsupportedStepEntityException(
          "GEOMETRIC_ITEM_SPECIFIC_USAGE usage must reference DRAUGHTING_CALLOUT or supported annotation content/occurrence");
    }
    StepEntity identifiedItem = resolve(referenceId(instance, definition, 3));
    if (!isSupportedGeometricUsageIdentifiedItem(identifiedItem)) {
      throw new UnsupportedStepEntityException(
          "GEOMETRIC_ITEM_SPECIFIC_USAGE identified item must reference supported point/geometric set, face, edge, path, loop, shell, model, solid, wire container, annotation content/occurrence or REPRESENTATION");
    }
    return new StepGeometricItemSpecificUsage(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        usage,
        identifiedItem);
  }

  private StepEntityDefinition definition(StepEntityInstance instance, String name) {
    return instance.requireDefinition(name);
  }

  private static void requireParameterCount(
      StepEntityInstance instance, StepEntityDefinition definition, int expected) {
    if (definition.parameters().size() != expected) {
      throw new StepResolutionException(
          definition.name()
              + " expects "
              + expected
              + " parameters but got "
              + definition.parameters().size()
              + " in entity #"
              + instance.id());
    }
  }

  private static void requireParameterCountIn(
      StepEntityInstance instance, StepEntityDefinition definition, int... expectedCounts) {
    int actual = definition.parameters().size();
    for (int expected : expectedCounts) {
      if (actual == expected) {
        return;
      }
    }
    StringBuilder expectedText = new StringBuilder();
    for (int i = 0; i < expectedCounts.length; i++) {
      if (i > 0) {
        expectedText.append(i == expectedCounts.length - 1 ? " or " : ", ");
      }
      expectedText.append(expectedCounts[i]);
    }
    throw new StepResolutionException(
        definition.name()
            + " expects "
            + expectedText
            + " parameters but got "
            + actual
            + " in entity #"
            + instance.id());
  }

  private String stringValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.StringValue stringValue) {
      return stringValue.value();
    }
    throw new StepResolutionException(
        definition.name() + " parameter " + index + " must be a string");
  }

  private String optionalStringValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = definition.parameters().get(index);
    if (isUnset(value)) {
      return "";
    }
    return stringValue(instance, definition, index);
  }

  private List<String> optionalStringListValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = definition.parameters().get(index);
    if (isUnset(value)) {
      return List.of();
    }
    StepValue unwrapped = unwrapTyped(value);
    if (!(unwrapped instanceof StepValue.ListValue listValue)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a string list");
    }
    List<String> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrappedElement = unwrapTyped(element);
      if (!(unwrappedElement instanceof StepValue.StringValue stringValue)) {
        throw new StepResolutionException(
            definition.name() + " string list must contain only strings");
      }
      result.add(stringValue.value());
    }
    return List.copyOf(result);
  }

  private double numberValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.NumberValue numberValue) {
      return numberValue.value();
    }
    throw new StepResolutionException(
        definition.name() + " parameter " + index + " must be a number");
  }

  private int integerValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    double value = numberValue(instance, definition, index);
    if (value != Math.rint(value)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be an integer");
    }
    return (int) value;
  }

  private Integer optionalIntegerValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = definition.parameters().get(index);
    if (isUnset(value)) {
      return null;
    }
    return integerValue(instance, definition, index);
  }

  private Double optionalNumberValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = definition.parameters().get(index);
    if (isUnset(value)) {
      return null;
    }
    return numberValue(instance, definition, index);
  }

  private StepDirection optionalDirectionReference(
      StepEntityInstance instance, StepEntityDefinition definition, int index, String message) {
    StepValue value = definition.parameters().get(index);
    if (isUnset(value)) {
      return null;
    }
    return requireEntity(referenceId(instance, definition, index), StepDirection.class, message);
  }

  private String enumValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.EnumValue enumValue) {
      return enumValue.value();
    }
    throw new StepResolutionException(
        definition.name() + " parameter " + index + " must be an enum");
  }

  private boolean booleanValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    return switch (enumValue(instance, definition, index)) {
      case "T" -> true;
      case "F" -> false;
      default ->
          throw new StepResolutionException(
              definition.name() + " parameter " + index + " must be .T. or .F.");
    };
  }

  private int referenceId(StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.ReferenceValue referenceValue) {
      return referenceValue.id();
    }
    throw new StepResolutionException(
        definition.name() + " parameter " + index + " must be a reference");
  }

  private List<Double> coordinateTriple(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    return coordinateList(instance, definition, index, 3, 3);
  }

  private List<Double> coordinateList(
      StepEntityInstance instance,
      StepEntityDefinition definition,
      int index,
      int minSize,
      int maxSize) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue listValue)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a list");
    }
    if (listValue.elements().size() < minSize || listValue.elements().size() > maxSize) {
      throw new UnsupportedStepEntityException(
          definition.name() + " only supports " + minSize + "D to " + maxSize + "D coordinates");
    }
    List<Double> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (unwrapped instanceof StepValue.NumberValue numberValue) {
        result.add(numberValue.value());
      } else {
        throw new StepResolutionException(
            definition.name() + " coordinate list must contain only numbers");
      }
    }
    return List.copyOf(result);
  }

  private List<Double> numberList(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue listValue)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a list");
    }
    List<Double> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.NumberValue numberValue)) {
        throw new StepResolutionException(
            definition.name() + " numeric list must contain only numbers");
      }
      result.add(numberValue.value());
    }
    return List.copyOf(result);
  }

  /**
   * Extracts a list of numbers from a pre-unwrapped StepValue.
   * Useful when the caller has already handled nested list unwrapping.
   */
  private List<Double> extractNumberList(StepEntityDefinition definition, StepValue value, String paramName) {
    if (!(value instanceof StepValue.ListValue listValue)) {
      throw new StepResolutionException(paramName + " parameter must be a list");
    }
    List<Double> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.NumberValue numberValue)) {
        throw new StepResolutionException(paramName + " numeric list must contain only numbers");
      }
      result.add(numberValue.value());
    }
    return List.copyOf(result);
  }

  private List<String> literalList(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue listValue)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a list");
    }
    List<String> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      result.add(literalText(element));
    }
    return List.copyOf(result);
  }

  private List<List<Double>> numberGrid(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue outerList)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a nested list");
    }
    List<List<Double>> grid = new ArrayList<>(outerList.elements().size());
    for (StepValue rowValue : outerList.elements()) {
      StepValue row = unwrapTyped(rowValue);
      if (!(row instanceof StepValue.ListValue rowList)) {
        throw new StepResolutionException(
            definition.name() + " numeric grid must contain only nested numeric lists");
      }
      List<Double> entries = new ArrayList<>(rowList.elements().size());
      for (StepValue element : rowList.elements()) {
        StepValue unwrapped = unwrapTyped(element);
        if (!(unwrapped instanceof StepValue.NumberValue numberValue)) {
          throw new StepResolutionException(
              definition.name() + " numeric grid must contain only numbers");
        }
        entries.add(numberValue.value());
      }
      grid.add(List.copyOf(entries));
    }
    return List.copyOf(grid);
  }

  private <T extends StepEntity> List<List<T>> referenceGrid(
      StepEntityInstance instance,
      StepEntityDefinition definition,
      int index,
      Class<T> type,
      String message) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue outerList)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a nested list");
    }
    List<List<T>> grid = new ArrayList<>(outerList.elements().size());
    for (StepValue rowValue : outerList.elements()) {
      StepValue row = unwrapTyped(rowValue);
      if (!(row instanceof StepValue.ListValue rowList)) {
        throw new StepResolutionException(message);
      }
      List<T> entries = new ArrayList<>(rowList.elements().size());
      for (StepValue element : rowList.elements()) {
        StepValue unwrapped = unwrapTyped(element);
        if (!(unwrapped instanceof StepValue.ReferenceValue referenceValue)) {
          throw new StepResolutionException(message);
        }
        entries.add(requireEntity(referenceValue.id(), type, message));
      }
      grid.add(List.copyOf(entries));
    }
    return List.copyOf(grid);
  }

  private List<Integer> integerList(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    List<Double> values = numberList(instance, definition, index);
    List<Integer> result = new ArrayList<>(values.size());
    for (double value : values) {
      if (value != Math.rint(value)) {
        throw new StepResolutionException(
            definition.name() + " integer list must contain only integers");
      }
      result.add((int) value);
    }
    return List.copyOf(result);
  }

  private boolean isUnset(StepValue value) {
    StepValue unwrapped = unwrapTyped(value);
    return unwrapped instanceof StepValue.OmittedValue
        || unwrapped instanceof StepValue.NotProvidedValue;
  }

  private StepValue unwrapTyped(StepValue value) {
    StepValue current = value;
    while (current instanceof StepValue.TypedValue typedValue) {
      current = typedValue.value();
    }
    return current;
  }

  private String literalText(StepValue value) {
    return switch (value) {
      case StepValue.StringValue stringValue -> stringValue.value();
      case StepValue.NumberValue numberValue -> numberValue.raw();
      case StepValue.EnumValue enumValue -> "." + enumValue.value() + ".";
      case StepValue.ReferenceValue referenceValue -> "#" + referenceValue.id();
      case StepValue.OmittedValue ignored -> "$";
      case StepValue.NotProvidedValue ignored -> "*";
      case StepValue.ListValue listValue ->
          listValue.elements().stream()
              .map(this::literalText)
              .collect(Collectors.joining(",", "(", ")"));
      case StepValue.TypedValue typedValue ->
          typedValue.typeName() + "(" + literalText(typedValue.value()) + ")";
    };
  }

  private <T extends StepEntity> T requireEntity(int id, Class<T> type, String message) {
    StepEntity entity = resolve(id);
    if (!type.isInstance(entity)) {
      throw new StepResolutionException(message + " but got " + entity.getClass().getSimpleName());
    }
    return type.cast(entity);
  }

  private StepEntity requireVertexLike(int id, String message) {
    StepEntity entity = resolve(id);
    if (!(entity instanceof StepVertex) && !(entity instanceof StepVertexPoint)) {
      throw new StepResolutionException(message + " but got " + entity.getClass().getSimpleName());
    }
    return entity;
  }

  private boolean isAnnotationOccurrence(StepEntity entity) {
    return entity instanceof StepAnnotationTextOccurrence
        || entity instanceof StepAnnotationPointOccurrence
        || entity instanceof StepAnnotationCurveOccurrence
        || entity instanceof StepLeaderCurve
        || entity instanceof StepProjectionCurve
        || entity instanceof StepDimensionCurve
        || entity instanceof StepAnnotationFillAreaOccurrence
        || entity instanceof StepAnnotationPlaceholderOccurrence
        || entity instanceof StepAnnotationPlane
        || entity instanceof StepAnnotationSymbolOccurrence
        || entity instanceof StepAnnotationSubfigureOccurrence
        || entity instanceof StepDraughtingAnnotationOccurrence
        || entity instanceof StepTerminatorSymbol;
  }

  private boolean isSupportedAnnotationUsageItem(StepEntity entity) {
    return entity instanceof StepDraughtingCallout || isSupportedAnnotationWrapperItem(entity);
  }

  private boolean isSupportedGeometricUsageIdentifiedItem(StepEntity entity) {
    return entity instanceof StepFaceEntity
        || entity instanceof StepEdgeCurve
        || entity instanceof StepSubedge
        || entity instanceof StepOrientedEdge
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
        || entity instanceof StepVertexLoop
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
        || isSupportedAnnotationWrapperItem(entity)
        || entity instanceof StepRepresentation;
  }

  private boolean isSupportedAssociationUsageIdentifiedItem(StepEntity entity) {
    return isSupportedGeometricUsageIdentifiedItem(entity)
        || isSupportedAnnotationUsageItem(entity)
        || entity instanceof StepPropertyDefinition
        || entity instanceof StepPropertyDefinitionRelationship
        || entity instanceof StepProductDefinition
        || entity instanceof StepProductDefinitionShape
        || entity instanceof StepShapeDefinitionRepresentation
        || entity instanceof StepContextDependentShapeRepresentation
        || entity instanceof StepProductDefinitionEffectivity
        || entity instanceof StepProductDefinitionRelationship
        || entity instanceof StepProductDefinitionFormation
        || entity instanceof StepProductDefinitionFormationRelationship
        || entity instanceof StepShapeAspect
        || entity instanceof StepShapeAspectOccurrence
        || entity instanceof StepShapeAspectRelationship
        || entity instanceof StepPlacedDatumTargetFeature;
  }

  private boolean isSupportedCurveReference(StepEntity entity) {
    return entity instanceof StepLine
        || entity instanceof StepLineSegment
        || entity instanceof StepCircle
        || entity instanceof StepEllipse
        || entity instanceof StepConicCurve
        || entity instanceof StepPolyline
        || entity instanceof StepOffsetCurve2D
        || entity instanceof StepOffsetCurve3D
        || entity instanceof StepOrientedCurve
        || entity instanceof StepCompositeCurve
        || entity instanceof StepCompositeCurveOnSurface
        || entity instanceof StepCompositeCurveOnSurface3D
        || entity instanceof StepCurve
        || entity instanceof StepBoundedCurve
        || entity instanceof StepBSplineCurve
        || entity instanceof StepBSplineCurveWithKnots
        || entity instanceof StepRationalBSplineCurve
        || entity instanceof StepBezierCurve
        || entity instanceof StepPiecewiseBezierCurve
        || entity instanceof StepUniformCurve
        || entity instanceof StepQuasiUniformCurve
        || entity instanceof StepTrimmedCurve
        || entity instanceof StepPcurve
        || entity instanceof StepDegeneratePcurve
        || entity instanceof StepSurfaceCurve
        || entity instanceof StepSeamCurve
        || entity instanceof StepAnnotationCurveOccurrence
        || entity instanceof StepLeaderCurve
        || entity instanceof StepProjectionCurve
        || entity instanceof StepDimensionCurve
        || entity instanceof StepDraughtingAnnotationOccurrence
        || entity instanceof StepTerminatorSymbol
        || entity instanceof StepClothoid
        || entity instanceof StepIndexedPolyCurve
        || entity instanceof StepDegenerateCurve
        || entity instanceof StepEdgeWire
        || entity instanceof StepSweptDiskSolid
        || entity instanceof StepCurve2D
        || entity instanceof StepMappedItem
        || (entity instanceof StepGeometricReplica replica
            && "CURVE_REPLICA".equals(replica.entityName()));
  }

  private boolean isSupportedSurfaceReference(StepEntity entity) {
    return entity instanceof StepPlane
        || entity instanceof StepSurface
        || entity instanceof StepBoundedSurface
        || entity instanceof StepOffsetSurface
        || entity instanceof StepOffsetSurface2
        || entity instanceof StepBSplineSurface
        || entity instanceof StepBSplineSurfaceWithKnots
        || entity instanceof StepRationalBSplineSurface
        || entity instanceof StepBezierSurface
        || entity instanceof StepPiecewiseBezierSurface
        || entity instanceof StepUniformSurface
        || entity instanceof StepQuasiUniformSurface
        || entity instanceof StepCylindricalSurface
        || entity instanceof StepConicalSurface
        || entity instanceof StepToroidalSurface
        || entity instanceof StepDegenerateToroidalSurface
        || entity instanceof StepSphericalSurface
        || entity instanceof StepSurfaceOfLinearExtrusion
        || entity instanceof StepSurfaceOfRevolution
        || entity instanceof StepSurfaceOfConstantRadius
        || entity instanceof StepRectangularTrimmedSurface
        || entity instanceof StepRectangularCompositeSurface
        || entity instanceof StepSurfacePatch
        || entity instanceof StepCurveBoundedSurface
        || entity instanceof StepOrientedSurface
        || entity instanceof StepSubface
        || entity instanceof StepMappedItem
        || (entity instanceof StepGeometricReplica replica
            && "SURFACE_REPLICA".equals(replica.entityName()));
  }

  private <T extends StepEntity> List<T> referenceList(
      StepEntityInstance instance,
      StepEntityDefinition definition,
      int index,
      Class<T> type,
      String message) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue listValue)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a list");
    }
    List<T> result = new ArrayList<>();
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.ReferenceValue referenceValue)) {
        throw new StepResolutionException(message);
      }
      result.add(requireEntity(referenceValue.id(), type, message));
    }
    return List.copyOf(result);
  }

  private List<StepEntity> entityReferenceList(
      StepEntityInstance instance, StepEntityDefinition definition, int index, String message) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue listValue)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a list");
    }
    List<StepEntity> result = new ArrayList<>();
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.ReferenceValue referenceValue)) {
        throw new StepResolutionException(message);
      }
      result.add(resolve(referenceValue.id()));
    }
    return List.copyOf(result);
  }

  private String deriveUnitKind(StepEntityInstance instance) {
    for (String candidate : List.of(
        "LENGTH_UNIT",
        "PLANE_ANGLE_UNIT",
        "SOLID_ANGLE_UNIT",
        "RATIO_UNIT",
        "AREA_UNIT",
        "VOLUME_UNIT",
        "TIME_UNIT",
        "THERMODYNAMIC_TEMPERATURE_UNIT",
        "ELECTRIC_CURRENT_UNIT",
        "AMOUNT_OF_SUBSTANCE_UNIT",
        "LUMINOUS_FLUX_UNIT",
        "LUMINOUS_INTENSITY_UNIT",
        "ACCELERATION_UNIT",
        "VELOCITY_UNIT",
        "THERMAL_RESISTANCE_UNIT",
        "MASS_DENSITY_UNIT",
        "DYNAMIC_VISCOSITY_UNIT",
        "KINEMATIC_VISCOSITY_UNIT",
        "MOMENT_OF_INERTIA_UNIT",
        "THERMAL_CONDUCTIVITY_UNIT",
        "HEAT_FLUX_DENSITY_UNIT",
        "SPECIFIC_HEAT_CAPACITY_UNIT",
        "AREA_DENSITY_UNIT",
        "VOLUMETRIC_FLOW_RATE_UNIT",
        "MASS_FLOW_RATE_UNIT",
        "ROTATIONAL_FREQUENCY_UNIT",
        "ANGULAR_VELOCITY_UNIT",
        "ANGULAR_ACCELERATION_UNIT",
        "TORQUE_UNIT",
        "LINEAR_FORCE_UNIT",
        "LINEAR_STIFFNESS_UNIT",
        "ROTATIONAL_STIFFNESS_UNIT",
        "LINEAR_MOMENT_UNIT",
        "FREQUENCY_UNIT",
        "FORCE_UNIT",
        "PRESSURE_UNIT",
        "ENERGY_UNIT",
        "POWER_UNIT",
        "ELECTRIC_CHARGE_UNIT",
        "ELECTRIC_POTENTIAL_UNIT",
        "CAPACITANCE_UNIT",
        "RESISTANCE_UNIT",
        "CONDUCTANCE_UNIT",
        "MAGNETIC_FLUX_UNIT",
        "MAGNETIC_FLUX_DENSITY_UNIT",
        "INDUCTANCE_UNIT",
        "ILLUMINANCE_UNIT",
        "RADIOACTIVITY_UNIT",
        "ABSORBED_DOSE_UNIT",
        "DOSE_EQUIVALENT_UNIT")) {
      if (instance.hasDefinition(candidate)) {
        return candidate;
      }
    }
    if (instance.hasDefinition("MASS_UNIT")) {
      return "MASS_UNIT";
    }
    return "NAMED_UNIT";
  }

  private boolean matchesUnitKind(StepEntity entity, String expectedUnitKind) {
    if (entity instanceof StepNamedUnit namedUnit) {
      return expectedUnitKind.equals(namedUnit.unitKind());
    }
    if (entity instanceof StepSiUnit siUnit) {
      return expectedUnitKind.equals(siUnit.unitKind());
    }
    if (entity instanceof StepConversionBasedUnit conversionBasedUnit) {
      return expectedUnitKind.equals(conversionBasedUnit.unitKind());
    }
    if (entity instanceof StepConversionBasedUnitWithOffset conversionBasedUnitWithOffset) {
      return expectedUnitKind.equals(conversionBasedUnitWithOffset.unitKind());
    }
    if (entity instanceof StepContextDependentUnit contextDependentUnit) {
      return expectedUnitKind.equals(contextDependentUnit.unitKind());
    }
    if (entity instanceof StepDerivedUnit derivedUnit) {
      return expectedUnitKind.equals(derivedUnit.unitKind());
    }
    return false;
  }

  private String inheritedRepresentationItemName(StepEntityInstance instance) {
    return instance.hasDefinition("REPRESENTATION_ITEM")
        ? stringValue(instance, definition(instance, "REPRESENTATION_ITEM"), 0)
        : "";
  }

  private String inheritedTopologicalRepresentationItemName(StepEntityInstance instance) {
    return instance.hasDefinition("TOPOLOGICAL_REPRESENTATION_ITEM")
        ? stringValue(instance, definition(instance, "TOPOLOGICAL_REPRESENTATION_ITEM"), 0)
        : inheritedRepresentationItemName(instance);
  }

  private int inheritedStyledItemTargetId(StepEntityInstance instance) {
    if (!instance.hasDefinition("STYLED_ITEM")) {
      throw new StepResolutionException("complex entity is missing STYLED_ITEM definition");
    }
    StepEntityDefinition definition = definition(instance, "STYLED_ITEM");
    requireParameterCount(instance, definition, 3);
    return referenceId(instance, definition, 2);
  }

  private static void registerShapeAspectAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName, (resolver, instance) -> resolver.resolveShapeAspect(instance, entityName));
    }
  }

  private static void registerShapeAspectOccurrenceAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName,
          (resolver, instance) -> resolver.resolveShapeAspectOccurrence(instance, entityName));
    }
  }

  private static void registerCharacterizedObjectAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName,
          (resolver, instance) -> resolver.resolveCharacterizedObject(instance, entityName));
    }
  }

  private static void registerExternallyDefinedItemAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName,
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, entityName));
    }
  }

  private static void registerProductDefinitionRelationshipAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName,
          (resolver, instance) ->
              resolver.resolveProductDefinitionRelationship(instance, entityName));
    }
  }

  private static void registerProductDefinitionRelationshipRelationshipAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName,
          (resolver, instance) ->
              resolver.resolveProductDefinitionRelationshipRelationship(instance, entityName));
    }
  }

  private static void registerTypedMeasureWithUnit(
      Map<String, EntityFactory> registry, String entityName, String expectedUnitKind) {
    registry.put(
        entityName,
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(instance, entityName, expectedUnitKind));
  }

  private static void registerTypedMeasureWithUnitPairs(
      Map<String, EntityFactory> registry, String... unitKinds) {
    for (String unitKind : unitKinds) {
      String measureName = unitKind.replace("_UNIT", "_MEASURE_WITH_UNIT");
      registerTypedMeasureWithUnit(registry, measureName, unitKind);
    }
  }

  private static void registerStandaloneDerivedUnitKinds(
      Map<String, EntityFactory> registry, String... unitKinds) {
    for (String unitKind : unitKinds) {
      registry.put(
          unitKind,
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, unitKind));
    }
  }

  private static void registerShapeAspectRelationshipAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName,
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, entityName));
    }
  }

  private static void registerRepresentationRelationshipAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName,
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(instance, entityName));
    }
  }

  private static void registerRepresentationAliases(
      Map<String, EntityFactory> registry, boolean shapeRepresentation, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName,
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, entityName, shapeRepresentation));
    }
  }

  private static Map<String, EntityFactory> createRegistry() {
    // Resolution order matters for complex entities such as
    // (LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT(...)).
    // Prefer the more specific entity factories inserted earlier here.
    Map<String, EntityFactory> registry = new LinkedHashMap<>();
    registry.put(
        "GEOMETRIC_REPRESENTATION_CONTEXT",
        StepEntityResolver::resolveGeometricRepresentationContext);
    registry.put(
        "SHAPE_REPRESENTATION",
        (resolver, instance) -> resolver.resolveRepresentation(instance, true));
    registerRepresentationAliases(
        registry,
        true,
        "GEOMETRIC_SET_SHAPE_REPRESENTATION",
        "SHELL_BASED_SURFACE_MODEL_SHAPE_REPRESENTATION",
        "SURFACE_MODEL_SHAPE_REPRESENTATION");
    registry.put(
        "ADVANCED_BREP_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "ADVANCED_BREP_SHAPE_REPRESENTATION", true));
    registry.put(
        "BEVELED_SHEET_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "BEVELED_SHEET_REPRESENTATION", true));
    registry.put(
        "ELEMENTARY_BREP_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "ELEMENTARY_BREP_SHAPE_REPRESENTATION", true));
    registry.put(
        "COMPOSITE_SHEET_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "COMPOSITE_SHEET_REPRESENTATION", true));
    registry.put(
        "FACETED_BREP_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "FACETED_BREP_SHAPE_REPRESENTATION", true));
    registry.put(
        "BLOCK_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "BLOCK_SHAPE_REPRESENTATION", true));
    registry.put(
        "CSG_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "CSG_SHAPE_REPRESENTATION", true));
    registry.put(
        "CSG_2D_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "CSG_2D_SHAPE_REPRESENTATION", true));
    registry.put(
        "SINGLE_AREA_CSG_2D_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "SINGLE_AREA_CSG_2D_SHAPE_REPRESENTATION", true));
    registry.put(
        "SINGLE_BOUNDARY_CSG_2D_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "SINGLE_BOUNDARY_CSG_2D_SHAPE_REPRESENTATION", true));
    registry.put(
        "CURVE_SWEPT_SOLID_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "CURVE_SWEPT_SOLID_SHAPE_REPRESENTATION", true));
    registry.put(
        "CYLINDRICAL_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "CYLINDRICAL_SHAPE_REPRESENTATION", true));
    registry.put(
        "DIRECTION_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "DIRECTION_SHAPE_REPRESENTATION", true));
    registry.put("BOOLEAN_CLIPPING_RESULT", StepEntityResolver::resolveBooleanClippingResult);
    registry.put("BOOLEAN_RESULT", StepEntityResolver::resolveBooleanResult);
    registry.put("CSG_SOLID", StepEntityResolver::resolveCsgSolid);
    registry.put("CSG_VOLUME", StepEntityResolver::resolveCsgVolume);
    registry.put("BLOCK_VOLUME", StepEntityResolver::resolveBlockVolume);
    // Advanced CSG volumes
    registry.put("CYLINDER_VOLUME", StepEntityResolver::resolveCylinderVolume);
    registry.put("SPHERE_VOLUME", StepEntityResolver::resolveSphereVolume);
    registry.put("TORUS_VOLUME", StepEntityResolver::resolveTorusVolume);
    registry.put("PRISM_VOLUME", StepEntityResolver::resolvePrismVolume);
    registry.put("RIGHT_CIRCULAR_CYLINDER_VOLUME", StepEntityResolver::resolveCylinderVolume);
    registry.put("RIGHT_CIRCULAR_CONE_VOLUME", StepEntityResolver::resolveCylinderVolume);
    registry.put("SOLID_REPLICA", StepEntityResolver::resolveSolidReplica);
    registry.put(
        "BLOCK",
        (resolver, instance) ->
            resolver.resolveCsgPrimitive(
                instance, "BLOCK", StepAxis2Placement3D.class, "AXIS2_PLACEMENT_3D", 3));
    registry.put(
        "SPHERE",
        (resolver, instance) ->
            resolver.resolveCsgPrimitive(
                instance, "SPHERE", StepAxis2Placement3D.class, "AXIS2_PLACEMENT_3D", 1));
    registry.put(
        "ELLIPSOID",
        (resolver, instance) ->
            resolver.resolveCsgPrimitive(
                instance, "ELLIPSOID", StepAxis2Placement3D.class, "AXIS2_PLACEMENT_3D", 3));
    registry.put(
        "RIGHT_ANGULAR_WEDGE",
        (resolver, instance) ->
            resolver.resolveCsgPrimitive(
                instance,
                "RIGHT_ANGULAR_WEDGE",
                StepAxis2Placement3D.class,
                "AXIS2_PLACEMENT_3D",
                4));
    registry.put(
        "RIGHT_CIRCULAR_CYLINDER",
        (resolver, instance) ->
            resolver.resolveCsgPrimitive(
                instance,
                "RIGHT_CIRCULAR_CYLINDER",
                StepAxis1Placement.class,
                "AXIS1_PLACEMENT",
                2));
    registry.put(
        "TORUS",
        (resolver, instance) ->
            resolver.resolveCsgPrimitive(
                instance, "TORUS", StepAxis1Placement.class, "AXIS1_PLACEMENT", 2));
    registry.put(
        "RIGHT_CIRCULAR_CONE",
        (resolver, instance) ->
            resolver.resolveCsgPrimitive(
                instance, "RIGHT_CIRCULAR_CONE", StepAxis2Placement3D.class, "AXIS2_PLACEMENT_3D", 2));
    registry.put("CIRCLE_PROFILE_DEF", StepEntityResolver::resolveCircleProfileDef);
    registry.put("RECTANGLE_PROFILE_DEF", StepEntityResolver::resolveRectangleProfileDef);
    registry.put(
        "RECTANGLE_HOLLOW_PROFILE_DEF",
        (resolver, instance) ->
            resolver.resolveParameterizedProfileDef(instance, "RECTANGLE_HOLLOW_PROFILE_DEF", 4));
    registry.put(
        "CENTERED_RECTANGLE_PROFILE_DEF",
        (resolver, instance) ->
            resolver.resolveParameterizedProfileDef(
                instance, "CENTERED_RECTANGLE_PROFILE_DEF", 2));
    registry.put(
        "CIRCULAR_HOLLOW_PROFILE_DEF",
        (resolver, instance) ->
            resolver.resolveParameterizedProfileDef(instance, "CIRCULAR_HOLLOW_PROFILE_DEF", 2));
    registry.put("POINT_REPLICA", (resolver, instance) -> resolver.resolveGeometricReplica(instance, "POINT_REPLICA"));
    registry.put("CURVE_REPLICA", (resolver, instance) -> resolver.resolveGeometricReplica(instance, "CURVE_REPLICA"));
    registry.put("SURFACE_REPLICA", (resolver, instance) -> resolver.resolveGeometricReplica(instance, "SURFACE_REPLICA"));
    registry.put(
        "ELLIPSE_PROFILE_DEF",
        (resolver, instance) -> resolver.resolveParameterizedProfileDef(
            instance, "ELLIPSE_PROFILE_DEF", 2));
    registry.put(
        "ROUNDED_RECTANGLE_PROFILE_DEF",
        (resolver, instance) -> resolver.resolveParameterizedProfileDef(
            instance, "ROUNDED_RECTANGLE_PROFILE_DEF", 3));
    registry.put(
        "CENTRE_LINE_ARC_PROFILE_DEF",
        (resolver, instance) ->
            resolver.resolveParameterizedProfileDef(instance, "CENTRE_LINE_ARC_PROFILE_DEF", 2));
    registry.put(
        "CENTERED_CIRCLE_PROFILE_DEF",
        (resolver, instance) ->
            resolver.resolveParameterizedProfileDef(instance, "CENTERED_CIRCLE_PROFILE_DEF", 2));
    registry.put(
        "ARBITRARY_CLOSED_PROFILE_DEF",
        StepEntityResolver::resolveArbitraryClosedProfileDef);
    registry.put(
        "ARBITRARY_PROFILE_DEF",
        (resolver, instance) ->
            resolver.resolveArbitraryProfileDef(instance, "ARBITRARY_PROFILE_DEF"));
    registry.put(
        "ARBITRARY_PROFILE_DEF_WITH_VOIDS",
        StepEntityResolver::resolveArbitraryProfileDefWithVoids);
    registry.put(
        "ARBITRARY_OPEN_PROFILE_DEF",
        (resolver, instance) ->
            resolver.resolveArbitraryProfileDef(instance, "ARBITRARY_OPEN_PROFILE_DEF"));
    // Standard structural steel profile definitions (Phase 2E)
    registry.put(
        "I_SHAPE_PROFILE_DEF",
        (resolver, instance) ->
            resolver.resolveParameterizedProfileDef(instance, "I_SHAPE_PROFILE_DEF", 6));
    registry.put(
        "T_SHAPE_PROFILE_DEF",
        (resolver, instance) ->
            resolver.resolveParameterizedProfileDef(instance, "T_SHAPE_PROFILE_DEF", 5));
    registry.put(
        "L_SHAPE_PROFILE_DEF",
        (resolver, instance) ->
            resolver.resolveParameterizedProfileDef(instance, "L_SHAPE_PROFILE_DEF", 4));
    registry.put(
        "U_SHAPE_PROFILE_DEF",
        (resolver, instance) ->
            resolver.resolveParameterizedProfileDef(instance, "U_SHAPE_PROFILE_DEF", 5));
    registry.put(
        "C_SHAPE_PROFILE_DEF",
        (resolver, instance) ->
            resolver.resolveParameterizedProfileDef(instance, "C_SHAPE_PROFILE_DEF", 5));
    registry.put(
        "Z_SHAPE_PROFILE_DEF",
        (resolver, instance) ->
            resolver.resolveParameterizedProfileDef(instance, "Z_SHAPE_PROFILE_DEF", 5));
    registry.put(
        "HAT_SHAPE_PROFILE_DEF",
        (resolver, instance) ->
            resolver.resolveParameterizedProfileDef(instance, "HAT_SHAPE_PROFILE_DEF", 5));
    registry.put(
        "ANGLE_PROFILE_DEF",
        (resolver, instance) ->
            resolver.resolveParameterizedProfileDef(instance, "ANGLE_PROFILE_DEF", 4));
    registry.put(
        "CHANNEL_PROFILE_DEF",
        (resolver, instance) ->
            resolver.resolveParameterizedProfileDef(instance, "CHANNEL_PROFILE_DEF", 5));
    registry.put(
        "TEE_PROFILE_DEF",
        (resolver, instance) ->
            resolver.resolveParameterizedProfileDef(instance, "TEE_PROFILE_DEF", 5));
    registry.put(
        "I_PROFILE_DEF",
        (resolver, instance) ->
            resolver.resolveParameterizedProfileDef(instance, "I_PROFILE_DEF", 6));
    registry.put(
        "L_PROFILE_DEF",
        (resolver, instance) ->
            resolver.resolveParameterizedProfileDef(instance, "L_PROFILE_DEF", 4));
    registry.put(
        "T_PROFILE_DEF",
        (resolver, instance) ->
            resolver.resolveParameterizedProfileDef(instance, "T_PROFILE_DEF", 5));
    registry.put(
        "U_PROFILE_DEF",
        (resolver, instance) ->
            resolver.resolveParameterizedProfileDef(instance, "U_PROFILE_DEF", 5));
    registry.put(
        "Z_PROFILE_DEF",
        (resolver, instance) ->
            resolver.resolveParameterizedProfileDef(instance, "Z_PROFILE_DEF", 5));
    registry.put(
        "FLAT_BAR_PROFILE_DEF",
        (resolver, instance) ->
            resolver.resolveParameterizedProfileDef(instance, "FLAT_BAR_PROFILE_DEF", 2));
    registry.put(
        "DOVE_TAIL_PROFILE_DEF",
        (resolver, instance) ->
            resolver.resolveParameterizedProfileDef(instance, "DOVE_TAIL_PROFILE_DEF", 4));
    registry.put("EXTRUDED_AREA_SOLID", StepEntityResolver::resolveExtrudedAreaSolid);
    registry.put("REVOLVED_AREA_SOLID", StepEntityResolver::resolveRevolvedAreaSolid);
    // Swept face solids
    registry.put("EXTRUDED_FACE_SOLID", StepEntityResolver::resolveExtrudedFaceSolid);
    registry.put("REVOLVED_FACE_SOLID", StepEntityResolver::resolveRevolvedFaceSolid);
    registry.put("SURFACE_CURVE_SWEPT_FACE_SOLID", (resolver, instance) ->
        resolver.resolveSweptFaceSolid(instance, "SURFACE_CURVE_SWEPT_FACE_SOLID"));
    registry.put("SWEPT_FACE_SOLID", (resolver, instance) ->
        resolver.resolveSweptFaceSolid(instance, "SWEPT_FACE_SOLID"));
    registry.put("BOX_DOMAIN", StepEntityResolver::resolveBoxDomain);
    registry.put("HALF_SPACE_SOLID", StepEntityResolver::resolveHalfSpaceSolid);
    registry.put("BOXED_HALF_SPACE", StepEntityResolver::resolveBoxedHalfSpace);
    registry.put("POLYGONAL_BOUNDED_HALF_SPACE", StepEntityResolver::resolvePolygonalBoundedHalfSpace);
    registry.put("SWEPT_DISK_SOLID", StepEntityResolver::resolveSweptDiskSolid);
    registry.put("REVOLVED_AREA_SOLID_TAPERED", StepEntityResolver::resolveRevolvedAreaSolidTapered);
    registry.put("EXTRUDED_AREA_SOLID_TAPERED", StepEntityResolver::resolveExtrudedAreaSolidTapered);
    registry.put("SURFACE_CURVE_SWEPT_AREA_SOLID", StepEntityResolver::resolveSurfaceCurveSweptAreaSolid);
    registry.put(
        "FACETED_BREP",
        (resolver, instance) -> resolver.resolveManifoldSolidBrep(instance, "FACETED_BREP"));
    registry.put("BREP_WITH_VOIDS", StepEntityResolver::resolveBrepWithVoids);
    registry.put("ADVANCED_BREP", StepEntityResolver::resolveAdvancedBrep);
    registry.put("COMPLEX_CLIPPING_RESULT", StepEntityResolver::resolveComplexClippingResult);
    registry.put(
        "EDGE_BASED_WIREFRAME_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "EDGE_BASED_WIREFRAME_SHAPE_REPRESENTATION", true));
    registry.put(
        "GEOMETRICALLY_BOUNDED_WIREFRAME_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "GEOMETRICALLY_BOUNDED_WIREFRAME_SHAPE_REPRESENTATION", true));
    registry.put(
        "GEOMETRICALLY_BOUNDED_2D_WIREFRAME_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "GEOMETRICALLY_BOUNDED_2D_WIREFRAME_REPRESENTATION", true));
    registry.put(
        "SHELL_BASED_WIREFRAME_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "SHELL_BASED_WIREFRAME_SHAPE_REPRESENTATION", true));
    registry.put(
        "MANIFOLD_SURFACE_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "MANIFOLD_SURFACE_SHAPE_REPRESENTATION", true));
    registry.put(
        "MANIFOLD_SUBSURFACE_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "MANIFOLD_SUBSURFACE_SHAPE_REPRESENTATION", true));
    registry.put(
        "SURFACE_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "SURFACE_SHAPE_REPRESENTATION", true));
    registry.put(
        "GEOMETRICALLY_BOUNDED_SURFACE_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "GEOMETRICALLY_BOUNDED_SURFACE_SHAPE_REPRESENTATION", true));
    registry.put(
        "COMPOUND_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "COMPOUND_SHAPE_REPRESENTATION", true));
    registry.put(
        "PLANAR_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "PLANAR_SHAPE_REPRESENTATION", true));
    registry.put(
        "POINT_PLACEMENT_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "POINT_PLACEMENT_SHAPE_REPRESENTATION", true));
    registry.put(
        "SHAPE_DIMENSION_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "SHAPE_DIMENSION_REPRESENTATION", true));
    registry.put(
        "SHAPE_REPRESENTATION_WITH_PARAMETERS",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "SHAPE_REPRESENTATION_WITH_PARAMETERS", true));
    registry.put(
        "LOCATION_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "LOCATION_SHAPE_REPRESENTATION", true));
    registry.put(
        "REPRESENTATIVE_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "REPRESENTATIVE_SHAPE_REPRESENTATION", true));
    registry.put(
        "NEUTRAL_SKETCH_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "NEUTRAL_SKETCH_REPRESENTATION", true));
    registry.put(
        "PROCEDURAL_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "PROCEDURAL_SHAPE_REPRESENTATION", true));
    registry.put(
        "TESSELLATED_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "TESSELLATED_SHAPE_REPRESENTATION", true));
    registry.put(
        "TESSELLATED_SHAPE_REPRESENTATION_WITH_ACCURACY_PARAMETERS",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "TESSELLATED_SHAPE_REPRESENTATION_WITH_ACCURACY_PARAMETERS", true));
    registry.put(
        "NGON_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "NGON_SHAPE_REPRESENTATION", true));
    registry.put(
        "SCAN_DATA_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "SCAN_DATA_SHAPE_REPRESENTATION", true));
    registry.put(
        "PATH_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "PATH_SHAPE_REPRESENTATION", true));
    registry.put(
        "WIREFRAME_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "WIREFRAME_SHAPE_REPRESENTATION", true));
    registry.put(
        "FACE_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "FACE_SHAPE_REPRESENTATION", true));
    registry.put(
        "NON_MANIFOLD_SURFACE_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "NON_MANIFOLD_SURFACE_SHAPE_REPRESENTATION", true));
    registry.put(
        "MECHANICAL_DESIGN_GEOMETRIC_PRESENTATION_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "MECHANICAL_DESIGN_GEOMETRIC_PRESENTATION_REPRESENTATION", false));
    registry.put(
        "MECHANICAL_DESIGN_SHADED_PRESENTATION_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "MECHANICAL_DESIGN_SHADED_PRESENTATION_REPRESENTATION", false));
    registry.put(
        "MECHANICAL_DESIGN_PRESENTATION_REPRESENTATION_WITH_DRAUGHTING",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "MECHANICAL_DESIGN_PRESENTATION_REPRESENTATION_WITH_DRAUGHTING", false));
    registry.put(
        "MECHANICAL_DESIGN_GEOMETRIC_PRESENTATION_AREA",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "MECHANICAL_DESIGN_GEOMETRIC_PRESENTATION_AREA", false));
    registry.put(
        "MECHANICAL_DESIGN_GEOMETRIC_PRESENTATION_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "MECHANICAL_DESIGN_GEOMETRIC_PRESENTATION_REPRESENTATION", false));
    registry.put(
        "MECHANICAL_DESIGN_SHADED_PRESENTATION_AREA",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "MECHANICAL_DESIGN_SHADED_PRESENTATION_AREA", false));
    registry.put(
        "VISUAL_APPEARANCE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "VISUAL_APPEARANCE_REPRESENTATION", false));
    registry.put(
        "PRESENTATION_AREA",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "PRESENTATION_AREA", false));
    registry.put(
        "PRESENTATION_VIEW",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "PRESENTATION_VIEW", false));
    registry.put(
        "SYMBOL_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "SYMBOL_REPRESENTATION", false));
    registry.put(
        "PRESENTATION_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "PRESENTATION_REPRESENTATION", false));
    registry.put(
        "PICTURE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "PICTURE_REPRESENTATION", false));
    registry.put(
        "TEXT_STRING_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "TEXT_STRING_REPRESENTATION", false));
    registry.put(
        "STRUCTURED_TEXT_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "STRUCTURED_TEXT_REPRESENTATION", false));
    registry.put(
        "PROCEDURAL_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "PROCEDURAL_REPRESENTATION", false));
    registry.put(
        "CLOSED_CURVE_STYLE_PARAMETERS",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "CLOSED_CURVE_STYLE_PARAMETERS", false));
    registry.put(
        "CURVE_STYLE_PARAMETERS_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "CURVE_STYLE_PARAMETERS_REPRESENTATION", false));
    registry.put(
        "CURVE_STYLE_PARAMETERS_WITH_ENDS",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "CURVE_STYLE_PARAMETERS_WITH_ENDS", false));
    registry.put(
        "CONSTRUCTIVE_GEOMETRY_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "CONSTRUCTIVE_GEOMETRY_REPRESENTATION", false));
    registry.put(
        "AREA_DEPENDENT_ANNOTATION_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "AREA_DEPENDENT_ANNOTATION_REPRESENTATION", false));
    registry.put(
        "PRESENTATION_SIZE",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "PRESENTATION_SIZE", false));
    registry.put(
        "VARIATIONAL_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "VARIATIONAL_REPRESENTATION", false));
    registry.put(
        "RANGE_CHARACTERISTIC",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "RANGE_CHARACTERISTIC", false));
    registry.put(
        "PLY_ANGLE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "PLY_ANGLE_REPRESENTATION", false));
    registry.put(
        "MOMENTS_OF_INERTIA_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "MOMENTS_OF_INERTIA_REPRESENTATION", false));
    registry.put(
        "UNCERTAINTY_ASSIGNED_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "UNCERTAINTY_ASSIGNED_REPRESENTATION", false));
    registry.put(
        "INTERPOLATED_CONFIGURATION_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "INTERPOLATED_CONFIGURATION_REPRESENTATION", false));
    registry.put(
        "KINEMATIC_FRAME_BACKGROUND_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "KINEMATIC_FRAME_BACKGROUND_REPRESENTATION", false));
    registry.put(
        "KINEMATIC_GROUND_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "KINEMATIC_GROUND_REPRESENTATION", false));
    registry.put(
        "KINEMATIC_LINK_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "KINEMATIC_LINK_REPRESENTATION", false));
    registry.put(
        "KINEMATIC_TOPOLOGY_DIRECTED_STRUCTURE",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "KINEMATIC_TOPOLOGY_DIRECTED_STRUCTURE", false));
    registry.put(
        "KINEMATIC_TOPOLOGY_NETWORK_STRUCTURE",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "KINEMATIC_TOPOLOGY_NETWORK_STRUCTURE", false));
    registry.put(
        "KINEMATIC_TOPOLOGY_STRUCTURE",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "KINEMATIC_TOPOLOGY_STRUCTURE", false));
    registry.put(
        "KINEMATIC_TOPOLOGY_SUBSTRUCTURE",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "KINEMATIC_TOPOLOGY_SUBSTRUCTURE", false));
    registry.put(
        "KINEMATIC_TOPOLOGY_TREE_STRUCTURE",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "KINEMATIC_TOPOLOGY_TREE_STRUCTURE", false));
    registry.put(
        "LINEAR_FLEXIBLE_LINK_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "LINEAR_FLEXIBLE_LINK_REPRESENTATION", false));
    registry.put(
        "RIGID_LINK_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "RIGID_LINK_REPRESENTATION", false));
    registry.put(
        "MECHANISM_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "MECHANISM_REPRESENTATION", false));
    registry.put(
        "MECHANISM_STATE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "MECHANISM_STATE_REPRESENTATION", false));
    registry.put(
        "LINK_MOTION_REPRESENTATION_ALONG_PATH",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "LINK_MOTION_REPRESENTATION_ALONG_PATH", false));
    registry.put(
        "REINFORCEMENT_ORIENTATION_BASIS",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "REINFORCEMENT_ORIENTATION_BASIS", false));
    registry.put(
        "CONNECTED_EDGE_WITH_LENGTH_SET_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "CONNECTED_EDGE_WITH_LENGTH_SET_REPRESENTATION", false));
    registry.put(
        "EDGE_BASED_TOPOLOGICAL_REPRESENTATION_WITH_LENGTH_CONSTRAINT",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance,
                "EDGE_BASED_TOPOLOGICAL_REPRESENTATION_WITH_LENGTH_CONSTRAINT",
                false));
    registry.put(
        "DATA_EQUIVALENCE_CRITERIA_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "DATA_EQUIVALENCE_CRITERIA_REPRESENTATION", false));
    registry.put(
        "DATA_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "DATA_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION", false));
    registry.put(
        "DATA_QUALITY_CRITERIA_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "DATA_QUALITY_CRITERIA_REPRESENTATION", false));
    registry.put(
        "DATA_QUALITY_INSPECTION_RESULT_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "DATA_QUALITY_INSPECTION_RESULT_REPRESENTATION", false));
    registry.put(
        "EXTERNALLY_CONDITIONED_DATA_QUALITY_CRITERIA_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance,
                "EXTERNALLY_CONDITIONED_DATA_QUALITY_CRITERIA_REPRESENTATION",
                false));
    registry.put(
        "EXTERNALLY_CONDITIONED_DATA_QUALITY_INSPECTION_RESULT_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance,
                "EXTERNALLY_CONDITIONED_DATA_QUALITY_INSPECTION_RESULT_REPRESENTATION",
                false));
    registry.put(
        "A3M_EQUIVALENCE_CRITERIA_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "A3M_EQUIVALENCE_CRITERIA_REPRESENTATION", false));
    registry.put(
        "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION", false));
    registry.put(
        "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_ASSEMBLY",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_ASSEMBLY", false));
    registry.put(
        "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_SHAPE",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_SHAPE", false));
    registry.put(
        "SHAPE_DATA_QUALITY_CRITERIA_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "SHAPE_DATA_QUALITY_CRITERIA_REPRESENTATION", false));
    registry.put(
        "SHAPE_DATA_QUALITY_INSPECTION_RESULT_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "SHAPE_DATA_QUALITY_INSPECTION_RESULT_REPRESENTATION", false));
    registry.put(
        "EXTERNALLY_DEFINED_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "EXTERNALLY_DEFINED_REPRESENTATION", false));
    registry.put(
        "EXTERNALLY_DEFINED_REPRESENTATION_WITH_PARAMETERS",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "EXTERNALLY_DEFINED_REPRESENTATION_WITH_PARAMETERS", false));
    registry.put(
        "SHAPE_CRITERIA_REPRESENTATION_WITH_ACCURACY",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "SHAPE_CRITERIA_REPRESENTATION_WITH_ACCURACY", false));
    registry.put(
        "SHAPE_INSPECTION_RESULT_REPRESENTATION_WITH_ACCURACY",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "SHAPE_INSPECTION_RESULT_REPRESENTATION_WITH_ACCURACY", false));
    registry.put(
        "ANALYSIS_MODEL",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "ANALYSIS_MODEL", false));
    registry.put(
        "MESSAGE_CONTENTS_ASSIGNMENT",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "MESSAGE_CONTENTS_ASSIGNMENT", false));
    registry.put(
        "MACHINING_TOOL_DIRECTION_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "MACHINING_TOOL_DIRECTION_REPRESENTATION", false));
    registry.put(
        "FOUNDED_KINEMATIC_PATH",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "FOUNDED_KINEMATIC_PATH", false));
    registry.put(
        "SIMPLIFIED_COUNTERBORE_HOLE_DEFINITION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "SIMPLIFIED_COUNTERBORE_HOLE_DEFINITION", false));
    registry.put(
        "SIMPLIFIED_COUNTERDRILL_HOLE_DEFINITION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "SIMPLIFIED_COUNTERDRILL_HOLE_DEFINITION", false));
    registry.put(
        "SIMPLIFIED_COUNTERSINK_HOLE_DEFINITION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "SIMPLIFIED_COUNTERSINK_HOLE_DEFINITION", false));
    registry.put(
        "MACHINING_CUTTING_CORNER_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "MACHINING_CUTTING_CORNER_REPRESENTATION", false));
    registry.put(
        "MACHINING_DWELL_TIME_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "MACHINING_DWELL_TIME_REPRESENTATION", false));
    registry.put(
        "MACHINING_FEED_SPEED_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "MACHINING_FEED_SPEED_REPRESENTATION", false));
    registry.put(
        "MACHINING_OFFSET_VECTOR_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "MACHINING_OFFSET_VECTOR_REPRESENTATION", false));
    registry.put(
        "MACHINING_SPINDLE_SPEED_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "MACHINING_SPINDLE_SPEED_REPRESENTATION", false));
    registry.put(
        "MACHINING_TOOL_BODY_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "MACHINING_TOOL_BODY_REPRESENTATION", false));
    registry.put(
        "MACHINING_TOOL_DIMENSION_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "MACHINING_TOOL_DIMENSION_REPRESENTATION", false));
    registry.put(
        "MACHINING_TOOLPATH_SPEED_PROFILE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "MACHINING_TOOLPATH_SPEED_PROFILE_REPRESENTATION", false));
    registry.put(
        "FREEFORM_MILLING_TOLERANCE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "FREEFORM_MILLING_TOLERANCE_REPRESENTATION", false));
    registry.put(
        "HARDNESS_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "HARDNESS_REPRESENTATION", false));
    registry.put(
        "DEFAULT_TOLERANCE_TABLE",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "DEFAULT_TOLERANCE_TABLE", false));
    registry.put(
        "OTHER_LIST_TABLE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "OTHER_LIST_TABLE_REPRESENTATION", false));
    registry.put(
        "CHARACTERIZED_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "CHARACTERIZED_REPRESENTATION", false));
    registry.put(
        "CHARACTERIZED_ITEM_WITHIN_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "CHARACTERIZED_ITEM_WITHIN_REPRESENTATION", false));
    registry.put(
        "CHARACTERIZED_CHAIN_BASED_ITEM_WITHIN_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "CHARACTERIZED_CHAIN_BASED_ITEM_WITHIN_REPRESENTATION", false));
    registry.put(
        "EVALUATED_CHARACTERISTIC",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "EVALUATED_CHARACTERISTIC", false));
    registry.put(
        "EVALUATED_CHARACTERISTIC_OF_PRODUCT_AS_INDIVIDUAL_TEST_RESULT",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance,
                "EVALUATED_CHARACTERISTIC_OF_PRODUCT_AS_INDIVIDUAL_TEST_RESULT",
                false));
    registry.put(
        "DRAUGHTING_MODEL",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "DRAUGHTING_MODEL", false));
    registry.put(
        "DRAUGHTING_SUBFIGURE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "DRAUGHTING_SUBFIGURE_REPRESENTATION", false));
    registry.put(
        "DRAUGHTING_SYMBOL_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "DRAUGHTING_SYMBOL_REPRESENTATION", false));
    registry.put(
        "DRAWING_SHEET_LAYOUT",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "DRAWING_SHEET_LAYOUT", false));
    registry.put(
        "DRAWING_SHEET_REVISION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "DRAWING_SHEET_REVISION", false));
    registry.put(
        "REPRESENTATION", (resolver, instance) -> resolver.resolveRepresentation(instance, false));
    registry.put(
        "PATH_PARAMETER_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "PATH_PARAMETER_REPRESENTATION", false));
    registry.put(
        "PRESCRIBED_PATH",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "PRESCRIBED_PATH", false));
    registry.put(
        "RESULTING_PATH",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "RESULTING_PATH", false));
    registry.put(
        "CHARACTER_GLYPH_SYMBOL",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "CHARACTER_GLYPH_SYMBOL", false));
    registry.put(
        "GENERIC_CHARACTER_GLYPH_SYMBOL",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "GENERIC_CHARACTER_GLYPH_SYMBOL", false));
    registry.put(
        "CHARACTER_GLYPH_SYMBOL_OUTLINE",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "CHARACTER_GLYPH_SYMBOL_OUTLINE", false));
    registry.put(
        "CHARACTER_GLYPH_SYMBOL_STROKE",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "CHARACTER_GLYPH_SYMBOL_STROKE", false));
    registry.put(
        "SURFACE_TEXTURE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "SURFACE_TEXTURE_REPRESENTATION", false));
    registry.put(
        "TACTILE_APPEARANCE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "TACTILE_APPEARANCE_REPRESENTATION", false));
    registry.put("APPLICATION_CONTEXT", StepEntityResolver::resolveApplicationContext);
    registry.put(
        "APPLICATION_PROTOCOL_DEFINITION",
        StepEntityResolver::resolveApplicationProtocolDefinition);
    registry.put("PRODUCT_CONTEXT", StepEntityResolver::resolveProductContext);
    registry.put(
        "MECHANICAL_CONTEXT",
        (resolver, instance) -> resolver.resolveProductContext(instance, "MECHANICAL_CONTEXT"));
    registry.put("PRODUCT", StepEntityResolver::resolveProduct);
    registry.put("PRODUCT_CATEGORY", StepEntityResolver::resolveProductCategory);
    registry.put(
        "PRODUCT_CATEGORY_RELATIONSHIP",
        StepEntityResolver::resolveProductCategoryRelationship);
    registry.put(
        "PRODUCT_RELATED_PRODUCT_CATEGORY",
        StepEntityResolver::resolveProductRelatedProductCategory);
    registry.put("PRODUCT_RELATIONSHIP", StepEntityResolver::resolveProductRelationship);
    registry.put(
        "PRODUCT_DEFINITION_FORMATION", StepEntityResolver::resolveProductDefinitionFormation);
    registry.put(
        "PRODUCT_DEFINITION_FORMATION_WITH_SPECIFIED_SOURCE",
        StepEntityResolver::resolveProductDefinitionFormation);
    registry.put(
        "PRODUCT_DEFINITION_FORMATION_RELATIONSHIP",
        StepEntityResolver::resolveProductDefinitionFormationRelationship);
    registry.put("PRODUCT_DEFINITION_CONTEXT", StepEntityResolver::resolveProductDefinitionContext);
    registry.put(
        "DESIGN_CONTEXT",
        (resolver, instance) ->
            resolver.resolveProductDefinitionContext(instance, "DESIGN_CONTEXT"));
    registry.put("PRODUCT_DEFINITION", StepEntityResolver::resolveProductDefinition);
    registry.put(
        "PRODUCT_DEFINITION_RELATIONSHIP",
        StepEntityResolver::resolveProductDefinitionRelationship);
    registerProductDefinitionRelationshipAliases(
        registry,
        "ASSEMBLY_COMPONENT_USAGE",
        "BREAKDOWN_CONTEXT",
        "BREAKDOWN_ELEMENT_USAGE",
        "BREAKDOWN_OF",
        "PRODUCT_DEFINITION_USAGE",
        "PROMISSORY_USAGE_OCCURRENCE",
        "SUPPLIED_PART_RELATIONSHIP");
    registry.put(
        "PRODUCT_DEFINITION_RELATIONSHIP_RELATIONSHIP",
        StepEntityResolver::resolveProductDefinitionRelationshipRelationship);
    registry.put(
        "PRODUCT_DEFINITION_USAGE_RELATIONSHIP",
        (resolver, instance) ->
            resolver.resolveProductDefinitionRelationshipRelationship(
                instance, "PRODUCT_DEFINITION_USAGE_RELATIONSHIP"));
    registerProductDefinitionRelationshipRelationshipAliases(
        registry,
        "ASSEMBLY_COMPONENT_USAGE_SUBSTITUTE",
        "PRODUCT_DEFINITION_SUBSTITUTE");
    registry.put("PRODUCT_DEFINITION_SHAPE", StepEntityResolver::resolveProductDefinitionShape);
    registry.put("PROPERTY_DEFINITION", StepEntityResolver::resolvePropertyDefinition);
    registry.put(
        "PROPERTY_DEFINITION_RELATIONSHIP",
        StepEntityResolver::resolvePropertyDefinitionRelationship);
    registry.put("GENERAL_PROPERTY", StepEntityResolver::resolveGeneralProperty);
    registry.put(
        "GENERAL_PROPERTY_RELATIONSHIP",
        StepEntityResolver::resolveGeneralPropertyRelationship);
    registry.put("GROUP", StepEntityResolver::resolveGroup);
    registry.put("CLASS", (resolver, instance) -> resolver.resolveGroup(instance, "CLASS"));
    registry.put(
        "CLASS_SYSTEM", (resolver, instance) -> resolver.resolveGroup(instance, "CLASS_SYSTEM"));
    registry.put("GROUP_RELATIONSHIP", StepEntityResolver::resolveGroupRelationship);
    registry.put("GROUP_ASSIGNMENT", StepEntityResolver::resolveGroupAssignment);
    registry.put("APPLIED_GROUP_ASSIGNMENT", StepEntityResolver::resolveAppliedGroupAssignment);
    registry.put("ADDRESS", StepEntityResolver::resolveAddress);
    registry.put("DOCUMENT_TYPE", StepEntityResolver::resolveDocumentType);
    registry.put("DOCUMENT", StepEntityResolver::resolveDocument);
    registry.put("DOCUMENT_RELATIONSHIP", StepEntityResolver::resolveDocumentRelationship);
    registry.put(
        "DOCUMENT_USAGE_CONSTRAINT", StepEntityResolver::resolveDocumentUsageConstraint);
    registry.put("DOCUMENT_REFERENCE", StepEntityResolver::resolveDocumentReference);
    registry.put(
        "APPLIED_DOCUMENT_REFERENCE", StepEntityResolver::resolveAppliedDocumentReference);
    registry.put(
        "CC_DESIGN_SPECIFICATION_REFERENCE",
        (resolver, instance) ->
            resolver.resolveAppliedDocumentReference(instance, "CC_DESIGN_SPECIFICATION_REFERENCE"));
    registry.put("PERSON", StepEntityResolver::resolvePerson);
    registry.put("ORGANIZATION", StepEntityResolver::resolveOrganization);
    registry.put(
        "PERSON_AND_ORGANIZATION", StepEntityResolver::resolvePersonAndOrganization);
    registry.put(
        "ORGANIZATION_RELATIONSHIP", StepEntityResolver::resolveOrganizationRelationship);
    registry.put("ORGANIZATION_ROLE", StepEntityResolver::resolveOrganizationRole);
    registry.put("ORGANIZATION_ASSIGNMENT", StepEntityResolver::resolveOrganizationAssignment);
    registry.put(
        "APPLIED_ORGANIZATION_ASSIGNMENT",
        StepEntityResolver::resolveAppliedOrganizationAssignment);
    registry.put(
        "CC_DESIGN_ORGANIZATION_ASSIGNMENT",
        (resolver, instance) ->
            resolver.resolveAppliedOrganizationAssignment(
                instance, "CC_DESIGN_ORGANIZATION_ASSIGNMENT"));
    registry.put("LANGUAGE", StepEntityResolver::resolveLanguage);
    registry.put("LANGUAGE_ASSIGNMENT", StepEntityResolver::resolveLanguageAssignment);
    registry.put(
        "APPLIED_LANGUAGE_ASSIGNMENT", StepEntityResolver::resolveAppliedLanguageAssignment);
    registry.put(
        "PERSON_AND_ORGANIZATION_ROLE",
        StepEntityResolver::resolvePersonAndOrganizationRole);
    registry.put(
        "PERSON_AND_ORGANIZATION_ASSIGNMENT",
        StepEntityResolver::resolvePersonAndOrganizationAssignment);
    registry.put(
        "APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT",
        StepEntityResolver::resolveAppliedPersonAndOrganizationAssignment);
    registry.put(
        "CC_DESIGN_PERSON_AND_ORGANIZATION_ASSIGNMENT",
        (resolver, instance) ->
            resolver.resolveAppliedPersonAndOrganizationAssignment(
                instance, "CC_DESIGN_PERSON_AND_ORGANIZATION_ASSIGNMENT"));
    registry.put("CALENDAR_DATE", StepEntityResolver::resolveCalendarDate);
    registry.put(
        "COORDINATED_UNIVERSAL_TIME_OFFSET",
        StepEntityResolver::resolveCoordinatedUniversalTimeOffset);
    registry.put("LOCAL_TIME", StepEntityResolver::resolveLocalTime);
    registry.put("DATE_AND_TIME", StepEntityResolver::resolveDateAndTime);
    registry.put("DATE_ROLE", StepEntityResolver::resolveDateRole);
    registry.put("DATE_ASSIGNMENT", StepEntityResolver::resolveDateAssignment);
    registry.put("APPLIED_DATE_ASSIGNMENT", StepEntityResolver::resolveAppliedDateAssignment);
    registry.put(
        "CC_DESIGN_DATE_ASSIGNMENT",
        (resolver, instance) ->
            resolver.resolveAppliedDateAssignment(instance, "CC_DESIGN_DATE_ASSIGNMENT"));
    registry.put("DATE_TIME_ROLE", StepEntityResolver::resolveDateTimeRole);
    registry.put("DATE_TIME_ASSIGNMENT", StepEntityResolver::resolveDateTimeAssignment);
    registry.put(
        "APPLIED_DATE_AND_TIME_ASSIGNMENT",
        StepEntityResolver::resolveAppliedDateTimeAssignment);
    registry.put(
        "APPLIED_DATE_TIME_ASSIGNMENT",
        (resolver, instance) ->
            resolver.resolveAppliedDateTimeAssignment(instance, "APPLIED_DATE_TIME_ASSIGNMENT"));
    registry.put(
        "CC_DESIGN_DATE_AND_TIME_ASSIGNMENT",
        (resolver, instance) ->
            resolver.resolveAppliedDateTimeAssignment(
                instance, "CC_DESIGN_DATE_AND_TIME_ASSIGNMENT"));
    registry.put("APPROVAL_STATUS", StepEntityResolver::resolveApprovalStatus);
    registry.put("APPROVAL", StepEntityResolver::resolveApproval);
    registry.put("APPROVAL_ROLE", StepEntityResolver::resolveApprovalRole);
    registry.put("APPROVAL_ASSIGNMENT", StepEntityResolver::resolveApprovalAssignment);
    registry.put(
        "APPLIED_APPROVAL_ASSIGNMENT", StepEntityResolver::resolveAppliedApprovalAssignment);
    registry.put(
        "CC_DESIGN_APPROVAL",
        (resolver, instance) ->
            resolver.resolveAppliedApprovalAssignment(instance, "CC_DESIGN_APPROVAL"));
    registry.put(
        "APPROVAL_PERSON_ORGANIZATION",
        StepEntityResolver::resolveApprovalPersonOrganization);
    registry.put("APPROVAL_DATE_TIME", StepEntityResolver::resolveApprovalDateTime);
    registry.put(
        "SECURITY_CLASSIFICATION_LEVEL",
        StepEntityResolver::resolveSecurityClassificationLevel);
    registry.put(
        "SECURITY_CLASSIFICATION", StepEntityResolver::resolveSecurityClassification);
    registry.put(
        "SECURITY_CLASSIFICATION_ASSIGNMENT",
        StepEntityResolver::resolveSecurityClassificationAssignment);
    registry.put(
        "APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT",
        StepEntityResolver::resolveAppliedSecurityClassificationAssignment);
    registry.put(
        "CC_DESIGN_SECURITY_CLASSIFICATION",
        (resolver, instance) ->
            resolver.resolveAppliedSecurityClassificationAssignment(
                instance, "CC_DESIGN_SECURITY_CLASSIFICATION"));
    registry.put("CONTRACT_TYPE", StepEntityResolver::resolveContractType);
    registry.put("CONTRACT", StepEntityResolver::resolveContract);
    registry.put("CONTRACT_ASSIGNMENT", StepEntityResolver::resolveContractAssignment);
    registry.put(
        "APPLIED_CONTRACT_ASSIGNMENT", StepEntityResolver::resolveAppliedContractAssignment);
    registry.put(
        "CC_DESIGN_CONTRACT",
        (resolver, instance) ->
            resolver.resolveAppliedContractAssignment(instance, "CC_DESIGN_CONTRACT"));
    registry.put("CERTIFICATION_TYPE", StepEntityResolver::resolveCertificationType);
    registry.put("CERTIFICATION", StepEntityResolver::resolveCertification);
    registry.put(
        "CERTIFICATION_ASSIGNMENT", StepEntityResolver::resolveCertificationAssignment);
    registry.put(
        "APPLIED_CERTIFICATION_ASSIGNMENT",
        StepEntityResolver::resolveAppliedCertificationAssignment);
    registry.put(
        "CC_DESIGN_CERTIFICATION",
        (resolver, instance) ->
            resolver.resolveAppliedCertificationAssignment(instance, "CC_DESIGN_CERTIFICATION"));
    registry.put("EFFECTIVITY", StepEntityResolver::resolveEffectivity);
    registry.put(
        "PRODUCT_DEFINITION_EFFECTIVITY",
        StepEntityResolver::resolveProductDefinitionEffectivity);
    registry.put("EFFECTIVITY_RELATIONSHIP", StepEntityResolver::resolveEffectivityRelationship);
    registry.put("CLASSIFICATION_ROLE", StepEntityResolver::resolveClassificationRole);
    registry.put(
        "CLASSIFICATION_ASSIGNMENT", StepEntityResolver::resolveClassificationAssignment);
    registry.put(
        "APPLIED_CLASSIFICATION_ASSIGNMENT",
        StepEntityResolver::resolveAppliedClassificationAssignment);
    registry.put("IDENTIFICATION_ROLE", StepEntityResolver::resolveIdentificationRole);
    registry.put(
        "IDENTIFICATION_ASSIGNMENT", StepEntityResolver::resolveIdentificationAssignment);
    registry.put(
        "APPLIED_IDENTIFICATION_ASSIGNMENT",
        StepEntityResolver::resolveAppliedIdentificationAssignment);
    registry.put(
        "EXTERNAL_IDENTIFICATION_ASSIGNMENT",
        StepEntityResolver::resolveExternalIdentificationAssignment);
    registry.put(
        "APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT",
        StepEntityResolver::resolveAppliedExternalIdentificationAssignment);
    registry.put("NAME_ASSIGNMENT", StepEntityResolver::resolveNameAssignment);
    registry.put("APPLIED_NAME_ASSIGNMENT", StepEntityResolver::resolveAppliedNameAssignment);
    registry.put("DESCRIPTION_ATTRIBUTE", StepEntityResolver::resolveDescriptionAttribute);
    registry.put("NAME_ATTRIBUTE", StepEntityResolver::resolveNameAttribute);
    registry.put("ID_ATTRIBUTE", StepEntityResolver::resolveIdAttribute);
    registry.put("EXTERNAL_SOURCE", StepEntityResolver::resolveExternalSource);
    registry.put("EXTERNALLY_DEFINED_ITEM",
        (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_ITEM"));
    registry.put(
        "EXTERNAL_SOURCE_RELATIONSHIP",
        StepEntityResolver::resolveExternalSourceRelationship);
    registry.put(
        "EXTERNALLY_DEFINED_ITEM",
        (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_ITEM"));
    registry.put(
        "EXTERNALLY_DEFINED_CLASS",
        (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_CLASS"));
    registry.put(
        "EXTERNALLY_DEFINED_GENERAL_PROPERTY",
        (resolver, instance) ->
            resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_GENERAL_PROPERTY"));
    registerExternallyDefinedItemAliases(
        registry,
        "EXTERNALLY_DEFINED_CHARACTER_GLYPH",
        "EXTERNALLY_DEFINED_CURVE_FONT",
        "EXTERNALLY_DEFINED_DIMENSION_DEFINITION",
        "EXTERNALLY_DEFINED_HATCH_STYLE",
        "EXTERNALLY_DEFINED_MARKER",
        "EXTERNALLY_DEFINED_PICTURE_REPRESENTATION_ITEM",
        "EXTERNALLY_DEFINED_STYLE",
        "EXTERNALLY_DEFINED_SYMBOL",
        "EXTERNALLY_DEFINED_TERMINATOR_SYMBOL",
        "EXTERNALLY_DEFINED_TEXT_FONT",
        "EXTERNALLY_DEFINED_TEXT_STYLE",
        "EXTERNALLY_DEFINED_TILE");
    registry.put("CHARACTERIZED_OBJECT", StepEntityResolver::resolveCharacterizedObject);
    // Phase 2A: Additional manufacturing features
    registerCharacterizedObjectAliases(
        registry,
        "MACHINING_OPERATION",
        "MACHINED_SURFACE",
        "TWO_5D_MANUFACTURING_FEATURE",
        "MANUFACTURING_FEATURE_REPRESENTATION",
        "DEPRESSION",
        "EDGE_ROUND");
    registerCharacterizedObjectAliases(
        registry,
        "ADDITIVE_MANUFACTURING_FEATURE",
        "BARRING_HOLE",
        "BASIC_ROUND_HOLE",
        "BEAD",
        "BOSS",
        "CIRCULAR_PATTERN",
        "COMPOUND_FEATURE",
        "COMPOSITE_HOLE",
        "CONTACT_FEATURE_DEFINITION",
        "COUNTERBORE_HOLE_DEFINITION",
        "COUNTERDRILL_HOLE_DEFINITION",
        "COUNTERSINK_HOLE_DEFINITION",
        "EXPLICIT_COMPOSITE_HOLE",
        "EXPLICIT_ROUND_HOLE",
        "EXTERNALLY_DEFINED_FEATURE_DEFINITION",
        "FEATURE_DEFINITION",
        "FEATURE_DEFINITION_WITH_CONNECTION_AREA",
        "FEATURE_IN_PANEL",
        "FEATURE_PATTERN",
        "FLAT_FACE",
        "GEAR",
        "GENERAL_FEATURE",
        "HOLE_IN_PANEL",
        "JOGGLE",
        "LOCATOR",
        "MARKING",
        "OUTER_ROUND",
        "OUTSIDE_PROFILE",
        "POCKET",
        "PROTRUSION",
        "RECTANGULAR_PATTERN",
        "REMOVAL_VOLUME",
        "REPLICATE_FEATURE",
        "REVOLVED_PROFILE",
        "RIB",
        "RIB_TOP",
        "ROUND_HOLE",
        "ROUNDED_END",
        "SHAPE_FEATURE_DEFINITION",
        "SLOT",
        "SPHERICAL_CAP",
        "SPOTFACE_DEFINITION",
        "SPOTFACE_HOLE_DEFINITION",
        "THREAD",
        "TURNED_KNURL");
    registry.put(
        "APEX",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "APEX"));
    registry.put(
        "ALL_AROUND_SHAPE_ASPECT",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "ALL_AROUND_SHAPE_ASPECT"));
    registry.put(
        "BETWEEN_SHAPE_ASPECT",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "BETWEEN_SHAPE_ASPECT"));
    registry.put(
        "CENTRE_OF_SYMMETRY",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "CENTRE_OF_SYMMETRY"));
    registry.put(
        "CHAMFER",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "CHAMFER"));
    registry.put(
        "CHAMFER_OFFSET",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "CHAMFER_OFFSET"));
    registry.put(
        "COMPONENT_FEATURE",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "COMPONENT_FEATURE"));
    registry.put(
        "COMPOSITE_GROUP_SHAPE_ASPECT",
        (resolver, instance) ->
            resolver.resolveShapeAspect(instance, "COMPOSITE_GROUP_SHAPE_ASPECT"));
    registry.put(
        "COMPOSITE_SHAPE_ASPECT",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "COMPOSITE_SHAPE_ASPECT"));
    registry.put(
        "COMPOSITE_UNIT_SHAPE_ASPECT",
        (resolver, instance) ->
            resolver.resolveShapeAspect(instance, "COMPOSITE_UNIT_SHAPE_ASPECT"));
    registry.put(
        "CONTINUOUS_SHAPE_ASPECT",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "CONTINUOUS_SHAPE_ASPECT"));
    registry.put(
        "DATUM",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "DATUM"));
    registry.put(
        "DATUM_FEATURE",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "DATUM_FEATURE"));
    registry.put(
        "DATUM_TARGET",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "DATUM_TARGET"));
    registry.put(
        "GEOMETRIC_ALIGNMENT",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "GEOMETRIC_ALIGNMENT"));
    registry.put(
        "GEOMETRIC_CONTACT",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "GEOMETRIC_CONTACT"));
    registry.put(
        "GEOMETRIC_INTERSECTION",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "GEOMETRIC_INTERSECTION"));
    registry.put(
        "GROUP_SHAPE_ASPECT",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "GROUP_SHAPE_ASPECT"));
    registry.put(
        "EDGE_ROUND",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "EDGE_ROUND"));
    registry.put(
        "EXTENSION",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "EXTENSION"));
    registry.put(
        "FILLET",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "FILLET"));
    registry.put(
        "PARALLEL_OFFSET",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "PARALLEL_OFFSET"));
    registry.put(
        "PERPENDICULAR_TO",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "PERPENDICULAR_TO"));
    registry.put(
        "INSTANCED_FEATURE",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "INSTANCED_FEATURE"));
    registry.put(
        "INSTANCED_SHAPE_ASPECT",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "INSTANCED_SHAPE_ASPECT"));
    registry.put(
        "SINGULAR_SHAPE_ASPECT",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "SINGULAR_SHAPE_ASPECT"));
    registry.put(
        "SYMMETRIC_SHAPE_ASPECT",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "SYMMETRIC_SHAPE_ASPECT"));
    registry.put(
        "TANGENT",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "TANGENT"));
    registry.put("SHAPE_ASPECT", StepEntityResolver::resolveShapeAspect);
    registry.put("SHAPE_ASPECT_OCCURRENCE",
        (resolver, instance) -> resolver.resolveShapeAspectOccurrence(instance, "SHAPE_ASPECT_OCCURRENCE"));
    registerShapeAspectAliases(
        registry,
        "APPLIED_AREA",
        "BEAD_END",
        "BOSS_TOP",
        "CIRCULAR_CLOSED_PROFILE",
        "COMPONENT_TERMINAL",
        "CONSTITUENT_SHAPE_ASPECT",
        "CONTACTING_FEATURE",
        "DATUM_REFERENCE_ELEMENT",
        "DATUM_SYSTEM",
        "DEFAULT_MODEL_GEOMETRIC_VIEW",
        "GENERAL_DATUM_REFERENCE",
        "HOLE_BOTTOM",
        "GEOMETRIC_TOLERANCE_WITH_MODIFIERS",
        "LAYOUT_SPACING_CONTEXTUAL_AREA",
        "MATED_PART_RELATIONSHIP",
        "MOUNTING_RESTRICTION_AREA",
        "MOUNTING_RESTRICTION_VOLUME",
        "PATH_FEATURE_COMPONENT",
        "PHYSICAL_COMPONENT_FEATURE",
        "PHYSICAL_COMPONENT_TERMINAL",
        "PROJECTED_ZONE_DEFINITION",
        "REFERENCE_GRAPHIC_REGISTRATION_MARK",
        "SEATING_PLANE",
        "TERMINAL_FEATURE",
        "TERMINAL_LOCATION_GROUP",
        "TOLERANCE_ZONE_DEFINITION");
    registry.put(
        "SHAPE_ASPECT_OCCURRENCE",
        (resolver, instance) -> resolver.resolveShapeAspectOccurrence(instance, "SHAPE_ASPECT_OCCURRENCE"));
    registerShapeAspectOccurrenceAliases(
        registry,
        "BASIC_ROUND_HOLE_OCCURRENCE",
        "COUNTERBORE_HOLE_OCCURRENCE",
        "COUNTERDRILL_HOLE_OCCURRENCE",
        "COUNTERSINK_HOLE_OCCURRENCE",
        "SIMPLIFIED_COUNTERBORE_HOLE_OCCURRENCE",
        "SIMPLIFIED_COUNTERDRILL_HOLE_OCCURRENCE",
        "SIMPLIFIED_COUNTERSINK_HOLE_OCCURRENCE",
        "SIMPLIFIED_SPOTFACE_HOLE_OCCURRENCE",
        "SPOTFACE_HOLE_OCCURRENCE");
    registry.put(
        "ANGULAR_LOCATION",
        (resolver, instance) ->
            resolver.resolveShapeAspectRelationship(instance, "ANGULAR_LOCATION"));
    registry.put(
        "COMPOSITE_SHAPE_ASPECT_RELATIONSHIP",
        (resolver, instance) ->
            resolver.resolveShapeAspectRelationship(
                instance, "COMPOSITE_SHAPE_ASPECT_RELATIONSHIP"));
    registry.put(
        "DIMENSIONAL_LOCATION",
        (resolver, instance) ->
            resolver.resolveShapeAspectRelationship(instance, "DIMENSIONAL_LOCATION"));
    registry.put(
        "DIMENSIONAL_SIZE",
        (resolver, instance) ->
            resolver.resolveShapeAspectRelationship(instance, "DIMENSIONAL_SIZE"));
    registry.put(
        "DIRECTED_DIMENSIONAL_LOCATION",
        (resolver, instance) ->
            resolver.resolveShapeAspectRelationship(instance, "DIRECTED_DIMENSIONAL_LOCATION"));
    registry.put(
        "FEATURE_COMPONENT_RELATIONSHIP",
        (resolver, instance) ->
            resolver.resolveShapeAspectRelationship(instance, "FEATURE_COMPONENT_RELATIONSHIP"));
    registry.put(
        "FEATURE_FOR_DATUM_TARGET_RELATIONSHIP",
        (resolver, instance) ->
            resolver.resolveShapeAspectRelationship(instance, "FEATURE_FOR_DATUM_TARGET_RELATIONSHIP"));
    registry.put(
        "GEOMETRIC_ALIGNMENT_RELATIONSHIP",
        (resolver, instance) ->
            resolver.resolveShapeAspectRelationship(instance, "GEOMETRIC_ALIGNMENT_RELATIONSHIP"));
    registry.put(
        "GEOMETRIC_CONTACT_RELATIONSHIP",
        (resolver, instance) ->
            resolver.resolveShapeAspectRelationship(instance, "GEOMETRIC_CONTACT_RELATIONSHIP"));
    registry.put(
        "MAKE_FROM_FEATURE_RELATIONSHIP",
        (resolver, instance) ->
            resolver.resolveShapeAspectRelationship(instance, "MAKE_FROM_FEATURE_RELATIONSHIP"));
    registry.put(
        "PATTERN_OFFSET_MEMBERSHIP",
        (resolver, instance) ->
            resolver.resolveShapeAspectRelationship(instance, "PATTERN_OFFSET_MEMBERSHIP"));
    registry.put(
        "PATTERN_OMIT_MEMBERSHIP",
        (resolver, instance) ->
            resolver.resolveShapeAspectRelationship(instance, "PATTERN_OMIT_MEMBERSHIP"));
    registry.put(
        "SHAPE_ASPECT_ASSOCIATIVITY",
        (resolver, instance) ->
            resolver.resolveShapeAspectRelationship(instance, "SHAPE_ASPECT_ASSOCIATIVITY"));
    registry.put(
        "SHAPE_ASPECT_DERIVING_RELATIONSHIP",
        (resolver, instance) ->
            resolver.resolveShapeAspectRelationship(instance, "SHAPE_ASPECT_DERIVING_RELATIONSHIP"));
    registry.put(
        "SHAPE_ASPECT_TRANSITION",
        (resolver, instance) ->
            resolver.resolveShapeAspectRelationship(instance, "SHAPE_ASPECT_TRANSITION"));
    registry.put(
        "SHAPE_DEFINING_RELATIONSHIP",
        (resolver, instance) ->
            resolver.resolveShapeAspectRelationship(instance, "SHAPE_DEFINING_RELATIONSHIP"));
    registry.put(
        "SHAPE_FEATURE_FIT_RELATIONSHIP",
        (resolver, instance) ->
            resolver.resolveShapeAspectRelationship(instance, "SHAPE_FEATURE_FIT_RELATIONSHIP"));
    registry.put(
        "SHAPE_ASPECT_RELATIONSHIP",
        StepEntityResolver::resolveShapeAspectRelationship);
    registerShapeAspectRelationshipAliases(
        registry,
        "ASSEMBLY_SHAPE_CONSTRAINT_ITEM_RELATIONSHIP",
        "ASSEMBLY_SHAPE_JOINT_ITEM_RELATIONSHIP",
        "COMPONENT_FEATURE_JOINT",
        "COMPONENT_FEATURE_RELATIONSHIP_WITH_TRANSFORMATION",
        "COMPONENT_MATING_CONSTRAINT_CONDITION",
        "COMPONENT_PATH_SHAPE_ASPECT_RELATIONSHIP",
        "CONNECTION_ZONE_INTERFACE_PLANE_RELATIONSHIP",
        "CONNECTIVITY_DEFINITION_ITEM_RELATIONSHIP",
        "CONTACT_FEATURE_FIT_RELATIONSHIP",
        "DIMENSIONAL_LOCATION_WITH_DATUM_FEATURE",
        "DIMENSIONAL_LOCATION_WITH_PATH",
        "POSITIONED_SKETCH_TO_PART_ASSOCIATION",
        "SHAPE_FEATURE_DEFINITION_ELEMENT_RELATIONSHIP");
    registry.put(
        "SHAPE_DEFINITION_REPRESENTATION",
        StepEntityResolver::resolveShapeDefinitionRepresentation);
    registry.put("ROW_VARIABLE", StepEntityResolver::resolveRowVariable);
    registry.put("SCALAR_VARIABLE", StepEntityResolver::resolveScalarVariable);
    registry.put("ABSTRACT_VARIABLE", StepEntityResolver::resolveAbstractVariable);
    registry.put("ATTRIBUTE_ASSERTION", StepEntityResolver::resolveAttributeAssertion);
    registry.put("BACK_CHAINING_RULE_BODY", StepEntityResolver::resolveBackChainingRuleBody);
    registry.put(
        "FORWARD_CHAINING_RULE_PREMISE",
        StepEntityResolver::resolveForwardChainingRulePremise);
    registry.put(
        "ACTION_PROPERTY_REPRESENTATION",
        StepEntityResolver::resolveActionPropertyRepresentation);
    registry.put(
        "CONTACT_RATIO_REPRESENTATION",
        StepEntityResolver::resolveContactRatioRepresentation);
    registry.put(
        "KINEMATIC_PROPERTY_DEFINITION_REPRESENTATION",
        StepEntityResolver::resolveKinematicPropertyDefinitionRepresentation);
    registry.put(
        "KINEMATIC_PROPERTY_MECHANISM_REPRESENTATION",
        StepEntityResolver::resolveKinematicPropertyMechanismRepresentation);
    registry.put(
        "KINEMATIC_PROPERTY_REPRESENTATION_RELATION",
        StepEntityResolver::resolveKinematicPropertyRepresentationRelation);
    registry.put(
        "KINEMATIC_PROPERTY_TOPOLOGY_REPRESENTATION",
        StepEntityResolver::resolveKinematicPropertyTopologyRepresentation);
    registry.put(
        "PLACED_DATUM_TARGET_FEATURE",
        StepEntityResolver::resolvePlacedDatumTargetFeature);
    registry.put(
        "RESOURCE_PROPERTY_REPRESENTATION",
        StepEntityResolver::resolveResourcePropertyRepresentation);
    registry.put(
        "PROPERTY_DEFINITION_REPRESENTATION",
        StepEntityResolver::resolvePropertyDefinitionRepresentation);
    registry.put("REPRESENTATION_MAP", StepEntityResolver::resolveRepresentationMap);
    registry.put("SYMBOL_REPRESENTATION_MAP", StepEntityResolver::resolveSymbolRepresentationMap);
    registry.put("MAPPED_ITEM", StepEntityResolver::resolveMappedItem);
    registry.put(
        "CARTESIAN_TRANSFORMATION_OPERATOR_2D",
        StepEntityResolver::resolveCartesianTransformationOperator2D);
    registry.put(
        "CARTESIAN_TRANSFORMATION_OPERATOR_3D",
        StepEntityResolver::resolveCartesianTransformationOperator3D);
    registry.put("USER_DEFINED_CURVE_FONT", StepEntityResolver::resolveUserDefinedCurveFont);
    registry.put("USER_DEFINED_MARKER", StepEntityResolver::resolveUserDefinedMarker);
    registry.put(
        "USER_DEFINED_TERMINATOR_SYMBOL",
        StepEntityResolver::resolveUserDefinedTerminatorSymbol);
    registry.put(
        "ITEM_DEFINED_TRANSFORMATION", StepEntityResolver::resolveItemDefinedTransformation);
    registry.put(
        "REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION",
        StepEntityResolver::resolveRepresentationRelationshipWithTransformation);
    registry.put(
        "REPRESENTATION_RELATIONSHIP", StepEntityResolver::resolveRepresentationRelationship);
    registry.put(
        "CONSTRUCTIVE_GEOMETRY_REPRESENTATION_RELATIONSHIP",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "CONSTRUCTIVE_GEOMETRY_REPRESENTATION_RELATIONSHIP"));
    registry.put(
        "DATA_EQUIVALENCE_DEFINITION_REPRESENTATION_RELATIONSHIP",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "DATA_EQUIVALENCE_DEFINITION_REPRESENTATION_RELATIONSHIP"));
    registry.put(
        "DATA_QUALITY_DEFINITION_REPRESENTATION_RELATIONSHIP",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "DATA_QUALITY_DEFINITION_REPRESENTATION_RELATIONSHIP"));
    registry.put(
        "DEFINITIONAL_REPRESENTATION_RELATIONSHIP",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "DEFINITIONAL_REPRESENTATION_RELATIONSHIP"));
    registry.put(
        "DEFINITIONAL_REPRESENTATION_RELATIONSHIP_WITH_SAME_CONTEXT",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "DEFINITIONAL_REPRESENTATION_RELATIONSHIP_WITH_SAME_CONTEXT"));
    registry.put(
        "DRAWING_SHEET_REVISION_SEQUENCE",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "DRAWING_SHEET_REVISION_SEQUENCE"));
    registry.put(
        "EXPLICIT_PROCEDURAL_REPRESENTATION_RELATIONSHIP",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "EXPLICIT_PROCEDURAL_REPRESENTATION_RELATIONSHIP"));
    registry.put(
        "EXPLICIT_PROCEDURAL_SHAPE_REPRESENTATION_RELATIONSHIP",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "EXPLICIT_PROCEDURAL_SHAPE_REPRESENTATION_RELATIONSHIP"));
    registry.put(
        "FACE_SHAPE_REPRESENTATION_RELATIONSHIP",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "FACE_SHAPE_REPRESENTATION_RELATIONSHIP"));
    registry.put(
        "FLAT_PATTERN_PLY_REPRESENTATION_RELATIONSHIP",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "FLAT_PATTERN_PLY_REPRESENTATION_RELATIONSHIP"));
    registry.put(
        "MECHANICAL_DESIGN_AND_DRAUGHTING_RELATIONSHIP",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "MECHANICAL_DESIGN_AND_DRAUGHTING_RELATIONSHIP"));
    registry.put(
        "PAIR_REPRESENTATION_RELATIONSHIP",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "PAIR_REPRESENTATION_RELATIONSHIP"));
    registry.put(
        "REPRESENTATION_RELATIONSHIP_WITH_CLASS",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "REPRESENTATION_RELATIONSHIP_WITH_CLASS"));
    registry.put(
        "SHAPE_DATA_QUALITY_INSPECTED_SHAPE_AND_RESULT_RELATIONSHIP",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "SHAPE_DATA_QUALITY_INSPECTED_SHAPE_AND_RESULT_RELATIONSHIP"));
    registry.put(
        "SHAPE_REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "SHAPE_REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION"));
    registry.put(
        "TOPOLOGY_TO_GEOMETRY_MODEL_ASSOCIATION",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "TOPOLOGY_TO_GEOMETRY_MODEL_ASSOCIATION"));
    registry.put(
        "GEOMETRY_TO_TOPOLOGY_MODEL_ASSOCIATION",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "GEOMETRY_TO_TOPOLOGY_MODEL_ASSOCIATION"));
    registry.put(
        "VARIATIONAL_CURRENT_REPRESENTATION_RELATIONSHIP",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "VARIATIONAL_CURRENT_REPRESENTATION_RELATIONSHIP"));
    registry.put(
        "COAXIAL_ASSEMBLY_CONSTRAINT",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "COAXIAL_ASSEMBLY_CONSTRAINT"));
    registry.put(
        "PARALLEL_ASSEMBLY_CONSTRAINT",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "PARALLEL_ASSEMBLY_CONSTRAINT"));
    registry.put(
        "PERPENDICULAR_ASSEMBLY_CONSTRAINT",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "PERPENDICULAR_ASSEMBLY_CONSTRAINT"));
    registry.put(
        "INCIDENCE_ASSEMBLY_CONSTRAINT",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "INCIDENCE_ASSEMBLY_CONSTRAINT"));
    registry.put(
        "TANGENT_ASSEMBLY_CONSTRAINT",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "TANGENT_ASSEMBLY_CONSTRAINT"));
    registry.put(
        "COAXIAL_ASSEMBLY_CONSTRAINT_WITH_DIMENSION",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "COAXIAL_ASSEMBLY_CONSTRAINT_WITH_DIMENSION"));
    registry.put(
        "PARALLEL_ASSEMBLY_CONSTRAINT_WITH_DIMENSION",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "PARALLEL_ASSEMBLY_CONSTRAINT_WITH_DIMENSION"));
    registry.put(
        "PERPENDICULAR_ASSEMBLY_CONSTRAINT_WITH_DIMENSION",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "PERPENDICULAR_ASSEMBLY_CONSTRAINT_WITH_DIMENSION"));
    registry.put(
        "INCIDENCE_ASSEMBLY_CONSTRAINT_WITH_DIMENSION",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "INCIDENCE_ASSEMBLY_CONSTRAINT_WITH_DIMENSION"));
    registry.put(
        "SURFACE_DISTANCE_ASSEMBLY_CONSTRAINT_WITH_DIMENSION",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "SURFACE_DISTANCE_ASSEMBLY_CONSTRAINT_WITH_DIMENSION"));
    registry.put(
        "ANGULARITY_TOLERANCE_WITH_MODIFIERS",
        (resolver, instance) ->
            resolver.resolveRepresentationRelationship(
                instance, "ANGULARITY_TOLERANCE_WITH_MODIFIERS"));
    registerRepresentationRelationshipAliases(
        registry,
        "REPRESENTATION_RELATIONSHIP_WITH_SAME_CONTEXT",
        "KINEMATIC_FRAME_BACKGROUND_REPRESENTATION_RELATIONSHIP",
        "KINEMATIC_FRAME_REPRESENTATION_RELATIONSHIP",
        "KINEMATIC_GROUND_REPRESENTATION_RELATIONSHIP",
        "KINEMATIC_LINK_REPRESENTATION_RELATIONSHIP",
        "KINEMATIC_PAIR_REPRESENTATION_RELATIONSHIP",
        "MECHANISM_REPRESENTATION_RELATIONSHIP",
        "MECHANISM_STATE_REPRESENTATION_RELATIONSHIP");
    registry.put(
        "SHAPE_REPRESENTATION_RELATIONSHIP",
        StepEntityResolver::resolveShapeRepresentationRelationship);
    registry.put(
        "NEXT_ASSEMBLY_USAGE_OCCURRENCE", StepEntityResolver::resolveNextAssemblyUsageOccurrence);
    registry.put(
        "CONTEXT_DEPENDENT_SHAPE_REPRESENTATION",
        StepEntityResolver::resolveContextDependentShapeRepresentation);
    registry.put(
        "UNCERTAINTY_MEASURE_WITH_UNIT", StepEntityResolver::resolveUncertaintyMeasureWithUnit);
    registry.put(
        "LENGTH_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(instance, "LENGTH_MEASURE_WITH_UNIT", "LENGTH_UNIT"));
    registry.put(
        "MASS_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(instance, "MASS_MEASURE_WITH_UNIT", "MASS_UNIT"));
    registry.put(
        "TIME_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(instance, "TIME_MEASURE_WITH_UNIT", "TIME_UNIT"));
    registry.put(
        "PLANE_ANGLE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "PLANE_ANGLE_MEASURE_WITH_UNIT", "PLANE_ANGLE_UNIT"));
    registry.put(
        "SOLID_ANGLE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "SOLID_ANGLE_MEASURE_WITH_UNIT", "SOLID_ANGLE_UNIT"));
    registry.put(
        "AREA_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(instance, "AREA_MEASURE_WITH_UNIT", "AREA_UNIT"));
    registry.put(
        "VOLUME_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(instance, "VOLUME_MEASURE_WITH_UNIT", "VOLUME_UNIT"));
    registry.put(
        "RATIO_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(instance, "RATIO_MEASURE_WITH_UNIT", "RATIO_UNIT"));
    registry.put(
        "THERMODYNAMIC_TEMPERATURE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance,
                "THERMODYNAMIC_TEMPERATURE_MEASURE_WITH_UNIT",
                "THERMODYNAMIC_TEMPERATURE_UNIT"));
    registry.put(
        "ELECTRIC_CURRENT_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "ELECTRIC_CURRENT_MEASURE_WITH_UNIT", "ELECTRIC_CURRENT_UNIT"));
    registry.put(
        "AMOUNT_OF_SUBSTANCE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance,
                "AMOUNT_OF_SUBSTANCE_MEASURE_WITH_UNIT",
                "AMOUNT_OF_SUBSTANCE_UNIT"));
    registry.put(
        "FREQUENCY_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "FREQUENCY_MEASURE_WITH_UNIT", "FREQUENCY_UNIT"));
    registry.put(
        "FORCE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(instance, "FORCE_MEASURE_WITH_UNIT", "FORCE_UNIT"));
    registry.put(
        "PRESSURE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "PRESSURE_MEASURE_WITH_UNIT", "PRESSURE_UNIT"));
    registry.put(
        "ENERGY_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "ENERGY_MEASURE_WITH_UNIT", "ENERGY_UNIT"));
    registry.put(
        "POWER_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(instance, "POWER_MEASURE_WITH_UNIT", "POWER_UNIT"));
    registry.put(
        "ELECTRIC_CHARGE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "ELECTRIC_CHARGE_MEASURE_WITH_UNIT", "ELECTRIC_CHARGE_UNIT"));
    registry.put(
        "ELECTRIC_POTENTIAL_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance,
                "ELECTRIC_POTENTIAL_MEASURE_WITH_UNIT",
                "ELECTRIC_POTENTIAL_UNIT"));
    registry.put(
        "CAPACITANCE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "CAPACITANCE_MEASURE_WITH_UNIT", "CAPACITANCE_UNIT"));
    registry.put(
        "RESISTANCE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "RESISTANCE_MEASURE_WITH_UNIT", "RESISTANCE_UNIT"));
    registry.put(
        "CONDUCTANCE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "CONDUCTANCE_MEASURE_WITH_UNIT", "CONDUCTANCE_UNIT"));
    registry.put(
        "MAGNETIC_FLUX_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "MAGNETIC_FLUX_MEASURE_WITH_UNIT", "MAGNETIC_FLUX_UNIT"));
    registry.put(
        "MAGNETIC_FLUX_DENSITY_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance,
                "MAGNETIC_FLUX_DENSITY_MEASURE_WITH_UNIT",
                "MAGNETIC_FLUX_DENSITY_UNIT"));
    registry.put(
        "INDUCTANCE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "INDUCTANCE_MEASURE_WITH_UNIT", "INDUCTANCE_UNIT"));
    registry.put(
        "ILLUMINANCE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "ILLUMINANCE_MEASURE_WITH_UNIT", "ILLUMINANCE_UNIT"));
    registry.put(
        "LUMINOUS_FLUX_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "LUMINOUS_FLUX_MEASURE_WITH_UNIT", "LUMINOUS_FLUX_UNIT"));
    registry.put(
        "LUMINOUS_INTENSITY_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance,
                "LUMINOUS_INTENSITY_MEASURE_WITH_UNIT",
                "LUMINOUS_INTENSITY_UNIT"));
    registry.put(
        "RADIOACTIVITY_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "RADIOACTIVITY_MEASURE_WITH_UNIT", "RADIOACTIVITY_UNIT"));
    registry.put(
        "ABSORBED_DOSE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "ABSORBED_DOSE_MEASURE_WITH_UNIT", "ABSORBED_DOSE_UNIT"));
    registry.put(
        "DOSE_EQUIVALENT_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "DOSE_EQUIVALENT_MEASURE_WITH_UNIT", "DOSE_EQUIVALENT_UNIT"));
    registry.put(
        "ACCELERATION_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "ACCELERATION_MEASURE_WITH_UNIT", "ACCELERATION_UNIT"));
    registry.put(
        "VELOCITY_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "VELOCITY_MEASURE_WITH_UNIT", "VELOCITY_UNIT"));
    registry.put(
        "THERMAL_RESISTANCE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance,
                "THERMAL_RESISTANCE_MEASURE_WITH_UNIT",
                "THERMAL_RESISTANCE_UNIT"));
    registerTypedMeasureWithUnitPairs(
        registry,
        "MASS_DENSITY_UNIT",
        "DYNAMIC_VISCOSITY_UNIT",
        "KINEMATIC_VISCOSITY_UNIT",
        "MOMENT_OF_INERTIA_UNIT",
        "THERMAL_CONDUCTIVITY_UNIT",
        "HEAT_FLUX_DENSITY_UNIT",
        "SPECIFIC_HEAT_CAPACITY_UNIT",
        "AREA_DENSITY_UNIT",
        "VOLUMETRIC_FLOW_RATE_UNIT",
        "MASS_FLOW_RATE_UNIT",
        "ROTATIONAL_FREQUENCY_UNIT",
        "ANGULAR_VELOCITY_UNIT",
        "ANGULAR_ACCELERATION_UNIT",
        "TORQUE_UNIT",
        "LINEAR_FORCE_UNIT",
        "LINEAR_STIFFNESS_UNIT",
        "ROTATIONAL_STIFFNESS_UNIT",
        "LINEAR_MOMENT_UNIT");
    registry.put(
        "GLOBAL_UNIT_ASSIGNED_CONTEXT", StepEntityResolver::resolveGlobalUnitAssignedContext);
    registry.put(
        "GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT",
        StepEntityResolver::resolveGlobalUncertaintyAssignedContext);
    registry.put("MEASURE_WITH_UNIT", StepEntityResolver::resolveMeasureWithUnit);
    registry.put("DERIVED_UNIT_ELEMENT", StepEntityResolver::resolveDerivedUnitElement);
    registry.put("DERIVED_UNIT", StepEntityResolver::resolveDerivedUnit);
    registry.put("DIMENSIONAL_EXPONENTS", StepEntityResolver::resolveDimensionalExponents);
    registry.put("SI_UNIT", StepEntityResolver::resolveSiUnit);
    registry.put(
        "CONVERSION_BASED_UNIT_WITH_OFFSET",
        StepEntityResolver::resolveConversionBasedUnitWithOffset);
    registry.put("CONVERSION_BASED_UNIT", StepEntityResolver::resolveConversionBasedUnit);
    registry.put("CONTEXT_DEPENDENT_UNIT", StepEntityResolver::resolveContextDependentUnit);
    registry.put("NAMED_UNIT", StepEntityResolver::resolveNamedUnit);
    registry.put(
        "LENGTH_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "LENGTH_UNIT"));
    registry.put(
        "MASS_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "MASS_UNIT"));
    registry.put(
        "PLANE_ANGLE_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "PLANE_ANGLE_UNIT"));
    registry.put(
        "SOLID_ANGLE_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "SOLID_ANGLE_UNIT"));
    registry.put(
        "RATIO_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "RATIO_UNIT"));
    registry.put(
        "AREA_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "AREA_UNIT"));
    registry.put(
        "VOLUME_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "VOLUME_UNIT"));
    registry.put(
        "TIME_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "TIME_UNIT"));
    registry.put(
        "THERMODYNAMIC_TEMPERATURE_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneUnitKind(instance, "THERMODYNAMIC_TEMPERATURE_UNIT"));
    registry.put(
        "ELECTRIC_CURRENT_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneUnitKind(instance, "ELECTRIC_CURRENT_UNIT"));
    registry.put(
        "AMOUNT_OF_SUBSTANCE_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneUnitKind(instance, "AMOUNT_OF_SUBSTANCE_UNIT"));
    registry.put(
        "LUMINOUS_FLUX_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "LUMINOUS_FLUX_UNIT"));
    registry.put(
        "LUMINOUS_INTENSITY_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneUnitKind(instance, "LUMINOUS_INTENSITY_UNIT"));
    registry.put(
        "ACCELERATION_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneDerivedUnitKind(instance, "ACCELERATION_UNIT"));
    registry.put(
        "VELOCITY_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "VELOCITY_UNIT"));
    registry.put(
        "THERMAL_RESISTANCE_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneDerivedUnitKind(instance, "THERMAL_RESISTANCE_UNIT"));
    registry.put(
        "FREQUENCY_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "FREQUENCY_UNIT"));
    registry.put(
        "FORCE_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "FORCE_UNIT"));
    registry.put(
        "PRESSURE_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "PRESSURE_UNIT"));
    registry.put(
        "ENERGY_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "ENERGY_UNIT"));
    registry.put(
        "POWER_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "POWER_UNIT"));
    registry.put(
        "ELECTRIC_CHARGE_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneDerivedUnitKind(instance, "ELECTRIC_CHARGE_UNIT"));
    registry.put(
        "ELECTRIC_POTENTIAL_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneDerivedUnitKind(instance, "ELECTRIC_POTENTIAL_UNIT"));
    registry.put(
        "CAPACITANCE_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "CAPACITANCE_UNIT"));
    registry.put(
        "RESISTANCE_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "RESISTANCE_UNIT"));
    registry.put(
        "CONDUCTANCE_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "CONDUCTANCE_UNIT"));
    registry.put(
        "MAGNETIC_FLUX_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneDerivedUnitKind(instance, "MAGNETIC_FLUX_UNIT"));
    registry.put(
        "MAGNETIC_FLUX_DENSITY_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneDerivedUnitKind(instance, "MAGNETIC_FLUX_DENSITY_UNIT"));
    registry.put(
        "INDUCTANCE_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "INDUCTANCE_UNIT"));
    registry.put(
        "ILLUMINANCE_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "ILLUMINANCE_UNIT"));
    registry.put(
        "RADIOACTIVITY_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneDerivedUnitKind(instance, "RADIOACTIVITY_UNIT"));
    registry.put(
        "ABSORBED_DOSE_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneDerivedUnitKind(instance, "ABSORBED_DOSE_UNIT"));
    registry.put(
        "DOSE_EQUIVALENT_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneDerivedUnitKind(instance, "DOSE_EQUIVALENT_UNIT"));
    registerStandaloneDerivedUnitKinds(
        registry,
        "MASS_DENSITY_UNIT",
        "DYNAMIC_VISCOSITY_UNIT",
        "KINEMATIC_VISCOSITY_UNIT",
        "MOMENT_OF_INERTIA_UNIT",
        "THERMAL_CONDUCTIVITY_UNIT",
        "HEAT_FLUX_DENSITY_UNIT",
        "SPECIFIC_HEAT_CAPACITY_UNIT",
        "AREA_DENSITY_UNIT",
        "VOLUMETRIC_FLOW_RATE_UNIT",
        "MASS_FLOW_RATE_UNIT",
        "ROTATIONAL_FREQUENCY_UNIT",
        "ANGULAR_VELOCITY_UNIT",
        "ANGULAR_ACCELERATION_UNIT",
        "TORQUE_UNIT",
        "LINEAR_FORCE_UNIT",
        "LINEAR_STIFFNESS_UNIT",
        "ROTATIONAL_STIFFNESS_UNIT",
        "LINEAR_MOMENT_UNIT");
    registry.put("POINT", StepEntityResolver::resolvePoint);
    registry.put("RATIONAL_B_SPLINE_CURVE", StepEntityResolver::resolveRationalBSplineCurve);
    registry.put("RATIONAL_B_SPLINE_SURFACE", StepEntityResolver::resolveRationalBSplineSurface);
    registry.put("B_SPLINE_CURVE_WITH_KNOTS", StepEntityResolver::resolveBSplineCurveWithKnots);
    registry.put("B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS", StepEntityResolver::resolveBSplineCurveWithKnotsAndBreakpoints);
    registry.put("B_SPLINE_SURFACE_WITH_KNOTS", StepEntityResolver::resolveBSplineSurfaceWithKnots);
    registry.put("B_SPLINE_SURFACE_WITH_KNOTS_AND_BREAKPOINTS", StepEntityResolver::resolveBSplineSurfaceWithKnotsAndBreakpoints);
    registry.put("PIECEWISE_BEZIER_CURVE", StepEntityResolver::resolvePiecewiseBezierCurve);
    registry.put("PIECEWISE_BEZIER_SURFACE", StepEntityResolver::resolvePiecewiseBezierSurface);
    registry.put("BEZIER_CURVE", StepEntityResolver::resolveBezierCurve);
    registry.put("BEZIER_SURFACE", StepEntityResolver::resolveBezierSurface);
    registry.put("UNIFORM_CURVE", StepEntityResolver::resolveUniformCurve);
    registry.put("UNIFORM_SURFACE", StepEntityResolver::resolveUniformSurface);
    registry.put("QUASI_UNIFORM_CURVE", StepEntityResolver::resolveQuasiUniformCurve);
    registry.put("QUASI_UNIFORM_SURFACE", StepEntityResolver::resolveQuasiUniformSurface);
    registry.put("B_SPLINE_CURVE", StepEntityResolver::resolveBSplineCurve);
    registry.put("B_SPLINE_SURFACE", StepEntityResolver::resolveBSplineSurface);
    registry.put("FACE_BASED_SURFACE_MODEL", StepEntityResolver::resolveFaceBasedSurfaceModel);
    registry.put("SHELL_BASED_SURFACE_MODEL", StepEntityResolver::resolveShellBasedSurfaceModel);
    registry.put("SURFACE_MODEL", StepEntityResolver::resolveSurfaceModel);
    registry.put("COMPOSITE_CURVE_SEGMENT", StepEntityResolver::resolveCompositeCurveSegment);
    registry.put(
        "COMPOSITE_CURVE_ON_SURFACE", StepEntityResolver::resolveCompositeCurveOnSurface);
    registry.put(
        "BOUNDARY_CURVE",
        (resolver, instance) ->
            resolver.resolveCompositeCurveOnSurface(instance, "BOUNDARY_CURVE"));
    registry.put(
        "OUTER_BOUNDARY_CURVE",
        (resolver, instance) ->
            resolver.resolveCompositeCurveOnSurface(instance, "OUTER_BOUNDARY_CURVE"));
    registry.put("COMPOSITE_CURVE", StepEntityResolver::resolveCompositeCurve);
    registry.put("COMPOSITE_TEXT", StepEntityResolver::resolveCompositeText);
    registry.put("TEXT_LITERAL", StepEntityResolver::resolveTextLiteral);
    registry.put("COMPOSED_TEXT", StepEntityResolver::resolveComposedText);
    registry.put("POLYLINE", StepEntityResolver::resolvePolyline);
    registry.put("INDEXED_POLY_CURVE", StepEntityResolver::resolveIndexedPolyCurve);
    registry.put("BOUNDED_CURVE", StepEntityResolver::resolveBoundedCurve);
    registry.put("BOUNDED_SURFACE", StepEntityResolver::resolveBoundedSurface);
    registry.put("CURVE", StepEntityResolver::resolveCurve);
    registry.put("SURFACE", StepEntityResolver::resolveSurface);
    registry.put("OFFSET_CURVE_2D", StepEntityResolver::resolveOffsetCurve2D);
    registry.put("OFFSET_CURVE_3D", StepEntityResolver::resolveOffsetCurve3D);
    registry.put("ORIENTED_CURVE", StepEntityResolver::resolveOrientedCurve);
    registry.put("OFFSET_SURFACE", StepEntityResolver::resolveOffsetSurface);
    registry.put("OFFSET_SURFACE_2", StepEntityResolver::resolveOffsetSurface2);
    registry.put("VERTEX", StepEntityResolver::resolveVertex);
    registry.put("EDGE_BASED_WIREFRAME_MODEL", StepEntityResolver::resolveEdgeBasedWireframeModel);
    registry.put("CONNECTED_EDGE_SET", StepEntityResolver::resolveConnectedEdgeSet);
    registry.put("SUBEDGE", StepEntityResolver::resolveSubedge);
    registry.put("EDGE", StepEntityResolver::resolveEdge);
    registry.put("FACE", StepEntityResolver::resolveFace);
    registry.put("MANIFOLD_SOLID_BREP", StepEntityResolver::resolveManifoldSolidBrep);
    registry.put("NON_MANIFOLD_SOLID_BREP", StepEntityResolver::resolveNonManifoldSolidBrep);
    registry.put("FACETTED_BREP", StepEntityResolver::resolveFacettedBrep);
    registry.put("MANIFOLD_SURFACE_MODEL", StepEntityResolver::resolveManifoldSurfaceModel);
    registry.put("SURFACED_EDGE_CURVE", StepEntityResolver::resolveSurfacedEdgeCurve);
    registry.put("GEOMETRIC_TOLERANCE",
        (resolver, instance) -> resolver.resolveGeometricTolerance(instance, "GEOMETRIC_TOLERANCE"));
    // Phase 2C: PMI extension entities
    registry.put(
        "GEOMETRIC_TOLERANCE_WITH_DEFINED_UNIT",
        (resolver, instance) -> resolver.resolveGeometricTolerance(instance, "GEOMETRIC_TOLERANCE_WITH_DEFINED_UNIT"));
    registry.put("DATUM_REFERENCE_COMPARTMENT", StepEntityResolver::resolveDatumReferenceCompartment);
    registry.put(
        "DATUM_REFERENCE_ELEMENT",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "DATUM_REFERENCE_ELEMENT"));
    registry.put(
        "COMMON_DATUM",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "COMMON_DATUM"));
    registry.put("TOLERANCE_ZONE_FORM", StepEntityResolver::resolveToleranceZoneForm);
    registry.put("TOLERANCE_ZONE", StepEntityResolver::resolveToleranceZone);
    registry.put("CONFIGURATION_ITEM", StepEntityResolver::resolveConfigurationItem);
    registry.put("CONFIGURATION_EFFECTIVITY", StepEntityResolver::resolveConfigurationEffectivity);
    registry.put("FEATURE_CONTROL_FRAME", StepEntityResolver::resolveFeatureControlFrame);
    registry.put("RUNOUT_TOLERANCE_ZONE", StepEntityResolver::resolveRunoutToleranceZone);
    registry.put("MATERIAL_DESIGNATION", StepEntityResolver::resolveMaterialDesignation);
    // Phase 2D: Material and configuration entities
    registry.put(
        "MATERIAL_PROPERTY",
        (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
    registry.put(
        "MATERIAL_PROPERTY_REPRESENTATION",
        (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));
    registry.put(
        "EFFECTIVITY_CONTEXT",
        (resolver, instance) -> resolver.resolveEffectivity(instance));
    registry.put(
        "CLASSIFIED_EFFECTIVITY",
        (resolver, instance) -> resolver.resolveEffectivity(instance));
    registry.put("LAYERED_ITEM", StepEntityResolver::resolveLayeredItem);
    registry.put("COLOR_SPECIFICATION", StepEntityResolver::resolveColorSpecification);
    registry.put("WITH_DESCRIPTIVE_REPRESENTATION_ITEM",
        StepEntityResolver::resolveWithDescriptiveRepresentationItem);
    registry.put("DIRECTED_DIMENSIONAL_SIZE", StepEntityResolver::resolveDirectedDimensionalSize);
    registerGeometricToleranceAliases(
        registry,
        "POSITION_TOLERANCE",
        "FLATNESS_TOLERANCE",
        "STRAIGHTNESS_TOLERANCE",
        "CIRCULARITY_TOLERANCE",
        "PERPENDICULARITY_TOLERANCE",
        "PARALLELISM_TOLERANCE",
        "ANGULARITY_TOLERANCE",
        "CYLINDRICITY_TOLERANCE",
        "CONCENTRICITY_TOLERANCE",
        "SYMMETRY_TOLERANCE",
        "CIRCULAR_RUNOUT_TOLERANCE",
        "TOTAL_RUNOUT_TOLERANCE",
        "PROFILE_OF_A_LINE_TOLERANCE",
        "PROFILE_OF_A_SURFACE_TOLERANCE");
    registry.put("DATUM", StepEntityResolver::resolveDatum);
    registry.put("DATUM_FEATURE", StepEntityResolver::resolveDatumFeature);
    registry.put("DATUM_REFERENCE", StepEntityResolver::resolveDatumReference);
    registry.put("DATUM_TARGET", StepEntityResolver::resolveDatumTarget);
    // GD&T extended types (OCCT Phase 3)
    registry.put("GEOMETRIC_TOLERANCE_WITH_DEFINED_AREA_UNIT",
        StepEntityResolver::resolveGeometricToleranceWithDefinedAreaUnit);
    registry.put("GEOMETRIC_TOLERANCE_WITH_MAXIMUM_TOLERANCE",
        StepEntityResolver::resolveGeometricToleranceWithMaximumTolerance);
    registry.put("NON_UNIFORM_ZONE_DEFINITION", StepEntityResolver::resolveNonUniformZoneDefinition);
    registry.put("DATUM_REFERENCE_MODIFIER_WITH_VALUE",
        StepEntityResolver::resolveDatumReferenceModifierWithValue);
    registry.put("RUNOUT_ZONE_DEFINITION_ORIENTATION",
        StepEntityResolver::resolveRunoutZoneDefinitionOrientation);
    registry.put("DATUM_REFERENCE_MODIFIER", StepEntityResolver::resolveDatumReferenceModifier);
    registry.put("DATUM_SYSTEM_REFERENCE", StepEntityResolver::resolveDatumSystemReference);
    // Note: DATUM_SYSTEM is already registered earlier as a Shape Aspect alias via registerShapeAspectAliases
    registry.put("TOLERANCE_PAIR", StepEntityResolver::resolveTolerancePair);
    registry.put("TOLERANCE_SET", StepEntityResolver::resolveToleranceSet);
    registry.put("COMPOSITE_DATUM_REFERENCE", StepEntityResolver::resolveCompositeDatumReference);
    // Note: MACHINING_OPERATION, MACHINED_SURFACE are already registered as CharacterizedObject aliases
    registry.put("MACHINING_OPERATION_SEQUENCE", StepEntityResolver::resolveMachiningOperationSequence);
    registry.put("FILLET_DEFINITION", StepEntityResolver::resolveFilletDefinition);
    registry.put("CHAMFER_DEFINITION", StepEntityResolver::resolveChamferDefinition);
    // Note: ROUND, GROOVE, HOLE, SLOT, STUD, PROTRUSION, CUTOUT, DEPRESSION, MARKING,
    // CIRCULAR_PATTERN, LINEAR_PATTERN are already registered as CharacterizedObject aliases
    // or ShapeAspect aliases via registerCharacterizedObjectAliases/registerShapeAspectAliases
    registry.put("GEOMETRIC_MEASUREMENT", StepEntityResolver::resolveGeometricMeasurement);
    registry.put("DIMENSIONAL_MEASUREMENT", StepEntityResolver::resolveDimensionalMeasurement);
    registry.put("DIMENSIONAL_SIZE", StepEntityResolver::resolveDimensionalSize);
    registry.put("DIMENSIONAL_LOCATION", StepEntityResolver::resolveDimensionalLocation);
    registry.put("SHAPE_DIMENSION_REPRESENTATION", (resolver, instance) ->
        resolver.resolveRepresentation(instance, "SHAPE_DIMENSION_REPRESENTATION", true));
    registry.put("PLUS_MINUS_TOLERANCE", StepEntityResolver::resolvePlusMinusTolerance);
    registry.put("TOLERANCE_VALUE", StepEntityResolver::resolveToleranceValue);
    registry.put("MEASURE_REPRESENTATION_ITEM_WITH_UNIT", StepEntityResolver::resolveMeasureRepresentationItemWithUnit);
    registry.put("MEASURE_QUALIFICATION", StepEntityResolver::resolveMeasureQualification);
    registry.put("MAKE_FROM_FEATURE", StepEntityResolver::resolveMakeFromFeature);
    registry.put("MAKE_FROM_USAGE_OPTION", StepEntityResolver::resolveMakeFromUsageOption);
    registry.put("QUANTIFIED_ASSEMBLY_COMPONENT_USAGE", StepEntityResolver::resolveQuantifiedAssemblyComponentUsage);
    registry.put("SPECIFIED_HIGHER_USAGE_OCCURRENCE", StepEntityResolver::resolveSpecifiedHigherUsageOccurrence);
    registry.put("ALTERNATE_PRODUCT_RELATIONSHIP", StepEntityResolver::resolveAlternateProductRelationship);
    registry.put("PRODUCT_DEFINITION_WITH_ASSOCIATED_DOCUMENTS", StepEntityResolver::resolveProductDefinitionWithAssociatedDocuments);
    registry.put("SHAPE_ASPECT_SHAPE_REPRESENTATION", StepEntityResolver::resolveShapeAspectShapeRepresentation);
    registry.put("MAKE_FROM_BUILD_ASSEMBLY", StepEntityResolver::resolveMakeFromBuildAssembly);
    registry.put("ASSEMBLY_COMPONENT_RELATIONSHIP", StepEntityResolver::resolveAssemblyComponentRelationship);
    registry.put("DESIGN_MAKE_FROM", StepEntityResolver::resolveDesignMakeFrom);
    registry.put("INTERPOLATED_CONFIGURATION_SEGMENT", StepEntityResolver::resolveInterpolatedConfigurationSegment);
    registry.put("RANGE_DIMENSIONAL_SIZE", StepEntityResolver::resolveRangeDimensionalSize);
    registry.put("DESIGNED_PART_DESIGN_VERSION", StepEntityResolver::resolveDesignedPartDesignVersion);
    registry.put("SURFACE_STYLE_RENDERING", StepEntityResolver::resolveSurfaceStyleRendering);
    registry.put("SURFACE_STYLE_RENDERING_WITH_PROPERTIES", StepEntityResolver::resolveSurfaceStyleRenderingWithProperties);
    registry.put("RENDERING_PROPERTIES", StepEntityResolver::resolveRenderingProperties);
    registry.put("LIGHT_SOURCE", StepEntityResolver::resolveLightSource);
    registry.put("LIGHT_SOURCE_AMBIENT", StepEntityResolver::resolveLightSourceAmbient);
    registry.put("LIGHT_SOURCE_DIRECTIONAL", StepEntityResolver::resolveLightSourceDirectional);
    registry.put("LIGHT_SOURCE_POSITIONAL", StepEntityResolver::resolveLightSourcePositional);
    registry.put("LIGHT_SOURCE_SPOT", StepEntityResolver::resolveLightSourceSpot);
    registry.put("PRESENTATION_LAYER_USAGE", StepEntityResolver::resolvePresentationLayerUsage);
    registry.put("CAMERA_MODEL_D2", StepEntityResolver::resolveCameraModelD2);
    registry.put("CAMERA_MODEL_D3", StepEntityResolver::resolveCameraModelD3);
    registry.put("CAMERA_USAGE", StepEntityResolver::resolveCameraUsage);
    registry.put("CAMERA_IMAGE", StepEntityResolver::resolveCameraImage);
    registry.put("PLANAR_BOX", StepEntityResolver::resolvePlanarBox);
    registry.put("PLANAR_EXTENT", StepEntityResolver::resolvePlanarExtent);
    registry.put("VIEW_VOLUME", StepEntityResolver::resolveViewVolume);
    registry.put("MECHANICAL_DESIGN_SHAPE_REPRESENTATION", StepEntityResolver::resolveMechanicalDesignShapeRepresentation);
    registry.put("KINEMATIC_PAIR", (resolver, instance) ->
        resolver.resolveKinematicPair(instance, "KINEMATIC_PAIR"));
    registry.put("KINEMATIC_JOINT", StepEntityResolver::resolveKinematicJoint);
    registry.put("KINEMATIC_LINK", StepEntityResolver::resolveKinematicLink);
    registry.put("KINEMATIC_STRUCTURE", StepEntityResolver::resolveKinematicStructure);
    // Kinematic pair types (OCCT Phase 2)
    registry.put("PRISMATIC_PAIR", StepEntityResolver::resolvePrismaticPair);
    registry.put("REVOLUTE_PAIR", StepEntityResolver::resolveRevolutePair);
    registry.put("CYLINDRICAL_PAIR", StepEntityResolver::resolveCylindricalPair);
    registry.put("SPHERICAL_PAIR", StepEntityResolver::resolveSphericalPair);
    registry.put("PLANAR_PAIR", StepEntityResolver::resolvePlanarPair);
    registry.put("UNIVERSAL_PAIR", StepEntityResolver::resolveUniversalPair);
    registry.put("SCREW_PAIR", StepEntityResolver::resolveScrewPair);
    registry.put("GEAR_PAIR", StepEntityResolver::resolveGearPair);
    registry.put("GEAR_PAIR_WITH_RANGE", StepEntityResolver::resolveGearPairWithRange);
    registry.put("RACK_AND_PINION_PAIR", StepEntityResolver::resolveRackAndPinionPair);
    registry.put("LOW_ORDER_KINEMATIC_PAIR_WITH_RANGE",
        StepEntityResolver::resolveLowOrderKinematicPairWithRange);
    registry.put("ACTUATED_KINEMATIC_PAIR", StepEntityResolver::resolveActuatedKinematicPair);
    registry.put("KINEMATIC_PATH", StepEntityResolver::resolveKinematicPath);
    registry.put("KINEMATIC_FRAME_BASED_TRANSFORMATION",
        StepEntityResolver::resolveKinematicFrameBasedTransformation);
    // Kinematic pair aliases
    registerKinematicPairAliases(registry,
        "PRISMATIC_PAIR_WITH_RANGE", "REVOLUTE_PAIR_WITH_RANGE",
        "CYLINDRICAL_PAIR_WITH_RANGE", "SPHERICAL_PAIR_WITH_RANGE",
        "PLANAR_PAIR_WITH_RANGE", "UNIVERSAL_PAIR_WITH_RANGE",
        "SCREW_PAIR_WITH_RANGE", "FULLY_CONSTRAINED_PAIR",
        "HOMOKINETIC_PAIR", "PLANAR_CURVE_PAIR",
        "POINT_ON_PLANAR_CURVE_PAIR", "POINT_ON_SURFACE_PAIR",
        "ROLLING_CURVE_PAIR", "ROLLING_SURFACE_PAIR",
        "SLIDING_CURVE_PAIR", "SLIDING_SURFACE_PAIR",
        "SPHERICAL_PAIR_WITH_PIN", "SPHERICAL_PAIR_WITH_PIN_AND_RANGE",
        "SURFACE_PAIR_WITH_RANGE", "UNCONSTRAINED_PAIR");
    registry.put("ANALYSIS_RESULT", StepEntityResolver::resolveAnalysisResult);
    registry.put("ANALYSIS_INSTANCE", StepEntityResolver::resolveAnalysisInstance);
    registry.put("CONFIGURATION_INSTANCE", StepEntityResolver::resolveConfigurationInstance);
    registry.put("MODEL_DEFINITION", StepEntityResolver::resolveModelDefinition);
    registry.put("MODEL_INSTANCE", StepEntityResolver::resolveModelInstance);
    registry.put("SIMULATION_DEFINITION", StepEntityResolver::resolveSimulationDefinition);
    registry.put("SIMULATION_INSTANCE", StepEntityResolver::resolveSimulationInstance);
    registry.put("PERSON_AND_ORGANIZATION_ADDRESS", StepEntityResolver::resolvePersonAndOrganizationAddress);
    registry.put("ORGANIZATION_ADDRESS", StepEntityResolver::resolveOrganizationAddress);
    registry.put("PERSON_ADDRESS", StepEntityResolver::resolvePersonAddress);
    registry.put("ANGULAR_SIZE", StepEntityResolver::resolveAngularSize);
    registry.put("GENERALIZED_DATUM", StepEntityResolver::resolveGeneralizedDatum);
    registry.put("ACTION_DIRECTIVE", StepEntityResolver::resolveActionDirective);
    registry.put("ACTION_METHOD", StepEntityResolver::resolveActionMethod);
    registry.put("ACTION", StepEntityResolver::resolveAction);
    registry.put("ACTION_RELATIONSHIP", StepEntityResolver::resolveActionRelationship);
    registry.put("ACTION_STATUS", StepEntityResolver::resolveActionStatus);
    registry.put("SOLID_MODEL", StepEntityResolver::resolveSolidModel);
    registry.put("ANNOTATION_FILL_AREA", StepEntityResolver::resolveAnnotationFillArea);
    registry.put(
        "ANNOTATION_FILL_AREA_OCCURRENCE",
        StepEntityResolver::resolveAnnotationFillAreaOccurrence);
    registry.put(
        "ANNOTATION_PLACEHOLDER_OCCURRENCE",
        StepEntityResolver::resolveAnnotationPlaceholderOccurrence);
    registry.put("ANNOTATION_PLANE", StepEntityResolver::resolveAnnotationPlane);
    registry.put("ANNOTATION_POINT_OCCURRENCE", StepEntityResolver::resolveAnnotationPointOccurrence);
    registry.put("LEADER_CURVE", StepEntityResolver::resolveLeaderCurve);
    registry.put("PROJECTION_CURVE", StepEntityResolver::resolveProjectionCurve);
    registry.put("DIMENSION_CURVE", StepEntityResolver::resolveDimensionCurve);
    registry.put(
        "ANNOTATION_SUBFIGURE_OCCURRENCE",
        StepEntityResolver::resolveAnnotationSubfigureOccurrence);
    registry.put(
        "DRAUGHTING_ANNOTATION_OCCURRENCE",
        StepEntityResolver::resolveDraughtingAnnotationOccurrence);
    registry.put("ANNOTATION_CURVE_OCCURRENCE", StepEntityResolver::resolveAnnotationCurveOccurrence);
    registry.put(
        "GEOMETRIC_REPRESENTATION_ITEM",
        StepEntityResolver::resolveGeometricRepresentationItem);
    registry.put(
        "TOPOLOGICAL_REPRESENTATION_ITEM",
        StepEntityResolver::resolveTopologicalRepresentationItem);
    registry.put("REPRESENTATION_ITEM", StepEntityResolver::resolveRepresentationItem);
    registry.put("REPRESENTATION_CONTEXT", StepEntityResolver::resolveRepresentationContext);
    registry.put(
        "DEFINITIONAL_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "DEFINITIONAL_REPRESENTATION", false));
    registry.put("COLOUR_RGB", StepEntityResolver::resolveColourRgb);
    registry.put(
        "DRAUGHTING_PRE_DEFINED_CURVE_FONT",
        StepEntityResolver::resolveDraughtingPreDefinedCurveFont);
    registry.put(
        "PRE_DEFINED_POINT_MARKER_SYMBOL",
        StepEntityResolver::resolvePreDefinedPointMarkerSymbol);
    registry.put(
        "PRE_DEFINED_DIMENSION_SYMBOL",
        StepEntityResolver::resolvePreDefinedDimensionSymbol);
    registry.put(
        "PRE_DEFINED_GEOMETRICAL_TOLERANCE_SYMBOL",
        StepEntityResolver::resolvePreDefinedGeometricalToleranceSymbol);
    registry.put(
        "PRE_DEFINED_TERMINATOR_SYMBOL",
        StepEntityResolver::resolvePreDefinedTerminatorSymbol);
    registry.put(
        "PRE_DEFINED_SURFACE_SIDE_STYLE",
        StepEntityResolver::resolvePreDefinedSurfaceSideStyle);
    registry.put(
        "DRAUGHTING_PRE_DEFINED_TEXT_FONT",
        StepEntityResolver::resolveDraughtingPreDefinedTextFont);
    registry.put("PRE_DEFINED_TEXT_FONT", StepEntityResolver::resolvePreDefinedTextFont);
    registry.put("PRE_DEFINED_ITEM", StepEntityResolver::resolvePreDefinedItem);
    registry.put("PRE_DEFINED_MARKER", StepEntityResolver::resolvePreDefinedMarker);
    registry.put("PRE_DEFINED_SYMBOL", StepEntityResolver::resolvePreDefinedSymbol);
    registry.put("PRE_DEFINED_CURVE_FONT", StepEntityResolver::resolvePreDefinedCurveFont);
    registry.put(
        "DRAUGHTING_PRE_DEFINED_COLOUR", StepEntityResolver::resolveDraughtingPreDefinedColour);
    registry.put("PRE_DEFINED_COLOUR", StepEntityResolver::resolvePreDefinedColour);
    registry.put("COLOUR_SPECIFICATION", StepEntityResolver::resolveColourSpecification);
    registry.put("COLOUR", StepEntityResolver::resolveColour);
    registry.put("CURVE_STYLE", StepEntityResolver::resolveCurveStyle);
    registry.put("POINT_STYLE", StepEntityResolver::resolvePointStyle);
    registry.put(
        "CHARACTER_GLYPH_STYLE_OUTLINE_WITH_CHARACTERISTICS",
        StepEntityResolver::resolveCharacterGlyphStyleOutlineWithCharacteristics);
    registry.put(
        "CHARACTER_GLYPH_STYLE_OUTLINE",
        StepEntityResolver::resolveCharacterGlyphStyleOutline);
    registry.put(
        "CHARACTER_GLYPH_STYLE_STROKE",
        StepEntityResolver::resolveCharacterGlyphStyleStroke);
    registry.put("TEXT_STYLE_FOR_DEFINED_FONT", StepEntityResolver::resolveTextStyleForDefinedFont);
    registry.put("TEXT_STYLE_WITH_SPACING", StepEntityResolver::resolveTextStyleWithSpacing);
    registry.put(
        "TEXT_STYLE_WITH_JUSTIFICATION",
        StepEntityResolver::resolveTextStyleWithJustification);
    registry.put("TEXT_STYLE_WITH_MIRROR", StepEntityResolver::resolveTextStyleWithMirror);
    registry.put(
        "TEXT_STYLE_WITH_BOX_CHARACTERISTICS",
        StepEntityResolver::resolveTextStyleWithBoxCharacteristics);
    registry.put("TEXT_STYLE", StepEntityResolver::resolveTextStyle);
    registry.put("SYMBOL_COLOUR", StepEntityResolver::resolveSymbolColour);
    registry.put("SYMBOL_STYLE", StepEntityResolver::resolveSymbolStyle);
    registry.put("FILL_AREA_STYLE_COLOUR", StepEntityResolver::resolveFillAreaStyleColour);
    registry.put("FILL_AREA_STYLE", StepEntityResolver::resolveFillAreaStyle);
    registry.put("SURFACE_STYLE_FILL_AREA", StepEntityResolver::resolveSurfaceStyleFillArea);
    registry.put("SURFACE_STYLE_BOUNDARY", StepEntityResolver::resolveSurfaceStyleBoundary);
    registry.put("SURFACE_STYLE_CONTROL_GRID", StepEntityResolver::resolveSurfaceStyleControlGrid);
    registry.put(
        "SURFACE_STYLE_SEGMENTATION_CURVE",
        StepEntityResolver::resolveSurfaceStyleSegmentationCurve);
    registry.put("SURFACE_STYLE_SILHOUETTE", StepEntityResolver::resolveSurfaceStyleSilhouette);
    registry.put("SURFACE_STYLE_TRANSPARENT", StepEntityResolver::resolveSurfaceStyleTransparent);
    registry.put(
        "SURFACE_STYLE_REFLECTANCE_AMBIENT",
        StepEntityResolver::resolveSurfaceStyleReflectanceAmbient);
    registry.put(
        "SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE",
        StepEntityResolver::resolveSurfaceStyleReflectanceAmbientDiffuse);
    registry.put(
        "SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE_SPECULAR",
        StepEntityResolver::resolveSurfaceStyleReflectanceAmbientDiffuseSpecular);
    registry.put(
        "SURFACE_STYLE_PARAMETER_LINE",
        StepEntityResolver::resolveSurfaceStyleParameterLine);
    registry.put("SURFACE_SIDE_STYLE", StepEntityResolver::resolveSurfaceSideStyle);
    registry.put("SURFACE_STYLE_USAGE", StepEntityResolver::resolveSurfaceStyleUsage);
    registry.put(
        "PRESENTATION_STYLE_ASSIGNMENT", StepEntityResolver::resolvePresentationStyleAssignment);
    registry.put("STYLED_ITEM", StepEntityResolver::resolveStyledItem);
    registry.put("OVER_RIDING_STYLED_ITEM", StepEntityResolver::resolveOverRidingStyledItem);
    registry.put(
        "PRESENTATION_LAYER_ASSIGNMENT", StepEntityResolver::resolvePresentationLayerAssignment);
    registry.put("ANNOTATION_TEXT", StepEntityResolver::resolveAnnotationText);
    registry.put("ANNOTATION_TEXT_CHARACTER", StepEntityResolver::resolveAnnotationTextCharacter);
    registry.put("ANNOTATION_SYMBOL", StepEntityResolver::resolveAnnotationSymbol);
    registry.put("ANNOTATION_SYMBOL_OCCURRENCE", StepEntityResolver::resolveAnnotationSymbolOccurrence);
    registry.put("TERMINATOR_SYMBOL", StepEntityResolver::resolveTerminatorSymbol);
    registry.put(
        "ANNOTATION_OCCURRENCE_RELATIONSHIP",
        StepEntityResolver::resolveAnnotationOccurrenceRelationship);
    registry.put(
        "ANNOTATION_OCCURRENCE_ASSOCIATIVITY",
        (resolver, instance) ->
            resolver.resolveAnnotationOccurrenceRelationship(instance, "ANNOTATION_OCCURRENCE_ASSOCIATIVITY"));
    registry.put(
        "DIMENSION_CURVE_TERMINATOR_TO_PROJECTION_CURVE_ASSOCIATIVITY",
        (resolver, instance) ->
            resolver.resolveAnnotationOccurrenceRelationship(
                instance, "DIMENSION_CURVE_TERMINATOR_TO_PROJECTION_CURVE_ASSOCIATIVITY"));
    registry.put("ANNOTATION_TEXT_OCCURRENCE", StepEntityResolver::resolveAnnotationTextOccurrence);
    registry.put("GEOMETRIC_CURVE_SET", StepEntityResolver::resolveGeometricCurveSet);
    registry.put("GEOMETRIC_SURFACE_SET", StepEntityResolver::resolveGeometricSurfaceSet);
    registry.put("GEOMETRIC_SET", StepEntityResolver::resolveGeometricSet);
    registry.put("POINT_SET", StepEntityResolver::resolvePointSet);
    // Phase 2B: Additional geometric set variants
    registry.put(
        "GEOMETRIC_SET_2D",
        (resolver, instance) -> resolver.resolveGeometricSet(instance));
    registry.put(
        "GEOMETRIC_SET_3D",
        (resolver, instance) -> resolver.resolveGeometricSet(instance));
    registry.put(
        "TRIANGULATED_SURFACE_SET",
        (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));
    registry.put(
        "POLYGONAL_FACE_SET",
        (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));
    registry.put("LEADER_DIRECTED_CALLOUT",
        (resolver, instance) -> resolver.resolveDraughtingCallout(instance, "LEADER_DIRECTED_CALLOUT"));
    registry.put(
        "PROJECTION_DIRECTED_CALLOUT",
        (resolver, instance) ->
            resolver.resolveDraughtingCallout(instance, "PROJECTION_DIRECTED_CALLOUT"));
    registry.put(
        "DIMENSION_CURVE_DIRECTED_CALLOUT",
        (resolver, instance) ->
            resolver.resolveDraughtingCallout(instance, "DIMENSION_CURVE_DIRECTED_CALLOUT"));
    registry.put(
        "DIMENSION_CALLOUT",
        (resolver, instance) -> resolver.resolveDraughtingCallout(instance, "DIMENSION_CALLOUT"));
    registry.put(
        "DATUM_FEATURE_CALLOUT",
        (resolver, instance) -> resolver.resolveDraughtingCallout(instance, "DATUM_FEATURE_CALLOUT"));
    registry.put(
        "DATUM_TARGET_CALLOUT",
        (resolver, instance) -> resolver.resolveDraughtingCallout(instance, "DATUM_TARGET_CALLOUT"));
    registry.put(
        "GEOMETRICAL_TOLERANCE_CALLOUT",
        (resolver, instance) ->
            resolver.resolveDraughtingCallout(instance, "GEOMETRICAL_TOLERANCE_CALLOUT"));
    registry.put(
        "ROUGHNESS_CALLOUT",
        (resolver, instance) -> resolver.resolveDraughtingCallout(instance, "ROUGHNESS_CALLOUT"));
    registry.put(
        "STRUCTURED_DIMENSION_CALLOUT",
        (resolver, instance) ->
            resolver.resolveDraughtingCallout(instance, "STRUCTURED_DIMENSION_CALLOUT"));
    registry.put(
        "SURFACE_CONDITION_CALLOUT",
        (resolver, instance) ->
            resolver.resolveDraughtingCallout(instance, "SURFACE_CONDITION_CALLOUT"));
    registry.put("DRAUGHTING_CALLOUT", StepEntityResolver::resolveDraughtingCallout);
    registry.put(
        "DRAUGHTING_CALLOUT_RELATIONSHIP",
        StepEntityResolver::resolveDraughtingCalloutRelationship);
    registry.put(
        "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE",
        StepEntityResolver::resolveChainBasedGeometricItemSpecificUsage);
    registry.put(
        "CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE",
        StepEntityResolver::resolveChainBasedItemIdentifiedRepresentationUsage);
    registry.put(
        "MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION",
        StepEntityResolver::resolveMechanicalDesignRequirementItemAssociation);
    registry.put(
        "PMI_REQUIREMENT_ITEM_ASSOCIATION",
        StepEntityResolver::resolvePmiRequirementItemAssociation);
    registry.put("PLACED_TARGET", StepEntityResolver::resolvePlacedTarget);
    registry.put(
        "DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER",
        StepEntityResolver::resolveDraughtingModelItemAssociationWithPlaceholder);
    registry.put(
        "GEOMETRIC_ITEM_SPECIFIC_USAGE", StepEntityResolver::resolveGeometricItemSpecificUsage);
    registry.put(
        "DRAUGHTING_MODEL_ITEM_ASSOCIATION",
        StepEntityResolver::resolveDraughtingModelItemAssociation);
    registry.put(
        "ITEM_IDENTIFIED_REPRESENTATION_USAGE",
        StepEntityResolver::resolveItemIdentifiedRepresentationUsage);
    registry.put(
        "MEASURE_REPRESENTATION_ITEM", StepEntityResolver::resolveMeasureRepresentationItem);
    registry.put(
        "DESCRIPTIVE_REPRESENTATION_ITEM",
        StepEntityResolver::resolveDescriptiveRepresentationItem);
    registry.put(
        "VALUE_REPRESENTATION_ITEM", StepEntityResolver::resolveValueRepresentationItem);
    registry.put("CARTESIAN_POINT", StepEntityResolver::resolveCartesianPoint);
    registry.put("DIRECTION", StepEntityResolver::resolveDirection);
    registry.put("VECTOR", StepEntityResolver::resolveVector);
    registry.put("AXIS1_PLACEMENT", StepEntityResolver::resolveAxis1Placement);
    registry.put("AXIS2_PLACEMENT_2D", StepEntityResolver::resolveAxis2Placement2D);
    registry.put("AXIS2_PLACEMENT_3D", StepEntityResolver::resolveAxis2Placement3D);
    registry.put("LINE", StepEntityResolver::resolveLine);
    registry.put("PLANE", StepEntityResolver::resolvePlane);
    registry.put("CIRCLE", StepEntityResolver::resolveCircle);
    registry.put("ELLIPSE", StepEntityResolver::resolveEllipse);
    registry.put(
        "PARABOLA",
        (resolver, instance) -> resolver.resolveConicCurve(instance, "PARABOLA", 1));
    registry.put(
        "HYPERBOLA",
        (resolver, instance) -> resolver.resolveConicCurve(instance, "HYPERBOLA", 2));
    registry.put(
        "DEGENERATE_CONIC",
        (resolver, instance) -> resolver.resolveConicCurve(instance, "DEGENERATE_CONIC", 0));
    registry.put(
        "CONIC_CURVE",
        (resolver, instance) -> resolver.resolveConicCurve(instance, "CONIC_CURVE", 2));
    registry.put("CLOTHOID", StepEntityResolver::resolveClothoid);
    registry.put("SURFACE_CURVE", StepEntityResolver::resolveSurfaceCurve);
    registry.put(
        "INTERSECTION_CURVE",
        (resolver, instance) -> resolver.resolveSurfaceCurve(instance, "INTERSECTION_CURVE"));
    registry.put("SEAM_CURVE", StepEntityResolver::resolveSeamCurve);
    registry.put("DEGENERATE_CURVE", StepEntityResolver::resolveDegenerateCurve);
    registry.put("DEGENERATE_PCURVE", StepEntityResolver::resolveDegeneratePcurve);
    registry.put("PCURVE", StepEntityResolver::resolvePcurve);
    registry.put("CYLINDRICAL_SURFACE", StepEntityResolver::resolveCylindricalSurface);
    registry.put("CONICAL_SURFACE", StepEntityResolver::resolveConicalSurface);
    registry.put("TOROIDAL_SURFACE", StepEntityResolver::resolveToroidalSurface);
    registry.put(
        "DEGENERATE_TOROIDAL_SURFACE",
        StepEntityResolver::resolveDegenerateToroidalSurface);
    registry.put("SPHERICAL_SURFACE", StepEntityResolver::resolveSphericalSurface);
    registry.put("SPHERICAL_SURFACE_WITH_ELLIPTICAL_AXIS", StepEntityResolver::resolveSphericalSurfaceWithEllipticalAxis);
    registry.put("CYLINDRICAL_SURFACE_WITH_ELLIPTICAL_AXIS", StepEntityResolver::resolveCylindricalSurfaceWithEllipticalAxis);
    registry.put("CONICAL_SURFACE_WITH_ELLIPTICAL_AXIS", StepEntityResolver::resolveConicalSurfaceWithEllipticalAxis);
    registry.put("TOROIDAL_SURFACE_WITH_ELLIPTICAL_AXIS", StepEntityResolver::resolveToroidalSurfaceWithEllipticalAxis);
    registry.put("TOROIDAL_SURFACE_WITH_CYLINDRICAL_AXIS", StepEntityResolver::resolveToroidalSurfaceWithCylindricalAxis);
    registry.put("BLENDED_SURFACE", StepEntityResolver::resolveBlendedSurface);
    registry.put("CHAMFER_EDGE", StepEntityResolver::resolveChamferEdge);
    registry.put("FILLET_EDGE", StepEntityResolver::resolveFilletEdge);
    registry.put("FREE_FORM_SURFACE", StepEntityResolver::resolveFreeFormSurface);
    registry.put("CURVED_TOLERANCE_ZONE", StepEntityResolver::resolveCurvedToleranceZone);
    registry.put("SURFACE_QUALITY", StepEntityResolver::resolveSurfaceQuality);
    registry.put("MEASUREMENT_POINT", StepEntityResolver::resolveMeasurementPoint);
    registry.put("SURFACE_MEASUREMENT", StepEntityResolver::resolveSurfaceMeasurement);
    registry.put("SURFACE_TEXTURE_REPRESENTATION_ITEM", StepEntityResolver::resolveSurfaceTextureRepresentationItem);
    registry.put("RULED_SURFACE", StepEntityResolver::resolveRuledSurface);
    registry.put("SURFACE_PATCH", StepEntityResolver::resolveSurfacePatch);
    registry.put(
        "RECTANGULAR_TRIMMED_SURFACE",
        StepEntityResolver::resolveRectangularTrimmedSurface);
    registry.put("CURVE_BOUNDED_SURFACE", StepEntityResolver::resolveCurveBoundedSurface);
    registry.put("ORIENTED_SURFACE", StepEntityResolver::resolveOrientedSurface);
    registry.put(
        "SURFACE_OF_LINEAR_EXTRUSION", StepEntityResolver::resolveSurfaceOfLinearExtrusion);
    registry.put("SURFACE_OF_REVOLUTION", StepEntityResolver::resolveSurfaceOfRevolution);
    registry.put("SURFACE_OF_CONSTANT_RADIUS", StepEntityResolver::resolveSurfaceOfConstantRadius);
    registry.put("TRIMMED_CURVE", StepEntityResolver::resolveTrimmedCurve);
    registry.put("VERTEX_POINT", StepEntityResolver::resolveVertexPoint);
    registry.put("EDGE_CURVE", StepEntityResolver::resolveEdgeCurve);
    registry.put("ORIENTED_EDGE", StepEntityResolver::resolveOrientedEdge);
    registry.put("VERTEX_LOOP", StepEntityResolver::resolveVertexLoop);
    registry.put("POLY_LOOP", StepEntityResolver::resolvePolyLoop);
    registry.put("OPEN_PATH", StepEntityResolver::resolveOpenPath);
    registry.put("SUBPATH", StepEntityResolver::resolveSubpath);
    registry.put("ORIENTED_PATH", StepEntityResolver::resolveOrientedPath);
    registry.put("PATH", StepEntityResolver::resolvePath);
    registry.put("EDGE_LOOP", StepEntityResolver::resolveEdgeLoop);
    registry.put("EDGE_WIRE", StepEntityResolver::resolveEdgeWire);
    registry.put("LINE_SEGMENT", StepEntityResolver::resolveLineSegment);
    registry.put("RECTANGULAR_COMPOSITE_SURFACE", StepEntityResolver::resolveRectangularCompositeSurface);
    registry.put("COMPOSITE_CURVE_ON_SURFACE_3D", StepEntityResolver::resolveCompositeCurveOnSurface3D);
    registry.put(
        "FACE_OUTER_BOUND", (resolver, instance) -> resolver.resolveFaceBound(instance, true));
    registry.put("FACE_BOUND", (resolver, instance) -> resolver.resolveFaceBound(instance, false));
    registry.put("FACE_SURFACE", StepEntityResolver::resolveFaceSurface);
    registry.put("ADVANCED_FACE", StepEntityResolver::resolveAdvancedFace);
    registry.put("ORIENTED_FACE", StepEntityResolver::resolveOrientedFace);
    registry.put("VERTEX_SHELL", StepEntityResolver::resolveVertexShell);
    registry.put("WIRE_SHELL", StepEntityResolver::resolveWireShell);
    registry.put("CONNECTED_FACE_SUB_SET", StepEntityResolver::resolveConnectedFaceSubSet);
    registry.put("CONNECTED_FACE_SET", StepEntityResolver::resolveConnectedFaceSet);
    registry.put("TESSELLATED_FACE_SET", StepEntityResolver::resolveTessellatedFaceSet);
    registry.put("SEAM_EDGE", StepEntityResolver::resolveSeamEdge);
    registry.put("TESSELLATED_FACE", StepEntityResolver::resolveTessellatedFace);
    registry.put("TESSELLATED_TRIANGLE", StepEntityResolver::resolveTessellatedTriangle);
    // Tessellated triangulated types (OCCT Phase 4)
    registry.put("TRIANGULATED_FACE", StepEntityResolver::resolveTriangulatedFace);
    registry.put("COMPLEX_TRIANGULATED_FACE", StepEntityResolver::resolveComplexTriangulatedFace);
    registry.put("CUBIC_BEZIER_TRIANGULATED_FACE", StepEntityResolver::resolveCubicBezierTriangulatedFace);
    registry.put("FINITE_ELEMENT_MESH", StepEntityResolver::resolveFiniteElementMesh);
    // FEA element types (OCCT Phase 5)
    registry.put("VOLUME_3D_ELEMENT_REPRESENTATION",
        StepEntityResolver::resolveVolume3dElementRepresentation);
    registry.put("VOLUME_3D_ELEMENT_PROPERTY", StepEntityResolver::resolveVolume3dElementProperty);
    registry.put("CURVE_3D_ELEMENT_PROPERTY", StepEntityResolver::resolveCurve3dElementProperty);
    registry.put("SURFACE_3D_ELEMENT_PROPERTY", StepEntityResolver::resolveSurface3dElementProperty);
    registry.put("FEA_MATERIAL_PROPERTY_REPRESENTATION",
        StepEntityResolver::resolveFeaMaterialPropertyRepresentation);
    registry.put("ELEMENT_VOLUME_2D", StepEntityResolver::resolveElementVolume2d);
    registry.put("ELEMENT_VOLUME_3D", StepEntityResolver::resolveElementVolume3d);
    registry.put("NODE_SET", StepEntityResolver::resolveNodeSet);
    registry.put("ELEMENT_SET", StepEntityResolver::resolveElementSet);
    registry.put("FEA_SECURED_VARIABLE", StepEntityResolver::resolveFeaSecuredVariable);
    registry.put("FEA_CONSTANT_FUNCTION_3D", StepEntityResolver::resolveFeaConstantFunction3d);
    registry.put("FEA_LINEAR_ALGEBRAIC_MATRIX", StepEntityResolver::resolveFeaLinearAlgebraicMatrix);
    registry.put("FEA_LINEAR_ALGEBRAIC_VECTOR", StepEntityResolver::resolveFeaLinearAlgebraicVector);
    registry.put("FEA_AXIS_2_PLACEMENT_3D", StepEntityResolver::resolveFeaAxis2Placement3d);
    registry.put("FEA_GROUP_REPRESENTATION", StepEntityResolver::resolveFeaGroupRepresentation);
    // FEA aliases
    registerFeaAliases(registry,
        "VOLUME_3D_ELEMENT_DESCRIPTOR", "SURFACE_3D_ELEMENT_DESCRIPTOR",
        "CURVE_3D_ELEMENT_DESCRIPTOR", "NODE_REPRESENTATION",
        "ELEMENT_REPRESENTATION", "NODE_DEFINITION",
        "FEA_MODEL", "FEA_MODEL_3D", "FEA_REPRESENTATION_ITEM");
    // Phase 2B: Advanced geometry entities
    registry.put(
        "TRIANGULATED_FACE_SET",
        (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance)); // Same as TESSELLATED_FACE_SET
    registry.put("SUBFACE", StepEntityResolver::resolveSubface);
    registry.put("ORIENTED_SUBFACE", StepEntityResolver::resolveOrientedSubface);
    registry.put("SURFACED_OPEN_SHELL", StepEntityResolver::resolveSurfacedOpenShell);
    registry.put("ORIENTED_OPEN_SHELL", StepEntityResolver::resolveOrientedOpenShell);
    registry.put("ORIENTED_CLOSED_SHELL", StepEntityResolver::resolveOrientedClosedShell);
    registry.put("SHELL_BASED_WIREFRAME_MODEL", StepEntityResolver::resolveShellBasedWireframeModel);
    registry.put("OPEN_SHELL", StepEntityResolver::resolveOpenShell);
    registry.put("CLOSED_SHELL", StepEntityResolver::resolveClosedShell);
    // Phase 2 extended: Additional geometric tolerance type aliases
    registerGeometricToleranceAliases(
        registry,
        "COAXIALITY_TOLERANCE",
        "PROFILE_OF_A_POINT_TOLERANCE",
        "LINE_PROFILE_TOLERANCE",
        "SURFACE_PROFILE_TOLERANCE",
        "RUNOUT_TOLERANCE",
        "AXIAL_RUNOUT_TOLERANCE",
        "RADIAL_RUNOUT_TOLERANCE",
        "TOTAL_AXIAL_RUNOUT_TOLERANCE",
        "TOTAL_RADIAL_RUNOUT_TOLERANCE");
    // Phase 2 extended: Additional shape aspect aliases
    registerShapeAspectAliases(
        registry,
        "ASSEMBLY_FEATURE",
        "BOUNDARY_CURVE_ELEMENT",
        "CHAMFER_EDGE",
        "CIRCULAR_PATTERN_MEMBER",
        "CORNER_FEATURE",
        "CROSS_SECTION_FEATURE",
        "CURVE_BASED_FEATURE",
        "DEFINITIONAL_SHAPE_ASPECT",
        "DERIVED_SHAPE_ASPECT",
        "EDGE_BLEND_FEATURE",
        "EDGE_FEATURE",
        "FABRICATED_FEATURE",
        "FINISH_FEATURE",
        "FLANGE_FEATURE",
        "FREE_FORM_FEATURE",
        "GROOVE_FEATURE",
        "GUIDE_FEATURE",
        "HEAT_TREAT_FEATURE",
        "KNURL_FEATURE",
        "LAND_FEATURE",
        "LEAD_FEATURE",
        "MACHINING_FEATURE",
        "MOLD_FEATURE",
        "MOUNTING_FEATURE",
        "NOTCH_FEATURE",
        "PASSAGE_FEATURE",
        "PIPE_FEATURE",
        "PLATE_FEATURE",
        "PRESS_FEATURE",
        "PUNCH_FEATURE",
        "RACE_FEATURE",
        "RADIUS_FEATURE",
        "REFERENCE_FEATURE",
        "RIB_EDGE",
        "ROUND_FEATURE",
        "SEAL_FEATURE",
        "SHEET_FEATURE",
        "SKETCH_FEATURE",
        "SLOT_FEATURE",
        "SPINE_FEATURE",
        "SPRING_FEATURE",
        "STEP_FEATURE",
        "STUD_FEATURE",
        "SURFACE_BASED_FEATURE",
        "TAB_FEATURE",
        "TAPER_FEATURE",
        "THREAD_FEATURE",
        "TOLERANCE_FEATURE",
        "TURN_FEATURE",
        "UNDERCUT_FEATURE",
        "VENT_FEATURE",
        "WELD_FEATURE",
        "WRAP_FEATURE");
    // Phase 6: Additional manufacturing feature aliases (verified ShapeAspect 4-param structure)
    registerShapeAspectAliases(
        registry,
        "ACCESS_FEATURE",
        "ACTUATOR_FEATURE",
        "CASTING_FEATURE",
        "CLAMP_FEATURE",
        "COMPLEX_FEATURE",
        "CONTROLLER_FEATURE",
        "DIE_FEATURE",
        "ELECTRICAL_FEATURE",
        "FASTENER_FEATURE",
        "FILTER_FEATURE",
        "FITTING_FEATURE",
        "FIXTURE_FEATURE",
        "FORGING_FEATURE",
        "GEAR_FEATURE",
        "HANDLING_FEATURE",
        "HEATING_FEATURE",
        "HOUSING_FEATURE",
        "HYDRAULIC_FEATURE",
        "INTERFACE_FEATURE",
        "JIG_FEATURE",
        "LABEL_FEATURE",
        "PAINTING_FEATURE",
        "SAFETY_FEATURE",
        "SENSOR_FEATURE",
        "SPRING_FEATURE",
        "VALVE_FEATURE");
    // Phase 7: Additional manufacturing features and operations
    registerShapeAspectAliases(
        registry,
        "COOLING_FEATURE",
        "LOCATOR_FEATURE",
        "LUBRICATION_FEATURE",
        "MARKING_FEATURE",
        "MODIFY_FEATURE",
        "PLATING_FEATURE",
        "PNEUMATIC_FEATURE",
        "ROBOT_FEATURE",
        "SHAFT_FEATURE",
        "STORAGE_FEATURE",
        "STRUCTURAL_FEATURE",
        "TRANSPORT_FEATURE");
    // Phase 7: Dimension representation aliases
    registerRepresentationAliases(
        registry,
        true,
        "ANGULAR_DIMENSION_REPRESENTATION",
        "CHAIN_DIMENSION_REPRESENTATION",
        "LINEAR_DIMENSION_REPRESENTATION",
        "MANUFACTURING_FEATURE_REPRESENTATION",
        "ORDINATE_DIMENSION_REPRESENTATION",
        "PROCESS_PLAN_REPRESENTATION",
        "COATING_REPRESENTATION_ITEM",
        "HARDNESS_REPRESENTATION_ITEM",
        "HEAT_TREATMENT_REPRESENTATION_ITEM");
    // Phase 2 extended: Additional representation type aliases
    registerRepresentationAliases(
        registry,
        true,
        "ADVANCED_FACE_SHAPE_REPRESENTATION",
        "ANNOTATION_SHAPE_REPRESENTATION",
        "AUXILIARY_GEOMETRIC_REPRESENTATION",
        "BEND_SHAPE_REPRESENTATION",
        "BLANK_SHAPE_REPRESENTATION",
        "CABLE_SHAPE_REPRESENTATION",
        "CARRIER_SHAPE_REPRESENTATION",
        "CUTOUT_SHAPE_REPRESENTATION",
        "DEFINITIONAL_SHAPE_REPRESENTATION",
        "DIE_SHAPE_REPRESENTATION",
        "DRAWING_SHAPE_REPRESENTATION",
        "ELECTRICAL_SHAPE_REPRESENTATION",
        "EXPLICIT_SHAPE_REPRESENTATION",
        "EXTRUSION_SHAPE_REPRESENTATION",
        "FASTENER_SHAPE_REPRESENTATION",
        "FITTING_SHAPE_REPRESENTATION",
        "FLAT_PATTERN_SHAPE_REPRESENTATION",
        "FRAME_SHAPE_REPRESENTATION",
        "HOLE_SHAPE_REPRESENTATION",
        "INTERCONNECT_SHAPE_REPRESENTATION",
        "JOINT_SHAPE_REPRESENTATION",
        "LAMINATE_SHAPE_REPRESENTATION",
        "LIBRARY_SHAPE_REPRESENTATION",
        "MACHINED_SHAPE_REPRESENTATION",
        "MOLD_SHAPE_REPRESENTATION",
        "MOUNTED_SHAPE_REPRESENTATION",
        "PACKAGE_SHAPE_REPRESENTATION",
        "PANEL_SHAPE_REPRESENTATION",
        "PART_SHAPE_REPRESENTATION",
        "PATTERN_SHAPE_REPRESENTATION",
        "PIPELINE_SHAPE_REPRESENTATION",
        "PRINTED_SHAPE_REPRESENTATION",
        "PROCESS_SHAPE_REPRESENTATION",
        "PRODUCT_SHAPE_REPRESENTATION",
        "REFERENCE_SHAPE_REPRESENTATION",
        "REINFORCEMENT_SHAPE_REPRESENTATION",
        "RIVET_SHAPE_REPRESENTATION",
        "ROUTE_SHAPE_REPRESENTATION",
        "SECTION_SHAPE_REPRESENTATION",
        "SHEET_METAL_SHAPE_REPRESENTATION",
        "SHELL_SHAPE_REPRESENTATION",
        "SLOT_SHAPE_REPRESENTATION",
        "STAMPED_SHAPE_REPRESENTATION",
        "STANDARD_SHAPE_REPRESENTATION",
        "STRUCTURAL_SHAPE_REPRESENTATION",
        "SURFACE_FINISH_SHAPE_REPRESENTATION",
        "TABULATION_SHAPE_REPRESENTATION",
        "THREAD_SHAPE_REPRESENTATION",
        "TOLERANCE_SHAPE_REPRESENTATION",
        "TOOL_SHAPE_REPRESENTATION",
        "TRANSFORMATION_SHAPE_REPRESENTATION",
        "UNFOLD_SHAPE_REPRESENTATION",
        "VALIDATION_SHAPE_REPRESENTATION",
        "VARIANT_SHAPE_REPRESENTATION",
        "WELD_SHAPE_REPRESENTATION",
        "WIRE_SHAPE_REPRESENTATION",
        "ZONE_SHAPE_REPRESENTATION");
    // Phase 2 extended: Additional representation relationship aliases
    registerRepresentationRelationshipAliases(
        registry,
        "ANNOTATION_RELATIONSHIP",
        "ASSEMBLY_RELATIONSHIP",
        "BREAKDOWN_RELATIONSHIP",
        "CAD_MODEL_RELATIONSHIP",
        "CATALOG_RELATIONSHIP",
        "CONFIGURATION_RELATIONSHIP",
        "DEFINITION_RELATIONSHIP",
        "DRAWING_RELATIONSHIP",
        "ELECTRICAL_RELATIONSHIP",
        "FEATURE_RELATIONSHIP",
        "GEOMETRY_RELATIONSHIP",
        "INSPECTION_RELATIONSHIP",
        "INTERFACE_RELATIONSHIP",
        "LIBRARY_RELATIONSHIP",
        "LOGISTIC_RELATIONSHIP",
        "MATERIAL_RELATIONSHIP",
        "MECHANICAL_RELATIONSHIP",
        "PACKAGE_RELATIONSHIP",
        "PART_RELATIONSHIP",
        "PROCESS_RELATIONSHIP",
        "QUALITY_RELATIONSHIP",
        "REFERENCE_RELATIONSHIP",
        "REQUIREMENT_RELATIONSHIP",
        "SHAPE_DEFINITION_RELATIONSHIP",
        "STRUCTURE_RELATIONSHIP",
        "TEST_RELATIONSHIP",
        "TOOL_RELATIONSHIP",
        "VERSION_RELATIONSHIP",
        "WIRE_RELATIONSHIP",
        "ZONE_RELATIONSHIP");
    // Phase 2 extended: Additional characterized object aliases
    registerCharacterizedObjectAliases(
        registry,
        "ADJUSTMENT_OPERATION",
        "ASSEMBLY_OPERATION",
        "CALIBRATION_OPERATION",
        "CLEANING_OPERATION",
        "COATING_OPERATION",
        "DISASSEMBLY_OPERATION",
        "FINISHING_OPERATION",
        "HEAT_TREATMENT_OPERATION",
        "INSPECTION_OPERATION",
        "INSTALLATION_OPERATION",
        "JOINING_OPERATION",
        "MACHINING_OPERATION_SEQUENCE",
        "MAINTENANCE_OPERATION",
        "MARKING_OPERATION",
        "MOLDING_OPERATION",
        "PACKAGING_OPERATION",
        "POLISHING_OPERATION",
        "PRESSING_OPERATION",
        "RESTORATION_OPERATION",
        "STAMPING_OPERATION",
        "TESTING_OPERATION",
        "TREATMENT_OPERATION",
        "WELDING_OPERATION",
        "ADHESIVE_BOND_FEATURE",
        "BEND_FEATURE",
        "CLINCH_FEATURE",
        "CO_EXTRUDED_FEATURE",
        "CRIMP_FEATURE",
        "DRAWN_FEATURE",
        "EMBOSSED_FEATURE",
        "ENGRAVED_FEATURE",
        "ETCHED_FEATURE",
        "FLATTENED_FEATURE",
        "FOLDED_FEATURE",
        "HEM_FEATURE",
        "JOG_FEATURE",
        "LANCED_FEATURE",
        "LASER_CUT_FEATURE",
        "MILLED_FEATURE",
        "NOTCHED_FEATURE",
        "PIERCED_FEATURE",
        "PLASMA_CUT_FEATURE",
        "PRESSED_FEATURE",
        "PUNCHED_FEATURE",
        "ROLLED_FEATURE",
        "SEAMED_FEATURE",
        "SHEARED_FEATURE",
        "SLIT_FEATURE",
        "SPUN_FEATURE",
        "STAKED_FEATURE",
        "STAMPED_FEATURE",
        "SWAGED_FEATURE",
        "TAPPED_FEATURE",
        "TURNED_FEATURE",
        "WATER_CUT_FEATURE",
        "WELDED_FEATURE");
    // Phase 2 extended: Additional externally defined item aliases
    registerExternallyDefinedItemAliases(
        registry,
        "EXTERNALLY_DEFINED_CAD_MODEL",
        "EXTERNALLY_DEFINED_CALCULATION",
        "EXTERNALLY_DEFINED_CONFIGURATION",
        "EXTERNALLY_DEFINED_CONSTRAINT",
        "EXTERNALLY_DEFINED_DOCUMENT",
        "EXTERNALLY_DEFINED_DRAWING",
        "EXTERNALLY_DEFINED_FEATURE",
        "EXTERNALLY_DEFINED_FINISH",
        "EXTERNALLY_DEFINED_GEOMETRY",
        "EXTERNALLY_DEFINED_INSPECTION",
        "EXTERNALLY_DEFINED_INTERFACE",
        "EXTERNALLY_DEFINED_LIBRARY",
        "EXTERNALLY_DEFINED_MATERIAL",
        "EXTERNALLY_DEFINED_MODEL",
        "EXTERNALLY_DEFINED_PART",
        "EXTERNALLY_DEFINED_PROCESS",
        "EXTERNALLY_DEFINED_PRODUCT",
        "EXTERNALLY_DEFINED_QUALITY",
        "EXTERNALLY_DEFINED_REFERENCE",
        "EXTERNALLY_DEFINED_REQUIREMENT",
        "EXTERNALLY_DEFINED_SHAPE",
        "EXTERNALLY_DEFINED_STANDARD",
        "EXTERNALLY_DEFINED_TEST",
        "EXTERNALLY_DEFINED_TOOL",
        "EXTERNALLY_DEFINED_VERSION",
        "EXTERNALLY_DEFINED_WIRE",
        "EXTERNALLY_DEFINED_ZONE");
    // Phase 3: Additional tessellation entities
    registry.put(
        "TRIANGULATED_FACE",
        (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));
    registry.put(
        "POLYGONAL_FACE",
        (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));
    registry.put(
        "TESSELLATED_SHELL",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "TESSELLATED_SOLID",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "TESSELLATED_CURVE",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "TESSELLATED_POINT_SET",
        (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));
    // Phase 3: Additional tolerance entities
    registry.put(
        "GEOMETRIC_TOLERANCE_WITH_DATUM_REFERENCE",
        (resolver, instance) -> resolver.resolveGeometricTolerance(instance, "GEOMETRIC_TOLERANCE_WITH_DATUM_REFERENCE"));
    registry.put(
        "PROJECTED_TOLERANCE_ZONE",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "PROJECTED_TOLERANCE_ZONE"));
    // Phase 3: Additional product definition entities
    registry.put(
        "MAKE_FROM_OPTION",
        (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "MAKE_FROM_OPTION"));
    registry.put(
        "AREA_IN_SET",
        (resolver, instance) -> resolver.resolveRepresentationRelationship(instance, "AREA_IN_SET"));
    registry.put(
        "ITEM_ASSOCIATED_DIMENSION",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "ITEM_ASSOCIATED_DIMENSION"));
    registry.put(
        "DIMENSION_PAIR",
        (resolver, instance) -> resolver.resolveShapeAspectRelationship(instance, "DIMENSION_PAIR"));
    registry.put(
        "DIMENSIONAL_CHARACTERISTIC_REPRESENTATION",
        (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));
    // Phase 3: Additional curve entities
    registry.put(
        "REPARAMETRISED_COMPOSITE_CURVE_SEGMENT",
        (resolver, instance) -> resolver.resolveCompositeCurveSegment(instance));
    registry.put(
        "B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS_CURVE",
        (resolver, instance) -> resolver.resolveBSplineCurveWithKnots(instance));
    // Phase 3: Additional external definition entities
    registry.put(
        "REPUBLICATION",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "EXTERNALLY_DEFINED_CURVE_FONT",
        (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_CURVE_FONT"));
    registry.put(
        "EXTERNALLY_DEFINED_HATCH_STYLE",
        (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_HATCH_STYLE"));
    registry.put(
        "EXTERNALLY_DEFINED_SYMBOL",
        (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_SYMBOL"));
    registry.put(
        "EXTERNALLY_DEFINED_TEXT_FONT",
        (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_TEXT_FONT"));
    registry.put(
        "EXTERNALLY_DEFINED_TILE_STYLE",
        (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "EXTERNALLY_DEFINED_TILE_STYLE"));
    // Phase 3: Additional draughting entities
    registry.put(
        "INSET_CALLOUT",
        (resolver, instance) -> resolver.resolveDraughtingCallout(instance, "INSET_CALLOUT"));
    registry.put(
        "VALUE_FORMAT",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "VALUE_FORMAT_TYPE",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "GLOBAL_CLOCK",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    // Phase 3: Additional property entities
    registry.put(
        "ACTION_PROPERTY",
        (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
    registry.put(
        "GENERAL_PROPERTY_ASSOCINATION",
        (resolver, instance) -> resolver.resolveGeneralPropertyRelationship(instance));
    registry.put(
        "GENERAL_PROPERTY_DEFINITION",
        (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
    registry.put(
        "FEATURE_COMPONENT_DEFINITION",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "FEATURE_COMPONENT_DEFINITION"));
    registry.put(
        "DERIVED_SHAPE_ASPECT",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "DERIVED_SHAPE_ASPECT"));
    registry.put(
        "APPLIED_SHAPE_ASPECT_ASSIGNMENT",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "APPLIED_SHAPE_ASPECT_ASSIGNMENT"));
    // Phase 3: Additional solid entities
    registry.put(
        "SWEPT_AREA_SOLID",
        (resolver, instance) -> resolver.resolveExtrudedAreaSolid(instance));
    registry.put(
        "SWEPT_VOLUME_SOLID",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "SHELL",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "AREA_SOLID",
        (resolver, instance) -> resolver.resolveExtrudedAreaSolid(instance));
    registry.put(
        "GEOMETRIC_REPRESENTATION_ITEM_WITH_GEOMETRY",
        (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));
    registry.put(
        "SHAPE_REPRESENTATION_WITH_PARAMETERS",
        (resolver, instance) -> resolver.resolveRepresentation(instance, "SHAPE_REPRESENTATION_WITH_PARAMETERS", true));
    registry.put(
        "REPRESENTATION_WITH_PARAMETERS",
        (resolver, instance) -> resolver.resolveRepresentation(instance, "REPRESENTATION_WITH_PARAMETERS", false));
    registry.put(
        "VOID_SOLID",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    // Phase 3: Replica entities (already handled via geometric replica resolver)
    registry.put(
        "REPLICA_GEOMETRY",
        (resolver, instance) -> resolver.resolveGeometricReplica(instance, "REPLICA_GEOMETRY"));
    registry.put(
        "GEOMETRIC_REPLICA",
        (resolver, instance) -> resolver.resolveGeometricReplica(instance, "GEOMETRIC_REPLICA"));
    // Phase 3: BREP variants
    registry.put(
        "BREP",
        (resolver, instance) -> resolver.resolveManifoldSolidBrep(instance, "BREP"));
    // Phase 3: Additional representation entities
    registry.put(
        "ADVANCED_FACE_REPRESENTATION",
        (resolver, instance) -> resolver.resolveRepresentation(instance, "ADVANCED_FACE_REPRESENTATION", true));
    registry.put(
        "FACE_REPRESENTATION",
        (resolver, instance) -> resolver.resolveRepresentation(instance, "FACE_REPRESENTATION", true));
    registry.put(
        "EDGE_REPRESENTATION",
        (resolver, instance) -> resolver.resolveRepresentation(instance, "EDGE_REPRESENTATION", true));
    registry.put(
        "VERTEX_REPRESENTATION",
        (resolver, instance) -> resolver.resolveRepresentation(instance, "VERTEX_REPRESENTATION", true));
    registry.put(
        "LOOP_REPRESENTATION",
        (resolver, instance) -> resolver.resolveRepresentation(instance, "LOOP_REPRESENTATION", true));
    registry.put(
        "SHELL_REPRESENTATION",
        (resolver, instance) -> resolver.resolveRepresentation(instance, "SHELL_REPRESENTATION", true));
    // Phase 4: Extended geometric tolerance types
    registerGeometricToleranceAliases(
        registry,
        "ANGULARITY_TOLERANCE_WITH_DATUM_REFERENCE",
        "CIRCULARITY_TOLERANCE_WITH_DATUM_REFERENCE",
        "CONCENTRICITY_TOLERANCE_WITH_DATUM_REFERENCE",
        "CYLINDRICITY_TOLERANCE_WITH_DATUM_REFERENCE",
        "FLATNESS_TOLERANCE_WITH_DATUM_REFERENCE",
        "PARALLELISM_TOLERANCE_WITH_DATUM_REFERENCE",
        "PERPENDICULARITY_TOLERANCE_WITH_DATUM_REFERENCE",
        "POSITION_TOLERANCE_WITH_DATUM_REFERENCE",
        "PROFILE_OF_A_LINE_TOLERANCE_WITH_DATUM_REFERENCE",
        "PROFILE_OF_A_SURFACE_TOLERANCE_WITH_DATUM_REFERENCE",
        "RUNOUT_TOLERANCE_WITH_DATUM_REFERENCE",
        "STRAIGHTNESS_TOLERANCE_WITH_DATUM_REFERENCE",
        "SYMMETRY_TOLERANCE_WITH_DATUM_REFERENCE",
        "TOTAL_RUNOUT_TOLERANCE_WITH_DATUM_REFERENCE");
    // Phase 4: Extended shape aspect relationship aliases
    registerShapeAspectRelationshipAliases(
        registry,
        "ASSEMBLY_FEATURE_RELATIONSHIP",
        "COMPONENT_FEATURE_RELATIONSHIP",
        "DATUM_RELATIONSHIP",
        "FEATURE_CHAIN_RELATIONSHIP",
        "GEOMETRIC_TOLERANCE_RELATIONSHIP",
        "MATING_FEATURE_RELATIONSHIP",
        "MOUNTING_FEATURE_RELATIONSHIP",
        "PART_FEATURE_RELATIONSHIP",
        "PROCESS_FEATURE_RELATIONSHIP",
        "REFERENCE_FEATURE_RELATIONSHIP",
        "TOLERANCE_CHAIN_RELATIONSHIP",
        "WELD_FEATURE_RELATIONSHIP");
    // Phase 4: Extended representation aliases
    registerRepresentationAliases(
        registry,
        true,
        "ADVANCED_SURFACE_SHAPE_REPRESENTATION",
        "ASSEMBLY_FEATURE_SHAPE_REPRESENTATION",
        "BEND_AREA_SHAPE_REPRESENTATION",
        "BLANKING_SHAPE_REPRESENTATION",
        "BOLTING_SHAPE_REPRESENTATION",
        "BONDING_SHAPE_REPRESENTATION",
        "CASTING_SHAPE_REPRESENTATION",
        "COATING_SHAPE_REPRESENTATION",
        "COMPOSITE_MATERIAL_SHAPE_REPRESENTATION",
        "COMPONENT_MOUNTING_SHAPE_REPRESENTATION",
        "CONNECTION_SHAPE_REPRESENTATION",
        "CORE_SHAPE_REPRESENTATION",
        "CUTTING_SHAPE_REPRESENTATION",
        "DEFINITION_FEATURE_SHAPE_REPRESENTATION",
        "DRILLING_SHAPE_REPRESENTATION",
        "EDGE_FINISH_SHAPE_REPRESENTATION",
        "ELECTRICAL_CONNECTION_SHAPE_REPRESENTATION",
        "FASTENING_SHAPE_REPRESENTATION",
        "FINISHING_SHAPE_REPRESENTATION",
        "FORGING_SHAPE_REPRESENTATION",
        "GRINDING_SHAPE_REPRESENTATION",
        "HARDENING_SHAPE_REPRESENTATION",
        "HEATING_SHAPE_REPRESENTATION",
        "HONING_SHAPE_REPRESENTATION",
        "INSERT_SHAPE_REPRESENTATION",
        "JOINING_SHAPE_REPRESENTATION",
        "KEYING_SHAPE_REPRESENTATION",
        "LAPPING_SHAPE_REPRESENTATION",
        "MACHINING_SETUP_SHAPE_REPRESENTATION",
        "MATERIAL_REMOVAL_SHAPE_REPRESENTATION",
        "MEASURING_SHAPE_REPRESENTATION",
        "MILLING_SHAPE_REPRESENTATION",
        "MOLD_CAVITY_SHAPE_REPRESENTATION",
        "NUTTING_SHAPE_REPRESENTATION",
        "OVERMOLDING_SHAPE_REPRESENTATION",
        "PINNING_SHAPE_REPRESENTATION",
        "PLATING_SHAPE_REPRESENTATION",
        "POLISHING_SHAPE_REPRESENTATION",
        "PRESSING_SHAPE_REPRESENTATION",
        "PUNCHING_SHAPE_REPRESENTATION",
        "RIVETING_SHAPE_REPRESENTATION",
        "ROUTING_SHAPE_REPRESENTATION",
        "SAWING_SHAPE_REPRESENTATION",
        "SCREWING_SHAPE_REPRESENTATION",
        "SEALING_SHAPE_REPRESENTATION",
        "SHAPING_SHAPE_REPRESENTATION",
        "SHEARING_SHAPE_REPRESENTATION",
        "SINTERING_SHAPE_REPRESENTATION",
        "SLOTTING_SHAPE_REPRESENTATION",
        "SPINNING_SHAPE_REPRESENTATION",
        "STAMPING_SHAPE_REPRESENTATION",
        "SURFACE_FINISH_SHAPE_REPRESENTATION",
        "TAPPING_SHAPE_REPRESENTATION",
        "TEMPERING_SHAPE_REPRESENTATION",
        "THREADING_SHAPE_REPRESENTATION",
        "TURNING_SHAPE_REPRESENTATION",
        "UNDERCUTTING_SHAPE_REPRESENTATION",
        "WELDING_SHAPE_REPRESENTATION",
        "WIRE_EDM_SHAPE_REPRESENTATION");
    // Phase 4: Extended representation relationship aliases
    registerRepresentationRelationshipAliases(
        registry,
        "ASSEMBLY_FEATURE_RELATIONSHIP",
        "BEND_RELATIONSHIP",
        "CAD_MODEL_TO_PHYSICAL_RELATIONSHIP",
        "COMPONENT_TO_FEATURE_RELATIONSHIP",
        "DEFINITION_TO_INSTANCE_RELATIONSHIP",
        "DESIGN_TO_MANUFACTURING_RELATIONSHIP",
        "ELECTRICAL_CONNECTION_RELATIONSHIP",
        "FEATURE_TO_FEATURE_RELATIONSHIP",
        "FEATURE_TO_PART_RELATIONSHIP",
        "GEOMETRY_TO_FEATURE_RELATIONSHIP",
        "INSPECTION_TO_PRODUCT_RELATIONSHIP",
        "INTERFACE_TO_INTERFACE_RELATIONSHIP",
        "MATERIAL_TO_GEOMETRY_RELATIONSHIP",
        "MOUNTING_TO_FEATURE_RELATIONSHIP",
        "PART_TO_ASSEMBLY_RELATIONSHIP",
        "PART_TO_FEATURE_RELATIONSHIP",
        "PART_TO_PART_RELATIONSHIP",
        "PROCESS_TO_FEATURE_RELATIONSHIP",
        "REFERENCE_TO_GEOMETRY_RELATIONSHIP",
        "SHAPE_TO_FEATURE_RELATIONSHIP",
        "SHAPE_TO_SHAPE_RELATIONSHIP",
        "TOOL_TO_PART_RELATIONSHIP",
        "WELD_TO_PART_RELATIONSHIP");
    // Phase 4: Extended characterized object aliases
    registerCharacterizedObjectAliases(
        registry,
        "ALIGNMENT_FEATURE",
        "ANCHORING_FEATURE",
        "AUXILIARY_FEATURE",
        "BASE_FEATURE",
        "BENDING_FEATURE",
        "BINDING_FEATURE",
        "BONDING_FEATURE",
        "BRAKING_FEATURE",
        "BRACING_FEATURE",
        "BRACKET_FEATURE",
        "BUSHING_FEATURE",
        "CAM_FEATURE",
        "CENTERING_FEATURE",
        "CHUCKING_FEATURE",
        "CLAMPING_FEATURE",
        "CLEARANCE_FEATURE",
        "CLIPPING_FEATURE",
        "CLOSING_FEATURE",
        "COATING_FEATURE",
        "COUPLING_FEATURE",
        "COVERING_FEATURE",
        "CUSHIONING_FEATURE",
        "CUTTING_FEATURE",
        "DAMPING_FEATURE",
        "DETENT_FEATURE",
        "DISPENSING_FEATURE",
        "DIVERTING_FEATURE",
        "DOCKING_FEATURE",
        "DRIVING_FEATURE",
        "EJECTING_FEATURE",
        "ENCLOSING_FEATURE",
        "ENGAGING_FEATURE",
        "FILLING_FEATURE",
        "FILTERING_FEATURE",
        "FITTING_FEATURE",
        "FIXING_FEATURE",
        "FLUID_HANDLING_FEATURE",
        "GAGING_FEATURE",
        "GASKETING_FEATURE",
        "GRIPPING_FEATURE",
        "GUIDING_FEATURE",
        "HOLDING_FEATURE",
        "HOUSING_FEATURE",
        "INDICATING_FEATURE",
        "INSERTING_FEATURE",
        "INSULATING_FEATURE",
        "INTERLOCKING_FEATURE",
        "JOINING_FEATURE",
        "KEYING_FEATURE",
        "LIFTING_FEATURE",
        "LIMITING_FEATURE",
        "LOCATING_FEATURE",
        "LOCKING_FEATURE",
        "MOUNTING_FEATURE",
        "MOVING_FEATURE",
        "NEUTRALIZING_FEATURE",
        "OILING_FEATURE",
        "OPENING_FEATURE",
        "ORIENTING_FEATURE",
        "PAINTING_FEATURE",
        "PRESSURIZING_FEATURE",
        "PROTECTING_FEATURE",
        "PULLING_FEATURE",
        "PUSHING_FEATURE",
        "REGULATING_FEATURE",
        "RELEASING_FEATURE",
        "RETAINING_FEATURE",
        "RETURNING_FEATURE",
        "ROTATING_FEATURE",
        "SEALING_FEATURE",
        "SECURING_FEATURE",
        "SETTING_FEATURE",
        "SHAPING_FEATURE",
        "SHIELDING_FEATURE",
        "SHIFTING_FEATURE",
        "SLIDING_FEATURE",
        "SNAPPING_FEATURE",
        "SOCKETING_FEATURE",
        "SUPPORTING_FEATURE",
        "SUSPENDING_FEATURE",
        "SWITCHING_FEATURE",
        "TENSIONING_FEATURE",
        "THRUSTING_FEATURE",
        "TILTING_FEATURE",
        "TIMING_FEATURE",
        "TOGGLE_FEATURE",
        "TRACKING_FEATURE",
        "TRANSMITTING_FEATURE",
        "TRAPPING_FEATURE",
        "TRIMMING_FEATURE",
        "TURNING_FEATURE",
        "UNLOADING_FEATURE",
        "VALVING_FEATURE",
        "VENTING_FEATURE",
        "VIBRATING_FEATURE",
        "WELDING_FEATURE",
        "WRAPPING_FEATURE");
    // Phase 5: Additional advanced geometry types (already registered, aliases added)
    // Phase 5: Additional profile definitions
    registerShapeAspectAliases(
        registry,
        "CIRCULAR_CLOSED_PROFILE",
        "RECTANGULAR_CLOSED_PROFILE",
        "CLOSED_PATH_PROFILE",
        "OPEN_PATH_PROFILE",
        "NUT_PROFILE",
        "BOLT_PROFILE",
        "SCREW_PROFILE",
        "FASTENER_PROFILE",
        "GASKET_PROFILE",
        "SEAL_PROFILE",
        "O_RING_PROFILE",
        "C_RING_PROFILE",
        "E_RING_PROFILE",
        "U_RING_PROFILE",
        "V_RING_PROFILE",
        "X_RING_PROFILE",
        "WIRE_PROFILE",
        "CABLE_PROFILE",
        "TUBE_PROFILE",
        "PIPE_PROFILE",
        "BEAM_PROFILE",
        "COLUMN_PROFILE",
        "STRUT_PROFILE",
        "BRACE_PROFILE",
        "TRUSS_PROFILE",
        "FRAME_PROFILE",
        "RAIL_PROFILE",
        "TRACK_PROFILE",
        "WHEEL_PROFILE",
        "TIRE_PROFILE",
        "ROLLER_PROFILE",
        "BELT_PROFILE",
        "CHAIN_PROFILE",
        "SPROCKET_PROFILE",
        "GEAR_PROFILE",
        "RACK_PROFILE",
        "PINION_PROFILE",
        "WORM_PROFILE",
        "WHEEL_GEAR_PROFILE",
        "BEVEL_GEAR_PROFILE",
        "HELICAL_GEAR_PROFILE",
        "SPUR_GEAR_PROFILE");
    // Phase 5: Additional tolerance zone types
    registerShapeAspectAliases(
        registry,
        "LINEAR_TOLERANCE_ZONE_DEFINITION",
        "RADIAL_TOLERANCE_ZONE_DEFINITION",
        "ANGULAR_TOLERANCE_ZONE_DEFINITION",
        "AXIAL_TOLERANCE_ZONE_DEFINITION",
        "COAXIAL_TOLERANCE_ZONE_DEFINITION",
        "CONCENTRIC_TOLERANCE_ZONE_DEFINITION",
        "SYMMETRIC_TOLERANCE_ZONE_DEFINITION",
        "POSITIONAL_TOLERANCE_ZONE_DEFINITION",
        "PROFILE_TOLERANCE_ZONE_DEFINITION",
        "RUNOUT_TOLERANCE_ZONE_DEFINITION",
        "TOTAL_RUNOUT_TOLERANCE_ZONE_DEFINITION");
    // Phase 5: Additional measurement representation types
    registerRepresentationAliases(
        registry,
        false,
        "ANGULAR_MEASUREMENT_REPRESENTATION",
        "LINEAR_MEASUREMENT_REPRESENTATION",
        "AREA_MEASUREMENT_REPRESENTATION",
        "VOLUME_MEASUREMENT_REPRESENTATION",
        "MASS_MEASUREMENT_REPRESENTATION",
        "TIME_MEASUREMENT_REPRESENTATION",
        "TEMPERATURE_MEASUREMENT_REPRESENTATION",
        "PRESSURE_MEASUREMENT_REPRESENTATION",
        "FORCE_MEASUREMENT_REPRESENTATION",
        "TORQUE_MEASUREMENT_REPRESENTATION",
        "POWER_MEASUREMENT_REPRESENTATION",
        "ENERGY_MEASUREMENT_REPRESENTATION",
        "SPEED_MEASUREMENT_REPRESENTATION",
        "VELOCITY_MEASUREMENT_REPRESENTATION",
        "ACCELERATION_MEASUREMENT_REPRESENTATION",
        "FREQUENCY_MEASUREMENT_REPRESENTATION",
        "VOLTAGE_MEASUREMENT_REPRESENTATION",
        "CURRENT_MEASUREMENT_REPRESENTATION",
        "RESISTANCE_MEASUREMENT_REPRESENTATION",
        "CAPACITANCE_MEASUREMENT_REPRESENTATION",
        "INDUCTANCE_MEASUREMENT_REPRESENTATION",
        "MAGNETIC_FLUX_MEASUREMENT_REPRESENTATION",
        "LUMINANCE_MEASUREMENT_REPRESENTATION",
        "ILLUMINANCE_MEASUREMENT_REPRESENTATION",
        "RADIATION_MEASUREMENT_REPRESENTATION",
        "SOUND_MEASUREMENT_REPRESENTATION",
        "VIBRATION_MEASUREMENT_REPRESENTATION",
        "ROUGHNESS_MEASUREMENT_REPRESENTATION",
        "FLATNESS_MEASUREMENT_REPRESENTATION",
        "CIRCULARITY_MEASUREMENT_REPRESENTATION",
        "CYLINDRICITY_MEASUREMENT_REPRESENTATION",
        "STRAIGHTNESS_MEASUREMENT_REPRESENTATION",
        "PERPENDICULARITY_MEASUREMENT_REPRESENTATION",
        "PARALLELISM_MEASUREMENT_REPRESENTATION",
        "ANGULARITY_MEASUREMENT_REPRESENTATION",
        "CONCENTRICITY_MEASUREMENT_REPRESENTATION",
        "SYMMETRY_MEASUREMENT_REPRESENTATION",
        "POSITION_MEASUREMENT_REPRESENTATION",
        "PROFILE_MEASUREMENT_REPRESENTATION",
        "RUNOUT_MEASUREMENT_REPRESENTATION");
    // Phase 5: Additional document types
    registry.put(
        "DRAWING_DOCUMENT",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "SPECIFICATION_DOCUMENT",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "TEST_DOCUMENT",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "REPORT_DOCUMENT",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "MANUAL_DOCUMENT",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "PROCEDURE_DOCUMENT",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "STANDARD_DOCUMENT",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "REGULATION_DOCUMENT",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "CONTRACT_DOCUMENT",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "ORDER_DOCUMENT",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "QUOTATION_DOCUMENT",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "INVOICE_DOCUMENT",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "PACKING_LIST_DOCUMENT",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "SHIPPING_DOCUMENT",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "RECEIVING_DOCUMENT",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "INSPECTION_DOCUMENT",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "CERTIFICATION_DOCUMENT",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "WARRANTY_DOCUMENT",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "MAINTENANCE_DOCUMENT",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "REPAIR_DOCUMENT",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "CALIBRATION_DOCUMENT",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "TRAINING_DOCUMENT",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "SAFETY_DOCUMENT",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "ENVIRONMENTAL_DOCUMENT",
        (resolver, instance) -> resolver.resolveDocument(instance));
    // Phase 5: Additional approval and certification types
    registry.put(
        "DESIGN_APPROVAL",
        (resolver, instance) -> resolver.resolveApproval(instance));
    registry.put(
        "MANUFACTURING_APPROVAL",
        (resolver, instance) -> resolver.resolveApproval(instance));
    registry.put(
        "QUALITY_APPROVAL",
        (resolver, instance) -> resolver.resolveApproval(instance));
    registry.put(
        "TESTING_APPROVAL",
        (resolver, instance) -> resolver.resolveApproval(instance));
    registry.put(
        "SHIPPING_APPROVAL",
        (resolver, instance) -> resolver.resolveApproval(instance));
    registry.put(
        "DELIVERY_APPROVAL",
        (resolver, instance) -> resolver.resolveApproval(instance));
    registry.put(
        "DESIGN_CERTIFICATION",
        (resolver, instance) -> resolver.resolveCertification(instance));
    registry.put(
        "MANUFACTURING_CERTIFICATION",
        (resolver, instance) -> resolver.resolveCertification(instance));
    registry.put(
        "QUALITY_CERTIFICATION",
        (resolver, instance) -> resolver.resolveCertification(instance));
    registry.put(
        "TESTING_CERTIFICATION",
        (resolver, instance) -> resolver.resolveCertification(instance));
    registry.put(
        "SAFETY_CERTIFICATION",
        (resolver, instance) -> resolver.resolveCertification(instance));
    registry.put(
        "ENVIRONMENTAL_CERTIFICATION",
        (resolver, instance) -> resolver.resolveCertification(instance));
    // Phase 5: Additional contract types
    registry.put(
        "PURCHASE_CONTRACT",
        (resolver, instance) -> resolver.resolveContract(instance));
    registry.put(
        "SALES_CONTRACT",
        (resolver, instance) -> resolver.resolveContract(instance));
    registry.put(
        "SERVICE_CONTRACT",
        (resolver, instance) -> resolver.resolveContract(instance));
    registry.put(
        "MAINTENANCE_CONTRACT",
        (resolver, instance) -> resolver.resolveContract(instance));
    registry.put(
        "LEASE_CONTRACT",
        (resolver, instance) -> resolver.resolveContract(instance));
    registry.put(
        "LICENSE_CONTRACT",
        (resolver, instance) -> resolver.resolveContract(instance));
    registry.put(
        "WARRANTY_CONTRACT",
        (resolver, instance) -> resolver.resolveContract(instance));
    // Phase 6: AP242 Tessellation extension entities
    registry.put(
        "TRIANGULATED_SURFACE_SET",
        (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));
    registry.put(
        "TESSELLATED_GEOMETRIC_SET",
        (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));
    registry.put(
        "TESSELLATED_STRUCTURED_MESH",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "TESSELLATED_MESH",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "TESSELLATED_MESH_ELEMENTS",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "TESSELLATED_MESH_ELEMENT_SET",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "TESSELLATED_MESH_STRUCTURE",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "TESSELLATED_CELL",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "TESSELLATED_CELL_SET",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "TESSELLATED_CURVE_SET",
        (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));
    registry.put(
        "TESSELLATED_EDGE",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "TESSELLATED_EDGE_SET",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "TESSELLATED_VERTEX",
        (resolver, instance) -> resolver.resolveCartesianPoint(instance));
    registry.put(
        "TESSELLATED_VERTEX_SET",
        (resolver, instance) -> resolver.resolveTessellatedFaceSet(instance));
    registry.put(
        "TESSELLATED_WIREFRAME",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "TESSELLATED_ANNOTATION",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "TESSELLATED_TEXT",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "TESSELLATED_DIMENSION",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "TESSELLATED_SYMBOL",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    // Phase 6: Additional BSpline variants
    registry.put(
        "B_SPLINE_CURVE_UNIFORM",
        (resolver, instance) -> resolver.resolveBSplineCurveWithKnots(instance));
    registry.put(
        "B_SPLINE_CURVE_QUASI_UNIFORM",
        (resolver, instance) -> resolver.resolveBSplineCurveWithKnots(instance));
    registry.put(
        "B_SPLINE_CURVE_BEZIER",
        (resolver, instance) -> resolver.resolveBSplineCurveWithKnots(instance));
    registry.put(
        "B_SPLINE_CURVE_PIECEWISE_BEZIER",
        (resolver, instance) -> resolver.resolveBSplineCurveWithKnots(instance));
    registry.put(
        "B_SPLINE_SURFACE_UNIFORM",
        (resolver, instance) -> resolver.resolveBSplineSurfaceWithKnots(instance));
    registry.put(
        "B_SPLINE_SURFACE_QUASI_UNIFORM",
        (resolver, instance) -> resolver.resolveBSplineSurfaceWithKnots(instance));
    registry.put(
        "B_SPLINE_SURFACE_BEZIER",
        (resolver, instance) -> resolver.resolveBSplineSurfaceWithKnots(instance));
    registry.put(
        "B_SPLINE_SURFACE_PIECEWISE_BEZIER",
        (resolver, instance) -> resolver.resolveBSplineSurfaceWithKnots(instance));
    // Note: UNIFORM_CURVE, QUASI_UNIFORM_CURVE, BEZIER_CURVE, UNIFORM_SURFACE, QUASI_UNIFORM_SURFACE, BEZIER_SURFACE
    // are already correctly registered earlier using resolveUniformCurve, resolveQuasiUniformCurve, etc.
    // Phase 6: Additional geometric representation items
    registry.put(
        "GEOMETRIC_SET_2D",
        (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));
    registry.put(
        "GEOMETRIC_SET_3D",
        (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));
    registry.put(
        "POINT_SET_2D",
        (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));
    registry.put(
        "POINT_SET_3D",
        (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));
    registry.put(
        "CURVE_SET_2D",
        (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));
    registry.put(
        "CURVE_SET_3D",
        (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));
    registry.put(
        "SURFACE_SET",
        (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));
    registry.put(
        "SHELL_SET",
        (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));
    registry.put(
        "SOLID_SET",
        (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));
    registry.put(
        "COMPOUND_SHAPE_REPRESENTATION",
        (resolver, instance) -> resolver.resolveRepresentation(instance, "COMPOUND_SHAPE_REPRESENTATION", true));
    registry.put(
        "MIXED_SHAPE_REPRESENTATION",
        (resolver, instance) -> resolver.resolveRepresentation(instance, "MIXED_SHAPE_REPRESENTATION", true));
    // Note: ANNOTATION_*_OCCURRENCE entities are already correctly registered earlier
    // using resolveAnnotationCurveOccurrence, resolveAnnotationFillAreaOccurrence, etc.
    // Phase 6: Additional annotation text entities
    // Note: DRAUGHTING_PRE_DEFINED_* entities are already correctly registered earlier
    // using resolveDraughtingPreDefinedColour, resolveDraughtingPreDefinedCurveFont, etc.
    registry.put(
        "DRAUGHTING_PRE_DEFINED_DIMENSION_SYMBOL",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "DRAUGHTING_PRE_DEFINED_POINT_SYMBOL",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    // Note: CURVE_STYLE, POINT_STYLE, SURFACE_SIDE_STYLE, SURFACE_STYLE_*, FILL_AREA_STYLE_*
    // are already correctly registered earlier using resolveCurveStyle, resolvePointStyle, etc.
    // Phase 6: Additional product definition and lifecycle entities
    registry.put(
        "PRODUCT_DEFINITION_SHAPE_WITH_ASSOCIATED_ITEMS",
        (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
    registry.put(
        "PRODUCT_DEFINITION_CONTEXT_ASSOCIATION",
        (resolver, instance) -> resolver.resolveProductDefinition(instance));
    registry.put(
        "PRODUCT_DEFINITION_FORMATION_WITH_SPECIFIED_SOURCE",
        (resolver, instance) -> resolver.resolveProductDefinitionFormation(instance));
    registry.put(
        "PRODUCT_DEFINITION_FORMATION_SPECIAL_USAGE",
        (resolver, instance) -> resolver.resolveProductDefinitionFormation(instance));
    registry.put(
        "PRODUCT_DEFINITION_RESOURCE",
        (resolver, instance) -> resolver.resolveProductDefinition(instance));
    // Note: PRODUCT_DEFINITION_SUBSTITUTE is already correctly registered earlier
    // using resolveProductDefinitionRelationshipRelationship (via registerProductDefinitionRelationshipRelationshipAliases)
    registry.put(
        "PRODUCT_DEFINITION_USAGE",
        (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "PRODUCT_DEFINITION_USAGE"));
    registry.put(
        "PRODUCT_DEFINITION_WITH_ASSOCIATED_DOCUMENTS",
        (resolver, instance) -> resolver.resolveProductDefinition(instance));
    registry.put(
        "ASSEMBLY_COMPONENT_USAGE",
        (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "ASSEMBLY_COMPONENT_USAGE"));
    registry.put(
        "PROMISSORY_USAGE_OCCURRENCE",
        (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "PROMISSORY_USAGE_OCCURRENCE"));
    registry.put(
        "QUANTIFIED_ASSEMBLY_COMPONENT_USAGE",
        (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "QUANTIFIED_ASSEMBLY_COMPONENT_USAGE"));
    registry.put(
        "SPECIFIED_HIGHER_USAGE_OCCURRENCE",
        (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "SPECIFIED_HIGHER_USAGE_OCCURRENCE"));
    registry.put(
        "ASSEMBLY_DEFINITION_USAGE",
        (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "ASSEMBLY_DEFINITION_USAGE"));
    registry.put(
        "COMPONENT_DEFINITION_USAGE",
        (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "COMPONENT_DEFINITION_USAGE"));
    // Phase 6: Additional configuration management entities
    registry.put(
        "CONFIGURATION_EFFECTIVITY",
        (resolver, instance) -> resolver.resolveEffectivity(instance));
    registry.put(
        "CONFIGURATION_ITEM_EFFECTIVITY",
        (resolver, instance) -> resolver.resolveEffectivity(instance));
    registry.put(
        "CONFIGURATION_ITEM_HIERARCHICAL_RELATIONSHIP",
        (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "CONFIGURATION_ITEM_HIERARCHICAL_RELATIONSHIP"));
    registry.put(
        "CONFIGURATION_ITEM_REVISION_SEQUENCE",
        (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "CONFIGURATION_ITEM_REVISION_SEQUENCE"));
    registry.put(
        "CONFIGURATION_DESIGN",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "CONFIGURATION_DESIGN_ITEM",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "PRODUCT_CONCEPT",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "PRODUCT_CONCEPT_FEATURE",
        (resolver, instance) -> resolver.resolveShapeAspect(instance, "PRODUCT_CONCEPT_FEATURE"));
    registry.put(
        "PRODUCT_CONCEPT_FEATURE_ASSOCIATION",
        (resolver, instance) -> resolver.resolveShapeAspectRelationship(instance, "PRODUCT_CONCEPT_FEATURE_ASSOCIATION"));
    registry.put(
        "PRODUCT_CONCEPT_FEATURE_CATEGORY",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "PRODUCT_CONCEPT_FEATURE_CATEGORY_USAGE",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "PRODUCT_CONCEPT_RELATIONSHIP",
        (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "PRODUCT_CONCEPT_RELATIONSHIP"));
    // Phase 6: Additional material and property entities (non-duplicate extensions)
    registry.put(
        "MATERIAL_DESIGNATION_CHARACTERIZATION",
        (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
    registry.put(
        "MATERIAL_PROPERTY_DEFINITION",
        (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
    registry.put(
        "MATERIAL_PROPERTY_DEFINITION_REPRESENTATION",
        (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));
    registry.put(
        "MECHANICAL_PROPERTY",
        (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
    registry.put(
        "MECHANICAL_PROPERTY_REPRESENTATION",
        (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));
    registry.put(
        "THERMAL_PROPERTY",
        (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
    registry.put(
        "THERMAL_PROPERTY_REPRESENTATION",
        (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));
    registry.put(
        "ELECTRICAL_PROPERTY",
        (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
    registry.put(
        "ELECTRICAL_PROPERTY_REPRESENTATION",
        (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));
    registry.put(
        "OPTICAL_PROPERTY",
        (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
    registry.put(
        "OPTICAL_PROPERTY_REPRESENTATION",
        (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));
    registry.put(
        "MAGNETIC_PROPERTY",
        (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
    registry.put(
        "MAGNETIC_PROPERTY_REPRESENTATION",
        (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));
    registry.put(
        "ACOUSTIC_PROPERTY",
        (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
    registry.put(
        "ACOUSTIC_PROPERTY_REPRESENTATION",
        (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));
    registry.put(
        "RADIATION_PROPERTY",
        (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
    registry.put(
        "RADIATION_PROPERTY_REPRESENTATION",
        (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));
    registry.put(
        "CHEMICAL_PROPERTY",
        (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
    registry.put(
        "CHEMICAL_PROPERTY_REPRESENTATION",
        (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));
    registry.put(
        "ENVIRONMENTAL_PROPERTY",
        (resolver, instance) -> resolver.resolvePropertyDefinition(instance));
    registry.put(
        "ENVIRONMENTAL_PROPERTY_REPRESENTATION",
        (resolver, instance) -> resolver.resolvePropertyDefinitionRepresentation(instance));
    // Phase 6: Additional security and classification entities
    // Note: SECURITY_CLASSIFICATION, SECURITY_CLASSIFICATION_LEVEL, APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT
    // are already correctly registered earlier using resolveSecurityClassification, etc.
    // Note: CLASSIFICATION_ASSIGNMENT, APPLIED_CLASSIFICATION_ASSIGNMENT, CLASSIFICATION_ROLE
    // are already correctly registered earlier using resolveClassificationAssignment, etc.
    // Phase 6: Additional organizational entities
    registry.put(
        "ORGANIZATION_TYPE",
        (resolver, instance) -> resolver.resolveOrganization(instance));
    registry.put(
        "ORGANIZATION_RELATIONSHIP",
        (resolver, instance) -> resolver.resolveOrganizationRelationship(instance));
    registry.put(
        "ORGANIZATION_ADDRESS_ASSIGNMENT",
        (resolver, instance) -> resolver.resolveAddress(instance));
    registry.put(
        "PERSON_ADDRESS_ASSIGNMENT",
        (resolver, instance) -> resolver.resolveAddress(instance));
    registry.put(
        "PERSON_ORGANIZATION_ASSIGNMENT",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    // Note: APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT is already correctly registered earlier
    // using resolveAppliedPersonAndOrganizationAssignment
    // Note: PERSON_AND_ORGANIZATION_ROLE, ORGANIZATION_ROLE, PERSON_ROLE
    // are already correctly registered earlier using resolvePersonAndOrganizationRole, etc.
    // Phase 6: Additional date and time entities (non-duplicate extensions)
    registry.put(
        "ORDINAL_DATE",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "WEEK_OF_YEAR_AND_DAY_DATE",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    // Note: APPLIED_DATE_ASSIGNMENT, APPLIED_DATE_TIME_ASSIGNMENT are already correctly registered
    // earlier using resolveAppliedDateAssignment, resolveAppliedDateTimeAssignment, etc.
    // Phase 6: Additional relationship and reference entities
    // Note: DOCUMENT_USAGE_CONSTRAINT is already correctly registered earlier
    // using resolveDocumentUsageConstraint
    registry.put(
        "DOCUMENT_FILE",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "EXTERNAL_FILE",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "EXTERNAL_FILE_RELATIONSHIP",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "DIGITAL_FILE",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "HARDCOPY_FILE",
        (resolver, instance) -> resolver.resolveDocument(instance));
    registry.put(
        "FILE_RELATIONSHIP",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    // Note: APPLIED_DOCUMENT_REFERENCE is already correctly registered earlier
    // using resolveAppliedDocumentReference
    registry.put(
        "APPLIED_DOCUMENT_USAGE_CONSTRAINT_ASSIGNMENT",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "APPLIED_EXTERNAL_DOCUMENT_ASSIGNMENT",
        (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "APPLIED_EXTERNAL_DOCUMENT_ASSIGNMENT"));
    // Phase 6: Additional action and process entities
    registry.put(
        "ACTION_REQUEST_SOLUTION",
        (resolver, instance) -> resolver.resolveAction(instance));
    registry.put(
        "ACTION_METHOD",
        (resolver, instance) -> resolver.resolveAction(instance));
    registry.put(
        "ACTION_METHOD_RELATIONSHIP",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "ACTION_RELATIONSHIP",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "ACTION_STATUS",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "ACTION_ASSIGNMENT",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "APPLIED_ACTION_ASSIGNMENT",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "ACTION_REQUEST_ASSIGNMENT",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "APPLIED_ACTION_REQUEST_ASSIGNMENT",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "ACTION_METHOD_ROLE",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    // Note: ACTION_PROPERTY_REPRESENTATION is already correctly registered earlier
    // using resolveActionPropertyRepresentation
    // Phase 6: Additional requirement and verification entities
    registry.put(
        "REQUIREMENT_ASSIGNMENT",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "APPLIED_REQUIREMENT_ASSIGNMENT",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "REQUIREMENT_VIEW_DEFINITION_RELATIONSHIP",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "REQUIREMENT_SPECIFICATION",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "REQUIREMENT_DEFINITION",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "VERIFICATION",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "VERIFICATION_RELATIONSHIP",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    // Note: CERTIFICATION_ASSIGNMENT, APPLIED_CERTIFICATION_ASSIGNMENT are already correctly registered
    // earlier using resolveCertificationAssignment, resolveAppliedCertificationAssignment, etc.
    // Phase 6: Additional measure and unit entities (non-duplicate extensions)
    registry.put(
        "SI_BASE_UNIT",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "SI_DERIVED_UNIT",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "SI_DERIVED_UNIT_ELEMENT",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "CONVERSION_BASED_UNIT_AND_RATIO_UNIT",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    // Note: *_MEASURE_WITH_UNIT entities are already correctly registered earlier
    // using resolveTypedMeasureWithUnit
    registry.put(
        "PARAMETER_VALUE",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    // Phase 6: Additional identification entities (non-duplicate extensions)
    // Note: IDENTIFICATION_ASSIGNMENT, APPLIED_IDENTIFICATION_ASSIGNMENT, EXTERNAL_IDENTIFICATION_ASSIGNMENT,
    // APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT are already correctly registered earlier
    // using resolveIdentificationAssignment, resolveExternalIdentificationAssignment, etc.
    // Phase 6: Additional context and framework entities (non-duplicate extensions)
    registry.put(
        "PRODUCT_RELATED_PRODUCT_CATEGORY",
        (resolver, instance) -> resolver.resolveProductRelatedProductCategory(instance));
    registry.put(
        "PRODUCT_CATEGORY_RELATIONSHIP",
        (resolver, instance) -> resolver.resolveProductCategoryRelationship(instance));
    // Phase 6: Additional model geometry entities
    registry.put(
        "GEOMETRIC_MODEL",
        (resolver, instance) -> resolver.resolveRepresentation(instance, "GEOMETRIC_MODEL", true));
    registry.put(
        "GEOMETRIC_MODEL_ELEMENT",
        (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));
    registry.put(
        "AXIS_PLACEMENT",
        (resolver, instance) -> resolver.resolveAxis2Placement3D(instance));
    registry.put(
        "AXIS_PLACEMENT_2D",
        (resolver, instance) -> resolver.resolveAxis2Placement2D(instance));
    registry.put(
        "AXIS_PLACEMENT_3D",
        (resolver, instance) -> resolver.resolveAxis2Placement3D(instance));
    registry.put(
        "PLACEMENT_1D",
        (resolver, instance) -> resolver.resolveAxis1Placement(instance));
    registry.put(
        "PLACEMENT_2D",
        (resolver, instance) -> resolver.resolveAxis2Placement2D(instance));
    registry.put(
        "PLACEMENT_3D",
        (resolver, instance) -> resolver.resolveAxis2Placement3D(instance));
    // Phase 6: Additional transformation and mapping entities
    registry.put(
        "ITEM_DEFINED_TRANSFORMATION",
        (resolver, instance) -> resolver.resolveItemDefinedTransformation(instance));
    registry.put(
        "REPRESENTATION_MAP",
        (resolver, instance) -> resolver.resolveRepresentationMap(instance));
    registry.put(
        "MAPPED_ITEM",
        (resolver, instance) -> resolver.resolveMappedItem(instance));
    registry.put(
        "SHAPE_REPRESENTATION_MAP",
        (resolver, instance) -> resolver.resolveRepresentationMap(instance));
    registry.put(
        "GEOMETRIC_REPRESENTATION_MAP",
        (resolver, instance) -> resolver.resolveRepresentationMap(instance));
    // Phase 6: Additional analysis and simulation entities
    registry.put(
        "ANALYSIS_REPRESENTATION",
        (resolver, instance) -> resolver.resolveRepresentation(instance, "ANALYSIS_REPRESENTATION", false));
    // Note: ANALYSIS_MODEL is already correctly registered earlier
    // using resolveRepresentation(instance, "ANALYSIS_MODEL", false)
    registry.put(
        "FEA_MODEL",
        (resolver, instance) -> resolver.resolveRepresentation(instance, "FEA_MODEL", false));
    registry.put(
        "FEA_MODEL_DEFINITION",
        (resolver, instance) -> resolver.resolveRepresentation(instance, "FEA_MODEL_DEFINITION", false));
    registry.put(
        "FEA_MODEL_3D",
        (resolver, instance) -> resolver.resolveRepresentation(instance, "FEA_MODEL_3D", true));
    registry.put(
        "FEA_MODEL_2D",
        (resolver, instance) -> resolver.resolveRepresentation(instance, "FEA_MODEL_2D", true));
    registry.put(
        "FEA_AXIS2_PLACEMENT_3D",
        (resolver, instance) -> resolver.resolveAxis2Placement3D(instance));
    registry.put(
        "FEA_LINEAR_ALGEBRA_MATRIX",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "FEA_LINEAR_ALGEBRA_MATRIX_3D",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "CURVE_ELEMENT_FREEDOM",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "CURVE_ELEMENT_FREEDOM_VALUE",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "SURFACE_ELEMENT_FREEDOM",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "SURFACE_ELEMENT_FREEDOM_VALUE",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "VOLUME_ELEMENT_FREEDOM",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "VOLUME_ELEMENT_FREEDOM_VALUE",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    // Phase 6: Additional assembly and structure entities
    registry.put(
        "ASSEMBLY_SHAPE_REPRESENTATION",
        (resolver, instance) -> resolver.resolveRepresentation(instance, "ASSEMBLY_SHAPE_REPRESENTATION", true));
    registry.put(
        "ASSEMBLY_SHAPE_REPRESENTATION_PREDEFINED",
        (resolver, instance) -> resolver.resolveRepresentation(instance, "ASSEMBLY_SHAPE_REPRESENTATION_PREDEFINED", true));
    registry.put(
        "ASSEMBLY_COMPONENT_STRUCTURE",
        (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "ASSEMBLY_COMPONENT_STRUCTURE"));
    registry.put(
        "ASSEMBLY_SEQUENCE_DEFINITION",
        (resolver, instance) -> resolver.resolveRepresentation(instance, "ASSEMBLY_SEQUENCE_DEFINITION", false));
    registry.put(
        "ASSEMBLY_SEQUENCE",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "ASSEMBLY_STEP",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    // Phase 6: Additional kinematic entities
    registry.put(
        "KINEMATIC_REPRESENTATION",
        (resolver, instance) -> resolver.resolveRepresentation(instance, "KINEMATIC_REPRESENTATION", false));
    registry.put(
        "KINEMATIC_REPRESENTATION_CONTEXT",
        (resolver, instance) -> resolver.resolveRepresentation(instance, "KINEMATIC_REPRESENTATION_CONTEXT", false));
    // Note: KINEMATIC_LINK_REPRESENTATION is already correctly registered earlier
    // using resolveRepresentation(instance, "KINEMATIC_LINK_REPRESENTATION", false)
    registry.put(
        "KINEMATIC_LINK_REPRESENTATION_RELATIONSHIP",
        (resolver, instance) -> resolver.resolveRepresentationRelationship(instance, "KINEMATIC_LINK_REPRESENTATION_RELATIONSHIP"));
    registry.put(
        "KINEMATIC_PATH",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    registry.put(
        "KINEMATIC_JOINT",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    // Note: MECHANISM_REPRESENTATION is already correctly registered earlier
    // using resolveRepresentation(instance, "MECHANISM_REPRESENTATION", false)
    registry.put(
        "MECHANISM",
        (resolver, instance) -> resolver.resolveRepresentationItem(instance));
    // Phase 6: Extended shape representation aliases (final batch)
    registerRepresentationAliases(
        registry,
        true,
        "ANALYSIS_SHAPE_REPRESENTATION",
        "ANIMATION_SHAPE_REPRESENTATION",
        "APPEARANCE_REPRESENTATION",
        "ASSEMBLY_DEFINITION_SHAPE_REPRESENTATION",
        "ASSEMBLY_PROCESS_SHAPE_REPRESENTATION",
        "ASSEMBLY_SITE_SHAPE_REPRESENTATION",
        "ASSEMBLY_WORK_INSTRUCTION_SHAPE_REPRESENTATION",
        "CALIBRATION_SHAPE_REPRESENTATION",
        "CABLE_ROUTING_SHAPE_REPRESENTATION",
        "CATASTROPHE_SHAPE_REPRESENTATION",
        "CATALOG_SHAPE_REPRESENTATION",
        "CNC_PROGRAM_SHAPE_REPRESENTATION",
        "COMPONENT_DEFINITION_SHAPE_REPRESENTATION",
        "COMPONENT_SITE_SHAPE_REPRESENTATION",
        "COMPONENT_WORK_INSTRUCTION_SHAPE_REPRESENTATION",
        "CONCEPTUAL_SHAPE_REPRESENTATION",
        "CONNECTION_DEFINITION_SHAPE_REPRESENTATION",
        "CONNECTION_SITE_SHAPE_REPRESENTATION",
        "CONNECTION_WORK_INSTRUCTION_SHAPE_REPRESENTATION",
        "COVERAGE_SHAPE_REPRESENTATION",
        "DESIGN_SHAPE_REPRESENTATION",
        "DISASSEMBLY_PROCESS_SHAPE_REPRESENTATION",
        "DOCUMENT_SHAPE_REPRESENTATION",
        "ELECTRICAL_ANALYSIS_SHAPE_REPRESENTATION",
        "ELECTRONIC_ASSEMBLY_SHAPE_REPRESENTATION",
        "EMC_SHAPE_REPRESENTATION",
        "ENVIRONMENTAL_SHAPE_REPRESENTATION",
        "FAILURE_SHAPE_REPRESENTATION",
        "FASTENER_ASSEMBLY_SHAPE_REPRESENTATION",
        "FINISHING_PROCESS_SHAPE_REPRESENTATION",
        "FUNCTIONAL_SHAPE_REPRESENTATION",
        "GASKET_SHAPE_REPRESENTATION",
        "GEOMETRIC_ANALYSIS_SHAPE_REPRESENTATION",
        "GEOMETRIC_TOLERANCE_SHAPE_REPRESENTATION",
        "HANDLING_SHAPE_REPRESENTATION",
        "HEAT_TREATMENT_SHAPE_REPRESENTATION",
        "HUMAN_SHAPE_REPRESENTATION",
        "INSPECTION_PROCESS_SHAPE_REPRESENTATION",
        "INSTALLATION_PROCESS_SHAPE_REPRESENTATION",
        "INTERFACE_DEFINITION_SHAPE_REPRESENTATION",
        "INTERFACE_SITE_SHAPE_REPRESENTATION",
        "INTERFACE_WORK_INSTRUCTION_SHAPE_REPRESENTATION",
        "INTERLOCK_SHAPE_REPRESENTATION",
        "JOINING_PROCESS_SHAPE_REPRESENTATION",
        "KINEMATIC_SHAPE_REPRESENTATION",
        "LAYOUT_SHAPE_REPRESENTATION",
        "LIFE_CYCLE_SHAPE_REPRESENTATION",
        "LOGISTIC_SHAPE_REPRESENTATION",
        "LOGISTIC_PROCESS_SHAPE_REPRESENTATION",
        "LOGISTIC_SITE_SHAPE_REPRESENTATION",
        "LOGISTIC_WORK_INSTRUCTION_SHAPE_REPRESENTATION",
        "MAINTENANCE_PROCESS_SHAPE_REPRESENTATION",
        "MANUFACTURING_SHAPE_REPRESENTATION",
        "MARKING_SHAPE_REPRESENTATION",
        "MATERIAL_FLOW_SHAPE_REPRESENTATION",
        "MECHANICAL_ANALYSIS_SHAPE_REPRESENTATION",
        "MOUNTING_DEFINITION_SHAPE_REPRESENTATION",
        "MOUNTING_SITE_SHAPE_REPRESENTATION",
        "MOUNTING_WORK_INSTRUCTION_SHAPE_REPRESENTATION",
        "NETWORK_SHAPE_REPRESENTATION",
        "OPERATOR_SHAPE_REPRESENTATION",
        "PACKAGING_PROCESS_SHAPE_REPRESENTATION",
        "PART_DEFINITION_SHAPE_REPRESENTATION",
        "PART_SITE_SHAPE_REPRESENTATION",
        "PART_WORK_INSTRUCTION_SHAPE_REPRESENTATION",
        "PATH_SHAPE_REPRESENTATION",
        "PHYSICAL_SHAPE_REPRESENTATION",
        "PIPE_DEFINITION_SHAPE_REPRESENTATION",
        "PIPE_SITE_SHAPE_REPRESENTATION",
        "PIPE_WORK_INSTRUCTION_SHAPE_REPRESENTATION",
        "PLANNING_SHAPE_REPRESENTATION",
        "POSITION_SHAPE_REPRESENTATION",
        "PROCESS_PLAN_SHAPE_REPRESENTATION",
        "PROCESS_SITE_SHAPE_REPRESENTATION",
        "PROCESS_WORK_INSTRUCTION_SHAPE_REPRESENTATION",
        "PROTECTION_SHAPE_REPRESENTATION",
        "QUALITY_CONTROL_SHAPE_REPRESENTATION",
        "RACK_SHAPE_REPRESENTATION",
        "RECOVERY_SHAPE_REPRESENTATION",
        "RECYCLING_SHAPE_REPRESENTATION",
        "REPAIR_PROCESS_SHAPE_REPRESENTATION",
        "RESOURCE_SHAPE_REPRESENTATION",
        "RESPONSE_SHAPE_REPRESENTATION",
        "RISK_SHAPE_REPRESENTATION",
        "ROBOT_SHAPE_REPRESENTATION",
        "RULE_SHAPE_REPRESENTATION",
        "SAFETY_SHAPE_REPRESENTATION",
        "SCHEDULE_SHAPE_REPRESENTATION",
        "Schematic_SHAPE_REPRESENTATION",
        "SEALING_SHAPE_REPRESENTATION",
        "SERVICE_SHAPE_REPRESENTATION",
        "SETUP_SHAPE_REPRESENTATION",
        "SHIPMENT_SHAPE_REPRESENTATION",
        "SIMULATION_SHAPE_REPRESENTATION",
        "SITE_SHAPE_REPRESENTATION",
        "SOFTWARE_SHAPE_REPRESENTATION",
        "SOLUTION_SHAPE_REPRESENTATION",
        "SPECIFICATION_SHAPE_REPRESENTATION",
        "STANDARD_OPERATION_SHAPE_REPRESENTATION",
        "STORAGE_SHAPE_REPRESENTATION",
        "STRUCTURAL_ANALYSIS_SHAPE_REPRESENTATION",
        "SUPPLIER_SHAPE_REPRESENTATION",
        "SUPPORT_SHAPE_REPRESENTATION",
        "SYSTEM_SHAPE_REPRESENTATION",
        "TEST_SHAPE_REPRESENTATION",
        "TESTING_PROCESS_SHAPE_REPRESENTATION",
        "THERMAL_ANALYSIS_SHAPE_REPRESENTATION",
        "TOOL_DEFINITION_SHAPE_REPRESENTATION",
        "TOOL_SITE_SHAPE_REPRESENTATION",
        "TOOL_WORK_INSTRUCTION_SHAPE_REPRESENTATION",
        "TRAINING_SHAPE_REPRESENTATION",
        "TRANSPORT_SHAPE_REPRESENTATION",
        "VALIDATION_SHAPE_REPRESENTATION",
        "VARIANT_DEFINITION_SHAPE_REPRESENTATION",
        "VARIANT_SITE_SHAPE_REPRESENTATION",
        "VARIANT_WORK_INSTRUCTION_SHAPE_REPRESENTATION",
        "VIRTUAL_SHAPE_REPRESENTATION",
        "VISUALIZATION_SHAPE_REPRESENTATION",
        "WAREHOUSE_SHAPE_REPRESENTATION",
        "WARRANTY_SHAPE_REPRESENTATION",
        "WORK_INSTRUCTION_SHAPE_REPRESENTATION",
        "WORK_SHAPE_REPRESENTATION",
        "ZONE_DEFINITION_SHAPE_REPRESENTATION",
        "ZONE_SITE_SHAPE_REPRESENTATION",
        "ZONE_WORK_INSTRUCTION_SHAPE_REPRESENTATION");

    // 2D curve entities
    registry.put("CIRCLE_2D", StepEntityResolver::resolveCircle2D);
    registry.put("ELLIPSE_2D", StepEntityResolver::resolveEllipse2D);
    registry.put("HYPERBOLA_2D", StepEntityResolver::resolveHyperbola2D);
    registry.put("PARABOLA_2D", StepEntityResolver::resolveParabola2D);
    registry.put("LINE_2D", StepEntityResolver::resolveLine2D);
    registry.put("POLYLINE_2D", StepEntityResolver::resolvePolyline2D);
    registry.put("TRIMMED_CURVE_2D", StepEntityResolver::resolveTrimmedCurve2D);
    registry.put("COMPOSITE_CURVE_2D", StepEntityResolver::resolveCompositeCurve2D);
    registry.put("B_SPLINE_CURVE_2D", StepEntityResolver::resolveBSplineCurve2D);
    registry.put("RATIONAL_B_SPLINE_CURVE_2D", StepEntityResolver::resolveRationalBSplineCurve2D);
    registry.put("BEZIER_CURVE_2D", StepEntityResolver::resolveBezierCurve2D);
    registry.put("QUASI_UNIFORM_CURVE_2D", StepEntityResolver::resolveQuasiUniformCurve2D);
    registry.put("UNIFORM_CURVE_2D", StepEntityResolver::resolveUniformCurve2D);
    registry.put("PIECEWISE_BEZIER_CURVE_2D", StepEntityResolver::resolvePiecewiseBezierCurve2D);
    registry.put("INDEXED_POLY_CURVE_2D", StepEntityResolver::resolveIndexedPolyCurve2D);
    registry.put("DEGENERATE_CURVE_2D", StepEntityResolver::resolveDegenerateCurve2D);

    // Surfaces with resolver methods but missing registry entries
    registry.put("COMPOSITE_CURVE_ON_SURFACE", StepEntityResolver::resolveCompositeCurveOnSurface);
    registry.put("DEGENERATE_TOROIDAL_SURFACE", StepEntityResolver::resolveDegenerateToroidalSurface);
    registry.put("SURFACE_OF_LINEAR_EXTRUSION", StepEntityResolver::resolveSurfaceOfLinearExtrusion);
    registry.put("SURFACE_OF_TRANSLATION", StepEntityResolver::resolveSurfaceOfTranslation);
    registry.put("SURFACE_OF_PROJECTION", StepEntityResolver::resolveSurfaceOfProjection);
    registry.put("PARABOLOID_SURFACE", StepEntityResolver::resolveParaboloidSurface);
    registry.put("HYPERBOLOID_SURFACE", StepEntityResolver::resolveHyperboloidSurface);
    registry.put("RECTANGULAR_TRIMMED_SURFACE", StepEntityResolver::resolveRectangularTrimmedSurface);
    registry.put("SURFACE_STYLE_PARAMETER_LINE", StepEntityResolver::resolveSurfaceStyleParameterLine);
    registry.put("SURFACE_STYLE_REFLECTANCE_AMBIENT", StepEntityResolver::resolveSurfaceStyleReflectanceAmbient);
    registry.put("SURFACE_STYLE_SEGMENTATION_CURVE", StepEntityResolver::resolveSurfaceStyleSegmentationCurve);

    // CSG and solids
    registry.put("CSG_PRIMITIVE", (resolver, instance) ->
        resolver.resolveCsgPrimitive(instance, "CSG_PRIMITIVE", StepAxis2Placement3D.class, "AXIS2_PLACEMENT_3D", 3));

    // Transformations
    registry.put("CARTESIAN_TRANSFORMATION_OPERATOR", StepEntityResolver::resolveCartesianTransformationOperator);
    registry.put("CARTESIAN_TRANSFORMATION_OPERATOR_2D", StepEntityResolver::resolveCartesianTransformationOperator2D);
    registry.put("CARTESIAN_TRANSFORMATION_OPERATOR_3D", StepEntityResolver::resolveCartesianTransformationOperator3D);
    registry.put("ITEM_DEFINED_TRANSFORMATION", StepEntityResolver::resolveItemDefinedTransformation);

    // Profile definitions
    registry.put("CENTERED_CIRCLE_PROFILE_DEF", StepEntityResolver::resolveCenteredCircleProfileDef);
    registry.put("CENTRE_LINE_ARC_PROFILE_DEF", StepEntityResolver::resolveCentreLineArcProfileDef);
    registry.put("RECTANGLE_HOLLOW_PROFILE_DEF", StepEntityResolver::resolveRectangleHollowProfileDef);
    registry.put("ARBITRARY_CLOSED_PROFILE_DEF", (resolver, instance) ->
        resolver.resolveArbitraryClosedProfileDef(instance));
    registry.put("ARBITRARY_PROFILE_DEF", (resolver, instance) ->
        resolver.resolveArbitraryProfileDef(instance, "ARBITRARY_PROFILE_DEF"));
    registry.put("ARBITRARY_PROFILE_DEF_WITH_VOIDS", StepEntityResolver::resolveArbitraryProfileDefWithVoids);
    registry.put("PARAMETERIZED_PROFILE_DEF", (resolver, instance) ->
        resolver.resolveParameterizedProfileDef(instance, "PARAMETERIZED_PROFILE_DEF", 3));
    registry.put("PROFILE_DEF", StepEntityResolver::resolveProfileDef);
    registry.put("CIRCLE_PROFILE_DEF", StepEntityResolver::resolveCircleProfileDef);
    registry.put("RECTANGLE_PROFILE_DEF", StepEntityResolver::resolveRectangleProfileDef);

    // Representation and context
    registry.put("GEOMETRIC_REPRESENTATION_CONTEXT", StepEntityResolver::resolveGeometricRepresentationContext);
    registry.put("GEOMETRIC_REPRESENTATION_ITEM", StepEntityResolver::resolveGeometricRepresentationItem);
    registry.put("TOPOLOGICAL_REPRESENTATION_ITEM", StepEntityResolver::resolveTopologicalRepresentationItem);
    registry.put("CONTEXT_DEPENDENT_SHAPE_REPRESENTATION", StepEntityResolver::resolveContextDependentShapeRepresentation);
    registry.put("NEXT_ASSEMBLY_USAGE_OCCURRENCE", StepEntityResolver::resolveNextAssemblyUsageOccurrence);
    registry.put("DESCRIPTIVE_REPRESENTATION_ITEM", StepEntityResolver::resolveDescriptiveRepresentationItem);
    registry.put("MEASURE_REPRESENTATION_ITEM", StepEntityResolver::resolveMeasureRepresentationItem);
    registry.put("VALUE_REPRESENTATION_ITEM", StepEntityResolver::resolveValueRepresentationItem);
    // Validation and calculated geometry
    registry.put("VALIDATION_PROPERTY_REPRESENTATION",
        StepEntityResolver::resolveValidationPropertyRepresentation);
    registry.put("CALCULATED_GEOMETRIC_REPRESENTATION_ITEM",
        StepEntityResolver::resolveCalculatedGeometricRepresentationItem);

    // Units and uncertainty
    registry.put("GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT", StepEntityResolver::resolveGlobalUncertaintyAssignedContext);
    registry.put("GLOBAL_UNIT_ASSIGNED_CONTEXT", StepEntityResolver::resolveGlobalUnitAssignedContext);
    registry.put("CONVERSION_BASED_UNIT_WITH_OFFSET", StepEntityResolver::resolveConversionBasedUnitWithOffset);
    registry.put("TYPED_MEASURE_WITH_UNIT", (resolver, instance) ->
        resolver.resolveTypedMeasureWithUnit(instance, "TYPED_MEASURE_WITH_UNIT"));
    registry.put("UNCERTAINTY_MEASURE_WITH_UNIT", StepEntityResolver::resolveUncertaintyMeasureWithUnit);
    registry.put("COORDINATED_UNIVERSAL_TIME_OFFSET", StepEntityResolver::resolveCoordinatedUniversalTimeOffset);

    // Tolerance and datum
    registry.put("DATUM_SYSTEM", StepEntityResolver::resolveDatumSystem);
    registry.put("SHAPE_ASPECT_RELATIONSHIP", StepEntityResolver::resolveShapeAspectRelationship);
    registry.put("SHAPE_DEFINITION_REPRESENTATION", StepEntityResolver::resolveShapeDefinitionRepresentation);
    registry.put("SHAPE_REPRESENTATION_RELATIONSHIP", StepEntityResolver::resolveShapeRepresentationRelationship);
    registry.put("RECTANGULAR_TOLERANCE_ZONE", StepEntityResolver::resolveRectangularToleranceZone);

    // Curves with resolver methods but missing registry entries
    registry.put("B_SPLINE_CURVE", StepEntityResolver::resolveBSplineCurve);

    // 2D curves needing new resolver methods
    registry.put("BOUNDED_CURVE_2D", StepEntityResolver::resolveBoundedCurve2D);
    registry.put("CURVE_2D", StepEntityResolver::resolveCurve2D);

    // Swept area solids (generic, delegate to specific subtypes)
    registry.put("EXTRUDED_AREA_SOLID", (resolver, instance) ->
        resolver.resolveSweptAreaSolid(instance, "EXTRUDED_AREA_SOLID"));
    registry.put("REVOLVED_AREA_SOLID", (resolver, instance) ->
        resolver.resolveSweptAreaSolid(instance, "REVOLVED_AREA_SOLID"));

    // Machined surface
    registry.put("MACHINED_SURFACE", StepEntityResolver::resolveMachinedSurface);

    return registry;
  }

  private record ResolvedBSplineCurveData(
      String name,
      int degree,
      List<StepCartesianPoint> controlPoints,
      String curveForm,
      boolean closedCurve,
      boolean selfIntersect) {}

  private record ResolvedBSplineSurfaceData(
      String name,
      int uDegree,
      int vDegree,
      List<List<StepCartesianPoint>> controlPoints,
      String surfaceForm,
      boolean uClosed,
      boolean vClosed,
      boolean selfIntersect) {}

  private interface EntityFactory {
    StepEntity create(StepEntityResolver resolver, StepEntityInstance instance);
  }
}
