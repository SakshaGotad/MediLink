package com.medilink.appointment.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class SlotResponse {

    private LocalTime time;
    private int bookedCount;
    private int maxCapacity;
    private boolean available;
}