/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager.Doctors;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wolfd.HospitalManager.Appointments.Appointment;
import com.wolfd.HospitalManager.Patients.Patient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table
@Entity
@NoArgsConstructor
public class Doctor
{
    public void add(final Patient patient)
    {
        patients.add(patient);
    }

    public void addApp(final Appointment appointment)
    {
        appointments.add(appointment);
    }

    public void deleteApp(final Appointment appointment)
    {
        appointments.remove(appointment);
    }

    public void deletePatient(final Patient patient)
    {
        patients.remove(patient);
    }

    @Override
    public String toString()
    {
        return getClass().getName()
            + "["
            + "id: " + id
            + " lastName: " + lastName
            + "]";
    }

    @Id
    @Getter
    @Setter
    @SequenceGenerator(
            name = "doctor_sequence",
            sequenceName = "doctor_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "doctor_sequence"
    )
    private Long id;

    @Getter
    @Setter
    private int employeeId;

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private String phoneNumber;

    @Getter
    @OneToMany
    private List<Patient> patients = new ArrayList<>();

    @Getter
    @OneToMany
    private List<Appointment> appointments = new ArrayList<>();
}