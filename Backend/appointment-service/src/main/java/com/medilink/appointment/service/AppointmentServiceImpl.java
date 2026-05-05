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
import com.medilink.appointment.dto.CreatePaymentRequest;
import com.medilink.appointment.entity.Appointment;
import com.medilink.appointment.enums.PaymentStatus;
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
        System.out.println("Saving appointment for patientId: " + patientId + ", doctorId: " + appointment.getDoctorId());
        Appointment saved = appointmentRepository.save(appointment);

        //  Call Payment Service
        CreatePaymentRequest paymentRequest = new CreatePaymentRequest();
        paymentRequest.setAppointmentId(saved.getId());
        paymentRequest.setPatientId(patientId);

        return mapToResponse(saved);

    }

    @Override
    public List<AppointmentResponse> getPatientAppointments(UUID patientId) {
        System.out.println("Querying appointments for patientId: " + patientId);
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        System.out.println("Found " + appointments.size() + " appointments");
        return appointments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getDoctorAppointments(UUID doctorId, LocalDate date, PaymentStatus paymentStatus) {
        System.out.println("Querying appointments for doctorId: " + doctorId + ", date: " + date + ", paymentStatus: " + paymentStatus);
        List<Appointment> appointments;
        
        if (date != null && paymentStatus != null) {
            appointments = appointmentRepository.findByDoctorIdAndAppointmentDateAndPaymentStatus(doctorId, date, paymentStatus);
        } else if (date != null) {
            appointments = appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, date);
        } else if (paymentStatus != null) {
            appointments = appointmentRepository.findByDoctorIdAndPaymentStatus(doctorId, paymentStatus);
        } else {
            appointments = appointmentRepository.findByDoctorId(doctorId);
        }
        System.out.println("Found " + appointments.size() + " appointments");
        
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
                .paymentStatus(appointment.getPaymentStatus())
                .notes(appointment.getNotes())
                .build();
    }
}