package com.minicad.app;

import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.ConicalSurface;
import com.minicad.geometry.CylindricalSurface;
import com.minicad.geometry.ToroidalSurface;
import com.minicad.geometry.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods for computing surface coordinates and normals.
 * Extracted from StepPreviewJsonExporter for better code organization.
 */
final class SurfaceGeometryHelper {

    private SurfaceGeometryHelper() {
        // Static helper class - no instances
    }

    // === Cylindrical surface helpers ===

    static List<Double> unwrapAngles(CylindricalSurface surface, List<CartesianPoint> points) {
        return unwrapAngles(surface.position(), points);
    }

    static List<Double> unwrapAngles(Axis2Placement3D placement, List<CartesianPoint> points) {
        List<Double> angles = new ArrayList<>(points.size());
        for (CartesianPoint point : points) {
            double angle = cylindricalAngle(placement, point);
            if (!angles.isEmpty()) {
                double previous = angles.get(angles.size() - 1);
                while (angle - previous > Math.PI) {
                    angle -= Math.PI * 2.0;
                }
                while (angle - previous < -Math.PI) {
                    angle += Math.PI * 2.0;
                }
            }
            angles.add(angle);
        }
        return List.copyOf(angles);
    }

    static double averageAxialHeight(CylindricalSurface surface, List<CartesianPoint> points) {
        return averageAxialHeight(surface.position(), points);
    }

    static double averageAxialHeight(Axis2Placement3D placement, List<CartesianPoint> points) {
        double total = 0.0;
        for (CartesianPoint point : points) {
            total += axialHeight(placement, point);
        }
        return total / points.size();
    }

    static double axialHeight(CylindricalSurface surface, CartesianPoint point) {
        return axialHeight(surface.position(), point);
    }

    static double axialHeight(Axis2Placement3D placement, CartesianPoint point) {
        return point.subtract(placement.location()).dot(placement.axis().asVector());
    }

    static double cylindricalAngle(CylindricalSurface surface, CartesianPoint point) {
        return cylindricalAngle(surface.position(), point);
    }

    static double cylindricalAngle(Axis2Placement3D placement, CartesianPoint point) {
        Vector3 offset = point.subtract(placement.location());
        double x = offset.dot(placement.xDirection().asVector());
        double y = offset.dot(placement.yDirection().asVector());
        return Math.atan2(y, x);
    }

    static CartesianPoint surfacePoint(CylindricalSurface surface, double angle, double height) {
        Axis2Placement3D placement = surface.position();
        Vector3 radial = placement.xDirection().asVector().scale(Math.cos(angle) * surface.radius())
                .add(placement.yDirection().asVector().scale(Math.sin(angle) * surface.radius()));
        Vector3 axial = placement.axis().asVector().scale(height);
        return placement.location().add(radial.add(axial));
    }

    static Vector3 cylindricalNormal(CylindricalSurface surface, double angle, boolean sameSense) {
        Axis2Placement3D placement = surface.position();
        Vector3 radial = placement.xDirection().asVector().scale(Math.cos(angle))
                .add(placement.yDirection().asVector().scale(Math.sin(angle)));
        return sameSense ? radial : radial.scale(-1.0);
    }

    // === Conical surface helpers ===

    static CartesianPoint conicalSurfacePoint(ConicalSurface surface, double angle, double height) {
        Axis2Placement3D placement = surface.position();
        double radius = surface.radius() + height * Math.tan(surface.semiAngle());
        Vector3 radial = placement.xDirection().asVector().scale(Math.cos(angle) * radius)
                .add(placement.yDirection().asVector().scale(Math.sin(angle) * radius));
        Vector3 axial = placement.axis().asVector().scale(height);
        return placement.location().add(radial.add(axial));
    }

    static Vector3 conicalNormal(ConicalSurface surface, double angle, boolean sameSense) {
        Axis2Placement3D placement = surface.position();
        double slope = Math.tan(surface.semiAngle());
        Vector3 radial = placement.xDirection().asVector().scale(Math.cos(angle))
                .add(placement.yDirection().asVector().scale(Math.sin(angle)));
        Vector3 normal = radial.subtract(placement.axis().asVector().scale(slope));
        return sameSense ? normal.normalize().asVector() : normal.normalize().reverse().asVector();
    }

    // === Spherical surface helpers ===

    static double sphericalU(Axis2Placement3D placement, CartesianPoint point) {
        Vector3 offset = point.subtract(placement.location());
        double x = offset.dot(placement.xDirection().asVector());
        double y = offset.dot(placement.yDirection().asVector());
        return Math.atan2(y, x);
    }

    static double sphericalV(Axis2Placement3D placement, CartesianPoint point, double radius) {
        Vector3 offset = point.subtract(placement.location());
        double z = offset.dot(placement.axis().asVector());
        double normalized = radius <= 1.0e-12 ? 0.0 : z / radius;
        normalized = Math.max(-1.0, Math.min(1.0, normalized));
        return Math.asin(normalized);
    }

