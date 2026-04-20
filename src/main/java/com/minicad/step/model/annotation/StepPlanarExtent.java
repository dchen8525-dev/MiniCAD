package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;public record StepPlanarExtent(int id, String name, double width, double height) implements StepEntity {}