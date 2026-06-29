package com.minicad.step.semantic;

import java.util.Map;

/**
 * Miscellaneous registry part 2.
 */
public final class MiscellaneousRegistry2 {

  private MiscellaneousRegistry2() {}

  public static void register(Map<String, EntityFactory> registry) {
// Entity: ACTION_ACTUAL
      registry.put(
          "ACTION_ACTUAL",
          (resolver, instance) ->
              resolver.resolveGenericActual(instance, "ACTION_ACTUAL"));

// Entity: ACTION_DIRECTIVE_RELATIONSHIP
      registry.put(
          "ACTION_DIRECTIVE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ACTION_DIRECTIVE_RELATIONSHIP"));

// Entity: ACTION_HAPPENING
      registry.put(
          "ACTION_HAPPENING",
          (resolver, instance) ->
              resolver.resolveGenericActual(instance, "ACTION_HAPPENING"));

// Entity: ACTION_METHOD_ASSIGNMENT
      registry.put(
          "ACTION_METHOD_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ACTION_METHOD_ASSIGNMENT"));

// Entity: ACTION_REQUEST_STATUS
      registry.put(
          "ACTION_REQUEST_STATUS",
          (resolver, instance) ->
              resolver.resolveGenericStatus(instance, "ACTION_REQUEST_STATUS"));

// Entity: ACTION_RESOURCE_RELATIONSHIP
      registry.put(
          "ACTION_RESOURCE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ACTION_RESOURCE_RELATIONSHIP"));

// Entity: ACTION_RESOURCE_REQUIREMENT
      registry.put(
          "ACTION_RESOURCE_REQUIREMENT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "ACTION_RESOURCE_REQUIREMENT"));

// Entity: ACTION_RESOURCE_TYPE
      registry.put(
          "ACTION_RESOURCE_TYPE",
          (resolver, instance) ->
              resolver.resolveGenericType(instance, "ACTION_RESOURCE_TYPE"));

// Entity: ADDITIVE_MANUFACTURING_BUILD_PLATE_RELATIONSHIP
      registry.put(
          "ADDITIVE_MANUFACTURING_BUILD_PLATE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ADDITIVE_MANUFACTURING_BUILD_PLATE_RELATIONSHIP"));

// Entity: ADDITIVE_MANUFACTURING_SETUP
      registry.put(
          "ADDITIVE_MANUFACTURING_SETUP",
          (resolver, instance) ->
              resolver.resolveGenericSetup(instance, "ADDITIVE_MANUFACTURING_SETUP"));

// Entity: ADDITIVE_MANUFACTURING_SETUP_RELATIONSHIP
      registry.put(
          "ADDITIVE_MANUFACTURING_SETUP_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ADDITIVE_MANUFACTURING_SETUP_RELATIONSHIP"));

// Entity: ADDITIVE_MANUFACTURING_SETUP_WORKPIECE_RELATIONSHIP
      registry.put(
          "ADDITIVE_MANUFACTURING_SETUP_WORKPIECE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ADDITIVE_MANUFACTURING_SETUP_WORKPIECE_RELATIONSHIP"));

// Entity: ADDITIVE_MANUFACTURING_SUPPORT_STRUCTURE_GEOMETRY_RELATIONSHIP
      registry.put(
          "ADDITIVE_MANUFACTURING_SUPPORT_STRUCTURE_GEOMETRY_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ADDITIVE_MANUFACTURING_SUPPORT_STRUCTURE_GEOMETRY_RELATIONSHIP"));

// Entity: ALTERNATIVE_SOLUTION_RELATIONSHIP
      registry.put(
          "ALTERNATIVE_SOLUTION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ALTERNATIVE_SOLUTION_RELATIONSHIP"));

// Entity: AP242_ASSIGNMENT_OBJECT_RELATIONSHIP
      registry.put(
          "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP"));

// Entity: APPLIED_ACTION_METHOD_ASSIGNMENT
      registry.put(
          "APPLIED_ACTION_METHOD_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_ACTION_METHOD_ASSIGNMENT"));

// Entity: APPLIED_ATTRIBUTE_CLASSIFICATION_ASSIGNMENT
      registry.put(
          "APPLIED_ATTRIBUTE_CLASSIFICATION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_ATTRIBUTE_CLASSIFICATION_ASSIGNMENT"));

