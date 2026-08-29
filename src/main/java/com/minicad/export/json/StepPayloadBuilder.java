package com.minicad.export.json;

import com.minicad.geometry.CartesianPoint;
import com.minicad.preview.payload.FacePayload;
import com.minicad.preview.payload.PointPayload;
import com.minicad.preview.payload.UnsupportedFacePayload;
import com.minicad.preview.payload.VectorPayload;
import com.minicad.step.model.StepFaceEntity;
import com.minicad.topology.Edge;
import com.minicad.topology.Face;
import com.minicad.topology.FaceBound;
import com.minicad.topology.OrientedEdge;
import com.minicad.topology.VertexLoop;
import com.minicad.topology.PolyLoop;
import com.minicad.topology.EdgeLoop;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Utility class for Payload manipulation and building operations.
 * Extracted from StepPreviewJsonExporter to improve maintainability.
 *
 * <p>This class provides helper methods for creating and transforming
 * preview payload objects.
 */
public final class StepPayloadBuilder {

    private StepPayloadBuilder() {
        // Utility class - prevent instantiation
    }

    /**
     * Reverses a face payload by flipping triangles and normal.
     * Used for oriented faces with negative orientation.
     *
     * @param base the base face payload
     * @return the reversed face payload
     */
    public static FacePayload reverseFacePayload(FacePayload base) {
        List<PointPayload> reversedTriangles = new ArrayList<>(base.triangles().size());
        for (int index = 0; index + 2 < base.triangles().size(); index += 3) {
            reversedTriangles.add(base.triangles().get(index));
            reversedTriangles.add(base.triangles().get(index + 2));
            reversedTriangles.add(base.triangles().get(index + 1));
        }
        return new FacePayload(
                base.stepId(),
                base.name(),
                base.surfaceType(),
                base.origin(),
                new VectorPayload(-base.normal().x(), -base.normal().y(), -base.normal().z()),
                !base.sameSense(),
                base.color(),
                base.transparency(),
                base.pbr(),
                base.layers(),
                base.loops(),
                List.copyOf(reversedTriangles),
                base.surface(),
                base.uvLoops()
        );
    }

    /**
     * Creates an unsupported face payload with a reason.
     *
     * @param stepFace the STEP face entity
     * @param geometry the face geometry entity
     * @param displayName the display name for the face
     * @param reason the reason why the face is unsupported
     * @return the unsupported face payload
     */
    public static UnsupportedFacePayload toUnsupportedFacePayload(
            StepFaceEntity stepFace,
            Object geometry,
            String displayName,
            String reason
    ) {
        String surfaceType = geometry != null ? StepTypeNameResolver.surfaceTypeName((com.minicad.step.model.StepEntity) geometry) : "UNKNOWN";
        return new UnsupportedFacePayload(
                stepFace.id(),
                displayName,
                surfaceType,
                reason == null ? "preview export returned no mesh" : reason
        );
    }

    /**
     * Reverses a closed loop of points.
     * Maintains the start/end point for closed loops.
     *
     * @param <T> the point type
     * @param points the list of points forming a closed loop
     * @return the reversed loop
     */
    public static <T> List<T> reverseClosedLoop(List<T> points) {
        if (points.size() < 2) {
            return points;
        }
        List<T> reversed = new ArrayList<>(points);
        if (reversed.get(0).equals(reversed.get(reversed.size() - 1))) {
            T start = reversed.remove(reversed.size() - 1);
            java.util.Collections.reverse(reversed);
            reversed.add(reversed.get(0));
            reversed.set(0, start);
            reversed.set(reversed.size() - 1, start);
            return reversed;
        }
        java.util.Collections.reverse(reversed);
        return reversed;
    }

    /**
     * Collects topology edges from a face.
     *
     * @param face the topology face
     * @param edges the set to collect edges into
     */
    public static void collectTopologyEdges(Face face, Set<Edge> edges) {
        for (FaceBound bound : face.bounds()) {
            if (bound.loop() instanceof EdgeLoop) {
                EdgeLoop edgeLoop = (EdgeLoop) bound.loop();
                for (OrientedEdge orientedEdge : edgeLoop.edges()) {
                    edges.add(orientedEdge.edge());
                }
            }
        }
    }

    /**
     * Samples points from a face bound.
     *
     * @param bound the face bound
     * @return the sampled points
     * @throws com.minicad.common.UnsupportedGeometryException if loop type is not supported
     */
    public static List<CartesianPoint> sampleLoop(FaceBound bound) {
        if (bound.loop() instanceof VertexLoop) {
            VertexLoop vertexLoop = (VertexLoop) bound.loop();
            return List.of(vertexLoop.vertex().point());
        }
        if (bound.loop() instanceof PolyLoop) {
            PolyLoop polyLoop = (PolyLoop) bound.loop();
            List<CartesianPoint> sampled = new ArrayList<>(polyLoop.points());
            if (!sampled.isEmpty() && sampled.get(0).distanceTo(sampled.get(sampled.size() - 1)) > 1.0e-9) {
                sampled.add(sampled.get(0));
            }
            return bound.orientation() ? sampled : reverseClosedLoop(sampled);
        }
        // For EdgeLoop, need oriented edge sampling which requires more context
        // This is handled separately in StepPreviewJsonExporter
        throw new com.minicad.common.UnsupportedGeometryException(
                "preview export requires EDGE_LOOP, POLY_LOOP or VERTEX_LOOP"
        );
    }

    /**
     * Computes a mapped payload ID for uniqueness.
     *
     * @param mappedItemId the mapped item ID
     * @param sourceId the source entity ID
     * @param salt a salt value
     * @return the computed payload ID
     */
    public static int mappedPayloadId(int mappedItemId, int sourceId, int salt) {
        return mappedItemId * 1000000 + sourceId * 10 + salt;
    }
}
