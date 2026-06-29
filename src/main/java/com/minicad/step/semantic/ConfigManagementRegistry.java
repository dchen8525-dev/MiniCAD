package com.minicad.step.semantic;

import java.util.Map;

/**
 * Registry for config entity types.
 * Extracted from MiscRegistry.java during refactoring.
 */
public final class ConfigManagementRegistry {

  private ConfigManagementRegistry() {}

  public static void register(Map<String, EntityFactory> registry) {
// Entity: DOCUMENT_TYPE
      registry.put("DOCUMENT_TYPE", StepEntityResolver::resolveDocumentType);

// Entity: DOCUMENT
      registry.put("DOCUMENT", StepEntityResolver::resolveDocument);

// Entity: DOCUMENT_RELATIONSHIP
      registry.put("DOCUMENT_RELATIONSHIP", StepEntityResolver::resolveDocumentRelationship);

// Entity: DOCUMENT_USAGE_CONSTRAINT
      registry.put(
          "DOCUMENT_USAGE_CONSTRAINT", StepEntityResolver::resolveDocumentUsageConstraint);

// Entity: DOCUMENT_REFERENCE
      registry.put("DOCUMENT_REFERENCE", StepEntityResolver::resolveDocumentReference);

// Entity: APPLIED_DOCUMENT_REFERENCE
      registry.put(
          "APPLIED_DOCUMENT_REFERENCE", StepEntityResolver::resolveAppliedDocumentReference);

// Entity: APPROVAL_STATUS
      registry.put("APPROVAL_STATUS", StepEntityResolver::resolveApprovalStatus);

// Entity: APPROVAL
      registry.put("APPROVAL", StepEntityResolver::resolveApproval);

// Entity: APPROVAL_ROLE
      registry.put("APPROVAL_ROLE", StepEntityResolver::resolveApprovalRole);

// Entity: APPROVAL_ASSIGNMENT
      registry.put("APPROVAL_ASSIGNMENT", StepEntityResolver::resolveApprovalAssignment);

// Entity: APPLIED_APPROVAL_ASSIGNMENT
      registry.put(
          "APPLIED_APPROVAL_ASSIGNMENT", StepEntityResolver::resolveAppliedApprovalAssignment);

// Entity: CC_DESIGN_APPROVAL
      registry.put(
          "CC_DESIGN_APPROVAL",
          (resolver, instance) ->
              resolver.resolveAppliedApprovalAssignment(instance, "CC_DESIGN_APPROVAL"));

// Entity: APPROVAL_PERSON_ORGANIZATION
      registry.put(
          "APPROVAL_PERSON_ORGANIZATION",
          StepEntityResolver::resolveApprovalPersonOrganization);

// Entity: APPROVAL_DATE_TIME
      registry.put("APPROVAL_DATE_TIME", StepEntityResolver::resolveApprovalDateTime);

// Entity: CONTRACT_TYPE
      registry.put("CONTRACT_TYPE", StepEntityResolver::resolveContractType);

// Entity: CONTRACT
      registry.put("CONTRACT", StepEntityResolver::resolveContract);

// Entity: CONTRACT_ASSIGNMENT
      registry.put("CONTRACT_ASSIGNMENT", StepEntityResolver::resolveContractAssignment);

// Entity: APPLIED_CONTRACT_ASSIGNMENT
      registry.put(
          "APPLIED_CONTRACT_ASSIGNMENT", StepEntityResolver::resolveAppliedContractAssignment);

// Entity: CC_DESIGN_CONTRACT
      registry.put(
          "CC_DESIGN_CONTRACT",
          (resolver, instance) ->
              resolver.resolveAppliedContractAssignment(instance, "CC_DESIGN_CONTRACT"));

// Entity: EFFECTIVITY
      registry.put("EFFECTIVITY", StepEntityResolver::resolveEffectivity);

// Entity: EFFECTIVITY_RELATIONSHIP
      registry.put("EFFECTIVITY_RELATIONSHIP", StepEntityResolver::resolveEffectivityRelationship);

// Entity: APPLIED_EFFECTIVITY_ASSIGNMENT
      registry.put(
          "APPLIED_EFFECTIVITY_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_EFFECTIVITY_ASSIGNMENT"));

// Entity: APPLIED_INEFFECTIVITY_ASSIGNMENT
      registry.put(
          "APPLIED_INEFFECTIVITY_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_INEFFECTIVITY_ASSIGNMENT"));

