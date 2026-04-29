package com.medilink.user.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medilink.user.dto.DoctorResponse;
import com.medilink.user.service.DoctorService;

@RestController()
@RequestMapping("/api/doctors")
public class DoctorController {
    private final DoctorService doctorService;
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
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
}
