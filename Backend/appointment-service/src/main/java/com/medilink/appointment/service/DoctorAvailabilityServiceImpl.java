package com.medilink.appointment.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.medilink.appointment.dto.CreateAvailabilityRequest;
import com.medilink.appointment.dto.SlotResponse;
import com.medilink.appointment.entity.Appointment;
import com.medilink.appointment.entity.DoctorAvailability;
import com.medilink.appointment.repository.AppointmentRepository;
import com.medilink.appointment.repository.DoctorAvailabilityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorAvailabilityServiceImpl implements DoctorAvailabilityService {

    private final DoctorAvailabilityRepository doctorAvailabilityRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public void createAvailability(UUID doctorId, CreateAvailabilityRequest request) {
        boolean overlap = doctorAvailabilityRepository
                .existsByDoctorIdAndDateAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                        doctorId,
                        request.getDate(),
                        request.getEndTime(),
                        request.getStartTime());

        if (overlap) {
            throw new RuntimeException("Availability overlaps with existing slot");
        }

        DoctorAvailability availability = DoctorAvailability.builder()
                .doctorId(doctorId)
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .slotDuration(request.getSlotDuration())
                .build();

        doctorAvailabilityRepository.save(availability);

    }

    @Override
    public List<SlotResponse> getAvailableSlots(UUID doctorId, LocalDate date) {

        List<DoctorAvailability> ranges = doctorAvailabilityRepository.findByDoctorIdAndDate(doctorId, date);
        List<Appointment> booked = appointmentRepository.findByDoctorId(doctorId);
        Set<LocalTime> bookedTimes = booked.stream()
                .filter(a -> a.getAppointmentDate().equals(date))
                .map(Appointment::getAppointmentTime)
                .collect(Collectors.toSet());
        List<SlotResponse> slots = new ArrayList<>();

        for(DoctorAvailability range : ranges) {
            LocalTime current = range.getStartTime();

            while (current.isBefore(range.getEndTime())) {

                boolean isBooked = bookedTimes.contains(current);

                slots.add(SlotResponse.builder()
                        .time(current)
                        .available(!isBooked)
                        .build());

                current = current.plusMinutes(range.getSlotDuration());
            }
        }
     return slots;
    }
}
