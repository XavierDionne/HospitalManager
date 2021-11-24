/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager.Appointments;

import io.swagger.annotations.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "appointment")
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/appointment")
public class AppointmentController
{

    @Autowired
    private final AppointmentService appointmentService;

    /**
     * This method handles a GET request. This http method is reserved for retrieval
     * operations.
     *
     * @return The complete list of appointments maintained by this service. The list
     *         is returned as a JSON response body represented by the
     *         {@link GetAppointmentsResponsePayload} object. This cannot be null, but it
     *         could be empty. Both the response payload and http status are wrapped
     *         within the {@link ResponseEntity} object returned.
     */
    @GetMapping
    @ApiOperation("Retrieves all the appointments managed by this service.")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "If the list of appointments was successfully retrieved.")
    })
    public ResponseEntity<GetAppointmentsResponsePayload> getAll()
    {
        log.info("Processing the incoming get all appointments http request.");

        // Create the object to return to the client. This will automatically get
        // converted from a POJO (plain old java object) into a JSON string. This
        // object represents the view in our MVC pattern. We never return the domain
        // object directly to the client.
        final GetAppointmentsResponsePayload payload = new GetAppointmentsResponsePayload();

        // Retrieve the complete list of appointments from the database (i.e.
        // The model component in the MVC pattern. a.k.a our domain objects).
        final List<Appointment> appointments = appointmentService.getAppointments();

        for(final Appointment current : appointments)
        {
            log.info("Found: " + current);
        }

        // Populate our response object with all the doctors retrieved from the
        // database.
        for(final Appointment currentAppointment : appointments)
        {
            payload.add(new AppointmentPayload(currentAppointment));
        }

        log.info("Successfully processed. Responding with payload=[{}].", payload);

        return new ResponseEntity<>(payload, HttpStatus.OK);
    }

    /**
     * This method handles a POST request. This http method is reserved for create
     * operations.
     *
     * @return If successful, a 200 OK and the database identifier for the appointment
     *         created within the response payload. This cannot be null. Both the
     *         response payload and http status are wrapped
     *         within the {@link ResponseEntity} object returned.
     */
    @PostMapping
    @ApiOperation("Creates a new appointment managed by this service.")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "If the list of appointment was successfully retrieved."),
            @ApiResponse(
                    code = 400,
                    message = "If the appointment is already managed by this service."),
    })
    public ResponseEntity<CreateAppointmentResponsePayload> create(
            final @RequestBody CreateAppointmentRequestPayload requestPayload)
    {
        log.info("Processing the incoming create patient http request containing "
                + "payload=[{}]", requestPayload);

        try
        {
            // Delegate the creation of a new appointment to the service. Give it the
            // enumerated attributes it requires to create a new appointment. If an appointment
            // with the same appId already exists, then this operation will throw.
            final long id =appointmentService.create(
                    requestPayload.getAppId(),
                    requestPayload.getDate(),
                    requestPayload.getRoom(),
                    requestPayload.getPatientId(),
                    requestPayload.getDoctorId());

            // If above succeeds (i.e. does not throw), then the appointment was created
            // in the database and its database identifier was returned. Communicate
            // this back to the client.
            final CreateAppointmentResponsePayload responsePayload
                    = new CreateAppointmentResponsePayload(id);

            log.info("Successfully processed. Responding with payload=[{}].",
                    responsePayload);

            return new ResponseEntity<>(responsePayload, HttpStatus.OK);
        }
        catch(final AppointmentService.AppointmentAlreadyExistsException aaee)
        {
            // Uh-oh. The appointment already exists. Return an http status 400 to the
            // client.
            log.error("The appointment already exists. Returning bad request to client.",
                    aaee);

            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        catch(final Exception e)
        {
            log.error("There was an error creating the appointment.", e);

            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping(path = "{appId}")
    public void update(
            final @PathVariable("appId") long appId,
            final @RequestParam(required = false) String room,
            final @RequestParam(required = false) String date)
    {
        appointmentService.updateAppointment(appId, room, date);
    }

    @DeleteMapping(path = "{appId}")
    public void delete(final @PathVariable("appId") long appId)
    {
        appointmentService.deleteAppointment(appId);
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
     * This object contains a list of <@link AppointmentsResponse> objects. This class
     * exists only to group these objects.
     * </p>
     */
    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @ApiModel(description = "The GET response payload sent back to the client "
            + "containing a list of appointments managed by this service.")
    private static final class GetAppointmentsResponsePayload
    {
        /**
         * Adds a new appointment instance to the list of appointments to return as the
         * response.
         *
         * @param appointment
         *        The {@link AppointmentPayload} object to add. This cannot be null.
         */
        private void add(final AppointmentPayload appointment)
        {
            appointments.add(appointment);
        }

        // The list of doctors to return as part of the response. This will
        // get marshalled into a JSON array. This cannot be null.
        private final List<AppointmentPayload> appointments = new ArrayList<>();
    }

    /**
     * This class represents the JSON we receive when a create appointment request
     * arrives at this controller.
     */
    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @ApiModel(description = "The POST request payload containing the details of a "
            +"new appointment to create at this service.")
    private static final class CreateAppointmentRequestPayload
    {
        @ApiModelProperty(
                value = "The appointment id identifier of the appointment.",
                required = true,
                example = EXAMPLE_APPID,
                position = 1)
        private long appId;

        // The room for this appointment. This cannot be null
        @ApiModelProperty(
                value = "The room of the appointment.",
                required = true,
                example = EXAMPLE_ROOM,
                position = 2)
        private String room;

        // The date for this appointment. This cannot be null
        @ApiModelProperty(
                value = "The date and time of the appointment.",
                required = true,
                example = EXAMPLE_DATE,
                position = 3)
        private String date;

        @ApiModelProperty(
                value = "The database identifier of the patient to associate this appointment with.",
                required = true,
                example = EXAMPLE_ID,
                position = 4)
        private long patientId;

        @ApiModelProperty(
                value = "The database identifier of the doctor to associate this appointment with.",
                required = true,
                example = EXAMPLE_ID,
                position = 5)
        private long doctorId;
    }

    /**
     * This class represents the JSON we send back in response to a successful
     * create appointment request.
     */
    @Getter
    @ToString
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @ApiModel(description = "The POST response payload sent back to the client "
            + "containing the database identifier of the newly created appointment.")
    private static final class CreateAppointmentResponsePayload
    {
        // The unique database identifier for this appointment. This cannot be null
        // nor less than or equal to zero.
        @ApiModelProperty(
                value = "The database identifier of the appointment.",
                required = true,
                example = EXAMPLE_ID,
                position = 0)
        private final long id;
    }

    /**
     * <p>
     * This class represents the appointment information (i.e for a single appointment)
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
    @Setter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @ApiModel(description = "The payload model representing an appointment")
    private static final class AppointmentPayload
    {
        /**
         * Constructs an object instance of this {@link AppointmentPayload} class.
         * This constructor facilitates converting the {@link Appointment} domain object
         * into an instance of this object.
         *
         * @param appointment
         *        The {@link Appointment} object to use. This cannot be null.
         */
        private AppointmentPayload(final Appointment appointment)
        {
            id = appointment.getId();
            appId = appointment.getAppId();
            room = appointment.getRoom();
            date = appointment.getDate();
        }

        // The unique database identifier for this appointment. This cannot be null
        // nor less than or equal to zero.
        @ApiModelProperty(
                value = "The database identifier of the appointment.",
                required = true,
                example = EXAMPLE_ID,
                position = 0)
        private final long id;

        // The appointment id identifier for this appointment. This cannot be null
        @ApiModelProperty(
                value = "The appointment id identifier of the appointment.",
                required = true,
                example = EXAMPLE_APPID,
                position = 1)
        private final long appId;

        // The room for this appointment. This cannot be null
        @ApiModelProperty(
                value = "The room of the appointment.",
                required = true,
                example = EXAMPLE_ROOM,
                position = 2)
        private final String room;

        // The date for this appointment. This cannot be null
        @ApiModelProperty(
                value = "The date and time of the appointment.",
                required = true,
                example = EXAMPLE_DATE,
                position = 3)
        private final String date;
    }

    // The example string to use for the database id everywhere.
    private static final String EXAMPLE_ID = "1";

    // The example string to use for first name everywhere.
    private static final String EXAMPLE_APPID = "\"873456\"";

    // The example string to use for last name everywhere.
    private static final String EXAMPLE_ROOM = "\"8A\"";

    // The example string to use for health card everywhere.
    private static final String EXAMPLE_DATE = "\"2021/08/22/14:00\"";

}