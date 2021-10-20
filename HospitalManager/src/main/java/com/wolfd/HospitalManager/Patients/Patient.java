/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager.Patients;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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


    public long getHealthCard() {
        return patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public long getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public void setHealthCard(Integer healthCard) {
        this.patientId = healthCard;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public String toString(){
        return String.format("Patient[patient ID:%d, health card:'%d', firstName:'%s', lastName:'%s', phone:%d, address:'%s']\n", patientId, healthCard, firstName, lastName, phone, address );
    }
}