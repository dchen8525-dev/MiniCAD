package com.minicad.topology;

import com.minicad.common.TopologyException;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.Plane;
import com.minicad.geometry.Vector3;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SolidTest {

    @Test
    void shouldCreateSolidFromClosedShell() {
        Face face = faceOnZ0();
        Shell shell = new Shell(List.of(face), true);

        Solid solid = new Solid(shell);

        assertEquals(shell, solid.outerShell());
        assertEquals(0, solid.voidShells().size());
    }

    @Test
    void shouldRejectSolidFromOpenShell() {
        Shell shell = new Shell(List.of(faceOnZ0()), false);

        TopologyException exception = assertThrows(TopologyException.class, () -> new Solid(shell));

        assertEquals("solid requires a closed shell", exception.getMessage());
    }

    @Test
    void shouldCreateSolidWithVoidShells() {
        Shell shell = new Shell(List.of(faceOnZ0()), true);
        Shell voidShell = new Shell(List.of(faceOnZ0()), true);

        Solid solid = new Solid(shell, List.of(voidShell));

        assertEquals(1, solid.voidShells().size());
    }

    private static Face faceOnZ0() {
        Vertex v0 = new Vertex(new CartesianPoint(0.0, 0.0, 0.0));
        Vertex v1 = new Vertex(new CartesianPoint(1.0, 0.0, 0.0));
        Vertex v2 = new Vertex(new CartesianPoint(1.0, 1.0, 0.0));
        Vertex v3 = new Vertex(new CartesianPoint(0.0, 1.0, 0.0));

        EdgeLoop loop = new EdgeLoop(List.of(
                new OrientedEdge(new Edge(v0, v1, line(v0, v1), true), true),
                new OrientedEdge(new Edge(v1, v2, line(v1, v2), true), true),
                new OrientedEdge(new Edge(v2, v3, line(v2, v3), true), true),
                new OrientedEdge(new Edge(v3, v0, line(v3, v0), true), true)
        ));

        return new Face(
                new Plane(new CartesianPoint(0.0, 0.0, 0.0), Direction3.from(new Vector3(0.0, 0.0, 1.0))),
                List.of(FaceBound.outer(loop, true)),
                true
        );
    }

    private static Line3 line(Vertex start, Vertex end) {
        return new Line3(start.point(), Direction3.from(end.point().subtract(start.point())));
    }
}
