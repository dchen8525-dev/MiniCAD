package com.minicad.export.json;

import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Circle;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.DegenerateCurve3;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Polyline3;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Runtime behaviour tests for the EDGE_SAMPLE_RULES dispatch in
 * StepEdgePayloadBuilder.sampleEdge, exercised through pure geometry objects
 * (no builder needed: the chain only samples the curve and pins the edge
 * endpoints).
 */
class EdgeSampleBehaviorTest {

    private static final CartesianPoint START = new CartesianPoint(1.0, 0.0, 0.0);
    private static final CartesianPoint END = new CartesianPoint(4.0, 0.0, 0.0);

    private static void assertPinned(List<CartesianPoint> points, int expectedMidpoints) {
        assertEquals(expectedMidpoints + 2, points.size());
        assertEquals(1.0, points.get(0).getX(), 1.0e-9);
        assertEquals(0.0, points.get(0).getY(), 1.0e-9);
        assertEquals(4.0, points.get(points.size() - 1).getX(), 1.0e-9);
        assertEquals(0.0, points.get(points.size() - 1).getY(), 1.0e-9);
    }

    @Test
    @DisplayName("Line3 collapses the sample to the pinned endpoints")
    void lineSampleIsJustEndpoints() {
        List<CartesianPoint> points = StepEdgePayloadBuilder.sampleEdge(START, END,
                new com.minicad.geometry.Line3(new CartesianPoint(0.0, 0.0, 0.0),
                        new Direction3(1.0, 0.0, 0.0), 5.0), true);
        assertEquals(2, points.size());
        assertEquals(START.getX(), points.get(0).getX(), 1.0e-9);
        assertEquals(END.getX(), points.get(1).getX(), 1.0e-9);
    }

    @Test
    @DisplayName("DegenerateCurve3 collapses the sample to the pinned endpoints")
    void degenerateSampleIsJustEndpoints() {
        List<CartesianPoint> points = StepEdgePayloadBuilder.sampleEdge(START, END,
                new DegenerateCurve3(new CartesianPoint(0.0, 0.0, 0.0)), true);
        assertEquals(2, points.size());
        assertEquals(START.getX(), points.get(0).getX(), 1.0e-9);
        assertEquals(END.getX(), points.get(1).getX(), 1.0e-9);
    }

    @Test
    @DisplayName("Polyline3 keeps interior points forward and pins endpoints")
    void polylineForwardPinsEndpoints() {
        Polyline3 polyline = new Polyline3(List.of(
                new CartesianPoint(0.0, 0.0, 0.0),
                new CartesianPoint(2.0, 1.0, 0.0),
                new CartesianPoint(9.0, 9.0, 0.0)));
        List<CartesianPoint> points = StepEdgePayloadBuilder.sampleEdge(START, END, polyline, true);
        assertPinned(points, 1);
        assertEquals(2.0, points.get(1).getX(), 1.0e-9);
        assertEquals(1.0, points.get(1).getY(), 1.0e-9);
    }

    @Test
    @DisplayName("Polyline3 with naturalForward=false reverses interior point order and pins endpoints")
    void polylineBackwardReversesInterior() {
        Polyline3 polyline = new Polyline3(List.of(
                new CartesianPoint(0.0, 0.0, 0.0),
                new CartesianPoint(2.0, 1.0, 0.0),
                new CartesianPoint(9.0, 0.0, 0.0)));
        List<CartesianPoint> points = StepEdgePayloadBuilder.sampleEdge(START, END, polyline, false);
        assertPinned(points, 1);
        // list reversal changes the order of interior points, not their coordinates
        assertEquals(2.0, points.get(1).getX(), 1.0e-9);
        assertEquals(1.0, points.get(1).getY(), 1.0e-9);
    }

    @Test
    @DisplayName("Circle samples the arc between the pinned endpoints")
    void circleSamplePinsEndpoints() {
        Circle circle = new Circle(new Axis2Placement3D(
                new CartesianPoint(0.0, 0.0, 0.0),
                new Direction3(0.0, 0.0, 1.0),
                new Direction3(1.0, 0.0, 0.0)), 3.0);
        // endpoints on the circle: angle 0 and angle PI
        CartesianPoint start = circle.pointAt(0.0);
        CartesianPoint end = circle.pointAt(Math.PI);
        List<CartesianPoint> forward = StepEdgePayloadBuilder.sampleEdge(start, end, circle, true);
        assertTrue(forward.size() >= 16, "circle arc sample should be dense");
        assertEquals(3.0, forward.get(0).getX(), 1.0e-9);
        assertEquals(0.0, forward.get(0).getY(), 1.0e-9);
        assertEquals(-3.0, forward.get(forward.size() - 1).getX(), 1.0e-9);
        // forward arc passes through +Y; backward arc passes through -Y
        CartesianPoint midForward = forward.get(forward.size() / 2);
        assertTrue(midForward.getY() > 0.0, "forward arc must pass through +Y");
        List<CartesianPoint> backward = StepEdgePayloadBuilder.sampleEdge(start, end, circle, false);
        CartesianPoint midBackward = backward.get(backward.size() / 2);
        assertTrue(midBackward.getY() < 0.0, "backward arc must pass through -Y");
    }

    @Test
    @DisplayName("a curve type with no rule throws the terminal UnsupportedGeometryException")
    void unknownCurveThrows() {
        Curve3 unknown = new Curve3() {
            @Override
            public boolean contains(CartesianPoint point) {
                return false;
            }

            @Override
            public CartesianPoint pointAt(double parameter) {
                return new CartesianPoint(0.0, 0.0, 0.0);
            }

            @Override
            public CartesianPoint closestPointTo(CartesianPoint point) {
                return new CartesianPoint(0.0, 0.0, 0.0);
            }
        };
        UnsupportedGeometryException exception = assertThrows(UnsupportedGeometryException.class,
                () -> StepEdgePayloadBuilder.sampleEdge(START, END, unknown, true));
        assertTrue(exception.getMessage().contains("preview export requires"),
                "unexpected message: " + exception.getMessage());
    }
}
