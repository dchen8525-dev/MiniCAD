package com.minicad.app;

import com.minicad.step.model.annotation.StepColourRgb;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.annotation.StepCurveStyle;
import com.minicad.step.model.annotation.StepFillAreaStyle;
import com.minicad.step.model.annotation.StepFillAreaStyleColour;
import com.minicad.step.model.annotation.StepDraughtingPreDefinedColour;
import com.minicad.step.model.annotation.StepOverRidingStyledItem;
import com.minicad.step.model.annotation.StepPresentationLayerAssignment;
import com.minicad.step.model.annotation.StepPresentationStyleAssignment;
import com.minicad.step.model.annotation.StepStyledItem;
import com.minicad.step.model.annotation.StepSurfaceSideStyle;
import com.minicad.step.model.annotation.StepSurfaceStyleFillArea;
import com.minicad.step.model.annotation.StepSurfaceStyleRendering;
import com.minicad.step.model.annotation.StepSurfaceStyleReflectanceAmbient;
import com.minicad.step.model.annotation.StepSurfaceStyleReflectanceAmbientDiffuse;
import com.minicad.step.model.annotation.StepSurfaceStyleReflectanceAmbientDiffuseSpecular;
import com.minicad.step.model.annotation.StepSurfaceStyleTransparent;
import com.minicad.step.model.annotation.StepSurfaceStyleUsage;

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
                extractStyle(styledItem, metadata);
            } else if (entity instanceof StepOverRidingStyledItem styledItem) {
                MutableMetadata metadata = mutableByItemId.computeIfAbsent(styledItem.item().id(), ignored -> new MutableMetadata());
                extractStyle(styledItem.styles(), metadata);
            } else if (entity instanceof StepPresentationLayerAssignment layerAssignment) {
                for (StepEntity assignedItem : layerAssignment.assignedItems()) {
                    MutableMetadata metadata = mutableByItemId.computeIfAbsent(assignedItem.id(), ignored -> new MutableMetadata());
                    metadata.layers.add(layerAssignment.name());
                }
            }
        }

        Map<Integer, DisplayMetadata> immutable = new LinkedHashMap<>();
        for (Map.Entry<Integer, MutableMetadata> entry : mutableByItemId.entrySet()) {
            immutable.put(entry.getKey(), new DisplayMetadata(
                    entry.getValue().rgb,
                    List.copyOf(entry.getValue().layers),
                    entry.getValue().transparency,
                    entry.getValue().pbr
            ));
        }
        return new StepMetadataExtractor(Map.copyOf(immutable));
    }

    public DisplayMetadata forItem(int itemId) {
        return metadataByItemId.getOrDefault(itemId, DisplayMetadata.EMPTY);
    }

    private static void extractStyle(StepStyledItem styledItem, MutableMetadata metadata) {
        extractStyle(styledItem.styles(), metadata);
    }

    private static void extractStyle(List<StepPresentationStyleAssignment> assignments, MutableMetadata metadata) {
        for (StepPresentationStyleAssignment assignment : assignments) {
            for (StepEntity style : assignment.styles()) {
                if (style instanceof StepCurveStyle curveStyle) {
                    int[] rgb = colourToRgb(curveStyle.colour());
                    if (rgb != null) {
                        metadata.rgb = rgb;
                    }
                }
                if (!(style instanceof StepSurfaceStyleUsage usage)) {
                    continue;
                }
                StepSurfaceSideStyle sideStyle = usage.style();
                for (StepEntity sideComponent : sideStyle.styles()) {
                    if (sideComponent instanceof StepSurfaceStyleFillArea surfaceFill) {
                        StepFillAreaStyle fillStyle = surfaceFill.fillStyle();
                        for (StepFillAreaStyleColour fillColour : fillStyle.styles()) {
                            int[] rgb = colourToRgb(fillColour.colour());
                            if (rgb != null) {
                                metadata.rgb = rgb;
                            }
                        }
                    } else if (sideComponent instanceof StepSurfaceStyleTransparent transparent) {
                        metadata.transparency = clamp01(transparent.transparency());
                    } else if (sideComponent instanceof StepSurfaceStyleRendering rendering) {
                        metadata.transparency = clamp01(rendering.transparency());
                        if (metadata.pbr == null) {
                            metadata.pbr = new PbrMetadata(
                                    rendering.diffuseReflection(),
                                    rendering.specularReflection(),
                                    null, null
                            );
                        }
                        // Rendering can also reference a fill area for color
                        if (rendering.surfaceStyle() instanceof StepSurfaceStyleFillArea renderingFill) {
                            StepFillAreaStyle fillStyle = renderingFill.fillStyle();
                            for (StepFillAreaStyleColour fillColour : fillStyle.styles()) {
                                int[] rgb = colourToRgb(fillColour.colour());
                                if (rgb != null) {
                                    metadata.rgb = rgb;
                                }
                            }
                        }
                    } else if (sideComponent instanceof StepSurfaceStyleReflectanceAmbientDiffuseSpecular reflectance) {
                        metadata.pbr = new PbrMetadata(
                                reflectance.diffuseReflectance(),
                                reflectance.specularReflectance(),
                                reflectance.specularExponent(),
                                colourToRgb(reflectance.specularColour())
                        );
                    } else if (sideComponent instanceof StepSurfaceStyleReflectanceAmbientDiffuse reflectance) {
                        if (metadata.pbr == null) {
                            metadata.pbr = new PbrMetadata(
                                    reflectance.diffuseReflectance(),
                                    0.0,
                                    null,
                                    null
                            );
                        }
                    } else if (sideComponent instanceof StepSurfaceStyleReflectanceAmbient reflectance) {
                        if (metadata.pbr == null) {
                            metadata.pbr = new PbrMetadata(
                                    reflectance.ambientReflectance(),
                                    0.0,
                                    null,
                                    null
                            );
                        }
                    }
                }
            }
        }
    }

    private static int[] colourToRgb(StepEntity colour) {
        if (colour == null) return null;
        if (colour instanceof StepColourRgb rgb) {
            return new int[]{
                    toChannel(rgb.red()),
                    toChannel(rgb.green()),
                    toChannel(rgb.blue())
            };
        }
        if (colour instanceof StepDraughtingPreDefinedColour predefined) {
            return switch (predefined.name().toLowerCase()) {
                case "blue" -> new int[]{0, 0, 255};
                case "red" -> new int[]{255, 0, 0};
                case "green" -> new int[]{0, 128, 0};
                case "yellow" -> new int[]{255, 255, 0};
                case "black" -> new int[]{0, 0, 0};
                case "white" -> new int[]{255, 255, 255};
                case "cyan" -> new int[]{0, 255, 255};
                case "magenta" -> new int[]{255, 0, 255};
                case "orange" -> new int[]{255, 165, 0};
                case "brown" -> new int[]{165, 42, 42};
                case "pink" -> new int[]{255, 192, 203};
                case "grey", "gray" -> new int[]{128, 128, 128};
                case "purple", "violet" -> new int[]{128, 0, 128};
                default -> null;
            };
        }
        return null;
    }

    private static double clamp01(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }

    private static int toChannel(double value) {
        return (int) Math.max(0, Math.min(255, Math.round(value * 255.0)));
    }

    public record DisplayMetadata(
            int[] rgb,
            List<String> layers,
            double transparency,
            PbrMetadata pbr
    ) {
        static final DisplayMetadata EMPTY = new DisplayMetadata(null, List.of(), 0.0, null);

        public DisplayMetadata {
            layers = List.copyOf(layers);
        }
    }

    /**
     * PBR material metadata extracted from STEP surface styling.
     *
     * @param diffuse diffuse reflection factor (0-1)
     * @param specular specular reflection factor (0-1)
     * @param specularExponent glossiness exponent (higher = shinier)
     * @param specularColor specular color tint, or null for white
     */
    public record PbrMetadata(
            double diffuse,
            double specular,
            Double specularExponent,
            int[] specularColor
    ) {}

    private static final class MutableMetadata {
        private int[] rgb;
        private final Set<String> layers = new LinkedHashSet<>();
        private double transparency = 0.0;
        private PbrMetadata pbr = null;
    }
}
