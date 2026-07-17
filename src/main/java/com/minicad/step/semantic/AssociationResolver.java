package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedStepEntityException;
import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;

import java.util.List;

/**
 * Association resolver - handles model item association and usage entities.
 * Extracted from StepEntityResolver to reduce file size and improve maintainability.
 * Contains geometric item specific usages, item identified representation usages,
 * draughting/PMI/mechanical design model item associations, and equivalences.
 */
final class AssociationResolver {

  private final StepEntityResolver resolver;

  AssociationResolver(StepEntityResolver resolver) {
    this.resolver = resolver;
  }

  // === Geometric Item Specific Usage Entities ===

  StepChainBasedGeometricItemSpecificUsage resolveChainBasedGeometricItemSpecificUsage(StepEntityInstance instance) {
    StepEntityDefinition definition =
        resolver.definition(instance, "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE");
    StepEntityResolver.requireParameterCount(instance, definition, 6);
    StepEntity usage = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!resolver.isSupportedAnnotationUsageItem(usage)) {
      throw new UnsupportedStepEntityException(
          "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE definition must reference DRAUGHTING_CALLOUT or supported annotation occurrence");
    }
    List<StepRepresentation> nodes =
        resolver.referenceList(
            instance,
            definition,
            3,
            StepRepresentation.class,
            "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE nodes must contain REPRESENTATION references");
    if (nodes.size() < 2) {
      throw new StepResolutionException(
          "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE nodes must contain at least 2 representations");
    }
    List<StepRepresentationRelationship> undirectedLinks =
        resolver.referenceList(
            instance,
            definition,
            4,
            StepRepresentationRelationship.class,
            "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE undirected_link must contain REPRESENTATION_RELATIONSHIP references");
    if (undirectedLinks.isEmpty()) {
      throw new StepResolutionException(
          "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE undirected_link must not be empty");
    }
    StepEntity identifiedItem = resolver.resolve(resolver.referenceId(instance, definition, 5));
    if (!resolver.isSupportedGeometricUsageIdentifiedItem(identifiedItem)) {
      throw new UnsupportedStepEntityException(
          "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE identified item must reference supported point/geometric set, face, edge, path, loop, shell, model, solid, wire container, annotation content/occurrence or REPRESENTATION");
    }
    return new StepChainBasedGeometricItemSpecificUsage(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        usage,
        nodes,
        undirectedLinks,
        identifiedItem);
  }

  StepChainBasedItemIdentifiedRepresentationUsage resolveChainBasedItemIdentifiedRepresentationUsage(StepEntityInstance instance) {
    StepEntityDefinition definition =
        resolver.definition(instance, "CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE");
    StepEntityResolver.requireParameterCount(instance, definition, 6);
    List<StepRepresentation> nodes =
        resolver.referenceList(
            instance,
            definition,
            3,
            StepRepresentation.class,
            "CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE nodes must contain REPRESENTATION references");
    if (nodes.size() < 2) {
      throw new StepResolutionException(
          "CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE nodes must contain at least 2 representations");
    }
    List<StepRepresentationRelationship> undirectedLinks =
        resolver.referenceList(
            instance,
            definition,
            4,
            StepRepresentationRelationship.class,
            "CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE undirected_link must contain REPRESENTATION_RELATIONSHIP references");
    if (undirectedLinks.isEmpty()) {
      throw new StepResolutionException(
          "CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE undirected_link must not be empty");
    }
    StepEntity identifiedItem = resolver.resolve(resolver.referenceId(instance, definition, 5));
    if (!resolver.isSupportedAssociationUsageIdentifiedItem(identifiedItem)) {
      throw new UnsupportedStepEntityException(
          "CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE identified item must reference supported point/geometric set, face, edge, path, loop, shell, model, solid, wire container or REPRESENTATION");
    }
    return new StepChainBasedItemIdentifiedRepresentationUsage(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        nodes,
        undirectedLinks,
        identifiedItem);
  }

  StepGeometricItemSpecificUsage resolveGeometricItemSpecificUsage(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "GEOMETRIC_ITEM_SPECIFIC_USAGE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    StepEntity usage = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!resolver.isSupportedAnnotationUsageItem(usage)) {
      throw new UnsupportedStepEntityException(
          "GEOMETRIC_ITEM_SPECIFIC_USAGE usage must reference DRAUGHTING_CALLOUT or supported annotation content/occurrence");
    }
    StepEntity identifiedItem = resolver.resolve(resolver.referenceId(instance, definition, 3));
    if (!resolver.isSupportedGeometricUsageIdentifiedItem(identifiedItem)) {
      throw new UnsupportedStepEntityException(
          "GEOMETRIC_ITEM_SPECIFIC_USAGE identified item must reference supported point/geometric set, face, edge, path, loop, shell, model, solid, wire container, annotation content/occurrence or REPRESENTATION");
    }
    return new StepGeometricItemSpecificUsage(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        usage,
        identifiedItem);
  }

  // === Model Item Association Entities ===

  StepA3mEquivalenceAccuracyAssociation resolveA3mEquivalenceAccuracyAssociation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "A3M_EQUIVALENCE_ACCURACY_ASSOCIATION");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    String description = resolver.optionalStringValue(instance, definition, 1);
    return new StepA3mEquivalenceAccuracyAssociation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        description,
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)));
  }

  StepDraughtingModelItemAssociation resolveDraughtingModelItemAssociation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DRAUGHTING_MODEL_ITEM_ASSOCIATION");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    StepEntity identifiedItem = resolver.resolve(resolver.referenceId(instance, definition, 4));
    if (!resolver.isSupportedAssociationUsageIdentifiedItem(identifiedItem)) {
      throw new UnsupportedStepEntityException(
          "DRAUGHTING_MODEL_ITEM_ASSOCIATION identified item must reference supported point/geometric set, face, edge, path, loop, shell, model, solid, wire container or REPRESENTATION");
    }
    return new StepDraughtingModelItemAssociation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepRepresentation.class,
            "DRAUGHTING_MODEL_ITEM_ASSOCIATION used_representation must reference REPRESENTATION"),
        identifiedItem);
  }

  StepDraughtingModelItemAssociationWithPlaceholder resolveDraughtingModelItemAssociationWithPlaceholder(StepEntityInstance instance) {
    StepEntityDefinition definition =
        resolver.definition(instance, "DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER");
    StepEntityResolver.requireParameterCount(instance, definition, 6);
    StepEntity identifiedItem = resolver.resolve(resolver.referenceId(instance, definition, 4));
    if (!resolver.isSupportedAnnotationUsageItem(identifiedItem)) {
      throw new StepResolutionException(
          "DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER identified_item must reference DRAUGHTING_CALLOUT or supported annotation content/occurrence");
    }
    return new StepDraughtingModelItemAssociationWithPlaceholder(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepRepresentation.class,
            "DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER used_representation must reference REPRESENTATION"),
        identifiedItem,
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 5),
            StepAnnotationPlaceholderOccurrence.class,
            "DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER annotation_placeholder must reference ANNOTATION_PLACEHOLDER_OCCURRENCE"));
  }

  StepMechanicalDesignRequirementItemAssociation resolveMechanicalDesignRequirementItemAssociation(StepEntityInstance instance) {
    StepEntityDefinition definition =
        resolver.definition(instance, "MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION");
    StepEntityResolver.requireParameterCount(instance, definition, 6);
    StepEntity identifiedItem = resolver.resolve(resolver.referenceId(instance, definition, 4));
    if (!resolver.isSupportedAssociationUsageIdentifiedItem(identifiedItem)) {
      throw new UnsupportedStepEntityException(
          "MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION identified item must reference supported point/geometric set, face, edge, path, loop, shell, model, solid, wire container or REPRESENTATION");
    }
    return new StepMechanicalDesignRequirementItemAssociation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepRepresentation.class,
            "MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION used_representation must reference REPRESENTATION"),
        identifiedItem,
        resolver.resolve(resolver.referenceId(instance, definition, 5)));
  }

  StepPmiRequirementItemAssociation resolvePmiRequirementItemAssociation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PMI_REQUIREMENT_ITEM_ASSOCIATION");
    StepEntityResolver.requireParameterCount(instance, definition, 6);
    StepEntity identifiedItem = resolver.resolve(resolver.referenceId(instance, definition, 4));
    if (!resolver.isSupportedAssociationUsageIdentifiedItem(identifiedItem)) {
      throw new UnsupportedStepEntityException(
          "PMI_REQUIREMENT_ITEM_ASSOCIATION identified item must reference supported point/geometric set, face, edge, path, loop, shell, model, solid, wire container or REPRESENTATION");
    }
    return new StepPmiRequirementItemAssociation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepRepresentation.class,
            "PMI_REQUIREMENT_ITEM_ASSOCIATION used_representation must reference REPRESENTATION"),
        identifiedItem,
        resolver.resolve(resolver.referenceId(instance, definition, 5)));
  }

  StepUsageAssociation resolveUsageAssociation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "USAGE_ASSOCIATION");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepUsageAssociation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }
}
