package com.minicad.topology;

import com.minicad.common.Epsilon;
import com.minicad.common.TopologyException;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Circle;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.Vector3;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EdgeLoopTest {

    @Test
    void shouldBuildClosedLoopFromConnectedEdges() {
        Vertex v0 = new Vertex(new CartesianPoint(0.0, 0.0, 0.0));
        Vertex v1 = new Vertex(new CartesianPoint(1.0, 0.0, 0.0));
        Vertex v2 = new Vertex(new CartesianPoint(1.0, 1.0, 0.0));
        Vertex v3 = new Vertex(new CartesianPoint(0.0, 1.0, 0.0));

        Edge e0 = new Edge(v0, v1, line(v0, v1), true);
        Edge e1 = new Edge(v1, v2, line(v1, v2), true);
        Edge e2 = new Edge(v2, v3, line(v2, v3), true);
        Edge e3 = new Edge(v3, v0, line(v3, v0), true);

        EdgeLoop loop = new EdgeLoop(List.of(
                new OrientedEdge(e0, true),
                new OrientedEdge(e1, true),
                new OrientedEdge(e2, true),
                new OrientedEdge(e3, true)
        ));

        assertEquals(4, loop.getEdges().size());
    }

    @Test
    void shouldRejectDisconnectedLoop() {
        Vertex v0 = new Vertex(new CartesianPoint(0.0, 0.0, 0.0));
        Vertex v1 = new Vertex(new CartesianPoint(1.0, 0.0, 0.0));
        Vertex v2 = new Vertex(new CartesianPoint(1.0, 1.0, 0.0));
        Vertex v3 = new Vertex(new CartesianPoint(0.0, 1.0, 0.0));

        Edge e0 = new Edge(v0, v1, line(v0, v1), true);
        Edge e1 = new Edge(v2, v3, line(v2, v3), true);

        TopologyException exception = assertThrows(
                TopologyException.class,
                () -> new EdgeLoop(List.of(new OrientedEdge(e0, true), new OrientedEdge(e1, true)))
        );

        assertEquals("edge loop must be connected and closed between edge 0 and edge 1; gap 1.0 exceeds tolerance "
                + Epsilon.IMPORT_TOPOLOGY_TOLERANCE, exception.getMessage());
    }

    @Test
    void shouldAllowLoopConnectivityWithinImportTopologyTolerance() {
        Vertex v0 = new Vertex(new CartesianPoint(0.0, 0.0, 0.0));
        Vertex v1 = new Vertex(new CartesianPoint(1.0, 0.0, 0.0));
        Vertex v1Projected = new Vertex(new CartesianPoint(1.0, Epsilon.IMPORT_TOPOLOGY_TOLERANCE * 0.5, 0.0));
        Vertex v2 = new Vertex(new CartesianPoint(1.0, 1.0, 0.0));
        Vertex v3 = new Vertex(new CartesianPoint(0.0, 1.0, 0.0));

        EdgeLoop loop = new EdgeLoop(List.of(
                new OrientedEdge(new Edge(v0, v1, line(v0, v1), true), true),
                new OrientedEdge(new Edge(v1Projected, v2, line(v1Projected, v2), true), true),
                new OrientedEdge(new Edge(v2, v3, line(v2, v3), true), true),
                new OrientedEdge(new Edge(v3, v0, line(v3, v0), true), true)
        ));

        assertEquals(4, loop.edgeCount());
    }

    @Test
    void shouldAllowSingleClosedCircularEdgeLoop() {
        Vertex v0 = new Vertex(new CartesianPoint(1.0, 0.0, 0.0));
        Circle circle = new Circle(
                new Axis2Placement3D(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        Direction3.from(new Vector3(0.0, 0.0, 1.0)),
                        Direction3.from(new Vector3(1.0, 0.0, 0.0))
                ),
                1.0
        );

        Edge edge = new Edge(v0, v0, circle, true);
        EdgeLoop loop = new EdgeLoop(List.of(new OrientedEdge(edge, true)));

        assertEquals(1, loop.getEdges().size());
    }

    @Test
    void shouldRejectCoincidentVerticesOnOpenCurve() {
        Vertex v0 = new Vertex(new CartesianPoint(0.0, 0.0, 0.0));
        Line3 line = new Line3(v0.getPoint(), Direction3.from(new Vector3(1.0, 0.0, 0.0)));

        TopologyException exception = assertThrows(
                TopologyException.class,
                () -> new Edge(v0, v0, line, true)
        );

        assertEquals("edge must have distinct vertices", exception.getMessage());
    }

    @Test
    void shouldAllowVerticesWithinImportToleranceOfCurve() {
        Vertex v0 = new Vertex(new CartesianPoint(0.0, 0.0, 0.0));
        Vertex v1 = new Vertex(new CartesianPoint(1.0, 0.0, 0.00005));
        Line3 line = new Line3(v0.getPoint(), Direction3.from(new Vector3(1.0, 0.0, 0.0)));

        Edge edge = new Edge(v0, v1, line, true);

        assertEquals(v1, edge.getEnd());
    }

    private static Line3 line(Vertex start, Vertex end) {
        return new Line3(start.getPoint(), Direction3.from(end.getPoint().subtract(start.getPoint())));
    }
}