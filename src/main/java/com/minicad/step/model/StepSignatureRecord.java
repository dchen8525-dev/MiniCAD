package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SIGNATURE_RECORD.
 * A signature record entity.
 *
 * @param id STEP instance id
 * @param name signature name
 * @param signatureType signature variance type
 * @param signatureValue signature variance value/hash
 * @param signatureSigner signature variance signer reference
 * @param signatureTimestamp signature variance timestamp
 * @param signatureValid signature variance valid flag
 * @param signatureStatus signature variance status
 */
public final class StepSignatureRecord extends AbstractStepEntity {
    private final String signatureType;
    private final String signatureValue;
    private final StepEntity signatureSigner;
    private final StepEntity signatureTimestamp;
    private final boolean signatureValid;
    private final String signatureStatus;

    public StepSignatureRecord(int id, String name, String signatureType, String signatureValue, StepEntity signatureSigner, StepEntity signatureTimestamp, boolean signatureValid, String signatureStatus) {
        super(id, name);
        this.signatureType = signatureType;
        this.signatureValue = signatureValue;
        this.signatureSigner = signatureSigner;
        this.signatureTimestamp = signatureTimestamp;
        this.signatureValid = signatureValid;
        this.signatureStatus = signatureStatus;
    }

    public String getSignatureType() {
        return signatureType;
    }

    public String getSignatureValue() {
        return signatureValue;
    }

    public StepEntity getSignatureSigner() {
        return signatureSigner;
    }

    public StepEntity getSignatureTimestamp() {
        return signatureTimestamp;
    }

    public boolean isSignatureValid() {
        return signatureValid;
    }

    public String getSignatureStatus() {
        return signatureStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("signatureType", signatureType);
        state.put("signatureValue", signatureValue);
        state.put("signatureSigner", signatureSigner);
        state.put("signatureTimestamp", signatureTimestamp);
        state.put("signatureValid", signatureValid);
        state.put("signatureStatus", signatureStatus);
        return state;
    }
}
