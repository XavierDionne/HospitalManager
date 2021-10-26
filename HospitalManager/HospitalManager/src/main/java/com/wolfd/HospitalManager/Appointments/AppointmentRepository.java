package com.wolfd.HospitalManager.Appointments;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Integer> {

    @Query("SELECT s FROM Appointment s WHERE s.appId = ?1")
    Optional<Appointment> findAppointmentByAppId(Integer appId);
}
