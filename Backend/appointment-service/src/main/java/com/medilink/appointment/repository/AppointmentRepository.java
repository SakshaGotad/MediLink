package com.medilink.appointment.repository;

import com.medilink.appointment.entity.Appointment;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    boolean existsByDoctorIdAndAppointmentDateAndAppointmentTime(
            UUID doctorId,
            LocalDate appointmentDate,
            LocalTime appointmentTime);

    List<Appointment> findByPatientId(UUID patientId);

    List<Appointment> findByDoctorId(UUID doctorId);

    long countByDoctorIdAndAppointmentDateAndAppointmentTime(
            UUID doctorId,
            LocalDate appointmentDate,
            LocalTime appointmentTime);

            @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT COUNT(a) FROM Appointment a
        WHERE a.doctorId = :doctorId
        AND a.appointmentDate = :date
        AND a.appointmentTime = :time
    """)
    long countWithLock(
            @Param("doctorId") UUID doctorId,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time
    );
}