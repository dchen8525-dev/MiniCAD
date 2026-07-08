package com.minicad.step.semantic;

import com.minicad.common.Epsilon;
import com.minicad.common.StepResolutionException;
import com.minicad.common.TopologyException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.CompositeCurve3;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.SurfaceGeometry;
import com.minicad.geometry.Vector3;
import com.minicad.step.model.core.base.StepEntity;
import com.minicad.step.model.core.base.StepFaceEntity;
import com.minicad.step.model.geometry.StepOpenPath;
import com.minicad.step.model.geometry.StepOrientedPath;
import com.minicad.step.model.geometry.StepPath;
import com.minicad.step.model.geometry.StepSubpath;
import com.minicad.step.model.geometry.StepSurfaceCurve;
import com.minicad.step.model.manufacturing.StepFilletEdge;
import com.minicad.step.model.manufacturing.StepChamferEdge;
import com.minicad.step.model.manufacturing.StepMachinedSurface;
import com.minicad.step.model.product.StepMappedItem;
import com.minicad.step.model.topology.StepAdvancedFace;
import com.minicad.step.model.topology.StepEdge;
import com.minicad.step.model.topology.StepEdgeCurve;
import com.minicad.step.model.topology.StepEdgeLoop;
import com.minicad.step.model.topology.StepFace;
import com.minicad.step.model.topology.StepFaceBound;
import com.minicad.step.model.topology.StepFaceSurface;
import com.minicad.step.model.topology.StepOrientedEdge;
import com.minicad.step.model.topology.StepOrientedFace;
import com.minicad.step.model.topology.StepOrientedSubface;
import com.minicad.step.model.topology.StepPolyLoop;
import com.minicad.step.model.geometry.StepSeamEdge;
import com.minicad.step.model.topology.StepSubedge;
import com.minicad.step.model.topology.StepSubface;
import com.minicad.step.model.topology.StepVertex;
import com.minicad.step.model.topology.StepVertexLoop;
import com.minicad.step.model.topology.StepVertexPoint;
import com.minicad.topology.Edge;
import com.minicad.topology.EdgeLoop;
import com.minicad.topology.Face;
import com.minicad.topology.FaceBound;
import com.minicad.topology.Loop;
import com.minicad.topology.OrientedEdge;
import com.minicad.topology.Vertex;
import com.minicad.topology.VertexLoop;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Builds topology objects (Vertex, Edge, Loop, Face, etc.) from STEP semantic entities.
 * This builder handles the construction of topological elements from STEP entity data.
 */
final class StepCadTopologyBuilder {

    private final StepCadBuilder builder;
    private final Map<Integer, StepEntity> entitiesById;
    private final Function<Integer, CartesianPoint> pointBuilder;
    private final Function<Integer, Curve3> curve3Builder;
    private final Function<Integer, SurfaceGeometry> surfaceGeometryBuilder;

    // Caches for topology objects
    private final Map<Integer, Vertex> vertices = new LinkedHashMap<>();
    private final Map<Integer, Edge> edges = new LinkedHashMap<>();
    private final Map<Integer, OrientedEdge> orientedEdges = new LinkedHashMap<>();
    private final Map<Integer, EdgeLoop> loops = new LinkedHashMap<>();
    private final Map<Integer, VertexLoop> vertexLoops = new LinkedHashMap<>();
    private final Map<Integer, com.minicad.topology.PolyLoop> polyLoops = new LinkedHashMap<>();
    private final Map<Integer, CompositeCurve3> paths = new LinkedHashMap<>();
    private final Map<Integer, FaceBound> faceBounds = new LinkedHashMap<>();
    private final Map<Integer, Face> faces = new LinkedHashMap<>();

    /**
     * Tolerance for projecting vertices onto curves when they don't exactly lie on the curve.
     */
    private static final double VERTEX_PROJECTION_TOLERANCE = 1.0e-2;

