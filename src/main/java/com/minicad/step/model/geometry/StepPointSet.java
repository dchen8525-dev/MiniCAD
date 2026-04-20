package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Minimal point set.
 *
 * @param id STEP instance id
 * @param name set name
 * @param points point elements
 */
public record StepPointSet(int id, String name, List<StepEntity> points) implements StepEntity {

    public StepPointSet {
        points = List.copyOf(points);
    }
}
