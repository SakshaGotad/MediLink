package com.medilink.appointment.repository;

import com.medilink.appointment.entity.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, UUID> {

    List<DoctorAvailability> findByDoctorIdAndDate(UUID doctorId, LocalDate date);

    boolean existsByDoctorIdAndDateAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            UUID doctorId,
            LocalDate date,
            LocalTime endTime,
            LocalTime startTime
    );
}