package com.fxmlspringtest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * @Author Anton Hellbe
 * A class that creates an RFID-key from input
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
public class RfidKey implements Serializable{

    private String id;
    private Boolean isEnabled = false;

    public RfidKey() {
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    /**
     * Constructor that creates a new RFID-key
     **/
    public RfidKey(String id) {
        this.id = id;
    }

    /**
     * Fetches the id of the RFID-key
     **/
    public String getId() {
        return id;
    }

    /**
     * Sets the id of the RFID-key
     * @param id the new id
     **/
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Formats the information of the Key to a String
     * @return the formatted information
     **/
    @Override
    public String toString() {
        return id;
    }

    /**
     * Checks if the RFID-Key is equal to another object
     * @param o the object to check if its equal
     * @return true or false
     **/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RfidKey rfidKey = (RfidKey) o;

        return id != null ? id.equals(rfidKey.id) : rfidKey.id == null;

    }

    /**
     * Returns the hashCode of the RFID-key if it exists
     * @return null or the hashCode
     **/
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}