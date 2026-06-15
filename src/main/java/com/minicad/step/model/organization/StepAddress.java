package com.minicad.step.model.organization;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
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
public final class StepAddress implements StepEntity {
    private final int id;
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
        this.id = id;
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

    public int getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAddress that = (StepAddress) o;
        return id == that.id && Objects.equals(internalLocation, that.internalLocation) && Objects.equals(streetNumber, that.streetNumber) && Objects.equals(street, that.street) && Objects.equals(postalBox, that.postalBox) && Objects.equals(town, that.town) && Objects.equals(region, that.region) && Objects.equals(postalCode, that.postalCode) && Objects.equals(country, that.country) && Objects.equals(facsimileNumber, that.facsimileNumber) && Objects.equals(telephoneNumber, that.telephoneNumber) && Objects.equals(electronicMailAddress, that.electronicMailAddress) && Objects.equals(telexNumber, that.telexNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, internalLocation, streetNumber, street, postalBox, town, region, postalCode, country, facsimileNumber, telephoneNumber, electronicMailAddress, telexNumber);
    }

    @Override
    public String toString() {
        return "StepAddress{" + "id=" + id + "internalLocation=" + internalLocation + "streetNumber=" + streetNumber + "street=" + street + "postalBox=" + postalBox + "town=" + town + "region=" + region + "postalCode=" + postalCode + "country=" + country + "facsimileNumber=" + facsimileNumber + "telephoneNumber=" + telephoneNumber + "electronicMailAddress=" + electronicMailAddress + "telexNumber=" + telexNumber + "}";
    }
}
