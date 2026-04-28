package com.medilink.user.entity;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientProfile {

    @Id
    @GeneratedValue
    private UUID id;

    // 🔥 REQUIRED
    private int age;
    private String gender;

    // ⚡ OPTIONAL
    private String phone;
    private String address;
    private String bloodGroup;
    private String medicalHistory;
    private String allergies;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}