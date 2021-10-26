/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager.Doctors;

import com.wolfd.HospitalManager.Appointments.Appointment;
import com.wolfd.HospitalManager.Patients.Patient;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
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

    public Doctor(){}

    public Doctor(Integer empId, String firstName, String lastName){
        this.empId = empId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Doctor(Integer empId, String firstName, String lastName, long phone){
        this.empId = empId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    /*
    Methods
     */

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Doctor["+"doctorId:"+doctorId+", employee Id:'"+empId+", firstName:'"+firstName+", lastName:'"+lastName +", phone:"+phone+']';
    }
}