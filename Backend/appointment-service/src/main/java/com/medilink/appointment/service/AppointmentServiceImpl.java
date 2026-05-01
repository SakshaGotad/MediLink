package com.medilink.appointment.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.medilink.appointment.dto.AppointmentResponse;
import com.medilink.appointment.enums.AppointmentStatus;
import com.medilink.appointment.dto.CreateAppointmentRequest;
import com.medilink.appointment.entity.Appointment;
import com.medilink.appointment.entity.DoctorAvailability;
import com.medilink.appointment.repository.AppointmentRepository;
import com.medilink.appointment.repository.DoctorAvailabilityRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorAvailabilityRepository doctorAvailabilityRepository;

    @Transactional
    @Override
    public AppointmentResponse createAppointment(UUID patientId, CreateAppointmentRequest request) {

        List<DoctorAvailability> availabilities = doctorAvailabilityRepository.findByDoctorIdAndDate(
                request.getDoctorId(),
                request.getAppointmentDate());

        if (availabilities.isEmpty()) {
        throw new RuntimeException("Doctor is not available on this date");
    }
 DoctorAvailability matched = availabilities.stream()
            .filter(a ->
                    !request.getAppointmentTime().isBefore(a.getStartTime()) &&
                    request.getAppointmentTime().isBefore(a.getEndTime())
            )
            .findFirst()
            .orElseThrow(() ->
                    new RuntimeException("Selected time is outside doctor availability")
            );

            long count = appointmentRepository
            .countWithLock(
                    request.getDoctorId(),
                    request.getAppointmentDate(),
                    request.getAppointmentTime()
            );

             if (count >= matched.getMaxPatientsPerSlot()) {
        throw new RuntimeException("Slot is full");
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