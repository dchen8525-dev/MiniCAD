package com.minicad.step.semantic;

import java.util.Map;

/**
 * Registry for classification entity types.
 * Extracted from MiscRegistry.java during refactoring.
 */
public final class ClassificationRegistry {

  private ClassificationRegistry() {}

  public static void register(Map<String, EntityFactory> registry) {
// Entity: ADDRESS
      registry.put("ADDRESS", StepEntityResolver::resolveAddress);

// Entity: PERSON
      registry.put("PERSON", (resolver, instance) -> resolver.assignmentResolver.resolvePerson(instance));

// Entity: ORGANIZATION
      registry.put("ORGANIZATION", StepEntityResolver::resolveOrganization);

// Entity: PERSON_AND_ORGANIZATION
      registry.put(
          "PERSON_AND_ORGANIZATION", (resolver, instance) -> resolver.assignmentResolver.resolvePersonAndOrganization(instance));

// Entity: ORGANIZATION_RELATIONSHIP
      registry.put(
          "ORGANIZATION_RELATIONSHIP",
          (resolver, instance) -> resolver.resolveOrganizationRelationship(instance));

// Entity: ORGANIZATION_ROLE
      registry.put("ORGANIZATION_ROLE", (resolver, instance) -> resolver.assignmentResolver.resolveOrganizationRole(instance));

// Entity: ORGANIZATION_ASSIGNMENT
      registry.put("ORGANIZATION_ASSIGNMENT", (resolver, instance) -> resolver.assignmentResolver.resolveOrganizationAssignment(instance));

// Entity: APPLIED_ORGANIZATION_ASSIGNMENT
      registry.put(
          "APPLIED_ORGANIZATION_ASSIGNMENT",
          StepEntityResolver::resolveAppliedOrganizationAssignment);

// Entity: CC_DESIGN_ORGANIZATION_ASSIGNMENT
      registry.put(
          "CC_DESIGN_ORGANIZATION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveAppliedOrganizationAssignment(
                  instance, "CC_DESIGN_ORGANIZATION_ASSIGNMENT"));

// Entity: PERSON_AND_ORGANIZATION_ROLE
      registry.put(
          "PERSON_AND_ORGANIZATION_ROLE",
          (resolver, instance) -> resolver.assignmentResolver.resolvePersonAndOrganizationRole(instance));

// Entity: PERSON_AND_ORGANIZATION_ASSIGNMENT
      registry.put(
          "PERSON_AND_ORGANIZATION_ASSIGNMENT",
          (resolver, instance) -> resolver.assignmentResolver.resolvePersonAndOrganizationAssignment(instance));

// Entity: APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT
      registry.put(
          "APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT",
          StepEntityResolver::resolveAppliedPersonAndOrganizationAssignment);

// Entity: CC_DESIGN_PERSON_AND_ORGANIZATION_ASSIGNMENT
      registry.put(
          "CC_DESIGN_PERSON_AND_ORGANIZATION_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveAppliedPersonAndOrganizationAssignment(
                  instance, "CC_DESIGN_PERSON_AND_ORGANIZATION_ASSIGNMENT"));

// Entity: CALENDAR_DATE
      registry.put("CALENDAR_DATE", StepEntityResolver::resolveCalendarDate);

// Entity: COORDINATED_UNIVERSAL_TIME_OFFSET
      registry.put("COORDINATED_UNIVERSAL_TIME_OFFSET", StepEntityResolver::resolveCoordinatedUniversalTimeOffset);


// Entity: LOCAL_TIME
      registry.put("LOCAL_TIME", StepEntityResolver::resolveLocalTime);

// Entity: DATE_AND_TIME
      registry.put("DATE_AND_TIME", StepEntityResolver::resolveDateAndTime);

// Entity: DATE_ROLE
      registry.put("DATE_ROLE", (resolver, instance) -> resolver.assignmentResolver.resolveDateRole(instance));

// Entity: DATE_ASSIGNMENT
      registry.put("DATE_ASSIGNMENT", (resolver, instance) -> resolver.assignmentResolver.resolveDateAssignment(instance));

// Entity: APPLIED_DATE_ASSIGNMENT
      registry.put("APPLIED_DATE_ASSIGNMENT", StepEntityResolver::resolveAppliedDateAssignment);

