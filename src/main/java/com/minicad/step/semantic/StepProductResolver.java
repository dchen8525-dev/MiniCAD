package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedStepEntityException;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.misc.StepGenericEntity;
import com.minicad.step.model.config_mgmt.StepInterpolatedConfigurationSegment;
import com.minicad.step.model.document.StepApplicationContext;
import com.minicad.step.model.geometry.StepAxis2Placement2D;
import com.minicad.step.model.geometry.StepAxis2Placement3D;
import com.minicad.step.model.product.StepAlternateProductRelationship;
import com.minicad.step.model.product.StepAssemblyComponentRelationship;
import com.minicad.step.model.product.StepAssemblyProcessPlan;
import com.minicad.step.model.product.StepBooleanClippingResult;
import com.minicad.step.model.product.StepBooleanResult;
import com.minicad.step.model.product.StepBrepWithVoids;
import com.minicad.step.model.product.StepComplexClippingResult;
import com.minicad.step.model.product.StepContextDependentShapeRepresentation;
import com.minicad.step.model.product.StepDesignedPartDesignVersion;
import com.minicad.step.model.product.StepMakeFromBuildAssembly;
import com.minicad.step.model.product.StepMakeFromFeature;
import com.minicad.step.model.product.StepMakeFromRelationship;
import com.minicad.step.model.product.StepMakeFromUsageOption;
import com.minicad.step.model.product.StepMappedItem;
import com.minicad.step.model.product.StepNextAssemblyUsageOccurrence;
import com.minicad.step.model.product.StepProduct;
import com.minicad.step.model.product.StepProductCategory;
import com.minicad.step.model.product.StepProductCategoryRelationship;
import com.minicad.step.model.product.StepProductContext;
import com.minicad.step.model.product.StepProductDefinition;
import com.minicad.step.model.product.StepProductDefinitionContext;
import com.minicad.step.model.product.StepProductDefinitionEffectivity;
import com.minicad.step.model.product.StepProductDefinitionFormation;
import com.minicad.step.model.product.StepProductDefinitionFormationRelationship;
import com.minicad.step.model.product.StepProductDefinitionRelationship;
import com.minicad.step.model.product.StepProductDefinitionRelationshipRelationship;
import com.minicad.step.model.product.StepProductDefinitionShape;
import com.minicad.step.model.product.StepProductDefinitionWithAssociatedDocuments;
import com.minicad.step.model.product.StepProductRelatedProductCategory;
import com.minicad.step.model.product.StepProductRelationship;
import com.minicad.step.model.product.StepProductVersion;
import com.minicad.step.model.product.StepQuantifiedAssemblyComponentUsage;
import com.minicad.step.model.product.StepShapeDefinitionRepresentation;
import com.minicad.step.model.product.StepSpecifiedHigherUsageOccurrence;
import com.minicad.step.model.workflow.StepSymbolRepresentationMap;
import com.minicad.step.model.workflow.StepRepresentation;
import com.minicad.step.model.product.StepRepresentationMap;
import com.minicad.step.model.product.StepRepresentationRelationship;
import com.minicad.step.model.product.StepRepresentationRelationshipWithTransformation;
import com.minicad.step.model.workflow.StepShapeRepresentationRelationship;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;

import java.util.List;

/**
 * Product resolver - handles resolution of STEP product entity types.
 * Extracted from StepEntityResolver to reduce its size and improve maintainability.
 */
final class StepProductResolver {

  private final StepEntityResolver resolver;

  StepProductResolver(StepEntityResolver resolver) {
    this.resolver = resolver;
  }

  // === Product ===

  StepProductContext resolveProductContext(StepEntityInstance instance) {
    return resolveProductContext(instance, "PRODUCT_CONTEXT");
  }

