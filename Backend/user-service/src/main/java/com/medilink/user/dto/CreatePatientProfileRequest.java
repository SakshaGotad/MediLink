package com.medilink.user.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePatientProfileRequest {

    @NotNull
    private Integer age;

    @NotBlank
    private String gender;

    // Optional
    private String phone;
    private String address;
    private String bloodGroup;
    private String medicalHistory;
}