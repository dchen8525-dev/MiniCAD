package com.minicad.app;

import com.minicad.common.Epsilon;

import java.util.List;

class GeometryMeasurementHelper {
    static double approximateSurfaceArea(List<FacePayload> faces) {
        double total = 0.0;
        for (FacePayload face : faces) {
            if (!face.triangles().isEmpty()) {
                total += triangleArea(face.triangles());
            } else {
                total += loopArea(face);
            }
        }
        return total;
    }

    static double approximateSurfaceArea(List<FacePayload> faces, double[] matrix) {
        double total = 0.0;
        for (FacePayload face : faces) {
            if (!face.triangles().isEmpty()) {
                total += triangleArea(face.triangles(), matrix);
            } else {
                total += loopArea(face, matrix);
            }
        }
        return total;
    }

    static double approximateEdgeLength(List<EdgePayload> edges) {
        double total = 0.0;
        for (EdgePayload edge : edges) {
            for (int i = 0; i + 1 < edge.points().size(); i++) {
                total += distance(edge.points().get(i), edge.points().get(i + 1));
            }
        }
        return total;
    }

    static double approximateEdgeLength(List<EdgePayload> edges, double[] matrix) {
        double total = 0.0;
        for (EdgePayload edge : edges) {
            for (int i = 0; i + 1 < edge.points().size(); i++) {
                total += distance(MathUtilityHelper.transform(edge.points().get(i), matrix), MathUtilityHelper.transform(edge.points().get(i + 1), matrix));
            }
        }
        return total;
    }

    static double triangleArea(List<PointPayload> triangles) {
        double total = 0.0;
        for (int i = 0; i + 2 < triangles.size(); i += 3) {
            PointPayload a = triangles.get(i);
            PointPayload b = triangles.get(i + 1);
            PointPayload c = triangles.get(i + 2);
            double abx = b.x() - a.x();
            double aby = b.y() - a.y();
            double abz = b.z() - a.z();
            double acx = c.x() - a.x();
            double acy = c.y() - a.y();
            double acz = c.z() - a.z();
            double cx = aby * acz - abz * acy;
            double cy = abz * acx - abx * acz;
            double cz = abx * acy - aby * acx;
            total += 0.5 * Math.sqrt(cx * cx + cy * cy + cz * cz);
        }
        return total;
    }

    static double triangleArea(List<PointPayload> triangles, double[] matrix) {
        double total = 0.0;
        for (int i = 0; i + 2 < triangles.size(); i += 3) {
            PointPayload a = MathUtilityHelper.transform(triangles.get(i), matrix);
            PointPayload b = MathUtilityHelper.transform(triangles.get(i + 1), matrix);
            PointPayload c = MathUtilityHelper.transform(triangles.get(i + 2), matrix);
            double abx = b.x() - a.x();
            double aby = b.y() - a.y();
            double abz = b.z() - a.z();
            double acx = c.x() - a.x();
            double acy = c.y() - a.y();
            double acz = c.z() - a.z();
            double cx = aby * acz - abz * acy;
            double cy = abz * acx - abx * acz;
            double cz = abx * acy - aby * acx;
            total += 0.5 * Math.sqrt(cx * cx + cy * cy + cz * cz);
        }
        return total;
    }

    static double loopArea(FacePayload face) {
        double total = 0.0;
        for (LoopPayload loop : face.loops()) {
            double area = polygonArea(loop.points(), face.normal());
            total += loop.outer() ? area : -area;
        }
        return Math.abs(total);
    }

    static double loopArea(FacePayload face, double[] matrix) {
        double total = 0.0;
        for (LoopPayload loop : face.loops()) {
            double area = polygonArea(loop.points(), face.normal(), matrix);
            total += loop.outer() ? area : -area;
        }
        return Math.abs(total);
    }

    static double polygonArea(List<PointPayload> points, VectorPayload normal) {
        if (points.size() < 3) {
            return 0.0;
        }
        double nx = normal.x();
        double ny = normal.y();
        double nz = normal.z();
        double length = Math.sqrt(nx * nx + ny * ny + nz * nz);
        if (length <= Epsilon.EPS) {
            return 0.0;
        }
        nx /= length;
        ny /= length;
        nz /= length;
        double areaVectorX = 0.0;
        double areaVectorY = 0.0;
        double areaVectorZ = 0.0;
        for (int i = 0; i < points.size(); i++) {
            PointPayload current = points.get(i);
            PointPayload next = points.get((i + 1) % points.size());
            areaVectorX += current.y() * next.z() - current.z() * next.y();
            areaVectorY += current.z() * next.x() - current.x() * next.z();
            areaVectorZ += current.x() * next.y() - current.y() * next.x();
        }
        return Math.abs((areaVectorX * nx + areaVectorY * ny + areaVectorZ * nz) * 0.5);
    }

    static double polygonArea(List<PointPayload> points, VectorPayload normal, double[] matrix) {
        if (points.size() < 3) {
            return 0.0;
        }
        double nx = normal.x();
        double ny = normal.y();
        double nz = normal.z();
        double length = Math.sqrt(nx * nx + ny * ny + nz * nz);
        if (length <= Epsilon.EPS) {
            return 0.0;
        }
        nx /= length;
        ny /= length;
        nz /= length;
        double areaVectorX = 0.0;
        double areaVectorY = 0.0;
        double areaVectorZ = 0.0;
        for (int i = 0; i < points.size(); i++) {
            PointPayload current = MathUtilityHelper.transform(points.get(i), matrix);
            PointPayload next = MathUtilityHelper.transform(points.get((i + 1) % points.size()), matrix);
            areaVectorX += current.y() * next.z() - current.z() * next.y();
            areaVectorY += current.z() * next.x() - current.x() * next.z();
            areaVectorZ += current.x() * next.y() - current.y() * next.x();
        }
        return Math.abs((areaVectorX * nx + areaVectorY * ny + areaVectorZ * nz) * 0.5);
    }

    static double distance(PointPayload a, PointPayload b) {
        double dx = b.x() - a.x();
        double dy = b.y() - a.y();
        double dz = b.z() - a.z();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
