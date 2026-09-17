package com.minicad.export.json;

import com.minicad.helper.MathUtilityHelper;
import com.minicad.preview.payload.AssemblyData;
import com.minicad.preview.payload.BoundsPayload;
import com.minicad.preview.payload.GeometryCollection;
import com.minicad.preview.payload.*;
import com.minicad.preview.statistics.BoundsAccumulator;

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
    public static void includeGeometry(BoundsAccumulator bounds, GeometryCollection geometry) {
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
    public static void includeAssembly(BoundsAccumulator bounds, AssemblyData assembly) {
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
    public static void includeBounds(BoundsAccumulator target, BoundsPayload bounds) {
        target.include(bounds.min());
        target.include(bounds.max());
    }

    /**
     * Includes representation bounds into the accumulator with transformation matrix.
     */
    public static void includeRepresentationBounds(
            BoundsAccumulator bounds,
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
