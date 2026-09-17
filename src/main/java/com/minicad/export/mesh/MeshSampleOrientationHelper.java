package com.minicad.export.mesh;

import com.minicad.geometry.CartesianPoint;
import com.minicad.topology.OrientedEdge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Orients a sampled edge polyline so that it runs from the edge's start vertex
 * to its end vertex.
 *
 * MeshTriangulatorParametric, MeshTriangulatorPlanar and StepMeshExporter's
 * Triangulator each carried a private copy of this logic -- identical bodies,
 * one of them an instance method on an inner class. The copies are gone; the
 * three call sites share this implementation.
 *
 * An empty sample list cannot be reversed into anything meaningful, so it is
 * answered with the two vertex points directly. Otherwise the polyline is
 * reversed when it clearly runs backwards -- the endpoint distances are
 * compared rather than just the first sample, so a curve sampled in reverse is
 * still recognised -- and the first/last samples are snapped onto the vertices
 * when they do not already coincide.
 */
final class MeshSampleOrientationHelper {

    private MeshSampleOrientationHelper() {
        // Static helper class - no instances
    }

    static List<CartesianPoint> orientSamples(OrientedEdge orientedEdge, List<CartesianPoint> samples) {
        if (samples.isEmpty()) {
            return List.of(
                    orientedEdge.startVertex().point(),
                    orientedEdge.endVertex().point()
            );
        }
        List<CartesianPoint> oriented = new ArrayList<>(samples);
        CartesianPoint expectedStart = orientedEdge.startVertex().point();
        CartesianPoint expectedEnd = orientedEdge.endVertex().point();
        double forward = samples.get(0).distanceTo(expectedStart) + samples.get(samples.size() - 1).distanceTo(expectedEnd);
        double backward = samples.get(0).distanceTo(expectedEnd) + samples.get(samples.size() - 1).distanceTo(expectedStart);
        if (backward < forward) {
            Collections.reverse(oriented);
        }
        if (!oriented.get(0).equals(expectedStart)) {
            oriented.set(0, expectedStart);
        }
        if (!oriented.get(oriented.size() - 1).equals(expectedEnd)) {
            oriented.set(oriented.size() - 1, expectedEnd);
        }
        return List.copyOf(oriented);
    }
}
