package com.minicad.step.semantic;

import java.util.Map;

/**
 * Miscellaneous registry part 4.
 */
public final class MiscellaneousRegistry4 {

  private MiscellaneousRegistry4() {}

  public static void register(Map<String, EntityFactory> registry) {
// Entity: COMPLEX_CLAUSE
      registry.put(
          "COMPLEX_CLAUSE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPLEX_CLAUSE"));

// Entity: COMPLEX_CONJUNCTIVE_CLAUSE
      registry.put(
          "COMPLEX_CONJUNCTIVE_CLAUSE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPLEX_CONJUNCTIVE_CLAUSE"));

// Entity: COMPLEX_DISJUNCTIVE_CLAUSE
      registry.put(
          "COMPLEX_DISJUNCTIVE_CLAUSE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPLEX_DISJUNCTIVE_CLAUSE"));

// Entity: COMPLEX_NUMBER_LITERAL
      registry.put(
          "COMPLEX_NUMBER_LITERAL",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPLEX_NUMBER_LITERAL"));

// Entity: COMPLEX_NUMBER_LITERAL_POLAR
      registry.put(
          "COMPLEX_NUMBER_LITERAL_POLAR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPLEX_NUMBER_LITERAL_POLAR"));

// Entity: COMPOSITE_MATERIAL_DESIGNATION
      registry.put(
          "COMPOSITE_MATERIAL_DESIGNATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COMPOSITE_MATERIAL_DESIGNATION"));

// Entity: CONDITION
      registry.put(
          "CONDITION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONDITION"));

// Entity: CONIC
      registry.put(
          "CONIC",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONIC"));

// Entity: CONSTANT_FUNCTION
      registry.put(
          "CONSTANT_FUNCTION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONSTANT_FUNCTION"));

// Entity: CONTAINING_MESSAGE
      registry.put(
          "CONTAINING_MESSAGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONTAINING_MESSAGE"));

// Entity: CONVEX_HEXAHEDRON
      registry.put(
          "CONVEX_HEXAHEDRON",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONVEX_HEXAHEDRON"));

// Entity: CURRENCY
      registry.put(
          "CURRENCY",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CURRENCY"));

// Entity: DATA_ENVIRONMENT
      registry.put(
          "DATA_ENVIRONMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_ENVIRONMENT"));

// Entity: DATA_EQUIVALENCE_INSPECTION_INSTANCE_REPORT
      registry.put(
          "DATA_EQUIVALENCE_INSPECTION_INSTANCE_REPORT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_EQUIVALENCE_INSPECTION_INSTANCE_REPORT"));

// Entity: DATA_EQUIVALENCE_INSPECTION_REPORT
      registry.put(
          "DATA_EQUIVALENCE_INSPECTION_REPORT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_EQUIVALENCE_INSPECTION_REPORT"));

// Entity: DATA_EQUIVALENCE_INSPECTION_RESULT
      registry.put(
          "DATA_EQUIVALENCE_INSPECTION_RESULT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_EQUIVALENCE_INSPECTION_RESULT"));

// Entity: DATA_EQUIVALENCE_INSPECTION_RESULT_WITH_JUDGEMENT
      registry.put(
          "DATA_EQUIVALENCE_INSPECTION_RESULT_WITH_JUDGEMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_EQUIVALENCE_INSPECTION_RESULT_WITH_JUDGEMENT"));

// Entity: DATA_EQUIVALENCE_REPORT_REQUEST
      registry.put(
          "DATA_EQUIVALENCE_REPORT_REQUEST",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_EQUIVALENCE_REPORT_REQUEST"));

// Entity: DATA_QUALITY_INSPECTION_INSTANCE_REPORT
      registry.put(
          "DATA_QUALITY_INSPECTION_INSTANCE_REPORT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_QUALITY_INSPECTION_INSTANCE_REPORT"));

// Entity: DATA_QUALITY_INSPECTION_REPORT
      registry.put(
          "DATA_QUALITY_INSPECTION_REPORT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_QUALITY_INSPECTION_REPORT"));

// Entity: DATA_QUALITY_INSPECTION_RESULT
      registry.put(
          "DATA_QUALITY_INSPECTION_RESULT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_QUALITY_INSPECTION_RESULT"));

// Entity: MATERIAL_DESIGNATION
      registry.put("MATERIAL_DESIGNATION", StepEntityResolver::resolveMaterialDesignation);

