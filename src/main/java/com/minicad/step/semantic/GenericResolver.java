package com.minicad.step.semantic;

import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;

import java.util.List;

/**
 * Generic resolver - handles generic expression, assignment, and property entities.
 * Extracted from StepEntityResolver to reduce file size and improve maintainability.
 * Contains simple/unary/binary/multiple-arity generic expressions and generic
 * assignment, relationship, requirement, status, property, setup, type, role entities.
 */
final class GenericResolver {

  private final StepEntityResolver resolver;

  GenericResolver(StepEntityResolver resolver) {
    this.resolver = resolver;
  }

  // === Generic Expression Entities ===

  StepBinaryGenericExpression resolveBinaryGenericExpression(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepBinaryGenericExpression(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.literalList(instance, definition, 1),
        entityName);
  }

  StepMultipleArityGenericExpression resolveMultipleArityGenericExpression(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepMultipleArityGenericExpression(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.literalList(instance, definition, 1),
        entityName);
  }

  StepSimpleGenericExpression resolveSimpleGenericExpression(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepSimpleGenericExpression(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        entityName);
  }

  StepUnaryGenericExpression resolveUnaryGenericExpression(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepUnaryGenericExpression(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        entityName);
  }

  // === Generic Assignment / Property Entities ===

  StepEntity resolveGenericActual(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepGenericEntity(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        entityName);
  }

  StepEntity resolveGenericAssignment(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    // Support 0-parameter (parent entity in complex instance), 2-parameter, and 3-parameter forms
    StepEntityResolver.requireParameterCountIn(instance, definition, 0, 2, 3);
    if (definition.parameters().size() == 0) {
      // Parent entity in complex instance, no parameters
      return new StepGenericEntity(instance.id(), "", entityName);
    } else if (definition.parameters().size() == 2) {
      return new StepGenericEntity(
          instance.id(),
          resolver.stringValue(instance, definition, 0),
          resolver.resolve(resolver.referenceId(instance, definition, 1)),
          entityName);
    } else {
      // 3-parameter form: name, items list, context reference
      List<StepEntity> items =
          resolver.entityReferenceList(
              instance, definition, 1, entityName + " items must contain entity references");
      StepEntity context = resolver.resolve(resolver.referenceId(instance, definition, 2));
      return new StepRepresentation(
          instance.id(),
          resolver.stringValue(instance, definition, 0),
          items,
          context,
          false,
          entityName);
    }
  }

  StepEntity resolveGenericProperty(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    // Support both 2-parameter (name, ref) and 3-parameter (name, items, context) forms
    StepEntityResolver.requireParameterCountIn(instance, definition, 2, 3);
    if (definition.parameters().size() == 2) {
      return new StepGenericEntity(
          instance.id(),
          resolver.stringValue(instance, definition, 0),
          resolver.resolve(resolver.referenceId(instance, definition, 1)),
          entityName);
    } else {
      // 3-parameter form: name, items list, context reference
      List<StepEntity> items =
          resolver.entityReferenceList(
              instance, definition, 1, entityName + " items must contain entity references");
      StepEntity context = resolver.resolve(resolver.referenceId(instance, definition, 2));
      return new StepRepresentation(
          instance.id(),
          resolver.stringValue(instance, definition, 0),
          items,
          context,
          false,
          entityName);
    }
  }

  StepEntity resolveGenericRelationship(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    String description = resolver.optionalStringValue(instance, definition, 1);
    return new StepGenericEntity(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        description,
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        entityName);
  }

  StepEntity resolveGenericRequirement(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepGenericEntity(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        entityName);
  }

  StepEntity resolveGenericRole(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepGenericEntity(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        entityName);
  }

  StepEntity resolveGenericSetup(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepGenericEntity(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        entityName);
  }

  StepEntity resolveGenericStatus(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepGenericEntity(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        entityName);
  }

  StepEntity resolveGenericType(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepGenericEntity(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        entityName);
  }
}
