package com.medilink.appointment.dto;

import java.time.LocalTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SlotResponse {
    private LocalTime time;
    private boolean available;
}
