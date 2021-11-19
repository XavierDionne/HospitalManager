/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager.Doctors;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorService
{
    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> getDoctors(){
        return doctorRepository.findAll();
    }

    @Transactional
    public long create(
        final String firstname,
        final String lastname,
        final int employeeId,
        final String phoneNumber)
    {
        final Doctor existingDoctor = doctorRepository
            .findByLastName(lastname);

        if (existingDoctor != null)
        {
            throw new DoctorAlreadyExistsException(lastname);
        }

        final Doctor doctor = new Doctor();
        doctor.setFirstName(firstname);
        doctor.setLastName(lastname);
        doctor.setEmployeeId(employeeId);
        doctor.setPhoneNumber(phoneNumber);

        final Doctor persistedDoctor = doctorRepository.save(doctor);

        return persistedDoctor.getId(); 
    }

    @Transactional
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
//        final Doctor doctor = doctorRepository.findById(id)
//            .orElseThrow(() -> new IllegalStateException(
//                "patient with id " + id + "does not exist"));
//
//        if (firstName != null && firstName.length() > 0 &&
//                !Objects.equals(doctor.getFirstName(), firstName))
//        {
//            doctor.setFirstName(firstName);
//        }
//
//        if (lastName != null && lastName.length() > 0 &&
//                !Objects.equals(doctor.getLastName(), lastName)) {
//            Optional<Doctor> doctorOptional = doctorRepository.findByLastName(lastName);
//            if (doctorOptional.isPresent()){
//                throw new IllegalStateException("lastName taken");
//            }
//            doctor.setLastName(lastName);
//        }
    }

    public Doctor get(final long id)
    {
        return doctorRepository.findById(id).get();
    }

    static final class DoctorAlreadyExistsException extends RuntimeException
    {
        private DoctorAlreadyExistsException(final String lastname)
        {
            super("A doctor with lastname=\""
                 + lastname
                 + "\" already exists. Only unique lastnames allowed.");
        }

        private static final long serialVersionUID = 1L;
    }
}