package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PRODUCT_VERSION.
 * A product version entity.
 *
 * @param id STEP instance id
 * @param name version name
 * @param versionId version identifier
 * @param description version description
 * @param product relating product
 * @param versionContext version context information
 */
public final class StepProductVersion extends AbstractStepEntity {
    private final String versionId;
    private final String description;
    private final StepEntity product;
    private final StepEntity versionContext;

    public StepProductVersion(int id, String name, String versionId, String description, StepEntity product, StepEntity versionContext) {
        super(id, name);
        this.versionId = versionId;
        this.description = description;
        this.product = product;
        this.versionContext = versionContext;
    }

    public String getVersionId() {
        return versionId;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getProduct() {
        return product;
    }

    public StepEntity getVersionContext() {
        return versionContext;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("versionId", versionId);
        state.put("description", description);
        state.put("product", product);
        state.put("versionContext", versionContext);
        return state;
    }
}
