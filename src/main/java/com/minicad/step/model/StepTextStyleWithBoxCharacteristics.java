package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal TEXT_STYLE_WITH_BOX_CHARACTERISTICS.
 *
 * @param id STEP instance id
 * @param name style name
 * @param characterAppearance character appearance definition
 * @param boxCharacteristics raw box characteristic literals
 */
public final class StepTextStyleWithBoxCharacteristics extends AbstractStepEntity {
    private final StepEntity characterAppearance;
    private final List<String> boxCharacteristics;

    public StepTextStyleWithBoxCharacteristics(int id, String name, StepEntity characterAppearance, List<String> boxCharacteristics) {
        super(id, name);
        this.characterAppearance = characterAppearance;
        this.boxCharacteristics = boxCharacteristics == null ? null : java.util.List.copyOf(boxCharacteristics);
    }

    public StepEntity getCharacterAppearance() {
        return characterAppearance;
    }

    public List<String> getBoxCharacteristics() {
        return boxCharacteristics;
    }

    // Record-style accessor
    public StepEntity characterAppearance() {
        return characterAppearance;
    }

    public List<String> boxCharacteristics() {
        return boxCharacteristics;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("characterAppearance", characterAppearance);
        state.put("boxCharacteristics", boxCharacteristics);
        return state;
    }
}
