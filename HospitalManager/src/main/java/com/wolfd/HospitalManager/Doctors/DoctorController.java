/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager.Doctors;

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

import com.wolfd.HospitalManager.Doctors.DoctorService.DoctorAlreadyExistsException;

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
@Api(tags = "doctor")
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/doctor")
public final class DoctorController
{
    @Autowired
    private final DoctorService doctorService;

    /**
     * This method handles a GET request. This http method is reserved for retrieval
     * operations.
     * 
     * @return The complete list of doctors maintained by this service. The list
     *         is returned as a JSON response body represented by the
     *         {@link GetDoctorsResponsePayload} object. This cannot be null, but it
     *         could be empty. Both the response payload and http status are wrapped
     *         within the {@link ResponseEntity} object returned.
     */
    @GetMapping
    @ApiOperation("Retrieves all the doctors managed by this service.")
    @ApiResponses({
        @ApiResponse(
            code = 200,
            message = "If the list of doctors was successfully retrieved.")
    })
    public ResponseEntity<GetDoctorsResponsePayload> getAll()
    {
        log.info("Processing the incoming get all doctors http request.");

        // Create the object to return to the client. This will automatically get
        // converted from a POJO (plain old java object) into a JSON string. This
        // object represents the view in our MVC pattern. We never return the domain
        // object directly to the client.
        final GetDoctorsResponsePayload payload = new GetDoctorsResponsePayload();

        // Retrieve the complete list of doctors from the database (i.e.
        // The model component in the MVC pattern. a.k.a our domain objects).
        final List<Doctor> doctors = doctorService.getDoctors();

        // Populate our response object with all the doctors retrieved from the
        // database.
        for(final Doctor currentDoctor : doctors)
        {
            payload.add(new DoctorPayload(currentDoctor));
        }

        log.info("Successfully processed. Responding with payload=[{}].", payload);

        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    /**
     * This method handles a POST request. This http method is reserved for create
     * operations.
     * 
     * @return If successful, a 200 OK and the database identifier for the doctor
     *         created within the response payload. This cannot be null. Both the
     *         response payload and http status are wrapped
     *         within the {@link ResponseEntity} object returned.
     */
    @PostMapping
    @ApiOperation("Creates a new doctor managed by this service.")
    @ApiResponses({
        @ApiResponse(
            code = 200,
            message = "If the list of doctors was successfully retrieved."),
        @ApiResponse(
                code = 400,
                message = "If the doctor is already managed by this service."),
    })
    public ResponseEntity<CreateDoctorResponsePayload> create(
        final @RequestBody CreateDoctorRequestPayload requestPayload)
    {
        log.info("Processing the incoming create doctor http request containing "
            + "payload=[{}]", requestPayload);

        try
        {
            // Delegate the creation of a new doctor to the service. Give it the
            // enumerated attributes it requires to create a new doctor. If a doctor
            // with the same lastname already exists, then this operation will throw.
            final long id = doctorService.create(
                requestPayload.getFirstname(),
                requestPayload.getLastname(),
                requestPayload.getEmployeeId(),
                requestPayload.getPhoneNumber());

            // If above succeeds (i.e. does not throw), then the doctor was created
            // in the database and its database identifier was returned. Communicate
            // this back to the client.
            final CreateDoctorResponsePayload responsePayload
                = new CreateDoctorResponsePayload(id);

            log.info("Successfully processed. Responding with payload=[{}].",
                responsePayload);

            return new ResponseEntity<>(responsePayload, HttpStatus.OK);
        }
        catch(final DoctorAlreadyExistsException daee)
        {
            // Uh-oh. The doctor already exists. Return an http status 400 to the
            // client.
            log.error("The doctor already exists. Returning bad request to client.",
                daee);

            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "{doctorId}")
    public void update(
        final @PathVariable("doctorId") Integer doctorId,
        final @RequestParam(required = false) String firstName,
        final @RequestParam(required = false) String lastName)
    {
        doctorService.updateDoctor(doctorId, firstName, lastName);
    }

    @DeleteMapping(path = "{doctorId}")
    public void delete(final @PathVariable("doctorId") Integer doctorId)
    {
        doctorService.deleteDoctor(doctorId);
    }

    /**
     * <p>
     * This class represents the list of doctors returned to the clients
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
     * This object contains a list of <@link DoctorResponse> objects. This class
     * exists only to group these objects.
     * </p>
     */
    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @ApiModel(description = "The GET response payload sent back to the client "
        + "containing a list of doctors managed by this service.")
    private static final class GetDoctorsResponsePayload
    {
        /**
         * Adds a new doctor instance to the list of doctors to return as the
         * response.
         *
         * @param doctor
         *        The {@link DoctorPayload} object to add. This cannot be null.
         */
        private void add(final DoctorPayload doctor)
        {
            doctors.add(doctor);
        }

        // The list of doctors to return as part of the response. This will
        // get marshalled into a JSON array. This cannot be null.
        private final List<DoctorPayload> doctors = new ArrayList<>();
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
    private static final class CreateDoctorRequestPayload
    {
        @ApiModelProperty(
            value = "The first name of the doctor.",
            required = true,
            example = EXAMPLE_FIRSTNAME,
            position = 0)
        private String firstname;

        @ApiModelProperty(
            value = "The last name of the doctor.",
            required = true,
            example = EXAMPLE_LASTTNAME,
            position = 1)
        private String lastname;

        @ApiModelProperty(
            value = "The employee identifier of the doctor.",
            required = true,
            example = EXAMPLE_EMPLOYEE_ID,
            position = 2)
        private int employeeId;

        @ApiModelProperty(
            value = "The phone number of the doctor.",
            required = true,
            example = EXAMPLE_PHONE_NUMBER,
            position = 3)
        private String phoneNumber;
    }

    /**
     * This class represents the JSON we send back in response to a successful
     * create doctor request.
     */
    @Getter
    @ToString
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @ApiModel(description = "The POST response payload sent back to the client "
        + "containing the database identifier of the newly created doctor.")
    private static final class CreateDoctorResponsePayload
    {
        // The unique database identifier for this doctor. This cannot be null
        // nor less than or equal to zero.
        @ApiModelProperty(
            value = "The database identifier of the doctor.",
            required = true,
            example = EXAMPLE_ID,
            position = 0)
        private final long id;
    }

    /**
     * <p>
     * This class represents the doctor information (i.e for a single doctor)
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
    @ApiModel(description = "The payload model representing a doctor.")
    private static final class DoctorPayload
    {
        /**
         * Constructs an object instance of this {@link DoctorPayload} class.
         * This constructor facilitates converting the {@link Doctor} domain object
         * into an instance of this object.
         *
         * @param doctor
         *        The {@link Doctor} object to use. This cannot be null.
         */
        private DoctorPayload(final Doctor doctor)
        {
            id = doctor.getId();
            employeeId = doctor.getEmployeeId();
            lastName = doctor.getLastName();
        }

        // The unique database identifier for this doctor. This cannot be null
        // nor less than or equal to zero.
        @ApiModelProperty(
            value = "The database identifier of the doctor.",
            required = true,
            example = EXAMPLE_ID,
            position = 0)
        private final long id;

        // The employee identifier for this doctor.
        @ApiModelProperty(
            value = "The employee identifier of the doctor.",
            required = true,
            example = EXAMPLE_EMPLOYEE_ID,
            position = 1)
        private final int employeeId;

        // The last name for this doctor. This cannot be null.
        @ApiModelProperty(
            value = "The last name of the doctor.",
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

    // The example string to use for employee id everywhere.
    private static final String EXAMPLE_EMPLOYEE_ID = "\"999999\"";

    // The example string to use for phone number everywhere.
    private static final String EXAMPLE_PHONE_NUMBER = "\"1-902-555-5555\"";
}