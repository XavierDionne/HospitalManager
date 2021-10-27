/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager.Appointments;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentService
{
    @Autowired
    private final AppointmentRepository appointmentRepository;

    public List<Appointment> getAppointments(){
        return appointmentRepository.findAll();
    }

    public void addNewAppointment(Appointment appointment) {
//        Optional<Appointment> appointmentOptional = appointmentRepository.
//                findAppointmentByAppId(appointment.getId());
//        if (appointmentOptional.isPresent()){
//            throw  new IllegalStateException("appointment ID taken");
//        }
//        appointmentRepository.save(appointment);
//
    }

    public void deleteAppointment(final long id) {
        boolean exists = appointmentRepository.existsById(id);
        if (!exists){
            throw new IllegalStateException(
                    "appointment with id " + id + " does not exists");
        }
        appointmentRepository.deleteById(id);
    }

    @Transactional
    public void updateAppointment(final long id,
                              Integer room) {
        final Date d = new Date();
        final Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "appointment with id " + id + " does not exist"));

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
}
