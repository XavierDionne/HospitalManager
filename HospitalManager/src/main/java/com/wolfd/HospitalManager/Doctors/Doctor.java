/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager.Doctors;

import com.wolfd.HospitalManager.Appointments.Appointment;
import com.wolfd.HospitalManager.Patients.Patient;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Doctor {

    /*
    Attributes
     */

    @Id
    @SequenceGenerator(
            name = "doctor_sequence",
            sequenceName = "doctor_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "doctor_sequence"
    )
    private Integer doctorId;

    private Integer empId;
    private String firstName;
    private String lastName;
    private long phone;

    @OneToMany
    private List<Patient> patients = new ArrayList<>();

    @OneToMany
    private List<Appointment> appointments = new ArrayList<>();

    /*
    Constructors
     */

    public Doctor(final Integer empId, final String firstName, final String lastName) {
        this.empId = empId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Doctor(final Integer empId, final String firstName, final String lastName, final long phone) {
        this.empId = empId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }
}