    StepCadTopologyBuilder(
            StepCadBuilder builder,
            Map<Integer, StepEntity> entitiesById,
            Function<Integer, CartesianPoint> pointBuilder,
            Function<Integer, Curve3> curve3Builder,
            Function<Integer, SurfaceGeometry> surfaceGeometryBuilder) {
        this.builder = builder;
        this.entitiesById = entitiesById;
        this.pointBuilder = pointBuilder;
        this.curve3Builder = curve3Builder;
        this.surfaceGeometryBuilder = surfaceGeometryBuilder;
    }

    // ========================================================================
    // Vertex building
    // ========================================================================

    /**
     * Builds a topological vertex from a STEP VERTEX_POINT entity.
     *
     * @param id STEP entity id
     * @return built vertex
     */
    Vertex buildVertex(int id) {
        Vertex existing = vertices.get(id);
        if (existing != null) {
            return existing;
        }
        StepEntity entity = requireExistingEntity(id);
        Vertex built;
        if (entity instanceof StepVertexPoint) {
            StepVertexPoint vertexPoint = (StepVertexPoint) entity;
            built = new Vertex(pointBuilder.apply(vertexPoint.point().id()));
        } else if (entity instanceof StepVertex) {
            StepVertex vertex = (StepVertex) entity;
            // VERTEX is the abstract base type. In complex entity syntax,
            // the actual vertex subtype (VERTEX_POINT) may be resolved at the same ID.
            StepEntity actual = entitiesById.get(vertex.id());
            if (actual != null && actual != vertex && actual instanceof StepVertexPoint) {
                StepVertexPoint vp = (StepVertexPoint) actual;
                built = new Vertex(pointBuilder.apply(vp.point().id()));
            } else {
                throw new StepResolutionException("entity #" + id + " is an abstract VERTEX with no concrete VERTEX_POINT subtype");
            }
        } else {
            throw new StepResolutionException("entity #" + id + " is not a VERTEX_POINT or VERTEX");
        }
        vertices.put(id, built);
        return built;
    }

    // ========================================================================
    // Edge building
    // ========================================================================

