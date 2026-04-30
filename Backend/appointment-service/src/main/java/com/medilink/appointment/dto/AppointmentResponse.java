package com.medilink.appointment.dto;

import com.medilink.appointment.entity.Appointment;
import com.medilink.appointment.enums.AppointmentStatus;
import com.medilink.appointment.enums.AppointmentType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
public class AppointmentResponse {

    private UUID id;
    private UUID doctorId;
    private UUID patientId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private AppointmentStatus status;
    private AppointmentType type;
    private String notes;
}