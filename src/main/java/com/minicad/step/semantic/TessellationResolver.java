package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedStepEntityException;
import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;
import com.minicad.step.syntax.StepValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Tessellation resolver - handles tessellated geometry and mesh entities.
 * Extracted from StepEntityResolver to reduce file size and improve maintainability.
 * Contains tessellated faces/sets/coordinates and finite element mesh entities.
 */
final class TessellationResolver {

  private final StepEntityResolver resolver;

  TessellationResolver(StepEntityResolver resolver) {
    this.resolver = resolver;
  }

  // === Tessellation Entities ===

  StepComplexTriangulatedFace resolveComplexTriangulatedFace(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "COMPLEX_TRIANGULATED_FACE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepComplexTriangulatedFace(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.entityReferenceList(instance, definition, 1,
            "COMPLEX_TRIANGULATED_FACE boundaries must contain entity references"),
        resolver.entityReferenceList(instance, definition, 2,
            "COMPLEX_TRIANGULATED_FACE vertices must contain entity references"));
  }

  StepFiniteElementMesh resolveFiniteElementMesh(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FINITE_ELEMENT_MESH");
    StepEntityResolver.requireParameterCount(instance, definition, 6);
    String meshType = resolver.stringValue(instance, definition, 1);
    List<StepEntity> nodes = resolver.entityReferenceList(
        instance, definition, 2, "FINITE_ELEMENT_MESH nodes must contain entity references");
    List<StepEntity> elements = resolver.entityReferenceList(
        instance, definition, 3, "FINITE_ELEMENT_MESH elements must contain entity references");
    StepValue elementTypesValue = resolver.unwrapTyped(definition.parameters().get(4));
    List<String> elementTypes = new ArrayList<>();
    if (elementTypesValue instanceof StepValue.ListValue) {
      StepValue.ListValue typeList = (StepValue.ListValue) elementTypesValue;
      for (StepValue typeElement : typeList.elements()) {
        if (typeElement instanceof StepValue.StringValue) {
          StepValue.StringValue sv = (StepValue.StringValue) typeElement;
          elementTypes.add(sv.value());
        } else if (typeElement instanceof StepValue.TypedValue) {
          StepValue.TypedValue tv = (StepValue.TypedValue) typeElement;
          if (tv.value() instanceof StepValue.StringValue) {
            StepValue.StringValue sv = (StepValue.StringValue) tv.value();
            elementTypes.add(sv.value());
          }
        }
      }
    }
    double meshDensity = resolver.numberValue(instance, definition, 5);
    return new StepFiniteElementMesh(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        meshType,
        List.copyOf(nodes),
        List.copyOf(elements),
        List.copyOf(elementTypes),
        meshDensity);
  }

  StepPolygonalBoundedHalfSpace resolvePolygonalBoundedHalfSpace(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "POLYGONAL_BOUNDED_HALF_SPACE");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    StepEntity basisSurface = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!resolver.isSupportedSurfaceReference(basisSurface)) {
      throw new UnsupportedStepEntityException(
          "POLYGONAL_BOUNDED_HALF_SPACE basis_surface must reference a supported surface");
    }
    List<StepCartesianPoint> polygonPoints =
        resolver.referenceList(
            instance,
            definition,
            3,
            StepCartesianPoint.class,
            "POLYGONAL_BOUNDED_HALF_SPACE points must reference CARTESIAN_POINT");
    return new StepPolygonalBoundedHalfSpace(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        basisSurface,
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepAxis2Placement3D.class,
            "POLYGONAL_BOUNDED_HALF_SPACE position must reference AXIS2_PLACEMENT_3D"),
        polygonPoints,
        resolver.booleanValue(instance, definition, 4));
  }

  StepTessellatedCoordinateSet resolveTessellatedCoordinateSet(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TESSELLATED_COORDINATE_SET");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepTessellatedCoordinateSet(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.entityReferenceList(instance, definition, 1, "TESSELLATED_COORDINATE_SET coordinates must contain entity references"));
  }

  StepTessellatedFace resolveTessellatedFace(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TESSELLATED_FACE");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    List<StepEntity> triangles = resolver.entityReferenceList(
        instance, definition, 1, "TESSELLATED_FACE triangles must contain entity references");
    return new StepTessellatedFace(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        triangles);
  }

  StepTessellatedFaceSet resolveTessellatedFaceSet(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TESSELLATED_FACE_SET");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    List<StepEntity> coordinateEntities =
        resolver.entityReferenceList(
            instance, definition, 2, "TESSELLATED_FACE_SET coordinates must contain entity references");
    List<StepCartesianPoint> coordinates = new ArrayList<>();
    for (StepEntity entity : coordinateEntities) {
      if (!(entity instanceof StepCartesianPoint)) {
        throw new StepResolutionException(
            "TESSELLATED_FACE_SET coordinates must contain CARTESIAN_POINT entities");
      }
      StepCartesianPoint point = (StepCartesianPoint) entity;
      coordinates.add(point);
    }
    StepValue value = resolver.unwrapTyped(definition.parameters().get(3));
    if (!(value instanceof StepValue.ListValue)) {
      throw new StepResolutionException(
          "TESSELLATED_FACE_SET parameter 3 must be a list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    List<List<Integer>> faceIndices = new ArrayList<>();
    for (StepValue element : listValue.elements()) {
      if (!(element instanceof StepValue.ListValue)) {
        throw new StepResolutionException(
            "TESSELLATED_FACE_SET face indices must be lists of integers");
      }
      StepValue.ListValue innerList = (StepValue.ListValue) element;
      List<Integer> indices = new ArrayList<>();
      for (StepValue innerElement : innerList.elements()) {
        if (!(innerElement instanceof StepValue.NumberValue)) {
          throw new StepResolutionException(
              "TESSELLATED_FACE_SET face indices must be integers");
        }
        StepValue.NumberValue numValue = (StepValue.NumberValue) innerElement;
        indices.add((int) numValue.value());
      }
      faceIndices.add(List.copyOf(indices));
    }
    return new StepTessellatedFaceSet(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        coordinates,
        List.copyOf(faceIndices));
  }

  StepTessellatedTriangle resolveTessellatedTriangle(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TESSELLATED_TRIANGLE");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    StepEntity v1 = resolver.resolve(resolver.referenceId(instance, definition, 2));
    StepEntity v2 = resolver.resolve(resolver.referenceId(instance, definition, 3));
    StepEntity v3 = resolver.resolve(resolver.referenceId(instance, definition, 4));
    if (!(v1 instanceof StepVertexPoint) && !(v1 instanceof StepVertex)) {
      throw new StepResolutionException(
          "TESSELLATED_TRIANGLE vertex1 must reference VERTEX but got " + v1.getClass().getSimpleName());
    }
    if (!(v2 instanceof StepVertexPoint) && !(v2 instanceof StepVertex)) {
      throw new StepResolutionException(
          "TESSELLATED_TRIANGLE vertex2 must reference VERTEX but got " + v2.getClass().getSimpleName());
    }
    if (!(v3 instanceof StepVertexPoint) && !(v3 instanceof StepVertex)) {
      throw new StepResolutionException(
          "TESSELLATED_TRIANGLE vertex3 must reference VERTEX but got " + v3.getClass().getSimpleName());
    }
    return new StepTessellatedTriangle(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        v1, v2, v3);
  }

  StepTriangulatedFace resolveTriangulatedFace(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TRIANGULATED_FACE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepTriangulatedFace(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.entityReferenceList(instance, definition, 1,
            "TRIANGULATED_FACE vertices must contain entity references"),
        resolver.integerList(instance, definition, 2));
  }
}
