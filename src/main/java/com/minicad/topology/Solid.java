package com.minicad.topology;

import com.minicad.common.TopologyException;
import com.minicad.geometry.BoundingBox3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Plane;

import java.util.List;
import java.util.Objects;

/**
 * Minimal solid wrapping a closed shell.
 *
 * @param outerShell outer closed shell
 * @param voidShells inner closed void shells
 */
/**
 * Minimal solid wrapping a closed shell.
 *
 * @param outerShell outer closed shell
 * @param voidShells inner closed void shells
 */
public final class Solid {
    private final Shell outerShell;
    private final List<Shell> voidShells;

    public Solid(Shell outerShell, List<Shell> voidShells) {
        if (outerShell == null || !outerShell.isClosed()) {
            throw new TopologyException("solid requires a closed shell");
        }
        if (voidShells != null && voidShells.stream().anyMatch(shell -> shell == null || !shell.isClosed())) {
            throw new TopologyException("solid voids require closed shells");
        }
        this.outerShell = outerShell;
        this.voidShells = voidShells == null ? null : java.util.List.copyOf(voidShells);
    }

    public Solid(Shell outerShell) {
        this(outerShell, List.of());
    }

    public Shell getOuterShell() {
        return outerShell;
    }

    public List<Shell> getVoidShells() {
        return voidShells;
    }

    // Record-style accessors
    public Shell outerShell() { return getOuterShell(); }
    public List<Shell> voidShells() { return getVoidShells(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solid that = (Solid) o;
        return Objects.equals(outerShell, that.outerShell) && Objects.equals(voidShells, that.voidShells);
    }

    @Override
    public int hashCode() {
        return Objects.hash(outerShell, voidShells);
    }

    @Override
    public String toString() {
        return "Solid{" + "outerShell=" + outerShell + "voidShells=" + voidShells + "}";
    }

    /**
     * Returns the bounding box of this solid.
     *
     * @return bounding box of outer shell
     */
    public BoundingBox3 boundingBox() {
        return outerShell != null ? outerShell.boundingBox() : BoundingBox3.empty();
    }

    /**
     * Returns the number of faces in this solid.
     *
     * @return total face count
     */
    public int faceCount() {
        return outerShell != null ? outerShell.faceCount() : 0;
    }

    /**
     * Returns the number of shells in this solid.
     *
     * @return shell count (outer + void shells)
     */
    public int shellCount() {
        int count = outerShell != null ? 1 : 0;
        if (voidShells != null) {
            count += voidShells.size();
        }
        return count;
    }

    /**
     * Returns the total surface area of this solid.
     *
     * @return total surface area
     */
    public double surfaceArea() {
        return outerShell != null ? outerShell.surfaceArea() : 0.0;
    }

    /**
     * Returns the approximate volume of this solid (bounding box volume).
     *
     * @return approximate volume
     */
    public double approximateVolume() {
        BoundingBox3 box = boundingBox();
        if (box.isEmpty()) {
            return 0.0;
        }
        double dx = box.getMaxX() - box.getMinX();
        double dy = box.getMaxY() - box.getMinY();
        double dz = box.getMaxZ() - box.getMinZ();
        return dx * dy * dz;
    }

    /**
     * Returns the centroid of this solid.
     *
     * @return centroid point
     */
    public CartesianPoint centroid() {
        return outerShell != null ? outerShell.centroid() : null;
    }

    /**
     * Returns the closest point on this solid to a given point.
     *
     * @param point the target point
     * @return closest point
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        return outerShell != null ? outerShell.closestPointTo(point) : null;
    }

    /**
     * Returns the distance from this solid to a given point.
     *
     * @param point the target point
     * @return minimum distance
     */
    public double distanceTo(CartesianPoint point) {
        return outerShell != null ? outerShell.distanceTo(point) : Double.MAX_VALUE;
    }

    /**
     * Checks if a point is approximately within this solid.
     *
     * @param point the point to check
     * @return true if within bounding box
     */
    public boolean containsApproximate(CartesianPoint point) {
        return outerShell != null && outerShell.containsApproximate(point);
    }

    /**
     * Returns all shells of this solid.
     *
     * @return list of all shells
     */
    public java.util.List<Shell> allShells() {
        java.util.List<Shell> result = new java.util.ArrayList<>();
        if (outerShell != null) {
            result.add(outerShell);
        }
        if (voidShells != null) {
            result.addAll(voidShells);
        }
        return result;
    }

    /**
     * Returns all faces of this solid.
     *
     * @return list of all faces
     */
    public java.util.List<Face> allFaces() {
        java.util.List<Face> result = new java.util.ArrayList<>();
        for (Shell shell : allShells()) {
            if (shell != null && shell.getFaces() != null) {
                result.addAll(shell.getFaces());
            }
        }
        return result;
    }

    /**
     * Returns all edges of this solid.
     *
     * @return list of all edges
     */
    public java.util.List<Edge> allEdges() {
        java.util.List<Edge> result = new java.util.ArrayList<>();
        for (Face face : allFaces()) {
            if (face != null) {
                for (com.minicad.topology.FaceBound bound : face.getBounds()) {
                    if (bound != null && bound.getLoop() instanceof com.minicad.topology.EdgeLoop) {
                        com.minicad.topology.EdgeLoop loop = (com.minicad.topology.EdgeLoop) bound.getLoop();
                        for (com.minicad.topology.OrientedEdge oe : loop.edges()) {
                            if (oe != null && oe.getEdge() != null) {
                                result.add(oe.getEdge());
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
