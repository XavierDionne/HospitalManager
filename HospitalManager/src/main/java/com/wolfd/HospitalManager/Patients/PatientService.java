/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager.Patients;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getPatients(){
        return patientRepository.findAll();
    }

    public void addNewPatient(Patient patient) {
        Optional<Patient> patientOptional = patientRepository.
                findPatientByLastName(patient.getLastName());
        if (patientOptional.isPresent()){
            throw  new IllegalStateException("last name taken");
        }
        patientRepository.save(patient);

    }

    public void deletePatient(Integer patientId) {
        boolean exists = patientRepository.existsById(patientId);
        if (!exists){
            throw new IllegalStateException(
                    "patient with id " + patientId + " does not exists");
        }
        patientRepository.deleteById(patientId);
    }

    @Transactional
    public void updatePatient(Integer patientId,
                              String firstName,
                              String lastName) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalStateException(
                        "patient with id " + patientId + " does not exist"));

        if (firstName != null && firstName.length() > 0 &&
                !Objects.equals(patient.getFirstName(), firstName)) {
            patient.setFirstName(firstName);
        }

        if (lastName != null && lastName.length() > 0 &&
                !Objects.equals(patient.getLastName(), lastName)) {
            Optional<Patient> patientOptional = patientRepository.findPatientByLastName(lastName);
            if (patientOptional.isPresent()){
                throw new IllegalStateException("lastName taken");
            }
            patient.setLastName(lastName);
        }
    }
}