package com.medilink.appointment.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import com.medilink.appointment.enums.PaymentStatus;

import com.medilink.appointment.dto.AppointmentResponse;
import com.medilink.appointment.dto.CreateAppointmentRequest;

public interface AppointmentService {
    AppointmentResponse createAppointment(UUID patientId, CreateAppointmentRequest request);

    List<AppointmentResponse> getPatientAppointments(UUID patientId);

    List<AppointmentResponse> getDoctorAppointments(UUID doctorId, LocalDate date, PaymentStatus paymentStatus);

    AppointmentResponse updateStatus(UUID appointmentId, String status);
    List<UUID> getDoctorPatientIds(UUID doctorId);
}
