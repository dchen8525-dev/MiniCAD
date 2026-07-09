package com.minicad.helper.metadata;

import com.minicad.step.model.StepColourRgb;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepCurveStyle;
import com.minicad.step.model.StepCurveStyleRendering;
import com.minicad.step.model.StepCurveStyleWithFont;
import com.minicad.step.model.StepFillAreaStyle;
import com.minicad.step.model.StepFillAreaStyleColour;
import com.minicad.step.model.StepDraughtingPreDefinedColour;
import com.minicad.step.model.StepOverRidingStyledItem;
import com.minicad.step.model.StepPresentationLayerAssignment;
import com.minicad.step.model.StepPresentationStyleAssignment;
import com.minicad.step.model.StepPreDefinedSurfaceStyle;
import com.minicad.step.model.StepStyledItem;
import com.minicad.step.model.StepSurfaceSideStyle;
import com.minicad.step.model.StepSurfaceStyleFillArea;
import com.minicad.step.model.StepSurfaceStyleParameterLines;
import com.minicad.step.model.StepSurfaceStyleRendering;
import com.minicad.step.model.StepSurfaceStyleReflectanceAmbient;
import com.minicad.step.model.StepSurfaceStyleReflectanceAmbientDiffuse;
import com.minicad.step.model.StepSurfaceStyleReflectanceAmbientDiffuseSpecular;
import com.minicad.step.model.StepSurfaceStyleTransparent;
import com.minicad.step.model.StepSurfaceStyleUsage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Objects;

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
            if (entity instanceof StepStyledItem) {
            StepStyledItem styledItem = (StepStyledItem) entity;
                MutableMetadata metadata = mutableByItemId.computeIfAbsent(styledItem.item().id(), ignored -> new MutableMetadata());
                extractStyle(styledItem, metadata);
            } else if (entity instanceof StepOverRidingStyledItem) {
            StepOverRidingStyledItem styledItem = (StepOverRidingStyledItem) entity;
                MutableMetadata metadata = mutableByItemId.computeIfAbsent(styledItem.item().id(), ignored -> new MutableMetadata());
                extractStyle(styledItem.styles(), metadata);
            } else if (entity instanceof StepPresentationLayerAssignment) {
            StepPresentationLayerAssignment layerAssignment = (StepPresentationLayerAssignment) entity;
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
                if (style instanceof StepCurveStyle) {
            StepCurveStyle curveStyle = (StepCurveStyle) style;
                    int[] rgb = colourToRgb(curveStyle.colour());
                    if (rgb != null) {
                        metadata.rgb = rgb;
                    }
                } else if (style instanceof StepCurveStyleRendering) {
            StepCurveStyleRendering curveRendering = (StepCurveStyleRendering) style;
                    int[] rgb = colourToRgb(curveRendering.colour());
                    if (rgb != null) {
                        metadata.rgb = rgb;
                    }
                    metadata.transparency = clamp01(curveRendering.transparency());
                } else if (style instanceof StepPreDefinedSurfaceStyle) {
            StepPreDefinedSurfaceStyle predefined = (StepPreDefinedSurfaceStyle) style;
                    int[] rgb = namedSurfaceColor(predefined.identifier());
                    if (rgb != null) {
                        metadata.rgb = rgb;
                    }
                } else if (style instanceof StepSurfaceStyleParameterLines) {
                    // Parameter lines visualization hint - no color data
                } else if (style instanceof StepCurveStyleWithFont) {
                    // Font styling for curves - no color data
                }
                if (!(style instanceof StepSurfaceStyleUsage)) {
                    continue;
                }
                StepSurfaceStyleUsage usage = (StepSurfaceStyleUsage) style;
                StepSurfaceSideStyle sideStyle = usage.style();
                for (StepEntity sideComponent : sideStyle.styles()) {
                    if (sideComponent instanceof StepSurfaceStyleFillArea) {
                        StepSurfaceStyleFillArea surfaceFill = (StepSurfaceStyleFillArea) sideComponent;
                        StepFillAreaStyle fillStyle = surfaceFill.fillStyle();
                        for (StepFillAreaStyleColour fillColour : fillStyle.styles()) {
                            int[] rgb = colourToRgb(fillColour.colour());
                            if (rgb != null) {
                                metadata.rgb = rgb;
                            }
                        }
                    } else if (sideComponent instanceof StepSurfaceStyleTransparent) {
            StepSurfaceStyleTransparent transparent = (StepSurfaceStyleTransparent) sideComponent;
                        metadata.transparency = clamp01(transparent.transparency());
                    } else if (sideComponent instanceof StepSurfaceStyleRendering) {
            StepSurfaceStyleRendering rendering = (StepSurfaceStyleRendering) sideComponent;
                        metadata.transparency = clamp01(rendering.transparency());
                        if (metadata.pbr == null) {
                            metadata.pbr = new PbrMetadata(
                                    rendering.diffuseReflection(),
                                    rendering.specularReflection(),
                                    null, null
                            );
                        }
                        // Rendering can also reference a fill area for color
                        if (rendering.surfaceStyle() instanceof StepSurfaceStyleFillArea) {
                            StepSurfaceStyleFillArea renderingFill = (StepSurfaceStyleFillArea) rendering.surfaceStyle();
                            StepFillAreaStyle fillStyle = renderingFill.fillStyle();
                            for (StepFillAreaStyleColour fillColour : fillStyle.styles()) {
                                int[] rgb = colourToRgb(fillColour.colour());
                                if (rgb != null) {
                                    metadata.rgb = rgb;
                                }
                            }
                        }
                    } else if (sideComponent instanceof StepSurfaceStyleReflectanceAmbientDiffuseSpecular) {
            StepSurfaceStyleReflectanceAmbientDiffuseSpecular reflectance = (StepSurfaceStyleReflectanceAmbientDiffuseSpecular) sideComponent;
                        metadata.pbr = new PbrMetadata(
                                reflectance.diffuseReflectance(),
                                reflectance.specularReflectance(),
                                reflectance.specularExponent(),
                                colourToRgb(reflectance.specularColour())
                        );
                    } else if (sideComponent instanceof StepSurfaceStyleReflectanceAmbientDiffuse) {
            StepSurfaceStyleReflectanceAmbientDiffuse reflectance = (StepSurfaceStyleReflectanceAmbientDiffuse) sideComponent;
                        if (metadata.pbr == null) {
                            metadata.pbr = new PbrMetadata(
                                    reflectance.diffuseReflectance(),
                                    0.0,
                                    null,
                                    null
                            );
                        }
                    } else if (sideComponent instanceof StepSurfaceStyleReflectanceAmbient) {
            StepSurfaceStyleReflectanceAmbient reflectance = (StepSurfaceStyleReflectanceAmbient) sideComponent;
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
        if (colour instanceof StepColourRgb) {
            StepColourRgb rgb = (StepColourRgb) colour;
            return new int[]{
                    toChannel(rgb.red()),
                    toChannel(rgb.green()),
                    toChannel(rgb.blue())
            };
        }
        if (colour instanceof StepDraughtingPreDefinedColour) {
            StepDraughtingPreDefinedColour predefined = (StepDraughtingPreDefinedColour) colour;
            return namedSurfaceColor(predefined.name());
        }
        return null;
    }

    private static int[] namedSurfaceColor(String name) {
        if (name == null) return null;
        String lowerName = name.toLowerCase();
        if (lowerName.equals("blue")) return new int[]{0, 0, 255};
        else if (lowerName.equals("red")) return new int[]{255, 0, 0};
        else if (lowerName.equals("green")) return new int[]{0, 128, 0};
        else if (lowerName.equals("yellow")) return new int[]{255, 255, 0};
        else if (lowerName.equals("black")) return new int[]{0, 0, 0};
        else if (lowerName.equals("white")) return new int[]{255, 255, 255};
        else if (lowerName.equals("cyan")) return new int[]{0, 255, 255};
        else if (lowerName.equals("magenta")) return new int[]{255, 0, 255};
        else if (lowerName.equals("orange")) return new int[]{255, 165, 0};
        else if (lowerName.equals("brown")) return new int[]{165, 42, 42};
        else if (lowerName.equals("pink")) return new int[]{255, 192, 203};
        else if (lowerName.equals("grey") || lowerName.equals("gray")) return new int[]{128, 128, 128};
        else if (lowerName.equals("purple") || lowerName.equals("violet")) return new int[]{128, 0, 128};
        else return null;
    }

    private static double clamp01(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }

    private static int toChannel(double value) {
        return (int) Math.max(0, Math.min(255, Math.round(value * 255.0)));
    }

    /**
     * PBR material metadata extracted from STEP surface styling.
     *
     * @param diffuse diffuse reflection factor (0-1)
     * @param specular specular reflection factor (0-1)
     * @param specularExponent glossiness exponent (higher = shinier)
     * @param specularColor specular color tint, or null for white
     */
public static final class DisplayMetadata {
    public static final DisplayMetadata EMPTY = new DisplayMetadata(null, List.of(), 0.0, null);
    private final int[] rgb;
    private final List<String> layers;
    private final double transparency;
    private final PbrMetadata pbr;

    public DisplayMetadata(int[] rgb, List<String> layers, double transparency, PbrMetadata pbr) {
        this.rgb = rgb;
        this.layers = layers == null ? null : java.util.List.copyOf(layers);
        this.transparency = transparency;
        this.pbr = pbr;
    }

    public int[] getRgb() {
        return rgb;
    }

    public List<String> getLayers() {
        return layers;
    }

    public double getTransparency() {
        return transparency;
    }

    public PbrMetadata getPbr() {
        return pbr;
    }

    // Record-style accessors
    public int[] rgb() { return rgb; }
    public List<String> layers() { return layers; }
    public double transparency() { return transparency; }
    public PbrMetadata pbr() { return pbr; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisplayMetadata that = (DisplayMetadata) o;
        return Objects.equals(rgb, that.rgb) && Objects.equals(layers, that.layers) && transparency == that.transparency && Objects.equals(pbr, that.pbr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rgb, layers, transparency, pbr);
    }

    @Override
    public String toString() {
        return "DisplayMetadata{" + "rgb=" + rgb + "layers=" + layers + "transparency=" + transparency + "pbr=" + pbr + "}";
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
    public static final class PbrMetadata {
        private final double diffuse;
        private final double specular;
        private final Double specularExponent;
        private final int[] specularColor;

        public PbrMetadata(double diffuse, double specular, Double specularExponent, int[] specularColor) {
            this.diffuse = diffuse;
            this.specular = specular;
            this.specularExponent = specularExponent;
            this.specularColor = specularColor == null ? null : specularColor.clone();
        }

        public double diffuse() { return diffuse; }
        public double specular() { return specular; }
        public Double specularExponent() { return specularExponent; }
        public int[] specularColor() { return specularColor == null ? null : specularColor.clone(); }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PbrMetadata that = (PbrMetadata) o;
            return Double.compare(diffuse, that.diffuse) == 0 && Double.compare(specular, that.specular) == 0
                    && Objects.equals(specularExponent, that.specularExponent)
                    && java.util.Arrays.equals(specularColor, that.specularColor);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(diffuse, specular, specularExponent);
            result = 31 * result + java.util.Arrays.hashCode(specularColor);
            return result;
        }

        @Override
        public String toString() {
            return "PbrMetadata{diffuse=" + diffuse + ", specular=" + specular
                    + ", specularExponent=" + specularExponent + ", specularColor=" + java.util.Arrays.toString(specularColor) + "}";
        }
    }

    private static final class MutableMetadata {
        private int[] rgb;
        private final Set<String> layers = new LinkedHashSet<>();
        private double transparency = 0.0;
        private PbrMetadata pbr = null;
    }
}
