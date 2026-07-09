package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedStepEntityException;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepFaceEntity;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepManifoldSurfaceModel;
import com.minicad.step.model.StepOpenPath;
import com.minicad.step.model.StepOrientedPath;
import com.minicad.step.model.StepPath;
import com.minicad.step.model.StepSubpath;
import com.minicad.step.model.StepSurfacedEdgeCurve;
import com.minicad.step.model.StepSurfacedOpenShell;
import com.minicad.step.model.StepEdgeBasedWireframeModel;
import com.minicad.step.model.StepFacettedBrep;
import com.minicad.step.model.StepFaceBasedSurfaceModel;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepNonManifoldSolidBrep;
import com.minicad.step.model.StepShellBasedSurfaceModel;
import com.minicad.step.model.StepAdvancedFace;
import com.minicad.step.model.StepClosedShell;
import com.minicad.step.model.StepConnectedEdgeSet;
import com.minicad.step.model.StepConnectedFaceSet;
import com.minicad.step.model.StepConnectedFaceSubSet;
import com.minicad.step.model.StepEdge;
import com.minicad.step.model.StepEdgeCurve;
import com.minicad.step.model.StepEdgeLoop;
import com.minicad.step.model.StepFaceBound;
import com.minicad.step.model.StepFaceSurface;
import com.minicad.step.model.StepLoop;
import com.minicad.step.model.StepOpenShell;
import com.minicad.step.model.StepOrientedClosedShell;
import com.minicad.step.model.StepOrientedEdge;
import com.minicad.step.model.StepOrientedFace;
import com.minicad.step.model.StepOrientedOpenShell;
import com.minicad.step.model.StepPolyLoop;
import com.minicad.step.model.StepSubedge;
import com.minicad.step.model.StepVertex;
import com.minicad.step.model.StepVertexLoop;
import com.minicad.step.model.StepVertexPoint;
import com.minicad.step.model.StepVertexShell;
import com.minicad.step.model.StepWireShell;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Topology resolver - handles resolution of STEP topological entities.
 * Extracted from StepEntityResolver to reduce its size and improve maintainability.
 */
final class StepTopologyResolver {

  private final StepEntityResolver resolver;

  StepTopologyResolver(StepEntityResolver resolver) {
    this.resolver = resolver;
  }

  // === Vertex ===

