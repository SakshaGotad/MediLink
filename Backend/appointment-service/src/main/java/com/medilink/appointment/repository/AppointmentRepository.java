package com.medilink.appointment.repository;

import com.medilink.appointment.entity.Appointment;
import com.medilink.appointment.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    boolean existsByDoctorIdAndAppointmentDateAndAppointmentTime(
            UUID doctorId,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    );

    long countByDoctorIdAndAppointmentDateAndAppointmentTime(
            UUID doctorId,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    );

    List<Appointment> findByPatientId(UUID patientId);

    List<Appointment> findByDoctorId(UUID doctorId);
    List<Appointment> findByDoctorIdAndAppointmentDate(UUID doctorId, LocalDate appointmentDate);
    List<Appointment> findByDoctorIdAndPaymentStatus(UUID doctorId, PaymentStatus paymentStatus);
    List<Appointment> findByDoctorIdAndAppointmentDateAndPaymentStatus(UUID doctorId, LocalDate appointmentDate, PaymentStatus paymentStatus);

    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT a.patientId FROM Appointment a WHERE a.doctorId = :doctorId")
    List<UUID> findDistinctPatientIdsByDoctorId(UUID doctorId);

}