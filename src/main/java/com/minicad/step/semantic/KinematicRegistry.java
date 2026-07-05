package com.minicad.step.semantic;

import java.util.Map;

/**
 * Registry for kinematic entity types.
 * Extracted from MiscRegistry.java during refactoring.
 */
public final class KinematicRegistry {

  private KinematicRegistry() {}

  public static void register(Map<String, EntityFactory> registry) {
// Entity: KINEMATIC_TOPOLOGY_DIRECTED_STRUCTURE
      registry.put(
          "KINEMATIC_TOPOLOGY_DIRECTED_STRUCTURE",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "KINEMATIC_TOPOLOGY_DIRECTED_STRUCTURE", false));

// Entity: KINEMATIC_TOPOLOGY_NETWORK_STRUCTURE
      registry.put(
          "KINEMATIC_TOPOLOGY_NETWORK_STRUCTURE",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "KINEMATIC_TOPOLOGY_NETWORK_STRUCTURE", false));

// Entity: KINEMATIC_TOPOLOGY_STRUCTURE
      registry.put(
          "KINEMATIC_TOPOLOGY_STRUCTURE",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "KINEMATIC_TOPOLOGY_STRUCTURE", false));

// Entity: KINEMATIC_TOPOLOGY_SUBSTRUCTURE
      registry.put(
          "KINEMATIC_TOPOLOGY_SUBSTRUCTURE",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "KINEMATIC_TOPOLOGY_SUBSTRUCTURE", false));

// Entity: KINEMATIC_TOPOLOGY_TREE_STRUCTURE
      registry.put(
          "KINEMATIC_TOPOLOGY_TREE_STRUCTURE",
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, "KINEMATIC_TOPOLOGY_TREE_STRUCTURE", false));

// Entity: ITEM_LINK_MOTION_RELATIONSHIP
      registry.put(
          "ITEM_LINK_MOTION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "ITEM_LINK_MOTION_RELATIONSHIP"));

// Entity: LINK_MOTION_RELATIONSHIP
      registry.put(
          "LINK_MOTION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveGenericRelationship(instance, "LINK_MOTION_RELATIONSHIP"));

// Entity: DATA_EQUIVALENCE_INSPECTED_ELEMENT_PAIR
      registry.put(
          "DATA_EQUIVALENCE_INSPECTED_ELEMENT_PAIR",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "DATA_EQUIVALENCE_INSPECTED_ELEMENT_PAIR"));

// Entity: CYLINDRICAL_PAIR_VALUE
      registry.put(
          "CYLINDRICAL_PAIR_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CYLINDRICAL_PAIR_VALUE"));

// Entity: CYLINDRICAL_PAIR_WITH_RANGE
      registry.put(
          "CYLINDRICAL_PAIR_WITH_RANGE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "CYLINDRICAL_PAIR_WITH_RANGE"));

// Entity: PLANAR_PAIR_VALUE
      registry.put(
          "PLANAR_PAIR_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PLANAR_PAIR_VALUE"));

