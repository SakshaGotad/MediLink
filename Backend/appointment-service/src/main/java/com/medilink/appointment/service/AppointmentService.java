package com.medilink.appointment.service;

import java.util.List;
import java.util.UUID;

import com.medilink.appointment.dto.AppointmentResponse;
import com.medilink.appointment.dto.CreateAppointmentRequest;

public interface AppointmentService {
    AppointmentResponse createAppointment(UUID patientId, CreateAppointmentRequest request);

    List<AppointmentResponse> getPatientAppointments(UUID patientId);

    List<AppointmentResponse> getDoctorAppointments(UUID doctorId);

    AppointmentResponse updateStatus(UUID appointmentId, String status);
    
    boolean checkAvailability(UUID doctorId, java.time.LocalDate date, java.time.LocalTime time);

}
