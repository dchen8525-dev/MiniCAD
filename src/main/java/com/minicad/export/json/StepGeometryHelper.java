package com.minicad.export.json;

import com.minicad.common.Epsilon;
import com.minicad.geometry.CartesianPoint;
import com.minicad.step.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for geometry operations and calculations.
 * Extracted from StepPreviewJsonExporter to improve maintainability.
 *
 * <p>This class provides helper methods for geometric computations,
 * point manipulation, and geometry extraction.
 */
public final class StepGeometryHelper {

    private StepGeometryHelper() {
        // Utility class - prevent instantiation
    }

    /**
     * Reverses a list of Cartesian points.
     *
     * @param points the list of points
     * @return the reversed list
     */
    public static List<CartesianPoint> reversed(List<CartesianPoint> points) {
        List<CartesianPoint> copy = new ArrayList<>(points);
        java.util.Collections.reverse(copy);
        return List.copyOf(copy);
    }

    /**
     * Resamples a polyline to a fixed number of segments.
     * Maintains the start and end points exactly.
     *
     * @param points the original polyline points
     * @param segments the number of segments
     * @return the resampled points
     */
    public static List<CartesianPoint> resamplePolyline(List<CartesianPoint> points, int segments) {
        if (points.size() < 2) {
            return List.of(points.get(0));
        }
        List<Double> lengths = new ArrayList<>(points.size());
        lengths.add(0.0);
        for (int i = 1; i < points.size(); i++) {
            lengths.add(lengths.get(i - 1) + points.get(i - 1).distanceTo(points.get(i)));
        }
        double total = lengths.get(lengths.size() - 1);
        if (total <= Epsilon.EPS) {
            return java.util.Collections.nCopies(segments + 1, points.get(0));
        }
        List<CartesianPoint> result = new ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double target = total * i / segments;
            result.add(pointAtDistance(points, lengths, target));
        }
        result.set(0, points.get(0));
        result.set(result.size() - 1, points.get(points.size() - 1));
        return List.copyOf(result);
    }

    /**
     * Gets a point at a specific distance along a polyline.
     *
     * @param points the polyline points
     * @param lengths cumulative lengths at each point
     * @param target the target distance
     * @return the point at the target distance
     */
    public static CartesianPoint pointAtDistance(List<CartesianPoint> points, List<Double> lengths, double target) {
        for (int i = 1; i < lengths.size(); i++) {
            if (target <= lengths.get(i)) {
                double start = lengths.get(i - 1);
                double segment = lengths.get(i) - start;
                double alpha = segment <= Epsilon.EPS ? 0.0 : (target - start) / segment;
                return interpolate(points.get(i - 1), points.get(i), alpha);
            }
        }
        return points.get(points.size() - 1);
    }

    /**
     * Interpolates between two points.
     *
     * @param a the start point
     * @param b the end point
     * @param alpha the interpolation factor (0.0 = a, 1.0 = b)
     * @return the interpolated point
     */
    public static CartesianPoint interpolate(CartesianPoint a, CartesianPoint b, double alpha) {
        return new CartesianPoint(
                a.x() * (1.0 - alpha) + b.x() * alpha,
                a.y() * (1.0 - alpha) + b.y() * alpha,
                a.z() * (1.0 - alpha) + b.z() * alpha
        );
    }

    /**
     * Extracts the geometry entity from a face entity.
     * Handles AdvancedFace, FaceSurface, and OrientedFace.
     *
     * @param stepFace the STEP face entity
     * @return the geometry entity, or null if not available
     */
    public static StepEntity faceGeometry(StepFaceEntity stepFace) {
        if (stepFace instanceof StepAdvancedFace) {
            StepAdvancedFace advancedFace = (StepAdvancedFace) stepFace;
            return advancedFace.faceGeometry();
        }
        if (stepFace instanceof StepFaceSurface) {
            StepFaceSurface faceSurface = (StepFaceSurface) stepFace;
            return faceSurface.faceGeometry();
        }
        if (stepFace instanceof StepOrientedFace) {
            StepOrientedFace orientedFace = (StepOrientedFace) stepFace;
            return faceGeometry(orientedFace.faceElement());
        }
        return null;
    }

    /**
     * Gets the display name for a face entity.
     *
     * @param stepFace the STEP face entity
     * @return the display name
     */
    public static String faceDisplayName(StepFaceEntity stepFace) {
        if (stepFace instanceof StepAdvancedFace) {
            StepAdvancedFace advancedFace = (StepAdvancedFace) stepFace;
            return advancedFace.name();
        }
        if (stepFace instanceof StepFaceSurface) {
            StepFaceSurface faceSurface = (StepFaceSurface) stepFace;
            return faceSurface.name();
        }
        if (stepFace instanceof StepOrientedFace) {
            StepOrientedFace orientedFace = (StepOrientedFace) stepFace;
            return orientedFace.name();
        }
        return null;
    }
}