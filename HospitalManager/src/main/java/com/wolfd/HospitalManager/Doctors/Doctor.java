package com.wolfd.HospitalManager.Doctors;

import javax.persistence.*;
import javax.print.Doc;

@Entity
@Table
public class Doctor {

    //Attributes
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
    Setters and getters
     */

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

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

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId=" + doctorId +
                ", employee Id='"+ empId + '\''+
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone=" + phone +
                '}';
    }
}