// Entity: MATERIAL_PROPERTY
      registry.put(
          "MATERIAL_PROPERTY",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));

// Entity: LAYERED_ITEM
      registry.put("LAYERED_ITEM", StepEntityResolver::resolveLayeredItem);

// Entity: COLOR_SPECIFICATION
      registry.put("COLOR_SPECIFICATION", StepEntityResolver::resolveColorSpecification);

// Entity: MACHINING_OPERATION_SEQUENCE
      registry.put("MACHINING_OPERATION_SEQUENCE", StepEntityResolver::resolveMachiningOperationSequence);

// Entity: GEOMETRIC_MEASUREMENT
      registry.put("GEOMETRIC_MEASUREMENT", StepEntityResolver::resolveGeometricMeasurement);

// Entity: MAKE_FROM_USAGE_OPTION
      registry.put("MAKE_FROM_USAGE_OPTION", StepEntityResolver::resolveMakeFromUsageOption);

// Entity: SPECIFIED_HIGHER_USAGE_OCCURRENCE
      registry.put("SPECIFIED_HIGHER_USAGE_OCCURRENCE", StepEntityResolver::resolveSpecifiedHigherUsageOccurrence);

// Entity: ALTERNATE_PRODUCT_RELATIONSHIP
      registry.put("ALTERNATE_PRODUCT_RELATIONSHIP", StepEntityResolver::resolveAlternateProductRelationship);

// Entity: DESIGN_MAKE_FROM
      registry.put("DESIGN_MAKE_FROM", StepEntityResolver::resolveDesignMakeFrom);

// Entity: RENDERING_PROPERTIES
      registry.put("RENDERING_PROPERTIES", StepEntityResolver::resolveRenderingProperties);

// Entity: LIGHT_SOURCE
      registry.put("LIGHT_SOURCE", StepEntityResolver::resolveLightSource);

// Entity: LIGHT_SOURCE_AMBIENT
      registry.put("LIGHT_SOURCE_AMBIENT", StepEntityResolver::resolveLightSourceAmbient);

// Entity: LIGHT_SOURCE_POSITIONAL
      registry.put("LIGHT_SOURCE_POSITIONAL", StepEntityResolver::resolveLightSourcePositional);

// Entity: LIGHT_SOURCE_SPOT
      registry.put("LIGHT_SOURCE_SPOT", StepEntityResolver::resolveLightSourceSpot);

// Entity: CAMERA_MODEL_D2
      registry.put("CAMERA_MODEL_D2", StepEntityResolver::resolveCameraModelD2);

// Entity: CAMERA_MODEL_D3
      registry.put("CAMERA_MODEL_D3", StepEntityResolver::resolveCameraModelD3);

// Entity: CAMERA_USAGE
      registry.put("CAMERA_USAGE", StepEntityResolver::resolveCameraUsage);

// Entity: CAMERA_IMAGE
      registry.put("CAMERA_IMAGE", StepEntityResolver::resolveCameraImage);

// Entity: PLANAR_BOX
      registry.put("PLANAR_BOX", StepEntityResolver::resolvePlanarBox);

// Entity: PLANAR_EXTENT
      registry.put("PLANAR_EXTENT", StepEntityResolver::resolvePlanarExtent);

// Entity: MOTION_CONSTRAINT
      registry.put("MOTION_CONSTRAINT", StepEntityResolver::resolveMotionConstraint);

// Entity: START_REQUEST
      registry.put("START_REQUEST", StepEntityResolver::resolveStartRequest);

// Entity: START_WORK
      registry.put("START_WORK", StepEntityResolver::resolveStartWork);

// Entity: WORK_ITEM
      registry.put("WORK_ITEM", StepEntityResolver::resolveWorkItem);

// Entity: SPECIFIC_HIGHER_USAGE_OCCURRENCE
      registry.put("SPECIFIC_HIGHER_USAGE_OCCURRENCE", StepEntityResolver::resolveSpecificHigherUsageOccurrence);

// Entity: USAGE_OCCURRENCE
      registry.put("USAGE_OCCURRENCE", StepEntityResolver::resolveUsageOccurrence);

// Entity: APPLIED_ATTRIBUTE_CLASSIFICATION
      registry.put("APPLIED_ATTRIBUTE_CLASSIFICATION", StepEntityResolver::resolveAppliedAttributeClassification);

