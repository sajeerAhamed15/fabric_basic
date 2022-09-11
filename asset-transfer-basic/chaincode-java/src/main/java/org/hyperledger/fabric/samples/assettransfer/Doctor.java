/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public final class Doctor {

    @Property()
    private final String doctorID;

    @Property()
    private final String name;

    @Property()
    private final String hospitalName;

    @Property()
    private final String regNumber;

    @Property()
    private final String contactNumber;

    @Property()
    private final String address;

    public String getDoctorID() {
        return doctorID;
    }

    public String getName() {
        return name;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public Doctor(@JsonProperty("doctorID") final String doctorID,
                  @JsonProperty("name") final String name,
                  @JsonProperty("hospitalName") final String hospitalName,
                  @JsonProperty("regNumber") final String regNumber,
                  @JsonProperty("contactNumber") final String contactNumber,
                  @JsonProperty("address") final String address) {
        this.doctorID = doctorID;
        this.name = name;
        this.hospitalName = hospitalName;
        this.regNumber = regNumber;
        this.contactNumber = contactNumber;
        this.address = address;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        Doctor other = (Doctor) obj;

        return Objects.deepEquals(
                new String[] {getDoctorID(), getName(), getHospitalName(),
                        getRegNumber(), getContactNumber(), getAddress()},
                new String[] {other.getDoctorID(), other.getName(),
                        other.getHospitalName(), other.getRegNumber(),
                        other.getContactNumber(), other.getAddress()});
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDoctorID(), getName(), getHospitalName(),
                getRegNumber(), getContactNumber(), getAddress());
    }

    @Override
    public String toString() {
        String stringValue = " ["
                + "doctorID=" + doctorID + ", "
                + "name=" + name + ", "
                + "name=" + hospitalName + ", "
                + "name=" + regNumber + ", "
                + "contactNumber=" + contactNumber + ", "
                + "address=" + address
                + "]";
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + stringValue;
    }
}
