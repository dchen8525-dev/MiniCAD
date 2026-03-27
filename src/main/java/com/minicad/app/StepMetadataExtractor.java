package com.minicad.app;

import com.minicad.step.model.StepColourRgb;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepFillAreaStyle;
import com.minicad.step.model.StepFillAreaStyleColour;
import com.minicad.step.model.StepPresentationLayerAssignment;
import com.minicad.step.model.StepPresentationStyleAssignment;
import com.minicad.step.model.StepStyledItem;
import com.minicad.step.model.StepSurfaceSideStyle;
import com.minicad.step.model.StepSurfaceStyleFillArea;
import com.minicad.step.model.StepSurfaceStyleUsage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Extracts a minimal set of display metadata from resolved STEP presentation entities.
 */
public final class StepMetadataExtractor {

    private final Map<Integer, DisplayMetadata> metadataByItemId;

    private StepMetadataExtractor(Map<Integer, DisplayMetadata> metadataByItemId) {
        this.metadataByItemId = metadataByItemId;
    }

    public static StepMetadataExtractor fromResolved(Map<Integer, StepEntity> resolved) {
        Map<Integer, MutableMetadata> mutableByItemId = new LinkedHashMap<>();

        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepStyledItem styledItem) {
                MutableMetadata metadata = mutableByItemId.computeIfAbsent(styledItem.item().id(), ignored -> new MutableMetadata());
                int[] rgb = extractRgb(styledItem);
                if (rgb != null) {
                    metadata.rgb = rgb;
                }
            } else if (entity instanceof StepPresentationLayerAssignment layerAssignment) {
                for (StepEntity assignedItem : layerAssignment.assignedItems()) {
                    MutableMetadata metadata = mutableByItemId.computeIfAbsent(assignedItem.id(), ignored -> new MutableMetadata());
                    metadata.layers.add(layerAssignment.name());
                }
            }
        }

        Map<Integer, DisplayMetadata> immutable = new LinkedHashMap<>();
        for (Map.Entry<Integer, MutableMetadata> entry : mutableByItemId.entrySet()) {
            immutable.put(entry.getKey(), new DisplayMetadata(entry.getValue().rgb, List.copyOf(entry.getValue().layers)));
        }
        return new StepMetadataExtractor(Map.copyOf(immutable));
    }

    public DisplayMetadata forItem(int itemId) {
        return metadataByItemId.getOrDefault(itemId, DisplayMetadata.EMPTY);
    }

    private static int[] extractRgb(StepStyledItem styledItem) {
        for (StepPresentationStyleAssignment assignment : styledItem.styles()) {
            for (StepSurfaceStyleUsage usage : assignment.styles()) {
                StepSurfaceSideStyle sideStyle = usage.style();
                for (StepSurfaceStyleFillArea surfaceFill : sideStyle.styles()) {
                    StepFillAreaStyle fillStyle = surfaceFill.fillStyle();
                    for (StepFillAreaStyleColour fillColour : fillStyle.styles()) {
                        StepColourRgb colour = fillColour.colour();
                        return new int[]{
                                toChannel(colour.red()),
                                toChannel(colour.green()),
                                toChannel(colour.blue())
                        };
                    }
                }
            }
        }
        return null;
    }

    private static int toChannel(double value) {
        return (int) Math.max(0, Math.min(255, Math.round(value * 255.0)));
    }

    public record DisplayMetadata(int[] rgb, List<String> layers) {
        static final DisplayMetadata EMPTY = new DisplayMetadata(null, List.of());

        public DisplayMetadata {
            layers = List.copyOf(layers);
        }
    }

    private static final class MutableMetadata {
        private int[] rgb;
        private final Set<String> layers = new LinkedHashSet<>();
    }
}
