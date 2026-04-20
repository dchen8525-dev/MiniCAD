package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;public record StepGeneralizedDatum(int id, String name, String description, StepEntity datumTarget) implements StepEntity {}