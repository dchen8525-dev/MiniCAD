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

  final Map<Integer, StepEntityInstance> instancesById;
  private final Map<Integer, StepEntity> resolved = new LinkedHashMap<>();
  private final Deque<Integer> resolutionStack = new ArrayDeque<>();
  private final StepTopologyResolver topologyResolver;
  private final StepProductResolver productResolver;
  private final GeometryResolver geometryResolver;
  private final SurfaceResolver surfaceResolver;
  private final GeometricFeatureResolver geometricFeatureResolver;
  private final AnalysisResolver analysisResolver;
  private final AnnotationResolver annotationResolver;
  private final MaterialResolver materialResolver;
  private final UnitResolver unitResolver;
  private final KinematicResolver kinematicResolver;
  private final TessellationResolver tessellationResolver;
  private final AssignmentResolver assignmentResolver;
  private final RepresentationResolver representationResolver;
  private final BSplineResolver bSplineResolver;
  private final BezierResolver bezierResolver;
  private final SolidResolver solidResolver;
  private final ProfileResolver profileResolver;
  private final MachiningResolver machiningResolver;
  private final TransformationResolver transformationResolver;
  private final GenericResolver genericResolver;
  private final AssociationResolver associationResolver;
  private final PropertyResolver propertyResolver;
  private final CurveResolver curveResolver;
  private final DraughtingResolver draughtingResolver;
  private final ManufacturingFeatureResolver manufacturingFeatureResolver;
  private final VisualizationResolver visualizationResolver;
  private final BoundaryConditionResolver boundaryConditionResolver;
  private final FeaElementResolver feaElementResolver;

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
    return Map.copyOf(resolved);
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
      resolutionStack.push(id);
      try {
        StepEntity entity = factory.create(this, instance);
        resolved.put(id, entity);
        return entity;
      } finally {
        resolutionStack.pop();
      }
    }

    throw new UnsupportedStepEntityException(
        "unsupported STEP entity #" + instance.id() + " " + instance.name());
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

  StepDirection resolveDirection(StepEntityInstance instance) {
    return geometryResolver.resolveDirection(instance);
  }

  StepVector resolveVector(StepEntityInstance instance) {
    return geometryResolver.resolveVector(instance);
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

  StepLine resolveLine(StepEntityInstance instance) {
    return geometryResolver.resolveLine(instance);
  }

  StepPolyline resolvePolyline(StepEntityInstance instance) {
    return geometryResolver.resolvePolyline(instance);
  }

  StepPlane resolvePlane(StepEntityInstance instance) {
    return surfaceResolver.resolvePlane(instance);
  }

  StepCircle resolveCircle(StepEntityInstance instance) {
    return geometryResolver.resolveCircle(instance);
  }

  StepEllipse resolveEllipse(StepEntityInstance instance) {
    return geometryResolver.resolveEllipse(instance);
  }

  StepConicCurve resolveConicCurve(StepEntityInstance instance, String entityName, int parameterCount) {
    return curveResolver.resolveConicCurve(instance, entityName, parameterCount);
  }

  StepCylindricalSurface resolveCylindricalSurface(StepEntityInstance instance) {
    return surfaceResolver.resolveCylindricalSurface(instance);
  }

  StepConicalSurface resolveConicalSurface(StepEntityInstance instance) {
    return surfaceResolver.resolveConicalSurface(instance);
  }

  StepToroidalSurface resolveToroidalSurface(StepEntityInstance instance) {
    return surfaceResolver.resolveToroidalSurface(instance);
  }

  StepDegenerateToroidalSurface resolveDegenerateToroidalSurface(
      StepEntityInstance instance) {
    return surfaceResolver.resolveDegenerateToroidalSurface(instance);
  }

  StepSphericalSurface resolveSphericalSurface(StepEntityInstance instance) {
    return surfaceResolver.resolveSphericalSurface(instance);
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

  StepParaboloidSurface resolveParaboloidSurface(StepEntityInstance instance) {
    return surfaceResolver.resolveParaboloidSurface(instance);
  }

  StepHyperboloidSurface resolveHyperboloidSurface(StepEntityInstance instance) {
    return surfaceResolver.resolveHyperboloidSurface(instance);
  }

  StepChamferEdge resolveChamferEdge(StepEntityInstance instance) {
    return geometricFeatureResolver.resolveChamferEdge(instance);
  }

  StepFilletEdge resolveFilletEdge(StepEntityInstance instance) {
    return geometricFeatureResolver.resolveFilletEdge(instance);
  }

  StepBlendedSurface resolveBlendedSurface(StepEntityInstance instance) {
    return geometricFeatureResolver.resolveBlendedSurface(instance);
  }

  StepFreeFormSurface resolveFreeFormSurface(StepEntityInstance instance) {
    return geometricFeatureResolver.resolveFreeFormSurface(instance);
  }

  List<List<StepEntity>> resolveFreeFormControlPoints(StepEntityInstance instance, StepEntityDefinition definition, int index) {
    return curveResolver.resolveFreeFormControlPoints(instance, definition, index);
  }

  StepCurvedToleranceZone resolveCurvedToleranceZone(StepEntityInstance instance) {
    return geometricFeatureResolver.resolveCurvedToleranceZone(instance);
  }

  StepSurfaceQuality resolveSurfaceQuality(StepEntityInstance instance) {
    return geometricFeatureResolver.resolveSurfaceQuality(instance);
  }

  StepMeasurementPoint resolveMeasurementPoint(StepEntityInstance instance) {
    return unitResolver.resolveMeasurementPoint(instance);
  }

  StepSurfaceMeasurement resolveSurfaceMeasurement(StepEntityInstance instance) {
    return unitResolver.resolveSurfaceMeasurement(instance);
  }

  StepSurfaceTextureRepresentationItem resolveSurfaceTextureRepresentationItem(StepEntityInstance instance) {
    return representationResolver.resolveSurfaceTextureRepresentationItem(instance);
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
      validParent = isSupportedCurveReference(parent);
    } else if ("SURFACE_REPLICA".equals(entityName)) {
      validParent = isSupportedSurfaceReference(parent);
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

  StepRectangularTrimmedSurface resolveRectangularTrimmedSurface(StepEntityInstance instance) {
    return surfaceResolver.resolveRectangularTrimmedSurface(instance);
  }

  StepCurveBoundedSurface resolveCurveBoundedSurface(StepEntityInstance instance) {
    return surfaceResolver.resolveCurveBoundedSurface(instance);
  }

  StepAnalysisResult resolveAnalysisResult(StepEntityInstance instance) {
    return analysisResolver.resolveAnalysisResult(instance);
  }

  StepAnalysisInstance resolveAnalysisInstance(StepEntityInstance instance) {
    return analysisResolver.resolveAnalysisInstance(instance);
  }

  StepConfigurationInstance resolveConfigurationInstance(StepEntityInstance instance) {
    return analysisResolver.resolveConfigurationInstance(instance);
  }

  StepModelDefinition resolveModelDefinition(StepEntityInstance instance) {
    return analysisResolver.resolveModelDefinition(instance);
  }

  StepModelInstance resolveModelInstance(StepEntityInstance instance) {
    return analysisResolver.resolveModelInstance(instance);
  }

  StepSimulationDefinition resolveSimulationDefinition(StepEntityInstance instance) {
    return analysisResolver.resolveSimulationDefinition(instance);
  }

  StepSimulationInstance resolveSimulationInstance(StepEntityInstance instance) {
    return analysisResolver.resolveSimulationInstance(instance);
  }

  StepOrientedSurface resolveOrientedSurface(StepEntityInstance instance) {
    return bezierResolver.resolveOrientedSurface(instance);
  }

  StepSurfaceOfLinearExtrusion resolveSurfaceOfLinearExtrusion(StepEntityInstance instance) {
    return surfaceResolver.resolveSurfaceOfLinearExtrusion(instance);
  }

  StepSurfaceOfRevolution resolveSurfaceOfRevolution(StepEntityInstance instance) {
    return surfaceResolver.resolveSurfaceOfRevolution(instance);
  }

  StepSurfaceOfTranslation resolveSurfaceOfTranslation(StepEntityInstance instance) {
    return surfaceResolver.resolveSurfaceOfTranslation(instance);
  }

  StepSurfaceOfProjection resolveSurfaceOfProjection(StepEntityInstance instance) {
    return surfaceResolver.resolveSurfaceOfProjection(instance);
  }

  StepOffsetCurve3D resolveOffsetCurve3D(StepEntityInstance instance) {
    return bezierResolver.resolveOffsetCurve3D(instance);
  }

  StepOffsetCurve2D resolveOffsetCurve2D(StepEntityInstance instance) {
    return bezierResolver.resolveOffsetCurve2D(instance);
  }

  StepOrientedCurve resolveOrientedCurve(StepEntityInstance instance) {
    return bezierResolver.resolveOrientedCurve(instance);
  }

  StepOffsetSurface resolveOffsetSurface(StepEntityInstance instance) {
    return bezierResolver.resolveOffsetSurface(instance);
  }

  StepCompositeCurveSegment resolveCompositeCurveSegment(StepEntityInstance instance) {
    return bezierResolver.resolveCompositeCurveSegment(instance);
  }

  StepCompositeCurve resolveCompositeCurve(StepEntityInstance instance) {
    return bezierResolver.resolveCompositeCurve(instance);
  }

  StepCompositeCurveOnSurface resolveCompositeCurveOnSurface(StepEntityInstance instance) {
    return curveResolver.resolveCompositeCurveOnSurface(instance);
  }

  StepCompositeCurveOnSurface resolveCompositeCurveOnSurface(StepEntityInstance instance, String entityName) {
    return curveResolver.resolveCompositeCurveOnSurface(instance, entityName);
  }

  StepTrimmedCurve resolveTrimmedCurve(StepEntityInstance instance) {
    return curveResolver.resolveTrimmedCurve(instance);
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

  StepSeamCurve resolveSeamCurve(StepEntityInstance instance) {
    return curveResolver.resolveSeamCurve(instance);
  }

  StepPcurve resolvePcurve(StepEntityInstance instance) {
    return curveResolver.resolvePcurve(instance);
  }

  StepDegeneratePcurve resolveDegeneratePcurve(StepEntityInstance instance) {
    return curveResolver.resolveDegeneratePcurve(instance);
  }

  StepBSplineCurveWithKnots resolveBSplineCurveWithKnots(StepEntityInstance instance) {
    return bSplineResolver.resolveBSplineCurveWithKnots(instance);
  }

  StepBSplineCurve resolveBSplineCurve(StepEntityInstance instance) {
    return bSplineResolver.resolveBSplineCurve(instance);
  }

  StepRationalBSplineCurve resolveRationalBSplineCurve(StepEntityInstance instance) {
    return bSplineResolver.resolveRationalBSplineCurve(instance);
  }

  StepBSplineSurfaceWithKnots resolveBSplineSurfaceWithKnots(StepEntityInstance instance) {
    return bSplineResolver.resolveBSplineSurfaceWithKnots(instance);
  }

  StepBSplineSurface resolveBSplineSurface(StepEntityInstance instance) {
    return bSplineResolver.resolveBSplineSurface(instance);
  }

  StepBSplineCurveWithKnotsAndBreakpoints resolveBSplineCurveWithKnotsAndBreakpoints(
      StepEntityInstance instance) {
    return bSplineResolver.resolveBSplineCurveWithKnotsAndBreakpoints(instance);
  }

  StepBSplineSurfaceWithKnotsAndBreakpoints resolveBSplineSurfaceWithKnotsAndBreakpoints(
      StepEntityInstance instance) {
    return bSplineResolver.resolveBSplineSurfaceWithKnotsAndBreakpoints(instance);
  }

  StepRationalBSplineSurface resolveRationalBSplineSurface(StepEntityInstance instance) {
    return bSplineResolver.resolveRationalBSplineSurface(instance);
  }

  StepVertexPoint resolveVertexPoint(StepEntityInstance instance) {
    return topologyResolver.resolveVertexPoint(instance);
  }

  StepEdgeCurve resolveEdgeCurve(StepEntityInstance instance) {
    return topologyResolver.resolveEdgeCurve(instance);
  }

  StepOrientedEdge resolveOrientedEdge(StepEntityInstance instance) {
    return topologyResolver.resolveOrientedEdge(instance);
  }

  StepSubedge resolveSubedge(StepEntityInstance instance) {
    return topologyResolver.resolveSubedge(instance);
  }

  StepConnectedEdgeSet resolveConnectedEdgeSet(StepEntityInstance instance) {
    return topologyResolver.resolveConnectedEdgeSet(instance);
  }

  StepEdgeBasedWireframeModel resolveEdgeBasedWireframeModel(StepEntityInstance instance) {
    return topologyResolver.resolveEdgeBasedWireframeModel(instance);
  }

  StepEdgeLoop resolveEdgeLoop(StepEntityInstance instance) {
    return topologyResolver.resolveEdgeLoop(instance);
  }

  StepPath resolvePath(StepEntityInstance instance) {
    return topologyResolver.resolvePath(instance);
  }

  StepOpenPath resolveOpenPath(StepEntityInstance instance) {
    return topologyResolver.resolveOpenPath(instance);
  }

  StepSubpath resolveSubpath(StepEntityInstance instance) {
    return topologyResolver.resolveSubpath(instance);
  }

  StepOrientedPath resolveOrientedPath(StepEntityInstance instance) {
    return topologyResolver.resolveOrientedPath(instance);
  }

  static boolean isPathEntity(StepEntity entity) {
    return StepTopologyResolver.isPathEntity(entity);
  }

  StepVertexLoop resolveVertexLoop(StepEntityInstance instance) {
    return topologyResolver.resolveVertexLoop(instance);
  }

  StepPolyLoop resolvePolyLoop(StepEntityInstance instance) {
    return topologyResolver.resolvePolyLoop(instance);
  }

  StepFaceBound resolveFaceBound(StepEntityInstance instance, boolean outer) {
    return topologyResolver.resolveFaceBound(instance, outer);
  }

  StepAdvancedFace resolveAdvancedFace(StepEntityInstance instance) {
    return topologyResolver.resolveAdvancedFace(instance);
  }

  StepFaceSurface resolveFaceSurface(StepEntityInstance instance) {
    return topologyResolver.resolveFaceSurface(instance);
  }

  StepOrientedFace resolveOrientedFace(StepEntityInstance instance) {
    return topologyResolver.resolveOrientedFace(instance);
  }

  StepOpenShell resolveOpenShell(StepEntityInstance instance) {
    return topologyResolver.resolveOpenShell(instance);
  }

  StepClosedShell resolveClosedShell(StepEntityInstance instance) {
    return topologyResolver.resolveClosedShell(instance);
  }

  StepSurfacedOpenShell resolveSurfacedOpenShell(StepEntityInstance instance) {
    return topologyResolver.resolveSurfacedOpenShell(instance);
  }

  StepOrientedOpenShell resolveOrientedOpenShell(StepEntityInstance instance) {
    return topologyResolver.resolveOrientedOpenShell(instance);
  }

  StepOrientedClosedShell resolveOrientedClosedShell(StepEntityInstance instance) {
    return topologyResolver.resolveOrientedClosedShell(instance);
  }

  StepConnectedFaceSet resolveConnectedFaceSet(StepEntityInstance instance) {
    return topologyResolver.resolveConnectedFaceSet(instance);
  }

  StepConnectedFaceSubSet resolveConnectedFaceSubSet(StepEntityInstance instance) {
    return topologyResolver.resolveConnectedFaceSubSet(instance);
  }

  StepVertexShell resolveVertexShell(StepEntityInstance instance) {
    return topologyResolver.resolveVertexShell(instance);
  }

  StepWireShell resolveWireShell(StepEntityInstance instance) {
    return topologyResolver.resolveWireShell(instance);
  }

  StepManifoldSolidBrep resolveManifoldSolidBrep(StepEntityInstance instance) {
    return topologyResolver.resolveManifoldSolidBrep(instance);
  }

  StepManifoldSolidBrep resolveManifoldSolidBrep(StepEntityInstance instance, String entityName) {
    return topologyResolver.resolveManifoldSolidBrep(instance, entityName);
  }

  StepNonManifoldSolidBrep resolveNonManifoldSolidBrep(StepEntityInstance instance) {
    return topologyResolver.resolveNonManifoldSolidBrep(instance);
  }

  StepFacettedBrep resolveFacettedBrep(StepEntityInstance instance) {
    return topologyResolver.resolveFacettedBrep(instance);
  }

  StepShellBasedSurfaceModel resolveShellBasedSurfaceModel(StepEntityInstance instance) {
    return topologyResolver.resolveShellBasedSurfaceModel(instance);
  }

  StepFaceBasedSurfaceModel resolveFaceBasedSurfaceModel(StepEntityInstance instance) {
    return topologyResolver.resolveFaceBasedSurfaceModel(instance);
  }

  StepManifoldSurfaceModel resolveManifoldSurfaceModel(StepEntityInstance instance) {
    return topologyResolver.resolveManifoldSurfaceModel(instance);
  }

  StepSurfacedEdgeCurve resolveSurfacedEdgeCurve(StepEntityInstance instance) {
    return topologyResolver.resolveSurfacedEdgeCurve(instance);
  }

  boolean isConnectedFaceSetEntity(StepEntity entity) {
    return topologyResolver.isConnectedFaceSetEntity(entity);
  }

  StepGeometricTolerance resolveGeometricTolerance(StepEntityInstance instance) {
    return annotationResolver.resolveGeometricTolerance(instance);
  }

  StepToleranceZoneForm resolveToleranceZoneForm(StepEntityInstance instance) {
    return annotationResolver.resolveToleranceZoneForm(instance);
  }

  StepToleranceZone resolveToleranceZone(StepEntityInstance instance) {
    return annotationResolver.resolveToleranceZone(instance);
  }

  StepConfigurationItem resolveConfigurationItem(StepEntityInstance instance) {
    return analysisResolver.resolveConfigurationItem(instance);
  }

  StepDirectedDimensionalSize resolveDirectedDimensionalSize(StepEntityInstance instance) {
    return annotationResolver.resolveDirectedDimensionalSize(instance);
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

  StepCompositeGroupTolerance resolveCompositeGroupTolerance(StepEntityInstance instance) {
    return annotationResolver.resolveCompositeGroupTolerance(instance);
  }

  StepGeometricToleranceTarget resolveGeometricToleranceTarget(StepEntityInstance instance) {
    return annotationResolver.resolveGeometricToleranceTarget(instance);
  }

  StepQualifiedRepresentationItem resolveQualifiedRepresentationItem(StepEntityInstance instance) {
    return representationResolver.resolveQualifiedRepresentationItem(instance);
  }

  StepDatumReferenceModifierWithSign resolveDatumReferenceModifierWithSign(StepEntityInstance instance) {
    return annotationResolver.resolveDatumReferenceModifierWithSign(instance);
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

  StepFeatureControlFrame resolveFeatureControlFrame(StepEntityInstance instance) {
    return annotationResolver.resolveFeatureControlFrame(instance);
  }

  StepRunoutToleranceZone resolveRunoutToleranceZone(StepEntityInstance instance) {
    return annotationResolver.resolveRunoutToleranceZone(instance);
  }

  StepGeometricToleranceWithDatumReference resolveGeometricToleranceWithDatumReference(StepEntityInstance instance) {
    return annotationResolver.resolveGeometricToleranceWithDatumReference(instance);
  }

  StepLinearToleranceZone resolveLinearToleranceZone(StepEntityInstance instance) {
    return annotationResolver.resolveLinearToleranceZone(instance);
  }

  StepRadialToleranceZone resolveRadialToleranceZone(StepEntityInstance instance) {
    return annotationResolver.resolveRadialToleranceZone(instance);
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
        literalList(instance, definition, 4));
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

  StepDatumReferenceModifierWithValue resolveDatumReferenceModifierWithValue(StepEntityInstance instance) {
    return annotationResolver.resolveDatumReferenceModifierWithValue(instance);
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

  StepDatumReferenceModifier resolveDatumReferenceModifier(StepEntityInstance instance) {
    return annotationResolver.resolveDatumReferenceModifier(instance);
  }

  // Phase 4: Tessellated triangulated resolve methods

  StepTriangulatedFace resolveTriangulatedFace(StepEntityInstance instance) {
    return tessellationResolver.resolveTriangulatedFace(instance);
  }

  StepComplexTriangulatedFace resolveComplexTriangulatedFace(StepEntityInstance instance) {
    return tessellationResolver.resolveComplexTriangulatedFace(instance);
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

  StepMaterialDesignation resolveMaterialDesignation(StepEntityInstance instance) {
    return materialResolver.resolveMaterialDesignation(instance);
  }

  StepLayeredItem resolveLayeredItem(StepEntityInstance instance) {
    return materialResolver.resolveLayeredItem(instance);
  }

  StepDatum resolveDatum(StepEntityInstance instance) {
    return annotationResolver.resolveDatum(instance);
  }

  StepDatumFeature resolveDatumFeature(StepEntityInstance instance) {
    return annotationResolver.resolveDatumFeature(instance);
  }

  StepDatumReference resolveDatumReference(StepEntityInstance instance) {
    return annotationResolver.resolveDatumReference(instance);
  }

  StepDatumReferenceCompartment resolveDatumReferenceCompartment(StepEntityInstance instance) {
    return annotationResolver.resolveDatumReferenceCompartment(instance);
  }

  StepDatumTarget resolveDatumTarget(StepEntityInstance instance) {
    return annotationResolver.resolveDatumTarget(instance);
  }

  StepDatumSystem resolveDatumSystem(StepEntityInstance instance) {
    return annotationResolver.resolveDatumSystem(instance);
  }

  StepDatumSystemReference resolveDatumSystemReference(StepEntityInstance instance) {
    return annotationResolver.resolveDatumSystemReference(instance);
  }

  StepTolerancePair resolveTolerancePair(StepEntityInstance instance) {
    return annotationResolver.resolveTolerancePair(instance);
  }

  StepToleranceSet resolveToleranceSet(StepEntityInstance instance) {
    return annotationResolver.resolveToleranceSet(instance);
  }

  StepGeometricMeasurement resolveGeometricMeasurement(StepEntityInstance instance) {
    return unitResolver.resolveGeometricMeasurement(instance);
  }

  StepDimensionalMeasurement resolveDimensionalMeasurement(StepEntityInstance instance) {
    return annotationResolver.resolveDimensionalMeasurement(instance);
  }

  // Manufacturing operation resolvers
  StepMachiningOperation resolveMachiningOperation(StepEntityInstance instance) {
    return machiningResolver.resolveMachiningOperation(instance);
  }

  StepMachiningOperationSequence resolveMachiningOperationSequence(StepEntityInstance instance) {
    return machiningResolver.resolveMachiningOperationSequence(instance);
  }

  // Feature definition resolvers
  StepFilletDefinition resolveFilletDefinition(StepEntityInstance instance) {
    return geometricFeatureResolver.resolveFilletDefinition(instance);
  }

  StepChamferDefinition resolveChamferDefinition(StepEntityInstance instance) {
    return geometricFeatureResolver.resolveChamferDefinition(instance);
  }

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

  StepCompositeDatumReference resolveCompositeDatumReference(StepEntityInstance instance) {
    return annotationResolver.resolveCompositeDatumReference(instance);
  }

  StepCsgVolume resolveCsgVolume(StepEntityInstance instance) {
    return solidResolver.resolveCsgVolume(instance);
  }

  StepBlockVolume resolveBlockVolume(StepEntityInstance instance) {
    return solidResolver.resolveBlockVolume(instance);
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

  StepRectangularToleranceZone resolveRectangularToleranceZone(StepEntityInstance instance) {
    return annotationResolver.resolveRectangularToleranceZone(instance);
  }

  StepToleranceModifier resolveToleranceModifier(StepEntityInstance instance) {
    return annotationResolver.resolveToleranceModifier(instance);
  }

  StepTypedMeasureWithUnit resolveTypedMeasureWithUnit(StepEntityInstance instance, String entityName) {
    return unitResolver.resolveTypedMeasureWithUnit(instance, entityName);
  }

  StepCartesianTransformationOperator resolveCartesianTransformationOperator(StepEntityInstance instance) {
    return transformationResolver.resolveCartesianTransformationOperator(instance);
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

  StepDimensionalSize resolveDimensionalSize(StepEntityInstance instance) {
    return annotationResolver.resolveDimensionalSize(instance);
  }

  StepDimensionalLocation resolveDimensionalLocation(StepEntityInstance instance) {
    return annotationResolver.resolveDimensionalLocation(instance);
  }

  StepShapeDimensionRepresentation resolveShapeDimensionRepresentation(StepEntityInstance instance) {
    return annotationResolver.resolveShapeDimensionRepresentation(instance);
  }

  StepPlusMinusTolerance resolvePlusMinusTolerance(StepEntityInstance instance) {
    return annotationResolver.resolvePlusMinusTolerance(instance);
  }

  StepToleranceValue resolveToleranceValue(StepEntityInstance instance) {
    return annotationResolver.resolveToleranceValue(instance);
  }

  StepMeasureRepresentationItemWithUnit resolveMeasureRepresentationItemWithUnit(StepEntityInstance instance) {
    return unitResolver.resolveMeasureRepresentationItemWithUnit(instance);
  }

  StepMeasureQualification resolveMeasureQualification(StepEntityInstance instance) {
    return unitResolver.resolveMeasureQualification(instance);
  }

  StepMakeFromFeature resolveMakeFromFeature(StepEntityInstance instance) {
    return productResolver.resolveMakeFromFeature(instance);
  }

  StepMakeFromUsageOption resolveMakeFromUsageOption(StepEntityInstance instance) {
    return productResolver.resolveMakeFromUsageOption(instance);
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

  StepShapeAspectShapeRepresentation resolveShapeAspectShapeRepresentation(StepEntityInstance instance) {
    return representationResolver.resolveShapeAspectShapeRepresentation(instance);
  }

  StepMakeFromBuildAssembly resolveMakeFromBuildAssembly(StepEntityInstance instance) {
    return productResolver.resolveMakeFromBuildAssembly(instance);
  }

  StepAssemblyComponentRelationship resolveAssemblyComponentRelationship(
      StepEntityInstance instance) {
    return productResolver.resolveAssemblyComponentRelationship(instance);
  }

  StepDesignMakeFrom resolveDesignMakeFrom(StepEntityInstance instance) {
    return productResolver.resolveDesignMakeFrom(instance);
  }

  StepInterpolatedConfigurationSegment resolveInterpolatedConfigurationSegment(
      StepEntityInstance instance) {
    return productResolver.resolveInterpolatedConfigurationSegment(instance);
  }

  StepRangeDimensionalSize resolveRangeDimensionalSize(StepEntityInstance instance) {
    return annotationResolver.resolveRangeDimensionalSize(instance);
  }

  StepDesignedPartDesignVersion resolveDesignedPartDesignVersion(StepEntityInstance instance) {
    return productResolver.resolveDesignedPartDesignVersion(instance);
  }

  StepSurfaceStyleRendering resolveSurfaceStyleRendering(StepEntityInstance instance) {
    return materialResolver.resolveSurfaceStyleRendering(instance);
  }

  StepSurfaceStyleRenderingWithProperties resolveSurfaceStyleRenderingWithProperties(StepEntityInstance instance) {
    return materialResolver.resolveSurfaceStyleRenderingWithProperties(instance);
  }

  StepRenderingProperties resolveRenderingProperties(StepEntityInstance instance) {
    return materialResolver.resolveRenderingProperties(instance);
  }

  StepLightSource resolveLightSource(StepEntityInstance instance) {
    return visualizationResolver.resolveLightSource(instance);
  }

  StepLightSourceAmbient resolveLightSourceAmbient(StepEntityInstance instance) {
    return visualizationResolver.resolveLightSourceAmbient(instance);
  }

  StepLightSourceDirectional resolveLightSourceDirectional(StepEntityInstance instance) {
    return visualizationResolver.resolveLightSourceDirectional(instance);
  }

  StepLightSourcePositional resolveLightSourcePositional(StepEntityInstance instance) {
    return visualizationResolver.resolveLightSourcePositional(instance);
  }

  StepLightSourceSpot resolveLightSourceSpot(StepEntityInstance instance) {
    return visualizationResolver.resolveLightSourceSpot(instance);
  }

  StepPresentationLayerUsage resolvePresentationLayerUsage(StepEntityInstance instance) {
    return materialResolver.resolvePresentationLayerUsage(instance);
  }

  StepCameraModelD2 resolveCameraModelD2(StepEntityInstance instance) {
    return visualizationResolver.resolveCameraModelD2(instance);
  }

  StepCameraModelD3 resolveCameraModelD3(StepEntityInstance instance) {
    return visualizationResolver.resolveCameraModelD3(instance);
  }

  StepCameraUsage resolveCameraUsage(StepEntityInstance instance) {
    return visualizationResolver.resolveCameraUsage(instance);
  }

  StepCameraImage resolveCameraImage(StepEntityInstance instance) {
    return visualizationResolver.resolveCameraImage(instance);
  }

  StepPlanarBox resolvePlanarBox(StepEntityInstance instance) {
    return visualizationResolver.resolvePlanarBox(instance);
  }

  StepPlanarExtent resolvePlanarExtent(StepEntityInstance instance) {
    return visualizationResolver.resolvePlanarExtent(instance);
  }

  StepViewVolume resolveViewVolume(StepEntityInstance instance) {
    return visualizationResolver.resolveViewVolume(instance);
  }

  StepMechanicalDesignShapeRepresentation resolveMechanicalDesignShapeRepresentation(StepEntityInstance instance) {
    return productResolver.resolveMechanicalDesignShapeRepresentation(instance);
  }

  StepKinematicPair resolveKinematicPair(StepEntityInstance instance) {
    return kinematicResolver.resolveKinematicPair(instance);
  }

  StepKinematicJoint resolveKinematicJoint(StepEntityInstance instance) {
    return kinematicResolver.resolveKinematicJoint(instance);
  }

  StepKinematicLink resolveKinematicLink(StepEntityInstance instance) {
    return kinematicResolver.resolveKinematicLink(instance);
  }

  StepKinematicStructure resolveKinematicStructure(StepEntityInstance instance) {
    return kinematicResolver.resolveKinematicStructure(instance);
  }

  StepKinematicPair resolveKinematicPair(StepEntityInstance instance, String entityName) {
    return kinematicResolver.resolveKinematicPair(instance, entityName);
  }

  StepPrismaticPair resolvePrismaticPair(StepEntityInstance instance) {
    return kinematicResolver.resolvePrismaticPair(instance);
  }

  StepRevolutePair resolveRevolutePair(StepEntityInstance instance) {
    return kinematicResolver.resolveRevolutePair(instance);
  }

  StepCylindricalPair resolveCylindricalPair(StepEntityInstance instance) {
    return kinematicResolver.resolveCylindricalPair(instance);
  }

  StepSphericalPair resolveSphericalPair(StepEntityInstance instance) {
    return kinematicResolver.resolveSphericalPair(instance);
  }

  StepPlanarPair resolvePlanarPair(StepEntityInstance instance) {
    return kinematicResolver.resolvePlanarPair(instance);
  }

  StepUniversalPair resolveUniversalPair(StepEntityInstance instance) {
    return kinematicResolver.resolveUniversalPair(instance);
  }

  StepScrewPair resolveScrewPair(StepEntityInstance instance) {
    return kinematicResolver.resolveScrewPair(instance);
  }

  StepGearPair resolveGearPair(StepEntityInstance instance) {
    return kinematicResolver.resolveGearPair(instance);
  }

  StepGearPairWithRange resolveGearPairWithRange(StepEntityInstance instance) {
    return kinematicResolver.resolveGearPairWithRange(instance);
  }

  StepRackAndPinionPair resolveRackAndPinionPair(StepEntityInstance instance) {
    return kinematicResolver.resolveRackAndPinionPair(instance);
  }

  StepLowOrderKinematicPairWithRange resolveLowOrderKinematicPairWithRange(StepEntityInstance instance) {
    return kinematicResolver.resolveLowOrderKinematicPairWithRange(instance);
  }

  StepActuatedKinematicPair resolveActuatedKinematicPair(StepEntityInstance instance) {
    return kinematicResolver.resolveActuatedKinematicPair(instance);
  }

  StepMechanismStateRepresentation resolveMechanismStateRepresentation(StepEntityInstance instance) {
    return kinematicResolver.resolveMechanismStateRepresentation(instance);
  }

  StepKinematicPath resolveKinematicPath(StepEntityInstance instance) {
    return kinematicResolver.resolveKinematicPath(instance);
  }

  StepKinematicFrameBasedTransformation resolveKinematicFrameBasedTransformation(StepEntityInstance instance) {
    return kinematicResolver.resolveKinematicFrameBasedTransformation(instance);
  }

  StepValidationPropertyRepresentation resolveValidationPropertyRepresentation(StepEntityInstance instance) {
    return representationResolver.resolveValidationPropertyRepresentation(instance);
  }

  StepCalculatedGeometricRepresentationItem resolveCalculatedGeometricRepresentationItem(StepEntityInstance instance) {
    return representationResolver.resolveCalculatedGeometricRepresentationItem(instance);
  }

  // Phase 5: FEA resolve methods

  StepVolume3dElementRepresentation resolveVolume3dElementRepresentation(StepEntityInstance instance) {
    return representationResolver.resolveVolume3dElementRepresentation(instance);
  }

  StepFeaNode resolveFeaNode(StepEntityInstance instance) {
    return analysisResolver.resolveFeaNode(instance);
  }

  StepFeaElement resolveFeaElement(StepEntityInstance instance) {
    return analysisResolver.resolveFeaElement(instance);
  }

  StepFeaLoad resolveFeaLoad(StepEntityInstance instance) {
    return analysisResolver.resolveFeaLoad(instance);
  }

  StepFeaModel resolveFeaModel(StepEntityInstance instance) {
    return analysisResolver.resolveFeaModel(instance);
  }

  StepMaterial resolveMaterial(StepEntityInstance instance) {
    return materialResolver.resolveMaterial(instance);
  }

  StepFeaLinearMaterial resolveFeaLinearMaterial(StepEntityInstance instance) {
    return materialResolver.resolveFeaLinearMaterial(instance);
  }

  StepFeaNonLinearMaterial resolveFeaNonLinearMaterial(StepEntityInstance instance) {
    return materialResolver.resolveFeaNonLinearMaterial(instance);
  }

  StepFeaMassDensity resolveFeaMassDensity(StepEntityInstance instance) {
    return analysisResolver.resolveFeaMassDensity(instance);
  }

  StepFeaYieldStress resolveFeaYieldStress(StepEntityInstance instance) {
    return analysisResolver.resolveFeaYieldStress(instance);
  }

  StepFeaUltimateStress resolveFeaUltimateStress(StepEntityInstance instance) {
    return analysisResolver.resolveFeaUltimateStress(instance);
  }

  StepDisplacementBoundaryCondition resolveDisplacementBoundaryCondition(StepEntityInstance instance) {
    return boundaryConditionResolver.resolveDisplacementBoundaryCondition(instance);
  }

  StepVelocityBoundaryCondition resolveVelocityBoundaryCondition(StepEntityInstance instance) {
    return boundaryConditionResolver.resolveVelocityBoundaryCondition(instance);
  }

  StepAccelerationBoundaryCondition resolveAccelerationBoundaryCondition(StepEntityInstance instance) {
    return boundaryConditionResolver.resolveAccelerationBoundaryCondition(instance);
  }

  StepForceBoundaryCondition resolveForceBoundaryCondition(StepEntityInstance instance) {
    return boundaryConditionResolver.resolveForceBoundaryCondition(instance);
  }

  StepPressureBoundaryCondition resolvePressureBoundaryCondition(StepEntityInstance instance) {
    return boundaryConditionResolver.resolvePressureBoundaryCondition(instance);
  }

  StepThermalBoundaryCondition resolveThermalBoundaryCondition(StepEntityInstance instance) {
    return boundaryConditionResolver.resolveThermalBoundaryCondition(instance);
  }

  StepStressAnalysis resolveStressAnalysis(StepEntityInstance instance) {
    return analysisResolver.resolveStressAnalysis(instance);
  }

  StepBucklingAnalysis resolveBucklingAnalysis(StepEntityInstance instance) {
    return analysisResolver.resolveBucklingAnalysis(instance);
  }

  StepModalAnalysis resolveModalAnalysis(StepEntityInstance instance) {
    return analysisResolver.resolveModalAnalysis(instance);
  }

  StepThermalAnalysis resolveThermalAnalysis(StepEntityInstance instance) {
    return analysisResolver.resolveThermalAnalysis(instance);
  }

  StepStructuralAnalysisModel resolveStructuralAnalysisModel(StepEntityInstance instance) {
    return analysisResolver.resolveStructuralAnalysisModel(instance);
  }

  StepRevoluteJoint resolveRevoluteJoint(StepEntityInstance instance) {
    return kinematicResolver.resolveRevoluteJoint(instance);
  }

  StepPrismaticJoint resolvePrismaticJoint(StepEntityInstance instance) {
    return kinematicResolver.resolvePrismaticJoint(instance);
  }

  StepSphericalJoint resolveSphericalJoint(StepEntityInstance instance) {
    return kinematicResolver.resolveSphericalJoint(instance);
  }

  StepCylindricalJoint resolveCylindricalJoint(StepEntityInstance instance) {
    return kinematicResolver.resolveCylindricalJoint(instance);
  }

  StepPlanarJoint resolvePlanarJoint(StepEntityInstance instance) {
    return kinematicResolver.resolvePlanarJoint(instance);
  }

  StepScrewJoint resolveScrewJoint(StepEntityInstance instance) {
    return kinematicResolver.resolveScrewJoint(instance);
  }

  StepGeneralJoint resolveGeneralJoint(StepEntityInstance instance) {
    return kinematicResolver.resolveGeneralJoint(instance);
  }

  StepDirectionSense resolveDirectionSense(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DIRECTION_SENSE");
    requireParameterCount(instance, definition, 2);
    return new StepDirectionSense(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1));
  }

  StepJointValue resolveJointValue(StepEntityInstance instance) {
    return kinematicResolver.resolveJointValue(instance);
  }

  StepKinematicChain resolveKinematicChain(StepEntityInstance instance) {
    return kinematicResolver.resolveKinematicChain(instance);
  }

  StepKinematicModel resolveKinematicModel(StepEntityInstance instance) {
    return analysisResolver.resolveKinematicModel(instance);
  }

  StepKinematicProperty resolveKinematicProperty(StepEntityInstance instance) {
    return kinematicResolver.resolveKinematicProperty(instance);
  }

  StepMotionConstraint resolveMotionConstraint(StepEntityInstance instance) {
    return kinematicResolver.resolveMotionConstraint(instance);
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

  StepShapeRepresentationTransformation resolveShapeRepresentationTransformation(StepEntityInstance instance) {
    return representationResolver.resolveShapeRepresentationTransformation(instance);
  }

  StepRepresentationContext3d resolveRepresentationContext3d(StepEntityInstance instance) {
    return representationResolver.resolveRepresentationContext3d(instance);
  }

  StepAppliedAttributeClassification resolveAppliedAttributeClassification(StepEntityInstance instance) {
    return propertyResolver.resolveAppliedAttributeClassification(instance);
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

  StepStructuralAnalysisRepresentation resolveStructuralAnalysisRepresentation(StepEntityInstance instance) {
    return analysisResolver.resolveStructuralAnalysisRepresentation(instance);
  }

  StepStructuralAnalysisRepresentationParameters resolveStructuralAnalysisRepresentationParameters(StepEntityInstance instance) {
    return analysisResolver.resolveStructuralAnalysisRepresentationParameters(instance);
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

  StepElementVolume resolveElementVolume(StepEntityInstance instance) {
    return feaElementResolver.resolveElementVolume(instance);
  }

  StepVolumeElement resolveVolumeElement(StepEntityInstance instance) {
    return feaElementResolver.resolveVolumeElement(instance);
  }

  StepSurfaceElement resolveSurfaceElement(StepEntityInstance instance) {
    return feaElementResolver.resolveSurfaceElement(instance);
  }

  StepLineElement resolveLineElement(StepEntityInstance instance) {
    return feaElementResolver.resolveLineElement(instance);
  }

  StepMassElement resolveMassElement(StepEntityInstance instance) {
    return feaElementResolver.resolveMassElement(instance);
  }

  StepConnectivityElement resolveConnectivityElement(StepEntityInstance instance) {
    return feaElementResolver.resolveConnectivityElement(instance);
  }

  StepElementGeometricDescription resolveElementGeometricDescription(StepEntityInstance instance) {
    return feaElementResolver.resolveElementGeometricDescription(instance);
  }

  StepUniformSurfaceElement resolveUniformSurfaceElement(StepEntityInstance instance) {
    return feaElementResolver.resolveUniformSurfaceElement(instance);
  }

  StepUniformVolumeElement resolveUniformVolumeElement(StepEntityInstance instance) {
    return feaElementResolver.resolveUniformVolumeElement(instance);
  }

  StepNodeRepresentation resolveNodeRepresentation(StepEntityInstance instance) {
    return representationResolver.resolveNodeRepresentation(instance);
  }

  StepVolume3dElementProperty resolveVolume3dElementProperty(StepEntityInstance instance) {
    return analysisResolver.resolveVolume3dElementProperty(instance);
  }

  StepCurve3dElementProperty resolveCurve3dElementProperty(StepEntityInstance instance) {
    return analysisResolver.resolveCurve3dElementProperty(instance);
  }

  StepSurface3dElementProperty resolveSurface3dElementProperty(StepEntityInstance instance) {
    return analysisResolver.resolveSurface3dElementProperty(instance);
  }

  StepFeaMaterialPropertyRepresentation resolveFeaMaterialPropertyRepresentation(StepEntityInstance instance) {
    return representationResolver.resolveFeaMaterialPropertyRepresentation(instance);
  }

  StepElementVolume2d resolveElementVolume2d(StepEntityInstance instance) {
    return feaElementResolver.resolveElementVolume2d(instance);
  }

  StepElementVolume3d resolveElementVolume3d(StepEntityInstance instance) {
    return feaElementResolver.resolveElementVolume3d(instance);
  }

  StepNodeSet resolveNodeSet(StepEntityInstance instance) {
    return feaElementResolver.resolveNodeSet(instance);
  }

  StepElementSet resolveElementSet(StepEntityInstance instance) {
    return feaElementResolver.resolveElementSet(instance);
  }

  StepFeaSecuredVariable resolveFeaSecuredVariable(StepEntityInstance instance) {
    return analysisResolver.resolveFeaSecuredVariable(instance);
  }

  StepFeaConstantFunction3d resolveFeaConstantFunction3d(StepEntityInstance instance) {
    return analysisResolver.resolveFeaConstantFunction3d(instance);
  }

  StepFeaLinearAlgebraicMatrix resolveFeaLinearAlgebraicMatrix(StepEntityInstance instance) {
    return analysisResolver.resolveFeaLinearAlgebraicMatrix(instance);
  }

  StepFeaLinearAlgebraicVector resolveFeaLinearAlgebraicVector(StepEntityInstance instance) {
    return analysisResolver.resolveFeaLinearAlgebraicVector(instance);
  }

  StepFeaAxis2Placement3d resolveFeaAxis2Placement3d(StepEntityInstance instance) {
    return analysisResolver.resolveFeaAxis2Placement3d(instance);
  }

  StepFeaGroupRepresentation resolveFeaGroupRepresentation(StepEntityInstance instance) {
    return representationResolver.resolveFeaGroupRepresentation(instance);
  }

  // New FEA element property resolvers
  StepFeaShellElementProperty resolveFeaShellElementProperty(StepEntityInstance instance) {
    return analysisResolver.resolveFeaShellElementProperty(instance);
  }

  StepFeaBeamElementProperty resolveFeaBeamElementProperty(StepEntityInstance instance) {
    return analysisResolver.resolveFeaBeamElementProperty(instance);
  }

  StepFea2DElementProperty resolveFea2DElementProperty(StepEntityInstance instance) {
    return analysisResolver.resolveFea2DElementProperty(instance);
  }

  StepFea3DElementProperty resolveFea3DElementProperty(StepEntityInstance instance) {
    return analysisResolver.resolveFea3DElementProperty(instance);
  }

  StepFeaTrussElementProperty resolveFeaTrussElementProperty(StepEntityInstance instance) {
    return analysisResolver.resolveFeaTrussElementProperty(instance);
  }

  StepFeaSpringElementProperty resolveFeaSpringElementProperty(StepEntityInstance instance) {
    return analysisResolver.resolveFeaSpringElementProperty(instance);
  }

  StepFeaVolumeElementProperty resolveFeaVolumeElementProperty(StepEntityInstance instance) {
    return analysisResolver.resolveFeaVolumeElementProperty(instance);
  }

  // Unit with unit resolvers
  StepLengthUnitWithUnit resolveLengthUnitWithUnit(StepEntityInstance instance) {
    return unitResolver.resolveLengthUnitWithUnit(instance);
  }

  StepPlaneAngleUnitWithUnit resolvePlaneAngleUnitWithUnit(StepEntityInstance instance) {
    return unitResolver.resolvePlaneAngleUnitWithUnit(instance);
  }

  StepVolumeUnitWithUnit resolveVolumeUnitWithUnit(StepEntityInstance instance) {
    return unitResolver.resolveVolumeUnitWithUnit(instance);
  }

  StepAreaUnitWithUnit resolveAreaUnitWithUnit(StepEntityInstance instance) {
    return unitResolver.resolveAreaUnitWithUnit(instance);
  }

  StepMassUnitWithUnit resolveMassUnitWithUnit(StepEntityInstance instance) {
    return unitResolver.resolveMassUnitWithUnit(instance);
  }

  StepConversionBasedUnitAndUnit resolveConversionBasedUnitAndUnit(StepEntityInstance instance) {
    return unitResolver.resolveConversionBasedUnitAndUnit(instance);
  }

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

  StepSweptProfileAreaOutline resolveSweptProfileAreaOutline(StepEntityInstance instance) {
    return profileResolver.resolveSweptProfileAreaOutline(instance);
  }

  // Kinematic reference resolvers
  StepKinematicLinkReference resolveKinematicLinkReference(StepEntityInstance instance) {
    return kinematicResolver.resolveKinematicLinkReference(instance);
  }

  StepKinematicJointReference resolveKinematicJointReference(StepEntityInstance instance) {
    return kinematicResolver.resolveKinematicJointReference(instance);
  }

  // Product representation resolvers
  StepHybridShapeRepresentation resolveHybridShapeRepresentation(StepEntityInstance instance) {
    return representationResolver.resolveHybridShapeRepresentation(instance);
  }

  StepDrawingRepresentation resolveDrawingRepresentation(StepEntityInstance instance) {
    return representationResolver.resolveDrawingRepresentation(instance);
  }

  StepSchematicRepresentation resolveSchematicRepresentation(StepEntityInstance instance) {
    return representationResolver.resolveSchematicRepresentation(instance);
  }

  StepSketchRepresentation resolveSketchRepresentation(StepEntityInstance instance) {
    return representationResolver.resolveSketchRepresentation(instance);
  }

  StepSectionRepresentation resolveSectionRepresentation(StepEntityInstance instance) {
    return representationResolver.resolveSectionRepresentation(instance);
  }

  StepTabulationRepresentation resolveTabulationRepresentation(StepEntityInstance instance) {
    return representationResolver.resolveTabulationRepresentation(instance);
  }

  StepZoneRepresentation resolveZoneRepresentation(StepEntityInstance instance) {
    return representationResolver.resolveZoneRepresentation(instance);
  }

  StepCsgPrimitive3D resolveCsgPrimitive3D(StepEntityInstance instance) {
    return solidResolver.resolveCsgPrimitive3D(instance);
  }

  StepCompoundRepresentationItem resolveCompoundRepresentationItem(StepEntityInstance instance, String entityName) {
    return representationResolver.resolveCompoundRepresentationItem(instance, entityName);
  }

  StepContextDependentGeometricShapeRepresentation resolveContextDependentGeometricShapeRepresentation(StepEntityInstance instance) {
    return representationResolver.resolveContextDependentGeometricShapeRepresentation(instance);
  }

  StepUsageAssociation resolveUsageAssociation(StepEntityInstance instance) {
    return associationResolver.resolveUsageAssociation(instance);
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
  StepExclusionAssignment resolveExclusionAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveExclusionAssignment(instance);
  }

  StepDateTimeEffectivity resolveDateTimeEffectivity(StepEntityInstance instance) {
    return assignmentResolver.resolveDateTimeEffectivity(instance);
  }

  StepDateEffectivity resolveDateEffectivity(StepEntityInstance instance) {
    return assignmentResolver.resolveDateEffectivity(instance);
  }

  StepLotEffectivity resolveLotEffectivity(StepEntityInstance instance) {
    return assignmentResolver.resolveLotEffectivity(instance);
  }

  StepSerialNumberEffectivity resolveSerialNumberEffectivity(StepEntityInstance instance) {
    return assignmentResolver.resolveSerialNumberEffectivity(instance);
  }

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
        intList(instance, definition, 2),
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
  StepAnnotationFillAreaRegion resolveAnnotationFillAreaRegion(StepEntityInstance instance) {
    return annotationResolver.resolveAnnotationFillAreaRegion(instance);
  }

  // Product resolvers
  StepAssemblyOperation resolveAssemblyOperation(StepEntityInstance instance) {
    return productResolver.resolveAssemblyOperation(instance);
  }

  StepAssemblySequence resolveAssemblySequence(StepEntityInstance instance) {
    return productResolver.resolveAssemblySequence(instance);
  }

  StepAssemblyStructure resolveAssemblyStructure(StepEntityInstance instance) {
    return productResolver.resolveAssemblyStructure(instance);
  }

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

  StepMechanismDefinition resolveMechanismDefinition(StepEntityInstance instance) {
    return kinematicResolver.resolveMechanismDefinition(instance);
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

  StepProductVersion resolveProductVersion(StepEntityInstance instance) {
    return productResolver.resolveProductVersion(instance);
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

  StepStructuralFeature resolveStructuralFeature(StepEntityInstance instance) {
    return geometricFeatureResolver.resolveStructuralFeature(instance);
  }

  StepFillAreaWithOutline resolveFillAreaWithOutline(StepEntityInstance instance) {
    return materialResolver.resolveFillAreaWithOutline(instance);
  }

  // Annotation resolvers
  StepAnnotationRecord resolveAnnotationRecord(StepEntityInstance instance) {
    return annotationResolver.resolveAnnotationRecord(instance);
  }

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

  StepExternallyDefinedHatchStyle resolveExternallyDefinedHatchStyle(StepEntityInstance instance) {
    return materialResolver.resolveExternallyDefinedHatchStyle(instance);
  }

  StepExternallyDefinedTileStyle resolveExternallyDefinedTileStyle(StepEntityInstance instance) {
    return materialResolver.resolveExternallyDefinedTileStyle(instance);
  }

  StepMarkingFeature resolveMarkingFeature(StepEntityInstance instance) {
    return geometricFeatureResolver.resolveMarkingFeature(instance);
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
  StepAngularDimensionRepresentation resolveAngularDimensionRepresentation(StepEntityInstance instance) {
    return annotationResolver.resolveAngularDimensionRepresentation(instance);
  }

  StepChainDimensionRepresentation resolveChainDimensionRepresentation(StepEntityInstance instance) {
    return annotationResolver.resolveChainDimensionRepresentation(instance);
  }

  StepLinearDimensionRepresentation resolveLinearDimensionRepresentation(StepEntityInstance instance) {
    return annotationResolver.resolveLinearDimensionRepresentation(instance);
  }

  StepOrdinateDimensionRepresentation resolveOrdinateDimensionRepresentation(StepEntityInstance instance) {
    return annotationResolver.resolveOrdinateDimensionRepresentation(instance);
  }

  StepShapeDimensionRepresentationWithTolerance resolveShapeDimensionRepresentationWithTolerance(StepEntityInstance instance) {
    return representationResolver.resolveShapeDimensionRepresentationWithTolerance(instance);
  }

  // FEA resolvers
  StepBoundaryCondition resolveBoundaryCondition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BOUNDARY_CONDITION");
    requireParameterCount(instance, definition, 6);
    return new StepBoundaryCondition(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        stringList(instance, definition, 3),
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
        stringList(instance, definition, 3),
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

  StepCompositeShapeAspect resolveCompositeShapeAspect(StepEntityInstance instance) {
    return representationResolver.resolveCompositeShapeAspect(instance);
  }

  // Product resolvers
  StepAssemblyComponentUsage resolveAssemblyComponentUsage(StepEntityInstance instance) {
    return productResolver.resolveAssemblyComponentUsage(instance);
  }

  StepBillOfMaterials resolveBillOfMaterials(StepEntityInstance instance) {
    return materialResolver.resolveBillOfMaterials(instance);
  }

  StepMakeFromRelationship resolveMakeFromRelationship(StepEntityInstance instance) {
    return productResolver.resolveMakeFromRelationship(instance);
  }

  StepTextLiteralWithDraughtingCallout resolveTextLiteralWithDraughtingCallout(StepEntityInstance instance) {
    return annotationResolver.resolveTextLiteralWithDraughtingCallout(instance);
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

  StepTextFont resolveTextFont(StepEntityInstance instance) {
    return materialResolver.resolveTextFont(instance);
  }

  StepCharacterGlyph resolveCharacterGlyph(StepEntityInstance instance) {
    return draughtingResolver.resolveCharacterGlyph(instance);
  }

  StepCharacterGlyphOutline resolveCharacterGlyphOutline(StepEntityInstance instance) {
    return draughtingResolver.resolveCharacterGlyphOutline(instance);
  }

  StepCharacterGlyphOutlineWithCharacteristics resolveCharacterGlyphOutlineWithCharacteristics(StepEntityInstance instance) {
    return draughtingResolver.resolveCharacterGlyphOutlineWithCharacteristics(instance);
  }

  StepCharacterGlyphStroke resolveCharacterGlyphStroke(StepEntityInstance instance) {
    return draughtingResolver.resolveCharacterGlyphStroke(instance);
  }

  StepPreDefinedSurfaceStyle resolvePreDefinedSurfaceStyle(StepEntityInstance instance) {
    return materialResolver.resolvePreDefinedSurfaceStyle(instance);
  }

  StepSurfaceStyleParameterLines resolveSurfaceStyleParameterLines(StepEntityInstance instance) {
    return materialResolver.resolveSurfaceStyleParameterLines(instance);
  }

  StepFillAreaStyleOutline resolveFillAreaStyleOutline(StepEntityInstance instance) {
    return materialResolver.resolveFillAreaStyleOutline(instance);
  }

  StepFillAreaStyleTransparent resolveFillAreaStyleTransparent(StepEntityInstance instance) {
    return materialResolver.resolveFillAreaStyleTransparent(instance);
  }

  StepFillAreaStyleHatching resolveFillAreaStyleHatching(StepEntityInstance instance) {
    return materialResolver.resolveFillAreaStyleHatching(instance);
  }

  StepFillAreaStyleTiling resolveFillAreaStyleTiling(StepEntityInstance instance) {
    return materialResolver.resolveFillAreaStyleTiling(instance);
  }

  StepCurveStyleFont resolveCurveStyleFont(StepEntityInstance instance) {
    return materialResolver.resolveCurveStyleFont(instance);
  }

  StepCurveStyleRendering resolveCurveStyleRendering(StepEntityInstance instance) {
    return materialResolver.resolveCurveStyleRendering(instance);
  }

  StepCurveStyleWithFont resolveCurveStyleWithFont(StepEntityInstance instance) {
    return materialResolver.resolveCurveStyleWithFont(instance);
  }

  StepDraughtingPreDefinedTerminatorSymbol resolveDraughtingPreDefinedTerminatorSymbol(StepEntityInstance instance) {
    return annotationResolver.resolveDraughtingPreDefinedTerminatorSymbol(instance);
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

  StepPmiGroup resolvePmiGroup(StepEntityInstance instance) {
    return propertyResolver.resolvePmiGroup(instance);
  }

  // Manufacturing resolvers
  StepFeatureElementDefinition resolveFeatureElementDefinition(StepEntityInstance instance) {
    return geometricFeatureResolver.resolveFeatureElementDefinition(instance);
  }

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
  StepTextFileRepresentation resolveTextFileRepresentation(StepEntityInstance instance) {
    return representationResolver.resolveTextFileRepresentation(instance);
  }

  StepPersonAndOrganizationAddress resolvePersonAndOrganizationAddress(StepEntityInstance instance) {
    return assignmentResolver.resolvePersonAndOrganizationAddress(instance);
  }

  StepOrganizationAddress resolveOrganizationAddress(StepEntityInstance instance) {
    return assignmentResolver.resolveOrganizationAddress(instance);
  }

  StepPersonAddress resolvePersonAddress(StepEntityInstance instance) {
    return assignmentResolver.resolvePersonAddress(instance);
  }

  StepAngularSize resolveAngularSize(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ANGULAR_SIZE");
    requireParameterCount(instance, definition, 4);
    return new StepAngularSize(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        numberValue(instance, definition, 2));
  }

  StepGeneralizedDatum resolveGeneralizedDatum(StepEntityInstance instance) {
    return annotationResolver.resolveGeneralizedDatum(instance);
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

  StepColorSpecification resolveColorSpecification(StepEntityInstance instance) {
    return materialResolver.resolveColorSpecification(instance);
  }

  StepWithDescriptiveRepresentationItem resolveWithDescriptiveRepresentationItem(StepEntityInstance instance) {
    return representationResolver.resolveWithDescriptiveRepresentationItem(instance);
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

  StepBooleanResult resolveBooleanResult(StepEntityInstance instance) {
    return solidResolver.resolveBooleanResult(instance);
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

  StepRepresentationContext resolveRepresentationContext(StepEntityInstance instance) {
    return representationResolver.resolveRepresentationContext(instance);
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

  StepProduct resolveProduct(StepEntityInstance instance) {
    return productResolver.resolveProduct(instance);
  }

  StepProductRelatedProductCategory resolveProductRelatedProductCategory(
      StepEntityInstance instance) {
    return productResolver.resolveProductRelatedProductCategory(instance);
  }

  StepProductCategory resolveProductCategory(StepEntityInstance instance) {
    return productResolver.resolveProductCategory(instance);
  }

  StepProductCategoryRelationship resolveProductCategoryRelationship(
      StepEntityInstance instance) {
    return productResolver.resolveProductCategoryRelationship(instance);
  }

  StepProductRelationship resolveProductRelationship(StepEntityInstance instance) {
    return productResolver.resolveProductRelationship(instance);
  }

  StepProductRelationship resolveProductRelationship(
      StepEntityInstance instance, String entityName) {
    return productResolver.resolveProductRelationship(instance, entityName);
  }

  StepProductDefinitionFormation resolveProductDefinitionFormation(
      StepEntityInstance instance) {
    return productResolver.resolveProductDefinitionFormation(instance);
  }

  StepProductDefinitionFormationRelationship resolveProductDefinitionFormationRelationship(StepEntityInstance instance) {
    return productResolver.resolveProductDefinitionFormationRelationship(instance);
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

  StepProductDefinitionShape resolveProductDefinitionShape(StepEntityInstance instance) {
    return productResolver.resolveProductDefinitionShape(instance);
  }

  StepPropertyDefinition resolvePropertyDefinition(StepEntityInstance instance) {
    return propertyResolver.resolvePropertyDefinition(instance);
  }

  StepPropertyDefinitionRelationship resolvePropertyDefinitionRelationship(StepEntityInstance instance) {
    return propertyResolver.resolvePropertyDefinitionRelationship(instance);
  }

  StepPropertyDefinitionRelationship resolvePropertyDefinitionRelationship(StepEntityInstance instance, String entityName) {
    return propertyResolver.resolvePropertyDefinitionRelationship(instance, entityName);
  }

  StepGeneralProperty resolveGeneralProperty(StepEntityInstance instance) {
    return propertyResolver.resolveGeneralProperty(instance);
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

  StepGroupRelationship resolveGroupRelationship(StepEntityInstance instance) {
    return propertyResolver.resolveGroupRelationship(instance);
  }

  StepGroupRelationship resolveGroupRelationship(StepEntityInstance instance, String entityName) {
    return propertyResolver.resolveGroupRelationship(instance, entityName);
  }

  StepGroupAssignment resolveGroupAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveGroupAssignment(instance);
  }

  StepAppliedGroupAssignment resolveAppliedGroupAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveAppliedGroupAssignment(instance);
  }

  StepAddress resolveAddress(StepEntityInstance instance) {
    return assignmentResolver.resolveAddress(instance);
  }

  StepDocumentType resolveDocumentType(StepEntityInstance instance) {
    return productResolver.resolveDocumentType(instance);
  }

  StepDocument resolveDocument(StepEntityInstance instance) {
    return productResolver.resolveDocument(instance);
  }

  StepDocumentRelationship resolveDocumentRelationship(StepEntityInstance instance) {
    return productResolver.resolveDocumentRelationship(instance);
  }

  StepDocumentUsageConstraint resolveDocumentUsageConstraint(StepEntityInstance instance) {
    return productResolver.resolveDocumentUsageConstraint(instance);
  }

  StepDocumentReference resolveDocumentReference(StepEntityInstance instance) {
    return productResolver.resolveDocumentReference(instance);
  }

  StepAppliedDocumentReference resolveAppliedDocumentReference(StepEntityInstance instance) {
    return productResolver.resolveAppliedDocumentReference(instance);
  }

  StepAppliedDocumentReference resolveAppliedDocumentReference(StepEntityInstance instance, String entityName) {
    return productResolver.resolveAppliedDocumentReference(instance, entityName);
  }

  StepPerson resolvePerson(StepEntityInstance instance) {
    return assignmentResolver.resolvePerson(instance);
  }

  StepOrganization resolveOrganization(StepEntityInstance instance) {
    return assignmentResolver.resolveOrganization(instance);
  }

  StepPersonAndOrganization resolvePersonAndOrganization(StepEntityInstance instance) {
    return assignmentResolver.resolvePersonAndOrganization(instance);
  }

  StepOrganizationRelationship resolveOrganizationRelationship(StepEntityInstance instance) {
    return assignmentResolver.resolveOrganizationRelationship(instance);
  }

  StepOrganizationRole resolveOrganizationRole(StepEntityInstance instance) {
    return assignmentResolver.resolveOrganizationRole(instance);
  }

  StepOrganizationAssignment resolveOrganizationAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveOrganizationAssignment(instance);
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

  StepEntity resolveLanguageAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveLanguageAssignment(instance);
  }

  StepAppliedLanguageAssignment resolveAppliedLanguageAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveAppliedLanguageAssignment(instance);
  }

  StepPersonAndOrganizationRole resolvePersonAndOrganizationRole(StepEntityInstance instance) {
    return assignmentResolver.resolvePersonAndOrganizationRole(instance);
  }

  StepPersonAndOrganizationAssignment resolvePersonAndOrganizationAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolvePersonAndOrganizationAssignment(instance);
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
        optionalIntegerValue(instance, definition, 1),
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

  StepDateRole resolveDateRole(StepEntityInstance instance) {
    return assignmentResolver.resolveDateRole(instance);
  }

  StepDateAssignment resolveDateAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveDateAssignment(instance);
  }

  StepAppliedDateAssignment resolveAppliedDateAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveAppliedDateAssignment(instance);
  }

  StepAppliedDateAssignment resolveAppliedDateAssignment(StepEntityInstance instance, String entityName) {
    return assignmentResolver.resolveAppliedDateAssignment(instance, entityName);
  }

  StepDateTimeRole resolveDateTimeRole(StepEntityInstance instance) {
    return assignmentResolver.resolveDateTimeRole(instance);
  }

  StepDateTimeAssignment resolveDateTimeAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveDateTimeAssignment(instance);
  }

  StepAppliedDateTimeAssignment resolveAppliedDateTimeAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveAppliedDateTimeAssignment(instance);
  }

  StepAppliedDateTimeAssignment resolveAppliedDateTimeAssignment(StepEntityInstance instance, String entityName) {
    return assignmentResolver.resolveAppliedDateTimeAssignment(instance, entityName);
  }

  StepApprovalStatus resolveApprovalStatus(StepEntityInstance instance) {
    return assignmentResolver.resolveApprovalStatus(instance);
  }

  StepApproval resolveApproval(StepEntityInstance instance) {
    return assignmentResolver.resolveApproval(instance);
  }

  StepApprovalRole resolveApprovalRole(StepEntityInstance instance) {
    return assignmentResolver.resolveApprovalRole(instance);
  }

  StepApprovalAssignment resolveApprovalAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveApprovalAssignment(instance);
  }

  StepAppliedApprovalAssignment resolveAppliedApprovalAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveAppliedApprovalAssignment(instance);
  }

  StepAppliedApprovalAssignment resolveAppliedApprovalAssignment(StepEntityInstance instance, String entityName) {
    return assignmentResolver.resolveAppliedApprovalAssignment(instance, entityName);
  }

  StepApprovalPersonOrganization resolveApprovalPersonOrganization(StepEntityInstance instance) {
    return assignmentResolver.resolveApprovalPersonOrganization(instance);
  }

  StepApprovalDateTime resolveApprovalDateTime(StepEntityInstance instance) {
    return assignmentResolver.resolveApprovalDateTime(instance);
  }

  StepSecurityClassificationLevel resolveSecurityClassificationLevel(StepEntityInstance instance) {
    return assignmentResolver.resolveSecurityClassificationLevel(instance);
  }

  StepSecurityClassification resolveSecurityClassification(StepEntityInstance instance) {
    return assignmentResolver.resolveSecurityClassification(instance);
  }

  StepSecurityClassificationAssignment resolveSecurityClassificationAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveSecurityClassificationAssignment(instance);
  }

  StepAppliedSecurityClassificationAssignment resolveAppliedSecurityClassificationAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveAppliedSecurityClassificationAssignment(instance);
  }

  StepAppliedSecurityClassificationAssignment resolveAppliedSecurityClassificationAssignment(StepEntityInstance instance, String entityName) {
    return assignmentResolver.resolveAppliedSecurityClassificationAssignment(instance, entityName);
  }

  StepContractType resolveContractType(StepEntityInstance instance) {
    return assignmentResolver.resolveContractType(instance);
  }

  StepContract resolveContract(StepEntityInstance instance) {
    return assignmentResolver.resolveContract(instance);
  }

  StepContractAssignment resolveContractAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveContractAssignment(instance);
  }

  StepAppliedContractAssignment resolveAppliedContractAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveAppliedContractAssignment(instance);
  }

  StepAppliedContractAssignment resolveAppliedContractAssignment(StepEntityInstance instance, String entityName) {
    return assignmentResolver.resolveAppliedContractAssignment(instance, entityName);
  }

  StepCertificationType resolveCertificationType(StepEntityInstance instance) {
    return assignmentResolver.resolveCertificationType(instance);
  }

  StepCertification resolveCertification(StepEntityInstance instance) {
    return assignmentResolver.resolveCertification(instance);
  }

  StepCertificationAssignment resolveCertificationAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveCertificationAssignment(instance);
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

  StepEffectivityRelationship resolveEffectivityRelationship(StepEntityInstance instance) {
    return assignmentResolver.resolveEffectivityRelationship(instance);
  }

  StepClassificationRole resolveClassificationRole(StepEntityInstance instance) {
    return assignmentResolver.resolveClassificationRole(instance);
  }

  StepClassificationAssignment resolveClassificationAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveClassificationAssignment(instance);
  }

  StepAppliedClassificationAssignment resolveAppliedClassificationAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveAppliedClassificationAssignment(instance);
  }

  StepIdentificationRole resolveIdentificationRole(StepEntityInstance instance) {
    return assignmentResolver.resolveIdentificationRole(instance);
  }

  StepIdentificationAssignment resolveIdentificationAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveIdentificationAssignment(instance);
  }

  StepAppliedIdentificationAssignment resolveAppliedIdentificationAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveAppliedIdentificationAssignment(instance);
  }

  StepExternalIdentificationAssignment resolveExternalIdentificationAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveExternalIdentificationAssignment(instance);
  }

  StepAppliedExternalIdentificationAssignment resolveAppliedExternalIdentificationAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveAppliedExternalIdentificationAssignment(instance);
  }

  StepNameAssignment resolveNameAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveNameAssignment(instance);
  }

  StepAppliedNameAssignment resolveAppliedNameAssignment(StepEntityInstance instance) {
    return assignmentResolver.resolveAppliedNameAssignment(instance);
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

  StepActionPropertyRepresentation resolveActionPropertyRepresentation(StepEntityInstance instance) {
    return representationResolver.resolveActionPropertyRepresentation(instance);
  }

  StepContactRatioRepresentation resolveContactRatioRepresentation(StepEntityInstance instance) {
    return representationResolver.resolveContactRatioRepresentation(instance);
  }

  StepKinematicPropertyDefinitionRepresentation resolveKinematicPropertyDefinitionRepresentation(StepEntityInstance instance) {
    return kinematicResolver.resolveKinematicPropertyDefinitionRepresentation(instance);
  }

  StepKinematicPropertyMechanismRepresentation resolveKinematicPropertyMechanismRepresentation(StepEntityInstance instance) {
    return kinematicResolver.resolveKinematicPropertyMechanismRepresentation(instance);
  }

  StepKinematicPropertyRepresentationRelation resolveKinematicPropertyRepresentationRelation(StepEntityInstance instance) {
    return kinematicResolver.resolveKinematicPropertyRepresentationRelation(instance);
  }

  StepKinematicPropertyTopologyRepresentation resolveKinematicPropertyTopologyRepresentation(StepEntityInstance instance) {
    return kinematicResolver.resolveKinematicPropertyTopologyRepresentation(instance);
  }

  StepPlacedDatumTargetFeature resolvePlacedDatumTargetFeature(StepEntityInstance instance) {
    return geometricFeatureResolver.resolvePlacedDatumTargetFeature(instance);
  }

  StepResourcePropertyRepresentation resolveResourcePropertyRepresentation(StepEntityInstance instance) {
    return representationResolver.resolveResourcePropertyRepresentation(instance);
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

  StepCartesianTransformationOperator resolveCartesianTransformationOperator2D(StepEntityInstance instance) {
    return transformationResolver.resolveCartesianTransformationOperator2D(instance);
  }

  StepCartesianTransformationOperator resolveCartesianTransformationOperator3D(StepEntityInstance instance) {
    return transformationResolver.resolveCartesianTransformationOperator3D(instance);
  }

  StepUserDefinedMarker resolveUserDefinedMarker(StepEntityInstance instance) {
    return materialResolver.resolveUserDefinedMarker(instance);
  }

  StepUserDefinedCurveFont resolveUserDefinedCurveFont(StepEntityInstance instance) {
    return materialResolver.resolveUserDefinedCurveFont(instance);
  }

  StepUserDefinedTerminatorSymbol resolveUserDefinedTerminatorSymbol(StepEntityInstance instance) {
    return draughtingResolver.resolveUserDefinedTerminatorSymbol(instance);
  }

  StepItemDefinedTransformation resolveItemDefinedTransformation(StepEntityInstance instance) {
    return transformationResolver.resolveItemDefinedTransformation(instance);
  }

  StepRepresentationRelationshipWithTransformation resolveRepresentationRelationshipWithTransformation(StepEntityInstance instance) {
    return transformationResolver.resolveRepresentationRelationshipWithTransformation(instance);
  }

  StepRepresentationRelationship resolveRepresentationRelationship(StepEntityInstance instance) {
    return representationResolver.resolveRepresentationRelationship(instance);
  }

  StepRepresentationRelationship resolveRepresentationRelationship(StepEntityInstance instance, String entityName) {
    return representationResolver.resolveRepresentationRelationship(instance, entityName);
  }

  StepShapeRepresentationRelationship resolveShapeRepresentationRelationship(StepEntityInstance instance) {
    return representationResolver.resolveShapeRepresentationRelationship(instance);
  }

  StepUncertaintyMeasureWithUnit resolveUncertaintyMeasureWithUnit(StepEntityInstance instance) {
    return unitResolver.resolveUncertaintyMeasureWithUnit(instance);
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

  StepMeasureWithUnit resolveMeasureWithUnit(StepEntityInstance instance) {
    return unitResolver.resolveMeasureWithUnit(instance);
  }

  StepTypedMeasureWithUnit resolveTypedMeasureWithUnit(StepEntityInstance instance, String entityName, String expectedUnitKind) {
    return unitResolver.resolveTypedMeasureWithUnit(instance, entityName, expectedUnitKind);
  }

  StepDerivedUnitElement resolveDerivedUnitElement(StepEntityInstance instance) {
    return unitResolver.resolveDerivedUnitElement(instance);
  }

  StepDerivedUnit resolveDerivedUnit(StepEntityInstance instance) {
    return unitResolver.resolveDerivedUnit(instance);
  }

  StepGeometricRepresentationContext resolveGeometricRepresentationContext(StepEntityInstance instance) {
    return representationResolver.resolveGeometricRepresentationContext(instance);
  }

  StepNamedUnit resolveNamedUnit(StepEntityInstance instance) {
    return unitResolver.resolveNamedUnit(instance);
  }

  StepDimensionalExponents resolveDimensionalExponents(StepEntityInstance instance) {
    return unitResolver.resolveDimensionalExponents(instance);
  }

  StepNamedUnit resolveStandaloneUnitKind(StepEntityInstance instance, String entityName) {
    return unitResolver.resolveStandaloneUnitKind(instance, entityName);
  }

  StepContextDependentUnit resolveContextDependentUnit(StepEntityInstance instance) {
    return unitResolver.resolveContextDependentUnit(instance);
  }

  StepConversionBasedUnit resolveConversionBasedUnit(StepEntityInstance instance, String entityName) {
    return unitResolver.resolveConversionBasedUnit(instance, entityName);
  }

  StepConversionBasedUnitWithOffset resolveConversionBasedUnitWithOffset(StepEntityInstance instance) {
    return unitResolver.resolveConversionBasedUnitWithOffset(instance);
  }

  StepDerivedUnit resolveStandaloneDerivedUnitKind(StepEntityInstance instance, String entityName) {
    return unitResolver.resolveStandaloneDerivedUnitKind(instance, entityName);
  }

  StepSiUnit resolveSiUnit(StepEntityInstance instance) {
    return unitResolver.resolveSiUnit(instance);
  }

  void validateNamedUnitDimensions(StepEntityInstance instance) {
    if (!instance.hasDefinition("NAMED_UNIT")) {
      return;
    }
    StepEntityDefinition definition = definition(instance, "NAMED_UNIT");
    requireParameterCount(instance, definition, 1);
    StepValue dimensions = unwrapTyped(definition.parameters().get(0));
    if (isUnset(dimensions)) {
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

  StepSurface resolveSurface(StepEntityInstance instance) {
    return surfaceResolver.resolveSurface(instance);
  }

  StepBoundedCurve resolveBoundedCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BOUNDED_CURVE");
    requireParameterCount(instance, definition, 0);
    return new StepBoundedCurve(instance.id(), inheritedRepresentationItemName(instance));
  }

  StepUniformCurve resolveUniformCurve(StepEntityInstance instance) {
    return bezierResolver.resolveUniformCurve(instance);
  }

  StepBezierCurve resolveBezierCurve(StepEntityInstance instance) {
    return bezierResolver.resolveBezierCurve(instance);
  }

  StepPiecewiseBezierCurve resolvePiecewiseBezierCurve(StepEntityInstance instance) {
    return bezierResolver.resolvePiecewiseBezierCurve(instance);
  }

  StepQuasiUniformCurve resolveQuasiUniformCurve(StepEntityInstance instance) {
    return bezierResolver.resolveQuasiUniformCurve(instance);
  }

  StepBoundedSurface resolveBoundedSurface(StepEntityInstance instance) {
    return bezierResolver.resolveBoundedSurface(instance);
  }

  StepUniformSurface resolveUniformSurface(StepEntityInstance instance) {
    return bezierResolver.resolveUniformSurface(instance);
  }

  StepBezierSurface resolveBezierSurface(StepEntityInstance instance) {
    return bezierResolver.resolveBezierSurface(instance);
  }

  StepPiecewiseBezierSurface resolvePiecewiseBezierSurface(StepEntityInstance instance) {
    return bezierResolver.resolvePiecewiseBezierSurface(instance);
  }

  StepQuasiUniformSurface resolveQuasiUniformSurface(StepEntityInstance instance) {
    return bezierResolver.resolveQuasiUniformSurface(instance);
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

  StepSurfaceModel resolveSurfaceModel(StepEntityInstance instance) {
    return surfaceResolver.resolveSurfaceModel(instance);
  }

  StepSolidModel resolveSolidModel(StepEntityInstance instance) {
    return solidResolver.resolveSolidModel(instance);
  }

  StepCsgSolid resolveCsgSolid(StepEntityInstance instance) {
    return solidResolver.resolveCsgSolid(instance);
  }

  StepSolidReplica resolveSolidReplica(StepEntityInstance instance) {
    return solidResolver.resolveSolidReplica(instance);
  }

  StepCsgPrimitive resolveCsgPrimitive(StepEntityInstance instance,
      String entityName,
      Class<? extends StepEntity> positionType,
      String positionTypeName,
      int dimensionCount) {
    return solidResolver.resolveCsgPrimitive(instance, entityName, positionType, positionTypeName, dimensionCount);
  }

  StepProfileDef resolveCircleProfileDef(StepEntityInstance instance) {
    return profileResolver.resolveCircleProfileDef(instance);
  }

  StepProfileDef resolveRectangleProfileDef(StepEntityInstance instance) {
    return profileResolver.resolveRectangleProfileDef(instance);
  }

  StepProfileDef resolveParameterizedProfileDef(StepEntityInstance instance, String entityName, int parameterCount) {
    return profileResolver.resolveParameterizedProfileDef(instance, entityName, parameterCount);
  }

  StepProfileDef resolveArbitraryClosedProfileDef(StepEntityInstance instance) {
    return profileResolver.resolveArbitraryClosedProfileDef(instance);
  }

  StepProfileDef resolveArbitraryProfileDefWithVoids(StepEntityInstance instance) {
    return profileResolver.resolveArbitraryProfileDefWithVoids(instance);
  }

  StepProfileDef resolveArbitraryProfileDef(StepEntityInstance instance, String entityName) {
    return profileResolver.resolveArbitraryProfileDef(instance, entityName);
  }

  StepProfileDef resolveProfileDef(StepEntityInstance instance) {
    return profileResolver.resolveProfileDef(instance);
  }

  StepProfileDef resolveProfileDefSubtype(StepEntityInstance instance, StepEntityDefinition concrete) {
    return profileResolver.resolveProfileDefSubtype(instance, concrete);
  }

  boolean isSupportedArbitraryProfileCurve(StepEntity curve) {
    return curve instanceof StepCurve
        || curve instanceof StepPolyline
        || curve instanceof StepCompositeCurve;
  }

  StepSweptAreaSolid resolveExtrudedAreaSolid(StepEntityInstance instance) {
    return solidResolver.resolveExtrudedAreaSolid(instance);
  }

  StepSweptAreaSolid resolveRevolvedAreaSolid(StepEntityInstance instance) {
    return solidResolver.resolveRevolvedAreaSolid(instance);
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

  StepHalfSpaceSolid resolveHalfSpaceSolid(StepEntityInstance instance) {
    return solidResolver.resolveHalfSpaceSolid(instance);
  }

  StepHalfSpaceSolid resolveBoxedHalfSpace(StepEntityInstance instance) {
    return solidResolver.resolveBoxedHalfSpace(instance);
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

  StepTopologicalRepresentationItem resolveTopologicalRepresentationItem(StepEntityInstance instance) {
    return representationResolver.resolveTopologicalRepresentationItem(instance);
  }

  StepVertex resolveVertex(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "VERTEX");
    requireParameterCount(instance, definition, 0);
    return new StepVertex(instance.id(), inheritedTopologicalRepresentationItemName(instance));
  }

  StepEdge resolveEdge(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "EDGE");
    requireParameterCount(instance, definition, 0);
    return new StepEdge(instance.id(), inheritedTopologicalRepresentationItemName(instance));
  }

  StepFace resolveFace(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FACE");
    requireParameterCount(instance, definition, 0);
    return new StepFace(instance.id(), inheritedTopologicalRepresentationItemName(instance));
  }

  StepColourRgb resolveColourRgb(StepEntityInstance instance) {
    return materialResolver.resolveColourRgb(instance);
  }

  StepColour resolveColour(StepEntityInstance instance) {
    return materialResolver.resolveColour(instance);
  }

  StepColourSpecification resolveColourSpecification(StepEntityInstance instance) {
    return materialResolver.resolveColourSpecification(instance);
  }

  StepDraughtingPreDefinedCurveFont resolveDraughtingPreDefinedCurveFont(StepEntityInstance instance) {
    return draughtingResolver.resolveDraughtingPreDefinedCurveFont(instance);
  }

  StepPreDefinedCurveFont resolvePreDefinedCurveFont(StepEntityInstance instance) {
    return materialResolver.resolvePreDefinedCurveFont(instance);
  }

  StepPreDefinedItem resolvePreDefinedItem(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRE_DEFINED_ITEM");
    requireParameterCount(instance, definition, 1);
    return new StepPreDefinedItem(instance.id(), stringValue(instance, definition, 0));
  }

  StepPreDefinedMarker resolvePreDefinedMarker(StepEntityInstance instance) {
    return materialResolver.resolvePreDefinedMarker(instance);
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

  StepPreDefinedTerminatorSymbol resolvePreDefinedTerminatorSymbol(StepEntityInstance instance) {
    return draughtingResolver.resolvePreDefinedTerminatorSymbol(instance);
  }

  StepPreDefinedSurfaceSideStyle resolvePreDefinedSurfaceSideStyle(StepEntityInstance instance) {
    return materialResolver.resolvePreDefinedSurfaceSideStyle(instance);
  }

  StepDraughtingPreDefinedTextFont resolveDraughtingPreDefinedTextFont(StepEntityInstance instance) {
    return draughtingResolver.resolveDraughtingPreDefinedTextFont(instance);
  }

  StepPreDefinedTextFont resolvePreDefinedTextFont(StepEntityInstance instance) {
    return materialResolver.resolvePreDefinedTextFont(instance);
  }

  StepDraughtingPreDefinedColour resolveDraughtingPreDefinedColour(StepEntityInstance instance) {
    return draughtingResolver.resolveDraughtingPreDefinedColour(instance);
  }

  StepPreDefinedColour resolvePreDefinedColour(StepEntityInstance instance) {
    return materialResolver.resolvePreDefinedColour(instance);
  }

  StepCurveStyle resolveCurveStyle(StepEntityInstance instance) {
    return materialResolver.resolveCurveStyle(instance);
  }

  StepPointStyle resolvePointStyle(StepEntityInstance instance) {
    return materialResolver.resolvePointStyle(instance);
  }

  StepCharacterGlyphStyleStroke resolveCharacterGlyphStyleStroke(StepEntityInstance instance) {
    return draughtingResolver.resolveCharacterGlyphStyleStroke(instance);
  }

  StepCharacterGlyphStyleOutline resolveCharacterGlyphStyleOutline(StepEntityInstance instance) {
    return draughtingResolver.resolveCharacterGlyphStyleOutline(instance);
  }

  StepCharacterGlyphStyleOutlineWithCharacteristics resolveCharacterGlyphStyleOutlineWithCharacteristics(StepEntityInstance instance) {
    return draughtingResolver.resolveCharacterGlyphStyleOutlineWithCharacteristics(instance);
  }

  StepTextStyleForDefinedFont resolveTextStyleForDefinedFont(StepEntityInstance instance) {
    return materialResolver.resolveTextStyleForDefinedFont(instance);
  }

  StepTextStyle resolveTextStyle(StepEntityInstance instance) {
    return materialResolver.resolveTextStyle(instance);
  }

  StepTextStyleWithSpacing resolveTextStyleWithSpacing(StepEntityInstance instance) {
    return materialResolver.resolveTextStyleWithSpacing(instance);
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

  StepTextStyleWithMirror resolveTextStyleWithMirror(StepEntityInstance instance) {
    return materialResolver.resolveTextStyleWithMirror(instance);
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

  StepSymbolColour resolveSymbolColour(StepEntityInstance instance) {
    return materialResolver.resolveSymbolColour(instance);
  }

  StepSymbolStyle resolveSymbolStyle(StepEntityInstance instance) {
    return materialResolver.resolveSymbolStyle(instance);
  }

  StepFillAreaStyleColour resolveFillAreaStyleColour(StepEntityInstance instance) {
    return materialResolver.resolveFillAreaStyleColour(instance);
  }

  StepFillAreaStyle resolveFillAreaStyle(StepEntityInstance instance) {
    return materialResolver.resolveFillAreaStyle(instance);
  }

  StepSurfaceStyleFillArea resolveSurfaceStyleFillArea(StepEntityInstance instance) {
    return materialResolver.resolveSurfaceStyleFillArea(instance);
  }

  StepCurveStyle requireCurveStyleReference(
      StepEntityInstance instance, StepEntityDefinition definition, String entityName) {
    return requireEntity(
        referenceId(instance, definition, 0),
        StepCurveStyle.class,
        entityName + " style must reference CURVE_STYLE");
  }

  StepSurfaceStyleBoundary resolveSurfaceStyleBoundary(StepEntityInstance instance) {
    return materialResolver.resolveSurfaceStyleBoundary(instance);
  }

  StepSurfaceStyleControlGrid resolveSurfaceStyleControlGrid(StepEntityInstance instance) {
    return materialResolver.resolveSurfaceStyleControlGrid(instance);
  }

  StepSurfaceStyleSegmentationCurve resolveSurfaceStyleSegmentationCurve(StepEntityInstance instance) {
    return materialResolver.resolveSurfaceStyleSegmentationCurve(instance);
  }

  StepSurfaceStyleSilhouette resolveSurfaceStyleSilhouette(StepEntityInstance instance) {
    return materialResolver.resolveSurfaceStyleSilhouette(instance);
  }

  StepSurfaceStyleTransparent resolveSurfaceStyleTransparent(StepEntityInstance instance) {
    return materialResolver.resolveSurfaceStyleTransparent(instance);
  }

  StepSurfaceStyleReflectanceAmbient resolveSurfaceStyleReflectanceAmbient(StepEntityInstance instance) {
    return materialResolver.resolveSurfaceStyleReflectanceAmbient(instance);
  }

  StepSurfaceStyleReflectanceAmbientDiffuse resolveSurfaceStyleReflectanceAmbientDiffuse(StepEntityInstance instance) {
    return materialResolver.resolveSurfaceStyleReflectanceAmbientDiffuse(instance);
  }

  StepSurfaceStyleReflectanceAmbientDiffuseSpecular resolveSurfaceStyleReflectanceAmbientDiffuseSpecular(StepEntityInstance instance) {
    return materialResolver.resolveSurfaceStyleReflectanceAmbientDiffuseSpecular(instance);
  }

  StepSurfaceStyleParameterLine resolveSurfaceStyleParameterLine(StepEntityInstance instance) {
    return materialResolver.resolveSurfaceStyleParameterLine(instance);
  }

  StepSurfaceSideStyle resolveSurfaceSideStyle(StepEntityInstance instance) {
    return materialResolver.resolveSurfaceSideStyle(instance);
  }

  StepSurfaceStyleUsage resolveSurfaceStyleUsage(StepEntityInstance instance) {
    return materialResolver.resolveSurfaceStyleUsage(instance);
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

  StepStyledItem resolveStyledItem(StepEntityInstance instance) {
    return materialResolver.resolveStyledItem(instance);
  }

  StepOverRidingStyledItem resolveOverRidingStyledItem(StepEntityInstance instance) {
    return materialResolver.resolveOverRidingStyledItem(instance);
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

  StepAnnotationTextOccurrence resolveAnnotationTextOccurrence(StepEntityInstance instance) {
    return draughtingResolver.resolveAnnotationTextOccurrence(instance);
  }

  StepAnnotationText resolveAnnotationText(StepEntityInstance instance) {
    return annotationResolver.resolveAnnotationText(instance);
  }

  StepAnnotationTextCharacter resolveAnnotationTextCharacter(StepEntityInstance instance) {
    return draughtingResolver.resolveAnnotationTextCharacter(instance);
  }

  StepAnnotationSymbol resolveAnnotationSymbol(StepEntityInstance instance) {
    return annotationResolver.resolveAnnotationSymbol(instance);
  }

  StepAnnotationSymbolOccurrence resolveAnnotationSymbolOccurrence(StepEntityInstance instance) {
    return draughtingResolver.resolveAnnotationSymbolOccurrence(instance);
  }

  StepAnnotationSubfigureOccurrence resolveAnnotationSubfigureOccurrence(StepEntityInstance instance) {
    return draughtingResolver.resolveAnnotationSubfigureOccurrence(instance);
  }

  StepDraughtingAnnotationOccurrence resolveDraughtingAnnotationOccurrence(StepEntityInstance instance) {
    return draughtingResolver.resolveDraughtingAnnotationOccurrence(instance);
  }

  StepTerminatorSymbol resolveTerminatorSymbol(StepEntityInstance instance) {
    return draughtingResolver.resolveTerminatorSymbol(instance);
  }

  StepAnnotationOccurrenceRelationship resolveAnnotationOccurrenceRelationship(StepEntityInstance instance) {
    return draughtingResolver.resolveAnnotationOccurrenceRelationship(instance);
  }

  StepAnnotationOccurrenceRelationship resolveAnnotationOccurrenceRelationship(StepEntityInstance instance, String entityName) {
    return draughtingResolver.resolveAnnotationOccurrenceRelationship(instance, entityName);
  }

  StepAnnotationPointOccurrence resolveAnnotationPointOccurrence(StepEntityInstance instance) {
    return draughtingResolver.resolveAnnotationPointOccurrence(instance);
  }

  StepAnnotationCurveOccurrence resolveAnnotationCurveOccurrence(StepEntityInstance instance) {
    return draughtingResolver.resolveAnnotationCurveOccurrence(instance);
  }

  StepLeaderCurve resolveLeaderCurve(StepEntityInstance instance) {
    return annotationResolver.resolveLeaderCurve(instance);
  }

  StepProjectionCurve resolveProjectionCurve(StepEntityInstance instance) {
    return annotationResolver.resolveProjectionCurve(instance);
  }

  StepDimensionCurve resolveDimensionCurve(StepEntityInstance instance) {
    return annotationResolver.resolveDimensionCurve(instance);
  }

  StepAnnotationFillArea resolveAnnotationFillArea(StepEntityInstance instance) {
    return annotationResolver.resolveAnnotationFillArea(instance);
  }

  StepAnnotationFillAreaOccurrence resolveAnnotationFillAreaOccurrence(StepEntityInstance instance) {
    return draughtingResolver.resolveAnnotationFillAreaOccurrence(instance);
  }

  StepAnnotationPlaceholderOccurrence resolveAnnotationPlaceholderOccurrence(StepEntityInstance instance) {
    return draughtingResolver.resolveAnnotationPlaceholderOccurrence(instance);
  }

  StepEntity requireSupportedPlaceholderItem(StepEntity item) {
    if (!isSupportedAnnotationPlaneElement(item)) {
      throw new UnsupportedStepEntityException(
          "ANNOTATION_PLACEHOLDER_OCCURRENCE item must reference supported point carriers or point-like annotation content/occurrences");
    }
    return item;
  }

  StepAnnotationPlane resolveAnnotationPlane(StepEntityInstance instance) {
    return annotationResolver.resolveAnnotationPlane(instance);
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

  StepGeometricSurfaceSet resolveGeometricSurfaceSet(StepEntityInstance instance) {
    return surfaceResolver.resolveGeometricSurfaceSet(instance);
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

  StepIndexedPolyCurve resolveIndexedPolyCurve(StepEntityInstance instance) {
    return curveResolver.resolveIndexedPolyCurve(instance);
  }

  StepSurfaceOfConstantRadius resolveSurfaceOfConstantRadius(StepEntityInstance instance) {
    return surfaceResolver.resolveSurfaceOfConstantRadius(instance);
  }

  StepDegenerateCurve resolveDegenerateCurve(StepEntityInstance instance) {
    return curveResolver.resolveDegenerateCurve(instance);
  }

  StepCircle2D resolveCircle2D(StepEntityInstance instance) {
    return geometryResolver.resolveCircle2D(instance);
  }

  StepEllipse2D resolveEllipse2D(StepEntityInstance instance) {
    return geometryResolver.resolveEllipse2D(instance);
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

  StepLine2D resolveLine2D(StepEntityInstance instance) {
    return geometryResolver.resolveLine2D(instance);
  }

  StepPolyline2D resolvePolyline2D(StepEntityInstance instance) {
    return geometryResolver.resolvePolyline2D(instance);
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

  StepBSplineCurve2D resolveBSplineCurve2D(StepEntityInstance instance) {
    return geometryResolver.resolveBSplineCurve2D(instance);
  }

  StepRationalBSplineCurve2D resolveRationalBSplineCurve2D(StepEntityInstance instance) {
    return geometryResolver.resolveRationalBSplineCurve2D(instance);
  }

  StepBezierCurve2D resolveBezierCurve2D(StepEntityInstance instance) {
    return geometryResolver.resolveBezierCurve2D(instance);
  }

  StepQuasiUniformCurve2D resolveQuasiUniformCurve2D(StepEntityInstance instance) {
    return geometryResolver.resolveQuasiUniformCurve2D(instance);
  }

  StepUniformCurve2D resolveUniformCurve2D(StepEntityInstance instance) {
    return geometryResolver.resolveUniformCurve2D(instance);
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

  StepIndexedPolyCurve2D resolveIndexedPolyCurve2D(StepEntityInstance instance) {
    return curveResolver.resolveIndexedPolyCurve2D(instance);
  }

  StepDegenerateCurve2D resolveDegenerateCurve2D(StepEntityInstance instance) {
    return curveResolver.resolveDegenerateCurve2D(instance);
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

  StepCurve2D resolveCurve2D(StepEntityInstance instance) {
    return curveResolver.resolveCurve2D(instance);
  }

  StepSweptAreaSolid resolveSweptAreaSolid(StepEntityInstance instance, String entityName) {
    return solidResolver.resolveSweptAreaSolid(instance, entityName);
  }

  StepMachinedSurface resolveMachinedSurface(StepEntityInstance instance) {
    return surfaceResolver.resolveMachinedSurface(instance);
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

  StepRectangularCompositeSurface resolveRectangularCompositeSurface(StepEntityInstance instance) {
    return surfaceResolver.resolveRectangularCompositeSurface(instance);
  }

  StepSurfacePatch resolveSurfacePatch(StepEntityInstance instance) {
    return surfaceResolver.resolveSurfacePatch(instance);
  }

  StepCompositeCurveOnSurface3D resolveCompositeCurveOnSurface3D(StepEntityInstance instance) {
    return curveResolver.resolveCompositeCurveOnSurface3D(instance);
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

  StepOffsetSurface2 resolveOffsetSurface2(StepEntityInstance instance) {
    return surfaceResolver.resolveOffsetSurface2(instance);
  }

  StepPolygonalBoundedHalfSpace resolvePolygonalBoundedHalfSpace(StepEntityInstance instance) {
    return tessellationResolver.resolvePolygonalBoundedHalfSpace(instance);
  }

  StepSubface resolveSubface(StepEntityInstance instance) {
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

  StepRectangleHollowProfileDef resolveRectangleHollowProfileDef(StepEntityInstance instance) {
    return profileResolver.resolveRectangleHollowProfileDef(instance);
  }

  StepCentreLineArcProfileDef resolveCentreLineArcProfileDef(StepEntityInstance instance) {
    return profileResolver.resolveCentreLineArcProfileDef(instance);
  }

  StepSweptDiskSolid resolveSweptDiskSolid(StepEntityInstance instance) {
    return solidResolver.resolveSweptDiskSolid(instance);
  }

  StepRuledSurface resolveRuledSurface(StepEntityInstance instance) {
    return surfaceResolver.resolveRuledSurface(instance);
  }

  StepCenteredCircleProfileDef resolveCenteredCircleProfileDef(StepEntityInstance instance) {
    return profileResolver.resolveCenteredCircleProfileDef(instance);
  }

  StepRevolvedAreaSolidTapered resolveRevolvedAreaSolidTapered(StepEntityInstance instance) {
    return solidResolver.resolveRevolvedAreaSolidTapered(instance);
  }

  StepExtrudedAreaSolidTapered resolveExtrudedAreaSolidTapered(StepEntityInstance instance) {
    return solidResolver.resolveExtrudedAreaSolidTapered(instance);
  }

  StepSurfaceCurveSweptAreaSolid resolveSurfaceCurveSweptAreaSolid(StepEntityInstance instance) {
    return solidResolver.resolveSurfaceCurveSweptAreaSolid(instance);
  }

  // Advanced CSG volume resolve methods

  StepCylinderVolume resolveCylinderVolume(StepEntityInstance instance) {
    return solidResolver.resolveCylinderVolume(instance);
  }

  StepRightCircularConeVolume resolveRightCircularConeVolume(StepEntityInstance instance) {
    return solidResolver.resolveRightCircularConeVolume(instance);
  }

  StepSphereVolume resolveSphereVolume(StepEntityInstance instance) {
    return solidResolver.resolveSphereVolume(instance);
  }

  StepTorusVolume resolveTorusVolume(StepEntityInstance instance) {
    return solidResolver.resolveTorusVolume(instance);
  }

  StepPrismVolume resolvePrismVolume(StepEntityInstance instance) {
    return solidResolver.resolvePrismVolume(instance);
  }

  // Swept face solid resolve methods

  StepExtrudedFaceSolid resolveExtrudedFaceSolid(StepEntityInstance instance) {
    return solidResolver.resolveExtrudedFaceSolid(instance);
  }

  StepRevolvedFaceSolid resolveRevolvedFaceSolid(StepEntityInstance instance) {
    return solidResolver.resolveRevolvedFaceSolid(instance);
  }

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

  StepTessellatedFace resolveTessellatedFace(StepEntityInstance instance) {
    return tessellationResolver.resolveTessellatedFace(instance);
  }

  StepTessellatedTriangle resolveTessellatedTriangle(StepEntityInstance instance) {
    return tessellationResolver.resolveTessellatedTriangle(instance);
  }

  StepFiniteElementMesh resolveFiniteElementMesh(StepEntityInstance instance) {
    return tessellationResolver.resolveFiniteElementMesh(instance);
  }

  boolean isSupportedAnnotationCurveCarrier(StepEntity item) {
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
        || isAnnotationOccurrence(item);
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

  boolean isSupportedGeometricSetElement(StepEntity element) {
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

  StepMotionPath resolveMotionPath(StepEntityInstance instance) {
    return kinematicResolver.resolveMotionPath(instance);
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

  StepDraughtingCalloutRelationship resolveDraughtingCalloutRelationship(StepEntityInstance instance) {
    return annotationResolver.resolveDraughtingCalloutRelationship(instance);
  }

  StepDraughtingCalloutRelationship resolveDraughtingCalloutRelationship(StepEntityInstance instance, String entityName) {
    return annotationResolver.resolveDraughtingCalloutRelationship(instance, entityName);
  }

  StepMeasureRepresentationItem resolveMeasureRepresentationItem(StepEntityInstance instance) {
    return unitResolver.resolveMeasureRepresentationItem(instance);
  }

  StepDescriptiveRepresentationItem resolveDescriptiveRepresentationItem(StepEntityInstance instance) {
    return representationResolver.resolveDescriptiveRepresentationItem(instance);
  }

  StepValueRepresentationItem resolveValueRepresentationItem(StepEntityInstance instance) {
    return representationResolver.resolveValueRepresentationItem(instance);
  }

  StepItemIdentifiedRepresentationUsage resolveItemIdentifiedRepresentationUsage(StepEntityInstance instance) {
    return representationResolver.resolveItemIdentifiedRepresentationUsage(instance);
  }

  StepChainBasedItemIdentifiedRepresentationUsage resolveChainBasedItemIdentifiedRepresentationUsage(StepEntityInstance instance) {
    return associationResolver.resolveChainBasedItemIdentifiedRepresentationUsage(instance);
  }

  StepChainBasedGeometricItemSpecificUsage resolveChainBasedGeometricItemSpecificUsage(StepEntityInstance instance) {
    return associationResolver.resolveChainBasedGeometricItemSpecificUsage(instance);
  }

  StepPmiRequirementItemAssociation resolvePmiRequirementItemAssociation(StepEntityInstance instance) {
    return associationResolver.resolvePmiRequirementItemAssociation(instance);
  }

  StepMechanicalDesignRequirementItemAssociation resolveMechanicalDesignRequirementItemAssociation(StepEntityInstance instance) {
    return associationResolver.resolveMechanicalDesignRequirementItemAssociation(instance);
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

  StepDraughtingModelItemAssociation resolveDraughtingModelItemAssociation(StepEntityInstance instance) {
    return associationResolver.resolveDraughtingModelItemAssociation(instance);
  }

  StepDraughtingModelItemAssociationWithPlaceholder resolveDraughtingModelItemAssociationWithPlaceholder(StepEntityInstance instance) {
    return associationResolver.resolveDraughtingModelItemAssociationWithPlaceholder(instance);
  }

  StepGeometricItemSpecificUsage resolveGeometricItemSpecificUsage(StepEntityInstance instance) {
    return associationResolver.resolveGeometricItemSpecificUsage(instance);
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

  StepThread resolveThread(StepEntityInstance instance) {
    return machiningResolver.resolveThread(instance);
  }

  StepShapeAspectOccurrence resolveShapeAspectOccurrence(StepEntityInstance instance) {
    return representationResolver.resolveShapeAspectOccurrence(instance);
  }

  StepEntityDefinition definition(StepEntityInstance instance, String name) {
    return instance.requireDefinition(name);
  }

  static void requireParameterCount(
      StepEntityInstance instance, StepEntityDefinition definition, int expected) {
    StepParameterReader.requireParameterCount(instance, definition, expected);
  }

  static void requireParameterCountIn(
      StepEntityInstance instance, StepEntityDefinition definition, int... expectedCounts) {
    StepParameterReader.requireParameterCountIn(instance, definition, expectedCounts);
  }

  String stringValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.StringValue) {
      StepValue.StringValue stringValue = (StepValue.StringValue) value;
      return stringValue.value();
    }
    throw StepParameterReader.parameterTypeMismatch(instance, definition, index, "string");
  }

  String optionalStringValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = definition.parameters().get(index);
    if (isUnset(value)) {
      return "";
    }
    return stringValue(instance, definition, index);
  }

  List<String> optionalStringListValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = definition.parameters().get(index);
    if (isUnset(value)) {
      return List.of();
    }
    StepValue unwrapped = unwrapTyped(value);
    if (!(unwrapped instanceof StepValue.ListValue)) {
      throw StepParameterReader.parameterTypeMismatch(instance, definition, index, "string list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) unwrapped;
    List<String> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrappedElement = unwrapTyped(element);
      if (!(unwrappedElement instanceof StepValue.StringValue)) {
        throw new StepResolutionException(
            definition.name() + " string list must contain only strings");
      }
      StepValue.StringValue stringValue = (StepValue.StringValue) unwrappedElement;
      result.add(stringValue.value());
    }
    return List.copyOf(result);
  }

  double numberValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.NumberValue) {
      StepValue.NumberValue numberValue = (StepValue.NumberValue) value;
      return numberValue.value();
    }
    throw StepParameterReader.parameterTypeMismatch(instance, definition, index, "number");
  }

  int integerValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    double value = numberValue(instance, definition, index);
    if (value != Math.rint(value)) {
      throw new StepResolutionException(
          "entity #"
              + instance.id()
              + " "
              + definition.name()
              + " parameter "
              + index
              + " type mismatch: expected integer, actual number");
    }
    return (int) value;
  }

  Integer optionalIntegerValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = definition.parameters().get(index);
    if (isUnset(value)) {
      return null;
    }
    return integerValue(instance, definition, index);
  }

  Double optionalNumberValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = definition.parameters().get(index);
    if (isUnset(value)) {
      return null;
    }
    return numberValue(instance, definition, index);
  }

  StepDirection optionalDirectionReference(
      StepEntityInstance instance, StepEntityDefinition definition, int index, String message) {
    StepValue value = definition.parameters().get(index);
    if (isUnset(value)) {
      return null;
    }
    return requireEntity(referenceId(instance, definition, index), StepDirection.class, message);
  }

  String enumValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.EnumValue) {
      StepValue.EnumValue enumValue = (StepValue.EnumValue) value;
      return enumValue.value();
    }
    throw StepParameterReader.parameterTypeMismatch(instance, definition, index, "enum");
  }

  boolean booleanValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    String enumVal = enumValue(instance, definition, index);
    if ("T".equals(enumVal)) {
      return true;
    }
    if ("F".equals(enumVal)) {
      return false;
    }
    throw new StepResolutionException(
        "entity #"
            + instance.id()
            + " "
            + definition.name()
            + " parameter "
            + index
            + " type mismatch: expected boolean .T. or .F., actual enum ."
            + enumVal
            + ".");
  }

  int referenceId(StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.ReferenceValue) {
      StepValue.ReferenceValue referenceValue = (StepValue.ReferenceValue) value;
      return referenceValue.id();
    }
    throw StepParameterReader.parameterTypeMismatch(instance, definition, index, "reference");
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

  List<Double> coordinateTriple(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    return coordinateList(instance, definition, index, 3, 3);
  }

  List<Double> doubleList(StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue)) {
      throw StepParameterReader.parameterTypeMismatch(instance, definition, index, "list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    return listValue.elements().stream()
        .map(v -> numberValueFrom(instance, v, definition, index))
        .collect(Collectors.toList());
  }

  double numberValueFrom(
      StepEntityInstance instance, StepValue value, StepEntityDefinition definition, int index) {
    value = unwrapTyped(value);
    if (!(value instanceof StepValue.NumberValue)) {
      throw parameterElementTypeMismatch(instance, definition, index, "number", value);
    }
    StepValue.NumberValue numberValue = (StepValue.NumberValue) value;
    return numberValue.value();
  }

  List<Double> coordinateList(
      StepEntityInstance instance,
      StepEntityDefinition definition,
      int index,
      int minSize,
      int maxSize) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue)) {
      throw StepParameterReader.parameterTypeMismatch(instance, definition, index, "list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    if (listValue.elements().size() < minSize || listValue.elements().size() > maxSize) {
      throw new UnsupportedStepEntityException(
          definition.name() + " only supports " + minSize + "D to " + maxSize + "D coordinates");
    }
    List<Double> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (unwrapped instanceof StepValue.NumberValue) {
        StepValue.NumberValue numberValue = (StepValue.NumberValue) unwrapped;
        result.add(numberValue.value());
      } else {
        throw parameterElementTypeMismatch(instance, definition, index, "number", element);
      }
    }
    return List.copyOf(result);
  }

  List<Double> numberList(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue)) {
      throw StepParameterReader.parameterTypeMismatch(instance, definition, index, "list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    List<Double> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.NumberValue)) {
        throw parameterElementTypeMismatch(instance, definition, index, "number", element);
      }
      StepValue.NumberValue numberValue = (StepValue.NumberValue) unwrapped;
      result.add(numberValue.value());
    }
    return List.copyOf(result);
  }

  List<Integer> intList(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue)) {
      throw StepParameterReader.parameterTypeMismatch(instance, definition, index, "list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    List<Integer> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.NumberValue)) {
        throw parameterElementTypeMismatch(instance, definition, index, "number", element);
      }
      StepValue.NumberValue numberValue = (StepValue.NumberValue) unwrapped;
      result.add((int) numberValue.value());
    }
    return List.copyOf(result);
  }

  List<String> stringList(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue)) {
      throw StepParameterReader.parameterTypeMismatch(instance, definition, index, "list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    List<String> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.StringValue)) {
        throw parameterElementTypeMismatch(instance, definition, index, "string", element);
      }
      StepValue.StringValue strValue = (StepValue.StringValue) unwrapped;
      result.add(strValue.value());
    }
    return List.copyOf(result);
  }

  String logicalValue(StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.EnumValue) {
      StepValue.EnumValue enumValue = (StepValue.EnumValue) value;
      return enumValue.value();
    }
    if (value instanceof StepValue.StringValue) {
      StepValue.StringValue strValue = (StepValue.StringValue) value;
      return strValue.value();
    }
    throw StepParameterReader.parameterTypeMismatch(
        instance, definition, index, "LOGICAL value (.T., .F., or .U.)");
  }

  /**
   * Extracts a list of numbers from a pre-unwrapped StepValue.
   * Useful when the caller has already handled nested list unwrapping.
   */
  List<Double> extractNumberList(StepEntityDefinition definition, StepValue value, String paramName) {
    if (!(value instanceof StepValue.ListValue)) {
      throw new StepResolutionException(paramName + " parameter must be a list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    List<Double> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.NumberValue)) {
        throw new StepResolutionException(paramName + " numeric list must contain only numbers");
      }
      StepValue.NumberValue numberValue = (StepValue.NumberValue) unwrapped;
      result.add(numberValue.value());
    }
    return List.copyOf(result);
  }

  List<String> literalList(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    List<String> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      result.add(literalText(element));
    }
    return List.copyOf(result);
  }

  List<List<Double>> numberGrid(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue)) {
      throw StepParameterReader.parameterTypeMismatch(instance, definition, index, "nested list");
    }
    StepValue.ListValue outerList = (StepValue.ListValue) value;
    List<List<Double>> grid = new ArrayList<>(outerList.elements().size());
    for (StepValue rowValue : outerList.elements()) {
      StepValue row = unwrapTyped(rowValue);
      if (!(row instanceof StepValue.ListValue)) {
        throw parameterElementTypeMismatch(instance, definition, index, "nested numeric list", rowValue);
      }
      StepValue.ListValue rowList = (StepValue.ListValue) row;
      List<Double> entries = new ArrayList<>(rowList.elements().size());
      for (StepValue element : rowList.elements()) {
        StepValue unwrapped = unwrapTyped(element);
        if (!(unwrapped instanceof StepValue.NumberValue)) {
          throw parameterElementTypeMismatch(instance, definition, index, "number", element);
        }
        StepValue.NumberValue numberValue = (StepValue.NumberValue) unwrapped;
        entries.add(numberValue.value());
      }
      grid.add(List.copyOf(entries));
    }
    return List.copyOf(grid);
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
        throw parameterElementTypeMismatch(instance, definition, index, "nested reference list", rowValue);
      }
      StepValue.ListValue rowList = (StepValue.ListValue) row;
      List<T> entries = new ArrayList<>(rowList.elements().size());
      for (StepValue element : rowList.elements()) {
        StepValue unwrapped = unwrapTyped(element);
        if (!(unwrapped instanceof StepValue.ReferenceValue)) {
          throw parameterElementTypeMismatch(instance, definition, index, "reference", element);
        }
        StepValue.ReferenceValue referenceValue = (StepValue.ReferenceValue) unwrapped;
        entries.add(requireEntity(referenceValue.id(), type, message));
      }
      grid.add(List.copyOf(entries));
    }
    return List.copyOf(grid);
  }

  List<Integer> integerList(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    List<Double> values = numberList(instance, definition, index);
    List<Integer> result = new ArrayList<>(values.size());
    for (double value : values) {
      if (value != Math.rint(value)) {
        throw new StepResolutionException(
            "entity #"
                + instance.id()
                + " "
                + definition.name()
                + " parameter "
                + index
                + " element type mismatch: expected integer, actual number");
      }
      result.add((int) value);
    }
    return List.copyOf(result);
  }

  StepResolutionException parameterElementTypeMismatch(
      StepEntityInstance instance,
      StepEntityDefinition definition,
      int index,
      String expected,
      StepValue actualValue) {
    return new StepResolutionException(
        "entity #"
            + instance.id()
            + " "
            + definition.name()
            + " parameter "
            + index
            + " element type mismatch: expected "
            + expected
            + ", actual "
            + StepParameterReader.valueType(actualValue));
  }

  boolean isUnset(StepValue value) {
    return StepParameterReader.isUnset(value);
  }

  // ---------------------------------------------------------------------------
  // SELECT type handling helpers (C10)
  // ---------------------------------------------------------------------------

  /** Wrapper for StepParameterReader.typedSelection with entity ID context. */
  StepParameterReader.TypedSelection typedSelection(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    return StepParameterReader.typedSelection(instance, definition, index);
  }

  /** Wrapper for StepParameterReader.optionalTypedSelection with entity ID context. */
  StepParameterReader.TypedSelection optionalTypedSelection(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    return StepParameterReader.optionalTypedSelection(instance, definition, index);
  }

  /** Wrapper for StepParameterReader.validateSelectTypeName. */
  void validateSelectTypeName(
      StepEntityInstance instance,
      StepEntityDefinition definition,
      int index,
      StepParameterReader.TypedSelection selection,
      java.util.Set<String> allowedTypes) {
    StepParameterReader.validateSelectTypeName(instance, definition, index, selection, allowedTypes);
  }

  /** Wrapper for StepParameterReader.validateSelectTypeKnown. */
  void validateSelectTypeKnown(
      StepEntityInstance instance,
      StepEntityDefinition definition,
      int index,
      StepParameterReader.TypedSelection selection) {
    StepParameterReader.validateSelectTypeKnown(instance, definition, index, selection);
  }

  // ---------------------------------------------------------------------------
  // Core value helpers
  // ---------------------------------------------------------------------------

  StepValue unwrapTyped(StepValue value) {
    StepValue current = value;
    while (current instanceof StepValue.TypedValue) {
      StepValue.TypedValue typedValue = (StepValue.TypedValue) current;
      current = typedValue.value();
    }
    return current;
  }

  String literalText(StepValue value) {
    if (value instanceof StepValue.StringValue) {
      StepValue.StringValue stringValue = (StepValue.StringValue) value;
      return stringValue.value();
    }
    if (value instanceof StepValue.NumberValue) {
      StepValue.NumberValue numberValue = (StepValue.NumberValue) value;
      return numberValue.raw();
    }
    if (value instanceof StepValue.EnumValue) {
      StepValue.EnumValue enumValue = (StepValue.EnumValue) value;
      return "." + enumValue.value() + ".";
    }
    if (value instanceof StepValue.ReferenceValue) {
      StepValue.ReferenceValue referenceValue = (StepValue.ReferenceValue) value;
      return "#" + referenceValue.id();
    }
    if (value instanceof StepValue.OmittedValue) {
      return "$";
    }
    if (value instanceof StepValue.NotProvidedValue) {
      return "*";
    }
    if (value instanceof StepValue.TypedValue) {
      StepValue.TypedValue typedValue = (StepValue.TypedValue) value;
      return typedValue.typeName() + "(" + literalText(typedValue.value()) + ")";
    }
    throw new IllegalArgumentException();
  }

  <T extends StepEntity> T requireEntity(int id, Class<T> type, String message) {
    StepEntity entity = resolve(id);
    if (!type.isInstance(entity)) {
      throw new StepResolutionException(message + " but got " + entity.getClass().getSimpleName());
    }
    return type.cast(entity);
  }

  boolean isAnnotationOccurrence(StepEntity entity) {
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

  boolean isSupportedCurveReference(StepEntity entity) {
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
        || (entity instanceof StepGeometricReplica
            && "CURVE_REPLICA".equals(((StepGeometricReplica) entity).entityName()));
  }

  boolean isSupportedSurfaceReference(StepEntity entity) {
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
        || (entity instanceof StepGeometricReplica
            && "SURFACE_REPLICA".equals(((StepGeometricReplica) entity).entityName()));
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

  String deriveUnitKind(StepEntityInstance instance) {
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

  boolean matchesUnitKind(StepEntity entity, String expectedUnitKind) {
    if (entity instanceof StepNamedUnit) {
            StepNamedUnit namedUnit = (StepNamedUnit) entity;
      return expectedUnitKind.equals(namedUnit.unitKind());
    }
    if (entity instanceof StepSiUnit) {
            StepSiUnit siUnit = (StepSiUnit) entity;
      return expectedUnitKind.equals(siUnit.unitKind());
    }
    if (entity instanceof StepConversionBasedUnit) {
            StepConversionBasedUnit conversionBasedUnit = (StepConversionBasedUnit) entity;
      return expectedUnitKind.equals(conversionBasedUnit.unitKind());
    }
    if (entity instanceof StepConversionBasedUnitWithOffset) {
            StepConversionBasedUnitWithOffset conversionBasedUnitWithOffset = (StepConversionBasedUnitWithOffset) entity;
      return expectedUnitKind.equals(conversionBasedUnitWithOffset.unitKind());
    }
    if (entity instanceof StepContextDependentUnit) {
            StepContextDependentUnit contextDependentUnit = (StepContextDependentUnit) entity;
      return expectedUnitKind.equals(contextDependentUnit.unitKind());
    }
    if (entity instanceof StepDerivedUnit) {
            StepDerivedUnit derivedUnit = (StepDerivedUnit) entity;
      return expectedUnitKind.equals(derivedUnit.unitKind());
    }
    return false;
  }

  String inheritedRepresentationItemName(StepEntityInstance instance) {
    return instance.hasDefinition("REPRESENTATION_ITEM")
        ? stringValue(instance, definition(instance, "REPRESENTATION_ITEM"), 0)
        : "";
  }

  String inheritedTopologicalRepresentationItemName(StepEntityInstance instance) {
    return instance.hasDefinition("TOPOLOGICAL_REPRESENTATION_ITEM")
        ? stringValue(instance, definition(instance, "TOPOLOGICAL_REPRESENTATION_ITEM"), 0)
        : inheritedRepresentationItemName(instance);
  }

  int inheritedStyledItemTargetId(StepEntityInstance instance) {
    if (!instance.hasDefinition("STYLED_ITEM")) {
      throw new StepResolutionException("complex entity is missing STYLED_ITEM definition");
    }
    StepEntityDefinition definition = definition(instance, "STYLED_ITEM");
    requireParameterCount(instance, definition, 3);
    return referenceId(instance, definition, 2);
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
        literalList(instance, definition, 2),
        literalList(instance, definition, 3),
        resolve(referenceId(instance, definition, 4)),
        literalList(instance, definition, 5),
        literalList(instance, definition, 6));
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