    static CartesianPoint sphericalSurfacePoint(Axis2Placement3D placement, double radius, double u, double v) {
        double cosV = Math.cos(v);
        Vector3 normal = placement.xDirection().asVector().scale(Math.cos(u) * cosV)
                .add(placement.yDirection().asVector().scale(Math.sin(u) * cosV))
                .add(placement.axis().asVector().scale(Math.sin(v)));
        return placement.location().add(normal.scale(radius));
    }

    static Vector3 sphericalNormal(Axis2Placement3D placement, double u, double v, boolean sameSense) {
        double cosV = Math.cos(v);
        Vector3 normal = placement.xDirection().asVector().scale(Math.cos(u) * cosV)
                .add(placement.yDirection().asVector().scale(Math.sin(u) * cosV))
                .add(placement.axis().asVector().scale(Math.sin(v)));
        return sameSense ? normal.normalize().asVector() : normal.normalize().reverse().asVector();
    }

    // === Toroidal surface helpers ===

    static CartesianPoint toroidalSurfacePoint(ToroidalSurface surface, double u, double v) {
        return toroidalSurfacePoint(surface.position(), surface.majorRadius(), surface.minorRadius(), u, v);
    }

    static CartesianPoint toroidalSurfacePoint(
            Axis2Placement3D placement,
            double majorRadius,
            double minorRadius,
            double u,
            double v
    ) {
        double radial = majorRadius + minorRadius * Math.cos(v);
        Vector3 xy = placement.xDirection().asVector().scale(Math.cos(u) * radial)
                .add(placement.yDirection().asVector().scale(Math.sin(u) * radial));
        Vector3 z = placement.axis().asVector().scale(minorRadius * Math.sin(v));
        return placement.location().add(xy.add(z));
    }

    static Vector3 toroidalNormal(ToroidalSurface surface, double u, double v, boolean sameSense) {
        return toroidalNormal(surface.position(), u, v, sameSense);
    }

    static Vector3 toroidalNormal(Axis2Placement3D placement, double u, double v, boolean sameSense) {
        Vector3 normal = placement.xDirection().asVector().scale(Math.cos(u) * Math.cos(v))
                .add(placement.yDirection().asVector().scale(Math.sin(u) * Math.cos(v)))
                .add(placement.axis().asVector().scale(Math.sin(v)));
        return sameSense ? normal.normalize().asVector() : normal.normalize().reverse().asVector();
    }

    static List<Double> unwrapToroidalU(ToroidalSurface surface, List<CartesianPoint> points) {
        List<Double> values = new ArrayList<>(points.size());
        for (CartesianPoint point : points) {
            double value = toroidalU(surface, point);
            if (!values.isEmpty()) {
                double previous = values.get(values.size() - 1);
                while (value - previous > Math.PI) {
                    value -= Math.PI * 2.0;
                }
                while (value - previous < -Math.PI) {
                    value += Math.PI * 2.0;
                }
            }
            values.add(value);
        }
        return List.copyOf(values);
    }

    static List<Double> unwrapToroidalV(ToroidalSurface surface, List<CartesianPoint> points) {
        List<Double> values = new ArrayList<>(points.size());
        for (CartesianPoint point : points) {
            double value = toroidalV(surface, point);
            if (!values.isEmpty()) {
                double previous = values.get(values.size() - 1);
                while (value - previous > Math.PI) {
                    value -= Math.PI * 2.0;
                }
                while (value - previous < -Math.PI) {
                    value += Math.PI * 2.0;
                }
            }
            values.add(value);
        }
        return List.copyOf(values);
    }

    static double averageToroidalV(ToroidalSurface surface, List<CartesianPoint> points) {
        double total = 0.0;
        for (CartesianPoint point : points) {
            total += toroidalV(surface, point);
        }
        return total / points.size();
    }

    static double toroidalU(ToroidalSurface surface, CartesianPoint point) {
        return toroidalU(surface.position(), point);
    }

    static double toroidalU(Axis2Placement3D placement, CartesianPoint point) {
        Vector3 offset = point.subtract(placement.location());
        double x = offset.dot(placement.xDirection().asVector());
        double y = offset.dot(placement.yDirection().asVector());
        return Math.atan2(y, x);
    }

    static double toroidalV(ToroidalSurface surface, CartesianPoint point) {
        return toroidalV(surface.position(), surface.majorRadius(), point);
    }

    static double toroidalV(Axis2Placement3D placement, double majorRadius, CartesianPoint point) {
        Vector3 offset = point.subtract(placement.location());
        double x = offset.dot(placement.xDirection().asVector());
        double y = offset.dot(placement.yDirection().asVector());
        double z = offset.dot(placement.axis().asVector());
        double rho = Math.sqrt(x * x + y * y);
        return Math.atan2(z, rho - majorRadius);
    }
}