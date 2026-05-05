package com.medilink.appointment.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.medilink.appointment.dto.AppointmentResponse;
import com.medilink.appointment.enums.AppointmentStatus;
import com.medilink.appointment.dto.CreateAppointmentRequest;
import com.medilink.appointment.entity.Appointment;
import com.medilink.appointment.repository.AppointmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    @Override
    public AppointmentResponse createAppointment(UUID patientId, CreateAppointmentRequest request) {
 
        // 1.1: Date/Time must be in future
        LocalDateTime appointmentDateTime = LocalDateTime.of(request.getAppointmentDate(), request.getAppointmentTime());
        if (appointmentDateTime.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Appointment must be in the future");
        }

        // 🔴 Step 1: Check slot availability
        boolean exists = appointmentRepository
                .existsByDoctorIdAndAppointmentDateAndAppointmentTime(
                        request.getDoctorId(),
                        request.getAppointmentDate(),
                        request.getAppointmentTime());

        if (exists) {
            throw new RuntimeException("Slot already booked");
        }

        // 🟢 Step 2: Create entity
        Appointment appointment = Appointment.builder()
                .patientId(patientId)
                .doctorId(request.getDoctorId())
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .type(request.getType())
                .notes(request.getNotes())
                .build();

        // 🟢 Step 3: Save
        Appointment saved = appointmentRepository.save(appointment);

        return mapToResponse(saved);
    }

    @Override
    public List<AppointmentResponse> getPatientAppointments(UUID patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getDoctorAppointments(UUID doctorId, LocalDate date) {
        List<Appointment> appointments;
        if (date != null) {
            appointments = appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, date);
        } else {
            appointments = appointmentRepository.findByDoctorId(doctorId);
        }
        
        return appointments.stream()
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
    public List<UUID> getDoctorPatientIds(UUID doctorId) {
        return appointmentRepository.findDistinctPatientIdsByDoctorId(doctorId);
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