package com.medilink.user.entity;

import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorProfile {

    @Id
    @GeneratedValue
    private UUID id;

    // 🔥 REQUIRED
    @NotBlank()
    private String specialization;
    
    private int experience;

    private double fee;
    
    @NotBlank()
    private String qualification;

    @NotBlank()
    private String licenseNumber;

    // ⚡ OPTIONAL
    private String clinicName;
    private String clinicAddress;
    private String bio;
    private String availableDays;
    private String profileImageUrl;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}