    /**
     * Builds a topological edge backed by a supported curve.
     *
     * @param id STEP entity id
     * @return built edge
     */
    Edge buildEdge(int id) {
        Edge existing = edges.get(id);
        if (existing != null) {
            return existing;
        }
        StepEntity entity = requireExistingEntity(id);
        Edge built;
        if (entity instanceof StepSeamEdge) {
            StepSeamEdge seamEdge = (StepSeamEdge) entity;
            // Seam edge: start and end vertices are the same (closed edge on a surface seam).
            // In complex entity syntax, the actual curve geometry may be at the same ID.
            StepEntity actual = entitiesById.get(seamEdge.id());
            Curve3 curve = null;
            if (actual != null && actual != seamEdge) {
                if (actual instanceof StepEdgeCurve) {
                    StepEdgeCurve ec = (StepEdgeCurve) actual;
                    curve = buildCurve3FromEdgeGeometry(ec.edgeGeometry());
                } else if (actual instanceof StepSurfaceCurve) {
                    StepSurfaceCurve sc = (StepSurfaceCurve) actual;
                    curve = curve3Builder.apply(sc.id());
                }
            }
            if (curve == null) {
                throw new UnsupportedGeometryException("SEAM_EDGE #" + seamEdge.id() + " has no associated curve geometry");
            }
            Vertex vertex = buildVertex(seamEdge.edgeStart().id());
            built = buildEdgeWithProjection(vertex, vertex, curve, true);
        } else if (entity instanceof StepEdgeCurve) {
            StepEdgeCurve edgeCurve = (StepEdgeCurve) entity;
            Curve3 curve = buildCurve3FromEdgeGeometry(edgeCurve.edgeGeometry());
            Vertex startVertex = buildVertex(edgeCurve.getStart().id());
            Vertex endVertex = buildVertex(edgeCurve.getEnd().id());
            built = buildEdgeWithProjection(startVertex, endVertex, curve, edgeCurve.isSameSense());
        } else if (entity instanceof StepFilletEdge) {
            StepFilletEdge filletEdge = (StepFilletEdge) entity;
            // Fillet edge wraps an original edge - build the underlying edge geometry.
            built = buildEdge(filletEdge.originalEdge().id());
        } else if (entity instanceof StepChamferEdge) {
            StepChamferEdge chamferEdge = (StepChamferEdge) entity;
            // Chamfer edge wraps an original edge - build the underlying edge geometry.
            built = buildEdge(chamferEdge.originalEdge().id());
        } else if (entity instanceof StepSubedge) {
            StepSubedge subedge = (StepSubedge) entity;
            Edge parent = buildEdge(subedge.parentEdge().id());
            built = buildEdgeWithProjection(
                    buildVertex(subedge.getStart().id()),
                    buildVertex(subedge.getEnd().id()),
                    parent.getCurve(),
                    parent.isSameSense());
        } else if (entity instanceof StepEdge) {
            StepEdge edge = (StepEdge) entity;
            // EDGE is the abstract base type. In complex entity syntax,
            // the actual edge subtype (EDGE_CURVE, SUBEDGE) may be resolved at the same ID.
            StepEntity actual = entitiesById.get(edge.id());
            if (actual != null && actual != edge) {
                if (actual instanceof StepEdgeCurve || actual instanceof StepSubedge) {
                    built = buildEdge(actual.id());
                } else {
                    throw new StepResolutionException("entity #" + id + " is an abstract EDGE with unsupported subtype " + StepCadBuilder.stepEntityTypeName(actual));
                }
            } else {
                throw new StepResolutionException("entity #" + id + " is an abstract EDGE with no concrete subtype");
            }
        } else if (entity instanceof StepMappedItem) {
            StepMappedItem mappedItem = (StepMappedItem) entity;
            // MAPPED_ITEM: dispatch through to mapping target for edge geometry
            built = buildEdge(mappedItem.mappingTarget().id());
        } else {
            throw new StepResolutionException("entity #" + id + " is not a SEAM_EDGE, EDGE_CURVE, FILLET_EDGE, CHAMFER_EDGE, SUBEDGE or EDGE");
        }
        edges.put(id, built);
        return built;
    }

    /**
     * Builds a curve from edge geometry, handling STEP entity resolution.
     */
    private Curve3 buildCurve3FromEdgeGeometry(StepEntity edgeGeometry) {
        if (edgeGeometry instanceof StepSurfaceCurve) {
            StepSurfaceCurve sc = (StepSurfaceCurve) edgeGeometry;
            return curve3Builder.apply(sc.id());
        }
        return curve3Builder.apply(edgeGeometry.id());
    }

    /**
     * Builds an edge, projecting vertices onto the curve if needed.
     * Industrial STEP files often have vertex coordinates rounded to a limited
     * number of decimal places, causing them to miss the curve by microns.
     */
    private Edge buildEdgeWithProjection(Vertex start, Vertex end, Curve3 curve, boolean sameSense) {
        try {
            return new Edge(start, end, curve, sameSense);
        } catch (TopologyException e) {
            // Project off-curve vertices onto the curve using closest-point projection
            CartesianPoint startPoint = start.point();
            CartesianPoint endPoint = end.point();
            CartesianPoint projectedStart = projectOntoCurve(startPoint, curve);
            CartesianPoint projectedEnd = projectOntoCurve(endPoint, curve);
            // Use projected vertices - if they're within tolerance, the edge will succeed
            Vertex vStart = (projectedStart.distanceTo(startPoint) > VERTEX_PROJECTION_TOLERANCE) ? start : new Vertex(projectedStart);
            Vertex vEnd = (projectedEnd.distanceTo(endPoint) > VERTEX_PROJECTION_TOLERANCE) ? end : new Vertex(projectedEnd);
            return new Edge(vStart, vEnd, curve, sameSense);
        }
    }

