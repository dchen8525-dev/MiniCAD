package com.minicad.step.model.organization;

import com.minicad.step.model.base.StepEntity;public record StepPersonAddress(int id, String name, StepEntity person, StepEntity address) implements StepEntity {}