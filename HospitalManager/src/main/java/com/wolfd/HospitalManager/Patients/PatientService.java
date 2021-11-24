/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager.Patients;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wolfd.HospitalManager.Doctors.Doctor;
import com.wolfd.HospitalManager.Doctors.DoctorService;

@Service
public class PatientService {

    @Autowired
    private final DoctorService doctorService;

    @Autowired
    private final PatientRepository patientRepository;

    public PatientService(
        final PatientRepository patientRepository,
        final DoctorService doctorService) {
           this.patientRepository = patientRepository;
           this.doctorService = doctorService;
    }

    @Transactional
    public List<Patient> getPatients(){
        return patientRepository.findAll();
    }

    @Transactional
    public long create(
            final String firstname,
            final String lastname,
            final int healthcard,
            final String phonenumber,
            final String address,
            final long doctorId)
    {
        final Doctor doctor = doctorService.get(doctorId);

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
        patient.setFamilyDoctor(doctor);

        doctor.add(patient);

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

    public Patient get(final long id)
    {
        return patientRepository.findById(id).get();
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