// Entity: ATTRIBUTE_CLASSIFICATION
      registry.put("ATTRIBUTE_CLASSIFICATION", StepEntityResolver::resolveAttributeClassification);

// Entity: MODEL_DEFINITION
      registry.put("MODEL_DEFINITION", StepEntityResolver::resolveModelDefinition);

// Entity: MODEL_INSTANCE
      registry.put("MODEL_INSTANCE", StepEntityResolver::resolveModelInstance);

// Entity: SIMULATION_DEFINITION
      registry.put("SIMULATION_DEFINITION", StepEntityResolver::resolveSimulationDefinition);

// Entity: SIMULATION_INSTANCE
      registry.put("SIMULATION_INSTANCE", StepEntityResolver::resolveSimulationInstance);

// Entity: ANGULAR_SIZE
      registry.put("ANGULAR_SIZE", StepEntityResolver::resolveAngularSize);

// Entity: ACTION_DIRECTIVE
      registry.put("ACTION_DIRECTIVE", StepEntityResolver::resolveActionDirective);

// Entity: ACTION_METHOD
      registry.put("ACTION_METHOD", StepEntityResolver::resolveActionMethod);

// Entity: ACTION
      registry.put("ACTION", StepEntityResolver::resolveAction);

// Entity: ACTION_RELATIONSHIP
      registry.put("ACTION_RELATIONSHIP", StepEntityResolver::resolveActionRelationship);

// Entity: ACTION_STATUS
      registry.put("ACTION_STATUS", StepEntityResolver::resolveActionStatus);

// Entity: PRE_DEFINED_ITEM
      registry.put("PRE_DEFINED_ITEM", StepEntityResolver::resolvePreDefinedItem);

// Entity: PRE_DEFINED_MARKER
      registry.put("PRE_DEFINED_MARKER", StepEntityResolver::resolvePreDefinedMarker);

// Entity: GEOMETRIC_SET
      registry.put("GEOMETRIC_SET", StepEntityResolver::resolveGeometricSet);

// Entity: ANGULAR_LOCATION
      registry.put("ANGULAR_LOCATION", StepEntityResolver::resolveAngularLocation);

// Entity: GEOMETRIC_SET_2D
      registry.put(
          "GEOMETRIC_SET_2D",
          (resolver, instance) -> resolver.resolveGeometricSet(instance));

// Entity: GEOMETRIC_SET_3D
      registry.put(
          "GEOMETRIC_SET_3D",
          (resolver, instance) -> resolver.resolveGeometricSet(instance));

// Entity: CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE
      registry.put(
          "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE",
          StepEntityResolver::resolveChainBasedGeometricItemSpecificUsage);

// Entity: MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION
      registry.put(
          "MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION",
          StepEntityResolver::resolveMechanicalDesignRequirementItemAssociation);

// Entity: PMI_REQUIREMENT_ITEM_ASSOCIATION
      registry.put(
          "PMI_REQUIREMENT_ITEM_ASSOCIATION",
          StepEntityResolver::resolvePmiRequirementItemAssociation);

// Entity: PLACED_TARGET
      registry.put("PLACED_TARGET", StepEntityResolver::resolvePlacedTarget);

// Entity: GEOMETRIC_ITEM_SPECIFIC_USAGE
      registry.put(
          "GEOMETRIC_ITEM_SPECIFIC_USAGE", StepEntityResolver::resolveGeometricItemSpecificUsage);

// Entity: DEGENERATE_CONIC
      registry.put(
          "DEGENERATE_CONIC",
          (resolver, instance) -> resolver.resolveConicCurve(instance, "DEGENERATE_CONIC", 0));

// Entity: MATERIAL
      registry.put("MATERIAL", StepEntityResolver::resolveMaterial);

// Entity: MAKE_FROM_OPTION
      registry.put(
          "MAKE_FROM_OPTION",
          (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "MAKE_FROM_OPTION"));

// Entity: AREA_IN_SET
      registry.put(
          "AREA_IN_SET",
          (resolver, instance) -> resolver.resolveRepresentationRelationship(instance, "AREA_IN_SET"));

// Entity: ITEM_ASSOCIATED_DIMENSION
      registry.put(
          "ITEM_ASSOCIATED_DIMENSION",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "ITEM_ASSOCIATED_DIMENSION"));

