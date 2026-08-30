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
      registry.put("FILLET_DEFINITION", (resolver, instance) -> resolver.geometricFeatureResolver.resolveFilletDefinition(instance));

// Entity: CHAMFER_DEFINITION
      registry.put("CHAMFER_DEFINITION", (resolver, instance) -> resolver.geometricFeatureResolver.resolveChamferDefinition(instance));

// Entity: PATTERN
      registry.put("PATTERN", StepEntityResolver::resolvePattern);

// Entity: FLAT_PATTERN
      registry.put("FLAT_PATTERN", StepEntityResolver::resolveFlatPattern);

// Entity: THREAD
      registry.put("THREAD", (resolver, instance) -> resolver.machiningResolver.resolveThread(instance));

// Entity: SPOTFACE_HOLE_OCCURRENCE
      registry.put(
          "SPOTFACE_HOLE_OCCURRENCE",
          (resolver, instance) ->
              resolver.resolveShapeAspectOccurrence(instance, "SPOTFACE_HOLE_OCCURRENCE"));

// Entity: SPOTFACE_HOLE_DEFINITION
      registry.put(
          "SPOTFACE_HOLE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "SPOTFACE_HOLE_DEFINITION"));

// Entity: COUNTERBORE_HOLE_OCCURRENCE (SHAPE_ASPECT_OCCURRENCE subtype, 5 params)
      registry.put(
          "COUNTERBORE_HOLE_OCCURRENCE",
          (resolver, instance) ->
              resolver.resolveShapeAspectOccurrence(instance, "COUNTERBORE_HOLE_OCCURRENCE"));

// Entity: COUNTERDRILL_HOLE_OCCURRENCE (SHAPE_ASPECT_OCCURRENCE subtype, 5 params)
      registry.put(
          "COUNTERDRILL_HOLE_OCCURRENCE",
          (resolver, instance) ->
              resolver.resolveShapeAspectOccurrence(instance, "COUNTERDRILL_HOLE_OCCURRENCE"));

// Entity: COUNTERSINK_HOLE_OCCURRENCE (SHAPE_ASPECT_OCCURRENCE subtype, 5 params)
      registry.put(
          "COUNTERSINK_HOLE_OCCURRENCE",
          (resolver, instance) ->
              resolver.resolveShapeAspectOccurrence(instance, "COUNTERSINK_HOLE_OCCURRENCE"));

// Entity: FEATURE_DEFINITION (minimal 2-param definition)
      registry.put(
          "FEATURE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "FEATURE_DEFINITION"));

// Entity: ADDITIVE_MANUFACTURING_FEATURE (minimal 2-param definition)
      registry.put(
          "ADDITIVE_MANUFACTURING_FEATURE",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "ADDITIVE_MANUFACTURING_FEATURE"));

// Entity: BARRING_HOLE (minimal 2-param definition)
      registry.put(
          "BARRING_HOLE",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "BARRING_HOLE"));

// Entity: BEAD (minimal 2-param definition)
      registry.put(
          "BEAD",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "BEAD"));

// Entity: BOSS (minimal 2-param definition)
      registry.put(
          "BOSS",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "BOSS"));

// Entity: CIRCULAR_PATTERN (minimal 2-param definition)
      registry.put(
          "CIRCULAR_PATTERN",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "CIRCULAR_PATTERN"));

// Entity: COMPOUND_FEATURE (minimal 2-param definition)
      registry.put(
          "COMPOUND_FEATURE",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "COMPOUND_FEATURE"));

// Entity: COMPOSITE_HOLE (minimal 2-param definition)
      registry.put(
          "COMPOSITE_HOLE",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "COMPOSITE_HOLE"));

// Entity: CONTACT_FEATURE_DEFINITION (minimal 2-param definition)
      registry.put(
          "CONTACT_FEATURE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "CONTACT_FEATURE_DEFINITION"));

// Entity: EXPLICIT_COMPOSITE_HOLE (minimal 2-param definition)
      registry.put(
          "EXPLICIT_COMPOSITE_HOLE",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "EXPLICIT_COMPOSITE_HOLE"));

// Entity: EXPLICIT_ROUND_HOLE (minimal 2-param definition)
      registry.put(
          "EXPLICIT_ROUND_HOLE",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "EXPLICIT_ROUND_HOLE"));

// Entity: EXTERNALLY_DEFINED_FEATURE_DEFINITION (minimal 2-param definition)
      registry.put(
          "EXTERNALLY_DEFINED_FEATURE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "EXTERNALLY_DEFINED_FEATURE_DEFINITION"));

// Entity: FEATURE_DEFINITION_WITH_CONNECTION_AREA (minimal 2-param definition)
      registry.put(
          "FEATURE_DEFINITION_WITH_CONNECTION_AREA",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "FEATURE_DEFINITION_WITH_CONNECTION_AREA"));

