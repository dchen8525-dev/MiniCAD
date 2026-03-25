package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Circle;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.Plane;
import com.minicad.geometry.Vector3;
import com.minicad.step.model.StepAdvancedFace;
import com.minicad.step.model.StepAxis2Placement3D;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepCircle;
import com.minicad.step.model.StepClosedShell;
import com.minicad.step.model.StepDirection;
import com.minicad.step.model.StepEdgeCurve;
import com.minicad.step.model.StepEdgeLoop;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepFaceBound;
import com.minicad.step.model.StepLine;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepOpenShell;
import com.minicad.step.model.StepOrientedEdge;
import com.minicad.step.model.StepPlane;
import com.minicad.step.model.StepVertexPoint;
import com.minicad.topology.Edge;
import com.minicad.topology.EdgeLoop;
import com.minicad.topology.Face;
import com.minicad.topology.FaceBound;
import com.minicad.topology.OrientedEdge;
import com.minicad.topology.Shell;
import com.minicad.topology.Solid;
import com.minicad.topology.Vertex;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds internal geometry and topology objects from resolved STEP semantic entities.
 */
public final class StepCadBuilder {

    private final Map<Integer, StepEntity> entitiesById;
    private final Map<Integer, CartesianPoint> points = new LinkedHashMap<>();
    private final Map<Integer, Direction3> directions = new LinkedHashMap<>();
    private final Map<Integer, Vector3> vectors = new LinkedHashMap<>();
    private final Map<Integer, Axis2Placement3D> placements = new LinkedHashMap<>();
    private final Map<Integer, Line3> lines = new LinkedHashMap<>();
    private final Map<Integer, Plane> planes = new LinkedHashMap<>();
    private final Map<Integer, Circle> circles = new LinkedHashMap<>();
    private final Map<Integer, Vertex> vertices = new LinkedHashMap<>();
    private final Map<Integer, Edge> edges = new LinkedHashMap<>();
    private final Map<Integer, OrientedEdge> orientedEdges = new LinkedHashMap<>();
    private final Map<Integer, EdgeLoop> loops = new LinkedHashMap<>();
    private final Map<Integer, FaceBound> faceBounds = new LinkedHashMap<>();
    private final Map<Integer, Face> faces = new LinkedHashMap<>();
    private final Map<Integer, Shell> shells = new LinkedHashMap<>();
    private final Map<Integer, Solid> solids = new LinkedHashMap<>();

    private StepCadBuilder(Map<Integer, StepEntity> entitiesById) {
        this.entitiesById = Map.copyOf(entitiesById);
    }

    /**
     * Creates a builder from resolved STEP semantic entities.
     *
     * @param entitiesById resolved entities indexed by STEP id
     * @return builder instance
     */
    public static StepCadBuilder fromResolved(Map<Integer, StepEntity> entitiesById) {
        return new StepCadBuilder(entitiesById);
    }

    /**
     * Builds a Cartesian point.
     *
     * @param id STEP entity id
     * @return built point
     */
    public CartesianPoint buildPoint(int id) {
        CartesianPoint existing = points.get(id);
        if (existing != null) {
            return existing;
        }
        StepCartesianPoint point = requireEntity(id, StepCartesianPoint.class, "CARTESIAN_POINT");
        CartesianPoint built = new CartesianPoint(
                point.coordinates().get(0),
                point.coordinates().get(1),
                point.coordinates().get(2)
        );
        points.put(id, built);
        return built;
    }

    /**
     * Builds a direction.
     *
     * @param id STEP entity id
     * @return built direction
     */
    public Direction3 buildDirection(int id) {
        Direction3 existing = directions.get(id);
        if (existing != null) {
            return existing;
        }
        StepDirection direction = requireEntity(id, StepDirection.class, "DIRECTION");
        Direction3 built = Direction3.from(new Vector3(
                direction.directionRatios().get(0),
                direction.directionRatios().get(1),
                direction.directionRatios().get(2)
        ));
        directions.put(id, built);
        return built;
    }

