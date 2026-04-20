package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Minimal OVER_RIDING_STYLED_ITEM.
 *
 * @param id step id
 * @param name style label
 * @param styles overriding style assignments
 * @param item styled target
 * @param overRiddenStyle referenced base styled item
 */
public record StepOverRidingStyledItem(
        int id,
        String name,
        List<StepPresentationStyleAssignment> styles,
        StepEntity item,
        StepStyledItem overRiddenStyle
) implements StepEntity {

    public StepOverRidingStyledItem {
        styles = List.copyOf(styles);
    }
}
