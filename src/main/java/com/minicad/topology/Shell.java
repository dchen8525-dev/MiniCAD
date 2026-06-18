package com.minicad.topology;

import com.minicad.common.TopologyException;
import com.minicad.geometry.BoundingBox3;
import com.minicad.geometry.CartesianPoint;

import java.util.List;
import java.util.Objects;

/**
 * Minimal shell made of planar faces.
 *
 * @param faces faces of the shell
 * @param closed whether the shell is declared closed
 */
/**
 * Minimal shell made of planar faces.
 *
 * @param faces faces of the shell
 * @param closed whether the shell is declared closed
 */
public final class Shell {
    private final List<Face> faces;
    private final boolean closed;

    public Shell(List<Face> faces, boolean closed) {
        this.faces = faces == null ? null : java.util.List.copyOf(faces);
        this.closed = closed;
    }

    public List<Face> getFaces() {
        return faces;
    }

    public boolean isClosed() {
        return closed;
    }

    // Record-style accessors
    public List<Face> faces() { return getFaces(); }
    public boolean closed() { return isClosed(); }

    /**
     * Returns the bounding box of this shell.
     *
     * @return bounding box enclosing all faces
     */
    public BoundingBox3 boundingBox() {
        if (faces == null || faces.isEmpty()) {
            return BoundingBox3.empty();
        }
        BoundingBox3 box = BoundingBox3.empty();
        for (Face face : faces) {
            if (face != null) {
                BoundingBox3 faceBox = face.boundingBox();
                if (faceBox != null) {
                    box = box.union(faceBox);
                }
            }
        }
        return box;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shell that = (Shell) o;
        return Objects.equals(faces, that.faces) && closed == that.closed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(faces, closed);
    }

    @Override
    public String toString() {
        return "Shell{" + "faces=" + faces + "closed=" + closed + "}";
    }

    /**
     * Returns the number of faces in this shell.
     *
     * @return face count
     */
    public int faceCount() {
        return faces != null ? faces.size() : 0;
    }

    /**
     * Returns the total edge count of this shell.
     *
     * @return total edge count
     */
    public int edgeCount() {
        if (faces == null) {
            return 0;
        }
        int count = 0;
        for (Face face : faces) {
            if (face != null) {
                count += face.edgeCount();
            }
        }
        return count;
    }

    /**
     * Returns all vertices of this shell.
     *
     * @return list of vertices
     */
    public java.util.List<Vertex> vertices() {
        java.util.List<Vertex> result = new java.util.ArrayList<>();
        if (faces == null) {
            return result;
        }
        for (Face face : faces) {
            if (face != null) {
                result.addAll(face.vertices());
            }
        }
        return result;
    }

    /**
     * Returns the total surface area of this shell.
     *
     * @return total surface area
     */
    public double surfaceArea() {
        if (faces == null) {
            return 0.0;
        }
        double total = 0.0;
        for (Face face : faces) {
            if (face != null) {
                total += face.area();
            }
        }
        return total;
    }

    /**
     * Returns the total perimeter of this shell.
     *
     * @return total perimeter length
     */
    public double perimeter() {
        if (faces == null) {
            return 0.0;
        }
        double total = 0.0;
        for (Face face : faces) {
            if (face != null) {
                total += face.perimeter();
            }
        }
        return total;
    }

    /**
     * Returns the centroid of this shell.
     *
     * @return centroid point, or null if shell has no faces or empty bounding box
     */
    public CartesianPoint centroid() {
        BoundingBox3 box = boundingBox();
        if (box == null || box.isEmpty()) {
            return null;
        }
        return box.center();
    }

    /**
     * Returns the closest point on this shell to a given point.
     *
     * @param point the target point
     * @return closest point
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        if (faces == null || faces.isEmpty()) {
            return null;
        }
        CartesianPoint closest = null;
        double minDist = Double.MAX_VALUE;
        for (Face face : faces) {
            if (face != null) {
                CartesianPoint fp = face.closestPointTo(point);
                if (fp != null) {
                    double dist = point.distanceTo(fp);
                    if (dist < minDist) {
                        minDist = dist;
                        closest = fp;
                    }
                }
            }
        }
        return closest;
    }

    /**
     * Returns the distance from this shell to a given point.
     *
     * @param point the target point
     * @return minimum distance
     */
    public double distanceTo(CartesianPoint point) {
        if (point == null) {
            return Double.MAX_VALUE;
        }
        CartesianPoint closest = closestPointTo(point);
        return closest != null ? point.distanceTo(closest) : Double.MAX_VALUE;
    }

    /**
     * Checks if a point is approximately within this shell (bounding box check).
     *
     * @param point the point to check
     * @return true if within bounding box
     */
    public boolean containsApproximate(CartesianPoint point) {
        if (point == null) {
            return false;
        }
        BoundingBox3 box = boundingBox();
        return box.containsPoint(point);
    }
}
