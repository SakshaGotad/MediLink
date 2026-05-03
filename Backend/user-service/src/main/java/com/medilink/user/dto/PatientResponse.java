package com.medilink.user.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class PatientResponse {
    private UUID id;
    private String name;
    private String email;
    private int age;
    private String gender;
    private String phone;
    private String bloodGroup;
    private String medicalHistory;
    private String allergies;
}
