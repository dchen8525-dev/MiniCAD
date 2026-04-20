package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;public record StepKinematicStructure(int id, String name, String description, StepEntity mechanism) implements StepEntity {}