/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.assettransfer;

import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import java.util.ArrayList;
import java.util.List;

@Contract(
        name = "basic",
        info = @Info(
                title = "Medical Records",
                description = "Storing Medical Records",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "sajeer.mohammedriyaf2021@my.ntu.ac.uk",
                        name = "Sajeer")))
@Default
public final class AllContracts implements ContractInterface {

    private final Genson genson = new Genson();

    private enum AssetTransferErrors {
        ASSET_NOT_FOUND,
        ASSET_ALREADY_EXISTS
    }

    ///////////////// Prescription //////////////////

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Prescription CreatePrescription(final Context ctx, final String prescriptionID,
                                           final String patientID, final String doctorID,
                                           final String date, final String medicine) {
        ChaincodeStub stub = ctx.getStub();

        if (PrescriptionExists(ctx, prescriptionID)) {
            String errorMessage = String.format("Prescription %s already exists", prescriptionID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_ALREADY_EXISTS.toString());
        }

        Prescription prescription = new Prescription(prescriptionID, patientID, doctorID, date, medicine);

        String sortedJson = genson.serialize(prescription);
        stub.putStringState(prescriptionID, sortedJson);

        return prescription;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean PrescriptionExists(final Context ctx, final String prescriptionID) {
        ChaincodeStub stub = ctx.getStub();
        String json = stub.getStringState(prescriptionID);

        return (json != null && !json.isEmpty());
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Prescription ReadPrescription(final Context ctx, final String prescriptionID) {
        ChaincodeStub stub = ctx.getStub();
        String json = stub.getStringState(prescriptionID);

        if (json == null || json.isEmpty()) {
            String errorMessage = String.format("Prescription %s does not exist", prescriptionID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        Prescription prescription = genson.deserialize(json, Prescription.class);
        return prescription;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeletePrescription(final Context ctx, final String prescriptionID) {
        ChaincodeStub stub = ctx.getStub();

        if (!PrescriptionExists(ctx, prescriptionID)) {
            String errorMessage = String.format("Prescription %s does not exist", prescriptionID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        stub.delState(prescriptionID);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllPrescriptions(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        List<Prescription> queryResults = new ArrayList<Prescription>();

        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

        for (KeyValue result: results) {
            Prescription prescription = genson.deserialize(result.getStringValue(), Prescription.class);
            System.out.println(prescription);
            queryResults.add(prescription);
        }

        final String response = genson.serialize(queryResults);

        return response;
    }





    ///////////////// Doctor //////////////////

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Doctor CreateDoctor(final Context ctx, final String doctorID, final String name,
                               final String hospitalName, final String regNumber,
                               final String contactNumber, final String address) {
        ChaincodeStub stub = ctx.getStub();

        if (DoctorExists(ctx, doctorID)) {
            String errorMessage = String.format("Doctor %s already exists", doctorID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_ALREADY_EXISTS.toString());
        }

        Doctor doctor = new Doctor(doctorID, name, hospitalName, regNumber, contactNumber, address);

        String sortedJson = genson.serialize(doctor);
        stub.putStringState(doctorID, sortedJson);

        return doctor;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean DoctorExists(final Context ctx, final String doctorID) {
        ChaincodeStub stub = ctx.getStub();
        String json = stub.getStringState(doctorID);

        return (json != null && !json.isEmpty());
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Doctor ReadDoctor(final Context ctx, final String doctorID) {
        ChaincodeStub stub = ctx.getStub();
        String json = stub.getStringState(doctorID);

        if (json == null || json.isEmpty()) {
            String errorMessage = String.format("Doctor %s does not exist", doctorID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        Doctor doctor = genson.deserialize(json, Doctor.class);
        return doctor;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeleteDoctor(final Context ctx, final String doctorID) {
        ChaincodeStub stub = ctx.getStub();

        if (!DoctorExists(ctx, doctorID)) {
            String errorMessage = String.format("Doctor %s does not exist", doctorID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        stub.delState(doctorID);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllDoctors(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        List<Doctor> queryResults = new ArrayList<Doctor>();

        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

        for (KeyValue result: results) {
            Doctor doctor = genson.deserialize(result.getStringValue(), Doctor.class);
            System.out.println(doctor);
            queryResults.add(doctor);
        }

        final String response = genson.serialize(queryResults);

        return response;
    }






    ///////////////// Patient ///////////////////

    /**
     * Creates some initial patients on the ledger.
     *
     * @param ctx the transaction context
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedgerPatient(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        CreatePatient(ctx, "1", "John", "NG8 5BA", "12/12/1995", "07310941234", "");
        CreatePatient(ctx, "2", "Jim", "NG8 5BA", "12/12/1996", "07123441234", "");

    }

    /**
     * Creates a new patient on the ledger.
     *
     * @param ctx the transaction context
     * @param patientID the ID of the new patient
     * @param name the name of the new patient
     * @param address the address of the new patient
     * @param dob the date of birth of the new patient
     * @param contactNumber the contact number of the new patient
     * @param emergencyContactNumber the emergency contact number of the new patient
     * @return
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Patient CreatePatient(final Context ctx, final String patientID, final String name,
                                 final String address, final String dob, final String contactNumber,
                                 final String emergencyContactNumber) {
        ChaincodeStub stub = ctx.getStub();

        if (PatientExists(ctx, patientID)) {
            String errorMessage = String.format("Patient %s already exists", patientID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_ALREADY_EXISTS.toString());
        }

        Patient patient = new Patient(patientID, name, address, dob, contactNumber, emergencyContactNumber);

        String sortedJson = genson.serialize(patient);
        stub.putStringState(patientID, sortedJson);

        return patient;
    }

    /**
     * Retrieves a patient with the specified ID from the ledger.
     *
     * @param ctx the transaction context
     * @param patientID the ID of the patient
     * @return the patient found on the ledger if there was one
     */
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Patient ReadPatient(final Context ctx, final String patientID) {
        ChaincodeStub stub = ctx.getStub();
        String patientJSON = stub.getStringState(patientID);

        if (patientJSON == null || patientJSON.isEmpty()) {
            String errorMessage = String.format("Patient %s does not exist", patientID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        Patient patient = genson.deserialize(patientJSON, Patient.class);
        return patient;
    }

    /**
     * Deletes patient on the ledger.
     *
     * @param ctx the transaction context
     * @param patientID the ID of the patient being deleted
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeletePatient(final Context ctx, final String patientID) {
        ChaincodeStub stub = ctx.getStub();

        if (!PatientExists(ctx, patientID)) {
            String errorMessage = String.format("Patient %s does not exist", patientID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        stub.delState(patientID);
    }

    /**
     * Checks the existence of the patient on the ledger
     *
     * @param ctx the transaction context
     * @param patientID the ID of the patient
     * @return boolean indicating the existence of the patient
     */
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean PatientExists(final Context ctx, final String patientID) {
        ChaincodeStub stub = ctx.getStub();
        String patientJSON = stub.getStringState(patientID);

        return (patientJSON != null && !patientJSON.isEmpty());
    }


    /**
     * Retrieves all patients from the ledger.
     *
     * @param ctx the transaction context
     * @return array of patients found on the ledger
     */
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllPatients(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        List<Patient> queryResults = new ArrayList<Patient>();

        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

        for (KeyValue result: results) {
            Patient patient = genson.deserialize(result.getStringValue(), Patient.class);
            System.out.println(patient);
            queryResults.add(patient);
        }

        final String response = genson.serialize(queryResults);

        return response;
    }






    /////////////////////// Asset ///////////////////////








    /**
     * Creates some initial assets on the ledger.
     *
     * @param ctx the transaction context
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        CreateAsset(ctx, "asset1", "blue", 5, "Tomoko", 300);
        CreateAsset(ctx, "asset2", "red", 5, "Brad", 400);
        CreateAsset(ctx, "asset3", "green", 10, "Jin Soo", 500);
        CreateAsset(ctx, "asset4", "yellow", 10, "Max", 600);
        CreateAsset(ctx, "asset5", "black", 15, "Adrian", 700);
        CreateAsset(ctx, "asset6", "white", 15, "Michel", 700);

    }

    /**
     * Creates a new asset on the ledger.
     *
     * @param ctx the transaction context
     * @param assetID the ID of the new asset
     * @param color the color of the new asset
     * @param size the size for the new asset
     * @param owner the owner of the new asset
     * @param appraisedValue the appraisedValue of the new asset
     * @return the created asset
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Asset CreateAsset(final Context ctx, final String assetID, final String color, final int size,
                             final String owner, final int appraisedValue) {
        ChaincodeStub stub = ctx.getStub();

        if (AssetExists(ctx, assetID)) {
            String errorMessage = String.format("Asset %s already exists", assetID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_ALREADY_EXISTS.toString());
        }

        Asset asset = new Asset(assetID, color, size, owner, appraisedValue);
        //Use Genson to convert the Asset into string, sort it alphabetically and serialize it into a json string
        String sortedJson = genson.serialize(asset);
        stub.putStringState(assetID, sortedJson);

        return asset;
    }

    /**
     * Retrieves an asset with the specified ID from the ledger.
     *
     * @param ctx the transaction context
     * @param assetID the ID of the asset
     * @return the asset found on the ledger if there was one
     */
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Asset ReadAsset(final Context ctx, final String assetID) {
        ChaincodeStub stub = ctx.getStub();
        String assetJSON = stub.getStringState(assetID);

        if (assetJSON == null || assetJSON.isEmpty()) {
            String errorMessage = String.format("Asset %s does not exist", assetID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        Asset asset = genson.deserialize(assetJSON, Asset.class);
        return asset;
    }

    /**
     * Updates the properties of an asset on the ledger.
     *
     * @param ctx the transaction context
     * @param assetID the ID of the asset being updated
     * @param color the color of the asset being updated
     * @param size the size of the asset being updated
     * @param owner the owner of the asset being updated
     * @param appraisedValue the appraisedValue of the asset being updated
     * @return the transferred asset
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Asset UpdateAsset(final Context ctx, final String assetID, final String color, final int size,
                             final String owner, final int appraisedValue) {
        ChaincodeStub stub = ctx.getStub();

        if (!AssetExists(ctx, assetID)) {
            String errorMessage = String.format("Asset %s does not exist", assetID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        Asset newAsset = new Asset(assetID, color, size, owner, appraisedValue);
        //Use Genson to convert the Asset into string, sort it alphabetically and serialize it into a json string
        String sortedJson = genson.serialize(newAsset);
        stub.putStringState(assetID, sortedJson);
        return newAsset;
    }

    /**
     * Deletes asset on the ledger.
     *
     * @param ctx the transaction context
     * @param assetID the ID of the asset being deleted
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeleteAsset(final Context ctx, final String assetID) {
        ChaincodeStub stub = ctx.getStub();

        if (!AssetExists(ctx, assetID)) {
            String errorMessage = String.format("Asset %s does not exist", assetID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        stub.delState(assetID);
    }

    /**
     * Checks the existence of the asset on the ledger
     *
     * @param ctx the transaction context
     * @param assetID the ID of the asset
     * @return boolean indicating the existence of the asset
     */
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean AssetExists(final Context ctx, final String assetID) {
        ChaincodeStub stub = ctx.getStub();
        String assetJSON = stub.getStringState(assetID);

        return (assetJSON != null && !assetJSON.isEmpty());
    }

    /**
     * Changes the owner of a asset on the ledger.
     *
     * @param ctx the transaction context
     * @param assetID the ID of the asset being transferred
     * @param newOwner the new owner
     * @return the old owner
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String TransferAsset(final Context ctx, final String assetID, final String newOwner) {
        ChaincodeStub stub = ctx.getStub();
        String assetJSON = stub.getStringState(assetID);

        if (assetJSON == null || assetJSON.isEmpty()) {
            String errorMessage = String.format("Asset %s does not exist", assetID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_NOT_FOUND.toString());
        }

        Asset asset = genson.deserialize(assetJSON, Asset.class);

        Asset newAsset = new Asset(asset.getAssetID(), asset.getColor(), asset.getSize(), newOwner, asset.getAppraisedValue());
        //Use a Genson to conver the Asset into string, sort it alphabetically and serialize it into a json string
        String sortedJson = genson.serialize(newAsset);
        stub.putStringState(assetID, sortedJson);

        return asset.getOwner();
    }

    /**
     * Retrieves all assets from the ledger.
     *
     * @param ctx the transaction context
     * @return array of assets found on the ledger
     */
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllAssets(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        List<Asset> queryResults = new ArrayList<Asset>();

        // To retrieve all assets from the ledger use getStateByRange with empty startKey & endKey.
        // Giving empty startKey & endKey is interpreted as all the keys from beginning to end.
        // As another example, if you use startKey = 'asset0', endKey = 'asset9' ,
        // then getStateByRange will retrieve asset with keys between asset0 (inclusive) and asset9 (exclusive) in lexical order.
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

        for (KeyValue result: results) {
            Asset asset = genson.deserialize(result.getStringValue(), Asset.class);
            System.out.println(asset);
            queryResults.add(asset);
        }

        final String response = genson.serialize(queryResults);

        return response;
    }
}
