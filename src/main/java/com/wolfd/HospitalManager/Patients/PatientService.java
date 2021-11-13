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

    @Transactional
    public long create(
            final String firstname,
            final String lastname,
            final int healthcard,
            final String phonenumber,
            final String address)
    {
        final Patient existingPatient = patientRepository
                .findByLastName(lastname);

        if (existingPatient != null)
        {
            throw new PatientAlreadyExistsException(healthcard);
        }

        final Patient patient = new Patient();
        patient.setFirstName(firstname);
        patient.setLastName(lastname);
        patient.setHealthCard(healthcard);
        patient.setPhone(phonenumber);
        patient.setAddress(address);

        final Patient persistedPatient = patientRepository.save(patient);

        return persistedPatient.getId();
    }

    @Transactional
    public void deletePatient(final long patientId)
    {
        final boolean exists = patientRepository.existsById(patientId);

        if (!exists)
        {
            throw new IllegalStateException(
                    "patient with id " + patientId + " does not exists");
        }

        patientRepository.deleteById(patientId);
    }

    @Transactional
    public void updatePatient(Long patientId,
                              String firstName,
                              String lastName) {
//        Patient patient = patientRepository.findById(patientId)
//                .orElseThrow(() -> new IllegalStateException(
//                        "patient with id " + patientId + " does not exist"));
//
//        if (firstName != null && firstName.length() > 0 &&
//                !Objects.equals(patient.getFirstName(), firstName)) {
//            patient.setFirstName(firstName);
//        }
//
//        if (lastName != null && lastName.length() > 0 &&
//                !Objects.equals(patient.getLastName(), lastName)) {
//            Optional<Patient> patientOptional = patientRepository.findByLastName(lastName);
//            if (patientOptional.isPresent()){
//                throw new IllegalStateException("lastName taken");
//            }
//            patient.setLastName(lastName);
//        }
    }

    static final class PatientAlreadyExistsException extends RuntimeException
    {
        private PatientAlreadyExistsException(final int healthcard)
        {
            super("A patient with healthcard=\""
                + healthcard
                + "\" already exists. Only unique healthcards allowed.");
        }

        private static final long serialVersionUID = 1L;
    }
}