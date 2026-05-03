package com.medilink.user.dto;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorResponse {

    private UUID id;              // 🔥 important
    private String name;
    private String email;
    private String specialization;
    private int experience;
    private double fee;
    private String qualification;
    private String clinicName;
}