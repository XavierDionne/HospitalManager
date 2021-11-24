/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager.Appointments;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;

import com.wolfd.HospitalManager.Doctors.Doctor;
import com.wolfd.HospitalManager.Patients.Patient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table
@Entity
@NoArgsConstructor
public class Appointment
{
    @Override
    public String toString()
    {
        return getClass().getName()
                + "["
                + "id: " + id
                + " date: " + date
                + " room: " + room
                + "]";
    }

    @Getter
    @Setter
    @Id
    @SequenceGenerator(
            name = "appointment_sequence",
            sequenceName = "appointment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "appointment_sequence"
    )
    private Long id;

    @Getter
    @Setter
    private long appId;

    @Getter
    @Setter
    private String date;

    @Getter
    @Setter
    private String room;

    @Getter
    @Setter
    @ManyToOne
    private Patient patient;

    @Getter
    @Setter
    @ManyToOne
    private Doctor doctor;
}