// Entity: REPUBLICATION
      registry.put(
          "REPUBLICATION",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: VALUE_FORMAT
      registry.put(
          "VALUE_FORMAT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: VALUE_FORMAT_TYPE
      registry.put(
          "VALUE_FORMAT_TYPE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: GLOBAL_CLOCK
      registry.put(
          "GLOBAL_CLOCK",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: ACTION_PROPERTY
      registry.put(
          "ACTION_PROPERTY",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));

// Entity: GENERAL_PROPERTY_ASSOCINATION
      registry.put(
          "GENERAL_PROPERTY_ASSOCINATION",
          (resolver, instance) -> resolver.resolveGeneralPropertyRelationship(instance));

// Entity: GENERAL_PROPERTY_DEFINITION
      registry.put(
          "GENERAL_PROPERTY_DEFINITION",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));

// Entity: DERIVED_SHAPE_ASPECT
      registry.put(
          "DERIVED_SHAPE_ASPECT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "DERIVED_SHAPE_ASPECT"));

// Entity: APPLIED_SHAPE_ASPECT_ASSIGNMENT
      registry.put(
          "APPLIED_SHAPE_ASPECT_ASSIGNMENT",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "APPLIED_SHAPE_ASPECT_ASSIGNMENT"));

// Entity: REPLICA_GEOMETRY
      registry.put(
          "REPLICA_GEOMETRY",
          (resolver, instance) -> resolver.resolveGeometricReplica(instance, "REPLICA_GEOMETRY"));

// Entity: GEOMETRIC_REPLICA
      registry.put(
          "GEOMETRIC_REPLICA",
          (resolver, instance) -> resolver.resolveGeometricReplica(instance, "GEOMETRIC_REPLICA"));

// Entity: GEOMETRIC_SET_2D
      registry.put(
          "GEOMETRIC_SET_2D",
          (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));

// Entity: GEOMETRIC_SET_3D
      registry.put(
          "GEOMETRIC_SET_3D",
          (resolver, instance) -> resolver.resolveGeometricRepresentationItem(instance));

// Entity: PROMISSORY_USAGE_OCCURRENCE
      registry.put(
          "PROMISSORY_USAGE_OCCURRENCE",
          (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "PROMISSORY_USAGE_OCCURRENCE"));

// Entity: SPECIFIED_HIGHER_USAGE_OCCURRENCE
      registry.put(
          "SPECIFIED_HIGHER_USAGE_OCCURRENCE",
          (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "SPECIFIED_HIGHER_USAGE_OCCURRENCE"));

// Entity: COMPONENT_DEFINITION_USAGE
      registry.put(
          "COMPONENT_DEFINITION_USAGE",
          (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "COMPONENT_DEFINITION_USAGE"));

// Entity: PRODUCT_CONCEPT
      registry.put(
          "PRODUCT_CONCEPT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: PRODUCT_CONCEPT_RELATIONSHIP
      registry.put(
          "PRODUCT_CONCEPT_RELATIONSHIP",
          (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "PRODUCT_CONCEPT_RELATIONSHIP"));

// Entity: MATERIAL_DESIGNATION_CHARACTERIZATION
      registry.put(
          "MATERIAL_DESIGNATION_CHARACTERIZATION",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));

// Entity: MATERIAL_PROPERTY_DEFINITION
      registry.put(
          "MATERIAL_PROPERTY_DEFINITION",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));

// Entity: MECHANICAL_PROPERTY
      registry.put(
          "MECHANICAL_PROPERTY",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));

// Entity: THERMAL_PROPERTY
      registry.put(
          "THERMAL_PROPERTY",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));

// Entity: ELECTRICAL_PROPERTY
      registry.put(
          "ELECTRICAL_PROPERTY",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));

// Entity: OPTICAL_PROPERTY
      registry.put(
          "OPTICAL_PROPERTY",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));

// Entity: MAGNETIC_PROPERTY
      registry.put(
          "MAGNETIC_PROPERTY",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));

// Entity: ACOUSTIC_PROPERTY
      registry.put(
          "ACOUSTIC_PROPERTY",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));

// Entity: RADIATION_PROPERTY
      registry.put(
          "RADIATION_PROPERTY",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));

// Entity: CHEMICAL_PROPERTY
      registry.put(
          "CHEMICAL_PROPERTY",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));

