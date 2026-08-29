package com.minicad.preview.payload;

import java.util.List;

/**
 * Surface payload for face geometry representation.
 */
public final class FaceSurfacePayload {
    private final String type;
    private final List<Double> center;
    private final List<Double> axis;
    private final List<Double> xDirection;
    private final double radius;
    private final Double minorRadius;
    private final Double semiAngle;
    private final double lowerHeight;
    private final double upperHeight;
    private final double startAngle;
    private final double sweepAngle;
    private final Integer uDegree;
    private final Integer vDegree;
    private final List<List<List<Double>>> controlPoints;
    private final List<Integer> uMultiplicities;
    private final List<Integer> vMultiplicities;
    private final List<Double> uKnots;
    private final List<Double> vKnots;
    private final String sourceType;
    private final Integer sourceStepId;
    private final String basisType;
    private final Integer basisStepId;
    private final Boolean orientation;
    private final Double offsetDistance;
    private final Double trimU1;
    private final Double trimU2;
    private final Double trimV1;
    private final Double trimV2;
    private final Boolean implicitOuter;
    private final Double transformScale;

    public FaceSurfacePayload(String type, List<Double> center, List<Double> axis, List<Double> xDirection, double radius, Double minorRadius, Double semiAngle, double lowerHeight, double upperHeight, double startAngle, double sweepAngle, Integer uDegree, Integer vDegree, List<List<List<Double>>> controlPoints, List<Integer> uMultiplicities, List<Integer> vMultiplicities, List<Double> uKnots, List<Double> vKnots, String sourceType, Integer sourceStepId, String basisType, Integer basisStepId, Boolean orientation, Double offsetDistance, Double trimU1, Double trimU2, Double trimV1, Double trimV2, Boolean implicitOuter, Double transformScale) {
        this.type = type;
        this.center = PreviewPayloadCopies.copy(center);
        this.axis = PreviewPayloadCopies.copy(axis);
        this.xDirection = PreviewPayloadCopies.copy(xDirection);
        this.radius = radius;
        this.minorRadius = minorRadius;
        this.semiAngle = semiAngle;
        this.lowerHeight = lowerHeight;
        this.upperHeight = upperHeight;
        this.startAngle = startAngle;
        this.sweepAngle = sweepAngle;
        this.uDegree = uDegree;
        this.vDegree = vDegree;
        this.controlPoints = PreviewPayloadCopies.copyControlPoints(controlPoints);
        this.uMultiplicities = PreviewPayloadCopies.copy(uMultiplicities);
        this.vMultiplicities = PreviewPayloadCopies.copy(vMultiplicities);
        this.uKnots = PreviewPayloadCopies.copy(uKnots);
        this.vKnots = PreviewPayloadCopies.copy(vKnots);
        this.sourceType = sourceType;
        this.sourceStepId = sourceStepId;
        this.basisType = basisType;
        this.basisStepId = basisStepId;
        this.orientation = orientation;
        this.offsetDistance = offsetDistance;
        this.trimU1 = trimU1;
        this.trimU2 = trimU2;
        this.trimV1 = trimV1;
        this.trimV2 = trimV2;
        this.implicitOuter = implicitOuter;
        this.transformScale = transformScale;
    }

