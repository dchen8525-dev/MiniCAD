package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.CompositeCurve3;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.SurfaceGeometry;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.base.StepFaceEntity;
import com.minicad.step.model.geometry.StepOpenPath;
import com.minicad.step.model.geometry.StepOrientedPath;
import com.minicad.step.model.geometry.StepPath;
import com.minicad.step.model.geometry.StepSubpath;
import com.minicad.step.model.manufacturing.StepMachinedSurface;
import com.minicad.step.model.product.StepMappedItem;
import com.minicad.step.model.topology.StepAdvancedFace;
import com.minicad.step.model.topology.StepEdgeLoop;
import com.minicad.step.model.topology.StepFace;
import com.minicad.step.model.topology.StepFaceBound;
import com.minicad.step.model.topology.StepFaceSurface;
import com.minicad.step.model.topology.StepOrientedEdge;
import com.minicad.step.model.topology.StepOrientedFace;
import com.minicad.step.model.topology.StepOrientedSubface;
import com.minicad.step.model.topology.StepPolyLoop;
import com.minicad.step.model.topology.StepSubface;
import com.minicad.step.model.topology.StepVertexLoop;
import com.minicad.topology.Edge;
import com.minicad.topology.EdgeLoop;
import com.minicad.topology.Face;
import com.minicad.topology.Loop;
import com.minicad.topology.OrientedEdge;
import com.minicad.topology.VertexLoop;

import java.util.ArrayList;
import java.util.List;

final class StepTopologyBuilder {

    private final StepCadBuilder builder;

    StepTopologyBuilder(StepCadBuilder builder) {
        this.builder = builder;
    }

    OrientedEdge buildOrientedEdge(int id) {
        StepOrientedEdge stepOrientedEdge = builder.requireEntity(id, StepOrientedEdge.class, "ORIENTED_EDGE");
        return new OrientedEdge(builder.buildEdge(stepOrientedEdge.edgeElement().id()), stepOrientedEdge.orientation());
    }

    EdgeLoop buildEdgeLoop(int id) {
        StepEdgeLoop loop = builder.requireEntity(id, StepEdgeLoop.class, "EDGE_LOOP");
        return new EdgeLoop(loop.edges().stream().map(edge -> builder.buildOrientedEdge(edge.id())).toList());
    }

    VertexLoop buildVertexLoop(int id) {
        StepVertexLoop loop = builder.requireEntity(id, StepVertexLoop.class, "VERTEX_LOOP");
        return new VertexLoop(builder.buildVertex(loop.loopVertex().id()));
    }

    com.minicad.topology.PolyLoop buildPolyLoop(int id) {
        StepPolyLoop loop = builder.requireEntity(id, StepPolyLoop.class, "POLY_LOOP");
        return new com.minicad.topology.PolyLoop(loop.polygon().stream().map(point -> builder.buildPoint(point.id())).toList());
    }

    CompositeCurve3 buildPath(int id) {
        StepEntity entity = builder.requireExistingEntity(id);
        List<StepOrientedEdge> edges;
        boolean needsEdgeOrientationFlip = false;
        if (entity instanceof StepPath path) {
            edges = path.edges();
        } else if (entity instanceof StepOpenPath openPath) {
            edges = openPath.edges();
        } else if (entity instanceof StepSubpath subpath) {
            edges = subpath.edges();
        } else if (entity instanceof StepOrientedPath orientedPath) {
            edges = orientedPath.edges();
            needsEdgeOrientationFlip = !orientedPath.orientation();
        } else {
            throw new StepResolutionException("entity #" + id + " is not a PATH, OPEN_PATH, SUBPATH, or ORIENTED_PATH");
        }
        if (edges.isEmpty()) {
            throw new StepResolutionException("entity #" + id + " path has no edges");
        }
        List<Curve3> curves;
        if (needsEdgeOrientationFlip) {
            curves = edges.stream()
                    .map(oe -> builder.buildOrientedEdge(oe.id()))
                    .map(oe -> new OrientedEdge(oe.edge(), !oe.orientation()))
                    .map(OrientedEdge::edge)
                    .map(Edge::curve)
                    .toList();
        } else {
            curves = edges.stream()
                    .map(oe -> builder.buildOrientedEdge(oe.id()))
                    .map(OrientedEdge::edge)
                    .map(Edge::curve)
                    .toList();
        }
        return new CompositeCurve3(curves);
    }

