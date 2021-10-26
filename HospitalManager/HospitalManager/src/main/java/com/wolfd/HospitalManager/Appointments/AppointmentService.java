package com.wolfd.HospitalManager.Appointments;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getAppointments(){
        return appointmentRepository.findAll();
    }

    public void addNewAppointment(Appointment appointment) {
        Optional<Appointment> appointmentOptional = appointmentRepository.
                findAppointmentByAppId(appointment.getAppId());
        if (appointmentOptional.isPresent()){
            throw  new IllegalStateException("appointment ID taken");
        }
        appointmentRepository.save(appointment);

    }

    public void deleteAppointment(Integer appId) {
        boolean exists = appointmentRepository.existsById(appId);
        if (!exists){
            throw new IllegalStateException(
                    "appointment with id " + appId + " does not exists");
        }
        appointmentRepository.deleteById(appId);
    }

    @Transactional
    public void updateAppointment(Integer appId,
                              Integer room) {
        Date d = new Date();
        Appointment appointment = appointmentRepository.findById(appId)
                .orElseThrow(() -> new IllegalStateException(
                        "appointment with id " + appId + " does not exist"));

        if (room != null && room > 0 &&
                !Objects.equals(appointment.getRoom(), room)) {
            appointment.setRoom(room);
        }

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