    /**
     * Projects a point onto a curve using closest-point projection.
     * Handles all supported curve types.
     */
    private static CartesianPoint projectOntoCurve(CartesianPoint point, Curve3 curve) {
        if (curve instanceof com.minicad.geometry.BSplineCurve3) {
            com.minicad.geometry.BSplineCurve3 bspline = (com.minicad.geometry.BSplineCurve3) curve;
            return bspline.closestPointTo(point);
        }
        if (curve instanceof com.minicad.geometry.RationalBSplineCurve3) {
            com.minicad.geometry.RationalBSplineCurve3 rational = (com.minicad.geometry.RationalBSplineCurve3) curve;
            return rational.closestPointTo(point);
        }
        if (curve instanceof com.minicad.geometry.Line3) {
            com.minicad.geometry.Line3 line = (com.minicad.geometry.Line3) curve;
            // Project onto infinite line: t is signed distance along direction
            Vector3 offset = point.subtract(line.getOrigin());
            double t = offset.dot(line.getDirection().asVector());
            return line.getOrigin().add(line.getDirection().asVector().scale(t));
        }
        if (curve instanceof com.minicad.geometry.Circle) {
            com.minicad.geometry.Circle circle = (com.minicad.geometry.Circle) curve;
            // Project onto circle: normalize vector from center, scale by radius
            CartesianPoint center = circle.getPosition().getLocation();
            Vector3 fromCenter = point.subtract(center);
            if (fromCenter.normSquared() <= Epsilon.EPS) {
                // Point is at center - pick arbitrary point on circle
                Vector3 xDir = circle.getPosition().xDirection().asVector();
                return center.add(xDir.scale(circle.getRadius()));
            }
            return center.add(fromCenter.normalize().asVector().scale(circle.getRadius()));
        }
        if (curve instanceof com.minicad.geometry.Ellipse3) {
            com.minicad.geometry.Ellipse3 ellipse = (com.minicad.geometry.Ellipse3) curve;
            // Approximate by sampling - good enough for projection
            return ellipse.closestPointTo(point);
        }
        if (curve instanceof com.minicad.geometry.Polyline3) {
            com.minicad.geometry.Polyline3 polyline = (com.minicad.geometry.Polyline3) curve;
            // Find closest point on polyline segments
            return polylineClosestPoint(point, polyline);
        }
        if (curve instanceof com.minicad.geometry.TrimmedCurve3) {
            com.minicad.geometry.TrimmedCurve3 trimmed = (com.minicad.geometry.TrimmedCurve3) curve;
            return projectOntoCurve(point, trimmed.getBasisCurve());
        }
        if (curve instanceof com.minicad.geometry.SurfaceCurve3) {
            com.minicad.geometry.SurfaceCurve3 sc = (com.minicad.geometry.SurfaceCurve3) curve;
            return projectOntoCurve(point, sc.getCurve3d());
        }
        if (curve instanceof com.minicad.geometry.CompositeCurve3) {
            com.minicad.geometry.CompositeCurve3 composite = (com.minicad.geometry.CompositeCurve3) curve;
            // Find closest point across all segments
            CartesianPoint closest = null;
            double minDist = Double.POSITIVE_INFINITY;
            for (Curve3 segment : composite.getSegments()) {
                CartesianPoint candidate = projectOntoCurve(point, segment);
                double dist = point.distanceTo(candidate);
                if (dist < minDist) {
                    minDist = dist;
                    closest = candidate;
                }
            }
            return closest;
        }
        // Fallback: return original point (Edge constructor will validate)
        return point;
    }

