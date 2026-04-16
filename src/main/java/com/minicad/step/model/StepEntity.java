package com.minicad.step.model;

/**
 * Marker interface for resolved STEP semantic entities.
 */
public sealed interface StepEntity permits StepCartesianPoint, StepDirection, StepVector,
        StepAxis1Placement, StepAxis2Placement2D, StepAxis2Placement3D, StepPoint, StepCurve, StepSurface,
        StepLine, StepPlane, StepCircle, StepEllipse, StepConicCurve, StepGeometricReplica, StepPolyline, StepOffsetCurve2D, StepOffsetCurve3D, StepOrientedCurve, StepOffsetSurface, StepCompositeCurveSegment, StepCompositeCurve, StepCompositeCurveOnSurface,
        StepSurfaceCurve, StepSeamCurve, StepGeometricSet, StepPointSet, StepBoundedCurve, StepBSplineCurve, StepBSplineCurveWithKnots, StepRationalBSplineCurve, StepPiecewiseBezierCurve, StepBezierCurve, StepUniformCurve, StepQuasiUniformCurve, StepSurfaceModel, StepBoundedSurface, StepBSplineSurface, StepBSplineSurfaceWithKnots, StepRationalBSplineSurface, StepPiecewiseBezierSurface, StepBezierSurface, StepUniformSurface, StepQuasiUniformSurface, StepCylindricalSurface,
        StepConicalSurface, StepToroidalSurface, StepDegenerateToroidalSurface, StepSphericalSurface, StepSurfaceOfLinearExtrusion, StepSurfaceOfRevolution, StepRectangularTrimmedSurface, StepCurveBoundedSurface, StepOrientedSurface,
        StepTrimmedCurve, StepTopologicalRepresentationItem, StepVertex, StepEdge, StepFace, StepPath, StepOpenPath, StepOrientedPath, StepSubpath, StepVertexPoint, StepEdgeCurve, StepSubedge, StepConnectedEdgeSet, StepEdgeBasedWireframeModel,
        StepOrientedEdge, StepLoop, StepFaceBound, StepFaceEntity, StepConnectedFaceSet, StepConnectedFaceSubSet, StepOpenShell, StepSurfacedOpenShell, StepOrientedOpenShell, StepVertexShell, StepWireShell,
        StepClosedShell, StepOrientedClosedShell, StepSolidModel, StepManifoldSolidBrep, StepFacettedBrep, StepBrepWithVoids, StepBooleanResult, StepBooleanClippingResult, StepCsgPrimitive, StepCsgSolid, StepSolidReplica, StepProfileDef, StepSweptAreaSolid, StepHalfSpaceSolid, StepBoxDomain, StepRepresentationContext, StepGeometricRepresentationContext,
        StepDimensionalExponents, StepNamedUnit, StepSiUnit, StepContextDependentUnit, StepConversionBasedUnit, StepConversionBasedUnitWithOffset, StepRepresentation, StepApplicationContext, StepProductContext, StepProduct,
        StepProductRelationship, StepProductDefinitionFormation, StepProductDefinitionFormationRelationship, StepProductDefinitionContext, StepProductDefinition, StepProductDefinitionRelationship, StepProductDefinitionRelationshipRelationship, StepRepresentationMap, StepSymbolRepresentationMap,
        StepProductDefinitionShape, StepCharacterizedObject, StepShapeAspect, StepShapeAspectOccurrence, StepShapeAspectRelationship, StepShapeDefinitionRepresentation, StepMeasureWithUnit, StepTypedMeasureWithUnit,
        StepShapeRepresentationRelationship, StepNextAssemblyUsageOccurrence,
        StepContextDependentShapeRepresentation, StepMappedItem, StepCartesianTransformationOperator, StepItemDefinedTransformation,
        StepRepresentationRelationship, StepRepresentationRelationshipWithTransformation, StepUncertaintyMeasureWithUnit,
        StepGlobalUnitAssignedContext, StepGlobalUncertaintyAssignedContext, StepColour, StepColourSpecification, StepColourRgb,
        StepGroup, StepGroupRelationship,
        StepDocumentType, StepDocument, StepDocumentRelationship, StepOrganization, StepOrganizationRelationship, StepPerson, StepPersonAndOrganization,
        StepCalendarDate, StepCoordinatedUniversalTimeOffset, StepLocalTime, StepDateAndTime,
        StepApprovalStatus, StepApproval, StepApprovalRole, StepApprovalPersonOrganization, StepApprovalDateTime,
        StepPersonAndOrganizationRole, StepDateRole, StepDateAssignment, StepAppliedDateAssignment,
        StepDateTimeRole, StepDateTimeAssignment,
        StepPersonAndOrganizationAssignment, StepAppliedPersonAndOrganizationAssignment,
        StepApprovalAssignment, StepAppliedApprovalAssignment, StepAppliedDateTimeAssignment,
        StepSecurityClassificationLevel, StepSecurityClassification,
        StepSecurityClassificationAssignment, StepAppliedSecurityClassificationAssignment,
        StepDocumentReference, StepAppliedDocumentReference,
        StepContractType, StepContract, StepContractAssignment, StepAppliedContractAssignment,
        StepCertificationType, StepCertification, StepCertificationAssignment, StepAppliedCertificationAssignment,
        StepEffectivity, StepProductDefinitionEffectivity,
        StepAddress, StepGeneralProperty, StepGeneralPropertyRelationship, StepDocumentUsageConstraint,
        StepLanguage, StepLanguageAssignment, StepAppliedLanguageAssignment,
        StepExternalSourceRelationship, StepExternalIdentificationAssignment, StepAppliedExternalIdentificationAssignment,
        StepProductCategory, StepProductCategoryRelationship, StepEffectivityRelationship,
        StepClassificationRole, StepClassificationAssignment, StepAppliedClassificationAssignment,
        StepIdentificationRole, StepIdentificationAssignment, StepAppliedIdentificationAssignment,
        StepNameAssignment, StepAppliedNameAssignment,
        StepDescriptionAttribute, StepNameAttribute, StepIdAttribute,
        StepExternalSource, StepExternallyDefinedItem,
        StepOrganizationRole, StepOrganizationAssignment, StepAppliedOrganizationAssignment,
        StepGroupAssignment, StepAppliedGroupAssignment,
        StepShellBasedSurfaceModel, StepFaceBasedSurfaceModel, StepShellBasedWireframeModel,
        StepCurveStyle, StepPointStyle, StepCharacterGlyphStyleStroke, StepCharacterGlyphStyleOutline, StepCharacterGlyphStyleOutlineWithCharacteristics, StepTextStyleForDefinedFont, StepTextStyle, StepTextStyleWithSpacing, StepTextStyleWithJustification, StepTextStyleWithMirror, StepTextStyleWithBoxCharacteristics, StepSymbolColour, StepSymbolStyle, StepPreDefinedItem, StepPreDefinedMarker, StepPreDefinedSymbol, StepPreDefinedPointMarkerSymbol, StepPreDefinedDimensionSymbol, StepPreDefinedGeometricalToleranceSymbol, StepPreDefinedTerminatorSymbol, StepPreDefinedSurfaceSideStyle, StepPreDefinedTextFont, StepDraughtingPreDefinedTextFont, StepPreDefinedCurveFont, StepDraughtingPreDefinedCurveFont, StepUserDefinedCurveFont, StepPreDefinedColour, StepDraughtingPreDefinedColour, StepUserDefinedMarker, StepUserDefinedTerminatorSymbol,
        StepFillAreaStyleColour, StepFillAreaStyle, StepSurfaceStyleFillArea, StepSurfaceStyleBoundary, StepSurfaceStyleControlGrid, StepSurfaceStyleSegmentationCurve, StepSurfaceStyleSilhouette, StepSurfaceStyleTransparent, StepSurfaceStyleReflectanceAmbient, StepSurfaceStyleReflectanceAmbientDiffuse, StepSurfaceStyleReflectanceAmbientDiffuseSpecular, StepSurfaceStyleParameterLine, StepSurfaceSideStyle,
        StepSurfaceStyleUsage, StepPresentationStyleAssignment, StepStyledItem, StepOverRidingStyledItem,
        StepPresentationLayerAssignment,
        StepAnnotationFillArea, StepAnnotationFillAreaOccurrence, StepAnnotationPlaceholderOccurrence, StepAnnotationPlane, StepAnnotationPointOccurrence, StepAnnotationCurveOccurrence, StepAnnotationSymbol, StepAnnotationSymbolOccurrence, StepAnnotationSubfigureOccurrence, StepDraughtingAnnotationOccurrence, StepTerminatorSymbol, StepAnnotationOccurrenceRelationship, StepLeaderCurve, StepProjectionCurve, StepDimensionCurve, StepAnnotationText, StepAnnotationTextCharacter, StepAnnotationTextOccurrence, StepGeometricCurveSet, StepDraughtingCallout, StepDraughtingCalloutRelationship, StepMeasureRepresentationItem, StepValueRepresentationItem,
        StepItemIdentifiedRepresentationUsage, StepChainBasedItemIdentifiedRepresentationUsage, StepChainBasedGeometricItemSpecificUsage, StepPmiRequirementItemAssociation, StepMechanicalDesignRequirementItemAssociation, StepPlacedTarget, StepDraughtingModelItemAssociation, StepDraughtingModelItemAssociationWithPlaceholder, StepGeometricItemSpecificUsage, StepPcurve, StepDegeneratePcurve, StepRepresentationItem, StepGeometricRepresentationItem,
        StepDescriptiveRepresentationItem, StepPropertyDefinition, StepPropertyDefinitionRelationship,
        StepPropertyDefinitionRepresentation, StepAbstractVariable, StepRowVariable, StepScalarVariable,
        StepAttributeAssertion, StepForwardChainingRulePremise, StepBackChainingRuleBody, StepActionPropertyRepresentation,
        StepContactRatioRepresentation, StepKinematicPropertyDefinitionRepresentation,
        StepKinematicPropertyMechanismRepresentation, StepKinematicPropertyRepresentationRelation,
        StepKinematicPropertyTopologyRepresentation, StepPlacedDatumTargetFeature,
        StepResourcePropertyRepresentation, StepDerivedUnitElement, StepDerivedUnit,
        StepApplicationProtocolDefinition, StepProductRelatedProductCategory, StepGeometricSurfaceSet,
        StepClothoid, StepIndexedPolyCurve, StepSurfaceOfConstantRadius, StepDegenerateCurve,
        StepEdgeWire, StepRectangularCompositeSurface, StepSurfacePatch,
        StepCompositeCurveOnSurface3D, StepLineSegment, StepOffsetSurface2,
        StepPolygonalBoundedHalfSpace, StepSubface,
        StepOrientedSubface, StepRectangleHollowProfileDef, StepCentreLineArcProfileDef,
        StepSweptDiskSolid, StepRuledSurface,
        StepCenteredCircleProfileDef, StepRevolvedAreaSolidTapered, StepExtrudedAreaSolidTapered,
        StepSurfaceCurveSweptAreaSolid, StepAdvancedBrep, StepComplexClippingResult,
        StepCompositeText, StepTextLiteral, StepTessellatedFaceSet, StepComposedText,
        StepNonManifoldSolidBrep,
        StepManifoldSurfaceModel, StepSurfacedEdgeCurve,
        StepGeometricTolerance, StepToleranceZoneForm, StepToleranceZone,
        StepConfigurationItem, StepDirectedDimensionalSize,
        StepConfigurationEffectivity, StepFeatureControlFrame,
        StepRunoutToleranceZone, StepMaterialDesignation,
        StepLayeredItem, StepColorSpecification, StepWithDescriptiveRepresentationItem,
        StepDatum, StepDatumFeature, StepDatumReference, StepDatumTarget,
        StepDimensionalSize, StepDimensionalLocation, StepShapeDimensionRepresentation,
        StepPlusMinusTolerance, StepToleranceValue, StepMeasureRepresentationItemWithUnit,
        StepMeasureQualification,
        StepCsgVolume, StepBlockVolume,
        StepAngularLocation, StepAngularSize,
        StepDatumReferenceCompartment, StepDatumReferenceElement, StepCommonDatum,
        StepCompositeShapeAspect,
        StepDesignedPartDesignVersion, StepMakeFromRelationship,
        StepExternallyDefinedHatchStyle, StepExternallyDefinedTileStyle,
        StepMakeFromFeature, StepMakeFromUsageOption, StepQuantifiedAssemblyComponentUsage,
        StepSpecifiedHigherUsageOccurrence, StepAlternateProductRelationship,
        StepProductDefinitionWithAssociatedDocuments, StepShapeAspectShapeRepresentation,
        StepMakeFromBuildAssembly, StepAssemblyComponentRelationship, StepDesignMakeFrom,
        StepInterpolatedConfigurationSegment, StepRangeDimensionalSize,
        StepSurfaceStyleRendering, StepSurfaceStyleRenderingWithProperties, StepRenderingProperties,
        StepLightSource, StepLightSourceAmbient, StepLightSourceDirectional,
        StepLightSourcePositional, StepLightSourceSpot,
        StepPresentationLayerUsage, StepCameraModelD2, StepCameraModelD3,
        StepCameraUsage, StepCameraImage, StepPlanarBox, StepPlanarExtent, StepViewVolume,
        StepMechanicalDesignShapeRepresentation,
        StepKinematicPair, StepKinematicJoint, StepKinematicLink, StepKinematicStructure,
        StepPersonAndOrganizationAddress, StepOrganizationAddress, StepPersonAddress,
        StepGeneralizedDatum,
        StepActionDirective, StepActionMethod, StepAction,
        StepActionRelationship, StepActionStatus,
        // Manufacturing feature entities
        StepMachiningOperation, StepMachinedSurface, StepTwo5DManufacturingFeature,
        StepManufacturingFeatureRepresentation, StepSlot, StepStud, StepProtrusion,
        StepDepression, StepRound, StepChamfer, StepHole, StepBore, StepThread,
        StepCounterboreHole, StepCountersinkHole, StepStep, StepPocket, StepGroove,
        StepRib, StepCutout, StepMarking, StepPattern, StepFeaturePattern,
        StepLinearPattern, StepCircularPattern,
        // Extended geometry entities
        StepBSplineCurveWithKnotsAndBreakpoints, StepBSplineSurfaceWithKnotsAndBreakpoints,
        StepToroidalSurfaceWithCylindricalAxis, StepToroidalSurfaceWithEllipticalAxis,
        StepCylindricalSurfaceWithEllipticalAxis, StepConicalSurfaceWithEllipticalAxis,
        StepSphericalSurfaceWithEllipticalAxis,
        // Extended tolerance zone entities
        StepLinearToleranceZone, StepRadialToleranceZone, StepRectangularToleranceZone, StepCurvedToleranceZone,
        // Manufacturing feature definition entities
        StepCounterboreHoleDefinition, StepCountersinkHoleDefinition, StepRoundHoleDefinition,
        StepSpotfaceHoleDefinition, StepSlotDefinition, StepPocketDefinition,
        StepChamferDefinition, StepFilletDefinition, StepGrooveDefinition,
        StepRibDefinition, StepThreadDefinition, StepStepDefinition,
        // Process and material representation entities
        StepSurfaceTextureRepresentationItem, StepHardnessRepresentationItem,
        StepCoatingRepresentationItem, StepHeatTreatmentRepresentationItem,
        StepProcessPlanRepresentation, StepProcessStepRepresentation,
        // Additional tolerance and dimension entities
        StepGeometricToleranceWithDatumReference, StepPlusMinusToleranceWithModifiers,
        StepTolerancePair, StepDatumSystemReference, StepCompositeDatumReference,
        StepProjectedZoneDefinition, StepShapeDimensionRepresentationWithTolerance,
        StepAngularDimensionRepresentation, StepLinearDimensionRepresentation,
        StepChainDimensionRepresentation, StepOrdinateDimensionRepresentation,
        StepMaterialPropertyRepresentation,
        // Process planning entities
        StepMachiningProcessPlan, StepAssemblyProcessPlan, StepMachiningWorkPlan,
        StepMachiningOperationSequence, StepMachiningSetup, StepMachiningToolpath,
        StepToolpathSpeedProfile, StepMachiningFeatureRelationship,
        // Product and assembly entities
        StepProductVersion, StepPartDefinition, StepAssemblyStructure, StepAssemblyComponentUsage,
        // Tolerance and inspection entities
        StepToleranceSet, StepDatumSystem, StepInspectionResult, StepInspectionCriteria,
        StepToleranceModifier,
        // Surface quality and material entities
        StepSurfaceQuality, StepCoatingSpecification, StepHeatTreatmentSpecification,
        StepMaterialSpecification,
        // Complex and modified feature entities
        StepComplexFeature, StepFeaturePatternInstance, StepModifyFeature,
        // Geometry entities
        StepFreeFormSurface, StepBlendedSurface, StepFilletEdge, StepChamferEdge,
        // Mechanical feature entities
        StepThreadFeature, StepKeywayFeature, StepWeldFeature, StepGearFeature,
        StepSpringFeature, StepBearingFeature, StepFastenerFeature, StepSealFeature,
        StepShaftFeature, StepHousingFeature,
        // Piping and fluid system entities
        StepPipeFeature, StepFlangeFeature, StepFittingFeature, StepValveFeature,
        // Sheet metal entities
        StepSheetMetalFeature, StepSheetMetalBend, StepFlatPattern,
        // Structural entities
        StepStructuralFeature,
        // Analysis and simulation entities
        StepAnalysisModel, StepFiniteElementMesh, StepBoundaryCondition, StepLoadCase,
        StepAnalysisResult, StepOptimizationCriteria, StepOptimizationResult, StepSimulationModel,
        // Kinematic and motion entities
        StepKinematicAnalysis, StepMotionPath, StepJointConfiguration, StepMechanismDefinition,
        // CNC and manufacturing entities
        StepCncProgram, StepCuttingTool, StepToolAssembly, StepWorkpiece,
        // Manufacturing process entities
        StepMoldFeature, StepDieFeature, StepCastingFeature, StepForgingFeature,
        StepWeldJoint, StepWeldProcess, StepHeatAffectedZone, StepFinishingFeature,
        StepPlatingFeature, StepPaintingFeature,
        // Measurement entities
        StepMeasurementPoint, StepDimensionalMeasurement, StepGeometricMeasurement,
        StepSurfaceMeasurement, StepMaterialTest, StepQualityRecord,
        // Assembly process entities
        StepAssemblySequence, StepAssemblyOperation, StepPackagingFeature,
        StepDocumentReferenceLink,
        // Lifecycle management entities
        StepChangeRequest, StepChangeOrder, StepVersionControl, StepLifecycleStage,
        // Fixture and tooling entities
        StepFixtureFeature, StepLocatorFeature, StepClampFeature, StepJigFeature,
        // Automation entities
        StepRobotFeature, StepSensorFeature, StepActuatorFeature, StepControllerFeature,
        // System entities
        StepElectricalFeature, StepHydraulicFeature, StepPneumaticFeature,
        StepLubricationFeature, StepCoolingFeature, StepHeatingFeature,
        StepFilterFeature, StepVentilationFeature,
        // Identification and safety entities
        StepLabelFeature, StepMarkingFeature, StepSafetyFeature, StepAccessFeature,
        // Handling and logistics entities
        StepHandlingFeature, StepTransportFeature, StepStorageFeature, StepInterfaceFeature,
        // Configuration and requirement entities
        StepConfigurationManagement, StepRequirementSpecification, StepDesignValidation, StepDesignVerification,
        // Planning and estimation entities
        StepCostEstimation, StepTimeEstimation, StepResourceAllocation, StepSchedulingInformation,
        // Service and support entities
        StepMaintenanceSchedule, StepServiceInformation, StepWarrantyInformation, StepSupplierInformation,
        // Customer and order entities
        StepCustomerInformation, StepOrderInformation, StepDeliveryInformation, StepTrackingInformation,
        // Production entities
        StepInventoryInformation, StepBillOfMaterials, StepWorkInstruction, StepOperatorQualification,
        // Environmental and compliance entities
        StepEnvironmentCondition, StepSafetyRequirement, StepRegulatoryCompliance, StepEnvironmentalImpact,
        // Document and reference entities
        StepCadModelReference, StepDrawingReference, StepSpecificationDocument, StepTechnicalNote,
        // Analysis and report entities
        StepCalculationRecord, StepTestReport, StepInspectionReport, StepAuditRecord,
        // Project and team entities
        StepProjectInformation, StepTeamInformation, StepResponsibilityAssignment, StepApprovalRecord,
        // Stock and inventory entities
        StepMaterialStock, StepToolStock, StepEquipmentStock, StepPartStock,
        // Order and logistics entities
        StepPurchaseOrder, StepReceivingRecord, StepShippingRecord, StepInventoryTransaction,
        // Quality and process entities
        StepQualityPlan, StepControlPlan, StepProcessSpecification, StepCapabilityProfile,
        // Risk and action entities
        StepFailureMode, StepRiskAssessment, StepCorrectiveAction, StepPreventiveAction,
        // Training and personnel entities
        StepTrainingRecord, StepCertificationRecord, StepSkillProfile, StepPerformanceRecord,
        // Planning and forecasting entities
        StepCapacityPlan, StepProductionPlan, StepDemandForecast, StepSupplyPlan,
        // Data and analysis entities
        StepDataCollection, StepDataAnalysis, StepStatisticsRecord, StepKpiDefinition,
        // KPI and improvement entities
        StepKpiMeasurement, StepImprovementProject, StepBestPractice, StepLessonLearned,
        // Specification entities
        StepConstraintSpecification, StepInterfaceSpecification, StepFunctionSpecification, StepBehaviorSpecification,
        // Architecture entities
        StepSystemArchitecture, StepComponentDefinition, StepConnectionDefinition, StepFlowDefinition,
        // Definition entities
        StepSignalDefinition, StepParameterDefinition, StepAlgorithmDefinition, StepProtocolDefinition,
        // State machine entities
        StepStateDefinition, StepTransitionDefinition, StepEventDefinition, StepScenarioDefinition,
        // Error and notification entities
        StepErrorHandling, StepExceptionHandling, StepLoggingSpecification, StepNotificationSpecification,
        // Security entities
        StepSecuritySpecification, StepAccessControl, StepEncryptionSpecification, StepAuthenticationSpecification,
        // History entities
        StepVersionHistory, StepChangeHistory, StepApprovalHistory, StepAuditHistory,
        // Traceability and compliance entities
        StepTraceabilityRecord, StepComplianceRecord, StepCertificationRecord2, StepCalibrationRecord,
        // Maintenance and lifecycle entities
        StepMaintenanceRecord, StepRepairRecord, StepInstallationRecord, StepDecommissionRecord,
        // Change management entities
        StepConfigurationRecord, StepUpgradeRecord, StepMigrationRecord, StepBackupRecord,
        // Data management entities
        StepRestoreRecord, StepArchiveRecord, StepRetrievalRecord, StepDisposalRecord,
        // Simulation and verification entities
        StepSimulationResult, StepVerificationResult, StepValidationResult, StepTestingResult,
        // Testing entities
        StepPerformanceTestResult, StepStressTestResult, StepAcceptanceTestResult, StepIntegrationTestResult,
        // Monitoring and health entities
        StepResourceUtilization, StepSystemHealth, StepPerformanceMonitoring, StepAlertConfiguration,
        // Incident and fault entities
        StepIncidentRecord, StepOutageRecord, StepFaultRecord, StepAnomalyRecord,
        // Diagnostic and troubleshooting entities
        StepDiagnosticRecord, StepTroubleshootingRecord, StepEscalationRecord, StepResolutionRecord,
        // Workflow and task entities
        StepWorkflowDefinition, StepWorkflowInstance, StepTaskDefinition, StepTaskInstance,
        StepScheduleDefinition, StepScheduleInstance, StepMilestoneDefinition, StepMilestoneInstance,
        // Notification entities
        StepNotificationDefinition, StepNotificationInstance,
        // Location and asset entities
        StepLocationDefinition, StepLocationInstance, StepAssetDefinition, StepAssetInstance,
        // Cost and resource entities
        StepCostDefinition, StepCostInstance, StepResourceDefinition, StepResourceInstance,
        StepCapabilityDefinition, StepCapabilityInstance,
        // Interface and port entities
        StepInterfaceDefinition, StepInterfaceInstance, StepPortDefinition, StepPortInstance,
        StepConnectorDefinition, StepConnectorInstance, StepSignalInstance,
        StepDataFlowDefinition, StepDataFlowInstance,
        // Event and rule entities
        StepEventInstance, StepRuleDefinition, StepRuleInstance,
        StepConstraintDefinition, StepConstraintInstance, StepParameterInstance, StepVariableDefinition, StepVariableInstance,
        // Formula and calculation entities
        StepFormulaDefinition, StepFormulaInstance, StepCalculationDefinition, StepCalculationInstance,
        StepTransformationDefinition, StepTransformationInstance,
        StepConversionDefinition, StepConversionInstance, StepMappingDefinition, StepMappingInstance,
        // Integration and validation entities
        StepIntegrationDefinition, StepIntegrationInstance,
        StepValidationDefinition, StepValidationInstance,
        StepVerificationDefinition, StepVerificationInstance,
        StepSimulationDefinition, StepSimulationInstance,
        StepModelDefinition, StepModelInstance,
        // Case and study entities
        StepCaseDefinition, StepCaseInstance, StepStudyDefinition, StepStudyInstance,
        StepAnalysisDefinition, StepAnalysisInstance,
        StepReportDefinition, StepReportInstance, StepLogDefinition, StepLogInstance,
        // Metric and threshold entities
        StepMetricDefinition, StepMetricInstance, StepThresholdDefinition, StepThresholdInstance,
        StepTargetDefinition, StepTargetInstance,
        StepBaselineDefinition, StepBaselineInstance, StepBenchmarkDefinition, StepBenchmarkInstance,
        // Policy and procedure entities
        StepPolicyDefinition, StepPolicyInstance, StepProcedureDefinition, StepProcedureInstance,
        StepGuidelineDefinition, StepGuidelineInstance,
        StepStandardDefinition, StepStandardInstance, StepRegulationDefinition, StepRegulationInstance,
        // Authorization and role entities
        StepAuthorizationDefinition, StepAuthorizationInstance,
        StepPermissionDefinition, StepPermissionInstance,
        StepRoleDefinition, StepRoleInstance,
        StepTeamDefinition, StepTeamInstance, StepDepartmentDefinition, StepDepartmentInstance,
        // Function and service entities
        StepFunctionDefinition, StepFunctionInstance, StepServiceDefinition, StepServiceInstance,
        // Module and network entities
        StepModuleDefinition, StepModuleInstance, StepNetworkDefinition, StepNetworkInstance,
        StepNodeDefinition, StepNodeInstance,
        // Connection and link entities
        StepConnectionDefinition2, StepConnectionInstance, StepLinkDefinition, StepLinkInstance,
        StepClusterDefinition, StepClusterInstance, StepPoolDefinition, StepPoolInstance,
        StepPartitionDefinition, StepPartitionInstance,
        // Zone and domain entities
        StepZoneDefinition, StepZoneInstance, StepDomainDefinition, StepDomainInstance,
        StepEnvironmentDefinition, StepEnvironmentInstance,
        StepPlatformDefinition, StepPlatformInstance, StepFrameworkDefinition, StepFrameworkInstance,
        // Library and package entities
        StepLibraryDefinition, StepLibraryInstance, StepPackageDefinition2, StepPackageInstance,
        StepArtifactDefinition, StepArtifactInstance,
        // Configuration and attribute entities
        StepConfigurationDefinition2, StepConfigurationInstance,
        StepAttributeDefinition, StepAttributeInstance,
        // Queue and stack entities
        StepQueueDefinition, StepQueueInstance, StepStackDefinition, StepStackInstance,
        // Stream and channel entities
        StepStreamDefinition, StepStreamInstance, StepChannelDefinition, StepChannelInstance,
        // Buffer entities
        StepBufferDefinition, StepBufferInstance,
        // Message and packet entities
        StepMessageDefinition, StepMessageInstance, StepPacketDefinition, StepPacketInstance,
        // Frame and block entities
        StepFrameDefinition, StepFrameInstance, StepBlockDefinition, StepBlockInstance,
        // Record entities
        StepRecordDefinition, StepRecordInstance,
        // Storage and repository entities
        StepStorageDefinition, StepStorageInstance, StepRepositoryDefinition, StepRepositoryInstance,
        // Cache and index entities
        StepCacheDefinition, StepCacheInstance, StepIndexDefinition, StepIndexInstance,
        // Query and filter entities
        StepQueryDefinition, StepQueryInstance, StepFilterDefinition, StepFilterInstance,
        // Scheduler and job entities
        StepSchedulerDefinition, StepSchedulerInstance, StepJobDefinition, StepJobInstance,
        // Command and instruction entities
        StepCommandDefinition, StepCommandInstance, StepInstructionDefinition, StepInstructionInstance,
        // Error and exception entities
        StepErrorDefinition, StepErrorInstance, StepExceptionDefinition, StepExceptionInstance,
        // Audit and trail entities
        StepAuditDefinition, StepAuditInstance, StepTrailDefinition, StepTrailInstance,
        // Lock and sync entities
        StepLockDefinition, StepLockInstance, StepSyncDefinition, StepSyncInstance,
        // Session and transaction entities
        StepSessionDefinition, StepSessionInstance, StepTransactionDefinition, StepTransactionInstance,
        // Monitor and probe entities
        StepMonitorDefinition, StepMonitorInstance, StepProbeDefinition, StepProbeInstance,
        // Checkpoint and recovery entities
        StepCheckpointDefinition, StepCheckpointInstance, StepRecoveryDefinition, StepRecoveryInstance,
        // Backup and restore entities
        StepBackupDefinition, StepBackupInstance, StepRestoreDefinition, StepRestoreInstance,
        StepArchiveDefinition, StepArchiveInstance,
        // Pipeline and stage entities
        StepPipelineDefinition, StepPipelineInstance, StepStageDefinition, StepStageInstance,
        // Sequence and branch entities
        StepSequenceDefinition, StepSequenceInstance, StepBranchDefinition, StepBranchInstance,
        StepLoopDefinition, StepLoopInstance,
        // Handler and trigger entities
        StepHandlerDefinition, StepHandlerInstance, StepTriggerDefinition, StepTriggerInstance,
        StepCallbackDefinition, StepCallbackInstance,
        // Timer and delay entities
        StepTimerDefinition, StepTimerInstance, StepDelayDefinition, StepDelayInstance,
        StepWaitDefinition, StepWaitInstance,
        // Timeout and retry entities
        StepTimeoutDefinition2, StepTimeoutInstance, StepRetryDefinition, StepRetryInstance,
        // Cancel and pause entities
        StepCancelDefinition, StepCancelInstance, StepPauseDefinition, StepPauseInstance,
        // State machine entities
        StepTransitionInstance, StepStateMachineDefinition, StepStateMachineInstance, StepStateInstance,
        // Chain entities
        StepEventChainDefinition, StepEventChainInstance, StepActionChainDefinition, StepActionChainInstance,
        // Execution entities
        StepExecutionContext, StepExecutionTrace,
        // Log entry and log entities
        StepLogEntry, StepEventLog, StepAuditLog, StepErrorLog,
        StepPerformanceLog, StepAccessLog, StepTraceLog, StepSecurityLog,
        StepSystemLog, StepApplicationLog,
        // Record entities
        StepMetadataRecord, StepTagRecord, StepAnnotationRecord, StepCommentRecord,
        StepReviewRecord, StepSignatureRecord, StepVersionRecord,
        StepHistoryRecord, StepChangeRecord,
        // Entry entities
        StepLockRecord, StepLockEntry, StepAccessEntry, StepPermissionEntry,
        StepRoleEntry, StepAuthorizationEntry,
        // Credential and session entities
        StepCredentialRecord, StepAuthenticationRecord, StepSessionRecord,
        // Activity and operation entities
        StepActivityRecord, StepOperationRecord, StepTransactionRecord,
        StepExecutionRecord, StepTaskRecord, StepJobRecord, StepProcessRecord,
        StepWorkflowRecord, StepScheduleRecord, StepEventRecord, StepNotificationRecord,
        // Log level entry entities
        StepErrorEntry, StepWarningEntry, StepInfoEntry, StepDebugEntry,
        StepTraceEntry, StepPerformanceEntry, StepMetricEntry, StepMeasurementEntry,
        StepStatisticsEntry, StepSummaryEntry,
        // 2D curve entities
        StepCircle2D, StepEllipse2D,
        StepHyperbola2D, StepParabola2D, StepLine2D,
        StepPolyline2D, StepTrimmedCurve2D, StepBoundedCurve2D, StepCompositeCurve2D,
        StepCurve2D, StepBSplineCurve2D, StepRationalBSplineCurve2D, StepBezierCurve2D,
        StepQuasiUniformCurve2D, StepUniformCurve2D, StepPiecewiseBezierCurve2D,
        StepIndexedPolyCurve2D, StepDegenerateCurve2D,
        // Tessellation entities
        StepSeamEdge, StepTessellatedFace, StepTessellatedTriangle {

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
