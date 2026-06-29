package com.minicad.step.semantic;

import java.util.Map;

/**
 * Registry for manufacturing entity types.
 * Extracted from MiscRegistry.java during refactoring.
 */
public final class ManufacturingRegistry {

  private ManufacturingRegistry() {}

  public static void register(Map<String, EntityFactory> registry) {
// Entity: SIMPLIFIED_COUNTERBORE_HOLE_DEFINITION
      registry.put(
          "SIMPLIFIED_COUNTERBORE_HOLE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "SIMPLIFIED_COUNTERBORE_HOLE_DEFINITION", false));

// Entity: SIMPLIFIED_COUNTERDRILL_HOLE_DEFINITION
      registry.put(
          "SIMPLIFIED_COUNTERDRILL_HOLE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "SIMPLIFIED_COUNTERDRILL_HOLE_DEFINITION", false));

// Entity: SIMPLIFIED_COUNTERSINK_HOLE_DEFINITION
      registry.put(
          "SIMPLIFIED_COUNTERSINK_HOLE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveRepresentation(
                  instance, "SIMPLIFIED_COUNTERSINK_HOLE_DEFINITION", false));

// Entity: CHAMFER
      registry.put(
          "CHAMFER",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "CHAMFER"));

// Entity: CHAMFER_OFFSET
      registry.put(
          "CHAMFER_OFFSET",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "CHAMFER_OFFSET"));

// Entity: FILLET
      registry.put(
          "FILLET",
          (resolver, instance) -> resolver.resolveShapeAspect(instance, "FILLET"));

// Entity: PATTERN_OFFSET_MEMBERSHIP
      registry.put(
          "PATTERN_OFFSET_MEMBERSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "PATTERN_OFFSET_MEMBERSHIP"));

// Entity: PATTERN_OMIT_MEMBERSHIP
      registry.put(
          "PATTERN_OMIT_MEMBERSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "PATTERN_OMIT_MEMBERSHIP"));

// Entity: HOLE_BOTTOM
      registry.put(
          "HOLE_BOTTOM",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "HOLE_BOTTOM"));

// Entity: MODIFIED_PATTERN
      registry.put(
          "MODIFIED_PATTERN",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "MODIFIED_PATTERN"));

// Entity: ATTACHMENT_SLOT_AS_PLANNED
      registry.put(
          "ATTACHMENT_SLOT_AS_PLANNED",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ATTACHMENT_SLOT_AS_PLANNED"));

// Entity: ATTACHMENT_SLOT_AS_REALIZED
      registry.put(
          "ATTACHMENT_SLOT_AS_REALIZED",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ATTACHMENT_SLOT_AS_REALIZED"));

// Entity: ATTACHMENT_SLOT_DESIGN
      registry.put(
          "ATTACHMENT_SLOT_DESIGN",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ATTACHMENT_SLOT_DESIGN"));

// Entity: ATTACHMENT_SLOT_DESIGN_TO_PLANNED
      registry.put(
          "ATTACHMENT_SLOT_DESIGN_TO_PLANNED",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ATTACHMENT_SLOT_DESIGN_TO_PLANNED"));

// Entity: ATTACHMENT_SLOT_DESIGN_TO_REALIZED
      registry.put(
          "ATTACHMENT_SLOT_DESIGN_TO_REALIZED",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ATTACHMENT_SLOT_DESIGN_TO_REALIZED"));

// Entity: ATTACHMENT_SLOT_ON_PRODUCT
      registry.put(
          "ATTACHMENT_SLOT_ON_PRODUCT",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ATTACHMENT_SLOT_ON_PRODUCT"));

// Entity: ATTACHMENT_SLOT_PLANNED_TO_REALIZED
      registry.put(
          "ATTACHMENT_SLOT_PLANNED_TO_REALIZED",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "ATTACHMENT_SLOT_PLANNED_TO_REALIZED"));

// Entity: FILLET_DEFINITION
      registry.put("FILLET_DEFINITION", StepEntityResolver::resolveFilletDefinition);

// Entity: CHAMFER_DEFINITION
      registry.put("CHAMFER_DEFINITION", StepEntityResolver::resolveChamferDefinition);

// Entity: PATTERN
      registry.put("PATTERN", StepEntityResolver::resolvePattern);

// Entity: FLAT_PATTERN
      registry.put("FLAT_PATTERN", StepEntityResolver::resolveFlatPattern);

// Entity: THREAD
      registry.put("THREAD", StepEntityResolver::resolveThread);


  }
}
