package com.minicad.preview.payload;

import java.util.List;

/**
 * Edge curve payload for curve geometry representation.
 */
public final class EdgeCurvePayload {
    private final int stepId;
    private final String type;
    private final String basisType;
    private final Integer basisStepId;
    private final List<Double> center;
    private final List<Double> axis;
    private final List<Double> xDirection;
    private final Double radius;
    private final Double semiAxis1;
    private final Double semiAxis2;
    private final Boolean orientation;
    private final Boolean senseAgreement;
    private final Double offsetDistance;
    private final Boolean selfIntersect;
    private final List<Double> refDirection;
    private final Double transformScale;
    private final String masterRepresentation;
    private final List<String> associatedSurfaceTypes;
    private final List<Integer> associatedSurfaceStepIds;
    private final String sourceType;
    private final Integer sourceStepId;
    private final double startAngle;
    private final double sweepAngle;

    public EdgeCurvePayload(int stepId, String type, String basisType, Integer basisStepId, List<Double> center, List<Double> axis, List<Double> xDirection, Double radius, Double semiAxis1, Double semiAxis2, Boolean orientation, Boolean senseAgreement, Double offsetDistance, Boolean selfIntersect, List<Double> refDirection, Double transformScale, String masterRepresentation, List<String> associatedSurfaceTypes, List<Integer> associatedSurfaceStepIds, String sourceType, Integer sourceStepId, double startAngle, double sweepAngle) {
        this.stepId = stepId;
        this.type = type;
        this.basisType = basisType;
        this.basisStepId = basisStepId;
        this.center = PreviewPayloadCopies.copy(center);
        this.axis = PreviewPayloadCopies.copy(axis);
        this.xDirection = PreviewPayloadCopies.copy(xDirection);
        this.radius = radius;
        this.semiAxis1 = semiAxis1;
        this.semiAxis2 = semiAxis2;
        this.orientation = orientation;
        this.senseAgreement = senseAgreement;
        this.offsetDistance = offsetDistance;
        this.selfIntersect = selfIntersect;
        this.refDirection = PreviewPayloadCopies.copy(refDirection);
        this.transformScale = transformScale;
        this.masterRepresentation = masterRepresentation;
        this.associatedSurfaceTypes = PreviewPayloadCopies.copy(associatedSurfaceTypes);
        this.associatedSurfaceStepIds = PreviewPayloadCopies.copy(associatedSurfaceStepIds);
        this.sourceType = sourceType;
        this.sourceStepId = sourceStepId;
        this.startAngle = startAngle;
        this.sweepAngle = sweepAngle;
    }

    public int getStepId() { return stepId; }
    public String getType() { return type; }
    public String getBasisType() { return basisType; }
    public Integer getBasisStepId() { return basisStepId; }
    public List<Double> getCenter() { return center; }
    public List<Double> getAxis() { return axis; }
    public List<Double> getXDirection() { return xDirection; }
    public Double getRadius() { return radius; }
    public Double getSemiAxis1() { return semiAxis1; }
    public Double getSemiAxis2() { return semiAxis2; }
    public Boolean getOrientation() { return orientation; }
    public Boolean getSenseAgreement() { return senseAgreement; }
    public Double getOffsetDistance() { return offsetDistance; }
    public Boolean getSelfIntersect() { return selfIntersect; }
    public List<Double> getRefDirection() { return refDirection; }
    public Double getTransformScale() { return transformScale; }
    public String getMasterRepresentation() { return masterRepresentation; }
    public List<String> getAssociatedSurfaceTypes() { return associatedSurfaceTypes; }
    public List<Integer> getAssociatedSurfaceStepIds() { return associatedSurfaceStepIds; }
    public String getSourceType() { return sourceType; }
    public Integer getSourceStepId() { return sourceStepId; }
    public double getStartAngle() { return startAngle; }
    public double getSweepAngle() { return sweepAngle; }

