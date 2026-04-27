package com.medilink.identity.controller;

import com.medilink.identity.dto.*;
import com.medilink.identity.entity.*;
import com.medilink.identity.service.ProfileService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // 👨‍⚕️ Doctor
    @PostMapping("/doctor/profile")
    public DoctorProfile createDoctorProfile(
            @Valid @RequestBody CreateDoctorProfileRequest request,
            Authentication auth
    ) {
        return profileService.createDoctorProfile(auth.getName(), request);
    }

    // 🧑 Patient
    @PostMapping("/patient/profile")
    public PatientProfile createPatientProfile(
            @Valid @RequestBody CreatePatientProfileRequest request,
            Authentication auth
    ) {
        return profileService.createPatientProfile(auth.getName(), request);
    }
}