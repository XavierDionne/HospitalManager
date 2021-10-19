/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager.Doctors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> getDoctors(){
        return doctorRepository.findAll();
    }

    public void addNewDoctor(Doctor doctor) {
        Optional<Doctor> doctorOptional = doctorRepository.
                findDoctorByLastName(doctor.getLastName());
        if (doctorOptional.isPresent()){
            throw  new IllegalStateException("last name taken");
        }
        doctorRepository.save(doctor);

    }

    public void deleteDoctor(Integer doctorId) {
        boolean exists = doctorRepository.existsById(doctorId);
        if (!exists){
            throw new IllegalStateException(
                    "student with id " + doctorId + " does not exists");
        }
        doctorRepository.deleteById(doctorId);
    }

    @Transactional
    public void updateDoctor(Integer doctorId,
                             String firstName,
                             String lastName) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalStateException(
                        "patient with id " + doctorId + "does not exist"));

        if (firstName != null && firstName.length() > 0 &&
                !Objects.equals(doctor.getFirstName(), firstName)) {
            doctor.setFirstName(firstName);
        }

        if (lastName != null && lastName.length() > 0 &&
                !Objects.equals(doctor.getLastName(), lastName)) {
            Optional<Doctor> doctorOptional = doctorRepository.findDoctorByLastName(lastName);
            if (doctorOptional.isPresent()){
                throw new IllegalStateException("lastName taken");
            }
            doctor.setLastName(lastName);
        }
    }
}

