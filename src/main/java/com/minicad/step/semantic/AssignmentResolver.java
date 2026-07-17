package com.minicad.step.semantic;

import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;


/**
 * Assignment resolver - handles organization, approval, date, and assignment entities.
 * Extracted from StepEntityResolver to reduce file size and improve maintainability.
 * Contains persons/organizations/addresses, approvals, dates, contracts,
 * certifications, classifications, identifications, and effectivity entities.
 */
final class AssignmentResolver {

  private final StepEntityResolver resolver;

  AssignmentResolver(StepEntityResolver resolver) {
    this.resolver = resolver;
  }

  // === Person / Organization Entities ===

  StepAddress resolveAddress(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ADDRESS");
    StepEntityResolver.requireParameterCount(instance, definition, 12);
    return new StepAddress(
        instance.id(),
        resolver.optionalStringValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1),
        resolver.optionalStringValue(instance, definition, 2),
        resolver.optionalStringValue(instance, definition, 3),
        resolver.optionalStringValue(instance, definition, 4),
        resolver.optionalStringValue(instance, definition, 5),
        resolver.optionalStringValue(instance, definition, 6),
        resolver.optionalStringValue(instance, definition, 7),
        resolver.optionalStringValue(instance, definition, 8),
        resolver.optionalStringValue(instance, definition, 9),
        resolver.optionalStringValue(instance, definition, 10),
        resolver.optionalStringValue(instance, definition, 11));
  }

  StepOrganization resolveOrganization(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ORGANIZATION");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepOrganization(
        instance.id(),
        resolver.optionalStringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.optionalStringValue(instance, definition, 2));
  }

  StepOrganizationAddress resolveOrganizationAddress(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ORGANIZATION_ADDRESS");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepOrganizationAddress(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepOrganizationAssignment resolveOrganizationAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ORGANIZATION_ASSIGNMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepOrganizationAssignment(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepOrganization.class,
            "ORGANIZATION_ASSIGNMENT assigned_organization must reference ORGANIZATION"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepOrganizationRole.class,
            "ORGANIZATION_ASSIGNMENT role must reference ORGANIZATION_ROLE"));
  }

  StepOrganizationRelationship resolveOrganizationRelationship(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ORGANIZATION_RELATIONSHIP");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepOrganizationRelationship(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepOrganization.class,
            "ORGANIZATION_RELATIONSHIP relating_organization must reference ORGANIZATION"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepOrganization.class,
            "ORGANIZATION_RELATIONSHIP related_organization must reference ORGANIZATION"));
  }

  StepOrganizationRole resolveOrganizationRole(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ORGANIZATION_ROLE");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepOrganizationRole(instance.id(), resolver.stringValue(instance, definition, 0));
  }

  StepPerson resolvePerson(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PERSON");
    StepEntityResolver.requireParameterCount(instance, definition, 6);
    return new StepPerson(
        instance.id(),
        resolver.optionalStringValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1),
        resolver.optionalStringValue(instance, definition, 2),
        resolver.optionalStringListValue(instance, definition, 3),
        resolver.optionalStringListValue(instance, definition, 4),
        resolver.optionalStringListValue(instance, definition, 5));
  }

  StepPersonAddress resolvePersonAddress(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PERSON_ADDRESS");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepPersonAddress(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepPersonAndOrganization resolvePersonAndOrganization(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PERSON_AND_ORGANIZATION");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepPersonAndOrganization(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepPerson.class,
            "PERSON_AND_ORGANIZATION person must reference PERSON"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepOrganization.class,
            "PERSON_AND_ORGANIZATION organization must reference ORGANIZATION"));
  }

  StepPersonAndOrganizationAddress resolvePersonAndOrganizationAddress(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PERSON_AND_ORGANIZATION_ADDRESS");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepPersonAndOrganizationAddress(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepPersonAndOrganizationAssignment resolvePersonAndOrganizationAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PERSON_AND_ORGANIZATION_ASSIGNMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepPersonAndOrganizationAssignment(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepPersonAndOrganization.class,
            "PERSON_AND_ORGANIZATION_ASSIGNMENT assigned_person_and_organization must reference PERSON_AND_ORGANIZATION"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepPersonAndOrganizationRole.class,
            "PERSON_AND_ORGANIZATION_ASSIGNMENT role must reference PERSON_AND_ORGANIZATION_ROLE"));
  }

  StepPersonAndOrganizationRole resolvePersonAndOrganizationRole(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PERSON_AND_ORGANIZATION_ROLE");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepPersonAndOrganizationRole(instance.id(), resolver.stringValue(instance, definition, 0));
  }

  // === Organization / Group / Language Assignment Entities ===

  StepAppliedGroupAssignment resolveAppliedGroupAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "APPLIED_GROUP_ASSIGNMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepAppliedGroupAssignment(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepGroup.class,
            "APPLIED_GROUP_ASSIGNMENT assigned_group must reference GROUP"),
        resolver.entityReferenceList(
            instance,
            definition,
            1,
            "APPLIED_GROUP_ASSIGNMENT items must contain entity references"));
  }

  StepAppliedLanguageAssignment resolveAppliedLanguageAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "APPLIED_LANGUAGE_ASSIGNMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepAppliedLanguageAssignment(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepLanguage.class,
            "APPLIED_LANGUAGE_ASSIGNMENT assigned_language must reference LANGUAGE"),
        resolver.entityReferenceList(
            instance,
            definition,
            1,
            "APPLIED_LANGUAGE_ASSIGNMENT items must contain entity references"));
  }

  StepAppliedOrganizationAssignment resolveAppliedOrganizationAssignment(StepEntityInstance instance) {
    return resolveAppliedOrganizationAssignment(instance, "APPLIED_ORGANIZATION_ASSIGNMENT");
  }

  StepAppliedOrganizationAssignment resolveAppliedOrganizationAssignment(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepAppliedOrganizationAssignment(
        instance.id(),
        entityName,
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepOrganization.class,
            entityName + " assigned_organization must reference ORGANIZATION"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepOrganizationRole.class,
            entityName + " role must reference ORGANIZATION_ROLE"),
        resolver.entityReferenceList(
            instance,
            definition,
            2,
            entityName + " items must contain entity references"));
  }

  StepExclusionAssignment resolveExclusionAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "EXCLUSION_ASSIGNMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepExclusionAssignment(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.entityReferenceList(instance, definition, 1,
            "EXCLUSION_ASSIGNMENT assigned items must contain entity references"),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepGroupAssignment resolveGroupAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "GROUP_ASSIGNMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepGroupAssignment(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepGroup.class,
            "GROUP_ASSIGNMENT assigned_group must reference GROUP"));
  }

  StepEntity resolveLanguageAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "LANGUAGE_ASSIGNMENT");
    if (definition.parameters().size() == 3) {
      return resolver.resolveRepresentation(instance, "LANGUAGE_ASSIGNMENT", false);
    }
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepLanguageAssignment(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepLanguage.class,
            "LANGUAGE_ASSIGNMENT assigned_language must reference LANGUAGE"));
  }

  // === Approval Entities ===

  StepAppliedApprovalAssignment resolveAppliedApprovalAssignment(StepEntityInstance instance) {
    return resolveAppliedApprovalAssignment(instance, "APPLIED_APPROVAL_ASSIGNMENT");
  }

  StepAppliedApprovalAssignment resolveAppliedApprovalAssignment(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepAppliedApprovalAssignment(
        instance.id(),
        entityName,
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepApproval.class,
            entityName + " assigned_approval must reference APPROVAL"),
        resolver.entityReferenceList(
            instance,
            definition,
            1,
            entityName + " items must contain entity references"));
  }

  StepApproval resolveApproval(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "APPROVAL");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepApproval(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepApprovalStatus.class,
            "APPROVAL status must reference APPROVAL_STATUS"),
        resolver.optionalStringValue(instance, definition, 1));
  }

  StepApprovalAssignment resolveApprovalAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "APPROVAL_ASSIGNMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepApprovalAssignment(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepApproval.class,
            "APPROVAL_ASSIGNMENT assigned_approval must reference APPROVAL"));
  }

  StepApprovalDateTime resolveApprovalDateTime(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "APPROVAL_DATE_TIME");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepApprovalDateTime(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepDateAndTime.class,
            "APPROVAL_DATE_TIME date_time must reference DATE_AND_TIME"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepApproval.class,
            "APPROVAL_DATE_TIME dated_approval must reference APPROVAL"));
  }

  StepApprovalPersonOrganization resolveApprovalPersonOrganization(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "APPROVAL_PERSON_ORGANIZATION");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepApprovalPersonOrganization(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepPersonAndOrganization.class,
            "APPROVAL_PERSON_ORGANIZATION person_organization must reference PERSON_AND_ORGANIZATION"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepApproval.class,
            "APPROVAL_PERSON_ORGANIZATION authorized_approval must reference APPROVAL"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepApprovalRole.class,
            "APPROVAL_PERSON_ORGANIZATION role must reference APPROVAL_ROLE"));
  }

  StepApprovalRole resolveApprovalRole(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "APPROVAL_ROLE");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepApprovalRole(instance.id(), resolver.stringValue(instance, definition, 0));
  }

  StepApprovalStatus resolveApprovalStatus(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "APPROVAL_STATUS");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepApprovalStatus(instance.id(), resolver.stringValue(instance, definition, 0));
  }

  // === Date / Time Entities ===

  StepAppliedDateAssignment resolveAppliedDateAssignment(StepEntityInstance instance) {
    return resolveAppliedDateAssignment(instance, "APPLIED_DATE_ASSIGNMENT");
  }

  StepAppliedDateAssignment resolveAppliedDateAssignment(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepAppliedDateAssignment(
        instance.id(),
        entityName,
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepCalendarDate.class,
            entityName + " assigned_date must reference CALENDAR_DATE"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepDateRole.class,
            entityName + " role must reference DATE_ROLE"),
        resolver.entityReferenceList(
            instance,
            definition,
            2,
            entityName + " items must contain entity references"));
  }

  StepAppliedDateTimeAssignment resolveAppliedDateTimeAssignment(StepEntityInstance instance) {
    return resolveAppliedDateTimeAssignment(instance, "APPLIED_DATE_AND_TIME_ASSIGNMENT");
  }

  StepAppliedDateTimeAssignment resolveAppliedDateTimeAssignment(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepAppliedDateTimeAssignment(
        instance.id(),
        entityName,
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepDateAndTime.class,
            entityName + " assigned_date_and_time must reference DATE_AND_TIME"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepDateTimeRole.class,
            entityName + " role must reference DATE_TIME_ROLE"),
        resolver.entityReferenceList(
            instance,
            definition,
            2,
            entityName + " items must contain entity references"));
  }

  StepDateAssignment resolveDateAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DATE_ASSIGNMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepDateAssignment(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepCalendarDate.class,
            "DATE_ASSIGNMENT assigned_date must reference CALENDAR_DATE"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepDateRole.class,
            "DATE_ASSIGNMENT role must reference DATE_ROLE"));
  }

  StepDateEffectivity resolveDateEffectivity(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DATE_EFFECTIVITY");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepDateEffectivity(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)));
  }

  StepDateRole resolveDateRole(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DATE_ROLE");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepDateRole(instance.id(), resolver.stringValue(instance, definition, 0));
  }

  StepDateTimeAssignment resolveDateTimeAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DATE_TIME_ASSIGNMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepDateTimeAssignment(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepDateAndTime.class,
            "DATE_TIME_ASSIGNMENT assigned_date_and_time must reference DATE_AND_TIME"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepDateTimeRole.class,
            "DATE_TIME_ASSIGNMENT role must reference DATE_TIME_ROLE"));
  }

  StepDateTimeEffectivity resolveDateTimeEffectivity(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DATE_TIME_EFFECTIVITY");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepDateTimeEffectivity(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)));
  }

  StepDateTimeRole resolveDateTimeRole(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DATE_TIME_ROLE");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepDateTimeRole(instance.id(), resolver.stringValue(instance, definition, 0));
  }

  // === Contract / Certification / Security Entities ===

  StepAppliedCertificationAssignment resolveAppliedCertificationAssignment(StepEntityInstance instance) {
    return resolveAppliedCertificationAssignment(instance, "APPLIED_CERTIFICATION_ASSIGNMENT");
  }

  StepAppliedCertificationAssignment resolveAppliedCertificationAssignment(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepAppliedCertificationAssignment(
        instance.id(),
        entityName,
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepCertification.class,
            entityName + " assigned_certification must reference CERTIFICATION"),
        resolver.entityReferenceList(
            instance,
            definition,
            1,
            entityName + " items must contain entity references"));
  }

  StepAppliedContractAssignment resolveAppliedContractAssignment(StepEntityInstance instance) {
    return resolveAppliedContractAssignment(instance, "APPLIED_CONTRACT_ASSIGNMENT");
  }

  StepAppliedContractAssignment resolveAppliedContractAssignment(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepAppliedContractAssignment(
        instance.id(),
        entityName,
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepContract.class,
            entityName + " assigned_contract must reference CONTRACT"),
        resolver.entityReferenceList(
            instance,
            definition,
            1,
            entityName + " items must contain entity references"));
  }

  StepCertification resolveCertification(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CERTIFICATION");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepCertification(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepCertificationType.class,
            "CERTIFICATION kind must reference CERTIFICATION_TYPE"));
  }

  StepCertificationAssignment resolveCertificationAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CERTIFICATION_ASSIGNMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepCertificationAssignment(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepCertification.class,
            "CERTIFICATION_ASSIGNMENT assigned_certification must reference CERTIFICATION"));
  }

  StepCertificationType resolveCertificationType(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CERTIFICATION_TYPE");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepCertificationType(instance.id(), resolver.stringValue(instance, definition, 0));
  }

  StepContract resolveContract(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CONTRACT");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepContract(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepContractType.class,
            "CONTRACT kind must reference CONTRACT_TYPE"));
  }

  StepContractAssignment resolveContractAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CONTRACT_ASSIGNMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepContractAssignment(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepContract.class,
            "CONTRACT_ASSIGNMENT assigned_contract must reference CONTRACT"));
  }

  StepContractType resolveContractType(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CONTRACT_TYPE");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepContractType(instance.id(), resolver.stringValue(instance, definition, 0));
  }

  StepSecurityClassification resolveSecurityClassification(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SECURITY_CLASSIFICATION");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepSecurityClassification(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepSecurityClassificationLevel.class,
            "SECURITY_CLASSIFICATION security_level must reference SECURITY_CLASSIFICATION_LEVEL"));
  }

  StepSecurityClassificationAssignment resolveSecurityClassificationAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SECURITY_CLASSIFICATION_ASSIGNMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepSecurityClassificationAssignment(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepSecurityClassification.class,
            "SECURITY_CLASSIFICATION_ASSIGNMENT assigned_security_classification must reference SECURITY_CLASSIFICATION"));
  }

  StepSecurityClassificationLevel resolveSecurityClassificationLevel(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SECURITY_CLASSIFICATION_LEVEL");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepSecurityClassificationLevel(instance.id(), resolver.stringValue(instance, definition, 0));
  }

  // === Classification / Identification / Name Entities ===

  StepAppliedClassificationAssignment resolveAppliedClassificationAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "APPLIED_CLASSIFICATION_ASSIGNMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepAppliedClassificationAssignment(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepGroup.class,
            "APPLIED_CLASSIFICATION_ASSIGNMENT assigned_class must reference GROUP"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepClassificationRole.class,
            "APPLIED_CLASSIFICATION_ASSIGNMENT role must reference CLASSIFICATION_ROLE"),
        resolver.entityReferenceList(
            instance,
            definition,
            2,
            "APPLIED_CLASSIFICATION_ASSIGNMENT items must contain entity references"));
  }

  StepAppliedIdentificationAssignment resolveAppliedIdentificationAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "APPLIED_IDENTIFICATION_ASSIGNMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepAppliedIdentificationAssignment(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepIdentificationRole.class,
            "APPLIED_IDENTIFICATION_ASSIGNMENT role must reference IDENTIFICATION_ROLE"),
        resolver.entityReferenceList(
            instance,
            definition,
            2,
            "APPLIED_IDENTIFICATION_ASSIGNMENT items must contain entity references"));
  }

  StepAppliedNameAssignment resolveAppliedNameAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "APPLIED_NAME_ASSIGNMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepAppliedNameAssignment(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.entityReferenceList(
            instance,
            definition,
            1,
            "APPLIED_NAME_ASSIGNMENT items must contain entity references"));
  }

  StepClassificationAssignment resolveClassificationAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CLASSIFICATION_ASSIGNMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepClassificationAssignment(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepGroup.class,
            "CLASSIFICATION_ASSIGNMENT assigned_class must reference GROUP"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepClassificationRole.class,
            "CLASSIFICATION_ASSIGNMENT role must reference CLASSIFICATION_ROLE"));
  }

  StepClassificationRole resolveClassificationRole(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CLASSIFICATION_ROLE");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepClassificationRole(instance.id(), resolver.stringValue(instance, definition, 0));
  }

  StepExternalIdentificationAssignment resolveExternalIdentificationAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "EXTERNAL_IDENTIFICATION_ASSIGNMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepExternalIdentificationAssignment(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepIdentificationRole.class,
            "EXTERNAL_IDENTIFICATION_ASSIGNMENT role must reference IDENTIFICATION_ROLE"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepExternalSource.class,
            "EXTERNAL_IDENTIFICATION_ASSIGNMENT source must reference EXTERNAL_SOURCE"));
  }

  StepIdentificationAssignment resolveIdentificationAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "IDENTIFICATION_ASSIGNMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepIdentificationAssignment(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepIdentificationRole.class,
            "IDENTIFICATION_ASSIGNMENT role must reference IDENTIFICATION_ROLE"));
  }

  StepIdentificationRole resolveIdentificationRole(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "IDENTIFICATION_ROLE");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepIdentificationRole(instance.id(), resolver.stringValue(instance, definition, 0));
  }

  StepNameAssignment resolveNameAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "NAME_ASSIGNMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepNameAssignment(instance.id(), resolver.stringValue(instance, definition, 0));
  }

  // === Effectivity Entities ===

  StepEffectivity resolveEffectivity(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "EFFECTIVITY");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepEffectivity(instance.id(), resolver.stringValue(instance, definition, 0));
  }

  StepEffectivityRelationship resolveEffectivityRelationship(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "EFFECTIVITY_RELATIONSHIP");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepEffectivityRelationship(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepEffectivity.class,
            "EFFECTIVITY_RELATIONSHIP relating_effectivity must reference EFFECTIVITY"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepEffectivity.class,
            "EFFECTIVITY_RELATIONSHIP related_effectivity must reference EFFECTIVITY"));
  }

  StepLotEffectivity resolveLotEffectivity(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "LOT_EFFECTIVITY");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepLotEffectivity(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)));
  }

  StepSerialNumberEffectivity resolveSerialNumberEffectivity(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SERIAL_NUMBER_EFFECTIVITY");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepSerialNumberEffectivity(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1));
  }
  // === Applied Assignment Entities ===

  StepAppliedExternalIdentificationAssignment resolveAppliedExternalIdentificationAssignment(StepEntityInstance instance) {
    StepEntityDefinition definition =
        resolver.definition(instance, "APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepAppliedExternalIdentificationAssignment(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepIdentificationRole.class,
            "APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT role must reference IDENTIFICATION_ROLE"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepExternalSource.class,
            "APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT source must reference EXTERNAL_SOURCE"),
        resolver.entityReferenceList(
            instance,
            definition,
            3,
            "APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT items must contain entity references"));
  }

  StepAppliedPersonAndOrganizationAssignment resolveAppliedPersonAndOrganizationAssignment(StepEntityInstance instance) {
    return resolveAppliedPersonAndOrganizationAssignment(
        instance, "APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT");
  }

  StepAppliedPersonAndOrganizationAssignment resolveAppliedPersonAndOrganizationAssignment(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepAppliedPersonAndOrganizationAssignment(
        instance.id(),
        entityName,
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepPersonAndOrganization.class,
            entityName + " assigned_person_and_organization must reference PERSON_AND_ORGANIZATION"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepPersonAndOrganizationRole.class,
            entityName + " role must reference PERSON_AND_ORGANIZATION_ROLE"),
        resolver.entityReferenceList(
            instance,
            definition,
            2,
            entityName + " items must contain entity references"));
  }

  StepAppliedSecurityClassificationAssignment resolveAppliedSecurityClassificationAssignment(StepEntityInstance instance) {
    return resolveAppliedSecurityClassificationAssignment(
        instance, "APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT");
  }

  StepAppliedSecurityClassificationAssignment resolveAppliedSecurityClassificationAssignment(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepAppliedSecurityClassificationAssignment(
        instance.id(),
        entityName,
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepSecurityClassification.class,
            entityName + " assigned_security_classification must reference SECURITY_CLASSIFICATION"),
        resolver.entityReferenceList(
            instance,
            definition,
            1,
            entityName + " items must contain entity references"));
  }
}
