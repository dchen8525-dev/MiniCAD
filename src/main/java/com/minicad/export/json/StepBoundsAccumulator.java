package com.minicad.export.json;

import com.minicad.helper.MathUtilityHelper;
import com.minicad.preview.payload.AssemblyData;
import com.minicad.preview.payload.BoundsPayload;
import com.minicad.preview.payload.GeometryCollection;
import com.minicad.preview.payload.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Helper class for accumulating bounds from STEP geometry.
 * Extracted from StepPreviewJsonExporter for better organization.
 */
public final class StepBoundsAccumulator {

    private StepBoundsAccumulator() {
        // Utility class
    }

    /**
     * Includes geometry collection bounds into the accumulator.
     */
    public static void includeGeometry(PreviewSerializers.BoundsAccumulator bounds, GeometryCollection geometry) {
        for (FacePayload face : geometry.faces()) {
            for (LoopPayload loop : face.loops()) {
                for (PointPayload point : loop.points()) {
                    bounds.include(point);
                }
            }
        }
        for (EdgePayload edge : geometry.edges()) {
            for (PointPayload point : edge.points()) {
                bounds.include(point);
            }
        }
    }

    /**
     * Includes assembly data bounds into the accumulator.
     */
    public static void includeAssembly(PreviewSerializers.BoundsAccumulator bounds, AssemblyData assembly) {
        Map<Integer, RepresentationPayload> byId = assembly.representations().stream()
                .collect(Collectors.toMap(RepresentationPayload::id, representation -> representation, (left, right) -> left, LinkedHashMap::new));
        for (InstancePayload instance : assembly.instances()) {
            for (Integer representationId : instance.representationIds()) {
                RepresentationPayload representation = byId.get(representationId);
                if (representation == null) {
                    continue;
                }
                for (FacePayload face : representation.faces()) {
                    for (LoopPayload loop : face.loops()) {
                        for (PointPayload point : loop.points()) {
                            bounds.include(MathUtilityHelper.transform(point, instance.worldMatrix()));
                        }
                    }
                }
                for (EdgePayload edge : representation.edges()) {
                    for (PointPayload point : edge.points()) {
                        bounds.include(MathUtilityHelper.transform(point, instance.worldMatrix()));
                    }
                }
            }
        }
    }

    /**
     * Includes bounds payload into the accumulator.
     */
    public static void includeBounds(PreviewSerializers.BoundsAccumulator target, BoundsPayload bounds) {
        target.include(bounds.min());
        target.include(bounds.max());
    }

    /**
     * Creates a copy of the bounds accumulator.
     */
    public static PreviewSerializers.BoundsAccumulator copyBounds(PreviewSerializers.BoundsAccumulator source) {
        PreviewSerializers.BoundsAccumulator copy = new PreviewSerializers.BoundsAccumulator();
        if (!source.isEmpty()) {
            copy.minX = source.minX;
            copy.minY = source.minY;
            copy.minZ = source.minZ;
            copy.maxX = source.maxX;
            copy.maxY = source.maxY;
            copy.maxZ = source.maxZ;
        }
        return copy;
    }

    /**
     * Includes representation bounds into the accumulator with transformation matrix.
     */
    public static void includeRepresentationBounds(
            PreviewSerializers.BoundsAccumulator bounds,
            RepresentationPayload representation,
            double[] matrix
    ) {
        for (FacePayload face : representation.faces()) {
            for (LoopPayload loop : face.loops()) {
                for (PointPayload point : loop.points()) {
                    bounds.include(MathUtilityHelper.transform(point, matrix));
                }
            }
        }
        for (EdgePayload edge : representation.edges()) {
            for (PointPayload point : edge.points()) {
                bounds.include(MathUtilityHelper.transform(point, matrix));
            }
        }
    }
}