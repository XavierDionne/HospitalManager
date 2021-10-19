/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager.Patients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/patient")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public String getPatients(){
        return patientService.getPatients().toString();
    }

    @PostMapping
    public void registerNewPatient(@RequestBody Patient patient){
        patientService.addNewPatient(patient);
    }

    @DeleteMapping(path = "{patientId}")
    public void deletePatient(@PathVariable("patientId") Integer healthCard){
        patientService.deletePatient(healthCard);
    }

    @PutMapping(path = "{patientId}")
    public void updatePatient(@PathVariable("patientId") Integer healthCard,
                              @RequestParam(required = false) String firstName,
                              @RequestParam(required = false) String lastName){
        patientService.updatePatient(healthCard, firstName, lastName);
    }
}