// Entity: APPLIED_CLASSIFICATION_ASSIGNMENT_RELATIONSHIP
      registry.put(
          "APPLIED_CLASSIFICATION_ASSIGNMENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_CLASSIFICATION_ASSIGNMENT_RELATIONSHIP"));

// Entity: APPLIED_DIRECTED_ACTION_ASSIGNMENT
      registry.put(
          "APPLIED_DIRECTED_ACTION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_DIRECTED_ACTION_ASSIGNMENT"));

// Entity: APPLIED_EVENT_OCCURRENCE_ASSIGNMENT
      registry.put(
          "APPLIED_EVENT_OCCURRENCE_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_EVENT_OCCURRENCE_ASSIGNMENT"));

// Entity: APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT_RELATIONSHIP
      registry.put(
          "APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT_RELATIONSHIP"));

// Entity: APPLIED_LOCATION_ASSIGNMENT
      registry.put(
          "APPLIED_LOCATION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_LOCATION_ASSIGNMENT"));

// Entity: APPLIED_STATE_OBSERVED_ASSIGNMENT
      registry.put(
          "APPLIED_STATE_OBSERVED_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_STATE_OBSERVED_ASSIGNMENT"));

// Entity: APPLIED_STATE_TYPE_ASSIGNMENT
      registry.put(
          "APPLIED_STATE_TYPE_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_STATE_TYPE_ASSIGNMENT"));

// Entity: ASCRIBABLE_STATE_RELATIONSHIP
      registry.put(
          "ASCRIBABLE_STATE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ASCRIBABLE_STATE_RELATIONSHIP"));

// Entity: ASSIGNED_REQUIREMENT
      registry.put(
          "ASSIGNED_REQUIREMENT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "ASSIGNED_REQUIREMENT"));

// Entity: ASSIGNMENT_OBJECT_RELATIONSHIP
      registry.put(
          "ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ASSIGNMENT_OBJECT_RELATIONSHIP"));

// Entity: ATTRIBUTE_CLASSIFICATION_ASSIGNMENT
      registry.put(
          "ATTRIBUTE_CLASSIFICATION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ATTRIBUTE_CLASSIFICATION_ASSIGNMENT"));

// Entity: ATTRIBUTE_LANGUAGE_ASSIGNMENT
      registry.put(
          "ATTRIBUTE_LANGUAGE_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ATTRIBUTE_LANGUAGE_ASSIGNMENT"));

// Entity: ATTRIBUTE_VALUE_ASSIGNMENT
      registry.put(
          "ATTRIBUTE_VALUE_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ATTRIBUTE_VALUE_ASSIGNMENT"));

// Entity: ATTRIBUTE_VALUE_ROLE
      registry.put(
          "ATTRIBUTE_VALUE_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericRole(instance, "ATTRIBUTE_VALUE_ROLE"));

// Entity: CHARACTERISTIC_TYPE
      registry.put(
          "CHARACTERISTIC_TYPE",
          (resolver, instance) ->
              resolver.resolveGenericType(instance, "CHARACTERISTIC_TYPE"));

// Entity: CHARACTERIZED_OBJECT_RELATIONSHIP
      registry.put(
          "CHARACTERIZED_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CHARACTERIZED_OBJECT_RELATIONSHIP"));

// Entity: CLASSIFICATION_ASSIGNMENT_RELATIONSHIP
      registry.put(
          "CLASSIFICATION_ASSIGNMENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CLASSIFICATION_ASSIGNMENT_RELATIONSHIP"));

// Entity: COLLECTION_ASSIGNMENT
      registry.put(
          "COLLECTION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COLLECTION_ASSIGNMENT"));

