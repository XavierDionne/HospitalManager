/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */
package com.wolfd.HospitalManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.NoArgsConstructor;

/**
 * <p>
 * This class contains the entry point (i.e. the main method)
 * to launch this hospital manager spring boot application.
 * </p>
 */
@NoArgsConstructor
@SpringBootApplication
public class HospitalManagerApplication
{
    /**
     * The entry point method to launch this application.
     *
     * @param arguments
     *        The array of command line arguments to use when launching
     *        this application. This cannot be null, but it could be empty.
     */
    public static void main(final String[] arguments)
    {
        SpringApplication.run(HospitalManagerApplication.class, arguments);
    }
}