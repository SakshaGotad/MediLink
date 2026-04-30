package com.medilink.appointment.dto;

import com.medilink.appointment.enums.AppointmentType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class CreateAppointmentRequest {

    @NotNull
    private UUID doctorId;

    @NotNull
    private LocalDate appointmentDate;

    @NotNull
    private LocalTime appointmentTime;

    @NotNull
    private AppointmentType type;

    private String notes;
}