package com.medilink.appointment.controller;

import com.medilink.appointment.dto.CreateAvailabilityRequest;
import com.medilink.appointment.dto.SlotResponse;
import com.medilink.appointment.service.DoctorAvailabilityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/availability")
@RequiredArgsConstructor
public class DoctorAvailabilityController {

    private final DoctorAvailabilityService availabilityService;

    @PostMapping
    public ResponseEntity<String> createAvailability(
            @RequestBody @Valid CreateAvailabilityRequest request
    ) {

        UUID doctorId = UUID.randomUUID(); // TEMP

        availabilityService.createAvailability(doctorId, request);

        return ResponseEntity.ok("Availability created");
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<List<SlotResponse>> getSlots(
            @PathVariable UUID doctorId,
            @RequestParam LocalDate date
    ) {
        return ResponseEntity.ok(
                availabilityService.getAvailableSlots(doctorId, date)
        );
    }
}