// Entity: COLLECTION_RELATIONSHIP
      registry.put(
          "COLLECTION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "COLLECTION_RELATIONSHIP"));

// Entity: DATA_EQUIVALENCE_DEFINITION_RELATIONSHIP
      registry.put(
          "DATA_EQUIVALENCE_DEFINITION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "DATA_EQUIVALENCE_DEFINITION_RELATIONSHIP"));

// Entity: DATA_EQUIVALENCE_INSPECTION_REQUIREMENT
      registry.put(
          "DATA_EQUIVALENCE_INSPECTION_REQUIREMENT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "DATA_EQUIVALENCE_INSPECTION_REQUIREMENT"));

// Entity: DATA_QUALITY_DEFINITION_RELATIONSHIP
      registry.put(
          "DATA_QUALITY_DEFINITION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "DATA_QUALITY_DEFINITION_RELATIONSHIP"));

// Entity: DATA_QUALITY_MEASUREMENT_REQUIREMENT
      registry.put(
          "DATA_QUALITY_MEASUREMENT_REQUIREMENT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "DATA_QUALITY_MEASUREMENT_REQUIREMENT"));

// Entity: DESIGN_MAKE_FROM_RELATIONSHIP
      registry.put(
          "DESIGN_MAKE_FROM_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "DESIGN_MAKE_FROM_RELATIONSHIP"));

// Entity: DIFFERENT_COMPONENT_TYPE
      registry.put(
          "DIFFERENT_COMPONENT_TYPE",
          (resolver, instance) ->
              resolver.resolveGenericType(instance, "DIFFERENT_COMPONENT_TYPE"));

// Entity: DIRECTED_ACTION_ASSIGNMENT
      registry.put(
          "DIRECTED_ACTION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DIRECTED_ACTION_ASSIGNMENT"));

// Entity: ENVELOPE_RELATIONSHIP
      registry.put(
          "ENVELOPE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ENVELOPE_RELATIONSHIP"));

// Entity: ERRONEOUS_TOPOLOGY_AND_GEOMETRY_RELATIONSHIP
      registry.put(
          "ERRONEOUS_TOPOLOGY_AND_GEOMETRY_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ERRONEOUS_TOPOLOGY_AND_GEOMETRY_RELATIONSHIP"));

// Entity: EVENT_OCCURRENCE_ASSIGNMENT
      registry.put(
          "EVENT_OCCURRENCE_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "EVENT_OCCURRENCE_ASSIGNMENT"));

// Entity: EVENT_OCCURRENCE_RELATIONSHIP
      registry.put(
          "EVENT_OCCURRENCE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "EVENT_OCCURRENCE_RELATIONSHIP"));

// Entity: EVENT_OCCURRENCE_ROLE
      registry.put(
          "EVENT_OCCURRENCE_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericRole(instance, "EVENT_OCCURRENCE_ROLE"));

// Entity: EXTERNALLY_DEFINED_ITEM_RELATIONSHIP
      registry.put(
          "EXTERNALLY_DEFINED_ITEM_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "EXTERNALLY_DEFINED_ITEM_RELATIONSHIP"));

// Entity: EXTERNAL_IDENTIFICATION_ASSIGNMENT_RELATIONSHIP
      registry.put(
          "EXTERNAL_IDENTIFICATION_ASSIGNMENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "EXTERNAL_IDENTIFICATION_ASSIGNMENT_RELATIONSHIP"));

// Entity: FACT_TYPE
      registry.put(
          "FACT_TYPE",
          (resolver, instance) ->
              resolver.resolveGenericType(instance, "FACT_TYPE"));

// Entity: FINAL_SOLUTION
      registry.put(
          "FINAL_SOLUTION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "FINAL_SOLUTION"));

// Entity: FREE_FORM_ASSIGNMENT
      registry.put(
          "FREE_FORM_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "FREE_FORM_ASSIGNMENT"));

// Entity: FROZEN_ASSIGNMENT
      registry.put(
          "FROZEN_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "FROZEN_ASSIGNMENT"));

// Entity: GENERAL_MATERIAL_PROPERTY
      registry.put(
          "GENERAL_MATERIAL_PROPERTY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GENERAL_MATERIAL_PROPERTY"));

// Entity: GENERAL_PROPERTY_ASSOCIATION
      registry.put(
          "GENERAL_PROPERTY_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GENERAL_PROPERTY_ASSOCIATION"));

// Entity: GENERIC_PROPERTY_RELATIONSHIP
      registry.put(
          "GENERIC_PROPERTY_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "GENERIC_PROPERTY_RELATIONSHIP"));

// Entity: GLOBAL_ASSIGNMENT
      registry.put(
          "GLOBAL_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "GLOBAL_ASSIGNMENT"));

// Entity: IDENTIFICATION_ASSIGNMENT_RELATIONSHIP
      registry.put(
          "IDENTIFICATION_ASSIGNMENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "IDENTIFICATION_ASSIGNMENT_RELATIONSHIP"));

// Entity: IDRM_CLASSIFICATION_ASSIGNMENT
      registry.put(
          "IDRM_CLASSIFICATION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "IDRM_CLASSIFICATION_ASSIGNMENT"));

// Entity: IMPLICIT_EXPLICIT_POSITIONED_SKETCH_RELATIONSHIP
      registry.put(
          "IMPLICIT_EXPLICIT_POSITIONED_SKETCH_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "IMPLICIT_EXPLICIT_POSITIONED_SKETCH_RELATIONSHIP"));

// Entity: INAPT_TOPOLOGY_AND_GEOMETRY_RELATIONSHIP
      registry.put(
          "INAPT_TOPOLOGY_AND_GEOMETRY_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "INAPT_TOPOLOGY_AND_GEOMETRY_RELATIONSHIP"));

// Entity: LOCATION_ASSIGNMENT
      registry.put(
          "LOCATION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LOCATION_ASSIGNMENT"));

// Entity: LOCATION_RELATIONSHIP
      registry.put(
          "LOCATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "LOCATION_RELATIONSHIP"));

// Entity: LOCATION_ROLE
      registry.put(
          "LOCATION_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericRole(instance, "LOCATION_ROLE"));

// Entity: MESSAGE_RELATIONSHIP
      registry.put(
          "MESSAGE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "MESSAGE_RELATIONSHIP"));

// Entity: MULTI_LANGUAGE_ATTRIBUTE_ASSIGNMENT
      registry.put(
          "MULTI_LANGUAGE_ATTRIBUTE_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "MULTI_LANGUAGE_ATTRIBUTE_ASSIGNMENT"));

// Entity: OBJECT_ROLE
      registry.put(
          "OBJECT_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericRole(instance, "OBJECT_ROLE"));

// Entity: OVERCOMPLEX_TOPOLOGY_AND_GEOMETRY_RELATIONSHIP
      registry.put(
          "OVERCOMPLEX_TOPOLOGY_AND_GEOMETRY_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "OVERCOMPLEX_TOPOLOGY_AND_GEOMETRY_RELATIONSHIP"));

// Entity: PROCESS_PROPERTY_ASSOCIATION
      registry.put(
          "PROCESS_PROPERTY_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PROCESS_PROPERTY_ASSOCIATION"));

// Entity: PRODUCT_DATA_AND_DATA_EQUIVALENCE_RELATIONSHIP
      registry.put(
          "PRODUCT_DATA_AND_DATA_EQUIVALENCE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PRODUCT_DATA_AND_DATA_EQUIVALENCE_RELATIONSHIP"));

// Entity: PRODUCT_DATA_AND_DATA_QUALITY_RELATIONSHIP
      registry.put(
          "PRODUCT_DATA_AND_DATA_QUALITY_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PRODUCT_DATA_AND_DATA_QUALITY_RELATIONSHIP"));

// Entity: PRODUCT_GROUP_ATTRIBUTE_ASSIGNMENT
      registry.put(
          "PRODUCT_GROUP_ATTRIBUTE_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PRODUCT_GROUP_ATTRIBUTE_ASSIGNMENT"));

// Entity: PRODUCT_GROUP_RELATIONSHIP
      registry.put(
          "PRODUCT_GROUP_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PRODUCT_GROUP_RELATIONSHIP"));

// Entity: PRODUCT_GROUP_RULE_ASSIGNMENT
      registry.put(
          "PRODUCT_GROUP_RULE_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PRODUCT_GROUP_RULE_ASSIGNMENT"));

// Entity: PRODUCT_MATERIAL_COMPOSITION_RELATIONSHIP
      registry.put(
          "PRODUCT_MATERIAL_COMPOSITION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "PRODUCT_MATERIAL_COMPOSITION_RELATIONSHIP"));

// Entity: RESOURCE_PROPERTY
      registry.put(
          "RESOURCE_PROPERTY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "RESOURCE_PROPERTY"));

// Entity: RESOURCE_REQUIREMENT_TYPE
      registry.put(
          "RESOURCE_REQUIREMENT_TYPE",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "RESOURCE_REQUIREMENT_TYPE"));

// Entity: RULE_SUPERSEDED_ASSIGNMENT
      registry.put(
          "RULE_SUPERSEDED_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "RULE_SUPERSEDED_ASSIGNMENT"));

// Entity: SAME_AS_EXTERNAL_ITEM_ASSIGNMENT
      registry.put(
          "SAME_AS_EXTERNAL_ITEM_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "SAME_AS_EXTERNAL_ITEM_ASSIGNMENT"));

// Entity: SATISFIED_REQUIREMENT
      registry.put(
          "SATISFIED_REQUIREMENT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "SATISFIED_REQUIREMENT"));

// Entity: SATISFIES_REQUIREMENT
      registry.put(
          "SATISFIES_REQUIREMENT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "SATISFIES_REQUIREMENT"));

// Entity: SCANNER_PROPERTY
      registry.put(
          "SCANNER_PROPERTY",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SCANNER_PROPERTY"));

// Entity: SINGLE_PROPERTY_IS_DEFINITION
      registry.put(
          "SINGLE_PROPERTY_IS_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SINGLE_PROPERTY_IS_DEFINITION"));

// Entity: SOURCED_REQUIREMENT
      registry.put(
          "SOURCED_REQUIREMENT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "SOURCED_REQUIREMENT"));

// Entity: SOURCE_FOR_REQUIREMENT
      registry.put(
          "SOURCE_FOR_REQUIREMENT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "SOURCE_FOR_REQUIREMENT"));

// Entity: STATECHAR_RELATIONSHIP_OBJECT
      registry.put(
          "STATECHAR_RELATIONSHIP_OBJECT",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "STATECHAR_RELATIONSHIP_OBJECT"));

// Entity: STATECHAR_TYPE_APPLIED_OBJECT
      registry.put(
          "STATECHAR_TYPE_APPLIED_OBJECT",
          (resolver, instance) ->
              resolver.resolveGenericType(instance, "STATECHAR_TYPE_APPLIED_OBJECT"));

// Entity: STATECHAR_TYPE_OBJECT
      registry.put(
          "STATECHAR_TYPE_OBJECT",
          (resolver, instance) ->
              resolver.resolveGenericType(instance, "STATECHAR_TYPE_OBJECT"));

// Entity: STATECHAR_TYPE_RELATIONSHIP_OBJECT
      registry.put(
          "STATECHAR_TYPE_RELATIONSHIP_OBJECT",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "STATECHAR_TYPE_RELATIONSHIP_OBJECT"));

// Entity: STATE_DEFINITION_TO_STATE_ASSIGNMENT_RELATIONSHIP
      registry.put(
          "STATE_DEFINITION_TO_STATE_ASSIGNMENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "STATE_DEFINITION_TO_STATE_ASSIGNMENT_RELATIONSHIP"));

// Entity: STATE_OBSERVED_ASSIGNMENT
      registry.put(
          "STATE_OBSERVED_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "STATE_OBSERVED_ASSIGNMENT"));

// Entity: STATE_OBSERVED_RELATIONSHIP
      registry.put(
          "STATE_OBSERVED_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "STATE_OBSERVED_RELATIONSHIP"));

// Entity: STATE_OBSERVED_ROLE
      registry.put(
          "STATE_OBSERVED_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericRole(instance, "STATE_OBSERVED_ROLE"));

// Entity: STATE_TYPE
      registry.put(
          "STATE_TYPE",
          (resolver, instance) ->
              resolver.resolveGenericType(instance, "STATE_TYPE"));

// Entity: STATE_TYPE_ASSIGNMENT
      registry.put(
          "STATE_TYPE_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "STATE_TYPE_ASSIGNMENT"));

// Entity: STATE_TYPE_RELATIONSHIP
      registry.put(
          "STATE_TYPE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "STATE_TYPE_RELATIONSHIP"));

// Entity: STATE_TYPE_ROLE
      registry.put(
          "STATE_TYPE_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericType(instance, "STATE_TYPE_ROLE"));

// Entity: UNIFORM_RESOURCE_IDENTIFIER
      registry.put(
          "UNIFORM_RESOURCE_IDENTIFIER",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "UNIFORM_RESOURCE_IDENTIFIER"));

// Entity: VALUE_FORMAT_TYPE_QUALIFIER
      registry.put(
          "VALUE_FORMAT_TYPE_QUALIFIER",
          (resolver, instance) ->
              resolver.resolveGenericType(instance, "VALUE_FORMAT_TYPE_QUALIFIER"));

// Entity: AP242_ASSIGNMENT_OBJECT_RELATIONSHIP
      registry.put(
          "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "AP242_ASSIGNMENT_OBJECT_RELATIONSHIP"));

// Entity: APPLIED_PRESENTED_ITEM
      registry.put(
          "APPLIED_PRESENTED_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "APPLIED_PRESENTED_ITEM"));

// Entity: APPLIED_USAGE_RIGHT
      registry.put(
          "APPLIED_USAGE_RIGHT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_USAGE_RIGHT"));

// Entity: COLLECTION_VIEW_DEFINITION
      registry.put(
          "COLLECTION_VIEW_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "COLLECTION_VIEW_DEFINITION"));

// Entity: CONFIGURABLE_ITEM
      registry.put(
          "CONFIGURABLE_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CONFIGURABLE_ITEM"));

// Entity: CONNECTIVITY_DEFINITION
      registry.put(
          "CONNECTIVITY_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CONNECTIVITY_DEFINITION"));

// Entity: COORDINATES_LIST
      registry.put(
          "COORDINATES_LIST",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "COORDINATES_LIST"));

// Entity: CRITERION_REPORT_ITEM_WITH_NUMBER_OF_INSTANCES
      registry.put(
          "CRITERION_REPORT_ITEM_WITH_NUMBER_OF_INSTANCES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CRITERION_REPORT_ITEM_WITH_NUMBER_OF_INSTANCES"));

// Entity: CRITERION_REPORT_ITEM_WITH_VALUE
      registry.put(
          "CRITERION_REPORT_ITEM_WITH_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CRITERION_REPORT_ITEM_WITH_VALUE"));

// Entity: DATA_EQUIVALENCE_ASSESSMENT_SPECIFICATION
      registry.put(
          "DATA_EQUIVALENCE_ASSESSMENT_SPECIFICATION",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "DATA_EQUIVALENCE_ASSESSMENT_SPECIFICATION"));

// Entity: DATA_EQUIVALENCE_CRITERION
      registry.put(
          "DATA_EQUIVALENCE_CRITERION",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "DATA_EQUIVALENCE_CRITERION"));

// Entity: DATA_EQUIVALENCE_DEFINITION
      registry.put(
          "DATA_EQUIVALENCE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DATA_EQUIVALENCE_DEFINITION"));

// Entity: DATA_EQUIVALENCE_INSPECTION_CRITERION_REPORT
      registry.put(
          "DATA_EQUIVALENCE_INSPECTION_CRITERION_REPORT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "DATA_EQUIVALENCE_INSPECTION_CRITERION_REPORT"));

// Entity: DATA_EQUIVALENCE_INSPECTION_CRITERION_REPORT_ITEM
      registry.put(
          "DATA_EQUIVALENCE_INSPECTION_CRITERION_REPORT_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "DATA_EQUIVALENCE_INSPECTION_CRITERION_REPORT_ITEM"));

// Entity: DATA_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM
      registry.put(
          "DATA_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DATA_EQUIVALENCE_INSPECTION_INSTANCE_REPORT_ITEM"));

// Entity: DATA_QUALITY_ASSESSMENT_MEASUREMENT_ASSOCIATION
      registry.put(
          "DATA_QUALITY_ASSESSMENT_MEASUREMENT_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_QUALITY_ASSESSMENT_MEASUREMENT_ASSOCIATION"));

// Entity: DATA_QUALITY_ASSESSMENT_SPECIFICATION
      registry.put(
          "DATA_QUALITY_ASSESSMENT_SPECIFICATION",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "DATA_QUALITY_ASSESSMENT_SPECIFICATION"));

// Entity: DATA_QUALITY_CRITERION
      registry.put(
          "DATA_QUALITY_CRITERION",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "DATA_QUALITY_CRITERION"));

// Entity: DATA_QUALITY_CRITERION_ASSESSMENT_ASSOCIATION
      registry.put(
          "DATA_QUALITY_CRITERION_ASSESSMENT_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_QUALITY_CRITERION_ASSESSMENT_ASSOCIATION"));

// Entity: DATA_QUALITY_CRITERION_MEASUREMENT_ASSOCIATION
      registry.put(
          "DATA_QUALITY_CRITERION_MEASUREMENT_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_QUALITY_CRITERION_MEASUREMENT_ASSOCIATION"));

// Entity: DATA_QUALITY_DEFINITION
      registry.put(
          "DATA_QUALITY_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DATA_QUALITY_DEFINITION"));

// Entity: DATA_QUALITY_INSPECTION_CRITERION_REPORT
      registry.put(
          "DATA_QUALITY_INSPECTION_CRITERION_REPORT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "DATA_QUALITY_INSPECTION_CRITERION_REPORT"));

// Entity: DATA_QUALITY_INSPECTION_CRITERION_REPORT_ITEM
      registry.put(
          "DATA_QUALITY_INSPECTION_CRITERION_REPORT_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "DATA_QUALITY_INSPECTION_CRITERION_REPORT_ITEM"));

// Entity: DATA_QUALITY_INSPECTION_INSTANCE_REPORT_ITEM
      registry.put(
          "DATA_QUALITY_INSPECTION_INSTANCE_REPORT_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DATA_QUALITY_INSPECTION_INSTANCE_REPORT_ITEM"));

// Entity: DATA_QUALITY_REPORT_MEASUREMENT_ASSOCIATION
      registry.put(
          "DATA_QUALITY_REPORT_MEASUREMENT_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATA_QUALITY_REPORT_MEASUREMENT_ASSOCIATION"));

// Entity: DEPENDENT_VARIABLE_DEFINITION
      registry.put(
          "DEPENDENT_VARIABLE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DEPENDENT_VARIABLE_DEFINITION"));

// Entity: DRAWING_DEFINITION
      registry.put(
          "DRAWING_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DRAWING_DEFINITION"));

// Entity: DRAWING_SHEET_REVISION_USAGE
      registry.put(
          "DRAWING_SHEET_REVISION_USAGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DRAWING_SHEET_REVISION_USAGE"));

// Entity: EQUIVALENCE_INSTANCE_REPORT_ITEM_WITH_NOTABLE_INSTANCES
      registry.put(
          "EQUIVALENCE_INSTANCE_REPORT_ITEM_WITH_NOTABLE_INSTANCES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EQUIVALENCE_INSTANCE_REPORT_ITEM_WITH_NOTABLE_INSTANCES"));

// Entity: EVENT_OCCURRENCE
      registry.put(
          "EVENT_OCCURRENCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "EVENT_OCCURRENCE"));

// Entity: EXTERNALLY_CONDITIONED_DATA_QUALITY_CRITERION
      registry.put(
          "EXTERNALLY_CONDITIONED_DATA_QUALITY_CRITERION",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "EXTERNALLY_CONDITIONED_DATA_QUALITY_CRITERION"));

// Entity: EXTERNALLY_CONDITIONED_DATA_QUALITY_INSPECTION_INSTANCE_REPORT_ITEM
      registry.put(
          "EXTERNALLY_CONDITIONED_DATA_QUALITY_INSPECTION_INSTANCE_REPORT_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EXTERNALLY_CONDITIONED_DATA_QUALITY_INSPECTION_INSTANCE_REPORT_ITEM"));

// Entity: EXTERNALLY_DEFINED_ITEM_WITH_MULTIPLE_REFERENCES
      registry.put(
          "EXTERNALLY_DEFINED_ITEM_WITH_MULTIPLE_REFERENCES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EXTERNALLY_DEFINED_ITEM_WITH_MULTIPLE_REFERENCES"));

// Entity: EXTERNALLY_LISTED_DATA
      registry.put(
          "EXTERNALLY_LISTED_DATA",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "EXTERNALLY_LISTED_DATA"));

// Entity: FIXED_INSTANCE_ATTRIBUTE_SET
      registry.put(
          "FIXED_INSTANCE_ATTRIBUTE_SET",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "FIXED_INSTANCE_ATTRIBUTE_SET"));

// Entity: FOUNDED_ITEM
      registry.put(
          "FOUNDED_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "FOUNDED_ITEM"));

// Entity: GPS_FILTRATION_SPECIFICATION
      registry.put(
          "GPS_FILTRATION_SPECIFICATION",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "GPS_FILTRATION_SPECIFICATION"));

// Entity: INFORMATION_USAGE_RIGHT
      registry.put(
          "INFORMATION_USAGE_RIGHT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "INFORMATION_USAGE_RIGHT"));

// Entity: INSTANCE_REPORT_ITEM_WITH_EXTREME_INSTANCES
      registry.put(
          "INSTANCE_REPORT_ITEM_WITH_EXTREME_INSTANCES",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "INSTANCE_REPORT_ITEM_WITH_EXTREME_INSTANCES"));

// Entity: MATING_MATERIAL_ITEMS
      registry.put(
          "MATING_MATERIAL_ITEMS",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "MATING_MATERIAL_ITEMS"));

// Entity: NON_AGREED_ACCURACY_PARAMETER_USAGE
      registry.put(
          "NON_AGREED_ACCURACY_PARAMETER_USAGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "NON_AGREED_ACCURACY_PARAMETER_USAGE"));

// Entity: NON_AGREED_SCALE_USAGE
      registry.put(
          "NON_AGREED_SCALE_USAGE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "NON_AGREED_SCALE_USAGE"));

// Entity: PLY_LAMINATE_SEQUENCE_DEFINITION
      registry.put(
          "PLY_LAMINATE_SEQUENCE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PLY_LAMINATE_SEQUENCE_DEFINITION"));

// Entity: PRESENTED_ITEM
      registry.put(
          "PRESENTED_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PRESENTED_ITEM"));

// Entity: PROCESS_PRODUCT_ASSOCIATION
      registry.put(
          "PROCESS_PRODUCT_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "PROCESS_PRODUCT_ASSOCIATION"));

// Entity: PRODUCT_GROUP_ATTRIBUTE_SET
      registry.put(
          "PRODUCT_GROUP_ATTRIBUTE_SET",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PRODUCT_GROUP_ATTRIBUTE_SET"));

// Entity: PRODUCT_SPECIFICATION
      registry.put(
          "PRODUCT_SPECIFICATION",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "PRODUCT_SPECIFICATION"));

// Entity: RELATIVE_EVENT_OCCURRENCE
      registry.put(
          "RELATIVE_EVENT_OCCURRENCE",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "RELATIVE_EVENT_OCCURRENCE"));

// Entity: REP_ITEM_GROUP
      registry.put(
          "REP_ITEM_GROUP",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "REP_ITEM_GROUP"));

// Entity: RIGHT_TO_USAGE_ASSOCIATION
      registry.put(
          "RIGHT_TO_USAGE_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "RIGHT_TO_USAGE_ASSOCIATION"));

// Entity: ROLE_ASSOCIATION
      registry.put(
          "ROLE_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ROLE_ASSOCIATION"));

// Entity: RULE_DEFINITION
      registry.put(
          "RULE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "RULE_DEFINITION"));

// Entity: RULE_SET
      registry.put(
          "RULE_SET",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "RULE_SET"));

// Entity: RULE_SET_GROUP
      registry.put(
          "RULE_SET_GROUP",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "RULE_SET_GROUP"));

// Entity: RULE_SOFTWARE_DEFINITION
      registry.put(
          "RULE_SOFTWARE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "RULE_SOFTWARE_DEFINITION"));

// Entity: SATISFYING_ITEM
      registry.put(
          "SATISFYING_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SATISFYING_ITEM"));

// Entity: SCANNED_DATA_ITEM
      registry.put(
          "SCANNED_DATA_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SCANNED_DATA_ITEM"));

// Entity: SHAPE_DATA_QUALITY_CRITERION
      registry.put(
          "SHAPE_DATA_QUALITY_CRITERION",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "SHAPE_DATA_QUALITY_CRITERION"));

// Entity: SHAPE_DATA_QUALITY_CRITERION_AND_ACCURACY_ASSOCIATION
      registry.put(
          "SHAPE_DATA_QUALITY_CRITERION_AND_ACCURACY_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "SHAPE_DATA_QUALITY_CRITERION_AND_ACCURACY_ASSOCIATION"));

// Entity: SHAPE_DATA_QUALITY_INSPECTION_CRITERION_REPORT
      registry.put(
          "SHAPE_DATA_QUALITY_INSPECTION_CRITERION_REPORT",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "SHAPE_DATA_QUALITY_INSPECTION_CRITERION_REPORT"));

// Entity: SHAPE_DATA_QUALITY_INSPECTION_INSTANCE_REPORT_ITEM
      registry.put(
          "SHAPE_DATA_QUALITY_INSPECTION_INSTANCE_REPORT_ITEM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "SHAPE_DATA_QUALITY_INSPECTION_INSTANCE_REPORT_ITEM"));

  }
}
