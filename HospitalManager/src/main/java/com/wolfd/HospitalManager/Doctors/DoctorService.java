/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager.Doctors;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void addNewDoctor(final Doctor doctor)
    {
        Optional<Doctor> doctorOptional = doctorRepository.
                findByLastName(doctor.getLastName());

        if (doctorOptional.isPresent()){
            throw  new IllegalStateException("last name taken");
        }

        doctorRepository.save(doctor);
    }

    public void deleteDoctor(final long id)
    {
        final boolean exists = doctorRepository.existsById(id);

        if (!exists)
        {
            throw new IllegalStateException(
                    "student with id " + id + " does not exists");
        }

        doctorRepository.deleteById(id);
    }

    @Transactional
    public void updateDoctor(
        final long id,
        final String firstName,
        final String lastName)
    {
        final Doctor doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException(
                "patient with id " + id + "does not exist"));

        if (firstName != null && firstName.length() > 0 &&
                !Objects.equals(doctor.getFirstName(), firstName))
        {
            doctor.setFirstName(firstName);
        }

        if (lastName != null && lastName.length() > 0 &&
                !Objects.equals(doctor.getLastName(), lastName)) {
            Optional<Doctor> doctorOptional = doctorRepository.findByLastName(lastName);
            if (doctorOptional.isPresent()){
                throw new IllegalStateException("lastName taken");
            }
            doctor.setLastName(lastName);
        }
    }
}