package com.medilink.appointment.service;

import com.medilink.appointment.dto.CreateAvailabilityRequest;
import com.medilink.appointment.dto.SlotResponse;
import com.medilink.appointment.entity.Appointment;
import com.medilink.appointment.entity.DoctorAvailability;
import com.medilink.appointment.repository.AppointmentRepository;
import com.medilink.appointment.repository.DoctorAvailabilityRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorAvailabilityServiceImpl implements DoctorAvailabilityService {

    private final DoctorAvailabilityRepository availabilityRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public void createAvailability(UUID doctorId, CreateAvailabilityRequest request) {

        if (request.getDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Cannot create availability for past dates");
        }

        boolean overlap = availabilityRepository
                .existsByDoctorIdAndDateAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                        doctorId,
                        request.getDate(),
                        request.getEndTime(),
                        request.getStartTime()
                );

        if (overlap) {
            throw new RuntimeException("Availability overlaps");
        }

        DoctorAvailability availability = DoctorAvailability.builder()
                .doctorId(doctorId)
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .slotDuration(request.getSlotDuration())
                .maxPatientsPerSlot(request.getMaxPatientsPerSlot())
                .build();

        availabilityRepository.save(availability);
    }

    @Override
    public List<SlotResponse> getAvailableSlots(UUID doctorId, LocalDate date) {

        List<DoctorAvailability> ranges =
                availabilityRepository.findByDoctorIdAndDate(doctorId, date);

        List<SlotResponse> result = new ArrayList<>();

        for (DoctorAvailability range : ranges) {

            LocalTime current = range.getStartTime();

            while (current.isBefore(range.getEndTime())) {

                // Skip slots that have already passed for today
                if (date.equals(LocalDate.now()) && current.isBefore(LocalTime.now())) {
                    current = current.plusMinutes(range.getSlotDuration());
                    continue;
                }

                long count = appointmentRepository
                        .countByDoctorIdAndAppointmentDateAndAppointmentTime(
                                doctorId,
                                date,
                                current
                        );

                result.add(SlotResponse.builder()
                        .time(current)
                        .bookedCount((int) count)
                        .maxCapacity(range.getMaxPatientsPerSlot())
                        .available(count < range.getMaxPatientsPerSlot())
                        .build());

                current = current.plusMinutes(range.getSlotDuration());
            }
        }

        return result;
    }
}