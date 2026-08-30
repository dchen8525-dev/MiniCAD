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
import com.minicad.step.model.StepFacetedBrepAndBrepWithVoids;
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
import com.minicad.step.model.StepAreaProfile;
import com.minicad.step.model.StepGeneralizedAreaProfile;
import com.minicad.step.model.StepSweptProfileAreaOutline;
import com.minicad.step.model.StepCenteredCircleProfileDef;
import com.minicad.step.model.StepChamfer;
import com.minicad.step.model.StepChamferEdge;
import com.minicad.step.model.StepClosedShell;
import com.minicad.step.model.StepChamferDefinition;
import com.minicad.step.model.StepFilletDefinition;
import com.minicad.step.model.StepFilletEdge;
import com.minicad.step.model.StepFlatPattern;
import com.minicad.step.model.StepThread;
import com.minicad.step.model.StepBore;
import com.minicad.step.model.StepCounterboreHole;
import com.minicad.step.model.StepCountersinkHole;
import com.minicad.step.model.StepPocket;
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
import com.minicad.step.model.StepFeatureElementDefinition;
import com.minicad.step.model.StepWebs;
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
import com.minicad.step.model.StepChange;
import com.minicad.step.model.StepStartRequest;
import com.minicad.step.model.StepStartWork;
import com.minicad.step.model.StepWorkItem;
import com.minicad.step.model.StepConfigurationItem;
import com.minicad.step.model.StepExclusionAssignment;
import com.minicad.step.model.StepDateTimeEffectivity;
import com.minicad.step.model.StepDateEffectivity;
import com.minicad.step.model.StepLotEffectivity;
import com.minicad.step.model.StepSerialNumberEffectivity;
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
import com.minicad.step.model.StepPmiRequirement;
import com.minicad.step.model.StepPmiGroup;
import com.minicad.step.model.StepAngularDimensionRepresentation;
import com.minicad.step.model.StepChainDimensionRepresentation;
import com.minicad.step.model.StepLinearDimensionRepresentation;
import com.minicad.step.model.StepOrdinateDimensionRepresentation;
import com.minicad.step.model.StepShapeDimensionRepresentationWithTolerance;
import com.minicad.step.model.StepGeometricMeasurement;
import com.minicad.step.model.StepGeometricToleranceWithDatumReference;
import com.minicad.step.model.StepLinearToleranceZone;
import com.minicad.step.model.StepRadialToleranceZone;
import com.minicad.step.model.StepProjectedZoneDefinition;
import com.minicad.step.model.StepPlusMinusToleranceWithModifiers;
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
import com.minicad.step.model.StepRevoluteJoint;
import com.minicad.step.model.StepPrismaticJoint;
import com.minicad.step.model.StepSphericalJoint;
import com.minicad.step.model.StepCylindricalJoint;
import com.minicad.step.model.StepPlanarJoint;
import com.minicad.step.model.StepScrewJoint;
import com.minicad.step.model.StepGeneralJoint;
import com.minicad.step.model.StepDirectionSense;
import com.minicad.step.model.StepJointValue;
import com.minicad.step.model.StepKinematicChain;
import com.minicad.step.model.StepKinematicModel;
import com.minicad.step.model.StepKinematicProperty;
import com.minicad.step.model.StepMotionConstraint;
import com.minicad.step.model.StepScrewPair;
import com.minicad.step.model.StepGearPair;
import com.minicad.step.model.StepGearPairWithRange;
import com.minicad.step.model.StepRackAndPinionPair;
import com.minicad.step.model.StepLowOrderKinematicPairWithRange;
import com.minicad.step.model.StepActuatedKinematicPair;
import com.minicad.step.model.StepMechanismStateRepresentation;
import com.minicad.step.model.StepKinematicPath;
import com.minicad.step.model.StepKinematicLinkReference;
import com.minicad.step.model.StepKinematicJointReference;
import com.minicad.step.model.StepMechanismDefinition;
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
import com.minicad.step.model.StepCompositeGroupTolerance;
import com.minicad.step.model.StepDatumReferenceModifierWithSign;
import com.minicad.step.model.StepGeometricToleranceTarget;
import com.minicad.step.model.StepModifier;
import com.minicad.step.model.StepQualifiedRepresentationItem;
import com.minicad.step.model.StepRunoutZoneDefinition;
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
import com.minicad.step.model.StepUsageAssociation;
import com.minicad.step.model.StepBuyFromUsageOption;
import com.minicad.step.model.StepAssemblyComponentUsage;
import com.minicad.step.model.StepBillOfMaterials;
import com.minicad.step.model.StepMakeFromRelationship;
import com.minicad.step.model.StepAssemblyOperation;
import com.minicad.step.model.StepAssemblySequence;
import com.minicad.step.model.StepAssemblyStructure;
import com.minicad.step.model.StepCadModelReference;
import com.minicad.step.model.StepComponentDefinition;
import com.minicad.step.model.StepEnvironmentalImpact;
import com.minicad.step.model.StepModuleDefinition;
import com.minicad.step.model.StepPartDefinition;
import com.minicad.step.model.StepProductVersion;
import com.minicad.step.model.StepProjectInformation;
import com.minicad.step.model.StepStructuralFeature;
import com.minicad.step.model.StepHybridShapeRepresentation;
import com.minicad.step.model.StepDrawingRepresentation;
import com.minicad.step.model.StepSchematicRepresentation;
import com.minicad.step.model.StepSketchRepresentation;
import com.minicad.step.model.StepSectionRepresentation;
import com.minicad.step.model.StepTabulationRepresentation;
import com.minicad.step.model.StepZoneRepresentation;
import com.minicad.step.model.StepCsgPrimitive3D;
import com.minicad.step.model.StepCompoundRepresentationItem;
import com.minicad.step.model.StepContextDependentGeometricShapeRepresentation;
import com.minicad.step.model.StepCylinderVolume;
import com.minicad.step.model.StepRightCircularConeVolume;
import com.minicad.step.model.StepSphereVolume;
import com.minicad.step.model.StepTorusVolume;
import com.minicad.step.model.StepPrismVolume;
import com.minicad.step.model.StepBlockVolume;
import com.minicad.step.model.StepTypedMeasureWithUnit;
import com.minicad.step.model.StepLengthUnitWithUnit;
import com.minicad.step.model.StepPlaneAngleUnitWithUnit;
import com.minicad.step.model.StepVolumeUnitWithUnit;
import com.minicad.step.model.StepAreaUnitWithUnit;
import com.minicad.step.model.StepMassUnitWithUnit;
import com.minicad.step.model.StepConversionBasedUnitAndUnit;
import com.minicad.step.model.StepExternallyDefinedConversionBasedUnit;
import com.minicad.step.model.StepNonAgreedUnitUsage;
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
import com.minicad.step.model.StepTextFileRepresentation;
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
import com.minicad.step.model.StepPolyline3D;
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
import com.minicad.step.model.StepMotionPath;
import com.minicad.step.model.StepAngularLocation;
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
import com.minicad.step.model.StepFeaNode;
import com.minicad.step.model.StepFeaElement;
import com.minicad.step.model.StepFeaLoad;
import com.minicad.step.model.StepFeaModel;
import com.minicad.step.model.StepMaterial;
import com.minicad.step.model.StepFeaLinearMaterial;
import com.minicad.step.model.StepFeaNonLinearMaterial;
import com.minicad.step.model.StepFeaMassDensity;
import com.minicad.step.model.StepFeaYieldStress;
import com.minicad.step.model.StepFeaUltimateStress;
import com.minicad.step.model.StepDisplacementBoundaryCondition;
import com.minicad.step.model.StepVelocityBoundaryCondition;
import com.minicad.step.model.StepAccelerationBoundaryCondition;
import com.minicad.step.model.StepForceBoundaryCondition;
import com.minicad.step.model.StepPressureBoundaryCondition;
import com.minicad.step.model.StepThermalBoundaryCondition;
import com.minicad.step.model.StepStressAnalysis;
import com.minicad.step.model.StepBucklingAnalysis;
import com.minicad.step.model.StepModalAnalysis;
import com.minicad.step.model.StepThermalAnalysis;
import com.minicad.step.model.StepStructuralAnalysisModel;
import com.minicad.step.model.StepElementVolume;
import com.minicad.step.model.StepVolumeElement;
import com.minicad.step.model.StepSurfaceElement;
import com.minicad.step.model.StepLineElement;
import com.minicad.step.model.StepMassElement;
import com.minicad.step.model.StepConnectivityElement;
import com.minicad.step.model.StepElementGeometricDescription;
import com.minicad.step.model.StepUniformSurfaceElement;
import com.minicad.step.model.StepUniformVolumeElement;
import com.minicad.step.model.StepNodeRepresentation;
import com.minicad.step.model.StepSpecificHigherUsageOccurrence;
import com.minicad.step.model.StepUsageOccurrence;
import com.minicad.step.model.StepShapeRepresentationTransformation;
import com.minicad.step.model.StepRepresentationContext3d;
import com.minicad.step.model.StepAppliedAttributeClassification;
import com.minicad.step.model.StepAttributeClassification;
import com.minicad.step.model.StepStructuralAnalysisRepresentation;
import com.minicad.step.model.StepStructuralAnalysisRepresentationParameters;
import com.minicad.step.model.StepValueReasonPair;
import com.minicad.step.model.StepBoundingBox;
import com.minicad.step.model.StepFillAreaShapeUse;
import com.minicad.step.model.StepPointOnFace;
import com.minicad.step.model.StepTessellatedCoordinateSet;
import com.minicad.step.model.StepUncertaintyMeasure;
import com.minicad.step.model.StepStructAnalysisModel;
import com.minicad.step.model.StepVolume3dElementProperty;
import com.minicad.step.model.StepCurve3dElementProperty;
import com.minicad.step.model.StepSurface3dElementProperty;
import com.minicad.step.model.StepFeaMaterialPropertyRepresentation;
import com.minicad.step.model.StepFeaShellElementProperty;
import com.minicad.step.model.StepFeaBeamElementProperty;
import com.minicad.step.model.StepFea2DElementProperty;
import com.minicad.step.model.StepFea3DElementProperty;
import com.minicad.step.model.StepFeaTrussElementProperty;
import com.minicad.step.model.StepFeaSpringElementProperty;
import com.minicad.step.model.StepFeaVolumeElementProperty;
import com.minicad.step.model.StepBoundaryCondition;
import com.minicad.step.model.StepLoadCase;
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
import com.minicad.step.model.StepTextLiteralWithDraughtingCallout;
import com.minicad.step.model.StepComposedTextLiteral;
import com.minicad.step.model.StepTextFont;
import com.minicad.step.model.StepCharacterGlyph;
import com.minicad.step.model.StepCharacterGlyphOutline;
import com.minicad.step.model.StepCharacterGlyphOutlineWithCharacteristics;
import com.minicad.step.model.StepCharacterGlyphStroke;
import com.minicad.step.model.StepPreDefinedSurfaceStyle;
import com.minicad.step.model.StepSurfaceStyleParameterLines;
import com.minicad.step.model.StepFillAreaStyleOutline;
import com.minicad.step.model.StepFillAreaStyleTransparent;
import com.minicad.step.model.StepFillAreaStyleHatching;
import com.minicad.step.model.StepFillAreaStyleTiling;
import com.minicad.step.model.StepAnnotationFillAreaRegion;
import com.minicad.step.model.StepFillAreaWithOutline;
import com.minicad.step.model.StepAnnotationRecord;
import com.minicad.step.model.StepDrawingReference;
import com.minicad.step.model.StepExternallyDefinedHatchStyle;
import com.minicad.step.model.StepExternallyDefinedTileStyle;
import com.minicad.step.model.StepMarkingFeature;
import com.minicad.step.model.StepTechnicalNote;
import com.minicad.step.model.StepCurveStyleFont;
import com.minicad.step.model.StepCurveStyleRendering;
import com.minicad.step.model.StepCurveStyleWithFont;
import com.minicad.step.model.StepDraughtingPreDefinedTerminatorSymbol;
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
import com.minicad.step.model.StepAttributeDefinition;
import com.minicad.step.model.StepAttributeInstance;
import com.minicad.step.model.StepCompositeShapeAspect;
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
import com.minicad.step.model.StepToroidalSurfaceWithSpecifiedBends;
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
import com.minicad.step.model.StepA3mEquivalenceAccuracyAssociation;
import com.minicad.step.model.StepA3mEquivalenceCriterion;
import com.minicad.step.model.StepA3mInspectedModelAndInspectionResultRelationship;
import com.minicad.step.model.StepA3maEquivalenceInspectionResult;
import com.minicad.step.model.StepA3msEquivalenceInspectionResult;
import com.minicad.step.model.StepDataEquivalenceAssessmentSpecification;
import com.minicad.step.model.StepDataEquivalenceInspectionCriterionReportItem;
import com.minicad.step.model.StepDataEquivalenceInspectionInstanceReportItem;
import com.minicad.step.model.StepDataEquivalenceInspectionRequirement;
import com.minicad.step.model.StepDataEquivalenceReportRequest;
import com.minicad.step.model.StepRepresentationItemRelationship;
import com.minicad.step.model.StepUnaryGenericExpression;
import com.minicad.step.model.StepBinaryGenericExpression;
import com.minicad.step.model.StepMultipleArityGenericExpression;
import com.minicad.step.model.StepSimpleGenericExpression;
import com.minicad.step.model.StepGenericEntity;
import com.minicad.step.model.StepCalculatedGeometricRepresentationItem;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;
import com.minicad.common.StepParseException;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepValue;

