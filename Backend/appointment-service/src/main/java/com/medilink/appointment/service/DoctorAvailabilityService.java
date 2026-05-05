package com.medilink.appointment.service;

import com.medilink.appointment.dto.CreateAvailabilityRequest;
import com.medilink.appointment.dto.SlotResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface DoctorAvailabilityService {

    void createAvailability(UUID doctorId, CreateAvailabilityRequest request);

    List<SlotResponse> getAvailableSlots(UUID doctorId, LocalDate date);
}