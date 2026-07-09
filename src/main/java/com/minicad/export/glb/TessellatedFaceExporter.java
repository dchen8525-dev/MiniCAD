package com.minicad.export.glb;

import com.minicad.common.GeometryException;
import com.minicad.helper.metadata.StepMetadataExtractor;
import com.minicad.preview.payload.FacePayload;
import com.minicad.preview.payload.LoopPayload;
import com.minicad.preview.payload.PayloadConversionHelper;
import com.minicad.preview.payload.PointPayload;
import com.minicad.preview.payload.VectorPayload;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepTessellatedFace;
import com.minicad.step.model.StepTessellatedFaceSet;
import com.minicad.step.model.StepTessellatedTriangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods for building tessellated face payloads.
 * Extracted from StepPreviewJsonExporter for better code organization.
 */
public final class TessellatedFaceExporter {

    private TessellatedFaceExporter() {
        // Static helper class - no instances
    }

    /**
     * Builds face payloads from a tessellated face set (triangle mesh).
     */
    public static List<FacePayload> buildTessellatedFacePayloads(
            StepTessellatedFaceSet tessellated,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<StepCartesianPoint> coords = tessellated.coordinates();
        List<PointPayload> points = new ArrayList<>(coords.size());
        for (StepCartesianPoint cp : coords) {
            double cx = cp.coordinates().get(0);
            double cy = cp.coordinates().size() > 1 ? cp.coordinates().get(1) : 0.0;
            double cz = cp.coordinates().size() > 2 ? cp.coordinates().get(2) : 0.0;
            points.add(new PointPayload(cx, cy, cz));
        }
        List<FacePayload> faces = new ArrayList<>(tessellated.faceIndices().size());
        int faceIndexCount = 0;
        for (List<Integer> faceIndex : tessellated.faceIndices()) {
            faceIndexCount++;
            if (faceIndex.size() < 3) continue;

            // D04: Validate indices are within valid range
            for (int idx : faceIndex) {
                if (idx < 1) {
                    throw new GeometryException("tessellated face set #" + tessellated.id()
                            + " face #" + faceIndexCount + " has invalid index " + idx + " (must be >= 1)");
                }
                if (idx > points.size()) {
                    throw new GeometryException("tessellated face set #" + tessellated.id()
                            + " face #" + faceIndexCount + " index " + idx + " exceeds coordinate count " + points.size());
                }
            }

            PointPayload p1 = points.get(faceIndex.get(0) - 1);
            PointPayload p2 = points.get(faceIndex.get(1) - 1);
            PointPayload p3 = points.get(faceIndex.get(2) - 1);
            VectorPayload normal = computeNormal(p1, p2, p3);
            if (normal == null) continue;
            List<PointPayload> triangle = List.of(p1, p2, p3);
            FacePayload face = new FacePayload(
                    tessellated.id(),
                    tessellated.name(),
                    "TESSELLATED_FACE_SET",
                    p1,
                    normal,
                    true,
                    PayloadConversionHelper.toColorPayload(metadata.rgb()),
                    metadata.transparency(),
                    PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                    metadata.layers(),
                    List.of(new LoopPayload(true, triangle)),
                    triangle,
                    null,
                    null
            );
            faces.add(face);
        }
        return faces;
    }

    /**
     * Builds a face payload from a tessellated face (single triangle or triangle set).
     */
    public static FacePayload buildTessellatedFacePayload(
            StepTessellatedFace tessellatedFace,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        for (StepEntity triangleRef : tessellatedFace.triangles()) {
            if (triangleRef instanceof StepTessellatedTriangle) {
                StepTessellatedTriangle triangle = (StepTessellatedTriangle) triangleRef;
                PointPayload p1 = pointPayloadFromVertex(triangle.vertex1());
                PointPayload p2 = pointPayloadFromVertex(triangle.vertex2());
                PointPayload p3 = pointPayloadFromVertex(triangle.vertex3());
                if (p1 == null || p2 == null || p3 == null) continue;
                VectorPayload normal = computeNormal(p1, p2, p3);
                if (normal == null) continue;
                List<PointPayload> tri = List.of(p1, p2, p3);
                return new FacePayload(
                        tessellatedFace.id(),
                        tessellatedFace.name(),
                        "TESSELLATED_FACE",
                        p1,
                        normal,
                        true,
                        PayloadConversionHelper.toColorPayload(metadata.rgb()),
                        metadata.transparency(),
                        PayloadConversionHelper.toPbrPayload(metadata.pbr()),
                        metadata.layers(),
                        List.of(new LoopPayload(true, tri)),
                        tri,
                        null,
                        null
                );
            }
        }
        return null;
    }

    /**
     * Computes the face normal from three triangle vertices using the cross product.
     * Returns null if the normal is degenerate (near-zero length).
     */
    public static VectorPayload computeNormal(PointPayload p1, PointPayload p2, PointPayload p3) {
        double nx = (p2.y() - p1.y()) * (p3.z() - p1.z()) - (p2.z() - p1.z()) * (p3.y() - p1.y());
        double ny = (p2.z() - p1.z()) * (p3.x() - p1.x()) - (p2.x() - p1.x()) * (p3.z() - p1.z());
        double nz = (p2.x() - p1.x()) * (p3.y() - p1.y()) - (p2.y() - p1.y()) * (p3.x() - p1.x());
        double len = Math.sqrt(nx * nx + ny * ny + nz * nz);
        if (len < 1.0e-9) return null;
        return new VectorPayload(nx / len, ny / len, nz / len);
    }

    /**
     * Converts a STEP vertex entity to a PointPayload.
     */
    public static PointPayload pointPayloadFromVertex(StepEntity vertex) {
        if (vertex instanceof StepCartesianPoint) {
            StepCartesianPoint cp = (StepCartesianPoint) vertex;
            double cx = cp.coordinates().get(0);
            double cy = cp.coordinates().size() > 1 ? cp.coordinates().get(1) : 0.0;
            double cz = cp.coordinates().size() > 2 ? cp.coordinates().get(2) : 0.0;
            return new PointPayload(cx, cy, cz);
        }
        return null;
    }
}