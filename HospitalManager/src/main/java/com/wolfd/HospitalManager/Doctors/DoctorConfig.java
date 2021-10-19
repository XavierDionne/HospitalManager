/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager.Doctors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DoctorConfig {


    @Bean
    CommandLineRunner commandLineRunner2(DoctorRepository doctorRepository){
        return args -> {
            Doctor bill = new Doctor(
                    12,
                    "Bill",
                    "Billyiam",
                    9022222222L

            );

            Doctor jerry= new Doctor(
                    13,
                    "Jerry",
                    "Jeremiah",
                    9027779999L
            );

            doctorRepository.saveAll(
                    List.of(bill,jerry)
            );
        };
    }
}

