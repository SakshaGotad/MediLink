package com.medilink.user.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDoctorProfileRequest {

    @NotBlank
    private String specialization;

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private Integer experience;

    @NotNull
    private Double fee;

    @NotBlank
    private String qualification;

    @NotBlank
    private String licenseNumber;

    // Optional
    private String clinicName;
    private String clinicAddress;
    private String bio;
}