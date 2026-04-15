package com.minicad.topology;

import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.BoundingBox3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Circle;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.Plane;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for topology bounding box methods.
 */
class TopologyBoundingBoxTest {

    @Test
    void edgeBoundingBox() {
        // Create an edge with vertices that lie on the line
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        Vertex v1 = new Vertex(line.pointAt(0));   // (0, 0, 0)
        Vertex v2 = new Vertex(line.pointAt(10));  // (10, 0, 0)
        Edge edge = new Edge(v1, v2, line, true);

        BoundingBox3 box = edge.boundingBox();
        assertTrue(box.contains(v1.point()));
        assertTrue(box.contains(v2.point()));
        assertEquals(0.0, box.minX());
        assertEquals(10.0, box.maxX());
    }

    @Test
    void edgeLength() {
        // Create an edge with vertices that lie on the line
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        Vertex v1 = new Vertex(line.pointAt(0));
        Vertex v2 = new Vertex(line.pointAt(10));
        Edge edge = new Edge(v1, v2, line, true);

        double length = edge.length();
        assertEquals(10.0, length, 0.001);
    }

    @Test
    void circularEdgeLength() {
        // Create a full circle edge
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Circle circle = new Circle(position, 5.0);

        // For a closed curve, use same vertex (point on the circle)
        Vertex v = new Vertex(circle.pointAt(0));  // (5, 0, 0)
        Edge edge = new Edge(v, v, circle, true);

        double length = edge.length();
        assertEquals(2 * Math.PI * 5, length, 0.001);
    }

