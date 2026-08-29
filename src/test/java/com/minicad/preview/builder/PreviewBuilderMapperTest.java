package com.minicad.preview.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minicad.preview.payload.EdgePayload;
import com.minicad.preview.payload.FacePayload;
import com.minicad.preview.payload.GeometryCollection;
import com.minicad.preview.payload.PointPayload;
import com.minicad.preview.payload.VectorPayload;
import java.util.List;
import org.junit.jupiter.api.Test;

class PreviewBuilderMapperTest {

    @Test
    void mergeGeometryConcatenatesAllThreeLists() {
        EdgePayload e1 = new EdgePayload(1,
                List.of(new PointPayload(0, 0, 0), new PointPayload(1, 0, 0)), null, null);
        EdgePayload e2 = new EdgePayload(2,
                List.of(new PointPayload(0, 0, 0), new PointPayload(0, 1, 0)), null, null);
        FacePayload f1 = face(1, true);
        GeometryCollection left = new GeometryCollection(List.of(e1), List.of(f1), List.of());
        GeometryCollection right = new GeometryCollection(List.of(e2), List.of(), List.of());

        GeometryCollection merged = PreviewGeometryCollector.mergeGeometry(left, right);

        assertEquals(2, merged.getEdges().size());
        assertEquals(1, merged.getFaces().size());
        assertEquals(0, merged.getUnsupportedFaces().size());
        // 合并后仍独立引用，不与原集合混用
        assertTrue(merged.getEdges().contains(e1));
        assertTrue(merged.getEdges().contains(e2));
    }

    @Test
    void reverseFacePayloadNegatesNormalAndFlipsWinding() {
        FacePayload base = new FacePayload(
                7, "face", "PLANE",
                new PointPayload(0, 0, 0),
                new VectorPayload(0, 0, 1),
                true,
                null, 0.0, null,
                List.of(), List.of(),
                List.of(new PointPayload(0, 0, 0),
                        new PointPayload(1, 0, 0),
                        new PointPayload(0, 1, 0)),
                null, List.of());

        FacePayload rev = PreviewFaceBuilder.reverseFacePayload(base);

        // 法线取反
        assertEquals(0.0, rev.getNormal().x(), 1e-9);
        assertEquals(0.0, rev.getNormal().y(), 1e-9);
        assertEquals(-1.0, rev.getNormal().z(), 1e-9);
        // sameSense 翻转
        assertFalse(rev.getSameSense());
        // 三角形绕序反转：(0,1,2) -> (0,2,1)
        List<PointPayload> bt = base.getTriangles();
        List<PointPayload> rt = rev.getTriangles();
        assertEquals(3, rt.size());
        assertEquals(bt.get(0), rt.get(0));
        assertEquals(bt.get(2), rt.get(1));
        assertEquals(bt.get(1), rt.get(2));
    }

    private static FacePayload face(int id, boolean sameSense) {
        return new FacePayload(id, "f" + id, "PLANE",
                new PointPayload(0, 0, 0),
                new VectorPayload(0, 0, 1),
                sameSense, null, 0.0, null,
                List.of(), List.of(),
                List.of(new PointPayload(0, 0, 0),
                        new PointPayload(1, 0, 0),
                        new PointPayload(0, 1, 0)),
                null, List.of());
    }
}
