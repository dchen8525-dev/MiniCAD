package com.minicad.step.semantic;

import com.minicad.common.StepParseException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedStepEntityException;
import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;

import java.util.List;

/**
 * Representation resolver - handles representation and shape aspect entities.
 * Extracted from StepEntityResolver to reduce file size and improve maintainability.
 * Contains representations, representation contexts/items/relationships,
 * property representations, and shape aspect entities.
 */
final class RepresentationResolver {

  private final StepEntityResolver resolver;

  RepresentationResolver(StepEntityResolver resolver) {
    this.resolver = resolver;
  }

  // === Representation Core Entities ===

  StepCalculatedGeometricRepresentationItem resolveCalculatedGeometricRepresentationItem(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CALCULATED_GEOMETRIC_REPRESENTATION_ITEM");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepCalculatedGeometricRepresentationItem(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)));
  }

  StepCompoundRepresentationItem resolveCompoundRepresentationItem(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepCompoundRepresentationItem(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.entityReferenceList(instance, definition, 1,
            entityName + " items must contain entity references"),
        entityName);
  }

  StepDescriptiveRepresentationItem resolveDescriptiveRepresentationItem(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DESCRIPTIVE_REPRESENTATION_ITEM");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepDescriptiveRepresentationItem(
        instance.id(), resolver.stringValue(instance, definition, 0), resolver.stringValue(instance, definition, 1));
  }

  StepGeometricRepresentationContext resolveGeometricRepresentationContext(StepEntityInstance instance) {
    StepEntityDefinition geometric = resolver.definition(instance, "GEOMETRIC_REPRESENTATION_CONTEXT");
    StepEntityDefinition representation = resolver.definition(instance, "REPRESENTATION_CONTEXT");
    StepEntityResolver.requireParameterCount(instance, geometric, 1);
    StepEntityResolver.requireParameterCount(instance, representation, 2);
    StepGlobalUnitAssignedContext globalUnits =
        instance.hasDefinition("GLOBAL_UNIT_ASSIGNED_CONTEXT")
            ? resolver.resolveGlobalUnitAssignedContext(instance)
            : null;
    StepGlobalUncertaintyAssignedContext globalUncertainty =
        instance.hasDefinition("GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT")
            ? resolver.resolveGlobalUncertaintyAssignedContext(instance)
            : null;
    return new StepGeometricRepresentationContext(
        instance.id(),
        resolver.integerValue(instance, geometric, 0),
        resolver.stringValue(instance, representation, 0),
        resolver.stringValue(instance, representation, 1),
        globalUnits,
        globalUncertainty);
  }

  StepGeometricRepresentationItem resolveGeometricRepresentationItem(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "GEOMETRIC_REPRESENTATION_ITEM");
    StepEntityResolver.requireParameterCount(instance, definition, 0);
    return new StepGeometricRepresentationItem(instance.id(), resolver.inheritedRepresentationItemName(instance));
  }

  StepItemIdentifiedRepresentationUsage resolveItemIdentifiedRepresentationUsage(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ITEM_IDENTIFIED_REPRESENTATION_USAGE");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    StepEntity identifiedItem = resolver.resolve(resolver.referenceId(instance, definition, 4));
    if (!resolver.isSupportedAssociationUsageIdentifiedItem(identifiedItem)) {
      throw new UnsupportedStepEntityException(
          "ITEM_IDENTIFIED_REPRESENTATION_USAGE identified item must reference supported point/geometric set, face, edge, path, loop, shell, model, solid, wire container or REPRESENTATION");
    }
    return new StepItemIdentifiedRepresentationUsage(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepRepresentation.class,
            "ITEM_IDENTIFIED_REPRESENTATION_USAGE used_representation must reference REPRESENTATION"),
        identifiedItem);
  }

  StepQualifiedRepresentationItem resolveQualifiedRepresentationItem(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "QUALIFIED_REPRESENTATION_ITEM");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepQualifiedRepresentationItem(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)));
  }

  StepRepresentation resolveRepresentation(StepEntityInstance instance, boolean shapeRepresentation) {
    String entityName = shapeRepresentation ? "SHAPE_REPRESENTATION" : "REPRESENTATION";
    return resolveRepresentation(instance, entityName, shapeRepresentation);
  }

  StepRepresentation resolveRepresentation(StepEntityInstance instance, String entityName, boolean shapeRepresentation) {
    // Try to find the specific entity definition first, fall back to parent definitions
    StepEntityDefinition definition = null;
    try {
      definition = resolver.definition(instance, entityName);
    } catch (StepParseException e) {
      // Try parent definitions for representation subtypes
      if (entityName.endsWith("_REPRESENTATION") && !entityName.equals("REPRESENTATION") && !entityName.equals("SHAPE_REPRESENTATION")) {
        try {
          definition = resolver.definition(instance, "SHAPE_REPRESENTATION");
        } catch (StepParseException e2) {
          definition = resolver.definition(instance, "REPRESENTATION");
        }
      } else {
        throw e;
      }
    }
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    List<StepEntity> items =
        resolver.entityReferenceList(
            instance, definition, 1, entityName + " items must contain entity references");
    StepEntity context = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!(context instanceof StepRepresentationContext)
        && !(context instanceof StepGeometricRepresentationContext)) {
      throw new StepResolutionException(
          entityName + " context must reference a representation context");
    }
    return new StepRepresentation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        items,
        context,
        shapeRepresentation,
        entityName);
  }

  StepRepresentationContext resolveRepresentationContext(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "REPRESENTATION_CONTEXT");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepRepresentationContext(
        instance.id(), resolver.stringValue(instance, definition, 0), resolver.stringValue(instance, definition, 1));
  }

  StepRepresentationContext3d resolveRepresentationContext3d(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "REPRESENTATION_CONTEXT_3D");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepRepresentationContext3d(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        StepResolverValueHelpers.doubleList(instance, definition, 2));
  }

  StepRepresentationItem resolveRepresentationItem(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "REPRESENTATION_ITEM");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepRepresentationItem(instance.id(), resolver.stringValue(instance, definition, 0));
  }

  StepRepresentationItemRelationship resolveRepresentationItemRelationship(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    String description = resolver.optionalStringValue(instance, definition, 1);
    return new StepRepresentationItemRelationship(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        description,
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        entityName);
  }

  StepRepresentationRelationship resolveRepresentationRelationship(StepEntityInstance instance) {
    return resolveRepresentationRelationship(instance, "REPRESENTATION_RELATIONSHIP");
  }

  StepRepresentationRelationship resolveRepresentationRelationship(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepRepresentationRelationship(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepRepresentation.class,
            entityName + " rep_1 must reference REPRESENTATION"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepRepresentation.class,
            entityName + " rep_2 must reference REPRESENTATION"),
        entityName);
  }

  StepSurfaceTextureRepresentationItem resolveSurfaceTextureRepresentationItem(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_TEXTURE_REPRESENTATION_ITEM");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepSurfaceTextureRepresentationItem(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.optionalNumberValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.stringValue(instance, definition, 3));
  }

  StepTopologicalRepresentationItem resolveTopologicalRepresentationItem(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TOPOLOGICAL_REPRESENTATION_ITEM");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    return new StepTopologicalRepresentationItem(
        instance.id(), resolver.stringValue(instance, definition, 0));
  }

  StepValueRepresentationItem resolveValueRepresentationItem(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "VALUE_REPRESENTATION_ITEM");
    StepEntityResolver.requireParameterCount(instance, definition, 2);

    // Use enhanced typedSelection with validation
    StepParameterReader.TypedSelection selection = StepResolverValueHelpers.typedSelection(instance, definition, 1);
    StepResolverValueHelpers.validateSelectTypeKnown(instance, definition, 1, selection);

    return new StepValueRepresentationItem(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        selection.typeName(),
        StepResolverValueHelpers.literalText(selection.value()));
  }

  StepWithDescriptiveRepresentationItem resolveWithDescriptiveRepresentationItem(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "WITH_DESCRIPTIVE_REPRESENTATION_ITEM");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    List<StepEntity> items =
        resolver.entityReferenceList(
            instance, definition, 2,
            "WITH_DESCRIPTIVE_REPRESENTATION_ITEM items must contain entity references");
    return new StepWithDescriptiveRepresentationItem(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        items,
        resolver.resolve(resolver.referenceId(instance, definition, 3)));
  }

  // === Shape Representation Entities ===

  StepContextDependentGeometricShapeRepresentation resolveContextDependentGeometricShapeRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CONTEXT_DEPENDENT_GEOMETRIC_SHAPE_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepContextDependentGeometricShapeRepresentation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.entityReferenceList(instance, definition, 2,
            "CONTEXT_DEPENDENT_GEOMETRIC_SHAPE_REPRESENTATION items must contain entity references"));
  }

  StepDrawingRepresentation resolveDrawingRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DRAWING_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepDrawingRepresentation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.entityReferenceList(instance, definition, 2,
            "DRAWING_REPRESENTATION items must contain entity references"));
  }

  StepHybridShapeRepresentation resolveHybridShapeRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "HYBRID_SHAPE_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepHybridShapeRepresentation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.entityReferenceList(instance, definition, 2,
            "HYBRID_SHAPE_REPRESENTATION items must contain entity references"));
  }

  StepNodeRepresentation resolveNodeRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "NODE_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepNodeRepresentation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.entityReferenceList(instance, definition, 1, "NODE_REPRESENTATION representedNodes must contain entity references"));
  }

  StepSchematicRepresentation resolveSchematicRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SCHEMATIC_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepSchematicRepresentation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.entityReferenceList(instance, definition, 2,
            "SCHEMATIC_REPRESENTATION items must contain entity references"));
  }

  StepSectionRepresentation resolveSectionRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SECTION_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepSectionRepresentation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.entityReferenceList(instance, definition, 2,
            "SECTION_REPRESENTATION items must contain entity references"));
  }

  StepShapeAspectShapeRepresentation resolveShapeAspectShapeRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SHAPE_ASPECT_SHAPE_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepShapeAspectShapeRepresentation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)));
  }

  StepShapeDimensionRepresentationWithTolerance resolveShapeDimensionRepresentationWithTolerance(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SHAPE_DIMENSION_REPRESENTATION_WITH_TOLERANCE");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepShapeDimensionRepresentationWithTolerance(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.entityReferenceList(instance, definition, 1,
            "SHAPE_DIMENSION_REPRESENTATION_WITH_TOLERANCE items must contain entity references"),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)));
  }

  StepShapeRepresentationRelationship resolveShapeRepresentationRelationship(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SHAPE_REPRESENTATION_RELATIONSHIP");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepShapeRepresentationRelationship(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepRepresentation.class,
            "SHAPE_REPRESENTATION_RELATIONSHIP rep_1 must reference REPRESENTATION"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepRepresentation.class,
            "SHAPE_REPRESENTATION_RELATIONSHIP rep_2 must reference REPRESENTATION"));
  }

  StepShapeRepresentationTransformation resolveShapeRepresentationTransformation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SHAPE_REPRESENTATION_TRANSFORMATION");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepShapeRepresentationTransformation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepSketchRepresentation resolveSketchRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SKETCH_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepSketchRepresentation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.entityReferenceList(instance, definition, 2,
            "SKETCH_REPRESENTATION items must contain entity references"));
  }

  StepTabulationRepresentation resolveTabulationRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TABULATION_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepTabulationRepresentation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.entityReferenceList(instance, definition, 2,
            "TABULATION_REPRESENTATION items must contain entity references"));
  }

  StepTextFileRepresentation resolveTextFileRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TEXT_FILE_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepTextFileRepresentation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1));
  }

  StepVolume3dElementRepresentation resolveVolume3dElementRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "VOLUME_3D_ELEMENT_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepVolume3dElementRepresentation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.entityReferenceList(instance, definition, 1,
            "VOLUME_3D_ELEMENT_REPRESENTATION elements must contain entity references"),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepZoneRepresentation resolveZoneRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ZONE_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepZoneRepresentation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.entityReferenceList(instance, definition, 2,
            "ZONE_REPRESENTATION items must contain entity references"));
  }

  // === Property Representation Entities ===

  StepActionPropertyRepresentation resolveActionPropertyRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ACTION_PROPERTY_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepActionPropertyRepresentation(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepPropertyDefinition.class,
            "ACTION_PROPERTY_REPRESENTATION definition must reference PROPERTY_DEFINITION"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepRepresentation.class,
            "ACTION_PROPERTY_REPRESENTATION used_representation must reference REPRESENTATION"));
  }

  StepContactRatioRepresentation resolveContactRatioRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CONTACT_RATIO_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepContactRatioRepresentation(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepPropertyDefinition.class,
            "CONTACT_RATIO_REPRESENTATION definition must reference PROPERTY_DEFINITION"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepRepresentation.class,
            "CONTACT_RATIO_REPRESENTATION used_representation must reference REPRESENTATION"));
  }

  StepFeaGroupRepresentation resolveFeaGroupRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FEA_GROUP_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepFeaGroupRepresentation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.entityReferenceList(instance, definition, 1,
            "FEA_GROUP_REPRESENTATION representations must contain entity references"),
        resolver.stringValue(instance, definition, 2));
  }

  StepFeaMaterialPropertyRepresentation resolveFeaMaterialPropertyRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FEA_MATERIAL_PROPERTY_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepFeaMaterialPropertyRepresentation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.entityReferenceList(instance, definition, 2,
            "FEA_MATERIAL_PROPERTY_REPRESENTATION properties must contain entity references"));
  }

  StepPropertyDefinitionRepresentation resolvePropertyDefinitionRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PROPERTY_DEFINITION_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepPropertyDefinitionRepresentation(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepPropertyDefinition.class,
            "PROPERTY_DEFINITION_REPRESENTATION definition must reference PROPERTY_DEFINITION"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepRepresentation.class,
            "PROPERTY_DEFINITION_REPRESENTATION used_representation must reference REPRESENTATION"));
  }

  StepResourcePropertyRepresentation resolveResourcePropertyRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "RESOURCE_PROPERTY_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepResourcePropertyRepresentation(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepPropertyDefinition.class,
            "RESOURCE_PROPERTY_REPRESENTATION definition must reference PROPERTY_DEFINITION"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepRepresentation.class,
            "RESOURCE_PROPERTY_REPRESENTATION used_representation must reference REPRESENTATION"));
  }

  StepValidationPropertyRepresentation resolveValidationPropertyRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "VALIDATION_PROPERTY_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepValidationPropertyRepresentation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.entityReferenceList(instance, definition, 2,
            "VALIDATION_PROPERTY_REPRESENTATION items must contain entity references"),
        resolver.resolve(resolver.referenceId(instance, definition, 3)));
  }

  // === Shape Aspect Entities ===

  StepCompositeShapeAspect resolveCompositeShapeAspect(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "COMPOSITE_SHAPE_ASPECT");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepCompositeShapeAspect(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.booleanValue(instance, definition, 3));
  }

  StepShapeAspect resolveShapeAspect(StepEntityInstance instance) {
    return resolveShapeAspect(instance, "SHAPE_ASPECT");
  }

  StepShapeAspect resolveShapeAspect(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    String productDefinitional = resolver.enumValue(instance, definition, 3);
    if (!List.of("T", "F", "U").contains(productDefinitional)) {
      throw new StepResolutionException(
          entityName + " product_definitional must be .T., .F. or .U.");
    }
    return new StepShapeAspect(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepProductDefinitionShape.class,
            entityName + " of_shape must reference PRODUCT_DEFINITION_SHAPE"),
        productDefinitional,
        entityName);
  }

  StepShapeAspectOccurrence resolveShapeAspectOccurrence(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    String productDefinitional = resolver.enumValue(instance, definition, 3);
    if (!List.of("T", "F", "U").contains(productDefinitional)) {
      throw new StepResolutionException(
          entityName + " product_definitional must be .T., .F. or .U.");
    }
    return new StepShapeAspectOccurrence(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepProductDefinitionShape.class,
            entityName + " of_shape must reference PRODUCT_DEFINITION_SHAPE"),
        productDefinitional,
        resolver.resolve(resolver.referenceId(instance, definition, 4)),
        entityName);
  }

  StepShapeAspectOccurrence resolveShapeAspectOccurrence(StepEntityInstance instance) {
    String entityName = instance.name();
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepShapeAspectOccurrence(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.requireEntity(resolver.referenceId(instance, definition, 2), com.minicad.step.model.StepProductDefinitionShape.class,
            "SHAPE_ASPECT_OCCURRENCE ofShape must reference PRODUCT_DEFINITION_SHAPE"),
        StepResolverValueHelpers.logicalValue(instance, definition, 3),
        resolver.resolve(resolver.referenceId(instance, definition, 4)),
        entityName);
  }

  StepShapeAspectRelationship resolveShapeAspectRelationship(StepEntityInstance instance) {
    return resolveShapeAspectRelationship(instance, "SHAPE_ASPECT_RELATIONSHIP");
  }

  StepShapeAspectRelationship resolveShapeAspectRelationship(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepShapeAspectRelationship(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.optionalStringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        entityName);
  }
}