    private static CartesianPoint polylineClosestPoint(CartesianPoint point, com.minicad.geometry.Polyline3 polyline) {
        List<CartesianPoint> points = polyline.getPoints();
        CartesianPoint closest = points.get(0);
        double minDist = Double.POSITIVE_INFINITY;
        for (int i = 0; i < points.size() - 1; i++) {
            CartesianPoint p = closestPointOnSegment(point, points.get(i), points.get(i + 1));
            double dist = point.distanceTo(p);
            if (dist < minDist) {
                minDist = dist;
                closest = p;
            }
        }
        return closest;
    }

    private static CartesianPoint closestPointOnSegment(CartesianPoint point, CartesianPoint a, CartesianPoint b) {
        Vector3 ab = b.subtract(a);
        double lenSq = ab.normSquared();
        if (lenSq <= Epsilon.EPS) return a;
        double t = Math.max(0, Math.min(1, point.subtract(a).dot(ab) / lenSq));
        return new CartesianPoint(a.getX() + ab.getX() * t, a.getY() + ab.getY() * t, a.getZ() + ab.getZ() * t);
    }

    // ========================================================================
    // Oriented edge building
    // ========================================================================

    /**
     * Builds an oriented edge.
     *
     * @param id STEP entity id
     * @return built oriented edge
     */
    OrientedEdge buildOrientedEdge(int id) {
        OrientedEdge existing = orientedEdges.get(id);
        if (existing != null) {
            return existing;
        }
        StepOrientedEdge stepOrientedEdge = requireEntity(id, StepOrientedEdge.class, "ORIENTED_EDGE");
        OrientedEdge built = new OrientedEdge(buildEdge(stepOrientedEdge.edgeElement().id()), stepOrientedEdge.orientation());
        orientedEdges.put(id, built);
        return built;
    }

    // ========================================================================
    // Loop building
    // ========================================================================

    /**
     * Builds an edge loop.
     *
     * @param id STEP entity id
     * @return built edge loop
     */
    EdgeLoop buildEdgeLoop(int id) {
        EdgeLoop existing = loops.get(id);
        if (existing != null) {
            return existing;
        }
        StepEdgeLoop loop = requireEntity(id, StepEdgeLoop.class, "EDGE_LOOP");
        EdgeLoop built = new EdgeLoop(loop.edges().stream().map(edge -> buildOrientedEdge(edge.id())).collect(Collectors.toList()));
        loops.put(id, built);
        return built;
    }

    /**
     * Builds a vertex loop.
     *
     * @param id STEP entity id
     * @return built vertex loop
     */
    VertexLoop buildVertexLoop(int id) {
        VertexLoop existing = vertexLoops.get(id);
        if (existing != null) {
            return existing;
        }
        StepVertexLoop loop = requireEntity(id, StepVertexLoop.class, "VERTEX_LOOP");
        VertexLoop built = new VertexLoop(buildVertex(loop.loopVertex().id()));
        vertexLoops.put(id, built);
        return built;
    }

    /**
     * Builds a poly loop.
     *
     * @param id STEP entity id
     * @return built poly loop
     */
    com.minicad.topology.PolyLoop buildPolyLoop(int id) {
        com.minicad.topology.PolyLoop existing = polyLoops.get(id);
        if (existing != null) {
            return existing;
        }
        StepPolyLoop loop = requireEntity(id, StepPolyLoop.class, "POLY_LOOP");
        com.minicad.topology.PolyLoop built = new com.minicad.topology.PolyLoop(
                loop.polygon().stream().map(pt -> pointBuilder.apply(pt.id())).collect(Collectors.toList()));
        polyLoops.put(id, built);
        return built;
    }

