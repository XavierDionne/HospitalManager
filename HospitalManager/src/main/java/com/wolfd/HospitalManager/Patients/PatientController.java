/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager.Patients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RestController
@RequiredArgsConstructor
@RequestMapping( "/api/v1/patient")
public class PatientController {

    @Autowired
    private final PatientService patientService;

    /**
     * @return The complete list of patients maintained by this service. The list
     *         is returned as a JSON response body represented by the
     *         {@link PatientController.GetPatientsResponse} object. This cannot be null, but it
     *         could be empty.
     */
    @GetMapping
    public GetPatientsResponse getPatients() {
        // Create the object to return to the client. This will automatically get
        // converted from a POJO (plain old java object) into a JSON string. This
        // object represents the view in our MVC pattern. We never return the domain
        // object directly to the client.
        final GetPatientsResponse response = new GetPatientsResponse();

        // Retrieve the complete list of doctors from the database (i.e.
        // The model component in the MVC pattern. a.k.a our domain objects).
        final List<Patient> patients = patientService.getPatients();

        // Populate our response object with all the doctors retrieved from the
        // database.
        for(final Patient currentPatient : patients)
        {
            response.add(new PatientResponse(currentPatient));
        }

        return response;
    }

    @PostMapping
    public void registerNewPatient(@RequestBody Patient patient)
    {
        patientService.addNewPatient(patient);
    }

    @DeleteMapping(path = "{patientId}")
    public void deletePatient(@PathVariable("patientId") Long id)
    {
        patientService.deletePatient(id);
    }

    @PutMapping(path = "{patientId}")
    public void updatePatient(@PathVariable("patientId") Long id,
                              @RequestParam(required = false) String firstName,
                              @RequestParam(required = false) String lastName)
    {
        patientService.updatePatient(id, firstName, lastName);
    }

    /**
     * <p>
     * This class represents the list of patients returned to the clients
     * when they invoke the  GET endpoint above.
     * </p>
     *
     * <p>
     * This object will be automatically be marshalled from its POJO representation
     * into its equivalent JSON string representation by the framework. The Jackson
     * libraries will take care of this action.
     * </p>
     *
     * <p>
     * This object contains a list of <@link PatientResponse> objects. This class
     * exists only to group these objects.
     * </p>
     */
    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class GetPatientsResponse
    {
        /**
         * Adds a new doctor instance to the list of doctors to return as the
         * response.
         *
         * @param patient
         *        The {@link PatientController.PatientResponse} object to add. This cannot be null.
         */
        private void add(final PatientResponse patient){patients.add(patient);}

        // The list of patients to return as part of the response. This will
        // get marshalled into a JSON array. This cannot be null.
        private final List<PatientResponse> patients = new ArrayList<>();
    }

    /**
     * <p>
     * This class represents the patient information (i.e for a single patient)
     * we exposed to the clients of our REST endpoints.
     * </p>
     *
     * <p>
     * This object will be automatically be marshalled from its POJO representation
     * into its equivalent JSON string representation by the framework. The Jackson
     * libraries will take care of this action.
     * </p>
     */
    @Getter
    @ToString
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class PatientResponse
    {
        /**
         * Constructs an object instance of this {@link PatientController.PatientResponse} class.
         * This constructor facilitates converting the {@link Patient} domain object
         * into an instance of this object.
         *
         * @param patient
         *        The {@link Patient} object to use. This cannot be null.
         */
        private PatientResponse(Patient patient){
            id = patient.getId();
            healthCard = patient.getHealthCard();
            lastName = patient.getLastName();
        }

        // The unique database identifier for this doctor. This cannot be null
        // nor less than or equal to zero.
        private final long id;

        // The employee identifier for this doctor.
        private final int healthCard;

        // The last name for this doctor. This cannot be null.
        private final String lastName;
    }


}