// Entity: CC_DESIGN_DATE_ASSIGNMENT
      registry.put(
          "CC_DESIGN_DATE_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveAppliedDateAssignment(instance, "CC_DESIGN_DATE_ASSIGNMENT"));

// Entity: DATE_TIME_ROLE
      registry.put("DATE_TIME_ROLE", (resolver, instance) -> resolver.assignmentResolver.resolveDateTimeRole(instance));

// Entity: DATE_TIME_ASSIGNMENT
      registry.put("DATE_TIME_ASSIGNMENT", (resolver, instance) -> resolver.assignmentResolver.resolveDateTimeAssignment(instance));

// Entity: APPLIED_DATE_AND_TIME_ASSIGNMENT
      registry.put(
          "APPLIED_DATE_AND_TIME_ASSIGNMENT",
          StepEntityResolver::resolveAppliedDateTimeAssignment);

// Entity: APPLIED_DATE_TIME_ASSIGNMENT
      registry.put(
          "APPLIED_DATE_TIME_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveAppliedDateTimeAssignment(instance, "APPLIED_DATE_TIME_ASSIGNMENT"));

// Entity: CC_DESIGN_DATE_AND_TIME_ASSIGNMENT
      registry.put(
          "CC_DESIGN_DATE_AND_TIME_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveAppliedDateTimeAssignment(
                  instance, "CC_DESIGN_DATE_AND_TIME_ASSIGNMENT"));

// Entity: SECURITY_CLASSIFICATION_LEVEL
      registry.put(
          "SECURITY_CLASSIFICATION_LEVEL",
          (resolver, instance) -> resolver.assignmentResolver.resolveSecurityClassificationLevel(instance));

// Entity: SECURITY_CLASSIFICATION
      registry.put(
          "SECURITY_CLASSIFICATION", (resolver, instance) -> resolver.assignmentResolver.resolveSecurityClassification(instance));

// Entity: SECURITY_CLASSIFICATION_ASSIGNMENT
      registry.put(
          "SECURITY_CLASSIFICATION_ASSIGNMENT",
          (resolver, instance) -> resolver.assignmentResolver.resolveSecurityClassificationAssignment(instance));

// Entity: APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT
      registry.put(
          "APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT",
          StepEntityResolver::resolveAppliedSecurityClassificationAssignment);

// Entity: CC_DESIGN_SECURITY_CLASSIFICATION
      registry.put(
          "CC_DESIGN_SECURITY_CLASSIFICATION",
          (resolver, instance) ->
              resolver.resolveAppliedSecurityClassificationAssignment(
                  instance, "CC_DESIGN_SECURITY_CLASSIFICATION"));

// Entity: CERTIFICATION_TYPE
      registry.put("CERTIFICATION_TYPE", (resolver, instance) -> resolver.assignmentResolver.resolveCertificationType(instance));

// Entity: CERTIFICATION
      registry.put("CERTIFICATION", StepEntityResolver::resolveCertification);

// Entity: CERTIFICATION_ASSIGNMENT
      registry.put(
          "CERTIFICATION_ASSIGNMENT", (resolver, instance) -> resolver.assignmentResolver.resolveCertificationAssignment(instance));

// Entity: APPLIED_CERTIFICATION_ASSIGNMENT
      registry.put(
          "APPLIED_CERTIFICATION_ASSIGNMENT",
          StepEntityResolver::resolveAppliedCertificationAssignment);

// Entity: CC_DESIGN_CERTIFICATION
      registry.put(
          "CC_DESIGN_CERTIFICATION",
          (resolver, instance) ->
              resolver.resolveAppliedCertificationAssignment(instance, "CC_DESIGN_CERTIFICATION"));

// Entity: APPLIED_ORGANIZATIONAL_PROJECT_ASSIGNMENT
      registry.put(
          "APPLIED_ORGANIZATIONAL_PROJECT_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_ORGANIZATIONAL_PROJECT_ASSIGNMENT"));

// Entity: APPLIED_ORGANIZATION_TYPE_ASSIGNMENT
      registry.put(
          "APPLIED_ORGANIZATION_TYPE_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_ORGANIZATION_TYPE_ASSIGNMENT"));

// Entity: APPLIED_TIME_INTERVAL_ASSIGNMENT
      registry.put(
          "APPLIED_TIME_INTERVAL_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "APPLIED_TIME_INTERVAL_ASSIGNMENT"));

// Entity: DATE_AND_TIME_ASSIGNMENT
      registry.put(
          "DATE_AND_TIME_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "DATE_AND_TIME_ASSIGNMENT"));

// Entity: ORGANIZATIONAL_PROJECT_ASSIGNMENT
      registry.put(
          "ORGANIZATIONAL_PROJECT_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ORGANIZATIONAL_PROJECT_ASSIGNMENT"));

// Entity: ORGANIZATIONAL_PROJECT_RELATIONSHIP
      registry.put(
          "ORGANIZATIONAL_PROJECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ORGANIZATIONAL_PROJECT_RELATIONSHIP"));

// Entity: ORGANIZATIONAL_PROJECT_ROLE
      registry.put(
          "ORGANIZATIONAL_PROJECT_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericRole(instance, "ORGANIZATIONAL_PROJECT_ROLE"));

// Entity: ORGANIZATION_TYPE_ASSIGNMENT
      registry.put(
          "ORGANIZATION_TYPE_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ORGANIZATION_TYPE_ASSIGNMENT"));

