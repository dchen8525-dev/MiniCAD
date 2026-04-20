package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;public record StepActionStatus(int id, String name, String description) implements StepEntity {}