import java.util.*;
import java.util.stream.Collectors;

/** Resolves raw STEP AST entities into a minimal semantic model. */
public final class StepEntityResolver {

  private static final Map<String, EntityFactory> REGISTRY = createRegistry();
  private static final Map<String, Integer> REGISTRY_ORDER = createRegistryOrder(REGISTRY);
  // Comfortably above any real model's dependency depth, yet low enough that
  // the recursion unwinds before the default thread stack is exhausted.
  private static final int MAX_RESOLUTION_DEPTH = 512;

  final Map<Integer, StepEntityInstance> instancesById;
  private final Map<Integer, StepEntity> resolved = new LinkedHashMap<>();
  private final Deque<Integer> resolutionStack = new ArrayDeque<>();
  // O(1) mirror of resolutionStack for cycle detection; ArrayDeque.contains
  // would be O(depth) on every resolve call.
  private final Set<Integer> onResolutionStack = new HashSet<>();
  final StepTopologyResolver topologyResolver;
  final StepProductResolver productResolver;
  final GeometryResolver geometryResolver;
  final SurfaceResolver surfaceResolver;
  final GeometricFeatureResolver geometricFeatureResolver;
  final AnalysisResolver analysisResolver;
  final AnnotationResolver annotationResolver;
  final MaterialResolver materialResolver;
  final UnitResolver unitResolver;
  final KinematicResolver kinematicResolver;
  final TessellationResolver tessellationResolver;
  final AssignmentResolver assignmentResolver;
  final RepresentationResolver representationResolver;
  final BSplineResolver bSplineResolver;
  final BezierResolver bezierResolver;
  final SolidResolver solidResolver;
  final ProfileResolver profileResolver;
  final MachiningResolver machiningResolver;
  final TransformationResolver transformationResolver;
  private final GenericResolver genericResolver;
  final AssociationResolver associationResolver;
  final PropertyResolver propertyResolver;
  final CurveResolver curveResolver;
  final DraughtingResolver draughtingResolver;
  private final ManufacturingFeatureResolver manufacturingFeatureResolver;
  final VisualizationResolver visualizationResolver;
  final BoundaryConditionResolver boundaryConditionResolver;
  final FeaElementResolver feaElementResolver;

