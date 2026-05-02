package com.medilink.user.controller;

import com.medilink.user.dto.*;
import com.medilink.user.entity.*;
import com.medilink.user.service.ProfileService;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // Create doctor profile - after registration
    @PostMapping("/doctor/profile")
    public DoctorProfile createDoctorProfile(
            @Valid @RequestBody CreateDoctorProfileRequest request,
            Authentication auth
    ) {
        return profileService.createDoctorProfile(auth, request);
    }

    // create patient profile - after registration.
    @PostMapping("/patient/profile")
    public PatientProfile createPatientProfile(
            @Valid @RequestBody CreatePatientProfileRequest request,
            Authentication auth
    ) {
        return profileService.createPatientProfile(auth, request);
    }

    // get patient profile of currently logged in user (own profile) 
    @GetMapping("/patient/profile")
    public PatientProfile getPatientProfile( Authentication auth){
        return profileService.getPatientProfile(auth);
    }

    // get doctor profile of currently logged in user (own profile) 
    @GetMapping("/doctor/profile")
    public DoctorProfile getDoctorProfile( Authentication auth){
        return profileService.getDoctorProfile(auth);
    }

    // update doctor profile of currently logged in user 
    @PutMapping("/doctor/profile/{doctorId}")
    public DoctorProfile updateDoctorProfile(
        @PathVariable UUID doctorId,
        @Valid @RequestBody CreateDoctorProfileRequest request,
        Authentication auth
    ){
        return profileService.updateDoctorProfile(doctorId, request, auth);
    }

    // update patient profile of currently logged in user 
    @PutMapping("/patient/profile/{patientId}")
    public PatientProfile updatePatientProfile(@PathVariable UUID patientId, @RequestBody CreatePatientProfileRequest request, Authentication auth) {
        return profileService.updatePatientProfile(patientId, request, auth);
    }
}