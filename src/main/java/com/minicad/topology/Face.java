package com.minicad.topology;

import com.minicad.common.TopologyException;
import com.minicad.geometry.BoundingBox3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Plane;
import com.minicad.geometry.SurfaceGeometry;

import java.util.List;

/**
 * Minimal face with optional planar validation.
 *
 * @param surface supporting surface
 * @param bounds face boundaries
 * @param sameSense whether the face orientation matches the surface normal
 */
public record Face(SurfaceGeometry surface, List<FaceBound> bounds, boolean sameSense) {

    /**
     * Creates a face and validates that planar loop vertices lie on the plane.
     */
    public Face {
        if (surface == null) {
            throw new TopologyException("surface must not be null");
        }
        if (bounds == null) {
            throw new TopologyException("bounds must not be null");
        }
        if (bounds.isEmpty()) {
            throw new TopologyException("bounds must not be empty");
        }
        bounds = List.copyOf(bounds);

        boolean hasOuter = false;
        for (FaceBound bound : bounds) {
            if (bound == null) {
                throw new TopologyException("bounds must not contain null");
            }
            if (bound.outer()) {
                hasOuter = true;
            }
            if (surface instanceof Plane plane && bound.loop() instanceof EdgeLoop edgeLoop) {
                for (OrientedEdge edge : edgeLoop.edges()) {
                    if (!plane.contains(edge.startVertex().point())) {
                        throw new TopologyException("all face vertices must lie on the plane");
                    }
                    if (!plane.contains(edge.endVertex().point())) {
                        throw new TopologyException("all face vertices must lie on the plane");
                    }
                }
            } else if (surface instanceof Plane plane && bound.loop() instanceof VertexLoop vertexLoop) {
                if (!plane.contains(vertexLoop.vertex().point())) {
                    throw new TopologyException("all face vertices must lie on the plane");
                }
            } else if (surface instanceof Plane plane && bound.loop() instanceof PolyLoop polyLoop) {
                for (var point : polyLoop.points()) {
                    if (!plane.contains(point)) {
                        throw new TopologyException("all face vertices must lie on the plane");
                    }
                }
            }
        }
        if (!hasOuter) {
            throw new TopologyException("face must contain an outer bound");
        }
    }

    /**
     * Returns the bounding box of the face.
     * The bounding box is computed from all vertices in the face bounds.
     *
     * @return bounding box enclosing the face
     */
    public BoundingBox3 boundingBox() {
        BoundingBox3 box = BoundingBox3.empty();
        for (FaceBound bound : bounds) {
            box = box.union(getBoundBoundingBox(bound));
        }
        return box;
    }

    /**
     * Gets bounding box from a face bound.
     */
    private static BoundingBox3 getBoundBoundingBox(FaceBound bound) {
        BoundingBox3 box = BoundingBox3.empty();
        if (bound.loop() instanceof EdgeLoop edgeLoop) {
            for (OrientedEdge edge : edgeLoop.edges()) {
                box = box.union(edge.edge().boundingBox());
            }
        } else if (bound.loop() instanceof VertexLoop vertexLoop) {
            box = box.union(vertexLoop.vertex().point());
        } else if (bound.loop() instanceof PolyLoop polyLoop) {
            for (CartesianPoint point : polyLoop.points()) {
                box = box.union(point);
            }
        }
        return box;
    }

    /**
     * Returns the count of edges in the face.
     *
     * @return number of edges
     */
    public int edgeCount() {
        int count = 0;
        for (FaceBound bound : bounds) {
            if (bound.loop() instanceof EdgeLoop edgeLoop) {
                count += edgeLoop.edges().size();
            }
        }
        return count;
    }

    /**
     * Returns all vertices in the face.
     *
     * @return list of unique vertices
     */
    public List<Vertex> vertices() {
        java.util.Set<Vertex> vertexSet = new java.util.LinkedHashSet<>();
        for (FaceBound bound : bounds) {
            if (bound.loop() instanceof EdgeLoop edgeLoop) {
                for (OrientedEdge edge : edgeLoop.edges()) {
                    vertexSet.add(edge.edge().start());
                    vertexSet.add(edge.edge().end());
                }
            } else if (bound.loop() instanceof VertexLoop vertexLoop) {
                vertexSet.add(vertexLoop.vertex());
            }
        }
        return List.copyOf(vertexSet);
    }