  private StepEntityResolver(StepFile file) {
    this.instancesById = file.entitiesById();
    this.topologyResolver = new StepTopologyResolver(this);
    this.productResolver = new StepProductResolver(this);
    this.geometryResolver = new GeometryResolver(this);
    this.surfaceResolver = new SurfaceResolver(this);
    this.geometricFeatureResolver = new GeometricFeatureResolver(this);
    this.analysisResolver = new AnalysisResolver(this);
    this.annotationResolver = new AnnotationResolver(this);
    this.materialResolver = new MaterialResolver(this);
    this.unitResolver = new UnitResolver(this);
    this.kinematicResolver = new KinematicResolver(this);
    this.tessellationResolver = new TessellationResolver(this);
    this.assignmentResolver = new AssignmentResolver(this);
    this.representationResolver = new RepresentationResolver(this);
    this.bSplineResolver = new BSplineResolver(this);
    this.bezierResolver = new BezierResolver(this);
    this.solidResolver = new SolidResolver(this);
    this.profileResolver = new ProfileResolver(this);
    this.machiningResolver = new MachiningResolver(this);
    this.transformationResolver = new TransformationResolver(this);
    this.genericResolver = new GenericResolver(this);
    this.associationResolver = new AssociationResolver(this);
    this.propertyResolver = new PropertyResolver(this);
    this.curveResolver = new CurveResolver(this);
    this.draughtingResolver = new DraughtingResolver(this);
    this.manufacturingFeatureResolver = new ManufacturingFeatureResolver(this);
    this.visualizationResolver = new VisualizationResolver(this);
    this.boundaryConditionResolver = new BoundaryConditionResolver(this);
    this.feaElementResolver = new FeaElementResolver(this);
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

  Map<Integer, StepEntity> resolveAll() {
    for (Integer id : instancesById.keySet()) {
      resolve(id);
    }
    // Unmodifiable view, not a copy: keeps declaration order for downstream
    // consumers (mesh face ordering follows this iteration order) and skips a
    // 93k-entry rehash on large files.
    return Collections.unmodifiableMap(resolved);
  }

  StepEntity resolve(int id) {
    StepEntity existing = resolved.get(id);
    if (existing != null) {
      return existing;
    }

    StepEntityInstance instance = instancesById.get(id);
    if (instance == null) {
      Integer referringId = resolutionStack.peek();
      if (referringId != null) {
        throw new StepResolutionException(
            "missing referenced entity #" + id + " referenced from entity #" + referringId);
      }
      throw new StepResolutionException("missing referenced entity #" + id);
    }

    EntityFactory factory = resolveFactory(instance);
    if (factory != null) {
      if (!onResolutionStack.add(id)) {
        throw new StepResolutionException(circularReferenceMessage(id));
      }
      // Cycle detection above cannot catch long-but-acyclic reference chains;
      // without a depth cap they die with an uncatchable StackOverflowError.
      if (resolutionStack.size() >= MAX_RESOLUTION_DEPTH) {
        throw new StepResolutionException(
            "entity reference chain deeper than " + MAX_RESOLUTION_DEPTH
                + " while resolving entity #" + id);
      }
      resolutionStack.push(id);
      try {
        StepEntity entity = factory.create(this, instance);
        resolved.put(id, entity);
        return entity;
      } finally {
        resolutionStack.pop();
        onResolutionStack.remove(id);
      }
    }

    throw new UnsupportedStepEntityException(
        "unsupported STEP entity #" + instance.id() + " " + instance.name());
  }

  private String circularReferenceMessage(int id) {
    StringBuilder path = new StringBuilder();
    for (Iterator<Integer> it = resolutionStack.descendingIterator(); it.hasNext(); ) {
      path.append('#').append(it.next()).append(" -> ");
    }
    path.append('#').append(id);
    return "circular reference while resolving entity #" + id + " (resolution path: " + path + ")";
  }

  static EntityFactory resolveFactory(StepEntityInstance instance) {
    EntityFactory selectedFactory = null;
    int selectedRank = Integer.MAX_VALUE;
    for (String normalizedName : instance.normalizedDefinitionNames()) {
      EntityFactory candidate = REGISTRY.get(normalizedName);
      if (candidate == null) {
        continue;
      }
      int candidateRank = REGISTRY_ORDER.getOrDefault(normalizedName, Integer.MAX_VALUE);
      if (candidateRank < selectedRank) {
        selectedFactory = candidate;
        selectedRank = candidateRank;
      }
    }
    return selectedFactory;
  }

  private static Map<String, Integer> createRegistryOrder(Map<String, EntityFactory> registry) {
    // Use LinkedHashMap to preserve insertion order from registry
    Map<String, Integer> order = new LinkedHashMap<>();
    int index = 0;
    for (String entityName : registry.keySet()) {
      order.put(entityName, index++);
    }
    return Map.copyOf(order);
  }

  StepCartesianPoint resolveCartesianPoint(StepEntityInstance instance) {
    return geometryResolver.resolveCartesianPoint(instance);
  }



  StepAxis2Placement3D resolveAxis2Placement3D(StepEntityInstance instance) {
    return geometryResolver.resolveAxis2Placement3D(instance);
  }

  StepAxis1Placement resolveAxis1Placement(StepEntityInstance instance) {
    return geometryResolver.resolveAxis1Placement(instance);
  }

  StepAxis2Placement2D resolveAxis2Placement2D(StepEntityInstance instance) {
    return geometryResolver.resolveAxis2Placement2D(instance);
  }






  StepConicCurve resolveConicCurve(StepEntityInstance instance, String entityName, int parameterCount) {
    return curveResolver.resolveConicCurve(instance, entityName, parameterCount);
  }




  StepDegenerateToroidalSurface resolveDegenerateToroidalSurface(
      StepEntityInstance instance) {
    return surfaceResolver.resolveDegenerateToroidalSurface(instance);
  }


  StepCylindricalSurfaceWithEllipticalAxis resolveCylindricalSurfaceWithEllipticalAxis(
      StepEntityInstance instance) {
    return surfaceResolver.resolveCylindricalSurfaceWithEllipticalAxis(instance);
  }

  StepConicalSurfaceWithEllipticalAxis resolveConicalSurfaceWithEllipticalAxis(
      StepEntityInstance instance) {
    return surfaceResolver.resolveConicalSurfaceWithEllipticalAxis(instance);
  }

  StepSphericalSurfaceWithEllipticalAxis resolveSphericalSurfaceWithEllipticalAxis(
      StepEntityInstance instance) {
    return surfaceResolver.resolveSphericalSurfaceWithEllipticalAxis(instance);
  }

  StepToroidalSurfaceWithEllipticalAxis resolveToroidalSurfaceWithEllipticalAxis(
      StepEntityInstance instance) {
    return surfaceResolver.resolveToroidalSurfaceWithEllipticalAxis(instance);
  }

  StepToroidalSurfaceWithCylindricalAxis resolveToroidalSurfaceWithCylindricalAxis(
      StepEntityInstance instance) {
    return surfaceResolver.resolveToroidalSurfaceWithCylindricalAxis(instance);
  }

  StepToroidalSurfaceWithSpecifiedBends resolveToroidalSurfaceWithSpecifiedBends(
      StepEntityInstance instance) {
    return surfaceResolver.resolveToroidalSurfaceWithSpecifiedBends(instance);
  }







  List<List<StepEntity>> resolveFreeFormControlPoints(StepEntityInstance instance, StepEntityDefinition definition, int index) {
    return curveResolver.resolveFreeFormControlPoints(instance, definition, index);
  }






  StepGeometricReplica resolveGeometricReplica(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 3);
    StepEntity parent = resolve(referenceId(instance, definition, 1));
    boolean validParent;
    if ("POINT_REPLICA".equals(entityName)) {
      validParent = parent instanceof StepCartesianPoint || parent instanceof StepVertexPoint;
    } else if ("CURVE_REPLICA".equals(entityName)) {
      validParent = StepResolverValueHelpers.isSupportedCurveReference(parent);
    } else if ("SURFACE_REPLICA".equals(entityName)) {
      validParent = StepResolverValueHelpers.isSupportedSurfaceReference(parent);
    } else {
      validParent = false;
    }
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



















  StepCompositeCurveSegment resolveCompositeCurveSegment(StepEntityInstance instance) {
    return bezierResolver.resolveCompositeCurveSegment(instance);
  }


  StepCompositeCurveOnSurface resolveCompositeCurveOnSurface(StepEntityInstance instance) {
    return curveResolver.resolveCompositeCurveOnSurface(instance);
  }

  StepCompositeCurveOnSurface resolveCompositeCurveOnSurface(StepEntityInstance instance, String entityName) {
    return curveResolver.resolveCompositeCurveOnSurface(instance, entityName);
  }


  List<StepValue> trimValues(
      StepEntityInstance instance, StepEntityDefinition definition, int index, String message) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue)) {
      throw StepParameterReader.parameterTypeMismatch(instance, definition, index, "list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    return List.copyOf(listValue.elements());
  }

  StepSurfaceCurve resolveSurfaceCurve(StepEntityInstance instance) {
    return curveResolver.resolveSurfaceCurve(instance);
  }

  StepSurfaceCurve resolveSurfaceCurve(StepEntityInstance instance, String entityName) {
    return curveResolver.resolveSurfaceCurve(instance, entityName);
  }




  StepBSplineCurveWithKnots resolveBSplineCurveWithKnots(StepEntityInstance instance) {
    return bSplineResolver.resolveBSplineCurveWithKnots(instance);
  }



  StepBSplineSurfaceWithKnots resolveBSplineSurfaceWithKnots(StepEntityInstance instance) {
    return bSplineResolver.resolveBSplineSurfaceWithKnots(instance);
  }


  StepBSplineCurveWithKnotsAndBreakpoints resolveBSplineCurveWithKnotsAndBreakpoints(
      StepEntityInstance instance) {
    return bSplineResolver.resolveBSplineCurveWithKnotsAndBreakpoints(instance);
  }

  StepBSplineSurfaceWithKnotsAndBreakpoints resolveBSplineSurfaceWithKnotsAndBreakpoints(
      StepEntityInstance instance) {
    return bSplineResolver.resolveBSplineSurfaceWithKnotsAndBreakpoints(instance);
  }













  static boolean isPathEntity(StepEntity entity) {
    return StepTopologyResolver.isPathEntity(entity);
  }



  StepFaceBound resolveFaceBound(StepEntityInstance instance, boolean outer) {
    return topologyResolver.resolveFaceBound(instance, outer);
  }













  StepManifoldSolidBrep resolveManifoldSolidBrep(StepEntityInstance instance) {
    return topologyResolver.resolveManifoldSolidBrep(instance);
  }

  StepManifoldSolidBrep resolveManifoldSolidBrep(StepEntityInstance instance, String entityName) {
    return topologyResolver.resolveManifoldSolidBrep(instance, entityName);
  }







  boolean isConnectedFaceSetEntity(StepEntity entity) {
    return topologyResolver.isConnectedFaceSetEntity(entity);
  }

  StepGeometricTolerance resolveGeometricTolerance(StepEntityInstance instance) {
    return annotationResolver.resolveGeometricTolerance(instance);
  }





  StepGeometricTolerance resolveGeometricTolerance(StepEntityInstance instance, String entityName) {
    return annotationResolver.resolveGeometricTolerance(instance, entityName);
  }

  StepModifier resolveModifier(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MODIFIER");
    requireParameterCount(instance, definition, 2);
    return new StepModifier(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1));
  }





  StepRunoutZoneDefinition resolveRunoutZoneDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "RUNOUT_ZONE_DEFINITION");
    requireParameterCount(instance, definition, 2);
    return new StepRunoutZoneDefinition(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepConfigurationEffectivity resolveConfigurationEffectivity(StepEntityInstance instance) {
    return analysisResolver.resolveConfigurationEffectivity(instance);
  }



  StepGeometricToleranceWithDatumReference resolveGeometricToleranceWithDatumReference(StepEntityInstance instance) {
    return annotationResolver.resolveGeometricToleranceWithDatumReference(instance);
  }



  StepProjectedZoneDefinition resolveProjectedZoneDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PROJECTED_ZONE_DEFINITION");
    requireParameterCount(instance, definition, 4);
    return new StepProjectedZoneDefinition(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        booleanValue(instance, definition, 3));
  }

  StepPlusMinusToleranceWithModifiers resolvePlusMinusToleranceWithModifiers(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PLUS_MINUS_TOLERANCE_WITH_MODIFIERS");
    requireParameterCount(instance, definition, 5);
    return new StepPlusMinusToleranceWithModifiers(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        numberValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)),
        StepResolverValueHelpers.literalList(instance, definition, 4));
  }

  // Phase 3: GD&T extended tolerance resolve methods

  StepGeometricToleranceWithDefinedAreaUnit resolveGeometricToleranceWithDefinedAreaUnit(
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

  StepGeometricToleranceWithMaximumTolerance resolveGeometricToleranceWithMaximumTolerance(
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

  StepNonUniformZoneDefinition resolveNonUniformZoneDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "NON_UNIFORM_ZONE_DEFINITION");
    requireParameterCount(instance, definition, 5);
    return new StepNonUniformZoneDefinition(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        numberValue(instance, definition, 3));
  }


  StepRunoutZoneDefinitionOrientation resolveRunoutZoneDefinitionOrientation(
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


  // Phase 4: Tessellated triangulated resolve methods

  StepTriangulatedFace resolveTriangulatedFace(StepEntityInstance instance) {
    return tessellationResolver.resolveTriangulatedFace(instance);
  }


  StepCubicBezierTriangulatedFace resolveCubicBezierTriangulatedFace(
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














  // Manufacturing operation resolvers
  StepMachiningOperation resolveMachiningOperation(StepEntityInstance instance) {
    return machiningResolver.resolveMachiningOperation(instance);
  }


  // Feature definition resolvers


  StepChamfer resolveChamfer(StepEntityInstance instance) {
    return geometricFeatureResolver.resolveChamfer(instance);
  }

  StepPocket resolvePocket(StepEntityInstance instance) {
    return manufacturingFeatureResolver.resolvePocket(instance);
  }

  StepBore resolveBore(StepEntityInstance instance) {
    return manufacturingFeatureResolver.resolveBore(instance);
  }

  StepCounterboreHole resolveCounterboreHole(StepEntityInstance instance) {
    return manufacturingFeatureResolver.resolveCounterboreHole(instance);
  }

  StepCountersinkHole resolveCountersinkHole(StepEntityInstance instance) {
    return manufacturingFeatureResolver.resolveCountersinkHole(instance);
  }

  StepRound resolveRound(StepEntityInstance instance) {
    return manufacturingFeatureResolver.resolveRound(instance);
  }

  StepGroove resolveGroove(StepEntityInstance instance) {
    return manufacturingFeatureResolver.resolveGroove(instance);
  }

  StepHole resolveHole(StepEntityInstance instance) {
    return manufacturingFeatureResolver.resolveHole(instance);
  }

  StepSlot resolveSlot(StepEntityInstance instance) {
    return manufacturingFeatureResolver.resolveSlot(instance);
  }

  StepStud resolveStud(StepEntityInstance instance) {
    return manufacturingFeatureResolver.resolveStud(instance);
  }

  StepProtrusion resolveProtrusion(StepEntityInstance instance) {
    return manufacturingFeatureResolver.resolveProtrusion(instance);
  }

  StepCutout resolveCutout(StepEntityInstance instance) {
    return manufacturingFeatureResolver.resolveCutout(instance);
  }

  StepDepression resolveDepression(StepEntityInstance instance) {
    return manufacturingFeatureResolver.resolveDepression(instance);
  }

  StepMarking resolveMarking(StepEntityInstance instance) {
    return draughtingResolver.resolveMarking(instance);
  }

  // Pattern resolvers
  StepCircularPattern resolveCircularPattern(StepEntityInstance instance) {
    return manufacturingFeatureResolver.resolveCircularPattern(instance);
  }

  StepLinearPattern resolveLinearPattern(StepEntityInstance instance) {
    return manufacturingFeatureResolver.resolveLinearPattern(instance);
  }




  StepAssemblyProcessPlan resolveAssemblyProcessPlan(StepEntityInstance instance) {
    return productResolver.resolveAssemblyProcessPlan(instance);
  }

  StepMachiningProcessPlan resolveMachiningProcessPlan(StepEntityInstance instance) {
    return machiningResolver.resolveMachiningProcessPlan(instance);
  }

  StepMachiningWorkPlan resolveMachiningWorkPlan(StepEntityInstance instance) {
    return machiningResolver.resolveMachiningWorkPlan(instance);
  }



  StepTypedMeasureWithUnit resolveTypedMeasureWithUnit(StepEntityInstance instance, String entityName) {
    return unitResolver.resolveTypedMeasureWithUnit(instance, entityName);
  }


  StepCartesianTransformationOperator resolveCartesianTransformationOperator(StepEntityInstance instance, String entityName) {
    return transformationResolver.resolveCartesianTransformationOperator(instance, entityName);
  }

  StepDirection optionalResolveDirection(int id) {
    StepEntity entity = resolve(id);
    if (entity instanceof StepDirection) {
            StepDirection direction = (StepDirection) entity;
      return direction;
    }
    return null;
  }

  StepCartesianPoint optionalResolveCartesianPoint(int id) {
    StepEntity entity = resolve(id);
    if (entity instanceof StepCartesianPoint) {
            StepCartesianPoint point = (StepCartesianPoint) entity;
      return point;
    }
    return null;
  }



  StepShapeDimensionRepresentation resolveShapeDimensionRepresentation(StepEntityInstance instance) {
    return annotationResolver.resolveShapeDimensionRepresentation(instance);
  }







  StepQuantifiedAssemblyComponentUsage resolveQuantifiedAssemblyComponentUsage(
      StepEntityInstance instance) {
    return productResolver.resolveQuantifiedAssemblyComponentUsage(instance);
  }

  StepSpecifiedHigherUsageOccurrence resolveSpecifiedHigherUsageOccurrence(
      StepEntityInstance instance) {
    return productResolver.resolveSpecifiedHigherUsageOccurrence(instance);
  }

  StepAlternateProductRelationship resolveAlternateProductRelationship(
      StepEntityInstance instance) {
    return productResolver.resolveAlternateProductRelationship(instance);
  }

  StepProductDefinitionWithAssociatedDocuments resolveProductDefinitionWithAssociatedDocuments(
      StepEntityInstance instance) {
    return productResolver.resolveProductDefinitionWithAssociatedDocuments(instance);
  }



  StepAssemblyComponentRelationship resolveAssemblyComponentRelationship(
      StepEntityInstance instance) {
    return productResolver.resolveAssemblyComponentRelationship(instance);
  }


  StepInterpolatedConfigurationSegment resolveInterpolatedConfigurationSegment(
      StepEntityInstance instance) {
    return productResolver.resolveInterpolatedConfigurationSegment(instance);
  }




















  StepKinematicPair resolveKinematicPair(StepEntityInstance instance) {
    return kinematicResolver.resolveKinematicPair(instance);
  }

  StepKinematicJoint resolveKinematicJoint(StepEntityInstance instance) {
    return kinematicResolver.resolveKinematicJoint(instance);
  }



  StepKinematicPair resolveKinematicPair(StepEntityInstance instance, String entityName) {
    return kinematicResolver.resolveKinematicPair(instance, entityName);
  }













  StepMechanismStateRepresentation resolveMechanismStateRepresentation(StepEntityInstance instance) {
    return kinematicResolver.resolveMechanismStateRepresentation(instance);
  }

  StepKinematicPath resolveKinematicPath(StepEntityInstance instance) {
    return kinematicResolver.resolveKinematicPath(instance);
  }




  // Phase 5: FEA resolve methods





  StepFeaModel resolveFeaModel(StepEntityInstance instance) {
    return analysisResolver.resolveFeaModel(instance);
  }

























  StepDirectionSense resolveDirectionSense(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DIRECTION_SENSE");
    requireParameterCount(instance, definition, 2);
    return new StepDirectionSense(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1));
  }






  StepChange resolveChange(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CHANGE");
    requireParameterCount(instance, definition, 3);
    return new StepChange(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1));
  }

  StepStartRequest resolveStartRequest(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "START_REQUEST");
    requireParameterCount(instance, definition, 3);
    return new StepStartRequest(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1));
  }

  StepStartWork resolveStartWork(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "START_WORK");
    requireParameterCount(instance, definition, 3);
    return new StepStartWork(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1));
  }

  StepWorkItem resolveWorkItem(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "WORK_ITEM");
    requireParameterCount(instance, definition, 3);
    return new StepWorkItem(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1));
  }

  StepSpecificHigherUsageOccurrence resolveSpecificHigherUsageOccurrence(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SPECIFIC_HIGHER_USAGE_OCCURRENCE");
    requireParameterCount(instance, definition, 3);
    return new StepSpecificHigherUsageOccurrence(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }

  StepUsageOccurrence resolveUsageOccurrence(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "USAGE_OCCURRENCE");
    requireParameterCount(instance, definition, 3);
    return new StepUsageOccurrence(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }




  StepAttributeClassification resolveAttributeClassification(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ATTRIBUTE_CLASSIFICATION");
    requireParameterCount(instance, definition, 3);
    return new StepAttributeClassification(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }



  StepValueReasonPair resolveValueReasonPair(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "VALUE_REASON_PAIR");
    requireParameterCount(instance, definition, 3);
    return new StepValueReasonPair(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        stringValue(instance, definition, 2));
  }

  StepBoundingBox resolveBoundingBox(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BOUNDING_BOX");
    requireParameterCount(instance, definition, 3);
    return new StepBoundingBox(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }

  StepFillAreaShapeUse resolveFillAreaShapeUse(StepEntityInstance instance) {
    return materialResolver.resolveFillAreaShapeUse(instance);
  }

  StepPointOnFace resolvePointOnFace(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "POINT_ON_FACE");
    requireParameterCount(instance, definition, 4);
    return new StepPointOnFace(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  StepTessellatedCoordinateSet resolveTessellatedCoordinateSet(StepEntityInstance instance) {
    return tessellationResolver.resolveTessellatedCoordinateSet(instance);
  }

  StepUncertaintyMeasure resolveUncertaintyMeasure(StepEntityInstance instance) {
    return unitResolver.resolveUncertaintyMeasure(instance);
  }

  StepStructAnalysisModel resolveStructAnalysisModel(StepEntityInstance instance) {
    return analysisResolver.resolveStructAnalysisModel(instance);
  }

























  // New FEA element property resolvers







  // Unit with unit resolvers






  // Profile resolvers
  StepAreaProfile resolveAreaProfile(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "AREA_PROFILE");
    requireParameterCount(instance, definition, 3);
    return new StepAreaProfile(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepGeneralizedAreaProfile resolveGeneralizedAreaProfile(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GENERALIZED_AREA_PROFILE");
    requireParameterCount(instance, definition, 3);
    return new StepGeneralizedAreaProfile(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }


  // Kinematic reference resolvers


  // Product representation resolvers








  StepCompoundRepresentationItem resolveCompoundRepresentationItem(StepEntityInstance instance, String entityName) {
    return representationResolver.resolveCompoundRepresentationItem(instance, entityName);
  }



  StepBuyFromUsageOption resolveBuyFromUsageOption(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BUY_FROM_USAGE_OPTION");
    requireParameterCount(instance, definition, 3);
    return new StepBuyFromUsageOption(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  // Config management resolvers





  // Geometry resolvers
  StepIndexedPolyCurve resolveIndexedPolycurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "INDEXED_POLYCURVE");
    requireParameterCount(instance, definition, 5);
    @SuppressWarnings("unchecked")
    List<StepCartesianPoint> points = (List<StepCartesianPoint>) (List<?>) entityReferenceList(instance, definition, 1,
        "INDEXED_POLYCURVE points must contain entity references");
    return new StepIndexedPolyCurve(
        instance.id(),
        stringValue(instance, definition, 0),
        points,
        StepResolverValueHelpers.intList(instance, definition, 2),
        booleanValue(instance, definition, 4));
  }

  StepPolyline3D resolvePolyline3D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "POLYLINE_3D");
    requireParameterCount(instance, definition, 3);
    return new StepPolyline3D(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "POLYLINE_3D points must contain entity references"));
  }

  // Annotation resolvers

  // Product resolvers



  StepCadModelReference resolveCadModelReference(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CAD_MODEL_REFERENCE");
    requireParameterCount(instance, definition, 8);
    return new StepCadModelReference(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        stringValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)),
        resolve(referenceId(instance, definition, 4)),
        resolve(referenceId(instance, definition, 5)),
        stringValue(instance, definition, 6));
  }

  StepComponentDefinition resolveComponentDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COMPONENT_DEFINITION");
    requireParameterCount(instance, definition, 8);
    return new StepComponentDefinition(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        stringValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)),
        entityReferenceList(instance, definition, 4,
            "COMPONENT_DEFINITION dependencies must contain entity references"),
        entityReferenceList(instance, definition, 5,
            "COMPONENT_DEFINITION properties must contain entity references"),
        stringValue(instance, definition, 6));
  }

  StepEnvironmentalImpact resolveEnvironmentalImpact(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ENVIRONMENTAL_IMPACT");
    requireParameterCount(instance, definition, 8);
    return new StepEnvironmentalImpact(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        numberValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)),
        numberValue(instance, definition, 4),
        entityReferenceList(instance, definition, 5,
            "ENVIRONMENTAL_IMPACT measures must contain entity references"),
        stringValue(instance, definition, 6));
  }


  StepModuleDefinition resolveModuleDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MODULE_DEFINITION");
    requireParameterCount(instance, definition, 7);
    return new StepModuleDefinition(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        stringValue(instance, definition, 2),
        entityReferenceList(instance, definition, 3,
            "MODULE_DEFINITION components must contain entity references"),
        entityReferenceList(instance, definition, 4,
            "MODULE_DEFINITION interfaces must contain entity references"),
        stringValue(instance, definition, 5));
  }

  StepPartDefinition resolvePartDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PART_DEFINITION");
    requireParameterCount(instance, definition, 6);
    return new StepPartDefinition(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        stringValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)),
        resolve(referenceId(instance, definition, 4)));
  }


  StepProjectInformation resolveProjectInformation(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PROJECT_INFORMATION");
    requireParameterCount(instance, definition, 9);
    return new StepProjectInformation(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        stringValue(instance, definition, 2),
        entityReferenceList(instance, definition, 3,
            "PROJECT_INFORMATION members must contain entity references"),
        resolve(referenceId(instance, definition, 4)),
        resolve(referenceId(instance, definition, 5)),
        numberValue(instance, definition, 6),
        stringValue(instance, definition, 7));
  }



  // Annotation resolvers

  StepDrawingReference resolveDrawingReference(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DRAWING_REFERENCE");
    requireParameterCount(instance, definition, 8);
    return new StepDrawingReference(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        stringValue(instance, definition, 2),
        stringValue(instance, definition, 3),
        numberValue(instance, definition, 4),
        stringValue(instance, definition, 5),
        resolve(referenceId(instance, definition, 6)));
  }




  StepTechnicalNote resolveTechnicalNote(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TECHNICAL_NOTE");
    requireParameterCount(instance, definition, 8);
    return new StepTechnicalNote(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        stringValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)),
        resolve(referenceId(instance, definition, 4)),
        stringValue(instance, definition, 5),
        stringValue(instance, definition, 6));
  }

  // Tolerance/dimension representation resolvers





  // FEA resolvers
  StepBoundaryCondition resolveBoundaryCondition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BOUNDARY_CONDITION");
    requireParameterCount(instance, definition, 6);
    return new StepBoundaryCondition(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        StepResolverValueHelpers.stringList(instance, definition, 3),
        stringValue(instance, definition, 4));
  }

  StepLoadCase resolveLoadCase(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LOAD_CASE");
    requireParameterCount(instance, definition, 6);
    return new StepLoadCase(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        entityReferenceList(instance, definition, 2,
            "LOAD_CASE loads must contain entity references"),
        stringValue(instance, definition, 3),
        stringValue(instance, definition, 4));
  }

  // Classification resolvers
  StepAttributeDefinition resolveAttributeDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ATTRIBUTE_DEFINITION");
    requireParameterCount(instance, definition, 7);
    return new StepAttributeDefinition(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        stringValue(instance, definition, 2),
        StepResolverValueHelpers.stringList(instance, definition, 3),
        stringValue(instance, definition, 4),
        stringValue(instance, definition, 5));
  }

  StepAttributeInstance resolveAttributeInstance(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ATTRIBUTE_INSTANCE");
    requireParameterCount(instance, definition, 5);
    return new StepAttributeInstance(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        stringValue(instance, definition, 2),
        stringValue(instance, definition, 3));
  }


  // Product resolvers
  StepAssemblyComponentUsage resolveAssemblyComponentUsage(StepEntityInstance instance) {
    return productResolver.resolveAssemblyComponentUsage(instance);
  }




  StepComposedTextLiteral resolveComposedTextLiteral(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COMPOSED_TEXT_LITERAL");
    requireParameterCount(instance, definition, 3);
    return new StepComposedTextLiteral(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "COMPOSED_TEXT_LITERAL components must contain entity references"));
  }
















  // Tolerance/PMI resolvers
  StepPmiRequirement resolvePmiRequirement(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PMI_REQUIREMENT");
    requireParameterCount(instance, definition, 4);
    return new StepPmiRequirement(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        stringValue(instance, definition, 2));
  }


  // Manufacturing resolvers

  StepWebs resolveWebs(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "WEBS");
    requireParameterCount(instance, definition, 3);
    return new StepWebs(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1));
  }

  StepPattern resolvePattern(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PATTERN");
    requireParameterCount(instance, definition, 4);
    return new StepPattern(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  // Document resolver




  StepAngularSize resolveAngularSize(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ANGULAR_SIZE");
    requireParameterCount(instance, definition, 4);
    return new StepAngularSize(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        numberValue(instance, definition, 2));
  }


  StepActionDirective resolveActionDirective(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ACTION_DIRECTIVE");
    requireParameterCount(instance, definition, 4);
    return new StepActionDirective(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        stringValue(instance, definition, 2));
  }

  StepActionMethod resolveActionMethod(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ACTION_METHOD");
    requireParameterCount(instance, definition, 4);
    return new StepActionMethod(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        stringValue(instance, definition, 2));
  }

  StepAction resolveAction(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ACTION");
    requireParameterCount(instance, definition, 4);
    return new StepAction(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        stringValue(instance, definition, 2));
  }

  StepActionRelationship resolveActionRelationship(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ACTION_RELATIONSHIP");
    requireParameterCount(instance, definition, 5);
    return new StepActionRelationship(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  StepActionStatus resolveActionStatus(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ACTION_STATUS");
    requireParameterCount(instance, definition, 3);
    return new StepActionStatus(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1));
  }



  boolean isOpenShellEntity(StepEntity entity) {
    return topologyResolver.isOpenShellEntity(entity);
  }

  boolean isClosedShellEntity(StepEntity entity) {
    return topologyResolver.isClosedShellEntity(entity);
  }

  boolean isShellEntity(StepEntity entity) {
    return topologyResolver.isShellEntity(entity);
  }

  boolean isBooleanOperandEntity(StepEntity entity) {
    return entity instanceof StepManifoldSolidBrep
        || entity instanceof StepBrepWithVoids
        || entity instanceof StepFacetedBrepAndBrepWithVoids
        || entity instanceof StepCsgPrimitive
        || entity instanceof StepCsgSolid
        || entity instanceof StepHalfSpaceSolid
        || entity instanceof StepPolygonalBoundedHalfSpace
        || entity instanceof StepSolidReplica
        || entity instanceof StepSweptAreaSolid
        || entity instanceof StepBooleanResult
        || entity instanceof StepBooleanClippingResult;
  }

  StepShellBasedWireframeModel resolveShellBasedWireframeModel(
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

  StepBrepWithVoids resolveBrepWithVoids(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BREP_WITH_VOIDS");
    requireParameterCount(instance, definition, 3);
    return new StepBrepWithVoids(
        instance.id(),
        stringValue(instance, definition, 0),
        requireClosedShellEntity(instance, definition, 1, "BREP_WITH_VOIDS outer must reference CLOSED_SHELL"),
        requireClosedShellEntities(instance, definition, 2, "BREP_WITH_VOIDS voids must contain CLOSED_SHELL references"));
  }

  StepFacetedBrepAndBrepWithVoids resolveFacetedBrepAndBrepWithVoids(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FACETED_BREP_AND_BREP_WITH_VOIDS");
    requireParameterCount(instance, definition, 3);
    return new StepFacetedBrepAndBrepWithVoids(
        instance.id(),
        stringValue(instance, definition, 0),
        requireClosedShellEntity(instance, definition, 1, "FACETED_BREP_AND_BREP_WITH_VOIDS outer must reference CLOSED_SHELL"),
        requireClosedShellEntities(instance, definition, 2, "FACETED_BREP_AND_BREP_WITH_VOIDS voids must contain CLOSED_SHELL references"));
  }


  StepBooleanResult resolveBooleanResult(StepEntityInstance instance, String entityName) {
    return solidResolver.resolveBooleanResult(instance, entityName);
  }

  StepBooleanClippingResult resolveBooleanClippingResult(StepEntityInstance instance) {
    StepBooleanResult base = resolveBooleanResult(instance, "BOOLEAN_CLIPPING_RESULT");
    return new StepBooleanClippingResult(
        base.id(), base.name(), base.operator(), base.firstOperand(), base.secondOperand());
  }

  StepEntity requireClosedShellEntity(
      StepEntityInstance instance, StepEntityDefinition definition, int parameterIndex, String message) {
    StepEntity shell = resolve(referenceId(instance, definition, parameterIndex));
    if (!isClosedShellEntity(shell)) {
      throw new StepResolutionException(message);
    }
    return shell;
  }

  List<StepEntity> requireClosedShellEntities(
      StepEntityInstance instance, StepEntityDefinition definition, int parameterIndex, String message) {
    List<StepEntity> shells = entityReferenceList(instance, definition, parameterIndex, message);
    for (StepEntity shell : shells) {
      if (!isClosedShellEntity(shell)) {
        throw new StepResolutionException(message);
      }
    }
    return shells;
  }


  StepApplicationContext resolveApplicationContext(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "APPLICATION_CONTEXT");
    requireParameterCount(instance, definition, 1);
    return new StepApplicationContext(instance.id(), stringValue(instance, definition, 0));
  }

  StepApplicationProtocolDefinition resolveApplicationProtocolDefinition(
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

  StepProductContext resolveProductContext(StepEntityInstance instance) {
    return productResolver.resolveProductContext(instance);
  }

  StepProductContext resolveProductContext(
      StepEntityInstance instance, String entityName) {
    return productResolver.resolveProductContext(instance, entityName);
  }


  StepProductRelatedProductCategory resolveProductRelatedProductCategory(
      StepEntityInstance instance) {
    return productResolver.resolveProductRelatedProductCategory(instance);
  }


  StepProductCategoryRelationship resolveProductCategoryRelationship(
      StepEntityInstance instance) {
    return productResolver.resolveProductCategoryRelationship(instance);
  }


  StepProductRelationship resolveProductRelationship(
      StepEntityInstance instance, String entityName) {
    return productResolver.resolveProductRelationship(instance, entityName);
  }

  StepProductDefinitionFormation resolveProductDefinitionFormation(
      StepEntityInstance instance) {
    return productResolver.resolveProductDefinitionFormation(instance);
  }


  StepProductDefinitionContext resolveProductDefinitionContext(
      StepEntityInstance instance) {
    return productResolver.resolveProductDefinitionContext(instance);
  }

  StepProductDefinitionContext resolveProductDefinitionContext(
      StepEntityInstance instance, String entityName) {
    return productResolver.resolveProductDefinitionContext(instance, entityName);
  }

  StepProductDefinition resolveProductDefinition(StepEntityInstance instance) {
    return productResolver.resolveProductDefinition(instance);
  }

  StepProductDefinitionRelationship resolveProductDefinitionRelationship(
      StepEntityInstance instance) {
    return productResolver.resolveProductDefinitionRelationship(instance);
  }

  StepProductDefinitionRelationship resolveProductDefinitionRelationship(
      StepEntityInstance instance, String entityName) {
    return productResolver.resolveProductDefinitionRelationship(instance, entityName);
  }

  // Flexible resolver for entities that allow relating/related to be broader types
  // Returns StepGenericEntity since relating/related may not be StepProductDefinition
  StepGenericEntity resolveProductDefinitionRelationshipFlexible(
      StepEntityInstance instance, String entityName) {
    return productResolver.resolveProductDefinitionRelationshipFlexible(instance, entityName);
  }

  StepProductDefinitionRelationshipRelationship resolveProductDefinitionRelationshipRelationship(StepEntityInstance instance) {
    return productResolver.resolveProductDefinitionRelationshipRelationship(instance);
  }

  StepProductDefinitionRelationshipRelationship resolveProductDefinitionRelationshipRelationship(
      StepEntityInstance instance, String entityName) {
    return productResolver.resolveProductDefinitionRelationshipRelationship(instance, entityName);
  }


  StepPropertyDefinition resolvePropertyDefinition(StepEntityInstance instance) {
    return propertyResolver.resolvePropertyDefinition(instance);
  }


  StepPropertyDefinitionRelationship resolvePropertyDefinitionRelationship(StepEntityInstance instance, String entityName) {
    return propertyResolver.resolvePropertyDefinitionRelationship(instance, entityName);
  }


  StepGeneralPropertyRelationship resolveGeneralPropertyRelationship(StepEntityInstance instance) {
    return propertyResolver.resolveGeneralPropertyRelationship(instance);
  }

  StepGroup resolveGroup(StepEntityInstance instance) {
    return propertyResolver.resolveGroup(instance);
  }

  StepGroup resolveGroup(StepEntityInstance instance, String entityName) {
    return propertyResolver.resolveGroup(instance, entityName);
  }


  StepGroupRelationship resolveGroupRelationship(StepEntityInstance instance, String entityName) {
    return propertyResolver.resolveGroupRelationship(instance, entityName);
  }



  StepAddress resolveAddress(StepEntityInstance instance) {
    return assignmentResolver.resolveAddress(instance);
  }


  StepDocument resolveDocument(StepEntityInstance instance) {
    return productResolver.resolveDocument(instance);
  }




  StepAppliedDocumentReference resolveAppliedDocumentReference(StepEntityInstance instance) {
    return productResolver.resolveAppliedDocumentReference(instance);
  }

  StepAppliedDocumentReference resolveAppliedDocumentReference(StepEntityInstance instance, String entityName) {
    return productResolver.resolveAppliedDocumentReference(instance, entityName);
  }


  StepOrganization resolveOrganization(StepEntityInstance instance) {
    return assignmentResolver.resolveOrganization(instance);
  }


  StepOrganizationRelationship resolveOrganizationRelationship(StepEntityInstance instance) {
    return assignmentResolver.resolveOrganizationRelationship(instance);
  }



  StepAppliedOrganizationAssignment resolveAppliedOrganizationAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveAppliedOrganizationAssignment(instance);
  }

  StepAppliedOrganizationAssignment resolveAppliedOrganizationAssignment(StepEntityInstance instance, String entityName) {
    return assignmentResolver.resolveAppliedOrganizationAssignment(instance, entityName);
  }

  StepLanguage resolveLanguage(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LANGUAGE");
    requireParameterCount(instance, definition, 1);
    return new StepLanguage(instance.id(), stringValue(instance, definition, 0));
  }





  StepAppliedPersonAndOrganizationAssignment resolveAppliedPersonAndOrganizationAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveAppliedPersonAndOrganizationAssignment(instance);
  }

  StepAppliedPersonAndOrganizationAssignment resolveAppliedPersonAndOrganizationAssignment(StepEntityInstance instance, String entityName) {
    return assignmentResolver.resolveAppliedPersonAndOrganizationAssignment(instance, entityName);
  }

  StepCalendarDate resolveCalendarDate(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CALENDAR_DATE");
    requireParameterCount(instance, definition, 3);
    return new StepCalendarDate(
        instance.id(),
        integerValue(instance, definition, 0),
        integerValue(instance, definition, 1),
        integerValue(instance, definition, 2));
  }

  StepCoordinatedUniversalTimeOffset resolveCoordinatedUniversalTimeOffset(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COORDINATED_UNIVERSAL_TIME_OFFSET");
    requireParameterCount(instance, definition, 3);
    return new StepCoordinatedUniversalTimeOffset(
        instance.id(),
        integerValue(instance, definition, 0),
        StepResolverValueHelpers.optionalIntegerValue(instance, definition, 1),
        enumValue(instance, definition, 2));
  }

  StepLocalTime resolveLocalTime(StepEntityInstance instance) {
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

  StepDateAndTime resolveDateAndTime(StepEntityInstance instance) {
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



  StepAppliedDateAssignment resolveAppliedDateAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveAppliedDateAssignment(instance);
  }

  StepAppliedDateAssignment resolveAppliedDateAssignment(StepEntityInstance instance, String entityName) {
    return assignmentResolver.resolveAppliedDateAssignment(instance, entityName);
  }



  StepAppliedDateTimeAssignment resolveAppliedDateTimeAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveAppliedDateTimeAssignment(instance);
  }

  StepAppliedDateTimeAssignment resolveAppliedDateTimeAssignment(StepEntityInstance instance, String entityName) {
    return assignmentResolver.resolveAppliedDateTimeAssignment(instance, entityName);
  }


  StepApproval resolveApproval(StepEntityInstance instance) {
    return assignmentResolver.resolveApproval(instance);
  }



  StepAppliedApprovalAssignment resolveAppliedApprovalAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveAppliedApprovalAssignment(instance);
  }

  StepAppliedApprovalAssignment resolveAppliedApprovalAssignment(StepEntityInstance instance, String entityName) {
    return assignmentResolver.resolveAppliedApprovalAssignment(instance, entityName);
  }






  StepAppliedSecurityClassificationAssignment resolveAppliedSecurityClassificationAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveAppliedSecurityClassificationAssignment(instance);
  }

  StepAppliedSecurityClassificationAssignment resolveAppliedSecurityClassificationAssignment(StepEntityInstance instance, String entityName) {
    return assignmentResolver.resolveAppliedSecurityClassificationAssignment(instance, entityName);
  }


  StepContract resolveContract(StepEntityInstance instance) {
    return assignmentResolver.resolveContract(instance);
  }


  StepAppliedContractAssignment resolveAppliedContractAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveAppliedContractAssignment(instance);
  }

  StepAppliedContractAssignment resolveAppliedContractAssignment(StepEntityInstance instance, String entityName) {
    return assignmentResolver.resolveAppliedContractAssignment(instance, entityName);
  }


  StepCertification resolveCertification(StepEntityInstance instance) {
    return assignmentResolver.resolveCertification(instance);
  }


  StepAppliedCertificationAssignment resolveAppliedCertificationAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveAppliedCertificationAssignment(instance);
  }

  StepAppliedCertificationAssignment resolveAppliedCertificationAssignment(StepEntityInstance instance, String entityName) {
    return assignmentResolver.resolveAppliedCertificationAssignment(instance, entityName);
  }

  StepEffectivity resolveEffectivity(StepEntityInstance instance) {
    return assignmentResolver.resolveEffectivity(instance);
  }

  StepProductDefinitionEffectivity resolveProductDefinitionEffectivity(
      StepEntityInstance instance) {
    return productResolver.resolveProductDefinitionEffectivity(instance);
  }












  StepDescriptionAttribute resolveDescriptionAttribute(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DESCRIPTION_ATTRIBUTE");
    requireParameterCount(instance, definition, 2);
    return new StepDescriptionAttribute(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepNameAttribute resolveNameAttribute(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "NAME_ATTRIBUTE");
    requireParameterCount(instance, definition, 2);
    return new StepNameAttribute(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepIdAttribute resolveIdAttribute(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ID_ATTRIBUTE");
    requireParameterCount(instance, definition, 2);
    return new StepIdAttribute(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepExternalSource resolveExternalSource(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "EXTERNAL_SOURCE");
    requireParameterCount(instance, definition, 1);
    return new StepExternalSource(instance.id(), stringValue(instance, definition, 0));
  }

  StepExternalSourceRelationship resolveExternalSourceRelationship(
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

  StepExternallyDefinedItem resolveExternallyDefinedItem(
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

  StepCharacterizedObject resolveCharacterizedObject(StepEntityInstance instance) {
    return resolveCharacterizedObject(instance, "CHARACTERIZED_OBJECT");
  }

  StepCharacterizedObject resolveCharacterizedObject(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 2);
    return new StepCharacterizedObject(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        entityName);
  }

  StepShapeAspect resolveShapeAspect(StepEntityInstance instance) {
    return representationResolver.resolveShapeAspect(instance);
  }

  StepShapeAspect resolveShapeAspect(StepEntityInstance instance, String entityName) {
    return representationResolver.resolveShapeAspect(instance, entityName);
  }

  StepShapeAspectOccurrence resolveShapeAspectOccurrence(StepEntityInstance instance, String entityName) {
    return representationResolver.resolveShapeAspectOccurrence(instance, entityName);
  }

  StepShapeAspectRelationship resolveShapeAspectRelationship(StepEntityInstance instance) {
    return representationResolver.resolveShapeAspectRelationship(instance);
  }

  StepShapeAspectRelationship resolveShapeAspectRelationship(StepEntityInstance instance, String entityName) {
    return representationResolver.resolveShapeAspectRelationship(instance, entityName);
  }

  StepShapeDefinitionRepresentation resolveShapeDefinitionRepresentation(
      StepEntityInstance instance) {
    return productResolver.resolveShapeDefinitionRepresentation(instance);
  }

  StepPropertyDefinitionRepresentation resolvePropertyDefinitionRepresentation(StepEntityInstance instance) {
    return representationResolver.resolvePropertyDefinitionRepresentation(instance);
  }

  StepAbstractVariable resolveAbstractVariable(StepEntityInstance instance) {
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

  StepRowVariable resolveRowVariable(StepEntityInstance instance) {
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

  StepScalarVariable resolveScalarVariable(StepEntityInstance instance) {
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

  StepAttributeAssertion resolveAttributeAssertion(StepEntityInstance instance) {
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

  StepForwardChainingRulePremise resolveForwardChainingRulePremise(
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

  StepBackChainingRuleBody resolveBackChainingRuleBody(StepEntityInstance instance) {
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









  StepRepresentationMap resolveRepresentationMap(StepEntityInstance instance) {
    return productResolver.resolveRepresentationMap(instance);
  }

  StepSymbolRepresentationMap resolveSymbolRepresentationMap(
      StepEntityInstance instance) {
    return productResolver.resolveSymbolRepresentationMap(instance);
  }

  StepMappedItem resolveMappedItem(StepEntityInstance instance) {
    return productResolver.resolveMappedItem(instance);
  }






  StepItemDefinedTransformation resolveItemDefinedTransformation(StepEntityInstance instance) {
    return transformationResolver.resolveItemDefinedTransformation(instance);
  }


  StepRepresentationRelationship resolveRepresentationRelationship(StepEntityInstance instance) {
    return representationResolver.resolveRepresentationRelationship(instance);
  }

  StepRepresentationRelationship resolveRepresentationRelationship(StepEntityInstance instance, String entityName) {
    return representationResolver.resolveRepresentationRelationship(instance, entityName);
  }



  StepGlobalUnitAssignedContext resolveGlobalUnitAssignedContext(
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

  StepGlobalUncertaintyAssignedContext resolveGlobalUncertaintyAssignedContext(
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

  StepNextAssemblyUsageOccurrence resolveNextAssemblyUsageOccurrence(
      StepEntityInstance instance) {
    return productResolver.resolveNextAssemblyUsageOccurrence(instance);
  }

  StepContextDependentShapeRepresentation resolveContextDependentShapeRepresentation(
      StepEntityInstance instance) {
    return productResolver.resolveContextDependentShapeRepresentation(instance);
  }


  StepTypedMeasureWithUnit resolveTypedMeasureWithUnit(StepEntityInstance instance, String entityName, String expectedUnitKind) {
    return unitResolver.resolveTypedMeasureWithUnit(instance, entityName, expectedUnitKind);
  }






  StepNamedUnit resolveStandaloneUnitKind(StepEntityInstance instance, String entityName) {
    return unitResolver.resolveStandaloneUnitKind(instance, entityName);
  }


  StepConversionBasedUnit resolveConversionBasedUnit(StepEntityInstance instance, String entityName) {
    return unitResolver.resolveConversionBasedUnit(instance, entityName);
  }


  StepDerivedUnit resolveStandaloneDerivedUnitKind(StepEntityInstance instance, String entityName) {
    return unitResolver.resolveStandaloneDerivedUnitKind(instance, entityName);
  }


  void validateNamedUnitDimensions(StepEntityInstance instance) {
    if (!instance.hasDefinition("NAMED_UNIT")) {
      return;
    }
    StepEntityDefinition definition = definition(instance, "NAMED_UNIT");
    requireParameterCount(instance, definition, 1);
    StepValue dimensions = unwrapTyped(definition.parameters().get(0));
    if (StepResolverValueHelpers.isUnset(dimensions)) {
      return;
    }
    if (!(dimensions instanceof StepValue.ReferenceValue)) {
      throw new UnsupportedStepEntityException(
          "NAMED_UNIT dimensions must be omitted, not provided or reference DIMENSIONAL_EXPONENTS");
    }
    StepValue.ReferenceValue referenceValue = (StepValue.ReferenceValue) dimensions;
    requireEntity(
        referenceValue.id(),
        StepDimensionalExponents.class,
        "NAMED_UNIT dimensions must reference DIMENSIONAL_EXPONENTS");
  }

  StepRepresentation resolveRepresentation(StepEntityInstance instance, boolean shapeRepresentation) {
    return representationResolver.resolveRepresentation(instance, shapeRepresentation);
  }

  StepRepresentation resolveRepresentation(StepEntityInstance instance, String entityName, boolean shapeRepresentation) {
    return representationResolver.resolveRepresentation(instance, entityName, shapeRepresentation);
  }

  StepRepresentationItem resolveRepresentationItem(StepEntityInstance instance) {
    return representationResolver.resolveRepresentationItem(instance);
  }

  StepGeometricRepresentationItem resolveGeometricRepresentationItem(StepEntityInstance instance) {
    return representationResolver.resolveGeometricRepresentationItem(instance);
  }

  StepPoint resolvePoint(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "POINT");
    requireParameterCount(instance, definition, 0);
    return new StepPoint(instance.id(), inheritedRepresentationItemName(instance));
  }

  StepCurve resolveCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CURVE");
    requireParameterCount(instance, definition, 0);
    return new StepCurve(instance.id(), inheritedRepresentationItemName(instance));
  }


  StepBoundedCurve resolveBoundedCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BOUNDED_CURVE");
    requireParameterCount(instance, definition, 0);
    return new StepBoundedCurve(instance.id(), inheritedRepresentationItemName(instance));
  }










  ResolvedBSplineCurveData resolveInheritedBSplineCurveData(StepEntityInstance instance) {
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

  ResolvedBSplineSurfaceData resolveInheritedBSplineSurfaceData(StepEntityInstance instance) {
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





  StepCsgPrimitive resolveCsgPrimitive(StepEntityInstance instance,
      String entityName,
      Class<? extends StepEntity> positionType,
      String positionTypeName,
      int dimensionCount) {
    return solidResolver.resolveCsgPrimitive(instance, entityName, positionType, positionTypeName, dimensionCount);
  }



  StepProfileDef resolveParameterizedProfileDef(StepEntityInstance instance, String entityName, int parameterCount) {
    return profileResolver.resolveParameterizedProfileDef(instance, entityName, parameterCount);
  }

  StepProfileDef resolveArbitraryClosedProfileDef(StepEntityInstance instance) {
    return profileResolver.resolveArbitraryClosedProfileDef(instance);
  }


  StepProfileDef resolveArbitraryProfileDef(StepEntityInstance instance, String entityName) {
    return profileResolver.resolveArbitraryProfileDef(instance, entityName);
  }


  StepProfileDef resolveProfileDefSubtype(StepEntityInstance instance, StepEntityDefinition concrete) {
    return profileResolver.resolveProfileDefSubtype(instance, concrete);
  }

  boolean isSupportedArbitraryProfileCurve(StepEntity curve) {
    return curve instanceof StepCurve
        || curve instanceof StepPolyline
        || curve instanceof StepCompositeCurve;
  }



  StepBoxDomain resolveBoxDomain(StepEntityInstance instance) {
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



  StepEntity requireSurfaceReference(
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


  StepVertex resolveVertex(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "VERTEX");
    requireParameterCount(instance, definition, 0);
    return new StepVertex(instance.id(), StepResolverValueHelpers.inheritedTopologicalRepresentationItemName(instance));
  }

  StepEdge resolveEdge(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "EDGE");
    requireParameterCount(instance, definition, 0);
    return new StepEdge(instance.id(), StepResolverValueHelpers.inheritedTopologicalRepresentationItemName(instance));
  }

  StepFace resolveFace(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FACE");
    requireParameterCount(instance, definition, 0);
    return new StepFace(instance.id(), StepResolverValueHelpers.inheritedTopologicalRepresentationItemName(instance));
  }






  StepPreDefinedItem resolvePreDefinedItem(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRE_DEFINED_ITEM");
    requireParameterCount(instance, definition, 1);
    return new StepPreDefinedItem(instance.id(), stringValue(instance, definition, 0));
  }


  StepPreDefinedSymbol resolvePreDefinedSymbol(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRE_DEFINED_SYMBOL");
    requireParameterCount(instance, definition, 1);
    return new StepPreDefinedSymbol(instance.id(), stringValue(instance, definition, 0));
  }

  StepPreDefinedPointMarkerSymbol resolvePreDefinedPointMarkerSymbol(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRE_DEFINED_POINT_MARKER_SYMBOL");
    requireParameterCount(instance, definition, 1);
    return new StepPreDefinedPointMarkerSymbol(instance.id(), stringValue(instance, definition, 0));
  }

  StepPreDefinedDimensionSymbol resolvePreDefinedDimensionSymbol(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRE_DEFINED_DIMENSION_SYMBOL");
    requireParameterCount(instance, definition, 1);
    return new StepPreDefinedDimensionSymbol(instance.id(), stringValue(instance, definition, 0));
  }

  StepPreDefinedGeometricalToleranceSymbol resolvePreDefinedGeometricalToleranceSymbol(
      StepEntityInstance instance) {
    StepEntityDefinition definition =
        definition(instance, "PRE_DEFINED_GEOMETRICAL_TOLERANCE_SYMBOL");
    requireParameterCount(instance, definition, 1);
    return new StepPreDefinedGeometricalToleranceSymbol(
        instance.id(), stringValue(instance, definition, 0));
  }















  StepTextStyleWithJustification resolveTextStyleWithJustification(
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


  StepTextStyleWithBoxCharacteristics resolveTextStyleWithBoxCharacteristics(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TEXT_STYLE_WITH_BOX_CHARACTERISTICS");
    requireParameterCount(instance, definition, 3);
    StepEntity characterAppearance = resolve(referenceId(instance, definition, 1));
    if (!(characterAppearance instanceof StepTextStyleForDefinedFont)) {
      throw new UnsupportedStepEntityException(
          "TEXT_STYLE_WITH_BOX_CHARACTERISTICS character_appearance must reference TEXT_STYLE_FOR_DEFINED_FONT");
    }
    List<String> boxCharacteristics = StepResolverValueHelpers.literalList(instance, definition, 2);
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






  StepCurveStyle requireCurveStyleReference(
      StepEntityInstance instance, StepEntityDefinition definition, String entityName) {
    return requireEntity(
        referenceId(instance, definition, 0),
        StepCurveStyle.class,
        entityName + " style must reference CURVE_STYLE");
  }












  StepPresentationStyleAssignment resolvePresentationStyleAssignment(
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



  StepPresentationLayerAssignment resolvePresentationLayerAssignment(
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









  StepAnnotationOccurrenceRelationship resolveAnnotationOccurrenceRelationship(StepEntityInstance instance) {
    return draughtingResolver.resolveAnnotationOccurrenceRelationship(instance);
  }

  StepAnnotationOccurrenceRelationship resolveAnnotationOccurrenceRelationship(StepEntityInstance instance, String entityName) {
    return draughtingResolver.resolveAnnotationOccurrenceRelationship(instance, entityName);
  }









  StepEntity requireSupportedPlaceholderItem(StepEntity item) {
    if (!isSupportedAnnotationPlaneElement(item)) {
      throw new UnsupportedStepEntityException(
          "ANNOTATION_PLACEHOLDER_OCCURRENCE item must reference supported point carriers or point-like annotation content/occurrences");
    }
    return item;
  }


  StepGeometricCurveSet resolveGeometricCurveSet(StepEntityInstance instance) {
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


  StepClothoid resolveClothoid(StepEntityInstance instance) {
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






  StepHyperbola2D resolveHyperbola2D(StepEntityInstance instance) {
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

  StepParabola2D resolveParabola2D(StepEntityInstance instance) {
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



  StepTrimmedCurve2D resolveTrimmedCurve2D(StepEntityInstance instance) {
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

  StepCompositeCurve2D resolveCompositeCurve2D(StepEntityInstance instance) {
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






  StepPiecewiseBezierCurve2D resolvePiecewiseBezierCurve2D(StepEntityInstance instance) {
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



  StepBoundedCurve2D resolveBoundedCurve2D(StepEntityInstance instance) {
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


  StepSweptAreaSolid resolveSweptAreaSolid(StepEntityInstance instance, String entityName) {
    return solidResolver.resolveSweptAreaSolid(instance, entityName);
  }


  StepEdgeWire resolveEdgeWire(StepEntityInstance instance) {
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




  StepLineSegment resolveLineSegment(StepEntityInstance instance) {
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



  StepSubface resolveSubface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SUBFACE");
    requireParameterCount(instance, definition, 3);
    StepEntity faceElement = resolve(referenceId(instance, definition, 1));
    if (!StepResolverValueHelpers.isSupportedSurfaceReference(faceElement)) {
      throw new UnsupportedStepEntityException(
          "SUBFACE face_element must reference a supported surface");
    }
    return new StepSubface(
        instance.id(),
        stringValue(instance, definition, 0),
        faceElement);
  }

  StepOrientedSubface resolveOrientedSubface(StepEntityInstance instance) {
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









  // Advanced CSG volume resolve methods






  // Swept face solid resolve methods



  StepSweptFaceSolid resolveSweptFaceSolid(StepEntityInstance instance, String entityName) {
    return solidResolver.resolveSweptFaceSolid(instance, entityName);
  }

  StepAdvancedBrep resolveAdvancedBrep(StepEntityInstance instance) {
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

  StepComplexClippingResult resolveComplexClippingResult(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COMPLEX_CLIPPING_RESULT");
    requireParameterCount(instance, definition, 4);
    return new StepComplexClippingResult(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        enumValue(instance, definition, 3));
  }

  StepCompositeText resolveCompositeText(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COMPOSITE_TEXT");
    requireParameterCount(instance, definition, 2);
    List<StepEntity> collection =
        entityReferenceList(
            instance, definition, 1, "COMPOSITE_TEXT collection must contain entity references");
    return new StepCompositeText(
        instance.id(), stringValue(instance, definition, 0), collection);
  }

  StepTextLiteral resolveTextLiteral(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TEXT_LITERAL");
    requireParameterCount(instance, definition, 3);
    return new StepTextLiteral(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepComposedText resolveComposedText(StepEntityInstance instance) {
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

  StepTessellatedFaceSet resolveTessellatedFaceSet(StepEntityInstance instance) {
    return tessellationResolver.resolveTessellatedFaceSet(instance);
  }

  StepSeamEdge resolveSeamEdge(StepEntityInstance instance) {
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




  boolean isSupportedAnnotationCurveCarrier(StepEntity item) {
    return StepResolverValueHelpers.isSupportedCurveReference(item)
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

  boolean isSupportedAnnotationPlaneElement(StepEntity item) {
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

  boolean isSupportedAnnotationPointCarrier(StepEntity item) {
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
        || (item instanceof StepGeometricReplica
            && "POINT_REPLICA".equals(((StepGeometricReplica) item).entityName()));
  }

  boolean isSupportedAnnotationWrapperItem(StepEntity item) {
    return item instanceof StepAnnotationSymbol
        || item instanceof StepAnnotationText
        || item instanceof StepAnnotationTextCharacter
        || item instanceof StepAnnotationFillArea
        || StepResolverValueHelpers.isAnnotationOccurrence(item);
  }

  StepGeometricSet resolveGeometricSet(StepEntityInstance instance) {
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

  boolean isSupportedGeometricCurveSetElement(StepEntity element) {
    return StepResolverValueHelpers.isSupportedCurveReference(element)
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

  boolean isSupportedGeometricSetElement(StepEntity element) {
    return isSupportedGeometricCurveSetElement(element)
        || StepResolverValueHelpers.isSupportedSurfaceReference(element)
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

  static boolean isPointLikeSetElement(StepEntity element) {
    return element instanceof StepCartesianPoint
        || element instanceof StepVertexPoint
        || (element instanceof StepGeometricReplica
            && "POINT_REPLICA".equals(((StepGeometricReplica) element).entityName()));
  }

  StepPointSet resolvePointSet(StepEntityInstance instance) {
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


  StepAngularLocation resolveAngularLocation(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ANGULAR_LOCATION");
    requireParameterCount(instance, definition, 4);
    return new StepAngularLocation(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  StepDraughtingCallout resolveDraughtingCallout(StepEntityInstance instance) {
    return annotationResolver.resolveDraughtingCallout(instance);
  }

  StepDraughtingCallout resolveDraughtingCallout(StepEntityInstance instance, String entityName) {
    return annotationResolver.resolveDraughtingCallout(instance, entityName);
  }


  StepDraughtingCalloutRelationship resolveDraughtingCalloutRelationship(StepEntityInstance instance, String entityName) {
    return annotationResolver.resolveDraughtingCalloutRelationship(instance, entityName);
  }









  StepPlacedTarget resolvePlacedTarget(StepEntityInstance instance) {
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




  // Manufacturing resolvers

  StepFlatPattern resolveFlatPattern(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FLAT_PATTERN");
    requireParameterCount(instance, definition, 6);
    return new StepFlatPattern(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        entityReferenceList(instance, definition, 2,
            "FLAT_PATTERN bendLines must contain entity references"),
        entityReferenceList(instance, definition, 3,
            "FLAT_PATTERN formingFeatures must contain entity references"),
        resolve(referenceId(instance, definition, 4)),
        entityReferenceList(instance, definition, 5,
            "FLAT_PATTERN unfoldingSequence must contain entity references"));
  }


  StepShapeAspectOccurrence resolveShapeAspectOccurrence(StepEntityInstance instance) {
    return representationResolver.resolveShapeAspectOccurrence(instance);
  }

  // Package-private shims retained for the stateless value helpers that have
  // more than 30 call sites across the resolver package. The bodies live
  // verbatim in StepResolverValueHelpers; these wrappers only forward.

  StepEntityDefinition definition(StepEntityInstance instance, String name) {
    return StepResolverValueHelpers.definition(instance, name);
  }

  static void requireParameterCount(
      StepEntityInstance instance, StepEntityDefinition definition, int expected) {
    StepResolverValueHelpers.requireParameterCount(instance, definition, expected);
  }

  static void requireParameterCountIn(
      StepEntityInstance instance, StepEntityDefinition definition, int... expectedCounts) {
    StepResolverValueHelpers.requireParameterCountIn(instance, definition, expectedCounts);
  }

  String stringValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    return StepResolverValueHelpers.stringValue(instance, definition, index);
  }

  String optionalStringValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    return StepResolverValueHelpers.optionalStringValue(instance, definition, index);
  }

  double numberValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    return StepResolverValueHelpers.numberValue(instance, definition, index);
  }

  Double optionalNumberValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    return StepResolverValueHelpers.optionalNumberValue(instance, definition, index);
  }

  int integerValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    return StepResolverValueHelpers.integerValue(instance, definition, index);
  }

  String enumValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    return StepResolverValueHelpers.enumValue(instance, definition, index);
  }

  boolean booleanValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    return StepResolverValueHelpers.booleanValue(instance, definition, index);
  }

  int referenceId(StepEntityInstance instance, StepEntityDefinition definition, int index) {
    return StepResolverValueHelpers.referenceId(instance, definition, index);
  }

  List<Double> numberList(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    return StepResolverValueHelpers.numberList(instance, definition, index);
  }

  List<Integer> integerList(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    return StepResolverValueHelpers.integerList(instance, definition, index);
  }

  StepValue unwrapTyped(StepValue value) {
    return StepResolverValueHelpers.unwrapTyped(value);
  }

  String inheritedRepresentationItemName(StepEntityInstance instance) {
    return StepResolverValueHelpers.inheritedRepresentationItemName(instance);
  }

  StepDirection optionalDirectionReference(
      StepEntityInstance instance, StepEntityDefinition definition, int index, String message) {
    StepValue value = definition.parameters().get(index);
    if (StepResolverValueHelpers.isUnset(value)) {
      return null;
    }
    return requireEntity(referenceId(instance, definition, index), StepDirection.class, message);
  }

  StepEntity tryResolveReference(StepValue value) {
    value = unwrapTyped(value);
    if (value instanceof StepValue.OmittedValue || value instanceof StepValue.NotProvidedValue) {
      return null;
    }
    if (value instanceof StepValue.ReferenceValue) {
      StepValue.ReferenceValue referenceValue = (StepValue.ReferenceValue) value;
      return resolve(referenceValue.id());
    }
    throw new StepResolutionException("parameter must be a reference or omit/not-provided");
  }

  <T extends StepEntity> List<List<T>> referenceGrid(
      StepEntityInstance instance,
      StepEntityDefinition definition,
      int index,
      Class<T> type,
      String message) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue)) {
      throw StepParameterReader.parameterTypeMismatch(instance, definition, index, "nested list");
    }
    StepValue.ListValue outerList = (StepValue.ListValue) value;
    List<List<T>> grid = new ArrayList<>(outerList.elements().size());
    for (StepValue rowValue : outerList.elements()) {
      StepValue row = unwrapTyped(rowValue);
      if (!(row instanceof StepValue.ListValue)) {
        throw StepResolverValueHelpers.parameterElementTypeMismatch(instance, definition, index, "nested reference list", rowValue);
      }
      StepValue.ListValue rowList = (StepValue.ListValue) row;
      List<T> entries = new ArrayList<>(rowList.elements().size());
      for (StepValue element : rowList.elements()) {
        StepValue unwrapped = unwrapTyped(element);
        if (!(unwrapped instanceof StepValue.ReferenceValue)) {
          throw StepResolverValueHelpers.parameterElementTypeMismatch(instance, definition, index, "reference", element);
        }
        StepValue.ReferenceValue referenceValue = (StepValue.ReferenceValue) unwrapped;
        entries.add(requireEntity(referenceValue.id(), type, message));
      }
      grid.add(List.copyOf(entries));
    }
    return List.copyOf(grid);
  }

  // ---------------------------------------------------------------------------
  // Core value helpers
  // ---------------------------------------------------------------------------

  <T extends StepEntity> T requireEntity(int id, Class<T> type, String message) {
    StepEntity entity = resolve(id);
    if (!type.isInstance(entity)) {
      throw new StepResolutionException(message + " but got " + entity.getClass().getSimpleName());
    }
    return type.cast(entity);
  }

  boolean isSupportedAnnotationUsageItem(StepEntity entity) {
    return entity instanceof StepDraughtingCallout || isSupportedAnnotationWrapperItem(entity);
  }

  boolean isSupportedGeometricUsageIdentifiedItem(StepEntity entity) {
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

  boolean isSupportedAssociationUsageIdentifiedItem(StepEntity entity) {
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

  <T extends StepEntity> List<T> referenceList(
      StepEntityInstance instance,
      StepEntityDefinition definition,
      int index,
      Class<T> type,
      String message) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    List<T> result = new ArrayList<>();
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.ReferenceValue)) {
        throw new StepResolutionException(message);
      }
      StepValue.ReferenceValue referenceValue = (StepValue.ReferenceValue) unwrapped;
      result.add(requireEntity(referenceValue.id(), type, message));
    }
    return List.copyOf(result);
  }

  List<StepEntity> entityReferenceList(
      StepEntityInstance instance, StepEntityDefinition definition, int index, String message) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    List<StepEntity> result = new ArrayList<>();
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.ReferenceValue)) {
        throw new StepResolutionException(message);
      }
      StepValue.ReferenceValue referenceValue = (StepValue.ReferenceValue) unwrapped;
      result.add(resolve(referenceValue.id()));
    }
    return List.copyOf(result);
  }

  static void registerProductDefinitionRelationshipAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName,
          (resolver, instance) ->
              resolver.resolveProductDefinitionRelationship(instance, entityName));
    }
  }

  static void registerProductDefinitionRelationshipRelationshipAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName,
          (resolver, instance) ->
              resolver.resolveProductDefinitionRelationshipRelationship(instance, entityName));
    }
  }

  static void registerRepresentationAliases(
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
    StepEntityRegistry.registerAll(registry);
    return registry;
  }

  // Phase 2: Additional unit resolver methods

  /**
   * Resolve EXTERNALLY_DEFINED_CONVERSION_BASED_UNIT entity.
   */
  StepExternallyDefinedConversionBasedUnit resolveExternallyDefinedConversionBasedUnit(StepEntityInstance instance) {
    return unitResolver.resolveExternallyDefinedConversionBasedUnit(instance);
  }

  /**
   * Resolve NON_AGREED_UNIT_USAGE entity.
   */
  StepNonAgreedUnitUsage resolveNonAgreedUnitUsage(StepEntityInstance instance) {
    return unitResolver.resolveNonAgreedUnitUsage(instance);
  }

  // Phase 2 Batch 2: A3M Validation entities resolver methods

  StepA3mEquivalenceAccuracyAssociation resolveA3mEquivalenceAccuracyAssociation(StepEntityInstance instance) {
    return associationResolver.resolveA3mEquivalenceAccuracyAssociation(instance);
  }

  StepA3mInspectedModelAndInspectionResultRelationship resolveA3mInspectedModelAndInspectionResultRelationship(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "A3M_INSPECTED_MODEL_AND_INSPECTION_RESULT_RELATIONSHIP");
    requireParameterCount(instance, definition, 2);
    return new StepA3mInspectedModelAndInspectionResultRelationship(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }

  StepA3maEquivalenceInspectionResult resolveA3maEquivalenceInspectionResult(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "A3MA_EQUIVALENCE_INSPECTION_RESULT");
    requireParameterCount(instance, definition, 4);
    String description = optionalStringValue(instance, definition, 1);
    return new StepA3maEquivalenceInspectionResult(
        instance.id(),
        stringValue(instance, definition, 0),
        description,
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  StepA3msEquivalenceInspectionResult resolveA3msEquivalenceInspectionResult(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "A3MS_EQUIVALENCE_INSPECTION_RESULT");
    requireParameterCount(instance, definition, 4);
    String description = optionalStringValue(instance, definition, 1);
    return new StepA3msEquivalenceInspectionResult(
        instance.id(),
        stringValue(instance, definition, 0),
        description,
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  StepA3mEquivalenceCriterion resolveA3mEquivalenceCriterion(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "A3M_EQUIVALENCE_CRITERION");
    requireParameterCount(instance, definition, 7);
    return new StepA3mEquivalenceCriterion(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        StepResolverValueHelpers.literalList(instance, definition, 2),
        StepResolverValueHelpers.literalList(instance, definition, 3),
        resolve(referenceId(instance, definition, 4)),
        StepResolverValueHelpers.literalList(instance, definition, 5),
        StepResolverValueHelpers.literalList(instance, definition, 6));
  }

  // Phase 2 Batch 2: Helper resolver methods for A3M validation entities

  StepRepresentationItemRelationship resolveRepresentationItemRelationship(StepEntityInstance instance, String entityName) {
    return representationResolver.resolveRepresentationItemRelationship(instance, entityName);
  }

  StepDataEquivalenceAssessmentSpecification resolveDataEquivalenceAssessmentSpecification(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 3);
    String description = optionalStringValue(instance, definition, 1);
    return new StepDataEquivalenceAssessmentSpecification(
        instance.id(),
        stringValue(instance, definition, 0),
        description,
        entityName);
  }

  StepDataEquivalenceInspectionCriterionReportItem resolveDataEquivalenceInspectionCriterionReportItem(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    // Note: This entity is subtype of representation_item with additional attributes
    // We use simplified extraction for now
    requireParameterCount(instance, definition, 2);
    return new StepDataEquivalenceInspectionCriterionReportItem(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        entityName);
  }

  StepDataEquivalenceInspectionInstanceReportItem resolveDataEquivalenceInspectionInstanceReportItem(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 2);
    return new StepDataEquivalenceInspectionInstanceReportItem(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        entityName);
  }

  StepDataEquivalenceInspectionRequirement resolveDataEquivalenceInspectionRequirement(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 2);
    return new StepDataEquivalenceInspectionRequirement(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        entityName);
  }

  StepDataEquivalenceReportRequest resolveDataEquivalenceReportRequest(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 2);
    return new StepDataEquivalenceReportRequest(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        entityName);
  }

  // Phase 2 Batch 3: Mathematical function and expression resolver methods

  StepUnaryGenericExpression resolveUnaryGenericExpression(StepEntityInstance instance, String entityName) {
    return genericResolver.resolveUnaryGenericExpression(instance, entityName);
  }

  StepBinaryGenericExpression resolveBinaryGenericExpression(StepEntityInstance instance, String entityName) {
    return genericResolver.resolveBinaryGenericExpression(instance, entityName);
  }

  StepMultipleArityGenericExpression resolveMultipleArityGenericExpression(StepEntityInstance instance, String entityName) {
    return genericResolver.resolveMultipleArityGenericExpression(instance, entityName);
  }

  StepSimpleGenericExpression resolveSimpleGenericExpression(StepEntityInstance instance, String entityName) {
    return genericResolver.resolveSimpleGenericExpression(instance, entityName);
  }

  static final class ResolvedBSplineCurveData {
      private final String name;
      private final int degree;
      private final List<StepCartesianPoint> controlPoints;
      private final String curveForm;
      private final boolean closedCurve;
      private final boolean selfIntersect;

      ResolvedBSplineCurveData(String name, int degree, List<StepCartesianPoint> controlPoints,
                                String curveForm, boolean closedCurve, boolean selfIntersect) {
          this.name = name;
          this.degree = degree;
          this.controlPoints = controlPoints == null ? null : List.copyOf(controlPoints);
          this.curveForm = curveForm;
          this.closedCurve = closedCurve;
          this.selfIntersect = selfIntersect;
      }

      String name() { return name; }
      int degree() { return degree; }
      List<StepCartesianPoint> controlPoints() { return controlPoints; }
      String curveForm() { return curveForm; }
      boolean closedCurve() { return closedCurve; }
      boolean selfIntersect() { return selfIntersect; }

      // Java Bean getters
      String getName() { return name; }
      int getDegree() { return degree; }
      List<StepCartesianPoint> getControlPoints() { return controlPoints; }
      String getCurveForm() { return curveForm; }
      boolean isClosedCurve() { return closedCurve; }
      boolean isSelfIntersect() { return selfIntersect; }
  }

  static final class ResolvedBSplineSurfaceData {
      private final String name;
      private final int uDegree;
      private final int vDegree;
      private final List<List<StepCartesianPoint>> controlPoints;
      private final String surfaceForm;
      private final boolean uClosed;
      private final boolean vClosed;
      private final boolean selfIntersect;

      ResolvedBSplineSurfaceData(String name, int uDegree, int vDegree,
                                  List<List<StepCartesianPoint>> controlPoints,
                                  String surfaceForm, boolean uClosed, boolean vClosed, boolean selfIntersect) {
          this.name = name;
          this.uDegree = uDegree;
          this.vDegree = vDegree;
          this.controlPoints = controlPoints == null ? null : controlPoints.stream()
              .map(row -> row == null ? null : List.copyOf(row))
              .collect(Collectors.toList());
          this.surfaceForm = surfaceForm;
          this.uClosed = uClosed;
          this.vClosed = vClosed;
          this.selfIntersect = selfIntersect;
      }

      String name() { return name; }
      int uDegree() { return uDegree; }
      int vDegree() { return vDegree; }
      List<List<StepCartesianPoint>> controlPoints() { return controlPoints; }
      String surfaceForm() { return surfaceForm; }
      boolean uClosed() { return uClosed; }
      boolean vClosed() { return vClosed; }
      boolean selfIntersect() { return selfIntersect; }

      // Java Bean getters
      String getName() { return name; }
      int getUDegree() { return uDegree; }
      int getVDegree() { return vDegree; }
      List<List<StepCartesianPoint>> getControlPoints() { return controlPoints; }
      String getSurfaceForm() { return surfaceForm; }
      boolean isUClosed() { return uClosed; }
      boolean isVClosed() { return vClosed; }
      boolean isSelfIntersect() { return selfIntersect; }
  }

  // Phase 2 Batch 4-10: Generic helper resolver methods for alias families

  StepEntity resolveGenericAssignment(StepEntityInstance instance, String entityName) {
    return genericResolver.resolveGenericAssignment(instance, entityName);
  }

  StepEntity resolveGenericRelationship(StepEntityInstance instance, String entityName) {
    return genericResolver.resolveGenericRelationship(instance, entityName);
  }

  StepEntity resolveGenericRequirement(StepEntityInstance instance, String entityName) {
    return genericResolver.resolveGenericRequirement(instance, entityName);
  }

  StepEntity resolveGenericStatus(StepEntityInstance instance, String entityName) {
    return genericResolver.resolveGenericStatus(instance, entityName);
  }

  StepEntity resolveGenericProperty(StepEntityInstance instance, String entityName) {
    return genericResolver.resolveGenericProperty(instance, entityName);
  }

  StepEntity resolveGenericSetup(StepEntityInstance instance, String entityName) {
    return genericResolver.resolveGenericSetup(instance, entityName);
  }

  StepEntity resolveGenericType(StepEntityInstance instance, String entityName) {
    return genericResolver.resolveGenericType(instance, entityName);
  }

  StepEntity resolveGenericRole(StepEntityInstance instance, String entityName) {
    return genericResolver.resolveGenericRole(instance, entityName);
  }

  StepEntity resolveGenericActual(StepEntityInstance instance, String entityName) {
    return genericResolver.resolveGenericActual(instance, entityName);
  }

}
