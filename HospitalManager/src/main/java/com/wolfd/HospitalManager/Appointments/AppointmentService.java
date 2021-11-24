/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager.Appointments;

import java.util.List;

import javax.transaction.Transactional;

import com.wolfd.HospitalManager.Doctors.Doctor;
import com.wolfd.HospitalManager.Doctors.DoctorService;
import com.wolfd.HospitalManager.Patients.Patient;
import com.wolfd.HospitalManager.Patients.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentService
{
    private final DoctorService doctorService;

    private final PatientService patientService;

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(
            final AppointmentRepository appointmentRepository,
            final DoctorService doctorService,
            final PatientService patientService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @Transactional
    public List<Appointment> getAppointments(){
        return appointmentRepository.findAll();
    }

    @Transactional
    public long create(
            final long appId,
            final String date,
            final String room,
            final long patientId,
            final long doctorId)
    {
        final Patient patient = patientService.get(patientId);

        final Doctor doctor = doctorService.get(doctorId);

        final Appointment existingAppointment = appointmentRepository
                .findByAppId(appId);

        if (existingAppointment != null)
        {
            throw new AppointmentAlreadyExistsException(appId);
        }

        final Appointment appointment = new Appointment();
        appointment.setAppId(appId);
        appointment.setDate(date);
        appointment.setRoom(room);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        doctor.addApp(appointment);

        patient.add(appointment);

        final Appointment persistedAppointment = appointmentRepository.save(appointment);

        return persistedAppointment.getId();
    }

    @Transactional
    public void deleteAppointment(final long id)
    {
        final boolean exists = appointmentRepository.existsById(id);

        if (!exists)
        {
            throw new IllegalStateException(
                    "appointment with id " + id + " does not exist");
        }

        final Appointment appointment = appointmentRepository.getById(id);

        appointment.getDoctor().deleteApp(appointment);

        appointment.getPatient().deleteApp(appointment);

        appointmentRepository.deleteById(id);
    }

    @Transactional
    public void updateAppointment(final long id,
                                  String room,
                                  String date) {
//        final Date d = new Date();
//        final Appointment appointment = appointmentRepository.findById(id)
//                .orElseThrow(() -> new IllegalStateException(
//                        "appointment with id " + id + " does not exist"));
//
//        if (room != null && room > 0 &&
//                !Objects.equals(appointment.getRoom(), room)) {
//            appointment.setRoom(room);
//        }

//        if (date != null &&
//                !Objects.equals(appointment.getDate(), date)) {
//            Optional<Appointment> appointmentOptional = appointmentRepository.findAppointmentByAppId(appId);
//            if (appointmentOptional.isPresent()){
//                throw new IllegalStateException("date taken");
//            }
//            appointment.setDate(date);
//        }
    }

    static final class AppointmentAlreadyExistsException extends RuntimeException
    {
        private AppointmentAlreadyExistsException(final long appId)
        {
            super("An appointment with appId=\""
                    + appId
                    + "\" already exists. Only unique appIds allowed");
        }

        private static final long serialVersionUID = 1L;
    }
}
