package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved NODE_SET.
 * A named set of finite element nodes.
 */
public record StepNodeSet(
    int id,
    String name,
    List<StepEntity> nodes
) implements StepEntity {

    public StepNodeSet {
        nodes = List.copyOf(nodes);
    }
}
