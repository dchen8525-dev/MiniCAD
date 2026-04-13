package com.minicad.topology;

import com.minicad.common.TopologyException;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.CylindricalSurface;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.Plane;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.Vector3;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FaceTest {

    @Test
    void shouldBuildPlanarFaceWithOuterBound() {
        Face face = new Face(
                new Plane(new CartesianPoint(0.0, 0.0, 0.0), Direction3.from(new Vector3(0.0, 0.0, 1.0))),
                List.of(FaceBound.outer(squareLoop(), true)),
                true
        );

        assertEquals(1, face.bounds().size());
    }

    @Test
    void shouldRejectFaceWithoutOuterBound() {
        TopologyException exception = assertThrows(
                TopologyException.class,
                () -> new Face(
                        new Plane(new CartesianPoint(0.0, 0.0, 0.0), Direction3.from(new Vector3(0.0, 0.0, 1.0))),
                        List.of(FaceBound.inner(squareLoop(), true)),
                        true
                )
        );

        assertEquals("face must contain an outer bound", exception.getMessage());
    }

    @Test
    void shouldRejectVertexOutsidePlane() {
        Vertex v0 = new Vertex(new CartesianPoint(0.0, 0.0, 0.0));
        Vertex v1 = new Vertex(new CartesianPoint(1.0, 0.0, 0.0));
        Vertex v2 = new Vertex(new CartesianPoint(0.0, 1.0, 1.0));

        EdgeLoop loop = new EdgeLoop(List.of(
                new OrientedEdge(new Edge(v0, v1, line(v0, v1), true), true),
                new OrientedEdge(new Edge(v1, v2, line(v1, v2), true), true),
                new OrientedEdge(new Edge(v2, v0, line(v2, v0), true), true)
        ));

        TopologyException exception = assertThrows(
                TopologyException.class,
                () -> new Face(
                        new Plane(new CartesianPoint(0.0, 0.0, 0.0), Direction3.from(new Vector3(0.0, 0.0, 1.0))),
                        List.of(FaceBound.outer(loop, true)),
                        true
                )
        );

        assertEquals("all face vertices must lie on the plane", exception.getMessage());
    }

    @Test
    void shouldAllowNonPlanarFaceWithoutPlanarContainmentCheck() {
        Vertex v0 = new Vertex(new CartesianPoint(2.0, 0.0, 0.0));
        Vertex v1 = new Vertex(new CartesianPoint(0.0, 2.0, 0.0));
        Vertex v2 = new Vertex(new CartesianPoint(0.0, 2.0, 1.0));

        Face face = new Face(
                new CylindricalSurface(
                        new Axis2Placement3D(
                                new CartesianPoint(0.0, 0.0, 0.0),
                                Direction3.from(new Vector3(0.0, 0.0, 1.0)),
                                Direction3.from(new Vector3(1.0, 0.0, 0.0))
                        ),
                        2.0
                ),
                List.of(FaceBound.outer(new PolyLoop(List.of(v0.point(), v1.point(), v2.point())), true)),
                true
        );

        assertEquals(1, face.bounds().size());
    }

    private static EdgeLoop squareLoop() {
        Vertex v0 = new Vertex(new CartesianPoint(0.0, 0.0, 0.0));
        Vertex v1 = new Vertex(new CartesianPoint(1.0, 0.0, 0.0));
        Vertex v2 = new Vertex(new CartesianPoint(1.0, 1.0, 0.0));
        Vertex v3 = new Vertex(new CartesianPoint(0.0, 1.0, 0.0));

        return new EdgeLoop(List.of(
                new OrientedEdge(new Edge(v0, v1, line(v0, v1), true), true),
                new OrientedEdge(new Edge(v1, v2, line(v1, v2), true), true),
                new OrientedEdge(new Edge(v2, v3, line(v2, v3), true), true),
                new OrientedEdge(new Edge(v3, v0, line(v3, v0), true), true)
        ));
    }

    private static Line3 line(Vertex start, Vertex end) {
        return new Line3(start.point(), Direction3.from(end.point().subtract(start.point())));
    }
}
