/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager.Patients;

import com.wolfd.HospitalManager.Appointments.Appointment;
import com.wolfd.HospitalManager.Doctors.Doctor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Patient {

    //Attributes
    @Id
    @SequenceGenerator(
            name = "patient_sequence",
            sequenceName = "patient_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "patient_sequence"
    )
    private Integer patientId;

    private Integer healthCard;
    private String firstName;
    private String lastName;
    private Long phone;
    private String address;

    @ManyToOne
    private Doctor familyDoctor;

    @OneToMany
    private List<Appointment> appointments = new ArrayList<>();

    /*
    Constructors
     */

    protected Patient(){}

    public Patient(Integer healthCard, String firstName, String lastName){
        this.healthCard = healthCard;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Patient(Integer healthCard, String firstName, String lastName, long phone){
        this.healthCard = healthCard;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public Patient(Integer healthCard, String firstName, String lastName, String address){
        this.healthCard = healthCard;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public Patient(Integer healthCard, String firstName, String lastName, long phone, String address){
        this.healthCard = healthCard;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    @Override
    public String toString(){
        return String.format("Patient[patient ID:%d, health card:'%d', firstName:'%s', lastName:'%s', phone:%d, address:'%s']\n", patientId, healthCard, firstName, lastName, phone, address );
    }
}