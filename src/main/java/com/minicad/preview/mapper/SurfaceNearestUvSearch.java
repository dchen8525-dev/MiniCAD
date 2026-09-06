package com.minicad.preview.mapper;

import com.minicad.geometry.BSplineSurface3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.RationalBSplineSurface3;
import com.minicad.helper.MathUtilityHelper;
import com.minicad.preview.payload.UvPoint;

/**
 * Coarse-to-refined nearest-UV search over B-spline surfaces, extracted from
 * SurfaceMapperHelper to keep that class under the 1000-line budget.
 */
final class SurfaceNearestUvSearch {

    private SurfaceNearestUvSearch() {
    }

    static UvPoint nearestUvOnBSplineSurface(BSplineSurface3 surface, CartesianPoint point, UvPoint previous) {
        double uStart = surface.uStart();
        double uEnd = surface.uEnd();
        double vStart = surface.vStart();
        double vEnd = surface.vEnd();
        boolean hasPrevious = previous != null;

        double bestU = hasPrevious ? MathUtilityHelper.clamp(previous.u(), uStart, uEnd) : uStart;
        double bestV = hasPrevious ? MathUtilityHelper.clamp(previous.v(), vStart, vEnd) : vStart;
        double bestDistance = surface.pointAt(bestU, bestV).distanceTo(point);

        int uSamples = hasPrevious ? 4 : 12;
        int vSamples = hasPrevious ? 4 : 12;
        double coarseWindowU = (uEnd - uStart) * (hasPrevious ? 0.08 : 0.25);
        double coarseWindowV = (vEnd - vStart) * (hasPrevious ? 0.08 : 0.25);
        double coarseMinU = hasPrevious ? Math.max(uStart, bestU - coarseWindowU) : uStart;
        double coarseMaxU = hasPrevious ? Math.min(uEnd, bestU + coarseWindowU) : uEnd;
        double coarseMinV = hasPrevious ? Math.max(vStart, bestV - coarseWindowV) : vStart;
        double coarseMaxV = hasPrevious ? Math.min(vEnd, bestV + coarseWindowV) : vEnd;

        for (int ui = 0; ui <= uSamples; ui++) {
            double u = coarseMinU + (coarseMaxU - coarseMinU) * ui / (double) uSamples;
            for (int vi = 0; vi <= vSamples; vi++) {
                double v = coarseMinV + (coarseMaxV - coarseMinV) * vi / (double) vSamples;
                double distance = surface.pointAt(u, v).distanceTo(point);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestU = u;
                    bestV = v;
                }
            }
        }

        double windowU = Math.max((uEnd - uStart) * (hasPrevious ? 0.03 : 0.08), 1.0e-5);
        double windowV = Math.max((vEnd - vStart) * (hasPrevious ? 0.03 : 0.08), 1.0e-5);
        int refinements = hasPrevious ? 3 : 4;
        int refinementSamples = hasPrevious ? 4 : 6;
        for (int refinement = 0; refinement < refinements; refinement++) {
            double minU = Math.max(uStart, bestU - windowU);
            double maxU = Math.min(uEnd, bestU + windowU);
            double minV = Math.max(vStart, bestV - windowV);
            double maxV = Math.min(vEnd, bestV + windowV);
            for (int ui = 0; ui <= refinementSamples; ui++) {
                double u = minU + (maxU - minU) * ui / (double) refinementSamples;
                for (int vi = 0; vi <= refinementSamples; vi++) {
                    double v = minV + (maxV - minV) * vi / (double) refinementSamples;
                    double distance = surface.pointAt(u, v).distanceTo(point);
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        bestU = u;
                        bestV = v;
                    }
                }
            }
            if (bestDistance <= 1.0e-6) {
                break;
            }
            windowU *= 0.5;
            windowV *= 0.5;
        }
        return new UvPoint(bestU, bestV);
    }

    static UvPoint nearestUvOnRationalBSplineSurface(
            RationalBSplineSurface3 surface,
            CartesianPoint point,
            UvPoint previous
    ) {
        double uStart = surface.uStart();
        double uEnd = surface.uEnd();
        double vStart = surface.vStart();
        double vEnd = surface.vEnd();
        boolean hasPrevious = previous != null;

        double bestU = hasPrevious ? MathUtilityHelper.clamp(previous.u(), uStart, uEnd) : uStart;
        double bestV = hasPrevious ? MathUtilityHelper.clamp(previous.v(), vStart, vEnd) : vStart;
        double bestDistance = surface.pointAt(bestU, bestV).distanceTo(point);

        int uSamples = hasPrevious ? 4 : 12;
        int vSamples = hasPrevious ? 4 : 12;
        double coarseWindowU = (uEnd - uStart) * (hasPrevious ? 0.08 : 0.25);
        double coarseWindowV = (vEnd - vStart) * (hasPrevious ? 0.08 : 0.25);
        double coarseMinU = hasPrevious ? Math.max(uStart, bestU - coarseWindowU) : uStart;
        double coarseMaxU = hasPrevious ? Math.min(uEnd, bestU + coarseWindowU) : uEnd;
        double coarseMinV = hasPrevious ? Math.max(vStart, bestV - coarseWindowV) : vStart;
        double coarseMaxV = hasPrevious ? Math.min(vEnd, bestV + coarseWindowV) : vEnd;

        for (int i = 0; i <= uSamples; i++) {
            double u = coarseMinU + (coarseMaxU - coarseMinU) * i / Math.max(uSamples, 1);
            for (int j = 0; j <= vSamples; j++) {
                double v = coarseMinV + (coarseMaxV - coarseMinV) * j / Math.max(vSamples, 1);
                double distance = surface.pointAt(u, v).distanceTo(point);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestU = u;
                    bestV = v;
                }
            }
        }

        for (int iteration = 0; iteration < 4; iteration++) {
            double stepU = (uEnd - uStart) / Math.pow(4.0, iteration + 2);
            double stepV = (vEnd - vStart) / Math.pow(4.0, iteration + 2);
            for (int du = -1; du <= 1; du++) {
                for (int dv = -1; dv <= 1; dv++) {
                    double u = MathUtilityHelper.clamp(bestU + du * stepU, uStart, uEnd);
                    double v = MathUtilityHelper.clamp(bestV + dv * stepV, vStart, vEnd);
                    double distance = surface.pointAt(u, v).distanceTo(point);
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        bestU = u;
                        bestV = v;
                    }
                }
            }
        }
        return new UvPoint(bestU, bestV);
    }
}
