package com.minicad.topology;

import com.minicad.common.TopologyException;
import com.minicad.geometry.BoundingBox3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Plane;

import java.util.List;

/**
 * Minimal solid wrapping a closed shell.
 *
 * @param outerShell outer closed shell
 * @param voidShells inner closed void shells
 */
public record Solid(Shell outerShell, List<Shell> voidShells) {

    /**
     * Creates a solid from a closed shell.
     */
    public Solid {
        if (outerShell == null) {
            throw new TopologyException("outerShell must not be null");
        }
        if (!outerShell.closed()) {
            throw new TopologyException("solid requires a closed shell");
        }
        if (voidShells == null) {
            throw new TopologyException("voidShells must not be null");
        }
        voidShells = List.copyOf(voidShells);
        for (Shell voidShell : voidShells) {
            if (voidShell == null) {
                throw new TopologyException("voidShells must not contain null");
            }
            if (!voidShell.closed()) {
                throw new TopologyException("solid void shells must be closed");
            }
        }
    }

    public Solid(Shell outerShell) {
        this(outerShell, List.of());
    }

    /**
     * Returns the bounding box of the solid.
     * This is the bounding box of the outer shell.
     *
     * @return bounding box enclosing the solid
     */
    public BoundingBox3 boundingBox() {
        return outerShell.boundingBox();
    }

    /**
     * Returns the count of faces in the solid.
     *
     * @return number of faces in outer shell
     */
    public int faceCount() {
        return outerShell.faceCount();
    }

    /**
     * Returns the count of shells in the solid.
     *
     * @return number of shells (outer + voids)
     */
    public int shellCount() {
        return 1 + voidShells.size();
    }

    /**
     * Returns the count of edges in the solid.
     *
     * @return number of edges in outer shell
     */
    public int edgeCount() {
        return outerShell.edgeCount();
    }

    /**
     * Returns all unique vertices in the solid.
     *
     * @return list of unique vertices
     */
    public List<Vertex> vertices() {
        return outerShell.vertices();
    }

    /**
     * Returns the total surface area of the solid.
     * This sums the areas of all planar faces.
     *
     * @return total surface area (sum of all face areas)
     */
    public double surfaceArea() {
        double totalArea = 0.0;
        totalArea += outerShell.surfaceArea();
        for (Shell voidShell : voidShells) {
            totalArea += voidShell.surfaceArea();
        }
        return totalArea;
    }

    /**
     * Returns an approximate volume based on bounding box.
     * For planar-faced solids with known geometry, this is a rough estimate.
     *
     * @return approximate volume from bounding box dimensions
     */
    public double approximateVolume() {
        BoundingBox3 box = boundingBox();
        return box.volume();
    }

    /**
     * Returns the centroid of the solid.
     * Computed from the outer shell centroid.
     *
     * @return centroid point
     */
    public CartesianPoint centroid() {
        return outerShell.centroid();
    }

    /**
     * Returns the closest point on the solid to a given point.
     *
     * @param point target point
     * @return closest point on the solid surface
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        return outerShell.closestPointTo(point);
    }

    /**
     * Returns the distance from a point to the solid surface.
     *
     * @param point target point
     * @return distance to the solid surface
     */
    public double distanceTo(CartesianPoint point) {
        return point.distanceTo(closestPointTo(point));
    }

    /**
     * Checks if a point is within the solid's bounding box.
     * This is an approximate containment check.
     *
     * @param point point to check
     * @return true if point is within the solid's bounding box
     */
    public boolean containsApproximate(CartesianPoint point) {
        return boundingBox().contains(point);
    }

    /**
     * Returns all shells including outer and voids.
     *
     * @return list of all shells
     */
    public List<Shell> allShells() {
        List<Shell> allShells = new java.util.ArrayList<>();
        allShells.add(outerShell);
        allShells.addAll(voidShells);
        return List.copyOf(allShells);
    }

    /**
     * Returns all unique faces across all shells.
     *
     * @return list of all faces
     */
    public List<Face> allFaces() {
        List<Face> allFaces = new java.util.ArrayList<>();
        for (Shell shell : allShells()) {
            allFaces.addAll(shell.faces());
        }
        return List.copyOf(allFaces);
    }

    /**
     * Returns all unique edges across all shells.
     *
     * @return list of all edges
     */
    public List<Edge> allEdges() {
        List<Edge> allEdges = new java.util.ArrayList<>();
        for (Shell shell : allShells()) {
            allEdges.addAll(shell.edges());
        }
        return List.copyOf(allEdges);
    }
}