    /**
     * Returns the total perimeter length of the face boundaries.
     *
     * @return total perimeter length
     */
    public double perimeter() {
        double totalLength = 0.0;
        for (FaceBound bound : bounds) {
            if (bound.loop() instanceof EdgeLoop edgeLoop) {
                totalLength += edgeLoop.perimeter();
            } else if (bound.loop() instanceof PolyLoop polyLoop) {
                totalLength += polyLoop.perimeter();
            }
        }
        return totalLength;
    }

    /**
     * Returns the count of bounds in the face.
     *
     * @return number of bounds (outer + inner)
     */
    public int boundCount() {
        return bounds.size();
    }

    /**
     * Computes the area of a planar face using the shoelace formula.
     * Only valid for planar faces; returns 0 for non-planar surfaces.
     *
     * @return area of the planar face, or 0 if surface is not a plane
     */
    public double area() {
        if (!(surface instanceof Plane)) {
            return 0.0;  // Area calculation only valid for planar faces
        }

        double totalArea = 0.0;
        for (FaceBound bound : bounds) {
            if (bound.outer() && bound.loop() instanceof EdgeLoop edgeLoop) {
                totalArea += computeLoopArea(edgeLoop);
            } else if (bound.outer() && bound.loop() instanceof PolyLoop polyLoop) {
                totalArea += computePolyLoopArea(polyLoop);
            }
            // Inner bounds subtract area
            if (!bound.outer() && bound.loop() instanceof EdgeLoop edgeLoop) {
                totalArea -= computeLoopArea(edgeLoop);
            } else if (!bound.outer() && bound.loop() instanceof PolyLoop polyLoop) {
                totalArea -= computePolyLoopArea(polyLoop);
            }
        }
        return Math.abs(totalArea);
    }

    private static double computeLoopArea(EdgeLoop loop) {
        List<Vertex> vertices = loop.vertices();
        if (vertices.size() < 3) {
            return 0.0;
        }
        return shoelaceArea(vertices);
    }

    private static double computePolyLoopArea(PolyLoop polyLoop) {
        List<CartesianPoint> points = polyLoop.points();
        if (points.size() < 3) {
            return 0.0;
        }
        List<Vertex> vertices = new java.util.ArrayList<>();
        for (CartesianPoint point : points) {
            vertices.add(new Vertex(point));
        }
        return shoelaceArea(vertices);
    }

    private static double shoelaceArea(List<Vertex> vertices) {
        int n = vertices.size();
        if (n < 3) {
            return 0.0;
        }
        // Project onto XY plane (assuming planar face lies in Z=const plane)
        double sum1 = 0.0;
        double sum2 = 0.0;
        for (int i = 0; i < n; i++) {
            CartesianPoint p1 = vertices.get(i).point();
            CartesianPoint p2 = vertices.get((i + 1) % n).point();
            sum1 += p1.x() * p2.y();
            sum2 += p2.x() * p1.y();
        }
        return Math.abs(sum1 - sum2) / 2.0;
    }

    /**
     * Returns the centroid of a planar face.
     * Only valid for planar faces; returns the average of vertices for non-planar.
     *
     * @return centroid point
     */
    public CartesianPoint centroid() {
        List<Vertex> allVertices = vertices();
        if (allVertices.isEmpty()) {
            return surface instanceof Plane plane ? plane.origin() : new CartesianPoint(0, 0, 0);
        }
        double sumX = 0.0;
        double sumY = 0.0;
        double sumZ = 0.0;
        for (Vertex vertex : allVertices) {
            CartesianPoint p = vertex.point();
            sumX += p.x();
            sumY += p.y();
            sumZ += p.z();
        }
        return new CartesianPoint(sumX / allVertices.size(), sumY / allVertices.size(), sumZ / allVertices.size());
    }