    /**
     * Builds a path into a composite curve.
     *
     * @param id STEP entity id
     * @return built composite curve representing the path geometry
     */
    CompositeCurve3 buildPath(int id) {
        CompositeCurve3 existing = paths.get(id);
        if (existing != null) {
            return existing;
        }
        StepEntity entity = requireExistingEntity(id);
        List<StepOrientedEdge> edges;
        boolean needsEdgeOrientationFlip = false;
        if (entity instanceof StepPath) {
            StepPath path = (StepPath) entity;
            edges = path.edges();
        } else if (entity instanceof StepOpenPath) {
            StepOpenPath openPath = (StepOpenPath) entity;
            edges = openPath.edges();
        } else if (entity instanceof StepSubpath) {
            StepSubpath subpath = (StepSubpath) entity;
            edges = subpath.edges();
        } else if (entity instanceof StepOrientedPath) {
            StepOrientedPath orientedPath = (StepOrientedPath) entity;
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
                    .map(oe -> buildOrientedEdge(oe.id()))
                    .map(oe -> new OrientedEdge(oe.edge(), !oe.orientation()))
                    .map(OrientedEdge::edge)
                    .map(Edge::curve)
                    .collect(Collectors.toList());
        } else {
            curves = edges.stream()
                    .map(oe -> buildOrientedEdge(oe.id()))
                    .map(OrientedEdge::edge)
                    .map(Edge::curve)
                    .collect(Collectors.toList());
        }
        CompositeCurve3 built = new CompositeCurve3(curves);
        paths.put(id, built);
        return built;
    }

    // ========================================================================
    // Face bound building
    // ========================================================================

    /**
     * Builds a face bound.
     *
     * @param id STEP entity id
     * @return built face bound
     */
    FaceBound buildFaceBound(int id) {
        FaceBound existing = faceBounds.get(id);
        if (existing != null) {
            return existing;
        }
        StepFaceBound stepFaceBound = requireEntity(id, StepFaceBound.class, "FACE_BOUND");
        Loop builtLoop;
        if (stepFaceBound.loop() instanceof StepEdgeLoop) {
            StepEdgeLoop edgeLoop = (StepEdgeLoop) stepFaceBound.loop();
            builtLoop = buildEdgeLoop(edgeLoop.id());
        } else if (stepFaceBound.loop() instanceof StepVertexLoop) {
            StepVertexLoop vertexLoop = (StepVertexLoop) stepFaceBound.loop();
            builtLoop = buildVertexLoop(vertexLoop.id());
        } else if (stepFaceBound.loop() instanceof StepPolyLoop) {
            StepPolyLoop polyLoop = (StepPolyLoop) stepFaceBound.loop();
            builtLoop = buildPolyLoop(polyLoop.id());
        } else {
            throw new UnsupportedGeometryException(
                    "FACE_BOUND construction requires EDGE_LOOP, VERTEX_LOOP, or POLY_LOOP");
        }
        FaceBound built = stepFaceBound.outer()
                ? FaceBound.outer(builtLoop, stepFaceBound.orientation())
                : FaceBound.inner(builtLoop, stepFaceBound.orientation());
        faceBounds.put(id, built);
        return built;
    }

    // ========================================================================
    // Face building
    // ========================================================================

