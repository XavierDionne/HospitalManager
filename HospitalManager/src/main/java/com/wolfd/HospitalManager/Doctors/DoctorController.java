/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager.Doctors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public String getDoctors(){
        return doctorService.getDoctors().toString();
    }

    @PostMapping
    public void registerNewDoctor(@RequestBody Doctor doctor){
        doctorService.addNewDoctor(doctor);
    }

    @DeleteMapping(path = "{doctorId}")
    public void deletePatient(@PathVariable("doctorId") Integer doctorId){
        doctorService.deleteDoctor(doctorId);
    }

    @PutMapping(path = "{doctorId}")
    public void updatePatient(@PathVariable("doctorId") Integer doctorId,
                              @RequestParam(required = false) String firstName,
                              @RequestParam(required = false) String lastName){
        doctorService.updateDoctor(doctorId, firstName, lastName);
    }
}
