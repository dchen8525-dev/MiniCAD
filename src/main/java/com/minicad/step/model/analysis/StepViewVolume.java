package com.minicad.step.model.analysis;

import com.minicad.step.model.base.StepEntity;public record StepViewVolume(int id, String name, double clippingBack, double clippingFront, double viewPlaneDistance, double viewPlaneWidth, double viewPlaneHeight) implements StepEntity {}