    /**
     * Builds a vector.
     *
     * @param id STEP entity id
     * @return built vector
     */
    public Vector3 buildVector(int id) {
        Vector3 existing = vectors.get(id);
        if (existing != null) {
            return existing;
        }
        com.minicad.step.model.StepVector vector = requireEntity(id, com.minicad.step.model.StepVector.class, "VECTOR");
        Vector3 built = buildDirection(vector.orientation().id()).asVector().scale(vector.magnitude());
        vectors.put(id, built);
        return built;
    }

    /**
     * Builds a placement.
     *
     * @param id STEP entity id
     * @return built placement
     */
    public Axis2Placement3D buildPlacement(int id) {
        Axis2Placement3D existing = placements.get(id);
        if (existing != null) {
            return existing;
        }
        StepAxis2Placement3D placement = requireEntity(id, StepAxis2Placement3D.class, "AXIS2_PLACEMENT_3D");
        Axis2Placement3D built = new Axis2Placement3D(
                buildPoint(placement.location().id()),
                buildDirection(placement.axis().id()),
                buildDirection(placement.refDirection().id())
        );
        placements.put(id, built);
        return built;
    }

    /**
     * Builds a line.
     *
     * @param id STEP entity id
     * @return built line
     */
    public Line3 buildLine(int id) {
        Line3 existing = lines.get(id);
        if (existing != null) {
            return existing;
        }
        StepLine line = requireEntity(id, StepLine.class, "LINE");
        Line3 built = new Line3(
                buildPoint(line.point().id()),
                buildDirection(line.vector().orientation().id())
        );
        lines.put(id, built);
        return built;
    }

    /**
     * Builds a plane.
     *
     * @param id STEP entity id
     * @return built plane
     */
    public Plane buildPlane(int id) {
        Plane existing = planes.get(id);
        if (existing != null) {
            return existing;
        }
        StepPlane plane = requireEntity(id, StepPlane.class, "PLANE");
        Axis2Placement3D placement = buildPlacement(plane.position().id());
        Plane built = new Plane(placement.location(), placement.axis());
        planes.put(id, built);
        return built;
    }

    /**
     * Builds a circle geometry object.
     *
     * @param id STEP entity id
     * @return built circle
     */
    public Circle buildCircle(int id) {
        Circle existing = circles.get(id);
        if (existing != null) {
            return existing;
        }
        StepCircle circle = requireEntity(id, StepCircle.class, "CIRCLE");
        Circle built = new Circle(buildPlacement(circle.position().id()), circle.radius());
        circles.put(id, built);
        return built;
    }

    /**
     * Builds a topological vertex.
     *
     * @param id STEP entity id
     * @return built vertex
     */
    public Vertex buildVertex(int id) {
        Vertex existing = vertices.get(id);
        if (existing != null) {
            return existing;
        }
        StepVertexPoint vertexPoint = requireEntity(id, StepVertexPoint.class, "VERTEX_POINT");
        Vertex built = new Vertex(buildPoint(vertexPoint.point().id()));
        vertices.put(id, built);
        return built;
    }

    /**
     * Builds a straight topological edge.
     *
     * @param id STEP entity id
     * @return built edge
     */
    public Edge buildEdge(int id) {
        Edge existing = edges.get(id);
        if (existing != null) {
            return existing;
        }
        StepEdgeCurve edgeCurve = requireEntity(id, StepEdgeCurve.class, "EDGE_CURVE");
        if (!(edgeCurve.edgeGeometry() instanceof StepLine line)) {
            if (edgeCurve.edgeGeometry() instanceof StepCircle) {
                throw new UnsupportedGeometryException("circular EDGE_CURVE topology is unsupported");
            }
            throw new UnsupportedGeometryException("EDGE_CURVE topology requires LINE geometry");
        }
        Edge built = new Edge(
                buildVertex(edgeCurve.start().id()),
                buildVertex(edgeCurve.end().id()),
                buildLine(line.id()),
                edgeCurve.sameSense()
        );
        edges.put(id, built);
        return built;
    }

    /**
     * Builds an oriented edge.
     *
     * @param id STEP entity id
     * @return built oriented edge
     */
    public OrientedEdge buildOrientedEdge(int id) {
        OrientedEdge existing = orientedEdges.get(id);
        if (existing != null) {
            return existing;
        }
        StepOrientedEdge stepOrientedEdge = requireEntity(id, StepOrientedEdge.class, "ORIENTED_EDGE");
        OrientedEdge built = new OrientedEdge(buildEdge(stepOrientedEdge.edgeElement().id()), stepOrientedEdge.orientation());
        orientedEdges.put(id, built);
        return built;
    }

