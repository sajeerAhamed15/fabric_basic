/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public final class Patient {

    @Property()
    private final String patientID;

    @Property()
    private final String name;

    @Property()
    private final String address;

    @Property()
    private final String dob;

    @Property()
    private final String contactNumber;

    @Property()
    private final String emergencyContactNumber;

    public String getPatientID() {
        return patientID;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDob() {
        return dob;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    public Patient(@JsonProperty("patientID") final String patientID,
                   @JsonProperty("name") final String name,
                   @JsonProperty("address") final String address,
                   @JsonProperty("dob") final String dob,
                   @JsonProperty("contactNumber") final String contactNumber,
                   @JsonProperty("emergencyContactNumber") final String emergencyContactNumber) {
        this.patientID = patientID;
        this.name = name;
        this.address = address;
        this.dob = dob;
        this.contactNumber = contactNumber;
        this.emergencyContactNumber = emergencyContactNumber;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        Patient other = (Patient) obj;

        return Objects.deepEquals(
                new String[] {getPatientID(), getName(), getAddress(),
                        getDob(), getContactNumber(), getEmergencyContactNumber()},
                new String[] {other.getPatientID(), other.getName(),
                        other.getAddress(), other.getDob(),
                        other.getContactNumber(), other.getEmergencyContactNumber()});
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPatientID(), getName(), getAddress(), getDob(), getContactNumber(), getEmergencyContactNumber());
    }

    @Override
    public String toString() {
        String stringValue = " ["
                + "patientID=" + patientID + ", "
                + "name=" + name + ", "
                + "address=" + address + ", "
                + "dob=" + dob + ", "
                + "contactNumber=" + contactNumber + ", "
                + "emergencyContactNumber=" + emergencyContactNumber
                + "]";
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + stringValue;
    }
}
