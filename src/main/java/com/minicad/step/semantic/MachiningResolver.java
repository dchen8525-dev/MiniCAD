package com.minicad.step.semantic;

import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;

import java.util.List;

/**
 * Machining resolver - handles machining process and thread feature entities.
 * Extracted from StepEntityResolver to reduce file size and improve maintainability.
 * Contains machining operations, operation sequences, work plans,
 * process plans, and thread features.
 */
final class MachiningResolver {

  private final StepEntityResolver resolver;

  MachiningResolver(StepEntityResolver resolver) {
    this.resolver = resolver;
  }

  // === Machining Operation Entities ===

  StepMachiningOperation resolveMachiningOperation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MACHINING_OPERATION");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    List<StepEntity> features =
        resolver.entityReferenceList(
            instance, definition, 3,
            "MACHINING_OPERATION features must contain entity references");
    return new StepMachiningOperation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        features);
  }

  StepMachiningOperationSequence resolveMachiningOperationSequence(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MACHINING_OPERATION_SEQUENCE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    List<StepEntity> operations =
        resolver.entityReferenceList(
            instance, definition, 2,
            "MACHINING_OPERATION_SEQUENCE operations must contain entity references");
    return new StepMachiningOperationSequence(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        operations,
        resolver.stringValue(instance, definition, 3));
  }

  // === Machining Plan Entities ===

  StepMachiningProcessPlan resolveMachiningProcessPlan(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MACHINING_PROCESS_PLAN");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    List<StepEntity> items =
        resolver.entityReferenceList(
            instance, definition, 2,
            "MACHINING_PROCESS_PLAN items must contain entity references");
    List<StepEntity> operations =
        resolver.entityReferenceList(
            instance, definition, 4,
            "MACHINING_PROCESS_PLAN operations must contain entity references");
    return new StepMachiningProcessPlan(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        items,
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        operations);
  }

  StepMachiningWorkPlan resolveMachiningWorkPlan(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MACHINING_WORK_PLAN");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    List<StepEntity> items =
        resolver.entityReferenceList(
            instance, definition, 2,
            "MACHINING_WORK_PLAN items must contain entity references");
    List<StepEntity> machiningSetup =
        resolver.entityReferenceList(
            instance, definition, 4,
            "MACHINING_WORK_PLAN machining_setup must contain entity references");
    return new StepMachiningWorkPlan(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        items,
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        machiningSetup);
  }

  // === Machining Feature Entities ===

  StepThread resolveThread(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "THREAD");
    StepEntityResolver.requireParameterCountIn(instance, definition, 2, 5, 6);
    int paramCount = definition.parameters().size();
    if (paramCount == 2) {
      // Minimal form: name, description
      return new StepThread(
          instance.id(),
          resolver.stringValue(instance, definition, 0),
          null, null, null, null);
    }
    return new StepThread(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        paramCount > 1 ? resolver.optionalNumberValue(instance, definition, 1) : null,
        paramCount > 2 ? resolver.optionalNumberValue(instance, definition, 2) : null,
        paramCount > 3 ? resolver.stringValue(instance, definition, 3) : null,
        paramCount > 4 ? resolver.optionalNumberValue(instance, definition, 4) : null);
  }
}
