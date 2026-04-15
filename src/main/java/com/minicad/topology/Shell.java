package com.minicad.topology;

import com.minicad.common.TopologyException;
import com.minicad.geometry.BoundingBox3;

import java.util.List;

/**
 * Minimal shell made of planar faces.
 *
 * @param faces faces of the shell
 * @param closed whether the shell is declared closed
 */
public record Shell(List<Face> faces, boolean closed) {

    /**
     * Creates a shell.
     */
    public Shell {
        if (faces == null) {
            throw new TopologyException("faces must not be null");
        }
        if (faces.isEmpty()) {
            throw new TopologyException("faces must not be empty");
        }
        faces = List.copyOf(faces);
        for (Face face : faces) {
            if (face == null) {
                throw new TopologyException("faces must not contain null");
            }
        }
    }

    /**
     * Returns the bounding box of the shell.
     *
     * @return bounding box enclosing all faces
     */
    public BoundingBox3 boundingBox() {
        BoundingBox3 box = BoundingBox3.empty();
        for (Face face : faces) {
            box = box.union(face.boundingBox());
        }
        return box;
    }

    /**
     * Returns the count of faces in the shell.
     *
     * @return number of faces
     */
    public int faceCount() {
        return faces.size();
    }

    /**
     * Returns the total count of edges in the shell.
     *
     * @return number of edges
     */
    public int edgeCount() {
        int count = 0;
        for (Face face : faces) {
            count += face.edgeCount();
        }
        return count;
    }

    /**
     * Returns all unique vertices in the shell.
     *
     * @return list of unique vertices
     */
    public List<Vertex> vertices() {
        java.util.Set<Vertex> vertexSet = new java.util.LinkedHashSet<>();
        for (Face face : faces) {
            for (Vertex vertex : face.vertices()) {
                vertexSet.add(vertex);
            }
        }
        return List.copyOf(vertexSet);
    }

    /**
     * Returns the total surface area of the shell.
     * This sums the areas of all planar faces.
     *
     * @return total surface area (sum of all face areas)
     */
    public double surfaceArea() {
        double totalArea = 0.0;
        for (Face face : faces) {
            totalArea += face.area();
        }
        return totalArea;
    }

    /**
     * Returns the total perimeter of all face boundaries in the shell.
     *
     * @return total perimeter length
     */
    public double perimeter() {
        double totalPerimeter = 0.0;
        for (Face face : faces) {
            totalPerimeter += face.perimeter();
        }
        return totalPerimeter;
    }

    /**
     * Returns the centroid of the shell.
     * Computed as the average of all face centroids.
     *
     * @return centroid point
     */
    public com.minicad.geometry.CartesianPoint centroid() {
        if (faces.isEmpty()) {
            return new com.minicad.geometry.CartesianPoint(0, 0, 0);
        }
        double sumX = 0.0;
        double sumY = 0.0;
        double sumZ = 0.0;
        for (Face face : faces) {
            com.minicad.geometry.CartesianPoint c = face.centroid();
            sumX += c.x();
            sumY += c.y();
            sumZ += c.z();
        }
        return new com.minicad.geometry.CartesianPoint(sumX / faces.size(), sumY / faces.size(), sumZ / faces.size());
    }

    /**
     * Returns the closest point on the shell to a given point.
     *
     * @param point target point
     * @return closest point on the shell
     */
    public com.minicad.geometry.CartesianPoint closestPointTo(com.minicad.geometry.CartesianPoint point) {
        com.minicad.geometry.CartesianPoint closest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Face face : faces) {
            com.minicad.geometry.CartesianPoint faceClosest = face.closestPointTo(point);
            double distance = point.distanceTo(faceClosest);
            if (distance < minDistance) {
                minDistance = distance;
                closest = faceClosest;
            }
        }
        return closest != null ? closest : centroid();
    }

    /**
     * Returns the distance from a point to the shell.
     *
     * @param point target point
     * @return distance to the shell
     */
    public double distanceTo(com.minicad.geometry.CartesianPoint point) {
        return point.distanceTo(closestPointTo(point));
    }

    /**
     * Checks if a point is within the shell's bounding box.
     *
     * @param point point to check
     * @return true if point is within the shell's bounding box
     */
    public boolean containsApproximate(com.minicad.geometry.CartesianPoint point) {
        return boundingBox().contains(point);
    }

    /**
     * Returns all unique edges in the shell.
     *
     * @return list of unique edges
     */
    public java.util.List<Edge> edges() {
        java.util.Set<Edge> edgeSet = new java.util.LinkedHashSet<>();
        for (Face face : faces) {
            for (FaceBound bound : face.bounds()) {
                if (bound.loop() instanceof EdgeLoop edgeLoop) {
                    for (OrientedEdge orientedEdge : edgeLoop.edges()) {
                        edgeSet.add(orientedEdge.edge());
                    }
                }
            }
        }
        return java.util.List.copyOf(edgeSet);
    }
}
