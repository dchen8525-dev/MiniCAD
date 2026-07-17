package com.minicad.step.semantic;

import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;


/**
 * Property resolver - handles property definition and group entities.
 * Extracted from StepEntityResolver to reduce file size and improve maintainability.
 * Contains property definitions and relationships, general properties,
 * groups and group relationships, and attribute classifications.
 */
final class PropertyResolver {

  private final StepEntityResolver resolver;

  PropertyResolver(StepEntityResolver resolver) {
    this.resolver = resolver;
  }

  // === Property Definition Entities ===

  StepAppliedAttributeClassification resolveAppliedAttributeClassification(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "APPLIED_ATTRIBUTE_CLASSIFICATION");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepAppliedAttributeClassification(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepGeneralProperty resolveGeneralProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "GENERAL_PROPERTY");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepGeneralProperty(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.optionalStringValue(instance, definition, 2));
  }

  StepGeneralPropertyRelationship resolveGeneralPropertyRelationship(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "GENERAL_PROPERTY_RELATIONSHIP");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepGeneralPropertyRelationship(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepGeneralProperty.class,
            "GENERAL_PROPERTY_RELATIONSHIP relating_general_property must reference GENERAL_PROPERTY"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepGeneralProperty.class,
            "GENERAL_PROPERTY_RELATIONSHIP related_general_property must reference GENERAL_PROPERTY"));
  }

  StepPropertyDefinition resolvePropertyDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PROPERTY_DEFINITION");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepPropertyDefinition(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepPropertyDefinitionRelationship resolvePropertyDefinitionRelationship(StepEntityInstance instance) {
    return resolvePropertyDefinitionRelationship(instance, "PROPERTY_DEFINITION_RELATIONSHIP");
  }

  StepPropertyDefinitionRelationship resolvePropertyDefinitionRelationship(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepPropertyDefinitionRelationship(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepPropertyDefinition.class,
            entityName + " relating_property_definition must reference PROPERTY_DEFINITION"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepPropertyDefinition.class,
            entityName + " related_property_definition must reference PROPERTY_DEFINITION"),
        entityName);
  }

  // === Group Entities ===

  StepGroup resolveGroup(StepEntityInstance instance) {
    return resolveGroup(instance, "GROUP");
  }

  StepGroup resolveGroup(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepGroup(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1),
        entityName);
  }

  StepGroupRelationship resolveGroupRelationship(StepEntityInstance instance) {
    return resolveGroupRelationship(instance, "GROUP_RELATIONSHIP");
  }

  StepGroupRelationship resolveGroupRelationship(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepGroupRelationship(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepGroup.class,
            entityName + " relating_group must reference GROUP"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepGroup.class,
            entityName + " related_group must reference GROUP"),
        entityName);
  }

  StepPmiGroup resolvePmiGroup(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PMI_GROUP");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepPmiGroup(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.entityReferenceList(instance, definition, 1,
            "PMI_GROUP members must contain entity references"));
  }
}
