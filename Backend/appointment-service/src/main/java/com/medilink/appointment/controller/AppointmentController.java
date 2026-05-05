package com.medilink.appointment.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medilink.appointment.dto.AppointmentResponse;
import com.medilink.appointment.dto.CreateAppointmentRequest;
import com.medilink.appointment.enums.PaymentStatus;
import com.medilink.appointment.service.AppointmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/create")
    public ResponseEntity<AppointmentResponse> createAppointment(
            @RequestBody @Valid CreateAppointmentRequest request,
            Authentication authentication
    ) {
        UUID patientId = UUID.fromString(authentication.getName());
        System.out.println("Controller - Creating appointment for patientId: " + patientId);

        return ResponseEntity.ok(
                appointmentService.createAppointment(patientId, request)
        );
    }


    @GetMapping("/patient")
    public ResponseEntity<List<AppointmentResponse>> getPatientAppointments(Authentication authentication) {
        UUID patientId = UUID.fromString(authentication.getName());
    System.out.println(patientId + "-----------------------------------------------------");
        return ResponseEntity.ok(
                appointmentService.getPatientAppointments(patientId)
        );
    }

    @GetMapping("/doctor")
    public ResponseEntity<List<AppointmentResponse>> getDoctorAppointments(
            Authentication authentication,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) PaymentStatus paymentStatus
    ) {
        UUID doctorId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(
                appointmentService.getDoctorAppointments(doctorId, date, paymentStatus)
        );
    }

    @GetMapping("/doctor/patient-ids")
    public ResponseEntity<List<UUID>> getDoctorPatientIds(Authentication authentication) {
        UUID doctorId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(appointmentService.getDoctorPatientIds(doctorId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentResponse> updateStatus(
            @PathVariable UUID id,
            @RequestParam String status
    ) {
        return ResponseEntity.ok(
                appointmentService.updateStatus(id, status)
        );
    }

  
}