// Entity: APPROVAL_RELATIONSHIP
      registry.put(
          "APPROVAL_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "APPROVAL_RELATIONSHIP"));

// Entity: CHANGE_COMPOSITION_RELATIONSHIP
      registry.put(
          "CHANGE_COMPOSITION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CHANGE_COMPOSITION_RELATIONSHIP"));

// Entity: CHANGE_GROUP_ASSIGNMENT
      registry.put(
          "CHANGE_GROUP_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CHANGE_GROUP_ASSIGNMENT"));

// Entity: COLLECTION_VERSION_RELATIONSHIP
      registry.put(
          "COLLECTION_VERSION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "COLLECTION_VERSION_RELATIONSHIP"));

// Entity: COLLECTION_VERSION_SEQUENCE_RELATIONSHIP
      registry.put(
          "COLLECTION_VERSION_SEQUENCE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "COLLECTION_VERSION_SEQUENCE_RELATIONSHIP"));

// Entity: CONFIGURATION_ITEM_RELATIONSHIP
      registry.put(
          "CONFIGURATION_ITEM_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CONFIGURATION_ITEM_RELATIONSHIP"));

// Entity: CONFIGURED_EFFECTIVITY_ASSIGNMENT
      registry.put(
          "CONFIGURED_EFFECTIVITY_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONFIGURED_EFFECTIVITY_ASSIGNMENT"));

// Entity: CONTRACT_RELATIONSHIP
      registry.put(
          "CONTRACT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "CONTRACT_RELATIONSHIP"));

// Entity: DOCUMENT_IDENTIFIER
      registry.put(
          "DOCUMENT_IDENTIFIER",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DOCUMENT_IDENTIFIER"));

// Entity: DOCUMENT_IDENTIFIER_ASSIGNMENT
      registry.put(
          "DOCUMENT_IDENTIFIER_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DOCUMENT_IDENTIFIER_ASSIGNMENT"));

// Entity: DOCUMENT_USAGE_CONSTRAINT_ASSIGNMENT
      registry.put(
          "DOCUMENT_USAGE_CONSTRAINT_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DOCUMENT_USAGE_CONSTRAINT_ASSIGNMENT"));