  StepProductContext resolveProductContext(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    boolean stepOrder =
        resolver.unwrapTyped(definition.parameters().get(1)) instanceof com.minicad.step.syntax.StepValue.ReferenceValue;
    return new StepProductContext(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        stepOrder ? resolver.stringValue(instance, definition, 2) : resolver.stringValue(instance, definition, 1),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, stepOrder ? 1 : 2),
            StepApplicationContext.class,
            entityName + " frame_of_reference must reference APPLICATION_CONTEXT"),
        entityName);
  }

  StepProduct resolveProduct(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PRODUCT");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepProduct(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.optionalStringValue(instance, definition, 2),
        resolver.referenceList(
            instance,
            definition,
            3,
            StepProductContext.class,
            "PRODUCT frame_of_reference must contain PRODUCT_CONTEXT references"));
  }

  StepProductRelatedProductCategory resolveProductRelatedProductCategory(
      StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PRODUCT_RELATED_PRODUCT_CATEGORY");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepProductRelatedProductCategory(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1),
        resolver.referenceList(
            instance,
            definition,
            2,
            StepProduct.class,
            "PRODUCT_RELATED_PRODUCT_CATEGORY products must contain PRODUCT references"));
  }

  StepProductCategory resolveProductCategory(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PRODUCT_CATEGORY");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepProductCategory(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1));
  }

  StepProductCategoryRelationship resolveProductCategoryRelationship(
      StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PRODUCT_CATEGORY_RELATIONSHIP");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepProductCategoryRelationship(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepProductCategory.class,
            "PRODUCT_CATEGORY_RELATIONSHIP category must reference PRODUCT_CATEGORY"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepProductCategory.class,
            "PRODUCT_CATEGORY_RELATIONSHIP sub_category must reference PRODUCT_CATEGORY"));
  }

  StepProductRelationship resolveProductRelationship(StepEntityInstance instance) {
    return resolveProductRelationship(instance, "PRODUCT_RELATIONSHIP");
  }

  StepProductRelationship resolveProductRelationship(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepProductRelationship(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.optionalStringValue(instance, definition, 2),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepProduct.class,
            entityName + " relating_product must reference PRODUCT"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 4),
            StepProduct.class,
            entityName + " related_product must reference PRODUCT"),
        entityName);
  }

  StepProductVersion resolveProductVersion(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PRODUCT_VERSION");
    StepEntityResolver.requireParameterCount(instance, definition, 6);
    return new StepProductVersion(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.stringValue(instance, definition, 2),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.resolve(resolver.referenceId(instance, definition, 4)));
  }

  // === Product Definition ===

  StepProductDefinitionFormation resolveProductDefinitionFormation(
      StepEntityInstance instance) {
    StepEntityDefinition definition;
    if (instance.hasDefinition("PRODUCT_DEFINITION_FORMATION_WITH_SPECIFIED_SOURCE")) {
      definition = resolver.definition(instance, "PRODUCT_DEFINITION_FORMATION_WITH_SPECIFIED_SOURCE");
      StepEntityResolver.requireParameterCount(instance, definition, 4);
    } else {
      definition = resolver.definition(instance, "PRODUCT_DEFINITION_FORMATION");
      StepEntityResolver.requireParameterCount(instance, definition, 3);
    }
    return new StepProductDefinitionFormation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepProduct.class,
            "PRODUCT_DEFINITION_FORMATION of_product must reference PRODUCT"));
  }

  StepProductDefinitionFormationRelationship
      resolveProductDefinitionFormationRelationship(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PRODUCT_DEFINITION_FORMATION_RELATIONSHIP");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepProductDefinitionFormationRelationship(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.optionalStringValue(instance, definition, 2),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepProductDefinitionFormation.class,
            "PRODUCT_DEFINITION_FORMATION_RELATIONSHIP relating_product_definition_formation must reference PRODUCT_DEFINITION_FORMATION"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 4),
            StepProductDefinitionFormation.class,
            "PRODUCT_DEFINITION_FORMATION_RELATIONSHIP related_product_definition_formation must reference PRODUCT_DEFINITION_FORMATION"));
  }

  StepProductDefinitionContext resolveProductDefinitionContext(
      StepEntityInstance instance) {
    return resolveProductDefinitionContext(instance, "PRODUCT_DEFINITION_CONTEXT");
  }

  StepProductDefinitionContext resolveProductDefinitionContext(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    boolean stepOrder =
        resolver.unwrapTyped(definition.parameters().get(1)) instanceof com.minicad.step.syntax.StepValue.ReferenceValue;
    return new StepProductDefinitionContext(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        stepOrder ? resolver.stringValue(instance, definition, 2) : resolver.stringValue(instance, definition, 1),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, stepOrder ? 1 : 2),
            StepApplicationContext.class,
            entityName + " frame_of_reference must reference APPLICATION_CONTEXT"),
        entityName);
  }

  StepProductDefinition resolveProductDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PRODUCT_DEFINITION");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepProductDefinition(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepProductDefinitionFormation.class,
            "PRODUCT_DEFINITION formation must reference PRODUCT_DEFINITION_FORMATION"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepProductDefinitionContext.class,
            "PRODUCT_DEFINITION frame_of_reference must reference PRODUCT_DEFINITION_CONTEXT"));
  }

  StepProductDefinitionRelationship resolveProductDefinitionRelationship(
      StepEntityInstance instance) {
    return resolveProductDefinitionRelationship(instance, "PRODUCT_DEFINITION_RELATIONSHIP");
  }

  StepProductDefinitionRelationship resolveProductDefinitionRelationship(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepProductDefinitionRelationship(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.optionalStringValue(instance, definition, 2),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepProductDefinition.class,
            entityName + " relating_product_definition must reference PRODUCT_DEFINITION"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 4),
            StepProductDefinition.class,
            entityName + " related_product_definition must reference PRODUCT_DEFINITION"),
        entityName);
  }

  StepGenericEntity resolveProductDefinitionRelationshipFlexible(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepGenericEntity(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.optionalStringValue(instance, definition, 2),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.resolve(resolver.referenceId(instance, definition, 4)),
        entityName);
  }

  StepProductDefinitionRelationshipRelationship
      resolveProductDefinitionRelationshipRelationship(StepEntityInstance instance) {
    return resolveProductDefinitionRelationshipRelationship(
        instance, "PRODUCT_DEFINITION_RELATIONSHIP_RELATIONSHIP");
  }

  StepProductDefinitionRelationshipRelationship resolveProductDefinitionRelationshipRelationship(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepProductDefinitionRelationshipRelationship(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.optionalStringValue(instance, definition, 2),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepProductDefinitionRelationship.class,
            entityName + " relating_product_definition_relationship must reference PRODUCT_DEFINITION_RELATIONSHIP"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 4),
            StepProductDefinitionRelationship.class,
            entityName + " related_product_definition_relationship must reference PRODUCT_DEFINITION_RELATIONSHIP"),
        entityName);
  }

  StepProductDefinitionShape resolveProductDefinitionShape(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PRODUCT_DEFINITION_SHAPE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity resolvedDefinition = resolver.tryResolveReference(definition.parameters().get(2));
    if (resolvedDefinition != null
        && !(resolvedDefinition instanceof StepProductDefinition)
        && !(resolvedDefinition instanceof StepNextAssemblyUsageOccurrence)) {
      throw new StepResolutionException(
          "PRODUCT_DEFINITION_SHAPE definition must reference PRODUCT_DEFINITION or NEXT_ASSEMBLY_USAGE_OCCURRENCE"
              + " but got "
              + resolvedDefinition.getClass().getSimpleName());
    }
    return new StepProductDefinitionShape(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1),
        resolvedDefinition);
  }

  StepProductDefinitionEffectivity resolveProductDefinitionEffectivity(
      StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PRODUCT_DEFINITION_EFFECTIVITY");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepProductDefinitionEffectivity(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepProductDefinition.class,
            "PRODUCT_DEFINITION_EFFECTIVITY product_definition must reference PRODUCT_DEFINITION"));
  }

  // === Assembly Usage ===

  StepNextAssemblyUsageOccurrence resolveNextAssemblyUsageOccurrence(
      StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "NEXT_ASSEMBLY_USAGE_OCCURRENCE");
    StepEntityResolver.requireParameterCountIn(instance, definition, 5, 6);
    return new StepNextAssemblyUsageOccurrence(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.stringValue(instance, definition, 2),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepProductDefinition.class,
            "NEXT_ASSEMBLY_USAGE_OCCURRENCE relating_product_definition must reference PRODUCT_DEFINITION"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 4),
            StepProductDefinition.class,
            "NEXT_ASSEMBLY_USAGE_OCCURRENCE related_product_definition must reference PRODUCT_DEFINITION"),
        definition.parameters().size() > 5 ? resolver.optionalStringValue(instance, definition, 5) : null);
  }

  StepContextDependentShapeRepresentation resolveContextDependentShapeRepresentation(
      StepEntityInstance instance) {
    StepEntityDefinition definition =
        resolver.definition(instance, "CONTEXT_DEPENDENT_SHAPE_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    StepEntity relationship = resolver.resolve(resolver.referenceId(instance, definition, 0));
    if (!(relationship instanceof StepShapeRepresentationRelationship)
        && !(relationship instanceof StepRepresentationRelationship)
        && !(relationship instanceof StepRepresentationRelationshipWithTransformation)) {
      throw new StepResolutionException(
          "CONTEXT_DEPENDENT_SHAPE_REPRESENTATION representation_relation must reference a representation relationship"
              + " but got "
              + relationship.getClass().getSimpleName());
    }
    StepEntity representedProductRelation = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!(representedProductRelation instanceof StepNextAssemblyUsageOccurrence)
        && !(representedProductRelation instanceof StepProductDefinitionShape)) {
      throw new StepResolutionException(
          "CONTEXT_DEPENDENT_SHAPE_REPRESENTATION represented_product_relation must reference"
              + " NEXT_ASSEMBLY_USAGE_OCCURRENCE or PRODUCT_DEFINITION_SHAPE but got "
              + representedProductRelation.getClass().getSimpleName());
    }
    return new StepContextDependentShapeRepresentation(
        instance.id(), relationship, representedProductRelation);
  }

  StepQuantifiedAssemblyComponentUsage resolveQuantifiedAssemblyComponentUsage(
      StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "QUANTIFIED_ASSEMBLY_COMPONENT_USAGE");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepQuantifiedAssemblyComponentUsage(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.integerValue(instance, definition, 3));
  }

  StepSpecifiedHigherUsageOccurrence resolveSpecifiedHigherUsageOccurrence(
      StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SPECIFIED_HIGHER_USAGE_OCCURRENCE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepSpecifiedHigherUsageOccurrence(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepAlternateProductRelationship resolveAlternateProductRelationship(
      StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ALTERNATE_PRODUCT_RELATIONSHIP");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepAlternateProductRelationship(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)));
  }

  // === Make From ===

  StepMakeFromBuildAssembly resolveMakeFromBuildAssembly(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MAKE_FROM_BUILD_ASSEMBLY");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepMakeFromBuildAssembly(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepAssemblyComponentRelationship resolveAssemblyComponentRelationship(
      StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ASSEMBLY_COMPONENT_RELATIONSHIP");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepAssemblyComponentRelationship(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)));
  }

  StepMakeFromFeature resolveMakeFromFeature(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MAKE_FROM_FEATURE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepMakeFromFeature(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepMakeFromUsageOption resolveMakeFromUsageOption(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MAKE_FROM_USAGE_OPTION");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepMakeFromUsageOption(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepMakeFromRelationship resolveMakeFromRelationship(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MAKE_FROM_RELATIONSHIP");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepMakeFromRelationship(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)));
  }

  // === Product Definition With Documents ===

  StepProductDefinitionWithAssociatedDocuments resolveProductDefinitionWithAssociatedDocuments(
      StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PRODUCT_DEFINITION_WITH_ASSOCIATED_DOCUMENTS");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    List<StepEntity> docs =
        resolver.entityReferenceList(
            instance, definition, 2,
            "PRODUCT_DEFINITION_WITH_ASSOCIATED_DOCUMENTS documents must contain entity references");
    return new StepProductDefinitionWithAssociatedDocuments(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        docs);
  }

  // === Shape Definition Representation ===

  StepShapeDefinitionRepresentation resolveShapeDefinitionRepresentation(
      StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SHAPE_DEFINITION_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepShapeDefinitionRepresentation(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepProductDefinitionShape.class,
            "SHAPE_DEFINITION_REPRESENTATION definition must reference PRODUCT_DEFINITION_SHAPE"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepRepresentation.class,
            "SHAPE_DEFINITION_REPRESENTATION used_representation must reference SHAPE_REPRESENTATION"));
  }

  // === Representation Map ===

  StepRepresentationMap resolveRepresentationMap(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "REPRESENTATION_MAP");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    StepEntity mappedOrigin = resolver.resolve(resolver.referenceId(instance, definition, 0));
    if (!(mappedOrigin instanceof StepAxis2Placement2D)
        && !(mappedOrigin instanceof StepAxis2Placement3D)) {
      throw new UnsupportedStepEntityException(
          "REPRESENTATION_MAP mapped_origin must reference AXIS2_PLACEMENT_2D or AXIS2_PLACEMENT_3D");
    }
    return new StepRepresentationMap(
        instance.id(),
        mappedOrigin,
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepRepresentation.class,
            "REPRESENTATION_MAP mapped_representation must reference REPRESENTATION"));
  }

  StepSymbolRepresentationMap resolveSymbolRepresentationMap(
      StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SYMBOL_REPRESENTATION_MAP");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    StepEntity mappedOrigin = resolver.resolve(resolver.referenceId(instance, definition, 0));
    if (!(mappedOrigin instanceof StepAxis2Placement2D)
        && !(mappedOrigin instanceof StepAxis2Placement3D)) {
      throw new UnsupportedStepEntityException(
          "SYMBOL_REPRESENTATION_MAP mapped_origin must reference AXIS2_PLACEMENT_2D or AXIS2_PLACEMENT_3D");
    }
    return new StepSymbolRepresentationMap(
        instance.id(),
        mappedOrigin,
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepRepresentation.class,
            "SYMBOL_REPRESENTATION_MAP mapped_representation must reference REPRESENTATION"));
  }

  // === Mapped Item ===

  StepMappedItem resolveMappedItem(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MAPPED_ITEM");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepMappedItem(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepRepresentationMap.class,
            "MAPPED_ITEM mapping_source must reference REPRESENTATION_MAP"),
        resolver.resolve(resolver.referenceId(instance, definition, 1)));
  }

  // === Assembly Process ===

  StepAssemblyProcessPlan resolveAssemblyProcessPlan(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ASSEMBLY_PROCESS_PLAN");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    List<StepEntity> items =
        resolver.entityReferenceList(
            instance, definition, 2,
            "ASSEMBLY_PROCESS_PLAN items must contain entity references");
    List<StepEntity> assemblySequence =
        resolver.entityReferenceList(
            instance, definition, 4,
            "ASSEMBLY_PROCESS_PLAN assembly_sequence must contain entity references");
    return new StepAssemblyProcessPlan(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        items,
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        assemblySequence);
  }

  // === Other Product Entities ===

  StepDesignedPartDesignVersion resolveDesignedPartDesignVersion(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DESIGNED_PART_DESIGN_VERSION");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepDesignedPartDesignVersion(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepInterpolatedConfigurationSegment resolveInterpolatedConfigurationSegment(
      StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "INTERPOLATED_CONFIGURATION_SEGMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepInterpolatedConfigurationSegment(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }
}