// Entity: FEATURE_IN_PANEL (minimal 2-param definition)
      registry.put(
          "FEATURE_IN_PANEL",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "FEATURE_IN_PANEL"));

// Entity: FEATURE_PATTERN (minimal 2-param definition)
      registry.put(
          "FEATURE_PATTERN",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "FEATURE_PATTERN"));

// Entity: FLAT_FACE (minimal 2-param definition)
      registry.put(
          "FLAT_FACE",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "FLAT_FACE"));

// Entity: GEAR (minimal 2-param definition)
      registry.put(
          "GEAR",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "GEAR"));

// Entity: GENERAL_FEATURE (minimal 2-param definition)
      registry.put(
          "GENERAL_FEATURE",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "GENERAL_FEATURE"));

// Entity: HOLE_IN_PANEL (minimal 2-param definition)
      registry.put(
          "HOLE_IN_PANEL",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "HOLE_IN_PANEL"));

// Entity: JOGGLE (minimal 2-param definition)
      registry.put(
          "JOGGLE",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "JOGGLE"));

// Entity: LOCATOR (minimal 2-param definition)
      registry.put(
          "LOCATOR",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "LOCATOR"));

// Entity: MARKING (minimal 2-param definition)
      registry.put(
          "MARKING",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "MARKING"));

// Entity: OUTER_ROUND (minimal 2-param definition)
      registry.put(
          "OUTER_ROUND",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "OUTER_ROUND"));

// Entity: OUTSIDE_PROFILE (minimal 2-param definition)
      registry.put(
          "OUTSIDE_PROFILE",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "OUTSIDE_PROFILE"));

// Entity: POCKET (minimal 2-param definition)
      registry.put(
          "POCKET",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "POCKET"));

// Entity: REMOVAL_VOLUME (minimal 2-param definition)
      registry.put(
          "REMOVAL_VOLUME",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "REMOVAL_VOLUME"));

// Entity: REPLICATE_FEATURE (minimal 2-param definition)
      registry.put(
          "REPLICATE_FEATURE",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "REPLICATE_FEATURE"));

// Entity: REVOLVED_PROFILE (minimal 2-param definition)
      registry.put(
          "REVOLVED_PROFILE",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "REVOLVED_PROFILE"));

// Entity: RIB_TOP (minimal 2-param definition)
      registry.put(
          "RIB_TOP",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "RIB_TOP"));

// Entity: ROUND_HOLE (minimal 2-param definition)
      registry.put(
          "ROUND_HOLE",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "ROUND_HOLE"));

// Entity: ROUNDED_END (minimal 2-param definition)
      registry.put(
          "ROUNDED_END",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "ROUNDED_END"));

// Entity: SHAPE_FEATURE_DEFINITION (minimal 2-param definition)
      registry.put(
          "SHAPE_FEATURE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "SHAPE_FEATURE_DEFINITION"));

// Entity: SLOT (minimal 2-param definition)
      registry.put(
          "SLOT",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "SLOT"));

// Entity: PROTRUSION (minimal 2-param definition)
      registry.put(
          "PROTRUSION",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "PROTRUSION"));

// Entity: RECTANGULAR_PATTERN (minimal 2-param definition)
      registry.put(
          "RECTANGULAR_PATTERN",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "RECTANGULAR_PATTERN"));

// Entity: RIB (minimal 2-param definition)
      registry.put(
          "RIB",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "RIB"));

// Entity: SPHERICAL_CAP (minimal 2-param definition)
      registry.put(
          "SPHERICAL_CAP",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "SPHERICAL_CAP"));