// Entity: ENVIRONMENTAL_PROPERTY
      registry.put(
          "ENVIRONMENTAL_PROPERTY",
          (resolver, instance) -> resolver.resolvePropertyDefinition(instance));

// Entity: EXTERNAL_FILE
      registry.put(
          "EXTERNAL_FILE",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: EXTERNAL_FILE_RELATIONSHIP
      registry.put(
          "EXTERNAL_FILE_RELATIONSHIP",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: DIGITAL_FILE
      registry.put(
          "DIGITAL_FILE",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: HARDCOPY_FILE
      registry.put(
          "HARDCOPY_FILE",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: FILE_RELATIONSHIP
      registry.put(
          "FILE_RELATIONSHIP",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: ACTION_REQUEST_SOLUTION
      registry.put(
          "ACTION_REQUEST_SOLUTION",
          (resolver, instance) -> resolver.resolveAction(instance));

// Entity: ACTION_METHOD
      registry.put(
          "ACTION_METHOD",
          (resolver, instance) -> resolver.resolveAction(instance));

// Entity: ACTION_METHOD_RELATIONSHIP
      registry.put(
          "ACTION_METHOD_RELATIONSHIP",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: ACTION_RELATIONSHIP
      registry.put(
          "ACTION_RELATIONSHIP",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: ACTION_STATUS
      registry.put(
          "ACTION_STATUS",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: ACTION_ASSIGNMENT
      registry.put(
          "ACTION_ASSIGNMENT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: APPLIED_ACTION_ASSIGNMENT
      registry.put(
          "APPLIED_ACTION_ASSIGNMENT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: ACTION_REQUEST_ASSIGNMENT
      registry.put(
          "ACTION_REQUEST_ASSIGNMENT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: APPLIED_ACTION_REQUEST_ASSIGNMENT
      registry.put(
          "APPLIED_ACTION_REQUEST_ASSIGNMENT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: ACTION_METHOD_ROLE
      registry.put(
          "ACTION_METHOD_ROLE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: REQUIREMENT_ASSIGNMENT
      registry.put(
          "REQUIREMENT_ASSIGNMENT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: APPLIED_REQUIREMENT_ASSIGNMENT
      registry.put(
          "APPLIED_REQUIREMENT_ASSIGNMENT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: REQUIREMENT_VIEW_DEFINITION_RELATIONSHIP
      registry.put(
          "REQUIREMENT_VIEW_DEFINITION_RELATIONSHIP",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: REQUIREMENT_SPECIFICATION
      registry.put(
          "REQUIREMENT_SPECIFICATION",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: REQUIREMENT_DEFINITION
      registry.put(
          "REQUIREMENT_DEFINITION",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: VERIFICATION
      registry.put(
          "VERIFICATION",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: VERIFICATION_RELATIONSHIP
      registry.put(
          "VERIFICATION_RELATIONSHIP",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: PARAMETER_VALUE
      registry.put(
          "PARAMETER_VALUE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: PRODUCT_RELATED_PRODUCT_CATEGORY
      registry.put(
          "PRODUCT_RELATED_PRODUCT_CATEGORY",
          (resolver, instance) -> resolver.resolveProductRelatedProductCategory(instance));

// Entity: PRODUCT_CATEGORY_RELATIONSHIP
      registry.put(
          "PRODUCT_CATEGORY_RELATIONSHIP",
          (resolver, instance) -> resolver.resolveProductCategoryRelationship(instance));

// Entity: GEOMETRIC_MODEL
      registry.put(
          "GEOMETRIC_MODEL",
          (resolver, instance) -> resolver.resolveRepresentation(instance, "GEOMETRIC_MODEL", true));

// Entity: ITEM_DEFINED_TRANSFORMATION
      registry.put(
          "ITEM_DEFINED_TRANSFORMATION",
          (resolver, instance) -> resolver.resolveItemDefinedTransformation(instance));

// Entity: MAPPED_ITEM
      registry.put(
          "MAPPED_ITEM",
          (resolver, instance) -> resolver.resolveMappedItem(instance));

// Entity: CARTESIAN_TRANSFORMATION_OPERATOR
      registry.put("CARTESIAN_TRANSFORMATION_OPERATOR", StepEntityResolver::resolveCartesianTransformationOperator);

// Entity: CARTESIAN_TRANSFORMATION_OPERATOR_2D
      registry.put("CARTESIAN_TRANSFORMATION_OPERATOR_2D", StepEntityResolver::resolveCartesianTransformationOperator2D);

// Entity: CARTESIAN_TRANSFORMATION_OPERATOR_3D
      registry.put("CARTESIAN_TRANSFORMATION_OPERATOR_3D", StepEntityResolver::resolveCartesianTransformationOperator3D);

// Entity: ITEM_DEFINED_TRANSFORMATION
      registry.put("ITEM_DEFINED_TRANSFORMATION", StepEntityResolver::resolveItemDefinedTransformation);

// Entity: SHAPE_ASPECT_RELATIONSHIP
      registry.put("SHAPE_ASPECT_RELATIONSHIP", StepEntityResolver::resolveShapeAspectRelationship);

// Entity: AREA_PROFILE
      registry.put("AREA_PROFILE", StepEntityResolver::resolveAreaProfile);

// Entity: GENERALIZED_AREA_PROFILE
      registry.put("GENERALIZED_AREA_PROFILE", StepEntityResolver::resolveGeneralizedAreaProfile);

// Entity: USAGE_ASSOCIATION
      registry.put("USAGE_ASSOCIATION", StepEntityResolver::resolveUsageAssociation);

// Entity: BUY_FROM_USAGE_OPTION
      registry.put("BUY_FROM_USAGE_OPTION", StepEntityResolver::resolveBuyFromUsageOption);

// Entity: EXCLUSION_ASSIGNMENT
      registry.put("EXCLUSION_ASSIGNMENT", StepEntityResolver::resolveExclusionAssignment);

// Entity: DRAWING_REFERENCE
      registry.put("DRAWING_REFERENCE", StepEntityResolver::resolveDrawingReference);

// Entity: TECHNICAL_NOTE
      registry.put("TECHNICAL_NOTE", StepEntityResolver::resolveTechnicalNote);

// Entity: CHARACTER_GLYPH
      registry.put("CHARACTER_GLYPH", StepEntityResolver::resolveCharacterGlyph);

// Entity: CHARACTER_GLYPH_STROKE
      registry.put("CHARACTER_GLYPH_STROKE", StepEntityResolver::resolveCharacterGlyphStroke);

// Entity: PMI_REQUIREMENT
      registry.put("PMI_REQUIREMENT", StepEntityResolver::resolvePmiRequirement);

// Entity: PMI_GROUP
      registry.put("PMI_GROUP", StepEntityResolver::resolvePmiGroup);

// Entity: WEBS
      registry.put("WEBS", StepEntityResolver::resolveWebs);

// Entity: ATTRIBUTE_DEFINITION
      registry.put("ATTRIBUTE_DEFINITION", StepEntityResolver::resolveAttributeDefinition);

// Entity: ATTRIBUTE_INSTANCE
      registry.put("ATTRIBUTE_INSTANCE", StepEntityResolver::resolveAttributeInstance);

// Entity: COMPOSITE_SHAPE_ASPECT
      registry.put("COMPOSITE_SHAPE_ASPECT", StepEntityResolver::resolveCompositeShapeAspect);

// Entity: BILL_OF_MATERIALS
      registry.put("BILL_OF_MATERIALS", StepEntityResolver::resolveBillOfMaterials);

// Entity: MAKE_FROM_RELATIONSHIP
      registry.put("MAKE_FROM_RELATIONSHIP", StepEntityResolver::resolveMakeFromRelationship);

// Entity: CAD_MODEL_REFERENCE
      registry.put("CAD_MODEL_REFERENCE", StepEntityResolver::resolveCadModelReference);

// Entity: COMPONENT_DEFINITION
      registry.put("COMPONENT_DEFINITION", StepEntityResolver::resolveComponentDefinition);

// Entity: ENVIRONMENTAL_IMPACT
      registry.put("ENVIRONMENTAL_IMPACT", StepEntityResolver::resolveEnvironmentalImpact);

// Entity: MODULE_DEFINITION
      registry.put("MODULE_DEFINITION", StepEntityResolver::resolveModuleDefinition);

// Entity: PART_DEFINITION
      registry.put("PART_DEFINITION", StepEntityResolver::resolvePartDefinition);

// Entity: PROJECT_INFORMATION
      registry.put("PROJECT_INFORMATION", StepEntityResolver::resolveProjectInformation);

  }
}
