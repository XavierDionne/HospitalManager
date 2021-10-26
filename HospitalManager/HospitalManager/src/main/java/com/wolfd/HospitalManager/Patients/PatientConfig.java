/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager.Patients;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PatientConfig {


    @Bean
    CommandLineRunner commandLineRunner(PatientRepository patientRepository){
        return args -> {
            Patient mariam = new Patient(
                    12312,
                    "Mariam",
                    "Bozo",
                    9021234567L,
                    "123 Fake Street"
            );

            Patient alex= new Patient(
                    345564,
                    "Alex",
                    "IDK",
                    9021119999L,
                    "237 Faker Court"
            );

            patientRepository.saveAll(
                    List.of(mariam,alex)
            );
        };
    }
}