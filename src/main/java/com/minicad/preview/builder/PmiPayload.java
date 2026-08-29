package com.minicad.preview.builder;

import com.minicad.preview.payload.PointPayload;
import com.minicad.preview.payload.PreviewPayloadCopies;

import java.util.List;

/**
 * PMI payload for STEP preview export.
 */
public final class PmiPayload {
    private final String name;
    private final String text;
    private final PointPayload position;
    private final List<PointPayload> leader;
    private final List<Integer> targetIds;
    private final List<PmiTargetPayload> targets;

    public PmiPayload(String name, String text, PointPayload position, List<PointPayload> leader, List<Integer> targetIds, List<PmiTargetPayload> targets) {
        this.name = name;
        this.text = text;
        this.position = position;
        this.leader = PreviewPayloadCopies.copy(leader);
        this.targetIds = PreviewPayloadCopies.copy(targetIds);
        this.targets = PreviewPayloadCopies.copy(targets);
    }

    public String getName() { return name; }
    public String getText() { return text; }
    public PointPayload getPosition() { return position; }
    public List<PointPayload> getLeader() { return leader; }
    public List<Integer> getTargetIds() { return targetIds; }
    public List<PmiTargetPayload> getTargets() { return targets; }

    // Record-style accessors
    public String name() { return name; }
    public String text() { return text; }
    public PointPayload position() { return position; }
    public List<PointPayload> leader() { return leader; }
    public List<Integer> targetIds() { return targetIds; }
    public List<PmiTargetPayload> targets() { return targets; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PmiPayload)) return false;
        PmiPayload that = (PmiPayload) o;
        return java.util.Objects.equals(name, that.name) && java.util.Objects.equals(text, that.text) && java.util.Objects.equals(position, that.position) && java.util.Objects.equals(leader, that.leader) && java.util.Objects.equals(targetIds, that.targetIds) && java.util.Objects.equals(targets, that.targets);
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(name, text, position, leader, targetIds, targets);
    }

    @Override public String toString() {
        return "PmiPayload{" + "name=name, text=text, position=position, leader=leader, targetIds=targetIds, targets=targets" + "}";
    }
}
