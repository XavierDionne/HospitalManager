/*
    Copyright 2021, Xavier Dionne, All rights reserved.
 */

package com.wolfd.HospitalManager.Doctors;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>
{
    @Query("SELECT s FROM Doctor s WHERE s.lastName = ?1")
    Optional<Doctor> findByLastName(final String lastName);
}