// Entity: SPOTFACE_DEFINITION (minimal 2-param definition)
      registry.put(
          "SPOTFACE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "SPOTFACE_DEFINITION"));

// Entity: SPOTFACE_HOLE_DEFINITION (minimal 2-param definition)
      registry.put(
          "SPOTFACE_HOLE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "SPOTFACE_HOLE_DEFINITION"));

// Entity: TURNED_KNURL (minimal 2-param definition)
      registry.put(
          "TURNED_KNURL",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "TURNED_KNURL"));

// Entity: BASIC_ROUND_HOLE_OCCURRENCE
      registry.put(
          "BASIC_ROUND_HOLE_OCCURRENCE",
          (resolver, instance) ->
              resolver.resolveShapeAspectOccurrence(instance, "BASIC_ROUND_HOLE_OCCURRENCE"));

// Entity: BASIC_ROUND_HOLE_DEFINITION (minimal 2-param definition)
      registry.put(
          "BASIC_ROUND_HOLE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "BASIC_ROUND_HOLE_DEFINITION"));

// Entity: BASIC_ROUND_HOLE (minimal 2-param definition)
      registry.put(
          "BASIC_ROUND_HOLE",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "BASIC_ROUND_HOLE"));

// Entity: COUNTERBORE_HOLE_DEFINITION (minimal 2-param definition)
      registry.put(
          "COUNTERBORE_HOLE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "COUNTERBORE_HOLE_DEFINITION"));

// Entity: COUNTERDRILL_HOLE_DEFINITION (minimal 2-param definition)
      registry.put(
          "COUNTERDRILL_HOLE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "COUNTERDRILL_HOLE_DEFINITION"));

// Entity: COUNTERSINK_HOLE_DEFINITION (minimal 2-param definition)
      registry.put(
          "COUNTERSINK_HOLE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveCharacterizedObject(instance, "COUNTERSINK_HOLE_DEFINITION"));

// NOTE: SLOT minimal definition registered earlier

// NOTE: BOSS minimal definition registered earlier

// NOTE: RECTANGULAR_PATTERN minimal definition registered earlier

// NOTE: PROTRUSION minimal definition registered earlier

// NOTE: FEATURE_PATTERN minimal definition registered earlier

// NOTE: RIB minimal definition registered earlier

// NOTE: BEAD minimal definition registered earlier, BEAD_END is SHAPE_ASPECT subtype

// Entity: BEAD_END (SHAPE_ASPECT subtype)
      registry.put(
          "BEAD_END",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "BEAD_END"));

// Entity: BOSS_TOP (SHAPE_ASPECT subtype)
      registry.put(
          "BOSS_TOP",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "BOSS_TOP"));

// Entity: COMPONENT_TERMINAL (SHAPE_ASPECT subtype)
      registry.put(
          "COMPONENT_TERMINAL",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "COMPONENT_TERMINAL"));

// Entity: CONSTITUENT_SHAPE_ASPECT (SHAPE_ASPECT subtype)
      registry.put(
          "CONSTITUENT_SHAPE_ASPECT",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "CONSTITUENT_SHAPE_ASPECT"));

// Entity: CONTACTING_FEATURE (SHAPE_ASPECT subtype)
      registry.put(
          "CONTACTING_FEATURE",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "CONTACTING_FEATURE"));

// Entity: DEFAULT_MODEL_GEOMETRIC_VIEW (SHAPE_ASPECT subtype)
      registry.put(
          "DEFAULT_MODEL_GEOMETRIC_VIEW",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "DEFAULT_MODEL_GEOMETRIC_VIEW"));

// Entity: GENERAL_DATUM_REFERENCE (SHAPE_ASPECT subtype)
      registry.put(
          "GENERAL_DATUM_REFERENCE",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "GENERAL_DATUM_REFERENCE"));

// Entity: GEOMETRIC_TOLERANCE_WITH_MODIFIERS (SHAPE_ASPECT subtype)
      registry.put(
          "GEOMETRIC_TOLERANCE_WITH_MODIFIERS",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "GEOMETRIC_TOLERANCE_WITH_MODIFIERS"));

// Entity: LAYOUT_SPACING_CONTEXTUAL_AREA (SHAPE_ASPECT subtype)
      registry.put(
          "LAYOUT_SPACING_CONTEXTUAL_AREA",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "LAYOUT_SPACING_CONTEXTUAL_AREA"));

// Entity: MATED_PART_RELATIONSHIP (SHAPE_ASPECT subtype)
      registry.put(
          "MATED_PART_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "MATED_PART_RELATIONSHIP"));

// Entity: MOUNTING_RESTRICTION_AREA (SHAPE_ASPECT subtype)
      registry.put(
          "MOUNTING_RESTRICTION_AREA",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "MOUNTING_RESTRICTION_AREA"));

// Entity: MOUNTING_RESTRICTION_VOLUME (SHAPE_ASPECT subtype)
      registry.put(
          "MOUNTING_RESTRICTION_VOLUME",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "MOUNTING_RESTRICTION_VOLUME"));

// Entity: PATH_FEATURE_COMPONENT (SHAPE_ASPECT subtype)
      registry.put(
          "PATH_FEATURE_COMPONENT",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "PATH_FEATURE_COMPONENT"));

// Entity: PHYSICAL_COMPONENT_FEATURE (SHAPE_ASPECT subtype)
      registry.put(
          "PHYSICAL_COMPONENT_FEATURE",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "PHYSICAL_COMPONENT_FEATURE"));

// Entity: PHYSICAL_COMPONENT_TERMINAL (SHAPE_ASPECT subtype)
      registry.put(
          "PHYSICAL_COMPONENT_TERMINAL",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "PHYSICAL_COMPONENT_TERMINAL"));

// NOTE: PROJECTED_ZONE_DEFINITION is registered in ToleranceRegistry with specialized resolver