  StepVertexPoint resolveVertexPoint(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "VERTEX_POINT");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepVertexPoint(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepCartesianPoint.class,
            "VERTEX_POINT geometry must reference CARTESIAN_POINT"));
  }

  // === Edge ===

  StepEdgeCurve resolveEdgeCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "EDGE_CURVE");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    StepEntity edgeGeometry = resolver.resolve(resolver.referenceId(instance, definition, 3));
    if (!resolver.isSupportedCurveReference(edgeGeometry)) {
      throw new UnsupportedStepEntityException(
          "EDGE_CURVE geometry must reference a supported curve");
    }
    return new StepEdgeCurve(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepVertexPoint.class,
            "EDGE_CURVE start must reference VERTEX_POINT"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepVertexPoint.class,
            "EDGE_CURVE end must reference VERTEX_POINT"),
        edgeGeometry,
        resolver.booleanValue(instance, definition, 4));
  }

  StepOrientedEdge resolveOrientedEdge(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ORIENTED_EDGE");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    StepEdgeCurve edgeElement =
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepEdgeCurve.class,
            "ORIENTED_EDGE edge_element must reference EDGE_CURVE");
    boolean orientation = resolver.booleanValue(instance, definition, 4);
    if (!resolver.isUnset(definition.parameters().get(1)) || !resolver.isUnset(definition.parameters().get(2))) {
      StepVertexPoint explicitStart =
          resolver.requireEntity(
              resolver.referenceId(instance, definition, 1),
              StepVertexPoint.class,
              "ORIENTED_EDGE edge_start must reference VERTEX_POINT");
      StepVertexPoint explicitEnd =
          resolver.requireEntity(
              resolver.referenceId(instance, definition, 2),
              StepVertexPoint.class,
              "ORIENTED_EDGE edge_end must reference VERTEX_POINT");
      StepVertexPoint expectedStart = orientation ? edgeElement.getStart() : edgeElement.getEnd();
      StepVertexPoint expectedEnd = orientation ? edgeElement.getEnd() : edgeElement.getStart();
      if (explicitStart.id() != expectedStart.id() || explicitEnd.id() != expectedEnd.id()) {
        throw new StepResolutionException(
            "ORIENTED_EDGE explicit edge_start/edge_end must match edge_element orientation");
      }
    }
    return new StepOrientedEdge(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        edgeElement,
        orientation);
  }

  StepSubedge resolveSubedge(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SUBEDGE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    StepEntity parentEdge = resolver.resolve(resolver.referenceId(instance, definition, 3));
    if (!(parentEdge instanceof StepEdgeCurve) && !(parentEdge instanceof StepSubedge)) {
      throw new UnsupportedStepEntityException(
          "SUBEDGE parent_edge must reference EDGE_CURVE or SUBEDGE");
    }
    if (parentEdge.id() == instance.id()) {
      throw new UnsupportedStepEntityException("SUBEDGE parent_edge must not self-reference");
    }
    return new StepSubedge(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        requireVertexLike(
            resolver.referenceId(instance, definition, 1),
            "SUBEDGE edge_start must reference VERTEX or VERTEX_POINT"),
        requireVertexLike(
            resolver.referenceId(instance, definition, 2),
            "SUBEDGE edge_end must reference VERTEX or VERTEX_POINT"),
        parentEdge);
  }

  StepConnectedEdgeSet resolveConnectedEdgeSet(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CONNECTED_EDGE_SET");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    List<StepEntity> edges = resolver.entityReferenceList(
        instance,
        definition,
        1,
        "CONNECTED_EDGE_SET edges must contain edge references");
    for (StepEntity edge : edges) {
      if (!(edge instanceof StepEdgeCurve)
          && !(edge instanceof StepOrientedEdge)
          && !(edge instanceof StepSubedge)
          && !(edge instanceof StepEdge)) {
        throw new UnsupportedStepEntityException(
            "CONNECTED_EDGE_SET edges must reference EDGE subtypes");
      }
    }
    return new StepConnectedEdgeSet(instance.id(), resolver.stringValue(instance, definition, 0), edges);
  }

  StepEdgeBasedWireframeModel resolveEdgeBasedWireframeModel(
      StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "EDGE_BASED_WIREFRAME_MODEL");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepEdgeBasedWireframeModel(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepConnectedEdgeSet.class,
            "EDGE_BASED_WIREFRAME_MODEL ebwm_boundary must contain CONNECTED_EDGE_SET references"));
  }

  // === Path/Loop ===

  StepEdgeLoop resolveEdgeLoop(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "EDGE_LOOP");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    List<StepOrientedEdge> edges =
        resolver.referenceList(
            instance,
            definition,
            1,
            StepOrientedEdge.class,
            "EDGE_LOOP edge list must contain ORIENTED_EDGE references");
    return new StepEdgeLoop(instance.id(), resolver.stringValue(instance, definition, 0), edges);
  }

  StepPath resolvePath(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PATH");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    List<StepOrientedEdge> edges =
        resolver.referenceList(
            instance,
            definition,
            1,
            StepOrientedEdge.class,
            "PATH edge list must contain ORIENTED_EDGE references");
    return new StepPath(instance.id(), resolver.stringValue(instance, definition, 0), edges);
  }

  StepOpenPath resolveOpenPath(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "OPEN_PATH");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    List<StepOrientedEdge> edges =
        resolver.referenceList(
            instance,
            definition,
            1,
            StepOrientedEdge.class,
            "OPEN_PATH edge list must contain ORIENTED_EDGE references");
    if (!edges.isEmpty()) {
      StepOrientedEdge first = edges.get(0);
      StepOrientedEdge last = edges.get(edges.size() - 1);
      if (orientedEdgeStartId(first) == orientedEdgeEndId(last)) {
        throw new UnsupportedStepEntityException(
            "OPEN_PATH start vertex must differ from end vertex");
      }
    }
    return new StepOpenPath(instance.id(), resolver.stringValue(instance, definition, 0), edges);
  }

  StepSubpath resolveSubpath(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SUBPATH");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    List<StepOrientedEdge> edges =
        resolver.referenceList(
            instance,
            definition,
            1,
            StepOrientedEdge.class,
            "SUBPATH edge list must contain ORIENTED_EDGE references");
    StepEntity parentPath = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!isPathEntity(parentPath)) {
      throw new UnsupportedStepEntityException(
          "SUBPATH parent_path must reference PATH, OPEN_PATH, SUBPATH, ORIENTED_PATH or EDGE_LOOP");
    }
    if (parentPath.id() == instance.id()) {
      throw new UnsupportedStepEntityException("SUBPATH parent_path must not reference itself");
    }
    return new StepSubpath(instance.id(), resolver.stringValue(instance, definition, 0), edges, parentPath);
  }

  StepOrientedPath resolveOrientedPath(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ORIENTED_PATH");
    StepEntityResolver.requireParameterCountIn(instance, definition, 2, 3);
    boolean hasStandaloneName = definition.parameters().size() == 3;
    String name =
        hasStandaloneName
            ? resolver.stringValue(instance, definition, 0)
            : instance.hasDefinition("PATH") ? resolver.stringValue(instance, resolver.definition(instance, "PATH"), 0) : "";
    int pathIndex = hasStandaloneName ? 1 : 0;
    StepEntity pathElement = resolver.resolve(resolver.referenceId(instance, definition, pathIndex));
    if (!isPathEntity(pathElement)) {
      throw new UnsupportedStepEntityException(
          "ORIENTED_PATH path_element must reference PATH, OPEN_PATH, SUBPATH, ORIENTED_PATH or EDGE_LOOP");
    }
    boolean orientation = resolver.booleanValue(instance, definition, pathIndex + 1);
    List<StepOrientedEdge> sourceEdges = pathEdges(pathElement);
    List<StepOrientedEdge> edges;
    if (orientation) {
      edges = sourceEdges;
    } else {
      List<StepOrientedEdge> reversed = new ArrayList<>(sourceEdges);
      Collections.reverse(reversed);
      edges = reversed;
    }
    return new StepOrientedPath(instance.id(), name, pathElement, orientation, edges);
  }

  StepVertexLoop resolveVertexLoop(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "VERTEX_LOOP");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepVertexLoop(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepVertexPoint.class,
            "VERTEX_LOOP loop_vertex must reference VERTEX_POINT"));
  }

  StepPolyLoop resolvePolyLoop(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "POLY_LOOP");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    List<StepCartesianPoint> polygon =
        resolver.referenceList(
            instance,
            definition,
            1,
            StepCartesianPoint.class,
            "POLY_LOOP polygon must contain CARTESIAN_POINT references");
    if (polygon.size() < 3) {
      throw new UnsupportedStepEntityException("POLY_LOOP requires at least 3 points");
    }
    return new StepPolyLoop(instance.id(), resolver.stringValue(instance, definition, 0), polygon);
  }

  // === Face ===

  StepFaceBound resolveFaceBound(StepEntityInstance instance, boolean outer) {
    StepEntityDefinition definition =
        resolver.definition(instance, outer ? "FACE_OUTER_BOUND" : "FACE_BOUND");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepFaceBound(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepLoop.class,
            "FACE_BOUND loop must reference LOOP subtype"),
        resolver.booleanValue(instance, definition, 2),
        outer);
  }

  StepAdvancedFace resolveAdvancedFace(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ADVANCED_FACE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    StepEntity faceGeometry = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!resolver.isSupportedSurfaceReference(faceGeometry)) {
      throw new UnsupportedStepEntityException(
          "ADVANCED_FACE geometry must reference a supported surface but got "
              + faceGeometry.getClass().getSimpleName());
    }
    return new StepAdvancedFace(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepFaceBound.class,
            "ADVANCED_FACE bounds must contain FACE_BOUND references"),
        faceGeometry,
        resolver.booleanValue(instance, definition, 3));
  }

  StepFaceSurface resolveFaceSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FACE_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    StepEntity faceGeometry = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!resolver.isSupportedSurfaceReference(faceGeometry)) {
      throw new UnsupportedStepEntityException(
          "FACE_SURFACE geometry must reference a supported surface but got "
              + faceGeometry.getClass().getSimpleName());
    }
    return new StepFaceSurface(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepFaceBound.class,
            "FACE_SURFACE bounds must contain FACE_BOUND references"),
        faceGeometry,
        resolver.booleanValue(instance, definition, 3));
  }

  StepOrientedFace resolveOrientedFace(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ORIENTED_FACE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepFaceEntity faceElement =
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepFaceEntity.class,
            "ORIENTED_FACE face_element must reference FACE subtype");
    return new StepOrientedFace(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        faceElement,
        resolver.booleanValue(instance, definition, 2));
  }

  // === Shell ===

  StepOpenShell resolveOpenShell(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "OPEN_SHELL");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepOpenShell(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepFaceEntity.class,
            "OPEN_SHELL faces must contain FACE subtype references"));
  }

  StepClosedShell resolveClosedShell(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CLOSED_SHELL");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepClosedShell(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepFaceEntity.class,
            "CLOSED_SHELL faces must contain FACE subtype references"));
  }

  StepSurfacedOpenShell resolveSurfacedOpenShell(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACED_OPEN_SHELL");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    List<StepFaceEntity> faces =
        resolver.referenceList(
            instance,
            definition,
            1,
            StepFaceEntity.class,
            "SURFACED_OPEN_SHELL faces must contain FACE subtype references");
    for (StepFaceEntity face : faces) {
      if (!(face instanceof StepFaceSurface)) {
        throw new StepResolutionException(
            "SURFACED_OPEN_SHELL faces must reference FACE_SURFACE or subtype");
      }
    }
    return new StepSurfacedOpenShell(instance.id(), resolver.stringValue(instance, definition, 0), faces);
  }

  StepOrientedOpenShell resolveOrientedOpenShell(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ORIENTED_OPEN_SHELL");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity openShellElement = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!(openShellElement instanceof StepOpenShell)
        && !(openShellElement instanceof StepSurfacedOpenShell)
        && !(openShellElement instanceof StepOrientedOpenShell)) {
      throw new StepResolutionException(
          "ORIENTED_OPEN_SHELL open_shell_element must reference OPEN_SHELL");
    }
    return new StepOrientedOpenShell(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        openShellElement,
        resolver.booleanValue(instance, definition, 2));
  }

  StepOrientedClosedShell resolveOrientedClosedShell(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ORIENTED_CLOSED_SHELL");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity closedShellElement = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!(closedShellElement instanceof StepClosedShell)
        && !(closedShellElement instanceof StepOrientedClosedShell)) {
      throw new StepResolutionException(
          "ORIENTED_CLOSED_SHELL closed_shell_element must reference CLOSED_SHELL");
    }
    return new StepOrientedClosedShell(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        closedShellElement,
        resolver.booleanValue(instance, definition, 2));
  }

  StepConnectedFaceSet resolveConnectedFaceSet(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CONNECTED_FACE_SET");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepConnectedFaceSet(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepFaceEntity.class,
            "CONNECTED_FACE_SET cfs_faces must contain FACE subtype references"));
  }

  StepConnectedFaceSubSet resolveConnectedFaceSubSet(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CONNECTED_FACE_SUB_SET");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity parentFaceSet = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!isConnectedFaceSetEntity(parentFaceSet)) {
      throw new StepResolutionException(
          "CONNECTED_FACE_SUB_SET parent_face_set must reference CONNECTED_FACE_SET or CONNECTED_FACE_SUB_SET");
    }
    if (parentFaceSet.id() == instance.id()) {
      throw new StepResolutionException(
          "CONNECTED_FACE_SUB_SET parent_face_set cannot reference itself");
    }
    return new StepConnectedFaceSubSet(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepFaceEntity.class,
            "CONNECTED_FACE_SUB_SET cfs_faces must contain FACE subtype references"),
        parentFaceSet);
  }

  StepVertexShell resolveVertexShell(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "VERTEX_SHELL");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepVertexShell(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepVertexLoop.class,
            "VERTEX_SHELL vertex_shell_extent must reference VERTEX_LOOP"));
  }

  StepWireShell resolveWireShell(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "WIRE_SHELL");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    List<StepLoop> loops =
        resolver.referenceList(
            instance,
            definition,
            1,
            StepLoop.class,
            "WIRE_SHELL wire_shell_extent must contain LOOP references");
    return new StepWireShell(instance.id(), resolver.stringValue(instance, definition, 0), loops);
  }

  // === Brep ===

  StepManifoldSolidBrep resolveManifoldSolidBrep(StepEntityInstance instance) {
    return resolveManifoldSolidBrep(instance, "MANIFOLD_SOLID_BREP");
  }

  StepManifoldSolidBrep resolveManifoldSolidBrep(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    StepEntity outer = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!isClosedShellEntity(outer)) {
      throw new StepResolutionException(entityName + " outer must reference CLOSED_SHELL");
    }
    return new StepManifoldSolidBrep(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        outer);
  }

  StepNonManifoldSolidBrep resolveNonManifoldSolidBrep(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "NON_MANIFOLD_SOLID_BREP");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    StepEntity outer = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!(outer instanceof StepClosedShell) && !(outer instanceof StepOpenShell)) {
      throw new StepResolutionException(
          "NON_MANIFOLD_SOLID_BREP outer must reference CLOSED_SHELL or OPEN_SHELL");
    }
    return new StepNonManifoldSolidBrep(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        outer);
  }

  StepFacettedBrep resolveFacettedBrep(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FACETTED_BREP");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    StepEntity outer = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!isClosedShellEntity(outer) && !isOpenShellEntity(outer)) {
      throw new StepResolutionException(
          "FACETTED_BREP outer must reference CLOSED_SHELL or OPEN_SHELL");
    }
    return new StepFacettedBrep(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        outer);
  }

  StepShellBasedSurfaceModel resolveShellBasedSurfaceModel(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SHELL_BASED_SURFACE_MODEL");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    List<StepEntity> shells =
        resolver.entityReferenceList(
            instance,
            definition,
            1,
            "SHELL_BASED_SURFACE_MODEL shells must contain shell references");
    for (StepEntity shell : shells) {
      if (!isShellEntity(shell)) {
        throw new StepResolutionException(
            "SHELL_BASED_SURFACE_MODEL shells must reference OPEN_SHELL, ORIENTED_OPEN_SHELL, CLOSED_SHELL or ORIENTED_CLOSED_SHELL");
      }
    }
    return new StepShellBasedSurfaceModel(
        instance.id(), resolver.stringValue(instance, definition, 0), shells);
  }

  StepFaceBasedSurfaceModel resolveFaceBasedSurfaceModel(
      StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FACE_BASED_SURFACE_MODEL");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    List<StepEntity> faceSets =
        resolver.entityReferenceList(
            instance,
            definition,
            1,
            "FACE_BASED_SURFACE_MODEL fbsm_faces must contain connected face sets");
    for (StepEntity faceSet : faceSets) {
      if (!isConnectedFaceSetEntity(faceSet)
          && !isShellEntity(faceSet)) {
        throw new StepResolutionException(
            "FACE_BASED_SURFACE_MODEL fbsm_faces must reference CONNECTED_FACE_SET, CONNECTED_FACE_SUB_SET, OPEN_SHELL, ORIENTED_OPEN_SHELL, CLOSED_SHELL or ORIENTED_CLOSED_SHELL");
      }
    }
    return new StepFaceBasedSurfaceModel(
        instance.id(), resolver.stringValue(instance, definition, 0), faceSets);
  }

  StepManifoldSurfaceModel resolveManifoldSurfaceModel(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MANIFOLD_SURFACE_MODEL");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    List<StepEntity> shells =
        resolver.entityReferenceList(
            instance,
            definition,
            1,
            "MANIFOLD_SURFACE_MODEL shells must contain shell references");
    return new StepManifoldSurfaceModel(
        instance.id(), resolver.stringValue(instance, definition, 0), shells);
  }

  StepSurfacedEdgeCurve resolveSurfacedEdgeCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACED_EDGE_CURVE");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepSurfacedEdgeCurve(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.booleanValue(instance, definition, 4));
  }

  // === Helper methods ===

  private static int orientedEdgeStartId(StepOrientedEdge edge) {
    return edge.isOrientation() ? edge.edgeElement().getStart().id() : edge.edgeElement().getEnd().id();
  }

  private static int orientedEdgeEndId(StepOrientedEdge edge) {
    return edge.isOrientation() ? edge.edgeElement().getEnd().id() : edge.edgeElement().getStart().id();
  }

  static boolean isPathEntity(StepEntity entity) {
    return entity instanceof StepPath
        || entity instanceof StepOpenPath
        || entity instanceof StepSubpath
        || entity instanceof StepOrientedPath
        || entity instanceof StepEdgeLoop;
  }

  private static List<StepOrientedEdge> pathEdges(StepEntity entity) {
    if (entity instanceof StepPath) {
      StepPath path = (StepPath) entity;
      return path.edges();
    }
    if (entity instanceof StepOpenPath) {
      StepOpenPath openPath = (StepOpenPath) entity;
      return openPath.edges();
    }
    if (entity instanceof StepSubpath) {
      StepSubpath subpath = (StepSubpath) entity;
      return subpath.edges();
    }
    if (entity instanceof StepOrientedPath) {
      StepOrientedPath orientedPath = (StepOrientedPath) entity;
      return orientedPath.edges();
    }
    if (entity instanceof StepEdgeLoop) {
      StepEdgeLoop edgeLoop = (StepEdgeLoop) entity;
      return edgeLoop.edges();
    }
    throw new IllegalArgumentException("Unknown value type: " + entity);
  }

  boolean isOpenShellEntity(StepEntity entity) {
    return entity instanceof StepOpenShell
        || entity instanceof StepSurfacedOpenShell
        || entity instanceof StepOrientedOpenShell;
  }

  boolean isClosedShellEntity(StepEntity entity) {
    return entity instanceof StepClosedShell || entity instanceof StepOrientedClosedShell;
  }

  boolean isShellEntity(StepEntity entity) {
    return isOpenShellEntity(entity) || isClosedShellEntity(entity);
  }

  boolean isConnectedFaceSetEntity(StepEntity entity) {
    return entity instanceof StepConnectedFaceSet || entity instanceof StepConnectedFaceSubSet;
  }

  private StepEntity requireVertexLike(int id, String message) {
    StepEntity entity = resolver.resolve(id);
    if (!(entity instanceof StepVertex) && !(entity instanceof StepVertexPoint)) {
      throw new StepResolutionException(message + " but got " + entity.getClass().getSimpleName());
    }
    return entity;
  }
}