    /**
     * Builds a planar face.
     *
     * @param id STEP entity id
     * @return built face
     */
    Face buildFace(int id) {
        Face existing = faces.get(id);
        if (existing != null) {
            return existing;
        }
        StepEntity entity = requireExistingEntity(id);
        if (entity instanceof StepOrientedFace) {
            StepOrientedFace orientedFace = (StepOrientedFace) entity;
            Face baseFace = buildFace(orientedFace.faceElement().id());
            Face built = new Face(
                    baseFace.surface(),
                    baseFace.bounds(),
                    orientedFace.orientation() ? baseFace.sameSense() : !baseFace.sameSense()
            );
            faces.put(id, built);
            return built;
        }
        if (entity instanceof StepAdvancedFace) {
            StepAdvancedFace advancedFace = (StepAdvancedFace) entity;
            Face built = buildFaceSurface(advancedFace, "ADVANCED_FACE");
            faces.put(id, built);
            return built;
        }
        if (entity instanceof StepFaceSurface) {
            StepFaceSurface faceSurface = (StepFaceSurface) entity;
            Face built = buildFaceSurface(faceSurface, "FACE_SURFACE");
            faces.put(id, built);
            return built;
        }
        if (entity instanceof StepSubface) {
            StepSubface subface = (StepSubface) entity;
            Face built = buildFace(subface.faceElement().id());
            faces.put(id, built);
            return built;
        }
        if (entity instanceof StepOrientedSubface) {
            StepOrientedSubface orientedSubface = (StepOrientedSubface) entity;
            Face baseFace = buildFace(orientedSubface.faceElement().id());
            Face built = new Face(
                    baseFace.surface(),
                    baseFace.bounds(),
                    orientedSubface.orientation() ? baseFace.sameSense() : !baseFace.sameSense()
            );
            faces.put(id, built);
            return built;
        }
        if (entity instanceof StepMachinedSurface) {
            StepMachinedSurface machinedSurface = (StepMachinedSurface) entity;
            Face built = buildFace(machinedSurface.face().id());
            faces.put(id, built);
            return built;
        }
        if (entity instanceof StepFace) {
            StepFace face = (StepFace) entity;
            StepEntity actual = builder.resolvedEntity(face.id());
            if (actual != null && actual != face) {
                if (actual instanceof StepOrientedFace || actual instanceof StepAdvancedFace
                        || actual instanceof StepFaceSurface || actual instanceof StepSubface
                        || actual instanceof StepOrientedSubface || actual instanceof StepMachinedSurface) {
                    return buildFace(actual.id());
                }
                throw new StepResolutionException("entity #" + id + " is an abstract FACE with unsupported subtype " + StepCadBuilder.stepEntityTypeName(actual));
            }
            throw new StepResolutionException("entity #" + id + " is an abstract FACE with no concrete subtype");
        }
        if (entity instanceof StepMappedItem) {
            StepMappedItem mappedItem = (StepMappedItem) entity;
            Face built = buildFace(mappedItem.mappingTarget().id());
            faces.put(id, built);
            return built;
        }
        throw new StepResolutionException("entity #" + id + " is not a FACE");
    }

    private Face buildFaceSurface(StepFaceEntity stepFace, String faceType) {
        StepEntity geometry = StepCadBuilder.faceGeometry(stepFace);
        SurfaceGeometry supportedSurface = surfaceGeometryBuilder.apply(geometry.id());
        if (supportedSurface == null) {
            String unsupportedSurfaceType = builder.describeUnsupportedFaceGeometry(geometry);
            if (unsupportedSurfaceType != null) {
                throw new UnsupportedGeometryException(faceType + " construction for " + unsupportedSurfaceType + " is unsupported");
            }
            throw new UnsupportedGeometryException(faceType + " construction requires PLANE geometry");
        }
        List<FaceBound> bounds = stepFace.bounds().stream()
                .map(bound -> buildFaceBound(bound.id()))
                .collect(Collectors.toList());
        if (bounds.stream().noneMatch(FaceBound::outer)) {
            bounds = inferOuterBounds(bounds);
        }
        return new Face(supportedSurface, bounds, StepCadBuilder.faceSameSense(stepFace));
    }

    private static List<FaceBound> inferOuterBounds(List<FaceBound> bounds) {
        if (bounds.isEmpty()) {
            return bounds;
        }
        List<FaceBound> result = new ArrayList<>();
        for (int i = 0; i < bounds.size(); i++) {
            FaceBound bound = bounds.get(i);
            if (i == 0) {
                result.add(FaceBound.outer(bound.loop(), bound.orientation()));
            } else {
                result.add(FaceBound.inner(bound.loop(), bound.orientation()));
            }
        }
        return List.copyOf(result);
    }

    // ========================================================================
    // Helper methods
    // ========================================================================

    private StepEntity requireExistingEntity(int id) {
        return builder.requireExistingEntity(id);
    }

    private <T extends StepEntity> T requireEntity(int id, Class<T> type, String entityName) {
        return builder.requireEntity(id, type, entityName);
    }
}