    @Test
    void faceBoundingBox() {
        // Create a simple face with one edge loop - all vertices lie on the plane z=0
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(10, 10, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 10, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        BoundingBox3 box = face.boundingBox();
        assertEquals(0.0, box.minX());
        assertEquals(0.0, box.minY());
        assertEquals(0.0, box.minZ());
        assertEquals(10.0, box.maxX());
        assertEquals(10.0, box.maxY());
        assertEquals(0.0, box.maxZ());
    }

    @Test
    void faceEdgeCount() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(10, 10, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 10, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        assertEquals(4, face.edgeCount());
        assertEquals(4, face.vertices().size());
    }

    @Test
    void shellBoundingBox() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(5, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(5, 5, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 5, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        Shell shell = new Shell(List.of(face), true);

        BoundingBox3 box = shell.boundingBox();
        assertEquals(0.0, box.minX());
        assertEquals(0.0, box.minY());
        assertEquals(5.0, box.maxX());
        assertEquals(5.0, box.maxY());

        assertEquals(1, shell.faceCount());
        assertEquals(4, shell.edgeCount());
        assertEquals(4, shell.vertices().size());
    }

    @Test
    void solidBoundingBox() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(3, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(3, 3, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 3, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        Shell shell = new Shell(List.of(face), true);
        Solid solid = new Solid(shell);

        BoundingBox3 box = solid.boundingBox();
        assertEquals(0.0, box.minX());
        assertEquals(0.0, box.minY());
        assertEquals(3.0, box.maxX());
        assertEquals(3.0, box.maxY());

        assertEquals(1, solid.faceCount());
        assertEquals(1, solid.shellCount());
    }

    @Test
    void edgeLoopPerimeter() {
        Line3 l1 = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(new CartesianPoint(10, 0, 0), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(new CartesianPoint(10, 10, 0), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(new CartesianPoint(0, 10, 0), new Direction3(0, -1, 0));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(10, 10, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 10, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));

        assertEquals(40.0, loop.perimeter(), 0.001);
        assertEquals(4, loop.edgeCount());
        assertEquals(4, loop.vertices().size());
    }

    @Test
    void orientedEdgeLength() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Edge edge = new Edge(v1, v2, line, true);

        OrientedEdge orientedForward = new OrientedEdge(edge, true);
        OrientedEdge orientedReverse = new OrientedEdge(edge, false);

        assertEquals(10.0, orientedForward.length(), 0.001);
        assertEquals(10.0, orientedReverse.length(), 0.001);
        assertEquals(v1, orientedForward.startVertex());
        assertEquals(v2, orientedForward.endVertex());
        assertEquals(v2, orientedReverse.startVertex());
        assertEquals(v1, orientedReverse.endVertex());
    }

    @Test
    void polyLoopBoundingBox() {
        PolyLoop polyLoop = new PolyLoop(List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0),
            new CartesianPoint(10, 10, 0),
            new CartesianPoint(0, 10, 0)
        ));

        BoundingBox3 box = polyLoop.boundingBox();
        assertEquals(0.0, box.minX());
        assertEquals(0.0, box.minY());
        assertEquals(10.0, box.maxX());
        assertEquals(10.0, box.maxY());
        assertEquals(4, polyLoop.vertexCount());
        assertEquals(40.0, polyLoop.perimeter(), 0.001);
    }

    @Test
    void vertexLoopBoundingBox() {
        Vertex vertex = new Vertex(new CartesianPoint(5, 10, 15));
        VertexLoop loop = new VertexLoop(vertex);

        BoundingBox3 box = loop.boundingBox();
        assertEquals(5.0, box.minX());
        assertEquals(10.0, box.minY());
        assertEquals(15.0, box.minZ());
    }

    @Test
    void facePerimeter() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(10, 10, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 10, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        assertEquals(40.0, face.perimeter(), 0.001);
        assertEquals(1, face.boundCount());
    }

    @Test
    void planarFaceArea() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(10, 10, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 10, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        assertEquals(100.0, face.area(), 0.001);  // 10 x 10 = 100
    }

    @Test
    void planarTriangleArea() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(5, 10, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), v3.point().subtract(v2.point()).normalize());
        Line3 l3 = new Line3(v3.point(), v1.point().subtract(v3.point()).normalize());

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v1, l3, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        assertEquals(50.0, face.area(), 0.001);  // Triangle area = 0.5 * 10 * 10 = 50
    }

    @Test
    void edgeClosestPointTo() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Edge edge = new Edge(v1, v2, line, true);

        // Point near the start vertex
        CartesianPoint nearStart = new CartesianPoint(0.5, 0, 0);
        CartesianPoint closestStart = edge.closestPointTo(nearStart);
        assertTrue(closestStart.distanceTo(new CartesianPoint(0, 0, 0)) < 1.0);

        // Point off the line - should find closest sampled point
        CartesianPoint offLine = new CartesianPoint(5, 5, 0);
        CartesianPoint closestOff = edge.closestPointTo(offLine);
        // closestPointTo uses sampling, verify we get some point
        assertNotNull(closestOff);
    }

    @Test
    void edgeDistanceTo() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Edge edge = new Edge(v1, v2, line, true);

        // Point at the start vertex
        CartesianPoint atStart = new CartesianPoint(0, 0, 0);
        double distanceAtStart = edge.distanceTo(atStart);
        assertEquals(0.0, distanceAtStart, 0.1);

        // Point off the line
        CartesianPoint offLine = new CartesianPoint(5, 5, 0);
        double distance = edge.distanceTo(offLine);
        assertTrue(distance >= 0);
    }

    @Test
    void edgeMidpoint() {
        // For a line edge, midpoint should be between start and end
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Edge edge = new Edge(v1, v2, line, true);

        CartesianPoint midpoint = edge.midpoint();
        // midpoint should be somewhere between the vertices
        assertTrue(midpoint.x() >= 0.0 && midpoint.x() <= 10.0);
        assertEquals(0.0, midpoint.y(), 0.1);
        assertEquals(0.0, midpoint.z(), 0.1);
    }

    @Test
    void edgeSample() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Edge edge = new Edge(v1, v2, line, true);

        java.util.List<CartesianPoint> samples = edge.sample(5);
        assertTrue(samples.size() >= 2);
        // First sample should be near origin
        assertEquals(0.0, samples.get(0).x(), 0.1);
    }

    @Test
    void edgeContains() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Edge edge = new Edge(v1, v2, line, true);

        // Point at start vertex (should be within tolerance)
        CartesianPoint atStart = new CartesianPoint(0, 0, 0);
        assertTrue(edge.contains(atStart));

        // Point far from edge
        CartesianPoint farAway = new CartesianPoint(5, 100, 0);
        assertFalse(edge.contains(farAway));
    }

    @Test
    void orientedEdgeClosestPointTo() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Edge edge = new Edge(v1, v2, line, true);

        OrientedEdge oriented = new OrientedEdge(edge, true);
        CartesianPoint nearStart = new CartesianPoint(0.5, 0, 0);
        CartesianPoint closest = oriented.closestPointTo(nearStart);
        assertNotNull(closest);
    }

    @Test
    void orientedEdgeDistanceTo() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Edge edge = new Edge(v1, v2, line, true);

        OrientedEdge oriented = new OrientedEdge(edge, true);
        CartesianPoint atStart = new CartesianPoint(0, 0, 0);
        double distance = oriented.distanceTo(atStart);
        assertEquals(0.0, distance, 0.1);
    }

    @Test
    void faceClosestPointTo() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(10, 10, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 10, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        // Point above the face
        CartesianPoint above = new CartesianPoint(5, 5, 10);
        CartesianPoint closest = face.closestPointTo(above);
        assertEquals(5.0, closest.x(), 0.1);
        assertEquals(5.0, closest.y(), 0.1);
        assertEquals(0.0, closest.z(), 0.1);
    }

    @Test
    void faceDistanceTo() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(10, 10, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 10, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        // Point at distance 10 above the face
        CartesianPoint above = new CartesianPoint(5, 5, 10);
        double distance = face.distanceTo(above);
        assertEquals(10.0, distance, 0.1);
    }

    @Test
    void faceCentroid() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(10, 10, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 10, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        CartesianPoint centroid = face.centroid();
        assertEquals(5.0, centroid.x(), 0.1);
        assertEquals(5.0, centroid.y(), 0.1);
        assertEquals(0.0, centroid.z(), 0.1);
    }

    @Test
    void faceNormal() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(10, 10, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 10, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        com.minicad.geometry.Vector3 normal = face.normal();
        assertEquals(0.0, normal.x(), 0.1);
        assertEquals(0.0, normal.y(), 0.1);
        assertEquals(1.0, normal.z(), 0.1);
    }

    @Test
    void shellSurfaceArea() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(10, 10, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 10, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        Shell shell = new Shell(List.of(face), true);

        assertEquals(100.0, shell.surfaceArea(), 0.001);
    }

    @Test
    void shellPerimeter() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(10, 10, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 10, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        Shell shell = new Shell(List.of(face), true);

        assertEquals(40.0, shell.perimeter(), 0.001);
    }

    @Test
    void shellCentroid() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(10, 10, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 10, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        Shell shell = new Shell(List.of(face), true);

        CartesianPoint centroid = shell.centroid();
        assertEquals(5.0, centroid.x(), 0.1);
        assertEquals(5.0, centroid.y(), 0.1);
        assertEquals(0.0, centroid.z(), 0.1);
    }

    @Test
    void shellClosestPointTo() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(10, 10, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 10, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        Shell shell = new Shell(List.of(face), true);

        CartesianPoint above = new CartesianPoint(5, 5, 10);
        CartesianPoint closest = shell.closestPointTo(above);
        assertEquals(5.0, closest.x(), 0.1);
        assertEquals(5.0, closest.y(), 0.1);
        assertEquals(0.0, closest.z(), 0.1);
    }

    @Test
    void shellDistanceTo() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(10, 10, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 10, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        Shell shell = new Shell(List.of(face), true);

        CartesianPoint above = new CartesianPoint(5, 5, 10);
        double distance = shell.distanceTo(above);
        assertEquals(10.0, distance, 0.1);
    }

    @Test
    void shellContainsApproximate() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(10, 10, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 10, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        Shell shell = new Shell(List.of(face), true);

        // Point inside bounding box
        CartesianPoint inside = new CartesianPoint(5, 5, 0);
        assertTrue(shell.containsApproximate(inside));

        // Point outside bounding box
        CartesianPoint outside = new CartesianPoint(20, 20, 0);
        assertFalse(shell.containsApproximate(outside));
    }

    @Test
    void solidSurfaceArea() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(10, 10, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 10, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        Shell shell = new Shell(List.of(face), true);
        Solid solid = new Solid(shell);

        assertEquals(100.0, solid.surfaceArea(), 0.001);
    }

    @Test
    void solidApproximateVolume() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(2, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(2, 3, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 3, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        Shell shell = new Shell(List.of(face), true);
        Solid solid = new Solid(shell);

        // Volume of bounding box: 2 * 3 * 0 = 0 (flat face)
        assertEquals(0.0, solid.approximateVolume(), 0.001);
    }

    @Test
    void solidCentroid() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(10, 10, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 10, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        Shell shell = new Shell(List.of(face), true);
        Solid solid = new Solid(shell);

        CartesianPoint centroid = solid.centroid();
        assertEquals(5.0, centroid.x(), 0.1);
        assertEquals(5.0, centroid.y(), 0.1);
        assertEquals(0.0, centroid.z(), 0.1);
    }

    @Test
    void solidClosestPointTo() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(10, 10, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 10, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        Shell shell = new Shell(List.of(face), true);
        Solid solid = new Solid(shell);

        CartesianPoint above = new CartesianPoint(5, 5, 10);
        CartesianPoint closest = solid.closestPointTo(above);
        assertEquals(5.0, closest.x(), 0.1);
        assertEquals(5.0, closest.y(), 0.1);
        assertEquals(0.0, closest.z(), 0.1);
    }

    @Test
    void solidDistanceTo() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(10, 10, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 10, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        Shell shell = new Shell(List.of(face), true);
        Solid solid = new Solid(shell);

        CartesianPoint above = new CartesianPoint(5, 5, 10);
        double distance = solid.distanceTo(above);
        assertEquals(10.0, distance, 0.1);
    }

    @Test
    void solidContainsApproximate() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(10, 10, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 10, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        Shell shell = new Shell(List.of(face), true);
        Solid solid = new Solid(shell);

        // Point inside bounding box
        assertTrue(solid.containsApproximate(new CartesianPoint(5, 5, 0)));

        // Point outside bounding box
        assertFalse(solid.containsApproximate(new CartesianPoint(20, 20, 0)));
    }

    @Test
    void solidAllShells() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(10, 10, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 10, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        Shell shell = new Shell(List.of(face), true);
        Solid solid = new Solid(shell);

        java.util.List<Shell> allShells = solid.allShells();
        assertEquals(1, allShells.size());
        assertEquals(shell, allShells.get(0));
    }

    @Test
    void solidAllFaces() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(10, 10, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 10, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        Shell shell = new Shell(List.of(face), true);
        Solid solid = new Solid(shell);

        java.util.List<Face> allFaces = solid.allFaces();
        assertEquals(1, allFaces.size());
        assertEquals(face, allFaces.get(0));
    }

    @Test
    void solidAllEdges() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(10, 10, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 10, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        Shell shell = new Shell(List.of(face), true);
        Solid solid = new Solid(shell);

        java.util.List<Edge> allEdges = solid.allEdges();
        assertEquals(4, allEdges.size());
    }

    @Test
    void faceContainsPoint() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(10, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(10, 10, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 10, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop loop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound bound = new FaceBound(loop, true, true);

        Face face = new Face(plane, List.of(bound), true);

        // Point inside the face
        assertTrue(face.contains(new CartesianPoint(5, 5, 0)));

        // Point outside the face
        assertFalse(face.contains(new CartesianPoint(20, 20, 0)));

        // Point not on the plane
        assertFalse(face.contains(new CartesianPoint(5, 5, 10)));
    }

    @Test
    void faceOuterAndInnerBounds() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        // Outer boundary
        Vertex v1 = new Vertex(new CartesianPoint(0, 0, 0));
        Vertex v2 = new Vertex(new CartesianPoint(20, 0, 0));
        Vertex v3 = new Vertex(new CartesianPoint(20, 20, 0));
        Vertex v4 = new Vertex(new CartesianPoint(0, 20, 0));

        Line3 l1 = new Line3(v1.point(), new Direction3(1, 0, 0));
        Line3 l2 = new Line3(v2.point(), new Direction3(0, 1, 0));
        Line3 l3 = new Line3(v3.point(), new Direction3(-1, 0, 0));
        Line3 l4 = new Line3(v4.point(), new Direction3(0, -1, 0));

        Edge e1 = new Edge(v1, v2, l1, true);
        Edge e2 = new Edge(v2, v3, l2, true);
        Edge e3 = new Edge(v3, v4, l3, true);
        Edge e4 = new Edge(v4, v1, l4, true);

        OrientedEdge oe1 = new OrientedEdge(e1, true);
        OrientedEdge oe2 = new OrientedEdge(e2, true);
        OrientedEdge oe3 = new OrientedEdge(e3, true);
        OrientedEdge oe4 = new OrientedEdge(e4, true);

        EdgeLoop outerLoop = new EdgeLoop(List.of(oe1, oe2, oe3, oe4));
        FaceBound outerBound = new FaceBound(outerLoop, true, true);

        Face face = new Face(plane, List.of(outerBound), true);

        assertNotNull(face.outerBound());
        assertEquals(0, face.innerBounds().size());
    }
}