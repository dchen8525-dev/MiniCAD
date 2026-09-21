package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

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
public final class StepAddress extends AbstractStepEntity {
    private final String internalLocation;
    private final String streetNumber;
    private final String street;
    private final String postalBox;
    private final String town;
    private final String region;
    private final String postalCode;
    private final String country;
    private final String facsimileNumber;
    private final String telephoneNumber;
    private final String electronicMailAddress;
    private final String telexNumber;

    public StepAddress(int id, String internalLocation, String streetNumber, String street, String postalBox, String town, String region, String postalCode, String country, String facsimileNumber, String telephoneNumber, String electronicMailAddress, String telexNumber) {
        super(id, "");
        this.internalLocation = internalLocation;
        this.streetNumber = streetNumber;
        this.street = street;
        this.postalBox = postalBox;
        this.town = town;
        this.region = region;
        this.postalCode = postalCode;
        this.country = country;
        this.facsimileNumber = facsimileNumber;
        this.telephoneNumber = telephoneNumber;
        this.electronicMailAddress = electronicMailAddress;
        this.telexNumber = telexNumber;
    }

    public String getName() {
        return internalLocation != null ? internalLocation : "";
    }

    public String getInternalLocation() {
        return internalLocation;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getStreet() {
        return street;
    }

    public String getPostalBox() {
        return postalBox;
    }

    public String getTown() {
        return town;
    }

    public String getRegion() {
        return region;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public String getFacsimileNumber() {
        return facsimileNumber;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getElectronicMailAddress() {
        return electronicMailAddress;
    }

    public String getTelexNumber() {
        return telexNumber;
    }

    // Record-style accessors
    public String town() { return getTown(); }
    public String electronicMailAddress() { return getElectronicMailAddress(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("internalLocation", internalLocation);
        state.put("streetNumber", streetNumber);
        state.put("street", street);
        state.put("postalBox", postalBox);
        state.put("town", town);
        state.put("region", region);
        state.put("postalCode", postalCode);
        state.put("country", country);
        state.put("facsimileNumber", facsimileNumber);
        state.put("telephoneNumber", telephoneNumber);
        state.put("electronicMailAddress", electronicMailAddress);
        state.put("telexNumber", telexNumber);
        return state;
    }
}
