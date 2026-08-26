package com.minicad.export.json;

import java.util.*;
import com.minicad.helper.StepMetadataExtractor;
import com.minicad.step.model.*;
import com.minicad.topology.*;
import com.minicad.common.StepResolutionException;
import com.minicad.geometry.*;
import com.minicad.step.model.StepOrientedFace;

/**
 * Shared metadata/point helpers for STEP payload building.
 */
public final class StepMetadataHelper {
    private StepMetadataHelper() {}


    public static String faceDisplayName(StepFaceEntity stepFace) {
        if (stepFace instanceof StepOrientedFace) {
            StepOrientedFace orientedFace = (StepOrientedFace) stepFace;
            return StepMetadataHelper.faceDisplayName(orientedFace.faceElement());
        }
        return stepFace.name();
    }


    public static StepMetadataExtractor.DisplayMetadata mergeMetadata(
            StepMetadataExtractor.DisplayMetadata inherited,
            StepMetadataExtractor.DisplayMetadata direct
    ) {
        StepMetadataExtractor.DisplayMetadata left = inherited == null ? StepMetadataExtractor.DisplayMetadata.EMPTY : inherited;
        StepMetadataExtractor.DisplayMetadata right = direct == null ? StepMetadataExtractor.DisplayMetadata.EMPTY : direct;
        int[] rgb = right.rgb() != null ? right.rgb() : left.rgb();
        Set<String> layers = new LinkedHashSet<>(left.layers());
        layers.addAll(right.layers());
        double transparency = right.transparency() > 0 ? right.transparency() : left.transparency();
        StepMetadataExtractor.PbrMetadata pbr = right.pbr() != null ? right.pbr() : left.pbr();
        return new StepMetadataExtractor.DisplayMetadata(rgb, List.copyOf(layers), transparency, pbr);
    }

}
