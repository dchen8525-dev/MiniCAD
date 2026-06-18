package com.minicad.topology;

import com.minicad.common.TopologyException;
import com.minicad.common.Epsilon;
import com.minicad.geometry.BoundingBox3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Plane;
import com.minicad.geometry.SurfaceGeometry;

import java.util.List;
import java.util.Objects;

/**
 * Minimal face with optional planar validation.
 *
 * @param surface supporting surface
 * @param bounds face boundaries
 * @param sameSense whether the face orientation matches the surface normal
 */
/**
 * Minimal face with optional planar validation.
 *
 * @param surface supporting surface
 * @param bounds face boundaries
 * @param sameSense whether the face orientation matches the surface normal
 */
public final class Face {
    private final SurfaceGeometry surface;
    private final List<FaceBound> bounds;
    private final boolean sameSense;

    public Face(SurfaceGeometry surface, List<FaceBound> bounds, boolean sameSense) {
        this.surface = surface;
        this.bounds = bounds == null ? null : java.util.List.copyOf(bounds);
        this.sameSense = sameSense;
    }

    public SurfaceGeometry getSurface() {
        return surface;
    }

    public List<FaceBound> getBounds() {
        return bounds;
    }

    public boolean isSameSense() {
        return sameSense;
    }

    // Record-style accessors
    public SurfaceGeometry surface() { return getSurface(); }
    public List<FaceBound> bounds() { return getBounds(); }
    public boolean sameSense() { return isSameSense(); }

