package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved ELEMENT_VOLUME_2D.
 * A 2D finite element volume (shell/plate element).
 */
public record StepElementVolume2d(
    int id,
    String name,
    List<StepEntity> nodes,
    String elementType
) implements StepEntity {

    public StepElementVolume2d {
        nodes = List.copyOf(nodes);
    }
}