// Entity: PLANAR_PAIR_WITH_RANGE
      registry.put(
          "PLANAR_PAIR_WITH_RANGE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "PLANAR_PAIR_WITH_RANGE"));

// Entity: FULLY_CONSTRAINED_PAIR
      registry.put(
          "FULLY_CONSTRAINED_PAIR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "FULLY_CONSTRAINED_PAIR"));

// Entity: GEAR_PAIR_VALUE
      registry.put(
          "GEAR_PAIR_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "GEAR_PAIR_VALUE"));

// Entity: HIGH_ORDER_KINEMATIC_PAIR
      registry.put(
          "HIGH_ORDER_KINEMATIC_PAIR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "HIGH_ORDER_KINEMATIC_PAIR"));

// Entity: HOMOKINETIC_PAIR
      registry.put(
          "HOMOKINETIC_PAIR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "HOMOKINETIC_PAIR"));

// Entity: LINK_MOTION_TRANSFORMATION
      registry.put(
          "LINK_MOTION_TRANSFORMATION",
          (resolver, instance) ->
              resolver.resolveGenericRequirement(instance, "LINK_MOTION_TRANSFORMATION"));

// Entity: LOW_ORDER_KINEMATIC_PAIR
      registry.put(
          "LOW_ORDER_KINEMATIC_PAIR",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LOW_ORDER_KINEMATIC_PAIR"));

// Entity: LOW_ORDER_KINEMATIC_PAIR_VALUE
      registry.put(
          "LOW_ORDER_KINEMATIC_PAIR_VALUE",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "LOW_ORDER_KINEMATIC_PAIR_VALUE"));

// Entity: LOW_ORDER_KINEMATIC_PAIR_WITH_MOTION_COUPLING
      registry.put(
          "LOW_ORDER_KINEMATIC_PAIR_WITH_MOTION_COUPLING",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "LOW_ORDER_KINEMATIC_PAIR_WITH_MOTION_COUPLING"));

// Entity: ORIENTED_JOINT
      registry.put(
          "ORIENTED_JOINT",
          (resolver, instance) ->
              resolver.resolveGenericProperty(instance, "ORIENTED_JOINT"));

// Entity: CHARACTERISTIC_DATA_COLUMN_HEADER_LINK
      registry.put(
          "CHARACTERISTIC_DATA_COLUMN_HEADER_LINK",
          (resolver, instance) ->
              resolver.resolveGenericAssignment(instance, "CHARACTERISTIC_DATA_COLUMN_HEADER_LINK"));

// Entity: KINEMATIC_PAIR
      registry.put("KINEMATIC_PAIR", (resolver, instance) ->
          resolver.resolveKinematicPair(instance, "KINEMATIC_PAIR"));

// Entity: KINEMATIC_JOINT
      registry.put(
          "KINEMATIC_JOINT",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: KINEMATIC_LINK
      registry.put("KINEMATIC_LINK", StepEntityResolver::resolveKinematicLink);

// Entity: KINEMATIC_STRUCTURE
      registry.put("KINEMATIC_STRUCTURE", StepEntityResolver::resolveKinematicStructure);

// Entity: PRISMATIC_PAIR
      registry.put("PRISMATIC_PAIR", StepEntityResolver::resolvePrismaticPair);

// Entity: REVOLUTE_PAIR
      registry.put("REVOLUTE_PAIR", StepEntityResolver::resolveRevolutePair);

// Entity: CYLINDRICAL_PAIR
      registry.put("CYLINDRICAL_PAIR", StepEntityResolver::resolveCylindricalPair);

// Entity: PLANAR_PAIR
      registry.put("PLANAR_PAIR", StepEntityResolver::resolvePlanarPair);

// Entity: UNIVERSAL_PAIR
      registry.put("UNIVERSAL_PAIR", StepEntityResolver::resolveUniversalPair);

// Entity: SCREW_PAIR
      registry.put("SCREW_PAIR", StepEntityResolver::resolveScrewPair);

// Entity: GEAR_PAIR
      registry.put("GEAR_PAIR", StepEntityResolver::resolveGearPair);

// Entity: GEAR_PAIR_WITH_RANGE
      registry.put("GEAR_PAIR_WITH_RANGE", StepEntityResolver::resolveGearPairWithRange);

// Entity: RACK_AND_PINION_PAIR
      registry.put("RACK_AND_PINION_PAIR", StepEntityResolver::resolveRackAndPinionPair);

// Entity: REVOLUTE_JOINT
      registry.put("REVOLUTE_JOINT", StepEntityResolver::resolveRevoluteJoint);

// Entity: PRISMATIC_JOINT
      registry.put("PRISMATIC_JOINT", StepEntityResolver::resolvePrismaticJoint);

// Entity: CYLINDRICAL_JOINT
      registry.put("CYLINDRICAL_JOINT", StepEntityResolver::resolveCylindricalJoint);

// Entity: PLANAR_JOINT
      registry.put("PLANAR_JOINT", StepEntityResolver::resolvePlanarJoint);

// Entity: SCREW_JOINT
      registry.put("SCREW_JOINT", StepEntityResolver::resolveScrewJoint);

// Entity: GENERAL_JOINT
      registry.put("GENERAL_JOINT", StepEntityResolver::resolveGeneralJoint);

// Entity: JOINT_VALUE
      registry.put("JOINT_VALUE", StepEntityResolver::resolveJointValue);

// Entity: KINEMATIC_CHAIN
      registry.put("KINEMATIC_CHAIN", StepEntityResolver::resolveKinematicChain);

// Entity: KINEMATIC_MODEL
      registry.put("KINEMATIC_MODEL", StepEntityResolver::resolveKinematicModel);

// Entity: KINEMATIC_PROPERTY
      registry.put("KINEMATIC_PROPERTY", StepEntityResolver::resolveKinematicProperty);

// Entity: VALUE_REASON_PAIR
      registry.put("VALUE_REASON_PAIR", StepEntityResolver::resolveValueReasonPair);

// Entity: LOW_ORDER_KINEMATIC_PAIR_WITH_RANGE
      registry.put("LOW_ORDER_KINEMATIC_PAIR_WITH_RANGE",
          StepEntityResolver::resolveLowOrderKinematicPairWithRange);

// Entity: ACTUATED_KINEMATIC_PAIR
      registry.put("ACTUATED_KINEMATIC_PAIR", StepEntityResolver::resolveActuatedKinematicPair);

// Entity: KINEMATIC_FRAME_BASED_TRANSFORMATION
      registry.put("KINEMATIC_FRAME_BASED_TRANSFORMATION",
          StepEntityResolver::resolveKinematicFrameBasedTransformation);

// Entity: DIMENSION_PAIR
      registry.put(
          "DIMENSION_PAIR",
          (resolver, instance) -> resolver.resolveShapeAspectRelationship(instance, "DIMENSION_PAIR"));

// Entity: REPAIR_DOCUMENT
      registry.put(
          "REPAIR_DOCUMENT",
          (resolver, instance) -> resolver.resolveDocument(instance));

// Entity: MECHANISM
      registry.put(
          "MECHANISM",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: KINEMATIC_LINK_REFERENCE
      registry.put("KINEMATIC_LINK_REFERENCE", StepEntityResolver::resolveKinematicLinkReference);

// Entity: KINEMATIC_JOINT_REFERENCE
      registry.put("KINEMATIC_JOINT_REFERENCE", StepEntityResolver::resolveKinematicJointReference);

// Entity: MECHANISM_DEFINITION
      registry.put("MECHANISM_DEFINITION", StepEntityResolver::resolveMechanismDefinition);

// Entity: KINEMATIC_FRAME_REPRESENTATION_RELATIONSHIP
      registry.put(
          "KINEMATIC_FRAME_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(instance, "KINEMATIC_FRAME_REPRESENTATION_RELATIONSHIP"));

// Entity: KINEMATIC_FRAME_BACKGROUND_REPRESENTATION_RELATIONSHIP
      registry.put(
          "KINEMATIC_FRAME_BACKGROUND_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(instance, "KINEMATIC_FRAME_BACKGROUND_REPRESENTATION_RELATIONSHIP"));

// Entity: KINEMATIC_LINK_SEQUENCE
      registry.put(
          "KINEMATIC_LINK_SEQUENCE",
          (resolver, instance) -> resolver.resolveRepresentationItem(instance));

// Entity: MECHANISM_STATE_REPRESENTATION_RELATIONSHIP
      registry.put(
          "MECHANISM_STATE_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(instance, "MECHANISM_STATE_REPRESENTATION_RELATIONSHIP"));

// Entity: KINEMATIC_GROUND_REPRESENTATION_RELATIONSHIP
      registry.put(
          "KINEMATIC_GROUND_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(instance, "KINEMATIC_GROUND_REPRESENTATION_RELATIONSHIP"));

// Entity: KINEMATIC_PAIR_REPRESENTATION_RELATIONSHIP
      registry.put(
          "KINEMATIC_PAIR_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(instance, "KINEMATIC_PAIR_REPRESENTATION_RELATIONSHIP"));

// Entity: KINEMATIC_LINK_REPRESENTATION_RELATIONSHIP
      registry.put(
          "KINEMATIC_LINK_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(instance, "KINEMATIC_LINK_REPRESENTATION_RELATIONSHIP"));

// Entity: MECHANISM_REPRESENTATION_RELATIONSHIP
      registry.put(
          "MECHANISM_REPRESENTATION_RELATIONSHIP",
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(instance, "MECHANISM_REPRESENTATION_RELATIONSHIP"));

  }
}
