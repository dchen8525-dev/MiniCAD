package com.minicad.preview.payload;

/**
 * UV bounds payload for STEP preview export.
 */
public final class UvBounds {
    private final double minU;
    private final double minV;
    private final double maxU;
    private final double maxV;

    public UvBounds(double minU, double minV, double maxU, double maxV) {
        this.minU = minU;
        this.minV = minV;
        this.maxU = maxU;
        this.maxV = maxV;
    }

    public double getMinU() { return minU; }
    public double getMinV() { return minV; }
    public double getMaxU() { return maxU; }
    public double getMaxV() { return maxV; }

    public double minU() { return minU; }
    public double minV() { return minV; }
    public double maxU() { return maxU; }
    public double maxV() { return maxV; }
    public double uSpan() { return maxU - minU; }
    public double vSpan() { return maxV - minV; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UvBounds that = (UvBounds) o;
        return Double.compare(that.minU, minU) == 0 && Double.compare(that.minV, minV) == 0 && Double.compare(that.maxU, maxU) == 0 && Double.compare(that.maxV, maxV) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(Double.hashCode(minU), Double.hashCode(minV), Double.hashCode(maxU), Double.hashCode(maxV));
    }

    @Override
    public String toString() {
        return "UvBounds{minU=" + minU + ", minV=" + minV + ", maxU=" + maxU + ", maxV=" + maxV + "}";
    }
}