    /**
     * Returns the normal vector of the face.
     * For planar faces, this is the plane normal.
     *
     * @return face normal vector
     */
    public com.minicad.geometry.Vector3 normal() {
        if (surface instanceof Plane plane) {
            return sameSense ? plane.normal().asVector() : plane.normal().reverse().asVector();
        }
        // For non-planar surfaces, compute from centroid position
        CartesianPoint center = centroid();
        return surface.normalAt(0.5, 0.5);
    }

    /**
     * Returns the outer boundary loop of the face.
     *
     * @return the outer FaceBound, or null if none found
     */
    public FaceBound outerBound() {
        for (FaceBound bound : bounds) {
            if (bound.outer()) {
                return bound;
            }
        }
        return null;
    }

    /**
     * Returns the inner boundary loops of the face.
     *
     * @return list of inner FaceBounds
     */
    public List<FaceBound> innerBounds() {
        List<FaceBound> innerBounds = new java.util.ArrayList<>();
        for (FaceBound bound : bounds) {
            if (!bound.outer()) {
                innerBounds.add(bound);
            }
        }
        return List.copyOf(innerBounds);
    }

    /**
     * Returns whether the face contains a given point.
     * For planar faces, this checks if the point is within the face boundaries.
     *
     * @param point point to check
     * @return true if the point is within the face
     */
    public boolean contains(CartesianPoint point) {
        if (surface instanceof Plane plane) {
            // First check if point is on the plane
            if (!plane.contains(point)) {
                return false;
            }
            // Check if point is within the outer boundary
            FaceBound outer = outerBound();
            if (outer == null) {
                return false;
            }
            // Use point-in-polygon test for planar faces
            return isPointInBoundary(point, outer);
        }
        // For non-planar surfaces, use approximate bounding box test
        return boundingBox().contains(point);
    }

    /**
     * Checks if a point is within a face boundary using point-in-polygon test.
     */
    private boolean isPointInBoundary(CartesianPoint point, FaceBound bound) {
        if (bound.loop() instanceof EdgeLoop edgeLoop) {
            List<Vertex> loopVertices = edgeLoop.vertices();
            return pointInPolygon(point, loopVertices);
        } else if (bound.loop() instanceof PolyLoop polyLoop) {
            List<CartesianPoint> loopPoints = polyLoop.points();
            List<Vertex> loopVertices = new java.util.ArrayList<>();
            for (CartesianPoint p : loopPoints) {
                loopVertices.add(new Vertex(p));
            }
            return pointInPolygon(point, loopVertices);
        }
        return false;
    }

    /**
     * Point-in-polygon test using ray casting algorithm.
     */
    private boolean pointInPolygon(CartesianPoint point, List<Vertex> polygon) {
        int n = polygon.size();
        if (n < 3) {
            return false;
        }
        // Project to XY plane for 2D test
        double px = point.x();
        double py = point.y();
        int inside = 0;
        for (int i = 0, j = n - 1; i < n; j = i++) {
            CartesianPoint pi = polygon.get(i).point();
            CartesianPoint pj = polygon.get(j).point();
            double xi = pi.x();
            double yi = pi.y();
            double xj = pj.x();
            double yj = pj.y();
            if (((yi > py) != (yj > py)) && (px < (xj - xi) * (py - yi) / (yj - yi) + xi)) {
                inside = 1 - inside;
            }
        }
        return inside == 1;
    }

    /**
     * Returns the closest point on the face to a given point.
     *
     * @param point target point
     * @return closest point on the face
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        if (surface instanceof Plane plane) {
            return plane.project(point);
        }
        // For non-planar surfaces, find closest among boundary vertices
        CartesianPoint closest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Vertex vertex : vertices()) {
            double distance = point.distanceTo(vertex.point());
            if (distance < minDistance) {
                minDistance = distance;
                closest = vertex.point();
            }
        }
        return closest != null ? closest : centroid();
    }

    /**
     * Returns the distance from a point to the face.
     *
     * @param point target point
     * @return distance to the face
     */
    public double distanceTo(CartesianPoint point) {
        return point.distanceTo(closestPointTo(point));
    }
}
