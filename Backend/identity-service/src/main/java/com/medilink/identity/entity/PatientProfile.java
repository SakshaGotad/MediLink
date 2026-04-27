package com.medilink.identity.entity;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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