    // Record-style accessors
    public int stepId() { return stepId; }
    public String type() { return type; }
    public String basisType() { return basisType; }
    public Integer basisStepId() { return basisStepId; }
    public List<Double> center() { return center; }
    public List<Double> axis() { return axis; }
    public List<Double> xDirection() { return xDirection; }
    public Double radius() { return radius; }
    public Double semiAxis1() { return semiAxis1; }
    public Double semiAxis2() { return semiAxis2; }
    public Boolean orientation() { return orientation; }
    public Boolean senseAgreement() { return senseAgreement; }
    public Double offsetDistance() { return offsetDistance; }
    public Boolean selfIntersect() { return selfIntersect; }
    public List<Double> refDirection() { return refDirection; }
    public Double transformScale() { return transformScale; }
    public String masterRepresentation() { return masterRepresentation; }
    public List<String> associatedSurfaceTypes() { return associatedSurfaceTypes; }
    public List<Integer> associatedSurfaceStepIds() { return associatedSurfaceStepIds; }
    public String sourceType() { return sourceType; }
    public Integer sourceStepId() { return sourceStepId; }
    public double startAngle() { return startAngle; }
    public double sweepAngle() { return sweepAngle; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EdgeCurvePayload)) return false;
        EdgeCurvePayload that = (EdgeCurvePayload) o;
        return stepId == that.stepId && java.util.Objects.equals(type, that.type) && java.util.Objects.equals(basisType, that.basisType) && java.util.Objects.equals(basisStepId, that.basisStepId) && java.util.Objects.equals(center, that.center) && java.util.Objects.equals(axis, that.axis) && java.util.Objects.equals(xDirection, that.xDirection) && java.util.Objects.equals(radius, that.radius) && java.util.Objects.equals(semiAxis1, that.semiAxis1) && java.util.Objects.equals(semiAxis2, that.semiAxis2) && java.util.Objects.equals(orientation, that.orientation) && java.util.Objects.equals(senseAgreement, that.senseAgreement) && java.util.Objects.equals(offsetDistance, that.offsetDistance) && java.util.Objects.equals(selfIntersect, that.selfIntersect) && java.util.Objects.equals(refDirection, that.refDirection) && java.util.Objects.equals(transformScale, that.transformScale) && java.util.Objects.equals(masterRepresentation, that.masterRepresentation) && java.util.Objects.equals(associatedSurfaceTypes, that.associatedSurfaceTypes) && java.util.Objects.equals(associatedSurfaceStepIds, that.associatedSurfaceStepIds) && java.util.Objects.equals(sourceType, that.sourceType) && java.util.Objects.equals(sourceStepId, that.sourceStepId) && Double.compare(that.startAngle, startAngle) == 0 && Double.compare(that.sweepAngle, sweepAngle) == 0;
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(stepId, type, basisType, basisStepId, center, axis, xDirection, radius, semiAxis1, semiAxis2, orientation, senseAgreement, offsetDistance, selfIntersect, refDirection, transformScale, masterRepresentation, associatedSurfaceTypes, associatedSurfaceStepIds, sourceType, sourceStepId, Double.hashCode(startAngle), Double.hashCode(sweepAngle));
    }

    @Override public String toString() {
        return "EdgeCurvePayload{" + "stepId=stepId, type=type, basisType=basisType, basisStepId=basisStepId, center=center, axis=axis, xDirection=xDirection, radius=radius, semiAxis1=semiAxis1, semiAxis2=semiAxis2, orientation=orientation, senseAgreement=senseAgreement, offsetDistance=offsetDistance, selfIntersect=selfIntersect, refDirection=refDirection, transformScale=transformScale, masterRepresentation=masterRepresentation, associatedSurfaceTypes=associatedSurfaceTypes, associatedSurfaceStepIds=associatedSurfaceStepIds, sourceType=sourceType, sourceStepId=sourceStepId, startAngle=startAngle, sweepAngle=sweepAngle" + "}";
    }
}
