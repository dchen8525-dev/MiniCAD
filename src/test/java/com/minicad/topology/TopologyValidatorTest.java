package com.minicad.topology;

import com.minicad.geometry.BSplineCurve3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Plane;
import com.minicad.geometry.Vector3;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TopologyValidatorTest {

    @Test
    void shouldReportEdgeUseCountOnClosedShellWithSingleFace() {
        Shell shell = new Shell(List.of(squareFace()), true);

        TopologyValidator.ValidationResult result = TopologyValidator.validateShell(shell);

        assertFalse(result.issues().isEmpty());
        assertTrue(result.issues().stream().anyMatch(i -> i.code().contains("closed_shell.edge_use_count")));
    }

    @Test
    void shouldReportNonManifoldEdge() {
        // Three faces all sharing the same edge e01 (used 3 times -> non-manifold)
        Vertex v0 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v1 = new Vertex(new CartesianPoint(1, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(0, 1, 0));
        Vertex v3 = new Vertex(new CartesianPoint(-1, 0, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, -1, 0));

        Edge e01 = new Edge(v0, v1, linearBSpline(v0, v1), true);
        Edge e12 = new Edge(v1, v2, linearBSpline(v1, v2), true);
        Edge e20 = new Edge(v2, v0, linearBSpline(v2, v0), true);
        Edge e13 = new Edge(v1, v3, linearBSpline(v1, v3), true);
        Edge e30 = new Edge(v3, v0, linearBSpline(v3, v0), true);
        Edge e14 = new Edge(v1, v4, linearBSpline(v1, v4), true);
        Edge e40 = new Edge(v4, v0, linearBSpline(v4, v0), true);

        // All faces on z=0 plane, sharing edge e01
        Face face1 = new Face(planeZ0(), List.of(FaceBound.outer(
                new EdgeLoop(List.of(
                        new OrientedEdge(e01, true),
                        new OrientedEdge(e12, true),
                        new OrientedEdge(e20, true)
                )), true)), true);

        Face face2 = new Face(planeZ0(), List.of(FaceBound.outer(
                new EdgeLoop(List.of(
                        new OrientedEdge(e01, true),
                        new OrientedEdge(e13, true),
                        new OrientedEdge(e30, true)
                )), true)), true);

        Face face3 = new Face(planeZ0(), List.of(FaceBound.outer(
                new EdgeLoop(List.of(
                        new OrientedEdge(e01, true),
                        new OrientedEdge(e14, true),
                        new OrientedEdge(e40, true)
                )), true)), true);

        Shell shell = new Shell(List.of(face1, face2, face3), false);

        TopologyValidator.ValidationResult result = TopologyValidator.validateShell(shell);

        assertTrue(result.issues().stream().anyMatch(i -> i.code().contains("non_manifold_edge")));
    }

    @Test
    void shouldReportEdgeOrientationOnClosedShell() {
        Vertex v0 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v1 = new Vertex(new CartesianPoint(1, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(1, 1, 0));

        Edge e01 = new Edge(v0, v1, linearBSpline(v0, v1), true);
        Edge e12 = new Edge(v1, v2, linearBSpline(v1, v2), true);
        Edge e20 = new Edge(v2, v0, linearBSpline(v2, v0), true);

        // Two faces using edges in same direction (should be opposite for closed shell)
        Face face1 = new Face(planeZ0(), List.of(FaceBound.outer(
                new EdgeLoop(List.of(
                        new OrientedEdge(e01, true),
                        new OrientedEdge(e12, true),
                        new OrientedEdge(e20, true)
                )), true)), true);

        Face face2 = new Face(planeZ0(), List.of(FaceBound.outer(
                new EdgeLoop(List.of(
                        new OrientedEdge(e01, true),
                        new OrientedEdge(e12, true),
                        new OrientedEdge(e20, true)
                )), true)), true);

        Shell shell = new Shell(List.of(face1, face2), true);

        TopologyValidator.ValidationResult result = TopologyValidator.validateShell(shell);

        assertTrue(result.issues().stream().anyMatch(i -> i.code().contains("closed_shell.edge_orientation")));
    }

    @Test
    void shouldReportNullShell() {
        TopologyValidator.ValidationResult result = TopologyValidator.validateShell(null);

        assertFalse(result.issues().isEmpty());
        assertTrue(result.issues().stream().anyMatch(i -> i.code().contains("shell.null")));
    }

    @Test
    void shouldReportNullSolid() {
        TopologyValidator.ValidationResult result = TopologyValidator.validateSolid(null);

        assertFalse(result.issues().isEmpty());
        assertTrue(result.issues().stream().anyMatch(i -> i.code().contains("solid.null")));
    }

    @Test
    void shouldReportVoidShellOutsideOuter() {
        Vertex outerV0 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex outerV1 = new Vertex(new CartesianPoint(2, 0, 0));
        Vertex outerV2 = new Vertex(new CartesianPoint(2, 2, 0));
        Vertex outerV3 = new Vertex(new CartesianPoint(0, 2, 0));

        Face outerFace = new Face(planeZ0(), List.of(FaceBound.outer(
                new EdgeLoop(List.of(
                        new OrientedEdge(new Edge(outerV0, outerV1, linearBSpline(outerV0, outerV1), true), true),
                        new OrientedEdge(new Edge(outerV1, outerV2, linearBSpline(outerV1, outerV2), true), true),
                        new OrientedEdge(new Edge(outerV2, outerV3, linearBSpline(outerV2, outerV3), true), true),
                        new OrientedEdge(new Edge(outerV3, outerV0, linearBSpline(outerV3, outerV0), true), true)
                )), true)), true);

        Shell outerShell = new Shell(List.of(outerFace), true);

        // Void shell outside outer bbox
        Vertex voidV0 = new Vertex(new CartesianPoint(5, 5, 10));
        Vertex voidV1 = new Vertex(new CartesianPoint(6, 5, 10));
        Vertex voidV2 = new Vertex(new CartesianPoint(6, 6, 10));
        Vertex voidV3 = new Vertex(new CartesianPoint(5, 6, 10));

        Plane planeZ10 = new Plane(new CartesianPoint(0, 0, 10), Direction3.from(new Vector3(0, 0, 1)));
        Face voidFace = new Face(planeZ10, List.of(FaceBound.outer(
                new EdgeLoop(List.of(
                        new OrientedEdge(new Edge(voidV0, voidV1, linearBSpline(voidV0, voidV1), true), true),
                        new OrientedEdge(new Edge(voidV1, voidV2, linearBSpline(voidV1, voidV2), true), true),
                        new OrientedEdge(new Edge(voidV2, voidV3, linearBSpline(voidV2, voidV3), true), true),
                        new OrientedEdge(new Edge(voidV3, voidV0, linearBSpline(voidV3, voidV0), true), true)
                )), true)), true);

        Shell voidShell = new Shell(List.of(voidFace), true);

        Solid solid = new Solid(outerShell, List.of(voidShell));

        TopologyValidator.ValidationResult result = TopologyValidator.validateSolid(solid);

        assertTrue(result.issues().stream().anyMatch(i -> i.code().contains("solid.void_outside_outer")));
    }

    @Test
    void shouldAcceptValidOpenShell() {
        Shell shell = new Shell(List.of(squareFace()), false);

        TopologyValidator.ValidationResult result = TopologyValidator.validateShell(shell);

        // Open shell should not report closed shell edge issues
        assertFalse(result.issues().stream().anyMatch(i -> i.code().contains("closed_shell")));
    }

    @Test
    void shouldDetectZeroAreaFace() {
        Vertex v0 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v1 = new Vertex(new CartesianPoint(1, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(2, 0, 0));

        Face face = new Face(planeZ0(), List.of(FaceBound.outer(
                new EdgeLoop(List.of(
                        new OrientedEdge(new Edge(v0, v1, linearBSpline(v0, v1), true), true),
                        new OrientedEdge(new Edge(v1, v2, linearBSpline(v1, v2), true), true),
                        new OrientedEdge(new Edge(v2, v0, linearBSpline(v2, v0), true), true)
                )), true)), true);

        Shell shell = new Shell(List.of(face), false);

        TopologyValidator.ValidationResult result = TopologyValidator.validateShell(shell);

        assertTrue(result.issues().stream().anyMatch(i -> i.code().contains("zero_area")));
    }

    private static Face squareFace() {
        Vertex v0 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v1 = new Vertex(new CartesianPoint(1, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(1, 1, 0));
        Vertex v3 = new Vertex(new CartesianPoint(0, 1, 0));

        return new Face(planeZ0(), List.of(FaceBound.outer(
                new EdgeLoop(List.of(
                        new OrientedEdge(new Edge(v0, v1, linearBSpline(v0, v1), true), true),
                        new OrientedEdge(new Edge(v1, v2, linearBSpline(v1, v2), true), true),
                        new OrientedEdge(new Edge(v2, v3, linearBSpline(v2, v3), true), true),
                        new OrientedEdge(new Edge(v3, v0, linearBSpline(v3, v0), true), true)
                )), true)), true);
    }

    private static BSplineCurve3 linearBSpline(Vertex start, Vertex end) {
        return new BSplineCurve3(
                1,
                List.of(start.point(), end.point()),
                List.of(2, 2),
                List.of(0.0, 1.0));
    }

    private static Plane planeZ0() {
        return new Plane(new CartesianPoint(0, 0, 0), Direction3.from(new Vector3(0, 0, 1)));
    }
}