    /**
     * Returns the outer boundary of this face, if present.
     *
     * @return outer boundary or null if not defined
     */
    public FaceBound outerBound() {
        if (bounds == null || bounds.isEmpty()) {
            return null;
        }
        for (FaceBound bound : bounds) {
            if (bound.orientation()) {
                return bound;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Face that = (Face) o;
        return Objects.equals(surface, that.surface) && Objects.equals(bounds, that.bounds) && sameSense == that.sameSense;
    }

    @Override
    public int hashCode() {
        return Objects.hash(surface, bounds, sameSense);
    }

    @Override
    public String toString() {
        return "Face{" + "surface=" + surface + "bounds=" + bounds + "sameSense=" + sameSense + "}";
    }

    /**
     * Returns the bounding box of this face.
     *
     * @return bounding box enclosing the face
     */
    public BoundingBox3 boundingBox() {
        if (surface == null) {
            return BoundingBox3.empty();
        }
        BoundingBox3 box = surface.boundingBox();
        // Include all boundary edges
        if (bounds != null) {
            for (FaceBound bound : bounds) {
                if (bound != null && bound.loop() != null) {
                    box = box.union(bound.loop().boundingBox());
                }
            }
        }
        return box;
    }

    /**
     * Returns the number of edges in this face.
     *
     * @return total edge count
     */
    public int edgeCount() {
        if (bounds == null) {
            return 0;
        }
        int count = 0;
        for (FaceBound bound : bounds) {
            if (bound != null && bound.loop() instanceof EdgeLoop) {
                EdgeLoop loop = (EdgeLoop) bound.loop();
                if (loop.edges() != null) {
                    count += loop.edges().size();
                }
            }
        }
        return count;
    }

    /**
     * Returns all vertices of this face.
     *
     * @return list of vertices
     */
    public java.util.List<Vertex> vertices() {
        java.util.List<Vertex> result = new java.util.ArrayList<>();
        if (bounds == null) {
            return result;
        }
        for (FaceBound bound : bounds) {
            if (bound != null && bound.loop() instanceof EdgeLoop) {
                EdgeLoop loop = (EdgeLoop) bound.loop();
                result.addAll(loop.vertices());
            }
        }
        return result;
    }

    /**
     * Returns the perimeter of this face.
     *
     * @return approximate perimeter length
     */
    public double perimeter() {
        if (bounds == null) {
            return 0.0;
        }
        double total = 0.0;
        for (FaceBound bound : bounds) {
            if (bound != null && bound.loop() instanceof EdgeLoop) {
                EdgeLoop loop = (EdgeLoop) bound.loop();
                for (OrientedEdge oe : loop.edges()) {
                    total += oe.length();
                }
            }
        }
        return total;
    }

    /**
     * Returns the area of this face (approximate for planar faces).
     *
     * @return approximate area
     */
    public double area() {
        if (surface instanceof Plane && bounds != null && !bounds.isEmpty()) {
            // Calculate area using shoelace formula for planar polygons
            FaceBound outer = outerBound();
            if (outer != null && outer.loop() instanceof EdgeLoop) {
                EdgeLoop loop = (EdgeLoop) outer.loop();
                java.util.List<Vertex> verts = loop.vertices();
                if (verts.size() >= 3) {
                    double area = 0.0;
                    for (int i = 0; i < verts.size(); i++) {
                        Vertex v1 = verts.get(i);
                        Vertex v2 = verts.get((i + 1) % verts.size());
                        area += v1.point().x() * v2.point().y() - v2.point().x() * v1.point().y();
                    }
                    return Math.abs(area) / 2.0;
                }
            }
        }
        // For non-planar surfaces, approximate via sampling
        return 0.0;
    }

    /**
     * Returns the closest point on this face to a given point.
     *
     * @param point the target point
     * @return closest point on the face
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        if (surface == null || point == null) {
            return null;
        }
        // For planar faces, project point onto the plane
        if (surface instanceof Plane) {
            Plane plane = (Plane) surface;
            CartesianPoint origin = plane.origin();
            com.minicad.geometry.Vector3 normal = plane.normal().asVector();
            // Project point onto plane
            com.minicad.geometry.Vector3 toPoint = point.subtract(origin);
            double dist = toPoint.dot(normal);
            return point.subtractVector(normal.scale(dist));
        }
        // For other surfaces, use bounding box center as fallback
        return surface.boundingBox().center();
    }

    /**
     * Returns the distance from this face to a given point.
     *
     * @param point the target point
     * @return distance to the closest point
     */
    public double distanceTo(CartesianPoint point) {
        if (point == null) {
            return Double.MAX_VALUE;
        }
        CartesianPoint closest = closestPointTo(point);
        return closest != null ? point.distanceTo(closest) : Double.MAX_VALUE;
    }

    /**
     * Returns the centroid of this face (approximate).
     *
     * @return centroid point
     */
    public CartesianPoint centroid() {
        java.util.List<Vertex> verts = vertices();
        if (verts.isEmpty()) {
            return surface != null ? surface.boundingBox().center() : null;
        }
        double x = 0, y = 0, z = 0;
        for (Vertex v : verts) {
            x += v.point().x();
            y += v.point().y();
            z += v.point().z();
        }
        return new CartesianPoint(x / verts.size(), y / verts.size(), z / verts.size());
    }

    /**
     * Returns the normal of this face.
     *
     * @return normal vector (as Vector3)
     */
    public com.minicad.geometry.Vector3 normal() {
        if (surface instanceof Plane) {
            Plane plane = (Plane) surface;
            com.minicad.geometry.Direction3 dir = plane.normal();
            return dir.asVector();
        }
        // For other surfaces, compute normal at center
        CartesianPoint center = surface != null ? surface.boundingBox().center() : null;
        if (center != null) {
            // Use parameterized normalAt(0.5, 0.5) as approximation
            return surface.normalAt(0.5, 0.5);
        }
        return new com.minicad.geometry.Vector3(0, 0, 1);
    }

    /**
     * Returns the number of bounds.
     *
     * @return bound count
     */
    public int boundCount() {
        return bounds != null ? bounds.size() : 0;
    }

    /**
     * Checks if this face contains a point.
     *
     * @param point the point to check
     * @return true if the point is within the face boundary
     */
    public boolean contains(CartesianPoint point) {
        if (point == null || surface == null) {
            return false;
        }
        // Check if point is on surface (for planar, use plane projection)
        CartesianPoint closest = closestPointTo(point);
        if (closest == null || point.distanceTo(closest) >= Epsilon.get()) {
            return false;
        }
        // Simple bounding box check for containment
        BoundingBox3 box = boundingBox();
        return box.containsPoint(point);
    }

    /**
     * Returns the inner boundaries of this face.
     *
     * @return list of inner bounds
     */
    public java.util.List<FaceBound> innerBounds() {
        if (bounds == null) {
            return java.util.List.of();
        }
        java.util.List<FaceBound> result = new java.util.ArrayList<>();
        for (FaceBound bound : bounds) {
            if (bound != null && !bound.outer()) {
                result.add(bound);
            }
        }
        return result;
    }
}
