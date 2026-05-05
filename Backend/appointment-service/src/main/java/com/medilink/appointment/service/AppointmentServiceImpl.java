package com.medilink.appointment.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.medilink.appointment.dto.AppointmentResponse;
import com.medilink.appointment.enums.AppointmentStatus;
import com.medilink.appointment.dto.CreateAppointmentRequest;
import com.medilink.appointment.dto.CreatePaymentRequest;
import com.medilink.appointment.entity.Appointment;
import com.medilink.appointment.repository.AppointmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final com.medilink.appointment.client.PaymentClient paymentClient;

    @Override
    public AppointmentResponse createAppointment(UUID patientId, CreateAppointmentRequest request) {

        // 🔴 Step 1: Validations
        
        // 1.1: Date/Time must be in future
        LocalDateTime appointmentDateTime = LocalDateTime.of(request.getAppointmentDate(), request.getAppointmentTime());
        if (appointmentDateTime.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Appointment must be in the future");
        }

        // 1.3: Check doctor availability
        boolean doctorSlotTaken = appointmentRepository
                .existsByDoctorIdAndAppointmentDateAndAppointmentTime(
                        request.getDoctorId(),
                        request.getAppointmentDate(),
                        request.getAppointmentTime());

        if (doctorSlotTaken) {
            throw new RuntimeException("Doctor is already booked for this slot");
        }

        // 1.4: Check patient availability
        boolean patientSlotTaken = appointmentRepository
                .existsByPatientIdAndAppointmentDateAndAppointmentTime(
                        patientId,
                        request.getAppointmentDate(),
                        request.getAppointmentTime());
        
        if (patientSlotTaken) {
            throw new RuntimeException("Patient already has an appointment at this time");
        }

        // 🟢 Step 2: Create entity
        Appointment appointment = Appointment.builder()
                .patientId(patientId)
                .doctorId(request.getDoctorId())
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .type(request.getType())
                .notes(request.getNotes())
                .status(AppointmentStatus.PENDING)
                .build();

        // 🟢 Step 3: Save
        Appointment saved = appointmentRepository.save(appointment);

        // 🔵 Step 4: Call Payment Service
        CreatePaymentRequest paymentRequest = new CreatePaymentRequest();
        paymentRequest.setAppointmentId(saved.getId());
        paymentRequest.setPatientId(patientId);
        paymentRequest.setAmount(500L); // Default amount for now

        paymentClient.createPayment(paymentRequest);

        return mapToResponse(saved);

    }

    @Override
    public List<AppointmentResponse> getPatientAppointments(UUID patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getDoctorAppointments(UUID doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentResponse updateStatus(UUID appointmentId, String status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.valueOf(status.toUpperCase()));
        Appointment updated = appointmentRepository.save(appointment);

        return mapToResponse(updated);
    }

    @Override
    public boolean checkAvailability(UUID doctorId, java.time.LocalDate date, java.time.LocalTime time) {
        LocalDateTime appointmentDateTime = LocalDateTime.of(date, time);
        if (appointmentDateTime.isBefore(LocalDateTime.now())) {
            return false;
        }
        return !appointmentRepository.existsByDoctorIdAndAppointmentDateAndAppointmentTime(doctorId, date, time);
    }

    private AppointmentResponse mapToResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .doctorId(appointment.getDoctorId())
                .patientId(appointment.getPatientId())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .status(appointment.getStatus())
                .type(appointment.getType())
                .notes(appointment.getNotes())
                .build();
    }
}