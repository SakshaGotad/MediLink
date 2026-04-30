package com.medilink.appointment.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.medilink.appointment.dto.CreateAvailabilityRequest;
import com.medilink.appointment.dto.SlotResponse;

public interface DoctorAvailabilityService {
    void createAvailability(UUID doctorId, CreateAvailabilityRequest request);

    List<SlotResponse> getAvailableSlots(UUID doctorId, LocalDate date);
}
