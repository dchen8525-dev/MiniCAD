package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepSignatureRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String signatureType;
    private final String signatureValue;
    private final StepEntity signatureSigner;
    private final StepEntity signatureTimestamp;
    private final boolean signatureValid;
    private final String signatureStatus;

    public StepSignatureRecord(int id, String name, String signatureType, String signatureValue, StepEntity signatureSigner, StepEntity signatureTimestamp, boolean signatureValid, String signatureStatus) {
        this.id = id;
        this.name = name;
        this.signatureType = signatureType;
        this.signatureValue = signatureValue;
        this.signatureSigner = signatureSigner;
        this.signatureTimestamp = signatureTimestamp;
        this.signatureValid = signatureValid;
        this.signatureStatus = signatureStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSignatureRecord that = (StepSignatureRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(signatureType, that.signatureType) && Objects.equals(signatureValue, that.signatureValue) && Objects.equals(signatureSigner, that.signatureSigner) && Objects.equals(signatureTimestamp, that.signatureTimestamp) && signatureValid == that.signatureValid && Objects.equals(signatureStatus, that.signatureStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, signatureType, signatureValue, signatureSigner, signatureTimestamp, signatureValid, signatureStatus);
    }

    @Override
    public String toString() {
        return "StepSignatureRecord{" + "id=" + id + "name=" + name + "signatureType=" + signatureType + "signatureValue=" + signatureValue + "signatureSigner=" + signatureSigner + "signatureTimestamp=" + signatureTimestamp + "signatureValid=" + signatureValid + "signatureStatus=" + signatureStatus + "}";
    }
}