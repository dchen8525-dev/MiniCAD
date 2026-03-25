package com.minicad.topology;

import com.minicad.common.TopologyException;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Line3;
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

        assertEquals(4, loop.edges().size());
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

        assertEquals("edge loop must be connected and closed", exception.getMessage());
    }

    private static Line3 line(Vertex start, Vertex end) {
        return new Line3(start.point(), Direction3.from(end.point().subtract(start.point())));
    }
}
