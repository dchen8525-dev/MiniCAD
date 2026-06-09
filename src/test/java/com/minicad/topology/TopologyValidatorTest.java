package com.minicad.topology;

import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.Plane;
import com.minicad.geometry.Vector3;
import org.junit.jupiter.api.Test;

import java.util.List;

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

        assertTrue(result.ok());
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
}
