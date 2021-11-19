/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager.Patients;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wolfd.HospitalManager.Patients.PatientService.PatientAlreadyExistsException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags = "patient")
@RequiredArgsConstructor
@RequestMapping( "/api/v1/patient")
public class PatientController
{

    @Autowired
    private final PatientService patientService;

    /**
     * This method handles a GET request. This http method is reserved for retrieval
     * operations.
     *
     * @return The complete list of patients maintained by this service. The list
     *         is returned as a JSON response body represented by the
     *         {@link PatientController.GetPatientsResponsePayload} object. This cannot be null, but it
     *         could be empty. Both the response payload and http status are wrapped
     *         within the {@link ResponseEntity} object returned.
     */
    @GetMapping
    @ApiOperation("Retrieves all the patients managed by this service.")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "If the list of patients was successfully retrieved.")
    })
    public ResponseEntity<PatientController.GetPatientsResponsePayload> getAll()
    {
        log.info("Processing the incoming get all patients http request.");

        // Create the object to return to the client. This will automatically get
        // converted from a POJO (plain old java object) into a JSON string. This
        // object represents the view in our MVC pattern. We never return the domain
        // object directly to the client.
        final PatientController.GetPatientsResponsePayload payload = new PatientController.GetPatientsResponsePayload();

        // Retrieve the complete list of patients from the database (i.e.
        // The model component in the MVC pattern. a.k.a our domain objects).
        final List<Patient> patients = patientService.getPatients();

        for(final Patient current : patients)
        {
            log.info("Found: " + current);
        }

        // Populate our response object with all the doctors retrieved from the
        // database.
        for(final Patient currentPatient : patients)
        {
            payload.add(new PatientController.PatientPayload(currentPatient));
        }

        log.info("Successfully processed. Responding with payload=[{}].", payload);

        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    /**
     * This method handles a POST request. This http method is reserved for create
     * operations.
     *
     * @return If successful, a 200 OK and the database identifier for the patient
     *         created within the response payload. This cannot be null. Both the
     *         response payload and http status are wrapped
     *         within the {@link ResponseEntity} object returned.
     */
    @PostMapping
    @ApiOperation("Creates a new patient managed by this service.")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "If the list of patients was successfully retrieved."),
            @ApiResponse(
                    code = 400,
                    message = "If the patient is already managed by this service."),
    })
    public ResponseEntity<PatientController.CreatePatientResponsePayload> create(
            final @RequestBody PatientController.CreatePatientRequestPayload requestPayload)
    {
        log.info("Processing the incoming create patient http request containing "
                + "payload=[{}]", requestPayload);

        try
        {
            // Delegate the creation of a new doctor to the service. Give it the
            // enumerated attributes it requires to create a new doctor. If a doctor
            // with the same lastname already exists, then this operation will throw.
            final long id = patientService.create(
                    requestPayload.getFirstname(),
                    requestPayload.getLastname(),
                    requestPayload.getHealthCard(),
                    requestPayload.getPhoneNumber(),
                    requestPayload.getAddress(),
                    requestPayload.getDoctorId());

            // If above succeeds (i.e. does not throw), then the doctor was created
            // in the database and its database identifier was returned. Communicate
            // this back to the client.
            final PatientController.CreatePatientResponsePayload responsePayload
                    = new PatientController.CreatePatientResponsePayload(id);

            log.info("Successfully processed. Responding with payload=[{}].",
                    responsePayload);

            return new ResponseEntity<>(responsePayload, HttpStatus.OK);
        }
        catch(final PatientAlreadyExistsException paee)
        {
            // Uh-oh. The patient already exists. Return an http status 400 to the
            // client.
            log.error("The patient already exists. Returning bad request to client.",
                    paee);

            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        catch(final Exception e)
        {
            log.error("There was an error creating the patient.", e);

            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "{patientId}")
    public void update(
            final @PathVariable("patientId") long patientId,
            final @RequestParam(required = false) String firstName,
            final @RequestParam(required = false) String lastName)
    {
        patientService.updatePatient(patientId, firstName, lastName);
    }

    @DeleteMapping(path = "{patientId}")
    public void delete(final @PathVariable("patientId") long patientId)
    {
        patientService.deletePatient(patientId);
    }

    /**
     * <p>
     * This class represents the list of patients returned to the clients
     * when they invoke the GET endpoint above.
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
    @ApiModel(description = "The GET response payload sent back to the client "
            + "containing a list of patients managed by this service.")
    private static final class GetPatientsResponsePayload
    {
        /**
         * Adds a new patient instance to the list of patients to return as the
         * response.
         *
         * @param patient
         *        The {@link PatientPayload} object to add. This cannot be null.
         */
        private void add(final PatientPayload patient)
        {
            patients.add(patient);
        }

        // The list of doctors to return as part of the response. This will
        // get marshalled into a JSON array. This cannot be null.
        private final List<PatientController.PatientPayload> patients = new ArrayList<>();
    }

    /**
     * This class represents the JSON we receive when a create doctor request
     * arrives at this controller.
     */
    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @ApiModel(description = "The POST request payload containing the details of a "
            +"new doctor to create at this service.")
    private static final class CreatePatientRequestPayload
    {
        @ApiModelProperty(
                value = "The first name of the patient.",
                required = true,
                example = EXAMPLE_FIRSTNAME,
                position = 0)
        private String firstname;

        @ApiModelProperty(
                value = "The last name of the patient.",
                required = true,
                example = EXAMPLE_LASTTNAME,
                position = 1)
        private String lastname;

        @ApiModelProperty(
                value = "The health card identifier of the patient.",
                required = true,
                example = EXAMPLE_HEALTHCARD,
                position = 2)
        private int healthCard;

        @ApiModelProperty(
                value = "The phone number of the patient.",
                required = true,
                example = EXAMPLE_PHONE_NUMBER,
                position = 3)
        private String phoneNumber;

        @ApiModelProperty(
                value = "The home address of the patient.",
                required = true,
                example = EXAMPLE_ADDRESS,
                position = 4)
        private String address;

        @ApiModelProperty(
                value = "The database identifier of the doctor to associate this patient with.",
                required = true,
                example = EXAMPLE_ID,
                position = 4)
        private long doctorId;
    }

    /**
     * This class represents the JSON we send back in response to a successful
     * create patient request.
     */
    @Getter
    @ToString
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @ApiModel(description = "The POST response payload sent back to the client "
            + "containing the database identifier of the newly created patient.")
    private static final class CreatePatientResponsePayload
    {
        // The unique database identifier for this patient. This cannot be null
        // nor less than or equal to zero.
        @ApiModelProperty(
                value = "The database identifier of the patient.",
                required = true,
                example = EXAMPLE_ID,
                position = 0)
        private final long id;
    }

    /**
     * <p>
     * This class represents the patient information (i.e for a single patient)
     * placed into response payloads that we exposed to the clients of our
     * REST endpoints.
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
    @ApiModel(description = "The payload model representing a patient.")
    private static final class PatientPayload
    {
        /**
         * Constructs an object instance of this {@link PatientPayload} class.
         * This constructor facilitates converting the {@link Patient} domain object
         * into an instance of this object.
         *
         * @param patient
         *        The {@link Patient} object to use. This cannot be null.
         */
        private PatientPayload(final Patient patient)
        {
            id = patient.getId();
            healthCard = patient.getHealthCard();
            lastName = patient.getLastName();
        }

        // The unique database identifier for this patient. This cannot be null
        // nor less than or equal to zero.
        @ApiModelProperty(
                value = "The database identifier of the patient.",
                required = true,
                example = EXAMPLE_ID,
                position = 0)
        private final long id;

        // The health card identifier for this patient.
        @ApiModelProperty(
                value = "The health card identifier of the patient.",
                required = true,
                example = EXAMPLE_HEALTHCARD,
                position = 1)
        private final int healthCard;

        // The last name for this patient. This cannot be null.
        @ApiModelProperty(
                value = "The last name of the patient.",
                required = true,
                example = EXAMPLE_LASTTNAME,
                position = 2)
        private final String lastName;
    }

    // The example string to use for the database id everywhere.
    private static final String EXAMPLE_ID = "1";

    // The example string to use for first name everywhere.
    private static final String EXAMPLE_FIRSTNAME = "\"Xavier\"";

    // The example string to use for last name everywhere.
    private static final String EXAMPLE_LASTTNAME = "\"Dionne\"";

    // The example string to use for health card everywhere.
    private static final String EXAMPLE_HEALTHCARD = "\"999999\"";

    // The example string to use for phone number everywhere.
    private static final String EXAMPLE_PHONE_NUMBER = "\"1-902-555-5555\"";

    // The example string to use for address everywhere.
    private static final String EXAMPLE_ADDRESS = "\"123 Fake Street";
}