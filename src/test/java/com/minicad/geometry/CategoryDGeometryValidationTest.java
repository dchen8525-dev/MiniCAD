package com.minicad.geometry;

import com.minicad.common.GeometryException;
import com.minicad.common.TopologyException;
import com.minicad.topology.*;
import com.minicad.topology.TopologyValidator.ValidationResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDGeometryValidationTest {

    @Test
    void shouldRejectBSplineWithMismatchedKnotControlPointCount() {
        assertThrows(GeometryException.class, () -> new BSplineCurve3(
                1,
                List.of(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        new CartesianPoint(1.0, 0.0, 0.0)),
                List.of(1, 1),
                List.of(0.0, 1.0)));
    }

    @Test
    void shouldRejectBSplineWithTooFewControlPointsForDegree() {
        assertThrows(GeometryException.class, () -> new BSplineCurve3(
                3,
                List.of(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        new CartesianPoint(1.0, 0.0, 0.0)),
                List.of(4, 4),
                List.of(0.0, 1.0)));
    }

    @Test
    void shouldAcceptValidBSpline() {
        BSplineCurve3 curve = new BSplineCurve3(
                1,
                List.of(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        new CartesianPoint(1.0, 0.0, 0.0)),
                List.of(2, 2),
                List.of(0.0, 1.0));
        assertEquals(1, curve.getDegree());
        assertEquals(2, curve.controlPointCount());
    }

    @Test
    void shouldRejectRationalBSplineWithWrongWeightCount() {
        assertThrows(GeometryException.class, () -> new RationalBSplineCurve3(
                1,
                List.of(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        new CartesianPoint(1.0, 0.0, 0.0)),
                List.of(1.0, 1.0, 1.0),
                List.of(2, 2),
                List.of(0.0, 1.0)));
    }

    @Test
    void shouldRejectRationalBSplineWithZeroWeight() {
        assertThrows(GeometryException.class, () -> new RationalBSplineCurve3(
                1,
                List.of(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        new CartesianPoint(1.0, 0.0, 0.0)),
                List.of(1.0, 0.0),
                List.of(2, 2),
                List.of(0.0, 1.0)));
    }

    @Test
    void shouldRejectRationalBSplineWithNegativeWeight() {
        assertThrows(GeometryException.class, () -> new RationalBSplineCurve3(
                1,
                List.of(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        new CartesianPoint(1.0, 0.0, 0.0)),
                List.of(1.0, -0.5),
                List.of(2, 2),
                List.of(0.0, 1.0)));
    }

    @Test
    void shouldRejectRationalBSplineWithInfiniteWeight() {
        assertThrows(GeometryException.class, () -> new RationalBSplineCurve3(
                1,
                List.of(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        new CartesianPoint(1.0, 0.0, 0.0)),
                List.of(1.0, Double.POSITIVE_INFINITY),
                List.of(2, 2),
                List.of(0.0, 1.0)));
    }

    @Test
    void shouldRejectRationalBSplineWithNaNWeight() {
        assertThrows(GeometryException.class, () -> new RationalBSplineCurve3(
                1,
                List.of(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        new CartesianPoint(1.0, 0.0, 0.0)),
                List.of(1.0, Double.NaN),
                List.of(2, 2),
                List.of(0.0, 1.0)));
    }

    @Test
    void shouldRejectRationalBSplineWithNullWeights() {
        assertThrows(GeometryException.class, () -> new RationalBSplineCurve3(
                1,
                List.of(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        new CartesianPoint(1.0, 0.0, 0.0)),
                null,
                List.of(2, 2),
                List.of(0.0, 1.0)));
    }

    @Test
    void shouldAcceptValidRationalBSpline() {
        RationalBSplineCurve3 curve = new RationalBSplineCurve3(
                1,
                List.of(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        new CartesianPoint(1.0, 0.0, 0.0)),
                List.of(1.0, 2.0),
                List.of(2, 2),
                List.of(0.0, 1.0));
        assertEquals(2, curve.weights().size());
        assertEquals(2.0, curve.weights().get(1));
    }

    @Test
    void shouldRejectTrimmedCurveWithNullBasisCurve() {
        assertThrows(GeometryException.class, () -> new TrimmedCurve3(
                null, 0.0, 1.0, true));
    }

    @Test
    void shouldRejectTrimmedCurveWithInfiniteTrimStart() {
        BSplineCurve3 basis = makeLinearBSpline();
        assertThrows(GeometryException.class, () -> new TrimmedCurve3(
                basis, Double.POSITIVE_INFINITY, 1.0, true));
    }

    @Test
    void shouldRejectTrimmedCurveWithNaNTrimEnd() {
        BSplineCurve3 basis = makeLinearBSpline();
        assertThrows(GeometryException.class, () -> new TrimmedCurve3(
                basis, 0.0, Double.NaN, true));
    }

    @Test
    void shouldAcceptTrimmedCurveWithReversedSense() {
        BSplineCurve3 basis = makeLinearBSpline();
        TrimmedCurve3 reversed = new TrimmedCurve3(basis, 0.0, 1.0, false);
        assertFalse(reversed.senseAgreement());
    }

    @Test
    void trimmedCurveReversedSenseShouldReversePointOrder() {
        BSplineCurve3 basis = makeLinearBSpline();
        TrimmedCurve3 forward = new TrimmedCurve3(basis, 0.0, 1.0, true);
        TrimmedCurve3 reversed = new TrimmedCurve3(basis, 0.0, 1.0, false);
        CartesianPoint forwardStart = forward.pointAt(0.0);
        CartesianPoint reversedEnd = reversed.pointAt(1.0);
        assertEquals(forwardStart.x(), reversedEnd.x(), 1e-9);
        assertEquals(forwardStart.y(), reversedEnd.y(), 1e-9);
        assertEquals(forwardStart.z(), reversedEnd.z(), 1e-9);
        CartesianPoint forwardEnd = forward.pointAt(1.0);
        CartesianPoint reversedStart = reversed.pointAt(0.0);
        assertEquals(forwardEnd.x(), reversedStart.x(), 1e-9);
        assertEquals(forwardEnd.y(), reversedStart.y(), 1e-9);
        assertEquals(forwardEnd.z(), reversedStart.z(), 1e-9);
    }

    @Test
    void trimmedCurveReversedSenseShouldReverseTangent() {
        BSplineCurve3 basis = makeLinearBSpline();
        TrimmedCurve3 forward = new TrimmedCurve3(basis, 0.0, 1.0, true);
        TrimmedCurve3 reversed = new TrimmedCurve3(basis, 0.0, 1.0, false);
        Vector3 forwardTangent = forward.tangentAt(0.5);
        Vector3 reversedTangent = reversed.tangentAt(0.5);
        assertEquals(forwardTangent.x(), -reversedTangent.x(), 1e-6);
        assertEquals(forwardTangent.y(), -reversedTangent.y(), 1e-6);
        assertEquals(forwardTangent.z(), -reversedTangent.z(), 1e-6);
    }

    @Test
    void trimmedCurveTrimEndpointsShouldMatchBasisCurveAtParams() {
        BSplineCurve3 basis = makeLinearBSpline();
        TrimmedCurve3 trimmed = new TrimmedCurve3(basis, 0.2, 0.8, true);
        CartesianPoint expectedStart = basis.pointAt(0.2);
        CartesianPoint expectedEnd = basis.pointAt(0.8);
        assertEquals(expectedStart.x(), trimmed.trimStart().x(), 1e-9);
        assertEquals(expectedStart.y(), trimmed.trimStart().y(), 1e-9);
        assertEquals(expectedEnd.x(), trimmed.trimEnd().x(), 1e-9);
        assertEquals(expectedEnd.y(), trimmed.trimEnd().y(), 1e-9);
    }

    @Test
    void shouldRejectZeroLengthEdge() {
        CartesianPoint samePoint = new CartesianPoint(1.0, 2.0, 3.0);
        Vertex v1 = new Vertex(samePoint);
        Vertex v2 = new Vertex(samePoint);
        BSplineCurve3 curve = makeLinearBSpline();
        assertThrows(TopologyException.class, () -> new Edge(v1, v2, curve, true));
    }

    @Test
    void shouldRejectNearZeroLengthEdge() {
        Vertex v1 = new Vertex(new CartesianPoint(0.0, 0.0, 0.0));
        Vertex v2 = new Vertex(new CartesianPoint(0.001, 0.0, 0.0));
        BSplineCurve3 curve = makeLinearBSpline();
        assertThrows(TopologyException.class, () -> new Edge(v1, v2, curve, true));
    }

    @Test
    void shouldAcceptEdgeWithDistinctVertices() {
        Vertex v1 = new Vertex(new CartesianPoint(0.0, 0.0, 0.0));
        Vertex v2 = new Vertex(new CartesianPoint(1.0, 0.0, 0.0));
        BSplineCurve3 curve = makeLinearBSpline();
        Edge edge = new Edge(v1, v2, curve, true);
        assertNotNull(edge);
        assertTrue(edge.length() > 0);
    }

    @Test
    void shouldRejectEdgeWithNullCurve() {
        Vertex v1 = new Vertex(new CartesianPoint(0.0, 0.0, 0.0));
        Vertex v2 = new Vertex(new CartesianPoint(1.0, 0.0, 0.0));
        assertThrows(TopologyException.class, () -> new Edge(v1, v2, null, true));
    }

    @Test
    void shouldDetectZeroAreaFaceViaTopologyValidator() {
        Plane plane = new Plane(
                new CartesianPoint(0, 0, 0),
                Direction3.from(new Vector3(0, 0, 1)));
        Vertex v0 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v1 = new Vertex(new CartesianPoint(1, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(2, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(3, 0, 0));
        BSplineCurve3 c1 = new BSplineCurve3(1,
                List.of(new CartesianPoint(0, 0, 0), new CartesianPoint(1, 0, 0)),
                List.of(2, 2), List.of(0.0, 1.0));
        BSplineCurve3 c2 = new BSplineCurve3(1,
                List.of(new CartesianPoint(1, 0, 0), new CartesianPoint(2, 0, 0)),
                List.of(2, 2), List.of(0.0, 1.0));
        BSplineCurve3 c3 = new BSplineCurve3(1,
                List.of(new CartesianPoint(2, 0, 0), new CartesianPoint(3, 0, 0)),
                List.of(2, 2), List.of(0.0, 1.0));
        BSplineCurve3 c4 = new BSplineCurve3(1,
                List.of(new CartesianPoint(3, 0, 0), new CartesianPoint(0, 0, 0)),
                List.of(2, 2), List.of(0.0, 1.0));
        Edge e01 = new Edge(v0, v1, c1, true);
        Edge e12 = new Edge(v1, v2, c2, true);
        Edge e23 = new Edge(v2, v3, c3, true);
        Edge e30 = new Edge(v3, v0, c4, true);
        EdgeLoop loop = new EdgeLoop(List.of(
                new OrientedEdge(e01, true),
                new OrientedEdge(e12, true),
                new OrientedEdge(e23, true),
                new OrientedEdge(e30, true)));
        Face face = new Face(plane, List.of(FaceBound.outer(loop, true)), true);
        Shell shell = new Shell(List.of(face), false);
        ValidationResult result = TopologyValidator.validateShell(shell);
        String zeroAreaCode = "zero_area";
        assertTrue(result.issues().stream()
                        .anyMatch(i -> i.code().contains(zeroAreaCode)),
                "Expected zero_area issue for collinear face vertices");
    }

    @Test
    void shouldAcceptValidAreaFaceViaTopologyValidator() {
        Plane plane = new Plane(
                new CartesianPoint(0, 0, 0),
                Direction3.from(new Vector3(0, 0, 1)));
        Vertex v0 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v1 = new Vertex(new CartesianPoint(1, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(1, 1, 0));
        Vertex v3 = new Vertex(new CartesianPoint(0, 1, 0));
        BSplineCurve3 c01 = new BSplineCurve3(1,
                List.of(new CartesianPoint(0, 0, 0), new CartesianPoint(1, 0, 0)),
                List.of(2, 2), List.of(0.0, 1.0));
        BSplineCurve3 c12 = new BSplineCurve3(1,
                List.of(new CartesianPoint(1, 0, 0), new CartesianPoint(1, 1, 0)),
                List.of(2, 2), List.of(0.0, 1.0));
        BSplineCurve3 c23 = new BSplineCurve3(1,
                List.of(new CartesianPoint(1, 1, 0), new CartesianPoint(0, 1, 0)),
                List.of(2, 2), List.of(0.0, 1.0));
        BSplineCurve3 c30 = new BSplineCurve3(1,
                List.of(new CartesianPoint(0, 1, 0), new CartesianPoint(0, 0, 0)),
                List.of(2, 2), List.of(0.0, 1.0));
        Edge e01 = new Edge(v0, v1, c01, true);
        Edge e12 = new Edge(v1, v2, c12, true);
        Edge e23 = new Edge(v2, v3, c23, true);
        Edge e30 = new Edge(v3, v0, c30, true);
        EdgeLoop loop = new EdgeLoop(List.of(
                new OrientedEdge(e01, true),
                new OrientedEdge(e12, true),
                new OrientedEdge(e23, true),
                new OrientedEdge(e30, true)));
        Face face = new Face(plane, List.of(FaceBound.outer(loop, true)), true);
        Shell shell = new Shell(List.of(face), false);
        ValidationResult result = TopologyValidator.validateShell(shell);
        String zeroAreaCode = "zero_area";
        assertFalse(result.issues().stream()
                        .anyMatch(i -> i.code().contains(zeroAreaCode)),
                "Valid face should not trigger zero_area issue");
    }

    @Test
    void shouldDetectZeroAreaFaceFromPolyLoop() {
        Plane plane = new Plane(
                new CartesianPoint(0, 0, 0),
                Direction3.from(new Vector3(0, 0, 1)));
        PolyLoop loop = new PolyLoop(List.of(
                new CartesianPoint(0, 0, 0),
                new CartesianPoint(0, 0, 0),
                new CartesianPoint(0, 0, 0)));
        Face face = new Face(plane, List.of(FaceBound.outer(loop, true)), true);
        Shell shell = new Shell(List.of(face), false);
        ValidationResult result = TopologyValidator.validateShell(shell);
        String zeroAreaCode = "zero_area";
        assertTrue(result.issues().stream()
                        .anyMatch(i -> i.code().contains(zeroAreaCode)),
                "Expected zero_area issue for coincident poly loop points");
    }

    private static BSplineCurve3 makeLinearBSpline() {
        return new BSplineCurve3(
                1,
                List.of(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        new CartesianPoint(1.0, 0.0, 0.0)),
                List.of(2, 2),
                List.of(0.0, 1.0));
    }
}
