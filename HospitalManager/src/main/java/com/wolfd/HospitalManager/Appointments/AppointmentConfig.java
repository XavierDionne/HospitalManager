package com.wolfd.HospitalManager.Appointments;

import java.util.Date;
import java.util.List;

import com.wolfd.HospitalManager.Doctors.Doctor;
import com.wolfd.HospitalManager.Patients.Patient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppointmentConfig {


    @Bean
    CommandLineRunner commandLineRunner3(AppointmentRepository appointmentRepository){
        return args -> {
        Patient one = new Patient(132112,
                "Johnny",
                "Smith",
                9021234567L,
                "255 YO Street");
        Patient two = new Patient(345564,
                "Trevor",
                "Bridge",
                9021119999L,
                "111 ThisAStreet Court");
        Doctor test1 = new Doctor(
                25,
                "test1",
                "test1",
                9011111111L
        );
        Doctor test2 = new Doctor(
                100,
                "test2",
                "test2",
                9089898989L
        );
        Date d1 = new Date(122, 04, 22, 4, 30);
        Date d2 = new Date(122, 04, 22, 4, 30);
            Appointment mariam = new Appointment(
                    one,
                    test1,
                    d1,
                    7
            );

            Appointment alex= new Appointment(
                    two,
                    test2,
                    d2,
                    12
            );

            appointmentRepository.saveAll(
                    List.of(mariam,alex)
            );
        };
    }
}
