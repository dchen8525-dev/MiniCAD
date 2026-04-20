package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

public record StepFeaVolumeElementProperty(int id, String name, List<StepEntity> properties, StepEntity material) implements StepEntity {
    public StepFeaVolumeElementProperty { properties = List.copyOf(properties); }
}
