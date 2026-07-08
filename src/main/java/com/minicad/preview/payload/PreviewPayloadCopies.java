package com.minicad.preview.payload;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for copying payload data structures.
 */
public final class PreviewPayloadCopies {
    private PreviewPayloadCopies() {
    }

    public static <T> List<T> copy(List<T> values) {
        return values == null ? null : List.copyOf(values);
    }

    public static List<List<List<Double>>> copyControlPoints(List<List<List<Double>>> values) {
        if (values == null) {
            return null;
        }
        return values.stream()
                .map(plane -> plane == null ? null : plane.stream()
                        .map(row -> row == null ? null : List.copyOf(row))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    public static double[] copy(double[] values) {
        return values == null ? null : values.clone();
    }

    public static float[] copy(float[] values) {
        return values == null ? null : values.clone();
    }

    public static int[] copy(int[] values) {
        return values == null ? null : values.clone();
    }
}