// Entity: DOCUMENT_USAGE_ROLE
      registry.put(
          "DOCUMENT_USAGE_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericRole(instance, "DOCUMENT_USAGE_ROLE"));

// Entity: EFFECTIVITY_ASSIGNMENT
      registry.put(
          "EFFECTIVITY_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "EFFECTIVITY_ASSIGNMENT"));

// Entity: VERSIONED_ACTION_REQUEST_RELATIONSHIP
      registry.put(
          "VERSIONED_ACTION_REQUEST_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "VERSIONED_ACTION_REQUEST_RELATIONSHIP"));

// Entity: DOCUMENT_PRODUCT_ASSOCIATION
      registry.put(
          "DOCUMENT_PRODUCT_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DOCUMENT_PRODUCT_ASSOCIATION"));

// Entity: CHANGE_GROUP
      registry.put(
          "CHANGE_GROUP",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CHANGE_GROUP"));

// Entity: CHANGE_REQUEST
      registry.put(
          "CHANGE_REQUEST",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CHANGE_REQUEST"));

// Entity: COLLECTION_VERSION
      registry.put(
          "COLLECTION_VERSION",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "COLLECTION_VERSION"));

// Entity: CONDITIONAL_EFFECTIVITY
      registry.put(
          "CONDITIONAL_EFFECTIVITY",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CONDITIONAL_EFFECTIVITY"));

// Entity: CONFIGURATION_ITEM
      registry.put("CONFIGURATION_ITEM", StepEntityResolver::resolveConfigurationItem);

// Entity: CONFIGURATION_EFFECTIVITY
      registry.put(
          "CONFIGURATION_EFFECTIVITY",
          (resolver, instance) -> resolver.resolveEffectivity(instance));

// Entity: CLASSIFIED_EFFECTIVITY
      registry.put(
          "CLASSIFIED_EFFECTIVITY",
          (resolver, instance) -> resolver.resolveEffectivity(instance));

// Entity: INTERPOLATED_CONFIGURATION_SEGMENT
      registry.put("INTERPOLATED_CONFIGURATION_SEGMENT", StepEntityResolver::resolveInterpolatedConfigurationSegment);

// Entity: DESIGNED_PART_DESIGN_VERSION
      registry.put("DESIGNED_PART_DESIGN_VERSION", StepEntityResolver::resolveDesignedPartDesignVersion);

// Entity: CHANGE
      registry.put("CHANGE", StepEntityResolver::resolveChange);

// Entity: CONFIGURATION_INSTANCE
      registry.put("CONFIGURATION_INSTANCE", StepEntityResolver::resolveConfigurationInstance);

// Entity: DRAWING_DOCUMENT
      registry.put(
          "DRAWING_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: SPECIFICATION_DOCUMENT
      registry.put(
          "SPECIFICATION_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: TEST_DOCUMENT
      registry.put(
          "TEST_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: REPORT_DOCUMENT
      registry.put(
          "REPORT_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: MANUAL_DOCUMENT
      registry.put(
          "MANUAL_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: PROCEDURE_DOCUMENT
      registry.put(
          "PROCEDURE_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: STANDARD_DOCUMENT
      registry.put(
          "STANDARD_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: REGULATION_DOCUMENT
      registry.put(
          "REGULATION_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: CONTRACT_DOCUMENT
      registry.put(
          "CONTRACT_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: ORDER_DOCUMENT
      registry.put(
          "ORDER_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: QUOTATION_DOCUMENT
      registry.put(
          "QUOTATION_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: INVOICE_DOCUMENT
      registry.put(
          "INVOICE_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: PACKING_LIST_DOCUMENT
      registry.put(
          "PACKING_LIST_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: SHIPPING_DOCUMENT
      registry.put(
          "SHIPPING_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: RECEIVING_DOCUMENT
      registry.put(
          "RECEIVING_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: INSPECTION_DOCUMENT
      registry.put(
          "INSPECTION_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: CERTIFICATION_DOCUMENT
      registry.put(
          "CERTIFICATION_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: WARRANTY_DOCUMENT
      registry.put(
          "WARRANTY_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: MAINTENANCE_DOCUMENT
      registry.put(
          "MAINTENANCE_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: CALIBRATION_DOCUMENT
      registry.put(
          "CALIBRATION_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: TRAINING_DOCUMENT
      registry.put(
          "TRAINING_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: SAFETY_DOCUMENT
      registry.put(
          "SAFETY_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: ENVIRONMENTAL_DOCUMENT
      registry.put(
          "ENVIRONMENTAL_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: DESIGN_APPROVAL
      registry.put(
          "DESIGN_APPROVAL",
          (resolver, instance) -> resolver.resolveApproval(instance));

// Entity: MANUFACTURING_APPROVAL
      registry.put(
          "MANUFACTURING_APPROVAL",
          (resolver, instance) -> resolver.resolveApproval(instance));

// Entity: QUALITY_APPROVAL
      registry.put(
          "QUALITY_APPROVAL",
          (resolver, instance) -> resolver.resolveApproval(instance));

// Entity: TESTING_APPROVAL
      registry.put(
          "TESTING_APPROVAL",
          (resolver, instance) -> resolver.resolveApproval(instance));

// Entity: SHIPPING_APPROVAL
      registry.put(
          "SHIPPING_APPROVAL",
          (resolver, instance) -> resolver.resolveApproval(instance));

// Entity: DELIVERY_APPROVAL
      registry.put(
          "DELIVERY_APPROVAL",
          (resolver, instance) -> resolver.resolveApproval(instance));

// Entity: PURCHASE_CONTRACT
      registry.put(
          "PURCHASE_CONTRACT",
          (resolver, instance) -> resolver.resolveContract(instance));

// Entity: SALES_CONTRACT
      registry.put(
          "SALES_CONTRACT",
          (resolver, instance) -> resolver.resolveContract(instance));

// Entity: SERVICE_CONTRACT
      registry.put(
          "SERVICE_CONTRACT",
          (resolver, instance) -> resolver.resolveContract(instance));

// Entity: MAINTENANCE_CONTRACT
      registry.put(
          "MAINTENANCE_CONTRACT",
          (resolver, instance) -> resolver.resolveContract(instance));

// Entity: LEASE_CONTRACT
      registry.put(
          "LEASE_CONTRACT",
          (resolver, instance) -> resolver.resolveContract(instance));

// Entity: LICENSE_CONTRACT
      registry.put(
          "LICENSE_CONTRACT",
          (resolver, instance) -> resolver.resolveContract(instance));

// Entity: WARRANTY_CONTRACT
      registry.put(
          "WARRANTY_CONTRACT",
          (resolver, instance) -> resolver.resolveContract(instance));

// Entity: CONFIGURATION_ITEM_EFFECTIVITY
      registry.put(
          "CONFIGURATION_ITEM_EFFECTIVITY",
          (resolver, instance) -> resolver.resolveEffectivity(instance));

// Entity: CONFIGURATION_ITEM_HIERARCHICAL_RELATIONSHIP
      registry.put(
          "CONFIGURATION_ITEM_HIERARCHICAL_RELATIONSHIP",
          (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "CONFIGURATION_ITEM_HIERARCHICAL_RELATIONSHIP"));

// Entity: CONFIGURATION_ITEM_REVISION_SEQUENCE
      registry.put(
          "CONFIGURATION_ITEM_REVISION_SEQUENCE",
          (resolver, instance) -> resolver.resolveProductDefinitionRelationship(instance, "CONFIGURATION_ITEM_REVISION_SEQUENCE"));

// Entity: CONFIGURATION_DESIGN
      registry.put(
          "CONFIGURATION_DESIGN",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: CONFIGURATION_DESIGN_ITEM
      registry.put(
          "CONFIGURATION_DESIGN_ITEM",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: DOCUMENT_FILE
      registry.put(
          "DOCUMENT_FILE",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: APPLIED_DOCUMENT_USAGE_CONSTRAINT_ASSIGNMENT
      registry.put(
          "APPLIED_DOCUMENT_USAGE_CONSTRAINT_ASSIGNMENT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: APPLIED_EXTERNAL_DOCUMENT_ASSIGNMENT
      registry.put(
          "APPLIED_EXTERNAL_DOCUMENT_ASSIGNMENT",
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, "APPLIED_EXTERNAL_DOCUMENT_ASSIGNMENT"));

// Entity: DATE_TIME_EFFECTIVITY
      registry.put("DATE_TIME_EFFECTIVITY", StepEntityResolver::resolveDateTimeEffectivity);

// Entity: DATE_EFFECTIVITY
      registry.put("DATE_EFFECTIVITY", StepEntityResolver::resolveDateEffectivity);

// Entity: LOT_EFFECTIVITY
      registry.put("LOT_EFFECTIVITY", StepEntityResolver::resolveLotEffectivity);

// Entity: SERIAL_NUMBER_EFFECTIVITY
      registry.put("SERIAL_NUMBER_EFFECTIVITY", StepEntityResolver::resolveSerialNumberEffectivity);

// Entity: PRODUCT_VERSION
      registry.put("PRODUCT_VERSION", StepEntityResolver::resolveProductVersion);


  }
}
