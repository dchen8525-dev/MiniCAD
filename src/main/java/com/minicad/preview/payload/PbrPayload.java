package com.minicad.preview.payload;

import java.util.Arrays;

/**
 * PBR (Physically Based Rendering) material payload.
 */
public final class PbrPayload {
    private final double diffuse;
    private final double specular;
    private final Double specularExponent;
    private final int[] specularColor;

    public PbrPayload(double diffuse, double specular, Double specularExponent, int[] specularColor) {
        this.diffuse = diffuse;
        this.specular = specular;
        this.specularExponent = specularExponent;
        this.specularColor = specularColor != null ? specularColor.clone() : null;
    }

    public double getDiffuse() {
        return diffuse;
    }
    public double getSpecular() {
        return specular;
    }
    public Double getSpecularExponent() {
        return specularExponent;
    }
    public int[] getSpecularColor() {
        return specularColor;
    }

    // Record-style accessors
    public double diffuse() { return diffuse; }
    public double specular() { return specular; }
    public Double specularExponent() { return specularExponent; }
    public int[] specularColor() { return specularColor; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PbrPayload that = (PbrPayload) o;
        return Double.compare(that.diffuse, diffuse) == 0 && Double.compare(that.specular, specular) == 0 && java.util.Objects.equals(specularExponent, that.specularExponent) && Arrays.equals(specularColor, that.specularColor);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(Double.hashCode(diffuse), Double.hashCode(specular), specularExponent, Arrays.hashCode(specularColor));
    }

    @Override
    public String toString() {
        return "PbrPayload{diffuse=" + diffuse + ", specular=" + specular + ", specularExponent=" + specularExponent + ", specularColor=" + Arrays.toString(specularColor) + "}";
    }
}