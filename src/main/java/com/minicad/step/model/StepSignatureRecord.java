package com.minicad.step.model;

import java.util.List;

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
public record StepSignatureRecord(
    int id,
    String name,
    String signatureType,
    String signatureValue,
    StepEntity signatureSigner,
    StepEntity signatureTimestamp,
    boolean signatureValid,
    String signatureStatus) implements StepEntity {
}