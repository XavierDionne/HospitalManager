/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager.Doctors;

import java.util.ArrayList;
import java.util.List;

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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/doctor")
public final class DoctorController {

    @Autowired
    private final DoctorService doctorService;

    /**
     * @return The complete list of doctors maintained by this service. The list
     *         is returned as a JSON response body represented by the
     *         {@link GetDoctorsResponse} object. This cannot be null, but it
     *         could be empty.
     */
    @GetMapping
    public GetDoctorsResponse getDoctors() {
        // Create the object to return to the client. This will automatically get
        // converted from a POJO (plain old java object) into a JSON string. This
        // object represents the view in our MVC pattern. We never return the domain
        // object directly to the client.
        final GetDoctorsResponse response = new GetDoctorsResponse();

        // Retrieve the complete list of doctors from the database (i.e.
        // The model component in the MVC pattern. a.k.a our domain objects).
        final List<Doctor> doctors = doctorService.getDoctors();

        // Populate our response object with all the doctors retrieved from the
        // database.
        for(final Doctor currentDoctor : doctors)
        {
            response.add(new DoctorResponse(currentDoctor));
        }

        return response;
    }

    @PostMapping
    public void registerNewDoctor(final @RequestBody Doctor doctor) {
        doctorService.addNewDoctor(doctor);
    }

    @DeleteMapping(path = "{doctorId}")
    public void deletePatient(final @PathVariable("doctorId") Integer doctorId) {
        doctorService.deleteDoctor(doctorId);
    }

    @PutMapping(path = "{doctorId}")
    public void updatePatient(final @PathVariable("doctorId") Integer doctorId,
                              final @RequestParam(required = false) String firstName,
                              final @RequestParam(required = false) String lastName) {
        doctorService.updateDoctor(doctorId, firstName, lastName);
    }

    /**
     * <p>
     * This class represents the list of doctors returned to the clients
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
     * This object contains a list of <@link DoctorResponse> objects. This class
     * exists only to group these objects.
     * </p>
     */
    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class GetDoctorsResponse
    {
        /**
         * Adds a new doctor instance to the list of doctors to return as the
         * response.
         *
         * @param doctor
         *        The {@link DoctorResponse} object to add. This cannot be null.
         */
        private void add(final DoctorResponse doctor)
        {
            doctors.add(doctor);
        }

        // The list of doctors to return as part of the response. This will
        // get marshalled into a JSON array. This cannot be null.
        private final List<DoctorResponse> doctors = new ArrayList<>();
    }

    /**
     * <p>
     * This class represents the doctor information (i.e for a single doctor)
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
    private static final class DoctorResponse
    {
        /**
         * Constructs an object instance of this {@link DoctorResponse} class.
         * This constructor facilitates converting the {@link Doctor} domain object
         * into an instance of this object.
         *
         * @param doctor
         *        The {@link Doctor} object to use. This cannot be null.
         */
        private DoctorResponse(final Doctor doctor)
        {
            id = doctor.getId();
            employeeId = doctor.getEmployeeId();
            lastName = doctor.getLastName();
        }

        // The unique database identifier for this doctor. This cannot be null
        // nor less than or equal to zero.
        private final long id;

        // The employee identifier for this doctor.
        private final int employeeId;

        // The last name for this doctor. This cannot be null.
        private final String lastName;
    }
}