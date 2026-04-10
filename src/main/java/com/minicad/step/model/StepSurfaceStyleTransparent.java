package com.minicad.step.model;

/**
 * Minimal SURFACE_STYLE_TRANSPARENT.
 *
 * @param id STEP instance id
 * @param transparency transparency factor
 */
public record StepSurfaceStyleTransparent(int id, double transparency) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
