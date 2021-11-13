/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager.Patients;

import com.wolfd.HospitalManager.Appointments.Appointment;
import com.wolfd.HospitalManager.Doctors.Doctor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Patient
{

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
    private Long Id;

    private Integer healthCard;

    private String firstName;

    private String lastName;

    private String phone;

    private String address;

    @ManyToOne
    private Doctor familyDoctor;

    @OneToMany
    private List<Appointment> appointments = new ArrayList<>();
}
