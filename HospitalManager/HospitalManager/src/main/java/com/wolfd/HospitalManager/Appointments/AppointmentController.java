package com.wolfd.HospitalManager.Appointments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public String getAppointments(){
        return appointmentService.getAppointments().toString();
    }

    @PostMapping
    public void registerNewAppointments(@RequestBody Appointment appointment){
        appointmentService.addNewAppointment(appointment);
    }

    @DeleteMapping(path = "{appId}")
    public void deleteAppointment(@PathVariable("appId") Integer appId){
        appointmentService.deleteAppointment(appId);
    }

    @PutMapping(path = "{appId}")
    public void updateAppointment(@PathVariable("appId") Integer appId,
                              @RequestParam(required = false) Integer room){
        appointmentService.updateAppointment(appId, room);
    }
}