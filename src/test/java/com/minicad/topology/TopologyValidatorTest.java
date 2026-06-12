package com.minicad.topology;

import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.Plane;
import com.minicad.geometry.Vector3;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TopologyValidatorTest {

    @Test
    void shouldReportBoundaryEdgesOnClosedShell() {
        Shell shell = new Shell(List.of(squareFace()), true);

        TopologyValidator.ValidationResult result = TopologyValidator.validateShell(shell);

        assertFalse(result.ok());
        assertEquals(4, result.errorCount());
        assertTrue(result.hasCode("closed_shell.edge_use_count"));
    }

    @Test
    void shouldAllowBoundaryEdgesOnOpenShell() {
        Shell shell = new Shell(List.of(squareFace()), false);

        TopologyValidator.ValidationResult result = TopologyValidator.validateShell(shell);

        assertTrue(result.ok(), () -> result.issues().toString());
    }

    @Test
    void shouldReportNonManifoldEdge() {
        Vertex v0 = vertex(0.0, 0.0, 0.0);
        Vertex v1 = vertex(1.0, 0.0, 0.0);
        Edge shared = edge(v0, v1);

        Shell shell = new Shell(List.of(
                triangleFace(shared, true, vertex(0.5, 1.0, 0.0)),
                triangleFace(shared, false, vertex(0.5, -1.0, 0.0)),
                triangleFace(shared, true, vertex(0.5, 0.5, 0.0))
        ), true);

        TopologyValidator.ValidationResult result = TopologyValidator.validateShell(shell);

        assertFalse(result.ok());
        assertTrue(result.hasCode("shell.non_manifold_edge"));
        assertTrue(result.hasCode("closed_shell.edge_use_count"));
    }

    @Test
    void shouldReportSameDirectionUseOnClosedShell() {
        Vertex v0 = vertex(0.0, 0.0, 0.0);
        Vertex v1 = vertex(1.0, 0.0, 0.0);
        Edge shared = edge(v0, v1);

        Shell shell = new Shell(List.of(
                triangleFace(shared, true, vertex(0.5, 1.0, 0.0)),
                triangleFace(shared, true, vertex(0.5, -1.0, 0.0))
        ), true);

        TopologyValidator.ValidationResult result = TopologyValidator.validateShell(shell);

        assertFalse(result.ok());
        assertTrue(result.hasCode("closed_shell.edge_orientation"));
    }

    @Test
    void shouldReportZeroAreaPlanarFace() {
        Vertex v0 = vertex(0.0, 0.0, 0.0);
        Vertex v1 = vertex(1.0, 0.0, 0.0);
        Vertex v2 = vertex(2.0, 0.0, 0.0);
        EdgeLoop loop = new EdgeLoop(List.of(
                new OrientedEdge(edge(v0, v1), true),
                new OrientedEdge(edge(v1, v2), true),
                new OrientedEdge(edge(v2, v0), true)
        ));
        Shell shell = new Shell(List.of(face(loop)), false);

        TopologyValidator.ValidationResult result = TopologyValidator.validateShell(shell);

        assertFalse(result.ok());
        assertTrue(result.hasCode("face.zero_area"));
    }

    @Test
    void shouldAllowOppositeOrientedVoidShellInsideOuterShell() {
        Solid solid = new Solid(
                tetraShell(0.0, 0.0, 0.0, 4.0, false),
                List.of(tetraShell(0.5, 0.5, 0.5, 1.0, true))
        );

        TopologyValidator.ValidationResult result = TopologyValidator.validateSolid(solid);

        assertTrue(result.ok(), () -> result.issues().toString());
    }

    @Test
    void shouldReportVoidShellWithSameOrientationAsOuterShell() {
        Solid solid = new Solid(
                tetraShell(0.0, 0.0, 0.0, 4.0, false),
                List.of(tetraShell(0.5, 0.5, 0.5, 1.0, false))
        );

        TopologyValidator.ValidationResult result = TopologyValidator.validateSolid(solid);

        assertFalse(result.ok());
        assertTrue(result.hasCode("solid.void_orientation"));
    }

    @Test
    void shouldReportVoidShellOutsideOuterShellBoundingBox() {
        Solid solid = new Solid(
                tetraShell(0.0, 0.0, 0.0, 2.0, false),
                List.of(tetraShell(3.0, 3.0, 3.0, 1.0, true))
        );

        TopologyValidator.ValidationResult result = TopologyValidator.validateSolid(solid);

        assertFalse(result.ok());
        assertTrue(result.hasCode("solid.void_outside_outer"));
    }

    private static Face squareFace() {
        Vertex v0 = vertex(0.0, 0.0, 0.0);
        Vertex v1 = vertex(1.0, 0.0, 0.0);
        Vertex v2 = vertex(1.0, 1.0, 0.0);
        Vertex v3 = vertex(0.0, 1.0, 0.0);
        EdgeLoop loop = new EdgeLoop(List.of(
                new OrientedEdge(edge(v0, v1), true),
                new OrientedEdge(edge(v1, v2), true),
                new OrientedEdge(edge(v2, v3), true),
                new OrientedEdge(edge(v3, v0), true)
        ));
        return face(loop);
    }

    private static Face triangleFace(Edge sharedEdge, boolean sharedOrientation, Vertex tip) {
        OrientedEdge sharedUse = new OrientedEdge(sharedEdge, sharedOrientation);
        Vertex loopStart = sharedUse.endVertex();
        Vertex loopEnd = sharedUse.startVertex();
        EdgeLoop loop = new EdgeLoop(List.of(
                sharedUse,
                new OrientedEdge(edge(loopStart, tip), true),
                new OrientedEdge(edge(tip, loopEnd), true)
        ));
        return face(loop);
    }

    private static Face face(EdgeLoop loop) {
        return new Face(
                new Plane(new CartesianPoint(0.0, 0.0, 0.0), Direction3.zAxis()),
                List.of(FaceBound.outer(loop, true)),
                true
        );
    }

    private static Edge edge(Vertex start, Vertex end) {
        return new Edge(start, end, new Line3(start.point(), Direction3.from(end.point().subtract(start.point()))), true);
    }

    private static Vertex vertex(double x, double y, double z) {
        return new Vertex(new CartesianPoint(x, y, z));
    }

    private static Shell tetraShell(double x, double y, double z, double size, boolean reverse) {
        Vertex a = vertex(x, y, z);
        Vertex b = vertex(x + size, y, z);
        Vertex c = vertex(x, y + size, z);
        Vertex d = vertex(x, y, z + size);
        Map<String, Edge> edges = new HashMap<>();
        List<Face> faces = List.of(
                triangle(a, c, b, reverse, edges),
                triangle(a, b, d, reverse, edges),
                triangle(a, d, c, reverse, edges),
                triangle(b, c, d, reverse, edges)
        );
        return new Shell(faces, true);
    }

    private static Face triangle(Vertex a, Vertex b, Vertex c, boolean reverse, Map<String, Edge> edges) {
        List<Vertex> vertices = reverse ? List.of(a, c, b) : List.of(a, b, c);
        EdgeLoop loop = new EdgeLoop(List.of(
                orientedEdge(vertices.get(0), vertices.get(1), edges),
                orientedEdge(vertices.get(1), vertices.get(2), edges),
                orientedEdge(vertices.get(2), vertices.get(0), edges)
        ));
        CartesianPoint p0 = vertices.get(0).point();
        Vector3 normal = vertices.get(1).point().subtract(p0).cross(vertices.get(2).point().subtract(p0));
        return new Face(new Plane(p0, Direction3.from(normal)), List.of(FaceBound.outer(loop, true)), true);
    }

    private static OrientedEdge orientedEdge(Vertex start, Vertex end, Map<String, Edge> edges) {
        String forward = edgeKey(start, end);
        String reverse = edgeKey(end, start);
        Edge existing = edges.get(forward);
        if (existing != null) {
            return new OrientedEdge(existing, true);
        }
        existing = edges.get(reverse);
        if (existing != null) {
            return new OrientedEdge(existing, false);
        }
        Edge edge = edge(start, end);
        edges.put(forward, edge);
        return new OrientedEdge(edge, true);
    }

    private static String edgeKey(Vertex start, Vertex end) {
        return vertexKey(start) + "->" + vertexKey(end);
    }

    private static String vertexKey(Vertex vertex) {
        CartesianPoint point = vertex.point();
        return point.x() + "," + point.y() + "," + point.z();
    }
}
