/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public final class Prescription {

    @Property()
    private final String prescriptionID;

    @Property()
    private final String patientID;

    @Property()
    private final String doctorID;

    @Property()
    private final String date;

    @Property()
    private final String medicine;

    public String getPrescriptionID() {
        return prescriptionID;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public String getDate() {
        return date;
    }

    public String getMedicine() {
        return medicine;
    }

    public Prescription(@JsonProperty("prescriptionID") final String prescriptionID,
                        @JsonProperty("patientID") final String patientID,
                        @JsonProperty("doctorID") final String doctorID,
                        @JsonProperty("date") final String date,
                        @JsonProperty("medicine") final String medicine) {
        this.prescriptionID = prescriptionID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.date = date;
        this.medicine = medicine;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        Prescription other = (Prescription) obj;

        return Objects.deepEquals(
                new String[] {getPrescriptionID(), getPatientID(), getDoctorID(), getDate(), getMedicine()},
                new String[] {other.getPrescriptionID(), other.getPatientID(), other.getDoctorID(), other.getDate(), other.getMedicine()});
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPrescriptionID(), getPatientID(), getDoctorID(), getDate(), getMedicine());
    }

    @Override
    public String toString() {
        String stringValue = " ["
                + "prescriptionID=" + prescriptionID + ", "
                + "name=" + patientID + ", "
                + "address=" + doctorID + ", "
                + "dob=" + date + ", "
                + "medicine=" + medicine
                + "]";
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + stringValue;
    }
}
