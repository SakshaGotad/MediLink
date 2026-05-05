package com.medilink.user.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medilink.user.dto.DoctorResponse;
import com.medilink.user.dto.PatientResponse;
import com.medilink.user.service.DoctorService;
import com.medilink.user.service.ProfileService;
import org.springframework.security.core.Authentication;

@RestController()
@RequestMapping("/api/doctors")
public class DoctorController {
    private final DoctorService doctorService;
    private final ProfileService profileService;

    public DoctorController(DoctorService doctorService, ProfileService profileService) {
        this.doctorService = doctorService;
        this.profileService = profileService;
    }

     @GetMapping()
     public List<DoctorResponse> getAllDoctors(
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String sort
    ) {
        return doctorService.getAllDoctors(specialization, sort);
    }

    @GetMapping("/{id}")
    public DoctorResponse getDoctorById(@PathVariable UUID id) {
        return doctorService.getDoctorById(id);
    }

    @GetMapping("/my-patients")
    public List<PatientResponse> getMyPatients(Authentication auth) {
        UUID doctorId = profileService.getDoctorProfile(auth).getId();
        return doctorService.getPatientsForDoctor(doctorId);
    }
}
