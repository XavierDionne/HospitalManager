/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager.Patients;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import com.wolfd.HospitalManager.Appointments.Appointment;
import com.wolfd.HospitalManager.Doctors.Doctor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Patient
{
    @Override
    public String toString()
    {
        return getClass().getName()
            + "["
            + "id: " + id
            + " lastName: " + lastName
            + " familyDoctor: " + familyDoctor
            + "]";
    }

    //Attributes
    @Id
    @Getter
    @Setter
    @SequenceGenerator(
            name = "patient_sequence",
            sequenceName = "patient_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "patient_sequence"
    )
    private Long id;

    @Getter
    @Setter
    private Integer healthCard;

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private String phone;

    @Getter
    @Setter
    private String address;

    @Getter
    @Setter
    @ManyToOne
    private Doctor familyDoctor;

    @Getter
    @OneToMany
    private List<Appointment> appointments = new ArrayList<>();
}