    /**
     * Builds an edge loop.
     *
     * @param id STEP entity id
     * @return built edge loop
     */
    public EdgeLoop buildEdgeLoop(int id) {
        EdgeLoop existing = loops.get(id);
        if (existing != null) {
            return existing;
        }
        StepEdgeLoop loop = requireEntity(id, StepEdgeLoop.class, "EDGE_LOOP");
        EdgeLoop built = new EdgeLoop(loop.edges().stream().map(edge -> buildOrientedEdge(edge.id())).toList());
        loops.put(id, built);
        return built;
    }

    /**
     * Builds a face bound.
     *
     * @param id STEP entity id
     * @return built face bound
     */
    public FaceBound buildFaceBound(int id) {
        FaceBound existing = faceBounds.get(id);
        if (existing != null) {
            return existing;
        }
        StepFaceBound stepFaceBound = requireEntity(id, StepFaceBound.class, "FACE_BOUND");
        FaceBound built = stepFaceBound.outer()
                ? FaceBound.outer(buildEdgeLoop(stepFaceBound.loop().id()), stepFaceBound.orientation())
                : FaceBound.inner(buildEdgeLoop(stepFaceBound.loop().id()), stepFaceBound.orientation());
        faceBounds.put(id, built);
        return built;
    }

    /**
     * Builds a planar face.
     *
     * @param id STEP entity id
     * @return built face
     */
    public Face buildFace(int id) {
        Face existing = faces.get(id);
        if (existing != null) {
            return existing;
        }
        StepAdvancedFace stepFace = requireEntity(id, StepAdvancedFace.class, "ADVANCED_FACE");
        if (!(stepFace.faceGeometry() instanceof StepPlane plane)) {
            throw new UnsupportedGeometryException("ADVANCED_FACE construction requires PLANE geometry");
        }
        List<FaceBound> bounds = stepFace.bounds().stream().map(bound -> buildFaceBound(bound.id())).toList();
        Face built = new Face(buildPlane(plane.id()), bounds, stepFace.sameSense());
        faces.put(id, built);
        return built;
    }

    /**
     * Builds a shell from OPEN_SHELL or CLOSED_SHELL.
     *
     * @param id STEP entity id
     * @return built shell
     */
    public Shell buildShell(int id) {
        Shell existing = shells.get(id);
        if (existing != null) {
            return existing;
        }
        StepEntity entity = requireExistingEntity(id);
        Shell built;
        if (entity instanceof StepOpenShell openShell) {
            built = new Shell(openShell.faces().stream().map(face -> buildFace(face.id())).toList(), false);
        } else if (entity instanceof StepClosedShell closedShell) {
            built = new Shell(closedShell.faces().stream().map(face -> buildFace(face.id())).toList(), true);
        } else {
            throw new StepResolutionException("entity #" + id + " is not an OPEN_SHELL or CLOSED_SHELL");
        }
        shells.put(id, built);
        return built;
    }

    /**
     * Builds a solid from MANIFOLD_SOLID_BREP.
     *
     * @param id STEP entity id
     * @return built solid
     */
    public Solid buildSolid(int id) {
        Solid existing = solids.get(id);
        if (existing != null) {
            return existing;
        }
        StepManifoldSolidBrep solidBrep = requireEntity(id, StepManifoldSolidBrep.class, "MANIFOLD_SOLID_BREP");
        Solid built = new Solid(buildShell(solidBrep.outer().id()));
        solids.put(id, built);
        return built;
    }

    private StepEntity requireExistingEntity(int id) {
        StepEntity entity = entitiesById.get(id);
        if (entity == null) {
            throw new StepResolutionException("missing resolved entity #" + id);
        }
        return entity;
    }

    private <T extends StepEntity> T requireEntity(int id, Class<T> type, String expectedName) {
        StepEntity entity = requireExistingEntity(id);
        if (!type.isInstance(entity)) {
            throw new StepResolutionException("entity #" + id + " is not a " + expectedName);
        }
        return type.cast(entity);
    }
}
