package com.wolfd.HospitalManager.Appointments;


import com.wolfd.HospitalManager.Doctors.Doctor;
import com.wolfd.HospitalManager.Patients.Patient;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table
public class Appointment {

    /*
    Attributes
     */

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
    private Integer appId;

    @ManyToOne(cascade = CascadeType.ALL)
    private Patient patient;

    @ManyToOne(cascade = CascadeType.ALL)
    private Doctor doctor;

    @Column(nullable = false)
    private Date date;

    private int room;

    private static final SimpleDateFormat FORMATTER
            = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    /*
    Constructors
     */

    public Appointment(){}

    public Appointment(Patient patient, Doctor doctor, Date date, int room){
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.room = room;
    }

    /*
    Methods
     */

    public Integer getAppId() {
        return appId;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "Appointment "+"appId:"+appId+", ["+patient+"], ["+doctor+"], date: "+date+", room:"+room;
    }
}
