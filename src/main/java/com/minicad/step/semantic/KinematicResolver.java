package com.minicad.step.semantic;

import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;


/**
 * Kinematic resolver - handles kinematic structure, pair, joint, and motion entities.
 * Extracted from StepEntityResolver to reduce file size and improve maintainability.
 * Contains kinematic chains/links/paths, low-order and gear pairs, joints,
 * mechanism definitions, and motion constraints.
 */
final class KinematicResolver {

  private final StepEntityResolver resolver;

  KinematicResolver(StepEntityResolver resolver) {
    this.resolver = resolver;
  }

  // === Kinematic Structure Entities ===

  StepKinematicChain resolveKinematicChain(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "KINEMATIC_CHAIN");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepKinematicChain(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1));
  }

  StepKinematicFrameBasedTransformation resolveKinematicFrameBasedTransformation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "KINEMATIC_FRAME_BASED_TRANSFORMATION");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepKinematicFrameBasedTransformation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)));
  }

  StepKinematicLink resolveKinematicLink(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "KINEMATIC_LINK");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepKinematicLink(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepKinematicLinkReference resolveKinematicLinkReference(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "KINEMATIC_LINK_REFERENCE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepKinematicLinkReference(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)));
  }

  StepKinematicPath resolveKinematicPath(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "KINEMATIC_PATH");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepKinematicPath(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.entityReferenceList(instance, definition, 4,
            "KINEMATIC_PATH pairs must contain entity references"));
  }

  StepKinematicProperty resolveKinematicProperty(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "KINEMATIC_PROPERTY");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepKinematicProperty(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepKinematicStructure resolveKinematicStructure(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "KINEMATIC_STRUCTURE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepKinematicStructure(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepMechanismDefinition resolveMechanismDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MECHANISM_DEFINITION");
    StepEntityResolver.requireParameterCount(instance, definition, 8);
    return new StepMechanismDefinition(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.entityReferenceList(instance, definition, 2,
            "MECHANISM_DEFINITION links must contain entity references"),
        resolver.entityReferenceList(instance, definition, 3,
            "MECHANISM_DEFINITION joints must contain entity references"),
        (int) resolver.numberValue(instance, definition, 4),
        resolver.resolve(resolver.referenceId(instance, definition, 5)),
        resolver.entityReferenceList(instance, definition, 6,
            "MECHANISM_DEFINITION actuated joints must contain entity references"));
  }

  StepMechanismStateRepresentation resolveMechanismStateRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MECHANISM_STATE_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepMechanismStateRepresentation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.entityReferenceList(instance, definition, 1,
            "MECHANISM_STATE_REPRESENTATION items must contain entity references"),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  // === Kinematic Pair Entities ===

  StepActuatedKinematicPair resolveActuatedKinematicPair(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ACTUATED_KINEMATIC_PAIR");
    StepEntityResolver.requireParameterCount(instance, definition, 6);
    return new StepActuatedKinematicPair(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.numberValue(instance, definition, 4));
  }

  StepCylindricalPair resolveCylindricalPair(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CYLINDRICAL_PAIR");
    StepEntityResolver.requireParameterCount(instance, definition, 7);
    return new StepCylindricalPair(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.resolve(resolver.referenceId(instance, definition, 4)),
        resolver.resolve(resolver.referenceId(instance, definition, 5)));
  }

  StepGearPair resolveGearPair(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "GEAR_PAIR");
    StepEntityResolver.requireParameterCount(instance, definition, 8);
    return new StepGearPair(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.numberValue(instance, definition, 4),
        resolver.resolve(resolver.referenceId(instance, definition, 5)),
        resolver.resolve(resolver.referenceId(instance, definition, 6)));
  }

  StepGearPairWithRange resolveGearPairWithRange(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "GEAR_PAIR_WITH_RANGE");
    StepEntityResolver.requireParameterCount(instance, definition, 11);
    return new StepGearPairWithRange(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.numberValue(instance, definition, 4),
        resolver.numberValue(instance, definition, 5),
        resolver.numberValue(instance, definition, 6),
        resolver.resolve(resolver.referenceId(instance, definition, 7)),
        resolver.resolve(resolver.referenceId(instance, definition, 8)));
  }

  StepKinematicPair resolveKinematicPair(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "KINEMATIC_PAIR");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepKinematicPair(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)));
  }

  StepKinematicPair resolveKinematicPair(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 7);
    return new StepKinematicPair(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)));
  }

  StepLowOrderKinematicPairWithRange resolveLowOrderKinematicPairWithRange(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "LOW_ORDER_KINEMATIC_PAIR_WITH_RANGE");
    StepEntityResolver.requireParameterCount(instance, definition, 10);
    return new StepLowOrderKinematicPairWithRange(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.numberValue(instance, definition, 4),
        resolver.numberValue(instance, definition, 5),
        resolver.resolve(resolver.referenceId(instance, definition, 6)),
        resolver.resolve(resolver.referenceId(instance, definition, 7)));
  }

  StepPlanarPair resolvePlanarPair(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PLANAR_PAIR");
    StepEntityResolver.requireParameterCount(instance, definition, 7);
    return new StepPlanarPair(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.resolve(resolver.referenceId(instance, definition, 4)),
        resolver.resolve(resolver.referenceId(instance, definition, 5)));
  }

  StepPrismaticPair resolvePrismaticPair(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PRISMATIC_PAIR");
    StepEntityResolver.requireParameterCount(instance, definition, 7);
    return new StepPrismaticPair(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.resolve(resolver.referenceId(instance, definition, 4)),
        resolver.resolve(resolver.referenceId(instance, definition, 5)));
  }

  StepRackAndPinionPair resolveRackAndPinionPair(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "RACK_AND_PINION_PAIR");
    StepEntityResolver.requireParameterCount(instance, definition, 8);
    return new StepRackAndPinionPair(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.numberValue(instance, definition, 4),
        resolver.resolve(resolver.referenceId(instance, definition, 5)),
        resolver.resolve(resolver.referenceId(instance, definition, 6)));
  }

  StepRevolutePair resolveRevolutePair(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "REVOLUTE_PAIR");
    StepEntityResolver.requireParameterCount(instance, definition, 7);
    return new StepRevolutePair(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.resolve(resolver.referenceId(instance, definition, 4)),
        resolver.resolve(resolver.referenceId(instance, definition, 5)));
  }

  StepScrewPair resolveScrewPair(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SCREW_PAIR");
    StepEntityResolver.requireParameterCount(instance, definition, 8);
    return new StepScrewPair(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.numberValue(instance, definition, 4),
        resolver.resolve(resolver.referenceId(instance, definition, 5)),
        resolver.resolve(resolver.referenceId(instance, definition, 6)));
  }

  StepSphericalPair resolveSphericalPair(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SPHERICAL_PAIR");
    StepEntityResolver.requireParameterCount(instance, definition, 6);
    return new StepSphericalPair(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.resolve(resolver.referenceId(instance, definition, 4)));
  }

  StepUniversalPair resolveUniversalPair(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "UNIVERSAL_PAIR");
    StepEntityResolver.requireParameterCount(instance, definition, 8);
    return new StepUniversalPair(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.resolve(resolver.referenceId(instance, definition, 4)),
        resolver.resolve(resolver.referenceId(instance, definition, 5)),
        resolver.resolve(resolver.referenceId(instance, definition, 6)));
  }

  // === Kinematic Joint Entities ===

  StepCylindricalJoint resolveCylindricalJoint(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CYLINDRICAL_JOINT");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepCylindricalJoint(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.resolve(resolver.referenceId(instance, definition, 4)));
  }

  StepGeneralJoint resolveGeneralJoint(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "GENERAL_JOINT");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepGeneralJoint(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)));
  }

  StepJointValue resolveJointValue(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "JOINT_VALUE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepJointValue(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.numberValue(instance, definition, 1),
        resolver.stringValue(instance, definition, 2));
  }

  StepKinematicJoint resolveKinematicJoint(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "KINEMATIC_JOINT");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepKinematicJoint(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepKinematicJointReference resolveKinematicJointReference(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "KINEMATIC_JOINT_REFERENCE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepKinematicJointReference(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)));
  }

  StepPlanarJoint resolvePlanarJoint(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PLANAR_JOINT");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepPlanarJoint(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.resolve(resolver.referenceId(instance, definition, 4)));
  }

  StepPrismaticJoint resolvePrismaticJoint(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PRISMATIC_JOINT");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepPrismaticJoint(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.resolve(resolver.referenceId(instance, definition, 4)));
  }

  StepRevoluteJoint resolveRevoluteJoint(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "REVOLUTE_JOINT");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepRevoluteJoint(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.resolve(resolver.referenceId(instance, definition, 4)));
  }

  StepScrewJoint resolveScrewJoint(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SCREW_JOINT");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepScrewJoint(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.numberValue(instance, definition, 4));
  }

  StepSphericalJoint resolveSphericalJoint(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SPHERICAL_JOINT");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepSphericalJoint(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.resolve(resolver.referenceId(instance, definition, 4)));
  }

  // === Motion Entities ===

  StepMotionConstraint resolveMotionConstraint(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MOTION_CONSTRAINT");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepMotionConstraint(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3));
  }

  StepMotionPath resolveMotionPath(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MOTION_PATH");
    StepEntityResolver.requireParameterCount(instance, definition, 7);
    return new StepMotionPath(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.stringValue(instance, definition, 2),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.resolve(resolver.referenceId(instance, definition, 4)),
        resolver.resolve(resolver.referenceId(instance, definition, 5)),
        resolver.resolve(resolver.referenceId(instance, definition, 6)));
  }
}
