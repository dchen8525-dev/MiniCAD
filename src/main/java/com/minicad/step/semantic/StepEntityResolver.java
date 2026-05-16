package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedStepEntityException;
import com.minicad.step.model.topology.StepAdvancedFace;
import com.minicad.step.model.annotation.StepAnnotationCurveOccurrence;
import com.minicad.step.model.annotation.StepAnnotationFillArea;
import com.minicad.step.model.annotation.StepAnnotationFillAreaOccurrence;
import com.minicad.step.model.annotation.StepAnnotationPlane;
import com.minicad.step.model.annotation.StepAnnotationPlaceholderOccurrence;
import com.minicad.step.model.annotation.StepAnnotationPointOccurrence;
import com.minicad.step.model.annotation.StepAnnotationOccurrenceRelationship;
import com.minicad.step.model.annotation.StepAnnotationSubfigureOccurrence;
import com.minicad.step.model.annotation.StepAnnotationSymbol;
import com.minicad.step.model.annotation.StepAnnotationSymbolOccurrence;
import com.minicad.step.model.annotation.StepAnnotationText;
import com.minicad.step.model.annotation.StepAnnotationTextCharacter;
import com.minicad.step.model.annotation.StepAnnotationTextOccurrence;
import com.minicad.step.model.action.StepAbstractVariable;
import com.minicad.step.model.action.StepActionPropertyRepresentation;
import com.minicad.step.model.organization.StepAddress;
import com.minicad.step.model.document.StepApplicationContext;
import com.minicad.step.model.document.StepApplicationProtocolDefinition;
import com.minicad.step.model.approval.StepAppliedApprovalAssignment;
import com.minicad.step.model.approval.StepAppliedCertificationAssignment;
import com.minicad.step.model.resource.StepAppliedContractAssignment;
import com.minicad.step.model.date_time.StepAppliedDateTimeAssignment;
import com.minicad.step.model.document.StepAppliedDocumentReference;
import com.minicad.step.model.classification.StepAppliedClassificationAssignment;
import com.minicad.step.model.classification.StepAppliedExternalIdentificationAssignment;
import com.minicad.step.model.classification.StepAppliedIdentificationAssignment;
import com.minicad.step.model.organization.StepAppliedLanguageAssignment;
import com.minicad.step.model.classification.StepAppliedNameAssignment;
import com.minicad.step.model.organization.StepAppliedOrganizationAssignment;
import com.minicad.step.model.classification.StepAppliedGroupAssignment;
import com.minicad.step.model.organization.StepAppliedPersonAndOrganizationAssignment;
import com.minicad.step.model.security.StepAppliedSecurityClassificationAssignment;
import com.minicad.step.model.approval.StepApproval;
import com.minicad.step.model.approval.StepApprovalAssignment;
import com.minicad.step.model.approval.StepApprovalDateTime;
import com.minicad.step.model.approval.StepApprovalPersonOrganization;
import com.minicad.step.model.approval.StepApprovalRole;
import com.minicad.step.model.approval.StepApprovalStatus;
import com.minicad.step.model.classification.StepAttributeAssertion;
import com.minicad.step.model.geometry.StepAxis1Placement;
import com.minicad.step.model.geometry.StepAxis2Placement2D;
import com.minicad.step.model.geometry.StepAxis2Placement3D;
import com.minicad.step.model.action.StepBackChainingRuleBody;
import com.minicad.step.model.geometry.StepBoundedCurve;
import com.minicad.step.model.geometry.StepBoundedSurface;
import com.minicad.step.model.geometry.StepBezierCurve;
import com.minicad.step.model.geometry.StepBezierSurface;
import com.minicad.step.model.geometry.StepBlendedSurface;
import com.minicad.step.model.product.StepBooleanClippingResult;
import com.minicad.step.model.product.StepBooleanResult;
import com.minicad.step.model.product.StepBrepWithVoids;
import com.minicad.step.model.geometry.StepBSplineCurve;
import com.minicad.step.model.geometry.StepBSplineCurveWithKnotsAndBreakpoints;
import com.minicad.step.model.geometry.StepBSplineCurveWithKnots;
import com.minicad.step.model.geometry.StepBoxDomain;
import com.minicad.step.model.geometry.StepCartesianTransformationOperator;
import com.minicad.step.model.geometry.StepCartesianPoint;
import com.minicad.step.model.approval.StepCertification;
import com.minicad.step.model.approval.StepCertificationAssignment;
import com.minicad.step.model.approval.StepCertificationType;
import com.minicad.step.model.product.StepChainBasedGeometricItemSpecificUsage;
import com.minicad.step.model.product.StepChainBasedItemIdentifiedRepresentationUsage;
import com.minicad.step.model.base.StepCharacterizedObject;
import com.minicad.step.model.annotation.StepCharacterGlyphStyleOutline;
import com.minicad.step.model.annotation.StepCharacterGlyphStyleOutlineWithCharacteristics;
import com.minicad.step.model.annotation.StepCharacterGlyphStyleStroke;
import com.minicad.step.model.classification.StepClassificationAssignment;
import com.minicad.step.model.classification.StepClassificationRole;
import com.minicad.step.model.geometry.StepBSplineCurveWithKnots;
import com.minicad.step.model.geometry.StepBSplineSurface;
import com.minicad.step.model.geometry.StepBSplineSurfaceWithKnotsAndBreakpoints;
import com.minicad.step.model.geometry.StepBSplineSurfaceWithKnots;
import com.minicad.step.model.geometry.StepRationalBSplineCurve;
import com.minicad.step.model.geometry.StepRationalBSplineSurface;
import com.minicad.step.model.geometry.StepRectangularCompositeSurface;
import com.minicad.step.model.date_time.StepCalendarDate;
import com.minicad.step.model.geometry.StepCircle;
import com.minicad.step.model.profile.StepCentreLineArcProfileDef;
import com.minicad.step.model.profile.StepAreaProfile;
import com.minicad.step.model.profile.StepGeneralizedAreaProfile;
import com.minicad.step.model.profile.StepSweptProfileAreaOutline;
import com.minicad.step.model.profile.StepCenteredCircleProfileDef;
import com.minicad.step.model.manufacturing.StepChamfer;
import com.minicad.step.model.manufacturing.StepChamferEdge;
import com.minicad.step.model.topology.StepClosedShell;
import com.minicad.step.model.manufacturing.StepChamferDefinition;
import com.minicad.step.model.manufacturing.StepFilletDefinition;
import com.minicad.step.model.manufacturing.StepFilletEdge;
import com.minicad.step.model.manufacturing.StepFlatPattern;
import com.minicad.step.model.manufacturing.StepThread;
import com.minicad.step.model.manufacturing.StepBore;
import com.minicad.step.model.manufacturing.StepCounterboreHole;
import com.minicad.step.model.manufacturing.StepCountersinkHole;
import com.minicad.step.model.manufacturing.StepPocket;
import com.minicad.step.model.manufacturing.StepMachiningOperation;
import com.minicad.step.model.manufacturing.StepMachiningOperationSequence;
import com.minicad.step.model.manufacturing.StepRound;
import com.minicad.step.model.manufacturing.StepGroove;
import com.minicad.step.model.manufacturing.StepHole;
import com.minicad.step.model.manufacturing.StepSlot;
import com.minicad.step.model.manufacturing.StepStud;
import com.minicad.step.model.manufacturing.StepProtrusion;
import com.minicad.step.model.manufacturing.StepCutout;
import com.minicad.step.model.manufacturing.StepDepression;
import com.minicad.step.model.annotation.StepMarking;
import com.minicad.step.model.manufacturing.StepCircularPattern;
import com.minicad.step.model.manufacturing.StepLinearPattern;
import com.minicad.step.model.manufacturing.StepPattern;
import com.minicad.step.model.manufacturing.StepFeatureElementDefinition;
import com.minicad.step.model.manufacturing.StepWebs;
import com.minicad.step.model.manufacturing.StepFeaturePattern;
import com.minicad.step.model.workflow.StepCompositeDatumReference;
import com.minicad.step.model.product.StepAssemblyProcessPlan;
import com.minicad.step.model.manufacturing.StepMachiningProcessPlan;
import com.minicad.step.model.manufacturing.StepMachiningWorkPlan;
import com.minicad.step.model.tolerance.StepRectangularToleranceZone;
import com.minicad.step.model.tolerance.StepToleranceModifier;
import com.minicad.step.model.annotation.StepComposedText;
import com.minicad.step.model.product.StepComplexClippingResult;
import com.minicad.step.model.annotation.StepCompositeText;
import com.minicad.step.model.geometry.StepClothoid;
import com.minicad.step.model.annotation.StepColour;
import com.minicad.step.model.annotation.StepColourSpecification;
import com.minicad.step.model.annotation.StepColourRgb;
import com.minicad.step.model.annotation.StepColorSpecification;
import com.minicad.step.model.config_mgmt.StepConfigurationEffectivity;
import com.minicad.step.model.config_mgmt.StepChange;
import com.minicad.step.model.config_mgmt.StepStartRequest;
import com.minicad.step.model.config_mgmt.StepStartWork;
import com.minicad.step.model.config_mgmt.StepWorkItem;
import com.minicad.step.model.config_mgmt.StepConfigurationItem;
import com.minicad.step.model.config_mgmt.StepExclusionAssignment;
import com.minicad.step.model.config_mgmt.StepDateTimeEffectivity;
import com.minicad.step.model.config_mgmt.StepDateEffectivity;
import com.minicad.step.model.config_mgmt.StepLotEffectivity;
import com.minicad.step.model.config_mgmt.StepSerialNumberEffectivity;
import com.minicad.step.model.workflow.StepDatum;
import com.minicad.step.model.workflow.StepDatumFeature;
import com.minicad.step.model.workflow.StepDatumReference;
import com.minicad.step.model.workflow.StepDatumReferenceCompartment;
import com.minicad.step.model.workflow.StepDatumReferenceElement;
import com.minicad.step.model.workflow.StepDatumReferenceModifier;
import com.minicad.step.model.workflow.StepDatumReferenceModifierWithValue;
import com.minicad.step.model.tolerance.StepGeometricToleranceWithDefinedAreaUnit;
import com.minicad.step.model.tolerance.StepGeometricToleranceWithMaximumTolerance;
import com.minicad.step.model.tolerance.StepNonUniformZoneDefinition;
import com.minicad.step.model.tolerance.StepRunoutZoneDefinitionOrientation;
import com.minicad.step.model.workflow.StepDatumSystemReference;
import com.minicad.step.model.tolerance.StepTolerancePair;
import com.minicad.step.model.tolerance.StepToleranceSet;
import com.minicad.step.model.workflow.StepDatumSystem;
import com.minicad.step.model.workflow.StepDatumTarget;
import com.minicad.step.model.tolerance.StepDimensionalLocation;
import com.minicad.step.model.tolerance.StepDimensionalSize;
import com.minicad.step.model.tolerance.StepDirectedDimensionalSize;
import com.minicad.step.model.manufacturing.StepFeatureControlFrame;
import com.minicad.step.model.tolerance.StepGeometricTolerance;
import com.minicad.step.model.tolerance.StepPmiRequirement;
import com.minicad.step.model.tolerance.StepPmiGroup;
import com.minicad.step.model.tolerance.StepAngularDimensionRepresentation;
import com.minicad.step.model.tolerance.StepChainDimensionRepresentation;
import com.minicad.step.model.tolerance.StepLinearDimensionRepresentation;
import com.minicad.step.model.tolerance.StepOrdinateDimensionRepresentation;
import com.minicad.step.model.tolerance.StepShapeDimensionRepresentationWithTolerance;
import com.minicad.step.model.geometry.StepGeometricMeasurement;
import com.minicad.step.model.tolerance.StepGeometricToleranceWithDatumReference;
import com.minicad.step.model.tolerance.StepLinearToleranceZone;
import com.minicad.step.model.tolerance.StepRadialToleranceZone;
import com.minicad.step.model.tolerance.StepProjectedZoneDefinition;
import com.minicad.step.model.tolerance.StepPlusMinusToleranceWithModifiers;
import com.minicad.step.model.unit.StepDimensionalMeasurement;
import com.minicad.step.model.classification.StepLayeredItem;
import com.minicad.step.model.manufacturing.StepMaterialDesignation;
import com.minicad.step.model.resource.StepMeasureQualification;
import com.minicad.step.model.workflow.StepMeasureRepresentationItemWithUnit;
import com.minicad.step.model.tolerance.StepPlusMinusTolerance;
import com.minicad.step.model.product.StepMakeFromBuildAssembly;
import com.minicad.step.model.product.StepMakeFromFeature;
import com.minicad.step.model.product.StepMakeFromUsageOption;
import com.minicad.step.model.product.StepQuantifiedAssemblyComponentUsage;
import com.minicad.step.model.product.StepSpecifiedHigherUsageOccurrence;
import com.minicad.step.model.product.StepAlternateProductRelationship;
import com.minicad.step.model.product.StepProductDefinitionWithAssociatedDocuments;
import com.minicad.step.model.classification.StepShapeAspectShapeRepresentation;
import com.minicad.step.model.product.StepAssemblyComponentRelationship;
import com.minicad.step.model.document.StepDesignMakeFrom;
import com.minicad.step.model.product.StepDesignedPartDesignVersion;
import com.minicad.step.model.config_mgmt.StepInterpolatedConfigurationSegment;
import com.minicad.step.model.tolerance.StepRangeDimensionalSize;
import com.minicad.step.model.annotation.StepSurfaceStyleRendering;
import com.minicad.step.model.annotation.StepSurfaceStyleRenderingWithProperties;
import com.minicad.step.model.annotation.StepRenderingProperties;
import com.minicad.step.model.annotation.StepLightSource;
import com.minicad.step.model.annotation.StepLightSourceAmbient;
import com.minicad.step.model.annotation.StepLightSourceDirectional;
import com.minicad.step.model.annotation.StepLightSourcePositional;
import com.minicad.step.model.annotation.StepLightSourceSpot;
import com.minicad.step.model.annotation.StepPresentationLayerUsage;
import com.minicad.step.model.annotation.StepCameraModelD2;
import com.minicad.step.model.annotation.StepCameraModelD3;
import com.minicad.step.model.annotation.StepCameraUsage;
import com.minicad.step.model.annotation.StepCameraImage;
import com.minicad.step.model.annotation.StepPlanarBox;
import com.minicad.step.model.annotation.StepPlanarExtent;
import com.minicad.step.model.analysis.StepViewVolume;
import com.minicad.step.model.product.StepMechanicalDesignShapeRepresentation;
import com.minicad.step.model.kinematic.StepKinematicPair;
import com.minicad.step.model.kinematic.StepKinematicJoint;
import com.minicad.step.model.kinematic.StepKinematicLink;
import com.minicad.step.model.kinematic.StepKinematicStructure;
import com.minicad.step.model.kinematic.StepPrismaticPair;
import com.minicad.step.model.kinematic.StepRevolutePair;
import com.minicad.step.model.kinematic.StepCylindricalPair;
import com.minicad.step.model.kinematic.StepSphericalPair;
import com.minicad.step.model.kinematic.StepPlanarPair;
import com.minicad.step.model.kinematic.StepUniversalPair;
import com.minicad.step.model.kinematic.StepRevoluteJoint;
import com.minicad.step.model.kinematic.StepPrismaticJoint;
import com.minicad.step.model.kinematic.StepSphericalJoint;
import com.minicad.step.model.kinematic.StepCylindricalJoint;
import com.minicad.step.model.kinematic.StepPlanarJoint;
import com.minicad.step.model.kinematic.StepScrewJoint;
import com.minicad.step.model.kinematic.StepGeneralJoint;
import com.minicad.step.model.kinematic.StepDirectionSense;
import com.minicad.step.model.kinematic.StepJointValue;
import com.minicad.step.model.kinematic.StepKinematicChain;
import com.minicad.step.model.kinematic.StepKinematicModel;
import com.minicad.step.model.kinematic.StepKinematicProperty;
import com.minicad.step.model.kinematic.StepMotionConstraint;
import com.minicad.step.model.kinematic.StepScrewPair;
import com.minicad.step.model.kinematic.StepGearPair;
import com.minicad.step.model.kinematic.StepGearPairWithRange;
import com.minicad.step.model.kinematic.StepRackAndPinionPair;
import com.minicad.step.model.kinematic.StepLowOrderKinematicPairWithRange;
import com.minicad.step.model.kinematic.StepActuatedKinematicPair;
import com.minicad.step.model.kinematic.StepMechanismStateRepresentation;
import com.minicad.step.model.kinematic.StepKinematicPath;
import com.minicad.step.model.kinematic.StepKinematicLinkReference;
import com.minicad.step.model.kinematic.StepKinematicJointReference;
import com.minicad.step.model.kinematic.StepMechanismDefinition;
import com.minicad.step.model.kinematic.StepKinematicFrameBasedTransformation;
import com.minicad.step.model.organization.StepPersonAndOrganizationAddress;
import com.minicad.step.model.organization.StepOrganizationAddress;
import com.minicad.step.model.organization.StepPersonAddress;
import com.minicad.step.model.geometry.StepAngularSize;
import com.minicad.step.model.workflow.StepGeneralizedDatum;
import com.minicad.step.model.action.StepActionDirective;
import com.minicad.step.model.action.StepActionMethod;
import com.minicad.step.model.action.StepAction;
import com.minicad.step.model.action.StepActionRelationship;
import com.minicad.step.model.action.StepActionStatus;
import com.minicad.step.model.analysis.StepAnalysisInstance;
import com.minicad.step.model.analysis.StepAnalysisResult;
import com.minicad.step.model.config_mgmt.StepConfigurationInstance;
import com.minicad.step.model.product.StepModelDefinition;
import com.minicad.step.model.workflow.StepModelInstance;
import com.minicad.step.model.validation.StepSimulationDefinition;
import com.minicad.step.model.validation.StepSimulationInstance;
import com.minicad.step.model.tolerance.StepCompositeGroupTolerance;
import com.minicad.step.model.tolerance.StepDatumReferenceModifierWithSign;
import com.minicad.step.model.tolerance.StepGeometricToleranceTarget;
import com.minicad.step.model.tolerance.StepModifier;
import com.minicad.step.model.tolerance.StepQualifiedRepresentationItem;
import com.minicad.step.model.tolerance.StepRunoutZoneDefinition;
import com.minicad.step.model.tolerance.StepRunoutToleranceZone;
import com.minicad.step.model.workflow.StepShapeDimensionRepresentation;
import com.minicad.step.model.tolerance.StepToleranceValue;
import com.minicad.step.model.tolerance.StepToleranceZone;
import com.minicad.step.model.tolerance.StepToleranceZoneForm;
import com.minicad.step.model.base.StepWithDescriptiveRepresentationItem;
import com.minicad.step.model.topology.StepConnectedEdgeSet;
import com.minicad.step.model.topology.StepConnectedFaceSet;
import com.minicad.step.model.topology.StepConnectedFaceSubSet;
import com.minicad.step.model.geometry.StepCompositeCurve;
import com.minicad.step.model.geometry.StepCompositeCurveOnSurface;
import com.minicad.step.model.geometry.StepCompositeCurveOnSurface3D;
import com.minicad.step.model.geometry.StepCompositeCurveSegment;
import com.minicad.step.model.geometry.StepConicCurve;
import com.minicad.step.model.tolerance.StepCurvedToleranceZone;
import com.minicad.step.model.geometry.StepFreeFormSurface;
import com.minicad.step.model.manufacturing.StepMeasurementPoint;
import com.minicad.step.model.geometry.StepSurfaceMeasurement;
import com.minicad.step.model.manufacturing.StepSurfaceQuality;
import com.minicad.step.model.manufacturing.StepSurfaceTextureRepresentationItem;
import com.minicad.step.model.geometry.StepBSplineCurveWithKnotsAndBreakpoints;
import com.minicad.step.model.geometry.StepBSplineSurfaceWithKnotsAndBreakpoints;
import com.minicad.step.model.geometry.StepConicalSurface;
import com.minicad.step.model.geometry.StepConicalSurfaceWithEllipticalAxis;
import com.minicad.step.model.geometry.StepConicCurve;
import com.minicad.step.model.resource.StepContract;
import com.minicad.step.model.resource.StepContractAssignment;
import com.minicad.step.model.resource.StepContractType;
import com.minicad.step.model.unit.StepConversionBasedUnit;
import com.minicad.step.model.unit.StepConversionBasedUnitWithOffset;
import com.minicad.step.model.unit.StepContextDependentUnit;
import com.minicad.step.model.product.StepContactRatioRepresentation;
import com.minicad.step.model.date_time.StepCoordinatedUniversalTimeOffset;
import com.minicad.step.model.geometry.StepCylindricalSurface;
import com.minicad.step.model.geometry.StepCylindricalSurfaceWithEllipticalAxis;
import com.minicad.step.model.product.StepCsgPrimitive;
import com.minicad.step.model.product.StepCsgSolid;
import com.minicad.step.model.product.StepCsgVolume;
import com.minicad.step.model.product.StepUsageAssociation;
import com.minicad.step.model.product.StepBuyFromUsageOption;
import com.minicad.step.model.product.StepAssemblyComponentUsage;
import com.minicad.step.model.product.StepBillOfMaterials;
import com.minicad.step.model.product.StepMakeFromRelationship;
import com.minicad.step.model.product.StepAssemblyOperation;
import com.minicad.step.model.product.StepAssemblySequence;
import com.minicad.step.model.product.StepAssemblyStructure;
import com.minicad.step.model.product.StepCadModelReference;
import com.minicad.step.model.product.StepComponentDefinition;
import com.minicad.step.model.product.StepEnvironmentalImpact;
import com.minicad.step.model.product.StepModuleDefinition;
import com.minicad.step.model.product.StepPartDefinition;
import com.minicad.step.model.product.StepProductVersion;
import com.minicad.step.model.product.StepProjectInformation;
import com.minicad.step.model.product.StepStructuralFeature;
import com.minicad.step.model.product.StepHybridShapeRepresentation;
import com.minicad.step.model.product.StepDrawingRepresentation;
import com.minicad.step.model.product.StepSchematicRepresentation;
import com.minicad.step.model.product.StepSketchRepresentation;
import com.minicad.step.model.product.StepSectionRepresentation;
import com.minicad.step.model.product.StepTabulationRepresentation;
import com.minicad.step.model.product.StepZoneRepresentation;
import com.minicad.step.model.product.StepCsgPrimitive3D;
import com.minicad.step.model.product.StepCompoundRepresentationItem;
import com.minicad.step.model.product.StepContextDependentGeometricShapeRepresentation;
import com.minicad.step.model.product.StepCylinderVolume;
import com.minicad.step.model.product.StepRightCircularConeVolume;
import com.minicad.step.model.product.StepSphereVolume;
import com.minicad.step.model.product.StepTorusVolume;
import com.minicad.step.model.product.StepPrismVolume;
import com.minicad.step.model.product.StepBlockVolume;
import com.minicad.step.model.unit.StepTypedMeasureWithUnit;
import com.minicad.step.model.unit.StepLengthUnitWithUnit;
import com.minicad.step.model.unit.StepPlaneAngleUnitWithUnit;
import com.minicad.step.model.unit.StepVolumeUnitWithUnit;
import com.minicad.step.model.unit.StepAreaUnitWithUnit;
import com.minicad.step.model.unit.StepMassUnitWithUnit;
import com.minicad.step.model.unit.StepConversionBasedUnitAndUnit;
import com.minicad.step.model.geometry.StepCartesianTransformationOperator;
import com.minicad.step.model.annotation.StepCurveStyle;
import com.minicad.step.model.date_time.StepDateAssignment;
import com.minicad.step.model.geometry.StepDegeneratePcurve;
import com.minicad.step.model.geometry.StepDegenerateCurve;
import com.minicad.step.model.geometry.StepDirection;
import com.minicad.step.model.tolerance.StepDimensionCurve;
import com.minicad.step.model.unit.StepDimensionalExponents;
import com.minicad.step.model.date_time.StepDateAndTime;
import com.minicad.step.model.date_time.StepDateTimeAssignment;
import com.minicad.step.model.date_time.StepDateTimeRole;
import com.minicad.step.model.date_time.StepDateRole;
import com.minicad.step.model.date_time.StepAppliedDateAssignment;
import com.minicad.step.model.geometry.StepDegenerateToroidalSurface;
import com.minicad.step.model.topology.StepEdgeWire;
import com.minicad.step.model.unit.StepDerivedUnit;
import com.minicad.step.model.unit.StepDerivedUnitElement;
import com.minicad.step.model.base.StepDescriptiveRepresentationItem;
import com.minicad.step.model.classification.StepDescriptionAttribute;
import com.minicad.step.model.document.StepDocument;
import com.minicad.step.model.document.StepDocumentReference;
import com.minicad.step.model.document.StepDocumentRelationship;
import com.minicad.step.model.document.StepDocumentType;
import com.minicad.step.model.document.StepTextFileRepresentation;
import com.minicad.step.model.document.StepDocumentUsageConstraint;
import com.minicad.step.model.annotation.StepDraughtingAnnotationOccurrence;
import com.minicad.step.model.annotation.StepDraughtingModelItemAssociation;
import com.minicad.step.model.annotation.StepDraughtingModelItemAssociationWithPlaceholder;
import com.minicad.step.model.annotation.StepDraughtingPreDefinedColour;
import com.minicad.step.model.annotation.StepDraughtingPreDefinedCurveFont;
import com.minicad.step.model.annotation.StepDraughtingPreDefinedTextFont;
import com.minicad.step.model.annotation.StepDraughtingCallout;
import com.minicad.step.model.annotation.StepDraughtingCalloutRelationship;
import com.minicad.step.model.topology.StepEdgeCurve;
import com.minicad.step.model.product.StepEdgeBasedWireframeModel;
import com.minicad.step.model.topology.StepEdgeLoop;
import com.minicad.step.model.product.StepEffectivity;
import com.minicad.step.model.product.StepEffectivityRelationship;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.classification.StepExternalSource;
import com.minicad.step.model.classification.StepExternalIdentificationAssignment;
import com.minicad.step.model.classification.StepExternalSourceRelationship;
import com.minicad.step.model.annotation.StepExternallyDefinedItem;
import com.minicad.step.model.base.StepFaceEntity;
import com.minicad.step.model.product.StepFacettedBrep;
import com.minicad.step.model.topology.StepFaceBound;
import com.minicad.step.model.product.StepFaceBasedSurfaceModel;
import com.minicad.step.model.topology.StepFaceSurface;
import com.minicad.step.model.annotation.StepFillAreaStyle;
import com.minicad.step.model.annotation.StepFillAreaStyleColour;
import com.minicad.step.model.action.StepForwardChainingRulePremise;
import com.minicad.step.model.product.StepGeometricCurveSet;
import com.minicad.step.model.product.StepGeometricSet;
import com.minicad.step.model.tolerance.StepGeometricTolerance;
import com.minicad.step.model.tolerance.StepToleranceZoneForm;
import com.minicad.step.model.tolerance.StepToleranceZone;
import com.minicad.step.model.config_mgmt.StepConfigurationItem;
import com.minicad.step.model.tolerance.StepDirectedDimensionalSize;
import com.minicad.step.model.product.StepGeometricItemSpecificUsage;
import com.minicad.step.model.workflow.StepGeometricRepresentationContext;
import com.minicad.step.model.resource.StepGeneralProperty;
import com.minicad.step.model.resource.StepGeneralPropertyRelationship;
import com.minicad.step.model.product.StepGeometricReplica;
import com.minicad.step.model.product.StepGeometricSurfaceSet;
import com.minicad.step.model.workflow.StepGlobalUncertaintyAssignedContext;
import com.minicad.step.model.unit.StepGlobalUnitAssignedContext;
import com.minicad.step.model.classification.StepGroup;
import com.minicad.step.model.classification.StepGroupAssignment;
import com.minicad.step.model.classification.StepGroupRelationship;
import com.minicad.step.model.product.StepHalfSpaceSolid;
import com.minicad.step.model.classification.StepIdAttribute;
import com.minicad.step.model.classification.StepIdentificationAssignment;
import com.minicad.step.model.classification.StepIdentificationRole;
import com.minicad.step.model.product.StepItemIdentifiedRepresentationUsage;
import com.minicad.step.model.geometry.StepIndexedPolyCurve;
import com.minicad.step.model.geometry.StepPolyline3D;
import com.minicad.step.model.product.StepItemDefinedTransformation;
import com.minicad.step.model.kinematic.StepKinematicPropertyDefinitionRepresentation;
import com.minicad.step.model.kinematic.StepKinematicPropertyMechanismRepresentation;
import com.minicad.step.model.kinematic.StepKinematicPropertyRepresentationRelation;
import com.minicad.step.model.kinematic.StepKinematicPropertyTopologyRepresentation;
import com.minicad.step.model.organization.StepLanguage;
import com.minicad.step.model.organization.StepLanguageAssignment;
import com.minicad.step.model.annotation.StepLeaderCurve;
import com.minicad.step.model.geometry.StepLine;
import com.minicad.step.model.geometry.StepLineSegment;
import com.minicad.step.model.date_time.StepLocalTime;
import com.minicad.step.model.product.StepManifoldSolidBrep;
import com.minicad.step.model.geometry.StepManifoldSurfaceModel;
import com.minicad.step.model.geometry.StepMotionPath;
import com.minicad.step.model.geometry.StepAngularLocation;
import com.minicad.step.model.product.StepMappedItem;
import com.minicad.step.model.unit.StepMeasureWithUnit;
import com.minicad.step.model.base.StepMeasureRepresentationItem;
import com.minicad.step.model.product.StepMechanicalDesignRequirementItemAssociation;
import com.minicad.step.model.unit.StepNamedUnit;
import com.minicad.step.model.classification.StepNameAssignment;
import com.minicad.step.model.classification.StepNameAttribute;
import com.minicad.step.model.topology.StepOpenShell;
import com.minicad.step.model.organization.StepOrganization;
import com.minicad.step.model.organization.StepOrganizationAssignment;
import com.minicad.step.model.organization.StepOrganizationRelationship;
import com.minicad.step.model.organization.StepOrganizationRole;
import com.minicad.step.model.geometry.StepOpenPath;
import com.minicad.step.model.annotation.StepOverRidingStyledItem;
import com.minicad.step.model.topology.StepOrientedEdge;
import com.minicad.step.model.topology.StepOrientedFace;
import com.minicad.step.model.topology.StepOrientedClosedShell;
import com.minicad.step.model.topology.StepOrientedOpenShell;
import com.minicad.step.model.geometry.StepOrientedPath;
import com.minicad.step.model.geometry.StepOrientedCurve;
import com.minicad.step.model.geometry.StepOrientedSurface;
import com.minicad.step.model.geometry.StepPath;
import com.minicad.step.model.organization.StepPerson;
import com.minicad.step.model.organization.StepPersonAndOrganization;
import com.minicad.step.model.organization.StepPersonAndOrganizationAssignment;
import com.minicad.step.model.organization.StepPersonAndOrganizationRole;
import com.minicad.step.model.geometry.StepPlane;
import com.minicad.step.model.workflow.StepPlacedDatumTargetFeature;
import com.minicad.step.model.workflow.StepPlacedTarget;
import com.minicad.step.model.topology.StepPolyLoop;
import com.minicad.step.model.geometry.StepPolyline;
import com.minicad.step.model.geometry.StepPcurve;
import com.minicad.step.model.geometry.StepEllipse;
import com.minicad.step.model.product.StepProduct;
import com.minicad.step.model.product.StepProductCategory;
import com.minicad.step.model.product.StepProductCategoryRelationship;
import com.minicad.step.model.geometry.StepProjectionCurve;
import com.minicad.step.model.product.StepProductContext;
import com.minicad.step.model.product.StepProductDefinition;
import com.minicad.step.model.product.StepProductDefinitionContext;
import com.minicad.step.model.product.StepProductDefinitionEffectivity;
import com.minicad.step.model.product.StepProductDefinitionFormation;
import com.minicad.step.model.product.StepProductDefinitionFormationRelationship;
import com.minicad.step.model.product.StepProductDefinitionRelationship;
import com.minicad.step.model.product.StepProductDefinitionRelationshipRelationship;
import com.minicad.step.model.product.StepProductDefinitionShape;
import com.minicad.step.model.product.StepProductRelatedProductCategory;
import com.minicad.step.model.product.StepProductRelationship;
import com.minicad.step.model.profile.StepProfileDef;
import com.minicad.step.model.workflow.StepPropertyDefinition;
import com.minicad.step.model.workflow.StepPropertyDefinitionRelationship;
import com.minicad.step.model.workflow.StepPropertyDefinitionRepresentation;
import com.minicad.step.model.annotation.StepPresentationLayerAssignment;
import com.minicad.step.model.annotation.StepPresentationStyleAssignment;
import com.minicad.step.model.geometry.StepPoint;
import com.minicad.step.model.geometry.StepPointSet;
import com.minicad.step.model.geometry.StepPiecewiseBezierCurve;
import com.minicad.step.model.geometry.StepPiecewiseBezierSurface;
import com.minicad.step.model.annotation.StepPointStyle;
import com.minicad.step.model.annotation.StepPreDefinedColour;
import com.minicad.step.model.annotation.StepPreDefinedCurveFont;
import com.minicad.step.model.annotation.StepPreDefinedDimensionSymbol;
import com.minicad.step.model.annotation.StepPreDefinedGeometricalToleranceSymbol;
import com.minicad.step.model.base.StepPreDefinedItem;
import com.minicad.step.model.annotation.StepPreDefinedMarker;
import com.minicad.step.model.annotation.StepPreDefinedPointMarkerSymbol;
import com.minicad.step.model.annotation.StepPreDefinedSurfaceSideStyle;
import com.minicad.step.model.annotation.StepPreDefinedSymbol;
import com.minicad.step.model.annotation.StepPreDefinedTerminatorSymbol;
import com.minicad.step.model.annotation.StepPreDefinedTextFont;
import com.minicad.step.model.annotation.StepPmiRequirementItemAssociation;
import com.minicad.step.model.workflow.StepRepresentation;
import com.minicad.step.model.topology.StepAdvancedBrep;
import com.minicad.step.model.geometry.StepRuledSurface;
import com.minicad.step.model.product.StepRevolvedAreaSolidTapered;
import com.minicad.step.model.product.StepExtrudedAreaSolidTapered;
import com.minicad.step.model.product.StepSurfaceCurveSweptAreaSolid;
import com.minicad.step.model.product.StepTessellatedFace;
import com.minicad.step.model.product.StepTessellatedFaceSet;
import com.minicad.step.model.topology.StepTriangulatedFace;
import com.minicad.step.model.topology.StepComplexTriangulatedFace;
import com.minicad.step.model.topology.StepCubicBezierTriangulatedFace;
import com.minicad.step.model.product.StepTessellatedTriangle;
import com.minicad.step.model.fea.StepFiniteElementMesh;
import com.minicad.step.model.fea.StepVolume3dElementRepresentation;
import com.minicad.step.model.fea.StepFeaNode;
import com.minicad.step.model.fea.StepFeaElement;
import com.minicad.step.model.fea.StepFeaLoad;
import com.minicad.step.model.fea.StepFeaModel;
import com.minicad.step.model.fea.StepMaterial;
import com.minicad.step.model.fea.StepFeaLinearMaterial;
import com.minicad.step.model.fea.StepFeaNonLinearMaterial;
import com.minicad.step.model.fea.StepFeaMassDensity;
import com.minicad.step.model.fea.StepFeaYieldStress;
import com.minicad.step.model.fea.StepFeaUltimateStress;
import com.minicad.step.model.fea.StepDisplacementBoundaryCondition;
import com.minicad.step.model.fea.StepVelocityBoundaryCondition;
import com.minicad.step.model.fea.StepAccelerationBoundaryCondition;
import com.minicad.step.model.fea.StepForceBoundaryCondition;
import com.minicad.step.model.fea.StepPressureBoundaryCondition;
import com.minicad.step.model.fea.StepThermalBoundaryCondition;
import com.minicad.step.model.fea.StepStressAnalysis;
import com.minicad.step.model.fea.StepBucklingAnalysis;
import com.minicad.step.model.fea.StepModalAnalysis;
import com.minicad.step.model.fea.StepThermalAnalysis;
import com.minicad.step.model.fea.StepStructuralAnalysisModel;
import com.minicad.step.model.element.StepElementVolume;
import com.minicad.step.model.element.StepVolumeElement;
import com.minicad.step.model.element.StepSurfaceElement;
import com.minicad.step.model.element.StepLineElement;
import com.minicad.step.model.element.StepMassElement;
import com.minicad.step.model.element.StepConnectivityElement;
import com.minicad.step.model.element.StepElementGeometricDescription;
import com.minicad.step.model.element.StepUniformSurfaceElement;
import com.minicad.step.model.element.StepUniformVolumeElement;
import com.minicad.step.model.element.StepNodeRepresentation;
import com.minicad.step.model.product.StepSpecificHigherUsageOccurrence;
import com.minicad.step.model.product.StepUsageOccurrence;
import com.minicad.step.model.product.StepShapeRepresentationTransformation;
import com.minicad.step.model.workflow.StepRepresentationContext3d;
import com.minicad.step.model.classification.StepAppliedAttributeClassification;
import com.minicad.step.model.classification.StepAttributeClassification;
import com.minicad.step.model.fea.StepStructuralAnalysisRepresentation;
import com.minicad.step.model.fea.StepStructuralAnalysisRepresentationParameters;
import com.minicad.step.model.workflow.StepValueReasonPair;
import com.minicad.step.model.geometry.StepBoundingBox;
import com.minicad.step.model.annotation.StepFillAreaShapeUse;
import com.minicad.step.model.topology.StepPointOnFace;
import com.minicad.step.model.product.StepTessellatedCoordinateSet;
import com.minicad.step.model.validation.StepUncertaintyMeasure;
import com.minicad.step.model.fea.StepStructAnalysisModel;
import com.minicad.step.model.fea.StepVolume3dElementProperty;
import com.minicad.step.model.fea.StepCurve3dElementProperty;
import com.minicad.step.model.fea.StepSurface3dElementProperty;
import com.minicad.step.model.fea.StepFeaMaterialPropertyRepresentation;
import com.minicad.step.model.fea.StepFeaShellElementProperty;
import com.minicad.step.model.fea.StepFeaBeamElementProperty;
import com.minicad.step.model.fea.StepFea2DElementProperty;
import com.minicad.step.model.fea.StepFea3DElementProperty;
import com.minicad.step.model.fea.StepFeaTrussElementProperty;
import com.minicad.step.model.fea.StepFeaSpringElementProperty;
import com.minicad.step.model.fea.StepFeaVolumeElementProperty;
import com.minicad.step.model.fea.StepBoundaryCondition;
import com.minicad.step.model.fea.StepLoadCase;
import com.minicad.step.model.fea.StepElementVolume2d;
import com.minicad.step.model.fea.StepElementVolume3d;
import com.minicad.step.model.fea.StepNodeSet;
import com.minicad.step.model.fea.StepElementSet;
import com.minicad.step.model.fea.StepFeaSecuredVariable;
import com.minicad.step.model.fea.StepFeaConstantFunction3d;
import com.minicad.step.model.fea.StepFeaLinearAlgebraicMatrix;
import com.minicad.step.model.fea.StepFeaLinearAlgebraicVector;
import com.minicad.step.model.geometry.StepFeaAxis2Placement3d;
import com.minicad.step.model.fea.StepFeaGroupRepresentation;
import com.minicad.step.model.annotation.StepTextLiteral;
import com.minicad.step.model.annotation.StepTextLiteralWithDraughtingCallout;
import com.minicad.step.model.annotation.StepComposedTextLiteral;
import com.minicad.step.model.annotation.StepTextFont;
import com.minicad.step.model.annotation.StepCharacterGlyph;
import com.minicad.step.model.annotation.StepCharacterGlyphOutline;
import com.minicad.step.model.annotation.StepCharacterGlyphOutlineWithCharacteristics;
import com.minicad.step.model.annotation.StepCharacterGlyphStroke;
import com.minicad.step.model.annotation.StepPreDefinedSurfaceStyle;
import com.minicad.step.model.annotation.StepSurfaceStyleParameterLines;
import com.minicad.step.model.annotation.StepFillAreaStyleOutline;
import com.minicad.step.model.annotation.StepFillAreaStyleTransparent;
import com.minicad.step.model.annotation.StepFillAreaStyleHatching;
import com.minicad.step.model.annotation.StepFillAreaStyleTiling;
import com.minicad.step.model.annotation.StepAnnotationFillAreaRegion;
import com.minicad.step.model.annotation.StepFillAreaWithOutline;
import com.minicad.step.model.annotation.StepAnnotationRecord;
import com.minicad.step.model.annotation.StepDrawingReference;
import com.minicad.step.model.annotation.StepExternallyDefinedHatchStyle;
import com.minicad.step.model.annotation.StepExternallyDefinedTileStyle;
import com.minicad.step.model.annotation.StepMarkingFeature;
import com.minicad.step.model.annotation.StepTechnicalNote;
import com.minicad.step.model.annotation.StepCurveStyleFont;
import com.minicad.step.model.annotation.StepCurveStyleRendering;
import com.minicad.step.model.annotation.StepCurveStyleWithFont;
import com.minicad.step.model.annotation.StepDraughtingPreDefinedTerminatorSymbol;
import com.minicad.step.model.product.StepRepresentationMap;
import com.minicad.step.model.base.StepRepresentationItem;
import com.minicad.step.model.product.StepRepresentationRelationship;
import com.minicad.step.model.product.StepRepresentationContext;
import com.minicad.step.model.product.StepRepresentationRelationshipWithTransformation;
import com.minicad.step.model.geometry.StepRectangularTrimmedSurface;
import com.minicad.step.model.resource.StepResourcePropertyRepresentation;
import com.minicad.step.model.action.StepRowVariable;
import com.minicad.step.model.action.StepScalarVariable;
import com.minicad.step.model.geometry.StepCurve;
import com.minicad.step.model.geometry.StepCurveBoundedSurface;
import com.minicad.step.model.base.StepGeometricRepresentationItem;
import com.minicad.step.model.security.StepSecurityClassification;
import com.minicad.step.model.security.StepSecurityClassificationAssignment;
import com.minicad.step.model.security.StepSecurityClassificationLevel;
import com.minicad.step.model.classification.StepShapeAspect;
import com.minicad.step.model.classification.StepShapeAspectOccurrence;
import com.minicad.step.model.classification.StepShapeAspectRelationship;
import com.minicad.step.model.classification.StepAttributeDefinition;
import com.minicad.step.model.classification.StepAttributeInstance;
import com.minicad.step.model.classification.StepCompositeShapeAspect;
import com.minicad.step.model.workflow.StepShapeRepresentationRelationship;
import com.minicad.step.model.product.StepShapeDefinitionRepresentation;
import com.minicad.step.model.product.StepShellBasedSurfaceModel;
import com.minicad.step.model.geometry.StepSeamCurve;
import com.minicad.step.model.geometry.StepSeamEdge;
import com.minicad.step.model.unit.StepSiUnit;
import com.minicad.step.model.product.StepSolidModel;
import com.minicad.step.model.product.StepSolidReplica;
import com.minicad.step.model.geometry.StepSurface;
import com.minicad.step.model.geometry.StepSurfaceCurve;
import com.minicad.step.model.geometry.StepSurfacedEdgeCurve;
import com.minicad.step.model.geometry.StepSurfacedOpenShell;
import com.minicad.step.model.geometry.StepSurfaceModel;
import com.minicad.step.model.geometry.StepSurfaceOfLinearExtrusion;
import com.minicad.step.model.geometry.StepSurfaceOfTranslation;
import com.minicad.step.model.geometry.StepSurfaceOfProjection;
import com.minicad.step.model.geometry.StepParaboloidSurface;
import com.minicad.step.model.geometry.StepHyperboloidSurface;
import com.minicad.step.model.geometry.StepSurfaceOfRevolution;
import com.minicad.step.model.geometry.StepSurfaceOfConstantRadius;
import com.minicad.step.model.geometry.StepSphericalSurface;
import com.minicad.step.model.geometry.StepSphericalSurfaceWithEllipticalAxis;
import com.minicad.step.model.product.StepPolygonalBoundedHalfSpace;
import com.minicad.step.model.geometry.StepSurfacePatch;
import com.minicad.step.model.annotation.StepSurfaceSideStyle;
import com.minicad.step.model.annotation.StepSurfaceStyleFillArea;
import com.minicad.step.model.annotation.StepSurfaceStyleBoundary;
import com.minicad.step.model.annotation.StepSurfaceStyleControlGrid;
import com.minicad.step.model.annotation.StepSurfaceStyleParameterLine;
import com.minicad.step.model.annotation.StepSurfaceStyleReflectanceAmbient;
import com.minicad.step.model.annotation.StepSurfaceStyleReflectanceAmbientDiffuse;
import com.minicad.step.model.annotation.StepSurfaceStyleReflectanceAmbientDiffuseSpecular;
import com.minicad.step.model.annotation.StepSurfaceStyleSegmentationCurve;
import com.minicad.step.model.annotation.StepSurfaceStyleSilhouette;
import com.minicad.step.model.annotation.StepSurfaceStyleTransparent;
import com.minicad.step.model.annotation.StepSurfaceStyleUsage;
import com.minicad.step.model.product.StepSweptAreaSolid;
import com.minicad.step.model.product.StepExtrudedFaceSolid;
import com.minicad.step.model.product.StepRevolvedFaceSolid;
import com.minicad.step.model.product.StepSweptFaceSolid;
import com.minicad.step.model.product.StepSweptDiskSolid;
import com.minicad.step.model.annotation.StepSymbolColour;
import com.minicad.step.model.workflow.StepSymbolRepresentationMap;
import com.minicad.step.model.annotation.StepSymbolStyle;
import com.minicad.step.model.annotation.StepStyledItem;
import com.minicad.step.model.topology.StepSubedge;
import com.minicad.step.model.geometry.StepSubpath;
import com.minicad.step.model.topology.StepSubface;
import com.minicad.step.model.topology.StepOrientedSubface;
import com.minicad.step.model.profile.StepRectangleHollowProfileDef;
import com.minicad.step.model.annotation.StepTextStyle;
import com.minicad.step.model.annotation.StepTextStyleWithBoxCharacteristics;
import com.minicad.step.model.annotation.StepTextStyleForDefinedFont;
import com.minicad.step.model.annotation.StepTextStyleWithJustification;
import com.minicad.step.model.annotation.StepTextStyleWithMirror;
import com.minicad.step.model.annotation.StepTextStyleWithSpacing;
import com.minicad.step.model.annotation.StepTerminatorSymbol;
import com.minicad.step.model.unit.StepTypedMeasureWithUnit;
import com.minicad.step.model.base.StepTopologicalRepresentationItem;
import com.minicad.step.model.geometry.StepTrimmedCurve;
import com.minicad.step.model.geometry.StepToroidalSurface;
import com.minicad.step.model.geometry.StepToroidalSurfaceWithCylindricalAxis;
import com.minicad.step.model.geometry.StepToroidalSurfaceWithSpecifiedBends;
import com.minicad.step.model.geometry.StepToroidalSurfaceWithEllipticalAxis;
import com.minicad.step.model.geometry.StepUniformCurve;
import com.minicad.step.model.geometry.StepQuasiUniformCurve;
import com.minicad.step.model.geometry.StepUniformSurface;
import com.minicad.step.model.geometry.StepQuasiUniformSurface;
import com.minicad.step.model.unit.StepUncertaintyMeasureWithUnit;
import com.minicad.step.model.product.StepNonManifoldSolidBrep;
import com.minicad.step.model.product.StepNextAssemblyUsageOccurrence;
import com.minicad.step.model.geometry.StepOffsetCurve2D;
import com.minicad.step.model.geometry.StepOffsetCurve3D;
import com.minicad.step.model.geometry.StepOffsetSurface;
import com.minicad.step.model.geometry.StepOffsetSurface2;
import com.minicad.step.model.product.StepContextDependentShapeRepresentation;
import com.minicad.step.model.topology.StepFace;
import com.minicad.step.model.topology.StepEdge;
import com.minicad.step.model.topology.StepLoop;
import com.minicad.step.model.manufacturing.StepMachinedSurface;
import com.minicad.step.model.geometry.StepVector;
import com.minicad.step.model.topology.StepVertex;
import com.minicad.step.model.topology.StepVertexLoop;
import com.minicad.step.model.topology.StepVertexPoint;
import com.minicad.step.model.topology.StepVertexShell;
import com.minicad.step.model.base.StepValueRepresentationItem;
import com.minicad.step.model.annotation.StepUserDefinedCurveFont;
import com.minicad.step.model.annotation.StepUserDefinedMarker;
import com.minicad.step.model.annotation.StepUserDefinedTerminatorSymbol;
import com.minicad.step.model.topology.StepWireShell;
import com.minicad.step.model.product.StepShellBasedWireframeModel;
import com.minicad.step.model.geometry.StepCircle2D;
import com.minicad.step.model.geometry.StepEllipse2D;
import com.minicad.step.model.geometry.StepHyperbola2D;
import com.minicad.step.model.geometry.StepParabola2D;
import com.minicad.step.model.geometry.StepLine2D;
import com.minicad.step.model.geometry.StepPolyline2D;
import com.minicad.step.model.geometry.StepTrimmedCurve2D;
import com.minicad.step.model.geometry.StepBoundedCurve2D;
import com.minicad.step.model.geometry.StepCompositeCurve2D;
import com.minicad.step.model.geometry.StepCurve2D;
import com.minicad.step.model.geometry.StepBSplineCurve2D;
import com.minicad.step.model.geometry.StepRationalBSplineCurve2D;
import com.minicad.step.model.geometry.StepBezierCurve2D;
import com.minicad.step.model.geometry.StepQuasiUniformCurve2D;
import com.minicad.step.model.geometry.StepUniformCurve2D;
import com.minicad.step.model.geometry.StepPiecewiseBezierCurve2D;
import com.minicad.step.model.geometry.StepIndexedPolyCurve2D;
import com.minicad.step.model.geometry.StepDegenerateCurve2D;
import com.minicad.step.model.validation.StepValidationPropertyRepresentation;
import com.minicad.step.model.analysis.StepCalculatedGeometricRepresentationItem;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;
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
      throw new StepResolutionException("missing referenced entity #" + id);
    }

    EntityFactory factory = resolveFactory(instance);
    if (factory != null) {
      StepEntity entity = factory.create(this, instance);
      resolved.put(id, entity);
      return entity;
    }

    throw new UnsupportedStepEntityException("unsupported STEP entity " + instance.name());
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
    Map<String, Integer> order = new HashMap<>();
    int index = 0;
    for (String entityName : registry.keySet()) {
      order.put(entityName, index++);
    }
    return Map.copyOf(order);
  }

  StepCartesianPoint resolveCartesianPoint(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CARTESIAN_POINT");
    requireParameterCount(instance, definition, 2);
    return new StepCartesianPoint(
        instance.id(),
        stringValue(instance, definition, 0),
        coordinateList(instance, definition, 1, 2, 3));
  }

  StepDirection resolveDirection(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DIRECTION");
    requireParameterCount(instance, definition, 2);
    return new StepDirection(
        instance.id(),
        stringValue(instance, definition, 0),
        coordinateList(instance, definition, 1, 2, 3));
  }

  StepVector resolveVector(StepEntityInstance instance) {
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

  StepAxis2Placement3D resolveAxis2Placement3D(StepEntityInstance instance) {
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

  StepAxis1Placement resolveAxis1Placement(StepEntityInstance instance) {
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

  StepAxis2Placement2D resolveAxis2Placement2D(StepEntityInstance instance) {
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

  StepLine resolveLine(StepEntityInstance instance) {
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

  StepPolyline resolvePolyline(StepEntityInstance instance) {
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

  StepPlane resolvePlane(StepEntityInstance instance) {
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

  StepCircle resolveCircle(StepEntityInstance instance) {
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

  StepEllipse resolveEllipse(StepEntityInstance instance) {
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

  StepConicCurve resolveConicCurve(
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

  StepCylindricalSurface resolveCylindricalSurface(StepEntityInstance instance) {
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

  StepConicalSurface resolveConicalSurface(StepEntityInstance instance) {
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

  StepToroidalSurface resolveToroidalSurface(StepEntityInstance instance) {
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

  StepDegenerateToroidalSurface resolveDegenerateToroidalSurface(
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

  StepSphericalSurface resolveSphericalSurface(StepEntityInstance instance) {
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

  StepCylindricalSurfaceWithEllipticalAxis resolveCylindricalSurfaceWithEllipticalAxis(
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

  StepConicalSurfaceWithEllipticalAxis resolveConicalSurfaceWithEllipticalAxis(
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

  StepSphericalSurfaceWithEllipticalAxis resolveSphericalSurfaceWithEllipticalAxis(
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

  StepToroidalSurfaceWithEllipticalAxis resolveToroidalSurfaceWithEllipticalAxis(
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

  StepToroidalSurfaceWithCylindricalAxis resolveToroidalSurfaceWithCylindricalAxis(
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

  StepToroidalSurfaceWithSpecifiedBends resolveToroidalSurfaceWithSpecifiedBends(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TOROIDAL_SURFACE_WITH_SPECIFIED_BENDS");
    requireParameterCount(instance, definition, 6);
    return new StepToroidalSurfaceWithSpecifiedBends(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "TOROIDAL_SURFACE_WITH_SPECIFIED_BENDS position must reference AXIS2_PLACEMENT_3D"),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3),
        resolve(referenceId(instance, definition, 4)),
        resolve(referenceId(instance, definition, 5)));
  }

  StepBlendedSurface resolveBlendedSurface(StepEntityInstance instance) {
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

  StepChamferEdge resolveChamferEdge(StepEntityInstance instance) {
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

  StepFilletEdge resolveFilletEdge(StepEntityInstance instance) {
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

  StepFreeFormSurface resolveFreeFormSurface(StepEntityInstance instance) {
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

  List<List<StepEntity>> resolveFreeFormControlPoints(
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

  StepCurvedToleranceZone resolveCurvedToleranceZone(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CURVED_TOLERANCE_ZONE");
    requireParameterCount(instance, definition, 5);
    return new StepCurvedToleranceZone(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  StepSurfaceQuality resolveSurfaceQuality(StepEntityInstance instance) {
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

  StepMeasurementPoint resolveMeasurementPoint(StepEntityInstance instance) {
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

  StepSurfaceMeasurement resolveSurfaceMeasurement(StepEntityInstance instance) {
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

  StepSurfaceTextureRepresentationItem resolveSurfaceTextureRepresentationItem(
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

  StepGeometricReplica resolveGeometricReplica(
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

  StepRectangularTrimmedSurface resolveRectangularTrimmedSurface(
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

  StepCurveBoundedSurface resolveCurveBoundedSurface(StepEntityInstance instance) {
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

  StepAnalysisResult resolveAnalysisResult(StepEntityInstance instance) {
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

  StepAnalysisInstance resolveAnalysisInstance(StepEntityInstance instance) {
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

  StepConfigurationInstance resolveConfigurationInstance(StepEntityInstance instance) {
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

  StepModelDefinition resolveModelDefinition(StepEntityInstance instance) {
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

  StepModelInstance resolveModelInstance(StepEntityInstance instance) {
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

  StepSimulationDefinition resolveSimulationDefinition(StepEntityInstance instance) {
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

  StepSimulationInstance resolveSimulationInstance(StepEntityInstance instance) {
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

  StepOrientedSurface resolveOrientedSurface(StepEntityInstance instance) {
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

  StepSurfaceOfLinearExtrusion resolveSurfaceOfLinearExtrusion(
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

  StepSurfaceOfRevolution resolveSurfaceOfRevolution(StepEntityInstance instance) {
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

  StepSurfaceOfTranslation resolveSurfaceOfTranslation(StepEntityInstance instance) {
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

  StepSurfaceOfProjection resolveSurfaceOfProjection(StepEntityInstance instance) {
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

  StepParaboloidSurface resolveParaboloidSurface(StepEntityInstance instance) {
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

  StepHyperboloidSurface resolveHyperboloidSurface(StepEntityInstance instance) {
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

  StepOffsetCurve3D resolveOffsetCurve3D(StepEntityInstance instance) {
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

  StepOffsetCurve2D resolveOffsetCurve2D(StepEntityInstance instance) {
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

  StepOrientedCurve resolveOrientedCurve(StepEntityInstance instance) {
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

  StepOffsetSurface resolveOffsetSurface(StepEntityInstance instance) {
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

  StepCompositeCurveSegment resolveCompositeCurveSegment(StepEntityInstance instance) {
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

  StepCompositeCurve resolveCompositeCurve(StepEntityInstance instance) {
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

  StepCompositeCurveOnSurface resolveCompositeCurveOnSurface(
      StepEntityInstance instance) {
    return resolveCompositeCurveOnSurface(instance, "COMPOSITE_CURVE_ON_SURFACE");
  }

  StepCompositeCurveOnSurface resolveCompositeCurveOnSurface(
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

  StepTrimmedCurve resolveTrimmedCurve(StepEntityInstance instance) {
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

  StepSurfaceCurve resolveSurfaceCurve(StepEntityInstance instance) {
    return resolveSurfaceCurve(instance, "SURFACE_CURVE");
  }

  StepSurfaceCurve resolveSurfaceCurve(StepEntityInstance instance, String entityName) {
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

  StepSeamCurve resolveSeamCurve(StepEntityInstance instance) {
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

  StepPcurve resolvePcurve(StepEntityInstance instance) {
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

  StepDegeneratePcurve resolveDegeneratePcurve(StepEntityInstance instance) {
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

  StepBSplineCurveWithKnots resolveBSplineCurveWithKnots(StepEntityInstance instance) {
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

  StepBSplineCurve resolveBSplineCurve(StepEntityInstance instance) {
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

  StepRationalBSplineCurve resolveRationalBSplineCurve(StepEntityInstance instance) {
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

  StepBSplineSurfaceWithKnots resolveBSplineSurfaceWithKnots(StepEntityInstance instance) {
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

  StepBSplineSurface resolveBSplineSurface(StepEntityInstance instance) {
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

  StepBSplineCurveWithKnotsAndBreakpoints resolveBSplineCurveWithKnotsAndBreakpoints(
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

  StepBSplineSurfaceWithKnotsAndBreakpoints resolveBSplineSurfaceWithKnotsAndBreakpoints(
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

  StepRationalBSplineSurface resolveRationalBSplineSurface(StepEntityInstance instance) {
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

  StepVertexPoint resolveVertexPoint(StepEntityInstance instance) {
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

  StepEdgeCurve resolveEdgeCurve(StepEntityInstance instance) {
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

  StepOrientedEdge resolveOrientedEdge(StepEntityInstance instance) {
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

  StepSubedge resolveSubedge(StepEntityInstance instance) {
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

  StepConnectedEdgeSet resolveConnectedEdgeSet(StepEntityInstance instance) {
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

  StepEdgeBasedWireframeModel resolveEdgeBasedWireframeModel(
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

  StepEdgeLoop resolveEdgeLoop(StepEntityInstance instance) {
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

  StepPath resolvePath(StepEntityInstance instance) {
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

  StepOpenPath resolveOpenPath(StepEntityInstance instance) {
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

  StepSubpath resolveSubpath(StepEntityInstance instance) {
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

  StepOrientedPath resolveOrientedPath(StepEntityInstance instance) {
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

  StepVertexLoop resolveVertexLoop(StepEntityInstance instance) {
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

  StepPolyLoop resolvePolyLoop(StepEntityInstance instance) {
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

  StepFaceBound resolveFaceBound(StepEntityInstance instance, boolean outer) {
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

  StepAdvancedFace resolveAdvancedFace(StepEntityInstance instance) {
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

  StepFaceSurface resolveFaceSurface(StepEntityInstance instance) {
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

  StepOrientedFace resolveOrientedFace(StepEntityInstance instance) {
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

  StepOpenShell resolveOpenShell(StepEntityInstance instance) {
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

  StepClosedShell resolveClosedShell(StepEntityInstance instance) {
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

  StepSurfacedOpenShell resolveSurfacedOpenShell(StepEntityInstance instance) {
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

  StepOrientedOpenShell resolveOrientedOpenShell(StepEntityInstance instance) {
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

  StepOrientedClosedShell resolveOrientedClosedShell(StepEntityInstance instance) {
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

  StepConnectedFaceSet resolveConnectedFaceSet(StepEntityInstance instance) {
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

  StepConnectedFaceSubSet resolveConnectedFaceSubSet(StepEntityInstance instance) {
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

  StepVertexShell resolveVertexShell(StepEntityInstance instance) {
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

  StepWireShell resolveWireShell(StepEntityInstance instance) {
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

  StepManifoldSolidBrep resolveManifoldSolidBrep(StepEntityInstance instance) {
    return resolveManifoldSolidBrep(instance, "MANIFOLD_SOLID_BREP");
  }

  StepManifoldSolidBrep resolveManifoldSolidBrep(
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

  StepNonManifoldSolidBrep resolveNonManifoldSolidBrep(StepEntityInstance instance) {
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

  StepFacettedBrep resolveFacettedBrep(StepEntityInstance instance) {
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

  StepShellBasedSurfaceModel resolveShellBasedSurfaceModel(StepEntityInstance instance) {
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

  StepFaceBasedSurfaceModel resolveFaceBasedSurfaceModel(
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

  StepManifoldSurfaceModel resolveManifoldSurfaceModel(StepEntityInstance instance) {
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

  StepSurfacedEdgeCurve resolveSurfacedEdgeCurve(StepEntityInstance instance) {
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

  StepGeometricTolerance resolveGeometricTolerance(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GEOMETRIC_TOLERANCE");
    requireParameterCount(instance, definition, 3);
    return new StepGeometricTolerance(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepToleranceZoneForm resolveToleranceZoneForm(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TOLERANCE_ZONE_FORM");
    requireParameterCount(instance, definition, 2);
    return new StepToleranceZoneForm(
        instance.id(),
        stringValue(instance, definition, 0),
        enumValue(instance, definition, 1));
  }

  StepToleranceZone resolveToleranceZone(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TOLERANCE_ZONE");
    requireParameterCount(instance, definition, 2);
    return new StepToleranceZone(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepConfigurationItem resolveConfigurationItem(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONFIGURATION_ITEM");
    requireParameterCount(instance, definition, 4);
    return new StepConfigurationItem(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        enumValue(instance, definition, 3));
  }

  StepDirectedDimensionalSize resolveDirectedDimensionalSize(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DIRECTED_DIMENSIONAL_SIZE");
    requireParameterCount(instance, definition, 3);
    return new StepDirectedDimensionalSize(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepGeometricTolerance resolveGeometricTolerance(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 3);
    return new StepGeometricTolerance(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
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
    StepEntityDefinition definition = definition(instance, "COMPOSITE_GROUP_TOLERANCE");
    requireParameterCount(instance, definition, 3);
    return new StepCompositeGroupTolerance(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepGeometricToleranceTarget resolveGeometricToleranceTarget(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GEOMETRIC_TOLERANCE_TARGET");
    requireParameterCount(instance, definition, 3);
    return new StepGeometricToleranceTarget(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2));
  }

  StepQualifiedRepresentationItem resolveQualifiedRepresentationItem(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "QUALIFIED_REPRESENTATION_ITEM");
    requireParameterCount(instance, definition, 2);
    return new StepQualifiedRepresentationItem(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepDatumReferenceModifierWithSign resolveDatumReferenceModifierWithSign(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DATUM_REFERENCE_MODIFIER_WITH_SIGN");
    requireParameterCount(instance, definition, 4);
    return new StepDatumReferenceModifierWithSign(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        stringValue(instance, definition, 2));
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
    StepEntityDefinition definition = definition(instance, "CONFIGURATION_EFFECTIVITY");
    requireParameterCount(instance, definition, 3);
    return new StepConfigurationEffectivity(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }

  StepFeatureControlFrame resolveFeatureControlFrame(StepEntityInstance instance) {
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

  StepRunoutToleranceZone resolveRunoutToleranceZone(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "RUNOUT_TOLERANCE_ZONE");
    requireParameterCount(instance, definition, 2);
    return new StepRunoutToleranceZone(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepGeometricToleranceWithDatumReference resolveGeometricToleranceWithDatumReference(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GEOMETRIC_TOLERANCE_WITH_DATUM_REFERENCE");
    requireParameterCount(instance, definition, 6);
    return new StepGeometricToleranceWithDatumReference(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        numberValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)),
        resolve(referenceId(instance, definition, 4)),
        resolve(referenceId(instance, definition, 5)));
  }

  StepLinearToleranceZone resolveLinearToleranceZone(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LINEAR_TOLERANCE_ZONE");
    requireParameterCount(instance, definition, 4);
    return new StepLinearToleranceZone(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        numberValue(instance, definition, 3));
  }

  StepRadialToleranceZone resolveRadialToleranceZone(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "RADIAL_TOLERANCE_ZONE");
    requireParameterCount(instance, definition, 4);
    return new StepRadialToleranceZone(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        numberValue(instance, definition, 3));
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

  StepDatumReferenceModifierWithValue resolveDatumReferenceModifierWithValue(
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
    StepEntityDefinition definition = definition(instance, "DATUM_REFERENCE_MODIFIER");
    requireParameterCount(instance, definition, 4);
    return new StepDatumReferenceModifier(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  // Phase 4: Tessellated triangulated resolve methods

  StepTriangulatedFace resolveTriangulatedFace(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TRIANGULATED_FACE");
    requireParameterCount(instance, definition, 4);
    return new StepTriangulatedFace(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "TRIANGULATED_FACE vertices must contain entity references"),
        integerList(instance, definition, 2));
  }

  StepComplexTriangulatedFace resolveComplexTriangulatedFace(StepEntityInstance instance) {
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

  StepLayeredItem resolveLayeredItem(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LAYERED_ITEM");
    requireParameterCount(instance, definition, 2);
    return new StepLayeredItem(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepDatum resolveDatum(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DATUM");
    requireParameterCount(instance, definition, 4);
    return new StepDatum(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        booleanValue(instance, definition, 3));
  }

  StepDatumFeature resolveDatumFeature(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DATUM_FEATURE");
    requireParameterCount(instance, definition, 4);
    return new StepDatumFeature(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepDatumReference resolveDatumReference(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DATUM_REFERENCE");
    requireParameterCount(instance, definition, 4);
    return new StepDatumReference(
        instance.id(),
        stringValue(instance, definition, 0),
        integerValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepDatumReferenceCompartment resolveDatumReferenceCompartment(StepEntityInstance instance) {
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

  StepDatumTarget resolveDatumTarget(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DATUM_TARGET");
    requireParameterCount(instance, definition, 4);
    return new StepDatumTarget(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepDatumSystem resolveDatumSystem(StepEntityInstance instance) {
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

  StepDatumSystemReference resolveDatumSystemReference(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DATUM_SYSTEM_REFERENCE");
    requireParameterCount(instance, definition, 4);
    return new StepDatumSystemReference(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        integerValue(instance, definition, 2));
  }

  StepTolerancePair resolveTolerancePair(StepEntityInstance instance) {
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

  StepToleranceSet resolveToleranceSet(StepEntityInstance instance) {
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

  StepGeometricMeasurement resolveGeometricMeasurement(StepEntityInstance instance) {
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

  StepDimensionalMeasurement resolveDimensionalMeasurement(StepEntityInstance instance) {
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
  StepMachiningOperation resolveMachiningOperation(StepEntityInstance instance) {
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

  StepMachiningOperationSequence resolveMachiningOperationSequence(StepEntityInstance instance) {
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
  StepFilletDefinition resolveFilletDefinition(StepEntityInstance instance) {
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

  StepChamferDefinition resolveChamferDefinition(StepEntityInstance instance) {
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

  StepChamfer resolveChamfer(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CHAMFER");
    requireParameterCount(instance, definition, 5);
    List<StepEntity> edges =
        entityReferenceList(
            instance, definition, 2,
            "CHAMFER edges must contain entity references");
    return new StepChamfer(
        instance.id(),
        stringValue(instance, definition, 0),
        edges,
        optionalNumberValue(instance, definition, 3),
        optionalNumberValue(instance, definition, 4));
  }

  StepPocket resolvePocket(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "POCKET");
    requireParameterCount(instance, definition, 6);
    return new StepPocket(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepEntity.class,
            "POCKET profile must reference a profile"),
        optionalNumberValue(instance, definition, 2),
        requireEntity(
            referenceId(instance, definition, 3),
            StepEntity.class,
            "POCKET direction must reference a direction"),
        stringValue(instance, definition, 4));
  }

  StepBore resolveBore(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BORE");
    requireParameterCount(instance, definition, 5);
    return new StepBore(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepEntity.class,
            "BORE profile must reference a profile"),
        optionalNumberValue(instance, definition, 2),
        requireEntity(
            referenceId(instance, definition, 3),
            StepEntity.class,
            "BORE direction must reference a direction"));
  }

  StepCounterboreHole resolveCounterboreHole(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COUNTERBORE_HOLE");
    requireParameterCount(instance, definition, 5);
    return new StepCounterboreHole(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepEntity.class,
            "COUNTERBORE_HOLE through_hole must reference a hole"),
        optionalNumberValue(instance, definition, 2),
        optionalNumberValue(instance, definition, 3));
  }

  StepCountersinkHole resolveCountersinkHole(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COUNTERSINK_HOLE");
    requireParameterCount(instance, definition, 5);
    return new StepCountersinkHole(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepEntity.class,
            "COUNTERSINK_HOLE through_hole must reference a hole"),
        optionalNumberValue(instance, definition, 2),
        optionalNumberValue(instance, definition, 3));
  }

  StepRound resolveRound(StepEntityInstance instance) {
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

  StepGroove resolveGroove(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GROOVE");
    requireParameterCount(instance, definition, 5);
    return new StepGroove(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        optionalNumberValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)));
  }

  StepHole resolveHole(StepEntityInstance instance) {
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

  StepSlot resolveSlot(StepEntityInstance instance) {
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

  StepStud resolveStud(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "STUD");
    requireParameterCount(instance, definition, 5);
    return new StepStud(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        optionalNumberValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)));
  }

  StepProtrusion resolveProtrusion(StepEntityInstance instance) {
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

  StepCutout resolveCutout(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CUTOUT");
    requireParameterCount(instance, definition, 5);
    return new StepCutout(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        optionalNumberValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)));
  }

  StepDepression resolveDepression(StepEntityInstance instance) {
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

  StepMarking resolveMarking(StepEntityInstance instance) {
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
  StepCircularPattern resolveCircularPattern(StepEntityInstance instance) {
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

  StepLinearPattern resolveLinearPattern(StepEntityInstance instance) {
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

  StepCompositeDatumReference resolveCompositeDatumReference(StepEntityInstance instance) {
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

  StepCsgVolume resolveCsgVolume(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CSG_VOLUME");
    requireParameterCount(instance, definition, 3);
    return new StepCsgVolume(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepBlockVolume resolveBlockVolume(StepEntityInstance instance) {
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

  StepAssemblyProcessPlan resolveAssemblyProcessPlan(StepEntityInstance instance) {
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

  StepMachiningProcessPlan resolveMachiningProcessPlan(StepEntityInstance instance) {
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

  StepMachiningWorkPlan resolveMachiningWorkPlan(StepEntityInstance instance) {
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

  StepRectangularToleranceZone resolveRectangularToleranceZone(StepEntityInstance instance) {
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

  StepToleranceModifier resolveToleranceModifier(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TOLERANCE_MODIFIER");
    requireParameterCount(instance, definition, 5);
    return new StepToleranceModifier(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        numberValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)));
  }

  StepTypedMeasureWithUnit resolveTypedMeasureWithUnit(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 3);
    return new StepTypedMeasureWithUnit(
        instance.id(),
        entityName,
        numberValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepCartesianTransformationOperator resolveCartesianTransformationOperator(
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

  StepCartesianTransformationOperator resolveCartesianTransformationOperator(
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

  StepDirection optionalResolveDirection(int id) {
    StepEntity entity = resolve(id);
    if (entity instanceof StepDirection direction) {
      return direction;
    }
    return null;
  }

  StepCartesianPoint optionalResolveCartesianPoint(int id) {
    StepEntity entity = resolve(id);
    if (entity instanceof StepCartesianPoint point) {
      return point;
    }
    return null;
  }

  StepDimensionalSize resolveDimensionalSize(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DIMENSIONAL_SIZE");
    requireParameterCount(instance, definition, 4);
    return new StepDimensionalSize(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepDimensionalLocation resolveDimensionalLocation(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DIMENSIONAL_LOCATION");
    requireParameterCount(instance, definition, 4);
    return new StepDimensionalLocation(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepShapeDimensionRepresentation resolveShapeDimensionRepresentation(StepEntityInstance instance) {
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

  StepPlusMinusTolerance resolvePlusMinusTolerance(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PLUS_MINUS_TOLERANCE");
    requireParameterCount(instance, definition, 4);
    return new StepPlusMinusTolerance(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }

  StepToleranceValue resolveToleranceValue(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TOLERANCE_VALUE");
    requireParameterCount(instance, definition, 4);
    return new StepToleranceValue(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        numberValue(instance, definition, 2));
  }

  StepMeasureRepresentationItemWithUnit resolveMeasureRepresentationItemWithUnit(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MEASURE_REPRESENTATION_ITEM_WITH_UNIT");
    requireParameterCount(instance, definition, 4);
    return new StepMeasureRepresentationItemWithUnit(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepMeasureQualification resolveMeasureQualification(StepEntityInstance instance) {
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

  StepMakeFromFeature resolveMakeFromFeature(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MAKE_FROM_FEATURE");
    requireParameterCount(instance, definition, 4);
    return new StepMakeFromFeature(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepMakeFromUsageOption resolveMakeFromUsageOption(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MAKE_FROM_USAGE_OPTION");
    requireParameterCount(instance, definition, 4);
    return new StepMakeFromUsageOption(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepQuantifiedAssemblyComponentUsage resolveQuantifiedAssemblyComponentUsage(
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

  StepSpecifiedHigherUsageOccurrence resolveSpecifiedHigherUsageOccurrence(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SPECIFIED_HIGHER_USAGE_OCCURRENCE");
    requireParameterCount(instance, definition, 4);
    return new StepSpecifiedHigherUsageOccurrence(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepAlternateProductRelationship resolveAlternateProductRelationship(
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

  StepProductDefinitionWithAssociatedDocuments resolveProductDefinitionWithAssociatedDocuments(
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

  StepShapeAspectShapeRepresentation resolveShapeAspectShapeRepresentation(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SHAPE_ASPECT_SHAPE_REPRESENTATION");
    requireParameterCount(instance, definition, 4);
    return new StepShapeAspectShapeRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepMakeFromBuildAssembly resolveMakeFromBuildAssembly(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MAKE_FROM_BUILD_ASSEMBLY");
    requireParameterCount(instance, definition, 4);
    return new StepMakeFromBuildAssembly(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepAssemblyComponentRelationship resolveAssemblyComponentRelationship(
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

  StepDesignMakeFrom resolveDesignMakeFrom(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DESIGN_MAKE_FROM");
    requireParameterCount(instance, definition, 5);
    return new StepDesignMakeFrom(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  StepInterpolatedConfigurationSegment resolveInterpolatedConfigurationSegment(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "INTERPOLATED_CONFIGURATION_SEGMENT");
    requireParameterCount(instance, definition, 4);
    return new StepInterpolatedConfigurationSegment(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepRangeDimensionalSize resolveRangeDimensionalSize(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "RANGE_DIMENSIONAL_SIZE");
    requireParameterCount(instance, definition, 5);
    return new StepRangeDimensionalSize(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  StepDesignedPartDesignVersion resolveDesignedPartDesignVersion(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DESIGNED_PART_DESIGN_VERSION");
    requireParameterCount(instance, definition, 4);
    return new StepDesignedPartDesignVersion(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepSurfaceStyleRendering resolveSurfaceStyleRendering(StepEntityInstance instance) {
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

  StepSurfaceStyleRenderingWithProperties resolveSurfaceStyleRenderingWithProperties(
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

  StepRenderingProperties resolveRenderingProperties(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "RENDERING_PROPERTIES");
    requireParameterCount(instance, definition, 4);
    return new StepRenderingProperties(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        numberValue(instance, definition, 2));
  }

  StepLightSource resolveLightSource(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LIGHT_SOURCE");
    requireParameterCount(instance, definition, 4);
    return new StepLightSource(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2));
  }

  StepLightSourceAmbient resolveLightSourceAmbient(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LIGHT_SOURCE_AMBIENT");
    requireParameterCount(instance, definition, 4);
    return new StepLightSourceAmbient(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2));
  }

  StepLightSourceDirectional resolveLightSourceDirectional(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LIGHT_SOURCE_DIRECTIONAL");
    requireParameterCount(instance, definition, 5);
    return new StepLightSourceDirectional(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)));
  }

  StepLightSourcePositional resolveLightSourcePositional(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LIGHT_SOURCE_POSITIONAL");
    requireParameterCount(instance, definition, 5);
    return new StepLightSourcePositional(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)));
  }

  StepLightSourceSpot resolveLightSourceSpot(StepEntityInstance instance) {
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

  StepPresentationLayerUsage resolvePresentationLayerUsage(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRESENTATION_LAYER_USAGE");
    requireParameterCount(instance, definition, 4);
    return new StepPresentationLayerUsage(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepCameraModelD2 resolveCameraModelD2(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CAMERA_MODEL_D2");
    requireParameterCount(instance, definition, 4);
    return new StepCameraModelD2(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }

  StepCameraModelD3 resolveCameraModelD3(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CAMERA_MODEL_D3");
    requireParameterCount(instance, definition, 5);
    return new StepCameraModelD3(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        numberValue(instance, definition, 3));
  }

  StepCameraUsage resolveCameraUsage(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CAMERA_USAGE");
    requireParameterCount(instance, definition, 4);
    return new StepCameraUsage(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepCameraImage resolveCameraImage(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CAMERA_IMAGE");
    requireParameterCount(instance, definition, 5);
    return new StepCameraImage(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        integerValue(instance, definition, 2),
        integerValue(instance, definition, 3));
  }

  StepPlanarBox resolvePlanarBox(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PLANAR_BOX");
    requireParameterCount(instance, definition, 5);
    return new StepPlanarBox(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  StepPlanarExtent resolvePlanarExtent(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PLANAR_EXTENT");
    requireParameterCount(instance, definition, 4);
    return new StepPlanarExtent(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        numberValue(instance, definition, 2));
  }

  StepViewVolume resolveViewVolume(StepEntityInstance instance) {
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

  StepMechanicalDesignShapeRepresentation resolveMechanicalDesignShapeRepresentation(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MECHANICAL_DESIGN_SHAPE_REPRESENTATION");
    requireParameterCount(instance, definition, 3);
    return new StepMechanicalDesignShapeRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepKinematicPair resolveKinematicPair(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "KINEMATIC_PAIR");
    requireParameterCount(instance, definition, 5);
    return new StepKinematicPair(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  StepKinematicJoint resolveKinematicJoint(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "KINEMATIC_JOINT");
    requireParameterCount(instance, definition, 4);
    return new StepKinematicJoint(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepKinematicLink resolveKinematicLink(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "KINEMATIC_LINK");
    requireParameterCount(instance, definition, 4);
    return new StepKinematicLink(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepKinematicStructure resolveKinematicStructure(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "KINEMATIC_STRUCTURE");
    requireParameterCount(instance, definition, 4);
    return new StepKinematicStructure(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepKinematicPair resolveKinematicPair(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 7);
    return new StepKinematicPair(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  StepPrismaticPair resolvePrismaticPair(StepEntityInstance instance) {
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

  StepRevolutePair resolveRevolutePair(StepEntityInstance instance) {
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

  StepCylindricalPair resolveCylindricalPair(StepEntityInstance instance) {
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

  StepSphericalPair resolveSphericalPair(StepEntityInstance instance) {
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

  StepPlanarPair resolvePlanarPair(StepEntityInstance instance) {
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

  StepUniversalPair resolveUniversalPair(StepEntityInstance instance) {
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

  StepScrewPair resolveScrewPair(StepEntityInstance instance) {
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

  StepGearPair resolveGearPair(StepEntityInstance instance) {
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

  StepGearPairWithRange resolveGearPairWithRange(StepEntityInstance instance) {
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

  StepRackAndPinionPair resolveRackAndPinionPair(StepEntityInstance instance) {
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

  StepLowOrderKinematicPairWithRange resolveLowOrderKinematicPairWithRange(
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

  StepActuatedKinematicPair resolveActuatedKinematicPair(StepEntityInstance instance) {
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

  StepMechanismStateRepresentation resolveMechanismStateRepresentation(
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

  StepKinematicPath resolveKinematicPath(StepEntityInstance instance) {
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

  StepKinematicFrameBasedTransformation resolveKinematicFrameBasedTransformation(
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

  StepValidationPropertyRepresentation resolveValidationPropertyRepresentation(
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

  StepCalculatedGeometricRepresentationItem resolveCalculatedGeometricRepresentationItem(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CALCULATED_GEOMETRIC_REPRESENTATION_ITEM");
    requireParameterCount(instance, definition, 3);
    return new StepCalculatedGeometricRepresentationItem(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }


  // Phase 5: FEA resolve methods

  StepVolume3dElementRepresentation resolveVolume3dElementRepresentation(
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

  StepFeaNode resolveFeaNode(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "NODE");
    requireParameterCount(instance, definition, 4);
    return new StepFeaNode(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  StepFeaElement resolveFeaElement(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ELEMENT");
    requireParameterCount(instance, definition, 4);
    StepEntity elementProperty = tryResolveReference(definition.parameters().get(3));
    return new StepFeaElement(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        entityReferenceList(instance, definition, 2, "ELEMENT nodes must contain entity references"),
        elementProperty);
  }

  StepFeaLoad resolveFeaLoad(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LOAD");
    requireParameterCount(instance, definition, 4);
    return new StepFeaLoad(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        numberValue(instance, definition, 3));
  }

  StepFeaModel resolveFeaModel(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_MODEL");
    requireParameterCount(instance, definition, 5);
    return new StepFeaModel(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        entityReferenceList(instance, definition, 2, "FEA_MODEL elements must contain entity references"),
        entityReferenceList(instance, definition, 3, "FEA_MODEL loads must contain entity references"),
        entityReferenceList(instance, definition, 4, "FEA_MODEL boundary conditions must contain entity references"));
  }

  StepMaterial resolveMaterial(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MATERIAL");
    requireParameterCount(instance, definition, 5);
    return new StepMaterial(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3),
        numberValue(instance, definition, 4));
  }

  StepFeaLinearMaterial resolveFeaLinearMaterial(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_LINEAR_MATERIAL");
    requireParameterCount(instance, definition, 4);
    return new StepFeaLinearMaterial(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  StepFeaNonLinearMaterial resolveFeaNonLinearMaterial(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_NON_LINEAR_MATERIAL");
    requireParameterCount(instance, definition, 3);
    return new StepFeaNonLinearMaterial(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        stringValue(instance, definition, 2));
  }

  StepFeaMassDensity resolveFeaMassDensity(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_MASS_DENSITY");
    requireParameterCount(instance, definition, 2);
    return new StepFeaMassDensity(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1));
  }

  StepFeaYieldStress resolveFeaYieldStress(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_YIELD_STRESS");
    requireParameterCount(instance, definition, 2);
    return new StepFeaYieldStress(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1));
  }

  StepFeaUltimateStress resolveFeaUltimateStress(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_ULTIMATE_STRESS");
    requireParameterCount(instance, definition, 2);
    return new StepFeaUltimateStress(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1));
  }

  StepDisplacementBoundaryCondition resolveDisplacementBoundaryCondition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DISPLACEMENT_BOUNDARY_CONDITION");
    requireParameterCount(instance, definition, 5);
    return new StepDisplacementBoundaryCondition(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3),
        numberValue(instance, definition, 4));
  }

  StepVelocityBoundaryCondition resolveVelocityBoundaryCondition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "VELOCITY_BOUNDARY_CONDITION");
    requireParameterCount(instance, definition, 5);
    return new StepVelocityBoundaryCondition(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3),
        numberValue(instance, definition, 4));
  }

  StepAccelerationBoundaryCondition resolveAccelerationBoundaryCondition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ACCELERATION_BOUNDARY_CONDITION");
    requireParameterCount(instance, definition, 5);
    return new StepAccelerationBoundaryCondition(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3),
        numberValue(instance, definition, 4));
  }

  StepForceBoundaryCondition resolveForceBoundaryCondition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FORCE_BOUNDARY_CONDITION");
    requireParameterCount(instance, definition, 5);
    return new StepForceBoundaryCondition(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3),
        numberValue(instance, definition, 4));
  }

  StepPressureBoundaryCondition resolvePressureBoundaryCondition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRESSURE_BOUNDARY_CONDITION");
    requireParameterCount(instance, definition, 3);
    return new StepPressureBoundaryCondition(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2));
  }

  StepThermalBoundaryCondition resolveThermalBoundaryCondition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "THERMAL_BOUNDARY_CONDITION");
    requireParameterCount(instance, definition, 4);
    return new StepThermalBoundaryCondition(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  StepStressAnalysis resolveStressAnalysis(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "STRESS_ANALYSIS");
    requireParameterCount(instance, definition, 2);
    return new StepStressAnalysis(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1));
  }

  StepBucklingAnalysis resolveBucklingAnalysis(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BUCKLING_ANALYSIS");
    requireParameterCount(instance, definition, 2);
    return new StepBucklingAnalysis(
        instance.id(),
        stringValue(instance, definition, 0),
        (int) numberValue(instance, definition, 1));
  }

  StepModalAnalysis resolveModalAnalysis(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MODAL_ANALYSIS");
    requireParameterCount(instance, definition, 2);
    return new StepModalAnalysis(
        instance.id(),
        stringValue(instance, definition, 0),
        (int) numberValue(instance, definition, 1));
  }

  StepThermalAnalysis resolveThermalAnalysis(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "THERMAL_ANALYSIS");
    requireParameterCount(instance, definition, 2);
    return new StepThermalAnalysis(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1));
  }

  StepStructuralAnalysisModel resolveStructuralAnalysisModel(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "STRUCTURAL_ANALYSIS_MODEL");
    requireParameterCount(instance, definition, 5);
    return new StepStructuralAnalysisModel(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        entityReferenceList(instance, definition, 2, "STRUCTURAL_ANALYSIS_MODEL elements must contain entity references"),
        entityReferenceList(instance, definition, 3, "STRUCTURAL_ANALYSIS_MODEL loads must contain entity references"),
        entityReferenceList(instance, definition, 4, "STRUCTURAL_ANALYSIS_MODEL boundary conditions must contain entity references"));
  }

  StepRevoluteJoint resolveRevoluteJoint(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "REVOLUTE_JOINT");
    requireParameterCount(instance, definition, 5);
    return new StepRevoluteJoint(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        resolve(referenceId(instance, definition, 4)));
  }

  StepPrismaticJoint resolvePrismaticJoint(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRISMATIC_JOINT");
    requireParameterCount(instance, definition, 5);
    return new StepPrismaticJoint(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        resolve(referenceId(instance, definition, 4)));
  }

  StepSphericalJoint resolveSphericalJoint(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SPHERICAL_JOINT");
    requireParameterCount(instance, definition, 5);
    return new StepSphericalJoint(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        resolve(referenceId(instance, definition, 4)));
  }

  StepCylindricalJoint resolveCylindricalJoint(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CYLINDRICAL_JOINT");
    requireParameterCount(instance, definition, 5);
    return new StepCylindricalJoint(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        resolve(referenceId(instance, definition, 4)));
  }

  StepPlanarJoint resolvePlanarJoint(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PLANAR_JOINT");
    requireParameterCount(instance, definition, 5);
    return new StepPlanarJoint(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        resolve(referenceId(instance, definition, 4)));
  }

  StepScrewJoint resolveScrewJoint(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SCREW_JOINT");
    requireParameterCount(instance, definition, 5);
    return new StepScrewJoint(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        numberValue(instance, definition, 4));
  }

  StepGeneralJoint resolveGeneralJoint(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GENERAL_JOINT");
    requireParameterCount(instance, definition, 4);
    return new StepGeneralJoint(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
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
    StepEntityDefinition definition = definition(instance, "JOINT_VALUE");
    requireParameterCount(instance, definition, 3);
    return new StepJointValue(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        stringValue(instance, definition, 2));
  }

  StepKinematicChain resolveKinematicChain(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "KINEMATIC_CHAIN");
    requireParameterCount(instance, definition, 3);
    return new StepKinematicChain(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1));
  }

  StepKinematicModel resolveKinematicModel(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "KINEMATIC_MODEL");
    requireParameterCount(instance, definition, 3);
    return new StepKinematicModel(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1));
  }

  StepKinematicProperty resolveKinematicProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "KINEMATIC_PROPERTY");
    requireParameterCount(instance, definition, 3);
    return new StepKinematicProperty(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepMotionConstraint resolveMotionConstraint(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MOTION_CONSTRAINT");
    requireParameterCount(instance, definition, 4);
    return new StepMotionConstraint(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
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
    StepEntityDefinition definition = definition(instance, "SHAPE_REPRESENTATION_TRANSFORMATION");
    requireParameterCount(instance, definition, 3);
    return new StepShapeRepresentationTransformation(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepRepresentationContext3d resolveRepresentationContext3d(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "REPRESENTATION_CONTEXT_3D");
    requireParameterCount(instance, definition, 3);
    return new StepRepresentationContext3d(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        doubleList(instance, definition, 2));
  }

  StepAppliedAttributeClassification resolveAppliedAttributeClassification(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "APPLIED_ATTRIBUTE_CLASSIFICATION");
    requireParameterCount(instance, definition, 3);
    return new StepAppliedAttributeClassification(
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

  StepStructuralAnalysisRepresentation resolveStructuralAnalysisRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "STRUCTURAL_ANALYSIS_REPRESENTATION");
    requireParameterCount(instance, definition, 3);
    return new StepStructuralAnalysisRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        entityReferenceList(instance, definition, 2, "STRUCTURAL_ANALYSIS_REPRESENTATION items must contain entity references"));
  }

  StepStructuralAnalysisRepresentationParameters resolveStructuralAnalysisRepresentationParameters(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "STRUCTURAL_ANALYSIS_REPRESENTATION_PARAMETERS");
    requireParameterCount(instance, definition, 2);
    return new StepStructuralAnalysisRepresentationParameters(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1));
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
    StepEntityDefinition definition = definition(instance, "FILL_AREA_SHAPE_USE");
    requireParameterCount(instance, definition, 2);
    return new StepFillAreaShapeUse(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
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
    StepEntityDefinition definition = definition(instance, "TESSELLATED_COORDINATE_SET");
    requireParameterCount(instance, definition, 2);
    return new StepTessellatedCoordinateSet(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1, "TESSELLATED_COORDINATE_SET coordinates must contain entity references"));
  }

  StepUncertaintyMeasure resolveUncertaintyMeasure(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "UNCERTAINTY_MEASURE");
    requireParameterCount(instance, definition, 3);
    return new StepUncertaintyMeasure(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        stringValue(instance, definition, 2));
  }

  StepStructAnalysisModel resolveStructAnalysisModel(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "STRUCT_ANALYSIS_MODEL");
    requireParameterCount(instance, definition, 3);
    return new StepStructAnalysisModel(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1));
  }

  StepElementVolume resolveElementVolume(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ELEMENT_VOLUME");
    requireParameterCount(instance, definition, 2);
    return new StepElementVolume(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1));
  }

  StepVolumeElement resolveVolumeElement(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "VOLUME_ELEMENT");
    requireParameterCount(instance, definition, 4);
    return new StepVolumeElement(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1, "VOLUME_ELEMENT nodes must contain entity references"),
        tryResolveReference(definition.parameters().get(2)));
  }

  StepSurfaceElement resolveSurfaceElement(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_ELEMENT");
    requireParameterCount(instance, definition, 4);
    return new StepSurfaceElement(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1, "SURFACE_ELEMENT nodes must contain entity references"),
        tryResolveReference(definition.parameters().get(2)));
  }

  StepLineElement resolveLineElement(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LINE_ELEMENT");
    requireParameterCount(instance, definition, 4);
    return new StepLineElement(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1, "LINE_ELEMENT nodes must contain entity references"),
        tryResolveReference(definition.parameters().get(2)));
  }

  StepMassElement resolveMassElement(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MASS_ELEMENT");
    requireParameterCount(instance, definition, 3);
    return new StepMassElement(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1, "MASS_ELEMENT nodes must contain entity references"),
        numberValue(instance, definition, 2));
  }

  StepConnectivityElement resolveConnectivityElement(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONNECTIVITY_ELEMENT");
    requireParameterCount(instance, definition, 4);
    return new StepConnectivityElement(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1, "CONNECTIVITY_ELEMENT nodes must contain entity references"),
        tryResolveReference(definition.parameters().get(2)));
  }

  StepElementGeometricDescription resolveElementGeometricDescription(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ELEMENT_GEOMETRIC_DESCRIPTION");
    requireParameterCount(instance, definition, 3);
    return new StepElementGeometricDescription(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepUniformSurfaceElement resolveUniformSurfaceElement(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "UNIFORM_SURFACE_ELEMENT");
    requireParameterCount(instance, definition, 4);
    return new StepUniformSurfaceElement(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1, "UNIFORM_SURFACE_ELEMENT nodes must contain entity references"),
        tryResolveReference(definition.parameters().get(2)));
  }

  StepUniformVolumeElement resolveUniformVolumeElement(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "UNIFORM_VOLUME_ELEMENT");
    requireParameterCount(instance, definition, 4);
    return new StepUniformVolumeElement(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1, "UNIFORM_VOLUME_ELEMENT nodes must contain entity references"),
        tryResolveReference(definition.parameters().get(2)));
  }

  StepNodeRepresentation resolveNodeRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "NODE_REPRESENTATION");
    requireParameterCount(instance, definition, 2);
    return new StepNodeRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1, "NODE_REPRESENTATION representedNodes must contain entity references"));
  }

  StepVolume3dElementProperty resolveVolume3dElementProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "VOLUME_3D_ELEMENT_PROPERTY");
    requireParameterCount(instance, definition, 5);
    return new StepVolume3dElementProperty(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  StepCurve3dElementProperty resolveCurve3dElementProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CURVE_3D_ELEMENT_PROPERTY");
    requireParameterCount(instance, definition, 5);
    return new StepCurve3dElementProperty(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  StepSurface3dElementProperty resolveSurface3dElementProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_3D_ELEMENT_PROPERTY");
    requireParameterCount(instance, definition, 5);
    return new StepSurface3dElementProperty(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  StepFeaMaterialPropertyRepresentation resolveFeaMaterialPropertyRepresentation(
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

  StepElementVolume2d resolveElementVolume2d(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ELEMENT_VOLUME_2D");
    requireParameterCount(instance, definition, 4);
    return new StepElementVolume2d(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "ELEMENT_VOLUME_2D nodes must contain entity references"),
        stringValue(instance, definition, 2));
  }

  StepElementVolume3d resolveElementVolume3d(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ELEMENT_VOLUME_3D");
    requireParameterCount(instance, definition, 4);
    return new StepElementVolume3d(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "ELEMENT_VOLUME_3D nodes must contain entity references"),
        stringValue(instance, definition, 2));
  }

  StepNodeSet resolveNodeSet(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "NODE_SET");
    requireParameterCount(instance, definition, 3);
    return new StepNodeSet(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "NODE_SET nodes must contain entity references"));
  }

  StepElementSet resolveElementSet(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ELEMENT_SET");
    requireParameterCount(instance, definition, 3);
    return new StepElementSet(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "ELEMENT_SET elements must contain entity references"));
  }

  StepFeaSecuredVariable resolveFeaSecuredVariable(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_SECURED_VARIABLE");
    requireParameterCount(instance, definition, 4);
    return new StepFeaSecuredVariable(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }

  StepFeaConstantFunction3d resolveFeaConstantFunction3d(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_CONSTANT_FUNCTION_3D");
    requireParameterCount(instance, definition, 4);
    return new StepFeaConstantFunction3d(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepFeaLinearAlgebraicMatrix resolveFeaLinearAlgebraicMatrix(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_LINEAR_ALGEBRAIC_MATRIX");
    requireParameterCount(instance, definition, 5);
    return new StepFeaLinearAlgebraicMatrix(
        instance.id(),
        stringValue(instance, definition, 0),
        (int) numberValue(instance, definition, 1),
        (int) numberValue(instance, definition, 2),
        numberList(instance, definition, 3));
  }

  StepFeaLinearAlgebraicVector resolveFeaLinearAlgebraicVector(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_LINEAR_ALGEBRAIC_VECTOR");
    requireParameterCount(instance, definition, 4);
    return new StepFeaLinearAlgebraicVector(
        instance.id(),
        stringValue(instance, definition, 0),
        (int) numberValue(instance, definition, 1),
        numberList(instance, definition, 2));
  }

  StepFeaAxis2Placement3d resolveFeaAxis2Placement3d(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_AXIS_2_PLACEMENT_3D");
    requireParameterCount(instance, definition, 5);
    return new StepFeaAxis2Placement3d(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  StepFeaGroupRepresentation resolveFeaGroupRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_GROUP_REPRESENTATION");
    requireParameterCount(instance, definition, 4);
    return new StepFeaGroupRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "FEA_GROUP_REPRESENTATION representations must contain entity references"),
        stringValue(instance, definition, 2));
  }

  // New FEA element property resolvers
  StepFeaShellElementProperty resolveFeaShellElementProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_SHELL_ELEMENT_PROPERTY");
    requireParameterCount(instance, definition, 4);
    return new StepFeaShellElementProperty(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "FEA_SHELL_ELEMENT_PROPERTY properties must contain entity references"),
        resolve(referenceId(instance, definition, 2)));
  }

  StepFeaBeamElementProperty resolveFeaBeamElementProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_BEAM_ELEMENT_PROPERTY");
    requireParameterCount(instance, definition, 5);
    return new StepFeaBeamElementProperty(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "FEA_BEAM_ELEMENT_PROPERTY properties must contain entity references"),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  StepFea2DElementProperty resolveFea2DElementProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_2D_ELEMENT_PROPERTY");
    requireParameterCount(instance, definition, 4);
    return new StepFea2DElementProperty(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "FEA_2D_ELEMENT_PROPERTY properties must contain entity references"),
        resolve(referenceId(instance, definition, 2)));
  }

  StepFea3DElementProperty resolveFea3DElementProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_3D_ELEMENT_PROPERTY");
    requireParameterCount(instance, definition, 4);
    return new StepFea3DElementProperty(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "FEA_3D_ELEMENT_PROPERTY properties must contain entity references"),
        resolve(referenceId(instance, definition, 2)));
  }

  StepFeaTrussElementProperty resolveFeaTrussElementProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_TRUSS_ELEMENT_PROPERTY");
    requireParameterCount(instance, definition, 4);
    return new StepFeaTrussElementProperty(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepFeaSpringElementProperty resolveFeaSpringElementProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_SPRING_ELEMENT_PROPERTY");
    requireParameterCount(instance, definition, 4);
    return new StepFeaSpringElementProperty(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepFeaVolumeElementProperty resolveFeaVolumeElementProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEA_VOLUME_ELEMENT_PROPERTY");
    requireParameterCount(instance, definition, 4);
    return new StepFeaVolumeElementProperty(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "FEA_VOLUME_ELEMENT_PROPERTY properties must contain entity references"),
        resolve(referenceId(instance, definition, 2)));
  }

  // Unit with unit resolvers
  StepLengthUnitWithUnit resolveLengthUnitWithUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LENGTH_UNIT_WITH_UNIT");
    requireParameterCount(instance, definition, 4);
    return new StepLengthUnitWithUnit(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }

  StepPlaneAngleUnitWithUnit resolvePlaneAngleUnitWithUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PLANE_ANGLE_UNIT_WITH_UNIT");
    requireParameterCount(instance, definition, 4);
    return new StepPlaneAngleUnitWithUnit(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }

  StepVolumeUnitWithUnit resolveVolumeUnitWithUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "VOLUME_UNIT_WITH_UNIT");
    requireParameterCount(instance, definition, 4);
    return new StepVolumeUnitWithUnit(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }

  StepAreaUnitWithUnit resolveAreaUnitWithUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "AREA_UNIT_WITH_UNIT");
    requireParameterCount(instance, definition, 4);
    return new StepAreaUnitWithUnit(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }

  StepMassUnitWithUnit resolveMassUnitWithUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MASS_UNIT_WITH_UNIT");
    requireParameterCount(instance, definition, 4);
    return new StepMassUnitWithUnit(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }

  StepConversionBasedUnitAndUnit resolveConversionBasedUnitAndUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONVERSION_BASED_UNIT_AND_UNIT");
    requireParameterCount(instance, definition, 4);
    return new StepConversionBasedUnitAndUnit(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
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
    StepEntityDefinition definition = definition(instance, "SWEPT_PROFILE_AREA_OUTLINE");
    requireParameterCount(instance, definition, 3);
    return new StepSweptProfileAreaOutline(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  // Kinematic reference resolvers
  StepKinematicLinkReference resolveKinematicLinkReference(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "KINEMATIC_LINK_REFERENCE");
    requireParameterCount(instance, definition, 3);
    return new StepKinematicLinkReference(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepKinematicJointReference resolveKinematicJointReference(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "KINEMATIC_JOINT_REFERENCE");
    requireParameterCount(instance, definition, 3);
    return new StepKinematicJointReference(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  // Product representation resolvers
  StepHybridShapeRepresentation resolveHybridShapeRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "HYBRID_SHAPE_REPRESENTATION");
    requireParameterCount(instance, definition, 4);
    return new StepHybridShapeRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        entityReferenceList(instance, definition, 2,
            "HYBRID_SHAPE_REPRESENTATION items must contain entity references"));
  }

  StepDrawingRepresentation resolveDrawingRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DRAWING_REPRESENTATION");
    requireParameterCount(instance, definition, 4);
    return new StepDrawingRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        entityReferenceList(instance, definition, 2,
            "DRAWING_REPRESENTATION items must contain entity references"));
  }

  StepSchematicRepresentation resolveSchematicRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SCHEMATIC_REPRESENTATION");
    requireParameterCount(instance, definition, 4);
    return new StepSchematicRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        entityReferenceList(instance, definition, 2,
            "SCHEMATIC_REPRESENTATION items must contain entity references"));
  }

  StepSketchRepresentation resolveSketchRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SKETCH_REPRESENTATION");
    requireParameterCount(instance, definition, 4);
    return new StepSketchRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        entityReferenceList(instance, definition, 2,
            "SKETCH_REPRESENTATION items must contain entity references"));
  }

  StepSectionRepresentation resolveSectionRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SECTION_REPRESENTATION");
    requireParameterCount(instance, definition, 4);
    return new StepSectionRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        entityReferenceList(instance, definition, 2,
            "SECTION_REPRESENTATION items must contain entity references"));
  }

  StepTabulationRepresentation resolveTabulationRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TABULATION_REPRESENTATION");
    requireParameterCount(instance, definition, 4);
    return new StepTabulationRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        entityReferenceList(instance, definition, 2,
            "TABULATION_REPRESENTATION items must contain entity references"));
  }

  StepZoneRepresentation resolveZoneRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ZONE_REPRESENTATION");
    requireParameterCount(instance, definition, 4);
    return new StepZoneRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        entityReferenceList(instance, definition, 2,
            "ZONE_REPRESENTATION items must contain entity references"));
  }

  StepCsgPrimitive3D resolveCsgPrimitive3D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CSG_PRIMITIVE_3D");
    requireParameterCount(instance, definition, 3);
    return new StepCsgPrimitive3D(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepCompoundRepresentationItem resolveCompoundRepresentationItem(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COMPOUND_REPRESENTATION_ITEM");
    requireParameterCount(instance, definition, 3);
    return new StepCompoundRepresentationItem(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "COMPOUND_REPRESENTATION_ITEM items must contain entity references"));
  }

  StepContextDependentGeometricShapeRepresentation resolveContextDependentGeometricShapeRepresentation(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONTEXT_DEPENDENT_GEOMETRIC_SHAPE_REPRESENTATION");
    requireParameterCount(instance, definition, 4);
    return new StepContextDependentGeometricShapeRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        entityReferenceList(instance, definition, 2,
            "CONTEXT_DEPENDENT_GEOMETRIC_SHAPE_REPRESENTATION items must contain entity references"));
  }

  StepUsageAssociation resolveUsageAssociation(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "USAGE_ASSOCIATION");
    requireParameterCount(instance, definition, 4);
    return new StepUsageAssociation(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
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
    StepEntityDefinition definition = definition(instance, "EXCLUSION_ASSIGNMENT");
    requireParameterCount(instance, definition, 4);
    return new StepExclusionAssignment(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "EXCLUSION_ASSIGNMENT assigned items must contain entity references"),
        resolve(referenceId(instance, definition, 2)));
  }

  StepDateTimeEffectivity resolveDateTimeEffectivity(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DATE_TIME_EFFECTIVITY");
    requireParameterCount(instance, definition, 3);
    return new StepDateTimeEffectivity(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepDateEffectivity resolveDateEffectivity(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DATE_EFFECTIVITY");
    requireParameterCount(instance, definition, 3);
    return new StepDateEffectivity(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepLotEffectivity resolveLotEffectivity(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LOT_EFFECTIVITY");
    requireParameterCount(instance, definition, 3);
    return new StepLotEffectivity(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepSerialNumberEffectivity resolveSerialNumberEffectivity(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SERIAL_NUMBER_EFFECTIVITY");
    requireParameterCount(instance, definition, 3);
    return new StepSerialNumberEffectivity(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1));
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
    StepEntityDefinition definition = definition(instance, "ANNOTATION_FILL_AREA_REGION");
    requireParameterCount(instance, definition, 3);
    return new StepAnnotationFillAreaRegion(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "ANNOTATION_FILL_AREA_REGION regions must contain entity references"));
  }

  // Product resolvers
  StepAssemblyOperation resolveAssemblyOperation(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ASSEMBLY_OPERATION");
    requireParameterCount(instance, definition, 8);
    return new StepAssemblyOperation(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        numberList(instance, definition, 2),
        entityReferenceList(instance, definition, 3,
            "ASSEMBLY_OPERATION components must contain entity references"),
        resolve(referenceId(instance, definition, 4)),
        resolve(referenceId(instance, definition, 5)),
        numberValue(instance, definition, 6));
  }

  StepAssemblySequence resolveAssemblySequence(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ASSEMBLY_SEQUENCE");
    requireParameterCount(instance, definition, 8);
    return new StepAssemblySequence(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "ASSEMBLY_SEQUENCE operations must contain entity references"),
        intList(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)),
        entityReferenceList(instance, definition, 4,
            "ASSEMBLY_SEQUENCE tools must contain entity references"),
        numberValue(instance, definition, 5),
        entityReferenceList(instance, definition, 6,
            "ASSEMBLY_SEQUENCE dependencies must contain entity references"));
  }

  StepAssemblyStructure resolveAssemblyStructure(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ASSEMBLY_STRUCTURE");
    requireParameterCount(instance, definition, 6);
    return new StepAssemblyStructure(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        entityReferenceList(instance, definition, 2,
            "ASSEMBLY_STRUCTURE components must contain entity references"),
        entityReferenceList(instance, definition, 3,
            "ASSEMBLY_STRUCTURE relationships must contain entity references"),
        stringValue(instance, definition, 4));
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
    StepEntityDefinition definition = definition(instance, "MECHANISM_DEFINITION");
    requireParameterCount(instance, definition, 8);
    return new StepMechanismDefinition(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        entityReferenceList(instance, definition, 2,
            "MECHANISM_DEFINITION links must contain entity references"),
        entityReferenceList(instance, definition, 3,
            "MECHANISM_DEFINITION joints must contain entity references"),
        (int) numberValue(instance, definition, 4),
        resolve(referenceId(instance, definition, 5)),
        entityReferenceList(instance, definition, 6,
            "MECHANISM_DEFINITION actuated joints must contain entity references"));
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
    StepEntityDefinition definition = definition(instance, "PRODUCT_VERSION");
    requireParameterCount(instance, definition, 6);
    return new StepProductVersion(
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

  StepStructuralFeature resolveStructuralFeature(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "STRUCTURAL_FEATURE");
    requireParameterCount(instance, definition, 8);
    return new StepStructuralFeature(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        numberValue(instance, definition, 3),
        resolve(referenceId(instance, definition, 4)),
        entityReferenceList(instance, definition, 5,
            "STRUCTURAL_FEATURE end conditions must contain entity references"),
        entityReferenceList(instance, definition, 6,
            "STRUCTURAL_FEATURE load points must contain entity references"));
  }

  StepFillAreaWithOutline resolveFillAreaWithOutline(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FILL_AREA_WITH_OUTLINE");
    requireParameterCount(instance, definition, 3);
    return new StepFillAreaWithOutline(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "FILL_AREA_WITH_OUTLINE outlines must contain entity references"));
  }

  // Annotation resolvers
  StepAnnotationRecord resolveAnnotationRecord(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ANNOTATION_RECORD");
    requireParameterCount(instance, definition, 8);
    return new StepAnnotationRecord(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        stringValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)),
        resolve(referenceId(instance, definition, 4)),
        resolve(referenceId(instance, definition, 5)),
        stringValue(instance, definition, 6));
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
    StepEntityDefinition definition = definition(instance, "EXTERNALLY_DEFINED_HATCH_STYLE");
    requireParameterCount(instance, definition, 2);
    return new StepExternallyDefinedHatchStyle(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepExternallyDefinedTileStyle resolveExternallyDefinedTileStyle(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "EXTERNALLY_DEFINED_TILE_STYLE");
    requireParameterCount(instance, definition, 2);
    return new StepExternallyDefinedTileStyle(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepMarkingFeature resolveMarkingFeature(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MARKING_FEATURE");
    requireParameterCount(instance, definition, 7);
    return new StepMarkingFeature(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        stringValue(instance, definition, 3),
        numberValue(instance, definition, 4),
        resolve(referenceId(instance, definition, 5)));
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
    StepEntityDefinition definition = definition(instance, "ANGULAR_DIMENSION_REPRESENTATION");
    requireParameterCount(instance, definition, 6);
    return new StepAngularDimensionRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "ANGULAR_DIMENSION_REPRESENTATION items must contain entity references"),
        resolve(referenceId(instance, definition, 2)),
        optionalNumberValue(instance, definition, 3),
        resolve(referenceId(instance, definition, 4)));
  }

  StepChainDimensionRepresentation resolveChainDimensionRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CHAIN_DIMENSION_REPRESENTATION");
    requireParameterCount(instance, definition, 5);
    return new StepChainDimensionRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "CHAIN_DIMENSION_REPRESENTATION items must contain entity references"),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  StepLinearDimensionRepresentation resolveLinearDimensionRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LINEAR_DIMENSION_REPRESENTATION");
    requireParameterCount(instance, definition, 6);
    return new StepLinearDimensionRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "LINEAR_DIMENSION_REPRESENTATION items must contain entity references"),
        resolve(referenceId(instance, definition, 2)),
        optionalNumberValue(instance, definition, 3),
        resolve(referenceId(instance, definition, 4)));
  }

  StepOrdinateDimensionRepresentation resolveOrdinateDimensionRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ORDINATE_DIMENSION_REPRESENTATION");
    requireParameterCount(instance, definition, 6);
    return new StepOrdinateDimensionRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "ORDINATE_DIMENSION_REPRESENTATION items must contain entity references"),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        resolve(referenceId(instance, definition, 4)));
  }

  StepShapeDimensionRepresentationWithTolerance resolveShapeDimensionRepresentationWithTolerance(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SHAPE_DIMENSION_REPRESENTATION_WITH_TOLERANCE");
    requireParameterCount(instance, definition, 5);
    return new StepShapeDimensionRepresentationWithTolerance(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "SHAPE_DIMENSION_REPRESENTATION_WITH_TOLERANCE items must contain entity references"),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
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
    StepEntityDefinition definition = definition(instance, "COMPOSITE_SHAPE_ASPECT");
    requireParameterCount(instance, definition, 4);
    return new StepCompositeShapeAspect(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        booleanValue(instance, definition, 3));
  }

  // Product resolvers
  StepAssemblyComponentUsage resolveAssemblyComponentUsage(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ASSEMBLY_COMPONENT_USAGE");
    requireParameterCount(instance, definition, 7);
    return new StepAssemblyComponentUsage(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        (int) numberValue(instance, definition, 3),
        stringValue(instance, definition, 4),
        resolve(referenceId(instance, definition, 5)));
  }

  StepBillOfMaterials resolveBillOfMaterials(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BILL_OF_MATERIALS");
    requireParameterCount(instance, definition, 8);
    return new StepBillOfMaterials(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        entityReferenceList(instance, definition, 2,
            "BILL_OF_MATERIALS items must contain entity references"),
        intList(instance, definition, 3),
        stringValue(instance, definition, 4),
        (int) numberValue(instance, definition, 5),
        stringValue(instance, definition, 6));
  }

  StepMakeFromRelationship resolveMakeFromRelationship(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MAKE_FROM_RELATIONSHIP");
    requireParameterCount(instance, definition, 5);
    return new StepMakeFromRelationship(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  StepTextLiteralWithDraughtingCallout resolveTextLiteralWithDraughtingCallout(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TEXT_LITERAL_WITH_DRAUGHTING_CALLOUT");
    requireParameterCount(instance, definition, 4);
    return new StepTextLiteralWithDraughtingCallout(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
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
    StepEntityDefinition definition = definition(instance, "TEXT_FONT");
    requireParameterCount(instance, definition, 3);
    return new StepTextFont(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1));
  }

  StepCharacterGlyph resolveCharacterGlyph(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CHARACTER_GLYPH");
    requireParameterCount(instance, definition, 3);
    return new StepCharacterGlyph(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1));
  }

  StepCharacterGlyphOutline resolveCharacterGlyphOutline(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CHARACTER_GLYPH_OUTLINE");
    requireParameterCount(instance, definition, 4);
    return new StepCharacterGlyphOutline(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }

  StepCharacterGlyphOutlineWithCharacteristics resolveCharacterGlyphOutlineWithCharacteristics(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CHARACTER_GLYPH_OUTLINE_WITH_CHARACTERISTICS");
    requireParameterCount(instance, definition, 5);
    return new StepCharacterGlyphOutlineWithCharacteristics(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)));
  }

  StepCharacterGlyphStroke resolveCharacterGlyphStroke(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CHARACTER_GLYPH_STROKE");
    requireParameterCount(instance, definition, 4);
    return new StepCharacterGlyphStroke(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }

  StepPreDefinedSurfaceStyle resolvePreDefinedSurfaceStyle(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRE_DEFINED_SURFACE_STYLE");
    requireParameterCount(instance, definition, 3);
    return new StepPreDefinedSurfaceStyle(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1));
  }

  StepSurfaceStyleParameterLines resolveSurfaceStyleParameterLines(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_STYLE_PARAMETER_LINES");
    requireParameterCount(instance, definition, 3);
    return new StepSurfaceStyleParameterLines(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepFillAreaStyleOutline resolveFillAreaStyleOutline(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FILL_AREA_STYLE_OUTLINE");
    requireParameterCount(instance, definition, 3);
    return new StepFillAreaStyleOutline(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepFillAreaStyleTransparent resolveFillAreaStyleTransparent(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FILL_AREA_STYLE_TRANSPARENT");
    requireParameterCount(instance, definition, 3);
    return new StepFillAreaStyleTransparent(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1));
  }

  StepFillAreaStyleHatching resolveFillAreaStyleHatching(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FILL_AREA_STYLE_HATCHING");
    requireParameterCount(instance, definition, 4);
    return new StepFillAreaStyleHatching(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        numberValue(instance, definition, 2));
  }

  StepFillAreaStyleTiling resolveFillAreaStyleTiling(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FILL_AREA_STYLE_TILING");
    requireParameterCount(instance, definition, 3);
    return new StepFillAreaStyleTiling(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepCurveStyleFont resolveCurveStyleFont(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CURVE_STYLE_FONT");
    requireParameterCount(instance, definition, 3);
    return new StepCurveStyleFont(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepCurveStyleRendering resolveCurveStyleRendering(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CURVE_STYLE_RENDERING");
    requireParameterCount(instance, definition, 4);
    return new StepCurveStyleRendering(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepCurveStyleWithFont resolveCurveStyleWithFont(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CURVE_STYLE_WITH_FONT");
    requireParameterCount(instance, definition, 4);
    return new StepCurveStyleWithFont(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        numberValue(instance, definition, 2));
  }

  StepDraughtingPreDefinedTerminatorSymbol resolveDraughtingPreDefinedTerminatorSymbol(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DRAUGHTING_PRE_DEFINED_TERMINATOR_SYMBOL");
    requireParameterCount(instance, definition, 3);
    return new StepDraughtingPreDefinedTerminatorSymbol(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1));
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
    StepEntityDefinition definition = definition(instance, "PMI_GROUP");
    requireParameterCount(instance, definition, 3);
    return new StepPmiGroup(
        instance.id(),
        stringValue(instance, definition, 0),
        entityReferenceList(instance, definition, 1,
            "PMI_GROUP members must contain entity references"));
  }

  // Manufacturing resolvers
  StepFeatureElementDefinition resolveFeatureElementDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FEATURE_ELEMENT_DEFINITION");
    requireParameterCount(instance, definition, 3);
    return new StepFeatureElementDefinition(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1));
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
    StepEntityDefinition definition = definition(instance, "TEXT_FILE_REPRESENTATION");
    requireParameterCount(instance, definition, 3);
    return new StepTextFileRepresentation(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1));
  }


  StepPersonAndOrganizationAddress resolvePersonAndOrganizationAddress(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PERSON_AND_ORGANIZATION_ADDRESS");
    requireParameterCount(instance, definition, 4);
    return new StepPersonAndOrganizationAddress(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }

  StepOrganizationAddress resolveOrganizationAddress(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ORGANIZATION_ADDRESS");
    requireParameterCount(instance, definition, 4);
    return new StepOrganizationAddress(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
  }

  StepPersonAddress resolvePersonAddress(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PERSON_ADDRESS");
    requireParameterCount(instance, definition, 4);
    return new StepPersonAddress(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)));
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
    StepEntityDefinition definition = definition(instance, "GENERALIZED_DATUM");
    requireParameterCount(instance, definition, 4);
    return new StepGeneralizedDatum(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
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
    StepEntityDefinition definition = definition(instance, "COLOR_SPECIFICATION");
    requireParameterCount(instance, definition, 4);
    return new StepColorSpecification(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  StepWithDescriptiveRepresentationItem resolveWithDescriptiveRepresentationItem(
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

  StepBooleanResult resolveBooleanResult(StepEntityInstance instance) {
    return resolveBooleanResult(instance, "BOOLEAN_RESULT");
  }

  StepBooleanResult resolveBooleanResult(
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

  StepBooleanClippingResult resolveBooleanClippingResult(StepEntityInstance instance) {
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

  StepRepresentationContext resolveRepresentationContext(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "REPRESENTATION_CONTEXT");
    requireParameterCount(instance, definition, 2);
    return new StepRepresentationContext(
        instance.id(), stringValue(instance, definition, 0), stringValue(instance, definition, 1));
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
    return resolveProductContext(instance, "PRODUCT_CONTEXT");
  }

  StepProductContext resolveProductContext(
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

  StepProduct resolveProduct(StepEntityInstance instance) {
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

  StepProductRelatedProductCategory resolveProductRelatedProductCategory(
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

  StepProductCategory resolveProductCategory(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRODUCT_CATEGORY");
    requireParameterCount(instance, definition, 2);
    return new StepProductCategory(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1));
  }

  StepProductCategoryRelationship resolveProductCategoryRelationship(
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

  StepProductRelationship resolveProductRelationship(StepEntityInstance instance) {
    return resolveProductRelationship(instance, "PRODUCT_RELATIONSHIP");
  }

  StepProductRelationship resolveProductRelationship(
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

  StepProductDefinitionFormation resolveProductDefinitionFormation(
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

  StepProductDefinitionFormationRelationship
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

  StepProductDefinitionContext resolveProductDefinitionContext(
      StepEntityInstance instance) {
    return resolveProductDefinitionContext(instance, "PRODUCT_DEFINITION_CONTEXT");
  }

  StepProductDefinitionContext resolveProductDefinitionContext(
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

  StepProductDefinition resolveProductDefinition(StepEntityInstance instance) {
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

  StepProductDefinitionRelationship resolveProductDefinitionRelationship(
      StepEntityInstance instance) {
    return resolveProductDefinitionRelationship(instance, "PRODUCT_DEFINITION_RELATIONSHIP");
  }

  StepProductDefinitionRelationship resolveProductDefinitionRelationship(
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

  StepProductDefinitionRelationshipRelationship
      resolveProductDefinitionRelationshipRelationship(StepEntityInstance instance) {
    return resolveProductDefinitionRelationshipRelationship(
        instance, "PRODUCT_DEFINITION_RELATIONSHIP_RELATIONSHIP");
  }

  StepProductDefinitionRelationshipRelationship resolveProductDefinitionRelationshipRelationship(
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

  StepProductDefinitionShape resolveProductDefinitionShape(StepEntityInstance instance) {
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

  StepPropertyDefinition resolvePropertyDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PROPERTY_DEFINITION");
    requireParameterCount(instance, definition, 3);
    return new StepPropertyDefinition(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  StepPropertyDefinitionRelationship resolvePropertyDefinitionRelationship(
      StepEntityInstance instance) {
    return resolvePropertyDefinitionRelationship(instance, "PROPERTY_DEFINITION_RELATIONSHIP");
  }

  StepPropertyDefinitionRelationship resolvePropertyDefinitionRelationship(
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

  StepGeneralProperty resolveGeneralProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GENERAL_PROPERTY");
    requireParameterCount(instance, definition, 3);
    return new StepGeneralProperty(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        optionalStringValue(instance, definition, 2));
  }

  StepGeneralPropertyRelationship resolveGeneralPropertyRelationship(
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

  StepGroup resolveGroup(StepEntityInstance instance) {
    return resolveGroup(instance, "GROUP");
  }

  StepGroup resolveGroup(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 2);
    return new StepGroup(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        entityName);
  }

  StepGroupRelationship resolveGroupRelationship(StepEntityInstance instance) {
    return resolveGroupRelationship(instance, "GROUP_RELATIONSHIP");
  }

  StepGroupRelationship resolveGroupRelationship(
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

  StepGroupAssignment resolveGroupAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GROUP_ASSIGNMENT");
    requireParameterCount(instance, definition, 1);
    return new StepGroupAssignment(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepGroup.class,
            "GROUP_ASSIGNMENT assigned_group must reference GROUP"));
  }

  StepAppliedGroupAssignment resolveAppliedGroupAssignment(
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

  StepAddress resolveAddress(StepEntityInstance instance) {
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

  StepDocumentType resolveDocumentType(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DOCUMENT_TYPE");
    requireParameterCount(instance, definition, 1);
    return new StepDocumentType(instance.id(), stringValue(instance, definition, 0));
  }

  StepDocument resolveDocument(StepEntityInstance instance) {
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

  StepDocumentRelationship resolveDocumentRelationship(StepEntityInstance instance) {
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

  StepDocumentUsageConstraint resolveDocumentUsageConstraint(
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

  StepDocumentReference resolveDocumentReference(StepEntityInstance instance) {
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

  StepAppliedDocumentReference resolveAppliedDocumentReference(
      StepEntityInstance instance) {
    return resolveAppliedDocumentReference(instance, "APPLIED_DOCUMENT_REFERENCE");
  }

  StepAppliedDocumentReference resolveAppliedDocumentReference(
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

  StepPerson resolvePerson(StepEntityInstance instance) {
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

  StepOrganization resolveOrganization(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ORGANIZATION");
    requireParameterCount(instance, definition, 3);
    return new StepOrganization(
        instance.id(),
        optionalStringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        optionalStringValue(instance, definition, 2));
  }

  StepPersonAndOrganization resolvePersonAndOrganization(StepEntityInstance instance) {
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

  StepOrganizationRelationship resolveOrganizationRelationship(
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

  StepOrganizationRole resolveOrganizationRole(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ORGANIZATION_ROLE");
    requireParameterCount(instance, definition, 1);
    return new StepOrganizationRole(instance.id(), stringValue(instance, definition, 0));
  }

  StepOrganizationAssignment resolveOrganizationAssignment(StepEntityInstance instance) {
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

  StepAppliedOrganizationAssignment resolveAppliedOrganizationAssignment(
      StepEntityInstance instance) {
    return resolveAppliedOrganizationAssignment(instance, "APPLIED_ORGANIZATION_ASSIGNMENT");
  }

  StepAppliedOrganizationAssignment resolveAppliedOrganizationAssignment(
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

  StepLanguage resolveLanguage(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LANGUAGE");
    requireParameterCount(instance, definition, 1);
    return new StepLanguage(instance.id(), stringValue(instance, definition, 0));
  }

  StepEntity resolveLanguageAssignment(StepEntityInstance instance) {
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

  StepAppliedLanguageAssignment resolveAppliedLanguageAssignment(
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

  StepPersonAndOrganizationRole resolvePersonAndOrganizationRole(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PERSON_AND_ORGANIZATION_ROLE");
    requireParameterCount(instance, definition, 1);
    return new StepPersonAndOrganizationRole(instance.id(), stringValue(instance, definition, 0));
  }

  StepPersonAndOrganizationAssignment resolvePersonAndOrganizationAssignment(
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

  StepAppliedPersonAndOrganizationAssignment
      resolveAppliedPersonAndOrganizationAssignment(StepEntityInstance instance) {
    return resolveAppliedPersonAndOrganizationAssignment(
        instance, "APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT");
  }

  StepAppliedPersonAndOrganizationAssignment
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
    StepEntityDefinition definition = definition(instance, "DATE_ROLE");
    requireParameterCount(instance, definition, 1);
    return new StepDateRole(instance.id(), stringValue(instance, definition, 0));
  }

  StepDateAssignment resolveDateAssignment(StepEntityInstance instance) {
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

  StepAppliedDateAssignment resolveAppliedDateAssignment(StepEntityInstance instance) {
    return resolveAppliedDateAssignment(instance, "APPLIED_DATE_ASSIGNMENT");
  }

  StepAppliedDateAssignment resolveAppliedDateAssignment(
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

  StepDateTimeRole resolveDateTimeRole(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DATE_TIME_ROLE");
    requireParameterCount(instance, definition, 1);
    return new StepDateTimeRole(instance.id(), stringValue(instance, definition, 0));
  }

  StepDateTimeAssignment resolveDateTimeAssignment(StepEntityInstance instance) {
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

  StepAppliedDateTimeAssignment resolveAppliedDateTimeAssignment(
      StepEntityInstance instance) {
    return resolveAppliedDateTimeAssignment(instance, "APPLIED_DATE_AND_TIME_ASSIGNMENT");
  }

  StepAppliedDateTimeAssignment resolveAppliedDateTimeAssignment(
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

  StepApprovalStatus resolveApprovalStatus(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "APPROVAL_STATUS");
    requireParameterCount(instance, definition, 1);
    return new StepApprovalStatus(instance.id(), stringValue(instance, definition, 0));
  }

  StepApproval resolveApproval(StepEntityInstance instance) {
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

  StepApprovalRole resolveApprovalRole(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "APPROVAL_ROLE");
    requireParameterCount(instance, definition, 1);
    return new StepApprovalRole(instance.id(), stringValue(instance, definition, 0));
  }

  StepApprovalAssignment resolveApprovalAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "APPROVAL_ASSIGNMENT");
    requireParameterCount(instance, definition, 1);
    return new StepApprovalAssignment(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepApproval.class,
            "APPROVAL_ASSIGNMENT assigned_approval must reference APPROVAL"));
  }

  StepAppliedApprovalAssignment resolveAppliedApprovalAssignment(
      StepEntityInstance instance) {
    return resolveAppliedApprovalAssignment(instance, "APPLIED_APPROVAL_ASSIGNMENT");
  }

  StepAppliedApprovalAssignment resolveAppliedApprovalAssignment(
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

  StepApprovalPersonOrganization resolveApprovalPersonOrganization(
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

  StepApprovalDateTime resolveApprovalDateTime(StepEntityInstance instance) {
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

  StepSecurityClassificationLevel resolveSecurityClassificationLevel(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SECURITY_CLASSIFICATION_LEVEL");
    requireParameterCount(instance, definition, 1);
    return new StepSecurityClassificationLevel(instance.id(), stringValue(instance, definition, 0));
  }

  StepSecurityClassification resolveSecurityClassification(
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

  StepSecurityClassificationAssignment resolveSecurityClassificationAssignment(
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

  StepAppliedSecurityClassificationAssignment
      resolveAppliedSecurityClassificationAssignment(StepEntityInstance instance) {
    return resolveAppliedSecurityClassificationAssignment(
        instance, "APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT");
  }

  StepAppliedSecurityClassificationAssignment
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

  StepContractType resolveContractType(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONTRACT_TYPE");
    requireParameterCount(instance, definition, 1);
    return new StepContractType(instance.id(), stringValue(instance, definition, 0));
  }

  StepContract resolveContract(StepEntityInstance instance) {
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

  StepContractAssignment resolveContractAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONTRACT_ASSIGNMENT");
    requireParameterCount(instance, definition, 1);
    return new StepContractAssignment(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepContract.class,
            "CONTRACT_ASSIGNMENT assigned_contract must reference CONTRACT"));
  }

  StepAppliedContractAssignment resolveAppliedContractAssignment(
      StepEntityInstance instance) {
    return resolveAppliedContractAssignment(instance, "APPLIED_CONTRACT_ASSIGNMENT");
  }

  StepAppliedContractAssignment resolveAppliedContractAssignment(
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

  StepCertificationType resolveCertificationType(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CERTIFICATION_TYPE");
    requireParameterCount(instance, definition, 1);
    return new StepCertificationType(instance.id(), stringValue(instance, definition, 0));
  }

  StepCertification resolveCertification(StepEntityInstance instance) {
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

  StepCertificationAssignment resolveCertificationAssignment(
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

  StepAppliedCertificationAssignment resolveAppliedCertificationAssignment(
      StepEntityInstance instance) {
    return resolveAppliedCertificationAssignment(instance, "APPLIED_CERTIFICATION_ASSIGNMENT");
  }

  StepAppliedCertificationAssignment resolveAppliedCertificationAssignment(
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

  StepEffectivity resolveEffectivity(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "EFFECTIVITY");
    requireParameterCount(instance, definition, 1);
    return new StepEffectivity(instance.id(), stringValue(instance, definition, 0));
  }

  StepProductDefinitionEffectivity resolveProductDefinitionEffectivity(
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

  StepEffectivityRelationship resolveEffectivityRelationship(
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

  StepClassificationRole resolveClassificationRole(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CLASSIFICATION_ROLE");
    requireParameterCount(instance, definition, 1);
    return new StepClassificationRole(instance.id(), stringValue(instance, definition, 0));
  }

  StepClassificationAssignment resolveClassificationAssignment(
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

  StepAppliedClassificationAssignment resolveAppliedClassificationAssignment(
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

  StepIdentificationRole resolveIdentificationRole(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "IDENTIFICATION_ROLE");
    requireParameterCount(instance, definition, 1);
    return new StepIdentificationRole(instance.id(), stringValue(instance, definition, 0));
  }

  StepIdentificationAssignment resolveIdentificationAssignment(
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

  StepAppliedIdentificationAssignment resolveAppliedIdentificationAssignment(
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

  StepExternalIdentificationAssignment resolveExternalIdentificationAssignment(
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

  StepAppliedExternalIdentificationAssignment
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

  StepNameAssignment resolveNameAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "NAME_ASSIGNMENT");
    requireParameterCount(instance, definition, 1);
    return new StepNameAssignment(instance.id(), stringValue(instance, definition, 0));
  }

  StepAppliedNameAssignment resolveAppliedNameAssignment(StepEntityInstance instance) {
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
    return resolveShapeAspect(instance, "SHAPE_ASPECT");
  }

  StepShapeAspect resolveShapeAspect(StepEntityInstance instance, String entityName) {
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

  StepShapeAspectOccurrence resolveShapeAspectOccurrence(
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

  StepShapeAspectRelationship resolveShapeAspectRelationship(StepEntityInstance instance) {
    return resolveShapeAspectRelationship(instance, "SHAPE_ASPECT_RELATIONSHIP");
  }

  StepShapeAspectRelationship resolveShapeAspectRelationship(
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

  StepShapeDefinitionRepresentation resolveShapeDefinitionRepresentation(
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

  StepPropertyDefinitionRepresentation resolvePropertyDefinitionRepresentation(
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

  StepActionPropertyRepresentation resolveActionPropertyRepresentation(
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

  StepContactRatioRepresentation resolveContactRatioRepresentation(
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

  StepKinematicPropertyDefinitionRepresentation
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

  StepKinematicPropertyMechanismRepresentation
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

  StepKinematicPropertyRepresentationRelation
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

  StepKinematicPropertyTopologyRepresentation
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

  StepPlacedDatumTargetFeature resolvePlacedDatumTargetFeature(
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

  StepResourcePropertyRepresentation resolveResourcePropertyRepresentation(
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

  StepRepresentationMap resolveRepresentationMap(StepEntityInstance instance) {
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

  StepSymbolRepresentationMap resolveSymbolRepresentationMap(
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

  StepMappedItem resolveMappedItem(StepEntityInstance instance) {
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

  StepCartesianTransformationOperator resolveCartesianTransformationOperator2D(
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

  StepCartesianTransformationOperator resolveCartesianTransformationOperator3D(
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

  StepUserDefinedMarker resolveUserDefinedMarker(StepEntityInstance instance) {
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

  StepUserDefinedCurveFont resolveUserDefinedCurveFont(StepEntityInstance instance) {
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

  StepUserDefinedTerminatorSymbol resolveUserDefinedTerminatorSymbol(
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

  StepItemDefinedTransformation resolveItemDefinedTransformation(
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

  StepRepresentationRelationshipWithTransformation
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

  StepRepresentationRelationship resolveRepresentationRelationship(
      StepEntityInstance instance) {
    return resolveRepresentationRelationship(instance, "REPRESENTATION_RELATIONSHIP");
  }

  StepRepresentationRelationship resolveRepresentationRelationship(
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

  StepShapeRepresentationRelationship resolveShapeRepresentationRelationship(
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

  StepUncertaintyMeasureWithUnit resolveUncertaintyMeasureWithUnit(
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

  StepContextDependentShapeRepresentation resolveContextDependentShapeRepresentation(
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

  StepMeasureWithUnit resolveMeasureWithUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MEASURE_WITH_UNIT");
    requireParameterCount(instance, definition, 2);
    return new StepMeasureWithUnit(
        instance.id(),
        numberValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  StepTypedMeasureWithUnit resolveTypedMeasureWithUnit(
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

  StepDerivedUnitElement resolveDerivedUnitElement(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DERIVED_UNIT_ELEMENT");
    requireParameterCount(instance, definition, 2);
    return new StepDerivedUnitElement(
        instance.id(),
        resolve(referenceId(instance, definition, 0)),
        numberValue(instance, definition, 1));
  }

  StepDerivedUnit resolveDerivedUnit(StepEntityInstance instance) {
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

  StepGeometricRepresentationContext resolveGeometricRepresentationContext(
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

  StepNamedUnit resolveNamedUnit(StepEntityInstance instance) {
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

  StepDimensionalExponents resolveDimensionalExponents(StepEntityInstance instance) {
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

  StepNamedUnit resolveStandaloneUnitKind(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 0);
    return new StepNamedUnit(instance.id(), entityName);
  }

  StepContextDependentUnit resolveContextDependentUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONTEXT_DEPENDENT_UNIT");
    requireParameterCount(instance, definition, 1);
    return new StepContextDependentUnit(
        instance.id(),
        stringValue(instance, definition, 0),
        deriveUnitKind(instance));
  }

  StepConversionBasedUnit resolveConversionBasedUnit(StepEntityInstance instance) {
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

  StepConversionBasedUnitWithOffset resolveConversionBasedUnitWithOffset(
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

  StepDerivedUnit resolveStandaloneDerivedUnitKind(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 0);
    return new StepDerivedUnit(instance.id(), List.of(), entityName);
  }

  StepSiUnit resolveSiUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SI_UNIT");
    requireParameterCount(instance, definition, 2);
    String prefix = null;
    if (!isUnset(definition.parameters().get(0))) {
      prefix = enumValue(instance, definition, 0);
    }
    return new StepSiUnit(
        instance.id(), deriveUnitKind(instance), prefix, enumValue(instance, definition, 1));
  }

  StepRepresentation resolveRepresentation(
      StepEntityInstance instance, boolean shapeRepresentation) {
    String entityName = shapeRepresentation ? "SHAPE_REPRESENTATION" : "REPRESENTATION";
    return resolveRepresentation(instance, entityName, shapeRepresentation);
  }

  StepRepresentation resolveRepresentation(
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

  StepRepresentationItem resolveRepresentationItem(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "REPRESENTATION_ITEM");
    requireParameterCount(instance, definition, 1);
    return new StepRepresentationItem(instance.id(), stringValue(instance, definition, 0));
  }

  StepGeometricRepresentationItem resolveGeometricRepresentationItem(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GEOMETRIC_REPRESENTATION_ITEM");
    requireParameterCount(instance, definition, 0);
    return new StepGeometricRepresentationItem(instance.id(), inheritedRepresentationItemName(instance));
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
    StepEntityDefinition definition = definition(instance, "SURFACE");
    requireParameterCount(instance, definition, 0);
    return new StepSurface(instance.id(), inheritedRepresentationItemName(instance));
  }

  StepBoundedCurve resolveBoundedCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BOUNDED_CURVE");
    requireParameterCount(instance, definition, 0);
    return new StepBoundedCurve(instance.id(), inheritedRepresentationItemName(instance));
  }

  StepUniformCurve resolveUniformCurve(StepEntityInstance instance) {
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

  StepBezierCurve resolveBezierCurve(StepEntityInstance instance) {
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

  StepPiecewiseBezierCurve resolvePiecewiseBezierCurve(StepEntityInstance instance) {
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

  StepQuasiUniformCurve resolveQuasiUniformCurve(StepEntityInstance instance) {
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

  StepBoundedSurface resolveBoundedSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BOUNDED_SURFACE");
    requireParameterCount(instance, definition, 0);
    return new StepBoundedSurface(instance.id(), inheritedRepresentationItemName(instance));
  }

  StepUniformSurface resolveUniformSurface(StepEntityInstance instance) {
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

  StepBezierSurface resolveBezierSurface(StepEntityInstance instance) {
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

  StepPiecewiseBezierSurface resolvePiecewiseBezierSurface(StepEntityInstance instance) {
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

  StepQuasiUniformSurface resolveQuasiUniformSurface(StepEntityInstance instance) {
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
    StepEntityDefinition definition = definition(instance, "SURFACE_MODEL");
    requireParameterCount(instance, definition, 0);
    return new StepSurfaceModel(instance.id(), inheritedRepresentationItemName(instance));
  }

  StepSolidModel resolveSolidModel(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SOLID_MODEL");
    requireParameterCount(instance, definition, 0);
    return new StepSolidModel(instance.id(), inheritedRepresentationItemName(instance));
  }

  StepCsgSolid resolveCsgSolid(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CSG_SOLID");
    requireParameterCount(instance, definition, 2);
    StepEntity treeRootExpression = resolve(referenceId(instance, definition, 1));
    if (!isBooleanOperandEntity(treeRootExpression)) {
      throw new StepResolutionException(
          "CSG_SOLID tree_root_expression must reference a supported CSG operand");
    }
    return new StepCsgSolid(instance.id(), stringValue(instance, definition, 0), treeRootExpression);
  }

  StepSolidReplica resolveSolidReplica(StepEntityInstance instance) {
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

  StepCsgPrimitive resolveCsgPrimitive(
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

  StepProfileDef resolveCircleProfileDef(StepEntityInstance instance) {
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

  StepProfileDef resolveRectangleProfileDef(StepEntityInstance instance) {
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

  StepProfileDef resolveParameterizedProfileDef(
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

  StepProfileDef resolveArbitraryClosedProfileDef(StepEntityInstance instance) {
    return resolveArbitraryProfileDef(instance, "ARBITRARY_CLOSED_PROFILE_DEF");
  }

  StepProfileDef resolveArbitraryProfileDefWithVoids(StepEntityInstance instance) {
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

  StepProfileDef resolveArbitraryProfileDef(
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

  StepProfileDef resolveProfileDef(StepEntityInstance instance) {
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

  StepProfileDef resolveProfileDefSubtype(StepEntityInstance instance, StepEntityDefinition concrete) {
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

  StepSweptAreaSolid resolveExtrudedAreaSolid(StepEntityInstance instance) {
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

  StepSweptAreaSolid resolveRevolvedAreaSolid(StepEntityInstance instance) {
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

  StepHalfSpaceSolid resolveBoxedHalfSpace(StepEntityInstance instance) {
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

  StepTopologicalRepresentationItem resolveTopologicalRepresentationItem(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TOPOLOGICAL_REPRESENTATION_ITEM");
    requireParameterCount(instance, definition, 1);
    return new StepTopologicalRepresentationItem(
        instance.id(), stringValue(instance, definition, 0));
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
    StepEntityDefinition definition = definition(instance, "COLOUR_RGB");
    requireParameterCount(instance, definition, 4);
    return new StepColourRgb(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  StepColour resolveColour(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COLOUR");
    requireParameterCount(instance, definition, 0);
    return new StepColour(instance.id());
  }

  StepColourSpecification resolveColourSpecification(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COLOUR_SPECIFICATION");
    requireParameterCount(instance, definition, 1);
    return new StepColourSpecification(instance.id(), stringValue(instance, definition, 0));
  }

  StepDraughtingPreDefinedCurveFont resolveDraughtingPreDefinedCurveFont(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DRAUGHTING_PRE_DEFINED_CURVE_FONT");
    requireParameterCount(instance, definition, 1);
    return new StepDraughtingPreDefinedCurveFont(
        instance.id(), stringValue(instance, definition, 0));
  }

  StepPreDefinedCurveFont resolvePreDefinedCurveFont(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRE_DEFINED_CURVE_FONT");
    requireParameterCount(instance, definition, 1);
    return new StepPreDefinedCurveFont(instance.id(), stringValue(instance, definition, 0));
  }

  StepPreDefinedItem resolvePreDefinedItem(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRE_DEFINED_ITEM");
    requireParameterCount(instance, definition, 1);
    return new StepPreDefinedItem(instance.id(), stringValue(instance, definition, 0));
  }

  StepPreDefinedMarker resolvePreDefinedMarker(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRE_DEFINED_MARKER");
    requireParameterCount(instance, definition, 1);
    return new StepPreDefinedMarker(instance.id(), stringValue(instance, definition, 0));
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

  StepPreDefinedTerminatorSymbol resolvePreDefinedTerminatorSymbol(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRE_DEFINED_TERMINATOR_SYMBOL");
    requireParameterCount(instance, definition, 1);
    return new StepPreDefinedTerminatorSymbol(instance.id(), stringValue(instance, definition, 0));
  }

  StepPreDefinedSurfaceSideStyle resolvePreDefinedSurfaceSideStyle(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRE_DEFINED_SURFACE_SIDE_STYLE");
    requireParameterCount(instance, definition, 1);
    return new StepPreDefinedSurfaceSideStyle(instance.id(), stringValue(instance, definition, 0));
  }

  StepDraughtingPreDefinedTextFont resolveDraughtingPreDefinedTextFont(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DRAUGHTING_PRE_DEFINED_TEXT_FONT");
    requireParameterCount(instance, definition, 1);
    return new StepDraughtingPreDefinedTextFont(instance.id(), stringValue(instance, definition, 0));
  }

  StepPreDefinedTextFont resolvePreDefinedTextFont(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRE_DEFINED_TEXT_FONT");
    requireParameterCount(instance, definition, 1);
    return new StepPreDefinedTextFont(instance.id(), stringValue(instance, definition, 0));
  }

  StepDraughtingPreDefinedColour resolveDraughtingPreDefinedColour(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DRAUGHTING_PRE_DEFINED_COLOUR");
    requireParameterCount(instance, definition, 1);
    return new StepDraughtingPreDefinedColour(instance.id(), stringValue(instance, definition, 0));
  }

  StepPreDefinedColour resolvePreDefinedColour(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRE_DEFINED_COLOUR");
    requireParameterCount(instance, definition, 1);
    return new StepPreDefinedColour(instance.id(), stringValue(instance, definition, 0));
  }

  StepCurveStyle resolveCurveStyle(StepEntityInstance instance) {
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

  StepPointStyle resolvePointStyle(StepEntityInstance instance) {
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

  StepCharacterGlyphStyleStroke resolveCharacterGlyphStyleStroke(
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

  StepCharacterGlyphStyleOutline resolveCharacterGlyphStyleOutline(
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

  StepCharacterGlyphStyleOutlineWithCharacteristics
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

  StepTextStyleForDefinedFont resolveTextStyleForDefinedFont(StepEntityInstance instance) {
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

  StepTextStyle resolveTextStyle(StepEntityInstance instance) {
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

  StepTextStyleWithSpacing resolveTextStyleWithSpacing(StepEntityInstance instance) {
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

  StepSymbolStyle resolveSymbolStyle(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SYMBOL_STYLE");
    requireParameterCount(instance, definition, 2);
    StepEntity styleOfSymbol = resolve(referenceId(instance, definition, 1));
    if (!(styleOfSymbol instanceof StepSymbolColour)) {
      throw new UnsupportedStepEntityException(
          "SYMBOL_STYLE style_of_symbol must reference SYMBOL_COLOUR");
    }
    return new StepSymbolStyle(instance.id(), stringValue(instance, definition, 0), styleOfSymbol);
  }

  StepFillAreaStyleColour resolveFillAreaStyleColour(StepEntityInstance instance) {
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

  StepFillAreaStyle resolveFillAreaStyle(StepEntityInstance instance) {
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

  StepSurfaceStyleFillArea resolveSurfaceStyleFillArea(StepEntityInstance instance) {
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

  StepSurfaceStyleBoundary resolveSurfaceStyleBoundary(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_STYLE_BOUNDARY");
    requireParameterCount(instance, definition, 1);
    return new StepSurfaceStyleBoundary(
        instance.id(), requireCurveStyleReference(instance, definition, "SURFACE_STYLE_BOUNDARY"));
  }

  StepSurfaceStyleControlGrid resolveSurfaceStyleControlGrid(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_STYLE_CONTROL_GRID");
    requireParameterCount(instance, definition, 1);
    return new StepSurfaceStyleControlGrid(
        instance.id(),
        requireCurveStyleReference(instance, definition, "SURFACE_STYLE_CONTROL_GRID"));
  }

  StepSurfaceStyleSegmentationCurve resolveSurfaceStyleSegmentationCurve(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_STYLE_SEGMENTATION_CURVE");
    requireParameterCount(instance, definition, 1);
    return new StepSurfaceStyleSegmentationCurve(
        instance.id(),
        requireCurveStyleReference(instance, definition, "SURFACE_STYLE_SEGMENTATION_CURVE"));
  }

  StepSurfaceStyleSilhouette resolveSurfaceStyleSilhouette(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_STYLE_SILHOUETTE");
    requireParameterCount(instance, definition, 1);
    return new StepSurfaceStyleSilhouette(
        instance.id(), requireCurveStyleReference(instance, definition, "SURFACE_STYLE_SILHOUETTE"));
  }

  StepSurfaceStyleTransparent resolveSurfaceStyleTransparent(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_STYLE_TRANSPARENT");
    requireParameterCount(instance, definition, 1);
    return new StepSurfaceStyleTransparent(instance.id(), numberValue(instance, definition, 0));
  }

  StepSurfaceStyleReflectanceAmbient resolveSurfaceStyleReflectanceAmbient(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_STYLE_REFLECTANCE_AMBIENT");
    requireParameterCount(instance, definition, 1);
    return new StepSurfaceStyleReflectanceAmbient(instance.id(), numberValue(instance, definition, 0));
  }

  StepSurfaceStyleReflectanceAmbientDiffuse resolveSurfaceStyleReflectanceAmbientDiffuse(
      StepEntityInstance instance) {
    StepEntityDefinition definition =
        definition(instance, "SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE");
    requireParameterCount(instance, definition, 2);
    return new StepSurfaceStyleReflectanceAmbientDiffuse(
        instance.id(),
        numberValue(instance, definition, 0),
        numberValue(instance, definition, 1));
  }

  StepSurfaceStyleReflectanceAmbientDiffuseSpecular
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

  StepSurfaceStyleParameterLine resolveSurfaceStyleParameterLine(
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

  StepSurfaceSideStyle resolveSurfaceSideStyle(StepEntityInstance instance) {
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

  StepSurfaceStyleUsage resolveSurfaceStyleUsage(StepEntityInstance instance) {
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

  StepOverRidingStyledItem resolveOverRidingStyledItem(StepEntityInstance instance) {
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

  StepAnnotationTextOccurrence resolveAnnotationTextOccurrence(
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

  StepAnnotationText resolveAnnotationText(StepEntityInstance instance) {
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

  StepAnnotationTextCharacter resolveAnnotationTextCharacter(
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

  StepAnnotationSymbol resolveAnnotationSymbol(StepEntityInstance instance) {
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

  StepAnnotationSymbolOccurrence resolveAnnotationSymbolOccurrence(
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

  StepAnnotationSubfigureOccurrence resolveAnnotationSubfigureOccurrence(
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

  StepDraughtingAnnotationOccurrence resolveDraughtingAnnotationOccurrence(
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

  StepTerminatorSymbol resolveTerminatorSymbol(
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

  StepAnnotationOccurrenceRelationship resolveAnnotationOccurrenceRelationship(
      StepEntityInstance instance) {
    return resolveAnnotationOccurrenceRelationship(instance, "ANNOTATION_OCCURRENCE_RELATIONSHIP");
  }

  StepAnnotationOccurrenceRelationship resolveAnnotationOccurrenceRelationship(
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

  StepAnnotationPointOccurrence resolveAnnotationPointOccurrence(
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

  StepAnnotationCurveOccurrence resolveAnnotationCurveOccurrence(
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

  StepLeaderCurve resolveLeaderCurve(StepEntityInstance instance) {
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

  StepProjectionCurve resolveProjectionCurve(StepEntityInstance instance) {
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

  StepDimensionCurve resolveDimensionCurve(StepEntityInstance instance) {
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

  StepAnnotationFillArea resolveAnnotationFillArea(StepEntityInstance instance) {
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

  StepAnnotationFillAreaOccurrence resolveAnnotationFillAreaOccurrence(
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

  StepAnnotationPlaceholderOccurrence resolveAnnotationPlaceholderOccurrence(
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

  StepAnnotationPlane resolveAnnotationPlane(StepEntityInstance instance) {
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

  StepSurfaceOfConstantRadius resolveSurfaceOfConstantRadius(StepEntityInstance instance) {
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

  StepDegenerateCurve resolveDegenerateCurve(StepEntityInstance instance) {
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

  StepCircle2D resolveCircle2D(StepEntityInstance instance) {
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

  StepEllipse2D resolveEllipse2D(StepEntityInstance instance) {
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

  StepPolyline2D resolvePolyline2D(StepEntityInstance instance) {
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

  StepRationalBSplineCurve2D resolveRationalBSplineCurve2D(StepEntityInstance instance) {
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

  StepBezierCurve2D resolveBezierCurve2D(StepEntityInstance instance) {
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

  StepQuasiUniformCurve2D resolveQuasiUniformCurve2D(StepEntityInstance instance) {
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

  StepUniformCurve2D resolveUniformCurve2D(StepEntityInstance instance) {
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

  StepDegenerateCurve2D resolveDegenerateCurve2D(StepEntityInstance instance) {
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

  StepSweptAreaSolid resolveSweptAreaSolid(StepEntityInstance instance, String entityName) {
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

  StepMachinedSurface resolveMachinedSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MACHINED_SURFACE");
    requireParameterCount(instance, definition, 2);
    int faceId = referenceId(instance, definition, 1);
    return new StepMachinedSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(faceId));
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

  StepSurfacePatch resolveSurfacePatch(StepEntityInstance instance) {
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

  StepCompositeCurveOnSurface3D resolveCompositeCurveOnSurface3D(StepEntityInstance instance) {
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

  StepPolygonalBoundedHalfSpace resolvePolygonalBoundedHalfSpace(StepEntityInstance instance) {
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

  StepCentreLineArcProfileDef resolveCentreLineArcProfileDef(StepEntityInstance instance) {
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

  StepSweptDiskSolid resolveSweptDiskSolid(StepEntityInstance instance) {
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

  StepRuledSurface resolveRuledSurface(StepEntityInstance instance) {
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

  StepCenteredCircleProfileDef resolveCenteredCircleProfileDef(StepEntityInstance instance) {
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

  StepRevolvedAreaSolidTapered resolveRevolvedAreaSolidTapered(StepEntityInstance instance) {
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

  StepExtrudedAreaSolidTapered resolveExtrudedAreaSolidTapered(StepEntityInstance instance) {
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

  StepSurfaceCurveSweptAreaSolid resolveSurfaceCurveSweptAreaSolid(StepEntityInstance instance) {
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

  StepCylinderVolume resolveCylinderVolume(StepEntityInstance instance) {
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

  StepRightCircularConeVolume resolveRightCircularConeVolume(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "RIGHT_CIRCULAR_CONE_VOLUME");
    requireParameterCount(instance, definition, 6);
    return new StepRightCircularConeVolume(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "RIGHT_CIRCULAR_CONE_VOLUME position must reference AXIS2_PLACEMENT_3D"),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3),
        numberValue(instance, definition, 4));
  }

  StepSphereVolume resolveSphereVolume(StepEntityInstance instance) {
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

  StepTorusVolume resolveTorusVolume(StepEntityInstance instance) {
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

  StepPrismVolume resolvePrismVolume(StepEntityInstance instance) {
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

  StepExtrudedFaceSolid resolveExtrudedFaceSolid(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "EXTRUDED_FACE_SOLID");
    requireParameterCount(instance, definition, 5);
    return new StepExtrudedFaceSolid(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        numberValue(instance, definition, 4));
  }

  StepRevolvedFaceSolid resolveRevolvedFaceSolid(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "REVOLVED_FACE_SOLID");
    requireParameterCount(instance, definition, 5);
    return new StepRevolvedFaceSolid(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        resolve(referenceId(instance, definition, 3)),
        numberValue(instance, definition, 4));
  }

  StepSweptFaceSolid resolveSweptFaceSolid(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 4);
    return new StepSweptFaceSolid(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        resolve(referenceId(instance, definition, 2)),
        entityName);
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
    StepEntityDefinition definition = definition(instance, "TESSELLATED_FACE");
    requireParameterCount(instance, definition, 2);
    List<StepEntity> triangles = entityReferenceList(
        instance, definition, 1, "TESSELLATED_FACE triangles must contain entity references");
    return new StepTessellatedFace(
        instance.id(),
        stringValue(instance, definition, 0),
        triangles);
  }

  StepTessellatedTriangle resolveTessellatedTriangle(StepEntityInstance instance) {
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

  StepFiniteElementMesh resolveFiniteElementMesh(StepEntityInstance instance) {
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
    StepEntityDefinition definition = definition(instance, "MOTION_PATH");
    requireParameterCount(instance, definition, 7);
    return new StepMotionPath(
        instance.id(),
        stringValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        stringValue(instance, definition, 2),
        resolve(referenceId(instance, definition, 3)),
        resolve(referenceId(instance, definition, 4)),
        resolve(referenceId(instance, definition, 5)),
        resolve(referenceId(instance, definition, 6)));
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
    return resolveDraughtingCallout(instance, "DRAUGHTING_CALLOUT");
  }

  StepDraughtingCallout resolveDraughtingCallout(
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

  StepDraughtingCalloutRelationship resolveDraughtingCalloutRelationship(
      StepEntityInstance instance) {
    return resolveDraughtingCalloutRelationship(instance, "DRAUGHTING_CALLOUT_RELATIONSHIP");
  }

  StepDraughtingCalloutRelationship resolveDraughtingCalloutRelationship(
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

  StepMeasureRepresentationItem resolveMeasureRepresentationItem(
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

  StepDescriptiveRepresentationItem resolveDescriptiveRepresentationItem(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DESCRIPTIVE_REPRESENTATION_ITEM");
    requireParameterCount(instance, definition, 2);
    return new StepDescriptiveRepresentationItem(
        instance.id(), stringValue(instance, definition, 0), stringValue(instance, definition, 1));
  }

  StepValueRepresentationItem resolveValueRepresentationItem(
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

  StepItemIdentifiedRepresentationUsage resolveItemIdentifiedRepresentationUsage(
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

  StepChainBasedItemIdentifiedRepresentationUsage
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

  StepChainBasedGeometricItemSpecificUsage resolveChainBasedGeometricItemSpecificUsage(
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

  StepPmiRequirementItemAssociation resolvePmiRequirementItemAssociation(
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

  StepMechanicalDesignRequirementItemAssociation
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

  StepDraughtingModelItemAssociation resolveDraughtingModelItemAssociation(
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

  StepDraughtingModelItemAssociationWithPlaceholder
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

  StepGeometricItemSpecificUsage resolveGeometricItemSpecificUsage(
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
    StepEntityDefinition definition = definition(instance, "THREAD");
    requireParameterCountIn(instance, definition, 2, 5, 6);
    int paramCount = definition.parameters().size();
    if (paramCount == 2) {
      // Minimal form: name, description
      return new StepThread(
          instance.id(),
          stringValue(instance, definition, 0),
          null, null, null, null);
    }
    return new StepThread(
        instance.id(),
        stringValue(instance, definition, 0),
        paramCount > 1 ? optionalNumberValue(instance, definition, 1) : null,
        paramCount > 2 ? optionalNumberValue(instance, definition, 2) : null,
        paramCount > 3 ? stringValue(instance, definition, 3) : null,
        paramCount > 4 ? optionalNumberValue(instance, definition, 4) : null);
  }

  StepShapeAspectOccurrence resolveShapeAspectOccurrence(StepEntityInstance instance) {
    String entityName = instance.name();
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 5);
    return new StepShapeAspectOccurrence(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        requireEntity(referenceId(instance, definition, 2), com.minicad.step.model.product.StepProductDefinitionShape.class,
            "SHAPE_ASPECT_OCCURRENCE ofShape must reference PRODUCT_DEFINITION_SHAPE"),
        logicalValue(instance, definition, 3),
        resolve(referenceId(instance, definition, 4)),
        entityName);
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

  private StepEntity tryResolveReference(StepValue value) {
    value = unwrapTyped(value);
    if (value instanceof StepValue.OmittedValue || value instanceof StepValue.NotProvidedValue) {
      return null;
    }
    if (value instanceof StepValue.ReferenceValue referenceValue) {
      return resolve(referenceValue.id());
    }
    throw new StepResolutionException("parameter must be a reference or omit/not-provided");
  }

  private List<Double> coordinateTriple(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    return coordinateList(instance, definition, index, 3, 3);
  }

  private List<Double> doubleList(StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue listValue)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a list");
    }
    return listValue.elements().stream()
        .map(v -> numberValueFrom(v, definition, index))
        .toList();
  }

  private double numberValueFrom(StepValue value, StepEntityDefinition definition, int index) {
    value = unwrapTyped(value);
    if (!(value instanceof StepValue.NumberValue numberValue)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a number");
    }
    return numberValue.value();
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

  private List<Integer> intList(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue listValue)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a list");
    }
    List<Integer> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.NumberValue numberValue)) {
        throw new StepResolutionException(
            definition.name() + " integer list must contain only numbers");
      }
      result.add((int) numberValue.value());
    }
    return List.copyOf(result);
  }

  private List<String> stringList(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue listValue)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a list");
    }
    List<String> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.StringValue strValue)) {
        throw new StepResolutionException(
            definition.name() + " string list must contain only strings");
      }
      result.add(strValue.value());
    }
    return List.copyOf(result);
  }

  private String logicalValue(StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.EnumValue enumValue) {
      return enumValue.value();
    }
    if (value instanceof StepValue.StringValue strValue) {
      return strValue.value();
    }
    throw new StepResolutionException(
        definition.name() + " parameter " + index + " must be a LOGICAL value (.T., .F., or .U.)");
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

  <T extends StepEntity> T requireEntity(int id, Class<T> type, String message) {
    StepEntity entity = resolve(id);
    if (!type.isInstance(entity)) {
      throw new StepResolutionException(message + " but got " + entity.getClass().getSimpleName());
    }
    return type.cast(entity);
  }

  StepEntity requireVertexLike(int id, String message) {
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
    MiscRegistry.register(registry);
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

}