    com.minicad.topology.FaceBound buildFaceBound(int id) {
        StepFaceBound stepFaceBound = builder.requireEntity(id, StepFaceBound.class, "FACE_BOUND");
        Loop builtLoop = switch (stepFaceBound.loop()) {
            case StepEdgeLoop edgeLoop -> builder.buildEdgeLoop(edgeLoop.id());
            case StepVertexLoop vertexLoop -> builder.buildVertexLoop(vertexLoop.id());
            case StepPolyLoop polyLoop -> builder.buildPolyLoop(polyLoop.id());
            default -> throw new UnsupportedGeometryException(
                    "FACE_BOUND construction requires EDGE_LOOP, VERTEX_LOOP, or POLY_LOOP");
        };
        return stepFaceBound.outer()
                ? com.minicad.topology.FaceBound.outer(builtLoop, stepFaceBound.orientation())
                : com.minicad.topology.FaceBound.inner(builtLoop, stepFaceBound.orientation());
    }

    Face buildFace(int id) {
        StepEntity entity = builder.requireExistingEntity(id);
        if (entity instanceof StepOrientedFace orientedFace) {
            Face baseFace = builder.buildFace(orientedFace.faceElement().id());
            return new Face(
                    baseFace.surface(),
                    baseFace.bounds(),
                    orientedFace.orientation() ? baseFace.sameSense() : !baseFace.sameSense()
            );
        }
        if (entity instanceof StepAdvancedFace advancedFace) {
            return buildFaceSurface(advancedFace, "ADVANCED_FACE");
        }
        if (entity instanceof StepFaceSurface faceSurface) {
            return buildFaceSurface(faceSurface, "FACE_SURFACE");
        }
        if (entity instanceof StepSubface subface) {
            return builder.buildFace(subface.faceElement().id());
        }
        if (entity instanceof StepOrientedSubface orientedSubface) {
            Face baseFace = builder.buildFace(orientedSubface.faceElement().id());
            return new Face(
                    baseFace.surface(),
                    baseFace.bounds(),
                    orientedSubface.orientation() ? baseFace.sameSense() : !baseFace.sameSense()
            );
        }
        if (entity instanceof StepMachinedSurface machinedSurface) {
            return builder.buildFace(machinedSurface.face().id());
        }
        if (entity instanceof StepFace face) {
            StepEntity actual = builder.resolvedEntity(face.id());
            if (actual != null && actual != face) {
                if (actual instanceof StepOrientedFace || actual instanceof StepAdvancedFace
                        || actual instanceof StepFaceSurface || actual instanceof StepSubface
                        || actual instanceof StepOrientedSubface || actual instanceof StepMachinedSurface) {
                    return builder.buildFace(actual.id());
                }
                throw new StepResolutionException("entity #" + id + " is an abstract FACE with unsupported subtype " + StepCadBuilder.stepEntityTypeName(actual));
            }
            throw new StepResolutionException("entity #" + id + " is an abstract FACE with no concrete subtype");
        }
        if (entity instanceof StepMappedItem mappedItem) {
            return builder.buildFace(mappedItem.mappingTarget().id());
        }
        throw new StepResolutionException("entity #" + id + " is not a FACE");
    }

    private Face buildFaceSurface(StepFaceEntity stepFace, String faceType) {
        StepEntity geometry = StepCadBuilder.faceGeometry(stepFace);
        SurfaceGeometry supportedSurface = builder.buildSupportedFaceGeometry(geometry, faceType);
        if (supportedSurface == null) {
            String unsupportedSurfaceType = builder.describeUnsupportedFaceGeometry(geometry);
            if (unsupportedSurfaceType != null) {
                throw new UnsupportedGeometryException(faceType + " construction for " + unsupportedSurfaceType + " is unsupported");
            }
            throw new UnsupportedGeometryException(faceType + " construction requires PLANE geometry");
        }
        List<com.minicad.topology.FaceBound> bounds = stepFace.bounds().stream()
                .map(bound -> builder.buildFaceBound(bound.id()))
                .toList();
        if (bounds.stream().noneMatch(com.minicad.topology.FaceBound::outer)) {
            bounds = inferOuterBounds(bounds);
        }
        return new Face(supportedSurface, bounds, StepCadBuilder.faceSameSense(stepFace));
    }

    private static List<com.minicad.topology.FaceBound> inferOuterBounds(List<com.minicad.topology.FaceBound> bounds) {
        if (bounds.isEmpty()) {
            return bounds;
        }
        List<com.minicad.topology.FaceBound> result = new ArrayList<>();
        for (int i = 0; i < bounds.size(); i++) {
            com.minicad.topology.FaceBound bound = bounds.get(i);
            if (i == 0) {
                result.add(com.minicad.topology.FaceBound.outer(bound.loop(), bound.orientation()));
            } else {
                result.add(com.minicad.topology.FaceBound.inner(bound.loop(), bound.orientation()));
            }
        }
        return List.copyOf(result);
    }
}