// Entity: REFERENCE_GRAPHIC_REGISTRATION_MARK (SHAPE_ASPECT subtype)
      registry.put(
          "REFERENCE_GRAPHIC_REGISTRATION_MARK",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "REFERENCE_GRAPHIC_REGISTRATION_MARK"));

// Entity: SEATING_PLANE (SHAPE_ASPECT subtype)
      registry.put(
          "SEATING_PLANE",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "SEATING_PLANE"));

// Entity: TERMINAL_FEATURE (SHAPE_ASPECT subtype)
      registry.put(
          "TERMINAL_FEATURE",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "TERMINAL_FEATURE"));

// Entity: TERMINAL_LOCATION_GROUP (SHAPE_ASPECT subtype)
      registry.put(
          "TERMINAL_LOCATION_GROUP",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "TERMINAL_LOCATION_GROUP"));

// Entity: TOLERANCE_ZONE_DEFINITION (SHAPE_ASPECT subtype)
      registry.put(
          "TOLERANCE_ZONE_DEFINITION",
          (resolver, instance) ->
              resolver.resolveShapeAspect(instance, "TOLERANCE_ZONE_DEFINITION"));

// Relationship entities (SHAPE_ASPECT_RELATIONSHIP subtypes)
// Entity: ASSEMBLY_SHAPE_CONSTRAINT_ITEM_RELATIONSHIP
      registry.put(
          "ASSEMBLY_SHAPE_CONSTRAINT_ITEM_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "ASSEMBLY_SHAPE_CONSTRAINT_ITEM_RELATIONSHIP"));

// Entity: ASSEMBLY_SHAPE_JOINT_ITEM_RELATIONSHIP
      registry.put(
          "ASSEMBLY_SHAPE_JOINT_ITEM_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "ASSEMBLY_SHAPE_JOINT_ITEM_RELATIONSHIP"));

// Entity: COMPONENT_FEATURE_JOINT
      registry.put(
          "COMPONENT_FEATURE_JOINT",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "COMPONENT_FEATURE_JOINT"));

// Entity: COMPONENT_FEATURE_RELATIONSHIP_WITH_TRANSFORMATION
      registry.put(
          "COMPONENT_FEATURE_RELATIONSHIP_WITH_TRANSFORMATION",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "COMPONENT_FEATURE_RELATIONSHIP_WITH_TRANSFORMATION"));

// Entity: COMPONENT_MATING_CONSTRAINT_CONDITION
      registry.put(
          "COMPONENT_MATING_CONSTRAINT_CONDITION",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "COMPONENT_MATING_CONSTRAINT_CONDITION"));

// Entity: COMPONENT_PATH_SHAPE_ASPECT_RELATIONSHIP
      registry.put(
          "COMPONENT_PATH_SHAPE_ASPECT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "COMPONENT_PATH_SHAPE_ASPECT_RELATIONSHIP"));

// Entity: CONNECTION_ZONE_INTERFACE_PLANE_RELATIONSHIP
      registry.put(
          "CONNECTION_ZONE_INTERFACE_PLANE_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "CONNECTION_ZONE_INTERFACE_PLANE_RELATIONSHIP"));

// Entity: CONNECTIVITY_DEFINITION_ITEM_RELATIONSHIP
      registry.put(
          "CONNECTIVITY_DEFINITION_ITEM_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "CONNECTIVITY_DEFINITION_ITEM_RELATIONSHIP"));

// Entity: CONTACT_FEATURE_FIT_RELATIONSHIP
      registry.put(
          "CONTACT_FEATURE_FIT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "CONTACT_FEATURE_FIT_RELATIONSHIP"));

// Entity: DIMENSIONAL_LOCATION_WITH_DATUM_FEATURE
      registry.put(
          "DIMENSIONAL_LOCATION_WITH_DATUM_FEATURE",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "DIMENSIONAL_LOCATION_WITH_DATUM_FEATURE"));

// Entity: DIMENSIONAL_LOCATION_WITH_PATH
      registry.put(
          "DIMENSIONAL_LOCATION_WITH_PATH",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "DIMENSIONAL_LOCATION_WITH_PATH"));

// Entity: POSITIONED_SKETCH_TO_PART_ASSOCIATION
      registry.put(
          "POSITIONED_SKETCH_TO_PART_ASSOCIATION",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "POSITIONED_SKETCH_TO_PART_ASSOCIATION"));

// Entity: SHAPE_FEATURE_DEFINITION_ELEMENT_RELATIONSHIP
      registry.put(
          "SHAPE_FEATURE_DEFINITION_ELEMENT_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, "SHAPE_FEATURE_DEFINITION_ELEMENT_RELATIONSHIP"));

  }
}