    // Convenience constructor for basic surface types with 17 parameters
    public FaceSurfacePayload(String type, List<Double> center, List<Double> axis, List<Double> xDirection, double radius, Double minorRadius, Double semiAngle, double lowerHeight, double upperHeight, double startAngle, double sweepAngle, Integer uDegree, Integer vDegree, List<List<List<Double>>> controlPoints, List<Integer> uMultiplicities, List<Integer> vMultiplicities, List<Double> uKnots) {
        this(type, center, axis, xDirection, radius, minorRadius, semiAngle, lowerHeight, upperHeight, startAngle, sweepAngle, uDegree, vDegree, controlPoints, uMultiplicities, vMultiplicities, uKnots, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public String getType() { return type; }
    public List<Double> getCenter() { return center; }
    public List<Double> getAxis() { return axis; }
    public List<Double> getXDirection() { return xDirection; }
    public double getRadius() { return radius; }
    public Double getMinorRadius() { return minorRadius; }
    public Double getSemiAngle() { return semiAngle; }
    public double getLowerHeight() { return lowerHeight; }
    public double getUpperHeight() { return upperHeight; }
    public double getStartAngle() { return startAngle; }
    public double getSweepAngle() { return sweepAngle; }
    public Integer getUDegree() { return uDegree; }
    public Integer getVDegree() { return vDegree; }
    public List<List<List<Double>>> getControlPoints() { return controlPoints; }
    public List<Integer> getUMultiplicities() { return uMultiplicities; }
    public List<Integer> getVMultiplicities() { return vMultiplicities; }
    public List<Double> getUKnots() { return uKnots; }
    public List<Double> getVKnots() { return vKnots; }
    public String getSourceType() { return sourceType; }
    public Integer getSourceStepId() { return sourceStepId; }
    public String getBasisType() { return basisType; }
    public Integer getBasisStepId() { return basisStepId; }
    public Boolean getOrientation() { return orientation; }
    public Double getOffsetDistance() { return offsetDistance; }
    public Double getTrimU1() { return trimU1; }
    public Double getTrimU2() { return trimU2; }
    public Double getTrimV1() { return trimV1; }
    public Double getTrimV2() { return trimV2; }
    public Boolean getImplicitOuter() { return implicitOuter; }
    public Double getTransformScale() { return transformScale; }

    // Record-style accessors
    public String type() { return type; }
    public List<Double> center() { return center; }
    public List<Double> axis() { return axis; }
    public List<Double> xDirection() { return xDirection; }
    public double radius() { return radius; }
    public Double minorRadius() { return minorRadius; }
    public Double semiAngle() { return semiAngle; }
    public double lowerHeight() { return lowerHeight; }
    public double upperHeight() { return upperHeight; }
    public double startAngle() { return startAngle; }
    public double sweepAngle() { return sweepAngle; }
    public Integer uDegree() { return uDegree; }
    public Integer vDegree() { return vDegree; }
    public List<List<List<Double>>> controlPoints() { return controlPoints; }
    public List<Integer> uMultiplicities() { return uMultiplicities; }
    public List<Integer> vMultiplicities() { return vMultiplicities; }
    public List<Double> uKnots() { return uKnots; }
    public List<Double> vKnots() { return vKnots; }
    public String sourceType() { return sourceType; }
    public Integer sourceStepId() { return sourceStepId; }
    public String basisType() { return basisType; }
    public Integer basisStepId() { return basisStepId; }
    public Boolean orientation() { return orientation; }
    public Double offsetDistance() { return offsetDistance; }
    public Double trimU1() { return trimU1; }
    public Double trimU2() { return trimU2; }
    public Double trimV1() { return trimV1; }
    public Double trimV2() { return trimV2; }
    public Boolean implicitOuter() { return implicitOuter; }
    public Double transformScale() { return transformScale; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FaceSurfacePayload)) return false;
        FaceSurfacePayload that = (FaceSurfacePayload) o;
        return java.util.Objects.equals(type, that.type) && java.util.Objects.equals(center, that.center) && java.util.Objects.equals(axis, that.axis) && java.util.Objects.equals(xDirection, that.xDirection) && Double.compare(that.radius, radius) == 0 && java.util.Objects.equals(minorRadius, that.minorRadius) && java.util.Objects.equals(semiAngle, that.semiAngle) && Double.compare(that.lowerHeight, lowerHeight) == 0 && Double.compare(that.upperHeight, upperHeight) == 0 && Double.compare(that.startAngle, startAngle) == 0 && Double.compare(that.sweepAngle, sweepAngle) == 0 && java.util.Objects.equals(uDegree, that.uDegree) && java.util.Objects.equals(vDegree, that.vDegree) && java.util.Objects.equals(controlPoints, that.controlPoints) && java.util.Objects.equals(uMultiplicities, that.uMultiplicities) && java.util.Objects.equals(vMultiplicities, that.vMultiplicities) && java.util.Objects.equals(uKnots, that.uKnots) && java.util.Objects.equals(vKnots, that.vKnots) && java.util.Objects.equals(sourceType, that.sourceType) && java.util.Objects.equals(sourceStepId, that.sourceStepId) && java.util.Objects.equals(basisType, that.basisType) && java.util.Objects.equals(basisStepId, that.basisStepId) && java.util.Objects.equals(orientation, that.orientation) && java.util.Objects.equals(offsetDistance, that.offsetDistance) && java.util.Objects.equals(trimU1, that.trimU1) && java.util.Objects.equals(trimU2, that.trimU2) && java.util.Objects.equals(trimV1, that.trimV1) && java.util.Objects.equals(trimV2, that.trimV2) && java.util.Objects.equals(implicitOuter, that.implicitOuter) && java.util.Objects.equals(transformScale, that.transformScale);
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(type, center, axis, xDirection, Double.hashCode(radius), minorRadius, semiAngle, Double.hashCode(lowerHeight), Double.hashCode(upperHeight), Double.hashCode(startAngle), Double.hashCode(sweepAngle), uDegree, vDegree, controlPoints, uMultiplicities, vMultiplicities, uKnots, vKnots, sourceType, sourceStepId, basisType, basisStepId, orientation, offsetDistance, trimU1, trimU2, trimV1, trimV2, implicitOuter, transformScale);
    }

    @Override public String toString() {
        return "FaceSurfacePayload{" + "type=type, center=center, axis=axis, xDirection=xDirection, radius=radius, minorRadius=minorRadius, semiAngle=semiAngle, lowerHeight=lowerHeight, upperHeight=upperHeight, startAngle=startAngle, sweepAngle=sweepAngle, uDegree=uDegree, vDegree=vDegree, controlPoints=controlPoints, uMultiplicities=uMultiplicities, vMultiplicities=vMultiplicities, uKnots=uKnots, vKnots=vKnots, sourceType=sourceType, sourceStepId=sourceStepId, basisType=basisType, basisStepId=basisStepId, orientation=orientation, offsetDistance=offsetDistance, trimU1=trimU1, trimU2=trimU2, trimV1=trimV1, trimV2=trimV2, implicitOuter=implicitOuter, transformScale=transformScale" + "}";
    }
}
