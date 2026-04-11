package com.minicad.step.model;

/**
 * Minimal ADDRESS metadata.
 *
 * @param id STEP instance id
 * @param internalLocation internal location
 * @param streetNumber street number
 * @param street street
 * @param postalBox postal box
 * @param town town
 * @param region region
 * @param postalCode postal code
 * @param country country
 * @param facsimileNumber facsimile number
 * @param telephoneNumber telephone number
 * @param electronicMailAddress email address
 * @param telexNumber telex number
 */
public record StepAddress(
        int id,
        String internalLocation,
        String streetNumber,
        String street,
        String postalBox,
        String town,
        String region,
        String postalCode,
        String country,
        String facsimileNumber,
        String telephoneNumber,
        String electronicMailAddress,
        String telexNumber
) implements StepEntity {

    @Override
    public String name() {
        return town;
    }
}