// Entity: ORGANIZATION_TYPE_ROLE
      registry.put(
          "ORGANIZATION_TYPE_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericType(instance, "ORGANIZATION_TYPE_ROLE"));

// Entity: TIME_INTERVAL_ASSIGNMENT
      registry.put(
          "TIME_INTERVAL_ASSIGNMENT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "TIME_INTERVAL_ASSIGNMENT"));

// Entity: TIME_INTERVAL_RELATIONSHIP
      registry.put(
          "TIME_INTERVAL_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "TIME_INTERVAL_RELATIONSHIP"));

// Entity: TIME_INTERVAL_ROLE
      registry.put(
          "TIME_INTERVAL_ROLE",
          (resolver, instance) ->
              resolver.resolveGenericRole(instance, "TIME_INTERVAL_ROLE"));

// Entity: PERSON_AND_ORGANIZATION_ADDRESS
      registry.put(
          "PERSON_AND_ORGANIZATION_ADDRESS",
          (resolver, instance) -> resolver.assignmentResolver.resolvePersonAndOrganizationAddress(instance));

// Entity: ORGANIZATION_ADDRESS
      registry.put("ORGANIZATION_ADDRESS", (resolver, instance) -> resolver.assignmentResolver.resolveOrganizationAddress(instance));

// Entity: PERSON_ADDRESS
      registry.put("PERSON_ADDRESS", (resolver, instance) -> resolver.assignmentResolver.resolvePersonAddress(instance));

// Entity: DESIGN_CERTIFICATION
      registry.put(
          "DESIGN_CERTIFICATION",
          (resolver, instance) -> resolver.resolveCertification(instance));

// Entity: MANUFACTURING_CERTIFICATION
      registry.put(
          "MANUFACTURING_CERTIFICATION",
          (resolver, instance) -> resolver.resolveCertification(instance));

// Entity: QUALITY_CERTIFICATION
      registry.put(
          "QUALITY_CERTIFICATION",
          (resolver, instance) -> resolver.resolveCertification(instance));

// Entity: TESTING_CERTIFICATION
      registry.put(
          "TESTING_CERTIFICATION",
          (resolver, instance) -> resolver.resolveCertification(instance));

// Entity: SAFETY_CERTIFICATION
      registry.put(
          "SAFETY_CERTIFICATION",
          (resolver, instance) -> resolver.resolveCertification(instance));

// Entity: ENVIRONMENTAL_CERTIFICATION
      registry.put(
          "ENVIRONMENTAL_CERTIFICATION",
          (resolver, instance) -> resolver.resolveCertification(instance));

// Entity: ORGANIZATION_TYPE
      registry.put(
          "ORGANIZATION_TYPE",
          (resolver, instance) -> resolver.resolveOrganization(instance));

// Entity: ORGANIZATION_ADDRESS_ASSIGNMENT
      registry.put(
          "ORGANIZATION_ADDRESS_ASSIGNMENT",
          (resolver, instance) -> resolver.resolveAddress(instance));

// Entity: PERSON_ADDRESS_ASSIGNMENT
      registry.put(
          "PERSON_ADDRESS_ASSIGNMENT",
          (resolver, instance) -> resolver.resolveAddress(instance));

// Entity: PERSON_ORGANIZATION_ASSIGNMENT
      registry.put(
          "PERSON_ORGANIZATION_ASSIGNMENT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: ORDINAL_DATE
      registry.put(
          "ORDINAL_DATE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: WEEK_OF_YEAR_AND_DAY_DATE
      registry.put(
          "WEEK_OF_YEAR_AND_DAY_DATE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: APPROVAL (moved from ConfigManagementRegistry for test organization)
      registry.put("APPROVAL", StepEntityResolver::resolveApproval);

// Entity: APPROVAL_STATUS (moved from ConfigManagementRegistry for test organization)
      registry.put("APPROVAL_STATUS", (resolver, instance) -> resolver.assignmentResolver.resolveApprovalStatus(instance));

// Entity: APPROVAL_ROLE (moved from ConfigManagementRegistry for test organization)
      registry.put("APPROVAL_ROLE", (resolver, instance) -> resolver.assignmentResolver.resolveApprovalRole(instance));

// Entity: APPROVAL_ASSIGNMENT (moved from ConfigManagementRegistry for test organization)
      registry.put("APPROVAL_ASSIGNMENT", (resolver, instance) -> resolver.assignmentResolver.resolveApprovalAssignment(instance));

// Entity: DOCUMENT (moved from ConfigManagementRegistry for test organization)
      registry.put("DOCUMENT", StepEntityResolver::resolveDocument);

// Entity: DOCUMENT_TYPE (moved from ConfigManagementRegistry for test organization)
      registry.put("DOCUMENT_TYPE", (resolver, instance) -> resolver.productResolver.resolveDocumentType(instance));

// Entity: DOCUMENT_RELATIONSHIP (moved from ConfigManagementRegistry for test organization)
      registry.put("DOCUMENT_RELATIONSHIP", (resolver, instance) -> resolver.productResolver.resolveDocumentRelationship(instance));

// Entity: EXTERNAL_SOURCE (moved from MiscellaneousRegistry1 for test organization)
      registry.put("EXTERNAL_SOURCE", StepEntityResolver::resolveExternalSource);

  }
}